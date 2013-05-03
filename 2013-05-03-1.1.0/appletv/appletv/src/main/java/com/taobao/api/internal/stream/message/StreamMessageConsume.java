package com.taobao.api.internal.stream.message;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.api.internal.stream.StreamConstants;
import com.taobao.api.internal.stream.TopCometStreamImpl.ControlThread;
import com.taobao.api.internal.stream.connect.HttpResponse;
import com.taobao.api.internal.util.StringUtils;
/**
 * 
 * @author zhenzi
 * 2011-8-12 上午10:05:00
 */
public class StreamMessageConsume{
	private static final Logger log = Logger.getLogger(StreamMessageConsume.class);
	private static final Pattern pattern = Pattern.compile("\\{\"packet\":\\{\"code\":(\\d+)(,\"msg\":(.+))?\\}\\}");
	/*
	 * 用于读取消息数据
	 */
	private HttpResponse response;
	/*
	 * 消息处理线程池 
	 */
	private StreamMsgConsumeFactory msgConsumeFactory;
	//用于标记消息通道知否还可用
	private boolean streamAlive = true;
	/*
	 * isv提供的消息监听器 
	 */
	private TopCometMessageListener msgListener;
	//用于与Controlthread交互，控制连接的管理
	private Condition controlCondition;
	private Lock lock;
	private ControlThread ct;
	/**
	 * 因为在服务端kickoff的时候可能会发送多个server kickoff。
	 * 尤其是在消息量大的情况下。这里做个控制，对于一个连接，
	 * 给isv的只有一个serverkickoff消息
	 */
	private boolean isServerKickOffSend = false;
	
	public StreamMessageConsume(StreamMsgConsumeFactory msgConsumeFactory,
			HttpResponse response,TopCometMessageListener msgListener, ControlThread ct) {
		this.msgConsumeFactory = msgConsumeFactory;
		this.response = response;
		this.msgListener = msgListener;
		this.controlCondition = ct.getControlCondition();
		this.lock = ct.getLock();
		this.ct = ct;
	}
	/**
	 * 给consume thread提供的读取消息的方法。
	 * @throws IOException
	 */
	public void nextMsg() throws IOException {
		if(!streamAlive){
			throw new IOException("Stream closed");
		}
        try {
            String line = response.getMsg();
            if(line == null){//正常读到流的末尾了。
            	streamAlive = false;
            	response.close();
            	return;
            }
            msgConsumeFactory.consume(new StreamEvent(line));
        } catch (IOException e) {//这个时候抛出：SocketTimeoutException
            response.close();
            streamAlive = false;
            throw e;
        }catch(RejectedExecutionException rejectException){
        	log.error("Message consume thread pool is full:", rejectException);
        }catch(NullPointerException npe){
        	log.error("Null point exception:", npe);
        }
    }
	public boolean isAlive(){
		return streamAlive;
	}
	/**
	 * 用于解析消息
	 * @author zhenzi
	 *
	 * 2012-8-12 下午2:21:27
	 */
	private class StreamEvent implements Runnable{
		private String msg;
		public StreamEvent(String msg){
			this.msg = msg;
		}
		public void run() {
			parseLine(msg);
		}
		
	}
	/**
	 * 解析消息，并且调用message listener的对应方法。
	 * @param msg
	 */
	private void parseLine(String msg){
		if(!StringUtils.isEmpty(msg)){
			try{
				Matcher matcher = pattern.matcher(msg);
				if (matcher.find()) {
					String code = matcher.group(1);
					if(StreamConstants.NEW_MESSAGE.equals(code)){
						msgListener.onReceiveMsg(matcher.group(3));
					}else if(StreamConstants.HEAT_BEAT.equals(code)){
						msgListener.onHeartBeat();
					}else if(StreamConstants.CONNECT_REACH_MAX_TIME.equals(code)){
						weakUp(code);
					}else if(StreamConstants.DISCARD_MESSAGE.equals(code)){
						msgListener.onDiscardMsg(matcher.group(3));
					}else if(StreamConstants.SERVER_DEPLOY.equals(code)){
						msgListener.onServerUpgrade(matcher.group(3));
						weakUp(code);
					}else if(StreamConstants.SERVER_REHASH.equals(code)){
						msgListener.onServerRehash();
						weakUp(code);
					}else if(StreamConstants.CLIENT_KICKOFF.equals(code)){
						msgListener.onClientKickOff();
						weakUp(code);
					}else if(StreamConstants.SERVER_KICKOFF.equals(code)){
//						msgListener.onServerKickOff();
						weakUp(code);
					}else if(StreamConstants.CONNECT_SUCCESS.equals(code)){
						msgListener.onConnectMsg(matcher.group(3));
					}else {
						msgListener.onOtherMsg(matcher.group(3));
					}
				}
			}catch(Exception e){
				msgListener.onException(e);
			}
		}
	}
	private void weakUp(String code){
		try{
			lock.lock();
			//如果不是服务端踢出
			if(!StreamConstants.SERVER_KICKOFF.equals(code)){
				ct.setServerRespCode(code);
				controlCondition.signalAll();
			}else if(!isServerKickOffSend){//如果是服务端踢出，但是第一次处理这个code
				isServerKickOffSend = true;
				ct.setServerRespCode(code);
				msgListener.onServerKickOff();//对于服务端踢出这个消息在这里特殊处理一下
				controlCondition.signalAll();
			}else {//服务端踢出，但是第二次处理这个code，则忽略
				controlCondition.signalAll();
			}
		}catch(Exception e){
			//ignore
		}finally{
			lock.unlock();
		}
	}
	/**
	 * 用于关闭整个数据通道
	 * @throws IOException
	 */
	public void close() throws IOException {
		streamAlive = false;
		response.close();
	}
}

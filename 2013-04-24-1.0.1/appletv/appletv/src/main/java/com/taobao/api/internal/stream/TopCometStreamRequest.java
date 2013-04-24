package com.taobao.api.internal.stream;

import java.util.Map;

import com.taobao.api.internal.stream.connect.ConnectionLifeCycleListener;
import com.taobao.api.internal.stream.message.TopCometMessageListener;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.internal.util.TaobaoUtils;

/**
 * 请求参数
 * @author zhenzi
 * 2012-1-4 上午11:34:35
 */
public class TopCometStreamRequest {
	private String appkey;
	private String secret;
	private String userId;
	private String connectId;
	/**
	 * 其他业务参数
	 */
	private Map<String,String> otherParam;
	private ConnectionLifeCycleListener connectListener;
	private TopCometMessageListener msgListener;
	
	public TopCometStreamRequest(String appkey,String secret,String userId,String connectId){
		if(StringUtils.isEmpty(appkey)){
			throw new RuntimeException("appkey is null");
		}
		if(StringUtils.isEmpty(secret)){
			throw new RuntimeException("secret is null");
		}
		if(!StringUtils.isEmpty(userId)){
			try{
				Long.parseLong(userId);
			}catch(Exception e){
				throw new RuntimeException("userid must a number type");
			}
		}
		if(StringUtils.isEmpty(connectId)){
			this.connectId = TaobaoUtils.getLocalNetWorkIp();
		}else{
			this.connectId = connectId;
		}
		this.appkey = appkey;
		this.secret = secret;
		this.userId = userId;
		
	}
	public String getAppkey() {
		return appkey;
	}
	public String getSecret() {
		return secret;
	}
	public String getUserId() {
		return userId;
	}
	
	public String getConnectId() {
		return connectId;
	}
	public ConnectionLifeCycleListener getConnectListener() {
		return connectListener;
	}
	public void setConnectListener(ConnectionLifeCycleListener connectListener) {
		this.connectListener = connectListener;
	}
	public TopCometMessageListener getMsgListener() {
		return msgListener;
	}
	public void setMsgListener(TopCometMessageListener msgListener) {
		this.msgListener = msgListener;
	}
	
	public Map<String, String> getOtherParam() {
		return otherParam;
	}
	public void setOtherParam(Map<String, String> otherParam) {
		this.otherParam = otherParam;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appkey == null) ? 0 : appkey.hashCode());
		result = prime * result
				+ ((connectId == null) ? 0 : connectId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopCometStreamRequest other = (TopCometStreamRequest) obj;
		if (appkey == null) {
			if (other.appkey != null)
				return false;
		} else if (!appkey.equals(other.appkey))
			return false;
		if (connectId == null) {
			if (other.connectId != null)
				return false;
		} else if (!connectId.equals(other.connectId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}

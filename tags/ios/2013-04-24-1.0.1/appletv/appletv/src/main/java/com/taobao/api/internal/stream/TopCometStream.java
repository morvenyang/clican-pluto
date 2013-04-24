package com.taobao.api.internal.stream;

import com.taobao.api.internal.stream.connect.ConnectionLifeCycleListener;
import com.taobao.api.internal.stream.message.TopCometMessageListener;

/**
 * 
 * @author zhenzi
 * 2011-8-9 上午09:34:24
 */
public interface TopCometStream {
	public void setConnectionListener(ConnectionLifeCycleListener connectionLifeCycleListener);
	public void setMessageListener(TopCometMessageListener cometMessageListener);
	public void start();
	public void stop();
	/**
	 * 在运行期添加新的长连接
	 * @param newClient
	 */
	public void addNewStreamClient(TopCometStreamRequest newClient);
}

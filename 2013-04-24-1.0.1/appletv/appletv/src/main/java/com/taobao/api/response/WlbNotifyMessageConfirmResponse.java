package com.taobao.api.response;

import java.util.Date;
import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.wlb.notify.message.confirm response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class WlbNotifyMessageConfirmResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2675417485485564778L;

	/** 
	 * 物流宝消息确认时间
	 */
	@ApiField("gmt_modified")
	private Date gmtModified;

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	public Date getGmtModified( ) {
		return this.gmtModified;
	}

}

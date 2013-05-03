package com.taobao.api.response;

import java.util.Date;
import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.wlb.out.inventory.change.notify response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class WlbOutInventoryChangeNotifyResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4527113977681683884L;

	/** 
	 * 库存变化通知成功时间
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

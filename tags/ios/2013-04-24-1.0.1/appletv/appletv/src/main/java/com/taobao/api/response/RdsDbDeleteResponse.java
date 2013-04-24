package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.RdsDbInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.rds.db.delete response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class RdsDbDeleteResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7324486628621634876L;

	/** 
	 * 删除数据库，返回结果对象
	 */
	@ApiField("rds_db_info")
	private RdsDbInfo rdsDbInfo;

	public void setRdsDbInfo(RdsDbInfo rdsDbInfo) {
		this.rdsDbInfo = rdsDbInfo;
	}
	public RdsDbInfo getRdsDbInfo( ) {
		return this.rdsDbInfo;
	}

}

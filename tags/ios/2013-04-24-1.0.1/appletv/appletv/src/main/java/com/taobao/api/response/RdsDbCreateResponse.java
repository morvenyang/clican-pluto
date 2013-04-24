package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.RdsDbInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.rds.db.create response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class RdsDbCreateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4288767878712931438L;

	/** 
	 * rds创建、查询、删除db返回结果的数据结构
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

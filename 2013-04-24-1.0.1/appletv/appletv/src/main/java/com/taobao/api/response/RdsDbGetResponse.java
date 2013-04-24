package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.RdsDbInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.rds.db.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class RdsDbGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5193359663726144493L;

	/** 
	 * 数据库查询返回数据结构
	 */
	@ApiListField("rds_db_infos")
	@ApiField("rds_db_info")
	private List<RdsDbInfo> rdsDbInfos;

	public void setRdsDbInfos(List<RdsDbInfo> rdsDbInfos) {
		this.rdsDbInfos = rdsDbInfos;
	}
	public List<RdsDbInfo> getRdsDbInfos( ) {
		return this.rdsDbInfos;
	}

}

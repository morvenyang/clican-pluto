package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.CreativeRecord;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creatives.record.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativesRecordGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7328917472653331122L;

	/** 
	 * 创意修改对象列表
	 */
	@ApiListField("creativerecords")
	@ApiField("creative_record")
	private List<CreativeRecord> creativerecords;

	public void setCreativerecords(List<CreativeRecord> creativerecords) {
		this.creativerecords = creativerecords;
	}
	public List<CreativeRecord> getCreativerecords( ) {
		return this.creativerecords;
	}

}

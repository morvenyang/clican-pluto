package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.INWordBase;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.insight.wordsbase.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaInsightWordsbaseGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1168297224133484686L;

	/** 
	 * 词基础数据对象列表
	 */
	@ApiListField("in_word_bases")
	@ApiField("i_n_word_base")
	private List<INWordBase> inWordBases;

	public void setInWordBases(List<INWordBase> inWordBases) {
		this.inWordBases = inWordBases;
	}
	public List<INWordBase> getInWordBases( ) {
		return this.inWordBases;
	}

}

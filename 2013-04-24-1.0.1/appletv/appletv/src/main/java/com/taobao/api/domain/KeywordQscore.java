package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 关键词质量得分
 *
 * @author auto create
 * @since 1.0, null
 */
public class KeywordQscore extends TaobaoObject {

	private static final long serialVersionUID = 1337569293796684345L;

	/**
	 * 推广组id
	 */
	@ApiField("adgroup_id")
	private Long adgroupId;

	/**
	 * 推广计划id
	 */
	@ApiField("campaign_id")
	private Long campaignId;

	/**
	 * 关键词id
	 */
	@ApiField("keyword_id")
	private Long keywordId;

	/**
	 * 主人昵称
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 质量得分
	 */
	@ApiField("qscore")
	private String qscore;

	/**
	 * 关键词
	 */
	@ApiField("word")
	private String word;

	public Long getAdgroupId() {
		return this.adgroupId;
	}
	public void setAdgroupId(Long adgroupId) {
		this.adgroupId = adgroupId;
	}

	public Long getCampaignId() {
		return this.campaignId;
	}
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getKeywordId() {
		return this.keywordId;
	}
	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}

	public String getNick() {
		return this.nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getQscore() {
		return this.qscore;
	}
	public void setQscore(String qscore) {
		this.qscore = qscore;
	}

	public String getWord() {
		return this.word;
	}
	public void setWord(String word) {
		this.word = word;
	}

}

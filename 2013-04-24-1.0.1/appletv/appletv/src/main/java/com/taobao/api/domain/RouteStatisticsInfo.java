package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 线路的统计信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RouteStatisticsInfo extends TaobaoObject {

	private static final long serialVersionUID = 1799829631752487691L;

	/**
	 * 评价数
	 */
	@ApiField("evaluation_count")
	private Long evaluationCount;

	/**
	 * 评价分值
	 */
	@ApiField("evaluation_score")
	private String evaluationScore;

	/**
	 * 发货网点个数
	 */
	@ApiField("from_network_count")
	private Long fromNetworkCount;

	/**
	 * 目的地网点个数
	 */
	@ApiField("to_network_count")
	private Long toNetworkCount;

	/**
	 * 干线上的下单量
	 */
	@ApiField("trunk_route_order_count")
	private Long trunkRouteOrderCount;

	public Long getEvaluationCount() {
		return this.evaluationCount;
	}
	public void setEvaluationCount(Long evaluationCount) {
		this.evaluationCount = evaluationCount;
	}

	public String getEvaluationScore() {
		return this.evaluationScore;
	}
	public void setEvaluationScore(String evaluationScore) {
		this.evaluationScore = evaluationScore;
	}

	public Long getFromNetworkCount() {
		return this.fromNetworkCount;
	}
	public void setFromNetworkCount(Long fromNetworkCount) {
		this.fromNetworkCount = fromNetworkCount;
	}

	public Long getToNetworkCount() {
		return this.toNetworkCount;
	}
	public void setToNetworkCount(Long toNetworkCount) {
		this.toNetworkCount = toNetworkCount;
	}

	public Long getTrunkRouteOrderCount() {
		return this.trunkRouteOrderCount;
	}
	public void setTrunkRouteOrderCount(Long trunkRouteOrderCount) {
		this.trunkRouteOrderCount = trunkRouteOrderCount;
	}

}

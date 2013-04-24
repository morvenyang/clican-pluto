package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.AlibabaLogisticsRouteQueryResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: alibaba.logistics.route.query request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class AlibabaLogisticsRouteQueryRequest implements TaobaoRequest<AlibabaLogisticsRouteQueryResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 是否是空运的条件
	 */
	private Boolean airTransport;

	/** 
	* 过滤线路的公司塞选条件
	 */
	private String corpList;

	/** 
	* 目的地id，可以是市和区
	 */
	private Long endAreaId;

	/** 
	* 是否合并线路
	 */
	private Boolean mergeRoute;

	/** 
	* 当前第几页
	 */
	private Long pageIndex;

	/** 
	* 每页显示的线路数
	 */
	private Long pageSize;

	/** 
	* 线路是否具有代收货款服务
	 */
	private Boolean showCods;

	/** 
	* 线路是否显示保障服务
	 */
	private Boolean showSpecials;

	/** 
	* 是否在线路中显示评价和网点信息
	 */
	private Boolean showStatisticsInfo;

	/** 
	* 线路排序方式。具体值如下,precise：精确匹配，corp：公司,wpa：重物价格升序,wpd：重物价格降序,vpa：体积价格升序,vpd：体积价格降序,trtid：运输时效降序,trtia：运输时效升序,corpLevel：公司级别，品牌>集市,evalScore：评价分数,routeTop：线路是否置顶,orderCount：下单量多少排序,special：保障服务优先排序。
	 */
	private String sortType;

	/** 
	* 数据来源，默认开放部分物流公司。
	 */
	private String source;

	/** 
	* 起始地id，可以是市和区
	 */
	private Long startAreaId;

	/** 
	* 是否查询总的查询条件的公司数目
	 */
	private Boolean summaryTotalCorps;

	/** 
	* 是否统计对应公司的线路数
	 */
	private Boolean summeryByCorp;

	/** 
	* 如果查找不到指定地区的线路，是否对地址进行上翻。如杭州市滨江区的地址呗翻转为杭州市。
	 */
	private Boolean turnLevel;

	public void setAirTransport(Boolean airTransport) {
		this.airTransport = airTransport;
	}
	public Boolean getAirTransport() {
		return this.airTransport;
	}

	public void setCorpList(String corpList) {
		this.corpList = corpList;
	}
	public String getCorpList() {
		return this.corpList;
	}

	public void setEndAreaId(Long endAreaId) {
		this.endAreaId = endAreaId;
	}
	public Long getEndAreaId() {
		return this.endAreaId;
	}

	public void setMergeRoute(Boolean mergeRoute) {
		this.mergeRoute = mergeRoute;
	}
	public Boolean getMergeRoute() {
		return this.mergeRoute;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Long getPageIndex() {
		return this.pageIndex;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageSize() {
		return this.pageSize;
	}

	public void setShowCods(Boolean showCods) {
		this.showCods = showCods;
	}
	public Boolean getShowCods() {
		return this.showCods;
	}

	public void setShowSpecials(Boolean showSpecials) {
		this.showSpecials = showSpecials;
	}
	public Boolean getShowSpecials() {
		return this.showSpecials;
	}

	public void setShowStatisticsInfo(Boolean showStatisticsInfo) {
		this.showStatisticsInfo = showStatisticsInfo;
	}
	public Boolean getShowStatisticsInfo() {
		return this.showStatisticsInfo;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getSortType() {
		return this.sortType;
	}

	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return this.source;
	}

	public void setStartAreaId(Long startAreaId) {
		this.startAreaId = startAreaId;
	}
	public Long getStartAreaId() {
		return this.startAreaId;
	}

	public void setSummaryTotalCorps(Boolean summaryTotalCorps) {
		this.summaryTotalCorps = summaryTotalCorps;
	}
	public Boolean getSummaryTotalCorps() {
		return this.summaryTotalCorps;
	}

	public void setSummeryByCorp(Boolean summeryByCorp) {
		this.summeryByCorp = summeryByCorp;
	}
	public Boolean getSummeryByCorp() {
		return this.summeryByCorp;
	}

	public void setTurnLevel(Boolean turnLevel) {
		this.turnLevel = turnLevel;
	}
	public Boolean getTurnLevel() {
		return this.turnLevel;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "alibaba.logistics.route.query";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("air_transport", this.airTransport);
		txtParams.put("corp_list", this.corpList);
		txtParams.put("end_area_id", this.endAreaId);
		txtParams.put("merge_route", this.mergeRoute);
		txtParams.put("page_index", this.pageIndex);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("show_cods", this.showCods);
		txtParams.put("show_specials", this.showSpecials);
		txtParams.put("show_statistics_info", this.showStatisticsInfo);
		txtParams.put("sort_type", this.sortType);
		txtParams.put("source", this.source);
		txtParams.put("start_area_id", this.startAreaId);
		txtParams.put("summary_total_corps", this.summaryTotalCorps);
		txtParams.put("summery_by_corp", this.summeryByCorp);
		txtParams.put("turn_level", this.turnLevel);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new TaobaoHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<AlibabaLogisticsRouteQueryResponse> getResponseClass() {
		return AlibabaLogisticsRouteQueryResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxListSize(corpList,15,"corpList");
		RequestCheckUtils.checkNotEmpty(endAreaId,"endAreaId");
		RequestCheckUtils.checkNotEmpty(pageIndex,"pageIndex");
		RequestCheckUtils.checkMaxValue(pageIndex,100000L,"pageIndex");
		RequestCheckUtils.checkMinValue(pageIndex,1L,"pageIndex");
		RequestCheckUtils.checkNotEmpty(pageSize,"pageSize");
		RequestCheckUtils.checkMaxValue(pageSize,100L,"pageSize");
		RequestCheckUtils.checkMinValue(pageSize,1L,"pageSize");
		RequestCheckUtils.checkNotEmpty(startAreaId,"startAreaId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}

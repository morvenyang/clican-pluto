package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 线路的分页信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RouteAlpPage extends TaobaoObject {

	private static final long serialVersionUID = 3856274645444541465L;

	/**
	 * 线路的列表
	 */
	@ApiListField("datas")
	@ApiField("complex_logistics_route")
	private List<ComplexLogisticsRoute> datas;

	/**
	 * 结束记录数
	 */
	@ApiField("end")
	private Long end;

	/**
	 * 总页数
	 */
	@ApiField("page_count")
	private Long pageCount;

	/**
	 * 当前页码
	 */
	@ApiField("page_index")
	private Long pageIndex;

	/**
	 * 每页条数
	 */
	@ApiField("page_size")
	private Long pageSize;

	/**
	 * 总的记录数
	 */
	@ApiField("record_count")
	private Long recordCount;

	/**
	 * 开始记录数
	 */
	@ApiField("start")
	private Long start;

	public List<ComplexLogisticsRoute> getDatas() {
		return this.datas;
	}
	public void setDatas(List<ComplexLogisticsRoute> datas) {
		this.datas = datas;
	}

	public Long getEnd() {
		return this.end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}

	public Long getPageCount() {
		return this.pageCount;
	}
	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	public Long getPageIndex() {
		return this.pageIndex;
	}
	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Long getPageSize() {
		return this.pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getRecordCount() {
		return this.recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	public Long getStart() {
		return this.start;
	}
	public void setStart(Long start) {
		this.start = start;
	}

}

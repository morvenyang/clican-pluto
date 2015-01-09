package com.chinatelecom.xysq.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModel;

import com.chinatelecom.xysq.api.Selectable;

@SuppressWarnings("unchecked")
public abstract class PageListDataModel<T> extends DataModel {

	private int pageSize;

	protected int getPageSize() {
		return this.pageSize;
	}

	protected int rowIndex;

	private PageList<T> pageQueryResult;

	private Set<Long> selectedIds;

	/**
	 * 创建一个datamodel显示数据，每一页指定的行数
	 */

	public PageListDataModel(int pageSize) {

		super();

		this.pageSize = pageSize;

		this.rowIndex = 0;

		this.pageQueryResult = null;
		
		this.selectedIds = new HashSet<Long>();

	}

	

	/**
	 * 数据是通过一个回调fetchData方法获取，而不是明确指定一个列表
	 */

	public void setWrappedData(Object o) {
		if (o instanceof PageList) {
			this.pageQueryResult = (PageList<T>) o;
		} else {
			throw new UnsupportedOperationException("setWrappedData");
		}
	}

	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * 指定的“当前行“在DataSet中.请注意，UIData组件会重复调用这个方法通过getRowData获取使用表中的对象！
	 */
	public void setRowIndex(int index) {
		if (index == -1) {
			return;
		}
		rowIndex = index;
	}

	/**
	 * 返回的总数据集大小（不只是的行数在当前页面！ ） 。
	 */
	public int getRowCount() {
		return getPage().getMaxCount();
	}

	/**
	 * 返回DataPage对象;如果目前还没有fetchPage一个。请注意，这并不保证datapage返回包含当前rowIndex行;
	 * 见getRowData 。
	 */
	public PageList<T> getPage() {
		if (pageQueryResult != null) {
			return pageQueryResult;
		}
		int rowIndex = getRowIndex();
		int startRow = rowIndex;
		if (rowIndex == -1) {
			// even when no row is selected, we still need a page
			// object so that we know the amount of data available.
			startRow = 0;
		} // invoke method on enclosing class
		if (pageQueryResult == null) {
			pageQueryResult = fetchPageByRowIndex(startRow, getPageSize());
		}
		return pageQueryResult;
	}

	/**
	 * 返回相应的对象到当前rowIndex 。如果目前DataPage对象缓存不包括该指数则通过fetchPage找到适当的页面。
	 */
	public Object getRowData() {
		if (rowIndex < 0) {
			throw new IllegalArgumentException(
					" Invalid rowIndex for PagedListDataModel; not within page ");
		} // ensure page exists; if rowIndex is beyond dataset size, then
			// we should still get back a DataPage object with the dataset size
			// in it
		if (pageQueryResult == null) {
			pageQueryResult = fetchPageByRowIndex(rowIndex, getPageSize());
		}
		int datasetSize = pageQueryResult.getMaxCount();
		int startRow = (pageQueryResult.getPage() - 1)
				* pageQueryResult.getPageSize();
		int nRows = pageQueryResult.getPageSize();
		int endRow = startRow + nRows;
		if (rowIndex >= datasetSize) {
			throw new IllegalArgumentException(" Invalid rowIndex ");
		}
		if (rowIndex < startRow) {
			pageQueryResult = fetchPageByRowIndex(rowIndex, getPageSize());
			startRow = (pageQueryResult.getPage() - 1)
					* pageQueryResult.getPageSize();
		} else if (rowIndex >= endRow) {
			pageQueryResult = fetchPageByRowIndex(rowIndex, getPageSize());
			startRow = (pageQueryResult.getPage() - 1)
					* pageQueryResult.getPageSize();
		}
		if (pageQueryResult.size() == 0 || pageQueryResult.size() < rowIndex) {
			return null;
		} else {
			return pageQueryResult.get(rowIndex);
		}
	}

	public Object getWrappedData() {
		return this.pageQueryResult;
	}

	/**
	 * 如果rowIndex的值在DataSet中，返回真；请注意，它可能是匹配一行而不是当前DataPage缓存；
	 * 如果是的话当getRowData调用请求页面也会调用fetchData方法。
	 */
	public boolean isRowAvailable() {
		PageList<T> pageQueryResult = getPage();
		if (pageQueryResult == null) {
			return false;
		}
		int rowIndex = getRowIndex();
		if (rowIndex < 0) {
			return false;
		} else if (rowIndex >= pageQueryResult.getMaxCount()) {
			return false;
		} else {
			return true;
		}
	}

	public PageList<T> fetchPageByRowIndex(int startRow, int pageSize) {
		List<T> dataList = null;
		if(pageQueryResult!=null){
			dataList=pageQueryResult.getDataList();
			for (T t : dataList) {
				if (t instanceof Selectable) {
					if(((Selectable)t).isSelected()){
						this.selectedIds.add(((Selectable)t).getId());
					}else{
						this.selectedIds.remove(((Selectable)t).getId());
					}
				}
			}
		}
		PageList<T> pageList = fetchPage(startRow / pageSize + 1, pageSize);
		dataList = pageList.getDataList();
		for (T t : dataList) {
			if (t instanceof Selectable) {
				if(this.selectedIds.contains(((Selectable) t).getId())){
					((Selectable)t).setSelected(true);
				}
			}
		}
		return pageList;
	}

	/**
	 * 受管bean必须实现此方法
	 */
	public abstract PageList<T> fetchPage(int page, int pageSize);

	/**
	 * 进行删除等操作后会立即改变列表项并且返回列表页的，请调用此方法，用于刷新列表。
	 */
	public void refresh() {
		if (this.pageQueryResult != null) {
			this.pageQueryResult = null;
			getPage();
		}
	}
	
	public Set<Long> getSelectedIds(){
		List<T> dataList = null;
		if(pageQueryResult!=null){
			dataList=pageQueryResult.getDataList();
			for (T t : dataList) {
				if (t instanceof Selectable) {
					if(((Selectable)t).isSelected()){
						this.selectedIds.add(((Selectable)t).getId());
					}else{
						this.selectedIds.remove(((Selectable)t).getId());
					}
				}
			}
		}
		return this.selectedIds;
	}

}
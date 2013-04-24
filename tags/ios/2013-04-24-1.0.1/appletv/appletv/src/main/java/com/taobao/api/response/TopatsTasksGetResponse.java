package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.Task;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.topats.tasks.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TopatsTasksGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7724753447527841724L;

	/** 
	 * 符合查询条件内的定时任务的结果集
	 */
	@ApiListField("tasks")
	@ApiField("task")
	private List<Task> tasks;

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<Task> getTasks( ) {
		return this.tasks;
	}

}

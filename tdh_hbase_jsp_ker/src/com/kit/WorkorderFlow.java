package com.kit;

/**
 * 
 * @author gh
 * @create_time 2017-06-02
 * 流程实体
 */
public class WorkorderFlow {
private String task_id;
	
	public String getTask_id() {
	return task_id;
}
public void setTask_id(String task_id) {
	this.task_id = task_id;
}
	private String instance_id;
	public String getInstance_id() {
		return instance_id;
	}
	public void setInstance_id(String instance_id) {
		this.instance_id = instance_id;
	}
	public String getTask_reach_time() {
		return task_reach_time;
	}
	public void setTask_reach_time(String task_reach_time) {
		this.task_reach_time = task_reach_time;
	}
	public String getTask_commit_time() {
		return task_commit_time;
	}
	public void setTask_commit_time(String task_commit_time) {
		this.task_commit_time = task_commit_time;
	}
	public String getTache_name() {
		return tache_name;
	}
	public void setTache_name(String tache_name) {
		this.tache_name = tache_name;
	}
	public String getLast_commitor() {
		return last_commitor;
	}
	public void setLast_commitor(String last_commitor) {
		this.last_commitor = last_commitor;
	}
	private String task_reach_time;
	private String task_commit_time;
	private String tache_name;
	private String last_commitor;
	private String cost_time;//花费时长

	public String getCost_time() {
		return cost_time;
	}
	public void setCost_time(String cost_time) {
		this.cost_time = cost_time;
	}
}

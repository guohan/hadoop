package de.kimrudolph.tutorials.utils;

import java.io.Serializable;
import java.util.List;


/**
 * Response object containing traffic details that will be sent to dashboard.
 * 
 * @author gh
 * @description 
 * 区分小球归属部分
 * stuats状态来划分
 * new 漏斗上半部分 
 * ing 处理中
 * finish 处理完 归档
 * 
 *
 */
public class Response implements Serializable {
	private List<Data> dataList;// 工单集合
	
	private List<Data> areaList;// 归属区域的工单集合

	
	private List<String> areasList; //区域集合 传给前端 动态创建区域漏斗
	private List<String> businessList;//业务工单类型 创建归档圆球
	
	public List<Data> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Data> areaList) {
		this.areaList = areaList;
	}

	public List<Data> getDataList() {
		return dataList;
	}

	public void setDataList(List<Data> dataList) {
		this.dataList = dataList;
	}
	
}

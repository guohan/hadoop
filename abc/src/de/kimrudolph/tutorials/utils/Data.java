package de.kimrudolph.tutorials.utils;

/**
 * 
 * @author gh
 * @description 工单相关信息
 *
 */
public class Data {

//	private final long time = System.currentTimeMillis();

	private String id;// 工单id
	private String color;// 工单小球颜色
	private String status;// 工单的业务状态
	private String area;// 工单归属区域
	private String channelType;// 工单类型
	private String businessType;//业务类型
	
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

}

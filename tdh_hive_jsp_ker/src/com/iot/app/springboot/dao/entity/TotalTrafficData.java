package com.iot.app.springboot.dao.entity;

import java.io.Serializable;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity class for total_traffic db table
 * 
 * @author abaghel
 *
 */
//@Table("total_traffic")
public class TotalTrafficData implements Serializable{
//	@PrimaryKeyColumn(name = "routeid",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
//
//	@PrimaryKeyColumn(name = "recordDate",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
//	
//	@PrimaryKeyColumn(name = "vehicletype",ordinal = 2,type = PrimaryKeyType.CLUSTERED)
//	
//	@Column(value = "totalcount")
//	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="MST")
//	@Column(value = "timestamp")
	private Date timeStamp;
	private String routeId;
	private String recordDate;
	private String vehicleType;
	private long totalCount;
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "TrafficData [routeId=" + routeId + ", vehicleType=" + vehicleType + ", totalCount=" + totalCount
				+ ", timeStamp=" + timeStamp + "]";
	}
	
	
}

package com.iot.app.springboot.vo;

import java.io.Serializable;
import java.util.List;

import com.iot.app.springboot.dao.entity.TotalTrafficData;

/**
 * Response object containing traffic details that will be sent to dashboard.
 * 
 * @author abaghel
 *
 */
public class Response implements Serializable {
	private List<TotalTrafficData> totalTraffic;
	
	public List<TotalTrafficData> getTotalTraffic() {
		return totalTraffic;
	}
	public void setTotalTraffic(List<TotalTrafficData> totalTraffic) {
		this.totalTraffic = totalTraffic;
	}

}

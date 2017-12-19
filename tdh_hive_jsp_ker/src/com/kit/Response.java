package com.kit;

import java.io.Serializable;
import java.util.List;


/**
 * Response object containing traffic details that will be sent to dashboard.
 * 
 * @author abaghel
 *
 */
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1122387442956514747L;
	private List<Bean> poi;

	public List<Bean> getPoi() {
		return poi;
	}

	public void setPoi(List<Bean> poi) {
		this.poi = poi;
	}
	

}

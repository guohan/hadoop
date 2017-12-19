package com.kit;

/**
 * 
 * @author guohan
 * @createtime 2016-12-26
 * @description
 *  创建实体对象bean
 *
 */
public class Bean {
    private String rowkey;
    public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private String family;
    private String qualifier;
    private String timestamp;
    private String value;
    
    public String toString(){
		return this.family+"\t"+this.qualifier+"\t"+this.timestamp+"\t"+this.value+"\t"+this.rowkey;
	}
    public Bean(String rowkey, String family,String qualifier,String timestamp,String value) {

  	  this.rowkey = rowkey;

  	  this.family = family;
  	 this.qualifier = qualifier;
  	 this.timestamp = timestamp;
  	 this.value = value;

  	 }
    public Bean() {
		// TODO Auto-generated constructor stub
	}
}

package com.kit.udf;

public class TestUDF {
  public static void main(String[] args) {
    /*
     * hive> add jar jarName.jar;
     * 
     * hive> create temporary function ip2num as
     * 'transwarp.io.inceptor.udf.InUDF';
     * 
     * hive> select ip2num(t.col1) from t limit 10;
     * 
     * hive> drop temporary function ip2num;
     */
  }
}

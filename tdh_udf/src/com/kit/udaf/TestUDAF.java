package com.kit.udaf;

public class TestUDAF {
  /*
   * hive> add jar jarName.jar;
   * 
   * hive> create temporary function test_group as
   * 'transwarp.io.inceptor.udaf.InUDAF';
   * 
   * hive> select test_group(field3,field4,field5,field6) from t;
   * 
   * hive> drop temporary function test_group;
   * 
   * hive> quit;
   */
}

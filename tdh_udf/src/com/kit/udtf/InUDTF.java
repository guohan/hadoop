package com.kit.udtf;

import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;

/*
 * UDTF(User-Defined Table-Generating Functions)  用来解决 输入一行输出多行(On-to-many maping) 的需求
 * 
 * 继承org.apache.hadoop.hive.ql.udf.generic.GenericUDTF。
 * 实现initialize, process, close三个方法
 * UDTF首先会调用initialize方法，此方法返回UDTF的返回行的信息（返回个数，类型）。
 * 初始化完成后，会调用process方法，对传入的参数进行处理，可以通过forword()方法把结果返回。
 * 最后close()方法调用，对需要清理的方法进行清理。
 * 
 * 
 * UDTF有两种使用方法，一种直接放到select后面，一种和lateral view一起使用。
 * 1：直接select中使用：select explode_map(properties) as (col1,col2) from src;
 * 不可以添加其他字段使用：select a, explode_map(properties) as (col1,col2) from src
 * 不可以嵌套调用：select explode_map(explode_map(properties)) from src
 * 不可以和group by/cluster by/distribute by/sort by一起使用：select explode_map(properties) as (col1,col2) from src group by col1, col2
 * 2：和lateral view一起使用：select src.id, mytable.col1, mytable.col2 from src lateral view explode_map(properties) mytable as col1, col2;
 * 此方法更为方便日常使用。执行过程相当于单独执行了两次抽取，然后union到一个表里。
 * 
 * 下面是我写的一个用来切分”key:value;key:value;”这种字符串，返回结果为key, value两个字段.
 */

public class InUDTF extends GenericUDTF {

  @Override
  public void close() throws HiveException {
    // TODO Auto-generated method stub

  }

  @Override
  public StructObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
    // TODO Auto-generated method stub
    if (arg0.length != 1) {
      throw new UDFArgumentLengthException(ErrorMsg.ERROR_10271,"ExplodeMap takes only one argument");
    }
    if (arg0[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
      throw new UDFArgumentException(ErrorMsg.ERROR_10271,"ExplodeMap takes string as a parameter");
    }

    ArrayList<String> fieldNames = new ArrayList<String>();
    ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
    fieldNames.add("col1");
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
    fieldNames.add("col2");
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

    return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
  }

  @Override
  public void process(Object[] arg0) throws HiveException {
    // TODO Auto-generated method stub
    String input = arg0[0].toString();
    String[] test = input.split(";");
    for (int i = 0; i < test.length; i++) {
      try {
        String[] result = test[i].split(":");
        forward(result);
      } catch (Exception e) {
        continue;
      }
    }
  }

}

package com.kit.udtf;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import scala.annotation.meta.field;

/**
 * @author GH
 * @createTime 2017-02-22
 * @description
 * udtf 开发要点
 * 1继承org.apache.hadoop.hive.ql.udf.generic.GenericUDTF
 * 2 实现initialize process close三个方法
 * a initialize初始化验证 返回字段名和字段类型
 * b 初始化完成后 调用process方法 对传入的参数进行处理,通过forward方法把结果返回
 * c 最后调用close方法进行清理工作
 * 字符串的拆分 多行输出
 *
 */
public class UDTFSplit extends GenericUDTF{

	@Override
	public void close() throws HiveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StructObjectInspector initialize(ObjectInspector[] arg0) 
			throws UDFArgumentException {
		ArrayList<String> fieldNames=new ArrayList<String>();
		ArrayList<ObjectInspector> fieldOIs= new ArrayList<ObjectInspector>();
		fieldNames.add("col1");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col2");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
		
	}

	@Override
	public void process(Object[] arg0) throws HiveException {
		// TODO Auto-generated method stub
		String input=arg0.toString();
	String[] inputSplits=input.split("#"
	);
	for (int i = 0; i < inputSplits.length; i++) {
		String[] result=inputSplits[i].split(":");
		forward(result);
	}
	}

}

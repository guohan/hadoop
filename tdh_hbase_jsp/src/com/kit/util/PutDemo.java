package com.kit.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.util.Bytes;

import com.kit.HbaseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PutDemo {

 public static void main(String[] args) throws IOException {
 //创建 HBase 上下文环境
 Configuration conf = HBaseConfiguration.create(); 
 System.out.println("conf="+conf);
 int count=0;
 
 HBaseHelper helper = HBaseHelper.getHelper(conf);
 System.out.println("helper="+helper);
 helper.dropTable("testtable3");
	String[] familys="colfam1".split(";");//244078
 helper.createTable("testtable2",familys);
 
 HTable table = new HTable(HbaseUtil.getConfiguration(), "testtable3"); 
// table.d
 long start = System.currentTimeMillis();
for(int i=1;i<100000;i++){
//设置 rowkey 的值
 Put put = new Put(Bytes.toBytes("row"+i)); 
// 设置 family:qualifier:value
 put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
 Bytes.toBytes("val1")); 
 put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"),
 Bytes.toBytes("val2")); 
 //调用 put 方法，插入数据导 HBase 数据表 testtable1 里
 table.put(put); 
// ArrayList<Put>
// List<Row> list=table.getWriteBuffer();
 count++;
 if(count%10000==0){
 System.out.println("Completed 10000 rows insetion");
 }
 }
 
 System.out.println(System.currentTimeMillis() - start);
 }
}
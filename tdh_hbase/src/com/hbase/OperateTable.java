package com.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;

public class OperateTable {
         public static void main(String[] args) throws IOException {
         
         Configuration conf = HBaseConfiguration.create();
//         conf.set("hbase.rootdir", "hdfs://172.16.19.161:9000/hbase");
         conf.set("hbase.rootdir", "hdfs://172.16.19.161:9000/hbase");
         conf.set("hbase.zookeeper.quorum", "192.168.56.101");//使用eclipse时必须添加这个，否则无法定位
         conf.set("hbase.zookeeper.property.clientPort", "2181");
         
         
//         HbaseTestcase.put(tablename, "row1", columnFamily, "cl1", "data");
         
//         HTable table = new HTable(conf, "test0701");
//         Put p1=new Put(Bytes.toBytes("row1"));
//         p1.add(Bytes.toBytes("article"), Bytes.toBytes("article"), Bytes.toBytes("data"));
//         table.put(p1);
//         System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
         
         HBaseAdmin admin = new HBaseAdmin(conf);// 新建一个数据库管理员//新api
         HTableDescriptor desc=new HTableDescriptor(TableName.valueOf("test0701"));
         //HTableDescriptor desc = new HTableDescriptor("blog");
         desc.addFamily(new HColumnDescriptor("article"));
         desc.addFamily(new HColumnDescriptor("author"));
         desc.addFamily(new HColumnDescriptor("count"));
         admin.createTable(desc );
         admin.close();
         System.out.println("test is ending");
         admin.disableTable("blog");
//         admin.deleteTable("blog");
         System.out.println("delete table is ending");
         //assertThat(admin.tableExists("blog"),is(false));
   }
}
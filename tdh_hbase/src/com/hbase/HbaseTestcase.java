package com.hbase;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;


public class HbaseTestcase {           
    //声明静态配置 HBaseConfiguration
    static Configuration cfg=HBaseConfiguration.create();

    //创建一张表，通过HBaseAdmin HTableDescriptor来创建
    public static void creat(String tablename,String columnFamily) throws Exception {
    	cfg.set("hbase.rootdir", "hdfs://172.16.19.161:9000/hbase");
    	cfg.set("hbase.zookeeper.quorum", "172.16.19.161");//使用eclipse时必须添加这个，否则无法定位
    	cfg.set("hbase.zookeeper.property.clientPort", "2181");
        HBaseAdmin admin = new HBaseAdmin(cfg);
        if (admin.tableExists(tablename)) {
            System.out.println("table Exists!");
            System.exit(0);
        }
        else{
            HTableDescriptor tableDesc = new HTableDescriptor(tablename);
            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            admin.createTable(tableDesc);
            System.out.println("create table success!");
        }
    }
  
    //添加一条数据，通过HTable Put为已经存在的表来添加数据
    public static void put(String tablename,String row, String columnFamily,String column,String data) throws Exception {
        HTable table = new HTable(cfg, tablename);
        Put p1=new Put(Bytes.toBytes(row));
        p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(p1);
        System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
    }
   
   public static void get(String tablename,String row) throws IOException{
            HTable table=new HTable(cfg,tablename);
            Get g=new Get(Bytes.toBytes(row));
                Result result=table.get(g);
                System.out.println("Get: "+result);
                table.close();
    }
    //显示所有数据，通过HTable Scan来获取已有表的信息
    public static void scan(String tablename) throws Exception{
         HTable table = new HTable(cfg, tablename);
         
//         conf.set("hbase.rootdir", "hdfs://hadoop1:9000/hbase");  
//         // 使用eclipse时必须添加这个，否则无法定位  
//         conf.set("hbase.zookeeper.quorum", "hadoop1");  
         
         cfg.set("hbase.zookeeper.quorum", "kit-b2");//使用eclipse时必须添加这个，否则无法定位
         cfg.set("hbase.zookeeper.property.clientPort", "2181");
         cfg.set("hbase.rootdir", "hdfs://172.16.19.152:9000/hyperbase1");
//         cfg.set("zookeeper.znone.parent", "/hyperbase1");
         Scan s = new Scan();
         ResultScanner rs = table.getScanner(s);
         for(Result r:rs){
             System.out.println("Scan: "+r);
         }
         table.close();
    }
    
    public static boolean delete(String tablename) throws IOException{
            
            HBaseAdmin admin=new HBaseAdmin(cfg);
            if(admin.tableExists(tablename)){
                    try
                    {
                            admin.disableTable(tablename);
                            admin.deleteTable(tablename);
                    }catch(Exception ex){
                            ex.printStackTrace();
                            return false;
                    }
                    
            }
            return true;
    }
  
    public static void  main (String [] agrs) {
            String tablename="kit0607";
        String columnFamily="cf";
          
            try {                     
            	HbaseTestcase.creat(tablename, columnFamily);
            	HbaseTestcase.put(tablename, "row1", columnFamily, "cl1", "data");
            	HbaseTestcase.get(tablename, "row1");
            	HbaseTestcase.scan(tablename);
         if(true==HbaseTestcase.delete(tablename))
                    System.out.println("Delete table:"+tablename+"success!");
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }    
}
}
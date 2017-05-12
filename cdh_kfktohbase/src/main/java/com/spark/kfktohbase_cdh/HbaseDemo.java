package com.spark.kfktohbase_cdh;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;


import java.io.IOException;


public class HbaseDemo {
	 private static HBaseAdmin admin = null;
  public static void main(String[] args) throws IOException {
    Configuration HBASE_CONFIG = new Configuration();
    

    Configuration conf = HBaseConfiguration.create(HBASE_CONFIG);
//    conf.set("zookeeper.znode.parent", "/hbase");
//    conf.set("hbase.zookeeper.property.clientPort", "2181");
    conf.set("hbase.zookeeper.quorum", "172.16.19.158");
    
    @SuppressWarnings("resource")
    HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
    
//    hBaseAdmin.disableTable("t0901"); 
//    hBaseAdmin.deleteTable("t0901"); 
//    hBaseAdmin.close();
//    System.out.println("delete success");
    
//    
    HTableDescriptor hTableDescriptor = new HTableDescriptor("t1021");
    hTableDescriptor.addFamily(new HColumnDescriptor("f"));
    hBaseAdmin.createTable(hTableDescriptor);
    System.out.println("create success");
  }
  
//delete table 
	public static void dropTable(String tableName) { 
        try { 
//        	HbaseUtil.open();
        	
//            HBaseAdmin admin = new HBaseAdmin(conf); 
            admin.disableTable(tableName); 
            admin.deleteTable(tableName); 
            admin.close();
            System.out.println("delete success");
        } catch (MasterNotRunningException e) { 
            e.printStackTrace(); 
        } catch (ZooKeeperConnectionException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
 
    } 
}


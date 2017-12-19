package com.transwrap;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;


public class HyperbaseDemo {
  public static void main(String[] args) throws IOException {
    Configuration HBASE_CONFIG = new Configuration();
      //Use the relative path to load the configuration file
//    HBASE_CONFIG.addResource(HyperbaseDemo.class.getClassLoader().getResource("transwarp/io/hyperbase/conf/core-site.xml"));
//    HBASE_CONFIG.addResource(HyperbaseDemo.class.getClassLoader().getResource("transwarp/io/yperbase/conf/core-site.xml"));
//    HBASE_CONFIG.addResource(HyperbaseDemo.class.getClassLoader().getResource("transwarp/io/yperbase/conf/core-site.xml"));
//    HBASE_CONFIG.addResource(HyperbaseDemo.class.getClassLoader().getResource("transwarp/io/yperbase/conf/core-site.xml"));
//    HBASE_CONFIG.addResource("core-site.xml");
//    HBASE_CONFIG.addResource("yarn-site.xml");
//    HBASE_CONFIG.addResource("hdfs-site.xml");
//    HBASE_CONFIG.addResource("hbase-site.xml");

      /*
      core-site.xml
      yarn-site.xml
      hdfs-site.xml
      hbase-site.xml
       */

    Configuration conf = HBaseConfiguration.create(HBASE_CONFIG);
//    conf.set("zookeeper.znode.parent", "/hyperbase1");
//    conf.set("hbase.zookeeper.property.clientPort", "2181");
    conf.set("hbase.zookeeper.quorum", "172.16.19.151,172.16.19.152,172.16.19.153");
//    conf.set("hbase.master", "172.16.19.153:60000");
//    conf.set("hbase.cluster.distributed", "true");
    
//    conf.set("hbase.security.authentication", "kerberos");
//    conf.set("hadoop.security.authentication", "kerberos");
//    
//    conf.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
//    conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
//   
//    UserGroupInformation.setConfiguration(conf);
//    UserGroupInformation.loginUserFromKeytab("hbase/kit-b1@TDH", "D:\\hbase.keytab");
  
    //Login with keytab
//    UserGroupInformation.setConfiguration(conf);
//    UserGroupInformation.loginUserFromKeytab("hbase@TDH", HyperbaseDemo.class.getClassLoader().getResource("hbase.keytab").getPath());
    
    @SuppressWarnings("resource")
    HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
    @SuppressWarnings("deprecation")
    HTableDescriptor hTableDescriptor = new HTableDescriptor("test11180011");
    hTableDescriptor.addFamily(new HColumnDescriptor("f"));
    hBaseAdmin.createTable(hTableDescriptor);
    System.out.println("create success");
  }
}


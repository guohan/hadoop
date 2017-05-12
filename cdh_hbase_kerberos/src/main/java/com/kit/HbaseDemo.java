package com.kit;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.util.SortedMap;

/***
 * 
 * @author gh
 * @createtime 2016-12-7 
 * @description 
 * hbase kerberos认证 curd操作示列
 *
 */
public class HbaseDemo {
	 private static HBaseAdmin admin = null;
  public static void main(String[] args) throws IOException {
    Configuration HBASE_CONFIG = new Configuration();
    Configuration conf = HBaseConfiguration.create(HBASE_CONFIG);
//    conf.set("zookeeper.znode.parent", "/hbase");
//    conf.set("hbase.zookeeper.property.clientPort", "2181");
//    conf.set("hbase.zookeeper.quorum", "172.16.19.158");
//    System. setProperty("java.security.krb5.conf", "D:/linux/cdh_kit/kerberos/krb5.conf" );
    conf = HBaseConfiguration.create();
    // 这个hbase.keytab也是从远程服务器上copy下来的, 里面存储的是密码相关信息
    // 这样我们就不需要交互式输入密码了
    conf.set("keytab.file" , HbaseDemo.class.getClassLoader().getResource("hbase.keytab").getPath() );
    conf.set("hbase.master", "172.16.19.156:60000");
    conf.set("hbase.cluster.distributed", "true");
    // 这个可以理解成用户名信息，也就是Principal
//    conf.set("kerberos.principal" , "hbase/_HOST@CLOUDERA" );         
    conf.set("hbase.zookeeper.quorum", "172.16.19.156");
    conf.set("hbase.zookeeper.property.clientPort", "2181");
    conf.set("hbase.security.authentication", "kerberos");
//    conf.set("hadoop.security.authentication", "kerberos");
    conf.set("hbase.master.kerberos.principal", "hbase/centos-1@CLOUDERA");
    conf.set("hbase.regionserver.kerberos.principal", "hbase/centos-1@CLOUDERA");
   UserGroupInformation. setConfiguration(conf);
    try {
//         UserGroupInformation. loginUserFromKeytab("hbase/centos-1@CLOUDERA", "D:/linux/cdh_kit/kerberos/156/hbase.keytab" );
         UserGroupInformation.loginUserFromKeytab("hbase/centos-1@CLOUDERA", HbaseDemo.class.getClassLoader().getResource("hbase.keytab").getPath());
  	   
    } catch (IOException e) {
          // TODO Auto-generated catch block
         e.printStackTrace();
   }
    
    
	// 构建实列
	HBaseAdmin admin = new HBaseAdmin(conf);
	TableName[] tablenames = admin.listTableNames();
	for (TableName tableName : tablenames)
	{
		System.out.println(tableName);
	}
  }
}


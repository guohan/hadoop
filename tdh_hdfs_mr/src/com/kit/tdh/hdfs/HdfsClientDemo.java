package com.kit.tdh.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

/**
 * 
 * @author guohan
 * @createtime 2016-08-11
 * 
 * @desc  测试读取配置文件后 在集群上面创建文件夹以及文件
 *
 */
public class HdfsClientDemo {
  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration();
    conf.addResource(HdfsClientDemo.class.getClassLoader().getResource("core-site.xml"));
    conf.addResource(HdfsClientDemo.class.getClassLoader().getResource("yarn-site.xml"));
    conf.addResource(HdfsClientDemo.class.getClassLoader().getResource("hdfs-site.xml"));
//    
//    UserGroupInformation.setConfiguration(conf);
//    UserGroupInformation.loginUserFromKeytab("hdfs@TDH", HdfsClientDemo.class.getClassLoader().getResource("transwarp/io/hdfs/hdfsclient/conf/hdfs.keytab").getPath());
//    
    FileSystem fs = FileSystem.get(conf);
    

    fs.createNewFile(new Path("/guohan1/test0819.txt"));//在hdfs路径上创建新的文件
    System.out.println("create file success");
//          fs.delete(new Path("/guohan"));
//          System.out.println("delete success!");
    /*
     * TODO  write/read hdfs files
     */
  }
}

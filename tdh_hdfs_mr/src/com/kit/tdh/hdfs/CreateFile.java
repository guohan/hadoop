package com.kit.tdh.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
/**
 * 
 * @author gh
 * @desc 创建文件目录
 *
 */
public class CreateFile {
	public static void main(String[] args) throws IOException {
		// create by lee
		Configuration conf = new Configuration();
				UserGroupInformation.setConfiguration(conf);
//				UserGroupInformation.loginUserFromKeytab("hdfs/kit-b1@TDH",
//						UploadFile.class.getClassLoader().getResource("hdfs.keytab")
//								.getPath());
				UserGroupInformation.loginUserFromKeytab("kitdev@TDH",
						UploadFile.class.getClassLoader().getResource("kitdev.keytab")
								.getPath());
//		String rootPath = "hdfs://172.16.2.245:8020";
		Path p = new Path( "/user/hdfs2017050501");
//		"/user/hdfs20170313001
		FileSystem fs = p.getFileSystem(conf);
		fs.create(p);
		System.out.println("create success");
		fs.close();
	}
}

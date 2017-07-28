package com.kit.cdh_hdfs_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.IOException;

/**
 * 
 * @author gh
 * @desc 创建文件
 *
 */
public class CreateFile {
	public static void main(String[] args) throws IOException {
		String rootPath = "hdfs://172.16.19.158:8020"; //dfs address
		Path p = new Path(rootPath + "/guohan/test.txt");//dfs file path
		Configuration conf = new Configuration();
		FileSystem fs = p.getFileSystem(conf);
		fs.create(p);
		System.out.println("create file success!");
		fs.close();
	}
}
package com.kit.cdh_hdfs_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/***
 * 
 * @author guohan
 * @desc 创建文件夹目录
 *
 */
public class CreateDir {
	public static void main(String[] args) throws IOException {
		String rootPath = "hdfs://172.16.19.158:8020"; //dfs address
		Path p = new Path(rootPath + "/tmp/newDir2");//文件目录
		
		Configuration conf = new Configuration();
		FileSystem fs = p.getFileSystem(conf);
		boolean b = fs.mkdirs(p);//创建目录
		System.out.println(b);
		fs.close();
	}
}

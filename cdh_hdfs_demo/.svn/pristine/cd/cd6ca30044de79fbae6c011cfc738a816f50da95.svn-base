package com.kit.cdh_hdfs_demo;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/**
 * 
 * @author guohan
 * @desc 删除文件操作
 *
 */
public class Delete {
	public static void main(String[] args) {
		String rootPath = "hdfs://172.16.19.158:8020"; //dfs address
		Path p = new Path(rootPath + "/guohan/test.txt");//dfs file path
		Configuration conf = new Configuration();// 加载资源文件
		try {
			FileSystem fs = p.getFileSystem(conf);//创建文件系统对象
			boolean b = fs.delete(p, true);//删除文件
			System.out.println(b);
			fs.close();//关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

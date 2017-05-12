package com.kit.cdh_hdfs_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;


/***
 * 
 * @author guohan
 * @desc 在hdfs的文件上面追写内容
 *
 */
public class AppendFile {
	
	public static void main(String[] args) throws IOException {
		
		
		for(int i =0;i<10000;i++){
			Path p = new Path("hdfs://172.16.19.158:8020"+"/guohan/test0817.txt");//hdfs 上已存在的文本
			Configuration conf = new Configuration();
			FileSystem fs = p.getFileSystem(conf);
			fs.setReplication(p, (short) 1);//true if successful; false if file does not exist or is a directory

			OutputStream out = fs.append(p);//判断 如果文件存在 那么可以正常追加文件内容到此文本
			InputStream in = new BufferedInputStream(new FileInputStream(
					"D:\\wordcountdata.txt"));//读取文件
			IOUtils.copyBytes(in, out, conf);//io工具类 复制字节 并输出到相关文件
			fs.close();//关闭文件系统
			IOUtils.closeStream(in);//关闭流 完成写文件操作
			System.out.println("test success!");
		}
		
	}
}

package com.kit.cdh_hdfs_demo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * 
 * @author gh
 * @createtime 2016/08/10 
 * @desc 描述  cdh环境下 成功跑通hdfs测试代码
 *
 */
public class UploadFile {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		String localFile = "E:\\yarn-site.xml";
		InputStream in = new BufferedInputStream(new FileInputStream(localFile));
		Path p = new Path( "/tmp/test090701");//hdfs 文件路径
		FileSystem fs = p.getFileSystem(conf);
		OutputStream out = fs.create(p);
		IOUtils.copyBytes(in, out, conf);
		fs.close();
		IOUtils.closeStream(in);
		System.out.println("test success!");
	}
}

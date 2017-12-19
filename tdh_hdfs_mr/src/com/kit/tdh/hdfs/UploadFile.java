package com.kit.tdh.hdfs;

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
 * @author gh tdh 平台下面测试程序 读取本地文件后 提交到hdfs
 */
public class UploadFile {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		String localFile = "D:\\a_hadoop_test\\bigfile2.txt";
		InputStream in = new BufferedInputStream(
				new FileInputStream(localFile));
		Path p = new Path("/user/hdfs20170614001");
		UserGroupInformation.setConfiguration(conf);
		UserGroupInformation.loginUserFromKeytab("kitdev@TDH",
				UploadFile.class.getClassLoader().getResource("kitdev.keytab")
						.getPath());

		FileSystem fs = p.getFileSystem(conf);
		OutputStream out = fs.create(p);
		IOUtils.copyBytes(in, out, conf);
		fs.close();
		IOUtils.closeStream(in);
		System.out.println("test success!");
	}
}

package com.kit.cdh_hdfs_demo.web;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.web.WebHdfsFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;
import java.net.URI;



/**
 * 
 * @author gh
 * @desc 远程操作文件系统 追加文件内容
 *
 */
public class AppendFile {
	public static void main(String[] args) throws IOException {
        URI uri = URI.create(WebHdfsFileSystem.SCHEME + "://" + "172.16.19.158:50070");
		Path p = new Path("/guohan/test.txt");
		Configuration conf = new Configuration();
//        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("hdfs");
//        UserGroupInformation.setLoginUser(ugi);
		WebHdfsFileSystem fs = (WebHdfsFileSystem) FileSystem.get(uri,conf);
		fs.setReplication(p, (short) 1);
		OutputStream out = fs.append(p);
		InputStream in = new BufferedInputStream(new FileInputStream(
				"E:\\yarn-site.xml"));
		IOUtils.copyBytes(in, out, conf);
		fs.close();
		IOUtils.closeStream(in);
	}
}

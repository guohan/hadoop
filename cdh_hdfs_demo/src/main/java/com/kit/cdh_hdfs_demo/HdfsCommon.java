package com.kit.cdh_hdfs_demo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsCommon {  
    private Configuration conf;  
    private FileSystem fs;  
    public HdfsCommon() throws IOException{  
        conf=new Configuration();  
        fs=FileSystem.get(conf);  
    }  
      
    /** 
     * 上传文件， 
     * @param localFile 本地路径 
     * @param hdfsPath 格式为hdfs://ip:port/destination 
     * @throws IOException 
     */  
    public void upFile(String localFile,String hdfsPath) throws IOException{  
        InputStream in=new BufferedInputStream(new FileInputStream(localFile));  
        OutputStream out=fs.create(new Path(hdfsPath));  
        IOUtils.copyBytes(in, out, conf);  
    	IOUtils.closeStream(in);
    }  
//    public void S(){
//    	Configuration conf = new Configuration();
//		String localFile = "E:\\yarn-site.xml";
//		InputStream in = new BufferedInputStream(new FileInputStream(localFile));
//		Path p = new Path( "/tmp/test0901");//hdfs 文件路径
//		FileSystem fs = p.getFileSystem(conf);
//		OutputStream out = fs.create(p);
//		IOUtils.copyBytes(in, out, conf);
//		fs.close();
//		IOUtils.closeStream(in);
//		System.out.println("test success!");
//    	
//    }
    /** 
     * 附加文件 
     * @param localFile 
     * @param hdfsPath 
     * @throws IOException 
     */  
    public void appendFile(String localFile,String hdfsPath) throws IOException{  
        InputStream in=new FileInputStream(localFile);  
        OutputStream out=fs.append(new Path(hdfsPath));  
        IOUtils.copyBytes(in, out, conf);  
    }  
    /** 
     * 下载文件 
     * @param hdfsPath 
     * @param localPath //需指定到文件名称
     * @throws IOException 
     */  
    public void downFile(String hdfsPath, String localPath) throws IOException{  
        InputStream in=fs.open(new Path(hdfsPath));  
        OutputStream out=new FileOutputStream(localPath);  
        IOUtils.copyBytes(in, out, conf);  
    }  
    /** 
     * 删除文件或目录 
     * @param hdfsPath 
     * @throws IOException 
     */  
    public void delFile(String hdfsPath) throws IOException{  
        fs.delete(new Path(hdfsPath), true);  
    }  
    
    public static void main(String[] args) throws IOException {  
        HdfsCommon hdfs=new HdfsCommon();  
//      hdfs.upFile("D:\\core-site.xml", "/guohan0901");  
      hdfs.downFile("/guohan0901", "e:\\test\\1.txt");  
//      hdfs.appendFile("D:\\hdfs-site.xml", "/guohan0901");  
//        hdfs.delFile("/guohan0901");  
    }  
}  
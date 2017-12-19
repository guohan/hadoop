package com.kit;


import java.util.*;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import kafka.auth.AuthenticationManager;
import kafka.javaapi.producer.Producer;  
import kafka.producer.KeyedMessage;  
import kafka.producer.ProducerConfig; 

/**
 * 
 * @author gh
 * 生产kafka消息 测试类
 * 供整个工程基本调试
 *
 */
public class TestProducer{  
	   private static Logger logger = Logger.getLogger(TestProducer.class);
	   
    public static void main(String[] args) throws UnsupportedEncodingException {  
        long events = 1000000L;  
        Random rnd = new Random();  
        //kafka相关配置信息从配置文件中读取TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath()
//        Properties props = new Properties();  
        InputStream is = TestProducer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
	    Properties p = new Properties();
	    try {
//	    	加载资源
			p.load(is);
////			添加鉴权
//		    AuthenticationManager.setAuthMethod( p.getProperty("authmethod").trim());//
//		    //打成jar包 后 此login方法 不能够识别jar包下面的资源文件 故写成配置
//	      AuthenticationManager.login( p.getProperty("authpwd").trim(),
////	    		  TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath() 
//	    		  p.getProperty("kafka_key_path").trim()  
//	    		  );
	      ProducerConfig config = new ProducerConfig(p);  
//	      构建producer
	        Producer<String, String> producer = new Producer<String, String>(config);  
	        for (long nEvents = 0; nEvents < events; nEvents++) {   
	               Date runtime = new Date();   
	               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String now = dateFormat.format(runtime);
	               String ip = "192.168.2." + rnd.nextInt(255);   
//	               String msg ="20160729 "+" 卡夫卡测试"+ now + ",www.example.com," + ip;   
	             byte[] msg =
	       		 getContent("D:\\gzkiterp\\workspace\\tdh_kfktohive\\resources\\json.txt");
	       		 String mesgg= new String(msg);
	       		// System.out.println("print json message"+mesgg);
//	               msg = new String(msg.getBytes(),"UTF-8");
	               KeyedMessage<String, String> data = new KeyedMessage<String, String>("oggtopic",mesgg);  
	               producer.send(data); //发送消息
	               System.out.println(data);
	            	logger.debug(msg);
	               try {  
	                   Thread.sleep(3000);  //休眠
	               } catch (InterruptedException e) {  
	                   // TODO Auto-generated catch block  
	                   e.printStackTrace();  
	               }  
	        }
	        producer.close();  //关闭链接
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
	    	logger.debug("生产消息异常："+"date"+new Date()+"error msg"+e1.getStackTrace());
			e1.printStackTrace();
		}
   //
       
    }  
    
    
    /**
	 * 解析文件
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	 public static byte[] getContent(String filePath) throws IOException {  
	        File file = new File(filePath);  
	        long fileSize = file.length();  
	        if (fileSize > Integer.MAX_VALUE) {  
	            System.out.println("file too big...");  
	            return null;  
	        }  
	        FileInputStream fi = new FileInputStream(file);  
	        byte[] buffer = new byte[(int) fileSize];  
	        int offset = 0;  
	        int numRead = 0;  
	        while (offset < buffer.length  
	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	            offset += numRead;  
	        }  
	        // 确保所有数据均被读取  
	        if (offset != buffer.length) {  
	        throw new IOException("Could not completely read file "  
	                    + file.getName());  
	        }  
	        fi.close();  


	        return buffer;  
	    }  
}  

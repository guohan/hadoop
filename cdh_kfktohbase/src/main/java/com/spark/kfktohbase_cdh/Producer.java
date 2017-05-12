/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spark.kfktohbase_cdh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
/**
 *
 * @author gh
 * @createTime 20160722
 * @description 
 *  生产消息 读取指定文件后 进行消息发送
 *
 */
public class Producer extends Thread {
	private static  kafka.javaapi.producer.Producer<String, String> producer;
//	private final String topic;
	private final Properties props = new Properties();
	private String filePath =
	"D:\\kit\\GZ_20160114_153000_TMR_BM.txt";
	  private static Logger logger = Logger.getLogger(Producer.class);
	private static String FILEPATH = null;
	/**
	 * 构造函数
	 * @throws IOException
	 */
	public Producer() throws IOException {
		InputStream is = TestProducer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
	    Properties p = new Properties();
//	    	加载资源
			p.load(is);
			FILEPATH= p.getProperty("dtpath").trim() ;
//			System.out.println(  p.getProperty("kafka_key_path").trim()  );
//    		  TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath());
		producer = new kafka.javaapi.producer.Producer<String, String>(new ProducerConfig(p));
	}
/*
 * 读取文件
 */
	public static String readTxtFile(String filePath) {
		StringBuffer buffer = new StringBuffer();
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					buffer.append(lineTxt);
					System.out.println(bufferedReader.readLine());
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return buffer.toString();
	}
	/**
	 * 解析文件
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	 public byte[] getContent(String filePath) throws IOException {  
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
	  
/**
 * 读取dt半结构化文件
 */
	public void run() {
		
		Document doc;
		try {
			int count=10000;
			for(int j=0;j<count;j++){
				doc = Jsoup
						.parse(
								new FileInputStream(FILEPATH),
								"UTF-8", "", Parser.xmlParser());
				String[] xmlData=new String[]{"system","Breaker","Disconnector",
						"GroundDisconnector","TransformerWinding","BusbarSection",
						"Compensator","SynchronousMachine","EnergyConsumer","ACLineSegment"};
				for (int i = 0; i < xmlData.length; i++) {
					System.out.println("======xmlData "+xmlData[i]+" starting===========");
					Elements elment = doc.select(xmlData[i]);
					String messageStr=parserValue(elment.get(0).text());
					System.out.println("msg"+xmlData[i]+" "+messageStr);
						 producer.send(new KeyedMessage<String, String>(xmlData[i], xmlData[i]+" "+messageStr));
						 logger.debug("kafka information: topic is "+xmlData[i]+"message is"+messageStr);
					System.out.println("======xmlData "+xmlData[i]+" ending===========");
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	 
/**
 * 读取txt文本文件
 */
//		public void run() {
//			try
//			{
//				byte[]  msg = 
//						getContent(filePath);
//				String mesgg= new String(msg);
//				String[] kafka=mesgg.split("\r\n");
//				 System.out.println(kafka.length);
//				 for(int i=0;i<kafka.length;i++){
//					 producer.send(new KeyedMessage<String, String>("TMR_BM",kafka[i]));
//				System.out.println("======xmlData "+i+" ending===========");
//				 }
//			} catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 
//			
//		}
	
	
	/**
	 * 把数据存放到集合
	 * @param str
	 * @return
	 */
	public static Map<String, String> parser(String str) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        String[] kv = str.split(" ");
        if (kv != null) {

            boolean lined = false;
            for (int k = 0; k < kv.length; k++) {
                if (kv[k] != null && !kv[k].trim().isEmpty()) {
                    if (kv[k].trim().equals("@")) {
                        continue;
                    }
                    if (kv[k].trim().equals("#")) {
                        lined = true;
                        continue;
                    }
                    if (lined) {
                    	System.out.println("name:"+kv[k - (kv.length / 2)]+"  值:"+kv[k]);
                        values.put(kv[k-(kv.length / 2) ], kv[k]);
                    }
                }
            }
        }

        return values;
    }
	/**
	 * 解析xml节点
	 * @param str
	 * @return
	 */
	public static String parserValue(String str) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		String[] kv = str.split(" ");
		if (kv != null) {

			boolean lined = false;
			for (int k = 0; k < kv.length; k++) {
				if (kv[k] != null && !kv[k].trim().isEmpty()) {
					if (kv[k].trim().equals("@")) {
						continue;
					}
					System.out.println(kv[k]);
					if (kv[k].trim().equals("#")) {
						lined = true;
						continue;
					}
					System.out.println(k);
//					System.out.println(kv.length / 3);
//					System.out.println(kv[k - (kv.length / 3)]);
					if (lined) {
						System.out.println("name:" + kv[k - (kv.length / 2)] + "  值:" + kv[k]);
						values.put(kv[k - (kv.length / 2)], kv[k]);
						sb.append(kv[k] + " ");
					}
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 主体main函数
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Producer().start();
		
	}
	
	
	

}

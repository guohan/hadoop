package com.kit.kafka;


import java.util.*;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
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
 *
 */
public class TestProducer{  
	   private static Logger logger = Logger.getLogger(TestProducer.class);
	   
    public static void main(String[] args) throws UnsupportedEncodingException {  
        long events = 10000L;  
        Random rnd = new Random();  
        System.out.println(TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath());
        //kafka相关配置信息从配置文件中读取
        Properties props = new Properties();  
        InputStream is = TestProducer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
	    Properties p = new Properties();
	    try {
//	    	加载资源
			p.load(is);
//			添加鉴权
		    AuthenticationManager.setAuthMethod( p.getProperty("authmethod").trim());
	      AuthenticationManager.login( p.getProperty("authpwd").trim(), TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath());
	      ProducerConfig config = new ProducerConfig(p);  
//	      构建producer
	        Producer<String, String> producer = new Producer<String, String>(config);  
	        for (long nEvents = 0; nEvents < events; nEvents++) {   
	               Date runtime = new Date();   
	               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String now = dateFormat.format(runtime);
	               String ip = "192.168.2." + rnd.nextInt(255);   
	               String msg =" "+"kafka test"+ now + ",www.example.com," + ip;   
	               msg = new String(msg.getBytes(),"UTF-8");
	               KeyedMessage<String, String> data = new KeyedMessage<String, String>("test",msg);  
	               producer.send(data); //发送消息
	            	logger.debug(msg);
	               try {  
	                   Thread.sleep(3000);  //休眠
	               } catch (InterruptedException e) {  
	                   // TODO Auto-generated catch block  
	                   e.printStackTrace();  
	               }  
	        }
	        producer.close();  
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
	    	logger.debug("生产消息异常："+"date"+new Date()+"error msg"+e1.getStackTrace());
			e1.printStackTrace();
		}
	    //kafka 鉴权认证
//	      p.getProperty("kfk.topic").trim()
//        //设置broker连接信息:ip:port
//    	props.put("metadata.broker.list", "172.16.19.151:9092,172.16.19.152:9092,172.16.19.153:9092");
//        props.put("metadata.broker.list",  p.getProperty("metadata.broker.list").trim());
//        props.put("metadata.broker.list", "192.168.56.101:9092");  
        //设置消息系列化使用的类  kafka.serializer.StringEncoder
//        props.put("serializer.class","kafka.serializer.StringEncoder"); //默认字符串编码消息  
        //props.put("partitioner.class", "example.producer.SimplePartitioner");  
      //request.required.acks 默认值：0
      //用来控制一个produce请求怎样才能算完成，准确的说，是有多少broker必须已经提交数据到log文件，并向leader发送ack，可以设置如下的值：
      // 0，意味着producer永远不会等待一个来自broker的ack，这就是0.7版本的行为。这个选项提供了最低的延迟，但是持久化的保证是最弱的，当server挂掉的时候会丢失一些数据。
      // 1，意味着在leader replica已经接收到数据后，producer会得到一个ack。这个选项提供了更好的持久性，因为在server确认请求成功处理后，client才会返回。如果刚写到leader上，还没来得及复制leader就挂了，那么消息才可能会丢失。
      // -1，意味着在所有的ISR都接收到数据后，producer才得到一个ack。这个选项提供了最好的持久性，只要还有一个replica存活，那么数据就不会丢失。
//        props.put("request.required.acks", "1");  
//        props.put("request.required.acks", p.getProperty("request.required.acks").trim());  
   //
       
    }  
}  

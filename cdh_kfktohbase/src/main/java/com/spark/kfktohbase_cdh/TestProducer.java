package com.spark.kfktohbase_cdh;

import java.util.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 
 * @author gh 生产kafka消息 测试类 供整个工程基本调试
 *
 */
public class TestProducer {
	private static Logger logger = Logger.getLogger(TestProducer.class);

	public static void main(String[] args) throws UnsupportedEncodingException {
		long events = 1000000L;
		Random rnd = new Random();
		// kafka相关配置信息从配置文件中读取TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath()
		// Properties props = new Properties();
		InputStream is = TestProducer.class.getClassLoader()
				.getResourceAsStream("kfk-test.properties");
		Properties p = new Properties();
		try {
			// 加载资源
			p.load(is);
			// 添加鉴权
			ProducerConfig config = new ProducerConfig(p);
			// 构建producer
			Producer<String, String> producer = new Producer<String, String>(
					config);
			for (long nEvents = 0; nEvents < events; nEvents++) {
				Date runtime = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String now = dateFormat.format(runtime);
				String ip = "192.168.2." + rnd.nextInt(255);
				String msg = "20160729 " + " 卡夫卡测试" + now + ",www.example.com,"
						+ ip;
				msg = new String(msg.getBytes(), "UTF-8");
				KeyedMessage<String, String> data = new KeyedMessage<String, String>(
						"test1021", msg);
				producer.send(data); // 发送消息
				System.out.println("msg" + msg);
				logger.debug(msg);
				try {
					Thread.sleep(3000); // 休眠
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			producer.close(); // 关闭链接
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.debug("生产消息异常：" + "date" + new Date() + "error msg"
					+ e1.getStackTrace());
			e1.printStackTrace();
		}
		//

	}
}

package com.spark.kfktohbase_cdh;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import scala.Tuple2;

import com.google.common.collect.Lists;

import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;

/**
 * @author gh
 * @createTime 20160722 
 * 通过spark streaming 读取kafka消息后存放到hbase数据库中
 */

public final class MsgFromkfkToHbase
{
	private static final Pattern SPACE = Pattern.compile(" ");
	private static Logger logger = Logger.getLogger(MsgFromkfkToHbase.class);

	public static void main(String[] args) throws IOException
	{

		String brokers = "centos-3:9092";// kafka集群配置
		final String topics = "system,Disconnector,Breaker";// 需要处理的对应主题
		InputStream is = MsgFromkfkToHbase.class.getClassLoader().getResourceAsStream("kfk-test.properties");
		Properties p = new Properties();
		// 加载资源
		p.load(is);

		// spark上下文配置
		SparkConf sparkConf = new SparkConf().setAppName("MsgFromkfkToHbase").setMaster("local");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));
		logger.debug("加载上下文成功！");
		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);
		// JavaStreamingContext.jarOfClass(arg0)

		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);
		logger.debug("加载kafka配置成功！");

		/**
		 * 使用以下方法之一触发： print() foreachRDD() saveAsObjectFiles()
		 * saveAsTextFiles() saveAsHadoopFiles()
		 * 
		 * 这里采用foreacherdd 遍历kafka数据
		 */
		messages.foreachRDD(new Function2<JavaPairRDD<String, String>, Time, Void>()
		{

			@Override
			public Void call(JavaPairRDD<String, String> rdd, Time time) throws Exception
			{

				rdd.foreach(new VoidFunction<Tuple2<String, String>>()
				{
					@Override
					public void call(Tuple2<String, String> tuple) throws Exception
					{
						String[] kafka = SPACE.split(tuple._2());
						logger.debug("################### insert hbase starting......." + tuple._2());
						pareDataToHbase(kafka);
						logger.debug("################### insert hbase ending.......");
					}
				});
				return null;
			}
		});

		// wordCounts.print();
		// Start the computation
		jssc.start();
		jssc.awaitTermination();
	}
/**
 * 解析数据 入库hbase
 * @param kafka
 */
	@SuppressWarnings("unchecked")
	private static void pareDataToHbase(String[] kafka)
	{

		/**
		 * 数据入库 获取到的是kafka数据 如何灵活的设计数据库 需要仔细构思
		 * 
		 * for (int i = 0; i < kafka.length; i++) {
		 * tableDescriptor.addFamily(new HColumnDescriptor(familys[i])); }
		 */
		Map map = new HashMap();
		Map infomap = new HashMap();
		if ("Breaker".equals(kafka[0].trim()))
		{
			infomap.put("factoryname", kafka[1]);
			infomap.put("Ocname", kafka[2]);
			infomap.put("ocid", kafka[3]);
			infomap.put("ocstatus", kafka[4]);
			infomap.put("oscsecnode", kafka[5]);
			infomap.put("Elea", kafka[6]);
			infomap.put("Eleb", kafka[7]);
			infomap.put("Elec", kafka[8]);
			infomap.put("pname", kafka[9]);
			map.put("info", infomap);
		} else if ("system".equals(kafka[0].trim()))
		{
			infomap.put("Area", kafka[1]);
			infomap.put("System", kafka[2]);
			infomap.put("Time", kafka[3]);
			infomap.put("Readtime", kafka[4]);
			map.put("info", infomap);
		} else if ("Disconnector".equals(kafka[0].trim()))
		{
			infomap.put("factoryname", kafka[1]);
			infomap.put("dzname", kafka[2]);
			infomap.put("dzid", kafka[3]);
			infomap.put("dzstatus", kafka[4]);
			infomap.put("dissecnode", kafka[5]);
			map.put("info", infomap);
		}
		HbaseUtil.add(kafka[0], kafka[0] + UUID.randomUUID(), map);
	}

}

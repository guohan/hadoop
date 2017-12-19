package com.kit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.http.HttpServer2;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

import com.google.common.collect.Maps;

public class LogStream {
	private final static Logger logger = LoggerFactory.getLogger(LogStream.class);

	private static HConnection connection = null;
	private static HTableInterface table = null;
	private static HttpServer2 infoServer = null;

	/**
	 * 鉴权 安全验证
	 * @param tablename
	 * @throws IOException
	 */
	public static void openHBase(String tablename) throws IOException {
		Configuration configuration = HBaseConfiguration.create();
		
		 configuration = HBaseConfiguration.create(); 
	        configuration.set("zookeeper.znode.parent", "/hyperbase1");
	        configuration.set("hbase.zookeeper.property.clientPort", "2181");
	        configuration.set("hbase.zookeeper.quorum", "172.16.19.151,172.16.19.152,172.16.19.153");
	        configuration.set("hbase.master", "172.16.19.153:60000");
	        configuration.set("hbase.cluster.distributed", "true");
	        
	        configuration.set("hbase.security.authentication", "kerberos");
	        configuration.set("hadoop.security.authentication", "kerberos");
	        
	        configuration.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
	        configuration.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
	       
	        UserGroupInformation.setConfiguration(configuration);
	        try
			{
				UserGroupInformation.loginUserFromKeytab("hbase/kit-b1@TDH", "D:\\hbase.keytab");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		synchronized (HConnection.class) {
			if (connection == null)
				connection = HConnectionManager.createConnection(configuration);
		}

		synchronized (HTableInterface.class) {
			if (table == null) {
				table = connection.getTable("recsys_logs");
			}
		}

		/* start http info server */
//		HttpServer2.Builder builder = new HttpServer2.Builder().setName("recsys").setConf(conf);
//		InetSocketAddress addr = NetUtils.createSocketAddr("0.0.0.0", 8089);
//		builder.addEndpoint(URI.create("http://" + NetUtils.getHostPortString(addr)));
//		infoServer = builder.build();
//
//		infoServer.addServlet("monitor", "/monitor", RecsysLogs.class);
//		infoServer.setAttribute("htable", table);
//		infoServer.setAttribute("conf", conf);
//		infoServer.start();
	}

	public static void closeHBase() {
		if (table != null)
			try {
				table.close();
			} catch (IOException e) {
				logger.error("关闭 table 出错", e);
			}
		if (connection != null)
			try {
				connection.close();
			} catch (IOException e) {
				logger.error("关闭 connection 出错", e);
			}
		if (infoServer != null && infoServer.isAlive())
			try {
				infoServer.stop();
			} catch (Exception e) {
				logger.error("关闭 infoServer 出错", e);
			}
	}

	public static void main(String[] args) {
//		 open hbase
		try {
			openHBase("logcount");
		} catch (IOException e) {
			logger.error("建立HBase 连接失败", e);
			System.exit(-1);
		}

		SparkConf conf = new SparkConf().setAppName("recsys log stream");
//		conf.setMaster("local");
		JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));
//开启kafka消费线程个数
		Map<String, Integer> topicMap = Maps.newHashMap();
		topicMap.put("test", 4);
		JavaPairReceiverInputDStream<String, String> logstream = KafkaUtils.createStream(ssc,
				"172.16.19.151:2181,172.16.19.152:2181,172.16.19.153:2181,,172.16.19.154:2181", "recsys_group1", topicMap);

		
		
		//spark streaming 计算处理kafka相关信息
		JavaDStream<String> lines = logstream.map(new Function<Tuple2<String, String>, String>() {
			private static final long serialVersionUID = -1801798365843350169L;

			@Override
			public String call(Tuple2<String, String> tuple2) {
				return tuple2._2();
			}
		}).filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 7786877762996470593L;

			@Override
			public Boolean call(String msg) throws Exception {
				return msg.indexOf("character service received paramters:") > 0;
			}
		});

		// 统计kafka消费中的数据，并保存到HBase中
		JavaDStream<Long> nums = lines.count();
		nums.foreachRDD(new Function<JavaRDD<Long>, Void>() {
			private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

			@Override
			public Void call(JavaRDD<Long> rdd) throws Exception {
				Long num = rdd.take(1).get(0);
				String ts = sdf.format(new Date());
				Put put = new Put(Bytes.toBytes(ts));
				put.add(Bytes.toBytes("f"), Bytes.toBytes("nums"), Bytes.toBytes(num));
				table.put(put);
				logger.debug("add data success: rdd animal"+rdd.take(1));
				return null;
			}
		});

		ssc.start();
		ssc.awaitTermination();
	}
}

package com.kit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.collect.Lists;

import kafka.auth.AuthenticationManager;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;

/**
 * @author gh
 * @createTime 2017/01/10 通过spark streaming 读取kafka消息后入库到hive数据库
 */

public final class KfkToHive {
	private static final Pattern SPACE = Pattern.compile(" ");
	private static Logger logger = Logger.getLogger(KfkToHive.class);

	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf()//.setMaster("local[*]")
					.setAppName("SparkHiveSqlJdbc"));
	// Initialize the SQLContext from SparkContext
	private static final SQLContext sqlContext = new SQLContext(sc);

	public static void main(String[] args) throws IOException {

		String brokers = "kit-b1:9092,kit-b2:9092,kit-b3:9092";// kafka集群配置
		final String topics = "system,Disconnector,Breaker,OGG_TOPIC,oggtopic";// 需要处理的对应主题
		InputStream is = KfkToHive.class.getClassLoader()
				.getResourceAsStream("kfk-test.properties");
		Properties p = new Properties();
		// 加载资源
		p.load(is);
		// // kafka认证鉴权
		// AuthenticationManager.setAuthMethod("kerberos");
		// AuthenticationManager.login("kafka/kit-b1@TDH",
		// //
		// TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath()
		// p.getProperty("kafka_key_path").trim());

		// spark上下文配置
		// SparkConf sparkConf = new
		// SparkConf().setAppName("MsgFromkfkToHive").setMaster("local");

		JavaStreamingContext jssc = new JavaStreamingContext(sc,
				Durations.seconds(1));
		logger.debug("加载上下文成功！");
		HashSet<String> topicsSet = new HashSet<String>(
				Arrays.asList(topics.split(",")));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);
		// JavaStreamingContext.jarOfClass(arg0)

		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils
				.createDirectStream(jssc, String.class, String.class,
						StringDecoder.class, StringDecoder.class, kafkaParams,
						topicsSet);
		logger.debug("加载kafka配置成功！");

		// The method foreachRDD(Function<JavaPairRDD<String,String>,Void>)
		// in the type JavaPairDStream<String,String> is not applicable for
		// the arguments (new Void Function<JavaPairRDD<String,String>>(){})
		// Get the lines, load to sqlContext
		// messages.foreachRDD(new VoidFunction<JavaPairRDD<String,String>>() {
		// private static final long serialVersionUID = 1L;
		//
		// public void call(JavaPairRDD<String, String> t) throws Exception {
		// if(t.count() < 1) return ;
		//// DataFrame df = sqlContext.read().json(t.values());
		//// df.show();
		// }
		// });

		/**
		 * 使用以下方法之一触发： print() foreachRDD() saveAsObjectFiles()
		 * saveAsTextFiles() saveAsHadoopFiles()
		 * 
		 * 这里采用foreacherdd 遍历kafka数据
		 */
		messages.foreachRDD(
				new Function2<JavaPairRDD<String, String>, Time, Void>() {

					@Override
					public Void call(JavaPairRDD<String, String> rdd, Time time)
							throws Exception {

						rdd.foreach(new VoidFunction<Tuple2<String, String>>() {
							@Override
							public void call(Tuple2<String, String> tuple)
									throws Exception {
								logger.debug("ogg mes"+tuple._2());
								String[] kafka = SPACE.split(tuple._2());
								String json = tuple._2();
								// logger.debug("################### insert
								// hbase starting......." + tuple._2());
								// [Disconnector, 白兔, 110kV1M2M分段1M母线侧1001刀闸,
								// Cbaitu.1001, 1, 0]
//								pareDataToHive(kafka);
								pareDataToHive(json);

								// logger.debug("################### insert
								// hbase ending.......");

								System.out.println("add success" + tuple._2());
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

	@SuppressWarnings("unchecked")
	private static void pareDataToHive(String kafka) {

		/**
		 * 数据入库 获取到的是kafka数据 如何灵活的设计数据库 需要仔细构思 解析kfk生产的消息后 对msg进行解析 入库不同的数据库
		 * 
		 * {"table":"BDTEST.TEST_KPI_SCORE","op_type":"I","op_ts":
		 * "2016-11-09 07:16:57.385885"
		 * ,"current_ts":"2016-11-10T17:29:12.479000","pos":
		 * "00000000-10000001391","after":{"SCORE_ID":0,"KPI_CYCLE":"4",
		 * "EVALUATE_ID":"MM_20_04_03","DEPT_CODE":"0302","P_ORGANIZATION_CODE":
		 * null,"YM":"200912","NUMERATOR":"0","DENOMINATOR":"0","SCORE":"0",
		 * "SUM_SCORE":null,"STATUS":null,"ETL_DT":"20120814151900"}}
		 */
		
		 try {
			 if(""!=kafka){//判断kafka的消息体是否符合解析的规则 是否包含必须有的字段
				 JSONObject obj = new JSONObject(kafka);
				String tableName= obj.get("table").toString();
				logger.debug("tableName"+tableName);
				String[] userTable=tableName.split("\\.");
				logger.debug("owner"+userTable[0]);
				logger.debug("table name is"+userTable[1]);
				String op_type= obj.get("op_type").toString();
				logger.debug("op_type is"+op_type);
				
					/*
					 * 解析字符串数据kafka 获取table名称 操作类型 操作时间 等相关字段信息 insert 与update操作需要匹配现有字段名称
					 * 这个保留意见 再进行讨论
					 **/
					if ("I".equals(op_type)) {
						// 匹配字段 获取表名称与表的相关信息
						
//						map.put("OWNER", OWNER);
//						map.put("TABLE_NAME", TABLE_NAME);
//						map.put("TABLE_COLS", TABLE_COLS);
//						map.put("TABLE_PK", TABLE_PK);
						//继续添加 需要解析的字段名称
						 JSONObject columns = new JSONObject(obj.get("after").toString());  //获取列信息
						 logger.debug("columns is"+obj.get("after").toString());
						if(userTable[1]==null||"".equals(userTable[1])){
							return;
						}
						Map<String, String> map = getTablesValues(userTable[1]);
						if(!map.get("OWNER").equalsIgnoreCase(userTable[0])||!map.get("TABLE_NAME").equalsIgnoreCase(userTable[1])){
							
						}
						String[] cls =map.get("TABLE_COLS").toString().split(",");//获取配置表的列字段名称
						logger.debug("cls"+cls.toString());
						String value=null;
						StringBuffer skey= new StringBuffer();//组装columns字段名称
						StringBuffer svalue= new StringBuffer();//组装values
						int i=0;
						for (String string : cls) {
							i++;
							value= columns.get(string).toString();//根据配置表的列获取kafka过来的value值 循环遍历字段与value值 拼装动态sql
							
							if(i%cls.length==0){//拼装sql
								skey.append(string);
								svalue.append("\'"+value+"\'");
							}else{
								skey.append(string+",");
								svalue.append("\'"+value+"\'"+",");
							}
						}
							
						String sql = "Insert into "+userTable[1]+"("+skey.toString()+") values ("+svalue.toString()+")";
						logger.debug("the insertsql is:"+sql);
						
						JDBCUtils.operation(sql);
					} else if ("U".equals(op_type)) {
						//继续添加 需要解析的字段名称
						 JSONObject columns = new JSONObject(obj.get("after").toString());  //获取列信息
						 logger.debug("columns is"+obj.get("after").toString());
						// 匹配字段 获取表名称与表的相关信息
//						update SM_PRIV_MENU_USER set UPD_USER='admin123'  , menu_id ='1' where id='201701162519501' and pid="";
						Map<String, String> map = getTablesValues(userTable[1]);
						String[] cls =map.get("TABLE_COLS").toString().split(",");//获取配置表的列字段名称
					
						logger.debug("cls"+cls.toString());
						String value=null;
						StringBuffer skey= new StringBuffer();//组装需修改的值
						StringBuffer svalue= new StringBuffer();//组装pk值
						
						String clusterId=null;
						String[] TABLE_PK=map.get("TABLE_PK").toString().split(",");
						int j=0;
						//取pk值
						for (String string : TABLE_PK) {
							clusterId=string;
							value= columns.get(string).toString();
							j++;
							if(j%TABLE_PK.length==0){//拼装sql
								svalue.append(string+"="+"\'"+value+"\'");
							}else{
								svalue.append(string+"="+"\'"+value+"\'"+"and ");
							}
						}
						int i=0;
//						取set的相关值 需过滤掉hive有关桶的字段
					
						for (String string : cls) {
							value= columns.get(string).toString();
							i++;
							if(!string.equals(clusterId)){
								if(i%cls.length==0){//拼装sql
									skey.append(string+"="+"\'"+value+"\'");
								}else{
									skey.append(string+"="+"\'"+value+"\'"+" , ");
								}
							}
							
						}
						String sql = "update "+userTable[1] +" set " + skey.toString() +" where "+svalue.toString();
					logger.debug("the update sql is"+ sql);
						JDBCUtils.operation(sql);
//						JDBCUtils.update();

					} else if ("D".equals(op_type)) {
						//继续添加 需要解析的字段名称
						 JSONObject columns = new JSONObject(obj.get("before").toString());  //获取列信息
						 logger.debug("columns is"+obj.get("before").toString());
//						delete from acidTable where name='bbb'
						Map<String, String> map = getTablesValues(userTable[1]);
						String value=null;
						StringBuffer skey= new StringBuffer();//组装需修改的值
						StringBuffer svalue= new StringBuffer();//组装pk值
						
						String clusterId=null;
						String[] TABLE_PK=map.get("TABLE_PK").toString().split(",");
						int j=0;
						//取pk值
						for (String string : TABLE_PK) {
							clusterId=string;
							value= columns.get(string).toString();
							j++;
							if(j%TABLE_PK.length==0){//拼装sql
								svalue.append(string+"="+"\'"+value+"\'");
							}else{
								svalue.append(string+"="+"\'"+value+"\'"+"and ");
							}
						}
						String sql="delete from "+userTable[1] +" where "+svalue.toString();
						JDBCUtils.operation(sql);
						logger.debug("add success!"+sql);
					}
			 }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据表名获取关系型数据表的相关信息
	 * 
	 * @param tablesName
	 * @return
	 */
	public static Map<String, String> getTablesValues(String tablesName) {
		// KIT_DEV SM_PRIV_MENU_USER
		// ID,EIPID,MENU_ID,UPD_USER,UPD_DATE,CRT_USER,CRT_DATE ID
		/**
		 * 链接oracle数据库
		 */
		Map<String, String> options = new HashMap<String, String>();
		// options.put("drive", "com.oracle.jdbc.Driver");
		// options.put("url", "jdbc:oracle:thin:@172.16.19.61:1521:AADC");
		// options.put("dbtable", "test_spark");// 读取配置表
		// options.put("user", "kit_dev");
		// options.put("password", "kit_dev");
		//

		options.put("drive", "com.oracle.jdbc.Driver");
		options.put("url", "jdbc:oracle:thin:@172.16.19.61:1521:orcl");
		options.put("dbtable", "test_spark");
		options.put("user", "KIT_DEV");
		options.put("password", "KIT_DEV");

		// sqlContext.read().format("jdbc").options(options).load().toDF()
		// .registerTempTable("t1");
		// sqlContext.read().format("jdbc").options(options).load().toDF()
		// .registerTempTable("t2");

		// 加载所有数据 http://blog.csdn.net/dabokele/article/details/52802150
		DataFrame jdbcDF = sqlContext.read().format("jdbc").options(options)
				.load();
		jdbcDF.show();
//		List list = jdbcDF.collectAsList();
//		logger.debug("获取第一行的信息" + jdbcDF.first());
//
//		logger.debug("获取单个字段的信息" + jdbcDF.select("PROJECT_ID").first());
//
//		logger.debug("get the data where project id equals"
//				+ jdbcDF.where("PROJECT_ID = 'GZ1500002725' ").first());
		// 如果配置表是多个table取数据出来放入集合
//		String TABLE_COLS = (jdbcDF.where("TABLE_NAME = 'DIM_LCAM_PROJECT_TYPE' ").select("TABLE_COLS").first())// 这里传表名
//				.toString();// 切分数组 获取具体的数据
		String TABLE_COLS = (jdbcDF.where("TABLE_NAME = '"+tablesName+"' ").select("TABLE_COLS").first())// 这里传表名
				.toString();// 切分数组 获取具体的数据
		TABLE_COLS=TABLE_COLS.substring(1);
		TABLE_COLS=TABLE_COLS.substring(0, TABLE_COLS.length()-1);
		logger.debug("TABLE_COLS"+TABLE_COLS);
		String OWNER =  (jdbcDF.where("TABLE_NAME = '"+tablesName+"' ").select("OWNER").first())// 这里传表名
				.toString();
				logger.debug("OWNER"+OWNER);
		String TABLE_NAME =  (jdbcDF.where("TABLE_NAME ='"+tablesName+"' ").select("TABLE_NAME").first())// 这里传表名
				.toString();
		logger.debug("TABLE_NAME"+TABLE_NAME);
		String TABLE_PK = (jdbcDF.where("TABLE_NAME ='"+tablesName+"' ").select("TABLE_PK").first())// 这里传表名
				.toString();
		TABLE_PK=TABLE_PK.substring(1);
		TABLE_PK=TABLE_PK.substring(0, TABLE_PK.length()-1);
		logger.debug("TABLE_PK"+TABLE_PK);

		Map<String, String> map = new HashMap<String, String>();
		map.put("OWNER", OWNER);
		map.put("TABLE_NAME", TABLE_NAME);
		map.put("TABLE_COLS", TABLE_COLS);
		map.put("TABLE_PK", TABLE_PK);
		return map;

	}

}

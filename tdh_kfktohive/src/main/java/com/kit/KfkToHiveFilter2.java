package com.kit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import scala.Tuple2;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.collect.Lists;

import kafka.auth.AuthenticationManager;
import kafka.message.MessageAndMetadata;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;

import org.apache.hadoop.hive.ql.exec.spark.session.SparkSession;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaRDD;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;

/**
 * @author gh
 * @createTime 2017/01/10
 * @description 通过spark streaming 读取kafka消息后入库到hive数据库 匹配规则 得到结果入库
 */

public final class KfkToHiveFilter2 {
	private static final Pattern SPACE = Pattern.compile(" ");
	private static Logger logger = Logger.getLogger(KfkToHiveFilter2.class);

	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf().setMaster("local[*]")
					.setAppName("kfkToHiveFilter"));
	// Initialize the SQLContext from SparkContext
	private static final SQLContext sqlContext = new SQLContext(sc);
	

	public static void main(String[] args) throws IOException {

		String brokers = "kit-b1:9092,kit-b2:9092,kit-b3:9092";// kafka集群配置
		final String topics = "oggtopic";// 需要处理的对应主题system,Disconnector,Breaker,OGG_TOPIC,
		InputStream is = KfkToHiveFilter2.class.getClassLoader()
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
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);
		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils
				.createDirectStream(jssc, String.class, String.class,
						StringDecoder.class, StringDecoder.class, kafkaParams,
						topicsSet);
		
//		JavaInputDStream<byte[]> stream = KafkaUtils.createDirectStream(jssc,
//				String.class, byte[].class, StringDecoder.class,
//				DefaultDecoder.class, byte[].class, kafkaParams,topicsSet);
		logger.debug("加载kafka配置成功！");

		final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference();
		
		messages.transformToPair(
				new Function<JavaPairRDD<String, String>, JavaPairRDD<String, String>>() {
					@Override
					public JavaPairRDD<String, String> call(
							JavaPairRDD<String, String> rdd) throws Exception {
						OffsetRange[] offsets = ((KafkaRDD<?, ?, ?, ?, ?>) rdd
								.rdd()).offsetRanges();
						offsetRanges.set(offsets);
						return rdd;
					}
				}).map(new Function<Tuple2<String, String>, String>() {
					@Override
					public String call(Tuple2<String, String> tuple2) {
						String[] kafka = SPACE.split(tuple2._2());
						System.out.println(kafka.toString());
						return tuple2._2();
					}
				}).foreachRDD(new Function<JavaRDD<String>, Void>() {
					@Override
					public Void call(JavaRDD<String> rdd) throws IOException {
						for (OffsetRange o : offsetRanges.get()) {
							logger.debug(o.topic() + " " + o.partition()
									+ " " + o.fromOffset() + " "
									+ o.untilOffset()+":"+	o.toString());
						}
						return null;
					}
				});

		// messages.transformToPair(
		// new Function<JavaPairRDD<String, String>, JavaPairRDD<String,
		// String>>() {
		// @Override
		// public JavaPairRDD<String, String> call(
		// JavaPairRDD<String, String> rdd) throws Exception {
		// OffsetRange[] offsets = ((KafkaRDD<?, ?, ?, ?, ?>) rdd
		// .rdd()).offsetRanges();
		// offsetRanges.set(offsets);
		// return rdd;
		// }
		// });// .map(f)
		
		/**
		 * 
		 * 这里采用foreacherdd 遍历kafka数据
		 */
		messages.foreachRDD(new Function<JavaPairRDD<String, String>, Void>() {

			@Override
			public Void call(JavaPairRDD<String, String> rdd) throws Exception {

				rdd.foreach(new VoidFunction<Tuple2<String, String>>() {
					@Override
					public void call(Tuple2<String, String> tuple)
							throws Exception {
						logger.debug("ogg mes" + tuple._2());
						String[] kafka = SPACE.split(tuple._2());
						String json = tuple._2();
						// pareDataToHive(json);
						long beginTime = System.currentTimeMillis();
						checkData(json);
						long endTime = System.currentTimeMillis();
						logger.debug("共计花费时间 多少毫秒" + (endTime - beginTime));
						logger.debug("the foreach kafka data is:" + json);
					}
				});
				return null;
			}
		});
		jssc.start();
		jssc.awaitTermination();
	}

	private static void checkData(String kafka) throws SQLException {

		try {
			if ("" != kafka) {// 判断kafka的消息体是否符合解析的规则 是否包含必须有的字段
				JSONObject obj = new JSONObject(kafka);
				String tableName = obj.get("table").toString();
				logger.debug("tableName" + tableName);
				String[] userTable = tableName.split("\\.");
				logger.debug("owner" + userTable[0]);
				logger.debug("table name is" + userTable[1]);
				String op_type = obj.get("op_type").toString();
				logger.debug("op_type is" + op_type);

				String tempdata = obj.get("after").toString();
				logger.debug("tempdata is" + tempdata);
				JSONObject columns = new JSONObject(
						obj.get("after").toString()); // 获取列信息
				logger.debug("columns is" + obj.get("after").toString());

				// +++++++++++++++++++++++++++++++++++++++++++++++++入库前
				// 校验逻辑+++++++++++++++++++++++++++++++++++++
				// 遍历多条数据 封装为dataframe
				// tempdata=tempdata.replace("\"", "\"");
				logger.debug("the data changed is:" + tempdata);// 转化为rdd能够识别的json

				JavaRDD<String> rdd = sc.parallelize(Arrays.asList(tempdata));// 获取kafka消息
				DataFrame peopleFromJsonRDD = sqlContext.read().json(rdd.rdd());// 转换成dataframe
				// Take a look at the schema of this new DataFrame.
				peopleFromJsonRDD.registerTempTable("tmptable");// 注册临时表
				logger.debug("be here ,register temptable success!");
				// 取配置表 需遍历多次
				logger.debug(
						"before get the properties table,before get the rule_cfg's rule_sql!");

				// DataFrame jdbcDF1 = sqlContext
				// .sql("select rule_sql from kitdev.rule_cfg where table_name="
				// + userTable[1] + " ");// 从hive表中取出过滤条件数据
				// Row[] lengths1 = jdbcDF1.collect();
				// for (Row row : lengths1) {
				// System.out.println(row.get(0));// 获取的是条件
				//
				// DataFrame jdbcDF = sqlContext
				// .sql("SELECT count(1) FROM tmptable where 1=1" + row.get(0)
				// + "");// 从hive表中取出数据过滤数据
				//
				// //程序每遍历一次 获取到一条数据 如有数据 则取出结果 入库存储表
				//
				//
				//
				// }

				Map<String, String> map = getTablesValues(userTable[1]);// 判断表是否存在
																		// 这里要做异常处理

				String value = null;
				StringBuffer skey = new StringBuffer();// 组装columns字段名称
				StringBuffer svalue = new StringBuffer();// 组装values
				StringBuffer pks = new StringBuffer();// 组装values
				String[] TABLE_PK = map.get("TABLE_PK").toString().split(",");
				int j = 0;
				// 取pk值
				for (String string : TABLE_PK) {
					// value = columns.get(string).toString()+":";
					pks.append(columns.get(string).toString() + ",");
				}
				// List<String> list = getRULE_RANGE_COL_NAME(userTable[1]);//
				// 根据表名获取range
				// col
				// name
				Set<String> set = getRULE_RANGE_COL_NAME_Set(userTable[1]);// 根据表名获取range
																			// col
				// name
				// set.iterator();
				// logger.debug("get the rule_cfg success,the list is"+list);
				// 判断数据来源的操作类型 是insert操作还是update操作

				if ("I".equals(op_type)) {
					for (String cls : set) {
						if (cls != null && !cls.equals("null")) {

							String rangecolsvalue = columns.get(cls).toString();// 遍历获取rangecolsvalue的值
							Map<String, String> sqlList = getRULE_RANGE_VALUE(
									userTable[1], cls, rangecolsvalue);// 根据表名
																		// col
																		// name值
																		// col
																		// value值获取sql
							for (Entry<String, String> entry : sqlList
									.entrySet()) {
								// Map.entry<Integer,String> 映射项（键-值对）
								// 有几个方法：用上面的名字entry
								// entry.getKey() ;entry.getValue();
								// entry.setValue();
								// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
								System.out.println("key= " + entry.getKey()
										+ " and value= " + entry.getValue());
								DataFrame jdbcDF = sqlContext
										.sql("SELECT * FROM tmptable where 1=1 and"
												+ "" + entry.getValue());// 读取临时表过滤数据准备入库
								logger.debug("begin get the data");// 开始遍历数据
																	// 如果根据配置表的条件获取到数据则
																	// 操作什么，没有数据则怎么操作
																	// 有数据 通过
																	// 没有数据就不通过
								long count = jdbcDF.count();// 获取结果
								System.out.println("thetablecount:" + count);
								if (count > 0) {// 判断结果 入库操作 有数据 不通过 存0 无数据通过存1
									long currentTime = System
											.currentTimeMillis();// 当前操作时间
									String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
											+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
											+ ") values (" + "'"
											+ entry.getKey() + "'," + "'"
											+ userTable[1] + "'," + "'" + pks
											+ "'," + "'"
											+ System.currentTimeMillis() + "',"
											+ "'" + "0" + "'" + ")";
									JDBCUtils.operation(sql);
									logger.debug("the insertsql is:" + sql);// 校验不通过
								} else if (count < 1) {
									String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
											+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
											+ ") values (" + "'"
											+ entry.getKey() + "'," + "'"
											+ userTable[1] + "'," + "'" + pks
											+ "'," + "'"
											+ System.currentTimeMillis() + "',"
											+ "'" + "1" + "'" + ")";
									// sqlContext.sql(sql);
									// jdbcDF.toDF().insertInto("tableName");这两种都是数据入库的操作方式
									// 年后回来进行测试 部署到集群
									jdbcDF.toDF().saveAsTable("");
									JDBCUtils.operation(sql);
									logger.debug("the insertsql is:" + sql);// 校验通过
								}
							}

						} else {

							Map<String, String> sqlList = getRULE_RANGE_VALUE(
									userTable[1]);// 根据表名 col name值 col
													// value值获取sql
							for (Entry<String, String> entry : sqlList
									.entrySet()) {
								logger.debug("key= " + entry.getKey()
										+ " and value= " + entry.getValue());
								try {
									if (entry.getKey().equals("10")
											|| entry.getKey().equals("12")
											|| entry.getKey().equals("20")) {
										System.out.println("test");
									}
									DataFrame jdbcDF = sqlContext
											.sql("SELECT * FROM tmptable T where 1=1 and "
													+ entry.getValue());// 读取临时表过滤数据准备入库
									// T.MID_VOL_RATED_CAPACITY IS NULL
									logger.debug("begin get the data,the sql is"
											+ "SELECT * FROM tmptable T where 1=1 and "
											+ entry.getValue());// 开始遍历数据
																// 如果根据配置表的条件获取到数据则
																// 操作什么，没有数据则怎么操作
																// 有数据 通过
																// 没有数据就不通过
									long count = jdbcDF.count();// 获取结果
									System.out
											.println("thetablecount:" + count);
									if (count > 0) {// 判断结果 入库操作 有数据 不通过 存0
													// 无数据通过存1
										long currentTime = System
												.currentTimeMillis();// 当前操作时间
										String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
												+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
												+ ") values (" + "'"
												+ entry.getKey() + "'," + "'"
												+ userTable[1] + "'," + "'"
												+ pks + "'," + "'"
												+ System.currentTimeMillis()
												+ "'," + "'" + "0" + "'" + ")";
										JDBCUtils.operation(sql);
										logger.debug("the insertsql is:" + sql);// 校验不通过
									} else if (count < 1) {
										String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
												+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
												+ ") values (" + "'"
												+ entry.getKey() + "'," + "'"
												+ userTable[1] + "'," + "'"
												+ pks + "'," + "'"
												+ System.currentTimeMillis()
												+ "'," + "'" + "1" + "'" + ")";
										JDBCUtils.operation(sql);
										logger.debug("the insertsql is:" + sql);// 校验通过
									}
								} catch (Exception e) {
									logger.error(e.getStackTrace());
								}

							}

						}

					}

				} else if ("U".equals(op_type)) {

					for (String cls : set) {
						if (cls != null && !cls.equals("null")) {

							String rangecolsvalue = columns.get(cls).toString();// 遍历获取rangecolsvalue的值
							Map<String, String> sqlList = getRULE_RANGE_VALUE(
									userTable[1], cls, rangecolsvalue);// 根据表名
																		// col
																		// name值
																		// col
																		// value值获取sql
							for (Entry<String, String> entry : sqlList
									.entrySet()) {
								// Map.entry<Integer,String> 映射项（键-值对）
								// 有几个方法：用上面的名字entry
								// entry.getKey() ;entry.getValue();
								// entry.setValue();
								// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
								System.out.println("key= " + entry.getKey()
										+ " and value= " + entry.getValue());
								DataFrame jdbcDF = sqlContext
										.sql("SELECT * FROM tmptable where 1=1 and"
												+ "" + entry.getValue());// 读取临时表过滤数据准备入库
								logger.debug("begin get the data");// 开始遍历数据
																	// 如果根据配置表的条件获取到数据则
																	// 操作什么，没有数据则怎么操作
																	// 有数据 通过
																	// 没有数据就不通过
								long count = jdbcDF.count();// 获取结果
								System.out.println("thetablecount:" + count); // 判断结果集
								if (count > 0) {// 判断结果 入库操作 有数据 不通过 存0 无数据通过存1
									long currentTime = System
											.currentTimeMillis();// 当前操作时间
									String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
											+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
											+ ") values (" + "'"
											+ entry.getKey() + "'," + "'"
											+ userTable[1] + "'," + "'" + pks
											+ "'," + "'"
											+ System.currentTimeMillis() + "',"
											+ "'" + "0" + "'" + ")";
									JDBCUtils.operation(sql);
									logger.debug("the insertsql is:" + sql);// 校验不通过
								} else if (count < 1) {
									String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
											+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
											+ ") values (" + "'"
											+ entry.getKey() + "'," + "'"
											+ userTable[1] + "'," + "'" + pks
											+ "'," + "'"
											+ System.currentTimeMillis() + "',"
											+ "'" + "1" + "'" + ")";
									JDBCUtils.operation(sql);
									logger.debug("the insertsql is:" + sql);// 校验通过
								}
							}

						} else {

							Map<String, String> sqlList = getRULE_RANGE_VALUE(
									userTable[1]);// 根据表名 col name值 col
													// value值获取sql
							for (Entry<String, String> entry : sqlList
									.entrySet()) {
								logger.debug("key= " + entry.getKey()
										+ " and value= " + entry.getValue());
								try {
									DataFrame jdbcDF = sqlContext
											.sql("SELECT * FROM tmptable T where 1=1 and "
													+ entry.getValue());// 读取临时表过滤数据准备入库
									// T.MID_VOL_RATED_CAPACITY IS NULL
									logger.debug("begin get the data,the sql is"
											+ "SELECT * FROM tmptable T where 1=1 and "
											+ entry.getValue());// 开始遍历数据
																// 如果根据配置表的条件获取到数据则
																// 操作什么，没有数据则怎么操作
																// 有数据 通过
																// 没有数据就不通过
									long count = jdbcDF.count();// 获取结果
									System.out
											.println("thetablecount:" + count);

									boolean check = checkRuleDataExist(
											entry.getKey(), userTable[1],
											pks.toString());// 总感觉只传结果值
															// 不行，pk的key也需传进来
															// 判断值是否存在

									if (check == true) {

										if (count > 0) {// 判断结果 入库操作 有数据 不通过 存0
														// 无数据通过存1
											// 组织更新sql
											String updateSql = "update "
													+ " kitdev.RULE_CHECK_RESULT_TODAY "
													+ " set  rule_check_time='"
													+ System.currentTimeMillis()
													+ "'," + " check_result='"
													+ 0 + "'"
													+ " where rule_id='"
													+ entry.getKey()
													+ "' and table_name='"
													+ userTable[1]
													+ "'and table_pk_value='"
													+ pks + "'";
											JDBCUtils.operation(updateSql);
											logger.debug("the insertsql is:"
													+ updateSql);// 校验不通过
										} else if (count < 1) {
											String updateSql = "update "
													+ " kitdev.RULE_CHECK_RESULT_TODAY "
													+ " set rule_check_time='"
													+ System.currentTimeMillis()
													+ "'," + "check_result='"
													+ 1 + "'"
													+ " where rule_id='"
													+ entry.getKey()
													+ "' and table_name='"
													+ userTable[1]
													+ "'and table_pk_value='"
													+ pks + "'";
											JDBCUtils.operation(updateSql);
											logger.debug("the insertsql is:"
													+ updateSql);// 校验通过
										}

									} else {

										if (count > 0) {// 判断结果 入库操作 有数据 不通过 存0
														// 无数据通过存1
											long currentTime = System
													.currentTimeMillis();// 当前操作时间
											String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
													+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
													+ ") values (" + "'"
													+ entry.getKey() + "',"
													+ "'" + userTable[1] + "',"
													+ "'" + pks + "'," + "'"
													+ System.currentTimeMillis()
													+ "'," + "'" + "0" + "'"
													+ ")";
											JDBCUtils.operation(sql);
											logger.debug(
													"the insertsql is:" + sql);// 校验不通过
										} else if (count < 1) {
											String sql = "Insert into kitdev.RULE_CHECK_RESULT_TODAY"
													+ "(rule_id,table_name,table_pk_value,rule_check_time,check_result"
													+ ") values (" + "'"
													+ entry.getKey() + "',"
													+ "'" + userTable[1] + "',"
													+ "'" + pks + "'," + "'"
													+ System.currentTimeMillis()
													+ "'," + "'" + "1" + "'"
													+ ")";
											JDBCUtils.operation(sql);
											logger.debug(
													"the insertsql is:" + sql);// 校验通过
										}
									}

								} catch (Exception e) {
									logger.error(e.getStackTrace());
								}

							}

						}

					}

				} else if ("D".equals(op_type)) {
					// 继续添加 需要解析的字段名称
					JSONObject columnsb = new JSONObject(
							obj.get("before").toString()); // 获取列信息
					logger.debug("columns is" + obj.get("before").toString());
					// delete from acidTable where name='bbb'
					// Map<String, String> map1 = getTablesValues(userTable[1]);
					// String value = null;
					// StringBuffer skey = new StringBuffer();// 组装需修改的值
					// StringBuffer svalue = new StringBuffer();// 组装pk值
					//
					// String clusterId = null;
					// String[] TABLE_PK = map.get("TABLE_PK").toString()
					// .split(",");
					// int j = 0;
					// 取pk值
					for (String string : TABLE_PK) {
						// clusterId = string;
						value = columnsb.get(string).toString();
						j++;
						if (j % TABLE_PK.length == 0) {// 拼装sql
							svalue.append(string + "=" + "\'" + value + "\'");
						} else {
							svalue.append(string + "=" + "\'" + value + "\'"
									+ "and ");
						}
					}
					String sql = "delete from " + userTable[1] + " where "
							+ svalue.toString();
					JDBCUtils.operation(sql);
					logger.debug("delete success!" + sql);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 判断校验规则后的数据结果是否存在
	 * 
	 * @param ruleId
	 * @param tableName
	 * @param pk
	 * @return
	 * @throws SQLException
	 */
	private static boolean checkRuleDataExist(String ruleId, String tableName,
			String pk) throws SQLException {

		boolean check = false;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		String get_rule_sql = "select rule_id  from kitdev.RULE_CHECK_RESULT_TODAY where table_name="
				+ "'" + tableName + "'" + " and rule_id ='" + ruleId
				+ "' and  table_pk_value='" + pk + "'";
		logger.debug("get_rule_sql: " + get_rule_sql);
		int i = 0;
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_rule_sql);
			// 取出数据
			while (rs.next()) {
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		if (i > 0) {
			check = true;
			return check;
		} else {
			return check;
		}

	}
	/**
	 * 获取到的是kafka数据 如何灵活的设计数据库 需要仔细构思 解析kfk生产的消息后 对msg进行解析 入库不同的数据表
	 * 
	 * @param kafka
	 */
	@SuppressWarnings("unchecked")
	private static void pareDataToHive(String kafka) {

		/**
		 * 数据入库 获取到的是kafka数据 如何灵活的设计数据库 需要仔细构思 解析kfk生产的消息后 对msg进行解析 入库不同的数据表
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
			if ("" != kafka) {// 判断kafka的消息体是否符合解析的规则 是否包含必须有的字段
				JSONObject obj = new JSONObject(kafka);
				String tableName = obj.get("table").toString();
				logger.debug("tableName" + tableName);
				String[] userTable = tableName.split("\\.");
				logger.debug("owner" + userTable[0]);
				logger.debug("table name is" + userTable[1]);
				String op_type = obj.get("op_type").toString();
				logger.debug("op_type is" + op_type);

				String tempdata = obj.get("after").toString();

				logger.debug("tempdata is" + tempdata);
				/*
				 * 解析字符串数据kafka 获取table名称 操作类型 操作时间 等相关字段信息 insert
				 * 与update操作需要匹配现有字段名称 这个保留意见 再进行讨论
				 **/
				if ("I".equals(op_type)) {
					// 匹配字段 获取表名称与表的相关信息
					// 继续添加 需要解析的字段名称
					JSONObject columns = new JSONObject(
							obj.get("after").toString()); // 获取列信息
					logger.debug("columns is" + obj.get("after").toString());
					if (userTable[1] == null || "".equals(userTable[1])) {
						return;
					}
					Map<String, String> map = getTablesValues(userTable[1]);
					if (!map.get("OWNER").equalsIgnoreCase(userTable[0]) || !map
							.get("TABLE_NAME").equalsIgnoreCase(userTable[1])) {

					}
					String[] cls = map.get("TABLE_COLS").toString().split(",");// 获取配置表的列字段名称
					logger.debug("cls" + cls.toString());
					String value = null;
					StringBuffer skey = new StringBuffer();// 组装columns字段名称
					StringBuffer svalue = new StringBuffer();// 组装values
					int i = 0;
					for (String string : cls) {
						i++;
						value = columns.get(string).toString();// 根据配置表的列获取kafka过来的value值
																// 循环遍历字段与value值
																// 拼装动态sql

						if (i % cls.length == 0) {// 拼装sql
							skey.append(string);
							svalue.append("\'" + value + "\'");
						} else {
							skey.append(string + ",");
							svalue.append("\'" + value + "\'" + ",");
						}
					}

					String sql = "Insert into " + userTable[1] + "("
							+ skey.toString() + ") values (" + svalue.toString()
							+ ")";
					logger.debug("the insertsql is:" + sql);

					JDBCUtils.operation(sql);
				} else if ("U".equals(op_type)) {
					// 继续添加 需要解析的字段名称
					JSONObject columns = new JSONObject(
							obj.get("after").toString()); // 获取列信息
					logger.debug("columns is" + obj.get("after").toString());
					// 匹配字段 获取表名称与表的相关信息
					// update SM_PRIV_MENU_USER set UPD_USER='admin123' ,
					// menu_id ='1' where id='201701162519501' and pid="";
					Map<String, String> map = getTablesValues(userTable[1]);
					String[] cls = map.get("TABLE_COLS").toString().split(",");// 获取配置表的列字段名称

					logger.debug("cls" + cls.toString());
					String value = null;
					StringBuffer skey = new StringBuffer();// 组装需修改的值
					StringBuffer svalue = new StringBuffer();// 组装pk值

					String clusterId = null;
					String[] TABLE_PK = map.get("TABLE_PK").toString()
							.split(",");
					int j = 0;
					// 取pk值
					for (String string : TABLE_PK) {
						clusterId = string;
						value = columns.get(string).toString();
						j++;
						if (j % TABLE_PK.length == 0) {// 拼装sql
							svalue.append(string + "=" + "\'" + value + "\'");
						} else {
							svalue.append(string + "=" + "\'" + value + "\'"
									+ "and ");
						}
					}
					int i = 0;
					// 取set的相关值 需过滤掉hive有关桶的字段

					for (String string : cls) {
						value = columns.get(string).toString();
						i++;
						if (!string.equals(clusterId)) {
							if (i % cls.length == 0) {// 拼装sql
								skey.append(string + "=" + "\'" + value + "\'");
							} else {
								skey.append(string + "=" + "\'" + value + "\'"
										+ " , ");
							}
						}

					}
					String sql = "update " + userTable[1] + " set "
							+ skey.toString() + " where " + svalue.toString();
					logger.debug("the update sql is" + sql);
					JDBCUtils.operation(sql);
					// JDBCUtils.update();

				} else if ("D".equals(op_type)) {
					// 继续添加 需要解析的字段名称
					JSONObject columns = new JSONObject(
							obj.get("before").toString()); // 获取列信息
					logger.debug("columns is" + obj.get("before").toString());
					// delete from acidTable where name='bbb'
					Map<String, String> map = getTablesValues(userTable[1]);
					String value = null;
					StringBuffer skey = new StringBuffer();// 组装需修改的值
					StringBuffer svalue = new StringBuffer();// 组装pk值

					String clusterId = null;
					String[] TABLE_PK = map.get("TABLE_PK").toString()
							.split(",");
					int j = 0;
					// 取pk值
					for (String string : TABLE_PK) {
						clusterId = string;
						value = columns.get(string).toString();
						j++;
						if (j % TABLE_PK.length == 0) {// 拼装sql
							svalue.append(string + "=" + "\'" + value + "\'");
						} else {
							svalue.append(string + "=" + "\'" + value + "\'"
									+ "and ");
						}
					}
					String sql = "delete from " + userTable[1] + " where "
							+ svalue.toString();
					JDBCUtils.operation(sql);
					logger.debug("add success!" + sql);
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

		// 加载所有数据 http://blog.csdn.net/dabokele/article/details/52802150
		DataFrame jdbcDF = sqlContext.read().format("jdbc").options(options)
				.load();
		jdbcDF.show();
		// List list = jdbcDF.collectAsList();
		// logger.debug("获取第一行的信息" + jdbcDF.first());
		//
		// logger.debug("获取单个字段的信息" + jdbcDF.select("PROJECT_ID").first());
		//
		// logger.debug("get the data where project id equals"
		// + jdbcDF.where("PROJECT_ID = 'GZ1500002725' ").first());
		// 如果配置表是多个table取数据出来放入集合
		// String TABLE_COLS = (jdbcDF.where("TABLE_NAME =
		// 'DIM_LCAM_PROJECT_TYPE' ").select("TABLE_COLS").first())// 这里传表名
		// .toString();// 切分数组 获取具体的数据
		String TABLE_COLS = (jdbcDF.where("TABLE_NAME = '" + tablesName + "' ")
				.select("TABLE_COLS").first())// 这里传表名
						.toString();// 切分数组 获取具体的数据
		TABLE_COLS = TABLE_COLS.substring(1);
		TABLE_COLS = TABLE_COLS.substring(0, TABLE_COLS.length() - 1);
		logger.debug("TABLE_COLS" + TABLE_COLS);
		String OWNER = (jdbcDF.where("TABLE_NAME = '" + tablesName + "' ")
				.select("OWNER").first()).toString();
		logger.debug("OWNER" + OWNER);
		String TABLE_NAME = (jdbcDF.where("TABLE_NAME ='" + tablesName + "' ")
				.select("TABLE_NAME").first()).toString();
		logger.debug("TABLE_NAME" + TABLE_NAME);
		String TABLE_PK = (jdbcDF.where("TABLE_NAME ='" + tablesName + "' ")
				.select("TABLE_PK").first()).toString();
		TABLE_PK = TABLE_PK.substring(1);
		TABLE_PK = TABLE_PK.substring(0, TABLE_PK.length() - 1);
		logger.debug("TABLE_PK" + TABLE_PK);

		Map<String, String> map = new HashMap<String, String>();
		map.put("OWNER", OWNER);
		map.put("TABLE_NAME", TABLE_NAME);
		map.put("TABLE_COLS", TABLE_COLS);
		map.put("TABLE_PK", TABLE_PK);
		return map;

	}

	/**
	 * 根据表名获取配置的sql信息
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * 			@2017-01-22 添加了范围字段 需重新匹配相关信息 表名 range范围 rulesql
	 */
	public static List<String> getRuleSqlValue(String tableName)
			throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		String get_rule_sql = "select rule_sql  from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'" + "and" + " rule_range_col_name=";
		// 根据表名获取range范围
		String get_range_sql = "select rule_range_col_name,rule_range_col_value  from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'";
		// rule_range_col_name
		// rule_range_col_value
		logger.debug("get_rule_sql: " + get_rule_sql);
		List<String> list = new ArrayList<String>();
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_rule_sql);
			// 取出数据
			while (rs.next()) {
				String rule = rs.getString("rule_sql");
				list.add(rule);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		return list;
	}

	/**
	 * 根据表名获取 没有colsname的配置信息
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static Set<String> getRULE_RANGE_COL_NAME_Set(String tableName)
			throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		// 根据表名获取range范围
		String get_range_sql = "select rule_range_col_name  from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'";
		// rule_range_col_name
		// rule_range_col_value
		logger.debug("get_rule_sql: " + get_range_sql);
		Set<String> list = new HashSet<String>();
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_range_sql);
			// 取出数据
			while (rs.next()) {
				String rule = rs.getString("rule_range_col_name");
				if ("null".equals(rule) || rule == null) {
					list.add("null");
				} else {
					list.add(rule);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		return list;
	}

	/**
	 * 根据表名获取colsname
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getRULE_RANGE_COL_NAME(String tableName)
			throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		// 根据表名获取range范围
		String get_range_sql = "select rule_range_col_name  from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'";
		// rule_range_col_name
		// rule_range_col_value
		logger.debug("get_rule_sql: " + get_range_sql);
		List<String> list = new ArrayList<String>();
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_range_sql);
			// 取出数据
			while (rs.next()) {
				String rule = rs.getString("rule_range_col_name");
				list.add(rule);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		return list;
	}
	/**
	 * 获取具体的sql值 ruleid and rule_sql
	 * 
	 * @param tableName
	 * @param columns
	 * @param values
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, String> getRULE_RANGE_VALUE(String tableName,
			String column, String values) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		// 根据表名获取range范围
		String get_rule_sql = "select rule_sql ,rule_id from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'" + "and  rule_range_col_name=" + column
				+ " and rule_range_col_value =" + values;
		// rule_range_col_name
		// rule_range_col_value
		logger.debug("get_rule_sql: " + get_rule_sql);
		Map<String, String> map = new HashMap<String, String>();
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_rule_sql);
			// 取出数据
			while (rs.next()) {
				String rule_sql = rs.getString("rule_sql");
				String rule_id = rs.getString("rule_id");
				map.put(rule_id, rule_sql);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		return map;
	}
	/**
	 * 获取具体的规则id和sql值
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, String> getRULE_RANGE_VALUE(String tableName)
			throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data 根据表名 range范围 生成集合 供后续调用
		// 根据表名获取range范围
		String get_rule_sql = "select rule_sql ,rule_id from kitdev.rule_cfg where table_name="
				+ "'" + tableName + "'";
		// rule_range_col_name
		// rule_range_col_value
		logger.debug("get_rule_sql: " + get_rule_sql);
		Map<String, String> map = new HashMap<String, String>();
		// try {
		try {
			conn = JDBCUtils.getConnection();
			// 创建运行环境
			st = conn.createStatement();
			// 运行HQL语句
			rs = st.executeQuery(get_rule_sql);
			// 取出数据
			while (rs.next()) {
				String rule_sql = rs.getString("rule_sql");
				String rule_id = rs.getString("rule_id");
				map.put(rule_id, rule_sql);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.relese(conn, st, rs);
		}
		return map;
	}

}

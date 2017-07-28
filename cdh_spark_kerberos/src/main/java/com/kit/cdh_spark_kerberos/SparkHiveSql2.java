package com.kit.cdh_spark_kerberos;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

public class SparkHiveSql2 {
	public static void main(String[] args) {
		//加载hive相关配置后 创建hive table loaddata to table
		SparkConf conf = new SparkConf().setAppName("simpledemo")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		HiveContext sqlContext = new org.apache.spark.sql.hive.HiveContext(sc);
//
		sqlContext
				.sql("CREATE TABLE IF NOT EXISTS src20170216 (key INT, value STRING)");
//		sqlContext.sql(
//				"LOAD DATA LOCAL INPATH '/kv1.txt' INTO TABLE src");

//		// Queries are expressed in HiveQL.
//		Row[] results = sqlContext.sql("FROM src SELECT key, value").collect();
//		for (Row row : results) {
//			System.out.println(row.getString(0) + "," + row.getString(1) + ","
//					+ row.getString(2));
//		}

//		/**
//		 * 链接其它数据库
//		 */
//		Map<String, String> options = new HashMap<String, String>();
//		options.put("url", "jdbc:172.16.19.71:1521:AADC");
//		options.put("dbtable", "PS_SAFE_DUTY");
//		options.put("user", "kit_dev");
//		options.put("password", "kit_dev");
//		//加载所有数据
//		DataFrame jdbcDF = sqlContext.read().format("jdbc").options(options)
//				.load();
//		jdbcDF.show();
		
	}
}

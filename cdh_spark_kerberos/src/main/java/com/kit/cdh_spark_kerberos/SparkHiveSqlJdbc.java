package com.kit.cdh_spark_kerberos;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * 
 * @author guohan
 *
 *查询数据 连接other jdbc库
 */
public class SparkHiveSqlJdbc {
	
	
	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf().setMaster("local[*]")
					.setAppName("SparkHiveSqlJdbc"));
	// Initialize the SQLContext from SparkContext
	private static final SQLContext sqlContext = new SQLContext(sc);
	public static void main(String[] args) {
		/**
		 * 链接其它数据库
		 */
		Map<String, String> options = new HashMap<String, String>();
		
		
		options.put("drive", "com.oracle.jdbc.Driver");
		options.put("url", "jdbc:oracle:thin:@172.16.19.71:1521:AADC");
		options.put("dbtable", "PS_SAFE_DUTY");
		options.put("user", "kit_dev");
		options.put("password", "kit_dev");

		sqlContext.read().format("jdbc").options(options).load().toDF().registerTempTable("t1");
//		JavaRDD<String> lines = sc.textFile("d:\\krb5.conf", 1);
		
		// Load oracle query result as DataFrame
//		DataFrame jdbcDF = sqlContext.load("jdbc", options);
//		jdbcDF.show();
		sqlContext.read().format("jdbc").options(options).load().toDF().registerTempTable("t2");
		DataFrame jdbcDF = sqlContext.sql(
		"SELECT"
		+ " u.SAFE_DUTY_TOPIC"
		+ " FROM t1 u ").toDF();
//		+ " join t2 p"
//		+ " ON u.PROJECT_ID = p.PROJECT_ID").toDF();
//		DataFrame jdbcDF = sqlContext.sql(
//				"SELECT * from user").toDF();
		jdbcDF.show();
		
//		 Row[] result = topTweets.collect();
//		 DataFrame tweetLength = sqlCtx.sql("SELECT stringLengthJava('text') FROM tweets LIMIT 10");
		    Row[] lengths = jdbcDF.collect();
		    for (Row row : lengths) {
		      System.out.println(row.get(0));
		    }
//		sc.stop();
		
		
	
		//++++++++++++++++++++++scala hive spark++++++++++++++++++++
//		sqlContext.sql("insert into table2 ");
//		val data = sc.textFile("path").map(x=>x.split("\\s+")).map(x=>Person(x(0),x(1).toInt,x(2)))
//			    data.toDF().registerTempTable("table1")
//			    hiveContext.sql("insert into table2 partition(date='2015-04-02') select name,col1,col2 from table1")
//		case class Person(name:String,col1:Int,col2:String)
//		 
//		def main(args:Array[String]){
//		    val sc = new org.apache.spark.SparkContext  
//		    val hiveContext = new org.apache.spark.sql.hive.HiveContext(sc)
//		    import hiveContext.implicits._
//		    hiveContext.sql("use DataBaseName")
//		    val data = sc.textFile("path").map(x=>x.split("\\s+")).map(x=>Person(x(0),x(1).toInt,x(2)))
//		    data.toDF()insertInto("tableName")
//		def main(args:Array[String]):Unit={
//			    val sc = new org.apache.spark.SparkContext   
//			    val hiveContext = new org.apache.spark.sql.hive.HiveContext(sc)
//			    import hiveContext.implicits._
//			    hiveContext.sql("use DataBaseName")
//			    val data = sc.textFile("path").map(x=>x.split("\\s+")).map(x=>Person(x(0),x(1).toInt,x(2)))
//			    data.toDF().registerTempTable("table1")
//			    hiveContext.sql("insert into table2 partition(date='2015-04-02') select name,col1,col2 from table1")
//			}
		
	}
}

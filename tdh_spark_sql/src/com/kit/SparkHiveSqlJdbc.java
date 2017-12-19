package com.kit;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
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
//	private static final SparkSQLParser
	private static Logger logger = Logger.getLogger(SparkHiveSqlJdbc.class);
	public static void main(String[] args) {
		/**
		 * 链接其它数据库
		 */
		Map<String, String> options = new HashMap<String, String>();
		
		
		
		options.put("drive", "com.oracle.jdbc.Driver");
		options.put("url", "jdbc:oracle:thin:@172.16.19.61:1521:orcl");
		options.put("dbtable", "test_spark");

		options.put("user", "KIT_DEV");
		options.put("password", "KIT_DEV");

		
		// Load oracle query result as DataFrame
		DataFrame jdbcDF = sqlContext.load("jdbc", options);
		jdbcDF.show();
		
	}
}

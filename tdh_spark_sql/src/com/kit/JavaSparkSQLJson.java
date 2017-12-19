package com.kit;
/*
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


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * 
 * @author guohan
 * 
 *         读取集合数据后 然后进行select数据查询
 *
 */
public class JavaSparkSQLJson {

	public static void main(String[] args) throws Exception {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL")
				.setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		SQLContext sqlContext = new SQLContext(ctx);

		// Alternatively, a DataFrame can be created for a JSON dataset
		// represented by
		// a RDD[String] storing one JSON object per string.
		List<String> jsonData = Arrays.asList(
//				"{\"SCORE_ID\":0,\"KPI_CYCLE\":\"4\",\"EVALUATE_ID\":\"MM_20_04_03\",\"DEPT_CODE\":\"0302\","
//						 +
//						 "\"P_ORGANIZATION_CODE\":null,\"YM\":\"200912\",\"NUMERATOR\":\"0\",\"DENOMINATOR\":\"0\",\"SCORE\":\"0\",\"SUM_SCORE\":null,"
//						 + "\"STATUS\":null,\"ETL_DT\":\"20120814151900\"}");
		
		"{\"SCORE_ID\":0,\"KPI_CYCLE\":\"4\",\"EVALUATE_ID\":\"MM_20_04_03\",\"DEPT_CODE\":\"0302\",\"P_ORGANIZATION_CODE\":null,\"YM\":\"200912\",\"NUMERATOR\":\"0\",\"DENOMINATOR\":\"0\",\"SCORE\":\"0\",\"SUM_SCORE\":null,\"STATUS\":null,\"ETL_DT\":\"20120814151900\"}");
		
//		List<String> jsonData = Arrays.asList(
////				"{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}"
////				"{\"city\":\"Columbus\",\"state\":\"Ohio\"}"
//				"{\"city\":\"cc\"}"
////				"{"SCORE_ID":0,"KPI_CYCLE":"4","EVALUATE_ID":"MM_20_04_03","DEPT_CODE":"0302","P_ORGANIZATION_CODE":null,"YM":"200912","NUMERATOR":"0","DENOMINATOR":"0","SCORE":"0","SUM_SCORE":null,"STATUS":null,"ETL_DT":"20120814151900"}"
//				);
		JavaRDD<String> anotherPeopleRDD = ctx.parallelize(jsonData);
		DataFrame peopleFromJsonRDD = sqlContext.read()
				.json(anotherPeopleRDD.rdd());

		// Take a look at the schema of this new DataFrame.
		peopleFromJsonRDD.printSchema();
		// The schema of anotherPeople is ...
		// root
		// |-- address: StructType
		// | |-- city: StringType
		// | |-- state: StringType
		// |-- name: StringType

		peopleFromJsonRDD.registerTempTable("people2");

		DataFrame peopleWithCity = sqlContext
//				.sql("SELECT SCORE_ID, EVALUATE_ID FROM people2");
				.sql("SELECT t.KPI_CYCLE, t.YM FROM people2 t");
		
		System.out.println("the data count is:"+peopleWithCity.count());
		List<String> nameAndCity = peopleWithCity.toJavaRDD()
				.map(new Function<Row, String>() {

					public String call(Row row) {
						return "Name: " + row.getString(0) + ", City: "
								+ row.getString(1);
					}
				}).collect();
		for (String name : nameAndCity) {
			System.out.println(name);
		}

		ctx.stop();
	}
}

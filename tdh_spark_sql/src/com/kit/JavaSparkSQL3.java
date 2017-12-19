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

package com.kit;

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
 * 解析json数据
 *
 */

public class JavaSparkSQL3 {
  public static class Person implements Serializable {
    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }

  public static void main(String[] args) throws Exception {
    SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL").setMaster("local");
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);
    SQLContext sqlContext = new SQLContext(ctx);



    System.out.println("=== Data source: JSON Dataset ===");
    // A JSON dataset is pointed by path.
    // The path can be either a single text file or a directory storing text files.
    String path = "D:/people.json";
    // Create a DataFrame from the file(s) pointed by path
    DataFrame peopleFromJsonFile = sqlContext.read().json(path);
//    sqlContext.jsonFile(path);
//    JavaRDD<String> jsonRDD=sqlContext.read().
//    		DataFrame df= sqlContext.read().json(jsonRDD);

    // Because the schema of a JSON dataset is automatically inferred, to write queries,
    // it is better to take a look at what is the schema.
//    peopleFromJsonFile.printSchema();
    // The schema of people is ...
    // root
    //  |-- age: IntegerType
    //  |-- name: StringType

    // Register this DataFrame as a table.
    peopleFromJsonFile.registerTempTable("people");

    // SQL statements can be run by using the sql methods provided by sqlContext.
    DataFrame teenagers3 = sqlContext.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");
    
    
//    teenagers3.show();

    // The results of SQL queries are DataFrame and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by ordinal.
    List<String>   teenagerNames  = teenagers3.toJavaRDD().map(new Function<Row, String>() {
     
      public String call(Row row) { return "Name: " + row.getString(0); }
    }).collect();
    for (String name: teenagerNames) {
      System.out.println(name);
    }

  

    ctx.stop();
  }
}

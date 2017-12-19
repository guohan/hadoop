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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import scala.Tuple2;

import com.google.common.collect.Lists;

import kafka.auth.AuthenticationManager;
import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 * Usage: JavaDirectKafkaWordCount <brokers> <topics>
 *   <brokers> is a list of one or more Kafka brokers
 *   <topics> is a list of one or more kafka topics to consume from
 *
 * Example:
 *    $ bin/run-example streaming.JavaDirectKafkaWordCount broker1-host:port,broker2-host:port topic1,topic2
 */

public final class MsgFromkfkToHbase {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {

    String brokers ="172.16.19.151:9092,172.16.19.152:9092,172.16.19.153:9092";//zk集群配置
   final   String topics = "system,Disconnector,Breaker";//需要处理的对应主题
//   //kafka认证鉴权
//    AuthenticationManager.setAuthMethod("kerberos");
//    //"/usr/lib/kafka/kafka.keytab"
//	 AuthenticationManager.login("kafka@TDH", TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath()  );
//  

    // Create context with a 2 seconds batch interval
	 //spark上下文配置
    SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount");//.setMaster("local[1]");
    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));

    HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
    HashMap<String, String> kafkaParams = new HashMap<String, String>();
    kafkaParams.put("metadata.broker.list", brokers);

    // Create direct kafka stream with brokers and topics
    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
        jssc,
        String.class,
        String.class,
        StringDecoder.class,
        StringDecoder.class,
        kafkaParams,
        topicsSet
    );

    // Get the lines, split them into words, count the words and print
    JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
     
      public String call(Tuple2<String, String> tuple2) {
        return tuple2._2();
      }
    });
    JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
     
    	//解析kafka消息后 入库处理 根据不同的topic 对应不同的hbase处理模块
      @SuppressWarnings("unchecked")
	public Iterable<String> call(String x) {
    	  String[] kafka=SPACE.split(x);
    	  pareDataToHabse(kafka);
//    	  HbaseTest.insertData("test-kit",topics, Bytes.toBytes(num).toString()); 

   /* 	  system 表入库
  		Map map = new HashMap();
  		Map infomap = new HashMap();
  		infomap.put("Area", kafka[0]);
  		infomap.put("System", kafka[1]);
  		infomap.put("Time", kafka[2]);
  		infomap.put("Readtime", kafka[3]);
  		map.put("info", infomap);
  		HbaseUtil.add("system", topics+UUID.randomUUID(), map);*/
    	  
//    	  Disconnector 表入库
    	  
//    	  Map map = new HashMap();
//    		Map infomap = new HashMap();
//    		infomap.put("factoryname", kafka[0]);
//    		infomap.put("dzname", kafka[1]);
//    		infomap.put("dzid", kafka[2]);
//    		infomap.put("dzstatus", kafka[3]);
//    		infomap.put("dissecnode", kafka[4]);
//    		map.put("info", infomap);
//    		HbaseUtil.add("Disconnector", topics+UUID.randomUUID(), map);
    	  
    		
    		
//    		 Map map = new HashMap();
//     		Map infomap = new HashMap();
//     		infomap.put("factoryname", kafka[0]);
//     		infomap.put("Ocname", kafka[1]);
//     		infomap.put("ocid", kafka[2]);
//     		infomap.put("ocstatus", kafka[3]);
//     		infomap.put("oscsecnode", kafka[4]);
//     		
//     		infomap.put("Elea", kafka[5]);
//     		infomap.put("Eleb", kafka[6]);
//     		infomap.put("Elec", kafka[7]);
//     		infomap.put("pname", kafka[8]);
//     		map.put("info", infomap);
//     		HbaseUtil.add("Breaker", topics+UUID.randomUUID(), map);
    	  
    	  //这里传进来的是每条kafka的消息
        return Lists.newArrayList(SPACE.split(x));
      }
    });
    JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
      new PairFunction<String, String, Integer>() {
       
        public Tuple2<String, Integer> call(String s) {
        	
          return new Tuple2<String, Integer>(s, 1);
        }
      }).reduceByKey(
        new Function2<Integer, Integer, Integer>() {
       
        public Integer call(Integer i1, Integer i2) {
          return i1 + i2;
        }
      });
    
    
    /**
     * 统计打印kafka每次消费的信息 
     */
    wordCounts.print();

    // Start the computation
    jssc.start();
    jssc.awaitTermination();
  }
  
  
  
  private static void pareDataToHabse(String [] kafka){
	  
	 /**
	  * 数据入库 获取到的是kafka数据   如何灵活的设计数据库 需要仔细构思
	  * 
	  * for (int i = 0; i < kafka.length; i++) {
					tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
				}
	  */
	  Map map = new HashMap();
		Map infomap = new HashMap();
	  if("Breaker".equals(kafka[0].trim())){
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
	  }else if("system".equals(kafka[0].trim())){
		  infomap.put("Area", kafka[1]);
			infomap.put("System", kafka[2]);
			infomap.put("Time", kafka[3]);
			infomap.put("Readtime", kafka[4]);
			map.put("info", infomap);
	  }else if("Disconnector".equals(kafka[0].trim())){
		  infomap.put("factoryname", kafka[1]);
			infomap.put("dzname", kafka[2]);
			infomap.put("dzid", kafka[3]);
			infomap.put("dzstatus", kafka[4]);
			infomap.put("dissecnode", kafka[5]);
			map.put("info", infomap);
	  }
//		HbaseUtil.add( kafka[0],  kafka[0]+UUID.randomUUID(), map);
  }
}

/**
 *  Spark Streaming Kafka Log Analyzer.
 */
package com.kit.spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

import com.databricks.apps.logs.ApacheAccessLog;
import com.databricks.apps.logs.Functions;

public class SparkStreamingKafkaLogAnalyzer {
	
	private static final Log LOGGER = LogFactory.getLog(SparkStreamingKafkaLogAnalyzer.class);

	// Stats will be computed for the last window length of time.
	private static final Duration WINDOW_LENGTH = new Duration(30 * 1000);

	// Stats will be computed every slide interval time.
	private static final Duration SLIDE_INTERVAL = new Duration(10 * 1000);

	public static void main(String[] args) {
		// Set application name
		String appName = "Spark Streaming Kafka Sample";

		// Create a Spark Context.
		SparkConf conf = new SparkConf()
			.setAppName(appName)
			.setMaster("local[*]")
			.set("spark.executor.memory", "1g");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// This sets the update window to be every 10 seconds.
		JavaStreamingContext jssc = new JavaStreamingContext(sc, SLIDE_INTERVAL); 

		String zkQuorum = "localhost:2181";
		String group = "spark-streaming-sample-groupid";
		String strTopics = "spark-streaming-sample-topic,test-topic1,test-topic2";
		int numThreads = 2;

		Map<String, Integer> topicMap = new HashMap<String, Integer>();
        String[] topics = strTopics.split(",");
        for (String topic: topics) {
            topicMap.put(topic, numThreads);
        }

        JavaPairReceiverInputDStream<String, String> logDataDStream =
                KafkaUtils.createStream(jssc, zkQuorum, group, topicMap);

        LOGGER.debug("Received DStream connecting to zookeeper " + zkQuorum + " group " + group + " topics" +
        		topicMap);
        LOGGER.debug("logDataDStream: "+ logDataDStream);

        JavaDStream<ApacheAccessLog> accessLogDStream = logDataDStream.map(
                new Function<Tuple2<String, String>, ApacheAccessLog>() {
                    public ApacheAccessLog call(Tuple2<String, String> message) {
                        String strLogMsg = message._2();
                        return ApacheAccessLog.parseFromLogLine(strLogMsg);
                    }
                }
            );  
        accessLogDStream.print();

        JavaDStream<ApacheAccessLog> windowDStream = accessLogDStream.window(
				WINDOW_LENGTH, SLIDE_INTERVAL);

        windowDStream.foreachRDD(new Function<JavaRDD<ApacheAccessLog>, Void>() {
				@Override
				public Void call(JavaRDD<ApacheAccessLog> accessLogs) {
					if (accessLogs.count() == 0) {
						LOGGER.debug("No access logs in this time interval");
						return null;
					}

					// Calculate statistics based on the content size.
					JavaRDD<Long> contentSizes = accessLogs.map(Functions.GET_CONTENT_SIZE).cache();
					Long min = contentSizes.min(Functions.LONG_NATURAL_ORDER_COMPARATOR);
					Long max = contentSizes.max(Functions.LONG_NATURAL_ORDER_COMPARATOR);
					Long avg = contentSizes.reduce(Functions.SUM_REDUCER) / contentSizes.count();
					
					LOGGER.debug("Web request content size statistics: Min=" + min + ", Max=" + max + ", Avg=" + avg);
					
					// Compute Response Code to Count.
					List<Tuple2<Integer, Long>> responseCodeToCount = accessLogs
						.mapToPair(Functions.GET_RESPONSE_CODE)
						.reduceByKey(Functions.SUM_REDUCER).take(100);
					LOGGER.debug("Response code counts: " + responseCodeToCount);

					// Any IPAddress that has accessed the server more than
					// 10 times.
					List<String> ipAddresses = accessLogs
						.mapToPair(Functions.GET_IP_ADDRESS)
						.reduceByKey(Functions.SUM_REDUCER)
						.filter(Functions.FILTER_GREATER_10)
						.map(Functions.GET_TUPLE_FIRST).take(100);
					LOGGER.debug("IPAddresses > 10 times: " + ipAddresses);

					// Top Endpoints.
					List<Tuple2<String, Long>> topEndpoints = accessLogs
						.mapToPair(Functions.GET_ENDPOINT)
						.reduceByKey(Functions.SUM_REDUCER)
						.top(10,
							new Functions.ValueComparator<String, Long>(
								Functions.LONG_NATURAL_ORDER_COMPARATOR));
					LOGGER.debug("Top Endpoints: " + topEndpoints);

					return null;
			}
		});

		// Start the streaming server.
		jssc.start(); // Start the computation
		jssc.awaitTermination(); // Wait for the computation to terminate
	}
}


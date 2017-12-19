/**
 *  Spark Streaming Kafka Log Generator.
 */
package com.kit.spark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author
 * 
 */
public class SparkStreamingKafkaLogGenerator {
	
	private static final Log LOGGER = LogFactory.getLog(SparkStreamingKafkaLogGenerator.class);

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Invalid arguments passed. Usage: SparkStreamingKafkaLogGenerator spark-streaming-sample-groupid spark-streaming-sample-topic 50 1000");
			System.exit(-1);
		}
		//
		// Get log generator run time arguments. 
		//
        String group = args[0];
		String topic = args[1];
		int iterations = new Integer(args[2]).intValue();
		long millisToSleep = new Long(args[3]).longValue();
		SparkStreamingKafkaLogGenerator logGenerator = new SparkStreamingKafkaLogGenerator();
		logGenerator.generateLogMessages(group, topic, iterations, millisToSleep);
	}

	private void generateLogMessages(String group, String topic, int iterations, long millisToSleep) {

		Properties props = new Properties();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);

		Producer producer = new Producer(config);

        // Get current system time
        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		Date currDate = new Date();
		String strDate = df.format(currDate);
		LOGGER.debug("strDate: " + strDate);

        String ipAddr = "127.0.0.1";
        String clientId = "test-client";
        String userId = "test-user";

        String msgPrefix = ipAddr + " " + clientId + " " + userId + " " + "[" + strDate + "]";

		String msg1 = msgPrefix + " \"GET /src/main/java/com/sparkstreaming/kafka/example/SparkStreamingKafkaLogGenerator.java HTTP/1.1\" 200 1234";
		String msg2 = msgPrefix + " \"GET /src/main/java/com/sparkstreaming/kafka/example/SparkStreamingKafkaLogAnalyzer.java HTTP/1.1\" 200 2000";
		String msg3 = msgPrefix + " \"GET /src/main/java/com/sparkstreaming/kafka/example/Error.java HTTP/1.1\" 404 2500";
		String msg4 = msgPrefix + " \"GET /src/main/java/com/sparkstreaming/kafka/example/DatabaseError.java HTTP/1.1\" 401 100";

		Random r = new Random();
		int low = 1;
		int high = 8;

		for (int i=1; i<=iterations; i++) {
			// Add delay per the run-time argument millisToSleep
			try {
				Thread.sleep(millisToSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.debug("**** ITERATION#: " + i);
			
			// Generate a random number.
			int rndNum = r.nextInt(high-low) + low;
			
			// Decide which message to post based on the random number generated
			// to simulate continuous flow of log messages.
			if (rndNum == 1 || rndNum == 8) {
				LOGGER.debug("Posting message msg1: " + msg1);
				KeyedMessage data = new KeyedMessage(topic, String.valueOf(i), msg1);
				producer.send(data);
			} else if (rndNum == 2 || rndNum == 7) {
				LOGGER.debug("Posting message msg2: " + msg2);
				KeyedMessage data = new KeyedMessage(topic, String.valueOf(i), msg2);
				producer.send(data);
			} else if (rndNum == 3 || rndNum == 6) {
				LOGGER.debug("Posting message msg3: " + msg3);
				KeyedMessage data = new KeyedMessage(topic, String.valueOf(i), msg3);
				producer.send(data);
			} else if (rndNum == 4 || rndNum == 5) {
				LOGGER.debug("Posting message msg4: " + msg4);
				KeyedMessage data = new KeyedMessage(topic, String.valueOf(i), msg4);
				producer.send(data);
			}
		}
		producer.close();
	}
}



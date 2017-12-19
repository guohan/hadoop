package com.kit;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.auth.AuthenticationManager;
import kafka.cluster.Broker;
import kafka.common.TopicAndPartition;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import kafka.message.MessageAndOffset;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

/**
 * 
 * @author gh 消费api调用
 * @createTime 20160722
 * @description
 * 此模块对kafka信息消费进行了多种测试 供参考
 * 包含了
 * 
 * 常规消费（kafka信息生产后进行消费信息的打印)
 *  分组
 *  offset指定分区和偏移量消费
 *  分区
 */
public class KafkaConsumer {

	private final ConsumerConnector consumer;
	private static Logger logger = Logger.getLogger(KafkaConsumer.class);
	private String topic=null;
	// 构造函数
	public KafkaConsumer() {
//    	加载资源配置文件
		Properties p = new Properties();
		  InputStream is = KafkaConsumer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
//		    Properties props = new Properties();
			try
			{
				p.load(is);
				System.out.println(	 KafkaConsumer.class.getClassLoader().getResource("kafka.keytab").getPath()
	);
//				 System.out.println(  p.getProperty("hbase_key_path").trim()  );
//				 AuthenticationManager.setAuthMethod("kerberos");
//				    System.out.println(  p.getProperty("kafka_key_path").trim()  );
//					 AuthenticationManager.login("kafka/kit-b1@TDH", 
////								 TestProducer.class.getClassLoader().getResource("kafka.keytab").getPath()
//							  p.getProperty("kafka_key_path").trim()  
//							 );
			   
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   
			 //获取配置文件中的主题名称
//		topic=props.getProperty("kfk.topic").trim();
//			 TMR_BM txt文件
		topic="oggtopic";//dt 文件
		ConsumerConfig config = new ConsumerConfig(p);
		//创建consumer链接器
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
	}

	// 常规 消费 只是加入鉴权
	void consume() {

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				keyDecoder, valueDecoder);
		KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext())
//			logger.debug(it.next().message());
		System.out.println(it.next().message());
		
		logger.debug("msg"+it.next().message());
		
		 try {
			JSONObject obj = new JSONObject(new JSONTokener(it.next().message()));
			System.out.println("table"+obj.get("table"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
//		consumer.shutdown();
	}

	// 分组消费模式  解析文本文件txt 入库hbase
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void group() {
//    	加载资源配置
		Properties p = new Properties();
		  InputStream is = KafkaConsumer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
		    Properties props = new Properties();
		    try {
				props.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //添加鉴权
//		    AuthenticationManager.setAuthMethod( props.getProperty("authmethod").trim());
//		      AuthenticationManager.login( props.getProperty("authpwd").trim(), KafkaConsumer.class.getClassLoader().getResource("kafka.keytab").getPath());
		ConsumerConfig config = new ConsumerConfig(props);
		//创建consumerconnnector连接器
		ConsumerConnector connector = Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		// 设置每个topic开几个线程
		topic="oggtopic";
		topicCountMap.put(topic, 1);
		// 获取stream
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topicCountMap);
		// 为每个stream启动一个线程消费消息
		for (KafkaStream<byte[], byte[]> stream : streams.get(topic)) {
			ConsumerIterator<byte[], byte[]> streamIterator = stream.iterator();
			// 逐条处理消息
			while (streamIterator.hasNext()) {
				MessageAndMetadata<byte[], byte[]> message = streamIterator.next();
				String topic = message.topic();
				int partition = message.partition();
				long offset = message.offset();
				try {
//					String key = new String(message.key());
					String msg = new String(message.message());
					// 在这里处理消息,这里仅简单的输出
					logger.debug(", thread : " + Thread.currentThread().getName()
							+ ", topic : " + topic + ", partition : " + partition + ", offset : " + offset +
							 " , mess : " + msg);
						/**
						 * 解析消息
//						 */
//						 Map map = new HashMap();
//							Map infomap = new HashMap();
//								String[] eles=msg.split("\t");
//								System.out.println("size"+eles.length);
//								for(int j=1;j<eles.length;j++){
//									infomap.put("element"+j, eles[j]);
//								}
//								map.put("info", infomap);
							//消息入库
//						HbaseUtil.add(topic,  topic+UUID.randomUUID(), map);
				} catch (Exception e) {
					logger.error(e.getStackTrace());
				}
			}
		}

	}


	// 指定分区和偏移量消费
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void offset() throws IOException {
		int partition = 0;
		long startOffet = 325;
		int fetchSize = 500000;
		long nextoffset=0;
		// 找到leader
		Broker leaderBroker = findLeader("172.16.19.151:9092,172.16.19.152:9092,172.16.19.154:9092",
				topic, partition);
		SimpleConsumer simpleConsumer = new SimpleConsumer(leaderBroker.host(), leaderBroker.port(), 20000, 10000,
				"mySimpleConsumer");
		while (true) {
			long offset = startOffet;
			// 添加fetch指定目标topic，分区，起始offset及fetchSize(字节)，可以添加多个fetch
			FetchRequest req = new FetchRequestBuilder()
					.addFetch(topic, partition, startOffet, fetchSize).build();
			// 拉取消息
			FetchResponse fetchResponse = simpleConsumer.fetch(req);

			ByteBufferMessageSet messageSet = fetchResponse.messageSet(topic, partition);
			for (MessageAndOffset messageAndOffset : messageSet) {
				Message mess = messageAndOffset.message();
				ByteBuffer payload = mess.payload();
				byte[] bytes = new byte[payload.limit()];
				payload.get(bytes);
				String msg;
				try {
					msg = new String(bytes, "UTF-8");
					offset = messageAndOffset.offset();
//					nextoffset = messageAndOffset.nextOffset();
					logger.debug("partition : " + partition + ", offset : " + offset + "  mess : " + msg);
					System.out.println("partition : " + partition + ", offset : " + offset + "  mess : " + msg);
					
//					 try {
//						JSONObject obj = new JSONObject(new JSONTokener(msg));
//						System.out.println("table"+obj.get("table"));
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}  

					/**
					 * 解析消息
					 */
//					String [] kafka=msg.split(" ");
//					 Map map = new HashMap();
//						Map infomap = new HashMap();
//					 infomap.put("Area", kafka[0]);
//						infomap.put("System", kafka[1]);
//						infomap.put("Time", kafka[2]);
//						infomap.put("Readtime", kafka[3]);
//						map.put("info", infomap);
						//消息入库
//					HbaseUtil.add(topic,  topic+UUID.randomUUID(), map);
					//					 HbaseTest.insertData("test-kit",topic,msg); 
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
//			kafka.api.OffsetRequest.LatestTime();
			//获取目前偏移量
			 @SuppressWarnings("unused")
			long readOffset = getLastOffset(simpleConsumer, topic, partition,   "",
	                    kafka.api.OffsetRequest.LatestTime());  
//			if(readOffset-1<offset ){
//				// 继续消费下一批
//			 System.out.println("size:"+messageSet.sizeInBytes());
			 //如果消费集合里面根据偏移量获取的集合大小小于零 则偏移量不做改变
			 if(messageSet.sizeInBytes()>0){
				 startOffet = offset + 1;
			 }
			
		}
	}
	
	
	
	
	
	
	// 指定分区和偏移量消费
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void consumerTMR_BM() throws IOException {
			topic="TMR_BM";
			int partition = 1;
			long startOffet = 8026;
			int fetchSize = 100000;
			long nextoffset=0;
			// 找到leader
			Broker leaderBroker = findLeader("172.16.19.151:9092,172.16.19.152:9092,172.16.19.154:9092",
					topic, partition);
			SimpleConsumer simpleConsumer = new SimpleConsumer(leaderBroker.host(), leaderBroker.port(), 20000, 10000,
					"mySimpleConsumer");
			while (true) {
				long offset = startOffet;
				// 添加fetch指定目标topic，分区，起始offset及fetchSize(字节)，可以添加多个fetch
				FetchRequest req = new FetchRequestBuilder()
						.addFetch(topic, partition, startOffet, fetchSize).build();
				// 拉取消息
				FetchResponse fetchResponse = simpleConsumer.fetch(req);

				ByteBufferMessageSet messageSet = fetchResponse.messageSet(topic, partition);
				for (MessageAndOffset messageAndOffset : messageSet) {
					Message mess = messageAndOffset.message();
					ByteBuffer payload = mess.payload();
					byte[] bytes = new byte[payload.limit()];
					payload.get(bytes);
					String msg;
					try {
						msg = new String(bytes, "UTF-8");
						offset = messageAndOffset.offset();
//						nextoffset = messageAndOffset.nextOffset();
						logger.debug("partition : " + partition + ", offset : " + offset + "  mess : " + msg);
						/**
						 * 解析消息
//						 */
						 Map map = new HashMap();
							Map infomap = new HashMap();
								String[] eles=msg.split("\t");
								System.out.println("size"+eles.length);
								for(int j=1;j<eles.length;j++){
									infomap.put("element"+j, eles[j]);
								}
								map.put("info", infomap);
							//消息入库
//						HbaseUtil.add(topic,  topic+UUID.randomUUID(), map);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
//				kafka.api.OffsetRequest.LatestTime();
				//获取目前偏移量
				 @SuppressWarnings("unused")
				long readOffset = getLastOffset(simpleConsumer, topic, partition,   "",
		                    kafka.api.OffsetRequest.LatestTime());  
//				if(readOffset-1<offset ){
//					// 继续消费下一批
//				 System.out.println("size:"+messageSet.sizeInBytes());
				 //如果消费集合里面根据偏移量获取的集合大小小于零 则偏移量不做改变
				 if(messageSet.sizeInBytes()>0){
					 startOffet = offset + 1;
				 }
				
			}
		}

	// 获取消费信息示例  根据时间节点获取offset
	void doSomething() {

		String topic = "topic";
		int partition = 1;
		String brokers = "172.16.19.151:9092,172.16.19.152:9092,172.16.19.154:9092";
		int maxReads = 100; // 读多少条数据
		// 1.找leader
		PartitionMetadata metadata = null;
		for (String ipPort : brokers.split(",")) {
			// 我们无需要把所有的brokers列表加进去，目的只是为了获得metedata信息，故只要有broker可连接即可
			SimpleConsumer consumer = null;
			try {
				String[] ipPortArray = ipPort.split(":");
				consumer = new SimpleConsumer(ipPortArray[0], Integer.parseInt(ipPortArray[1]), 100000, 64 * 1024,
						"leaderLookup");
				List<String> topics = new ArrayList<String>();
				topics.add(topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				// 取meta信息
				TopicMetadataResponse resp = consumer.send(req);

				// 获取topic的所有metedate信息(目测只有一个metedata信息，)
				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						// 获取每个meta信息的分区信息,这里我们只取我们关心的partition的metedata
						System.out.println("----" + part.partitionId());
						if (part.partitionId() == partition) {
							metadata = part;
							break;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Error communicating with Broker [" + ipPort + "] to find Leader for [" + topic
						+ ", " + partition + "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}
		if (metadata == null || metadata.leader() == null) {
			System.out.println("meta data or leader not found, exit.");
			return;
		}
		// 拿到leader
		Broker leadBroker = metadata.leader();
		// 获取所有副本
		System.out.println(metadata.replicas());

		// 2.获取lastOffset(这里提供了两种方式：从头取或从最后拿到的开始取，下面这个是从头取)
		long whichTime = kafka.api.OffsetRequest.EarliestTime();
		// 这个是从最后拿到的开始取
		// long whichTime = kafka.api.OffsetRequest.LatestTime();
		System.out.println("lastTime:" + whichTime);
		String clientName = "Client_" + topic + "_" + partition;
		SimpleConsumer consumer = new SimpleConsumer(leadBroker.host(), leadBroker.port(), 100000, 64 * 1024,
				clientName);
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
		OffsetRequest request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
		// 获取指定时间前有效的offset列表
		OffsetResponse response = consumer.getOffsetsBefore(request);
		if (response.hasError()) {
			logger.debug("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
			return;
		}
		// 千万不要认为offset一定是从0开始的
		long[] offsets = response.offsets(topic, partition);
		System.out.println("offset list:" + Arrays.toString(offsets));
		long offset = offsets[0];

		// 读数据
		while (maxReads > 0) {
			// 注意不要调用里面的replicaId()方法，这是内部使用的。
			FetchRequest req = new FetchRequestBuilder().clientId(clientName).addFetch(topic, partition, offset, 100000)
					.build();
			FetchResponse fetchResponse = consumer.fetch(req);
			if (fetchResponse.hasError()) {
				// 出错处理。这里只直接返回了。实际上可以根据出错的类型进行判断，如code ==
				// ErrorMapping.OffsetOutOfRangeCode()表示拿到的offset错误
				// 一般出错处理可以再次拿offset,或重新找leader，重新建立consumer。可以将上面的操作都封装成方法。再在该循环来进行消费
				// 当然，在取所有leader的同时可以用metadata.replicas()更新最新的节点信息。另外zookeeper可能不会立即检测到有节点挂掉，故如果发现老的leader和新的leader一样，可能是leader根本没挂，也可能是zookeeper还没检测到，总之需要等等。
				short code = fetchResponse.errorCode(topic, partition);
				logger.debug("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
				return;
			}
			// 取一批消息
			boolean empty = true;
			for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partition)) {
				empty = false;
				long curOffset = messageAndOffset.offset();
				// 下面这个检测有必要，因为当消息是压缩的时候，通过fetch获取到的是一个整块数据。块中解压后不一定第一个消息就是offset所指定的。就是说存在再次取到已读过的消息。
				if (curOffset < offset) {
					logger.debug("Found an old offset: " + curOffset + " Expecting: " + offset);
					continue;
				}
				// 可以通过当前消息知道下一条消息的offset是多少
				offset = messageAndOffset.nextOffset();
				ByteBuffer payload = messageAndOffset.message().payload();
				byte[] bytes = new byte[payload.limit()];
				payload.get(bytes);
				try {
					logger.debug(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				maxReads++;
			}
			// 进入循环中，等待一会后获取下一批数据
			if (empty) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 退出（这里象征性的写一下）
		if (consumer != null)
			consumer.close();
	}

	/**
	 * 找到指定分区的leader broker
	 * 
	 * @param brokerHosts
	 *            broker地址，格式为：“host1:port1,host2:port2,host3:port3”
	 * @param topic
	 *            topic
	 * @param partition
	 *            分区
	 * @return
	 */
	public Broker findLeader(String brokerHosts, String topic, int partition) {
		Broker leader = findPartitionMetadata(brokerHosts, topic, partition).leader();
		logger.debug(String.format("Leader tor topic %s, partition %d is %s:%d", topic, partition, leader.host(),
				leader.port()));
		// System.out.println(String.format("Leader tor topic %s, partition %d
		// is %s:%d", topic, partition, leader.host(),
		// leader.port()));
		return leader;
	}

	/**
	 * 找到指定分区的元数据
	 * 
	 * @param brokerHosts
	 *            broker地址，格式为：“host1:port1,host2:port2,host3:port3”
	 * @param topic
	 *            topic
	 * @param partition
	 *            分区
	 * @return 元数据
	 */
	private PartitionMetadata findPartitionMetadata(String brokerHosts, String topic, int partition) {
		PartitionMetadata returnMetaData = null;
		for (String brokerHost : brokerHosts.split(",")) {
			SimpleConsumer consumer = null;
			String[] splits = brokerHost.split(":");
			consumer = new SimpleConsumer(splits[0], Integer.valueOf(splits[1]), 100000, 64 * 1024, "leaderLookup");
			List<String> topics = Collections.singletonList(topic);
			TopicMetadataRequest request = new TopicMetadataRequest(topics);
			TopicMetadataResponse response = consumer.send(request);
			List<TopicMetadata> topicMetadatas = response.topicsMetadata();
			for (TopicMetadata topicMetadata : topicMetadatas) {
				for (PartitionMetadata PartitionMetadata : topicMetadata.partitionsMetadata()) {
					if (PartitionMetadata.partitionId() == partition) {
						returnMetaData = PartitionMetadata;
					}
				}
			}
			if (consumer != null)
				consumer.close();
		}
		return returnMetaData;
	}

	/**
	 * 根据时间戳找到某个客户端消费的offset
	 * 
	 * @param consumer
	 *            SimpleConsumer
	 * @param topic
	 *            topic
	 * @param partition
	 *            分区
	 * @param clientID
	 *            客户端的ID
	 * @param whichTime
	 *            时间戳
	 * @return offset
	 */
	public long getLastOffset(SimpleConsumer consumer, String topic, int partition, String clientID, long whichTime) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
		OffsetRequest request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientID);
		OffsetResponse response = consumer.getOffsetsBefore(request);
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}

	//调用各种模式进行消费
	public static void main(String[] args) {
//		 new KafkaConsumer().consume();
//		 new KafkaConsumer().group();
		 try
		{
			 //dtfile
			new KafkaConsumer().offset();
			 //txt 
//			 new KafkaConsumer().consumerTMR_BM();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
//		new KafkaConsumer().doSomething();
	}
}
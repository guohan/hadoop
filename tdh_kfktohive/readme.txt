说明：此文本文件，是对本工程，各个组件类的说明
HbaseUtil  hbase组件 工具类 包含了基本的curd操作
JavaSparkPiTest 介绍spark的基本流程实现
KafkaConsumer kafka的消费实现，run方法里面实现了kakfa获取消费信息以后，入库habase的操作
MsgFromkfkToHbase 使用sparkstreaming消费kafka消息后 入库hbase
TestProducer    生产kafka消息 测试类， 对整个kafka消息的生产进行测试 对整个步骤流程做相关了解
Producer   在生产消息的基础上，做进一步的深入，读取规则文件 不规则的xml文本 ，进行消息的生产
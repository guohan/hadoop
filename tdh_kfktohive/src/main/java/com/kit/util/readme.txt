Spark directStream保存/读取kafka offset 
一、情景：当Spark streaming程序意外退出时，数据仍然再往Kafka中推送，
然而由于Kafka默认是从latest的offset读取，这会导致数据丢失。为了避免数据丢失，
那么我们需要记录每次消费的offset，以便下次检查并且从指定的offset开始读取
http://blog.csdn.net/rongyongfeikai2/article/details/49784785
http://blog.csdn.net/high2011/article/details/53706446
http://blog.itpub.net/29754888/viewspace-2125804/
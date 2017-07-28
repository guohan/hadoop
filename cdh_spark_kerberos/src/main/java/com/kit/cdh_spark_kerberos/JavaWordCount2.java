package com.kit.cdh_spark_kerberos;
import scala.Tuple2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaWordCount2 {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) throws Exception {

//		if (args.length < 1) {
//			System.err.println("Usage: JavaWordCount <file>");
//			System.exit(1);
//		}

		SparkConf sparkConf = new SparkConf().setAppName("wconcluster").setMaster("local");
//		String [] cc= {"D:\\spark3.jar"};
//		sparkConf.setJars(cc);
//		sparkConf.setMaster("spark://192.168.56.101:7077");
//        sparkConf.setMaster("spark://172.16.19.151:7077");
		//设置内存大小
        sparkConf.set("spark.executor.memory", "3000m");
        
        Configuration configuration = new Configuration();
        System.setProperty("java.security.auth.useSubjectCresOnly", "false");// 使用系统底层的认证授权
		System.setProperty("java.security.krb5.kdc", "centos-1");// 集群机器 域名映射
		System.setProperty("java.security.krb5.realm", "CLOUDERA");// realm名称
		configuration.set("hadoop.security.authentication", "Kerberos");
//		UserGroupInformation.setConfiguration(conf);
////		// 设置登录名 与密码
//		System.out.println("get the keytab"+	p.getProperty("hdfs_key_path").trim());
//		UserGroupInformation.loginUserFromKeytab("hdfs/kit-b1@TDH",
//				p.getProperty("hdfs_key_path").trim());kitdev/kitdev#123 /user/kitdev 
		UserGroupInformation.loginUserFromKeytab("hdfs/centos-1@CLOUDERA",
				JavaWordCount2.class.getClassLoader().getResource("hdfs.keytab")
						.getPath());
		System.out.println("read key ,after kerbores..............");
      //创建上下文
//        org.apache.spark.deploy.ClientArguments
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		//创建弹性数据集
		JavaRDD<String> lines =
				ctx
//				.textFile("hdfs://hadoop-VirtualBox:9000/xiaolu/wordcount/wordcountdata.txt", 1);
		.textFile("hdfs://172.16.19.156:8020/wordcount/wc.txt", 1);
		JavaRDD<String> words = lines
				.flatMap(new FlatMapFunction<String, String>() {
					public Iterable<String> call(String s) {
						return Arrays.asList(SPACE.split(s));
					}
				});

		JavaPairRDD<String, Integer> ones = words
				.mapToPair(new PairFunction<String, String, Integer>() {
					public Tuple2<String, Integer> call(String s) {
						return new Tuple2<String, Integer>(s, 1);
					}
				});

		JavaPairRDD<String, Integer> counts = ones
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
					public Integer call(Integer i1, Integer i2) {
						return i1 + i2;
					}
				});
		counts.
//		saveAsTextFile("hdfs://hadoop-VirtualBox:9000/xiaolu/wordcount/txt");
		saveAsTextFile("hdfs://172.16.19.156:8020/wordcount/txtt1");
		System.out.println("the num five is:"+ones.take(5));
		List<Tuple2<String, Integer>> output = counts.collect();
		for (Tuple2<?, ?> tuple : output) {
			System.out.println(tuple._1() + ": " + tuple._2());
			
		}
		
		ctx.stop();
	}
}

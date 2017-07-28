package com.kit.cdh_spark_kerberos;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.deploy.SparkSubmit;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes an approximation to pi Usage: JavaSparkPi [slices]  圆周率
 * spark 基本示列 定义了spark常规的作业模式
 */
public  class JavaSparkPiTest {

	public static void main(String[] args) throws Exception {
//		//申明应用发布的模式与应用名称
		SparkConf sparkConf= new SparkConf().setAppName("JavaSparkPiTest").setMaster("local[*]");  
////		sparkConf.setMaster("yarn-client");
//		sparkConf.set("spark.master", "yarn-client");
//		String [] cc= {"D:\\spark3.jar"};
//		sparkConf.setJars(cc);
//		sparkConf.setMaster("spark://hadoop-VirtualBox:7077");
		//设置内存大小
		sparkConf.set("spark.executor.memory", "1000m");
//		"yarn-client"
//		sparkConf.set
		//创建上下文
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

		int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
		int n = 5 * slices;
		List<Integer> l = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++) {
			l.add(i);
		}

		
		long count = jsc.parallelize(l).filter(new Function<Integer, Boolean>() {
			  public Boolean call(Integer i) {
			    double x = Math.random();
			    double y = Math.random();
			    return x*x + y*y < 1;
			  }
			}).count();
		//创建rdd数据集
		JavaRDD<Integer> dataSet = jsc.parallelize(l, slices);

		System.out.println("Pi is roughly " + 4.0 * count / n);
		jsc.stop();
		
		
	}
}


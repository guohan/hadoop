package com.spark.kfktohbase_cdh;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.List;

/**@author gh
 * @createTime 20160722
 * Computes an approximation to pi Usage: JavaSparkPi [slices]
 * spark 基本示列 定义了spark常规的作业模式 spark 基本案例测试
 */
public  class JavaSparkPiTest {

	public static void main(String[] args) throws Exception {
//		SparkConf sparkConf = new SparkConf().
//				set("spark.master", "spark://172.16.19.151:7077").setAppName("JavaSparkPiTest");
//		//申明应用发布的模式与应用名称
		SparkConf sparkConf= new SparkConf().setAppName("JavaSparkPiTest");//.setMaster("local[1]");  
////		sparkConf.setMaster("yarn-client");
//		sparkConf.set("spark.master", "yarn-client");
		sparkConf.setMaster("spark://192.168.56.101:7077");
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


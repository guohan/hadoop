package com.kit.hadoop.example;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;

import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 
 * @author Administrator
 *         http://www.cnblogs.com/xia520pi/archive/2012/06/04/2534533.html 
 *         实例描述
 * 
 *         对输入文件中数据进行排序。输入文件中的每行内容均为一个数字，即一个数据。要求在输出中每行有两个间隔的数字，其中，
 *         第一个代表原始数据在原始数据集中的位次，第二个代表原始数据。
 *
 *         设计思路
 * 
 *         这个实例仅仅要求对输入数据进行排序，熟悉MapReduce过程的读者会很快想到在MapReduce过程中就有排序，
 *         是否可以利用这个默认的排序，而不需要自己再实现具体的排序呢？答案是肯定的。
 * 
 *         但是在使用之前首先需要了解它的默认排序规则。它是按照key值进行排序的，如果key为封装int的IntWritable类型，
 *         那么MapReduce按照数字大小对key排序，如果key为封装为String的Text类型，
 *         那么MapReduce按照字典顺序对字符串排序。
 * 
 *         了解了这个细节，我们就知道应该使用封装int的IntWritable型数据结构了。
 *         也就是在map中将读入的数据转化成IntWritable型，然后作为key值输出（value任意）。reduce拿到
 *         <key，value-list>之后，将输入的key作为value输出，并根据value-list中元素的个数决定输出的次数。输出的key
 *         （即代码中的linenum）是一个全局变量，它统计当前key的位次。需要注意的是这个程序中没有配置Combiner，
 *         也就是在MapReduce过程中不使用Combiner。这主要是因为使用map和reduce就已经能够完成任务了。
 */
public class Sort {

	// map将输入中的value化成IntWritable类型，作为输出的key

	public static class Map
			extends
				Mapper<Object, Text, IntWritable, IntWritable> {

		private static IntWritable data = new IntWritable();

		// 实现map函数

		public void map(Object key, Text value, Context context)

				throws IOException, InterruptedException {

			String line = value.toString();

			data.set(Integer.parseInt(line));

			context.write(data, new IntWritable(1));

		}

	}

	// reduce将输入中的key复制到输出数据的key上，

	// 然后根据输入的value-list中元素的个数决定key的输出次数

	// 用全局linenum来代表key的位次

	public static class Reduce
			extends

				Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

		private static IntWritable linenum = new IntWritable(1);

		// 实现reduce函数

		public void reduce(IntWritable key, Iterable<IntWritable> values,
				Context context)

				throws IOException, InterruptedException {

			for (IntWritable val : values) {

				context.write(linenum, key);

				linenum = new IntWritable(linenum.get() + 1);

			}

		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		// 这句话很关键

		// conf.set("mapred.job.tracker", "192.168.1.2:9001");

		String[] ioArgs = new String[]{
				"hdfs://172.16.19.158:8020/blogs/datasort",
				"hdfs://172.16.19.158:8020/blogs/datasort/datasort_out"};

		String[] otherArgs = new GenericOptionsParser(conf, ioArgs)
				.getRemainingArgs();

		if (otherArgs.length != 2) {

			System.err.println("Usage: Data Sort <in> <out>");

			System.exit(2);

		}

		Job job = new Job(conf, "Data Sort");
		job.setJar("D:\\hadoop.jar");// 指定jar 进行远程作业提交
		job.setJarByClass(Sort.class);
		job.setNumReduceTasks(1);

		// 设置Map和Reduce处理类

		job.setMapperClass(Map.class);

		job.setReducerClass(Reduce.class);

		// 设置输出类型

		job.setOutputKeyClass(IntWritable.class);

		job.setOutputValueClass(IntWritable.class);

		// 设置输入和输出目录

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
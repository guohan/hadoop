package com.kit.hadoop.kpi;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.kit.hadoop.example.EJob;


public class KPITime
{
	public static class MyTimeMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1);
		private Text time = new Text();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			KPI kpi = KPI.filterTime(value.toString());
			if(kpi.isValid())
			{
				time.set(kpi.getTime_local());
				context.write(time, one);
			}
			
		}
		
	}
	
	public static class MyTimeReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		private IntWritable count = new IntWritable();
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
		{
			int sum = 0;
			for(IntWritable val : values)
			{
				sum += val.get();
			}
			count.set(sum);
			context.write(key, count);
		}
		
	}
	
	
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException
	{
		 File jarFile = EJob.createTempJar("target");

	      ClassLoader classLoader = EJob.getClassLoader();

	      Thread.currentThread().setContextClassLoader(classLoader);
	    Configuration conf = new Configuration();
	    
	    
	    String[] otherArgs = new String[2];

	    otherArgs[0] = "hdfs://172.16.19.158:8020/kpi/input";//计算原文件目录，需提前在里面存入文件

	    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	    otherArgs[1] =new Path("hdfs://172.16.19.158:8020/kpi/timeoutput") + time;//计算后的计算结果存储目录，每次程序执行的结果目录不能相同，所以添加时间标签


	    
	   
		Job job = new Job(conf, "Time count");
		 ((JobConf) job.getConfiguration()).setJar(jarFile.toString());//环境变量调用，添加此句则可在eclipse中直接提交mapreduce任务，如果将该java文件打成jar包，需要将该句注释掉，否则在执行时反而找不到环境变量
		 
		 job.setNumReduceTasks(1);
		job.setJarByClass(KPITime.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setMapperClass(MyTimeMapper.class);
		job.setCombinerClass(MyTimeReducer.class);
		job.setReducerClass(MyTimeReducer.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		System.exit(job.waitForCompletion(true)?0:1);
	}
}

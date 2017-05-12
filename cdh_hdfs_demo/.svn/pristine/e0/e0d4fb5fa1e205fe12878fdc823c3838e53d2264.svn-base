package com.kit.hadoop.kpi;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

/**
 * 
 * @author Administrator
 * 本例采用的数据源为个人网站的Web日志(由bsspirit提供)，大家也可以通过百度统计获得某个网站的日志。
 */
public class KPIPV {

  public static class MyPVMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private  IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException 
    {
    	
    	KPI kpi = KPI.filterPVs(value.toString());
    	if(kpi.isValid())
    	{
    		word.set(kpi.getRequest());
    		context.write(word, one);
    	}
    }
  }
  
  public static class MyPVReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for(IntWritable val : values)
      {
    	  sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }
	


  public static void main(String[] args) throws Exception {
	  
	   /**

       * 环境变量配置

       */

      File jarFile = EJob.createTempJar("target");

      ClassLoader classLoader = EJob.getClassLoader();

      Thread.currentThread().setContextClassLoader(classLoader);
    Configuration conf = new Configuration();
    
    
    String[] otherArgs = new String[2];

    otherArgs[0] = "hdfs://172.16.19.158:8020/kpi/input";//计算原文件目录，需提前在里面存入文件

    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    otherArgs[1] =new Path("hdfs://172.16.19.158:8020/kpi/pvoutput") + time;//计算后的计算结果存储目录，每次程序执行的结果目录不能相同，所以添加时间标签


    
    Job job = new Job(conf, "PV count");
    ((JobConf) job.getConfiguration()).setJar(jarFile.toString());//环境变量调用，添加此句则可在eclipse中直接提交mapreduce任务，如果将该java文件打成jar包，需要将该句注释掉，否则在执行时反而找不到环境变量
    job.setNumReduceTasks(1);
    job.setJarByClass(KPIPV.class);
    job.setMapperClass(MyPVMapper.class);
//    job.setCombinerClass(PVReducer.class);
    job.setReducerClass(MyPVReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

package com.kit.hadoop.kpi;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

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

public class KPIIP {

  public static class MyIPMapper 
       extends Mapper<Object, Text, Text, Text>{		//this value is Text
    
    private Text hello = new Text();
    private Text world = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
//     KPI kpi = KPI.filterIPs(value.toString());
    	KPI kpi= new KPI();  
    	  kpi.parser(value.toString());  
      if (kpi.isValid()) {
        hello.set(kpi.getRequest());
        world.set(kpi.getRemote_addr());
        context.write(hello, world);
      }
    }
  }
  
  public static class MyIPReducer 
       extends Reducer<Text,Text,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<Text> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      Set<String> set = new HashSet<String>();
      
      for (Text val : values) {
          set.add(val.toString());
        }
      result.set(set.size());
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
	  File jarFile = EJob.createTempJar("target");

      ClassLoader classLoader = EJob.getClassLoader();

      Thread.currentThread().setContextClassLoader(classLoader);
    Configuration conf = new Configuration();
    
    
    String[] otherArgs = new String[2];

    otherArgs[0] = "hdfs://172.16.19.158:8020/kpi/input";//计算原文件目录，需提前在里面存入文件

    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    otherArgs[1] =new Path("hdfs://172.16.19.158:8020/kpi/ipoutput") + time;//计算后的计算结果存储目录，每次程序执行的结果目录不能相同，所以添加时间标签


    
   
    Job job = new Job(conf, "IP count");
	 ((JobConf) job.getConfiguration()).setJar(jarFile.toString());//环境变量调用，添加此句则可在eclipse中直接提交mapreduce任务，如果将该java文件打成jar包，需要将该句注释掉，否则在执行时反而找不到环境变量
	 
	 job.setNumReduceTasks(1);
    job.setJarByClass(KPIIP.class);
    job.setMapperClass(MyIPMapper.class);
//    job.setCombinerClass(MyIPReducer.class);
    job.setReducerClass(MyIPReducer.class);
    
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);		//beacuse you choose value is Text, there also should fix
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

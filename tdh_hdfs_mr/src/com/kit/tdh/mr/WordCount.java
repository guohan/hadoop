package com.kit.tdh.mr;


import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.GenericOptionsParser;

/***
 * 
 * @author gh
 * mapreduce 实现wordcount计数功能
 *
 */
public class WordCount
{
  public static void main(String[] args)
    throws Exception
  {
    Configuration conf = new Configuration();
    
    
    
	
	
	//hadood hdfs 添加鉴权认证
    //create by lee
    UserGroupInformation.setConfiguration(conf);
    //设置登录名 与密码
    UserGroupInformation.loginUserFromKeytab//("hdfs/kit-b1@TDH", WordCount.class.getClassLoader().getResource("hdfs.keytab").getPath());
    ("kitdev", WordCount.class.getClassLoader().getResource("kitdev.keytab").getPath());
	
//  conf.set("mapreduce.job.jar","D:\\mr01.jar");
   
//    Job job = new Job(conf, "mr_wordcount"); 
  Job job=Job.getInstance(conf,"mr_wordcount_1114");//采用这种方法 资源消耗更少
//    job.setJar("D:\\mr01.jar");//指定jar 
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);//指定map
    job.setCombinerClass(IntSumReducer.class);//指定combiner
    job.setReducerClass(IntSumReducer.class);//指定reduce
    job.setOutputKeyClass(Text.class);//指定输出key的类型
    job.setOutputValueClass(IntWritable.class);//指定输出value的类型
    job.setNumReduceTasks(1);//指定输出文件的个数 reduce作业个数
//    long start=System.currentTimeMillis();
    FileInputFormat.addInputPath(job, new Path("hdfs://kit-b1:8020/user/discover/fpm.tx"));//文件输入路径
    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    FileOutputFormat.setOutputPath(job, new Path("hdfs://kit-b1:8020/user/discover/output061401"));//文件输出路径
//    job.isComplete();
//    System.out.println("the app used "+( job.getFinishTime()));
//    System.exit(job.waitForCompletion(true) ? 0 : 1);//等待job执行 
	if (job.waitForCompletion(true)) {
		System.out.println("ok!");
	}
	else{
		System.out.println("error!");
		System.exit(0);
	}
//    job.killJob();
 
  }
  /*

   * 用户自定义reduce函数，如果有多个热度测，则每个reduce处理自己对应的map结果数据

   * Reduce过程需要继承org.apache.hadoop.mapreduce包中Reducer类，并重写其reduce方法。

   * Map过程输出<key,values>中key为单个单词，而values是对应单词的计数值所组成的列表，Map的输出就是Reduce的输入，

   * 所以reduce方法只要遍历values并求和，即可得到某个单词的总次数。

   */
  public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable>
  {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context)
      throws IOException, InterruptedException
    {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      this.result.set(sum);
      context.write(key, this.result);
    }
  }

  
  /*

   * 用户自定义map函数，对以<key, value>为输入的结果文件进行处理

   * Map过程需要继承org.apache.hadoop.mapreduce包中Mapper类，并重写其map方法。

   * 通过在map方法中添加两句把key值和value值输出到控制台的代码

   * ，可以发现map方法中value值存储的是文本文件中的一行（以回车符为行结束标记），而key值为该行的首字母相对于文本文件的首地址的偏移量。

   * 然后StringTokenizer类将每一行拆分成为一个个的单词

   * ，并将<word,1>作为map方法的结果输出，其余的工作都交有MapReduce框架处理。 每行数据调用一次 Tokenizer：单词分词器

   */
  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
  {
    private static final IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException
    {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        this.word.set(itr.nextToken());
        context.write(this.word, one);
      }
    }
  }
}

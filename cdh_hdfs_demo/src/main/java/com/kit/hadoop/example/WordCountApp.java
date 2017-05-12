package com.kit.hadoop.example;

import java.io.IOException;  
import java.net.URI;  
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.FileSystem;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Counter;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
  
/** 
 * mapreduce中计数器的使用 
 * @author woshiccna 
 * 
 */  
public class WordCountApp {  
//	private static final  String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    private static final String INPUT_PATH = "hdfs://172.16.19.158:8020/blogs/share";  
    private static final String OUTPUT_PATH = "hdfs://172.16.19.158:8020/blogs/share/output";
      
    public static void main(String[] args) throws IOException, URISyntaxException,   
          ClassNotFoundException, InterruptedException {  
        Configuration conf = new Configuration();  
        final FileSystem fileSystem = FileSystem.get(new URI(OUTPUT_PATH), conf);  
        fileSystem.delete(new Path(OUTPUT_PATH), true);  
          
        final Job job = new Job(conf, WordCountApp.class.getSimpleName());  
        job.setJarByClass(WordCountApp.class);  
        job.setJar("D:\\a_hadoop_test/hadoop.jar");//指定jar 进行远程作业提交
        FileInputFormat.setInputPaths(job, INPUT_PATH);  
        job.setMapperClass(MyMapper.class);  
        job.setMapOutputKeyClass(Text.class);  
        job.setMapOutputValueClass(LongWritable.class);  
        job.setNumReduceTasks(1);//指定输出文件的个数 reduce作业个数
        job.setReducerClass(MyReducer.class);  
        job.setOutputKeyClass(Text.class);  
        job.setOutputValueClass(LongWritable.class);  
        FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));  
          
        job.waitForCompletion(true);  
    }  
  
    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {  
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {  
            final String line = value.toString();  
            StringTokenizer tokenizer = new StringTokenizer(line);  
            final Counter counter = context.getCounter("Sensitive", "hello");  
            if (value.toString().contains("hello")) {  
                counter.increment(5L);   //当查询到包含hello的词语时，计数器加1  
            }  
            while(tokenizer.hasMoreTokens()) {  
                String target = tokenizer.nextToken();  
                context.write(new Text(target), new LongWritable(1));  
            }  
        }  
    }  
      
    public static class MyReducer extends Reducer<Text, LongWritable, Text, LongWritable> {  
  
        @Override  
        protected void reduce(Text key, Iterable<LongWritable> value,  
                Reducer<Text, LongWritable, Text, LongWritable>.Context context)  
                throws IOException, InterruptedException {  
            long times = 0l;  
            while (value.iterator().hasNext()) {  
                times += value.iterator().next().get();  
            }  
            context.write(key, new LongWritable(times));  
        }  
          
    }  
      
}  

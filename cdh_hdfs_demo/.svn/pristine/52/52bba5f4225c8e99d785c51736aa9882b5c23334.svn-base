package com.kit.hadoop.example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.conf.Configured;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.IntWritable;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;  
import org.apache.hadoop.util.Tool;  
import org.apache.hadoop.util.ToolRunner;  
public class SecondTest extends Configured implements Tool{  
    enum Counter{  
        LINESKIP,  
    }     
    public static class Map extends Mapper<LongWritable,Text,Text,IntWritable>{  
        private static final IntWritable one = new IntWritable(1);   
        public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{  
            String line = value.toString();  
            try{  
                String[] lineSplit = line.split(",");  
                String requestUrl = lineSplit[4];  
                requestUrl = requestUrl.substring(requestUrl.indexOf(' ')+1, requestUrl.lastIndexOf(' '));  
                Text out = new Text(requestUrl);  
                context.write(out,one);  
            }catch(java.lang.ArrayIndexOutOfBoundsException e){  
                context.getCounter(Counter.LINESKIP).increment(1);  
            }             
        }  
    }     
    public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable>{          
        public void reduce(Text key, Iterable<IntWritable> values,Context context)throws IOException{  
            int count =  0;    
            for(IntWritable v : values){    
                count = count + 1;    
            }    
            try {  
                context.write(key, new IntWritable(count));  
            } catch (InterruptedException e) {  
                 e.printStackTrace();  
            }             
        }         
    }     
    @Override  
    public int run(String[] args) throws Exception {  
        Configuration conf = getConf();  
        conf.set("mapreduce.job.jar","D:\\hadoop_second.jar");
        Job job = new Job(conf, "logAnalysis");  
        job.setJarByClass(SecondTest.class);          
//        FileInputFormat.addInputPath(job, new Path(args[0]));  
        FileInputFormat.addInputPath(job, new Path("hdfs://172.16.19.158:8020/guohan/test0817.txt"));
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        FileOutputFormat.setOutputPath(job, new Path("hdfs://172.16.19.158:8020/guohan/output_url"+time));
//        FileOutputFormat.setOutputPath(job, new Path(args[1]));       
        job.setMapperClass(Map.class);  
        job.setReducerClass(Reduce.class);  
        job.setOutputFormatClass(TextOutputFormat.class);         
        //keep the same format with the output of Map and Reduce  
        job.setOutputKeyClass(Text.class);  
        job.setOutputValueClass(IntWritable.class);       
        job.waitForCompletion(true);  
        return job.isSuccessful()?0:1;  
    }     
    public static void main(String[] args)throws Exception{       
        int res = ToolRunner.run(new Configuration(), new SecondTest(),args);         
        System.exit(res);  
    }  
}  
package com.kit.hadoop.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 之所以存在reduce side join，是因为在map阶段不能获取所有需要的join字段，即：同一个key对应的字段可能位于不同map中。但
 * Reduce side join是非常低效的，因为shuffle阶段要进行大量的数据传输。Map side
 * join是针对以下场景进行的优化：两个待连接表中，有一个表非常大，而另一个表非常小，以至于小表可以直接存放到内存中。这样，我们可以将小表复制多份，
 * 让每个map task内存中存在一份（比如存放到hash table中），然后只扫描大表：对于大表中的每一条记录key/value，在hash
 * table中查找是否有相同的key的记录，如果有，则连接后输出即可。为了支持文件的复制，Hadoop提供了一个类DistributedCache，
 * 使用该类的方法如下：
 * 
 * （1）用户使用静态方法DistributedCache.addCacheFile()指定要复制的文件，它的参数是文件的URI（如果是HDFS上的文件，
 * 可以这样：hdfs://jobtracker:50030/home/XXX/file）。JobTracker在作业启动之前会获取这个URI列表，
 * 并将相应的文件拷贝到各个TaskTracker的本地磁盘上。
 * 
 * （2）用户使用DistributedCache.getLocalCacheFiles()方法获取文件目录，并使用标准的文件读写API读取相应的文件。
 * 
 * @author Administrator
 *
 */
public class MultiTableJoin extends Configured implements Tool {
	public static class MapClass
			extends
				Mapper<LongWritable, Text, Text, Text> {

		// 用于缓存 sex、user 文件中的数据
		private Map<String, String> userMap = new HashMap<String, String>();
		private Map<String, String> sexMap = new HashMap<String, String>();

		private Text oKey = new Text();
		private Text oValue = new Text();
		private String[] kv;

		// 此方法会在map方法执行之前执行
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			BufferedReader in = null;

			try {
				// 从当前作业中获取要缓存的文件
				Path[] paths = DistributedCache
						.getLocalCacheFiles(context.getConfiguration());
				String uidNameAddr = null;
				String sidSex = null;
				for (Path path : paths) {
					if (path.toString().contains("user")) {
						in = new BufferedReader(
								new FileReader(path.toString()));
						while (null != (uidNameAddr = in.readLine())) {
							userMap.put(uidNameAddr.split("\t", -1)[0],
									uidNameAddr.split("\t", -1)[1]);
						}
					} else if (path.toString().contains("sex")) {
						in = new BufferedReader(
								new FileReader(path.toString()));
						while (null != (sidSex = in.readLine())) {
							sexMap.put(sidSex.split("\t", -1)[0],
									sidSex.split("\t", -1)[1]);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			kv = value.toString().split("\t");
			// map join: 在map阶段过滤掉不需要的数据
			if (userMap.containsKey(kv[0]) && sexMap.containsKey(kv[1])) {
				oKey.set(userMap.get(kv[0]) + "\t" + sexMap.get(kv[1]));
				oValue.set("1");
				context.write(oKey, oValue);
			}
		}

	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		private Text oValue = new Text();

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			int sumCount = 0;

			for (Text val : values) {
				sumCount += Integer.parseInt(val.toString());
			}
			oValue.set(String.valueOf(sumCount));
			context.write(key, oValue);
		}

	}

	public int run(String[] args) throws Exception {

		 File jarFile = EJob.createTempJar("target");

	      ClassLoader classLoader = EJob.getClassLoader();

	      Thread.currentThread().setContextClassLoader(classLoader);
		Job job = new Job(getConf(), "MultiTableJoin");
		 ((JobConf) job.getConfiguration()).setJar(jarFile.toString());//

		job.setJobName("MultiTableJoin");
		job.setJarByClass(MultiTableJoin.class);
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		String[] otherArgs = new GenericOptionsParser(job.getConfiguration(),
				args).getRemainingArgs();

		// 我们把第1、2个参数的地址作为要缓存的文件路径
		DistributedCache.addCacheFile(
//				new Path(otherArgs[1]).toUri(),""
				new Path("hdfs://172.16.19.158:8020/blogs/msj/sex.txt").toUri(),
				job.getConfiguration());
		DistributedCache.addCacheFile(
//				new Path(otherArgs[2]).toUri(),
				new Path("hdfs://172.16.19.158:8020/blogs/msj/user.txt").toUri(),
				job.getConfiguration());
		FileInputFormat.addInputPath(job, 
//				new Path(otherArgs[3])
				new Path("hdfs://172.16.19.158:8020/blogs/msj/login.txt")
				);
		FileOutputFormat.setOutputPath(job, 
//				new Path(otherArgs[4])
				new Path("hdfs://172.16.19.158:8020/blogs/msj/output")
				);
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
//		args=new String["123","","",""];
		int res = ToolRunner.run(new Configuration(), new MultiTableJoin(),
				args);
		System.exit(res);
	}

}

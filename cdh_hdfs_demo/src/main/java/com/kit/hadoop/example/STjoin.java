package com.kit.hadoop.example;

import java.io.IOException;

import java.util.*;

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
 *http://www.cnblogs.com/xia520pi/archive/2012/06/04/2534533.html
 */
public class STjoin
{

	public static int time = 0;

	/*
	 * 
	 * map将输出分割child和parent，然后正序输出一次作为右表，
	 * 
	 * 反序输出一次作为左表，需要注意的是在输出的value中必须
	 * 
	 * 加上左右表的区别标识。
	 * 
	 */

	public static class Map extends Mapper<Object, Text, Text, Text>
	{

		// 实现map函数

		public void map(Object key, Text value, Context context)

				throws IOException, InterruptedException
		{

			String childname = new String();// 孩子名称

			String parentname = new String();// 父母名称

			String relationtype = new String();// 左右表标识

			// 输入的一行预处理文本

			StringTokenizer itr = new StringTokenizer(value.toString());

			String[] values = new String[2];

			int i = 0;

			while (itr.hasMoreTokens())
			{

				values[i] = itr.nextToken();

				i++;

			}

			if (values[0].compareTo("child") != 0)
			{

				childname = values[0];

				parentname = values[1];

				// 输出左表

				relationtype = "1";

				context.write(new Text(values[1]), new Text(relationtype +

						"+" + childname + "+" + parentname));

				// 输出右表

				relationtype = "2";

				context.write(new Text(values[0]), new Text(relationtype +

						"+" + childname + "+" + parentname));

			}

		}

	}

	public static class Reduce extends Reducer<Text, Text, Text, Text>
	{

		// 实现reduce函数

		public void reduce(Text key, Iterable<Text> values, Context context)

				throws IOException, InterruptedException
		{

			// 输出表头

			if (0 == time)
			{

				context.write(new Text("grandchild"), new Text("grandparent"));

				time++;

			}

			int grandchildnum = 0;

			String[] grandchild = new String[10];

			int grandparentnum = 0;

			String[] grandparent = new String[10];

			Iterator ite = values.iterator();

			while (ite.hasNext())
			{

				String record = ite.next().toString();

				int len = record.length();

				int i = 2;

				if (0 == len)
				{

					continue;

				}

				// 取得左右表标识

				char relationtype = record.charAt(0);

				// 定义孩子和父母变量

				String childname = new String();

				String parentname = new String();

				// 获取value-list中value的child

				while (record.charAt(i) != '+')
				{

					childname += record.charAt(i);

					i++;

				}

				i = i + 1;

				// 获取value-list中value的parent

				while (i < len)
				{

					parentname += record.charAt(i);

					i++;

				}

				// 左表，取出child放入grandchildren

				if ('1' == relationtype)
				{

					grandchild[grandchildnum] = childname;

					grandchildnum++;

				}

				// 右表，取出parent放入grandparent

				if ('2' == relationtype)
				{

					grandparent[grandparentnum] = parentname;

					grandparentnum++;

				}

			}

			// grandchild和grandparent数组求笛卡尔儿积

			if (0 != grandchildnum && 0 != grandparentnum)
			{

				for (int m = 0; m < grandchildnum; m++)
				{

					for (int n = 0; n < grandparentnum; n++)
					{

						// 输出结果

						context.write(new Text(grandchild[m]), new Text(grandparent[n]));

					}

				}

			}

		}

	}

	public static void main(String[] args) throws Exception
	{

		Configuration conf = new Configuration();

		// 这句话很关键

//		conf.set("mapred.job.tracker", "192.168.1.2:9001");

		 String[] ioArgs=new String[]{"hdfs://172.16.19.158:8020/blogs/childparent","hdfs://172.16.19.158:8020/blogs/childparent/output"};

		String[] otherArgs = new GenericOptionsParser(conf, ioArgs).getRemainingArgs();

		if (otherArgs.length != 2)
		{

			System.err.println("Usage: Single Table Join <in> <out>");

			System.exit(2);

		}

		Job job = new Job(conf, "Single Table Join");

		job.setJarByClass(STjoin.class);

		// 设置Map和Reduce处理类

		job.setMapperClass(Map.class);

		job.setReducerClass(Reduce.class);
		  job.setJar("D:\\hadoop.jar");//指定jar 进行远程作业提交
		  job.setNumReduceTasks(1);

		// 设置输出类型

		job.setOutputKeyClass(Text.class);

		job.setOutputValueClass(Text.class);

		// 设置输入和输出目录

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}

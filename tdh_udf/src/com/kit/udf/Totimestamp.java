package com.kit.udf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.LongWritable;
/**
 * 
 * @author guohan
 * @createtime 2017-02-22
 * 转换时间
 * udf 开发要点：
 * 一个用户udf必须继承org.apache.hadoop.hive.exec.UDF
 * 2 一个udf需要实现evaluate方法。evaluate的参数个数以及类型都是用户自己定义的。在使用
 * udf的时候，inceptor会调用udf的evaluate方法
 * demo是以yyyy-MM-dd hh:mm:ss形式的时间字符串为参数返回时间戳timestamp，单位为毫秒
 *
 */
public class Totimestamp extends UDF {

	public LongWritable evaluate(String time) {
		String str1 = time.substring(0, time.indexOf("."));
		String str2 = time.substring(0, time.indexOf(".")+1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long millisec;
		Date date = new Date();
		try {
			date = sdf.parse(str1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		millisec = date.getTime() + Long.parseLong(str2);
		LongWritable result = new LongWritable(millisec);
		System.out.println(result);
		return result;
	}
}

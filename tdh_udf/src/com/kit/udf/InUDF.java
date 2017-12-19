package com.kit.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * 
 * @author guohan
 * @createtime 2017-02-22
 * @description
 *将IP地址转换成数字表示
 * 1 一个用户udf必须继承org.apache.hadoop.hive.exec.UDF
 * 2 一个udf需要实现evaluate方法。evaluate的参数个数以及类型都是用户自己定义的。在使用
 * udf的时候，inceptor会调用udf的evaluate方法
 */

@Description(name = "ip2num",
value = "_FUNC_(ipcode) - Returns returns the NUMBER code",
extended = "Example:\n"
+ "  > SELECT _FUNC_(192.168.56.101) FROM ipudf LIMIT 1;\n" + "  t\n"
+ "  > SELECT _FUNC_(192.168.19.79) FROM ipudf LIMIT 1;\n" + "  T\n")

public class InUDF extends UDF {
	public static long evaluate(String ipNum) {
		String[] arr = ipNum.split("\\.");
		if (arr.length == 4) {
			return Integer.parseInt(arr[0]) * 256 * 256 * 256l
					+ Integer.parseInt(arr[1]) * 256 * 256l
					+ Integer.parseInt(arr[2]) * 256l
					+ Integer.parseInt(arr[3]);
		} else {
			return 0l;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(evaluate("192.168.56.101"));
	}
}

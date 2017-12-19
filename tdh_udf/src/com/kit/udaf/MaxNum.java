package com.kit.udaf;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.IntWritable;

/**
 * 
 * @author gh
 * @createtime 2017-02-22
 * @description 用户的udaf必须继承了org.apache.hadoop.hive.ql.exec.udaf
 *              用户的udaf必须包含至少一个实现org.apache.hadoop.hive.ql.exec的静态类.
 *              诸如常见的实现了udafevaluator 一个计算函数必须实现的5个方法的具体含义如下: 1
 *              init:主要负责初始化计算函数并且重设其内部状态，一般就是重设其内部字段。一般在静态类中定义一个内部字段来存放最终的结果. 2
 *              iterate:每一次对一个新值进行聚集计算的时候都会调用该方法，计算函数会根据聚集计算结果更新内部状态，
 *              当输入值合法或者正确计算了，就返回true. 3
 *              terminatepartial:hive需要部分聚集结果的时候调用该方法，必须要返回一个封装了聚集计算当前状态的对象 4
 *              merge:hive进行合并一个部分聚集和另一个部分聚集的时候会调用该方法 5
 *              terminate:hive最终聚集结果的时候就会调用该方法，计算函数需要把状态作为一个值返回给用户
 *              下面demo实现找到最大值功能，以表中某一字段为参数，返回最大值
 *
 */
public class MaxNum extends UDAF {
	public static class MaxUDAFE implements UDAFEvaluator {
		private IntWritable result;
		// 负责初始化计算函数并设置它的内部状态 ,result是存放最终结果的
		@Override
		public void init() {
			// TODO Auto-generated method stube
			result = null;

		}
		// 每次对一个新值进行聚集计算都会调用iterate方法
		public boolean iterate(IntWritable value) {
			if (value == null)
				if (result == null)
					result = new IntWritable(value.get());

				else
					result.set(Math.max(result.get(), value.get()));
			return true;
		}

		public IntWritable terminatePartial() {
			return result;
		}
		public boolean merge(IntWritable other) {
			return iterate(other);
		}
		public IntWritable terminate() {
			return result;
		}

	}

}

package com.kit.beam;


import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Distinct;

/**
 * 
 * @author guohan
 * @createtime 2017-03-17
 * @description
 * 去重也是对数据集比较常见的操作，使用Apache Beam来实现，示例代码如下所示：
 * 
 *  java -cp   beam-0.0.1-SNAPSHOT.jar  com.kit.beam.JoinExample
 *
 */
public class DistinctExample {

	public static void main(String[] args) throws Exception {

		PipelineOptions options = PipelineOptionsFactory.create();

//		options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：DirectRunner（Local模式）

		Pipeline pipeline = Pipeline.create(options);

		pipeline.apply(TextIO.Read.from("D:\\1.txt"))

				.apply(Distinct.<String> create()) // 创建一个处理String类型的PTransform：Distinct

				.apply(TextIO.Write.to("deduped.txt")); // 输出结果

		pipeline.run().waitUntilFinish();

	}
}

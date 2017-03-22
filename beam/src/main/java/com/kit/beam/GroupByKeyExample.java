package com.kit.beam;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.runners.direct.repackaged.com.google.common.base.Joiner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

/**
 * 
 * @author Administrator
 * @description 对数据进行分组操作也非常普遍，我们拿一个最基础的PTransform实现GroupByKey来实现一个例子
 */
public class GroupByKeyExample {

	@SuppressWarnings("serial")

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.create();

//		options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：DirectRunner（Local模式）

		Pipeline pipeline = Pipeline.create(options);

		pipeline.apply(TextIO.Read.from("/tmp/dataset/MY_INFO_FILE.txt"))

				.apply("ExtractFields",
						ParDo.of(new DoFn<String, KV<String, String>>() {

							@ProcessElement

							public void processElement(ProcessContext c) {

								// file format example: 35451605324179 3G CMCC

								String[] values = c.element().split("\t");

								if (values.length == 3) {

									c.output(KV.of(values[1], values[0]));

								}

							}

						}))

				.apply("GroupByKey", GroupByKey.<String, String> create()) // 创建一个GroupByKey实例的PTransform

				.apply("ConcatResults", MapElements.via(

						new SimpleFunction<KV<String, Iterable<String>>, String>() {

							@Override

							public String apply(
									KV<String, Iterable<String>> input) {

								return new StringBuffer()

										.append(input.getKey()).append("\t")

										.append(Joiner.on(",")
												.join(input.getValue()))

										.toString();

							}

						}))

				.apply(TextIO.Write.to("grouppedResults"));

		pipeline.run().waitUntilFinish();
	}

}

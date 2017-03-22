package com.kit.beam;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

/**
 * 
 * @author guohan
 * @createtime 2017-03-17
 * @description
 * beam 使用demo
 *
 */
public class MinimalWordCount {

	public static void main(String[] args) {
		PipelineOptions options = PipelineOptionsFactory.create();
		options.setJobName("");
//		options.setRunner(DirectRunner.class);// 显示指定pipelinerunner：DirectRunner
												// local模式
		Pipeline pipeline = Pipeline.create(options);

		// ++++++++++++以HDFS数据源作为Source+++++++++++
		// PCollection<KV<LongWritable, Text>> resultCollection =
		// pipeline.apply(HDFSFileSource.readFrom(
		//
		// "hdfs://myserver:8020/data/ds/beam.txt",
		//
		// TextInputFormat.class, LongWritable.class, Text.class))

		pipeline.apply(TextIO.Read
				.from("D:\\1.txt"))/// 读取本地文件，构建第一个PTransform
				.apply("ExtractWords", ParDo.of(new DoFn<String, String>() {
					@ProcessElement
				public void processElement(ProcessContext c) {// 对文件中每一行进行处理（实际上Split）
						for (String word : c.element()
								.split("[\\s:\\,\\.\\-]+")) {
							if (!word.isEmpty()) {
								c.output(word);
							}
						}
					}
				})).apply(Count.<String> perElement())
				.apply("ConcatResultKVs", MapElements.via( // 拼接最后的格式化输出（Key为Word，Value为Count）
						new SimpleFunction<KV<String, Long>, String>() {
							@Override
							public String apply(KV<String, Long> input) {
								return input.getKey() + ": " + input.getValue();
							}
						}))
				.apply(TextIO.Write.to("wordcount")); // 输出结果
		pipeline.run().waitUntilFinish();
	}
}

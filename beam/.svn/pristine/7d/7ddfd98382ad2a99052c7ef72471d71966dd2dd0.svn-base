package com.kit.beam;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;

/**
 * 
 * @author guohan
 * @createtime 2017-03-17
 * @description
 * 
 *通过Join操作后，能够知道用户使用的什么网络（用户名+网络），使用Apache Beam实现
 */
public class JoinExample {
	@SuppressWarnings("serial")

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.create();

//		options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：DirectRunner（Local模式）

		Pipeline pipeline = Pipeline.create(options);

		// create ID info collection

		final PCollection<KV<String, String>> idInfoCollection = pipeline

				.apply(TextIO.Read.from("D:\\MY_ID_INFO_FILE.txt"))

				.apply("CreateUserIdInfoPairs", MapElements.via(

						new SimpleFunction<String, KV<String, String>>() {

							@Override

							public KV<String, String> apply(String input) {

								// line format example: 35451605324179 Jack

								String[] values = input.split("\t");
System.out.println("values[0]"+values[0]+"values[1]"+values[1]);
								return KV.of(values[0], values[1]);

							}

						}));

		// create operation collection

		final PCollection<KV<String, String>> opCollection = pipeline

				.apply(TextIO.Read.from("D:\\MY_ID_OP_INFO_FILE.txt"))

				.apply("CreateIdOperationPairs", MapElements.via(

						new SimpleFunction<String, KV<String, String>>() {

							@Override

							public KV<String, String> apply(String input) {

								// line format example: 35237005342309 3G CMCC

								String[] values = input.split("\t");
								System.out.println("values[0]"+values[0]+"values[1]"+values[1]);
								return KV.of(values[0], values[1]);

							}

						}));

		final TupleTag<String> idInfoTag = new TupleTag<String>();

		final TupleTag<String> opInfoTag = new TupleTag<String>();

		final PCollection<KV<String, CoGbkResult>> cogrouppedCollection = KeyedPCollectionTuple

				.of(idInfoTag, idInfoCollection)

				.and(opInfoTag, opCollection)

				.apply(CoGroupByKey.<String> create());

		final PCollection<KV<String, String>> finalResultCollection = cogrouppedCollection

				.apply("CreateJoinedIdInfoPairs", ParDo.of(
						new DoFn<KV<String, CoGbkResult>, KV<String, String>>() {

							@ProcessElement

							public void processElement(ProcessContext c) {

								KV<String, CoGbkResult> e = c.element();

								String id = e.getKey();

								String name = e.getValue().getOnly(idInfoTag);

								for (String opInfo : c.element().getValue()
										.getAll(opInfoTag)) {

									// Generate a string that combines
									// information from both collection values

									c.output(KV.of(id,
											"\t" + name + "\t" + opInfo));

								}

							}

						}));

		PCollection<String> formattedResults = finalResultCollection

				.apply("FormatFinalResults",
						ParDo.of(new DoFn<KV<String, String>, String>() {

							@ProcessElement

							public void processElement(ProcessContext c) {

								c.output(c.element().getKey() + "\t"
										+ c.element().getValue());

							}

						}));

		formattedResults.apply(TextIO.Write.to("joinedResults"));

		pipeline.run().waitUntilFinish();

	}
}

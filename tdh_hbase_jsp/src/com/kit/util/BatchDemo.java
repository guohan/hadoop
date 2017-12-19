package com.kit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.util.Bytes;

import com.kit.HbaseUtil;

public class BatchDemo {

	private final static byte[] row1 = Bytes.toBytes("row1");
	private final static byte[] row2 = Bytes.toBytes("row2");
	private final static byte[] c1 = Bytes.toBytes("c1");
	private final static byte[] c2 = Bytes.toBytes("c2");
	private final static byte[] q1 = Bytes.toBytes("q1");
	private final static byte[] q2 = Bytes.toBytes("q2");
	public static void main(String[] args) throws Exception {
		List<Row> batch = new ArrayList<Row>();
		Put put = new Put(row2);
		put.add(c2, q1, Bytes.toBytes("v5"));
		batch.add(put);

		Get get1 = new Get(row1);
		get1.addColumn(c1, q1);
		batch.add(get1);

		Delete delete = new Delete(row1);
		delete.deleteColumns(c1, q2);
		batch.add(delete);

		Get get2 = new Get(row2);
		get2.addFamily(Bytes.toBytes("ccc"));
		batch.add(get2);
		
		Object[] results= new Object[batch.size()];
		HTable t;
		try {
			t = new HTable(HbaseUtil.getConfiguration(), "");
			t.batch(batch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

package com.kit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.kit.HbaseUtil;

/**
 * Used by the book examples to generate tables and fill them with test data.
 */
public class HBaseHelper {
	// 在 Java 代码中，为了连接到 HBase，我们首先创建一个配置（Configuration）对象，使用该对象创建一个 HTable 实例。
	// 这个 HTable 对象用于处理所有的客户端 API 调用。
	private Configuration conf = null;
	private HBaseAdmin admin = null;

	protected HBaseHelper(Configuration conf) throws IOException {
		this.conf = conf;
		this.admin = new HBaseAdmin(conf);
	}

	public static HBaseHelper getHelper(Configuration conf) throws IOException {
		return new HBaseHelper(conf);
	}

	public void createTable(String tableName, String[] familys) {
		try {
			// HTable table = (HTable)pool.getTable(tableName);
			// admin.getTableDescriptor(tableName)
			HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);;
			if (!hBaseAdmin.tableExists(tableName)) {
				HTableDescriptor tableDescriptor = new HTableDescriptor(
						tableName);
				for (int i = 0; i < familys.length; i++) {
					tableDescriptor
							.addFamily(new HColumnDescriptor(familys[i]));
				}
				hBaseAdmin.createTable(tableDescriptor);
				System.out.println("create success");
			}
			hBaseAdmin.flush(tableName);
			hBaseAdmin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void put(String table, String row, String fam, String qual, long ts,
			String val) throws IOException {
		HTable tbl = new HTable(conf, table);
		Put put = new Put(Bytes.toBytes(row));
		put.add(Bytes.toBytes(fam), Bytes.toBytes(qual), ts,
				Bytes.toBytes(val));
		tbl.put(put);
		tbl.close();
	}

	public void put(String table, String[] rows, String[] fams, String[] quals,
			long[] ts, String[] vals) throws IOException {
		HTable tbl = new HTable(conf, table);
		for (String row : rows) {
			Put put = new Put(Bytes.toBytes(row));
			for (String fam : fams) {
				int v = 0;
				for (String qual : quals) {
					String val = vals[v < vals.length ? v : vals.length];
					long t = ts[v < ts.length ? v : ts.length - 1];
					put.add(Bytes.toBytes(fam), Bytes.toBytes(qual), t,
							Bytes.toBytes(val));
					v++;
				}
			}
			tbl.put(put);
		}
		tbl.close();
	}

	public void dump(String table, String[] rows, String[] fams, String[] quals)
			throws IOException {
		HTable tbl = new HTable(conf, table);
		List<Get> gets = new ArrayList<Get>();
		for (String row : rows) {
			Get get = new Get(Bytes.toBytes(row));
			get.setMaxVersions();
			if (fams != null) {
				for (String fam : fams) {
					for (String qual : quals) {
						get.addColumn(Bytes.toBytes(fam), Bytes.toBytes(qual));
					}
				}
			}
			gets.add(get);
		}
		Result[] results = tbl.get(gets);
		for (Result result : results) {
			for (KeyValue kv : result.raw()) {
				System.out.println("KV: " + kv + ", Value: "
						+ Bytes.toString(kv.getValue()));
			}
		}
	}
	public void dropTable(String tableName) {
		try {

			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				admin.close();
			}
			System.out.println("delete success");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

// public void dropTable(String table) throws IOException {
// if (existsTable(table)) {
// disableTable(table);
// admin.deleteTable(table);
// }
// }

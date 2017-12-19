package com.kit.hbase.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

/**
 * @author yan.zhang
 * hbase提供的数据库操作api演示demo
 */
public class HbaseUtil {
	
    private static Configuration conf;
    
	public static void open() throws IOException {
		
		
		
//		System.setProperty("hadoop.home.dir","D:\\linux\\resource\\");
		
		conf = HBaseConfiguration.create();
//		conf.set("hbase.rootdir", "hdfs://kit-b1:9000/hyperbase1");
		 conf.set("hbase.rootdir", "hdfs://kit-b1:9000/hyperbase1");
		conf.set("hbase.cluster.distributed", "true");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "kit-b1");
		conf.set("zookeeper.znode.parent", "/hyperbase1");
		conf.set("hbase.master", "kit-b1:60000");
//		conf.set("hbase.security.authentication", "kerberos");
//		conf.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");y
//		conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
	    //Login with keytab
	    UserGroupInformation.setConfiguration(conf);
	    UserGroupInformation.loginUserFromKeytab("hbase/kit-b2@TDH", HyperbaseDemo.class.getClassLoader().getResource("hbase.keytab").getPath());
	    HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
	    System.out.println(hBaseAdmin.tableExists("xxx"));
	    System.out.println("sysout is ok");
	}
	
	/**
	 * 建立表
	 * @param tableName 表名
	 * @param familys 列族
	 */
	public static void create(String tableName, String[] familys) {
		try {
			HbaseUtil.open();
			HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
			if (!hBaseAdmin.tableExists(tableName)) {
				HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
				for (int i = 0; i < familys.length; i++) {
					tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
				}
				hBaseAdmin.createTable(tableDescriptor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 插入单行
	 * @param tableName 表名
	 * @param key 主键
	 * @param map 对象集合 Map<列族名, Map<列名, 值>>
	 */
	public static void add(String tableName, String key, Map<String, Map<String, String>> map) {
		try {
			HbaseUtil.open();
			HTable table = new HTable(conf, tableName);
			List<Put> list = new ArrayList();
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				Map object = (Map)entry.getValue();
				String family = (String)entry.getKey();
				Iterator entryIt= object.entrySet().iterator();
				while (entryIt.hasNext()) {
					Put put = new Put(key.getBytes()); 
					Map.Entry subentry = (Map.Entry)entryIt.next();
					String value = (String)subentry.getValue();
					String column = (String)subentry.getKey();
					put.add(family.getBytes(), column.getBytes(), value.getBytes());
					list.add(put);
				}
			}
			table.put(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得单行
	 * @param tableName 表名
	 * @param key 主键
	 * @return
	 */
	public static Result get(String tableName, String key) {
		try {
			HbaseUtil.open();
			HTable table = new HTable(conf, tableName);
			Get get = new Get(key.getBytes());  
			Result result = table.get(get);  
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得集合
	 * @param tableName 表名
	 * @param family 条件列族
	 * @param column 条件列
	 * @param value 条件值
	 */
	public static Iterator list(String tableName, String family, String column, String value) {
		try {
			HbaseUtil.open();
			HTable table = new HTable(conf, tableName);
			Scan scan=new Scan();  
			scan.addColumn(family.getBytes(), column.getBytes());
			SingleColumnValueFilter filter=new SingleColumnValueFilter(  
					family.getBytes(), column.getBytes(), CompareOp.EQUAL, value.getBytes()
			);  
			scan.setFilter(filter);
			ResultScanner scanner = table.getScanner(scan);
			Iterator<Result> res = scanner.iterator();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 以下为建表、插入数据、根据主键获取数据、根据查询条件获取数据四种常见的数据库操作
	 * 根据主键获取数据和根据查询条件获取数据可以把查询结果直接封装到一个bean方便使用，暂未实现
	 * 执行以下步骤前，请保证hbase和hdfs已经启动
	 */
	public static void main(String[] args) throws Exception, ZooKeeperConnectionException {
		/** 1.建表，表名people，建立两个列族info和account */
		Configuration conf = new Configuration();
	    conf.set("zookeeper.znode.parent", "/hyperbase1");
	    conf.set("hbase.zookeeper.property.clientPort", "2181");
	    conf.set("hbase.zookeeper.quorum", "172.16.19.151,172.16.19.152,172.16.19.153");
	    conf.set("hbase.master", "172.16.19.153:60000");
	    conf.set("hbase.cluster.distributed", "true");
	    
	    conf.set("hbase.security.authentication", "kerberos");
	    conf.set("hadoop.security.authentication", "kerberos");
	    
	    conf.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
	    conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
	   
	    UserGroupInformation.setConfiguration(conf);
	    UserGroupInformation.loginUserFromKeytab("hbase/kit-b1@TDH", "D:\\hbase.keytab");
	    HBaseAdmin admin = new HBaseAdmin(conf);
	    //HTableDescriptor tableDescriptor = new HTableDescriptor("");
	    System.out.println(admin.tableExists("xxx"));
	    
	    HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("hhh"));
	    tableDescriptor.addFamily(new HColumnDescriptor("aaa"));
	    admin.createTable(tableDescriptor);
		//open();
		//HbaseUtil.create("people0706", new String[]{"info", "account"});	
//		
//		/** 2.插入数据，两条数据，数据结构如：zy,info:name=zhangyan,account:amount=10000;tb,info:name=tangbin,account:amount=1000000 */
//		Map map = new HashMap();
//		Map infomap = new HashMap();
//		infomap.put("name", "zhangyan");
//		Map accountmap = new HashMap();
//		accountmap.put("amount", "10000");
//		map.put("info", infomap);
//		map.put("account", accountmap);
//		HbaseUtil.add("people", "zy", map);
//		map = new HashMap();
//		infomap = new HashMap();
//		infomap.put("name", "tangbin");
//		accountmap = new HashMap();
//		accountmap.put("amount", "1000000");
//		map.put("info", infomap);
//		map.put("account", accountmap);
//		HbaseUtil.add("people", "tb", map);
//		
//		/** 3.获得单行,查找peopele中主键为zy的数据 */
//		Result result = HbaseUtil.get("people", "zy");
//		byte[] b = result.getValue("info".getBytes(), "name".getBytes());
//		String str = new String(b);
//		System.out.println(str);
		
/*		*//** 4.根据条件获得集合，people表中info列族的name列值为zhangyan的所有数据 *//*
		Iterator it = HbaseUtil.list("people", "info", "name", "zhangyan");
		while(it.hasNext()) {
			Result object = (Result)it.next();
			byte[] bb = object.getValue("info".getBytes(), "name".getBytes());
			String strs = new String(bb);
			System.out.println(strs);
		}*/
	}
}

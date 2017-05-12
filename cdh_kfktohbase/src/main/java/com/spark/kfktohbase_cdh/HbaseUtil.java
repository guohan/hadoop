package com.spark.kfktohbase_cdh;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author gh
 * @createTime 20160722
 * hbase提供的数据库操作api演示
 * hbase工具类
 */
public class HbaseUtil {
	
    private static Configuration conf;
    private static HBaseAdmin admin = null;
    private static final int MAX_TABLE_COUNT = 10;
    
    private static HConnection conn = null;  
    /**
     * 配置数据库链接 ：添加基本信息和鉴权配置信息
     */
	public static void open() {
		
		InputStream is = TestProducer.class.getClassLoader().getResourceAsStream("kfk-test.properties");
	    Properties p = new Properties();
//	    	加载资源
		
		conf = HBaseConfiguration.create(); 
//		conf.set("zookeeper.znode.parent", "/hyperbase1");
//		conf.set("hbase.zookeeper.property.clientPort", "2181");
//		conf.set("hbase.zookeeper.quorum", "172.16.19.151,172.16.19.152,172.16.19.153");
//		conf.set("hbase.master", "172.16.19.153:60000");
//		conf.set("hbase.cluster.distributed", "true");
//		
//		//添加kerberos认证相关信息
//		conf.set("hbase.security.authentication", "kerberos");
//		conf.set("hadoop.security.authentication", "kerberos");
//		conf.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
//		conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
		
		  conf.set("hbase.zookeeper.quorum", "172.16.19.158");
	        UserGroupInformation.setConfiguration(conf);
	        try
			{
	        	p.load(is);
	        	//配置登陆
		        admin = new HBaseAdmin(conf);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	        if(conn == null)  
	        {  
	            /* 
	             * * 创建一个HConnection 
	             * HConnection connection = HConnectionManager.createConnection(conf);  
	             * HTableInterface table = connection.getTable("mytable"); 
	             * table.get(...); ... 
	             * table.close();  
	             * connection.close(); 
	             * */  
	            try
				{
					conn = HConnectionManager.createConnection(conf);
				} catch (IOException e)
				{
					e.printStackTrace();
				}  
	        }  
	        
	        
	}
	
	//构建hconnection实列
	 public static synchronized HConnection getHConnection() throws ZooKeeperConnectionException  
	    {  
	        if(conn == null)  
	        {  
	            /* 
	             * * 创建一个HConnection 
	             * HConnection connection = HConnectionManager.createConnection(conf);  
	             * HTableInterface table = connection.getTable("mytable"); 
	             * table.get(...); ... 
	             * table.close();  
	             * connection.close(); 
	             * */  
	            try
				{
					conn = HConnectionManager.createConnection(conf);
				} catch (IOException e)
				{
					e.printStackTrace();
				}  
	        }  
	          
	        return conn;  
	    }  
	/**
	 * 建立表
	 * @param tableName 表名
	 * @param familys 列族
	 * 
	 * 
	 * 
	 *   HTable table = (HTable)pool.getTable("user_test_xuyang");
        //批量操作 共两种 底层都是调用  HConnection的processBatch方法(
        // table.batch(List<Put>) 和table.flushCommits()会直接调用)
 
        //首先  自动flush 关闭    就像 JDBC中的 auto_commit,  否则 加每一条 提交
        // 一次,影响性能     不过table.put(List<Put>) table.batch(List<Row>)不受这
        // 个影响, 设置false,只有当put总大小超过writeBufferSize 才提交  或者手工
        // table.flushCommits() （table.put(List<Put>)操作完成后会手工提交一次）,
        // writeBufferSize 也可以调整
        table.setAutoFlush(false);
        //writeBufferSize 默认为2M ,调大可以增加批量处理的吞吐量, 丢失数据的风险也会加大
        table.setWriteBufferSize(1024*1024*5);
        //这样可以看到 当前客户端缓存了多少put
        ArrayList<Put> putx = table.getWriteBuffer();
	 */
	public static void create(String tableName, String[] familys) {
		try {
			HbaseUtil.open();
//			 HTable table = (HTable)pool.getTable(tableName);
//			 admin.getTableDescriptor(tableName)
			HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);;
			if (!hBaseAdmin.tableExists(tableName)) {
				HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
				for (int i = 0; i < familys.length; i++) {
					tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
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
	
	/**
	 * 根据htablepool 去创建表
	 * @param tableName
	 * @param familys
	 * 
	 */
	public static void create2(String tableName, String[] familys) {
         
		try {
			HbaseUtil.open();
//			 HTableInterface usersTable = getHConnection().getTable(tableName);  
//			 usersTable.setAutoFlush(true);
//			 usersTable.setWriteBufferSize(1024*1024*5);
			if (!admin.tableExists(tableName)) {
				HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
				for (int i = 0; i < familys.length; i++) {
					tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
				}
				admin.createTable(tableDescriptor);
				System.out.println("create success");
			}
		    admin.flush(tableName);
			admin.close();
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
			table.close();
			System.out.println("add sucess!");
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
	 * 获取表相关的信息
	 * 
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	public static void getTable() throws MasterNotRunningException, ZooKeeperConnectionException, IOException
	{
		HbaseUtil.open();
		// 构建实列
		HBaseAdmin admin = new HBaseAdmin(conf);
		

		TableName[] tablenames = admin.listTableNames();
		for (TableName tableName : tablenames)
		{
			System.out.println(tableName);
		}
		// 指定表名称 获取表的描述信息
//		HTableDescriptor tabledesc = admin.getTableDescriptor(Bytes.toBytes("test-kit0718"));
//		System.out.println(tabledesc);
//		logger.debug("get table des success" + tabledesc);
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
	//delete table 
	public static void dropTable(String tableName) { 
        try { 
        	HbaseUtil.open();
        	
//            HBaseAdmin admin = new HBaseAdmin(conf); 
            admin.disableTable(tableName); 
            admin.deleteTable(tableName); 
            admin.close();
            System.out.println("delete success");
        } catch (MasterNotRunningException e) { 
            e.printStackTrace(); 
        } catch (ZooKeeperConnectionException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
 
    } 
	/**
	 * 以下为建表、插入数据、根据主键获取数据、根据查询条件获取数据四种常见的数据库操作
	 * 根据主键获取数据和根据查询条件获取数据可以把查询结果直接封装到一个bean方便使用，暂未实现
	 * 执行以下步骤前，请保证hbase和hdfs已经启动
	 */
	public static void main(String[] args) throws Exception, ZooKeeperConnectionException {
		
		//获取所有表的信息
//		getTable();
		//delete table
//		dropTable("Breaker");
//		create1();
		/** 1.建表，表名people，建立两个列族info和account */
		HbaseUtil.create2("Breaker", new String[]{"info"});	
		
//		Breaker system  Disconnector TMR_BM
		/** 2.插入数据，两条数据，数据结构如：zy,info:name=zhangyan,account:amount=10000;tb,info:name=tangbin,account:amount=1000000 */
//		info:Area System time readtime
		
//		Map map = new HashMap();
//		Map infomap = new HashMap();
//		infomap.put("Area", "广州中调".getBytes());
//		infomap.put("System", "DF8003E");
//		infomap.put("time", "1451439000");
//		infomap.put("readtime", "2015-12-30T09:30:00");
//		map.put("info", infomap);
//		HbaseUtil.add("kafka_system", "kafka1", map);
//		
//		
//		map = new HashMap();
//		infomap.put("Area", "广州中调1".getBytes());
//		infomap.put("System", "DF80103E");
//		infomap.put("time", "1452439000");
//		infomap.put("readtime", "2015-12-30T09:20:00");
//		map.put("info", infomap);
//		map.put("info", infomap);
//		HbaseUtil.add("kafka_system", "kafka2", map);
	}
}


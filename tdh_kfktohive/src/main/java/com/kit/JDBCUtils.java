package com.kit;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.sql.*;

import javax.security.auth.Subject;
import javax.sql.DataSource;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;


/**
 * Created by Pactera on 2016/10/20.
 */
public class JDBCUtils {
	public static String driver = "org.apache.hive.jdbc.HiveDriver";
	// public static String url ="jdbc:hive2://172.16.19.156:10000/default";
	private String url;
	private static Logger logger = Logger.getLogger(JDBCUtils.class);
	// 注册驱动

	// 获取连接
	public static Connection getConnection() throws IOException {
		try {
			// org.apache.hadoop.conf.Configuration conf = new
			// org.apache.hadoop.conf.Configuration();
			// conf.set("hadoop.security.authentication", "Kerberos");
			// UserGroupInformation.setConfiguration(conf);
			//
			// UserGroupInformation.loginUserFromKeytab("hive/kit-b1@TDH",
			// InJdbc.class.getClassLoader().getResource("hive.keytab")
			// .getPath());
			/*
			 * Hive2 JDBC URL with kerberos certification
			 */;
			// String testUrl =
			// "jdbc:hive2://172.16.19.154:10000/default;principal=hive/kit-b1@TDH";
			String testUrl = "jdbc:hive2://172.16.19.154:10000/default";
			// String testUrl =
			// "jdbc:hive2://172.16.19.151:10000/default;principal=hive/kit-b1@TDH;"+
			// "authentication=kerberos;"+
			// "kuser=hive/kit-b1@TDH;"+
			// "keytab="+InJdbc.class.getClassLoader().getResource("hive.keytab").getPath()+";"+
			// "krb5conf="+InJdbc.class.getClassLoader().getResource("krb5.conf").getPath()+"";
			System.out.println(testUrl);
			Class.forName(driver);
			
			return DriverManager.getConnection(testUrl, "kitdev", "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	static Connection getConnection(Subject signedOnUserSubject)
			throws Exception {
		Connection conn = (Connection) Subject.doAs(signedOnUserSubject,
				new PrivilegedExceptionAction<Object>() {
					public Object run() {
						Connection con = null;
						String JDBC_DB_URL = "jdbc:hive2://HiveHost:10000/default;"
								+ "principal=hive/localhost.localdomain@EXAMPLE.COM;"
								+ "kerberosAuthType=fromSubject";
						try {
							Class.forName(driver);
							con = DriverManager.getConnection(JDBC_DB_URL);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						return con;
					}
				});
		return conn;
	}

	// 释放资源
	public static void relese(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				st = null;
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}

	}

	void createTable() {
		Connection connection = null;
		Statement st = null;
		try {
			connection = DriverManager.getConnection(url, "", "");
			st = connection.createStatement();
			// String sql1 = "drop table if exists acidTable";
			String sql2 = "drop table if exists acidTable";
			// String sql3 = "drop table if exists partitionAcidTable";
			// String sql4 = "drop table if exists range_int_table";
			String sql5 = "create table acidTable(name varchar(10),age int, degree int) clustered by (age) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
			// String sql6 = "create table partitionAcidTable(name
			// varchar(10),age int) partitioned by (city string) clustered by
			// (age) into 10 buckets stored as orc
			// TBLProperties(\"transactional\"=\"true\")";
			// String sql7 = "create table range_int_table(id int, value int)
			// partitioned by range (id) (partition less1 values less than (1),
			// partition less10 values less than (10) ) clustered by (value)
			// into 3 buckets stored as orc
			// TBLProperties(\"transactional\"=\"true\")";
			// st.execute(sql1);
			st.execute(sql2);
			// st.execute(sql3);
			// st.execute(sql4);
			st.execute(sql5);
			// st.execute(sql6);
			// st.execute(sql7);

		} catch (SQLException e) {
			e.getMessage();
		} finally {
			closeConnectionAndStatement(connection, st);
		}

	}

	/**
	 * 关闭connection & statement
	 *
	 * @param connection
	 * @param st
	 */
	private static void closeConnectionAndStatement(Connection connection,
			Statement st) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println(e.getStackTrace());
			}
		}
		if (null != st) {
			try {
				st.close();
			} catch (SQLException e) {
				System.out.println(e.getStackTrace());
			}
		}
	}
	
	
	

	/**
	 * 单条插入 更改或者插入
	 */
	public static  void operation(String sql) {
		Connection conn = null;
		Statement st = null;
		try {
			conn =JDBCUtils.getConnection();
			st = conn.createStatement();
			st.execute(sql);
			logger.debug("jdbc operation success:"+sql);
		} catch (Exception e) {
			logger.debug("出错的sql为："+sql);
			logger.error(e.getStackTrace());
		} 
		finally {
			closeConnectionAndStatement(conn, st);
		}
	}
	
	

	/**
	 * 单条插入
	 */
	public static  void insert(String[] params) {
		String sql1 = "Insert into kfktohiveTable(topic, key,value) values ('"+params[0]+"', '"+params[1]+"','"+params[2]+"')";
		String sql3 = "set transaction.type=inceptor";
		Connection conn = null;
		Statement st = null;
		try {
			conn =JDBCUtils.getConnection();
			st = conn.createStatement();
			st.execute(sql3);
			st.execute(sql1);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} 
		finally {
			closeConnectionAndStatement(conn, st);
		}
	}
	

	/**
	 * 将acidTable表中age>15的记录的degree字段更新为degree*2
	 */
	public static  void update() {
		String sql1 = "set transaction.type=inceptor";
		String sql2 = "update acidTable set degree=degree*2 where age>15";
		Connection conn = null;
		Statement st = null;
		try {
			conn =JDBCUtils.getConnection();
			st = conn.createStatement();

			st.execute(sql1);
			st.execute(sql2);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * 删除acidTable表中name='bbb'的记录
	 */
	public static  void delete() {
		String sql1 = "set transaction.type=inceptor";
		String sql2 = "delete from acidTable where name='bbb'";
		Connection conn = null;
		Statement st = null;
		try {
			conn =JDBCUtils.getConnection();
			st = conn.createStatement();

			st.execute(sql1);
			st.execute(sql2);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}


	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data
		String sql = "select * from  kitdev.rule_cfg";
//		 try {
		 conn = JDBCUtils.getConnection();
		 // 创建运行环境
		 st = conn.createStatement();
		 // 运行HQL语句
		 rs = st.executeQuery(sql);
		 // 取出数据
		 while (rs.next()) {
		 String i = rs.getString("rule_sql");
		 String i1 = rs.getString("rule_name");
		 String i2= rs.getString("column_name");
 
		 System.out.println(i +"   "+i1+"  "+i2);
		 }
		// // st.execute("LOAD DATA LOCAL INPATH 'D:\\kv1.txt' INTO TABLE
		// // testHiveDriverTable");
		// // System.out.println("load data success!");
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// JDBCUtils.relese(conn, st, rs);
		// }
		// create table
//		try {
//			conn = JDBCUtils.getConnection();
//			st = conn.createStatement();
//			String sql2 = "drop table if exists kfktohiveTable";
//			String sql5 = "create table kfktohiveTable(topic varchar(20),key varchar(20),value value(20)) clustered by (key) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			st.execute(sql5);
//			System.out.println("create table success!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		 StringBuilder sb =new StringBuilder();
//		 sb.append("Batchinsert into  kfktohiveTable(topic, key,value) batchvalues( ");
//		 try {
//			conn = JDBCUtils.getConnection();
//			st = conn.createStatement();
//			 long startTime = System.currentTimeMillis();
//			for(int i=1;i<=100;i++){
////				String sql1 = "Insert into kfktohiveTable(topic, key,value) values ('topic"+i+"', 'key"+i+"','"+i+"_value')";
////				String sql3 = "set transaction.type=inceptor";
////				String sql2 = "Batchinsert into  kfktohiveTable(topic, key,value) batchvalues(values ('aaa1', 12,3), values ('bbb1', 22,9), values ('cc1', 22,9))";
////				String sql2 = "Batchinsert into  kfktohiveTable(topic, key,value) batchvalues(values ('aaa1', 12,3), values ('bbb1', 22,9), values ('cc1', 22,9))";
////				Batchinsert into  kfktohiveTable(topic, key,value) batchvalues( values ('topic1', 'key1','1_value') ,values ('topic2', 'key2','2_value') ,values ('topic2', 'key2','2_value') )
//				
//				if(i%100==0){
//					sb.append("values ('topic"+i+"', 'key"+i+"','"+i+"_value') )");
//				}else{
//					sb.append("values ('topic"+i+"', 'key"+i+"','"+i+"_value') ,");
//				}
////				sb.toString().substring(beginIndex, endIndex);
////				sb.toString().re
//				try {
//					
////					st.execute(sql3);
////					st.execute(sql1);
////					st.execute(sql2);
////					st.execute(sb.toString());
//					System.out.println("excute"+i+" success!");
//				} catch (Exception e) {
//					e.getStackTrace();
//				} 
//			}
//			st.execute(sb.toString());
////			System.out.println(sb.toString());
//			long endTime = System.currentTimeMillis();
//			System.out.println("插入1w行数据花费"+(endTime-startTime)+"毫秒");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}finally {
//            JDBCUtils.relese(conn,st,rs);
//        }
			
		

		
	}
}

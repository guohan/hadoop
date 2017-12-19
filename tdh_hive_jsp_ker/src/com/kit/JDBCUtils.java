package com.kit;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.sql.*;

import javax.security.auth.Subject;

import org.apache.hadoop.security.UserGroupInformation;


/**
 * Created by Pactera on 2016/10/20.
 */
public class JDBCUtils {
	public static String driver = "org.apache.hive.jdbc.HiveDriver";
	// public static String url ="jdbc:hive2://172.16.19.156:10000/default";
	private String url;
	// 注册驱动

	// 获取连接
	public static Connection getConnection() throws IOException {
		try {
			 org.apache.hadoop.conf.Configuration conf = new
			 org.apache.hadoop.conf.Configuration();
//			 conf.set("hadoop.security.authentication", "Kerberos");
//			 UserGroupInformation.setConfiguration(conf);
//			//
//			 UserGroupInformation.loginUserFromKeytab("hive/kit-b1@TDH",
//			 InJdbc.class.getClassLoader().getResource("hive.keytab")
//			 .getPath());
			 
//			  UserGroupInformation.setConfiguration(conf);
//			    //设置登录名 与密码
//			    UserGroupInformation.loginUserFromKeytab//("hdfs/kit-b1@TDH", WordCount.class.getClassLoader().getResource("hdfs.keytab").getPath());
//			    ("kitdev@TDH", InJdbc.class.getClassLoader().getResource("kitdev.keytab").getPath());
				
			/*
			 * Hive2 JDBC URL with kerberos certification
			 */;
			 String testUrl =
			 "jdbc:hive2://172.16.19.154:10000/kitdev";
//			String testUrl = "jdbc:hive2://172.16.19.154:10000/default;principal=kitdev@TDH";
//			 String testUrl =
//			 "jdbc:hive2://172.16.19.151:10000/default;principal=hive/kit-b1@TDH;"+
//			 "authentication=kerberos;"+
//			 "kuser=hive/kit-b1@TDH;"+
//			 "keytab="+InJdbc.class.getClassLoader().getResource("hive.keytab").getPath()+";"+
//			 "krb5conf="+InJdbc.class.getClassLoader().getResource("krb5.conf").getPath()+"";
			 
//			 String testUrl =
//					 "jdbc:hive2://kit-b4:10000/default;principal=hive/kit-b4@TDH;"+
//					 "authentication=kerberos;"+
//					 "kuser=kitdev@TDH;"+
//					 "keytab="+ InJdbc.class.getClassLoader().getResource("kitdev.keytab").getPath() +";"+
//					 "krb5conf="+InJdbc.class.getClassLoader().getResource("krb5.conf").getPath()+"";
			System.out.println(testUrl);
			Class.forName(driver);
//			return DriverManager.getConnection(testUrl, "kitdev", "kitdev#123");//默认root 不用写username
			return DriverManager.getConnection(testUrl, "hive", "hive123");
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
	private void closeConnectionAndStatement(Connection connection,
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
	 * 单条插入
	 */
	void insert() {
		String sql1 = "Insert into acidTable(name, age,degree) values ('aaa', 12,23)";
		String sql2 = "Insert into partitionAcidTable partition(city='sh') (name, age)values ('aaa', 12)";
		String sql3 = "set transaction.type=inceptor";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			st = conn.createStatement();
			st.execute(sql3);
			st.execute(sql1);
			st.execute(sql2);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// select data
		String sql = "select * from  ca_ctl_log";
		// try {
		// conn = JDBCUtils.getConnection();
		// // 创建运行环境
		// st = conn.createStatement();
		// // 运行HQL语句
		// rs = st.executeQuery(sql);
		// // 取出数据
		// while (rs.next()) {
		// String i = rs.getString("log_time");
		// System.out.println(i);
		// }
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
//			String sql2 = "drop table if exists acidTable";
//			String sql5 = "create table acidTable(name varchar(10),age int, degree int) clustered by (age) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			
//			String sql2 = "drop table if exists kfktohiveTable";
//			String sql5 = "create table kfktohiveTable(topic varchar(10),key varchar(10),value varchar(10)) clustered by (key) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			st.execute(sql2);
//			st.execute(sql5);
//			System.out.println("create table success!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String sql1 = "Insert into kfktohiveTable(topic, key,value) values ('topic12', '12','23_value')";
		String sql3 = "set transaction.type=inceptor";
		try {
			conn = JDBCUtils.getConnection();
			st = conn.createStatement();
			st.execute(sql3);
			st.execute(sql1);
			System.out.println("excutor success!");
		} catch (Exception e) {
			e.getStackTrace();
		} 
	}
}

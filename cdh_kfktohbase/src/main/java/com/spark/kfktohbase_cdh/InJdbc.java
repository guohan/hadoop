package com.spark.kfktohbase_cdh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

public class InJdbc {


	private static Log LOG = LogFactory.getLog(InJdbc.class);
	/** hive server2 driver */
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	/** hive server1 driver */
//	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	private String url;

	static {
		try {

			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
			System.exit(1);
		}
	}

	public InJdbc(String inceptorServerHost) {
    /*
     * hiveserver1 jdbc url
     */
//        this.url = "jdbc:transwarp://" + inceptorServerHost + ":10000/default";

     /*
      * Hive2 JDBC URL with kerberos certification
      */
//    this.url = "jdbc:hive2://" + inceptorServerHost + ":10000/default;principal=hive/idcisp-ftp-003@TDH;"+
//    "authentication=kerberos;"+
//    "kuser=hive/idcisp-ftp-003@TDH;"+
//    "keytab="+InJdbc.class.getClassLoader().getResource("hive.keytab").getPath()+";"+
//    "krb5conf="+InJdbc.class.getClassLoader().getResource("krb5.conf").getPath()+"";

        System.out.println(this.url);
//    "keytab="+HyperbaseBatchInsert.class.getClassLoader().getResource("transwarp/io/hyperbase/transwarp.io.hyperbase.BatchInsert/conf/hive.keytab").getPath()+";"+
//    "krb5conf="+HyperbaseBatchInsert.class.getClassLoader().getResource("transwarp/io/hyperbase/transwarp.io.hyperbase.BatchInsert/conf/krb5.conf").getPath()+"";

    /*
     * hiveserver2 JDBC URL with LDAP certification
     */
    this.url = "jdbc:hive2://" + inceptorServerHost + ":10000/default";

    /*
     * hiveserver2 jdbc url
     */
//   this.url = "jdbc:hive2://" + inceptorServerHost + ":10000/default";
	}

	/**
	 * 创建Demo程序需要使用到的表
	 */
	void createTable() {
		Connection connection = null;
		Statement st = null;
		try {
			connection = DriverManager.getConnection(url, "", "");
			st = connection.createStatement();
//			String sql1 = "drop table if exists acidTable";
			String sql2 = "drop table if exists acidTable";
//			String sql3 = "drop table if exists partitionAcidTable";
//			String sql4 = "drop table if exists range_int_table";
			String sql5 = "create table acidTable(name varchar(10),age int, degree int) clustered by (age) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			String sql6 = "create table partitionAcidTable(name varchar(10),age int) partitioned by (city string) clustered by (age) into 10 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			String sql7 = "create table range_int_table(id int, value int) partitioned by range (id) (partition less1 values less than (1), partition less10 values less than (10) ) clustered by (value) into 3 buckets stored as orc TBLProperties(\"transactional\"=\"true\")";
//			st.execute(sql1);
			st.execute(sql2);
//			st.execute(sql3);
//			st.execute(sql4);
			st.execute(sql5);
//			st.execute(sql6);
//			st.execute(sql7);

		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
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
	private void closeConnectionAndStatement(Connection connection, Statement st) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOG.error("connection close error", e);
			}
		}
		if (null != st) {
			try {
				st.close();
			} catch (SQLException e) {
				LOG.error("statement close error", e);
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
			LOG.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * 批量插入acidTable和partitionAcidTable
	 */
	void batchInsert() {
		String sql1 = "set transaction.type=inceptor";
		String sql2 = "Batchinsert into  acidTable(name, age,degree) batchvalues(values ('aaa', 12,3), values ('bbb', 22,9))";
		String sql3 = "Batchinsert into  partitionAcidTable partition(city='sh') (name, age) batchvalues(values ('aaa', 12), values ('bbb', 22))";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			st = conn.createStatement();

			st.execute(sql1);
			st.execute(sql2);
			st.execute(sql3);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * 将acidTable表中age>15的记录的degree字段更新为degree*2
	 */
	void update() {
		String sql1 = "set transaction.type=inceptor";
		String sql2 = "update acidTable set degree=degree*2 where age>15";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			st = conn.createStatement();

			st.execute(sql1);
			st.execute(sql2);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * 删除acidTable表中name='bbb'的记录
	 */
	void delete() {
		String sql1 = "set transaction.type=inceptor";
		String sql2 = "delete from acidTable where name='bbb'";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			st = conn.createStatement();

			st.execute(sql1);
			st.execute(sql2);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * 分页查询，查询第start到第end条记录
	 *
	 * @param start
	 *            从第start条开始遍历
	 * @param end
	 *            遍历到第end条结束
	 */
	private void scan(long start, long end) {
		String sql = "select name,degree  from acidtable order by degree desc limit "
				+ end + "; ";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			long i = 1;
			while (rs.next()) {
				if (i >= start) {
					String name = rs.getString("name");
					int degree = rs.getInt("degree");
                    LOG.info(name + "   " + degree);
				}
				i++;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeConnectionAndStatement(conn, st);
		}

	}

	public static void main(String[] args) throws SQLException {
		InJdbc hjd = new InJdbc("172.16.19.156");  //inceptorServerHost
		hjd.createTable();
//		hjd.batchInsert();
//		hjd.update();
//		hjd.delete();
//		hjd.scan(1, 3);
	}



}

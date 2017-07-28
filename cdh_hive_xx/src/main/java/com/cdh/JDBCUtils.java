package com.cdh;

import java.security.PrivilegedExceptionAction;
import java.sql.*;

import javax.security.auth.Subject;

/**
 * Created by Pactera on 2016/10/20.
 */
public class JDBCUtils {
    public static String driver ="org.apache.hive.jdbc.HiveDriver";
    public static String url ="jdbc:hive2://172.16.19.156:10000/default";

    //注册驱动


    //获取连接
    public static Connection getConnection(){
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url,"","");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    static Connection getConnection( Subject signedOnUserSubject ) throws Exception{
        Connection conn = (Connection) Subject.doAs(signedOnUserSubject, new PrivilegedExceptionAction<Object>()
            {
                public Object run()
                {
                        Connection con = null;
                        String JDBC_DB_URL = "jdbc:hive2://HiveHost:10000/default;"+
                                               "principal=hive/localhost.localdomain@EXAMPLE.COM;" +
                                               "kerberosAuthType=fromSubject";
                        try {
                                Class.forName(driver);
                                con =  DriverManager.getConnection(JDBC_DB_URL);
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

    //释放资源
    public static void relese(Connection conn, Statement st, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                rs=null;
            }
        }
        if(st!=null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                st=null;
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                conn=null;
            }
        }



    }

}

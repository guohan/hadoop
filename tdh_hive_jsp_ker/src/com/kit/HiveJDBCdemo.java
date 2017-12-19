package  com.kit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by GuoHan on 2016/10/20.
 */
public class HiveJDBCdemo {

    public static void main(String[] args) {
        Connection conn=null;
        Statement st =null;
        ResultSet rs = null;

        String sql = "select *from  kitdev.RULE_CHECK_RESULT_TODAY limit  10";
        try {
            conn = JDBCUtils.getConnection();
            //创建运行环境
            st = conn.createStatement();
            //运行HQL语句
          rs = st.executeQuery(sql);
            //取出数据
            while (rs.next()){
                String i = rs.getString("rule_id");
                System.out.println(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.relese(conn,st,rs);
        }
        
    }
}

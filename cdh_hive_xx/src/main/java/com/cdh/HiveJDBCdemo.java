package  com.cdh;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pactera on 2016/10/20.
 */
public class HiveJDBCdemo {

    public static void main(String[] args) {
        Connection conn=null;
        Statement st =null;
        ResultSet rs = null;

        String sql = "select * from digui";
        try {
            conn = JDBCUtils.getConnection();
            //创建运行环境
            st = conn.createStatement();
            //运行HQL语句
          rs = st.executeQuery(sql);
            //取出数据
            while (rs.next()){
                String i = rs.getString("id");
                System.out.println(i);
            }
//            st.execute("LOAD DATA LOCAL INPATH 'D:\\kv1.txt' INTO TABLE testHiveDriverTable");
			System.out.println("load data success!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.relese(conn,st,rs);
        }
        
    }
}

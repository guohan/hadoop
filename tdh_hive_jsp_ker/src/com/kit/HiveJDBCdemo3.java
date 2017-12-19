package  com.kit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by GuoHan on 2017/01/09.
 */
public class HiveJDBCdemo3 {

    public static void main(String[] args) {
        Connection conn=null;
        Statement st =null;
        ResultSet rs = null;

        String sql = "select * from   hbase_MREQ_PICKING_PROJ_INFO";
        try {
            conn = JDBCUtils.getConnection();
            //创建运行环境
            st = conn.createStatement();
            //运行HQL语句
          rs = st.executeQuery(sql);
            //取出数据
            while (rs.next()){
                String ROW_ID = rs.getString("ROW_ID");
                String MATERIAL_DIR_NAME=rs.getString("MATERIAL_DIR_NAME");
                System.out.println("ROW_ID"+ROW_ID+"MATERIAL_DIR_NAME"+MATERIAL_DIR_NAME);
            }
//            st.execute("LOAD DATA LOCAL INPATH 'D:\\kv1.txt' INTO TABLE testHiveDriverTable");
//			System.out.println("load data success!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.relese(conn,st,rs);
        }
        
    }
}

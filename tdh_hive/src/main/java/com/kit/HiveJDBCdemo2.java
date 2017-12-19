package  com.kit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoHan on 2016/10/20.
 */
public class HiveJDBCdemo2 {

    public static void main(String[] args) {
        Connection conn=null;
        Statement st =null;
        ResultSet rs = null;

        String sql = "select * from  hbase_MREQ_APP_STORE_MATL_IN_OUT";
        try {
        	List<String> list = new ArrayList();
            conn = JDBCUtils.getConnection();
            //创建运行环境
            st = conn.createStatement();
            //运行HQL语句
          rs = st.executeQuery(sql);
            //取出数据
            while (rs.next()){
            	  String ROW_ID = rs.getString("ROW_ID");
                String CLASSIFY_ID = rs.getString("CLASSIFY_ID");
                String CLASSIFY_NAME = rs.getString("CLASSIFY_NAME");
                String IN_AMT = rs.getString("IN_AMT");
                String IN_QTY = rs.getString("IN_QTY");
                String OUT_AMT = rs.getString("OUT_AMT");
                String OUT_QTY = rs.getString("OUT_QTY");
                String UNIT_NAME = rs.getString("UNIT_NAME");
                String YEAR_MTH_ID = rs.getString("YEAR_MTH_ID");
                System.out.println("CLASSIFY_ID:"+CLASSIFY_ID+"CLASSIFY_NAME: "+CLASSIFY_NAME+"ROW_ID:"+ROW_ID);
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

package com.kit;

import java.io.IOException;  
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;  
  

/** 
* @author GH
* @createtime 2016-12-26
* @description 
* 查找表的所有数据
* 获取所有数据 
*/ 

  
public class QuerySystemList extends HttpServlet{  
  
    
    private static final long serialVersionUID = 1L;  
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
         
        protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
                 
        }  

        
//        private void scan(long start, long end) {
//    		String sql = "select name,degree  from acidtable order by degree desc limit "
//    				+ end + "; ";
//    		Connection conn = null;
//    		Statement st = null;
//    		try {
//    			conn = DriverManager.getConnection(url, "", "");
//    			st = conn.createStatement();
//    			ResultSet rs = st.executeQuery(sql);
//    			long i = 1;
//    			while (rs.next()) {
//    				if (i >= start) {
//    					String name = rs.getString("name");
//    					int degree = rs.getInt("degree");
//                        LOG.info(name + "   " + degree);
//    				}
//    				i++;
//    			}
//    		} catch (Exception e) {
//    			LOG.error(e.getMessage(), e);
//    		} finally {
//    			closeConnectionAndStatement(conn, st);
//    		}
//
//    	}

        
        /**
         * 查询hbase_TB_KPI_SCORE_YW_SYSTEM的数据
         * 
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            // TODO Auto-generated method stub  
        	List<SystemBean> list = new ArrayList<>(); 
        	 long startTime = System.currentTimeMillis();
             try {  
            	  Connection conn=null;
                  Statement st =null;
                  ResultSet rs = null;
//                  String sql = "select * from hbase_TB_KPI_SCORE_YW_SYSTEM limit 10000";//13g
                  String sql = "select * from  tb_kpi_score_yw_system_bak limit 10000";//13g
                  
//                  http://localhost:8080/tdh_hive_jsp/QuerySystemList.do
                  try {
                      conn = JDBCUtils.getConnection();
                      //创建运行环境
                      st = conn.createStatement();
                      //运行HQL语句
                     
                    rs = st.executeQuery(sql);
                      //取出数据
                      while (rs.next()){
                    	  SystemBean bean = new SystemBean();
                    	  String ROW_ID = rs.getString("ROW_ID");
                          String BATCJ_ID=rs.getString("BATCJ_ID");
                          String CREATEDATE = rs.getString("CREATEDATE");
                          String QCOUNT=rs.getString("QCOUNT");
                          String RCOUNT = rs.getString("RCOUNT");
                          String RULECODE=rs.getString("RULECODE");
                          String SCOUNT = rs.getString("SCOUNT");
                          String SCORE=rs.getString("SCORE");
                          String SYS_ID = rs.getString("SYS_ID");
                          bean.setROW_ID(ROW_ID);
                         bean.setBATCJ_ID(BATCJ_ID);
                         bean.setCREATEDATE(CREATEDATE);
                         bean.setQCOUNT(QCOUNT);
                         bean.setRCOUNT(RCOUNT);
                         bean.setRULECODE(RULECODE);
                         bean.setSCORE(SCORE);
                         bean.setSCOUNT(SCOUNT);
                         bean.setSYS_ID(SYS_ID);
                          
                          
                          list.add(bean);
                      }
                  }catch (Exception e){
                      e.printStackTrace();
                  }finally {
                      JDBCUtils.relese(conn,st,rs);//释放资源
                  }
                  long endTime =System.currentTimeMillis();
                   request.setAttribute("list",list);
                   System.out.println("after select all record!");
                   System.out.println("查询数据花费"+(endTime-startTime)+"毫秒");
             } catch (Exception e) {  
                 e.printStackTrace();  
             }  
            request.getRequestDispatcher("/systemList.jsp").forward(request,response);
        }  
  
}  

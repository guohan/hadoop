package com.kit;

import java.io.IOException;  
import java.io.PrintWriter;
import java.sql.Connection;
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

  
public class QueryDataList extends HttpServlet {  
  
    
    private static final long serialVersionUID = 1L;  
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
         
        protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
                 
        }  

        
        /**
         * 查询hbase_MREQ_APP_STORE_MATL_IN_OUT的数据
         * 
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            // TODO Auto-generated method stub  
        	List<Bean> list = new ArrayList<>(); 
        	 long startTime = System.currentTimeMillis();
             try {  
            	  Connection conn=null;
                  Statement st =null;
                  ResultSet rs = null;
                  String sql = "select * from hbase_MREQ_APP_STORE_MATL_IN_OUT limit 10000";
                  try {
                      conn = JDBCUtils.getConnection();
                      //创建运行环境
                      st = conn.createStatement();
                      //运行HQL语句
                     
                    rs = st.executeQuery(sql);
                      //取出数据
                      while (rs.next()){
                    	  Bean bean = new Bean();
                    	  String ROW_ID = rs.getString("ROW_ID");
                          String CLASSIFY_ID = rs.getString("CLASSIFY_ID");
                          String CLASSIFY_NAME = rs.getString("CLASSIFY_NAME");
                          String IN_AMT = rs.getString("IN_AMT");
                          String IN_QTY = rs.getString("IN_QTY");
                          String OUT_AMT = rs.getString("OUT_AMT");
                          String OUT_QTY = rs.getString("OUT_QTY");
                          String UNIT_NAME = rs.getString("UNIT_NAME");
                          String YEAR_MTH_ID = rs.getString("YEAR_MTH_ID");
                          bean.setROW_ID(ROW_ID);
                          bean.setCLASSIFY_ID(CLASSIFY_ID);
                          bean.setIN_AMT(IN_AMT);
                          bean.setCLASSIFY_NAME(CLASSIFY_NAME);
                          bean.setIN_QTY(IN_QTY);
                          bean.setOUT_AMT(OUT_AMT);
                          bean.setOUT_QTY(OUT_QTY);
                          bean.setUNIT_NAME(UNIT_NAME);
                          bean.setYEAR_MTH_ID(YEAR_MTH_ID);
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
            request.getRequestDispatcher("/DataList.jsp").forward(request,response);
        }  
  
}  

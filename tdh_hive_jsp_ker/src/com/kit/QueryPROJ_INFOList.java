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

  
public class QueryPROJ_INFOList extends HttpServlet{  
  
    
    private static final long serialVersionUID = 1L;  
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
         
        protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
                 
        }  

        
        /**
         * 查询hbase_MREQ_PICKING_PROJ_INFO的数据
         * 
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            // TODO Auto-generated method stub  
        	List<ProjectInfoBean> list = new ArrayList<>(); 
        	 long startTime = System.currentTimeMillis();
             try {  
            	  Connection conn=null;
                  Statement st =null;
                  ResultSet rs = null;
                  String sql = "select * from hbase_MREQ_PICKING_PROJ_INFO";//839m
                  try {
                      conn = JDBCUtils.getConnection();
                      //创建运行环境
                      st = conn.createStatement();
                      //运行HQL语句
                     
                    rs = st.executeQuery(sql);
                      //取出数据
                      while (rs.next()){
                    	  ProjectInfoBean bean = new ProjectInfoBean();
                    	  String ROW_ID = rs.getString("ROW_ID");
                          String MATERIAL_DIR_NAME=rs.getString("MATERIAL_DIR_NAME");
                          String PROJ_CNT = rs.getString("PROJ_CNT");
                          bean.setROW_ID(ROW_ID);
                          bean.setMATERIAL_DIR_NAME(MATERIAL_DIR_NAME);
                          bean.setPROJ_CNT(PROJ_CNT);
                          
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
            request.getRequestDispatcher("/MREQ_PICKING_PROJ_INFO.jsp").forward(request,response);
        }  
  
}  

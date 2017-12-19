package com.kit;

import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.ArrayList;  
import java.util.List;  
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.hbase.HBaseConfiguration;  
import org.apache.hadoop.hbase.HColumnDescriptor;  
import org.apache.hadoop.hbase.HTableDescriptor;  
import org.apache.hadoop.hbase.KeyValue;  
import org.apache.hadoop.hbase.MasterNotRunningException;  
import org.apache.hadoop.hbase.ZooKeeperConnectionException;  
import org.apache.hadoop.hbase.client.Delete;  
import org.apache.hadoop.hbase.client.Get;  
import org.apache.hadoop.hbase.client.HBaseAdmin;  
import org.apache.hadoop.hbase.client.HTable;  
import org.apache.hadoop.hbase.client.Put;  
import org.apache.hadoop.hbase.client.Result;  
import org.apache.hadoop.hbase.client.ResultScanner;  
import org.apache.hadoop.hbase.client.Scan;  
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;  
  
/** 
 *  @author GH
 *  @createtime 2016-12-16
 *  @description 
 *  添加数据
 */ 
  
public class AddDataServlet extends HttpServlet{  
  
    
    private static final long serialVersionUID = 1L;  
     @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
         
     /**
      * 处理业务逻辑
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      *  (String tableName, String rowKey, String family, String qualifier, String value)  
      */
        protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            response.setContentType("text/html;charset=GBK");  
            // TODO Auto-generated method stub  
             try {  
            	   String tablename = "scores";  
                   String[] familys = {"grade", "course"};  
                   
                   HBaseAdmin admin = new HBaseAdmin(HbaseUtil.getConfiguration());  
                   System.out.println("before add data!");
//                 (String tableName, String rowKey, String family, String qualifier, String value)  
                 
                 HTable table = new HTable(HbaseUtil.getConfiguration(), tablename);  
                 Put put = new Put(Bytes.toBytes("zkb1"));  
//                 put.add(Bytes.toBytes("grade"),Bytes.toBytes(""),Bytes.toBytes("5"));  
                 put.add(Bytes.toBytes("course"),Bytes.toBytes("数学"),Bytes.toBytes("90"));  
//                 tablename,"zkb","course","","90"
                 table.put(put);  
                 System.out.println("after add data!");
             } catch (Exception e) {  
                 e.printStackTrace();  
             }  
              
        }  
        @Override  
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            // TODO Auto-generated method stub  
            doPost(request, response);  
        }  
  
}  

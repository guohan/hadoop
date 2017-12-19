package com.kit;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * 
 * @author GuoHan
 * @createtime 2016-12-29
 * @description
 * 删除表
 *
 */
public class DeleteTableServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3934476993506980051L;

	/**
	 * delete table
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=GBK");  
     	String tableName=req.getParameter("table");
		HBaseAdmin admin = new HBaseAdmin(HbaseUtil.getConfiguration());;
         admin.disableTable(tableName);//删除表前应当丢弃该表  
         admin.deleteTable(tableName);  
		// TODO Auto-generated method stub
		  req.getRequestDispatcher("/tableDataList.jsp").forward(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}

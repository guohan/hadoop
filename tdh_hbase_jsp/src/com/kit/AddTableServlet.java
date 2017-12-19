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
 * 添加表和列族
 *
 */
public class AddTableServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3934476993506980051L;

	/**
	 * 添加表和列族
	 * 请求url
	 * http://localhost:8080/tdh_hbase_jsp/addTable.do?table=tablename&cloumns=c1;c2;c3
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=GBK");  
     	String tableName=req.getParameter("table");
     	String cloumn=req.getParameter("cloumns");//获取列族名称 有分号隔开
     	//判断传参是否为空或者是否合法
     	if(null==tableName||null==cloumn){
     		req.getRequestDispatcher("/404.jsp").forward(req,resp);
  		  return ;
  	}
     	String[] familys=cloumn.split(";");
		HBaseAdmin hBaseAdmin = new HBaseAdmin(HbaseUtil.getConfiguration());;
		if (!hBaseAdmin.tableExists(tableName)) {
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDescriptor.addFamily(new HColumnDescriptor(familys[i]));
			}
			hBaseAdmin.createTable(tableDescriptor);
			System.out.println("create success");
		}
	    try {
			hBaseAdmin.flush(tableName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hBaseAdmin.close();
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

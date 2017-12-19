package com.kit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

/**
 * @author GH
 * @createtime 2017-01-03
 * @description
 * Servlet implementation class ShowTableList
 * 
 * request url:
 * http://localhost:8080/tdh_hbase_jsp/tableList.jsp
 */
@WebServlet("/ShowTableList")
public class ShowTableList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowTableList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 构建实列
		HBaseAdmin admin = new HBaseAdmin(HbaseUtil.getConfiguration());
		TableName[] tablenames = admin.listTableNames();
		List<String> list = new ArrayList<>();
		for (TableName tableName : tablenames)
		{
//			System.out.println(tableName);
			list.add(tableName.toString());
		}
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setAttribute("list",list);
        System.out.println("after select all record!");
		 request.getRequestDispatcher("/tableDataList.jsp").forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

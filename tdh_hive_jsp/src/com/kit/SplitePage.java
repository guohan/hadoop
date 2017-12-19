package com.kit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class SplitePage extends HttpServlet {
	private static int size =-1;
	
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * 对tb_kpi_score_yw_system_bak表的数据进行分页
	 * http://localhost:8080/tdh_hive_jsp/queryPageAll?cur=1
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//保存查询出来的数据
		List<TestBean> list = new ArrayList<>();
		long startTime = new Date().getTime();
		//当前查询页
		int curPage;
		String tmp = request.getParameter("cur");
		if (tmp == null)
			curPage = 1;
		else
			curPage = Integer.parseInt(tmp);

		if(curPage<1)curPage=1;
		request.setAttribute("cur", curPage);
		
		//每一页显示的行数
		int linePage = 20;
		//根据当前页和每页的行数来分页
		String sql= "select row_id,score from tb_kpi_score_yw_system_bak limit " 
				+ ((curPage-1)*linePage+1) + "," + linePage;
		System.out.println("sql :" + sql);
		try {
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtils.getConnection();
				// 创建运行环境
				st = conn.createStatement();
				//只需获取一次tb_kpi_score_yw_system_bak表的总行数
				if(size<0)
					size = getTotalPage(st);
				// 运行HQL语句
				rs = st.executeQuery(sql);
				//遍历rs的全部数据
				 while (rs.next()){
					 TestBean bean = new TestBean();
					 String key = rs.getString("row_id");
					 String value=rs.getString("score");
					 bean.setKey(key);
					 bean.setValue(value);
					 list.add(bean);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtils.relese(conn, st, rs);// 释放资源
			}

			request.setAttribute("list", list);
			//将总行数转换成总页数
			request.setAttribute("size", (int)Math.ceil(1.0*size/linePage));
			System.out.println("size :"+size);
			System.out.println("after select all record!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = new Date().getTime();
		System.out.println("spend time: " + (endTime - startTime));
		
		request.getRequestDispatcher("/splitepage.jsp").forward(request, response);
	}
	
	//获取tb_kpi_score_yw_system_bak表的总行数
	private int getTotalPage(Statement st) {
		System.out.println("Running method getTotalPage()-------");
		String sql = "select count(*) size from tb_kpi_score_yw_system_bak";
		try {
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			int size = rs.getInt(1);
			rs.close();
			return size;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}

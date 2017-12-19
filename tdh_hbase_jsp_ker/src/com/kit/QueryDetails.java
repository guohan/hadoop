package com.kit;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
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
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;


/**
 * @author GH
 * @createtime 2016-12-26
 * @description 查找表的所有数据 请求url:
 *              http://localhost:8080/tdh_hbase_jsp/queryAll.do?table=KITDEV:
 *              MREQ_APP_STORE_MATL_IN_OUT
 *              http://192.168.191.1:8080/tdh_hbase_jsp_ker/queryAll.do?table=
 *              KITDEV:MREQ_APP_STORE_MATL_IN_OUT
 * 
 *              kitdev:FW_KFGDXX kitdev:WFRT_TASK_EXEC_INFO
 * 
 */

public class QueryDetails extends HttpServlet {

	private static final long serialVersionUID = 1L;
	PrintWriter writer = null;
	public static Configuration conf = null;
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * 根据参数table 查询表的记录
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String wid = request.getParameter("workorder_id");
			String tid = request.getParameter("task_instance_id");
			if(null==wid||null==tid){
      		  request.getRequestDispatcher("/404.jsp").forward(request,response);
      		  return ;
      	}
			int stop=Integer.parseInt(tid)+1;
			String stoprow=String.valueOf(stop)+"_";
			String tablename = "kitdev:FW_KFGDXX";
			HTable table = new HTable(HbaseUtil.getConfiguration(), tablename);
			Get get = new Get(wid.getBytes()); //6466068
//			 get 'kitdev:WFRT_TASK_EXEC_INFO','172354209_1'
			Result result = table.get(get);
			String orderflow = "kitdev:WFRT_TASK_EXEC_INFO";
			HTable tableFlow = new HTable(HbaseUtil.getConfiguration(), orderflow);
//			Get getFlow = new Get("172354209".getBytes());
//			Result resultFlow = tableFlow.get(getFlow);
			
			
			
//			Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryPrefixComparator("172354209".getBytes()));
			  Scan scan = new Scan();  
//			  scan.set
//			  scan.set
//			  scan.setStartRow("172354209_".getBytes());
//			  scan.setStopRow("172354210_".getBytes());
			  scan.setStartRow(Bytes.toBytes(tid+"_"));
			  scan.setStopRow(Bytes.toBytes(stoprow));
//			  scan.setStartRow("5193:#");
//
//			  scan.setStopRow("5193::");
//			   FilterList filterList1 = new FilterList(filter); 
//             s.setFilter(filterList1); 
           
			
			long begin = System.currentTimeMillis();
			List<WorkorderDetail> list = new ArrayList<WorkorderDetail>();
			List<WorkorderFlow> flowlist = new ArrayList<WorkorderFlow>();
			// List<Bean> list = new ArrayList();

			Map<String, List<Bean>> map = new HashMap<String, List<Bean>>();// 只存放一条数据
			// List<String,Map<String,String>> listData = new ArrayList();
			Map<String, Map<String, String>> mapData = new HashMap<String, Map<String, String>>();
			// for(Result r:result){
//			StringBuffer sbkey = new StringBuffer();
//			StringBuffer sbvalue = new StringBuffer();
//			String rowkey = null;
			Map<String, String> data1 = new HashMap<String, String>();
			Map<String, String> data2 = new HashMap<String, String>();
			// 通过遍历cell取列与值的信息
			WorkorderDetail details = new WorkorderDetail();
			
			for (Cell cell : result.rawCells()) {
				String value = Bytes.toString(CellUtil.cloneValue(cell));
				String Qualifier = Bytes
						.toString(CellUtil.cloneQualifier(cell));
				String family = Bytes.toString(CellUtil.cloneFamily(cell));
				String row = Bytes.toString(CellUtil.cloneRow(cell));
				System.out.println("row" + row + "Qualifier" + Qualifier
						+ "family" + family + "value" + value);
				data1.put(Qualifier, value);
			}
			
			  ResultScanner ss = tableFlow.getScanner(scan); 
			  int count=0;
	             for(Result r:ss){  
	            	 WorkorderFlow flow = new WorkorderFlow();
	            	 count++;
	                 //通过遍历cell取列与值的信息
	                 for(Cell cell : r.rawCells()){  
	              	   String value=Bytes.toString(CellUtil.cloneValue(cell));
	              	   String Qualifier=Bytes.toString(CellUtil.cloneQualifier(cell));
	              	   String family=Bytes.toString(CellUtil.cloneFamily(cell));
	              	   String row=Bytes.toString(CellUtil.cloneRow(cell));
	              	   System.out.println("row"+row+"Qualifier:  "+Qualifier+" family"+family+"value"+value);
	              	 data2.put(Qualifier, value);
	              	 data2.put("TASK_ID",String.valueOf(count));
	                 }
	                 
	                 

	     			// 流程表模块
//	     			String instance_id = data2.get("instance_id");
	     			String task_reach_time = data2.get("TASK_REACH_TIME");
	     			String task_commit_time = data2.get("TASK_COMMIT_TIME");
	     			String tache_name = data2.get("TACHE_NAME");
	     			String last_commitor = data2.get("LAST_COMMITOR");
	     			String task_id = data2.get("TASK_ID");
	     			String cost_time = data2.get("COST_TIME");
//	     			flow.setInstance_id(instance_id);
	     			flow.setLast_commitor(last_commitor);
	     			flow.setTache_name(tache_name);
	     			flow.setTask_commit_time(task_commit_time);
	     			flow.setTask_reach_time(task_reach_time);
	     			flow.setTask_id(task_id);
	     			flow.setCost_time(DateTimeUtil.getMin(task_commit_time, task_reach_time));
//	     			flow.setTask_id(String.valueOf((Integer.parseInt(task_id)) + 1));
//	     			if (cost_time == null || cost_time.equals("null")) {
//	     				flow.setCost_time("");
//	     			} else {
//	     				flow.setCost_time(
//	     						(Integer.valueOf(cost_time)).toString() + "分钟");
//	     			}
	     			flowlist.add(flow);
	                 
	             }

			String LDNR = data1.get("LDNR");
			String HJFS = data1.get("HJFS");
			String ZHDFPJDM = data1.get("ZHDFPJDM");
			String SCDFPJDM = data1.get("SCDFPJDM");
			String SLRBS = data1.get("SLRBS");
			String ZZBM = data1.get("ZZBM");
			String NMBZ = data1.get("NMBZ");
			String CFFYBZ = data1.get("CFFYBZ");
			String KFGZDBS = data1.get("KFGZDBS");
			String YWLBDM = data1.get("YWLBDM");
			String LDHM = data1.get("LDHM");
			String ZXGH = data1.get("ZXGH");
			String SLSJ = data1.get("SLSJ");
			String YHDZ = data1.get("YHDZ");
			String GZDZTDM = data1.get("GZDZTDM");
			String HFBZ = data1.get("HFBZ");
			String GDRBS = data1.get("GDRBS");
			String GDSJ = data1.get("GDSJ");
			String JJCDDM = data1.get("JJCDDM");
			String ZJCLBZ = data1.get("ZJCLBZ");
			details.setLDNR(LDNR);
			details.setHJFS(HJFS);

			if (ZHDFPJDM.equals("1")) {
				details.setZHDFPJDM("非常满意");
			} else {
				details.setZHDFPJDM("满意");
			}

			if (SCDFPJDM.equals("1")) {
				details.setSCDFPJDM("非常满意");
			} else {
				details.setSCDFPJDM("满意");
			}

			details.setSLRBS(SLRBS);
			details.setZZBM(ZZBM);

			details.setNMBZ(NMBZ);

			if (CFFYBZ.equals("1")) {
				details.setCFFYBZ("是");
			} else {
				details.setCFFYBZ("否");
			}

			details.setKFGZDBS(KFGZDBS);
			if (YWLBDM.equals("1")) {
				details.setYWLBDM("报修");
			} else if (YWLBDM.equals("2")) {
				details.setYWLBDM("报装");
			} else if (YWLBDM.equals("3")) {
				details.setYWLBDM("咨询");
			} else if (YWLBDM.equals("4")) {
				details.setYWLBDM("查询");
			} else if (YWLBDM.equals("5")) {
				details.setYWLBDM("投诉");
			} else if (YWLBDM.equals("6")) {
				details.setYWLBDM("举报");
			} else if (YWLBDM.equals("7")) {
				details.setYWLBDM("表扬");
			} else if (YWLBDM.equals("8")) {
				details.setYWLBDM("建议");
			} else if (YWLBDM.equals("9")) {
				details.setYWLBDM("意见");
			} else if (YWLBDM.equals("10")) {
				details.setYWLBDM("订阅信息");
			} else {
				details.setYWLBDM("其它");
			}

			details.setLDHM(LDHM);
			details.setZXGH(ZXGH);
			details.setSLSJ(SLSJ);
			details.setYHDZ(YHDZ);

			if ("0".equals(GZDZTDM)) {
				details.setHFBZ("作废");
			} else if ("1".equals(GZDZTDM)) {
				details.setHFBZ("受理");
			} else if ("2".equals(GZDZTDM)) {
				details.setHFBZ("处理");
			} else {
				details.setHFBZ("归档");
			}

			;// ;工作单状态 如、、、
			if ("1".equals(HFBZ)) {
				details.setHFBZ("是");
			} else {
				details.setHFBZ("否");
			}

			details.setGDRBS(GDRBS);
			details.setGDSJ(GDSJ);

			if ("0".equals(JJCDDM)) {
				details.setJJCDDM("普通");
			} else if ("1".equals(JJCDDM)) {
				details.setJJCDDM("越级");
			} else if ("2".equals(JJCDDM)) {
				details.setJJCDDM("重要");
			} else {
				details.setJJCDDM("重大");
			}

			details.setZJCLBZ(ZJCLBZ);
			details.setGDLY("95588");
			details.setFWLY("人工电话");
			details.setYYS("9天");
			details.setKHXM("");
			details.setYXFW("");
			details.setCLYJ("");
			list.add(details);

			// }

			request.setAttribute("list", list);

			request.setAttribute("flowlist", flowlist);
			table.close();
			tableFlow.close();
			long end = System.currentTimeMillis();
			System.out.println("查询消耗时间为：" + (end - begin));
			System.out.println("after select all record!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/DataList2.jsp").forward(request,
				response);
	}

}

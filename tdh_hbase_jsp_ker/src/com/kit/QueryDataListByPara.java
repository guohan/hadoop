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
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;  
  

/** 
* @author GH
* @createtime 2016-12-26
* @description 
* 查找表的所有数据 根据条件过滤
* 请求url:
         * http://localhost:8080/tdh_hbase_jsp/queryAll.do?table=table1
*/ 

  
public class QueryDataListByPara extends HttpServlet{  
  
    
    private static final long serialVersionUID = 1L;  
     PrintWriter  writer  = null;  
     public static Configuration conf = null;  
     @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
         
        protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
                 
        }  
        public static String scanLimitData(String tableName){

    		try {
    			HTable table = new HTable(conf, tableName);
    			List<Map<String,String>> list =new ArrayList<>();
    			Scan scan = new Scan();
//    			scan.setStartRow(Bytes.toBytes(startRowkey));
//    			scan.setStartRow(Bytes.toBytes(endRowkey));
    			ResultScanner resultScanner = table.getScanner(scan);

//    			JSONObject hresult=new JSONObject();
    			Iterator<Result> res = resultScanner.iterator();
    			while (res.hasNext()) {
    				Result result = res.next();
    				Map<String,String> keyvalue=new HashMap<>();
    				for(KeyValue kv:result.list()){
    					keyvalue.put(Bytes.toString(kv.getQualifier()),Bytes.toString(kv.getValue()));
    				}
//    				hresult.accumulate(Bytes.toString(result.getRow()), keyvalue);
    			}
    			return "".toString();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return "表连接失败或表不存在！！！";
    		}
    	}

        
        
        /**
         * 
         * @param tableName
         * 条件过滤
         */
        public static void QueryByCondition3(String tableName) { 
        	 
            try { 
            	 HTable table = new HTable(HbaseUtil.getConfiguration(), tableName);
     
                List<Filter> filters = new ArrayList<Filter>(); 
     
                Filter filter1 = new SingleColumnValueFilter(Bytes 
                        .toBytes("column1"), null, CompareOp.EQUAL, Bytes 
                        .toBytes("aaa")); 
                filters.add(filter1); 
     
                Filter filter2 = new SingleColumnValueFilter(Bytes 
                        .toBytes("column2"), null, CompareOp.EQUAL, Bytes 
                        .toBytes("bbb")); 
                filters.add(filter2); 
     
                Filter filter3 = new SingleColumnValueFilter(Bytes 
                        .toBytes("column3"), null, CompareOp.EQUAL, Bytes 
                        .toBytes("ccc")); 
                filters.add(filter3); 
     
                FilterList filterList1 = new FilterList(filters); 
     
                Scan scan = new Scan(); 
                scan.setFilter(filterList1); 
                ResultScanner rs = table.getScanner(scan); 
                for (Result r : rs) { 
                    System.out.println("获得到rowkey:" + new String(r.getRow())); 
                    for (KeyValue keyValue : r.raw()) { 
                        System.out.println("列：" + new String(keyValue.getFamily()) 
                                + "====值:" + new String(keyValue.getValue())); 
                    } 
                } 
                rs.close(); 
     
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
     
        } 
        /**
         * 根据参数table 查询表的记录
         * 
         */
        @SuppressWarnings("deprecation")
		@Override  
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            // TODO Auto-generated method stub  
//            doPost(request, response);  
//        	 response.setContentType("text/html;charset=GBK");  
        	String tablename=request.getParameter("table");
//             writer = response.getWriter();  
            // TODO Auto-generated method stub  
             try {  
//            	   String tablename = "KITDEV:MREQ_APP_STORE_MATL_IN_OUT";  
//            	 KITDEV:MREQ_STORE_MATL_IN_OUT_DETL
//                   String[] familys = {"grade", "course"};  
            	 
            	 HTable table = new HTable(HbaseUtil.getConfiguration(), tablename);
                 
                 List<Filter> filters = new ArrayList<Filter>(); 
      
//                 cl:MATERIAL_DIR_NAME 
                 Filter filter = new SingleColumnValueFilter(Bytes.toBytes("cl"), Bytes.toBytes("MATERIAL_DIR_NAME"),CompareOp.EQUAL, Bytes.toBytes("10kV 冷缩式电缆附件")); // 当列column1的值为aaa时进行查询 
                 filters.add(filter); 
//                 
//                 Filter filter1 = new SingleColumnValueFilter(Bytes 
//                         .toBytes("MATERIAL_DIR_NAME"), null, CompareOp.EQUAL, Bytes 
//                         .toBytes("10kV 冷缩式电缆附件")); 
//                 filters.add(filter1); 
      
                 Filter filter2 = new SingleColumnValueFilter(Bytes 
                         .toBytes("cl"), Bytes.toBytes("PROJ_CNT"), CompareOp.EQUAL, Bytes 
                         .toBytes("5")); 
                 filters.add(filter2); 
                 
                 //正则表达式过滤
                 RegexStringComparator comp = new RegexStringComparator("you."); // 以 you 开头的字符串
                 SingleColumnValueFilter filter3 = new SingleColumnValueFilter(Bytes.toBytes("family"), Bytes.toBytes("qualifier"), CompareOp.EQUAL, comp);
//                 filters.add(filter3);
      
                 //直接输出值为多少的列与值
                 Filter filter11 = new ValueFilter(CompareOp.EQUAL,                   
                	        new SubstringComparator("10kV 冷缩式电缆附件"));                                            
                 filters.add(filter11); 
                 
                 Filter filtertime = new TimestampsFilter(null);
                 
                                                                      

//                 Rowkey中包括某些字符串                   字符串存在list a内       
//               Filter filter2 = new RowFilter(CompareOp.EQUAL,		       
                 // new RegexStringComparator(".*"+maclist.get(a)+"$"));		
//            	Filter filter2 = new RowFilter(CompareOp.EQUAL,		       
//            	   new SubstringComparator(maclist.get(a)));		   
//            									
//            	filters.add(filter2);					        
//            	FilterList filterList123 = new FilterList(Operator.MUST_PASS_ONE,filters);  
//            	  filters.add(filterList123); 
                 
                 Filter filter4 = new SingleColumnValueFilter(Bytes 
                         .toBytes("cl"), null, CompareOp.EQUAL, Bytes 
                         .toBytes("ccc")); 
                 filters.add(filter4); 
      
                 FilterList filterList1 = new FilterList(filters); 
      
                
            	 
            	 
                   
                   System.out.println("before select all record!");
                   Scan s = new Scan();  
                   s.setFilter(filterList1); 
                   ResultScanner ss = table.getScanner(s);  
                   List<Bean> list = new ArrayList();
//                   System.out.println(ss.);
                   Map<String, List<Bean>> map= new HashMap<String, List<Bean>>();//只存放一条数据
                
//                   List<String,Map<String,String>> listData = new ArrayList();
                   Map<String, Map<String,String>> mapData= new HashMap<String, Map<String,String>>();
                   for(Result r:ss){  
                	   StringBuffer sbkey=new StringBuffer();
                	   StringBuffer sbvalue=new StringBuffer();
                	   String rowkey=null;
                       Map<String,String> data1= new HashMap<String, String>();
                       //通过遍历KeyValue取列与值的信息
                       for(KeyValue kv : r.raw()){  
                    	   if(data1.size()<1){
                    		   sbkey.append(new String(kv.getQualifier())+"|");
                        	   sbvalue.append(new String(kv.getValue())+"|"); 
                    	   }
                    	   
                    	   if(mapData.containsKey(new String(kv.getRow()))){
                    		   sbkey.append(new String(kv.getQualifier())+"|");
                        	   sbvalue.append(new String(kv.getValue()) +"|");
                        	   mapData.remove(new String(kv.getRow()));
                        	  
                    	   }
//                    	   System.out.println(new String(kv.getFamily()));
                    	    rowkey=new String(kv.getRow());
                    	   data1.put(sbkey.toString(), sbvalue.toString());
                    	   mapData.put(new String(kv.getRow()),data1 );
                    	 
                    	  
                       }  
                       Bean bean = new Bean();
                	   bean.setRowkey(rowkey);
       	        	   bean.setQualifier(sbkey.toString());
       	        	   bean.setValue(sbvalue.toString());
       	        	   list.add(bean);
                      
                   }   
                  
                   request.setAttribute("list",list);
                   System.out.println("after select all record!");
             } catch (Exception e) {  
                 e.printStackTrace();  
             }  
            request.getRequestDispatcher("/DataList.jsp").forward(request,response);
        }  
  
}  

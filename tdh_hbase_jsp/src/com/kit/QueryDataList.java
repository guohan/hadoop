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
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;  
  

/** 
* @author GH
* @createtime 2016-12-26
* @description 
* 查找表的所有数据
* 请求url:
         * http://localhost:8080/tdh_hbase_jsp/queryAll.do?table=table1
         * http://localhost:8080/tdh_hbase_jsp/queryAll.do?table=KITDEV:MREQ_APP_STORE_MATL_IN_OUT
         * 
         * KITDEV:MREQ_PICKING_PROJ_INFO column=cl:MATERIAL_DIR_NAME 
*/ 

  
public class QueryDataList extends HttpServlet{  
  
    
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
        //	判断url 是否传值
        	String tablename=request.getParameter("table");
        	if(null==tablename){
        		  request.getRequestDispatcher("/404.jsp").forward(request,response);
        		  return ;
        	}
             try {  
//            	   String tablename = "KITDEV:MREQ_APP_STORE_MATL_IN_OUT";  
//            	 KITDEV:MREQ_STORE_MATL_IN_OUT_DETL
//                   String[] familys = {"grade", "course"};  
//            	 List<Filter> filters = new ArrayList<Filter>(); 
//            	  Filter filter11 = new ValueFilter(CompareOp.EQUAL,                   
//              	        new SubstringComparator("10kV 冷缩式电缆附件"));                                            
//               filters.add(filter11); 
//                   
//               Filter filter = new SingleColumnValueFilter(Bytes.toBytes("cl"), Bytes.toBytes("RULECODE"),CompareOp.EQUAL, Bytes.toBytes("10kV 冷缩式电缆附件")); // 当列column1的值为aaa时进行查询 
//               filters.add(filter); 
               
//               Filter filter1 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator("row010".getBytes()));
//            	 Filter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator("1000605877".getBytes()));
//            	 Filter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator("1000101804:201312".getBytes()));
//            	 filters.add(filter2); 
            	 
                   HTable table = new HTable(HbaseUtil.getConfiguration(), tablename);  
//                   table.setAutoFlush(false);//设置缓冲区
//                   table.flushCommits();//刷写 读取数据的时候用到
//                   table.setWriteBufferSize(20971520);//2mb
//                   table.setScannerCaching(1000);
                   System.out.println("before select all record!");
                   Scan s = new Scan();  
//                   s.setCaching(1000);//每次从服务器端读取的行数（影响 RPC）；
//                   s.setBatch(0);//指定最多返回的 Cell 数目。用于防止一行中有过多的数据，导致 OutofMemory 错误，默认无限制。
//                   s.add
//                   s.addFamily(Bytes.toBytes("cl"));
//                   s.addColumn(Bytes.toBytes("cl"), Bytes.toBytes("MATERIAL_DIR_NAME"));
//                   s.addColumn(Bytes.toBytes("cl"), Bytes.toBytes("RULECODE"));
//                   s.addColumn(Bytes.toBytes("cl"), Bytes.toBytes("CLASSIFY_NAME"));
//                   s.addColumn(Bytes.toBytes("cl"), Bytes.toBytes("MATERIAL_DIR_NAME"));
                   
//                   KITDEV:MREQ_PICKING_PROJ_INFO column=cl:MATERIAL_DIR_NAME 我在这个字段上建索引了

                   
//                   Get get = new Get("1000605877".getBytes()); // 根据主键查询
//                   Result r = table.get(get);

                   ResultScanner ss = table.getScanner(s);  
//                   ResultScanner ss =  table.get(get);
//                   Result ss1= table.get(get);
                   long begin = System.currentTimeMillis();
                   List<Bean> list = new ArrayList();
//                   FilterList filterList1 = new FilterList(filters); 
//                   s.setFilter(filterList1); 
                   
                   Map<String, List<Bean>> map= new HashMap<String, List<Bean>>();//只存放一条数据
                
//                   List<String,Map<String,String>> listData = new ArrayList();
                   Map<String, Map<String,String>> mapData= new HashMap<String, Map<String,String>>();
                   
                   for(Result r:ss){  
                	   StringBuffer sbkey=new StringBuffer();
                	   StringBuffer sbvalue=new StringBuffer();
                	   String rowkey=null;
                       Map<String,String> data1= new HashMap<String, String>();
                       //通过遍历cell取列与值的信息
//                       for(Cell cell : r.rawCells()){  
//                    	   String value=Bytes.toString(CellUtil.cloneValue(cell));
//                    	   String Qualifier=Bytes.toString(CellUtil.cloneQualifier(cell));
//                    	   String family=Bytes.toString(CellUtil.cloneFamily(cell));
//                    	   String row=Bytes.toString(CellUtil.cloneRow(cell));
//                    	   System.out.println("row"+row+"Qualifier"+Qualifier+"family"+family+"value"+value);
//                    	   
//                       }
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
                   ss.close();
                   long end = System.currentTimeMillis();
                   System.out.println("查询消耗时间为："+(end-begin));
                   System.out.println("after select all record!");
             } catch (Exception e) {  
                 e.printStackTrace();  
             }  
            request.getRequestDispatcher("/DataList.jsp").forward(request,response);
        }  
  
}  

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
 *  hbase 数据展示
 */ 
public class HbaseServlet extends HttpServlet{  
   
    private static final long serialVersionUID = 1L;  
     PrintWriter  writer  = null;  
     private static Configuration conf = null;  
     @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    
    }
        /** 
         * 初始化配置 
         */  
//        static {  
//           
//        }  
        /**
         * 创建表
         * @param tableName
         * @param familys
         * @throws Exception
         */
        public void creatTable(String tableName, String[] familys) throws Exception {  
            HBaseAdmin admin = new HBaseAdmin(conf);  
            if (admin.tableExists(tableName)) {  
                System.out.println(writer==null);  
                writer.println("table already exists!");  
                writer.println("<br>");  
            } else {  
                HTableDescriptor tableDesc = new HTableDescriptor(tableName);  
                for(int i=0; i<familys.length; i++){  
                    tableDesc.addFamily(new HColumnDescriptor(familys[i]));  
                }  
                admin.createTable(tableDesc);  
                writer.println("create table " + tableName + " ok.");  
                writer.println("<br>");  
            }   
        }  
          
        /** 
         * 删除表 
         */  
        public  void deleteTable(String tableName) throws Exception {  
           try {  
               HBaseAdmin admin = new HBaseAdmin(conf);  
               admin.disableTable(tableName);//删除表前应当丢弃该表  
               admin.deleteTable(tableName);  
               writer.println("delete table " + tableName + " ok.");  
               writer.println("<br>");  
           } catch (MasterNotRunningException e) {  
               e.printStackTrace();  
           } catch (ZooKeeperConnectionException e) {  
               e.printStackTrace();  
           }  
        }  
           
        /** 
         * 插入一行记录 
         */  
        public  void addRecord (String tableName, String rowKey, String family, String qualifier, String value)  
                throws Exception{  
            try {  
                HTable table = new HTable(conf, tableName);  
                Put put = new Put(Bytes.toBytes(rowKey));  
                put.add(Bytes.toBytes(family),Bytes.toBytes(qualifier),Bytes.toBytes(value));  
                table.put(put);  
                writer.println("insert recored " + rowKey + " to table " + tableName +" ok.");  
                writer.println("<br>");  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
       
        /** 
         * 删除一行记录 
         */  
        public  void delRecord (String tableName, String rowKey) throws IOException{  
            HTable table = new HTable(conf, tableName);  
            List list = new ArrayList();  
            Delete del = new Delete(rowKey.getBytes());  
            list.add(del);  
            table.delete(list);  
            writer.println("del recored " + rowKey + " ok.");  
            writer.println("<br>");  
        }  
           
        /** 
         * 查找一行记录 
         */  
        public  void getOneRecord (String tableName, String rowKey) throws IOException{  
            HTable table = new HTable(conf, tableName);  
            Get get = new Get(rowKey.getBytes());  
            Result rs = table.get(get);  
            for(KeyValue kv : rs.raw()){  
                writer.println(new String(kv.getRow()) + " " );  
                writer.println("<br>");  
               writer.println(new String(kv.getFamily()) + ":" );  
               writer.println("<br>");  
               writer.println(new String(kv.getQualifier()) + " " );  
               writer.println("<br>");  
               writer.println(kv.getTimestamp() + " " );  
               writer.println("<br>");  
                writer.println(new String(kv.getValue()));  
                writer.println("<br>");  
            }  
        }  
           
        /** 
         * 显示所有数据 
         */  
        public  void getAllRecord (String tableName) {  
            try{  
                 HTable table = new HTable(conf, tableName);  
                 Scan s = new Scan();  
                 ResultScanner ss = table.getScanner(s);  
                 for(Result r:ss){  
                     for(KeyValue kv : r.raw()){  
                           
                       writer.println(new String(kv.getRow()) + " ");  
                       writer.println("<br>");  
                       writer.println(new String(kv.getFamily()) + ":");  
                       writer.println("<br>");  
                       writer.println(new String(kv.getQualifier()) + " ");  
                       writer.println("<br>");  
                       writer.println(kv.getTimestamp() + " ");  
                       writer.println("<br>");  
                       writer.println(new String(kv.getValue()));  
                       writer.println("<br>");  
                     }  
                 }  
            } catch (IOException e){  
                e.printStackTrace();  
            }  
        }  
         
        @SuppressWarnings({"resource", "deprecation"})
		protected void doPost(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
            response.setContentType("text/html;charset=GBK");  
             writer = response.getWriter();  
            // TODO Auto-generated method stub  
             
        	 Configuration HBASE_CONFIG = new Configuration();
             Configuration conf = HBaseConfiguration.create(HBASE_CONFIG);
             conf = HBaseConfiguration.create();
             // 这个hbase.keytab也是从远程服务器上copy下来的, 里面存储的是密码相关信息
             // 这样我们就不需要交互式输入密码了
             conf.addResource(HbaseServlet.class.getClassLoader().getResource("core-site.xml"));
      	    conf.addResource(HbaseServlet.class.getClassLoader().getResource("hbase-site.xml"));
      	    conf.addResource(HbaseServlet.class.getClassLoader().getResource("hdfs-site.xml"));
//             conf.set("keytab.file" , "D:/linux/cdh_kit/kerberos/156/hbase.keytab");
      	    conf.set("keytab.file" , HbaseServlet.class.getClassLoader().getResource("hbase.keytab").getPath());
             conf.set("hbase.master", "172.16.19.156:60000");
             conf.set("hbase.cluster.distributed", "true");
             // 这个可以理解成用户名信息，也就是Principal
//             conf.set("kerberos.principal" , "hbase/_HOST@CLOUDERA" );         
             conf.set("hbase.zookeeper.quorum", "172.16.19.156");
             conf.set("hbase.zookeeper.property.clientPort", "2181");
             conf.set("hbase.security.authentication", "kerberos");
//             conf.set("hadoop.security.authentication", "kerberos");
             conf.set("hbase.master.kerberos.principal", "hbase/centos-1@CLOUDERA");
             conf.set("hbase.regionserver.kerberos.principal", "hbase/centos-1@CLOUDERA");
            UserGroupInformation. setConfiguration(conf);
             try {
//                  UserGroupInformation.loginUserFromKeytab("hbase/centos-1@CLOUDERA", "D:/linux/cdh_kit/kerberos/156/hbase.keytab" );
          	   System.out.println("before login !");
          	   UserGroupInformation.loginUserFromKeytab("hbase/centos-1@CLOUDERA", HbaseServlet.class.getClassLoader().getResource("hbase.keytab").getPath());
           	   System.out.println("login success!");
             } catch (IOException e) {
                   // TODO Auto-generated catch block
                  e.printStackTrace();
            }
             conf = HBaseConfiguration.create(HBASE_CONFIG);  
             try {  
            	   String tablename = "scores";  
                   String[] familys = {"grade", "course"};  
                   System.out.println("before create table......");
                   
//                   HBaseAdmin admin = new HBaseAdmin(conf);  
//                   if (admin.tableExists(tablename)) {  
//                       System.out.println(writer==null);  
//                       writer.println("table already exists!");  
//                       writer.println("<br>");  
//                   } else {  
//                       HTableDescriptor tableDesc = new HTableDescriptor(tablename);  
//                       for(int i=0; i<familys.length; i++){  
//                           tableDesc.addFamily(new HColumnDescriptor(familys[i]));  
//                       }  
//                       admin.createTable(tableDesc);  
//                       writer.println("create table " + tablename + " ok.");  
//                       writer.println("<br>");  
//                   }   
//                 this.creatTable(tablename, familys);  
                 System.out.println("after create table......");
//                    
//                 //add record zkb  
                 System.out.println("before add data!");
                 
//                 (String tableName, String rowKey, String family, String qualifier, String value)  
                 
                 HTable table = new HTable(conf, tablename);  
                 Put put = new Put(Bytes.toBytes("zkb"));  
//                 put.add(Bytes.toBytes("grade"),Bytes.toBytes(""),Bytes.toBytes("5"));  
                 put.add(Bytes.toBytes("course"),Bytes.toBytes(""),Bytes.toBytes("90"));  
//                 tablename,"zkb","course","","90"
                 table.put(put);  
                 writer.println("insert recored " + "zkb" + " to table " + tablename +" ok.");  
                 writer.println("<br>");  
//                 this.addRecord(tablename,"zkb","grade","","5");  
//                 this.addRecord(tablename,"zkb","course","","90");  
//                 this.addRecord(tablename,"zkb","course","math","97");  
//                 this.addRecord(tablename,"zkb","course","art","87");  
//                 //add record  baoniu  
//                 this.addRecord(tablename,"baoniu","grade","","4");  
//                 this.addRecord(tablename,"baoniu","course","math","89");  
                 System.out.println("after add data!");
//                    
                 System.out.println("before select single record!");
                 writer.println("===========get one record========");  
                 writer.println("<br>");  
//                 HTable table = new HTable(conf, tablename);  
                 Get get = new Get("zkb".getBytes());  
                 Result rs = table.get(get);  
                 for(KeyValue kv : rs.raw()){  
                     writer.println(new String(kv.getRow()) + " " );  
                     writer.println("<br>");  
                    writer.println(new String(kv.getFamily()) + ":" );  
                    writer.println("<br>");  
                    writer.println(new String(kv.getQualifier()) + " " );  
                    writer.println("<br>");  
                    writer.println(kv.getTimestamp() + " " );  
                    writer.println("<br>");  
                     writer.println(new String(kv.getValue()));  
                     writer.println("<br>");  
                 }  
                 System.out.println("after select single record!");
//                 this.getOneRecord(tablename, "zkb");  
//                    
//                 writer.println("===========show all record========");  
//                 writer.println("<br>");  
//                 this.getAllRecord(tablename);  
//                    
//                 writer.println("===========del one record========");  
//                 writer.println("<br>");  
//                 this.delRecord(tablename, "baoniu");  
//                 this.getAllRecord(tablename);  
//                    
//                 writer.println("===========show all record========");  
//                 writer.println("<br>");  
//                 this.getAllRecord(tablename);  
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

## tdh hive 整合jsp#############
##first step: import data from hbase to hive 
tablename :KITDEV:MREQ_PICKING_PROJ_INFO
--建立hive外部表
CREATE EXTERNAL TABLE hbase_MREQ_PICKING_PROJ_INFO (
ROW_ID STRING,
MATERIAL_DIR_NAME STRING,
PROJ_CNT STRING
) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES
("hbase.columns.mapping"=":key,cl:MATERIAL_DIR_NAME, cl:PROJ_CNT") 
TBLPROPERTIES
("hbase.table.name"="KITDEV:MREQ_PICKING_PROJ_INFO");

## second step: connect hive connection
			String testUrl = "jdbc:hive2://172.16.19.154:10000/default";
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			return DriverManager.getConnection(testUrl, "", "");
## third step:			write code to make your app to run!
demoOne: select data from table

 public static void main(String[] args) {
        Connection conn=null;
        Statement st =null;
        ResultSet rs = null;
        String sql = "select * from  ca_ctl_log";
        try {
            conn = JDBCUtils.getConnection();
            //创建运行环境
            st = conn.createStatement();
            //运行HQL语句
          rs = st.executeQuery(sql);
            //取出数据
            while (rs.next()){
                String i = rs.getString("log_time");
                System.out.println(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.relese(conn,st,rs);
        }
    }

##fourth step: wirte the servlet code to implements the destination

a: get the dat to the list
b:set the list to req
c:the jsp page  show the data
 
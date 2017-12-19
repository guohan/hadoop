package com.kit.example;
/**
 * 
 * @author lenovo
 * hbase数据获取示例
 */
/*
 * Need Packages:
 * commons-codec-1.4.jar
 *
 * commons-logging-1.1.1.jar
 *
 * hadoop-0.20.2-core.jar
 *
 * hbase-0.90.2.jar
 *
 * log4j-1.2.16.jar
 *
 * zookeeper-3.3.2.jar
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseSelecter
{
    public static Configuration configuration = null;
    static
    {
        configuration = HBaseConfiguration.create();
        //configuration.set("hbase.master", "192.168.0.201:60000");
        configuration.set("hbase.zookeeper.quorum", "idc01-hd-nd-03,idc01-hd-nd-04,idc01-hd-nd-05");
        //configuration.set("hbase.zookeeper.property.clientPort", "2181");
    }

    public static void selectRowKey(String tablename, String rowKey) throws IOException
    {
        HTable table = new HTable(configuration, tablename);
        Get g = new Get(rowKey.getBytes());
        Result rs = table.get(g);

        for (KeyValue kv : rs.raw())
        {
            System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
            System.out.println("Column Family: " + new String(kv.getFamily()));
            System.out.println("Column       :" + new String(kv.getQualifier()));
            System.out.println("value        : " + new String(kv.getValue()));
        }
    }

    public static void selectRowKeyFamily(String tablename, String rowKey, String family) throws IOException
    {
        HTable table = new HTable(configuration, tablename);
        Get g = new Get(rowKey.getBytes());
        g.addFamily(Bytes.toBytes(family));
        Result rs = table.get(g);
        for (KeyValue kv : rs.raw())
        {
            System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
            System.out.println("Column Family: " + new String(kv.getFamily()));
            System.out.println("Column       :" + new String(kv.getQualifier()));
            System.out.println("value        : " + new String(kv.getValue()));
        }
    }

    public static void selectRowKeyFamilyColumn(String tablename, String rowKey, String family, String column)
            throws IOException
    {
        HTable table = new HTable(configuration, tablename);
        Get g = new Get(rowKey.getBytes());
        g.addColumn(family.getBytes(), column.getBytes());

        Result rs = table.get(g);

        for (KeyValue kv : rs.raw())
        {
            System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
            System.out.println("Column Family: " + new String(kv.getFamily()));
            System.out.println("Column       :" + new String(kv.getQualifier()));
            System.out.println("value        : " + new String(kv.getValue()));
        }
    }

    public static void selectFilter(String tablename, List<String> arr) throws IOException
    {
        HTable table = new HTable(configuration, tablename);
        Scan scan = new Scan();// 实例化一个遍历器
        FilterList filterList = new FilterList(); // 过滤器List

        for (String v : arr)
        { // 下标0为列簇，1为列名，3为条件
            String[] wheres = v.split(",");

            filterList.addFilter(new SingleColumnValueFilter(// 过滤器
                    wheres[0].getBytes(), wheres[1].getBytes(),

                    CompareOp.EQUAL,// 各个条件之间是" and "的关系
                    wheres[2].getBytes()));
        }
        scan.setFilter(filterList);
        ResultScanner ResultScannerFilterList = table.getScanner(scan);
        for (Result rs = ResultScannerFilterList.next(); rs != null; rs = ResultScannerFilterList.next())
        {
            for (KeyValue kv : rs.list())
            {
                System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
                System.out.println("Column Family: " + new String(kv.getFamily()));
                System.out.println("Column       :" + new String(kv.getQualifier()));
                System.out.println("value        : " + new String(kv.getValue()));
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        if(args.length < 2){
            System.out.println("Usage: HbaseSelecter table key");
            System.exit(-1);
        }

        System.out.println("Table: " + args[0] + " , key: " + args[1]);
        selectRowKey(args[0], args[1]);

        /*
        System.out.println("------------------------行键  查询----------------------------------");
        selectRowKey("b2c", "yihaodian1002865");
        selectRowKey("b2c", "yihaodian1003396");

        System.out.println("------------------------行键+列簇 查询----------------------------------");
        selectRowKeyFamily("riapguh", "用户A", "user");
        selectRowKeyFamily("riapguh", "用户B", "user");

        System.out.println("------------------------行键+列簇+列名 查询----------------------------------");
        selectRowKeyFamilyColumn("riapguh", "用户A", "user", "user_code");
        selectRowKeyFamilyColumn("riapguh", "用户B", "user", "user_code");

        System.out.println("------------------------条件 查询----------------------------------");
        List<String> arr = new ArrayList<String>();
        arr.add("dpt,dpt_code,d_001");
        arr.add("user,user_code,u_0001");
        selectFilter("riapguh", arr);
        */
    }
}
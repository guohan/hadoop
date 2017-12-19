//package com.hbase;
//
///**
//* Hbase 基本CRUD 样例代码   覆盖Put Get Delete checkAndPut checkAndDelete  Scan
//* 通过上面的各种操作的例子, 会基本覆盖Htable可以用的的所有方法
//* 这里不涉及Hbase 管理代码的操作
//* @author Administrator
//*
//*/
//public class HbaseCRUDTest_New {
//    private static org.apache.hadoop.conf.Configuration conf = null;
//    private static HTablePool pool = null;
//    private static HBaseAdmin admin = null;
//    private static final int MAX_TABLE_COUNT = 10;
//
//    @BeforeClass
//    public static void before()throws Exception{
//        /**服务器端缓存客户端的连接 是以conf为单位的（可能不准确：通常一个客户端
//         * 连接过来, 服务器端会有一个线程与之对应, 缓存的是这个服务器端的线程）,
//         * 所以最好不要到处创建conf实例, 一个就够了, 所有共用conf创建的到Hbase
//         * 的连接和操作, 会共用一个连接  这样可以提高性能, 也会减小服务器端的压力
//         * 实际上创建Htable pool admin都是通过HConnection接口的实现类（
//         * HConnectionImplementation）来完成的, 多个HConnection会由
//         * HConnectionManager来管理, 而conf是HConnectionImplementation的最
//         * 重要的构造参数 , 上面就以conf 来 标识和替代Hconnection 可能会带来歧义
//         * 以为conf就是连接本身
//         */
//        conf = HBaseConfiguration.create();
//        //这是一个10台集群的daily 日常性能测试环境
//        conf.set("hbase.zookeeper.quorum", "10.232.31.209,10.232.31.210,10.232.31.211";
//        conf.set("hbase.zookeeper.property.clientPort", "3325";
//        //conf.addResource("dataSource.xml";//也可以载入一个标准的Hbase配置文件
//
//        /**HTable是非线程安全的  在多线程环境下使用HTablePool是一个好的解决方案,
//         * 参数MAX_TABLE_COUNT 是 pool保持的每个Htable实例的最大数量  ,
//         * 比如为10   如果有100个线程getTable() 同一张表   则他们会共用 pool中的该
//         * 表的10个实例   有些可能要排队等 用完的要回收放回去
//         * 使用的时候 就不要new Htable了, 直接从pool中取
//         * 用完再putTable 放回去
//         *
//         * 在0.92以上的版本  则不用放回去   直接table.close() 即可    putTable 被标记
//         * 为 @Deprecated.  0.90.2 版本使用 putTable 下面的代码都没有 做 这些操作
//         * 避免 不同版本 出问题
//         */
//        pool = new HTablePool(conf, MAX_TABLE_COUNT);
//        admin = new HBaseAdmin(conf);
//    }
//    /**
//     * 注意一下：Put Get Delete Scan等操作的对象 都提供一个空的构造函数, 一般不要直接使用, 他们存在主要是在rpc传输的反序列化的时候要用到（了解Java RMI的应该很清楚）
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    @Test public void putTest() throws IOException, InterruptedException{
//        HTable table = (HTable)pool.getTable("user_test_xuyang";
//        //批量操作 共两种 底层都是调用  HConnection的processBatch方法(
//        // table.batch(List<Put> 和table.flushCommits()会直接调用)
//
//        //首先  自动flush 关闭    就像 JDBC中的 auto_commit,  否则 加每一条 提交
//        // 一次,影响性能     不过table.put(List<Put> table.batch(List<Row>不受这
//        // 个影响, 设置false,只有当put总大小超过writeBufferSize 才提交  或者手工
//        // table.flushCommits() （table.put(List<Put>操作完成后会手工提交一次）,
//        // writeBufferSize 也可以调整
//        table.setAutoFlush(false);
//        //writeBufferSize 默认为2M ,调大可以增加批量处理的吞吐量, 丢失数据的风险也会加大
//        table.setWriteBufferSize(1024*1024*5);
//        //这样可以看到 当前客户端缓存了多少put
//        ArrayList<Put> putx = table.getWriteBuffer();
//
//        // 批量操作方法一,单一操作的批量 比如Htable.put delete get 都提供了List作
//        // 为参数的批处理.   默认每10条 或List<Put>数据量 超过writeBufferSize 提交
//        // 如果AutoFlush为true 一次性table.put(List<Put>只提交一次
//        List<Put> puts = new ArrayList<Put>(10);
//
//        for (int i = 0,len=10; i < len; i++) {
//            Put put = new Put(Bytes.toBytes("row-"+i),new Date().getTime());
//            put.add(Bytes.toBytes("data", Bytes.toBytes("name"), Bytes.toBytes("value"+i));
//            // 这里可以自定义添加时间戳, 默认就是当前时间(RegionServer服务器端的
//            // 时间) 也可以自己定义, 多版本时候(默认3)比如想插入一条比现在最新的记
//            // 录老的, 一些特殊情况下可能会有这种需求
//            put.add(Bytes.toBytes("data"), Bytes.toBytes("email"), System.currentTimeMillis(),
//                    Bytes.toBytes("value"+i+"@sina.com"));
//            //也可以直接加入一个KeyValue,实际上底层就是存储为KeyValue的, 如果对
//            // 底层较熟悉, 这种操行更加高效, 一般上面的就可以完成日常工作了
//            put.add(new KeyValue(Bytes.toBytes("row-"+i), Bytes.toBytes("data"),
//                    Bytes.toBytes("age"),Bytes.toBytes(20+i)));
//            puts.add(put);
//
//            //写操作日志  这个对性能影响比较大,  但有很重要, 如果设为true, 只要写
//            // 成功, 就算 机器挂掉 也不会丢失,
//            put.setWriteToWAL(false);
//            /**
//             * Put还有一些额外的东西
//             */
//            //put.has(family, qualifier,ts,value)
//            //put 当前在内存中的大小  这个在setWriteBufferSize 可能会用到
//            /**实际上底层是 这么干的（当然还有其他比如put数量对table.flushCommits()的触发）
//             * for(Put pututs){
//             *  total+=put.heapSize();
//             *  if(total>=table.getWriteBufferSize())
//             *      table.flushCommits();
//             * }
//             */
//            put.heapSize();
//            //put 中 每次调用add 底层都会添加一个KeyValue,这个是添加的KeyValue数量
//            put.size();
//
//            //判断put中是否已经存在了 给定的family qualifier ts value
////          put.has(family, qualifier)
////          put.has(family, qualifier, value)
////          put.has(family, qualifier, ts)
////          put.has(family, qualifier, ts, value)
//
//            //下面的方法 从字面上基本上就可以知道
//            put.isEmpty();
//            put.getRow();
//            put.getRowLock();
//            put.getLockId();
//            put.numFamilies();
//
//        }
//
//        table.put(puts);
//        table.flushCommits();
//        System.out.println(table.get(new Get(Bytes.toBytes("row-1"))));
//        admin.flush("user_test_xuyang");
//        System.out.println(table.get(new Get(Bytes.toBytes("row-1"))));
//
//        //批量操作方法一, 使用batch,可以混合各种操作 ( Put Delete Get 都是接口Row的实现)
//        //主要 这个如果处理Put操作 是不会使用客户端缓存的   会直接异步的发送到服务器端
//        List<Row> rows = new ArrayList<Row>(10);
//        for (int i = 10,len=20; i < len; i++) {
//            Put put = new Put(Bytes.toBytes(("row-"+i)));
//            put.add(Bytes.toBytes("data"), Bytes.toBytes("name"), Bytes.toBytes(("value"+i)));
//            put.add(Bytes.toBytes("data"), Bytes.toBytes("email"), Bytes.toBytes(("value"+i+"@sina.com")));
//            rows.add(put);
//        }
//        //可以添加 删除操作   但是 最好不要把对同一行的Put Delete用batch操作 ,
//        // 因为 为了更好的性能  发到服务器端操作的顺序  是会改变的   很有可能不是放入的顺序
//        rows.add(new Delete(Bytes.toBytes("row-9")));
//        table.batch(rows);
//
//            /**  一些需要注意的地方：
//             * 1. 提交到服务器  处理如果出现问题  会从服务器端返回RetriesExhaustedWithDetailsException
//             * 包含出错的原因 和重试的次数
//             * 如果 服务器端还是操作失败 , 这些put还会缓存在客户端  等到下次buffer 被flush,
//             * 注意  如果客户端挂掉了   这些数据是会丢失的
//             * 当然如果是NoSuchColumnFamilyException只会重试一次 并且不会恢复
//             * 下面的情况要注意了
//             * table.put(puts); 是会抛出异常的,而且不会再提交  这样数据会丢失的
//             * 捕获这个异常手工table.flushCommits() 可以确保已经写入缓存的还可以有可能写入成功
//             * try {
//                    table.put(puts);
//                } catch (Exception e) {
//                    table.flushCommits();
//                }
//             * table.flushCommits(); 也会有异常   也要捕获
//             *
//             * 2. 还时候 启用缓存   正常操作发生异常时候并不会被正常报出来, 有时候
//             * 会等到buffer被flush后才报出来  这也是要注意的地方
//             *
//             * 3.在缓存中的puts 被发送到服务器端的顺序和服务器处理的顺序 是控制不
//             * 到的, 如果想指定顺序 , 只能使用较小的批处理  强制他们按照批处理的顺序执行
//             */
//
//        /**
//         * 完备的一条记录就是一个KeyValue 一个rowkey可能有多个KeyValue（比如
//         * 多个版本, 一个版本是一条）
//         * rowkey ColumnFamily  Column  TimeStamp Type Value
//         * 其中的Type就是区别Put和Delete等操作的类别, 实际上Delete也是添加一条记录
//         * （Hbase存储的HDFS文件是只读的, 更新用 添加+删除 组合完成, 删除实际上
//         * 也是添加一条删除,实际操作都是添加,在Hbase Compact时候 合并数据时候会剔除标记为删除的rowkey）
//         * 这种 增 改 删的一致性操作  在客户端给我们的操作带来了便利
//         *
//         * 实际上ColumnFamily Column的名字是会以byte的形式存储在数据中的,
//         * 因此, 它们在设计的时候名字应该尽可能的短 这样可以节省不少的空间
//         */
//    }
//
//    /**
//     * Delete与Put一致 把全部的Put改成Delete  table.put -->table.delete 就可以了,
//     * 不过有些需要注意, 看下面
//     */
//    @Test public void deleteTest(){
//        HTable table = (HTable)pool.getTable("user_test_xuyang");
//        try {
//            //如果上面介绍的KeyValue 有点印象, 通过delete提供的构造函数可以知道
//            //不指定会删除所有的版本
//            Delete delete = new Delete(Bytes.toBytes("row-1"));
//            table.delete(delete);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 一些原子性操作   对于java并发工具包有所了解的 应该会知道 轻量级锁的核心就是CAS机制(Compare and swap),
//     * 这里在概念上有些类似, 也可以类似于  SQL中  select 出来然后   insert or update的 操作  Hbase这里可以保证他们在一个原子操作
//     * 这个在高并发 场景下  更新值  是个好的选择
//     * table.checkAndPut(row, family, qualifier, value, put)
//     * table.checkAndDelete(row, family, qualifier, value, delete)
//     * @throws IOException
//     */
//    @Test public void atomicOP() throws IOException{
//        byte[] row = Bytes.toBytes("row-12");
//        byte[] family = Bytes.toBytes("data");
//
//        HTable table = (HTable)pool.getTable("user_test_xuyang");
//        //操作成功会返回 true,否则false;  如果是个不存在的qualifier, 把value置为null  check是会成功的
//        Put put = new Put(row);
//        put.add(family, Bytes.toBytes("namex"), Bytes.toBytes("value12"));
//        //check 和put是同一个row
//        boolean result1 = table.checkAndPut(row, family, Bytes.toBytes("namex"), null, put);  //true
//        boolean result2 = table.checkAndPut(row, family, Bytes.toBytes("namex"), null, put);   //false
//
//        Put put2 = new Put(row);
//        put2.add(family, Bytes.toBytes("namex"), Bytes.toBytes("value12"));
//        boolean result3 = table.checkAndPut(row, family, Bytes.toBytes("namex"),
//                Bytes.toBytes("value12"), put2);  //true
//
//        Put put3 = new Put(Bytes.toBytes("row-13"));
//        put3.add(family, Bytes.toBytes("namex"), Bytes.toBytes("value13"));
//        boolean result4 = table.checkAndPut(row, family, Bytes.toBytes("namex2"),
//                Bytes.toBytes("value12"), put3);  //org.apache.hadoop.hbase.DoNotRetryIOException
//        //注意：check 和put的一定要是同一行 否则会报错
//
////      table.checkAndDelete类似
//    }
//
//    /**
//     * 上面的一些操作有些方法可能涉及到Row Locks 但并没有说明   这里详细介绍下
//     *
//     * 一些会使数据发生变化的操作  比如like put(), delete(), checkAndPut()等等 , 操作都是以一个row为单位的,
//     * 使用row lock 可以保证  一次性只能有一个客户端修改一个row
//     * 虽然 实践中  客户端应用程序 并没有明确的使用lock, 但服务端会在适当的时机保护每一个独立的操作
//     *
//     * 如果可能应当尽量避免使用lock, 就像RSBMS一样会有死锁问题
//     * @throws IOException
//     */
//    @Test public void rowLocksTest() throws IOException{
//        HTable table = (HTable)pool.getTable("user_test_xuyang");
//        byte[] row = Bytes.toBytes("row-8");
//        RowLock lock = table.lockRow(row);
//        //.....相关操作
//        table.unlockRow(lock);
//        //锁有效时间 默认时间是1分钟
//    }
//
//    @SuppressWarnings("deprecation")
//    @Test public void getTest(){
//        HTable table = (HTable)pool.getTable("user_test_xuyang");
//        Get get = new Get(Bytes.toBytes("row-10"));
//        //默认 get 只会取得最新的记录, 使用下面的方法可以获取其他的版本
//        //有两个方法 一个带参数的可以指定版本数量, 可能会抛出异常;另外一个没有
//        // 参数, 默认Integer.MAX_VALUE, 不会抛出异常
//        get.setMaxVersions();
//    //  get.setFilter(filter); get 一般数据比较少比较少使用filter, 在Scan的时候会详细介绍Filter
//        //通过get.addColumn提供了各种重载方法, 可以过滤只获取哪些ColumnFamily
//        // 和Column,get实现这种过滤只能使用这种方法, 接下来的Scan还可以使用Filter来实现
//        get.addColumn(Bytes.toBytes("data"),Bytes.toBytes("email"));
//
//        try {
//            Result result = table.get(get);
//            //这是一个简单的 获取返回结果的方法, 还有其他的通过遍历Map的方式
//            List<KeyValue> values = result.list();
//            //由于KeyValue靠近底层, 对于一些一些Offset,Length结尾的方法 可以忽略,
//            // 比较感兴趣的可以关注下Hbase的底层存储
//            for (KeyValue keyValue : values) {
//                StringBuilder sb = new StringBuilder(Bytes.toString(keyValue.getFamily()));
//                sb.append(":").append(Bytes.toString(keyValue.getQualifier())).append("--:");
//                sb.append(Bytes.toString(keyValue.getValue())).append("  ").append(
//                        new Date(keyValue.getTimestamp()).toLocaleString());
//                System.out.println(sb.toString());
//            }
//
//            //这是另外一种获取返回结果的方式, 这种在Scan的返回多个Result的时候
//            // 相对实用, 一个rowkey的都在一起, 一个ColumnFamily的也聚合在一起
//            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> nMap = result.getMap();
//            for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry:nMap.entrySet() ) {
//                //entry.getKey()为family key
//                String family = Bytes.toString(entry.getKey());
//                System.out.print(family+":");
//                for (Map.Entry<byte[], NavigableMap<Long, byte[]>> entry2 : entry.getValue().entrySet() ) {
//                    // entry2.getKey()为qualifier  当然qualifier有可能为空  这个不是问题  但为null的只能有一个
//                    String qualifier = Bytes.toString(entry2.getKey());
//                    System.out.print(qualifier+"--:");
//                    for (Map.Entry<Long, byte[]> entry3:entry2.getValue().entrySet() ) {
//                        //entry3.getKey()为 timestamp  entry3.getValue()为 value
//                        System.out.print(Bytes.toString(entry3.getValue())+" "
//                                +new Date(entry3.getKey()).toLocaleString());
//                    }
//                }
//            }
//            System.out.println("------------------");
//            //Get的批处理类似于 SQL中的in操作,但操作起来也相当的简单, 和上面
//            // 的Put Delete非常类似,也可以混合使用
//            List<Row> rows = new ArrayList<Row>();
//            rows.add(new Get(Bytes.toBytes("row-10")));
//            rows.add(new Get(Bytes.toBytes("row-11")));
//            rows.add(new Put(Bytes.toBytes("row-1222221")));
//            try {
//                Object[] objs = table.batch(rows);
//                for (Object obj : objs) {
//                    printKeyValue((Result)obj);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        /**
//         * Get获取单个 或随机的几个row 使用起来非常方便, 对于访问多个连续的row
//         * 使用下面将要介绍的Scan操作,通常情况下, 完成一个业务 需要多个操作, 而
//         * ORM无法将一个业务所有的操作SQL封装在一起, 除非直接使用JDBC
//         * Hbase的这种 Put Get Delete 是不是很棒 , 对了没有Update, update的就
//         * 是新增一条, 由于有版本, 旧的不会被立即淘汰掉
//         */
//    }
//
//    public void printKeyValue(Result result){
//        List<KeyValue> values = result.list();
//        //由于KeyValue靠近底层, 对于一些一些Offset,Length结尾的方法 可以忽略,
//        // 比较感兴趣的可以关注下Hbase的底层存储
//        for (KeyValue keyValue : values) {
//            StringBuilder sb = new StringBuilder(Bytes.toString(keyValue.getFamily()));
//            sb.append(":").append(Bytes.toString(keyValue.getQualifier())).append("--:");
//            sb.append(Bytes.toString(keyValue.getValue())).append("  ").append(
//                    new Date(keyValue.getTimestamp()).toLocaleString());
//            System.out.println(sb.toString());
//        }
//    }
//
//    /**
//     * 对于连续记录的顺序访问  就是类似于 最常见的Select操作
//     * 实际上Scan 非常类似于Hibernate 的DetachedCriteria, 而scan 使用的Filter就相当于Criteria的Expression或Restrictions
//     * 可以实现离线封装查询条件   这个是相当的给力啊
//     */
//    @Test public void ScanTest(){
//        ResultScanner resultScanner = null;
//        try {
//            /**
//             * 如Scan的名字, Scan是在一定的范围内startkey(StartRow)和endkey(StopRow)
//             * 之间 顺序的扫描, 配合Filter 可以跳过不满足条件的记录  返回需要的结果
//             * 当然startkey和endkey只是标识一个范围, 它们对应的rowkey可能并不存在,
//             * 但如果存在(startkey) 扫描的范围是[startkey,endkey),否则就是(startkey,endkey)
//             * 可以看到 Scan 有一个包裹Get的构造, 可以利用该get的rowkey作为startkey
//             */
//            Scan scan  = new Scan();
//
//            /**
//             * ResultScanner 就是table scanner返回的结果集, 类似于游标 可以迭代获取结果,
//             * batch 就是每次迭代从服务器获取的记录数, 设置太小 会频繁到服务器取数据,
//             * 太大 会对客户端造成比较大的压力,  具体根据需要使用 , 正常使用可以不必管
//             * 它, 大批量读取可以考虑用它改善性能
//             * 这里要注意了： 这个记录数是qualifier不是row, 如果一个row有17个qualifier,
//             * setBatch(5),一个row就会分散到4个Result中, 分别持有5,5,5,2个qualifier
//             * （默认一个row的所有qualifier会在一个Result中）
//             *
//             * ColumnPaginationFilter 对于一个Row会在一个Result 但是只返回前面一部分
//             *
//             * 如果使用FirstKeyOnlyFilter等 不是扫描Row全部的Filter 会有冲突 会有异常抛出 */
//            scan.setBatch(10);
//            /**发给scanners的缓存的Row的数量, 如果没有设置会使用 HTable#getScannerCaching()的值
//             * 一般 越大 Scan速度越快, 但消耗的内存也越大*/
//            scan.setCaching(10);
//            //简而言之就是  batch 是qualifier column级别的   caching是row级别的
//
//            //RegionServer是否应当缓存 当前客户端访问过的数据块    如果是随机的get 这个最好为false
//            scan.setCacheBlocks(true);
//
//            /** Scan 最复杂, 也最有用的就是Filter, 特别是FilterList对Filter进行的组合
//             * 这里只先介绍Scan的其他参数 ;对于Filter,后面会单独介绍*/
//
//            //startrow和stoprow 可以改变
//            scan.setStartRow(Bytes.toBytes("row-12"));
//            scan.setStopRow(Bytes.toBytes("row-1110"));
//            scan.setMaxVersions(3);//同Get
//            /** 可以指定一个时间范围, 扫描指定时间或时间范围的的记录,  */
//            scan.setTimeRange(System.currentTimeMillis()-1000000, System.currentTimeMillis());
//            /**
//             * 也可以指定timestamp 查询
//             */
//            scan.setTimeStamp(System.currentTimeMillis());
//
//            /**可以使用Get中类似的方法 来限制获取的ColumnFamily Column*/
//            scan.addColumn(Bytes.toBytes(""),Bytes.toBytes(""));
//
//            //Scan中使用最多的还是Filter
//            HTable table = (HTable)pool.getTable("user_test_xuyang");
//            //看下面
//            table.setScannerCaching(1000);
//            resultScanner = table.getScanner(scan);
//            //这是foreach格式    是调用resultScanner.next()的
//            //默认情况下  每次调用next() 都要RPC一下服务器   每个row一次, 即时resultScanner(int nbRows)
//            //table.setScannerCaching() 默认是1  可以手工设置  设置后 该table实例的所有scan都有效
//            //也可以每个scan单设置定就是上面有说过的scan.setCaching(1024*10); 这个会覆盖table设置的值
//            for (Result result : resultScanner) {
//                //这里就不多说了   和Get中一样的解析
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            //这样一定要记住 用完close
//            if(resultScanner!=null)resultScanner.close();
//        }
//
//    }
//
//    /**
//     * 高级的Scan,就是Filter中的FilterList  可以组合各个Filter
//     * select cf1.column1,cf2.column2* from table_name where rowkey>10 or value like 'xxx%' limit 10
//     * 如果上面的SQL解析出来 and 表示MUST_PASS_ALL, or 表示MUST_PASS_ONE
//     * 就是下面这个样(虽然理解可能不同,但下图的代码如下：)
//     *  ( (cf1 and column1) or (cf2 and column2*)  ) and (rowkey>10 or value like 'xxx%')
//     * setFilter(
//     *                                                        -ColumnFamilyFilter  cf1
//     *                                     -filterList(ALL)--|
//     *                                    |                   -ColumnFilter   column1
//     *                  -filterList(ONE)->|
//     *                 |                  |                   -ColumnFamilyFilter cf2
//     *                 |                   -filterList(ALL)--|
//     *                 |                                      -ColumnPrefixFilter column2
//     *filterList(ALL)->|                -RowFilter 10
//     *                 |-filterList(ONE)->|
//     *                 |                   -ValueFilter xx
//     *                  -PageFilter 10
//     * @author Administrator
//     *
//     */
//    @Test public void scanAdvance(){
//        Scan scan  = new Scan();
//        List<Filter> rootList = new ArrayList<Filter>();
//            List<Filter> selectList = new ArrayList<Filter>();
//                List<Filter> select_1 = new ArrayList<Filter>();
//                    select_1.add(new FamilyFilter(CompareFilter.CompareOp.EQUAL,
//                            new BinaryComparator(Bytes.toBytes("cf1"))));
//                    select_1.add(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
//                            new BinaryComparator(Bytes.toBytes("column1"))));
//                List<Filter> select_2 = new ArrayList<Filter>();
//                    select_2.add(new FamilyFilter(CompareFilter.CompareOp.EQUAL,
//                            new BinaryComparator(Bytes.toBytes("cf2"))));
//                    select_2.add(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
//                            new BinaryPrefixComparator(Bytes.toBytes("column"))));
//            selectList.add(new FilterList(Operator.MUST_PASS_ALL, select_1));
//            selectList.add(new FilterList(Operator.MUST_PASS_ALL, select_2));
//        rootList.add(new FilterList(Operator.MUST_PASS_ONE,selectList));
//
//            List<Filter> whereList = new ArrayList<Filter>();
//                whereList.add(new RowFilter(CompareFilter.CompareOp.GREATER,
//                        new BinaryComparator(Bytes.toBytes(10))));
//                whereList.add(new RowFilter(CompareFilter.CompareOp.EQUAL,
//                        new BinaryPrefixComparator(Bytes.toBytes("xxx"))));
//        rootList.add(new FilterList(Operator.MUST_PASS_ONE,whereList));
//        scan.setFilter(new FilterList(Operator.MUST_PASS_ALL, rootList));
//        //这样的嵌套 写起来着实很烦, 可以自己封装成程序
//    }
//
//    /**
//     * 一个不得不说的操作  分页操作, RDBMS 比如mysql :select * from table_name where sss=sss limit 1 10;oracle 利用rownum也可以迂回实现,
//     * Hbase这方面支持的不是太好, 也可以支持翻页
//     */
//    @Test public void pageTest(){
//        //与传统的分页的不同  start 是个起始的row  而不是一个数字 ,   下一页 的时候
//        // 需要将上一页的最后一条记录作为分页条件传回来
//        //这个start要是byte[],页面上只能暂时保存字符串  怎么办呢？？
//        //Bytes.toStringBinary(byte[])与Bytes.toBytesBinary(String) 可以完美的实现字符串和byte[]的相互转换
//        // Bytes.toStringBinary(Bytes.toBytesBinary("abc")) equals "abc" 是true
//        byte[] start = Bytes.toBytes("row-13");
//        int limit = 10;
//        Scan scan = new Scan();
//        scan.setStartRow(start);
//
//        List<Filter> rootList = new ArrayList<Filter>();
//        rootList.add(new PageFilter(limit));
//        ////root.add(new Filter()) 添加其他的过滤条件
//
//        scan.setFilter(new FilterList(Operator.MUST_PASS_ALL, rootList));
//    }
//
//    /**除了上面用到的
//     * Htable 还有一些其他的有用方法
//     * @throws IOException
//     */
//    @SuppressWarnings("unused")
//    @Test public void htableOthers() throws IOException{
//        HTable table = (HTable)pool.getTable("user_test_xuyang");
//        byte [] row = Bytes.toBytes("row-13");
//        //获取指定row的 数据所在的Region的信息 ：名字, 编码后名字(Hadoop 中的
//        // 路径名), startKey endkey等--->hri  ；还有该Region所在的主机的地址信息--->addr
//        HRegionLocation hrl = table.getRegionLocation(row);
//        HRegionInfo hri= hrl.getRegionInfo();
//        HServerAddress addr = hrl.getServerAddress();
//
//        //获取所有Region的信息
//        Map<HRegionInfo,HServerAddress> regions = table.getRegionsInfo();
//
//        //获取该表所在的所有Region的  startKey 和 endKey
//        Pair<byte[][], byte[][]> startendKeys = table.getStartEndKeys();
//        //下面的是通过上面的实现的
//        table.getStartKeys();
//        table.getEndKeys();
//
//        //table.getRowOrBefore(row, family) 这个一般用不到  0.92时候 就要被废弃了
//    }
//
//    /**
//     * 操作完成后, 清理下资源还是很有必要的,
//     * 在系统的ServletContextListener
//     */
//    @AfterClass
//    public static void after(){
//        try {
//            if(conf!=null) HConnectionManager.deleteConnection(conf, false);
//            if(pool!=null)pool.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//        // 路径名), startKey endkey等--->hri  ；还有该Region所在的主机的地址信息--->addr
//        HRegionLocation hrl = table.getRegionLocation(row);
//        HRegionInfo hri= hrl.getRegionInfo();
//        HServerAddress addr = hrl.getServerAddress();
//
//        //获取所有Region的信息
//        Map<HRegionInfo,HServerAddress> regions = table.getRegionsInfo();
//
//        //获取该表所在的所有Region的  startKey 和 endKey
//        Pair<byte[][], byte[][]> starten

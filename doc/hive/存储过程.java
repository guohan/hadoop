
package com.aspire.dotcard.syncData.dao;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.aspire.common.config.ConfigFactory;
import com.aspire.common.db.DAOException;
import com.aspire.common.db.DB;
import com.aspire.common.exception.BOException;
import com.aspire.common.log.proxy.JLogger;
import com.aspire.common.log.proxy.LoggerFactory;
import com.aspire.common.persistence.DataAccessException;
import com.aspire.common.persistence.util.SQLCode;
import com.aspire.dotcard.gcontent.DeviceDAO;
import com.aspire.dotcard.gcontent.DeviceVO;
import com.aspire.dotcard.gcontent.GAppContent;
import com.aspire.dotcard.gcontent.GAppGame;
import com.aspire.dotcard.gcontent.GAppSoftWare;
import com.aspire.dotcard.gcontent.GAppTheme;
import com.aspire.dotcard.gcontent.GContent;
import com.aspire.dotcard.gcontent.GContentFactory;
import com.aspire.dotcard.syncData.vo.ContentTmp;
import com.aspire.dotcard.syncData.vo.DeviceResourceVO;
import com.aspire.ponaadmin.web.db.TransactionDB;
import com.aspire.ponaadmin.web.system.Config;
import com.aspire.ponaadmin.web.util.PublicUtil;

/**
 * <p>
 * 数据同步（包括业务数据和内容数据）
 * </p>
 * <p>
 * Copyright (c) 2003-2007 ASPire TECHNOLOGIES (SHENZHEN) LTD All Rights
 * Reserved
 * </p>
 * 
 * @author zhangmin
 * @version 1.0.1.0
 * @since 1.0.1.0
 */
public class DataSyncDAO
{

    /**
     * 日志引用
     */
    JLogger logger = LoggerFactory.getLogger(DataSyncDAO.class);

    private static DataSyncDAO dao = new DataSyncDAO();

    /**
     * 从运营标签中获取标签数
     */
    private int OP_TAG_NUM = 2;

    /**
     * 从应用标签中获取标签数
     */
    private int AP_TAG_NUM = 6;

    /**
     * 获取标签排列顺序，true表示运营标签在前 false表示运用标签在后
     */
    private boolean OP_TAG_FIRST = true;

    /**
     * 待同步内容机型适配列表缓存。key 表示 contentid，value 表示 适配机型的列表
     */
    //private HashMap ContentDevicesCache;
    
    /**
     * 待同步内容平台适配列表缓存。key 表示 contentid，value 表示 适配平台的列表
     */
    private HashMap ContentPlatformCache;

    /**
     * 免费的应用集合 add by aiyan 2011-11-10
     */
    private Set ContentFeeCache;

    /**
     * MM终端机型列表
     */
   // private HashMap deviceMappingCache;

    private DataSyncDAO()
    {
        OP_TAG_NUM = Integer.parseInt(Config.getInstance()
                                            .getModuleConfig()
                                            .getItemValue("OP_TAG_NUM"));
        AP_TAG_NUM = Integer.parseInt(Config.getInstance()
                                            .getModuleConfig()
                                            .getItemValue("AP_TAG_NUM"));
        String opTag = Config.getInstance()
                             .getModuleConfig()
                             .getItemValue("OP_TAG_FIRST");
        if (null != opTag && "true".equalsIgnoreCase(opTag))
        {
            OP_TAG_FIRST = true;
        }
        else
        {
            OP_TAG_FIRST = false;
        }

        if (OP_TAG_NUM > AP_TAG_NUM)
        {
            logger.error("从运营标签中获取标签数不能大于从应用标签中获取标签数");
            OP_TAG_NUM = 2;
            AP_TAG_NUM = 6;
        }
    }

    /**
     * 单例模式
     * 
     * @return
     */
    public static DataSyncDAO getInstance()
    {

        return dao;
    }

    /**
     * 支持事务的数据库操作器，如果为空表示是非事务类型的操作
     */
    private TransactionDB transactionDB;

    /**
     * 获取事务类型TransactionDB的实例 如果已经指定了，用已经指定的。如果没有指定，自己创建一个，注意自己创建的直接用不支持事务类型的即可
     * 
     * @return TransactionDB
     */
    private TransactionDB getTransactionDB()
    {

        if (this.transactionDB != null)
        {
            return this.transactionDB;
        }
        return TransactionDB.getInstance();
    }

    /**
     * 获取事务类型的DAO实例
     * 
     * @return AwardDAO
     */

    public static DataSyncDAO getTransactionInstance(TransactionDB transactionDB)
    {

        DataSyncDAO dao = new DataSyncDAO();
        dao.transactionDB = transactionDB;
        return dao;
    }

    /**
     * 本地同步前的数据准备，主要是读取缓存信息。
     */
    public void prepareDate() throws BOException
    {
        try
        {
            //ContentDevicesCache = getAllSyncContentDevices();
            ContentPlatformCache = getAllSyncContentPlatform();
            ContentFeeCache = getAllSyncContentFee();
            //deviceMappingCache = DeviceDAO.getInstance().getDeviceList();
        }
        catch (DAOException e)
        {
            logger.error("初始化缓存失败" + e);
            throw new BOException("初始化缓存失败");
        }

    }
    
    /**
     * 本地同步前的数据准备，主要是读取缓存信息。 
     * add by aiyan 2012-05-28 同步的时候如果电子流那边超过2万个应用更新就容易内存溢出，此处修改。
     */
    public void prepareDate(Set contentIdSet) throws BOException
    {
        try
        {
            //ContentDevicesCache = getAllSyncContentDevices(contentIdSet);
            ContentPlatformCache = getAllSyncContentPlatform(contentIdSet);
            if(ContentFeeCache==null){
            	ContentFeeCache = getAllSyncContentFee();
            }
//            if(deviceMappingCache==null){
//            	deviceMappingCache = DeviceDAO.getInstance().getDeviceList();
//            }
            
        }
        catch (DAOException e)
        {
            logger.error("初始化缓存失败" + e);
            throw new BOException("初始化缓存失败");
        }

    }
    

    /**
     * 清楚缓存信息，释放内存。
     */
    public void clearDate() throws BOException
    {
       // ContentDevicesCache = null;
        ContentPlatformCache = null;
        ContentFeeCache = null;
       // deviceMappingCache = null;
    }
    
    //add by aiyan 2012-07-24。
//    public void clearContentDevicesCache(){
//    	ContentDevicesCache = null;
//    }
  //add by dongke 2013-04-17
    public void clearContentPlatformCache(){
    	ContentPlatformCache = null;
    }
    /**
     * 将CMS中的业务同步到PAS数据库中
     * 
     */
    public void syncService() throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("syncService()");
        }
        // 将需求中罗列的字段查询出来；
        // 写一个sql语句将一期的业务表和cms中查出来的信息合成一个物化视图即可。
       // CallableStatement cs = null;
       // Connection conn = DB.getInstance().getConnection();
        try
        {
            // cs = conn.prepareCall("{call dbms_mview.refresh(list =>
            // 'v_service')}");
            // cs.execute();
            this.syncVService();// 物化视图改成表。added by zhangwei
           // this.syncMOTOExt(); // 同步MOTO的应用扩展信息表  add by dongke 20110408
           // this.syncHTCExt(); // add by wml 130114
            // add by tungke 20091113
// del by dongke 20120515          
            //  cs = conn.prepareCall("{call dbms_mview.refresh(list => 'v_third_service')}");
         //   cs.execute();
        }
        catch (Exception ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
        	/*try
            {
               // cs.close();
             //   DB.close(conn);
            }
            catch (Exception ex)
            {
                logger.error("关闭CallableStatement=失败");
            }*/
        }
    }
    /**
     * 发送结果短信通知
     * @param content 短信内容
     * @param phone 发送号码
     * @throws DAOException 
     */
    public  void sendMsg(String phone,String content) throws DAOException{
    	String sqlCode="com.aspire.dotcard.syncData.dao.DataSyncDAO.sendMsg";
    	String[] params={phone,content};
    	DB.getInstance().executeBySQLCode(sqlCode, params);
    }
    
    /**
     * 将需要同步的内容列表并将内容id和内容最后更新时间插入到数据库t_syncContent_tmp中。
     * 
     * @param systime,当前系统的时间
     * @param isFull boolean 是否是全量同步，true 表示全量同步，false 表示增量同步
     */
    public void addContentTmp(long systime, boolean isFull) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("insertSysTime(" + systime + ")");
        }
        String sqlCode;
        Object[] paras;
        ResultSet rs = null;
        try
        {
            if (isFull)// 全量同步
            {
                paras = null;
                // 将内容id和最后跟新时间插入到表t_syncContent_tmp中.
                sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT1";
            }
            else
            // 增量同步
            {
                // 在表t_lastsynctime中查询得到上次系统启动时间；
                sqlCode = "SyncData.DataSyncDAO.addContentTmp().SELECT";
                rs = DB.getInstance().queryBySQLCode(sqlCode, null);
                // 如果该记录不存在证明是首次同步,则将CMS内容表中的所有内容状态为发布和过期的查询出来.
                if (rs.next())
                {
                    paras = new Object[1];
                    paras[0] = new Timestamp(systime);
                    // 将内容id和最后跟新时间插入到表t_syncContent_tmp中.
                    sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT2";
                }
                else
                // 查不到同步时间则使用全量同步。
                {
                    logger.info("t_lastsynctime表中没有上次同步记录。本次同步执行全量同步！");
                    addContentTmp(systime, true);
                    return;
                }
                // 如果有上次启动时间,则在CMS中取内容最后更新时间在上次启动时间和systime之间的数据

            }
            TransactionDB tdb = this.getTransactionDB();
            tdb.executeBySQLCode(sqlCode, paras);

        }
        catch (SQLException e)
        {
            throw new DAOException("将需要同步的内容列表放入历史表出错", e);
        }
        finally
        {
            DB.close(rs);
        }
    }
    
    /**
     * 将需要同步的紧急上线临时表中内容列表并将内容id和内容最后更新时间插入到数据库t_syncContent_tmp中。
     * 
     * @param systime,当前系统的时间
     */
    public void addContentTmp() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("addContentTmp()");
        }
        
        String sqlCode;
        ResultSet rs = null;
        try
        {
            // 将内容id和最后跟新时间插入到表t_syncContent_tmp中.
            sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT5";

            TransactionDB tdb = this.getTransactionDB();
            tdb.executeBySQLCode(sqlCode, null);
        }
        finally
        {
            DB.close(rs);
        }
    }

    /**
     * 根据内容最后更新时间升序排列查询得到需要同步的内容id,内容状态和内容最后更新时间列表。
     * 
     * @return list
     */
    public List getSyncContentTmp() throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getSyncContentTmp()");
        }
        // 根据t_synctime_tmp表中内容更新时间的升序查询得到查询结果；
        String sqlCode = "SyncData.DataSyncDAO.getSyncContentTmp().SELECT";
        ResultSet rs = DB.getInstance().queryBySQLCode(sqlCode, null);
        ContentTmp tmp;
        List list = new ArrayList();
        // 遍历结果,将每条记录置入类ContentTmp的各个属性中
        try
        {
            while (rs.next())
            {
                tmp = new ContentTmp();
                this.getContentTmpByRS(rs, tmp);
                list.add(tmp);
            }

        }
        catch (SQLException e)
        {
            throw new DAOException("读取临时表数据异常", e);
        }
        finally
        {
            // add by tungke for close
            DB.close(rs);

        }
        // 将各个ContentTmp放入list中返回
        return list;
    }

    /**
     * 将本次系统执行内容同步时间插入到表t_lastsynctime
     * 
     * @param Systime,本次系统执行内容同步的时间
     */
    public void insertSysTime(long systime) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("insertSysTime(" + systime + ")");
        }
        // 将systime时间插入表t_lastsynctime中。
        String sqlCode = "SyncData.DataSyncDAO.insertSysTime().INSERT";
        Timestamp ts = new Timestamp(systime);
        Object[] paras = { ts };
        TransactionDB tdb = this.getTransactionDB();
        tdb.executeBySQLCode(sqlCode, paras);
    }

    /**
     * 视图初始化为表
     * 
     */
    public void initViewToTable() throws DAOException
    {

        // 将视图创建为表
        // String dropTable1Sql = "drop table v_cm_content";
        String talbeName = "v_cm_content";
        String fromSql = "select * from PPMS_V_CM_CONTENT ";
        // String createTemp1Sql = "create table v_cm_content as select * from
        // PPMS_V_CM_CONTENT ";
        String createindexSql = "create index V_CM_CONTENT_CONID on V_CM_CONTENT (CONTENTID) ";
        CallableStatement cs = null;
        Connection conn = DB.getInstance().getConnection();
        try
        {  // add by dongke 20120515
           // this.fullSyncTables(talbeName, fromSql);

         //   DB.getInstance().execute(createindexSql, null);
          //  this.fullGetTableStats("V_CM_CONTENT");
        	
        	//CallableStatement cs = null;
            //Connection conn = DB.getInstance().getConnection();
           
             cs = conn.prepareCall("{?=call f_v_cm_content}");  
            cs.registerOutParameter(1, Types.INTEGER);  
            cs.execute();  
            int intValue = cs.getInt(1); //获取函数返回结果
            logger.debug("init表v_cm_content result =："+intValue);
            if(intValue != 0){
            	throw new DAOException("init v_cm_content error");
            }


          //  cs = conn.prepareCall("{call dbms_mview.refresh(list => 'v_third_service')}");
           // cs.execute();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("init表v_cm_content出错：", e);
            throw new DAOException("init表v_cm_content出错：",e);
        }finally{
        	if(cs != null){
        		try
				{
					cs.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(conn != null){
        		try
				{
					conn.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	
        }

        // 数据初始化完成

    }
    
    /**
     * MTK平台应用计费点对应关系模型同步过程
     * 
     */
    public void initMTKViewToTable() throws DAOException
    {

        // 将视图创建为表
        String talbeName = "v_mtk_content";
        String fromSql = "select * from PPMS_V_MTK_CONTENT ";

        try
        {
            // 视图转换为本地表
            this.fullSyncTables(talbeName, fromSql);

            // 表分析
            this.fullGetTableStats(talbeName);
            
            String sqlsCode = DB.getInstance().getSQLByCode("DataSyncDAO.initMTKViewToTable().grantright");
            String [] sqls = null;
            if(sqlsCode.indexOf(";")>=0){
            	 sqls = sqlsCode.split(";");
            }else{
               sqls = new String[]{sqlsCode};
            }
            if(sqls != null && sqls.length>0){
            	for(int i = 0 ; i < sqls.length; i ++){
            		DB.getInstance().execute(sqls[i],null);
            	}
            }
        }
        catch (BOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("drop表v_mtk_content出错：" + fromSql, e);
        }

        // 数据初始化完成

    }

    /**
     * 删除内容临时表中相应的记录
     * 
     * @param Id ,数据库中产生的序号
     */
    public void delSynccontetTmp(int Id) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("delSynccontetTmp(" + Id + ")");
        }
        // 通过传入的id字段找到相应的记录进行删除
        String sqlCode = "SyncData.DataSyncDAO.delSynccontetTmp().DELETE";
        Object[] paras = { new Integer(Id) };
        TransactionDB tdb = this.getTransactionDB();
        tdb.executeBySQLCode(sqlCode, paras);
    }

    /**
     * 根据内容id和内容类型从cms中查询得到内容对象，如果找不到该内容，则抛出异常
     * 
     * @param ContentId,内容id
     * @param contentType,内容对象
     * @return
     * @throws DAOException 发生数据库异常。或者找不到该内容
     */
    public GContent getGcontentFromCMS(String contentId, String contentType)
                    throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getGcontentFromCMS(" + contentId + "," + contentType
                         + ")");
        }
        String sqlCode = GContentFactory.getSqlCodeByType(contentType);
        Object[] paras = { contentId };
        ResultSet rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
        GContent gc = null;
        try
        {
            if (rs.next())
            {
                gc = this.getGContentByRS(rs, contentType);
            }
            else
            {
                throw new NullPointerException("从CMS找不到该内容。contentId="
                                               + contentId);
            }
        }
        catch (Exception e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        return gc;
    }

    /**
     * 将查询出来的结果集
     * 
     * @param rs
     * @param tmp
     * @throws SQLException
     */
    private void getContentTmpByRS(ResultSet rs, ContentTmp tmp)
                    throws SQLException
    {

        tmp.setId(rs.getInt("id"));
        tmp.setContentId(rs.getString("contentid"));
        tmp.setName(rs.getString("name"));
        tmp.setContentType(rs.getString("contentType"));
        tmp.setStatus(rs.getString("status"));
        tmp.setLupdDate(rs.getDate("pLupdDate"));
    }

    /**
     * 设置content的属性
     * 
     * @param rs，结果集
     * @param type,内容类型
     * @return
     */
    private GContent getGContentByRS(ResultSet rs, String type)
                    throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("getGContentByRS(" + type + ")");
        }
        if (GAppGame.TYPE_APPGAME.equals(type.trim()))
        {
            GAppGame content = new GAppGame();
            this.setBaseContent(rs, content);
            this.setGAppGameContents(rs, content);
            return content;
        }
        else if (GAppSoftWare.TYPE_APPSOFTWARE.equals(type.trim()))
        {
            GAppSoftWare content = new GAppSoftWare();
            this.setBaseContent(rs, content);
            this.setGAppSoftwareContents(rs, content);
            return content;
        }
        else if (GAppTheme.TYPE_APPTHEME.equals(type.trim()))
        {
            GAppTheme content = new GAppTheme();
            this.setBaseContent(rs, content);
            this.setGAppthemeContents(rs, content);
            return content;

        }
        else
        {
            return null;
        }
    }

    /**
     * 设置内容基本属性
     * 
     * @param rs
     * @param content
     * @throws SQLException
     */
    private void setBaseContent(ResultSet rs, GAppContent content)
                    throws SQLException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("setBaseContent()");
        }

        content.setName(rs.getString("name"));
        content.setCateName(rs.getString("cateName"));
        content.setSpName(rs.getString("spName"));
        content.setIcpCode(rs.getString("icpCode"));
        content.setIcpServId(rs.getString("icpServId"));
        content.setContentTag(rs.getString("contentTag"));
        content.setIntroduction(rs.getString("introduction"));
        content.setContentID(rs.getString("contentID"));
        content.setCompanyID(rs.getString("companyID"));
        content.setProductID(rs.getString("productID"));
        content.setKeywords(rs.getString("keywords"));
        // content.setCreateDate(PublicUtil.getCurDateTime());//createDate
        content.setCreateDate(rs.getString("createDate"));
        content.setMarketDate(rs.getString("marketdate"));
        content.setLupdDate(rs.getString("lupddate"));
        
        
        //在t_r_gcontent 增加 FUNCDESC varchar2(2000)， 为了是电子流在CM_CT_APPGAME加了个“新功能介绍”介绍的字段。
    	// add by aiyan 2011-11-10
        content.setFuncdesc(rs.getString("funcdesc"));
        
        content.setPlupdDate(rs.getString("plupddate"));
        // add by tungke
        content.setOtherNet(rs.getString("otherNet"));
        // add by tungke
        content.setServAttr(rs.getString("servAttr"));
        content.setSubType(rs.getString("subType"));
        //
        content.setPvcID(rs.getString("pvcid"));
        content.setCityID(rs.getString("cityid"));
        
       
        

        // 增加运维属性设置，初始值为0
        content.setOrderTimes(0);
        content.setWeekOrderTimes(0);
        content.setMonthOrderTimes(0);
        content.setDayOrderTimes(0);
        content.setScanTimes(0);
        content.setWeekScanTimes(0);
        content.setMonthScanTimes(0);
        content.setDayScanTimes(0);
        content.setSearchTimes(0);
        content.setWeekSearchTimes(0);
        content.setMonthSearchTimes(0);
        content.setDaySearchTimes(0);
        content.setCommentTimes(0);
        content.setWeekCommentTimes(0);
        content.setMonthCommentTimes(0);
        content.setDayCommentTimes(0);
        content.setMarkTimes(0);
        content.setWeekMarkTimes(0);
        content.setMonthMarkTimes(0);
        content.setDayMarkTimes(0);
        content.setCommendTimes(0);
        content.setWeekCommendTimes(0);
        content.setMonthCommendTimes(0);
        content.setDayCommendTimes(0);
        content.setCollectTimes(0);
        content.setWeekCollectTimes(0);
        content.setMonthCollectTimes(0);
        content.setDayCollectTimes(0);
        content.setAverageMark(0);
    }

    /**
     * 设置应用游戏内容属性
     * 
     * @param rs
     * @param content
     */
    private void setGAppGameContents(ResultSet rs, GAppGame content)
                    throws Exception
    {
        content.setAppCateID(rs.getString("appCateID"));
        content.setAppCateName(rs.getString("appCateName"));
        content.setWWWPropaPicture1(this.getImageUrl(rs.getString("WWWPropaPicture1")));
        content.setWWWPropaPicture2(this.getImageUrl(rs.getString("WWWPropaPicture2")));
        content.setWWWPropaPicture3(this.getImageUrl(rs.getString("WWWPropaPicture3")));
        content.setClientPreviewPicture1(this.getImageUrl(rs.getString("clientPreviewPicture1")));
        content.setClientPreviewPicture2(this.getImageUrl(rs.getString("clientPreviewPicture2")));
        content.setClientPreviewPicture3(this.getImageUrl(rs.getString("clientPreviewPicture3")));
        content.setClientPreviewPicture4(this.getImageUrl(rs.getString("clientPreviewPicture4")));
        // content.setProvider(rs.getString("provider"));
        content.setProvider("O");
        content.setLanguage(rs.getString("language"));
        content.setHandBook(rs.getString("handBook"));
        content.setHandBookPicture(this.getImageUrl(rs.getString("handBookPicture")));
        content.setUserGuide(rs.getString("userGuide"));
        content.setUserGuidePicture(this.getImageUrl(rs.getString("userGuidePicture")));
        content.setGameVideo(this.getVideoUrl(rs.getString("gameVideo")));

        // 加入flash应用接入对应全曲的aac播放HTTP URL
        content.setHandBookPicture(this.getVideoUrl(rs.getString("video")));

        content.setLOGO1(this.getImageUrl(rs.getString("LOGO1")));
        content.setLOGO2(this.getImageUrl(rs.getString("LOGO2")));
        content.setLOGO3(this.getImageUrl(rs.getString("LOGO3")));
        content.setLOGO4(this.getImageUrl(rs.getString("LOGO4")));
        content.setLOGO5(this.getImageUrl(rs.getString("LOGO5")));
        content.setCartoonPicture(this.getImageUrl(rs.getString("cartoonPicture")));
        // content.setProgramSize(rs.getInt("programsize"));
        // content.setProgramID(rs.getString("pid"));
        content.setProgramSize(0);
        content.setProgramID("");
        content.setOnlineType(rs.getInt("onlinetype"));
        content.setVersion(rs.getString("version"));
        content.setPicture1(this.getImageUrl(rs.getString("picture1")));
        content.setPicture2(this.getImageUrl(rs.getString("picture2")));
        content.setPicture3(this.getImageUrl(rs.getString("picture3")));
        content.setPicture4(this.getImageUrl(rs.getString("picture4")));
        content.setPicture5(this.getImageUrl(rs.getString("picture5")));
        content.setPicture6(this.getImageUrl(rs.getString("picture6")));
        content.setPicture7(this.getImageUrl(rs.getString("picture7")));
        content.setPicture8(this.getImageUrl(rs.getString("picture8")));
        content.setIsSupportDotcard(rs.getString("isSupportDotcard"));
        content.setPlatform(this.getPlatformByContentID(rs.getString("contentID")));
        content.setChargeTime(rs.getString("chargeTime"));

        content.setAdvertPic(this.getImageUrl(rs.getString("advertPic")));
        //content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));removed by aiyan 2012-08-14
        try{
        	
        	//logger.error("rs:"+rs);
        	//logger.error("clob:"+rs.getClob("RICHAPPDESC"));
        	//rs.getClob("RichAppdesc");
        	content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));
        }catch(Exception e){
        	logger.error("setGAppthemeContents->content.setRichAppdesc有错误！忽略了",e);
        }
        
        // 设置内容支持的终端类型,和品牌
        //setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * 设置应用软件的扩展属性
     * 
     * @param rs
     * @param content
     */
    private void setGAppSoftwareContents(ResultSet rs, GAppSoftWare content)
                    throws Exception
    {
        content.setAppCateID(rs.getString("appCateID"));
        content.setAppCateName(rs.getString("appCateName"));
        content.setWWWPropaPicture1(this.getImageUrl(rs.getString("WWWPropaPicture1")));
        content.setWWWPropaPicture2(this.getImageUrl(rs.getString("WWWPropaPicture2")));
        content.setWWWPropaPicture3(this.getImageUrl(rs.getString("WWWPropaPicture3")));
        content.setClientPreviewPicture1(this.getImageUrl(rs.getString("clientPreviewPicture1")));
        content.setClientPreviewPicture2(this.getImageUrl(rs.getString("clientPreviewPicture2")));
        content.setClientPreviewPicture3(this.getImageUrl(rs.getString("clientPreviewPicture3")));
        content.setClientPreviewPicture4(this.getImageUrl(rs.getString("clientPreviewPicture4")));
        content.setLanguage(rs.getString("language"));
        // content.setProvider(rs.getString("provider"));
        content.setProvider("O");
        content.setManual(this.getFileUrl(rs.getString("manual")));
        content.setLOGO1(this.getImageUrl(rs.getString("LOGO1")));
        content.setLOGO2(this.getImageUrl(rs.getString("LOGO2")));
        content.setLOGO3(this.getImageUrl(rs.getString("LOGO3")));
        content.setLOGO4(this.getImageUrl(rs.getString("LOGO4")));
        content.setLOGO5(this.getImageUrl(rs.getString("LOGO5")));
        content.setCartoonPicture(this.getImageUrl(rs.getString("cartoonPicture")));
        // content.setProgramSize(rs.getInt("programsize"));
        // content.setProgramID(rs.getString("pid"));

        // 加入flash应用接入对应全曲的aac播放HTTP
        content.setHandBookPicture(this.getVideoUrl(rs.getString("video")));

        content.setProgramSize(0);
        content.setProgramID("");
        content.setOnlineType(rs.getInt("onlinetype"));
        content.setVersion(rs.getString("version"));
        content.setPicture1(this.getImageUrl(rs.getString("picture1")));
        content.setPicture2(this.getImageUrl(rs.getString("picture2")));
        content.setPicture3(this.getImageUrl(rs.getString("picture3")));
        content.setPicture4(this.getImageUrl(rs.getString("picture4")));
        content.setPicture5(this.getImageUrl(rs.getString("picture5")));
        content.setPicture6(this.getImageUrl(rs.getString("picture6")));
        content.setPicture7(this.getImageUrl(rs.getString("picture7")));
        content.setPicture8(this.getImageUrl(rs.getString("picture8")));
        content.setIsSupportDotcard(rs.getString("isSupportDotcard"));
        content.setPlatform(this.getPlatformByContentID(rs.getString("contentID")));
        content.setChargeTime(rs.getString("chargeTime"));

        content.setAdvertPic(this.getImageUrl(rs.getString("advertPic")));
        //content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));removed by aiyan 2012-08-14
        try{
        	
        	//logger.error("rs:"+rs);
        	//logger.error("clob:"+rs.getClob("RICHAPPDESC"));
        	//rs.getClob("RichAppdesc");
        	content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));
        }catch(Exception e){
        	logger.error("setGAppthemeContents->content.setRichAppdesc有错误！忽略了",e);
        }
        
        // 设置软件内容支持的终端类型,和品牌
       // setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * 设置应用主题的扩展属性
     * 
     * @param rs
     * @param content
     */
    private void setGAppthemeContents(ResultSet rs, GAppTheme content)
                    throws Exception
    {
        content.setAppCateID(rs.getString("appCateID"));
        content.setAppCateName(rs.getString("appCateName"));
        content.setWWWPropaPicture1(this.getImageUrl(rs.getString("WWWPropaPicture1")));
        content.setWWWPropaPicture2(this.getImageUrl(rs.getString("WWWPropaPicture2")));
        content.setWWWPropaPicture3(this.getImageUrl(rs.getString("WWWPropaPicture3")));
        content.setClientPreviewPicture1(this.getImageUrl(rs.getString("clientPreviewPicture1")));
        content.setClientPreviewPicture2(this.getImageUrl(rs.getString("clientPreviewPicture2")));
        content.setClientPreviewPicture3(this.getImageUrl(rs.getString("clientPreviewPicture3")));
        content.setClientPreviewPicture4(this.getImageUrl(rs.getString("clientPreviewPicture4")));
        content.setLanguage(rs.getString("language"));
        // content.setProvider(rs.getString("provider"));
        content.setProvider("O");
        content.setLOGO1(this.getImageUrl(rs.getString("LOGO1")));
        content.setLOGO2(this.getImageUrl(rs.getString("LOGO2")));
        content.setLOGO3(this.getImageUrl(rs.getString("LOGO3")));
        content.setLOGO4(this.getImageUrl(rs.getString("LOGO4")));
        content.setLOGO5(this.getImageUrl(rs.getString("LOGO5")));
        content.setCartoonPicture(this.getImageUrl(rs.getString("cartoonPicture")));
        // content.setProgramSize(rs.getInt("programsize"));
        // content.setProgramID(rs.getString("pid"));
        content.setProgramSize(0);
        content.setProgramID("");
        content.setOnlineType(rs.getInt("onlinetype"));
        content.setVersion(rs.getString("version"));
        content.setPicture1(this.getImageUrl(rs.getString("picture1")));
        content.setPicture2(this.getImageUrl(rs.getString("picture2")));
        content.setPicture3(this.getImageUrl(rs.getString("picture3")));
        content.setPicture4(this.getImageUrl(rs.getString("picture4")));
        content.setPicture5(this.getImageUrl(rs.getString("picture5")));
        content.setPicture6(this.getImageUrl(rs.getString("picture6")));
        content.setPicture7(this.getImageUrl(rs.getString("picture7")));
        content.setPicture8(this.getImageUrl(rs.getString("picture8")));
        content.setIsSupportDotcard(rs.getString("isSupportDotcard"));
        content.setPlatform(this.getPlatformByContentID(rs.getString("contentID")));
        content.setChargeTime(rs.getString("chargeTime"));
        
        content.setAdvertPic(this.getImageUrl(rs.getString("advertPic")));
        //content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));removed by aiyan 2012-12-28
        try{
        	
        	//logger.error("rs:"+rs);
        	//logger.error("clob:"+rs.getClob("RICHAPPDESC"));
        	//rs.getClob("RichAppdesc");
        	content.setRichAppdesc(clob2String(rs.getClob("RichAppdesc")));
        }catch(Exception e){
        	logger.error("setGAppthemeContents->content.setRichAppdesc有错误！忽略了",e);
        }
        
        // 设置游戏内容支持的终端类型,和品牌
        // setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * 根据内容id得到终端列表
     * 
     * @param contentId
     * @param mathDeviceid 模糊适配设备ID zhangweixing add
     * @return 支持机型的终端id的列表
     */
//    private List getTerminalList(String contentId, List mathDeviceid)
//    {
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("getTerminalList(" + contentId + ")");
//        }
//        //
//        List newList = new ArrayList();
//        HashMap deviceMap = ( HashMap ) ContentDevicesCache.get(contentId);
//        if (deviceMap != null)
//        {
//            for (Iterator itrator = deviceMap.values().iterator(); itrator.hasNext();)
//            {
//                DeviceResourceVO vo = ( DeviceResourceVO ) itrator.next();
//                if (vo.getMatch() == 1)
//                {
//                    newList.add(vo.getDeviceId());
//                }
//                else
//                {
//                   // Boolean isFree = ( Boolean ) ContentFeeCache.get(contentId);
//                    if (ContentFeeCache.contains(contentId))// 只有免费才匹配。
//                    {
//                        newList.add(vo.getDeviceId());
//                        mathDeviceid.add(vo.getDeviceId());
//                    }
//                }
//            }
//        }
//        return newList;
//    }
    private List getTerminalList(String contentId)
  {
    	List newList = new ArrayList();
    	//TODO
    	
    	return newList;
  }
    /**
     * 判断当前同步操作是否为首次操作
     * 
     * @param systime ,当前系统的时间
     * @return 首次操作 返回true
     * @throws DAOException
     * @author biran
     */
    public boolean getFirstSync(long systime) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getFirstSync(" + systime + ")");
        }
        // 在表t_lastsynctime中查询得到上次系统启动时间；
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().SELECT";

        ResultSet rs = DB.getInstance().queryBySQLCode(sqlCode, null);
        // 如果该记录不存在证明是首次同步,则无须再做同步检查操作
        try
        {
            rs.next();
            Object o = rs.getObject(1);
            if (o == null)
            {
                // t_lastsynctime表记录不存在,为首次同步操作
                return true;
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        // 检查同步标志，默认为检查
        return false;
    }

    /**
     * 查询出CMS中新增/解除内容与业务关联的数据。
     * 
     * @throws DAOException
     * @author biran
     */
    public void againInsSyncContentTmp() throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("againInsSyncContentTmp()");
        }
        /*
         * // 查询出CMS中新增内容与业务关联的数据 String sqlCode =
         * "SyncData.DataSyncDAO.addContentTmp().INSERT3";
         * 
         * TransactionDB tdb = this.getTransactionDB();
         * tdb.executeBySQLCode(sqlCode, null);
         */

        // 查询出CMS中解除内容与业务关联的数据
        TransactionDB tdb = this.getTransactionDB();
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT4";
        tdb.executeBySQLCode(sqlCode, null);

    }

    /**
     * deviceName格式：{devicename1},{devicename2}… brand格式：{brand1},{brand2}…
     * 
     * @param content
     * @throws DAOException
     */
//    private void setDeviceNameAndBrand(GAppContent content) throws DAOException
//    {
//        // 取数据库中支持的终端类型DeviceDAO
//        // HashMap deviceMappingCache = DeviceDAO.getInstance().getDeviceList();
//        // 取得内容支持的终端类型集合 从CM_DEVICE_RESOURCE中查询
//        List matchDeviceId = new ArrayList();
//        List cList = getTerminalList(content.getContentID(), matchDeviceId);
//        // 记录平台支持的机型
//        // List errorList=new ArrayList();
//        // cList = this.filterList(cList, deviceMappingCache,errorList);
//
//        /*
//         * //记录PAS不支持deviceName的日志 if(errorList.size() > 0) {
//         * this.logErrorDeviceName(errorList,content); }
//         */
//        // 获取当前系统支持的机型名称，和品牌，按照字母顺序进行排序
//        Collection deviceNameList = new TreeSet();
//        
//        HashMap brandMap = new HashMap();// 需要去重。
//        for (int i = 0; i < cList.size(); i++)
//        {
//            String deviceId = ( String ) cList.get(i);
//            DeviceVO deviceVO = ( DeviceVO ) deviceMappingCache.get(deviceId);
//            deviceNameList.add(deviceVO.getDeviceName());
//            brandMap.put(deviceVO.getBrand(), "");
//        }
//        content.setFulldeviceID(list2String(cList));
//        content.setDeviceName(list2String(deviceNameList));
//        content.setBrand(list2String(brandMap.keySet()));
//        String temp = list2String(matchDeviceId);
//        content.setMatch_Deviceid(temp);// 模糊适配设备ID zhangweixing add
//        String fulldevice = PublicUtil.filterMbrandEspecialChar(list2String(deviceNameList));
//        content.setFulldeviceName(fulldevice);// add 20101108
//    }

    /* *//**
             * 目前不需要再验证了。 将存在于第一个list中且存在于第二个list中数据返回
             * 
             * @param one
             * @param deviceMap 终端库所有的机型集合
             * @return
             */
    /*
     * private List filterList(List one,HashMap deviceMap,List errorList) { List
     * result = new ArrayList(); Object temp = ""; for(int i = 0,size =
     * one.size(); i < size; i++) { temp=one.get(i); DeviceVO
     * vo=(DeviceVO)deviceMap.get(temp); if(vo!=null) { result.add(vo); }else {
     * //记录在PAS库中不存在的deviceName errorList.add(temp); } }
     * 
     * return result; }
     */
    /**
     * 将list中的数据拼装为 {devicename},{devicename}… 的样式
     * 
     * @param list 保存deviceName的集合
     * @return 形如：{devicename1},{devicename2}… 的字符串
     */
    private String list2String(Collection collection)
    {
        StringBuffer sb = new StringBuffer();
        Iterator iterator = collection.iterator();
        boolean dotFlag = false;// 第一次不需要插入逗号
        while (iterator.hasNext())
        {
            if (dotFlag)
            {
                sb.append(",");
            }
            else
            {
                dotFlag = true;
            }
            sb.append("{");
            sb.append(iterator.next());
            sb.append("}");
        }
        return sb.toString();
    }

    /**
     * 根据资源id得到文件内容的url
     * 
     * @param resourceId,资源id return String
     */
    private String getFileUrl(String resourceId) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getFileUrl(" + resourceId + ")");
        }
        String sqlCode = "SyncData.DataSyncDAO.getFileUrl().SELECT";
        Object[] paras = { resourceId };
        ResultSet rs = null;
        rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
        String url = null;
        try
        {
            if (rs != null && rs.next())
            {
                url = rs.getString("URL");
            }
        }
        catch (SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        return url;
    }

    /**
     * 根据资源id得到图片的url
     * 
     * @param resourceId,资源id return String
     */
    private String getImageUrl(String resourceId) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getImageUrl(" + resourceId + ")");
        }
        if (resourceId == null)
        {
            return null;
        }
        String sqlCode = "SyncData.DataSyncDAO.getImageUrl().SELECT";
        Object[] paras = { resourceId };
        ResultSet rs = null;
        rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
        String url = null;
        try
        {
            if (rs != null && rs.next())
            {
                url = rs.getString("URL");
            }
        }
        catch (SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        return url;
    }

    /**
     * 根据资源id得到视频内容的url
     * 
     * @param resourceId,资源id return String
     */
    private String getVideoUrl(String resourceId) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getVideoUrl(" + resourceId + ")");
        }
        if (resourceId == null)
        {
            return null;
        }
        String sqlCode = "SyncData.DataSyncDAO.getVideoUrl().SELECT";
        Object[] paras = { resourceId };
        ResultSet rs = null;
        rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
        String url = null;
        try
        {
            if (rs != null && rs.next())
            {
                url = rs.getString("URL");
            }
        }
        catch (SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        return url;
    }

    /**
     * 查询内容对应业务的业务通道类型 liyouli add patch 134
     */
    public String queryContentUmflag(String icpCode, String icpServID)
                    throws DAOException
    {
        String umFlag = null;

        String sqlCode = "SyncData.DataSyncDAO.queryContentUmflag().SELECT";
        Object[] paras = { icpCode, icpServID };
        ResultSet rs = null;
        try
        {
            rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
            if (rs.next())
            {
                umFlag = rs.getString("umflag");
            }
        }
        catch (SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }

        return umFlag;
    }

    /**
     * 根据内容ID得到支持的所有平台集合，以{}作边界，逗号分隔
     * 
     * @param contentID
     * @return
     * @throws DAOException
     */
   /* private String getPlatformByContentID(String contentID) throws DAOException
    {
        // 从配置项中获取kjava平台类型的扩展类型
        String platformExt = "";
        try
        {
            platformExt = Config.getInstance()
                                .getModuleConfig()
                                .getItemValue("platformExt");
        }
        catch (Exception e1)
        {
            logger.error("从配置项中获取kjava平台类型的扩展类型是出错！");
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("getPlatformByContentID(" + contentID + ")");
        }
        if (contentID == null)
        {
            return null;
        }
        String sqlCode = "SyncData.DataSyncDAO.getPlatformByContentID().SELECT";
        Object[] paras = { contentID };
        ResultSet rs = null;
        rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
        String platform = "";
        // 统计支持的平台个数
        int i = 0;
        try
        {
            String tmp = null;
            while (rs != null && rs.next())
            {
                tmp = rs.getString("platform").toLowerCase();
                if (platform.indexOf(tmp) == -1)
                {
                    if (i >= 1)
                    {
                        platform = platform + ",";
                    }
                    platform = platform + "{" + tmp + "}";
                }
                if ("kjava".equalsIgnoreCase(tmp)
                    && "1".equals(rs.getString("platformExt"))
                    && platform.indexOf(platformExt) == -1)
                {
                    platform = platform + "," + "{" + platformExt + "}";
                }
                i++;
            }
        }
        catch (SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            DB.close(rs);
        }
        return platform;
    }*/
    
    private String getPlatformByContentID(String contentID) throws DAOException
    {
    	StringBuffer ps = (StringBuffer)ContentPlatformCache.get(contentID);
    	String rsp = null;
    	if(ps != null ){
    		rsp = ps.toString();
    	}
    	return rsp;
    }
    /**
     * 
     * @desc 同步适配关系表
     * @author tungke Apr 5, 2011
     * @throws BOException
     */
    public void syncVCmDeviceResource() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("同步表 V_CM_DEVICE_RESOURC");
        }
        try
        {
        	String sytype = Config.getInstance()
            .getModuleConfig()
            .getItemValue("DEVICE_SYN_S");//同步状态；1，采用存储过程，2，采用JAVA代码同步
        	String dbsytype = Config.getInstance()
            .getModuleConfig()
            .getItemValue("DEVICE_SYN_TYPE");//存储过程同步类型;1，增量，0 ，全量
        	
        	int dbt = 1;//默认增量
        	if(dbsytype != null  && dbsytype.equals("0")){
        		//存储过程全量
        		dbt = 0;
        	}
        	if(sytype != null && sytype.equals("1")){
        		//调用存储过程同步，增量全量视dbt取值，1，增量，0，全量。
        		 CallableStatement cs = null;
        	        Connection conn = DB.getInstance().getConnection();
        		cs = conn.prepareCall("{?=call f_ppms_cm_device_pid(?,?)}"); 
        		
        		cs.setInt(2,dbt);
        		cs.setString(3,"java call");
        		
                cs.registerOutParameter(1, Types.INTEGER);  
                cs.execute();  
                int intValue = cs.getInt(1); //获取函数返回结果
                logger.debug("init表v_cm_content result =："+intValue);
                if(intValue != 0){
                	throw new DAOException("init v_cm_content error");
                }
        	}else{
        	
        	
            String fromSql = DB.getInstance()
                               .getSQLByCode("DataSyncDAO.syncVCmDeviceResource().SELECT");
            fullSyncTables("v_cm_device_resource",
                           fromSql,
                           "CONTENTID, DEVICE_NAME");

            // DB.getInstance().execute("create index
            // INDEX_v_device_resource_cID on v_cm_device_resource (contentid)",
            // null);

            this.fullGetTableStats("V_CM_DEVICE_RESOURCE");
        	}
        }
        catch (DAOException e)
        {
            throw new BOException("同步V_CM_DEVICE_RESOURC 表出错！", e);
        }catch (Exception e2)
        {
        	
        	e2.printStackTrace();
            throw new BOException("存储过程 同步V_CM_DEVICE_RESOURC 表出错！", e2);
        }
    }

    public void syncVService() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("同步表 V_SERVICE");
        }
        try
        {
            String fromSql = DB.getInstance()
                               .getSQLByCode("DataSyncDAO.syncVService().SELECT");
            fullSyncTables("V_SERVICE", fromSql, "CONTENTID");

//            DB.getInstance()
//              .execute("create unique index index_v_service_pk on V_SERVICE (icpcode, icpservid, providertype)",
//                       null);
            
            // dba ->chenghuoping say index on V_SERVICE (icpservid) is ok... modify by aiyan 2012-03-26
	          DB.getInstance()
	          .execute("create unique index index_v_service_serid_pk on V_SERVICE (icpservid)",
	                   null);
            
	          DB.getInstance()
	          .execute("create unique index INDEX_V_SERVICE_PK on V_SERVICE(ICPSERVID,CONTENTID)",
	                   null);
	          
            // DB.getInstance().execute("create index index_v_service on
            // v_service(contentid)", null);
	          
	        
            String sqlsCode = DB.getInstance().getSQLByCode("DataSyncDAO.syncVService().grantright");
            String [] sqls = null;
            if(sqlsCode.indexOf(";")>=0){
            	 sqls = sqlsCode.split(";");
            }else{
               sqls = new String[]{sqlsCode};
            }
            if(sqls != null && sqls.length>0){
            	for(int i = 0 ; i < sqls.length; i ++){
            		DB.getInstance().execute(sqls[i],null);
            	}
            }
            
	          //   DB.getInstance()
	          //    .executeBySQLCode("DataSyncDAO.syncVService().grantright", null);
	          this.fullGetTableStats("V_SERVICE");
        }
        catch (DAOException e)
        {
            throw new BOException("同步V_SERVICE 表出错！", e);
        }
    }
    
    /**
     * 
     *@desc 同步HTC应用扩展表 
     * @throws BOException
     */
    public void syncHTCExt() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("同步表 CM_CONTENT_HTCEXT");
        }
        try
        {
            String fromSql = DB.getInstance()
                               .getSQLByCode("DataSyncDAO.syncHTCExt().SELECT");
            fullSyncTables("CM_CONTENT_HTCEXT", fromSql, "APPID");

            this.fullGetTableStats("CM_CONTENT_HTCEXT");
        }
        catch (DAOException e)
        {
            throw new BOException("同步CM_CONTENT_HTCEXT 表出错！", e);
        }
    }

    /**
     * 
     *@desc 同步MOTO应用扩展表 
     *@author dongke
     *Apr 8, 2011
     * @throws BOException
     */
    public void syncMOTOExt() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("同步表 CM_CONTENT_MOTOEXT");
        }
        try
        {
            String fromSql = DB.getInstance()
                               .getSQLByCode("DataSyncDAO.syncMOTOExt().SELECT");
            fullSyncTables("CM_CONTENT_MOTOEXT", fromSql, "APPID");

          //  DB.getInstance()
            //  .execute("create unique index index_v_service_pk on V_SERVICE (icpcode, icpservid, providertype)",
            //           null);
            // DB.getInstance().execute("create index index_v_service on
            // v_service(contentid)", null);
          //  DB.getInstance()
            //  .executeBySQLCode("DataSyncDAO.syncVService().granttodls", null);
            this.fullGetTableStats("CM_CONTENT_MOTOEXT");
        }
        catch (DAOException e)
        {
            throw new BOException("同步CM_CONTENT_MOTOEXT 表出错！", e);
        }
    }
    /**
     * 通过contentID查找所有的商品信息,这些信息主要用于同步邮件结果展示。
     * 
     * @param contentID
     * @return list
     * @throws DAOException
     */
    public List getAllGoodsInfoByContentID(String contentID)
                    throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("getAllGoodsInfoByContentID(" + contentID
                         + ") is beginning ....");
        }
        Object[] paras = { contentID };
        String sqlCode = "DataSyncDAO.getAllGoodsInfoByContentID().SELECT";
        ArrayList list = new ArrayList();
        ResultSet rs = null;
        try
        {
            rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
            while (rs.next())
            {
                /*
                 * GoodsInfoVO vo = rs.getString("goodsid"));
                 * vo.setContentID(contentID)
                 */
                // list.add(vo);
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return list;
    }

    /**
     * 获取需要同步的上线内容的平台匹配信息。 返回HashMap，key 表示 contentid，value 表示 适配平台的列表
     * 
     * add by aiyan 2012-05-28 同步的时候如果电子流那边超过2万个应用更新就容易内存溢出，此处修改。
     * @return HashMap
     * @throws DAOException
     */
    public HashMap getAllSyncContentPlatform(Set contentIdSet) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("获取需要同步的上线内容的平台匹配信息");
        }
        
        if(contentIdSet==null||contentIdSet.size()==0){
        	return new HashMap();
        }
        StringBuffer sb = new StringBuffer();
        for(Iterator it =contentIdSet.iterator();it.hasNext(); ){
        	sb.append(",").append(it.next());
        }
        
        
        String sql = "select  distinct t.contentid,t.osname,t.platform   from T_CM_OPEN_DEVICE t where t.contentid  ";
        sql = sql +"in (" +sb.substring(1).toString()+")";
        HashMap allMap = new HashMap();
        ResultSet rs = null;
        try
        {
            rs = DB.getInstance().query(sql, null);
            while (rs.next())
            {
                String contentId = rs.getString(1);
                String platform = rs.getString(2).toLowerCase();
                if (platform != null && !platform.equals("")) {
					StringBuffer platForms = (StringBuffer) allMap
							.get(contentId);
					if (platForms == null) {
						platForms = new StringBuffer();
						platForms.append("{" + platform + "}");
						allMap.put(contentId, platForms);
					} else {
						if (platForms.indexOf("{" + platform + "}") == -1) {
							// 不包含该平台则追加进去
							platForms.append(",{" + platform + "}");
						} else {
							logger.error("重复平台信息");
						}
					}

				}

               

            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return allMap;
    }

    /**
     * 获取需要同步的上线内容的机型匹配信息。 返回HashMap，key 表示 contentid，value 表示 适配机型的列表
     * 
     * add by aiyan 2012-05-28 同步的时候如果电子流那边超过2万个应用更新就容易内存溢出，此处修改。
     * @return HashMap
     * @throws DAOException
     */
    public HashMap getAllSyncContentDevices(Set contentIdSet) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("获取需要同步的上线内容的机型匹配信息");
        }
        
        if(contentIdSet==null||contentIdSet.size()==0){
        	return new HashMap();
        }
        StringBuffer sb = new StringBuffer();
        for(Iterator it =contentIdSet.iterator();it.hasNext(); ){
        	sb.append(",").append(it.next());
        }
        
        
        String sql = "select distinct t.contentid,p.device_id,p.match,p.prosubmitdate from t_synctime_tmp t,V_CM_DEVICE_RESOURCE p where t.contentid=p.contentid and t.status='0006' and t.contentid ";
        sql = sql +"in (" +sb.substring(1).toString()+")";
        HashMap allMap = new HashMap();
        ResultSet rs = null;
        try
        {
            rs = DB.getInstance().query(sql, null);
            while (rs.next())
            {
                String contentId = rs.getString(1);
                
                HashMap deviceMap = ( HashMap ) allMap.get(contentId);
                if (deviceMap == null)
                {
                    deviceMap = new HashMap();
                    allMap.put(contentId, deviceMap);
                }
                DeviceResourceVO vo = new DeviceResourceVO();
                vo.setDeviceId(rs.getString(2));
                vo.setMatch(rs.getInt(3));

                Date proSubmitDate = rs.getTimestamp(4);
                if (proSubmitDate == null)// 跟ppms 确认，该值不可能为空
                {
                    continue;
                }
                vo.setProSubmitDate(proSubmitDate);

                DeviceResourceVO oldVO = ( DeviceResourceVO ) deviceMap.get(vo.getDeviceId());
                if (oldVO != null)// 说明已经存在
                {
                    if (vo.getProSubmitDate().after(oldVO.getProSubmitDate()))// 获取最新的程序的机型适配关系。
                    {
                        deviceMap.put(vo.getDeviceId(), vo);
                    }
                }
                else
                {
                    deviceMap.put(vo.getDeviceId(), vo);
                }

            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return allMap;
    }

    
    /**
     * 获取需要同步的上线内容的机型匹配信息。 返回HashMap，key 表示 contentid，value 表示 适配机型的列表
     * 
     * @return HashMap
     * @throws DAOException
     */
//    public HashMap getAllSyncContentDevices() throws DAOException
//    {
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("获取需要同步的上线内容的机型匹配信息");
//        }
//        Object[] paras = null;
//        String sqlCode = "DataSyncDAO.getAllSyncContentDevices().SELECT";
//        HashMap allMap = new HashMap();
//        ResultSet rs = null;
//        try
//        {
//            rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
//            while (rs.next())
//            {
//                /*
//                 * GoodsInfoVO vo = rs.getString("goodsid"));contentid
//                 * vo.setContentID(contentID)
//                 */
//                // list.add(vo);
//                String contentId = rs.getString(1);
//                HashMap deviceMap = ( HashMap ) allMap.get(contentId);
//                if (deviceMap == null)
//                {
//                    deviceMap = new HashMap();
//                    allMap.put(contentId, deviceMap);
//                }
//                DeviceResourceVO vo = new DeviceResourceVO();
//                vo.setDeviceId(rs.getString(2));
//                vo.setMatch(rs.getInt(3));
//
//                Date proSubmitDate = rs.getTimestamp(4);
//                if (proSubmitDate == null)// 跟ppms 确认，该值不可能为空
//                {
//                    continue;
//                }
//                vo.setProSubmitDate(proSubmitDate);
//
//                DeviceResourceVO oldVO = ( DeviceResourceVO ) deviceMap.get(vo.getDeviceId());
//                if (oldVO != null)// 说明已经存在
//                {
//                    if (vo.getProSubmitDate().after(oldVO.getProSubmitDate()))// 获取最新的程序的机型适配关系。
//                    {
//                        deviceMap.put(vo.getDeviceId(), vo);
//                    }
//                }
//                else
//                {
//                    deviceMap.put(vo.getDeviceId(), vo);
//                }
//
//            }
//        }
//        catch (SQLException ex)
//        {
//            throw new DAOException(ex);
//        }
//        finally
//        {
//            DB.close(rs);
//        }
//        return allMap;
//    }
    
    
    public HashMap getAllSyncContentPlatform() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("获取需要同步的上线内容的适配平台匹配信息");
        }
        Object[] paras = null;
        String sqlCode = "DataSyncDAO.getAllSyncContentPlatform().SELECT";
        HashMap allMap = new HashMap();
        ResultSet rs = null;
        try
        {
        	// select t.contentid,t.osname,t.platform from T_CM_OPEN_DEVICE t
            rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
            while (rs.next())
            {
                /*
                 * GoodsInfoVO vo = rs.getString("goodsid"));contentid
                 * vo.setContentID(contentID)
                 */
                // list.add(vo);
                String contentId = rs.getString(1);
                String platform = rs.getString(2);
                if (platform != null && !platform.equals("")) {
					StringBuffer platForms = (StringBuffer) allMap
							.get(contentId);
					if (platForms == null) {
						platForms = new StringBuffer();
						platForms.append("{" + platform + "}");
						allMap.put(contentId, platForms);
					} else {
						if (platForms.indexOf("{" + platform + "}") == -1) {
							// 不包含该平台则追加进去
							platForms.append(",{" + platform + "}");
						} else {
							logger.error("重复平台信息");
						}
					}

				}
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return allMap;
    }
    
    /**
     * 获取需要同步的上线内容的资费是否免费。//需要根据免费信息进行模糊机型
     * 
     * @return hashMap key 表示contentId value 表示该应用是否免费
     * @throws DAOException
     */
    public Set getAllSyncContentFee() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("获取需要同步的上线内容的资费是否免费信息");
        }
        Object[] paras = null;
        String sqlCode = "DataSyncDAO.getAllSyncContentFee().SELECT";
        Set set = new HashSet();
        ResultSet rs = null;
        try
        {
            rs = DB.getInstance().queryBySQLCode(sqlCode, paras);
            while (rs.next())
            {
                String contentId = rs.getString(1);
                int mobilePrice = rs.getInt(2);
                
                
                //removed by aiyan 2011-11-10
//                if (mobilePrice != 0)
//                {
//                    map.put(contentId, new Boolean(false));
//                }
//                else
//                {
//                    map.put(contentId, new Boolean(true));
//                }
                
                //add by aiyan 2011-11-10 因为这里用Map意义不大，改用Set
                if(mobilePrice == 0){
                	set.add(contentId);
                }
                

            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return set;
    }

    
    public void fullSyncService() throws BOException
    {
        String fromSql = "";
        fullSyncTables("v_service", fromSql);
        try
        {
            DB.getInstance().execute("", null);
            DB.getInstance().execute("", null);
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /***************************************************************************
     * 进行表分析
     * 
     * @param tableName
     * @throws BOException
     */
    public void fullGetTableStats(String tableName) throws BOException
    {

        String dbName = Config.getInstance()
        .getModuleConfig()
        .getItemValue("DB_NAME");
        
        // dba ->chenghuoping say 'cascade=>true,degree=>4' is bad... modify by aiyan 2012-03-26
        
//        String sql = "begin dbms_stats.GATHER_TABLE_STATS('"
//                     + dbName.toUpperCase() + "','" + tableName.toUpperCase()
//                     + "',cascade=>true,degree=>4); end;";
        
        String sql = "begin dbms_stats.GATHER_TABLE_STATS('"
            + dbName.toUpperCase() + "','" + tableName.toUpperCase()
            + "'); end;";
        
        logger.debug("进行表分析的sql为：" + sql);
        try
        {
            // 1 进行表分析
            DB.getInstance().execute(sql, null);
        }
        catch (DAOException e)
        {
            logger.error("表分析出错" + e);
            throw new BOException("表分析出错", e);
        }
    }

    /**
     * 全量同步表内容，通过别名建表然后改名实现
     * 
     * @param tableName
     * @fromSql 数据来源的sql
     * @throws BOException 通过过程出现异常
     */
    public void fullSyncTables(String tableName, String fromSql,
                               String indexTargetCon) throws BOException
    {

        logger.info("开始同步表 tableName:" + tableName);
        String timestamp = PublicUtil.getCurDateTime("HHSSS");
        String tempTableName = tableName + timestamp;// 防止同时操作的情况。
        String backupTableName = tableName + timestamp + "_bak";

        String createTempSql = "create table " + tempTableName + " as "
                               + fromSql;// 创建临时表
        String backupTableSql = "alter table " + tableName + " rename to "
                                + backupTableName;// 备份旧表
        String replaceSql = "alter table " + tempTableName + " rename to "
                            + tableName;// 该临时表名为正式表

        String dropBackupSql = "drop table " + backupTableName;// 删除备份表
        String dropTempSql = "drop table " + tempTableName;// 删除临时表
        String revertBackupTableSql = "alter table " + backupTableName
                                      + " rename to " + tableName;// 还原备份表

        logger.info("开始创建临时表:" + tempTableName);
        try
        {
            DB.getInstance().execute(createTempSql, null);
            logger.info("开始创建临时表索引:" + "create index IDX_" + tempTableName
                        + " on " + tempTableName + " (" + indexTargetCon + ")");
            DB.getInstance().execute("create index IDX_" + tempTableName
                                                     + " on " + tempTableName
                                                     + " (" + indexTargetCon
                                                     + ")",
                                     null);

        }
        catch (DAOException e)
        {
            throw new BOException("创建临时表出错：" + tempTableName, e);
        }

        logger.info("开始备份旧表 :" + backupTableName);
        try
        {
            DB.getInstance().execute(backupTableSql, null);
        }
        catch (DAOException e)
        {

            throw new BOException("备份表:" + tableName + "出错：" + tempTableName, e);
        }

        logger.info("开始更改临时表名为正式表:" + tempTableName);
        try
        {
            DB.getInstance().execute(replaceSql, null);

        }
        catch (DAOException e)
        {
            // 更改临时表失败，需要还原备份表，并删除临时表
            try
            {
                DB.getInstance().execute(revertBackupTableSql, null);// 还原备份表
                DB.getInstance().execute(dropTempSql, null);// 需要删除临时表。
            }
            catch (DAOException e1)
            {
                logger.error(e1);
            }
            throw new BOException("更改临时表名出错：" + tempTableName, e);
        }
        logger.info("删除备份表 " + backupTableName);
        try
        {
            DB.getInstance().execute(dropBackupSql, null);
        }
        catch (DAOException e)
        {
            logger.error("删除备份表失败:" + backupTableName);
        }// 删除备份表
    }

    /**
     * 全量同步表内容，通过别名建表然后改名实现
     * 
     * @param tableName
     * @fromSql 数据来源的sql
     * @throws BOException 通过过程出现异常
     */
    public void fullSyncTables(String tableName, String fromSql)
                    throws BOException
    {

        logger.info("开始同步表 tableName:" + tableName);
        String timestamp = PublicUtil.getCurDateTime("HHSSS");
        String tempTableName = tableName + timestamp;// 防止同时操作的情况。
        String backupTableName = tableName + timestamp + "_bak";

        String createTempSql = "create table " + tempTableName + " as "
                               + fromSql;// 创建临时表
        String backupTableSql = "alter table " + tableName + " rename to "
                                + backupTableName;// 备份旧表
        String replaceSql = "alter table " + tempTableName + " rename to "
                            + tableName;// 该临时表名为正式表

        String dropBackupSql = "drop table " + backupTableName;// 删除备份表
        String dropTempSql = "drop table " + tempTableName;// 删除临时表
        String revertBackupTableSql = "alter table " + backupTableName
                                      + " rename to " + tableName;// 还原备份表

        logger.info("开始创建临时表:" + tempTableName);
        try
        {
            DB.getInstance().execute(createTempSql, null);
        }
        catch (DAOException e)
        {
            throw new BOException("创建临时表出错：" + tempTableName, e);
        }

        logger.info("开始备份旧表 :" + backupTableName);
        try
        {
            DB.getInstance().execute(backupTableSql, null);
        }
        catch (DAOException e)
        {

            throw new BOException("备份表:" + tableName + "出错：" + tempTableName, e);
        }

        logger.info("开始更改临时表名为正式表:" + tempTableName);
        try
        {
            DB.getInstance().execute(replaceSql, null);

        }
        catch (DAOException e)
        {
            // 更改临时表失败，需要还原备份表，并删除临时表
            try
            {
                DB.getInstance().execute(revertBackupTableSql, null);// 还原备份表
                DB.getInstance().execute(dropTempSql, null);// 需要删除临时表。
            }
            catch (DAOException e1)
            {
                logger.error(e1);
            }
            throw new BOException("更改临时表名出错：" + tempTableName, e);
        }
        logger.info("删除备份表 " + backupTableName);
        try
        {
            DB.getInstance().execute(dropBackupSql, null);
        }
        catch (DAOException e)
        {
            logger.error("删除备份表失败:" + backupTableName);
        }// 删除备份表
    }

    /**
     * 获取应用标签
     * 
     * @param contentid
     * @return
     * @throws DAOException
     */
    public String getTagByContentID(String contentid) throws DAOException
    {
        StringBuffer sb = new StringBuffer("");
        if (contentid != null && contentid.length() > 0)
        {
            String querySQLCode = "DataSyncDAO.getTagByContentID().SELECT";
            Object[] paras = { contentid };
            ResultSet rs = null;
            try
            {
                rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
                // sb = new StringBuffer("");
                while (rs.next())
                {
                    String tagName = rs.getString(1);
                    sb.append("{" + tagName + "};");
                }
            }
            catch (SQLException ex)
            {
                logger.error("获取标签数据库操作失败," + ex);
                throw new DAOException(ex);
            }
            finally
            {
                DB.close(rs);
            }
        }
        return sb.toString();
    }

    /**
     * 获取运营和应用标签
     * 
     * @param contentid
     * @return
     * @throws DAOException
     */
    public String getOPTagAndAPTagByContentID(String contentid)
                    throws DAOException
    {
        String opTag = getLimitOPTagByContentID(contentid);
        String apTag = getLimitAPTagByContentID(contentid);
        if (OP_TAG_FIRST)
        {
            return opTag + apTag;
        }
        else
        {
            return apTag + opTag;
        }
    }

    /**
     * 获取应用标签
     * 
     * @param contentid
     * @return
     * @throws DAOException
     */
    private String getLimitAPTagByContentID(String contentid)
                    throws DAOException
    {
        StringBuffer sb = new StringBuffer("");
        if (contentid != null && contentid.length() > 0)
        {
            String querySQLCode = "DataSyncDAO.getLimitAPTagByContentID().SELECT";
            Object[] paras = { contentid, new Integer(AP_TAG_NUM - OP_TAG_NUM) };
            ResultSet rs = null;
            try
            {
                rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
                // sb = new StringBuffer("");
                while (rs.next())
                {
                    String tagName = rs.getString(1);
                    sb.append("{" + tagName + "};");
                }
            }
            catch (SQLException ex)
            {
                logger.error("获取标签数据库操作失败," + ex);
                throw new DAOException(ex);
            }
            finally
            {
                DB.close(rs);
            }
        }
        return sb.toString();
    }

    /**
     * 获取应用标签
     * 
     * @param contentid
     * @return
     * @throws DAOException
     */
    private String getLimitOPTagByContentID(String contentid)
                    throws DAOException
    {
        StringBuffer sb = new StringBuffer("");
        if (contentid != null && contentid.length() > 0)
        {
            String querySQLCode = "DataSyncDAO.getLimitOPTagByContentID().SELECT";
            Object[] paras = { contentid, new Integer(OP_TAG_NUM) };
            ResultSet rs = null;
            try
            {
                rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
                // sb = new StringBuffer("");
                while (rs.next())
                {
                    String tagName = rs.getString(1);
                    sb.append("{" + tagName + "};");
                }
            }
            catch (SQLException ex)
            {
                logger.error("获取标签数据库操作失败," + ex);
                throw new DAOException(ex);
            }
            finally
            {
                DB.close(rs);
            }
        }
        return sb.toString();
    }

    /**
     * 更新内容的评分,通过表 t_pps_comment_grade 计算得到
     * 
     * @return
     * @throws DAOException
     */
    public void updateRemarks()
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("updateRemarks()");
        }
        String sqlCode = "SyncData.DataSyncDAO.updateRemarks.UPDATE";
        try
        {
            DB.getInstance().executeBySQLCode(sqlCode, null);
        }
        catch (DAOException e)
        {
            logger.error("更新内容评分异常", e);
        }

    }

    /**
     * 查询分类目录下的商品
     * 
     * @param refNodeId
     * @param appCateName
     * @return
     * @throws Exception
     */
    public List getRefContentsByCateName(String querySQLCode,String refNodeId) throws DAOException
    {
        
        Object[] paras = { refNodeId };
        ResultSet rs = null;
        List refs = new ArrayList();
        try
        {
            rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
            // sb = new StringBuffer("");
            while (rs.next())
            {
                String id = rs.getString(1);
                refs.add(id);
            }
        }
        catch (SQLException ex)
        {
            logger.error("获取标签数据库操作失败," + ex);
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return refs;
    }
    
    /**
     * 
     *@desc  获取MOTO应用的MOTO的一级分类和二级分类名称
     *@author dongke
     *Apr 8, 2011
     * @param ctId
     * @return
     * @throws DAOException
     */
        public String[] getMOTOAppCateNameById(String ctId) throws DAOException
        {
            String querySQLCode = "DataSyncDAO.getMOTOAppCateNameById().SELECT";
            Object[] paras = { ctId };
            ResultSet rs = null;
            String [] res = null;
            String appCateName = null;
            String CateName = null;
            try
            {
                rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
                // sb = new StringBuffer("");
                while (rs.next())
                {
                	res = new String[2];
                    appCateName = rs.getString(3);
                    CateName  = rs.getString(2);
                    res[0] = CateName;
                    res[1] = appCateName;
                }
            }
            catch (SQLException ex)
            {
                logger.error("获取标签数据库操作失败," + ex);
                throw new DAOException(ex);
            }
            finally
            {
                DB.close(rs);
            }
            return res;
        }
        
    /**
	 * 
	 * @desc 获取HTC应用的HTC的一级分类和二级分类名称
	 * @author dongke Apr 8, 2011
	 * @param ctId
	 * @return
	 * @throws DAOException
	 */
	public String[] getHTCAppCateNameById(String ctId) throws DAOException {
		String querySQLCode = "DataSyncDAO.getHTCAppCateNameById().SELECT";
		Object[] paras = { ctId };
		ResultSet rs = null;
		String[] res = null;
		String appCateName = null;
		String CateName = null;
		try {
			rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
			// sb = new StringBuffer("");
			while (rs.next()) {
				res = new String[2];
				appCateName = rs.getString(3);
				CateName = rs.getString(2);
				res[0] = CateName;
				res[1] = appCateName;
			}
		} catch (SQLException ex) {
			logger.error("获取标签数据库操作失败," + ex);
			throw new DAOException(ex);
		} finally {
			DB.close(rs);
		}
		return res;
	}
            
/**
 * 
 * @desc 获取应用的二级分类名称
 * @author dongke Apr 8, 2011
 * @param ctId
 * @return
 * @throws DAOException
 */
    public String getGcAppCateNameById(String ctId) throws DAOException
    {
        String querySQLCode = "DataSyncDAO.getGcAppCateNameById().SELECT";
        Object[] paras = { ctId };
        ResultSet rs = null;
        String appCateName = null;
        try
        {
            rs = DB.getInstance().queryBySQLCode(querySQLCode, paras);
            // sb = new StringBuffer("");
            while (rs.next())
            {
                appCateName = rs.getString(1);
            }
        }
        catch (SQLException ex)
        {
            logger.error("获取标签数据库操作失败," + ex);
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return appCateName;
    }

    /**
     * 插入同步结果表
     * 
     * @param list
     * @throws DAOException
     */
    public void insertSynResult(List[] list) throws DAOException
    {
        logger.debug("DataSyncDAO.insertSynResult begin!");
        List updateList = list[0];// 成功更新。
        List addList = list[1];// 成功上线
        List deleteList = list[2];// 成功下线
        List errorList = list[3];// 失败同步
        PublicUtil.removeDuplicateWithOrder(errorList);// 去掉重复记录同步失败的问题。
        int size = updateList.size() + addList.size() + deleteList.size()
                   + errorList.size();
        logger.debug("共有" + size + "条记录，更新：" + updateList.size() + ",上线:"
                     + addList.size() + ",下线：" + deleteList.size() + ",删除:"
                     + errorList.size());
        String sqlCode = "DataSyncDAO.insertSynResult().INSERT";
        Object mutiParas[][] = new Object[size][4];
        String time = PublicUtil.getDateString(new Date(),
                                               "yyyy-MM-dd HH:mm:ss");
        if (size > 0)
        {
            ContentTmp tmp = null;
            int j = 0;
            for (int i = 0; i < addList.size(); i++)
            {
                tmp = ( ContentTmp ) addList.get(i);
                mutiParas[j][0] = tmp.getContentId();
                mutiParas[j][1] = tmp.getName();
                mutiParas[j][2] = new Integer(1);
                mutiParas[j][3] = time;
                j++;
            }
            for (int i = 0; i < updateList.size(); i++)
            {
                tmp = ( ContentTmp ) updateList.get(i);
                mutiParas[j][0] = tmp.getContentId();
                mutiParas[j][1] = tmp.getName();
                mutiParas[j][2] = new Integer(2);
                mutiParas[j][3] = time;
                j++;
            }
            for (int i = 0; i < deleteList.size(); i++)
            {
                tmp = ( ContentTmp ) deleteList.get(i);
                mutiParas[j][0] = tmp.getContentId();
                mutiParas[j][1] = tmp.getName();
                mutiParas[j][2] = new Integer(3);
                mutiParas[j][3] = time;
                j++;
            }
            for (int i = 0; i < errorList.size(); i++)
            {
                tmp = ( ContentTmp ) errorList.get(i);
                mutiParas[j][0] = tmp.getContentId();
                mutiParas[j][1] = tmp.getName();
                mutiParas[j][2] = new Integer(4);
                mutiParas[j][3] = time;
                j++;
            }

            try
            {
                DB.getInstance().executeBatchBySQLCode(sqlCode, mutiParas);
            }
            catch (Exception e)
            {
                logger.error("插入同步内容结果异常", e);
            }
        }
        logger.debug("DataSyncDAO.insertSynResult end!");
    }

    /**
     * 用于增量更新适配关系表时。删除当前device表中无用数据
     * 
     * @param contentId
     * @throws DAOException
     */
    public void deleteDeviceByContentid(String contentId) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.deleteDeviceByContentid()");
        }

        // delete v_cm_device_resource d where d.contentid = ?
        String sqlCode = "com.aspire.dotcard.syncData.dao.DataSyncDAO.deleteDeviceByContentid";
        String sql;

        try
        {
            sql = SQLCode.getInstance().getSQLStatement(sqlCode);
        }
        catch (DataAccessException e1)
        {
            throw new DAOException("DataSyncDAO.deleteDeviceByContentid() 获取sql语句出错",
                                   e1);
        }

        try
        {
            DB.getInstance().execute(sql, new Object[] { contentId });
        }
        catch (Exception e)
        {
            throw new DAOException("DataSyncDAO.deleteDeviceByContentid() error.",
                                   e);
        }
    }

    /**
     * 用于增量更新适配关系表时。按数据状态来新增device表中数据
     * 
     * @param contentId
     * @throws DAOException
     */
    public void insertDeviceByContentid(String contentId) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.insertDeviceByContentid()");
        }

        // insert into v_cm_device_resource select * from
        // ppms_CM_DEVICE_RESOURCE c where c.contentid=?
        String sqlCode = "com.aspire.dotcard.syncData.dao.DataSyncDAO.insertDeviceByContentid";
        String sql;

        try
        {
            sql = SQLCode.getInstance().getSQLStatement(sqlCode);
        }
        catch (DataAccessException e1)
        {
            throw new DAOException("DataSyncDAO.insertDeviceByContentid() 获取sql语句出错",
                                   e1);
        }

        try
        {
            DB.getInstance().execute(sql, new Object[] { contentId });
        }
        catch (Exception e)
        {
            throw new DAOException("DataSyncDAO.insertDeviceByContentid() error.",
                                   e);
        }
    }

    /**
     * 用于存储下线应用记录历史数据
     * 
     * @param contentId 应用id
     * @param type 应用类型
     * @throws DAOException
     */
    public void addContentIdHis(String contentId, String type)
                    throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.insertDeviceByContentidList()");
        }

        // insert into t_r_gcontent_his values (？,to_char(sysdate,'yyyy-mm-dd
        // hh24:mi:ss'),？)
        String sqlCode = "com.aspire.dotcard.syncData.dao.DataSyncDAO.addContentIdHis";

        try
        {
            DB.getInstance().executeBySQLCode(sqlCode.toString(),
                                              new Object[] { contentId, type });
        }
        catch (Exception e)
        {
            throw new DAOException("DataSyncDAO.insertDeviceByContentidList() error.",
                                   e);
        }
    }
    
    /**
     * 用于同步内容适配关系数据
     * 
     * @param contentList 应用列表
     * @param type 应用类型
     * @throws DAOException
     */
    public void syncVCmDeviceResourceAdd(List contentList)
                    throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.syncVCmDeviceResourceAdd()");
        }
        
        // 如果记录数大于900，逐条处理
        if(contentList.size()>900)
        {
            syncVCmDeviceResourceAddWhere(contentList);
        }
        // 如果记录数小于900大于0，用单条sql语句处理
        else if(contentList.size()>0)
        {
            syncVCmDeviceResourceAddIn(contentList);
        }
    }
    
    /**
     * 用于逐条处理同步内容适配关系数据
     * 
     * @param contentList 应用列表
     * @param type 应用类型
     * @throws DAOException
     */
    public void syncVCmDeviceResourceAddWhere(List contentList) throws DAOException
    {

        // delete v_cm_device_resource t where t.contentid=?
        String sqlCode_del = "com.aspire.dotcard.syncData.dao.DataSyncDAO.syncVCmDeviceResourceAdd.where.del";
        
        // insert into v_cm_device_resource (select * from ppms_CM_DEVICE_RESOURCE p where p.contentid=?)
        String sqlCode_add = "com.aspire.dotcard.syncData.dao.DataSyncDAO.syncVCmDeviceResourceAdd.where.add";
        
        String[] mutiSQL_del = new String[contentList.size()];
        String[] mutiSQL_add = new String[contentList.size()];
        Object paras[][] = new String[contentList.size()][1];
        
        for (int i = 0; i < contentList.size(); i++)
        {
            ContentTmp temp = ( ContentTmp ) contentList.get(i);
            mutiSQL_del[i] = sqlCode_del;
            mutiSQL_add[i] = sqlCode_add;
            paras[i][0] = temp.getContentId();
        }
        
        try
        {
            DB.getInstance().executeMutiBySQLCode(mutiSQL_del, paras);
            DB.getInstance().executeMutiBySQLCode(mutiSQL_add, paras);
        }
        catch (DAOException e)
        {
            throw new DAOException("用于逐条处理同步内容适配关系数据时发生异常:", e);
        }
    }
    
    /**
     * 用于用同一个sql语句处理同步内容适配关系数据
     * 
     * @param contentList 应用列表
     * @param type 应用类型
     * @throws DAOException
     */
    public void syncVCmDeviceResourceAddIn(List contentList) throws DAOException
    {
        // delete v_cm_device_resource t
        String sqlCode_del = "com.aspire.dotcard.syncData.dao.DataSyncDAO.syncVCmDeviceResourceAdd.in.del";
        // insert into v_cm_device_resource (select * from ppms_CM_DEVICE_RESOURCE p
        String sqlCode_add = "com.aspire.dotcard.syncData.dao.DataSyncDAO.syncVCmDeviceResourceAdd.in.add";
        StringBuffer sql_del = new StringBuffer();
        StringBuffer sql_add = new StringBuffer();
        StringBuffer in_sql = new StringBuffer();
        try
        {
            sql_del.append(SQLCode.getInstance().getSQLStatement(sqlCode_del));
            sql_add.append(SQLCode.getInstance().getSQLStatement(sqlCode_add));
        }
        catch (DataAccessException e1)
        {
            throw new DAOException("获取数据库操作语句错误:", e1);
        }
        
        // 组合in条件语句
        in_sql.append(" where contentid in (");
        for (int i = 0; i < contentList.size(); i++)
        {
            ContentTmp temp = ( ContentTmp ) contentList.get(i);
            in_sql.append(" '").append(temp.getContentId()).append("',");
        }
        in_sql.deleteCharAt(in_sql.length()-1);
        in_sql.append(")");
        
        // 合并in条件语句至基础语句
        sql_del.append(in_sql);
        sql_add.append(in_sql).append(")");
        
        try
        {
            DB.getInstance().execute(sql_del.toString(), new Object[]{});
            DB.getInstance().execute(sql_add.toString(), new Object[]{});
        }
        catch (DAOException e)
        {
            throw new DAOException("用于用同一个sql语句处理同步内容适配关系数据时发生异常:", e);
        }
    }
    
    /**
     * 将CLOB转成String ,静态方法
     * @param clob 字段
     * @return 内容字串，如果出现错误，返回null
     */
    public final static String clob2String(Clob clob)
    {
		if (clob == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer(65535);//64K
		Reader clobStream = null;//创建一个输入流对象
		try
		{
			clobStream = clob.getCharacterStream();
			char[] b = new char[60000];//每次获取60K
			int i = 0;
			while ((i = clobStream.read(b)) != -1)
			{
				sb.append(b, 0, i);
			}
		} catch (Exception ex)
		{
			sb = null;
		} finally
		{
			try
			{
				if (clobStream != null) clobStream.close();
			} catch (Exception e)
			{
			}
		}
		if (sb == null)
			return "";
		else
			return sb.toString();
	}

    /**
     * 反向在查询应同步的数据。
     * @throws DAOException
     */
    public void reverseSyncContentTmp() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("reverseSyncContentTmp()");
        }

        // 查询出CMS中解除内容与业务关联的数据
        TransactionDB tdb = this.getTransactionDB();
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT6";
        tdb.executeBySQLCode(sqlCode, null);

    }
    
    
    /**
     * 
     * 
     * @param Id ,数据库中产生的序号
     */
    public void refreshContetExt() 
    {

		// 清除本地的
		// delete from t_content_ext t where exists (select 1 from PPMS_V_T_CONTENT_EXT p where t.id = p.id);
		String sqlCode = "SyncData.DataSyncDAO.refreshContetExt().DELETE";
		//insert into  t_content_ext t select *  from  PPMS_V_T_CONTENT_EXT;
		String sqlCodeInsert = "SyncData.DataSyncDAO.refreshContetExt().INSERT";
	
		TransactionDB tdb = null;
		// 进行事务调用
		try
		{
			tdb = this.getTransactionDB();
			tdb.executeBySQLCode(sqlCode, null);
			tdb.executeBySQLCode(sqlCodeInsert, null);
			tdb.commit();
			logger.debug("refreshContetExt success ");
		} catch (Exception e)
		{
			// 回滚
			tdb.rollback();
		} finally
		{
			if (tdb != null)
			{
				tdb.close();
			}
		}
	}
    
    /**
     * 
     *@desc 刷新应用的徽章
     *@author dongke
     *Jun 27, 2012
     */
    public void refreshIteMmark()
	{
		HashMap resultMap = this.getRightNowInActive();
		//清理过期的促销信息的徽章
		//delete from  T_KEY_RESOURCE t where t.keyId = 29 and t.value in ('4','5','10') and exists (select 1 from t_content_ext e where to_char(sysdate-1,'yyyymmdd')=e.dateend and t.tid=e.contentid)
		String deleteItemSqlCode = "SyncData.DataSyncDAO.refreshIteMmark().delete"; 
		try
		{
			DB.getInstance().executeBySQLCode(deleteItemSqlCode, null);
		} catch (DAOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (resultMap != null && resultMap.size() > 0)
		{
		
			ResultSet rs = null;
			Set ts = resultMap.entrySet();
			Iterator it = ts.iterator();
			while (it.hasNext())
			{
				Entry ey = (Entry) it.next();
				String contentid = (String) ey.getKey();
				String type = (String) ey.getValue();
				// select t.value T_KEY_RESOURCE t where t.keyId = 29 and
				// t.tid=?
				
				String itemMmark = null;
				if(type != null && type.equals("1")){
					//折扣
					itemMmark = "5";
				}else if(type != null && type.equals("3")){
					//限时免费
					itemMmark = "4";
				}else if(type != null && type.equals("4")){
					//特约免费
					itemMmark = "10";
				}
				String sqlCode = "SyncData.DataSyncDAO.refreshIteMmark().SELECT";
				Object [] para1 = {contentid};
				try
				{
					rs = DB.getInstance().queryBySQLCode(sqlCode, para1);
					if (rs != null && rs.next())
					{//不为空，并且已经有徽章了
						String value = rs.getString("value");
						if (value != null && (value.equals("4") || value.equals("5")))
						{
							// update T_KEY_RESOURCE t set
							// t.value=?,t.lupdate=sysdate where t.keyId = 29
							// and t.tid=?
							Object [] para = {itemMmark,contentid};
							String sqlCodeUpdate = "SyncData.DataSyncDAO.refreshIteMmark().update";
							DB.getInstance().executeBySQLCode(sqlCodeUpdate, para);
						}
						else
						{
							logger.debug("yunying IteMmark is more importent");
						}

					}
					else
					{//为空，该应用没有打过徽章
						// 插入徽章
						// insert into T_KEY_RESOURCE (tid,keyid,value,lupdate)
						// values(contentid,29,itemMmark,sysdate);
						String sqlCodeInsert = "SyncData.DataSyncDAO.refreshIteMmark().insert";
						Object[] para = {contentid,itemMmark};
						DB.getInstance().executeBySQLCode(sqlCodeInsert, para);
					}
					
					logger.debug(" IteMmark refesh success");
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
    /**
	 * 
	 * @desc 获取当前处于活动中的应用
	 * @author dongke Jun 27, 2012
	 * @return
	 */
    private  HashMap getRightNowInActive(){
    	
    	//select t.contentid,t.type from t_content_ext  t where t.type in (1,3,4) and to_char(sysdate,'yyyymmdd')>= t.datestart and  to_char(sysdate,'yyyymmdd')<= t.dateend
    	String sqlCode = "SyncData.DataSyncDAO.getRightNowInActive().SELECT";
    	HashMap resultMap = null;
         ResultSet rs = null;
         try
         {
         rs = DB.getInstance().queryBySQLCode(sqlCode, null);
         resultMap = new HashMap();
         while(rs.next()){
        	String   contentid = rs.getString("contentid");
        	String type = rs.getString("type");
        	resultMap.put(contentid,type);
        	
         }
         
         }catch(Exception e2){
        	 logger.error("获取数据库操作失败," + e2);
        	 
         }finally{
        	 if(rs != null){
        		 DB.close(rs);
        	 }
         }
         return resultMap;
    }
    
}

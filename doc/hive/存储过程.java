
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
 * ����ͬ��������ҵ�����ݺ��������ݣ�
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
     * ��־����
     */
    JLogger logger = LoggerFactory.getLogger(DataSyncDAO.class);

    private static DataSyncDAO dao = new DataSyncDAO();

    /**
     * ����Ӫ��ǩ�л�ȡ��ǩ��
     */
    private int OP_TAG_NUM = 2;

    /**
     * ��Ӧ�ñ�ǩ�л�ȡ��ǩ��
     */
    private int AP_TAG_NUM = 6;

    /**
     * ��ȡ��ǩ����˳��true��ʾ��Ӫ��ǩ��ǰ false��ʾ���ñ�ǩ�ں�
     */
    private boolean OP_TAG_FIRST = true;

    /**
     * ��ͬ�����ݻ��������б��档key ��ʾ contentid��value ��ʾ ������͵��б�
     */
    //private HashMap ContentDevicesCache;
    
    /**
     * ��ͬ������ƽ̨�����б��档key ��ʾ contentid��value ��ʾ ����ƽ̨���б�
     */
    private HashMap ContentPlatformCache;

    /**
     * ��ѵ�Ӧ�ü��� add by aiyan 2011-11-10
     */
    private Set ContentFeeCache;

    /**
     * MM�ն˻����б�
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
            logger.error("����Ӫ��ǩ�л�ȡ��ǩ�����ܴ��ڴ�Ӧ�ñ�ǩ�л�ȡ��ǩ��");
            OP_TAG_NUM = 2;
            AP_TAG_NUM = 6;
        }
    }

    /**
     * ����ģʽ
     * 
     * @return
     */
    public static DataSyncDAO getInstance()
    {

        return dao;
    }

    /**
     * ֧����������ݿ�����������Ϊ�ձ�ʾ�Ƿ��������͵Ĳ���
     */
    private TransactionDB transactionDB;

    /**
     * ��ȡ��������TransactionDB��ʵ�� ����Ѿ�ָ���ˣ����Ѿ�ָ���ġ����û��ָ�����Լ�����һ����ע���Լ�������ֱ���ò�֧���������͵ļ���
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
     * ��ȡ�������͵�DAOʵ��
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
     * ����ͬ��ǰ������׼������Ҫ�Ƕ�ȡ������Ϣ��
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
            logger.error("��ʼ������ʧ��" + e);
            throw new BOException("��ʼ������ʧ��");
        }

    }
    
    /**
     * ����ͬ��ǰ������׼������Ҫ�Ƕ�ȡ������Ϣ�� 
     * add by aiyan 2012-05-28 ͬ����ʱ������������Ǳ߳���2���Ӧ�ø��¾������ڴ�������˴��޸ġ�
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
            logger.error("��ʼ������ʧ��" + e);
            throw new BOException("��ʼ������ʧ��");
        }

    }
    

    /**
     * ���������Ϣ���ͷ��ڴ档
     */
    public void clearDate() throws BOException
    {
       // ContentDevicesCache = null;
        ContentPlatformCache = null;
        ContentFeeCache = null;
       // deviceMappingCache = null;
    }
    
    //add by aiyan 2012-07-24��
//    public void clearContentDevicesCache(){
//    	ContentDevicesCache = null;
//    }
  //add by dongke 2013-04-17
    public void clearContentPlatformCache(){
    	ContentPlatformCache = null;
    }
    /**
     * ��CMS�е�ҵ��ͬ����PAS���ݿ���
     * 
     */
    public void syncService() throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("syncService()");
        }
        // �����������е��ֶβ�ѯ������
        // дһ��sql��佫һ�ڵ�ҵ����cms�в��������Ϣ�ϳ�һ���ﻯ��ͼ���ɡ�
       // CallableStatement cs = null;
       // Connection conn = DB.getInstance().getConnection();
        try
        {
            // cs = conn.prepareCall("{call dbms_mview.refresh(list =>
            // 'v_service')}");
            // cs.execute();
            this.syncVService();// �ﻯ��ͼ�ĳɱ�added by zhangwei
           // this.syncMOTOExt(); // ͬ��MOTO��Ӧ����չ��Ϣ��  add by dongke 20110408
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
                logger.error("�ر�CallableStatement=ʧ��");
            }*/
        }
    }
    /**
     * ���ͽ������֪ͨ
     * @param content ��������
     * @param phone ���ͺ���
     * @throws DAOException 
     */
    public  void sendMsg(String phone,String content) throws DAOException{
    	String sqlCode="com.aspire.dotcard.syncData.dao.DataSyncDAO.sendMsg";
    	String[] params={phone,content};
    	DB.getInstance().executeBySQLCode(sqlCode, params);
    }
    
    /**
     * ����Ҫͬ���������б�������id������������ʱ����뵽���ݿ�t_syncContent_tmp�С�
     * 
     * @param systime,��ǰϵͳ��ʱ��
     * @param isFull boolean �Ƿ���ȫ��ͬ����true ��ʾȫ��ͬ����false ��ʾ����ͬ��
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
            if (isFull)// ȫ��ͬ��
            {
                paras = null;
                // ������id��������ʱ����뵽��t_syncContent_tmp��.
                sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT1";
            }
            else
            // ����ͬ��
            {
                // �ڱ�t_lastsynctime�в�ѯ�õ��ϴ�ϵͳ����ʱ�䣻
                sqlCode = "SyncData.DataSyncDAO.addContentTmp().SELECT";
                rs = DB.getInstance().queryBySQLCode(sqlCode, null);
                // ����ü�¼������֤�����״�ͬ��,��CMS���ݱ��е���������״̬Ϊ�����͹��ڵĲ�ѯ����.
                if (rs.next())
                {
                    paras = new Object[1];
                    paras[0] = new Timestamp(systime);
                    // ������id��������ʱ����뵽��t_syncContent_tmp��.
                    sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT2";
                }
                else
                // �鲻��ͬ��ʱ����ʹ��ȫ��ͬ����
                {
                    logger.info("t_lastsynctime����û���ϴ�ͬ����¼������ͬ��ִ��ȫ��ͬ����");
                    addContentTmp(systime, true);
                    return;
                }
                // ������ϴ�����ʱ��,����CMS��ȡ����������ʱ�����ϴ�����ʱ���systime֮�������

            }
            TransactionDB tdb = this.getTransactionDB();
            tdb.executeBySQLCode(sqlCode, paras);

        }
        catch (SQLException e)
        {
            throw new DAOException("����Ҫͬ���������б������ʷ�����", e);
        }
        finally
        {
            DB.close(rs);
        }
    }
    
    /**
     * ����Ҫͬ���Ľ���������ʱ���������б�������id������������ʱ����뵽���ݿ�t_syncContent_tmp�С�
     * 
     * @param systime,��ǰϵͳ��ʱ��
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
            // ������id��������ʱ����뵽��t_syncContent_tmp��.
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
     * ��������������ʱ���������в�ѯ�õ���Ҫͬ��������id,����״̬������������ʱ���б�
     * 
     * @return list
     */
    public List getSyncContentTmp() throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getSyncContentTmp()");
        }
        // ����t_synctime_tmp�������ݸ���ʱ��������ѯ�õ���ѯ�����
        String sqlCode = "SyncData.DataSyncDAO.getSyncContentTmp().SELECT";
        ResultSet rs = DB.getInstance().queryBySQLCode(sqlCode, null);
        ContentTmp tmp;
        List list = new ArrayList();
        // �������,��ÿ����¼������ContentTmp�ĸ���������
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
            throw new DAOException("��ȡ��ʱ�������쳣", e);
        }
        finally
        {
            // add by tungke for close
            DB.close(rs);

        }
        // ������ContentTmp����list�з���
        return list;
    }

    /**
     * ������ϵͳִ������ͬ��ʱ����뵽��t_lastsynctime
     * 
     * @param Systime,����ϵͳִ������ͬ����ʱ��
     */
    public void insertSysTime(long systime) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("insertSysTime(" + systime + ")");
        }
        // ��systimeʱ������t_lastsynctime�С�
        String sqlCode = "SyncData.DataSyncDAO.insertSysTime().INSERT";
        Timestamp ts = new Timestamp(systime);
        Object[] paras = { ts };
        TransactionDB tdb = this.getTransactionDB();
        tdb.executeBySQLCode(sqlCode, paras);
    }

    /**
     * ��ͼ��ʼ��Ϊ��
     * 
     */
    public void initViewToTable() throws DAOException
    {

        // ����ͼ����Ϊ��
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
            int intValue = cs.getInt(1); //��ȡ�������ؽ��
            logger.debug("init��v_cm_content result =��"+intValue);
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
            logger.error("init��v_cm_content����", e);
            throw new DAOException("init��v_cm_content����",e);
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

        // ���ݳ�ʼ�����

    }
    
    /**
     * MTKƽ̨Ӧ�üƷѵ��Ӧ��ϵģ��ͬ������
     * 
     */
    public void initMTKViewToTable() throws DAOException
    {

        // ����ͼ����Ϊ��
        String talbeName = "v_mtk_content";
        String fromSql = "select * from PPMS_V_MTK_CONTENT ";

        try
        {
            // ��ͼת��Ϊ���ر�
            this.fullSyncTables(talbeName, fromSql);

            // �����
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
            logger.error("drop��v_mtk_content����" + fromSql, e);
        }

        // ���ݳ�ʼ�����

    }

    /**
     * ɾ��������ʱ������Ӧ�ļ�¼
     * 
     * @param Id ,���ݿ��в��������
     */
    public void delSynccontetTmp(int Id) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("delSynccontetTmp(" + Id + ")");
        }
        // ͨ�������id�ֶ��ҵ���Ӧ�ļ�¼����ɾ��
        String sqlCode = "SyncData.DataSyncDAO.delSynccontetTmp().DELETE";
        Object[] paras = { new Integer(Id) };
        TransactionDB tdb = this.getTransactionDB();
        tdb.executeBySQLCode(sqlCode, paras);
    }

    /**
     * ��������id���������ʹ�cms�в�ѯ�õ����ݶ�������Ҳ��������ݣ����׳��쳣
     * 
     * @param ContentId,����id
     * @param contentType,���ݶ���
     * @return
     * @throws DAOException �������ݿ��쳣�������Ҳ���������
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
                throw new NullPointerException("��CMS�Ҳ��������ݡ�contentId="
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
     * ����ѯ�����Ľ����
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
     * ����content������
     * 
     * @param rs�������
     * @param type,��������
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
     * �������ݻ�������
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
        
        
        //��t_r_gcontent ���� FUNCDESC varchar2(2000)�� Ϊ���ǵ�������CM_CT_APPGAME���˸����¹��ܽ��ܡ����ܵ��ֶΡ�
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
        
       
        

        // ������ά�������ã���ʼֵΪ0
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
     * ����Ӧ����Ϸ��������
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

        // ����flashӦ�ý����Ӧȫ����aac����HTTP URL
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
        	logger.error("setGAppthemeContents->content.setRichAppdesc�д��󣡺�����",e);
        }
        
        // ��������֧�ֵ��ն�����,��Ʒ��
        //setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * ����Ӧ���������չ����
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

        // ����flashӦ�ý����Ӧȫ����aac����HTTP
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
        	logger.error("setGAppthemeContents->content.setRichAppdesc�д��󣡺�����",e);
        }
        
        // �����������֧�ֵ��ն�����,��Ʒ��
       // setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * ����Ӧ���������չ����
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
        	logger.error("setGAppthemeContents->content.setRichAppdesc�д��󣡺�����",e);
        }
        
        // ������Ϸ����֧�ֵ��ն�����,��Ʒ��
        // setDeviceNameAndBrand(content);
        /*
         * String deviceName = this.getDeviceName(content);
         * content.setDeviceName(deviceName);
         */
    }

    /**
     * ��������id�õ��ն��б�
     * 
     * @param contentId
     * @param mathDeviceid ģ�������豸ID zhangweixing add
     * @return ֧�ֻ��͵��ն�id���б�
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
//                    if (ContentFeeCache.contains(contentId))// ֻ����Ѳ�ƥ�䡣
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
     * �жϵ�ǰͬ�������Ƿ�Ϊ�״β���
     * 
     * @param systime ,��ǰϵͳ��ʱ��
     * @return �״β��� ����true
     * @throws DAOException
     * @author biran
     */
    public boolean getFirstSync(long systime) throws DAOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("getFirstSync(" + systime + ")");
        }
        // �ڱ�t_lastsynctime�в�ѯ�õ��ϴ�ϵͳ����ʱ�䣻
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().SELECT";

        ResultSet rs = DB.getInstance().queryBySQLCode(sqlCode, null);
        // ����ü�¼������֤�����״�ͬ��,����������ͬ��������
        try
        {
            rs.next();
            Object o = rs.getObject(1);
            if (o == null)
            {
                // t_lastsynctime���¼������,Ϊ�״�ͬ������
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
        // ���ͬ����־��Ĭ��Ϊ���
        return false;
    }

    /**
     * ��ѯ��CMS������/���������ҵ����������ݡ�
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
         * // ��ѯ��CMS������������ҵ����������� String sqlCode =
         * "SyncData.DataSyncDAO.addContentTmp().INSERT3";
         * 
         * TransactionDB tdb = this.getTransactionDB();
         * tdb.executeBySQLCode(sqlCode, null);
         */

        // ��ѯ��CMS�н��������ҵ�����������
        TransactionDB tdb = this.getTransactionDB();
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT4";
        tdb.executeBySQLCode(sqlCode, null);

    }

    /**
     * deviceName��ʽ��{devicename1},{devicename2}�� brand��ʽ��{brand1},{brand2}��
     * 
     * @param content
     * @throws DAOException
     */
//    private void setDeviceNameAndBrand(GAppContent content) throws DAOException
//    {
//        // ȡ���ݿ���֧�ֵ��ն�����DeviceDAO
//        // HashMap deviceMappingCache = DeviceDAO.getInstance().getDeviceList();
//        // ȡ������֧�ֵ��ն����ͼ��� ��CM_DEVICE_RESOURCE�в�ѯ
//        List matchDeviceId = new ArrayList();
//        List cList = getTerminalList(content.getContentID(), matchDeviceId);
//        // ��¼ƽ̨֧�ֵĻ���
//        // List errorList=new ArrayList();
//        // cList = this.filterList(cList, deviceMappingCache,errorList);
//
//        /*
//         * //��¼PAS��֧��deviceName����־ if(errorList.size() > 0) {
//         * this.logErrorDeviceName(errorList,content); }
//         */
//        // ��ȡ��ǰϵͳ֧�ֵĻ������ƣ���Ʒ�ƣ�������ĸ˳���������
//        Collection deviceNameList = new TreeSet();
//        
//        HashMap brandMap = new HashMap();// ��Ҫȥ�ء�
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
//        content.setMatch_Deviceid(temp);// ģ�������豸ID zhangweixing add
//        String fulldevice = PublicUtil.filterMbrandEspecialChar(list2String(deviceNameList));
//        content.setFulldeviceName(fulldevice);// add 20101108
//    }

    /* *//**
             * Ŀǰ����Ҫ����֤�ˡ� �������ڵ�һ��list���Ҵ����ڵڶ���list�����ݷ���
             * 
             * @param one
             * @param deviceMap �ն˿����еĻ��ͼ���
             * @return
             */
    /*
     * private List filterList(List one,HashMap deviceMap,List errorList) { List
     * result = new ArrayList(); Object temp = ""; for(int i = 0,size =
     * one.size(); i < size; i++) { temp=one.get(i); DeviceVO
     * vo=(DeviceVO)deviceMap.get(temp); if(vo!=null) { result.add(vo); }else {
     * //��¼��PAS���в����ڵ�deviceName errorList.add(temp); } }
     * 
     * return result; }
     */
    /**
     * ��list�е�����ƴװΪ {devicename},{devicename}�� ����ʽ
     * 
     * @param list ����deviceName�ļ���
     * @return ���磺{devicename1},{devicename2}�� ���ַ���
     */
    private String list2String(Collection collection)
    {
        StringBuffer sb = new StringBuffer();
        Iterator iterator = collection.iterator();
        boolean dotFlag = false;// ��һ�β���Ҫ���붺��
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
     * ������Դid�õ��ļ����ݵ�url
     * 
     * @param resourceId,��Դid return String
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
     * ������Դid�õ�ͼƬ��url
     * 
     * @param resourceId,��Դid return String
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
     * ������Դid�õ���Ƶ���ݵ�url
     * 
     * @param resourceId,��Դid return String
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
     * ��ѯ���ݶ�Ӧҵ���ҵ��ͨ������ liyouli add patch 134
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
     * ��������ID�õ�֧�ֵ�����ƽ̨���ϣ���{}���߽磬���ŷָ�
     * 
     * @param contentID
     * @return
     * @throws DAOException
     */
   /* private String getPlatformByContentID(String contentID) throws DAOException
    {
        // ���������л�ȡkjavaƽ̨���͵���չ����
        String platformExt = "";
        try
        {
            platformExt = Config.getInstance()
                                .getModuleConfig()
                                .getItemValue("platformExt");
        }
        catch (Exception e1)
        {
            logger.error("���������л�ȡkjavaƽ̨���͵���չ�����ǳ���");
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
        // ͳ��֧�ֵ�ƽ̨����
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
     * @desc ͬ�������ϵ��
     * @author tungke Apr 5, 2011
     * @throws BOException
     */
    public void syncVCmDeviceResource() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("ͬ���� V_CM_DEVICE_RESOURC");
        }
        try
        {
        	String sytype = Config.getInstance()
            .getModuleConfig()
            .getItemValue("DEVICE_SYN_S");//ͬ��״̬��1�����ô洢���̣�2������JAVA����ͬ��
        	String dbsytype = Config.getInstance()
            .getModuleConfig()
            .getItemValue("DEVICE_SYN_TYPE");//�洢����ͬ������;1��������0 ��ȫ��
        	
        	int dbt = 1;//Ĭ������
        	if(dbsytype != null  && dbsytype.equals("0")){
        		//�洢����ȫ��
        		dbt = 0;
        	}
        	if(sytype != null && sytype.equals("1")){
        		//���ô洢����ͬ��������ȫ����dbtȡֵ��1��������0��ȫ����
        		 CallableStatement cs = null;
        	        Connection conn = DB.getInstance().getConnection();
        		cs = conn.prepareCall("{?=call f_ppms_cm_device_pid(?,?)}"); 
        		
        		cs.setInt(2,dbt);
        		cs.setString(3,"java call");
        		
                cs.registerOutParameter(1, Types.INTEGER);  
                cs.execute();  
                int intValue = cs.getInt(1); //��ȡ�������ؽ��
                logger.debug("init��v_cm_content result =��"+intValue);
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
            throw new BOException("ͬ��V_CM_DEVICE_RESOURC �����", e);
        }catch (Exception e2)
        {
        	
        	e2.printStackTrace();
            throw new BOException("�洢���� ͬ��V_CM_DEVICE_RESOURC �����", e2);
        }
    }

    public void syncVService() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("ͬ���� V_SERVICE");
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
            throw new BOException("ͬ��V_SERVICE �����", e);
        }
    }
    
    /**
     * 
     *@desc ͬ��HTCӦ����չ�� 
     * @throws BOException
     */
    public void syncHTCExt() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("ͬ���� CM_CONTENT_HTCEXT");
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
            throw new BOException("ͬ��CM_CONTENT_HTCEXT �����", e);
        }
    }

    /**
     * 
     *@desc ͬ��MOTOӦ����չ�� 
     *@author dongke
     *Apr 8, 2011
     * @throws BOException
     */
    public void syncMOTOExt() throws BOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("ͬ���� CM_CONTENT_MOTOEXT");
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
            throw new BOException("ͬ��CM_CONTENT_MOTOEXT �����", e);
        }
    }
    /**
     * ͨ��contentID�������е���Ʒ��Ϣ,��Щ��Ϣ��Ҫ����ͬ���ʼ����չʾ��
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
     * ��ȡ��Ҫͬ�����������ݵ�ƽ̨ƥ����Ϣ�� ����HashMap��key ��ʾ contentid��value ��ʾ ����ƽ̨���б�
     * 
     * add by aiyan 2012-05-28 ͬ����ʱ������������Ǳ߳���2���Ӧ�ø��¾������ڴ�������˴��޸ġ�
     * @return HashMap
     * @throws DAOException
     */
    public HashMap getAllSyncContentPlatform(Set contentIdSet) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("��ȡ��Ҫͬ�����������ݵ�ƽ̨ƥ����Ϣ");
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
							// ��������ƽ̨��׷�ӽ�ȥ
							platForms.append(",{" + platform + "}");
						} else {
							logger.error("�ظ�ƽ̨��Ϣ");
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
     * ��ȡ��Ҫͬ�����������ݵĻ���ƥ����Ϣ�� ����HashMap��key ��ʾ contentid��value ��ʾ ������͵��б�
     * 
     * add by aiyan 2012-05-28 ͬ����ʱ������������Ǳ߳���2���Ӧ�ø��¾������ڴ�������˴��޸ġ�
     * @return HashMap
     * @throws DAOException
     */
    public HashMap getAllSyncContentDevices(Set contentIdSet) throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("��ȡ��Ҫͬ�����������ݵĻ���ƥ����Ϣ");
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
                if (proSubmitDate == null)// ��ppms ȷ�ϣ���ֵ������Ϊ��
                {
                    continue;
                }
                vo.setProSubmitDate(proSubmitDate);

                DeviceResourceVO oldVO = ( DeviceResourceVO ) deviceMap.get(vo.getDeviceId());
                if (oldVO != null)// ˵���Ѿ�����
                {
                    if (vo.getProSubmitDate().after(oldVO.getProSubmitDate()))// ��ȡ���µĳ���Ļ��������ϵ��
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
     * ��ȡ��Ҫͬ�����������ݵĻ���ƥ����Ϣ�� ����HashMap��key ��ʾ contentid��value ��ʾ ������͵��б�
     * 
     * @return HashMap
     * @throws DAOException
     */
//    public HashMap getAllSyncContentDevices() throws DAOException
//    {
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("��ȡ��Ҫͬ�����������ݵĻ���ƥ����Ϣ");
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
//                if (proSubmitDate == null)// ��ppms ȷ�ϣ���ֵ������Ϊ��
//                {
//                    continue;
//                }
//                vo.setProSubmitDate(proSubmitDate);
//
//                DeviceResourceVO oldVO = ( DeviceResourceVO ) deviceMap.get(vo.getDeviceId());
//                if (oldVO != null)// ˵���Ѿ�����
//                {
//                    if (vo.getProSubmitDate().after(oldVO.getProSubmitDate()))// ��ȡ���µĳ���Ļ��������ϵ��
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
            logger.debug("��ȡ��Ҫͬ�����������ݵ�����ƽ̨ƥ����Ϣ");
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
							// ��������ƽ̨��׷�ӽ�ȥ
							platForms.append(",{" + platform + "}");
						} else {
							logger.error("�ظ�ƽ̨��Ϣ");
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
     * ��ȡ��Ҫͬ�����������ݵ��ʷ��Ƿ���ѡ�//��Ҫ���������Ϣ����ģ������
     * 
     * @return hashMap key ��ʾcontentId value ��ʾ��Ӧ���Ƿ����
     * @throws DAOException
     */
    public Set getAllSyncContentFee() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("��ȡ��Ҫͬ�����������ݵ��ʷ��Ƿ������Ϣ");
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
                
                //add by aiyan 2011-11-10 ��Ϊ������Map���岻�󣬸���Set
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
     * ���б����
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
        
        logger.debug("���б������sqlΪ��" + sql);
        try
        {
            // 1 ���б����
            DB.getInstance().execute(sql, null);
        }
        catch (DAOException e)
        {
            logger.error("���������" + e);
            throw new BOException("���������", e);
        }
    }

    /**
     * ȫ��ͬ�������ݣ�ͨ����������Ȼ�����ʵ��
     * 
     * @param tableName
     * @fromSql ������Դ��sql
     * @throws BOException ͨ�����̳����쳣
     */
    public void fullSyncTables(String tableName, String fromSql,
                               String indexTargetCon) throws BOException
    {

        logger.info("��ʼͬ���� tableName:" + tableName);
        String timestamp = PublicUtil.getCurDateTime("HHSSS");
        String tempTableName = tableName + timestamp;// ��ֹͬʱ�����������
        String backupTableName = tableName + timestamp + "_bak";

        String createTempSql = "create table " + tempTableName + " as "
                               + fromSql;// ������ʱ��
        String backupTableSql = "alter table " + tableName + " rename to "
                                + backupTableName;// ���ݾɱ�
        String replaceSql = "alter table " + tempTableName + " rename to "
                            + tableName;// ����ʱ����Ϊ��ʽ��

        String dropBackupSql = "drop table " + backupTableName;// ɾ�����ݱ�
        String dropTempSql = "drop table " + tempTableName;// ɾ����ʱ��
        String revertBackupTableSql = "alter table " + backupTableName
                                      + " rename to " + tableName;// ��ԭ���ݱ�

        logger.info("��ʼ������ʱ��:" + tempTableName);
        try
        {
            DB.getInstance().execute(createTempSql, null);
            logger.info("��ʼ������ʱ������:" + "create index IDX_" + tempTableName
                        + " on " + tempTableName + " (" + indexTargetCon + ")");
            DB.getInstance().execute("create index IDX_" + tempTableName
                                                     + " on " + tempTableName
                                                     + " (" + indexTargetCon
                                                     + ")",
                                     null);

        }
        catch (DAOException e)
        {
            throw new BOException("������ʱ�����" + tempTableName, e);
        }

        logger.info("��ʼ���ݾɱ� :" + backupTableName);
        try
        {
            DB.getInstance().execute(backupTableSql, null);
        }
        catch (DAOException e)
        {

            throw new BOException("���ݱ�:" + tableName + "����" + tempTableName, e);
        }

        logger.info("��ʼ������ʱ����Ϊ��ʽ��:" + tempTableName);
        try
        {
            DB.getInstance().execute(replaceSql, null);

        }
        catch (DAOException e)
        {
            // ������ʱ��ʧ�ܣ���Ҫ��ԭ���ݱ���ɾ����ʱ��
            try
            {
                DB.getInstance().execute(revertBackupTableSql, null);// ��ԭ���ݱ�
                DB.getInstance().execute(dropTempSql, null);// ��Ҫɾ����ʱ��
            }
            catch (DAOException e1)
            {
                logger.error(e1);
            }
            throw new BOException("������ʱ��������" + tempTableName, e);
        }
        logger.info("ɾ�����ݱ� " + backupTableName);
        try
        {
            DB.getInstance().execute(dropBackupSql, null);
        }
        catch (DAOException e)
        {
            logger.error("ɾ�����ݱ�ʧ��:" + backupTableName);
        }// ɾ�����ݱ�
    }

    /**
     * ȫ��ͬ�������ݣ�ͨ����������Ȼ�����ʵ��
     * 
     * @param tableName
     * @fromSql ������Դ��sql
     * @throws BOException ͨ�����̳����쳣
     */
    public void fullSyncTables(String tableName, String fromSql)
                    throws BOException
    {

        logger.info("��ʼͬ���� tableName:" + tableName);
        String timestamp = PublicUtil.getCurDateTime("HHSSS");
        String tempTableName = tableName + timestamp;// ��ֹͬʱ�����������
        String backupTableName = tableName + timestamp + "_bak";

        String createTempSql = "create table " + tempTableName + " as "
                               + fromSql;// ������ʱ��
        String backupTableSql = "alter table " + tableName + " rename to "
                                + backupTableName;// ���ݾɱ�
        String replaceSql = "alter table " + tempTableName + " rename to "
                            + tableName;// ����ʱ����Ϊ��ʽ��

        String dropBackupSql = "drop table " + backupTableName;// ɾ�����ݱ�
        String dropTempSql = "drop table " + tempTableName;// ɾ����ʱ��
        String revertBackupTableSql = "alter table " + backupTableName
                                      + " rename to " + tableName;// ��ԭ���ݱ�

        logger.info("��ʼ������ʱ��:" + tempTableName);
        try
        {
            DB.getInstance().execute(createTempSql, null);
        }
        catch (DAOException e)
        {
            throw new BOException("������ʱ�����" + tempTableName, e);
        }

        logger.info("��ʼ���ݾɱ� :" + backupTableName);
        try
        {
            DB.getInstance().execute(backupTableSql, null);
        }
        catch (DAOException e)
        {

            throw new BOException("���ݱ�:" + tableName + "����" + tempTableName, e);
        }

        logger.info("��ʼ������ʱ����Ϊ��ʽ��:" + tempTableName);
        try
        {
            DB.getInstance().execute(replaceSql, null);

        }
        catch (DAOException e)
        {
            // ������ʱ��ʧ�ܣ���Ҫ��ԭ���ݱ���ɾ����ʱ��
            try
            {
                DB.getInstance().execute(revertBackupTableSql, null);// ��ԭ���ݱ�
                DB.getInstance().execute(dropTempSql, null);// ��Ҫɾ����ʱ��
            }
            catch (DAOException e1)
            {
                logger.error(e1);
            }
            throw new BOException("������ʱ��������" + tempTableName, e);
        }
        logger.info("ɾ�����ݱ� " + backupTableName);
        try
        {
            DB.getInstance().execute(dropBackupSql, null);
        }
        catch (DAOException e)
        {
            logger.error("ɾ�����ݱ�ʧ��:" + backupTableName);
        }// ɾ�����ݱ�
    }

    /**
     * ��ȡӦ�ñ�ǩ
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
                logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
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
     * ��ȡ��Ӫ��Ӧ�ñ�ǩ
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
     * ��ȡӦ�ñ�ǩ
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
                logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
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
     * ��ȡӦ�ñ�ǩ
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
                logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
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
     * �������ݵ�����,ͨ���� t_pps_comment_grade ����õ�
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
            logger.error("�������������쳣", e);
        }

    }

    /**
     * ��ѯ����Ŀ¼�µ���Ʒ
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
            logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
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
     *@desc  ��ȡMOTOӦ�õ�MOTO��һ������Ͷ�����������
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
                logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
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
	 * @desc ��ȡHTCӦ�õ�HTC��һ������Ͷ�����������
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
			logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
			throw new DAOException(ex);
		} finally {
			DB.close(rs);
		}
		return res;
	}
            
/**
 * 
 * @desc ��ȡӦ�õĶ�����������
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
            logger.error("��ȡ��ǩ���ݿ����ʧ��," + ex);
            throw new DAOException(ex);
        }
        finally
        {
            DB.close(rs);
        }
        return appCateName;
    }

    /**
     * ����ͬ�������
     * 
     * @param list
     * @throws DAOException
     */
    public void insertSynResult(List[] list) throws DAOException
    {
        logger.debug("DataSyncDAO.insertSynResult begin!");
        List updateList = list[0];// �ɹ����¡�
        List addList = list[1];// �ɹ�����
        List deleteList = list[2];// �ɹ�����
        List errorList = list[3];// ʧ��ͬ��
        PublicUtil.removeDuplicateWithOrder(errorList);// ȥ���ظ���¼ͬ��ʧ�ܵ����⡣
        int size = updateList.size() + addList.size() + deleteList.size()
                   + errorList.size();
        logger.debug("����" + size + "����¼�����£�" + updateList.size() + ",����:"
                     + addList.size() + ",���ߣ�" + deleteList.size() + ",ɾ��:"
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
                logger.error("����ͬ�����ݽ���쳣", e);
            }
        }
        logger.debug("DataSyncDAO.insertSynResult end!");
    }

    /**
     * �����������������ϵ��ʱ��ɾ����ǰdevice������������
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
            throw new DAOException("DataSyncDAO.deleteDeviceByContentid() ��ȡsql������",
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
     * �����������������ϵ��ʱ��������״̬������device��������
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
            throw new DAOException("DataSyncDAO.insertDeviceByContentid() ��ȡsql������",
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
     * ���ڴ洢����Ӧ�ü�¼��ʷ����
     * 
     * @param contentId Ӧ��id
     * @param type Ӧ������
     * @throws DAOException
     */
    public void addContentIdHis(String contentId, String type)
                    throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.insertDeviceByContentidList()");
        }

        // insert into t_r_gcontent_his values (��,to_char(sysdate,'yyyy-mm-dd
        // hh24:mi:ss'),��)
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
     * ����ͬ�����������ϵ����
     * 
     * @param contentList Ӧ���б�
     * @param type Ӧ������
     * @throws DAOException
     */
    public void syncVCmDeviceResourceAdd(List contentList)
                    throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("DataSyncDAO.syncVCmDeviceResourceAdd()");
        }
        
        // �����¼������900����������
        if(contentList.size()>900)
        {
            syncVCmDeviceResourceAddWhere(contentList);
        }
        // �����¼��С��900����0���õ���sql��䴦��
        else if(contentList.size()>0)
        {
            syncVCmDeviceResourceAddIn(contentList);
        }
    }
    
    /**
     * ������������ͬ�����������ϵ����
     * 
     * @param contentList Ӧ���б�
     * @param type Ӧ������
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
            throw new DAOException("������������ͬ�����������ϵ����ʱ�����쳣:", e);
        }
    }
    
    /**
     * ������ͬһ��sql��䴦��ͬ�����������ϵ����
     * 
     * @param contentList Ӧ���б�
     * @param type Ӧ������
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
            throw new DAOException("��ȡ���ݿ����������:", e1);
        }
        
        // ���in�������
        in_sql.append(" where contentid in (");
        for (int i = 0; i < contentList.size(); i++)
        {
            ContentTmp temp = ( ContentTmp ) contentList.get(i);
            in_sql.append(" '").append(temp.getContentId()).append("',");
        }
        in_sql.deleteCharAt(in_sql.length()-1);
        in_sql.append(")");
        
        // �ϲ�in����������������
        sql_del.append(in_sql);
        sql_add.append(in_sql).append(")");
        
        try
        {
            DB.getInstance().execute(sql_del.toString(), new Object[]{});
            DB.getInstance().execute(sql_add.toString(), new Object[]{});
        }
        catch (DAOException e)
        {
            throw new DAOException("������ͬһ��sql��䴦��ͬ�����������ϵ����ʱ�����쳣:", e);
        }
    }
    
    /**
     * ��CLOBת��String ,��̬����
     * @param clob �ֶ�
     * @return �����ִ���������ִ��󣬷���null
     */
    public final static String clob2String(Clob clob)
    {
		if (clob == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer(65535);//64K
		Reader clobStream = null;//����һ������������
		try
		{
			clobStream = clob.getCharacterStream();
			char[] b = new char[60000];//ÿ�λ�ȡ60K
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
     * �����ڲ�ѯӦͬ�������ݡ�
     * @throws DAOException
     */
    public void reverseSyncContentTmp() throws DAOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("reverseSyncContentTmp()");
        }

        // ��ѯ��CMS�н��������ҵ�����������
        TransactionDB tdb = this.getTransactionDB();
        String sqlCode = "SyncData.DataSyncDAO.addContentTmp().INSERT6";
        tdb.executeBySQLCode(sqlCode, null);

    }
    
    
    /**
     * 
     * 
     * @param Id ,���ݿ��в��������
     */
    public void refreshContetExt() 
    {

		// ������ص�
		// delete from t_content_ext t where exists (select 1 from PPMS_V_T_CONTENT_EXT p where t.id = p.id);
		String sqlCode = "SyncData.DataSyncDAO.refreshContetExt().DELETE";
		//insert into  t_content_ext t select *  from  PPMS_V_T_CONTENT_EXT;
		String sqlCodeInsert = "SyncData.DataSyncDAO.refreshContetExt().INSERT";
	
		TransactionDB tdb = null;
		// �����������
		try
		{
			tdb = this.getTransactionDB();
			tdb.executeBySQLCode(sqlCode, null);
			tdb.executeBySQLCode(sqlCodeInsert, null);
			tdb.commit();
			logger.debug("refreshContetExt success ");
		} catch (Exception e)
		{
			// �ع�
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
     *@desc ˢ��Ӧ�õĻ���
     *@author dongke
     *Jun 27, 2012
     */
    public void refreshIteMmark()
	{
		HashMap resultMap = this.getRightNowInActive();
		//������ڵĴ�����Ϣ�Ļ���
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
					//�ۿ�
					itemMmark = "5";
				}else if(type != null && type.equals("3")){
					//��ʱ���
					itemMmark = "4";
				}else if(type != null && type.equals("4")){
					//��Լ���
					itemMmark = "10";
				}
				String sqlCode = "SyncData.DataSyncDAO.refreshIteMmark().SELECT";
				Object [] para1 = {contentid};
				try
				{
					rs = DB.getInstance().queryBySQLCode(sqlCode, para1);
					if (rs != null && rs.next())
					{//��Ϊ�գ������Ѿ��л�����
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
					{//Ϊ�գ���Ӧ��û�д������
						// �������
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
	 * @desc ��ȡ��ǰ���ڻ�е�Ӧ��
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
        	 logger.error("��ȡ���ݿ����ʧ��," + e2);
        	 
         }finally{
        	 if(rs != null){
        		 DB.close(rs);
        	 }
         }
         return resultMap;
    }
    
}

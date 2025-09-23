/***********************************************************************
 * Module:  com.crux.util.SQLUtil
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 10:58:09 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.config.Config;
import com.crux.common.model.DTO;
import com.crux.common.model.UserSession;
import com.crux.common.model.RecordAudit;
import com.crux.common.parameter.Parameter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.io.OutputStream;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;

public class SQLUtil {
   protected Connection connection;
   protected PreparedStatement preparedStatement;
   protected UserSession userSession;
   public String stAttr1;
   
   public static ResultSet result;

   private final static transient LogManager logger = LogManager.getInstance(SQLUtil.class);

   public static transient int insertCount = 0;
   public static transient int updateCount = 0;

   public final static transient boolean useLockProtect = Parameter.readBoolean("SYS_LOCK_PROTECT", false);
   private String dsOverride;
   private String stQuery;

   public static HashMap dn;
   private static HashMap qc;
   private static ArrayList triggers = new ArrayList();
   private boolean hasCLOB;
   private boolean hideLogger = false;

   public SQLUtil() throws Exception {
      userSession = UserSessionMgr.getInstance().getUserSession();
      if ((userSession==null) || (userSession.getStUserID()==null)) userSession=(UserSession) SessionManager.getInstance().getSession();
      dsOverride = "PRIMARY";
   }

   public SQLUtil(String stDataSourceName) throws Exception {
      this();
      dsOverride = stDataSourceName;
   }

   public Connection getConnection() throws Exception {
      if (connection == null) {
         if (dsOverride != null)
            connection = ConnectionCache.getInstance().getConnection(dsOverride);
         else
            connection = ConnectionCache.getInstance().getConnection();
      }

      return connection;

      //logger.logDebug("getConnection: CN = "+connection);
   }

   public String getDriverName() throws Exception {
      if (dn==null) dn=new HashMap();
      String dvn = (String)dn.get(dsOverride);
      if (dvn==null) {
         getConnection();
         dvn = connection.getMetaData().getDriverName()+" "+connection.getMetaData().getDriverVersion();

         logger.logDebug("getDriverName: "+dvn);
         logger.logDebug("Database : "+connection.getMetaData().getDatabaseProductName()+" "+connection.getMetaData().getDatabaseProductVersion());

         dvn = dvn.toUpperCase();

         if (dvn.indexOf("ORACLE")>=0) dvn="ORA";
         if (dvn.indexOf("POSTGRE")>=0) dvn="PGR";

         dn.put(dsOverride, dvn) ;
      }
      return dvn;
   }

   public PreparedStatement setQuery(String stSQL) throws Exception {
      getConnection();

      stSQL = getSQLCache(stSQL, getDriverName());

      stQuery = stSQL;
      if(!hideLogger) logger.logDebug("setQuery: "+stSQL); //tes mark logger
      //if (preparedStatement!=null) throw new RuntimeException("You must call reset !");
      //todo: i'm mark it to make the prepareStatement available for rs.last()
       //preparedStatement = connection.prepareStatement(stSQL);
      preparedStatement = connection.prepareStatement(stSQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      return preparedStatement;
   }

   private String getSQLCache(String stSQL, String driverName) {
      if (stSQL.indexOf('[')<0) return stSQL;

      if (qc==null)
         qc = new HashMap();

      HashMap qx = (HashMap)qc.get(stSQL);

      if (qx==null) {
         qx = new HashMap();
         qc.put(stSQL,qx);

         final String[] sqx = stSQL.split("[\\]]");

         for (int i = 0; i < sqx.length; i++) {
            String s1 = sqx[i];
            final String[] s2 = s1.split("[\\[]");
            qx.put(s2[0],s2[1]);
             if(!hideLogger) logger.logDebug("getSQLCache: stored:("+s2[0]+","+s2[1]+")"); //tes mark logger
         }
      }

      return (String) qx.get(driverName);
   }

   public PreparedStatement setParam(int iIndex, Date value) throws Exception {
      if (value == null)
         preparedStatement.setTimestamp(iIndex, null);
      else
         preparedStatement.setTimestamp(iIndex, new Timestamp(value.getTime()));

      return preparedStatement;
   }

   public PreparedStatement setParamCLOB(int iIndex, String value) throws Exception {
       if(!hideLogger) logger.logDebug("setParamCLOB: "+value); //tes mark logger

      //oracle.sql.CLOB.createTemporary

      Clob clob = (Clob) oracle.sql.CLOB.empty_lob();

      if (value!=null)
         ((oracle.sql.CLOB) clob).putString(1, value);

      preparedStatement.setClob(iIndex, clob);

      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, Double value) throws Exception {
      if (value == null)
         preparedStatement.setNull(iIndex, Types.NUMERIC);
      else
         preparedStatement.setDouble(iIndex, value.doubleValue());
      
      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, Integer value) throws Exception {
      if (value == null)
         preparedStatement.setNull(iIndex, Types.NUMERIC);
      else
         preparedStatement.setInt(iIndex, value.intValue());

      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, Long value) throws Exception {
      if (value == null)
         preparedStatement.setNull(iIndex, Types.INTEGER);
      else
         preparedStatement.setInt(iIndex, value.intValue());
     
      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, String value) throws Exception {
      preparedStatement.setString(iIndex, value);
 
      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, BigDecimal value) throws Exception {
      preparedStatement.setBigDecimal(iIndex, value);
    
      return preparedStatement;
   }

   public PreparedStatement setParam(int iIndex, Object value) throws Exception {
      if (value instanceof Date) return setParam(iIndex, (Date) value);
      else if (value instanceof Long) return setParam(iIndex, (Long) value);
      else if (value instanceof Integer) return setParam(iIndex, (Integer) value);
      else if (value instanceof BigDecimal) return setParam(iIndex, (BigDecimal) value);
      else if (value instanceof CLOBWrapper) return setParamCLOB(iIndex, (String) ((CLOBWrapper) value).o);
      else return setParam(iIndex, (String) value);
   }

   public void release() {
      try {
         releaseResource();
         
         if (connection != null) {
            connection.close();
            connection = null;
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   protected DTO reload(DTO dto ) throws Exception {

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      final StringBuffer whereList = new StringBuffer();
      final ArrayList paramWhere = new ArrayList();

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "select * from "+stTableName+" where "+whereList.toString();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(i+1, paramWhere.get(i));
         }

         final ResultSet RS = PS.executeQuery();

         final DTOList l = ListUtil.getDTOListFromResultSet(RS, dto.getClass(),0,0);

         if (l.size()==1) {
            return (DTO) l.get(0);
         }

         return null;
      }
      finally {
         releaseResource();
      }
   }

   public void insert(DTO dto) throws Exception {
      insertCount++;

      dto.beforeInsert();

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      UserSession us = dto.getUserSession();

      if (us==null) us = this.userSession;

      final StringBuffer fieldList = new StringBuffer();
      final StringBuffer valueList = new StringBuffer(); 

      if (dto instanceof  RecordAudit) {
         if (us != null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }else if (us == null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }
         else if (dto.isEnableStoreSync() ) {
            fieldList.append("CREATE_DATE,CREATE_WHO,CHANGE_DATE,CHANGE_WHO");
            valueList.append("?,?,?,?");
         }
      }

      boolean hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (!df.isDatabaseField()) continue;

         //if (df.isReadOnly())
         //   if (!df.isPrimaryKey()) continue;

         if (fieldList.length()>0) {
            fieldList.append(",");
            valueList.append(",");
         }

         if (df.isCLOB()) {
            hasCLOB=true;
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("EMPTY_CLOB()");
         } else {
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("?");
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "INSERT INTO "+stTableName+"("+fieldList.toString()+") VALUES("+valueList.toString()+")";

      getConnection();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         it = fields.values().iterator();

         int i=1;

         if ((us != null) && (dto instanceof RecordAudit)) {
            setParam(i++, us.getDtTransactionDate());
            setParam(i++, us.getStUserID());
         }

         if ((us == null) && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
         }

         if (dto.isEnableStoreSync() && (dto instanceof RecordAudit)) {
            setParam(i++, dto.getDtCreateDate());
            setParam(i++, dto.getStCreateWho());
            setParam(i++, dto.getDtChangeDate());
            setParam(i++, dto.getStChangeWho());
         }

         while (it.hasNext()) {
            DTOField df = (DTOField) it.next();

            if (!df.isDatabaseField()) continue;

            if (df.isCLOB()) continue;

            //if (df.isReadOnly())
            //   if (!df.isPrimaryKey()) continue;

            final Object o = df.getGetter().invoke(dto, null);

            if (df.isCLOB2()) {
               setParamCLOB(i++, (String)o);
            };

            setParam(i++, o);
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            //logger.logDebug("insert: no record updated");
            throw new SQLException("Failed to insert record into "+stTableName+", probably caused by trigger failure");
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("INSERT",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error inserting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }

   private void updateCLOB(DTO dto) throws Exception {

      /*
statement = connection.prepareStatement("SELECT MSG_DETAIL FROM EAI_ADMIN.SO_LOG WHERE SO_ID = ? FOR UPDATE");
            statement.setLong(1, x);
            resultSet = statement.executeQuery();
            resultSet.next();
            CLOB clob = ((OracleResultSet) resultSet).getCLOB(1);

            OutputStream asciiOutputStream = clob.getAsciiOutputStream();
            asciiOutputStream.write(s.toString().getBytes());
            asciiOutputStream.close();

            connection.commit();

      */

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();


      final StringBuffer setList = new StringBuffer();

      final StringBuffer whereList = new StringBuffer();

      final ArrayList paramSet = new ArrayList();
      final ArrayList paramWhere = new ArrayList();

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
            else if (!df.isReadOnly()) {
               if (!df.isCLOB()) continue;
               if (setList.length()>0) setList.append(",");
               setList.append(df.getStDatabaseFieldName());
               paramSet.add(df.getGetter().invoke(dto,null));
            }
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      final String stTableName = (String) tnf.get(null);

      String qry = "SELECT "+setList.toString()+" FROM "+stTableName+" WHERE "+whereList.toString()+" FOR UPDATE";

      final PreparedStatement PS = setQuery(qry);

      try {
         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(i+1, paramWhere.get(i));
         }


         ResultSet RS = PS.executeQuery();

         RS.next();

         try {
            for (int i = 0; i < paramSet.size(); i++) {
               Object o = paramSet.get(i);

               if (o==null) continue;

               if (o!=null) o = String.valueOf(o);

               String s = (String)o;

                if(!hideLogger) logger.logDebug("updateCLOB: "+s); //tes mark logger

               CLOB clob = ((OracleResultSet) RS).getCLOB(1);

               OutputStream str = clob.getAsciiOutputStream();

//               Clob CL = RS.getClob(i+1);

//               OutputStream str = CL.setAsciiStream(0);

               try {
                  str.write(s.getBytes());
               } finally {
                  str.close();
               }

            }
         } finally {
            RS.close();
         }

      } finally {
         PS.close();
      }
   }

   public void setParams(ArrayList par) throws Exception {

      for (int i = 0; i < par.size(); i++) {
         Object o = (Object) par.get(i);

         setParam(i+1,o);
      }
   }

   private static class CLOBWrapper {
      private Object o;

      public CLOBWrapper(Object _o) {
         o = _o;
      }
   }

   public int update(DTO dto) throws Exception {
      updateCount++;

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());
      
      long t = System.currentTimeMillis();

      Iterator it = fields.values().iterator();

      final StringBuffer setList = new StringBuffer();

      final StringBuffer whereList = new StringBuffer();

      final ArrayList paramSet = new ArrayList();
      final ArrayList paramWhere = new ArrayList();

      UserSession us = dto.getUserSession();

      if (us==null) us=this.userSession;

      if ((dto instanceof  RecordAudit) && (!dto.isEnableStoreSync())) {
         if (us==null) throw new Exception("UserSession not attached");
      }

      if ((us != null) && (dto instanceof RecordAudit) && (!dto.isEnableStoreSync())) {
         setList.append("CHANGE_DATE = ?, CHANGE_WHO = ?");
         paramSet.add(us.getDtTransactionDate());
         paramSet.add(us.getStUserID());
      }

      if ((dto.isEnableStoreSync()) && (dto instanceof RecordAudit)) {
         setList.append("CREATE_DATE = ?, CREATE_WHO = ?, CHANGE_DATE = ?, CHANGE_WHO = ?");
         paramSet.add(dto.getDtCreateDate());
         paramSet.add(dto.getStCreateWho());
         paramSet.add(dto.getDtChangeDate());
         paramSet.add(dto.getStChangeWho());
      }

      hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
            else if (!df.isReadOnly()) {
               if (df.isCLOB()) {
                  hasCLOB=true;
                  continue;
               }
               if (setList.length()>0) setList.append(",");
               setList.append(df.getStDatabaseFieldName()+"=?");

               if (df.isCLOB2()) {
                  paramSet.add(new CLOBWrapper(df.getGetter().invoke(dto,null)));
               } else {
                  paramSet.add(df.getGetter().invoke(dto,null));
               }

            }
         }
      }

	  /*
      if (useLockProtect && (dto.isEnableLockProtect()) && (dto.getDtCreateDate()!=null) && (!dto.isEnableStoreSync()))
         if (whereList.length()>0) {
            if (dto instanceof RecordAudit)
             {
               if (dto.getDtChangeDate() != null) {
                  //whereList.append(" and (date_trunc('seconds', change_date)=date_trunc('seconds', timestamp ?)) ");
                  whereList.append(" and (change_date=?) ");
                  paramWhere.add(dto.getDtChangeDate());
               } else {
                  whereList.append(" and change_date is null ");
               }
            }
         }*/

      final Field tnf = dto.getClass().getField("tableName");


      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final StringBuffer sqlSZ = new StringBuffer();
      sqlSZ
            .append("UPDATE ")
            .append(stTableName)
            .append(" SET "+setList.toString())
            .append((whereList.length()>0?" WHERE "+whereList.toString():""));

      final String stSQL = sqlSZ.toString();

       if(!hideLogger) logger.logInfo("update: paramss="+paramSet); //tes mark logger
       if(!hideLogger) logger.logInfo("update: paramsw="+paramWhere); //tes mark logger

      getConnection();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         for (int i = 0; i < paramSet.size(); i++) {
            setParam(i+1, paramSet.get(i));
         }

         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(i+paramSet.size()+1, paramWhere.get(i));
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
             if(!hideLogger) logger.logDebug("update: no record updated"); //tes mark logger

            releaseResource();

            if (dto instanceof RecordAudit) {
               if (dto.isEnableLockProtect()) {

                   if(!hideLogger) logger.logDebug("update: dto="+LogUtil.logGetterMethods(dto)); //tes mark logger

                  final DTO checkDTO = reload(dto);

                  if (checkDTO==null)
                     throw new Exception("failed to update record");
                  else {
                     throw new Exception("Record already updated by "+checkDTO.getStChangeWho()+" at  "+checkDTO.getDtChangeDate());
                  }
               }
               else throw new Exception("failed to update record");
            }
         }

         if (dto instanceof  RecordAudit ) {
            dto.setStChangeWho(us.getStUserID());
            dto.setDtChangeDate(us.getDtTransactionDate());
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("UPDATE",dto.getOld(), dto);

         t = System.currentTimeMillis() - t;

         if (t>50) logger.logWarning("High cost update : "+t+" ms ["+dto.getClass()+"]");
         
         return f;
      }
      catch (Exception e) {
         logger.logError("Error updating record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }

   public void delete(DTO dto) throws Exception {
      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      final StringBuffer whereList = new StringBuffer();
      final ArrayList paramWhere = new ArrayList();

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final boolean normalDelete = (DTOCache.getInstance().isUsingNormalDelete(dto.getClass()));

      final String stSQL =
            normalDelete?
            "DELETE FROM "+stTableName+
            (whereList.length()>0?" WHERE "+whereList.toString():"") :
            "UPDATE "+stTableName+" set delete_flag='Y', change_date=?, change_who=? "+
            (whereList.length()>0?" WHERE "+whereList.toString():"");

      getConnection();

      UserSession us = dto.getUserSession();

      if (us==null) us=this.userSession;

      final PreparedStatement PS = setQuery(stSQL);

      try {
         int j=1;

         if (!normalDelete) {
            setParam(j++, us.getDtTransactionDate());
            setParam(j++, us.getStUserID());
         }

         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(j++, paramWhere.get(i));
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            logger.logDebug("delete: no record deleted");
         }

         trigger("DELETE",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error deleting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }

   private void trigger(String s, DTO old, DTO dto) throws Exception {

      //if (true) return; // trigger is disabled

      if (triggers==null) return;

      final String cn = dto.getClass().getName();

      for (int i = 0; i < triggers.size(); i++) {
         Trigger trigger = (Trigger) triggers.get(i);

         if (trigger.className.equalsIgnoreCase(cn))
            trigger.onUpdate(s,old,dto);
      }
   }

   private void resetPool(DTO dto) {
      try {
         DTOPool.getInstance().reset(dto);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public int store(DTO dto) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      if (dto.isNew())
         insert(dto);
      else if (dto.isUpdate())
         return update(dto);
      else if (dto.isDelete())
         delete(dto);
      else if (dto.isInsertOrUpdate()) {
         if (update(dto)==0) insert(dto);
      }

       //long t = System.currentTimeMillis();
      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }
   
   public int store2(DTO dto) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      if (dto.isNew())
         insert(dto);
      else if (dto.isUpdate())
         return update(dto);
      else if (dto.isDelete())
         deleteNormal(dto);
      else if (dto.isInsertOrUpdate()) {
         if (update(dto)==0) insert(dto);
      }

      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }
   
   public int storeDeleteNormal(DTO dto) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      deleteNormal(dto);

      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }

   public void reset() {
      releaseResource();
   }

   public void releaseResource() {
      try {
         if (preparedStatement != null) {
            preparedStatement.close();
            preparedStatement = null;
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void store(DTOList l) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            store(dto);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) store(dto);
      }

   }
   
   public void store2(DTOList l) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            store2(dto);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) store2(dto);
      }

   }
   
   public void storeDeleteNormal(DTOList l) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            storeDeleteNormal(dto);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) storeDeleteNormal(dto);
      }
   }

   public static String getSequence(String stSequenceName, int length) throws Exception {
      final String seq = getSequence(stSequenceName);

      if (seq==null) return null;

      if (seq.length() > length) return seq;

      final char[] prefix = new char[length-seq.length()];

      for (int i = 0; i < prefix.length; i++)
         prefix[i] = '0';

      return new String(prefix)+seq;
   }

   public static String getSequence(String stSequenceName) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("select nextval('"+stSequenceName+"')");

         final ResultSet RS = PS.executeQuery();

         if (RS.next())
            return RS.getString(1);

         return null;
      }
      finally {
         S.release();
      }
   }

   public UserSession getUserSession() {
      return userSession;
   }

   public void setUserSession(UserSession userSession) {
      this.userSession = userSession;
   }

   public static String exprIN(String stFieldName, Collection oValues) throws Exception {
      if (oValues == null) return null;

      if (oValues.size() == 0) return "";

      if (oValues.size() == 1) {
         //addParam(oValues.iterator().next());
         return stFieldName+" = ?";
      }

      final Iterator it = oValues.iterator();

      final StringBuffer sz = new StringBuffer();

      sz.append(stFieldName).append(" IN (");

      int i=0;

      while (it.hasNext()) {
         String oval = (String) it.next();

         if (i > 0) sz.append(",");
         sz.append("?");

         i++;

         //addParam(oval);
      }
      sz.append(")");

      return sz.toString();
   }

   public ResultSet executeQuery() throws Exception {
      long t = System.currentTimeMillis();

      final ResultSet rs = preparedStatement.executeQuery();

      t=System.currentTimeMillis()-t;

      if (t>100) {
         logger.logWarning("executeQuery: probably High cost query : ["+t+" ms] - nQuery: "+stQuery);
      }

      return rs;
   }
   
   public void executeQuery2() throws Exception {
      long t = System.currentTimeMillis();

      final ResultSet res = preparedStatement.executeQuery();

      t=System.currentTimeMillis()-t;

      if (t>100) {
         logger.logWarning("executeQuery: probably High cost query : ["+t+" ms] - nQuery: "+stQuery);
      }

      	this.result = res;
   }

   public static void addTrigger(Trigger t) {
      triggers.add(t);
   }

   public void execSQL(String s) throws Exception {
      getConnection();
      PreparedStatement PS = connection.prepareStatement(s);
      try {
         PS.executeUpdate();
      } finally {
         PS.close();
      }
   }

   public static abstract class Trigger {
      private String className;

      protected Trigger(String _className) {
         className = _className;
      }

      public abstract void onUpdate(String op, DTO dold, DTO dnew) throws Exception;
   }

   public static int qstore(DTO dto) throws Exception {
      SQLUtil S = new SQLUtil();

      try {
         return S.store(dto);
      } finally {
         S.release();
      }
   }

   public static void qstore(DTOList l) throws Exception {
      SQLUtil S = new SQLUtil();

      try {
         S.store(l);
      } finally {
         S.release();
      }


   }
   
   public void deleteNormal(DTO dto) throws Exception {
      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      final StringBuffer whereList = new StringBuffer();
      final ArrayList paramWhere = new ArrayList();

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final boolean normalDelete = (DTOCache.getInstance().isUsingNormalDelete(dto.getClass()));

      final String stSQL =
            "DELETE FROM "+stTableName+
            (whereList.length()>0?" WHERE "+whereList.toString():"");

      getConnection();

      UserSession us = dto.getUserSession();

      if (us==null) us=this.userSession;

      final PreparedStatement PS = setQuery(stSQL);

      try {
         int j=1;

         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(j++, paramWhere.get(i));
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            logger.logDebug("delete: no record deleted");
         }

         trigger("DELETE",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error deleting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }

   public int storeFromJobs(DTO dto) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      if (dto.isNew())
         insertFromJobs(dto);
      else if (dto.isUpdate())
         return updateFromJobs(dto);
      else if (dto.isDelete())
         delete(dto);
      else if (dto.isInsertOrUpdate()) {
         if (update(dto)==0) insertFromJobs(dto);
      }

       //long t = System.currentTimeMillis();
      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }

   public void insertFromJobs(DTO dto) throws Exception {
      insertCount++;

      dto.beforeInsert();

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      UserSession us = dto.getUserSession();

      if (us==null) us = this.userSession;

      final StringBuffer fieldList = new StringBuffer();
      final StringBuffer valueList = new StringBuffer();

      if (dto instanceof  RecordAudit) {
         if (us != null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }
         else if (dto.isEnableStoreSync() ) {
            fieldList.append("CREATE_DATE,CREATE_WHO,CHANGE_DATE,CHANGE_WHO");
            valueList.append("?,?,?,?");
         }

         if (us == null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }

      }

      boolean hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (!df.isDatabaseField()) continue;

         //if (df.isReadOnly())
         //   if (!df.isPrimaryKey()) continue;

         if (fieldList.length()>0) {
            fieldList.append(",");
            valueList.append(",");
         }

         if (df.isCLOB()) {
            hasCLOB=true;
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("EMPTY_CLOB()");
         } else {
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("?");
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "INSERT INTO "+stTableName+"("+fieldList.toString()+") VALUES("+valueList.toString()+")";

      getConnection();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         it = fields.values().iterator();

         int i=1;

         if ((us != null) && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
         }

         if ((us == null) && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
         }

         if (dto.isEnableStoreSync() && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
            setParam(i++, dto.getDtChangeDate());
            setParam(i++, dto.getStChangeWho());
         }

         while (it.hasNext()) {
            DTOField df = (DTOField) it.next();

            if (!df.isDatabaseField()) continue;

            if (df.isCLOB()) continue;

            //if (df.isReadOnly())
            //   if (!df.isPrimaryKey()) continue;

            final Object o = df.getGetter().invoke(dto, null);

            if (df.isCLOB2()) {
               setParamCLOB(i++, (String)o);
            };

            setParam(i++, o);
         }

         final int f = PS.executeUpdate();

         
         if (f == 0) {
            //logger.logDebug("insert: no record updated");
            throw new SQLException("Failed to insert record into "+stTableName+", probably caused by trigger failure");
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("INSERT",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error inserting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }


   public void storeFromJobs(DTOList l) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            storeFromJobs(dto);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) storeFromJobs(dto);
      }

   }

   public int storeToGateway(DTO dto) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      if (dto.isNew())
         insertToGateway(dto);
      else if (dto.isInsertOrUpdate()) {
         if (update(dto)==0) insertToGateway(dto);
      }

       //long t = System.currentTimeMillis();
      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }

   public void insertToGateway(DTO dto) throws Exception {
      insertCount++;

      dto.beforeInsert();

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      UserSession us = dto.getUserSession();

      if (us==null) us = this.userSession;

      final StringBuffer fieldList = new StringBuffer();
      final StringBuffer valueList = new StringBuffer();

      if (dto instanceof  RecordAudit) {
         if (us != null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }

         if (us == null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }
         
         else if (dto.isEnableStoreSync() ) {
            fieldList.append("CREATE_DATE,CREATE_WHO,CHANGE_DATE,CHANGE_WHO");
            valueList.append("?,?,?,?");
         }
      }

      boolean hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (!df.isDatabaseField()) continue;

         //if (df.isReadOnly())
         //   if (!df.isPrimaryKey()) continue;

         if (fieldList.length()>0) {
            fieldList.append(",");
            valueList.append(",");
         }

         if (df.isCLOB()) {
            hasCLOB=true;
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("EMPTY_CLOB()");
         } else {
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("?");
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "INSERT INTO "+stTableName+"("+fieldList.toString()+") VALUES("+valueList.toString()+")";

      //getConnection();

      connection = ConnectionCache.getInstance().getConnection("GATEWAY");

      final PreparedStatement PS = setQuery(stSQL);

      try {
         it = fields.values().iterator();

         int i=1;

         if ((us != null) && (dto instanceof RecordAudit)) {
            setParam(i++, us.getDtTransactionDate());
            setParam(i++, us.getStUserID());
         }

         if ((us == null) && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
         }

         if (dto.isEnableStoreSync() && (dto instanceof RecordAudit)) {
            setParam(i++, dto.getDtCreateDate());
            setParam(i++, dto.getStCreateWho());
            setParam(i++, dto.getDtChangeDate());
            setParam(i++, dto.getStChangeWho());
         }

         while (it.hasNext()) {
            DTOField df = (DTOField) it.next();

            if (!df.isDatabaseField()) continue;

            if (df.isCLOB()) continue;

            //if (df.isReadOnly())
            //   if (!df.isPrimaryKey()) continue;

            final Object o = df.getGetter().invoke(dto, null);

            if (df.isCLOB2()) {
               setParamCLOB(i++, (String)o);
            };

            setParam(i++, o);
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            //logger.logDebug("insert: no record updated");
            throw new SQLException("Failed to insert record into "+stTableName+", probably caused by trigger failure");
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("INSERT",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error inserting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
         release();
      }
   }

   public void storeToGateway(DTOList l) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            storeToGateway(dto);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) storeToGateway(dto);
      }

   }

   public void storeToDataSource(DTOList l, String dataSourceName) throws Exception {
      if (l==null) return;

      if (l.getDeleted() != null) {
         final DTOList l2 = l.getDeleted();

         for (int i = 0; i < l2.size(); i++) {
            DTO dto = (DTO) l2.get(i);
            storeToDataSource(dto, dataSourceName);
         }
      }

      for (int i = 0; i < l.size(); i++) {
         DTO dto = (DTO) l.get(i);

         if (dto.isUpdate() || dto.isNew() || dto.isDelete()) storeToDataSource(dto, dataSourceName);
      }

   }

   public int storeToDataSource(DTO dto, String dataSourceName) throws Exception {
      if (dto==null) return 0;

      long t = System.currentTimeMillis();

      if (dto.isNew())
         insertToDataSource(dto, dataSourceName);
      else if (dto.isInsertOrUpdate()) {
         if (update(dto)==0) insertToDataSource(dto, dataSourceName);
      }

       //long t = System.currentTimeMillis();
      t = System.currentTimeMillis() - t;

      if (t>50) logger.logWarning("High cost store : "+t+" ms ["+dto.getClass()+"]");

      releaseResource();
      return 1;
   }

   public void insertToDataSource(DTO dto, String dataSourceName) throws Exception {
      insertCount++;

      dto.beforeInsert();

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      UserSession us = dto.getUserSession();

      if (us==null) us = this.userSession;

      final StringBuffer fieldList = new StringBuffer();
      final StringBuffer valueList = new StringBuffer();

      if (dto instanceof  RecordAudit) {
         if (us != null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }

         if (us == null) {
            fieldList.append("CREATE_DATE,CREATE_WHO");
            valueList.append("?,?");
         }

         else if (dto.isEnableStoreSync() ) {
            fieldList.append("CREATE_DATE,CREATE_WHO,CHANGE_DATE,CHANGE_WHO");
            valueList.append("?,?,?,?");
         }
      }

      boolean hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (!df.isDatabaseField()) continue;

         //if (df.isReadOnly())
         //   if (!df.isPrimaryKey()) continue;

         if (fieldList.length()>0) {
            fieldList.append(",");
            valueList.append(",");
         }

         if (df.isCLOB()) {
            hasCLOB=true;
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("EMPTY_CLOB()");
         } else {
            fieldList.append(df.getStDatabaseFieldName());
            valueList.append("?");
         }
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "INSERT INTO "+stTableName+"("+fieldList.toString()+") VALUES("+valueList.toString()+")";

      //getConnection();

      connection = ConnectionCache.getInstance().getConnection(dataSourceName);

      final PreparedStatement PS = setQuery(stSQL);

      try {
         it = fields.values().iterator();

         int i=1;

         if ((us != null) && (dto instanceof RecordAudit)) {
            setParam(i++, us.getDtTransactionDate());
            setParam(i++, us.getStUserID());
         }

         if ((us == null) && (dto instanceof RecordAudit)) {
            setParam(i++, new Date());
            setParam(i++, "00000001");
         }

         if (dto.isEnableStoreSync() && (dto instanceof RecordAudit)) {
            setParam(i++, dto.getDtCreateDate());
            setParam(i++, dto.getStCreateWho());
            setParam(i++, dto.getDtChangeDate());
            setParam(i++, dto.getStChangeWho());
         }

         while (it.hasNext()) {
            DTOField df = (DTOField) it.next();

            if (!df.isDatabaseField()) continue;

            if (df.isCLOB()) continue;

            //if (df.isReadOnly())
            //   if (!df.isPrimaryKey()) continue;

            final Object o = df.getGetter().invoke(dto, null);

            if (df.isCLOB2()) {
               setParamCLOB(i++, (String)o);
            };

            setParam(i++, o);
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            //logger.logDebug("insert: no record updated");
            throw new SQLException("Failed to insert record into "+stTableName+", probably caused by trigger failure");
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("INSERT",dto.getOld(), dto);
      }
      catch (Exception e) {
         logger.logError("Error inserting record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
         release();
      }
   }



   public static String exprANY(String stFieldName, Collection oValues) throws Exception {
      if (oValues == null) return null;

      if (oValues.size() == 0) return "";

      if (oValues.size() == 1) {
         //addParam(oValues.iterator().next());
         return stFieldName+" = ?";
      }

      final Iterator it = oValues.iterator();

      final StringBuffer sz = new StringBuffer();

      sz.append(stFieldName).append(" = ANY (VALUES");

      int i=0;

      while (it.hasNext()) {
         String oval = (String) it.next();

         if (i > 0) sz.append(",");
         sz.append("(?)");

         i++;

         //addParam(oval);
      }
      sz.append(")");

      return sz.toString();
   }

   public int updateFromJobs(DTO dto) throws Exception {
      updateCount++;

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      long t = System.currentTimeMillis();

      Iterator it = fields.values().iterator();

      final StringBuffer setList = new StringBuffer();

      final StringBuffer whereList = new StringBuffer();

      final ArrayList paramSet = new ArrayList();
      final ArrayList paramWhere = new ArrayList();

      UserSession us = dto.getUserSession();

      if (us==null) us=this.userSession;

      if ((dto instanceof  RecordAudit) && (!dto.isEnableStoreSync())) {
         //if (us==null) throw new Exception("UserSession not attached");
      }

      if ((us != null) && (dto instanceof RecordAudit) && (!dto.isEnableStoreSync())) {
         setList.append("CHANGE_DATE = ?, CHANGE_WHO = ?");
         paramSet.add(us.getDtTransactionDate());
         paramSet.add(us.getStUserID());
      }

      if ((dto.isEnableStoreSync()) && (dto instanceof RecordAudit)) {
         setList.append("CREATE_DATE = ?, CREATE_WHO = ?, CHANGE_DATE = ?, CHANGE_WHO = ?");
         paramSet.add(dto.getDtCreateDate());
         paramSet.add(dto.getStCreateWho());
         paramSet.add(dto.getDtChangeDate());
         paramSet.add(dto.getStChangeWho());
      }

      hasCLOB = false;

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (df.isDatabaseField()) {
            if (df.isPrimaryKey()) {
               if (whereList.length()>0) whereList.append(" AND ");
               whereList.append(df.getStDatabaseFieldName()+"=?");
               paramWhere.add(df.getGetter().invoke(dto,null));
            }
            else if (!df.isReadOnly()) {
               if (df.isCLOB()) {
                  hasCLOB=true;
                  continue;
               }
               if (setList.length()>0) setList.append(",");
               setList.append(df.getStDatabaseFieldName()+"=?");

               if (df.isCLOB2()) {
                  paramSet.add(new CLOBWrapper(df.getGetter().invoke(dto,null)));
               } else {
                  paramSet.add(df.getGetter().invoke(dto,null));
               }

            }
         }
      }

	  /*
      if (useLockProtect && (dto.isEnableLockProtect()) && (dto.getDtCreateDate()!=null) && (!dto.isEnableStoreSync()))
         if (whereList.length()>0) {
            if (dto instanceof RecordAudit)
             {
               if (dto.getDtChangeDate() != null) {
                  //whereList.append(" and (date_trunc('seconds', change_date)=date_trunc('seconds', timestamp ?)) ");
                  whereList.append(" and (change_date=?) ");
                  paramWhere.add(dto.getDtChangeDate());
               } else {
                  whereList.append(" and change_date is null ");
               }
            }
         }*/

      final Field tnf = dto.getClass().getField("tableName");


      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final StringBuffer sqlSZ = new StringBuffer();
      sqlSZ
            .append("UPDATE ")
            .append(stTableName)
            .append(" SET "+setList.toString())
            .append((whereList.length()>0?" WHERE "+whereList.toString():""));

      final String stSQL = sqlSZ.toString();

       if(!hideLogger) logger.logInfo("update: paramss="+paramSet); //tes mark logger
       if(!hideLogger) logger.logInfo("update: paramsw="+paramWhere); //tes mark logger

      getConnection();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         for (int i = 0; i < paramSet.size(); i++) {
            setParam(i+1, paramSet.get(i));
         }

         for (int i = 0; i < paramWhere.size(); i++) {
            setParam(i+paramSet.size()+1, paramWhere.get(i));
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
             if(!hideLogger) logger.logDebug("update: no record updated"); //tes mark logger

            releaseResource();

            if (dto instanceof RecordAudit) {
               if (dto.isEnableLockProtect()) {

                   if(!hideLogger) logger.logDebug("update: dto="+LogUtil.logGetterMethods(dto)); //tes mark logger

                  final DTO checkDTO = reload(dto);

                  if (checkDTO==null)
                     throw new Exception("failed to update record");
                  else {
                     throw new Exception("Record already updated by "+checkDTO.getStChangeWho()+" at  "+checkDTO.getDtChangeDate());
                  }
               }
               else throw new Exception("failed to update record");
            }
         }

         if (dto instanceof  RecordAudit ) {
            dto.setStChangeWho("00000001");
            dto.setDtChangeDate(new Date());
         }

         if (hasCLOB) {
            releaseResource();
            updateCLOB(dto);
         }

         trigger("UPDATE",dto.getOld(), dto);

         t = System.currentTimeMillis() - t;

         if (t>50) logger.logWarning("High cost update : "+t+" ms ["+dto.getClass()+"]");

         return f;
      }
      catch (Exception e) {
         logger.logError("Error updating record : ("+e.toString()+") : "+dto);
         throw e;
      }
      finally {
         releaseResource();
         resetPool(dto);
      }
   }
     
}

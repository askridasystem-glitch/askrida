/***********************************************************************
 * Module:  com.crux.util.SQLUtilArchive
 * Author:  Denny Mahendra
 * Created: Aug 2, 2004 10:03:54 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import com.crux.common.model.DTO;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;


public class SQLUtilArchive extends SQLUtil {
   public SQLUtilArchive() throws Exception {
   }

   public Connection getConnection() throws Exception {
      if (connection==null)
      {
         connection = ConnectionCache.getInstance().getConnection("java:/TelkomselArcDS");
      }

      return connection;
   }

   public void release() {
      try {
         releaseResource();

         if (connection != null) {
            connection.close();
            connection = null;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void releaseResource() {
      try {
         if (preparedStatement != null) {
            preparedStatement.close();
            preparedStatement = null;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public PreparedStatement setQuery(String stSQL) throws Exception {
      getConnection();
      preparedStatement = connection.prepareStatement(stSQL);
      return preparedStatement;
   }

   public void insert(DTO dto) throws Exception {
      insertCount++;

      final HashMap fields = DTOCache.getInstance().getFields(dto.getClass());

      Iterator it = fields.values().iterator();

      final StringBuffer fieldList = new StringBuffer();
      final StringBuffer valueList = new StringBuffer();

      fieldList.append("CREATE_DATE,CREATE_WHO,CHANGE_DATE,CHANGE_WHO");
      valueList.append("?,?,?,?");

      while (it.hasNext()) {
         DTOField df = (DTOField) it.next();

         if (!df.isDatabaseField()) continue;

         if (fieldList.length() > 0) {
            fieldList.append(",");
            valueList.append(",");
         }
         fieldList.append(df.getStDatabaseFieldName());
         valueList.append("?");
      }

      final Field tnf = dto.getClass().getField("tableName");

      if (tnf == null) throw new IllegalArgumentException("Class does not have 'tableName' field.");

      final String stTableName = (String) tnf.get(null);

      final String stSQL = "INSERT INTO " + stTableName + "(" + fieldList.toString() + ") VALUES(" + valueList.toString() + ")";

      getConnection();

      final PreparedStatement PS = setQuery(stSQL);

      try {
         it = fields.values().iterator();

         int i = 1;

         setParam(i++, dto.getDtCreateDate());
         setParam(i++, dto.getStCreateWho());
         setParam(i++, dto.getDtChangeDate());
         setParam(i++, dto.getStChangeWho());

         while (it.hasNext()) {
            DTOField df = (DTOField) it.next();

            if (!df.isDatabaseField()) continue;

            final Object o = df.getGetter().invoke(dto, null);

            setParam(i++, o);
         }

         final int f = PS.executeUpdate();

         if (f == 0) {
            throw new SQLException("Failed to insert record into " + stTableName + ", probably caused by trigger failure");
         }
      } catch (Exception e) {
         throw e;
      } finally {
         release();
      }
   }
}

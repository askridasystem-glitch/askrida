/***********************************************************************
 * Module:  com.crux.common.parameter.Parameter
 * Author:  Denny Mahendra
 * Created: Apr 26, 2004 5:39:58 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter;

import com.crux.util.LogManager;
import com.crux.util.SQLUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class Parameter {

   private final static transient LogManager logger = LogManager.getInstance(Parameter.class);

   public final static transient HashMap pool = new HashMap();

   public static Date readDate(String stParameterID) {
      return readDate(stParameterID, 1);
   }

   public static Date readDate(String stParameterID, int iSeq) {
      return readDate(null, stParameterID,iSeq);

   }

   public static Date readDate(String stDataSource, String stParameterID, int iSeq) {
      try {
         final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

         try {
            final PreparedStatement PS = S.setQuery("select value_date from s_parameter where param_id = ? and param_seq = ?");

            PS.setString(1, stParameterID);
            PS.setInt(2, iSeq);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               return RS.getTimestamp(1);
            }

            return null;
         }
         finally {
            S.release();
         }
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static String readString(String stParameterID) {
      return readString(stParameterID,1);
   }

   public static String readString(String stParameterID, int iSeq) {
      return readString(null, stParameterID, iSeq);
   }

   public static String readString(String stDataSource, String stParameterID, int iSeq) {
      try {
         final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

         try {
            final PreparedStatement PS = S.setQuery("select value_string from s_parameter where param_id = ? and param_seq = ?");

            PS.setString(1, stParameterID);
            PS.setInt(2, iSeq);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               return RS.getString(1);
            }

            return null;
         }
         finally {
            S.release();
         }
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static boolean readBoolean(String stParameterID) {
      return readBoolean(stParameterID, 1, false);
   }

   public static boolean readBoolean(String stParameterID,boolean defaultValue) {
      return readBoolean(stParameterID, 1, defaultValue);
   }

   public static boolean readBoolean(String stParameterID, int iSeq, boolean defaultValue) {
      return readBoolean(null, stParameterID, iSeq, defaultValue);
   }

   public static boolean readBoolean(String stDataSource, String stParameterID, int iSeq, boolean defaultValue) {
      final BigDecimal n = readNum(stDataSource, stParameterID, iSeq);

      if (n!=null) return n.intValue() == 1;

      return defaultValue;
   }

   public static BigDecimal readNum(String stParameterID) {
      return readNum(stParameterID, 1);
   }

   public static BigDecimal readNum(String stParameterID, int iSeq) {
      return readNum(null, stParameterID,iSeq);
   }

   public static BigDecimal readNum(String stDataSource, String stParameterID, int iSeq) {
      try {
         final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

         try {
            final PreparedStatement PS = S.setQuery("select value_number from s_parameter where param_id = ? and param_seq = ?");

            PS.setString(1, stParameterID);
            PS.setInt(2, iSeq);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               return RS.getBigDecimal(1);
            }

            return null;
         }
         finally {
            S.release();
         }
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static void setDate(String stParameterID, Date dt) throws Exception {
      setDate(stParameterID, 1, dt);
   }

   public static void setDate(String stParameterID, int iSeq, Date dt) throws Exception {
      setDate(null, stParameterID, iSeq, dt);
   }

   public static void setDate(String stDataSource, String stParameterID, int iSeq, Date dt) throws Exception {
      final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

      try {
         final PreparedStatement PS = S.setQuery("update s_parameter set value_date=? where param_id = ? and param_seq = ?");

         PS.setTimestamp(1, new java.sql.Timestamp(dt.getTime()));
         PS.setString(2, stParameterID);
         PS.setInt(3, iSeq);

         final int n = PS.executeUpdate();

         if (n == 0) {
            S.reset();
            final PreparedStatement PS2 = S.setQuery("insert into s_parameter(param_id,param_seq,value_date) values(?,?,?)");
            PS2.setString(1, stParameterID);
            PS2.setInt(2, iSeq);
            PS2.setTimestamp(3, new java.sql.Timestamp(dt.getTime()));
            PS2.executeUpdate();
         }
      }
      finally {
         S.release();
      }
   }

   public static void setString(String stParameterID, String st) throws Exception {
      setString(stParameterID, 1, st);
   }

   public static void setString(String stParameterID, int iSeq, String st) throws Exception {
      setString(null, stParameterID, iSeq,st);
   }

   public static void setString(String stDataSource, String stParameterID, int iSeq, String st) throws Exception {
      final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

      try {
         final PreparedStatement PS = S.setQuery("update s_parameter set value_string=? where param_id = ? and param_seq = ?");

         PS.setString(1, st);
         PS.setString(2, stParameterID);
         PS.setInt(3, iSeq);

         final int n = PS.executeUpdate();

         if (n == 0) {
            S.reset();
            final PreparedStatement PS2 = S.setQuery("insert into s_parameter(param_id,param_seq,value_string,param_type) values(?,?,?,'STRING')");
            PS2.setString(1, stParameterID);
            PS2.setInt(2, iSeq);
            PS2.setString(3, st);
            PS2.executeUpdate();
         }
      }
      finally {
         S.release();
      }
   }

   public static void setBoolean(String stParameterID, boolean x) throws Exception {
      setBoolean(stParameterID, 1, x);
   }

   public static void setBoolean(String stParameterID, int iSeq, boolean x) throws Exception {
      setNumber(stParameterID, iSeq, new BigDecimal(x?1:0));
   }

   public static void setNumber(String stParameterID, int iSeq, BigDecimal bd) throws Exception {
      setNumber(null, stParameterID, iSeq, bd);
   }

   public static void setNumber(String stDataSource, String stParameterID, int iSeq, BigDecimal bd) throws Exception {
      final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

      try {
         final PreparedStatement PS = S.setQuery("update s_parameter set value_number=? where param_id = ? and param_seq = ?");

         PS.setBigDecimal(1, bd);
         PS.setString(2, stParameterID);
         PS.setInt(3, iSeq);

         final int n = PS.executeUpdate();

         if (n == 0) {
            S.reset();
            final PreparedStatement PS2 = S.setQuery("insert into s_parameter(param_id,param_desc,param_seq,value_number,param_group,param_order,param_type) values(?,?,?,?,?,?,?)");
            PS2.setString(1, stParameterID);
            PS2.setString(2, stParameterID);
            PS2.setInt(3, iSeq);
            PS2.setBigDecimal(4, bd);
            PS2.setString(5, "x");
            PS2.setString(6, "1");
            PS2.setString(7, "INTEGER");
            PS2.executeUpdate();
         }
      }
      finally {
         S.release();
      }
   }

   public static String readStringAccounts(String stParameterID) {
      return readStringAccounts(stParameterID,1);
   }

   public static String readStringAccounts(String stParameterID, int iSeq) {
      return readStringAccounts(null, stParameterID, iSeq);
   }

   public static String readStringAccounts(String stDataSource, String stParameterID, int iSeq) {
      try {
         final SQLUtil S = stDataSource == null ? new SQLUtil() : new SQLUtil(stDataSource);

         try {
            final PreparedStatement PS = S.setQuery("select value_string from gl_accounts_header where acc_hdr_id = ? and param_seq = ?");

            PS.setString(1, stParameterID);
            PS.setInt(2, iSeq);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               return RS.getString(1);
            }

            return null;
         }
         finally {
            S.release();
         }
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }


}

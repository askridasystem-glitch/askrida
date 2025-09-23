/***********************************************************************
 * Module:  com.crux.util.IDFactory
 * Author:  Denny Mahendra
 * Created: Mar 24, 2004 5:52:38 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.model.UserSession;
import com.crux.common.config.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;

public class IDFactory {
   private final static transient LogManager logger = LogManager.getInstance(IDFactory.class);

   private final static HashMap seqMap = new HashMap();
   private static int seqStart = 10000;

   public static String createID2(String stPrefix, int totalDigits) throws Exception {
      int retry=1;

      final SQLUtil S = new SQLUtil();

      long t = System.currentTimeMillis();

      do {

         try {
            final String seqname = ("IDF_"+stPrefix).toLowerCase();

            if (seqMap.get(seqname) == null) {
               final PreparedStatement PS = S.setQuery("select count(1) from pg_class where relname=? and relkind='S'");

               PS.setString(1, seqname);

               final ResultSet RS = PS.executeQuery();

               if (RS.next()) {

                  if (RS.getInt(1)<1) {
                     S.releaseResource();

                     final PreparedStatement PS2 = S.setQuery("create sequence "+seqname+" start "+getCurrentSequence(stPrefix)+1);

                     ThreadContext.getInstance().add("CRS",seqname);

                     PS2.executeUpdate();
                  }
                  seqMap.put(seqname,"OK");
               }

               S.releaseResource();
            }

            {
               try {
                  final PreparedStatement PS = S.setQuery("select nextval('"+seqname+"') ");

                  final ResultSet RS = PS.executeQuery();

                  if (RS.next()) {
                     final long seq = RS.getLong(1);

                     String stResult = constructID(seq, totalDigits, stPrefix);

                     //logger.logDebug("createID2: "+stResult); tes mark logger

                     t = System.currentTimeMillis() - t;

                     if (t>50) logger.logWarning("High cost when creating ID : "+stPrefix+" ("+t+" ms)");

                     return stResult;
                  }

                  throw new Exception("Failed to create id : "+stPrefix);
               }
               catch (Exception e) {
                  e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                  if (retry<=0) throw e;
                  seqMap.remove(seqname);
                  logger.logDebug("createID2: retrying ... ");
                  retry--;
               }
            }
         }
         finally {
            S.release();
         }
      } while (true);
   }

   private static long getCurrentSequence(String stPrefix) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("select idsequence from idmaster where idprefix = ?");

         PS.setString(1, stPrefix);

         final ResultSet RS = PS.executeQuery();

         long n=0;

         if (RS.next()) {
            n = RS.getLong(1);
         }

         return n;
      }
      finally {
         S.release();
      }
   }

   public static String createID(String stPrefix, int totalDigits) throws Exception {
      if (
            (stPrefix.indexOf("OPH") == 0) ||
            (stPrefix.indexOf("DPH") == 0) ||
            (stPrefix.indexOf("HIS") == 0) ||
            (stPrefix.indexOf("GRNI") == 0) ||
            (stPrefix.indexOf("PLI") == 0) ||
            (stPrefix.indexOf("GRNI") == 0) ||
            (stPrefix.indexOf("IVI") == 0) ||
            (stPrefix.indexOf("MTI") == 0) ||
            (stPrefix.indexOf("MTL") == 0) ||
            (stPrefix.indexOf("SOI") == 0) ||
            (stPrefix.indexOf("IVH") == 0)
      )
         return createID2(stPrefix, totalDigits);

      final SQLUtil S = new SQLUtil();

      long t = System.currentTimeMillis();

      try {
         final PreparedStatement ps0 = S.setQuery("update idmaster set idsequence=idsequence+1 where idprefix = ?");

         S.setParam(1, stPrefix);

         ps0.executeUpdate();

         S.reset();

         final PreparedStatement PS = S.setQuery("select * from idmaster where idprefix = ?");

         S.setParam(1,stPrefix);

         final ResultSet RS = PS.executeQuery();

         long seq=1;

         if (!RS.next()) {
            S.reset();

            final PreparedStatement PS2 = S.setQuery("insert into idmaster(idprefix,idsequence) values(?,?)");

            SQLUtil.insertCount++;

            S.setParam(1,stPrefix);
            S.setParam(2,new Long(1));

            PS2.executeUpdate();
         } else {

            seq=RS.getLong(2);
         }

         S.release();

         String stResult = constructID(seq, totalDigits, stPrefix);

         //logger.logDebug("createID: "+stResult); tes mark logger

         t = System.currentTimeMillis() - t;

         if (t>50) logger.logWarning("High cost when creating ID : "+stPrefix+" ("+t+" ms)");

         return stResult;
      }
      finally {

         S.release();

      }
   }

   private static String constructID(long seq, int totalDigits, String stPrefix) {
      final String stSeq = String.valueOf(seq);

      final int zeroes = totalDigits - (stSeq.length() + stPrefix.length());

      String stResult = null;

      if (zeroes < 0)

         stResult = stPrefix + stSeq;

      else {

         final StringBuffer sz = new StringBuffer();

         sz.append(stPrefix);

         for (int i=0;i<zeroes;i++) sz.append('0');

         sz.append(stSeq);

         stResult = sz.toString();
      }
      return stResult;
   }

   public static String getYearMonth(UserSession us) {
      final Calendar cal = Calendar.getInstance();

      cal.setTime(us.getDtTransactionDate());

      final int yr = cal.get(Calendar.YEAR) % 100;

      final int month = cal.get(Calendar.MONTH)+1;

      final StringBuffer sz = new StringBuffer();

      if (yr<10) sz.append('0');
      sz.append(yr);
      if (month<10) sz.append('0');
      sz.append(month);

      return sz.toString();
   }

   /*public static String createTransactionID(String stPrefix, int totalDigits, UserSession us) throws Exception {
      final StringBuffer sz = new StringBuffer();

      sz.append(stPrefix);
      sz.append(us.getStBranchID());
      sz.append(getYearMonth(us));

      return createID(sz.toString(), totalDigits);
   }

   public static String createOracleInterfaceID(String stIDName, int totalDigits, UserSession us) throws Exception {
      final String stPrefix = stIDName + us.getStBranchCode();
      final String s = createID(stPrefix, totalDigits+stIDName.length());
      return s.substring(stPrefix.length(), s.length())+us.getStBranchCode();
   }*/

   public static void cancelCache() {
      ArrayList l = (ArrayList)ThreadContext.getInstance().get("CRS");
      logger.logDebug("cancelCache: "+l);
      if (l!=null) {
         for (int i = 0; i < l.size(); i++) {
            String seqname = (String) l.get(i);
            seqMap.remove(seqname);
         }
      }
   }

   /*public static void store(SequencesView s) throws Exception {
      if (s.isSequence()) {
         SQLUtil S = new SQLUtil();
         try {
            final PreparedStatement PS2 = S.setQuery("create sequence "+s.getStSequenceName()+" start "+s.getLgCurrentValue()+1);

            PS2.executeUpdate();

         } finally {

            S.release();
         }
      }
      else if (s.isIDMaster()) {
         SQLUtil S = new SQLUtil();

         try {
            final PreparedStatement PS2 = S.setQuery("insert into idmaster(idprefix,idsequence) values(?,?)");

            S.setParam(1,s.getStSequenceName());
            S.setParam(2,s.getLgCurrentValue());

            PS2.executeUpdate();

         } finally {

            S.release();
         }
      }
   }*/

   public static long createNumericID(String stPrefix) throws Exception {
      return createNumericID(stPrefix,seqStart);
   };

   public static long createNumericID(String stPrefix, int seqStart) throws Exception {
      final SQLUtil S = new SQLUtil();

      long t = System.currentTimeMillis();

      try {
         final String seqname = ("IDF_"+stPrefix).toUpperCase();

         if (seqMap.get(seqname) == null) {
            final PreparedStatement PS = S.setQuery(
                    "ORA[select count(1) from all_objects where object_name=?]" +
                    "PGR[select count(1) from pg_class where upper(relname)=? and relkind='S']"
            );

            PS.setString(1, seqname);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {

                if (RS.getInt(1)<1) {
                  S.releaseResource();

                   //seqStart = 1;
                   final PreparedStatement PS2 = S.setQuery(
                          "ORA[create sequence "+seqname+" start WITH "+(getCurrentSequence(stPrefix)+seqStart)+"]"+
                          "PGR[create sequence "+seqname+" start "+(getCurrentSequence(stPrefix)+seqStart)+"]"
                  );

                  PS2.executeUpdate();
               } else
                  seqMap.put(seqname,"OK");
            }

            S.releaseResource();
         }

         {
            final PreparedStatement PS = S.setQuery(
                    "ORA[select "+seqname+".nextval from dual]" +
                    "PGR[select nextval('"+seqname+"')]"
            );

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               final long seq = RS.getLong(1);

              // logger.logDebug("createID2: "+seq); tes mark logger

               t = System.currentTimeMillis() - t;

               if (t>50) logger.logWarning("High cost when creating ID : "+stPrefix+" ("+t+" ms)");

               return seq;
            }

            throw new Exception("Failed to create id : "+stPrefix);
         }
      }
      finally {
         S.release();
      }
   }
   
   public static String createCharID(String stPrefix, int seqStart) throws Exception {
      final SQLUtil S = new SQLUtil();

      long t = System.currentTimeMillis();

      try {
         final String seqname = ("IDF_"+stPrefix).toUpperCase();

         if (seqMap.get(seqname) == null) {
            final PreparedStatement PS = S.setQuery(
                    "ORA[select count(1) from all_objects where object_name=?]" +
                    "PGR[select count(1) from pg_class where upper(relname)=? and relkind='S']"
            );

            PS.setString(1, seqname);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {

                if (RS.getInt(1)<1) {
                  S.releaseResource();

                   //seqStart = 1;
                   final PreparedStatement PS2 = S.setQuery(
                          "ORA[create sequence "+seqname+" start WITH "+(getCurrentSequence(stPrefix)+seqStart)+"]"+
                          "PGR[create sequence "+seqname+" start "+(getCurrentSequence(stPrefix)+seqStart)+"]"
                  );

                  PS2.executeUpdate();
               } else
                  seqMap.put(seqname,"OK");
            }

            S.releaseResource();
         }

         {
            final PreparedStatement PS = S.setQuery(
                    "ORA[select "+seqname+".nextval from dual]" +
                    "PGR[select nextval('"+seqname+"')]"
            );

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               final String seq = RS.getString(1);

               //logger.logDebug("createID2: "+seq); tes mark logger

               t = System.currentTimeMillis() - t;

               if (t>50) logger.logWarning("High cost when creating ID : "+stPrefix+" ("+t+" ms)");

               return seq;
            }

            throw new Exception("Failed to create id : "+stPrefix);
         }
      }
      finally {
         S.release();
      }
   }

   public static String createBranchedNumericID(String prefix) throws Exception {
      return
              String.valueOf(
                      createNumericID(prefix)*1000+Long.parseLong(Config.getMachineID())
              );
   }
}

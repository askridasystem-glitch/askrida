/***********************************************************************
 * Module:  com.crux.util.NumberUtil
 * Author:  Denny Mahendra
 * Created: Mar 19, 2004 4:41:26 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.text.DecimalFormat;
import java.math.BigDecimal;

public class NumberUtil {
   private static DecimalFormat df = new DecimalFormat("###,###.#####");
   private static DecimalFormat dfDouble = new DecimalFormat("##############.##");

   private static DecimalFormat moneyformat = new DecimalFormat("#,##0.00");
   private static DecimalFormat floatformat = new DecimalFormat("#0.00");

   public static double getValue(Double dbData) {
      return dbData == null?0.0d:dbData.doubleValue();
   }

   public static double getValue(BigDecimal dbData) {
      return dbData == null?0.0d:dbData.doubleValue();
   }

   public static long getValue(Long dbData) {
      return dbData == null?0:dbData.longValue();
   }

   public static String getFormattedValue(Object dbData) throws Exception {
      return df.format(dbData);
   }

   public static String getFormattedDouble(double dbData) throws Exception {
     return dfDouble.format(dbData);
   }

   public static int getValue(Integer intData) throws Exception {
      return intData == null?0:intData.intValue();
   }

   public static Double getDouble(String stData) throws Exception {
      return stData == null || stData.equals("")? new Double(0) :new Double(df.parse(stData).doubleValue());
   }

   public static String removeComma(String s) {
      if (s == null) return null;

      if (s.indexOf(',') >= 0) {
         final StringBuffer sz = new StringBuffer();

         char c;

         for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (c != ',') sz.append(c);
         }

         return sz.toString();
      }

      return s;
   }
   
   public static String removePoint(String s) {
      if (s == null) return null;

      if (s.indexOf('.') >= 0) {
         final StringBuffer sz = new StringBuffer();

         char c;
         String d="";

         for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (c != '.') sz.append(c);
            if (c == '.') d = String.valueOf(c);
            if(d.equalsIgnoreCase(".") && c=='0') break;
         }

         return sz.toString();
      }

      return s;
   }

   public static Integer getInteger(String stData) {
      return stData == null || stData.equals("")? new Integer(0) :new Integer(removeComma(stData));
   }

   public static void setPattern(String stPattern) throws Exception {
      df.applyPattern(stPattern);
   }

   public static String getMoneyStr(BigDecimal dbValue,int iDigit) {
      if (dbValue == null) return "";
      return getMoneyStr(dbValue.doubleValue(),iDigit);
   }

   public static String getMoneyStr(Double dbValue,int iDigit) {
      if (dbValue == null) return "";
      return getMoneyStr(dbValue.doubleValue(),iDigit);
   }

   public static String getMoneyStr(double dbValue,int iDigit) {
      moneyformat.setMinimumFractionDigits(iDigit);
      moneyformat.setMaximumFractionDigits(iDigit);

      return moneyformat.format(dbValue);
   }

   public static String getMoneyStr(Double dbValue) {
      return dbValue == null ? "" : getMoneyStr(dbValue.doubleValue(), 2);
   }

   public static String getFloatStr(Double dbValue,int iDigit) {
      if (dbValue == null) return null;
      return getFloatStr(dbValue.doubleValue(),iDigit);
   }

   public static String getFloatStr(double dbValue,int iDigit) {
      floatformat.setMinimumFractionDigits(iDigit);
      floatformat.setMaximumFractionDigits(iDigit);

      return floatformat.format(dbValue);
   }

   public static String getIntStr(double db) {
      return Integer.toString((int)db);
   }

   public static String getIntStr(Double db) {
      if (db == null) return null;
      return Integer.toString(db.intValue());
   }

   public static Long getLong(String stValue) {
      return stValue == null || stValue.equals("")? new Long(0) : new Long(removeComma(stValue));
   }

   public static Integer doubleToInteger(Double db) {
      if (db == null) return new Integer(0);

      return new Integer(db.intValue());
   }

   public static Double longToDouble(Long l) {
      if (l == null) return new Double(0);

      return new Double(l.doubleValue());
   }
}

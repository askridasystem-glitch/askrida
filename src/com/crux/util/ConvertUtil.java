/***********************************************************************
 * Module:  com.crux.util.ConvertUtil
 * Author:  Denny Mahendra
 * Created: Dec 29, 2005 12:04:13 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import java.util.Date;
import java.math.BigDecimal;

public class ConvertUtil {
   private final static transient LogManager logger = LogManager.getInstance(ConvertUtil.class);

   public static String deBlank(String st) {
      if ("".equals(st)) return null;
      return st==null?null:st.trim();
   }

   public static String getString(String st) {
      if ("".equals(st)) return null;
      return st==null?null:st;
   }

   public static String getFlag(String st) {
      return "on".equalsIgnoreCase(st)?"Y":"N";
   }

   public static Long getLong(String st) {
      st=deBlank(st);
      return (st==null)?null:new Long(st);
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

   public static long getlong(String st) {
      st=deBlank(st);
      if (st==null) return 0; else return Long.parseLong(removeComma(st));
   }

   public static Date getDate(String st) {
      st=deBlank(st);
      if (st==null) return null; else return DateUtil.getDate(st);
   }

   public static BigDecimal getNum(String st) {
      try {
         st=deBlank(st);
         return (st==null)?null:new BigDecimal(removeComma(st));
      }
      catch (RuntimeException e) {
         logger.logDebug("getNum: error processing number : ["+st+"]");
         throw e;
      }
      catch (Error e) {
         logger.logDebug("getNum: error processing number : ["+st+"]");
         throw e;
      }
   }

   public static Object getInteger(String st) {
      st=deBlank(st);
      return (st==null)?null:new Integer(st);
   }

    public static String print(Long lg) {
      return lg == null ? "" : lg.toString();
   }

   public static String print(BigDecimal lg) {
      return lg==null?"":lg.toString();
   }

   public static String print(BigDecimal lg, int prec) {
      if (lg == null) return "";
      return NumberUtil.getMoneyStr(lg.doubleValue(), prec);
   }

   public static String print(String st) {
      return st == null ? "" : st;
   }

   public static String print(Date st) {
      return st == null ? "" : DateUtil.getDateStr(st);
   }

   public static String print(Object st) {
      if (st instanceof Date) return print((Date) st);
      if (st instanceof Long) return print((Long) st);
      if (st instanceof BigDecimal) return print((BigDecimal) st,2);
      return st == null ? "" : st.toString();
   }

   public static String getScriptValueOfObject(Object o) {
      if (o instanceof String)
         return "'" + o + "'";
      else if (o instanceof Date)
         return "'" + DateUtil.getDateStr((Date) o) + "'";
      else
         return String.valueOf(o);
   }

   public static String printDateTime(Date d) {
      if (d == null) return "";
      return DateUtil.getDateTimeStr(d);
   }

   public static String removeTrailing(String s) {

      int i=s.length()-1;

      while (true) {
         final char c = s.charAt(i);

         if (c=='0' || c==' ') {
            i--;continue;
         }

         if (c=='.') {
            i--;
            break;
         }

         break;
      }

      return s.substring(0,i+1);
   }

   public static Object convert(Object value, Class cls) {

      if (value==null) return null;

      if (value instanceof Date) value=DateUtil.getDateStr((Date)value);

      if (String.class.isAssignableFrom(cls)) {
         return String.valueOf(value);
      }
      else if (BigDecimal.class.isAssignableFrom(cls)) {
         return new BigDecimal(removeComma(String.valueOf(value)));
      }
      else if (Date.class.isAssignableFrom(cls)) {
         return DateUtil.getDate(String.valueOf(value));
      }
      throw new RuntimeException("Unknown data type : "+cls);
   }

   public static String printpr(int i, Object o) {
      String z = print(o);

      int n = i-z.length();

      if (n<=0) return z;

      char[] p = new char [n];

      for (int j = 0; j < p.length; j++)  p[j] = ' ';

      return new String(p)+z;
   }

   public static String printpl(int i, Object o) {
      String z = print(o);

      int n = i-z.length();

      if (n<=0) return z;

      char[] p = new char [n];

      for (int j = 0; j < p.length; j++)  p[j] = ' ';

      return z+new String(p);
   }
}

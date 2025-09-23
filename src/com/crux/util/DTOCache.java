/***********************************************************************
 * Module:  com.crux.util.DTOCache
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:25:03 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class DTOCache {
   private static DTOCache staticinstance;

   //private final static transient LogManager logger = LogManager.getInstance(DTOCache.class);

   private HashMap classes = new HashMap();
   private HashMap methods = new HashMap();
   private HashMap fields = new HashMap();
   private HashMap pkFields = new HashMap();

   public DTOField getPkField(Class cl) throws Exception {
      DTOField f = (DTOField) pkFields.get(cl.getName());

      if (f==null) {
         loadClass(cl);
         f = (DTOField) pkFields.get(cl.getName());
      }

      return f;
   }

   public static DTOCache getInstance() {
      if (staticinstance == null) staticinstance = new DTOCache();
      return staticinstance;
   }

   private DTOCache() {
   }

   private final static transient LogManager logger = LogManager.getInstance(DTOCache.class);

   public static String getTableName(Class DTOClass) throws Exception {
      return (String) DTOClass.getField("tableName").get(null);
   }

   public void loadClass(Class cl) throws Exception {
      //final Class cl = Class.forName(stClassName);

      final String stClassName = cl.getName();

      final Field fieldMap = cl.getField("fieldMap");

      //if (fieldMap == null) throw new IllegalStateException("Field map not found in "+stClassName);
      if (fieldMap == null) throw new IllegalStateException("Field map not found in "+cl);

      final String [][] fm = (String [][]) fieldMap.get(null);

      final DTOMap hm = new DTOMap();

      for (int i = 0; i < fm.length; i++) {
         final char[] fn = fm[i][0].toCharArray();

         fn[0] = Character.toUpperCase(fn[0]);

         final String suffix = new String(fn);

         final Method mGetter = cl.getMethod("get"+suffix,null);
         final Method mSetter = cl.getMethod("set"+suffix,new Class[] {mGetter.getReturnType()});

         final DTOField df = new DTOField();

         String stDatabaseFieldName = fm[i][1];

         int j;

         if ((j=stDatabaseFieldName.indexOf('*')) != 0) {
            final String[] s = stDatabaseFieldName.split("\\*");
            stDatabaseFieldName = s[0];

            for (int k = 1; k < s.length; k++) {
               String t = s[k];
               if ("r".equalsIgnoreCase(t)) {
                  df.markReadOnly();
               }
               else if ("n".equalsIgnoreCase(t)) {
                  df.markNonDatabaseField();
               }
               else if ("pk".equalsIgnoreCase(t)) {
                  df.markPrimaryKey();
                  df.markReadOnly();
                  pkFields.put(stClassName, df);
               }
               else if ("nd".equalsIgnoreCase(t)) {
                  hm.setNormalDeleteMode(true);
               }
               else if ("clob".equalsIgnoreCase(t)) {
                  df.setStStoreMode(t);
               }
               else if ("clob2".equalsIgnoreCase(t)) {
                  df.setStStoreMode(t);
               }

            }
         }

         df.setStFieldName(fm[i][0]);
         df.setStDatabaseFieldName(stDatabaseFieldName);
         df.setFieldType(mGetter.getReturnType());
         df.setGetter(mGetter);
         df.setSetter(mSetter);

         hm.put(stDatabaseFieldName.toLowerCase(), df);

         methods.put(stClassName+"/"+stDatabaseFieldName.toUpperCase(), df);
         fields.put(stClassName+"/"+df.getStFieldName().toUpperCase(), df);
      }

      classes.put(stClassName, hm);
   }

   public boolean isUsingNormalDelete(Class cl) {
      final DTOMap hm = (DTOMap)classes.get(cl.getName());
      if (hm==null) throw new RuntimeException("Class not found : "+cl);
      return hm.isNormalDeleteMode();
   }

   public HashMap getFields(Class cl) {
      final String stClassName = cl.getName();
      try {
         HashMap m = (HashMap)classes.get(stClassName);

         if (m == null) {
            loadClass(cl);
            m = (HashMap)classes.get(stClassName);
         }

         return m;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new IllegalArgumentException(e.toString());
      }
   }

   //private final static transient LogManager logger = LogManager.getInstance(DTOCache.class);

   public DTOField getDesc(Class cl, String stFieldName) throws Exception {

      final String stClassName = cl.getName();

      //logger.logDebug("getDesc("+stClassName+","+stFieldName+")");

      final String stFieldID = stClassName+"/"+stFieldName.toUpperCase();

      DTOField df = (DTOField) methods.get(stFieldID);

      if (df == null) {
         loadClass(cl);
         df = (DTOField) methods.get(stFieldID);
      }

      return df;
   }

   public Method getGetter(Class cl, String stFieldName) throws Exception {
      final String stClassName = cl.getName();
      final String methodID = stClassName+'/'+stFieldName+"/GET";
      Method method = (Method) methods.get(methodID);
      if (method == null) {
         loadClass(cl);

         method = (Method) methods.get(methodID);
      }

      return method;
   }

   public Method getSetter(Class cl, String stFieldName) throws Exception {
      final String stClassName = cl.getName();
      final String methodID = stClassName+'/'+stFieldName+"/SET";
      Method method = (Method) methods.get(methodID);
      if (method == null) {
         loadClass(cl);

         method = (Method) methods.get(methodID);
      }

      return method;
   }

   public DTOField findField(String cls, String field) {
      if (cls==null) return null;
      try {
         loadClass(Class.forName(cls));
         return (DTOField) fields.get(cls+"/"+field.toUpperCase());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}

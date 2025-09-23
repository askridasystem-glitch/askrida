/***********************************************************************
 * Module:  com.crux.lov.LOVManager
 * Author:  Denny Mahendra
 * Created: Dec 31, 2005 10:46:15 PM
 * Purpose:
 ***********************************************************************/

package com.crux.lov;

import com.crux.util.LOV;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LOVManager {
   private static LOVManager staticinstance;
   public HashMap lovRegistry = new HashMap();
   private boolean hideLogger = false;

   private final static transient LogManager logger = LogManager.getInstance(LOVManager.class);

   public static LOVManager getInstance() {
      if (staticinstance == null) staticinstance = new LOVManager();
      return staticinstance;
   }

   private LOVManager() {
   }

   public void register(Class cls) {
      try {
         final Object server = cls.newInstance();

         final Method[] methods = cls.getMethods();

         for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            if (method.getName().indexOf("LOV")==0) {

               final Class[] pars = method.getParameterTypes();

               if (!LOV.class.isAssignableFrom(method.getReturnType())) {
                  logger.logError("Invalid LOV function declaration : "+method.getName()+" (return type should be LOV descendant) ");
                  continue;
               }

               if (pars.length>1) {
                  logger.logError("Invalid LOV function declaration : "+method.getName()+" (too many parameter) ");
                  continue;
               }

               if (pars.length==1)
                  if (!Map.class.isAssignableFrom(pars[0])) {
                     logger.logError("Invalid LOV function declaration : "+method.getName()+" (invalid parameter type, should be Map descendant) ");
                     continue;
                  }


               lovRegistry.put(
                       method.getName().toLowerCase(),
                       new LOVBean(server,method,pars.length>0)
               );
            }
         }

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private static HashMap pool  = new HashMap();

   public LOV getLOV(String lov, Map lovParameters) {
      String key = lov+lovParameters;
      LOVPoolBean love = (LOVPoolBean)LOVManager.pool.get(key);
      if (love!=null)
         if (love.expire(10*1000)) love=null;
      if (love==null) {
         love = new LOVPoolBean(getLOVx(lov,lovParameters));
         pool.put(key,love);
      }
      return (LOV) love.getObject();
   }

   public String getDescription(String stcode, String lov) {

      final HashMap map = new HashMap();

      map.put("value",stcode);

      final LOV l = getLOV(lov,map);

      if (l==null) return stcode;

      return l.getComboDesc(stcode);
   }

   public String getRef1(String vsGroup, String vsCode) {
      try {
         final SQLUtil S = new SQLUtil();


         try {
            final PreparedStatement PS = S.setQuery("select ref1 from s_valueset where vs_group=? and vs_code=?");
            PS.setString(1,vsGroup);
            PS.setString(2,vsCode);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               String ref1 = RS.getString(1);

               if(!hideLogger) logger.logDebug("getRef1: vsGroup="+vsGroup+" vsCode="+vsCode+" : ref1="+ref1);

               return ref1;
            }

            return null;
         } finally {
            S.release();
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   public String getDescriptionLOV(String vsGroup, String vsCode) {
      try {
         final SQLUtil S = new SQLUtil();


         try {
            final PreparedStatement PS = S.setQuery("select vs_description from s_valueset where vs_group=? and vs_code=? order by orderseq");
            PS.setString(1,vsGroup);
            PS.setString(2,vsCode);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
               String desc = RS.getString(1);

               logger.logDebug("getDescLOV: vsGroup="+vsGroup+" vsCode="+vsCode+" : desc="+desc);

               return desc;
            }

            return null;
         } finally {
            S.release();
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private static class LOVPoolBean {
      private Object object;
      private long t;

      public LOVPoolBean(Object o) {
         t = System.currentTimeMillis();
         object = o;
      }

      public Object getObject() {
         return object;
      }

      public void setObject(Object object) {
         this.object = object;
      }

      public boolean expire(int i) {
         return (System.currentTimeMillis()-t>i);
      }
   }

   public LOV getLOVx(String lov, Map lovParameters) {
      try {
         if (lov.indexOf("VS_")==0) {
            return getValueSetLOV(lov);
         }

         final LOVBean lovBean = (LOVBean)lovRegistry.get(lov.toLowerCase());

         if (lovBean==null) return null;

         if (lovBean.useParameter) {
            if (lovParameters==null) lovParameters = new HashMap();
            return (LOV) lovBean.getter.invoke(lovBean.server, new Object [] {lovParameters});
         } else
            return (LOV) lovBean.getter.invoke(lovBean.server, null);

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private LOV getValueSetLOV(String lov) throws Exception {
      return ListUtil.getLookUpFromQuery(
              "select vs_code,vs_description,default_flag from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
              new Object [] {lov.substring(3,lov.length())}
              );
   }

   private static class LOVBean {
      public Object server;
      public Method getter;
      public boolean useParameter;

      public LOVBean(Object server, Method getter, boolean useParameter) {
         this.server = server;
         this.getter = getter;
         this.useParameter = useParameter;

         logger.logDebug("LOVBean: registered : "+getter.getName()+" served by "+server.getClass()+" "+(useParameter?"using parameters":""));
      }
   }
}

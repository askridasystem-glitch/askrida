/***********************************************************************
 * Module:  com.crux.initializer.ejb.trikomselDS.SchemaInitializer
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 4:51:26 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb.wds;

import com.crux.util.SQLUtil;
import com.crux.util.LogManager;
import com.crux.util.StringTools;
import com.crux.util.stringutil.StringUtil;
import com.crux.common.parameter.Parameter;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class SchemaInitializer {
   protected String ds;
   private static String scriptPath;

   private final static transient LogManager logger = LogManager.getInstance(SchemaInitializer.class);

   public SchemaInitializer(String stDS) {
      ds = stDS;
   }

   protected boolean isInitialized() throws Exception {
      final SQLUtil S = new SQLUtil(ds);

      try {
         S.getConnection();
      }
      catch (Exception e) {
         S.release();
         throw e;
      }

      try {
         final PreparedStatement PS = S.setQuery("select count(*) from s_users");

         final ResultSet RS = PS.executeQuery();

         RS.next();

         RS.getInt(1);

         return true;
      }
      catch (Exception e) {

         System.out.println("e:"+e);

         return false;
      } finally {
         S.release();
      }
   }

   public void doInitialize() throws Exception {
      //if (isInitialized()) return;
      runScriptFile("initdump.sql");
   }

   protected void runScriptFile(String scriptFile) throws Exception {
      logger.logDebug("runScriptFile: "+scriptPath);
      //final InputStream scriptStream = this.getClass().getClassLoader().getResourceAsStream(scriptFile);
      final InputStream scriptStream = new FileInputStream(scriptPath);

      if (scriptStream == null) {
         System.err.println("script file not found : "+scriptFile);
         throw new RuntimeException("script file not found : "+scriptFile);
         //return;
      }

      try {
         final BufferedReader rdr = new BufferedReader(
               new InputStreamReader(
                     scriptStream
               )
         );

         runScript(rdr);
      } finally {
         scriptStream.close();
      }

   }

   protected void runScript(final BufferedReader rdr) throws Exception {
      String s;
      StringBuffer q = new StringBuffer();
      int sci=0;
      int sco=0;

      getConnection();

      try {
         sco = Parameter.readNum("SCHEMA_INFO").intValue();
      } catch (Throwable e) {
      }

      boolean enterBlockMode = false;
      boolean exitBlockMode = false;
      int n;

      while ((s=rdr.readLine())!=null) {
         final int cmt = s.indexOf("--");
         if (cmt>=0) {
            s=s.substring(0,cmt);
         }

         String[] qz = StringUtil.split(s,'\n');

         s = qz[0];
//         final String q0 = s.split("\n")[0];

         n = s.indexOf("<<<");
         if (n>=0){
            s=StringTools.delete(s,n,3);
            enterBlockMode = true;
         }

         if (enterBlockMode) {
            n = s.indexOf(">>>");
            if (n>=0) {
               s=StringTools.delete(s,n,3);
               exitBlockMode = true;
            }
         }

         if (s.length()>0)
            q.append(s).append('\n');

         boolean evaluate =
                 (!enterBlockMode && (s.indexOf(';')>=0)) ||
                 (enterBlockMode && exitBlockMode);

         if (evaluate) {
            String s2 = q.toString().trim();

            if (!enterBlockMode) s2 = StringUtil.split(q.toString().trim(),';')[0];

            enterBlockMode = false;
            exitBlockMode = false;
            evaluate=false;

            if (s2.length()>0) {
               sci++;
               if (sci>sco) {
                  try {
                     execQuery(s2);
                  } catch (Throwable e) {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                  }
               }
            }
            q=new StringBuffer();
         }
      }

      if (sci>sco)
         Parameter.setNumber("SCHEMA_INFO",1,new BigDecimal(sci));
   }

   private void getConnection() throws Exception {

      while (true) {
         final SQLUtil S = new SQLUtil(ds);

         try {
            try {
               S.getConnection();
            } catch (Exception e) {
               logger.logDebug("getConnection: unable to retrieve connection ("+e+")");

               Thread.sleep(2000);

               continue;
            }

            break;
         } finally {
            S.release();
         }
      }
   }

   protected void runScript(String sz) throws Exception {
      final String[] r = StringUtil.split(sz.replace('\n',' '),';');
//      final String[] r = sz.replace('\n',' ').split(";");

      for (int i = 0; i < r.length; i++) {
         String s = r[i].trim();
         if (s.length()>0)
         {
            try {
               execQuery(s);
            } catch (Throwable e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
         }
      }
   }

   protected void execQuery(String q) throws Exception {
      final SQLUtil S = new SQLUtil(ds);

      try {
         final PreparedStatement PS = S.setQuery(q);

         PS.execute();
      }
      finally {
         S.release();
      }
   }

   public static void main(String[] args) throws Exception {
      new SchemaInitializer(null).doInitialize();
   }

   public static void setScript(String realPath) {

      scriptPath = realPath;
   }
}

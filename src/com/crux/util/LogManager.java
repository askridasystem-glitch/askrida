/***********************************************************************
 * Module:  com.crux.util.LogManager
 * Author:  Denny Mahendra
 * Created: Mar 8, 2004 2:56:09 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

//import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.crux.common.config.Config;

import java.util.HashMap;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogManager {
   public static HashMap hmLoggers = new HashMap();
   private String stClassName;
   private String stShortClassName;
   private Logger log4j;

   static {
      /*BigDecimal lv=null;
      try {
         //lv = Parameter.readNum("LOG_LEVEL");
         lv = Level.DEBUG_INT;
      }
      catch (Exception e) {
         System.err.println("Error retrieving log level ("+e.toString()+")");
      }
      logLevel = lv==null?2:lv.intValue();
      LogManager.getInstance(LogManager.class.getName()).logInfo("Debug Level =  "+logLevel);*/
   }

   protected LogManager(String stClassName) {
      this.stClassName = stClassName;
      this.stShortClassName = stClassName.substring(stClassName.lastIndexOf('.')+1, stClassName.length());

      //log4jlogger = Logger.getLogger(stClassName);

//      log4jlogger = null;

      //log4jlogger.setLevel(Level.toLevel(logLevel));
      log4j = Logger.getLogger(stClassName);
      hmLoggers.put(stClassName, this);
   }

   public static LogManager getInstance(Class cl) {
      return getInstance(cl.getName());
   }

   public static LogManager getInstance(String stClassName) {
      LogManager logm = (LogManager) hmLoggers.get(stClassName);

      if (logm == null) {
         logm = new LogManager(stClassName);
      }

      return logm;
   }

   public String getLevelDesc(int level) {
      switch (level) {
         case 0: return "*FATAL";
         case 1: return "*ERROR";
         case 2: return "*WARN ";
         case 3: return " info ";
         case 4: return " debug";
      }

      return "?";
   }

   private void log(int level, String stInfo) {
      System.out.println("["+getLevelDesc(level)+"]:"+stShortClassName+":"+stInfo);

   }

   public void logDebug(String stInfo) {
      log(4,stInfo);
   }

   public void logError(String stInfo) {
      log(1,stInfo);
/*
      if (log4jlogger == null)
         log(ERROR,stInfo);
      else
         log4jlogger.error(stInfo);
*/
   }

   public void logInfo(String stInfo) {
      log(3,stInfo);
/*
      if (log4jlogger == null)
         log(INFO,stInfo);
      else
         log4jlogger.info(stInfo);
*/
   }

   public void logWarning(String stInfo) {
      log(2,stInfo);
      log4j.warn(stInfo);
//      if (log4jlogger == null)
//         log(WARNING,stInfo);
//      else
//         log4jlogger.warn(stInfo);
   }

   public void logFatal(String stInfo) {
      log(0,stInfo);
//      if (log4jlogger == null)
//         log(FATAL,stInfo);
//      else
//         log4jlogger.fatal(stInfo);
   }

   public void log(Throwable e) {
      String s="?";
      if (Config.JRE_1_4)
         while (e.getCause()!=null) e=e.getCause();
      if (e==null) {
         s="(No error ?)";
      }
      if (e.getMessage() == null) s=e.toString();
      s=e.getMessage();

      StringWriter sw = new StringWriter();

      e.printStackTrace(
              new PrintWriter(
                      sw
              )
      );

      logError(sw.toString());
   }
}

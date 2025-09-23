/***********************************************************************
 * Module:  com.crux.util.jasper.JasperUtil
 * Author:  Denny Mahendra
 * Created: Sep 10, 2004 4:10:04 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.jasper;

import net.sf.jasperreports.engine.JasperCompileManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.crux.util.LogManager;

public class JasperUtil {

   private final static transient LogManager logger = LogManager.getInstance(JasperUtil.class);

   public static String getJasperPath(HttpServletRequest rq , String stJasperName) {
      final ServletContext servletContext = rq.getSession().getServletContext();

      return servletContext.getRealPath(stJasperName);
   }

   public static void compile(String jasperDesign, String jasperFile) throws Exception {

      logger.logDebug("compile: ('"+jasperDesign+"','"+jasperFile+"')");

      FileInputStream fi=null;
      FileOutputStream fo=null;
      try {
         fi = new FileInputStream(jasperDesign);
         fo = new FileOutputStream(jasperFile);

         JasperCompileManager.compileReportToStream(fi,fo);

      } finally {
         if (fi!=null) fi.close();
         if (fo!=null) fo.close();
      }
   }

   /*public static String compileJasper(String design, String jasperOutput) {

      JasperCompileManager.compileReportToStream();
   }*/
}

/***********************************************************************
 * Module:  com.webfin.insurance.helper.InsurancePolicyHelper
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 9:57:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.manual.helper;

import com.crux.common.controller.Helper;
import com.crux.common.model.DTO;
import com.crux.util.*;
import com.webfin.FinCodec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.File;

public class PrintManualBookHelper extends Helper{
   private final static transient LogManager logger = LogManager.getInstance(PrintManualBookHelper.class);


   private static HashSet formList = null;

   public void printManual(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
    
      loadFormList(rq);
      
      //if(true) throw new RuntimeException(formList.toString());
      
      final String manualbook = rq.getParameter("manualbook");

      String urx=null;
      
      //urx = "/pages/manual/"+ manualbook;
      
      if (formList.contains(manualbook)) {
            urx = "/pages/manual/docs/"+ manualbook;
         }
      
      if (urx==null) throw new RuntimeException("Tidak Ada File PDF Yang Diinginkan");

      logger.logDebug("printManual: forwarding to ########## "+urx);

     
      if(manualbook.endsWith("xls")){
            rp.setContentType("application/vnd.ms-excel");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else if(manualbook.endsWith("zip")){
            rp.setContentType("application/zip");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else{
           rp.setContentType("application/pdf");
           rp.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
           rp.setHeader("Pragma", "token");
      }

      //rp.getOutputStream().flush();
      rq.getRequestDispatcher(urx).forward(rq,rp);
      return;
      
   }
   
   public void printManual2(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
    
      loadFormList(rq);
      
      //if(true) throw new RuntimeException(formList.toString());
      
      final String manualbook = rq.getParameter("manualbook");

      String urx=null;
      
      //urx = "/pages/manual/"+ manualbook;
      //logger.logDebug("formList = "+formList);
      
      if (formList.contains(manualbook)) {
            urx = "/pages/manual/docs/"+ manualbook;
         }
      
      if (urx==null) throw new RuntimeException("Tidak Ada File PDF Yang Diinginkan");

      logger.logDebug("printManual: forwarding to ########## "+urx);

      rp.setContentType("text/html");
      rp.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".chm;");
      rp.setHeader("Pragma", "token");
      rp.getOutputStream().flush();
      //rq.getRequestDispatcher(urx).forward(rq,rp);
      rp.sendRedirect(urx);
      
   }

   private void loadFormList(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/manual/docs")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

   public void printInsuranceTools(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

      loadInsuranceToolsList(rq);

      //if(true) throw new RuntimeException(formList.toString());

      final String manualbook = rq.getParameter("manualbook");

      String urx=null;

      //urx = "/pages/manual/"+ manualbook;

      if (formList.contains(manualbook)) {
            urx = "/pages/insurance/pword/docs/"+ manualbook;
         }

      if (urx==null) throw new RuntimeException("Tidak Ada File PDF Yang Diinginkan");

      logger.logDebug("printManual: forwarding to ########## "+urx);


      if(manualbook.endsWith("xls")){
            rp.setContentType("application/vnd.ms-excel");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else if(manualbook.endsWith("zip")){
            rp.setContentType("application/zip");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else{
           rp.setContentType("application/pdf");
           rp.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
           rp.setHeader("Pragma", "token");
      }

      //rp.getOutputStream().flush();
      rq.getRequestDispatcher(urx).forward(rq,rp);
      return;

   }

   private void loadInsuranceToolsList(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/insurance/pword/docs")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

   private void loadFormList2(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/manual/docs/usermanual")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

   public void printManual3(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

      loadFormList2(rq);

      //if(true) throw new RuntimeException(formList.toString());

      final String manualbook = rq.getParameter("manualbook");

      String urx=null;

      //urx = "/pages/manual/"+ manualbook;

      if (formList.contains(manualbook)) {
            urx = "/pages/manual/docs/usermanual/"+ manualbook;
         }

      if (urx==null) throw new RuntimeException("Tidak Ada File PDF Yang Diinginkan");

      logger.logDebug("printManual: forwarding to ########## "+urx);


      if(manualbook.endsWith("xls")){
            rp.setContentType("application/vnd.ms-excel");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else if(manualbook.endsWith("zip")){
            rp.setContentType("application/zip");
            rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
            rp.setHeader("Pragma", "token");
      }else{
           rp.setContentType("application/pdf");
           rp.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
           rp.setHeader("Pragma", "token");
      }

      //rp.getOutputStream().flush();
      rq.getRequestDispatcher(urx).forward(rq,rp);
      return;

   }

}

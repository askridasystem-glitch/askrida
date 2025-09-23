/***********************************************************************
 * Module:  com.webfin.insurance.helper.InsurancePolicyHelper
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 9:57:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.utilities.helper;

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

public class UtilitiesHelper extends Helper{
   private final static transient LogManager logger = LogManager.getInstance(UtilitiesHelper.class);


   private static HashSet formList = null;

   public void printManual(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
    
      loadFormList(rq);
      
      final String manualbook = rq.getParameter("manualbook");

      String urx=null;
      
      if (formList.contains(manualbook)) {
            urx = "/pages/utilities/docs/"+ manualbook;
         }
      
      if (urx==null) throw new RuntimeException("Tidak Ada File Yang Diinginkan");

      logger.logDebug("printManual: forwarding to ########## "+urx);

      rp.setContentType("application/pdf");
      rp.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
      if(manualbook.endsWith("xls")){
      		rp.setContentType("application/vnd.ms-excel");
        	rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
                rp.setHeader("Pragma", "token");
      }else if(manualbook.endsWith("doc")){
      		rp.setContentType("application/vnd.ms-word");
        	rp.setHeader("Content-Disposition","attachment; filename="+manualbook+";");
                rp.setHeader("Pragma", "token");
      }

      rp.getOutputStream().flush();
      rq.getRequestDispatcher(urx).forward(rq,rp);
      
   }
   
   private void loadFormList(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/utilities/docs")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

}

/***********************************************************************
 * Module:  com.crux.file.helper.FileUploadHelper
 * Author:  Denny Mahendra
 * Created: Jul 28, 2006 1:07:38 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.file.helper;

import com.crux.common.controller.Helper;
import com.crux.file.FileView;
import com.crux.file.FileManager;
import com.crux.util.LogManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Enumeration;
import java.io.FileInputStream;

import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;



public class FileUploadHelper extends Helper {
   private final static transient LogManager logger = LogManager.getInstance(FileUploadHelper.class);
   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }
   public void upload(HttpServletRequest rq)  throws Exception {

      String fileID = getString(rq.getParameter("file_id"));
      //String field_name = getString(rq.getParameter("field_name"));

      if (fileID!=null) {
         rq.setAttribute("FILEDESC",FileManager.getInstance().getFileDesc(fileID));
         
      }

      Enumeration n = rq.getAttributeNames();

      FileView fv = null;

      while (n.hasMoreElements()) {
         String s = (String) n.nextElement();

         Object x = rq.getAttribute(s);

         if (x instanceof FileView) {
            fv = (FileView) x;

            String p = s.substring(0, s.length()-5);
            p = p.substring(5,p.length());

            fv.setStFileGroup(rq.getParameter("group"));
            fv.setStDescription(getString(rq.getParameter("oFilen"+p)));

            fv.store();

         }
      }
      if (fv!=null) {
         rq.setAttribute("UPLOADED_FILE",fv.getStFileID());
         rq.setAttribute("UPLOADED_DESC",fv.getStDescription());
         rq.setAttribute("IMAGE",fv.getStImageFlag());
         //rq.setAttribute("field_name",field_name);
         //getRemoteInsurance().setID(fv.getStFileID());
      }
      
   }
   
   public void uploadNotSave(HttpServletRequest rq)  throws Exception {

      String fileID = getString(rq.getParameter("file_id"));

      if (fileID!=null) {
         rq.setAttribute("FILEDESC",FileManager.getInstance().getFileDesc(fileID));
      }

      Enumeration n = rq.getAttributeNames();

      FileView fv = null;


      while (n.hasMoreElements()) {
         String s = (String) n.nextElement();

         Object x = rq.getAttribute(s);

         if (x instanceof FileView) {
            fv = (FileView) x;

            String p = s.substring(0, s.length()-5);
            p = p.substring(5,p.length());

            fv.setStFileGroup("excel");
            fv.setStDescription(getString(rq.getParameter("oFilen"+p)));

            fv.store();



         }
      }
      if (fv!=null) {
         rq.setAttribute("UPLOADED_FILE",fv.getStFileID());
         rq.setAttribute("UPLOADED_DESC",fv.getStDescription());
         rq.setAttribute("IMAGE",fv.getStImageFlag());
      }
      
   }

   public static void enableCache(HttpServletResponse httpServletResponse) {
      // Set to expire far in the past.
      httpServletResponse.setHeader("Expires", null);

      // Set standard HTTP/1.1 no-cache headers.
      httpServletResponse.setHeader("Cache-Control", null);

      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
      httpServletResponse.addHeader("Cache-Control", null);

      // Set standard HTTP/1.0 no-cache header.
      httpServletResponse.setHeader("Pragma", null);
   }

   public void download(HttpServletRequest rq, HttpServletResponse rp)  throws Exception {
      String fileID = getString(rq.getParameter("fileid"));
      String thumb = getString(rq.getParameter("thumb"));

      FileView file = FileManager.getInstance().getFile(fileID);

      if (file==null) return;

      enableCache(rp);

      if (thumb!=null)
         rp.setContentType("image/jpeg");
      else
         rp.setContentType(file.getStMimeType());

      rp.setHeader("Content-Disposition","inline; filename="+file.getStOriginalName()+";");
      rp.setContentLength((int) file.getDbOriginalSize().longValue());

      FileInputStream fi=null;
      ServletOutputStream os=null;

      String fn = file.getStFilePath();

      if (thumb!=null) fn+="_thumb_"+thumb;

      try {
         fi = new FileInputStream(fn);

         os = rp.getOutputStream();

         byte[] buf = new byte [4096];

         while (fi.available()>0) {
            int n = fi.read(buf);
            os.write(buf,0,n);
         }
      } finally {
         if (fi!=null) fi.close();
         //if (os!=null) os.close();
      }
   }
}

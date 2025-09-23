/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.archive.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.webfin.archive.ejb.ArchiveManager;
import com.webfin.archive.ejb.ArchiveManagerHome;
import com.webfin.archive.filter.ArchiveFilter;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;

public class ArchiveHelper extends Helper {
   private ArchiveManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ArchiveManagerHome) JNDIUtil.getInstance().lookup("ArchiveManagerEJB",ArchiveManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      ArchiveFilter f;

      f=(ArchiveFilter) get(rq,"FILTER");

      if (f==null) {
         f = new ArchiveFilter();
         put(rq,"FILTER",f);
      }
      updatePaging(rq,f);

      final DTOList l = getRemoteEntityManager().listEntities(f);

      rq.setAttribute("LIST",l);
   }

   public void create(HttpServletRequest rq)  throws Exception {
   }

   public void edit(HttpServletRequest rq)  throws Exception {
   }

   public void view(HttpServletRequest rq)  throws Exception {
   }

   public void retrieve(HttpServletRequest rq)  throws Exception {
   }

   public void populate(HttpServletRequest rq)  throws Exception {
   }

   public void save(HttpServletRequest rq)  throws Exception {
   }
}

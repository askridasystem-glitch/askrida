/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.approval.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.webfin.incoming.filter.IncomingFilter;
import com.webfin.incoming.model.IncomingView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletResponse;

public class IncomingHelper extends Helper {
   private IncomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      IncomingFilter f;

      f=(IncomingFilter) get(rq,"FILTER");

      if (f==null) {
         f = new IncomingFilter();
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
   
   public void print(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
        
        final String incomingid = rq.getParameter("inid");
        
        final IncomingView inbox = getRemoteEntityManager().loadEntity(incomingid);
        
        rq.setAttribute("INCOMING",inbox);
        rq.setAttribute("LOCK_PDF","N");
        
        final String urx = "/pages/incoming/report/incoming.fop";

        rq.getRequestDispatcher(urx).forward(rq,rp);
    }
}

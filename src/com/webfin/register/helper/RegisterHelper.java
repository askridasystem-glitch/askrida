/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.register.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.webfin.register.ejb.RegisterManager;
import com.webfin.register.ejb.RegisterManagerHome;
import com.webfin.register.filter.RegisterFilter;
import com.webfin.register.model.RegisterView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletResponse;

public class RegisterHelper extends Helper {
   private RegisterManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((RegisterManagerHome) JNDIUtil.getInstance().lookup("RegisterManagerEJB",RegisterManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      RegisterFilter f;

      f=(RegisterFilter) get(rq,"FILTER");

      if (f==null) {
         f = new RegisterFilter();
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
        
        final String registerid = rq.getParameter("outid");
        
        final RegisterView reg = getRemoteEntityManager().loadEntity(registerid);
        
        rq.setAttribute("REGISTER",reg);
        //rq.setAttribute("LOCK_PDF","N");
        
        final String urx = "/pages/register/report/register.fop";

        rq.getRequestDispatcher(urx).forward(rq,rp);
    }
   
}

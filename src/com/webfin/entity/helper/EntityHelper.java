/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;
import com.webfin.entity.filter.EntityFilter;
import com.webfin.entity.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;

public class EntityHelper extends Helper {
   private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB",EntityManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      EntityFilter f;

      f=(EntityFilter) get(rq,"FILTER");

      if (f==null) {
         f = new EntityFilter();
         put(rq,"FILTER",f);
      }
      updatePaging(rq,f);

      final DTOList l = getRemoteEntityManager().listEntities(f);

      rq.setAttribute("LIST",l);
   }

   public void create(HttpServletRequest rq)  throws Exception {
   	           
        EntityView view = new EntityView();
        
        view.markNew();

        //rq.setAttribute("JH",view);
 
        //populate(rq,view);
    
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
   
   public void select(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      //String costcenter = getString(rq.getParameter("costcenter"));

      final SelectEntityForm form = new SelectEntityForm ();

      //if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

      form.setStKey(key);
      //form.setStCostCenter(costcenter);

      if (key!=null) {
         final DTOList list = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      * " +
                 "   from " +
                 "      ent_master a" +
                 "   where " +
                 "      upper(a.ent_id || a.ent_name) like ?" +
                 "   limit 100",
                 new Object [] {'%'+key.toUpperCase()+'%'},
                 EntityView.class);

         rq.setAttribute("LIST",list);
      }

      rq.setAttribute("FORM",form);
   }
}

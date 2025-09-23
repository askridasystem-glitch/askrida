/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.forms;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;
import com.webfin.entity.filter.EntityFilter;
import com.webfin.entity.model.EntityView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class EntityListForm extends Form {
   private DTOList list;
   private String entityid;
   private String branch;

   public void initialize() {
      //branch = SessionManager.getInstance().getSession().getStBranch();
   }

   public String getBranch() {
      return branch;
   }

   public void setBranch(String branch) {
      this.branch = branch;
   }

   public String getEntityid() {
      return entityid;
   }

   public void setEntityid(String entityid) {
      this.entityid = entityid;
   }

   private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB",EntityManagerHome.class.getName()))
            .create();
   }

   public DTOList getList() throws Exception {
        if (list == null) {
            list = new DTOList();
            final EntityFilter f = new EntityFilter();
            f.orderKey = "ent_id";
            f.stKey = "ent_name";
            list.setFilter(f.activate());
        }
        //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");
        sqa.addQuery(" from ent_master");

        if (branch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(branch);
        }

        sqa.addClause("coalesce(active_flag,'Y') <> 'N'");

        sqa.setLimit(800);

        /*
        UserSessionView us = SessionManager.getInstance().getSession();

        if(us.getStBranch()==null||"00".equalsIgnoreCase(us.getStBranch())){
        }else{
        sqa.addClause("(sharef0=? or sharef1=? or sharef2='NAT')");
        sqa.addPar(us.getStUserID());
        sqa.addPar(us.getStBranch());
        }


        if (branch!=null) {
        sqa.addClause("cc_code=?");
        sqa.addPar(branch);
        }*/

        sqa.addFilter(list.getFilter());

        list = sqa.getList(EntityView.class);

        return list;
    }


   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      final EntityMasterForm form = (EntityMasterForm) super.newForm("entity_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final EntityMasterForm form = (EntityMasterForm) super.newForm("entity_edit",this);

      form.setAttribute("ent_id",entityid);

      form.edit();

      form.show();
   }

   public void clickView() throws Exception {
      final EntityMasterForm form = (EntityMasterForm) super.newForm("entity_edit",this);

      form.setAttribute("ent_id",entityid);

      form.view();

      form.show();
   }

   public void refresh() {
   }

   public void clickCheck() throws Exception {
        final EntityMasterForm form = (EntityMasterForm) super.newForm("entity_check", this);

        form.createNewCheck();

        form.show();
    }

}

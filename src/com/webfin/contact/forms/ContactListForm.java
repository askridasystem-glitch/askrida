/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.contact.forms;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.common.model.Filter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.login.model.UserSessionView;
import com.webfin.contact.ejb.ContactManager;
import com.webfin.contact.ejb.ContactManagerHome;
import com.webfin.contact.filter.ContactFilter;
import com.webfin.contact.model.ContactView;
import com.crux.util.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ContactListForm extends Form {
   private DTOList list;
   private String entityid;
   private String branch;

   public void initialize() {
      branch = SessionManager.getInstance().getSession().getStBranch();
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

   private ContactManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ContactManagerHome) JNDIUtil.getInstance().lookup("ContactManagerEJB",ContactManagerHome.class.getName()))
            .create();
   }

   public DTOList getList() throws Exception {
      if (list==null) {
         list=new DTOList();
         final ContactFilter f = new ContactFilter();
         f.orderKey="ent_id";
         list.setFilter(f.activate());
      }
      //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from customer_master");

      UserSessionView us = SessionManager.getInstance().getSession();

      //sqa.addClause("(sharef0=? or sharef1=? or sharef2='NAT')");
      //sqa.addPar(us.getStUserID());
      //sqa.addPar(us.getStBranch());

      //if (branch!=null) {
         //sqa.addClause("cc_code=?");
         //sqa.addPar(branch);
      //}

      sqa.addFilter(list.getFilter());

      list = sqa.getList(ContactView.class);

      return list;
   }


   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      final ContactMasterForm form = (ContactMasterForm) super.newForm("contact_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final ContactMasterForm form = (ContactMasterForm) super.newForm("contact_edit",this);

      form.setAttribute("ent_id",entityid);

      form.edit();

      form.show();
   }

   public void clickView() throws Exception {
      final ContactMasterForm form = (ContactMasterForm) super.newForm("contact_edit",this);

      form.setAttribute("ent_id",entityid);

      form.view();

      form.show();
   }

   public void clickDelete()throws Exception{
	   final ContactMasterForm form = (ContactMasterForm) super.newForm("contact_edit",this);
	   
	   form.setAttribute("ent_id",entityid);
	   
	   if(entityid==null) 
	   {
		   throw new RuntimeException("Choose contact first !");
	   } 
	   else
	   {
		   String delete_master = "Delete from customer_master where ent_id='"+ entityid +"'";
		   String delete_address = "Delete from customer_address where ent_id='"+ entityid +"'";
		   
		   final SQLUtil sql = new SQLUtil();
		   sql.execSQL(delete_master);
		   sql.execSQL(delete_address);
	   }
	  
	   
	   
	
       
   }
   
   public void refresh() {
   }
}

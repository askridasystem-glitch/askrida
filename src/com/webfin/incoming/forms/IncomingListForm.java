/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.incoming.forms;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.common.model.Filter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.login.model.UserSessionView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.webfin.incoming.filter.IncomingFilter;
import com.webfin.incoming.model.IncomingView;
import com.crux.util.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class IncomingListForm extends Form {
   private DTOList list;
   private String inid;
   private String refno;
   private String branch;
   private String stUnreadFlag="Y";
   
    public String getStUnreadFlag() {
      return stUnreadFlag;
   }

   public void setStUnreadFlag(String stUnreadFlag) {
      this.stUnreadFlag = stUnreadFlag;
   }

   public void initialize() {
      branch = SessionManager.getInstance().getSession().getStBranch();
   }

   public String getBranch() {
      return branch;
   }

   public void setBranch(String branch) {
      this.branch = branch;
   }

   public String getRefNo() {
      return refno;
   }

   public void setRefNo(String refno) {
      this.refno = refno;
   }
   
   public String getInID() {
      return inid;
   }

   public void setInID(String inid) {
      this.inid = inid;
   }

   private IncomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
            .create();
   }

   public DTOList getList() throws Exception {
      if (list==null) {
         list=new DTOList();
         final IncomingFilter f = new IncomingFilter();
         //f.orderKey="in_id desc";
         list.setFilter(f.activate());
      }
      //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*,date_trunc('second',create_date)::time::varchar as jam");
      sqa.addQuery(" from incoming_letter");

      UserSessionView us = SessionManager.getInstance().getSession();

      //sqa.addClause("(sharef0=? or sharef1=? or sharef2='NAT')");
      //sqa.addPar(us.getStUserID());
      //sqa.addPar(us.getStBranch());
	  sqa.addClause("receiver = ?");
	  sqa.addPar(us.getStUserID());
	  
	  sqa.addClause("delete_flag = ?");
	  sqa.addPar("N");
	  
      if (stUnreadFlag!=null) {
         sqa.addClause("read_flag=?");
         if(stUnreadFlag.equalsIgnoreCase("Y"))
         		sqa.addPar("N");
         else
         		sqa.addPar("Y");
      }
      sqa.addOrder("in_id desc");
      sqa.addFilter(list.getFilter());

      list = sqa.getList(IncomingView.class);
      return list;
   }


   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      final IncomingMasterForm form = (IncomingMasterForm) super.newForm("incoming_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final IncomingMasterForm form = (IncomingMasterForm) super.newForm("incoming_edit",this);

      form.setAttribute("inid",inid);

      form.edit();

      form.show();
   }

   public void clickView() throws Exception {
      final IncomingMasterForm form = (IncomingMasterForm) super.newForm("incoming_edit",this);

      form.setAttribute("inid",inid);

      form.view();

      form.show();
   }

   public void clickDelete()throws Exception{
	   final IncomingMasterForm form = (IncomingMasterForm) super.newForm("incoming_edit",this);
	   
	   form.setAttribute("inid",inid);
	   
	   if(inid==null) 
	   {
		   throw new RuntimeException("Choose letter first !");
	   } 
	   else
	   {
		   String delete_master = "Update incoming_letter set delete_flag = 'Y' where in_id='"+ inid +"'";
		   //String delete_address = "Delete from incoming_dist where in_id='"+ inid +"'";
		   
		   final SQLUtil sql = new SQLUtil();
		   sql.execSQL(delete_master);
		   //sql.execSQL(delete_address);
                   sql.releaseResource();
                   sql.release();
	   }
	  

       
   }
   
   public void refresh() {
   }
   
   public void clickForward() throws Exception {
      final IncomingMasterForm form = (IncomingMasterForm) super.newForm("incoming_edit",this);

      form.setAttribute("inid",inid);

      form.edit();

      form.show();
   }
}

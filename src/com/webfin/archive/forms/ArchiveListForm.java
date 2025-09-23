/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.archive.forms;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.common.model.Filter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.login.model.UserSessionView;
import com.webfin.archive.ejb.ArchiveManager;
import com.webfin.archive.ejb.ArchiveManagerHome;
import com.webfin.archive.filter.ArchiveFilter;
import com.webfin.archive.model.ArchiveView;
import com.crux.util.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ArchiveListForm extends Form {
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

   private ArchiveManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ArchiveManagerHome) JNDIUtil.getInstance().lookup("ArchiveManagerEJB",ArchiveManagerHome.class.getName()))
            .create();
   }

   public DTOList getList() throws Exception {
      if (list==null) {
         list=new DTOList();
         final ArchiveFilter f = new ArchiveFilter();
         //f.orderKey="in_id desc";
         list.setFilter(f.activate());
      }
      //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from archive_doc");

      sqa.addFilter(list.getFilter());

      list = sqa.getList(ArchiveView.class);
      return list;
   }


   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      final ArchiveMasterForm form = (ArchiveMasterForm) super.newForm("archive_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final ArchiveMasterForm form = (ArchiveMasterForm) super.newForm("archive_edit",this);

      form.setAttribute("inid",inid);

      form.edit();

      form.show();
   }

   public void clickView() throws Exception {
      final ArchiveMasterForm form = (ArchiveMasterForm) super.newForm("archive_edit",this);

      form.setAttribute("inid",inid);

      form.view();

      form.show();
   }

   public void clickDelete()throws Exception{
	   final ArchiveMasterForm form = (ArchiveMasterForm) super.newForm("archive_edit",this);
	   
	   form.setAttribute("inid",inid);
	   
	   if(inid==null) 
	   {
		   throw new RuntimeException("Choose letter first !");
	   } 
	   else
	   {
		   String delete_master = "Update archive_letter set delete_flag = 'Y' where in_id='"+ inid +"'";
		   //String delete_address = "Delete from archive_dist where in_id='"+ inid +"'";
		   
		   final SQLUtil sql = new SQLUtil();
		   sql.execSQL(delete_master);
		   //sql.execSQL(delete_address);
	   }
	  

       
   }
   
   public void refresh() {
   }
}

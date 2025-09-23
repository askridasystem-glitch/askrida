/***********************************************************************
 * Module:  com.webfin.archive.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.archive.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserRoleView;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.archive.model.ArchiveView;
//import com.webfin.archive.model.ArchiveDistributionView;
import com.webfin.archive.ejb.ArchiveManager;
import com.webfin.archive.ejb.ArchiveManagerHome;
import com.crux.util.SQLAssembler;
import com.jspsmart.upload.Request;
import com.webfin.outcoming.forms.OutcomingMasterForm;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ArchiveMasterForm extends WebForm {
   private ArchiveView archive = null;

   private LOV lovPaymentTerm;
   private FormTab tabs;
   //private ArchiveDistributionView address;
   private String stSelectedAddress;
   private String userIndex;
   private String addressid;

   

   public void createNew() {
      archive = new ArchiveView();
      archive.markNew();
   }

   public void onChangeBranch() {
   }

   public void onClassChange() {

   }
   
   public String getUserIndex() {
	      return userIndex;
	  }

	   public void setUserIndex(String userIndex) {
	      this.userIndex = userIndex;
	}
   
   public void doDeleteAddress() {
	   
		  
//	   final ArchiveDistributionView adr = new ArchiveDistributionView();
//	     
//	   final Integer id = NumberUtil.getInteger(stSelectedAddress);
//	   String id2 = id.toString();
//	   int id3 = Integer.parseInt(stSelectedAddress);
//	    
//	   archive.getDistributions().delete(id3);
//	   
//	   
//	   System.out.println("Id= "+ id);
	   
	   //
	   //int n = Integer.parseInt(userIndex);
	   //user.getUserroles().delete(n);
	  int n = Integer.parseInt(stSelectedAddress);

      //final DTOList objects = archive.getDistributions();
		
	  //archive.getDistributions().delete(n);

      //objects.delete(n);
      
      //getLovAddresses();
      
      stSelectedAddress=null;
      
      //getTabs();
	     
	  
   }

   public void edit() throws Exception {
      view();
      setReadOnly(false);

      archive.markUpdate();

      
   }

   public void view() throws Exception {
      final String archive_id = (String)getAttribute("inid");
      archive=getRemoteEntityManager().loadEntity(archive_id);
	  	  
      if (archive==null) throw new RuntimeException("Entity not found !");

      setReadOnly(true);
   }

   public void afterUpdateForm() {
     
   }

   public String getStSelectedAddress() {
      return stSelectedAddress;
   }


   

   public void selectAddress() {
      // do nothing
   }

   public void setTabs(FormTab tabs) {
      this.tabs = tabs;
   }

   public LOV getLovPaymentTerm() throws Exception {
      if (lovPaymentTerm==null) lovPaymentTerm = ListUtil.getLookUpFromQuery("select payment_term_id,description from payment_term");
      return lovPaymentTerm;
   }

   public void setLovPaymentTerm(LOV lovPaymentTerm) {
      this.lovPaymentTerm = lovPaymentTerm;
   }

   public ArchiveMasterForm() {
   }

   public ArchiveView getEntity() {
      return archive;
   }

   public void setEntity(ArchiveView archive) {
      this.archive = archive;
   }

   private ArchiveManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ArchiveManagerHome) JNDIUtil.getInstance().lookup("ArchiveManagerEJB",ArchiveManagerHome.class.getName()))
            .create();
   }

   public void doSave() throws Exception {
      final ArchiveView cloned = (ArchiveView)ObjectCloner.deepCopy(archive);
      getRemoteEntityManager().save(cloned);
      super.close();
   }

   public void doDelete() throws Exception
   {
	   //getRemoteEntityManager().
   }
   
   public void doClose() {
      super.close();
   }
   
   
}

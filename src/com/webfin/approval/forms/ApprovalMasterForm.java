/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.approval.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserRoleView;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.model.IncomingDistributionView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.crux.util.SQLAssembler;
import com.jspsmart.upload.Request;
import com.webfin.approval.model.ApprovalView;
import com.webfin.outcoming.forms.OutcomingMasterForm;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ApprovalMasterForm extends WebForm {
   private ApprovalView entity = null;

   private LOV lovPaymentTerm;
   private FormTab tabs;
   private IncomingDistributionView address;
   private String stSelectedAddress;
   private String userIndex;
   private String addressid;
   private String instIndex;
   private DTOList document;

   public LOV getLovAddresses() {
      return entity.getDistributions();
   }

   public void createNew() {
      entity = new ApprovalView();
      entity.markNew();
      //entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
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
	   
		  
//	   final IncomingDistributionView adr = new IncomingDistributionView();
//	     
//	   final Integer id = NumberUtil.getInteger(stSelectedAddress);
//	   String id2 = id.toString();
//	   int id3 = Integer.parseInt(stSelectedAddress);
//	    
//	   entity.getDistributions().delete(id3);
//	   
//	   
//	   System.out.println("Id= "+ id);
	   
	   //
	   //int n = Integer.parseInt(userIndex);
	   //user.getUserroles().delete(n);
	  int n = Integer.parseInt(stSelectedAddress);

      //final DTOList objects = entity.getDistributions();
		
	  entity.getDistributions().delete(n);

      //objects.delete(n);
      
      //getLovAddresses();
      
      stSelectedAddress=null;
      
      getTabs();
	     
	  
   }

   public void edit() throws Exception {
      view();
      setReadOnly(false);

      entity.markUpdate();

      final DTOList addresses = entity.getDistributions();

      for (int i = 0; i < addresses.size(); i++) {
         IncomingDistributionView ead = (IncomingDistributionView) addresses.get(i);

         ead.markUpdate();
      }
   }

   public void view() throws Exception {
      final String entity_id = (String)getAttribute("inid");
      entity=getRemoteEntityManager().loadApprove(entity_id);
	  
//      if(entity.getStReadFlag().equalsIgnoreCase("N"))
//	  		getRemoteEntityManager().updateReadStatus(entity,"Y");
	  
      if (entity==null) throw new RuntimeException("Entity not found !");

      setReadOnly(true);
   }

   public void afterUpdateForm() {
      if (entity!=null) {
         final Integer idx = NumberUtil.getInteger(stSelectedAddress);
         address = idx==null?null:(IncomingDistributionView) entity.getDistributions().get(idx.intValue());

         final DTOList addresses = entity.getDistributions();

         entity.setStReceiver(null);

         for (int i = 0; i < addresses.size(); i++) {
            IncomingDistributionView adr = (IncomingDistributionView) addresses.get(i);

            if (entity.getStReceiver()==null)
               entity.setStReceiver(adr.getStReceiver());

//            if (adr.isPrimary()) {
//               entity.setStAddress(adr.getStAddress());
//               break;
//            }
         }
      }
   }

   public String getStSelectedAddress() {
      return stSelectedAddress;
   }

   public void setStSelectedAddress(String stSelectedAddress) {
      this.stSelectedAddress = stSelectedAddress;
   }

   public IncomingDistributionView getAddress() {
      return address;
   }

   public void setAddress(IncomingDistributionView address) {
      this.address = address;
   }



   public void doNewAddress() {
      final IncomingDistributionView adr = new IncomingDistributionView();
     
      adr.markNew();

      entity.getDistributions().add(adr);

      stSelectedAddress = String.valueOf(entity.getDistributions().size()-1);

      address = adr;
   }

   public void selectAddress() {
      // do nothing
   }

   public FormTab getTabs() {
      if (tabs==null) {
         tabs = new FormTab();

         tabs.add(new FormTab.TabBean("TAB0","APPROVAL",true));
         //tabs.add(new FormTab.TabBean("TAB1","LAMPIRAN",true));
        
         tabs.setActiveTab("TAB0");
      }
      
      return tabs;
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

   public ApprovalMasterForm() {
   }

   public ApprovalView getEntity() {
      return entity;
   }

   public void setEntity(ApprovalView entity) {
      this.entity = entity;
   }

   private IncomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
            .create();
   }

   public void doSave() throws Exception {
      final IncomingView cloned = (IncomingView)ObjectCloner.deepCopy(entity);
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
   
   public void doReply() throws Exception{
//      final OutcomingMasterForm form = (OutcomingMasterForm) super.newForm("outcoming_edit", this);
//
//      form.reply(entity);
//
//      form.setReplyMode(true);
//
//      form.show();
   }
   
    public void forward() throws Exception {
//      final OutcomingMasterForm form = (OutcomingMasterForm) super.newForm("outcoming_edit", this);
//
//      form.replyForward(entity);
//
//      form.setReplyMode(true);
//
//      form.show();
   }

    public String getInstIndex()
    {
        return instIndex;
    }

    public void setInstIndex(String instIndex)
    {
        this.instIndex = instIndex;
    }
    
    public DTOList getDocument() throws Exception {
        return entity.getDocuments();
    }

    public void setDocument(DTOList document)
    {
        this.document = document;
    }
}

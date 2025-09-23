/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.contact.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserRoleView;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.contact.model.ContactView;
import com.webfin.contact.model.ContactAddressView;
import com.webfin.contact.ejb.ContactManager;
import com.webfin.contact.ejb.ContactManagerHome;
import com.crux.util.SQLAssembler;
import com.jspsmart.upload.Request;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ContactMasterForm extends WebForm {
   private ContactView entity = null;

   private LOV lovPaymentTerm;
   private FormTab tabs;
   private ContactAddressView address;
   private String stSelectedAddress;
   private String userIndex;
   private String addressid;

   public LOV getLovAddresses() {
      return entity.getAddresses();
   }

   public void createNew() {
      entity = new ContactView();
      entity.markNew();
      entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
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
	   
		  
	   final ContactAddressView adr = new ContactAddressView();
	     
	   final Integer id = NumberUtil.getInteger(stSelectedAddress);
	   String id2 = id.toString();
	   int id3 = Integer.parseInt(stSelectedAddress);
	    
	   entity.getAddresses().delete(id3);
	   
	   
	   System.out.println("Id= "+ id);
	   
	   //
	   //int n = Integer.parseInt(userIndex);
	   //user.getUserroles().delete(n);
	     
	  
   }

   public void edit() throws Exception {
      view();
      setReadOnly(false);

      entity.markUpdate();

      final DTOList addresses = entity.getAddresses();

      for (int i = 0; i < addresses.size(); i++) {
         ContactAddressView ead = (ContactAddressView) addresses.get(i);

         ead.markUpdate();
      }
   }

   public void view() throws Exception {
      final String entity_id = (String)getAttribute("ent_id");
      entity=getRemoteEntityManager().loadEntity(entity_id);

      if (entity==null) throw new RuntimeException("Entity not found !");

      setReadOnly(true);
   }

   public void afterUpdateForm() {
      if (entity!=null) {
         final Integer idx = NumberUtil.getInteger(stSelectedAddress);
         address = idx==null?null:(ContactAddressView) entity.getAddresses().get(idx.intValue());

         final DTOList addresses = entity.getAddresses();

         entity.setStAddress(null);

         for (int i = 0; i < addresses.size(); i++) {
            ContactAddressView adr = (ContactAddressView) addresses.get(i);

            if (entity.getStAddress()==null)
               entity.setStAddress(adr.getStAddress());

            if (adr.isPrimary()) {
               entity.setStAddress(adr.getStAddress());
               break;
            }
         }
      }
   }

   public String getStSelectedAddress() {
      return stSelectedAddress;
   }

   public void setStSelectedAddress(String stSelectedAddress) {
      this.stSelectedAddress = stSelectedAddress;
   }

   public ContactAddressView getAddress() {
      return address;
   }

   public void setAddress(ContactAddressView address) {
      this.address = address;
   }



   public void doNewAddress() {
      final ContactAddressView adr = new ContactAddressView();
      adr.markNew();

      entity.getAddresses().add(adr);

      stSelectedAddress = String.valueOf(entity.getAddresses().size()-1);

      address = adr;
   }

   public void selectAddress() {
      // do nothing
   }

   public FormTab getTabs() {
      if (tabs==null) {
         tabs = new FormTab();

         tabs.add(new FormTab.TabBean("TAB1","ADDRESS",true));
         //tabs.add(new FormTab.TabBean("TAB2","RELATIONS",true));

         tabs.setActiveTab("TAB1");
         
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

   public ContactMasterForm() {
   }

   public ContactView getEntity() {
      return entity;
   }

   public void setEntity(ContactView entity) {
      this.entity = entity;
   }

   private ContactManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ContactManagerHome) JNDIUtil.getInstance().lookup("ContactManagerEJB",ContactManagerHome.class.getName()))
            .create();
   }

   public void doSave() throws Exception {
      final ContactView cloned = (ContactView)ObjectCloner.deepCopy(entity);
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

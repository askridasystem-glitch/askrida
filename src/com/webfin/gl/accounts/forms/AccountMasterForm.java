/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.accounts.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.entity.model.EntityView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;
import com.webfin.gl.model.AccountView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class AccountMasterForm extends WebForm {
   private EntityView entity = null;
   
   private AccountView account;

   private LOV lovPaymentTerm;
   private FormTab tabs;
   private EntityAddressView address;
   private String stSelectedAddress;
   
   public AccountView getAccount() {
      return account;
   }
   
   public LOV getLovAddresses() {
      return entity.getAddresses();
   }

   public void createNew() {
      entity = new EntityView();
      entity.markNew();
      entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
   }

   public void onChangeBranch() {
   }

   public void onClassChange() {

   }

   public void doDeleteAddress() {
      
   }

   public void edit() throws Exception {
      view();
      setReadOnly(false);

      entity.markUpdate();

      final DTOList addresses = entity.getAddresses();

      for (int i = 0; i < addresses.size(); i++) {
         EntityAddressView ead = (EntityAddressView) addresses.get(i);

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
         address = idx==null?null:(EntityAddressView) entity.getAddresses().get(idx.intValue());

         final DTOList addresses = entity.getAddresses();

         entity.setStAddress(null);

         for (int i = 0; i < addresses.size(); i++) {
            EntityAddressView adr = (EntityAddressView) addresses.get(i);

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

   public EntityAddressView getAddress() {
      return address;
   }

   public void setAddress(EntityAddressView address) {
      this.address = address;
   }



   public void doNewAddress() {
      final EntityAddressView adr = new EntityAddressView();
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

         tabs.add(new FormTab.TabBean("TAB1","{L-ENGADDRESS-L}{L-INAALAMAT-L}",true));
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

   public AccountMasterForm() {
   }

   public EntityView getEntity() {
      return entity;
   }

   public void setEntity(EntityView entity) {
      this.entity = entity;
   }

   private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB",EntityManagerHome.class.getName()))
            .create();
   }

   public void doSave() throws Exception {
      final EntityView cloned = (EntityView)ObjectCloner.deepCopy(entity);
      
      if(cloned.getStTaxFile()!=null){
      	 CheckNPWP(cloned.getStTaxFile());
      }
      
      getRemoteEntityManager().save(cloned);
      super.close();
   }

   public void doClose() {
      super.close();
   }
   
   public boolean CheckNPWP(String NPWP){
   		/* format NPWP : XX.XXX.XXX.X-XXX.XXX
   		 *				 11.111.111.1-111.111
   		 *				 01234567890123456789  */
   		 
   		if(NPWP.length()!=20){
   			throw new RuntimeException("Format No NPWP Salah,Harus 20 Digit!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
   		}
   		
   		String npwpAll[] = NPWP.split("[\\.]");
   		boolean cek = true;
   		
   		if(!NPWP.substring(2,3).equalsIgnoreCase(".")||!NPWP.substring(6,7).equalsIgnoreCase(".")||
   		   !NPWP.substring(10,11).equalsIgnoreCase(".")||!NPWP.substring(16,17).equalsIgnoreCase(".")){
   				throw new RuntimeException("Format No NPWP Salah, Harus Ada Titik!<br>Format : YY.YYY.YYY.Y-YYY.YYY ");	
   		}
   		  		
   		if(npwpAll[0].length()!=2){
   			cek = false;
   			throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");	
   		} 
   		
   		if(npwpAll[1].length()!=3){
   			cek = false;
   			throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
	
   		} 
		if(npwpAll[2].length()!=3){
			cek = false;
			throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
		} 
		if(npwpAll[3].length()!=5){
			cek = false;
			throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
	
		} 
		if(npwpAll[4].length()!=3){
			cek = false;
			throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
		} 
   		
   		return cek;
   }
   
   public String generateGLCode()throws Exception{
   	  final SQLUtil S = new SQLUtil();
      String tod2 = null;
      try {
         final PreparedStatement PS = S.setQuery("select gl_code"+
												" from ent_master where ref1 = ? "+
												" order by gl_code;");

		 PS.setString(1,entity.getStRef1());
		 
         final ResultSet RS = PS.executeQuery();
         
         if(RS.next()){
         	RS.last();
		 
			 String glcode = RS.getString("gl_code");
			 
			 //01001
	
	         glcode = glcode.substring(2);
	         String tod = null;
	         //glcode = glcode + 1;
	         int glcode2 = Integer.parseInt(glcode);
	         glcode2 = glcode2 + 1;
	         String tes = String.valueOf(glcode2);
	         if(tes.length()==1) tod = "00"+ tes;
	         else if(tes.length()==2) tod = "0"+tes;
	         else if(tes.length()==3) tod = tes;
	         
	         
	         String cumi = entity.getStRef1();
	         int cumi2 = Integer.parseInt(cumi);
	         String cumi3 = String.valueOf(cumi2);
	         if(cumi3.length()==1) tod2 = "0"+cumi+tod;
	         else if(cumi3.length()==2) tod2 = cumi+tod;
         }else{
         	 String cumi = entity.getStRef1();
	         int cumi2 = Integer.parseInt(cumi);
	         String cumi3 = String.valueOf(cumi2);
	         if(cumi3.length()==1) tod2 = "0"+cumi+"001";
	         else if(cumi3.length()==2) tod2 = cumi+"001";
         }
		 
         return tod2;

      } finally {
         S.release();
      }
   }
   
   public void makeGLCOde() throws Exception{
   		entity.setStGLCode(generateGLCode());
   }
}

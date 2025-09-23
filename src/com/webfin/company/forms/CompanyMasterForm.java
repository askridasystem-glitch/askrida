/***********************************************************************
 * Module:  com.webfin.Company.forms.CompanyMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.company.forms;

import com.crux.util.stringutil.StringUtil;
import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.company.ejb.CompanyManager;
import com.webfin.company.ejb.CompanyManagerHome;
import com.webfin.company.model.*;
import com.webfin.company.ejb.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class CompanyMasterForm extends WebForm {
   private CompanyView company = null;

   private LOV lovPaymentTerm;

   public void createNew() {
      company = new CompanyView();
      company.markNew();
      company.setStVSGroup("COMP_TYPE_GROUP");
      
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

      company.markUpdate();

   }

   public void view() throws Exception {
      final String company_id = (String)getAttribute("vs_code");
      company = getRemoteCompanyManager().loadCompany(company_id);

      if (company==null) throw new RuntimeException("company not found !");

      setReadOnly(true);
   }

   public void afterUpdateForm() {
     
   }

   public CompanyMasterForm() {
   }

   public CompanyView getCompany() {
      return company;
   }

   public void setCompany(CompanyView Company) {
      this.company = company;
   }

   private CompanyManager getRemoteCompanyManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((CompanyManagerHome) JNDIUtil.getInstance().lookup("CompanyManagerEJB",CompanyManagerHome.class.getName()))
            .create();
   }

   public void doSave() throws Exception {
      final CompanyView cloned = (CompanyView)ObjectCloner.deepCopy(company);
      
      getRemoteCompanyManager().save(cloned);
      super.close();
   }

   public void doClose() {
      super.close();
   }
   
}

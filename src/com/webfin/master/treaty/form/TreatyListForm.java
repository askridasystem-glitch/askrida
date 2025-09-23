/***********************************************************************
 * Module:  com.webfin.master.treaty.form.TreatyListForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.master.treaty.form;

import com.crux.web.form.Form;
import com.crux.web.form.FormManager;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.webfin.insurance.model.InsuranceTreatyTypesView;
import com.webfin.insurance.model.InsuranceTreatyView;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.crux.web.controller.SessionManager;
import com.crux.common.controller.UserSessionMgr;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class TreatyListForm extends Form {

   private DTOList list;
   private String treatyid;
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");

   private boolean enableApproval1 = SessionManager.getInstance().getSession().hasResource("RI_TREATY_APPROVE1");
   private boolean enableApproval2 = SessionManager.getInstance().getSession().hasResource("RI_TREATY_APPROVE2");
   private boolean enableApproval3 = SessionManager.getInstance().getSession().hasResource("RI_TREATY_APPROVE3");
   private boolean enableApproval4 = SessionManager.getInstance().getSession().hasResource("RI_TREATY_APPROVE4");


   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public String getTreatyid() {
      return treatyid;
   }

   public void setTreatyid(String treatyid) {
      this.treatyid = treatyid;
   }

   public DTOList getList() throws Exception {

      if (list==null) {
         list=new DTOList();
         list.getFilter().activate();
         list.getFilter().orderKey="ins_treaty_id";
      }

      SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");

      sqa.addQuery(
              "from ins_treaty"
      );

      sqa.addFilter(list.getFilter());

      list = sqa.getList(InsuranceTreatyView.class);

      return list;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.createNew();

      x.show();

   }

   public void clickEdit() throws Exception {

      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.edit(treatyid);

      x.show();
   }

   public void clickView() throws Exception {
      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.view(treatyid);

      x.show();

   }

   public void list() {

   }
   
   public boolean isEnableSuperEdit() {
      return enableSuperEdit;
   }

   public void clickApprove1() throws Exception {

      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.approve1(treatyid);

      x.show();
   }

   public void clickApprove2() throws Exception {

      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.approve2(treatyid);

      x.show();
   }

   public void clickApprove3() throws Exception {

      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.approve3(treatyid);

      x.show();
   }

   public void clickApprove4() throws Exception {

      TreatyMasterForm x = (TreatyMasterForm) FormManager.getInstance().newForm("treaty_form",this);

      x.approve4(treatyid);

      x.show();
   }

    /**
     * @return the enableApproval1
     */
    public boolean isEnableApproval1() {
        return enableApproval1;
    }

    /**
     * @param enableApproval1 the enableApproval1 to set
     */
    public void setEnableApproval1(boolean enableApproval1) {
        this.enableApproval1 = enableApproval1;
    }

    /**
     * @return the enableApproval2
     */
    public boolean isEnableApproval2() {
        return enableApproval2;
    }

    /**
     * @param enableApproval2 the enableApproval2 to set
     */
    public void setEnableApproval2(boolean enableApproval2) {
        this.enableApproval2 = enableApproval2;
    }

    /**
     * @return the enableApproval3
     */
    public boolean isEnableApproval3() {
        return enableApproval3;
    }

    /**
     * @param enableApproval3 the enableApproval3 to set
     */
    public void setEnableApproval3(boolean enableApproval3) {
        this.enableApproval3 = enableApproval3;
    }

    /**
     * @return the enableApproval4
     */
    public boolean isEnableApproval4() {
        return enableApproval4;
    }

    /**
     * @param enableApproval4 the enableApproval4 to set
     */
    public void setEnableApproval4(boolean enableApproval4) {
        this.enableApproval4 = enableApproval4;
    }

}
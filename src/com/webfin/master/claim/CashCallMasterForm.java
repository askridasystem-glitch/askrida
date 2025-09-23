/***********************************************************************
 * Module:  com.webfin.master.treaty.form.CashCallMasterForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.master.claim;

import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.common.controller.FormTab;
import com.webfin.insurance.model.InsuranceTreatyView;
import com.webfin.insurance.model.InsuranceTreatyDetailView;
import com.webfin.insurance.model.InsuranceTreatySharesView;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.WebFinLOVRegistry;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsuranceCashCallDetailView;
import com.webfin.insurance.model.InsuranceCashCallView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;

public class CashCallMasterForm extends Form {

   private InsuranceCashCallView tre;
   private InsuranceTreatyDetailView tredet;
   private FormTab tbpoltype;
   private FormTab tbtreatytype;
   private String stPolicyTypeGroupID;
   private String stPolicyTypeID;
   private Integer detailIndex;
   private Integer memberIndex;
   private DTOList members;
   private DTOList details;
   private boolean drillMode = false;
   private boolean enableEdit = false;
   private boolean enableApprove = false;

   public void changeClass() {
      
   }

   public boolean isDrillMode() {
      return drillMode;
   }

   public void setDrillMode(boolean drillMode) {
      this.drillMode = drillMode;
   }

   public Integer getDetailIndex() {
      return detailIndex;
   }

   public void setDetailIndex(Integer detailIndex) {
      this.detailIndex = detailIndex;
   }

   public DTOList getDetails() {
      details = tre.getDetails();
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }

   public Integer getMemberIndex() {
      return memberIndex;
   }

   public void setMemberIndex(Integer memberIndex) {
      this.memberIndex = memberIndex;
   }

   public DTOList getMembers() {
      InsuranceTreatyDetailView activeTreatyDetail = getActiveTreatyDetail();

      if (activeTreatyDetail==null) {
         InsuranceTreatyDetailView trd = new InsuranceTreatyDetailView();

         trd.markNew();

         trd.setStTreatyTypeID(tbtreatytype.activeTab.tabID);

         tre.getDetails().add(trd);

         activeTreatyDetail = getActiveTreatyDetail();
      }

      members = activeTreatyDetail.getShares();

      return members;
   }

   public InsuranceTreatyDetailView getActiveTreatyDetail() {
      return (InsuranceTreatyDetailView) getDetails().get(detailIndex.intValue());
   }

   public void setMembers(DTOList members) {
      this.members = members;
   }

   public String getStPolicyTypeGroupID() {
      return stPolicyTypeGroupID;
   }

   public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
      this.stPolicyTypeGroupID = stPolicyTypeGroupID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public FormTab getTbtreatytype() throws Exception {
      if (tbtreatytype==null){
         tbtreatytype = new FormTab(WebFinLOVRegistry.getInstance().LOV_TreatyType());
      }
      return tbtreatytype;
   }

   public void setTbtreatytype(FormTab tbtreatytype) {
      this.tbtreatytype = tbtreatytype;
   }

   public FormTab getTbpoltype() throws Exception {
      if (tbpoltype==null) {
         tbpoltype = new FormTab(WebFinLOVRegistry.getInstance().LOV_PolicyType(null));
      }
      return tbpoltype;
   }

   public void setTbpoltype(FormTab tbpoltype) {
      this.tbpoltype = tbpoltype;
   }

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public void onChangePolicyType() {

      tre.getDetails(stPolicyTypeID);

      if(tre.isUpdate()) {

         tre.getDetails().markAllUpdate();

         DTOList details = tre.getDetails();

         for (int i = 0; i < details.size(); i++) {
            InsuranceCashCallDetailView d = (InsuranceCashCallDetailView) details.get(i);

            d.getShares().markAllUpdate();
         }

      } else {



      }
   }

   public void drillToggle() {
      drillMode = !drillMode;
   }

   public void onChangePolicyTypeGroup() {

   }

   public InsuranceCashCallView getTre() {
      return tre;
   }
   
   public InsuranceTreatyDetailView getTredet() {
      return tredet;
   }
   
   public void setTredet(InsuranceTreatyDetailView tredet) {
      this.tredet = tredet;
   }

   public void setTre(InsuranceCashCallView tre) {
      this.tre = tre;
   }

   public void createNew() {
      tre= new InsuranceCashCallView();

      tre.markNew();

      tre.setDetails(new DTOList());

      setTitle("CREATE NEW CASH CALL");
      
      enableEdit = true;

   }

   public void edit(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      tre.markUpdate();

      setTitle("EDIT CASH CALL");
      
      enableEdit = true;
   }

   public void view(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      //setReadOnly(true);

      setTitle("VIEW CASH CALL");
      
      enableEdit = false;
   }

   public void save() throws Exception {

      getRemoteInsurance().save(tre);

      close();
   }

   public void close() {
      super.close();
   }

   public void newMember() {

      InsuranceTreatySharesView sh = new InsuranceTreatySharesView();

      sh.markNew();

      members.add(sh);
   }

   public void delMember() {
      members.delete(memberIndex.intValue());
   }

   public LOV getLovDetailParent() {
      return getDetails();
   }

   public void newDetail() {
      InsuranceCashCallDetailView d = new InsuranceCashCallDetailView();

      d.markNew();

      d.setStPolicyTypeID(getStPolicyTypeID());

      getDetails().add(d);
   }

   public void delDetail() {

      getDetails().delete(detailIndex.intValue());
 
   }

   public boolean isOR(Integer index) {

      InsuranceTreatyDetailView d = (InsuranceTreatyDetailView) getDetails().get(index.intValue());

      return d.isOR();
   }
   
   public boolean isBPDAN(Integer index) {

      InsuranceTreatyDetailView d = (InsuranceTreatyDetailView) getDetails().get(index.intValue());

      return d.isBPDAN();
      
   }
   
   public boolean isEnableEdit() {   	
   	  return enableEdit;
   }

   public void approve1(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      tre.markUpdate();

      if(tre.getStApprovedFlag1()!=null)
          throw new RuntimeException("Sudah disetujui oleh operator");

      tre.setStApprovedFlag1("Y");
      tre.setStApprovedWho1(UserManager.getInstance().getUser().getStUserID());
      tre.setDtApprovedDate1(new Date());

      setTitle("APPROVE TREATY (OPERATOR)");

      enableApprove = true;
   }

   public void approve2(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      tre.markUpdate();

      if(tre.getStApprovedFlag1()==null)
          throw new RuntimeException("Belum disetujui oleh operator");

      if(tre.getStApprovedFlag2()!=null)
          throw new RuntimeException("Sudah disetujui oleh kepala bagian");

      tre.setStApprovedFlag2("Y");
      tre.setStApprovedWho2(UserManager.getInstance().getUser().getStUserID());
      tre.setDtApprovedDate2(new Date());

      setTitle("APPROVE TREATY (KEPALA BAGIAN)");

      enableApprove = true;
   }

   public void approve3(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      tre.markUpdate();

      if(tre.getStApprovedFlag2()==null)
          throw new RuntimeException("Belum disetujui oleh kepala bagian");

      if(tre.getStApprovedFlag3()!=null)
          throw new RuntimeException("Sudah disetujui oleh kepala divisi");

      tre.setStApprovedFlag3("Y");
      tre.setStApprovedWho3(UserManager.getInstance().getUser().getStUserID());
      tre.setDtApprovedDate3(new Date());

      setTitle("APPROVE TREATY (KEPALA DIVISI)");

      enableApprove = true;
   }

   public void approve4(String treatyid) {
      tre = (InsuranceCashCallView) DTOPool.getInstance().getDTO(InsuranceCashCallView.class, treatyid);

      tre.markUpdate();

      if(tre.getStApprovedFlag3()==null)
          throw new RuntimeException("Belum disetujui oleh kepala divisi");

      if(tre.getStApprovedFlag4()!=null)
          throw new RuntimeException("Sudah disetujui oleh TI");

      tre.setStApprovedFlag4("Y");
      tre.setStApprovedWho4(UserManager.getInstance().getUser().getStUserID());
      tre.setDtApprovedDate4(new Date());
      tre.setStActiveFlag("Y");

      setTitle("AKTIVASI TREATY (TI)");

      enableApprove = true;
   }

    /**
     * @return the enableApprove
     */
    public boolean isEnableApprove() {
        return enableApprove;
    }

    /**
     * @param enableApprove the enableApprove to set
     */
    public void setEnableApprove(boolean enableApprove) {
        this.enableApprove = enableApprove;
    }

}

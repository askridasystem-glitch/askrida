/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:53 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.common.controller.FormTab;
import com.crux.common.controller.UserSessionMgr;
import com.crux.common.parameter.Parameter;
import com.crux.common.config.Config;
import com.crux.util.*;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.lov.LOVManager;
import com.crux.pool.DTOPool;
import com.crux.ff.model.FlexTableView;
import com.webfin.FinCodec;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.*;
import com.webfin.WebFinLOVRegistry;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class PolicyParentForm extends Form {

   private final static transient LogManager logger = LogManager.getInstance(PolicyForm.class);

   private InsurancePolicyParentView policy;
   private FormTab tabs;
   private FormTab rdtabs;
   private FormTab ritabs;
   private FormTab claimtabs;
   private FormTab coverritabs;
   private String stSelectedObject;
   private String stClaimObject;
   private String detailindex;
   private String coinsIndex;
   private String insItemID;
   private InsurancePolicyObjectView selectedObject;
   private DTOList objectSumInsureds;
   private DTOList objectCoverage;
   public String sumInsuredAddID;
   private DTOList sumInsuredLOV;
   private String tsiIndex;
   private String claimItemindex;
   private String deductIndex;
   private String objDeductIndex;
   private String coverageAddID;
   private Integer covIndex;
   private Integer covReinsIndex;
   private Integer treatiesIndex;
   private Integer treatyDetailIndex;
   private DTOList coverageLOV;
   private DTOList insItemLOV;

   private Integer idxTreaty;
   private Integer idxTreatyDetail;
   private Integer idxTreatyShares;
   private Integer idxTreatySharesCover;

   private boolean reasMode;
   private boolean approvalMode;
   private boolean allowDuplicateTSI;
   private boolean overKreasiLimit;

   
   
   public String getStClaimStatus() {
      if (policy==null) return null;
      return policy.getStClaimStatus();
   }

   public void setStClaimObject(String stClaimObject) {
      this.stClaimObject = stClaimObject;
   }

   public InsurancePolicyObjectView getClaimObject() {
      return policy.getClaimObject();
   }

   public String getClaimItemindex() {
      return claimItemindex;
   }

   public void setClaimItemindex(String claimItemindex) {
      this.claimItemindex = claimItemindex;
   }

   public DTOList getClaimItems() {
      return policy.getClaimItems();
   }

   public boolean isReasMode() {
      return reasMode;
   }

   public void setReasMode(boolean reasMode) {
      this.reasMode = reasMode;
   }

   public FormTab getClaimritabs() {
      selectedObject = policy.getClaimObject();

      return getRitabs();
   }
   
   public FormTab getClaimtabs() {
      return claimtabs;
   }

   public FormTab getRitabs() {

      String cid=null;

      if (ritabs!=null && ritabs.activeTab!=null)
         cid=ritabs.activeTab.tabID;

      final DTOList tdtls = selectedObject.getTreatyDetails();

      if (ritabs!=null)
         if (tdtls.size()!=ritabs.tabs.size()) {
            ritabs=null;
         }

      if (ritabs==null) {
         ritabs = new FormTab();


         for (int i = 0; i < tdtls.size(); i++) {
            InsurancePolicyTreatyDetailView tdt = (InsurancePolicyTreatyDetailView) tdtls.get(i);

            final String tabid = tdt.getStInsuranceTreatyDetailID();

            ritabs.add(new FormTab.TabBean(tabid,tdt.getStTreatyClassDesc(), true));

            if (Tools.isEqual(tabid, cid)) ritabs.setActiveTab(cid);

            if (ritabs.activeTab==null) ritabs.setActiveTab(tabid);
         }
      }
      return ritabs;
   }

   public void setRitabs(FormTab ritabs) {
      this.ritabs = ritabs;
   }
   
   /*
   public FormTab getCoverRitabs() {

      String cid=null;

      if (coverritabs!=null && coverritabs.activeTab!=null)
         cid=coverritabs.activeTab.tabID;

      final DTOList tdtls = selectedObject.getCoverage();

      if (coverritabs!=null)
         if (tdtls.size()!=coverritabs.tabs.size()) {
            coverritabs=null;
         }

      if (coverritabs==null) {
         coverritabs = new FormTab();


         for (int i = 0; i < tdtls.size(); i++) {
            InsurancePolicyCoverReinsView tdt = (InsurancePolicyCoverReinsView) tdtls.get(i);

            final String tabid = tdt.getStInsuranceCoverID();

            coverritabs.add(new FormTab.TabBean(tabid,tdt.getStTreatyClassDesc(), true));

            if (Tools.isEqual(tabid, cid)) coverritabs.setActiveTab(cid);

            if (coverritabs.activeTab==null) coverritabs.setActiveTab(tabid);
         }
      }
      return coverritabs;
   }

   public void setCoverRitabs(FormTab coverritabs) {
      this.coverritabs = coverritabs;
   }*/

   public Integer getIdxTreaty() {
      return idxTreaty;
   }

   public void setIdxTreaty(Integer idxTreaty) {
      this.idxTreaty = idxTreaty;
   }

   public Integer getIdxTreatyDetail() {
      return idxTreatyDetail;
   }

   public void setIdxTreatyDetail(Integer idxTreatyDetail) {
      this.idxTreatyDetail = idxTreatyDetail;
   }

   public Integer getIdxTreatyShares() {
      return idxTreatyShares;
   }

   public void setIdxTreatyShares(Integer idxTreatyShares) {
      this.idxTreatyShares = idxTreatyShares;
   }
   
   public Integer getIdxTreatySharesCover() {
      return idxTreatySharesCover;
   }

   public void setIdxTreatySharesCover(Integer idxTreatySharesCover) {
      this.idxTreatySharesCover = idxTreatySharesCover;
   }

   public void changeBranch() {
   }

   public LOV getLovTreaty() throws Exception {
      /*return ListUtil.getLookUpFromQuery(
              "select ins_treaty_id,treaty_name from ins_treaty order by ins_treaty_id"
      );*/

      final HashMap p = new HashMap();

      p.put("per_start",policy.getDtPeriodStart());
      p.put("pend",policy.getDtPeriodEnd());

      return LOVManager.getInstance().getLOV("LOV_Treaty",p);
   }

   public LOV getLovTreatyClass() throws Exception {

      final String treatyID = (String)getAttribute("LOV_PARAM_TREATY_ID");

      return ListUtil.getLookUpFromQuery(
              "   select " +
              "      a.ins_treaty_detail_id,b.treaty_type_name " +
              "   from " +
              "      ins_treaty_detail a" +
              "         inner join ins_treaty_types b on b.ins_treaty_type_id = a.treaty_type" +
              "   where" +
              "      a.ins_treaty_id=?" +
              "   order by " +
              "      b.treaty_type_name",
              new Object [] {treatyID}
      );
   }

   public void resetTreaty() throws Exception{
      getTreaties().deleteAll();
      ritabs=null;

      policy.recalculate();
      
      
   }

   public void changeTreaty() {
      getTreaties().deleteAll();
      policy.setStInsuranceTreatyID(null);
      ritabs=null;
   }

   public void raiseTreatyLevel() {
      final InsurancePolicyTreatyView treaty = (InsurancePolicyTreatyView)getTreaties().get(0);

      //treaty.raise(policy.getStPolicyTypeID(), getDbInsuredAmount());
   }

   public void lowerTreatyLevel() {
      final InsurancePolicyTreatyView treaty = (InsurancePolicyTreatyView)getTreaties().get(0);
      treaty.lower();
   }

   public void onNewTreatyDetail() {

   }

   public void onDeleteTreatyDetail() {

   }

   public Integer getTreatyDetailIndex() {
      return treatyDetailIndex;
   }

   public void setTreatyDetailIndex(Integer treatyDetailIndex) {
      this.treatyDetailIndex = treatyDetailIndex;
   }

   public Integer getTreatiesIndex() {
      return treatiesIndex;
   }

   public void setTreatiesIndex(Integer treatiesIndex) {
      this.treatiesIndex = treatiesIndex;
   }

   public void onNewTreaty() {
      final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

      tre.markNew();

      getTreaties().add(tre);
   }

   public void onDeleteTreaty() {
      getTreaties().delete(treatiesIndex.intValue());
   }

   public String getStStatus() {
      if (policy==null) return null;
      return policy.getStNextStatus()==null?policy.getStStatus():policy.getStNextStatus();
   }

   public void setStStatus(String stStatus) {
   }

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public String getCoinsIndex() {
      return coinsIndex;
   }

   public void setCoinsIndex(String coinsIndex) {
      this.coinsIndex = coinsIndex;
   }

   public String getInsItemID() {
      return insItemID;
   }

   public void setInsItemID(String insItemID) {
      this.insItemID = insItemID;
   }

   public String getDetailindex() {
      return detailindex;
   }

   public void setDetailindex(String detailindex) {
      this.detailindex = detailindex;
   }

   public DTOList getDetails() {
      return policy.getDetails();
   }

   public String getStSelectedObject() {
      return stSelectedObject;
   }

   public void setStSelectedObject(String stSelectedObject) {
      this.stSelectedObject = stSelectedObject;
   }

   public InsurancePolicyObjectView getSelectedObject() {
      return selectedObject;
   }

   public void setSelectedObject(InsurancePolicyObjectView selectedObject) {
      this.selectedObject = selectedObject;
   }

   public InsurancePolicyParentView getPolicy() {
      return policy;
   }

   public void setPolicy(InsurancePolicyParentView policy) {
      this.policy = policy;
   }

   public void createNew() throws Exception {
      policy = new InsurancePolicyParentView();
		
      policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());

      policy.markNew();

      policy.setStStatus(FinCodec.PolicyStatus.PARENT);
      policy.setStActiveFlag("Y");
      //policy.setStEffectiveFlag("Y");

      policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      policy.setDbCurrencyRate(BDUtil.one);
      policy.setDbPeriodRate(new BigDecimal(100));

      policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));

      policy.markNew();
      
   }


   public void afterUpdateForm() {
      //switchSelectedObject();
      //policy.recalculate();
      //policy.recalculateTreaty();
      //if (policy.isModified())
       //  policy.recalculateTreaty();
         
      //if(policy.isModified()&&policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE))
      	 //policy.recalculateTreatyEndorse();
   }

   public LookUpUtil getLOV_KreasiRe() throws Exception {
      return ListUtil.getLookUpFromQuery(
              "   select " +
              "      ent_id,ent_name " +
              "   from " +
              "      ent_master\n" +
              "   where " +
              "      ent_id in (\n" +
              "         select distinct refid1 from ins_rates_big where rate_class='PAKREASI'\n" +
              "      )" +
              "   order by ent_name"
      );
   }


   public DTOList getSuminsureds() {
      return policy.getSuminsureds();
   }

   public DTOList getCoins() throws Exception {
      return policy.getCoins2();
   }

   public DTOList getCovers() {
      return policy.getCovers();
   }
   
   public DTOList getCoveragereins() {
      return policy.getCoveragereins();
   }

   public DTOList getClausules() {
      return policy.getClausules();
   }

   public DTOList getDeductibles() {
      return policy.getDeductibles();
   }

   public void superEdit(String policyID) throws Exception {
      view(policyID);

      super.setReadOnly(false);

      //policy.setObjects(null);

      policy.markUpdateO();

      //if (true)
      //   throw new RuntimeException("success");
		
	  /*
      final DTOList objects = policy.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

         obj.getSuminsureds().markAllUpdate();
         obj.getDeductibles().markAllUpdate();
         obj.getCoverage().markAllUpdate();
  
         //obj.getCoverageReinsurance().markAllUpdate();

         obj.markUpdate();
         
         obj.getTreaties().markAllUpdate();

         final DTOList treatyDetails = obj.getTreatyDetails();
         treatyDetails.markAllUpdate();

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
            
            final DTOList share = trdi.getShares();
            trdi.getShares().markAllUpdate();
            
	            for (int k = 0; k < share.size(); k++) {
	            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);
	            
	            ri.getCoverage().markAllUpdate();
	         }
         }
      }

      policy.getCoins().markAllUpdate();
      policy.getDetails().markAllUpdate();
      policy.getClaimItems().markAllUpdate();
      policy.getDeductibles().markAllUpdate();
      

      initTabs();

      if (policy.getStInsuranceTreatyID()==null)
         defineTreaty();

      if (policy.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
   	  */
   }
   
  

   public FormTab getRdtabs() {
      return rdtabs;
   }

   public void onFormCreate() {
      initTabs();
   }
/*
   private void initTabs() {

      tabs = new FormTab();

      tabs.add(new FormTab.TabBean("TAB_ENDORSE","ENDORSEMENT",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM","CLAIM",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_ITEM","CLAIM ITEMS",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_RE","CLAIM RE",false));
      tabs.add(new FormTab.TabBean("TAB_RISK_DET","RISK DETAIL",true));
      tabs.add(new FormTab.TabBean("TAB_CLAUSES","CLAUSES",true));
      tabs.add(new FormTab.TabBean("TAB_DEDUCTIBLES","DEDUCTIBLES",false));
      tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",true));
      tabs.add(new FormTab.TabBean("TAB_INST","PAYMENT",true));
      tabs.add(new FormTab.TabBean("TAB_COINS","COINSURANCE",true));
      tabs.add(new FormTab.TabBean("TAB_CLAIMCO","CLAIM CO",true));
      tabs.add(new FormTab.TabBean("TAB_OPTIONS","OPTIONS",true));
      //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));

      tabs.setActiveTab("TAB_RISK_DET");

      boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
      boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
      boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
      boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
      boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
      boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());

      if (statusEndorse) {
         tabs.enable("TAB_ENDORSE",true);
         tabs.setActiveTab("TAB_ENDORSE");

      }

      if (statusClaim) {
         tabs.enable("TAB_CLAIM",true);
         tabs.enable("TAB_CLAIM_ITEM",true);
         tabs.enable("TAB_CLAIM_RE",true);
         tabs.setActiveTab("TAB_CLAIM");
      }

      rdtabs = new FormTab();

      rdtabs.add(new FormTab.TabBean("TAB_DETAIL","DETAILS",true));
      rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
      rdtabs.add(new FormTab.TabBean("TAB_RDCLM","CLAIM",statusClaim));
      rdtabs.add(new FormTab.TabBean("TAB_SI","SUM INSURED",true));
      rdtabs.add(new FormTab.TabBean("TAB_DED","DEDUCTIBLES",true));
      rdtabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));

      rdtabs.setActiveTab("TAB_DETAIL");

      if (!(reasMode || statusClaim)) {
         rdtabs.enable("TAB_REINS",false);
      }

      if (Config.isDevelopmentMode()) {
         rdtabs.enable("TAB_REINS",true);
      }

      if (statusDraft) setTitle("PROPOSAL");
      else if (statusPolicy) setTitle("POLICY");
      else if (statusEndorse) setTitle("ENDORSEMENT");
      else if (statusClaim) setTitle("CLAIM");
      else if (statusCancel) setTitle("CANCEL");
      else if (statusSPPA) setTitle("SPPA");
   }
*/
/*
private void initTabs() {

      tabs = new FormTab();

      tabs.add(new FormTab.TabBean("TAB_ENDORSE","ENDORSEMENT",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_ITEM","{L-ENGCLAIM ITEMS-L}{L-INAITEM KLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_RE","{L-ENGCLAIM R/I-L}{L-INAR/I KLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_RISK_DET","{L-ENGRISK DETAIL-L}{L-INADETIL RESIKO-L}",true));
      tabs.add(new FormTab.TabBean("TAB_CLAUSES","{L-ENGCLAUSES-L}{L-INAKLAUSULA-L}",false));
      tabs.add(new FormTab.TabBean("TAB_DEDUCTIBLES","DEDUCTIBLES",false));
      tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",false));
      tabs.add(new FormTab.TabBean("TAB_INST","{L-ENGPAYMENT-L}{L-INAPEMBAYARAN-L}",false));
      tabs.add(new FormTab.TabBean("TAB_COINS","{L-ENGCOINSURANCE-L}{L-INACOASURANSI-L}",true));
      tabs.add(new FormTab.TabBean("TAB_CLAIMCO","{L-ENGCLAIM CO-L}{L-INAKLAIM CO-L}",false));
      tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));
      //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));

      tabs.setActiveTab("TAB_RISK_DET");

      boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
      boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
      boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
      boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
      boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
      boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());
	  boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStStatus());

	 // boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
	 // boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());

	  if(!statusDraft && !statusSPPA){
	  	 tabs.enable("TAB_CLAUSES",true);
	  	 tabs.enable("TAB_PREMI",true);
	  	 tabs.enable("TAB_INST",true);
	  	 tabs.enable("TAB_CLAIMCO",true);
	  	 tabs.enable("TAB_OPTIONS",true);
	  }
	  

      if (statusEndorse) {
         tabs.enable("TAB_ENDORSE",true);
         tabs.setActiveTab("TAB_ENDORSE");

      }

      if (statusClaim) {
         tabs.enable("TAB_CLAIM",true);
         tabs.enable("TAB_CLAIM_ITEM",true);
         tabs.enable("TAB_CLAIM_RE",true);
         tabs.setActiveTab("TAB_CLAIM");
      }

      rdtabs = new FormTab();

      rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
      rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
      rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
      rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
      rdtabs.add(new FormTab.TabBean("TAB_DED","DEDUCTIBLES",true));
      rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));
  
      rdtabs.setActiveTab("TAB_DETAIL");

      if (!(reasMode || statusClaim)) {
         rdtabs.enable("TAB_REINS",false);
      }

      if (Config.isDevelopmentMode()) {
         rdtabs.enable("TAB_REINS",true);
      }

      if (statusDraft) setTitle("{L-ENGPROPOSAL-L}{L-INAPROPOSAL-L}");
      else if (statusPolicy) setTitle("{L-ENGPOLICY-L}{L-INAPOLIS-L}");
      else if (statusEndorse) setTitle("ENDORSEMENT");
      else if (statusClaim) setTitle("{L-ENGCLAIM-L}{L-INAKLAIM-L}");
      else if (statusCancel) setTitle("CANCEL");
      else if (statusSPPA) setTitle("SPPA");
      else if (statusRenewal) setTitle("RENEWAL");
      
//      claimtabs = new FormTab();
      
//      claimtabs.add(new FormTab.TabBean("TAB_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INADOKUMEN-L}",statusClaim));
 //     claimtabs.add(new FormTab.TabBean("TAB_PLA","{L-ENGPLA-L}{L-INAPLA-L}",statusClaim));
//      claimtabs.add(new FormTab.TabBean("TAB_DLA","{L-ENGDLA-L}{L-INADLA-L}",false));
//	  claimtabs.add(new FormTab.TabBean("TAB_APPROVED","{L-ENGAPPROVED CLAIM-L}{L-INAKLAIM DISETUJUI-L}",false));
  
//  	  claimtabs.setActiveTab("TAB_DOCUMENTS");
  	  
 // 	  if(statusDLA){
 // 	 		claimtabs.enable("TAB_DLA",true);
 // 	 		claimtabs.enable("TAB_APPROVED",true);
  //	  }
   }
 */  
 
 
   private void initTabs() {

      tabs = new FormTab();

      tabs.add(new FormTab.TabBean("TAB_ENDORSE","ENDORSEMENT",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_ITEM","{L-ENGCLAIM ITEMS-L}{L-INAITEM KLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_CLAIM_RE","{L-ENGCLAIM R/I-L}{L-INAR/I KLAIM-L}",false));
      tabs.add(new FormTab.TabBean("TAB_RISK_DET","{L-ENGRISK DETAIL-L}{L-INADETIL RESIKO-L}",true));
      tabs.add(new FormTab.TabBean("TAB_CLAUSES","{L-ENGCLAUSES-L}{L-INAKLAUSULA-L}",false));
      tabs.add(new FormTab.TabBean("TAB_DEDUCTIBLES","DEDUCTIBLES",false));
      tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",false));
      tabs.add(new FormTab.TabBean("TAB_INST","{L-ENGPAYMENT-L}{L-INAPEMBAYARAN-L}",false));
      tabs.add(new FormTab.TabBean("TAB_COINS","{L-ENGCOINSURANCE-L}{L-INAKOASURANSI-L}",true));
      tabs.add(new FormTab.TabBean("TAB_CLAIMCO","{L-ENGCLAIM CO-L}{L-INAKLAIM CO-L}",false));
      tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));
      //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));

      tabs.setActiveTab("TAB_RISK_DET");

      boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
      boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
      boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
      boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
      boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
      boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());
	  boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStStatus());
	  boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStStatus());

	  boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
	  boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());

	  if(!statusDraft && !statusSPPA){
	  	 tabs.enable("TAB_CLAUSES",true);
	  	 tabs.enable("TAB_PREMI",true);
	  	 tabs.enable("TAB_INST",true);	  	 
	  	 tabs.enable("TAB_OPTIONS",true);
	  }
	  

      if (statusEndorse||statusClaimEndorse) {
         tabs.enable("TAB_ENDORSE",true);
         tabs.setActiveTab("TAB_ENDORSE");

      }

      if (statusClaim||statusClaimEndorse) {
         tabs.enable("TAB_CLAIM",true);
         tabs.enable("TAB_CLAIM_ITEM",true);
         tabs.enable("TAB_CLAIM_RE",true);
         tabs.enable("TAB_CLAIMCO",true);
         tabs.setActiveTab("TAB_CLAIM");
      }

      rdtabs = new FormTab();

      rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
      rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
      rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
      rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
      rdtabs.add(new FormTab.TabBean("TAB_DED","DEDUCTIBLES",true));
      rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));
      
      rdtabs.setActiveTab("TAB_DETAIL");

      if (!(reasMode || statusClaim)) {
         rdtabs.enable("TAB_REINS",false);
      }

      if (Config.isDevelopmentMode()) {
         rdtabs.enable("TAB_REINS",true);
      }

      if (statusDraft) setTitle("{L-ENGPROPOSAL-L}{L-INAPROPOSAL-L}");
      else if (statusPolicy) setTitle("{L-ENGPOLICY-L}{L-INAPOLIS-L}");
      else if (statusEndorse) setTitle("ENDORSEMENT");
      else if (statusClaim) setTitle("{L-ENGCLAIM-L}{L-INAKLAIM-L}");
      else if (statusCancel) setTitle("CANCEL");
      else if (statusSPPA) setTitle("SPPA");
      else if (statusRenewal) setTitle("{L-ENGRENEWAL-L}{L-INAPERPANJANGAN-L}");
      
      claimtabs = new FormTab();
      
      claimtabs.add(new FormTab.TabBean("TAB_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INADOKUMEN-L}",false));
      claimtabs.add(new FormTab.TabBean("TAB_PLA","{L-ENGPLA-L}{L-INALKS-L}",false));
      claimtabs.add(new FormTab.TabBean("TAB_DLA","{L-ENGDLA-L}{L-INALKP-L}",false));
	  //claimtabs.add(new FormTab.TabBean("TAB_APPROVED","{L-ENGAPPROVED CLAIM-L}{L-INAKLAIM DISETUJUI-L}",false));
  
  	  if(statusClaim||statusClaimEndorse){
  	  	    claimtabs.enable("TAB_DOCUMENTS",true);
  	  	    claimtabs.enable("TAB_PLA",true);
  	  }
  	  
  	  claimtabs.setActiveTab("TAB_DOCUMENTS");
  	  
  	  if(statusClaimEndorse){
  	  	tabs.setActiveTab("TAB_ENDORSE");
  	  }
  	  
  	  if(statusDLA||statusClaimEndorse){
  	 		claimtabs.enable("TAB_DLA",true);		
  	 		//claimtabs.enable("TAB_APPROVED",true);
  	  }
   }
   
   public void view(String policyID) throws Exception {

      if (policyID==null) throw new RuntimeException("Please select policy");

      policy = getRemoteInsurance().getInsurancePolicyParent(policyID);

      //if (policy==null) throw new RuntimeException("Policy not found");
	  
	  /*
      policy.loadObjects();
      policy.loadClausules();
      policy.loadEntities();
      policy.loadDetails();
      policy.loadCoins();

      policy.recalculate();
	  */
	  
      super.setReadOnly(true);

      //productionModeTabs();

      //initTabs();
   }

   public FormTab getTabs() {
      return tabs;
   }

   public void setTabs(FormTab tabs) {
      this.tabs = tabs;
   }

   public DTOList getTreaties() {
      return selectedObject.getTreaties();
   }

   public DTOList getClaimTreaties() {
      return getClaimObject().getTreaties();
   }

   public void selectObject() {
   }

   private DTOList getMainCover() throws Exception {
      return ListUtil.getDTOListFromQuery(
              "   select" +
              "      b.*" +
              "   from  " +
              "      ins_cover_poltype a" +
              "         inner join ins_cover b on b.ins_cover_id=a.ins_cover_id" +
              "   where " +
              "      a.pol_subtype_id = ? and a.cover_category = ?",
              new Object [] {
                 policy.getStConditionID(),
                 FinCodec.CoverCategory.MAIN
              },
              InsurancePolicyCoverView.class
      );
   }
   
  private DTOList getMainCoverReins() throws Exception {
      return ListUtil.getDTOListFromQuery(
              "   select" +
              "      b.*" +
              "   from  " +
              "      ins_cover_poltype a" +
              "         inner join ins_cover b on b.ins_cover_id=a.ins_cover_id" +
              "   where " +
              "      a.pol_subtype_id = ? and a.cover_category = ?",
              new Object [] {
                 policy.getStConditionID(),
                 FinCodec.CoverCategory.MAIN
              },
              InsurancePolicyCoverReinsView.class
      );
   }



   

   public void onChgCurrency() throws Exception {
      policy.setDbCurrencyRate(
              CurrencyManager.getInstance().getRate(
                      policy.getStCurrencyCode(),
                      policy.getDtPolicyDate()
              )
      );
   }

   public void onChangePolicyType() throws Exception {
      //policy.loadClausules();
      policy.setStRateMethod(policy.getPolicyType().getStRateMethod());
      policy.setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(policy.getStRateMethod()));
      
   }

   public void onChangePolicyConditions() {
      sumInsuredLOV=null;
      coverageLOV=null;
   }

   public void onChangeRiskCategory() {

   }

   public InsurancePolicyItemsView onNewClaimItem() throws Exception {
      if (insItemID == null) throw new Exception("Please select item to be added");

      final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

      item.setStItemClass(FinCodec.PolicyItemClass.CLAIM);

      item.markNew();

      item.setStInsItemID(insItemID);

      item.setStChargableFlag("Y");

      getClaimItems().add(item);

      policy.recalculate();

      return item;
   }

   public void onNewDetail() throws Exception {

      if (insItemID == null) throw new Exception("Please select item to be added");

      final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

      item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

      item.markNew();

      item.setStInsItemID(insItemID);

      getDetails().add(item);

      policy.recalculate();
   }

   public void onDeleteDetail() {
      final int n = Integer.parseInt(detailindex);

      getDetails().delete(n);
   }

   public void onDeleteClaimItem() {
      final int n = Integer.parseInt(claimItemindex);

      getClaimItems().delete(n);
   }

   public void onNewCoinsurance() throws Exception {
      final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

      co.markNew();

      co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);

      getCoins().add(co);

      policy.recalculate();
   }

   public void onDeleteCoinsurance() throws Exception {
      getCoins().delete(Integer.parseInt(coinsIndex));
      policy.recalculate();
   }

   public void onChangeRegion() throws Exception {
      if (policy.getStRegionID()!=null) {
         //policy.setStCoverTypeCode(policy.getPolicyType().getStInsuranceCoverSourceID());
         chgCoverType();
         WebFinLOVRegistry.getInstance().setStCostC(policy.getStCostCenterCode());
         policy.setStCostCenterCode2(policy.getStCostCenterCode());
      }
   }

   public void chgCoverType() throws Exception {

      if (policy.getStRegionID()==null) {
         policy.setStCoverTypeCode(null);
         throw new RuntimeException("Region cannot be empty");
      }

      insItemLOV = null;
      //policy.getCoins().deleteAll();

      if (policy.getStCoverTypeCode()!=null) {
         try {
            //addMyCompany();

            //doNewObject();

            //defineTreaty();
         } catch (Exception e) {
            policy.setStCoverTypeCode(null);
            throw e;
         }
      }
   }

   private void defineTreaty() {
      final boolean inward = policy.getCoverSource().isInward();

      //resetTreaty();
      if (policy.getStInsuranceTreatyID()==null) {
         policy.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
         //selectTreaty();
      }
   }

   public void selectTreaty() {
      
   }

   private void addMyCompany() throws Exception {

      final InsuranceCoverSourceView cs = policy.getCoverSource();

      final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

      co.markNew();

      co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);

      co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));

      co.setStHoldingCompanyFlag("Y");

      getCoins().add(co);
   }

   public void createNewProductionOnly() throws Exception {
      createNew();

      policy.setStProductionOnlyFlag("Y");

      productionModeTabs();
   }

   private void productionModeTabs() {

      if(policy.isProductionMode()) {
         getTabs().enable("TAB_RISK_DET",false);
         getTabs().enable("TAB_CLAUSES",false);
         getTabs().enable("TAB_DEDUCTIBLES",false);
         getTabs().setActiveTab("TAB_PREMI");
      }
   }

   public void doAddSumInsured() throws Exception {
      /*final HashMap map = sumInsuredLOV.getMapOf("ins_tsi_cat_id");

      final InsuranceTSIView tsi = (InsuranceTSIView)map.get(sumInsuredAddID);*/

      if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");

      final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();

      ptsi.setStInsuranceTSIPolTypeID(sumInsuredAddID);

      ptsi.initializeDefaults();

      ptsi.markNew();

      selectedObject.getSuminsureds().add(ptsi);

      policy.recalculate();
   }

   public void onClickDeleteSumInsuredItem() throws Exception {
      if (policy.isStatusEndorse()) {
         final InsurancePolicyTSIView dtsi = ((InsurancePolicyTSIView)selectedObject.getSuminsureds().get(Integer.parseInt(tsiIndex)));
         if (dtsi.hasRef()) {
            dtsi.doVoid();
            policy.recalculate();
            return;
         }
      }

      selectedObject.getSuminsureds().delete(Integer.parseInt(tsiIndex));
      policy.recalculate();
   }

   public String getSumInsuredAddID() {
      return sumInsuredAddID;
   }

   public void setSumInsuredAddID(String sumInsuredAddID) {
      this.sumInsuredAddID = sumInsuredAddID;
   }

   public String getTsiIndex() {
      return tsiIndex;
   }

   public void setTsiIndex(String tsiIndex) {
      this.tsiIndex = tsiIndex;
   }

   public DTOList getSumInsuredAddLOV() throws Exception {
      allowDuplicateTSI = true;

      loadSumInsuredLOV();

      if (allowDuplicateTSI) return sumInsuredLOV;

      final HashMap dupMap = selectedObject.getSuminsureds().getMapOf("ins_tsi_cat_id");

      final DTOList lov = new DTOList();

      for (int i = 0; i < sumInsuredLOV.size(); i++) {
         InsuranceTSIPolTypeView insuranceTSIView = (InsuranceTSIPolTypeView) sumInsuredLOV.get(i);

         if (dupMap!=null)
            if (dupMap.containsKey(insuranceTSIView.getStInsuranceTSICategoryID())) continue;

         lov.add(insuranceTSIView);
      }

      return lov;
   }

   private void loadSumInsuredLOV() throws Exception {
      if (sumInsuredLOV == null) {
         sumInsuredLOV = ListUtil.getDTOListFromQuery(
                 "   select" +
                 "      a.ins_tcpt_id, b.ins_tsi_cat_id, b.description, a.default_tsi_flag" +
                 "   from  " +
                 "      ins_tsicat_poltype a " +
                 "      inner join ins_tsi_cat b on b.ins_tsi_cat_id = a.ins_tsi_cat_id" +
                 "   where" +
                 "      a.pol_type_id=?" ,
                 new Object [] {policy.getStPolicyTypeID()},
                 InsuranceTSIPolTypeView.class
         );
      }
   }

   public void doAddCoverage() throws Exception {
      final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

      cv.setStInsuranceCoverPolTypeID(coverageAddID);

      cv.initializeDefaults();

      final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

      cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
      cv.setStCoverCategory(cvpt.getStCoverCategory());
      cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

      cv.markNew();

      selectedObject.getCoverage().add(cv);
      
       
      final DTOList treatyDetails = selectedObject.getTreatyDetails();     

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
            
           
			final DTOList shares = trdi.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
               
               final InsurancePolicyCoverReinsView cvreins2 = new InsurancePolicyCoverReinsView();
               
               cvreins2.setStInsuranceCoverPolTypeID(coverageAddID);

      		   cvreins2.initializeDefaults();
      		   
               cvreins2.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
			   cvreins2.setStCoverCategory(cvpt.getStCoverCategory());
			   cvreins2.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
			   
			   cvreins2.markNew();
				
			   ri.getCoverage().add(cvreins2);
            }

         }

      policy.recalculate();
   }
   
   public void addCoverageReins(){
   	  try{
   	   final DTOList coverage = selectedObject.getCoverage();
   	  
   	   final DTOList treatyDetails = selectedObject.getTreatyDetails();
   	   
   	   for(int l=0;l<coverage.size();l++){
		 InsurancePolicyCoverView view = (InsurancePolicyCoverView) coverage.get(l);
			   	 
         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
           
			final DTOList shares = trdi.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
               
               final InsurancePolicyCoverReinsView cvreins2 = new InsurancePolicyCoverReinsView();
               
			   cvreins2.setStInsuranceCoverPolTypeID(view.getStInsuranceCoverPolTypeID());

	      	   cvreins2.initializeDefaults();
	      		   
               cvreins2.setStInsuranceCoverID(view.getStInsuranceCoverID());
			   cvreins2.setStCoverCategory(view.getStCoverCategory());
			   cvreins2.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
			   	   
			   cvreins2.markNew();
				
			   ri.getCoverage().add(cvreins2);
            }

           }
         }
   	  }catch(Exception e){
   	  	 throw new RuntimeException ("Eror= "+e);
   	  }

   }

   public String getCoverageAddID() {
      return coverageAddID;
   }

   public void setCoverageAddID(String coverageAddID) {
      this.coverageAddID = coverageAddID;
   }

   public DTOList getInsuranceItemLOV() throws Exception {
      loadInsuranceItemLOV();

      final DTOList lov = new DTOList();

      for (int i = 0; i < insItemLOV.size(); i++) {
         InsuranceItemsView ii = (InsuranceItemsView) insItemLOV.get(i);

         final boolean zeroMax = Tools.isEqual(ii.getLgEntryMax(), LongUtil.zero);

         if (zeroMax) continue;

         lov.add(ii);
      }

      return lov;
   }

   private void loadInsuranceItemLOV() throws Exception {

      if (insItemLOV == null)
         insItemLOV = getRemoteInsurance().getInsuranceItemLOV(policy.getStCoverTypeCode()); 
   }

   public DTOList getCoverageAddLOV() throws Exception {
      loadCoverageLOV();

      final DTOList lov = new DTOList();

      final HashMap map = selectedObject.getCoverage().getMapOf("ins_cover_id");

      for (int i = 0; i < coverageLOV.size(); i++) {
         InsuranceCoverPolTypeView cv = (InsuranceCoverPolTypeView) coverageLOV.get(i);

         if (cv.isMainCover()) continue;

         //if (map!=null)
         //   if (map.containsKey(cv.getStInsuranceCoverID())) continue;

         lov.add(cv);
      }

      return lov;
   }

   private void loadCoverageLOV() throws Exception {
      if (coverageLOV==null)
         coverageLOV = ListUtil.getDTOListFromQuery(
                 "   select" +
                 "      a.ins_cvpt_id, a.ins_cover_id,  b.description, a.cover_category, a.default_cover_flag" +
                 "   from" +
                 "      ins_cover_poltype a" +
                 "      inner join ins_cover b on b.ins_cover_id = a.ins_cover_id" +
                 "   where " +
                 "      a.pol_type_id = ?" ,
                 new Object [] {policy.getStPolicyTypeID()},
                 InsuranceCoverPolTypeView.class
         );
   }

   public void onClickDeleteCoverageItem() throws Exception {
      if (policy.isStatusEndorse()) {
         final InsurancePolicyCoverView cover = ((InsurancePolicyCoverView) selectedObject.getCoverage().get(covIndex.intValue()));
		 //final InsurancePolicyCoverReinsView coverreins = ((InsurancePolicyCoverReinsView) selectedObject.getCoveragereins().get(covReinsIndex.intValue()));

         if (cover.hasRef()) {
            cover.doVoid();
            policy.recalculate();
            return;
         }
      }
		
      selectedObject.getCoverage().delete(covIndex.intValue());
      
      
         final DTOList treatyDetails = selectedObject.getTreatyDetails();     

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
            
           
			final DTOList shares = trdi.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
				
			   if(ri.getCoverage()==null) continue;
			   ri.getCoverage().delete(covIndex.intValue());
            }

         }
      
      policy.recalculate();
   }

   public Integer getCovIndex() {
      return covIndex;
   }

   public void setCovIndex(Integer covIndex) {
      this.covIndex = covIndex;
   }
   
   public Integer getCovReinsIndex() {
      return covReinsIndex;
   }

   public void setCovReinsIndex(Integer covReinsIndex) {
      this.covReinsIndex = covReinsIndex;
   }

   public void btnRecalculate()throws Exception {
   	  //if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA())
      //  policy.setObjIndex(stClaimObject);
      	
      policy.recalculate();   
   }

   public void btnSave() throws Exception {

      getRemoteInsurance().saveParent(policy,policy.getStNextStatus());

      close();
   }

   public void btnCancel() {
      close();
   }

   public String getDeductIndex() {
      return deductIndex;
   }

   public void setDeductIndex(String deductIndex) {
      this.deductIndex = deductIndex;
   }

   public void onChangePolicyTypeGroup() {
      
   }

   public void editCreateSPPA(String policyID) throws Exception {
      superEdit(policyID);

      if (!FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("SPPA can only be created from drafts");
      }

      checkActiveEffective();

      policy.setStNextStatus(FinCodec.PolicyStatus.SPPA);
      initTabs();

      policy.setStEffectiveFlag("N");
      policy.setStActiveFlag("Y");
   }
   
   public void editCreateRenewal(String policyID) throws Exception {
     
     superEdit(policyID);

      checkActiveEffective();

      if (!policy.isEffective())
         throw new RuntimeException("Policy not yet effective");

      if (!(FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) ||
      		FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus()))) {
         throw new RuntimeException("Renewal can only be created from Policy / Endorsement");
      }

      //policy.generatePolicyNo();
      policy.setStPolicyNo(null);
      policy.setStPostedFlag("N");
      policy.setStEffectiveFlag("N");
      policy.setDtPolicyDate(new Date());
      policy.setDtPeriodStart(policy.getDtPeriodEnd());     
      policy.setDtPeriodEnd(null);   
      policy.setStNextStatus(FinCodec.PolicyStatus.RENEWAL);
      initTabs();
   }
   
   
   public void editCreateClaimPLA(String policyID) throws Exception {
      superEdit(policyID);

      checkActiveEffective();
      
      if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
      		throw new RuntimeException("PLA Can Only Be Created From Polis");
      }
      
      if(!policy.canClaimAgain()){
      	 throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Polis Sama");
      }

      if (!policy.isEffective()) {

         throw new RuntimeException("Policy not yet effective");
      }
      
      
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/

	  policy.generatePLANo();
      policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
      policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
      policy.setStDocumentBranchingFlag("Y");
      policy.setStPostedFlag("N");
      policy.setStEffectiveFlag("N");
      policy.setStActiveClaimFlag("Y");

      final DTOList claimItems = policy.getClaimItems();

      if (claimItems.size()==0) {

         insItemID = getClaimBrutoInsItemID();

         final InsurancePolicyItemsView item = onNewClaimItem();

         item.setStChargableFlag("Y");

         item.setStROFlag("Y");
      }

      initTabs();
   }
   
   


   private void checkActiveEffective() {
      if (!policy.isEffective())
         throw new RuntimeException("Please approve the document");

      if (!policy.isActive())
         throw new RuntimeException("Document is not active, please refer to the last active document");
   }

   public void editCreatePolis(String policyID) throws Exception {

      if (policyID==null) {

         createNew();

         policy.setStStatus(FinCodec.PolicyStatus.POLICY);

         return;
      }

      superEdit(policyID);

      checkActiveEffective();

      if (!policy.isEffective())
         throw new RuntimeException("Policy not yet effective");

      if (!FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy can only be created from SPPA");
      }

      //policy.generatePolicyNo();

      policy.setStNextStatus(FinCodec.PolicyStatus.POLICY);
      policy.setStPostedFlag("N");
      policy.setStEffectiveFlag("N");
      
      initTabs();
   }

   public void editCreateClaim(String policyID) throws Exception {
      superEdit(policyID);

      checkActiveEffective();

      if (!policy.isEffective()) {

         throw new RuntimeException("Policy not yet effective");
      }

      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/

      policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
      policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
      policy.setStDocumentBranchingFlag("Y");
      policy.setStPostedFlag("N");
      policy.setStEffectiveFlag("N");

      final DTOList claimItems = policy.getClaimItems();

      if (claimItems.size()==0) {

         insItemID = getClaimBrutoInsItemID();

         final InsurancePolicyItemsView item = onNewClaimItem();

         item.setStChargableFlag("Y");

         item.setStROFlag("Y");
      }

      initTabs();
   }
   
   
  

   private String getClaimBrutoInsItemID() throws Exception {

      final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='CLAIMG' and active_flag='Y'");

      return lu.getCode(0);
   }

   public BigDecimal getTransactionLimit(String cat, String cat2) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         S.setQuery(
                 "   select " +
                 "      max(c.refn"+cat2+") " +
                 "   from " +
                 "         s_user_roles b " +
                 "         inner join ff_table c on c.fft_group_id='CAPA' and c.ref1=b.role_id and c.ref2=? and c.ref3=?" +
                 "   where" +
                 "      b.user_id=?");

         S.setParam(1,cat);
         S.setParam(2,policy.getStPolicyTypeID());
         S.setParam(3,SessionManager.getInstance().getUserID());

         final ResultSet RS = S.executeQuery();

         if (RS.next()) return RS.getBigDecimal(1);

         return null;
      } finally {

         S.release();
      }
   }
   
   

   public BigDecimal getComissionLimit() throws Exception {
      FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
              "   select " +
              "      b.*" +
              "   from " +
              "      ff_table b " +
              "   where b.ref3=? and b.ref1=? and b.ref2 in (select role_id from s_user_roles where user_id=?) and b.fft_group_id='COMM'" +
              "      order by refn1 desc",
                 new Object [] {policy.getStPolicyTypeID(), policy.getStCostCenterCode(), SessionManager.getInstance().getUserID()},
                 FlexTableView.class
         ).getDTO();

      return ft==null?null:ft.getDbReference1();
   }
   
  
   public void btnApprove() throws Exception {
      policy.setStEffectiveFlag("Y");

      if (policy.isStatusPolicy() || policy.isStatusSPPA()) {
         final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass());

         final boolean enoughLimit = Tools.compare(transactionLimit, BDUtil.mul(policy.getDbInsuredAmount(),policy.getDbCurrencyRate()))>0;
				
         if (!enoughLimit) throw new RuntimeException("Policy value beyond user privielege");

         BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),2);

         logger.logDebug("btnApprove: comissionRatio = "+comissionRatio);

         BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimit());
         
         
         
         logger.logDebug("btnApprove: comissionLimit = "+comissionLimit);

         boolean overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
         
         
         logger.logDebug("btnApprove: overLimit = "+overLimit);
         
         

      }

      if (policy.isStatusClaimDLA()) {
         final BigDecimal transactionLimit = getTransactionLimit("CLAIM", "1");

         final boolean enoughLimit = Tools.compare(transactionLimit, policy.getDbClaimAmountApproved())>0;

         if (!enoughLimit) throw new RuntimeException("Claim amount beyond user privielege");
      }

      policy.validate(true);

      if (
              policy.isStatusPolicy()
              || policy.isStatusEndorse()||policy.isStatusClaimDLA()
      )
         policy.setStPostedFlag("Y");

      btnSave();
   }
   
    public void btnApproveReins() throws Exception {
      /*policy.setStEffectiveFlag("Y");
		
      if (policy.isStatusPolicy() || policy.isStatusSPPA()) {
         final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass());

         final boolean enoughLimit = Tools.compare(transactionLimit, policy.getDbInsuredAmount())>0;

         if (!enoughLimit) throw new RuntimeException("Policy value beyond user privielege");

         BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),2);

         logger.logDebug("btnApprove: comissionRatio = "+comissionRatio);

         BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimit());

         logger.logDebug("btnApprove: comissionLimit = "+comissionLimit);

         boolean overLimit = Tools.compare(comissionRatio, comissionLimit)>0;

         logger.logDebug("btnApprove: overLimit = "+overLimit);

         if (overLimit) throw new RuntimeException("Comission/Discount amount is too high");
      }*/
      /*
      if (policy.isStatusClaim()) {
         final BigDecimal transactionLimit = getTransactionLimit("CLAIM", "1");

         final boolean enoughLimit = Tools.compare(transactionLimit, policy.getDbClaimAmountEstimate())>0;

         if (!enoughLimit) throw new RuntimeException("Claim amount beyond user privielege");
      }

      policy.validate(true);

      if (
              policy.isStatusPolicy()
              || policy.isStatusEndorse()||policy.isStatusClaimDLA()
      )
         policy.setStPostedFlag("Y");*/

      btnSave();
   }

   public void btnReject() throws Exception {
      policy.setStEffectiveFlag("N");
      policy.setStActiveFlag("N");

      policy.recalculate();

      final boolean noReverse = policy.isStatusClaimDLA();

//      if (noReverse)
//         getRemoteInsurance().save(policy, null);
 //     else
//         getRemoteInsurance().saveAndReverse(policy);

      close();

   }

   public void edit(String policyID) throws Exception {
      superEdit(policyID);
      if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

      if (policy.isEffective()) throw new Exception("This document is not editable, because it has already approved");
   }
   
   public void editReins(String policyID) throws Exception {
      //superEditReins(policyID);
      if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

      if (policy.isEffective()) throw new Exception("This document is not editable, because it has already approved");
   }

   public void changePeriodBase() {
      policy.calculatePeriods();
      //policy.recalculate();
   }

   public void onNewObjDeductible() {
      final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

      ded.markNew();

      selectedObject.getDeductibles().add(ded);

   }

   public void onDeleteObjDeductible() {
      selectedObject.getDeductibles().delete(Integer.parseInt(objDeductIndex));
   }

   public void onNewDeductible() {
      final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

      ded.markNew();

      getDeductibles().add(ded);
   }

   public void onDeleteDeductible() {
      getDeductibles().delete(Integer.parseInt(deductIndex));
   }

   public String getObjDeductIndex() {
      return objDeductIndex;
   }

   public void setObjDeductIndex(String objDeductIndex) {
      this.objDeductIndex = objDeductIndex;
   }

   public boolean isRejectable() {
      return (policy.getStNextStatus()==null);
   }

   public void changePeriodFactor() {
      policy.calculatePeriods();
      //policy.recalculate();
   }

   public void calcPeriods() {
      policy.calculatePeriods();
   }
   
   public void defaultPeriod(){
   	 policy.defaultPeriods();
   }

   public void addTreatyShare() {
      final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

      ri.markNew();
      
      getSelectedObjTreatyDetail().getShares().add(ri);
        
   }
   
   public void addTreatyShareReins(){
   	   try{
      final DTOList cover = selectedObject.getCoverage();
      

      for(int i=0;i<cover.size();i++){
      		InsurancePolicyCoverView coverage = (InsurancePolicyCoverView) cover.get(i);
      		final DTOList view = (DTOList) getSelectedObjTreatyShare().getCoverage();
      		      		      		
      		InsurancePolicyCoverReinsView coverRein = new InsurancePolicyCoverReinsView();
      		
      		coverRein.setStInsuranceCoverPolTypeID(coverage.getStInsuranceCoverPolTypeID());

      		coverRein.initializeDefaults();
      		   
            coverRein.setStInsuranceCoverID(coverage.getStInsuranceCoverID());
			coverRein.setStCoverCategory(coverage.getStCoverCategory());
			coverRein.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
			
			coverRein.markNew();
      		
      		getSelectedObjTreatyShare().getCoverage().add(coverRein);
	
      }
      
      policy.recalculate();
      
      }catch(Exception e){
      }
   }

   public void deleteTreatyShare() {
      getSelectedObjTreatyDetail().getShares().delete(idxTreatyShares.intValue());
   }

   private InsurancePolicyReinsView getSelectedObjTreatyShare() {
      return (InsurancePolicyReinsView)getSelectedObjTreatyDetail().getShares().get(idxTreatyShares.intValue());
   }
   
   public void deleteTreatyShareCover() {
      getSelectedObjTreatyShare().getCoverage().delete(covIndex.intValue());
   }

   private InsurancePolicyCoverReinsView getSelectedObjTreatyShareCover() {
      return (InsurancePolicyCoverReinsView)getSelectedObjTreatyShare().getCoverage().get(idxTreatySharesCover.intValue());
   }

   private InsurancePolicyTreatyDetailView getSelectedObjTreatyDetail() {
      return (InsurancePolicyTreatyDetailView)getSelectedObjTreaty().getDetails().get(idxTreatyDetail.intValue());
   }

   private InsurancePolicyTreatyView getSelectedObjTreaty() {
      return (InsurancePolicyTreatyView) selectedObject.getTreaties().get(idxTreaty.intValue());
   }

   public void approval(String policyID) throws Exception {
      edit(policyID);
      
      super.setReadOnly(true);

      approvalMode = true;
   }
   
   public void approvalReins(String policyID) throws Exception {
      editReins(policyID);
      
      super.setReadOnly(true);

      approvalMode = true;
      
      reasMode = true;
   }

   public boolean isApprovalMode() {
      return approvalMode;
   }

   public void setApprovalMode(boolean approvalMode) {
      this.approvalMode = approvalMode;
   }

   public void testGeneratePolicyNo() throws Exception {
      policy.generatePolicyNo();
   }

   public void testGenerateEndorseNo() {
      policy.generateEndorseNo();
   }

   

   public void changeCoTreaty() {

   }

   public void chgRateClass() {
   	  /*
      InsurancePolicyCoverView cover  = (InsurancePolicyCoverView) policy.getCovers().get(covIndex.intValue());

      LookUpUtil rslu = FinCodec.RateScale.getLookUp();
      int idx = rslu.getIndex(cover.getStRateScale());

      idx++;

      if (idx>=rslu.size()) idx=0;

      cover.setStRateScale(rslu.getCode(idx));
      */
 	  
   }

   public void btnApproveparent() throws Exception {

      policy.setStEffectiveFlag("Y");

      btnSave();
   }
}

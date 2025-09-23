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
import com.crux.file.FileView;
import com.crux.file.FileManager;

import com.webfin.entity.model.EntityView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import java.io.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.joda.time.DateTime;

public class PolicyMandiriForm extends Form {
    
    private final static transient LogManager logger = LogManager.getInstance(PolicyForm.class);
    
    private InsurancePolicyView policy;
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
    private InsurancePolicyObjDefaultView selectedDefaultObject;
    private DTOList objectSumInsureds;
    private DTOList objectCoverage;
    public String sumInsuredAddID;
    private DTOList sumInsuredLOV;
    private String tsiIndex;
    private String claimItemindex;
    private String deductIndex;
    private String objDeductIndex;
    private String instIndex;
    private String coverageAddID;
    private Integer covIndex;
    private Integer covReinsIndex;
    private Integer treatiesIndex;
    private Integer treatyDetailIndex;
    private DTOList coverageLOV;
    private DTOList insItemLOV;
    private DTOList arTaxLOV;
    
    private Integer idxTreaty;
    private Integer idxTreatyDetail;
    private Integer idxTreatyShares;
    private Integer idxTreatySharesCover;
    
    private boolean reasMode;
    private boolean approvalMode;
    private boolean allowDuplicateTSI;
    private boolean overKreasiLimit;
    private boolean reverseMode;
    private boolean reApprovedMode;
    private boolean savePolicyHistoryMode;
    private boolean approvedViaReverseMode;
    private boolean excludeReasMode = false;
    private String insitemid;
    private boolean changeTreatyObject = false;
    private String stItemCategory;
    private String paramriskcat;
    private boolean reasApprovedMode;
    private boolean enableNoPolicy = false;
    
    private String stFilePhysic;
    
    private boolean disabled=true;
    
    private boolean manualTreaty = SessionManager.getInstance().getSession().hasResource("MANUAL_TREATY");
    
    private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
    
    private boolean canCreatePolicyHistory = false;
    private boolean totalEndorseMode = false;
    private boolean partialEndorseTSIMode = false;
    private boolean descriptionEndorseMode = false;
    private boolean partialEndorseRateMode = false;
    private boolean restitutionEndorseMode = false;
    
    private boolean approvalByDirectorMode;
    private boolean editClaimNoteMode;

    public boolean isEnableNoPolicy() {
        return enableNoPolicy;
    }
    
    public void setEnableNoPolicy(boolean enableNoPolicy){
        this.enableNoPolicy = enableNoPolicy;
    }
    
    public boolean isCanCreatePolicyHistory() {
        return canCreatePolicyHistory;
    }
    
    public void setCanCreatePolicyHistory(boolean canCreatePolicyHistory){
        this.canCreatePolicyHistory = canCreatePolicyHistory;
    }
    
    public boolean isEnableSuperEdit() {
        return enableSuperEdit;
    }
    
    public boolean isManualTreaty() {
        return manualTreaty;
    }
    
    public String getStFilePhysic() {
        return stFilePhysic;
    }
    
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
    
    public String getStItemCategory() {
        return stItemCategory;
    }
    
    public void setStItemCategory(String stItemCategory) {
        this.stItemCategory = stItemCategory;
    }
    
    public String getInsItem() {
        return insitemid;
    }
    
    public void setInsItem(String insitemid) {
        this.insitemid = insitemid;
    }
    
    public String getStClaimObject() {
        
        if (policy.getClaimObject()!=null)
            stClaimObject = String.valueOf(policy.getObjects().indexOf(policy.getClaimObject()));
        
        return stClaimObject;
    }
    
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
        policy.recalculateTreaty();
        
    }
    
    public void changeTreaty() {
        final DTOList objects = policy.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            obj.setStInsuranceTreatyID(null);
            obj.getTreaties().deleteAll();
            ritabs=null;
        }
        //policy.setStInsuranceTreatyID(null);
        changeTreatyObject = false;
    }
    
    public void changeTreatyPerObject() {
        selectedObject.getTreaties().deleteAll();
        selectedObject.setStInsuranceTreatyID(null);
        //policy.setStInsuranceTreatyID(null);
        ritabs=null;
        changeTreatyObject = true;
    }
    
    public void raiseTreatyLevel() throws Exception{
        final InsurancePolicyTreatyView treaty = (InsurancePolicyTreatyView)getTreaties().get(0);
        
        //treaty.raise(policy.getStPolicyTypeID(), getDbInsuredAmount());
        treaty.raise(policy.getStPolicyTypeID() , selectedObject.getDbObjectInsuredAmountShare(), selectedObject.getDbTreatyLimitRatio(), policy.getDbCurrencyRateTreaty(),policy.getStCurrencyCode());
        
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
    
    public InsurancePolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;
    }
    
    public void setSelectedDefaultObject(InsurancePolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;
    }
    
    public InsurancePolicyView getPolicy() {
        return policy;
    }
    
    public void setPolicy(InsurancePolicyView policy) {
        this.policy = policy;
    }
    
    public void createNew() throws Exception {
        policy = new InsurancePolicyView();
        
        policy.setDetails(new DTOList());
        policy.setObjects(new DTOList());
        policy.setCovers(new DTOList());
        policy.setSuminsureds(new DTOList());
        policy.setCoins(new DTOList());
        policy.setDeductibles(new DTOList());
        policy.setInstallment(new DTOList());
        policy.setStInstallmentPeriods("1");
        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());
        
        policy.markNew();
        
        policy.setStStatus(FinCodec.PolicyStatus.DRAFT);
        policy.setStActiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        
        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbCurrencyRateTreaty(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));
        
        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
        onNewInstallment();
        
        policy.markNew();
        
        initTabs();
    }
    
    public void createNewPolicyHistory() throws Exception {
        policy = new InsurancePolicyView();
        
        policy.setDetails(new DTOList());
        policy.setObjects(new DTOList());
        policy.setCovers(new DTOList());
        policy.setSuminsureds(new DTOList());
        policy.setCoins(new DTOList());
        policy.setDeductibles(new DTOList());
        policy.setInstallment(new DTOList());
        policy.setStInstallmentPeriods("1");
        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());
        
        policy.markNew();
        
        policy.setStStatus(FinCodec.PolicyStatus.HISTORY);
        policy.setStNextStatus(FinCodec.PolicyStatus.HISTORY);
        policy.setStActiveFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        
        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbCurrencyRateTreaty(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));
        
        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
        onNewInstallment();
        
        policy.markNew();
        
        initTabs();
    }
    
    public void afterUpdateForm() {
        switchSelectedObject();
        //policy.recalculate();
      /*
      if (policy.isModified()){
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()){
                                try{
                                        recalculateReas();
                                }catch(Exception e){}
       
        }
      }*/
        
    }
    
    public void recalculateReas() throws Exception{
        policy.recalculateCoverRI();
        policy.recalculateTreaty();
        policy.recalculateCoverRI();
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
    
    public void switchSelectedObject() {
        if (stSelectedObject==null){selectedObject=null;} else{
            selectedObject = (InsurancePolicyObjectView) getObjects().get(Integer.parseInt(stSelectedObject));
        }
        
    }
    
    public DTOList getSuminsureds() {
        return policy.getSuminsureds();
    }
    
    public DTOList getCoins() throws Exception {
        return policy.getCoins2();
    }
    
    public DTOList getInstallment() throws Exception {
        return policy.getInstallment();
    }
    
    public DTOList getCovers() {
        return policy.getCovers();
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
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
            
            obj.getTreaties().markAllUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
            }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
            
        }
        
        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();
        
        policy.showItemsAccount();
        
        //policy.reCalculateInstallment();
        
        initTabsMandiri();
        
    }
    
    public void superEditHistory(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        final boolean inward = policy.getCoverSource().isInward();
        
        //if (true)
        //   throw new RuntimeException("success");
        
        final DTOList objects = policy.getObjects();
        
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
            
            obj.getTreaties().markAllUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
            }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
            
        }
        
        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();
        
        //policy.showItemsAccount();
        
        //policy.reCalculateInstallment();
        
        initTabs();
        
    }
    
    
    public void superEditApprove(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        //policy.getClausules().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        
        policy.showItemsAccount();
        
        //policy.reCalculateInstallment();
        
        //policy.reCalculateInstallment2();
        policy.setApprovalMode(true);
        
        initTabs();
        
    }
    
    public void superEditReins(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            obj.markUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList shares = trdi.getShares();
                
                shares.markAllUpdate();
                
                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                    
                    ri.setStApprovedFlag("Y");
                }
            }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
            
        }
        
        //policy.setStReinsuranceApprovedWho(UserManager.getInstance().getUser().getStUserID());
        policy.setDtReinsuranceApprovedDate(new Date());
        
        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        
        initTabs();
    }
    
    public FormTab getRdtabs() {
        return rdtabs;
    }
    
    public void onFormCreate() {
        initTabsMandiri();
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
        //tabs.add(new FormTab.TabBean("TAB_DEDUCTIBLES","DEDUCTIBLES",false));
        tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",true));
        tabs.add(new FormTab.TabBean("TAB_INST","{L-ENGPAYMENT-L}{L-INAPEMBAYARAN-L}",false));
        tabs.add(new FormTab.TabBean("TAB_COINS","{L-ENGCOINSURANCE-L}{L-INAKOASURANSI-L}",true));
        tabs.add(new FormTab.TabBean("TAB_COINS_COVER","{L-ENGCO-COVER-L}{L-INACO-COVER-L}",true));
        //tabs.add(new FormTab.TabBean("TAB_COVER_REINS","{L-ENGR/I-COVER-L}{L-INAR/I-COVER-L}",true));
        tabs.add(new FormTab.TabBean("TAB_CLAIMCO","{L-ENGCLAIM CO-L}{L-INAKLAIM CO-L}",false));
        tabs.add(new FormTab.TabBean("TAB_CLAIMCOINS_COVER","{L-ENGCLAIM CO-COVER-L}{L-INAKLAIM CO-COVER-L}",false));
        //tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));
        //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));
        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INALAMPIRAN-L}",false));
        
        tabs.setActiveTab("TAB_RISK_DET");
        
        boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
        boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
        boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
        boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
        boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
        boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());
        boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStStatus());
        boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStStatus());
        boolean statusEndorseRI = FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(getStStatus());
        
        boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
        boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
        
        if(!statusDraft && !statusSPPA){
            tabs.enable("TAB_CLAUSES",true);
            //tabs.enable("TAB_PREMI",true);
            tabs.enable("TAB_INST",true);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            //tabs.enable("TAB_OPTIONS",true);
        }
        
        
        if (statusEndorse||statusClaimEndorse||statusEndorseRI) {
            tabs.enable("TAB_ENDORSE",true);
            tabs.setActiveTab("TAB_ENDORSE");
            
        }
        
        if (statusClaim||statusClaimEndorse) {
            tabs.enable("TAB_CLAIM",true);
            tabs.enable("TAB_CLAIM_ITEM",true);
            tabs.enable("TAB_CLAIM_RE",true);
            tabs.enable("TAB_CLAIMCO",true);
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            //tabs.enable("TAB_COVER_REINS",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",false);
            tabs.enable("TAB_CLAIMCOINS_COVER",true);
            tabs.setActiveTab("TAB_CLAIM");
        }
        
        rdtabs = new FormTab();
        
        rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
        //rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
        rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_DED","{L-ENGDEDUCTIBLES-L}{L-INARESIKO SENDIRI-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));
        
        rdtabs.setActiveTab("TAB_DETAIL");
        
        if (!(reasMode || statusClaim || statusEndorseRI)) {
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
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            //tabs.enable("TAB_COVER_REINS",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",false);
        }
        
        claimtabs.setActiveTab("TAB_DOCUMENTS");
        
        if(statusClaimEndorse){
            tabs.setActiveTab("TAB_ENDORSE");
        }
        
        if(statusDLA||statusClaimEndorse){
            claimtabs.enable("TAB_DLA",true);
        }
        
    }
    
    public void view(String policyID) throws Exception {
        
        if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");
        
        policy = getRemoteInsurance().getInsurancePolicy(policyID);
        
        if (policy==null) throw new RuntimeException("Policy not found");
        
        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();
        
        //policy.reCalculateInstallment();
        
        //policy.reCalculateInstallment2();
        
        super.setReadOnly(true);
        
        //productionModeTabs();
        
        initTabsMandiri();
    }
    
    public LOV getLovObjects() {
        return getObjects();
    }
    
    public DTOList getObjects() {
        return policy.getObjects();
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
    
    public void selectObject()throws Exception {
        
    }
    
    
    public void doNewObject() throws Exception {
        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getClObjectClass().newInstance();
        o.markNew();
        o.setPolicy(policy);
        o.setCoverage(new DTOList());
        
        o.setSuminsureds(new DTOList());
        o.setDeductibles(new DTOList());
        
        final DTOList mainCovers = getMainCover();
        
        for (int i = 0; i < mainCovers.size(); i++) {
            InsurancePolicyCoverView mainCover = (InsurancePolicyCoverView) mainCovers.get(i);
            mainCover.markNew();
            mainCover.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
            
            o.getCoverage().add(mainCover);
        }
        getObjects().add(o);
        
        setStSelectedObject(String.valueOf(getObjects().size()-1));
        switchSelectedObject();
        
        final DTOList covers = getCoverageAddLOV();
        
        for (int i = 0; i < covers.size(); i++) {
            InsuranceCoverPolTypeView cpt = (InsuranceCoverPolTypeView) covers.get(i);
            
            if (!Tools.isYes(cpt.getStDefaultCoverFlag())) continue;
            
            final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
            
            cv.setStInsuranceCoverPolTypeID(cpt.getStInsuranceCoverPolTypeID());
            
            cv.initializeDefaults();
            
            final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
            
            cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
            cv.setStCoverCategory(cvpt.getStCoverCategory());
            
            cv.markNew();
            
            o.getCoverage().add(cv);
            
        }
        
        
        final DTOList sumInsuredAddLOV = getSumInsuredAddLOV();
        
        for (int i = 0; i < sumInsuredAddLOV.size(); i++) {
            InsuranceTSIPolTypeView ipt = (InsuranceTSIPolTypeView) sumInsuredAddLOV.get(i);
            
            if (!Tools.isYes(ipt.getStDefaultTSIFlag())) continue;
            
            final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
            
            ptsi.setStInsuranceTSIPolTypeID(ipt.getStInsuranceTSIPolTypeID());
            
            ptsi.initializeDefaults();
            
            ptsi.markNew();
            
            selectedObject.getSuminsureds().add(ptsi);
        }
        
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
            defineObjectTreaty();
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")||policy.getStPolicyTypeID().equalsIgnoreCase("98")||policy.getStPolicyTypeID().equalsIgnoreCase("99"))
            selectedObject.setStRiskCategoryID(policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart()));
        
        policy.getHandler().onNewObject(policy,o);
        
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
    
    
    
    public void doDeleteObject() throws Exception {
        
        int n = Integer.parseInt(stSelectedObject);
        
        final DTOList objects = policy.getObjects();
        
        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(n);
        
        obj.getDeductibles().deleteAll();
        obj.getSuminsureds().deleteAll();
        obj.getCoverage().deleteAll();
        obj.getClausules().deleteAll();
        
        objects.delete(n);
        
        selectedObject=null;
        stSelectedObject=null;
    }
    
    public void onChgCurrency() throws Exception {
        policy.setDbCurrencyRate(
                CurrencyManager.getInstance().getRate(
                policy.getStCurrencyCode(),
                policy.getDtPolicyDate()
                )
                );
        policy.setDbCurrencyRateTreaty(
                CurrencyManager.getInstance().getRateTreaty(
                policy.getStCurrencyCode(),
                policy.getDtPolicyDate()));
        
        policy.setStCurrencyCode(policy.getStCurrencyCode());
    }
    
    public void onChangePolicyType() throws Exception {
        policy.loadClausules();
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
        
        //policy.recalculate();
        
        return item;
    }
    
    public void onNewDetail() throws Exception {
        
        if (insItemID == null) throw new Exception("Please select item to be added");
        
        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();
        
        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);
        
        item.markNew();
        
        item.setStInsItemID(insItemID);
        
        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());
        
        getDetails().add(item);
        
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
        
        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINS);
        
        getCoins().add(co);
        
        //policy.recalculate();
    }
    
    public void onDeleteCoinsurance() throws Exception {
        getCoins().delete(Integer.parseInt(coinsIndex));
        //policy.recalculate();
    }
    
    public void onChangeRegion() throws Exception {
        if (policy.getStRegionID()!=null) {
            policy.setStCoverTypeCode(policy.getPolicyType().getStInsuranceCoverSourceID());
            chgCoverType();
            policy.setStCostCenterCode2(policy.getStCostCenterCode());
        }
    }
    
    public void chgCoverType() throws Exception {
        
        if (policy.getStRegionID()==null) {
            policy.setStCoverTypeCode(null);
            throw new RuntimeException("Region cannot be empty");
        }
        
        insItemLOV = null;
        policy.getCoins2().deleteAll();
        policy.getCoinsCoverage().deleteAll();
        final String polType = policy.getStPolicyTypeID();
        
        if(polType.equalsIgnoreCase("21"))
            if (policy.getStCoverTypeCode()!=null)
                if(policy.getStCoverTypeCode().equalsIgnoreCase("DIRECT")){
            policy.setStCoverTypeCode(null);
            throw new RuntimeException("Jenis Penutupan PA Kreasi Tidak Boleh Direct");
                }
        
        
        if(polType.equalsIgnoreCase("21")||polType.equalsIgnoreCase("3")||polType.equalsIgnoreCase("55")||polType.equalsIgnoreCase("56")||polType.equalsIgnoreCase("57")||
                polType.equalsIgnoreCase("58")||polType.equalsIgnoreCase("51")||polType.equalsIgnoreCase("52")||polType.equalsIgnoreCase("53")||polType.equalsIgnoreCase("54")||
                polType.equalsIgnoreCase("5")||polType.equalsIgnoreCase("14"))
            defaultPeriod();
        
        if (policy.getStCoverTypeCode()!=null) {
            try {
                addMyCompany();
                
                addMyCompanyCoverage();
                
                addBiayaPolisMateraiAutomatic();
                
                addKomisiAutomatic();
                
            } catch (Exception e) {
                policy.setStCoverTypeCode(null);
                throw e;
            }
        }
        
    }
    
    private void defineTreaty() throws Exception{
        final boolean inward = policy.getCoverSource().isInward();
        
        String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            if(policy.getStPolicyTypeID().equalsIgnoreCase("3") || policy.getStPolicyTypeID().equalsIgnoreCase("1"))
                if(obj.getDtReference1()!=null)
                    treatyID = obj.getDtReference1()!=null?policy.getInsuranceTreatyID(obj.getDtReference1()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
            
            if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("15")){
                setCMTreaty(obj);
            }else{
                obj.setStInsuranceTreatyID(treatyID);
            }
        }
    }
    
    public void setCMTreaty(InsurancePolicyObjDefaultView obj)throws Exception{
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList cov = obj.getCoverage();
        for (int i = 0; i < cov.size(); i++) {
            InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(i);
            
            if(cover.getStInsuranceCoverID().equalsIgnoreCase("139")){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(getInsuranceTreatyIDCM(policy.getDtPeriodStart(), "139"));
            }
            
            if(cover.getStInsuranceCoverID().equalsIgnoreCase("143")){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(getInsuranceTreatyIDCM(policy.getDtPeriodStart(), "143"));
            }
            
        }
    }
    
    private String getInsuranceTreatyIDCM(Date per_start, String ins_cover_id) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      ins_treaty_id,treaty_name " +
                    "   from " +
                    "         ins_treaty" +
                    "   where" +
                    "      treaty_period_start <= ? " +
                    "   and treaty_period_end >= ? " +
                    "   and ref1 = ? ");
            
            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, ins_cover_id);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }
    
    private void defineObjectTreaty() throws Exception{
        final boolean inward = policy.getCoverSource().isInward();
        
        if(policy.getDtPeriodStart()!=null)
            selectedObject.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
        else
            selectedObject.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
    }
    
    
    public void selectTreaty() throws Exception{
        
        if(changeTreatyObject==false){
            final DTOList objects = policy.getObjects();
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                obj.getTreaties().deleteAll();
                obj.setStInsuranceTreatyID(selectedObject.getStInsuranceTreatyID());
                ritabs=null;
            }
        }
        policy.recalculate();
        policy.recalculateTreaty();
    }
    
    public void selectTreatyPerObject() {
        
    }
    
    private void addMyCompany() throws Exception {
        
        final InsuranceCoverSourceView cs = policy.getCoverSource();
        
        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();
        
        co.markNew();
        
        co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);
        
        co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
        
        co.setStHoldingCompanyFlag("Y");
        
        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINS);
        
        getCoins().add(co);
    }
    
    private void addMyCompanyCoverage() throws Exception {
        boolean canCreate = false;
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) canCreate = true;
        else if(policy.getStPolicyTypeID().equalsIgnoreCase("98")) canCreate = true;
        else if(policy.getStPolicyTypeID().equalsIgnoreCase("99")) canCreate = true;
        
        if(!canCreate) return;
        
        final InsuranceCoverSourceView cs = policy.getCoverSource();
        
        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();
        
        co.markNew();
        
        co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);
        
        co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
        
        co.setStHoldingCompanyFlag("Y");
        
        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);
        
        getCoinsCoverage().add(co);
    }
    
    private void addLeaderCompany() throws Exception {
        
        final InsuranceCoverSourceView cs = policy.getCoverSource();
        
        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();
        
        co.markNew();
        
        co.setStPositionCode(FinCodec.CoInsurancePosition.LEADER);
        
        //co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
        
        //co.setStHoldingCompanyFlag("Y");
        
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
        
        if(sumInsuredAddID==null) throw new RuntimeException("Pilih dulu item yang ingin ditambah");
        
        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
        
        ptsi.setStInsuranceTSIPolTypeID(sumInsuredAddID);
        
        ptsi.initializeDefaults();
        
        ptsi.markNew();
        
        selectedObject.getSuminsureds().add(ptsi);
        
        //policy.recalculate();
    }
    
    public void onClickDeleteSumInsuredItem() throws Exception {
        if (policy.isStatusEndorse()) {
            final InsurancePolicyTSIView dtsi = ((InsurancePolicyTSIView)selectedObject.getSuminsureds().get(Integer.parseInt(tsiIndex)));
            if (dtsi.hasRef()) {
                dtsi.doVoid();
                //policy.recalculate();
                return;
            }
        }
        
        selectedObject.getSuminsureds().delete(Integer.parseInt(tsiIndex));
        //policy.recalculate();
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
        
      /*
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
       
      }*/
        
        //policy.recalculate();
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
            throw new RuntimeException("Eror= "+e);
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
            
            stItemCategory = ii.getStItemCategory();
            
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
//        if (policy.isStatusEndorse()) {
//            final InsurancePolicyCoverView cover = ((InsurancePolicyCoverView) selectedObject.getCoverage().get(covIndex.intValue()));
//            
//            if (cover.hasRef()) {
//                cover.doVoid();
//                return;
//            }
//        }
        
        selectedObject.getCoverage().delete(covIndex.intValue());
        
        //policy.recalculate();
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
        
        policy.recalculateBasicMandiri();
        
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusEndorseIntern()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()){
            policy.recalculateTreaty();
        }
    }
    
    public void btnKreasi()throws Exception{
        if(policy.getStPolicyNo()==null) throw new RuntimeException("Nomor Polis Tidak Boleh Kosong");
        //if(policy.getDtPeriodStart()==null) throw new RuntimeException("Tanggal Periode Awal Tidak Boleh Kosong");
        downloadHistory();
    }
    
    public void btnRecalculateCoverRI()throws Exception {
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI()){
            policy.recalculateCoverRI();
        }
    }
    
    public void btnSave() throws Exception {
        
        if(!isInputPaymentDateMode()){
            policy.recalculateBasicMandiri();
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()){
                if(!approvalMode){
                    policy.recalculateTreaty();
                    policy.validateExclusionRisk(false);
                    policy.validateTreaty(false);
                }
            }
            
            policy.validate(false);
        }
        
        getRemoteInsurance().save(policy,policy.getStNextStatus(),approvalMode);
                
        close();
    }
    
    public String btnSavePolicyHistory() throws Exception {
        
        policy.recalculate();
        
        if(policy.isStatusHistory()){
            if(!approvalMode){
                policy.recalculateTreaty();
                policy.validateExclusionRisk(false);
                policy.validateTreaty(false);
            }
        }
        
        policy.validateData();
        
        policy.validate(false);
        
        policy.checkHistoryPolicyNo();
        
        String polID = getRemoteInsurance().savePolicyHistory(policy,policy.getStNextStatus(),approvalMode);
        
        close();
        
        return polID;
    }
    
    public void btnReverse() throws Exception {
        
        boolean withinCurrentMonth = DateUtil.getDateStr(policy.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        final boolean blockValidPolicyDateAdmin = Parameter.readBoolean("BLOCKING_POLICY_DATE_ADMIN");
        boolean canReverse = true;
        
        if(!policy.isStatusClaim())
            if(!withinCurrentMonth) canReverse = false;
        
        if(policy.isStatusClaim()){
            boolean withinCurrentMonthClaim = DateUtil.getDateStr(policy.getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
            if(!withinCurrentMonthClaim) canReverse = false;
            
            if(!withinCurrentMonthClaim)
                if(policy.isStatusClaimPLA()) canReverse = true;
        }
        
        if(!blockValidPolicyDateAdmin) canReverse = true;
        
        if(policy.getStReference12()!=null)
            if(Tools.isYes(policy.getStReference12())) throw new RuntimeException("Polis Sudah Di Transfer Ke Sistem Keuangan Online (Foxpro)");

        if (!canReverse) throw new RuntimeException("Tanggal Polis/DLA Tidak Valid (Sudah Tutup Produksi)");
        
        getRemoteInsurance().reverse(policy);
        
        close();
    }
    
    public void btnReApprove() throws Exception {
        
        getRemoteInsurance().reverse(policy);
        
        final boolean inward = policy.getCoverSource().isInward();
        final DTOList objects = policy.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            obj.setStInsuranceTreatyID(null);
            obj.getTreaties().deleteAll();
            
            if(policy.getDtPeriodStart()!=null)
                obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
            else
                obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
        }
        
        policy.recalculate();
        policy.recalculateTreaty();
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        getRemoteInsurance().saveAfterReverse(policy, policy.getStNextStatus(), false);
        
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
            throw new RuntimeException("SPPA hanya bisa dibuat dari penawaran");
        }
        
        checkActiveEffective();
        
        policy.setStNextStatus(FinCodec.PolicyStatus.SPPA);
        initTabs();
        
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStEffectiveFlag("N");
        policy.setStActiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
    }
    
    public void editCreateRenewal(String policyID) throws Exception {
        
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
        if (!(FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()))) {
            throw new RuntimeException("Polis Perpanjangan hanya bisa dibuat dari Polis, Endorsemen, History, dan Renewal");
        }
        
        //policy.generatePolicyNo();
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStPolicyNo(null);
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setDtPolicyDate(new Date());
        policy.setDtPeriodStart(policy.getDtPeriodEnd());
        policy.setDtPeriodEnd(null);
        policy.setStNextStatus(FinCodec.PolicyStatus.RENEWAL);
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference12(null);
        changeTreaty();
        defineTreaty();
        initTabs();
    }
    
    public void editCreateClaimPLA(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        policy.checkLastPolicyNo(policy.getStPolicyNo());
        
        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");
        
        if(blockPremiNotPaid)
            if(!policy.isPremiPaidInSystem())
                throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");
        
        
        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }
        
      /*
      if(!policy.canClaimAgain()){
         throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Polis Sama");
      }*/
        
        if (!policy.isEffective()) {
            
            throw new RuntimeException("Polis Belum Disetujui");
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
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference12(null);
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
           
            policy.invokeCustomCriteria(obj);
         
        }
        
        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            item.setStROFlag("Y");
        }
        
        initTabs();
    }
    
    public void editCreateClaimDLA(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");
        
        
        if(blockPremiNotPaid)
            if(!policy.isPremiPaidInSystem())
                throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");
        
        
        if(!policy.isStatusClaimPLA()){
            throw new RuntimeException("DLA hanya bisa dibuat dari PLA");
        }
        
        if (!policy.isEffective()) {
            
            throw new RuntimeException("Policy not yet effective");
        }
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/
        
        policy.generateDLANo();
        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStCoverNoteFlag("N");
        policy.setStReference3(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        
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
        
        defineTreaty();
        
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStNextStatus(FinCodec.PolicyStatus.POLICY);
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("51") && policy.getStCostCenterCode().equalsIgnoreCase("24")){
            setEnableNoPolicy(true);
            policy.generatePolicyNoWithoutCounter();
        }
        
        initTabs();
    }
    
    public void editCreateEndorse(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        
        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);
            
            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            policy.invokeCustomCriteria(obj);
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);
        
        if(policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
            policy.generatePersetujuanPrinsipEndorseNo();
        
        initTabs();
        
    }
    
    public void editCreateEndorseRI(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.setStPolicyNo(policy.getStPolicyNo());
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSERI);
        policy.setStReference3(null);
        
        //reasMode = true;
        
        initTabs();
        
    }
    
    public void editCreateEndorseIntern(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.setStPolicyNo(policy.getStPolicyNo());
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSEIN);
        
        //reasMode = true;
        
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
        policy.setStCoverNoteFlag("N");
        
        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            item.setStROFlag("Y");
        }
        
        initTabs();
    }
    
    public void editCreateClaimEndorse(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective()) {
            
            throw new RuntimeException("Policy not yet effective");
        }
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/
        
        //policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        //policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        //policy.setStDocumentBranchingFlag("Y");
        //policy.setStPostedFlag("N");
        //policy.setStEffectiveFlag("N");
        
        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            item.setStROFlag("Y");
        }
        
        //policy.setDbClaimAmountEndorse(policy.)
        
        policy.generateClaimEndorseDLA();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        
        initTabs();
        //
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
    
    public BigDecimal getComissionLimitBackup() throws Exception {
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
    
    public BigDecimal getComissionLimit() throws Exception {
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 in (select role_id from s_user_roles where user_id=?) and b.fft_group_id='COMM'" +
                "      order by refn1 desc",
                new Object [] {policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID()},
                FlexTableView.class
                ).getDTO();
        
        return ft==null?null:ft.getDbReference1();
    }
    
    public BigDecimal getComissionALimit() throws Exception {
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select "+
                "  b.* "+
                "  from "+
                "  ff_table b "+
                "  where b.ref3= ? and b.ref4= ? and b.fft_group_id='COMM' "+
                "  order by refn1 desc",
                new Object [] {policy.getStPolicyTypeID(), policy.getStCostCenterCode()},
                FlexTableView.class
                ).getDTO();
        
        return ft==null?null:ft.getDbReference1();
    }
    
    public BigDecimal getComissionCompanyLimit() throws Exception {
        
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref1=? and b.ref4 = ? and b.fft_group_id='COMM'" +
                "      order by refn1 desc",
                new Object [] {policy.getStPolicyTypeID(), policy.getStCostCenterCode(), policy.getEntity().getStRef2()},
                FlexTableView.class
                ).getDTO();
        
        return ft==null?null:ft.getDbReference1();
        
    }
    
    public BigDecimal getKreasiComissionLimit() throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            final PreparedStatement PS = S.setQuery(
                    "   select " +
                    "      comm " +
                    "   from " +
                    "         ins_rates_big " +
                    "         where ref1=? and ref2=? and ref3 =? and refid1=? and period_start<=? and period_end>=? and rate_class='PAKREASI_JUAL'");
            
            int n=1;
            
            final DTOList objects = policy.getObjects();
            
            InsurancePolicyObjectView obj2 = (InsurancePolicyObjectView) objects.get(0);
            
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) obj2;
            
            PS.setString(n++,policy.getStKreasiTypeID());
            PS.setString(n++,policy.getStCostCenterCode());
            PS.setString(n++,obj.getStReference2());
            PS.setString(n++,policy.getStCoinsID());
            
            S.setParam(n++,policy.getDtPolicyDate());
            S.setParam(n++,policy.getDtPolicyDate());
            
            final ResultSet RS = PS.executeQuery();
            
            if (RS.next()) return RS.getBigDecimal(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }
    
    public void btnApprove() throws Exception {
        policy.setExcludeReasMode(isExcludeReasMode());
        policy.setStEffectiveFlag("Y");
        
        if(policy.isStatusSPPA()){
            if(policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)){
                final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass());
                policy.validateTSILimit(transactionLimit, false);
                
                BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);
            
                BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimit());

                boolean overLimit = false;
                BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());

                String errorMessage="";
                if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                    overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Grup "+ policy.getEntity().getStEntityName() +" Terlalu Tinggi";
                }else{
                    overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Melebihi Kewenangan ("+comissionRatio +" > "+comissionLimit+")";
                }

                if (overLimit){
                    policy.setStUnderwritingFinishFlag("N");
                    policy.setStEffectiveFlag("N");
                    getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                    throw new RuntimeException(errorMessage);
                }
            }
        }
        
        if (policy.isStatusPolicy() || policy.isStatusRenewal() || policy.isStatusEndorseRI()) {
            final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass());
            
         /*final boolean enoughLimit = Tools.compare(transactionLimit, BDUtil.mul(policy.getDbInsuredAmount(),policy.getDbCurrencyRate()))>0;
           if (!enoughLimit) throw new RuntimeException("Nilai TSI Melebihi Limit Anda");
          */
            //CEK LIMIT TSI PER OBJEK(RESIKO), BUKAN PER POLIS(TOTAL)
            policy.validateTSILimit(transactionLimit, false);
            
            BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);
            
            BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimit());
            
            boolean overLimit = false;
            BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());
            
            String errorMessage="";
            if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon "+ policy.getEntity().getStEntityName() +" Terlalu Tinggi";
            }else{
                overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon Melebihi Kewenangan Anda ("+comissionRatio +" > "+comissionLimit+")";
            }
            
            if (overLimit){
                policy.setStUnderwritingFinishFlag("N");
                policy.setStEffectiveFlag("N");
                getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                throw new RuntimeException(errorMessage);
            }
            
            //policy.validateZoneLimit();
            
        }
        
        /*
        if(policy.isStatusEndorse()){
            if(policy.getStPolicyTypeID().equalsIgnoreCase("21"))
                isNullPremiCoinsurance();
        }*/
        
        
        if (policy.isStatusClaimDLA()) {
            final BigDecimal transactionLimit = getTransactionLimit("CLAIM", "1");
            
            final boolean enoughLimit = Tools.compare(transactionLimit, policy.getDbClaimAmountApproved())>0;
            
            if (!enoughLimit){
                policy.setStClaimFinishFlag("N");
                policy.setStEffectiveFlag("N");
                getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                throw new RuntimeException("Jumlah Klaim Melebihi Limit Anda");
            }
        }
        
        if(isApprovalByDirectorMode()){
            DTOList policyDocuments = policy.getPolicyDocuments();
                    
            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                
                if(doc.getStInsuranceDocumentTypeID().equalsIgnoreCase("94"))
                    if(doc.getStFilePhysic()==null)
                        throw new RuntimeException("File Lampiran Persetujuan Direksi Harus Dilampirkan");
            }
        }
        
        policy.validate(true);
        
        if (policy.isStatusPolicy()|| policy.isStatusEndorse()
        ||policy.isStatusClaimDLA()||policy.isStatusRenewal()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
            policy.setStPostedFlag("Y");
        
        policy.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        policy.setDtApprovedDate(new Date());
        
        approvalMode = true;
        
        btnSave();
    }
    
    public void btnApproveViaReverse() throws Exception {
        policy.setExcludeReasMode(isExcludeReasMode());
        policy.setStEffectiveFlag("Y");
        
        if (policy.isStatusPolicy()||policy.isStatusRenewal()||policy.isStatusEndorse()||policy.isStatusEndorseRI()) {
            final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass());
            
            final boolean enoughLimit = Tools.compare(transactionLimit, BDUtil.mul(policy.getDbInsuredAmount(),policy.getDbCurrencyRate()))>0;
            
            if (!enoughLimit) throw new RuntimeException("Nilai TSI Melebihi Limit Anda");
            
            BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),2);
            
            BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimit());
            
            boolean overLimit = false;
            BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());
            String errorMessage="";
            if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon Terlalu Tinggi";
            }else{
                overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon Melebihi Kewenangan Anda";
            }
            
            if (overLimit) throw new RuntimeException(errorMessage);
            
            policy.validateZoneLimit();
            
            final boolean blockPremiCoins = Parameter.readBoolean("BLOCKING_COINS_PREMI");
            
            if(blockPremiCoins)
                if(policy.getStPolicyTypeID().equalsIgnoreCase("21"))
                    if(isPremiCoinsuranceMinus())
                        throw new RuntimeException("Premi Koasuransi Lebih Besar Dari Share Askrida!");
        }
        
        if (policy.isStatusClaimDLA()) {
            final BigDecimal transactionLimit = getTransactionLimit("CLAIM", "1");
            
            final boolean enoughLimit = Tools.compare(transactionLimit, policy.getDbClaimAmountApproved())>0;
            
            if (!enoughLimit) throw new RuntimeException("Jumlah Klaim Melebihi Limit Anda");
        }
        
        if(!isExcludeReasMode())
            policy.validate(true);
        
        if (policy.isStatusPolicy()|| policy.isStatusEndorse()
        ||policy.isStatusClaimDLA()||policy.isStatusRenewal()||policy.isStatusEndorseRI())
            policy.setStPostedFlag("Y");
        
        btnSave();
    }
    
    public void btnApproveReins() throws Exception {
        
        policy.validateObjects();
        
        policy.validateTreaty(true);
        
        if(policy.getStReinsuranceApprovedWho()==null||!isEnableSuperEdit())
            policy.setStReinsuranceApprovedWho(UserManager.getInstance().getUser().getStUserID());
        
        getRemoteInsurance().save(policy,policy.getStNextStatus(),approvalMode);
        
        approvalMode = true;
        
        btnApprove();
        
        close();
    }
    
    public void btnReject() throws Exception {
        policy.setStEffectiveFlag("N");
        policy.setStActiveFlag("N");
        
        policy.recalculate();
        
        final boolean noReverse = policy.isStatusClaimDLA();
        
        if (noReverse)
            getRemoteInsurance().save(policy, null,false);
        else
            getRemoteInsurance().saveAndReverse(policy);
        
        close();
        
    }
    
    public void edit(String policyID) throws Exception {
        superEdit(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
        if(!policy.getStPolicyTypeGroupID().equalsIgnoreCase("8") && !policy.isStatusSPPA())
            if (policy.isEffective()) throw new Exception("This document is not editable, because it has already approved");
    }
    
    public void editHistory(String policyID) throws Exception {
        superEditHistory(policyID);
        
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
    }
    
    public void editApprove(String policyID) throws Exception {
        superEditApprove(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
        if (policy.isEffective()) throw new Exception("This document is not editable, because it has already approved");
    }
    
    public void editReins(String policyID) throws Exception {
        superEditReins(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
        if (policy.isEffective()) throw new Exception("This document is not editable, because it has already approved");
    }
    
    public void editPayment(String policyID) throws Exception {
        superEdit(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
        if (!policy.isEffective()) throw new Exception("Polis Belum Disetujui");
    }
    
    public void changePeriodBase() throws Exception{
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
    
    public void onNewInstallment() throws Exception {
        final InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();
        
        inst.markNew();
        
        getInstallment().add(inst);
        
        String n = String.valueOf(getInstallment().size());
        
        policy.setStInstallmentPeriods(n);
        
        policy.reCalculateInstallment();
    }
    
    public void onDeleteInstallment() throws Exception{
        getInstallment().delete(Integer.parseInt(instIndex));
        
        String n = String.valueOf(getInstallment().size());
        
        policy.setStInstallmentPeriods(n);
        
        policy.reCalculateInstallment();
    }
    
    public String getObjDeductIndex() {
        return objDeductIndex;
    }
    
    public String getInstIndex() {
        return instIndex;
    }
    
    public void setInstIndex(String instIndex) {
        this.instIndex = instIndex;
    }
    
    public void setObjDeductIndex(String objDeductIndex) {
        this.objDeductIndex = objDeductIndex;
    }
    
    public boolean isRejectable() {
        return (policy.getStNextStatus()==null);
    }
    
    public void changePeriodFactor() throws Exception{
        policy.calculatePeriods();
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
        editApprove(policyID);
        
        super.setReadOnly(true);
        
        approvalMode = true;
        
    }
    
    public void approvalReins(String policyID) throws Exception {
        editReins(policyID);
        
        super.setReadOnly(true);
        
        //approvalMode = true;
        
        reasMode = true;
    }
    
    public boolean isApprovalMode() {
        return approvalMode;
    }
    
    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }
    
    public boolean isDisabled() {
        return disabled;
    }
    
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public boolean isReverseMode() {
        return reverseMode;
    }
    
    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }
    
    public boolean isApprovedReasMode() {
        return reasApprovedMode;
    }
    
    public void setApprovedReasMode(boolean reasApprovedMode) {
        this.reasApprovedMode = reasApprovedMode;
    }
    
    public boolean isReApprovedMode() {
        return reApprovedMode;
    }
    
    public void setReApprovedMode(boolean reApprovedMode) {
        this.reApprovedMode = reApprovedMode;
    }
    
    public boolean isSavePolicyHistoryMode() {
        return savePolicyHistoryMode;
    }
    
    public void setSavePolicyHistoryMode(boolean savePolicyHistoryMode) {
        this.savePolicyHistoryMode = savePolicyHistoryMode;
    }
    
    public boolean isApprovedViaReverseMode() {
        return approvedViaReverseMode;
    }
    
    public void setApprovedViaReverseMode(boolean approvedViaReverseMode) {
        this.approvedViaReverseMode = approvedViaReverseMode;
    }
    
    public boolean isExcludeReasMode() {
        return excludeReasMode;
    }
    
    public void setExcludeReasMode(boolean excludeReasMode) {
        this.excludeReasMode = excludeReasMode;
    }
    
    public void testGeneratePolicyNo() throws Exception {
        policy.generatePolicyNo();
    }
    
    public void testGenerateEndorseNo() {
        policy.generateEndorseNo();
    }
    
    public void selectClaimObject() throws Exception {
        
        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjects().get(Integer.parseInt(stClaimObject));
        
        policy.setStClaimObjectID(o.getStPolicyObjectID());
        
        policy.setObjIndex(stClaimObject);
        
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
    
    public void addPeriodDesc(){
        policy.setAddPeriodDesc(true);
    }
    
    public void delPeriodDesc(){
        policy.setAddPeriodDesc(false);
    }
    
    public void addLampiran(){
        boolean canUploadFile = false;
        
        switch(Integer.parseInt(policy.getStPolicyTypeID())){
            case 21 : canUploadFile = true; break;
            case 14 : canUploadFile = true; break;
            case 5  : canUploadFile = true; break;
            case 81 : canUploadFile = true; break;
            case 41 : canUploadFile = true; break;
            case 42 : canUploadFile = true; break;
            case 3 : canUploadFile = true; break;
            case 4 : canUploadFile = true; break;
            case 1 : canUploadFile = true; break;
            case 19 : canUploadFile = true; break;
            case 12 : canUploadFile = true; break;
            default : canUploadFile = false;
        }
        
        if(!canUploadFile)
            throw new RuntimeException("Sementara Baru Tersedia Untuk Polis PA Kreasi, CIT, CIS, Cash Management, Kendaraan Bermotor, PA, PAR, Fire, dan EQ");
        
        policy.setLampiran(true);
        
        final DTOList konversiDocuments = policy.getKonversiDocuments();
        
        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
        doc.setStSelectedFlag("Y");
        /*
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21"))
            canUploadFile = true;
         
        if(policy.getStPolicyTypeID().equalsIgnoreCase("14"))
            canUploadFile = true;
         
        if(policy.getStPolicyTypeID().equalsIgnoreCase("5"))
            canUploadFile = true;
         
        if(policy.getStPolicyTypeID().equalsIgnoreCase("81"))
            canUploadFile = true;
         
        if(policy.getStPolicyTypeID().equalsIgnoreCase("41"))
            canUploadFile = true;*/
    }
    
    public void delLampiran(){
        policy.setLampiran(false);
        final DTOList konversiDocuments = policy.getKonversiDocuments();
        
        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
        doc.setStSelectedFlag("N");
    }
    
    public boolean isPremiCoinsuranceMinus(){
        final DTOList coins = policy.getCoinsCoverage();
        BigDecimal premiAskrida = null;
        BigDecimal premiCoinsurance = null;
        boolean minus = false;
        if(coins.size()==2){
            for(int i=0;i<coins.size();i++){
                InsurancePolicyCoinsView coinsurance = (InsurancePolicyCoinsView)coins.get(i);
                
                if(i==0){
                    premiAskrida = coinsurance.getDbPremiAmount();
                }
                
                if(i==1){
                    premiCoinsurance = coinsurance.getDbPremiAmount();
                }
            }
            if(Tools.compare(premiCoinsurance,premiAskrida)>0)
                minus = true;
            else if(Tools.compare(premiAskrida,premiCoinsurance)>0)
                minus = false;
        }
        
        
        
        return minus;
        
    }
    
    public void isNullPremiCoinsurance(){
        final DTOList coins = policy.getCoinsCoverage();
        
        boolean leaderHasPremi = false;
        for(int i=0;i<coins.size();i++){
            InsurancePolicyCoinsView coinsurance = (InsurancePolicyCoinsView)coins.get(i);
            
            if(coinsurance.isHoldingCompany())
                if(!BDUtil.isZeroOrNull(coinsurance.getDbPremiAmount()))
                    leaderHasPremi = true;
            
            if(!coinsurance.isHoldingCompany())
                if(BDUtil.isZeroOrNull(coinsurance.getDbPremiAmount()))
                    if(leaderHasPremi)
                        throw new RuntimeException("Premi Koasuransi Nol, Hubungi Bag. Underwriting Kantor Pusat");
        }
        
    }
    
    public void uploadExcel()throws Exception{
        try {
            //String fileID = getRemoteInsurance().getID();
            
            final DTOList konversiDocuments = policy.getKonversiDocuments();
            
            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
            
            String fileID = doc.getStFilePhysic();
            
            FileView file = FileManager.getInstance().getFile(fileID);
            
            FileInputStream fis = new FileInputStream(file.getStFilePath());
            
            final boolean inward = policy.getCoverSource().isInward();
            
            final String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
            
            if("21".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadPAKreasi(fis, treatyID);
            
            if("14".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadCIT(fis, treatyID);
            
            if("5".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadCIS(fis, treatyID);
            
            if("81".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadPAR(fis, treatyID);
            
            if("41".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadCMCIT(fis);
            
            if("42".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadCMCIS(fis);
            
            if("3".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadMV(fis,treatyID);
            
            if("4".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadPA(fis,treatyID);
            
            if("1".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadFire(fis,treatyID);
            
            if("19".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadEarthquake(fis,treatyID);
            
            if("12".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadBurglary(fis,treatyID);
            
            defineTreaty();
            
        } catch (Exception e) {
            throw new RuntimeException("error upload excel= "+ e);
        }
    }
    
    public void doNewLampiranObject() throws Exception {
        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getClObjectClass().newInstance();
        o.markNew();
        o.setPolicy(policy);
        o.setCoverage(new DTOList());
        
        o.setSuminsureds(new DTOList());
        o.setDeductibles(new DTOList());
        
        getObjects().add(o);
        
        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) o;
        o.markNew();
        obj.markNew();
        setSelectedDefaultObject(obj);
        
    }
    
    public void doAddLampiranSumInsured(String instsipolid,BigDecimal tsi) throws Exception {
        
        //if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");
        
        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
        
        ptsi.setStInsuranceTSIPolTypeID(instsipolid);
        
        ptsi.initializeDefaults();
        
        ptsi.setDbInsuredAmount(tsi);
        
        ptsi.markNew();
        
        selectedDefaultObject.getSuminsureds().add(ptsi);
        
    }
    
    public void doAddLampiranCover(String cvptid,BigDecimal rate) throws Exception{
        
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        
        cv.setDbRate(rate);
        cv.setStEntryRateFlag("Y");
        
        cv.markNew();
        
        selectedDefaultObject.getCoverage().add(cv);
        
    }
    
    public void doAddLampiranCoverHistory(String cvptid,BigDecimal rate,String manualPremiFlag) throws Exception{
        
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        
        cv.setDbRate(rate);
        //cv.setStEntryRateFlag("Y");
        cv.setStEntryPremiFlag(manualPremiFlag);
        cv.setStAutoRateFlag("N");
        cv.setStEntryRateFlag("N");
        
        cv.markNew();
        
        selectedDefaultObject.getCoverage().add(cv);
        
    }
    
    public void doAddLampiranCoverKreasi(String cvptid,BigDecimal rate,BigDecimal premi) throws Exception{
        
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        
        if(rate!=null){
            cv.setDbRate(rate);
            cv.setStEntryRateFlag("Y");
        }
        if(rate==null){
            cv.setStAutoRateFlag("N");
            cv.setStEntryRateFlag("N");
            cv.setStEntryPremiFlag("Y");
            cv.setDbPremiNew(premi);
            cv.setDbPremi(cv.getDbPremiNew());
        }
        
        cv.markNew();
        
        selectedDefaultObject.getCoverage().add(cv);
        
    }
    
    public void doAddLampiranCoverPAR(String cvptid,BigDecimal rate) throws Exception{
        //final DTOList covers = getCoverageAddLOV();
        //for (int i = 0; i < covers.size(); i++) {
        //InsuranceCoverPolTypeView cpt = (InsuranceCoverPolTypeView) covers.get(i);
        
        //if (!Tools.isYes(cpt.getStDefaultCoverFlag())) continue;
        
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        cv.setDbRate(rate);
        
        if(cvpt.getStInsuranceCoverPolTypeID().equalsIgnoreCase("209"))
            cv.setStEntryRateFlag("Y");
        else
            cv.setStEntryRateFlag("N");
        
        cv.markNew();
        
        selectedDefaultObject.getCoverage().add(cv);
        
        //}
    }
    
    private void uploadPAKreasi(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//nama
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//tgl lahir
            HSSFCell cell3  = row.getCell(Short.parseShort("5"));//tgl awal
            HSSFCell cell4  = row.getCell(Short.parseShort("6"));//tgl akhir
            HSSFCell cell5  = row.getCell(Short.parseShort("8"));//tsi
            HSSFCell cell6  = row.getCell(Short.parseShort("10"));//premi
            HSSFCell cell7  = row.getCell(Short.parseShort("9"));//rate
            HSSFCell cell8  = row.getCell(Short.parseShort("3"));//usia
            
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Nama Harus String/Character");
            
            if(cell5.getCellType()!=cell5.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI Harus Numeric");
            
            if(cell6.getCellType()!=cell6.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Premi Harus Numeric");
            
            //if(cell7.getCellType()!=cell7.CELL_TYPE_NUMERIC)
            //throw new RuntimeException("Kolom Rate Harus Numeric");
            
            double a = cell5.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);
            
            doAddLampiranSumInsured("225",b);
            
            double tes3 = cell6.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);
            
            BigDecimal d = null;
            if(cell7!=null){
                double tes4 = cell7.getNumericCellValue();
                d = new BigDecimal(tes4);
            }
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if(cell2!=null) getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            
            if(cell8!=null){
                if(cell8.getCellType()==cell8.CELL_TYPE_NUMERIC)
                    getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell8.getNumericCellValue())));
                else if(cell8.getCellType()==cell8.CELL_TYPE_STRING)
                    getSelectedDefaultObject().setStReference2(cell8.getStringCellValue());
            }
            
            if(cell3.getCellType()==cell3.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
            else getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
            
            if(cell4.getCellType()==cell4.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
            else getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
            
            //getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue()); //akhir
            getSelectedDefaultObject().setDbReference4(b); //tsi
            getSelectedDefaultObject().setDbReference6(c); //premi
            if(cell7!=null) getSelectedDefaultObject().setStReference9("Y");
            if(cell7!=null) getSelectedDefaultObject().setDbReference5(d);
            
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            //add coverkreasi
            HSSFCell cellCoverPA  = row.getCell(Short.parseShort("20"));
            if(cellCoverPA.getCellType()!=cellCoverPA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            HSSFCell cellRatePA  = row.getCell(Short.parseShort("18"));
            if(cellRatePA.getCellType()!=cellRatePA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
            
            BigDecimal ratePA2 = null;
            if(cell7!=null){
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }
            
            HSSFCell cellPremiPA  = row.getCell(Short.parseShort("19"));
            if(cellPremiPA.getCellType()!=cellPremiPA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
            
            BigDecimal premiPA2 = null;
            if(cell7==null){
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }
            
            doAddLampiranCoverKreasi(cvptID3,ratePA2,premiPA2);
            
            //add cover PHK
            HSSFCell cellCoverPHK  = row.getCell(Short.parseShort("25"));
            if(cellCoverPHK!=null){
                //if(cellCoverPHK.getCellType()()!=1){
                if(cellCoverPHK.getCellType()!=cellCoverPHK.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom CvptID Harus Numeric");
                
                double cvptPHK = cellCoverPHK.getNumericCellValue();
                BigDecimal cvptPHK2 = new BigDecimal(cvptPHK);
                String cvptPHK3 = cvptPHK2.toString();
                
                HSSFCell cellRatePHK  = row.getCell(Short.parseShort("23"));
                if(cellRatePHK.getCellType()!=cellRatePHK.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate PHK Harus Numeric");
                
                BigDecimal ratePHK2 = null;
                if(cell7!=null){
                    double ratePHK = cellRatePHK.getNumericCellValue();
                    ratePHK2 = new BigDecimal(ratePHK);
                }
                
                HSSFCell cellPremiPHK  = row.getCell(Short.parseShort("24"));
                if(cellPremiPHK.getCellType()!=cellPremiPHK.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
                
                BigDecimal premiPHK2 = null;
                if(cell7==null){
                    double premiPHK = cellPremiPHK.getNumericCellValue();
                    premiPHK2 = new BigDecimal(premiPHK);
                }
                
                doAddLampiranCoverKreasi(cvptPHK3,ratePHK2,premiPHK2);
                //}
            }
            
            //paw 30
            HSSFCell cellCoverPAW  = row.getCell(Short.parseShort("30"));
            if(cellCoverPAW!=null){
                //if(cellCoverPAW.getCellType()()!=1){
                //if(true)	throw new RuntimeException("PAW= "+ cellCoverPAW.);
                if(cellCoverPAW.getCellType()!=cellCoverPAW.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom CvptID Harus Numeric");
                
                double cvptPAW = cellCoverPAW.getNumericCellValue();
                BigDecimal cvptPAW2 = new BigDecimal(cvptPAW);
                String cvptPAW3 = cvptPAW2.toString();
                
                HSSFCell cellRatePAW  = row.getCell(Short.parseShort("28"));
                if(cellRatePAW.getCellType()!=cellRatePAW.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate PAW Harus Numeric");
                
                BigDecimal ratePAW2 = null;
                if(cell7!=null){
                    double ratePAW = cellRatePAW.getNumericCellValue();
                    ratePAW2 = new BigDecimal(ratePAW);
                }
                
                HSSFCell cellPremiPAW  = row.getCell(Short.parseShort("29"));
                if(cellPremiPAW.getCellType()!=cellPremiPAW.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
                
                BigDecimal premiPAW2 = null;
                if(cell7==null){
                    double premiPAW = cellPremiPAW.getNumericCellValue();
                    premiPAW2 = new BigDecimal(premiPAW);
                }
                
                doAddLampiranCoverKreasi(cvptPAW3,ratePAW2,premiPAW2);
                //}
            }
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }
        
        
    }
    
    private void uploadCIT(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");
        
        if(policy.getDtPeriodStart()==null) throw new RuntimeException("Periode Awal Harus Diisi");
        
        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);
            
            //HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            //if(cellControl==null) break;
            
            doNewLampiranObject();
            
            
            
            HSSFCell cell1  = row.getCell(Short.parseShort("0"));//transit dari (String)
            HSSFCell cell2  = row.getCell(Short.parseShort("1"));//transit ke (String)
            HSSFCell cell3  = row.getCell(Short.parseShort("2"));//tgl transit (date)
            HSSFCell cell4  = row.getCell(Short.parseShort("3"));//waktu transit (String)
            HSSFCell cell5  = row.getCell(Short.parseShort("4"));//no deklarasi (String)
            HSSFCell cell6  = row.getCell(Short.parseShort("5"));//diangkut dengan	(String)
            
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            
            if(cell2.getCellType()!=cell2.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom TSI Harus String/Character");
            
            if(cell4.getCellType()!=cell4.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Premi Harus String/Character");
            
            if(cell5.getCellType()!=cell5.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            if(cell6.getCellType()!=cell6.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setDtReference1(cell3.getDateCellValue()); //tgl lahir
            getSelectedDefaultObject().setStReference3(cell4.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell5.getStringCellValue());
            getSelectedDefaultObject().setStReference5(cell6.getStringCellValue());
            //getSelectedDefaultObject().setStRiskCategoryID("537");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            
            //add coverkreasi
            HSSFCell cellCoverCIT  = row.getCell(Short.parseShort("11"));//cvptid
            if(cellCoverCIT.getCellType()!=cellCoverCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            //get sum insured
            HSSFCell cellTSICIT  = row.getCell(Short.parseShort("8"));//get rate cover
            if(cellTSICIT.getCellType()!=cellTSICIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            
            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            //
            
            HSSFCell cellRateCIT  = row.getCell(Short.parseShort("9"));//get rate cover
            if(cellRateCIT!=null)
                if(cellRateCIT.getCellType()!=cellRateCIT.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate CIT Harus Numeric");
            
            BigDecimal ratePA2 = null;
            if(cellRateCIT!=null){
                double ratePA = cellRateCIT.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }
            
            //premi
            HSSFCell cellPremiCIT  = row.getCell(Short.parseShort("10"));//get rate cover
            if(cellPremiCIT.getCellType()!=cellPremiCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Premi CIT Harus Numeric");
            
            double PremiPA = cellPremiCIT.getNumericCellValue();
            BigDecimal PremiPA2 = new BigDecimal(PremiPA);
            
            doAddLampiranSumInsured("182",tsiCIT2);
            doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
    }
    
    private void uploadCIS(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");
        
        if(policy.getDtPeriodStart()==null) throw new RuntimeException("Periode Awal Harus Diisi");
        
        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);
            
            //HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            //if(cellControl==null) break;
            
            doNewLampiranObject();
            
            
                                /*
                                 *  BISNIS
                                        BUATAN
                                        ALAMAT RESIKO
                                        KONDISI/SITUASI
                                        TANGGAL
                                 */
            
            HSSFCell cell1  = row.getCell(Short.parseShort("0"));//BISNIS (String)
            HSSFCell cell2  = row.getCell(Short.parseShort("1"));//BUATAN (String)
            HSSFCell cell3  = row.getCell(Short.parseShort("2"));//ALAMAT RESIKO (String)
            HSSFCell cell4  = row.getCell(Short.parseShort("3"));//KONDISI/SITUASI (String)
            HSSFCell cell5  = row.getCell(Short.parseShort("4"));//TANGGAL (date)
            
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            
            if(cell2.getCellType()!=cell2.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom TSI Harus String/Character");
            
            if(cell3.getCellType()!=cell3.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Premi Harus String/Character");
            
            if(cell4.getCellType()!=cell4.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell4.getStringCellValue());
            
            if(cell5!=null)
                getSelectedDefaultObject().setDtReference1(cell5.getDateCellValue());
            
            //getSelectedDefaultObject().setStRiskCategoryID("536");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            HSSFCell cellCoverCIT  = row.getCell(Short.parseShort("10"));//cvptid
            if(cellCoverCIT.getCellType()!=cellCoverCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            
            HSSFCell cellTSICIT  = row.getCell(Short.parseShort("7"));//get rate cover
            if(cellTSICIT.getCellType()!=cellTSICIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            
            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            
            
            HSSFCell cellRateCIT  = row.getCell(Short.parseShort("8"));//get rate
            if(cellRateCIT!=null)
                if(cellRateCIT.getCellType()!=cellRateCIT.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom Rate CIT Harus Numeric");
            
            BigDecimal ratePA2 = null;
            
            if(cellRateCIT!=null){
                double ratePA = cellRateCIT.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }
            
            HSSFCell cellPremiCIT  = row.getCell(Short.parseShort("9"));//get premi
            if(cellPremiCIT.getCellType()!=cellPremiCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Premi CIT Harus Numeric");
            
            double PremiPA = cellPremiCIT.getNumericCellValue();
            BigDecimal PremiPA2 = new BigDecimal(PremiPA);
            
            doAddLampiranSumInsured("42",tsiCIT2);
            doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
    }
    
    private void uploadCMCIT(FileInputStream fis)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");
        
        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);
            
            //HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            //if(cellControl==null) break;
            
            doNewLampiranObject();
            
            
            
            HSSFCell cell1  = row.getCell(Short.parseShort("0"));//transit dari (String)
            HSSFCell cell2  = row.getCell(Short.parseShort("1"));//transit ke (String)
            HSSFCell cell3  = row.getCell(Short.parseShort("2"));//tgl transit (date)
            HSSFCell cell4  = row.getCell(Short.parseShort("3"));//waktu transit (String)
            HSSFCell cell5  = row.getCell(Short.parseShort("4"));//no deklarasi (String)
            HSSFCell cell6  = row.getCell(Short.parseShort("5"));//diangkut dengan	(String)
            
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            
            if(cell2.getCellType()!=cell2.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom TSI Harus String/Character");
            
            if(cell4.getCellType()!=cell4.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Premi Harus String/Character");
            
            if(cell5.getCellType()!=cell5.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            if(cell6.getCellType()!=cell6.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setDtReference1(cell3.getDateCellValue()); //tgl lahir
            getSelectedDefaultObject().setStReference3(cell4.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell5.getStringCellValue());
            getSelectedDefaultObject().setStReference5(cell6.getStringCellValue());
            //getSelectedDefaultObject().setStRiskCategoryID("537");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            
            //add coverkreasi
            HSSFCell cellCoverCIT  = row.getCell(Short.parseShort("11"));//cvptid
            if(cellCoverCIT.getCellType()!=cellCoverCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            //get sum insured
            HSSFCell cellTSICIT  = row.getCell(Short.parseShort("8"));//get rate cover
            if(cellTSICIT.getCellType()!=cellTSICIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            
            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            //
            
            HSSFCell cellRateCIT  = row.getCell(Short.parseShort("9"));//get rate cover
            if(cellRateCIT.getCellType()!=cellRateCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate CIT Harus Numeric");
            
            double ratePA = cellRateCIT.getNumericCellValue();
            BigDecimal ratePA2 = new BigDecimal(ratePA);
            
            doAddLampiranSumInsured("475",tsiCIT2);
            doAddLampiranCover(cvptID3,ratePA2);
            
        }
    }
    
    public InsuranceItemsView getInsItemCat(String stInsItemID) {
        return (InsuranceItemsView) DTOPool.getInstance().getDTO(InsuranceItemsView.class, stInsItemID);
    }
    
    public void clickGenerateDLANo() throws Exception{
        final String dla = policy.generateDLANo2();
        throw new RuntimeException(policy.generateDLANo2());
    }
    
    public void refresh(){
        setDisabled(false);
    }
    
    public void refresh2(){
        setDisabled(true);
    }
    
    public String getParamriskcat() {
        return paramriskcat;
    }
    
    public void setParamriskcat(String paramriskcat) {
        this.paramriskcat = paramriskcat;
    }
    
    public void calcDays(){
        
        if(policy.getStDaysLength()!=null&&policy.getStUnits()!=null){
            DateTime startDate = new DateTime(policy.getDtPeriodStart());
            DateTime endDate = new DateTime();
            
            if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.DAY))
                endDate = startDate.plusDays(Integer.parseInt(policy.getStDaysLength())-1);
            else if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.MONTH))
                endDate = startDate.plusMonths(Integer.parseInt(policy.getStDaysLength()));
            else if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.YEAR))
                endDate = startDate.plusYears(Integer.parseInt(policy.getStDaysLength()));
            
            policy.setDtPeriodEnd(endDate.toDate());
        }
        
    }
    
    public void downloadHistory()throws Exception{
        
        policy.checkHistoryPolicyNo();
        
        final DTOList abaKreasi = policy.getABAKreasi();
        
        policy.setStPolicyNo(policy.convertOldPolicyNoToNewFormat(policy.getStPolicyNo()));
        
        if(abaKreasi.size()<1) throw new RuntimeException("Data Polis Tidak Ditemukan");
        
        String stRiskCategoryID = null;
        
        for(int i=0; i < abaKreasi.size();i++){
            InsuranceKreasiView kreasi = (InsuranceKreasiView) abaKreasi.get(i);
            
            doNewLampiranObject();
            doAddLampiranSumInsured("225",kreasi.getDbInsured());
            
            /*
            if(policy.getStCostCenterCode().equalsIgnoreCase("21"))
             
            else
                getSelectedDefaultObject().setStReference1(kreasi.getStInsuranceNoUrut() + ". "+ kreasi.getStInsuranceNama()); //nama
             */
            getSelectedDefaultObject().setStReference1(kreasi.getStInsuranceNama()); //nama
            getSelectedDefaultObject().setDtReference1(kreasi.getDtTanggalLahir()); //tgl lahir
            getSelectedDefaultObject().setStReference2(kreasi.getStInsuranceUmur());
            getSelectedDefaultObject().setDtReference2(kreasi.getDtTanggalCair()); //awal
            getSelectedDefaultObject().setDtReference3(kreasi.getDtTanggalAkhir()); //awal
            getSelectedDefaultObject().setDbReference4(kreasi.getDbInsured()); //tsi
            getSelectedDefaultObject().setDbReference6(kreasi.getDbPremi()); //premi
            //getSelectedDefaultObject().setStReference9("Y");
            getSelectedDefaultObject().setDbReference5(kreasi.getDbRatePremi());
            
            if(i==0){
                policy.setDtPolicyDate(kreasi.getDtTanggalCair());
                policy.setDtPeriodStart(kreasi.getDtTanggalCair());
                stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
            }
            
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            if(i==(abaKreasi.size()-1)) policy.setDtPeriodEnd(kreasi.getDtTanggalAkhir());
            
            String manualPremiFlag = "N";
            
            doAddLampiranCoverHistory("148",kreasi.getDbRatePremi(),manualPremiFlag);
        }
        
        defaultPeriod();
        defineTreaty();
        
    }
    
    public void changeParentPolicy()throws Exception{
        final InsurancePolicyParentView parent = (InsurancePolicyParentView) DTOPool.getInstance().getDTO(InsurancePolicyParentView.class, policy.getStReference2());
        
        if (parent==null) throw new RuntimeException("Polis Induk Tidak Ditemukan");
        
        policy.setStEntityID(parent.getStEntityID());
        policy.setStCustomerName(parent.getStCustomerName());
        policy.setStCustomerAddress(parent.getStCustomerAddress());
        policy.setStProducerID(parent.getStProducerID());
        policy.setStProducerName(parent.getStProducerName());
        policy.setStProducerAddress(parent.getStProducerAddress());
    }
    
    public void onNewDetailAutomatic(String insItemID, String stReadOnly) throws Exception {
        
        if (insItemID == null) throw new Exception("Please select item to be added");
        
        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();
        
        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);
        
        item.markNew();
        
        item.setStInsItemID(insItemID);
        
        item.setStROFlag(stReadOnly);
        
        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());
        
        if(item.isKomisi()){
            item.setStFlagEntryByRate("Y");
            item.setDbRate(getComissionALimit());
            if(getComissionALimit()==null) return;
        }
        
        getDetails().add(item);
        
    }
    
    public void addBiayaPolisMateraiAutomatic()throws Exception{
        final String coverType = policy.getStCoverTypeCode();
        
        if(policy.isStatusHistory()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()) return;
        
        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic("15","N");
            onNewDetailAutomatic("14","N");
        }
        
        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic("22","N");
            onNewDetailAutomatic("21","N");
        }
        
        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic("29","N");
            onNewDetailAutomatic("28","N");
        }
        
    }
    
    public void doDeleteAllObjects() throws Exception{
        getObjects().deleteAll();
    }
    
    private void addKomisiAutomatic() throws Exception{
        final String coverType = policy.getStCoverTypeCode();
        
        if(policy.isStatusHistory()) return;
        
        /*
        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomaticKomisi("57","Y");
        }
         
        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomaticKomisi("61","Y");
        }
         
        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomaticKomisi("65","Y");
        }*/
        
        if(coverType.equalsIgnoreCase("COINSOUTSELF")){
            onNewDetailAutomaticKomisi("32","Y");
        }
    }
    
    public void onNewDetailAutomaticKomisi(String insItemID, String stReadOnly) throws Exception {
        
        if (insItemID == null) throw new Exception("Please select item to be added");
        
        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();
        
        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);
        
        item.markNew();
        
        item.setStInsItemID(insItemID);
        
        //item.setStROFlag(stReadOnly);
        
        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());
        
        if(item.isKomisi()){
            //item.setStFlagEntryByRate("Y");
            //item.setDbRate(getComissionALimit());
            //item.setStEntityID(Parameter.readString("COMMISSION_A_ENTITY"));
            //item.setStTaxCode("1");
            //if(getComissionALimit()==null) return;
        }
        
        getDetails().add(item);
        
    }
    
    public void reaproveByExcel()throws Exception{
        
        final String fileID = getRemoteInsurance().getID();
        
        FileView file = FileManager.getInstance().getFile(fileID);
        
        FileInputStream fis = new FileInputStream(file.getStFilePath());
        
        reapproveWithExcel(fis);
        
    }
    
    private void reapproveWithExcel(FileInputStream fis)throws Exception{
        try{
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("sheet");
            
            if(sheet==null)
                throw new RuntimeException("Sheet sheet Tidak Ditemukan !");
            
            int rows  = sheet.getPhysicalNumberOfRows();
            
            for (int r = 0; r < rows; r++){
                HSSFRow row   = sheet.getRow(r);
                
                HSSFCell cellPolId  = row.getCell(Short.parseShort("0"));//pol_id
                
                if(cellPolId.getCellType()!=cellPolId.CELL_TYPE_STRING){
                    double polidDouble = cellPolId.getNumericCellValue();
                    BigDecimal polidBD = new BigDecimal(polidDouble);
                    String polid = polidBD.toString();
                    clickReApprovalByPolicyID(polid);
                }else if(cellPolId.getCellType()!=cellPolId.CELL_TYPE_NUMERIC){
                    clickReApprovalByPolicyID(cellPolId.getStringCellValue());
                }
            }
        }catch (Exception e) {
            throw new RuntimeException("error reapprove using excel = "+ e);
        }
    }
    
    public void clickReApprovalByPolicyID(String pol_id) throws Exception {
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol_id);
        
        //reverse dulu
        final InsurancePolicyView polis = (InsurancePolicyView) pol;
        getRemoteInsurance().reverse(pol);
        
        //ubah treaty n simpan
        
        final boolean inward = polis.getCoverSource().isInward();
        
        final DTOList objects = polis.getObjects();
        
        for (int j = 0; j < objects.size(); j++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
            
            obj.getTreaties().markAllUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int k = 0; k < treatyDetails.size(); k++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);
                
                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
            }
            
            obj.setStInsuranceTreatyID(null);
            obj.getTreaties().deleteAll();
            
            if(polis.getDtPeriodStart()!=null)
                obj.setStInsuranceTreatyID(polis.getInsuranceTreatyID(polis.getDtPeriodStart()));
            
            if(polis.isStatusPolicy()||polis.isStatusClaim()||polis.isStatusEndorse()||polis.isStatusRenewal()||polis.isStatusHistory()||polis.isStatusEndorseRI())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
            
        }
        
        polis.getCoins2().markAllUpdate();
        polis.getDetails().markAllUpdate();
        polis.getClaimItems().markAllUpdate();
        polis.getDeductibles().markAllUpdate();
        polis.getInstallment().markAllUpdate();
        
        polis.recalculate();
        polis.recalculateTreatyReverse();
        
        getRemoteInsurance().save(polis, polis.getStNextStatus(), false);
        
        final InsurancePolicyView pol2 = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol_id);
        //simpan sekaligus approval
        pol2.markUpdate();
        pol2.setStPostedFlag("Y");
        pol2.setStEffectiveFlag("Y");
        getRemoteInsurance().approveAfterReverse(pol2, pol2.getStNextStatus(), true);
        
    }
    
    private void uploadCMCIS(FileInputStream fis)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");
        
        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);
            
            doNewLampiranObject();
            
            
                                /*
                                 *  BISNIS
                                        BUATAN
                                        ALAMAT RESIKO
                                        KONDISI/SITUASI
                                        TANGGAL
                                 */
            
            HSSFCell cell1  = row.getCell(Short.parseShort("0"));//BISNIS (String)
            HSSFCell cell2  = row.getCell(Short.parseShort("1"));//BUATAN (String)
            HSSFCell cell3  = row.getCell(Short.parseShort("2"));//ALAMAT RESIKO (String)
            HSSFCell cell4  = row.getCell(Short.parseShort("3"));//KONDISI/SITUASI (String)
            HSSFCell cell5  = row.getCell(Short.parseShort("4"));//TANGGAL (date)
            
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            
            if(cell2.getCellType()!=cell2.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom TSI Harus String/Character");
            
            if(cell3.getCellType()!=cell3.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Premi Harus String/Character");
            
            if(cell4.getCellType()!=cell4.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom Rate Harus String/Character");
            
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell4.getStringCellValue());
            
            if(cell5!=null)
                getSelectedDefaultObject().setDtReference1(cell5.getDateCellValue());
            
            //getSelectedDefaultObject().setStRiskCategoryID("536");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            HSSFCell cellCoverCIT  = row.getCell(Short.parseShort("10"));//cvptid
            if(cellCoverCIT.getCellType()!=cellCoverCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            
            HSSFCell cellTSICIT  = row.getCell(Short.parseShort("7"));//get rate cover
            if(cellTSICIT.getCellType()!=cellTSICIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            
            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            
            
            HSSFCell cellRateCIT  = row.getCell(Short.parseShort("8"));//get rate cover
            if(cellRateCIT.getCellType()!=cellRateCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate CIT Harus Numeric");
            
            double ratePA = cellRateCIT.getNumericCellValue();
            BigDecimal ratePA2 = new BigDecimal(ratePA);
            
            doAddLampiranSumInsured("476",tsiCIT2);
            doAddLampiranCover(cvptID3,ratePA2);
            
            
        }
    }
    
    private boolean inputPaymentDateMode;
    
    public boolean isInputPaymentDateMode() {
        return inputPaymentDateMode;
    }
    
    public void setInputPaymentDateMode(boolean inputPaymentDateMode) {
        this.inputPaymentDateMode = inputPaymentDateMode;
    }
    
    public void validateHistoryPolicy() throws Exception{
        policy.validateHistoryPolicy();
    }
    
    private String coinsCoverIndex;
    
    public String getCoinsCoverIndex() {
        return coinsCoverIndex;
    }
    
    public void setCoinsCoverIndex(String coinsCoverIndex) {
        this.coinsCoverIndex = coinsCoverIndex;
    }
    
    public DTOList getCoinsCoverage() throws Exception {
        return policy.getCoinsCoverage();
    }
    
    private String coverReinsIndex;
    
    public String getCoverReinsIndex() {
        return coverReinsIndex;
    }
    
    public void setCoverReinsIndex(String coverReinsIndex) {
        this.coverReinsIndex = coverReinsIndex;
    }
    
    public DTOList getCoverageReinsurance() throws Exception {
        return policy.getCoverageReinsurance();
    }
    
    private void addCoverageReinsurance() throws Exception {
        
        if(!policy.getStPolicyTypeID().equalsIgnoreCase("21")) return;
        
        final InsurancePolicyCoverReinsView coreins = new InsurancePolicyCoverReinsView();
        
        coreins.markNew();
        
        coreins.setStMemberEntityID("215");
        coreins.setDbSharePct(new BigDecimal(7.5));
        coreins.setStInsuranceCoverID("78");
        coreins.setStInsuranceCoverPolTypeID("148");
        coreins.setDbCommissionRate(new BigDecimal(5));
        
        getCoverageReinsurance().add(coreins);
        
        final InsurancePolicyCoverReinsView coreins2 = new InsurancePolicyCoverReinsView();
        
        coreins2.markNew();
        
        coreins2.setStMemberEntityID("215");
        coreins2.setDbSharePct(new BigDecimal(45));
        coreins2.setStInsuranceCoverID("124");
        coreins2.setStInsuranceCoverPolTypeID("242");
        coreins.setDbCommissionRate(new BigDecimal(5));
        
        getCoverageReinsurance().add(coreins2);
    }
    
    public void createNewSPPA() throws Exception {
        policy = new InsurancePolicyView();
        
        policy.setDetails(new DTOList());
        policy.setObjects(new DTOList());
        policy.setCovers(new DTOList());
        policy.setSuminsureds(new DTOList());
        policy.setCoins(new DTOList());
        policy.setDeductibles(new DTOList());
        policy.setInstallment(new DTOList());
        policy.setStInstallmentPeriods("1");
        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());
        
        policy.markNew();
        
        policy.setStStatus(FinCodec.PolicyStatus.SPPA);
        //policy.setStNextStatus(FinCodec.PolicyStatus.SPPA);
        policy.setStActiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        
        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbCurrencyRateTreaty(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));
        
        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
        onNewInstallment();
        
        policy.markNew();
        
        initTabs();
    }
    
    public void editCreateProposalFromPolicy(String policyID) throws Exception {
        
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
        if (!(FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus()))) {
            throw new RuntimeException("Pemindahan Data ke proposal hanya bisa dari Policy / Endorsement");
        }
        
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStPolicyNo(null);
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setDtPolicyDate(new Date());
        policy.setDtPeriodStart(policy.getDtPeriodStart());
        policy.setDtPeriodEnd(policy.getDtPeriodEnd());
        policy.setStNextStatus(FinCodec.PolicyStatus.DRAFT);
        policy.setStCoverNoteFlag("N");
        defineTreaty();
        initTabs();
    }
    
    private void uploadPA(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
                No Polisi
                Merek/Jenis
                Tahun Pembuatan
                No Rangka
                No Mesin
                Tempat Duduk
                Digunakan
                Tipe
                Nama
                ins_risk_cat_id
             **/
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//no polisi
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            HSSFCell cell10  = row.getCell(Short.parseShort("10"));//Kode Resiko
            HSSFCell cell11  = row.getCell(Short.parseShort("11"));//Kode Resiko
            HSSFCell cell12  = row.getCell(Short.parseShort("12"));//Kode Resiko
            HSSFCell cell13  = row.getCell(Short.parseShort("13"));//Kode Resiko
            HSSFCell cell14  = row.getCell(Short.parseShort("14"));//Kode Resiko
            HSSFCell cell15  = row.getCell(Short.parseShort("15"));//Kode Resiko
            HSSFCell cell16  = row.getCell(Short.parseShort("16"));//Kode Resiko
            HSSFCell cell17  = row.getCell(Short.parseShort("17"));//Kode Resiko
            
            /*
            ins_tcpt_id
            ins_tsi_cat_id
            TSI
             *
            ins_tcpt_id
            ins_tsi_cat_id
            TSI
             *
            ins_cover_id
            insured_amount
            rate
            premi
            ins_cvpt_id
             *
            ins_cover_id
            insured_amount
            rate
            premi
            ins_cvpt_id
             */
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStReference2(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            
            //getSelectedDefaultObject().setStReference6(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            if(cell6!=null)
                getSelectedDefaultObject().setDtReference1(cell6.getDateCellValue());
            
            getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            
            if(cell8!=null) getSelectedDefaultObject().setDtReference2(cell8.getDateCellValue());
            if(cell9!=null) getSelectedDefaultObject().setDtReference3(cell9.getDateCellValue());
            
            
            getSelectedDefaultObject().setStReference10(cell10.getCellType()==cell10.CELL_TYPE_STRING?cell10.getStringCellValue():new BigDecimal(cell10.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell11.getCellType()==cell11.CELL_TYPE_STRING?cell11.getStringCellValue():new BigDecimal(cell11.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference12(cell12.getCellType()==cell12.CELL_TYPE_STRING?cell12.getStringCellValue():new BigDecimal(cell12.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference13(cell13.getCellType()==cell13.CELL_TYPE_STRING?cell13.getStringCellValue():new BigDecimal(cell13.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference14(cell14.getCellType()==cell14.CELL_TYPE_STRING?cell14.getStringCellValue():new BigDecimal(cell14.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference15(cell15.getCellType()==cell15.CELL_TYPE_STRING?cell15.getStringCellValue():new BigDecimal(cell15.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference16(cell16.getCellType()==cell16.CELL_TYPE_STRING?cell16.getStringCellValue():new BigDecimal(cell16.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setStRiskCategoryID(cell17.getCellType()==cell17.CELL_TYPE_STRING?cell17.getStringCellValue():new BigDecimal(cell17.getNumericCellValue()).toString());
            
            
            //tsi kendaraan
            HSSFCell cell18  = row.getCell(Short.parseShort("18"));//ins_tcpt_id
            HSSFCell cell19  = row.getCell(Short.parseShort("19"));//ins_tsi_cat_id
            HSSFCell cell20  = row.getCell(Short.parseShort("20"));//tsi
            
            if(cell18!=null)
                doAddLampiranSumInsured(cell18.getCellType()==cell18.CELL_TYPE_STRING?cell18.getStringCellValue():new BigDecimal(cell18.getNumericCellValue()).toString(), new BigDecimal(cell20.getNumericCellValue()));
            
            //cover meninggal
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("25"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("21"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("22"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("23"));//tsi
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("24"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            final BigDecimal tsi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI1.getNumericCellValue()))?null:new BigDecimal(cell_TSI1.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, tsi1);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, tsi1);
            }
            
            //cover cacat tetap
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("30"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("26"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("27"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("28"));//tsi
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("29"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            final BigDecimal tsi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI2.getNumericCellValue()))?null:new BigDecimal(cell_TSI2.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, tsi2);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, tsi2);
            }
            
            //cover sementara
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(Short.parseShort("35"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(Short.parseShort("31"));//RATE
            HSSFCell cell_Rate3  = row.getCell(Short.parseShort("32"));//premi
            HSSFCell cell_Premi3  = row.getCell(Short.parseShort("33"));//tsi
            HSSFCell cell_TSI3  = row.getCell(Short.parseShort("34"));//ins_cvpt_id
            
            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());
            final BigDecimal tsi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI3.getNumericCellValue()))?null:new BigDecimal(cell_TSI3.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, tsi3);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, tsi3);
            }
            
            //cover ongkos dokter
            HSSFCell cell_Ins_Cvpt_ID4  = row.getCell(Short.parseShort("40"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4  = row.getCell(Short.parseShort("36"));//RATE
            HSSFCell cell_Rate4  = row.getCell(Short.parseShort("37"));//premi
            HSSFCell cell_Premi4  = row.getCell(Short.parseShort("38"));//tsi
            HSSFCell cell_TSI4  = row.getCell(Short.parseShort("39"));//ins_cvpt_id
            
            final BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue()))?null:new BigDecimal(cell_Rate4.getNumericCellValue());
            final BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue()))?null:new BigDecimal(cell_Premi4.getNumericCellValue());
            final BigDecimal tsi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI4.getNumericCellValue()))?null:new BigDecimal(cell_TSI4.getNumericCellValue());
            
            
            if(cell_Ins_Cvpt_ID4.getCellType()==cell_Ins_Cvpt_ID4.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, tsi4);
            }else if(cell_Ins_Cvpt_ID4.getCellType()==cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, tsi4);
            }
            
            //cover santunan
            HSSFCell cell_Ins_Cvpt_ID5  = row.getCell(Short.parseShort("45"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID5  = row.getCell(Short.parseShort("41"));//RATE
            HSSFCell cell_Rate5  = row.getCell(Short.parseShort("42"));//premi
            HSSFCell cell_Premi5  = row.getCell(Short.parseShort("43"));//tsi
            HSSFCell cell_TSI5  = row.getCell(Short.parseShort("44"));//ins_cvpt_id
            
            final BigDecimal rate5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate5.getNumericCellValue()))?null:new BigDecimal(cell_Rate5.getNumericCellValue());
            final BigDecimal premi5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi5.getNumericCellValue()))?null:new BigDecimal(cell_Premi5.getNumericCellValue());
            final BigDecimal tsi5 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI5.getNumericCellValue()))?null:new BigDecimal(cell_TSI5.getNumericCellValue());
            
            
            if(cell_Ins_Cvpt_ID5.getCellType()==cell_Ins_Cvpt_ID5.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID5.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID5.getStringCellValue(), rate5, premi5, tsi5);
            }else if(cell_Ins_Cvpt_ID5.getCellType()==cell_Ins_Cvpt_ID5.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID5.getNumericCellValue()).toString(), rate5, premi5, tsi5);
            }
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
    }
    
    public void doAddLampiranCoverPA(String cvptid,BigDecimal rate,BigDecimal premi, BigDecimal tsi) throws Exception{
        
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        
        if(tsi!=null){
            cv.setStEntryInsuredAmountFlag("Y");
            cv.setDbInsuredAmount(tsi);
        }
        
        if(rate!=null){
            cv.setDbRate(rate);
            cv.setStEntryRateFlag("Y");
        }
        if(rate==null&&premi!=null){
            cv.setStAutoRateFlag("N");
            cv.setStEntryRateFlag("N");
            cv.setStEntryPremiFlag("Y");
            cv.setDbPremiNew(premi);
            cv.setDbPremi(cv.getDbPremiNew());
        }
        
        cv.markNew();
        
        selectedDefaultObject.getCoverage().add(cv);
        
    }
    
    private void uploadPAR(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
Occupation-L}{L-INA Penggunaan-L}
Class Construction-L}{L-INA Kelas Konstruksi-L}
Risk Code-L}{L-INA Kode Risiko-L}
Electricity-L}{L-INA Penerangan-L}
Risk Location-L}{L-INA Alamat Risiko-L}
Post Code-L}{L-INA Kode Pos-L}
Accumulation Code-L}{L-INA Kode Akumulasi-L}
Name-L}{L-INA Nama-L}
             
             **/
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//no polisi
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            
            String kodePos = policy.getStKodePos(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference9(kodePos);
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference11(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());
            
            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */
            
            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(Short.parseShort("10"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(Short.parseShort("11"));//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("12"));//tsi
            
            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }
            
            
            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(Short.parseShort("13"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(Short.parseShort("14"));//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("15"));//tsi
            
            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }
            
            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(Short.parseShort("16"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(Short.parseShort("17"));//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(Short.parseShort("18"));//tsi
            
            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }
            
            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */
            
            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("19"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("20"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("21"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("22"));//tsi
            //HSSFCell cell_TSI1  = row.getCell(Short.parseShort("23"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }
            
            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("24"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("25"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("26"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("27"));//tsi
            //HSSFCell cell_TSI2  = row.getCell(Short.parseShort("28"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }
            
            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(Short.parseShort("29"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(Short.parseShort("30"));//RATE
            HSSFCell cell_Rate3 = row.getCell(Short.parseShort("31"));//premi
            HSSFCell cell_Premi3  = row.getCell(Short.parseShort("32"));//tsi
            //HSSFCell cell_TSI3  = row.getCell(Short.parseShort("33"));//ins_cvpt_id
            
            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
        
        
    }
    
    private void uploadFire(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
Occupation-L}{L-INA Penggunaan-L}
Class Construction-L}{L-INA Kelas Konstruksi-L}
Risk Code-L}{L-INA Kode Risiko-L}
Electricity-L}{L-INA Penerangan-L}
Risk Location-L}{L-INA Alamat Risiko-L}
Post Code-L}{L-INA Kode Pos-L}
Accumulation Code-L}{L-INA Kode Akumulasi-L}
Name-L}{L-INA Nama-L}
             
             **/
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//no polisi
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            
            HSSFCell cellPeriodStart  = row.getCell(Short.parseShort("34"));//Kode Resiko
            HSSFCell cellPeriodEnd = row.getCell(Short.parseShort("35"));//Kode Resiko
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            
            String kodePos = policy.getStKodePos(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference9(kodePos);
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());
            
            if(cellPeriodStart!=null){
                if(cellPeriodStart.getCellType()==cellPeriodStart.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
            }
            
            if(cellPeriodEnd!=null){
                if(cellPeriodEnd.getCellType()==cellPeriodEnd.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
            }
            
            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */
            
            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(Short.parseShort("10"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(Short.parseShort("11"));//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("12"));//tsi
            
            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }
            
            
            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(Short.parseShort("13"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(Short.parseShort("14"));//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("15"));//tsi
            
            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }
            
            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(Short.parseShort("16"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(Short.parseShort("17"));//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(Short.parseShort("18"));//tsi
            
            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }
            
            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */
            
            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("19"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("20"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("21"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("22"));//tsi
            //HSSFCell cell_TSI1  = row.getCell(Short.parseShort("23"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }
            
            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("24"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("25"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("26"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("27"));//tsi
            //HSSFCell cell_TSI2  = row.getCell(Short.parseShort("28"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }
            
            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(Short.parseShort("29"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(Short.parseShort("30"));//RATE
            HSSFCell cell_Rate3 = row.getCell(Short.parseShort("31"));//premi
            HSSFCell cell_Premi3  = row.getCell(Short.parseShort("32"));//tsi
            //HSSFCell cell_TSI3  = row.getCell(Short.parseShort("33"));//ins_cvpt_id
            
            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
        
        
    }
    
    private void createEndorseKreasiWithExcel(FileInputStream fis)throws Exception{
        try{
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("sheet");
            
            if(sheet==null)
                throw new RuntimeException("Sheet sheet Tidak Ditemukan !");
            
            int rows  = sheet.getPhysicalNumberOfRows();
            
            for (int r = 0; r < rows; r++){
                HSSFRow row   = sheet.getRow(r);
                
                HSSFCell cellPolId  = row.getCell(Short.parseShort("0"));//pol_id
                //HSSFCell cellInsPolObjID  = row.getCell(Short.parseShort("1"));//ins_pol_obj_id
                //HSSFCell cellPremiKoas  = row.getCell(Short.parseShort("2"));//premi_koas
                //HSSFCell cellKomisiKoas  = row.getCell(Short.parseShort("3"));//komisi_koas
                
                editCreateEndorse2(cellPolId.getCellType()==cellPolId.CELL_TYPE_STRING?cellPolId.getStringCellValue():new BigDecimal(cellPolId.getNumericCellValue()).toString());
                
            }
        }catch (Exception e) {
            throw new RuntimeException("error reapprove using excel = "+ e);
        }
    }
    
    public void editCreateEndorse2(String policyID) throws Exception {
        superEdit(policyID);
        
        //checkActiveEffective();
        
        //if (!policy.isEffective())
        // throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStEndorseNotes("FIXCOASNULL");
        
        //policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            final InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
            
            /*
            if(objx.getStPolicyObjectID().equalsIgnoreCase(polObjectID)){
                objx.setStReference14("Y");
                objx.setDbReference2(premiKoas);//premi koas
                objx.setStReference10("N");
                objx.setStReference11("Y");
                objx.setDbReference7(rateKomisiKoas);//rate komisi koas
            }*/
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            policy.invokeCustomCriteria(obj);
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                
                cov.setStEntryPremiFlag("N");
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);
        
        policy.recalculate();
        
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI()){
            if(!approvalMode){
                policy.recalculateTreaty();
                policy.validateExclusionRisk(false);
                policy.validateTreaty(false);
            }
        }
        
        String pol_id = getRemoteInsurance().saveAndReturnPolicy(policy,policy.getStNextStatus(),false);
        
    }
    
    public void uploadExcelCreateEndorseKreasi()throws Exception{
        try {
            
            final DTOList konversiDocuments = policy.getKonversiDocuments();
            
            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
            
            String fileID = doc.getStFilePhysic();
            
            FileView file = FileManager.getInstance().getFile(fileID);
            
            FileInputStream fis = new FileInputStream(file.getStFilePath());
            
            final boolean inward = policy.getCoverSource().isInward();
            
            final String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
            
            createEndorseKreasiWithExcel(fis);
            
        } catch (Exception e) {
            throw new RuntimeException("error upload excel= "+ e);
        }
    }
    
    private void approveWithExcel(FileInputStream fis)throws Exception{
        try{
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("sheet");
            
            if(sheet==null)
                throw new RuntimeException("Sheet sheet Tidak Ditemukan !");
            
            int rows  = sheet.getPhysicalNumberOfRows();
            
            for (int r = 0; r < rows; r++){
                HSSFRow row   = sheet.getRow(r);
                
                HSSFCell cellPolId  = row.getCell(Short.parseShort("0"));//pol_id
                
                approveByExcel(cellPolId.getCellType()==cellPolId.CELL_TYPE_STRING?cellPolId.getStringCellValue():new BigDecimal(cellPolId.getNumericCellValue()).toString());
            }
        }catch (Exception e) {
            throw new RuntimeException("error reapprove using excel = "+ e);
        }
    }
    
    public void approveByExcel(String policyID) throws Exception {
        approval(policyID);
        btnApprove();
        
    }
    
    public void approveEndorseKreasiByExcel()throws Exception{
        try {
            final String fileID = getRemoteInsurance().getID();
            
            FileView file = FileManager.getInstance().getFile(fileID);
            
            FileInputStream fis = new FileInputStream(file.getStFilePath());
            
            final boolean inward = policy.getCoverSource().isInward();
            
            final String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
            
            approveWithExcel(fis);
            
        } catch (Exception e) {
            throw new RuntimeException("error upload excel= "+ e);
        }
    }
    
    private void uploadEarthquake(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
Penggunaan              stReference1
Zona Gempa              stReference2
Kelas Konstruksi	stReference3
Kategori Bangunan	stReference4
Jumlah Lantai           stReference10
Keterangan              stReference5
Zona                    stReference6
Tahun Pembuatan         stReference7
Alamat Risiko           stReference8
Kode Pos                stReference9
Kelas Konstruksi	stRiskClass
Kode Resiko
             **/
            
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            HSSFCell cell10  = row.getCell(Short.parseShort("10"));//nama
            HSSFCell cell11  = row.getCell(Short.parseShort("11"));//nama
            HSSFCell cell12  = row.getCell(Short.parseShort("12"));//nama
            HSSFCell cell13  = row.getCell(Short.parseShort("13"));//nama
            HSSFCell cellREF2DESC  = row.getCell(Short.parseShort("38"));//nama
            
            String kodePos = policy.getStKodePos(cell11.getCellType()==cell11.CELL_TYPE_STRING?cell11.getStringCellValue():new BigDecimal(cell11.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference9(kodePos);
            
            getSelectedDefaultObject().setStReference1(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2Desc(cellREF2DESC.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference10(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell10.getCellType()==cell10.CELL_TYPE_STRING?cell10.getStringCellValue():new BigDecimal(cell10.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference9(cell11.getCellType()==cell11.CELL_TYPE_STRING?cell11.getStringCellValue():new BigDecimal(cell11.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStRiskClass(cell12.getCellType()==cell12.CELL_TYPE_STRING?cell12.getStringCellValue():new BigDecimal(cell12.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setStRiskCategoryID(cell13.getCellType()==cell13.CELL_TYPE_STRING?cell13.getStringCellValue():new BigDecimal(cell13.getNumericCellValue()).toString());
            
            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */
            
            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(Short.parseShort("14"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(Short.parseShort("15"));//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("16"));//tsi
            
            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }
            
            
            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(Short.parseShort("17"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(Short.parseShort("18"));//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("19"));//tsi
            
            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }
            
            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(Short.parseShort("20"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(Short.parseShort("21"));//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(Short.parseShort("22"));//tsi
            
            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }
            
            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */
            
            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("23"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("24"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("25"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("26"));//tsi
            //HSSFCell cell_TSI1  = row.getCell(Short.parseShort("27"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }
            
            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("28"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("29"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("30"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("31"));//tsi
            //HSSFCell cell_TSI2  = row.getCell(Short.parseShort("32"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }
            
            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(Short.parseShort("33"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(Short.parseShort("34"));//RATE
            HSSFCell cell_Rate3 = row.getCell(Short.parseShort("35"));//premi
            HSSFCell cell_Premi3  = row.getCell(Short.parseShort("36"));//tsi
            //HSSFCell cell_TSI3  = row.getCell(Short.parseShort("37"));//ins_cvpt_id
            
            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
        
        
    }
    
    private void uploadMV(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
                No Polisi
                Merek/Jenis
                Tahun Pembuatan
                No Rangka
                No Mesin
                Tempat Duduk
                Digunakan
                Tipe
                Nama
                ins_risk_cat_id
             **/
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//no polisi
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            HSSFCell cell10  = row.getCell(Short.parseShort("10"));//Kode Resiko
            HSSFCell cellPeriodStart  = row.getCell(Short.parseShort("27"));//Kode Resiko
            HSSFCell cellPeriodEnd = row.getCell(Short.parseShort("28"));//Kode Resiko
            
            /*
            ins_tcpt_id
            ins_tsi_cat_id
            TSI
             *
            ins_tcpt_id
            ins_tsi_cat_id
            TSI
             *
            ins_cover_id
            insured_amount
            rate
            premi
            ins_cvpt_id
             *
            ins_cover_id
            insured_amount
            rate
            premi
            ins_cvpt_id
             */
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStReference2(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference9(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStRiskCategoryID(cell10.getCellType()==cell10.CELL_TYPE_STRING?cell10.getStringCellValue():new BigDecimal(cell10.getNumericCellValue()).toString());
            
            if(cellPeriodStart!=null){
                if(cellPeriodStart.getCellType()==cellPeriodStart.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
            }
            
            if(cellPeriodEnd!=null){
                if(cellPeriodEnd.getCellType()==cellPeriodEnd.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
            }
            
            //tsi kendaraan
            HSSFCell cell11  = row.getCell(Short.parseShort("11"));//ins_tcpt_id
            HSSFCell cell12  = row.getCell(Short.parseShort("12"));//ins_tsi_cat_id
            HSSFCell cell13  = row.getCell(Short.parseShort("13"));//tsi
            //tsi peralatan
            HSSFCell cell14  = row.getCell(Short.parseShort("14"));///ins_tcpt_id
            HSSFCell cell15  = row.getCell(Short.parseShort("15"));//ins_tsi_cat_id
            HSSFCell cell16  = row.getCell(Short.parseShort("16"));//tsi
            
            //cover comprehensive
            HSSFCell cell17  = row.getCell(Short.parseShort("17"));//ins_cover_id
            HSSFCell cell18  = row.getCell(Short.parseShort("18"));//insured_amount
            HSSFCell cell19  = row.getCell(Short.parseShort("19"));//rate
            HSSFCell cell20  = row.getCell(Short.parseShort("20"));//premi
            HSSFCell cell21  = row.getCell(Short.parseShort("21"));//ins_cvpt_id
            
            //cover tjh
            HSSFCell cell22 = row.getCell(Short.parseShort("22"));//ins_cover_id
            HSSFCell cell23  = row.getCell(Short.parseShort("23"));//insured_amount
            HSSFCell cell24  = row.getCell(Short.parseShort("24"));//rate
            HSSFCell cell25  = row.getCell(Short.parseShort("25"));//premi
            HSSFCell cell26  = row.getCell(Short.parseShort("26"));//ins_cvpt_id
            
            if(cell13!=null)
                doAddLampiranSumInsured(cell11.getCellType()==cell11.CELL_TYPE_STRING?cell11.getStringCellValue():new BigDecimal(cell11.getNumericCellValue()).toString(), new BigDecimal(cell13.getNumericCellValue()));
            
            //if(cell14!=null)
            //doAddLampiranSumInsured(cell14.getCellType()==cell14.CELL_TYPE_STRING?cell14.getStringCellValue():new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));
            
            if(cell14.getCellType()==cell14.CELL_TYPE_STRING){
                if(!cell14.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell14.getCellType()==cell14.CELL_TYPE_STRING?cell14.getStringCellValue():new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));
            }else if(cell14.getCellType()==cell14.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(cell14.getCellType()==cell14.CELL_TYPE_STRING?cell14.getStringCellValue():new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));
            }
            //NEW
            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("21"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("17"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("19"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("20"));//tsi
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("18"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            final BigDecimal tsi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI1.getNumericCellValue()))?null:new BigDecimal(cell_TSI1.getNumericCellValue());
            
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, tsi1);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, tsi1);
            }
            
            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("26"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("22"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("24"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("25"));//tsi
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("23"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            final BigDecimal tsi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI2.getNumericCellValue()))?null:new BigDecimal(cell_TSI2.getNumericCellValue());
            
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, tsi2);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, tsi2);
            }
            //END NEW
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
    }
    
    public void applyRISlipToALLMember() throws Exception{
        
        final DTOList objects = policy.getObjects();
        String riSlip = "NULL";
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final InsuranceTreatyDetailView tdr = trdi.getTreatyDetail();
                
                final InsuranceTreatyTypesView treatyType = tdr.getTreatyType();
                
                final boolean nonProportional = Tools.isYes(treatyType.getStNonProportionalFlag());
                
                final DTOList share = trdi.getShares();
                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView member = (InsurancePolicyReinsView) share.get(k);
                    
                    if(i==0 && j==0 && k==0) riSlip = member.getStRISlipNo();
                    
                    if(nonProportional) member.setStRISlipNo(riSlip);
                    
                }
            }
            
        }
        
    }
    
    public void editCreatePolisManual(String policyID) throws Exception {
        
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
        
        defineTreaty();
        
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStNextStatus(FinCodec.PolicyStatus.POLICY);
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStCoverNoteFlag("N");
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("51") && policy.getStCostCenterCode().equalsIgnoreCase("24")){
            setEnableNoPolicy(true);
            policy.generatePolicyNoWithoutCounter();
        }
        
        setEnableNoPolicy(true);
        policy.generatePolicyNoWithoutCounter();
        
        initTabs();
    }
    
    private void uploadBurglary(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");
        
        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
        
        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        
        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);
            
            HSSFCell cellControl  = row.getCell(Short.parseShort("0"));
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            /*
Occupation-L}{L-INA Penggunaan-L}
Class Construction-L}{L-INA Kelas Konstruksi-L}
Risk Code-L}{L-INA Kode Risiko-L}
Electricity-L}{L-INA Penerangan-L}
Risk Location-L}{L-INA Alamat Risiko-L}
Post Code-L}{L-INA Kode Pos-L}
Accumulation Code-L}{L-INA Kode Akumulasi-L}
Name-L}{L-INA Nama-L}
             
             **/
            
            HSSFCell cell1  = row.getCell(Short.parseShort("1"));//no polisi
            HSSFCell cell2  = row.getCell(Short.parseShort("2"));//merk/jenis
            HSSFCell cell3  = row.getCell(Short.parseShort("3"));//Tahun pembuatan
            HSSFCell cell4  = row.getCell(Short.parseShort("4"));//No rangka
            HSSFCell cell5  = row.getCell(Short.parseShort("5"));//No Mesin
            HSSFCell cell6  = row.getCell(Short.parseShort("6"));//Tempat duduk
            HSSFCell cell7  = row.getCell(Short.parseShort("7"));//Digunakan
            HSSFCell cell8  = row.getCell(Short.parseShort("8"));//Tipe
            HSSFCell cell9  = row.getCell(Short.parseShort("9"));//nama
            
            HSSFCell cellPeriodStart  = row.getCell(Short.parseShort("34"));//Kode Resiko
            HSSFCell cellPeriodEnd = row.getCell(Short.parseShort("35"));//Kode Resiko
            
            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            
            String kodePos = policy.getStKodePos(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference9(kodePos);
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType()==cell8.CELL_TYPE_STRING?cell8.getStringCellValue():new BigDecimal(cell8.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());
            
            if(cellPeriodStart!=null){
                if(cellPeriodStart.getCellType()==cellPeriodStart.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
            }
            
            if(cellPeriodEnd!=null){
                if(cellPeriodEnd.getCellType()==cellPeriodEnd.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
            }
            
            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */
            
            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(Short.parseShort("10"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(Short.parseShort("11"));//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(Short.parseShort("12"));//tsi
            
            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }
            
            
            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(Short.parseShort("13"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(Short.parseShort("14"));//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(Short.parseShort("15"));//tsi
            
            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }
            
            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(Short.parseShort("16"));//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(Short.parseShort("17"));//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(Short.parseShort("18"));//tsi
            
            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }
            
            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */
            
            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(Short.parseShort("19"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(Short.parseShort("20"));//RATE
            HSSFCell cell_Rate1  = row.getCell(Short.parseShort("21"));//premi
            HSSFCell cell_Premi1  = row.getCell(Short.parseShort("22"));//tsi
            //HSSFCell cell_TSI1  = row.getCell(Short.parseShort("23"));//ins_cvpt_id
            
            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }
            
            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(Short.parseShort("24"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(Short.parseShort("25"));//RATE
            HSSFCell cell_Rate2  = row.getCell(Short.parseShort("26"));//premi
            HSSFCell cell_Premi2  = row.getCell(Short.parseShort("27"));//tsi
            //HSSFCell cell_TSI2  = row.getCell(Short.parseShort("28"));//ins_cvpt_id
            
            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }
            
            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(Short.parseShort("29"));//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(Short.parseShort("30"));//RATE
            HSSFCell cell_Rate3 = row.getCell(Short.parseShort("31"));//premi
            HSSFCell cell_Premi3  = row.getCell(Short.parseShort("32"));//tsi
            //HSSFCell cell_TSI3  = row.getCell(Short.parseShort("33"));//ins_cvpt_id
            
            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());
            
            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }
        
        
    }
    
    public void createTemporaryPolicy() throws Exception {
        policy = new InsurancePolicyView();
        
        policy.setDetails(new DTOList());
        policy.setObjects(new DTOList());
        policy.setCovers(new DTOList());
        policy.setSuminsureds(new DTOList());
        policy.setCoins(new DTOList());
        policy.setDeductibles(new DTOList());
        policy.setInstallment(new DTOList());
        policy.setStInstallmentPeriods("1");
        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());
        
        policy.markNew();
        
        policy.setStStatus(FinCodec.PolicyStatus.TEMPORARY);
        policy.setStActiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        
        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbCurrencyRateTreaty(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));
        
        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
        onNewInstallment();
        
        policy.markNew();
        
        //defineTreaty();
        
        initTabs();
    }
    
    public String editCreateAutoEndorse(String policyID, BigDecimal premiBruto, BigDecimal TSI) throws Exception {
        policy = new InsurancePolicyView();
        
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.generateEndorseNo();
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStEndorseNotes("AUTO ENDORSEMEN TEMPORARY POLICY");
        
        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            policy.invokeCustomCriteria(obj);
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                
                cov.setStEntryPremiFlag("Y");
                cov.setStEntryRateFlag("N");
                cov.setStAutoRateFlag("N");
                cov.setDbPremiNew(BDUtil.negate(premiBruto));
                cov.setDbPremi(BDUtil.negate(premiBruto));
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
                
                tsi.setDbInsuredAmount(BDUtil.sub(tsi.getDbInsuredAmount(), TSI));
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSETEMPORARY);
        
        btnRecalculate();
        
        return getRemoteInsurance().saveAutoEndorsement(policy, policy.getStNextStatus(), true);
        
    }
    
    public void onNewCoinsuranceCoverage() throws Exception {
        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();
        
        co.markNew();
        
        co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);
        
        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);
        
        getCoinsCoverage().add(co);
        
    }
    
    public void onDeleteCoinsuranceCoverage() throws Exception {
        getCoinsCoverage().delete(Integer.parseInt(coinsCoverIndex));
    }
    
    public EntityView getEntity(String entityID) throws Exception{
        return policy.getEntity2(entityID);
    }

    
    public boolean downloadHistoryUsingExcel()throws Exception{
        
        boolean sudahPernah = policy.checkHistoryPolicyNoUsingExcel();

        final DTOList abaKreasi = policy.getABAKreasi();
        
        policy.setStPolicyNo(policy.convertOldPolicyNoToNewFormat(policy.getStPolicyNo()));
        
        //if(abaKreasi.size()<1) throw new RuntimeException("Data Polis Tidak Ditemukan");
        
        String stRiskCategoryID = null;
        
        if(abaKreasi.size()>0){
            for(int i=0; i < abaKreasi.size();i++){
            InsuranceKreasiView kreasi = (InsuranceKreasiView) abaKreasi.get(i);
            
            doNewLampiranObject();
            doAddLampiranSumInsured("225",kreasi.getDbInsured());
            
            /*
            if(policy.getStCostCenterCode().equalsIgnoreCase("21"))
             
            else
                getSelectedDefaultObject().setStReference1(kreasi.getStInsuranceNoUrut() + ". "+ kreasi.getStInsuranceNama()); //nama
             */
            getSelectedDefaultObject().setStReference1(kreasi.getStInsuranceNama()); //nama
            getSelectedDefaultObject().setDtReference1(kreasi.getDtTanggalLahir()); //tgl lahir
            getSelectedDefaultObject().setStReference2(kreasi.getStInsuranceUmur());
            getSelectedDefaultObject().setDtReference2(kreasi.getDtTanggalCair()); //awal
            getSelectedDefaultObject().setDtReference3(kreasi.getDtTanggalAkhir()); //awal
            getSelectedDefaultObject().setDbReference4(kreasi.getDbInsured()); //tsi
            getSelectedDefaultObject().setDbReference6(kreasi.getDbPremi()); //premi
            //getSelectedDefaultObject().setStReference9("Y");
            getSelectedDefaultObject().setDbReference5(kreasi.getDbRatePremi());
            
            if(i==0){
                policy.setDtPolicyDate(kreasi.getDtTanggalCair());
                policy.setDtPeriodStart(kreasi.getDtTanggalCair());
                stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
            }
            
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            if(i==(abaKreasi.size()-1)) policy.setDtPeriodEnd(kreasi.getDtTanggalAkhir());
            
            String manualPremiFlag = "N";
            
            doAddLampiranCoverHistory("148",kreasi.getDbRatePremi(),manualPremiFlag);
            }
        }
        
        
        defaultPeriod();
        defineTreaty();
        
        return sudahPernah;
        
    }
    
    public void editCreateEndorseMedan(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        //if (!policy.isEffective())
            //throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        
        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStReference8("DONIMEDAN");
        
        //policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            policy.invokeCustomCriteria(obj);
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }
     
    public void btnSaveMedan() throws Exception {
        
    
            policy.recalculate();
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()){
                if(!approvalMode){
                    policy.recalculateTreaty();
                    policy.validateExclusionRisk(false);
                    policy.validateTreaty(false);
                }
            }
            policy.validateData();
            policy.validate(false);

 
            getRemoteInsurance().save(policy,policy.getStNextStatus(),approvalMode);

    }
     
    public String getPolicyIDUsePolNo(String polNo)throws Exception{
        final SQLUtil S = new SQLUtil();
        String polid = null;
        try {

            final PreparedStatement PS = S.setQuery("select pol_id "+
                    " from ins_policy "+
                    " where pol_no like ? and status in ('POLICY','ENDORSE','RENEWAL','HISTORY')"+ 
                    " order by pol_no desc limit 1 ");
           
            
            PS.setString(1,"%"+polNo+"%");

            
            final ResultSet RS = PS.executeQuery();
            
            if (RS.next()) {
                polid = RS.getString("pol_id");
            }
            
            return polid;
            
        }finally{
            S.release();
        }
        
        
    }
      
    public void createEndorseUsePolicyNo(String nopol) throws Exception{
         final String pol_id = getPolicyIDUsePolNo(nopol);
         logger.logDebug("++++++++ CREATE ENDORSE USING NOPOL : "+ nopol+"  : POL ID : "+ pol_id +" ++++++++++");
         if(pol_id!=null){
            editCreateEndorseMedan(pol_id);
            btnSaveMedan();
         }

     }

    public void editCreateEndorseRIBGSB(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        //if (!policy.isEffective())
            //throw new RuntimeException("Policy not yet effective");

      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/

        //policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);

        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStReference8("ENDORSERIDONI");

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList item = policy.getDetails();

        for (int j = 0; j < item.size(); j++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            final BigDecimal premiObject = obj.getDbObjectPremiTotalAmount();

            policy.invokeCustomCriteria(obj);

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                cov.setStEntryPremiFlag(null);
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }

            final DTOList treaties = obj.getTreaties();

            for (int l = 0; l < treaties.size(); l++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);


                final DTOList tredetails = tre.getDetails();

                for (int j = 0; j < tredetails.size(); j++) {
                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                    final DTOList shares = tredet.getShares();

                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                        if(tredet.getTreatyDetail().isOR()){
                            ri.setDbPremiAmount(BDUtil.mul(premiObject, BDUtil.getRateFromPct(new BigDecimal(5)),0));
                            ri.setStAutoRateFlag(null);
                            ri.setStUseRateFlag(null);
                        }

                        if(tredet.getTreatyDetail().isQS()){
                            ri.setDbPremiAmount(BDUtil.mul(premiObject, BDUtil.getRateFromPct(new BigDecimal(5)),0));
                            ri.setDbPremiAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbSharePct()),0));
                            ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));
                            ri.setStAutoRateFlag(null);
                            ri.setStUseRateFlag(null);
                        }

                    }

                }


            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSERI);

        policy.setStEndorseNotes("KOMPOSISI SHARE REASURANSI ADALAH SEBAGAI BERIKUT :\n\n"+
                        "OWN RETENTION = 25%\n"+
                        "QUOTA SHARE    = 75%\n\n"+
                        "SELAIN DARIPADA ITU TETAP BERLAKU SEPERTI PADA POLIS");

        //initTabs();

    }

    public void editCreateEndorseBatalTotal(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

      if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
            throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
      }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");

        policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(cov.getDbPremi()));
                    cov.setDbPremiNew(cov.getDbPremi());
                }
                /*
                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(cov.getDbPremi()));
                    cov.setDbPremiNew(cov.getDbPremi());
                }*/
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                tsi.setDbInsuredAmount(BDUtil.zero);
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        if(policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
            policy.generatePersetujuanPrinsipEndorseNo();

        initTabs();

    }

    public void editCreateEndorseBatalCoas(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStReference8("ENDORSECOINS_LESSTHAN5MILLION_NOT5BRANCH");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n"+
                                "PENGHAPUSAN MEMBER CO-COVER ");

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList item = policy.getDetails();

        for (int j = 0; j < item.size(); j++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

            items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            objx.setStReference14("Y");
            objx.setDbReference2(BDUtil.negate(objx.getDbReference2()));
            objx.setStReference8("96");

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(cov.isEntryPremi())
                    cov.setStEntryPremiFlag(null);

            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());


            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    public void editCreateEndorseBatalCoas5Branch(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStReference8("ENDORSECOINS_5BRANCH");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n"+
                                "PENGHAPUSAN DAN PEMINDAHAN MEMBER CO-COVER ");

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList item = policy.getDetails();

        for (int j = 0; j < item.size(); j++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

            items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            objx.setStReference14("Y");
            objx.setDbReference2(objx.getDbReference2());

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(cov.isEntryPremi())
                    cov.setStEntryPremiFlag(null);

            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());


            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    public boolean isTotalEndorseMode() {
        return totalEndorseMode;
    }

    public void setTotalEndorseMode(boolean totalEndorseMode) {
        this.totalEndorseMode = totalEndorseMode;
    }

    public boolean isPartialEndorseTSIMode() {
        return partialEndorseTSIMode;
    }

    public void setPartialEndorseTSIMode(boolean partialEndorseTSIMode) {
        this.partialEndorseTSIMode = partialEndorseTSIMode;
    }

    public boolean isDescriptionEndorseMode() {
        return descriptionEndorseMode;
    }

    public void setDescriptionEndorseMode(boolean descriptionEndorseMode) {
        this.descriptionEndorseMode = descriptionEndorseMode;
    }
    
    public void editCreateEndorseBatalTotalMode(String policyID) throws Exception {
        superEditMode(policyID);
        
        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

      if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
            throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
      }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");

        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);
            
            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(cov.getDbPremi()));
                    cov.setDbPremiNew(cov.getDbPremi());
                }
                
                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(BDUtil.zero);
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                tsi.setDbInsuredAmount(BDUtil.zero);
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        if(policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
            policy.generatePersetujuanPrinsipEndorseNo();
        
        policy.setStEndorseNotes("ENDORSEMENT PEMBATALAN TOTAL \n\n"+
                                "");

        initTabs();

    }
    
    public void superEditMode(String policyID) throws Exception {
        view(policyID);
        
        //super.setReadOnly(false);
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
            
            obj.getTreaties().markAllUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
            }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }
            
            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
            
        }
        
        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();
        
        policy.showItemsAccount();
        
        //policy.reCalculateInstallment();
        
        initTabs();
        
    }
    
    public void editCreateEndorseDescriptionMode(String policyID) throws Exception {
        superEditMode(policyID);
        
        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

      if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
           !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
            throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
      }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");

        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);
            
            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        if(policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
            policy.generatePersetujuanPrinsipEndorseNo();
        
        //policy.setStEndorseNotes("ENDORSEMENT KETERANGAN \n\n"+
        //                        "");

        initTabs();

    }

    public boolean isPartialEndorseRateMode() {
        return partialEndorseRateMode;
    }

    public void setPartialEndorseRateMode(boolean partialEndorseRateMode) {
        this.partialEndorseRateMode = partialEndorseRateMode;
    }

    public boolean isRestitutionEndorseMode() {
        return restitutionEndorseMode;
    }

    public void setRestitutionEndorseMode(boolean restitutionEndorseMode) {
        this.restitutionEndorseMode = restitutionEndorseMode;
    }
    
    public void btnTolak() throws Exception {
        policy.setStEffectiveFlag("N");
        policy.setStRejectFlag("Y");
        
        policy.recalculate();
        
        final boolean noReverse = policy.isStatusClaimDLA();
        
        if (noReverse)
            getRemoteInsurance().save(policy, null,false);
        else
            getRemoteInsurance().saveAndReverse(policy);
        
        close();
        
    }
    
    public void approvalByDirector(String policyID) throws Exception {
        editApprove(policyID);
        
        super.setReadOnly(true);
        
        approvalMode = true;
        
        approvalByDirectorMode = true;
        
        tabs.setActiveTab("TAB_POLICY_DOCUMENTS");
        
    }

    public boolean isApprovalByDirectorMode() {
        return approvalByDirectorMode;
    }

    public void setApprovalByDirectorMode(boolean approvalByDirectorMode) {
        this.approvalByDirectorMode = approvalByDirectorMode;
    }

    public boolean isEditClaimNoteMode() {
        return editClaimNoteMode;
    }

    public void setEditClaimNoteMode(boolean editClaimNoteMode) {
        this.editClaimNoteMode = editClaimNoteMode;
    }
    
    public void applyDeductibleToAllObjects() throws Exception{
        final DTOList deduct = selectedObject.getDeductibles();

        final DTOList object = policy.getObjects();
        int dedSize = deduct.size();

        for (int  i= 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);
            
            if(obj==selectedObject) continue;
            
            for (int  j= 0; j < dedSize ; j++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deduct.get(j);
                
                InsurancePolicyDeductibleView newDed = new InsurancePolicyDeductibleView();
                newDed = ded;
                newDed.markNew();
                obj.getDeductibles().add(newDed);
            } 
            
        }
    }
    
    public void applyCoverageToAllObjects() throws Exception{
        final DTOList cover = selectedObject.getCoverage();

        final DTOList object = policy.getObjects();
        int covSize = cover.size();

        for (int  i= 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);
            
            obj.setDbObjectInsuredAmount(null);
            
            if(obj==selectedObject) continue;
            
            for (int  j= 0; j < covSize ; j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(j);
              
                final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

                cv.setStInsuranceCoverPolTypeID(cov.getStInsuranceCoverPolTypeID());

                cv.initializeDefaults();

                final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

                cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
                cv.setStCoverCategory(cvpt.getStCoverCategory());
                cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
                if(cov.isEntryInsuredAmount()) cv.setDbInsuredAmount(cov.getDbInsuredAmount());
                if(cov.isEntryPremi()){
                    cv.setDbPremi(cov.getDbPremi());
                    cv.setDbPremiNew(cov.getDbPremiNew());
                }
                cv.setStEntryInsuredAmountFlag(cov.getStEntryInsuredAmountFlag());
                cv.setStEntryPremiFlag(cov.getStEntryPremiFlag());
                cv.setStEntryRateFlag(cov.getStEntryRateFlag());
                cv.setDbRate(cov.getDbRate());

                cv.markNew();

                obj.getCoverage().add(cv);
            } 
        }
    }
    
    public void applySumInsuredToAllObjects() throws Exception{
        final DTOList tsi = selectedObject.getSuminsureds();

        final DTOList object = policy.getObjects();
        int tsiSize = tsi.size();

        for (int  i= 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);
            
            if(obj==selectedObject) continue;
            
            for (int  j= 0; j < tsiSize ; j++) {
                InsurancePolicyTSIView suminsured = (InsurancePolicyTSIView) tsi.get(j);
                
                InsurancePolicyTSIView newTSI = new InsurancePolicyTSIView();
                newTSI = suminsured;
                newTSI.markNew();
                obj.getCoverage().add(newTSI);
            } 
            
        }
    }
    
    public void createNewSPPAMandiri() throws Exception {
        policy = new InsurancePolicyView();
        
        policy.setDetails(new DTOList());
        policy.setObjects(new DTOList());
        policy.setCovers(new DTOList());
        policy.setSuminsureds(new DTOList());
        policy.setCoins(new DTOList());
        policy.setDeductibles(new DTOList());
        policy.setInstallment(new DTOList());
        policy.setStInstallmentPeriods("1");
        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());
        
        policy.markNew();
        
        policy.setStStatus(FinCodec.PolicyStatus.SPPA);
        policy.setStActiveFlag("Y");
        policy.setStCoverNoteFlag("N");
        
        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbCurrencyRateTreaty(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));
        policy.setStStatusOther("MANDIRI");
        
        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
        onNewInstallment();
        
        policy.markNew();
        
        initTabsMandiri();
    }
    
    private void initTabsMandiri() {
        
        tabs = new FormTab();
        
        tabs.add(new FormTab.TabBean("TAB_ENDORSE","ENDORSEMENT",false));
        tabs.add(new FormTab.TabBean("TAB_CLAIM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",false));
        tabs.add(new FormTab.TabBean("TAB_CLAIM_ITEM","{L-ENGCLAIM ITEMS-L}{L-INAITEM KLAIM-L}",false));
        tabs.add(new FormTab.TabBean("TAB_CLAIM_RE","{L-ENGCLAIM R/I-L}{L-INAR/I KLAIM-L}",false));
        tabs.add(new FormTab.TabBean("TAB_RISK_DET","{L-ENGRISK DETAIL-L}{L-INADETIL RESIKO-L}",true));
        tabs.add(new FormTab.TabBean("TAB_CLAUSES","{L-ENGCLAUSES-L}{L-INAKLAUSULA-L}",false));
        //tabs.add(new FormTab.TabBean("TAB_DEDUCTIBLES","DEDUCTIBLES",false));
        //tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",true));
        tabs.add(new FormTab.TabBean("TAB_INST","{L-ENGPAYMENT-L}{L-INAPEMBAYARAN-L}",false));
        tabs.add(new FormTab.TabBean("TAB_COINS","{L-ENGCOINSURANCE-L}{L-INAKOASURANSI-L}",true));
        tabs.add(new FormTab.TabBean("TAB_COINS_COVER","{L-ENGCO-COVER-L}{L-INACO-COVER-L}",true));
        //tabs.add(new FormTab.TabBean("TAB_COVER_REINS","{L-ENGR/I-COVER-L}{L-INAR/I-COVER-L}",true));
        tabs.add(new FormTab.TabBean("TAB_CLAIMCO","{L-ENGCLAIM CO-L}{L-INAKLAIM CO-L}",false));
        tabs.add(new FormTab.TabBean("TAB_CLAIMCOINS_COVER","{L-ENGCLAIM CO-COVER-L}{L-INAKLAIM CO-COVER-L}",false));
        //tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));
        //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));
        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INALAMPIRAN-L}",false));
        
        tabs.setActiveTab("TAB_RISK_DET");
        
        boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
        boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
        boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
        boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
        boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
        boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());
        boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStStatus());
        boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStStatus());
        boolean statusEndorseRI = FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(getStStatus());
        
        boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
        boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
        
        if(!statusDraft && !statusSPPA){
            tabs.enable("TAB_CLAUSES",true);
            //tabs.enable("TAB_PREMI",true);
            tabs.enable("TAB_INST",true);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            //tabs.enable("TAB_OPTIONS",true);
        }
        
        
        if (statusEndorse||statusClaimEndorse||statusEndorseRI) {
            tabs.enable("TAB_ENDORSE",true);
            tabs.setActiveTab("TAB_ENDORSE");
            
        }
        
        if (statusClaim||statusClaimEndorse) {
            tabs.enable("TAB_CLAIM",true);
            tabs.enable("TAB_CLAIM_ITEM",true);
            tabs.enable("TAB_CLAIM_RE",true);
            tabs.enable("TAB_CLAIMCO",true);
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            //tabs.enable("TAB_COVER_REINS",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",false);
            tabs.enable("TAB_CLAIMCOINS_COVER",true);
            tabs.setActiveTab("TAB_CLAIM");
        }
        
        rdtabs = new FormTab();
        
        rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
        //rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
        //rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
        rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_DED","{L-ENGDEDUCTIBLES-L}{L-INARESIKO SENDIRI-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));
        
        rdtabs.setActiveTab("TAB_DETAIL");
        
        if (!(reasMode || statusClaim || statusEndorseRI)) {
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
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            //tabs.enable("TAB_COVER_REINS",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",false);
        }
        
        claimtabs.setActiveTab("TAB_DOCUMENTS");
        
        if(statusClaimEndorse){
            tabs.setActiveTab("TAB_ENDORSE");
        }
        
        if(statusDLA||statusClaimEndorse){
            claimtabs.enable("TAB_DLA",true);
        }
        
    }
    
    public void onChangeRegionMandiri() throws Exception {
        if (policy.getStRegionID()!=null) {
            policy.setStCoverTypeCode("DIRECT");
            chgCoverTypeMandiri();
        }
    }
    
    public void chgCoverTypeMandiri() throws Exception {
        
        policy.setDtPolicyDate(new Date());
        if (policy.getStRegionID()==null) {
            policy.setStCoverTypeCode(null);
            throw new RuntimeException("Region cannot be empty");
        }
        
        insItemLOV = null;
        policy.getCoins2().deleteAll();
        policy.getCoinsCoverage().deleteAll();
        final String polType = policy.getStPolicyTypeID();
        
        
        if(polType.equalsIgnoreCase("21")||polType.equalsIgnoreCase("3")||polType.equalsIgnoreCase("55")||polType.equalsIgnoreCase("56")||polType.equalsIgnoreCase("57")||
                polType.equalsIgnoreCase("58")||polType.equalsIgnoreCase("51")||polType.equalsIgnoreCase("52")||polType.equalsIgnoreCase("53")||polType.equalsIgnoreCase("54")||
                polType.equalsIgnoreCase("5")||polType.equalsIgnoreCase("14"))
        {
            policy.setDbPeriodRate(new BigDecimal(1));
            policy.setStPeriodBaseID("6");
            policy.setStPremiumFactorID("1");
        }
        
        if (policy.getStCoverTypeCode()!=null) {
            try {
                addMyCompany();
                
                addMyCompanyCoverage();
                
                
            } catch (Exception e) {
                policy.setStCoverTypeCode(null);
                throw e;
            }
        }
        
    }
    
    public void changeBranchMandiri() throws Exception {
        
            SQLUtil S = new SQLUtil();
            
            try {
                PreparedStatement PS = S.setQuery("select region_id from s_region where cc_code = ? ");
                PS.setString(1, policy.getStCostCenterCode());
                ResultSet RS = PS.executeQuery();
                if (RS.next())
                    policy.setStRegionID(RS.getString(1));
            } finally {
                S.release();
            }
            
            onChangeRegionMandiri();

    }
    
    public void doNewObjectMandiri() throws Exception {
        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getClObjectClass().newInstance();
        o.markNew();
        o.setPolicy(policy);
        o.setCoverage(new DTOList());
        
        o.setSuminsureds(new DTOList());
        o.setDeductibles(new DTOList());
        
        final DTOList mainCovers = getMainCover();
        
        for (int i = 0; i < mainCovers.size(); i++) {
            InsurancePolicyCoverView mainCover = (InsurancePolicyCoverView) mainCovers.get(i);
            mainCover.markNew();
            mainCover.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
            
            o.getCoverage().add(mainCover);
        }
        getObjects().add(o);
        
        setStSelectedObject(String.valueOf(getObjects().size()-1));
        switchSelectedObject();
        
        final DTOList covers = getCoverageAddLOVMandiri();
        
        for (int i = 0; i < covers.size(); i++) {
            InsuranceCoverPolTypeView cpt = (InsuranceCoverPolTypeView) covers.get(i);
            
            if (!Tools.isYes(cpt.getStDefaultCoverFlag2())) continue;
            
            final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
            
            cv.setStInsuranceCoverPolTypeID(cpt.getStInsuranceCoverPolTypeID());
            
            cv.initializeDefaults();
            
            final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
            
            cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
            cv.setStCoverCategory(cvpt.getStCoverCategory());
            
            cv.markNew();
            
            o.getCoverage().add(cv);
            
        }
        
        
        final DTOList sumInsuredAddLOV = getSumInsuredAddLOVMandiri();
        
        for (int i = 0; i < sumInsuredAddLOV.size(); i++) {
            InsuranceTSIPolTypeView ipt = (InsuranceTSIPolTypeView) sumInsuredAddLOV.get(i);
            
            if (!Tools.isYes(ipt.getStDefaultTSIFlag2())) continue;
            
            final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
            
            ptsi.setStInsuranceTSIPolTypeID(ipt.getStInsuranceTSIPolTypeID());
            
            ptsi.initializeDefaults();
            
            ptsi.markNew();
            
            selectedObject.getSuminsureds().add(ptsi);
        }
        
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
            defineObjectTreaty();
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")||policy.getStPolicyTypeID().equalsIgnoreCase("98")||policy.getStPolicyTypeID().equalsIgnoreCase("99"))
            selectedObject.setStRiskCategoryID(policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart()));
        
        policy.getHandler().onNewObject(policy,o);
        
    }
    
    private DTOList getMainCoverMandiri() throws Exception {
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
    
    public DTOList getSumInsuredAddLOVMandiri() throws Exception {
        allowDuplicateTSI = true;
        
        loadSumInsuredLOVMandiri();
        
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
    
    private void loadSumInsuredLOVMandiri() throws Exception {
        if (sumInsuredLOV == null) {
            sumInsuredLOV = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.ins_tcpt_id, b.ins_tsi_cat_id, b.description, a.default_tsi_flag2" +
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
    
    public DTOList getCoverageAddLOVMandiri() throws Exception {
        loadCoverageLOVMandiri();
        
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
    
    private void loadCoverageLOVMandiri() throws Exception {
        if (coverageLOV==null)
            coverageLOV = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.ins_cvpt_id, a.ins_cover_id,  b.description, a.cover_category, a.default_cover_flag2" +
                    "   from" +
                    "      ins_cover_poltype a" +
                    "      inner join ins_cover b on b.ins_cover_id = a.ins_cover_id" +
                    "   where " +
                    "      a.pol_type_id = ? order by ins_cvpt_id" ,
                    new Object [] {policy.getStPolicyTypeID()},
                    InsuranceCoverPolTypeView.class
                    );
    }

 
}

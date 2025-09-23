/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:53 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.common.controller.FormTab;
import com.crux.common.parameter.Parameter;
import com.crux.common.config.Config;
import com.crux.common.model.UserSession;
import com.crux.util.*;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.lov.LOVManager;
import com.crux.pool.DTOPool;
import com.crux.ff.model.FlexTableView;
import com.webfin.FinCodec;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.insurance.custom.PAKreasiHandler;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.*;
import com.crux.file.FileView;
import com.crux.file.FileManager;
import com.crux.util.crypt.Digest;
import com.webfin.approval.model.ApprovalView;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;

import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.webfin.incoming.model.IncomingView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Date;

import java.io.*;
import java.math.BigInteger;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Months;
import org.joda.time.Years;

public class PolicySplitForm extends Form {
    
    private final static transient LogManager logger = LogManager.getInstance(PolicySplitForm.class);
    
    private InsuranceSplitPolicyView policy;
    private FormTab tabs;
    private FormTab rdtabs;
    private FormTab ritabs;
    private FormTab claimtabs;
    private String stSelectedObject;
    private String stClaimObject;
    private String detailindex;
    private String coinsIndex;
    private String insItemID;
    private InsuranceSplitPolicyObjectView selectedObject;
    private InsuranceSplitPolicyObjDefaultView selectedDefaultObject;
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
    
    private Integer idxTreaty;
    private Integer idxTreatyDetail;
    private Integer idxTreatyShares;
    private Integer idxTreatySharesCover;
    
    private boolean reasMode;
    private boolean approvalMode;
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
    private boolean editKeteranganMode = false;
    
    private boolean approvalByDirectorMode;
    private boolean editClaimNoteMode;
    private boolean rejectMode;
    private DTOList deductibleLOV;
    
    private boolean enableSwapPremiORKoas = SessionManager.getInstance().getSession().hasResource("SWAP_COAS_PREMIUM");
    private boolean createEndorseAndPolicyMode = false;
    private boolean editReasMode = false;
    private boolean approvedReasMode = false;

    private boolean canApproveHeadOffice = SessionManager.getInstance().getSession().hasResource("POL_UWRIT_HEADOFFICE_APPROVAL");

    private String stKey;
    private String objIndex;
    private String deductibleAddID;
    private DTOList clausulesLOV;
    private String clausulesAddID;
    private String clausulesIndex;
    private boolean reasDataOnlyApprovedMode = false;
    private boolean riskApprovalMode;
    private boolean showNotifLetter = false;
    private boolean bentukPolisMode;
    private String kirimNotifikasi;
    private String coverageLine;

    private boolean canEditValidasiCabang = SessionManager.getInstance().getSession().hasResource("POL_PROP_APRV_RISK_BRANCH");
    private boolean canEditValidasiInduk = SessionManager.getInstance().getSession().hasResource("POL_PROP_APRV_RISK_INDUK");
    private boolean canEditValidasiPusat = SessionManager.getInstance().getSession().hasResource("POL_PROP_APRV_RISK_HO");

    private boolean showRIPerils = false;

    private String docIndex;

    private boolean approvalByDivisiMode;
    private boolean drillMode = false;
    private boolean showRIInstallment = false;
    private String instRIIndex;
    private Integer idxTreatySharesInst;
    private boolean showTop5Object = false;

    public boolean isShowTop5Object() {
        return showTop5Object;
    }

    public void setShowTop5Object(boolean showTop5Object) {
        this.showTop5Object = showTop5Object;
    }

    public Integer getIdxTreatySharesInst() {
        return idxTreatySharesInst;
    }

    public void setIdxTreatySharesInst(Integer idxTreatySharesInst) {
        this.idxTreatySharesInst = idxTreatySharesInst;
    }

    public String getInstRIIndex() {
        return instRIIndex;
    }

    public void setInstRIIndex(String instRIIndex) {
        this.instRIIndex = instRIIndex;
    }

    public boolean isDrillMode() {
      return drillMode;
   }

   public void setDrillMode(boolean drillMode) {
      this.drillMode = drillMode;
   }

    public boolean isApprovalByDivisiMode() {
        return approvalByDivisiMode;
    }

    public void setApprovalByDivisiMode(boolean approvalByDivisiMode) {
        this.approvalByDivisiMode = approvalByDivisiMode;
    }
    
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
        //selectedObject = policy.getClaimObject();
        
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
    
    public void changeBranch() throws Exception{
        cekClosingStatus("PROPOSAL");
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
        policy.recalculateTreatyInitialize();
        
    }
    
    public void changeTreaty() {
        final DTOList objects = policy.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            obj.setStInsuranceTreatyID(null);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                obj.setDbReference1(null);
                obj.setDbReference2(null);
                obj.setDbReference3(null);
                obj.setDbReference4(null);
            }

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

        if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){

            InsuranceSplitPolicyObjDefaultView obj = (InsuranceSplitPolicyObjDefaultView) selectedObject;

            obj.setDbReference1(null);
            obj.setDbReference2(null);
            obj.setDbReference3(null);
            obj.setDbReference4(null);
        }
        
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
    
    public InsuranceSplitPolicyObjectView getSelectedObject() {
        return selectedObject;
    }
    
    public void setSelectedObject(InsuranceSplitPolicyObjectView selectedObject) {
        this.selectedObject = selectedObject;
    }
    
    public InsuranceSplitPolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;
    }
    
    public void setSelectedDefaultObject(InsuranceSplitPolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;
    }
    
    public InsuranceSplitPolicyView getPolicy() {
        return policy;
    }
    
    public void setPolicy(InsuranceSplitPolicyView policy) {
        this.policy = policy;
    }
    
    public void createNew() throws Exception {
        policy = new InsuranceSplitPolicyView();
        
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
        //applyDefaultClausules();
        
        policy.markNew();
        
        initTabs();
    }
    
    public void createNewPolicyHistory() throws Exception {
        policy = new InsuranceSplitPolicyView();
        
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
            selectedObject = (InsuranceSplitPolicyObjectView) getObjects().get(Integer.parseInt(stSelectedObject));
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
        //return policy.getClausulesNew();
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
            InsuranceSplitPolicyObjDefaultView obj = (InsuranceSplitPolicyObjDefaultView) objects.get(i);
            
            obj.getSuminsureds().markAllUpdate(); 
            //obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
          
            
        }
        
        //policy.getCoins2().markAllUpdate();
        //policy.getDetails().markAllUpdate();
        //policy.getClaimItems().markAllUpdate();
        //policy.getDeductibles().markAllUpdate();
        //policy.getInstallment().markAllUpdate();
        //policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        
        //policy.getClausules().markAllUpdate();
        
        //policy.showItemsAccount();
        
        //policy.reCalculateInstallment();
        
        initTabs();
        
    }

    public void superEditPLA(String policyID) throws Exception {
        view(policyID);

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        int size = objects.size();

        if(size<=1000){

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

                policy.invokeCustomCriteria(obj);

                if(obj.getStOrderNo()==null){

                    obj.setStOrderNo(String.valueOf(i+1));

                    SQLUtil S = new SQLUtil();

                    PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ obj.getStOrderNo() +" where ins_pol_obj_id = ?");

                    PS.setObject(1,obj.getStPolicyObjectID());

                    int j = PS.executeUpdate();

                    S.release();
                }

            }
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
        policy.getObjects().markAllUpdate();
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
        
        initReinsuranceTabs();
    }
    
    public FormTab getRdtabs() {
        return rdtabs;
    }
    
    public void onFormCreate() {
        initTabs();
    }
    
    private void initTabs() {
        
        tabs = new FormTab();
        
        tabs.add(new FormTab.TabBean("TAB_REJECT","{L-ENGREJECT NOTES-L}{L-INAKETERANGAN-L}",false));
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
        //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));
        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INALAMPIRAN-L}",false));
        tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));
        
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
        boolean statusInward = FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(getStStatus());
        boolean statusClaimInward = FinCodec.PolicyStatus.CLAIMINWARD.equalsIgnoreCase(getStStatus());
        
        boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
        boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
        boolean rejectModex = isRejectMode();
        
        if(!statusDraft && !statusSPPA){
            tabs.enable("TAB_CLAUSES",true);
            //tabs.enable("TAB_PREMI",true);
            tabs.enable("TAB_INST",true);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            tabs.enable("TAB_OPTIONS",true);
        }
        
        if (statusEndorse||statusClaimEndorse||statusEndorseRI) {
            tabs.enable("TAB_ENDORSE",true);
            tabs.setActiveTab("TAB_ENDORSE");
        }

        if(rejectModex){
            tabs.enable("TAB_REJECT",true);
            tabs.setActiveTab("TAB_REJECT");
        }
        
        if (statusClaim || statusClaimEndorse || statusClaimInward) {
            tabs.enable("TAB_CLAIM",true);
            tabs.enable("TAB_CLAIM_ITEM",true);
            tabs.enable("TAB_CLAIM_RE",true);
            tabs.enable("TAB_CLAIMCO",true);
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            tabs.enable("TAB_CLAIMCOINS_COVER",true);
            tabs.setActiveTab("TAB_CLAIM");
            tabs.enable("TAB_OPTIONS",true);
        }
        
        rdtabs = new FormTab();
        
        rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
        //rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
        //rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
        rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_DED","{L-ENGDEDUCTIBLES-L}{L-INARESIKO SENDIRI-L}",true));

        //rdtabs.add(new FormTab.TabBean("TAB_ANALYSIS","ANALISA RESIKO",true));
        //rdtabs.add(new FormTab.TabBean("TAB_DETAIL_DOCUMENTS","DOKUMEN",true));
        rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));
        
        rdtabs.setActiveTab("TAB_DETAIL");
        
        if (!(reasMode || statusPolicy || statusRenewal || statusEndorse || statusClaim || statusEndorseRI || statusInward || statusClaimEndorse || statusClaimInward)) {
            rdtabs.enable("TAB_REINS",false);
        }
        
        if (Config.isDevelopmentMode()) {
            rdtabs.enable("TAB_REINS",true);
            
            if(!statusDraft){
                tabs.enable("TAB_CLAUSES",true);
                //tabs.enable("TAB_PREMI",true);
                tabs.enable("TAB_INST",true);
                tabs.enable("TAB_POLICY_DOCUMENTS",true);
                tabs.enable("TAB_OPTIONS",true);
            }
        }
        //rdtabs.enable("TAB_REINS",true);
        
        if (statusDraft) setTitle("SPLIT POLIS");
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
        claimtabs.add(new FormTab.TabBean("TAB_SUBROGASI_CLAIM", "SUBROGASI", true));
        //claimtabs.add(new FormTab.TabBean("TAB_APPROVED","{L-ENGAPPROVED CLAIM-L}{L-INAKLAIM DISETUJUI-L}",false));
        
        if(statusClaim || statusClaimEndorse || statusClaimInward){
            claimtabs.enable("TAB_DOCUMENTS",true);
            claimtabs.enable("TAB_PLA",true);
            claimtabs.enable("TAB_SUBROGASI_CLAIM", true);
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            rdtabs.enable("TAB_SPPA",false);
            //rdtabs.enable("TAB_ANALYSIS",false);

            //rdtabs.enable("TAB_DETAIL_DOCUMENTS",false);
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
        
        policy = getRemoteInsurance().getInsuranceSplitPolicy(policyID);
        
        if (policy==null) throw new RuntimeException("Policy not found");
        
        policy.loadObjects();
        //policy.loadClausules();
        //policy.loadEntities();
        //policy.loadDetails();
        //policy.loadCoins();
        //policy.loadInstallment();
        
        //policy.reCalculateInstallment();
        
        //policy.reCalculateInstallment2();
        
        super.setReadOnly(true);
        
        //productionModeTabs();
        
        initTabs();
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
        final InsuranceSplitPolicyObjectView o = (InsuranceSplitPolicyObjectView) policy.getClObjectClass().newInstance();
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
        
        final DTOList deductiblesLOV = getDeductibleAddLOV();
        
        for (int i = 0; i < deductiblesLOV.size(); i++) {
            InsuranceClaimCauseView cause = (InsuranceClaimCauseView) deductiblesLOV.get(i);
            
            if (!Tools.isYes(cause.getStDefaultFlag())) continue;
            
            final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();
            
            ded.setStInsuranceClaimCauseID(cause.getStInsuranceClaimCauseID());

            ded.markNew();
            
            selectedObject.getDeductibles().add(ded);
        }
        
        if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()||policy.isStatusInward())
            defineObjectTreaty();
        
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")||policy.getStPolicyTypeID().equalsIgnoreCase("98")||policy.getStPolicyTypeID().equalsIgnoreCase("99")||policy.getStPolicyTypeID().equalsIgnoreCase("64"))
            selectedObject.setStRiskCategoryID(policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart()));

        if(policy.getStPolicyTypeID().equalsIgnoreCase("83"))
            selectedObject.setStRiskCategoryCode1("2976");

        if(policy.getStPolicyTypeID().equalsIgnoreCase("83"))
            selectedObject.setStRiskCategoryID("18542");

        InsuranceSplitPolicyObjDefaultView selectedObjectX = (InsuranceSplitPolicyObjDefaultView) selectedObject;
        selectedObjectX.setDbPeriodRate(policy.getDbPeriodRate());
        selectedObjectX.setStPeriodBaseID(policy.getStPeriodBaseID());
        selectedObjectX.setStPremiumFactorID(policy.getStPremiumFactorID());

        //policy.getHandler().onNewObject(policy,o);
        
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
        if(policy.getStPolicyTypeID().equalsIgnoreCase("42")){
            policy.setStPolicyTypeID(null);
            throw new RuntimeException("Harus input Cash Management di Jenis CIT(Cash Management)");
        }

//        if(policy.getStPolicyTypeID().equalsIgnoreCase("44")){
//            policy.setStPolicyTypeID(null);
//            throw new RuntimeException("Untuk sementara jenis ini tidak bisa diinput karena sedang proses perbaikan");
//        }
            
        
        //policy.setStCoverTypeCode(policy.getPolicyType().getStInsuranceCoverSourceID());
        policy.setStCoverTypeCode(getInsuranceCoverIDBranch());
        
        if(policy.isStatusInward()){
            policy.setStCoverTypeCode("DIRECT");
            policy.setStRegionID("1");
            policy.setStCostCenterCode("00");
        }

        chgCoverType();
            
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

        if(getInsItemCat(insItemID).isNotReinsCharge())
            item.setStChargableFlag("N");
        
        //policy.recalculate();
        
        return item;
    }
    
    public void onNewDetail() throws Exception {
        
        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(insItemID);
        
        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();
        
        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);
        
        item.markNew();
        
        item.setStInsItemID(insItemID);
        
        item.setStInsuranceItemCategory(itemCat.getStItemCategory());

        if(itemCat.getDbDefaultValue()!=null){
            item.setStFlagEntryByRate("Y");
            item.setDbRate(itemCat.getDbDefaultValue());
        }

        
        getDetails().add(item);

        if(itemCat.getStInsuranceItemChildID()!=null)
            onNewDetailByChildID(itemCat.getStInsuranceItemChildID());
        
    }

    public void onNewDetailByChildID(String itemID) throws Exception {

        //if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(itemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(itemID);

        item.setStInsuranceItemCategory(itemCat.getStItemCategory());

        item.setStROFlag("Y");

        if(itemCat.getDbDefaultValue()!=null){
            item.setStFlagEntryByRate("Y");
            item.setDbRate(itemCat.getDbDefaultValue());
        }

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
            //policy.setStCoverTypeCode(policy.getPolicyType().getStInsuranceCoverSourceID());
            if(policy.isStatusInward()) policy.setStCoverTypeCode("DIRECT");
            //chgCoverType();
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
        
        if(!policy.isStatusEndorse())
            if(policy.getPolicyType().isDefaultPeriod())
                defaultPeriod();
        
        if (policy.getStCoverTypeCode()!=null) {
            try {
                addMyCompany();
                
                addMyCompanyCoverage();
                
                if(!policy.isStatusEndorse()){
                    //addBiayaPolisMateraiAutomatic();
                
                    //addKomisiAutomatic();
                }

            } catch (Exception e) {
                policy.setStCoverTypeCode(null);
                throw e;
            }
        }
        
    }
    
    private void defineTreaty() throws Exception{
        final boolean inward = policy.getCoverSource().isInward();
        
        String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");

        final String polTypeID = policy.getStPolicyTypeID();

        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            if(polTypeID.equalsIgnoreCase("3") || polTypeID.equalsIgnoreCase("1") || polTypeID.equalsIgnoreCase("81")){
                if(obj.getDtReference1()!=null)
                    treatyID = obj.getDtReference1()!=null?policy.getInsuranceTreatyID(obj.getDtReference1()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");
            }

            if(polTypeID.equalsIgnoreCase("85")){
                treatyID = "36";
            }

            if(obj.getRiskCategory().getStInsuranceTreatyID()!=null)
                    treatyID = obj.getRiskCategory().getStInsuranceTreatyID();
            
            if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("15")){
                setCMTreaty(obj);
            }else{
                obj.setStInsuranceTreatyID(treatyID);
            }

            ////jika gempa bumi
            if(polTypeID.equalsIgnoreCase("19"))
                setEarthquakeTreaty(obj);

            //jika kredit konsumtif
            if(polTypeID.equalsIgnoreCase("59")){
                setCreditTreaty(obj);
            }
   
            //jika PA Kreasi
            if(polTypeID.equalsIgnoreCase("21")){

                DateTime bulanKreasiTreaty = new DateTime(DateUtil.getDate("31/03/2013"));
                DateTime bulanProduksi = new DateTime(policy.getDtPolicyDate());

                if(bulanProduksi.isAfter(bulanKreasiTreaty))
                           setCreditTreaty(obj);
            }

        }
    }
    
    public void setCMTreaty(InsurancePolicyObjDefaultView obj)throws Exception{
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList cov = obj.getCoverage();
        for (int i = 0; i < cov.size(); i++) {
            InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(i);
            
            if(cover.getStInsuranceCoverID().equalsIgnoreCase("139")||cover.getStInsuranceCoverID().equalsIgnoreCase("304")){
                if(policy.getDtPeriodStart()!=null)
                    //obj.setStInsuranceTreatyID("27");
                    obj.setStInsuranceTreatyID(getInsuranceTreatyIDCM(policy.getDtPeriodStart(), "139"));
                    
            }
            
            if(cover.getStInsuranceCoverID().equalsIgnoreCase("143")||cover.getStInsuranceCoverID().equalsIgnoreCase("319")
                    ||cover.getStInsuranceCoverID().equalsIgnoreCase("320")||cover.getStInsuranceCoverID().equalsIgnoreCase("321")){
                if(policy.getDtPeriodStart()!=null)
                    //obj.setStInsuranceTreatyID("30");
                    obj.setStInsuranceTreatyID(getInsuranceTreatyIDCM(policy.getDtPeriodStart(), "143"));
            }
            
        }
    }
    
    public String getInsuranceTreatyIDCM(Date per_start, String ins_cover_id) throws Exception {
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
                    "   and ref1 like ? ");
            
            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, "%"+ins_cover_id+"%");
            
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

        InsuranceSplitPolicyObjDefaultView objx = (InsuranceSplitPolicyObjDefaultView) selectedObject;

 
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
        policy.recalculateTreatyInitialize();
        //policy.recalculateTreatyInitializePerObject(selectedObject);
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
        /*if (policy.isStatusEndorse()) {
            final InsurancePolicyTSIView dtsi = ((InsurancePolicyTSIView)selectedObject.getSuminsureds().get(Integer.parseInt(tsiIndex)));
            if (dtsi.hasRef()) {
                dtsi.doVoid();
                
                return;
            }
        }*/
        
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
        
        loadSumInsuredLOV();
        
        //if (allowDuplicateTSI) return sumInsuredLOV;
        
        final HashMap dupMap = selectedObject.getSuminsureds().getMapOf("ins_tsi_cat_id");
        
        final DTOList lov = new DTOList();
        
        for (int i = 0; i < sumInsuredLOV.size(); i++) {
            InsuranceTSIPolTypeView insuranceTSIView = (InsuranceTSIPolTypeView) sumInsuredLOV.get(i);
            
            //if (dupMap!=null)
                //if (dupMap.containsKey(insuranceTSIView.getStInsuranceTSICategoryID())) continue;

            if(insuranceTSIView.getStActiveFlag()!=null)
                if(Tools.isNo(insuranceTSIView.getStActiveFlag()))
                    continue;
            
            lov.add(insuranceTSIView);
        }
        
        return lov;
    }
    
    private void loadSumInsuredLOV() throws Exception {
        if (sumInsuredLOV == null) {
            sumInsuredLOV = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.ins_tcpt_id, b.ins_tsi_cat_id, b.description, a.default_tsi_flag,a.active_flag" +
                    "   from  " +
                    "      ins_tsicat_poltype a " +
                    "      inner join ins_tsi_cat b on b.ins_tsi_cat_id = a.ins_tsi_cat_id" +
                    "   where" +
                    "      a.pol_type_id=? order by a.ins_tcpt_id" ,
                    new Object [] {policy.getStPolicyTypeID()},
                    InsuranceTSIPolTypeView.class
                    );
        }
    }
    
    public void doAddCoverage() throws Exception {
        /*
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();
        
        cv.setStInsuranceCoverPolTypeID(coverageAddID);
        
        cv.initializeDefaults();
        
        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();
        
        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);
        
        cv.markNew();
        */

        if(getCoverageLine()!=null){
            int line = Integer.parseInt(getCoverageLine());

            for (int i = 0; i < line; i++) {
                final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

                cv.setStInsuranceCoverPolTypeID(coverageAddID);

                cv.initializeDefaults();

                final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

                cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
                cv.setStCoverCategory(cvpt.getStCoverCategory());
                cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

                cv.markNew();
                cv.setStDepreciationYear(String.valueOf(i+1));
                selectedObject.getCoverage().add(cv);
            }
                setCoverageLine(null);

        }else{
            final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

            cv.setStInsuranceCoverPolTypeID(coverageAddID);

            cv.initializeDefaults();

            final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

            cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
            cv.setStCoverCategory(cvpt.getStCoverCategory());
            cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

            cv.markNew();
            selectedObject.getCoverage().add(cv);
        }
        
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

            if(cv.getStActiveFlag()!=null) continue;

            if(cv.getStActiveFlag()!=null)
                if(Tools.isNo(cv.getStActiveFlag()))
                    continue;

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
                    "      coalesce(a.active_flag,'Y') <> 'N' and a.pol_type_id = ? order by a.ins_cvpt_id" ,
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

        policy.recalculate();
        
        if(policy.isRecalculateTreaty()){
            policy.recalculateTreaty();
        }
    }

    public void btnRecalculateClaim()throws Exception {

        final DTOList objects = policy.getObjects();

        int size = objects.size();

        if(size<=1000){
            
            policy.recalculate();

            if(policy.isRecalculateTreaty()){
                policy.recalculateTreaty();
            }
        }else{
            
            policy.recalculateClaimOnly();

            if(policy.isRecalculateTreaty()){
                policy.recalculateTreatyClaimObject();
            }
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
        

        if(!isInputPaymentDateMode() && !isEditKeteranganMode() && !editReasMode && !isEditClaimNoteMode()){
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
        }

        getRemoteInsurance().saveSplit(policy,policy.getStNextStatus(),approvalMode);
        
        close();
    } 

    
    public void btnReverse() throws Exception {
        
        boolean withinCurrentMonth = DateUtil.getDateStr(policy.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        //final boolean blockValidPolicyDateAdmin = Parameter.readBoolean("BLOCKING_POLICY_DATE_ADMIN");
        boolean canReverse = true;
        
        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse())
            if(!withinCurrentMonth) canReverse = false;
        
        if(policy.isStatusClaim()||policy.isStatusClaimEndorse()){
            boolean withinCurrentMonthClaim = DateUtil.getDateStr(policy.getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
            if(!withinCurrentMonthClaim) canReverse = false;
            
            if(!withinCurrentMonthClaim)
                if(policy.isStatusClaimPLA()) canReverse = true;
        }
        
        //if(!blockValidPolicyDateAdmin) canReverse = true;
        
        if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal())
            if(!withinCurrentMonth) canReverse = false;

        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
            if (policy.getDtPaymentDate() != null) {
                //if (Tools.isYes(policy.getStReference12())) {
                    throw new RuntimeException("Polis Sudah Dibayar pada no bukti : "+ policy.getStReceiptNo());
                //}
            }
        }

        if (!canReverse) throw new RuntimeException("Tanggal Polis/DLA Tidak Valid (Sudah Tutup Produksi)");
        
//        getRemoteInsurance().reverse(policy);
        
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

        cekClosingStatus("INPUT");
        
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
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
    }
    
    public void editCreateRenewal(String policyID) throws Exception {
        
        superEdit(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
        if (!(FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) ||
                FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()))) {
            throw new RuntimeException("Polis Perpanjangan hanya bisa dibuat dari Polis, Endorsemen, History, dan Renewal");
        }
        
         if(policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)||
                policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND))
            throw new RuntimeException("Surety Bond dan Kontra Bank Garansi tidak bisa dibuat renewal");
        
        if(policy.getStAdminNotes()!=null)
            if(policy.getStAdminNotes().equalsIgnoreCase(FinCodec.PolicyStatus.RENEWAL))
                throw new RuntimeException("Polis Sudah Pernah dibuat Perpanjangan");
        
        //policy.generatePolicyNo();
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStParentPolicyNo(policy.getStPolicyNo());
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
        policy.setStRenewalCounter(policy.calculateRenewalCounter(policy.getStRenewalCounter()));
        policy.setStReinsuranceApprovedWho(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStEndorseNotes(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        
        if(policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE))
            policy.setStReference1(null);

        Date tanggalAwal = new Date();
        tanggalAwal = policy.getDtPeriodStart();
        
        final DTOList object = policy.getObjects();
        
        for (int j = 0; j < object.size(); j++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object.get(j);
            
            if(obj.getStVoidFlag()!=null) obj.setStVoidFlag(null);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("3") || policy.getStPolicyTypeID().equalsIgnoreCase("1") || policy.getStPolicyTypeID().equalsIgnoreCase("81")){
                if(obj.getDtReference1()!=null){
                    obj.setDtReference1(obj.getDtReference2());
                    obj.setDtReference2(null);
                    tanggalAwal = obj.getDtReference1();
                }

            }

            //set kode resiko baru
            obj.applyNewRiskCode(tanggalAwal);

//            if(policy.getStPolicyTypeID().equalsIgnoreCase("19"))
//                obj.setStReference2(null);
            
        }
        
        final DTOList detail = policy.getDetails();
        for (int i = 0; i < detail.size(); i++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) detail.get(i);
            
            if(!items.isAutoTaxRate()) items.setStTaxAutoRateFlag(null);
        }
        
        changeTreaty();
        defineTreaty();
        initTabs();
    }
    
    public void editCreateClaimPLA(String policyID) throws Exception {
        superEditPLA(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();
        
        policy.checkLastPolicyNo(policy.getStPolicyNo());
        
        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");
        
        if(blockPremiNotPaid)
            if(!policy.isPremiPaidInOldSystem())
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

        policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        //policy.setStReference3(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        //policy.setDtPaymentDate(null);
        //policy.setStPaymentNotes(null);

        /*
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
           
            policy.invokeCustomCriteria(obj);

            if(objx.getStOrderNo()==null){

                objx.setStOrderNo(String.valueOf(i+1));

                SQLUtil S = new SQLUtil();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ objx.getStOrderNo() +" where ins_pol_obj_id = ?");

                PS.setObject(1,obj.getStPolicyObjectID());

                int j = PS.executeUpdate();

                S.release();
            }
        }*/
        
        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            //item.setStROFlag("Y");
        }

        onChgCurrencyClaim();

        initTabs();
    }
    
    public void editCreateClaimDLA(String policyID) throws Exception {
        superEdit(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();
        
        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");
        
        
        if(blockPremiNotPaid)
            if(!policy.isPremiPaidInOldSystem())
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
        //policy.setStReference3(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        //policy.setStReference3(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        
        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            //item.setStROFlag("Y");
        }
        
        
        if (policy.getDbClaimAdvancePaymentAmount()!=null) {
            
            insItemID = getUangMukaKlaimInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setDbAmount(policy.getDbClaimAdvancePaymentAmount());
            
            item.setStChargableFlag("Y");
            
            item.setStROFlag("Y");
        }

        onChgCurrencyClaim();
        
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

        cekClosingStatus("INPUT");
        
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
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);

        /*
        if(policy.getStPolicyTypeID().equalsIgnoreCase("51") && policy.getStCostCenterCode().equalsIgnoreCase("24")){
            setEnableNoPolicy(true);
            policy.generatePolicyNoWithoutCounter();
        }
        */
        
        initTabs();
    }
    
    public void editCreateEndorse(String policyID) throws Exception {
        superEdit(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/
        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        
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

                if(cov.isEntryPremi()) cov.setStEntryPremiFlag(null);     
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
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            final DTOList coverage = obj.getCoverage();
            
            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                
                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                cov.setStEntryRateFlag("N");
                cov.setStAutoRateFlag("N");
                cov.setStEntryPremiFlag("Y");
            }
            
            final DTOList suminsureds = obj.getSuminsureds();
            
            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                
                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSERI);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        
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
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        
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

        cekClosingStatus("INPUT");
        
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

        cekClosingStatus("INPUT");

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
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReadyToApproveFlag(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setDbClaimAmountEndorse(policy.getDbClaimAmountApproved());
        policy.setStClaimPaymentUsedFlag(null);
        policy.setDtClaimPaymentDate(null);
        policy.setDtApprovedClaimDate(null);
        policy.setStEffectiveClaimFlag(null);
        policy.setStRiskApproved(null);
        policy.setStRiskApprovedWho(null);
        policy.setDtRiskApprovedDate(null);

        if (Tools.isYes(policy.getStReference13())) {
            policy.setDbClaimSubroPaid(new BigDecimal(100));
        }
        
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

        final DTOList detail = policy.getClaimItems();
        for (int i = 0; i < detail.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) detail.get(i);

            item.setDbAmount(null);

        }

        readTreatyFromPolisInduk();
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        
        initTabs();
        //
    }
    
    private String getClaimBrutoInsItemID() throws Exception {
        
        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='CLAIMG' and active_flag='Y'");
        
        return lu.getCode(0);
    }
    
    public BigDecimal getTransactionLimit(String cat, String cat2, String userID) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      c.refn"+ cat2 +
                    "   from " +
                    "         s_user_roles b " +
                    "         inner join ff_table c on c.fft_group_id = 'CAPA' and c.ref1 = b.role_id and c.ref2 = ? and c.ref3 = ?" +
                    "   where" +
                    " c.active_flag = 'Y' "+
                    " and b.user_id= ? and ref4 is null and (ref5 is null or ref5  = ?)"+
                    " and period_start <= ? and period_end >= ?"
                    + " order by ref5 limit 1");
            
            S.setParam(1,cat);
            S.setParam(2,policy.getStPolicyTypeID());
            S.setParam(3,userID);
            S.setParam(4,policy.getEntity().getStRef2());
            S.setParam(5,policy.getDtPeriodStart());
            S.setParam(6,policy.getDtPeriodStart());
            
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
                "   where b.ref3=? and b.ref2 in (select role_id from s_user_roles where user_id=?) and b.fft_group_id='COMM'" +
                "   and (b.ref5 = ? or b.ref5 is null) "+
                "   and active_flag = 'Y'"+
                "   order by ref5,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID(), policy.getStProducerID()},
                FlexTableView.class
                ).getDTO();
        
        return ft==null?null:ft.getDbReference1();
    }

    public BigDecimal getComissionLimitNew() throws Exception {
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 in (select role_id from s_user_roles where user_id=?) and b.fft_group_id='COMM'" +
                "   and (b.ref5 = ? or b.ref5 is null) "+ //--cari by pemasar
                "   and (b.ref4 = ? or b.ref4 is null) "+ //--cari by grup sumbis
                "   and (b.ref6 = ? or b.ref6 is null) "+ //--cari by jenis kredit
                "   and active_flag = 'Y'"+
                "   and period_start <= ? and period_end >= ?"+
                "   order by ref6,ref5,ref4,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID(), policy.getStProducerID(), policy.getEntity().getStRef2(), policy.getStKreasiTypeID(), policy.getDtPeriodStart(), policy.getDtPeriodStart()},
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
                "   where b.ref3=? and b.ref2 in (select role_id from s_user_roles where user_id=?) "
                + " and b.ref4 = ? and b.fft_group_id='COMM' " +
                " and active_flag = 'Y' "+
                " and period_start <= ? and period_end >= ? "+
                "      order by refn1 desc",
                new Object [] {policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID(), policy.getEntity().getStRef2(), policy.getDtPolicyDate(), policy.getDtPolicyDate()},
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
            if(policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE))
            {

                checkPassword();
                
                BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
                
                if(isApprovalByDirectorMode()){
                    transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"dirtek");
                }
                
                policy.validateTSILimit(transactionLimit);
                
                //final BigDecimal transactionLimitPerPrincipal = getTransactionLimit("ACCEPT_PRINCIPAL", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
                policy.validateAkumulasiResikoLimit(transactionLimit);
                
                BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);

                comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getPremiNetto()), policy.getDbPremiTotal(),4);

                BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew());

                boolean overLimit = false;
                
                String errorMessage="";
                
                overLimit = Tools.compare(comissionRatio, comissionLimit)>0;

                if(overLimit) errorMessage = "Jumlah Komisi/Diskon ("+ BDUtil.getPctFromRate(comissionRatio) +") Melebihi Kewenangan Anda ("+ BDUtil.getPctFromRate(comissionLimit) +")";


                if (overLimit){
                    policy.setStUnderwritingFinishFlag("N");
                    policy.setStEffectiveFlag("N");
                   // getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                    throw new RuntimeException(errorMessage);
                }
            }
        }
        
        if(policy.isStatusEndorse()) checkPassword();
        
        if (policy.isStatusPolicy() || policy.isStatusRenewal()) {
             
            checkPassword();

            validateDoubleInvoice();
            
            BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(), SessionManager.getInstance().getUserID());

            if(isApprovalByDirectorMode()){
                transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"dirtek");
            }

            if(isApprovalByDivisiMode()){
                transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"kadiv-uw");
            }

            //CEK LIMIT TSI PER OBJEK(RESIKO), BUKAN PER POLIS(TOTAL)
            if(isApprovalByDivisiMode() || isApprovalByDirectorMode()) policy.validateTSILimitByPejabat(transactionLimit);
            else policy.validateTSILimit(transactionLimit);
            
            //CEK LIMIT AKUMULASI PER PRINCIPAL
            //final BigDecimal transactionLimitPerPrincipal = getTransactionLimit("ACCEPT_PRINCIPAL", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
            policy.validateAkumulasiResikoLimit(transactionLimit);

            //CEK LIMIT KOMISI PER ITEM
            policy.checkDetailsLimit();

            //policy.validateObjectsApprove();
            
            BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);

            comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getPremiNetto()), policy.getDbPremiTotal(),4);
            
            BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew());

            if(isApprovalByDirectorMode()){
                comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew("dirtek"));
            }

            if(isApprovalByDivisiMode()){
                comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew("kadiv-uw"));
            }

            boolean overLimit = false;

            String errorMessage="";
            
            overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
            if(overLimit) errorMessage = "Jumlah Komisi/Diskon ("+ BDUtil.getPctFromRate(comissionRatio) +") Melebihi Kewenangan Anda ("+ BDUtil.getPctFromRate(comissionLimit) +")";

            if (overLimit){
                policy.setStUnderwritingFinishFlag("N");
                policy.setStEffectiveFlag("N");
//                getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                throw new RuntimeException(errorMessage);
            }
        }
        
        if (policy.isStatusClaimDLA()) {
            checkPassword();

            validateClaimItemApproval();
            
            final BigDecimal transactionLimit = getClaimLimit("CLAIM", "1");
            
            BigDecimal claimAMT = policy.getDbClaimAmount();
            
            if(!policy.isDirect()) claimAMT = policy.getClaimLimit();

            if(policy.isCoinsSelf()){
                final DTOList coin = policy.getCoins2();
                
                for (int i = 0; i < coin.size(); i++) {
                    InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coin.get(i);

                    if(!co.isAskrida()) continue;

                    if(co.isAskrida()) claimAMT = co.getDbClaimAmount();
                }
            }

            final boolean enoughLimit = Tools.compare(transactionLimit, claimAMT)>=0;
 
            if(!isApprovalByDirectorMode())
                if (!enoughLimit){
                    policy.setStClaimFinishFlag("N");
                    policy.setStEffectiveFlag("N");
                    //getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
         
                    throw new RuntimeException("Jumlah Klaim "+ JSPUtil.print(claimAMT) +" Melebihi Limit Anda "+ JSPUtil.print(transactionLimit));
                }
        }
        
        if(isApprovalByDirectorMode()){
            DTOList policyDocuments = policy.getPolicyDocuments();
            
            boolean check = false;
            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                
                if(doc.getStSelectedFlag().equalsIgnoreCase("Y") && doc.getStFilePhysic()!=null)
                    check = true;
            }
            
            if(!check)
                throw new RuntimeException("File Lampiran Persetujuan Harus Dilampirkan");
        }

        if(isApprovalByDivisiMode()){
            DTOList policyDocuments = policy.getPolicyDocuments();

            boolean check = false;
            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                if(doc.getStInsuranceDocumentTypeID().equalsIgnoreCase("95"))
                    if(doc.isMarked()&& doc.getStFilePhysic()!=null)
                        check = true;
            }

            if(!check)
                throw new RuntimeException("File Catatan Persetujuan Divisi Harus Dilampirkan");
        }
        
        policy.validate(true);
        
        if (policy.isStatusPolicy()|| policy.isStatusEndorse()||policy.isStatusClaimDLA()||policy.isStatusRenewal()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()||policy.isStatusInward())
            policy.setStPostedFlag("Y");
        
        if(policy.isShouldValidateApproval())
            if(!policy.isReadyToApprove()){
                approvalMode = true; 
                policy.setStEffectiveFlag(null);
                policy.setStPassword(null);
                throw new RuntimeException("Data belum siap untuk disetujui");
            }  
        
        policy.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        policy.setDtApprovedDate(new Date());
        policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
        policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());

        approvalMode = true;

        btnSave();
    }
    
    public void btnApproveViaReverse() throws Exception {
        policy.setExcludeReasMode(isExcludeReasMode());
        policy.setStEffectiveFlag("Y");
        
        if (policy.isStatusPolicy()||policy.isStatusRenewal()||policy.isStatusEndorse()||policy.isStatusEndorseRI()) {
            final BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
            
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
            final BigDecimal transactionLimit = getTransactionLimit("CLAIM", "1",SessionManager.getInstance().getUserID());
            
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
   
    
    public void edit(String policyID) throws Exception {
        superEdit(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");
        
        if (policy.isEffective()) throw new Exception("Data tidak bisa diubah karena sudah disetujui");
       
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
        final InsuranceSplitPolicyDeductibleView ded = new InsuranceSplitPolicyDeductibleView();

        ded.setStInsuranceClaimCauseID(deductibleAddID);

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

        final InsuranceTreatyDetailView detail = (InsuranceTreatyDetailView) getSelectedObjTreatyDetail().getTreatyDetail();

        if(detail.isBPDAN()){
            final InsuranceTreatySharesView member = (InsuranceTreatySharesView) detail.getShares().get(0);
            
            ri.setStMemberEntityID(member.getStMemberEntityID());
        }


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
    
    private InsurancePolicyTreatyDetailView getSelectedObjTreatyDetail() {
        return (InsurancePolicyTreatyDetailView)getSelectedObjTreaty().getDetails().get(idxTreatyDetail.intValue());
    }
    
    private InsurancePolicyTreatyView getSelectedObjTreaty() {
        return (InsurancePolicyTreatyView) selectedObject.getTreaties().get(idxTreaty.intValue());
    }
    
    public void approval(String policyID) throws Exception {
        edit(policyID);

        cekClosingStatus("APPROVE");
        
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
        
        boolean canNotClaim = policy.validateClaimObject(o);
        
        if(canNotClaim){
            stClaimObject = null;
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim sudah nol, tidak bisa dilakukan pembuatan klaim");
        }
            
        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());
        
            policy.setObjIndex(stClaimObject);
            
            final DTOList tsi = o.getSuminsureds();
            
            BigDecimal totalTSI = null;
            for (int i = 0; i < tsi.size(); i++) {
                InsurancePolicyTSIView suminsured = (InsurancePolicyTSIView) tsi.get(i);
                
                totalTSI = BDUtil.add(totalTSI, suminsured.getDbInsuredAmount());
            }
            
            policy.setDbClaimAmountEstimate(totalTSI);
        }

        readTreatyFromPolisInduk();
        
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

        if(!policy.getPolicyType().canUpload())
            throw new RuntimeException("Sementara Belum Tersedia Untuk Jenis Polis Tsb");
        
        policy.setLampiran(true);
        
        final DTOList konversiDocuments = policy.getKonversiDocuments();
        
        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
        doc.setStSelectedFlag("Y");

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
            //doDeleteAllObjects();
            
            final DTOList konversiDocuments = policy.getKonversiDocuments();
            
            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);
            
            String fileID = doc.getStFilePhysic();
            
            FileView file = FileManager.getInstance().getFile(fileID);
            
            FileInputStream fis = new FileInputStream(file.getStFilePath());
            
            final boolean inward = policy.getCoverSource().isInward();
            
            final String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");

            if("1".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadFire(fis,treatyID);

            if("2".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadKonsorsium(fis,treatyID);

             if("51".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadBidBond51(fis, treatyID);

            if("52".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadPerformanceBond52(fis, treatyID);

            if("53".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadAdvancepaymentBond53(fis, treatyID);

            if("54".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadMaintenanceBond54(fis, treatyID);

            if("2".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadFire(fis,treatyID);

            if("87".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadKreditMacet(fis,treatyID);

            if("88".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadKreditSerbaguna(fis,treatyID);

            /*
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
            
            
            
            if("19".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadEarthquake(fis,treatyID);
            
            if("12".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadBurglary(fis,treatyID);
            
            if("31".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadSipanda(fis,treatyID);
            
            if("32".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadSipanda(fis,treatyID);
            
            if("33".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadSipanda(fis,treatyID);
            
            if("59".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadKreditKonsumtif(fis,treatyID); //uploadKredit(fis,treatyID);

            if("6".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadCAR(fis,treatyID);

            if("7".equalsIgnoreCase(policy.getStPolicyTypeID()))
                uploadEAR(fis,treatyID);

            if ("13".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadMC(fis, treatyID);
            }

            if ("64".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadTravel(fis, treatyID);
            }

            if ("72".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadEarthquake(fis, treatyID);
            }

            if ("23".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadTerrorismSabotage(fis, treatyID);
            }

            if ("85".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadKPR(fis, treatyID);
            }

            if ("8".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadMB(fis, treatyID);
            }
            
            if ("97".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadTP(fis, treatyID);
            }

            if ("51".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadSurety(fis, treatyID);
            }

            if ("52".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadSurety(fis, treatyID);
            }

            if ("53".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadSurety(fis, treatyID);
            }

            if ("54".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadSurety(fis, treatyID);
            }

            if ("9".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadCPME(fis, treatyID);
            }

            if ("10".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadEEI(fis, treatyID);
            }

            if ("73".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadKreditKMK(fis, treatyID);
            }

            if ("80".equalsIgnoreCase(policy.getStPolicyTypeID())) {
                uploadKreditProduktif(fis, treatyID);
            }
            */
            
            defineTreaty();
            
        } catch (Exception e) {
            //doDeleteAllObjects();
            throw new RuntimeException("error upload excel= "+ e);
        }
    }
    
    public void doNewLampiranObject() throws Exception {
        final InsuranceSplitPolicyObjectView o = (InsuranceSplitPolicyObjectView) policy.getClObjectClass().newInstance();
        o.markNew();
        o.setPolicy(policy);
        o.setCoverage(new DTOList());
        
        o.setSuminsureds(new DTOList());
        o.setDeductibles(new DTOList());
        
        getObjects().add(o);
        
        InsuranceSplitPolicyObjDefaultView obj = (InsuranceSplitPolicyObjDefaultView) o;
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
            
            HSSFCell cellControl  = row.getCell(0);
            if(cellControl==null) break;
            
            doNewLampiranObject();
            
            HSSFCell cell1  = row.getCell(1);//nama
            HSSFCell cell2  = row.getCell(2);//tgl lahir
            HSSFCell cell3  = row.getCell(5);//tgl awal
            HSSFCell cell4  = row.getCell(6);//tgl akhir
            HSSFCell cell5  = row.getCell(8);//tsi
            HSSFCell cell6  = row.getCell(10);//premi
            HSSFCell cell7  = row.getCell(9);//rate
            HSSFCell cell8  = row.getCell(3);//usia
            
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
            HSSFCell cellCoverPA  = row.getCell(20);
            if(cellCoverPA.getCellType()!=cellCoverPA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            HSSFCell cellRatePA  = row.getCell(18);
            if(cellRatePA.getCellType()!=cellRatePA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
            
            BigDecimal ratePA2 = null;
            if(cell7!=null){
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }
            
            HSSFCell cellPremiPA  = row.getCell(19);
            if(cellPremiPA.getCellType()!=cellPremiPA.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");
            
            BigDecimal premiPA2 = null;
            if(cell7==null){
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }
            
            doAddLampiranCoverKreasi(cvptID3,ratePA2,premiPA2);
            
            //add cover PHK
            HSSFCell cellCoverPHK  = row.getCell(25);
            if(cellCoverPHK!=null){

                //if(cellCoverPHK.getCellType()()!=1){
                if(cellCoverPHK.getCellType()!=cellCoverPHK.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom CvptID Harus Numeric");

                
                double cvptPHK = cellCoverPHK.getNumericCellValue();
                BigDecimal cvptPHK2 = new BigDecimal(cvptPHK);
                String cvptPHK3 = cvptPHK2.toString();

                if(cvptPHK != 0){
                    HSSFCell cellRatePHK  = row.getCell(23);
                    if(cellRatePHK.getCellType()!=cellRatePHK.CELL_TYPE_NUMERIC)
                        throw new RuntimeException("Kolom Rate PHK Harus Numeric");

                    BigDecimal ratePHK2 = null;
                    if(cell7!=null){
                        double ratePHK = cellRatePHK.getNumericCellValue();
                        ratePHK2 = new BigDecimal(ratePHK);
                    }

                    HSSFCell cellPremiPHK  = row.getCell(24);
                    if(cellPremiPHK.getCellType()!=cellPremiPHK.CELL_TYPE_NUMERIC)
                        throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");

                    BigDecimal premiPHK2 = null;
                    if(cell7==null){
                        double premiPHK = cellPremiPHK.getNumericCellValue();
                        premiPHK2 = new BigDecimal(premiPHK);
                    }

                    doAddLampiranCoverKreasi(cvptPHK3,ratePHK2,premiPHK2);
                }
                
                
                //}
            }
            
            //paw 30
            HSSFCell cellCoverPAW  = row.getCell(30);
            if(cellCoverPAW!=null){
                //if(cellCoverPAW.getCellType()()!=1){
                //if(true)	throw new RuntimeException("PAW= "+ cellCoverPAW.);
                if(cellCoverPAW.getCellType()!=cellCoverPAW.CELL_TYPE_NUMERIC)
                    throw new RuntimeException("Kolom CvptID Harus Numeric");
                
                double cvptPAW = cellCoverPAW.getNumericCellValue();
                BigDecimal cvptPAW2 = new BigDecimal(cvptPAW);
                String cvptPAW3 = cvptPAW2.toString();

                if(cvptPAW != 0){
                    HSSFCell cellRatePAW  = row.getCell(28);
                    if(cellRatePAW.getCellType()!=cellRatePAW.CELL_TYPE_NUMERIC)
                        throw new RuntimeException("Kolom Rate PAW Harus Numeric");

                    BigDecimal ratePAW2 = null;
                    if(cell7!=null){
                        double ratePAW = cellRatePAW.getNumericCellValue();
                        ratePAW2 = new BigDecimal(ratePAW);
                    }

                    HSSFCell cellPremiPAW  = row.getCell(29);
                    if(cellPremiPAW.getCellType()!=cellPremiPAW.CELL_TYPE_NUMERIC)
                        throw new RuntimeException("Kolom Rate PA Kreasi Harus Numeric");

                    BigDecimal premiPAW2 = null;
                    if(cell7==null){
                        double premiPAW = cellPremiPAW.getNumericCellValue();
                        premiPAW2 = new BigDecimal(premiPAW);
                    }

                    doAddLampiranCoverKreasi(cvptPAW3,ratePAW2,premiPAW2);
                }
                
                
                //}
            }
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }
        
        
    }
    
    private void uploadCIT(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Uploads");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Uploads Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(0);//transit dari (String)
            HSSFCell cell2 = row.getCell(1);//transit ke (String)
            HSSFCell cell3 = row.getCell(2);//tgl transit (date)
            HSSFCell cell4 = row.getCell(3);//waktu transit (String)
            HSSFCell cell5 = row.getCell(4);//no deklarasi (String)
            HSSFCell cell6 = row.getCell(5);//diangkut dengan	(String)
            HSSFCell cell7 = row.getCell(6);//petugas (String)
            HSSFCell cell8 = row.getCell(7);//debitur (String)
            HSSFCell cell9 = row.getCell(14);//pengawal
            HSSFCell cell10 = row.getCell(15);//petugas bank

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            }

            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Transit Ke Harus String/Character");
            }

            if (cell4.getCellType() != cell4.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Waktu Transit Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom no deklarasi Harus String/Character");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom diangkut dengan Harus String/Character");
            }

            if (cell6.getCellType() != cell7.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom petugas Harus String/Character");
            }

            if (cell6.getCellType() != cell8.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom debitur Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setDtReference1(cell3.getDateCellValue()); //tgl lahir
            getSelectedDefaultObject().setStReference3(cell4.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell5.getStringCellValue());
            getSelectedDefaultObject().setStReference5(cell6.getStringCellValue());
            getSelectedDefaultObject().setStReference6(cell7.getStringCellValue());
            getSelectedDefaultObject().setStReference7(cell8.getStringCellValue());

            getSelectedDefaultObject().setStReference8(cell9.getStringCellValue());
            getSelectedDefaultObject().setStReference9(cell10.getStringCellValue());
            //getSelectedDefaultObject().setStRiskCategoryID("537");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);


            //add coverkreasi
            HSSFCell cellCoverCIT = row.getCell(13);//cvptid
            if (cellCoverCIT.getCellType() != cellCoverCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            //get sum insured
            HSSFCell cellTSICIT = row.getCell(10);//get rate cover
            if (cellTSICIT.getCellType() != cellTSICIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            }

            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            //

            HSSFCell cellRateCIT = row.getCell(11);//get rate cover
            if (cellRateCIT != null) {
                if (cellRateCIT.getCellType() != cellRateCIT.CELL_TYPE_NUMERIC) {
                    throw new RuntimeException("Kolom Rate CIT Harus Numeric");
                }
            }

            BigDecimal ratePA2 = null;
            if (cellRateCIT != null) {
                double ratePA = cellRateCIT.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            //premi
            HSSFCell cellPremiCIT = row.getCell(12);//get rate cover
            if (cellPremiCIT.getCellType() != cellPremiCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi CIT Harus Numeric");
            }

            double PremiPA = cellPremiCIT.getNumericCellValue();
            BigDecimal PremiPA2 = new BigDecimal(PremiPA);

            doAddLampiranSumInsured("182", tsiCIT2);
            doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadCIS(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");
        HSSFSheet sheetDoc = wb.getSheet("UploadDocument");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        //if (sheet == null && sheetDoc == null) {
        //    throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        //}

        //jika cis biasa
        if(sheet!=null){
            
            int rows = sheet.getPhysicalNumberOfRows();
            for (int r = 2; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);

                //HSSFCell cellControl  = row.getCell(0);
                //if(cellControl==null) break;

                doNewLampiranObject();


                /*
                 *  BISNIS
                BUATAN
                ALAMAT RESIKO
                KONDISI/SITUASI
                TANGGAL
                 */

                HSSFCell cell1 = row.getCell(0);//BISNIS (String)
                HSSFCell cell2 = row.getCell(1);//BUATAN (String)
                HSSFCell cell3 = row.getCell(2);//ALAMAT RESIKO (String)
                HSSFCell cell4 = row.getCell(3);//KONDISI/SITUASI (String)
                HSSFCell cell5 = row.getCell(4);//TANGGAL (date)
                HSSFCell cell6 = row.getCell(5);//TANGGAL (date)

                HSSFCell cellPeriodRate = row.getCell(20);//period rate
                HSSFCell cellPeriodBaseID = row.getCell(21);//period base
                HSSFCell cellPremiumFactor = row.getCell(22);//premium factor

                if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom BISNIS Harus String/Character");
                }

                if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom BUATAN Harus String/Character");
                }

                if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom ALAMAT RESIKO Harus String/Character");
                }

                if (cell4.getCellType() != cell4.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom KONDISI/SITUASI Harus String/Character");
                }


                getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
                getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
                getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());
                getSelectedDefaultObject().setStReference4(cell4.getStringCellValue());

                if (cell5 != null) {
                    getSelectedDefaultObject().setDtReference1(cell5.getDateCellValue());
                }

                if (cell6 != null) {
                    getSelectedDefaultObject().setDtReference2(cell6.getDateCellValue());
                }

                if(cellPeriodRate!=null){
                    getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                    //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                    getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType()==cellPeriodRate.CELL_TYPE_STRING?new BigDecimal(cellPeriodRate.getStringCellValue()):new BigDecimal(cellPeriodRate.getNumericCellValue()));
                }


                if(cellPeriodBaseID!=null)
                    getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType()==cellPeriodBaseID.CELL_TYPE_STRING?cellPeriodBaseID.getStringCellValue():new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());

                if(cellPremiumFactor!=null)
                    getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType()==cellPremiumFactor.CELL_TYPE_STRING?cellPremiumFactor.getStringCellValue():new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());


                //getSelectedDefaultObject().setStRiskCategoryID("536");
                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                /*
                HSSFCell cellCoverCIT = row.getCell(10);//cvptid
                if (cellCoverCIT.getCellType() != cellCoverCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
                }

                double cvptID = cellCoverCIT.getNumericCellValue();
                BigDecimal cvptID2 = new BigDecimal(cvptID);
                String cvptID3 = cvptID2.toString();

                HSSFCell cellRateCIT = row.getCell(8);//get rate
                if (cellRateCIT != null) {
                if (cellRateCIT.getCellType() != cellRateCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate CIT Harus Numeric");
                }
                }

                BigDecimal ratePA2 = null;

                if (cellRateCIT != null) {
                double ratePA = cellRateCIT.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
                }

                HSSFCell cellPremiCIT = row.getCell(9);//get premi
                if (cellPremiCIT.getCellType() != cellPremiCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi CIT Harus Numeric");
                }

                double PremiPA = cellPremiCIT.getNumericCellValue();
                BigDecimal PremiPA2 = new BigDecimal(PremiPA);*/

                HSSFCell cellTSICIT = row.getCell(9);//get rate cover
                if (cellTSICIT.getCellType() != cellTSICIT.CELL_TYPE_NUMERIC) {
                    throw new RuntimeException("Kolom TSI CIT Harus Numeric");
                }

                double tsiCIT = cellTSICIT.getNumericCellValue();
                BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);

                doAddLampiranSumInsured("42", tsiCIT2);
                //doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
                getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

                //cover 1
                HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(7);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID1 = row.getCell(8);//RATE
                HSSFCell cell_Rate1 = row.getCell(10);//premi
                HSSFCell cell_Premi1 = row.getCell(11);//tsi
                //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

                final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
                final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

                if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                    }
                } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
                }

                //cover 2
                HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(12);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID2 = row.getCell(13);//RATE
                HSSFCell cell_Rate2 = row.getCell(14);//premi
                HSSFCell cell_Premi2 = row.getCell(15);//tsi
                //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

                final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
                final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

                if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                    }
                } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
                }

                //cover 3
                HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(16);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID3 = row.getCell(17);//RATE
                HSSFCell cell_Rate3 = row.getCell(18);//premi
                HSSFCell cell_Premi3 = row.getCell(19);//tsi
                //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

                final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
                final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

                if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                    }
                } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
                }

            }
        }

        //cis dokumen
        if(sheetDoc!=null){

            int rows = sheetDoc.getPhysicalNumberOfRows();

            for (int r = 2; r < rows; r++) {
                HSSFRow row = sheetDoc.getRow(r);

                //HSSFCell cellControl  = row.getCell(0);
                //if(cellControl==null) break;

                doNewLampiranObject();


                /*
                 *  BISNIS
                BUATAN
                ALAMAT RESIKO
                KONDISI/SITUASI
                TANGGAL
                 */

                HSSFCell cell1 = row.getCell(0);//BISNIS (String)
                HSSFCell cell2 = row.getCell(1);//BUATAN (String)
                HSSFCell cell3 = row.getCell(2);//ALAMAT RESIKO (String)
                HSSFCell cell4 = row.getCell(3);//KONDISI/SITUASI (String)
                HSSFCell cell5 = row.getCell(4);//TANGGAL (date)
                HSSFCell cell6 = row.getCell(5);//TANGGAL (date)
                HSSFCell cellDeskripsiTSI = row.getCell(23);//TANGGAL (date)
                HSSFCell cellPenjelasanTSI = row.getCell(24);//TANGGAL (date)
                HSSFCell cellDebitur = row.getCell(25);//TANGGAL (date)

                HSSFCell cellPeriodRate = row.getCell(20);//period rate
                HSSFCell cellPeriodBaseID = row.getCell(21);//period base
                HSSFCell cellPremiumFactor = row.getCell(22);//premium factor

                if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom BISNIS Harus String/Character");
                }

                if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom BUATAN Harus String/Character");
                }

                if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom ALAMAT RESIKO Harus String/Character");
                }

                if (cell4.getCellType() != cell4.CELL_TYPE_STRING) {
                    throw new RuntimeException("Kolom KONDISI/SITUASI Harus String/Character");
                }


                getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
                getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
                getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());
                getSelectedDefaultObject().setStReference4(cell4.getStringCellValue());
                getSelectedDefaultObject().setStReference5(cellDebitur.getStringCellValue());

                if (cell5 != null) {
                    getSelectedDefaultObject().setDtReference1(cell5.getDateCellValue());
                }

                if (cell6 != null) {
                    getSelectedDefaultObject().setDtReference2(cell6.getDateCellValue());
                }

                if(cellPeriodRate!=null){
                    getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                    //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                    getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType()==cellPeriodRate.CELL_TYPE_STRING?new BigDecimal(cellPeriodRate.getStringCellValue()):new BigDecimal(cellPeriodRate.getNumericCellValue()));
                }


                if(cellPeriodBaseID!=null)
                    getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType()==cellPeriodBaseID.CELL_TYPE_STRING?cellPeriodBaseID.getStringCellValue():new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());

                if(cellPremiumFactor!=null)
                    getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType()==cellPremiumFactor.CELL_TYPE_STRING?cellPremiumFactor.getStringCellValue():new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());


                //getSelectedDefaultObject().setStRiskCategoryID("536");
                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);


                HSSFCell cellTSICIT = row.getCell(9);//get rate cover
                if (cellTSICIT.getCellType() != cellTSICIT.CELL_TYPE_NUMERIC) {
                    throw new RuntimeException("Kolom TSI CIT Harus Numeric");
                }

                double tsiCIT = cellTSICIT.getNumericCellValue();
                BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);

                doAddLampiranSumInsured("42", tsiCIT2, cellDeskripsiTSI.getStringCellValue(), cellPenjelasanTSI.getStringCellValue());

                //doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
                getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

                //cover 1
                HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(7);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID1 = row.getCell(8);//RATE
                HSSFCell cell_Rate1 = row.getCell(10);//premi
                HSSFCell cell_Premi1 = row.getCell(11);//tsi
                //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

                final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
                final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

                if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                    }
                } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
                }

                //cover 2
                HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(12);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID2 = row.getCell(13);//RATE
                HSSFCell cell_Rate2 = row.getCell(14);//premi
                HSSFCell cell_Premi2 = row.getCell(15);//tsi
                //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

                final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
                final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

                if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                    }
                } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
                }

                //cover 3
                HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(16);//ins_cover_id
                HSSFCell cell_Ins_Cover_ID3 = row.getCell(17);//RATE
                HSSFCell cell_Rate3 = row.getCell(18);//premi
                HSSFCell cell_Premi3 = row.getCell(19);//tsi
                //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

                final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
                final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

                if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                    if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                        doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                    }
                } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                    doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
                }

            }
        }

        
    }

    private void uploadCMCIT(FileInputStream fis) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UploadCM2020");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet UploadCM2020 Tidak Ditemukan, Download Format konversi terbaru di menu Download");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(0);//transit dari (String)
            HSSFCell cell2 = row.getCell(1);//transit ke (String)
            HSSFCell cell3 = row.getCell(2);//tgl transit (date)
            HSSFCell cell4 = row.getCell(3);//waktu transit (String)
            HSSFCell cell5 = row.getCell(4);//no deklarasi (String)
            HSSFCell cell6 = row.getCell(5);//diangkut dengan	(String)
            HSSFCell cell7 = row.getCell(12);//TANGGAL (date)
            HSSFCell cellTSIOR = row.getCell(16);
            HSSFCell cellTSILimit1 = row.getCell(17);
            HSSFCell cellTSILimit2 = row.getCell(18);
            HSSFCell cellTSILimitFac = row.getCell(19);
            HSSFCell cellProrataHari = row.getCell(6);
            HSSFCell cellPremi = row.getCell(10);

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Transit Dari Harus String/Character");
            }

            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom transit ke Harus String/Character");
            }

            if (cell4.getCellType() != cell4.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom waktu transit Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom no deklarasi Harus String/Character");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom diangkut dengan Harus String/Character");
            }

            /*
            if(cellTSIOR.getCellType()!=2)
            throw new RuntimeException("Formula TOTAL TSI PER RISK di sheet UPLOAD belum di copy");

            if(cellTSILimit1.getCellType()!=2)
            throw new RuntimeException("Formula LIMIT 1 di sheet UPLOAD belum di copy");

            if(cellTSILimit2.getCellType()!=2)
            throw new RuntimeException("Formula LIMIT 2 di sheet UPLOAD belum di copy");

            if(cellTSILimitFac.getCellType()!=2)
            throw new RuntimeException("Formula FAC di sheet UPLOAD belum di copy");*/

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setDtReference1(cell3.getDateCellValue()); //tgl lahir
            getSelectedDefaultObject().setStReference3(cell4.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell5.getStringCellValue());
            getSelectedDefaultObject().setStReference5(cell6.getStringCellValue());
            getSelectedDefaultObject().setStReference6(cell7.getStringCellValue());

            getSelectedDefaultObject().setDbReference1(new BigDecimal(cellTSIOR.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference2(new BigDecimal(cellTSILimit1.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference3(new BigDecimal(cellTSILimit2.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference4(new BigDecimal(cellTSILimitFac.getNumericCellValue()));

            //if(cellTSIOR.getCellType()!=cellTSIOR.);
            //logger.logDebug(">>>>>>>>>>>>>>>> " + cell1.getStringCellValue() + " >>>> TSI OR : " + cellTSIOR.getNumericCellValue() + " >>> " + cellTSIOR.getCellType());

            //getSelectedDefaultObject().setStRiskCategoryID("537");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverCIT = row.getCell(11);//cvptid
            if (cellCoverCIT.getCellType() != cellCoverCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            //get sum insured
            HSSFCell cellTSICIT = row.getCell(8);//get rate cover
            if (cellTSICIT.getCellType() != cellTSICIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            }

            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            //

            HSSFCell cellRateCIT = row.getCell(9);//get rate cover
            if (cellRateCIT.getCellType() != cellRateCIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate CIT Harus Numeric");
            }

            double ratePA = cellRateCIT.getNumericCellValue();
            BigDecimal ratePA2 = new BigDecimal(ratePA);

            double premi = cellPremi.getNumericCellValue();
            BigDecimal premi2 = new BigDecimal(premi);

            double hari = cellProrataHari.getNumericCellValue();

            if (hari != 0) {
                ratePA2 = null;
            }

            doAddLampiranSumInsured("475", tsiCIT2);
            doAddLampiranCoverKreasi(cvptID3, ratePA2, premi2);

//            setCMTreaty(getSelectedDefaultObject());

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
        
        doDeleteAllObjects();
        
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
            if(kreasi.getStKoasuransi()!=null) getSelectedDefaultObject().setStReference8(kreasi.getStKoasuransi());
            
            if(i==0){
                policy.setDtPolicyDate(kreasi.getDtTanggalCair());
                policy.setDtPeriodStart(kreasi.getDtTanggalCair());
                stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());
                if(kreasi.getStKoasuransi()!=null){
                    policy.setStCoinsID(kreasi.getStKoasuransi());
                    policy.setStCoinsName(policy.getEntity2(kreasi.getStKoasuransi()).getStEntityName());
                }
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
        policy.setStReference2Desc(parent.getStPolicyNo());
    }
    
    public void onNewDetailAutomatic(InsurancePolicyView policy, String insItemID, String stReadOnly, BigDecimal amount) throws Exception {
        
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

        if(!BDUtil.isZeroOrNull(amount))
            item.setDbAmount(amount);
        
        policy.getDetails().add(item);
        
    }
    
    public void addBiayaPolisMateraiAutomatic(InsurancePolicyView policy, BigDecimal biayaPolis, BigDecimal materai)throws Exception{
        final String coverType = policy.getStCoverTypeCode();
        
        if(policy.isStatusHistory()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()||policy.isStatusInward()) return;
        
        if(coverType.equalsIgnoreCase("DIRECT")){

            if(!BDUtil.isZeroOrNull(biayaPolis))
                onNewDetailAutomatic(policy,"15","N",biayaPolis);

            if(!BDUtil.isZeroOrNull(materai))
                onNewDetailAutomatic(policy,"14","N",materai);
        }
        
        if(coverType.equalsIgnoreCase("COINSOUT")){

            if(!BDUtil.isZeroOrNull(biayaPolis))
                onNewDetailAutomatic(policy,"22","N",biayaPolis);

            if(!BDUtil.isZeroOrNull(materai))
                onNewDetailAutomatic(policy,"21","N",materai);
        }
        
        if(coverType.equalsIgnoreCase("COINSIN")){

            if(!BDUtil.isZeroOrNull(biayaPolis))
                onNewDetailAutomatic(policy,"29","N",biayaPolis);

            if(!BDUtil.isZeroOrNull(materai))
                onNewDetailAutomatic(policy,"28","N",materai);
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
                
                HSSFCell cellPolId  = row.getCell(0);//pol_id
                
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
            
            HSSFCell cell1  = row.getCell(0);//BISNIS (String)
            HSSFCell cell2  = row.getCell(1);//BUATAN (String)
            HSSFCell cell3  = row.getCell(2);//ALAMAT RESIKO (String)
            HSSFCell cell4  = row.getCell(3);//KONDISI/SITUASI (String)
            HSSFCell cell5  = row.getCell(4);//TANGGAL (date)
            //HSSFCell cell6  = row.getCell(12);//TANGGAL (date)
            
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
            //getSelectedDefaultObject().setStReference6(cell6.getStringCellValue());
            
            if(cell5!=null)
                getSelectedDefaultObject().setDtReference1(cell5.getDateCellValue());
            
            //getSelectedDefaultObject().setStRiskCategoryID("536");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            HSSFCell cellCoverCIT  = row.getCell(10);//cvptid
            if(cellCoverCIT.getCellType()!=cellCoverCIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            
            double cvptID = cellCoverCIT.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();
            
            
            HSSFCell cellTSICIT  = row.getCell(7);//get rate cover
            if(cellTSICIT.getCellType()!=cellTSICIT.CELL_TYPE_NUMERIC)
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            
            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);
            
            
            HSSFCell cellRateCIT  = row.getCell(8);//get rate cover
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

    public void createNewSPPA() throws Exception {
        policy = new InsuranceSplitPolicyView();
        
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
            
            HSSFCell cellControl  = row.getCell(0);
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
            
            HSSFCell cell1  = row.getCell(1);//no polisi
            HSSFCell cell2  = row.getCell(2);//merk/jenis
            HSSFCell cell3  = row.getCell(3);//Tahun pembuatan
            HSSFCell cell4  = row.getCell(4);//No rangka
            HSSFCell cell5  = row.getCell(5);//No Mesin
            HSSFCell cell6  = row.getCell(6);//Tempat duduk
            HSSFCell cell7  = row.getCell(7);//Digunakan
            HSSFCell cell8  = row.getCell(8);//Tipe
            HSSFCell cell9  = row.getCell(9);//nama
            HSSFCell cell10  = row.getCell(10);//Kode Resiko
            HSSFCell cell11  = row.getCell(11);//Kode Resiko
            HSSFCell cell12  = row.getCell(12);//Kode Resiko
            HSSFCell cell13  = row.getCell(13);//Kode Resiko
            HSSFCell cell14  = row.getCell(14);//Kode Resiko
            HSSFCell cell15  = row.getCell(15);//Kode Resiko
            HSSFCell cell16  = row.getCell(16);//Kode Resiko
            HSSFCell cell17  = row.getCell(17);//Kode Resiko
            
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
            HSSFCell cell18  = row.getCell(18);//ins_tcpt_id
            HSSFCell cell19  = row.getCell(19);//ins_tsi_cat_id
            HSSFCell cell20  = row.getCell(20);//tsi
            
            if(cell18!=null)
                doAddLampiranSumInsured(cell18.getCellType()==cell18.CELL_TYPE_STRING?cell18.getStringCellValue():new BigDecimal(cell18.getNumericCellValue()).toString(), new BigDecimal(cell20.getNumericCellValue()));
            
            //cover meninggal
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(25);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(21);//RATE
            HSSFCell cell_Rate1  = row.getCell(22);//premi
            HSSFCell cell_Premi1  = row.getCell(23);//tsi
            HSSFCell cell_TSI1  = row.getCell(24);//ins_cvpt_id
            
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
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(30);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(26);//RATE
            HSSFCell cell_Rate2  = row.getCell(27);//premi
            HSSFCell cell_Premi2  = row.getCell(28);//tsi
            HSSFCell cell_TSI2  = row.getCell(29);//ins_cvpt_id
            
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
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(35);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(31);//RATE
            HSSFCell cell_Rate3  = row.getCell(32);//premi
            HSSFCell cell_Premi3  = row.getCell(33);//tsi
            HSSFCell cell_TSI3  = row.getCell(34);//ins_cvpt_id
            
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
            HSSFCell cell_Ins_Cvpt_ID4  = row.getCell(40);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4  = row.getCell(36);//RATE
            HSSFCell cell_Rate4  = row.getCell(37);//premi
            HSSFCell cell_Premi4  = row.getCell(38);//tsi
            HSSFCell cell_TSI4  = row.getCell(39);//ins_cvpt_id
            
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
            HSSFCell cell_Ins_Cvpt_ID5  = row.getCell(45);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID5  = row.getCell(41);//RATE
            HSSFCell cell_Rate5  = row.getCell(42);//premi
            HSSFCell cell_Premi5  = row.getCell(43);//tsi
            HSSFCell cell_TSI5  = row.getCell(44);//ins_cvpt_id
            
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
        
        final InsuranceSplitPolicyCoverView cv = new InsuranceSplitPolicyCoverView();
        
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
    
   private void uploadPAR(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            //if(cellControl==null) break;

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

            HSSFCell cell1 = row.getCell(1);//penggunaan
            HSSFCell cell2 = row.getCell(2);//kelas kontruksi
            HSSFCell cell3 = row.getCell(3);//kode resiko
            HSSFCell cell5 = row.getCell(5);//alamat
            HSSFCell cell6 = row.getCell(6);//kode pos
            HSSFCell cell8 = row.getCell(8);//nama

            HSSFCell cellKodeResiko1 = row.getCell(9);//kode resiko 1
            HSSFCell cellKodeResiko2 = row.getCell(10);//kode resiko 2
            HSSFCell cellKodeResiko3 = row.getCell(11);//kode resiko 3

            HSSFCell cellNONota = row.getCell(14);//no nota debet
            HSSFCell cellNOKPAK = row.getCell(15);//no kpak
            HSSFCell cellProvinsi = row.getCell(16);//provinsi

            HSSFCell cellPeriodStart = row.getCell(45);//periode awal
            HSSFCell cellPeriodEnd = row.getCell(46);//periode akhir
            HSSFCell cell4 = row.getCell(49);//penerangan

            HSSFCell cellPeriodRate = row.getCell(17);//period rate
            HSSFCell cellPeriodBaseID = row.getCell(73);//period base
            HSSFCell cellPremiumFactor = row.getCell(74);//premium factor


            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

            String kodePos = policy.getStKodePos(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            if (kodePos != null) {
                getSelectedDefaultObject().setStReference9(kodePos);
            }
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());

            //getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());

            if (cellPeriodStart != null) {
                if (cellPeriodStart.getCellType() == cellPeriodStart.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
                }
            }

            if (cellPeriodEnd != null) {
                if (cellPeriodEnd.getCellType() == cellPeriodEnd.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
                }
            }

            if (cellKodeResiko1 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode1(cellKodeResiko1.getCellType() == cellKodeResiko1.CELL_TYPE_STRING ? cellKodeResiko1.getStringCellValue() : new BigDecimal(cellKodeResiko1.getNumericCellValue()).toString());
            }

            if (cellKodeResiko2 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode2(cellKodeResiko2.getCellType() == cellKodeResiko2.CELL_TYPE_STRING ? cellKodeResiko2.getStringCellValue() : new BigDecimal(cellKodeResiko2.getNumericCellValue()).toString());
            }

            if (cellKodeResiko3 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode3(cellKodeResiko3.getCellType() == cellKodeResiko3.CELL_TYPE_STRING ? cellKodeResiko3.getStringCellValue() : new BigDecimal(cellKodeResiko3.getNumericCellValue()).toString());
            }

            if (cellNONota != null) {
                getSelectedDefaultObject().setStReference14(cellNONota.getStringCellValue());
            }

            if (cellNOKPAK != null) {
                getSelectedDefaultObject().setStReference15(cellNOKPAK.getStringCellValue());
            }

            if (cellProvinsi != null) {
                getSelectedDefaultObject().setStReference16(cellProvinsi.getStringCellValue());
            }

            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }


            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(51);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(52);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC1 = row.getCell(21);//tsi desc
            HSSFCell cell_TSI1 = row.getCell(22);//tsi
            HSSFCell cell_TSI1CoMem = row.getCell(23);//tsi100

            String tsiDesc1 = cell_TSI_DESC1 != null ? cell_TSI_DESC1.getStringCellValue() : null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), new BigDecimal(cell_TSI1CoMem.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), new BigDecimal(cell_TSI1CoMem.getNumericCellValue()), tsiDesc1);
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(53);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(54);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC2 = row.getCell(25);//tsi desc
            HSSFCell cell_TSI2 = row.getCell(26);//tsi
            HSSFCell cell_TSI2CoMem = row.getCell(27);//tsi100

            String tsiDesc2 = cell_TSI_DESC2 != null ? cell_TSI_DESC2.getStringCellValue() : null;

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()), new BigDecimal(cell_TSI2CoMem.getNumericCellValue()), tsiDesc2);
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()), new BigDecimal(cell_TSI2CoMem.getNumericCellValue()), tsiDesc2);
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(55);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(56);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC3 = row.getCell(29);//tsi desc
            HSSFCell cell_TSI3 = row.getCell(30);//tsi
            HSSFCell cell_TSI3CoMem = row.getCell(31);//tsi100

            String tsiDesc3 = cell_TSI_DESC3 != null ? cell_TSI_DESC3.getStringCellValue() : null;


            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()), new BigDecimal(cell_TSI3CoMem.getNumericCellValue()), tsiDesc3);
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()), new BigDecimal(cell_TSI3CoMem.getNumericCellValue()), tsiDesc3);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(57);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(58);//RATE
            HSSFCell cell_Rate1 = row.getCell(59);//premi
            HSSFCell cell_Premi1 = row.getCell(60);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(61);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(62);//RATE
            HSSFCell cell_Rate2 = row.getCell(63);//premi
            HSSFCell cell_Premi2 = row.getCell(64);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate2 != null) {
                rate2 = rate2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(65);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(66);//RATE
            HSSFCell cell_Rate3 = row.getCell(67);//premi
            HSSFCell cell_Premi3 = row.getCell(68);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (premi3 != null) {
                premi3 = premi3.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate3 != null) {
                rate3 = rate3.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //cover 4
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(69);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(70);//RATE
            HSSFCell cell_Rate4 = row.getCell(71);//premi
            HSSFCell cell_Premi4 = row.getCell(72);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());

            if (premi4 != null) {
                premi4 = premi4.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate4 != null) {
                rate4 = rate4.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, null);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadFire(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cell1 = row.getCell(1);//penggunaan
            HSSFCell cell2 = row.getCell(2);//kelas kontruksi
            HSSFCell cell3 = row.getCell(3);//kode resiko
            HSSFCell cell5 = row.getCell(5);//alamat
            HSSFCell cell6 = row.getCell(6);//kode pos
            HSSFCell cell8 = row.getCell(8);//nama

            HSSFCell cellKodeResiko1 = row.getCell(9);//kode resiko 1
            HSSFCell cellKodeResiko2 = row.getCell(10);//kode resiko 2
            HSSFCell cellKodeResiko3 = row.getCell(11);//kode resiko 3

            HSSFCell cellNONota = row.getCell(14);//no nota debet
            HSSFCell cellNOKPAK = row.getCell(15);//no kpak
            HSSFCell cellProvinsi = row.getCell(16);//provinsi

            HSSFCell cellPeriodStart = row.getCell(41);//periode awal
            HSSFCell cellPeriodEnd = row.getCell(42);//periode akhir
            HSSFCell cell4 = row.getCell(52);//penerangan

            HSSFCell cellPeriodRate = row.getCell(17);//period rate
            HSSFCell cellPeriodBaseID = row.getCell(76);//period base
            HSSFCell cellPremiumFactor = row.getCell(77);//premium factor

            HSSFCell cellEntityID = row.getCell(44);//entity id
            HSSFCell cellProdID = row.getCell(45);//period base

            HSSFCell cellBiayaPolis = row.getCell(46);//period base
            HSSFCell cellMaterai = row.getCell(47);//period base
            HSSFCell cellFeebasePct = row.getCell(48);//period base
            HSSFCell cellKomisiPct = row.getCell(49);//period base
            HSSFCell cellKomisiEntID = row.getCell(50);//premium factor


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

            String kodePos = policy.getStKodePos(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            if (kodePos != null) {
                getSelectedDefaultObject().setStReference9(kodePos);
            }
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());

            //getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType()==cell9.CELL_TYPE_STRING?cell9.getStringCellValue():new BigDecimal(cell9.getNumericCellValue()).toString());

            if (cellPeriodStart != null) {
                if (cellPeriodStart.getCellType() == cellPeriodStart.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
                }
            }

            if (cellPeriodEnd != null) {
                if (cellPeriodEnd.getCellType() == cellPeriodEnd.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
                }
            }

            getSelectedDefaultObject().setDtPeriodStart(getSelectedDefaultObject().getDtReference1());
            getSelectedDefaultObject().setDtPeriodEnd(getSelectedDefaultObject().getDtReference2());

            if (cellKodeResiko1 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode1(cellKodeResiko1.getCellType() == cellKodeResiko1.CELL_TYPE_STRING ? cellKodeResiko1.getStringCellValue() : new BigDecimal(cellKodeResiko1.getNumericCellValue()).toString());
            }

            if (cellKodeResiko2 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode2(cellKodeResiko2.getCellType() == cellKodeResiko2.CELL_TYPE_STRING ? cellKodeResiko2.getStringCellValue() : new BigDecimal(cellKodeResiko2.getNumericCellValue()).toString());
            }

            if (cellKodeResiko3 != null) {
                getSelectedDefaultObject().setStRiskCategoryCode3(cellKodeResiko3.getCellType() == cellKodeResiko3.CELL_TYPE_STRING ? cellKodeResiko3.getStringCellValue() : new BigDecimal(cellKodeResiko3.getNumericCellValue()).toString());
            }

            if (cellNONota != null) {
                getSelectedDefaultObject().setStReference14(cellNONota.getStringCellValue());
            }

            if (cellNOKPAK != null) {
                getSelectedDefaultObject().setStReference15(cellNOKPAK.getStringCellValue());
            }

            if (cellProvinsi != null) {
                getSelectedDefaultObject().setStReference16(cellProvinsi.getStringCellValue());
            }

            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }


            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));

            if(cellKomisiEntID!=null)
                getSelectedDefaultObject().setStKomisiEntityID(cellKomisiEntID.getCellType() == cellKomisiEntID.CELL_TYPE_STRING ? cellKomisiEntID.getStringCellValue() : new BigDecimal(cellKomisiEntID.getNumericCellValue()).toString());


            getSelectedDefaultObject().setDbBrokerFeePct(BDUtil.zero);
            getSelectedDefaultObject().setDbHandlingFeePct(BDUtil.zero);


            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(54);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(55);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC1 = row.getCell(21);//tsi desc
            HSSFCell cell_TSI1 = row.getCell(22);//tsi

            String tsiDesc1 = cell_TSI_DESC1!=null?cell_TSI_DESC1.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(56);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(57);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC2 = row.getCell(24);//tsi desc
            HSSFCell cell_TSI2 = row.getCell(25);//tsi

            String tsiDesc2 = cell_TSI_DESC2!=null?cell_TSI_DESC2.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()), tsiDesc2);
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()), tsiDesc2);
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(58);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(59);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC3 = row.getCell(27);//tsi desc
            HSSFCell cell_TSI3 = row.getCell(28);//tsi

            String tsiDesc3 = cell_TSI_DESC3!=null?cell_TSI_DESC3.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()), tsiDesc3);
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()), tsiDesc3);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(60);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(61);//RATE
            HSSFCell cell_Rate1 = row.getCell(62);//premi
            HSSFCell cell_Premi1 = row.getCell(63);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(64);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(65);//RATE
            HSSFCell cell_Rate2 = row.getCell(66);//premi
            HSSFCell cell_Premi2 = row.getCell(67);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate2 != null) {
                rate2 = rate2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(68);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(69);//RATE
            HSSFCell cell_Rate3 = row.getCell(70);//premi
            HSSFCell cell_Premi3 = row.getCell(71);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (premi3 != null) {
                premi3 = premi3.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate3 != null) {
                rate3 = rate3.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //cover 4
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(72);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(73);//RATE
            HSSFCell cell_Rate4 = row.getCell(74);//premi
            HSSFCell cell_Premi4 = row.getCell(75);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());

            if (premi4 != null) {
                premi4 = premi4.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate4 != null) {
                rate4 = rate4.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, null);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, null);
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
                
                HSSFCell cellPolId  = row.getCell(0);//pol_id
                //HSSFCell cellInsPolObjID  = row.getCell(1);//ins_pol_obj_id
                //HSSFCell cellPremiKoas  = row.getCell(2);//premi_koas
                //HSSFCell cellKomisiKoas  = row.getCell(3);//komisi_koas
                
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
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        
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
        
//        String pol_id = getRemoteInsurance().saveAndReturnPolicy(policy,policy.getStNextStatus(),false);
        
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
                
                HSSFCell cellPolId  = row.getCell(0);//pol_id
                
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
    
     private void uploadEarthquake(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Konversi3");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

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

            HSSFCell cell2 = row.getCell(1); //Penggunaan
            HSSFCell cell7 = row.getCell(6); //Keterangan
            HSSFCell cell9 = row.getCell(8); //Tahun Pembuatan
            HSSFCell cell10 = row.getCell(9); //Alamat Risiko
            HSSFCell cell12 = row.getCell(11); //Kelas Konstruksi Kebakaran
            HSSFCell cellProvince = row.getCell(13); //Provinsi
            HSSFCell cell3 = row.getCell(36); //Zona Gempa
            HSSFCell cell4 = row.getCell(37); //Kelas Konstruksi
            HSSFCell cell5 = row.getCell(38); //Kategori Bangunan
            HSSFCell cell6 = row.getCell(39); //Jumlah Lantai
            HSSFCell cell8 = row.getCell(40); //Zona
            HSSFCell cell13 = row.getCell(41); //Kode Resiko
            HSSFCell cellREF2DESC = row.getCell(60);//nama
            HSSFCell cell11 = row.getCell(61); //Kode Pos

            /*
            String kodePos = policy.getStKodePosMaipark(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
            if (kodePos != null) {
            getSelectedDefaultObject().setStReference9(kodePos);
            }
             */

            getSelectedDefaultObject().setStReference1(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2Desc(cellREF2DESC.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference10(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell7.getCellType() == cell7.CELL_TYPE_STRING ? cell7.getStringCellValue() : new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell9.getCellType() == cell9.CELL_TYPE_STRING ? cell9.getStringCellValue() : new BigDecimal(cell9.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell10.getCellType() == cell10.CELL_TYPE_STRING ? cell10.getStringCellValue() : new BigDecimal(cell10.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference9(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStRiskClass(cell12.getCellType() == cell12.CELL_TYPE_STRING ? cell12.getStringCellValue() : new BigDecimal(cell12.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cellProvince.getCellType() == cellProvince.CELL_TYPE_STRING ? cellProvince.getStringCellValue() : new BigDecimal(cellProvince.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStRiskCategoryID(cell13.getCellType() == cell13.CELL_TYPE_STRING ? cell13.getStringCellValue() : new BigDecimal(cell13.getNumericCellValue()).toString());

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(42);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(43);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC1 = row.getCell(15);//tsi desc
            HSSFCell cell_TSI1 = row.getCell(16);//tsi
            HSSFCell cell_TSI1CoMem = row.getCell(17);//tsi100

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), new BigDecimal(cell_TSI1CoMem.getNumericCellValue()), cell_TSI_DESC1.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), new BigDecimal(cell_TSI1CoMem.getNumericCellValue()), cell_TSI_DESC1.getStringCellValue());
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(44);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(45);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC2 = row.getCell(19);//tsi desc
            HSSFCell cell_TSI2 = row.getCell(20);//tsi
            HSSFCell cell_TSI2CoMem = row.getCell(21);//tsi100

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()), new BigDecimal(cell_TSI2CoMem.getNumericCellValue()), cell_TSI_DESC2.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()), new BigDecimal(cell_TSI2CoMem.getNumericCellValue()), cell_TSI_DESC2.getStringCellValue());
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(46);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(47);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC3 = row.getCell(23);//tsi desc
            HSSFCell cell_TSI3 = row.getCell(24);//tsi
            HSSFCell cell_TSI3CoMem = row.getCell(25);//tsi100

            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()), new BigDecimal(cell_TSI3CoMem.getNumericCellValue()), cell_TSI_DESC3.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()), new BigDecimal(cell_TSI3CoMem.getNumericCellValue()), cell_TSI_DESC3.getStringCellValue());
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(48);//ins_cvpt_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(49);//ins_cover_id
            HSSFCell cell_Rate1 = row.getCell(50);//rate
            HSSFCell cell_Premi1 = row.getCell(51);//premi

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(52);//ins_cvpt_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(53);//ins_cover_id
            HSSFCell cell_Rate2 = row.getCell(54);//rate
            HSSFCell cell_Premi2 = row.getCell(55);//premi

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(56);//ins_cvpt_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(57);//ins_cover_id
            HSSFCell cell_Rate3 = row.getCell(58);//rate
            HSSFCell cell_Premi3 = row.getCell(59);//premi

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }
    
    private void uploadMV(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

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

            HSSFCell cell1 = row.getCell(1);//no polisi
            HSSFCell cell2 = row.getCell(71);//merk/jenis
            HSSFCell cell3 = row.getCell(3);//Tahun pembuatan
            HSSFCell cell4 = row.getCell(4);//No rangka
            HSSFCell cell5 = row.getCell(5);//No Mesin
            HSSFCell cell6 = row.getCell(6);//Tempat duduk
            HSSFCell cell7 = row.getCell(72);//Digunakan
            HSSFCell cell8 = row.getCell(8);//Tipe
            HSSFCell cell9 = row.getCell(9);//nama
            HSSFCell cell10 = row.getCell(73);//Kode Resiko
            HSSFCell cellPeriodStart = row.getCell(32);
            HSSFCell cellPeriodEnd = row.getCell(33);

            HSSFCell cellPeriodRate = row.getCell(29);//period rate period factor
            HSSFCell cellPeriodBaseID = row.getCell(96);//period base of period
            HSSFCell cellPremiumFactor = row.getCell(97);//premium factor rate factor

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
            getSelectedDefaultObject().setStReference2(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell7.getCellType() == cell7.CELL_TYPE_STRING ? cell7.getStringCellValue() : new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference9(cell9.getCellType() == cell9.CELL_TYPE_STRING ? cell9.getStringCellValue() : new BigDecimal(cell9.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStRiskCategoryID(cell10.getCellType() == cell10.CELL_TYPE_STRING ? cell10.getStringCellValue() : new BigDecimal(cell10.getNumericCellValue()).toString());

            if (cellPeriodStart != null) {
                if (cellPeriodStart.getCellType() == cellPeriodStart.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
                }
            }

            if (cellPeriodEnd != null) {
                if (cellPeriodEnd.getCellType() == cellPeriodEnd.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
                }
            }

            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }

            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }


            //tsi kendaraan
            HSSFCell cell11 = row.getCell(74);//ins_tcpt_id
            HSSFCell cell12 = row.getCell(75);//ins_tsi_cat_id
            HSSFCell cell13 = row.getCell(76);//tsi
            //tsi peralatan
            HSSFCell cell14 = row.getCell(77);///ins_tcpt_id
            HSSFCell cell15 = row.getCell(78);//ins_tsi_cat_id
            HSSFCell cell16 = row.getCell(79);//tsi

//            //cover comprehensive
//            HSSFCell cell17 = row.getCell(17);//ins_cover_id
//            HSSFCell cell18 = row.getCell(18);//rate
//            HSSFCell cell19 = row.getCell(19);//premi
//            HSSFCell cell20 = row.getCell(20);//ins_cvpt_id
//            HSSFCell cell21 = row.getCell(21);//insured_amount
//
//            //cover tjh
//            HSSFCell cell22 = row.getCell(22);//ins_cover_id
//            HSSFCell cell23 = row.getCell(23);//rate
//            HSSFCell cell24 = row.getCell(24);//premi
//            HSSFCell cell25 = row.getCell(25);//ins_cvpt_id
//            HSSFCell cell26 = row.getCell(26);//insured_amount
//
//            //cover lainnya
//            HSSFCell cell27 = row.getCell(27);//ins_cover_id
//            HSSFCell cell28 = row.getCell(28);//rate
//            HSSFCell cell29 = row.getCell(29);//premi
//            HSSFCell cell30 = row.getCell(30);//ins_cvpt_id
//            HSSFCell cell31 = row.getCell(31);//insured_amount

            if (cell13 != null) {
                doAddLampiranSumInsured(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString(), new BigDecimal(cell13.getNumericCellValue()));
            }

            //if(cell14!=null)
            //doAddLampiranSumInsured(cell14.getCellType()==cell14.CELL_TYPE_STRING?cell14.getStringCellValue():new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));

            if (cell14.getCellType() == cell14.CELL_TYPE_STRING) {
                if (!cell14.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell14.getCellType() == cell14.CELL_TYPE_STRING ? cell14.getStringCellValue() : new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));
                }
            } else if (cell14.getCellType() == cell14.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(cell14.getCellType() == cell14.CELL_TYPE_STRING ? cell14.getStringCellValue() : new BigDecimal(cell14.getNumericCellValue()).toString(), new BigDecimal(cell16.getNumericCellValue()));
            }
            //NEW

            //cover 1
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(80);//ins_cover_id
            HSSFCell cell_Rate1 = row.getCell(81);//rate
            HSSFCell cell_Premi1 = row.getCell(82);//premi
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(83);//ins_cvpt_id
            HSSFCell cell_TSI1 = row.getCell(14);//tsi

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());
            BigDecimal tsi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI1.getNumericCellValue())) ? null : new BigDecimal(cell_TSI1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, tsi1);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, tsi1);
            }

            //cover 2
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(84);//ins_cover_id
            HSSFCell cell_Rate2 = row.getCell(85);//rate
            HSSFCell cell_Premi2 = row.getCell(86);//premi
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(87);//ins_cvpt_id
            HSSFCell cell_TSI2 = row.getCell(18);//tsi

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());
            BigDecimal tsi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI2.getNumericCellValue())) ? null : new BigDecimal(cell_TSI2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, tsi2);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, tsi2);
            }

            //cover 3
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(88);//ins_cover_id
            HSSFCell cell_Rate3 = row.getCell(89);//rate
            HSSFCell cell_Premi3 = row.getCell(90);//premi
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(91);//ins_cvpt_id
            HSSFCell cell_TSI3 = row.getCell(22);//tsi

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());
            BigDecimal tsi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI3.getNumericCellValue())) ? null : new BigDecimal(cell_TSI3.getNumericCellValue());

            if (premi3 != null) {
                premi3 = premi3.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, tsi3);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, tsi3);
            }

            //cover 4
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(92);//ins_cover_id
            HSSFCell cell_Rate4 = row.getCell(93);//rate
            HSSFCell cell_Premi4 = row.getCell(94);//premi
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(95);//ins_cvpt_id
            HSSFCell cell_TSI4 = row.getCell(26);//tsi

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());
            BigDecimal tsi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_TSI4.getNumericCellValue())) ? null : new BigDecimal(cell_TSI4.getNumericCellValue());

            if (premi4 != null) {
                premi4 = premi4.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, tsi4);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, tsi4);
            }
            //END NEW

            HSSFCell cell_Ins_Clm_CauseID1 = row.getCell(98);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID1 = row.getCell(99);//deduct type
            HSSFCell cell_Ded_PCT1 = row.getCell(36);//deduct pct
            HSSFCell cell_Ded_Min1 = row.getCell(38);//deduct min
            HSSFCell cell_Ded_Max1 = row.getCell(39);//deduct max

            //add deductible 1
            BigDecimal pct1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT1.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT1.getNumericCellValue());
            BigDecimal min1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min1.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min1.getNumericCellValue());
            BigDecimal max1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max1.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max1.getNumericCellValue());

            if (cell_Ins_Clm_CauseID1.getCellType() == cell_Ins_Clm_CauseID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID1.getStringCellValue(), pct1, cell_Ins_Ded_typeID1.getStringCellValue(), min1, max1);
                }
            } else if (cell_Ins_Clm_CauseID1.getCellType() == cell_Ins_Clm_CauseID1.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID1.getNumericCellValue()).toString(), pct1, cell_Ins_Ded_typeID1.getStringCellValue(), min1, max1);
            }

            HSSFCell cell_Ins_Clm_CauseID2 = row.getCell(100);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID2 = row.getCell(101);//deduct type
            HSSFCell cell_Ded_PCT2 = row.getCell(41);//deduct pct
            HSSFCell cell_Ded_Min2 = row.getCell(43);//deduct min
            HSSFCell cell_Ded_Max2 = row.getCell(44);//deduct max

            //add deductible 2
            BigDecimal pct2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT2.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT2.getNumericCellValue());
            BigDecimal min2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min2.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min2.getNumericCellValue());
            BigDecimal max2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max2.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max2.getNumericCellValue());

            if (cell_Ins_Clm_CauseID2.getCellType() == cell_Ins_Clm_CauseID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID2.getStringCellValue(), pct2, cell_Ins_Ded_typeID2.getStringCellValue(), min2, max2);
                }
            } else if (cell_Ins_Clm_CauseID2.getCellType() == cell_Ins_Clm_CauseID2.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID2.getNumericCellValue()).toString(), pct2, cell_Ins_Ded_typeID2.getStringCellValue(), min2, max2);
            }

            HSSFCell cell_Ins_Clm_CauseID3 = row.getCell(102);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID3 = row.getCell(103);//deduct type
            HSSFCell cell_Ded_PCT3 = row.getCell(46);//deduct pct
            HSSFCell cell_Ded_Min3 = row.getCell(48);//deduct min
            HSSFCell cell_Ded_Max3 = row.getCell(49);//deduct max

            //add deductible 3
            BigDecimal pct3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT3.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT3.getNumericCellValue());
            BigDecimal min3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min3.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min3.getNumericCellValue());
            BigDecimal max3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max3.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max3.getNumericCellValue());

            if (cell_Ins_Clm_CauseID3.getCellType() == cell_Ins_Clm_CauseID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID3.getStringCellValue(), pct3, cell_Ins_Ded_typeID3.getStringCellValue(), min3, max3);
                }
            } else if (cell_Ins_Clm_CauseID3.getCellType() == cell_Ins_Clm_CauseID3.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID3.getNumericCellValue()).toString(), pct3, cell_Ins_Ded_typeID3.getStringCellValue(), min3, max3);
            }

            HSSFCell cell_Ins_Clm_CauseID4 = row.getCell(104);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID4 = row.getCell(105);//deduct type
            HSSFCell cell_Ded_PCT4 = row.getCell(51);//deduct pct
            HSSFCell cell_Ded_Min4 = row.getCell(53);//deduct min
            HSSFCell cell_Ded_Max4 = row.getCell(54);//deduct max

            //add deductible 4
            BigDecimal pct4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT4.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT4.getNumericCellValue());
            BigDecimal min4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min4.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min4.getNumericCellValue());
            BigDecimal max4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max4.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max4.getNumericCellValue());

            if (cell_Ins_Clm_CauseID4.getCellType() == cell_Ins_Clm_CauseID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID4.getStringCellValue(), pct4, cell_Ins_Ded_typeID4.getStringCellValue(), min4, max4);
                }
            } else if (cell_Ins_Clm_CauseID4.getCellType() == cell_Ins_Clm_CauseID4.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID4.getNumericCellValue()).toString(), pct4, cell_Ins_Ded_typeID4.getStringCellValue(), min4, max4);
            }

            HSSFCell cell_Ins_Clm_CauseID5 = row.getCell(106);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID5 = row.getCell(107);//deduct type
            HSSFCell cell_Ded_PCT5 = row.getCell(56);//deduct pct
            HSSFCell cell_Ded_Min5 = row.getCell(58);//deduct min
            HSSFCell cell_Ded_Max5 = row.getCell(59);//deduct max

            //add deductible 5
            BigDecimal pct5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT5.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT5.getNumericCellValue());
            BigDecimal min5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min5.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min5.getNumericCellValue());
            BigDecimal max5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max5.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max5.getNumericCellValue());

            if (cell_Ins_Clm_CauseID5.getCellType() == cell_Ins_Clm_CauseID5.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID5.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID5.getStringCellValue(), pct5, cell_Ins_Ded_typeID5.getStringCellValue(), min5, max5);
                }
            } else if (cell_Ins_Clm_CauseID5.getCellType() == cell_Ins_Clm_CauseID5.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID5.getNumericCellValue()).toString(), pct5, cell_Ins_Ded_typeID5.getStringCellValue(), min5, max5);
            }
  
            HSSFCell cell_Ins_Clm_CauseID6 = row.getCell(108);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID6 = row.getCell(109);//deduct type
            HSSFCell cell_Ded_PCT6 = row.getCell(61);//deduct pct
            HSSFCell cell_Ded_Min6 = row.getCell(63);//deduct min
            HSSFCell cell_Ded_Max6 = row.getCell(64);//deduct max

            //add deductible 6
            BigDecimal pct6 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT6.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT6.getNumericCellValue());
            BigDecimal min6 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min6.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min6.getNumericCellValue());
            BigDecimal max6 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max6.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max6.getNumericCellValue());

            if (cell_Ins_Clm_CauseID6.getCellType() == cell_Ins_Clm_CauseID6.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID6.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID6.getStringCellValue(), pct6, cell_Ins_Ded_typeID6.getStringCellValue(), min6, max6);
                }
            } else if (cell_Ins_Clm_CauseID6.getCellType() == cell_Ins_Clm_CauseID6.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID6.getNumericCellValue()).toString(), pct6, cell_Ins_Ded_typeID6.getStringCellValue(), min6, max6);
            }

            HSSFCell cell_Ins_Clm_CauseID7 = row.getCell(110);//ins_clm_cause_id
            HSSFCell cell_Ins_Ded_typeID7 = row.getCell(111);//deduct type
            HSSFCell cell_Ded_PCT7 = row.getCell(66);//deduct pct
            HSSFCell cell_Ded_Min7 = row.getCell(68);//deduct min
            HSSFCell cell_Ded_Max7 = row.getCell(69);//deduct max

            //add deductible 7
            BigDecimal pct7 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_PCT7.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_PCT7.getNumericCellValue());
            BigDecimal min7 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Min7.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Min7.getNumericCellValue());
            BigDecimal max7 = BDUtil.isZeroOrNull(new BigDecimal(cell_Ded_Max7.getNumericCellValue())) ? null : new BigDecimal(cell_Ded_Max7.getNumericCellValue());

            if (cell_Ins_Clm_CauseID7.getCellType() == cell_Ins_Clm_CauseID7.CELL_TYPE_STRING) {
                if (!cell_Ins_Clm_CauseID7.getStringCellValue().equalsIgnoreCase("NULL")) {
                    onNewObjDeductible(cell_Ins_Clm_CauseID7.getStringCellValue(), pct7, cell_Ins_Ded_typeID7.getStringCellValue(), min7, max7);
                }
            } else if (cell_Ins_Clm_CauseID7.getCellType() == cell_Ins_Clm_CauseID7.CELL_TYPE_NUMERIC) {
                onNewObjDeductible(new BigDecimal(cell_Ins_Clm_CauseID7.getNumericCellValue()).toString(), pct7, cell_Ins_Ded_typeID7.getStringCellValue(), min7, max7);
            }

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

    private void uploadBurglary(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if(sheet==null)
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");

        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows+1; r++){
            HSSFRow row   = sheet.getRow(r);

            HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

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

            HSSFCell cell1  = row.getCell(2);//penggunaan
            HSSFCell cell2  = row.getCell(3);//kelas kontruksi
            HSSFCell cell3  = row.getCell(4);//kode resiko
            HSSFCell cell4  = row.getCell(35);//penerangan
            HSSFCell cell5  = row.getCell(6);//alamat
            HSSFCell cell6  = row.getCell(7);//kode pos
            //HSSFCell cell7  = row.getCell(8);//

            HSSFCell cell8  = row.getCell(9);//nama

            //HSSFCell cell9  = row.getCell(10);//nama

            HSSFCell cellPeriodStart  = row.getCell(31);//periode awal
            HSSFCell cellPeriodEnd = row.getCell(32);//periode akhir

            HSSFCell cellRiskCat = row.getCell(36);//kode resiko 1
            //HSSFCell cellKodeResiko2 = row.getCell(11);//kode resiko 2
            //HSSFCell cellKodeResiko3 = row.getCell(12);//kode resiko 3

            //HSSFCell cellNONota = row.getCell(15);//no nota debet
            //HSSFCell cellNOKPAK = row.getCell(16);//no kpak

            HSSFCell cellPeriodRate = row.getCell(13);//period rate
            HSSFCell cellPeriodBaseID = row.getCell(55);//period base
            HSSFCell cellPremiumFactor = row.getCell(56);//premium factor


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

            getSelectedDefaultObject().setStRiskCategoryID(cellRiskCat.getCellType()==cellRiskCat.CELL_TYPE_STRING?cellRiskCat.getStringCellValue():new BigDecimal(cellRiskCat.getNumericCellValue()).toString());

            if(cellPeriodStart!=null){
                if(cellPeriodStart.getCellType()==cellPeriodStart.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
            }

            if(cellPeriodEnd!=null){
                if(cellPeriodEnd.getCellType()==cellPeriodEnd.CELL_TYPE_STRING) getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                else getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
            }

            if(cellPeriodRate!=null){
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType()==cellPeriodRate.CELL_TYPE_STRING?new BigDecimal(cellPeriodRate.getStringCellValue()):new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }

            if(cellPeriodBaseID!=null)
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType()==cellPeriodBaseID.CELL_TYPE_STRING?cellPeriodBaseID.getStringCellValue():new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());

            if(cellPremiumFactor!=null)
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType()==cellPremiumFactor.CELL_TYPE_STRING?cellPremiumFactor.getStringCellValue():new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(37);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(38);//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(17);//tsi

            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(39);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(40);//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(19);//tsi

            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(41);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(42);//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(21);//tsi

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
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(43);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(44);//RATE
            HSSFCell cell_Rate1  = row.getCell(45);//premi
            HSSFCell cell_Premi1  = row.getCell(46);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());

            if(rate1!=null)
                rate1 = rate1.setScale(3,BigDecimal.ROUND_HALF_UP);

            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(47);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(48);//RATE
            HSSFCell cell_Rate2  = row.getCell(49);//premi
            HSSFCell cell_Premi2  = row.getCell(50);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());

            if(rate2!=null)
                rate2 = rate2.setScale(3,BigDecimal.ROUND_HALF_UP);

            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(51);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(52);//RATE
            HSSFCell cell_Rate3 = row.getCell(53);//premi
            HSSFCell cell_Premi3  = row.getCell(54);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());

            if(rate3!=null)
                rate3 = rate3.setScale(3,BigDecimal.ROUND_HALF_UP);

            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

    public String editCreateAutoEndorse(String policyID, BigDecimal premiBruto, BigDecimal TSI) throws Exception {
        policy = new InsuranceSplitPolicyView();
        
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

        return "";
        //return getRemoteInsurance().saveAutoEndorsement(policy, policy.getStNextStatus(), true);
        
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
        return getEntityByID(entityID);
    }

    public EntityView getEntityByID(String stEntID) {

        EntityView entity = new EntityView();

        entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);
        
        return entity;
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

    public void editCreateEndorseBatalTotal(String policyID) throws Exception {
        superEdit(policyID);

        cekClosingStatus("INPUT");

        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID("2");
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList details = policy.getDetails();
        
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);
            
            if(items.isStampFee() || items.isPolicyCost()) items.setDbAmount(BDUtil.negate(items.getDbAmount()));
   
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            //policy.invokeCustomCriteria(obj);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                cov.setDbInsuredAmount(BDUtil.zero);

                if(cov.isEntryPremi()&& !cov.isEntryRate()){
                    cov.setStEntryPremiFlag("Y");
                    cov.setDbPremi(BDUtil.negate(cov.getDbPremi()));
                    cov.setDbPremiNew(cov.getDbPremi());
                    cov.setStEntryRateFlag("N");
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

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReference8("PROSES_ENDORSE_MEI_MEGALIFE");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n"+
                                "SESI PREMI KOAS");

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

            /*
            if(objx.getStReference8().equalsIgnoreCase("96")){
                //BATALIN PREMI KOAS
                objx.setStReference14("Y");
                objx.setDbReference2(BDUtil.negate(objx.getDbReference2()));

                //BATALIN KOMISI KOAS
                objx.setDbReference9(BDUtil.negate(objx.getDbReference9()));
                //objx.setStReference8("96");
            }*/

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

        cekClosingStatus("INPUT");
        
        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        policy.checkEndorseNoBefore(policy.getStPolicyNo());
        
        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);
            
            if(items.isStampFee()) items.setDbAmount(BDUtil.negate(items.getDbAmount()));
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.negate(items.getDbAmount()));
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(objx.isUsingPeriodFactorPerObject()){
                objx.setDbPeriodRateBefore(objx.getDbPeriodRate());
                objx.setStPeriodBaseBeforeID(objx.getStPeriodBaseID());
            }
            
            //policy.invokeCustomCriteria(obj);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();
                
                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }
                
                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
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

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                if(tsi.getDbInsuredAmountFull()!=null) tsi.setDbInsuredAmountFull(BDUtil.zero);

                tsi.setDbInsuredAmount(BDUtil.zero);
            }

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.setStEditFlag("Y");
                trdi.setDbTSIAmount(BDUtil.negate(trdi.getDbTSIAmount()));

                final DTOList share = trdi.getShares();

                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("Y");
                    ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));

                }

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

        cekClosingStatus("INPUT");
        
        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

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

                if(cov.isEntryPremi()) cov.setStEntryPremiFlag(null);
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
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);
            
            if(obj==selectedObject) continue;
            
            for (int  j= 0; j < dedSize ; j++) {
                InsuranceSplitPolicyDeductibleView ded = (InsuranceSplitPolicyDeductibleView) deduct.get(j);
                
                InsuranceSplitPolicyDeductibleView newDed = new InsuranceSplitPolicyDeductibleView();
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
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);
            
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
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);
            
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
    
    public boolean isRejectMode() {
        return rejectMode;
    }

    public void setRejectMode(boolean rejectMode) {
        this.rejectMode = rejectMode;
    }
    
    public void reject(String policyID) throws Exception {
        setRejectMode(true);
         
        editApprove(policyID);
        
        super.setReadOnly(true);
        
        approvalMode = true;
        
    }
     
    private IncomingManager getRemoteEntityManager2() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
            .create();
   }

     
    public void sendRejectionLetters(InsurancePolicyView pol) throws Exception{
          UserSession us = SessionManager.getInstance().getSession();
          
          IncomingView incoming = new IncomingView();

          incoming.setStRefNo("REJECT-"+pol.getStPolicyNo());
          incoming.setStSender(us.getStUserID());
          incoming.setStSenderName(us.getStUserName());
          incoming.setDtLetterDate(new Date());
          incoming.setStSubject("REJECT-"+pol.getStPolicyNo());
          incoming.setStNote("Ini adalah surat otomatis :\n\n ");
          incoming.setStNote(incoming.getStNote() + pol.getStRejectNotes());
          incoming.setStReceiver(Parameter.readString("REJECT_LETTERS_"+pol.getStCostCenterCode()));
          //incoming.setStReceiver("doni");
          incoming.setStReadFlag("N");
          incoming.setStDeleteFlag("N");

          incoming.markNew();
          getRemoteEntityManager2().save(incoming);
    }
    
    public void editCreateEndorseCoverType(String policyID) throws Exception {
        superEdit(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/

        cekPolisExpire();
        
        policy.setStCoverTypeCode(null);
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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        
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

                if(cov.isEntryPremi()) cov.setStEntryPremiFlag(null);
                
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
    
    public DTOList getDeductibleAddLOV() throws Exception {
        loadDeductibleLOV();

        final DTOList lov = new DTOList();

        for (int i = 0; i < deductibleLOV.size(); i++) {
            InsuranceClaimCauseView cls = (InsuranceClaimCauseView) deductibleLOV.get(i);

            //if(cls.getStActiveFlag()!=null) continue;

            if(cls.getStActiveFlag()!=null)
                if(Tools.isNo(cls.getStActiveFlag()))
                    continue;

            lov.add(cls);
        }

        return lov;
    }
    
    private void loadDeductibleLOV() throws Exception {
        if (deductibleLOV==null)
            deductibleLOV = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      *" +
                    "   from" +
                    "      ins_clm_cause " +
                    "   where " +
                    "      pol_type_id = ?" ,
                    new Object [] {policy.getStPolicyTypeID()},
                    InsuranceClaimCauseView.class
                    );
    }
    
    private String getUangMukaKlaimInsItemID() throws Exception {
        
        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='ADVPAYMENT'");
        
        return lu.getCode(0);
    }
    
    public void adjustPremiORWithKoas()throws Exception{
        final DTOList objects = policy.getObjects();
        for (int i = 0; i < objects.size(); i++){
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            BigDecimal premiOR = obj.getDbReference6();
            BigDecimal premiKoas = obj.getDbReference2();
            
            if(BDUtil.biggerThan(premiKoas, premiOR))
                obj.setDbReference6(premiKoas);
        }
    }

    public boolean isEnableSwapPremiORKoas()
    {
        return enableSwapPremiORKoas;
    }

    public void setEnableSwapPremiORKoas(boolean enableSwapPremiORKoas)
    {
        this.enableSwapPremiORKoas = enableSwapPremiORKoas;
    }
    
    public void editKeterangan(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        policy.markUpdateO();
        
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.markUpdate();

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

        policy.showItemsAccount();
        
        initTabs();
        
    }

    public boolean isEditKeteranganMode()
    {
        return editKeteranganMode;
    }

    public void setEditKeteranganMode(boolean editKeteranganMode)
    {
        this.editKeteranganMode = editKeteranganMode;
    }
    
    public void superEditReasOnly(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

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

        policy.showItemsAccount();
        
        policy.setEditReasOnlyMode(true);

        initTabs();
        
    }
    
    public void editCreatePolisAfterEndorse(String policyID) throws Exception {
        
        if (policyID==null) {
            
            createNew();
            
            policy.setStStatus(FinCodec.PolicyStatus.POLICY);
            
            return;
        }
        
        superEdit(policyID);

        cekClosingStatus("INPUT");
        
        checkActiveEffective();

        boolean bolehRIBatal = true;
        if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("1")){
            bolehRIBatal = false;
            
            if(policy.getStAdminNotes()!=null)
                if(policy.getStAdminNotes().equalsIgnoreCase("RI_OK"))
                    bolehRIBatal = true;
        }

        if(!bolehRIBatal)
                throw new RuntimeException("Fitur ini tidak bisa dilakukan untuk jenis polis Fire, karena endorsemen fire membutuhkan validasi bagian reasuransi");
        
        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");
        
        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) && !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus())) {
            throw new RuntimeException("Endorse ini hanya bisa dibuat dari Polis/Renewal");
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
        policy.setStPolicyNo(null);
        policy.setDtPolicyDate(new Date());
        policy.setStReference4(null);
        policy.setStReference1(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        
        initTabs();
    }

    public boolean isCreateEndorseAndPolicyMode()
    {
        return createEndorseAndPolicyMode;
    }

    public void setCreateEndorseAndPolicyMode(boolean createEndorseAndPolicyMode)
    {
        this.createEndorseAndPolicyMode = createEndorseAndPolicyMode;
    }
    
    public void btnApproveENDORSE() throws Exception {
        
    }
    
    public void setTaxCode()throws Exception{
        InsurancePolicyItemsView item = (InsurancePolicyItemsView) getDetails().get(Integer.parseInt(getDetailindex()));
        
        if(item.getInsItem().isNotUseTax()) return;
            
        final String tax_code = item.getEntity().getStTaxCode();
        
        if(tax_code==null) item.setStTaxCode(null);
            
        if(tax_code!=null){
            if(item.isKomisi()){
                if(tax_code.equalsIgnoreCase("PPH21")) item.setStTaxCode("1");
                else if(tax_code.equalsIgnoreCase("PPH23")) item.setStTaxCode("2");
            }else if(item.isBrokerFee()){
                if(tax_code.equalsIgnoreCase("PPH21")) item.setStTaxCode("4");
                else if(tax_code.equalsIgnoreCase("PPH23")) item.setStTaxCode("5");
            }else if(item.isHandlingFee()){
                if(tax_code.equalsIgnoreCase("PPH21")) item.setStTaxCode("7");
                else if(tax_code.equalsIgnoreCase("PPH23")) item.setStTaxCode("8");
            }
        }
    }
    
    public void createNewInward() throws Exception {
        
    }
    
    public void addFeeBaseAutomatically(InsurancePolicyView policy, BigDecimal feebasePct, String insItemID)throws Exception{
        
        final String coverType = policy.getStCoverTypeCode();

        if(!policy.getStPolicyTypeID().equalsIgnoreCase("87") && !policy.getStPolicyTypeID().equalsIgnoreCase("88"))
            if(insItemID==null) insItemID = "66";

        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStEntityID(), feebasePct);
        }
        
        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStEntityID(), feebasePct);
        }
        
        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStEntityID(), feebasePct);
        }
        
        if(coverType.equalsIgnoreCase("COINSOUTSELF")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStEntityID(), feebasePct);
        }
        
    }

    public void addKomisiAutomatically(InsurancePolicyView policy, BigDecimal komisiPct, String stEntityID)throws Exception{
        if(policy.isStatusHistory()||policy.isStatusEndorse()||policy.isStatusInward()) return;

        final String coverType = policy.getStCoverTypeCode();

        /*
        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic2(policy,"11","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic2(policy,"18","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic2(policy,"25","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSOUTSELF")){
            onNewDetailAutomatic2(policy,"32","N", stEntityID, komisiPct);
        }*/

        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic2(policy,"112","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic2(policy,"116","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic2(policy,"117","N", stEntityID, komisiPct);
        }

    } 
    
    public void onNewDetailAutomatic2(InsurancePolicyView policy, String insItemID, String stReadOnly, String entityID, BigDecimal feebasePct) throws Exception {
        
        if (insItemID == null) throw new Exception("Please select item to be added");
        
        final DTOList detail = policy.getDetails();
        
        for(int i=0; i<detail.size(); i++){
            InsurancePolicyItemsView det = (InsurancePolicyItemsView) detail.get(i);
            
            //if(det.getInsItem().isNotUseTax()) policy.getDetails().delete(i);
        }

        final InsuranceItemsView itemCat = getInsItemCat(insItemID);
        
        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();
        
        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);
        
        item.markNew();
        
        item.setStInsItemID(insItemID);
        
        item.setStROFlag(stReadOnly);
        
        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        if(entityID!=null) item.setStEntityID(entityID);

        EntityView ent = getEntity(entityID);

        if(ent!=null){
            if(item.isKomisi2()){
                if(ent.getStTaxCode().equalsIgnoreCase("PPH23")){
                    item.setStTaxCode("2");
                }else{
                    item.setStTaxCode("1");
                }
            }

            if(item.isBrokerFee()){
                  item.setStTaxCode("5");
            }

        }

        if(!BDUtil.isZeroOrNull(feebasePct)){
            item.setStFlagEntryByRate("Y");
            item.setDbRate(feebasePct);
        }

        policy.getDetails().add(item);

        if(itemCat.getStInsuranceItemChildID()!=null)
            onNewDetailByChildID(policy, itemCat.getStInsuranceItemChildID());
        
    }
    
    public BigDecimal getClaimLimit(String cat, String cat2) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      c.refn" + cat2 +
                    "   from " +
                    "         s_user_roles b " +
                    "         inner join ff_table c on c.fft_group_id='CAPA' and c.ref1=b.role_id and c.ref2=? and c.ref3=?" +
                    "   where" +
                    " c.active_flag = 'Y' "+
                    " and b.user_id=?"+
                    " and (ref4 = ? or ref4 is null) "+
                    " order by ref4 limit 1");
            
            S.setParam(1,cat);
            S.setParam(2,policy.getStPolicyTypeID());
            S.setParam(3,SessionManager.getInstance().getUserID());
            S.setParam(4,policy.getStClaimCauseID());
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getBigDecimal(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }
    
    public void checkPassword()throws Exception{
        if(policy.getStPassword()==null) throw new RuntimeException("Password Validasi Harus Diisi");
        
        String encryptPswd = Digest.computeDigest(UserManager.getInstance().getUser().getStUserID(),policy.getStPassword());
        boolean samePassword = (Tools.isEqual(UserManager.getInstance().getUser().getStPasswd(), encryptPswd));
        
        /*logger.logDebug("++++++++++++++= CEK PASWORD : "+ samePassword);
        logger.logDebug("++++++++++++++= PASWORD USER : "+ UserManager.getInstance().getUser().getStPasswd());
        logger.logDebug("++++++++++++++= ENTRY PASWORD : "+ encryptPswd);*/
        if(!samePassword){
            approvalMode = true; 
            policy.setStEffectiveFlag(null);
            policy.setStPassword(null);
            throw new RuntimeException("Password Anda Salah,Ulangi Kembali");
        }
    }
    
    private void uploadSipanda(FileInputStream fis, String treatyID)throws Exception{
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
            
            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;
            
            doNewLampiranObject();
            setStSelectedObject(String.valueOf(getObjects().size()-1));
            switchSelectedObject();
            
            
                                /*
                                 *  BISNIS
                                        BUATAN
                                        ALAMAT RESIKO
                                        KONDISI/SITUASI
                                        TANGGAL
                                 */
            
            HSSFCell cell1  = row.getCell(0);//KODE (String)
            HSSFCell cell2  = row.getCell(1);//BANK (String)
            HSSFCell cell3  = row.getCell(2);//JENIS (String)
            
            HSSFCell cell4  = row.getCell(3);//STRATA 1 (String)
            HSSFCell cell5  = row.getCell(4);//STRATA 2 (String)
            HSSFCell cell6  = row.getCell(5);//STRATA 3 (String)
            HSSFCell cell7  = row.getCell(6);//STRATA 4 (String)
            HSSFCell cell8  = row.getCell(7);//STRATA 5 (String)
            HSSFCell cell9  = row.getCell(8);//STRATA 6 (String)
            HSSFCell cell10  = row.getCell(9);//STRATA 7 (String)
            HSSFCell cell11  = row.getCell(10);//STRATA 8 (String)
            HSSFCell cell12  = row.getCell(11);//STRATA 9 (String)
            HSSFCell cell13  = row.getCell(12);//STRATA 10 (String)
             
            HSSFCell cellRate  = row.getCell(13);//STRATA 10 (String)
            HSSFCell cellPremi  = row.getCell(14);//STRATA 10 (String)
            
            /*
            if(cell1.getCellType()!=cell1.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom KODE Harus String/Character");
            
            if(cell2.getCellType()!=cell2.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom BANK Harus String/Character");
             
            if(cell3.getCellType()!=cell3.CELL_TYPE_STRING)
                throw new RuntimeException("Kolom JENIS String/Character");*/
            //logger.logDebug("++++++++++++++++++ KODE : "+ cell1.getStringCellValue());
            
            getSelectedDefaultObject().setStReference1(cell1.getCellType()==cell1.CELL_TYPE_STRING?cell1.getStringCellValue():new BigDecimal(cell1.getNumericCellValue()).toString()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            
            getSelectedDefaultObject().setDbReference1(new BigDecimal(cell4.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference2(new BigDecimal(cell5.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference3(new BigDecimal(cell6.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference4(new BigDecimal(cell7.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference5(new BigDecimal(cell8.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference6(new BigDecimal(cell9.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference7(new BigDecimal(cell10.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference8(new BigDecimal(cell11.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference9(new BigDecimal(cell12.getNumericCellValue()));
            getSelectedDefaultObject().setDbReference10(new BigDecimal(cell13.getNumericCellValue()));

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
            
            
            //add tsi
            final DTOList sumInsuredAddLOV = getSumInsuredAddLOV();
        
        for (int i = 0; i < sumInsuredAddLOV.size(); i++) {
            InsuranceTSIPolTypeView ipt = (InsuranceTSIPolTypeView) sumInsuredAddLOV.get(i);
            
            if (!Tools.isYes(ipt.getStDefaultTSIFlag())) continue;
            
            final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
            
            ptsi.setStInsuranceTSIPolTypeID(ipt.getStInsuranceTSIPolTypeID());
            
            ptsi.initializeDefaults();
            
            ptsi.markNew();
            
            getSelectedDefaultObject().getSuminsureds().add(ptsi);
        }

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
            
            getSelectedDefaultObject().getCoverage().add(cv);
            
        }
        

            DTOList cover = getSelectedDefaultObject().getCoverage();
            for(int i=0; i<cover.size(); i++){
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(i);
                
                if(cov.isAutoRate()) cov.setStAutoRateFlag(null);
                    
                if(cellRate!=null){
                    if(!BDUtil.isZeroOrNull(new BigDecimal(cellRate.getNumericCellValue()))){
                        cov.setDbRate(new BigDecimal(cellRate.getNumericCellValue()));
                    }else if(!BDUtil.isZeroOrNull(new BigDecimal(cellPremi.getNumericCellValue()))){
                        cov.setStEntryRateFlag(null);
                        cov.setStEntryPremiFlag("Y");
                        cov.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));
                        cov.setDbPremiNew(new BigDecimal(cellPremi.getNumericCellValue()));
                    }
                        
                        
                }
            }
            
        }
    }

    public void applyRIMemberToAllObjects() throws Exception{

        final DTOList object = policy.getObjects();

        for (int  i= 0; i < object.size(); i++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);
            
            if(obj==selectedObject) continue;
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            
            for (int  j= 0; j < treatyDetails.size() ; j++) {
                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                //if(!tredet.getTreatyDetail().getStInsuranceTreatyDetailID().isFacultative()) continue;
                
                //tredet.getShares().deleteAll();
                    
                final DTOList treatyDetailsSelected = selectedObject.getTreatyDetails();
        
                for (int  x= 0; x < treatyDetailsSelected.size() ; x++) {
                    InsurancePolicyTreatyDetailView tredetSelected = (InsurancePolicyTreatyDetailView) treatyDetailsSelected.get(x);

                    //if(!tredetSelected.getTreatyDetail().isFacultative()) continue;
                    
                    if(!tredet.getTreatyDetail().getStInsuranceTreatyDetailID().equalsIgnoreCase(tredetSelected.getTreatyDetail().getStInsuranceTreatyDetailID()))
                        continue;
                    
                    tredet.getShares().deleteAll();
                    
                    final DTOList sharesSelected = tredetSelected.getShares();
                    for (int  y= 0; y < sharesSelected.size() ; y++) {
                        InsurancePolicyReinsView riSelected = (InsurancePolicyReinsView) sharesSelected.get(y);
                        
                        InsurancePolicyReinsView riNew = new InsurancePolicyReinsView();
        
                        riNew = (InsurancePolicyReinsView) riSelected.clone();

                        riNew.markNew();

                        tredet.getShares().add(riNew);
                    }
                } 
            }  
        }
    }


    public void applyRIRateAndSlipToAll() throws Exception{

        final DTOList object = policy.getObjects();

        for (int  i= 0; i < object.size(); i++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);

            if(obj==selectedObject) continue;

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int  j= 0; j < treatyDetails.size() ; j++) {
                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                //if(!tredet.getTreatyDetail().getStInsuranceTreatyDetailID().isFacultative()) continue;

                final DTOList treatyDetailsSelected = selectedObject.getTreatyDetails();

                for (int  x= 0; x < treatyDetailsSelected.size() ; x++) {
                    InsurancePolicyTreatyDetailView tredetSelected = (InsurancePolicyTreatyDetailView) treatyDetailsSelected.get(x);

                    //if(!tredetSelected.getTreatyDetail().isFacultative()) continue;

                    if(!tredet.getTreatyDetail().getStInsuranceTreatyDetailID().equalsIgnoreCase(tredetSelected.getTreatyDetail().getStInsuranceTreatyDetailID()))
                        continue;

                    final DTOList sharesSelected = tredetSelected.getShares();

                    final DTOList shares = tredet.getShares();

                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                        for (int  y= 0; y < sharesSelected.size() ; y++) {
                            InsurancePolicyReinsView riSelected = (InsurancePolicyReinsView) sharesSelected.get(y);

                            if(ri.getStInsuranceTreatySharesID()==null) continue;

                            if(!ri.getStInsuranceTreatySharesID().equalsIgnoreCase(riSelected.getStInsuranceTreatySharesID()))
                                continue;

                            if(ri.getStInsuranceTreatySharesID().equalsIgnoreCase(riSelected.getStInsuranceTreatySharesID())){
                                ri.setDbPremiRate(riSelected.getDbPremiRate());
                                ri.setDbRICommRate(riSelected.getDbRICommRate());
                                ri.setStRISlipNo(riSelected.getStRISlipNo());
                                ri.setStUseRateFlag(riSelected.getStUseRateFlag());
                                ri.setStAutoRateFlag(riSelected.getStAutoRateFlag());
                            }

                        }

                    }

                }
            }
        }
    }
            
    public void editCreateClaimEndorseCoins(String policyID) throws Exception {
        superEdit(policyID);
        
        checkActiveEffective();
        
        if (!policy.isEffective()) {
            
            throw new RuntimeException("Policy not yet effective");
        }

        final DTOList claimItems = policy.getClaimItems();
        
        if (claimItems.size()==0) {
            
            insItemID = getClaimBrutoInsItemID();
            
            final InsurancePolicyItemsView item = onNewClaimItem();
            
            item.setStChargableFlag("Y");
            
            item.setStROFlag("Y");
        }
        
        policy.generateClaimEndorseDLA();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReadyToApproveFlag(null);
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
            
            final InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
            
            objx.setStReference8("1");
            
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
        
        final DTOList claimCoins = policy.getCoinsCoverage();
        for (int k = 0; k < claimCoins.size(); k++) {
            InsurancePolicyCoinsView coins = (InsurancePolicyCoinsView) claimCoins.get(k);
            
            if(coins.isHoldingCompany()){
                coins.setStAutoClaimAmount("Y");
                coins.setDbClaimAmount(BDUtil.negate(policy.getDbClaimAmountApproved()));
            }
 
        }
        
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
  
    }
    
    public void btnSaveCoins() throws Exception {
        

            policy.recalculate();
            
            policy.validateData();
            policy.validate(false);
        
            policy.setDtApprovedDate(new Date());
            policy.setDtApprovedClaimDate(new Date());
            //policy.setStApprovedWho("admin");
            //getRemoteInsurance().save(policy, policy.getStNextStatus(), true);

    }
    
    private void defineTreaty2() throws Exception{
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
    
    public void approvalByDivision(String policyID) throws Exception {
        editApprove(policyID);
        
        super.setReadOnly(true);
        
        approvalMode = true;
        
        approvalByDirectorMode = true;
        
        tabs.setActiveTab("TAB_POLICY_DOCUMENTS");
        
    }

    private void uploadKredit(POIFSFileSystem fis, String treatyID) throws Exception {
        //POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheet("Konversi2020");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//nama
            HSSFCell cell2 = row.getCell(2);//tgl lahir
            HSSFCell cell3 = row.getCell(3);//tgl awal
            HSSFCell cell4 = row.getCell(4);//tgl akhir
            HSSFCell cell5 = row.getCell(9);//tsi
            HSSFCell cell6 = row.getCell(10);//premi
            HSSFCell cell7 = row.getCell(7);//rate
            HSSFCell cell8 = row.getCell(5);//usia
            HSSFCell cellKtp = row.getCell(13);//ktp
            HSSFCell cellPK = row.getCell(14);//pk
            HSSFCell cellTglPK = row.getCell(15);//tgl pk
            HSSFCell cellAlamat = row.getCell(16);//alamat
            HSSFCell cellLama = row.getCell(8);//lama
            HSSFCell cellStatus = row.getCell(30);//kategori
            HSSFCell cellKategori = row.getCell(29);//kategori debitur
            HSSFCell cellCoverage = row.getCell(31);//kategori debitur
            HSSFCell cellRekPinjaman = row.getCell(17);//rek pinjaman

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cell5.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured("488", b);

            double tes3 = cell6.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            BigDecimal d = null;
            if (cell7 != null) {
                double tes4 = cell7.getNumericCellValue();
                d = new BigDecimal(tes4);
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if (cell2 != null) {
                getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            }
            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }            //if(cellKtp!=null) getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //ktp
            //if(cellPK!=null) getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //pk
            //getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //kategori

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference4(ConvertUtil.removeTrailing(String.valueOf(cellPK.getNumericCellValue()))); //usia
                }
            }

            if (cellRekPinjaman != null) {
                if (cellRekPinjaman.getCellType() == cellRekPinjaman.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference16(cellRekPinjaman.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference16(ConvertUtil.removeTrailing(String.valueOf(cellRekPinjaman.getNumericCellValue()))); //usia
                }
            }


            if (cellKategori.getCellType() == cellKategori.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference8(cellKategori.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference8(ConvertUtil.removeTrailing(String.valueOf(cellKategori.getNumericCellValue()))); //usia
            }
            if (cell8.getCellType() == cell8.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference2(cell8.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell8.getNumericCellValue()))); //usia
            }
            if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
            } else {
                getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
            }
            if (cellStatus.getCellType() == cellStatus.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //PNS/NON
            }
            if (cellTglPK != null) {
                if (cellTglPK.getCellType() == cellTglPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference4(DateUtil.getDate(cellTglPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue());
                }
            }

            if (cell3.getCellType() == cell3.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
            }

            if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
            }

            if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference13(cellCoverage.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference13(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
            }

            /*
            if(cellStatus.getCellType()==cellStatus.CELL_TYPE_STRING)
            getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //status
            else
            getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //lama
             */

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(22);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(24);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cell7 != null) {
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            HSSFCell cellPremiPA = row.getCell(25);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cell7 == null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasi(cvptID3, ratePA2, premiPA2);

            //add coverkreasi
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(27);//ins_cover_id
            HSSFCell cell_Premi2 = row.getCell(28);//tsi

            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverKreasi(cell_Ins_Cvpt_ID2.getStringCellValue(), null, premi2);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverKreasi(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), null, premi2);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    
    public void editCreateEndorseBatalCoasPerObject(String policyID, DTOList object) throws Exception {
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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        //policy.setStReference8("PROSES_OR_JUNI_MEGALIFE");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");

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

            /*
            if(objx.getStReference8().equalsIgnoreCase("96")){
                //BATALIN PREMI KOAS
                objx.setStReference14("Y");
                objx.setDbReference2(BDUtil.negate(objx.getDbReference2()));

                //BATALIN KOMISI KOAS
                objx.setDbReference9(BDUtil.negate(objx.getDbReference9()));
                //objx.setStReference8("96");
            }*/
            for (int k = 0; k < object.size(); k++) {
                InsurancePolicyView objectMega = (InsurancePolicyView) object.get(k);
                
                BigDecimal premiKoasNet = BDUtil.sub(objx.getDbReference2(), objx.getDbReference9());
                
                if(objx.getStReference1().trim().equalsIgnoreCase(objectMega.getStReference1().trim())){
                    if(BDUtil.isEqual(premiKoasNet, objectMega.getDbPremiTotal(),0)){
                        objx.setStReference14("Y"); 
                        objx.setDbReference2(BDUtil.negate(objx.getDbReference2()));
                    } 
                }
            }

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
    
    public void setTaxCodeOnClaim()throws Exception{
        InsurancePolicyItemsView item = (InsurancePolicyItemsView) getClaimItems().get(Integer.parseInt(getClaimItemindex()));

        if(item.getInsItem().isNotUseTax()) return;

        final EntityView ent = item.getEntity();

        String tax_code = "10";

        if(ent.getStClaimTaxCode()!=null){
            tax_code = ent.getStClaimTaxCode();
        }

        if(tax_code==null) item.setStTaxCode(null);

        if(tax_code!=null){
            if(item.isJasaBengkel()){
//                if(tax_code.equalsIgnoreCase("PPH21")) item.setStTaxCode("10");
//                else if(tax_code.equalsIgnoreCase("PPH23")) item.setStTaxCode("10");
                  item.setStTaxCode(tax_code);
            }
        }

        item.setStEntityID(ent.getStEntityID());

    }
    
    public void editReinsuranceOnly(String policyID) throws Exception {
        superEditReverseReinsurance(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

        //if (policy.isEffectiveReinsurance()) throw new RuntimeException("Data tidak bisa diubah karena sudah disetujui R/I");
   }
    
    public void superEditReverseReinsurance(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);
        
        //policy.setStRIFinishFlag("N");
        
        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            final DTOList treatyDetails = obj.getTreatyDetails();
            
            treatyDetails.markAllUpdate();
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList share = trdi.getShares();
                
                share.markAllUpdate();

                /*
                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView reins = (InsurancePolicyReinsView) share.get(k);
                    
                    if(reins.getDbTSIAmount()!=null && reins.getDbTSIAmountEdited()==null){
                        reins.setDbTSIAmountEdited(reins.getDbTSIAmount());
                        reins.setDbTSIAmount(null);
                    }
                    
                    if(reins.getDbPremiAmount()!=null && reins.getDbPremiAmountEdited()==null){
                        reins.setStUseRateFlag("N");
                        reins.setStAutoRateFlag("Y");
                        reins.setDbPremiAmountEdited(reins.getDbPremiAmount());
                        reins.setDbPremiAmount(null);
                       
                    }
                    
                    if(reins.getDbRICommAmount()!=null && reins.getDbRICommAmountEdited()==null){
                        reins.setDbRICommAmountEdited(reins.getDbRICommAmount());
                        reins.setDbRICommAmount(null);
                        
                    }
                }
                */
            }
            
        }
        
        //policy.showItemsAccount();
        
        initTabs();
        
    }
    
     public void btnReverseReinsurance() throws Exception {
        
        boolean withinCurrentMonth = DateUtil.getDateStr(policy.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        
        boolean canReverse = true;
        
        //getRemoteInsurance().reverseReinsuranceOnly(policy);

        policy.setStRIFinishFlag("N");
        policy.setStRIPostedFlag("N");

        //getRemoteInsurance().reverseReinsuranceJurnalBalik(policy);

        String policyID = policy.getStPolicyID();

        setPolicy(null);
        
        editReinsuranceOnly(policyID);

        
    }

    public boolean isEditReasMode() {
        return editReasMode;
    }

    public void setEditReasMode(boolean editReasMode) {
        this.editReasMode = editReasMode;
    }
    
    public void btnApproveReinsuranceOnly() throws Exception {
        
    }
    
    public void editReinsuranceOnly2(String policyID) throws Exception {
        superEditReverseReinsurance2(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

        if (policy.isEffectiveReinsurance()) throw new RuntimeException("Data tidak bisa diubah karena sudah disetujui R/I");
   }
    
    public void superEditReverseReinsurance2(String policyID) throws Exception {
        view(policyID);
        
        super.setReadOnly(false);
        
        policy.setObjects(null);

        final boolean inward = policy.getCoverSource().isInward();
        
        final DTOList objects = policy.getObjects();
        
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                final DTOList share = trdi.getShares();
                
                share.markAllUpdate();
                
            }
            
        }
        
        initReinsuranceTabs();
        
    }
    
    private String getInsuranceCoverIDBranch() throws Exception {

        final SQLUtil S = new SQLUtil();
        
        try {
            final PreparedStatement PS = S.setQuery("select pol_type_id,ins_cover_source_id from ins_policy_types_branch where pol_type_id = ? and cc_code = ?");
            
            PS.setString(1, policy.getStPolicyTypeID());
            PS.setString(2, policy.getStCostCenterCode());
            
            final ResultSet RS = PS.executeQuery();
            
            if (RS.next()){
                //logger.logDebug("++++++++++++++ get : "+ RS.getString(1));
                return RS.getString(2);
            }
            
            return "";
            
        } finally {
            S.release();
        }
    }
    
    public void validateJenisKredit() throws Exception{
        if(!policy.getStPolicyTypeID().equalsIgnoreCase("21") && !policy.getStPolicyTypeID().equalsIgnoreCase("59"))
            return;
        
        if(policy.isStatusEndorse()||policy.isStatusClaim()||policy.isStatusClaimEndorse()||policy.isStatusEndorseRI()||policy.isStatusHistory())
            return;
        
        if(policy.getStAdminNotes()!=null)
            if(policy.getStAdminNotes().equalsIgnoreCase("APPROVED"))
                return;

        if(policy.isBypassValidation())
            return;

        final DTOList object = policy.getObjects();
        
        DateTime bulanIzinKredit = new DateTime(DateUtil.getDate("19/06/2012"));
        DateTime bulanNovember = new DateTime(DateUtil.getDate("01/11/2012"));
        //DateTime bulanDesember = new DateTime(DateUtil.getDate("01/12/2012"));
        DateTime tglCreate = new DateTime(policy.getDtCreateDate());
        
        PAKreasiHandler hd = new PAKreasiHandler(); 

        hd.getJenisKredit(policy.getStKreasiTypeID());

        //String jenis_kredit = hd.getJenisKredit(policy.getStKreasiTypeID());
        //String jenis_rate = hd.getJenisRate(policy.getStKreasiTypeID());
        String jenis_kredit = hd.getJenisKredit();
        String jenis_rate = hd.getJenisRate();
        boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
        boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
        boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
        
        String onlyPlafonKreasi = hd.getRef3(policy.getStKreasiTypeID());
        boolean onlyPlafon = Tools.isNo(onlyPlafonKreasi);

        for (int i = 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            if(object.size()==1){
                if(objx.getStReference20()!=null)
                    if(Tools.isYes(objx.getStReference20()))
                        return;
            }

            DateTime tglAwal = new DateTime(objx.getDtReference2());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                
                //DateTime bulanIzinKreasiSemarang = new DateTime(DateUtil.getDate("01/01/2013"));
                //DateTime tglPolis = new DateTime(policy.getDtPolicyDate());

                BigDecimal tsi = objx.getDbReference4();
                int maksUsia = Integer.parseInt(objx.getStReference2()) + Integer.parseInt(objx.getStReference4());

                //if(policy.getStCostCenterCode().equalsIgnoreCase("22"))
                    //if(tglPolis.isBefore(bulanIzinKreasiSemarang))
                       // return;

                if(policy.getStCostCenterCode().equalsIgnoreCase("22"))
                    return;
        
                if(tglCreate.isBefore(bulanNovember)) 
                    return;

                if(BDUtil.biggerThanEqual(tsi, new BigDecimal(1000000000)))
                    return;

                if(maksUsia > 65)
                    return;

                if(objx.getStReference21()!=null)
                    if(Tools.isYes(objx.getStReference21()))
                        return;
                
                if(tglAwal.isAfter(bulanIzinKredit)){
                        if(onlyPlafon){
                            policy.setStKreasiTypeID(null);
                            throw new RuntimeException("Asuransi PA Kreasi hanya boleh jenis Plafon Awal, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
                        }
                }
                
            }
            
            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                if(tglAwal.isAfter(bulanIzinKredit)){
                    if(isPlafonAwal){
                        policy.setStKreasiTypeID(null);
                        throw new RuntimeException("Asuransi Kredit hanya boleh jenis Baki Debet, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
                    }
                }
                
                if(tglAwal.isBefore(bulanIzinKredit)){
                        policy.setStKreasiTypeID(null);
                        throw new RuntimeException("Asuransi Kredit hanya boleh jenis Baki Debet, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
                }
                
            }

        }

        
    }
    
    
    public void copyReinsFromBefore() throws Exception{
        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            final DTOList treaty = obj.getTreaties();

            for (int j = 0; j < treaty.size(); j++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(j);

                final DTOList treatyDetails = tre.getDetails();

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    final DTOList members = tredet.getShares();

                    for (int l = 0; l < members.size(); l++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) members.get(l);

                        if(ri.getDbTSIAmountEdited() != null) ri.setDbTSIAmount(ri.getDbTSIAmountEdited());
                        if(ri.getDbPremiAmountEdited() != null) ri.setDbPremiAmount(ri.getDbPremiAmountEdited());
                        if(ri.getDbRICommAmountEdited() != null) ri.setDbRICommAmount(ri.getDbRICommAmountEdited());

                    }

                }

            }
        }
    }

    public void editCreateEndorseRestitution(String policyID) throws Exception {
        superEdit(policyID);

        cekClosingStatus("INPUT");

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        policy.checkEndorseNoBefore(policy.getStPolicyNo());

        policy.getDetails().deleteAll();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                if(cov.isEntryPremi()) cov.setStEntryPremiFlag(null);
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

    /**
     * @return the stKey
     */
    public String getStKey() {
        return stKey;
    }

    /**
     * @param stKey the stKey to set
     */
    public void setStKey(String stKey) {
        this.stKey = stKey;
    }

    private DTOList objectSearch;
    
    /**
     * @return the objectSearch
     */
    public DTOList getObjectSearch() {
        loadSearchObject();
        return objectSearch;
    }

    /**
     * @param objectSearch the objectSearch to set
     */
    public void setObjectSearch(DTOList objectSearch) {
        this.objectSearch = objectSearch;
    }

    public void loadSearchObject() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (getStKey()!=null){
                objectSearch = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_obj where pol_id = ? and upper(ref1) like ? order by ins_pol_obj_id",
                        new Object[]{policy.getStPolicyID(), "%"+ getStKey().toUpperCase() +"%"},
                        InsurancePolicyObjDefaultView.class
                        );
            }else{
                 objectSearch = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_obj where pol_id = ? limit 0",
                        new Object[]{policy.getStPolicyID()},
                        InsurancePolicyObjDefaultView.class
                        );
            }
                
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void searchObject()throws Exception{
        getObjectSearch();
    }

    public void chooseObject()throws Exception{

        int idx = Integer.parseInt(objIndex)-1;
        stSelectedObject = String.valueOf(idx);
        selectedObject = (InsuranceSplitPolicyObjectView) getObjects().get(Integer.parseInt(stSelectedObject));
        selectObject();
    }

    /**
     * @return the objIndex
     */
    public String getObjIndex() {
        return objIndex;
    }

    /**
     * @param objIndex the objIndex to set
     */
    public void setObjIndex(String objIndex) {
        this.objIndex = objIndex;
    }

    public void setCreditTreaty(InsurancePolicyObjDefaultView obj)throws Exception{

        final boolean inward = policy.getCoverSource().isInward();
        String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");

        final EntityView entity = policy.getEntity();
        String treatyIDByCompGroup = policy.getInsuranceTreatyID(policy.getDtPeriodStart(), entity.getStRef2());

        boolean orOnly = true;

        if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
            int usiaKredit = Integer.parseInt(obj.getStReference2());
            int usiaPlusLamaKredit = usiaKredit + Integer.parseInt(obj.getStReference5());

            if(usiaKredit >= 17 && usiaKredit <=64 && usiaPlusLamaKredit <= 70) orOnly = false;
        }

        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
            int usiaKreasi = Integer.parseInt(obj.getStReference2());
            int usiaPlusLamaKreasi = usiaKreasi + Integer.parseInt(obj.getStReference4());

            boolean isORKreasi = obj.getStReference8().equalsIgnoreCase("1");

            if(usiaKreasi >= 17 && usiaKreasi <=64 && usiaPlusLamaKreasi <= 70 && isORKreasi) orOnly = false;

        }

        if(policy.getStRegionID().equalsIgnoreCase("39")) orOnly = true;

        if(orOnly){
            obj.setStInsuranceTreatyID("36");
        }else{
            obj.setStInsuranceTreatyID(treatyID);
        }

        if(treatyIDByCompGroup!=null)
            obj.setStInsuranceTreatyID(treatyIDByCompGroup);

    }

    public void superEditClaim(String policyID) throws Exception {
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

        policy.showItemsAccount();

        initTabs();

    }

    public void cekLimit()throws Exception{
        policy.checkDetailsLimit();
    }

    public void changeTreatyClaimObject() throws Exception{


        policy.setStClaimObjectID(null);
        //policy.setStClaimObjectID("17327988");

        
        DTOList obj = policy.getObjects();

        InsurancePolicyObjectView objDet = (InsurancePolicyObjectView) obj.get(0);

        objDet.setStInsuranceTreatyID(null);
        objDet.getTreaties().deleteAll();
        objDet.setTreaties(null);

        ritabs = null;

        DTOList claimObj2 = policy.getObjectsByID("17327988");

        InsurancePolicyObjectView claimObj3 = (InsurancePolicyObjectView) claimObj2.get(0);

        objDet.setTreaties(claimObj3.getTreaties());
    }

    public void setReinsuranceNull() throws Exception{

        final DTOList object = policy.getObjects();

        for (int  i= 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            final DTOList treatyDetails = obj.getTreatyDetails();
            
            for (int  j= 0; j < treatyDetails.size() ; j++) {
                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
                
                tredet.setStEditFlag("Y");
                tredet.setDbTSIAmount(BDUtil.zero);
                
                final DTOList shares = tredet.getShares();
                
                for (int  y= 0; y < shares.size() ; y++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(y);

                    ri.setStUseRateFlag(null);
                    ri.setStAutoRateFlag(null);
                    ri.setDbSharePct(BDUtil.zero);
                    ri.setDbTSIAmount(BDUtil.zero);
                    ri.setDbPremiAmount(BDUtil.zero);
                }
                 
            }  
        }
    }

    public void selectClaimTreaty() throws Exception{


            final DTOList objects = policy.getObjects();
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                obj.getTreaties().deleteAll();
                obj.setStInsuranceTreatyID(getClaimObject().getStInsuranceTreatyID());
                ritabs=null;
            }
        policy.recalculate();
        policy.recalculateTreatyInitialize();
    }

    private void uploadCAR(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        if(policy.getDtPeriodStart()==null) throw new RuntimeException("Periode Awal Harus Diisi");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");

        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1  = row.getCell(1);//transit dari (String)
            HSSFCell cell2  = row.getCell(2);//transit ke (String)
            HSSFCell cell3  = row.getCell(3);//tgl transit (date)
            HSSFCell cell4  = row.getCell(4);//waktu transit (String)
            HSSFCell cell5  = row.getCell(5);//no deklarasi (String)
            HSSFCell cell6  = row.getCell(6);//diangkut dengan	(String)
            HSSFCell cell7  = row.getCell(7);//diangkut dengan	(String)

            String kodePos = policy.getStKodePos(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference4(kodePos);

            getSelectedDefaultObject().setStReference1(cell1.getCellType()==cell1.CELL_TYPE_STRING?cell1.getStringCellValue():new BigDecimal(cell1.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStRiskCategoryID(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(8);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(9);//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(10);//tsi

            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }

            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(11);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(12);//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(13);//tsi

            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(14);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(15);//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(16);//tsi

            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }

            //add coverkreasi1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(17);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(18);//RATE
            HSSFCell cell_Rate1  = row.getCell(19);//premi
            HSSFCell cell_Premi1  = row.getCell(20);//tsi

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());

            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //add coverkreasi2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(21);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(22);//RATE
            HSSFCell cell_Rate2  = row.getCell(23);//premi
            HSSFCell cell_Premi2  = row.getCell(24);//tsi

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());

            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //add coverkreasi3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(25);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(26);//RATE
            HSSFCell cell_Rate3  = row.getCell(27);//premi
            HSSFCell cell_Premi3  = row.getCell(28);//tsi

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());

            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //doAddLampiranSumInsured("182",tsiCIT);
            //doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadEAR(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        if(policy.getDtPeriodStart()==null) throw new RuntimeException("Periode Awal Harus Diisi");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");

        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1  = row.getCell(1);//transit dari (String)
            HSSFCell cell2  = row.getCell(2);//transit ke (String)
            HSSFCell cell3  = row.getCell(3);//tgl transit (date)
            HSSFCell cell4  = row.getCell(4);//waktu transit (String)
            HSSFCell cell5  = row.getCell(5);//no deklarasi (String)
            HSSFCell cell6  = row.getCell(6);//diangkut dengan	(String)
            HSSFCell cell7  = row.getCell(7);//diangkut dengan	(String)

            String kodePos = policy.getStKodePos(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference4(kodePos);

            getSelectedDefaultObject().setStReference1(cell1.getCellType()==cell1.CELL_TYPE_STRING?cell1.getStringCellValue():new BigDecimal(cell1.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell6.getCellType()==cell6.CELL_TYPE_STRING?cell6.getStringCellValue():new BigDecimal(cell6.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStRiskCategoryID(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(8);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(9);//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(10);//tsi

            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }

            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(11);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(12);//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(13);//tsi

            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(14);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(15);//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(16);//tsi

            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }

            //add coverkreasi1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(17);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(18);//RATE
            HSSFCell cell_Rate1  = row.getCell(19);//premi
            HSSFCell cell_Premi1  = row.getCell(20);//tsi

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());

            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //add coverkreasi2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(21);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(22);//RATE
            HSSFCell cell_Rate2  = row.getCell(23);//premi
            HSSFCell cell_Premi2  = row.getCell(24);//tsi

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());

            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //add coverkreasi3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(25);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(26);//RATE
            HSSFCell cell_Rate3  = row.getCell(27);//premi
            HSSFCell cell_Premi3  = row.getCell(28);//tsi

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());

            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //doAddLampiranSumInsured("182",tsiCIT);
            //doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    public void btnReverseJurnalBalik() throws Exception {

        boolean withinCurrentMonth = DateUtil.getDateStr(policy.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        boolean canReverse = true;

        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse())
            if(!withinCurrentMonth) canReverse = false;

        if(policy.isStatusClaim()||policy.isStatusClaimEndorse()){
            boolean withinCurrentMonthClaim = DateUtil.getDateStr(policy.getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
            if(!withinCurrentMonthClaim) canReverse = false;

            if(!withinCurrentMonthClaim)
                if(policy.isStatusClaimPLA()) canReverse = true;
        }

        if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal())
            if(!withinCurrentMonth) canReverse = false;

        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
            if (policy.getDtPaymentDate() != null) {
                //if (Tools.isYes(policy.getStReference12())) {
                   throw new RuntimeException("Polis Sudah Dibayar pada no bukti : "+ policy.getStReceiptNo());
                //}
            }
        }
        
        if (!canReverse) throw new RuntimeException("Tanggal Polis/DLA Tidak Valid (Sudah Tutup Produksi)");

//        getRemoteInsurance().reverse(policy);

        close();
    }

    public void btnReverseJurnalBalikOnly() throws Exception {

        boolean withinCurrentMonth = DateUtil.getDateStr(policy.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        //final boolean blockValidPolicyDateAdmin = Parameter.readBoolean("BLOCKING_POLICY_DATE_ADMIN");
        boolean canReverse = true;
        boolean canEntryBackDate = UserManager.getInstance().getUser().canEntryBackdate();

        DateTime currentDateLastDay = new DateTime(policy.getDtPolicyDate());
        currentDateLastDay = currentDateLastDay.dayOfMonth().withMaximumValue();

        int batasMaxHari = 4;
        DateTime maximumBackDate = new DateTime();
        maximumBackDate = currentDateLastDay.plusDays(batasMaxHari);

        DateTime currentDate = new DateTime(new Date());

        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse())
            if(!withinCurrentMonth) canReverse = false;

        if(policy.isStatusClaim()||policy.isStatusClaimEndorse()){
            boolean withinCurrentMonthClaim = DateUtil.getDateStr(policy.getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
            if(!withinCurrentMonthClaim) canReverse = false;

            if(!withinCurrentMonthClaim)
                if(policy.isStatusClaimPLA()) canReverse = true;
        }

        //if(!blockValidPolicyDateAdmin) canReverse = true;

        if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal()){
            if(!withinCurrentMonth) canReverse = false;
            //if(canEntryBackDate)
//                 if(currentDate.isAfter(maximumBackDate))
//                     canReverse = false;
        }
            

        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
            if (policy.getDtPaymentDate() != null) {
                //if (Tools.isYes(policy.getStReference12())) {
                    throw new RuntimeException("Polis Sudah Dibayar pada no bukti : "+ policy.getStReceiptNo());
                //}
            }
        }

         if(policy.isStatusDraft() || policy.isStatusSPPA())
             canReverse = true;

        if(policy.getStReverseNotes()==null)
            throw new RuntimeException("Catatan reverse harus di input");

        if (!canReverse) throw new RuntimeException("Tanggal Polis/DLA Tidak Valid (Sudah Tutup Produksi)");

//        getRemoteInsurance().reverseJurnalBalik(policy);

        close();
    }

    public void selectClaimObjectUseOrder() throws Exception {

        int idx = Integer.parseInt(objIndex)-1;
        stSelectedObject = String.valueOf(idx);

        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjects().get(Integer.parseInt(stSelectedObject));

        //DTOList objf = policy.getObjectsByID(objIndex);
        //InsurancePolicyObjectView o = (InsurancePolicyObjectView) objf.get(0) ;

        boolean canNotClaim = policy.validateClaimObject(o);

        if(canNotClaim){
            stClaimObject = null;
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim sudah nol, tidak bisa dilakukan pembuatan klaim");
        }

        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());

            policy.setObjIndex(stClaimObject);

            final DTOList tsi = o.getSuminsureds();

            BigDecimal totalTSI = null;
            for (int i = 0; i < tsi.size(); i++) {
                InsurancePolicyTSIView suminsured = (InsurancePolicyTSIView) tsi.get(i);

                totalTSI = BDUtil.add(totalTSI, suminsured.getDbInsuredAmount());
            }

            policy.setDbClaimAmountEstimate(totalTSI);
        }

    }

    public void btnSaveReinsurance() throws Exception {
        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStNextStatus())){
            defineTreaty();
        }

        if(!isInputPaymentDateMode() && !isEditKeteranganMode() && !editReasMode){
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
            validateJenisKredit();
        }

        if(isEditKeteranganMode()){
            policy.setPrincipal();
        }



        close();
    }

    public BigDecimal getComissionLimitOJK() throws Exception {
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 in ('OJK') and b.fft_group_id='COMM'" +
                "   and (b.ref5 = ? or b.ref5 is null) "+
                "   and active_flag = 'Y'"+
                "   and date_trunc('day',period_start) <= ?"+
                "   order by ref5,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), policy.getStProducerID(), policy.getDtPeriodStart()},
                FlexTableView.class
                ).getDTO();

        return ft==null?null:ft.getDbReference1();
    }

    public void cekAllShareAutoRate() {

        final InsurancePolicyTreatyDetailView selectedDetail = (InsurancePolicyTreatyDetailView) getSelectedObjTreatyDetail();
        
        final DTOList member = getSelectedObjTreatyDetail().getShares();

        for (int i = 0; i < member.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(i);

            ri.setStAutoRateFlag(selectedDetail.getStAutoRateFlag());

        }
    }

    public void cekAllShareUseRate() {

        final InsurancePolicyTreatyDetailView selectedDetail = (InsurancePolicyTreatyDetailView) getSelectedObjTreatyDetail();

        final DTOList member = getSelectedObjTreatyDetail().getShares();

        for (int i = 0; i < member.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(i);

            ri.setStUseRateFlag(selectedDetail.getStUseRateFlag());

        }
    }

    public void cekAllShareApproved() {

        final InsurancePolicyTreatyDetailView selectedDetail = (InsurancePolicyTreatyDetailView) getSelectedObjTreatyDetail();

        final DTOList member = getSelectedObjTreatyDetail().getShares();

        for (int i = 0; i < member.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(i);

            ri.setStApprovedFlag(selectedDetail.getStApprovedFlag());

        }
    }

    public void cekPolisExpire() throws Exception{
        DateTime tanggalAkhir = new DateTime(policy.getDtPeriodEnd());
        DateTime tanggalCurrent = new DateTime(new Date());

        //if(tanggalAkhir.isBefore(tanggalCurrent))
            //throw new RuntimeException("Polis sudah berakhir tidak bisa di endorse");
    }

    private void uploadMC(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(0);//BISNIS (String)
            HSSFCell cell2 = row.getCell(1);//BUATAN (String)
            HSSFCell cell3 = row.getCell(2);//ALAMAT RESIKO (String)
            HSSFCell cell4 = row.getCell(3);//KONDISI/SITUASI (String)
            HSSFCell cell5 = row.getCell(4);//TANGGAL (date)
            HSSFCell cell6 = row.getCell(5);//BISNIS (String)
            HSSFCell cell7 = row.getCell(6);//BUATAN (String)
            HSSFCell cell8 = row.getCell(7);//ALAMAT RESIKO (String)
            HSSFCell cell9 = row.getCell(8);//KONDISI/SITUASI (String)
            HSSFCell cell10 = row.getCell(9);//TANGGAL (date)
            HSSFCell cell11 = row.getCell(10);//BISNIS (String)
            HSSFCell cell12 = row.getCell(11);//BUATAN (String)
            HSSFCell cell13 = row.getCell(12);//ALAMAT RESIKO (String)
            HSSFCell cell14 = row.getCell(13);//KONDISI/SITUASI (String)
            HSSFCell cell15 = row.getCell(14);//TANGGAL (date)

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Objek Pertanggungan Harus String/Character");
            }
            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom No. L/C Harus String/Character");
            }
            if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Bank Harus String/Character");
            }
            if (cell4.getCellType() != cell4.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Penerima Barang Harus String/Character");
            }
            if (cell5.getCellType() != cell5.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Dari / Ke Harus String/Character");
            }
            if (cell6.getCellType() != cell6.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Pindah Kapal Di Harus String/Character");
            }
            if (cell7.getCellType() != cell7.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Alat Angkut / Kapal Harus String/Character");
            }
            if (cell8.getCellType() != cell8.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom No. Invoice Harus String/Character");
            }
            if (cell9.getCellType() != cell9.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom No. B/L Harus String/Character");
            }
            if (cell10.getCellType() != cell10.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Packing List Harus String/Character");
            }
            if (cell11.getCellType() != cell11.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Agen Survey Harus String/Character");
            }
            if (cell12.getCellType() != cell12.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Agen Klaim Harus String/Character");
            }
            if (cell13.getCellType() != cell13.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Pembayaran Klaim di Luar Negeri Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());
            getSelectedDefaultObject().setStReference4(cell4.getStringCellValue());
            getSelectedDefaultObject().setStReference5(cell5.getStringCellValue());
            getSelectedDefaultObject().setStReference6(cell6.getStringCellValue());
            getSelectedDefaultObject().setStReference7(cell7.getStringCellValue());
            getSelectedDefaultObject().setStReference8(cell8.getStringCellValue());
            getSelectedDefaultObject().setStReference9(cell9.getStringCellValue());
            getSelectedDefaultObject().setStReference10(cell10.getStringCellValue());
            getSelectedDefaultObject().setStReference11(cell11.getStringCellValue());
            getSelectedDefaultObject().setStReference12(cell12.getStringCellValue());
            getSelectedDefaultObject().setStReference13(cell13.getStringCellValue());

            if (cell14 != null) {
                getSelectedDefaultObject().setDtReference1(cell14.getDateCellValue()); //tgl lahir
            }

            getSelectedDefaultObject().setStRiskCategoryID(cell15.getCellType() == cell15.CELL_TYPE_STRING ? cell15.getStringCellValue() : new BigDecimal(cell15.getNumericCellValue()).toString());

            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(15);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(16);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(17);//tsi

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }

            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(18);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(19);//ins_tsi_cat_id
            HSSFCell cell_TSI2 = row.getCell(20);//tsi

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(21);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(22);//RATE
            HSSFCell cell_Rate1 = row.getCell(23);//premi
            HSSFCell cell_Premi1 = row.getCell(24);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(25);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(26);//RATE
            HSSFCell cell_Rate2 = row.getCell(27);//premi
            HSSFCell cell_Premi2 = row.getCell(28);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(29);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(30);//RATE
            HSSFCell cell_Rate3 = row.getCell(31);//premi
            HSSFCell cell_Premi3 = row.getCell(32);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    public void setujuiPusatAllObject() throws Exception{
        final DTOList object = policy.getObjects();

        for (int i = 0; i < object.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object.get(i);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) obj.setStReference21("Y");
            else if(policy.getStPolicyTypeID().equalsIgnoreCase("59")) obj.setStReference10("Y");
            else if(policy.getStPolicyTypeID().equalsIgnoreCase("80")) obj.setStReference19("Y");

        }
    }

    /**
     * @return the canApproveHeadOffice
     */
    public boolean isCanApproveHeadOffice() {
        return canApproveHeadOffice;
    }

    /**
     * @param canApproveHeadOffice the canApproveHeadOffice to set
     */
    public void setCanApproveHeadOffice(boolean canApproveHeadOffice) {
        this.canApproveHeadOffice = canApproveHeadOffice;
    }

    /**
     * @return the deductibleAddID
     */
    public String getDeductibleAddID() {
        return deductibleAddID;
    }

    /**
     * @param deductibleAddID the deductibleAddID to set
     */
    public void setDeductibleAddID(String deductibleAddID) {
        this.deductibleAddID = deductibleAddID;
    }

    public DTOList getClausulesLOV() throws Exception {
        final DTOList lov = loadClausulesLOV();

        return lov;

    }

    public DTOList loadClausulesLOV() throws Exception {
        if (clausulesLOV==null)
            clausulesLOV = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      *" +
                    "   from" +
                    "      ins_clausules " +
                    "   where " +
                    "      pol_type_id = ? and cc_code = ? " ,
                    new Object [] {policy.getStPolicyTypeID(), policy.getStCostCenterCode()},
                    InsuranceClausulesView.class
                    );

        return clausulesLOV;
    }

    /**
     * @return the clausulesAddID
     */
    public String getClausulesAddID() {
        return clausulesAddID;
    }

    /**
     * @param clausulesAddID the clausulesAddID to set
     */
    public void setClausulesAddID(String clausulesAddID) {
        this.clausulesAddID = clausulesAddID;
    }

    public void onNewClausules() {
        final InsurancePolicyClausulesView clause = new InsurancePolicyClausulesView();

        clause.setStClauseID(clausulesAddID);
        clause.setStSelectedFlag("Y");

        clause.markNew();

        getClausules().add(clause);

    }

    /**
     * @return the clausulesIndex
     */
    public String getClausulesIndex() {
        return clausulesIndex;
    }

    /**
     * @param clausulesIndex the clausulesIndex to set
     */
    public void setClausulesIndex(String clausulesIndex) {
        this.clausulesIndex = clausulesIndex;
    }

    public void onDeleteClausules() {
        getClausules().delete(Integer.parseInt(clausulesIndex));
    }

    public void applyDefaultClausules(){
        try {

                final DTOList defaultClausules = ListUtil.getDTOListFromQuery(
                        "select "+
                        " * " +
                         " from " +
                            " ins_clausules b " +
                         " where b.pol_type_id = ? and cc_code = ? and f_default = 'Y'" +
                         " order by b.shortdesc",
                        new Object [] {policy.getStPolicyTypeID(), policy.getStCostCenterCode()},
                        InsuranceClausulesView.class
                        );

                for (int i = 0; i < defaultClausules.size(); i++) {
                    InsuranceClausulesView cls = (InsuranceClausulesView) defaultClausules.get(i);

                    clausulesAddID = cls.getStInsuranceClauseID();

                    onNewClausules();
                }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initReinsuranceTabs() {

        tabs = new FormTab();

        tabs.add(new FormTab.TabBean("TAB_REJECT","{L-ENGREJECT NOTES-L}{L-INAKETERANGAN-L}",false));
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
        //tabs.add(new FormTab.TabBean("TAB_REINS","REINSURANCE",true));
        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INALAMPIRAN-L}",false));
        tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));

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
        boolean statusInward = FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(getStStatus());

        boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
        boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
        boolean rejectModex = isRejectMode();

        if(!statusDraft && !statusSPPA){
            tabs.enable("TAB_CLAUSES",true);
            //tabs.enable("TAB_PREMI",true);
            tabs.enable("TAB_INST",true);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            tabs.enable("TAB_OPTIONS",true);
        }

        if (statusEndorse||statusClaimEndorse||statusEndorseRI) {
            tabs.enable("TAB_ENDORSE",true);
            tabs.setActiveTab("TAB_ENDORSE");
        }

        if(rejectModex){
            tabs.enable("TAB_REJECT",true);
            tabs.setActiveTab("TAB_REJECT");
        }

        if (statusClaim||statusClaimEndorse) {
            tabs.enable("TAB_CLAIM",true);
            tabs.enable("TAB_CLAIM_ITEM",true);
            tabs.enable("TAB_CLAIM_RE",true);
            tabs.enable("TAB_CLAIMCO",true);
            tabs.enable("TAB_COINS",false);
            tabs.enable("TAB_COINS_COVER",false);
            //tabs.enable("TAB_COVER_REINS",false);
            //tabs.enable("TAB_POLICY_DOCUMENTS",false);
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
            tabs.enable("TAB_CLAIMCOINS_COVER",true);
            tabs.setActiveTab("TAB_CLAIM");
        }

        rdtabs = new FormTab();

        rdtabs.add(new FormTab.TabBean("TAB_DETAIL","{L-ENGDETAILS-L}{L-INADETIL-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_SPPA","SPPA",true));
        rdtabs.add(new FormTab.TabBean("TAB_ANALYSIS","ANALISA RESIKO",true));
        //rdtabs.add(new FormTab.TabBean("TAB_RDCLM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",statusClaim));
        rdtabs.add(new FormTab.TabBean("TAB_SI","{L-ENGSUM INSURED-L}{L-INAHARGA PERTANGGUNGAN-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_DED","{L-ENGDEDUCTIBLES-L}{L-INARESIKO SENDIRI-L}",true));
        rdtabs.add(new FormTab.TabBean("TAB_REINS","{L-ENGREINSURANCE-L}{L-INAREASURANSI-L}",true));

        rdtabs.setActiveTab("TAB_REINS");

        if (!(reasMode || statusClaim || statusEndorseRI || statusInward)) {
            //rdtabs.enable("TAB_REINS",false);
        }

        if (Config.isDevelopmentMode()) {
            rdtabs.enable("TAB_REINS",true);
        }
        //rdtabs.enable("TAB_REINS",true);

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
            tabs.enable("TAB_POLICY_DOCUMENTS",true);
        }

        claimtabs.setActiveTab("TAB_DOCUMENTS");

        if(statusClaimEndorse){
            tabs.setActiveTab("TAB_ENDORSE");
        }

        if(statusDLA||statusClaimEndorse){
            claimtabs.enable("TAB_DLA",true);
        }



    }

    /**
     * @return the reasDataOnlyApprovedMode
     */
    public boolean isReasDataOnlyApprovedMode() {
        return reasDataOnlyApprovedMode;
    }

    /**
     * @param reasDataOnlyApprovedMode the reasDataOnlyApprovedMode to set
     */
    public void setReasDataOnlyApprovedMode(boolean reasDataOnlyApprovedMode) {
        this.reasDataOnlyApprovedMode = reasDataOnlyApprovedMode;
    }

    public void defaultPeriodObject(){

        InsuranceSplitPolicyObjDefaultView objx = (InsuranceSplitPolicyObjDefaultView) getSelectedObject();
        objx.setDbPeriodRate(new BigDecimal(100));
        objx.setStPeriodBaseID("2");
        objx.setStPremiumFactorID("1");
    }

    public void changePeriodBaseObject() throws Exception{
        policy.calculatePeriods();
        //policy.recalculate();
    }

    public void editCreateUploadEndorse(String policyID, DTOList object) throws Exception {
        superEdit(policyID);

        //checkActiveEffective();

        //if (!policy.isEffective())
            //throw new RuntimeException("Policy not yet effective");

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());
        if(startDate.isEqual(endDate))
                policy.setStAllowSamePeriodFlag("Y");

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

            for (int k = 0; k < object.size(); k++) {
                uploadEndorsemenView objectUpload = (uploadEndorsemenView) object.get(k);

                boolean endorseObject = objx.getStOrderNo().trim().equalsIgnoreCase(objectUpload.getStOrderNo().trim());

                //BigDecimal premiKoasNet = BDUtil.sub(objx.getDbReference2(), objx.getDbReference9());

                if(endorseObject){
                    //KODE KOAS MENJADI
                    objx.setStReference8(objectUpload.getStKoasuransiMenjadi());

                    //PREMI KOAS
                    objx.setStReference14("Y");
                    objx.setDbReference1(objectUpload.getDbRateKoas());
                    objx.setDbReference2(objectUpload.getDbPremiKoas());
                    
                    //KOMISI KOAS
                    objx.setStReference11("N");
                    objx.setDbReference7(objectUpload.getDbRateKomisiKoas());
                    objx.setDbReference9(objectUpload.getDbKomisiKoas());

                    //NO REKAP OBJEK
                    if(objectUpload.getStRecapNoObject()!=null){
                        objx.setStReference23("Y");
                        objx.setStRekapKreasi(objectUpload.getStRecapNoObject());
                    }
  
                    if(objectUpload.getStEndorseNoteObject()!=null)
                        objx.setStReference79(objectUpload.getStEndorseNoteObject());
                    
                    policy.setStEndorseNotes(objectUpload.getStEndorseNote());
                    policy.setStAdminNotes(objectUpload.getStRecapNo());

                    if(Tools.isYes(objectUpload.getStAutoApproveFlag())){
                        policy.setStEffectiveFlag("Y");
                        policy.setStPostedFlag("Y");
                        policy.setStApprovedWho(objectUpload.getStApprovedWho());
                        policy.setDtApprovedDate(new Date());
                        policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
                        policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());

                        if(objectUpload.getDtPolicyDate()!=null){
                            policy.setDtPolicyDate(objectUpload.getDtPolicyDate());
                            policy.setDtApprovedDate(objectUpload.getDtPolicyDate());
                        }
                    }
 
                }
            }

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

    public void btnSaveUpload() throws Exception {

        if(Tools.isYes(policy.getStEffectiveFlag()))
            approvalMode = true;
        else
            approvalMode = false;

        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStNextStatus())){
            defineTreaty();
        }

        if(!isInputPaymentDateMode() && !isEditKeteranganMode() && !editReasMode){
            policy.recalculate();

            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen()){
                if(!approvalMode){
                    policy.recalculateTreaty();
                    policy.validateExclusionRisk(false);
                    policy.validateTreaty(false);
                }
            }
            policy.validateDataUploadEndorse();
            policy.validate(false);
            validateJenisKredit();
        }

        if(isEditKeteranganMode()){
            policy.setPrincipal();
        }

//        getRemoteInsurance().saveApproveUpload(policy,policy.getStNextStatus(),approvalMode);

    }

    private void validateClaimItemApproval() throws Exception{
        final DTOList claimItem = policy.getClaimItems();

        boolean userCabang = false;

        if(UserManager.getInstance().getUser().getStBranch()!=null)
            if(!UserManager.getInstance().getUser().getStBranch().equalsIgnoreCase("00"))
                userCabang = true;

        for (int i = 0; i < claimItem.size(); i++) {
            InsurancePolicyItemsView itemClaim = (InsurancePolicyItemsView) claimItem.get(i);

            if(!itemClaim.isGratia() && !itemClaim.isSubrogasi()) continue;

            if(itemClaim.isGratia())
                if(userCabang)
                    throw new RuntimeException("User cabang tidak bisa melakukan approval klaim ex gratia, konfirmasi ke klaim kantor pusat untuk approval");

//            if(itemClaim.isSubrogasi())
//                if(userCabang)
//                    throw new RuntimeException("User cabang tidak bisa melakukan approval klaim subrogasi, konfirmasi ke klaim kantor pusat untuk approval");

        }

//        if(policy.isDataSubrogasi())
//            if(userCabang)
//                    throw new RuntimeException("User cabang tidak bisa melakukan approval klaim subrogasi, konfirmasi ke klaim kantor pusat untuk approval");

    }

    private void uploadTravel(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();
            setStSelectedObject(String.valueOf(getObjects().size() - 1));

            /*
             *  BISNIS
            BUATAN
            ALAMAT RESIKO
            KONDISI/SITUASI
            TANGGAL
             */

            HSSFCell cell0 = row.getCell(0);//Yang Dipertanggungkan (String)
            HSSFCell cell1 = row.getCell(1);//No. Kelompok (Group)  (String)
            HSSFCell cell2 = row.getCell(2);//No. KTP (String)
            HSSFCell cell3 = row.getCell(3);//No. Passport (String)
            HSSFCell cell4 = row.getCell(4);//Jenis Kelamin (String)
            HSSFCell cell5 = row.getCell(5);//Kota Kelahiran (String)
            HSSFCell cell6 = row.getCell(6);//Tanggal Lahir (Date)
            HSSFCell cell7 = row.getCell(7);//Umur (String)
            HSSFCell cell8 = row.getCell(8);//Maksud Perjalanan  (String)
            HSSFCell cell9 = row.getCell(9);//Asal Berangkat (String)
            HSSFCell cell10 = row.getCell(10);//Tempat  Tujuan (String)
            HSSFCell cell11 = row.getCell(11);//Jangka Waktu (String)
            HSSFCell cell12 = row.getCell(12);//Awal Pertanggungan  (Date)
            HSSFCell cell13 = row.getCell(13);//Akhir Pertanggungan  (Date)
            HSSFCell cell14 = row.getCell(14);//Nama Perusahaan  (String)
            HSSFCell cell15 = row.getCell(15);//Alamat Rumah (String)
            HSSFCell cell16 = row.getCell(16);//Alamat Kantor (String)
            HSSFCell cell17 = row.getCell(17);//Nama Ahli Waris (String)
            HSSFCell cell18 = row.getCell(18);//Hubungan  (String)
            HSSFCell cell19 = row.getCell(19);//Alamat Ahli Waris  (String)
            HSSFCell cell20 = row.getCell(20);//Pekerjaan Tertanggung (String)
            HSSFCell cell21 = row.getCell(21);//Paket Travel (String)
            HSSFCell cell22 = row.getCell(22);//Tipe Travel (String)
            HSSFCell cell23 = row.getCell(23);//Lama Perjalanan (String)

            if (cell0.getCellType() != cell0.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Yang Dipertanggungkan Harus String/Character");
            }
            if (cell5.getCellType() != cell5.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Kota Kelahiran Harus String/Character");
            }
            if (cell8.getCellType() != cell8.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Maksud Perjalanan Harus String/Character");
            }
            if (cell9.getCellType() != cell9.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Asal Berangkat Harus String/Character");
            }
            if (cell10.getCellType() != cell10.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Tempat Tujuan Harus String/Character");
            }
            if (cell11.getCellType() != cell11.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Jangka Waktu Harus String/Character");
            }
            if (cell14.getCellType() != cell14.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Perusahaan Harus String/Character");
            }
            if (cell15.getCellType() != cell15.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Alamat Rumah Harus String/Character");
            }
            if (cell16.getCellType() != cell16.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Alamat Kantor Harus String/Character");
            }
            if (cell17.getCellType() != cell17.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Ahli Waris Harus String/Character");
            }
            if (cell18.getCellType() != cell18.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Hubungan Harus String/Character");
            }
            if (cell19.getCellType() != cell19.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Alamat Ahli Waris Harus String/Character");
            }
            if (cell20.getCellType() != cell20.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Pekerjaan Tertanggung Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell0.getStringCellValue()); //Yang Dipertanggungkan
            getSelectedDefaultObject().setStReference2(cell1.getStringCellValue()); //No. Kelompok (Group)
            getSelectedDefaultObject().setStReference3(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference6(cell5.getStringCellValue()); //Kota Kelahiran
            getSelectedDefaultObject().setDtReference1(cell6.getDateCellValue()); //Tanggal Lahir
            getSelectedDefaultObject().setStReference19(cell7.getCellType() == cell7.CELL_TYPE_STRING ? cell7.getStringCellValue() : new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell8.getStringCellValue()); //Maksud Perjalanan
            getSelectedDefaultObject().setStReference21(cell9.getStringCellValue()); //Asal Berangkat
            getSelectedDefaultObject().setStReference8(cell10.getStringCellValue()); //Tempat  Tujuan
            getSelectedDefaultObject().setStReference9(cell11.getStringCellValue()); //Jangka Waktu
            getSelectedDefaultObject().setDtReference2(cell12.getDateCellValue()); //Awal Pertanggungan
            getSelectedDefaultObject().setDtReference3(cell13.getDateCellValue()); //Akhir Pertanggungan
            getSelectedDefaultObject().setStReference10(cell14.getStringCellValue()); //Nama Perusahaan
            getSelectedDefaultObject().setStReference11(cell15.getStringCellValue()); //Alamat Rumah
            getSelectedDefaultObject().setStReference12(cell16.getStringCellValue()); //Alamat Kantor
            getSelectedDefaultObject().setStReference13(cell17.getStringCellValue()); //Nama Ahli Waris
            getSelectedDefaultObject().setStReference14(cell18.getStringCellValue()); //Hubungan
            getSelectedDefaultObject().setStReference15(cell19.getStringCellValue()); //Alamat Ahli Waris
            getSelectedDefaultObject().setStReference16(cell20.getStringCellValue()); //Pekerjaan Tertanggung
            getSelectedDefaultObject().setStReference17(cell21.getCellType() == cell21.CELL_TYPE_STRING ? cell21.getStringCellValue() : new BigDecimal(cell21.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference18(cell22.getCellType() == cell22.CELL_TYPE_STRING ? cell22.getStringCellValue() : new BigDecimal(cell22.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference20(cell23.getCellType() == cell23.CELL_TYPE_STRING ? cell23.getStringCellValue() : new BigDecimal(cell23.getNumericCellValue()).toString());

            //getSelectedDefaultObject().setStRiskCategoryID("536");
            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);


            doAddLampiranSumInsured("301", new BigDecimal(0));

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadTerrorismSabotage(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi3");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            //if(cellControl==null) break;

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

            HSSFCell cell1 = row.getCell(1);//penggunaan
            HSSFCell cell2 = row.getCell(2);//kelas kontruksi
            HSSFCell cell3 = row.getCell(3);//kode resiko
            HSSFCell cell4 = row.getCell(40);//penerangan
            HSSFCell cell5 = row.getCell(5);//alamat
            HSSFCell cell6 = row.getCell(6);//kode pos

            HSSFCell cell8 = row.getCell(8);//nama
            HSSFCell cellProvinsi = row.getCell(11);//provinsi

            HSSFCell cell9 = row.getCell(41);//kode resiko

            HSSFCell cellPeriodStart = row.getCell(36);//periode awal
            HSSFCell cellPeriodEnd = row.getCell(37);//periode akhir
//
//            HSSFCell cellKodeResiko1 = row.getCell(10);//kode resiko 1
//            HSSFCell cellKodeResiko2 = row.getCell(11);//kode resiko 2
//            HSSFCell cellKodeResiko3 = row.getCell(12);//kode resiko 3
//
//            HSSFCell cellNONota = row.getCell(16);//no nota debet
//            HSSFCell cellNOKPAK = row.getCell(17);//no kpak

            HSSFCell cellPeriodRate = row.getCell(12);//period rate
            HSSFCell cellPeriodBaseID = row.getCell(68);//period base
            HSSFCell cellPremiumFactor = row.getCell(69);//premium factor


            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

            String kodePos = policy.getStKodePos(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            if (kodePos != null) {
                getSelectedDefaultObject().setStReference9(kodePos);
            }
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());
            //getSelectedDefaultObject().setStReference7(cell7.getCellType()==cell7.CELL_TYPE_STRING?cell7.getStringCellValue():new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStRiskCategoryID(cell9.getCellType() == cell9.CELL_TYPE_STRING ? cell9.getStringCellValue() : new BigDecimal(cell9.getNumericCellValue()).toString());

            if (cellPeriodStart != null) {
                if (cellPeriodStart.getCellType() == cellPeriodStart.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellPeriodStart.getDateCellValue());
                }
            }

            if (cellPeriodEnd != null) {
                if (cellPeriodEnd.getCellType() == cellPeriodEnd.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference2(cellPeriodEnd.getDateCellValue());
                }
            }

            if (cellProvinsi != null) {
                getSelectedDefaultObject().setStReference14(cellProvinsi.getStringCellValue());
            }

//            if(cellKodeResiko1!=null)
//                getSelectedDefaultObject().setStRiskCategoryCode1(cellKodeResiko1.getCellType()==cellKodeResiko1.CELL_TYPE_STRING?cellKodeResiko1.getStringCellValue():new BigDecimal(cellKodeResiko1.getNumericCellValue()).toString());
//
//            if(cellKodeResiko2!=null)
//                getSelectedDefaultObject().setStRiskCategoryCode2(cellKodeResiko2.getCellType()==cellKodeResiko2.CELL_TYPE_STRING?cellKodeResiko2.getStringCellValue():new BigDecimal(cellKodeResiko2.getNumericCellValue()).toString());
//
//            if(cellKodeResiko3!=null)
//                getSelectedDefaultObject().setStRiskCategoryCode3(cellKodeResiko3.getCellType()==cellKodeResiko3.CELL_TYPE_STRING?cellKodeResiko3.getStringCellValue():new BigDecimal(cellKodeResiko3.getNumericCellValue()).toString());
//
//            if(cellNONota != null)
//                getSelectedDefaultObject().setStReference14(cellNONota.getStringCellValue());
//
//            if(cellNOKPAK != null)
//                getSelectedDefaultObject().setStReference15(cellNOKPAK.getStringCellValue());

            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }


            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(42);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(43);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(16);//tsi

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(44);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(45);//ins_tsi_cat_id
            HSSFCell cell_TSI2 = row.getCell(18);//tsi

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(46);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(47);//ins_tsi_cat_id
            HSSFCell cell_TSI3 = row.getCell(20);//tsi

            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
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
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(48);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(49);//RATE
            HSSFCell cell_Rate1 = row.getCell(50);//premi
            HSSFCell cell_Premi1 = row.getCell(51);//tsi

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (rate1 != null) {
                rate1 = rate1.setScale(3, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(52);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(53);//RATE
            HSSFCell cell_Rate2 = row.getCell(54);//premi
            HSSFCell cell_Premi2 = row.getCell(55);//tsi

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (rate2 != null) {
                rate2 = rate2.setScale(3, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(56);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(57);//RATE
            HSSFCell cell_Rate3 = row.getCell(58);//premi
            HSSFCell cell_Premi3 = row.getCell(59);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (rate3 != null) {
                rate3 = rate3.setScale(3, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //cover 4
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(60);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(61);//RATE
            HSSFCell cell_Rate4 = row.getCell(62);//premi
            HSSFCell cell_Premi4 = row.getCell(63);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            final BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());

            if (rate4 != null) {
                rate4 = rate4.setScale(3, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, null);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, null);
            }

            //cover 5
            HSSFCell cell_Ins_Cvpt_ID5 = row.getCell(64);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID5 = row.getCell(65);//RATE
            HSSFCell cell_Rate5 = row.getCell(66);//premi
            HSSFCell cell_Premi5 = row.getCell(67);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate5.getNumericCellValue())) ? null : new BigDecimal(cell_Rate5.getNumericCellValue());
            final BigDecimal premi5 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi5.getNumericCellValue())) ? null : new BigDecimal(cell_Premi5.getNumericCellValue());

            if (rate5 != null) {
                rate5 = rate5.setScale(3, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID5.getCellType() == cell_Ins_Cvpt_ID5.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID5.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID5.getStringCellValue(), rate5, premi5, null);
                }
            } else if (cell_Ins_Cvpt_ID5.getCellType() == cell_Ins_Cvpt_ID5.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID5.getNumericCellValue()).toString(), rate5, premi5, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

    public void onNewClaimCoinsuranceCover() throws Exception {
        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

        co.markNew();

        co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);

        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);

        getCoinsCoverage().add(co);

    }

    public void onDeleteClaimCoinsuranceCover() throws Exception {
        getCoinsCoverage().delete(Integer.parseInt(coinsCoverIndex));
    }

    public void onChgCurrencyClaim() throws Exception {
        policy.setDbCurrencyRateClaim(
                CurrencyManager.getInstance().getRate(
                policy.getStClaimCurrency(),
                policy.getDtPolicyDate()
                )
                );

        policy.setStClaimCurrency(policy.getStClaimCurrency());

    }

    public InsuranceClaimCauseView getInsuranceClaimCause(String stClaimCauseID) {
        return (InsuranceClaimCauseView) DTOPool.getInstance().getDTO(InsuranceClaimCauseView.class, stClaimCauseID);
    }

    public void validateClaimCause()throws Exception{
        InsuranceClaimCauseView cause = getInsuranceClaimCause(policy.getStClaimCauseID());

        if(!cause.isActive()){
            policy.setStClaimCauseID(null);
            throw new RuntimeException("Penyebab Klaim Sudah Tidak Aktif, Tidak Boleh Dipilih");
        }
            
    }

    private void uploadKPR(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(0);//DEBITUR (String)
            HSSFCell cell2 = row.getCell(1);//NAMA BANK (String)
            HSSFCell cell3 = row.getCell(2);//NO SPPB (String)
            HSSFCell cell4 = row.getCell(3);//TGL SPPB (date)
            HSSFCell cell5 = row.getCell(11);//BANK ID (date)

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom DEBITUR Harus String/Character");
            }

            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom NAMA BANK Harus String/Character");
            }

            if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom NO SPPB Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell3.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());

            if (cell4 != null) {
                getSelectedDefaultObject().setDtReference1(cell4.getDateCellValue());
            }

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            HSSFCell cellTSICIT = row.getCell(8);//get rate cover
            if (cellTSICIT.getCellType() != cellTSICIT.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI CIT Harus Numeric");
            }

            double tsiCIT = cellTSICIT.getNumericCellValue();
            BigDecimal tsiCIT2 = new BigDecimal(tsiCIT);

            doAddLampiranSumInsured("573", tsiCIT2);
            //doAddLampiranCoverKreasi(cvptID3, ratePA2, PremiPA2);
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(6);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(7);//RATE
            HSSFCell cell_Rate1 = row.getCell(9);//premi
            HSSFCell cell_Premi1 = row.getCell(10);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

        }
    }

    public void applyDateHeader() throws Exception{
        final DTOList objects = policy.getObjects();

        DateTime tglAwalTerawal = new DateTime();
        DateTime tglAkhirTerakhir = new DateTime();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            if(i==0){

                        if(policy.getStPolicyTypeID().equalsIgnoreCase("21") || policy.getStPolicyTypeID().equalsIgnoreCase("59")|| policy.getStPolicyTypeID().equalsIgnoreCase("64")
                                || policy.getStPolicyTypeID().equalsIgnoreCase("4")){
                            tglAwalTerawal = new DateTime(obj.getDtReference2());
                            tglAkhirTerakhir = new DateTime(obj.getDtReference3());
                        }


                        if(policy.getStPolicyTypeID().equalsIgnoreCase("3") || policy.getStPolicyTypeID().equalsIgnoreCase("1") || policy.getStPolicyTypeID().equalsIgnoreCase("81")){
                            if(obj.getDtReference1()!=null){
                                tglAwalTerawal = new DateTime(obj.getDtReference1());
                                tglAkhirTerakhir = new DateTime(obj.getDtReference2());
                            }
                        }
            }

            DateTime tglAwal2 = new DateTime(obj.getDtReference2());
            DateTime tglAkhir2 = new DateTime(obj.getDtReference3());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21") || policy.getStPolicyTypeID().equalsIgnoreCase("59")|| policy.getStPolicyTypeID().equalsIgnoreCase("64")
                        || policy.getStPolicyTypeID().equalsIgnoreCase("4")){
                    tglAwal2 = new DateTime(obj.getDtReference2());
                    tglAkhir2 = new DateTime(obj.getDtReference3());
            }


            if(policy.getStPolicyTypeID().equalsIgnoreCase("3") || policy.getStPolicyTypeID().equalsIgnoreCase("1") || policy.getStPolicyTypeID().equalsIgnoreCase("81")){
                if(obj.getDtReference1()!=null){
                    tglAwal2 = new DateTime(obj.getDtReference1());
                    tglAkhir2 = new DateTime(obj.getDtReference2());
                }
            }

            if(tglAwalTerawal.isAfter(tglAwal2))
                tglAwalTerawal = tglAwal2;

            if(tglAkhirTerakhir.isBefore(tglAkhir2))
                tglAkhirTerakhir = tglAkhir2;

        }

        policy.setDtPeriodStart(tglAwalTerawal.toDate());
        policy.setDtPeriodEnd(tglAkhirTerakhir.toDate());

    }

    private void uploadMB(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(0);//DEBITUR (String)
            HSSFCell cell2 = row.getCell(1);//NAMA BANK (String)
            HSSFCell cell3 = row.getCell(2);//NO SPPB (String)
            HSSFCell cell4 = row.getCell(3);//TGL SPPB (date)
            HSSFCell cell5 = row.getCell(4);//TGL SPPB (date)

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Pertanggungan Harus String/Character");
            }

            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Jenis Usaha Harus String/Character");
            }

            if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Lokasi Resiko Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell3.getStringCellValue()); //nama

            String kodePos = policy.getStKodePos(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference4(kodePos);

            getSelectedDefaultObject().setStRiskCategoryID(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(5);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(6);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(7);//tsi

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }

            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(8);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(9);//ins_tsi_cat_id
            HSSFCell cell_TSI2 = row.getCell(10);//tsi

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(11);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(12);//RATE
            HSSFCell cell_Rate1 = row.getCell(13);//premi
            HSSFCell cell_Premi1 = row.getCell(14);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(15);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(16);//RATE
            HSSFCell cell_Rate2 = row.getCell(17);//premi
            HSSFCell cell_Premi2 = row.getCell(18);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(19);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(20);//RATE
            HSSFCell cell_Rate3 = row.getCell(21);//premi
            HSSFCell cell_Premi3 = row.getCell(22);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    public void approvalRisk(String policyID) throws Exception {
        edit(policyID);

        if(!policy.isStatusSPPA())
            throw new RuntimeException("Persetujuan Analisa resiko hanya bisa pada level SPPA");

        policy.setStRiskApproved("Y");

        super.setReadOnly(true);
        rdtabs.setActiveTab("TAB_ANALYSIS");

        //approvalMode = true;

    }

    public void btnApproveRisk() throws Exception {

        if(policy.isStatusSPPA()){

                //checkPassword();

                BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),SessionManager.getInstance().getUserID());

                if(isApprovalByDirectorMode()){
                    transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"dirtek");
                }

                policy.validateTSILimit(transactionLimit);

                //final BigDecimal transactionLimitPerPrincipal = getTransactionLimit("ACCEPT_PRINCIPAL", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
                policy.validateAkumulasiResikoLimit(transactionLimit);

                BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);

                BigDecimal premiNett = null;
                BigDecimal totalKomisi = BDUtil.zero;
                BigDecimal totalDiskon = BDUtil.zero;
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm1()),policy.getDbNDComm2());
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm3()),policy.getDbNDComm4());
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDFeeBase1()),policy.getDbNDFeeBase2());
                premiNett = BDUtil.sub(policy.getDbPremiTotal(), totalKomisi);
                totalDiskon = BDUtil.add(policy.getDbNDDisc1(), policy.getDbNDDisc2());
                premiNett = BDUtil.sub(premiNett, totalDiskon);

                comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), premiNett), policy.getDbPremiTotal(),4);

                BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew());

                boolean overLimit = false;
                BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());

                String errorMessage="";
                if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                    overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Grup Perusahaan "+ policy.getEntity().getStEntityName() +" Terlalu Tinggi";
                }else{
                    overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Melebihi Kewenangan ("+comissionRatio +" > "+comissionLimit+")";
                }

                if (overLimit){
                    policy.setStUnderwritingFinishFlag("N");
                    policy.setStEffectiveFlag("N");
                    policy.setStRiskApproved("N");
//                    getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                    throw new RuntimeException(errorMessage);
                }
            }

        policy.setStRiskApprovedWho(UserManager.getInstance().getUser().getStUserID());
        policy.setDtRiskApprovedDate(new Date());
        policy.setStRiskApproved("Y");

        btnSave();
    }

    /**
     * @return the riskApprovalMode
     */
    public boolean isRiskApprovalMode() {
        return riskApprovalMode;
    }

    /**
     * @param riskApprovalMode the riskApprovalMode to set
     */
    public void setRiskApprovalMode(boolean riskApprovalMode) {
        this.riskApprovalMode = riskApprovalMode;
    }

    public void sendApprovalLetters() throws Exception{

          ApprovalView notif = loadApprove(policy.getStPolicyID());

          final DTOList object = policy.getObjects();

              String validasiCabang = "N";
              String validasiInduk = "N";
              String validasiHO = "N";
              String setujuiResiko = "N";
              String statusInduk = "";

              for (int i = 0; i < object.size(); i++) {
                  InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object.get(i);

                  if(Tools.isYes(obj.getStValidasiCabang()))
                      validasiCabang = "Y";

                  if(Tools.isYes(obj.getStValidasiCabangInduk())){
                       validasiInduk = "Y";
                       //statusInduk = "HEAD OFFICE";
                  }

                  if(Tools.isYes(obj.getStValidasiKantorPusat())){
                      validasiHO = "Y";
                  }

                  if(Tools.isYes(obj.getStRiskApproved())){
                      setujuiResiko = "Y";
                  }

              }

          if(notif==null){

                  UserSession us = SessionManager.getInstance().getSession();

                  ApprovalView incoming = new ApprovalView();

                  incoming.setStRefNo("APPROVE-US-" + policy.getStPolicyID());
                  incoming.setStSender(us.getStUserID());
                  incoming.setStSenderName(us.getStUserName());
                  incoming.setDtLetterDate(new Date());
                  incoming.setStSubject("APPROVE-"+ policy.getStPolicyID());
                  incoming.setStNote("Ini adalah notifikasi otomatis :\n\n Permintaan approval SPPA overlimit ID : "+ policy.getStPolicyID());
                  incoming.setStCostCenterCode(policy.getStCostCenterCode());
                  incoming.setStPolicyID(policy.getStPolicyID());
                  incoming.setStStatus("PARENT");

                  if(Tools.isYes(validasiCabang))
                      incoming.setStValidasiCabang("Y");

                  //KIRIM NOTIF KE CABANG INDUK
                  GLCostCenterView cabang = policy.getCostCenter(policy.getStCostCenterCode());

                  if(!cabang.getStSubCostCenterCode().equalsIgnoreCase(cabang.getStCostCenterCode()))
                        incoming.setStReceiver(Parameter.readString("BRANCH_"+ cabang.getStSubCostCenterCode()));
                  //else
                        //incoming.setStReceiver(Parameter.readString("BRANCH_"+ cabang.getStSubCostCenterCode()));

                  incoming.setStReceiver(policy.getStNotificationUserID());
                  incoming.setStReadFlag("N");
                  incoming.setStDeleteFlag("N");

                  incoming.markNew();
                  getRemoteEntityManager2().saveApprove(incoming);

                  policy.setStNotificationUserID(null);
          }

          if(notif!=null){
              
                SQLUtil S = new SQLUtil();

                String query = "update ins_approval_notification set validasi_cabang_f = '"+ validasiCabang +"', validasi_induk_f = '"+ validasiInduk +"', validasi_headoffice_f = '"+ validasiHO +"',risk_analysis_eff_f = '"+ setujuiResiko +"'";

                if(!statusInduk.equalsIgnoreCase(""))
                    query = query + ", status = '"+ statusInduk +"' ";

                PreparedStatement PS = S.setQuery( query + " where pol_id = ?");

                PS.setObject(1,policy.getStPolicyID());

                int j = PS.executeUpdate();

                S.release();
          }

          
          //close();
    }

    public ApprovalView loadApprove(String policyID) throws Exception {
      final ApprovalView ent = (ApprovalView)ListUtil.getDTOListFromQuery(
                    "select * from ins_approval_notification where pol_id = ?",
                    new Object [] {policyID},
                    ApprovalView.class
            ).getDTO();

      return ent;
    }

    /**
     * @return the showNotifLetter
     */
    public boolean isShowNotifLetter() {
        return showNotifLetter;
    }

    /**
     * @param showNotifLetter the showNotifLetter to set
     */
    public void setShowNotifLetter(boolean showNotifLetter) {
        this.showNotifLetter = showNotifLetter;
    }

    public boolean validateTSILimit(BigDecimal transactionLimit, InsurancePolicyView pol) throws Exception{
        final DTOList object = pol.getObjects();

        boolean enoughLimit = false;

        for(int i=0;i<object.size();i++){
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            InsurancePolicyObjDefaultView object2 = (InsurancePolicyObjDefaultView) obj;

            final BigDecimal transactionLimitPerRiskCode = pol.getTransactionLimitPerRiskCode("ACCEPT", pol.getStRiskClass(),SessionManager.getInstance().getUserID(), obj.getStRiskCategoryID());

            enoughLimit = false;

            BigDecimal tsiObject = BDUtil.mul(obj.getDbObjectInsuredAmount(),pol.getDbCurrencyRate());

            if(!BDUtil.isZeroOrNull(object2.getDbLimitOfLiability2()))
                tsiObject = BDUtil.mul(object2.getDbLimitOfLiability2(),pol.getDbCurrencyRate());

            if(!BDUtil.isZeroOrNull(transactionLimitPerRiskCode))
                enoughLimit = Tools.compare(transactionLimitPerRiskCode, tsiObject)>=0;
            else
                enoughLimit = Tools.compare(transactionLimit, tsiObject)>=0;

            if(!enoughLimit){
                pol.setStUnderwritingFinishFlag("N");
                pol.setStEffectiveFlag("N");
                policy.setStRiskApproved("N");
                getRemoteInsurance().saveInputPaymentDate(pol, approvalMode);
                setShowNotifLetter(true);

                GLCostCenterView cabang = policy.getCostCenter(policy.getStCostCenterCode());

                if(!cabang.getStSubCostCenterCode().equalsIgnoreCase(cabang.getStCostCenterCode()))
                        pol.setStNotificationUserID(Parameter.readString("BRANCH_"+ cabang.getStSubCostCenterCode()));

                throw new RuntimeException("Nilai TSI Objek No ("+(i+1)+") "+ object2.getStReference1()+"  Melebihi Limit Kewenangan Anda ("+ tsiObject +" > "+transactionLimit +"), isi dulu analisa resiko");

            }
        }

        return enoughLimit;
    }

    /**
     * @return the bentukPolisMode
     */
    public boolean isBentukPolisMode() {
        return bentukPolisMode;
    }

    /**
     * @param bentukPolisMode the bentukPolisMode to set
     */
    public void setBentukPolisMode(boolean bentukPolisMode) {
        this.bentukPolisMode = bentukPolisMode;
    }

    public void cekLimitLevel() throws Exception {

        if(policy.isStatusSPPA()){

                BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),SessionManager.getInstance().getUserID());

                if(isApprovalByDirectorMode()){
                    transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"dirtek");
                }

                policy.validateTSILimit(transactionLimit);

                //final BigDecimal transactionLimitPerPrincipal = getTransactionLimit("ACCEPT_PRINCIPAL", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
                policy.validateAkumulasiResikoLimit(transactionLimit);

                BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);

                BigDecimal premiNett = null;
                BigDecimal totalKomisi = BDUtil.zero;
                BigDecimal totalDiskon = BDUtil.zero;
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm1()),policy.getDbNDComm2());
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm3()),policy.getDbNDComm4());
                totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDFeeBase1()),policy.getDbNDFeeBase2());
                premiNett = BDUtil.sub(policy.getDbPremiTotal(), totalKomisi);
                totalDiskon = BDUtil.add(policy.getDbNDDisc1(), policy.getDbNDDisc2());
                premiNett = BDUtil.sub(premiNett, totalDiskon);

                comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), premiNett), policy.getDbPremiTotal(),4);

                BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew());

                boolean overLimit = false;
                BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());

                String errorMessage="";
                if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                    overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Grup Perusahaan "+ policy.getEntity().getStEntityName() +" Terlalu Tinggi";
                }else{
                    overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                    if(overLimit) errorMessage = "Jumlah Komisi/Diskon Melebihi Kewenangan ("+comissionRatio +" > "+comissionLimit+")";
                }

                if (overLimit){
                    policy.setStUnderwritingFinishFlag("N");
                    policy.setStEffectiveFlag("N");
//                    getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                    throw new RuntimeException(errorMessage);
                }
        }

    }

    /**
     * @return the kirimNotifikasi
     */
    public String isKirimNotifikasi() {
        return kirimNotifikasi;
    }

    /**
     * @param kirimNotifikasi the kirimNotifikasi to set
     */
    public void setKirimNotifikasi(String kirimNotifikasi) {
        this.kirimNotifikasi = kirimNotifikasi;
    }

    public boolean kirimNotifikasi() {
        return Tools.isYes(kirimNotifikasi);
    }

    /**
     * @return the canEditValidasiCabang
     */
    public boolean isCanEditValidasiCabang() {
        return canEditValidasiCabang;
    }

    /**
     * @param canEditValidasiCabang the canEditValidasiCabang to set
     */
    public void setCanEditValidasiCabang(boolean canEditValidasiCabang) {
        this.canEditValidasiCabang = canEditValidasiCabang;
    }

    /**
     * @return the canEditValidasiInduk
     */
    public boolean isCanEditValidasiInduk() {
        return canEditValidasiInduk;
    }

    /**
     * @param canEditValidasiInduk the canEditValidasiInduk to set
     */
    public void setCanEditValidasiInduk(boolean canEditValidasiInduk) {
        this.canEditValidasiInduk = canEditValidasiInduk;
    }

    /**
     * @return the canEditValidasiPusat
     */
    public boolean isCanEditValidasiPusat() {
        return canEditValidasiPusat;
    }

    /**
     * @param canEditValidasiPusat the canEditValidasiPusat to set
     */
    public void setCanEditValidasiPusat(boolean canEditValidasiPusat) {
        this.canEditValidasiPusat = canEditValidasiPusat;
    }

    public void cekClosingStatus(String status) throws Exception{

        final boolean blockClosingEndOfDay = Parameter.readBoolean("UWRIT_CLOSING_END_OF_DAY");

        if(blockClosingEndOfDay){
            
                final ClosingDetailView cls = PeriodManager.getInstance().getClosing(DateUtil.getYear(DateUtil.getNewDate()), policy.getStCostCenterCode());

                if(cls!=null){

                        if(status.equalsIgnoreCase("PROPOSAL"))
                            if(cls==null){
                                policy.setStCostCenterCode(null);
                                throw new RuntimeException("Tabel setting closing belum diisi");
                            }

                        if(cls==null)
                            throw new RuntimeException("Tabel setting closing belum diisi");

                        //DateTime dtBatasInput = new DateTime(cls.getDtEditEndDate());
                        //DateTime dtBatasReverseSetujui = new DateTime(cls.getDtReverseEndDate());
                        //DateTime now = new DateTime();

                        DateTimeZone timeZoneWIB = DateTimeZone.forID( "Asia/Bangkok" );

                        String batasJamInput [] = cls.getStEditEndTime().split("[\\:]");
                        int jam = Integer.parseInt(batasJamInput[0]);
                        int menit = Integer.parseInt(batasJamInput[1]);

                        String batasJamSetujui [] = cls.getStReverseEndTime().split("[\\:]");
                        int jam2 = Integer.parseInt(batasJamSetujui[0]);
                        int menit2 = Integer.parseInt(batasJamSetujui[1]);

                        DateTime dtBatasInput = new DateTime().withZone(timeZoneWIB).withTime(jam, menit, 0, 0);
                        DateTime dtBatasReverseSetujui = new DateTime().withZone(timeZoneWIB).withTime(jam2, menit2, 0, 0);
                        DateTime now = new DateTime().withZone(timeZoneWIB);

                        logger.logDebug("########### new date : "+ DateUtil.getNewDate());
                        logger.logDebug("########### batas input : "+ dtBatasInput);
                        logger.logDebug("########### batas reverse : "+ dtBatasReverseSetujui);
                        logger.logDebug("########### now : "+ now);

                        if(status.equalsIgnoreCase("PROPOSAL"))
                            if(dtBatasInput.isBefore(now)){
                                policy.setStCostCenterCode(null);
                                throw new RuntimeException("Tidak bisa input data karena sudah lewat batas jam input "+ DateUtil.getDateTimeStr2(dtBatasInput.toDate()));
                            }

                        if(status.equalsIgnoreCase("PROPOSAL"))
                            if(dtBatasInput.isBefore(now)){
                                policy.setStCostCenterCode(null);
                                throw new RuntimeException("Tidak bisa input data karena sudah lewat batas jam input "+ DateUtil.getDateTimeStr2(dtBatasInput.toDate()));
                            }

                        if(status.equalsIgnoreCase("INPUT"))
                            if(dtBatasInput.isBefore(now))
                                throw new RuntimeException("Tidak bisa input data karena sudah lewat batas jam input "+ DateUtil.getDateTimeStr2(dtBatasInput.toDate()));

                        if(status.equalsIgnoreCase("APPROVE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa setujui data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));

                        if(status.equalsIgnoreCase("REVERSE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa reverse data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));
                }
                

        }
        

    }


    public void applyPeriodToAll() throws Exception{
        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21") || policy.getStPolicyTypeID().equalsIgnoreCase("59")|| policy.getStPolicyTypeID().equalsIgnoreCase("64")
                                || policy.getStPolicyTypeID().equalsIgnoreCase("4")){
                obj.setDtReference2(policy.getDtPeriodStart());
                obj.setDtReference3(policy.getDtPeriodEnd());
            }

             if(policy.getStPolicyTypeID().equalsIgnoreCase("3") || policy.getStPolicyTypeID().equalsIgnoreCase("1") || policy.getStPolicyTypeID().equalsIgnoreCase("81")){
                 obj.setDtReference1(policy.getDtPeriodStart());
                 obj.setDtReference2(policy.getDtPeriodEnd());
             }

        }

    }

    /**
     * @return the coverageLine
     */
    public String getCoverageLine() {
        return coverageLine;
    }

    /**
     * @param coverageLine the coverageLine to set
     */
    public void setCoverageLine(String coverageLine) {
        this.coverageLine = coverageLine;
    }

    public void applyAllNotDouble()throws Exception{
        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.setStReference15("Y");
        }
    }

    /**
     * @return the showRIPerils
     */
    public boolean isShowRIPerils() {
        return showRIPerils;
    }

    /**
     * @param showRIPerils the showRIPerils to set
     */
    public void setShowRIPerils(boolean showRIPerils) {
        this.showRIPerils = showRIPerils;
    }

    public void showRIPerils(){
        setShowRIPerils(!showRIPerils);
    }

    public boolean isShowRIInstallment() {
        return showRIInstallment;
    }

    /**
     * @param showRIPerils the showRIPerils to set
     */
    public void setShowRIInstallment(boolean showRIInstallment) {
        this.showRIInstallment = showRIInstallment;
    }

    public void showRIInstallment(){
        setShowRIInstallment(!showRIInstallment);
    }
    
    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
        .create();
    }

    public void btnGetInward() throws Exception{
        InsurancePolicyInwardView invoice = getRemoteAccountReceivable().getARInvoiceInwardByPolNo(getPolicy().getStPolicyNo());

        final DTOList details = invoice.getDetails();

        BigDecimal premiGross = null;
        BigDecimal komisi = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

            if(det.isPremiGross2()) premiGross = det.getDbEnteredAmount();
            if(det.isCommission2()) komisi = det.getDbEnteredAmount();

        }

        EntityView ent = getEntity(invoice.getStEntityID());

        getPolicy().setStEntityID(ent.getStEntityID());
        getPolicy().setStCustomerName(ent.getStEntityName());
        getPolicy().setStCustomerAddress(ent.getStAddress());

        getPolicy().setStProducerID(ent.getStEntityID());
        getPolicy().setStProducerName(ent.getStEntityName());
        getPolicy().setStProducerAddress(ent.getStAddress());

        getPolicy().setDtPeriodStart(invoice.getDtAttrPolicyPeriodStart());
        getPolicy().setDtPeriodEnd(invoice.getDtAttrPolicyPeriodEnd());

        getPolicy().setStCurrencyCode(invoice.getStCurrencyCode());
        getPolicy().setDbCurrencyRate(invoice.getDbCurrencyRate());

        defaultPeriod();

        doNewLampiranObject();

        InsuranceTSIPolTypeView tsi = getTSICat("79");

        doAddLampiranSumInsured(tsi.getStInsuranceTSIPolTypeID(), invoice.getDbAttrPolicyTSI());

        InsuranceCoverPolTypeView cover = getCoverPolType();

        doAddLampiranCoverKreasi(cover.getStInsuranceCoverPolTypeID(), null, premiGross);

        if(komisi!=null)  addKomisiInwardAutomatic(komisi);

        final String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):Parameter.readString("UWRIT_DEF_OTREATY");

        getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        
        selectObject();
    }

    public InsuranceTSIPolTypeView getTSICat(String stInsTSICatID) throws Exception {
      final InsuranceTSIPolTypeView tsi = (InsuranceTSIPolTypeView)ListUtil.getDTOListFromQuery(
                    "select * from ins_tsicat_poltype where pol_type_id = ? and ins_tsi_cat_id = ?",
                    new Object [] {getPolicy().getStPolicyTypeID(), stInsTSICatID},
                    InsuranceTSIPolTypeView.class
            ).getDTO();

      return tsi;
    }

    public InsuranceCoverPolTypeView getCoverPolType() throws Exception {
      final InsuranceCoverPolTypeView cover = (InsuranceCoverPolTypeView)ListUtil.getDTOListFromQuery(
                    "select * from ins_cover_poltype where pol_type_id = ? and cover_category = 'MAIN' and coalesce(active_flag,'Y') <> 'N'",
                    new Object [] {getPolicy().getStPolicyTypeID()},
                    InsuranceCoverPolTypeView.class
            ).getDTO();

      return cover;
    }

    public void uploadExcelRI()throws Exception{
        try {

            final DTOList konversiDocuments = policy.getKonversiDocuments();

            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(0);

            String fileID = doc.getStFilePhysic();

            FileView file = FileManager.getInstance().getFile(fileID);

            FileInputStream fis = new FileInputStream(file.getStFilePath());

            uploadReinsurance(fis, "2016");

            close();

        } catch (Exception e) {
            throw new RuntimeException("error upload excel reas = "+ e);
        }
    }

    private void uploadReinsurance(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetDetil = wb.getSheet("detil");

        if (sheetDetil == null) {
            throw new RuntimeException("Sheet detil Tidak Ditemukan !");
        }

        int rows = sheetDetil.getPhysicalNumberOfRows();
        for (int r = 1; r < rows; r++) {
            HSSFRow row = sheetDetil.getRow(r);

            //update detil
            HSSFCell cellTSI = row.getCell(11);
            HSSFCell cellPremi = row.getCell(12);
            HSSFCell cellRateKomisi = row.getCell(13);
            HSSFCell cellKomisi = row.getCell(14);
            HSSFCell cellRatePremi = row.getCell(19);
            HSSFCell cellInsPolTredetID = row.getCell(8);

            //String updateDetil = "update ins_pol_treaty_detail set tsi_amount="+ cellTSI.getNumericCellValue()!=null?cellTSI.getNumericCellValue():"null" +",premi_amount="+ cellPremi.getNumericCellValue() +",comm_rate="+ cellRateKomisi.getNumericCellValue() +",comm_amt="+ cellKomisi.getNumericCellValue() +",premi_rate="+ cellRatePremi.getNumericCellValue() +",edit_flag = 'Y' where ins_pol_tre_det_id = "+ cellInsPolTredetID.getStringCellValue() +";";



        }
    }

    public void doAddLampiranSumInsured2(String instsipolid, BigDecimal tsi, String tsidesc) throws Exception {

        //if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");

        final InsuranceSplitPolicyTSIView ptsi = new InsuranceSplitPolicyTSIView();

        ptsi.setStInsuranceTSIPolTypeID(instsipolid);

        if(tsidesc!=null)
                ptsi.setStTSICategoryDescription(tsidesc);

        //ptsi.setStDescription(tsidesc);

        ptsi.initializeDefaults();

        ptsi.setDbInsuredAmount(tsi);

        ptsi.markNew();

        selectedDefaultObject.getSuminsureds().add(ptsi);

    }

    private void uploadTP(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//nama
            HSSFCell cell2 = row.getCell(2);//tgl lahir
            HSSFCell cell3 = row.getCell(3);//tgl awal
            HSSFCell cell4 = row.getCell(4);//tgl akhir
            HSSFCell cell5 = row.getCell(5);//usia
            HSSFCell cell6 = row.getCell(8);//tsi
            HSSFCell cell7 = row.getCell(9);//premi

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cell7.getCellType() != cell7.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cell6.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured("500", b);

            double tes3 = cell7.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if (cell2 != null) {
                getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            }
            if (cell5 != null) {
                if (cell5.getCellType() == cell5.CELL_TYPE_NUMERIC) {
                    getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell5.getNumericCellValue())));
                } else if (cell5.getCellType() == cell5.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference2(cell5.getStringCellValue());
                }
            }

            if (cell3.getCellType() == cell3.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
            }

            if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
            }

            getSelectedDefaultObject().setDbReference4(b); //tsi
            getSelectedDefaultObject().setDbReference6(c); //premi

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(12);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellPremiPA = row.getCell(14);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi PA Kreasi Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cell7 != null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasi(cvptID3, null, premiPA2);

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    public boolean showRateFactor(){

        final String type_code = policy.getPolicyType() != null?policy.getPolicyType().getStPolicyTypeCode():"";

        boolean showRateFactor =  type_code.equalsIgnoreCase("OM_PROP") || type_code.equalsIgnoreCase("OM_VEH")? false:true;

        if(policy.isStatusHistory()) showRateFactor = true;

        return showRateFactor;
    }

    private void uploadSurety(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 5; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            //if(cellControl==null) break;

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

            HSSFCell cell1 = row.getCell(1);//principal id
            HSSFCell cell2 = row.getCell(2);//alamat principal
            HSSFCell cell3 = row.getCell(3);//pejabat principal
            HSSFCell cell4 = row.getCell(4);//jabatan principal
            HSSFCell cell5 = row.getCell(5);//surety id
            HSSFCell cell6 = row.getCell(6);//alamat surety
            HSSFCell cell7 = row.getCell(7);//pejabat surety
            HSSFCell cell8 = row.getCell(8);//jabatan surety

            HSSFCell cell9 = row.getCell(9);//no surat
            HSSFCell cellTglSurat = row.getCell(10);//tgl surat
            HSSFCell cell11 = row.getCell(11);//nama proyek
            HSSFCell cell12 = row.getCell(12);//tempat tender
            HSSFCell cell14 = row.getCell(14);//no kontrak
            HSSFCell cellTglKontrak = row.getCell(15);//tgl kontrak
            HSSFCell cell16 = row.getCell(16);//nilai kontrak
            HSSFCell cellKodeResiko = row.getCell(23);//kode resiko
            HSSFCell cellNoPolis = row.getCell(21);//nopolis rujukan

            String entID = cell1.getCellType() == cell1.CELL_TYPE_STRING ? cell1.getStringCellValue() : new BigDecimal(cell1.getNumericCellValue()).toString();
            String entityName = policy.getEntity2(entID).getStEntityName();
            getSelectedDefaultObject().setStReference1(entityName);
            getSelectedDefaultObject().setStReference1Desc(entityName);
            getSelectedDefaultObject().setStReference2(entID);

            getSelectedDefaultObject().setStReference3(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference6(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference7(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference8(cell7.getCellType() == cell7.CELL_TYPE_STRING ? cell7.getStringCellValue() : new BigDecimal(cell7.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference9(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference10(cell9.getCellType() == cell9.CELL_TYPE_STRING ? cell9.getStringCellValue() : new BigDecimal(cell9.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference14(cellNoPolis.getCellType() == cellNoPolis.CELL_TYPE_STRING ? cellNoPolis.getStringCellValue() : new BigDecimal(cellNoPolis.getNumericCellValue()).toString());

            if (policy.getStPolicyTypeID().equalsIgnoreCase("51")) {
                getSelectedDefaultObject().setStReference11(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference12(cell12.getCellType() == cell12.CELL_TYPE_STRING ? cell12.getStringCellValue() : new BigDecimal(cell12.getNumericCellValue()).toString());

            } else if (policy.getStPolicyTypeID().equalsIgnoreCase("52")) {
                getSelectedDefaultObject().setStReference11(cell14.getCellType() == cell14.CELL_TYPE_STRING ? cell14.getStringCellValue() : new BigDecimal(cell14.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference12(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference13(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

                if (cellTglKontrak != null) {
                    if (cellTglKontrak.getCellType() == cellTglKontrak.CELL_TYPE_STRING) {
                        getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellTglKontrak.getStringCellValue())); //awal
                    } else {
                        getSelectedDefaultObject().setDtReference2(cellTglKontrak.getDateCellValue());
                    }
                }
            } else if (policy.getStPolicyTypeID().equalsIgnoreCase("53")) {
                getSelectedDefaultObject().setStReference11(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference12(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());
                getSelectedDefaultObject().setDbReference2(new BigDecimal(cell16.getNumericCellValue()));

            } else if (policy.getStPolicyTypeID().equalsIgnoreCase("54")) {
                getSelectedDefaultObject().setStReference11(cell11.getCellType() == cell11.CELL_TYPE_STRING ? cell11.getStringCellValue() : new BigDecimal(cell11.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference12(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());

            }

            if (cellTglSurat != null) {
                if (cellTglSurat.getCellType() == cellTglSurat.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellTglSurat.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellTglSurat.getDateCellValue());
                }
            }

            if (cellKodeResiko != null) {
                getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(24);//ins_tsi_cat_id
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(25);//ins_tcpt_id
            HSSFCell cell_TSI100 = row.getCell(13);//tsi100
            HSSFCell cell_TSI1 = row.getCell(26);//tsi

            BigDecimal tsi100;
            if (cell_TSI100 != null) {
                tsi100 = new BigDecimal(cell_TSI100.getNumericCellValue());
            } else {
                tsi100 = BDUtil.zero;
            }

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured3(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsi100, null);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured3(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsi100, null);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(27);//ins_cover_id
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(28);//ins_cvpt_id
            HSSFCell cell_Rate1 = row.getCell(29);//rate
            HSSFCell cell_Premi1 = row.getCell(30);//premi

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    public void doAddLampiranSumInsured3(String instsipolid, BigDecimal tsi, BigDecimal tsi100, String tsidesc) throws Exception {

        //if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");

        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();

        ptsi.setStInsuranceTSIPolTypeID(instsipolid);

        if (tsidesc != null) {
            ptsi.setStTSICategoryDescription(tsidesc);
        }

        ptsi.initializeDefaults();

        ptsi.setDbInsuredAmount(tsi);

        if (tsi100 != null && !BDUtil.isZeroOrNull(tsi100)) {
            ptsi.setDbInsuredAmountFull(tsi100);
        }

        ptsi.markNew();

        selectedDefaultObject.getSuminsureds().add(ptsi);

    }

    public void editCreateUploadEndorsePolis(String policyID, DTOList object) throws Exception {
        superEdit(policyID);

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());

        if(startDate.isEqual(endDate))
                policy.setStAllowSamePeriodFlag("Y");

        uploadEndorseDetailView objectUpload = (uploadEndorseDetailView) object.get(0);

        boolean hitungDetilPremi = true;

        if(BDUtil.isZero(objectUpload.getDbFeeBase()) && BDUtil.isZero(objectUpload.getDbKomisi()) && BDUtil.isZero(objectUpload.getDbBrokerFee())
                && BDUtil.isZero(objectUpload.getDbBiayaPolis()) && BDUtil.isZero(objectUpload.getDbMaterai())){

            policy.getDetails().deleteAll();
            hitungDetilPremi = false;
        }

        if(hitungDetilPremi){
            final DTOList item = policy.getDetails();

            for (int j = 0; j < item.size(); j++) {
                InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

                items.setDbAmount(BDUtil.zero);

                if(items.isFeeBase() && objectUpload.getDbFeeBase()!=null){
                    items.setStFlagEntryByRate("N");
                    items.setDbAmount(objectUpload.getDbFeeBase());
                }

                if(items.isKomisi2() && objectUpload.getDbKomisi()!=null){
                    items.setStFlagEntryByRate("N");
                    items.setDbAmount(objectUpload.getDbKomisi());
                }

                if(items.isBrokerFee() && objectUpload.getDbBrokerFee()!=null){
                    items.setStFlagEntryByRate("N");
                    items.setDbAmount(objectUpload.getDbBrokerFee());
                }

                if(items.isPolicyCost() && objectUpload.getDbBiayaPolis()!=null){
                    items.setStFlagEntryByRate("N");
                    items.setDbAmount(objectUpload.getDbBiayaPolis());
                }

                if(items.isStampFee() && objectUpload.getDbMaterai()!=null){
                    items.setStFlagEntryByRate("N");
                    items.setDbAmount(objectUpload.getDbMaterai());
                }

            }
        }

        policy.setStEndorseNotes(objectUpload.getStEndorseNotes());

        /*
        if(objectUpload.getStApprovedWho()!=null){
            if(!objectUpload.getStApprovedWho().equalsIgnoreCase("")){
                policy.setStEffectiveFlag("Y");
                policy.setStPostedFlag("Y");
                policy.setStApprovedWho(objectUpload.getStApprovedWho());
                policy.setDtApprovedDate(new Date());
                policy.setDtPolicyDate(new Date());
                policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
                policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());

            }
        }*/

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
            
            for (int j = 0; j < object.size(); j++) {
                uploadEndorseDetailView objectUploadDet = (uploadEndorseDetailView) object.get(j);
                
                if(objx.getStOrderNo().trim().equalsIgnoreCase(objectUploadDet.getStOrderNo().trim())
                   && objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStNama().trim().toUpperCase())){

                    if(objectUploadDet.getDtRestitutionDate()!=null)
                        objx.setDtReference5(objectUploadDet.getDtRestitutionDate());

                    if(objectUploadDet.getStSisaJangkaWaktu()!=null){
                        if(policy.getStPolicyTypeID().equalsIgnoreCase("59"))
                              objx.setStReference9(objectUploadDet.getStSisaJangkaWaktu());
                        else if(policy.getStPolicyTypeID().equalsIgnoreCase("21"))
                            objx.setStReference15(objectUploadDet.getStSisaJangkaWaktu());
                    }       

                    if(objectUploadDet.getStReferenceNo()!=null)
                        objx.setStReference12(objectUploadDet.getStReferenceNo().trim());

                    if(objectUploadDet.getDbRestitutionPct()!=null)
                        policy.setDbPeriodRateBefore(objectUploadDet.getDbRestitutionPct());

                    //jika endorse jangka waktu
                    if(objectUploadDet.isEndorseJangkaWaktu()){
                        if(objectUploadDet.getDtPeriodeAwal()!=null)
                            objx.setDtReference2(objectUploadDet.getDtPeriodeAwal());

                        if(objectUploadDet.getDtPeriodeAkhir()!=null)
                            objx.setDtReference3(objectUploadDet.getDtPeriodeAkhir());
                    }
                }      
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(cov.isEntryPremi())
                    cov.setStEntryPremiFlag(null);

                for (int k = 0; k < object.size(); k++) {
                    uploadEndorseDetailView objectUploadDet = (uploadEndorseDetailView) object.get(k);

                    if(objx.getStOrderNo().trim().equalsIgnoreCase(objectUploadDet.getStOrderNo().trim())
                       && objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStNama().trim().toUpperCase())){

                        if(objectUploadDet.getDbPremi()!=null){
                            cov.setDbPremi(objectUploadDet.getDbPremi());
                            cov.setDbPremiNew(objectUploadDet.getDbPremi());

                            if(!cov.isEntryRate())
                                cov.setStEntryPremiFlag("Y");
                        }

                        if(objectUploadDet.isEndorseJangkaWaktu()){
                            if(objectUploadDet.getDbPremi()!=null){

                                cov.setStEntryPremiFlag("Y");
                                cov.setStEntryRateFlag("N");
                                cov.setDbPremi(objectUploadDet.getDbPremi());
                                cov.setDbPremiNew(objectUploadDet.getDbPremi());
                            }
                        }
                    }
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                for (int k = 0; k < object.size(); k++) {
                    uploadEndorseDetailView objectUploadDet = (uploadEndorseDetailView) object.get(k);

                    if(objx.getStOrderNo().trim().equalsIgnoreCase(objectUploadDet.getStOrderNo().trim())
                       && objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStNama().trim().toUpperCase())){

                        if(objectUploadDet.getDbTSI()!=null){
                            tsi.setDbInsuredAmount(objectUploadDet.getDbTSI());

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("21"))
                                objx.setDbReference4(objectUploadDet.getDbTSI());
                        }
                            
                    }
                }

            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    public void approvalLite(String policyID) throws Exception {
        editLite(policyID);

        cekClosingStatus("APPROVE");

        super.setReadOnly(true);

        approvalMode = true;

    }

    public void editLite(String policyID) throws Exception {
        superEditLite(policyID);

        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

        if (policy.isEffective()) throw new Exception("Data tidak bisa diubah karena sudah disetujui");

    }

    public void superEditLite(String policyID) throws Exception {
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

        policy.showItemsAccount();

        initTabsLite();

    }

    private void initTabsLite() {

        tabs = new FormTab();

        tabs.add(new FormTab.TabBean("TAB_REJECT","{L-ENGREJECT NOTES-L}{L-INAKETERANGAN-L}",false));
        tabs.add(new FormTab.TabBean("TAB_ENDORSE","ENDORSEMENT",false));
//        tabs.add(new FormTab.TabBean("TAB_CLAIM","{L-ENGCLAIM-L}{L-INAKLAIM-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_CLAIM_ITEM","{L-ENGCLAIM ITEMS-L}{L-INAITEM KLAIM-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_CLAIM_RE","{L-ENGCLAIM R/I-L}{L-INAR/I KLAIM-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_RISK_DET","{L-ENGRISK DETAIL-L}{L-INADETIL RESIKO-L}",true));
//        tabs.add(new FormTab.TabBean("TAB_CLAUSES","{L-ENGCLAUSES-L}{L-INAKLAUSULA-L}",false));

        tabs.add(new FormTab.TabBean("TAB_PREMI","PREMI",true));
//        tabs.add(new FormTab.TabBean("TAB_INST","{L-ENGPAYMENT-L}{L-INAPEMBAYARAN-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_COINS","{L-ENGCOINSURANCE-L}{L-INAKOASURANSI-L}",true));
//        tabs.add(new FormTab.TabBean("TAB_COINS_COVER","{L-ENGCO-COVER-L}{L-INACO-COVER-L}",true));
//
//        tabs.add(new FormTab.TabBean("TAB_CLAIMCO","{L-ENGCLAIM CO-L}{L-INAKLAIM CO-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_CLAIMCOINS_COVER","{L-ENGCLAIM CO-COVER-L}{L-INAKLAIM CO-COVER-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS","{L-ENGDOCUMENTS-L}{L-INALAMPIRAN-L}",false));
//        tabs.add(new FormTab.TabBean("TAB_OPTIONS","{L-ENGOPTIONS-L}{L-INAPILIHAN-L}",false));

        tabs.setActiveTab("TAB_PREMI");

        boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStStatus());
        boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStStatus());
        boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStStatus());
        boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStStatus());
        boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStStatus());
        boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStStatus());
        boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStStatus());
        boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStStatus());
        boolean statusEndorseRI = FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(getStStatus());
        boolean statusInward = FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(getStStatus());

        boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
        boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
        boolean rejectModex = isRejectMode();

        if (statusEndorse||statusClaimEndorse||statusEndorseRI) {
            tabs.enable("TAB_ENDORSE",true);
            tabs.setActiveTab("TAB_ENDORSE");
        }

        if(rejectModex){
            tabs.enable("TAB_REJECT",true);
            tabs.setActiveTab("TAB_REJECT");
        }

        if (statusDraft) setTitle("{L-ENGPROPOSAL-L}{L-INAPROPOSAL-L}");
        else if (statusPolicy) setTitle("{L-ENGPOLICY-L}{L-INAPOLIS-L} (Lite Version)");
        else if (statusEndorse) setTitle("ENDORSEMENT");
        else if (statusClaim) setTitle("{L-ENGCLAIM-L}{L-INAKLAIM-L}");
        else if (statusCancel) setTitle("CANCEL");
        else if (statusSPPA) setTitle("SPPA");
        else if (statusRenewal) setTitle("{L-ENGRENEWAL-L}{L-INAPERPANJANGAN-L}");



    }

     private void addKomisiInwardAutomatic(BigDecimal komisi) throws Exception{

        final String coverType = policy.getStCoverTypeCode();

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        String itemID = null;

        if(coverType.equalsIgnoreCase("DIRECT")){
            itemID = "11";
        }

        if(coverType.equalsIgnoreCase("COINSOUT")){
            itemID = "18";
        }

        if(coverType.equalsIgnoreCase("COINSIN")){
            itemID = "25";
        }

        item.setStInsItemID(itemID);

        item.setStInsuranceItemCategory(getInsItemCat(itemID).getStItemCategory());
        item.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
        item.setStTaxCode("1");
        item.setDbAmount(komisi);

        getDetails().add(item);

    }

     public void editCreateEndorseKomisi(InsurancePolicyView polis) throws Exception {
        superEdit(polis.getStPolicyID());

        policy.generateEndorseNo();
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());
        if(startDate.isEqual(endDate))
                policy.setStAllowSamePeriodFlag("Y");

        policy.setStEndorseNotes("ENDORSE PEMBENTUKAN PPN FEEBASE");
        policy.setStAdminNotes("ENDORSE_PPN");

        policy.setStEffectiveFlag("N");
        policy.setStPostedFlag("N");
        //policy.setStApprovedWho("05660717");
        //policy.setDtApprovedDate(new Date());
        //policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
        //policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());
        policy.setDtPolicyDate(new Date());
        //policy.setDtApprovedDate(new Date());



        final DTOList item = policy.getDetails();

        for (int j = 0; j < item.size(); j++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

            items.setStFlagEntryByRate("N");
            items.setDbAmount(BDUtil.negate(items.getDbAmount()));

            /*
            items.setDbAmount(BDUtil.zero);

            if(items.isKomisi2()){
                items.setStFlagEntryByRate("N");
                items.setDbAmount(pol2.getDbKomisiKoas());
            }*/

        }

        //nambah feebase
        final InsuranceItemsView itemCat = getInsItemCat("66");

        final InsurancePolicyItemsView itemNew = new InsurancePolicyItemsView();

        itemNew.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        itemNew.markNew();

        itemNew.setStInsItemID("66");

        itemNew.setStEntityID(policy.getStEntityID());

        itemNew.setStInsuranceItemCategory(itemCat.getStItemCategory());

        if(itemCat.getDbDefaultValue()!=null){
            itemNew.setStFlagEntryByRate("Y");
            itemNew.setDbRate(itemCat.getDbDefaultValue());
        }


        policy.getDetails().add(itemNew);

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

                cov.setDbPremi(BigDecimal.ZERO);
                cov.setDbPremiNew(BigDecimal.ZERO);
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

     private void uploadCPME(FileInputStream fis, String treatyID)throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        if(policy.getDtPeriodStart()==null) throw new RuntimeException("Periode Awal Harus Diisi");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if(sheet==null)
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");

        int rows  = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1  = row.getCell(1);//yang dipertanggungkan
            HSSFCell cell2  = row.getCell(2);//penggunaan
            HSSFCell cell3  = row.getCell(3);//alamat resiko
            HSSFCell cell4  = row.getCell(4);//kode pos(String)
            HSSFCell cell5  = row.getCell(5);//kode resiko

            String kodePos = policy.getStKodePos(cell4.getCellType()==cell4.CELL_TYPE_STRING?cell4.getStringCellValue():new BigDecimal(cell4.getNumericCellValue()).toString());
            if(kodePos!=null) getSelectedDefaultObject().setStReference4(kodePos);

            getSelectedDefaultObject().setStReference1(cell1.getCellType()==cell1.CELL_TYPE_STRING?cell1.getStringCellValue():new BigDecimal(cell1.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference2(cell2.getCellType()==cell2.CELL_TYPE_STRING?cell2.getStringCellValue():new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType()==cell3.CELL_TYPE_STRING?cell3.getStringCellValue():new BigDecimal(cell3.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStRiskCategoryID(cell5.getCellType()==cell5.CELL_TYPE_STRING?cell5.getStringCellValue():new BigDecimal(cell5.getNumericCellValue()).toString());

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1  = row.getCell(6);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1  = row.getCell(7);//ins_tsi_cat_id
            HSSFCell cell_TSI1  = row.getCell(8);//tsi

            if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID1.getCellType()==cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()));
            }

            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2  = row.getCell(9);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2  = row.getCell(10);//ins_tsi_cat_id
            HSSFCell cell_TSI2  = row.getCell(11);//tsi

            if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID2.getCellType()==cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()));
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3  = row.getCell(12);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3  = row.getCell(13);//ins_tsi_cat_id
            HSSFCell cell_TSI3  = row.getCell(14);//tsi

            if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranSumInsured(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }else if(cell_Ins_Tcpt_ID3.getCellType()==cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranSumInsured(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()));
            }

            //add coverkreasi1
            HSSFCell cell_Ins_Cvpt_ID1  = row.getCell(15);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1  = row.getCell(16);//RATE
            HSSFCell cell_Rate1  = row.getCell(17);//premi
            HSSFCell cell_Premi1  = row.getCell(18);//tsi

            final BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue()))?null:new BigDecimal(cell_Rate1.getNumericCellValue());
            final BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue()))?null:new BigDecimal(cell_Premi1.getNumericCellValue());

            if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
            }else if(cell_Ins_Cvpt_ID1.getCellType()==cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //add coverkreasi2
            HSSFCell cell_Ins_Cvpt_ID2  = row.getCell(19);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2  = row.getCell(20);//RATE
            HSSFCell cell_Rate2  = row.getCell(21);//premi
            HSSFCell cell_Premi2  = row.getCell(22);//tsi

            final BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue()))?null:new BigDecimal(cell_Rate2.getNumericCellValue());
            final BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue()))?null:new BigDecimal(cell_Premi2.getNumericCellValue());

            if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
            }else if(cell_Ins_Cvpt_ID2.getCellType()==cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //add coverkreasi3
            HSSFCell cell_Ins_Cvpt_ID3  = row.getCell(23);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3  = row.getCell(24);//RATE
            HSSFCell cell_Rate3  = row.getCell(25);//premi
            HSSFCell cell_Premi3  = row.getCell(26);//tsi

            final BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue()))?null:new BigDecimal(cell_Rate3.getNumericCellValue());
            final BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue()))?null:new BigDecimal(cell_Premi3.getNumericCellValue());

            if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_STRING){
                if(!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL"))
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
            }else if(cell_Ins_Cvpt_ID3.getCellType()==cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC){
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

        }
    }


     public void cekTSI() throws Exception {



        if (policy.isStatusPolicy() || policy.isStatusRenewal()) {


            BigDecimal transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(), SessionManager.getInstance().getUserID());

            //jika polis member, maka kalikan limit x share askrida
            if(policy.isMember()){
                //transactionLimit = BDUtil.mul(transactionLimit, BDUtil.getRateFromPct(policy.getDbSharePct()), 2);
            }

            if(isApprovalByDirectorMode()){
                transactionLimit = getTransactionLimit("ACCEPT", policy.getStRiskClass(),"dirtek");
            }

            //CEK LIMIT TSI PER OBJEK(RESIKO), BUKAN PER POLIS(TOTAL)
            policy.validateTSILimit(transactionLimit);

            //CEK LIMIT AKUMULASI PER PRINCIPAL
            //final BigDecimal transactionLimitPerPrincipal = getTransactionLimit("ACCEPT_PRINCIPAL", policy.getStRiskClass(),SessionManager.getInstance().getUserID());
            policy.validateAkumulasiResikoLimit(transactionLimit);

            //CEK LIMIT KOMISI PER ITEM
            policy.checkDetailsLimit();

            policy.validateObjectsApprove();

            BigDecimal comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), policy.getDbPremiNetto()), policy.getDbPremiTotal(),4);

            BigDecimal premiNett = null;
            BigDecimal totalKomisi = BDUtil.zero;
            BigDecimal totalDiskon = BDUtil.zero;
            BigDecimal totalBrokerfee = BDUtil.zero;
            totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm1()),policy.getDbNDComm2());
            totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDComm3()),policy.getDbNDComm4());
            totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, policy.getDbNDFeeBase1()),policy.getDbNDFeeBase2());
            totalBrokerfee = BDUtil.add(BDUtil.add(totalBrokerfee, policy.getDbNDBrok1()), policy.getDbNDBrok2());
            premiNett = BDUtil.sub(policy.getDbPremiTotal(), totalKomisi);
            premiNett = BDUtil.sub(premiNett, totalBrokerfee);
            totalDiskon = BDUtil.add(policy.getDbNDDisc1(), policy.getDbNDDisc2());
            premiNett = BDUtil.sub(premiNett, totalDiskon);

            comissionRatio = BDUtil.div(BDUtil.sub(policy.getDbPremiTotal(), premiNett), policy.getDbPremiTotal(),4);

            BigDecimal comissionLimit = BDUtil.getRateFromPct(getComissionLimitNew());

            boolean overLimit = false;
            BigDecimal comissionCompanyLimit = BDUtil.getRateFromPct(getComissionCompanyLimit());
            comissionCompanyLimit = null;

            String errorMessage="";
            if(!BDUtil.isZeroOrNull(comissionCompanyLimit)){
                overLimit = Tools.compare(comissionRatio, comissionCompanyLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon "+ policy.getEntity().getStEntityName() +" Terlalu Tinggi";
            }else{
                overLimit = Tools.compare(comissionRatio, comissionLimit)>0;
                if(overLimit) errorMessage = "Jumlah Komisi/Diskon ("+ BDUtil.getPctFromRate(comissionRatio) +") Melebihi Kewenangan Anda ("+ BDUtil.getPctFromRate(comissionLimit) +")";
            }

            if (overLimit){
                policy.setStUnderwritingFinishFlag("N");
                policy.setStEffectiveFlag("N");
//                getRemoteInsurance().saveInputPaymentDate(policy, approvalMode);
                throw new RuntimeException(errorMessage);
            }
        }


    }

     private void uploadEEI(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Upload");

        if (policy.getDtPeriodStart() == null) {
            throw new RuntimeException("Periode Awal Harus Diisi");
        }

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Upload Tidak Ditemukan !");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            //HSSFCell cellControl  = row.getCell(0);
            //if(cellControl==null) break;

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//transit ke (String)
            HSSFCell cell2 = row.getCell(2);//tgl transit (date)
            HSSFCell cell3 = row.getCell(3);//waktu transit (String)
            HSSFCell cell4 = row.getCell(4);//no deklarasi (String)

            String kodePos = policy.getStKodePos(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            if (kodePos != null) {
                getSelectedDefaultObject().setStReference4(kodePos);
            }

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Tertanggung Dari Harus String/Character");
            }

            if (cell2.getCellType() != cell2.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Penggunaan Ke Harus String/Character");
            }

            if (cell3.getCellType() != cell3.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Alamat Transit Harus String/Character");
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            getSelectedDefaultObject().setStReference2(cell2.getStringCellValue());
            getSelectedDefaultObject().setStReference3(cell3.getStringCellValue());

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(30);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(31);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC1 = row.getCell(9);//tsi desc
            HSSFCell cell_TSI1 = row.getCell(10);//tsi

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), cell_TSI_DESC1.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), cell_TSI_DESC1.getStringCellValue());
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(32);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(33);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC2 = row.getCell(12);//tsi desc
            HSSFCell cell_TSI2 = row.getCell(13);//tsi

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()), cell_TSI_DESC2.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()), cell_TSI_DESC2.getStringCellValue());
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(34);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(35);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC3 = row.getCell(15);//tsi desc
            HSSFCell cell_TSI3 = row.getCell(16);//tsi

            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()), cell_TSI_DESC3.getStringCellValue());
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()), cell_TSI_DESC3.getStringCellValue());
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(36);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(37);//RATE
            HSSFCell cell_Rate1 = row.getCell(38);//premi
            HSSFCell cell_Premi1 = row.getCell(39);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(40);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(41);//RATE
            HSSFCell cell_Rate2 = row.getCell(42);//premi
            HSSFCell cell_Premi2 = row.getCell(43);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (rate2 != null) {
                rate2 = rate2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(44);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(45);//RATE
            HSSFCell cell_Rate3 = row.getCell(46);//premi
            HSSFCell cell_Premi3 = row.getCell(47);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (premi3 != null) {
                premi3 = premi3.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (rate3 != null) {
                rate3 = rate3.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //cover 4
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(48);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(49);//RATE
            HSSFCell cell_Rate4 = row.getCell(50);//premi
            HSSFCell cell_Premi4 = row.getCell(51);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());

            if (premi4 != null) {
                premi4 = premi4.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (rate4 != null) {
                rate4 = rate4.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, null);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

     private void uploadKreditKMK(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Konversi2");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual!");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cellNama = row.getCell(1);//nama
            HSSFCell cellTglLahir = row.getCell(2);//tgl lahir
            HSSFCell cellTglAwal = row.getCell(3);//tgl awal
            HSSFCell cellTglAkhir = row.getCell(4);//tgl akhir
            HSSFCell cellInsured = row.getCell(9);//tsi
            HSSFCell cellPremi = row.getCell(10);//premi
            HSSFCell cellRate = row.getCell(7);//rate
            HSSFCell cellUsia = row.getCell(5);//usia
            HSSFCell cellKtp = row.getCell(12);//ktp
            HSSFCell cellPK = row.getCell(13);//pk
            HSSFCell cellTglPK = row.getCell(14);//tgl pk
            HSSFCell cellAlamat = row.getCell(15);//alamat
            HSSFCell cellLama = row.getCell(8);//lama
            HSSFCell cellKategori = row.getCell(35);//kategori debitur
            HSSFCell cellCoverage = row.getCell(36);//kategori debitur
            HSSFCell cellNoRP = row.getCell(16);//ktp
            HSSFCell cellJenisUsaha = row.getCell(17);//pk
            HSSFCell cellPengPlafon = row.getCell(18);//tgl pk
            HSSFCell cellTaksasi = row.getCell(19);//alamat
            HSSFCell cellAgunan = row.getCell(20);//ktp
            HSSFCell cellPengikatan = row.getCell(21);//pk
            HSSFCell cellNoSKP = row.getCell(22);//tgl pk
            HSSFCell cellTglSKP = row.getCell(23);//alamat
            HSSFCell cellNoInvoice = row.getCell(24);//tgl pk
            HSSFCell cellTenorHari = row.getCell(25);//alamat

            HSSFCell cellPeriodBaseID = row.getCell(37);//period base of period
            HSSFCell cellPremiumFactor = row.getCell(38);//premium factor rate factor

            if (cellNama.getCellType() != cellNama.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cellInsured.getCellType() != cellInsured.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cellPremi.getCellType() != cellPremi.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            if (cellTaksasi.getCellType() != cellTaksasi.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Taksasi Harus Numeric");
            }

            double a = cellInsured.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured("574", b);

            double tes3 = cellPremi.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            BigDecimal d = null;
            if (cellRate != null) {
                double tes4 = cellRate.getNumericCellValue();
                d = new BigDecimal(tes4);
            }

            getSelectedDefaultObject().setStReference1(cellNama.getStringCellValue()); //nama

            if (cellTglLahir != null) {
                getSelectedDefaultObject().setDtReference1(cellTglLahir.getDateCellValue()); //tgl lahir
            }

            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //usia
                }
            }

            if (cellKategori != null) {
                if (cellKategori.getCellType() == cellKategori.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference8(cellKategori.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference8(ConvertUtil.removeTrailing(String.valueOf(cellKategori.getNumericCellValue()))); //usia
                }
            }

            if (cellUsia != null) {
                if (cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference2(cellUsia.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cellUsia.getNumericCellValue()))); //usia
                }
            }

            if (cellLama != null) {
                if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
                } else {
                    getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
                }
            }

            if (cellTglPK != null) {
                if (cellTglPK.getCellType() == cellTglPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference4(DateUtil.getDate(cellTglPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue());
                }
            }

            if (cellTglAwal.getCellType() == cellTglAwal.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellTglAwal.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference2(cellTglAwal.getDateCellValue());
            }

            if (cellTglAkhir.getCellType() == cellTglAkhir.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cellTglAkhir.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference3(cellTglAkhir.getDateCellValue());
            }

            if (cellCoverage != null) {
                if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference13(cellCoverage.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference13(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
                }
            }

            if (cellNoRP != null) {
                if (cellNoRP.getCellType() == cellNoRP.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference16(cellNoRP.getStringCellValue()); //usia
                }
            }

            if (cellJenisUsaha != null) {
                if (cellJenisUsaha.getCellType() == cellJenisUsaha.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference7(cellJenisUsaha.getStringCellValue()); //usia
                }
            }

            if (cellPengPlafon != null) {
                if (cellPengPlafon.getCellType() == cellPengPlafon.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference23(cellPengPlafon.getStringCellValue()); //usia
                }
            }

            if (cellTaksasi != null) {
                if (cellTaksasi.getCellType() == cellTaksasi.CELL_TYPE_NUMERIC) {
                    getSelectedDefaultObject().setDbReference7(new BigDecimal(cellTaksasi.getNumericCellValue())); //usia
                }
            }

            if (cellAgunan != null) {
                if (cellAgunan.getCellType() == cellAgunan.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference20(cellAgunan.getStringCellValue()); //usia
                }
            }

            if (cellPengikatan != null) {
                if (cellPengikatan.getCellType() == cellPengikatan.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference21(cellPengikatan.getStringCellValue()); //usia
                }
            }

            if (cellNoSKP != null) {
                if (cellNoSKP.getCellType() == cellNoSKP.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference22(cellNoSKP.getStringCellValue()); //usia
                }
            }

            if (cellTglSKP != null) {
                if (cellTglSKP.getCellType() == cellTglSKP.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference7(DateUtil.getDate(cellTglSKP.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference7(cellTglSKP.getDateCellValue());
                }
            }

            if (cellNoInvoice != null) {
                if (cellNoInvoice.getCellType() == cellNoInvoice.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference25(cellNoInvoice.getStringCellValue()); //usia
                }
            }

            if (cellTenorHari != null) {
                if (cellTenorHari.getCellType() == cellTenorHari.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference26(cellTenorHari.getStringCellValue()); //lama
                } else {
                    getSelectedDefaultObject().setStReference26(ConvertUtil.removeTrailing(String.valueOf(cellTenorHari.getNumericCellValue()))); //lama
                }

                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                getSelectedDefaultObject().setDbPeriodRate(cellTenorHari.getCellType() == cellTenorHari.CELL_TYPE_STRING ? new BigDecimal(cellTenorHari.getStringCellValue()) : new BigDecimal(cellTenorHari.getNumericCellValue()));

            }

            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(31);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(33);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cellRate != null) {
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            HSSFCell cellPremiPA = row.getCell(34);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cellRate == null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasi(cvptID3, ratePA2, premiPA2);

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }
    }

     public void getLowestRateOJK() throws Exception{
         getSelectedObject().getLowestRateOJK();
     }

     public void getHighestRateOJK() throws Exception{
         getSelectedObject().getHighestRateOJK();
     }

     private void uploadKreditProduktif(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Konversi2");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual!");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cellNama = row.getCell(1);//nama
            HSSFCell cellTglLahir = row.getCell(2);//tgl lahir
            HSSFCell cellTglAwal = row.getCell(3);//tgl awal
            HSSFCell cellTglAkhir = row.getCell(4);//tgl akhir
            HSSFCell cellUsia = row.getCell(5);//usia
            HSSFCell cellBulan = row.getCell(6);//usia
            HSSFCell cellRate = row.getCell(7);//rate
            HSSFCell cellLama = row.getCell(8);//lama
            HSSFCell cellTsi = row.getCell(9);//tsi
            HSSFCell cellPremi = row.getCell(10);//premi
            HSSFCell cellKtp = row.getCell(11);//ktp
            HSSFCell cellPK = row.getCell(12);//pk
            HSSFCell cellTglPK = row.getCell(13);//tgl pk
            HSSFCell cellAlamat = row.getCell(14);//alamat
            HSSFCell cellUsaha = row.getCell(19);//alamat
            HSSFCell cellPenggunaan = row.getCell(20);//alamat
            HSSFCell cellBentukAgunan = row.getCell(21);//alamat
            HSSFCell cellCaraIkat = row.getCell(22);//alamat
            HSSFCell cellNilaiTaksasi = row.getCell(23);//alamat
            HSSFCell cellNoSKP = row.getCell(24);//alamat
            HSSFCell cellTglSKP = row.getCell(25);//alamat
            HSSFCell cellPekerjaan = row.getCell(32);//alamat
            HSSFCell cellCoverage = row.getCell(33);//alamat
            HSSFCell cellJenisKredit = row.getCell(34);//alamat
            HSSFCell cellSumberBayar = row.getCell(35);//alamat


            if (cellNama.getCellType() != cellNama.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cellTsi.getCellType() != cellTsi.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cellPremi.getCellType() != cellPremi.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cellTsi.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured("577", b);

            double tes3 = cellPremi.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

//            BigDecimal d = null;
//            if (cell7 != null) {
//                double tes4 = cell7.getNumericCellValue();
//                d = new BigDecimal(tes4);
//            }

            getSelectedDefaultObject().setStReference1(cellNama.getStringCellValue()); //nama
            if (cellTglLahir != null) {
                getSelectedDefaultObject().setDtReference1(cellTglLahir.getDateCellValue()); //tgl lahir
            }

            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }            

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference7(cellPK.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellPK.getNumericCellValue()))); //usia
                }
            }

            if (cellTglPK!= null) {
                getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue()); //tgl lahir
            }

            if (cellTglAwal!= null) {
                getSelectedDefaultObject().setDtReference2(cellTglAwal.getDateCellValue()); //tgl lahir
            }

            if (cellTglAkhir!= null) {
                getSelectedDefaultObject().setDtReference3(cellTglAkhir.getDateCellValue()); //tgl lahir
            }

            if (cellUsia != null) {
                if (cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference2(cellUsia.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cellUsia.getNumericCellValue()))); //usia
                }
            }

            if (cellPekerjaan != null) {
                if (cellPekerjaan.getCellType() == cellPekerjaan.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPekerjaan.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference4(ConvertUtil.removeTrailing(String.valueOf(cellPekerjaan.getNumericCellValue()))); //usia
                }
            }

            if (cellCoverage != null) {
                if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference10(cellCoverage.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference10(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
                }
            }

            if (cellJenisKredit != null) {
                if (cellJenisKredit.getCellType() == cellJenisKredit.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference9(cellJenisKredit.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference9(ConvertUtil.removeTrailing(String.valueOf(cellJenisKredit.getNumericCellValue()))); //usia
                }
            }

            if (cellSumberBayar != null) {
                if (cellSumberBayar.getCellType() == cellSumberBayar.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference11(cellSumberBayar.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference11(ConvertUtil.removeTrailing(String.valueOf(cellSumberBayar.getNumericCellValue()))); //usia
                }
            }

            if (cellUsaha != null) {
                getSelectedDefaultObject().setStReference12(cellUsaha.getStringCellValue()); //alamat
            }

            if (cellPenggunaan != null) {
                getSelectedDefaultObject().setStReference13(cellPenggunaan.getStringCellValue()); //alamat
            }

            if (cellBentukAgunan != null) {
                getSelectedDefaultObject().setStReference14(cellBentukAgunan.getStringCellValue()); //alamat
            }

            if (cellCaraIkat != null) {
                getSelectedDefaultObject().setStReference21(cellCaraIkat.getStringCellValue()); //alamat
            }

            if (cellNilaiTaksasi != null) {
                getSelectedDefaultObject().setDbReference7(new BigDecimal(cellNilaiTaksasi.getNumericCellValue())); //alamat
            }

            if (cellNoSKP != null) {
                getSelectedDefaultObject().setStReference16(cellNoSKP.getStringCellValue()); //alamat
            }

            if (cellTglSKP!= null) {
                getSelectedDefaultObject().setDtReference6(cellTglSKP.getDateCellValue()); //tgl lahir
            }


            /*
            if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
            } else {
                getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
            }*/


            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add cover kredit
            HSSFCell cellCoverPA = row.getCell(28);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(30);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cellRate != null) {
                double ratePA = cellRate.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            if (cellPremi.getCellType() != cellPremi.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (ratePA2 == null) {
                double premiPA = cellPremi.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasi(cvptID3, ratePA2, premiPA2);

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

     public void editInward(String policyID) throws Exception {
        superEdit(policyID);
        if (!policy.isActive()) throw new RuntimeException("This document is not editable because it is already a historical entry");

        //if (policy.isEffective()) throw new Exception("Data tidak bisa diubah karena sudah disetujui");

    }

     public BigDecimal getComissionLimitNew(String userID) throws Exception {
        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 in (select role_id from s_user_roles where user_id=?) and b.fft_group_id='COMM'" +
                "   and (b.ref5 = ? or b.ref5 is null) "+ //--cari by pemasar
                "   and (b.ref4 = ? or b.ref4 is null) "+ //--cari by grup sumbis
                "   and active_flag = 'Y'"+
                "   and period_start <= ? and period_end >= ?"+
                "   order by ref5,ref4,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), userID, policy.getStProducerID(), policy.getEntity().getStRef2(), policy.getDtPeriodStart(), policy.getDtPeriodStart()},
                FlexTableView.class
                ).getDTO();

        return ft==null?null:ft.getDbReference1();
    }

     private String objSubroIndex;
    private String subrogasiAddID;
    private DTOList subrogasiLOV;

    /**
     * @return the objSubroIndex
     */
    public String getObjSubroIndex() {
        return objSubroIndex;
    }

    /**
     * @param objSubroIndex the objSubroIndex to set
     */
    public void setObjSubroIndex(String objSubroIndex) {
        this.objSubroIndex = objSubroIndex;
    }

    public void onNewObjSubrogasi() {
        final InsurancePolicySubrogasiView ded = new InsurancePolicySubrogasiView();

        ded.setStInsuranceClaimCauseID(subrogasiAddID);

        ded.markNew();

        selectedObject.getSubrogasi().add(ded);

    }

    public void onDeleteObjSubrogasi() {
        selectedObject.getSubrogasi().delete(Integer.parseInt(objSubroIndex));
    }

    public DTOList getSubrogasi() {
        return policy.getSubrogasi();
    }

    public void applySubrogasiToAllObjects() throws Exception {
        final DTOList subro = selectedObject.getSubrogasi();

        final DTOList object = policy.getObjects();
        int subSize = subro.size();

        for (int i = 0; i < object.size(); i++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) object.get(i);

            if (obj == selectedObject) {
                continue;
            }

            for (int j = 0; j < subSize; j++) {
                InsurancePolicySubrogasiView sub = (InsurancePolicySubrogasiView) subro.get(j);

                InsurancePolicySubrogasiView newSub = new InsurancePolicySubrogasiView();
                newSub = sub;
                newSub.markNew();
                obj.getSubrogasi().add(newSub);
            }
        }
    }

    /**
     * @return the subrogasiAddID
     */
    public String getSubrogasiAddID() {
        return subrogasiAddID;
    }

    /**
     * @param subrogasiAddID the subrogasiAddID to set
     */
    public void setSubrogasiAddID(String subrogasiAddID) {
        this.subrogasiAddID = subrogasiAddID;
    }

    public DTOList getSubrogasiAddLOV() throws Exception {
        loadSubrogasiLOV();

        final DTOList lov = new DTOList();

        for (int i = 0; i < subrogasiLOV.size(); i++) {
            InsuranceDocumentTypeView cls = (InsuranceDocumentTypeView) subrogasiLOV.get(i);

            //if(cls.getStActiveFlag()!=null) continue;

            /*if (cls.getStActiveFlag() != null) {
            if (Tools.isNo(cls.getStActiveFlag())) {
            continue;
            }
            }*/

            lov.add(cls);
        }

        return lov;
    }

    private void loadSubrogasiLOV() throws Exception {
        if (subrogasiLOV == null) {
            subrogasiLOV = ListUtil.getDTOListFromQuery(
                    /*"   select"
                    + "      *"
                    + "   from"
                    + "      ins_clm_cause "
                    + "   where "
                    + "      pol_type_id = ?",*/
                    "select b.*,a.ins_documents_id "
                    + "from ins_documents a "
                    + "inner join ins_document_type b on b.ins_document_type_id = a.ins_document_type_id "
                    + "where a.pol_type_id = ? and a.document_class = ? ",
                    new Object[]{policy.getStPolicyTypeID(), "SUBRO"},
                    InsuranceDocumentTypeView.class);
        }
    }

    /**
     * @return the docIndex
     */
    public String getDocIndex() {
        return docIndex;
    }

    /**
     * @param docIndex the docIndex to set
     */
    public void setDocIndex(String docIndex) {
        this.docIndex = docIndex;
    }
    
    private void uploadKreditKonsumtif(FileInputStream fis, String treatyID) throws Exception{
        POIFSFileSystem fsCek = new POIFSFileSystem(fis);
        HSSFWorkbook wbCek = new HSSFWorkbook(fsCek);
        HSSFSheet sheet = wbCek.getSheet("KonversiNew");
        HSSFSheet sheet2 = wbCek.getSheet("Konversi2020");

        if (sheet != null){
            uploadKreditNew(fsCek, treatyID);
        }
        
        if (sheet2 != null){
            uploadKredit(fsCek, treatyID);
        }
        
    }

    private void uploadKreditNew(POIFSFileSystem fis, String treatyID) throws Exception {
        //POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheet("KonversiNew");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual!");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//nama
            HSSFCell cell2 = row.getCell(2);//tgl lahir
            HSSFCell cell3 = row.getCell(3);//tgl awal
            HSSFCell cell4 = row.getCell(4);//tgl akhir
            HSSFCell cell5 = row.getCell(9);//tsi
            HSSFCell cell6 = row.getCell(10);//premi
            HSSFCell cell7 = row.getCell(7);//rate
            HSSFCell cell8 = row.getCell(5);//usia
            HSSFCell cellKtp = row.getCell(13);//ktp
            HSSFCell cellPK = row.getCell(14);//pk
            HSSFCell cellTglPK = row.getCell(15);//tgl pk
            HSSFCell cellAlamat = row.getCell(16);//alamat
            HSSFCell cellLama = row.getCell(8);//lama
            HSSFCell cellStatus = row.getCell(32);//kategori
            HSSFCell cellKategori = row.getCell(31);//kategori debitur
            HSSFCell cellCoverage = row.getCell(33);//kategori debitur
            HSSFCell cellPekerjaan = row.getCell(34);//kategori debitur
            HSSFCell cellJenisTransaksi = row.getCell(35);//kategori debitur

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cell5.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured("488", b);

            double tes3 = cell6.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            BigDecimal d = null;
            if (cell7 != null) {
                double tes4 = cell7.getNumericCellValue();
                d = new BigDecimal(tes4);
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if (cell2 != null) {
                getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            }
            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }            //if(cellKtp!=null) getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //ktp
            //if(cellPK!=null) getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //pk
            //getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //kategori

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference4(ConvertUtil.removeTrailing(String.valueOf(cellPK.getNumericCellValue()))); //usia
                }
            }


            if (cellKategori.getCellType() == cellKategori.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference8(cellKategori.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference8(ConvertUtil.removeTrailing(String.valueOf(cellKategori.getNumericCellValue()))); //usia
            }
            if (cell8.getCellType() == cell8.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference2(cell8.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell8.getNumericCellValue()))); //usia
            }
            if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
            } else {
                getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
            }
            if (cellStatus.getCellType() == cellStatus.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //PNS/NON
            }
            if (cellTglPK != null) {
                if (cellTglPK.getCellType() == cellTglPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference4(DateUtil.getDate(cellTglPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue());
                }
            }

            if (cell3.getCellType() == cell3.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
            }

            if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
            } else {
                getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
            }

            if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference13(cellCoverage.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference13(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
            }

            if (cellPekerjaan.getCellType() == cellPekerjaan.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference19(cellPekerjaan.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference19(ConvertUtil.removeTrailing(String.valueOf(cellPekerjaan.getNumericCellValue()))); //usia
            }

            if (cellJenisTransaksi.getCellType() == cellJenisTransaksi.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference22(cellJenisTransaksi.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference22(ConvertUtil.removeTrailing(String.valueOf(cellJenisTransaksi.getNumericCellValue()))); //usia
            }

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(24);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(26);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cell7 != null) {
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            HSSFCell cellPremiPA = row.getCell(27);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cell7 == null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasi(cvptID3, ratePA2, premiPA2);

            //add coverkreasi
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(29);//ins_cover_id
            HSSFCell cell_Premi2 = row.getCell(30);//tsi

            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverKreasi(cell_Ins_Cvpt_ID2.getStringCellValue(), null, premi2);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverKreasi(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), null, premi2);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    public void approvalByDivisi(String policyID) throws Exception {
        editApprove(policyID);

        super.setReadOnly(true);

        approvalMode = true;

        approvalByDivisiMode = true;

        tabs.setActiveTab("TAB_POLICY_DOCUMENTS");

    }

    public void addReinsInst() {

        final InsurancePolicyReinsInstallmentView inst = new InsurancePolicyReinsInstallmentView();

        inst.markNew();

        getSelectedObjTreatyShare().getInstallment().add(inst);

    }

    public void deleteReinsInst() {
        getSelectedObjTreatyShare().getInstallment().delete(idxTreatySharesInst.intValue());
    }

    public void showCicilan(){
        showRIInstallment = true;
    }

    public void readTreatyFromPolisInduk(){

        if(policy.isPolis()) return;

        InsurancePolicyObjDefaultView object = (InsurancePolicyObjDefaultView) getClaimObject();

        if(object.getStOrderNo()!=null){

            //baca spreading reas polis induk
            final DTOList treatyInduk = policy.getTreatyInduk(object.getStOrderNo());

            if(treatyInduk!=null){
                
                //hapus rincian ri klaim yg sudah terbentuk
                getClaimObject().getTreaties().deleteAll();

                treatyInduk.convertAllToNew();

                InsurancePolicyTreatyView treatyPolis = (InsurancePolicyTreatyView) treatyInduk.get(0);

                final DTOList treatyDetails = treatyPolis.getDetails();

                treatyDetails.convertAllToNew();

                for (int i = 0; i < treatyDetails.size(); i++) {
                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(i);

                    tredet.setStEditFlag("Y");

                    final DTOList shares = tredet.getShares();

                    shares.convertAllToNew();

                }

                getClaimObject().setTreaties(treatyInduk);
            }

        }
        

    }

    public void onNewObjDeductible(String insuranceClaimCauseID, BigDecimal pct, String jenis, BigDecimal min, BigDecimal max) {
        final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

        ded.setStInsuranceClaimCauseID(insuranceClaimCauseID);

        if(!BDUtil.isZeroOrNull(pct)) ded.setDbPct(pct);

        if(jenis!=null) ded.setStDeductType(jenis);

        if(!BDUtil.isZeroOrNull(min)) ded.setDbAmountMin(min);

        if(!BDUtil.isZeroOrNull(max)) ded.setDbAmountMax(max);


        ded.markNew();

        selectedDefaultObject.getDeductibles().add(ded);

    }

    private DTOList objectTop5;

    /**
     * @return the objectSearch
     */
    public DTOList getObjectTop5() {
        loadObjectobjectTop5();
        return objectTop5;
    }

    /**
     * @param objectSearch the objectSearch to set
     */
    public void setObjectTop5(DTOList objectTop5) {
        this.objectTop5 = objectTop5;
    }

    public void loadObjectobjectTop5() {
        //if (!isAutoLoadEnabled()) return;
        try {

                 objectTop5 = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_obj where pol_id = ? order by insured_amount desc limit 5",
                        new Object[]{policy.getStPolicyID()},
                        InsurancePolicyObjDefaultView.class
                        );


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showTop5(){
        setShowTop5Object(!isShowTop5Object());
    }

    public void editCreateEndorsePPN(InsurancePolicyView polis, BigDecimal ppn) throws Exception {
        superEdit(polis.getStPolicyID());

        policy.generateEndorseNo();
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());
        if(startDate.isEqual(endDate))
                policy.setStAllowSamePeriodFlag("Y");

        policy.setStEndorseNotes("ENDORSE PEMBENTUKAN PPN FEEBASE");
        policy.setStAdminNotes("ENDORSE_PPN");

        policy.setStEffectiveFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStApprovedWho("00000002");
        policy.setDtApprovedDate(DateUtil.getDate("30/09/2019"));
        //policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
        //policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());
        policy.setDtPolicyDate(DateUtil.getDate("30/09/2019"));
        policy.setDtEndorseDate(DateUtil.getDate("30/09/2019"));
        //policy.setDtApprovedDate(new Date());


        /*
        final DTOList item = policy.getDetails();

        for (int j = 0; j < item.size(); j++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(j);

            items.setStFlagEntryByRate("N");
            items.setDbAmount(BDUtil.negate(items.getDbAmount()));


        }*/

        //nambah ppn feebase
        final InsuranceItemsView itemCat = getInsItemCat("91");

        final InsurancePolicyItemsView itemNew = new InsurancePolicyItemsView();

        itemNew.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        itemNew.markNew();

        itemNew.setStInsItemID("91");

        itemNew.setStEntityID(policy.getStEntityID());

        itemNew.setStInsuranceItemCategory(itemCat.getStItemCategory());

        itemNew.setDbAmount(ppn);
        /*
        if(itemCat.getDbDefaultValue()!=null){
            itemNew.setStFlagEntryByRate("Y");
            itemNew.setDbRate(itemCat.getDbDefaultValue());
        }*/


        policy.getDetails().add(itemNew);

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

                cov.setDbPremi(BigDecimal.ZERO);
                cov.setDbPremiNew(BigDecimal.ZERO);
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    private void validateDoubleInvoice(){
        final DTOList invoices = policy.getArinvoices();

        if(invoices.size()>0)
            throw new RuntimeException("Outstanding premi sudah ada sebelumnya, konfirmasi ke TI Kantor Pusat");
    }

    public void setEarthquakeTreaty(InsurancePolicyObjDefaultView obj)throws Exception{

        final String zonaMaipark = obj.getEQZoneCode();

        if(policy.getDtPeriodStart()!=null){

            DateTime dt = new DateTime(policy.getDtPeriodStart());
            // get the year
            int year = dt.getYear();

            if(year>=2020)
                obj.setStInsuranceTreatyID(getEarthquakeTreatyID(policy.getDtPeriodStart(), zonaMaipark));
        }
               


    }

    public String getEarthquakeTreatyID(Date per_start, String maiparkZone) throws Exception {
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
                    "   and earthquake_zone = ? ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, maiparkZone);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;

        } finally {

            S.release();
        }
    }

    public void editCreateClaimInward(String policyID) throws Exception {
        superEditPLA(policyID);

        checkActiveEffective();

        //policy.checkLastPolicyNo(policy.getStPolicyNo());

        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");

        //if(blockPremiNotPaid)
            //if(!policy.isPremiPaidInOldSystem())
                //throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");


        if(!policy.isStatusInward()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

      /*
      if(!policy.canClaimAgain()){
         throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Polis Sama");
      }*/

        if (!policy.isEffective()) {

            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIMINWARD);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        //policy.setStReference3(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        //policy.setDtPaymentDate(null);
        //policy.setStPaymentNotes(null);

        /*
        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            if(objx.getStOrderNo()==null){

                objx.setStOrderNo(String.valueOf(i+1));

                SQLUtil S = new SQLUtil();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ objx.getStOrderNo() +" where ins_pol_obj_id = ?");

                PS.setObject(1,obj.getStPolicyObjectID());

                int j = PS.executeUpdate();

                S.release();
            }
        }*/

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem();

            item.setStChargableFlag("Y");

            //item.setStROFlag("Y");
        }

        onChgCurrencyClaim();

        initTabs();
    }

    public DTOList getReverse() throws Exception {
        return policy.getReverse();
    }

    

    public void checkCustomer() throws Exception {

        String fileID = getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("ENTITY");

        final DTOList details = new DTOList();

//        headerProposal.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPol = row.getCell(3);//nomor polis
            HSSFCell cellTglLahir = row.getCell(4);//nomor polis

            String tglLahir = cellTglLahir.getStringCellValue();

            String toList = cellNoPol.getStringCellValue();
            String to[] = toList.split(",");

            DTOList entityList = null;
            for (int i = 0; i < to.length; i++) {
                if (!tglLahir.equalsIgnoreCase("NULL")) {
                    tglLahir = tglLahir.substring(0, 2) + "/" + tglLahir.substring(3, 5) + "/" + tglLahir.substring(6, 10);
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + cellNoPol.getStringCellValue() + " " + cellTglLahir.getStringCellValue());
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + tglLahir);
                    Date dateStart = DateUtil.getDate(tglLahir);
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + dateStart);

//                entityList = getEntityViewbyEntName(cellNoPol.getStringCellValue(), dateStart);
                    entityList = getEntityViewbyEntName(to[i], dateStart);
                } else {
//                entityList = getEntityViewbyEntName(cellNoPol.getStringCellValue(), null);
                    entityList = getEntityViewbyEntName(to[i], null);
                }
            }

            if (entityList.size() >= 1) {
                for (int i = 0; i < entityList.size(); i++) {
                    InsurancePolicyView inv = (InsurancePolicyView) entityList.get(i);

                    alasan = alasan + "<br>Debitur " + inv.getStReference1() + " " + inv.getStPolicyNo() + " Tgl.Lahir " + inv.getDtPolicyDate() + " Ada.";
                    cekGagal = true;
                }
            }
        }

        if (cekGagal) {
            throw new RuntimeException("Nama Debitur Terdetect : " + alasan);
        }
    }


    public DTOList getEntityViewbyEntName(String entname, Date tglLahir) throws Exception {

        String sql = "select a.pol_id,a.pol_no,b.ref1,b.refd1 as policy_date,b.order_no as ref2 "
                + "from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "where a.active_flag='Y' and  a.effective_flag='Y' "
                + "and  a.status IN ('POLICY','RENEWAL') and a.pol_type_id in (21,59) "
                + "and date_trunc('day',a.policy_date) >= '2015-01-01 00:00:00' "
                + "and date_trunc('day',a.policy_date) <= 'now' "
                + "and btrim(upper(b.ref1)) = ? ";

        if (tglLahir != null) {
            sql = sql + " and b.refd1 = '" + tglLahir + "'";
        }

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{entname.toUpperCase()},
                InsurancePolicyView.class);
    }

    public void approvalByDivisiClaim(String policyID) throws Exception {
        edit(policyID);

        approvalByDivisiMode = true;

        policy.setStRiskApproved("Y");
        policy.setStRiskApprovedWho(SessionManager.getInstance().getUserID());
        policy.setDtRiskApprovedDate(new Date());

        tabs.setActiveTab("TAB_OPTIONS");

    }

    public void doAddLampiranSumInsured(String instsipolid, BigDecimal tsi, String tsidesc, String tsiPenjelasan) throws Exception {

        //if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");

        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();

        ptsi.setStInsuranceTSIPolTypeID(instsipolid);

        if(tsidesc!=null)
                ptsi.setStTSICategoryDescription(tsidesc);

        if(tsiPenjelasan!=null)
            ptsi.setStDescription(tsiPenjelasan);

        ptsi.initializeDefaults();

        ptsi.setDbInsuredAmount(tsi);

        ptsi.markNew();

        selectedDefaultObject.getSuminsureds().add(ptsi);

    }

    public void editCreateEndorseKeterangan(InsurancePolicyView polis) throws Exception {
        superEdit(polis.getStPolicyID());

        policy.generateEndorseNo();
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(new BigDecimal(100));
        policy.setStPeriodBaseBeforeID("2");
        policy.setStEndorseNotes("ENDORSEMENT INTEREN :\n\n");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());
        if(startDate.isEqual(endDate))
                policy.setStAllowSamePeriodFlag("Y");

        policy.setStEndorseNotes("Pembentukan tanggal lahir Debitur di lampiran tertanggung polis induk atas permohonan Bank Sulselbar dan Cabang Makassar serta persetujuan Divisi Akseptasi");
        policy.setStAdminNotes("ENDORSE_TGL");

        policy.setStEffectiveFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStApprovedWho("05760202");
        policy.setDtApprovedDate(DateUtil.getDate("01/10/2020"));
        //policy.setStPassword(UserManager.getInstance().getUser().getStPasswd());
        //policy.setStClientIP(UserManager.getInstance().getUser().getIPAddress());
        policy.setDtPolicyDate(DateUtil.getDate("01/10/2020"));
        policy.setDtEndorseDate(DateUtil.getDate("01/10/2020"));
        //policy.setDtApprovedDate(new Date());



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

                cov.setDbPremi(BigDecimal.ZERO);
                cov.setDbPremiNew(BigDecimal.ZERO);
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }


    public void editCreateEndorseBatalTotalObjek(InsurancePolicyView polis, DTOList objek) throws Exception {
        superEdit(polis.getStPolicyID());

        //cekClosingStatus("INPUT");

        //checkActiveEffective();

        //if (!policy.isEffective())
            //throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID("2");
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStAdminNotes("PROSES_TI");

        policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList detailsCek = policy.getDetails();

        if(detailsCek.size()<1){
            //add feebase
            onNewDetailAutomatic("66","N", policy.getStEntityID(),new BigDecimal(10), null,null);

            //add komisi
            onNewDetailAutomatic("11","N", "951490",new BigDecimal(15.8),"1",null);

            //add brokerfee
            onNewDetailAutomatic("12","N", "1023859",new BigDecimal(10.2), "5",null);
        }
        

        final DTOList details = policy.getDetails();

        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            if(items.isStampFee() || items.isPolicyCost()) items.setDbAmount(BDUtil.negate(items.getDbAmount()));

        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                for (int k = 0; k < objek.size(); k++) {
                    InsurancePolicyView objectBatal = (InsurancePolicyView) objek.get(k);

                    if(objx.getStOrderNo().equalsIgnoreCase(objectBatal.getStReference1())){
                        cov.setDbInsuredAmount(BDUtil.zero);

                        if(!BDUtil.isZero(cov.getDbPremi())){
                            if(cov.isEntryPremi()&& !cov.isEntryRate()){
                                cov.setStEntryPremiFlag("Y");
                                cov.setDbPremi(BDUtil.negate(cov.getDbPremi()));
                                cov.setDbPremiNew(cov.getDbPremi());
                                cov.setStEntryRateFlag("N");
                            }
                        }
                        
                    }

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

                for (int k = 0; k < objek.size(); k++) {
                    InsurancePolicyView objectBatal = (InsurancePolicyView) objek.get(k);

                    if(objx.getStOrderNo().equalsIgnoreCase(objectBatal.getStReference1())){
                        tsi.setDbInsuredAmount(BDUtil.zero);
                    }

                }

            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);


        //initTabs();

    }

    public void onNewDetailAutomatic(String insItemID, String stReadOnly, String entityID, BigDecimal pct, String taxCode, BigDecimal amount) throws Exception {

        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(insItemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStROFlag(stReadOnly);

        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        if(pct!=null)
            item.setDbRate(pct);
        
        if(amount!=null)
            item.setDbAmount(amount);

        item.setStFlagEntryByRate("N");

        if(taxCode!=null)
            item.setStTaxCode(taxCode);

        if(entityID!=null) item.setStEntityID(entityID);

        getPolicy().getDetails().add(item);

        if(itemCat.getStInsuranceItemChildID()!=null)
            onNewDetailByChildID(itemCat.getStInsuranceItemChildID());

    }

    public void editCreateEndorseRIProses(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        if (!policy.isEffective())
            throw new RuntimeException("Policy not yet effective");

      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in Endorsable state");
      }*/

        policy.setStPolicyNo(policy.getStPolicyNo());
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStPeriodBaseBeforeID("2");
        policy.setDbPeriodRateBefore(BDUtil.hundred);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho("12061991");
        policy.setDtApprovedDate(new Date());
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setStEndorseNotes("ENDORSE RI");
        policy.setStAdminNotes("AUTO_PRC");

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                cov.setStEntryRateFlag("N");
                cov.setStAutoRateFlag("N");
                cov.setStEntryPremiFlag("Y");
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
            }

            //ganti treaty
            //obj.setStInsuranceTreatyID(null);
            //obj.getTreaties().deleteAll();
            //obj.setStInsuranceTreatyID("186");
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSERI);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);

        policy.recalculate();

        policy.recalculateTreaty();
        //policy.recalculateTreatyInitialize();

        //reasMode = true;

        //initTabs();

    }

    public void editCreateEndorseBentukKomisi(InsurancePolicyView polis, InsurancePolicyView endorse) throws Exception {
        superEdit(polis.getStPolicyID());

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        cekPolisExpire();

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
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID("2");
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStAdminNotes("FEEBASE");
        policy.setStEndorseNotes("PEMBENTUKAN FEEBASE TERMASUK PPN");

        policy.setStEffectiveFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStApprovedWho("00000002");
        policy.setDtApprovedDate(DateUtil.getDate("12/05/2021"));
        policy.setDtPolicyDate(DateUtil.getDate("12/05/2021"));
        policy.setDtEndorseDate(DateUtil.getDate("12/05/2021"));

        policy.checkEndorseNoBefore(policy.getStPolicyNo());

        if(true){
            //add feebase
            onNewDetailAutomatic("96","N", policy.getStEntityID(),null, null, endorse.getDbNDFeeBase1());

            //add komisi
            //onNewDetailAutomatic("11","N", "951490",new BigDecimal(15.8),"1");

            //add brokerfee
            //onNewDetailAutomatic("12","N", "1023859",new BigDecimal(10.2), "5");
        }


        final DTOList details = policy.getDetails();

        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            if(items.isStampFee() || items.isPolicyCost()) items.setDbAmount(BDUtil.zero);

        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                cov.setDbPremi(BDUtil.zero);
                cov.setDbPremiNew(BDUtil.zero);

            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    public void createPolisSplit(InsuranceSplitPolicyView policy) throws Exception{
        //dapetin polis nya
        //final InsuranceSplitPolicyView policy = getRemoteInsurance().getInsuranceSplitPolicy(policyID);

        final SQLUtil S = new SQLUtil();

        //dapetin objek nya
        final DTOList objects = policy.getObjectsProcess();

        for (int i = 0; i < objects.size(); i++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

            InsuranceSplitPolicyObjDefaultView objx = (InsuranceSplitPolicyObjDefaultView) obj;

            //buat polisnya
            InsurancePolicyView polisNew = new InsurancePolicyView();
            polisNew.setStCurrencyCode(policy.getStCurrencyCode());
            polisNew.setStPolicyTypeID(policy.getStPolicyTypeID());
            polisNew.setDtPolicyDate(new Date());
            polisNew.setStRegionID(policy.getStRegionID());
            polisNew.setDbCurrencyRate(policy.getDbCurrencyRate());
            polisNew.setStCostCenterCode(policy.getStCostCenterCode());

            polisNew.setStRegionIDSource(policy.getStRegionID());
            polisNew.setStCostCenterCodeSource(policy.getStCostCenterCode());

            if(policy.getStKreasiTypeID()!=null)
                polisNew.setStKreasiTypeID(policy.getStKreasiTypeID());

            //get aks non aks dari kode di user
            if(UserManager.getInstance().getUser().getStCostCenterCodeSource()!=null){
                //polisNew.setStRegionIDSource(UserManager.getInstance().getUser().getStRegionIDSource());
                //polisNew.setStCostCenterCodeSource(UserManager.getInstance().getUser().getStCostCenterCodeSource());
            }

            if(objx.getDtPeriodStart()!=null){
                polisNew.setDtPeriodStart(objx.getDtPeriodStart());
            }else{
                polisNew.setDtPeriodStart(objx.getDtReference1());
            }

            if(objx.getDtPeriodEnd()!=null){
                polisNew.setDtPeriodEnd(objx.getDtPeriodEnd());
            }else{
                polisNew.setDtPeriodEnd(objx.getDtReference2());
            }

            DateTime bulanNonAKS = new DateTime(DateUtil.getDate("31/12/2023"));
            DateTime tglAwal = new DateTime(polisNew.getDtPeriodStart());

            DateTime bulanNonAKS2 = new DateTime(DateUtil.getDate("01/01/2024"));

            //Jika cob non AKS dan periode awal > 1 januari 2024
            if(tglAwal.isAfter(bulanNonAKS)){
                if(polisNew.getPolicyType().getStBusinessTypeID().equalsIgnoreCase("1")){
                    //polisNew.setStRegionID("85");
                    //polisNew.setStCostCenterCode("80");
                }
            }
            
            //polisNew.setDtPeriodEnd(objx.getDtReference2());

            polisNew.setStCoverTypeCode(policy.getStCoverTypeCode());

            polisNew.setDetails(new DTOList());
            polisNew.setObjects(new DTOList());
            polisNew.setCovers(new DTOList());
            polisNew.setSuminsureds(new DTOList());
            polisNew.setCoins(new DTOList());
            polisNew.setDeductibles(new DTOList());
            polisNew.setInstallment(new DTOList());
            polisNew.setStPolicyID(null);

            //add coins
            addCoinsSplit(polisNew);
    
            EntityView ent = getEntity(objx.getStEntityID());            

            polisNew.setStEntityID(objx.getStEntityID());
            polisNew.setStCustomerName(ent.getStEntityName());

            if(objx.getStObjectDescriptionWithoutCounter()!=null)
                polisNew.setStCustomerName(ent.getStEntityName() + " QQ " + objx.getStObjectDescriptionWithoutCounter());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("1"))
                polisNew.setStCustomerName(ent.getStEntityName() + " QQ " + objx.getStReference11());

            polisNew.setStCustomerAddress(ent.getStAddress());

            EntityView prod = getEntity(objx.getStProducerID());
            polisNew.setStProducerID(objx.getStProducerID());
            polisNew.setStProducerName(prod.getStEntityName());
            polisNew.setStProducerAddress(prod.getStAddress());

            polisNew.setStMarketingOfficerWho(policy.getStMarketingOfficerWho());
            polisNew.setStPaymentCompanyID(objx.getStPaymentCompanyID());

            polisNew.setStWarranty(policy.getStWarranty());
            polisNew.setStCoLeaderID(policy.getStCoLeaderID());
            polisNew.setStCoinsPolicyNo(policy.getStCoinsPolicyNo());
            polisNew.setDbSharePct(policy.getDbSharePct());


            polisNew.setStPolicyTypeGroupID(policy.getStPolicyTypeGroupID());
            polisNew.setStInstallmentPeriodID(policy.getStInstallmentPeriodID());
            polisNew.setStInstallmentPeriods(policy.getStInstallmentPeriods());
            polisNew.setDbPeriodRate(objx.getDbPeriodRate());
            polisNew.setStPeriodBaseID(objx.getStPeriodBaseID());
            polisNew.setStPremiumFactorID(objx.getStPremiumFactorID());
            polisNew.setStRateMethod(policy.getStRateMethod());
            polisNew.setStRateMethodDesc(policy.getStRateMethodDesc());
            polisNew.setDbCurrencyRateTreaty(policy.getDbCurrencyRateTreaty());
            polisNew.setStPrintCode(null);
            polisNew.setStPrintStamp(null);
            polisNew.setStNextStatus(FinCodec.PolicyStatus.POLICY);
            polisNew.setStStatus(FinCodec.PolicyStatus.POLICY);
            polisNew.setStPostedFlag("N");
            polisNew.setStEffectiveFlag("N");
            polisNew.setStCoverNoteFlag("N");
            polisNew.setStApprovedWho(null);
            polisNew.setDtApprovedDate(null);
            polisNew.setStReference3(null);
            polisNew.setStClientIP(null);
            polisNew.setStPassword(null);
            polisNew.setStReadyToApproveFlag(null);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("87")||policy.getStPolicyTypeID().equalsIgnoreCase("88")){
                polisNew.defaultPeriods();
            }
                

            polisNew.markNew();

            //buat objeknya
            InsurancePolicyObjDefaultView objy = new InsurancePolicyObjDefaultView();

            objy.markNew();
            objy.setPolicy(polisNew);
            objy.setCoverage(new DTOList());
            objy.setSuminsureds(new DTOList());

            //InsurancePolicyObjDefaultView objy = (InsurancePolicyObjDefaultView) o;
            //objy.markNew();


            objy.setStRiskCategoryID(obj.getStRiskCategoryID());
            objy.setStObjectDescription(obj.getStObjectDescription());
            objy.setStReference1(objx.getStReference1());
            objy.setStReference2(objx.getStReference2());
            objy.setStReference3(objx.getStReference3());
            objy.setStReference4(objx.getStReference4());
            objy.setStReference5(objx.getStReference5());
            objy.setStReference6(objx.getStReference6());
            objy.setStReference7(objx.getStReference7());
            objy.setStReference8(objx.getStReference8());
            objy.setStReference9(objx.getStReference9());
            objy.setStReference10(objx.getStReference10());
            objy.setStReference11(objx.getStReference11());
            objy.setStReference12(objx.getStReference12());
            objy.setStReference13(objx.getStReference13());
            objy.setStReference14(objx.getStReference14());
            objy.setStReference15(objx.getStReference15());
            objy.setStReference16(objx.getStReference16());
            objy.setStReference17(objx.getStReference17());
            objy.setStReference18(objx.getStReference18());
            objy.setStReference19(objx.getStReference19());
            objy.setStReference20(objx.getStReference20());
            objy.setStReference21(objx.getStReference21());
            objy.setStReference22(objx.getStReference22());
            objy.setStReference23(objx.getStReference23());
            objy.setStReference24(objx.getStReference24());
            objy.setStReference25(objx.getStReference25());
            objy.setStReference26(objx.getStReference26());
            objy.setStReference27(objx.getStReference27());
            objy.setStReference28(objx.getStReference28());
            objy.setStReference29(objx.getStReference29());
            objy.setStReference30(objx.getStReference30());
            objy.setStReference31(objx.getStReference31());
            objy.setStReference32(objx.getStReference32());
            objy.setStReference33(objx.getStReference33());
            objy.setStReference34(objx.getStReference34());
            objy.setStReference35(objx.getStReference35());
            objy.setStReference36(objx.getStReference36());
            objy.setStReference37(objx.getStReference37());
            objy.setStReference38(objx.getStReference38());
            objy.setStReference39(objx.getStReference39());
            objy.setStReference40(objx.getStReference40());

            objy.setStRiskClass(objx.getStRiskClass());
            objy.setStReference9Desc(objx.getStReference9Desc());


            objy.setStReference1Desc(objx.getStReference1Desc());
            objy.setStReference2Desc(objx.getStReference2Desc());
            objy.setStReference3Desc(objx.getStReference3Desc());
            objy.setStReference4Desc(objx.getStReference4Desc());
            objy.setStReference5Desc(objx.getStReference5Desc());
            objy.setStReference6Desc(objx.getStReference6Desc());
            objy.setStReference7Desc(objx.getStReference7Desc());
            objy.setStReference8Desc(objx.getStReference8Desc());
            objy.setStReference9Desc(objx.getStReference9Desc());

            objy.setDtReference1(objx.getDtReference1());
            objy.setDtReference2(objx.getDtReference2());
            objy.setDtReference3(objx.getDtReference3());
            objy.setDtReference4(objx.getDtReference4());
            objy.setDtReference5(objx.getDtReference5());
            objy.setDtReference6(objx.getDtReference6());
            objy.setDtReference7(objx.getDtReference7());
            objy.setDtReference8(objx.getDtReference8());

            objy.setDbReference1(objx.getDbReference1());
            objy.setDbReference2(objx.getDbReference2());
            objy.setDbReference3(objx.getDbReference3());

            objy.setDbReference4(objx.getDbReference4());
            objy.setDbReference5(objx.getDbReference5());
            objy.setDbReference6(objx.getDbReference6());
            objy.setDbReference7(objx.getDbReference7());
            objy.setDbReference8(objx.getDbReference8());
            objy.setDbReference9(objx.getDbReference9());
            objy.setDbReference10(objx.getDbReference10());
            objy.setDbReference11(objx.getDbReference11());
            objy.setDbReference12(objx.getDbReference12());

            objy.setDbReference26(objx.getDbReference26());

            objy.setStInsuranceTreatyID(objx.getStInsuranceTreatyID());
            objy.setStRiskCategoryCode1(objx.getStRiskCategoryCode1());
            objy.setDbPeriodRate(objx.getDbPeriodRate());
            objy.setStPeriodBaseID(objx.getStPeriodBaseID());
            objy.setStPremiumFactorID(objx.getStPremiumFactorID());
            objy.setStPeriodFactorObjectFlag(objx.getStPeriodFactorObjectFlag());

            polisNew.getObjects().add(objy);

            //buat coverage
            final DTOList covers = obj.getCoverage();

            for (int j = 0; j < covers.size(); j++) {
                InsuranceSplitPolicyCoverView cov = (InsuranceSplitPolicyCoverView) covers.get(j);

                onNewCoverSplit(objy, cov);

            }

            //buat tsi
            final DTOList suminsured = obj.getSuminsureds();

            for (int k = 0; k < suminsured.size(); k++) {
                InsuranceSplitPolicyTSIView tsi = (InsuranceSplitPolicyTSIView) suminsured.get(k);

                onNewTSISplit(objy, tsi);
            }

            //buat deductible
            final DTOList deductible = obj.getDeductibles();

            for (int l = 0; l < deductible.size(); l++) {
                InsuranceSplitPolicyDeductibleView ded = (InsuranceSplitPolicyDeductibleView) deductible.get(l);

                onNewDeductibleSplit(objy, ded);
            }

            //installment
            onNewInstallmentSplit(polisNew);

            //add biaya polis materai
            addBiayaPolisMateraiAutomatic(polisNew, objx.getDbBiayaPolis(), objx.getDbBiayaMaterai());

            //feebase
            if(!BDUtil.isZeroOrNull(objx.getDbFeebasePct()))
                addFeeBaseAutomatically(polisNew, objx.getDbFeebasePct(), objx.getStFeebaseInsItemID());

            if(!BDUtil.isZeroOrNull(objx.getDbKomisiPct())){
                if(objx.getStKomisiEntityID()==null)
                    addKomisiAutomatically(polisNew, objx.getDbKomisiPct(), polisNew.getStProducerID());
                else
                    addKomisiAutomatically(polisNew, objx.getDbKomisiPct(), objx.getStKomisiEntityID());
            }       

            if(!BDUtil.isZeroOrNull(objx.getDbHandlingFeePct()))
                addHandlingFeeAutomatically(polisNew, objx.getDbHandlingFeePct(), polisNew.getStCoLeaderID());

            if(!BDUtil.isZeroOrNull(objx.getDbBrokerFeePct()))
                addBrokerfeeAutomatically(polisNew, objx.getDbBrokerFeePct(), objx.getStBrokerfeeInsItemID());

            polisNew.defineTreaty();
            polisNew.recalculateInterkoneksi();
            polisNew.recalculateTreaty();

            if(policy.getStPolicyTypeID().equalsIgnoreCase("87")||policy.getStPolicyTypeID().equalsIgnoreCase("88")){

                polisNew.setStReadyToApproveFlag("Y");
                polisNew.setStRIFinishFlag("Y");
            }

            getRemoteInsurance().saveFromSplit(polisNew,polisNew.getStNextStatus(),false);


            logger.logInfo("+++++++ UPDATE STATUS objek : "+ obj.getStPolicyObjectID() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_split_pol_obj set status = '1' where ins_pol_obj_id = ?");

            PS.setObject(1,obj.getStPolicyObjectID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ obj.getStPolicyObjectID() +" ++++++++++++++++++");

            S.release();

        }
    }

    public void onNewInstallmentSplit(InsurancePolicyView polisNew) throws Exception {
        InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();

        inst.markNew();

        polisNew.getInstallment().add(inst);

        String n = String.valueOf(polisNew.getInstallment().size());
    }

    public void onNewTSISplit(InsurancePolicyObjDefaultView objy, InsuranceSplitPolicyTSIView tsi) throws Exception{
        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();

                ptsi.setStInsuranceTSIPolTypeID(tsi.getStInsuranceTSIPolTypeID());

                ptsi.initializeDefaults();
                ptsi.setDbInsuredAmount(tsi.getDbInsuredAmount());

                ptsi.markNew();

                objy.getSuminsureds().add(ptsi);
    }

    public void onNewCoverSplit(InsurancePolicyObjDefaultView objy, InsuranceSplitPolicyCoverView cov) throws Exception{
        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

                cv.setStInsuranceCoverPolTypeID(cov.getStInsuranceCoverPolTypeID());

                cv.initializeDefaults();

                cv.setStInsuranceCoverID(cov.getStInsuranceCoverID());
                cv.setStCoverCategory(cov.getStCoverCategory());
                cv.setStEntryRateFlag(cov.getStEntryRateFlag());
                cv.setDbRate(cov.getDbRate());
                cv.setDbPremi(cov.getDbPremi());
                cv.setDbPremiNew(cov.getDbPremiNew());

                if(cov.getDbRate()==null && cov.getStEntryPremiFlag()!=null){
                    //cv.setStEntryPremiFlag(cov.getStEntryPremiFlag());
                }

                cv.markNew();

                objy.getCoverage().add(cv);
    }

    public void onNewDeductibleSplit(InsurancePolicyObjDefaultView objy, InsuranceSplitPolicyDeductibleView dedv) {
        final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

        ded.setStInsuranceClaimCauseID(dedv.getStInsuranceClaimCauseID());
        ded.setStCurrencyCode(dedv.getStCurrencyCode());
        ded.setDbPct(dedv.getDbPct());
        ded.setStDeductType(dedv.getStDeductType());
        ded.setDbAmountMin(dedv.getDbAmountMin());
        ded.setDbAmountMax(dedv.getDbAmountMax());
        ded.setDbAmount(dedv.getDbAmount());

        ded.markNew();

        objy.getDeductibles().add(ded);
    }

    public void addCoinsSplit(InsurancePolicyView polisNew) throws Exception{

        final InsuranceCoverSourceView cs = polisNew.getCoverSource();

        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

        co.markNew();
        co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);
        co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
        co.setStHoldingCompanyFlag("Y");
        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINS);

        polisNew.getCoins().add(co);
    }

    private void uploadBidBond51(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cellEntityID = row.getCell(1);//entity id
            HSSFCell cellProdID = row.getCell(2);//period base

            HSSFCell cell1 = row.getCell(8);//principal
            HSSFCell cell2 = row.getCell(9);//surety

            HSSFCell cell3 = row.getCell(10);//kode resiko
            HSSFCell cell4 = row.getCell(11);//tgl und
            HSSFCell cell5 = row.getCell(12);//nama proyek
            HSSFCell cell6 = row.getCell(13);//tempat tender
            HSSFCell cellKodeResiko = row.getCell(21);//kode resiko

            HSSFCell cellTSI = row.getCell(17);//kode resiko
            HSSFCell cellRate = row.getCell(18);//kode resiko
            HSSFCell cellPremi = row.getCell(19);//kode resiko


            HSSFCell cellBiayaPolis = row.getCell(3);//period base
            HSSFCell cellMaterai = row.getCell(4);//period base
            HSSFCell cellFeebasePct = row.getCell(5);//period base
            HSSFCell cellKomisiPct = row.getCell(6);//period base
            HSSFCell cellHFeePct = row.getCell(7);//period base
            //HSSFCell cellPremiumFactor = row.getCell(76);//premium factor

            HSSFCell cell13 = row.getCell(14);//tgl und
            HSSFCell cell14 = row.getCell(15);//tgl und



            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());


            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(new BigDecimal(cellHFeePct.getNumericCellValue()));

            EntityView principal = getEntity(cell1.getCellType() == cell1.CELL_TYPE_STRING ? cell1.getStringCellValue() : new BigDecimal(cell1.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference2(principal.getStEntityID()); //principal
            getSelectedDefaultObject().setStReference1(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference1Desc(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference3(principal.getStAddress()); //principal
            getSelectedDefaultObject().setStReference4(principal.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference5(principal.getStFunctionaryPosition()); //principal

            EntityView surety = getEntity(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference6(surety.getStEntityID()); //surety
            getSelectedDefaultObject().setStReference6Desc(surety.getStEntityName()); //surety
            getSelectedDefaultObject().setStReference7(surety.getStAddress()); //surety
            getSelectedDefaultObject().setStReference8(surety.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference9(surety.getStFunctionaryPosition()); //principal


            getSelectedDefaultObject().setStReference10(cell3.getStringCellValue()); //no surat undagan
            getSelectedDefaultObject().setStReference11(cell5.getStringCellValue()); //principal
            getSelectedDefaultObject().setStReference12(cell6.getStringCellValue()); //principal

            //getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cell4.getStringCellValue())); //awal
            if (cell4 != null) {
                if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cell4.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cell4.getDateCellValue());
                }
            }

            if (cell13 != null) {
                if (cell13.getCellType() == cell13.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cell13.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodStart(cell13.getDateCellValue());
                }
            }

            if (cell14 != null) {
                if (cell14.getCellType() == cell14.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cell14.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodEnd(cell14.getDateCellValue());
                }
            }


            getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());

            if (true) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(BDUtil.hundred);
                getSelectedDefaultObject().setStPeriodBaseID("2");
                getSelectedDefaultObject().setStPremiumFactorID("1");
            }

            /*
            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }


            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            */


            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(22);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(23);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(17);//tsi

            String tsiDesc1 = null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(24);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(25);//RATE
            HSSFCell cell_Rate1 = row.getCell(26);//premi
            HSSFCell cell_Premi1 = row.getCell(27);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }



            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadPerformanceBond52(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cellEntityID = row.getCell(1);//entity id
            HSSFCell cellProdID = row.getCell(2);//period base

            HSSFCell cellPrincipalID = row.getCell(8);//principal
            HSSFCell cellSuretyID = row.getCell(9);//surety

            HSSFCell cellNoSPPBJ = row.getCell(10);//kode resiko
            HSSFCell cellTglSPPBJ = row.getCell(11);//tgl und
            HSSFCell cellNoKontrak = row.getCell(12);//nama proyek
            HSSFCell cellTglKontrak = row.getCell(13);//tempat tender
            HSSFCell cellNamaProjek = row.getCell(14);//tgl und
            HSSFCell cellTglAwal = row.getCell(15);//tgl und
            HSSFCell cellTglAkhir = row.getCell(16);//tgl und

            HSSFCell cellKodeResiko = row.getCell(22);//kode resiko

            HSSFCell cellTSI = row.getCell(18);//kode resiko
            HSSFCell cellRate = row.getCell(19);//kode resiko
            HSSFCell cellPremi = row.getCell(20);//kode resiko


            HSSFCell cellBiayaPolis = row.getCell(3);//period base
            HSSFCell cellMaterai = row.getCell(4);//period base
            HSSFCell cellFeebasePct = row.getCell(5);//period base
            HSSFCell cellKomisiPct = row.getCell(6);//period base
            HSSFCell cellHFeePct = row.getCell(7);//period base
            //HSSFCell cellPremiumFactor = row.getCell(76);//premium factor


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(new BigDecimal(cellHFeePct.getNumericCellValue()));

            EntityView principal = getEntity(cellPrincipalID.getCellType() == cellPrincipalID.CELL_TYPE_STRING ? cellPrincipalID.getStringCellValue() : new BigDecimal(cellPrincipalID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference2(principal.getStEntityID()); //principal
            getSelectedDefaultObject().setStReference1(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference1Desc(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference3(principal.getStAddress()); //principal
            getSelectedDefaultObject().setStReference4(principal.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference5(principal.getStFunctionaryPosition()); //principal

            EntityView surety = getEntity(cellSuretyID.getCellType() == cellSuretyID.CELL_TYPE_STRING ? cellSuretyID.getStringCellValue() : new BigDecimal(cellSuretyID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference6(surety.getStEntityID()); //surety
            getSelectedDefaultObject().setStReference6Desc(surety.getStEntityName()); //surety
            getSelectedDefaultObject().setStReference7(surety.getStAddress()); //surety
            getSelectedDefaultObject().setStReference8(surety.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference9(surety.getStFunctionaryPosition()); //principal


            if (cellNoSPPBJ != null)
                getSelectedDefaultObject().setStReference10(cellNoSPPBJ.getStringCellValue()); //no surat undagan

            if (cellTglSPPBJ != null) {
                if (cellTglSPPBJ.getCellType() == cellTglSPPBJ.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellTglSPPBJ.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellTglSPPBJ.getDateCellValue());
                }
            }

            getSelectedDefaultObject().setStReference11(cellNoKontrak.getStringCellValue()); //principal

            if (cellTglKontrak != null) {
                if (cellTglKontrak.getCellType() == cellTglKontrak.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cellTglKontrak.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference2(cellTglKontrak.getDateCellValue());
                }
            }

            if (cellNamaProjek != null)
                getSelectedDefaultObject().setStReference12(cellNamaProjek.getStringCellValue()); //principal

            if (cellTglAwal != null) {
                if (cellTglAwal.getCellType() == cellTglAwal.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cellTglAwal.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodStart(cellTglAwal.getDateCellValue());
                }
            }

            if (cellTglAkhir != null) {
                if (cellTglAkhir.getCellType() == cellTglAkhir.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cellTglAkhir.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodEnd(cellTglAkhir.getDateCellValue());
                }
            }


            getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());


            if (true) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(BDUtil.hundred);
                getSelectedDefaultObject().setStPeriodBaseID("2");
                getSelectedDefaultObject().setStPremiumFactorID("1");
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(23);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(24);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(18);//tsi

            String tsiDesc1 = null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(25);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(26);//RATE
            HSSFCell cell_Rate1 = row.getCell(27);//premi
            HSSFCell cell_Premi1 = row.getCell(28);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadAdvancepaymentBond53(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cellEntityID = row.getCell(1);//entity id
            HSSFCell cellProdID = row.getCell(2);//period base

            HSSFCell cellPrincipalID = row.getCell(8);//principal
            HSSFCell cellSuretyID = row.getCell(9);//surety

            HSSFCell cellNoSPK = row.getCell(10);//kode resiko
            HSSFCell cellTglSPK = row.getCell(11);//tgl und
            HSSFCell cellNilaiKontrak = row.getCell(12);//nama proyek
            HSSFCell cellNamaProjek = row.getCell(13);//tgl und
            HSSFCell cellTglAwal = row.getCell(14);//tgl und
            HSSFCell cellTglAkhir = row.getCell(15);//tgl und

            HSSFCell cellKodeResiko = row.getCell(21);//kode resiko

            HSSFCell cellTSI = row.getCell(17);//kode resiko
            HSSFCell cellRate = row.getCell(18);//kode resiko
            HSSFCell cellPremi = row.getCell(19);//kode resiko


            HSSFCell cellBiayaPolis = row.getCell(3);//period base
            HSSFCell cellMaterai = row.getCell(4);//period base
            HSSFCell cellFeebasePct = row.getCell(5);//period base
            HSSFCell cellKomisiPct = row.getCell(6);//period base
            HSSFCell cellHFeePct = row.getCell(7);//period base
            //HSSFCell cellPremiumFactor = row.getCell(76);//premium factor


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(new BigDecimal(cellHFeePct.getNumericCellValue()));

            EntityView principal = getEntity(cellPrincipalID.getCellType() == cellPrincipalID.CELL_TYPE_STRING ? cellPrincipalID.getStringCellValue() : new BigDecimal(cellPrincipalID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference2(principal.getStEntityID()); //principal
            getSelectedDefaultObject().setStReference1(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference1Desc(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference3(principal.getStAddress()); //principal
            getSelectedDefaultObject().setStReference4(principal.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference5(principal.getStFunctionaryPosition()); //principal

            EntityView surety = getEntity(cellSuretyID.getCellType() == cellSuretyID.CELL_TYPE_STRING ? cellSuretyID.getStringCellValue() : new BigDecimal(cellSuretyID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference6(surety.getStEntityID()); //surety
            getSelectedDefaultObject().setStReference6Desc(surety.getStEntityName()); //surety
            getSelectedDefaultObject().setStReference7(surety.getStAddress()); //surety
            getSelectedDefaultObject().setStReference8(surety.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference9(surety.getStFunctionaryPosition()); //principal


            if (cellNoSPK != null)
                getSelectedDefaultObject().setStReference10(cellNoSPK.getStringCellValue()); //no surat undagan

            if (cellTglSPK != null) {
                if (cellTglSPK.getCellType() == cellTglSPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellTglSPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellTglSPK.getDateCellValue());
                }
            }

            getSelectedDefaultObject().setStReference11(cellNamaProjek.getStringCellValue()); //principal
            getSelectedDefaultObject().setDbReference2( new BigDecimal(cellNilaiKontrak.getNumericCellValue())); //principal

            if (cellTglAwal != null) {
                if (cellTglAwal.getCellType() == cellTglAwal.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cellTglAwal.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodStart(cellTglAwal.getDateCellValue());
                }
            }

            if (cellTglAkhir != null) {
                if (cellTglAkhir.getCellType() == cellTglAkhir.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cellTglAkhir.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodEnd(cellTglAkhir.getDateCellValue());
                }
            }


            getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());


            if (true) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(BDUtil.hundred);
                getSelectedDefaultObject().setStPeriodBaseID("2");
                getSelectedDefaultObject().setStPremiumFactorID("1");
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(22);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(23);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(17);//tsi

            String tsiDesc1 = null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(24);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(25);//RATE
            HSSFCell cell_Rate1 = row.getCell(26);//premi
            HSSFCell cell_Premi1 = row.getCell(27);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadMaintenanceBond54(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cellEntityID = row.getCell(1);//entity id
            HSSFCell cellProdID = row.getCell(2);//period base

            HSSFCell cellPrincipalID = row.getCell(8);//principal
            HSSFCell cellSuretyID = row.getCell(9);//surety

            HSSFCell cellNoBAST = row.getCell(10);//kode resiko
            HSSFCell cellTglBAST = row.getCell(11);//tgl und
            HSSFCell cellNamaProjek = row.getCell(12);//tgl und
            HSSFCell cellTglAwal = row.getCell(13);//tgl und
            HSSFCell cellTglAkhir = row.getCell(14);//tgl und

            HSSFCell cellKodeResiko = row.getCell(20);//kode resiko

            HSSFCell cellTSI = row.getCell(16);//kode resiko
            HSSFCell cellRate = row.getCell(17);//kode resiko
            HSSFCell cellPremi = row.getCell(18);//kode resiko


            HSSFCell cellBiayaPolis = row.getCell(3);//period base
            HSSFCell cellMaterai = row.getCell(4);//period base
            HSSFCell cellFeebasePct = row.getCell(5);//period base
            HSSFCell cellKomisiPct = row.getCell(6);//period base
            HSSFCell cellHFeePct = row.getCell(7);//period base
            //HSSFCell cellPremiumFactor = row.getCell(76);//premium factor


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(new BigDecimal(cellHFeePct.getNumericCellValue()));

            EntityView principal = getEntity(cellPrincipalID.getCellType() == cellPrincipalID.CELL_TYPE_STRING ? cellPrincipalID.getStringCellValue() : new BigDecimal(cellPrincipalID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference2(principal.getStEntityID()); //principal
            getSelectedDefaultObject().setStReference1(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference1Desc(principal.getStEntityName()); //principal
            getSelectedDefaultObject().setStReference3(principal.getStAddress()); //principal
            getSelectedDefaultObject().setStReference4(principal.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference5(principal.getStFunctionaryPosition()); //principal

            EntityView surety = getEntity(cellSuretyID.getCellType() == cellSuretyID.CELL_TYPE_STRING ? cellSuretyID.getStringCellValue() : new BigDecimal(cellSuretyID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference6(surety.getStEntityID()); //surety
            getSelectedDefaultObject().setStReference12(surety.getStEntityID());
            getSelectedDefaultObject().setStReference6Desc(surety.getStEntityName()); //surety
            getSelectedDefaultObject().setStReference7(surety.getStAddress()); //surety
            getSelectedDefaultObject().setStReference8(surety.getStFunctionaryName()); //principal
            getSelectedDefaultObject().setStReference9(surety.getStFunctionaryPosition()); //principal


            if (cellNoBAST != null)
                getSelectedDefaultObject().setStReference10(cellNoBAST.getStringCellValue()); //no surat undagan

            if (cellTglBAST != null) {
                if (cellTglBAST.getCellType() == cellTglBAST.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference1(DateUtil.getDate(cellTglBAST.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference1(cellTglBAST.getDateCellValue());
                }
            }

            getSelectedDefaultObject().setStReference11(cellNamaProjek.getStringCellValue()); //principal

            if (cellTglAwal != null) {
                if (cellTglAwal.getCellType() == cellTglAwal.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cellTglAwal.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodStart(cellTglAwal.getDateCellValue());
                }
            }

            if (cellTglAkhir != null) {
                if (cellTglAkhir.getCellType() == cellTglAkhir.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cellTglAkhir.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodEnd(cellTglAkhir.getDateCellValue());
                }
            }


            getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());


            if (true) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(BDUtil.hundred);
                getSelectedDefaultObject().setStPeriodBaseID("2");
                getSelectedDefaultObject().setStPremiumFactorID("1");
            }

            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(21);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(22);//ins_tsi_cat_id
            HSSFCell cell_TSI1 = row.getCell(16);//tsi

            String tsiDesc1 = null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(23);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(24);//RATE
            HSSFCell cell_Rate1 = row.getCell(25);//premi
            HSSFCell cell_Premi1 = row.getCell(26);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    public void addHandlingFeeAutomatically(InsurancePolicyView policy, BigDecimal komisiPct, String stEntityID)throws Exception{
        if(policy.isStatusHistory()||policy.isStatusEndorse()||policy.isStatusInward()) return;

        final String coverType = policy.getStCoverTypeCode();

        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic2(policy,"13","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic2(policy,"20","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic2(policy,"27","N", stEntityID, komisiPct);
        }

        if(coverType.equalsIgnoreCase("COINSOUTSELF")){
            onNewDetailAutomatic2(policy,"34","N", stEntityID, komisiPct);
        }

    }

    private void uploadKonsorsium(FileInputStream fis, String treatyID) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Conversi");

        //final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(),policy.getDtPeriodStart());

        if (sheet == null) {
            throw new RuntimeException("Sheet Conversi Tidak Ditemukan, download format konversi terbaru pada sistem !");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if(rows>104)
            throw new RuntimeException("Jumlah maksimal data yang bisa di upload adalah 100 data");


        for (int r = 4; r < rows+1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

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

            HSSFCell cell1 = row.getCell(1);//penggunaan
            HSSFCell cell2 = row.getCell(2);//kelas kontruksi
            HSSFCell cell3 = row.getCell(3);//kode resiko
            HSSFCell cell5 = row.getCell(5);//alamat
            HSSFCell cell6 = row.getCell(6);//kode pos
            HSSFCell cell8 = row.getCell(8);//nama
            HSSFCell cellKodeResiko = row.getCell(45);//nama
            HSSFCell cellKodePasar = row.getCell(70);//nama
            HSSFCell cellNamaPasar = row.getCell(7);//nama



            HSSFCell cellPeriodStart = row.getCell(34);//periode awal
            HSSFCell cellPeriodEnd = row.getCell(35);//periode akhir
            HSSFCell cell4 = row.getCell(44);//penerangan

            HSSFCell cellPeriodRate = row.getCell(10);//period rate
            HSSFCell cellPeriodBaseID = row.getCell(68);//period base
            HSSFCell cellPremiumFactor = row.getCell(69);//premium factor

            HSSFCell cellEntityID = row.getCell(37);//entity id
            HSSFCell cellProdID = row.getCell(38);//period base

            HSSFCell cellBiayaPolis = row.getCell(39);//period base
            HSSFCell cellMaterai = row.getCell(40);//period base
            HSSFCell cellFeebasePct = row.getCell(41);//period base
            HSSFCell cellKomisiPct = row.getCell(42);//period base
            //HSSFCell cellPremiumFactor = row.getCell(76);//premium factor


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue());
            getSelectedDefaultObject().setStRiskClass(cell2.getCellType() == cell2.CELL_TYPE_STRING ? cell2.getStringCellValue() : new BigDecimal(cell2.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference3(cell3.getCellType() == cell3.CELL_TYPE_STRING ? cell3.getStringCellValue() : new BigDecimal(cell3.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference4(cell4.getCellType() == cell4.CELL_TYPE_STRING ? cell4.getStringCellValue() : new BigDecimal(cell4.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference5(cell5.getCellType() == cell5.CELL_TYPE_STRING ? cell5.getStringCellValue() : new BigDecimal(cell5.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStReference11(cell8.getCellType() == cell8.CELL_TYPE_STRING ? cell8.getStringCellValue() : new BigDecimal(cell8.getNumericCellValue()).toString());


            String kodePos = policy.getStKodePos(cell6.getCellType() == cell6.CELL_TYPE_STRING ? cell6.getStringCellValue() : new BigDecimal(cell6.getNumericCellValue()).toString());
            if (kodePos != null) {
                getSelectedDefaultObject().setStReference9(kodePos);
            }

            if (cellPeriodStart != null) {
                if (cellPeriodStart.getCellType() == cellPeriodStart.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cellPeriodStart.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodStart(cellPeriodStart.getDateCellValue());
                }
            }

            if (cellPeriodEnd != null) {
                if (cellPeriodEnd.getCellType() == cellPeriodEnd.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cellPeriodEnd.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtPeriodEnd(cellPeriodEnd.getDateCellValue());
                }
            }


            if (cellKodeResiko != null) {
                getSelectedDefaultObject().setStRiskCategoryID(cellKodeResiko.getCellType() == cellKodeResiko.CELL_TYPE_STRING ? cellKodeResiko.getStringCellValue() : new BigDecimal(cellKodeResiko.getNumericCellValue()).toString());
            }

            

            if (cellKodePasar != null) {
                getSelectedDefaultObject().setStReference7(cellKodePasar.getCellType() == cellKodePasar.CELL_TYPE_STRING ? cellKodePasar.getStringCellValue() : new BigDecimal(cellKodePasar.getNumericCellValue()).toString());
                getSelectedDefaultObject().setStReference7Desc(cellNamaPasar.getCellType() == cellNamaPasar.CELL_TYPE_STRING ? cellNamaPasar.getStringCellValue() : new BigDecimal(cellNamaPasar.getNumericCellValue()).toString());
                logger.logDebug("############################## kode pasar = "+ getSelectedDefaultObject().getStReference7());
            }

            if (cellPeriodRate != null) {
                getSelectedDefaultObject().setStPeriodFactorObjectFlag("Y");
                //getSelectedDefaultObject().setDbPeriodRate(new BigDecimal(cellPeriodRate.getNumericCellValue()));
                getSelectedDefaultObject().setDbPeriodRate(cellPeriodRate.getCellType() == cellPeriodRate.CELL_TYPE_STRING ? new BigDecimal(cellPeriodRate.getStringCellValue()) : new BigDecimal(cellPeriodRate.getNumericCellValue()));
            }


            if (cellPeriodBaseID != null) {
                getSelectedDefaultObject().setStPeriodBaseID(cellPeriodBaseID.getCellType() == cellPeriodBaseID.CELL_TYPE_STRING ? cellPeriodBaseID.getStringCellValue() : new BigDecimal(cellPeriodBaseID.getNumericCellValue()).toString());
            }

            if (cellPremiumFactor != null) {
                getSelectedDefaultObject().setStPremiumFactorID(cellPremiumFactor.getCellType() == cellPremiumFactor.CELL_TYPE_STRING ? cellPremiumFactor.getStringCellValue() : new BigDecimal(cellPremiumFactor.getNumericCellValue()).toString());
            }

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));


            /*
             *ins_tcpt_id
            ins_tsi_cat_id
            TSI
             */

            //tsi 1
            HSSFCell cell_Ins_Tcpt_ID1 = row.getCell(46);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID1 = row.getCell(47);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC1 = row.getCell(14);//tsi desc
            HSSFCell cell_TSI1 = row.getCell(15);//tsi

            String tsiDesc1 = cell_TSI_DESC1!=null?cell_TSI_DESC1.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID1.getStringCellValue(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
                }
            } else if (cell_Ins_Tcpt_ID1.getCellType() == cell_Ins_Tcpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID1.getNumericCellValue()).toString(), new BigDecimal(cell_TSI1.getNumericCellValue()), tsiDesc1);
            }


            //tsi 2
            HSSFCell cell_Ins_Tcpt_ID2 = row.getCell(48);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID2 = row.getCell(49);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC2 = row.getCell(17);//tsi desc
            HSSFCell cell_TSI2 = row.getCell(18);//tsi

            String tsiDesc2 = cell_TSI_DESC2!=null?cell_TSI_DESC2.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID2.getStringCellValue(), new BigDecimal(cell_TSI2.getNumericCellValue()), tsiDesc2);
                }
            } else if (cell_Ins_Tcpt_ID2.getCellType() == cell_Ins_Tcpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID2.getNumericCellValue()).toString(), new BigDecimal(cell_TSI2.getNumericCellValue()), tsiDesc2);
            }

            //tsi 3
            HSSFCell cell_Ins_Tcpt_ID3 = row.getCell(50);//ins_tcpt_id
            HSSFCell cell_Ins_Tsi_Cat_ID3 = row.getCell(51);//ins_tsi_cat_id
            HSSFCell cell_TSI_DESC3 = row.getCell(20);//tsi desc
            HSSFCell cell_TSI3 = row.getCell(21);//tsi

            String tsiDesc3 = cell_TSI_DESC3!=null?cell_TSI_DESC3.getStringCellValue():null;

            if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Tcpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranSumInsured2(cell_Ins_Tcpt_ID3.getStringCellValue(), new BigDecimal(cell_TSI3.getNumericCellValue()), tsiDesc3);
                }
            } else if (cell_Ins_Tcpt_ID3.getCellType() == cell_Ins_Tcpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranSumInsured2(new BigDecimal(cell_Ins_Tcpt_ID3.getNumericCellValue()).toString(), new BigDecimal(cell_TSI3.getNumericCellValue()), tsiDesc3);
            }

            /*
            ins_cvpt_id
            ins_cover_id
            rate
            premi
            tsi
             */

            //cover 1
            HSSFCell cell_Ins_Cvpt_ID1 = row.getCell(52);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID1 = row.getCell(53);//RATE
            HSSFCell cell_Rate1 = row.getCell(54);//premi
            HSSFCell cell_Premi1 = row.getCell(55);//tsi
            //HSSFCell cell_TSI1  = row.getCell(23);//ins_cvpt_id

            BigDecimal rate1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate1.getNumericCellValue())) ? null : new BigDecimal(cell_Rate1.getNumericCellValue());
            BigDecimal premi1 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi1.getNumericCellValue())) ? null : new BigDecimal(cell_Premi1.getNumericCellValue());

            if (premi1 != null) {
                premi1 = premi1.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate1 != null) {
                rate1 = rate1.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID1.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID1.getStringCellValue(), rate1, premi1, null);
                }
            } else if (cell_Ins_Cvpt_ID1.getCellType() == cell_Ins_Cvpt_ID1.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID1.getNumericCellValue()).toString(), rate1, premi1, null);
            }

            //cover 2
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(56);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID2 = row.getCell(57);//RATE
            HSSFCell cell_Rate2 = row.getCell(58);//premi
            HSSFCell cell_Premi2 = row.getCell(59);//tsi
            //HSSFCell cell_TSI2  = row.getCell(28);//ins_cvpt_id

            BigDecimal rate2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate2.getNumericCellValue())) ? null : new BigDecimal(cell_Rate2.getNumericCellValue());
            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate2 != null) {
                rate2 = rate2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID2.getStringCellValue(), rate2, premi2, null);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), rate2, premi2, null);
            }

            //cover 3
            HSSFCell cell_Ins_Cvpt_ID3 = row.getCell(60);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID3 = row.getCell(61);//RATE
            HSSFCell cell_Rate3 = row.getCell(62);//premi
            HSSFCell cell_Premi3 = row.getCell(63);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate3.getNumericCellValue())) ? null : new BigDecimal(cell_Rate3.getNumericCellValue());
            BigDecimal premi3 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi3.getNumericCellValue())) ? null : new BigDecimal(cell_Premi3.getNumericCellValue());

            if (premi3 != null) {
                premi3 = premi3.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate3 != null) {
                rate3 = rate3.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID3.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID3.getStringCellValue(), rate3, premi3, null);
                }
            } else if (cell_Ins_Cvpt_ID3.getCellType() == cell_Ins_Cvpt_ID3.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID3.getNumericCellValue()).toString(), rate3, premi3, null);
            }

            //cover 4
            HSSFCell cell_Ins_Cvpt_ID4 = row.getCell(64);//ins_cover_id
            HSSFCell cell_Ins_Cover_ID4 = row.getCell(65);//RATE
            HSSFCell cell_Rate4 = row.getCell(66);//premi
            HSSFCell cell_Premi4 = row.getCell(67);//tsi
            //HSSFCell cell_TSI3  = row.getCell(33);//ins_cvpt_id

            BigDecimal rate4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Rate4.getNumericCellValue())) ? null : new BigDecimal(cell_Rate4.getNumericCellValue());
            BigDecimal premi4 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi4.getNumericCellValue())) ? null : new BigDecimal(cell_Premi4.getNumericCellValue());

            if (premi4 != null) {
                premi4 = premi4.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            if (rate4 != null) {
                rate4 = rate4.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID4.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverPA(cell_Ins_Cvpt_ID4.getStringCellValue(), rate4, premi4, null);
                }
            } else if (cell_Ins_Cvpt_ID4.getCellType() == cell_Ins_Cvpt_ID4.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverPA(new BigDecimal(cell_Ins_Cvpt_ID4.getNumericCellValue()).toString(), rate4, premi4, null);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }
    }

    private void uploadKreditSerbaguna(FileInputStream fis, String treatyID) throws Exception{
        POIFSFileSystem fsCek = new POIFSFileSystem(fis);
        HSSFWorkbook wbCek = new HSSFWorkbook(fsCek);

        HSSFSheet sheet2 = wbCek.getSheet("Konversi2020");

        if (sheet2 != null){
            uploadKreditSerbaguna(fsCek, treatyID); //existing
            //uploadKreditSerbaguna(fsCek, treatyID);
        }

    }

    private void uploadKreditSerbaguna(POIFSFileSystem fis, String treatyID) throws Exception {
        //POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheet("Konversi2020");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPolicyDate());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual!");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//nama
            HSSFCell cell2 = row.getCell(2);//tgl lahir
            HSSFCell cell3 = row.getCell(7);//tgl awal
            HSSFCell cell4 = row.getCell(8);//tgl akhir
            HSSFCell cell5 = row.getCell(11);//tsi
            HSSFCell cell6 = row.getCell(12);//premi

            HSSFCell cellPremiNetto = row.getCell(13);//premi netto
            HSSFCell cell7 = row.getCell(9);//rate
            HSSFCell cell8 = row.getCell(5);//usia
            HSSFCell cellKtp = row.getCell(16);//ktp
            HSSFCell cellPK = row.getCell(17);//pk
            HSSFCell cellTglPK = row.getCell(18);//tgl pk
            HSSFCell cellAlamat = row.getCell(19);//alamat
            HSSFCell cellLama = row.getCell(10);//lama
            HSSFCell cellStatus = row.getCell(48);//kategori
            HSSFCell cellKategori = row.getCell(47);//kategori debitur
            HSSFCell cellCoverage = row.getCell(49);//kategori debitur
            HSSFCell cellRekPinjaman = row.getCell(20);//rek pinjaman
            HSSFCell cellJenisKredit = row.getCell(21);//Jenis Kredit
            HSSFCell cellKodeUnik = row.getCell(22);//Kode Unik
            HSSFCell cellPembayaran = row.getCell(50);//Kode Unik
            HSSFCell cellCaraBayar = row.getCell(51);//Kode Unik

            HSSFCell cellCaraBayarPremi = row.getCell(54);//Kode Unik


            HSSFCell cellEntityID = row.getCell(28);//entity id
            HSSFCell cellProdID = row.getCell(29);//period base

            HSSFCell cellBiayaPolis = row.getCell(30);//period base
            HSSFCell cellMaterai = row.getCell(31);//period base
            HSSFCell cellFeebasePct = row.getCell(32);//period base
            HSSFCell cellKomisiPct = row.getCell(33);//period base

            HSSFCell cellKomisiEntID = row.getCell(34);//period base
            HSSFCell cellBrokerfeePct = row.getCell(35);//period base

            HSSFCell cellFeebaseInsItemID = row.getCell(52);//period base
            HSSFCell cellBrokerfeeInsItemID = row.getCell(53);//period base

            HSSFCell tglAwal100 = row.getCell(3);//tgl awal
            HSSFCell tglAkhir100 = row.getCell(4);//tgl akhir

            HSSFCell cellMOEntID = row.getCell(36);//mo ent id
            HSSFCell cellTagihanEntID = row.getCell(37);//mo ent id

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbBrokerFeePct(new BigDecimal(cellBrokerfeePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(BDUtil.zero);

            if(cellFeebaseInsItemID!=null)
                getSelectedDefaultObject().setStFeebaseInsItemID(cellFeebaseInsItemID.getCellType() == cellFeebaseInsItemID.CELL_TYPE_STRING ? cellFeebaseInsItemID.getStringCellValue() : new BigDecimal(cellFeebaseInsItemID.getNumericCellValue()).toString());

            if(cellBrokerfeeInsItemID!=null)
                getSelectedDefaultObject().setStBrokerfeeInsItemID(cellBrokerfeeInsItemID.getCellType() == cellBrokerfeeInsItemID.CELL_TYPE_STRING ? cellBrokerfeeInsItemID.getStringCellValue() : new BigDecimal(cellBrokerfeeInsItemID.getNumericCellValue()).toString());


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setDbReference11(new BigDecimal(cell6.getNumericCellValue()));

            if(cellPremiNetto!=null)
                getSelectedDefaultObject().setDbReference26(new BigDecimal(cellPremiNetto.getNumericCellValue()));

            if(cellCaraBayarPremi!=null){
                if (cellCaraBayarPremi.getCellType() == cellCaraBayarPremi.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference28(cellCaraBayarPremi.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference28(ConvertUtil.removeTrailing(String.valueOf(cellCaraBayarPremi.getNumericCellValue()))); //PNS/NON
                }
            }

            if(cellMOEntID!=null)
                policy.setStMarketingOfficerWho(cellMOEntID.getCellType() == cellMOEntID.CELL_TYPE_STRING ? cellMOEntID.getStringCellValue() : new BigDecimal(cellMOEntID.getNumericCellValue()).toString());

            if(cellTagihanEntID!=null)
                getSelectedDefaultObject().setStPaymentCompanyID(cellTagihanEntID.getCellType() == cellTagihanEntID.CELL_TYPE_STRING ? cellTagihanEntID.getStringCellValue() : new BigDecimal(cellTagihanEntID.getNumericCellValue()).toString());


            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cell5.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured2("601", b, null);

            double tes3 = cell6.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            BigDecimal d = null;
            if (cell7 != null) {
                double tes4 = cell7.getNumericCellValue();
                d = new BigDecimal(tes4);
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if (cell2 != null) {
                getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            }
            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }            //if(cellKtp!=null) getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //ktp
            //if(cellPK!=null) getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //pk
            //getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //kategori

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference4(ConvertUtil.removeTrailing(String.valueOf(cellPK.getNumericCellValue()))); //usia
                }
            }


            if (cellKategori.getCellType() == cellKategori.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference8(cellKategori.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference8(ConvertUtil.removeTrailing(String.valueOf(cellKategori.getNumericCellValue()))); //usia
            }
            if (cell8.getCellType() == cell8.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference2(cell8.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell8.getNumericCellValue()))); //usia
            }
            if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
            } else {
                getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
            }
            if (cellStatus.getCellType() == cellStatus.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //PNS/NON
            }
            if (cellTglPK != null) {
                if (cellTglPK.getCellType() == cellTglPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference4(DateUtil.getDate(cellTglPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue());
                }
            }

            if (cell3.getCellType() == cell3.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
                getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cell3.getStringCellValue()));
            } else {
                getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
                getSelectedDefaultObject().setDtPeriodStart(cell3.getDateCellValue());
            }

            if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
                getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cell4.getStringCellValue()));
            } else {
                getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
                getSelectedDefaultObject().setDtPeriodEnd(cell4.getDateCellValue());
            }

            if (cellRekPinjaman != null) {
                if (cellRekPinjaman.getCellType() == cellRekPinjaman.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference16(cellRekPinjaman.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference16(ConvertUtil.removeTrailing(String.valueOf(cellRekPinjaman.getNumericCellValue()))); //usia
                }
            }

            if (cellJenisKredit != null) {
                if (cellJenisKredit.getCellType() == cellJenisKredit.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference23(cellJenisKredit.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference23(ConvertUtil.removeTrailing(String.valueOf(cellJenisKredit.getNumericCellValue()))); //usia
                }
            }

            if (cellKodeUnik != null) {
                if (cellKodeUnik.getCellType() == cellKodeUnik.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference24(cellKodeUnik.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference24(ConvertUtil.removeTrailing(String.valueOf(cellKodeUnik.getNumericCellValue()))); //usia
                }
            }

            if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference13(cellCoverage.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference13(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
            }


            if (cellPembayaran != null){
                if (cellPembayaran.getCellType() == cellPembayaran.CELL_TYPE_STRING) {
                    if(!cellPembayaran.getStringCellValue().equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference26(cellPembayaran.getStringCellValue()); //usia
                } else {
                    if(!String.valueOf(cellPembayaran.getNumericCellValue()).equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference26(ConvertUtil.removeTrailing(String.valueOf(cellPembayaran.getNumericCellValue()))); //usia
                }
            }

            if (cellCaraBayar != null){
                if (cellCaraBayar.getCellType() == cellCaraBayar.CELL_TYPE_STRING) {
                    if(!cellCaraBayar.getStringCellValue().equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference27(cellCaraBayar.getStringCellValue()); //usia
                } else {
                    if(!String.valueOf(cellCaraBayar.getNumericCellValue()).equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference27(ConvertUtil.removeTrailing(String.valueOf(cellCaraBayar.getNumericCellValue()))); //usia
                }
            }

            if (cellKomisiEntID != null){
                if (cellKomisiEntID.getCellType() == cellKomisiEntID.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStKomisiEntityID(cellKomisiEntID.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStKomisiEntityID(ConvertUtil.removeTrailing(String.valueOf(cellKomisiEntID.getNumericCellValue()))); //usia
                }
            }



            if (tglAwal100 != null) {
                getSelectedDefaultObject().setDtReference7(tglAwal100.getDateCellValue()); //tgl lahir
            }

            if (tglAkhir100 != null) {
                getSelectedDefaultObject().setDtReference8(tglAkhir100.getDateCellValue()); //tgl lahir
            }


            /*
            if(cellStatus.getCellType()==cellStatus.CELL_TYPE_STRING)
            getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //status
            else
            getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //lama
             */

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(40);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(42);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cell7 != null) {
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            HSSFCell cellPremiPA = row.getCell(43);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cell7 == null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasiSerbaguna(cvptID3, ratePA2, premiPA2);

            //add coverkreasi
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(45);//ins_cover_id
            HSSFCell cell_Premi2 = row.getCell(46);//tsi

            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverKreasiSerbaguna(cell_Ins_Cvpt_ID2.getStringCellValue(), null, premi2);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverKreasiSerbaguna(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), null, premi2);
            }

            //VALIDATE
            int jangkaWaktuTahun = DateUtil.getYearsBetweenHUTB(getSelectedDefaultObject().getDtReference2(), getSelectedDefaultObject().getDtReference3());

            if(jangkaWaktuTahun > 5)
                  throw new RuntimeException("Jangka waktu pertanggungan asuransi kredit Debitur "+ getSelectedDefaultObject().getStReference1()+" maksimal 5 tahun");

            Date birth = getSelectedDefaultObject().getDtReference1();
            Date perStart = getSelectedDefaultObject().getDtReference2();
            Date perEnd = getSelectedDefaultObject().getDtReference3();

            DateTime birthDate = new DateTime(birth);
            DateTime startDate = new DateTime(perStart);
            DateTime endDate = new DateTime(perEnd);
            Months m = Months.monthsBetween(startDate, endDate);
            Years y = Years.yearsBetween(startDate, endDate);
            int mon = m.getMonths();
            int year = y.getYears();

            long age = 0;

            if(birth!=null) age = perStart.getTime()-(birth.getTime());

            age = age / (1000l*60l*60l*24l*365l);

            DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();

            if(startDate.isAfter(policyDateLastDay))
                throw new RuntimeException("Tanggal mulai kredit Debitur "+getSelectedDefaultObject().getStReference1()+" tidak boleh > tanggal polis");

            if(endDate.isBefore(startDate))
                    throw new RuntimeException("Tanggal akhir kredit Debitur "+getSelectedDefaultObject().getStReference1()+" tidak boleh kurang dari tanggal mulai");

                if(startDate.isEqual(birthDate))
                    throw new RuntimeException("Tanggal mulai kredit Debitur "+getSelectedDefaultObject().getStReference1()+" tidak boleh sama dengan tanggal lahir");

            if(startDate.isEqual(endDate))
                throw new RuntimeException("Tanggal mulai kredit Debitur "+getSelectedDefaultObject().getStReference1()+" tidak boleh sama dengan tanggal akhir");

            if(Integer.parseInt(getSelectedDefaultObject().getStReference2()) < 17)
                throw new RuntimeException("Umur Debitur "+getSelectedDefaultObject().getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

            if(Integer.parseInt(getSelectedDefaultObject().getStReference2()) > 75)
                throw new RuntimeException("Umur Debitur "+getSelectedDefaultObject().getStReference1()+" Tidak Boleh Melebihi 75 Tahun");

            if(age > 75 || age < 17)
                throw new RuntimeException("Umur Debitur "+getSelectedDefaultObject().getStReference1()+" Salah, Cek Tanggal Lahir");

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }

    }

    public void doAddLampiranCoverKreasiSerbaguna(String cvptid,BigDecimal rate,BigDecimal premi) throws Exception{

        final InsuranceSplitPolicyCoverView cv = new InsuranceSplitPolicyCoverView();

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

    private void uploadKreditMacet(FileInputStream fis, String treatyID) throws Exception{
        POIFSFileSystem fsCek = new POIFSFileSystem(fis);
        HSSFWorkbook wbCek = new HSSFWorkbook(fsCek);

        HSSFSheet sheet2 = wbCek.getSheet("Konversi2020");

        if (sheet2 != null){
            uploadKreditMacet(fsCek, treatyID); //existing
            //uploadKreditSerbaguna(fsCek, treatyID);
        }

    }

    private void uploadKreditMacet(POIFSFileSystem fis, String treatyID) throws Exception {
        //POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheet("Konversi2020");

        final String stRiskCategoryID = policy.getRiskCategoryID(policy.getStPolicyTypeID(), policy.getDtPolicyDate());

        if (sheet == null) {
            throw new RuntimeException("Sheet Konversi Tidak Ditemukan, download format excel terbaru pada menu user manual!");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        for (int r = 4; r < rows + 1; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            doNewLampiranObject();

            HSSFCell cell1 = row.getCell(1);//nama
            HSSFCell cell2 = row.getCell(2);//tgl lahir
            HSSFCell cell3 = row.getCell(7);//tgl awal
            HSSFCell cell4 = row.getCell(8);//tgl akhir
            HSSFCell cell5 = row.getCell(11);//tsi
            HSSFCell cell6 = row.getCell(12);//premi

            HSSFCell cellPremiNetto = row.getCell(13);//premi netto
            HSSFCell cell7 = row.getCell(9);//rate
            HSSFCell cell8 = row.getCell(5);//usia
            HSSFCell cellKtp = row.getCell(16);//ktp
            HSSFCell cellPK = row.getCell(17);//pk
            HSSFCell cellTglPK = row.getCell(18);//tgl pk
            HSSFCell cellAlamat = row.getCell(19);//alamat
            HSSFCell cellLama = row.getCell(10);//lama
            HSSFCell cellStatus = row.getCell(48);//kategori
            HSSFCell cellKategori = row.getCell(47);//kategori debitur
            HSSFCell cellCoverage = row.getCell(49);//kategori debitur
            HSSFCell cellRekPinjaman = row.getCell(20);//rek pinjaman
            HSSFCell cellJenisKredit = row.getCell(21);//Jenis Kredit
            HSSFCell cellKodeUnik = row.getCell(22);//Kode Unik
            HSSFCell cellPembayaran = row.getCell(50);//Kode Unik
            HSSFCell cellCaraBayar = row.getCell(51);//Kode Unik

            HSSFCell cellCaraBayarPremi = row.getCell(54);//Kode Unik


            HSSFCell cellEntityID = row.getCell(28);//entity id
            HSSFCell cellProdID = row.getCell(29);//period base

            HSSFCell cellBiayaPolis = row.getCell(30);//period base
            HSSFCell cellMaterai = row.getCell(31);//period base
            HSSFCell cellFeebasePct = row.getCell(32);//period base
            HSSFCell cellKomisiPct = row.getCell(33);//period base

            HSSFCell cellKomisiEntID = row.getCell(34);//period base
            HSSFCell cellBrokerfeePct = row.getCell(35);//period base

            HSSFCell cellFeebaseInsItemID = row.getCell(52);//period base
            HSSFCell cellBrokerfeeInsItemID = row.getCell(53);//period base

            HSSFCell tglAwal100 = row.getCell(3);//tgl awal
            HSSFCell tglAkhir100 = row.getCell(4);//tgl akhir

            HSSFCell cellMOEntID = row.getCell(36);//mo
            HSSFCell cellTagihanEntID = row.getCell(37);//mo

            getSelectedDefaultObject().setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));
            getSelectedDefaultObject().setDbBiayaMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));
            getSelectedDefaultObject().setDbFeebasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));
            getSelectedDefaultObject().setDbBrokerFeePct(new BigDecimal(cellBrokerfeePct.getNumericCellValue()));
            getSelectedDefaultObject().setDbHandlingFeePct(BDUtil.zero);

            if(cellFeebaseInsItemID!=null)
                getSelectedDefaultObject().setStFeebaseInsItemID(cellFeebaseInsItemID.getCellType() == cellFeebaseInsItemID.CELL_TYPE_STRING ? cellFeebaseInsItemID.getStringCellValue() : new BigDecimal(cellFeebaseInsItemID.getNumericCellValue()).toString());

            if(cellBrokerfeeInsItemID!=null)
                getSelectedDefaultObject().setStBrokerfeeInsItemID(cellBrokerfeeInsItemID.getCellType() == cellBrokerfeeInsItemID.CELL_TYPE_STRING ? cellBrokerfeeInsItemID.getStringCellValue() : new BigDecimal(cellBrokerfeeInsItemID.getNumericCellValue()).toString());


            getSelectedDefaultObject().setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
            getSelectedDefaultObject().setStProducerID(cellProdID.getCellType() == cellProdID.CELL_TYPE_STRING ? cellProdID.getStringCellValue() : new BigDecimal(cellProdID.getNumericCellValue()).toString());

            getSelectedDefaultObject().setDbReference11(new BigDecimal(cell6.getNumericCellValue()));

            if(cellPremiNetto!=null)
                getSelectedDefaultObject().setDbReference26(new BigDecimal(cellPremiNetto.getNumericCellValue()));

            if(cellCaraBayarPremi!=null){
                if (cellCaraBayarPremi.getCellType() == cellCaraBayarPremi.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference28(cellCaraBayarPremi.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference28(ConvertUtil.removeTrailing(String.valueOf(cellCaraBayarPremi.getNumericCellValue()))); //PNS/NON
                }
            }

            if (cell1.getCellType() != cell1.CELL_TYPE_STRING) {
                throw new RuntimeException("Kolom Nama Harus String/Character");
            }

            if (cell5.getCellType() != cell5.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom TSI Harus Numeric");
            }

            if (cell6.getCellType() != cell6.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Premi Harus Numeric");
            }

            double a = cell5.getNumericCellValue();
            BigDecimal b = new BigDecimal(a);

            doAddLampiranSumInsured2("600", b, null);

            double tes3 = cell6.getNumericCellValue();
            BigDecimal c = new BigDecimal(tes3);

            BigDecimal d = null;
            if (cell7 != null) {
                double tes4 = cell7.getNumericCellValue();
                d = new BigDecimal(tes4);
            }

            getSelectedDefaultObject().setStReference1(cell1.getStringCellValue()); //nama
            if (cell2 != null) {
                getSelectedDefaultObject().setDtReference1(cell2.getDateCellValue()); //tgl lahir
            }
            if (cellAlamat != null) {
                getSelectedDefaultObject().setStReference6(cellAlamat.getStringCellValue()); //alamat
            }            //if(cellKtp!=null) getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //ktp
            //if(cellPK!=null) getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //pk
            //getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //kategori

            if (cellKtp != null) {
                if (cellKtp.getCellType() == cellKtp.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference3(cellKtp.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference3(ConvertUtil.removeTrailing(String.valueOf(cellKtp.getNumericCellValue()))); //usia
                }
            }

            if (cellPK != null) {
                if (cellPK.getCellType() == cellPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference4(cellPK.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference4(ConvertUtil.removeTrailing(String.valueOf(cellPK.getNumericCellValue()))); //usia
                }
            }


            if (cellKategori.getCellType() == cellKategori.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference8(cellKategori.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference8(ConvertUtil.removeTrailing(String.valueOf(cellKategori.getNumericCellValue()))); //usia
            }
            if (cell8.getCellType() == cell8.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference2(cell8.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference2(ConvertUtil.removeTrailing(String.valueOf(cell8.getNumericCellValue()))); //usia
            }
            if (cellLama.getCellType() == cellLama.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference5(cellLama.getStringCellValue()); //lama
            } else {
                getSelectedDefaultObject().setStReference5(ConvertUtil.removeTrailing(String.valueOf(cellLama.getNumericCellValue()))); //lama
            }
            if (cellStatus.getCellType() == cellStatus.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //PNS/NON
            }
            if (cellTglPK != null) {
                if (cellTglPK.getCellType() == cellTglPK.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setDtReference4(DateUtil.getDate(cellTglPK.getStringCellValue())); //awal
                } else {
                    getSelectedDefaultObject().setDtReference4(cellTglPK.getDateCellValue());
                }
            }

            if (cell3.getCellType() == cell3.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference2(DateUtil.getDate(cell3.getStringCellValue())); //awal
                getSelectedDefaultObject().setDtPeriodStart(DateUtil.getDate(cell3.getStringCellValue()));
            } else {
                getSelectedDefaultObject().setDtReference2(cell3.getDateCellValue());
                getSelectedDefaultObject().setDtPeriodStart(cell3.getDateCellValue());
            }

            if (cell4.getCellType() == cell4.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setDtReference3(DateUtil.getDate(cell4.getStringCellValue())); //awal
                getSelectedDefaultObject().setDtPeriodEnd(DateUtil.getDate(cell4.getStringCellValue()));
            } else {
                getSelectedDefaultObject().setDtReference3(cell4.getDateCellValue());
                getSelectedDefaultObject().setDtPeriodEnd(cell4.getDateCellValue());
            }

            if (cellRekPinjaman != null) {
                if (cellRekPinjaman.getCellType() == cellRekPinjaman.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference16(cellRekPinjaman.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference16(ConvertUtil.removeTrailing(String.valueOf(cellRekPinjaman.getNumericCellValue()))); //usia
                }
            }

            if (cellJenisKredit != null) {
                if (cellJenisKredit.getCellType() == cellJenisKredit.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference23(cellJenisKredit.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference23(ConvertUtil.removeTrailing(String.valueOf(cellJenisKredit.getNumericCellValue()))); //usia
                }
            }

            if (cellKodeUnik != null) {
                if (cellKodeUnik.getCellType() == cellKodeUnik.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStReference24(cellKodeUnik.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStReference24(ConvertUtil.removeTrailing(String.valueOf(cellKodeUnik.getNumericCellValue()))); //usia
                }
            }

            if (cellCoverage.getCellType() == cellCoverage.CELL_TYPE_STRING) {
                getSelectedDefaultObject().setStReference13(cellCoverage.getStringCellValue()); //usia
            } else {
                getSelectedDefaultObject().setStReference13(ConvertUtil.removeTrailing(String.valueOf(cellCoverage.getNumericCellValue()))); //usia
            }


            if (cellPembayaran != null){
                if (cellPembayaran.getCellType() == cellPembayaran.CELL_TYPE_STRING) {
                    if(!cellPembayaran.getStringCellValue().equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference26(cellPembayaran.getStringCellValue()); //usia
                } else {
                    if(!String.valueOf(cellPembayaran.getNumericCellValue()).equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference26(ConvertUtil.removeTrailing(String.valueOf(cellPembayaran.getNumericCellValue()))); //usia
                }
            }

            if (cellCaraBayar != null){
                if (cellCaraBayar.getCellType() == cellCaraBayar.CELL_TYPE_STRING) {
                    if(!cellCaraBayar.getStringCellValue().equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference27(cellCaraBayar.getStringCellValue()); //usia
                } else {
                    if(!String.valueOf(cellCaraBayar.getNumericCellValue()).equalsIgnoreCase("0"))
                        getSelectedDefaultObject().setStReference27(ConvertUtil.removeTrailing(String.valueOf(cellCaraBayar.getNumericCellValue()))); //usia
                }
            }

            if (cellKomisiEntID != null){
                if (cellKomisiEntID.getCellType() == cellKomisiEntID.CELL_TYPE_STRING) {
                    getSelectedDefaultObject().setStKomisiEntityID(cellKomisiEntID.getStringCellValue()); //usia
                } else {
                    getSelectedDefaultObject().setStKomisiEntityID(ConvertUtil.removeTrailing(String.valueOf(cellKomisiEntID.getNumericCellValue()))); //usia
                }
            }


            if (tglAwal100 != null) {
                getSelectedDefaultObject().setDtReference7(tglAwal100.getDateCellValue()); //tgl lahir
            }

            if (tglAkhir100 != null) {
                getSelectedDefaultObject().setDtReference8(tglAkhir100.getDateCellValue()); //tgl lahir
            }

            if(cellMOEntID!=null)
                policy.setStMarketingOfficerWho(cellMOEntID.getCellType() == cellMOEntID.CELL_TYPE_STRING ? cellMOEntID.getStringCellValue() : new BigDecimal(cellMOEntID.getNumericCellValue()).toString());

            if(cellTagihanEntID!=null)
                getSelectedDefaultObject().setStPaymentCompanyID(cellTagihanEntID.getCellType() == cellTagihanEntID.CELL_TYPE_STRING ? cellTagihanEntID.getStringCellValue() : new BigDecimal(cellTagihanEntID.getNumericCellValue()).toString());


            /*
            if(cellStatus.getCellType()==cellStatus.CELL_TYPE_STRING)
            getSelectedDefaultObject().setStReference7(cellStatus.getStringCellValue()); //status
            else
            getSelectedDefaultObject().setStReference7(ConvertUtil.removeTrailing(String.valueOf(cellStatus.getNumericCellValue()))); //lama
             */

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);

            //add coverkreasi
            HSSFCell cellCoverPA = row.getCell(40);
            if (cellCoverPA.getCellType() != cellCoverPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom CvptID Harus Numeric");
            }

            double cvptID = cellCoverPA.getNumericCellValue();
            BigDecimal cvptID2 = new BigDecimal(cvptID);
            String cvptID3 = cvptID2.toString();

            HSSFCell cellRatePA = row.getCell(42);
            if (cellRatePA.getCellType() != cellRatePA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal ratePA2 = null;
            if (cell7 != null) {
                double ratePA = cellRatePA.getNumericCellValue();
                ratePA2 = new BigDecimal(ratePA);
            }

            HSSFCell cellPremiPA = row.getCell(43);
            if (cellPremiPA.getCellType() != cellPremiPA.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("Kolom Rate Harus Numeric");
            }

            BigDecimal premiPA2 = null;
            if (cell7 == null) {
                double premiPA = cellPremiPA.getNumericCellValue();
                premiPA2 = new BigDecimal(premiPA);
            }

            doAddLampiranCoverKreasiSerbaguna(cvptID3, ratePA2, premiPA2);

            //add coverkreasi
            HSSFCell cell_Ins_Cvpt_ID2 = row.getCell(45);//ins_cover_id
            HSSFCell cell_Premi2 = row.getCell(46);//tsi

            BigDecimal premi2 = BDUtil.isZeroOrNull(new BigDecimal(cell_Premi2.getNumericCellValue())) ? null : new BigDecimal(cell_Premi2.getNumericCellValue());

            if (premi2 != null) {
                premi2 = premi2.setScale(6, BigDecimal.ROUND_HALF_UP);
            }

            if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_STRING) {
                if (!cell_Ins_Cvpt_ID2.getStringCellValue().equalsIgnoreCase("NULL")) {
                    doAddLampiranCoverKreasiSerbaguna(cell_Ins_Cvpt_ID2.getStringCellValue(), null, premi2);
                }
            } else if (cell_Ins_Cvpt_ID2.getCellType() == cell_Ins_Cvpt_ID2.CELL_TYPE_NUMERIC) {
                doAddLampiranCoverKreasiSerbaguna(new BigDecimal(cell_Ins_Cvpt_ID2.getNumericCellValue()).toString(), null, premi2);
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }

    }

    public void onNewDetailByChildID(InsurancePolicyView policy, String itemID) throws Exception {

        //if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(itemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(itemID);

        item.setStInsuranceItemCategory(itemCat.getStItemCategory());

        //item.setStROFlag("Y");

        if(itemCat.getDbDefaultValue()!=null){
            item.setStFlagEntryByRate("Y");
            item.setDbRate(itemCat.getDbDefaultValue());
        }

        policy.getDetails().add(item);

    }

    public void addBrokerfeeAutomatically(InsurancePolicyView policy, BigDecimal feebasePct, String insItemID)throws Exception{

        final String coverType = policy.getStCoverTypeCode();

        if(coverType.equalsIgnoreCase("DIRECT")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStProducerID(), feebasePct);
        }

        if(coverType.equalsIgnoreCase("COINSOUT")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStProducerID(), feebasePct);
        }

        if(coverType.equalsIgnoreCase("COINSIN")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStProducerID(), feebasePct);
        }

        if(coverType.equalsIgnoreCase("COINSOUTSELF")){
            onNewDetailAutomatic2(policy,insItemID,"N", policy.getStProducerID(), feebasePct);
        }

    }


}
/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyListForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:25 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.common.model.Filter;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;
import com.crux.util.crypt.Digest;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.FinCodec;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsuranceItemsView;
import com.webfin.insurance.model.InsuranceKreasiView;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyTreatyView;
import com.webfin.insurance.model.InsuranceSplitPolicyCoverView;
import com.webfin.insurance.model.InsuranceSplitPolicyObjectView;
import com.webfin.insurance.model.InsuranceSplitPolicyTSIView;
import com.webfin.insurance.model.InsuranceSplitPolicyView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PolicySplitListForm extends Form {
    
    private final static transient LogManager logger = LogManager.getInstance(PolicySplitListForm.class);
    
    private String policyID;
    private String stPrintForm;
    private String stLang;
    private String stFontSize;
    private String stAttached;
    private String stOutstandingFlag="Y";
    private String stNotApprovedPolicy="N";
    private String stShowReinsNotApproved="N";
    private DTOList list;
    private boolean enableCreateSPPA;
    private boolean enableCreatePolis;
    private boolean enableCreateProduction;
    private boolean enableCreateProposal;
    private boolean enableEdit = false;
    private boolean enableRenewal;
    private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
    private boolean enableEndorsement;
    private boolean enableClaim;
    private boolean enableView = true;
    private boolean enablePrint = false;
    private String printingLOV = "";
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    
    private Date dtFilterPolicyDateFrom;
    private Date dtFilterPolicyDateTo;
    private Date dtFilterPolicyExpireFrom;
    private Date dtFilterPolicyExpireTo;
    private HashSet editFilter = new HashSet();
    private boolean reas;
    private boolean canNavigateBranch;
    private boolean canEdit;
    private boolean canCreate;
    private boolean canApprove;
    private boolean listEffective;
    private boolean claim;
    private boolean canCreatePolicyHistory;
    private boolean policy;
    
    private DTOList pakreasilist;
    
    private String stName;
    private Date dtBirthDate;
    private String stPolicyLevel="POLICY";
    private Date dtLiquidity;
    private Date dtEndOfCredit;
    
    private String stAuthorized;
    private boolean isShowFilter = false;
    
    private String stPolicy;
    
    private boolean kreasiNavigateBranch = SessionManager.getInstance().getSession().hasResource("KREASI_NAVBR");
    
    private boolean approveUW;
    private String stPolicyGroup;
    private boolean enableEditClaim;
    private boolean enableInputPaymentDate;
    private String stCreateWho;
    private String stPolicyNo;
    
    private boolean isBondingDivision = false;// = SessionManager.getInstance().getSession().hasResource("BONDING_DIVISION");

    private String stShowClaimsNotApproved="N";
    
    private String stShowClaimsOnly="N";
    
    private String stPoliceNo;
    private String stYearBuilt;
    private String stChassisNo;
    private String stMachineNo;
    private String stSeat;
    private String stUsage;
    private String stType;
    private String stTypeOfVehicle;
    private String stNama;
    private String stKreasi;
    
    private boolean canReverse = SessionManager.getInstance().getSession().hasResource("CAN_REVERSE");
    
    private String stFileUpload;
    private String stEndorseMode;
    
    private boolean approvalByDirector = SessionManager.getInstance().getSession().hasResource("APPROVE_BY_DIRECTOR");
    
    private boolean claimApprovalByDirector = SessionManager.getInstance().getSession().hasResource("CLAIM_APPROVE_BY_DIRECTOR");
    
    private String stPolicyStatus;
    private String stPolicyCustomer;
    private String stPrincipal;
    private String stLimitFlag = "3";
    private String stNotPrintedPolicy="N";
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private boolean canNavigateRegion;
    
    private boolean reinsApprovalByDirector = SessionManager.getInstance().getSession().hasResource("APPROVE_BY_DIRECTOR_RI");
    private boolean canReverseReins;
    private boolean canApproveReins;
    
    private Date dtPolicyDateFrom;
    private Date dtPolicyDateTo;
    private String stAge;

    private Date dtLiquidityFrom;
    private Date dtLiquidityTo;
    private Date dtEndOfCreditFrom;
    private Date dtEndOfCreditTo;

    private String stKey;
    private String stCriteria;

    //private boolean enableUserSuperEdit = SessionManager.getInstance().getSession().hasResource("USER_POL_SUPER_EDIT");
    //private boolean canInputManualPolicy = SessionManager.getInstance().getSession().hasResource("CAN_DELETE_SIGN_CODE");
    private boolean canDeleteSignCode = SessionManager.getInstance().getSession().hasResource("POL_DELETE_SIGNCODE");
    private boolean enablePrintPreSign = false;
    private boolean enablePrintDigitized = false;

    private String stTglTahun;
    private String stUsia;
    private String stKreasiKredit;
    private String stJangkaWaktu;
    private boolean canApproveHeadOffice;
    private String stEntityID;

    private boolean canApproveReinsOnly;
    private boolean enablePrintDigitalPolicy = false;

    private boolean canApproveAnalisaResiko;
    private String stShowDataReject = "N";
    private DTOList listPolicyChecking;

    private boolean approvalByDivisi = SessionManager.getInstance().getSession().hasResource("APPROVE_BY_DIVISI");

    private boolean canCreateClaimInward;

    private String stNoPerjanjianKredit;
    private String stNoRekeningPinjaman;
    private String stNoIdentitas;
    private String stKategoriDeb;
    private String stPekerjaanDeb;

    private boolean claimApprovalByDivisi = SessionManager.getInstance().getSession().hasResource("CLAIM_APPROVE_BY_DIVISI");

    public boolean isClaimApprovalByDivisi() {
        return claimApprovalByDivisi;
    }

    public void setClaimApprovalByDivisi(boolean claimApprovalByDivisi) {
        this.claimApprovalByDivisi = claimApprovalByDivisi;
    }

    public String getStNoIdentitas() {
        return stNoIdentitas;
    }

    public void setStNoIdentitas(String stNoIdentitas) {
        this.stNoIdentitas = stNoIdentitas;
    }

    public String getStNoPerjanjianKredit() {
        return stNoPerjanjianKredit;
    }

    public void setStNoPerjanjianKredit(String stNoPerjanjianKredit) {
        this.stNoPerjanjianKredit = stNoPerjanjianKredit;
    }

    public String getStNoRekeningPinjaman() {
        return stNoRekeningPinjaman;
    }

    public void setStNoRekeningPinjaman(String stNoRekeningPinjaman) {
        this.stNoRekeningPinjaman = stNoRekeningPinjaman;
    }

    public boolean isCanCreateClaimInward() {
        return canCreateClaimInward;
    }

    public void setCanCreateClaimInward(boolean canCreateClaimInward) {
        this.canCreateClaimInward = canCreateClaimInward;
    }

    public boolean isApprovalByDivisi() {
        return approvalByDivisi;
    }

    public void setApprovalByDivisi(boolean approvalByDivisi) {
        this.approvalByDivisi = approvalByDivisi;
    }

    public String getStPolicyGroup() {
        return stPolicyGroup;
    }
    
    public void setStPolicyGroup(String stPolicyGroup) {
        this.stPolicyGroup = stPolicyGroup;
    }
    
    public String getStCreateWho() {
        return stCreateWho;
    }
    
    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }
    
    public String getStAuthorized() {
        return stAuthorized;
    }
    
    public void setStAuthorized(String stAuthorized) {
        this.stAuthorized = stAuthorized;
    }
    
    private BigDecimal dbOverLimit;
    
    public boolean isClaim() {
        return claim;
    }
    
    public void setClaim(boolean claim) {
        this.claim = claim;
    }
    
    public boolean isReas() {
        return reas;
    }
    
    public void setReas(boolean reas) {
        this.reas = reas;
    }
    
    public boolean isPolicy() {
        return policy;
    }
    
    public void setPolicy(boolean policy) {
        this.policy = policy;
    }
    
    public String getStOutstandingFlag() {
        return stOutstandingFlag;
    }
    
    public void setStOutstandingFlag(String stOutstandingFlag) {
        this.stOutstandingFlag = stOutstandingFlag;
    }
    
    public String getStNotApprovedPolicy() {
        return stNotApprovedPolicy;
    }
    
    public void setStNotApprovedPolicy(String stNotApprovedPolicy) {
        this.stNotApprovedPolicy = stNotApprovedPolicy;
    }
    
    public String getStShowReinsNotApproved() {
        return stShowReinsNotApproved;
    }
    
    public void setStShowReinsNotApproved(String stShowReinsNotApproved) {
        this.stShowReinsNotApproved = stShowReinsNotApproved;
    }
    
    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }
    
    public boolean isKreasiNavigateBranch() {
        return kreasiNavigateBranch;
    }
    
    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }
    
    public String getStBranch() {
        return stBranch;
    }
    
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }
    
    public Date getDtFilterPolicyDateFrom() {
        return dtFilterPolicyDateFrom;
    }
    
    public void setDtFilterPolicyDateFrom(Date dtFilterPolicyDateFrom) {
        this.dtFilterPolicyDateFrom = dtFilterPolicyDateFrom;
    }
    
    public Date getDtFilterPolicyDateTo() {
        return dtFilterPolicyDateTo;
    }
    
    public void setDtFilterPolicyDateTo(Date dtFilterPolicyDateTo) {
        this.dtFilterPolicyDateTo = dtFilterPolicyDateTo;
    }
    
    public Date getDtFilterPolicyExpireFrom() {
        return dtFilterPolicyExpireFrom;
    }
    
    public void setDtFilterPolicyExpireFrom(Date dtFilterPolicyExpireFrom) {
        this.dtFilterPolicyExpireFrom = dtFilterPolicyExpireFrom;
    }
    
    public Date getDtFilterPolicyExpireTo() {
        return dtFilterPolicyExpireTo;
    }
    
    public void setDtFilterPolicyExpireTo(Date dtFilterPolicyExpireTo) {
        this.dtFilterPolicyExpireTo = dtFilterPolicyExpireTo;
    }
    
    public String getPrintingLOV() {
        return printingLOV;
    }
    
    public boolean isEnableCreateProduction() {
        return enableCreateProduction;
    }
    
    public void setEnableCreateProduction(boolean enableCreateProduction) {
        this.enableCreateProduction = enableCreateProduction;
    }
    
    public boolean isEnableCreateSPPA() {
        return enableCreateSPPA;
    }
    
    public boolean isEnableCreatePolis() {
        return enableCreatePolis;
    }
    
    public boolean isEnableCreateProposal() {
        return enableCreateProposal;
    }
    
    public boolean isEnableEdit() {
        return enableEdit;
    }
    
    public boolean isEnableSuperEdit() {
        return enableSuperEdit;
    }
    
    public boolean isEnableEndorsement() {
        return enableEndorsement;
    }
    
    public boolean isEnableClaim() {
        return enableClaim;
    }
    
    public boolean isEnableClaim2(){
        return enableEditClaim;
    }
    
     public boolean isEnableInputPaymentDate(){
        return enableInputPaymentDate;
    }
    
    public boolean isEnableClaimPLA() {
        return enableClaim;
    }
    
    public boolean isEnableClaimDLA() {
        return enableClaim;
    }
    
    public boolean isEnableView() {
        return enableView;
    }
    
    public boolean isEnablePrint() {
        return enablePrint;
    }
    
    public boolean isEnableRenewal() {
        return enableRenewal;
    }
    
    
    private ArrayList statusFilter;
    
    private ArrayList policyTypeGroupFilter;
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
    public String getPolicyID() {
        return policyID;
    }
    
    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }
    
    public DTOList getList() throws Exception {

        final String stMarketerID = UserManager.getInstance().getUser().getStMarketerID();

        if (list==null) {
            list=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
        
        final SQLAssembler sqa = new SQLAssembler();

        /*
        String Sql = "from " +
                " (select * " +
                " from " +
                " ins_policy_view3 a ";
        */

        String Sql = "from " +
                " (select a.*,b.description as policy_type_desc, "+
                " (select count(ins_pol_obj_id) "+
                "        from ins_split_pol_obj x where x.pol_id = a.pol_id "+
                ") as jumlah_objek, "+
                " (select count(ins_pol_obj_id)  "+
                "        from ins_split_pol_obj x where x.pol_id = a.pol_id and x.status = '1' "+
                ") as objek_terproses "+
                " from ins_split_policy a "+
                " left join ins_policy_types b on b.pol_type_id = a.pol_type_id ";
        
         if(getDbOverLimit()!=null){
         	Sql = Sql + 
         		" inner join ins_pol_obj c on c.pol_id = a.pol_id"+
         		" where c.insured_amount >= ?";
            sqa.addPar(dbOverLimit);
         }

        Sql = Sql + " order by pol_id desc) a";

        //Sql = Sql + ") a";

        sqa.addQuery(Sql); 	
        
        String select = "";
        if(getDbOverLimit()!=null) select = " distinct ";

        select = select + " *,"+
                " (select user_name"+
                " from s_users"+
                " where user_id = a.create_who limit 1) as create_name,"+
                " (select user_name"+
                " from s_users"+
                " where user_id = a.approved_who limit 1) as approved_name,"+
                " (select user_name"+
                " from s_users"+
                " where user_id = a.ri_approved_who limit 1) as approved_reins_name, "+
                " (select cek_bayar_klaim(a.pol_id)) as claim_payment_date2 ";
        
        sqa.addSelect(select);
        
        sqa.addFilter(list.getFilter());

        if (Tools.isYes(stOutstandingFlag) && !Tools.isYes(stShowDataReject)) {
            sqa.addClause("coalesce(a.active_flag,'') ='Y'");
        }
        
        if (Tools.isYes(stNotApprovedPolicy) || Tools.isYes(stShowClaimsNotApproved)) {
            sqa.addClause("coalesce(a.effective_flag,'N') <> 'Y'");
        }

        if (stBranch!=null) {
            sqa.addClause(
                    "a.cc_code = ?"
                    );
            sqa.addPar(stBranch);
        }
        
        if (statusFilter!=null) {
            if (Tools.isYes(stNotApprovedPolicy)){
                sqa.addClause(" status in (?,?,?)");
                sqa.addPar("POLICY");
                sqa.addPar("ENDORSE");
                sqa.addPar("RENEWAL");
            }else if (Tools.isYes(stShowClaimsNotApproved) || Tools.isYes(stShowClaimsOnly)){
                sqa.addClause(" status in (?,?)");
                sqa.addPar("CLAIM");
                sqa.addPar("CLAIM ENDORSE");
            }else{
                sqa.addClause(SQLUtil.exprIN("status",statusFilter));
                
                for (int i = 0; i < statusFilter.size(); i++) {
                    String s = (String) statusFilter.get(i);
                    
                    sqa.addPar(s);
                }
            }
        }
        
        if (Tools.isYes(stShowReinsNotApproved)) {
            sqa.addClause(" f_ri_finish is null");
        }
        
        if(stCreateWho!=null){
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateWho);
        }

        if (stRegion!=null) {
            sqa.addClause(
                    "a.region_id = ?"
                    );
            sqa.addPar(stRegion);
        }
        
        if (dtFilterPolicyDateFrom!=null) {
            sqa.addClause(
                    "a.policy_date >= ?"
                    );
            sqa.addPar(dtFilterPolicyDateFrom);
        }
        
        if (dtFilterPolicyDateTo!=null) {
            sqa.addClause(
                    "a.policy_date <= ?"
                    );
            sqa.addPar(dtFilterPolicyDateTo);
        }
        
        if (dtFilterPolicyExpireFrom!=null) {
            sqa.addClause(
                    "a.period_end >= ?"
                    );
            sqa.addPar(dtFilterPolicyExpireFrom);
        }
        
        if (dtFilterPolicyExpireTo!=null) {
            sqa.addClause(
                    "a.period_end <= ?"
                    );
            sqa.addPar(dtFilterPolicyExpireTo);
        }
        
        if (stPolicyNo!=null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar("%"+stPolicyNo+"%");
        }
        
        if(stPolicyTypeID!=null){
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }
        
        if (stPolicyGroup!=null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyGroup);
        }
        
        if (stPolicyStatus!=null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stPolicyStatus);
        }
        
        if (stPolicyCustomer!=null) {
            sqa.addClause("upper(a.cust_name) like ?");
            sqa.addPar("%"+ stPolicyCustomer.toUpperCase() +"%");
        }
        
        if (stPrincipal!=null) {
            sqa.addClause("upper(a.ref5) like ?");
            sqa.addPar("%"+ stPrincipal.toUpperCase() +"%");
        }
        
        if(stOverLimitPolicy!=null)
            if(Tools.isYes(stOverLimitPolicy)){
                if(isPolicy()){
                    sqa.addClause("a.f_uw_finish = ?");
                    sqa.addPar("N");
                }else if(isReas()){
                    sqa.addClause("a.f_uw_finish = ?");
                    sqa.addPar("N");
                }else if(isClaim()){
                    sqa.addClause(" a.f_claim_finish =?");
                    sqa.addPar("N");
                } 
            }

        if(stGatewayData!=null)
            if(Tools.isYes(stGatewayData)){
                sqa.addClause("coalesce(a.gateway_data_f,'N') = 'Y'");
            }
        
        if(Tools.isYes(stNotPrintedPolicy)){
            sqa.addClause(" document_print_flag is null");
        }

        if (Tools.isYes(stShowDataReject)) {
            sqa.addClause("(a.reject_flag = 'Y' or (a.active_flag = 'N' and a.effective_flag = 'N'))");
        }

        if(stMarketerID!=null){
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if(stKey!=null){
            sqa.addClause(stCriteria);

            if(stCriteria.toUpperCase().contains("LIKE"))
                sqa.addPar("%"+stKey.toUpperCase()+"%");
            else
                sqa.addPar(stKey.toUpperCase());
        }

        if(printingLOV.equalsIgnoreCase("PROPOSAL"))
            if (stBranch!=null)
                sqa.addOrder("pol_id desc");

        if(getStLimitFlag()!=null){
            int limit = Integer.parseInt(getStLimitFlag()) * 20;
            sqa.setLimit(limit);
        }
        
        list = sqa.getList(InsuranceSplitPolicyView.class);
        
        return list;
    }
    
    public void refresh() {
        
    }
    
    public void btnRefresh() {
        
    }
    
     public void btnRefreshVehicle() {
        
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public PolicySplitListForm() {
    }
    
    public void clickCreate() throws Exception {
                      
        final PolicySplitForm form = (PolicySplitForm)newForm("policy_split_edit",this);
        
        form.createNew();
        
        form.show();
    }
    
    public void clickCreatePolicyHistory() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.createNewPolicyHistory();
        
        form.setSavePolicyHistoryMode(true);
        
        form.setEnableNoPolicy(true);
        
        form.setCanCreatePolicyHistory(true);
        
        form.show();
    }
    
    public void clickCreateProd() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.createNewProductionOnly();
        
        form.show();
    }
    
    public void clickCreatePolis() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreatePolis(policyID);
        
        form.show();
    }
    
    public void clickCreateSPPA() throws Exception {

        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.editCreateSPPA(policyID);

        form.show();
        /*
        if (policyID==null){
            String form_name = "policy_edit";         
            if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
            if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
            final PolicyForm form = (PolicyForm)newForm(form_name,this);
            
            form.createNewSPPA();
            
            form.show();
        }else{
            String form_name = "policy_edit";         
            if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
            if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
            final PolicyForm form = (PolicyForm)newForm(form_name,this);

            form.editCreateSPPA(policyID);

            form.show();
        }*/
    }
    
    private void validatePolicyID() {
        if (policyID==null) throw new RuntimeException("Pilih dulu data yang ingin diproses");
    }
    
    public void clickCreateEndorse() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateEndorse(policyID);
        
        form.show();

    }
    
    /*
    public void clickCreateEndorseByDate() throws Exception {
        
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * from ins_policy where policy_date >= ? and policy_date <=? and pol_type_id in (55,56,57,58) and effective_flag = 'Y' and status in ('POLICY') and cc_code = '44'",
                new Object[]{getDtFilterPolicyDateFrom(), getDtFilterPolicyDateTo()},
                InsurancePolicyView.class
                );
        
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);
            
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
            
            policy.setDbPeriodRateBefore(new BigDecimal(100));
            policy.setStPeriodBaseBeforeID("2");
            
            final DTOList objects = policy.getObjects();
            
            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);
                
                InsurancePolicyObjDefaultView obj2 = (InsurancePolicyObjDefaultView) obj;
                
                obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                
                final DTOList detail = policy.getDetails();
                BigDecimal policyCost = null;
                BigDecimal stampFee = null;
                for (int m = 0; m < detail.size(); m++) {
                    InsurancePolicyItemsView items = (InsurancePolicyItemsView) detail.get(m);
                    
                    if(items.isPolicyCost()){
                        items.setDbAmount(BDUtil.negate(BDUtil.sub(items.getDbAmount(),obj2.getDbReference6())));
                        policyCost = items.getDbAmount();
                    }
                    
                    if(items.isStampFee()){
                        items.setDbAmount(BDUtil.negate(BDUtil.sub(items.getDbAmount(),obj2.getDbReference7())));
                        stampFee = items.getDbAmount();
                    }
                }
                
                final DTOList coverage = obj.getCoverage();
                
                for (int k = 0; k < coverage.size(); k++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(k);
                    
                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                    
                    cov.setStEntryPremiFlag("Y");
                    cov.setStAutoRateFlag("N");
                    cov.setStEntryRateFlag("N");
                    cov.setDbPremiNew(BDUtil.negate(BDUtil.sub(cov.getDbPremiNew(),obj2.getDbReference5())));
                    cov.setDbPremi(cov.getDbPremiNew());
                    
                    obj2.setDbReference5(BDUtil.negate(cov.getDbPremi()));
                    obj2.setDbReference6(BDUtil.negate(policyCost));
                    obj2.setDbReference7(BDUtil.negate(stampFee));
                    
                }
                
                final DTOList suminsureds = obj.getSuminsureds();
                
                for (int l = 0; l < suminsureds.size(); l++) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(l);
                    
                    tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());
                }
            }
            
            policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);
            
            policy.recalculate();
            policy.recalculateTreaty();
            policy.validateExcludedRiskTSI(false);
            policy.validateTreaty(false);
            
            policy.validateData();
            
            policy.validate(false);
            
            InsurancePolicyView pol = (InsurancePolicyView) getRemoteInsurance().saveEndorse(policy,policy.getStNextStatus(),false);
            
            pol.setStEffectiveFlag("Y");
            pol.setStPostedFlag("Y");
            pol.markUpdateO();
            getRemoteInsurance().saveEndorse(pol,pol.getStNextStatus(),true);
        }
        
    }*/
    
    /*
    public void clickFixEndorseByDate() throws Exception {
        
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * from ins_policy where pol_type_id in (55,56,57,58) and effective_flag = 'Y' and status in ('ENDORSE') and cc_code = ?",
                new Object[]{"44"},
                InsurancePolicyView.class
                );
        
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);
            
            final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();
            
            co.markNew();
            
            co.setStPolicyID(policy.getStPolicyID());
            
            co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
            
            co.setDbSharePct(BDUtil.hundred);
            
            co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);
            
            co.setStHoldingCompanyFlag("Y");
            
            co.setStAutoPremiFlag("Y");
            
            co.setDbAmount(policy.getDbInsuredAmountEndorse());
            
            co.setDbPremiAmount(policy.getDbPremiTotal());
            
            policy.getCoins().add(co);
            
            policy.markUpdate();
            
            policy.setDtEndorseDate(new Date());
            
            policy.setDtPolicyDate(new Date());
            
            InsurancePolicyView pol = (InsurancePolicyView) getRemoteInsurance().saveEndorse(policy,policy.getStNextStatus(),false);
            
        }
        
    }*/
    
    public void clickCreateEndorseRI() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateEndorseRI(policyID);
        
        form.show();
    }
    
    public void clickCreateEndorseIntern() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateEndorseIntern(policyID);
        
        form.show();
    }
    
    public void clickCreateClaim() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateClaim(policyID);
        
        form.show();
    }
    
    
    public void clickCreateClaimEndorse() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateClaimEndorse(policyID);
        
        form.show();
    }
    
    
    public void clickEdit() throws Exception {
        validatePolicyID();

        final PolicySplitForm form = (PolicySplitForm)newForm("policy_split_edit",this);
        
        final InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) DTOPool.getInstance().getDTO(InsuranceSplitPolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");
        
        form.setReasMode(reas);
        
        form.setApprovalMode(false);

        form.edit(policyID);
        
        
        form.show();
    }
    
    public void clickEditClaim() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");
        
        form.setReasMode(reas);
        
        form.setApprovalMode(false);
        
        form.editHistory(policyID);
        
        form.setEditClaimNoteMode(true);
        
        form.show();
    }
    
    public void clickEditInputPaymentDate() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");
        
        form.setReasMode(reas);
        
        form.setApprovalMode(false);
        
        form.setInputPaymentDateMode(true);
        
        form.editPayment(policyID);

        form.setReadOnly(true);
        
        form.show();
    }
    
    public void clickEditViaReverse() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");
        
        form.setReasMode(reas);
        
        form.setApprovalMode(false);
        
        form.setReverseMode(true);
        
        form.edit(policyID);
        
        form.show();
    }
    
    public void clickReApproval() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        form.superEdit(policyID);
        
        form.setReApprovedMode(true);
        
        form.show();
        
    }    
    
    public void clickReApprovalByDate() throws Exception {
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where pol_id in ( "+
                " select pol_id "+
                " from proses_teknik) "+
                " and coalesce(admin_notes,'') <> 'APPROVAL_ARI'  order by pol_id",
                InsurancePolicyView.class
                );
        
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);
            
            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());
            
            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);
            /*
            final DTOList objects = polis.getObjects();
            
            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
                
                obj.markUpdate();
                
                obj.getTreaties().markAllUpdate();
                
                final DTOList treatyDetails = obj.getTreatyDetails();
                treatyDetails.markAllUpdate();
                
                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);
                    
                    final DTOList share = trdi.getShares();
                    trdi.getShares().markAllUpdate();
                }
                obj.getTreaties().deleteAll();
                polis.recalculate();
                polis.recalculateTreatyKBGEndorse();
            }
            */
            pol.markUpdate();
            
            polis.recalculate();
            
            pol.setStPostedFlag("Y");
            pol.setStEffectiveFlag("Y");
            getRemoteInsurance().saveAfterReverse(polis, polis.getStNextStatus(), false);

            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'APPROVAL_ARI' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            
        }
        
    }

    public void clickReverseUW() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        form.superEdit(policyID);
        form.setReadOnly(true);

        form.cekClosingStatus("REVERSE");

        if (!form.getPolicy().isEffective()) throw new Exception("Data tidak bisa di reverse karena belum disetujui");
        
        form.setReverseMode(true);

        //update ins_policy set posted_flag = null,effective_flag='N',approved_who=null,approved_date=null,
        //password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?

        form.getPolicy().setStPostedFlag(null);
        form.getPolicy().setStEffectiveFlag("N");
        form.getPolicy().setStApprovedWho(null);
        form.getPolicy().setDtApprovedDate(null);
        form.getPolicy().setStPassword(null);
        form.getPolicy().setStClientIP(null);
        form.getPolicy().setStReference3(null);
        form.getPolicy().setStReference4(null);
        form.getPolicy().setStReadyToApproveFlag(null);
        form.getPolicy().setStAdminNotes(null);

        form.show();
    }
    
    public void clickCreateRenewal() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateRenewal(policyID);

        form.show();
    }
    
    public void clickCreateClaimPLA() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateClaimPLA(policyID);
        
        form.show();
    }
    
    public void clickCreateClaimDLA() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateClaimDLA(policyID);
        
        form.show();
    }
    
    public void clickSuperEdit() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.setReasMode(reas);
        
        form.superEdit(policyID);
        
        form.show();
    }
    
    public void clickView() throws Exception {

        final PolicySplitForm form = (PolicySplitForm)newForm("policy_split_edit",this);
        
        form.setReasMode(reas);
        
        form.view(policyID);
        
        form.show();
    }
    
    public String getStPrintForm() {
        return stPrintForm;
    }
    
    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }
    
    public String getStLang() {
        return stLang;
    }
    
    public void setStLang(String stLang) {
        this.stLang = stLang;
    }
    
    public void proposal() {
        setTitle("UPLOAD SPLIT POLIS");

        printingLOV = "PROPOSAL";
        
        canEdit = hasResource("POL_PROP_EDIT");
        canCreate = hasResource("POL_PROP_CREATE");
        canApprove = hasResource("POL_PROP_APRV");
        enablePrint = hasResource("POL_PROP_PRINT");
        enablePrintPreSign = hasResource("POL_UWRIT_PRINT_PRESIGN");
        enablePrintDigitized = hasResource("POL_UWRIT_PRINT_DIGITIZED");
        canApproveAnalisaResiko = hasResource("POL_PROP_APRV_RISK_ANALYSIS");
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.DRAFT,
            FinCodec.PolicyStatus.SPPA,
        });
        
        enableCreateSPPA=canCreate;
        enableCreateProposal=canCreate;
        
        if (canEdit) {
            editFilter.add(FinCodec.PolicyStatus.SPPA);
            editFilter.add(FinCodec.PolicyStatus.DRAFT);
        }
        
        canNavigateBranch = hasResource("POL_PROP_NAVBR");
        canNavigateRegion = hasResource("POL_PROP_NAVREG");
        enableEdit = canEdit;
        
    }
    
    public boolean isCanApprove() {
        return canApprove;
    }
    
    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }
    
    public boolean isCanEdit() {
        return canEdit;
    }
    
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
    
    public boolean isCanCreate() {
        return canCreate;
    }
    
    public boolean isCanCreatePolicyHistory() {
        return canCreatePolicyHistory;
    }
    
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }
    
    public boolean isListEffective() {
        return listEffective;
    }
    
    public void setListEffective(boolean listEffective) {
        this.listEffective = listEffective;
    }
    
    private boolean hasResource(String rsrc) {
        return SessionManager.getInstance().getSession().hasResource(rsrc);
    }
    
    private ArrayList array2list(String[] strings) {
        
        final ArrayList l = new ArrayList();
        
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            
            l.add(string);
        }
        
        return l;
    }
    
    public void underwrit() {
        setTitle("UNDERWRITING");
        printingLOV = "UWRIT";
        
        canEdit = hasResource("POL_UWRIT_EDIT");
        canCreate = hasResource("POL_UWRIT_CREATE");
        canApprove = hasResource("POL_UWRIT_APRV");
        enablePrint = hasResource("POL_UWRIT_PRINT");
        canCreatePolicyHistory = hasResource("POL_UWRIT_CREATE_HISTORY");
        approveUW = hasResource("POL_UWRIT_APR_UW");
        enableInputPaymentDate = hasResource("POL_UWRIT_PAYMENT_DATE");
        //canInputTemporaryPolicy = hasResource("CAN_INPUT_TEMP_POL");
        enablePrintPreSign = hasResource("POL_UWRIT_PRINT_PRESIGN");
        enablePrintDigitized = hasResource("POL_UWRIT_PRINT_DIGITIZED");
        enablePrintDigitalPolicy = hasResource("POL_UWRIT_PRINT_DIGITAL_POLICY");

        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.SPPA,
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.RENEWAL,
            FinCodec.PolicyStatus.HISTORY,
        });
        
        //enableCreateSPPA=canCreate;
        enableCreatePolis=canCreate;
        enableEndorsement=canCreate;
        enableRenewal=canCreate;
        
        editFilter.add(FinCodec.PolicyStatus.SPPA);
        editFilter.add(FinCodec.PolicyStatus.POLICY);
        editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        editFilter.add(FinCodec.PolicyStatus.RENEWAL);
        editFilter.add(FinCodec.PolicyStatus.HISTORY);
        
        canNavigateBranch = hasResource("POL_UWRIT_NAVBR");
        canNavigateRegion = hasResource("POL_UWRIT_NAVREG");
        
        enableEdit = canEdit;
        policy = true;

//        if(SessionManager.getInstance().getSession().getStBranch()!=null)
//            if(SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("15"))
//                enableView = false;
    }
    
    public void claim() {
        printingLOV = "CLAIM";
        
        setTitle("CLAIM");
        
        canEdit = hasResource("POL_CLAIM_EDIT");
        canCreate = hasResource("POL_CLAIM_CREATE");
        canApprove = hasResource("POL_CLAIM_APRV");
        enablePrint = hasResource("POL_CLAIM_PRINT");
        canNavigateBranch = hasResource("POL_CLAIM_NAVBR");
        enableEditClaim = hasResource("POL_CLAIM_EDIT2");
        canNavigateRegion = hasResource("POL_CLAIM_NAVREG");
        canCreateClaimInward = hasResource("POL_CLAIM_INWARD_CREATE");
        enablePrintPreSign = true;
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.CLAIM,
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.RENEWAL,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.ENDORSECLAIM,
            FinCodec.PolicyStatus.HISTORY,
            FinCodec.PolicyStatus.INWARD,
            FinCodec.PolicyStatus.CLAIMINWARD,
        });
        enableClaim=true;
        enableEdit = canEdit;
        listEffective = true;
        claim = true;
        
        editFilter.add(FinCodec.PolicyStatus.CLAIM);
        editFilter.add(FinCodec.PolicyStatus.ENDORSECLAIM);
        editFilter.add(FinCodec.PolicyStatus.HISTORY);
        editFilter.add(FinCodec.PolicyStatus.CLAIMINWARD);

//        if(SessionManager.getInstance().getSession().getStBranch()!=null)
//            if(SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("15"))
//                enableView = false;
        
    }
    
    public void renewal() {
        printingLOV = "RENEWAL";
        
        setTitle("RENEWAL NOTICES");
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
        });
    }
    
    public void reas() {
        setTitle("REINSURANCE");
        
        printingLOV = "REAS";
        
        canEdit = hasResource("POL_RI_EDIT");
        canCreate = hasResource("POL_RI_CREATE");
        canApprove = hasResource("POL_RI_APRV");
        enablePrint = hasResource("POL_RI_PRINT");
        canNavigateBranch = hasResource("POL_RI_NAVBR");
        canNavigateRegion = hasResource("POL_RI_NAVREG");
        canReverseReins = hasResource("REVERSE_RI");
        canApproveReins = hasResource("APPROVE_RI");
        canApproveReinsOnly = hasResource("APPROVE_RI_ONLY");
        enablePrintPreSign = true;
        
        statusFilter = array2list(new String [] {
            //FinCodec.PolicyStatus.SPPA,
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.RENEWAL,
            FinCodec.PolicyStatus.ENDORSERI,
            FinCodec.PolicyStatus.HISTORY,
            FinCodec.PolicyStatus.INWARD,
        });
        
        reas = true;
        enableEdit = canEdit;
        
        //editFilter.add(FinCodec.PolicyStatus.SPPA);
        editFilter.add(FinCodec.PolicyStatus.POLICY);
        editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        editFilter.add(FinCodec.PolicyStatus.RENEWAL);
        editFilter.add(FinCodec.PolicyStatus.ENDORSERI);
        editFilter.add(FinCodec.PolicyStatus.HISTORY);
        editFilter.add(FinCodec.PolicyStatus.INWARD);
    }
    
    public void clickApproval() throws Exception {
        validatePolicyID();
                   
        final PolicySplitForm form = (PolicySplitForm)newForm("policy_split_edit",this);
        
        form.approval(policyID);
        
        form.setApprovalMode(true);
        
        form.show();
    }
    
    public void clickApprovalViaReverse() throws Exception {
        final PolicyForm form = (PolicyForm)newForm("policy_edit", this);
        
        form.approval(policyID);
        
        form.setApprovedViaReverseMode(true);
        
        form.setReverseMode(true);
        
        form.show();
    }
    
    public void clickApprovalExcludeReas() throws Exception {
        final PolicyForm form = (PolicyForm)newForm("policy_edit", this);
        
        form.approval(policyID);
        
        form.setExcludeReasMode(true);
        
        form.show();
    }
    
    public void clickApprovalReins() throws Exception {
        final PolicyForm form = (PolicyForm)newForm("policy_edit_reinsurance", this);

        //if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        
        form.approvalReins(policyID);
        
        form.setApprovalMode(false);
        
        form.setApprovedReasMode(true);

        form.setReasDataOnlyApprovedMode(true);
        
        form.getPolicy().setStReinsuranceApprovedWho(UserManager.getInstance().getUser().getStUserID());
        
        form.show();
    }
    
    public String getStFontSize() {
        return stFontSize;
    }
    
    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }
    
    public String getStAttached() {
        return stAttached;
    }
    
    public void setStAttached(String stAttached) {
        this.stAttached = stAttached;
    }
    
    public DTOList getPakreasilist() throws Exception {

        if (pakreasilist == null) {
            pakreasilist = new DTOList();
            final Filter filter = new Filter();

            pakreasilist.setFilter(filter.activate());
        }

        final SQLAssembler sqa = new SQLAssembler();

        //System.out.println(stTglTahun + " <----------------- stTanggalTahun");

        if (stPolicy == null && stName == null && dtBirthDate == null && dtLiquidityFrom == null && dtEndOfCreditFrom == null
                && stTglTahun == null && dtPolicyDateFrom == null && dtPolicyDateTo == null && stNoRekeningPinjaman==null && stNoPerjanjianKredit==null) {
            sqa.addQuery(
                    "         from "
                    + "            aba_kreasi a");

            sqa.addSelect("a.*");
            sqa.setLimit(0);
        } else if (stPolicy != null || stName != null || dtBirthDate != null || dtLiquidityFrom != null || dtEndOfCreditFrom != null
                || stTglTahun != null || dtPolicyDateFrom != null || dtPolicyDateTo != null || stNoRekeningPinjaman!=null || stNoPerjanjianKredit!=null) {
            if (stKreasi.equalsIgnoreCase("1")) {

                String sql = "";
                if (stTglTahun != null) {
                    if (stTglTahun.equalsIgnoreCase("2")) {
                        sql = ", EXTRACT(YEAR FROM a.policy_date) - EXTRACT(YEAR FROM b.refd1) as jmltaon";
                    }
                } else if (stJangkaWaktu != null) {
                    sql = ", EXTRACT(YEAR FROM b.refd3) - EXTRACT(YEAR FROM b.refd2) as jmltaon";
                }

                sqa.addSelect(" a.pol_no, b.ref1 as nama, b.ref2 as umur, b.refd1 as tgl_lhr, "
                        + " b.refd2 as tgl_cair,b.refd3 as tgl_akhir, "
                        + " getpremi(a.pol_type_id = 21,getpremi(a.status = 'ENDORSE',b.insured_amount,b.refn4),b.insured_amount) as insured_amount,"
                        + " getpremi(a.pol_type_id = 21,b.refn6,b.premi_total) as premi,"
                        + " b.refn5 as rate_premi, a.status,b.order_no as norut,b.ref3 as no_identitas,"
                        + " b.ref4 as no_pk,b.ref16 as no_rek_pinjaman,a.cust_name as entity_name, a.cc_code,a.pol_id " + sql + "");

                sqa.addQuery(" from ins_policy a "
                        + " inner join ins_pol_obj b on a.pol_id = b.pol_id ");

                sqa.addClause("a.pol_type_id in (21,59,80)");
                sqa.addClause("a.active_flag='Y'");
                sqa.addClause("a.effective_flag='Y'");

                sqa.addFilter(pakreasilist.getFilter());

                if (stEntityID != null) {
                    sqa.addClause("a.entity_id = ?");
                    sqa.addPar(stEntityID);
                }

                if (dtPolicyDateFrom != null) {
                    sqa.addClause("date_trunc('day',a.policy_date) >= ?");
                    sqa.addPar(dtPolicyDateFrom);
                }

                if (dtPolicyDateTo != null) {
                    sqa.addClause("date_trunc('day',a.policy_date) <= ?");
                    sqa.addPar(dtPolicyDateTo);
                }

                if (stPolicy != null) {
                    sqa.addClause("a.pol_no like ?");
                    sqa.addPar("%" + stPolicy + "%");
                }

                if (stBranch != null) {
                    sqa.addClause(
                            "a.cc_code = ?");
                    sqa.addPar(stBranch);
                }

                if (stName != null) {
                    sqa.addClause(
                            "to_tsvector('english', b.ref1) @@ to_tsquery('english', ?) ");

                    sqa.addPar(stName.replaceAll(" ", "&").replaceAll("&&", "&"));
                }

                if (stPolicyLevel != null) {
                    sqa.addClause(
                            "a.status = ?");
                    sqa.addPar(stPolicyLevel);
                }

                if (dtBirthDate != null) {
                    sqa.addClause(
                            "b.refd1 = ?");
                    sqa.addPar(dtBirthDate);
                }

                if (stAge != null) {
                    sqa.addClause("b.ref2 >= ?");
                    sqa.addPar(stAge);
                }

                if (dtLiquidityFrom != null) {
                    sqa.addClause("date_trunc('day',b.refd2) >= ?");
                    sqa.addPar(dtLiquidityFrom);
                }

                if (dtLiquidityTo != null) {
                    sqa.addClause("date_trunc('day',b.refd2) <= ?");
                    sqa.addPar(dtLiquidityTo);
                }

                if (dtEndOfCreditFrom != null) {
                    sqa.addClause("date_trunc('day',b.refd3) >= ?");
                    sqa.addPar(dtEndOfCreditFrom);
                }

                if (dtEndOfCreditTo != null) {
                    sqa.addClause("date_trunc('day',b.refd3) <= ?");
                    sqa.addPar(dtEndOfCreditTo);
                }

                if (stNoIdentitas != null) {
                    sqa.addClause("upper(b.ref3) = ?");
                    sqa.addPar(stNoIdentitas.toUpperCase());
                }

                if (stNoPerjanjianKredit != null) {
                    sqa.addClause("upper(b.ref4) = ?");
                    sqa.addPar(stNoPerjanjianKredit.toUpperCase());
                }

                if (stNoRekeningPinjaman != null) {
                    sqa.addClause("upper(b.ref16) = ?");
                    sqa.addPar(stNoRekeningPinjaman.toUpperCase());
                }

                if (stKategoriDeb != null) {
                    sqa.addClause("b.ref7 = ?");
                    sqa.addPar(stKategoriDeb);
                }

                if (stPekerjaanDeb != null) {
                    sqa.addClause("b.ref4 = ?");
                    sqa.addPar(stPekerjaanDeb);
                }

                // tanggal taun
                if (stTglTahun != null) {
                    if (stTglTahun.equalsIgnoreCase("1")) {
                        sqa.addClause(" b.refd1 is null ");
                    } else if (stTglTahun.equalsIgnoreCase("2")) {
                        sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon > 80";
                        return pakreasilist = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyView.class);
                    } else if (stTglTahun.equalsIgnoreCase("3")) {
                        sqa.addClause(" b.refd1 =  b.refd2");
                    } else if (stTglTahun.equalsIgnoreCase("4")) {
                        sqa.addClause(" b.refd2 <> b.refd3  ");
                    }
                }

                // usia
                if (stUsia != null) {
                    sqa.addClause("b.ref2 >= ?");
                    sqa.addPar(stUsia);
                }

                // jangka waktu
                if (stJangkaWaktu != null) {
                    if (stKreasiKredit.equalsIgnoreCase("1")) {
                        sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon > " + stJangkaWaktu + "";
                        return pakreasilist = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyView.class);
                    } else if (stKreasiKredit.equalsIgnoreCase("2")) {
                        sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon < " + stJangkaWaktu + "";
                        return pakreasilist = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyView.class);
                    }

                }


                sqa.setLimit(700);
                sqa.addOrder(" a.pol_no desc");

            } else if (stKreasi.equalsIgnoreCase("2")) {
                sqa.addQuery(
                        "         from "
                        + "            aba_kreasi a");

                sqa.addSelect("a.*");

                sqa.addFilter(pakreasilist.getFilter());

                if (stPolicy != null) {
                    sqa.addClause("a.pol_no like ?");
                    sqa.addPar("%" + stPolicy + "%");
                }

                if (stBranch != null) {
                    sqa.addClause(
                            "a.cc_code = ?");
                    sqa.addPar(stBranch);
                }

                if (stName != null) {
                    sqa.addClause(
                            "to_tsvector('english', a.nama) @@ to_tsquery('english', ?) ");

                    sqa.addPar(stName.replaceAll(" ", "&").replaceAll("&&", "&"));
                }

                if (stPolicyLevel != null) {
                    sqa.addClause(
                            "a.status = ?");
                    sqa.addPar(stPolicyLevel);
                }

                if (dtBirthDate != null) {
                    sqa.addClause(
                            "a.tgl_lhr = ?");
                    sqa.addPar(dtBirthDate);
                }

                if (dtLiquidityFrom != null) {
                    sqa.addClause(
                            "a.tgl_cair = ?");
                    sqa.addPar(dtLiquidityFrom);
                }

                if (dtEndOfCreditFrom != null) {
                    sqa.addClause(
                            "a.tgl_akhir = ?");
                    sqa.addPar(dtEndOfCreditFrom);
                }

                String sql = "";


                // tanggal taun
                if (stTglTahun != null) {
                    // tanggal lahir kosong
                    if (stTglTahun.equalsIgnoreCase("1")) {
                        sqa.addClause(" a.tgl_lhr is null ");
                        // tahun pada tanggal tidak valid
                    } else if (stTglTahun.equalsIgnoreCase("2")) {
                        sqa.addClause(" umur > '75' and tgl_lhr not null ");
                        // tanggal lahir sama dengan tanggal awal asuransi
                    } else if (stTglTahun.equalsIgnoreCase("3")) {
                        sqa.addClause(" a.tgl_lhr = a.tgl_cair");
                        // tanggal awal dan tanggal akhir tidak sama
                    } else if (stTglTahun.equalsIgnoreCase("4")) {
                        sqa.addClause(" a.tgl_cair <> a.tgl_akhir ");
                    }
                }

                // usia
                if (stUsia != null) {
                    sqa.addClause("a.umur >= ?");
                    sqa.addPar(stUsia);
                }

                // jangka waktu
                if (stJangkaWaktu != null) {
                    // jangka waktu tidak lebih dari 15 taon
                    if (stKreasiKredit.equalsIgnoreCase("1")) {
                        sqa.addClause(" EXTRACT(YEAR FROM a.tgl_cair)  - EXTRACT(YEAR FROM a.tgl_akhir) > " + stJangkaWaktu + "");
                        // jangka waktu awal tidak boleh kurang dari 3 taon
                    } else if (stKreasiKredit.equalsIgnoreCase("2")) {
                        sqa.addClause(" EXTRACT(YEAR FROM a.tgl_cair)  - EXTRACT(YEAR FROM a.tgl_akhir) < " + stJangkaWaktu + "");
                    }

                }

                sqa.setLimit(700);
                sqa.addOrder(" pol_no desc");
            }

        }

        pakreasilist = sqa.getList(InsuranceKreasiView.class);

        return pakreasilist;
    }
    
    public void setPakreasilist(DTOList pakreasilist) {
        this.pakreasilist = pakreasilist;
    }
    
    public String getStName() {
        return stName;
    }
    
    public void setStName(String stName) {
        this.stName = stName;
    }
    
    public Date getDtBirthDate() {
        return dtBirthDate;
    }
    
    public void setDtBirthDate(Date dtBirthDate) {
        this.dtBirthDate = dtBirthDate;
    }
    
    public String getStPolicyLevel() {
        return stPolicyLevel;
    }
    
    public void setStPolicyLevel(String stPolicyLevel) {
        this.stPolicyLevel = stPolicyLevel;
    }
    
    public Date getDtLiquidity() {
        return dtLiquidity;
    }
    
    public void setDtliquidity(Date dtLiquidity) {
        this.dtLiquidity = dtLiquidity;
    }
    
    public Date getDtEndOfCredit() {
        return dtEndOfCredit;
    }
    
    public void setDtEndOfCredit(Date dtEndOfCredit) {
        this.dtEndOfCredit = dtEndOfCredit;
    }
    
    public void showFilter(){
        setShowFilter(false);
    }
    
    public void hideFilter(){
        setShowFilter(true);
    }
    
    public boolean isShowFilter(){
        return isShowFilter;
    }
    
    public void setShowFilter(boolean isShowFilter){
        this.isShowFilter = isShowFilter;
    }
    
    public boolean getIsShowFilter(){
        return isShowFilter;
    }
    
    public String getStPolicy() {
        return stPolicy;
    }
    
    public void setStPolicy(String stPolicy) {
        this.stPolicy = stPolicy;
    }
    
    public boolean isApproveUW() {
        return approveUW;
    }

    public boolean isIsBondingDivision() {
        return isBondingDivision;
    }

    public void setIsBondingDivision(boolean isBondingDivision) {
        this.isBondingDivision = isBondingDivision;
    }

    public String getStShowClaimsNotApproved() {
        return stShowClaimsNotApproved;
    }
    
    public void setStShowClaimsNotApproved(String stShowClaimsNotApproved) {
        this.stShowClaimsNotApproved = stShowClaimsNotApproved;
    }
    
    public String getStShowClaimsOnly() {
        return stShowClaimsOnly;
    }
    
    public void setStShowClaimsOnly(String stShowClaimsOnly) {
        this.stShowClaimsOnly = stShowClaimsOnly;
    }
    
    public BigDecimal getDbOverLimit() {
        return dbOverLimit;
    }
    
    public void setDbOverLimit(BigDecimal dbOverLimit) {
        this.dbOverLimit = dbOverLimit;
    }

    public String getStPoliceNo() {
        return stPoliceNo;
    }

    public void setStPoliceNo(String stPoliceNo) {
        this.stPoliceNo = stPoliceNo;
    }

    public String getStYearBuilt() {
        return stYearBuilt;
    }

    public void setStYearBuilt(String stYearBuilt) {
        this.stYearBuilt = stYearBuilt;
    }

    public String getStChassisNo() {
        return stChassisNo;
    }

    public void setStChassisNo(String stChassisNo) {
        this.stChassisNo = stChassisNo;
    }

    public String getStMachineNo() {
        return stMachineNo;
    }

    public void setStMachineNo(String stMachineNo) {
        this.stMachineNo = stMachineNo;
    }

    public String getStSeat() {
        return stSeat;
    }

    public void setStSeat(String stSeat) {
        this.stSeat = stSeat;
    }

    public String getStUsage() {
        return stUsage;
    }

    public void setStUsage(String stUsage) {
        this.stUsage = stUsage;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
    }

    public String getStTypeOfVehicle() {
        return stTypeOfVehicle;
    }

    public void setStTypeOfVehicle(String stTypeOfVehicle) {
        this.stTypeOfVehicle = stTypeOfVehicle;
    }

    public String getStNama() {
        return stNama;
    }

    public void setStNama(String stNama) {
        this.stNama = stNama;
    }
    
    private DTOList vehicle;
    
    public DTOList getVehicleList() throws Exception {

        if (vehicle == null) {
            vehicle = new DTOList();
            final Filter filter = new Filter();

            vehicle.setFilter(filter.activate());
        }

        final SQLAssembler sqa = new SQLAssembler();

        if (stPolicy == null && stPoliceNo == null && stYearBuilt == null && stChassisNo == null && stMachineNo == null
                && stSeat == null && stUsage == null && stTypeOfVehicle == null && stName == null) {
            sqa.addQuery(
                    "         from "
                    + "            ins_pol_obj a");

            sqa.addSelect("a.*");
            sqa.setLimit(0);
        } else if (stPolicy != null || stPoliceNo != null || stYearBuilt != null || stChassisNo != null || stMachineNo != null
                && stSeat != null || stUsage != null || stTypeOfVehicle != null || stName != null) {
            sqa.addQuery(
                    "         from "
                    + "         ins_policy a    "
                    + "inner join ins_pol_obj b on a.pol_id = b.pol_id ");

            sqa.addSelect("*");

            sqa.addFilter(vehicle.getFilter());

            sqa.addClause("a.pol_type_id = 3");

            if (stPolicy != null) {
                sqa.addClause("a.pol_no = ?");
                sqa.addPar(stPolicy);
            }

            if (stPolicyLevel != null) {
//                sqa.addClause(
//                        "a.status = ?");
//                sqa.addPar(stPolicyLevel);
            }

            if (stBranch != null) {
                sqa.addClause(
                        "a.cc_code = ?");
                sqa.addPar(stBranch);
            }

            if (stPolicyLevel != null) {
//                sqa.addClause(
//                        "a.status = ?");
//                sqa.addPar(stPolicyLevel);
            }

            if (stPoliceNo != null) {
                sqa.addClause(
                        " btrim(upper(b.ref1)) = ?");
                sqa.addPar(stPoliceNo.toUpperCase());
            }

            if (stYearBuilt != null) {
                sqa.addClause(
                        " b.ref3 = ?");
                sqa.addPar(stYearBuilt.toUpperCase());
            }

            if (stChassisNo != null) {
                sqa.addClause(
                        " b.ref4 like ?");
                sqa.addPar("%" + stChassisNo.toUpperCase() + "%");
            }

            if (stMachineNo != null) {
                sqa.addClause(
                        " b.ref5 = ?");
                sqa.addPar(stMachineNo.toUpperCase());
            }

            if (stUsage != null) {
                sqa.addClause(
                        " b.ref7 like ?");
                sqa.addPar("%" + stUsage.toUpperCase() + "%");
            }

            if (stTypeOfVehicle != null) {
                sqa.addClause(
                        " b.ref2 like ?");
                sqa.addPar("%" + stTypeOfVehicle.toUpperCase() + "%");
            }

            if (stName != null) {
                sqa.addClause(
                        " b.ref9 like ?");
                sqa.addPar("%" + stName.toUpperCase() + "%");
            }

            sqa.addOrder(" a.pol_no desc");
            sqa.setLimit(10);
        }

        vehicle = sqa.getList(InsurancePolicyObjDefaultView.class);

        return vehicle;
    }
    
    public void setVehicleList(DTOList vehicle) {
        this.vehicle = vehicle;
    }
    
    public void clickCreateProposalFromPolicy() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateProposalFromPolicy(policyID);

        form.show();
    }
    
     public BigDecimal getTransactionLimitByCCCode(String cccode, String policytype) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "    select "+
                    " max(c.refn1)  "+
                    " from  "+
                    " ff_table c  "+
                    " where c.fft_group_id='CAPA' and c.ref2='ACCEPT' " +
                    " and c.ref7= ? and c.ref6 = ? limit 1");

            S.setParam(1, policytype);
            S.setParam(2, cccode);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getBigDecimal(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }
     
     public BigDecimal getTransactionLimitByCCCode2(String cccode, String policytype) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "    select "+
                    " max(c.refn1)  "+
                    " from  "+
                    " ff_table c  "+
                    " where c.fft_group_id='CAPA' and c.ref2='ACCEPT' " +
                    " and c.ref3= ? and c.ref6 = ? limit 1");

            S.setParam(1, policytype);
            S.setParam(2, cccode);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getBigDecimal(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }

     
     public void onChangePolicyTypeGroup() {
        
    }
     
     private String stPolicyTypeID;
     
     public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }
    
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }
    
    private String stOverLimitPolicy;
    
    public String getStOverLimitPolicy() {
        return stOverLimitPolicy;
    }
    
    public void setStOverLimitPolicy(String stOverLimitPolicy) {
        this.stOverLimitPolicy = stOverLimitPolicy;
    }
    
    public boolean IsCanReverse() {
        return canReverse;
    }

    public void setCanReverse(boolean canReverse) {
        this.canReverse = canReverse;
    }

    public String getStKreasi() {
        return stKreasi;
    }

    public void setStKreasi(String stKreasi) {
        this.stKreasi = stKreasi;
    }
    
    public void underwritmonitoring() {
        setTitle("UNDERWRITING MONITORING (Auto Refresh Every 5 Minutes)");
        printingLOV = "UWRIT";
        setAutoRefresh(true);
        
        canEdit = hasResource("POL_UWRIT_EDIT");
        canCreate = hasResource("POL_UWRIT_CREATE");
        canApprove = hasResource("POL_UWRIT_APRV");
        enablePrint = hasResource("POL_UWRIT_PRINT");
        canCreatePolicyHistory = hasResource("POL_UWRIT_CREATE_HISTORY");
        approveUW = hasResource("POL_UWRIT_APR_UW");
        enableInputPaymentDate = hasResource("POL_UWRIT_PAYMENT_DATE");
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.RENEWAL,
        });
        
        //enableCreateSPPA=canCreate;
        enableCreatePolis=canCreate;
        enableEndorsement=canCreate;
        enableRenewal=canCreate;
        
        editFilter.add(FinCodec.PolicyStatus.POLICY);
        editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        editFilter.add(FinCodec.PolicyStatus.RENEWAL);
        
        canNavigateBranch = hasResource("POL_UWRIT_NAVBR");
        
        enableEdit = canEdit;
        policy = true;
    }
    
    public DTOList getListUWMonitoring() throws Exception {
      /*DTOList list = ListUtil.getDTOListFromQuery("select * from ins_policy where active_flag = 'Y' order by pol_no",
                    InsurancePolicyView.class,filter);
       
      list.setFilter(filter);*/
        
        if (list==null) {
            list=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
        String Sql = "         from " +
                "            ins_policy a" +
                "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id"; 
         
         Sql = Sql + "  left join gl_cost_center c on a.cc_code = c.cc_code ";       

        sqa.addQuery(Sql); 	
        
        String select = "";
        
        select = select + " substr(pol_no,0,5)||'-'||substr(pol_no,5,4)||'-'||substr(pol_no,9,4)||'-'||substr(pol_no,13,4)||'-'||substr(pol_no,17,2) as pol_no,"+
                    " a.pol_id ,a.effective_flag,a.status,a.insured_amount,a.insured_amount_e,a.premi_total,a.policy_date,c.description as cc_code,short_desc as description";
        
        sqa.addSelect(select);
        
        sqa.addFilter(list.getFilter());
       
        if (Tools.isYes(stOutstandingFlag)) {
            sqa.addClause("((a.effective_flag='N' and a.active_flag='Y') or (a.active_flag='Y'))");
        }
        
        if (Tools.isYes(stNotApprovedPolicy) || Tools.isYes(stShowClaimsNotApproved)) {
            sqa.addClause("(a.effective_flag='N' or a.effective_flag is null)");
        }
        
        if (statusFilter!=null) {
            if (Tools.isYes(stNotApprovedPolicy)){
                sqa.addClause(" status in (?,?,?)");
                sqa.addPar("POLICY");
                sqa.addPar("ENDORSE");
                sqa.addPar("RENEWAL");
            }if (Tools.isYes(stShowClaimsNotApproved) || Tools.isYes(stShowClaimsOnly)){
                sqa.addClause(" status in (?)");
                sqa.addPar("CLAIM");
            }else{
                sqa.addClause(SQLUtil.exprIN("status",statusFilter));
                
                for (int i = 0; i < statusFilter.size(); i++) {
                    String s = (String) statusFilter.get(i);
                    
                    sqa.addPar(s);
                }
            }
        }
        
        sqa.addClause("f_uw_finish = ?");
        sqa.addPar("N");
        
        sqa.addOrder("policy_date asc");
        
        list = sqa.getList(InsurancePolicyView.class);
        
        return list;
    }
    
    public void reinsurancemonitoring() {
        setTitle("REINSURANCE MONITORING (Auto Refresh Every 5 Minutes)");
        printingLOV = "UWRIT";
        setAutoRefresh(true);

        canEdit = hasResource("POL_RI_EDIT");
        canCreate = hasResource("POL_RI_CREATE");
        canApprove = hasResource("POL_RI_APRV");
        enablePrint = hasResource("POL_RI_PRINT");
        canNavigateBranch = hasResource("POL_RI_NAVBR");
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.RENEWAL,
            FinCodec.PolicyStatus.ENDORSERI,
        });
        
        reas = true;
        enableEdit = canEdit;
        
        editFilter.add(FinCodec.PolicyStatus.POLICY);
        editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        editFilter.add(FinCodec.PolicyStatus.RENEWAL);
        editFilter.add(FinCodec.PolicyStatus.ENDORSERI);
    }
    
    public DTOList getListReasMonitoring() throws Exception {
      /*DTOList list = ListUtil.getDTOListFromQuery("select * from ins_policy where active_flag = 'Y' order by pol_no",
                    InsurancePolicyView.class,filter);
       
      list.setFilter(filter);*/
        
        if (list==null) {
            list=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
        String Sql = "         from " +
                "            ins_policy a" +
                "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id"; 
         
         Sql = Sql + "  left join gl_cost_center c on a.cc_code = c.cc_code ";       

        sqa.addQuery(Sql); 	
        
        String select = "";
        
        select = select + " substr(pol_no,0,5)||'-'||substr(pol_no,5,4)||'-'||substr(pol_no,9,4)||'-'||substr(pol_no,13,4)||'-'||substr(pol_no,17,2) as pol_no,"+
                    " a.pol_id ,a.effective_flag,a.status,a.insured_amount,a.insured_amount_e,a.premi_total,a.policy_date,c.description as cc_code,short_desc as description,f_ri_finish";
        
        sqa.addSelect(select);
        
        sqa.addFilter(list.getFilter());
       
        if (Tools.isYes(stOutstandingFlag)) {
            sqa.addClause("((a.effective_flag='N' and a.active_flag='Y') or (a.active_flag='Y'))");
        }
        
        if (Tools.isYes(stNotApprovedPolicy) || Tools.isYes(stShowClaimsNotApproved)) {
            sqa.addClause("(a.effective_flag='N' or a.effective_flag is null)");
        }
        
        if (statusFilter!=null) {
            if (Tools.isYes(stNotApprovedPolicy)){
                sqa.addClause(" status in (?,?,?)");
                sqa.addPar("POLICY");
                sqa.addPar("ENDORSE");
                sqa.addPar("RENEWAL");
            }if (Tools.isYes(stShowClaimsNotApproved) || Tools.isYes(stShowClaimsOnly)){
                sqa.addClause(" status in (?)");
                sqa.addPar("CLAIM");
            }else{
                sqa.addClause(SQLUtil.exprIN("status",statusFilter));
                
                for (int i = 0; i < statusFilter.size(); i++) {
                    String s = (String) statusFilter.get(i);
                    
                    sqa.addPar(s);
                }
            }
        }
        
        if (Tools.isYes(stShowReinsNotApproved)) {
            sqa.addClause(" f_ri_finish is null");
        }

        sqa.addClause(" f_ri_finish is null");
        
        sqa.addOrder("policy_date asc");
        
        list = sqa.getList(InsurancePolicyView.class);
        
        return list;
    }
    
     public void claimmonitoring() {
        printingLOV = "CLAIM";
        setAutoRefresh(true);
        setTitle("CLAIM MONITORING (Auto Refresh Every 5 Minutes)");
        
        canEdit = hasResource("POL_CLAIM_EDIT");
        canCreate = hasResource("POL_CLAIM_CREATE");
        canApprove = hasResource("POL_CLAIM_APRV");
        enablePrint = hasResource("POL_CLAIM_PRINT");
        canNavigateBranch = hasResource("POL_CLAIM_NAVBR");
        enableEditClaim = hasResource("POL_CLAIM_EDIT2");
        
        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.CLAIM,
            FinCodec.PolicyStatus.ENDORSECLAIM,
        });
        enableClaim=true;
        enableEdit = canEdit;
        listEffective = true;
        claim = true;
        
        editFilter.add(FinCodec.PolicyStatus.CLAIM);
    
    }
    
    public DTOList getListClaimMonitoring() throws Exception {
      /*DTOList list = ListUtil.getDTOListFromQuery("select * from ins_policy where active_flag = 'Y' order by pol_no",
                    InsurancePolicyView.class,filter);
       
      list.setFilter(filter);*/
        
        if (list==null) {
            list=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
         String Sql = "   from " +
                "      (" +
                "         select " +
                "            select  a.pol_id,a.active_flag,a.effective_flag,a.document_print_flag, "+
                "               a.f_ri_finish,a.pol_no,a.ref1,a.claim_status,a.pla_no,a.dla_no, "+
	        "               a.status,a.cc_code,a.root_id,a.create_date,a.claim_amount_est,a.claim_amount_approved, "+
	        "               a.cust_name,a.ref5,a.insured_amount_e,a.insured_amount,a.premi_total, "+
	        "               a.approved_who,a.approved_date,a.create_who, "+
	        "               a.policy_date,a.pol_type_id,a.ins_policy_type_grp_id, "+
	        "               a.period_start,a.period_end,a.f_uw_finish,a.f_claim_finish, "+
	        "               b.description as policy_type_desc " +
                "         from " +
                "            ins_policy a" +
                "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id";  
            
        
         if(getDbOverLimit()!=null){
         	Sql = Sql + 
         		" inner join ins_pol_obj c on c.pol_id = a.pol_id"+
         		" where c.insured_amount >= ?";
            sqa.addPar(dbOverLimit);
         }
        
         if(getStPolicyGroup()!=null && getStBranch()!=null && getStOverLimitPolicy().equalsIgnoreCase("Y") && getStPolicyTypeID()==null){
                if(getTransactionLimitByCCCode(getStBranch(), getStPolicyGroup())==null)
                    throw new RuntimeException("Limit TSI Daerah "+ getStBranch() + " Group "+ getStPolicyGroup() +" Belum diisi");
            
                Sql = Sql + 
         		" inner join ins_pol_obj c on c.pol_id = a.pol_id"+
         		" where c.insured_amount >= ?";
                        
                sqa.addPar(getTransactionLimitByCCCode(getStBranch(), getStPolicyGroup()));
         }else if(getStPolicyGroup()!=null && getStBranch()!=null && getStOverLimitPolicy().equalsIgnoreCase("Y") && getStPolicyTypeID()!=null){
                if(getTransactionLimitByCCCode2(getStBranch(), getStPolicyTypeID())==null)
                    throw new RuntimeException("Limit TSI Daerah "+ getStBranch() + " jenis "+ getStPolicyTypeID() +" Belum diisi");
            
                Sql = Sql + 
         		" inner join ins_pol_obj c on c.pol_id = a.pol_id"+
         		" where c.insured_amount >= ?";
                        
                sqa.addPar(getTransactionLimitByCCCode2(getStBranch(), getStPolicyTypeID()));
         }
         
         Sql = Sql + " ) a";       

        sqa.addQuery(Sql); 	
        
        String select = "";
        if(getDbOverLimit()!=null) select = " distinct ";
        if(getStPolicyGroup()!=null && getStBranch()!=null && getStOverLimitPolicy().equalsIgnoreCase("Y") && getStPolicyTypeID()==null) select = " distinct ";
        if(getStPolicyGroup()!=null && getStBranch()!=null && getStOverLimitPolicy().equalsIgnoreCase("Y") && getStPolicyTypeID()!=null) select = " distinct ";
        
        select = select + " * ";
        
        sqa.addSelect(select);
        
        sqa.addFilter(list.getFilter());
       
        if (Tools.isYes(stOutstandingFlag)) {
            sqa.addClause("((a.effective_flag='N' and a.active_flag='Y') or (a.active_flag='Y'))");
        }
        
        if (Tools.isYes(stNotApprovedPolicy) || Tools.isYes(stShowClaimsNotApproved)) {
            sqa.addClause("(a.effective_flag='N' or a.effective_flag is null)");
        }
        
        /*
        if (statusFilter!=null) {
            if (Tools.isYes(stNotApprovedPolicy)){
                sqa.addClause(" status in (?,?,?)");
                sqa.addPar("POLICY");
                sqa.addPar("ENDORSE");
                sqa.addPar("RENEWAL");
            }if (Tools.isYes(stShowClaimsNotApproved) || Tools.isYes(stShowClaimsOnly)){
                
            }else{
                sqa.addClause(SQLUtil.exprIN("status",statusFilter));
                
                for (int i = 0; i < statusFilter.size(); i++) {
                    String s = (String) statusFilter.get(i);
                    
                    sqa.addPar(s);
                }
            }
        }*/
        
        sqa.addClause(" status in (?)");
        sqa.addPar("CLAIM");
        
        if (Tools.isYes(stShowReinsNotApproved)) {
            sqa.addClause(" f_ri_finish is null");
        }
        
        if(stCreateWho!=null){
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateWho);
        }
        
        if (stBranch!=null) {
            sqa.addClause(
                    "a.cc_code = ?"
                    );
            sqa.addPar(stBranch);
        }
        
        if (dtFilterPolicyDateFrom!=null) {
            sqa.addClause(
                    "a.policy_date >= ?"
                    );
            sqa.addPar(dtFilterPolicyDateFrom);
        }
        
        if (dtFilterPolicyDateTo!=null) {
            sqa.addClause(
                    "a.policy_date <= ?"
                    );
            sqa.addPar(dtFilterPolicyDateTo);
        }
        
        if (dtFilterPolicyExpireFrom!=null) {
            sqa.addClause(
                    "a.period_end >= ?"
                    );
            sqa.addPar(dtFilterPolicyExpireFrom);
        }
        
        if (dtFilterPolicyExpireTo!=null) {
            sqa.addClause(
                    "a.period_end <= ?"
                    );
            sqa.addPar(dtFilterPolicyExpireTo);
        }
        
        if(stPolicyTypeID!=null){
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }
        
        if(!isIsBondingDivision()){
            if (stPolicyGroup!=null) {
            sqa.addClause(
                    "a.ins_policy_type_grp_id = ?"
                    );
            sqa.addPar(stPolicyGroup);
            }
        }else{
            
            policyTypeGroupFilter = array2list(new String [] {
                "7",
                "8",
            });
            
            sqa.addClause(SQLUtil.exprIN("a.ins_policy_type_grp_id", policyTypeGroupFilter));
                
                for (int i = 0; i < policyTypeGroupFilter.size(); i++) {
                    String s = (String) policyTypeGroupFilter.get(i);
                    
                    sqa.addPar(s);
                }
        }
        
        sqa.addClause(" a.f_claim_finish =?");
        sqa.addPar("N");
        
        sqa.addOrder("root_id desc, create_date desc");
        
        list = sqa.getList(InsurancePolicyView.class);
        
        return list;
    }
  
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }
    
    public void clickReApprovalByDate2() throws Exception {
         
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null; 

        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select pol_id "+
                  "  from ins_policy "+
                  "   where status = 'ENDORSE' and pol_type_id = '1' "+
                  "   and cc_code = '43' and coalesce(effective_flag,'N') <> 'Y' "+
                  "   and substr(pol_no,0,17) in ( "+
                  "           select substr(pol_no,0,17)  "+
                  "           from proses_bppdan "+
                  "   ) "+
                  "  order by pol_id",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());
            
            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);

            polis.markUpdate();
            //polis.getObjects().markAllUpdate();
            //polis.getCoins2().markAllUpdate();
            //polis.getCoinsCoverage().markAllUpdate();
            polis.recalculate();
             
            polis.setStPostedFlag("Y");
            polis.setStEffectiveFlag("Y");
            polis.setStRIFinishFlag("Y");
            polis.setStAdminNotes("PROSES_KPRBTN");
            polis.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
            polis.setDtApprovedDate(new Date());
            //polis.setStPassword(UserManager.getInstance().getUser().getStPasswd());
            //polis.setStClientIP(UserManager.getInstance().getUser().getIPAddress());

            getRemoteInsurance().save(polis, polis.getStNextStatus(), true);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'PROSES_KPRBTN' where pol_no = ?");
            
            PS.setObject(1,policy.getStPolicyNo());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            
            
        }
        
    }
    
    public void clickReApprovalByDate2Desc() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where admin_notes <> 'PROSES2' and pol_id in "+
                " ( "+
                "         select policy_id "+
                "         from ins_pol_coins "+
                "          where policy_id in ( "+
                "                 select pol_id "+
                "                 from ins_policy "+
                "                 where status = 'POLICY' and pol_type_id = '21' "+
                 "                and date_trunc('day',policy_date) >= '2016-12-01 00:00:00' and date_trunc('day',policy_date) <= '2016-12-31 00:00:00' "+
                 "                and effective_flag = 'Y' "+
                 "        ) and entity_id <> 1 "+
                 " ) order by poL_id ",
                InsurancePolicyView.class
                );
        
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);
            
            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());
            
            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            
            polis.getObjects().markAllUpdate();
            polis.getCoins2().markAllUpdate();
            polis.getCoinsCoverage().markAllUpdate();
            polis.recalculate();
            //polis.recalculateTreaty();

            polis.setStPostedFlag("Y");
            polis.setStEffectiveFlag("Y");
            getRemoteInsurance().save(polis, polis.getStNextStatus(), true);

            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'PROSES2' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            
        }
        
    }

    public String getStFileUpload() {
        return stFileUpload;
    }

    public void setStFileUpload(String stFileUpload) {
        this.stFileUpload = stFileUpload;
    }

    public void clickCreateEndorseBatalTotal() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.editCreateEndorseBatalTotal(policyID);
        
        form.show();
    }

    public String getStEndorseMode() {
        return stEndorseMode;
    }

    public void setStEndorseMode(String stEndorseMode) {
        this.stEndorseMode = stEndorseMode;
    }
    
    public void clickCreateEndorseByMode() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        if(getStEndorseMode()==null) throw new RuntimeException("Jenis endorse harus dipilih");
            
        if(getStEndorseMode().equalsIgnoreCase("TOTAL")){
            form.editCreateEndorseBatalTotalMode(policyID);
            form.setTotalEndorseMode(true);
            form.getPolicy().setStEndorseMode("TOTAL");
        }else if(getStEndorseMode().equalsIgnoreCase("PARTIAL_SI")){
            form.editCreateEndorseDescriptionMode(policyID);
            form.setPartialEndorseTSIMode(true);
            form.getPolicy().setStEndorseMode("PARTIAL_SI");
            form.getPolicy().setStEndorseNotes("ENDORSE PERUBAHAN TSI");
            //setItemNull(form.getPolicy());
        }else if(getStEndorseMode().equalsIgnoreCase("PARTIAL_RATE")){
            form.editCreateEndorseDescriptionMode(policyID);
            form.setPartialEndorseRateMode(true);
            form.getPolicy().setStEndorseMode("PARTIAL_RATE");
            form.getPolicy().setStEndorseNotes("ENDORSE PERUBAHAN RATE");
            //setItemNull(form.getPolicy());
        }else if(getStEndorseMode().equalsIgnoreCase("DESCRIPTION")){
            form.editCreateEndorseDescriptionMode(policyID);
            form.setDescriptionEndorseMode(true);
            form.getPolicy().setStEndorseMode("DESCRIPTION");
            form.getPolicy().setStEndorseNotes("ENDORSE PERUBAHAN KETERANGAN");
            //setItemNull(form.getPolicy());
        }else if(getStEndorseMode().equalsIgnoreCase("RESTITUTION")){
            form.editCreateEndorse(policyID);
            //form.setRestitutionEndorseMode(true);
            form.getPolicy().setStEndorseMode("RESTITUTION");

            if(form.getPolicy().isRestitutionEndorseMode())
                if(!form.getPolicy().isPolisIndukPremiPaid())
                    throw new RuntimeException("Polis induk belum dibayar premi, tidak bisa restitusi");
            //setItemNull(form.getPolicy());
        }else if(getStEndorseMode().equalsIgnoreCase("OTHERS")){
            form.editCreateEndorse(policyID);
            form.getPolicy().setStEndorseMode("OTHERS");
            //setItemNull(form.getPolicy());
        }else if(getStEndorseMode().equalsIgnoreCase("COVER_TYPE")){
            form.editCreateEndorseCoverType(policyID);
            form.getPolicy().setStEndorseMode("COVER_TYPE");
        }else if (getStEndorseMode().equalsIgnoreCase("RESTITUTIONX"))
        {
          form.editCreateEndorseDescriptionMode(this.policyID);
          if ((!form.getPolicy().getStCostCenterCode().equalsIgnoreCase("13")) && (!form.getPolicy().getStCostCenterCode().equalsIgnoreCase("19"))) {
            throw new RuntimeException("Endorse Restitusi Silang hanya untuk Cabang Riau dan Batam");
          }
          form.setPartialEndorseRateMode(true);
          form.getPolicy().setStEndorseMode("RESTITUTIONX");
          form.getPolicy().setStEndorseNotes("DENGAN INI DICATAT DAN DISETUJUI, BAHWA :\n\n1). PINJAMAN KREDIT ATAS NAMA YANG TERCANTUM PADA LAMPIRAN POLIS INI MERUPAKAN RESTITUSI SILANG.\n2). ATAS HAL TERSEBUT, MAKA DILAKUKAN RESTITUSI PREMI SESUAI DENGAN SYARAT DAN KETENTUAN YANG BERLAKU");
        }

        form.show();
    }

    public void setItemNull(InsurancePolicyView policy){
        final DTOList item = policy.getDetails();

        for (int i = 0; i < item.size(); i++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) item.get(i);

            items.setDbAmount(null);
            items.setDbTaxAmount(null);
            items.setDbRate(null);

        }
    }
    
     public void clickApprovalByDirector() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        //final PolicyForm form = (PolicyForm)newForm("policy_edit", this);
        
        form.approvalByDirector(policyID);
        
        form.setApprovalMode(true);
        
        form.show();
    }

    public boolean isApprovalByDirector() {
        return approvalByDirector;
    }

    public void setApprovalByDirector(boolean approvalByDirector) {
        this.approvalByDirector = approvalByDirector;
    }

    public boolean isClaimApprovalByDirector() {
        return claimApprovalByDirector;
    }

    public void setClaimApprovalByDirector(boolean claimApprovalByDirector) {
        this.claimApprovalByDirector = claimApprovalByDirector;
    }

    public void clickReject() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
       
        form.reject(policyID);
        
        form.setApprovalMode(true);
        
        form.setRejectMode(true);
        
        form.show();
    }

    public String getStPolicyStatus()
    {
        return stPolicyStatus;
    }

    public void setStPolicyStatus(String stPolicyStatus)
    {
        this.stPolicyStatus = stPolicyStatus;
    }

    public String getStPolicyCustomer()
    {
        return stPolicyCustomer;
    }

    public void setStPolicyCustomer(String stPolicyCustomer)
    {
        this.stPolicyCustomer = stPolicyCustomer;
    }

    public String getStPrincipal()
    {
        return stPrincipal;
    }

    public void setStPrincipal(String stPrincipal)
    {
        this.stPrincipal = stPrincipal;
    }
    
    public void clickEditKeterangan() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.setReasMode(reas);
        form.setEditKeteranganMode(true);
        
        form.editKeterangan(policyID);
        
        form.show();
    }
    
    public void clickSuperEditReas() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.setReasMode(reas);
        
        form.superEditReasOnly(policyID);
        
        
        form.show();
    }
    
    public void clickCreateEndorseBatalAndNewPolicy() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.setCreateEndorseAndPolicyMode(true);
        form.editCreatePolisAfterEndorse(policyID);
        
        form.show();
        
    }
    
    public void clickCreateInward() throws Exception {
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        form.createNewInward();
        
        //form.setSavePolicyHistoryMode(true);
        
        form.setEnableNoPolicy(true);
        
        //form.setCanCreatePolicyHistory(true);
        
        form.show();
    }

    public String getStLimitFlag()
    {
        return stLimitFlag;
    }

    public void setStLimitFlag(String stLimitFlag)
    {
        this.stLimitFlag = stLimitFlag;
    }

    public String getStNotPrintedPolicy() {
        return stNotPrintedPolicy;
    }

    public void setStNotPrintedPolicy(String stNotPrintedPolicy) {
        this.stNotPrintedPolicy = stNotPrintedPolicy;
    }

    public String getStRegion() {
        return stRegion;
    }

    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }

    public boolean isCanNavigateRegion() {
        return canNavigateRegion;
    }

    public void setCanNavigateRegion(boolean canNavigateRegion) {
        this.canNavigateRegion = canNavigateRegion;
    }
    
    public void clickApprovalByDivision() throws Exception {
        validatePolicyID();
        
        String form_name = "policy_edit";         
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";         
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";                      
        final PolicyForm form = (PolicyForm)newForm(form_name,this);
        
        //final PolicyForm form = (PolicyForm)newForm("policy_edit", this);
        
        form.approvalByDirector(policyID);
        
        form.setApprovalMode(true);
        
        form.show();
    }

    public boolean isReinsApprovalByDirector() {
        return reinsApprovalByDirector;
    }

    public void setReinsApprovalByDirector(boolean reinsApprovalByDirector) {
        this.reinsApprovalByDirector = reinsApprovalByDirector;
    }
    
    public void clickEditReinsurance() throws Exception {
        validatePolicyID();
        
        final PolicyForm form = (PolicyForm)newForm("policy_edit_reinsurance",this);
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);
        
        if (pol==null) throw new RuntimeException("Document not found ??");
        
        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");
        
        form.setReasMode(reas);
        
        form.setApprovalMode(false);
        
        form.setEditReasMode(true);

        if(pol.isStatusInward())
            form.editInward(policyID);
        else
            form.editReinsuranceOnly(policyID);
        
        //form.getPolicy().setStRIPostedFlag("N");
        
        form.getPolicy().setEditReasOnlyMode(true);

        form.show();
    }
    
    public void clickReverseReinsurance() throws Exception {
        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);

        if (pol==null) throw new RuntimeException("Document not found ??");

        form.superEdit(policyID);

        if (!form.getPolicy().isEffective()) throw new Exception("Data tidak bisa di reverse karena belum disetujui");

        form.setReverseMode(true);

        //update ins_policy set posted_flag = null,effective_flag='N',approved_who=null,approved_date=null,
        //password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?
        //f_ri_finish = 'N',ri_posted_flag='N'
        
        form.getPolicy().setStRIFinishFlag(null);
        form.getPolicy().setStRIPostedFlag(null);

        form.show();
    }
    
    public void clickApprovalReinsOnly() throws Exception {
        final PolicyForm form = (PolicyForm)newForm("policy_edit_reinsurance", this);
        
        form.editReinsuranceOnly2(policyID);
        
        form.setApprovalMode(false);
        
        form.setApprovedReasMode(true);
        
        form.setEditReasMode(true);
        
        form.setApprovedReasMode(true);
        
        form.getPolicy().setStRIFinishFlag("Y");
        form.getPolicy().markUpdateO();
        form.getPolicy().setStRIPostedFlag("Y");
        
        form.show();
    }

    public boolean isCanReverseReins() {
        return canReverseReins;
    }

    public void setCanReverseReins(boolean canReverseReins) {
        this.canReverseReins = canReverseReins;
    }

    public boolean isCanApproveReins() {
        return canApproveReins;
    }

    public void setCanApproveReins(boolean canApproveReins) {
        this.canApproveReins = canApproveReins;
    }

    public Date getDtPolicyDateFrom() {
        return dtPolicyDateFrom;
    }
    
    public void setDtPolicyDateFrom(Date dtPolicyDateFrom) {
        this.dtPolicyDateFrom = dtPolicyDateFrom;
    }
    
    public Date getDtPolicyDateTo() {
        return dtPolicyDateTo;
    }
    
    public void setDtPolicyDateTo(Date dtPolicyDateTo) {
        this.dtPolicyDateTo = dtPolicyDateTo;
    }
    
    public String getStAge() {
        return stAge;
    }
    
    public void setStAge(String stAge) {
        this.stAge = stAge;
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

    /**
     * @return the stCriteria
     */
    public String getStCriteria() {
        return stCriteria;
    }

    /**
     * @param stCriteria the stCriteria to set
     */
    public void setStCriteria(String stCriteria) {
        this.stCriteria = stCriteria;
    }


    public void clickReApprovalReinsurance() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                 "   from ins_policy a "+
                 "      where status = 'POLICY' "+
                 "      and pol_type_id = 21 "+
                 "      and effective_flag = 'Y' "+
                 "      and date_trunc('day',policy_date) >= '2012-12-01 00:00:00' "+
                 "      and date_trunc('day',policy_date) <= '2012-12-31 00:00:00' "+
                 "      and admin_notes is null "+
                 "      order by pol_id",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);

            final DTOList objects = polis.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                obj.getTreaties().deleteAll();
                obj.setStInsuranceTreatyID(null);
                obj.setStInsuranceTreatyID("31");

                obj.markUpdate();

                obj.getTreaties().markAllUpdate();

                final DTOList treatyDetails = obj.getTreatyDetails();
                treatyDetails.markAllUpdate();

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    trdi.getShares().markAllUpdate();
                }

            }

            //polis.markUpdate();
            polis.getObjects().markAllUpdate();
            polis.recalculate();
            polis.recalculateTreaty();

            polis.setStPostedFlag("Y");
            polis.setStEffectiveFlag("Y");
            polis.setStAdminNotes("PROSES_DESEMBER_KOAS");
            getRemoteInsurance().save(polis, polis.getStNextStatus(), true);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            //PreparedStatement PS = S.setQuery("update PROSES_JULI_JS set admin_notes = 'PROSES_JULI_JS' where pol_id = ?");

            //PS.setObject(1,policy.getStPolicyID());

            //int j = PS.executeUpdate();

            //if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");


        }

    }

    public Date getDtLiquidityFrom() {
        return dtLiquidityFrom;
    }

    public void setDtLiquidityFrom(Date dtLiquidityFrom) {
        this.dtLiquidityFrom = dtLiquidityFrom;
    }

    public Date getDtLiquidityTo() {
        return dtLiquidityTo;
    }

    public void setDtLiquidityTo(Date dtLiquidityTo) {
        this.dtLiquidityTo = dtLiquidityTo;
    }

    public Date getDtEndOfCreditFrom() {
        return dtEndOfCreditFrom;
    }

    public void setDtEndOfCreditFrom(Date dtEndOfCreditFrom) {
        this.dtEndOfCreditFrom = dtEndOfCreditFrom;
    }

    public Date getDtEndOfCreditTo() {
        return dtEndOfCreditTo;
    }

    public void setDtEndOfCreditTo(Date dtEndOfCreditTo) {
        this.dtEndOfCreditTo = dtEndOfCreditTo;
    }

    public void clickChangeTreatyByDate2() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy a "+
                " where pol_type_id = '59' "+
                " and status in ('POLICY','RENEWAL') "+
                " and period_start >= '2013-04-01 00:00:00' "+
                " and coalesce(admin_notes,'') not like '%GANTI TREATY 2013%' "+
                " order by pol_id",
                InsurancePolicyView.class);
        
        PolicyForm form = new PolicyForm();

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);

            final DTOList objects = pol.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

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
                obj.setStInsuranceTreatyID("31");
                
            }

            pol.getObjects().markAllUpdate();
            pol.recalculate();
            pol.recalculateTreaty();

            getRemoteInsurance().save(pol, pol.getStNextStatus(), false);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'GANTI TREATY 2013' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");


        }

    }

    public void clickDeleteSignCode() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);

        if (pol==null) throw new RuntimeException("Document not found ??");

        if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");

        form.setReasMode(reas); 

        form.setApprovalMode(false);

        form.setInputPaymentDateMode(true);

        form.editPayment(policyID);

        form.getPolicy().setStSignCode(null);
        form.getPolicy().setStDocumentPrintFlag(null);

        form.setReadOnly(true);

        form.show();
    }

    /**
     * @return the canDeleteSignCode
     */
    public boolean isCanDeleteSignCode() {
        return canDeleteSignCode;
    }

    /**
     * @param canDeleteSignCode the canDeleteSignCode to set
     */
    public void setCanDeleteSignCode(boolean canDeleteSignCode) {
        this.canDeleteSignCode = canDeleteSignCode;
    }

    /**
     * @return the enablePrintPreSign
     */
    public boolean isEnablePrintPreSign() {
        return enablePrintPreSign;
    }

    /**
     * @param enablePrintPreSign the enablePrintPreSign to set
     */
    public void setEnablePrintPreSign(boolean enablePrintPreSign) {
        this.enablePrintPreSign = enablePrintPreSign;
    }

    /**
     * @return the enablePrintDigitized
     */
    public boolean isEnablePrintDigitized() {
        return enablePrintDigitized;
    }

    /**
     * @param enablePrintDigitized the enablePrintDigitized to set
     */
    public void setEnablePrintDigitized(boolean enablePrintDigitized) {
        this.enablePrintDigitized = enablePrintDigitized;
    }
    
    private static HashSet formList = null;
    
    private void loadFormList() {
      if (formList==null || true) {
         final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/report")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

    public void previewPolicy()  throws Exception {

      loadFormList();

      final String policyid = policyID;

      final String fontsize = stFontSize;

      final String attached = stAttached;

      final String authorized = stAuthorized;

      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPreview(policyid);

      SessionManager.getInstance().getRequest().setAttribute("POLICY",policy);
      SessionManager.getInstance().getRequest().setAttribute("FONTSIZE",fontsize);
      SessionManager.getInstance().getRequest().setAttribute("attached",attached);
      SessionManager.getInstance().getRequest().setAttribute("authorized",authorized);
      SessionManager.getInstance().getRequest().setAttribute("preview","preview");
      SessionManager.getInstance().getRequest().setAttribute("digitalsign","N");
      
      final ArrayList plist = new ArrayList();

      //String nom = getString(rq.getParameter("nom"));

      String alter = stPrintForm;

      if (alter==null) throw new RuntimeException("ALT code not specified");

      if (alter==null) alter=""; else alter="_alt"+alter;

        plist.add(policy.getStPolicyTypeID()+"_"+policy.getStStatus()+alter+"_"+policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID()+"_"+policy.getStStatus()+alter);
        plist.add(policy.getStPolicyTypeID()+alter+"_"+policy.getStCostCenterCode()+"_"+attached);
        plist.add(policy.getStPolicyTypeID()+alter+"_"+policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID()+alter);

        plist.add("0"+"_"+policy.getStStatus()+alter);
        plist.add("0"+alter+"_"+policy.getStCostCenterCode()+"_"+attached);
        plist.add("0"+alter+"_"+policy.getStCostCenterCode());
        plist.add("0"+alter+"_"+attached);
        plist.add("0"+alter);

      //plist.add("0"+"_"+policy.getStStatus());
      //plist.add("0");

      String urx=null;

      //logger.logDebug("printPolicy: scanlist:"+plist);

      for (int i = 0; i < plist.size(); i++) {
         String s = (String) plist.get(i);

         if (formList.contains("pol"+s+".fop.jsp")) {
            urx = "/pages/insurance/report/pol"+s+".fop";
            break;
         }
      }

      if (urx==null) throw new RuntimeException("Unable to find suitable print form");

      //if (nom!=null)
         //getRemoteInsurance().registerPrintSerial(policy, nom, urx);


      logger.logDebug("printPolicy: forwarding to ########## "+urx);

      super.redirect(urx);

      //SessionManager.getInstance().getRequest().setAttribute("FLOW_CARD_CHECKED","Y");

   }

    public void clickExcelReas() throws Exception {

        final DTOList l = EXCEL_REAS_MEMBER();
        final DTOList m = EXCEL_REAS_INDUK();

        SessionManager.getInstance().getRequest().setAttribute("MEMBER",l);
        SessionManager.getInstance().getRequest().setAttribute("INDUK",m);

        EXPORT_MIGRATION();

    }

    public DTOList EXCEL_REAS_MEMBER() throws Exception {

        final String sql = "select a.pol_no,b.ref1,b.refd1 as tgl_awal,b.refd2 as tgl_akhir,f.treaty_type,g.ent_name,b.insured_amount,h.premi_new, "+
                            " b.premi_total,e.*  "+
                            " from ins_policy a "+
                            " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                            " inner join ins_pol_cover h on b.ins_pol_obj_id = h.ins_pol_obj_id "+
                            " inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "+
                            " inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id "+
                            " inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id "+
                            " inner join ins_pol_ri e on d.ins_pol_tre_det_id = e.ins_pol_tre_det_id "+
                           "  inner join ent_master g on e.member_ent_id = g.ent_id "+
                            " where a.pol_id = "+ policyID +
                            " ORDER BY A.POL_ID,B.INS_POL_OBJ_ID,e.valid_ri_date,f.ins_treaty_detail_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("MEMBER", l);

        return l;

    }

    public DTOList EXCEL_REAS_INDUK() throws Exception {

        final String sql = "select a.pol_no,a.policy_date,a.period_start,f.treaty_type,b.insured_amount,b.ref1,b.refd1 as tgl_awal,b.refd2 as tgl_akhir,h.rate,d.* "+
                            " from ins_policy a"+
                            " inner join ins_pol_obj b on a.pol_id = b.pol_id"+
                            " inner join ins_pol_cover h on b.ins_pol_obj_id = h.ins_pol_obj_id"+
                            " inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id"+
                            " inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id"+
                            " inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id"+
                            " where "+
                            " a.pol_id = "+ policyID +
                            " ORDER BY A.POL_ID,B.INS_POL_OBJ_ID,f.ins_treaty_detail_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("INDUK", l);

        return l;

    }
    
    
    public void EXPORT_MIGRATION()  throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //CREATE SHEET INDUK
        XSSFSheet sheet = wb.createSheet("INDUK");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("INDUK");


            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("pol_no");
            row1.createCell(1).setCellValue("policy_date");
            row1.createCell(2).setCellValue("period_start");
            row1.createCell(3).setCellValue("treaty_type");
            row1.createCell(4).setCellValue("insured_amount");
            row1.createCell(5).setCellValue("ref1");
            row1.createCell(6).setCellValue("tanggal_awal");
            row1.createCell(7).setCellValue("tanggal_akhir");

            row1.createCell(8).setCellValue("rate");
            row1.createCell(9).setCellValue("ins_pol_tre_det_id");
            row1.createCell(10).setCellValue("ins_pol_treaty_id");
            row1.createCell(11).setCellValue("ins_treaty_detail_id");
            row1.createCell(12).setCellValue("tsi_amount");
            row1.createCell(13).setCellValue("premi_amount");
            row1.createCell(14).setCellValue("comm_rate");
            row1.createCell(15).setCellValue("comm_amt");
            row1.createCell(16).setCellValue("tsi_pct");
            row1.createCell(17).setCellValue("premi_rate");
            row1.createCell(18).setCellValue("edit_flag");

            HashDTO h2 = (HashDTO) list.get(0);
            String nomorPolis = h2.getFieldValueByFieldNameST("pol_no");

        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tanggal_awal"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tanggal_akhir"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ins_pol_treaty_id"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ins_treaty_detail_id"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_amt").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_pct").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("edit_flag"));

        }

        //CREATE SHEET MEMBER
        XSSFSheet sheetMember = wb.createSheet("MEMBER");

        final DTOList listMember = (DTOList) SessionManager.getInstance().getRequest().getAttribute("MEMBER");


            XSSFRow row1Member = sheetMember.createRow(0);
            row1Member.createCell(0).setCellValue("pol_no");
            row1Member.createCell(1).setCellValue("ref1");
            row1Member.createCell(2).setCellValue("tgl_awal");
            row1Member.createCell(3).setCellValue("tgl_akhir");
            row1Member.createCell(4).setCellValue("treaty_type");
            row1Member.createCell(5).setCellValue("ent_name");
            row1Member.createCell(6).setCellValue("insured_amount");
            row1Member.createCell(7).setCellValue("premi_new");

            row1Member.createCell(8).setCellValue("premi_total");
            row1Member.createCell(9).setCellValue("ins_pol_ri_id");
            row1Member.createCell(10).setCellValue("ins_pol_treaty_id");
            row1Member.createCell(11).setCellValue("ins_pol_tre_det_id");
            row1Member.createCell(12).setCellValue("ins_treaty_detail_id");
            row1Member.createCell(13).setCellValue("ins_treaty_shares_id");
            row1Member.createCell(14).setCellValue("member_ent_id");
            row1Member.createCell(15).setCellValue("sharepct");
            row1Member.createCell(16).setCellValue("premi_amount");
            row1Member.createCell(17).setCellValue("tsi_amount");
            row1Member.createCell(18).setCellValue("premi_rate");
            row1Member.createCell(19).setCellValue("auto_rate_flag");
            row1Member.createCell(20).setCellValue("use_rate_flag");
            row1Member.createCell(21).setCellValue("ricomm_rate");
            row1Member.createCell(22).setCellValue("ricomm_amt");
            row1Member.createCell(23).setCellValue("notes");
            row1Member.createCell(24).setCellValue("ri_slip_no");
            row1Member.createCell(25).setCellValue("f_approve");
            row1Member.createCell(26).setCellValue("valid_ri_date");

        for (int j=0;j< listMember.size() ; j++){
            HashDTO hMember = (HashDTO) listMember.get(j);

            XSSFRow row = sheetMember.createRow(j+1);
            row.createCell(0).setCellValue(hMember.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(hMember.getFieldValueByFieldNameST("ref1"));
            row.createCell(2).setCellValue(hMember.getFieldValueByFieldNameDT("tanggal_awal"));
            row.createCell(3).setCellValue(hMember.getFieldValueByFieldNameDT("tanggal_akhir"));
            row.createCell(4).setCellValue(hMember.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(5).setCellValue(hMember.getFieldValueByFieldNameST("ent_name"));
            row.createCell(6).setCellValue(hMember.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(7).setCellValue(hMember.getFieldValueByFieldNameBD("premi_new").doubleValue());
            row.createCell(8).setCellValue(hMember.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(9).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_ri_id").doubleValue());
            row.createCell(10).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_treaty_id").doubleValue());
            row.createCell(11).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(12).setCellValue(hMember.getFieldValueByFieldNameBD("ins_treaty_detail_id").doubleValue());
            row.createCell(13).setCellValue(hMember.getFieldValueByFieldNameBD("ins_treaty_shares_id").doubleValue());
            row.createCell(14).setCellValue(hMember.getFieldValueByFieldNameBD("member_ent_id").doubleValue());
            row.createCell(15).setCellValue(hMember.getFieldValueByFieldNameBD("sharepct").doubleValue());
            row.createCell(16).setCellValue(hMember.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            row.createCell(17).setCellValue(hMember.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(18).setCellValue(hMember.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            row.createCell(19).setCellValue(hMember.getFieldValueByFieldNameST("auto_rate_flag"));
            row.createCell(20).setCellValue(hMember.getFieldValueByFieldNameST("use_rate_flag"));
            row.createCell(21).setCellValue(hMember.getFieldValueByFieldNameBD("ricomm_rate").doubleValue());
            row.createCell(22).setCellValue(hMember.getFieldValueByFieldNameBD("ricomm_amt").doubleValue());
            row.createCell(23).setCellValue(hMember.getFieldValueByFieldNameST("notes"));
            row.createCell(24).setCellValue(hMember.getFieldValueByFieldNameST("ri_slip_no"));
            row.createCell(25).setCellValue(hMember.getFieldValueByFieldNameST("f_approve"));
            row.createCell(26).setCellValue(hMember.getFieldValueByFieldNameDT("valid_ri_date"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename="+ nomorPolis +".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }
    
    public void clickChangeTreatyByDateEndorse() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        /*
        listPolicy = ListUtil.getDTOListFromQuery(
                " select a.* "+
                " from ins_policy a "+
                " where pol_type_id in (21) "+
                " and effective_flag = 'Y' and status in ('POLICY','RENEWAL') "+
                " and date_trunc('day',policy_date) >= '2013-07-01 00:00:00' "+
                " and date_trunc('day',policy_date) <= '2013-09-30 00:00:00' "+
                " and coalesce(admin_notes,'') <> 'TREATY CREDIT FINAL 5' "+
                " order by pol_id",
                InsurancePolicyView.class);
        */

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where status = 'ENDORSE' "+
                " and pol_no in "+
                " ( "+
                " select pol_no "+
                " from proses_reas group by pol_no)",
                InsurancePolicyView.class);


        PolicyForm form = new PolicyForm(); 

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);

            final DTOList objects = pol.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                boolean orOnly = false;

                if(obj.getStReference80()==null) orOnly = false;

                 if(obj.getStReference80()!=null){
                     if(obj.getStReference80().equalsIgnoreCase("OR-05122013")) orOnly = true;
                     else orOnly = false;
                 }

                //if(!orOnly) continue;

                //obj.markUpdate();
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

                //set treaty
                final boolean inward = pol.getCoverSource().isInward();
                String treatyID = pol.getDtPeriodStart()!=null?pol.getInsuranceTreatyID(pol.getDtPeriodStart()):Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY");

                

                /*
                if(pol.getStPolicyTypeID().equalsIgnoreCase("59")){

                    DateTime startDate = new DateTime(obj.getDtReference2());
                    DateTime endDate = new DateTime(obj.getDtReference3());
                    DateTime tglLahir = new DateTime(obj.getDtReference1());

                    Years umur = Years.yearsBetween(tglLahir, startDate);
                    int umurDebitur = umur.getYears();

                    int usiaKredit = umurDebitur;
                    //int usiaKredit = Integer.parseInt(obj.getStReference2());

                    DateTime bulanIzinTreaty = new DateTime(DateUtil.getDate("31/03/2013"));

                    boolean sudahIjin = false;

                    if(startDate.isAfter(bulanIzinTreaty)) sudahIjin = true;

                    Years y = Years.yearsBetween(startDate, endDate);
                    int year = y.getYears();

                    int usiaPlusLamaKredit = usiaKredit + year;

                    int tahunAwal = Integer.parseInt(DateUtil.getYear(obj.getDtReference2()));
                    int tahunPolis = Integer.parseInt(DateUtil.getYear(policy.getDtPolicyDate()));

                    boolean tahunBenar = tahunPolis >= tahunAwal?true:false;

                    if(usiaKredit >= 17 && usiaKredit <=64 && usiaPlusLamaKredit <= 70 && sudahIjin && umurDebitur >=17 && tahunBenar) orOnly = false;
                }

                if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    int usiaKreasi = Integer.parseInt(obj.getStReference2());
                    int usiaPlusLamaKreasi = usiaKreasi + Integer.parseInt(obj.getStReference4());

                    boolean isORKreasi = obj.getStReference8().equalsIgnoreCase("1");

                    DateTime bulanIzinTreaty = new DateTime(DateUtil.getDate("31/03/2013"));

                    boolean sudahIjin = false;

                    DateTime startDate = new DateTime(obj.getDtReference2());
                    DateTime endDate = new DateTime(obj.getDtReference3());
                    DateTime tglLahir = new DateTime(obj.getDtReference1());

                    Years umur = Years.yearsBetween(tglLahir, startDate);
                    int umurDebitur = umur.getYears();

                    int usiaKredit = umurDebitur;

                    if(startDate.isAfter(bulanIzinTreaty)) sudahIjin = true;

                    int tahunAwal = Integer.parseInt(DateUtil.getYear(obj.getDtReference2()));
                    int tahunPolis = Integer.parseInt(DateUtil.getYear(policy.getDtPolicyDate()));
                    
                    boolean tahunBenar = tahunPolis >= tahunAwal?true:false;

                    if(usiaKreasi >= 17 && usiaKreasi <=64 && usiaPlusLamaKreasi <= 70 && isORKreasi && sudahIjin && umurDebitur >=17  && tahunBenar) orOnly = false;

                }
                */

                if(orOnly){
                    obj.setStInsuranceTreatyID("36");
                }else{
                    //if(treatyID == null) treatyID = "31";
                    
                    obj.setStInsuranceTreatyID("31");
                }
                
            }

            //pol.getObjects().markAllUpdate();
            pol.recalculate();
            pol.recalculateTreaty();
           
            pol.recalculateTreatyInitialize();

            getRemoteInsurance().saveReinsuranceOnly(pol, pol.getStNextStatus(), false);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'TREATY CREDIT FINAL 5' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");

        }

    }

    public void clickChangeTreatyByDate3() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                "   from ins_policy   "+
                "      where pol_no in (select pol_no from proses_reas) AND coalesce(admin_notes,'') <> 'RI_UPD' "+
                "      order by policy_date",
               InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            //final InsurancePolicyView polis = (InsurancePolicyView) pol;
            //getRemoteInsurance().reverse(pol);

            DTOList objects = pol.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

               // if(obj.getStReference80()==null) continue;

                //int nourut = Integer.parseInt(obj.getStOrderNo());
                //if(nourut > 60) continue;
  
                String treatyAutoFac = "45";
                String onlyOR = "36";
                String treatyExcluded = "35";
                String treaty = obj.getDtReference2()!=null?pol.getInsuranceTreatyID(obj.getDtReference2()):pol.getInsuranceTreatyID(pol.getDtPeriodStart());
                String treatyApply = "";
                String treaty2013 = "31";
                String treaty2014 = "38";
                String treaty2015 = "84";

                if(pol.getStPolicyTypeID().equalsIgnoreCase("1"))
                    treaty = obj.getDtReference1()!=null?pol.getInsuranceTreatyID(obj.getDtReference1()):pol.getInsuranceTreatyID(pol.getDtPeriodStart());

                //treatyApply = obj.getStInsuranceTreatyID();
                boolean proses = true;

                treatyApply = treaty;

                treatyApply = "36";

                //if(obj.getStReference80()!=null){

                    //if(obj.getStReference80().equalsIgnoreCase("")) continue;
                    /*
                    if(pol.getStPolicyTypeID().equalsIgnoreCase("59") || pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                             if(obj.getStReference80().contains("AUTOFAC")) treatyApply = treatyAutoFac;
                             else if(obj.getStReference80().contains("OR")) treatyApply = onlyOR;
                             else if(obj.getStReference80().contains("TREATY")) treatyApply = treaty;
                             else if(obj.getStReference80().contains("RI COM")) treatyApply = "COMM";
                             else if(obj.getStReference80().contains("2015")) treatyApply = treaty2015;
                             else continue;

                             if(obj.getStReference80().contains("TREATY"))
                                if(treatyApply==null)
                                    treatyApply = treaty2015;    
                    }*/
                    

                    // FIRE
                    /*
                    if(!pol.getStPolicyTypeID().equalsIgnoreCase("59") && !pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                         if(obj.getStReference80().contains("EXCLUDE")) treatyApply = treatyExcluded;
                         else if(obj.getStReference80().contains("2015")) treatyApply = treaty2015;
                         else if(obj.getStReference80().contains("2014")) treatyApply = treaty2014;
                         else if(obj.getStReference80().contains("2013")) treatyApply = treaty2013;
                         else if(obj.getStReference80().contains("OR")) treatyApply = onlyOR;
                         else if(obj.getStReference80().contains("TREATY")) treatyApply = treaty;
                         else if(obj.getStReference80().contains("RI COM")) treatyApply = "COMM";
                         else continue;
                    }*/

                     proses = true;
                     
                //}

                obj.getTreaties().markAllUpdate();

                final DTOList treatyDetails = obj.getTreatyDetails();
                treatyDetails.markAllUpdate();

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    final DTOList share = trdi.getShares();
                    trdi.getShares().markAllUpdate();

                    if(treatyApply.equalsIgnoreCase("COMM")){
                        
                            if(trdi.getTreatyDetail().isQS()){
                                trdi.setDbComissionRate(new BigDecimal(32.5));
                            }else if(trdi.getTreatyDetail().isOR()){
                                trdi.setDbComissionRate(null);
                                trdi.setDbComission(null);
                            }else if(trdi.getTreatyDetail().isBPDAN()){
                                trdi.setDbComissionRate(new BigDecimal(30));
                            }

                            for (int l = 0; l < share.size(); l++) {
                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(l);

                                if(trdi.getTreatyDetail().isQS()){
                                    ri.setDbRICommRate(new BigDecimal(32.5));
                                }else if(trdi.getTreatyDetail().isOR()){
                                    ri.setDbRICommRate(null);
                                    ri.setDbRICommAmount(null);
                                }else if(trdi.getTreatyDetail().isBPDAN()){
                                    ri.setDbRICommRate(new BigDecimal(30));
                                }
                            }
                        
                    }
                }

                if(!treatyApply.equalsIgnoreCase("COMM")){
                        //String treatyAsli = obj.getStInsuranceTreatyID();
                        obj.setStInsuranceTreatyID(null);
                        obj.getTreaties().deleteAll();

                        obj.setStInsuranceTreatyID(treatyApply);
                        //obj.setStInsuranceTreatyID(treatyAsli);
                        //logger.logWarning("######### yuhuu treaty objek "+obj.getStOrderNo() +" "+obj.getStReference1()+" : "+ treatyApply);
                }

            }

            pol.recalculate();
            pol.recalculateTreaty();

            //RESET TREATY
            if(pol.isStatusEndorse())
                pol.recalculateTreatyInitialize();

//            pol.markUpdate();
//            pol.setStRIFinishFlag("Y");
//            pol.setStReinsuranceApprovedWho("14068310");

            getRemoteInsurance().saveReinsuranceOnly(pol, pol.getStNextStatus(), false);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'RI_UPD' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");

        }

    }

    public void clickJurnalUlangPerBulan() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        /*
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy a   "+
                "  where a.status IN ('POLICY','ENDORSE','RENEWAL') and a.active_flag='Y' and a.effective_flag = 'Y'  "+
                "  and date_trunc('day',a.POLICY_DATE) >= '2016-10-01 00:00:00' and date_trunc('day',a.POLICY_DATE) <= '2016-10-31 00:00:00'  "+
                "  and coalesce(admin_notes,'') <> 'JURNAL' order by POLICY_DATE ",
                InsurancePolicyView.class);
        */

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where coalesce(admin_notes,'') <> 'APP' "+
                " and pol_no in ('040430301219000100', "+
                " '040430301219000200', "+
                " '040430301219000300', "+
                " '040430301219000400', "+
                " '040430301219000500', "+
                " '040430301219000600', "+
                " '040430301219000700', "+
                " '040430301219000800', "+
                " '040430301219000900', "+
                " '040430301219001000', "+
                " '040430301219001100', "+
                " '040430301219001200', "+
                " '040430301219001300', "+
                " '040430301219001400', "+
                " '040430301219001500', "+
                " '040430301219001600', "+
                " '040430301219001700', "+
                " '040430301219001800', "+
                " '040430301219001900', "+
                " '040430301219002000', "+
                " '040430301219002100', "+
                " '040430301219002200', "+
                " '040430301219002300', "+
                " '040430301219002400', "+
                " '040430301219002500', "+
                " '040430301219002600', "+
                " '040430301219002700' "+
                " ) order by pol_no,pol_id ",
                InsurancePolicyView.class);
       
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;

            //polis.recalculate();

            getRemoteInsurance().jurnalUlang(polis, polis.getStNextStatus(), true);

            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'APP' where pol_id = ?");

            PS.setObject(1,policy.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");

        }

    }

    /**
     * @return the stTglTahun
     */
    public String getStTglTahun() {
        return stTglTahun;
    }

    /**
     * @param stTglTahun the stTglTahun to set
     */
    public void setStTglTahun(String stTglTahun) {
        this.stTglTahun = stTglTahun;
    }

    /**
     * @return the stUsia
     */
    public String getStUsia() {
        return stUsia;
    }

    /**
     * @param stUsia the stUsia to set
     */
    public void setStUsia(String stUsia) {
        this.stUsia = stUsia;
    }

    /**
     * @return the stKreasiKredit
     */
    public String getStKreasiKredit() {
        return stKreasiKredit;
    }

    /**
     * @param stKreasiKredit the stKreasiKredit to set
     */
    public void setStKreasiKredit(String stKreasiKredit) {
        this.stKreasiKredit = stKreasiKredit;
    }

    /**
     * @return the stJangkaWaktu
     */
    public String getStJangkaWaktu() {
        return stJangkaWaktu;
    }

    /**
     * @param stJangkaWaktu the stJangkaWaktu to set
     */
    public void setStJangkaWaktu(String stJangkaWaktu) {
        this.stJangkaWaktu = stJangkaWaktu;
    }

    public void EXCEL_PAKREASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stKreasi.equalsIgnoreCase("1")) {
            String sqls = "";
            if (getStTglTahun() != null) {
                if (getStTglTahun().equalsIgnoreCase("2")) {
                    sqls = ", EXTRACT(YEAR FROM a.policy_date)  - EXTRACT(YEAR FROM b.refd1) as jmltaon";
                }
            } else if (getStJangkaWaktu() != null) {
                sqls = ", EXTRACT(YEAR FROM b.refd3)  - EXTRACT(YEAR FROM b.refd2) as jmltaon";
            }

            sqa.addSelect(" a.policy_date as tgl_polis, ';'||a.pol_no as pol_no, trim(b.ref1) as nama, b.ref2 as umur, b.refd1 as tgl_lhr, "
                    + " b.refd2 as tgl_cair,b.refd3 as tgl_akhir, "
                    + " getpremi(a.pol_type_id = 21,getpremi(a.status = 'ENDORSE',b.insured_amount,b.refn4),b.insured_amount) as insured_amount,"
                    + " getpremi(a.pol_type_id = 21,b.refn6,b.premi_total) as premi,"
                    + " b.refn5 as rate_premi,a.status, a.cc_code,a.pol_id " + sqls + "");

            sqa.addQuery(" from ins_policy a "
                    + " inner join ins_pol_obj b on a.pol_id = b.pol_id ");

            sqa.addClause("a.pol_type_id in (21,59,80)");
            sqa.addClause("a.active_flag='Y'");
            sqa.addClause("a.effective_flag='Y'");

            if (dtPolicyDateFrom != null) {
                sqa.addClause("date_trunc('day',a.policy_date) >= '" + dtPolicyDateFrom + "'");
                //sqa.addPar(dtPolicyDateFrom);
            }

            if (dtPolicyDateTo != null) {
                sqa.addClause("date_trunc('day',a.policy_date) <= '" + dtPolicyDateTo + "'");
                //sqa.addPar(dtPolicyDateTo);
            }

            if (stPolicy != null) {
                sqa.addClause("a.pol_no like '%" + stPolicy + "%'");
                //sqa.addPar("%"+stPolicy+"%");
            }

            if (stBranch != null) {
                sqa.addClause("a.cc_code = '" + stBranch + "'");
                //sqa.addPar(stBranch);
            }

            if (stName != null) {
                sqa.addClause("to_tsvector('english', b.ref1) @@ to_tsquery('english', '" + stName.replaceAll(" ", "&").replaceAll("&&", "&") + "') ");
                //sqa.addPar(stName.replaceAll(" ","&").replaceAll("&&","&"));
            }

            if (stPolicyLevel != null) {
                sqa.addClause("a.status = '" + stPolicyLevel + "'");
                //sqa.addPar(stPolicyLevel);
            } else {
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
            }

            if (dtBirthDate != null) {
                sqa.addClause("b.refd1 = '" + dtBirthDate + "'");
                //sqa.addPar(dtBirthDate);
            }

            if (stAge != null) {
                sqa.addClause("b.ref2 >= '" + stAge + "'");
                //sqa.addPar(stAge);
            }

            if (dtLiquidityFrom != null) {
                sqa.addClause("date_trunc('day',b.refd2) >= '" + dtLiquidityFrom + "'");
                //sqa.addPar(dtLiquidityFrom);
            }

            if (dtLiquidityTo != null) {
                sqa.addClause("date_trunc('day',b.refd2) <= '" + dtLiquidityTo + "'");
                //sqa.addPar(dtLiquidityTo);
            }

            if (dtEndOfCreditFrom != null) {
                sqa.addClause("date_trunc('day',b.refd3) >= '" + dtEndOfCreditFrom + "'");
                //sqa.addPar(dtEndOfCreditFrom);
            }

            if (dtEndOfCreditTo != null) {
                sqa.addClause("date_trunc('day',b.refd3) <= '" + dtEndOfCreditTo + "'");
                //sqa.addPar(dtEndOfCreditTo);
            }

            // tanggal taun
            if (getStTglTahun() != null) {
                if (getStTglTahun().equalsIgnoreCase("1")) {
                    sqa.addClause(" b.refd1 is null ");
                } else if (getStTglTahun().equalsIgnoreCase("2")) {
                    String sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon > 80 order by a.cc_code,a.pol_no,b.ins_pol_obj_id ";
                    /*
                    final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    HashDTO.class);

                    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

                    return l;

                     */

                } else if (getStTglTahun().equalsIgnoreCase("3")) {
                    sqa.addClause(" b.refd1 =  b.refd2");
                } else if (getStTglTahun().equalsIgnoreCase("4")) {
                    sqa.addClause(" b.refd2 <> b.refd3  ");
                }
            }

            // usia
            if (getStUsia() != null) {
                sqa.addClause("b.ref2 >= '" + getStUsia() + "'");
                //sqa.addPar(getStUsia());
            }

            // jangka waktu
            if (getStJangkaWaktu() != null) {
                if (getStKreasiKredit().equalsIgnoreCase("1")) {
                    String sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon > " + getStJangkaWaktu() + " order by a.cc_code,a.pol_no,b.ins_pol_obj_id";

                    /*
                    final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    HashDTO.class);

                    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

                    return l;
                     */

                } else if (getStKreasiKredit().equalsIgnoreCase("2")) {
                    String sql = " Select * from (" + sqa.getSQL() + " )x where jmltaon < " + getStJangkaWaktu() + " order by a.cc_code,a.pol_no,b.ins_pol_obj_id";
                    /*
                    final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    HashDTO.class);

                    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

                    return l;
                     */
                }

            }

            if (stKategoriDeb != null) {
                sqa.addClause("b.ref7 = '" + stKategoriDeb + "'");
                //sqa.addPar(dtEndOfCreditTo);
            }

            if (stPekerjaanDeb != null) {
                sqa.addClause("b.ref4 = '" + stPekerjaanDeb + "'");
//                    sqa.addPar(stPekerjaanDeb);
            }

        } else if (stKreasi.equalsIgnoreCase("2")) {
            sqa.addSelect(" a.norut, ';'||a.pol_no as pol_no, a.nama, a.umur, a.tgl_lhr, a.tgl_cair, a.tgl_akhir, "
                    + "a.insured_amount, a.rate_premi, a.premi, a.cc_code ");

            sqa.addQuery(" from aba_kreasi a ");

            if (stPolicy != null) {
                sqa.addClause("a.pol_no like '%" + stPolicy + "%'");
                //sqa.addPar(stPolicy);
            }

            if (stBranch != null) {
                sqa.addClause(
                        "a.cc_code = '" + stBranch + "'");
                //sqa.addPar(stBranch);
            }

            if (stName != null) {
                sqa.addClause(
                        "to_tsvector('english', a.nama) @@ to_tsquery('english', '" + stName.replaceAll(" ", "&").replaceAll("&&", "&") + "') ");

                //sqa.addPar(stName.replaceAll(" ","&").replaceAll("&&","&"));
            }

            if (stPolicyLevel != null) {
                sqa.addClause(
                        "a.status = '" + stPolicyLevel + "'");
                //sqa.addPar(stPolicyLevel);
            }

            if (dtBirthDate != null) {
                sqa.addClause(
                        "a.tgl_lhr = '" + dtBirthDate + "'");
                //sqa.addPar(dtBirthDate);
            }

            if (dtLiquidityFrom != null) {
                sqa.addClause(
                        "a.tgl_cair = '" + dtLiquidityFrom + "'");
                //sqa.addPar(dtLiquidityFrom);
            }

            if (dtEndOfCreditFrom != null) {
                sqa.addClause(
                        "a.tgl_akhir = '" + dtPolicyDateFrom + "'");
                //sqa.addPar(dtEndOfCreditFrom);
            }
        }

        String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "pakreasilist_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         *
         */
    }

    public void EXPORT_PAKREASI()  throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //CREATE SHEET INDUK
        XSSFSheet sheet = wb.createSheet("INDUK");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("INDUK");


        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("pol_no");
        row1.createCell(1).setCellValue("policy_date");
        row1.createCell(2).setCellValue("period_start");
        row1.createCell(3).setCellValue("treaty_type");
        row1.createCell(4).setCellValue("insured_amount");
        row1.createCell(5).setCellValue("ref1");
        row1.createCell(6).setCellValue("tanggal_awal");
        row1.createCell(7).setCellValue("tanggal_akhir");

        row1.createCell(8).setCellValue("rate");
        row1.createCell(9).setCellValue("ins_pol_tre_det_id");
        row1.createCell(10).setCellValue("ins_pol_treaty_id");
        row1.createCell(11).setCellValue("ins_treaty_detail_id");
        row1.createCell(12).setCellValue("tsi_amount");
        row1.createCell(13).setCellValue("premi_amount");
        row1.createCell(14).setCellValue("comm_rate");
        row1.createCell(15).setCellValue("comm_amt");
        row1.createCell(16).setCellValue("tsi_pct");
        row1.createCell(17).setCellValue("premi_rate");
        row1.createCell(18).setCellValue("edit_flag");

        HashDTO h2 = (HashDTO) list.get(0);
        String nomorPolis = h2.getFieldValueByFieldNameST("pol_no");

        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tanggal_awal"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tanggal_akhir"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ins_pol_treaty_id"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ins_treaty_detail_id"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_amt").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_pct").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("edit_flag"));

        }

        //CREATE SHEET MEMBER
        XSSFSheet sheetMember = wb.createSheet("MEMBER");

        final DTOList listMember = (DTOList) SessionManager.getInstance().getRequest().getAttribute("MEMBER");


        XSSFRow row1Member = sheetMember.createRow(0);
        row1Member.createCell(0).setCellValue("pol_no");
        row1Member.createCell(1).setCellValue("ref1");
        row1Member.createCell(2).setCellValue("tgl_awal");
        row1Member.createCell(3).setCellValue("tgl_akhir");
        row1Member.createCell(4).setCellValue("treaty_type");
        row1Member.createCell(5).setCellValue("ent_name");
        row1Member.createCell(6).setCellValue("insured_amount");
        row1Member.createCell(7).setCellValue("premi_new");

        row1Member.createCell(8).setCellValue("premi_total");
        row1Member.createCell(9).setCellValue("ins_pol_ri_id");
        row1Member.createCell(10).setCellValue("ins_pol_treaty_id");
        row1Member.createCell(11).setCellValue("ins_pol_tre_det_id");
        row1Member.createCell(12).setCellValue("ins_treaty_detail_id");
        row1Member.createCell(13).setCellValue("ins_treaty_shares_id");
        row1Member.createCell(14).setCellValue("member_ent_id");
        row1Member.createCell(15).setCellValue("sharepct");
        row1Member.createCell(16).setCellValue("premi_amount");
        row1Member.createCell(17).setCellValue("tsi_amount");
        row1Member.createCell(18).setCellValue("premi_rate");
        row1Member.createCell(19).setCellValue("auto_rate_flag");
        row1Member.createCell(20).setCellValue("use_rate_flag");
        row1Member.createCell(21).setCellValue("ricomm_rate");
        row1Member.createCell(22).setCellValue("ricomm_amt");
        row1Member.createCell(23).setCellValue("notes");
        row1Member.createCell(24).setCellValue("ri_slip_no");
        row1Member.createCell(25).setCellValue("f_approve");
        row1Member.createCell(26).setCellValue("valid_ri_date");

        for (int j=0;j< listMember.size() ; j++){
            HashDTO hMember = (HashDTO) listMember.get(j);

            XSSFRow row = sheetMember.createRow(j+1);
            row.createCell(0).setCellValue(hMember.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(hMember.getFieldValueByFieldNameST("ref1"));
            row.createCell(2).setCellValue(hMember.getFieldValueByFieldNameDT("tanggal_awal"));
            row.createCell(3).setCellValue(hMember.getFieldValueByFieldNameDT("tanggal_akhir"));
            row.createCell(4).setCellValue(hMember.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(5).setCellValue(hMember.getFieldValueByFieldNameST("ent_name"));
            row.createCell(6).setCellValue(hMember.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(7).setCellValue(hMember.getFieldValueByFieldNameBD("premi_new").doubleValue());
            row.createCell(8).setCellValue(hMember.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(9).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_ri_id").doubleValue());
            row.createCell(10).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_treaty_id").doubleValue());
            row.createCell(11).setCellValue(hMember.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(12).setCellValue(hMember.getFieldValueByFieldNameBD("ins_treaty_detail_id").doubleValue());
            row.createCell(13).setCellValue(hMember.getFieldValueByFieldNameBD("ins_treaty_shares_id").doubleValue());
            row.createCell(14).setCellValue(hMember.getFieldValueByFieldNameBD("member_ent_id").doubleValue());
            row.createCell(15).setCellValue(hMember.getFieldValueByFieldNameBD("sharepct").doubleValue());
            row.createCell(16).setCellValue(hMember.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            row.createCell(17).setCellValue(hMember.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(18).setCellValue(hMember.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            row.createCell(19).setCellValue(hMember.getFieldValueByFieldNameST("auto_rate_flag"));
            row.createCell(20).setCellValue(hMember.getFieldValueByFieldNameST("use_rate_flag"));
            row.createCell(21).setCellValue(hMember.getFieldValueByFieldNameBD("ricomm_rate").doubleValue());
            row.createCell(22).setCellValue(hMember.getFieldValueByFieldNameBD("ricomm_amt").doubleValue());
            row.createCell(23).setCellValue(hMember.getFieldValueByFieldNameST("notes"));
            row.createCell(24).setCellValue(hMember.getFieldValueByFieldNameST("ri_slip_no"));
            row.createCell(25).setCellValue(hMember.getFieldValueByFieldNameST("f_approve"));
            row.createCell(26).setCellValue(hMember.getFieldValueByFieldNameDT("valid_ri_date"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename="+ nomorPolis +".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
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
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    /**
     * @return the canApproveReinsOnly
     */
    public boolean isCanApproveReinsOnly() {
        return canApproveReinsOnly;
    }

    /**
     * @param canApproveReinsOnly the canApproveReinsOnly to set
     */
    public void setCanApproveReinsOnly(boolean canApproveReinsOnly) {
        this.canApproveReinsOnly = canApproveReinsOnly;
    }

    /**
     * @return the enablePrintDigitalPolicy
     */
    public boolean isEnablePrintDigitalPolicy() {
        return enablePrintDigitalPolicy;
    }

    /**
     * @param enablePrintDigitalPolicy the enablePrintDigitalPolicy to set
     */
    public void setEnablePrintDigitalPolicy(boolean enablePrintDigitalPolicy) {
        this.enablePrintDigitalPolicy = enablePrintDigitalPolicy;
    }

    public void clickChangeKlaimTreatyByDate() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+ 
                "   from ins_policy "+
                " where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and active_flag='Y' AND effective_flag = 'Y' AND coalesce(admin_notes,'') <> '0421'   "+
                " and pol_type_id in (21,59,80,73,74) and date_trunc('day',dla_date) >= '2021-04-01 00:00:00' and date_trunc('day',dla_date) <= '2021-04-30 00:00:00'  "+
                 " order by pol_id "
                ,InsurancePolicyView.class);


        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            DTOList objects = pol.getObjects();

            InsurancePolicyObjectView objx = (InsurancePolicyObjectView) objects.get(0);

            pol.setClaimObject(objx);

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                //015923230613005300
                //012345678901234567
                DTOList treaties = ListUtil.getDTOListFromQuery(
                        " select c.* "+
                        " from ins_policy a "+
                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                        " inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "+
                        " where A.POL_NO = ? and (b.order_no = ? or b.ref1 = ?) and a.status in ('POLICY','ENDORSE','RENEWAL','HISTORY')",
                        new Object [] {pol.getStPolicyNo().substring(0, 16) + "00", obj.getStOrderNo(), obj.getStReference1() },
                        InsurancePolicyTreatyView.class
                        );

                InsurancePolicyTreatyView treaty = (InsurancePolicyTreatyView) treaties.get(0);

                if(treaty==null){
                    DTOList treaties2 = ListUtil.getDTOListFromQuery(
                        " select c.ins_poL_treaty_id,coalesce(c.ins_treaty_id,b.ins_treaty_id) as ins_treaty_id,c.policy_id,c.ins_pol_obj_id "+
                        " from ins_policy a "+
                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                        " left join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "+
                        " where substr(A.POL_NO,0,17) = ? and (b.order_no = ? or b.ref1 = ?) and a.status in ('POLICY','ENDORSE','RENEWAL','HISTORY') order by a.pol_id asc limit 1",
                        new Object [] {pol.getStPolicyNo().substring(0, 16), obj.getStOrderNo(), obj.getStReference1() },
                        InsurancePolicyTreatyView.class
                        );

                     treaty = (InsurancePolicyTreatyView) treaties2.get(0);
                }

                String treatyApplied = treaty.getStInsuranceTreatyID();

                obj.getTreaties().markAllUpdate();

                final DTOList treatyDetails = obj.getTreatyDetails();
                treatyDetails.markAllUpdate();

                DTOList objv = policy.getObjectsByID(treaty.getStInsurancePolicyObjectID());
                InsurancePolicyObjectView objz = (InsurancePolicyObjectView) objv.get(0);

                int size = objz.getTreatyDetails().size();

                boolean orOnly = true;

                final DTOList treatyDetailsInduk = objz.getTreatyDetails();
                for (int l = 0; l < treatyDetailsInduk.size(); l++) {
                    InsurancePolicyTreatyDetailView trdiInduk = (InsurancePolicyTreatyDetailView) treatyDetailsInduk.get(l);

                    if(!trdiInduk.getTreatyDetail().isOR()) orOnly = false;

                }

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    final DTOList share = trdi.getShares();
                    trdi.getShares().markAllUpdate();
                }

                obj.setStInsuranceTreatyID(null);
                obj.getTreaties().deleteAll();

                if(size <= 0){
                    obj.setStInsuranceTreatyID("36");
                }else if(orOnly){
                    obj.setStInsuranceTreatyID("36");
                }else{
                    obj.setStInsuranceTreatyID(treatyApplied);
                }

            }

            pol.recalculate();

            pol.recalculateTreaty();

            pol.recalculateClaim();

            //RESET TREATY

            //.setStRIFinishFlag("Y");
            //pol.setStReinsuranceApprovedWho("14068310");
            //

            getRemoteInsurance().saveReinsuranceOnly(pol, pol.getStNextStatus(), false);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = '0421' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");

        }

    } 
    
     private String getUangMukaKlaimInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='ADVPAYMENT'");

        return lu.getCode(0); 
    }

    public void clickChangeTreatyOnlyByDate() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        /*
        listPolicy = ListUtil.getDTOListFromQuery(
                " select a.* "+
                " from ins_policy a "+
                " where a.status IN ('POLICY','RENEWAL') and a.active_flag='Y' and a.effective_flag = 'Y' "+
                " and date_trunc('day',a.POLICY_DATE) >= '2014-08-01 00:00:00' and date_trunc('day',a.POLICY_DATE) <= '2014-08-31 00:00:00' "+
                " and a.pol_type_id IN ('1') and ri_approved_who is null and coalesce(admin_notes,'') <> 'TREATY_BPPDAN_04092014' order by a.pol_id",
               InsurancePolicyView.class);
        */

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from proses_bppdan "+
                " where flag is null "+
                " order by pol_no",
               InsurancePolicyObjDefaultView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyObjDefaultView policy = (InsurancePolicyObjDefaultView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            DTOList objects = pol.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                //if(!obj.getStOrderNo().equalsIgnoreCase(policy.getStOrderNo())) continue;

                //obj.getTreaties().markAllUpdate();
                obj.getTreaties();

                final DTOList treatyDetails = obj.getTreatyDetails();
                //treatyDetails.markAllUpdate();

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    //final DTOList share = trdi.getShares();
                    //trdi.getShares().markAllUpdate();

                    if(trdi.getTreatyDetail().isBPDAN()){
                        trdi.getShares().markAllUpdate();
                        trdi.getShares().deleteAll();
                        trdi.initSharesProses(pol, obj);
                    }

                }

                //obj.setStInsuranceTreatyID(null);
                //obj.getTreaties().deleteAll();

                //obj.setStInsuranceTreatyID(treatyApply);
            }

            pol.recalculate();
            pol.recalculateTreaty();

            //RESET TREATY
            //if(pol.isStatusEndorse())
                pol.recalculateTreatyInitialize();
            //.setStRIFinishFlag("Y");
            //pol.setStReinsuranceApprovedWho("14068310");
            //

            getRemoteInsurance().saveReinsuranceOnly(pol, pol.getStNextStatus(), false);
            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update proses_bppdan set flag = 'Y' where pol_id = ?");

            PS.setObject(1,pol.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");

        }

    }

    public void EXCEL_VEHICLE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_no as nopol,b.ref1 as nopolisi,b.ref3 as tahun,b.ref4 as rangka,b.ref5 as mesin,"
                + "b.ref7 as penggunaan,b.ref9 as nama ");

        sqa.addQuery(
                "         from "
                + "         ins_policy a    "
                + "inner join ins_pol_obj b on a.pol_id = b.pol_id ");

        sqa.addFilter(vehicle.getFilter());

        if (stPolicy != null) {
            sqa.addClause("a.pol_no like '%" + stPolicy + "%'");
            //sqa.addPar("%"+stPolicy+"%");
        }

        if (stPolicyLevel != null) {
            sqa.addClause("a.status = '" + stPolicyLevel + "'");
            //sqa.addPar(stPolicyLevel);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stPoliceNo != null) {
            sqa.addClause(" b.ref1 like '%" + stPoliceNo.toUpperCase() + "%'");
            //sqa.addPar("%" + stPoliceNo.toUpperCase() + "%");
        }

        if (stYearBuilt != null) {
            sqa.addClause(" b.ref3 like '%" + stYearBuilt.toUpperCase() + "%'");
            //sqa.addPar("%" + stYearBuilt.toUpperCase() + "%");
        }

        if (stChassisNo != null) {
            sqa.addClause(" b.ref4 like '%" + stChassisNo.toUpperCase() + "%'");
            //sqa.addPar("%" + stChassisNo.toUpperCase() + "%");
        }

        if (stMachineNo != null) {
            sqa.addClause(" b.ref5 like '%" + stMachineNo.toUpperCase() + "%'");
            //sqa.addPar("%" + stMachineNo.toUpperCase() + "%");
        }

        if (stUsage != null) {
            sqa.addClause(" b.ref7 like '%" + stUsage.toUpperCase() + "%'");
            //sqa.addPar("%" + stUsage.toUpperCase() + "%");
        }

        if (stTypeOfVehicle != null) {
            sqa.addClause(" b.ref2 like '%" + stTypeOfVehicle.toUpperCase() + "%'");
            //sqa.addPar("%" + stTypeOfVehicle.toUpperCase() + "%");
        }

        if (stName != null) {
            sqa.addClause(" b.ref9 like '%" + stName.toUpperCase() + "%'");
            //sqa.addPar("%" + stName.toUpperCase() + "%");
        }

        String sql = sqa.getSQL() + " order by a.pol_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "vehiclelist_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         *
         */
    }

    public void clickChangePassword() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from s_users "+
                " where branch = '33' and password is null order by user_id",
               UserSessionView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            UserSessionView policy = (UserSessionView) listPolicy.get(i);

            String password = Digest.computeDigest(policy.getStUserID(),policy.getStUserID());

            PreparedStatement PS = S.setQuery("update s_users set password = ? where user_id = ?");

            PS.setObject(1,password);
            PS.setObject(2,policy.getStUserID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE password user id : "+ policy.getStUserID() +" ++++++++++++++++++");

        }

    }

    private String stGatewayData;

    /**
     * @return the stGatewayData
     */
    public String getStGatewayData() {
        return stGatewayData;
    }

    /**
     * @param stGatewayData the stGatewayData to set
     */
    public void setStGatewayData(String stGatewayData) {
        this.stGatewayData = stGatewayData;
    }


    public void tesKoneksi() throws Exception {

        //if(policy.isDataGateway()){

                final SQLUtil S2 = new SQLUtil("GATEWAY");

                try{

                    PreparedStatement PS = S2.setQuery("select * from data_teks_masuk limit 5 ");

                    PS.executeQuery();

                    //S2.release();

                }catch (Exception e) {

                    //ctx.setRollbackOnly();

                    throw e;

                } finally{
                    logger.logWarning("############### masuk finally nih");
                    //S2.release();
                }
        //}

    }

    /**
     * @return the canApproveAnalisaResiko
     */
    public boolean isCanApproveAnalisaResiko() {
        return canApproveAnalisaResiko;
    }

    /**
     * @param canApproveAnalisaResiko the canApproveAnalisaResiko to set
     */
    public void setCanApproveAnalisaResiko(boolean canApproveAnalisaResiko) {
        this.canApproveAnalisaResiko = canApproveAnalisaResiko;
    }

    public void clickApprovalAnalisaResiko() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.approvalRisk(policyID);

        //form.setApprovalMode(true);
        form.setRiskApprovalMode(true);

        form.show();
    }

    public void clickCreatePolisTrial() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.editCreatePolis(policyID);
        form.setReadOnly(true);
        form.setBentukPolisMode(true);

        form.show();
    }

    /**
     * @return the stShowDataReject
     */
    public String getStShowDataReject() {
        return stShowDataReject;
    }

    /**
     * @param stShowDataReject the stShowDataReject to set
     */
    public void setStShowDataReject(String stShowDataReject) {
        this.stShowDataReject = stShowDataReject;
    }

    /**
     * @return the listPolicyChecking
     */
    public DTOList getListPolicyChecking() throws Exception {
        if (listPolicyChecking==null) {
            listPolicyChecking=new DTOList();
            final Filter filter = new Filter();
            listPolicyChecking.setFilter(filter.activate());
        }

        final SQLAssembler sqa = new SQLAssembler();

        if (stPolicyNo!=null){
            if(verifikasiCaptcha()){

                    String Sql = "   from " +
                    "      (" +
                    "         select " +
                    "               a.*,"+
                    "               b.description as policy_type_desc" +
                    "         from " +
                    "            ins_policy a" +
                    "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id";


                     Sql = Sql + " ) a";

                    sqa.addQuery(Sql);

                    String select = "";

                    select = select + " *,"+
                            " (select user_name"+
                            " from s_users"+
                            " where user_id = a.create_who limit 1) as create_name,"+
                            " (select user_name"+
                            " from s_users"+
                            " where user_id = a.approved_who limit 1) as approved_name,"+
                            " (select user_name"+
                            " from s_users"+
                            " where user_id = a.ri_approved_who limit 1) as approved_reins_name ";

                    sqa.addSelect(select);

                    sqa.addFilter(listPolicyChecking.getFilter());

                    if (Tools.isYes(stOutstandingFlag) && !Tools.isYes(stShowDataReject)) {
                        sqa.addClause("a.active_flag='Y'");
                    }

                    if (Tools.isYes(stNotApprovedPolicy) || Tools.isYes(stShowClaimsNotApproved)) {
                        sqa.addClause("coalesce(a.effective_flag,'N') <> 'Y'");
                    }

                    if (statusFilter!=null) {
                        if (Tools.isYes(stNotApprovedPolicy)){
                            sqa.addClause(" status in (?,?,?)");
                            sqa.addPar("POLICY");
                            sqa.addPar("ENDORSE");
                            sqa.addPar("RENEWAL");
                        }else if (Tools.isYes(stShowClaimsNotApproved) || Tools.isYes(stShowClaimsOnly)){
                            sqa.addClause(" status in (?,?)");
                            sqa.addPar("CLAIM");
                            sqa.addPar("CLAIM ENDORSE");
                        }else{
                            sqa.addClause(SQLUtil.exprANY("status",statusFilter));

                            for (int i = 0; i < statusFilter.size(); i++) {
                                String s = (String) statusFilter.get(i);

                                sqa.addPar(s);
                            }
                        }
                    }

                    if (stPolicyNo!=null) {
                        sqa.addClause("a.pol_no like ?");
                        sqa.addPar("%"+stPolicyNo+"%");
                    }

                    if (stPolicyCustomer!=null) {
                        sqa.addClause("upper(a.cust_name) like ?");
                        sqa.addPar("%"+ stPolicyCustomer.toUpperCase() +"%");
                    }

                    if (stPolicyNo==null)
                        sqa.setLimit(0);

                        listPolicyChecking = sqa.getList(InsurancePolicyView.class);
            }else{
                //jika captcha salah
                sqa.addSelect("'CAPTCHA ANDA SALAH'::character varying as pol_no,'WRONG'::character varying as cust_name");
                sqa.addQuery("from ins_policy");
                sqa.setLimit(1);

                listPolicyChecking = sqa.getList(InsurancePolicyView.class);
            }
        }


        return listPolicyChecking;
    }

    /**
     * @param listPolicyChecking the listPolicyChecking to set
     */
    public void setListPolicyChecking(DTOList listPolicyChecking) {
        this.listPolicyChecking = listPolicyChecking;
    }

 public void cekpolicy() {
        //setTitle("POLIS CHECKING (Surety Bond & Kontra Bank Garansi)");
        printingLOV = "UWRIT";
        setUseHeader(false);

        canEdit = hasResource("POL_UWRIT_EDIT");
        canCreate = hasResource("POL_UWRIT_CREATE");
        canApprove = hasResource("POL_UWRIT_APRV");
        enablePrint = hasResource("POL_UWRIT_PRINT");
        canCreatePolicyHistory = hasResource("POL_UWRIT_CREATE_HISTORY");
        approveUW = hasResource("POL_UWRIT_APR_UW");
        enableInputPaymentDate = hasResource("POL_UWRIT_PAYMENT_DATE");

        statusFilter = array2list(new String [] {
            FinCodec.PolicyStatus.POLICY,
            FinCodec.PolicyStatus.ENDORSE,
            FinCodec.PolicyStatus.RENEWAL,
        });

        //enableCreateSPPA=canCreate;
        enableCreatePolis=canCreate;
        enableEndorsement=canCreate;
        enableRenewal=canCreate;

        editFilter.add(FinCodec.PolicyStatus.POLICY);
        editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        editFilter.add(FinCodec.PolicyStatus.RENEWAL);

        canNavigateBranch = hasResource("POL_UWRIT_NAVBR");

        enableEdit = canEdit;
        policy = true;
    }

    public void refreshCekPolis() {
        setShowFilter(true);
    }
     


    public boolean verifikasiCaptcha() throws Exception{
        // get reCAPTCHA request param
          String gRecaptchaResponse = SessionManager.getInstance().getRequest().getParameter("g-recaptcha-response");
          System.out.println(gRecaptchaResponse);
          boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);

          return verify;
          //if(!verify) throw new RuntimeException("Verifikasi Captcha yang anda input salah");

    }

    public void onNewDetailAutomatic(InsurancePolicyView pol, String insItemID, String stReadOnly, String entityID, BigDecimal pct, String taxCode) throws Exception {

        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(insItemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStROFlag(stReadOnly);

        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        item.setDbRate(pct);
        item.setStFlagEntryByRate("Y");

        if(taxCode!=null)
            item.setStTaxCode(taxCode);

        if(entityID!=null) item.setStEntityID(entityID);

        pol.getDetails().add(item);

    }

    public void onNewDetailAutomatic2(InsurancePolicyView pol,String insItemID, String stReadOnly, String entityID, BigDecimal pct, String taxCode) throws Exception {

        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStROFlag(stReadOnly);

        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        item.setDbRate(pct);
        item.setStFlagEntryByRate("Y");

        if(taxCode!=null)
            item.setStTaxCode(taxCode);

        if(entityID!=null) item.setStEntityID(entityID);

        pol.getDetails().add(item);

    }

    public InsuranceItemsView getInsItemCat(String stInsItemID) {
        return (InsuranceItemsView) DTOPool.getInstance().getDTO(InsuranceItemsView.class, stInsItemID);
    }

    public void createEndorsemenByDATA() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                 " from proses_endorse_sby "+
                " where admin_notes is null",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView pol2 = (InsurancePolicyView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND SUBSTR(POL_NO,0,17) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo().substring(0, 16)},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateEndorseKeterangan(polis);

                form.btnSaveEndorsePPN();

                PreparedStatement PS = S.setQuery("update proses_endorse_sulselbar set admin_notes = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");


        }

        S.release();

    }

    public void clickApprovalByDivisi() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.approvalByDivisi(policyID);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickEditAndApproveKumpulan() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                     " select * "+
                     " from ins_policy a  "+
                     " where status = 'ENDORSE' and pol_id in (  "+
                     " select pol_id from proses_bandung where admin_notes is null  "+
                     " )"+
                     " order by a.pol_id",
                InsurancePolicyView.class); 

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;

            //polis.markUpdateO();
            //polis.getDetails().markAllUpdate();

            //polis.setStPostedFlag("Y");
            //polis.setStEffectiveFlag("Y");
            //polis.setStApprovedWho("05780189");
            //polis.setDtApprovedDate(new Date());
            //polis.setStPassword(UserManager.getInstance().getUser().getStPasswd());
            //polis.setStClientIP("192.168.200.54");
           // polis.setStReadyToApproveFlag("Y");

            polis.recalculate();

            getRemoteInsurance().saveAfterReverse(polis,polis.getStNextStatus(),true);

            logger.logInfo("+++++++ approve POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'Y' where pol_id = ?");

            PS.setObject(1,policy.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");


        }

    }

    public void clickEditKumpulan() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where active_flag = 'Y' and pla_no in ( "+
                "         select pla_no "+
                "         from proses_klaim "+
                " ) and coalesce(admin_notes,'') <> 'KLM0719' order by POL_ID",
                InsurancePolicyView.class);
 
        /*
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where pol_id = 5988515",
                InsurancePolicyView.class);*/

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;

            final DTOList objects = polis.getObjects();

            InsurancePolicyObjectView objx = (InsurancePolicyObjectView) objects.get(0);

            polis.setClaimObject(objx);

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                obj.getTreaties().markAllUpdate();

                final DTOList treatyDetails = obj.getTreatyDetails();
                treatyDetails.markAllUpdate();

                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    trdi.getShares().markAllUpdate();
                }

            }

            polis.recalculate();
            polis.recalculateTreaty();
            polis.recalculateClaim(); 

            getRemoteInsurance().saveReinsuranceOnly(polis,polis.getStNextStatus(),false);

            logger.logInfo("+++++++ approve POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update ins_policy set admin_notes = 'KLM0719' where pol_id = ?");

            PS.setObject(1,policy.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");


        }

    }

    public void clickProsesPolisToInterkoneksi() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        /*
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy a   "+
                "  where a.status IN ('POLICY','ENDORSE','RENEWAL') and a.active_flag='Y' and a.effective_flag = 'Y'  "+
                "  and date_trunc('day',a.POLICY_DATE) >= '2016-10-01 00:00:00' and date_trunc('day',a.POLICY_DATE) <= '2016-10-31 00:00:00'  "+
                "  and coalesce(admin_notes,'') <> 'JURNAL' order by POLICY_DATE ",
                InsurancePolicyView.class);
        */

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where pol_no in ( "+
                " select pol_no "+
                " from proses_h2h_jateng where status is null) ",
                InsurancePolicyView.class); 

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;

            //polis.recalculate();

            //getRemoteInsurance().saveDataToGateway(polis);

            logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update proses_h2h_jateng set status = 'Y' where pol_no = ?");

            PS.setObject(1,policy.getStPolicyNo());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");

        }

    }

    public void clickEditPolisKumpulan() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                     " from ins_policy a  "+
                     " where status = 'ENDORSE' and pol_id in (  "+
                     " select pol_id from proses_bandung where admin_notes is null  "+
                     " )"+
                     " order by a.pol_id",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            final InsurancePolicyView polis = (InsurancePolicyView) pol;

            polis.markUpdateO();

            final DTOList objects = polis.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                obj.getSuminsureds().markAllUpdate();
                obj.getCoverage().markAllUpdate();

                obj.markUpdate();

            }

            polis.getCoins2().markAllUpdate();
            polis.getDetails().markAllUpdate();

            polis.getInstallment().markAllUpdate();
            polis.getCoinsCoverage().markAllUpdate();

            polis.recalculate();

            getRemoteInsurance().save(polis,polis.getStNextStatus(),false);

            logger.logInfo("+++++++ approve POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            PreparedStatement PS = S.setQuery("update proses_bandung set admin_notes = 'Y' where pol_id = ?");

            PS.setObject(1,policy.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");


        }

    }

    public void createEndorsemenRIByDATA() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                " from proses_endorse_ri "+
                " where flag is null "+
                " order by pol_no",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView pol2 = (InsurancePolicyView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND POL_NO = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo()},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateEndorseRIProses(polis.getStPolicyID(),"");

                form.btnSaveProsesEndorseRI();

                PreparedStatement PS = S.setQuery("update proses_endorse_ri set flag = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");


        }

        S.release();

    }

    public void reasInward() {
        setTitle("REINSURANCE / INWARD");

        printingLOV = "REAS";

        canEdit = hasResource("POL_RI_EDIT");
        canCreate = hasResource("POL_RI_CREATE");
        canApprove = hasResource("POL_RI_APRV");
        enablePrint = hasResource("POL_RI_PRINT");
        canNavigateBranch = hasResource("POL_RI_NAVBR");
        canNavigateRegion = hasResource("POL_RI_NAVREG");
        canReverseReins = hasResource("REVERSE_RI");
        canApproveReins = hasResource("APPROVE_RI");
        canApproveReinsOnly = hasResource("APPROVE_RI_ONLY");
        enablePrintPreSign = true;

        statusFilter = array2list(new String [] {
            //FinCodec.PolicyStatus.SPPA,
            //FinCodec.PolicyStatus.POLICY,
            //FinCodec.PolicyStatus.ENDORSE,
            //FinCodec.PolicyStatus.RENEWAL,
            //FinCodec.PolicyStatus.ENDORSERI,
            //FinCodec.PolicyStatus.HISTORY,
            FinCodec.PolicyStatus.INWARD,
        });

        reas = true;
        enableEdit = canEdit;

        //editFilter.add(FinCodec.PolicyStatus.SPPA);
        //editFilter.add(FinCodec.PolicyStatus.POLICY);
        //editFilter.add(FinCodec.PolicyStatus.ENDORSE);
        //editFilter.add(FinCodec.PolicyStatus.RENEWAL);
        //editFilter.add(FinCodec.PolicyStatus.ENDORSERI);
        //editFilter.add(FinCodec.PolicyStatus.HISTORY);
        editFilter.add(FinCodec.PolicyStatus.INWARD);
    }

    public void clickCreateClaimInward() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.editCreateClaimInward(policyID);

        form.show();
    }

    public void clickCheck() throws Exception {
        final PolicyForm form = (PolicyForm) super.newForm("policyobj_check", this);

        form.createNewCheck();

        form.show();
    }

    public void clickApprovalClaimByDivision() throws Exception {
        validatePolicyID();

        String form_name = "policy_edit";
        if(printingLOV.equalsIgnoreCase("CLAIM")) form_name = "policy_edit_claim";
        if(printingLOV.equalsIgnoreCase("REAS")) form_name = "policy_edit_reinsurance";
        final PolicyForm form = (PolicyForm)newForm(form_name,this);

        form.approvalByDivisiClaim(policyID);
        form.setRiskApprovalMode(true);

        //form.setApprovalMode(true);

        form.show();
    }

    public String getStKategoriDeb() {
        return stKategoriDeb;
    }

    /**
     * @param stKategoriDeb the stKategoriDeb to set
     */
    public void setStKategoriDeb(String stKategoriDeb) {
        this.stKategoriDeb = stKategoriDeb;
    }

    /**
     * @return the stPekerjaanDeb
     */
    public String getStPekerjaanDeb() {
        return stPekerjaanDeb;
    }

    /**
     * @param stPekerjaanDeb the stPekerjaanDeb to set
     */
    public void setStPekerjaanDeb(String stPekerjaanDeb) {
        this.stPekerjaanDeb = stPekerjaanDeb;
    }

    public void createEndorsemenByDATA2() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select pol_no "+
                 " from proses_endorse_mantap "+
                " where flag is null group by pol_no order by pol_no",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView pol2 = (InsurancePolicyView) listPolicy.get(i);

                DTOList listObjek = ListUtil.getDTOListFromQuery(
                "select * "+
                 " from proses_endorse_sby "+
                " where pol_no = ?",
                new Object[]{pol2.getStPolicyNo()},
                InsurancePolicyView.class);

                PolicyForm form = new PolicyForm();

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND SUBSTR(POL_NO,0,17) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo().substring(0, 16)},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateEndorseBatalTotalObjek(polis, listObjek);

                form.btnSave();

                PreparedStatement PS = S.setQuery("update proses_endorse_sby set admin_notes = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");

        }

        S.release();

    }

    public void createEndorsemenKomisiByData() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                 " from proses_endorse_mantap "+
                " where flag is null order by pol_no",
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView endorse = (InsurancePolicyView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND SUBSTR(POL_NO,0,17) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{endorse.getStPolicyNo().substring(0, 16)},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateEndorseBentukKomisi(polis, endorse);

                form.btnSaveEndorsePPN();

                PreparedStatement PS = S.setQuery("update proses_endorse_mantap set flag = 'Y' where pol_no = ?");

                PS.setObject(1,endorse.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ endorse.getStPolicyNo() +" ++++++++++++++++++");

        }

        S.release();

    }

    public void clickSplitProcess() throws Exception {

        //dapetin polis nya
        final InsuranceSplitPolicyView policy = getRemoteInsurance().getInsuranceSplitPolicy(policyID);

        if(!policy.isEffective())
            throw new RuntimeException("Data upload belum disetujui, setujui dulu untuk proses split");

        PolicySplitForm form = new PolicySplitForm();

        form.createPolisSplit(policy);

    }

    public void clickPrint() throws Exception{
        EXCEL_DETAILS();
        EXPORT_DETAILS();
    }

    public DTOList EXCEL_DETAILS() throws Exception {

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("*");

    sqa.addQuery(" from ins_split_pol_obj  ");

    sqa.addClause("pol_id = ?");
    sqa.addPar(this.policyID);

    sqa.addOrder("ins_pol_obj_id");


    DTOList l = ListUtil.getDTOListFromQuery(sqa.getSQL(), sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_DETAILS() throws Exception {
      
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    for (int i = 0; i < list.size(); i++){
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row0 = sheet.createRow(2);
      row0.createCell(0).setCellValue("no");
      row0.createCell(1).setCellValue("nama objek");
      row0.createCell(2).setCellValue("no referensi");
      row0.createCell(3).setCellValue("status proses");

      XSSFRow row = sheet.createRow(i + 3);

      row.createCell(0).setCellValue(i+1);
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("ref4"));

      String status = "N";

      if(h.getFieldValueByFieldNameST("status")!=null)
          if(h.getFieldValueByFieldNameST("status").equalsIgnoreCase("1"))
              status = "Y";

      row.createCell(3).setCellValue(status);

    }

    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=splitdetails_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();

  }

}




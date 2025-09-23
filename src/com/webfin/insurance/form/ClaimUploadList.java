/***********************************************************************
 * Module:  com.webfin.gl.form.GLJournalList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.model.Filter;
import com.crux.common.parameter.Parameter;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.UploadClaimHeaderView;
import com.webfin.insurance.model.UploadEndorseFireHeaderView;
import com.webfin.insurance.model.UploadEndorseHeaderView;
import com.webfin.insurance.model.UploadSubrogasiHeaderView;
import com.webfin.insurance.model.uploadClaimDetailView;
import com.webfin.insurance.model.uploadEndorseDetailView;
import com.webfin.insurance.model.uploadEndorseFireDetailView;
import com.webfin.insurance.model.uploadSubrogasiDetailView;
import java.io.File;
import java.math.BigDecimal;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;

public class ClaimUploadList extends Form {

    private DTOList list;
    private String memorialID;
    private String description;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("CLAIM_UPL_NAVBR");
    private boolean canEdit = SessionManager.getInstance().getSession().hasResource("CLAIM_UPL_EDIT");
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("CLAIM_UPL_CREATE");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("CLAIM_UPL_APRV");
    private Date dtFilterEntryDateFrom;
    private Date dtFilterEntryDateTo;
    private InsurancePolicyView policy;
    private String stClaimObject;
    private DTOList listProcess;
    private DTOList listSubrogasi;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadList.class);

    public DTOList getList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (list == null) {
            list = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
 
        sqa.addSelect(" a.*,b.user_name as create_name,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_claim_detail x where x.ins_upload_id = a.ins_upload_id) as jumlah_data,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_claim_detail x where x.ins_upload_id = a.ins_upload_id and status = 'Y') as jumlah_data_proses ");

        sqa.addQuery(" from ins_upload_claim_header a"+
                   " inner join s_users b on a.create_who = b.user_id");

        sqa.addFilter(list.getFilter());

        if(branch!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(branch);
        }

        if(dtFilterEntryDateFrom!=null){
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if(dtFilterEntryDateTo!=null){
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addOrder("a.ins_upload_id desc");
        
       // sqa.addGroup("ins_upload_id,status,recap_no,create_who");

       sqa.setLimit(200);

        list = sqa.getList(UploadClaimHeaderView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickRefresh() {
    }

    public void btnSearch() throws Exception {
        getList().getFilter().setCurrentPage(0);
    }

    /**
     * @return the memorialID
     */
    public String getMemorialID() {
        return memorialID;
    }

    /**
     * @param memorialID the memorialID to set
     */
    public void setMemorialID(String memorialID) {
        this.memorialID = memorialID;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the canNavigateBranch
     */
    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    /**
     * @param canNavigateBranch the canNavigateBranch to set
     */
    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    /**
     * @return the canEdit
     */
    public boolean isCanEdit() {
        return canEdit;
    }

    /**
     * @param canEdit the canEdit to set
     */
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    /**
     * @return the canCreate
     */
    public boolean isCanCreate() {
        return canCreate;
    }

    /**
     * @param canCreate the canCreate to set
     */
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    /**
     * @return the canApprove
     */
    public boolean isCanApprove() {
        return canApprove;
    }

    /**
     * @param canApprove the canApprove to set
     */
    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    /**
     * @return the dtFilterEntryDateFrom
     */
    public Date getDtFilterEntryDateFrom() {
        return dtFilterEntryDateFrom;
    }

    /**
     * @param dtFilterEntryDateFrom the dtFilterEntryDateFrom to set
     */
    public void setDtFilterEntryDateFrom(Date dtFilterEntryDateFrom) {
        this.dtFilterEntryDateFrom = dtFilterEntryDateFrom;
    }

    /**
     * @return the dtFilterEntryDateTo
     */
    public Date getDtFilterEntryDateTo() {
        return dtFilterEntryDateTo;
    }

    /**
     * @param dtFilterEntryDateTo the dtFilterEntryDateTo to set
     */
    public void setDtFilterEntryDateTo(Date dtFilterEntryDateTo) {
        this.dtFilterEntryDateTo = dtFilterEntryDateTo;
    }

    public void refresh(){

    }

    public void clickCreate() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_upload_form", this);

      form.createNew();

      form.show();

   }

   public void clickEdit() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_upload_form", this);

      form.Edit(memorialID);

      form.show();

   }

   public void clickView() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_upload_form", this);

      form.view(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void clickApproval() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_upload_form", this);

      form.approve(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void createClaimPLAByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_claim_header "+
                " where ins_upload_id = ? ",
                new Object[]{memorialID},
                UploadClaimHeaderView.class);

        UploadClaimHeaderView header = (UploadClaimHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_upload_claim_header set status = 'On Process' where ins_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES DATA KLAIM
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_claim_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " order by ins_upload_dtl_id",
                new Object[]{memorialID},
                uploadClaimDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadClaimDetailView klaim = (uploadClaimDetailView) listPolicy.get(i);

                System.out.println("Bikin LKS no polis : "+ klaim.getStPolicyNo() +" urut "+ klaim.getStOrderNo());

                //get polis by nopol & No urut
                InsurancePolicyView polis = getInsurancePolicy(klaim.getStPolicyNo(), klaim.getStOrderNo());

                if(polis!=null){

                    //simpan LKS nya
                    String policyID = editCreateClaimPLAH2H(polis, header, klaim);

                    PreparedStatement PS = S.setQuery("update ins_upload_claim_detail set status = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, klaim.getStInsuranceUploadDetailID());

                    int j = PS.executeUpdate();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ klaim.getStPolicyNo() +"  urut "+ klaim.getStOrderNo()+" +++++++++++++++++++");
                }

                

        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_upload_claim_header set posted_flag = 'Y', status = 'Finished' where ins_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }
   
   public String getStClaimObject() {
        return stClaimObject;
    }

    /**
     * @param stClaimObject the stClaimObject to set
     */
    public void setStClaimObject(String stClaimObject) {
        this.stClaimObject = stClaimObject;
    }
    
    private void checkActiveEffective() {
        if (!policy.isEffective())
            throw new RuntimeException("Please approve the document");

        if (!policy.isActive())
            throw new RuntimeException("Document is not active, please refer to the last active document");
    }

   public String editCreateClaimPLAH2H(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditH2H(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        checkActiveEffective();

        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

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
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);

        policy.setDtPLADate(new Date());
        policy.setDtClaimProposeDate(klaim.getDtTanggalPengajuanKlaim());
        policy.setDtClaimDate(klaim.getDtTanggalKlaim());

        policy.setStClaimEventLocation(klaim.getStClaimEventLocation());
        policy.setStClaimPersonName(klaim.getStClaimPersonName());
        policy.setStClaimPersonAddress(klaim.getStClaimPersonAddress());
        policy.setStClaimPersonStatus(klaim.getStClaimPersonStatus());
        policy.setStClaimCauseID(klaim.getStClaimCauseID());

        if(klaim.getStClaimSubCauseID()!=null)
            policy.setStClaimSubCauseID(klaim.getStClaimSubCauseID());

        policy.setStClaimBenefit(klaim.getStClaimBenefit());
        policy.setStClaimDocuments(klaim.getStClaimDocument());
        policy.setStClaimChronology(klaim.getStClaimChronology());

        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(klaim.getStClaimCurrency());
        policy.setDbCurrencyRateClaim(klaim.getDbClaimCurrencyRate());

        policy.setDbClaimAmountEstimate(klaim.getDbClaimAmountEstimate());

        if(klaim.getStPotensiSubrogasi()!=null)
            policy.setStReference13(klaim.getStPotensiSubrogasi());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStOrderNo());

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            //COPY JENIS PEKERJAAN DR OBJECT KE KLAIM
            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                if(objx.getStReference7()!=null){
                    policy.setStReference14(objx.getStReference7());
                }
            }
        }

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            //tambah klaim bruto
            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbClaimAmountEstimate());

            //tambah klaim deductible jika ada
            if(!BDUtil.isZeroOrNull(klaim.getDbClaimDeductibleAmount())){

                String insItemDedID = "47";

                final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

                itemDed.setStChargableFlag("Y");

                itemDed.setDbAmount(klaim.getDbClaimDeductibleAmount());
            }

        }

        //tambah doc klaim
        /*
        if(klaim.getStFilePhysic()!=null){
            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");

            //dapetin doc klaim nya
            final DTOList listDocPolicy = policy.getClaimDocuments();

            for (int j = 0; j < listDocPolicy.size(); j++) {
                    InsurancePolicyDocumentView docPolicy = (InsurancePolicyDocumentView) listDocPolicy.get(j);

                    if(docPolicy.getStInsuranceDocumentTypeID().equalsIgnoreCase("1")){
                        docPolicy.markNew();

                        logger.logWarning("Kecentang doc klaim : "+ docPolicy.getStInsuranceDocumentTypeID());

                        docPolicy.setStSelectedFlag("Y");
                        docPolicy.setStFilePhysic(fileID);
                    }
                }
        }*/

        //PILIH OBJEK KLAIM
        selectClaimObjectH2H(klaim);

        policy.recalculate();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimH2H(policy,policy.getStNextStatus(),false);
        
        //tambahin dokumen klaim
        if(klaim.getStFilePhysic()!=null){
            
            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");
            
            InsurancePolicyDocumentView docPolicy = new InsurancePolicyDocumentView();

            docPolicy.setStInsuranceDocumentTypeID("1");
            docPolicy.setStDocumentClass("CLAIM");
            docPolicy.setStPolicyID(policyID);
            docPolicy.markNew();
            docPolicy.setStSelectedFlag("Y");
            docPolicy.setStFilePhysic(fileID);
            docPolicy.setStDocumentNotes("DOKUMEN LENGKAP");
            docPolicy.store();
            
       }



        return policyID;
    }

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

   /**
     * @return the policy
     */
    public InsurancePolicyView getPolicy() {
        return policy;
    }

    /**
     * @param policy the policy to set
     */
    public void setPolicy(InsurancePolicyView policy) {
        this.policy = policy;
    }

   private String getClaimBrutoInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='CLAIMG' and active_flag='Y'");

        return lu.getCode(0);
    }

   public InsurancePolicyItemsView onNewClaimItem(String insItemID) throws Exception {
        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.CLAIM);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStChargableFlag("Y");

        getClaimItems().add(item);

        return item;
    }

   public DTOList getClaimItems() {
        return policy.getClaimItems();
    }


    public InsurancePolicyView getInsurancePolicy(String stPolicyNo, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ( select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? order by a.pol_id desc "+
                                        " ) x order by pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public void superEditH2H(InsurancePolicyView data, uploadClaimDetailView klaim) throws Exception {
        viewH2H(data, klaim);

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStOrderNo());

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

    }

    public void viewH2H(InsurancePolicyView data, uploadClaimDetailView klaim) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(klaim.getStPolicyNo(), klaim.getStOrderNo());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjectsByOrderNo(klaim.getStOrderNo());
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public void selectClaimObjectH2H(uploadClaimDetailView klaim) throws Exception {

        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjectsByOrderNo(klaim.getStOrderNo()).get(0);

        boolean canNotClaim = policy.validateClaimObject(o);

        if(canNotClaim){
            setStClaimObject(null);
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim "+ klaim.getStPolicyNo() +" "+ klaim.getStOrderNo() +" sudah nol, tidak bisa dilakukan pembuatan klaim");
        }

        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());

            policy.setObjIndex(getStClaimObject());

            policy.setDbClaimAmountEstimate(klaim.getDbClaimAmountEstimate());

        }

        policy.setClaimObject(o);

        policy.setDbClaimAmountEstimate(klaim.getDbClaimAmountEstimate());

        o.setStClaimLossID(klaim.getStClaimLossID());


    }

     public void clickCreateProcess() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_process_form", this);

      form.createNewProcess();

      form.show();

   }

   public void clickEditProcess() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_process_form", this);

      form.Edit(memorialID);

      form.show();

   }

   public void clickViewProcess() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_process_form", this);

      form.view(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void clickApprovalProcess() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("claim_process_form", this);

      form.approve(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void processCreateClaimByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_claim_header "+
                " where ins_upload_id = ? ",
                new Object[]{memorialID},
                UploadClaimHeaderView.class);

        UploadClaimHeaderView header = (UploadClaimHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_upload_claim_header set status = 'On Process' where ins_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES DATA KLAIM
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_claim_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " order by ins_upload_dtl_id",
                new Object[]{memorialID},
                uploadClaimDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadClaimDetailView klaim = (uploadClaimDetailView) listPolicy.get(i);

                System.out.println("Bikin LKS no polis : "+ klaim.getStPolicyNo() +" urut "+ klaim.getStOrderNo());

                //get polis by nopol & No urut
                InsurancePolicyView polis = getInsurancePolicy(klaim.getStPolicyNo(), klaim.getStOrderNo());

                if(polis!=null){

                    //JIKA PROSES BENTUK LKP SD SETUJUI LKP
                    if(klaim.isDLAApprovedProcess()){

                        logger.logDebug("###### CREATE & SETUJUI LKP "+ klaim.getStPolicyNo());

                        //CEK DULU SUDAH ADA LKS / BELUM
                        InsurancePolicyView lks = getPLAExisting(klaim.getStPolicyNo(), klaim.getStOrderNo());

                        //jika lks sudah ada, setujui & buat lkp setujui
                        if(lks!=null){

                            //cek jika lks sdh disetujui/belum

                            logger.logDebug("###### LKS SUDAH ADA "+ klaim.getStPolicyNo());

                            //jika blm disetujui, setujui lks nya
                            if(!lks.isEffective()){


                                lks.markUpdate();
                                lks.setStEffectiveFlag("Y");
                                lks.setStActiveFlag("N");
                                lks.setStApprovedWho(klaim.getStApprovedWho());
                                lks.setDtApprovedDate(new Date());

                                getRemoteInsurance().save(lks, lks.getStNextStatus(), false);

                            }

                            //lanjut buat lkp setujui
                            editCreateClaimDLA(lks, header, klaim);

                        }else{
                            //jika lks blm ada, bentuk lks dulu, baru setujui

                            logger.logDebug("###### LKS BELUM ADA, CREATE LKP & SETUJUI "+ klaim.getStPolicyNo());

                            //simpan LKS sudah disetujui
                            String policyID = editCreateClaimPLA(polis, header, klaim);

                            //dapetin lks yg baru dibuat
                            InsurancePolicyView lksSimpan = getInsurancePolicyByPolID(policyID);

                            //lanjut buat lkp setujui
                            editCreateClaimDLA(lksSimpan, header, klaim);
                        }
                    }

                    //JIKA PROSES BUAT ENDORSE LKP & SETUJUI
                    if(klaim.isDLAEndorseProcess()){

                        //cari lkp yang sudah disetujui nya dulu
                        InsurancePolicyView lkp = getDLAExisting(klaim.getStPolicyNo(), klaim.getStOrderNo());

                        //JIKA LKP DITEMUKAN, BUAT ENDORSE LKP NYA
                        if(lkp!=null){
                            editCreateEndorseClaimDLA(lkp, header, klaim);
                        }
                    }


                    //PROSES BENTUK LKS, LKP, LALU TOLAK LKP
                    if(klaim.isDLARejectProcess()){

                        //CEK DULU SUDAH ADA LKS / BELUM
                        InsurancePolicyView lks = getPLAExisting(klaim.getStPolicyNo(), klaim.getStOrderNo());

                        //jika lks sudah ada, setujui & buat lkp setujui
                        if(lks!=null){

                            //cek jika lks sdh disetujui/belum

                            //jika blm disetujui, setujui lks nya
                            if(!lks.isEffective()){

                                lks.markUpdate();
                                lks.setStEffectiveFlag("Y");
                                lks.setStActiveFlag("N");
                                lks.setStApprovedWho(klaim.getStApprovedWho());
                                lks.setDtApprovedDate(new Date());

                                getRemoteInsurance().save(lks, lks.getStNextStatus(), false);

                            }

                            //lanjut buat lkp TOLAK
                            editCreateClaimDLAReject(lks, header, klaim);

                        }else{
                            //jika lks blm ada, bentuk lks dulu, baru setujui

                            //simpan LKS sudah disetujui
                            String policyID = editCreateClaimPLA(polis, header, klaim);

                            //dapetin lks yg baru dibuat
                            InsurancePolicyView lksSimpan = getInsurancePolicyByPolID(policyID);

                            //lanjut buat lkp TOLAK
                            editCreateClaimDLAReject(lksSimpan, header, klaim);
                        }
                    }

                    //PROSES BENTUK LKS SUDAH DISETUJUI
                    if(klaim.isPLAApprovedProcess()){

                        //CEK DULU SUDAH ADA LKS / BELUM
                        InsurancePolicyView lks = getPLAExisting(klaim.getStPolicyNo(), klaim.getStOrderNo());

                        //jika lks sudah ada, setujui 
                        if(lks!=null){

                            //cek jika lks sdh disetujui/belum

                            //jika blm disetujui, setujui lks nya
                            if(!lks.isEffective()){

                                lks.markUpdate();
                                lks.setStEffectiveFlag("Y");
                                lks.setStApprovedWho(klaim.getStApprovedWho());
                                lks.setDtApprovedDate(new Date());

                                getRemoteInsurance().save(lks, lks.getStNextStatus(), false);

                            }

                        }else{
                            //jika lks blm ada, bentuk lks dulu, baru setujui

                            //simpan LKS sudah disetujui
                            editCreateClaimPLAOnly(polis, header, klaim);

                        }
                    }


                    PreparedStatement PS = S.setQuery("update ins_upload_claim_detail set status = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, klaim.getStInsuranceUploadDetailID());

                    int j = PS.executeUpdate();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ klaim.getStPolicyNo() +"  urut "+ klaim.getStOrderNo()+" +++++++++++++++++++");
                }



        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_upload_claim_header set posted_flag = 'Y', status = 'Finished' where ins_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }

   public InsurancePolicyView getPLAExisting(String stPolicyNo, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('CLAIM') and claim_status = 'PLA' and active_flag = 'Y'"+
                                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

   public String editCreateClaimDLA(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditLKS(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        //checkActiveEffective();

        if(!policy.isStatusClaimPLA()){
            throw new RuntimeException("DLA Can Only Be Created From PLA");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStActiveFlag("Y");

        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        policy.setDtDLADate(new Date());
        policy.setDtApprovedClaimDate(new Date());
        policy.setDbClaimAmountApproved(klaim.getDbClaimAmountEstimate());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());


        //tambah klaim biaya survey jika ada
        if(!BDUtil.isZeroOrNull(klaim.getDbSurveyCostAmount())){

            String insItemDedID = "77";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbSurveyCostAmount());

            if(klaim.getStSurveyNotes()!=null)
                itemDed.setStDescription(klaim.getStSurveyNotes());

            itemDed.setStEntityID(klaim.getStSurveyorEntID());
            itemDed.setStTaxCode("13");
        }

        //tambah PPN jika ada
        if(!BDUtil.isZeroOrNull(klaim.getDbPPNAmount())){

            String insItemDedID = "76";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbPPNAmount());

        }

        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        policy.setClaimObject(objectClaim);

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        //getRemoteInsurance().saveNotaToCare(policy, true);

        return policyID;
    }

   public void superEditLKS(InsurancePolicyView data, uploadClaimDetailView klaim) throws Exception {
        viewLKS(data);

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStOrderNo());

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

    }

    public void viewLKS(InsurancePolicyView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicyByPolID(data.getStPolicyID());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public InsurancePolicyView getInsurancePolicyByPolID(String stPolicyID) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*"+
                                        " from ins_policy a "+
                                        " where pol_id = ? ",
                new Object[]{stPolicyID},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public String editCreateClaimPLA(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditH2H(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        checkActiveEffective();

        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

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
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStActiveFlag("N");


        policy.setDtPLADate(new Date());
        policy.setDtClaimProposeDate(klaim.getDtTanggalPengajuanKlaim());
        policy.setDtClaimDate(klaim.getDtTanggalKlaim());

        policy.setStClaimEventLocation(klaim.getStClaimEventLocation());
        policy.setStClaimPersonName(klaim.getStClaimPersonName());
        policy.setStClaimPersonAddress(klaim.getStClaimPersonAddress());
        policy.setStClaimPersonStatus(klaim.getStClaimPersonStatus());
        policy.setStClaimCauseID(klaim.getStClaimCauseID());

        if(klaim.getStClaimSubCauseID()!=null)
            policy.setStClaimSubCauseID(klaim.getStClaimSubCauseID());

        policy.setStClaimBenefit(klaim.getStClaimBenefit());
        policy.setStClaimDocuments(klaim.getStClaimDocument());
        policy.setStClaimChronology(klaim.getStClaimChronology());

        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(klaim.getStClaimCurrency());
        policy.setDbCurrencyRateClaim(klaim.getDbClaimCurrencyRate());

        policy.setDbClaimAmountEstimate(klaim.getDbClaimAmountEstimate());

        policy.setStEffectiveFlag("Y");
        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());

        if(klaim.getStPotensiSubrogasi()!=null)
            policy.setStReference13(klaim.getStPotensiSubrogasi());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStOrderNo());

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            //COPY JENIS PEKERJAAN DR OBJECT KE KLAIM
            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                if(objx.getStReference7()!=null){
                    policy.setStReference14(objx.getStReference7());
                }
            }
        }

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            //tambah klaim bruto
            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbClaimAmountEstimate());

            //tambah klaim deductible jika ada
            if(!BDUtil.isZeroOrNull(klaim.getDbClaimDeductibleAmount())){

                String insItemDedID = "47";

                final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

                itemDed.setStChargableFlag("Y");

                itemDed.setDbAmount(klaim.getDbClaimDeductibleAmount());
            }

        }

        //tambah doc klaim
        /*
        if(klaim.getStFilePhysic()!=null){
            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");

            //dapetin doc klaim nya
            final DTOList listDocPolicy = policy.getClaimDocuments();

            for (int j = 0; j < listDocPolicy.size(); j++) {
                    InsurancePolicyDocumentView docPolicy = (InsurancePolicyDocumentView) listDocPolicy.get(j);

                    if(docPolicy.getStInsuranceDocumentTypeID().equalsIgnoreCase("1")){
                        docPolicy.markNew();

                        logger.logWarning("Kecentang doc klaim : "+ docPolicy.getStInsuranceDocumentTypeID());

                        docPolicy.setStSelectedFlag("Y");
                        docPolicy.setStFilePhysic(fileID);
                    }
                }
        }*/

        //PILIH OBJEK KLAIM
        selectClaimObjectH2H(klaim);

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(),false);

        //tambahin dokumen klaim
        if(klaim.getStFilePhysic()!=null){

            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");

            InsurancePolicyDocumentView docPolicy = new InsurancePolicyDocumentView();

            docPolicy.setStInsuranceDocumentTypeID("1");
            docPolicy.setStDocumentClass("CLAIM");
            docPolicy.setStPolicyID(policyID);
            docPolicy.markNew();
            docPolicy.setStSelectedFlag("Y");
            docPolicy.setStFilePhysic(fileID);
            docPolicy.setStDocumentNotes("DOKUMEN LENGKAP");
            docPolicy.store();

       }



        return policyID;
    }

    public InsurancePolicyView getDLAExisting(String stPolicyNo, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('CLAIM') and claim_status = 'DLA' and active_flag = 'Y' and effective_flag = 'Y'"+
                                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public String editCreateEndorseClaimDLA(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditLKS(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        checkActiveEffective();

        if(!policy.isStatusClaimDLA()){
            throw new RuntimeException("DLA Endorse Can Only Be Created From DLA");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.generateClaimEndorseDLA();

        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        policy.setDtDLADate(new Date());
        policy.setDtApprovedClaimDate(new Date());
        policy.setDbClaimAmountApproved(klaim.getDbClaimAmountEstimate());
        policy.setDbClaimAmountEndorse(BigDecimal.ZERO);

        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        policy.setClaimObject(objectClaim);

        final DTOList itemClaim = policy.getClaimItems();

        // SET NILAI PELUNASAN BIAYA SURVEY
        for (int i = 0; i < itemClaim.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) itemClaim.get(i);

            if(item.getStInsItemID().equalsIgnoreCase("77")){

                item.setDbAmount(klaim.getDbSurveyCostAmount());

                if(klaim.getStSurveyNotes()!=null){
                    item.setStDescription(klaim.getStSurveyNotes());
                    policy.setStEndorseNotes(klaim.getStSurveyNotes());
                }
                    
            }


            //jika ppn
            if(item.getStInsItemID().equalsIgnoreCase("76")){

                item.setDbAmount(klaim.getDbPPNAmount());

            }

        }

        //tambah PPN jika ada
        /*if(!BDUtil.isZeroOrNull(klaim.getDbPPNAmount())){

            String insItemDedID = "76";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbPPNAmount());

        }

        //tambah klaim biaya survey jika ada
        
        if(!BDUtil.isZeroOrNull(klaim.getDbSurveyCostAmount())){

            String insItemDedID = "77";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbSurveyCostAmount());

            if(klaim.getStSurveyNotes()!=null)
                itemDed.setStDescription(klaim.getStSurveyNotes());

            itemDed.setStEntityID(klaim.getStSurveyorEntID());
            itemDed.setStTaxCode("13");
        }*/


        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        //getRemoteInsurance().saveNotaToCare(policy, true);

        return policyID;
    }

    public String editCreateClaimDLAReject(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditLKS(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        //checkActiveEffective();

        if(!policy.isStatusClaimPLA()){
            throw new RuntimeException("DLA Can Only Be Created From PLA");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");

        policy.setStApprovedWho(klaim.getStApprovedWho());
        //policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");

        policy.setDtDLADate(new Date());
        policy.setDbClaimAmountApproved(klaim.getDbClaimAmountEstimate());

        //REJECT
        policy.setStEffectiveFlag("N");
        policy.setStActiveFlag("N");
        policy.setStRejectFlag("Y");
        policy.setDtRejectDate(new Date());
        policy.setStRejectNotes(klaim.getStRejectNotes());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());


        //tambah klaim biaya survey jika ada
        if(!BDUtil.isZeroOrNull(klaim.getDbSurveyCostAmount())){

            String insItemDedID = "77";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbSurveyCostAmount());

            if(klaim.getStSurveyNotes()!=null)
                itemDed.setStDescription(klaim.getStSurveyNotes());

            itemDed.setStEntityID(klaim.getStSurveyorEntID());
            itemDed.setStTaxCode("13");
        }


        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), false);

        return policyID;
    }

    public DTOList getListProcess() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listProcess == null) {
            listProcess = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listProcess.setFilter(filter.activate());
        }

        sqa.addSelect(" a.*,b.user_name as create_name,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_claim_detail x where x.ins_upload_id = a.ins_upload_id) as jumlah_data,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_claim_detail x where x.ins_upload_id = a.ins_upload_id and status = 'Y') as jumlah_data_proses ");

        sqa.addQuery(" from ins_upload_claim_header a"+
                   " inner join s_users b on a.create_who = b.user_id");

        sqa.addClause("a.cc_code = '00'");

        sqa.addFilter(listProcess.getFilter());

        if(branch!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(branch);
        }

        if(dtFilterEntryDateFrom!=null){
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if(dtFilterEntryDateTo!=null){
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addOrder("a.ins_upload_id desc");

       // sqa.addGroup("ins_upload_id,status,recap_no,create_who");

       sqa.setLimit(200);

        listProcess = sqa.getList(UploadClaimHeaderView.class);

        return listProcess;
    }

    public void setListProcess(DTOList listProcess) {
        this.listProcess = listProcess;
    }

    public String editCreateClaimPLAOnly(InsurancePolicyView data, UploadClaimHeaderView header, uploadClaimDetailView klaim) throws Exception {
        superEditH2H(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        checkActiveEffective();

        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStActiveFlag("Y");


        policy.setDtPLADate(new Date());
        policy.setDtClaimProposeDate(klaim.getDtTanggalPengajuanKlaim());
        policy.setDtClaimDate(klaim.getDtTanggalKlaim());

        policy.setStClaimEventLocation(klaim.getStClaimEventLocation());
        policy.setStClaimPersonName(klaim.getStClaimPersonName());
        policy.setStClaimPersonAddress(klaim.getStClaimPersonAddress());
        policy.setStClaimPersonStatus(klaim.getStClaimPersonStatus());
        policy.setStClaimCauseID(klaim.getStClaimCauseID());

        if(klaim.getStClaimSubCauseID()!=null)
            policy.setStClaimSubCauseID(klaim.getStClaimSubCauseID());

        policy.setStClaimBenefit(klaim.getStClaimBenefit());
        policy.setStClaimDocuments(klaim.getStClaimDocument());
        policy.setStClaimChronology(klaim.getStClaimChronology());

        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(klaim.getStClaimCurrency());
        policy.setDbCurrencyRateClaim(klaim.getDbClaimCurrencyRate());

        policy.setDbClaimAmountEstimate(klaim.getDbClaimAmountEstimate());

        policy.setStRIFinishFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());

        if(klaim.getStPotensiSubrogasi()!=null)
            policy.setStReference13(klaim.getStPotensiSubrogasi());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStOrderNo());

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            //COPY JENIS PEKERJAAN DR OBJECT KE KLAIM
            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                if(objx.getStReference7()!=null){
                    policy.setStReference14(objx.getStReference7());
                }
            }
        }

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            //tambah klaim bruto
            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbClaimAmountEstimate());

            //tambah klaim deductible jika ada
            if(!BDUtil.isZeroOrNull(klaim.getDbClaimDeductibleAmount())){

                String insItemDedID = "47";

                final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

                itemDed.setStChargableFlag("Y");

                itemDed.setDbAmount(klaim.getDbClaimDeductibleAmount());
            }

        }

        //tambah doc klaim
        /*
        if(klaim.getStFilePhysic()!=null){
            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");

            //dapetin doc klaim nya
            final DTOList listDocPolicy = policy.getClaimDocuments();

            for (int j = 0; j < listDocPolicy.size(); j++) {
                    InsurancePolicyDocumentView docPolicy = (InsurancePolicyDocumentView) listDocPolicy.get(j);

                    if(docPolicy.getStInsuranceDocumentTypeID().equalsIgnoreCase("1")){
                        docPolicy.markNew();

                        logger.logWarning("Kecentang doc klaim : "+ docPolicy.getStInsuranceDocumentTypeID());

                        docPolicy.setStSelectedFlag("Y");
                        docPolicy.setStFilePhysic(fileID);
                    }
                }
        }*/

        //PILIH OBJEK KLAIM
        selectClaimObjectH2H(klaim);

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(),false);

        //tambahin dokumen klaim
        if(klaim.getStFilePhysic()!=null){

            //save ulang document nya
            FileView docKlaim = FileManager.getInstance().getFile(klaim.getStFilePhysic());

            File source = new File(docKlaim.getStFilePath());

            String fileID = FileUtil.saveDocument(source, klaim.getStPolicyNo() +"_"+ klaim.getStOrderNo() + "_"+"1");

            InsurancePolicyDocumentView docPolicy = new InsurancePolicyDocumentView();

            docPolicy.setStInsuranceDocumentTypeID("1");
            docPolicy.setStDocumentClass("CLAIM");
            docPolicy.setStPolicyID(policyID);
            docPolicy.markNew();
            docPolicy.setStSelectedFlag("Y");
            docPolicy.setStFilePhysic(fileID);
            docPolicy.setStDocumentNotes("DOKUMEN LENGKAP");
            docPolicy.store();

       }



        return policyID;
    }

    public DTOList getListSubrogasi() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listSubrogasi == null) {
            listSubrogasi = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listSubrogasi.setFilter(filter.activate());
        }

        sqa.addSelect(" a.*,b.user_name as create_name,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_subrogasi_detail x where x.ins_upload_id = a.ins_upload_id) as jumlah_data,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_subrogasi_detail x where x.ins_upload_id = a.ins_upload_id and status = 'Y') as jumlah_data_proses ");

        sqa.addQuery(" from ins_upload_subrogasi_header a"+
                   " inner join s_users b on a.create_who = b.user_id");

        //sqa.addClause("a.cc_code = '00'");

        sqa.addFilter(listSubrogasi.getFilter());

        if(branch!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(branch);
        }

        if(dtFilterEntryDateFrom!=null){
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if(dtFilterEntryDateTo!=null){
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addOrder("a.ins_upload_id desc");

       // sqa.addGroup("ins_upload_id,status,recap_no,create_who");

       sqa.setLimit(200);

        listSubrogasi = sqa.getList(UploadSubrogasiHeaderView.class);

        return listSubrogasi;
    }

    public void setListSubrogasi(DTOList listSubrogasi) {
        this.listSubrogasi = listSubrogasi;
    }

    public void clickCreateSubrogasi() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("subrogasi_process_form", this);

      form.createNewSubrogasi();

      form.show();

   }

   public void clickEditSubrogasi() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("subrogasi_process_form", this);

      form.EditSubrogasi(memorialID);

      form.show();

   }

   public void clickViewSubrogasi() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("subrogasi_process_form", this);

      form.viewSubrogasi(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void clickApprovalSubrogasi() throws Exception {
      final ClaimUploadForm form = (ClaimUploadForm) newForm("subrogasi_process_form", this);

      form.approveSubrogasi(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void processSubrogasi() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_subrogasi_header "+
                " where ins_upload_id = ? ",
                new Object[]{memorialID},
                UploadSubrogasiHeaderView.class);

        UploadSubrogasiHeaderView header = (UploadSubrogasiHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_upload_subrogasi_header set status = 'On Process' where ins_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES DATA KLAIM
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_subrogasi_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " order by ins_upload_dtl_id",
                new Object[]{memorialID},
                uploadSubrogasiDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadSubrogasiDetailView klaim = (uploadSubrogasiDetailView) listPolicy.get(i);

                System.out.println("Proses subrogasi LKP NO : "+ klaim.getStDLANo());

                //get LKP terakhir by nopol & No LKP
                InsurancePolicyView lkp = getLKP(klaim.getStPolicyNo(), klaim.getStDLANo());

                if(lkp!=null){

                    //BENTUK LKP ENDORSE TANPA SETUJUI
                    if(klaim.isCreateSubrogasiNotApproved()){

                        //cari lkp existing
                        InsurancePolicyView lkpExisting = getLKP(klaim.getStPolicyNo(), klaim.getStDLANo());

                        //JIKA LKP DITEMUKAN, BUAT ENDORSE LKP NYA
                        if(lkpExisting!=null){
                            createEndorseSubrogasi(lkpExisting, header, klaim);
                        }
                    }


                    //BENTUK LKP ENDORSE LANGSUNG SETUJUI
                    if(klaim.isCreateSubrogasiApproved()){

                        //cari lkp existing
                        InsurancePolicyView lkpExisting = getLKP(klaim.getStPolicyNo(), klaim.getStDLANo());

                        //JIKA LKP DITEMUKAN, BUAT ENDORSE LKP NYA & SETUJUI
                        if(lkpExisting!=null){
                            createEndorseSubrogasiApproved(lkpExisting, header, klaim);
                        }
                    }


                    PreparedStatement PS = S.setQuery("update ins_upload_subrogasi_detail set status = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, klaim.getStInsuranceUploadDetailID());

                    int j = PS.executeUpdate();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ klaim.getStPolicyNo() +"  no lkp "+ klaim.getStDLANo()+" +++++++++++++++++++");

                    S.release();
                }

                //SETUJUI LKP ENDORSE YANG SUDAH ADA
                if(klaim.isApprovedSubrogasi()){

                    //get LKP by LKP
                    InsurancePolicyView lkpSpecimen = getDLASpecimen(klaim.getStDLANo());

                    //JIKA LKP DITEMUKAN, SETUJUI
                    if(lkpSpecimen!=null){
                        approveSubrogasi(lkpSpecimen, header, klaim);
                    }

                    PreparedStatement PS = S.setQuery("update ins_upload_subrogasi_detail set status = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, klaim.getStInsuranceUploadDetailID());

                    int j = PS.executeUpdate();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ klaim.getStPolicyNo() +"  lkp "+ klaim.getStDLANo()+" +++++++++++++++++++");

                    S.release();
                }



        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_upload_subrogasi_header set posted_flag = 'Y', status = 'Finished' where ins_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }

   public InsurancePolicyView getDLA(String stDLANo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where effective_flag = 'Y' and dla_no = ?",
                new Object[]{stDLANo},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

   public String createEndorseSubrogasi(InsurancePolicyView data, UploadSubrogasiHeaderView header, uploadSubrogasiDetailView klaim) throws Exception {
        superEditLKP(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        //checkActiveEffective();

        if(!policy.isStatusClaimDLA()){
            throw new RuntimeException("DLA Endorse Can Only Be Created From DLA");
        }

        if (!policy.isEffective()) {
            //throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag(null);
        policy.setStEffectiveFlag(null);
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.generateClaimEndorseDLA();

        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        policy.setDtDLADate(new Date());
        policy.setDtApprovedClaimDate(null);
        //policy.setDbClaimAmountApproved(klaim.getDbClaimAmountEstimate());
        policy.setDbClaimAmountEndorse(BigDecimal.ZERO);
        policy.setStEndorseNotes(klaim.getStEndorseNotes());

        policy.setDtReference1(klaim.getDtTanggalBayarSubrogasi());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        policy.setClaimObject(objectClaim);

        //HAPUS ITEM KLAIM
        policy.getClaimItems().deleteAll();

        //tambah subrogasi
        if(!BDUtil.isZeroOrNull(klaim.getDbSubrogasiAmount())){

            String insItemDedID = "48";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbSubrogasiAmount());
        }

        //tambah fee recovery
        if(!BDUtil.isZeroOrNull(klaim.getDbFeeRecovery())){

            String insItemDedID = "73";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbFeeRecovery());
        }

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), false);

        //Simpan nota ke sistem care via API
        //getRemoteInsurance().saveNotaToCare(policy, true);

        return policyID;
    }

   public void superEditLKP(InsurancePolicyView data, uploadSubrogasiDetailView klaim) throws Exception {
        viewLKP(data);

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

    }

    public void viewLKP(InsurancePolicyView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicyByPolID(data.getStPolicyID());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public String createEndorseSubrogasiApproved(InsurancePolicyView data, UploadSubrogasiHeaderView header, uploadSubrogasiDetailView klaim) throws Exception {
        superEditLKP(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        //checkActiveEffective();

        if(!policy.isStatusClaimDLA()){
            throw new RuntimeException("DLA Endorse Can Only Be Created From DLA");
        }

        if (!policy.isEffective()) {
            //throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.generateClaimEndorseDLA();

        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        policy.setDtDLADate(new Date());
        policy.setDtApprovedClaimDate(new Date());
        //policy.setDbClaimAmountApproved(klaim.getDbClaimAmountEstimate());
        policy.setDbClaimAmountEndorse(BigDecimal.ZERO);
        policy.setStEndorseNotes(klaim.getStEndorseNotes());

        policy.setDtReference1(klaim.getDtTanggalBayarSubrogasi());

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        policy.setClaimObject(objectClaim);

        //HAPUS ITEM KLAIM
        policy.getClaimItems().deleteAll();

        //tambah subrogasi
        if(!BDUtil.isZeroOrNull(klaim.getDbSubrogasiAmount())){

            String insItemDedID = "48";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbSubrogasiAmount());
        }

        //tambah fee recovery
        if(!BDUtil.isZeroOrNull(klaim.getDbFeeRecovery())){

            String insItemDedID = "73";

            final InsurancePolicyItemsView itemDed = onNewClaimItem(insItemDedID);

            itemDed.setStChargableFlag("Y");

            itemDed.setDbAmount(klaim.getDbFeeRecovery());
        }

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        //getRemoteInsurance().saveNotaToCare(policy, true);

        return policyID;
    }

    public InsurancePolicyView getLKP(String stPolicyNo, String noLKP) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ( select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('CLAIM','CLAIM ENDORSE')"+
                                        " and substr(a.pol_no, 0, 17) = ? and a.dla_no like ? "
                                        + " order by a.pol_id desc "+
                                        " ) x order by pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noLKP+"%"},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public InsurancePolicyView getDLASpecimen(String stDLANo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where coalesce(effective_flag,'N') <> 'Y' and dla_no = ?",
                new Object[]{stDLANo},
                InsurancePolicyView.class).getDTO();

        return pol;
    }


    public String approveSubrogasi(InsurancePolicyView data, UploadSubrogasiHeaderView header, uploadSubrogasiDetailView klaim) throws Exception {
        superEditLKP(data,klaim);

        logger.logDebug("################## KLAIM no urut "+ policy.getStAccountno());

        if(!policy.isStatusClaimDLA()){
            throw new RuntimeException("DLA Endorse Can Only Be Created From DLA");
        }

        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");

        policy.setStApprovedWho(klaim.getStApprovedWho());
        policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        if(header.getStRecapNo()!=null)
            policy.setStReferenceNo(header.getStRecapNo());

        policy.setDtDLADate(new Date());
        policy.setDtApprovedClaimDate(new Date());
        policy.setDtReference1(klaim.getDtTanggalBayarSubrogasi());

        policy.recalculate();
        policy.recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimProcess(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        //getRemoteInsurance().saveNotaToCare(policy, true);

        return policyID;
    }




}

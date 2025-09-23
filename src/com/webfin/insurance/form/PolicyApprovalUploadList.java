/***********************************************************************
 * Module:  com.webfin.gl.form.GLJournalList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.model.Filter;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.model.JournalView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.PolicyApprovalHeaderView;
import com.webfin.insurance.model.PolicyApprovalUploadDetailView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;

public class PolicyApprovalUploadList extends Form {

    private DTOList list;
    private JournalView journal;
    private String memorialID;
    private String description;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_APRV_UPL_NAVBR");
    private boolean canEdit = SessionManager.getInstance().getSession().hasResource("POL_APRV_UPL_EDIT");
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("POL_APRV_UPL_CREATE");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("POL_APRV_UPL_APRV");
    private boolean canProcess = SessionManager.getInstance().getSession().hasResource("POL_PROCESS_UPL_APRV");

    public boolean isCanProcess() {
        return canProcess;
    }

    public void setCanProcess(boolean canProcess) {
        this.canProcess = canProcess;
    }
    private Date dtFilterEntryDateFrom;
    private Date dtFilterEntryDateTo;

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
                    " (select count(ins_policy_approval_upload_dtl_id) from ins_policy_approval_upload_detail x where x.ins_policy_approval_upload_id = a.ins_policy_approval_upload_id) as jumlah_data,"+
                    " (select count(ins_policy_approval_upload_dtl_id) from ins_policy_approval_upload_detail x where x.ins_policy_approval_upload_id = a.ins_policy_approval_upload_id and status = 'Y') as jumlah_data_proses, "+
                    " c.user_name as approved_name");

        sqa.addQuery(" from ins_policy_approval_upload_header a"+
                   " inner join s_users b on a.create_who = b.user_id"+
                   " left join s_users c on a.approved_who = c.user_id");

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

        sqa.addOrder("a.ins_policy_approval_upload_id desc");
        
       // sqa.addGroup("ins_upload_id,status,recap_no,create_who");

       sqa.setLimit(100);

        list = sqa.getList(PolicyApprovalHeaderView.class);

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

   public void clickCreate() throws Exception {
      final PolicyApprovalUploadForm form = (PolicyApprovalUploadForm)newForm("policy_approval_upload_form", this);

      form.createNewUpload();

      form.show();

   }

   public void clickEdit() throws Exception {
      final PolicyApprovalUploadForm form = (PolicyApprovalUploadForm)newForm("policy_approval_upload_form", this);

      form.Edit(memorialID);

      form.show();

   }

   public void clickView() throws Exception {
      final PolicyApprovalUploadForm form = (PolicyApprovalUploadForm)newForm("policy_approval_upload_form", this);

      form.view(memorialID);

      form.setReadOnly(true);

      form.show();

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
     * @return the journal
     */
    public JournalView getJournal() {
        journal = (JournalView) DTOPool.getInstance().getDTO(JournalView.class, memorialID);
        return journal;
    }

    /**
     * @param journal the journal to set
     */
    public void setJournal(JournalView journal) {
        this.journal = journal;
    }

    public void clickApproval() throws Exception {
      final PolicyApprovalUploadForm form = (PolicyApprovalUploadForm)newForm("policy_approval_upload_form", this);

      form.approve(memorialID);

      form.setReadOnly(true);

      form.show();

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

    public void prosesApprovalPolicyByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_policy_approval_upload_header "+
                " where ins_policy_approval_upload_id = ? ",
                new Object[]{memorialID},
                PolicyApprovalHeaderView.class);

        PolicyApprovalHeaderView header = (PolicyApprovalHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_policy_approval_upload_header set status = 'On Process' where ins_policy_approval_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES SETUJUI POLIS

        //dapetin list polis nya
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_policy_approval_upload_detail "+
                " where ins_policy_approval_upload_id = ? and coalesce(status,'') <> 'Y'"+
                " order by ins_policy_approval_upload_dtl_id ",
                new Object[]{memorialID},
                PolicyApprovalUploadDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                PolicyApprovalUploadDetailView pol = (PolicyApprovalUploadDetailView) listPolicy.get(i);

                DTOList listPolis = null;

                listPolis = ListUtil.getDTOListFromQuery(
                    " SELECT * FROM ins_policy where pol_no = ? "+
                    " and status in ('POLICY','ENDORSE','RENEWAL') AND coalesce(effective_flag,'') <> 'Y'",
                    new Object[]{pol.getStPolicyNo().trim()},
                InsurancePolicyView.class);

                if(listPolis!=null){

                    InsurancePolicyView polis = (InsurancePolicyView) listPolis.get(0);

                    boolean withinCurrentMonth = DateUtil.getDateStr(polis.getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                    polis.markUpdateO();

                    polis.getDetails().markAllUpdate();

                    polis.setStReadyToApproveFlag("Y");
                    polis.setStPostedFlag("Y");
                    polis.setStEffectiveFlag("Y");
                    polis.setStApprovedWho(header.getStApprovedWho());

                    Date tglSetujui = new Date();

                    polis.setDtApprovedDate(tglSetujui);
                    //polis.setDtPolicyDate(tglSetujui);
                    
                    //jika tidak dibulan yg sama tgl polis nya
                    if (!withinCurrentMonth) {
                        polis.setDtPolicyDate(new Date());
                    }

                    //jika polis askred, update tgl stnc objek
                    if(polis.getStPolicyTypeID().equalsIgnoreCase("59") || polis.getStPolicyTypeID().equalsIgnoreCase("88") || polis.getStPolicyTypeID().equalsIgnoreCase("87")){
                        if(SessionManager.getInstance().getSession().isHeadOfficeUser()){

                            polis.getObjects().markAllUpdate();

                            final DTOList objects = polis.getObjects();

                            for (int j = 0; j < objects.size(); j++) {
                                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

                                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                                objx.setStReference10("Y");
                                objx.setDtReference6(polis.getDtPolicyDate());

                                if(pol.getDtTanggalStnc()!=null){
                                    objx.setDtReference6(pol.getDtTanggalStnc());
                                }

                            }
                        }
                    }
                    
                    //polis.setStPassword(UserManager.getInstance().getUser().getStPasswd());
                    //polis.setStClientIP("192.168.200.54");

                    if(pol.getStDescription()!=null)
                        polis.setStWarranty(pol.getStDescription());

                    polis.recalculateUploadApproval();

                    getRemoteInsurance().save(polis,polis.getStNextStatus(),true);

                    //Simpan nota ke sistem care via API
                    //getRemoteInsurance().saveNotaToCare(polis, true);

                    logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");

                    PreparedStatement PS = S.setQuery("update ins_policy_approval_upload_detail set status = 'Y' where pol_no = ?");

                    PS.setObject(1,pol.getStPolicyNo());

                    int j = PS.executeUpdate();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol.getStPolicyNo() +" ++++++++++++++++++");
                }

                

        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_policy_approval_upload_header set posted_flag = 'Y', status = 'Finished' where ins_policy_approval_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

}

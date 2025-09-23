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
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.JournalView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.UploadEndorseFireHeaderView;
import com.webfin.insurance.model.UploadEndorseHeaderView;
import com.webfin.insurance.model.UploadHeaderView;
import com.webfin.insurance.model.uploadEndorseDetailView;
import com.webfin.insurance.model.uploadEndorseFireDetailView;
import com.webfin.insurance.model.uploadEndorsemenView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;

public class EndorseUploadList extends Form {

    private DTOList list;
    private JournalView journal;
    private String memorialID;
    private String description;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_UPL_NAVBR");
    private boolean canEdit = SessionManager.getInstance().getSession().hasResource("POL_UPL_EDIT");
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("POL_UPL_CREATE");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("POL_UPL_APRV");

    private boolean canNavigateBranchFire = SessionManager.getInstance().getSession().hasResource("POL_UPLFIRE_NAVBR");
    private boolean canEditFire = SessionManager.getInstance().getSession().hasResource("POL_UPLFIRE_EDIT");
    private boolean canCreateFire = SessionManager.getInstance().getSession().hasResource("POL_UPLFIRE_CREATE");
    private boolean canApproveFire = SessionManager.getInstance().getSession().hasResource("POL_UPLFIRE_APRV");

    private Date dtFilterEntryDateFrom;
    private Date dtFilterEntryDateTo;
    private DTOList fireList;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadList.class);

    public boolean isCanApproveFire() {
        return canApproveFire;
    }

    public void setCanApproveFire(boolean canApproveFire) {
        this.canApproveFire = canApproveFire;
    }

    public boolean isCanCreateFire() {
        return canCreateFire;
    }

    public void setCanCreateFire(boolean canCreateFire) {
        this.canCreateFire = canCreateFire;
    }

    public boolean isCanEditFire() {
        return canEditFire;
    }

    public void setCanEditFire(boolean canEditFire) {
        this.canEditFire = canEditFire;
    }

    public boolean isCanNavigateBranchFire() {
        return canNavigateBranchFire;
    }

    public void setCanNavigateBranchFire(boolean canNavigateBranchFire) {
        this.canNavigateBranchFire = canNavigateBranchFire;
    }

    public DTOList getList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (list == null) {
            list = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
 
        sqa.addSelect(" a.*,b.user_name as create_name,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_endorse_detail x where x.ins_upload_id = a.ins_upload_id) as jumlah_data,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_endorse_detail x where x.ins_upload_id = a.ins_upload_id and status = 'Y') as jumlah_data_proses ");

        sqa.addQuery(" from ins_upload_header a"+
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

       sqa.setLimit(150);

        list = sqa.getList(UploadEndorseHeaderView.class);

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

   public void clickUploadExcel() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsepolis_upload_form", this);

      form.createNewUpload();

      form.show();

   }

   public void clickEdit() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsepolis_upload_form", this);

      form.Edit(memorialID);

      form.show();

   }

   public void clickView() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsepolis_upload_form", this);

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

    public void createEndorsemenByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_header "+
                " where ins_upload_id = ? ",
                new Object[]{memorialID},
                UploadEndorseHeaderView.class);

        UploadEndorseHeaderView header = (UploadEndorseHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES 
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_upload_header set status = 'On Process' where ins_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES DATA ENDORSE
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select pol_no "+
                " from ins_upload_endorse_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " group by pol_no "+
                " order by min(ins_upload_dtl_id)",
                new Object[]{memorialID},
                uploadEndorseDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadEndorseDetailView pol2 = (uploadEndorseDetailView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                DTOList listObject = null;
                listObject = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " from ins_upload_endorse_detail "+
                " where coalesce(status,'') <> 'Y' "+
                " and pol_no = ? and ins_upload_id = ? "+
                " ORDER BY order_no",
                new Object[]{pol2.getStPolicyNo(), memorialID},
                uploadEndorseDetailView.class);

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,1,16) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo().substring(0, 16)},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateUploadEndorsePolis(polis.getStPolicyID(), listObject);

                form.btnRecalculate();
                form.getPolicy().recalculateTreaty();

                form.btnSaveUpload();

                PreparedStatement PS = S.setQuery("update ins_upload_endorse_detail set status = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");

        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_upload_header set posted_flag = 'Y', status = 'Finished' where ins_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }

    public void clickApproval() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsepolis_upload_form", this);

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

    public DTOList getFireList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (fireList == null) {
            fireList = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            fireList.setFilter(filter.activate());
        }

        sqa.addSelect(" a.*,b.user_name as create_name,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_endorse_fire_detail x where x.ins_upload_id = a.ins_upload_id) as jumlah_data,"+
                    " (select count(ins_upload_dtl_id) from ins_upload_endorse_fire_detail x where x.ins_upload_id = a.ins_upload_id and status = 'Y') as jumlah_data_proses ");

        sqa.addQuery(" from ins_upload_fire_header a"+
                   " inner join s_users b on a.create_who = b.user_id");

        sqa.addFilter(fireList.getFilter());

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

        fireList = sqa.getList(UploadEndorseFireHeaderView.class);

        return fireList;
    }

    public void setFireList(DTOList fireList) {
        this.fireList = fireList;
    }

    public void clickCreateFire() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsefire_upload_form", this);

      form.createNewFire();

      form.show();

   }

   public void clickEditFire() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsefire_upload_form", this);

      form.EditFire(memorialID);

      form.show();

   }

   public void clickViewFire() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsefire_upload_form", this);

      form.viewFire(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void clickApprovalFire() throws Exception {
      final EndorseUploadForm form = (EndorseUploadForm)newForm("endorsefire_upload_form", this);

      form.approveFire(memorialID);

      form.setReadOnly(true);

      form.show();

   }

   public void createEndorsemenFireByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_fire_header "+
                " where ins_upload_id = ? ",
                new Object[]{memorialID},
                UploadEndorseFireHeaderView.class);

        UploadEndorseFireHeaderView header = (UploadEndorseFireHeaderView) listHeader.get(0);

        if(!header.isEffective())
            throw new RuntimeException("Data tidak bisa diproses karena belum disetujui");

        if(header.isPosted())
            throw new RuntimeException("Data sudah diproses");

        //TANDAI SEDANG DI PROSES
        final SQLUtil S2 = new SQLUtil();
        PreparedStatement PS2 = S2.setQuery("update ins_upload_fire_header set status = 'On Process' where ins_upload_id = ?");

        PS2.setObject(1, memorialID);

        int j2 = PS2.executeUpdate();

        if (j2!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S2.release();

        //PROSES DATA ENDORSE
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select pol_no "+
                " from ins_upload_endorse_fire_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " group by pol_no "+
                " order by min(ins_upload_dtl_id)",
                new Object[]{memorialID},
                uploadEndorseFireDetailView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadEndorseFireDetailView pol2 = (uploadEndorseFireDetailView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                DTOList listObject = null;
                listObject = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " from ins_upload_endorse_fire_detail "+
                " where coalesce(status,'') <> 'Y' "+
                " and pol_no = ? and ins_upload_id = ? "+
                " ORDER BY order_no",
                new Object[]{pol2.getStPolicyNo(), memorialID},
                uploadEndorseFireDetailView.class);

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,1,16) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo().length()>16?pol2.getStPolicyNo().substring(0, 16):pol2.getStPolicyNo()},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateUploadEndorsePolisFire(polis.getStPolicyID(), header, listObject);

                form.btnRecalculate();
                form.getPolicy().recalculateTreaty();

                form.btnSaveUpload();

                PreparedStatement PS = S.setQuery("update ins_upload_endorse_fire_detail set status = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");

        }

        //TANDAI SUDAH DI PROSES
        final SQLUtil S3 = new SQLUtil();
        PreparedStatement PS3 = S3.setQuery("update ins_upload_fire_header set posted_flag = 'Y', status = 'Finished' where ins_upload_id = ?");

        PS3.setObject(1, memorialID);

        int j3 = PS3.executeUpdate();

        if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
        S3.release();

        S.release();


    }

}

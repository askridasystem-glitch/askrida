/***********************************************************************
 * Module:  com.webfin.ar.forms.RequestFeeList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.model.HashDTO;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.model.*;
import java.sql.PreparedStatement;
import java.util.Date;

public class RequestFeeList extends Form {

    private ARRequestFee request;
    private String arreqid;
    private DTOList list;
    private Date dtReqDateFrom;
    private Date dtReqDateTo;
    private String stActiveFlag = "Y";
    private String stEffectiveFlag;
    private String stCashflowFlag;
    private String printLang;
    private String stLang;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stRegion = SessionManager.getInstance().getSession().getStDivisionID();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("REQ_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("REQ_NAVRE");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("REQ_APPROVAL");
    private boolean canApproveDireksi = SessionManager.getInstance().getSession().hasResource("REQ_APPROVAL_DIR");
    private boolean canCashier = SessionManager.getInstance().getSession().hasResource("REQ_CASHIER");
    private boolean canReverse = SessionManager.getInstance().getSession().hasResource("REQ_REVERSE");
    private boolean canReprint = SessionManager.getInstance().getSession().hasResource("REQ_REPRINT");
    private boolean ownerPms = SessionManager.getInstance().getSession().hasResource("REQ_PMS");
    private boolean ownerUmum = SessionManager.getInstance().getSession().hasResource("REQ_UMUM");
    private boolean ownerAdm = SessionManager.getInstance().getSession().hasResource("REQ_ADM");
    private final static transient LogManager logger = LogManager.getInstance(RequestFeeList.class);

    public DTOList getList() throws Exception {

        if (list == null) {
            list = new DTOList();
            list.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.*,b.user_name as create_name,c.user_name as approved_name,d.user_name as cashier_name ");
        sqa.addSelect(" a.*,coalesce(b.approved,'') || coalesce(b.notapproved,'') as approved_name ");

        sqa.addQuery(
                " from ar_request_fee a "
                + "left join ( "
                + "select a.in_id,'<br><br>belum setujui :<br>'||(select STRING_AGG(CASE WHEN a.eff_flag = 'N' "
                + " THEN b.job_position END, ', ')) AS notapproved, "
                + "'sudah setujui :<br>'||coalesce((select STRING_AGG(CASE WHEN a.eff_flag = 'Y'"
                + " THEN b.job_position END, ', ')),'') AS approved "
                + "from ar_request_approval a inner join s_users b on b.user_id = a.approval_who "
                + "where a.delete_flag is null group by a.in_id ) b on b.in_id = a.req_id  ");
//                + " left join s_users b on b.user_id = a.create_who "
//                + " left join s_users c on c.user_id = a.approved_who "
//                + " left join s_users d on d.user_id = a.cashier_who ");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("a.act_flag='Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("a.act_flag='N'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("(a.validasi_f='N' or a.validasi_f is null)");
        }

        sqa.addClause("((a.status = 'PENGAJUAN' and a.validasi_f  = 'Y') or (a.status = 'REALISASI'))");
        sqa.addClause("a.deleted is null");
//        sqa.addClause("a.cashflow_f = 'Y'");
//        sqa.addClause("((a.status = 'PENGAJUAN' and a.act_flag = 'Y' and a.validasi_f  = 'Y') or "
//                + "(a.status = 'REALISASI' and a.act_flag = 'Y' and a.validasi_f = 'N'))");

        if (dtReqDateFrom != null) {
            sqa.addClause("date_trunc('day',a.request_date) >= ?");
            sqa.addPar(dtReqDateFrom);
        }

        if (dtReqDateTo != null) {
            sqa.addClause("date_trunc('day',a.request_date) <= ?");
            sqa.addPar(dtReqDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (isOwnerPms()) {
            sqa.addClause("a.pilihan = 1");
        } else if (isOwnerUmum()) {
            sqa.addClause("a.pilihan = 2");
        } else if (isOwnerAdm()) {
            sqa.addClause("a.pilihan in (3,14)");
        }

//        if (SessionManager.getInstance().getSession().getStBranch() == null) {
//            if (SessionManager.getInstance().getSession().getStJobPosition().equalsIgnoreCase("KADIV")) {
//                sqa.addClause("(a.eff_flag='N' or a.eff_flag is null)");
//
//                sqa.addClause("a.nominal > (select c.refn1 "
//                        + "from s_user_roles b "
//                        + "inner join ff_table c on c.fft_group_id = 'AUTHO' and c.ref2 = b.role_id "
//                        + "where c.active_flag = 'Y' and b.user_id = ? "
//                        + "and date_trunc('day',c.period_start) <= ? "
//                        + "and date_trunc('day',c.period_end) >= ?  )");
//                sqa.addPar(SessionManager.getInstance().getSession().getStUserID());
//                sqa.addPar(new Date());
//                sqa.addPar(new Date());
//            }
//        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("a.req_id desc");

        sqa.setLimit(40);

        list = sqa.getList(ARRequestFee.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreateRequest() throws Exception {

        final RequestFeeForm form = (RequestFeeForm) newForm("permintaan_form", this);
//        final RequestFeeForm form = (RequestFeeForm) newForm("persetujuan_form", this);

        form.clickCreateNew();

        form.show();
    }

    public void clickEdit() throws Exception {
        validateReqID();

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "permintaan_form";
//            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        final ARRequestFee req = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arreqid);

        if (req == null) {
            throw new RuntimeException("Document not found ??");
        }

        form.setApprovalMode(false);

        form.clickEdit(arreqid);

        form.show();
    }

    public void clickView() throws Exception {
        validateReqID();

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "permintaan_form";
//            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        form.clickView(arreqid);

        form.setApprovalMode(false);

        form.show();

    }

    public void clickApproval() throws Exception {
        validateReqID();

        if (getRequest().isEffective()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "permintaan_form";
//            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        form.clickApproval(arreqid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickReverse() throws Exception {
        validateReqID();

        if (!getRequest().isEffective()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "permintaan_form";
//            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        final RequestFeeForm form = (RequestFeeForm) newForm(form_name, this);

        form.clickView(arreqid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickSuperEdit() throws Exception {
        validateReqID();

        RequestFeeForm form = (RequestFeeForm) super.newForm("permintaan_form", this);

        form.clickSuperEdit(arreqid);

        form.setEnableSuperEdit(true);

        form.show();
    }

    public void list() {
    }

    public void refresh() {
    }

    private void validateReqID() {
        if (arreqid == null) {
            throw new RuntimeException("Pilih No. Pengajuan yang ingin diproses");
        }
    }

    /**
     * @return the stBranch
     */
    public String getStBranch() {
        String cabang = null;
        if (stBranch == null) {
            cabang = "00";
        } else {
            cabang = stBranch;
        }
        return cabang;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
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
     * @return the canApproveDireksi
     */
    public boolean isCanApproveDireksi() {
        return canApproveDireksi;
    }

    /**
     * @param canApproveDireksi the canApproveDireksi to set
     */
    public void setCanApproveDireksi(boolean canApproveDireksi) {
        this.canApproveDireksi = canApproveDireksi;
    }

    /**
     * @return the dtReqDateFrom
     */
    public Date getDtReqDateFrom() {
        return dtReqDateFrom;
    }

    /**
     * @param dtReqDateFrom the dtReqDateFrom to set
     */
    public void setDtReqDateFrom(Date dtReqDateFrom) {
        this.dtReqDateFrom = dtReqDateFrom;
    }

    /**
     * @return the dtReqDateTo
     */
    public Date getDtReqDateTo() {
        return dtReqDateTo;
    }

    /**
     * @param dtReqDateTo the dtReqDateTo to set
     */
    public void setDtReqDateTo(Date dtReqDateTo) {
        this.dtReqDateTo = dtReqDateTo;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stEffectiveFlag
     */
    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    /**
     * @param stEffectiveFlag the stEffectiveFlag to set
     */
    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    /**
     * @return the canNavigateRegion
     */
    public boolean isCanNavigateRegion() {
        return canNavigateRegion;
    }

    /**
     * @param canNavigateRegion the canNavigateRegion to set
     */
    public void setCanNavigateRegion(boolean canNavigateRegion) {
        this.canNavigateRegion = canNavigateRegion;
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
     * @return the canReverse
     */
    public boolean isCanReverse() {
        return canReverse;
    }

    /**
     * @param canReverse the canReverse to set
     */
    public void setCanReverse(boolean canReverse) {
        this.canReverse = canReverse;
    }

    /**
     * @return the stRegion
     */
    public String getStRegion() {
        return stRegion;
    }

    /**
     * @param stRegion the stRegion to set
     */
    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }

    /**
     * @return the arreqid
     */
    public String getArreqid() {
        return arreqid;
    }

    /**
     * @param arreqid the arreqid to set
     */
    public void setArreqid(String arreqid) {
        this.arreqid = arreqid;
    }

    private ARRequestFee getRequest() {
        request = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arreqid);
        return request;
    }

    public void clickCashier() throws Exception {
        validateReqID();

        if (getRequest().isCashierFlag()) {
            throw new Exception("Anggaran Sudah Disetujui oleh Kasir");
        }

        if (!getRequest().isEffective()) {
            throw new Exception("Persetujuan Oleh Atasan Belum Disetujui");
        }

        if (getRequest().isStatusRequest()) {
            throw new Exception("Permintaan harus status APPROVED");
        }

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "validasiapp_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "validasicsb_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        form.clickCashier(arreqid);

        form.setCashierMode(true);

        form.show();
    }

    /**
     * @return the canCashier
     */
    public boolean isCanCashier() {
        return canCashier;
    }

    /**
     * @param canCashier the canCashier to set
     */
    public void setCanCashier(boolean canCashier) {
        this.canCashier = canCashier;
    }

    public void clickCreateReimburse() throws Exception {

        validateReqID();

        final RequestFeeForm form = (RequestFeeForm) newForm("realisasi_form", this);

        form.editCreateReimburse(arreqid);

        form.show();
    }

    public void clickApprovalDireksi() throws Exception {
        validateReqID();

        if (getRequest().isStatusRequest()) {
            throw new Exception("Persetujuan oleh Direksi hanya bisa Status APPROVED dan CASHBACK");
        }

        if (getRequest().isEffective()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "permintaan_form";
//            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        form.clickApprovalDireksi(arreqid);

        form.setApprovalByDireksiMode(true);

        form.show();
    }

    /**
     * @return the printLang
     */
    public String getPrintLang() {
        return printLang;
    }

    /**
     * @param printLang the printLang to set
     */
    public void setPrintLang(String printLang) {
        this.printLang = printLang;
    }

    /**
     * @return the stLang
     */
    public String getStLang() {
        return stLang;
    }

    /**
     * @param stLang the stLang to set
     */
    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    /**
     * @return the canReprint
     */
    public boolean isCanReprint() {
        return canReprint;
    }

    /**
     * @param canReprint the canReprint to set
     */
    public void setCanReprint(boolean canReprint) {
        this.canReprint = canReprint;
    }

    public void clickRePrint() throws Exception {
        validateReqID();

        if (!getRequest().isEffective()) {
            throw new Exception("Persetujuan Oleh Atasan Belum Disetujui");
        }

        if (getRequest().isStatusRequest()) {
            throw new Exception("Permintaan harus status APP dan CSB");
        }

        String form_name = "permintaan_form";

        if ("APP".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "persetujuan_form";
        } else if ("CSB".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "realisasi_form";
        } else if ("RFD".equalsIgnoreCase(getRequest().getStStatus())) {
            form_name = "refund_form";
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm(form_name, this);

        form.setRePrintMode(true);

        form.editRequest(arreqid);

        form.getArrequest().setStPrintFlag(null);

        form.setReadOnly(true);

        form.show();
    }

    public void clickCreateRefund() throws Exception {

        validateReqID();

        final RequestFeeForm form = (RequestFeeForm) newForm("refund_form", this);

        form.editCreateRefund(arreqid);

        form.show();
    }

    public void clickCreateRealized() throws Exception {

        final RequestFeeForm form = (RequestFeeForm) newForm("realized_form", this);

        form.clickCreateRealized();

        form.show();
    }

    public void clickCreateRealizedFromProposal() throws Exception {

        validateReqID();

        final RequestFeeForm form = (RequestFeeForm) newForm("realized_form", this);

        form.editCreateRealized(arreqid);

        form.show();
    }

    public void clickPaidValidate() throws Exception {
        validateReqID();

        if (!getRequest().isEffective()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        if (getRequest().isCashierFlag()) {
            throw new Exception("Anggaran Sudah Divalidasi");
        }

        final RequestFeeForm form = (RequestFeeForm) newForm("realizedpaid_form", this);

        form.receiptRequest(arreqid);

        form.setReceiptMode(true);

        form.show();
    }

    public void clickEditRealized() throws Exception {
        validateReqID();

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.setApprovalMode(false);

        form.clickEdit(arreqid);

        form.show();
    }

    public void clickViewRealized() throws Exception {
        validateReqID();

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.clickView(arreqid);

        form.setApprovalMode(false);

        form.show();

    }

    public void clickAppRealized() throws Exception {
        validateReqID();

        if (getRequest().isEffective()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.clickApproval(arreqid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickValRealized() throws Exception {
        validateReqID();

        if (getRequest().isEffective()) {
            throw new Exception("Anggaran Sudah Divalidasi Pimpinan Pengguna");
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.clickApprovalVal(arreqid);

        form.setValidasiRealzMode(true);

        form.show();
    }
    private DTOList listvalidasi;

    /**
     * @return the listval
     */
    public DTOList getListvalidasi() throws Exception {
        if (listvalidasi == null) {
            listvalidasi = new DTOList();
            listvalidasi.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.*,d.user_name as cashier_name ");

        sqa.addQuery(
                " from ar_request_fee a "
                + " left join s_users d on d.user_id = a.cashier_who ");

        sqa.addClause("a.act_flag='Y' and a.validasi_f = 'Y'");
        sqa.addClause("a.status = 'REALISASI'");
        sqa.addClause("a.deleted is null");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("a.act_flag='Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("a.act_flag='N'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("a.cashier_flag = 'N'");
        }

        if (dtReqDateFrom != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) >= ?");
            sqa.addPar(dtReqDateFrom);
        }

        if (dtReqDateTo != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) <= ?");
            sqa.addPar(dtReqDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        sqa.addFilter(listvalidasi.getFilter());

        sqa.addOrder("a.req_id desc");

        sqa.setLimit(40);

        listvalidasi = sqa.getList(ARRequestFee.class);

        return listvalidasi;
    }

    /**
     * @param listval the listval to set
     */
    public void setListvalidasi(DTOList listvalidasi) {
        this.listvalidasi = listvalidasi;
    }

    public void clickViewValidate() throws Exception {
        validateReqID();

        RequestFeeForm form = (RequestFeeForm) super.newForm("validate_form", this);

        form.clickView(arreqid);

        form.setApprovalMode(false);

        form.show();

    }
    private DTOList listproposal;

    /**
     * @return the proposallist
     */
    public DTOList getListproposal() throws Exception {

        if (listproposal == null) {
            listproposal = new DTOList();
            listproposal.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.*,c.user_name as approved_name ");
//
//        sqa.addQuery(
//                " from ar_request_fee a "
//                + " left join s_users c on c.user_id = a.approved_who ");

        sqa.addSelect(" a.*,coalesce(b.approved,'') || coalesce(b.notapproved,'') as approved_name ");

        sqa.addQuery(" from ar_request_fee a "
                + "left join ( "
                + "select a.in_id,'<br><br>belum setujui :<br>'||(select STRING_AGG(CASE WHEN a.eff_flag = 'N' "
                + " THEN b.job_position END, ', ')) AS notapproved, "
                + "'sudah setujui :<br>'||coalesce((select STRING_AGG(CASE WHEN a.eff_flag = 'Y'"
                + " THEN b.job_position END, ', ')),'') AS approved "
                + "from ar_request_approval a inner join s_users b on b.user_id = a.approval_who "
                + "where a.delete_flag is null group by a.in_id ) b on b.in_id = a.req_id  ");

        sqa.addClause("a.status = 'PENGAJUAN'");
        sqa.addClause("a.deleted is null");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("a.act_flag='Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("a.act_flag='N'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("(a.validasi_f='N' or a.validasi_f is null)");
        }

        if (dtReqDateFrom != null) {
            sqa.addClause("date_trunc('day',a.request_date) >= ?");
            sqa.addPar(dtReqDateFrom);
        }

        if (dtReqDateTo != null) {
            sqa.addClause("date_trunc('day',a.request_date) <= ?");
            sqa.addPar(dtReqDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        sqa.addFilter(listproposal.getFilter());

        sqa.addOrder("a.req_id desc");

        sqa.setLimit(40);

        listproposal = sqa.getList(ARRequestFee.class);

        return listproposal;
    }

    /**
     * @param proposallist the proposallist to set
     */
    public void setListproposal(DTOList listproposal) {
        this.listproposal = listproposal;
    }

    public void clickCreateProposal() throws Exception {

        final RequestFeeForm form = (RequestFeeForm) newForm("proposal_form", this);

        form.clickCreateProposal();

        form.show();
    }

    public void clickEditProposal() throws Exception {
        validateReqID();

        boolean checkf = true;
        if (isOwnerAdm() || isOwnerPms() || isOwnerUmum()) {
            checkf = false;
        }

        if (checkf) {
            if (getRequest().isEffective()) {
                throw new Exception("Anggaran Sudah Divalidasi Pimpinan Pengguna");
            }
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);

        form.setApprovalMode(false);

        form.clickEdit(arreqid);

        form.show();
    }

    public void clickViewProposal() throws Exception {
        validateReqID();

        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);

        form.clickView(arreqid);

        form.setApprovalMode(false);

        form.show();

    }

    public void clickAppProposal() throws Exception {
        validateReqID();

        if (!getRequest().isEffective()) {
            throw new Exception("Anggaran Belum Divalidasi Pimpinan Pengguna");
        }

        if (getRequest().isValidasiFlag()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);

        form.clickApproval(arreqid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickValidasi() throws Exception {
        validateReqID();

        if (getRequest().isEffective()) {
            throw new Exception("Anggaran Sudah Divalidasi Pimpinan Pengguna");
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);

        form.clickApprovalVal(arreqid);

        form.setValidasiMode(true);

        form.show();
    }

//    public void clickAppProposalDireksi() throws Exception {
//        validateReqID();
//
//        if (getRequest().isEffective()) {
//            throw new Exception("Anggaran Sudah Disetujui");
//        }
//
//        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);
//
//        form.clickApprovalDireksi(arreqid);
//
//        form.setApprovalByDireksiMode(true);
//
//        form.show();
//    }
    /**
     * @return the ownerPms
     */
    public boolean isOwnerPms() {
        return ownerPms;
    }

    /**
     * @param ownerPms the ownerPms to set
     */
    public void setOwnerPms(boolean ownerPms) {
        this.ownerPms = ownerPms;
    }

    /**
     * @return the ownerUmum
     */
    public boolean isOwnerUmum() {
        return ownerUmum;
    }

    /**
     * @param ownerUmum the ownerUmum to set
     */
    public void setOwnerUmum(boolean ownerUmum) {
        this.ownerUmum = ownerUmum;
    }

    /**
     * @return the ownerAdm
     */
    public boolean isOwnerAdm() {
        return ownerAdm;
    }

    /**
     * @param ownerAdm the ownerAdm to set
     */
    public void setOwnerAdm(boolean ownerAdm) {
        this.ownerAdm = ownerAdm;
    }

    public void clickEditAppProposal() throws Exception {
        validateReqID();

        boolean checkf = true;
        if (isOwnerAdm() || isOwnerPms() || isOwnerUmum()) {
            checkf = false;
        }

        if (checkf) {
            if (getRequest().isEffective()) {
                throw new Exception("Anggaran Sudah Divalidasi Pimpinan Pengguna");
            }
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("proposal_form", this);

        form.setApprovalMode(false);

        form.clickEditApp(arreqid);

        form.show();
    }

    public void clickEditAppRealized() throws Exception {
        validateReqID();

        boolean checkf = true;
        if (isOwnerAdm() || isOwnerPms() || isOwnerUmum()) {
            checkf = false;
        }

        if (checkf) {
            if (getRequest().isEffective()) {
                throw new Exception("Anggaran Sudah Divalidasi Pimpinan Pengguna");
            }
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.setApprovalMode(false);

        form.clickEditApp(arreqid);

        form.show();
    }

    public void clickAppRealisasi() throws Exception {
        validateReqID();

        if (!getRequest().isEffective()) {
            throw new Exception("Anggaran Belum Divalidasi Pimpinan Pengguna");
        }

        if (getRequest().isValidasiFlag()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("realized_form", this);

        form.clickApproval(arreqid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickCashflow() throws Exception {
        validateReqID();

        if (getRequest().isStatusProposal()) {
            if (!getRequest().isValidasiFlag()) {
                throw new Exception("Anggaran Belum Disetujui Pemilik Anggaran");
            }
        }

        if (getRequest().isStatusRealized()) {
            if (getRequest().isValidasiFlag()) {
                throw new Exception("Anggaran Harus status PROPOSAL");
            }
        }

        RequestFeeForm form = (RequestFeeForm) super.newForm("cashflow_edit", this);

        form.clickEditCashflow(arreqid);

        form.setCashflowMode(true);

        form.show();
    }

    public void clickValidate() throws Exception {
        validateReqID();

        if (getRequest().isStatusProposal()) {
            if (getRequest().isValidasiFlag()) {
                throw new Exception("Anggaran Harus status REALIZED");
            }
        }

        if (getRequest().isStatusRealized()) {
            if (!getRequest().isValidasiFlag()) {
                throw new Exception("Anggaran Belum Disetujui Pemilik Anggaran");
            }
        }

//        RequestFeeForm form = (RequestFeeForm) super.newForm("realizedpaid_form", this);
        RequestFeeForm form = (RequestFeeForm) super.newForm("validate_form", this);

        form.clickEditValidasi(arreqid);

        form.setValFinanceMode(true);

        form.show();
    }

    public void clickAppValidate() throws Exception {
        validateReqID();
        String formname = null;

        if (getRequest().isStatusProposal()) {
            if (getRequest().getDtCashflowStart() == null) {
                throw new Exception("Tanggal Cashflow belum diinput");
            }
            formname = "cashflow_edit";
        }

        if (getRequest().isStatusRealized()) {
            if (getRequest().getStAccountNo() == null) {
                throw new Exception("Bank belum diinput");
            }
            formname = "validate_form";
        }

        final RequestFeeForm form = (RequestFeeForm) newForm(formname, this);

        form.receiptRequest(arreqid);

        form.setCashierMode(true);

        form.show();
    }

    /**
     * @return the stCashflowFlag
     */
    public String getStCashflowFlag() {
        return stCashflowFlag;
    }

    /**
     * @param stCashflowFlag the stCashflowFlag to set
     */
    public void setStCashflowFlag(String stCashflowFlag) {
        this.stCashflowFlag = stCashflowFlag;
    }
    private DTOList listcashflow;

    /**
     * @return the listcashflow
     */
    public DTOList getListcashflow() throws Exception {

        if (listcashflow == null) {
            listcashflow = new DTOList();
            listcashflow.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.*,e.user_name as cashier_name ");

        sqa.addQuery(
                " from ar_request_fee a "
                + " left join s_users e on e.user_id = a.cashflow_who ");

        sqa.addClause("a.act_flag='Y' and a.validasi_f = 'Y'");
        sqa.addClause("a.status = 'PENGAJUAN'");
        sqa.addClause("a.deleted is null");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("a.act_flag='Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("a.act_flag='N'");
        }

        if (Tools.isYes(stCashflowFlag)) {
            sqa.addClause("a.cashflow_f = 'N'");
        }

        if (dtReqDateFrom != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) >= ?");
            sqa.addPar(dtReqDateFrom);
        }

        if (dtReqDateTo != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) <= ?");
            sqa.addPar(dtReqDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        sqa.addFilter(listcashflow.getFilter());

        sqa.addOrder("a.req_id desc");

        sqa.setLimit(40);

        listcashflow = sqa.getList(ARRequestFee.class);

        return listcashflow;
    }

    /**
     * @param listcashflow the listcashflow to set
     */
    public void setListcashflow(DTOList listcashflow) {
        this.listcashflow = listcashflow;
    }
}

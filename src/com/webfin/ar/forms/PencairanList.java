/***********************************************************************
 * Module:  com.webfin.ar.forms.PencairanList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.*;
import java.math.BigDecimal;

import java.util.Date;

public class PencairanList extends Form {

    private ARInvestmentPencairanView pencairan;
    private DTOList list;
    private String arcairid;
    private String stNodefo;
    private String stActiveFlag = "Y";
    private String stEffectiveFlag = "Y";
    private BigDecimal dbNominal;
    private String stCurrencyDesc;
    private Date dtCairDateFrom;
    private Date dtCairDateTo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("CAIR_NAVBR");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("CAIR_APRV");
    private final static transient LogManager logger = LogManager.getInstance(PencairanList.class);
    private DTOList cairlist;

    public DTOList getList() throws Exception {

        if (list == null) {
            list = new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ar_cair_id desc";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery(
                "from ar_inv_pencairan");

//        sqa.addClause("ar_izincair_id is null");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("active_flag='Y'");
            sqa.addClause("deleted is null");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("deleted is null");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("(effective_flag='N' or effective_flag is null)");
        }

//        if (Tools.isNo(stEffectiveFlag)) {
//            sqa.addClause("a.effective_flag='Y'");
//        }

        if (dtCairDateFrom != null) {
            sqa.addClause("date_trunc('day',tglcair) >= ?");
            sqa.addPar(dtCairDateFrom);
        }

        if (dtCairDateTo != null) {
            sqa.addClause("date_trunc('day',tglcair) <= ?");
            sqa.addPar(dtCairDateTo);
        }

        if (stNodefo != null) {
            sqa.addClause("nodefo = ?");
            sqa.addPar(stNodefo);
        }

        if (stBranch != null) {
            sqa.addClause("koda = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("ar_cair_id desc");

        sqa.setLimit(100);

        list = sqa.getList(ARInvestmentPencairanView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreate() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() != null) {
            throw new RuntimeException("Pencairan Tidak Bisa Dibuat, No. Bukti Pencairan Sudah Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);

        form.edit(arcairid);

        form.show();
    }

    public void clickCreateTanpaJurnal() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);

        //form.createNew();
        form.edit(arcairid);

        form.show();

    }

    public void clickEdit() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() == null) {
            throw new RuntimeException("No. Bukti Pencairan Belum Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);

        form.edit(arcairid);

        form.show();
    }

    public void clickView() throws Exception {
        validatePencairanID();

        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);

        form.view(arcairid);

        form.show();

    }

    public void clickApproval() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() == null) {
            throw new RuntimeException("No. Bukti Pencairan Belum Ada");
        }

        if (getPencairan().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);

        form.view(arcairid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickReverse() throws Exception {

        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (!getPencairan().isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (getPencairan().getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new Exception("Data tidak bisa di-Reverse karena HISTORY, klik Ubah");
        }

        final PencairanForm form = (PencairanForm) newForm("pencairan_form", this);

        form.view(arcairid);

        form.setReverseMode(true);

        form.show();
    }

    public void list() {
    }

    public void refresh() {
    }

    public String getStNodefo() {
        return stNodefo;
    }

    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    public String getStCurrencyDesc() {
        return stCurrencyDesc;
    }

    public void setStCurrencyDesc(String stCurrencyDesc) {
        this.stCurrencyDesc = stCurrencyDesc;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public String getArcairid() {
        return arcairid;
    }

    public void setArcairid(String arcairid) {
        this.arcairid = arcairid;
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    private void validatePencairanID() {
        if (arcairid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    public Date getDtCairDateFrom() {
        return dtCairDateFrom;
    }

    public void setDtCairDateFrom(Date dtCairDateFrom) {
        this.dtCairDateFrom = dtCairDateFrom;
    }

    public Date getDtCairDateTo() {
        return dtCairDateTo;
    }

    public void setDtCairDateTo(Date dtCairDateTo) {
        this.dtCairDateTo = dtCairDateTo;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    private ARInvestmentPencairanView getPencairan() {
        pencairan = (ARInvestmentPencairanView) DTOPool.getInstance().getDTO(ARInvestmentPencairanView.class, arcairid);
        return pencairan;
    }

    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the cairlist
     */
    public DTOList getCairlist() throws Exception {
        if (cairlist == null) {
            cairlist = new DTOList();
            cairlist.getFilter().activate();
            //list.getFilter().orderKey="ar_cair_id desc";
        }

        SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect("*");
        sqa.addSelect(" a.ar_cair_id,a.active_flag,a.effective_flag,a.nodefo,a.bukti_c,a.bukti_b,a.tglcair,a.nominal,b.no_surat,"
                + "(select string_agg(x.pencairan_ket,'|') from ar_izin_pencairan_detail x where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'REALISASI' and x.delete_flag is null "
                + "and x.ar_depo_id = (select x.ar_depo_id from ar_izin_pencairan_detail x where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'INVOICE' "
                + "and x.nodefo = a.nodefo and x.delete_flag is null )) as regbentuk,"
                //                + "(select string_agg(x.receipt_no,'|') from ar_izin_pencairan_detail x where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'REALISASI' and x.delete_flag is null "
                //                + "and x.ar_depo_id = (select x.ar_depo_id from ar_izin_pencairan_detail x where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'INVOICE' "
                //                + "and x.nodefo = a.nodefo and x.delete_flag is null )) as realisasi_nobuk "
                + "(select string_agg(x.receipt_no,'|') from ar_invoice x where x.ar_invoice_id in (select x.ar_invoice_id from ar_izin_pencairan_detail x "
                + "where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'REALISASI'  and x.delete_flag is null and x.ar_depo_id = "
                + "(select x.ar_depo_id from ar_izin_pencairan_detail x where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'INVOICE' "
                + "and x.nodefo = a.nodefo and x.delete_flag is null )))"
                + "||'|'||"
                + "(select x.trx_no from gl_je_detail x where x.pol_no in (select x.ar_depo_id::text from ar_izin_pencairan_detail x "
                + "where x.ar_izincair_id = a.ar_izincair_id and x.line_type = 'INVOICE' and x.nodefo = a.nodefo and x.delete_flag is null) "
                + "group by x.trx_no) as realisasi_nobuk ");

//        sqa.addQuery("from ar_inv_pencairan");
        sqa.addQuery("from ar_inv_pencairan a "
                + "left join ar_izin_pencairan b on b.ar_izincair_id = a.ar_izincair_id ");

        sqa.addClause("a.ar_izincair_id is not null");
        sqa.addClause("b.approvedcab_flag = 'Y' and b.approvedpus_flag = 'Y'");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("a.active_flag='Y'");
            sqa.addClause("a.deleted is null");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("a.deleted is null");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("(coalesce(a.effective_flag,'N') <> 'Y' or a.realisasi_nobuk is null)");
        }

//        if (Tools.isNo(stEffectiveFlag)) {
//            sqa.addClause("a.effective_flag='Y'");
//        }

        if (dtCairDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tglcair) >= ?");
            sqa.addPar(dtCairDateFrom);
        }

        if (dtCairDateTo != null) {
            sqa.addClause("date_trunc('day',a.tglcair) <= ?");
            sqa.addPar(dtCairDateTo);
        }

        if (stNodefo != null) {
            sqa.addClause("a.nodefo = ?");
            sqa.addPar(stNodefo);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(cairlist.getFilter());

//        sqa.addOrder("a.ar_cair_id desc");
        sqa.addOrder("b.ar_izincair_id desc,a.ar_izincairdet_id desc");

        sqa.setLimit(100);

        cairlist = sqa.getList(ARInvestmentPencairanView.class);

        return cairlist;
    }

    /**
     * @param cairlist the cairlist to set
     */
    public void setCairlist(DTOList cairlist) {
        this.cairlist = cairlist;
    }

    public void clickCreateIzin() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() != null) {
            throw new RuntimeException("Pencairan Tidak Bisa Dibuat, No. Bukti Pencairan Sudah Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("cairdepo_form", this);

        form.editIzin(arcairid);

        form.show();
    }

    public void clickEditIzin() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() == null) {
            throw new RuntimeException("No. Bukti Pencairan Belum Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("cairdepo_form", this);

        form.editIzin(arcairid);

        form.show();
    }

    public void clickRealisasiIzin() throws Exception {
        validatePencairanID();

        if (getPencairan().getStRealisasiNobuk() != null) {
            throw new RuntimeException("Realisasi Pembayaran Sudah Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("cairdepo_real_form", this);

        form.realisasiIzin(arcairid);

//        form.setReceiptMode(true);

        form.show();
    }
    private String stPaymentMode;

    /**
     * @return the stPaymentMode
     */
    public String getStPaymentMode() {
        return stPaymentMode;
    }

    /**
     * @param stPaymentMode the stPaymentMode to set
     */
    public void setStPaymentMode(String stPaymentMode) {
        this.stPaymentMode = stPaymentMode;
    }

    public void clickCreatePaymentByMode() throws Exception {
        validatePencairanID();
//
//        String form_name = "policy_edit";
//        if (printingLOV.equalsIgnoreCase("CLAIM")) {
//            form_name = "policy_edit_claim";
//        }
//        if (printingLOV.equalsIgnoreCase("REAS")) {
//            form_name = "policy_edit_reinsurance";
//        }
//        final PolicyForm form = (PolicyForm) newForm(form_name, this);
        PencairanForm form = (PencairanForm) super.newForm("pencairan_form", this);
//
//        if (getStEndorseMode() == null) {
//            throw new RuntimeException("Jenis endorse harus dipilih");
//        }
//
//        if (getStEndorseMode().equalsIgnoreCase("TOTAL")) {
//            form.editCreateEndorseBatalTotalMode(policyID);
//            form.setTotalEndorseMode(true);
//            form.getPolicy().setStEndorseMode("TOTAL");
//        } else if (getStEndorseMode().equalsIgnoreCase("PARTIAL_SI")) {
//            form.editCreateEndorseDescriptionMode(policyID);
//            form.setPartialEndorseTSIMode(true);
//            form.getPolicy().setStEndorseMode("PARTIAL_SI");
//            form.getPolicy().setStEndorseNotes("ENDORSE PERUBAHAN TSI");
//        }

        form.show();
    }

    public void clickCreateBentukUlang() throws Exception {
        validatePencairanID();

        if (!getPencairan().isActiveCairFlag()) {
            //if (!getPencairan().isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
            //}
        }

        if (getPencairan().getStBuktiC() == null) {
            throw new RuntimeException("No. Bukti Pencairan Belum Ada");
        }

        PencairanForm form = (PencairanForm) super.newForm("bentukulangdepo_form", this);

        form.editBentukUlang(arcairid);

        form.show();
    }
}

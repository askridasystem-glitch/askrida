/***********************************************************************
 * Module:  com.webfin.ar.forms.PembentukanList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.lang.LanguageManager;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.*;
import java.math.BigDecimal;
import java.sql.ResultSet;

import java.util.Date;

public class PembentukanList extends Form {

    private ARInvestmentDepositoView deposito;
    private DTOList list;
    private String ardepoid;
    private String stNodefo;
    private String stActiveFlag = "Y";
    private String stEffectiveFlag = "Y";
    private BigDecimal dbNominal;
    private String stCurrency;
    private Date dtDepoDateFrom;
    private Date dtDepoDateTo;
    private Date dtEndDate;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("DEPO_NAVBR");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("DEPO_APRV");
    private String stType;
    private String stStatus;
    private final static transient LogManager logger = LogManager.getInstance(PembentukanList.class);
    private boolean izinApprovedCab = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_CAB");
    private boolean izinApprovedPus = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_PUS");
    private DTOList bentuklist;
    private String printingLOV = "";
    private String stPrintForm;
    private String stFontSize;

    public DTOList getList() throws Exception {

        if (list == null) {
            list = new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ar_depo_id";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery(
                "from ar_inv_deposito");

//        sqa.addClause("ar_izindet_id is null");

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

        if (dtDepoDateFrom != null) {
            sqa.addClause("date_trunc('day',tglmuta) >= ?");
            sqa.addPar(dtDepoDateFrom);
        }

        if (dtDepoDateTo != null) {
            sqa.addClause("date_trunc('day',tglmuta) <= ?");
            sqa.addPar(dtDepoDateTo);
        }

        if (dtEndDate != null) {
            sqa.addClause("date_trunc('day',tglakhir) >= ?");
            sqa.addPar(DateUtil.getYear(dtEndDate) + "-" + DateUtil.getMonth2Digit(dtEndDate) + "-01");
        }

        if (dtEndDate != null) {
            sqa.addClause("date_trunc('day',tglakhir) <= ?");
            sqa.addPar(dtEndDate);
        }

        if (stNodefo != null) {
            sqa.addClause("nodefo = ?");
            sqa.addPar(stNodefo);
        }

        if (stBranch != null) {
            sqa.addClause("koda = ?");
            sqa.addPar(stBranch);
        }

        if (stType != null) {
            sqa.addClause("status = ?");
            sqa.addPar(stType);
        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("ar_depo_id desc");

        sqa.setLimit(100);

        list = sqa.getList(ARInvestmentDepositoView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreate() throws Exception {

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukan_form", this);

        form.createNew();

        form.setApprovalMode(false);

        form.show();

    }

    public void clickEdit() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukan_form", this);

        form.edit(ardepoid);

        form.setApprovalMode(false);

        form.show();
    }

    public void clickView() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukan_form", this);

        form.view(ardepoid);

        form.setApprovalMode(false);

        form.show();

    }

    public void clickApproval() throws Exception {
        validateDepoID();

        if (getDeposito().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukan_form", this);

        form.view(ardepoid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickReverse() throws Exception {

        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (getDeposito().getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new Exception("Data tidak bisa di-Reverse karena HISTORY, klik Ubah");
        }

        //if (getDeposito().getStJournalStatus().equalsIgnoreCase("NEW") && !getDeposito().getStKonter().equalsIgnoreCase("0")) {
        //    throw new Exception("Data tidak bisa di-Reverse karena sudah Perpanjangan");
        //}

        final PembentukanForm form = (PembentukanForm) newForm("pembentukan_form", this);

        form.view(ardepoid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickSuperEdit() throws Exception {

        validateDepoID();

        if (!getDeposito().getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new Exception("Tidak Bisa Super Edit");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukan_form", this);

        form.superEdit(ardepoid);

        form.setEnableSuperEdit(true);

        form.show();
    }

    public void createRenewal() throws Exception {

        if (dtEndDate == null) {
            throw new Exception("Tanggal Akhir Belum Diinput");
        }

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "
                + " from ar_inv_deposito "
                + " where date_trunc('day',tglakhir) >= ? and date_trunc('day',tglakhir) <= ? and deleted is null "
                + " and kodedepo = '2' and koda = ? and ar_cair_id is null and active_flag = 'Y' and effective_flag = 'Y' "
                //+ " and nodefo in ('285596','297657','308302','311066','158376','292145','270499','293847','270788','306664','268891','0305096','A366825','A366825','290688','308765','346257','346114','346134','0339421','314759','314783','0333711','D276075','0338152','306201','306875','314926','324677','367620','324911','365483','335297','364476','D276508','335323','367821','363193','0343311','0397888','384117','384119','318465','389162','359920','299699','318985','0435690','443278','460117','414136','412111','0423935','457069','428536','254884','0330984','412354','457512','453405','443671','457309','483139','0481418','D421642','408462','475413','453733','408690','486860','414184','457120','483274','483288','306536','306543','005148','004692','004960','003130','005091','005146','005242','001493','001554','001585','006434','006661','007222','2879','02971','243549','188283','198112','303114','323517','216649','229466','259259','301695','338118','312264','339722','449378','372826','332778','394856','447232','450168','473190','491044','497390','497411','450343','498675','498676','469760','370952','438529','413593','469250','408924','415765','473677','544016','518020','487534','483608','060171','0492','DB0481718','41449','DB465465','DB 473282','DB383069','DB403808','DP367600','DB447363','DB 457217','DB383160','DB338506','DB321105','1919','00219','DB288793','DB0309758','DB 0366236','DB 368406','DB 402531','DB 453702','06380','A 002337','003207','DB 0358239','DB268734','DB361075','DB302798','DB340925','DB237099','DB229022','DB312257')"
                ,//and active_flag = 'Y' ",
                new Object[]{DateUtil.getYear(dtEndDate) + "-" + DateUtil.getMonth2Digit(dtEndDate) + "-01 00:00:00", dtEndDate, stBranch},
                ARInvestmentDepositoView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            ARInvestmentDepositoView depo = (ARInvestmentDepositoView) listPolicy.get(i);

            PembentukanForm form = new PembentukanForm();
            form.createRenewal(depo);

            depo.setStKeterangan("Periode Deposito Yang Habis Pada Tanggal: " + LanguageManager.getInstance().translate(DateUtil.getDateStr(depo.getDtTglakhir(), "dd ^^ yyyy")));
            depo.setStActiveFlag("N");
            depo.markUpdate();
            save(depo);

            final DTOList pencairan = depo.getPencairan();
            for (int j = 0; j < pencairan.size(); j++) {
                ARInvestmentPencairanView cair = (ARInvestmentPencairanView) pencairan.get(j);

                cair.setStActiveFlag("N");
                cair.setStARParentID(cair.getStARCairID());
                cair.markUpdate();

            }

            S.store(pencairan);

            final DTOList bunga = depo.getBunga();
            for (int k = 0; k < bunga.size(); k++) {
                ARInvestmentBungaView bnga = (ARInvestmentBungaView) bunga.get(k);

                bnga.setStARParentID(bnga.getStARBungaID());
                bnga.markUpdate();

            }

            S.store(bunga);
        }

        S.release();
    }

    private void save(ARInvestmentDepositoView depo) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.store(depo);
        } finally {
            S.release();
        }
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

    public String getStCurrency() {
        return stCurrency;
    }

    public void setStCurrency(String stCurrency) {
        this.stCurrency = stCurrency;
    }

    public String getArdepoid() {
        return ardepoid;
    }

    public void setArdepoid(String ardepoid) {
        this.ardepoid = ardepoid;
    }

    public Date getDtDepoDateFrom() {
        return dtDepoDateFrom;
    }

    public void setDtDepoDateFrom(Date dtDepoDateFrom) {
        this.dtDepoDateFrom = dtDepoDateFrom;
    }

    public Date getDtDepoDateTo() {
        return dtDepoDateTo;
    }

    public void setDtDepoDateTo(Date dtDepoDateTo) {
        this.dtDepoDateTo = dtDepoDateTo;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    private void validateDepoID() {
        if (ardepoid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    private ARInvestmentDepositoView getDeposito() {
        deposito = (ARInvestmentDepositoView) DTOPool.getInstance().getDTO(ARInvestmentDepositoView.class, ardepoid);
        return deposito;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
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

    public Date getDtEndDate() {
        return dtEndDate;
    }

    public void setDtEndDate(Date dtEndDate) {
        this.dtEndDate = dtEndDate;
    }

    public void clickCreateBunga() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukanbunga_form", this);

        form.createBunga(ardepoid);

        form.setBungaMode(true);

        form.show();
    }

    public void clickApprovalBunga() throws Exception {
        validateDepoID();

        if (getDeposito().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukanbunga_form", this);

        form.view(ardepoid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickReverseBunga() throws Exception {

        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (getDeposito().getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new Exception("Data tidak bisa di-Reverse karena HISTORY, klik Ubah");
        }

        //if (getDeposito().getStJournalStatus().equalsIgnoreCase("NEW") && !getDeposito().getStKonter().equalsIgnoreCase("0")) {
        //    throw new Exception("Data tidak bisa di-Reverse karena sudah Perpanjangan");
        //}

        final PembentukanForm form = (PembentukanForm) newForm("pembentukanbunga_form", this);

        form.view(ardepoid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickViewBunga() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukanbunga_form", this);

        form.view(ardepoid);

        form.setBungaMode(false);

        form.show();
    }

    /**
     * @return the izinApprovedCab
     */
    public boolean isIzinApprovedCab() {
        return izinApprovedCab;
    }

    /**
     * @param izinApprovedCab the izinApprovedCab to set
     */
    public void setIzinApprovedCab(boolean izinApprovedCab) {
        this.izinApprovedCab = izinApprovedCab;
    }

    /**
     * @return the izinApprovedPus
     */
    public boolean isIzinApprovedPus() {
        return izinApprovedPus;
    }

    /**
     * @param izinApprovedPus the izinApprovedPus to set
     */
    public void setIzinApprovedPus(boolean izinApprovedPus) {
        this.izinApprovedPus = izinApprovedPus;
    }

    /**
     * @return the printingLOV
     */
    public String getPrintingLOV() {
        return printingLOV;
    }

    /**
     * @param printingLOV the printingLOV to set
     */
    public void setPrintingLOV(String printingLOV) {
        this.printingLOV = printingLOV;
    }

    /**
     * @return the stPrintForm
     */
    public String getStPrintForm() {
        return stPrintForm;
    }

    /**
     * @param stPrintForm the stPrintForm to set
     */
    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    /**
     * @return the stFontSize
     */
    public String getStFontSize() {
        return stFontSize;
    }

    /**
     * @param stFontSize the stFontSize to set
     */
    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    /**
     * @return the bentuklist
     */
    public DTOList getBentuklist() throws Exception {

        if (bentuklist == null) {
            bentuklist = new DTOList();
            bentuklist.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ar_inv_deposito");

        sqa.addClause("ar_izindet_id is not null");
//        sqa.addClause("journal_status is not null");

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

        if (stBranch != null) {
            sqa.addClause("koda = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(bentuklist.getFilter());

        sqa.addOrder("ar_depo_id desc");

        sqa.setLimit(100);

        bentuklist = sqa.getList(ARInvestmentDepositoView.class);

        return bentuklist;
    }

    /**
     * @param bentuklist the bentuklist to set
     */
    public void setBentuklist(DTOList bentuklist) {
        this.bentuklist = bentuklist;
    }

    public void clickCreateIzin() throws Exception {
        validateDepoID();

        if (getDeposito().getStBuktiB() != null) {
            throw new RuntimeException("Deposito Tidak Bisa Dibuat, No. Bukti Sudah Ada");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepo_form", this);

        form.editBentuk(ardepoid);

        form.show();
    }

    public void clickEditIzin() throws Exception {
        validateDepoID();

        if (getDeposito().getStBuktiB() == null) {
            throw new RuntimeException("Tidak Bisa Diubah, No. Bukti Deposito Belum Ada");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepo_form", this);

        form.editBentuk(ardepoid);

        form.show();
    }

    public void clickViewIzin() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepo_form", this);

        form.view(ardepoid);

        form.setApprovalMode(false);

        form.show();

    }

    public void clickApprovalBentuk() throws Exception {
        validateDepoID();

        if (getDeposito().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepo_form", this);

        form.view(ardepoid);

        form.setApprovalMode(true);

        form.show();
    }

    public void clickInputIzinTiket() throws Exception {
        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new RuntimeException("Pembentukan belum disetujui, klik UBAH untuk Input Bukti");
        }

        if (getDeposito().getStUpload() != null) {
            throw new RuntimeException("Bukti Bilyet Sudah Ada");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepobukti_form", this);

        form.editInputBilyet(ardepoid);

        form.show();
    }

    public void clickPosisiBilyet() throws Exception {
        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new RuntimeException("Pembentukan belum disetujui, klik UBAH untuk Input Posisi Bilyet");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("bentukdepoposisi_form", this);

        form.editInputBilyet(ardepoid);

        form.show();
    }

    public void clickReverseBentuk() throws Exception {

        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (getDeposito().getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new Exception("Data tidak bisa di-Reverse karena HISTORY, klik Ubah");
        }

        final PembentukanForm form = (PembentukanForm) newForm("bentukdepo_form", this);

        form.view(ardepoid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickCreateCair() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukancair_form", this);

        form.createCair(ardepoid);

        form.setBungaMode(true);

        form.show();
    }

    public void clickViewCair() throws Exception {
        validateDepoID();

        PembentukanForm form = (PembentukanForm) super.newForm("pembentukancair_form", this);

        form.view(ardepoid);

        form.setCairMode(false);

        form.show();
    }

    public void clickEditBilyet() throws Exception {
        validateDepoID();

        if (!getDeposito().isEffective()) {
            throw new RuntimeException("Pembentukan belum disetujui, klik UBAH untuk Ubah Bilyet");
        }

        PembentukanForm form = (PembentukanForm) super.newForm("editbilyet_form", this);

        form.editInputBilyet(ardepoid);

        form.show();
    }
}

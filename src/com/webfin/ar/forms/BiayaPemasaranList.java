/***********************************************************************
 * Module:  com.webfin.ar.forms.BiayaPemasaranList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.model.Filter;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.ar.model.BiayaPemasaranView;
import com.webfin.insurance.model.uploadPiutangPremiView;
import java.math.BigDecimal;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class BiayaPemasaranList extends Form {

    private String description;
    private String branch;
    private String memorialID;
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("PROP_CREATE");
    private boolean approvalCab = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_CAB");
    private boolean approvalSie = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_SIE");
    private boolean approvalBag = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_BAG");
    private boolean approvalDiv = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_DIV");
    private boolean approvalPms = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_PMS");
    private uploadPiutangPremiView piutangPremi;
    private String stNotRealized;
    private String stNotApproved;
    private String stShowAll;
    private Date dtFilterEntryDateFrom;
    private Date dtFilterEntryDateTo;
    private DTOList listPemasaran;
    private BiayaPemasaranView pemasaran;
    private String period;
    private String year;
    private BigDecimal dbAmount;
    private BigDecimal dbAmountShare;
    private DTOList listBiapem;

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }
    private final static transient LogManager logger = LogManager.getInstance(BiayaPemasaranList.class);

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

    public void clickRefresh() {
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
     * @return the approvalCab
     */
    public boolean isApprovalCab() {
        return approvalCab;
    }

    /**
     * @param approvalCab the approvalCab to set
     */
    public void setApprovalCab(boolean approvalCab) {
        this.approvalCab = approvalCab;
    }

    /**
     * @return the approvalSie
     */
    public boolean isApprovalSie() {
        return approvalSie;
    }

    /**
     * @param approvalSie the approvalSie to set
     */
    public void setApprovalSie(boolean approvalSie) {
        this.approvalSie = approvalSie;
    }

    /**
     * @return the approvalBag
     */
    public boolean isApprovalBag() {
        return approvalBag;
    }

    /**
     * @param approvalBag the approvalBag to set
     */
    public void setApprovalBag(boolean approvalBag) {
        this.approvalBag = approvalBag;
    }

    /**
     * @return the approvalDiv
     */
    public boolean isApprovalDiv() {
        return approvalDiv;
    }

    /**
     * @param approvalDiv the approvalDiv to set
     */
    public void setApprovalDiv(boolean approvalDiv) {
        this.approvalDiv = approvalDiv;
    }

    private void validateDepoID() {
        if (memorialID == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
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
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();

    /**
     * @return the stBranch
     */
    public String getStBranch() {
        return stBranch;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public void refresh() {
    }

    /**
     * @return the stNotApproved
     */
    public String getStNotApproved() {
        return stNotApproved;
    }

    /**
     * @param stNotApproved the stNotApproved to set
     */
    public void setStNotApproved(String stNotApproved) {
        this.stNotApproved = stNotApproved;
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

    /**
     * @return the stNotRealized
     */
    public String getStNotRealized() {
        return stNotRealized;
    }

    /**
     * @param stNotRealized the stNotRealized to set
     */
    public void setStNotRealized(String stNotRealized) {
        this.stNotRealized = stNotRealized;
    }

    /**
     * @return the stShowAll
     */
    public String getStShowAll() {
        return stShowAll;
    }

    /**
     * @param stShowAll the stShowAll to set
     */
    public void setStShowAll(String stShowAll) {
        this.stShowAll = stShowAll;
    }

    /**
     * @return the listPemasaran
     */
    public DTOList getListPemasaran() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        if (listPemasaran == null) {
            listPemasaran = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listPemasaran.setFilter(filter.activate());
        }

        sqa.addSelect(" a.*,b.vs_description as transaksi ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "left join s_valueset b on b.vs_code = a.kd_input::text and b.vs_group = 'TRANSAKSI' ");

        sqa.addClause(" a.filenota is null ");
        sqa.addClause(" a.delete_f is null ");

        if (getStBranch() != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (getTransaksi() != null) {
            sqa.addClause(" a.kd_input = ? ");
            sqa.addPar(transaksi);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        if (Tools.isYes(stNotApproved) && Tools.isNo(stNotRealized)) {
            sqa.addClause(" (coalesce(a.status2,'Y') <> 'Y' or coalesce(a.status3,'Y') <> 'Y' or coalesce(a.status4,'N') <> 'Y') ");
        }

        if (Tools.isYes(stNotRealized) && Tools.isNo(stNotApproved)) {
            sqa.addClause(" a.no_bukti_bayar is null and a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y' ");
        }

        sqa.addOrder(" pms_id desc ");

        sqa.setLimit(100);

        sqa.addFilter(listPemasaran.getFilter());

        listPemasaran = sqa.getList(BiayaPemasaranView.class);

//        listComm = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                uploadProposalCommView.class);

        return listPemasaran;
    }

    /**
     * @param listPemasaran the listPemasaran to set
     */
    public void setListPemasaran(DTOList listPemasaran) {
        this.listPemasaran = listPemasaran;
    }

    /**
     * @return the pemasaran
     */
    public BiayaPemasaranView getPemasaran() {
        return pemasaran;
    }

    /**
     * @param pemasaran the pemasaran to set
     */
    public void setPemasaran(BiayaPemasaranView pemasaran) {
        this.pemasaran = pemasaran;
    }

    public void clickCreatePemasaran() throws Exception {

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.createBiayaPemasaran();

        form.show();

    }

    public void clickEditPemasaran() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.editPemasaran(memorialID);

        form.setEditMode(true);

        form.show();

    }

    public void clickViewPemasaran() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.viewPemasaran(memorialID);

        //form.setReadOnly(true);

        form.setViewMode(true);

        form.show();

    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the dbAmount
     */
    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    /**
     * @param dbAmount the dbAmount to set
     */
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    /**
     * @return the dbAmountShare
     */
    public BigDecimal getDbAmountShare() {
        return dbAmountShare;
    }

    /**
     * @param dbAmountShare the dbAmountShare to set
     */
    public void setDbAmountShare(BigDecimal dbAmountShare) {
        this.dbAmountShare = dbAmountShare;
    }

    public void onChangeAmount() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi, "
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqa.addQuery(" from ins_policies a "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqa.addClause("a.pol_type_id in (21,59)");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (period != null) {
            sqa.addClause("substr(a.policy_date::text,6,2) = ?");
            sqa.addPar(period);
        }

        if (year != null) {
            sqa.addClause("substr(a.policy_date::text,1,4) = ?");
            sqa.addPar(year);
        }

        String sql = "select a.cc_code,(a.premi-a.diskon) as biaya "
                + "from ( " + sqa.getSQL() + " group by a.cc_code  ) a ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        BiayaPemasaranView close = (BiayaPemasaranView) l.get(0);

        setDbAmount(close.getDbBiaya());
        setDbAmountShare(BDUtil.mul(getDbAmount(), new BigDecimal(0.01)));
    }

    public void clickApprovalPms1() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.approvalPms1(memorialID);

        form.setApprovalCab(true);

        form.show();
    }

    public void clickApprovalPms2() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.approvalPms2(memorialID);

        form.setApprovalSie(true);

        form.show();
    }

    public void clickApprovalPms3() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.approvalPms3(memorialID);

        form.setApprovalBag(true);

        form.show();
    }

    public void clickApprovalPms4() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.approvalPms4(memorialID);

        form.setApprovalDiv(true);

        form.show();
    }

    public void clickReversePmsCab() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.reversePmsCab(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickReversePmsSie() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.reversePmsSie(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickReceiptPemasaran() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_bayar_form", this);

        form.receiptPemasaran(memorialID);

        form.setReceiptMode(true);

        form.show();
    }

    public void clickPrintReloadPms() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("pemasaran_form", this);

        form.viewPemasaran(memorialID);

        form.setViewMode(true);

        form.show();
    }
    private String transaksi;

    /**
     * @return the transaksi
     */
    public String getTransaksi() {
        return transaksi;
    }

    /**
     * @param transaksi the transaksi to set
     */
    public void setTransaksi(String transaksi) {
        this.transaksi = transaksi;
    }

    public void clickCreatePms() throws Exception {

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_form", this);

        form.createBiayaPemasaran();

        form.show();

    }

    public void clickEditPms() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_form", this);

        form.editPemasaran(memorialID);

        form.setEditMode(true);

        form.show();

    }

    public void clickViewPms() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.viewPemasaran(memorialID);

        //form.setReadOnly(true);

        form.setViewMode(true);

        form.show();

    }

    public void clickReceiptPms() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.receiptPemasaran(memorialID);

        form.setReceiptMode(true);

        form.show();
    }

    public void clickAppPms1() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.approvalPms1New(memorialID);

        form.setApprovalCab(true);

        form.show();
    }

    public void clickAppPms2() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.approvalPms2(memorialID);

        form.setApprovalSie(true);

        form.show();
    }

    public void clickAppPms3() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.approvalPms3(memorialID);

        form.setApprovalBag(true);

        form.show();
    }

    public void clickAppPms4() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.approvalPms4(memorialID);

        form.setApprovalDiv(true);

        form.show();
    }

    public void clickRevPmsCab() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.reversePmsCab(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickRevPmsSie() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.reversePmsSie(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickPrintReloadPms2() throws Exception {
        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_form", this);

        form.viewPemasaran(memorialID);

        form.setViewMode(true);

        form.show();
    }

    public void clickValidasiPms() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_bayar_form", this);

        form.viewPemasaran(memorialID);

        form.setValidasiMode(true);

        form.show();
    }

    /**
     * @return the listBiapem
     */
    public DTOList getListBiapem() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listBiapem == null) {
            listBiapem = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listBiapem.setFilter(filter.activate());
        }

//        sqa.addSelect(" a.*,b.vs_description as transaksi ");
        sqa.addSelect(" a.pms_id,a.months,a.years,a.no_spp,a.validasi_f,a.nota_f,a.status1,a.status2,a.status3,a.status4,"
                + "a.cc_code,a.entry_date,a.total_biaya,a.jumlah_data,a.ket,a.no_bukti as no_bukti,"
                + "a.no_bukti_bayar as no_bukti_bayar,a.create_who,b.vs_description as transaksi ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "left join s_valueset b on b.vs_code = a.kd_input::text and b.vs_group = 'TRANSAKSI' ");

        sqa.addClause(" a.filenota is not null ");
        sqa.addClause(" a.delete_f is null ");

        if (getStBranch() != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (getTransaksi() != null) {
            sqa.addClause(" a.kd_input = ? ");
            sqa.addPar(transaksi);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        if (Tools.isYes(stNotApproved) && Tools.isNo(stNotRealized)) {
            sqa.addClause(" (coalesce(a.status2,'Y') <> 'Y' or coalesce(a.status3,'Y') <> 'Y' or coalesce(a.status4,'N') <> 'Y') ");
        }

        if (Tools.isYes(stNotRealized) && Tools.isNo(stNotApproved)) {
            sqa.addClause(" a.no_bukti_bayar is null and a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y' ");
        }

        sqa.addOrder(" pms_id desc ");

        sqa.setLimit(100);

        sqa.addFilter(listBiapem.getFilter());

        listBiapem = sqa.getList(BiayaPemasaranView.class);

        return listBiapem;
    }

    /**
     * @param listBiapem the listBiapem to set
     */
    public void setListBiapem(DTOList listBiapem) {
        this.listBiapem = listBiapem;
    }

    /**
     * @return the approvalPms
     */
    public boolean isApprovalPms() {
        return approvalPms;
    }

    /**
     * @param approvalPms the approvalPms to set
     */
    public void setApprovalPms(boolean approvalPms) {
        this.approvalPms = approvalPms;
    }

    public void clickNotaPms() throws Exception {
        validateDepoID();

        final BiayaPemasaranForm form = (BiayaPemasaranForm) newForm("biapem_nota_form", this);

        form.editPemasaranNota(memorialID);

        form.setNotaMode(true);

        form.show();
    }
}

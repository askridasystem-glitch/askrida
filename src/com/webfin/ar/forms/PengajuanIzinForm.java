/***********************************************************************
 * Module:  com.webfin.ar.forms.PengajuanIzinForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.controller.FormTab;
import com.crux.common.model.HashDTO;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

public class PengajuanIzinForm extends Form {

    private ARInvestmentDepositoIndexView depositoindex;
    private ARInvestmentIzinDepositoDetailView depodetail;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean approvalMode;
    private boolean reverseMode;
    private boolean enableSuperEdit;
    private final static transient LogManager logger = LogManager.getInstance(PembentukanForm.class);
    private ARInvestmentIzinDepositoView izindeposito = null;
    private FormTab tabdeposito;
    private String stSelectedObject;
    private boolean createMode;
    private boolean editMode;
    private boolean viewMode;
    private boolean approvedMode;
    private ARInvestmentIzinPencairanView izinpencairan;
    private ARInvestmentIzinPencairanDetView pencairandetail;
    private ARInvestmentIzinBungaView izinbunga;
    private boolean inputBuktiMode;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void close() {
        super.close();
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

//    public void onChgCurrency() throws Exception {
//        depodetail.setDbCurrencyRate(
//                CurrencyManager.getInstance().getRate(
//                depodetail.getStCurrency(),
//                depodetail.getDtTgldepo()));
//        depodetail.setStCurrency(depodetail.getStCurrency());
//        depodetail.setDbNominal(BDUtil.mul(depodetail.getDbCurrencyRate(), depodetail.getDbNominalKurs()));
//    }
    public void onChgCall() throws Exception {

        if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
            depodetail.setStHari(depodetail.getStHari());
            depodetail.setStBulan("0");
        } else {
            depodetail.setStHari("0");
            depodetail.setStBulan(depodetail.getStBulan());
        }

    }

    public void validate2() throws Exception {
        if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
            calcDays();
        } else if (depodetail.getStKodedepo().equalsIgnoreCase("2")) {
            calcMonths();
        }
    }

    public void refresh() {
        depodetail.setDbNominal(BDUtil.mul(depodetail.getDbCurrencyRate(), depodetail.getDbNominalKurs()));
    }

    public void calcDays() throws Exception {

        if (Tools.compare(depodetail.getStHari(), new String("20")) > 0) {
            throw new RuntimeException("Jangka waktu On Call tidak boleh melebihi 20 Hari");
        }

        if (depodetail.getStHari() != null) {
            DateTime startDate = new DateTime(depodetail.getDtTglawal());
            DateTime endDate = new DateTime();

            if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
                endDate = startDate.plusDays(Integer.parseInt(depodetail.getStHari()));
            }

            depodetail.setDtTglakhir(endDate.toDate());
        }
    }

    public void calcMonths() throws Exception {

        if (Tools.compare(depodetail.getStBulan(), new String("12")) > 0) {
            throw new RuntimeException("Jangka waktu Non On Call tidak boleh melebihi 12 Bulan");
        }

        if (depodetail.getStBulan() != null) {
            DateTime startDate = new DateTime(depodetail.getDtTglawal());
            DateTime endDate = new DateTime();

            if (depodetail.getStKodedepo().equalsIgnoreCase("2")) {
                endDate = startDate.plusMonths(Integer.parseInt(depodetail.getStBulan()));
            }

            depodetail.setDtTglakhir(endDate.toDate());
        }
    }

    public void validate() {
        /*
        if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
        if (depodetail.getStHari()==null) throw new RuntimeException("Jumlah Hari Belum Diisi");
        } else {
        if (depodetail.getStBulan()==null) throw new RuntimeException("Jumlah Bulan Belum Diisi");
        }*/

        Date policyDateStart = depodetail.getDtTglawal();
        Date policyDateEnd = depodetail.getDtTglakhir();

        DateTime startDate = new DateTime(policyDateStart);
        DateTime endDate = new DateTime(policyDateEnd);

        Days z = Days.daysBetween(startDate, endDate);
        int day = z.getDays();

        Months x = Months.monthsBetween(startDate, endDate);
        int month = x.getMonths();

        if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
            if (!depodetail.getStHari().equalsIgnoreCase(Integer.toString(day))) {
                throw new RuntimeException("Tanggal Akhir bilyet tidak sesuai dengan Jangka Waktu On Call,  klik REFRESH");
            }
        }
    }

    public boolean isApprovalMode() {
        return approvalMode;
    }

    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    public boolean isEnableSuperEdit() {
        return enableSuperEdit;
    }

    public void setEnableSuperEdit(boolean enableSuperEdit) {
        this.enableSuperEdit = enableSuperEdit;
    }

    public void selectMonth() {
    }

//    public void onChgPeriod() throws Exception {
//        if (!Tools.isEqual(DateUtil.getMonth2Digit(depodetail.getDtTgldepo()), depodetail.getStMonths())) {
//            throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
//        }
//        if (!Tools.isEqual(DateUtil.getYear(depodetail.getDtTgldepo()), depodetail.getStYears())) {
//            throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
//        }
//    }
    /**
     * @return the izindeposito
     */
    public ARInvestmentIzinDepositoView getIzindeposito() {
        return izindeposito;
    }

    /**
     * @param izindeposito the izindeposito to set
     */
    public void setIzindeposito(ARInvestmentIzinDepositoView izindeposito) {
        this.izindeposito = izindeposito;
    }

    public void createNewIzin() {
        izindeposito = new ARInvestmentIzinDepositoView();

        izindeposito.markNew();
//        izindeposito.setDeposito(new DTOList());
        izindeposito.setDepodetail(new DTOList());

        //izindeposito.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("CREATE IZIN DEPOSITO");

        editMode = true;
        viewMode = false;
        approvedMode = false;
    }

    public void editIzin(String ardepoid) throws Exception {
        viewIzin(ardepoid);

        if (izindeposito.isEffectiveCab()) {
            throw new Exception("Data Sudah Disetujui Cabang");
        }
        if (izindeposito.isEffectivePus()) {
            throw new Exception("Data Sudah Disetujui Pusat");
        }

        setReadOnly(false);

        izindeposito.markUpdate();
        izindeposito.getDepodetail().markAllUpdate();

        setTitle("EDIT IZIN DEPOSITO");

        editMode = true;
        viewMode = false;
        approvedMode = false;
    }

    public void viewIzin(String ardepoid) throws Exception {

        izindeposito = getRemoteGeneralLedger().loadIzinDeposito(ardepoid);

        if (izindeposito == null) {
            throw new RuntimeException("Deposito not found !");
        }

        setReadOnly(true);

        setTitle("VIEW IZIN DEPOSITO");

        editMode = false;
        viewMode = true;
        approvedMode = false;
    }

    public void saveIzin() throws Exception {

        getRemoteGeneralLedger().saveIzin(izindeposito, approvedMode);

        close();
    }

    public void refreshIzin() {
    }

    /**
     * @return the tabsppd
     */
    public FormTab getTabdeposito() {
        if (tabdeposito == null) {
            tabdeposito = new FormTab();

            tabdeposito.add(new FormTab.TabBean("TAB1", "DEPOSITO", true));
            tabdeposito.setActiveTab("TAB1");

        }
        return tabdeposito;
    }

    /**
     * @param tabdeposito the tabdeposito to set
     */
    public void setTabdeposito(FormTab tabdeposito) {
        this.tabdeposito = tabdeposito;
    }

    /**
     * @return the stSelectedObject
     */
    public String getStSelectedObject() {
        return stSelectedObject;
    }

    /**
     * @param stSelectedObject the stSelectedObject to set
     */
    public void setStSelectedObject(String stSelectedObject) {
        this.stSelectedObject = stSelectedObject;
    }

    public void selectObject() throws Exception {
    }

    public void doNewObject() {
//        final ARInvestmentDepositoIndexView adr = new ARInvestmentDepositoIndexView();
        final ARInvestmentIzinDepositoDetailView adr = new ARInvestmentIzinDepositoDetailView();
        adr.markNew();
        adr.setDtTglawal(izindeposito.getDtMutationDate());

//        izindeposito.getDeposito().add(adr);
        izindeposito.getDepodetail().add(adr);

//        stSelectedObject = String.valueOf(izindeposito.getDeposito().size() - 1);
        stSelectedObject = String.valueOf(izindeposito.getDepodetail().size() - 1);

//        depositoindex = adr;
        depodetail = adr;
    }

    public void doDeleteObject() throws Exception {

        int n = Integer.parseInt(stSelectedObject);

//        final DTOList objects = izindeposito.getDeposito();
        final DTOList objects = izindeposito.getDepodetail();

//        final ARInvestmentDepositoIndexView obj = (ARInvestmentDepositoIndexView) objects.get(n);
        final ARInvestmentIzinDepositoDetailView obj = (ARInvestmentIzinDepositoDetailView) objects.get(n);

        objects.delete(n);

//        depositoindex = null;
        depodetail = null;

        setStSelectedObject(null);
    }

    public LOV getLovObjects() {
        return getObjects();
    }

    public DTOList getObjects() {
//        return izindeposito.getDeposito();
        return izindeposito.getDepodetail();
    }

    public void setDateIzin() throws Exception {

        String date = "01/" + izindeposito.getStMonths() + "/" + izindeposito.getStYears();

        izindeposito.setDtMutationDate(DateUtil.getDate(date));
    }

    public void afterUpdateForm() {
        if (izindeposito != null) {
            final Integer idx = NumberUtil.getInteger(stSelectedObject);
//            depositoindex = idx == null ? null : (ARInvestmentDepositoIndexView) izindeposito.getDeposito().get(idx.intValue());
            depodetail = idx == null ? null : (ARInvestmentIzinDepositoDetailView) izindeposito.getDepodetail().get(idx.intValue());

//            final DTOList depositoDet = izindeposito.getDeposito();
            final DTOList depositoDet = izindeposito.getDepodetail();

            izindeposito.setStNodefoSementara(null);

            for (int i = 0; i < depositoDet.size(); i++) {
                ARInvestmentIzinDepositoDetailView adr = (ARInvestmentIzinDepositoDetailView) depositoDet.get(i);

                if (izindeposito.getStNodefoSementara() == null) {
                    izindeposito.setStNodefoSementara(adr.getStNodefoSementara());
                }

                if (adr.isPrimary()) {
                    izindeposito.setStNodefoSementara(adr.getStNodefoSementara());
                    break;
                }
            }
        }
    }
    private boolean approvalCab = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_CAB");
    private boolean approvalPus = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_PUS");

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
     * @return the approvalPus
     */
    public boolean isApprovalPus() {
        return approvalPus;
    }

    /**
     * @param approvalPus the approvalPus to set
     */
    public void setApprovalPus(boolean approvalPus) {
        this.approvalPus = approvalPus;
    }

    public void approvalCab(String ardepoid) throws Exception {

        izindeposito = getRemoteGeneralLedger().loadIzinDeposito(ardepoid);

        if (izindeposito.isEffectiveCab()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;

        editMode = false;
        viewMode = false;
        approvedMode = true;

    }

    public void approvalPus(String ardepoid) throws Exception {

        izindeposito = getRemoteGeneralLedger().loadIzinDeposito(ardepoid);

        if (!izindeposito.isEffectiveCab()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (izindeposito.isEffectivePus()) {
            throw new Exception("Data Sudah disetujui Pusat");
        }

        super.setReadOnly(true);

        approvalPus = true;

        editMode = false;
        viewMode = false;
        approvedMode = true;

    }

    public void approveIzin() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        if (isApprovalCab()) {

            try {
                PreparedStatement PS = S.setQuery("update ar_izin_deposito set approvedcab_flag = 'Y', approvedcab_who = ?, approvedcab_date = ? where ar_izin_id = ?");

                PS.setObject(1, SessionManager.getInstance().getUserID());
                PS.setDate(2, new java.sql.Date(new Date().getTime()));
                PS.setObject(3, izindeposito.getStARIzinID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
        }

        if (isApprovalPus()) {

            try {
                PreparedStatement PS = S.setQuery("update ar_izin_deposito set approvedpus_flag = 'Y', approvedpus_who = ?, approvedpus_date = ? where ar_izin_id = ?");

                PS.setObject(1, SessionManager.getInstance().getUserID());
                PS.setDate(2, new java.sql.Date(new Date().getTime()));
                PS.setObject(3, izindeposito.getStARIzinID());

                int p = PS.executeUpdate();

//                PreparedStatement PS2 = S.setQuery("update ar_inv_deposito set journal_status = 'NEW' where ar_izindet_id = ?");
//
//                PS2.setObject(1, izindeposito.getStARIzinID());
//
//                int p2 = PS2.executeUpdate();

                getRemoteGeneralLedger().saveIzin(izindeposito, approvalPus);

            } finally {
                S.release();
            }
        }

        close();
    }

    /**
     * @return the editMode
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * @param editMode the editMode to set
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * @return the viewMode
     */
    public boolean isViewMode() {
        return viewMode;
    }

    /**
     * @param viewMode the viewMode to set
     */
    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    /**
     * @return the approvedMode
     */
    public boolean isApprovedMode() {
        return approvedMode;
    }

    /**
     * @param approvedMode the approvedMode to set
     */
    public void setApprovedMode(boolean approvedMode) {
        this.approvedMode = approvedMode;
    }

    public void createNewIzinCair() {
        izinpencairan = new ARInvestmentIzinPencairanView();

        izinpencairan.markNew();

        setTitle("CREATE IZIN PENCAIRAN");

        createMode = true;
        editMode = false;
        viewMode = false;
        approvedMode = false;
    }

    public void editIzinCair(String ardepoid) throws Exception {
        viewIzinCair(ardepoid);

        if (izinpencairan.isEffectiveCab()) {
            throw new Exception("Data Sudah Disetujui Cabang");
        }
        if (izinpencairan.isEffectivePus()) {
            throw new Exception("Data Sudah Disetujui Pusat");
        }

        setReadOnly(false);

        izinpencairan.markUpdate();
        izinpencairan.getPencairandet().markAllUpdate();
//        pencairandetail.getListRealisasi().markAllUpdate();

        setTitle("EDIT IZIN PENCAIRAN");

        createMode = false;
        editMode = true;
        viewMode = false;
        approvedMode = false;
    }

    public void viewIzinCair(String ardepoid) throws Exception {

        izinpencairan = getRemoteGeneralLedger().loadIzinPencairan(ardepoid);

        if (izinpencairan == null) {
            throw new RuntimeException("Deposito not found !");
        }

        setReadOnly(true);

        setTitle("VIEW IZIN PENCAIRAN");

        createMode = false;
        editMode = false;
        viewMode = true;
        approvedMode = false;
    }

    public void recalculateIzinCair() throws Exception {

        BigDecimal totalInv = new BigDecimal(0);
        BigDecimal totalReal = new BigDecimal(0);
        DTOList pencairandet = izinpencairan.getPencairandet();
        for (int i = 0; i < pencairandet.size(); i++) {
            ARInvestmentIzinPencairanDetView adr = (ARInvestmentIzinPencairanDetView) pencairandet.get(i);

            totalInv = BDUtil.add(totalInv, adr.getDbBilyetAmount());

            DTOList realisasidet = adr.getListRealisasi();
            for (int j = 0; j < realisasidet.size(); j++) {
                ARInvestmentIzinPencairanDetView adrj = (ARInvestmentIzinPencairanDetView) realisasidet.get(j);

                if (adrj.getStJenisCair().equalsIgnoreCase("1")) {

                    if (adrj.getStDLANo() == null) {
                        throw new Exception("Data Klaim Belum Terinput");
                    }

                    //UNTUK PENGECEKAN LKP
//                    validateKlaimIzinAlready(adrj.getStARInvoiceID()); belum fix

                    if (createMode) {
                        DTOList proposal = validateKlaimDetAlready();
                        for (int k = 0; k < proposal.size(); k++) {
                            ARInvestmentIzinPencairanDetView prop = (ARInvestmentIzinPencairanDetView) proposal.get(k);

                            //UNTUK PENGECEKAN LKP
                            if (adrj.getStARInvoiceID().equalsIgnoreCase(prop.getStARInvoiceID())) {
                                throw new RuntimeException(prop.getStPencairanKet() + " sudah terinput di No. Pengajuan " + prop.getStBuktiB());
                            }
                        }
                    }
                }

                if (adrj.getStJenisCair().equalsIgnoreCase("10")) {

                    if (adrj.getStDLANo() == null) {
                        throw new Exception("Data Restitusi Belum Terinput");
                    }

                    if (createMode) {
                        DTOList proposal = validateRestitusiAlready();
                        for (int k = 0; k < proposal.size(); k++) {
                            ARInvestmentIzinPencairanDetView prop = (ARInvestmentIzinPencairanDetView) proposal.get(k);

                            //UNTUK PENGECEKAN LKP
                            if (adrj.getStARInvoiceID().equalsIgnoreCase(prop.getStARInvoiceID())) {
                                throw new RuntimeException(prop.getStPencairanKet() + " sudah terinput di No. Pengajuan " + prop.getStBuktiB());
                            }
                        }
                    }
                }

                totalReal = BDUtil.add(totalReal, adrj.getDbNilai());
            }

//            if (Tools.compare(totalInv, totalReal) < 0) {
//                throw new RuntimeException("Realisasi melebihi Deposito (" + totalReal + " < " + totalInv + "). \n\n Bilyet " + adr.getStNodefo() + " " + adr.getStBuktiB());
//            }
        }
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ " + totalReal);
        izinpencairan.setDbTotalRealisasi(totalReal);
        izinpencairan.setDbTotalBilyet(totalInv);

    }

    public void saveIzinCair() throws Exception {

        recalculateIzinCair();

        getRemoteGeneralLedger().saveIzinCair(izinpencairan, approvalMode);

        close();
    }

    public void approvalCabCair(String ardepoid) throws Exception {

        izinpencairan = getRemoteGeneralLedger().loadIzinPencairan(ardepoid);

        if (izinpencairan.isEffectiveCab()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;
        approvalPus = false;

        createMode = false;
        editMode = false;
        viewMode = false;
        approvedMode = true;

    }

    public void approvalPusCair(String ardepoid) throws Exception {

        izinpencairan = getRemoteGeneralLedger().loadIzinPencairan(ardepoid);

        if (!izinpencairan.isEffectiveCab()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (izinpencairan.isEffectivePus()) {
            throw new Exception("Data Sudah disetujui Pusat");
        }

        super.setReadOnly(true);

        approvalCab = false;
        approvalPus = true;

        createMode = false;
        editMode = false;
        viewMode = false;
        approvedMode = true;

    }

    public void approveIzinCair() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        if (isApprovalCab()) {

            try {
                PreparedStatement PS = S.setQuery("update ar_izin_pencairan set approvedcab_flag = 'Y', approvedcab_who = ?, approvedcab_date = ? where ar_izincair_id = ?");

                PS.setObject(1, SessionManager.getInstance().getUserID());
                PS.setDate(2, new java.sql.Date(new Date().getTime()));
                PS.setObject(3, izinpencairan.getStARIzinCairID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
        }

        if (isApprovalPus()) {

            try {
                PreparedStatement PS = S.setQuery("update ar_izin_pencairan set approvedpus_flag = 'Y', approvedpus_who = ?, approvedpus_date = ? where ar_izincair_id = ?");

                PS.setObject(1, SessionManager.getInstance().getUserID());
                PS.setDate(2, new java.sql.Date(new Date().getTime()));
                PS.setObject(3, izinpencairan.getStARIzinCairID());

                int p = PS.executeUpdate();

                DTOList pencairandet = izinpencairan.getPencairandet();
                for (int i = 0; i < pencairandet.size(); i++) {
                    ARInvestmentIzinPencairanDetView adr = (ARInvestmentIzinPencairanDetView) pencairandet.get(i);

                    final DTOList pencairanDet = adr.getListRealisasi();
                    for (int j = 0; j < pencairanDet.size(); j++) {
                        ARInvestmentIzinPencairanDetView det = (ARInvestmentIzinPencairanDetView) pencairanDet.get(j);

                        if (det.getStJenisCair().equalsIgnoreCase("1")
                                || det.getStJenisCair().equalsIgnoreCase("10")) {
//                            PreparedStatement PS2 = S.setQuery("update ar_invoice set no_surat_hutang = ?, approved_flag = 'Y' where ar_invoice_id = ? and coalesce(cancel_flag,'') <> 'Y' and ar_trx_type_id = 12 ");
                            PreparedStatement PS2 = S.setQuery("update ar_invoice set no_izin_pencairan = ?, approved_flag = 'Y' where ar_invoice_id = ? and coalesce(cancel_flag,'') <> 'Y' ");

                            PS2.setObject(1, izinpencairan.getStNoSurat());
                            PS2.setObject(2, det.getStARInvoiceID());

                            int p2 = PS2.executeUpdate();
                        }

                        if (det.getStJenisCair().equalsIgnoreCase("9")) {
                            PreparedStatement PS2 = S.setQuery("update ar_invoice set no_izin_pencairan = ? where no_surat_hutang = ? and coalesce(cancel_flag,'') <> 'Y' and ar_trx_type_id = 11 ");

                            PS2.setObject(1, izinpencairan.getStNoSurat());
                            PS2.setObject(2, det.getStDLANo());

                            int p2 = PS2.executeUpdate();
                        }
                    }
                }

                getRemoteGeneralLedger().saveIzinCair(izinpencairan, approvalPus);
            } finally {
                S.release();
            }
        }

        close();
    }

    /**
     * @return the izinpencairan
     */
    public ARInvestmentIzinPencairanView getIzinpencairan() {
        return izinpencairan;
    }

    /**
     * @param izinpencairan the izinpencairan to set
     */
    public void setIzinpencairan(ARInvestmentIzinPencairanView izinpencairan) {
        this.izinpencairan = izinpencairan;
    }
    private String notesindex;

    public String getNotesindex() {
        return notesindex;
    }

    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public DTOList getPencairandet() throws Exception {
        return izinpencairan.getPencairandet();
    }

    public void onNewCairDet() throws Exception {
        final ARInvestmentIzinPencairanDetView bng = new ARInvestmentIzinPencairanDetView();

        bng.markNew();

        getPencairandet().add(bng);

        String n = String.valueOf(getPencairandet().size());
    }

    public void onDeleteCairDet() throws Exception {
        final ARInvestmentIzinPencairanDetView currentLine = (ARInvestmentIzinPencairanDetView) getPencairandet().get(Integer.parseInt(notesindex));

        getPencairandet().delete(Integer.parseInt(notesindex));

        currentLine.getDetails().deleteAll();
        currentLine.getListRealisasi().deleteAll();
    }

    public void onDeleteInvoiceTitipanItem() throws Exception {
        final ARInvestmentIzinPencairanDetView currentLine = (ARInvestmentIzinPencairanDetView) getPencairandet().get(Integer.parseInt(notesindex));

        currentLine.getListRealisasi().delete(Integer.parseInt(notesrealisasiindex));
    }

    public void setDateIzinCair() throws Exception {

        String date = "01/" + izinpencairan.getStMonths() + "/" + izinpencairan.getStYears();

        izinpencairan.setDtMutationDate(DateUtil.getDate(date));
    }

    /**
     * @return the pencairandetail
     */
    public ARInvestmentIzinPencairanDetView getPencairandetail() {
        return pencairandetail;
    }

    /**
     * @param pencairandetail the pencairandetail to set
     */
    public void setPencairandetail(ARInvestmentIzinPencairanDetView pencairandetail) {
        this.pencairandetail = pencairandetail;
    }

    /**
     * @return the depositoindex
     */
    public ARInvestmentDepositoIndexView getDepositoindex() {
        return depositoindex;
    }

    /**
     * @param depositoindex the depositoindex to set
     */
    public void setDepositoindex(ARInvestmentDepositoIndexView depositoindex) {
        this.depositoindex = depositoindex;
    }

    public void createNewIzinBunga() {
        izinbunga = new ARInvestmentIzinBungaView();

        izinbunga.markNew();

        setTitle("CREATE IZIN BUNGA");

        editMode = true;
        viewMode = false;
        approvalMode = false;
    }

    public void editIzinBunga(String ardepoid) throws Exception {
        viewIzinBunga(ardepoid);

        if (izinbunga.isEffectiveCab()) {
            throw new Exception("Data Sudah Disetujui Cabang");
        }
        if (izinbunga.isEffectivePus()) {
            throw new Exception("Data Sudah Disetujui Pusat");
        }

        setReadOnly(false);

        izinbunga.markUpdate();
        izinbunga.getBungadet().markAllUpdate();

        setTitle("EDIT IZIN BUNGA");

        editMode = true;
        viewMode = false;
        approvalMode = false;
    }

    public void viewIzinBunga(String ardepoid) throws Exception {

        izinbunga = getRemoteGeneralLedger().loadIzinBunga(ardepoid);

        if (izinbunga == null) {
            throw new RuntimeException("Deposito not found !");
        }

        setReadOnly(true);

        setTitle("VIEW IZIN PENCAIRAN");

        editMode = false;
        viewMode = true;
        approvalMode = false;
    }

    public void approvalCabBunga(String ardepoid) throws Exception {

        izinbunga = getRemoteGeneralLedger().loadIzinBunga(ardepoid);

        if (izinbunga.isEffectiveCab()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;
        approvalPus = false;

        editMode = false;
        viewMode = false;
        approvalMode = true;

    }

    public void approvalPusBunga(String ardepoid) throws Exception {

        izinbunga = getRemoteGeneralLedger().loadIzinBunga(ardepoid);

        if (!izinbunga.isEffectiveCab()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (izinbunga.isEffectivePus()) {
            throw new Exception("Data Sudah disetujui Pusat");
        }

        super.setReadOnly(true);

        approvalCab = false;
        approvalPus = true;

        editMode = false;
        viewMode = false;
        approvalMode = true;

    }

    public void approvalBunga(String ardepoid) throws Exception {

        izinbunga = getRemoteGeneralLedger().loadIzinBunga(ardepoid);

        if (izinbunga.isEffectivePus()) {
            throw new Exception("Data Belum disetujui");
        }

        super.setReadOnly(true);

        editMode = false;
        viewMode = false;
        approvalMode = true;

    }

    public void validateEntryBunga() throws Exception {

        final DTOList pegawai = izinbunga.getBungadet();
        for (int j = 0; j < pegawai.size(); j++) {
            ARInvestmentBungaView doc = (ARInvestmentBungaView) pegawai.get(j);

            if (doc.getDbAngka() == null) {
                throw new Exception("Bilyet " + doc.getStNodefo() + ", Nilai Bunga masih kosong!!");
            }

            if (Tools.isEqual(doc.getDbAngka(), BDUtil.zero)) {
                throw new Exception("Bilyet " + doc.getStNodefo() + ", Nilai Bunga tidak boleh 0.00!!");
            }

            if (SessionManager.getInstance().getSession().getStBranch() != null) {
                if (doc.getStKodedepo().equalsIgnoreCase("2")) {
                    if (doc.getDbPersen() == null) {
                        throw new Exception("Bilyet " + doc.getStNodefo() + ", Bunga Bulan Depan masih kosong!!");
                    }
                    if (Tools.isEqual(doc.getDbPersen(), BDUtil.zero)) {
                        throw new Exception("Bilyet " + doc.getStNodefo() + ", Bunga Bulan Depan tidak boleh 0.00!!");
                    }
                }
            }
        }
    }

    public void saveIzinBunga() throws Exception {

        getRemoteGeneralLedger().saveIzinBunga(izinbunga, approvalMode);

        close();
    }

    public void approveIzinBunga() throws Exception {
        validateEntryBunga();

        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        final DTOList bungadet = izinbunga.getBungadet();
        for (int i = 0; i < bungadet.size(); i++) {
            ARInvestmentBungaView dep = (ARInvestmentBungaView) bungadet.get(i);

            if (Tools.compare(dep.getDbAngka(), BDUtil.zero) <= 0) {
                throw new RuntimeException("Nominal Bunga " + dep.getStNodefo() + " belum diinput ");
            }
        }

        try {
            PreparedStatement PS = S.setQuery("update ar_izin_bunga set approvedpus_flag = 'Y', approvedpus_who = ?, approvedpus_date = ? where ar_izinbng_id = ?");

            PS.setObject(1, SessionManager.getInstance().getUserID());
            PS.setDate(2, new java.sql.Date(new Date().getTime()));
            PS.setObject(3, izinbunga.getStARIzinBngID());

            int p = PS.executeUpdate();

            getRemoteGeneralLedger().saveIzinBunga(izinbunga, approvalMode);
        } finally {
            S.release();
        }

        close();
    }

    /**
     * @return the izinbunga
     */
    public ARInvestmentIzinBungaView getIzinbunga() {
        return izinbunga;
    }

    /**
     * @param izinbunga the izinbunga to set
     */
    public void setIzinbunga(ARInvestmentIzinBungaView izinbunga) {
        this.izinbunga = izinbunga;
    }

    public DTOList getBungadet() throws Exception {
        return izinbunga.getBungadet();
    }

    public void retrieveBunga() throws Exception {

        int tahunCodeLast;
        int bulanCode = Integer.parseInt(izinbunga.getStMonths());

        String policyDateStart = null;
        String policyDateEnd = null;
        Date dateStart = null;
        Date dateEnd = null;
        String bulan = null;
        String tahun = null;

        if (bulanCode == 1) {
            tahunCodeLast = Integer.parseInt(izinbunga.getStYears()) - 1;

            bulan = "12";
            tahun = Integer.toString(tahunCodeLast);

            policyDateStart = tahunCodeLast + "-12-01 00:00:00";
            policyDateEnd = tahunCodeLast + "-12-31 00:00:00";

            dateStart = DateUtil.getDate("01/12/" + tahunCodeLast);
            dateEnd = DateUtil.getDate("31/12/" + tahunCodeLast);
        } else if (bulanCode > 1) {
            int bulanCodeNow = Integer.parseInt(izinbunga.getStMonths()) - 1;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(izinbunga.getDtMutationDate()));
            dateStart = pd.getDtStartDate();
            dateEnd = pd.getDtEndDate();

            bulan = DateUtil.getMonth2Digit(dateStart);
            tahun = DateUtil.getYear(dateStart);

            policyDateStart = dateStart.toString();
            policyDateEnd = dateEnd.toString();
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(" from ar_inv_perpanjangan a ");

//        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.deleted is null");

        sqa.addClause(" date_trunc('day',a.tgldepo) <= '" + policyDateEnd + "'");
        sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > '" + policyDateEnd + "')");

        if (izinbunga.getStCostCenterCode() != null) {
            sqa.addClause(" a.koda = ? ");
            sqa.addPar(izinbunga.getStCostCenterCode());
        }

//        if (izinbunga.getStYears() != null) {
//            sqa.addClause("a.years = ?");
//            sqa.addPar(tahun);
//        }
//
//        if (izinbunga.getStMonths() != null) {
//            sqa.addClause("a.months = ?");
//            sqa.addPar(bulan);
//        }
//
////        if (izinbunga.getStReceiptClassID() != null) {
////            sqa.addClause("a.rc_id = ?");
////            sqa.addPar(izinbunga.getStReceiptClassID());
////        }
//
////        if (izinbunga.getStCompanyType() != null) {
////            sqa.addClause("a.comp_type = ?");
////            sqa.addPar(izinbunga.getStCompanyType());
////        }

        String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentPerpanjanganView.class);

        final DTOList details = new DTOList();
        izinbunga.setBungadet(details);

        for (int i = 0; i < l.size(); i++) {
//            HashDTO h = (HashDTO) l.get(i);
            ARInvestmentPerpanjanganView h = (ARInvestmentPerpanjanganView) l.get(i);

            ARInvestmentBungaView bungadet = new ARInvestmentBungaView();
            bungadet.markNew();

            int days = DateUtil.getDaysAmount(h.getDtTglawal(), h.getDtTglakhir());

            bungadet.setStARDepoID(h.getStARDepoID());
            if (h.getStBuktiB() != null) {
                bungadet.setStNoBuktiB(h.getStBuktiB());
            }
            bungadet.setStNoRekeningDeposito(h.getStNoRekeningDeposito());
            if (bungadet.findSpecimenDir(izinbunga.getStCostCenterCode()) != null) {
                bungadet.setStEntityID(bungadet.findSpecimenDir(izinbunga.getStCostCenterCode()));
            } else {
                bungadet.setStEntityID(h.getStEntityID());
            }
            bungadet.setStNodefo(h.getStNodefo());
            bungadet.setDbNominal(h.getDbNominal());
            bungadet.setDtTglBunga(izinbunga.getDtMutationDate());
            bungadet.setDbAngka(BDUtil.zero);
            bungadet.setStAccountBank(bungadet.getAccounts().getStAccountNo());
            bungadet.setStBankName(bungadet.getAccounts().getStDescription());
            bungadet.setStAccountDepo(bungadet.getAccountsInvest().getStAccountNo());
            bungadet.setStDepoName(bungadet.getAccountsInvest().getStDescription());
            bungadet.setDbAngka1(bungadet.getDbAngka1());
            bungadet.setDbPersen(bungadet.getDbPersen());
//            bungadet.setStHari(String.valueOf(days));

            details.add(bungadet);
        }

        izinbunga.setBungadet(details);
    }

    public void setDateIzinBunga() throws Exception {

        String date = "01/" + izinbunga.getStMonths() + "/" + izinbunga.getStYears();

        izinbunga.setDtMutationDate(DateUtil.getDate(date));
    }

    public void addRealisasi() throws Exception {
        final ARInvestmentIzinPencairanDetView rcl = new ARInvestmentIzinPencairanDetView();
        rcl.markNew();

        ARInvestmentIzinPencairanDetView rclTitip = (ARInvestmentIzinPencairanDetView) getPencairandet().get(Integer.parseInt(notesindex));

        rcl.setStARIzinCairID(rclTitip.getStARIzinCairID());
        rcl.setStLineType("REALISASI");
        rcl.setStJenisCair("2");

        rclTitip.getListRealisasi().add(rcl);
    }
    private String notesrealisasiindex;

    /**
     * @return the notesrealisasiindex
     */
    public String getNotesrealisasiindex() {
        return notesrealisasiindex;
    }

    /**
     * @param notesrealisasiindex the notesrealisasiindex to set
     */
    public void setNotesrealisasiindex(String notesrealisasiindex) {
        this.notesrealisasiindex = notesrealisasiindex;
    }

    public void onNewBungaDet() throws Exception {
        final ARInvestmentBungaView bng = new ARInvestmentBungaView();

        bng.markNew();

        getBungadet().add(bng);

        String n = String.valueOf(getBungadet().size());
    }

    public void onDeleteBungaDet() throws Exception {
        getBungadet().delete(Integer.parseInt(notesindex));

        String n = String.valueOf(getBungadet().size());
    }

    public void onDeleteBungaAll() throws Exception {
        getBungadet().deleteAll();
    }

    /**
     * @return the depodetail
     */
    public ARInvestmentIzinDepositoDetailView getDepodetail() {
        return depodetail;
    }

    /**
     * @param depodetail the depodetail to set
     */
    public void setDepodetail(ARInvestmentIzinDepositoDetailView depodetail) {
        this.depodetail = depodetail;
    }

    public void uploadExcel() throws Exception {

        String fileID = getIzinpencairan().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetDepo = wb.getSheet("DEPOSITO");
        HSSFSheet sheetKlaim = wb.getSheet("KLAIM");

        int rows = sheetDepo.getPhysicalNumberOfRows();
        int rows2 = sheetKlaim.getPhysicalNumberOfRows();

        String arDepoID = null;
        String alasan = "";
        boolean cekGagal = false;

        boolean adaPolis = false;
        for (int r = 5; r <= rows; r++) {
            HSSFRow row = sheetDepo.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            //if(cellControl==null) break;

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorPolis = row.getCell(3);//nomor polis

            if (cellNomorPolis.getStringCellValue() != null) {
                adaPolis = true;
            }
        }

        if (adaPolis) {
            for (int r = 5; r <= rows; r++) {
                HSSFRow row = sheetDepo.getRow(r);

                HSSFCell cellControl = row.getCell(1);
                //if(cellControl==null) break;

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellBilyet = row.getCell(3);//nomor polis

                DTOList bilyetList = getDepositoNo(cellBilyet.getStringCellValue());
                for (int k = 0; k < bilyetList.size(); k++) {
                    ARInvestmentDepositoView inv = (ARInvestmentDepositoView) bilyetList.get(k);

                    if (inv == null) {
                        alasan = alasan + "<br>Bilyet " + cellBilyet.getStringCellValue() + " tidak ditemukan.";
                        cekGagal = true;
                        continue;
                    }

                    arDepoID = inv.getStARDepoID();

                    onNewPencairan(inv.getStARDepoID(), inv);

                }

                String alasanTitipan = "";
                boolean cekGagalTitipan = false;
                for (int q = 5; q <= rows2; q++) {
                    HSSFRow row2 = sheetKlaim.getRow(q);

                    HSSFCell cellControl2 = row2.getCell(1);
                    if (cellControl2 == null) {
                        break;
                    }

                    if (cellControl2.getStringCellValue().equalsIgnoreCase("END")) {
                        break;
                    }

                    HSSFCell cellBilyet2 = row2.getCell(3);//nodefo
                    HSSFCell cellDLA = row2.getCell(4);//nomor lkp

                    //cek jika bukan titipan punya nomor polis tsb, skip
                    if (!cellBilyet.getStringCellValue().equalsIgnoreCase(cellBilyet2.getStringCellValue())) {
                        continue;
                    }

                    InsurancePolicyView policy = getClaimPolicy(cellDLA.getStringCellValue());

                    if (policy == null) {
                        alasanTitipan = alasanTitipan + "<br>LKP " + cellDLA.getStringCellValue() + " tidak ditemukan.";
                        cekGagalTitipan = true;
                        continue;
                    }

                    final ARInvestmentIzinPencairanDetView rcl = new ARInvestmentIzinPencairanDetView();
                    rcl.markNew();

                    ARInvestmentIzinPencairanDetView rclTitip = (ARInvestmentIzinPencairanDetView) getPencairandet().get(Integer.parseInt(notesindex));
                    logger.logDebug("#########################1 " + rclTitip);

                    rcl.setStARIzinCairID(rclTitip.getStARIzinCairID());
                    rcl.setStLineType("REALISASI");
                    rcl.setStJenisCair("1");
                    rcl.setStPencairanKet(policy.getStDLANo());
                    rcl.setStARInvoiceID(policy.getStPolicyID());
                    rcl.setStDLANo(policy.getStPolicyNo());
                    rcl.setDbNilai(policy.getDbClaimAmount());
                    rcl.setStARDepoID(arDepoID);

                    rclTitip.getListRealisasi().add(rcl);
                }

                if (cekGagal) {
                    throw new RuntimeException("Deposito gagal konversi : " + alasan);
                }

                if (cekGagalTitipan) {
                    throw new RuntimeException("Klaim gagal konversi : " + alasanTitipan);
                }
            }

        } else if (!adaPolis) {

            String alasanTitipan = "";
            boolean cekGagalTitipan = false;
            for (int q = 5; q <= rows2; q++) {
                HSSFRow row2 = sheetKlaim.getRow(q);

                HSSFCell cellControl2 = row2.getCell(1);
                if (cellControl2 == null) {
                    break;
                }

                if (cellControl2.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellBilyet2 = row2.getCell(3);//nodefo
                HSSFCell cellDLA = row2.getCell(4);//nomor lkp

                InsurancePolicyView policy = getClaimPolicy(cellDLA.getStringCellValue());

                if (policy == null) {
                    alasanTitipan = alasanTitipan + "<br>LKP " + cellDLA.getStringCellValue() + " tidak ditemukan.";
                    cekGagalTitipan = true;
                    continue;
                }

                final ARInvestmentIzinPencairanDetView rcl = new ARInvestmentIzinPencairanDetView();
                rcl.markNew();

                int lastRLC = getPencairandet().size() - 1;

                ARInvestmentIzinPencairanDetView rclTitip = (ARInvestmentIzinPencairanDetView) getPencairandet().get(lastRLC);
                logger.logDebug("#########################1 " + rclTitip.getStARInvoiceID());

                rcl.setStARIzinCairID(rclTitip.getStARIzinCairID());
                rcl.setStLineType("REALISASI");
                rcl.setStJenisCair("1");
                rcl.setStPencairanKet(policy.getStDLANo());
                rcl.setStARInvoiceID(policy.getStPolicyID());
                rcl.setStDLANo(policy.getStPolicyNo());
                rcl.setDbNilai(policy.getDbClaimAmount());
                rcl.setStARDepoID(arDepoID);

                rclTitip.getListRealisasi().add(rcl);
            }
        }

        recalculateIzinCair();

        if (cekGagal) {
            throw new RuntimeException("Klaim gagal konversi : " + alasan);
        }
    }

    public DTOList getDepositoNo(String attrpolid) throws Exception {

        String sql = "select * from ar_inv_deposito a where nodefo = ? "
                + "and a.active_flag = 'Y' and a.effective_flag = 'Y' and a.deleted is null ";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolid},
                ARInvestmentDepositoView.class);
    }

    private InsurancePolicyView getClaimPolicy(String attrpolid) throws Exception {
        final InsurancePolicyView titipan = (InsurancePolicyView) ListUtil.getDTOListFromQuery(
                "select * from ins_policy a where dla_no = ? and a.active_flag = 'Y' and a.effective_flag = 'Y'",
                new Object[]{attrpolid},
                InsurancePolicyView.class).getDTO();

        return titipan;
    }

    public void validateNodefoAlreadyIn(String arInvoiceID) throws Exception {
        final DTOList detail = izinpencairan.getPencairandet();

        for (int i = 0; i < detail.size(); i++) {
            ARInvestmentIzinPencairanDetView rl = (ARInvestmentIzinPencairanDetView) detail.get(i);

            if (rl.getDepo() != null) {
                if (rl.getDepo().getStARDepoID().equalsIgnoreCase(arInvoiceID)) {
                    throw new RuntimeException("Bilyet " + rl.getDepo().getStNodefo() + " Sudah Dipilih sebelumnya");
                }
            }
        }
    }

    public void validateClaimAlreadyIn(String arInvoiceID) throws Exception {
        final DTOList detail = izinpencairan.getPencairandet();

        for (int i = 0; i < detail.size(); i++) {
            ARInvestmentIzinPencairanDetView rl = (ARInvestmentIzinPencairanDetView) detail.get(i);

            DTOList realisasidet = rl.getListRealisasi();
            for (int j = 0; j < realisasidet.size(); j++) {
                ARInvestmentIzinPencairanDetView adrj = (ARInvestmentIzinPencairanDetView) realisasidet.get(j);

                if (rl.getInvoice() != null) {
                    if (rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID)) {
                        throw new RuntimeException("LKP " + rl.getInvoice().getStRefID2() + " Sudah Dipilih sebelumnya");
                    }
                }
            }
        }
    }

    public void onNewPencairan(String invoice_id, ARInvestmentDepositoView invoiceParam) throws Exception {

        validateNodefoAlreadyIn(invoice_id);

        final ARInvestmentDepositoView depo = invoiceParam;

        ARInvestmentIzinPencairanDetView reins = new ARInvestmentIzinPencairanDetView();
        reins.markNew();

        reins.setStLineType("INVOICE");
        reins.setStARDepoID(depo.getStARDepoID());
        reins.setStBuktiB(depo.getStBuktiB());
        reins.setStNodefo(depo.getStNodefo());
        reins.setDbBilyetAmount(depo.getDbNominal());

        izinpencairan.getPencairandet().add(reins);

        notesindex = String.valueOf(izinpencairan.getPencairandet().size() - 1);
    }

    public void reverseIzinCair(String ardepoid) throws Exception {
        viewIzinCair(ardepoid);

        if (!izinpencairan.isEffectivePus()) {
            throw new Exception("Data Belum Disetujui Pusat");
        }

        setReadOnly(true);

        izinpencairan.markUpdate();
        izinpencairan.getPencairandet().markAllUpdate();

        setTitle("REVERSE IZIN PENCAIRAN");

        editMode = false;
        viewMode = false;
        approvedMode = false;
        reverseMode = true;
    }

    public void doReverseIzinCair() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        getRemoteGeneralLedger().reverseIzinCair(izinpencairan);

        close();
    }

    public void editInputNobukCair(String ardepoid) throws Exception {
        viewIzinCair(ardepoid);

        if (!izinpencairan.isEffectiveCab()) {
            throw new Exception("Data Belum Disetujui Cabang");
        }

        if (!izinpencairan.isEffectivePus()) {
            throw new Exception("Data Belum Disetujui Pusat");
        }

        setReadOnly(false);

        izinpencairan.markUpdate();
        izinpencairan.getPencairandet().markAllUpdate();

        setTitle("INPUT BUKTI");

        inputBuktiMode = true;
    }

    /**
     * @return the inputBuktiMode
     */
    public boolean isInputBuktiMode() {
        return inputBuktiMode;
    }

    /**
     * @param inputBuktiMode the inputBuktiMode to set
     */
    public void setInputBuktiMode(boolean inputBuktiMode) {
        this.inputBuktiMode = inputBuktiMode;
    }

    public void doSaveNobukIzinCair() throws Exception {

        getRemoteGeneralLedger().saveIzinCair(izinpencairan, approvalMode);

        close();
    }

    public void downloadBunga() throws Exception {

        final DTOList l = EXCEL_ENTRY_BUNGA();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_ENTRY_BUNGA();
    }

    public DTOList EXCEL_ENTRY_BUNGA() throws Exception {

        int tahunCodeLast;
        int bulanCode = Integer.parseInt(izinbunga.getStMonths());

        String policyDateStart = null;
        String policyDateEnd = null;
        Date dateStart = null;
        Date dateEnd = null;
        String bulan = null;
        String tahun = null;

        if (bulanCode == 1) {
            tahunCodeLast = Integer.parseInt(izinbunga.getStYears()) - 1;

            bulan = "12";
            tahun = Integer.toString(tahunCodeLast);

            policyDateStart = tahunCodeLast + "-12-01 00:00:00";
            policyDateEnd = tahunCodeLast + "-12-31 00:00:00";

            dateStart = DateUtil.getDate("01/12/" + tahunCodeLast);
            dateEnd = DateUtil.getDate("31/12/" + tahunCodeLast);
        } else if (bulanCode > 1) {
            int bulanCodeNow = Integer.parseInt(izinbunga.getStMonths()) - 1;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(izinbunga.getDtMutationDate()));
            dateStart = pd.getDtStartDate();
            dateEnd = pd.getDtEndDate();

            bulan = DateUtil.getMonth2Digit(dateStart);
            tahun = DateUtil.getYear(dateStart);

            policyDateStart = dateStart.toString();
            policyDateEnd = dateEnd.toString();
        }

        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.ar_depo_id,a.bukti_b,a.norekdep,a.account_depo,a.nodefo,a.nominal,"
//                + "a.kdbank,a.account_bank,a.nama_bank,b.hari,b.persen,b.angka1 ");
        sqa.addSelect(" a.ar_depo_id,a.bukti_b,a.norekdep,a.account_depo,a.nodefo,a.nominal,"
                + "a.kdbank,a.account_bank,a.nama_bank,b.angka1,b.ar_bunga_id,b.hari,b.persen,b.tglawal,b.tglakhir,"
                + "row_number() over (partition by a.ar_depo_id order by a.ar_depo_id,b.ar_bunga_id desc) as rn ");

//        sqa.addQuery(" from ar_inv_perpanjangan a "
//                + "left join ar_inv_bunga b on b.nodefo = a.nodefo and b.years = '" + tahun + "' and b.months = '" + bulan + "' and b.delete_flag is null ");

        sqa.addQuery(" from ar_inv_perpanjangan a "
                + "left join ar_inv_bunga b on b.ar_depo_id = a.ar_depo_id and b.delete_flag is null ");

        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.deleted is null");

        sqa.addClause(" date_trunc('day',a.tgldepo) <= '" + policyDateEnd + "'");
        sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > '" + policyDateEnd + "')");

        if (izinbunga.getStCostCenterCode() != null) {
            sqa.addClause(" a.koda = ? ");
            sqa.addPar(izinbunga.getStCostCenterCode());
        }

        String sql = "select a.ar_depo_id,a.ar_bunga_id,a.bukti_b,a.norekdep,a.account_depo,a.nodefo,"
                + "a.nominal,a.kdbank,a.account_bank,a.nama_bank,a.angka1,a.hari,a.persen,a.tglawal,a.tglakhir  "
                + "from ( " + sqa.getSQL() + " order by 1,2 ) a where rn = 1 order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        return l;
    }

    public void EXPORT_ENTRY_BUNGA() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("budep");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal sisa = null;
        String nobuktitipan = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            int days = DateUtil.getDaysAmount(h.getFieldValueByFieldNameDT("tglawal"), h.getFieldValueByFieldNameDT("tglakhir"));

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("ENTRY BUNGA");

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Proses");
            row0.createCell(1).setCellValue("ID Bentuk");
            row0.createCell(2).setCellValue("Bukti B");
            row0.createCell(3).setCellValue("ID Depo");
            row0.createCell(4).setCellValue("Akun Depo");
            row0.createCell(5).setCellValue("Bilyet");
            row0.createCell(6).setCellValue("Nominal");
            row0.createCell(7).setCellValue("ID Bank");
            row0.createCell(8).setCellValue("Akun Bank");
            row0.createCell(9).setCellValue("Nama Bank");
            row0.createCell(10).setCellValue("Tanggal bunga");
            row0.createCell(11).setCellValue("Bunga Entry");
            row0.createCell(12).setCellValue("Bunga Hitung");
            row0.createCell(13).setCellValue("(%) Bunga Bulan Depan");
            row0.createCell(14).setCellValue("(%) Bulan Sebelum");
//            row0.createCell(15).setCellValue("Hari");

            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue("Y");
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("ar_depo_id").toString());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("norekdep").toString());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("kdbank").toString());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("account_bank"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(10).setCellValue(izinbunga.getDtMutationDate());
            row.createCell(11).setCellValue(BDUtil.zero.doubleValue());
            if (h.getFieldValueByFieldNameBD("angka1") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("angka1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("persen") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("persen").doubleValue());
            }
//            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("hari"));
//            row.createCell(14).setCellValue(String.valueOf(days));

            XSSFRow rowInward = sheet.createRow(list.size() + 3);
            rowInward.createCell(0).setCellValue("END");

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=entrybunga.xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void uploadExcelBunga() throws Exception {

        String fileID = getIzinbunga().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("budep");

        String arDepoID = null;
        String alasan = "";
        boolean cekGagal = false;

        final DTOList details = new DTOList();

        int rows = sheet.getPhysicalNumberOfRows();

        for (int r = 3; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellIDBentuk = row.getCell(1);//cabang
            HSSFCell cellBuktiB = row.getCell(2);//cabang
            HSSFCell cellIDDepo = row.getCell(3);//cabang
            HSSFCell cellAkunDepo = row.getCell(4);//cabang
            HSSFCell cellBilyet = row.getCell(5);//cabang
            HSSFCell cellNominal = row.getCell(6);//cabang
            HSSFCell cellIDBank = row.getCell(7);//cabang
            HSSFCell cellAkunBank = row.getCell(8);//cabang
            HSSFCell cellNamaBank = row.getCell(9);//cabang
            HSSFCell cellTglBunga = row.getCell(10);//cabang
            HSSFCell cellBungaEntry = row.getCell(11);//cabang
            HSSFCell cellBungaHitung = row.getCell(12);//cabang
            HSSFCell cellPersenBunga = row.getCell(13);//cabang
            HSSFCell cellHari = row.getCell(14);//cabang

//            logger.logDebug("################### cek1  " + cellIDBentuk.getStringCellValue());
//            logger.logDebug("################### cek3  " + cellIDDepo.getStringCellValue());
//            logger.logDebug("################### cek5  " + cellBilyet.getStringCellValue());
//            logger.logDebug("################### cek6  " + cellNominal.getNumericCellValue());
//            logger.logDebug("################### cek7  " + cellIDBank.getStringCellValue());
//            logger.logDebug("################### cek9  " + cellNamaBank.getStringCellValue());

            ARInvestmentBungaView bungadet = new ARInvestmentBungaView();
            bungadet.markNew();

            bungadet.setStARDepoID(cellIDBentuk.getCellType() == cellIDBentuk.CELL_TYPE_STRING ? cellIDBentuk.getStringCellValue() : new BigDecimal(cellIDBentuk.getNumericCellValue()).toString());
            if (cellBuktiB != null) {
                bungadet.setStNoBuktiB(cellBuktiB.getCellType() == cellBuktiB.CELL_TYPE_STRING ? cellBuktiB.getStringCellValue() : new BigDecimal(cellBuktiB.getNumericCellValue()).toString());
            }
            bungadet.setStNoRekeningDeposito(cellIDDepo.getCellType() == cellIDDepo.CELL_TYPE_STRING ? cellIDDepo.getStringCellValue() : new BigDecimal(cellIDDepo.getNumericCellValue()).toString());
            bungadet.setStAccountDepo(cellAkunDepo.getCellType() == cellAkunDepo.CELL_TYPE_STRING ? cellAkunDepo.getStringCellValue() : new BigDecimal(cellAkunDepo.getNumericCellValue()).toString());
            bungadet.setStNodefo(cellBilyet.getCellType() == cellBilyet.CELL_TYPE_STRING ? cellBilyet.getStringCellValue() : new BigDecimal(cellBilyet.getNumericCellValue()).toString());
            bungadet.setDbNominal(new BigDecimal(cellNominal.getNumericCellValue()));
            bungadet.setStEntityID(cellIDBank.getCellType() == cellIDBank.CELL_TYPE_STRING ? cellIDBank.getStringCellValue() : new BigDecimal(cellIDBank.getNumericCellValue()).toString());
            bungadet.setStAccountBank(cellAkunBank.getCellType() == cellAkunBank.CELL_TYPE_STRING ? cellAkunBank.getStringCellValue() : new BigDecimal(cellAkunBank.getNumericCellValue()).toString());
            bungadet.setStBankName(bungadet.getAccounts().getStDescription());
            bungadet.setDtTglBunga(cellTglBunga.getDateCellValue());
            bungadet.setDbAngka(new BigDecimal(cellBungaEntry.getNumericCellValue()));
            bungadet.setDbPersen(new BigDecimal(cellPersenBunga.getNumericCellValue()));
            if (cellBungaHitung != null) {
                bungadet.setDbAngka1(new BigDecimal(cellBungaHitung.getNumericCellValue()));
            }
//            bungadet.setStHari(cellHari.getCellType() == cellHari.CELL_TYPE_STRING ? cellHari.getStringCellValue() : new BigDecimal(cellHari.getNumericCellValue()).toString());

//            bungadet.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
//            bungadet.setStMonths(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());

            getBungadet().add(bungadet);

        }
    }

    public DTOList validateKlaimDetAlready() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select b.ar_invoice_id,a.no_surat as bukti_b,b.pencairan_ket "
                + "from ar_izin_pencairan a "
                + "inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id "
                + "where a.active_flag = 'Y' and b.jns_pencairan = '1' and a.cc_code = ? ",
                //                + "where b.jns_pencairan = '1' and a.ar_izincair_id <> ?  ",
                new Object[]{izinpencairan.getStCostCenterCode()},
                ARInvestmentIzinPencairanDetView.class);
    }

    public DTOList validateRestitusiAlready() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select b.ar_invoice_id,a.no_surat as bukti_b,b.pencairan_ket "
                + "from ar_izin_pencairan a "
                + "inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id "
                + "where a.active_flag = 'Y' and b.jns_pencairan = '10' and a.cc_code = ? ",
                //                + "where b.jns_pencairan = '1' and a.ar_izincair_id <> ?  ",
                new Object[]{izinpencairan.getStCostCenterCode()},
                ARInvestmentIzinPencairanDetView.class);
    }

    public void validateKlaimIzinAlready(String arinvocieid) throws Exception {

        DTOList pencairandet = izinpencairan.getPencairandet();
        for (int i = 0; i < pencairandet.size(); i++) {
            ARInvestmentIzinPencairanDetView adr = (ARInvestmentIzinPencairanDetView) pencairandet.get(i);

            DTOList realisasidet = adr.getListRealisasi();
            for (int j = 0; j < realisasidet.size(); j++) {
                ARInvestmentIzinPencairanDetView adrj = (ARInvestmentIzinPencairanDetView) realisasidet.get(j);

                if (adrj.getStARInvoiceID().equalsIgnoreCase(arinvocieid)) {
                    throw new RuntimeException(adrj.getStPencairanKet() + " Sudah Dipilih Sebelumnya (Double Input)");
                }
            }
        }
    }

    public void setRekening() {
        if (depodetail.isAskrida()) {
            depodetail.setStAtasNama("PT ASURANSI BANGUN ASKRIDA");
            depodetail.setStBankName(null);
            depodetail.setStEntityID(null);
        } else {
            depodetail.setStAtasNama(null);
            depodetail.setStBankName(null);
            depodetail.setStAccountBank(null);
        }
    }

    /**
     * @return the createMode
     */
    public boolean isCreateMode() {
        return createMode;
    }

    /**
     * @param createMode the createMode to set
     */
    public void setCreateMode(boolean createMode) {
        this.createMode = createMode;
    }

    public void reverseIzinBentuk(String ardepoid) throws Exception {
        viewIzin(ardepoid);

        if (!izindeposito.isEffectivePus()) {
            throw new Exception("Data Belum Disetujui Pusat");
        }

        setReadOnly(true);

        izindeposito.markUpdate();
        izindeposito.getDepodetail().markAllUpdate();

        setTitle("REVERSE IZIN PENGAJUAN");

        editMode = false;
        viewMode = false;
        approvedMode = false;
        reverseMode = true;
    }

    public void doReverseIzinBentuk() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        getRemoteGeneralLedger().reverseIzinBentuk(izindeposito);

        close();
    }

    public void reverseIzinBunga(String ardepoid) throws Exception {
        viewIzinBunga(ardepoid);

        if (!izinbunga.isEffectivePus()) {
            throw new Exception("Data Belum Disetujui Pusat");
        }

        setReadOnly(true);

        izinbunga.markUpdate();
        izinbunga.getBungadet().markAllUpdate();

        setTitle("REVERSE IZIN BUNGA");

        editMode = false;
        viewMode = false;
        approvedMode = false;
        reverseMode = true;
    }

    public void doReverseIzinBunga() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (izindeposito.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + izindeposito.getStMonths() + " dan tahun " + izindeposito.getStYears() + " tsb sudah diposting");
//        }

        getRemoteGeneralLedger().reverseIzinBunga(izinbunga);

        close();
    }
}

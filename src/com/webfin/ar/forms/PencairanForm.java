/***********************************************************************
 * Module:  com.webfin.ar.forms.PencairanForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.util.DateUtil;
import com.crux.util.ObjectCloner;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.BDUtil;
import com.crux.util.JNDIUtil;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;
import com.webfin.gl.ejb.CurrencyManager;

import com.webfin.insurance.ejb.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.math.BigDecimal;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import org.joda.time.DateTime;

public class PencairanForm extends Form {

    private ARInvestmentPencairanView pencairan;
    private ARInvestmentBungaView bunga;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private ARReceiptClassView receiptclass = null;
    private boolean approvalMode;
    private boolean reverseMode;
    private boolean journalMode;
//    private boolean receiptMode;
    private boolean Posted;
    private Date tglakhirTrx;
    private final static transient LogManager logger = LogManager.getInstance(PencairanForm.class);

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void createNew() {
        pencairan = new ARInvestmentPencairanView();

        pencairan.markNew();

        pencairan.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        pencairan.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        pencairan.setDbCurrencyRate(BDUtil.one);
        pencairan.setDbPajak(new BigDecimal(20));

        journalMode = true;
        doNewBunga();

        setTitle("CREATE PENCAIRAN");
    }

    public void edit(String arcairid) throws Exception {
        view(arcairid);
        /*
        DateTime tgltempo = new DateTime(pencairan.getDtTglakhir());
        Date tglexp2 = tgltempo.toDate();
        boolean compare = Tools.compare(new Date(), tglexp2) > 0;
         */

        if (!pencairan.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (pencairan.getStBuktiC() != null) {
            if (pencairan.getStJournalStatus().equalsIgnoreCase("NEW")) {
                if (pencairan.isEffective()) {
                    throw new Exception("Data Sudah Disetujui");
                }
            }
        }

        if (!pencairan.getDeposito().isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui");
        }

        //if (compare) throw new Exception("Jangka Waktu Deposito Sudah Berakhir, Harap Perpanjangan");

        setReadOnly(false);

        pencairan.markUpdate();

        final DTOList bunga = pencairan.getBunga();

        for (int i = 0; i < bunga.size(); i++) {
            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

            bng.markUpdate();
        }

        setTitle("EDIT PENCAIRAN");
    }

    public void view(String arcairid) throws Exception {

        //pencairan = (ARInvestmentPencairanView) DTOPool.getInstance().getDTO(ARInvestmentPencairanView.class, arcairid);

        pencairan = getRemoteGeneralLedger().loadPencairan(arcairid);

        setReadOnly(true);

        setTitle("VIEW PENCAIRAN");
    }

    public void save() throws Exception {

        if (!pencairan.isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
        }

        //if (isPinalty()&&pencairan.getDbPinalty()==null) throw new Exception("Pinalty Harus Diisi");

        final ARInvestmentPencairanView cloned = (ARInvestmentPencairanView) ObjectCloner.deepCopy(pencairan);

        onChgPeriod();

        getRemoteGeneralLedger().savePencairan(cloned);

        //getRemoteGeneralLedger().savePencairan(pencairan);

        close();
    }

    public void saveWithoutJurnal() throws Exception {

        if (!pencairan.isValidateCair()) {
            throw new RuntimeException("No. Bilyet tidak bisa dicairkan, hubungi bagian Investasi");
        }

        final ARInvestmentPencairanView cloned = (ARInvestmentPencairanView) ObjectCloner.deepCopy(pencairan);

        pencairan.checkNodefo();

        onChgPeriod();

        getRemoteGeneralLedger().saveWithoutJurnalCair(cloned);

        close();
    }

    public void approval(String arcairid) throws Exception {
        edit(arcairid);

        super.setReadOnly(true);

        approvalMode = true;

    }

    public void approve() throws Exception {

        if (!pencairan.getDeposito().isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui");
        }

        String cair_id = pencairan.getStARCairID();

        if (pencairan.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pencairan.getStMonths() + " dan tahun " + pencairan.getStYears() + " tsb sudah diposting");
        }

        final ARInvestmentPencairanView jh = new ARInvestmentPencairanView();

        final DTOList details = jh.getDetails(cair_id);

        jh.markUpdate();
        jh.setStEffectiveFlag("Y");
        jh.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        jh.setDtApprovedDate(new Date());

        getRemoteGeneralLedger().approve(pencairan, details);

        approvalMode = true;

        close();
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

    public ARInvestmentPencairanView getPencairan() {
        return pencairan;
    }

    public void setPencairan(ARInvestmentPencairanView pencairan) {
        this.pencairan = pencairan;
    }

    public void refresh() {
        //pencairan.setDbNominal(BDUtil.mul(pencairan.getDbCurrencyRate(), pencairan.getDbNominalKurs()));
    }

    public boolean isApprovalMode() {
        return approvalMode;
    }

    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public boolean isPinalty() {
        return Tools.compare(pencairan.getDtTglCair(), tglakhirTrx) < 0;
    }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    public void reverse() throws Exception {

        if (pencairan.getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new RuntimeException("Nobuk Tidak Bisa Di-Reverse Karena Tanpa Jurnal (HISTORY)");
        }

        if (!pencairan.isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (pencairan.isPosted()) {
            throw new RuntimeException("Nobuk " + pencairan.getStBuktiC() + " Masuk Mutasi Bulan " + pencairan.getStMonths() + " dan Tahun " + pencairan.getStYears() + " Yang Sudah Diposting");
        }

        getRemoteGeneralLedger().reversePencairan(pencairan);

        close();
    }

    public boolean isJournalMode() {
        return journalMode;
    }

    public void setJournalMode(boolean journalMode) {
        this.journalMode = journalMode;
    }

    public void onChgCurrency() throws Exception {
        pencairan.setDbPinalty(BDUtil.mul(pencairan.getDbCurrencyRate(), pencairan.getDbPinalty()));
    }

    public void doNewBunga() {
        final ARInvestmentBungaView bng = new ARInvestmentBungaView();

        bng.markNew();

        pencairan.getBunga().add(bng);

        bunga = bng;
    }

    public void setDate() throws Exception {

        String date = "01/" + pencairan.getStMonths() + "/" + pencairan.getStYears();
        String date2 = DateUtil.getDays(pencairan.getDtTglawal()) + "/" + pencairan.getStMonths() + "/" + pencairan.getStYears();

        DateTime endDate = new DateTime(DateUtil.getDate(date2));
        DateTime startDate = new DateTime();
        startDate = endDate.minusMonths(1);

        pencairan.setDtTglCair(DateUtil.getDate(date));
        pencairan.setDtTglawalTrx(startDate.toDate());
        pencairan.setDtTglakhirTrx(endDate.toDate());
    }

    public void selectMonth() {
    }

    public void setPinalty() {

        if (Tools.compare(pencairan.getDtTglCair(), pencairan.getDtTglakhirTrx()) < 0) {
            pencairan.setStType("1");
        } else {
            pencairan.setStType("2");
        }

    }

    public void onChgPeriod() throws Exception {
        if (!Tools.isEqual(DateUtil.getMonth2Digit(pencairan.getDtTglCair()), pencairan.getStMonths())) {
            throw new Exception("Bulan tidak sama dengan Tanggal Cair");
        }
        if (!Tools.isEqual(DateUtil.getYear(pencairan.getDtTglCair()), pencairan.getStYears())) {
            throw new Exception("Tahun tidak sama dengan Tanggal Cair");
        }
    }

    public Date getTglakhirTrx() {
        return tglakhirTrx;
    }

    public void setTglakhirTrx(Date tglakhirTrx) {
        this.tglakhirTrx = tglakhirTrx;
    }

    public void editIzin(String arcairid) throws Exception {
        view(arcairid);

        if (!pencairan.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (pencairan.getStBuktiC() != null) {
            if (pencairan.getStJournalStatus().equalsIgnoreCase("NEW")) {
                if (pencairan.isEffective()) {
                    throw new Exception("Data Sudah Disetujui");
                }
            }
        }

        if (!pencairan.getDeposito().isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui");
        }

        setReadOnly(false);

        pencairan.markUpdate();

        setTitle("EDIT PENCAIRAN");
    }

    public void realisasiIzin(String arcairid) throws Exception {
        view(arcairid);

        if (!pencairan.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (!pencairan.isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        setReadOnly(false);

        pencairan.markUpdate();

        setTitle("REALISASI PEMBAYARAN");
    }

    public void editBentukUlang(String arcairid) throws Exception {
        view(arcairid);

        if (!pencairan.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (pencairan.getStBuktiC() != null) {
            if (pencairan.getStJournalStatus().equalsIgnoreCase("NEW")) {
                if (!pencairan.isEffective()) {
                    throw new Exception("Data Belum Disetujui");
                }
            }
        }

        if (!pencairan.getDeposito().isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui");
        }

        setReadOnly(false);

        pencairan.setDtTgldepo(null);
        pencairan.setDtTglmuta(null);
        pencairan.setStYears(null);
        pencairan.setStMonths(null);
        pencairan.setStBuktiB(null);
        pencairan.markUpdate();

        setTitle("EDIT PENCAIRAN");
    }

    public void saveUlang() throws Exception {

        final ARInvestmentPencairanView cloned = (ARInvestmentPencairanView) ObjectCloner.deepCopy(pencairan);

        onChgPeriod();

        getRemoteGeneralLedger().saveUlang(cloned);

        //getRemoteGeneralLedger().savePencairan(pencairan);

        close();
    }
//    /**
//     * @return the receiptMode
//     */
//    public boolean isReceiptMode() {
//        return receiptMode;
//    }
//
//    /**
//     * @param receiptMode the receiptMode to set
//     */
//    public void setReceiptMode(boolean receiptMode) {
//        this.receiptMode = receiptMode;
//    }
}

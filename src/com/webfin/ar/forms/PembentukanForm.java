/***********************************************************************
 * Module:  com.webfin.ar.forms.PembentukanForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.controller.FormTab;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.webfin.FinCodec;
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
import java.math.BigInteger;
import java.sql.PreparedStatement;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

public class PembentukanForm extends Form {

    private ARInvestmentDepositoView deposito;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private ARInvestmentPencairanView pencairan;
    private ARInvestmentBungaView bunga;
    //private ARInvestmentPerpanjanganView perpanjangan;
    private boolean approvalMode;
    private boolean reverseMode;
    private boolean enableSuperEdit;
    private final static transient LogManager logger = LogManager.getInstance(PembentukanForm.class);
    private boolean bungaMode;
    private boolean cairMode;
    private boolean inputBuktiMode;
    private boolean inputPosisiMode;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void createNew() {
        deposito = new ARInvestmentDepositoView();

        deposito.markNew();

        deposito.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        deposito.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        deposito.setDbCurrencyRate(BDUtil.one);
        deposito.setDbPajak(new BigDecimal(20));

        doNewPencairan();

        doNewBunga();

        //doNewPerpanjangan();

        setTitle("CREATE DEPOSITO");
    }

    public void edit(String ardepoid) throws Exception {
        view(ardepoid);

        if (deposito.getStJournalStatus().equalsIgnoreCase("NEW")) {
            if (deposito.isEffective()) {
                throw new Exception("Data Sudah Disetujui");
            }
        }

        setReadOnly(false);

        deposito.markUpdate();

        final DTOList cair = deposito.getPencairan();

        for (int i = 0; i < cair.size(); i++) {
            ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

            pcr.markUpdate();
        }

        final DTOList bunga = deposito.getBunga();

        for (int j = 0; j < bunga.size(); j++) {
            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(j);

            bng.markUpdate();
        }

//        final DTOList renewal = deposito.getPerpanjangan2();
//
//        for (int k = 0; k < renewal.size(); k++) {
//            ARInvestmentPerpanjanganView rnl = (ARInvestmentPerpanjanganView) renewal.get(k);
//
//            rnl.markUpdate();
//        }

        setTitle("EDIT DEPOSITO");
    }

    public void superEdit(String ardepoid) throws Exception {
        view(ardepoid);

        //if (deposito.isEffective()) throw new Exception("Data Sudah Disetujui");

        setReadOnly(false);

        setEnableSuperEdit(true);

        deposito.markUpdate();

        final DTOList cair = deposito.getPencairan();

        for (int i = 0; i < cair.size(); i++) {
            ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

            pcr.markUpdate();
        }

        final DTOList bunga = deposito.getBunga();

        for (int j = 0; j < bunga.size(); j++) {
            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(j);

            bng.markUpdate();
        }

//        final DTOList renewal = deposito.getPerpanjangan2();
//
//        for (int k = 0; k < renewal.size(); k++) {
//            ARInvestmentPerpanjanganView rnl = (ARInvestmentPerpanjanganView) renewal.get(k);
//
//            rnl.markUpdate();
//        }

        setTitle("EDIT DEPOSITO");
    }

    public void view(String ardepoid) throws Exception {

        deposito = getRemoteGeneralLedger().loadDeposito(ardepoid);

        if (deposito == null) {
            throw new RuntimeException("Deposito not found !");
        }

        setReadOnly(true);

        setTitle("VIEW DEPOSITO");
    }

    public void save() throws Exception {
        final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView) ObjectCloner.deepCopy(deposito);

        validate();

        deposito.checkNodefo();

        onChgPeriod();

//        if (cloned.getStUpload() == null) {
//            throw new RuntimeException("File Lampiran Belum di Upload");
//        }

        getRemoteGeneralLedger().save(cloned);

        close();
    }

    public void saveWithoutJurnal() throws Exception {
        final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView) ObjectCloner.deepCopy(deposito);

        validate();

        deposito.checkNodefo();

        onChgPeriod();

        getRemoteGeneralLedger().saveWithoutJurnal(cloned);

        close();
    }

    public void approval(String ardepoid) throws Exception {
        edit(ardepoid);

        super.setReadOnly(true);

        approvalMode = true;

    }

    public void approve() throws Exception {

        String depo_id = deposito.getStARDepoID();

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        final ARInvestmentDepositoView jh = new ARInvestmentDepositoView();

        final DTOList details = jh.getDetails(depo_id);

        jh.markUpdate();
        jh.setStEffectiveFlag("Y");
        jh.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        jh.setDtApprovedDate(new Date());

        getRemoteGeneralLedger().approve(jh, details);

        approvalMode = true;

        close();
    }

    public void createRenewal(ARInvestmentDepositoView depo) throws Exception {

        approvalMode = true;

        /*
        final DTOList pencairan = depo.getPencairan();
        
        for (int i = 0; i < pencairan.size(); i++) {
        ARInvestmentPencairanView cair = (ARInvestmentPencairanView) pencairan.get(i);

        //if (cair.getDtTglCair()!=null) return;

        cair.setStARParentID(cair.getStARCairID());

        }
        
        final DTOList bunga = depo.getBunga();
        
        for (int j = 0; j < bunga.size(); j++) {
        ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(j);

        //if (bng.getDtTglCair()!=null) return;

        bng.setStARParentID(bng.getStARBungaID());
        }
         */

        depo.setStNextStatus(FinCodec.Deposito.RENEWAL);
        depo.setStApprovedWho(depo.getStApprovedWho());

        getRemoteGeneralLedger().saveRenewal(depo, depo.getStNextStatus(), approvalMode);

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

    public ARInvestmentDepositoView getDeposito() {
        return deposito;
    }

    public void setDeposito(ARInvestmentDepositoView deposito) {
        this.deposito = deposito;
    }

    public void onChgCurrency() throws Exception {
        deposito.setDbCurrencyRate(
                CurrencyManager.getInstance().getRate(
                deposito.getStCurrency(),
                deposito.getDtTgldepo()));
        deposito.setStCurrency(deposito.getStCurrency());
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }

    public void onChgCall() throws Exception {

        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            deposito.setStHari(deposito.getStHari());
            deposito.setStBulan("0");
        } else {
            deposito.setStHari("0");
            deposito.setStBulan(deposito.getStBulan());
        }

    }

    public void validate2() throws Exception {
        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            calcDays();
        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
            calcMonths();
        }
    }

    public void refresh() {
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }

    public void calcDays() throws Exception {

        if (Tools.compare(deposito.getStHari(), new String("20")) > 0) {
            throw new RuntimeException("Jangka waktu On Call tidak boleh melebihi 20 Hari");
        }

        if (deposito.getStHari() != null) {
            DateTime startDate = new DateTime(deposito.getDtTglawal());
            DateTime endDate = new DateTime();

            if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                endDate = startDate.plusDays(Integer.parseInt(deposito.getStHari()));
            }

            deposito.setDtTglakhir(endDate.toDate());
        }

    }

    public void calcMonths() throws Exception {

        if (Tools.compare(deposito.getStBulan(), new String("12")) > 0) {
            throw new RuntimeException("Jangka waktu Non On Call tidak boleh melebihi 12 Bulan");
        }

        if (deposito.getStBulan() != null) {
            DateTime startDate = new DateTime(deposito.getDtTglawal());
            DateTime endDate = new DateTime();

            if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                endDate = startDate.plusMonths(Integer.parseInt(deposito.getStBulan()));
            }

            deposito.setDtTglakhir(endDate.toDate());
        }
    }

    public void validate() {
        /*
        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
        if (deposito.getStHari()==null) throw new RuntimeException("Jumlah Hari Belum Diisi");
        } else {
        if (deposito.getStBulan()==null) throw new RuntimeException("Jumlah Bulan Belum Diisi");
        }*/

        Date policyDateStart = deposito.getDtTglawal();
        Date policyDateEnd = deposito.getDtTglakhir();

        DateTime startDate = new DateTime(policyDateStart);
        DateTime endDate = new DateTime(policyDateEnd);

        Days z = Days.daysBetween(startDate, endDate);
        int day = z.getDays();

        Months x = Months.monthsBetween(startDate, endDate);
        int month = x.getMonths();

        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            if (!deposito.getStHari().equalsIgnoreCase(Integer.toString(day))) {
                throw new RuntimeException("Tanggal Akhir bilyet tidak sesuai dengan Jangka Waktu On Call,  klik REFRESH");
            }
        }
//        else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
//            if (!deposito.getStMonths().equalsIgnoreCase(Integer.toString(month))) {
//                if (!DateUtil.getDays(deposito.getDtTglawal()).equalsIgnoreCase(DateUtil.getDays(deposito.getDtTglakhir()))) {
//                    //if (!DateUtil.getDays(deposito.getDtTglawal()).equalsIgnoreCase("31")) {
//                    throw new RuntimeException("Tanggal Akhir bilyet tidak sesuai dengan Jangka Waktu Non On Call, klik REFRESH");
//                    //}
//                }
//            }
//        }
    }

    public void doNewPencairan() {
        final ARInvestmentPencairanView pcr = new ARInvestmentPencairanView();

        pcr.markNew();

        deposito.getPencairan().add(pcr);

        pencairan = pcr;
    }

    public void doNewBunga() {
        final ARInvestmentBungaView bng = new ARInvestmentBungaView();

        bng.markNew();

        deposito.getBunga().add(bng);

        bunga = bng;
    }

//    public void doNewPerpanjangan() {
//        final ARInvestmentPerpanjanganView rnl = new ARInvestmentPerpanjanganView();
//
//        rnl.markNew();
//
//        deposito.getPerpanjangan2().add(rnl);
//
//        perpanjangan = rnl;
//    }
    public boolean isApprovalMode() {
        return approvalMode;
    }

    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public void generateDBNominal() throws Exception {
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    public void reverse() throws Exception {

        //boolean withinCurrentMonth = DateUtil.getDateStr(deposito.getDtTgldepo(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        //boolean canReverse = true;

        // if (!canReverse) throw new RuntimeException("Tanggal Pendebetan Tidak Valid (Sudah Tutup Produksi)");

        if (deposito.getStJournalStatus().equalsIgnoreCase("HISTORY")) {
            throw new RuntimeException("Nobuk Tidak Bisa Di-Reverse Karena Tanpa Jurnal (HISTORY)");
        }

        if (!deposito.isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (deposito.isPosted()) {
            throw new RuntimeException("Nobuk " + deposito.getStBuktiB() + " Masuk Mutasi Bulan " + deposito.getStMonths() + " dan Tahun " + deposito.getStYears() + " Yang Sudah Diposting");
        }

        getRemoteGeneralLedger().reverseDeposito(deposito);

        close();
    }

    public boolean isEnableSuperEdit() {
        return enableSuperEdit;
    }

    public void setEnableSuperEdit(boolean enableSuperEdit) {
        this.enableSuperEdit = enableSuperEdit;
    }

    public void setDate() throws Exception {

        String date = "01/" + deposito.getStMonths() + "/" + deposito.getStYears();

        deposito.setDtTglawal(DateUtil.getDate(date));
        deposito.setDtTgldepo(DateUtil.getDate(date));
        deposito.setDtTglmuta(DateUtil.getDate(date));
    }

    public void selectMonth() {
    }

    public void onChgPeriod() throws Exception {
        if (!Tools.isEqual(DateUtil.getMonth2Digit(deposito.getDtTgldepo()), deposito.getStMonths())) {
            throw new Exception("Bulan tidak sama dengan Tanggal Depo");
        }
        if (!Tools.isEqual(DateUtil.getYear(deposito.getDtTgldepo()), deposito.getStYears())) {
            throw new Exception("Tahun tidak sama dengan Tanggal Depo");
        }
    }
    private String notesindex;

    public String getNotesindex() {
        return notesindex;
    }

    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public void onNewBunga() throws Exception {
        final ARInvestmentBungaView bng = new ARInvestmentBungaView();

        bng.markNew();

        getBunga().add(bng);

        String n = String.valueOf(getBunga().size());
    }

    public void onDeleteBunga() throws Exception {
        getBunga().delete(Integer.parseInt(notesindex));

        String n = String.valueOf(getBunga().size());
    }

    public DTOList getBunga() throws Exception {
        return deposito.getBunga();
    }

    public void createBunga(String ardepoid) throws Exception {
        view(ardepoid);

        bungaMode = true;

        setReadOnly(false);

        if (!deposito.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (!deposito.isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui, Tidak Bisa Input Bunga");
        }

        DateTime tglak = new DateTime(deposito.getDtTglEntryCair());
        Date tglexp = tglak.plusDays(60).toDate();
        boolean compare = Tools.compare(new Date(), tglexp) > 0;

        if (compare) {
            throw new Exception("Tidak Bisa Input Bunga, Jangka Waktu Pencairan Telah Habis");
        }

        setTitle("BUNGA DEPOSITO");
    }

    public void saveBunga2() throws Exception {
        //final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView) ObjectCloner.deepCopy(deposito);

        validate();

        deposito.checkNodefo();

        onChgPeriod();

        getRemoteGeneralLedger().saveBunga2(deposito, bungaMode);

        close();
    }

    /**
     * @return the bungaMode
     */
    public boolean isBungaMode() {
        return bungaMode;
    }

    /**
     * @param bungaMode the bungaMode to set
     */
    public void setBungaMode(boolean bungaMode) {
        this.bungaMode = bungaMode;
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

    public void editBentuk(String ardepoid) throws Exception {
        view(ardepoid);

        if (deposito.getStJournalStatus().equalsIgnoreCase("NEW")) {
            if (deposito.isEffective()) {
                throw new Exception("Data Sudah Disetujui");
            }
        }

        setReadOnly(false);

        deposito.markUpdate();

        setTitle("EDIT DEPOSITO");

        inputBuktiMode = false;
    }

    public void saveBentuk() throws Exception {
        deposito.checkNodefo();

        if (deposito.getDtTansferDate() != null) {
            String bulantf = DateUtil.getMonth2Digit(deposito.getDtTansferDate());
            String bulantr = deposito.getStMonths();
            String bulantrplus = deposito.getStMonths() + 1;

//            if (Tools.compare(bulantf, bulantr) < 0) {
//                throw new Exception("Tanggal Transfer maksimal 1 Bulan dari Bulan Transaksi!");
//            }

//            if (!Tools.isEqual(DateUtil.getMonth2Digit(deposito.getDtTansferDate()), deposito.getStMonths())) {
//                throw new Exception("Bulan pada Tanggal Transfer harus sama dengan Bulan Transaksi!");
//            }

            DateTime currentDate = new DateTime(deposito.getDtTgldepo());

            Date maximumBackDate = currentDate.plusDays(3).toDate();

            boolean compare = Tools.compare(deposito.getDtTansferDate(), maximumBackDate) > 0;
            if (SessionManager.getInstance().getSession().getStBranch() != null) {
                if (compare) {
                    throw new RuntimeException("Tanggal Transfer melebihi waktu 3 hari dari Tanggal Awal Debet!");
                }
            }

            boolean compare2 = Tools.compare(deposito.getDtTansferDate(), deposito.getDtTgldepo()) < 0;
            if (compare2) {
                throw new RuntimeException("Tanggal Transfer dibawah Tanggal Awal Debet!");
            }
        }

//        if (deposito.getStUpload() == null) {
//            throw new RuntimeException("File Lampiran Belum di Upload");
//        }

        getRemoteGeneralLedger().saveIzinBentuk(deposito);

        close();
    }

    public void editInputBilyet(String ardepoid) throws Exception {
        view(ardepoid);

        setReadOnly(false);

        deposito.setStNodefoOld(deposito.getStNodefo());
        deposito.setStNoRekeningOld(deposito.getStNoRekening());
        deposito.markUpdate();

        logger.logDebug("@@@@@@@@@@@@@1 " + deposito.getStNodefoOld());
        logger.logDebug("@@@@@@@@@@@@@2 " + deposito.getStNoRekeningOld());
        logger.logDebug("@@@@@@@@@@@@@3 " + deposito.getStNodefo());
        logger.logDebug("@@@@@@@@@@@@@4 " + deposito.getStNoRekening());

        deposito.markUpdate();

        setTitle("INPUT BUKTI DEPOSITO");

        inputBuktiMode = true;
    }

    public void editPosisiBilyet(String ardepoid) throws Exception {
        view(ardepoid);

        setReadOnly(false);

        deposito.markUpdate();

        setTitle("INPUT POSISI BILYET");

        inputPosisiMode = true;
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

    public void recalculateBentuk() throws Exception {
        if (deposito.getDbNominalDana() == null) {
            throw new RuntimeException("Transfer Dana wajib diisi, jika ingin by-pass harap Ijin Pusat!");
        }

        BigDecimal nominal = BDUtil.mul(deposito.getDbNominal(), BDUtil.getRateFromPct(new BigDecimal(20)));
        if (Tools.compare(deposito.getDbNominalDana(), nominal) < 0) {
            throw new RuntimeException("Transfer Dana wajib diisi minimal 20% Deposito");
        }
    }

    public void approveBentuk() throws Exception {

        if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
            if (!deposito.isIjinPusat()) {
                recalculateBentuk();
            }
        }

        String depo_id = deposito.getStARDepoID();

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        final ARInvestmentDepositoView jh = new ARInvestmentDepositoView();

        final DTOList details = jh.getDetails(depo_id);

        jh.markUpdate();
        jh.setStEffectiveFlag("Y");
        jh.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        jh.setDtApprovedDate(new Date());

        getRemoteGeneralLedger().approve(jh, details);

        approvalMode = true;

        close();
    }

    /**
     * @return the inputPosisiMode
     */
    public boolean isInputPosisiMode() {
        return inputPosisiMode;
    }

    /**
     * @param inputPosisiMode the inputPosisiMode to set
     */
    public void setInputPosisiMode(boolean inputPosisiMode) {
        this.inputPosisiMode = inputPosisiMode;
    }

    public void saveUpload() throws Exception {
        final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView) ObjectCloner.deepCopy(deposito);

        getRemoteGeneralLedger().saveUpload(cloned);

        close();
    }

    public void onNewCair() throws Exception {
        final ARInvestmentPencairanView cair = new ARInvestmentPencairanView();

        cair.markNew();

        getPencairan().add(cair);

        String n = String.valueOf(getPencairan().size());
    }

    public void onDeleteCair() throws Exception {
        getPencairan().delete(Integer.parseInt(notesindex));

        String n = String.valueOf(getPencairan().size());
    }

    public DTOList getPencairan() throws Exception {
        return deposito.getPencairan();
    }

    public void createCair(String ardepoid) throws Exception {
        view(ardepoid);

        cairMode = true;

        setReadOnly(false);

        if (!deposito.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (!deposito.isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui, Tidak Bisa Input Bunga");
        }

        onNewCair();

//        DateTime tglak = new DateTime(deposito.getDtTglEntryCair());
//        Date tglexp = tglak.plusDays(60).toDate();
//        boolean compare = Tools.compare(new Date(), tglexp) > 0;
//
//        if (compare) {
//            throw new Exception("Tidak Bisa Input Bunga, Jangka Waktu Pencairan Telah Habis");
//        }

        setTitle("PENCAIRAN DEPOSITO");
    }

    public void saveCair() throws Exception {

        validate();

        deposito.checkNodefo();

        onChgPeriod();

        getRemoteGeneralLedger().saveCair(deposito, cairMode);

        close();
    }

    /**
     * @return the bungaMode
     */
    public boolean isCairMode() {
        return cairMode;
    }

    /**
     * @param bungaMode the bungaMode to set
     */
    public void setCairMode(boolean cairMode) {
        this.cairMode = cairMode;
    }

    public void editBilyet() throws Exception {

        getRemoteGeneralLedger().saveEditBilyet(deposito);

        close();
    }
}

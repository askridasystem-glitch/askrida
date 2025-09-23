/***********************************************************************
 * Module:  com.webfin.ar.forms.BungaForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.util.DateUtil;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;

import com.webfin.insurance.ejb.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import org.joda.time.DateTime;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;

public class BungaForm extends Form {

    private ARInvestmentBungaView bunga;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean approvalMode;
    private boolean reverseMode;
    private boolean Posted;
    private final static transient LogManager logger = LogManager.getInstance(BungaForm.class);

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void edit(String arbungaid) throws Exception {
        view(arbungaid);

        //bunga.setStEntityID(bunga.findAccountBank(bunga.getStEntityID()));
        //bunga.setStBankName(new BigDecimal(20));

        DateTime tglak = new DateTime(bunga.getDtTglEntryCair());
        Date tglexp = tglak.plusDays(60).toDate();
        boolean compare = Tools.compare(new Date(), tglexp) > 0;
        /*
        DateTime tgltempo = new DateTime(bunga.getDtTglAkhir());
        Date tglexp2 = tgltempo.toDate();
        boolean compare2 = Tools.compare(new Date(), tglexp2) > 0;
         */

        if (!bunga.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (bunga.isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        if (bunga.getDeposito() != null) {
            if (!bunga.getDeposito().isEffective()) {
                throw new Exception("Pembentukan Deposito Belum Disetujui");
            }
        }

        //if (compare2) throw new Exception("Jangka Waktu Deposito Sudah Berakhir, Harap Perpanjangan");
        if (compare) {
            throw new Exception("Tidak Bisa Input Bunga, Jangka Waktu Pencairan Telah Habis");
        }

        setReadOnly(false);

        if (bunga.getStAccountBank() == null) {
            if (bunga.getStReceiptClassID().equalsIgnoreCase("3")) {

                bunga.setStEntityID(bunga.findSpecimenDir(bunga.getStCostCenterCode()));
                //logger.logDebug("$$$$$$$$$$$$$$$ "+bunga.getStEntityID());
                bunga.setStBankName(bunga.findSpecimenDirDesc(bunga.getStEntityID()));
            }
        }

        bunga.markUpdate();

        setTitle("EDIT BUNGA");
    }

    public void view(String arbungaid) throws Exception {

        //bunga = (ARInvestmentBungaView) DTOPool.getInstance().getDTO(ARInvestmentBungaView.class, arbungaid);

        bunga = getRemoteGeneralLedger().loadBunga(arbungaid);

        setReadOnly(true);

        setTitle("VIEW BUNGA");
    }

    public void save() throws Exception {

        onChgPeriod();

        getRemoteGeneralLedger().saveBunga(bunga);

        close();
    }

    public void approval(String arbungaid) throws Exception {
        edit(arbungaid);

        super.setReadOnly(true);

        approvalMode = true;
    }

    public void approve() throws Exception {

        if (!bunga.getDeposito().isEffective()) {
            throw new Exception("Pembentukan Deposito Belum Disetujui");
        }

        String bunga_id = bunga.getStARBungaID();

        if (bunga.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + bunga.getStMonths() + " dan tahun " + bunga.getStYears() + " tsb sudah diposting");
        }

        final ARInvestmentBungaView jh = new ARInvestmentBungaView();

        final DTOList details = jh.getDetails(bunga_id);

        jh.markUpdate();
        jh.setStEffectiveFlag("Y");
        jh.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        jh.setDtApprovedDate(new Date());

        getRemoteGeneralLedger().approve(jh, details);

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

    public void refresh() throws Exception {
    }

    public ARInvestmentBungaView getBunga() {
        return bunga;
    }

    public void setBunga(ARInvestmentBungaView bunga) {
        this.bunga = bunga;
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

    public void reverse() throws Exception {

        if (!bunga.isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        if (bunga.isPosted()) {
            throw new RuntimeException("Nobuk " + bunga.getStNoBuktiD() + " Masuk Mutasi Bulan " + bunga.getStMonths() + " dan Tahun " + bunga.getStYears() + " Yang Sudah Diposting");
        }

        getRemoteGeneralLedger().reverseBunga(bunga);

        close();
    }

    public void setDate() throws Exception {

        String date = "01/" + bunga.getStMonths() + "/" + bunga.getStYears();

        bunga.setDtTglBunga(DateUtil.getDate(date));
    }

    public void selectMonth() {
    }

    public void onChgPeriod() throws Exception {
        if (!Tools.isEqual(DateUtil.getMonth2Digit(bunga.getDtTglBunga()), bunga.getStMonths())) {
            throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
        }
        if (!Tools.isEqual(DateUtil.getYear(bunga.getDtTglBunga()), bunga.getStYears())) {
            throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
        }
    }

    public void editBng(String arbungaid) throws Exception {
        view(arbungaid);

        if (!bunga.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (bunga.isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        setReadOnly(false);

        bunga.markUpdate();

        setTitle("EDIT BUNGA");
    }

    public void saveBng() throws Exception {

        onChgPeriod();

        getRemoteGeneralLedger().saveBungaBng(bunga);

        close();
    }
}

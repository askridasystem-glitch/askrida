/***********************************************************************
 * Module:  com.webfin.ar.forms.RequestFeeForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.controller.FormTab;
import com.crux.common.parameter.Parameter;
import com.crux.ff.model.FlexTableView;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.gl.ejb.CurrencyManager;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.AccountView2;
import com.webfin.insurance.model.BiayaOperasionalDetail;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.joda.time.DateTime;
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RequestFeeForm extends Form {

    private ARRequestFee arrequest;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private boolean reverseMode;
    private boolean enableSuperEdit;
    private boolean cashierMode;
    private boolean rePrintMode;
//    private String stAccountEntityNo;
//    private String stAccountEntityDesc;
    private FormTab tabs;
    private DTOList document;
    private String instIndex;
    private boolean approvalMode;
    private boolean approvalByDireksiMode;
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("REQ_APPROVAL");
    private boolean canApproveDireksi = SessionManager.getInstance().getSession().hasResource("REQ_APPROVAL_DIR");
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("REQ_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("REQ_NAVRE");
    private final static transient LogManager logger = LogManager.getInstance(RequestFeeForm.class);
    private static final int BUFFER_SIZE = 4096;
    private String stProposalName;
    private ARRequestFeeObj reqObject;
    private boolean validasiMode;
    private boolean validasiRealzMode;
    private boolean cashflowMode;
    private boolean valFinanceMode;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public String getStStatus() {
        if (arrequest == null) {
            return null;
        }
        return arrequest.getStNextStatus() == null ? arrequest.getStStatus() : arrequest.getStNextStatus();
    }

    public void setStStatus(String stStatus) {
    }

    public void clickCreateNew() {
        arrequest = new ARRequestFee();
        arrequest.markNew();

        arrequest.setStStatus("APP");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");

        arrequest.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        arrequest.setStRegionID(SessionManager.getInstance().getSession().getStRegion());

        arrequest.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        arrequest.setDbCurrencyRate(BDUtil.one);

        arrequest.setStYears(DateUtil.getYear(new Date()));

        arrequest.markNew();

        setTitle("PERMINTAAN ANGGARAN");

        initTabs();
//        initTabsNew();
    }

    public void clickCreateProposal() throws Exception {
        arrequest = new ARRequestFee();
        arrequest.markNew();

        arrequest.setStStatus("PENGAJUAN");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStValidasiF("N");
//        arrequest.setStAnggaranType("1");

//        arrequest.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
//        arrequest.setStRegionID(SessionManager.getInstance().getSession().getStRegion());

        arrequest.setStCostCenterCode(getStBranch());
        arrequest.setStRegionID(SessionManager.getInstance().getSession().getStDivisionID());

        arrequest.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        arrequest.setDbCurrencyRate(BDUtil.one);

//        arrequest.setStYears(DateUtil.getYear(new Date()));

        final DTOList approval = arrequest.getApproval();
        if (approval.size() == 0) {
            final ARRequestApprovalView item = onNewApproval();
        }

        setTitle("PENGAJUAN ANGGARAN");

        initTabs();
//        initTabsNew();

        proposalMode = true;
        realisasiMode = false;
    }

    public void clickCreateRealized() throws Exception {
        arrequest = new ARRequestFee();
        arrequest.markNew();

        arrequest.setStStatus("REALISASI");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStValidasiF("N");
        arrequest.setStAnggaranType("2");
        arrequest.setStPilihan("3");

        arrequest.setStCostCenterCode(getStBranch());
        arrequest.setStRegionID(SessionManager.getInstance().getSession().getStDivisionID());

        arrequest.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        arrequest.setDbCurrencyRate(BDUtil.one);

        arrequest.setStYears(DateUtil.getYear(new Date()));

        final DTOList approval = arrequest.getApproval();
        if (approval.size() == 0) {
            final ARRequestApprovalView item = onNewApproval();
        }

        arrequest.markNew();

        setTitle("REALISASI ANGGARAN");

        initTabs();
//        initTabsNew();

        proposalMode = false;
        realisasiMode = true;
    }

    public void clickEdit(String arreqid) throws Exception {
        clickView(arreqid);

        if (arrequest.isValidasiFlag()) {
            throw new Exception("Anggaran Sudah Disetujui");
        }

        setReadOnly(false);

        arrequest.markUpdate();
        arrequest.getReqObject().markAllUpdate();
        arrequest.getDocuments().markAllUpdate();
        arrequest.getApproval().markAllUpdate();

        if (arrequest.isStatusProposal()) {
            setTitle("EDIT PENGAJUAN");
            proposalMode = true;
            realisasiMode = false;
        } else if (arrequest.isStatusRealized()) {
            setTitle("EDIT REALISASI");
            proposalMode = false;
            realisasiMode = true;
        }
    }

    public void clickSuperEdit(String arreqid) throws Exception {
        clickView(arreqid);

        setReadOnly(false);

        //loadAccountNo();

        setEnableSuperEdit(true);

        arrequest.markUpdate();

        if (arrequest.isStatusProposal()) {
            setTitle("LIHAT PENGAJUAN");
            proposalMode = true;
            realisasiMode = false;
        } else if (arrequest.isStatusRealized()) {
            setTitle("LIHAT REALISASI");
            proposalMode = false;
            realisasiMode = true;
        }
    }

    public void clickView(String arreqid) throws Exception {

        arrequest = getRemoteGeneralLedger().loadRequest(arreqid);

        if (arrequest == null) {
            throw new RuntimeException("Permintaan tidak dapat ditemukan !");
        }

        setReadOnly(true);

        setTitle("LIHAT");

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@1 " + arrequest.isStatusApproval());
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@2 " + arrequest.isStatusCashback());
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@3 " + arrequest.isStatusRefund());
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@4 " + arrequest.isStatusRequest());

        initTabs();

        if (arrequest.isStatusProposal()) {
            setTitle("EDIT PENGAJUAN");
            proposalMode = true;
            realisasiMode = false;
        } else if (arrequest.isStatusRealized()) {
            setTitle("EDIT REALISASI");
            proposalMode = false;
            realisasiMode = true;
        }
    }

    public void doSave() throws Exception {

        onChgPeriod();

//        if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
//
////            if (arrequest.getStReceiptClassID().equalsIgnoreCase("A")) {
////                throw new RuntimeException("Metode harus Bank");
////            }
//
//            checkLampiranAktiva();
//        }
//
//        if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
////            if (!arrequest.getStReceiptClassID().equalsIgnoreCase("A")) {
////                throw new RuntimeException("Metode harus Kas");
////            }
//        }

//        if (arrequest.isStatusCashback()
//                || arrequest.isStatusRefund()) {
//            recalculatedet();
//        }


        String nirpKadiv = null;
        String nirpDirBidang = null;
        if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
            nirpKadiv = Parameter.readString("KADIV_CAPTIVE");
            nirpDirBidang = Parameter.readString("DIRPEM");
        } else if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
            nirpKadiv = Parameter.readString("KADIV_SDM");
            nirpDirBidang = Parameter.readString("DIROPS");
        } else if (arrequest.getStPilihan().equalsIgnoreCase("3")) {
            nirpKadiv = Parameter.readString("KADIV_UMUM");
            nirpDirBidang = Parameter.readString("DIROPS");
        } else if (arrequest.getStPilihan().equalsIgnoreCase("14")) {
            nirpKadiv = Parameter.readString("KADIV_UMUM");
            nirpDirBidang = Parameter.readString("DIROPS");
        }

        String nirpDirektorat = Parameter.readString(getDirektorat());

        /*CODINGAN PERUBAHAN*/
//        if (arrequest.isProgramKerja()) {
//            if (isProposalMode()) {
//                boolean cekLimit = isCekLimit();
//                if (cekLimit) {
//                    final DTOList objects = arrequest.getApproval();
////                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + objects.size());
//                    for (int i = 0; i < getLevelDireksi(); i++) {
//                        ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(i);
//
////                        obj.setStApprovalWho(nirpKadiv);
//                        doNewApproval();
//                    }
//                } else {
//                    final DTOList objects = arrequest.getApproval();
//                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + objects.size());
//                    for (int i = 0; i < objects.size(); i++) {
//                        ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(0);
//
//                        obj.setStApprovalWho(nirpKadiv);
//                    }
//                }
//            }
//        }

        /*CODINGAN ORI*/
        if (arrequest.isProgramKerja()) {
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@ simpan ke Request_Approval ");
            if (isProposalMode()) {
                final DTOList objects = arrequest.getApproval();
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + objects.size());
                for (int i = 0; i < objects.size(); i++) {
                    ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(0);

                    obj.setStApprovalWho(nirpKadiv);
                }
            }
        }
        if (arrequest.isBiayaRutin()) {
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@ simpan ke Request_Approval ");
            if (isRealisasiMode()) {
                final DTOList objects = arrequest.getApproval();
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + objects.size());
                for (int i = 0; i < objects.size(); i++) {
                    ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(0);

                    obj.setStApprovalWho(nirpKadiv);
                }
            }
        }

        recalculatedet();

//        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode, null);
        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode);
//        getRemoteGeneralLedger().saveRequest(arrequest, approvalMode);

        close();
    }

    public void clickApproval(String arreqid) throws Exception {
        clickEdit(arreqid);

        if (isApprovalMode()) {
            if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
                if (!arrequest.isOwnerPms()) {
                    throw new RuntimeException("Tidak bisa setujui, bukan kewenangan!");
                }
            } else if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
                if (!arrequest.isOwnerUmum()) {
                    throw new RuntimeException("Tidak bisa setujui, bukan kewenangan!");
                }
            } else if (arrequest.getStPilihan().equalsIgnoreCase("3")) {
                if (!arrequest.isOwnerAdm()) {
                    throw new RuntimeException("Tidak bisa setujui, bukan kewenangan!");
                }
            }
        }

        setReadOnly(true);

        //loadAccountNo();

        approvalMode = true;

//        tabs.setActiveTab("TAB_APPROVAL");

//        initTabsNew();
    }

    public void clickApprovalDireksi(String policyID) throws Exception {
        clickEdit(policyID);

        setReadOnly(true);

        //loadAccountNo();

        approvalByDireksiMode = true;

//        tabs.setActiveTab("TAB_POLICY_DOCUMENTS");

//        initTabs();
    }

    public void clickCashier(String arreqid) throws Exception {
        clickEditCashier(arreqid);

        setReadOnly(false);

        //loadAccountNo();

        cashierMode = true;
    }

    public void doApproveOLD() throws Exception {

        //CEK LIMIT PERSETUJUAN
        if (arrequest.isStatusProposal()) {

            //if (arrequest.getStDocuments() == null) {
            //    throw new RuntimeException("E-Document belum ada");
            //}

            if (isApprovalMode()) {

//                boolean appPemilik = false;
//                if (SessionManager.getInstance().getUserID().equalsIgnoreCase("040787") //bu ning
//                        || SessionManager.getInstance().getUserID().equalsIgnoreCase("05101082") //pak opi
//                        || SessionManager.getInstance().getUserID().equalsIgnoreCase("04650076")) //pak warman
//                {
//
//                    appPemilik = true;
//                }
//
//                if (arrequest.getStAnggaranType().equalsIgnoreCase("1")) {
//                    if (!appPemilik) {
//                        throw new RuntimeException("Harus Disetujui Oleh Pemilik Anggaran!");
//                    }
//                }

                if (arrequest.getStAnggaranType().equalsIgnoreCase("1")) {
                    if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
                        if (!SessionManager.getInstance().getUserID().equalsIgnoreCase("05101082")) {
                            throw new RuntimeException("Harus Disetujui Oleh Pemilik Anggaran!");
                        }
                    } else if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
                        if (!SessionManager.getInstance().getUserID().equalsIgnoreCase("040787")) {
                            throw new RuntimeException("Harus Disetujui Oleh Pemilik Anggaran!");
                        }
                    } else if (arrequest.getStPilihan().equalsIgnoreCase("3")) {
                        if (!SessionManager.getInstance().getUserID().equalsIgnoreCase("04650076")) {
                            throw new RuntimeException("Harus Disetujui Oleh Pemilik Anggaran!");
                        }
                    }
                }

                BigDecimal transactionLimit = getTransactionLimit("AUTHO", SessionManager.getInstance().getUserID());

                boolean enoughLimit = false;
                enoughLimit = Tools.compare(transactionLimit, arrequest.getDbNominal()) >= 0;

                if (!enoughLimit) {
                    arrequest.setStEffectiveFlag("N");
                    throw new RuntimeException("Nilai Persetujuan Melebihi Limit Kewenangan Anda, Limit Anda adalah " + JSPUtil.print(transactionLimit, 2));
                }
            }

            if (isApprovalByDireksiMode()) {

                DTOList policyDocuments = arrequest.getPolicyDocuments();

                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                    if (doc.getStInsuranceDocumentTypeID().equalsIgnoreCase("94")) {
                        if (doc.getStFilePhysic() == null) {
                            throw new RuntimeException("File Lampiran Persetujuan Direksi Harus Dilampirkan");
                        }
                    }
                }

                BigDecimal transactionLimit = getTransactionLimit("AUTHO", "dirtek");

                boolean enoughLimit = false;
                enoughLimit = Tools.compare(transactionLimit, arrequest.getDbNominal()) >= 0;

                if (!enoughLimit) {
                    arrequest.setStEffectiveFlag("N");
                    throw new RuntimeException("Nilai Persetujuan Melebihi Limit Kewenangan Anda, Limit Anda adalah " + JSPUtil.print(transactionLimit, 2));
                }
            }
        }

//        if (arrequest.isStatusCashback()) {
//
//            BigDecimal transactionLimit = getTransactionLimit("AUTHO", SessionManager.getInstance().getUserID());
//
//            boolean enoughLimit = false;
//            enoughLimit = Tools.compare(transactionLimit, arrequest.getDbNominal()) >= 0;
//
//            if (!enoughLimit) {
//                arrequest.setStEffectiveFlag("N");
//                throw new RuntimeException("Nilai Persetujuan Melebihi Limit Kewenangan Anda, Limit Anda adalah " + JSPUtil.print(transactionLimit, 2));
//            }
//        }

        arrequest.setStEffectiveFlag("Y");
        arrequest.setStApprovedWho(SessionManager.getInstance().getSession().getStUserID());
        arrequest.setDtApprovedDate(new Date());
        approvalMode = true;

        arrequest.markUpdate();

        //clickSave();
//        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode, null);
        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode);
//        getRemoteGeneralLedger().saveRequest(arrequest, approvalMode);

        close();
    }

    public void doApproveCashier() throws Exception {

        if (arrequest.isStatusProposal()) {
            arrequest.setStCashflowF("Y");
        }

        if (arrequest.isStatusRealized()) {
            arrequest.setStCashierFlag("Y");
        }

        arrequest.markUpdate();

        //loadAccountNo();

        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), cashierMode);
//        getRemoteGeneralLedger().saveRequest(arrequest, cashierMode);

//        final DTOList details = arrequest.getReqObject();
//        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), cashierMode, details);


//        if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
//            if (arrequest.isStatusCashback()) {
//                saveToInventaris(arrequest, details);
//            }

        close();
    }

    public void close() {
        super.close();
    }

    public String getStBranch() {
        String cabang = null;
        if (stBranch == null) {
            cabang = "00";
        } else {
            cabang = stBranch;
        }
        return cabang;
//        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
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

    public void refresh() throws Exception {
        //untuk proposal komisi//
//        if (arrequest.getStBiaopGroupID() != null) {
//            if (arrequest.getStBiaopGroupID().equalsIgnoreCase("19")) {
//                onProposalAmount();
//            }
//        }
    }

    public void doReverse() throws Exception {

        if (!arrequest.isValidasiFlag()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        if (arrequest.isPosted()) {
            throw new RuntimeException("Nobuk " + arrequest.getStTransactionNo() + " Masuk Mutasi Bulan " + arrequest.getStMonths() + " dan Tahun " + arrequest.getStYears() + " Yang Sudah Diposting");
        }

        getRemoteGeneralLedger().reverseRequest(arrequest);

        close();
    }

    public boolean isEnableSuperEdit() {
        return enableSuperEdit;
    }

    public void setEnableSuperEdit(boolean enableSuperEdit) {
        this.enableSuperEdit = enableSuperEdit;
    }

    /**
     * @return the arrequest
     */
    public ARRequestFee getArrequest() {
        return arrequest;
    }

    /**
     * @param arrequest the arrequest to set
     */
    public void setArrequest(ARRequestFee arrequest) {
        this.arrequest = arrequest;
    }

    public void editCreatePolicy(String arreqid) throws Exception {

        if (arreqid == null) {

            clickCreateNew();

            arrequest.setStStatus("APP");

            return;
        }

        clickSuperEdit(arreqid);

        if (arrequest.isStatusApproval() || arrequest.isStatusCashback() || arrequest.isStatusRefund()) {
            throw new RuntimeException("Data Harus Status REQUEST");
        }

        checkActiveEffective();

        arrequest.setStNextStatus("APP");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStApprovedWho(null);
        arrequest.setDtApprovedDate(null);

        arrequest.markUpdate();

        setTitle("PERSETUJUAN BIAYA");
    }

    private void checkActiveEffective() {
        if (!arrequest.isActive()) {
            throw new RuntimeException("Data Tidak Aktif");
        }

        if (!arrequest.isValidasiFlag()) {
            throw new RuntimeException("Permintaan Belum Disetujui");
        }
    }

    /**
     * @return the cashierMode
     */
    public boolean isCashierMode() {
        return cashierMode;
    }

    /**
     * @param cashierMode the cashierMode to set
     */
    public void setCashierMode(boolean cashierMode) {
        this.cashierMode = cashierMode;
    }

    public void clickEditCashier(String arreqid) throws Exception {
        clickView(arreqid);

        if (!arrequest.isValidasiFlag()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        cashierMode = true;

        arrequest.markUpdate();

        setTitle("SETUJUI OLEH KASIR");
    }

    public BigDecimal getTransactionLimit(String cat, String userID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select max(c.refn1) "
                    + "from s_user_roles b "
                    + "inner join ff_table c on c.fft_group_id = ? and c.ref2 = b.role_id "
                    + "where c.active_flag = 'Y' and b.user_id = ? "
                    + "and date_trunc('day',c.period_start) <= ? "
                    + "and date_trunc('day',c.period_end) >= ? ");

            S.setParam(1, cat);
            S.setParam(2, userID);
            S.setParam(3, arrequest.getDtTglRequest());
            S.setParam(4, arrequest.getDtTglRequest());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getRKAPLimitOLD(String biaoptype) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select max(a.refn1) "
                    + "from ff_table a "
                    + "where a.fft_group_id = 'RKAP' and a.active_flag = 'Y' "
                    + "and a.ref1 = ? and a.ref2 = ? and a.ref3 = ? "
                    + "and date_trunc('day',a.period_start) <= ? "
                    + "and date_trunc('day',a.period_end) >= ? ");

            S.setParam(1, arrequest.getStCostCenterCode());
            S.setParam(2, arrequest.getStRegionID());
            S.setParam(3, biaoptype);
            S.setParam(4, arrequest.getDtTglRequest());
            S.setParam(5, arrequest.getDtTglRequest());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getTotalNominal() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select coalesce(sum(a.nominal_used),0) "
                    + "from ar_request_fee a "
                    + "inner join s_biaop_detail b on b.biaop_dtl_id = a.biaoptypeid "
                    + "where a.status = 'CSB' and a.cashier_flag = 'Y' and a.deleted is null "
                    + "and a.biaoptypeid = ? and a.cc_code = ? and a.region_id = ? ");

            S.setParam(1, arrequest.getStBiaopTypeID());
            S.setParam(2, arrequest.getStCostCenterCode());
            S.setParam(3, arrequest.getStRegionID());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void editCreateReimburse(String arreqid) throws Exception {

        if (arreqid == null) {

            clickCreateNew();

            arrequest.setStStatus("CSB");

            return;
        }

        clickSuperEdit(arreqid);

        if (arrequest.isStatusRequest() || arrequest.isStatusCashback() || arrequest.isStatusRefund()) {
            throw new RuntimeException("Anggaran Harus Status APPROVED");
        }

        if (!arrequest.isCashierFlag()) {
            throw new RuntimeException("Persetujuan belum di-Approve kasir");
        }

        DateTime currentDateLastDay = new DateTime(arrequest.getDtCashierDate());

        Date maximumBackDate = currentDateLastDay.plusDays(7).toDate();

        boolean compare = Tools.compare(new Date(), maximumBackDate) > 0;
        if (compare) {
            throw new RuntimeException("Pengeluaran uang muka kas kecil max 7 hari kerja kwitansi harus disampaikan ke kasir");
        }

        checkActiveEffective();

        arrequest.generateEndorseNo();
        arrequest.setStNextStatus("CSB");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStApprovedWho(null);
        arrequest.setDtApprovedDate(null);
        arrequest.setStCashierWho(null);
        arrequest.setDtCashierDate(null);
        arrequest.setStPrintFlag(null);
        arrequest.setStTransactionNo(null);
        arrequest.setDbNominalUsed(null);

        arrequest.checkRealisasiNoBefore(arrequest.getStRequestNo());

        approvalMode = false;

        arrequest.markUpdate();
//        getReqObject().deleteAll();

        setTitle("REALISASI BIAYA");

        initTabs();
    }

    /**
     * @return the approvalByDireksiMode
     */
    public boolean isApprovalByDireksiMode() {
        return approvalByDireksiMode;
    }

    /**
     * @param approvalByDireksiMode the approvalByDireksiMode to set
     */
    public void setApprovalByDireksiMode(boolean approvalByDireksiMode) {
        this.approvalByDireksiMode = approvalByDireksiMode;
    }

    public void chgCurrency() throws Exception {
        arrequest.setDbCurrencyRate(CurrencyManager.getInstance().getRate(arrequest.getStCurrency(), arrequest.getDtTglRequest()));
    }

    public void selectMonth() {
    }

    public void setDate() throws Exception {

        if (isPosted()) {
            String tahun = arrequest.getStYears();
            arrequest.setStYears(null);
            throw new RuntimeException("Transaksi bulan " + arrequest.getStMonths() + " dan tahun " + tahun + " tsb sudah diposting");
        }


        String date = DateUtil.getDays2Digit(new Date()) + "/" + arrequest.getStMonths() + "/" + arrequest.getStYears();

        arrequest.setDtTglRequest(DateUtil.getDate(date));

    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if (arrequest.getStCostCenterCode() != null) {
                cek = cek + " and cc_code = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, arrequest.getStMonths());
            PS.setString(2, arrequest.getStYears());

            if (arrequest.getStCostCenterCode() != null) {
                PS.setString(3, arrequest.getStCostCenterCode());
            }

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

//    /**
//     * @return the stAccountEntityNo
//     */
//    public String getStAccountEntityNo() {
//        return stAccountEntityNo;
//    }
//
//    /**
//     * @param stAccountEntityNo the stAccountEntityNo to set
//     */
//    public void setStAccountEntityNo(String stAccountEntityNo) {
//        this.stAccountEntityNo = stAccountEntityNo;
//    }
//
//    /**
//     * @return the stAccountEntityDesc
//     */
//    public String getStAccountEntityDesc() {
//        return stAccountEntityDesc;
//    }
//
//    /**
//     * @param stAccountEntityDesc the stAccountEntityDesc to set
//     */
//    public void setStAccountEntityDesc(String stAccountEntityDesc) {
//        this.stAccountEntityDesc = stAccountEntityDesc;
//    }
//    public void loadAccountNo() throws Exception {
//        AccountView2 acc = getAccount();
//
//        if (acc != null) {
//            setStAccountEntityNo(acc.getStAccountNo());
//            setStAccountEntityDesc(acc.getStDescription());
//        }
//
//        //getEdocumentPDF();
//
//    }
    public AccountView2 getAccount() {
        if (arrequest.getStAccountID() == null) {
            return null;
        }
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, arrequest.getStAccountID());
    }

    public void onChgPeriod() throws Exception {

        if (!Tools.isEqual(DateUtil.getMonth2Digit(arrequest.getDtTglRequest()), arrequest.getStMonths())) {
            throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
        }
        if (!Tools.isEqual(DateUtil.getYear(arrequest.getDtTglRequest()), arrequest.getStYears())) {
            throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
        }

    }

    /**
     * @return the tabs
     */
    public FormTab getTabs() {
        return tabs;
    }

    /**
     * @param tabs the tabs to set
     */
    public void setTabs(FormTab tabs) {
        this.tabs = tabs;
    }

    private void initTabsNew() {
        tabs = new FormTab();

        tabs.add(new FormTab.TabBean("TAB_LAMPIRAN", "LAMPIRAN", true));

        tabs.setActiveTab("TAB_LAMPIRAN");
    }

    private void initTabs() {
        tabs = new FormTab();

        tabs.add(new FormTab.TabBean("TAB_DETIL", "DETIL", true));
        tabs.add(new FormTab.TabBean("TAB_LAMPIRAN", "LAMPIRAN", false));
        tabs.add(new FormTab.TabBean("TAB_APPROVAL", "PERSETUJUAN", false));
//        tabs.add(new FormTab.TabBean("TAB_POLICY_DOCUMENTS", "DOKUMEN", false));
//        tabs.add(new FormTab.TabBean("TAB_BRANCH", "CABANG", false));


        if (arrequest.isStatusProposal()) {
            tabs.setActiveTab("TAB_DETIL");
            tabs.enable("TAB_LAMPIRAN", true);
            if (isOwnerAdm() || isOwnerPms() || isOwnerUmum()) {
                tabs.enable("TAB_APPROVAL", true);
//                tabs.enable("TAB_POLICY_DOCUMENTS", true);
            }
        }

        if (arrequest.isStatusRealized()) {
            tabs.setActiveTab("TAB_DETIL");
            tabs.enable("TAB_LAMPIRAN", true);
            if (isOwnerAdm() || isOwnerPms() || isOwnerUmum()) {
                tabs.enable("TAB_APPROVAL", true);
            }
        }

//        if (arrequest.isStatusApproval()) {
//            tabs.setActiveTab("TAB_DETIL");
//            tabs.enable("TAB_LAMPIRAN", true);
//            tabs.enable("TAB_POLICY_DOCUMENTS", true);
////            if (isApprovalByDireksiMode()) {
////                if (arrequest.getBiaopDetil().isHeadOfficeFlag() && isCanNavigateRegion()) {
////                    tabs.enable("TAB_BRANCH", true);
////                }
////            }
//        }

//        if (arrequest.isStatusCashback()) {
//            tabs.setActiveTab("TAB_DETIL");
//            tabs.enable("TAB_LAMPIRAN", true);
//            tabs.enable("TAB_POLICY_DOCUMENTS", true);
//            if (isApprovalByDireksiMode()) {
//                if (arrequest.getBiaopDetil().isHeadOfficeFlag() && isCanNavigateRegion()) {
//                    tabs.enable("TAB_BRANCH", true);
//                }
//            }
//        }
    }

//    @Override
//    public void onFormCreate() {
//        initTabs();
//    }
    public DTOList getDocument() throws Exception {
        return arrequest.getDocuments();
    }

    public void setDocument(DTOList document) {
        this.document = document;
    }

    public void doNewDocument() {
        final ARRequestDocumentsView adr = new ARRequestDocumentsView();

        adr.markNew();

        arrequest.getDocuments().add(adr);

    }

    public void doDeleteDocument() {

        arrequest.getDocuments().delete(Integer.parseInt(instIndex));

    }

    public String getInstIndex() {
        return instIndex;
    }

    public void setInstIndex(String instIndex) {
        this.instIndex = instIndex;
    }

    public BigDecimal getKASLimit(String cat, String level) throws Exception {
        final SQLUtil S = new SQLUtil();

        String query = "select max(a.refn1) "
                + "from ff_table a "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' "
                + "and a.ref1 = ? and a.ref2 = ? and a.ref3 = ? "
                + "and date_trunc('day',a.period_start) <= ? "
                + "and date_trunc('day',a.period_end) >= ? ";

        if (level != null) {
            query = query + "and a.ref4 = ? ";
        }

        try {
            S.setQuery(query);

            S.setParam(1, cat);
            S.setParam(2, arrequest.getStCostCenterCode());
            S.setParam(3, arrequest.getStRegionID());
            S.setParam(4, arrequest.getStReceiptClassID());
            S.setParam(5, arrequest.getDtTglRequest());
            S.setParam(6, arrequest.getDtTglRequest());

            if (level != null) {
                S.setParam(7, level);
            }

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getTotalNominalKas(String level) throws Exception {
        final SQLUtil S = new SQLUtil();

        String query = "select sum(a.nominal) "
                + "from ar_request_fee a "
                + "inner join gl_accounts b on b.account_id = a.account_entity_id "
                + "where a.status = 'APP' and a.deleted is null "
                + "and a.rc_id = ? and a.cc_code = ? "
                + "and a.months = ? and a.years = ? ";

        if (level != null) {
            query = query + "and b.acct_level = ? ";
        }

        try {
            S.setQuery(query);

            S.setParam(1, arrequest.getStReceiptClassID());
            S.setParam(2, arrequest.getStCostCenterCode());
            S.setParam(3, arrequest.getStMonths());
            S.setParam(4, arrequest.getStYears());

            if (level != null) {
                S.setParam(5, level);
            }

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
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

    public void onCheckRkap2() throws Exception {

        //DTOList clausules = arrequest.getClausules();
        DTOList clausules = getList();

        BigDecimal usedRkap = null;
        for (int i = 0; i < clausules.size(); i++) {
            FlexTableView d = (FlexTableView) clausules.get(i);

            if (!d.isMarked()) {
                d.setDbReference3(BDUtil.zero);
            } else if (d.isMarked()) {

                if (arrequest.isStatusApproval()) {
                    usedRkap = BDUtil.add(usedRkap, d.getDbReference2());
                    d.setDbReference3(BDUtil.sub(d.getDbReference1(), d.getDbReference2()));
                } else if (arrequest.isStatusCashback()) {
                    usedRkap = arrequest.getDbNominal();
                    d.setDbReference3(BDUtil.add(d.getDbReference1(), d.getDbReference2()));
                }

                if (BDUtil.lesserThanZero(d.getDbReference3())) {
                    throw new RuntimeException("Nilai RKAP untuk Cabang " + d.getStDescriptionNew() + " sudah melebihi limit");
                }
            }
            if (!d.isMarked()) {
                continue;
            }
        }
    }

    public void recalculate() throws Exception {
        onCheckRkap2();
    }

    public void editRequest(String arreqid) throws Exception {
        clickSuperEdit(arreqid);
        if (!arrequest.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (!arrequest.isValidasiFlag()) {
            throw new Exception("Polis Belum Disetujui");
        }
    }

    public void changeRKAP() {

        arrequest.setStAccountIDChoice(null);

    }
    DTOList list;

    public DTOList getList() {
        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void select_rkap() throws Exception {
        list = null;

        if (list == null) {
            list = ListUtil.getDTOListFromQuery(
                    " select a.description as description_new,b.* "
                    + " from s_region a "
                    + " left join ff_table b on b.ref2 = a.region_id::text "
                    + " and b.fft_group_id='RKAP' and b.active_flag = 'Y' and b.ref3 = ? "
                    + " and date_trunc('day',b.period_start) <= ? "
                    + " and date_trunc('day',b.period_end) >= ? order by a.cc_code,a.region_id ",
                    new Object[]{arrequest.getStAccountIDChoice(), arrequest.getDtTglRequest(), arrequest.getDtTglRequest()},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView icl = (FlexTableView) list.get(i);

                if (arrequest.getStARRequestID() != null) {
                    if (arrequest.getStRegionID().equalsIgnoreCase(icl.getStReference2())) {
                        icl.setStAddFlag("Y");

                        if (Tools.compare(icl.getDbReference1(), arrequest.getDbNominal()) >= 0) {
                            if (arrequest.isStatusApproval()) {
                                icl.setDbReference2(arrequest.getDbNominal());
                            } else if (arrequest.isStatusCashback()) {
                                icl.setDbReference2(arrequest.getDbNominalBack());
                            }
                        } else {
                            icl.setDbReference2(icl.getDbReference1());
                        }
                    }
                }
            }
        }
    }

    public void save_rkap() throws Exception {

        for (int i = 0; i < list.size(); i++) {
            FlexTableView fft = (FlexTableView) list.get(i);

            final boolean marked = fft.isMarked();

            if (!marked) {
                continue;
            }

            if (marked) {
                if (fft.getStFFTID() != null) {
                    fft.markUpdate();
                    if (arrequest.isStatusApproval()) {
                        fft.setDbReference4(fft.getDbReference1());
                        fft.setDbReference1(BDUtil.sub(fft.getDbReference1(), fft.getDbReference2()));
                    } else if (arrequest.isStatusCashback()) {
                        fft.setDbReference1(BDUtil.add(fft.getDbReference1(), fft.getDbReference2()));
                    }
                }
            }

            if (!marked && fft.getStFFTID() != null) {
                fft.markDelete();
            }

            fft.setDbReference2(null);
            fft.setDbReference3(null);
            fft.setStAddFlag(null);
        }

        SQLUtil.qstore(list);

        //close();
    }

    /**
     * @return the rePrintMode
     */
    public boolean isRePrintMode() {
        return rePrintMode;
    }

    public void setRePrintMode(boolean rePrintMode) {
        this.rePrintMode = rePrintMode;
    }

    /**
     * @return the stProposalName
     */
    public String getStProposalName() {
        return stProposalName;
    }

    /**
     * @param stProposalName the stProposalName to set
     */
    public void setStProposalName(String stProposalName) {
        this.stProposalName = stProposalName;
    }
    /*
    public void getEdocumentPDF() throws Exception {
    
    File fo = new File("C:/");
    
    String fileFOlder = "/fin-repository";
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    String sf = sdf.format(new Date());
    
    String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
    
    try {
    new File(tempPath).mkdir();
    } catch (Exception e) {
    }
    
    final SQLUtil S = new SQLUtil("MYSQLEDOC");
    
    try {
    S.setQuery("select * "
    + "from file "
    + "where no_permintaan = ? ");
    
    S.setParam(1, arrequest.getStRequestNo());
    
    final ResultSet rs = S.executeQuery();
    
    if (rs.next()) {
    if (arrequest.getStDocuments() == null) {
    
    System.out.println(rs.getInt(1) + "  " + rs.getString("path") + "  " + rs.getString("file_name") + "  " + rs.getString("o_password") + "  " + rs.getString("password_dokumen"));
    String fileURL = "http://192.168.200.19/file_manager_fix/sharing/" + rs.getString("path") + rs.getString("file_name");
    String saveDir = tempPath + File.separator;
    String PDF_OWNER_PASSWORD = rs.getString("o_password");
    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + PDF_OWNER_PASSWORD);
    logger.logDebug("#################### " + Base64.decodeBase64(PDF_OWNER_PASSWORD));
    
    String passDokumen = decodeBase64(PDF_OWNER_PASSWORD);
    System.out.print("\n");
    
    try {
    downloadFile(fileURL, saveDir, passDokumen);
    } catch (IOException ex) {
    ex.printStackTrace();
    }
    
    }
    }
    } finally {
    S.release();
    }
    }
    
    public void downloadFile(String fileURL, String saveDir, String passDokumen)
    throws IOException, Exception {
    URL url = new URL(fileURL);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    int responseCode = httpConn.getResponseCode();
    String fileID = null;
    
    // always check HTTP response code first
    if (responseCode == HttpURLConnection.HTTP_OK) {
    String fileName = "";
    String disposition = httpConn.getHeaderField("Content-Disposition");
    String contentType = httpConn.getContentType();
    int contentLength = httpConn.getContentLength();
    
    
    // Checking whether the URL contains a PDF
    if (!httpConn.getContentType().equalsIgnoreCase("application/pdf")) {
    System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
    } else {
    if (disposition != null) {
    // extracts file name from header field
    int index = disposition.indexOf("filename=");
    if (index > 0) {
    fileName = disposition.substring(index + 10,
    disposition.length() - 1);
    }
    } else {
    // extracts file name from URL
    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
    fileURL.length());
    }
    
    System.out.println("Content-Type = " + contentType);
    System.out.println("Content-Disposition = " + disposition);
    System.out.println("Content-Length = " + contentLength);
    System.out.println("fileName = " + fileName);
    
    // opens input stream from the HTTP connection
    InputStream inputStream = httpConn.getInputStream();
    String saveFilePath = saveDir + fileName;
    String saveFilePathEncrypt = saveDir + "encrypt_" + fileName;
    
    FileOutputStream outputStream = new FileOutputStream(saveFilePath);
    
    int bytesRead = -1;
    byte[] buffer = new byte[BUFFER_SIZE];
    while ((bytesRead = inputStream.read(buffer)) != -1) {
    outputStream.write(buffer, 0, bytesRead);
    }
    
    outputStream.close();
    inputStream.close();
    
    System.out.println("File downloaded");
    
    PDDocument document = null;
    
    try {
    document = PDDocument.load(saveFilePath);
    System.out.print(saveFilePath + "\n");
    
    if (document.isEncrypted()) {
    System.out.print(document.isEncrypted() + "\n");
    DecryptionMaterial decryptionMaterial = null;
    
    decryptionMaterial = new StandardDecryptionMaterial(passDokumen);
    document.openProtection(decryptionMaterial);
    AccessPermission ap = document.getCurrentAccessPermission();
    System.out.print(ap.isOwnerPermission() + "\n");
    if (ap.isOwnerPermission()) {
    document.setAllSecurityToBeRemoved(true);
    document.save(saveFilePathEncrypt);
    
    } else {
    throw new IOException(
    "Error: You are only allowed to decrypt a document with the owner password.");
    }
    } else {
    System.err.println("Error: Document is not encrypted.");
    }
    
    File fo = new File(saveFilePathEncrypt);
    
    final SQLUtil S = new SQLUtil();
    
    try {
    FileView fv = new FileView();
    
    fv.markNew();
    
    fv.setStFileID(String.valueOf(IDFactory.createNumericID("FILE")));
    fv.setStOriginalName(fileName);
    fv.setStFilePath(fo.getCanonicalPath());
    fv.setDtFileDate(new Date(fo.lastModified()));
    fv.setStFileType("PDF");
    fv.setStMimeType(contentType);
    fv.setDbOriginalSize(new BigDecimal(fo.length()));
    fv.setStDescription("E-Document");
    fv.setStImageFlag("N");
    
    fv.determineFileType();
    
    S.store(fv);
    
    fileID = fv.getStFileID();
    
    } finally {
    S.release();
    }
    } finally {
    if (document != null) {
    document.close();
    }
    }
    }
    } else {
    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
    }
    httpConn.disconnect();
    
    arrequest.setStDocuments(fileID);
    
    refresh();
    }
    
    public String decodeBase64(String base64String) throws Exception {
    
    byte[] decoded = Base64.decodeBase64(base64String);
    System.out.println("Base 64 Decoded  String : " + new String(decoded));
    
    return new String(decoded);
    
    }
     */
//    public void onProposalAmount() throws Exception {
//
//        DTOList details = getRemoteGeneralLedger().getInsuranceProposalComm(arrequest.getStNoSuratHutang());
//
//        BigDecimal totalAmount = null;
//
//        for (int i = 0; i < details.size(); i++) {
//            uploadProposalCommView object = (uploadProposalCommView) details.get(i);
//
//            totalAmount = BDUtil.add(totalAmount, object.getDbAmount());
//
//        }
//
//        for (int i = 0; i < details.size(); i++) {
//            uploadProposalCommView object = (uploadProposalCommView) details.get(i);
//
//            object.setDbAmountTotal(totalAmount);
//
//        }
//
//        arrequest.setDbNominal(totalAmount);
//        arrequest.setStDescription("Bayar Komisi " + details.size() + " Polis");
//    }
    private String notesindex;

    public String getNotesindex() {
        return notesindex;
    }

    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public void onNewObj() throws Exception {
        final ARRequestFeeObj bng = new ARRequestFeeObj();

        bng.markNew();

        getReqObject().add(bng);

        String n = String.valueOf(getReqObject().size());
    }

    public void onDeleteObj() throws Exception {
        getReqObject().delete(Integer.parseInt(notesindex));

        String n = String.valueOf(getReqObject().size());
    }

    public DTOList getReqObject() throws Exception {
        return arrequest.getReqObject();
    }

    public BigDecimal getCBBalance(String arreqid) throws Exception {
        final SQLUtil S = new SQLUtil();

        String query = "select sum(coalesce(b.nominal_realisasi,0)) as used "
                + "from ar_request_fee a "
                + "left join ar_request_fee_obj b on b.req_id = a.req_id and b.delete_flag is null "
                + "where cashier_flag = 'Y' and a.request_no like '" + arreqid + "%' ";

        try {
            S.setQuery(query);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void recalculatedetOLD() throws Exception {

        BigDecimal totalNominalEstimated = null;
        BigDecimal totalNominalRealized = null;

        DTOList details = arrequest.getReqObject();
        for (int i = 0; i < details.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) details.get(i);

            object.setDbNominal(BDUtil.mul(new BigDecimal(object.getStQuantity()), object.getDbHargaSatuan()));

//            if (object.getStKwitansi() == null) {
//                throw new RuntimeException("File Lampiran Kwitansi Pada Kolom Keterangan '" + object.getStDescription().toUpperCase() + "' akun '" + object.getStAccountNo() + "', Wajib Dilampirkan");
//            }

            totalNominalEstimated = BDUtil.add(totalNominalEstimated, object.getDbNominal());
            totalNominalRealized = BDUtil.add(totalNominalRealized, object.getDbNominalRealisasi());

        }
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@@@ estimated : " + totalNominalEstimated);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@@@ realized : " + totalNominalRealized);
        arrequest.setDbNominal(totalNominalEstimated);
        arrequest.setDbNominalUsed(totalNominalRealized);

        if (arrequest.isStatusCashback()) {
            BigDecimal kasBalance = getCBBalance(arrequest.getStRequestNo().substring(0, 14));
            logger.logDebug("@@@@@@@@@@@@@@@@@@@ : kasBalance " + kasBalance);

            kasBalance = BDUtil.add(kasBalance, arrequest.getDbNominalUsed());
            logger.logDebug("@@@@@@@@@@@@@@@@@@@ : total " + kasBalance);
            logger.logDebug("@@@@@@@@@@@@@@@@@@@ : " + arrequest.getDbNominal());

            if (Tools.compare(kasBalance, arrequest.getDbNominal()) > 0) {
                String warning = "Realisasi anggaran melebihi uang muka <br> (" + JSPUtil.print(arrequest.getDbNominal(), 2) + " < " + JSPUtil.print(kasBalance, 2) + ") <br>"
                        + " sebesar " + JSPUtil.print(BDUtil.sub(kasBalance, arrequest.getDbNominal()), 2);
                throw new RuntimeException(warning);
            }

            BigDecimal nominalBack = BDUtil.sub(arrequest.getDbNominal(), arrequest.getDbNominalUsed());
            logger.logDebug("@@@@@@@@@@@@@@@@@@@ : nominalBack " + nominalBack);

            arrequest.setDbNominalBack(nominalBack);
        }

        if (arrequest.isStatusRefund()) {
            BigDecimal nominalBack = getNominalBack(arrequest.getStRootID());
            logger.logDebug("@@@@@@@@@@@@@@@@@@@ : yuhuhh " + nominalBack);

            arrequest.setDbNominalBack(nominalBack);
        }

        if (arrequest.isStatusCashback()
                || arrequest.isStatusRefund()) {
            checkValid();

            if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
                checkLimitRKAP();
            }
        }
    }

    public void checkValid() throws Exception {
        String pilihan = null;
        if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
            pilihan = "AKTIVA";
        } else {
            pilihan = "BIAYA OPERASIONAL";
        }

        DateTime requestDate = new DateTime(arrequest.getDtTglRequest());

        DTOList details = arrequest.getReqObject();

        for (int i = 0; i < details.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) details.get(i);

            if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
                if (!Tools.isYes(object.getBiaopDetil().getStHeadOfficeFlag())) {
                    throw new RuntimeException("Pilihan '" + pilihan + "' tidak bisa untuk Detil Operasional '" + object.getBiaopDetil().getStDescription().toUpperCase() + "'. <br><br>"
                            + "Pilih BIAYA OPERASIONAL.");
                }

                if (Tools.compare(object.getDbNominal(), new BigDecimal(500000)) < 0) {
                    throw new RuntimeException("Jumlah Biaya harus diatas Rp. 500.000,00");
                }

                DateTime realisasiDate = new DateTime(object.getDtTglCashback());
                if (realisasiDate.isBefore(requestDate)) {
                    throw new RuntimeException("Tanggal Realisasi untuk Detil Operasional '" + object.getBiaopDetil().getStDescription().toUpperCase() + "', harus diatas Tanggal Permintaan");
                }
            } else {
                if (Tools.isYes(object.getBiaopDetil().getStHeadOfficeFlag())) {
                    throw new RuntimeException("Pilihan '" + pilihan + "' tidak bisa untuk Detil Operasional '" + object.getBiaopDetil().getStDescription().toUpperCase() + "'. <br><br>"
                            + "Pilih AKTIVA/INVENTARIS KANTOR.");
                }

                if (Tools.compare(object.getDbNominal(), new BigDecimal(500000)) > 0) {
                    throw new RuntimeException("Untuk Pilihan '" + pilihan + "', Jumlah Biaya '" + object.getBiaopDetil().getStDescription().toUpperCase() + "' melebihi limit per item (maks. Rp. 500.000,00)");
                }
            }
        }
    }

    public void checkLimitRKAP() throws Exception {

        BigDecimal sumRKAP89 = new BigDecimal(0);
        BigDecimal sumRKAP90 = new BigDecimal(0);
        BigDecimal sumRKAP91 = new BigDecimal(0);
        BigDecimal sumRKAP92 = new BigDecimal(0);
        BigDecimal sumRKAP93 = new BigDecimal(0);
        BigDecimal sumRKAP94 = new BigDecimal(0);
        BigDecimal sumRKAP95 = new BigDecimal(0);
        BigDecimal sumRKAP96 = new BigDecimal(0);
        BigDecimal sumRKAP97 = new BigDecimal(0);
        BigDecimal sumRKAP98 = new BigDecimal(0);
        BigDecimal sumRKAP99 = new BigDecimal(0);
        BigDecimal sumRKAP100 = new BigDecimal(0);
        BigDecimal sumRKAP101 = new BigDecimal(0);
        BigDecimal sumRKAP102 = new BigDecimal(0);
        BigDecimal sumRKAP103 = new BigDecimal(0);
        BigDecimal sumRKAP104 = new BigDecimal(0);
        BigDecimal sumRKAP105 = new BigDecimal(0);
        BigDecimal sumRKAP106 = new BigDecimal(0);
        BigDecimal sumRKAP107 = new BigDecimal(0);
        BigDecimal sumRKAP108 = new BigDecimal(0);
        BigDecimal sumRKAP109 = new BigDecimal(0);
        BigDecimal sumRKAP110 = new BigDecimal(0);
        BigDecimal sumRKAP111 = new BigDecimal(0);
        BigDecimal sumRKAP112 = new BigDecimal(0);
        BigDecimal sumRKAP113 = new BigDecimal(0);
        BigDecimal sumRKAP114 = new BigDecimal(0);
        BigDecimal sumRKAP115 = new BigDecimal(0);
        BigDecimal sumRKAP116 = new BigDecimal(0);
        BigDecimal sumRKAP117 = new BigDecimal(0);
        BigDecimal sumRKAP118 = new BigDecimal(0);
        BigDecimal sumRKAP119 = new BigDecimal(0);
        BigDecimal sumRKAP120 = new BigDecimal(0);
        BigDecimal sumRKAP121 = new BigDecimal(0);
        BigDecimal sumRKAP122 = new BigDecimal(0);
        BigDecimal sumRKAP123 = new BigDecimal(0);
        BigDecimal sumRKAP124 = new BigDecimal(0);
        BigDecimal sumRKAP125 = new BigDecimal(0);
        BigDecimal sumRKAP126 = new BigDecimal(0);
        BigDecimal sumRKAP127 = new BigDecimal(0);
        BigDecimal sumRKAP128 = new BigDecimal(0);
        BigDecimal sumRKAP129 = new BigDecimal(0);
        BigDecimal sumRKAP130 = new BigDecimal(0);
        BigDecimal sumRKAP131 = new BigDecimal(0);
        BigDecimal sumRKAP132 = new BigDecimal(0);
        BigDecimal sumRKAP133 = new BigDecimal(0);
        BigDecimal sumRKAP134 = new BigDecimal(0);
        BigDecimal sumRKAP135 = new BigDecimal(0);
        BigDecimal sumRKAP136 = new BigDecimal(0);
        BigDecimal sumRKAP137 = new BigDecimal(0);
        BigDecimal sumRKAP138 = new BigDecimal(0);
        BigDecimal sumRKAP139 = new BigDecimal(0);
        BigDecimal sumRKAP140 = new BigDecimal(0);
        BigDecimal sumRKAP141 = new BigDecimal(0);
        BigDecimal sumRKAP142 = new BigDecimal(0);
        BigDecimal sumRKAP143 = new BigDecimal(0);
        BigDecimal sumRKAP144 = new BigDecimal(0);
        BigDecimal sumRKAP145 = new BigDecimal(0);
        BigDecimal sumRKAP146 = new BigDecimal(0);
        BigDecimal sumRKAP147 = new BigDecimal(0);
        BigDecimal sumRKAP148 = new BigDecimal(0);
        BigDecimal sumRKAP149 = new BigDecimal(0);
        BigDecimal sumRKAP150 = new BigDecimal(0);
        BigDecimal sumRKAP151 = new BigDecimal(0);
        BigDecimal sumRKAP152 = new BigDecimal(0);
        BigDecimal sumRKAP153 = new BigDecimal(0);
        BigDecimal sumRKAP154 = new BigDecimal(0);
        BigDecimal sumRKAP155 = new BigDecimal(0);
        BigDecimal sumRKAP156 = new BigDecimal(0);
        BigDecimal sumRKAP157 = new BigDecimal(0);
        BigDecimal sumRKAP158 = new BigDecimal(0);
        BigDecimal sumRKAP159 = new BigDecimal(0);
        BigDecimal sumRKAP160 = new BigDecimal(0);
        BigDecimal sumRKAP161 = new BigDecimal(0);
        BigDecimal sumRKAP162 = new BigDecimal(0);
        BigDecimal sumRKAP163 = new BigDecimal(0);
        BigDecimal sumRKAP164 = new BigDecimal(0);
        BigDecimal sumRKAP165 = new BigDecimal(0);
        BigDecimal sumRKAP166 = new BigDecimal(0);
        BigDecimal sumRKAP167 = new BigDecimal(0);
        BigDecimal sumRKAP168 = new BigDecimal(0);
        BigDecimal sumRKAP169 = new BigDecimal(0);
        BigDecimal sumRKAP170 = new BigDecimal(0);
        BigDecimal sumRKAP171 = new BigDecimal(0);
        BigDecimal sumRKAP172 = new BigDecimal(0);
        BigDecimal sumRKAP173 = new BigDecimal(0);
        BigDecimal sumRKAP174 = new BigDecimal(0);
        BigDecimal sumRKAP175 = new BigDecimal(0);
        BigDecimal sumRKAP176 = new BigDecimal(0);
        BigDecimal sumRKAP177 = new BigDecimal(0);
        BigDecimal sumRKAP178 = new BigDecimal(0);
        BigDecimal sumRKAP179 = new BigDecimal(0);
        BigDecimal sumRKAP180 = new BigDecimal(0);
        BigDecimal sumRKAP181 = new BigDecimal(0);
        BigDecimal sumRKAP182 = new BigDecimal(0);
        BigDecimal sumRKAP183 = new BigDecimal(0);
        BigDecimal sumRKAP184 = new BigDecimal(0);
        BigDecimal sumRKAP185 = new BigDecimal(0);
        BigDecimal sumRKAP186 = new BigDecimal(0);
        BigDecimal sumRKAP187 = new BigDecimal(0);
        BigDecimal sumRKAP188 = new BigDecimal(0);
        BigDecimal sumRKAP189 = new BigDecimal(0);
        BigDecimal sumRKAP190 = new BigDecimal(0);
        BigDecimal sumRKAP191 = new BigDecimal(0);
        BigDecimal sumRKAP295 = new BigDecimal(0);
        BigDecimal sumRKAP297 = new BigDecimal(0);

        BigDecimal totalSumRKAP = new BigDecimal(0);
        BigDecimal sumRKAPperDetilYears = new BigDecimal(0);
        BigDecimal RKAPLimit = new BigDecimal(0);

        DTOList details = arrequest.getReqObject();
        for (int i = 0; i < details.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) details.get(i);

            if (object.getStBiaopTypeID().equalsIgnoreCase("89")) {
                sumRKAP89 = BDUtil.add(sumRKAP89, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("90")) {
                sumRKAP90 = BDUtil.add(sumRKAP90, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("91")) {
                sumRKAP91 = BDUtil.add(sumRKAP91, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("92")) {
                sumRKAP92 = BDUtil.add(sumRKAP92, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("93")) {
                sumRKAP93 = BDUtil.add(sumRKAP93, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("94")) {
                sumRKAP94 = BDUtil.add(sumRKAP94, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("95")) {
                sumRKAP95 = BDUtil.add(sumRKAP95, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("96")) {
                sumRKAP96 = BDUtil.add(sumRKAP96, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("97")) {
                sumRKAP97 = BDUtil.add(sumRKAP97, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("98")) {
                sumRKAP98 = BDUtil.add(sumRKAP98, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("99")) {
                sumRKAP99 = BDUtil.add(sumRKAP99, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("100")) {
                sumRKAP100 = BDUtil.add(sumRKAP100, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("101")) {
                sumRKAP101 = BDUtil.add(sumRKAP101, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("102")) {
                sumRKAP102 = BDUtil.add(sumRKAP102, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("103")) {
                sumRKAP103 = BDUtil.add(sumRKAP103, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("104")) {
                sumRKAP104 = BDUtil.add(sumRKAP104, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("105")) {
                sumRKAP105 = BDUtil.add(sumRKAP105, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("106")) {
                sumRKAP106 = BDUtil.add(sumRKAP106, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("107")) {
                sumRKAP107 = BDUtil.add(sumRKAP107, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("108")) {
                sumRKAP108 = BDUtil.add(sumRKAP108, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("109")) {
                sumRKAP109 = BDUtil.add(sumRKAP109, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("110")) {
                sumRKAP110 = BDUtil.add(sumRKAP110, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("111")) {
                sumRKAP111 = BDUtil.add(sumRKAP111, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("112")) {
                sumRKAP112 = BDUtil.add(sumRKAP112, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("113")) {
                sumRKAP113 = BDUtil.add(sumRKAP113, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("114")) {
                sumRKAP114 = BDUtil.add(sumRKAP114, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("115")) {
                sumRKAP115 = BDUtil.add(sumRKAP115, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("116")) {
                sumRKAP116 = BDUtil.add(sumRKAP116, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("117")) {
                sumRKAP117 = BDUtil.add(sumRKAP117, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("118")) {
                sumRKAP118 = BDUtil.add(sumRKAP118, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("119")) {
                sumRKAP119 = BDUtil.add(sumRKAP119, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("120")) {
                sumRKAP120 = BDUtil.add(sumRKAP120, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("121")) {
                sumRKAP121 = BDUtil.add(sumRKAP121, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("122")) {
                sumRKAP122 = BDUtil.add(sumRKAP122, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("123")) {
                sumRKAP123 = BDUtil.add(sumRKAP123, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("124")) {
                sumRKAP124 = BDUtil.add(sumRKAP124, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("125")) {
                sumRKAP125 = BDUtil.add(sumRKAP125, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("126")) {
                sumRKAP126 = BDUtil.add(sumRKAP126, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("127")) {
                sumRKAP127 = BDUtil.add(sumRKAP127, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("128")) {
                sumRKAP128 = BDUtil.add(sumRKAP128, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("129")) {
                sumRKAP129 = BDUtil.add(sumRKAP129, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("130")) {
                sumRKAP130 = BDUtil.add(sumRKAP130, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("131")) {
                sumRKAP131 = BDUtil.add(sumRKAP131, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("132")) {
                sumRKAP132 = BDUtil.add(sumRKAP132, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("133")) {
                sumRKAP133 = BDUtil.add(sumRKAP133, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("134")) {
                sumRKAP134 = BDUtil.add(sumRKAP134, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("135")) {
                sumRKAP135 = BDUtil.add(sumRKAP135, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("136")) {
                sumRKAP136 = BDUtil.add(sumRKAP136, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("137")) {
                sumRKAP137 = BDUtil.add(sumRKAP137, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("138")) {
                sumRKAP138 = BDUtil.add(sumRKAP138, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("139")) {
                sumRKAP139 = BDUtil.add(sumRKAP139, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("140")) {
                sumRKAP140 = BDUtil.add(sumRKAP140, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("141")) {
                sumRKAP141 = BDUtil.add(sumRKAP141, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("142")) {
                sumRKAP142 = BDUtil.add(sumRKAP142, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("143")) {
                sumRKAP143 = BDUtil.add(sumRKAP143, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("144")) {
                sumRKAP144 = BDUtil.add(sumRKAP144, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("145")) {
                sumRKAP145 = BDUtil.add(sumRKAP145, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("146")) {
                sumRKAP146 = BDUtil.add(sumRKAP146, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("147")) {
                sumRKAP147 = BDUtil.add(sumRKAP147, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("148")) {
                sumRKAP148 = BDUtil.add(sumRKAP148, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("149")) {
                sumRKAP149 = BDUtil.add(sumRKAP149, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("150")) {
                sumRKAP150 = BDUtil.add(sumRKAP150, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("151")) {
                sumRKAP151 = BDUtil.add(sumRKAP151, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("152")) {
                sumRKAP152 = BDUtil.add(sumRKAP152, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("153")) {
                sumRKAP153 = BDUtil.add(sumRKAP153, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("154")) {
                sumRKAP154 = BDUtil.add(sumRKAP154, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("155")) {
                sumRKAP155 = BDUtil.add(sumRKAP155, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("156")) {
                sumRKAP156 = BDUtil.add(sumRKAP156, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("157")) {
                sumRKAP157 = BDUtil.add(sumRKAP157, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("158")) {
                sumRKAP158 = BDUtil.add(sumRKAP158, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("159")) {
                sumRKAP159 = BDUtil.add(sumRKAP159, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("160")) {
                sumRKAP160 = BDUtil.add(sumRKAP160, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("161")) {
                sumRKAP161 = BDUtil.add(sumRKAP161, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("162")) {
                sumRKAP162 = BDUtil.add(sumRKAP162, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("163")) {
                sumRKAP163 = BDUtil.add(sumRKAP163, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("164")) {
                sumRKAP164 = BDUtil.add(sumRKAP164, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("165")) {
                sumRKAP165 = BDUtil.add(sumRKAP165, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("166")) {
                sumRKAP166 = BDUtil.add(sumRKAP166, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("167")) {
                sumRKAP167 = BDUtil.add(sumRKAP167, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("168")) {
                sumRKAP168 = BDUtil.add(sumRKAP168, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("169")) {
                sumRKAP169 = BDUtil.add(sumRKAP169, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("170")) {
                sumRKAP170 = BDUtil.add(sumRKAP170, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("171")) {
                sumRKAP171 = BDUtil.add(sumRKAP171, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("172")) {
                sumRKAP172 = BDUtil.add(sumRKAP172, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("173")) {
                sumRKAP173 = BDUtil.add(sumRKAP173, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("174")) {
                sumRKAP174 = BDUtil.add(sumRKAP174, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("175")) {
                sumRKAP175 = BDUtil.add(sumRKAP175, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("176")) {
                sumRKAP176 = BDUtil.add(sumRKAP176, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("177")) {
                sumRKAP177 = BDUtil.add(sumRKAP177, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("178")) {
                sumRKAP178 = BDUtil.add(sumRKAP178, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("179")) {
                sumRKAP179 = BDUtil.add(sumRKAP179, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("180")) {
                sumRKAP180 = BDUtil.add(sumRKAP180, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("181")) {
                sumRKAP181 = BDUtil.add(sumRKAP181, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("182")) {
                sumRKAP182 = BDUtil.add(sumRKAP182, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("183")) {
                sumRKAP183 = BDUtil.add(sumRKAP183, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("184")) {
                sumRKAP184 = BDUtil.add(sumRKAP184, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("185")) {
                sumRKAP185 = BDUtil.add(sumRKAP185, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("186")) {
                sumRKAP186 = BDUtil.add(sumRKAP186, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("187")) {
                sumRKAP187 = BDUtil.add(sumRKAP187, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("188")) {
                sumRKAP188 = BDUtil.add(sumRKAP188, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("189")) {
                sumRKAP189 = BDUtil.add(sumRKAP189, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("190")) {
                sumRKAP190 = BDUtil.add(sumRKAP190, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("191")) {
                sumRKAP191 = BDUtil.add(sumRKAP191, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("295")) {
                sumRKAP295 = BDUtil.add(sumRKAP295, object.getDbNominalRealisasi());
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("297")) {
                sumRKAP297 = BDUtil.add(sumRKAP297, object.getDbNominalRealisasi());
            }

            sumRKAPperDetilYears = getSumReqObjYears(object.getStBiaopTypeID());
            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ sumRKAPperDetilYears: " + sumRKAPperDetilYears);
            RKAPLimit = getRKAPLimit(object.getStBiaopTypeID());
            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ RKAPLimit: " + RKAPLimit);

            if (object.getStBiaopTypeID().equalsIgnoreCase("89")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP89);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("90")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP90);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("91")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP91);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("92")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP92);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("93")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP93);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("94")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP94);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("95")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP95);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("96")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP96);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("97")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP97);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("98")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP98);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("99")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP99);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("100")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP100);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("101")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP101);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("102")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP102);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("103")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP103);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("104")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP104);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("105")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP105);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("106")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP106);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("107")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP107);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("108")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP108);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("109")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP109);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("110")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP110);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("111")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP111);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("112")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP112);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("113")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP113);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("114")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP114);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("115")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP115);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("116")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP116);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("117")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP117);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("118")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP118);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("119")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP119);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("120")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP120);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("121")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP121);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("122")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP122);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("123")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP123);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("124")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP124);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("125")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP125);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("126")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP126);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("127")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP127);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("128")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP128);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("129")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP129);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("130")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP130);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("131")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP131);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("132")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP132);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("133")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP133);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("134")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP134);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("135")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP135);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("136")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP136);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("137")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP137);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("138")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP138);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("139")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP139);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("140")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP140);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("141")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP141);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("142")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP142);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("143")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP143);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("144")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP144);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("145")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP145);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("146")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP146);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("147")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP147);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("148")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP148);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("149")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP149);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("150")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP150);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("151")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP151);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("152")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP152);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("153")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP153);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("154")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP154);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("155")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP155);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("156")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP156);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("157")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP157);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("158")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP158);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("159")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP159);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("160")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP160);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("161")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP161);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("162")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP162);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("163")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP163);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("164")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP164);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("165")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP165);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("166")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP166);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("167")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP167);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("168")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP168);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("169")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP169);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("170")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP170);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("171")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP171);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("172")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP172);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("173")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP173);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("174")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP174);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("175")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP175);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("176")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP176);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("177")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP177);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("178")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP178);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("179")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP179);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("180")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP180);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("181")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP181);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("182")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP182);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("183")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP183);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("184")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP184);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("185")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP185);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("186")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP186);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("187")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP187);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("188")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP188);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("189")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP189);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("190")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP190);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("191")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP191);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("295")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP295);
            }
            if (object.getStBiaopTypeID().equalsIgnoreCase("297")) {
                totalSumRKAP = BDUtil.add(sumRKAPperDetilYears, sumRKAP297);
            }

            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ totalSumRKAP: " + totalSumRKAP);
            String warningRKAP = "Total nilai RKAP untuk Detil " + object.getBiaopDetil().getStDescription().toUpperCase() + " (" + JSPUtil.print(totalSumRKAP, 2) + ") melebihi limit (" + JSPUtil.print(RKAPLimit, 2) + ")";

            boolean enoughLimit = false;
            enoughLimit = Tools.compare(RKAPLimit, totalSumRKAP) >= 0;

            if (!enoughLimit) {
                throw new RuntimeException(warningRKAP);
            }
        }
    }

    public BigDecimal getSumReqObjYears(String biaoptypeid) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery("select sum(b.nominal) as nominal "
                    + "from ar_request_fee a "
                    + "inner join ar_request_fee_obj b on b.req_id = a.req_id "
                    + "where a.act_flag = 'Y' and a.eff_flag = 'Y' and a.cashier_flag = 'Y' and a.status = 'CSB' "
                    + "and b.biaoptypeid = ? and a.cc_code = ? and a.region_id = ? and a.years = ? ");

            S.setParam(1, biaoptypeid);
            S.setParam(2, arrequest.getStCostCenterCode());
            S.setParam(3, arrequest.getStRegionID());
            S.setParam(4, arrequest.getStYears());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getSumReqObj(String biaoptypeid) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery("select sum(b.nominal) as nominal "
                    + "from ar_request_fee_obj b "
                    + "where b.biaoptypeid = ? ");
            S.setParam(1, biaoptypeid);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void checkLampiranAktiva() throws Exception {

        DTOList documents = arrequest.getDocuments();

        if (documents.size() < 1) {
            throw new RuntimeException("File Lampiran Persetujuan Surat/Memo Harus Dilampirkan");
        }

        for (int i = 0; i < documents.size(); i++) {
            ARRequestDocumentsView doc = (ARRequestDocumentsView) documents.get(i);

            if (doc.getStFilePhysic() == null) {
                throw new RuntimeException("Upload File Lampiran");
            }

        }
    }

    public void saveToInventaris(ARRequestFee arrequest, DTOList request) throws Exception {
        URL url = new URL("http://192.168.200.242/InventarisWs/service/doSaveDataInventaris");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        JSONArray jarray = new JSONArray();
        for (int i = 0; i < request.size(); i++) {
            ARRequestFeeObj reqObj = (ARRequestFeeObj) request.get(i);

            int quantity = Integer.parseInt(reqObj.getStQuantity());
            for (int j = 0; j < quantity; j++) {
                JSONObject job = new JSONObject();
//                ARRequestFeeObj reqObj = (ARRequestFeeObj) request.get(j);

                job.put("req_obj_id", reqObj.getStARRequestObjID());
                job.put("accountid", reqObj.getStAccountID());
                job.put("accountno", reqObj.getStAccountNo());
                job.put("biaoptypeid", reqObj.getStBiaopTypeID());
                job.put("description", reqObj.getStDescription());
                job.put("nominal", reqObj.getDbNominalRealisasi().toString());
                job.put("req_id", reqObj.getStARRequestID());
                job.put("cashback_date", DateUtil.getDateStr(reqObj.getDtTglCashback(), "ddMMyyyy"));
//            job.put("quantity", reqObj.getStQuantity());
                job.put("quantity", "1");
                job.put("satuanid", reqObj.getStSatuanID());
                job.put("harga_satuan", reqObj.getDbHargaSatuan().toString());

                job.put("cc_code", arrequest.getStCostCenterCode());
                job.put("region_id", arrequest.getStRegionID());
                job.put("trans_no", arrequest.getStTransactionNo());
                jarray.add(job);

            }
        }

        JSONObject job = new JSONObject();
        job.put("inventaris", jarray);

        OutputStream os = conn.getOutputStream();
        os.write(job.toString().getBytes());
        os.flush();

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        JSONParser parser = new JSONParser();
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        Object obj = parser.parse(br);
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println(jsonObject.get("status"));

    }

    public void editCreateRefund(String arreqid) throws Exception {

        if (arreqid == null) {

            clickCreateNew();

            arrequest.setStStatus("RFD");

            return;
        }

        clickSuperEdit(arreqid);

        if (arrequest.isStatusRequest() || arrequest.isStatusApproval() || arrequest.isStatusRefund()) {
            throw new RuntimeException("Anggaran Harus Status CASHBACK");
        }

        if (!arrequest.isCashierFlag()) {
            throw new RuntimeException("Persetujuan belum di-Approve kasir");
        }

//        DateTime currentDateLastDay = new DateTime(arrequest.getDtCashierDate());
//
//        Date maximumBackDate = currentDateLastDay.plusDays(7).toDate();
//
//        boolean compare = Tools.compare(new Date(), maximumBackDate) > 0;
//        if (compare) {
//            throw new RuntimeException("Pengeluaran uang muka kas kecil max 7 hari kerja kwitansi harus disampaikan ke kasir");
//        }

        checkActiveEffective();

        arrequest.generateEndorseNo();
        arrequest.setStNextStatus("RFD");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStApprovedWho(null);
        arrequest.setDtApprovedDate(null);
        arrequest.setStCashierWho(null);
        arrequest.setDtCashierDate(null);
        arrequest.setStPrintFlag(null);
        arrequest.setStTransactionNo(null);
        arrequest.setDbNominalUsed(null);

        arrequest.checkRealisasiNoBefore(arrequest.getStRequestNo());

        approvalMode = false;

        arrequest.markUpdate();
        getReqObject().deleteAll();

        setTitle("PENGEMBALIAN BIAYA");

        recalculatedet();

//        initTabsNew();
    }

    public BigDecimal getNominalBack(String arreqid) throws Exception {
        final SQLUtil S = new SQLUtil();

        String query = "select sum(a.nominal-a.nominal_used) as back "
                + "from ar_request_fee a "
                + "where a.status = 'CSB' and a.cashier_flag = 'Y' "
                + "and a.root_id = " + arreqid;

        try {
            S.setQuery(query);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void doValidasi() throws Exception {

        if (isCashflowMode()) {

            if (arrequest.getDtCashflowStart() == null) {
                throw new RuntimeException("Tanggal Cashflow belum diinput");
            }

//            arrequest.setStCashflowF("Y");
            arrequest.setStCashflowWho(SessionManager.getInstance().getSession().getStUserID());
            arrequest.setDtCashflowDate(new Date());
        }

        if (isValFinanceMode()) {

            if (arrequest.getStAccountID() == null) {
                throw new RuntimeException("Bank belum diinput!!");
            }

//            arrequest.setStCashierFlag("Y");
            arrequest.setStCashierWho(SessionManager.getInstance().getSession().getStUserID());
            arrequest.setDtCashierDate(new Date());
        }
        approvalMode = true;

        arrequest.markUpdate();

        //clickSave();
//        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode, null);
        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), approvalMode);
//        getRemoteGeneralLedger().saveRequest(arrequest, approvalMode);

        close();
    }

    public BigDecimal getRKAPLimit(String biaoptype) throws Exception {

        /*
         * select f.approve_pusat, sum(f.jumlah_pusat) as total, g.no_account,g.deskripsi
        from kebijakan_perusahaan a
        join identifikasi_masalah b on (a.id=b.id_kebijakan)
        join cabang c on (b.kode_cabang=c.kode_cabang)
        join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah)
        join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja)
        join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran)
        join gl_chart g on (f.no_account=g.no_account)
        where b.tahun=2020 and c.kode_cabang=53 and f.no_account LIKE '822%'
         */
        String sql = null;
        String account[] = getBiaopDetail(biaoptype).getStAccount().split("[\\|]");
        if (account.length == 1) {
            sql = " and f.no_account like '" + account[0] + "%' ";
        } else if (account.length > 1) {
            sql = " and (f.no_account like '" + account[0] + "%' ";
            for (int k = 1; k < account.length; k++) {
                sql = sql + " or f.no_account like = '" + account[k] + "%'";
            }
            sql = sql + ") ";
        }

        final SQLUtil S = new SQLUtil("RKAPDB");

        try {
            S.setQuery(
                    "select sum(f.jumlah_pusat) "
                    + "from kebijakan_perusahaan a "
                    + "     join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                    + "     join cabang c on (b.kode_cabang=c.kode_cabang) "
                    + "     join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                    + "     join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja) "
                    + "     join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran) "
                    + "     join gl_chart g on (f.no_account=g.no_account) "
                    + "     where b.tahun=? and c.kode_cabang=? " + sql);

            S.setParam(1, arrequest.getStYears());
            S.setParam(2, getKodaRkap(arrequest.getStRegionID()));

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BiayaOperasionalDetail getBiaopDetail(String biaoptype) {
        final BiayaOperasionalDetail polType = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, biaoptype);

        return polType;
    }

    public String getKodaRkap(String hcisid) throws Exception {
        final SQLUtil S = new SQLUtil();

        String query = "select rkap_id from s_division where hcis_id = ? ";

        try {
            S.setQuery(query);
            S.setParam(1, hcisid);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void recalculatedet() throws Exception {

        BigDecimal totalNominalRealized = null;
        BigDecimal totalKertasKerja = null;
        BigDecimal getAnggaranInduk = null;

        DTOList details = arrequest.getReqObject();
        for (int i = 0; i < details.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) details.get(i);

            object.setDbNominal(getAnggaranRKAP(object.getStAccountID()));

            if (isRealisasiMode()) {
                object.setDbTotalNilai(BDUtil.mul(new BigDecimal(object.getStQuantity()), object.getDbNominalRealisasi()));

                if (object.getStKwitansi() == null) {
                    throw new RuntimeException("Kwitansi Realisasi '" + object.getStDescription() + "' belum di-upload!");
                }

                if (Tools.compare(object.getDtTglInvoice(), arrequest.getDtTglRequest()) < 0) {
                    throw new Exception("Tanggal Invoice pada Realisasi '" + object.getStDescription() + "' harus melebihi Tanggal Pengajuan");
                }

                if (Tools.compare(object.getDtTglInvoice(), arrequest.getDtCashflowStart()) < 0) {
                    throw new Exception("Tanggal Invoice pada Realisasi '" + object.getStDescription() + "' <br> diluar Periode Cashflow <br> "
                            + " (" + JSPUtil.print(arrequest.getDtCashflowStart()) + " sd " + JSPUtil.print(arrequest.getDtCashflowEnd()) + ") ");
                }

                if (Tools.compare(object.getDtTglInvoice(), arrequest.getDtCashflowEnd()) > 0) {
                    throw new Exception("Tanggal Invoice pada Realisasi '" + object.getStDescription() + "' <br> diluar Periode Cashflow <br> "
                            + " (" + JSPUtil.print(arrequest.getDtCashflowStart()) + " sd " + JSPUtil.print(arrequest.getDtCashflowEnd()) + ") ");
                }

                if (Tools.compare(object.getDbNominalRealisasi(), object.getDbHargaSatuan()) > 0) {
                    throw new Exception("Nilai Realisasi '" + object.getStDescription() + "' lebih besar dari Nilai Pengajuan");
                }

            } else {
                object.setDbTotalNilai(BDUtil.mul(new BigDecimal(object.getStQuantity()), object.getDbHargaSatuan()));
            }

            totalNominalRealized = BDUtil.add(totalNominalRealized, object.getDbTotalNilai());

            if (Tools.compare(object.getDbTotalNilai(), object.getDbNominal()) > 0) {
                String warning = "Realisasi melebihi anggaran <br> (" + JSPUtil.print(object.getDbNominal(), 2) + " < " + JSPUtil.print(object.getDbTotalNilai(), 2);
                throw new RuntimeException(warning);
            }
        }
        arrequest.setDbNominalUsed(totalNominalRealized);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ totalNominalRealized: " + totalNominalRealized);

    }

    public BigDecimal getAnggaranRKAP(String kertaskerjaID) throws Exception {
        final SQLUtil S = new SQLUtil("RKAPDB");

        /*
        select coalesce(a.jumlah_pusat,0)
        from kertas_kerja a
        where a.id_kertas_kerja = 1522
         */
        String sql = null;
        if (arrequest.getStPilihan().equalsIgnoreCase("14")) {
            sql = "select coalesce(a.jumlah_pusat,0) - coalesce(a.anggaran_terpakai,0) "
                    + "from sub_barang a "
                    + "where a.id_sub_barang = ? ";
        } else {
            sql = "select coalesce(a.jumlah_pusat,0) - coalesce(a.anggaran_terpakai,0) "
                    + "from kertas_kerja a "
                    + "where a.id_kertas_kerja = ? ";
        }

        try {
            S.setQuery(sql);
//                    "select coalesce(a.jumlah_pusat,0) - coalesce(a.anggaran_terpakai,0) "
//                    + "from kertas_kerja a "
//                    + "where a.id_kertas_kerja = ? ");

            S.setParam(1, kertaskerjaID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void receiptRequest(String arreqid) throws Exception {

        arrequest = getRemoteGeneralLedger().loadRequest(arreqid);

        if (arrequest == null) {
            throw new RuntimeException("Permintaan tidak dapat ditemukan !");
        }

        setReadOnly(true);

        cashierMode = true;

        setTitle("BAYAR ANGGARAN");

        initTabs();
    }
    private boolean receiptMode;

    /**
     * @return the receiptMode
     */
    public boolean isReceiptMode() {
        return receiptMode;
    }

    /**
     * @param receiptMode the receiptMode to set
     */
    public void setReceiptMode(boolean receiptMode) {
        this.receiptMode = receiptMode;
    }

    public void editCreateRealized(String arreqid) throws Exception {

        if (arreqid == null) {

            clickCreateRealized();

            arrequest.setStStatus("REALISASI");

            return;
        }

        clickSuperEdit(arreqid);

        logger.logDebug("############# " + arrequest.getStStatus());

        if (arrequest.isStatusProposal()) {
            if (!arrequest.isCashflowFlag()) {
                throw new RuntimeException("Anggaran belum di-Cashflow Divisi Keuangan");
            }
        }

        if (arrequest.isStatusRealized()) {
            throw new RuntimeException("Anggaran Harus Status PROPOSAL");
        }

//        DateTime currentDateLastDay = new DateTime(arrequest.getDtCashierDate());
//
//        Date maximumBackDate = currentDateLastDay.plusDays(7).toDate();
//
//        boolean compare = Tools.compare(new Date(), maximumBackDate) > 0;
//        if (compare) {
//            throw new RuntimeException("Pengeluaran uang muka kas kecil max 7 hari kerja kwitansi harus disampaikan ke kasir");
//        }

        checkActiveEffective();

        arrequest.setStNextStatus("REALISASI");
        arrequest.setStActiveFlag("Y");
        arrequest.setStEffectiveFlag("N");
        arrequest.setStValidasiF("N");
        arrequest.setStApprovedWho(null);
        arrequest.setDtApprovedDate(null);
        arrequest.setStCashierWho(null);
        arrequest.setDtCashierDate(null);
        arrequest.setStPrintFlag(null);
        arrequest.setStTransactionNo(null);
        arrequest.setDbNominalUsed(null);
        arrequest.setStMonths(null);
        arrequest.setStYears(null);

        arrequest.checkRealisasiNoBefore(arrequest.getStRequestNo());
//        arrequest.generateEndorseNo();
        arrequest.generateNoRealisasi();

        final DTOList objects = arrequest.getApproval();
        for (int i = 0; i < objects.size(); i++) {
            ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(i);

//            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStEffectiveFlag("N");
        }

        approvalMode = false;

        arrequest.markUpdate();
//        getReqObject().deleteAll();

        setTitle("REALISASI ANGGARAN");

        initTabs();

        proposalMode = false;
        realisasiMode = true;
    }
    private boolean proposalMode;
    private boolean realisasiMode;

    /**
     * @return the realisasiMode
     */
    public boolean isRealisasiMode() {
        return realisasiMode;
    }

    /**
     * @param realisasiMode the realisasiMode to set
     */
    public void setRealisasiMode(boolean realisasiMode) {
        this.realisasiMode = realisasiMode;
    }

    /**
     * @return the proposalMode
     */
    public boolean isProposalMode() {
        return proposalMode;
    }

    /**
     * @param proposalMode the proposalMode to set
     */
    public void setProposalMode(boolean proposalMode) {
        this.proposalMode = proposalMode;
    }
    private boolean ownerPms = SessionManager.getInstance().getSession().hasResource("REQ_PMS");
    private boolean ownerUmum = SessionManager.getInstance().getSession().hasResource("REQ_UMUM");
    private boolean ownerAdm = SessionManager.getInstance().getSession().hasResource("REQ_ADM");

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
    private DTOList approval;

    /**
     * @return the approval
     */
    public DTOList getApproval() {
        return arrequest.getApproval();
    }

    /**
     * @param approval the approval to set
     */
    public void setApproval(DTOList approval) {
        this.approval = approval;
    }
    private String appIndex;

    public void doNewApproval() {
        final ARRequestApprovalView adr = new ARRequestApprovalView();

        adr.markNew();

        arrequest.getApproval().add(adr);

    }

    public void doDeleteApproval() {

        arrequest.getApproval().delete(Integer.parseInt(appIndex));

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

    public void clickEditApp(String arreqid) throws Exception {
        clickView(arreqid);

        if (arrequest.getStPilihan().equalsIgnoreCase("1")) {
            if (!arrequest.isOwnerPms()) {
                throw new RuntimeException("Tidak bisa diinput, bukan kewenangan!");
            }
        } else if (arrequest.getStPilihan().equalsIgnoreCase("2")) {
            if (!arrequest.isOwnerUmum()) {
                throw new RuntimeException("Tidak bisa diinput, bukan kewenangan!");
            }
        } else if (arrequest.getStPilihan().equalsIgnoreCase("3")) {
            if (!arrequest.isOwnerAdm()) {
                throw new RuntimeException("Tidak bisa diinput, bukan kewenangan!");
            }
        }

//        final DTOList objects = arrequest.getApproval();
//        logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + objects.size());
//        for (int i = 0; i < objects.size(); i++) {
//            ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(0);
//
//            if (!obj.isEffective()) {
//                throw new RuntimeException("Kadiv Belum Setujui!");
//            }
//        }

        clickEdit(arreqid);

        setReadOnly(false);

        tabs.setActiveTab("TAB_APPROVAL");
    }

    /**
     * @return the appIndex
     */
    public String getAppIndex() {
        return appIndex;
    }

    /**
     * @param appIndex the appIndex to set
     */
    public void setAppIndex(String appIndex) {
        this.appIndex = appIndex;
    }

    public void doApprove() throws Exception {

        //CEK LIMIT PERSETUJUAN
//        if (arrequest.isStatusProposal()) {
        if (isApprovalMode()) {
            final DTOList objects = arrequest.getApproval();
            if (isCanApprove()) {
                for (int i = 0; i < objects.size(); i++) {
                    ARRequestApprovalView obj = (ARRequestApprovalView) objects.get(0);

                    if (obj.isEffective()) {
                        throw new RuntimeException("Kadiv sudah Setujui!");
                    }

                    if (!SessionManager.getInstance().getSession().getStUserID().equalsIgnoreCase(obj.getStApprovalWho())) {
                        throw new RuntimeException("Harus Disetujui Oleh Pemilik Anggaran!");
                    }
                }

                String param = getLevelLimit();
                String param2[] = param.split("[\\|]");
                String level = param2[0];
                String ket = param2[1];

                logger.logDebug("@@@@@@@@@@@@@@@@@@@@lvl " + level);
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@obj " + objects.size());
                if (Tools.compare(objects.size(), Integer.parseInt(level)) < 0) {
                    throw new RuntimeException("Pejabat Persetujuan belum sesuai limit (" + ket + ")");
                }
            }

            if (isCanApproveDireksi()) {
                String param = getLevelLimit();
                String param2[] = param.split("[\\|]");
                String level = param2[0];
                String ket = param2[1];

                logger.logDebug("@@@@@@@@@@@@@@@@@@@@lvl " + level);
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@obj " + objects.size());
                if (Tools.compare(objects.size(), Integer.parseInt(level)) < 0) {
                    throw new RuntimeException("Pejabat Persetujuan belum sesuai limit (" + ket + ")");
                }
            }
        }

        String approval = SessionManager.getInstance().getSession().getStUserID();
        String getDireksi = getDireksi(approval);
        if (getDireksi == null) {
            throw new RuntimeException("ID Direksi Bapak " + getUser(approval).getStUserName().toUpperCase() + " Tidak Terdaftar untuk Persetujuan Direksi");
        }

        if (getDireksi != null) {
            String param2[] = getDireksi.split("[\\|]");
            String eff = param2[1];

            if (Tools.isYes(eff)) {
                throw new RuntimeException("ID Direksi Bapak " + getUser(approval).getStUserName().toUpperCase() + " sudah menyetujui Anggaran");
            }
        }

        final SQLUtil S = new SQLUtil();

        PreparedStatement PS = S.setQuery("update ar_request_approval set approval_date = ?, eff_flag = 'Y' where in_id = ? and approval_who = ? and delete_flag is null ");

        PS.setDate(1, new java.sql.Date(new Date().getTime()));
        PS.setObject(2, arrequest.getStARRequestID());
        PS.setObject(3, approval);

        int j = PS.executeUpdate();

        if (j != 0) {
            logger.logInfo("+++++++ UPDATE APPROVAL : " + arrequest.getStARRequestID() + " ++++++++++++++++++");
        }

        S.release();
//        }

        if (Tools.isYes(allApproval())) {
            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + allApproval());

            PreparedStatement PS1 = S.setQuery("update ar_request_fee set validasi_f = 'Y' where req_id = ? ");

            PS1.setObject(1, arrequest.getStARRequestID());

            int k = PS1.executeUpdate();

            if (k != 0) {
                logger.logInfo("+++++++ UPDATE VALIDASI : " + arrequest.getStARRequestID() + " ++++++++++++++++++");
            }

            S.release();
        }

        close();
    }

    public String getDireksi(String direksi) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    " select a.approval_who||'|'||a.eff_flag as approval from ar_request_approval a "
                    + "where a.in_id = ? and a.approval_who = ? ");

            S.setParam(1, arrequest.getStARRequestID());
            S.setParam(2, direksi);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }
            return null;
        } finally {
            S.release();
        }
    }

    public String getLevelLimit() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    " select a.ref3||'|'||a.keterangan as level from ar_request_level a "
                    + "where a.act_flag = 'Y' and a.ref1 <= ? and a.ref2 >= ? ");

            S.setParam(1, arrequest.getDbNominalUsed());
            S.setParam(2, arrequest.getDbNominalUsed());

            final ResultSet RS = S.executeQuery();

//            if (RS.next()) {
//                return RS.getInt(1);
//            }
//            return RS.getInt(1);

            if (RS.next()) {
                return RS.getString(1);
            }
            return null;
        } finally {
            S.release();
        }
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public ARRequestApprovalView onNewApproval() throws Exception {

        final ARRequestApprovalView item = new ARRequestApprovalView();

        item.markNew();

        item.setStApprovalWho("");

        item.setStEffectiveFlag("N");

        getApproval().add(item);

        return item;
    }

    /**
     * @return the validasiMode
     */
    public boolean isValidasiMode() {
        return validasiMode;
    }

    /**
     * @param validasiMode the validasiMode to set
     */
    public void setValidasiMode(boolean validasiMode) {
        this.validasiMode = validasiMode;
    }

    public void clickApprovalVal(String arreqid) throws Exception {
        clickEdit(arreqid);

        setReadOnly(true);

        validasiMode = true;
    }

    public void doApproveVal() throws Exception {

//        if (isValidasiRealzMode()) {
//            final DTOList objects = arrequest.getReqObject();
//            for (int i = 0; i < objects.size(); i++) {
//                ARRequestFeeObj obj = (ARRequestFeeObj) objects.get(i);
//
//                if (obj.getStKwitansi() == null) {
//                    throw new RuntimeException("Kwitansi Realisasi " + obj.getStDescription() + " belum di-upload!");
//                }
//
//                if (Tools.compare(obj.getDtTglInvoice(), arrequest.getDtTglRequest()) < 0) {
//                    throw new Exception("Tanggal Invoice pada Keterangan " + obj.getStDescription() + " tidak bisa backdate");
//                }
//            }
//            logger.logDebug("################### jalankan kwitansi");
//        }

        arrequest.setStEffectiveFlag("Y");
        arrequest.setStApprovedWho(SessionManager.getInstance().getSession().getStUserID());
        arrequest.setDtApprovedDate(new Date());

        arrequest.markUpdate();
        getRemoteGeneralLedger().saveRequest(arrequest, arrequest.getStNextStatus(), validasiMode);

        close();
    }

    public String allApproval() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isApproval = false;

        try {
            String cek = "select eff_flag from ar_request_approval "
                    + "where in_id = ? and delete_flag is null "
                    + "group by eff_flag order by eff_flag limit 1";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, arrequest.getStARRequestID());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

        } finally {
            S.release();
        }

        return null;
    }

    /**
     * @return the validasiRealzMode
     */
    public boolean isValidasiRealzMode() {
        return validasiRealzMode;
    }

    /**
     * @param validasiRealzMode the validasiRealzMode to set
     */
    public void setValidasiRealzMode(boolean validasiRealzMode) {
        this.validasiRealzMode = validasiRealzMode;
    }

    public int getLevelDireksi() throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            String cek = "select ref3 from ar_request_level a "
                    + "where a.act_flag = 'Y' and a.ref1 <= ? and a.ref2 >= ? ";

            PreparedStatement PS = S.setQuery(cek);
            S.setParam(1, arrequest.getDbNominalUsed());
            S.setParam(2, arrequest.getDbNominalUsed());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return Integer.parseInt(RS.getString(1));
            }

            return -1;

        } finally {
            S.release();
        }
    }

    public boolean isCekLimit() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isCekLimit = false;

        try {
            String cek = "select level_id from ar_request_level a "
                    + "where a.act_flag = 'Y' and a.ref1 <= ? and a.ref2 >= ? ";

            PreparedStatement PS = S.setQuery(cek);
            S.setParam(1, arrequest.getDbNominalUsed());
            S.setParam(2, arrequest.getDbNominalUsed());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isCekLimit = true;
            }

        } finally {
            S.release();
        }


        return isCekLimit;
    }

    public String getDirektorat() throws Exception {

        SQLUtil S = new SQLUtil();

        try {
            String cek = "select direktorat from s_division "
                    + "where rkap_id = ? and active_flag = 'Y' ";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, arrequest.getStRegionID());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

        } finally {
            S.release();
        }

        return null;
    }

    public void clickEditCashflow(String arreqid) throws Exception {
        clickView(arreqid);

        if (!arrequest.isValidasiFlag()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        if (arrequest.isCashflowFlag()) {
            throw new Exception("Anggaran sudah di-Cashflow");
        }

        setReadOnly(false);

        arrequest.markUpdate();

        setTitle("BUAT CASHFLOW");
    }

    public void clickEditValidasi(String arreqid) throws Exception {
        clickView(arreqid);

        if (!arrequest.isValidasiFlag()) {
            throw new Exception("Anggaran Belum Disetujui");
        }

        if (arrequest.isCashierFlag()) {
            throw new Exception("Anggaran sudah di-Validasi");
        }

        setReadOnly(false);

        arrequest.markUpdate();

        setTitle("BUAT VALIDASI");
    }

    /**
     * @return the cashflowMode
     */
    public boolean isCashflowMode() {
        return cashflowMode;
    }

    /**
     * @param cashflowMode the cashflowMode to set
     */
    public void setCashflowMode(boolean cashflowMode) {
        this.cashflowMode = cashflowMode;
    }

    /**
     * @return the valFinanceMode
     */
    public boolean isValFinanceMode() {
        return valFinanceMode;
    }

    /**
     * @param valFinanceMode the valFinanceMode to set
     */
    public void setValFinanceMode(boolean valFinanceMode) {
        this.valFinanceMode = valFinanceMode;
    }
}

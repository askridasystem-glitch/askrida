/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentPencairanView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.AccountView2;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ARInvestmentPencairanView extends DTO {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentPencairanView.class);
    public static String tableName = "ar_inv_pencairan";
    public static String fieldMap[][] = {
        {"stARCairID", "ar_cair_id*pk"},
        {"stCreateWho", "create_who"},
        {"dtCreateDate", "create_date"},
        {"stChangeWho", "change_who"},
        {"dtChangeDate", "change_date"},
        {"stARDepoID", "ar_depo_id"},
        {"stNodefo", "nodefo"},
        {"stKonter", "konter"},
        {"stBuktiB", "bukti_b"},
        {"stBuktiC", "bukti_c"},
        {"stNoRekeningDeposito", "norekdep"},
        {"stKodedepo", "kodedepo"},
        {"stCurrency", "ccy"},
        {"dbCurrencyRate", "ccy_rate"},
        {"dbNominalKurs", "nomkurs"},
        {"dbNominal", "nominal"},
        {"dtTglawal", "tglawal"},
        {"dtTglakhir", "tglakhir"},
        {"stBulan", "bulan"},
        {"stHari", "hari"},
        {"dbBunga", "bunga"},
        {"dbPajak", "pajak"},
        {"stCostCenterCode", "koda"},
        {"stCompanyType", "comp_type"},
        {"stEntityID", "kdbank"},
        {"dtTgldepo", "tgldepo"},
        {"dtTglmuta", "tglmuta"},
        {"dtTglCair", "tglcair"},
        {"stKeterangan", "ket"},
        {"stRegister", "register"},
        {"stRegisterBentuk", "regbentuk"},
        {"stDepoName", "nama_depo"},
        {"stBankName", "nama_bank"},
        {"stEffectiveFlag", "effective_flag"},
        {"stReceiptClassID", "rc_id"},
        {"stApprovedWho", "approved_who"},
        {"dtApprovedDate", "approved_date"},
        {"stARParentID", "ar_parent_id"},
        {"stActiveFlag", "active_flag"},
        {"dbPinalty", "pinalty"},
        {"stAccountDepo", "account_depo"},
        {"stAccountBank", "account_bank"},
        //{"stFlag","flag"},
        {"stNoRekening", "norekening"},
        {"stCreateName", "create_name*n"},
        {"stChangeName", "change_name*n"},
        {"stJournalStatus", "journal_status"},
        {"stType", "type"},
        {"stYears", "years"},
        {"stMonths", "months"},
        {"dtTglawalTrx", "tglawaltrans"},
        {"dtTglakhirTrx", "tglakhirtrans"},
        {"stDeleted", "deleted"},
        {"stActiveCairFlag", "active_cair_flag"},
        {"stARIzinCairDetID", "ar_izincairdet_id"},
        {"stARIzinCairID", "ar_izincair_id"},
        {"stRealisasiNobuk", "realisasi_nobuk"},
        {"stARBungaID", "ar_bunga_id"},
        {"stNoSurat", "no_surat*n"},};
    private String stARCairID;
    private String stARDepoID;
    private String stCreateWho;
    private Date dtCreateDate;
    private String stChangeWho;
    private Date dtChangeDate;
    private String stNodefo;
    private String stKonter;
    private String stBuktiB;
    private String stBuktiC;
    private String stNoRekeningDeposito;
    private String stKodedepo;
    private String stCurrency;
    private BigDecimal dbCurrencyRate;
    private BigDecimal dbNominalKurs;
    private BigDecimal dbNominal;
    private Date dtTglawal;
    private Date dtTglakhir;
    private String stBulan;
    private String stHari;
    private BigDecimal dbBunga;
    private BigDecimal dbPajak;
    private String stCostCenterCode;
    private String stCompanyType;
    private String stEntityID;
    private Date dtTgldepo;
    private Date dtTglmuta;
    private Date dtTglCair;
    private String stKeterangan;
    //private String stFlag;
    private String stRegister;
    private String stRegisterBentuk;
    private String stDepoName;
    private String stBankName;
    private String stEffectiveFlag;
    private String stReceiptClassID;
    private String stApprovedWho;
    private Date dtApprovedDate;
    private String stARParentID;
    private String stActiveFlag;
    private BigDecimal dbPinalty;
    private String stAccountDepo;
    private String stAccountBank;
    private String stNoRekening;
    private String stCreateName;
    private String stChangeName;
    private String stJournalStatus;
    private String stType;
    private String stYears;
    private String stMonths;
    private boolean Posted;
    private Date dtTglawalTrx;
    private Date dtTglakhirTrx;
    private String stDeleted;
    private String stActiveCairFlag;
    private String stARIzinCairDetID;
    private String stARIzinCairID;
    private String stRealisasiNobuk;
    private String stARBungaID;
    private String stNoSurat;

    public String getStARCairID() {
        return stARCairID;
    }

    public void setStARCairID(String stARCairID) {
        this.stARCairID = stARCairID;
    }

    public String getStKonter() {
        return stKonter;
    }

    public void setStKonter(String stKonter) {
        this.stKonter = stKonter;
    }

    public String getStBuktiB() {
        return stBuktiB;
    }

    public void setStBuktiB(String stBuktiB) {
        this.stBuktiB = stBuktiB;
    }

    public String getStBuktiC() {
        return stBuktiC;
    }

    public void setStBuktiC(String stBuktiC) {
        this.stBuktiC = stBuktiC;
    }

    public String getStNoRekeningDeposito() {
        return stNoRekeningDeposito;
    }

    public void setStNoRekeningDeposito(String stNoRekeningDeposito) {
        this.stNoRekeningDeposito = stNoRekeningDeposito;
    }

    public String getStKodedepo() {
        return stKodedepo;
    }

    public void setStKodedepo(String stKodedepo) {
        this.stKodedepo = stKodedepo;
    }

    public String getStCurrency() {
        return stCurrency;
    }

    public void setStCurrency(String stCurrency) {
        this.stCurrency = stCurrency;
    }

    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }

    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
    }

    public BigDecimal getDbNominalKurs() {
        return dbNominalKurs;
    }

    public void setDbNominalKurs(BigDecimal dbNominalKurs) {
        this.dbNominalKurs = dbNominalKurs;
    }

    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    public Date getDtTglawal() {
        return dtTglawal;
    }

    public void setDtTglawal(Date dtTglawal) {
        this.dtTglawal = dtTglawal;
    }

    public Date getDtTglakhir() {
        return dtTglakhir;
    }

    public void setDtTglakhir(Date dtTglakhir) {
        this.dtTglakhir = dtTglakhir;
    }

    public String getStBulan() {
        return stBulan;
    }

    public void setStBulan(String stBulan) {
        this.stBulan = stBulan;
    }

    public String getStHari() {
        return stHari;
    }

    public void setStHari(String stHari) {
        this.stHari = stHari;
    }

    public BigDecimal getDbBunga() {
        return dbBunga;
    }

    public void setDbBunga(BigDecimal dbBunga) {
        this.dbBunga = dbBunga;
    }

    public BigDecimal getDbPajak() {
        return dbPajak;
    }

    public void setDbPajak(BigDecimal dbPajak) {
        this.dbPajak = dbPajak;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public Date getDtTgldepo() {
        return dtTgldepo;
    }

    public void setDtTgldepo(Date dtTgldepo) {
        this.dtTgldepo = dtTgldepo;
    }

    public Date getDtTglmuta() {
        return dtTglmuta;
    }

    public void setDtTglmuta(Date dtTglmuta) {
        this.dtTglmuta = dtTglmuta;
    }

    public String getStKeterangan() {
        return stKeterangan;
    }

    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }
    /*
    public String getStFlag() {
    return stFlag;
    }

    public void setStFlag(String stFlag) {
    this.stFlag = stFlag;
    }
     */

    public String getStRegister() {
        return stRegister;
    }

    public void setStRegister(String stRegister) {
        this.stRegister = stRegister;
    }

    public String getStRegisterBentuk() {
        return stRegisterBentuk;
    }

    public void setStRegisterBentuk(String stRegisterBentuk) {
        this.stRegisterBentuk = stRegisterBentuk;
    }

    public String getStDepoName() {
        return stDepoName;
    }

    public void setStDepoName(String stDepoName) {
        this.stDepoName = stDepoName;
    }

    public String getStBankName() {
        return stBankName;
    }

    public void setStBankName(String stBankName) {
        this.stBankName = stBankName;
    }

    public Date getDtTglCair() {
        return dtTglCair;
    }

    public void setDtTglCair(Date dtTglCair) {
        this.dtTglCair = dtTglCair;
    }

    public String getStARDepoID() {
        return stARDepoID;
    }

    public void setStARDepoID(String stARDepoID) {
        this.stARDepoID = stARDepoID;
    }

    public String getStNodefo() {
        return stNodefo;
    }

    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    public String getStCompanyType() {
        return stCompanyType;
    }

    public void setStCompanyType(String stCompanyType) {
        this.stCompanyType = stCompanyType;
    }

    public AccountView2 getAccounts() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stEntityID);
    }

    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public boolean isEffective() {
        return Tools.isYes(stEffectiveFlag);
    }

    public boolean isActive() {
        return Tools.isYes(stActiveFlag);
    }

    public ARReceiptClassView getReceiptclass() {
        return (ARReceiptClassView) (stReceiptClassID == null ? null : DTOPool.getInstance().getDTO(ARReceiptClassView.class, stReceiptClassID));
    }

    public String getStReceiptClassID() {
        return stReceiptClassID;
    }

    public void setStReceiptClassID(String stReceiptClassID) {
        this.stReceiptClassID = stReceiptClassID;
    }
    private DTOList bunga;

    public DTOList getBunga() {
        loadBunga();
        return bunga;
    }

    private void loadBunga() {

        String nobuk = null;
        String norek = null;
        if (stBuktiB != null) {
            nobuk = "= '" + stBuktiB + "'";
        } else {
            nobuk = "is null";
        }

        if (stNoRekening != null) {
            norek = "= '" + stNoRekening + "'";
        } else {
            norek = "is null";
        }

        try {
            if (bunga == null) {
                bunga = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_bunga where bukti_b " + nobuk + " and nodefo = ? and norekening " + norek + " and norekdep = ? and nomkurs = ? and koda = ? and delete_flag is null ",
                        new Object[]{stNodefo, stNoRekeningDeposito, dbNominalKurs, stCostCenterCode},
                        ARInvestmentBungaView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private DTOList details;

    public DTOList getDetails(String ARCairID) {
        loadDetails(ARCairID);
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }

    public void loadDetails(String ARCairID) {
        try {
            if (details == null) {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_pencairan where ar_cair_id = ? and deleted is null ",
                        new Object[]{ARCairID},
                        ARInvestmentPencairanView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ARInvestmentDepositoView getDeposito() {
        final ARInvestmentDepositoView deposito = (ARInvestmentDepositoView) DTOPool.getInstance().getDTO(ARInvestmentDepositoView.class, stARDepoID);

        return deposito;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    public Date getDtApprovedDate() {
        return dtApprovedDate;
    }

    public void setDtApprovedDate(Date dtApprovedDate) {
        this.dtApprovedDate = dtApprovedDate;
    }

    public String getStARParentID() {
        return stARParentID;
    }

    public void setStARParentID(String stARParentID) {
        this.stARParentID = stARParentID;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public boolean raisePeriode() throws Exception {
        return Tools.compare(new Date(), getDtTglawal()) >= 0
                && Tools.compare(new Date(), getDtTglakhir()) <= 0;
    }

    public void generateNoBuktiC() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        final String methodCode = Tools.getDigitRightJustified(getReceiptclass().getStMethodCode(), 1);
        String bankCode = null;
        if (getStEntityID() != null) {
            bankCode = getAccounts().getStAccountNo().substring(5, 10);
        }

        String counterKey =
                DateUtil.getYear2Digit(getDtTglCair())
                + DateUtil.getMonth2Digit(getDtTglCair());

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        rn = StringTools.leftPad(rn, '0', 5);

        stBuktiC =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //deposito.setStBuktiB(stNodefo);
        //deposito.setStRegister(stRegister);
    }

    public void generateRegisterCair() throws Exception {
        /*
        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);

        String counterKey =
        DateUtil.getYear2Digit(getDtTglmuta())+
        DateUtil.getMonth2Digit(getDtTglmuta());

        String rn = String.valueOf(IDFactory.createNumericID("INVREG" + counterKey + ccCode,1));

        rn = StringTools.leftPad(rn,'0', 5);
         */

        stRegister =
                stBuktiC.substring(1, 7)
                + stBuktiC.substring(14, 19);

        //deposito.setStBuktiB(stNodefo);
        //deposito.setStRegister(stRegister);
    }

    public BigDecimal getDbPinalty() {
        return dbPinalty;
    }

    public void setDbPinalty(BigDecimal dbPinalty) {
        this.dbPinalty = dbPinalty;
    }

    public AccountView2 getAccountsInvest() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stNoRekeningDeposito);
    }

    public String findAccountBunga(String stCostCenterCode) throws Exception {
        String bunga = new String("651120000000 " + stCostCenterCode);

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where cc_code = ? and accountno = ?");

            PS.setString(1, stCostCenterCode);
            PS.setString(2, bunga);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public String findAccountPinalty(String stCostCenterCode) throws Exception {
        String bunga = new String("892100000000 " + stCostCenterCode);

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where cc_code = ? and accountno = ?");

            PS.setString(1, stCostCenterCode);
            PS.setString(2, bunga);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public String getStAccountDepo() {
        return stAccountDepo;
    }

    public void setStAccountDepo(String stAccountDepo) {
        this.stAccountDepo = stAccountDepo;
    }

    public String getStAccountBank() {
        return stAccountBank;
    }

    public void setStAccountBank(String stAccountBank) {
        this.stAccountBank = stAccountBank;
    }

    public String getStNoRekening() {
        return stNoRekening;
    }

    public void setStNoRekening(String stNoRekening) {
        this.stNoRekening = stNoRekening;
    }

    public String findAccountBank(String stEntityID) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select accountno from gl_accounts where account_id = ?");

            PS.setString(1, stEntityID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public String getStCreateWho() {
        return stCreateWho;
    }

    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }

    public String getStCreateName() {
        if (stCreateWho == null) {
            return "";
        }

        UserSessionView view = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stCreateWho);

        return view.getStUserName();
        //return stCreateName;
    }

    public String getStChangeName() {
        if (stChangeWho == null) {
            return "";
        }

        UserSessionView view = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stChangeWho);

        return view.getStUserName();
        //return stCreateName;
    }

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    public void setStChangeName(String stChangeName) {
        this.stChangeName = stChangeName;
    }

    public void checkNodefo() throws Exception {
        final DTOList history = getHistoryNodefo();

        for (int i = 0; i < history.size(); i++) {
            ARInvestmentPencairanView pol = (ARInvestmentPencairanView) history.get(i);

            if (pol.getStNodefo().equalsIgnoreCase(getStNodefo())) {
                if (getStARDepoID() == null) {
                    throw new RuntimeException("Nodefo " + getStNodefo() + " Sudah Pernah Dibuat");
                }
            }
        }
    }
    private DTOList historynodefo;

    public DTOList getHistoryNodefo() {
        loadHistoryNodefo();
        return historynodefo;
    }

    public void loadHistoryNodefo() {
        try {
            if (historynodefo == null) {
                historynodefo = ListUtil.getDTOListFromQuery(
                        "select nodefo "
                        + " from ar_inv_pencairan "
                        + " where nodefo = ? and deleted is null limit 5",
                        new Object[]{stNodefo},
                        ARInvestmentPencairanView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private DTOList depositoview;

    public DTOList getDepositoView() {
        loadDepositoView();
        return depositoview;
    }

    private void loadDepositoView() {

        String nobuk = null;
        String norek = null;
        if (stBuktiB != null) {
            nobuk = "= '" + stBuktiB + "'";
        } else {
            nobuk = "is null";
        }

        if (stNoRekening != null) {
            norek = "= '" + stNoRekening + "'";
        } else {
            norek = "is null";
        }

        try {
            if (depositoview == null) {
                depositoview = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_deposito where bukti_b " + nobuk + " and nodefo = ? and norekening " + norek + " and date_trunc('day',tglakhir) >= ? and norekdep = ? and nomkurs = ? and koda = ? and deleted is null ",
                        new Object[]{stNodefo, dtTglCair, stNoRekeningDeposito, dbNominalKurs, stCostCenterCode},
                        ARInvestmentDepositoView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStJournalStatus() {
        return stJournalStatus;
    }

    public void setStJournalStatus(String stJournalStatus) {
        this.stJournalStatus = stJournalStatus;
    }

    public Date getDtCreateDate() {
        return dtCreateDate;
    }

    public void setDtCreateDate(Date dtCreateDate) {
        this.dtCreateDate = dtCreateDate;
    }

    public String getStChangeWho() {
        return stChangeWho;
    }

    public void setStChangeWho(String stChangeWho) {
        this.stChangeWho = stChangeWho;
    }

    public Date getDtChangeDate() {
        return dtChangeDate;
    }

    public void setDtChangeDate(Date dtChangeDate) {
        this.dtChangeDate = dtChangeDate;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
    }

    public String getStYears() {
        return stYears;
    }

    public void setStYears(String stYears) {
        this.stYears = stYears;
    }

    public String getStMonths() {
        return stMonths;
    }

    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if (getStCostCenterCode() != null) {
                cek = cek + " and cc_code = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStMonths());
            PS.setString(2, getStYears());

            if (getStCostCenterCode() != null) {
                PS.setString(3, getStCostCenterCode());
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

    public void setPosted(boolean Posted) {
        this.Posted = Posted;
    }

    public Date getDtTglawalTrx() {
        return dtTglawalTrx;
    }

    public void setDtTglawalTrx(Date dtTglawalTrx) {
        this.dtTglawalTrx = dtTglawalTrx;
    }

    public Date getDtTglakhirTrx() {
        return dtTglakhirTrx;
    }

    public void setDtTglakhirTrx(Date dtTglakhirTrx) {
        this.dtTglakhirTrx = dtTglakhirTrx;
    }
    private boolean ValidateCair;

    /**
     * @return the ValidateCair
     */
    public boolean isValidateCair() throws Exception {
        SQLUtil S = new SQLUtil();

        boolean isValidateCair = false;

        try {
            String cek = " select  a.ar_depo_id,a.nodefo,a.bukti_b "
                    + "from ar_inv_perpanjangan a "
                    + "where nodefo = ? ";

            if (getStBuktiB() != null) {
                cek = cek + " and bukti_b = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStNodefo());

            if (getStBuktiB() != null) {
                PS.setString(2, getStBuktiB());
            }

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isValidateCair = true;
            }

        } finally {
            S.release();
        }

        return isValidateCair;
    }

    /**
     * @param ValidateCair the ValidateCair to set
     */
    public void setValidateCair(boolean ValidateCair) {
        this.ValidateCair = ValidateCair;
    }

    /**
     * @return the stDeleted
     */
    public String getStDeleted() {
        return stDeleted;
    }

    /**
     * @param stDeleted the stDeleted to set
     */
    public void setStDeleted(String stDeleted) {
        this.stDeleted = stDeleted;
    }

    public boolean isDeleted() {
        return Tools.isYes(stDeleted);
    }

    /**
     * @return the stActiveCairFlag
     */
    public String getStActiveCairFlag() {
        return stActiveCairFlag;
    }

    /**
     * @param stActiveCairFlag the stActiveCairFlag to set
     */
    public void setStActiveCairFlag(String stActiveCairFlag) {
        this.stActiveCairFlag = stActiveCairFlag;
    }

    public boolean isActiveCairFlag() {
        return Tools.isYes(stActiveCairFlag);
    }
    private DTOList perpanjangan;

    public DTOList getPerpanjangan() {
        loadPerpanjangan();
        return perpanjangan;
    }

    private void loadPerpanjangan() {

        String nobuk = null;
        String norek = null;
        if (stBuktiB != null) {
            nobuk = "= '" + stBuktiB + "'";
        } else {
            nobuk = "is null";
        }

        if (stNoRekening != null) {
            norek = "= '" + stNoRekening + "'";
        } else {
            norek = "is null";
        }

        try {
            if (perpanjangan == null) {
                perpanjangan = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_perpanjangan where bukti_b " + nobuk + " and nodefo = ? and norekening " + norek + " and norekdep = ? and nomkurs = ? and koda = ? ",
                        new Object[]{stNodefo, stNoRekeningDeposito, dbNominalKurs, stCostCenterCode},
                        ARInvestmentPerpanjanganView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stARIzinCairDetID
     */
    public String getStARIzinCairDetID() {
        return stARIzinCairDetID;
    }

    /**
     * @param stARIzinCairDetID the stARIzinCairDetID to set
     */
    public void setStARIzinCairDetID(String stARIzinCairDetID) {
        this.stARIzinCairDetID = stARIzinCairDetID;
    }

    /**
     * @return the stARIzinCairID
     */
    public String getStARIzinCairID() {
        return stARIzinCairID;
    }

    /**
     * @param stARIzinCairID the stARIzinCairID to set
     */
    public void setStARIzinCairID(String stARIzinCairID) {
        this.stARIzinCairID = stARIzinCairID;
    }

    public String getEntity(String glcode) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select ent_name from ent_master where gl_code = ?");

            PS.setString(1, glcode);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }
            return null;

        } finally {
            S.release();
        }
    }

    public String getNoRekening(String glcode) throws Exception {

        final SQLUtil S = new SQLUtil();

        String sql = "select rekno from gl_accounts where accountno like '122%" + glcode + "%" + stCostCenterCode + "'";

        try {
            final PreparedStatement PS = S.setQuery(sql);

            logger.logDebug("@@@@@@@@@@@@@@@@@@@@ " + sql);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }
            return null;

        } finally {
            S.release();
        }
    }

    public String findAccountBankUtama(String ccCode) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where acct_level = '1' and cc_code = ? ");

            PS.setString(1, ccCode);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    /**
     * @return the stRealisasiNobuk
     */
    public String getStRealisasiNobuk() {
        return stRealisasiNobuk;
    }

    /**
     * @param stRealisasiNobuk the stRealisasiNobuk to set
     */
    public void setStRealisasiNobuk(String stRealisasiNobuk) {
        this.stRealisasiNobuk = stRealisasiNobuk;
    }

    /**
     * @return the stARBungaID
     */
    public String getStARBungaID() {
        return stARBungaID;
    }

    /**
     * @param stARBungaID the stARBungaID to set
     */
    public void setStARBungaID(String stARBungaID) {
        this.stARBungaID = stARBungaID;
    }
    private DTOList bungaharian;

    /**
     * @return the bungaharian
     */
    public DTOList getBungaharian() {
        loadBungaharian();
        return bungaharian;
    }

    /**
     * @param bungaharian the bungaharian to set
     */
    public void setBungaharian(DTOList bungaharian) {
        this.bungaharian = bungaharian;
    }

    private void loadBungaharian() {
        try {
            if (bungaharian == null) {
                bungaharian = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_bunga where ar_depo_id = ? and delete_flag is null ",
                        new Object[]{stARDepoID},
                        ARInvestmentBungaView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStNoSurat() {
        return stNoSurat;
    }

    public void setStNoSurat(String stNoSurat) {
        this.stNoSurat = stNoSurat;
    }
}

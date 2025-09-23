/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentDepositoView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView2;
import com.webfin.gl.model.GLCurrencyView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

public class ARInvestmentDepositoView extends DTO {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentDepositoView.class);
    public static String tableName = "ar_inv_deposito";
    public static String comboFields[] = {"ar_depo_id", "bukti_b", "nodefo", "jumlah"};
    /*
    CREATE TABLE ar_inv_deposito
    (
    ar_depo_id bigint NOT NULL,
    nodefo character varying(32),
    konter bigint,
    bukti_b character varying(15),
    norekdep character varying(15),
    kodedepo character varying(1),
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    ccy character varying(5),
    ccy_rate numeric,
    nomkurs numeric,
    nominal numeric,
    tglawal timestamp without time zone,
    tglakhir timestamp without time zone,
    bulan character varying(5),
    hari character varying(5),
    bunga numeric,
    pajak numeric,
    koda character varying(2),
    kdbank bigint,
    tgldepo timestamp without time zone,
    tglmuta timestamp without time zone,
    tgltrans timestamp without time zone,
    tglrest timestamp without time zone,
    ket character varying(255),
    flag character varying(1),
    register character varying(10),
    CONSTRAINT ar_deposito_pkey PRIMARY KEY (ar_depo_id )
    )*/
    public static String fieldMap[][] = {
        {"stARDepoID", "ar_depo_id*pk"},
        {"stCreateWho", "create_who"},
        {"dtCreateDate", "create_date"},
        {"stChangeWho", "change_who"},
        {"dtChangeDate", "change_date"},
        {"stNodefo", "nodefo"},
        {"stKonter", "konter"},
        {"stBuktiB", "bukti_b"},
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
        {"stKeterangan", "ket"},
        {"stRegister", "register"},
        {"stDepoName", "nama_depo"},
        {"stBankName", "nama_bank"},
        {"stEffectiveFlag", "effective_flag"},
        {"stReceiptClassID", "rc_id"},
        {"stStatus", "status"},
        {"stApprovedWho", "approved_who"},
        {"dtApprovedDate", "approved_date"},
        {"stARParentID", "ar_parent_id"},
        {"stActiveFlag", "active_flag"},
        {"stAccountDepo", "account_depo"},
        {"stAccountBank", "account_bank"},
        {"stNoRekening", "norekening"},
        {"stCreateName", "create_name*n"},
        {"dtTglCair", "tglcair"},
        {"dtTglEntryCair", "tglentrycair"},
        {"stARCairID", "ar_cair_id"},
        {"stJournalStatus", "journal_status"},
        {"stYears", "years"},
        {"stMonths", "months"},
        {"stBuktiD", "bukti_d*n"},
        {"dtTglBunga", "tglbunga*n"},
        {"dbAngka", "angka*n"},
        {"stUpload", "upload_bentuk"},
        {"stARIzinDetID", "ar_izindet_id"},
        {"stPrimaryFlag", "primary_flag"},
        {"stUnit", "unit"},
        {"stJumlah", "jumlah*n"},
        {"stNodefoSementara", "nodefo_sementara"},
        {"stKdBankDana", "kdbank_dana"},
        {"stBankNameDana", "nama_bank_dana"},
        {"stAccountBankDana", "account_bank_dana"},
        {"dbNominalDana", "nominal_dana"},
        {"stIjinPusatF", "ijin_pusat_f"},
        {"dtTansferDate", "transfer_date"},
        {"stPosisi", "posisi"},
        {"stARIzinID", "ar_izin_id"},
        {"stARCairUlangID", "ar_cairulang_id"},
        {"stAdminNotes", "admin_notes"},
        {"stNodefoOld", "nodefo_old*n"},
        {"stNoRekeningOld", "norekening_old*n"},};
    private String stARDepoID;
    private String stCreateWho;
    private Date dtCreateDate;
    private String stChangeWho;
    private Date dtChangeDate;
    private String stNodefo;
    private String stKonter;
    private String stBuktiB;
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
    private String stKeterangan;
    //private String stFlag;
    private String stRegister;
    private String stDepoName;
    private String stBankName;
    private String stEffectiveFlag;
    private String stReceiptClassID;
    private String stStatus;
    private String stNextStatus;
    private String stApprovedWho;
    private Date dtApprovedDate;
    private String stARParentID;
    private String stActiveFlag;
    private String stAccountDepo;
    private String stAccountBank;
    private String stNoRekening;
    private String stCreateName;
    private Date dtTglCair;
    private Date dtTglEntryCair;
    private String stARCairID;
    private String stJournalStatus;
    private String stYears;
    private String stMonths;
    private boolean Posted;
    private String stBuktiD;
    private String stUpload;
    private String stARIzinDetID;
    private Date dtTglBunga;
    private BigDecimal dbAngka;
    private String stPrimaryFlag;
    private String stUnit;
    private String stJumlah;
    private String stNodefoSementara;
    private String stKdBankDana;
    private String stBankNameDana;
    private String stAccountBankDana;
    private BigDecimal dbNominalDana;
    private String stIjinPusatF;
    private Date dtTansferDate;
    private String stPosisi;
    private String stARIzinID;
    private String stARCairUlangID;
    private String stAdminNotes;
    private String stNodefoOld;
    private String stNoRekeningOld;

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

    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }

    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
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
    private DTOList pencairan;

    public DTOList getPencairan() {
        loadPencairan();
        return pencairan;
    }

    private void loadPencairan() {
        try {
            if (pencairan == null) {
                pencairan = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_pencairan where ar_depo_id = ? and deleted is null order by ar_cair_id ",
                        new Object[]{stARDepoID},
                        ARInvestmentPencairanView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPencairan(DTOList pencairan) {
        this.pencairan = pencairan;
    }

    public Date advance(Date perDate, int addYear) {

        //final int perlen = lgPeriodLength.intValue();

        final Calendar cld = Calendar.getInstance();

        cld.setTime(perDate);

        cld.add(Calendar.YEAR, addYear);

        return cld.getTime();

    }
    private DTOList bunga;

    public DTOList getBunga() {
        loadBunga();
        return bunga;
    }

    private void loadBunga() {
        try {
            if (bunga == null) {
                bunga = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_bunga where ar_depo_id = ? and delete_flag is null "
                        + " order by ar_bunga_id ",
                        new Object[]{stARDepoID},
                        ARInvestmentBungaView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBunga(DTOList bunga) {
        this.bunga = bunga;
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

    public AccountView2 getAccountsInvest() {
        final AccountView2 account = (AccountView2) DTOPool.getInstance().getDTO(AccountView2.class, stNoRekeningDeposito);

        return account;
    }

    private DTOList details;

    public DTOList getDetails(String ARDepoID) {
        loadDetails(ARDepoID);
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }

    public void loadDetails(String ARDepoID) {
        try {
            if (details == null) {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_deposito where ar_depo_id = ? and deleted is null ",
                        new Object[]{ARDepoID},
                        ARInvestmentDepositoView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public void generateRenewalNo() {

        int eNo = Integer.parseInt(stKonter);

        eNo += 1;

        stKonter = String.valueOf(eNo);
    }

    public String getStNextStatus() {
        return stNextStatus;
    }

    public void setStNextStatus(String stNextStatus) {
        this.stNextStatus = stNextStatus;
    }

    private String getStCurrentStatus() {
        return stNextStatus == null ? stStatus : stNextStatus;
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

    public ARInvestmentPencairanView getPencairan2() {
        final ARInvestmentPencairanView cair = (ARInvestmentPencairanView) DTOPool.getInstance().getDTO(ARInvestmentPencairanView.class, stARDepoID);

        return cair;
    }

    public ARInvestmentBungaView getBunga2() {
        final ARInvestmentBungaView bunga = (ARInvestmentBungaView) DTOPool.getInstance().getDTO(ARInvestmentBungaView.class, stARDepoID);

        return bunga;
    }

    public GLCurrencyView getCurrency() {
        return (GLCurrencyView) DTOPool.getInstance().getDTO(GLCurrencyView.class, stCurrency);
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

    public void checkNodefo() throws Exception {
        String nodefo = null;
        String historynodefo = null;

        final DTOList history = getHistoryNodefo();

        for (int i = 0; i < history.size(); i++) {
            ARInvestmentDepositoView pol = (ARInvestmentDepositoView) history.get(i);

            nodefo = getStNodefo() + getStNoRekening();
            historynodefo = pol.getStNodefo() + pol.getStNoRekening();

            if (getStARDepoID() == null) {
                if (nodefo.equalsIgnoreCase(historynodefo)) //if(getStARDepoID()==null)
                {
                    throw new RuntimeException("Nodefo " + getStNodefo() + " dan No. Rekening " + getStNoRekening() + " Sudah Pernah Dibuat");

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
                        "select * "
                        + " from ar_inv_deposito "
                        + " where status in ('DEPOSITO','RENEWAL') and nodefo = ? and deleted is null limit 5",
                        new Object[]{stNodefo},
                        ARInvestmentDepositoView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateNoBuktiDeposito() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        final String methodCode = Tools.getDigitRightJustified(getReceiptclass().getStMethodCode(), 1);
        String bankCode = null;
        if (getStEntityID() != null) {
            bankCode = getAccounts().getStAccountNo().substring(5, 10);
        }
        String counterKey =
                DateUtil.getYear2Digit(getDtTglmuta())
                + DateUtil.getMonth2Digit(getDtTglmuta());

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H

        rn = StringTools.leftPad(rn, '0', 5);

        stBuktiB =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
    }

    public void generateRegisterBentuk() throws Exception {
        /*
        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);

        String counterKey =
        DateUtil.getYear2Digit(getDtTglmuta())+
        DateUtil.getMonth2Digit(getDtTglmuta());

        String rn = String.valueOf(IDFactory.createNumericID("INVREG" + counterKey + ccCode,1));

        rn = StringTools.leftPad(rn,'0', 5);
         *
         *C130110101000300008
         */

        stRegister =
                stBuktiB.substring(1, 7)
                + stBuktiB.substring(14, 19);

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
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

    public String findAccountDepo(String stNoRekeningDeposito) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select accountno from gl_accounts where account_id = ?");

            PS.setString(1, stNoRekeningDeposito);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);


            }
            return null;

        } finally {
            S.release();
        }
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

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    public BigDecimal mHari() throws Exception {
        if (stARDepoID == null) {
            return null;


        }
        BigDecimal mHari = new BigDecimal(0);

        if (stKodedepo.equalsIgnoreCase("1")) {
            mHari = BDUtil.div(new BigDecimal(stHari), new BigDecimal(365));
        } else {
            mHari = BDUtil.mul(new BigDecimal(stBulan), new BigDecimal(30));
            mHari = BDUtil.div(mHari, new BigDecimal(365));
        }

        return mHari;
    }

    public BigDecimal mPajak() throws Exception {
        if (stARDepoID == null) {
            return null;


        }
        return BDUtil.div(BDUtil.sub(new BigDecimal(100), dbPajak), new BigDecimal(100));
    }

    public BigDecimal mNomKursStart(BigDecimal nominal) throws Exception {
        if (stARDepoID == null) {
            return null;


        }
        return BDUtil.div(BDUtil.mul(nominal, dbBunga), new BigDecimal(100));
    }

    public BigDecimal reCalculateStart(String kodedepo, String bulan, BigDecimal nominal) throws Exception {
        //test();

        BigDecimal rumus = new BigDecimal(0);
        rumus = BDUtil.mul(mNomKursStart(nominal), mHari());
        rumus = BDUtil.mul(rumus, mPajak());

        if (kodedepo.equalsIgnoreCase("1")) {
            rumus = BDUtil.mul(rumus, mPajak());
        } else {
            rumus = BDUtil.mul(rumus, mPajak());
            rumus = BDUtil.div(rumus, new BigDecimal(bulan));
        }

        return rumus;
    }

    public Date getDtTglCair() {
        return dtTglCair;
    }

    public void setDtTglCair(Date dtTglCair) {
        this.dtTglCair = dtTglCair;
    }

    public Date getDtTglEntryCair() {
        return dtTglEntryCair;
    }

    public void setDtTglEntryCair(Date dtTglEntryCair) {
        this.dtTglEntryCair = dtTglEntryCair;
    }

    public String getStARCairID() {
        return stARCairID;
    }

    public void setStARCairID(String stARCairID) {
        this.stARCairID = stARCairID;
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
    private DTOList perpanjangan;

    public DTOList getPerpanjangan() {
        loadPerpanjangan();
        return perpanjangan;
    }

    private void loadPerpanjangan() {
        try {
            if (perpanjangan == null) {
                perpanjangan = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_deposito where nodefo = ? and norekening = ? and status = 'RENEWAL' and deleted is null ",
                        new Object[]{stNodefo, stNoRekening},
                        ARInvestmentDepositoView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPerpanjangan(DTOList perpanjangan) {
        this.perpanjangan = perpanjangan;
    }
    private DTOList bunganodefo;

    public DTOList getBunganodefo() {
        loadBunganodefo();
        return bunganodefo;
    }

    private void loadBunganodefo() {
        try {
            if (bunganodefo == null) {
                bunganodefo = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_bunga where nodefo = ? and norekening = ? and delete_flag is null ",
                        new Object[]{stNodefo, stNoRekening},
                        ARInvestmentBungaView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBunganodefo(DTOList bunganodefo) {
        this.bunganodefo = bunganodefo;
    }
    private DTOList cairnodefo;

    public DTOList getCairnodefo() {
        loadCairnodefo();
        return cairnodefo;
    }

    private void loadCairnodefo() {
        try {
            if (cairnodefo == null) {
                cairnodefo = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_pencairan where nodefo = ? and norekening = ? and deleted is null ",
                        new Object[]{stNodefo, stNoRekening},
                        ARInvestmentPencairanView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCairnodefo(DTOList cairnodefo) {
        this.cairnodefo = cairnodefo;
    }

    public String getStBuktiD() {
        return stBuktiD;
    }

    public void setStBuktiD(String stBuktiD) {
        this.stBuktiD = stBuktiD;
    }

    public Date getDtTglBunga() {
        return dtTglBunga;
    }

    public void setDtTglBunga(Date dtTglBunga) {
        this.dtTglBunga = dtTglBunga;
    }

    public BigDecimal getDbAngka() {
        return dbAngka;
    }

    public void setDbAngka(BigDecimal dbAngka) {
        this.dbAngka = dbAngka;
    }
    private DTOList perpanjangan2;

    public DTOList getPerpanjangan2() {
        loadPerpanjangan2();
        return perpanjangan2;
    }

    private void loadPerpanjangan2() {
        try {
            if (perpanjangan2 == null) {
                perpanjangan2 = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_perpanjangan where ar_depo_id = ? order by ar_depo_id ",
                        new Object[]{stARDepoID},
                        ARInvestmentPerpanjanganView.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPerpanjangan2(DTOList perpanjangan2) {
        this.perpanjangan2 = perpanjangan2;
    }

    /**
     * @return the StUpload
     */
    public String getStUpload() {
        return stUpload;
    }

    /**
     * @param StUpload the StUpload to set
     */
    public void setStUpload(String stUpload) {
        this.stUpload = stUpload;
    }

    /**
     * @return the stARIzinID
     */
    public String getStARIzinDetID() {
        return stARIzinDetID;
    }

    /**
     * @param stARIzinID the stARIzinID to set
     */
    public void setStARIzinDetID(String stARIzinDetID) {
        this.stARIzinDetID = stARIzinDetID;
    }

    /**
     * @return the stPrimaryFlag
     */
    public String getStPrimaryFlag() {
        return stPrimaryFlag;
    }

    /**
     * @param stPrimaryFlag the stPrimaryFlag to set
     */
    public void setStPrimaryFlag(String stPrimaryFlag) {
        this.stPrimaryFlag = stPrimaryFlag;
    }

    public boolean isPrimary() {
        return Tools.isYes(stPrimaryFlag);
    }

    /**
     * @return the stUnit
     */
    public String getStUnit() {
        return stUnit;
    }

    /**
     * @param stUnit the stUnit to set
     */
    public void setStUnit(String stUnit) {
        this.stUnit = stUnit;
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

    /**
     * @return the stJumlah
     */
    public String getStJumlah() {
        return stJumlah;
    }

    /**
     * @param stJumlah the stJumlah to set
     */
    public void setStJumlah(String stJumlah) {
        this.stJumlah = stJumlah;
    }

    /**
     * @return the stNodefoSementara
     */
    public String getStNodefoSementara() {
        return stNodefoSementara;
    }

    /**
     * @param stNodefoSementara the stNodefoSementara to set
     */
    public void setStNodefoSementara(String stNodefoSementara) {
        this.stNodefoSementara = stNodefoSementara;
    }

    /**
     * @return the stKdBankDana
     */
    public String getStKdBankDana() {
        return stKdBankDana;
    }

    /**
     * @param stKdBankDana the stKdBankDana to set
     */
    public void setStKdBankDana(String stKdBankDana) {
        this.stKdBankDana = stKdBankDana;
    }

    /**
     * @return the stBankNameDana
     */
    public String getStBankNameDana() {
        return stBankNameDana;
    }

    /**
     * @param stBankNameDana the stBankNameDana to set
     */
    public void setStBankNameDana(String stBankNameDana) {
        this.stBankNameDana = stBankNameDana;
    }

    /**
     * @return the stAccountBankDana
     */
    public String getStAccountBankDana() {
        return stAccountBankDana;
    }

    /**
     * @param stAccountBankDana the stAccountBankDana to set
     */
    public void setStAccountBankDana(String stAccountBankDana) {
        this.stAccountBankDana = stAccountBankDana;
    }

    /**
     * @return the dbNominalDana
     */
    public BigDecimal getDbNominalDana() {
        return dbNominalDana;
    }

    /**
     * @param dbNominalDana the dbNominalDana to set
     */
    public void setDbNominalDana(BigDecimal dbNominalDana) {
        this.dbNominalDana = dbNominalDana;
    }

    public AccountView2 getAccountsDana() {
        final AccountView2 account = (AccountView2) DTOPool.getInstance().getDTO(AccountView2.class, stKdBankDana);

        return account;
    }

    public String findAccount(String akun, String koda) throws Exception {
        final SQLUtil S = new SQLUtil();

        String sql = "select account_id from gl_accounts where cc_code = ? and accountno like ? ";

        try {
            final PreparedStatement PS = S.setQuery(sql);

            PS.setString(1, koda);
            PS.setString(2, akun + "%");

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
     * @return the stIjinPusatF
     */
    public String getStIjinPusatF() {
        return stIjinPusatF;
    }

    /**
     * @param stIjinPusatF the stIjinPusatF to set
     */
    public void setStIjinPusatF(String stIjinPusatF) {
        this.stIjinPusatF = stIjinPusatF;
    }

    public boolean isIjinPusat() {
        return Tools.isYes(stIjinPusatF);
    }

    /**
     * @return the dtTansferDate
     */
    public Date getDtTansferDate() {
        return dtTansferDate;
    }

    /**
     * @param dtTansferDate the dtTansferDate to set
     */
    public void setDtTansferDate(Date dtTansferDate) {
        this.dtTansferDate = dtTansferDate;
    }

    /**
     * @return the stPosisi
     */
    public String getStPosisi() {
        return stPosisi;
    }

    /**
     * @param stPosisi the stPosisi to set
     */
    public void setStPosisi(String stPosisi) {
        this.stPosisi = stPosisi;
    }

    /**
     * @return the stARIzinID
     */
    public String getStARIzinID() {
        return stARIzinID;
    }

    /**
     * @param stARIzinID the stARIzinID to set
     */
    public void setStARIzinID(String stARIzinID) {
        this.stARIzinID = stARIzinID;
    }

    /**
     * @return the stARCairUlangID
     */
    public String getStARCairUlangID() {
        return stARCairUlangID;
    }

    /**
     * @param stARCairUlangID the stARCairUlangID to set
     */
    public void setStARCairUlangID(String stARCairUlangID) {
        this.stARCairUlangID = stARCairUlangID;
    }

    /**
     * @return the stAdminNotes
     */
    public String getStAdminNotes() {
        return stAdminNotes;
    }

    /**
     * @param stAdminNotes the stAdminNotes to set
     */
    public void setStAdminNotes(String stAdminNotes) {
        this.stAdminNotes = stAdminNotes;
    }

    /**
     * @return the stNodefoOld
     */
    public String getStNodefoOld() {
        return stNodefoOld;
    }

    /**
     * @param stNodefoOld the stNodefoOld to set
     */
    public void setStNodefoOld(String stNodefoOld) {
        this.stNodefoOld = stNodefoOld;
    }

    /**
     * @return the stNoRekeningOld
     */
    public String getStNoRekeningOld() {
        return stNoRekeningOld;
    }

    /**
     * @param stNoRekeningOld the stNoRekeningOld to set
     */
    public void setStNoRekeningOld(String stNoRekeningOld) {
        this.stNoRekeningOld = stNoRekeningOld;
    }
}

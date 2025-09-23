/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentBungaView
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.AccountView2;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ARInvestmentBungaView extends DTO {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentBungaView.class);
    public static String tableName = "ar_inv_bunga";
    public transient static String comboFields[] = {"ar_bunga_id", "nodefo", "bukti_b", "tglmutasi", "jumlah", "bukti_d"};
    public static String fieldMap[][] = {
        {"stARBungaID", "ar_bunga_id*pk"},
        {"stCreateWho", "create_who"},
        {"dtCreateDate", "create_date"},
        {"stChangeWho", "change_who"},
        {"dtChangeDate", "change_date"},
        {"stARDepoID", "ar_depo_id"},
        {"stNodefo", "nodefo"},
        {"stNoBuktiB", "bukti_b"},
        {"stNoRekeningDeposito", "norekdep"},
        {"dbPersen", "persen"},
        {"stHari", "hari"},
        {"dtTglBunga", "tglbunga"},
        {"stCompanyType", "comp_type"},
        {"stCurrency", "ccy"},
        {"dbCurrencyRate", "ccy_rate"},
        {"dbAngka", "angka"},
        {"dbAngka1", "angka1"},
        {"stCostCenterCode", "koda"},
        {"stEntityID", "kdbank"},
        {"stKeterangan", "ket"},
        {"stRegister", "register"},
        {"stRegisterBentuk", "regbentuk"},
        {"stDepoName", "nama_depo"},
        {"stBankName", "nama_bank"},
        {"dbNominalKurs", "nomkurs"},
        {"dbNominal", "nominal"},
        {"stARCairID", "ar_cair_id"},
        {"stKodedepo", "kodedepo"},
        {"dbPajak", "pajak"},
        {"stEffectiveFlag", "effective_flag"},
        {"stApprovedWho", "approved_who"},
        {"dtApprovedDate", "approved_date"},
        {"stARParentID", "ar_parent_id"},
        {"stActiveFlag", "active_flag"},
        {"dtTglAwal", "tglawal"},
        {"dtTglAkhir", "tglakhir"},
        {"dtTglCair", "tglcair"},
        {"stNoBuktiD", "bukti_d"},
        {"stReceiptClassID", "rc_id"},
        {"stAccountDepo", "account_depo"},
        {"stAccountBank", "account_bank"},
        {"stNoRekening", "norekening"},
        {"stCreateName", "create_name*n"},
        {"stChangeName", "change_name*n"},
        {"dtTglEntryCair", "tgl_entrycair"},
        {"StYears", "years"},
        {"StMonths", "months"},
        {"stJumlah", "jumlah*n"},
        {"stTglMutasi", "tglmutasi*n"},
        {"stARIzinBngID", "ar_izinbng_id"},
        {"stNoSurat", "no_surat*n"},
        {"stNoBuktiC", "bukti_c"},};
    private String stJumlah;
    private String stTglMutasi;
    private String stARBungaID;
    private String stARDepoID;
    private String stCreateWho;
    private Date dtCreateDate;
    private String stChangeWho;
    private Date dtChangeDate;
    private String stNoBuktiB;
    private String stNodefo;
    private String stNoRekeningDeposito;
    private BigDecimal dbPersen;
    private String stHari;
    private Date dtTglBunga;
    private String stCurrency;
    private BigDecimal dbCurrencyRate;
    private BigDecimal dbAngka;
    private BigDecimal dbAngka1;
    private String stCompanyType;
    private String stCostCenterCode;
    private String stEntityID;
    private String stCustomer;
    private String stKeterangan;
    private String stRegister;
    private String stRegisterBentuk;
    private String stDepoName;
    private String stBankName;
    private BigDecimal dbNominalKurs;
    private BigDecimal dbNominal;
    private String stARCairID;
    private String stKodedepo;
    private BigDecimal dbPajak;
    private String stEffectiveFlag;
    private String stApprovedWho;
    private Date dtApprovedDate;
    private String stARParentID;
    private String stActiveFlag;
    private Date dtTglAwal;
    private Date dtTglAkhir;
    private Date dtTglCair;
    private String stNoBuktiD;
    private String stReceiptClassID;
    private String stAccountDepo;
    private String stAccountBank;
    private String stNoRekening;
    private ARInvestmentDepositoView deposito;
    private String stCreateName;
    private String stChangeName;
    private Date dtTglEntryCair;
    private String StYears;
    private String StMonths;
    private String stARIzinBngID;
    private String stNoSurat;
    private String stNoBuktiC;
    private boolean Posted;

    public String getStARBungaID() {
        return stARBungaID;
    }

    public void setStARBungaID(String stARBungaID) {
        this.stARBungaID = stARBungaID;
    }

    public String getStNoBuktiB() {
        return stNoBuktiB;
    }

    public void setStNoBuktiB(String stNoBuktiB) {
        this.stNoBuktiB = stNoBuktiB;
    }

    public String getStNodefo() {
        return stNodefo;
    }

    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    public String getStNoRekeningDeposito() {
        return stNoRekeningDeposito;
    }

    public void setStNoRekeningDeposito(String stNoRekeningDeposito) {
        this.stNoRekeningDeposito = stNoRekeningDeposito;
    }

    public BigDecimal getDbPersen() {
        return dbPersen;
    }

    public void setDbPersen(BigDecimal dbPersen) {
        this.dbPersen = dbPersen;
    }

    public String getStHari() {
        return stHari;
    }

    public void setStHari(String stHari) {
        this.stHari = stHari;
    }

    public Date getDtTglBunga() {
        return dtTglBunga;
    }

    public void setDtTglBunga(Date dtTglBunga) {
        this.dtTglBunga = dtTglBunga;
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

    public BigDecimal getDbAngka() {
        return dbAngka;
    }

    public void setDbAngka(BigDecimal dbAngka) {
        this.dbAngka = dbAngka;
    }

    public BigDecimal getDbAngka1() {
        return dbAngka1;
    }

    public void setDbAngka1(BigDecimal dbAngka1) {
        this.dbAngka1 = dbAngka1;
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

    public String getStCustomer() {
        return stCustomer;
    }

    public void setStCustomer(String stCustomer) {
        this.stCustomer = stCustomer;
    }

    public String getStKeterangan() {
        return stKeterangan;
    }

    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

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

    public String getStARDepoID() {
        return stARDepoID;
    }

    public void setStARDepoID(String stARDepoID) {
        this.stARDepoID = stARDepoID;
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

    public ARInvestmentDepositoView getDeposito() {
        final ARInvestmentDepositoView deposito = (ARInvestmentDepositoView) DTOPool.getInstance().getDTO(ARInvestmentDepositoView.class, stARDepoID);

        return deposito;
    }

    public ARInvestmentPencairanView getPencairan() {
        final ARInvestmentPencairanView cair = (ARInvestmentPencairanView) DTOPool.getInstance().getDTO(ARInvestmentPencairanView.class, stARCairID);

        return cair;
    }

    public BigDecimal mHari() throws Exception {
        if (stARDepoID == null) {
            return null;
        }

        return BDUtil.div(new BigDecimal(stHari), new BigDecimal(365));
    }

    public BigDecimal mPajak() throws Exception {
        if (getDeposito() == null) {
            return null;
        }

        return BDUtil.div(BDUtil.sub(new BigDecimal(100), dbPajak), new BigDecimal(100));
    }

    /*
    public void test() throws Exception {
    logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@@@@@@ : " +dbNominalKurs);
    logger.logDebug("########################### : " +dbPersen);
    logger.logDebug("$$$$$$$$$$$$$$$$$$$$$$$$$$$ : " +stHari);
    logger.logDebug("%%%%%%%%%%%%%%%%%%%%%%%%%%% : " +dbPajak);
    logger.logDebug("WWWWWWWWWWWWWWWWWWWWWWWWWWW : " +getDeposito().getStBulan());
    logger.logDebug("&&&&&&&&&&&&&&&&&&&&&&&&&&& : " +mHari());
    logger.logDebug("*************************** : " +mPajak());
    logger.logDebug("WWWWWWWWWWWWWWWWWWWWWWWWWWW : " +mNomKurs());
    }
     */
    /*
    public BigDecimal mNomKursStart(BigDecimal nominal) throws Exception {
    if (getDeposito()==null) return null;

    return BDUtil.div(BDUtil.mul(nominal, dbPersen), new BigDecimal(100));
    }    
    
    public void reCalculateStart(String kodedepo, String bulan, BigDecimal nominal) throws Exception {
    //test();

    BigDecimal rumus = new BigDecimal(0);
    rumus = BDUtil.mul(mNomKursStart(nominal), mHari());
    rumus = BDUtil.mul(rumus, mPajak());

    if (kodedepo.equalsIgnoreCase("1")) {
    setDbAngka1(rumus);
    } else {
    setDbAngka1(BDUtil.div(rumus, new BigDecimal(bulan)));
    }
    }
     */
    public BigDecimal mNomKurs() throws Exception {
        if (getDeposito() == null) {
            return null;
        }

        return BDUtil.div(dbNominal, new BigDecimal(100));
    }

    public void reCalculate() throws Exception {
        //test();

        BigDecimal angka = new BigDecimal(0);
        if (getStKodedepo().equalsIgnoreCase("1")) {
            angka = getDbAngka();
        } else {
            angka = BDUtil.mul(getDbAngka(), new BigDecimal(getDeposito().getStBulan()));
        }

        BigDecimal rumus = new BigDecimal(0);
        rumus = BDUtil.mul(mPajak(), mHari());

        BigDecimal rumus2 = new BigDecimal(0);
        rumus2 = BDUtil.div(angka, rumus);

        setDbPersen(BDUtil.div(rumus2, mNomKurs()));
    }

    public String getStARCairID() {
        return stARCairID;
    }

    public void setStARCairID(String stARCairID) {
        this.stARCairID = stARCairID;
    }

    public String getStKodedepo() {
        return stKodedepo;
    }

    public void setStKodedepo(String stKodedepo) {
        this.stKodedepo = stKodedepo;
    }

    public BigDecimal getDbPajak() {
        return dbPajak;
    }

    public void setDbPajak(BigDecimal dbPajak) {
        this.dbPajak = dbPajak;
    }

    public AccountView2 getAccounts() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stEntityID);
    }

    public AccountView2 getAccountsInvest() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stNoRekeningDeposito);
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
    private DTOList details;

    public DTOList getDetails(String ARBungaID) {
        loadDetails(ARBungaID);
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }

    public void loadDetails(String ARBungaID) {
        try {
            if (details == null) {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_bunga where ar_bunga_id = ? and delete_flag is null ",
                        new Object[]{ARBungaID},
                        ARInvestmentBungaView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public Date getDtTglAwal() {
        return dtTglAwal;
    }

    public void setDtTglAwal(Date dtTglAwal) {
        this.dtTglAwal = dtTglAwal;
    }

    public Date getDtTglAkhir() {
        return dtTglAkhir;
    }

    public void setDtTglAkhir(Date dtTglAkhir) {
        this.dtTglAkhir = dtTglAkhir;
    }

    public boolean raisePeriode() throws Exception {
        return Tools.compare(new Date(), getDtTglAwal()) >= 0
                && Tools.compare(new Date(), getDtTglAkhir()) <= 0;
    }

    public Date getDtTglCair() {
        return dtTglCair;
    }

    public void setDtTglCair(Date dtTglCair) {
        this.dtTglCair = dtTglCair;
    }

    public ARReceiptClassView getReceiptclass() {
        return (ARReceiptClassView) (stReceiptClassID == null ? null : DTOPool.getInstance().getDTO(ARReceiptClassView.class, stReceiptClassID));
    }

    public void generateNoBuktiD() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        final String methodCode = Tools.getDigitRightJustified(getReceiptclass().getStMethodCode(), 1);
        String bankCode = null;
        if (getStEntityID() != null) {
            bankCode = getAccounts().getStAccountNo().substring(5, 10);
        }

        String counterKey =
                DateUtil.getYear2Digit(getDtTglBunga())
                + DateUtil.getMonth2Digit(getDtTglBunga());

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        rn = StringTools.leftPad(rn, '0', 5);

        stNoBuktiD =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //deposito.setStBuktiB(stNodefo);
        //deposito.setStRegister(stRegister);
    }

    public void generateRegisterBunga() throws Exception {
        /*
        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);

        String counterKey =
        DateUtil.getYear2Digit(getDtTglBunga())+
        DateUtil.getMonth2Digit(getDtTglBunga());

        String rn = String.valueOf(IDFactory.createNumericID("INVREG" + counterKey + ccCode,1));

        rn = StringTools.leftPad(rn,'0', 5);
         */

        stRegister =
                stNoBuktiD.substring(1, 7)
                + stNoBuktiD.substring(14, 19);

        //deposito.setStBuktiB(stNodefo);
        //deposito.setStRegister(stRegister);
    }

    public String getStNoBuktiD() {
        return stNoBuktiD;
    }

    public void setStNoBuktiD(String stNoBuktiD) {
        this.stNoBuktiD = stNoBuktiD;
    }

    public String getStReceiptClassID() {
        return stReceiptClassID;
    }

    public void setStReceiptClassID(String stReceiptClassID) {
        this.stReceiptClassID = stReceiptClassID;
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

    public String findSpecimenDir(String stCostCenterCode) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where cc_code = ? and accountno like '12210%' and upper(description) like '%BUDEP%' ");

            PS.setString(1, stCostCenterCode);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public String findSpecimenDirDesc(String stEntityID) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select description from gl_accounts where account_id = ?");

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

    public Date getDtTglEntryCair() {
        return dtTglEntryCair;
    }

    public void setDtTglEntryCair(Date dtTglEntryCair) {
        this.dtTglEntryCair = dtTglEntryCair;
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

    public String getStYears() {
        return StYears;
    }

    public void setStYears(String StYears) {
        this.StYears = StYears;
    }

    public String getStMonths() {
        return StMonths;
    }

    public void setStMonths(String StMonths) {
        this.StMonths = StMonths;
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

    public String getStJumlah() {
        return stJumlah;
    }

    public void setStJumlah(String stJumlah) {
        this.stJumlah = stJumlah;
    }

    public String getStTglMutasi() {
        return stTglMutasi;
    }

    public void setStTglMutasi(String stTglMutasi) {
        this.stTglMutasi = stTglMutasi;
    }

    public boolean isHaveBunga() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isHaveBunga = false;

        try {
            String cek = "select ar_bunga_id from ar_inv_bunga where effective_flag = 'Y' and nodefo = ? and norekdep = ? and nomkurs = ? and koda = ? and months = ? and years = ? and delete_flag is null ";

            if (getStNoBuktiB() != null) {
                cek = cek + "and bukti_b = ? ";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStNodefo());
            PS.setString(2, getStNoRekeningDeposito());
            PS.setBigDecimal(3, getDbNominalKurs());
            PS.setString(4, getStCostCenterCode());
            PS.setString(5, getStMonths());
            PS.setString(6, getStYears());

            if (getStNoBuktiB() != null) {
                PS.setString(7, getStNoBuktiB());
            }

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isHaveBunga = true;
            }

        } finally {
            S.release();
        }

        return isHaveBunga;
    }

    /**
     * @return the stARIzinBngID
     */
    public String getStARIzinBngID() {
        return stARIzinBngID;
    }

    /**
     * @param stARIzinBngID the stARIzinBngID to set
     */
    public void setStARIzinBngID(String stARIzinBngID) {
        this.stARIzinBngID = stARIzinBngID;
    }

    /**
     * @return the stNoSurat
     */
    public String getStNoSurat() {
        return stNoSurat;
    }

    /**
     * @param stNoSurat the stNoSurat to set
     */
    public void setStNoSurat(String stNoSurat) {
        this.stNoSurat = stNoSurat;
    }

    /**
     * @return the stNoBuktiC
     */
    public String getStNoBuktiC() {
        return stNoBuktiC;
    }

    /**
     * @param stNoBuktiC the stNoBuktiC to set
     */
    public void setStNoBuktiC(String stNoBuktiC) {
        this.stNoBuktiC = stNoBuktiC;
    }
}

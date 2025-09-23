/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentIzinDepositoDetailView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
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

public class ARInvestmentIzinDepositoDetailView extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentIzinDepositoDetailView.class);
    public static String tableName = "ar_izin_deposito_detail";
    public static String comboFields[] = {"index", "nodefo_sementara"};
    /*
    -- Table: ar_izin_deposito_detail

-- DROP TABLE ar_izin_deposito_detail;

CREATE TABLE ar_izin_deposito_detail
(
  ar_depo_id bigint NOT NULL,
  nodefo character varying(32),
  norekdep bigint,
  kodedepo character varying(1),
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  nomkurs numeric,
  nominal numeric,
  tglawal timestamp without time zone,
  tglakhir timestamp without time zone,
  bulan character varying(5),
  hari character varying(5),
  bunga numeric,
  koda character varying(2),
  comp_type character varying(2),
  kdbank bigint,
  rc_id bigint,
  active_flag character varying(1),
  nama_depo character varying(255),
  account_depo character varying(15),
  nama_bank character varying(255),
  account_bank character varying(15),
  sumberbank bigint,
  nama_sumberbank character varying(255),
  account_sumberbank character varying(15),
  norekening character varying(32),
  delete_flag character varying(1),
  upload_bentuk bigint,
  ar_izin_id bigint,
  primary_flag character varying(1),
  unit character varying(10),
  nodefo_sementara text,
  CONSTRAINT ar_izin_deposito_detail_pkey PRIMARY KEY (ar_depo_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ar_izin_deposito_detail
  OWNER TO postgres;

-- Index: ix_ar_izin_deposito_detail_arizinid

-- DROP INDEX ix_ar_izin_deposito_detail_arizinid;

CREATE INDEX ix_ar_izin_deposito_detail_arizinid
  ON ar_izin_deposito_detail
  USING btree
  (ar_izin_id);

-- Index: ix_ar_izin_deposito_detail_nodefo

-- DROP INDEX ix_ar_izin_deposito_detail_nodefo;

CREATE INDEX ix_ar_izin_deposito_detail_nodefo
  ON ar_izin_deposito_detail
  USING btree
  (nodefo COLLATE pg_catalog."default");
*/
    public static String fieldMap[][] = {
        {"stARDepoID", "ar_depo_id*pk"},
        {"stNodefo", "nodefo"},
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
        {"stCostCenterCode", "koda"},
        {"stCompanyType", "comp_type"},
        {"stEntityID", "kdbank"},
        {"stReceiptClassID", "rc_id"},
        {"stDepoName", "nama_depo"},
        {"stBankName", "nama_bank"},
        {"stActiveFlag", "active_flag"},
        {"stAccountDepo", "account_depo"},
        {"stAccountBank", "account_bank"},
        {"stSumberID", "sumberid"},
        {"stSumberBank", "sumberbank"},
        {"stNamaSumberBank", "nama_sumberbank"},
        {"stAccountSumberBank", "account_sumberbank"},
        {"stARIzinID", "ar_izin_id"},
        {"stPrimaryFlag", "primary_flag"},
        {"stUnit", "unit"},
        {"stNodefoSementara", "nodefo_sementara"},
        {"stTujuanID", "tujuanid"},
        {"stAtasNama", "atasnama"},
        {"stNoSurat", "nosurat"},};
    private String stARDepoID;
    private String stNodefo;
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
    private String stCostCenterCode;
    private String stCompanyType;
    private String stEntityID;
    private String stDepoName;
    private String stBankName;
    private String stReceiptClassID;
    private String stActiveFlag;
    private String stAccountDepo;
    private String stAccountBank;
    private String stSumberID;
    private String stSumberBank;
    private String stNamaSumberBank;
    private String stAccountSumberBank;
    private String stARIzinID;
    private String stPrimaryFlag;
    private String stUnit;
    private String stNodefoSementara;
    private String stTujuanID;
    private String stAtasNama;
    private String stNoSurat;

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

    public BigDecimal mNomKursStart(BigDecimal nominal) throws Exception {
        if (stARDepoID == null) {
            return null;


        }
        return BDUtil.div(BDUtil.mul(nominal, dbBunga), new BigDecimal(100));
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
     * @return the stSumberID
     */
    public String getStSumberID() {
        return stSumberID;
    }

    /**
     * @param stSumberID the stSumberID to set
     */
    public void setStSumberID(String stSumberID) {
        this.stSumberID = stSumberID;
    }

    /**
     * @return the stSumberBank
     */
    public String getStSumberBank() {
        return stSumberBank;
    }

    /**
     * @param stSumberBank the stSumberBank to set
     */
    public void setStSumberBank(String stSumberBank) {
        this.stSumberBank = stSumberBank;
    }

    /**
     * @return the stNamaSumberBank
     */
    public String getStNamaSumberBank() {
        return stNamaSumberBank;
    }

    /**
     * @param stNamaSumberBank the stNamaSumberBank to set
     */
    public void setStNamaSumberBank(String stNamaSumberBank) {
        this.stNamaSumberBank = stNamaSumberBank;
    }

    /**
     * @return the stAccountSumberBank
     */
    public String getStAccountSumberBank() {
        return stAccountSumberBank;
    }

    /**
     * @param stAccountSumberBank the stAccountSumberBank to set
     */
    public void setStAccountSumberBank(String stAccountSumberBank) {
        this.stAccountSumberBank = stAccountSumberBank;
    }

    /**
     * @return the stTujuanID
     */
    public String getStTujuanID() {
        return stTujuanID;
    }

    /**
     * @param stTujuanID the stTujuanID to set
     */
    public void setStTujuanID(String stTujuanID) {
        this.stTujuanID = stTujuanID;
    }

    public boolean isDebet() {
        return stSumberID.equalsIgnoreCase("1");
    }

    public boolean isAskrida() {
        return stTujuanID.equalsIgnoreCase("1");
    }

    /**
     * @return the stAtasNama
     */
    public String getStAtasNama() {
        return stAtasNama;
    }

    /**
     * @param stAtasNama the stAtasNama to set
     */
    public void setStAtasNama(String stAtasNama) {
        this.stAtasNama = stAtasNama;
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

    public AccountView2 getAccountBudep() throws Exception {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, findSpecimenDir(stCostCenterCode));
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
}

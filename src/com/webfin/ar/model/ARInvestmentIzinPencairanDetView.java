/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentIzinPencairanDetView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ARInvestmentIzinPencairanDetView extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentIzinPencairanDetView.class);
    public static String tableName = "ar_izin_pencairan_detail";
    //public static String comboFields[] = {"ar_depo_id","nodefo","register","bukti_b","accountno","description"};
    /*
    -- Table: ar_izin_pencairan_detail

    -- DROP TABLE ar_izin_pencairan_detail;

    CREATE TABLE ar_izin_pencairan_detail
    (
    ar_izincairdet_id bigint NOT NULL,
    ar_izincair_id bigint,
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    active_flag character varying(1),
    ar_depo_id bigint,
    bukti_b character varying(20),
    nodefo character varying(32),
    pencairan_ket character varying(32),
    pol_id bigint,
    dla_no character varying(32),
    CONSTRAINT ar_izin_pencairan_detail_pkey PRIMARY KEY (ar_izincairdet_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ar_izin_pencairan_detail
    OWNER TO postgres;
     */
    public static String fieldMap[][] = {
        {"stARIzinCairDetID", "ar_izincairdet_id*pk"},
        {"stARIzinCairID", "ar_izincair_id"},
        {"stActiveFlag", "active_flag"},
        {"stARDepoID", "ar_depo_id"},
        {"stBuktiB", "bukti_b"},
        {"stNodefo", "nodefo"},
        {"stPencairanKet", "pencairan_ket"},
        {"stARInvoiceID", "ar_invoice_id"},
        {"stDLANo", "dla_no"},
        {"dbNilai", "nilai"},
        {"stJenisCair", "jns_pencairan"},
        {"dbBilyetAmount", "bilyet_amount"},
        {"stLineType", "line_type"},
        {"stReceiptNo", "receipt_no"},};
    private String stARIzinCairDetID;
    private String stARIzinCairID;
    private String stActiveFlag;
    private String stARDepoID;
    private String stBuktiB;
    private String stNodefo;
    private String stPencairanKet;
    private String stARInvoiceID;
    private String stDLANo;
    private String stJenisCair;
    private BigDecimal dbNilai;
    private BigDecimal dbBilyetAmount;
    private String stLineType;
    private String stReceiptNo;
    private DTOList deposito;
    private DTOList perpanjangan;

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

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stPencairanKet
     */
    public String getStPencairanKet() {
        return stPencairanKet;
    }

    /**
     * @param stPencairanKet the stPencairanKet to set
     */
    public void setStPencairanKet(String stPencairanKet) {
        this.stPencairanKet = stPencairanKet;
    }

    /**
     * @return the stPolicyID
     */
    public String getStARInvoiceID() {
        return stARInvoiceID;
    }

    /**
     * @param stPolicyID the stPolicyID to set
     */
    public void setStARInvoiceID(String stARInvoiceID) {
        this.stARInvoiceID = stARInvoiceID;
    }

    /**
     * @return the stBuktiB
     */
    public String getStBuktiB() {
        return stBuktiB;
    }

    /**
     * @param stBuktiB the stBuktiB to set
     */
    public void setStBuktiB(String stBuktiB) {
        this.stBuktiB = stBuktiB;
    }

    /**
     * @return the stNodefo
     */
    public String getStNodefo() {
        return stNodefo;
    }

    /**
     * @param stNodefo the stNodefo to set
     */
    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    /**
     * @return the stDLANo
     */
    public String getStDLANo() {
        return stDLANo;
    }

    /**
     * @param stDLANo the stDLANo to set
     */
    public void setStDLANo(String stDLANo) {
        this.stDLANo = stDLANo;
    }

    /**
     * @return the stJenisCair
     */
    public String getStJenisCair() {
        return stJenisCair;
    }

    /**
     * @param stJenisCair the stJenisCair to set
     */
    public void setStJenisCair(String stJenisCair) {
        this.stJenisCair = stJenisCair;
    }

    /**
     * @return the dbNilai
     */
    public BigDecimal getDbNilai() {
        return dbNilai;
    }

    /**
     * @param dbNilai the dbNilai to set
     */
    public void setDbNilai(BigDecimal dbNilai) {
        this.dbNilai = dbNilai;
    }

    /**
     * @return the dbBilyetAmount
     */
    public BigDecimal getDbBilyetAmount() {
        return dbBilyetAmount;
    }

    /**
     * @param dbBilyetAmount the dbBilyetAmount to set
     */
    public void setDbBilyetAmount(BigDecimal dbBilyetAmount) {
        this.dbBilyetAmount = dbBilyetAmount;
    }

    /**
     * @return the stARCairID
     */
    public String getStARDepoID() {
        return stARDepoID;
    }

    /**
     * @param stARCairID the stARCairID to set
     */
    public void setStARDepoID(String stARDepoID) {
        this.stARDepoID = stARDepoID;
    }

    public void loadDeposito() {
        try {
            if (deposito == null) {
                deposito = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_deposito where ar_depo_id = ? and active_flag = 'Y' and deleted is null order by ar_depo_id ",
                        new Object[]{stARDepoID},
                        ARInvestmentDepositoView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the deposito
     */
    public DTOList getDeposito() {
        loadDeposito();
        return deposito;
    }

    /**
     * @param deposito the deposito to set
     */
    public void setDeposito(DTOList deposito) {
        this.deposito = deposito;
    }
    private DTOList details;

    public DTOList getDetails() {
        if (details == null) {
            details = new DTOList();
        }
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }
    private DTOList listRealisasi;

    public DTOList getListRealisasi() throws Exception {
        if (listRealisasi == null) {
            listRealisasi = loadDetailsRealisasi("REALISASI");
        }
        if (listRealisasi == null) {
            listRealisasi = new DTOList();
        }
        return listRealisasi;
    }

    public void setListRealisasi(DTOList listRealisasi) {
        this.listRealisasi = listRealisasi;
    }

    private DTOList loadDetailsRealisasi(String receiptLineType) throws Exception {
        if (stARIzinCairID != null) {
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_izin_pencairan_detail where ar_izincair_id = ? and ar_depo_id = ? and line_type = ? and delete_flag is null order by ar_izincairdet_id ",
                    new Object[]{stARIzinCairID, stARDepoID, receiptLineType},
                    ARInvestmentIzinPencairanDetView.class);
        }

        return null;
    }

    /**
     * @return the stLineType
     */
    public String getStLineType() {
        return stLineType;
    }

    /**
     * @param stLineType the stLineType to set
     */
    public void setStLineType(String stLineType) {
        this.stLineType = stLineType;
    }

    public ARInvestmentDepositoView getDepo() {
        final ARInvestmentDepositoView depo = (ARInvestmentDepositoView) DTOPool.getInstance().getDTO(ARInvestmentDepositoView.class, stARDepoID);

        return depo;
    }

    public ARInvoiceView getInvoice() {
        final ARInvoiceView inv = (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, stARInvoiceID);

        return inv;
    }

    /**
     * @return the perpanjangan
     */
    public DTOList getPerpanjangan() {
        loadPerpanjangan();
        return perpanjangan;
    }

    /**
     * @param perpanjangan the perpanjangan to set
     */
    public void setPerpanjangan(DTOList perpanjangan) {
        this.perpanjangan = perpanjangan;
    }

    public void loadPerpanjangan() {
        try {
            if (perpanjangan == null) {
                perpanjangan = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_perpanjangan where ar_depo_id = ? and active_flag = 'Y' and deleted is null order by ar_depo_id ",
                        new Object[]{stARDepoID},
                        ARInvestmentPerpanjanganView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public String getStJenisDescription() {
        String jenis = null;
        if (getStJenisCair().equalsIgnoreCase("1")) {
            jenis = "KLAIM";
        } else if (getStJenisCair().equalsIgnoreCase("2")) {
            jenis = "TRANSFER";
        } else if (getStJenisCair().equalsIgnoreCase("3")) {
            jenis = "BENTUK";
        } else {
            jenis = "LAIN";
        }

        return jenis;
    }

    /**
     * @return the stReceiptNo
     */
    public String getStReceiptNo() {
        return stReceiptNo;
    }

    /**
     * @param stReceiptNo the stReceiptNo to set
     */
    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public void generateNoBukti() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getDepo().getStCostCenterCode(), 2);
        final String methodCode = Tools.getDigitRightJustified(getDepo().getReceiptclass().getStMethodCode(), 1);
        String bankCode = null;
        if (getDepo().getStEntityID() != null) {
            bankCode = getDepo().getAccounts().getStAccountNo().substring(5, 10);
        }
        String counterKey =
                DateUtil.getYear2Digit(new Date())
                + DateUtil.getMonth2Digit(new Date());

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H

        rn = StringTools.leftPad(rn, '0', 5);

        stReceiptNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
    }
}

/***********************************************************************
 * Module:  com.webfin.ar.model.ARRequestFee
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.model.BiayaOperasionalDetail;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ARRequestFeeObj extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(ARRequestFeeObj.class);
    public static String tableName = "ar_request_fee_obj";
    public static String fieldMap[][] = {
        {"stARRequestObjID", "req_obj_id*pk"},
        {"stAccountID", "accountid"},
        {"stAccountNo", "accountno"},
        {"stAccountDesc", "accountdesc"},
        {"stBiaopTypeID", "biaoptypeid"},
        {"stDescription", "description"},
        {"dbNominal", "nominal"},
        {"stARRequestID", "req_id"},
        {"stKwitansi", "kwitansi"},
        {"dtTglCashback", "cashback_date"},
        {"stSatuanID", "satuanid"},
        {"stQuantity", "quantity"},
        {"dbHargaSatuan", "harga_satuan"},
        {"dbNominalRealisasi", "nominal_realisasi"},
        {"stSpesifikasi", "spesifikasi"},
        {"stBiaopGroupID", "biaoptypegrp"},
        {"stAccountID2", "accountid2"},
        {"dbTotalNilai", "total_nilai"},
        {"dtTglInvoice", "invoice_date"},};
    private String stARRequestObjID;
    private String stAccountID;
    private String stAccountNo;
    private String stAccountDesc;
    private String stBiaopTypeID;
    private String stDescription;
    private BigDecimal dbNominal;
    private String stARRequestID;
    private String stKwitansi;
    private Date dtTglCashback;
    private String stSatuanID;
    private String stQuantity;
    private BigDecimal dbHargaSatuan;
    private BigDecimal dbNominalRealisasi;
    private BigDecimal dbTotalNilai;
    private String stSpesifikasi;
    private String stBiaopGroupID;
    private String stAccountID2;
    private Date dtTglInvoice;

    /**
     * @return the stARRequestID
     */
    public String getStARRequestID() {
        return stARRequestID;
    }

    /**
     * @param stARRequestID the stARRequestID to set
     */
    public void setStARRequestID(String stARRequestID) {
        this.stARRequestID = stARRequestID;
    }

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    /**
     * @return the dbNominal
     */
    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    /**
     * @param dbNominal the dbNominal to set
     */
    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    /**
     * @return the stAccountID
     */
    public String getStAccountID() {
        return stAccountID;
    }

    /**
     * @param stAccountID the stAccountID to set
     */
    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    /**
     * @return the stBiaopTypeID
     */
    public String getStBiaopTypeID() {
        return stBiaopTypeID;
    }

    /**
     * @param stBiaopTypeID the stBiaopTypeID to set
     */
    public void setStBiaopTypeID(String stBiaopTypeID) {
        this.stBiaopTypeID = stBiaopTypeID;
    }

    /**
     * @return the stKwitansi
     */
    public String getStKwitansi() {
        return stKwitansi;
    }

    /**
     * @param stKwitansi the stKwitansi to set
     */
    public void setStKwitansi(String stKwitansi) {
        this.stKwitansi = stKwitansi;
    }

    /**
     * @return the dtTglCashback
     */
    public Date getDtTglCashback() {
        return dtTglCashback;
    }

    /**
     * @param dtTglCashback the dtTglCashback to set
     */
    public void setDtTglCashback(Date dtTglCashback) {
        this.dtTglCashback = dtTglCashback;
    }

    /**
     * @return the stAccountNo
     */
    public String getStAccountNo() {
        return stAccountNo;
    }

    /**
     * @param stAccountNo the stAccountNo to set
     */
    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    /**
     * @return the stAccountDesc
     */
    public String getStAccountDesc() {
        return stAccountDesc;
    }

    /**
     * @param stAccountDesc the stAccountDesc to set
     */
    public void setStAccountDesc(String stAccountDesc) {
        this.stAccountDesc = stAccountDesc;
    }

    /**
     * @return the stARRequestObjID
     */
    public String getStARRequestObjID() {
        return stARRequestObjID;
    }

    /**
     * @param stARRequestObjID the stARRequestObjID to set
     */
    public void setStARRequestObjID(String stARRequestObjID) {
        this.stARRequestObjID = stARRequestObjID;
    }

    public BiayaOperasionalDetail getBiaopDetil() {
        final BiayaOperasionalDetail dtl = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, stBiaopTypeID);

        return dtl;
    }

    /**
     * @return the stSatuanID
     */
    public String getStSatuanID() {
        return stSatuanID;
    }

    /**
     * @param stSatuanID the stSatuanID to set
     */
    public void setStSatuanID(String stSatuanID) {
        this.stSatuanID = stSatuanID;
    }

    /**
     * @return the dbHargaSatuan
     */
    public BigDecimal getDbHargaSatuan() {
        return dbHargaSatuan;
    }

    /**
     * @param dbHargaSatuan the dbHargaSatuan to set
     */
    public void setDbHargaSatuan(BigDecimal dbHargaSatuan) {
        this.dbHargaSatuan = dbHargaSatuan;
    }

    /**
     * @return the stQuantity
     */
    public String getStQuantity() {
        return stQuantity;
    }

    /**
     * @param stQuantity the stQuantity to set
     */
    public void setStQuantity(String stQuantity) {
        this.stQuantity = stQuantity;
    }

    /**
     * @return the dbNominalRealisasi
     */
    public BigDecimal getDbNominalRealisasi() {
        return dbNominalRealisasi;
    }

    /**
     * @param dbNominalRealisasi the dbNominalRealisasi to set
     */
    public void setDbNominalRealisasi(BigDecimal dbNominalRealisasi) {
        this.dbNominalRealisasi = dbNominalRealisasi;
    }

    /**
     * @return the stSpesifikasi
     */
    public String getStSpesifikasi() {
        return stSpesifikasi;
    }

    /**
     * @param stSpesifikasi the stSpesifikasi to set
     */
    public void setStSpesifikasi(String stSpesifikasi) {
        this.stSpesifikasi = stSpesifikasi;
    }

    /**
     * @return the stBiaopGroupID
     */
    public String getStBiaopGroupID() {
        return stBiaopGroupID;
    }

    /**
     * @param stBiaopGroupID the stBiaopGroupID to set
     */
    public void setStBiaopGroupID(String stBiaopGroupID) {
        this.stBiaopGroupID = stBiaopGroupID;
    }

    /**
     * @return the stAccountID2
     */
    public String getStAccountID2() {
        return stAccountID2;
    }

    /**
     * @param stAccountID2 the stAccountID2 to set
     */
    public void setStAccountID2(String stAccountID2) {
        this.stAccountID2 = stAccountID2;
    }

    public String findAccount(String account, String koda) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where accountno like ? and cc_code = ? ");

            PS.setString(1, account + "%");
            PS.setString(2, koda);

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
     * @return the dbTotalNilai
     */
    public BigDecimal getDbTotalNilai() {
        return dbTotalNilai;
    }

    /**
     * @param dbTotalNilai the dbTotalNilai to set
     */
    public void setDbTotalNilai(BigDecimal dbTotalNilai) {
        this.dbTotalNilai = dbTotalNilai;
    }

    /**
     * @return the dtTglInvoice
     */
    public Date getDtTglInvoice() {
        return dtTglInvoice;
    }

    /**
     * @param dtTglInvoice the dtTglInvoice to set
     */
    public void setDtTglInvoice(Date dtTglInvoice) {
        this.dtTglInvoice = dtTglInvoice;
    }
}

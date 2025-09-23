/***********************************************************************
 * Module: com.webfin.ar.model.BiayaPemasaranDetailView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.file.FileView;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.SQLAssembler;
import com.webfin.gl.model.GLCostCenterView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class BiayaPemasaranDetailView extends DTO implements RecordAudit {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    -- Table: biaya_pemasaran_detail

-- DROP TABLE biaya_pemasaran_detail;

CREATE TABLE biaya_pemasaran_detail
(
  pms_det_id bigint NOT NULL,
  pms_id bigint,
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  jenis_pemasaranid character varying(5),
  accountid character varying(5),
  keterangan text,
  description text,
  applydate timestamp without time zone,
  nilai numeric,
  CONSTRAINT biaya_pemasaran_detail_pkey PRIMARY KEY (pms_det_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE biaya_pemasaran_detail
  OWNER TO postgres;

-- Index: biaya_pemasaran_detail_idx

-- DROP INDEX biaya_pemasaran_detail_idx;

CREATE INDEX biaya_pemasaran_detail_idx
  ON biaya_pemasaran_detail
  USING btree
  (pms_id);

-- Index: biaya_pemasaran_detail_idx_det

-- DROP INDEX biaya_pemasaran_detail_idx_det;

CREATE INDEX biaya_pemasaran_detail_idx_det
  ON biaya_pemasaran_detail
  USING btree
  (pms_det_id);
     */
    public static String tableName = "biaya_pemasaran_detail";
    public static String comboFields[] = {"pms_det_id", "jenis_pemasaranid", "accountid"};
    public transient static String fieldMap[][] = {
        {"stPemasaranDetailID", "pms_det_id*pk*nd"},
        {"stPemasaranID", "pms_id"},
        {"stJenisPemasaranID", "jenis_pemasaranid"},
        {"stAccountID", "accountid"},
        {"stAccountNo", "accountno"},
        {"stKeterangan", "keterangan"},
        {"stDescription", "description"},
        {"dtApplyDate", "applydate"},
        {"dbNilai", "nilai"},
        {"dbExcRatePajak", "excess_ratepajak"},
        {"dbExcAmount", "excess_amount"},
        {"stTaxType", "tax_type"},
        {"dbRkapAmount", "rkap_amount"},};
    private String stPemasaranDetailID;
    private String stPemasaranID;
    private String stJenisPemasaranID;
    private String stAccountID;
    private String stAccountNo;
    private String stKeterangan;
    private String stDescription;
    private Date dtApplyDate;
    private BigDecimal dbNilai;
    private BigDecimal dbExcRatePajak;
    private BigDecimal dbExcAmount;
    private String stTaxType;
    private BigDecimal dbRkapAmount;

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public SQLAssembler getSQAComm(String receiptid) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.* ");
        sqa.addSelect(" a.pol_no,a.tertanggung,a.amount,a.no_surat_hutang,a.periode_awal,a.periode_akhir,a.cc_code,"
                    + " (select x.receipt_no from ins_policy x where x.pol_id = a.ins_pol_id) as reinsurer_note ");

        sqa.addQuery(" from ins_proposal_komisi a ");

        sqa.addClause("a.ins_upload_id = ? ");
        sqa.addPar(receiptid);

        sqa.addOrder("a.ins_upload_dtl_id ");

        return sqa;
    }

    /**
     * @return the stPemasaranID
     */
    public String getStPemasaranID() {
        return stPemasaranID;
    }

    /**
     * @param stPemasaranID the stPemasaranID to set
     */
    public void setStPemasaranID(String stPemasaranID) {
        this.stPemasaranID = stPemasaranID;
    }

    /**
     * @return the stPemasaranDetailID
     */
    public String getStPemasaranDetailID() {
        return stPemasaranDetailID;
    }

    /**
     * @param stPemasaranDetailID the stPemasaranDetailID to set
     */
    public void setStPemasaranDetailID(String stPemasaranDetailID) {
        this.stPemasaranDetailID = stPemasaranDetailID;
    }

    /**
     * @return the stJenisPemasaranID
     */
    public String getStJenisPemasaranID() {
        return stJenisPemasaranID;
    }

    /**
     * @param stJenisPemasaranID the stJenisPemasaranID to set
     */
    public void setStJenisPemasaranID(String stJenisPemasaranID) {
        this.stJenisPemasaranID = stJenisPemasaranID;
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
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
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
     * @return the dtApplyDate
     */
    public Date getDtApplyDate() {
        return dtApplyDate;
    }

    /**
     * @param dtApplyDate the dtApplyDate to set
     */
    public void setDtApplyDate(Date dtApplyDate) {
        this.dtApplyDate = dtApplyDate;
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
     * @return the dbExcAmount
     */
    public BigDecimal getDbExcAmount() {
        return dbExcAmount;
    }

    /**
     * @param dbExcAmount the dbExcAmount to set
     */
    public void setDbExcAmount(BigDecimal dbExcAmount) {
        this.dbExcAmount = dbExcAmount;
    }

    /**
     * @return the dbExcRatePajak
     */
    public BigDecimal getDbExcRatePajak() {
        return dbExcRatePajak;
    }

    /**
     * @param dbExcRatePajak the dbExcRatePajak to set
     */
    public void setDbExcRatePajak(BigDecimal dbExcRatePajak) {
        this.dbExcRatePajak = dbExcRatePajak;
    }

    /**
     * @return the stTaxType
     */
    public String getStTaxType() {
        return stTaxType;
    }

    /**
     * @param stTaxType the stTaxType to set
     */
    public void setStTaxType(String stTaxType) {
        this.stTaxType = stTaxType;
    }

    /**
     * @return the dbRkapAmount
     */
    public BigDecimal getDbRkapAmount() {
        return dbRkapAmount;
    }

    /**
     * @param dbRkapAmount the dbRkapAmount to set
     */
    public void setDbRkapAmount(BigDecimal dbRkapAmount) {
        this.dbRkapAmount = dbRkapAmount;
    }

}

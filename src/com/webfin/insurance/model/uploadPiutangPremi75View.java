/***********************************************************************
 * Module:  com.crux.login.model.uploadPiutangPremiView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.SQLAssembler;
import com.webfin.gl.model.GLCostCenterView;
import java.math.BigDecimal;
import java.util.Date;

import java.util.HashMap;

public class uploadPiutangPremi75View extends DTO {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    -- Table: ins_warning_piutang

    -- DROP TABLE ins_warning_piutang;

    CREATE TABLE ins_warning_piutang
    (
    ins_os_dtl_id bigint NOT NULL,
    ins_os_id bigint,
    pol_no character varying(32),
    tertanggung character varying(255),
    amount numeric,
    no_surat_hutang character varying(255),
    amount_total numeric,
    data_amount bigint,
    file_physic bigint,
    create_date timestamp without time zone,
    create_who character varying(32),
    change_date timestamp without time zone,
    change_who character varying(32),
    cc_code character varying(5),
    ket text,
    CONSTRAINT ins_warning_piutang_pkey PRIMARY KEY (ins_os_dtl_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ins_warning_piutang
    OWNER TO postgres;

    -- Index: ins_warning_piutang_ins_os_id_idx

    -- DROP INDEX ins_warning_piutang_ins_os_id_idx;

    CREATE INDEX ins_warning_piutang_ins_os_id_idx
    ON ins_warning_piutang
    USING btree
    (ins_os_id);

    -- Index: ins_warning_piutang_pol_no_idx

    -- DROP INDEX ins_warning_piutang_pol_no_idx;

    CREATE INDEX ins_warning_piutang_pol_no_idx
    ON ins_warning_piutang
    USING btree
    (pol_no COLLATE pg_catalog."default");
     */
    public static String tableName = "ins_warning_piutang75";
    public static String comboFields[] = {"ins_piutang_id", "no_surat_hutang", "data_amount"};
    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_piutang_dtl_id"},
        {"stInsuranceUploadID", "ins_piutang_id"},
        {"stPolicyID", "pol_id"},
        {"stPolicyNo", "pol_no"},
        {"stTertanggung", "tertanggung"},
        {"dtPolicyDate", "policy_date"},
        {"dbAmount", "amount"},
        {"stNoSuratHutang", "no_surat_hutang"},
        {"dbAmountTotal", "amount_total"},
        {"stDataAmount", "data_amount"},
        {"stFilePhysic", "file_physic"},
        {"stCostCenterCode", "cc_code"},
        {"stKeterangan", "ket"},
        {"stCreateWho", "create_who"},
        {"dtCreateDate", "create_date"},
        {"stChangeWho", "change_who"},
        {"dtChangeDate", "change_date"},
        {"dbPremiNetto", "preminetto"},
        {"stDataSettled", "data_settled*n"},
        {"dbSettled", "settled*n"},};
    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stPolicyID;
    private String stPolicyNo;
    private String stTertanggung;
    private Date dtPolicyDate;
    private BigDecimal dbAmount;
    private String stNoSuratHutang;
    private BigDecimal dbAmountTotal;
    private String stDataAmount;
    private String stFilePhysic;
    private String stCostCenterCode;
    private String stKeterangan;
    private String stCreateWho;
    private Date dtCreateDate;
    private String stChangeWho;
    private Date dtChangeDate;
    private BigDecimal dbPremiNetto;
    private String stDataSettled;
    private BigDecimal dbSettled;

    public FileView getFile() {

        final FileView file = (FileView) DTOPool.getInstance().getDTO(FileView.class, getStFilePhysic());

        return file;

    }

    /**
     * @return the stInsuranceUploadDetailID
     */
    public String getStInsuranceUploadDetailID() {
        return stInsuranceUploadDetailID;
    }

    /**
     * @param stInsuranceUploadDetailID the stInsuranceUploadDetailID to set
     */
    public void setStInsuranceUploadDetailID(String stInsuranceUploadDetailID) {
        this.stInsuranceUploadDetailID = stInsuranceUploadDetailID;
    }

    /**
     * @return the stInsuranceUploadID
     */
    public String getStInsuranceUploadID() {
        return stInsuranceUploadID;
    }

    /**
     * @param stInsuranceUploadID the stInsuranceUploadID to set
     */
    public void setStInsuranceUploadID(String stInsuranceUploadID) {
        this.stInsuranceUploadID = stInsuranceUploadID;
    }

    /**
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    /**
     * @return the stDataAmount
     */
    public String getStDataAmount() {
        return stDataAmount;
    }

    /**
     * @param stDataAmount the stDataAmount to set
     */
    public void setStDataAmount(String stDataAmount) {
        this.stDataAmount = stDataAmount;
    }

    /**
     * @return the stTertanggung
     */
    public String getStTertanggung() {
        return stTertanggung;
    }

    /**
     * @param stTertanggung the stTertanggung to set
     */
    public void setStTertanggung(String stTertanggung) {
        this.stTertanggung = stTertanggung;
    }

    /**
     * @return the dbAmount
     */
    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    /**
     * @param dbAmount the dbAmount to set
     */
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    /**
     * @return the dbAmountTotal
     */
    public BigDecimal getDbAmountTotal() {
        return dbAmountTotal;
    }

    /**
     * @param dbAmountTotal the dbAmountTotal to set
     */
    public void setDbAmountTotal(BigDecimal dbAmountTotal) {
        this.dbAmountTotal = dbAmountTotal;
    }

    /**
     * @return the stNoSuratHutang
     */
    public String getStNoSuratHutang() {
        return stNoSuratHutang;
    }

    /**
     * @param stNoSuratHutang the stNoSuratHutang to set
     */
    public void setStNoSuratHutang(String stNoSuratHutang) {
        this.stNoSuratHutang = stNoSuratHutang;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public GLCostCenterView getCostCenter(String stCostCenterCode) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return costcenter;
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
     * @return the dtPolicyDate
     */
    public Date getDtPolicyDate() {
        return dtPolicyDate;
    }

    /**
     * @param dtPolicyDate the dtPolicyDate to set
     */
    public void setDtPolicyDate(Date dtPolicyDate) {
        this.dtPolicyDate = dtPolicyDate;
    }

    /**
     * @return the stCreateWho
     */
    public String getStCreateWho() {
        return stCreateWho;
    }

    /**
     * @param stCreateWho the stCreateWho to set
     */
    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }

    /**
     * @return the dtCreateDate
     */
    public Date getDtCreateDate() {
        return dtCreateDate;
    }

    /**
     * @param dtCreateDate the dtCreateDate to set
     */
    public void setDtCreateDate(Date dtCreateDate) {
        this.dtCreateDate = dtCreateDate;
    }

    /**
     * @return the stChangeWho
     */
    public String getStChangeWho() {
        return stChangeWho;
    }

    /**
     * @param stChangeWho the stChangeWho to set
     */
    public void setStChangeWho(String stChangeWho) {
        this.stChangeWho = stChangeWho;
    }

    /**
     * @return the dtChangeDate
     */
    public Date getDtChangeDate() {
        return dtChangeDate;
    }

    /**
     * @param dtChangeDate the dtChangeDate to set
     */
    public void setDtChangeDate(Date dtChangeDate) {
        this.dtChangeDate = dtChangeDate;
    }

    /**
     * @return the stPolicyID
     */
    public String getStPolicyID() {
        return stPolicyID;
    }

    /**
     * @param stPolicyID the stPolicyID to set
     */
    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public SQLAssembler getSQAComm(String receiptid) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();
        
//        sqa.addSelect(" a.* ");
        sqa.addSelect(" a.ins_piutang_id,a.cc_code,a.policy_date,a.pol_id,a.pol_no,a.tertanggung,a.amount,a.preminetto,(select string_agg(b.receipt_no,'|')) as ket, "
                + "a.amount_total,a.data_amount,a.no_surat_hutang ");

        sqa.addQuery(" from ins_warning_piutang a "
                + "inner join ar_invoice b on b.attr_pol_id = a.pol_id::bigint and b.ar_trx_type_id in (5,6,7) ");

        sqa.addClause("a.ins_piutang_id = ? ");
        sqa.addPar(receiptid);

        sqa.addGroup("a.ins_piutang_id,a.cc_code,a.policy_date,a.pol_id,a.pol_no,a.tertanggung,a.amount,a.preminetto,a.amount_total,a.data_amount,a.no_surat_hutang ");
        sqa.addOrder("a.pol_no ");

        return sqa;
    }

    /**
     * @return the dbPremiNetto
     */
    public BigDecimal getDbPremiNetto() {
        return dbPremiNetto;
    }

    /**
     * @param dbPremiNetto the dbPremiNetto to set
     */
    public void setDbPremiNetto(BigDecimal dbPremiNetto) {
        this.dbPremiNetto = dbPremiNetto;
    }

    /**
     * @return the stDataSettled
     */
    public String getStDataSettled() {
        return stDataSettled;
    }

    /**
     * @param stDataSettled the stDataSettled to set
     */
    public void setStDataSettled(String stDataSettled) {
        this.stDataSettled = stDataSettled;
    }

    /**
     * @return the dbSettled
     */
    public BigDecimal getDbSettled() {
        return dbSettled;
    }

    /**
     * @param dbSettled the dbSettled to set
     */
    public void setDbSettled(BigDecimal dbSettled) {
        this.dbSettled = dbSettled;
    }
}

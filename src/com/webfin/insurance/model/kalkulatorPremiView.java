/***********************************************************************
 * Module:  com.crux.login.model.uploadReinsuranceSpreadingView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;

import java.util.Date;
import java.util.HashMap;

public class kalkulatorPremiView extends DTO implements RecordAudit {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    -- Table: ins_upload_detail

    -- DROP TABLE ins_upload_detail;

    CREATE TABLE ins_upload_detail
    (
    ins_upload_dtl_id bigint,
    ins_upload_id bigint,
    pol_id bigint,
    ins_pol_obj_id bigint,
    pol_no character varying(32),
    order_no bigint,
    nama character varying(255),
    tanggal_lahir timestamp without time zone,
    periode_awal timestamp without time zone,
    periode_akhir timestamp without time zone,
    usia character varying(32),
    tsi numeric,
    rate_jual numeric,
    premi numeric,
    koasuransi bigint,
    koas_menjadi bigint,
    rate_koas numeric,
    premi_koas numeric,
    rate_komisi_koas numeric,
    komisi_koas numeric,
    status character varying(32)
     *
     * file_id character varying(255),
    recap_no character varying(255),
    tsi_total numeric,
    premi_total numeric,
    data_amount bigint
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ins_upload_detail
    OWNER TO postgres;

     */
    public static String tableName = "ins_upload_reins";

    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_upload_dtl_id*pk*nd"},
        {"stInsuranceUploadID", "ins_upload_id"},
        {"stInsurancePolicyID", "ins_pol_id"},
        {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
        {"stPolicyNo", "pol_no"},
        {"stOrderNo", "order_no"},
        {"stNama", "nama"},
        {"dtTanggalLahir", "tanggal_lahir"},
        {"dtPeriodeAwal", "periode_awal"},
        {"dtPeriodeAkhir", "periode_akhir"},
        {"stUsia", "usia"},
        {"dbTSI", "tsi"},
        {"dbPremi", "premi"},
        {"stTreaty", "treaty"},
        {"stStatus", "status"},
        {"stFilePhysic", "file_physic"},
        {"stRecapNo", "recap_no"},
        {"dbTSITotal", "tsi_total"},
        {"dbPremiTotal", "premi_total"},
        {"stDataAmount", "data_amount"},
        {"stReinsurerNote", "reinsurer_note"},
        {"stReinsurerNoteObject", "reinsurer_note_objek"},
    
    };

    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stInsurancePolicyID;
    private String stInsurancePolicyObjectID;
    private String stPolicyNo;
    private String stOrderNo;
    private String stNama;
    private Date dtTanggalLahir;
    private Date dtPeriodeAwal;
    private Date dtPeriodeAkhir;
    private String stUsia;
    private BigDecimal dbTSI;
    private BigDecimal dbPremi;
    private String stTreaty;
    private String stStatus;
    private String stFilePhysic;
    private String stRecapNo;
    private BigDecimal dbTSITotal;
    private BigDecimal dbPremiTotal;
    private String stDataAmount;
    private String stReinsurerNote;
    private String stRecapNoObject;
    private String stReinsurerNoteObject;
    private String stBulan;
    private String stLama;
    private String stKategori;
    private String stPerluasan;
    private BigDecimal dbRate;
    private BigDecimal dbPremiPerluasan;
    private String stPekerjaan;

    public String getStPekerjaan() {
        return stPekerjaan;
    }

    public void setStPekerjaan(String stPekerjaan) {
        this.stPekerjaan = stPekerjaan;
    }

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
     * @return the stInsurancePolicyObjectID
     */
    public String getStInsurancePolicyObjectID() {
        return stInsurancePolicyObjectID;
    }

    /**
     * @param stInsurancePolicyObjectID the stInsurancePolicyObjectID to set
     */
    public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
        this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
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
     * @return the stOrderNo
     */
    public String getStOrderNo() {
        return stOrderNo;
    }

    /**
     * @param stOrderNo the stOrderNo to set
     */
    public void setStOrderNo(String stOrderNo) {
        this.stOrderNo = stOrderNo;
    }

    /**
     * @return the stNama
     */
    public String getStNama() {
        return stNama;
    }

    /**
     * @param stNama the stNama to set
     */
    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    /**
     * @return the dtTanggalLahir
     */
    public Date getDtTanggalLahir() {
        return dtTanggalLahir;
    }

    /**
     * @param dtTanggalLahir the dtTanggalLahir to set
     */
    public void setDtTanggalLahir(Date dtTanggalLahir) {
        this.dtTanggalLahir = dtTanggalLahir;
    }

    /**
     * @return the dtPeriodeAwal
     */
    public Date getDtPeriodeAwal() {
        return dtPeriodeAwal;
    }

    /**
     * @param dtPeriodeAwal the dtPeriodeAwal to set
     */
    public void setDtPeriodeAwal(Date dtPeriodeAwal) {
        this.dtPeriodeAwal = dtPeriodeAwal;
    }

    /**
     * @return the dtPeriodeAkhir
     */
    public Date getDtPeriodeAkhir() {
        return dtPeriodeAkhir;
    }

    /**
     * @param dtPeriodeAkhir the dtPeriodeAkhir to set
     */
    public void setDtPeriodeAkhir(Date dtPeriodeAkhir) {
        this.dtPeriodeAkhir = dtPeriodeAkhir;
    }

    /**
     * @return the stUsia
     */
    public String getStUsia() {
        return stUsia;
    }

    /**
     * @param stUsia the stUsia to set
     */
    public void setStUsia(String stUsia) {
        this.stUsia = stUsia;
    }

    /**
     * @return the dbTSI
     */
    public BigDecimal getDbTSI() {
        return dbTSI;
    }

    /**
     * @param dbTSI the dbTSI to set
     */
    public void setDbTSI(BigDecimal dbTSI) {
        this.dbTSI = dbTSI;
    }

    /**
     * @return the dbPremi
     */
    public BigDecimal getDbPremi() {
        return dbPremi;
    }

    /**
     * @param dbPremi the dbPremi to set
     */
    public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
    }

    /**
     * @return the stReinsurer
     */
    public String getStTreaty() {
        return stTreaty;
    }

    /**
     * @param stReinsurer the stReinsurer to set
     */
    public void setStTreaty(String stTreaty) {
        this.stTreaty = stTreaty;
    }

    /**
     * @return the stStatus
     */
    public String getStStatus() {
        return stStatus;
    }

    /**
     * @param stStatus the stStatus to set
     */
    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
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
     * @return the stRecapNo
     */
    public String getStRecapNo() {
        return stRecapNo;
    }

    /**
     * @param stRecapNo the stRecapNo to set
     */
    public void setStRecapNo(String stRecapNo) {
        this.stRecapNo = stRecapNo;
    }

    /**
     * @return the dbTSITotal
     */
    public BigDecimal getDbTSITotal() {
        return dbTSITotal;
    }

    /**
     * @param dbTSITotal the dbTSITotal to set
     */
    public void setDbTSITotal(BigDecimal dbTSITotal) {
        this.dbTSITotal = dbTSITotal;
    }

    /**
     * @return the dbPremiTotal
     */
    public BigDecimal getDbPremiTotal() {
        return dbPremiTotal;
    }

    /**
     * @param dbPremiTotal the dbPremiTotal to set
     */
    public void setDbPremiTotal(BigDecimal dbPremiTotal) {
        this.dbPremiTotal = dbPremiTotal;
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
     * @return the stReinsurerNote
     */
    public String getStReinsurerNote() {
        return stReinsurerNote;
    }

    /**
     * @param stReinsurerNote the stReinsurerNote to set
     */
    public void setStReinsurerNote(String stReinsurerNote) {
        this.stReinsurerNote = stReinsurerNote;
    }

    /**
     * @return the stRecapNoObject
     */
    public String getStRecapNoObject() {
        return stRecapNoObject;
    }

    /**
     * @param stRecapNoObject the stRecapNoObject to set
     */
    public void setStRecapNoObject(String stRecapNoObject) {
        this.stRecapNoObject = stRecapNoObject;
    }

    /**
     * @return the stReinsurerNoteObject
     */
    public String getStReinsurerNoteObject() {
        return stReinsurerNoteObject;
    }

    /**
     * @param stReinsurerNoteObject the stReinsurerNoteObject to set
     */
    public void setStReinsurerNoteObject(String stReinsurerNoteObject) {
        this.stReinsurerNoteObject = stReinsurerNoteObject;
    }

    /**
     * @return the stInsurancePolicyID
     */
    public String getStInsurancePolicyID() {
        return stInsurancePolicyID;
    }

    /**
     * @param stInsurancePolicyID the stInsurancePolicyID to set
     */
    public void setStInsurancePolicyID(String stInsurancePolicyID) {
        this.stInsurancePolicyID = stInsurancePolicyID;
    }

    /**
     * @return the stBulan
     */
    public String getStBulan() {
        return stBulan;
    }

    /**
     * @param stBulan the stBulan to set
     */
    public void setStBulan(String stBulan) {
        this.stBulan = stBulan;
    }

    /**
     * @return the stLama
     */
    public String getStLama() {
        return stLama;
    }

    /**
     * @param stLama the stLama to set
     */
    public void setStLama(String stLama) {
        this.stLama = stLama;
    }

    /**
     * @return the stKategori
     */
    public String getStKategori() {
        return stKategori;
    }

    /**
     * @param stKategori the stKategori to set
     */
    public void setStKategori(String stKategori) {
        this.stKategori = stKategori;
    }

    /**
     * @return the stPerluasan
     */
    public String getStPerluasan() {
        return stPerluasan;
    }

    /**
     * @param stPerluasan the stPerluasan to set
     */
    public void setStPerluasan(String stPerluasan) {
        this.stPerluasan = stPerluasan;
    }

    /**
     * @return the dbRate
     */
    public BigDecimal getDbRate() {
        return dbRate;
    }

    /**
     * @param dbRate the dbRate to set
     */
    public void setDbRate(BigDecimal dbRate) {
        this.dbRate = dbRate;
    }

    /**
     * @return the dbPremiPerluasan
     */
    public BigDecimal getDbPremiPerluasan() {
        return dbPremiPerluasan;
    }

    /**
     * @param dbPremiPerluasan the dbPremiPerluasan to set
     */
    public void setDbPremiPerluasan(BigDecimal dbPremiPerluasan) {
        this.dbPremiPerluasan = dbPremiPerluasan;
    }
}
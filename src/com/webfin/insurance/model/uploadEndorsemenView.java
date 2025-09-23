/***********************************************************************
 * Module:  com.crux.login.model.uploadEndorsemenView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.UserSession;
import com.crux.file.FileView;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.UserLogView;
import com.crux.login.model.UserRoleView;
import com.webfin.gl.model.GLCostCenterView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.system.region.model.RegionView;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

public class uploadEndorsemenView extends DTO implements RecordAudit
{
    private HashMap attributes = new HashMap();
    
    public void setAttribute(String x, Object o)
    {
        attributes.put(x,o);
    }
    
    public Object getAttribute(String x)
    {
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
    public static String tableName = "ins_upload_detail";
    
    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_upload_dtl_id*pk*nd"},
        {"stInsuranceUploadID", "ins_upload_id"},
        {"stPolicyID", "pol_id"},
        {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
        {"stPolicyNo", "pol_no"},
        {"stOrderNo", "order_no"},
        {"stNama", "nama"},
        {"dtTanggalLahir", "tanggal_lahir"},
        {"dtPeriodeAwal", "periode_awal"},
        {"dtPeriodeAkhir", "periode_akhir"},
        {"stUsia", "usia"},
        {"dbTSI", "tsi"},
        {"dbRateJual", "rate_jual"},
        {"dbPremi", "premi"},
        {"stKoasuransi", "koasuransi"},
        {"stKoasuransiMenjadi","koas_menjadi"},
        {"dbRateKoas", "rate_koas"},
        {"dbPremiKoas", "premi_koas"},
        {"dbRateKomisiKoas", "rate_komisi_koas"},
        {"dbKomisiKoas", "komisi_koas"},
        {"stStatus", "status"},
        {"stFilePhysic", "file_physic"},
        {"stRecapNo", "recap_no"},
        {"dbTSITotal", "tsi_total"},
        {"dbPremiTotal", "premi_total"},
        {"stDataAmount", "data_amount"},
        {"dbPremiKoasTotal", "premi_koas_total"},
        {"dbKomisiKoasTotal", "komisi_koas_total"},
        {"stEndorseNote", "endorse_note"},
        {"stRecapNoObject", "recap_no_objek"},
        {"stEndorseNoteObject", "endorse_note_objek"},
        {"stAutoApproveFlag", "auto_approve_flag"},
        {"stApprovedWho", "approved_who"},
        {"dtPolicyDate", "policy_date"},


    };

    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stPolicyID;
    private String stInsurancePolicyObjectID;
    private String stPolicyNo;
    private String stOrderNo;
    private String stNama;
    private Date dtTanggalLahir;
    private Date dtPeriodeAwal;
    private Date dtPeriodeAkhir;
    private String stUsia;
    private BigDecimal dbTSI;
    private BigDecimal dbRateJual;
    private BigDecimal dbPremi;
    private String stKoasuransi;
    private String stKoasuransiMenjadi;
    private BigDecimal dbRateKoas;
    private BigDecimal dbPremiKoas;
    private BigDecimal dbRateKomisiKoas;
    private BigDecimal dbKomisiKoas;
    private String stStatus;
    private String stFilePhysic;
    private String stRecapNo;
    private BigDecimal dbTSITotal;
    private BigDecimal dbPremiTotal;
    private String stDataAmount;
    private BigDecimal dbPremiKoasTotal;
    private BigDecimal dbKomisiKoasTotal;
    private String stEndorseNote;
    private String stRecapNoObject;
    private String stEndorseNoteObject;
    private String stAutoApproveFlag;
    private String stApprovedWho;
    private Date dtPolicyDate;

    public FileView getFile()
    {

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
     * @return the dbRateJual
     */
    public BigDecimal getDbRateJual() {
        return dbRateJual;
    }

    /**
     * @param dbRateJual the dbRateJual to set
     */
    public void setDbRateJual(BigDecimal dbRateJual) {
        this.dbRateJual = dbRateJual;
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
     * @return the stKoasuransi
     */
    public String getStKoasuransi() {
        return stKoasuransi;
    }

    /**
     * @param stKoasuransi the stKoasuransi to set
     */
    public void setStKoasuransi(String stKoasuransi) {
        this.stKoasuransi = stKoasuransi;
    }

    /**
     * @return the stKoasuransiMenjadi
     */
    public String getStKoasuransiMenjadi() {
        return stKoasuransiMenjadi;
    }

    /**
     * @param stKoasuransiMenjadi the stKoasuransiMenjadi to set
     */
    public void setStKoasuransiMenjadi(String stKoasuransiMenjadi) {
        this.stKoasuransiMenjadi = stKoasuransiMenjadi;
    }

    /**
     * @return the dbRateKoas
     */
    public BigDecimal getDbRateKoas() {
        return dbRateKoas;
    }

    /**
     * @param dbRateKoas the dbRateKoas to set
     */
    public void setDbRateKoas(BigDecimal dbRateKoas) {
        this.dbRateKoas = dbRateKoas;
    }

    /**
     * @return the dbPremiKoas
     */
    public BigDecimal getDbPremiKoas() {
        return dbPremiKoas;
    }

    /**
     * @param dbPremiKoas the dbPremiKoas to set
     */
    public void setDbPremiKoas(BigDecimal dbPremiKoas) {
        this.dbPremiKoas = dbPremiKoas;
    }

    /**
     * @return the dbRateKomisiKoas
     */
    public BigDecimal getDbRateKomisiKoas() {
        return dbRateKomisiKoas;
    }

    /**
     * @param dbRateKomisiKoas the dbRateKomisiKoas to set
     */
    public void setDbRateKomisiKoas(BigDecimal dbRateKomisiKoas) {
        this.dbRateKomisiKoas = dbRateKomisiKoas;
    }

    /**
     * @return the dbKomisiKoas
     */
    public BigDecimal getDbKomisiKoas() {
        return dbKomisiKoas;
    }

    /**
     * @param dbKomisiKoas the dbKomisiKoas to set
     */
    public void setDbKomisiKoas(BigDecimal dbKomisiKoas) {
        this.dbKomisiKoas = dbKomisiKoas;
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
    public String getstFilePhysic() {
        return getStFilePhysic();
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setstFilePhysic(String stFilePhysic) {
        this.setStFilePhysic(stFilePhysic);
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
     * @return the dbPremiKoasTotal
     */
    public BigDecimal getDbPremiKoasTotal() {
        return dbPremiKoasTotal;
    }

    /**
     * @param dbPremiKoasTotal the dbPremiKoasTotal to set
     */
    public void setDbPremiKoasTotal(BigDecimal dbPremiKoasTotal) {
        this.dbPremiKoasTotal = dbPremiKoasTotal;
    }

    /**
     * @return the dbKomisiKoasTotal
     */
    public BigDecimal getDbKomisiKoasTotal() {
        return dbKomisiKoasTotal;
    }

    /**
     * @param dbKomisiKoasTotal the dbKomisiKoasTotal to set
     */
    public void setDbKomisiKoasTotal(BigDecimal dbKomisiKoasTotal) {
        this.dbKomisiKoasTotal = dbKomisiKoasTotal;
    }

    /**
     * @return the stEndorseNote
     */
    public String getStEndorseNote() {
        return stEndorseNote;
    }

    /**
     * @param stEndorseNote the stEndorseNote to set
     */
    public void setStEndorseNote(String stEndorseNote) {
        this.stEndorseNote = stEndorseNote;
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
     * @return the stEndorseNoteObject
     */
    public String getStEndorseNoteObject() {
        return stEndorseNoteObject;
    }

    /**
     * @param stEndorseNoteObject the stEndorseNoteObject to set
     */
    public void setStEndorseNoteObject(String stEndorseNoteObject) {
        this.stEndorseNoteObject = stEndorseNoteObject;
    }

    /**
     * @return the stAutoApproveFlag
     */
    public String getStAutoApproveFlag() {
        return stAutoApproveFlag;
    }

    /**
     * @param stAutoApproveFlag the stAutoApproveFlag to set
     */
    public void setStAutoApproveFlag(String stAutoApproveFlag) {
        this.stAutoApproveFlag = stAutoApproveFlag;
    }

    /**
     * @return the stApprovedWho
     */
    public String getStApprovedWho() {
        return stApprovedWho;
    }

    /**
     * @param stApprovedWho the stApprovedWho to set
     */
    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
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

}

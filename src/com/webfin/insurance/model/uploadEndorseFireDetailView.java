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

public class uploadEndorseFireDetailView extends DTO implements RecordAudit
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
    ins_upload_dtl_id bigint NOT NULL,
  ins_upload_id bigint,
  pol_id bigint,
  ins_pol_obj_id bigint,
  pol_no character varying(32),
  order_no bigint,
  nama character varying(255),
  tsi numeric,
  premi numeric,
  feebase numeric,
  komisi numeric,
  brokerfee numeric,
  biayapolis numeric,
  materai numeric,
  status character varying(32),
  file_id character varying(255),
  recap_no character varying(255),
  tsi_total numeric,
  premi_total numeric,
  file_physic bigint,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),

  auto_approve_flag character varying(1),
  approved_who character varying(32),
  policy_date timestamp without time zone,
*/
    public static String tableName = "ins_upload_endorse_fire_detail";
    
    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_upload_dtl_id*pk*nd"},
        {"stInsuranceUploadID", "ins_upload_id"},
        {"stPolicyID", "pol_id"},
        {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
        {"stPolicyNo", "pol_no"},
        {"stOrderNo", "order_no"},
        {"stNama", "nama"},
        {"dbTSI", "tsi"},
        {"dbPremi", "premi"},
        {"dbFeeBase", "feebase"},
        {"dbKomisi", "komisi"},
        {"dbBrokerFee", "brokerfee"},
        {"dbBiayaPolis", "biayapolis"},
        {"dbMaterai", "materai"},
        {"stStatus", "status"},
        {"stAutoApproveFlag", "auto_approve_flag"},
        {"stApprovedWho", "approved_who"},
        {"dtPolicyDate", "policy_date"},
        {"stEndorseNotes", "endorse_note"},
        {"dtRestitutionDate", "restitution_date"},
        {"stSisaJangkaWaktu", "sisa_jangka_waktu"},
        {"dbRestitutionPct", "restitution_pct"},
        {"stReferenceNo", "reference_no"},
        {"dbTSIAwal", "tsi_awal"},
        {"dbPremiAwal", "premi_awal"},
        {"dtTanggalLahir", "tanggal_lahir"},
        {"dtPeriodeAwal", "periode_awal"},
        {"dtPeriodeAkhir", "periode_akhir"},
        {"stBypassValidasiFlag", "bypass_validasi_f"},
        {"stJenisEndorse", "jenis_endorse"},
        {"dbFeeBasePct", "feebase_pct"},
        {"dbKomisiPct", "komisi_pct"},
        {"dbBrokerFeePct", "brokerfee_pct"},
        {"stAlamatRisiko", "alamat_risiko"},

    };

    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stKodeBank;
    private String stEntityID;
    private String stNomorPerjanjianKredit;
    private String stNomorBuktiBayar;
    private Date dtTanggalBayar;
    private BigDecimal dbPremiAmount;
    private String stValidFlag;
    private DTOList dataTeks;
    private String stKeterangan;

     private String stPolicyID;
     private String stInsurancePolicyObjectID;
     private String stPolicyNo;
     private String stOrderNo;
     private String stNama;
     private BigDecimal dbTSI;
     private BigDecimal dbPremi;
     private BigDecimal dbFeeBase;
     private BigDecimal dbKomisi;
     private BigDecimal dbBrokerFee;
     private BigDecimal dbBiayaPolis;
     private BigDecimal dbMaterai;
     private String stStatus;
     private String stAutoApproveFlag;
     private String stApprovedWho;
     private Date dtPolicyDate;
     private String stEndorseNotes;
     private Date dtRestitutionDate;
     private String stSisaJangkaWaktu;
     private BigDecimal dbRestitutionPct;
     private String stReferenceNo;
     private BigDecimal dbTSIAwal;
     private BigDecimal dbPremiAwal;
     private Date dtTanggalLahir;
     private Date dtPeriodeAwal;
     private Date dtPeriodeAkhir;
     private String stBypassValidasiFlag;
     private String stJenisEndorse;
     private BigDecimal dbFeeBasePct;
     private String stAlamatRisiko;

    public String getStAlamatRisiko() {
        return stAlamatRisiko;
    }

    public void setStAlamatRisiko(String stAlamatRisiko) {
        this.stAlamatRisiko = stAlamatRisiko;
    }

    public BigDecimal getDbBrokerFeePct() {
        return dbBrokerFeePct;
    }

    public void setDbBrokerFeePct(BigDecimal dbBrokerFeePct) {
        this.dbBrokerFeePct = dbBrokerFeePct;
    }

    public BigDecimal getDbFeeBasePct() {
        return dbFeeBasePct;
    }

    public void setDbFeeBasePct(BigDecimal dbFeeBasePct) {
        this.dbFeeBasePct = dbFeeBasePct;
    }

    public BigDecimal getDbKomisiPct() {
        return dbKomisiPct;
    }

    public void setDbKomisiPct(BigDecimal dbKomisiPct) {
        this.dbKomisiPct = dbKomisiPct;
    }
     private BigDecimal dbKomisiPct;
     private BigDecimal dbBrokerFeePct;

    public String getStJenisEndorse() {
        return stJenisEndorse;
    }

    public void setStJenisEndorse(String stJenisEndorse) {
        this.stJenisEndorse = stJenisEndorse;
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
     * @return the stKodeBank
     */
    public String getStKodeBank() {
        return stKodeBank;
    }

    /**
     * @param stKodeBank the stKodeBank to set
     */
    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    /**
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    /**
     * @return the stNomorPerjanjianKredit
     */
    public String getStNomorPerjanjianKredit() {
        return stNomorPerjanjianKredit;
    }

    /**
     * @param stNomorPerjanjianKredit the stNomorPerjanjianKredit to set
     */
    public void setStNomorPerjanjianKredit(String stNomorPerjanjianKredit) {
        this.stNomorPerjanjianKredit = stNomorPerjanjianKredit;
    }

    /**
     * @return the stNomorBuktiBayar
     */
    public String getStNomorBuktiBayar() {
        return stNomorBuktiBayar;
    }

    /**
     * @param stNomorBuktiBayar the stNomorBuktiBayar to set
     */
    public void setStNomorBuktiBayar(String stNomorBuktiBayar) {
        this.stNomorBuktiBayar = stNomorBuktiBayar;
    }

    /**
     * @return the dtTanggalBayar
     */
    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    /**
     * @param dtTanggalBayar the dtTanggalBayar to set
     */
    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }

    /**
     * @return the dbPremiAmount
     */
    public BigDecimal getDbPremiAmount() {
        return dbPremiAmount;
    }

    /**
     * @param dbPremiAmount the dbPremiAmount to set
     */
    public void setDbPremiAmount(BigDecimal dbPremiAmount) {
        this.dbPremiAmount = dbPremiAmount;
    }

    public String getEntityID(String stKodeBank) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ent_id " +
                    "   from " +
                    "         ent_master " +
                    "   where" +
                    "      ref_gateway_code = ?");

            S.setParam(1,stKodeBank);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    /**
     * @return the stValidFlag
     */
    public String getStValidFlag() {
        return stValidFlag;
    }

    /**
     * @param stValidFlag the stValidFlag to set
     */
    public void setStValidFlag(String stValidFlag) {
        this.stValidFlag = stValidFlag;
    }

    private void loadDataTeks(String cabang) {
        try {
            if (dataTeks == null) {
                dataTeks = ListUtil.getDTOListFromQuery(
                        "select * from data_teks_masuk where no_rek_pinjaman = ? and cc_code = ? ",
                        new Object[]{stNomorPerjanjianKredit, cabang},
                        DataTeksMasukView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the dataTeks
     */
    public DTOList getDataTeks(String cabang) {
        loadDataTeks(cabang);
        return dataTeks;
    }

    /**
     * @param dataTeks the dataTeks to set
     */
    public void setDataTeks(DTOList dataTeks) {
        this.dataTeks = dataTeks;
    }

    public boolean isValid() {
        return Tools.isYes(stValidFlag);
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
     * @return the dbFeeBase
     */
    public BigDecimal getDbFeeBase() {
        return dbFeeBase;
    }

    /**
     * @param dbFeeBase the dbFeeBase to set
     */
    public void setDbFeeBase(BigDecimal dbFeeBase) {
        this.dbFeeBase = dbFeeBase;
    }

    /**
     * @return the dbKomisi
     */
    public BigDecimal getDbKomisi() {
        return dbKomisi;
    }

    /**
     * @param dbKomisi the dbKomisi to set
     */
    public void setDbKomisi(BigDecimal dbKomisi) {
        this.dbKomisi = dbKomisi;
    }

    /**
     * @return the dbBrokerFee
     */
    public BigDecimal getDbBrokerFee() {
        return dbBrokerFee;
    }

    /**
     * @param dbBrokerFee the dbBrokerFee to set
     */
    public void setDbBrokerFee(BigDecimal dbBrokerFee) {
        this.dbBrokerFee = dbBrokerFee;
    }

    /**
     * @return the dbBiayaPolis
     */
    public BigDecimal getDbBiayaPolis() {
        return dbBiayaPolis;
    }

    /**
     * @param dbBiayaPolis the dbBiayaPolis to set
     */
    public void setDbBiayaPolis(BigDecimal dbBiayaPolis) {
        this.dbBiayaPolis = dbBiayaPolis;
    }

    /**
     * @return the dbMaterai
     */
    public BigDecimal getDbMaterai() {
        return dbMaterai;
    }

    /**
     * @param dbMaterai the dbMaterai to set
     */
    public void setDbMaterai(BigDecimal dbMaterai) {
        this.dbMaterai = dbMaterai;
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

    /**
     * @return the stEndorseNotes
     */
    public String getStEndorseNotes() {
        return stEndorseNotes;
    }

    /**
     * @param stEndorseNotes the stEndorseNotes to set
     */
    public void setStEndorseNotes(String stEndorseNotes) {
        this.stEndorseNotes = stEndorseNotes;
    }

    /**
     * @return the dtRestitutionDate
     */
    public Date getDtRestitutionDate() {
        return dtRestitutionDate;
    }

    /**
     * @param dtRestitutionDate the dtRestitutionDate to set
     */
    public void setDtRestitutionDate(Date dtRestitutionDate) {
        this.dtRestitutionDate = dtRestitutionDate;
    }

    /**
     * @return the stSisaJangkaWaktu
     */
    public String getStSisaJangkaWaktu() {
        return stSisaJangkaWaktu;
    }

    /**
     * @param stSisaJangkaWaktu the stSisaJangkaWaktu to set
     */
    public void setStSisaJangkaWaktu(String stSisaJangkaWaktu) {
        this.stSisaJangkaWaktu = stSisaJangkaWaktu;
    }

    /**
     * @return the dbRestitutionPct
     */
    public BigDecimal getDbRestitutionPct() {
        return dbRestitutionPct;
    }

    /**
     * @param dbRestitutionPct the dbRestitutionPct to set
     */
    public void setDbRestitutionPct(BigDecimal dbRestitutionPct) {
        this.dbRestitutionPct = dbRestitutionPct;
    }

    /**
     * @return the stReferenceNo
     */
    public String getStReferenceNo() {
        return stReferenceNo;
    }

    /**
     * @param stReferenceNo the stReferenceNo to set
     */
    public void setStReferenceNo(String stReferenceNo) {
        this.stReferenceNo = stReferenceNo;
    }

    /**
     * @return the dbTSIAwal
     */
    public BigDecimal getDbTSIAwal() {
        return dbTSIAwal;
    }

    /**
     * @param dbTSIAwal the dbTSIAwal to set
     */
    public void setDbTSIAwal(BigDecimal dbTSIAwal) {
        this.dbTSIAwal = dbTSIAwal;
    }

    /**
     * @return the dbPremiAwal
     */
    public BigDecimal getDbPremiAwal() {
        return dbPremiAwal;
    }

    /**
     * @param dbPremiAwal the dbPremiAwal to set
     */
    public void setDbPremiAwal(BigDecimal dbPremiAwal) {
        this.dbPremiAwal = dbPremiAwal;
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
     * @return the stBypassValidasiFlag
     */
    public String getStBypassValidasiFlag() {
        return stBypassValidasiFlag;
    }

    /**
     * @param stBypassValidasiFlag the stBypassValidasiFlag to set
     */
    public void setStBypassValidasiFlag(String stBypassValidasiFlag) {
        this.stBypassValidasiFlag = stBypassValidasiFlag;
    }

    public boolean isBypassValidasi(){
        return Tools.isYes(stBypassValidasiFlag);
    }

    public boolean isEndorseJangkaWaktu() {
        return "JANGKA_WAKTU".equalsIgnoreCase(getStJenisEndorse());
    }

}

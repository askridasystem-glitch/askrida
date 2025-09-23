/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceProdukView
 * Author:  Prasetyo Dwi P
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceKreasiView extends DTO {

   /*
  CREATE TABLE aba_kreasi
(
  norut bigint NOT NULL,
  pol_no character varying(32),
  nama character varying(32),
  umur character varying(32),
  tgl_lhr timestamp without time zone,
  tgl_cair timestamp without time zone,
  tgl_akhir timestamp without time zone,
  insured_amount numeric,
  rate_premi numeric,
  premi numeric,
  pol_id bigint,
  CONSTRAINT aba_kreasi_pk PRIMARY KEY (ins_pol_obj_id)
)
WITH OIDS;
ALTER TABLE aba_kreasi OWNER TO postgres;
   */

   public static String tableName = "aba_kreasi";

   public static String fieldMap[][] = {
   	   {"stInsuranceNoUrut", "norut"},
           {"stInsuranceNoPolis", "pol_no"},
           {"stInsuranceNama", "nama"},
           {"stInsuranceUmur", "umur"},
           {"dtTanggalLahir", "tgl_lhr"},
           {"dtTanggalCair", "tgl_cair"},
           {"dtTanggalAkhir", "tgl_akhir"},
           {"dbInsured", "insured_amount"},
           {"dbRatePremi", "rate_premi"},
           {"dbPremi", "premi"},
           {"stStatus", "status"},
           {"stCabang", "cc_code"},
           {"stPolicyID", "pol_id*pk*nd"},
           {"stKoasuransi", "koas"},
           {"stNoIdentitas", "no_identitas"},
           {"stNoRekeningPinjaman", "no_rek_pinjaman"},
           {"stNoPerjanjianKredit", "no_pk"},
           {"stEntityName", "entity_name"},
           {"dtTanggalAwalPK", "tgl_awal_pk"},
           {"dtTanggalAkhirPK", "tgl_akhir_pk"},

   };

    private String stInsuranceNoUrut;
    private String stInsuranceNoPolis;
    private String stInsuranceNama;
    private String stInsuranceUmur;
    private String stStatus;
    private String stCabang;
    private Date dtTanggalLahir;
    private Date dtTanggalCair;
    private Date dtTanggalAkhir;
    private BigDecimal dbInsured;
    private BigDecimal dbRatePremi;
    private BigDecimal dbPremi;
    private String stPolicyID;
    private String stKoasuransi;
    private String stNoIdentitas;
    private String stNoRekeningPinjaman;
    private String stNoPerjanjianKredit;
    private String stEntityName;
    private Date dtTanggalAwalPK;
    private Date dtTanggalAkhirPK;

    public Date getDtTanggalAkhirPK() {
        return dtTanggalAkhirPK;
    }

    public void setDtTanggalAkhirPK(Date dtTanggalAkhirPK) {
        this.dtTanggalAkhirPK = dtTanggalAkhirPK;
    }

    public Date getDtTanggalAwalPK() {
        return dtTanggalAwalPK;
    }

    public void setDtTanggalAwalPK(Date dtTanggalAwalPK) {
        this.dtTanggalAwalPK = dtTanggalAwalPK;
    }

    public String getStEntityName() {
        return stEntityName;
    }

    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }

    public String getStNoIdentitas() {
        return stNoIdentitas;
    }

    public void setStNoIdentitas(String stNoIdentitas) {
        this.stNoIdentitas = stNoIdentitas;
    }

    public String getStNoPerjanjianKredit() {
        return stNoPerjanjianKredit;
    }

    public void setStNoPerjanjianKredit(String stNoPerjanjianKredit) {
        this.stNoPerjanjianKredit = stNoPerjanjianKredit;
    }

    public String getStNoRekeningPinjaman() {
        return stNoRekeningPinjaman;
    }

    public void setStNoRekeningPinjaman(String stNoRekeningPinjaman) {
        this.stNoRekeningPinjaman = stNoRekeningPinjaman;
    }
    
    public String getStInsuranceNoUrut() {
        return stInsuranceNoUrut;
    }

    public void setStInsuranceNoUrut(String stInsuranceNoUrut) {
        this.stInsuranceNoUrut = stInsuranceNoUrut;
    }
    
    public String getStInsuranceNoPolis() {
        return stInsuranceNoPolis;
    }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis) {
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }
    
    public String getStInsuranceNama() {
        return stInsuranceNama;
    }

    public void setStInsuranceNama(String stInsuranceNama) {
        this.stInsuranceNama = stInsuranceNama;
    }
    
    public String getStInsuranceUmur() {
        return stInsuranceUmur;
    }

    public void setStInsuranceUmur(String stInsuranceUmur) {
        this.stInsuranceUmur = stInsuranceUmur;
    }
    
    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }
    
    public String getStCabang() {
        return stCabang;
    }

    public void setStCabang(String stCabang) {
        this.stCabang = stCabang;
    }
    
    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public Date getDtTanggalLahir() {
        return dtTanggalLahir;
    }

    public void setDtTanggalLahir(Date dtTanggalLahir) {
        this.dtTanggalLahir = dtTanggalLahir;
    }
    
    public Date getDtTanggalCair() {
        return dtTanggalCair;
    }

    public void setDtTanggalCair(Date dtTanggalCair) {
        this.dtTanggalCair = dtTanggalCair;
    }
    
    public Date getDtTanggalAkhir() {
        return dtTanggalAkhir;
    }

    public void setDtTanggalAkhir(Date dtTanggalAkhir) {
        this.dtTanggalAkhir = dtTanggalAkhir;
    }

    public BigDecimal getDbInsured() {
        return dbInsured;
    }

    public void setDbInsured(BigDecimal dbInsured) {
        this.dbInsured = dbInsured;
    }  
    
    public BigDecimal getDbRatePremi() {
        return dbRatePremi;
    }

    public void setDbRatePremi(BigDecimal dbRatePremi) {
        this.dbRatePremi = dbRatePremi;
    }  
    
    public BigDecimal getDbPremi() {
        return dbPremi;
    }

    public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
    }      

    public String getStKoasuransi()
    {
        return stKoasuransi;
    }

    public void setStKoasuransi(String stKoasuransi)
    {
        this.stKoasuransi = stKoasuransi;
    }
}
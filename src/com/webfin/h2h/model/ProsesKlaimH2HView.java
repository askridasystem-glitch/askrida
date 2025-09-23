/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceProdukView
 * Author:  Ahmad Rhodoni
 ***********************************************************************/

package com.webfin.h2h.model;


import com.crux.common.model.DTO;
import java.math.BigDecimal;
import java.util.Date;

public class ProsesKlaimH2HView extends DTO {

   /*

CREATE TABLE proses_data_klaim_h2h
(
  pol_no character varying(32),
  nama character varying(128),
  no_rek_pinjaman character varying(128),
  no_perjanjian_kredit character varying(128),
  no_identitas character varying(128),
  insured_amount numeric,
  claim_amount numeric,
  data_core text,
  no_urut text,
  claim_date timestamp without time zone,
  dla_date timestamp without time zone,
  proses_flag character varying(1),
  pol_id bigint,
  pla_no character varying(32),
  dla_no character varying(32)
)
   */

   public static String tableName = "proses_data_klaim_h2h";

   public static String fieldMap[][] = {
   	   {"stPolicyNo", "pol_no"},
           {"stNama", "nama"},
           {"stNomorRekeningPinjaman", "no_rek_pinjaman"},
           {"stNomorPK", "no_perjanjian_kredit"},
           {"stNomorIdentitas", "no_identitas"},
           {"dbInsuredAmount", "insured_amount"},
           {"dbClaimAmount", "claim_amount"},
           {"stNoUrut", "no_urut"},
           {"dtClaimDate", "claim_date"},
           {"dtDLADate", "dla_date"},
           {"dtTanggalPengajuan", "tgl_pengajuan"},

           {"stDLANo", "dla_no*n"},
           {"dbSubrogasiAmount", "nilai_subrogasi*n"},
           {"dbFeeSubrogasiAmount", "fee_subrogasi*n"},
           
   };

   private String stPolicyNo;
   private String stNama;
   private String stNomorRekeningPinjaman;
   private String stNomorPK;
   private String stNomorIdentitas;
   private BigDecimal dbInsuredAmount;
   private BigDecimal dbClaimAmount;
   private String stNoUrut;
   private Date dtClaimDate;
   private Date dtDLADate;
   private Date dtTanggalPengajuan;

   private String stDLANo;
   private BigDecimal dbSubrogasiAmount;
   private BigDecimal dbFeeSubrogasiAmount;

    public BigDecimal getDbFeeSubrogasiAmount() {
        return dbFeeSubrogasiAmount;
    }

    public void setDbFeeSubrogasiAmount(BigDecimal dbFeeSubrogasiAmount) {
        this.dbFeeSubrogasiAmount = dbFeeSubrogasiAmount;
    }

    public BigDecimal getDbSubrogasiAmount() {
        return dbSubrogasiAmount;
    }

    public void setDbSubrogasiAmount(BigDecimal dbSubrogasiAmount) {
        this.dbSubrogasiAmount = dbSubrogasiAmount;
    }

    public String getStDLANo() {
        return stDLANo;
    }

    public void setStDLANo(String stDLANo) {
        this.stDLANo = stDLANo;
    }

   

    public Date getDtTanggalPengajuan() {
        return dtTanggalPengajuan;
    }

    public void setDtTanggalPengajuan(Date dtTanggalPengajuan) {
        this.dtTanggalPengajuan = dtTanggalPengajuan;
    }

    public BigDecimal getDbClaimAmount() {
        return dbClaimAmount;
    }

    public void setDbClaimAmount(BigDecimal dbClaimAmount) {
        this.dbClaimAmount = dbClaimAmount;
    }

    public BigDecimal getDbInsuredAmount() {
        return dbInsuredAmount;
    }

    public void setDbInsuredAmount(BigDecimal dbInsuredAmount) {
        this.dbInsuredAmount = dbInsuredAmount;
    }

    public Date getDtClaimDate() {
        return dtClaimDate;
    }

    public void setDtClaimDate(Date dtClaimDate) {
        this.dtClaimDate = dtClaimDate;
    }

    public Date getDtDLADate() {
        return dtDLADate;
    }

    public void setDtDLADate(Date dtDLADate) {
        this.dtDLADate = dtDLADate;
    }

    public String getStNama() {
        return stNama;
    }

    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    public String getStNoUrut() {
        return stNoUrut;
    }

    public void setStNoUrut(String stNoUrut) {
        this.stNoUrut = stNoUrut;
    }

    public String getStNomorIdentitas() {
        return stNomorIdentitas;
    }

    public void setStNomorIdentitas(String stNomorIdentitas) {
        this.stNomorIdentitas = stNomorIdentitas;
    }

    public String getStNomorPK() {
        return stNomorPK;
    }

    public void setStNomorPK(String stNomorPK) {
        this.stNomorPK = stNomorPK;
    }

    public String getStNomorRekeningPinjaman() {
        return stNomorRekeningPinjaman;
    }

    public void setStNomorRekeningPinjaman(String stNomorRekeningPinjaman) {
        this.stNomorRekeningPinjaman = stNomorRekeningPinjaman;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }
   
   

}
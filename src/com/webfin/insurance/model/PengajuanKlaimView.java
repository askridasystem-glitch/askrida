/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:51:57 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;
import java.util.Date;

public class PengajuanKlaimView extends DTO implements RecordAudit {
   /*
     no_aplikasi character varying(30),
  no_polis character varying(20),
  tgl_klaim timestamp without time zone,
  penyebab character varying(1),
  nama_debitur character varying(50),
  alamat_debitur character varying(100),
  jumlah_klaim numeric,
  proses_flag character varying(1),
  tgl_proses timestamp without time zone,
  ins_clm_cause_id bigint
   */

   public static String tableName = "pengajuan_klaim";

   public static String fieldMap[][] = {
      {"stNomorAplikasi","no_aplikasi*pk"},
      {"stPolicyNo","no_polis"},
      {"dtTanggalKlaim","tgl_klaim"},
      {"stPenyebab","penyebab"},
      {"stNamaDebitur","nama_debitur"},
      {"stAlamatDebitur","alamat_debitur"},
      {"dbJumlahKlaim","jumlah_klaim"},
      {"stProsesFlag","proses_flag"},
      {"dtTanggalProses","tgl_proses"},
      {"stInsuranceClaimCauseID","ins_clm_cause_id"},
      {"stClaimLossID","claim_loss_id"},
      
   };

   private String stNomorAplikasi;
   private String stPolicyNo;
   private String stPenyebab;
   private String stNamaDebitur;
   private String stAlamatDebitur;
   private String stProsesFlag;
   private String stInsuranceClaimCauseID;
   private String stClaimLossID;

   private Date dtTanggalKlaim;
   private Date dtTanggalProses;

   private BigDecimal dbJumlahKlaim;

    /**
     * @return the stNomorAplikasi
     */
    public String getStNomorAplikasi() {
        return stNomorAplikasi;
    }

    /**
     * @param stNomorAplikasi the stNomorAplikasi to set
     */
    public void setStNomorAplikasi(String stNomorAplikasi) {
        this.stNomorAplikasi = stNomorAplikasi;
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
     * @return the stPenyebab
     */
    public String getStPenyebab() {
        return stPenyebab;
    }

    /**
     * @param stPenyebab the stPenyebab to set
     */
    public void setStPenyebab(String stPenyebab) {
        this.stPenyebab = stPenyebab;
    }

    /**
     * @return the stNamaDebitur
     */
    public String getStNamaDebitur() {
        return stNamaDebitur;
    }

    /**
     * @param stNamaDebitur the stNamaDebitur to set
     */
    public void setStNamaDebitur(String stNamaDebitur) {
        this.stNamaDebitur = stNamaDebitur;
    }

    /**
     * @return the stAlamatDebitur
     */
    public String getStAlamatDebitur() {
        return stAlamatDebitur;
    }

    /**
     * @param stAlamatDebitur the stAlamatDebitur to set
     */
    public void setStAlamatDebitur(String stAlamatDebitur) {
        this.stAlamatDebitur = stAlamatDebitur;
    }

    /**
     * @return the stProsesFlag
     */
    public String getStProsesFlag() {
        return stProsesFlag;
    }

    /**
     * @param stProsesFlag the stProsesFlag to set
     */
    public void setStProsesFlag(String stProsesFlag) {
        this.stProsesFlag = stProsesFlag;
    }

    /**
     * @return the stInsuranceClaimCauseID
     */
    public String getStInsuranceClaimCauseID() {
        return stInsuranceClaimCauseID;
    }

    /**
     * @param stInsuranceClaimCauseID the stInsuranceClaimCauseID to set
     */
    public void setStInsuranceClaimCauseID(String stInsuranceClaimCauseID) {
        this.stInsuranceClaimCauseID = stInsuranceClaimCauseID;
    }

    /**
     * @return the dtTanggalKlaim
     */
    public Date getDtTanggalKlaim() {
        return dtTanggalKlaim;
    }

    /**
     * @param dtTanggalKlaim the dtTanggalKlaim to set
     */
    public void setDtTanggalKlaim(Date dtTanggalKlaim) {
        this.dtTanggalKlaim = dtTanggalKlaim;
    }

    /**
     * @return the dtTanggalProses
     */
    public Date getDtTanggalProses() {
        return dtTanggalProses;
    }

    /**
     * @param dtTanggalProses the dtTanggalProses to set
     */
    public void setDtTanggalProses(Date dtTanggalProses) {
        this.dtTanggalProses = dtTanggalProses;
    }

    /**
     * @return the dbJumlahKlaim
     */
    public BigDecimal getDbJumlahKlaim() {
        return dbJumlahKlaim;
    }

    /**
     * @param dbJumlahKlaim the dbJumlahKlaim to set
     */
    public void setDbJumlahKlaim(BigDecimal dbJumlahKlaim) {
        this.dbJumlahKlaim = dbJumlahKlaim;
    }

    /**
     * @return the stClaimLossID
     */
    public String getStClaimLossID() {
        return stClaimLossID;
    }

    /**
     * @param stClaimLossID the stClaimLossID to set
     */
    public void setStClaimLossID(String stClaimLossID) {
        this.stClaimLossID = stClaimLossID;
    }

   

}

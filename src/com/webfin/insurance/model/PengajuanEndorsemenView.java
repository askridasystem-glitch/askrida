/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIView
 * Author:  Denny Mahendra
 * Created: Jan 27, 2006 5:33:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.BDUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;

public class PengajuanEndorsemenView extends DTO implements RecordAudit {
/*
data_id bigint,
  group_id bigint,
  cc_code character varying(8),
  region_id bigint,
  pol_type_id character varying(8),
  kode_bank character varying(32),
  nama character varying(255),
  usia bigint,
  tgl_lahir timestamp without time zone,
  tgl_awal timestamp without time zone,
  tgl_akhir timestamp without time zone,
  insured_amount numeric,
  premi_total numeric,
  tgl_proses timestamp without time zone,
  proses_flag character varying(1)
*/

   public static String tableName = "pengajuan_endorse";

   public static String fieldMap[][] = {
      {"stPolicyNo","pol_no*pk"},
      {"stKodeBank","kode_bank"},
      {"stNama","nama"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"dtTanggalProses","tgl_proses"},
      {"stProsesFlag","proses_flag"},
      {"stKategori","kategori"},
      {"stStatus","status"},
      {"dtTanggalTransfer","tgl_transfer"},
      {"stJenisEndorse","jenis_endorse"},
      {"stNomorUrut","no_urut"},
      {"stNomorRekeningPinjaman","no_rekening_pinjaman"},
      {"stApprovedWho","approved_who"},
      {"dbPremi","premi"},

   };
   
   private String stPolicyNo;
   private String stKodeBank;
   private String stNama;
   private String stCostCenterCode;
   private String stRegionID;
   private Date dtTanggalProses;
   private String stProsesFlag;
   private String stKategori;
   private String stStatus;
   private Date dtTanggalTransfer;
   private String stJenisEndorse;
   private String stNomorUrut;
   private String stApprovedWho;
   private BigDecimal dbPremi;

    public BigDecimal getDbPremi() {
        return dbPremi;
    }

    public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    public String getStNomorRekeningPinjaman() {
        return stNomorRekeningPinjaman;
    }

    public void setStNomorRekeningPinjaman(String stNomorRekeningPinjaman) {
        this.stNomorRekeningPinjaman = stNomorRekeningPinjaman;
    }

    public String getStNomorUrut() {
        return stNomorUrut;
    }

    public void setStNomorUrut(String stNomorUrut) {
        this.stNomorUrut = stNomorUrut;
    }
   private String stNomorRekeningPinjaman;

    public Date getDtTanggalProses() {
        return dtTanggalProses;
    }

    public void setDtTanggalProses(Date dtTanggalProses) {
        this.dtTanggalProses = dtTanggalProses;
    }

    public Date getDtTanggalTransfer() {
        return dtTanggalTransfer;
    }

    public void setDtTanggalTransfer(Date dtTanggalTransfer) {
        this.dtTanggalTransfer = dtTanggalTransfer;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStJenisEndorse() {
        return stJenisEndorse;
    }

    public void setStJenisEndorse(String stJenisEndorse) {
        this.stJenisEndorse = stJenisEndorse;
    }

    public String getStKategori() {
        return stKategori;
    }

    public void setStKategori(String stKategori) {
        this.stKategori = stKategori;
    }

    public String getStKodeBank() {
        return stKodeBank;
    }

    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    public String getStNama() {
        return stNama;
    }

    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    public String getStProsesFlag() {
        return stProsesFlag;
    }

    public void setStProsesFlag(String stProsesFlag) {
        this.stProsesFlag = stProsesFlag;
    }

    public String getStRegionID() {
        return stRegionID;
    }

    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public boolean isBatalDebiturEndorseMode() {
        return "1".equalsIgnoreCase(getStJenisEndorse());
    }

    public boolean isBatalTotalEndorseMode() {
        return "2".equalsIgnoreCase(getStJenisEndorse());
    }

}

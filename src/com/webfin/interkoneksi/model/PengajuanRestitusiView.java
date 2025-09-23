/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIView
 * Author:  Denny Mahendra
 * Created: Jan 27, 2006 5:33:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.interkoneksi.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;
import java.util.Date;

public class PengajuanRestitusiView extends DTO implements RecordAudit {
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

   public static String tableName = "ws_pengajuan_restitusi";

   public static String fieldMap[][] = {
      {"stID","id*pk"},
      {"stNomorPolis","no_polis"},
      {"stNomorUrut","no_urut"},
      {"stNomorLoan","nomor_loan"},
      {"stNomorPK","no_pk"},
      {"stNamaDebitur","nama_debitur"},
      {"dtTanggalLahir","tgl_lahir"},
      {"dbPlafondKredit","plafond_kredit"},
      {"dtTanggalAwal","tgl_awal_kredit"},
      {"dtTanggalJatuhTempo","tgl_jatuh_tempo"},
      {"dtTanggalRestitusi","tgl_restitusi"},
      {"stKodeBank","kd_bank"},
      {"stSisaJangkaWaktu","sisa_jangka_waktu"},
      {"dbRestitusiPct","restitusi_pct"},
      {"dbPremiRestitusi","premi_restitusi"},
      {"stTransactionNo","trx_no"},
      {"stEndorseWording","endorse_wording"},
   };

   private String stID;
   private String stNomorPolis;
   private String stNomorUrut;
   private String stNomorLoan;
   private String stNomorPK;
   private String stNamaDebitur;
   private Date dtTanggalLahir;
   private BigDecimal dbPlafondKredit;
   private Date dtTanggalAwal;
   private Date dtTanggalJatuhTempo;
   private Date dtTanggalRestitusi;
   private String stKodeBank;
   private String stSisaJangkaWaktu;
   private BigDecimal dbRestitusiPct;
   private BigDecimal dbPremiRestitusi;
   private String stTransactionNo;
   private String stEndorseWording;

    public String getStEndorseWording() {
        return stEndorseWording;
    }

    public void setStEndorseWording(String stEndorseWording) {
        this.stEndorseWording = stEndorseWording;
    }

    public String getStTransactionNo() {
        return stTransactionNo;
    }

    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }

    public BigDecimal getDbPlafondKredit() {
        return dbPlafondKredit;
    }

    public void setDbPlafondKredit(BigDecimal dbPlafondKredit) {
        this.dbPlafondKredit = dbPlafondKredit;
    }

    public BigDecimal getDbPremiRestitusi() {
        return dbPremiRestitusi;
    }

    public void setDbPremiRestitusi(BigDecimal dbPremiRestitusi) {
        this.dbPremiRestitusi = dbPremiRestitusi;
    }

    public BigDecimal getDbRestitusiPct() {
        return dbRestitusiPct;
    }

    public void setDbRestitusiPct(BigDecimal dbRestitusiPct) {
        this.dbRestitusiPct = dbRestitusiPct;
    }

    public Date getDtTanggalAwal() {
        return dtTanggalAwal;
    }

    public void setDtTanggalAwal(Date dtTanggalAwal) {
        this.dtTanggalAwal = dtTanggalAwal;
    }

    public Date getDtTanggalJatuhTempo() {
        return dtTanggalJatuhTempo;
    }

    public void setDtTanggalJatuhTempo(Date dtTanggalJatuhTempo) {
        this.dtTanggalJatuhTempo = dtTanggalJatuhTempo;
    }

    public Date getDtTanggalLahir() {
        return dtTanggalLahir;
    }

    public void setDtTanggalLahir(Date dtTanggalLahir) {
        this.dtTanggalLahir = dtTanggalLahir;
    }

    public Date getDtTanggalRestitusi() {
        return dtTanggalRestitusi;
    }

    public void setDtTanggalRestitusi(Date dtTanggalRestitusi) {
        this.dtTanggalRestitusi = dtTanggalRestitusi;
    }

    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
    }

    public String getStKodeBank() {
        return stKodeBank;
    }

    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    public String getStNamaDebitur() {
        return stNamaDebitur;
    }

    public void setStNamaDebitur(String stNamaDebitur) {
        this.stNamaDebitur = stNamaDebitur;
    }

    public String getStNomorLoan() {
        return stNomorLoan;
    }

    public void setStNomorLoan(String stNomorLoan) {
        this.stNomorLoan = stNomorLoan;
    }

    public String getStNomorPK() {
        return stNomorPK;
    }

    public void setStNomorPK(String stNomorPK) {
        this.stNomorPK = stNomorPK;
    }

    public String getStNomorPolis() {
        return stNomorPolis;
    }

    public void setStNomorPolis(String stNomorPolis) {
        this.stNomorPolis = stNomorPolis;
    }

    public String getStNomorUrut() {
        return stNomorUrut;
    }

    public void setStNomorUrut(String stNomorUrut) {
        this.stNomorUrut = stNomorUrut;
    }

    public String getStSisaJangkaWaktu() {
        return stSisaJangkaWaktu;
    }

    public void setStSisaJangkaWaktu(String stSisaJangkaWaktu) {
        this.stSisaJangkaWaktu = stSisaJangkaWaktu;
    }

}

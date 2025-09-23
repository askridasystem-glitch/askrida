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

public class PengajuanSubrogasiiView extends DTO implements RecordAudit {
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

   public static String tableName = "ws_subrogasi";

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
      {"dbNilaiSubrogasi","nilai_subrogasi"},
      {"dtTanggalPengajuan","tgl_pengajuan"},
      {"dtTanggalBayarSubrogasi","tgl_bayar_subrogasi"},
      {"stNomorRekening","nomor_rekening"},
      {"stNomorReferensiBayar","no_referensi_bayar"},
      {"dbJumlahFee","jumlah_fee"},
      {"dbOutstandingSubrogasi","outstanding_subrogasi"},
      {"stTransactionNo","trx_no"},



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

   private BigDecimal dbNilaiSubrogasi;
   private Date dtTanggalPengajuan;

    public BigDecimal getDbJumlahFee() {
        return dbJumlahFee;
    }

    public void setDbJumlahFee(BigDecimal dbJumlahFee) {
        this.dbJumlahFee = dbJumlahFee;
    }

    public BigDecimal getDbNilaiSubrogasi() {
        return dbNilaiSubrogasi;
    }

    public void setDbNilaiSubrogasi(BigDecimal dbNilaiSubrogasi) {
        this.dbNilaiSubrogasi = dbNilaiSubrogasi;
    }

    public BigDecimal getDbOutstandingSubrogasi() {
        return dbOutstandingSubrogasi;
    }

    public void setDbOutstandingSubrogasi(BigDecimal dbOutstandingSubrogasi) {
        this.dbOutstandingSubrogasi = dbOutstandingSubrogasi;
    }

    public Date getDtTanggalBayarSubrogasi() {
        return dtTanggalBayarSubrogasi;
    }

    public void setDtTanggalBayarSubrogasi(Date dtTanggalBayarSubrogasi) {
        this.dtTanggalBayarSubrogasi = dtTanggalBayarSubrogasi;
    }

    public Date getDtTanggalPengajuan() {
        return dtTanggalPengajuan;
    }

    public void setDtTanggalPengajuan(Date dtTanggalPengajuan) {
        this.dtTanggalPengajuan = dtTanggalPengajuan;
    }

    public String getStNomorReferensiBayar() {
        return stNomorReferensiBayar;
    }

    public void setStNomorReferensiBayar(String stNomorReferensiBayar) {
        this.stNomorReferensiBayar = stNomorReferensiBayar;
    }

    public String getStNomorRekening() {
        return stNomorRekening;
    }

    public void setStNomorRekening(String stNomorRekening) {
        this.stNomorRekening = stNomorRekening;
    }

    public String getStTransactionNo() {
        return stTransactionNo;
    }

    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }
   private Date dtTanggalBayarSubrogasi;
   private String stNomorRekening;
   private String stNomorReferensiBayar;
   private BigDecimal dbJumlahFee;
   private BigDecimal dbOutstandingSubrogasi;
   private String stTransactionNo;


    public BigDecimal getDbPlafondKredit() {
        return dbPlafondKredit;
    }

    public void setDbPlafondKredit(BigDecimal dbPlafondKredit) {
        this.dbPlafondKredit = dbPlafondKredit;
    }

    

    public Date getDtTanggalAwal() {
        return dtTanggalAwal;
    }

    public void setDtTanggalAwal(Date dtTanggalAwal) {
        this.dtTanggalAwal = dtTanggalAwal;
    }

    

    public Date getDtTanggalLahir() {
        return dtTanggalLahir;
    }

    public void setDtTanggalLahir(Date dtTanggalLahir) {
        this.dtTanggalLahir = dtTanggalLahir;
    }

    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
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

}

/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:51:57 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.interkoneksi.model;

import com.webfin.insurance.model.*;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
//import com.webfin.h2h.model.DokumenKlaimH2HView;

import java.math.BigDecimal;
import java.util.Date;

public class PengajuanEndorseH2HView extends DTO implements RecordAudit {
    /*
    CREATE TABLE ws_pengajuan_endorse
    (
    no_pengajuan_endorse character varying(32) NOT NULL,
    nomor_loan character varying(128) NOT NULL,
    no_pk character varying(128) NOT NULL,
    tgl_pengajuan timestamp without time zone,
    premi numeric,
    kd_bank character varying(32),
    nama_debitur character varying(50),
    alamat_debitur character varying(200),
    tgl_lahir timestamp without time zone,
    no_ktp character varying(128),
    tgl_pk timestamp without time zone,
    plafond_kredit numeric,
    tgl_awal_kredit timestamp without time zone,
    tgl_jatuh_tempo timestamp without time zone,
    jenis_pekerjaan character varying(10),
    kode_produk character varying(6),
    create_date timestamp without time zone,
    no_polis character varying(32),
    tgl_polis timestamp without time zone,
    kode_instansi character varying(32),
    key_code character varying(128),
    request_json text,
    trx_no character varying(32),
    kode_endorse character varying(2),
    keterangan_endorse text,
    file_name character varying(255),
    file_type character varying(255),
    file_path character varying(255),
    file_size numeric,
    CONSTRAINT ws_pengajuan_endorse_pkey PRIMARY KEY (nomor_loan)
    )
     */

    public static String tableName = "ws_pengajuan_endorse";
    public static String fieldMap[][] = {
        {"stNomorPengajuanEndorse", "no_pengajuan_endorse*pk"},
        {"stNomorLoan", "nomor_loan"},
        {"stNomorPK", "no_pk"},
        {"dbPremi", "premi"},
        {"stKodeBank", "kd_bank"},
        {"stNamaDebitur", "nama_debitur"},
        {"stAlamatDebitur", "alamat_debitur"},
        {"dtTanggalLahir", "tgl_lahir"},
        {"stNomorKTP", "no_ktp"},
        {"dtTanggalPK", "tgl_pk"},
        {"dbPlafondKredit", "plafond_kredit"},
        {"dtTanggalAwal", "tgl_awal_kredit"},
        {"dtTanggalJatuhTempo", "tgl_jatuh_tempo"},
        {"stJenisPekerjaan", "jenis_pekerjaan"},
        {"stKodeProduk", "kode_produk"},
        {"stKeteranganEndorse", "keterangan_endorse"},
        {"stNoPolisLama", "no_polis_lama"},
        {"stTransactionNo", "trx_no"},};
    
    private String stNomorPengajuanEndorse;
    private String stNomorLoan;
    private String stNomorPK;
    private BigDecimal dbPremi;
    private String stKodeBank;
    private String stNamaDebitur;
    private String stAlamatDebitur;
    private Date dtTanggalLahir;
    private String stNomorKTP;
    private Date dtTanggalPK;
    private BigDecimal dbPlafondKredit;
    private Date dtTanggalAwal;
    private Date dtTanggalJatuhTempo;
    private String stJenisPekerjaan;
    private String stKodeProduk;
    private String stKeteranganEndorse;
    private String stNoPolisLama;

    public String getStTransactionNo() {
        return stTransactionNo;
    }

    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }
    private String stTransactionNo;

    public String getStNoPolisLama() {
        return stNoPolisLama;
    }

    public void setStNoPolisLama(String stNoPolisLama) {
        this.stNoPolisLama = stNoPolisLama;
    }

    public BigDecimal getDbPlafondKredit() {
        return dbPlafondKredit;
    }

    public void setDbPlafondKredit(BigDecimal dbPlafondKredit) {
        this.dbPlafondKredit = dbPlafondKredit;
    }

    public BigDecimal getDbPremi() {
        return dbPremi;
    }

    public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
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

    public Date getDtTanggalPK() {
        return dtTanggalPK;
    }

    public void setDtTanggalPK(Date dtTanggalPK) {
        this.dtTanggalPK = dtTanggalPK;
    }

    public String getStAlamatDebitur() {
        return stAlamatDebitur;
    }

    public void setStAlamatDebitur(String stAlamatDebitur) {
        this.stAlamatDebitur = stAlamatDebitur;
    }

    public String getStJenisPekerjaan() {
        return stJenisPekerjaan;
    }

    public void setStJenisPekerjaan(String stJenisPekerjaan) {
        this.stJenisPekerjaan = stJenisPekerjaan;
    }

    public String getStKeteranganEndorse() {
        return stKeteranganEndorse;
    }

    public void setStKeteranganEndorse(String stKeteranganEndorse) {
        this.stKeteranganEndorse = stKeteranganEndorse;
    }

    public String getStKodeBank() {
        return stKodeBank;
    }

    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    public String getStKodeProduk() {
        return stKodeProduk;
    }

    public void setStKodeProduk(String stKodeProduk) {
        this.stKodeProduk = stKodeProduk;
    }

    public String getStNamaDebitur() {
        return stNamaDebitur;
    }

    public void setStNamaDebitur(String stNamaDebitur) {
        this.stNamaDebitur = stNamaDebitur;
    }

    public String getStNomorKTP() {
        return stNomorKTP;
    }

    public void setStNomorKTP(String stNomorKTP) {
        this.stNomorKTP = stNomorKTP;
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

    public String getStNomorPengajuanEndorse() {
        return stNomorPengajuanEndorse;
    }

    public void setStNomorPengajuanEndorse(String stNomorPengajuanEndorse) {
        this.stNomorPengajuanEndorse = stNomorPengajuanEndorse;
    }
    private DTOList documents;
    /*
    public DTOList getDocuments() {
    if (documents == null && stNomorLoan != null)
    documents = loadDocuments(stNomorLoan);
    return documents;
    }

    private static DTOList loadDocuments(String stNomorAplikasi) {
    try {

    final DTOList l = ListUtil.getDTOListFromQuery(
    "   select ROW_NUMBER () OVER (ORDER BY kode_dokumen_askrida) as nomor,"+
    "   a.*,b.description " +
    "     from ws_dokumen_fire a " +
    "   left join ins_document_type b on a.kode_dokumen_askrida::bigint = b.ins_document_type_id "+
    "   where " +
    "      a.nomor_loan = ? order by kode_dokumen_askrida",
    new Object[]{stNomorAplikasi},
    DokumenFireH2HView.class
    );

    return l;

    } catch (Exception e) {
    throw new RuntimeException(e);
    }
    }

    public void setDocuments(DTOList documents) {
    this.documents = documents;
    }*/
}

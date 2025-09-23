/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:51:57 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.h2h.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.webfin.entity.model.EntityView;

import java.math.BigDecimal;
import java.util.Date;

public class PengajuanPolisKBGView extends DTO implements RecordAudit {
    /*
    CREATE TABLE pengajuan_polis_konstruksi
    (
    id_pengajuan_polis bigint NOT NULL DEFAULT nextval('id_pengajuan_polis_kbg'::regclass),
    no_aplikasi character varying(255),
    cc_code character varying(4),
    region_id bigint,
    jenis_asuransi bigint,
    no_polis character varying(255),
    tanggal_polis timestamp without time zone,
    kode_bank character varying(255),
    ent_id bigint,
    kode_principal character varying(255),
    kode_unik_principal character varying(255),
    nama_principal character varying(255),
    pejabat_principal character varying(255),
    jabatan_pejabat character varying(255),
    nama_obligee character varying(255),
    kategori_obligee character varying(255),
    nama_proyek character varying(255),
    nilai_proyek numeric,
    nomor_spkbg character varying(255),
    tanggal_spkbg timestamp without time zone,
    nomor_spk character varying(255),
    tanggal_spk timestamp without time zone,
    nomor_kontrak character varying(255),
    tanggal_kontrak timestamp without time zone,
    nomor_bg character varying(255),
    tanggal_bg timestamp without time zone,
    nomor_undangan character varying(255),
    tanggal_undangan timestamp without time zone,
    jenis_agunan character varying(255),
    nilai_taksasi_agunan numeric,
    keterangan character varying(255),
    nomor_rek_bank character varying(255),
    nomor_sppbj character varying(255),
    tanggal_sppbj timestamp without time zone,
    nomor_pho_bast character varying(255),
    tanggal_pho_bast timestamp without time zone,
    tanggal_awal timestamp without time zone,
    tanggal_akhir timestamp without time zone,
    jasa_jaminan numeric,
    biaya_admin numeric,
    biaya_materai numeric,
    nilai_kbg numeric,
    rate_premi numeric,
    premi numeric,
    kode_bayar character varying(255),
    nilai_bayar_premi numeric,
    tanggal_bayar timestamp without time zone,
    no_bukti_bayar character varying(255),
    penanggung_ent_id bigint,
    pejabat_bank character varying(255),
    jenis_kontrak integer,
    no_npwp character varying(225),
    komisi1_pct numeric,
    komisi1_amount numeric,
    komisi1_ent_id bigint,
    komisi2_pct numeric,
    komisi2_amount numeric,
    komisi2_ent_id bigint,
    feebase1_pct numeric,
    feebase1_amount numeric,
    feebase1_ent_id bigint,
    feebase2_pct numeric,
    feebase2_amount numeric,
    feebase2_ent_id bigint,
    discount_pct numeric,
    discount_amount numeric,
    feebase1_ppn_include_amount numeric,
    feebase1_ppn_exclude_amount numeric,
    feebase1_ppn_include_pct numeric,
    feebase1_ppn_exclude_pct numeric,
    brokerage_include_ppn_pct numeric,
    brokerage_include_ppn_amount numeric,
    brokerage_exclude_ppn_pct numeric,
    brokerage_exclude_ppn_amount numeric,
    brokerage_ent_id bigint,
    tgl_proses timestamp without time zone,
    proses_flag character varying(32),
    tgl_transfer timestamp without time zone,
    )
     */

    public static String tableName = "pengajuan_polis_kbg";

    public static String fieldMap[][] = {
        {"stDataID", "id_pengajuan_polis*pk"},
        {"stNomorAplikasi", "no_aplikasi"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stJenisAsuransi", "jenis_asuransi"},
        {"stPolicyNo", "no_polis"},
        {"dtTanggalPolis", "tanggal_polis"},
        {"stEntityID", "ent_id"},
        {"stKodePrincipal", "kode_principal"},
        {"stNamaPrincipal", "nama_principal"},
        {"stPejabatPrincipal", "pejabat_principal"},
        {"stJabatanPejabat", "jabatan_pejabat"},
        {"stNamaObligee", "nama_obligee"},
        {"stKategoriObligee", "kategori_obligee"},
        {"stNamaProyek", "nama_proyek"},
        {"dbNilaiProyek", "nilai_proyek"},
        {"stNomorSPKBG", "nomor_spkbg"},
        {"dtTanggalSPKBG", "tanggal_spkbg"},
        {"stNomorSPK", "nomor_spk"},
        {"dtTanggalSPK", "tanggal_spk"},
        {"stNomorKontrak", "nomor_kontrak"},
        {"dtTanggalKontrak", "tanggal_kontrak"},
        {"stNomorBG", "nomor_bg"},
        {"dtTanggalBG", "tanggal_bg"},
        {"stNomorUndangan", "nomor_undangan"},
        {"dtTanggalUndangan", "tanggal_undangan"},
        {"stPenanggungEntityID", "penanggung_ent_id"},
        {"stPejabatBank", "pejabat_bank"},
        {"dbNilaiKBG", "nilai_kbg"},
        {"dbRate", "rate_premi"},
        {"dbPremi", "premi"},
        {"dbBiayaAdmin", "biaya_admin"},
        {"dbBiayaMaterai", "biaya_materai"},
        {"stNomorRekKredit", "nomor_rekening_kredit"},
        {"dtTanggalPK", "tanggal_perjanjian_kredit"},
        {"dtTanggalAwal", "tanggal_awal"},
        {"dtTanggalAkhir", "tanggal_akhir"},
        {"stJenisAgunan", "jenis_agunan"},
        {"dbNilaiTaksasiAgunan", "nilai_taksasi_agunan"},
        {"stNoPK", "no_pk"},
        {"stCoverage", "coverage_penjaminan"},
        {"dbKomisi1Pct", "komisi1_pct"},
        {"dbKomisi1Amount", "komisi1_amount"},
        {"dbKomisi2Pct", "komisi2_pct"},
        {"dbKomisi2Amount", "komisi2_amount"},
        {"dbFeeBase1Pct", "feebase1_pct"},
        {"dbFeeBase1Amount", "feebase1_amount"},
        {"dbFeeBase2Pct", "feebase2_pct"},
        {"dbFeeBase2Amount", "feebase2_amount"},
        {"stKomisi1EntityID", "komisi1_ent_id"},
        {"stKomisi2EntityID", "komisi2_ent_id"},
        {"stFeeBase1EntityID", "feebase1_ent_id"},
        {"stFeeBase2EntityID", "feebase2_ent_id"},
        {"dbDiscountPct", "discount_pct"},
        {"dbDiscountAmount", "discount_amount"},
        {"dbFeeBasePPN1ExcludePct", "feebase1_ppn_exclude_pct"},
        {"dbFeeBasePPN1Include", "feebase1_ppn_include_amount"},
        {"dbFeeBasePPN1Exclude", "feebase1_ppn_exclude_amount"},
        {"dbBrokerageIncludePPNPct", "brokerage_include_ppn_pct"},
        {"dbBrokerageIncludePPNAmount", "brokerage_include_ppn_amount"},
        {"dbBrokerageExcludePPNPct", "brokerage_exclude_ppn_pct"},
        {"dbBrokerageExcludePPNAmount", "brokerage_exclude_ppn_amount"},
        {"stBrokerageEntityID", "brokerage_ent_id"},
        {"dtTanggalProses", "tgl_proses"},
        {"stProsesFlag", "proses_flag"},
        {"dtTanggalProses", "tgl_transfer"},
        {"stEntityName", "ent_name*n"},
        {"stNomorRekeningBank", "nomor_rek_bank"},
    
    };


    private String stDataID;
    private String stNomorAplikasi;
    private String stCostCenterCode;
    private String stRegionID;
    private String stPolicyNo;
    private Date dtTanggalPolis;
    private String stEntityID;
    private String stPenanggungEntityID;
    private String stPejabatBank;
    private String stPejabatPrincipal;
    private String stNamaObligee;
    private String stKategoriObligee;
    private BigDecimal dbNilaiKBG;
    private BigDecimal dbRate;
    private BigDecimal dbPremi;
    private BigDecimal dbBiayaAdmin;
    private String stNomorRekKredit;
    private Date dtTanggalPK;
    private Date dtTanggalAwal;
    private Date dtTanggalAkhir;
    private String stNamaProyek;
    private BigDecimal dbNilaiProyek;
    private String stNomorKontrak;
    private Date dtTanggalKontrak;
    private String stJenisAgunan;
    private BigDecimal dbNilaiTaksasiAgunan;
    private String stKodePrincipal;
    private String stNamaPrincipal;
    private String stJabatanPejabat;
    private String stNoPK;
    private String stEntityName;
    private String stCoverage;
    private BigDecimal dbSumInsured;
    private BigDecimal dbRatePct;
    private BigDecimal dbPremiFlexas;
    private BigDecimal dbKomisi1Pct;
    private BigDecimal dbKomisi1Amount;
    private BigDecimal dbKomisi2Pct;
    private BigDecimal dbKomisi2Amount;
    private BigDecimal dbFeeBase1Pct;
    private BigDecimal dbFeeBase1Amount;
    private BigDecimal dbFeeBase2Pct;
    private BigDecimal dbFeeBase2Amount;
    private String stKomisi1EntityID;
    private String stKomisi2EntityID;
    private String stFeeBase1EntityID;
    private String stFeeBase2EntityID;
    private BigDecimal dbDiscountPct;
    private BigDecimal dbDiscountAmount;
    private BigDecimal dbFeeBasePPN1Include;
    private BigDecimal dbFeeBasePPN1Exclude;
    private BigDecimal dbBrokerageIncludePPNPct;
    private BigDecimal dbBrokerageIncludePPNAmount;
    private BigDecimal dbBrokerageExcludePPNPct;
    private BigDecimal dbBrokerageExcludePPNAmount;
    private String stBrokerageEntityID;
    private Date dtTanggalProses;
    private String stProsesFlag;
    private BigDecimal dbFeeBasePPN1ExcludePct;

    private String stNomorSPKBG;
    private Date dtTanggalSPKBG;
    private String stNomorSPK;
    private Date dtTanggalSPK;
    private String stNomorBG;
    private Date dtTanggalBG;
    private String stNomorUndangan;
    private Date dtTanggalUndangan;
    private String stNomorRekeningBank;
    private BigDecimal dbBiayaMaterai;
    private String stJenisAsuransi;

    public BigDecimal getDbNilaiKBG() {
        return dbNilaiKBG;
    }

    public void setDbNilaiKBG(BigDecimal dbNilaiKBG) {
        this.dbNilaiKBG = dbNilaiKBG;
    }

    public Date getDtTanggalAkhir() {
        return dtTanggalAkhir;
    }

    public void setDtTanggalAkhir(Date dtTanggalAkhir) {
        this.dtTanggalAkhir = dtTanggalAkhir;
    }

    public Date getDtTanggalAwal() {
        return dtTanggalAwal;
    }

    public void setDtTanggalAwal(Date dtTanggalAwal) {
        this.dtTanggalAwal = dtTanggalAwal;
    }



    public String getStJenisAsuransi() {
        return stJenisAsuransi;
    }

    public void setStJenisAsuransi(String stJenisAsuransi) {
        this.stJenisAsuransi = stJenisAsuransi;
    }

    

    public BigDecimal getDbBiayaMaterai() {
        return dbBiayaMaterai;
    }

    public void setDbBiayaMaterai(BigDecimal dbBiayaMaterai) {
        this.dbBiayaMaterai = dbBiayaMaterai;
    }

    

    public String getStNomorRekeningBank() {
        return stNomorRekeningBank;
    }

    public void setStNomorRekeningBank(String stNomorRekeningBank) {
        this.stNomorRekeningBank = stNomorRekeningBank;
    }

    public Date getDtTanggalBG() {
        return dtTanggalBG;
    }

    public void setDtTanggalBG(Date dtTanggalBG) {
        this.dtTanggalBG = dtTanggalBG;
    }

    public Date getDtTanggalUndangan() {
        return dtTanggalUndangan;
    }

    public void setDtTanggalUndangan(Date dtTanggalUndangan) {
        this.dtTanggalUndangan = dtTanggalUndangan;
    }

    public String getStNomorBG() {
        return stNomorBG;
    }

    public void setStNomorBG(String stNomorBG) {
        this.stNomorBG = stNomorBG;
    }

    public String getStNomorUndangan() {
        return stNomorUndangan;
    }

    public void setStNomorUndangan(String stNomorUndangan) {
        this.stNomorUndangan = stNomorUndangan;
    }

    public Date getDtTanggalSPK() {
        return dtTanggalSPK;
    }

    public void setDtTanggalSPK(Date dtTanggalSPK) {
        this.dtTanggalSPK = dtTanggalSPK;
    }

    public String getStNomorSPK() {
        return stNomorSPK;
    }

    public void setStNomorSPK(String stNomorSPK) {
        this.stNomorSPK = stNomorSPK;
    }

    public Date getDtTanggalSPKBG() {
        return dtTanggalSPKBG;
    }

    public void setDtTanggalSPKBG(Date dtTanggalSPKBG) {
        this.dtTanggalSPKBG = dtTanggalSPKBG;
    }

    public String getStNomorSPKBG() {
        return stNomorSPKBG;
    }

    public void setStNomorSPKBG(String stNomorSPKBG) {
        this.stNomorSPKBG = stNomorSPKBG;
    }



    public String getStPejabatPrincipal() {
        return stPejabatPrincipal;
    }

    public void setStPejabatPrincipal(String stPejabatPrincipal) {
        this.stPejabatPrincipal = stPejabatPrincipal;
    }

    public BigDecimal getDbFeeBasePPN1ExcludePct() {
        return dbFeeBasePPN1ExcludePct;
    }

    public void setDbFeeBasePPN1ExcludePct(BigDecimal dbFeeBasePPN1ExcludePct) {
        this.dbFeeBasePPN1ExcludePct = dbFeeBasePPN1ExcludePct;
    }
    private EntityView entity;

    public String getStCoverage() {
        return stCoverage;
    }

    public void setStCoverage(String stCoverage) {
        this.stCoverage = stCoverage;
    }

    public String getStEntityName() {
        return stEntityName;
    }

    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }

    public BigDecimal getDbBiayaAdmin() {
        return dbBiayaAdmin;
    }

    public void setDbBiayaAdmin(BigDecimal dbBiayaAdmin) {
        this.dbBiayaAdmin = dbBiayaAdmin;
    }

    public BigDecimal getDbNilaiProyek() {
        return dbNilaiProyek;
    }

    public void setDbNilaiProyek(BigDecimal dbNilaiProyek) {
        this.dbNilaiProyek = dbNilaiProyek;
    }

    public BigDecimal getDbNilaiTaksasiAgunan() {
        return dbNilaiTaksasiAgunan;
    }

    public void setDbNilaiTaksasiAgunan(BigDecimal dbNilaiTaksasiAgunan) {
        this.dbNilaiTaksasiAgunan = dbNilaiTaksasiAgunan;
    }

    public BigDecimal getDbPremi() {
        return dbPremi;
    }

    public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
    }

    public BigDecimal getDbRate() {
        return dbRate;
    }

    public void setDbRate(BigDecimal dbRate) {
        this.dbRate = dbRate;
    }

    public Date getDtTanggalKontrak() {
        return dtTanggalKontrak;
    }

    public void setDtTanggalKontrak(Date dtTanggalKontrak) {
        this.dtTanggalKontrak = dtTanggalKontrak;
    }

    public Date getDtTanggalPK() {
        return dtTanggalPK;
    }

    public void setDtTanggalPK(Date dtTanggalPK) {
        this.dtTanggalPK = dtTanggalPK;
    }

    public String getStJabatanPejabat() {
        return stJabatanPejabat;
    }

    public void setStJabatanPejabat(String stJabatanPejabat) {
        this.stJabatanPejabat = stJabatanPejabat;
    }

    public String getStJenisAgunan() {
        return stJenisAgunan;
    }

    public void setStJenisAgunan(String stJenisAgunan) {
        this.stJenisAgunan = stJenisAgunan;
    }

    public String getStKategoriObligee() {
        return stKategoriObligee;
    }

    public void setStKategoriObligee(String stKategoriObligee) {
        this.stKategoriObligee = stKategoriObligee;
    }

    public String getStKodePrincipal() {
        return stKodePrincipal;
    }

    public void setStKodePrincipal(String stKodePrincipal) {
        this.stKodePrincipal = stKodePrincipal;
    }

    public String getStNamaObligee() {
        return stNamaObligee;
    }

    public void setStNamaObligee(String stNamaObligee) {
        this.stNamaObligee = stNamaObligee;
    }

    public String getStNamaPrincipal() {
        return stNamaPrincipal;
    }

    public void setStNamaPrincipal(String stNamaPrincipal) {
        this.stNamaPrincipal = stNamaPrincipal;
    }

    public String getStNamaProyek() {
        return stNamaProyek;
    }

    public void setStNamaProyek(String stNamaProyek) {
        this.stNamaProyek = stNamaProyek;
    }

    public String getStNoPK() {
        return stNoPK;
    }

    public void setStNoPK(String stNoPK) {
        this.stNoPK = stNoPK;
    }

    public String getStNomorAplikasi() {
        return stNomorAplikasi;
    }

    public void setStNomorAplikasi(String stNomorAplikasi) {
        this.stNomorAplikasi = stNomorAplikasi;
    }

    public String getStNomorKontrak() {
        return stNomorKontrak;
    }

    public void setStNomorKontrak(String stNomorKontrak) {
        this.stNomorKontrak = stNomorKontrak;
    }

    public String getStNomorRekKredit() {
        return stNomorRekKredit;
    }

    public void setStNomorRekKredit(String stNomorRekKredit) {
        this.stNomorRekKredit = stNomorRekKredit;
    }

    public String getStPejabatBank() {
        return stPejabatBank;
    }

    public void setStPejabatBank(String stPejabatBank) {
        this.stPejabatBank = stPejabatBank;
    }

    public String getStPenanggungEntityID() {
        return stPenanggungEntityID;
    }

    public void setStPenanggungEntityID(String stPenanggungEntityID) {
        this.stPenanggungEntityID = stPenanggungEntityID;
    }

    public BigDecimal getDbBrokerageExcludePPNAmount() {
        return dbBrokerageExcludePPNAmount;
    }

    public void setDbBrokerageExcludePPNAmount(BigDecimal dbBrokerageExcludePPNAmount) {
        this.dbBrokerageExcludePPNAmount = dbBrokerageExcludePPNAmount;
    }

    public BigDecimal getDbBrokerageExcludePPNPct() {
        return dbBrokerageExcludePPNPct;
    }

    public void setDbBrokerageExcludePPNPct(BigDecimal dbBrokerageExcludePPNPct) {
        this.dbBrokerageExcludePPNPct = dbBrokerageExcludePPNPct;
    }

    public BigDecimal getDbBrokerageIncludePPNAmount() {
        return dbBrokerageIncludePPNAmount;
    }

    public void setDbBrokerageIncludePPNAmount(BigDecimal dbBrokerageIncludePPNAmount) {
        this.dbBrokerageIncludePPNAmount = dbBrokerageIncludePPNAmount;
    }

    public BigDecimal getDbBrokerageIncludePPNPct() {
        return dbBrokerageIncludePPNPct;
    }

    public void setDbBrokerageIncludePPNPct(BigDecimal dbBrokerageIncludePPNPct) {
        this.dbBrokerageIncludePPNPct = dbBrokerageIncludePPNPct;
    }

    public BigDecimal getDbDiscountAmount() {
        return dbDiscountAmount;
    }

    public void setDbDiscountAmount(BigDecimal dbDiscountAmount) {
        this.dbDiscountAmount = dbDiscountAmount;
    }

    public BigDecimal getDbDiscountPct() {
        return dbDiscountPct;
    }

    public void setDbDiscountPct(BigDecimal dbDiscountPct) {
        this.dbDiscountPct = dbDiscountPct;
    }

    public BigDecimal getDbFeeBase1Amount() {
        return dbFeeBase1Amount;
    }

    public void setDbFeeBase1Amount(BigDecimal dbFeeBase1Amount) {
        this.dbFeeBase1Amount = dbFeeBase1Amount;
    }

    public BigDecimal getDbFeeBase1Pct() {
        return dbFeeBase1Pct;
    }

    public void setDbFeeBase1Pct(BigDecimal dbFeeBase1Pct) {
        this.dbFeeBase1Pct = dbFeeBase1Pct;
    }

    public BigDecimal getDbFeeBase2Amount() {
        return dbFeeBase2Amount;
    }

    public void setDbFeeBase2Amount(BigDecimal dbFeeBase2Amount) {
        this.dbFeeBase2Amount = dbFeeBase2Amount;
    }

    public BigDecimal getDbFeeBase2Pct() {
        return dbFeeBase2Pct;
    }

    public void setDbFeeBase2Pct(BigDecimal dbFeeBase2Pct) {
        this.dbFeeBase2Pct = dbFeeBase2Pct;
    }

    public BigDecimal getDbFeeBasePPN1Exclude() {
        return dbFeeBasePPN1Exclude;
    }

    public void setDbFeeBasePPN1Exclude(BigDecimal dbFeeBasePPN1Exclude) {
        this.dbFeeBasePPN1Exclude = dbFeeBasePPN1Exclude;
    }

    public BigDecimal getDbFeeBasePPN1Include() {
        return dbFeeBasePPN1Include;
    }

    public void setDbFeeBasePPN1Include(BigDecimal dbFeeBasePPN1Include) {
        this.dbFeeBasePPN1Include = dbFeeBasePPN1Include;
    }

    public BigDecimal getDbKomisi1Amount() {
        return dbKomisi1Amount;
    }

    public void setDbKomisi1Amount(BigDecimal dbKomisi1Amount) {
        this.dbKomisi1Amount = dbKomisi1Amount;
    }

    public BigDecimal getDbKomisi1Pct() {
        return dbKomisi1Pct;
    }

    public void setDbKomisi1Pct(BigDecimal dbKomisi1Pct) {
        this.dbKomisi1Pct = dbKomisi1Pct;
    }

    public BigDecimal getDbKomisi2Amount() {
        return dbKomisi2Amount;
    }

    public void setDbKomisi2Amount(BigDecimal dbKomisi2Amount) {
        this.dbKomisi2Amount = dbKomisi2Amount;
    }

    public BigDecimal getDbKomisi2Pct() {
        return dbKomisi2Pct;
    }

    public void setDbKomisi2Pct(BigDecimal dbKomisi2Pct) {
        this.dbKomisi2Pct = dbKomisi2Pct;
    }

    public BigDecimal getDbPremiFlexas() {
        return dbPremiFlexas;
    }

    public void setDbPremiFlexas(BigDecimal dbPremiFlexas) {
        this.dbPremiFlexas = dbPremiFlexas;
    }

    public BigDecimal getDbRatePct() {
        return dbRatePct;
    }

    public void setDbRatePct(BigDecimal dbRatePct) {
        this.dbRatePct = dbRatePct;
    }

    public BigDecimal getDbSumInsured() {
        return dbSumInsured;
    }

    public void setDbSumInsured(BigDecimal dbSumInsured) {
        this.dbSumInsured = dbSumInsured;
    }

    public Date getDtTanggalPolis() {
        return dtTanggalPolis;
    }

    public void setDtTanggalPolis(Date dtTanggalPolis) {
        this.dtTanggalPolis = dtTanggalPolis;
    }

    public Date getDtTanggalProses() {
        return dtTanggalProses;
    }

    public void setDtTanggalProses(Date dtTanggalProses) {
        this.dtTanggalProses = dtTanggalProses;
    }

    public String getStBrokerageEntityID() {
        return stBrokerageEntityID;
    }

    public void setStBrokerageEntityID(String stBrokerageEntityID) {
        this.stBrokerageEntityID = stBrokerageEntityID;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStDataID() {
        return stDataID;
    }

    public void setStDataID(String stDataID) {
        this.stDataID = stDataID;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStFeeBase1EntityID() {
        return stFeeBase1EntityID;
    }

    public void setStFeeBase1EntityID(String stFeeBase1EntityID) {
        this.stFeeBase1EntityID = stFeeBase1EntityID;
    }

    public String getStFeeBase2EntityID() {
        return stFeeBase2EntityID;
    }

    public void setStFeeBase2EntityID(String stFeeBase2EntityID) {
        this.stFeeBase2EntityID = stFeeBase2EntityID;
    }

    public String getStKomisi1EntityID() {
        return stKomisi1EntityID;
    }

    public void setStKomisi1EntityID(String stKomisi1EntityID) {
        this.stKomisi1EntityID = stKomisi1EntityID;
    }

    public String getStKomisi2EntityID() {
        return stKomisi2EntityID;
    }

    public void setStKomisi2EntityID(String stKomisi2EntityID) {
        this.stKomisi2EntityID = stKomisi2EntityID;
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
    private DTOList documents;

    /*
    public DTOList getDocuments() {
    if (documents == null && stNomorLoan != null)
    documents = loadDocuments(stNomorLoan);
    else
    documents = new DTOList();
    return documents;
    }*/
    public void setDocuments(DTOList documents) {
        this.documents = documents;
    }

    public EntityView getEntity(String stEntID) {

        entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);

        return entity;
    }
}

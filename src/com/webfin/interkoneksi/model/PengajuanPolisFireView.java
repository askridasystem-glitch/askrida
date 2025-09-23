/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:51:57 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.interkoneksi.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.math.BigDecimal;
import java.util.Date;

public class PengajuanPolisFireView extends DTO implements RecordAudit {
    /*
    pengajuan_polis_kebakaran
    (
    data_id bigint,
    pol_no character varying(32),
    tgl_polis timestamp without time zone,
    cc_code  character varying(4),
    region_id bigint,
    pol_type_id character varying(4),
    kode_bank character varying(12),
    entity_id bigint,
    penggunaan character varying(32),
    kelas_konstruksi character varying(12),
    penerangan character varying(12),
    kode_risiko character varying(24),
    alamat_risiko text,
    provinsi character varying(64),
    kode_pos character varying(10),
    nama_debitur character varying(32),
    periode_awal  timestamp without time zone,
    periode_akhir  timestamp without time zone,
    kode_risiko1 character varying(24),
    kode_risiko2 character varying(24),
    kode_risiko3 character varying(24),
    sum_insured numeric,
    rate_pct numeric,
    premi_flexas numeric,
    tgl_stnc timestamp without time zone,

    komisi1_pct numeric,
    komisi1_amount numeric,
    komisi2_pct numeric,
    komisi2_amount numeric,
    feebase1_pct numeric,
    feebase1_amount numeric,
    feebase2_pct numeric,
    feebase2_amount numeric,
     *
    komisi1_ent_id bigint,
    komisi2_ent_id bigint,
    feebase1_ent_id bigint,
    feebase2_ent_id bigint,
     *
    discount_pct numeric,
    discount_amount numeric,
    feebase1_ppn_include numeric,
    feebase1_ppn_exclude numeric,
    feebase1_ppn_include_pct numeric,
    feebase1_ppn_exclude_pct numeric,
    brokerage_include_ppn_pct numeric,
    brokerage_include_ppn_amount numeric,
    brokerage_exclude_ppn_pct numeric,
    brokerage_exclude_ppn_amount numeric,
    brokerage_ent_id bigint,

    tgl_proses timestamp without time zone,
    proses_flag character varying(1),
    tgl_transfer timestamp without time zone
    )
     */

    public static String tableName = "pengajuan_polis_kebakaran";
    
    public static String fieldMap[][] = {
        {"stDataID", "data_id*pk"},
        {"stPolicyNo", "pol_no"},
        {"dtTanggalPolis", "tgl_polis"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stPolicyTypeID", "pol_type_id"},
        {"stKodeBank", "kode_bank"},
        {"stEntityID", "entity_id"},
        {"stPenggunaan", "penggunaan"},
        {"stKelasKonstruksi", "kelas_konstruksi"},
        {"stPenerangan", "penerangan"},
        {"stKodeRisiko", "kode_risiko"},
        {"stAlamatRisiko", "alamat_risiko"},
        {"stProvinsi", "provinsi"},
        {"stKodePos", "kode_pos"},
        {"stNamaDebitur", "nama_debitur"},
        {"dtTanggalAwal", "periode_awal"},
        {"dtTanggalAkhir", "periode_akhir"},
        {"stKodeRisiko1", "kode_risiko1"},
        {"stKodeRisiko2", "kode_risiko2"},
        {"stKodeRisiko3", "kode_risiko3"},
        {"dbSumInsured", "sum_insured"},
        {"dbRatePct", "rate_pct"},
        {"dbPremiFlexas", "premi_flexas"},
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
        {"dbFeeBasePPN1Include", "feebase1_ppn_include"},
        {"dbFeeBasePPN1Exclude", "feebase1_ppn_exclude"},
        {"dbBrokerageIncludePPNPct", "brokerage_include_ppn_pct"},
        {"dbBrokerageIncludePPNAmount", "brokerage_include_ppn_amount"},
        {"dbBrokerageExcludePPNPct", "brokerage_exclude_ppn_pct"},
        {"dbBrokerageExcludePPNAmount", "brokerage_exclude_ppn_amount"},
        {"stBrokerageEntityID", "brokerage_ent_id"},
        {"dtTanggalProses", "tgl_proses"},
        {"stProsesFlag", "proses_flag"},
        {"stJangkaWaktuBulan", "jk_waktu_bulan"},
        {"stBatasKanan", "batas_kanan"},
        {"stBatasKiri", "batas_kiri"},
        {"stBatasDepan", "batas_depan"},
        {"stBatasBelakang", "batas_belakang"},
        {"dbDeductiblePctOfClaim", "deductible_pct_of_claim"},
        {"dbDeductibleMinimum", "deductible_min"},
        {"dbDeductibleMaximum", "deductible_max"},
        {"dbBiayaPolis", "biaya_polis"},
        {"stCostCenterCodeSource", "cc_code_source"},
        {"stRegionIDSource", "region_id_source"},
    
    };


    private String stDataID;
    private String stPolicyNo;
    private Date dtTanggalPolis;
    private String stCostCenterCode;
    private String stRegionID;
    private String stPolicyTypeID;
    private String stKodeBank;
    private String stEntityID;
    private String stPenggunaan;
    private String stKelasKonstruksi;
    private String stPenerangan;
    private String stKodeRisiko;
    private String stAlamatRisiko;
    private String stProvinsi;
    private String stKodePos;
    private String stNamaDebitur;
    private Date dtTanggalAwal;
    private Date dtTanggalAkhir;
    private String stKodeRisiko1;
    private String stKodeRisiko2;
    private String stKodeRisiko3;
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
    private String stJangkaWaktuBulan;
    private String stBatasKanan;
    private String stBatasKiri;
    private String stBatasDepan;
    private String stBatasBelakang;
    private BigDecimal dbDeductiblePctOfClaim;
    private BigDecimal dbDeductibleMinimum;
    private BigDecimal dbDeductibleMaximum;
    private BigDecimal dbBiayaPolis;
    private String stCostCenterCodeSource;
    private String stRegionIDSource;

    public String getStCostCenterCodeSource() {
        return stCostCenterCodeSource;
    }

    public void setStCostCenterCodeSource(String stCostCenterCodeSource) {
        this.stCostCenterCodeSource = stCostCenterCodeSource;
    }

    public String getStRegionIDSource() {
        return stRegionIDSource;
    }

    public void setStRegionIDSource(String stRegionIDSource) {
        this.stRegionIDSource = stRegionIDSource;
    }

    public BigDecimal getDbBiayaPolis() {
        return dbBiayaPolis;
    }

    public void setDbBiayaPolis(BigDecimal dbBiayaPolis) {
        this.dbBiayaPolis = dbBiayaPolis;
    }

    public BigDecimal getDbDeductibleMaximum() {
        return dbDeductibleMaximum;
    }

    public void setDbDeductibleMaximum(BigDecimal dbDeductibleMaximum) {
        this.dbDeductibleMaximum = dbDeductibleMaximum;
    }

    public BigDecimal getDbDeductibleMinimum() {
        return dbDeductibleMinimum;
    }

    public void setDbDeductibleMinimum(BigDecimal dbDeductibleMinimum) {
        this.dbDeductibleMinimum = dbDeductibleMinimum;
    }

    public BigDecimal getDbDeductiblePctOfClaim() {
        return dbDeductiblePctOfClaim;
    }

    public void setDbDeductiblePctOfClaim(BigDecimal dbDeductiblePctOfClaim) {
        this.dbDeductiblePctOfClaim = dbDeductiblePctOfClaim;
    }

    public String getStBatasBelakang() {
        return stBatasBelakang;
    }

    public void setStBatasBelakang(String stBatasBelakang) {
        this.stBatasBelakang = stBatasBelakang;
    }

    public String getStBatasDepan() {
        return stBatasDepan;
    }

    public void setStBatasDepan(String stBatasDepan) {
        this.stBatasDepan = stBatasDepan;
    }

    public String getStBatasKanan() {
        return stBatasKanan;
    }

    public void setStBatasKanan(String stBatasKanan) {
        this.stBatasKanan = stBatasKanan;
    }

    public String getStBatasKiri() {
        return stBatasKiri;
    }

    public void setStBatasKiri(String stBatasKiri) {
        this.stBatasKiri = stBatasKiri;
    }

    public String getStJangkaWaktuBulan() {
        return stJangkaWaktuBulan;
    }

    public void setStJangkaWaktuBulan(String stJangkaWaktuBulan) {
        this.stJangkaWaktuBulan = stJangkaWaktuBulan;
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

    public String getStAlamatRisiko() {
        return stAlamatRisiko;
    }

    public void setStAlamatRisiko(String stAlamatRisiko) {
        this.stAlamatRisiko = stAlamatRisiko;
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

    public String getStKelasKonstruksi() {
        return stKelasKonstruksi;
    }

    public void setStKelasKonstruksi(String stKelasKonstruksi) {
        this.stKelasKonstruksi = stKelasKonstruksi;
    }

    public String getStKodeBank() {
        return stKodeBank;
    }

    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    public String getStKodePos() {
        return stKodePos;
    }

    public void setStKodePos(String stKodePos) {
        this.stKodePos = stKodePos;
    }

    public String getStKodeRisiko() {
        return stKodeRisiko;
    }

    public void setStKodeRisiko(String stKodeRisiko) {
        this.stKodeRisiko = stKodeRisiko;
    }

    public String getStKodeRisiko1() {
        return stKodeRisiko1;
    }

    public void setStKodeRisiko1(String stKodeRisiko1) {
        this.stKodeRisiko1 = stKodeRisiko1;
    }

    public String getStKodeRisiko2() {
        return stKodeRisiko2;
    }

    public void setStKodeRisiko2(String stKodeRisiko2) {
        this.stKodeRisiko2 = stKodeRisiko2;
    }

    public String getStKodeRisiko3() {
        return stKodeRisiko3;
    }

    public void setStKodeRisiko3(String stKodeRisiko3) {
        this.stKodeRisiko3 = stKodeRisiko3;
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

    public String getStNamaDebitur() {
        return stNamaDebitur;
    }

    public void setStNamaDebitur(String stNamaDebitur) {
        this.stNamaDebitur = stNamaDebitur;
    }

    public String getStPenerangan() {
        return stPenerangan;
    }

    public void setStPenerangan(String stPenerangan) {
        this.stPenerangan = stPenerangan;
    }

    public String getStPenggunaan() {
        return stPenggunaan;
    }

    public void setStPenggunaan(String stPenggunaan) {
        this.stPenggunaan = stPenggunaan;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public String getStProsesFlag() {
        return stProsesFlag;
    }

    public void setStProsesFlag(String stProsesFlag) {
        this.stProsesFlag = stProsesFlag;
    }

    public String getStProvinsi() {
        return stProvinsi;
    }

    public void setStProvinsi(String stProvinsi) {
        this.stProvinsi = stProvinsi;
    }

    public String getStRegionID() {
        return stRegionID;
    }

    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

}

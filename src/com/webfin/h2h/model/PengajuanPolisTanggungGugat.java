/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.h2h.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author doni
 */
public class PengajuanPolisTanggungGugat {

     public static String tableName = "pengajuan_polis_tanggung_gugat";

    public static String fieldMap[][] = {

        {"dataId","data_id*pk"},
        {"polNo","pol_no"},
        {"tglPolis","tgl_polis"},
        {"ccCode","cc_code"},
        {"regionId","region_id"},
        {"polTypeId","pol_type_id"},
        {"entityId","entity_id"},
        {"marketerEntId","marketer_ent_id"},
        {"nama","nama"},
        {"alamat","alamat"},
        {"tglLahir","tgl_lahir"},
        {"jenisKelamin","jenis_kelamin"},
        {"usia","usia"},
        {"nik","nik"},
        {"noHp","no_hp"},
        {"email","email"},
        {"noHpDarurat","no_hp_darurat"},
        {"kategoriData","kategori_data"},
        {"namaDarurat","nama_darurat"},
        {"profesi","profesi"},
        {"planCoverage","plan_coverage"},
        {"periodeAwal","periode_awal"},
        {"periodeAkhir","periode_akhir"},
        {"noStr","no_str"},
        {"statusStr","status_str"},
        {"periodeAwalStr","periode_awal_str"},
        {"periodeAkhirStr","periode_akhir_str"},
        {"noSip1","no_sip1"},
        {"periodeAwalSip1","periode_awal_sip1"},
        {"periodeAkhirSip1","periode_akhir_sip1"},
        {"penerbitSip1","penerbit_sip1"},
        {"tempatPraktikSip1","tempat_praktik_sip1"},
        {"noSip2","no_sip2"},
        {"periodeAwalSip2","periode_awal_sip2"},
        {"periodeAkhirSip2","periode_akhir_sip2"},
        {"penerbitSip2","penerbit_sip2"},
        {"tempatPraktikSip2","tempat_praktik_sip2"},
        {"noSip3","no_sip3"},
        {"periodeAwalSip3","periode_awal_sip3"},
        {"periodeAkhirSip3","periode_akhir_sip3"},
        {"penerbitSip3","penerbit_sip3"},
        {"tempatPraktikSip3","tempat_praktik_sip3"},
        {"pekerjaan","pekerjaan"},
        {"sumInsured","sum_insured"},
        {"rate","rate"},
        {"premi","premi"},
        {"tglStnc","tgl_stnc"},
        {"komisi1Pct","komisi1_pct"},
        {"komisi1Amount","komisi1_amount"},
        {"komisi2Pct","komisi2_pct"},
        {"komisi2Amount","komisi2_amount"},
        {"feebase1Pct","feebase1_pct"},
        {"feebase1Amount","feebase1_amount"},
        {"feebase2Pct","feebase2_pct"},
        {"feebase2Amount","feebase2_amount"},
        {"komisi1EntId","komisi1_ent_id"},
        {"komisi2EntId","komisi2_ent_id"},
        {"feebase1EntId","feebase1_ent_id"},
        {"feebase2EntId","feebase2_ent_id"},
        {"discountPct","discount_pct"},
        {"discountAmount","discount_amount"},
        {"feebase1PpnInclude","feebase1_ppn_include"},
        {"feebase1PpnExclude","feebase1_ppn_exclude"},
        {"feebase1PpnIncludePct","feebase1_ppn_include_pct"},
        {"feebase1PpnExcludePct","feebase1_ppn_exclude_pct"},
        {"brokeragePct","brokerage_pct"},
        {"brokerageAmount","brokerage_amount"},
        {"brokerageIncludePpnPct","brokerage_include_ppn_pct"},
        {"brokerageIncludePpnAmount","brokerage_include_ppn_amount"},
        {"brokerageExcludePpnPct","brokerage_exclude_ppn_pct"},
        {"brokerageExcludePpnAmount","brokerage_exclude_ppn_amount"},
        {"brokerageEntId","brokerage_ent_id"},
        {"tglProses","tgl_proses"},
        {"prosesFlag","proses_flag"},
        {"tglTransfer","tgl_transfer"},
        {"jkWaktuBulan","jk_waktu_bulan"},
        {"deductiblePctOfClaim","deductible_pct_of_claim"},
        {"deductibleMin","deductible_min"},
        {"deductibleMax","deductible_max"},
        {"biayaPolis","biaya_polis"},
        {"biayaMaterai","biaya_materai"},
        {"ccCodeSource","cc_code_source"},
        {"regionIdSource","region_id_source"},
        {"warranty","warranty"},

    };

    private String dataId;
    private String polNo;
    private Date tglPolis;
    private String ccCode;
    private String regionId;
    private String polTypeId;
    private String entityId;
    private String marketerEntId;
    private String nama;
    private String alamat;
    private Date tglLahir;
    private String jenisKelamin;
    private String usia;
    private String nik;
    private String noHp;
    private String email;
    private String noHpDarurat;
    private String kategoriData;
    private String namaDarurat;
    private String profesi;
    private String planCoverage;
    private Date periodeAwal;
    private Date periodeAkhir;
    private String noStr;
    private String statusStr;
    private Date periodeAwalStr;
    private Date periodeAkhirStr;
    private String noSip1;
    private Date periodeAwalSip1;
    private Date periodeAkhirSip1;
    private String penerbitSip1;
    private String tempatPraktikSip1;
    private String noSip2;
    private Date periodeAwalSip2;
    private Date periodeAkhirSip2;
    private String penerbitSip2;
    private String tempatPraktikSip2;
    private String noSip3;
    private Date periodeAwalSip3;
    private Date periodeAkhirSip3;
    private String penerbitSip3;
    private String tempatPraktikSip3;
    private String pekerjaan;
    private BigDecimal sumInsured;
    private BigDecimal rate;
    private BigDecimal premi;
    private Date tglStnc;
    private BigDecimal komisi1Pct;
    private BigDecimal komisi1Amount;
    private BigDecimal komisi2Pct;
    private BigDecimal komisi2Amount;
    private BigDecimal feebase1Pct;
    private BigDecimal feebase1Amount;
    private BigDecimal feebase2Pct;
    private BigDecimal feebase2Amount;
    private String komisi1EntId;
    private String komisi2EntId;
    private String feebase1EntId;
    private String feebase2EntId;
    private BigDecimal discountPct;
    private BigDecimal discountAmount;
    private BigDecimal feebase1PpnInclude;
    private BigDecimal feebase1PpnExclude;
    private BigDecimal feebase1PpnIncludePct;
    private BigDecimal feebase1PpnExcludePct;
    private BigDecimal brokeragePct;
    private BigDecimal brokerageAmount;
    private BigDecimal brokerageIncludePpnPct;
    private BigDecimal brokerageIncludePpnAmount;
    private BigDecimal brokerageExcludePpnPct;
    private BigDecimal brokerageExcludePpnAmount;
    private String brokerageEntId;
    private Date tglProses;
    private String prosesFlag;
    private Date tglTransfer;
    private String jkWaktuBulan;
    private BigDecimal deductiblePctOfClaim;
    private BigDecimal deductibleMin;
    private BigDecimal deductibleMax;
    private BigDecimal biayaPolis;
    private BigDecimal biayaMaterai;
    private String ccCodeSource;
    private String regionIdSource;
    private String warranty;

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public BigDecimal getBiayaMaterai() {
        return biayaMaterai;
    }

    public void setBiayaMaterai(BigDecimal biayaMaterai) {
        this.biayaMaterai = biayaMaterai;
    }

    public BigDecimal getBiayaPolis() {
        return biayaPolis;
    }

    public void setBiayaPolis(BigDecimal biayaPolis) {
        this.biayaPolis = biayaPolis;
    }

    public BigDecimal getBrokerageAmount() {
        return brokerageAmount;
    }

    public void setBrokerageAmount(BigDecimal brokerageAmount) {
        this.brokerageAmount = brokerageAmount;
    }

    public String getBrokerageEntId() {
        return brokerageEntId;
    }

    public void setBrokerageEntId(String brokerageEntId) {
        this.brokerageEntId = brokerageEntId;
    }

    public BigDecimal getBrokerageExcludePpnAmount() {
        return brokerageExcludePpnAmount;
    }

    public void setBrokerageExcludePpnAmount(BigDecimal brokerageExcludePpnAmount) {
        this.brokerageExcludePpnAmount = brokerageExcludePpnAmount;
    }

    public BigDecimal getBrokerageExcludePpnPct() {
        return brokerageExcludePpnPct;
    }

    public void setBrokerageExcludePpnPct(BigDecimal brokerageExcludePpnPct) {
        this.brokerageExcludePpnPct = brokerageExcludePpnPct;
    }

    public BigDecimal getBrokerageIncludePpnAmount() {
        return brokerageIncludePpnAmount;
    }

    public void setBrokerageIncludePpnAmount(BigDecimal brokerageIncludePpnAmount) {
        this.brokerageIncludePpnAmount = brokerageIncludePpnAmount;
    }

    public BigDecimal getBrokerageIncludePpnPct() {
        return brokerageIncludePpnPct;
    }

    public void setBrokerageIncludePpnPct(BigDecimal brokerageIncludePpnPct) {
        this.brokerageIncludePpnPct = brokerageIncludePpnPct;
    }

    public BigDecimal getBrokeragePct() {
        return brokeragePct;
    }

    public void setBrokeragePct(BigDecimal brokeragePct) {
        this.brokeragePct = brokeragePct;
    }

    public String getCcCode() {
        return ccCode;
    }

    public void setCcCode(String ccCode) {
        this.ccCode = ccCode;
    }

    public String getCcCodeSource() {
        return ccCodeSource;
    }

    public void setCcCodeSource(String ccCodeSource) {
        this.ccCodeSource = ccCodeSource;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public BigDecimal getDeductibleMax() {
        return deductibleMax;
    }

    public void setDeductibleMax(BigDecimal deductibleMax) {
        this.deductibleMax = deductibleMax;
    }

    public BigDecimal getDeductibleMin() {
        return deductibleMin;
    }

    public void setDeductibleMin(BigDecimal deductibleMin) {
        this.deductibleMin = deductibleMin;
    }

    public BigDecimal getDeductiblePctOfClaim() {
        return deductiblePctOfClaim;
    }

    public void setDeductiblePctOfClaim(BigDecimal deductiblePctOfClaim) {
        this.deductiblePctOfClaim = deductiblePctOfClaim;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountPct() {
        return discountPct;
    }

    public void setDiscountPct(BigDecimal discountPct) {
        this.discountPct = discountPct;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public BigDecimal getFeebase1Amount() {
        return feebase1Amount;
    }

    public void setFeebase1Amount(BigDecimal feebase1Amount) {
        this.feebase1Amount = feebase1Amount;
    }

    public String getFeebase1EntId() {
        return feebase1EntId;
    }

    public void setFeebase1EntId(String feebase1EntId) {
        this.feebase1EntId = feebase1EntId;
    }

    public BigDecimal getFeebase1Pct() {
        return feebase1Pct;
    }

    public void setFeebase1Pct(BigDecimal feebase1Pct) {
        this.feebase1Pct = feebase1Pct;
    }

    public BigDecimal getFeebase1PpnExclude() {
        return feebase1PpnExclude;
    }

    public void setFeebase1PpnExclude(BigDecimal feebase1PpnExclude) {
        this.feebase1PpnExclude = feebase1PpnExclude;
    }

    public BigDecimal getFeebase1PpnExcludePct() {
        return feebase1PpnExcludePct;
    }

    public void setFeebase1PpnExcludePct(BigDecimal feebase1PpnExcludePct) {
        this.feebase1PpnExcludePct = feebase1PpnExcludePct;
    }

    public BigDecimal getFeebase1PpnInclude() {
        return feebase1PpnInclude;
    }

    public void setFeebase1PpnInclude(BigDecimal feebase1PpnInclude) {
        this.feebase1PpnInclude = feebase1PpnInclude;
    }

    public BigDecimal getFeebase1PpnIncludePct() {
        return feebase1PpnIncludePct;
    }

    public void setFeebase1PpnIncludePct(BigDecimal feebase1PpnIncludePct) {
        this.feebase1PpnIncludePct = feebase1PpnIncludePct;
    }

    public BigDecimal getFeebase2Amount() {
        return feebase2Amount;
    }

    public void setFeebase2Amount(BigDecimal feebase2Amount) {
        this.feebase2Amount = feebase2Amount;
    }

    public String getFeebase2EntId() {
        return feebase2EntId;
    }

    public void setFeebase2EntId(String feebase2EntId) {
        this.feebase2EntId = feebase2EntId;
    }

    public BigDecimal getFeebase2Pct() {
        return feebase2Pct;
    }

    public void setFeebase2Pct(BigDecimal feebase2Pct) {
        this.feebase2Pct = feebase2Pct;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getJkWaktuBulan() {
        return jkWaktuBulan;
    }

    public void setJkWaktuBulan(String jkWaktuBulan) {
        this.jkWaktuBulan = jkWaktuBulan;
    }

    public String getKategoriData() {
        return kategoriData;
    }

    public void setKategoriData(String kategoriData) {
        this.kategoriData = kategoriData;
    }

    public BigDecimal getKomisi1Amount() {
        return komisi1Amount;
    }

    public void setKomisi1Amount(BigDecimal komisi1Amount) {
        this.komisi1Amount = komisi1Amount;
    }

    public String getKomisi1EntId() {
        return komisi1EntId;
    }

    public void setKomisi1EntId(String komisi1EntId) {
        this.komisi1EntId = komisi1EntId;
    }

    public BigDecimal getKomisi1Pct() {
        return komisi1Pct;
    }

    public void setKomisi1Pct(BigDecimal komisi1Pct) {
        this.komisi1Pct = komisi1Pct;
    }

    public BigDecimal getKomisi2Amount() {
        return komisi2Amount;
    }

    public void setKomisi2Amount(BigDecimal komisi2Amount) {
        this.komisi2Amount = komisi2Amount;
    }

    public String getKomisi2EntId() {
        return komisi2EntId;
    }

    public void setKomisi2EntId(String komisi2EntId) {
        this.komisi2EntId = komisi2EntId;
    }

    public BigDecimal getKomisi2Pct() {
        return komisi2Pct;
    }

    public void setKomisi2Pct(BigDecimal komisi2Pct) {
        this.komisi2Pct = komisi2Pct;
    }

    public String getMarketerEntId() {
        return marketerEntId;
    }

    public void setMarketerEntId(String marketerEntId) {
        this.marketerEntId = marketerEntId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaDarurat() {
        return namaDarurat;
    }

    public void setNamaDarurat(String namaDarurat) {
        this.namaDarurat = namaDarurat;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNoHpDarurat() {
        return noHpDarurat;
    }

    public void setNoHpDarurat(String noHpDarurat) {
        this.noHpDarurat = noHpDarurat;
    }

    public String getNoSip1() {
        return noSip1;
    }

    public void setNoSip1(String noSip1) {
        this.noSip1 = noSip1;
    }

    public String getNoSip2() {
        return noSip2;
    }

    public void setNoSip2(String noSip2) {
        this.noSip2 = noSip2;
    }

    public String getNoSip3() {
        return noSip3;
    }

    public void setNoSip3(String noSip3) {
        this.noSip3 = noSip3;
    }

    public String getNoStr() {
        return noStr;
    }

    public void setNoStr(String noStr) {
        this.noStr = noStr;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getPenerbitSip1() {
        return penerbitSip1;
    }

    public void setPenerbitSip1(String penerbitSip1) {
        this.penerbitSip1 = penerbitSip1;
    }

    public String getPenerbitSip2() {
        return penerbitSip2;
    }

    public void setPenerbitSip2(String penerbitSip2) {
        this.penerbitSip2 = penerbitSip2;
    }

    public String getPenerbitSip3() {
        return penerbitSip3;
    }

    public void setPenerbitSip3(String penerbitSip3) {
        this.penerbitSip3 = penerbitSip3;
    }

    public Date getPeriodeAkhir() {
        return periodeAkhir;
    }

    public void setPeriodeAkhir(Date periodeAkhir) {
        this.periodeAkhir = periodeAkhir;
    }

    public Date getPeriodeAkhirSip1() {
        return periodeAkhirSip1;
    }

    public void setPeriodeAkhirSip1(Date periodeAkhirSip1) {
        this.periodeAkhirSip1 = periodeAkhirSip1;
    }

    public Date getPeriodeAkhirSip2() {
        return periodeAkhirSip2;
    }

    public void setPeriodeAkhirSip2(Date periodeAkhirSip2) {
        this.periodeAkhirSip2 = periodeAkhirSip2;
    }

    public Date getPeriodeAkhirSip3() {
        return periodeAkhirSip3;
    }

    public void setPeriodeAkhirSip3(Date periodeAkhirSip3) {
        this.periodeAkhirSip3 = periodeAkhirSip3;
    }

    public Date getPeriodeAkhirStr() {
        return periodeAkhirStr;
    }

    public void setPeriodeAkhirStr(Date periodeAkhirStr) {
        this.periodeAkhirStr = periodeAkhirStr;
    }

    public Date getPeriodeAwal() {
        return periodeAwal;
    }

    public void setPeriodeAwal(Date periodeAwal) {
        this.periodeAwal = periodeAwal;
    }

    public Date getPeriodeAwalSip1() {
        return periodeAwalSip1;
    }

    public void setPeriodeAwalSip1(Date periodeAwalSip1) {
        this.periodeAwalSip1 = periodeAwalSip1;
    }

    public Date getPeriodeAwalSip2() {
        return periodeAwalSip2;
    }

    public void setPeriodeAwalSip2(Date periodeAwalSip2) {
        this.periodeAwalSip2 = periodeAwalSip2;
    }

    public Date getPeriodeAwalSip3() {
        return periodeAwalSip3;
    }

    public void setPeriodeAwalSip3(Date periodeAwalSip3) {
        this.periodeAwalSip3 = periodeAwalSip3;
    }

    public Date getPeriodeAwalStr() {
        return periodeAwalStr;
    }

    public void setPeriodeAwalStr(Date periodeAwalStr) {
        this.periodeAwalStr = periodeAwalStr;
    }

    public String getPlanCoverage() {
        return planCoverage;
    }

    public void setPlanCoverage(String planCoverage) {
        this.planCoverage = planCoverage;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }

    public String getPolTypeId() {
        return polTypeId;
    }

    public void setPolTypeId(String polTypeId) {
        this.polTypeId = polTypeId;
    }

    public BigDecimal getPremi() {
        return premi;
    }

    public void setPremi(BigDecimal premi) {
        this.premi = premi;
    }

    public String getProfesi() {
        return profesi;
    }

    public void setProfesi(String profesi) {
        this.profesi = profesi;
    }

    public String getProsesFlag() {
        return prosesFlag;
    }

    public void setProsesFlag(String prosesFlag) {
        this.prosesFlag = prosesFlag;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionIdSource() {
        return regionIdSource;
    }

    public void setRegionIdSource(String regionIdSource) {
        this.regionIdSource = regionIdSource;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getTempatPraktikSip1() {
        return tempatPraktikSip1;
    }

    public void setTempatPraktikSip1(String tempatPraktikSip1) {
        this.tempatPraktikSip1 = tempatPraktikSip1;
    }

    public String getTempatPraktikSip2() {
        return tempatPraktikSip2;
    }

    public void setTempatPraktikSip2(String tempatPraktikSip2) {
        this.tempatPraktikSip2 = tempatPraktikSip2;
    }

    public String getTempatPraktikSip3() {
        return tempatPraktikSip3;
    }

    public void setTempatPraktikSip3(String tempatPraktikSip3) {
        this.tempatPraktikSip3 = tempatPraktikSip3;
    }

    public Date getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(Date tglLahir) {
        this.tglLahir = tglLahir;
    }

    public Date getTglPolis() {
        return tglPolis;
    }

    public void setTglPolis(Date tglPolis) {
        this.tglPolis = tglPolis;
    }

    public Date getTglProses() {
        return tglProses;
    }

    public void setTglProses(Date tglProses) {
        this.tglProses = tglProses;
    }

    public Date getTglStnc() {
        return tglStnc;
    }

    public void setTglStnc(Date tglStnc) {
        this.tglStnc = tglStnc;
    }

    public Date getTglTransfer() {
        return tglTransfer;
    }

    public void setTglTransfer(Date tglTransfer) {
        this.tglTransfer = tglTransfer;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }



}

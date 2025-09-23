/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.h2h.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author doni
 */
public class SPPAOnlineKendaraan  extends DTO implements RecordAudit{
    
    public static String tableName = "sppa_kendaraan_pengajuan_polis";

    public static String fieldMap[][] = {

        {"idPengajuanPolis","id_pengajuan_polis*pk"},
        {"namaDebitur","nama_debitur"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"entID", "ent_id"},
        {"wilayah","wilayah"},
        {"merk","merk"},
        {"tipe","tipe"},
        {"tahunPembuatan","tahun_pembuatan"},
        {"nomorRangka","nomor_rangka"},
        {"nomorMesin","nomor_mesin"},
        {"penggunaan","penggunaan"},
        {"jenisKendaraan","jenis_kendaraan"},
        {"tanggalAwal","tanggal_awal"},
        {"tanggalAkhir","tanggal_akhir"},
        {"hargaPertanggungan","harga_pertanggungan"},
        {"coverage","coverage"},
        {"jangkaWaktuComprehensive","jangka_waktu_comprehensive"},
        {"jangkaWaktuTlo","jangka_waktu_tlo"},
        {"perluasan1","perluasan1"},
        {"perluasan2","perluasan2"},
        {"perluasan3","perluasan3"},
        {"perluasan4","perluasan4"},
        {"perluasan5","perluasan5"},
        {"hargaPertanggungan5","harga_pertanggungan5"},
        {"perluasan6","perluasan6"},
        {"hargaPertanggungan6","harga_pertanggungan6"},
        {"perluasan7","perluasan7"},
        {"hargaPertanggungan7","harga_pertanggungan7"},
        {"premiDasar","premi_dasar"},
        {"totalPremi","total_premi"},
        {"noRekening","no_rekening"},
        {"perluasan8","perluasan8"},
        {"hargaPertanggungan8","harga_pertanggungan8"},
        {"deskripsi","deskripsi"},
        {"rate","rate"},
        {"noPengajuan","no_pengajuan"},
        {"createDate","create_date"},
        {"unikKey","unik_key"},
        {"nopol","nopol"},
        {"tanggalPolis","tanggal_polis"},
        {"statusApproval","status_approval"},
        {"approvalDate","approval_date"},
        {"kodeBank","kode_bank"},
        {"noKtp","no_ktp"},
        {"tempatDuduk","tempat_duduk"},
        {"noPolisi","no_polisi"},
        {"fotoKtp","foto_ktp"},
        {"fotoSuratKendaraan","foto_surat_kendaraan"},
        {"fotoKendaraan","foto_kendaraan"},
        {"fotoKendaraan2","foto_kendaraan2"},
        {"biayaPolis", "biaya_polis"},
        {"biayaMaterai", "biaya_materai"},
        {"feebase1Pct", "feebase1_pct"},
        {"feebase1Amount", "feebase1_amount"},
        {"stDeductibleID1", "deductible_id1"},
        {"dbDeductiblePctOfClaim1", "deductible_pct_of_claim1"},
        {"dbDeductibleMin1", "deductible_min1"},
        {"dbDeductibleMax1", "deductible_max1"},
        {"stDeductibleID2", "deductible_id2"},
        {"dbDeductiblePctOfClaim2", "deductible_pct_of_claim2"},
        {"dbDeductibleMin2", "deductible_min2"},
        {"dbDeductibleMax2", "deductible_max2"},
        {"stDeductibleID3", "deductible_id3"},
        {"dbDeductiblePctOfClaim3", "deductible_pct_of_claim3"},
        {"dbDeductibleMin3", "deductible_min3"},
        {"dbDeductibleMax3", "deductible_max3"},
        {"stDeductibleID4", "deductible_id4"},
        {"dbDeductiblePctOfClaim4", "deductible_pct_of_claim4"},
        {"dbDeductibleMin4", "deductible_min4"},
        {"dbDeductibleMax4", "deductible_max4"},
        {"stDeductibleID5", "deductible_id5"},
        {"dbDeductiblePctOfClaim5", "deductible_pct_of_claim5"},
        {"dbDeductibleMin5", "deductible_min5"},
        {"dbDeductibleMax5", "deductible_max5"},
        {"stDeductibleID6", "deductible_id6"},
        {"dbDeductiblePctOfClaim6", "deductible_pct_of_claim6"},
        {"dbDeductibleMin6", "deductible_min6"},
        {"dbDeductibleMax6", "deductible_max6"},
        {"stDeductibleID7", "deductible_id7"},
        {"dbDeductiblePctOfClaim7", "deductible_pct_of_claim7"},
        {"dbDeductibleMin7", "deductible_min7"},
        {"dbDeductibleMax7", "deductible_max7"},
        {"stDeductibleID8", "deductible_id8"},
        {"dbDeductiblePctOfClaim8", "deductible_pct_of_claim8"},
        {"dbDeductibleMin8", "deductible_min8"},
        {"dbDeductibleMax8", "deductible_max8"},

    };

    private String idPengajuanPolis;
    private String namaDebitur;
    private String wilayah;
    private String merk;
    private String tipe;
    private String tahunPembuatan;
    private String nomorRangka;
    private String nomorMesin;
    private String penggunaan;
    private String jenisKendaraan;
    private Date tanggalAwal;
    private Date tanggalAkhir;
    private BigDecimal hargaPertanggungan;
    private String coverage;
    private String jangkaWaktuComprehensive;
    private String jangkaWaktuTlo;
    private String perluasan1;
    private String perluasan2;
    private String perluasan3;
    private String perluasan4;
    private String perluasan5;
    private BigDecimal hargaPertanggungan5;
    private String perluasan6;
    private BigDecimal hargaPertanggungan6;
    private String perluasan7;
    private BigDecimal hargaPertanggungan7;
    private BigDecimal premiDasar;
    private BigDecimal totalPremi;
    private String noRekening;
    private String perluasan8;
    private BigDecimal hargaPertanggungan8;
    private String deskripsi;
    private BigDecimal rate;
    private String noPengajuan;
    private Date createDate;
    private String unikKey;
    private String nopol;
    private String tanggalPolis;
    private String statusApproval;
    private Date approvalDate;
    private String kodeBank;
    private String noKtp;
    private String tempatDuduk;
    private String noPolisi;
    private String fotoKtp;
    private String fotoSuratKendaraan;
    private String fotoKendaraan;
    private String fotoKendaraan2;

    private String stCostCenterCode;
    private String stRegionID;
    private String entID;

     private BigDecimal biayaPolis;
    private BigDecimal biayaMaterai;
    private BigDecimal feebase1Pct;
    private BigDecimal feebase1Amount;

    private String stDeductibleID1;
    private BigDecimal dbDeductiblePctOfClaim1;
    private BigDecimal dbDeductibleMin1;
    private BigDecimal dbDeductibleMax1;

    private String stDeductibleID2;
    private BigDecimal dbDeductiblePctOfClaim2;
    private BigDecimal dbDeductibleMin2;
    private BigDecimal dbDeductibleMax2;

    private String stDeductibleID3;
    private BigDecimal dbDeductiblePctOfClaim3;
    private BigDecimal dbDeductibleMin3;
    private BigDecimal dbDeductibleMax3;

    private String stDeductibleID4;
    private BigDecimal dbDeductiblePctOfClaim4;
    private BigDecimal dbDeductibleMin4;
    private BigDecimal dbDeductibleMax4;

    private String stDeductibleID5;
    private BigDecimal dbDeductiblePctOfClaim5;
    private BigDecimal dbDeductibleMin5;
    private BigDecimal dbDeductibleMax5;

    private String stDeductibleID6;
    private BigDecimal dbDeductiblePctOfClaim6;
    private BigDecimal dbDeductibleMin6;
    private BigDecimal dbDeductibleMax6;

    private String stDeductibleID7;
    private BigDecimal dbDeductiblePctOfClaim7;
    private BigDecimal dbDeductibleMin7;
    private BigDecimal dbDeductibleMax7;

    private String stDeductibleID8;
    private BigDecimal dbDeductiblePctOfClaim8;
    private BigDecimal dbDeductibleMin8;
    private BigDecimal dbDeductibleMax8;

    public BigDecimal getDbDeductibleMax5() {
        return dbDeductibleMax5;
    }

    public void setDbDeductibleMax5(BigDecimal dbDeductibleMax5) {
        this.dbDeductibleMax5 = dbDeductibleMax5;
    }

    public BigDecimal getDbDeductibleMax6() {
        return dbDeductibleMax6;
    }

    public void setDbDeductibleMax6(BigDecimal dbDeductibleMax6) {
        this.dbDeductibleMax6 = dbDeductibleMax6;
    }

    public BigDecimal getDbDeductibleMax7() {
        return dbDeductibleMax7;
    }

    public void setDbDeductibleMax7(BigDecimal dbDeductibleMax7) {
        this.dbDeductibleMax7 = dbDeductibleMax7;
    }

    public BigDecimal getDbDeductibleMax8() {
        return dbDeductibleMax8;
    }

    public void setDbDeductibleMax8(BigDecimal dbDeductibleMax8) {
        this.dbDeductibleMax8 = dbDeductibleMax8;
    }

    public BigDecimal getDbDeductibleMin5() {
        return dbDeductibleMin5;
    }

    public void setDbDeductibleMin5(BigDecimal dbDeductibleMin5) {
        this.dbDeductibleMin5 = dbDeductibleMin5;
    }

    public BigDecimal getDbDeductibleMin6() {
        return dbDeductibleMin6;
    }

    public void setDbDeductibleMin6(BigDecimal dbDeductibleMin6) {
        this.dbDeductibleMin6 = dbDeductibleMin6;
    }

    public BigDecimal getDbDeductibleMin7() {
        return dbDeductibleMin7;
    }

    public void setDbDeductibleMin7(BigDecimal dbDeductibleMin7) {
        this.dbDeductibleMin7 = dbDeductibleMin7;
    }

    public BigDecimal getDbDeductibleMin8() {
        return dbDeductibleMin8;
    }

    public void setDbDeductibleMin8(BigDecimal dbDeductibleMin8) {
        this.dbDeductibleMin8 = dbDeductibleMin8;
    }

    public BigDecimal getDbDeductiblePctOfClaim5() {
        return dbDeductiblePctOfClaim5;
    }

    public void setDbDeductiblePctOfClaim5(BigDecimal dbDeductiblePctOfClaim5) {
        this.dbDeductiblePctOfClaim5 = dbDeductiblePctOfClaim5;
    }

    public BigDecimal getDbDeductiblePctOfClaim6() {
        return dbDeductiblePctOfClaim6;
    }

    public void setDbDeductiblePctOfClaim6(BigDecimal dbDeductiblePctOfClaim6) {
        this.dbDeductiblePctOfClaim6 = dbDeductiblePctOfClaim6;
    }

    public BigDecimal getDbDeductiblePctOfClaim7() {
        return dbDeductiblePctOfClaim7;
    }

    public void setDbDeductiblePctOfClaim7(BigDecimal dbDeductiblePctOfClaim7) {
        this.dbDeductiblePctOfClaim7 = dbDeductiblePctOfClaim7;
    }

    public BigDecimal getDbDeductiblePctOfClaim8() {
        return dbDeductiblePctOfClaim8;
    }

    public void setDbDeductiblePctOfClaim8(BigDecimal dbDeductiblePctOfClaim8) {
        this.dbDeductiblePctOfClaim8 = dbDeductiblePctOfClaim8;
    }

    public String getStDeductibleID5() {
        return stDeductibleID5;
    }

    public void setStDeductibleID5(String stDeductibleID5) {
        this.stDeductibleID5 = stDeductibleID5;
    }

    public String getStDeductibleID6() {
        return stDeductibleID6;
    }

    public void setStDeductibleID6(String stDeductibleID6) {
        this.stDeductibleID6 = stDeductibleID6;
    }

    public String getStDeductibleID7() {
        return stDeductibleID7;
    }

    public void setStDeductibleID7(String stDeductibleID7) {
        this.stDeductibleID7 = stDeductibleID7;
    }

    public String getStDeductibleID8() {
        return stDeductibleID8;
    }

    public void setStDeductibleID8(String stDeductibleID8) {
        this.stDeductibleID8 = stDeductibleID8;
    }

    public BigDecimal getDbDeductibleMax1() {
        return dbDeductibleMax1;
    }

    public void setDbDeductibleMax1(BigDecimal dbDeductibleMax1) {
        this.dbDeductibleMax1 = dbDeductibleMax1;
    }

    public BigDecimal getDbDeductibleMax2() {
        return dbDeductibleMax2;
    }

    public void setDbDeductibleMax2(BigDecimal dbDeductibleMax2) {
        this.dbDeductibleMax2 = dbDeductibleMax2;
    }

    public BigDecimal getDbDeductibleMax3() {
        return dbDeductibleMax3;
    }

    public void setDbDeductibleMax3(BigDecimal dbDeductibleMax3) {
        this.dbDeductibleMax3 = dbDeductibleMax3;
    }

    public BigDecimal getDbDeductibleMax4() {
        return dbDeductibleMax4;
    }

    public void setDbDeductibleMax4(BigDecimal dbDeductibleMax4) {
        this.dbDeductibleMax4 = dbDeductibleMax4;
    }

    public BigDecimal getDbDeductibleMin1() {
        return dbDeductibleMin1;
    }

    public void setDbDeductibleMin1(BigDecimal dbDeductibleMin1) {
        this.dbDeductibleMin1 = dbDeductibleMin1;
    }

    public BigDecimal getDbDeductibleMin2() {
        return dbDeductibleMin2;
    }

    public void setDbDeductibleMin2(BigDecimal dbDeductibleMin2) {
        this.dbDeductibleMin2 = dbDeductibleMin2;
    }

    public BigDecimal getDbDeductibleMin3() {
        return dbDeductibleMin3;
    }

    public void setDbDeductibleMin3(BigDecimal dbDeductibleMin3) {
        this.dbDeductibleMin3 = dbDeductibleMin3;
    }

    public BigDecimal getDbDeductibleMin4() {
        return dbDeductibleMin4;
    }

    public void setDbDeductibleMin4(BigDecimal dbDeductibleMin4) {
        this.dbDeductibleMin4 = dbDeductibleMin4;
    }

    public BigDecimal getDbDeductiblePctOfClaim1() {
        return dbDeductiblePctOfClaim1;
    }

    public void setDbDeductiblePctOfClaim1(BigDecimal dbDeductiblePctOfClaim1) {
        this.dbDeductiblePctOfClaim1 = dbDeductiblePctOfClaim1;
    }

    public BigDecimal getDbDeductiblePctOfClaim2() {
        return dbDeductiblePctOfClaim2;
    }

    public void setDbDeductiblePctOfClaim2(BigDecimal dbDeductiblePctOfClaim2) {
        this.dbDeductiblePctOfClaim2 = dbDeductiblePctOfClaim2;
    }

    public BigDecimal getDbDeductiblePctOfClaim3() {
        return dbDeductiblePctOfClaim3;
    }

    public void setDbDeductiblePctOfClaim3(BigDecimal dbDeductiblePctOfClaim3) {
        this.dbDeductiblePctOfClaim3 = dbDeductiblePctOfClaim3;
    }

    public BigDecimal getDbDeductiblePctOfClaim4() {
        return dbDeductiblePctOfClaim4;
    }

    public void setDbDeductiblePctOfClaim4(BigDecimal dbDeductiblePctOfClaim4) {
        this.dbDeductiblePctOfClaim4 = dbDeductiblePctOfClaim4;
    }

    public String getStDeductibleID1() {
        return stDeductibleID1;
    }

    public void setStDeductibleID1(String stDeductibleID1) {
        this.stDeductibleID1 = stDeductibleID1;
    }

    public String getStDeductibleID2() {
        return stDeductibleID2;
    }

    public void setStDeductibleID2(String stDeductibleID2) {
        this.stDeductibleID2 = stDeductibleID2;
    }

    public String getStDeductibleID3() {
        return stDeductibleID3;
    }

    public void setStDeductibleID3(String stDeductibleID3) {
        this.stDeductibleID3 = stDeductibleID3;
    }

    public String getStDeductibleID4() {
        return stDeductibleID4;
    }

    public void setStDeductibleID4(String stDeductibleID4) {
        this.stDeductibleID4 = stDeductibleID4;
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

    public BigDecimal getFeebase1Amount() {
        return feebase1Amount;
    }

    public void setFeebase1Amount(BigDecimal feebase1Amount) {
        this.feebase1Amount = feebase1Amount;
    }

    public BigDecimal getFeebase1Pct() {
        return feebase1Pct;
    }

    public void setFeebase1Pct(BigDecimal feebase1Pct) {
        this.feebase1Pct = feebase1Pct;
    }

    public String getEntID() {
        return entID;
    }

    public void setEntID(String entID) {
        this.entID = entID;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStRegionID() {
        return stRegionID;
    }

    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFotoKendaraan() {
        return fotoKendaraan;
    }

    public void setFotoKendaraan(String fotoKendaraan) {
        this.fotoKendaraan = fotoKendaraan;
    }

    public String getFotoKendaraan2() {
        return fotoKendaraan2;
    }

    public void setFotoKendaraan2(String fotoKendaraan2) {
        this.fotoKendaraan2 = fotoKendaraan2;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public String getFotoSuratKendaraan() {
        return fotoSuratKendaraan;
    }

    public void setFotoSuratKendaraan(String fotoSuratKendaraan) {
        this.fotoSuratKendaraan = fotoSuratKendaraan;
    }

    public BigDecimal getHargaPertanggungan() {
        return hargaPertanggungan;
    }

    public void setHargaPertanggungan(BigDecimal hargaPertanggungan) {
        this.hargaPertanggungan = hargaPertanggungan;
    }

    public BigDecimal getHargaPertanggungan5() {
        return hargaPertanggungan5;
    }

    public void setHargaPertanggungan5(BigDecimal hargaPertanggungan5) {
        this.hargaPertanggungan5 = hargaPertanggungan5;
    }

    public BigDecimal getHargaPertanggungan6() {
        return hargaPertanggungan6;
    }

    public void setHargaPertanggungan6(BigDecimal hargaPertanggungan6) {
        this.hargaPertanggungan6 = hargaPertanggungan6;
    }

    public BigDecimal getHargaPertanggungan7() {
        return hargaPertanggungan7;
    }

    public void setHargaPertanggungan7(BigDecimal hargaPertanggungan7) {
        this.hargaPertanggungan7 = hargaPertanggungan7;
    }

    public BigDecimal getHargaPertanggungan8() {
        return hargaPertanggungan8;
    }

    public void setHargaPertanggungan8(BigDecimal hargaPertanggungan8) {
        this.hargaPertanggungan8 = hargaPertanggungan8;
    }

    public String getIdPengajuanPolis() {
        return idPengajuanPolis;
    }

    public void setIdPengajuanPolis(String idPengajuanPolis) {
        this.idPengajuanPolis = idPengajuanPolis;
    }

    public String getJangkaWaktuComprehensive() {
        return jangkaWaktuComprehensive;
    }

    public void setJangkaWaktuComprehensive(String jangkaWaktuComprehensive) {
        this.jangkaWaktuComprehensive = jangkaWaktuComprehensive;
    }

    public String getJangkaWaktuTlo() {
        return jangkaWaktuTlo;
    }

    public void setJangkaWaktuTlo(String jangkaWaktuTlo) {
        this.jangkaWaktuTlo = jangkaWaktuTlo;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public void setJenisKendaraan(String jenisKendaraan) {
        this.jenisKendaraan = jenisKendaraan;
    }

    public String getKodeBank() {
        return kodeBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getNamaDebitur() {
        return namaDebitur;
    }

    public void setNamaDebitur(String namaDebitur) {
        this.namaDebitur = namaDebitur;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getNoPolisi() {
        return noPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        this.noPolisi = noPolisi;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getNomorMesin() {
        return nomorMesin;
    }

    public void setNomorMesin(String nomorMesin) {
        this.nomorMesin = nomorMesin;
    }

    public String getNomorRangka() {
        return nomorRangka;
    }

    public void setNomorRangka(String nomorRangka) {
        this.nomorRangka = nomorRangka;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getPenggunaan() {
        return penggunaan;
    }

    public void setPenggunaan(String penggunaan) {
        this.penggunaan = penggunaan;
    }

    public String getPerluasan1() {
        return perluasan1;
    }

    public void setPerluasan1(String perluasan1) {
        this.perluasan1 = perluasan1;
    }

    public String getPerluasan2() {
        return perluasan2;
    }

    public void setPerluasan2(String perluasan2) {
        this.perluasan2 = perluasan2;
    }

    public String getPerluasan3() {
        return perluasan3;
    }

    public void setPerluasan3(String perluasan3) {
        this.perluasan3 = perluasan3;
    }

    public String getPerluasan4() {
        return perluasan4;
    }

    public void setPerluasan4(String perluasan4) {
        this.perluasan4 = perluasan4;
    }

    public String getPerluasan5() {
        return perluasan5;
    }

    public void setPerluasan5(String perluasan5) {
        this.perluasan5 = perluasan5;
    }

    public String getPerluasan6() {
        return perluasan6;
    }

    public void setPerluasan6(String perluasan6) {
        this.perluasan6 = perluasan6;
    }

    public String getPerluasan7() {
        return perluasan7;
    }

    public void setPerluasan7(String perluasan7) {
        this.perluasan7 = perluasan7;
    }

    public String getPerluasan8() {
        return perluasan8;
    }

    public void setPerluasan8(String perluasan8) {
        this.perluasan8 = perluasan8;
    }

    public BigDecimal getPremiDasar() {
        return premiDasar;
    }

    public void setPremiDasar(BigDecimal premiDasar) {
        this.premiDasar = premiDasar;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getStatusApproval() {
        return statusApproval;
    }

    public void setStatusApproval(String statusApproval) {
        this.statusApproval = statusApproval;
    }

    public String getTahunPembuatan() {
        return tahunPembuatan;
    }

    public void setTahunPembuatan(String tahunPembuatan) {
        this.tahunPembuatan = tahunPembuatan;
    }

    public Date getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setTanggalAkhir(Date tanggalAkhir) {
        this.tanggalAkhir = tanggalAkhir;
    }

    public Date getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(Date tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }

    public String getTanggalPolis() {
        return tanggalPolis;
    }

    public void setTanggalPolis(String tanggalPolis) {
        this.tanggalPolis = tanggalPolis;
    }

    public String getTempatDuduk() {
        return tempatDuduk;
    }

    public void setTempatDuduk(String tempatDuduk) {
        this.tempatDuduk = tempatDuduk;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public BigDecimal getTotalPremi() {
        return totalPremi;
    }

    public void setTotalPremi(BigDecimal totalPremi) {
        this.totalPremi = totalPremi;
    }

    public String getUnikKey() {
        return unikKey;
    }

    public void setUnikKey(String unikKey) {
        this.unikKey = unikKey;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    DTOList detailPremi;

    public DTOList getDetailPremi() {
        loadDetails();
        return detailPremi;
    }

    public void loadDetails() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (detailPremi == null) {
                detailPremi = ListUtil.getDTOListFromQueryDS(
                        "select * from sppa_kendaraan_detail_premi where no_pengajuan = ? order by tahun",
                        new Object[]{noPengajuan},
                        SPPAOnlineKendaraanDetailPremi.class,"GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDetailPremi(DTOList detailPremi) {
        this.detailPremi = detailPremi;
    }

    public String getCodeMerk(String merk) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    " select vs_code "+
                    " from s_valueset "+
                    " where vs_group = 'INSOBJ_VEH_MEREK' and active_flag = 'Y' "+
                    " and upper(vs_description) = ? ");

            S.setParam(1,merk.toUpperCase().trim());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            //return null;
            return "";

        } finally {

            S.release();
        }
    }



}

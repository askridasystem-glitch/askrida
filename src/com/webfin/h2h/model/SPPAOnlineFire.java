/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfin.h2h.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author doni
 */
public class SPPAOnlineFire extends DTO implements RecordAudit{

    public static String tableName = "sppa_kebakaran_pengajuan_polis";

    public static String fieldMap[][] = {

        {"id", "id*pk"},
        {"penggunaan", "penggunaan"},
        {"kelasKontruksi", "kelas_kontruksi"},
        {"penerangan", "penerangan"},
        {"alamatRisiko", "alamat_risiko"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"propinsi", "propinsi"},
        {"kodePos", "kode_pos"},
        {"kodeOkupasi", "kode_okupasi"},
        {"periodeAwal", "periode_awal"},
        {"periodeAkhir", "periode_akhir"},
        {"nilaiBangunan", "nilai_bangunan"},
        {"perluasan", "perluasan"},
        {"rate", "rate"},
        {"premi", "premi"},
        {"createDate", "create_date"},
        {"approvalDate", "approval_date"},
        {"statusApproval", "status_approval"},
        {"registerNo", "register_no"},
        {"perluasan1", "perluasan1"},
        {"perluasan2", "perluasan2"},
        {"entID", "ent_id"},
        {"kodeBank", "kode_bank"},
        {"grupBank", "grup_bank"},
        {"okupasiDepan", "okupasi_depan"},
        {"okupasiBelakang", "okupasi_belakang"},
        {"okupasiKanan", "okupasi_kanan"},
        {"okupasiKiri", "okupasi_kiri"},
        {"fotoDepan", "foto_depan"},
        {"fotoBelakang", "foto_belakang"},
        {"fotoKanan", "foto_kanan"},
        {"fotoKiri", "foto_kiri"},
        {"deskripsi", "deskripsi"},
        {"catatan", "catatan"},
        {"nopol", "nopol"},
        {"premiDasar", "premi_dasar"},
        {"rateDasar", "rate_dasar"},
        {"tanggalPolis", "tanggal_polis"},
        {"nama", "nama"},
        {"nomorRekening", "nomor_rekening"},
        {"keteranganDepan", "keterangan_depan"},
        {"keteranganBelakang", "keterangan_belakang"},
        {"keteranganKanan", "keterangan_kanan"},
        {"keteranganKiri", "keterangan_kiri"},
        {"deskripsiPenggunaan", "deskripsi_penggunaan"},
        {"kota", "kota"},
        {"kecamatan", "kecamatan"},
        {"kelurahan", "kelurahan"},
        {"nilaiBarang", "nilai_barang"},
        {"totalTsi", "total_tsi"},
        {"jarakDepan", "jarak_depan"},
        {"jarakBelakang", "jarak_belakang"},
        {"jarakKanan", "jarak_kanan"},
        {"jarakKiri", "jarak_kiri"},
        {"nilaiPertanggungan", "nilai_pertanggungan"},
        {"fotoDalam", "foto_dalam"},
        {"fotoTambahan1", "foto_tambahan1"},
        {"fotoTambahan2", "foto_tambahan2"},
        {"fotoTambahan3", "foto_tambahan3"},
        {"fotoTambahan4", "foto_tambahan4"},
        {"fotoTambahan5", "foto_tambahan5"},
        {"noPengajuan", "no_pengajuan"},
        {"unikKey", "unik_key"},
        {"noKtp", "no_ktp"},
        {"noShm", "no_shm"},
        {"biayaPolis", "biaya_polis"},
        {"biayaMaterai", "biaya_materai"},
        {"feebase1Pct", "feebase1_pct"},
        {"feebase1Amount", "feebase1_amount"},
        {"ratePerluasan1", "rate_perluasan1"},
        {"ratePerluasan2", "rate_perluasan2"},
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
        {"stDataID", "data_id"},

    };


    private String id;
    private String penggunaan;
    private String kelasKontruksi;
    private String penerangan;
    private String alamatRisiko;
    private String propinsi;
    private String kodePos;
    private String kodeOkupasi;
    private Date periodeAwal;
    private Date periodeAkhir;
    private BigDecimal nilaiBangunan;
    private String perluasan;
    private BigDecimal rate;
    private BigDecimal premi;
    private Date createDate;
    private Date approvalDate;
    private String statusApproval;
    private String registerNo;
    private String perluasan1;
    private String perluasan2;
    private String entID;
    private String kodeBank;
    private String grupBank;
    private String okupasiDepan;
    private String okupasiBelakang;
    private String okupasiKanan;
    private String okupasiKiri;
    private String fotoDepan;
    private String fotoBelakang;
    private String fotoKanan;
    private String fotoKiri;
    private String deskripsi;
    private String catatan;
    private String nopol;
    private BigDecimal premiDasar;
    private BigDecimal rateDasar;
    private String tanggalPolis;
    private String nama;
    private String nomorRekening;
    private String keteranganDepan;
    private String keteranganBelakang;
    private String keteranganKanan;
    private String keteranganKiri;
    private String deskripsiPenggunaan;
    private String kota;
    private String kecamatan;
    private String kelurahan;
    private BigDecimal nilaiBarang;
    private BigDecimal totalTsi;
    private String jarakDepan;
    private String jarakBelakang;
    private String jarakKanan;
    private String jarakKiri;
    private BigDecimal nilaiPertanggungan;
    private String fotoDalam;
    private String fotoTambahan1 = "no_image.jpg";
    private String fotoTambahan2 = "no_image.jpg";
    private String fotoTambahan3 = "no_image.jpg";
    private String fotoTambahan4 = "no_image.jpg";
    private String fotoTambahan5 = "no_image.jpg";
    private String noPengajuan;
    private String unikKey;
    private String noKtp;
    private String noShm;
    private String stCostCenterCode;
    private String stRegionID;

    private BigDecimal biayaPolis;
    private BigDecimal biayaMaterai;
    private BigDecimal feebase1Pct;
    private BigDecimal feebase1Amount;
    private BigDecimal ratePerluasan1;
    private BigDecimal ratePerluasan2;

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
    private String stDataID;

    public String getStDataID() {
        return stDataID;
    }

    public void setStDataID(String stDataID) {
        this.stDataID = stDataID;
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

    public BigDecimal getRatePerluasan1() {
        return ratePerluasan1;
    }

    public void setRatePerluasan1(BigDecimal ratePerluasan1) {
        this.ratePerluasan1 = ratePerluasan1;
    }

    public BigDecimal getRatePerluasan2() {
        return ratePerluasan2;
    }

    public void setRatePerluasan2(BigDecimal ratePerluasan2) {
        this.ratePerluasan2 = ratePerluasan2;
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

    public String getAlamatRisiko() {
        return alamatRisiko;
    }

    public void setAlamatRisiko(String alamatRisiko) {
        this.alamatRisiko = alamatRisiko;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
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

    public String getDeskripsiPenggunaan() {
        return deskripsiPenggunaan;
    }

    public void setDeskripsiPenggunaan(String deskripsiPenggunaan) {
        this.deskripsiPenggunaan = deskripsiPenggunaan;
    }

    public String getFotoBelakang() {
        return fotoBelakang;
    }

    public void setFotoBelakang(String fotoBelakang) {
        this.fotoBelakang = fotoBelakang;
    }

    public String getFotoDalam() {
        return fotoDalam;
    }

    public void setFotoDalam(String fotoDalam) {
        this.fotoDalam = fotoDalam;
    }

    public String getFotoDepan() {
        return fotoDepan;
    }

    public void setFotoDepan(String fotoDepan) {
        this.fotoDepan = fotoDepan;
    }

    public String getFotoKanan() {
        return fotoKanan;
    }

    public void setFotoKanan(String fotoKanan) {
        this.fotoKanan = fotoKanan;
    }

    public String getFotoKiri() {
        return fotoKiri;
    }

    public void setFotoKiri(String fotoKiri) {
        this.fotoKiri = fotoKiri;
    }

    public String getFotoTambahan1() {
        return fotoTambahan1;
    }

    public void setFotoTambahan1(String fotoTambahan1) {
        this.fotoTambahan1 = fotoTambahan1;
    }

    public String getFotoTambahan2() {
        return fotoTambahan2;
    }

    public void setFotoTambahan2(String fotoTambahan2) {
        this.fotoTambahan2 = fotoTambahan2;
    }

    public String getFotoTambahan3() {
        return fotoTambahan3;
    }

    public void setFotoTambahan3(String fotoTambahan3) {
        this.fotoTambahan3 = fotoTambahan3;
    }

    public String getFotoTambahan4() {
        return fotoTambahan4;
    }

    public void setFotoTambahan4(String fotoTambahan4) {
        this.fotoTambahan4 = fotoTambahan4;
    }

    public String getFotoTambahan5() {
        return fotoTambahan5;
    }

    public void setFotoTambahan5(String fotoTambahan5) {
        this.fotoTambahan5 = fotoTambahan5;
    }

    public String getGrupBank() {
        return grupBank;
    }

    public void setGrupBank(String grupBank) {
        this.grupBank = grupBank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJarakBelakang() {
        return jarakBelakang;
    }

    public void setJarakBelakang(String jarakBelakang) {
        this.jarakBelakang = jarakBelakang;
    }

    public String getJarakDepan() {
        return jarakDepan;
    }

    public void setJarakDepan(String jarakDepan) {
        this.jarakDepan = jarakDepan;
    }

    public String getJarakKanan() {
        return jarakKanan;
    }

    public void setJarakKanan(String jarakKanan) {
        this.jarakKanan = jarakKanan;
    }

    public String getJarakKiri() {
        return jarakKiri;
    }

    public void setJarakKiri(String jarakKiri) {
        this.jarakKiri = jarakKiri;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelasKontruksi() {
        return kelasKontruksi;
    }

    public void setKelasKontruksi(String kelasKontruksi) {
        this.kelasKontruksi = kelasKontruksi;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKeteranganBelakang() {
        return keteranganBelakang;
    }

    public void setKeteranganBelakang(String keteranganBelakang) {
        this.keteranganBelakang = keteranganBelakang;
    }

    public String getKeteranganDepan() {
        return keteranganDepan;
    }

    public void setKeteranganDepan(String keteranganDepan) {
        this.keteranganDepan = keteranganDepan;
    }

    public String getKeteranganKanan() {
        return keteranganKanan;
    }

    public void setKeteranganKanan(String keteranganKanan) {
        this.keteranganKanan = keteranganKanan;
    }

    public String getKeteranganKiri() {
        return keteranganKiri;
    }

    public void setKeteranganKiri(String keteranganKiri) {
        this.keteranganKiri = keteranganKiri;
    }

    public String getKodeBank() {
        return kodeBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank;
    }

    public String getKodeOkupasi() {
        return kodeOkupasi;
    }

    public void setKodeOkupasi(String kodeOkupasi) {
        this.kodeOkupasi = kodeOkupasi;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public BigDecimal getNilaiBangunan() {
        return nilaiBangunan;
    }

    public void setNilaiBangunan(BigDecimal nilaiBangunan) {
        this.nilaiBangunan = nilaiBangunan;
    }

    public BigDecimal getNilaiBarang() {
        return nilaiBarang;
    }

    public void setNilaiBarang(BigDecimal nilaiBarang) {
        this.nilaiBarang = nilaiBarang;
    }

    public BigDecimal getNilaiPertanggungan() {
        return nilaiPertanggungan;
    }

    public void setNilaiPertanggungan(BigDecimal nilaiPertanggungan) {
        this.nilaiPertanggungan = nilaiPertanggungan;
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

    public String getNoShm() {
        return noShm;
    }

    public void setNoShm(String noShm) {
        this.noShm = noShm;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getOkupasiBelakang() {
        return okupasiBelakang;
    }

    public void setOkupasiBelakang(String okupasiBelakang) {
        this.okupasiBelakang = okupasiBelakang;
    }

    public String getOkupasiDepan() {
        return okupasiDepan;
    }

    public void setOkupasiDepan(String okupasiDepan) {
        this.okupasiDepan = okupasiDepan;
    }

    public String getOkupasiKanan() {
        return okupasiKanan;
    }

    public void setOkupasiKanan(String okupasiKanan) {
        this.okupasiKanan = okupasiKanan;
    }

    public String getOkupasiKiri() {
        return okupasiKiri;
    }

    public void setOkupasiKiri(String okupasiKiri) {
        this.okupasiKiri = okupasiKiri;
    }

    public String getPenerangan() {
        return penerangan;
    }

    public void setPenerangan(String penerangan) {
        this.penerangan = penerangan;
    }

    public String getPenggunaan() {
        return penggunaan;
    }

    public void setPenggunaan(String penggunaan) {
        this.penggunaan = penggunaan;
    }

    public Date getPeriodeAkhir() {
        return periodeAkhir;
    }

    public void setPeriodeAkhir(Date periodeAkhir) {
        this.periodeAkhir = periodeAkhir;
    }

    public Date getPeriodeAwal() {
        return periodeAwal;
    }

    public void setPeriodeAwal(Date periodeAwal) {
        this.periodeAwal = periodeAwal;
    }

    public String getPerluasan() {
        return perluasan;
    }

    public void setPerluasan(String perluasan) {
        this.perluasan = perluasan;
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

    public BigDecimal getPremi() {
        return premi;
    }

    public void setPremi(BigDecimal premi) {
        this.premi = premi;
    }

    public BigDecimal getPremiDasar() {
        return premiDasar;
    }

    public void setPremiDasar(BigDecimal premiDasar) {
        this.premiDasar = premiDasar;
    }

    public String getPropinsi() {
        return propinsi;
    }

    public void setPropinsi(String propinsi) {
        this.propinsi = propinsi;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRateDasar() {
        return rateDasar;
    }

    public void setRateDasar(BigDecimal rateDasar) {
        this.rateDasar = rateDasar;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getStatusApproval() {
        return statusApproval;
    }

    public void setStatusApproval(String statusApproval) {
        this.statusApproval = statusApproval;
    }

    public String getTanggalPolis() {
        return tanggalPolis;
    }

    public void setTanggalPolis(String tanggalPolis) {
        this.tanggalPolis = tanggalPolis;
    }

    public BigDecimal getTotalTsi() {
        return totalTsi;
    }

    public void setTotalTsi(BigDecimal totalTsi) {
        this.totalTsi = totalTsi;
    }

    public String getUnikKey() {
        return unikKey;
    }

    public void setUnikKey(String unikKey) {
        this.unikKey = unikKey;
    }
}

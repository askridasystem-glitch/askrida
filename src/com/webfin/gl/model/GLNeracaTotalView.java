/***********************************************************************
 * Module:  com.webfin.gl.model.GLNeracaTotalView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 10:57:56 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;

public class GLNeracaTotalView extends DTO implements RecordAudit {
    /*
    CREATE TABLE gl_dept
    (
    varchar(8) NOT NULL,
    varchar(128) NOT NULL,
    create_date timestamp NOT NULL,
    create_who varchar(32) NOT NULL,
    change_date timestamp,
    change_who varchar(32),
    CONSTRAINT dept_pk PRIMARY KEY (dept_code)
    )
     */

    public static String tableName = "gl_neraca_total";
    private String stGLNeracaID;
    private String stDescription;
    private String stReference1;
    private String stReference2;
    private String stActiveFlag;

    private BigDecimal dbDeposito;
    private BigDecimal dbSertifikatDeposito;
    private BigDecimal dbSuratBerharga;
    private BigDecimal dbPenyertaan;
    private BigDecimal dbProperti;
    private BigDecimal dbPropertiPinjamanHipotek;
    private BigDecimal dbInvestasiLain;
    private BigDecimal dbKasdanBank;
    private BigDecimal dbPiutangPremi;
    private BigDecimal dbPiutangReasuransi;
    private BigDecimal dbAsetReasuransi;
    private BigDecimal dbPiutangHasilInvestasi;
    private BigDecimal dbPiutangLain;
    private BigDecimal dbPiutangLain2;
    private BigDecimal dbBiayaDibayarDimuka;
    private BigDecimal dbAktivaTetap;
    private BigDecimal dbAktivaLain;
    private BigDecimal dbAktivaPajakTangguhan;
    private BigDecimal dbUtangKlaim;
    private BigDecimal dbEstimasiKlaimRetensiSendiri;
    private BigDecimal dbPremiYBMP;
    private BigDecimal dbUtangReasuransi;
    private BigDecimal dbUtangKomis;
    private BigDecimal dbUtangPajak;
    private BigDecimal dbBiayaygmasihharusdibayar;
    private BigDecimal dbUtangSewaGunaUsaha;
    private BigDecimal dbUtangLain;
    private BigDecimal dbUtangJangkaPanjang;
    private BigDecimal dbTitipanModalDisetor;
    private BigDecimal dbTitipanModalDisetor2;
    private BigDecimal dbAgioSaham;
    private BigDecimal dbCadanganUmum;
    private BigDecimal dbKeuntunganKerugianblmdisetor;
    private BigDecimal dbCadanganKhusus;
    private BigDecimal dbLabaRugiDitahan;
    private BigDecimal dbSaldoLabaRugiTahunLalu;
    private BigDecimal dbSaldoLabaRugiTahunBerjalan;
    private BigDecimal dbPremiBruto;
    private BigDecimal dbPremiReasuransi;
    private BigDecimal dbPenurunanKenaikanPYBMP;
    private BigDecimal dbKlaimBruto;
    private BigDecimal dbKlaimReasuransi;
    private BigDecimal dbKenaikanPenurunanEKRS;
    private BigDecimal dbBebanKomisiNetto;
    private BigDecimal dbBebanUnderwritingLainNeto;
    private BigDecimal dbHasilInvestasi;
    private BigDecimal dbBebanUsaha;
    private BigDecimal dbLabaBersih;

    private String stKeterangan;
    public static String fieldMap[][] = {
        {"stGLNeracaID", "gl_ins_id*pk"},
        {"stDescription", "description"},
        {"stReference1", "ref1"},
        {"stReference2", "ref2"},
        {"stActiveFlag", "active_flag"},
        {"dbDeposito", "Deposito"},
        {"dbSertifikatDeposito", "Sertifikat_Deposito"},
        {"dbSuratBerharga", "Surat_Berharga"},
        {"dbPenyertaan", "Penyertaan"},
        {"dbProperti", "Properti"},
        {"dbPropertiPinjamanHipotek", "Properti_Pinjaman_Hipotek"},
        {"dbInvestasiLain", "Investasi_Lain"},
        {"dbKasdanBank", "Kas_dan_Bank"},
        {"dbPiutangPremi", "Piutang_Premi"},
        {"dbPiutangReasuransi", "Piutang_Reasuransi"},
        {"dbAsetReasuransi", "Aset_Reasuransi"},
        {"dbPiutangHasilInvestasi", "Piutang_Hasil_Investasi"},
        {"dbPiutangLain", "Piutang_Lain"},
        {"dbPiutangLain2", "Piutang_Lain_2"},
        {"dbBiayaDibayarDimuka", "Biaya_Dibayar_Dimuka"},
        {"dbAktivaTetap", "Aktiva_Tetap"},
        {"dbAktivaLain", "Aktiva_Lain"},
        {"dbAktivaPajakTangguhan", "Aktiva_Pajak_Tangguhan"},
        {"dbUtangKlaim", "Utang_Klaim"},
        {"dbEstimasiKlaimRetensiSendiri", "Estimasi_Klaim_Retensi_Sendiri"},
        {"dbPremiYBMP", "Premi_YBMP"},
        {"dbUtangReasuransi", "Utang_Reasuransi"},
        {"dbUtangKomis", "Utang_Komis"},
        {"dbUtangPajak", "Utang_Pajak"},
        {"dbBiayaygmasihharusdibayar", "Biaya_yg_masih_harus_dibayar"},
        {"dbUtangSewaGunaUsaha", "Utang_Sewa_Guna_Usaha"},
        {"dbUtangLain", "Utang_Lain"},
        {"dbUtangJangkaPanjang", "Utang_Jangka_Panjang"},
        {"dbTitipanModalDisetor", "Titipan_Modal_Disetor"},
        {"dbTitipanModalDisetor2", "Titipan_Modal_Disetor_2"},
        {"dbAgioSaham", "Agio_Saham"},
        {"dbCadanganUmum", "Cadangan_Umum"},
        {"dbKeuntunganKerugianblmdisetor", "KeuntunganKerugian_blm_disetor"},
        {"dbCadanganKhusus", "Cadangan_Khusus"},
        {"dbLabaRugiDitahan", "Laba_Rugi_Ditahan"},
        {"dbSaldoLabaRugiTahunLalu", "Saldo_Laba_Rugi_Tahun_Lalu"},
        {"dbSaldoLabaRugiTahunBerjalan", "Saldo_Laba_Rugi_Tahun_Berjalan"},
        {"dbPremiBruto", "Premi_Bruto"},
        {"dbPremiReasuransi", "Premi_Reasuransi"},
        {"dbPenurunanKenaikanPYBMP", "Penurunan_Kenaikan_PYBMP"},
        {"dbKlaimBruto", "Klaim_Bruto"},
        {"dbKlaimReasuransi", "Klaim_Reasuransi"},
        {"dbKenaikanPenurunanEKRS", "Kenaikan_Penurunan_EKRS"},
        {"dbBebanKomisiNetto", "Beban_Komisi_Netto"},
        {"dbBebanUnderwritingLainNeto", "Beban_Underwriting_Lain_Neto"},
        {"dbHasilInvestasi", "Hasil_Investasi"},
        {"dbBebanUsaha", "Beban_Usaha"},
        {"dbLabaBersih", "Laba_Bersih"},
        {"stKeterangan", "ket"},
    };

    public static String comboFields[] = {"gl_ins_id", "description"};

    /**
     * @return the stGLNeracaID
     */
    public String getStGLNeracaID() {
        return stGLNeracaID;
    }

    /**
     * @param stGLNeracaID the stGLNeracaID to set
     */
    public void setStGLNeracaID(String stGLNeracaID) {
        this.stGLNeracaID = stGLNeracaID;
    }

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    /**
     * @return the stReference1
     */
    public String getStReference1() {
        return stReference1;
    }

    /**
     * @param stReference1 the stReference1 to set
     */
    public void setStReference1(String stReference1) {
        this.stReference1 = stReference1;
    }

    /**
     * @return the stReference2
     */
    public String getStReference2() {
        return stReference2;
    }

    /**
     * @param stReference2 the stReference2 to set
     */
    public void setStReference2(String stReference2) {
        this.stReference2 = stReference2;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the dbDeposito
     */
    public BigDecimal getDbDeposito() {
        return dbDeposito;
    }

    /**
     * @param dbDeposito the dbDeposito to set
     */
    public void setDbDeposito(BigDecimal dbDeposito) {
        this.dbDeposito = dbDeposito;
    }

    /**
     * @return the dbSertifikatDeposito
     */
    public BigDecimal getDbSertifikatDeposito() {
        return dbSertifikatDeposito;
    }

    /**
     * @param dbSertifikatDeposito the dbSertifikatDeposito to set
     */
    public void setDbSertifikatDeposito(BigDecimal dbSertifikatDeposito) {
        this.dbSertifikatDeposito = dbSertifikatDeposito;
    }

    /**
     * @return the dbSuratBerharga
     */
    public BigDecimal getDbSuratBerharga() {
        return dbSuratBerharga;
    }

    /**
     * @param dbSuratBerharga the dbSuratBerharga to set
     */
    public void setDbSuratBerharga(BigDecimal dbSuratBerharga) {
        this.dbSuratBerharga = dbSuratBerharga;
    }

    /**
     * @return the dbPenyertaan
     */
    public BigDecimal getDbPenyertaan() {
        return dbPenyertaan;
    }

    /**
     * @param dbPenyertaan the dbPenyertaan to set
     */
    public void setDbPenyertaan(BigDecimal dbPenyertaan) {
        this.dbPenyertaan = dbPenyertaan;
    }

    /**
     * @return the dbProperti
     */
    public BigDecimal getDbProperti() {
        return dbProperti;
    }

    /**
     * @param dbProperti the dbProperti to set
     */
    public void setDbProperti(BigDecimal dbProperti) {
        this.dbProperti = dbProperti;
    }

    /**
     * @return the dbPropertiPinjamanHipotek
     */
    public BigDecimal getDbPropertiPinjamanHipotek() {
        return dbPropertiPinjamanHipotek;
    }

    /**
     * @param dbPropertiPinjamanHipotek the dbPropertiPinjamanHipotek to set
     */
    public void setDbPropertiPinjamanHipotek(BigDecimal dbPropertiPinjamanHipotek) {
        this.dbPropertiPinjamanHipotek = dbPropertiPinjamanHipotek;
    }

    /**
     * @return the dbInvestasiLain
     */
    public BigDecimal getDbInvestasiLain() {
        return dbInvestasiLain;
    }

    /**
     * @param dbInvestasiLain the dbInvestasiLain to set
     */
    public void setDbInvestasiLain(BigDecimal dbInvestasiLain) {
        this.dbInvestasiLain = dbInvestasiLain;
    }

    /**
     * @return the dbKasdanBank
     */
    public BigDecimal getDbKasdanBank() {
        return dbKasdanBank;
    }

    /**
     * @param dbKasdanBank the dbKasdanBank to set
     */
    public void setDbKasdanBank(BigDecimal dbKasdanBank) {
        this.dbKasdanBank = dbKasdanBank;
    }

    /**
     * @return the dbPiutangPremi
     */
    public BigDecimal getDbPiutangPremi() {
        return dbPiutangPremi;
    }

    /**
     * @param dbPiutangPremi the dbPiutangPremi to set
     */
    public void setDbPiutangPremi(BigDecimal dbPiutangPremi) {
        this.dbPiutangPremi = dbPiutangPremi;
    }

    /**
     * @return the dbPiutangReasuransi
     */
    public BigDecimal getDbPiutangReasuransi() {
        return dbPiutangReasuransi;
    }

    /**
     * @param dbPiutangReasuransi the dbPiutangReasuransi to set
     */
    public void setDbPiutangReasuransi(BigDecimal dbPiutangReasuransi) {
        this.dbPiutangReasuransi = dbPiutangReasuransi;
    }

    /**
     * @return the dbAsetReasuransi
     */
    public BigDecimal getDbAsetReasuransi() {
        return dbAsetReasuransi;
    }

    /**
     * @param dbAsetReasuransi the dbAsetReasuransi to set
     */
    public void setDbAsetReasuransi(BigDecimal dbAsetReasuransi) {
        this.dbAsetReasuransi = dbAsetReasuransi;
    }

    /**
     * @return the dbPiutangHasilInvestasi
     */
    public BigDecimal getDbPiutangHasilInvestasi() {
        return dbPiutangHasilInvestasi;
    }

    /**
     * @param dbPiutangHasilInvestasi the dbPiutangHasilInvestasi to set
     */
    public void setDbPiutangHasilInvestasi(BigDecimal dbPiutangHasilInvestasi) {
        this.dbPiutangHasilInvestasi = dbPiutangHasilInvestasi;
    }

    /**
     * @return the dbPiutangLain
     */
    public BigDecimal getDbPiutangLain() {
        return dbPiutangLain;
    }

    /**
     * @param dbPiutangLain the dbPiutangLain to set
     */
    public void setDbPiutangLain(BigDecimal dbPiutangLain) {
        this.dbPiutangLain = dbPiutangLain;
    }

    /**
     * @return the dbPiutangLain2
     */
    public BigDecimal getDbPiutangLain2() {
        return dbPiutangLain2;
    }

    /**
     * @param dbPiutangLain2 the dbPiutangLain2 to set
     */
    public void setDbPiutangLain2(BigDecimal dbPiutangLain2) {
        this.dbPiutangLain2 = dbPiutangLain2;
    }

    /**
     * @return the dbBiayaDibayarDimuka
     */
    public BigDecimal getDbBiayaDibayarDimuka() {
        return dbBiayaDibayarDimuka;
    }

    /**
     * @param dbBiayaDibayarDimuka the dbBiayaDibayarDimuka to set
     */
    public void setDbBiayaDibayarDimuka(BigDecimal dbBiayaDibayarDimuka) {
        this.dbBiayaDibayarDimuka = dbBiayaDibayarDimuka;
    }

    /**
     * @return the dbAktivaTetap
     */
    public BigDecimal getDbAktivaTetap() {
        return dbAktivaTetap;
    }

    /**
     * @param dbAktivaTetap the dbAktivaTetap to set
     */
    public void setDbAktivaTetap(BigDecimal dbAktivaTetap) {
        this.dbAktivaTetap = dbAktivaTetap;
    }

    /**
     * @return the dbAktivaLain
     */
    public BigDecimal getDbAktivaLain() {
        return dbAktivaLain;
    }

    /**
     * @param dbAktivaLain the dbAktivaLain to set
     */
    public void setDbAktivaLain(BigDecimal dbAktivaLain) {
        this.dbAktivaLain = dbAktivaLain;
    }

    /**
     * @return the dbAktivaPajakTangguhan
     */
    public BigDecimal getDbAktivaPajakTangguhan() {
        return dbAktivaPajakTangguhan;
    }

    /**
     * @param dbAktivaPajakTangguhan the dbAktivaPajakTangguhan to set
     */
    public void setDbAktivaPajakTangguhan(BigDecimal dbAktivaPajakTangguhan) {
        this.dbAktivaPajakTangguhan = dbAktivaPajakTangguhan;
    }

    /**
     * @return the dbUtangKlaim
     */
    public BigDecimal getDbUtangKlaim() {
        return dbUtangKlaim;
    }

    /**
     * @param dbUtangKlaim the dbUtangKlaim to set
     */
    public void setDbUtangKlaim(BigDecimal dbUtangKlaim) {
        this.dbUtangKlaim = dbUtangKlaim;
    }

    /**
     * @return the dbEstimasiKlaimRetensiSendiri
     */
    public BigDecimal getDbEstimasiKlaimRetensiSendiri() {
        return dbEstimasiKlaimRetensiSendiri;
    }

    /**
     * @param dbEstimasiKlaimRetensiSendiri the dbEstimasiKlaimRetensiSendiri to set
     */
    public void setDbEstimasiKlaimRetensiSendiri(BigDecimal dbEstimasiKlaimRetensiSendiri) {
        this.dbEstimasiKlaimRetensiSendiri = dbEstimasiKlaimRetensiSendiri;
    }

    /**
     * @return the dbPremiYBMP
     */
    public BigDecimal getDbPremiYBMP() {
        return dbPremiYBMP;
    }

    /**
     * @param dbPremiYBMP the dbPremiYBMP to set
     */
    public void setDbPremiYBMP(BigDecimal dbPremiYBMP) {
        this.dbPremiYBMP = dbPremiYBMP;
    }

    /**
     * @return the dbUtangReasuransi
     */
    public BigDecimal getDbUtangReasuransi() {
        return dbUtangReasuransi;
    }

    /**
     * @param dbUtangReasuransi the dbUtangReasuransi to set
     */
    public void setDbUtangReasuransi(BigDecimal dbUtangReasuransi) {
        this.dbUtangReasuransi = dbUtangReasuransi;
    }

    /**
     * @return the dbUtangKomis
     */
    public BigDecimal getDbUtangKomis() {
        return dbUtangKomis;
    }

    /**
     * @param dbUtangKomis the dbUtangKomis to set
     */
    public void setDbUtangKomis(BigDecimal dbUtangKomis) {
        this.dbUtangKomis = dbUtangKomis;
    }

    /**
     * @return the dbUtangPajak
     */
    public BigDecimal getDbUtangPajak() {
        return dbUtangPajak;
    }

    /**
     * @param dbUtangPajak the dbUtangPajak to set
     */
    public void setDbUtangPajak(BigDecimal dbUtangPajak) {
        this.dbUtangPajak = dbUtangPajak;
    }

    /**
     * @return the dbBiayaygmasihharusdibayar
     */
    public BigDecimal getDbBiayaygmasihharusdibayar() {
        return dbBiayaygmasihharusdibayar;
    }

    /**
     * @param dbBiayaygmasihharusdibayar the dbBiayaygmasihharusdibayar to set
     */
    public void setDbBiayaygmasihharusdibayar(BigDecimal dbBiayaygmasihharusdibayar) {
        this.dbBiayaygmasihharusdibayar = dbBiayaygmasihharusdibayar;
    }

    /**
     * @return the dbUtangSewaGunaUsaha
     */
    public BigDecimal getDbUtangSewaGunaUsaha() {
        return dbUtangSewaGunaUsaha;
    }

    /**
     * @param dbUtangSewaGunaUsaha the dbUtangSewaGunaUsaha to set
     */
    public void setDbUtangSewaGunaUsaha(BigDecimal dbUtangSewaGunaUsaha) {
        this.dbUtangSewaGunaUsaha = dbUtangSewaGunaUsaha;
    }

    /**
     * @return the dbUtangLain
     */
    public BigDecimal getDbUtangLain() {
        return dbUtangLain;
    }

    /**
     * @param dbUtangLain the dbUtangLain to set
     */
    public void setDbUtangLain(BigDecimal dbUtangLain) {
        this.dbUtangLain = dbUtangLain;
    }

    /**
     * @return the dbUtangJangkaPanjang
     */
    public BigDecimal getDbUtangJangkaPanjang() {
        return dbUtangJangkaPanjang;
    }

    /**
     * @param dbUtangJangkaPanjang the dbUtangJangkaPanjang to set
     */
    public void setDbUtangJangkaPanjang(BigDecimal dbUtangJangkaPanjang) {
        this.dbUtangJangkaPanjang = dbUtangJangkaPanjang;
    }

    /**
     * @return the dbTitipanModalDisetor
     */
    public BigDecimal getDbTitipanModalDisetor() {
        return dbTitipanModalDisetor;
    }

    /**
     * @param dbTitipanModalDisetor the dbTitipanModalDisetor to set
     */
    public void setDbTitipanModalDisetor(BigDecimal dbTitipanModalDisetor) {
        this.dbTitipanModalDisetor = dbTitipanModalDisetor;
    }

    /**
     * @return the dbTitipanModalDisetor2
     */
    public BigDecimal getDbTitipanModalDisetor2() {
        return dbTitipanModalDisetor2;
    }

    /**
     * @param dbTitipanModalDisetor2 the dbTitipanModalDisetor2 to set
     */
    public void setDbTitipanModalDisetor2(BigDecimal dbTitipanModalDisetor2) {
        this.dbTitipanModalDisetor2 = dbTitipanModalDisetor2;
    }

    /**
     * @return the dbAgioSaham
     */
    public BigDecimal getDbAgioSaham() {
        return dbAgioSaham;
    }

    /**
     * @param dbAgioSaham the dbAgioSaham to set
     */
    public void setDbAgioSaham(BigDecimal dbAgioSaham) {
        this.dbAgioSaham = dbAgioSaham;
    }

    /**
     * @return the dbCadanganUmum
     */
    public BigDecimal getDbCadanganUmum() {
        return dbCadanganUmum;
    }

    /**
     * @param dbCadanganUmum the dbCadanganUmum to set
     */
    public void setDbCadanganUmum(BigDecimal dbCadanganUmum) {
        this.dbCadanganUmum = dbCadanganUmum;
    }

    /**
     * @return the dbKeuntunganKerugianblmdisetor
     */
    public BigDecimal getDbKeuntunganKerugianblmdisetor() {
        return dbKeuntunganKerugianblmdisetor;
    }

    /**
     * @param dbKeuntunganKerugianblmdisetor the dbKeuntunganKerugianblmdisetor to set
     */
    public void setDbKeuntunganKerugianblmdisetor(BigDecimal dbKeuntunganKerugianblmdisetor) {
        this.dbKeuntunganKerugianblmdisetor = dbKeuntunganKerugianblmdisetor;
    }

    /**
     * @return the dbCadanganKhusus
     */
    public BigDecimal getDbCadanganKhusus() {
        return dbCadanganKhusus;
    }

    /**
     * @param dbCadanganKhusus the dbCadanganKhusus to set
     */
    public void setDbCadanganKhusus(BigDecimal dbCadanganKhusus) {
        this.dbCadanganKhusus = dbCadanganKhusus;
    }

    /**
     * @return the dbLabaRugiDitahan
     */
    public BigDecimal getDbLabaRugiDitahan() {
        return dbLabaRugiDitahan;
    }

    /**
     * @param dbLabaRugiDitahan the dbLabaRugiDitahan to set
     */
    public void setDbLabaRugiDitahan(BigDecimal dbLabaRugiDitahan) {
        this.dbLabaRugiDitahan = dbLabaRugiDitahan;
    }

    /**
     * @return the dbSaldoLabaRugiTahunLalu
     */
    public BigDecimal getDbSaldoLabaRugiTahunLalu() {
        return dbSaldoLabaRugiTahunLalu;
    }

    /**
     * @param dbSaldoLabaRugiTahunLalu the dbSaldoLabaRugiTahunLalu to set
     */
    public void setDbSaldoLabaRugiTahunLalu(BigDecimal dbSaldoLabaRugiTahunLalu) {
        this.dbSaldoLabaRugiTahunLalu = dbSaldoLabaRugiTahunLalu;
    }

    /**
     * @return the dbSaldoLabaRugiTahunBerjalan
     */
    public BigDecimal getDbSaldoLabaRugiTahunBerjalan() {
        return dbSaldoLabaRugiTahunBerjalan;
    }

    /**
     * @param dbSaldoLabaRugiTahunBerjalan the dbSaldoLabaRugiTahunBerjalan to set
     */
    public void setDbSaldoLabaRugiTahunBerjalan(BigDecimal dbSaldoLabaRugiTahunBerjalan) {
        this.dbSaldoLabaRugiTahunBerjalan = dbSaldoLabaRugiTahunBerjalan;
    }

    /**
     * @return the dbPremiBruto
     */
    public BigDecimal getDbPremiBruto() {
        return dbPremiBruto;
    }

    /**
     * @param dbPremiBruto the dbPremiBruto to set
     */
    public void setDbPremiBruto(BigDecimal dbPremiBruto) {
        this.dbPremiBruto = dbPremiBruto;
    }

    /**
     * @return the dbPremiReasuransi
     */
    public BigDecimal getDbPremiReasuransi() {
        return dbPremiReasuransi;
    }

    /**
     * @param dbPremiReasuransi the dbPremiReasuransi to set
     */
    public void setDbPremiReasuransi(BigDecimal dbPremiReasuransi) {
        this.dbPremiReasuransi = dbPremiReasuransi;
    }

    /**
     * @return the dbPenurunanKenaikanPYBMP
     */
    public BigDecimal getDbPenurunanKenaikanPYBMP() {
        return dbPenurunanKenaikanPYBMP;
    }

    /**
     * @param dbPenurunanKenaikanPYBMP the dbPenurunanKenaikanPYBMP to set
     */
    public void setDbPenurunanKenaikanPYBMP(BigDecimal dbPenurunanKenaikanPYBMP) {
        this.dbPenurunanKenaikanPYBMP = dbPenurunanKenaikanPYBMP;
    }

    /**
     * @return the dbKlaimBruto
     */
    public BigDecimal getDbKlaimBruto() {
        return dbKlaimBruto;
    }

    /**
     * @param dbKlaimBruto the dbKlaimBruto to set
     */
    public void setDbKlaimBruto(BigDecimal dbKlaimBruto) {
        this.dbKlaimBruto = dbKlaimBruto;
    }

    /**
     * @return the dbKlaimReasuransi
     */
    public BigDecimal getDbKlaimReasuransi() {
        return dbKlaimReasuransi;
    }

    /**
     * @param dbKlaimReasuransi the dbKlaimReasuransi to set
     */
    public void setDbKlaimReasuransi(BigDecimal dbKlaimReasuransi) {
        this.dbKlaimReasuransi = dbKlaimReasuransi;
    }

    /**
     * @return the dbKenaikanPenurunanEKRS
     */
    public BigDecimal getDbKenaikanPenurunanEKRS() {
        return dbKenaikanPenurunanEKRS;
    }

    /**
     * @param dbKenaikanPenurunanEKRS the dbKenaikanPenurunanEKRS to set
     */
    public void setDbKenaikanPenurunanEKRS(BigDecimal dbKenaikanPenurunanEKRS) {
        this.dbKenaikanPenurunanEKRS = dbKenaikanPenurunanEKRS;
    }

    /**
     * @return the dbBebanKomisiNetto
     */
    public BigDecimal getDbBebanKomisiNetto() {
        return dbBebanKomisiNetto;
    }

    /**
     * @param dbBebanKomisiNetto the dbBebanKomisiNetto to set
     */
    public void setDbBebanKomisiNetto(BigDecimal dbBebanKomisiNetto) {
        this.dbBebanKomisiNetto = dbBebanKomisiNetto;
    }

    /**
     * @return the dbBebanUnderwritingLainNeto
     */
    public BigDecimal getDbBebanUnderwritingLainNeto() {
        return dbBebanUnderwritingLainNeto;
    }

    /**
     * @param dbBebanUnderwritingLainNeto the dbBebanUnderwritingLainNeto to set
     */
    public void setDbBebanUnderwritingLainNeto(BigDecimal dbBebanUnderwritingLainNeto) {
        this.dbBebanUnderwritingLainNeto = dbBebanUnderwritingLainNeto;
    }

    /**
     * @return the dbHasilInvestasi
     */
    public BigDecimal getDbHasilInvestasi() {
        return dbHasilInvestasi;
    }

    /**
     * @param dbHasilInvestasi the dbHasilInvestasi to set
     */
    public void setDbHasilInvestasi(BigDecimal dbHasilInvestasi) {
        this.dbHasilInvestasi = dbHasilInvestasi;
    }

    /**
     * @return the dbBebanUsaha
     */
    public BigDecimal getDbBebanUsaha() {
        return dbBebanUsaha;
    }

    /**
     * @param dbBebanUsaha the dbBebanUsaha to set
     */
    public void setDbBebanUsaha(BigDecimal dbBebanUsaha) {
        this.dbBebanUsaha = dbBebanUsaha;
    }

    /**
     * @return the dbLabaBersih
     */
    public BigDecimal getDbLabaBersih() {
        return dbLabaBersih;
    }

    /**
     * @param dbLabaBersih the dbLabaBersih to set
     */
    public void setDbLabaBersih(BigDecimal dbLabaBersih) {
        this.dbLabaBersih = dbLabaBersih;
    }

    /**
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }
}

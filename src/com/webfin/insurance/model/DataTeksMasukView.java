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

public class DataTeksMasukView extends DTO implements RecordAudit {
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

   public static String tableName = "data_teks_masuk";

   public static String fieldMap[][] = {
      {"stDataID","data_id*pk"},
      {"stGroupID","group_id"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"stPolicyTypeID","pol_type_id"},
      {"stKodeBank","kode_bank"},
      {"stEntityID","entity_id"},
      {"stNama","nama"},
      {"stUsia","usia"},
      {"dtTanggalLahir","tgl_lahir"},
      {"dtTanggalAwal","tgl_awal"},
      {"dtTanggalAkhir","tgl_akhir"},
      {"dbInsuredAmount","insured_amount"},
      {"dbPremiTotal","premi_total"},
      {"dtTanggalProses","tgl_proses"},
      {"stProsesFlag","proses_flag"},
      {"stKategori","kategori"},
      {"stPolicyNo","pol_no"},
      {"dbClaimAmount","claim_amount"},
      {"stStatus","status"},
      {"stJenisKredit","jenis_kredit"},
      {"stJenisPekerjaan","jenis_pekerjaan"},
      {"stJenisIdentitas","jenis_identitas"},
      {"stNomorIdentitas","no_identitas"},
      {"stNoRekeningPinjaman","no_rek_pinjaman"},
      {"stNoPerjanjianKredit","no_perjanjian_kredit"},
      {"stJenisPolis","jenis_polis*n"},
      {"stNamaBank","nama_bank*n"},
      {"dtTanggalTransfer","tgl_transfer"},
      {"dbRate","rate"},
      {"stJangkaWaktuBulan","jk_waktu_bulan"},
      {"stNotes","notes"},
      {"stValid","valid_f"},
      {"stSPK","spk_f"},
      {"stSPKFile","spk_file"},
      {"stPKBFile","pkb_file"},
      {"stEKGFile","ekg_file"},
      {"stAdaulFile","adaul_file"},
      {"stThoraxFile","thorax_file"},
      {"stTRMFile","trm_file"},
      {"stHIVFile","hiv_file"},
      {"stDescription","description"},
      {"dbPremiBank","premi_bank"},
      {"stStatusKerja","status_kerja"},
      {"stJenisKreditAskrida","jenis_kredit_askrida"},
      {"stKodeTarif","kode_tarif"},
      {"stPaketCoverage","paket_coverage"},
      {"dbKomisi1Pct","komisi1_pct"},
      {"dbKomisi1Amount","komisi1_amount"},
      {"dbKomisi2Pct","komisi2_pct"},
      {"dbKomisi2Amount","komisi2_amount"},
      {"dbFeeBase1Pct","feebase1_pct"},
      {"dbFeeBase1Amount","feebase1_amount"},
      {"dbFeeBase2Pct","feebase2_pct"},
      {"dbFeeBase2Amount","feebase2_amount"},
      {"dtTanggalBayar","tgl_bayar"},
      {"stPaketCoverageAskrida","paket_coverage_askrida"},
      {"stKomisi1EntityID","komisi1_ent_id"},
      {"stKomisi2EntityID","komisi2_ent_id"},
      {"stFeeBase1EntityID","feebase1_ent_id"},
      {"stFeeBase2EntityID","feebase2_ent_id"},
      {"dtTanggalPolis","tgl_polis"},
      {"stCostCenterCodeCore","cc_code_core"},
      {"stRegionIDCore","region_id_core"},
      {"dbDiscountPct","discount_pct"},
      {"dbDiscountAmount","discount_amount"},
      {"stKodeTransaksi","kode_transaksi"},
      {"stNomorArsipPembayaran","no_arsip_pembayaran"},
      {"stJenisData","jenis_data"},
      {"dbBiayaPolis","biaya_polis"},
      {"dbBiayaMaterai","biaya_materai"},
      {"dbTotalTagihanPremi","total_tagihan"},
      {"dtTanggalSTNC","tanggal_stnc"},
      {"stPersetujuanPusat","persetujuan_pusat"},
      {"dbFeeBasePPN1Include","feebase1_ppn_include"},
      {"dbFeeBasePPN1Exclude","feebase1_ppn_exclude"},
      {"stNoPolisInduk","nopolis_induk"},
      {"dbPremiRefund","premi_refund"},
      {"stNomorBuktiBayar","no_bukti_bayar"},
      {"stNomorReferensiPembayaran","no_referensi_pembayaran"},
      {"stJenisKreditDebitur","jenis_kredit_debitur"},
      {"stSumberPembayaran","sumber_pembayaran"},
      {"stKlausula","klausula"},
      {"stWarranty","warranty"},
      {"stCaseByCaseFlag","cbc_flag"},
      {"dbBrokerageIncludePPNPct","brokerage_include_ppn_pct"},
      {"dbBrokerageIncludePPNAmount","brokerage_include_ppn_amount"},
      {"dbBrokerageExcludePPNPct","brokerage_exclude_ppn_pct"},
      {"dbBrokerageExcludePPNAmount","brokerage_exclude_ppn_amount"},
      {"stBrokerageEntityID","brokerage_ent_id"},
      {"stTransactionNo","trx_no"},
      {"stJenisPengajuan","jenis_pengajuan"},
      {"stNoRekeningPinjamanLama","no_rek_pinjaman_lama"},
      {"stPolicyNoBefore","pol_no_before"},
      {"dbFeeBase1PPNPct","feebase1_ppn_pct"},
      {"stPromoFlag","promo_flag"},
      {"dtTanggalAwal100","tgl_awal_100"},
      {"dtTanggalAkhir100","tgl_akhir_100"},
      {"dbPremi100","premi_100"},
      {"dbPremiSisa","premi_sisa"},
      {"stPembayaranPremi","pembayaran_premi"},
      {"dbKomisi1IncludePPNPct","komisi1_ppn_include_pct"},
      {"dbKomisi1IncludePPNAmount","komisi1_ppn_include"},
   };

   private String stDataID;
   private String stGroupID;
   private String stCostCenterCode;
   private String stRegionID;
   private String stPolicyTypeID;
   private String stKodeBank;
   private String stNama;
   private String stUsia;
   private Date dtTanggalLahir;
   private Date dtTanggalAwal;
   private Date dtTanggalAkhir;
   private BigDecimal dbInsuredAmount;
   private BigDecimal dbPremiTotal;
   private Date dtTanggalProses;
   private String stProsesFlag;
   private String stKategori;
   private String stPolicyNo;
   private BigDecimal dbClaimAmount;
   private String stStatus;
   private String stJenisKredit;
   private String stJenisPekerjaan;
   private String stNoRekeningPinjaman;
   private String stNoPerjanjianKredit;
   private String stNomorIdentitas;
   private String stJenisPolis;
   private String stNamaBank;
   private String stJenisIdentitas;
   private String stEntityID;
   private Date dtTanggalTransfer;
   private BigDecimal dbRate;
   private String stJangkaWaktuBulan;
   private String stNotes;
   private String stValid;
   private String stSPK;
   private String stSPKFile;
   private String stPKBFile;
   private String stEKGFile;
   private String stAdaulFile;
   private String stThoraxFile;
   private String stTRMFile;
   private String stHIVFile;
   private String stDescription;
   private BigDecimal dbPremiBank;
   private String stStatusKerja;
   private String stJenisKreditAskrida;
   private String stKodeTarif;
   private String stPaketCoverage;
   private BigDecimal dbKomisi1Pct;
   private BigDecimal dbKomisi1Amount;
   private BigDecimal dbKomisi2Pct;
   private BigDecimal dbKomisi2Amount;
   private BigDecimal dbFeeBase1Pct;
   private BigDecimal dbFeeBase1Amount;
   private BigDecimal dbFeeBase2Pct;
   private BigDecimal dbFeeBase2Amount;
   private Date dtTanggalBayar;
   private String stPaketCoverageAskrida;
   private String stKomisi1EntityID;
   private String stKomisi2EntityID;
   private String stFeeBase1EntityID;
   private String stFeeBase2EntityID;
   private Date dtTanggalPolis;
   private String stCostCenterCodeCore;
   private String stRegionIDCore;
   private BigDecimal dbDiscountPct;
   private BigDecimal dbDiscountAmount;
   private String stKodeTransaksi;
   private String stNomorArsipPembayaran;
   private String stJenisData;
   private BigDecimal dbBiayaPolis;
   private BigDecimal dbBiayaMaterai;
   private BigDecimal dbTotalTagihanPremi;
   private Date dtTanggalSTNC;
   private String stPersetujuanPusat;
   private BigDecimal dbFeeBasePPN1Include;
   private BigDecimal dbFeeBasePPN1Exclude;
   private String stNoPolisInduk;
   private BigDecimal dbPremiRefund;
   private String stNomorBuktiBayar;
   private String stNomorReferensiPembayaran;
   private String stJenisKreditDebitur;
   private String stSumberPembayaran;
   private String stKlausula;
   private String stWarranty;
   private String stCaseByCaseFlag;
   private BigDecimal dbBrokerageIncludePPNPct;
   private BigDecimal dbBrokerageIncludePPNAmount;
   private BigDecimal dbBrokerageExcludePPNPct;
   private BigDecimal dbBrokerageExcludePPNAmount;
   private String stTransactionNo;
   private String stJenisPengajuan;
   private String stNoRekeningPinjamanLama;
   private String stPolicyNoBefore;
   private BigDecimal dbFeeBase1PPNPct;
   private String stPromoFlag;
   private Date dtTanggalAwal100;
   private Date dtTanggalAkhir100;
   private BigDecimal dbPremi100;
   private BigDecimal dbPremiSisa;
   private String stPembayaranPremi;
   private BigDecimal dbKomisi1IncludePPNPct;
   private BigDecimal dbKomisi1IncludePPNAmount;

    public BigDecimal getDbKomisi1IncludePPNAmount() {
        return dbKomisi1IncludePPNAmount;
    }

    public void setDbKomisi1IncludePPNAmount(BigDecimal dbKomisi1IncludePPNAmount) {
        this.dbKomisi1IncludePPNAmount = dbKomisi1IncludePPNAmount;
    }

    public BigDecimal getDbKomisi1IncludePPNPct() {
        return dbKomisi1IncludePPNPct;
    }

    public void setDbKomisi1IncludePPNPct(BigDecimal dbKomisi1IncludePPNPct) {
        this.dbKomisi1IncludePPNPct = dbKomisi1IncludePPNPct;
    }

    public String getStPembayaranPremi() {
        return stPembayaranPremi;
    }

    public void setStPembayaranPremi(String stPembayaranPremi) {
        this.stPembayaranPremi = stPembayaranPremi;
    }

    public BigDecimal getDbPremi100() {
        return dbPremi100;
    }

    public void setDbPremi100(BigDecimal dbPremi100) {
        this.dbPremi100 = dbPremi100;
    }

    public BigDecimal getDbPremiSisa() {
        return dbPremiSisa;
    }

    public void setDbPremiSisa(BigDecimal dbPremiSisa) {
        this.dbPremiSisa = dbPremiSisa;
    }

    public Date getDtTanggalAkhir100() {
        return dtTanggalAkhir100;
    }

    public void setDtTanggalAkhir100(Date dtTanggalAkhir100) {
        this.dtTanggalAkhir100 = dtTanggalAkhir100;
    }

    public Date getDtTanggalAwal100() {
        return dtTanggalAwal100;
    }

    public void setDtTanggalAwal100(Date dtTanggalAwal100) {
        this.dtTanggalAwal100 = dtTanggalAwal100;
    }

    public String getStPromoFlag() {
        return stPromoFlag;
    }

    public void setStPromoFlag(String stPromoFlag) {
        this.stPromoFlag = stPromoFlag;
    }

   public boolean isPromo(){
       return Tools.isYes(stPromoFlag);
   }

    public BigDecimal getDbFeeBase1PPNPct() {
        return dbFeeBase1PPNPct;
    }

    public void setDbFeeBase1PPNPct(BigDecimal dbFeeBase1PPNPct) {
        this.dbFeeBase1PPNPct = dbFeeBase1PPNPct;
    }

    public String getStJenisPengajuan() {
        return stJenisPengajuan;
    }

    public void setStJenisPengajuan(String stJenisPengajuan) {
        this.stJenisPengajuan = stJenisPengajuan;
    }

    public String getStNoRekeningPinjamanLama() {
        return stNoRekeningPinjamanLama;
    }

    public void setStNoRekeningPinjamanLama(String stNoRekeningPinjamanLama) {
        this.stNoRekeningPinjamanLama = stNoRekeningPinjamanLama;
    }

    public String getStPolicyNoBefore() {
        return stPolicyNoBefore;
    }

    public void setStPolicyNoBefore(String stPolicyNoBefore) {
        this.stPolicyNoBefore = stPolicyNoBefore;
    }

    public String getStTransactionNo() {
        return stTransactionNo;
    }

    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
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

    public String getStBrokerageEntityID() {
        return stBrokerageEntityID;
    }

    public void setStBrokerageEntityID(String stBrokerageEntityID) {
        this.stBrokerageEntityID = stBrokerageEntityID;
    }
   private String stBrokerageEntityID;

    public String getStCaseByCaseFlag() {
        return stCaseByCaseFlag;
    }

    public void setStCaseByCaseFlag(String stCaseByCaseFlag) {
        this.stCaseByCaseFlag = stCaseByCaseFlag;
    }

    public boolean isCaseByCase(){
        return Tools.isYes(stCaseByCaseFlag);
    }

    public String getStWarranty() {
        return stWarranty;
    }

    public void setStWarranty(String stWarranty) {
        this.stWarranty = stWarranty;
    }

    public String getStKlausula() {
        return stKlausula;
    }

    public void setStKlausula(String stKlausula) {
        this.stKlausula = stKlausula;
    }

    /**
     * @return the stDataID
     */
    public String getStDataID() {
        return stDataID;
    }

    /**
     * @param stDataID the stDataID to set
     */
    public void setStDataID(String stDataID) {
        this.stDataID = stDataID;
    }

    /**
     * @return the stGroupID
     */
    public String getStGroupID() {
        return stGroupID;
    }

    /**
     * @param stGroupID the stGroupID to set
     */
    public void setStGroupID(String stGroupID) {
        this.stGroupID = stGroupID;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stRegionID
     */
    public String getStRegionID() {
        return stRegionID;
    }

    /**
     * @param stRegionID the stRegionID to set
     */
    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    /**
     * @return the stKodeBank
     */
    public String getStKodeBank() {
        return stKodeBank;
    }

    /**
     * @param stKodeBank the stKodeBank to set
     */
    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    /**
     * @return the stNama
     */
    public String getStNama() {
        return stNama;
    }

    /**
     * @param stNama the stNama to set
     */
    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    /**
     * @return the stUsia
     */
    public String getStUsia() {
        return stUsia;
    }

    /**
     * @param stUsia the stUsia to set
     */
    public void setStUsia(String stUsia) {
        this.stUsia = stUsia;
    }

    /**
     * @return the dtTanggalLahir
     */
    public Date getDtTanggalLahir() {
        return dtTanggalLahir;
    }

    /**
     * @param dtTanggalLahir the dtTanggalLahir to set
     */
    public void setDtTanggalLahir(Date dtTanggalLahir) {
        this.dtTanggalLahir = dtTanggalLahir;
    }

    /**
     * @return the dtTanggalAwal
     */
    public Date getDtTanggalAwal() {
        return dtTanggalAwal;
    }

    /**
     * @param dtTanggalAwal the dtTanggalAwal to set
     */
    public void setDtTanggalAwal(Date dtTanggalAwal) {
        this.dtTanggalAwal = dtTanggalAwal;
    }

    /**
     * @return the dtTanggalAkhir
     */
    public Date getDtTanggalAkhir() {
        return dtTanggalAkhir;
    }

    /**
     * @param dtTanggalAkhir the dtTanggalAkhir to set
     */
    public void setDtTanggalAkhir(Date dtTanggalAkhir) {
        this.dtTanggalAkhir = dtTanggalAkhir;
    }

    /**
     * @return the dbInsuredAmount
     */
    public BigDecimal getDbInsuredAmount() {
        return dbInsuredAmount;
    }

    /**
     * @param dbInsuredAmount the dbInsuredAmount to set
     */
    public void setDbInsuredAmount(BigDecimal dbInsuredAmount) {
        this.dbInsuredAmount = dbInsuredAmount;
    }

    /**
     * @return the dbPremiTotal
     */
    public BigDecimal getDbPremiTotal() {
        return dbPremiTotal;
    }

    /**
     * @param dbPremiTotal the dbPremiTotal to set
     */
    public void setDbPremiTotal(BigDecimal dbPremiTotal) {
        this.dbPremiTotal = dbPremiTotal;
    }

    /**
     * @return the dtTanggalProses
     */
    public Date getDtTanggalProses() {
        return dtTanggalProses;
    }

    /**
     * @param dtTanggalProses the dtTanggalProses to set
     */
    public void setDtTanggalProses(Date dtTanggalProses) {
        this.dtTanggalProses = dtTanggalProses;
    }

    /**
     * @return the stProsesFlag
     */
    public String getStProsesFlag() {
        return stProsesFlag;
    }

    /**
     * @param stProsesFlag the stProsesFlag to set
     */
    public void setStProsesFlag(String stProsesFlag) {
        this.stProsesFlag = stProsesFlag;
    }

    /**
     * @return the stKategori
     */
    public String getStKategori() {
        return stKategori;
    }

    /**
     * @param stKategori the stKategori to set
     */
    public void setStKategori(String stKategori) {
        this.stKategori = stKategori;
    }

    /**
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    /**
     * @return the dbClaimAmount
     */
    public BigDecimal getDbClaimAmount() {
        return dbClaimAmount;
    }

    /**
     * @param dbClaimAmount the dbClaimAmount to set
     */
    public void setDbClaimAmount(BigDecimal dbClaimAmount) {
        this.dbClaimAmount = dbClaimAmount;
    }

    /**
     * @return the stStatus
     */
    public String getStStatus() {
        return stStatus;
    }

    /**
     * @param stStatus the stStatus to set
     */
    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    /**
     * @return the stJenisKredit
     */
    public String getStJenisKredit() {
        return stJenisKredit;
    }

    /**
     * @param stJenisKredit the stJenisKredit to set
     */
    public void setStJenisKredit(String stJenisKredit) {
        this.stJenisKredit = stJenisKredit;
    }

    /**
     * @return the stJenisPekerjaan
     */
    public String getStJenisPekerjaan() {
        return stJenisPekerjaan;
    }

    /**
     * @param stJenisPekerjaan the stJenisPekerjaan to set
     */
    public void setStJenisPekerjaan(String stJenisPekerjaan) {
        this.stJenisPekerjaan = stJenisPekerjaan;
    }

    /**
     * @return the stNoRekeningPinjaman
     */
    public String getStNoRekeningPinjaman() {
        return stNoRekeningPinjaman;
    }

    /**
     * @param stNoRekeningPinjaman the stNoRekeningPinjaman to set
     */
    public void setStNoRekeningPinjaman(String stNoRekeningPinjaman) {
        this.stNoRekeningPinjaman = stNoRekeningPinjaman;
    }

    /**
     * @return the stNoPerjanjianKredit
     */
    public String getStNoPerjanjianKredit() {
        return stNoPerjanjianKredit;
    }

    /**
     * @param stNoPerjanjianKredit the stNoPerjanjianKredit to set
     */
    public void setStNoPerjanjianKredit(String stNoPerjanjianKredit) {
        this.stNoPerjanjianKredit = stNoPerjanjianKredit;
    }

    /**
     * @return the stNomorKTP
     */
    public String getStNomorIdentitas() {
        return stNomorIdentitas;
    }

    /**
     * @param stNomorKTP the stNomorKTP to set
     */
    public void setStNomorIdentitas(String stNomorIdentitas) {
        this.stNomorIdentitas = stNomorIdentitas;
    }

    /**
     * @return the stJenisPolis
     */
    public String getStJenisPolis() {
        return stJenisPolis;
    }

    /**
     * @param stJenisPolis the stJenisPolis to set
     */
    public void setStJenisPolis(String stJenisPolis) {
        this.stJenisPolis = stJenisPolis;
    }

    /**
     * @return the stNamaBank
     */
    public String getStNamaBank() {
        return stNamaBank;
    }

    /**
     * @param stNamaBank the stNamaBank to set
     */
    public void setStNamaBank(String stNamaBank) {
        this.stNamaBank = stNamaBank;
    }

    /**
     * @return the stJenisIdentitas
     */
    public String getStJenisIdentitas() {
        return stJenisIdentitas;
    }

    /**
     * @param stJenisIdentitas the stJenisIdentitas to set
     */
    public void setStJenisIdentitas(String stJenisIdentitas) {
        this.stJenisIdentitas = stJenisIdentitas;
    }

    /**
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    /**
     * @return the dtTanggalTransfer
     */
    public Date getDtTanggalTransfer() {
        return dtTanggalTransfer;
    }

    /**
     * @param dtTanggalTransfer the dtTanggalTransfer to set
     */
    public void setDtTanggalTransfer(Date dtTanggalTransfer) {
        this.dtTanggalTransfer = dtTanggalTransfer;
    }

    public void calculatePremi() throws Exception{
        String kodePekerjaan = getKodePekerjaan();
        String kodeRate = getKodeRate(kodePekerjaan);

        String kodeRateByPekerjaan = getKodeRateByPekerjaan();

        if(kodeRateByPekerjaan!=null) kodeRate = kodeRateByPekerjaan;
        
        DateTime startDate = new DateTime(getDtTanggalAwal());
        DateTime endDate = new DateTime(getDtTanggalAkhir());
        Months m = Months.monthsBetween(startDate, endDate);
        int jangkaWaktu = m.getMonths();

        BigDecimal rate = getRate(kodeRate, String.valueOf(jangkaWaktu));
        BigDecimal premi = BDUtil.mul(getDbInsuredAmount(), BDUtil.getRateFromPct(rate),2);

        if(!BDUtil.isZeroOrNull(getDbPremiBank())){
             if(!BDUtil.isEqual(premi, getDbPremiBank(), 2)){
                 premi = getDbPremiBank();
                 rate = BDUtil.mul(BDUtil.div(getDbPremiBank(), getDbInsuredAmount(),6), BDUtil.hundred);
             }
        }
        
        setStJangkaWaktuBulan(String.valueOf(jangkaWaktu));
        setDbRate(rate);
        setDbPremiTotal(premi);
        //setStValid("Y");

        cekSyaratPKS();

        if(Integer.parseInt(getStUsia()) < 17)
            setStValid("N");

    }

    public String getKodeRateByPekerjaan() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ref3 " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'WORKING_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,getStCostCenterCode());
            S.setParam(2,getStJenisPekerjaan());

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getKodeRate(String kodePekerjaan) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ref2 " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'BANKCREDIT_TYPE' and ref1 = ? and vs_code = ? and (ref3 = ? or ref3 is null)");

            S.setParam(1,getStCostCenterCode());
            S.setParam(2,getStJenisKredit());
            S.setParam(3,kodePekerjaan);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getKodePekerjaan() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ref2 " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'WORKING_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,getStCostCenterCode());
            S.setParam(2,getStJenisPekerjaan());

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public BigDecimal getRate(String kodeRate, String jangkaWaktu) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     rate1 " +
                    "   from " +
                    "         ins_rates_big " +
                    "   where" +
                    "      rate_class = 'PAKREASI_RATE' and ref2 = ? and ref1 = ? and ref3 = ?");

            S.setParam(1, getStCostCenterCode());
            S.setParam(2, kodeRate);
            S.setParam(3, jangkaWaktu);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getBigDecimal(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    /**
     * @return the dbRate
     */
    public BigDecimal getDbRate() {
        return dbRate;
    }

    /**
     * @param dbRate the dbRate to set
     */
    public void setDbRate(BigDecimal dbRate) {
        this.dbRate = dbRate;
    }

    /**
     * @return the stJangkaWaktuBulan
     */
    public String getStJangkaWaktuBulan() {
        return stJangkaWaktuBulan;
    }

    /**
     * @param stJangkaWaktuBulan the stJangkaWaktuBulan to set
     */
    public void setStJangkaWaktuBulan(String stJangkaWaktuBulan) {
        this.stJangkaWaktuBulan = stJangkaWaktuBulan;
    }

    public void cekSyaratPKS() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     description, ref1 " +
                    "   from " +
                    "         ins_rates_big " +
                    "   where" +
                    "      rate_class = 'PAKREASI_SYARAT' and ref2 = ? and ref3 = ? and ? between rate0 and rate1");

            S.setParam(1, getStCostCenterCode());
            S.setParam(2, getStUsia());
            S.setParam(3, getDbInsuredAmount());

            final ResultSet RS = S.executeQuery();

            if (RS.next()){

                setStDescription(RS.getString(1));

                if(Tools.isNo(RS.getString(2))){
                    setDbRate(null);
                    setDbPremiTotal(null);
                    setStValid("N");
                    setStStatus("CONFIRM");
                }

            }

        }finally{
            S.release();
        }
    }

    /**
     * @return the stNotes
     */
    public String getStNotes() {
        return stNotes;
    }

    /**
     * @param stNotes the stNotes to set
     */
    public void setStNotes(String stNotes) {
        this.stNotes = stNotes;
    }

    /**
     * @return the stValid
     */
    public String getStValid() {
        return stValid;
    }

    /**
     * @param stValid the stValid to set
     */
    public void setStValid(String stValid) {
        this.stValid = stValid;
    }

    /**
     * @return the stSPK
     */
    public String getStSPK() {
        return stSPK;
    }

    /**
     * @param stSPK the stSPK to set
     */
    public void setStSPK(String stSPK) {
        this.stSPK = stSPK;
    }

    /**
     * @return the stSPKFile
     */
    public String getStSPKFile() {
        return stSPKFile;
    }

    /**
     * @param stSPKFile the stSPKFile to set
     */
    public void setStSPKFile(String stSPKFile) {
        this.stSPKFile = stSPKFile;
    }

    /**
     * @return the stPKBFile
     */
    public String getStPKBFile() {
        return stPKBFile;
    }

    /**
     * @param stPKBFile the stPKBFile to set
     */
    public void setStPKBFile(String stPKBFile) {
        this.stPKBFile = stPKBFile;
    }

    /**
     * @return the stEKGFile
     */
    public String getStEKGFile() {
        return stEKGFile;
    }

    /**
     * @param stEKGFile the stEKGFile to set
     */
    public void setStEKGFile(String stEKGFile) {
        this.stEKGFile = stEKGFile;
    }

    /**
     * @return the stAdaulFile
     */
    public String getStAdaulFile() {
        return stAdaulFile;
    }

    /**
     * @param stAdaulFile the stAdaulFile to set
     */
    public void setStAdaulFile(String stAdaulFile) {
        this.stAdaulFile = stAdaulFile;
    }

    /**
     * @return the stThoraxFile
     */
    public String getStThoraxFile() {
        return stThoraxFile;
    }

    /**
     * @param stThoraxFile the stThoraxFile to set
     */
    public void setStThoraxFile(String stThoraxFile) {
        this.stThoraxFile = stThoraxFile;
    }

    /**
     * @return the stTRMFile
     */
    public String getStTRMFile() {
        return stTRMFile;
    }

    /**
     * @param stTRMFile the stTRMFile to set
     */
    public void setStTRMFile(String stTRMFile) {
        this.stTRMFile = stTRMFile;
    }

    /**
     * @return the stHIVFile
     */
    public String getStHIVFile() {
        return stHIVFile;
    }

    /**
     * @param stHIVFile the stHIVFile to set
     */
    public void setStHIVFile(String stHIVFile) {
        this.stHIVFile = stHIVFile;
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
     * @return the dbPremiBank
     */
    public BigDecimal getDbPremiBank() {
        return dbPremiBank;
    }

    /**
     * @param dbPremiBank the dbPremiBank to set
     */
    public void setDbPremiBank(BigDecimal dbPremiBank) {
        this.dbPremiBank = dbPremiBank;
    }

    /**
     * @return the stStatusKerja
     */
    public String getStStatusKerja() {
        return stStatusKerja;
    }

    /**
     * @param stStatusKerja the stStatusKerja to set
     */
    public void setStStatusKerja(String stStatusKerja) {
        this.stStatusKerja = stStatusKerja;
    }

    /**
     * @return the stJenisKreditAskrida
     */
    public String getStJenisKreditAskrida() {
        return stJenisKreditAskrida;
    }

    /**
     * @param stJenisKreditAskrida the stJenisKreditAskrida to set
     */
    public void setStJenisKreditAskrida(String stJenisKreditAskrida) {
        this.stJenisKreditAskrida = stJenisKreditAskrida;
    }

    /**
     * @return the stKodeTarif
     */
    public String getStKodeTarif() {
        return stKodeTarif;
    }

    /**
     * @param stKodeTarif the stKodeTarif to set
     */
    public void setStKodeTarif(String stKodeTarif) {
        this.stKodeTarif = stKodeTarif;
    }

    /**
     * @return the stPaketCoverage
     */
    public String getStPaketCoverage() {
        return stPaketCoverage;
    }

    /**
     * @param stPaketCoverage the stPaketCoverage to set
     */
    public void setStPaketCoverage(String stPaketCoverage) {
        this.stPaketCoverage = stPaketCoverage;
    }

    /**
     * @return the dbKomisi1Pct
     */
    public BigDecimal getDbKomisi1Pct() {
        return dbKomisi1Pct;
    }

    /**
     * @param dbKomisi1Pct the dbKomisi1Pct to set
     */
    public void setDbKomisi1Pct(BigDecimal dbKomisi1Pct) {
        this.dbKomisi1Pct = dbKomisi1Pct;
    }

    /**
     * @return the dbKomisi1Amount
     */
    public BigDecimal getDbKomisi1Amount() {
        return dbKomisi1Amount;
    }

    /**
     * @param dbKomisi1Amount the dbKomisi1Amount to set
     */
    public void setDbKomisi1Amount(BigDecimal dbKomisi1Amount) {
        this.dbKomisi1Amount = dbKomisi1Amount;
    }

    /**
     * @return the dbKomisi2Pct
     */
    public BigDecimal getDbKomisi2Pct() {
        return dbKomisi2Pct;
    }

    /**
     * @param dbKomisi2Pct the dbKomisi2Pct to set
     */
    public void setDbKomisi2Pct(BigDecimal dbKomisi2Pct) {
        this.dbKomisi2Pct = dbKomisi2Pct;
    }

    /**
     * @return the dbKomisi2Amount
     */
    public BigDecimal getDbKomisi2Amount() {
        return dbKomisi2Amount;
    }

    /**
     * @param dbKomisi2Amount the dbKomisi2Amount to set
     */
    public void setDbKomisi2Amount(BigDecimal dbKomisi2Amount) {
        this.dbKomisi2Amount = dbKomisi2Amount;
    }

    /**
     * @return the dbFeeBase1Pct
     */
    public BigDecimal getDbFeeBase1Pct() {
        return dbFeeBase1Pct;
    }

    /**
     * @param dbFeeBase1Pct the dbFeeBase1Pct to set
     */
    public void setDbFeeBase1Pct(BigDecimal dbFeeBase1Pct) {
        this.dbFeeBase1Pct = dbFeeBase1Pct;
    }

    /**
     * @return the dbFeeBase1Amount
     */
    public BigDecimal getDbFeeBase1Amount() {
        return dbFeeBase1Amount;
    }

    /**
     * @param dbFeeBase1Amount the dbFeeBase1Amount to set
     */
    public void setDbFeeBase1Amount(BigDecimal dbFeeBase1Amount) {
        this.dbFeeBase1Amount = dbFeeBase1Amount;
    }

    /**
     * @return the dbFeeBase2Pct
     */
    public BigDecimal getDbFeeBase2Pct() {
        return dbFeeBase2Pct;
    }

    /**
     * @param dbFeeBase2Pct the dbFeeBase2Pct to set
     */
    public void setDbFeeBase2Pct(BigDecimal dbFeeBase2Pct) {
        this.dbFeeBase2Pct = dbFeeBase2Pct;
    }

    /**
     * @return the dbFeeBase2Amount
     */
    public BigDecimal getDbFeeBase2Amount() {
        return dbFeeBase2Amount;
    }

    /**
     * @param dbFeeBase2Amount the dbFeeBase2Amount to set
     */
    public void setDbFeeBase2Amount(BigDecimal dbFeeBase2Amount) {
        this.dbFeeBase2Amount = dbFeeBase2Amount;
    }

    /**
     * @return the dtTanggalBayar
     */
    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    /**
     * @param dtTanggalBayar the dtTanggalBayar to set
     */
    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }

    /**
     * @return the stPaketCoverageAskrida
     */
    public String getStPaketCoverageAskrida() {
        return stPaketCoverageAskrida;
    }

    /**
     * @param stPaketCoverageAskrida the stPaketCoverageAskrida to set
     */
    public void setStPaketCoverageAskrida(String stPaketCoverageAskrida) {
        this.stPaketCoverageAskrida = stPaketCoverageAskrida;
    }

    /**
     * @return the stKomisi1EntityID
     */
    public String getStKomisi1EntityID() {
        return stKomisi1EntityID;
    }

    /**
     * @param stKomisi1EntityID the stKomisi1EntityID to set
     */
    public void setStKomisi1EntityID(String stKomisi1EntityID) {
        this.stKomisi1EntityID = stKomisi1EntityID;
    }

    /**
     * @return the stKomisi2EntityID
     */
    public String getStKomisi2EntityID() {
        return stKomisi2EntityID;
    }

    /**
     * @param stKomisi2EntityID the stKomisi2EntityID to set
     */
    public void setStKomisi2EntityID(String stKomisi2EntityID) {
        this.stKomisi2EntityID = stKomisi2EntityID;
    }

    /**
     * @param stFeeBase1EntityID the stFeeBase1EntityID to set
     */
    public void setStFeeBase1EntityID(String stFeeBase1EntityID) {
        this.stFeeBase1EntityID = stFeeBase1EntityID;
    }

    /**
     * @return the stFeeBase2EntityID
     */
    public String getStFeeBase2EntityID() {
        return stFeeBase2EntityID;
    }

    /**
     * @param stFeeBase2EntityID the stFeeBase2EntityID to set
     */
    public void setStFeeBase2EntityID(String stFeeBase2EntityID) {
        this.stFeeBase2EntityID = stFeeBase2EntityID;
    }

    /**
     * @return the stFeeBase1EntityID
     */
    public String getStFeeBase1EntityID() {
        return stFeeBase1EntityID;
    }

    /**
     * @return the dtTanggalPolis
     */
    public Date getDtTanggalPolis() {
        return dtTanggalPolis;
    }

    /**
     * @param dtTanggalPolis the dtTanggalPolis to set
     */
    public void setDtTanggalPolis(Date dtTanggalPolis) {
        this.dtTanggalPolis = dtTanggalPolis;
    }

    /**
     * @return the stCostCenterCodeCore
     */
    public String getStCostCenterCodeCore() {
        return stCostCenterCodeCore;
    }

    /**
     * @param stCostCenterCodeCore the stCostCenterCodeCore to set
     */
    public void setStCostCenterCodeCore(String stCostCenterCodeCore) {
        this.stCostCenterCodeCore = stCostCenterCodeCore;
    }

    /**
     * @return the stRegionIDCore
     */
    public String getStRegionIDCore() {
        return stRegionIDCore;
    }

    /**
     * @param stRegionIDCore the stRegionIDCore to set
     */
    public void setStRegionIDCore(String stRegionIDCore) {
        this.stRegionIDCore = stRegionIDCore;
    }

    /**
     * @return the dbDiscountPct
     */
    public BigDecimal getDbDiscountPct() {
        return dbDiscountPct;
    }

    /**
     * @param dbDiscountPct the dbDiscountPct to set
     */
    public void setDbDiscountPct(BigDecimal dbDiscountPct) {
        this.dbDiscountPct = dbDiscountPct;
    }

    /**
     * @return the dbDiscountAmount
     */
    public BigDecimal getDbDiscountAmount() {
        return dbDiscountAmount;
    }

    /**
     * @param dbDiscountAmount the dbDiscountAmount to set
     */
    public void setDbDiscountAmount(BigDecimal dbDiscountAmount) {
        this.dbDiscountAmount = dbDiscountAmount;
    }

    /**
     * @return the stKodeTransaksi
     */
    public String getStKodeTransaksi() {
        return stKodeTransaksi;
    }

    /**
     * @param stKodeTransaksi the stKodeTransaksi to set
     */
    public void setStKodeTransaksi(String stKodeTransaksi) {
        this.stKodeTransaksi = stKodeTransaksi;
    }

    /**
     * @return the stNomorArsipPembayaran
     */
    public String getStNomorArsipPembayaran() {
        return stNomorArsipPembayaran;
    }

    /**
     * @param stNomorArsipPembayaran the stNomorArsipPembayaran to set
     */
    public void setStNomorArsipPembayaran(String stNomorArsipPembayaran) {
        this.stNomorArsipPembayaran = stNomorArsipPembayaran;
    }

    /**
     * @return the stJenisData
     */
    public String getStJenisData() {
        return stJenisData;
    }

    /**
     * @param stJenisData the stJenisData to set
     */
    public void setStJenisData(String stJenisData) {
        this.stJenisData = stJenisData;
    }

    /**
     * @return the dbBiayaPolis
     */
    public BigDecimal getDbBiayaPolis() {
        return dbBiayaPolis;
    }

    /**
     * @param dbBiayaPolis the dbBiayaPolis to set
     */
    public void setDbBiayaPolis(BigDecimal dbBiayaPolis) {
        this.dbBiayaPolis = dbBiayaPolis;
    }

    /**
     * @return the dbBiayaMaterai
     */
    public BigDecimal getDbBiayaMaterai() {
        return dbBiayaMaterai;
    }

    /**
     * @param dbBiayaMaterai the dbBiayaMaterai to set
     */
    public void setDbBiayaMaterai(BigDecimal dbBiayaMaterai) {
        this.dbBiayaMaterai = dbBiayaMaterai;
    }

    /**
     * @return the dbTotalTagihanPremi
     */
    public BigDecimal getDbTotalTagihanPremi() {
        return dbTotalTagihanPremi;
    }

    /**
     * @param dbTotalTagihanPremi the dbTotalTagihanPremi to set
     */
    public void setDbTotalTagihanPremi(BigDecimal dbTotalTagihanPremi) {
        this.dbTotalTagihanPremi = dbTotalTagihanPremi;
    }

    /**
     * @return the dtTanggalSTNC
     */
    public Date getDtTanggalSTNC() {
        return dtTanggalSTNC;
    }

    /**
     * @param dtTanggalSTNC the dtTanggalSTNC to set
     */
    public void setDtTanggalSTNC(Date dtTanggalSTNC) {
        this.dtTanggalSTNC = dtTanggalSTNC;
    }

    /**
     * @return the stPersetujuanPusat
     */
    public String getStPersetujuanPusat() {
        return stPersetujuanPusat;
    }

    /**
     * @param stPersetujuanPusat the stPersetujuanPusat to set
     */
    public void setStPersetujuanPusat(String stPersetujuanPusat) {
        this.stPersetujuanPusat = stPersetujuanPusat;
    }

    /**
     * @return the dbFeeBasePPN1Include
     */
    public BigDecimal getDbFeeBasePPN1Include() {
        return dbFeeBasePPN1Include;
    }

    /**
     * @param dbFeeBasePPN1Include the dbFeeBasePPN1Include to set
     */
    public void setDbFeeBasePPN1Include(BigDecimal dbFeeBasePPN1Include) {
        this.dbFeeBasePPN1Include = dbFeeBasePPN1Include;
    }

    /**
     * @return the dbFeeBasePPN1Exclude
     */
    public BigDecimal getDbFeeBasePPN1Exclude() {
        return dbFeeBasePPN1Exclude;
    }

    /**
     * @param dbFeeBasePPN1Exclude the dbFeeBasePPN1Exclude to set
     */
    public void setDbFeeBasePPN1Exclude(BigDecimal dbFeeBasePPN1Exclude) {
        this.dbFeeBasePPN1Exclude = dbFeeBasePPN1Exclude;
    }

    /**
     * @return the stNoPolisInduk
     */
    public String getStNoPolisInduk() {
        return stNoPolisInduk;
    }

    /**
     * @param stNoPolisInduk the stNoPolisInduk to set
     */
    public void setStNoPolisInduk(String stNoPolisInduk) {
        this.stNoPolisInduk = stNoPolisInduk;
    }

    /**
     * @return the dbPremiRefund
     */
    public BigDecimal getDbPremiRefund() {
        return dbPremiRefund;
    }

    /**
     * @param dbPremiRefund the dbPremiRefund to set
     */
    public void setDbPremiRefund(BigDecimal dbPremiRefund) {
        this.dbPremiRefund = dbPremiRefund;
    }

    /**
     * @return the stNomorBuktiBayar
     */
    public String getStNomorBuktiBayar() {
        return stNomorBuktiBayar;
    }

    /**
     * @param stNomorBuktiBayar the stNomorBuktiBayar to set
     */
    public void setStNomorBuktiBayar(String stNomorBuktiBayar) {
        this.stNomorBuktiBayar = stNomorBuktiBayar;
    }

    /**
     * @return the stNomorReferensiPembayaran
     */
    public String getStNomorReferensiPembayaran() {
        return stNomorReferensiPembayaran;
    }

    /**
     * @param stNomorReferensiPembayaran the stNomorReferensiPembayaran to set
     */
    public void setStNomorReferensiPembayaran(String stNomorReferensiPembayaran) {
        this.stNomorReferensiPembayaran = stNomorReferensiPembayaran;
    }

    /**
     * @return the stJenisKreditDebitur
     */
    public String getStJenisKreditDebitur() {
        return stJenisKreditDebitur;
    }

    /**
     * @param stJenisKreditDebitur the stJenisKreditDebitur to set
     */
    public void setStJenisKreditDebitur(String stJenisKreditDebitur) {
        this.stJenisKreditDebitur = stJenisKreditDebitur;
    }

    /**
     * @return the stSumberPembayaran
     */
    public String getStSumberPembayaran() {
        return stSumberPembayaran;
    }

    /**
     * @param stSumberPembayaran the stSumberPembayaran to set
     */
    public void setStSumberPembayaran(String stSumberPembayaran) {
        this.stSumberPembayaran = stSumberPembayaran;
    }

    public boolean isTopUp() {

        boolean topUp = false;

        if(getStKodeTransaksi()!=null)
            if(getStKodeTransaksi().equalsIgnoreCase("3"))
                topUp = true;

        return topUp;
    }

}

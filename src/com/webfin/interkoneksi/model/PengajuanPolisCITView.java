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

public class PengajuanPolisCITView extends DTO implements RecordAudit {
    /*
    CREATE TABLE pengajuan_polis_cit
(
  data_id bigint NOT NULL,
  unique_key character varying(32),
  cc_code character varying(4),
  region_id bigint,
  jenis_asuransi character varying(2),
  kode_bank character varying(12),
  entity_id bigint,
  nama_debitur character varying(32),
  tanggal_berangkat timestamp without time zone,
  kode_bank_asal character varying(12),
  bank_asal character varying(255),
  kode_bank_tujuan character varying(12),
  bank_tujuan character varying(255),
  rate numeric,
  premi numeric,
  waktu character varying(32),
  no_deklarasi character varying(32),
  diangkut_dengan character varying(255),
  alat_angkut1 character varying(32),
  alat_angkut2 character varying(32),
  alat_angkut3 character varying(32),
  account_officer character varying(100),
  pengawal character varying(200),
  sum_insured numeric,
  jumlah_bayar_premi numeric,
  tgl_bayar_premi timestamp without time zone,
  no_referensi_bayar character varying(100),
  no_rekening character varying(32),
  create_date timestamp without time zone,
  request_json text,
  trx_no character varying(32),
  pol_no character varying(32),
  tgl_polis timestamp without time zone,
  komisi1_pct numeric,
  komisi1_amount numeric,
  komisi2_pct numeric,
  komisi2_amount numeric,
  feebase1_pct numeric,
  feebase1_amount numeric,
  feebase2_pct numeric,
  feebase2_amount numeric,
  komisi1_ent_id bigint,
  komisi2_ent_id bigint,
  feebase1_ent_id bigint,
  feebase2_ent_id bigint,
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
  tgl_transfer timestamp without time zone,
  CONSTRAINT pengajuan_polis_cit_pkey PRIMARY KEY (data_id)
)
     */

    public static String tableName = "pengajuan_polis_cit";
    
    public static String fieldMap[][] = {
        {"stDataID", "data_id*pk"},
        {"stUniqueKey", "unique_key"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stJenisAsuransi", "jenis_asuransi"},
        {"stKodeBank", "kode_bank"},
        {"stEntityID", "entity_id"},
        {"stNamaDebitur", "nama_debitur"},
        {"dtTanggalBerangkat", "tanggal_berangkat"},
        {"stKodeBankAsal", "kode_bank_asal"},
        {"stBankAsal", "bank_asal"},
        {"stKodeBankTujuan", "kode_bank_tujuan"},
        {"stBankTujuan", "bank_tujuan"},
        {"dbRate", "rate"},
        {"dbPremi", "premi"},
        {"stWaktu", "waktu"},
        {"stNoDeklarasi", "no_deklarasi"},
        {"stDiangkutDengan", "diangkut_dengan"},
        {"stAlatAngkut1", "alat_angkut1"},
        {"stAlatAngkut2", "alat_angkut2"},
        {"stAlatAngkut3", "alat_angkut3"},
        {"stAccountOfficer", "account_officer"},
        {"stPengawal", "pengawal"},
        {"dbSumInsured", "sum_insured"},
        {"dbJumlahBayarPremi", "jumlah_bayar_premi"},
        {"dtTanggalBayarPremi", "tgl_bayar_premi"},
        {"stNoReferensiBayar", "no_referensi_bayar"},
        {"stPolicyNo", "pol_no"},
        {"dtTanggalPolis", "tgl_polis"},
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
    };


    private String stDataID;
    private String stUniqueKey;
    private String stCostCenterCode;
    private String stRegionID;
    private String stJenisAsuransi;
    private String stKodeBank;
    private String stEntityID;
    private String stNamaDebitur;
    private Date dtTanggalBerangkat;
    private String stKodeBankAsal;
    private String stBankAsal;
    private String stKodeBankTujuan;
    private String stBankTujuan;
    private BigDecimal dbRate;
    private BigDecimal dbPremi;
    private String stWaktu;
    private String stNoDeklarasi;
    private String stDiangkutDengan;
    private String stAlatAngkut1;
    private String stAlatAngkut2;
    private String stAlatAngkut3;
    private String stAccountOfficer;
    private String stPengawal;
    private BigDecimal dbSumInsured;
    private BigDecimal dbJumlahBayarPremi;
    private Date dtTanggalBayarPremi;
    private String stNoReferensiBayar;
    private String stPolicyNo;
    private Date dtTanggalPolis;
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

    

    public BigDecimal getDbDeductibleMax1() {
        return dbDeductibleMax1;
    }

    public void setDbDeductibleMax1(BigDecimal dbDeductibleMax1) {
        this.dbDeductibleMax1 = dbDeductibleMax1;
    }

    public BigDecimal getDbDeductibleMin1() {
        return dbDeductibleMin1;
    }

    public void setDbDeductibleMin1(BigDecimal dbDeductibleMin1) {
        this.dbDeductibleMin1 = dbDeductibleMin1;
    }

    public BigDecimal getDbDeductiblePctOfClaim1() {
        return dbDeductiblePctOfClaim1;
    }

    public void setDbDeductiblePctOfClaim1(BigDecimal dbDeductiblePctOfClaim1) {
        this.dbDeductiblePctOfClaim1 = dbDeductiblePctOfClaim1;
    }

    public String getStDeductibleID1() {
        return stDeductibleID1;
    }

    public void setStDeductibleID1(String stDeductibleID1) {
        this.stDeductibleID1 = stDeductibleID1;
    }

    public String getStJenisAsuransi() {
        return stJenisAsuransi;
    }

    public void setStJenisAsuransi(String stJenisAsuransi) {
        this.stJenisAsuransi = stJenisAsuransi;
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

    public BigDecimal getDbJumlahBayarPremi() {
        return dbJumlahBayarPremi;
    }

    public void setDbJumlahBayarPremi(BigDecimal dbJumlahBayarPremi) {
        this.dbJumlahBayarPremi = dbJumlahBayarPremi;
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

    public BigDecimal getDbSumInsured() {
        return dbSumInsured;
    }

    public void setDbSumInsured(BigDecimal dbSumInsured) {
        this.dbSumInsured = dbSumInsured;
    }

    public Date getDtTanggalBayarPremi() {
        return dtTanggalBayarPremi;
    }

    public void setDtTanggalBayarPremi(Date dtTanggalBayarPremi) {
        this.dtTanggalBayarPremi = dtTanggalBayarPremi;
    }

    public Date getDtTanggalBerangkat() {
        return dtTanggalBerangkat;
    }

    public void setDtTanggalBerangkat(Date dtTanggalBerangkat) {
        this.dtTanggalBerangkat = dtTanggalBerangkat;
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

    public String getStAccountOfficer() {
        return stAccountOfficer;
    }

    public void setStAccountOfficer(String stAccountOfficer) {
        this.stAccountOfficer = stAccountOfficer;
    }

    public String getStAlatAngkut1() {
        return stAlatAngkut1;
    }

    public void setStAlatAngkut1(String stAlatAngkut1) {
        this.stAlatAngkut1 = stAlatAngkut1;
    }

    public String getStAlatAngkut2() {
        return stAlatAngkut2;
    }

    public void setStAlatAngkut2(String stAlatAngkut2) {
        this.stAlatAngkut2 = stAlatAngkut2;
    }

    public String getStAlatAngkut3() {
        return stAlatAngkut3;
    }

    public void setStAlatAngkut3(String stAlatAngkut3) {
        this.stAlatAngkut3 = stAlatAngkut3;
    }

    public String getStBankAsal() {
        return stBankAsal;
    }

    public void setStBankAsal(String stBankAsal) {
        this.stBankAsal = stBankAsal;
    }

    public String getStBankTujuan() {
        return stBankTujuan;
    }

    public void setStBankTujuan(String stBankTujuan) {
        this.stBankTujuan = stBankTujuan;
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

    public String getStDiangkutDengan() {
        return stDiangkutDengan;
    }

    public void setStDiangkutDengan(String stDiangkutDengan) {
        this.stDiangkutDengan = stDiangkutDengan;
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

    public String getStKodeBank() {
        return stKodeBank;
    }

    public void setStKodeBank(String stKodeBank) {
        this.stKodeBank = stKodeBank;
    }

    public String getStKodeBankAsal() {
        return stKodeBankAsal;
    }

    public void setStKodeBankAsal(String stKodeBankAsal) {
        this.stKodeBankAsal = stKodeBankAsal;
    }

    public String getStKodeBankTujuan() {
        return stKodeBankTujuan;
    }

    public void setStKodeBankTujuan(String stKodeBankTujuan) {
        this.stKodeBankTujuan = stKodeBankTujuan;
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

    public String getStNoDeklarasi() {
        return stNoDeklarasi;
    }

    public void setStNoDeklarasi(String stNoDeklarasi) {
        this.stNoDeklarasi = stNoDeklarasi;
    }

    public String getStNoReferensiBayar() {
        return stNoReferensiBayar;
    }

    public void setStNoReferensiBayar(String stNoReferensiBayar) {
        this.stNoReferensiBayar = stNoReferensiBayar;
    }

    public String getStPengawal() {
        return stPengawal;
    }

    public void setStPengawal(String stPengawal) {
        this.stPengawal = stPengawal;
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

    public String getStUniqueKey() {
        return stUniqueKey;
    }

    public void setStUniqueKey(String stUniqueKey) {
        this.stUniqueKey = stUniqueKey;
    }

    public String getStWaktu() {
        return stWaktu;
    }

    public void setStWaktu(String stWaktu) {
        this.stWaktu = stWaktu;
    }

}

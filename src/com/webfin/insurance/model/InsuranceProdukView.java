/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceProdukView
 * Author:  Ahmad Rhodoni
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceProdukView extends DTO {

   /*
  produk_id bigint,
  nopol character varying(32),
  nobuk character varying(32),
  nopolm character varying(32),
  cnoplm character varying(32),
  tglpol timestamp without time zone,
  nama character varying(32),
  harper numeric,
  preto numeric,
  biapol numeric,
  biamat numeric,
  komisi numeric,
  batal character varying(32),
  tglm timestamp without time zone,
  tgla timestamp without time zone,
  relasi character varying,
  tglent timestamp without time zone,
  flnd character varying(32),
  cetdn character varying(32),
  kali character varying(32),
  dollar numeric,
  fltutup character varying(32),
  kodeko character varying(32),
  persko numeric,
  nomorko character varying(32),
  clerk character varying(32),
  tglketik timestamp without time zone,
  flpro character varying(32),
  diskon numeric,
  h_fee numeric,
  b_fee numeric,
  agen character varying(32),
  tgltran timestamp without time zone,
  tglrest timestamp without time zone
   */
   private String stPolicyID;
   private String stInsuranceProdukID;
   private String stInsuranceNoPolis;
   private String stInsuranceNoBukti;
   private String stInsuranceNoPolisLama;
   private String stInsuranceNoPolisLamaCounter;
   private String stInsuranceNama;
   private String stBatal;
   private String stRelasi;
   private String stFlagTutup;
   private String stKodeKoasuransi;
   private String stClerk;
   private String stAgen;
   private String stFlagProduksi;
   private String stFlagEndorsement;
   private String stFlagDN;
   private String stKali;
   private String stEntityID;
    
   private BigDecimal dbHargaPertanggungan;
   private BigDecimal dbPremiBruto;
   private BigDecimal dbBiayaPolis;
   private BigDecimal dbBiayaMaterai;
   private BigDecimal dbKomisi;
   private BigDecimal dbDollar;
   private BigDecimal dbPersenKoasuransi;
   private BigDecimal dbNomorKoasuransi;
   private BigDecimal dbDiskon;
   private BigDecimal dbHandlingFee;
   private BigDecimal dbBrokerageFee;
   private BigDecimal dbFeeBase;

   private Date dtTglPolDate;
   private Date dtTanggalMulaiDate;
   private Date dtTanggalAkhirDate;
   private Date dtTanggalEntry;
   private Date dtTglKetikDate;
   private Date dtTanggalTransaksi;
   private Date dtTanggalRest;
   
   private String stFlag; 
   private String stInsuranceNoPolisWeb;

   public static String tableName = "aba_produk";

   public static String fieldMap[][] = {
   	  {"stPolicyID", "pol_id*pk*nd"},
      {"stInsuranceNoPolis","nopol"},
      {"stInsuranceNoBukti","nobuk"},
      {"stInsuranceNoPolisLama","nopolm"},
      {"stInsuranceNoPolisLamaCounter","cnoplm"},
      {"dtTglPolDate","tglpol"},
      {"stInsuranceNama","nama"},
      {"dbHargaPertanggungan","harper"},
      {"dbPremiBruto","preto"},
      {"dbBiayaPolis","biapol"},
      {"dbBiayaMaterai","biamat"},
      {"dbKomisi","komisi"},
      {"stBatal","batal"},
      {"dtTanggalMulaiDate","tglm"},
      {"dtTanggalAkhirDate","tgla"},
      {"stRelasi","relasi"},
      {"dtTanggalEntry","tglent"},
      {"stFlagEndorsement","flnd"},
      {"stFlagDN","cetdn"},
      {"stKali","kali"},
      {"dbDollar","dollar"},
      {"stFlagTutup","fltutup"},
      {"stKodeKoasuransi","kodeko"},
      {"dbPersenKoasuransi","persko"},
      {"dbNomorKoasuransi","nomorko"},
      {"stClerk","clerk"},
      {"dtTglKetikDate","tglketik"},
      {"stFlagProduksi","flpro"},
      {"dbDiskon","diskon"},
      {"dbHandlingFee","h_fee"},
      {"dbBrokerageFee","b_fee"},
      {"stAgen","agen"},
      {"dtTanggalTransaksi","tgltran"},
      {"dtTanggalRest","tglrest"},
      {"stEntityID","entity_id"},
      {"stFlag","flag"}, 
      {"stInsuranceNoPolisWeb","pol_no"},
      {"dbFeeBase","feebase"},
      
   };
   
   public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

   public String getStInsuranceProdukID(){
       return stInsuranceProdukID;
   }

   public void setStInsuranceProdukID(String stInsuranceProdukID){
       this.stInsuranceProdukID = stInsuranceProdukID;
   }

   public String getStInsuranceNoPolis(){
       return stInsuranceNoPolis;
   }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis){
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }

    public String getStInsuranceNoBukti(){
        return stInsuranceNoBukti;
    }

    public void setStInsuranceNoBukti(String stInsuranceNoBukti){
        this.stInsuranceNoBukti = stInsuranceNoBukti;
    }

    public String getStInsuranceNoPolisLama(){
        return stInsuranceNoPolisLama;
    }

    public void setStInsuranceNoPolisLama(String stInsuranceNoPolisLama){
        this.stInsuranceNoPolisLama = stInsuranceNoPolisLama;
    }

    public String getStInsuranceNoPolisLamaCounter(){
        return stInsuranceNoPolisLamaCounter;
    }

    public void setStInsuranceNoPolisLamaCounter(String stInsuranceNoPolisLamaCounter){
        this.stInsuranceNoPolisLamaCounter = stInsuranceNoPolisLamaCounter;
    }

    public String getStInsuranceNama(){
        return stInsuranceNama;
    }

    public void setStInsuranceNama(String stInsuranceNama){
        this.stInsuranceNama = stInsuranceNama;
    }

    public String getStAgen(){
        return stAgen;
    }

    public void setStAgen(String stAgen){
        this.stAgen = stAgen;
    }

    public String getStBatal(){
        return stBatal;
    }

    public void setStBatal(String stBatal){
        this.stBatal = stBatal;
    }

    public String getStClerk(){
        return stClerk;
    }

    public void setStClerk(String stClerk){
        this.stClerk = stClerk;
    }

    public String getStFlagTutup(){
        return stFlagTutup;
    }

    public void setStFlagTutup(String stFlagTutup){
        this.stFlagTutup = stFlagTutup;
    }

    public String getStKodeKoasuransi(){
        return stKodeKoasuransi;
    }

    public void setStKodeKoasuransi(String stKodeKoasuransi){
        this.stKodeKoasuransi = stKodeKoasuransi;
    }

    public String getStRelasi(){
        return stRelasi;
    }

    public void setStRelasi(String stRelasi){
        this.stRelasi = stRelasi;
    }

    public BigDecimal getDbBiayaMaterai() {
        return dbBiayaMaterai;
    }

    public void setDbBiayaMaterai(BigDecimal dbBiayaMaterai) {
        this.dbBiayaMaterai = dbBiayaMaterai;
    }

    public BigDecimal getDbBiayaPolis() {
        return dbBiayaPolis;
    }

    public void setDbBiayaPolis(BigDecimal dbBiayaPolis) {
        this.dbBiayaPolis = dbBiayaPolis;
    }

    public BigDecimal getDbBrokerageFee() {
        return dbBrokerageFee;
    }

    public void setDbBrokerageFee(BigDecimal dbBrokerageFee) {
        this.dbBrokerageFee = dbBrokerageFee;
    }

    public BigDecimal getDbDiskon() {
        return dbDiskon;
    }

    public void setDbDiskon(BigDecimal dbDiskon) {
        this.dbDiskon = dbDiskon;
    }

    public BigDecimal getDbDollar() {
        return dbDollar;
    }

    public void setDbDollar(BigDecimal dbDollar) {
        this.dbDollar = dbDollar;
    }

    public BigDecimal getDbHandlingFee() {
        return dbHandlingFee;
    }

    public void setDbHandlingFee(BigDecimal dbHandlingFee) {
        this.dbHandlingFee = dbHandlingFee;
    }

    public BigDecimal getDbHargaPertanggungan() {
        return dbHargaPertanggungan;
    }

    public void setDbHargaPertanggungan(BigDecimal dbHargaPertanggungan) {
        this.dbHargaPertanggungan = dbHargaPertanggungan;
    }

    public String getStKali() {
        return stKali;
    }

    public void setStKali(String stKali) {
        this.stKali = stKali;
    }

    public BigDecimal getDbKomisi() {
        return dbKomisi;
    }

    public void setDbKomisi(BigDecimal dbKomisi) {
        this.dbKomisi = dbKomisi;
    }

    public BigDecimal getDbNomorKoasuransi() {
        return dbNomorKoasuransi;
    }

    public void setDbNomorKoasuransi(BigDecimal dbNomorKoasuransi) {
        this.dbNomorKoasuransi = dbNomorKoasuransi;
    }

    public BigDecimal getDbPersenKoasuransi() {
        return dbPersenKoasuransi;
    }

    public void setDbPersenKoasuransi(BigDecimal dbPersenKoasuransi) {
        this.dbPersenKoasuransi = dbPersenKoasuransi;
    }

    public BigDecimal getDbPremiBruto() {
        return dbPremiBruto;
    }

    public void setDbPremiBruto(BigDecimal dbPremiBruto) {
        this.dbPremiBruto = dbPremiBruto;
    }

    public Date getDtTanggalAkhirDate() {
        return dtTanggalAkhirDate;
    }

    public void setDtTanggalAkhirDate(Date dtTanggalAkhirDate) {
        this.dtTanggalAkhirDate = dtTanggalAkhirDate;
    }

    public Date getDtTanggalEntry() {
        return dtTanggalEntry;
    }

    public void setDtTanggalEntry(Date dtTanggalEntry) {
        this.dtTanggalEntry = dtTanggalEntry;
    }

    public Date getDtTanggalMulaiDate() {
        return dtTanggalMulaiDate;
    }

    public void setDtTanggalMulaiDate(Date dtTanggalMulaiDate) {
        this.dtTanggalMulaiDate = dtTanggalMulaiDate;
    }

    public Date getDtTanggalRest() {
        return dtTanggalRest;
    }

    public void setDtTanggalRest(Date dtTanggalRest) {
        this.dtTanggalRest = dtTanggalRest;
    }

    public Date getDtTanggalTransaksi() {
        return dtTanggalTransaksi;
    }

    public void setDtTanggalTransaksi(Date dtTanggalTransaksi) {
        this.dtTanggalTransaksi = dtTanggalTransaksi;
    }

    public Date getDtTglKetikDate() {
        return dtTglKetikDate;
    }

    public void setDtTglKetikDate(Date dtTglKetikDate) {
        this.dtTglKetikDate = dtTglKetikDate;
    }

    public Date getDtTglPolDate() {
        return dtTglPolDate;
    }

    public void setDtTglPolDate(Date dtTglPolDate) {
        this.dtTglPolDate = dtTglPolDate;
    }

    public String getStFlagDN() {
        return stFlagDN;
    }

    public void setStFlagDN(String stFlagDN) {
        this.stFlagDN = stFlagDN;
    }

    public String getStFlagEndorsement() {
        return stFlagEndorsement;
    }

    public void setStFlagEndorsement(String stFlagEndorsement) {
        this.stFlagEndorsement = stFlagEndorsement;
    }

    public String getStFlagProduksi() {
        return stFlagProduksi;
    }

    public void setStFlagProduksi(String stFlagProduksi) {
        this.stFlagProduksi = stFlagProduksi;
    }
    
    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }
    
    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }
    
    public String getStInsuranceNoPolisWeb(){
        return stInsuranceNoPolisWeb;
    }

    public void setStInsuranceNoPolisWeb(String stInsuranceNoPolisWeb){
        this.stInsuranceNoPolisWeb = stInsuranceNoPolisWeb;
    }

    public BigDecimal getDbFeeBase() {
        return dbFeeBase;
    }

    public void setDbFeeBase(BigDecimal dbFeeBase) {
        this.dbFeeBase = dbFeeBase;
    }
    
}
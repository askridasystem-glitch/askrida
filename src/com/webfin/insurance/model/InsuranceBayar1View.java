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

public class InsuranceBayar1View extends DTO {

   /*
  CREATE TABLE aba_bayar1
(
  bayar1_id bigint,
  nopol character varying(32),
  konter bigint,
  kodeko bigint,
  nobuk character varying(32),
  norek character varying(32),
  preto numeric,
  jtempo timestamp without time zone,
  komisi numeric,
  diskon numeric,
  h_fee numeric,
  b_fee numeric,
  tglbay timestamp without time zone,
  bayar numeric,
  keter character varying(32),
  tglent timestamp without time zone,
  kodent character varying(32),
  kodecet character varying(32),
  nobukdn character varying(32),
  tglketik timestamp without time zone,
  tgbre timestamp without time zone,
  komre numeric,
  tgltran timestamp without time zone,
  tglrest timestamp without time zone,
  tgltran1 timestamp without time zone,
  tglrest1 timestamp without time zone
)
WITH OIDS;
   */

   public static String tableName = "aba_bayar1";

   public static String fieldMap[][] = {
   	 	   {"stPolicyID", "pol_id*pk*nd"},
           {"stInsuranceNoPolis", "nopol"},
           {"stInsuranceKonter", "konter"},
           {"stInsuranceKodeKoasuransi", "kodeko"},
           {"stInsuranceNoBukti", "nobuk"},
           {"stInsuranceNoRekening", "norek"},
           {"dbPremiTotal", "preto"},
           {"dtTanggalJatuhTempo", "jtempo"},
           {"dbKomisi", "komisi"},
           {"dbDiskon", "diskon"},
           {"dbHandlingFee", "h_fee"},
           {"dbBrokerFee", "b_fee"},
           {"dtTanggalBayar", "tglbay"},
           {"dbBayar", "bayar"},
           {"stKeterangan", "keter"},
           {"dtTanggalEntry", "tglent"},
           {"stKodeEntry", "kodent"},
           {"stKodeCetak", "kodecet"},
           {"stNomorBuktiDN", "nobukdn"},
           {"dtTglKetik", "tglketik"},
           {"dtTglBre", "tgbre"},
           {"dbKomRe", "komre"},
           {"dtTanggalTran", "tgltran"},
           {"dtTanggalRest", "tglrest"},
           {"dtTanggalTran1", "tgltran1"},
           {"dtTanggalRest1", "tglrest1"},
            {"stFlag","flag"}, 
           {"dbFeeBase", "feebase"},
   };

	private String stPolicyID;
    private String stInsuranceProdukID;
    private String stInsuranceNoPolis;
    private String stInsuranceKonter;
    private String stInsuranceKodeKoasuransi;
    private String stInsuranceNoBukti;
    private String stInsuranceNoRekening;
    private BigDecimal dbPremiTotal;
    private Date dtTanggalJatuhTempo;
    private BigDecimal dbKomisi;
    private BigDecimal dbDiskon;
    private BigDecimal dbHandlingFee;
    private BigDecimal dbBrokerFee;
    private Date dtTanggalBayar;
    private BigDecimal dbBayar;
    private String stKeterangan;
    private Date dtTanggalEntry;
    private String stKodeEntry;
    private String stKodeCetak;
    private String stNomorBuktiDN;
    private Date dtTglKetik;
    private Date dtTglBre;
    private BigDecimal dbKomRe;
    private Date dtTanggalTran;
    private Date dtTanggalRest;
    private Date dtTanggalTran1;
    private Date dtTanggalRest1;
    private String stFlag; 
    private BigDecimal dbFeeBase;
    
    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public BigDecimal getDbBayar() {
        return dbBayar;
    }

    public void setDbBayar(BigDecimal dbBayar) {
        this.dbBayar = dbBayar;
    }

    public BigDecimal getDbBrokerFee() {
        return dbBrokerFee;
    }

    public void setDbBrokerFee(BigDecimal dbBrokerFee) {
        this.dbBrokerFee = dbBrokerFee;
    }

    public BigDecimal getDbDiskon() {
        return dbDiskon;
    }

    public void setDbDiskon(BigDecimal dbDiskon) {
        this.dbDiskon = dbDiskon;
    }

    public BigDecimal getDbHandlingFee() {
        return dbHandlingFee;
    }

    public void setDbHandlingFee(BigDecimal dbHandlingFee) {
        this.dbHandlingFee = dbHandlingFee;
    }

    public BigDecimal getDbKomisi() {
        return dbKomisi;
    }

    public void setDbKomisi(BigDecimal dbKomisi) {
        this.dbKomisi = dbKomisi;
    }

    public BigDecimal getDbKomRe() {
        return dbKomRe;
    }

    public void setDbKomRe(BigDecimal dbKomRe) {
        this.dbKomRe = dbKomRe;
    }

    public BigDecimal getDbPremiTotal() {
        return dbPremiTotal;
    }

    public void setDbPremiTotal(BigDecimal dbPremiTotal) {
        this.dbPremiTotal = dbPremiTotal;
    }

    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }

    public Date getDtTanggalEntry() {
        return dtTanggalEntry;
    }

    public void setDtTanggalEntry(Date dtTanggalEntry) {
        this.dtTanggalEntry = dtTanggalEntry;
    }

    public Date getDtTanggalJatuhTempo() {
        return dtTanggalJatuhTempo;
    }

    public void setDtTanggalJatuhTempo(Date dtTanggalJatuhTempo) {
        this.dtTanggalJatuhTempo = dtTanggalJatuhTempo;
    }

    public Date getDtTanggalRest1() {
        return dtTanggalRest1;
    }

    public void setDtTanggalRest1(Date dtTanggalRest1) {
        this.dtTanggalRest1 = dtTanggalRest1;
    }

    public Date getDtTanggalRest() {
        return dtTanggalRest;
    }

    public void setDtTanggalRest(Date dtTanggalRest) {
        this.dtTanggalRest = dtTanggalRest;
    }

    public Date getDtTanggalTran1() {
        return dtTanggalTran1;
    }

    public void setDtTanggalTran1(Date dtTanggalTran1) {
        this.dtTanggalTran1 = dtTanggalTran1;
    }

    public Date getDtTanggalTran() {
        return dtTanggalTran;
    }

    public void setDtTanggalTran(Date dtTanggalTran) {
        this.dtTanggalTran = dtTanggalTran;
    }

    public Date getDtTglBre() {
        return dtTglBre;
    }

    public void setDtTglBre(Date dtTglBre) {
        this.dtTglBre = dtTglBre;
    }

    public Date getDtTglKetik() {
        return dtTglKetik;
    }

    public void setDtTglKetik(Date dtTglKetik) {
        this.dtTglKetik = dtTglKetik;
    }

    public String getStInsuranceKodeKoasuransi() {
        return stInsuranceKodeKoasuransi;
    }

    public void setStInsuranceKodeKoasuransi(String stInsuranceKodeKoasuransi) {
        this.stInsuranceKodeKoasuransi = stInsuranceKodeKoasuransi;
    }

    public String getStInsuranceKonter() {
        return stInsuranceKonter;
    }

    public void setStInsuranceKonter(String stInsuranceKonter) {
        this.stInsuranceKonter = stInsuranceKonter;
    }

    public String getStInsuranceNoBukti() {
        return stInsuranceNoBukti;
    }

    public void setStInsuranceNoBukti(String stInsuranceNoBukti) {
        this.stInsuranceNoBukti = stInsuranceNoBukti;
    }

    public String getStInsuranceNoPolis() {
        return stInsuranceNoPolis;
    }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis) {
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }

    public String getStInsuranceNoRekening() {
        return stInsuranceNoRekening;
    }

    public void setStInsuranceNoRekening(String stInsuranceNoRekening) {
        this.stInsuranceNoRekening = stInsuranceNoRekening;
    }

    public String getStInsuranceProdukID() {
        return stInsuranceProdukID;
    }

    public void setStInsuranceProdukID(String stInsuranceProdukID) {
        this.stInsuranceProdukID = stInsuranceProdukID;
    }

    public String getStKeterangan() {
        return stKeterangan;
    }

    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

    public String getStKodeCetak() {
        return stKodeCetak;
    }

    public void setStKodeCetak(String stKodeCetak) {
        this.stKodeCetak = stKodeCetak;
    }

    public String getStKodeEntry() {
        return stKodeEntry;
    }

    public void setStKodeEntry(String stKodeEntry) {
        this.stKodeEntry = stKodeEntry;
    }

    public String getStNomorBuktiDN() {
        return stNomorBuktiDN;
    }

    public void setStNomorBuktiDN(String stNomorBuktiDN) {
        this.stNomorBuktiDN = stNomorBuktiDN;
    }
    
    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }

    public BigDecimal getDbFeeBase() {
        return dbFeeBase;
    }

    public void setDbFeeBase(BigDecimal dbFeeBase) {
        this.dbFeeBase = dbFeeBase;
    }
}
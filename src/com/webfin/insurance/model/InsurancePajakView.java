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

public class InsurancePajakView extends DTO {

   /*
CREATE TABLE aba_pajak
(
  produk_id bigint,
  nopol character varying(32),
  kodeko character varying(32),
  konter bigint,
  pajak_a numeric,
  pajak_b numeric,
  pajak_c numeric,
  pajak_d numeric,
  b_fee numeric,
  baypaj_a numeric,
  baypaj_b numeric,
  baypaj_c numeric,
  baypaj_d numeric,
  bayb_fee numeric,
  tglbay_a timestamp without time zone,
  tglbay_b timestamp without time zone,
  tglbay_c timestamp without time zone,
  tglbay_d timestamp without time zone,
  tglb_fee timestamp without time zone,

  nobuk_a character varying(32),
  nobuk_b character varying(32),
  nobuk_c character varying(32),
  nobuk_d character varying(32),
  nobukb_fee character varying(32),

  norek_a character varying(32),
  norek_b character varying(32),
  norek_c character varying(32),
  norek_d character varying(32),
  norekb_fee character varying(32),

  tglent_a timestamp without time zone,
  tglent_b timestamp without time zone,
  tglent_c timestamp without time zone,
  tglent_d timestamp without time zone,
  tglentb_fe timestamp without time zone,

  tgltran timestamp without time zone,
  tglrest timestamp without time zone,
  npwp1 character varying(32),
  npwp2 character varying(32),
  npwp3 character varying(32),
  npwp4 character varying(32),
  npwp5 character varying(32)
)
WITH OIDS;
ALTER TABLE aba_pajak OWNER TO postgres;  
   */

   public static String tableName = "aba_pajak";

   public static String fieldMap[][] = {
   	   {"stPolicyID", "pol_id*pk*nd"},
           {"stInsuranceNoPolis", "nopol"},
           {"stInsuranceKonter", "konter"},
           {"stInsuranceKodeKoasuransi", "kodeko"},
           {"dbPajakA","pajak_a"},
           {"dbPajakB","pajak_b"},
           {"dbPajakC","pajak_c"},
           {"dbPajakD","pajak_d"},
           {"dbBrokerFee", "b_fee"},
           {"dbBayarPajakA", "baypaj_a"},
           {"dbBayarPajakB", "baypaj_b"},
           {"dbBayarPajakC", "baypaj_c"},
           {"dbBayarPajakD", "baypaj_d"},
           {"dbBayarBrokerFee", "bayb_fee"},
           {"dtTanggalBayarA", "tglbay_a"},
           {"dtTanggalBayarB", "tglbay_b"},
           {"dtTanggalBayarC", "tglbay_c"},
           {"dtTanggalBayarD", "tglbay_d"},
           {"dtTanggalBayarBrokerFee", "tglb_fee"},
           {"stInsuranceNoBuktiA", "nobuk_a"},
           {"stInsuranceNoBuktiB", "nobuk_b"},
           {"stInsuranceNoBuktiC", "nobuk_c"},
           {"stInsuranceNoBuktiD", "nobuk_d"},
           {"stInsuranceNoBuktiBrokerFee", "nobukb_fee"},
           {"stInsuranceNoRekeningA", "norek_a"},
           {"stInsuranceNoRekeningB", "norek_b"},
           {"stInsuranceNoRekeningC", "norek_c"},
           {"stInsuranceNoRekeningD", "norek_d"},
           {"stInsuranceNoRekeningBrokerFee", "norekb_fee"},
           {"dtTanggalEntryA", "tglent_a"},
           {"dtTanggalEntryB", "tglent_b"},
           {"dtTanggalEntryC", "tglent_c"},
           {"dtTanggalEntryD", "tglent_d"},
           {"dtTanggalEntryBrokerFee", "tglentb_fee"},
           {"dtTanggalTran", "tgltran"},
           {"dtTanggalRest", "tglrest"},
           {"stNPWP1","npwp1"},
           {"stNPWP2","npwp2"},
           {"stNPWP3","npwp3"},
           {"stNPWP4","npwp4"},
           {"stNPWP5","npwp5"},
          {"stKodePajakA","kdpajaka"},
          {"stKodePajakB","kdpajakb"},
          {"stKodePajakC","kdpajakc"},
          {"stKodePajakD","kdpajakd"},
          {"stFlag","flag"},
   };
    
    private String stPolicyID;
    private String stInsuranceProdukID;
    private String stInsuranceNoPolis;
    private String stInsuranceKonter;
    private String stInsuranceKodeKoasuransi;
    private BigDecimal dbPajakA;
    private BigDecimal dbPajakB;
    private BigDecimal dbPajakC;
    private BigDecimal dbPajakD;
    private BigDecimal dbBrokerFee;
    private BigDecimal dbBayarPajakA;
    private BigDecimal dbBayarPajakB;
    private BigDecimal dbBayarPajakC;
    private BigDecimal dbBayarPajakD;
    private BigDecimal dbBayarBrokerFee;
    private Date dtTanggalBayarA;
    private Date dtTanggalBayarB;
    private Date dtTanggalBayarC;
    private Date dtTanggalBayarD;
    private Date dtTanggalBayarBrokerFee;
    private String stInsuranceNoBuktiA;
    private String stInsuranceNoBuktiB;
    private String stInsuranceNoBuktiC;
    private String stInsuranceNoBuktiD;
    private String stInsuranceNoBuktiBrokerFee;
    private String stInsuranceNoRekeningA;
    private String stInsuranceNoRekeningB;
    private String stInsuranceNoRekeningC;
    private String stInsuranceNoRekeningD;
    private String stInsuranceNoRekeningBrokerFee;
    private Date dtTanggalEntryA;
    private Date dtTanggalEntryB;
    private Date dtTanggalEntryC;
    private Date dtTanggalEntryD;
    private Date dtTanggalEntryBrokerFee;
    private Date dtTanggalTran;
    private Date dtTanggalRest;
    private String stNPWP1;
    private String stNPWP2;
    private String stNPWP3;
    private String stNPWP4;
    private String stNPWP5;
    private String stKodePajakA;
    private String stKodePajakB;
    private String stKodePajakC;
    private String stKodePajakD;
    private String stFlag;
    
    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public BigDecimal getDbBayarBrokerFee() {
        return dbBayarBrokerFee;
    }

    public void setDbBayarBrokerFee(BigDecimal dbBayarBrokerFee) {
        this.dbBayarBrokerFee = dbBayarBrokerFee;
    }

    public BigDecimal getDbBayarPajakA() {
        return dbBayarPajakA;
    }

    public void setDbBayarPajakA(BigDecimal dbBayarPajakA) {
        this.dbBayarPajakA = dbBayarPajakA;
    }

    public BigDecimal getDbBayarPajakB() {
        return dbBayarPajakB;
    }

    public void setDbBayarPajakB(BigDecimal dbBayarPajakB) {
        this.dbBayarPajakB = dbBayarPajakB;
    }

    public BigDecimal getDbBayarPajakC() {
        return dbBayarPajakC;
    }

    public void setDbBayarPajakC(BigDecimal dbBayarPajakC) {
        this.dbBayarPajakC = dbBayarPajakC;
    }

    public BigDecimal getDbBayarPajakD() {
        return dbBayarPajakD;
    }

    public void setDbBayarPajakD(BigDecimal dbBayarPajakD) {
        this.dbBayarPajakD = dbBayarPajakD;
    }

    public BigDecimal getDbBrokerFee() {
        return dbBrokerFee;
    }

    public void setDbBrokerFee(BigDecimal dbBrokerFee) {
        this.dbBrokerFee = dbBrokerFee;
    }

    public BigDecimal getDbPajakA() {
        return dbPajakA;
    }

    public void setDbPajakA(BigDecimal dbPajakA) {
        this.dbPajakA = dbPajakA;
    }

    public BigDecimal getDbPajakB() {
        return dbPajakB;
    }

    public void setDbPajakB(BigDecimal dbPajakB) {
        this.dbPajakB = dbPajakB;
    }

    public BigDecimal getDbPajakC() {
        return dbPajakC;
    }

    public void setDbPajakC(BigDecimal dbPajakC) {
        this.dbPajakC = dbPajakC;
    }

    public BigDecimal getDbPajakD() {
        return dbPajakD;
    }

    public void setDbPajakD(BigDecimal dbPajakD) {
        this.dbPajakD = dbPajakD;
    }

    public Date getDtTanggalBayarA() {
        return dtTanggalBayarA;
    }

    public void setDtTanggalBayarA(Date dtTanggalBayarA) {
        this.dtTanggalBayarA = dtTanggalBayarA;
    }

    public Date getDtTanggalBayarB() {
        return dtTanggalBayarB;
    }

    public void setDtTanggalBayarB(Date dtTanggalBayarB) {
        this.dtTanggalBayarB = dtTanggalBayarB;
    }

    public Date getDtTanggalBayarBrokerFee() {
        return dtTanggalBayarBrokerFee;
    }

    public void setDtTanggalBayarBrokerFee(Date dtTanggalBayarBrokerFee) {
        this.dtTanggalBayarBrokerFee = dtTanggalBayarBrokerFee;
    }

    public Date getDtTanggalBayarC() {
        return dtTanggalBayarC;
    }

    public void setDtTanggalBayarC(Date dtTanggalBayarC) {
        this.dtTanggalBayarC = dtTanggalBayarC;
    }

    public Date getDtTanggalBayarD() {
        return dtTanggalBayarD;
    }

    public void setDtTanggalBayarD(Date dtTanggalBayarD) {
        this.dtTanggalBayarD = dtTanggalBayarD;
    }

    public Date getDtTanggalEntryA() {
        return dtTanggalEntryA;
    }

    public void setDtTanggalEntryA(Date dtTanggalEntryA) {
        this.dtTanggalEntryA = dtTanggalEntryA;
    }

    public Date getDtTanggalEntryB() {
        return dtTanggalEntryB;
    }

    public void setDtTanggalEntryB(Date dtTanggalEntryB) {
        this.dtTanggalEntryB = dtTanggalEntryB;
    }

    public Date getDtTanggalEntryBrokerFee() {
        return dtTanggalEntryBrokerFee;
    }

    public void setDtTanggalEntryBrokerFee(Date dtTanggalEntryBrokerFee) {
        this.dtTanggalEntryBrokerFee = dtTanggalEntryBrokerFee;
    }

    public Date getDtTanggalEntryC() {
        return dtTanggalEntryC;
    }

    public void setDtTanggalEntryC(Date dtTanggalEntryC) {
        this.dtTanggalEntryC = dtTanggalEntryC;
    }

    public Date getDtTanggalEntryD() {
        return dtTanggalEntryD;
    }

    public void setDtTanggalEntryD(Date dtTanggalEntryD) {
        this.dtTanggalEntryD = dtTanggalEntryD;
    }

    public Date getDtTanggalRest() {
        return dtTanggalRest;
    }

    public void setDtTanggalRest(Date dtTanggalRest) {
        this.dtTanggalRest = dtTanggalRest;
    }

    public Date getDtTanggalTran() {
        return dtTanggalTran;
    }

    public void setDtTanggalTran(Date dtTanggalTran) {
        this.dtTanggalTran = dtTanggalTran;
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

    public String getStInsuranceNoBuktiA() {
        return stInsuranceNoBuktiA;
    }

    public void setStInsuranceNoBuktiA(String stInsuranceNoBuktiA) {
        this.stInsuranceNoBuktiA = stInsuranceNoBuktiA;
    }

    public String getStInsuranceNoBuktiB() {
        return stInsuranceNoBuktiB;
    }

    public void setStInsuranceNoBuktiB(String stInsuranceNoBuktiB) {
        this.stInsuranceNoBuktiB = stInsuranceNoBuktiB;
    }

    public String getStInsuranceNoBuktiBrokerFee() {
        return stInsuranceNoBuktiBrokerFee;
    }

    public void setStInsuranceNoBuktiBrokerFee(String stInsuranceNoBuktiBrokerFee) {
        this.stInsuranceNoBuktiBrokerFee = stInsuranceNoBuktiBrokerFee;
    }

    public String getStInsuranceNoBuktiC() {
        return stInsuranceNoBuktiC;
    }

    public void setStInsuranceNoBuktiC(String stInsuranceNoBuktiC) {
        this.stInsuranceNoBuktiC = stInsuranceNoBuktiC;
    }

    public String getStInsuranceNoBuktiD() {
        return stInsuranceNoBuktiD;
    }

    public void setStInsuranceNoBuktiD(String stInsuranceNoBuktiD) {
        this.stInsuranceNoBuktiD = stInsuranceNoBuktiD;
    }

    public String getStInsuranceNoPolis() {
        return stInsuranceNoPolis;
    }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis) {
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }

    public String getStInsuranceNoRekeningA() {
        return stInsuranceNoRekeningA;
    }

    public void setStInsuranceNoRekeningA(String stInsuranceNoRekeningA) {
        this.stInsuranceNoRekeningA = stInsuranceNoRekeningA;
    }

    public String getStInsuranceNoRekeningB() {
        return stInsuranceNoRekeningB;
    }

    public void setStInsuranceNoRekeningB(String stInsuranceNoRekeningB) {
        this.stInsuranceNoRekeningB = stInsuranceNoRekeningB;
    }

    public String getStInsuranceNoRekeningBrokerFee() {
        return stInsuranceNoRekeningBrokerFee;
    }

    public void setStInsuranceNoRekeningBrokerFee(String stInsuranceNoRekeningBrokerFee) {
        this.stInsuranceNoRekeningBrokerFee = stInsuranceNoRekeningBrokerFee;
    }

    public String getStInsuranceNoRekeningC() {
        return stInsuranceNoRekeningC;
    }

    public void setStInsuranceNoRekeningC(String stInsuranceNoRekeningC) {
        this.stInsuranceNoRekeningC = stInsuranceNoRekeningC;
    }

    public String getStInsuranceNoRekeningD() {
        return stInsuranceNoRekeningD;
    }

    public void setStInsuranceNoRekeningD(String stInsuranceNoRekeningD) {
        this.stInsuranceNoRekeningD = stInsuranceNoRekeningD;
    }

    public String getStInsuranceProdukID() {
        return stInsuranceProdukID;
    }

    public void setStInsuranceProdukID(String stInsuranceProdukID) {
        this.stInsuranceProdukID = stInsuranceProdukID;
    }

    public String getStNPWP1() {
        return stNPWP1;
    }

    public void setStNPWP1(String stNPWP1) {
        this.stNPWP1 = stNPWP1;
    }

    public String getStNPWP2() {
        return stNPWP2;
    }

    public void setStNPWP2(String stNPWP2) {
        this.stNPWP2 = stNPWP2;
    }

    public String getStNPWP3() {
        return stNPWP3;
    }

    public void setStNPWP3(String stNPWP3) {
        this.stNPWP3 = stNPWP3;
    }

    public String getStNPWP4() {
        return stNPWP4;
    }

    public void setStNPWP4(String stNPWP4) {
        this.stNPWP4 = stNPWP4;
    }

    public String getStNPWP5() {
        return stNPWP5;
    }

    public void setStNPWP5(String stNPWP5) {
        this.stNPWP5 = stNPWP5;
    }
    
   public String getStKodePajakA() {
        return stKodePajakA;
    }

    public void setStKodePajakA(String stKodePajakA) {
        this.stKodePajakA = stKodePajakA;
    }

    public String getStKodePajakB() {
        return stKodePajakB;
    }

    public void setStKodePajakB(String stKodePajakB) {
        this.stKodePajakB = stKodePajakB;
    }

    public String getStKodePajakC() {
        return stKodePajakC;
    }

    public void setStKodePajakC(String stKodePajakC) {
        this.stKodePajakC = stKodePajakC;
    }

    public String getStKodePajakD() {
        return stKodePajakD;
    }

    public void setStKodePajakD(String stKodePajakD) {
        this.stKodePajakD = stKodePajakD;
    }
    
    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }
}
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

public class InsuranceHutangView extends DTO{

   /*
  produk_id bigint,
  nopol character varying(32),
  konter bigint,
  kodeko character varying(32),
  nobuk1 character varying(32),
  norek1 character varying(32),
  nila numeric,
  tglb1 timestamp without time zone,
  bayhut1 numeric,
  ket1 character varying(32),
  tglent1 timestamp without time zone,
  kodent1 character varying(32),
  nobuk2 character varying(32),
  norek2 character varying(32),
  nilb numeric,
  tglb2 timestamp without time zone,
  bayhut2 numeric,
  ket2 character varying(32),
  tglent2 timestamp without time zone,
  kodent2 character varying(32),
  nilc numeric,
  nobuk3 character varying(32),
  norek3 character varying(32),
  tglb3 timestamp without time zone,
  bayhut3 numeric,
  ket3 character varying(32),
  tglent3 timestamp without time zone,
  kodent3 character varying(32),
  nobuk4 character varying(32),
  norek4 character varying(32),
  nild numeric,
  tglb4 timestamp without time zone,
  bayhut4 numeric,
  ket4 character varying(32),
  tglent4 timestamp without time zone,
  kodent4 character varying(32),
  nobuk5 character varying(32),
  norek5 character varying(32),
  tglb5 timestamp without time zone,
  bayhut5 numeric,
  ket5 character varying(32),
  tglent5 timestamp without time zone,
  kodent5 character varying(32),
  tgltran1 timestamp without time zone,
  tglrest1 timestamp without time zone,
  tgltran2 timestamp without time zone,
  tglrest2 timestamp without time zone,
  tgltran3 timestamp without time zone,
  tglrest3 timestamp without time zone,
  tgltran4 timestamp without time zone,
  tglrest4 timestamp without time zone,
  tgltran5 timestamp without time zone,
  tglrest5 timestamp without time zone,
  tglrest timestamp without time zone,
  tgltran timestamp without time zone
   */
   
   private String stPolicyID;
    private String stInsuranceProdukID;
    private String stInsuranceNoPolis;
    private String stInsuranceKonter;
    private String stInsuranceKodeKoasuransi;
    private String stInsuranceNoBukti1;
    private String stInsuranceNoRekening1;
    private BigDecimal dbNilaiA;
    private Date dtTanggalBayar1;
    private BigDecimal dbBayarHutang1;
    private String stKeterangan1;
    private Date dtTanggalEntry1;
    private String stKodeEntry1;
    private String stInsuranceNoBukti2;
    private String stInsuranceNoRekening2;
    private BigDecimal dbNilaiB;
    private Date dtTanggalBayar2;
    private BigDecimal dbBayarHutang2;
    private String stKeterangan2;
    private Date dtTanggalEntry2;
    private String stKodeEntry2;
    private String stInsuranceNoBukti3;
    private String stInsuranceNoRekening3;
    private BigDecimal dbNilaiC;
    private Date dtTanggalBayar3;
    private BigDecimal dbBayarHutang3;
    private String stKeterangan3;
    private Date dtTanggalEntry3;
    private String stKodeEntry3;
    private String stInsuranceNoBukti4;
    private String stInsuranceNoRekening4;
    private BigDecimal dbNilaiD;
    private Date dtTanggalBayar4;
    private BigDecimal dbBayarHutang4;
    private String stKeterangan4;
    private Date dtTanggalEntry4;
    private String stKodeEntry4;
    private String stInsuranceNoBukti5;
    private String stInsuranceNoRekening5;
    private Date dtTanggalBayar5;
    private BigDecimal dbBayarHutang5;
    private String stKeterangan5;
    private Date dtTanggalEntry5;
    private String stKodeEntry5;
    private Date dtTanggalTransaksi1;
    private Date dtTanggalRest1;
    private Date dtTanggalTransaksi2;
    private Date dtTanggalRest2;
    private Date dtTanggalTransaksi3;
    private Date dtTanggalRest3;
    private Date dtTanggalTransaksi4;
    private Date dtTanggalRest4;
    private Date dtTanggalTransaksi15;
    private Date dtTanggalRest5;
    private Date dtTanggalRest;
    private Date dtTanggalTransaksi;
    private String stKodePajakA;
    private String stKodePajakB;
    private String stKodePajakC;
    private String stKodePajakD;
    private BigDecimal dbNilaiE;
    
    private String stFlag;

   public static String tableName = "aba_hutang";

   public static String fieldMap[][] = {
   	  {"stPolicyID", "pol_id*pk*nd"},
      {"stInsuranceNoPolis","nopol"},
      {"stInsuranceKonter","konter"},
      {"stInsuranceKodeKoasuransi","kodeko"},
      {"stInsuranceNoBukti1","nobuk1"},
      {"stInsuranceNoRekening1","norek1"},
      {"dbNilaiA","nila"},
      {"dtTanggalBayar1","tglb1"},
      {"dbBayarHutang1","bayhut1"},
      {"stKeterangan1","ket1"},
      {"dtTanggalEntry1","tglent1"},
      {"stKodeEntry1","kodent1"},
      {"stInsuranceNoBukti2","nobuk2"},
      {"stInsuranceNoRekening2","norek2"},
      {"dbNilaiB","nilb"},
      {"dtTanggalBayar2","tglb2"},
      {"dbBayarHutang2","bayhut2"},
      {"stKeterangan2","ket2"},
      {"dtTanggalEntry2","tglent2"},
      {"stKodeEntry2","kodent2"},
      {"stInsuranceNoBukti3","nobuk3"},
      {"stInsuranceNoRekening3","norek3"},
      {"dbNilaiC","nilc"},
      {"dtTanggalBayar3","tglb3"},
      {"dbBayarHutang3","bayhut3"},
      {"stKeterangan3","ket3"},
      {"dtTanggalEntry3","tglent3"},
      {"stKodeEntry3","kodent3"},
      {"stInsuranceNoBukti4","nobuk4"},
      {"stInsuranceNoRekening4","norek4"},
      {"dbNilaiD","nild"},
      {"dtTanggalBayar4","tglb4"},
      {"dbBayarHutang4","bayhut4"},
      {"stKeterangan4","ket4"},
      {"dtTanggalEntry4","tglent4"},
      {"stKodeEntry4","kodent4"},
      {"stInsuranceNoBukti5","nobuk5"},
      {"stInsuranceNoRekening5","norek5"},
      {"dtTanggalBayar5","tglb5"},
      {"dbBayarHutang5","bayhut5"},
      {"stKeterangan5","ket5"},
      {"dtTanggalEntry5","tglent5"},
      {"stKodeEntry5","kodent5"},
      {"dtTanggalTransaksi1","tgltran1"},
      {"dtTanggalRest1","tglrest1"},
      {"dtTanggalTransaksi2","tgltran2"},
      {"dtTanggalRest2","tglrest2"},
      {"dtTanggalTransaksi3","tgltran3"},
      {"dtTanggalRest3","tglrest3"},
      {"dtTanggalTransaksi4","tgltran4"},
      {"dtTanggalRest4","tglrest4"},
      {"dtTanggalTransaksi15","tgltran5"},
      {"dtTanggalRest5","tglrest5"},
      {"dtTanggalRest","tglrest"},
      {"dtTanggalTransaksi","tgltran"},
      {"stKodePajakA","kdpajaka"},
      {"stKodePajakB","kdpajakb"},
      {"stKodePajakC","kdpajakc"},
      {"stKodePajakD","kdpajakd"},
      {"stFlag","flag"},
      {"dbNilaiE","nile"},
   };
   
   
    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public BigDecimal getDbBayarHutang1() {
        return dbBayarHutang1;
    }

    public void setDbBayarHutang1(BigDecimal dbBayarHutang1) {
        this.dbBayarHutang1 = dbBayarHutang1;
    }

    public BigDecimal getDbBayarHutang3() {
        return dbBayarHutang3;
    }

    public void setDbBayarHutang3(BigDecimal dbBayarHutang3) {
        this.dbBayarHutang3 = dbBayarHutang3;
    }

    public BigDecimal getDbBayarHutang4() {
        return dbBayarHutang4;
    }

    public void setDbBayarHutang4(BigDecimal dbBayarHutang4) {
        this.dbBayarHutang4 = dbBayarHutang4;
    }

    public BigDecimal getDbBayarHutang5() {
        return dbBayarHutang5;
    }

    public void setDbBayarHutang5(BigDecimal dbBayarHutang5) {
        this.dbBayarHutang5 = dbBayarHutang5;
    }

    public BigDecimal getDbBayarHutang2() {
        return dbBayarHutang2;
    }

    public void setDbBayarHutang2(BigDecimal dbBayarHutang2) {
        this.dbBayarHutang2 = dbBayarHutang2;
    }

    public BigDecimal getDbNilaiA() {
        return dbNilaiA;
    }

    public void setDbNilaiA(BigDecimal dbNilaiA) {
        this.dbNilaiA = dbNilaiA;
    }

    public BigDecimal getDbNilaiB() {
        return dbNilaiB;
    }

    public void setDbNilaiB(BigDecimal dbNilaiB) {
        this.dbNilaiB = dbNilaiB;
    }

    public BigDecimal getDbNilaiC() {
        return dbNilaiC;
    }

    public void setDbNilaiC(BigDecimal dbNilaiC) {
        this.dbNilaiC = dbNilaiC;
    }

    public BigDecimal getDbNilaiD() {
        return dbNilaiD;
    }

    public void setDbNilaiD(BigDecimal dbNilaiD) {
        this.dbNilaiD = dbNilaiD;
    }

    public Date getDtTanggalBayar1() {
        return dtTanggalBayar1;
    }

    public void setDtTanggalBayar1(Date dtTanggalBayar1) {
        this.dtTanggalBayar1 = dtTanggalBayar1;
    }

    public Date getDtTanggalBayar2() {
        return dtTanggalBayar2;
    }

    public void setDtTanggalBayar2(Date dtTanggalBayar2) {
        this.dtTanggalBayar2 = dtTanggalBayar2;
    }

    public Date getDtTanggalBayar3() {
        return dtTanggalBayar3;
    }

    public void setDtTanggalBayar3(Date dtTanggalBayar3) {
        this.dtTanggalBayar3 = dtTanggalBayar3;
    }

    public Date getDtTanggalBayar4() {
        return dtTanggalBayar4;
    }

    public void setDtTanggalBayar4(Date dtTanggalBayar4) {
        this.dtTanggalBayar4 = dtTanggalBayar4;
    }

    public Date getDtTanggalBayar5() {
        return dtTanggalBayar5;
    }

    public void setDtTanggalBayar5(Date dtTanggalBayar5) {
        this.dtTanggalBayar5 = dtTanggalBayar5;
    }

    public Date getDtTanggalEntry1() {
        return dtTanggalEntry1;
    }

    public void setDtTanggalEntry1(Date dtTanggalEntry1) {
        this.dtTanggalEntry1 = dtTanggalEntry1;
    }

    public Date getDtTanggalEntry2() {
        return dtTanggalEntry2;
    }

    public void setDtTanggalEntry2(Date dtTanggalEntry2) {
        this.dtTanggalEntry2 = dtTanggalEntry2;
    }

    public Date getDtTanggalEntry3() {
        return dtTanggalEntry3;
    }

    public void setDtTanggalEntry3(Date dtTanggalEntry3) {
        this.dtTanggalEntry3 = dtTanggalEntry3;
    }

    public Date getDtTanggalEntry4() {
        return dtTanggalEntry4;
    }

    public void setDtTanggalEntry4(Date dtTanggalEntry4) {
        this.dtTanggalEntry4 = dtTanggalEntry4;
    }

    public Date getDtTanggalEntry5() {
        return dtTanggalEntry5;
    }

    public void setDtTanggalEntry5(Date dtTanggalEntry5) {
        this.dtTanggalEntry5 = dtTanggalEntry5;
    }

    public Date getDtTanggalRest1() {
        return dtTanggalRest1;
    }

    public void setDtTanggalRest1(Date dtTanggalRest1) {
        this.dtTanggalRest1 = dtTanggalRest1;
    }

    public Date getDtTanggalRest2() {
        return dtTanggalRest2;
    }

    public void setDtTanggalRest2(Date dtTanggalRest2) {
        this.dtTanggalRest2 = dtTanggalRest2;
    }

    public Date getDtTanggalRest3() {
        return dtTanggalRest3;
    }

    public void setDtTanggalRest3(Date dtTanggalRest3) {
        this.dtTanggalRest3 = dtTanggalRest3;
    }

    public Date getDtTanggalRest4() {
        return dtTanggalRest4;
    }

    public void setDtTanggalRest4(Date dtTanggalRest4) {
        this.dtTanggalRest4 = dtTanggalRest4;
    }

    public Date getDtTanggalRest5() {
        return dtTanggalRest5;
    }

    public void setDtTanggalRest5(Date dtTanggalRest5) {
        this.dtTanggalRest5 = dtTanggalRest5;
    }

    public Date getDtTanggalRest() {
        return dtTanggalRest;
    }

    public void setDtTanggalRest(Date dtTanggalRest) {
        this.dtTanggalRest = dtTanggalRest;
    }

    public Date getDtTanggalTransaksi15() {
        return dtTanggalTransaksi15;
    }

    public void setDtTanggalTransaksi15(Date dtTanggalTransaksi15) {
        this.dtTanggalTransaksi15 = dtTanggalTransaksi15;
    }

    public Date getDtTanggalTransaksi1() {
        return dtTanggalTransaksi1;
    }

    public void setDtTanggalTransaksi1(Date dtTanggalTransaksi1) {
        this.dtTanggalTransaksi1 = dtTanggalTransaksi1;
    }

    public Date getDtTanggalTransaksi2() {
        return dtTanggalTransaksi2;
    }

    public void setDtTanggalTransaksi2(Date dtTanggalTransaksi2) {
        this.dtTanggalTransaksi2 = dtTanggalTransaksi2;
    }

    public Date getDtTanggalTransaksi3() {
        return dtTanggalTransaksi3;
    }

    public void setDtTanggalTransaksi3(Date dtTanggalTransaksi3) {
        this.dtTanggalTransaksi3 = dtTanggalTransaksi3;
    }

    public Date getDtTanggalTransaksi4() {
        return dtTanggalTransaksi4;
    }

    public void setDtTanggalTransaksi4(Date dtTanggalTransaksi4) {
        this.dtTanggalTransaksi4 = dtTanggalTransaksi4;
    }

    public Date getDtTanggalTransaksi() {
        return dtTanggalTransaksi;
    }

    public void setDtTanggalTransaksi(Date dtTanggalTransaksi) {
        this.dtTanggalTransaksi = dtTanggalTransaksi;
    }

    public static String[][] getFieldMap() {
        return fieldMap;
    }

    public static void setFieldMap(String[][] fieldMap) {
        InsuranceHutangView.fieldMap = fieldMap;
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

    public String getStInsuranceNoBukti1() {
        return stInsuranceNoBukti1;
    }

    public void setStInsuranceNoBukti1(String stInsuranceNoBukti1) {
        this.stInsuranceNoBukti1 = stInsuranceNoBukti1;
    }

    public String getStInsuranceNoBukti2() {
        return stInsuranceNoBukti2;
    }

    public void setStInsuranceNoBukti2(String stInsuranceNoBukti2) {
        this.stInsuranceNoBukti2 = stInsuranceNoBukti2;
    }

    public String getStInsuranceNoBukti3() {
        return stInsuranceNoBukti3;
    }

    public void setStInsuranceNoBukti3(String stInsuranceNoBukti3) {
        this.stInsuranceNoBukti3 = stInsuranceNoBukti3;
    }

    public String getStInsuranceNoBukti4() {
        return stInsuranceNoBukti4;
    }

    public void setStInsuranceNoBukti4(String stInsuranceNoBukti4) {
        this.stInsuranceNoBukti4 = stInsuranceNoBukti4;
    }

    public String getStInsuranceNoBukti5() {
        return stInsuranceNoBukti5;
    }

    public void setStInsuranceNoBukti5(String stInsuranceNoBukti5) {
        this.stInsuranceNoBukti5 = stInsuranceNoBukti5;
    }

    public String getStInsuranceNoPolis() {
        return stInsuranceNoPolis;
    }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis) {
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }

    public String getStInsuranceNoRekening1() {
        return stInsuranceNoRekening1;
    }

    public void setStInsuranceNoRekening1(String stInsuranceNoRekening1) {
        this.stInsuranceNoRekening1 = stInsuranceNoRekening1;
    }

    public String getStInsuranceNoRekening2() {
        return stInsuranceNoRekening2;
    }

    public void setStInsuranceNoRekening2(String stInsuranceNoRekening2) {
        this.stInsuranceNoRekening2 = stInsuranceNoRekening2;
    }

    public String getStInsuranceNoRekening3() {
        return stInsuranceNoRekening3;
    }

    public void setStInsuranceNoRekening3(String stInsuranceNoRekening3) {
        this.stInsuranceNoRekening3 = stInsuranceNoRekening3;
    }

    public String getStInsuranceNoRekening4() {
        return stInsuranceNoRekening4;
    }

    public void setStInsuranceNoRekening4(String stInsuranceNoRekening4) {
        this.stInsuranceNoRekening4 = stInsuranceNoRekening4;
    }

    public String getStInsuranceNoRekening5() {
        return stInsuranceNoRekening5;
    }

    public void setStInsuranceNoRekening5(String stInsuranceNoRekening5) {
        this.stInsuranceNoRekening5 = stInsuranceNoRekening5;
    }

    public String getStInsuranceProdukID() {
        return stInsuranceProdukID;
    }

    public void setStInsuranceProdukID(String stInsuranceProdukID) {
        this.stInsuranceProdukID = stInsuranceProdukID;
    }

    public String getStKeterangan1() {
        return stKeterangan1;
    }

    public void setStKeterangan1(String stKeterangan1) {
        this.stKeterangan1 = stKeterangan1;
    }

    public String getStKeterangan2() {
        return stKeterangan2;
    }

    public void setStKeterangan2(String stKeterangan2) {
        this.stKeterangan2 = stKeterangan2;
    }

    public String getStKeterangan3() {
        return stKeterangan3;
    }

    public void setStKeterangan3(String stKeterangan3) {
        this.stKeterangan3 = stKeterangan3;
    }

    public String getStKeterangan4() {
        return stKeterangan4;
    }

    public void setStKeterangan4(String stKeterangan4) {
        this.stKeterangan4 = stKeterangan4;
    }

    public String getStKeterangan5() {
        return stKeterangan5;
    }

    public void setStKeterangan5(String stKeterangan5) {
        this.stKeterangan5 = stKeterangan5;
    }

    public String getStKodeEntry1() {
        return stKodeEntry1;
    }

    public void setStKodeEntry1(String stKodeEntry1) {
        this.stKodeEntry1 = stKodeEntry1;
    }

    public String getStKodeEntry2() {
        return stKodeEntry2;
    }

    public void setStKodeEntry2(String stKodeEntry2) {
        this.stKodeEntry2 = stKodeEntry2;
    }

    public String getStKodeEntry3() {
        return stKodeEntry3;
    }

    public void setStKodeEntry3(String stKodeEntry3) {
        this.stKodeEntry3 = stKodeEntry3;
    }

    public String getStKodeEntry4() {
        return stKodeEntry4;
    }

    public void setStKodeEntry4(String stKodeEntry4) {
        this.stKodeEntry4 = stKodeEntry4;
    }

    public String getStKodeEntry5() {
        return stKodeEntry5;
    }

    public void setStKodeEntry5(String stKodeEntry5) {
        this.stKodeEntry5 = stKodeEntry5;
    }

    public static String getTableName() {
        return tableName;
    }

    public static void setTableName(String tableName) {
        InsuranceHutangView.tableName = tableName;
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

    public BigDecimal getDbNilaiE() {
        return dbNilaiE;
    }

    public void setDbNilaiE(BigDecimal dbNilaiE) {
        this.dbNilaiE = dbNilaiE;
    }
}
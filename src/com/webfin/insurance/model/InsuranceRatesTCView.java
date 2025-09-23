/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceRatesBigView
 * Author:  Denny Mahendra
 * Created: Oct 18, 2007 12:22:54 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;
import java.util.Date;

public class InsuranceRatesTCView extends DTO implements RecordAudit {
   /*
-- Table: ins_rates_big

-- CREATE TABLE ins_rates_tc
(
  tc_id bigint NOT NULL,
  kode_produk character varying(3),
  description character varying(255),
  period_start timestamp without time zone,
  period_end timestamp without time zone,
  rate_pmil numeric,
  active_flag character varying(1),
  max_tsi numeric,
  max_usia_jtempo bigint,
  rate_pmil2 numeric,
  kode_pekerjaan character varying(4),
  CONSTRAINT ins_rates_tc_pkey PRIMARY KEY (tc_id)
)
WITH

   */

   public static String tableName = "ins_rates_tc";

   public static String fieldMap[][] = {
      {"stInsuranceTCID","tc_id*pk"},
      {"stKodeProduk","kode_produk"},
      {"stDescription","description"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"dbRatePermil","rate_pmil"},
      {"dbMaxTSI","max_tsi"},
      {"stMaxUsiaJTempo","max_usia_jtempo"},
      {"dbRatePermil2","rate_pmil2"},
      {"stKodePekerjaan","kode_pekerjaan"},
      {"stActiveFlag","active_flag"},
      {"dbRateKoasPermil","rate_koas_pmil"},
   };

    private String stInsuranceTCID;
    private String stKodeProduk;
    private String stDescription;
    private Date dtPeriodStart;
    private Date dtPeriodEnd;
    private BigDecimal dbRatePermil;
    private BigDecimal dbMaxTSI;
    private BigDecimal dbRatePermil2;
    private String stMaxUsiaJTempo;
    private String stKodePekerjaan;
    private String stActiveFlag;
    private BigDecimal dbRateKoasPermil;

    public BigDecimal getDbRateKoasPermil() {
        return dbRateKoasPermil;
    }

    public void setDbRateKoasPermil(BigDecimal dbRateKoasPermil) {
        this.dbRateKoasPermil = dbRateKoasPermil;
    }


    public BigDecimal getDbMaxTSI() {
        return dbMaxTSI;
    }

    public void setDbMaxTSI(BigDecimal dbMaxTSI) {
        this.dbMaxTSI = dbMaxTSI;
    }

    public BigDecimal getDbRatePermil() {
        return dbRatePermil;
    }

    public void setDbRatePermil(BigDecimal dbRatePermil) {
        this.dbRatePermil = dbRatePermil;
    }

    public BigDecimal getDbRatePermil2() {
        return dbRatePermil2;
    }

    public void setDbRatePermil2(BigDecimal dbRatePermil2) {
        this.dbRatePermil2 = dbRatePermil2;
    }

    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStInsuranceTCID() {
        return stInsuranceTCID;
    }

    public void setStInsuranceTCID(String stInsuranceTCID) {
        this.stInsuranceTCID = stInsuranceTCID;
    }

    public String getStKodePekerjaan() {
        return stKodePekerjaan;
    }

    public void setStKodePekerjaan(String stKodePekerjaan) {
        this.stKodePekerjaan = stKodePekerjaan;
    }

    public String getStKodeProduk() {
        return stKodeProduk;
    }

    public void setStKodeProduk(String stKodeProduk) {
        this.stKodeProduk = stKodeProduk;
    }

    public String getStMaxUsiaJTempo() {
        return stMaxUsiaJTempo;
    }

    public void setStMaxUsiaJTempo(String stMaxUsiaJTempo) {
        this.stMaxUsiaJTempo = stMaxUsiaJTempo;
    }

    



}

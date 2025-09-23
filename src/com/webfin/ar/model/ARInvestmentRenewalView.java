/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentRenewalView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.util.GLUtil;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.time.DateTime;

public class ARInvestmentRenewalView extends DTO implements RecordAudit {
   /*
   REATE TABLE ar_receipt
(
  ar_receipt_id int8 NOT NULL,
  receipt_no varchar(64),
  amount numeric,
  ccy varchar(3),
  posted_flag varchar(1),
  pmt_method_id int8,
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_pk PRIMARY KEY (ar_receipt_id)
    
ALTER TABLE ar_receipt ADD COLUMN description varchar(255);
ALTER TABLE ar_receipt ADD COLUMN shortdesc varchar(128);
    
ALTER TABLE ar_receipt ADD COLUMN ar_settlement_id int8;
ALTER TABLE ar_receipt ADD COLUMN entity_id int8;
    
    
)
    */
    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentRenewalView.class);
    
    public static String tableName = "ar_inv_renewal";
    
    public static String fieldMap[][] = {
        {"stARRenewalID","ar_renewal_id*pk"},
        {"stNodefo","nodefo"},
        {"stKonter","konter"},
        {"stBuktiB","bukti_b"},
        {"stNoRekeningDeposito","norekdep"},
        {"stKodedepo","kodedepo"},
        {"dtCreateDate","create_date"},
        {"stCreateWho","create_who"},
        {"dtChangeDate","change_date"},
        {"stChangeWho","change_who"},
        {"stCurrency","ccy"},
        {"dbcCurrencyRate","ccy_rate"},
        {"dbNominalKurs","nomkurs"},
        {"dbNominal","nominal"},
        {"dtTglawal","tglawal"},
        {"dtTglakhir","tglakhir"},
        {"stBulan","bulan"},
        {"stHari","hari"},
        {"dbBunga","bunga"},
        {"dbPajak","pajak"},
        {"stCostCenterCode","koda"},
        {"stPolicyTypeID","pol_type_id"},
        {"stEntityID","kdbank"},
        {"dtTgldepo","tgldepo"},
        {"dtTglmuta","tglmuta"},
        {"dtTglTrans","tgltrans"},
        {"dtTglRest","tglrest"},
        {"stKeterangan","ket"},
        {"stFlag","flag"},
        {"stRegister","register"},
    };
    
    private String stARRenewalID;
    private String stNodefo;
    private String stKonter;
    private String stBuktiB;
    private String stNoRekeningDeposito;
    private String stKodedepo;
    private Date dtCreateDate;
    private String stCreateWho;
    private Date dtChangeDate;
    private String stChangeWho;
    private String stCurrency;
    private BigDecimal dbcCurrencyRate;
    private BigDecimal dbNominalKurs;
    private BigDecimal dbNominal;
    private Date dtTglawal;
    private Date dtTglakhir;
    private String stBulan;
    private String stHari;
    private BigDecimal dbBunga;
    private BigDecimal dbPajak;
    private String stCostCenterCode;
    private String stPolicyTypeID;
    private String stEntityID;
    private Date dtTgldepo;
    private Date dtTglmuta;
    private Date dtTglTrans;
    private Date dtTglRest;
    private String stKeterangan;
    private String stFlag;
    private String stRegister;

    public String getStARRenewalID() {
        return stARRenewalID;
    }

    public void setStARRenewalID(String stARRenewalID) {
        this.stARRenewalID = stARRenewalID;
    }

    public String getStNodefo() {
        return stNodefo;
    }

    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    public String getStKonter() {
        return stKonter;
    }

    public void setStKonter(String stKonter) {
        this.stKonter = stKonter;
    }

    public String getStBuktiB() {
        return stBuktiB;
    }

    public void setStBuktiB(String stBuktiB) {
        this.stBuktiB = stBuktiB;
    }

    public String getStNoRekeningDeposito() {
        return stNoRekeningDeposito;
    }

    public void setStNoRekeningDeposito(String stNoRekeningDeposito) {
        this.stNoRekeningDeposito = stNoRekeningDeposito;
    }

    public String getStKodedepo() {
        return stKodedepo;
    }

    public void setStKodedepo(String stKodedepo) {
        this.stKodedepo = stKodedepo;
    }

    public Date getDtCreateDate() {
        return dtCreateDate;
    }

    public void setDtCreateDate(Date dtCreateDate) {
        this.dtCreateDate = dtCreateDate;
    }

    public String getStCreateWho() {
        return stCreateWho;
    }

    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }

    public Date getDtChangeDate() {
        return dtChangeDate;
    }

    public void setDtChangeDate(Date dtChangeDate) {
        this.dtChangeDate = dtChangeDate;
    }

    public String getStChangeWho() {
        return stChangeWho;
    }

    public void setStChangeWho(String stChangeWho) {
        this.stChangeWho = stChangeWho;
    }

    public String getStCurrency() {
        return stCurrency;
    }

    public void setStCurrency(String stCurrency) {
        this.stCurrency = stCurrency;
    }

    public BigDecimal getDbcCurrencyRate() {
        return dbcCurrencyRate;
    }

    public void setDbcCurrencyRate(BigDecimal dbcCurrencyRate) {
        this.dbcCurrencyRate = dbcCurrencyRate;
    }

    public BigDecimal getDbNominalKurs() {
        return dbNominalKurs;
    }

    public void setDbNominalKurs(BigDecimal dbNominalKurs) {
        this.dbNominalKurs = dbNominalKurs;
    }

    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    public Date getDtTglawal() {
        return dtTglawal;
    }

    public void setDtTglawal(Date dtTglawal) {
        this.dtTglawal = dtTglawal;
    }

    public Date getDtTglakhir() {
        return dtTglakhir;
    }

    public void setDtTglakhir(Date dtTglakhir) {
        this.dtTglakhir = dtTglakhir;
    }

    public String getStBulan() {
        return stBulan;
    }

    public void setStBulan(String stBulan) {
        this.stBulan = stBulan;
    }

    public String getStHari() {
        return stHari;
    }

    public void setStHari(String stHari) {
        this.stHari = stHari;
    }

    public BigDecimal getDbBunga() {
        return dbBunga;
    }

    public void setDbBunga(BigDecimal dbBunga) {
        this.dbBunga = dbBunga;
    }

    public BigDecimal getDbPajak() {
        return dbPajak;
    }

    public void setDbPajak(BigDecimal dbPajak) {
        this.dbPajak = dbPajak;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public Date getDtTgldepo() {
        return dtTgldepo;
    }

    public void setDtTgldepo(Date dtTgldepo) {
        this.dtTgldepo = dtTgldepo;
    }

    public Date getDtTglmuta() {
        return dtTglmuta;
    }

    public void setDtTglmuta(Date dtTglmuta) {
        this.dtTglmuta = dtTglmuta;
    }

    public Date getDtTglTrans() {
        return dtTglTrans;
    }

    public void setDtTglTrans(Date dtTglTrans) {
        this.dtTglTrans = dtTglTrans;
    }

    public Date getDtTglRest() {
        return dtTglRest;
    }

    public void setDtTglRest(Date dtTglRest) {
        this.dtTglRest = dtTglRest;
    }

    public String getStKeterangan() {
        return stKeterangan;
    }

    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }

    public String getStRegister() {
        return stRegister;
    }

    public void setStRegister(String stRegister) {
        this.stRegister = stRegister;
    }
    
}

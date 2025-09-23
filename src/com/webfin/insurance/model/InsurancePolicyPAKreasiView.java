/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyVehicleView
 * Author:  Denny Mahendra
 * Created: Nov 2, 2005 8:43:30 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.util.DTOList;

import java.math.BigDecimal;
import java.util.Date;

public class InsurancePolicyPAKreasiView
 {
   /*
   CREATE TABLE ins_pol_vehicles
(
   int8 NOT NULL,
   int8,
   int8,
   varchar(16),
   int4,
   varchar(64),
   varchar(64),
   int4,
   numeric,
   numeric,
   numeric,
   numeric,
   numeric,
   numeric,
   numeric,
   numeric,
   numeric,
   text,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_vehicles_pk PRIMARY KEY (ins_pol_obj_id)
)
   */

   public static String tableName = "ins_pol_obj";

   public static String fieldMap[][] = {
      {"stPolicyObjectID","ins_pol_obj_id*pk*nd"},
      {"stPolicyObjectRefID","ins_pol_obj_ref_id"},
      {"stPolicyID","pol_id"},
      {"stMemberName","ref1"},
      {"dtBirthDate","refd1"},
      {"stAge","ref2"},
      {"stIDNumber","ref3"},
      {"dtLiquidityStart","refd2"},
      {"stTimeOfLoan","ref4"},
      {"dbSumInsured","refn4"},
      {"dbSellingRate","ref9"},
      {"dbPremium","refn7"},
      {"dtTempSettlement","refd4"},
      {"dtPayment","refn10"},
      {"stReferenceNo","ref12"},
      {"dbCoinsRate","refn1"},
      {"dbCoinsPremium","refn2"},
      {"dbCoinsCommission","ref11"},
      {"dbCommissionRate","refn7"},
      {"dbCommission","refn9"},
      {"dtEndOfCredit","refd3"},
      {"dbRateOfPremium","refn5"},
   };

   private String stPolicyObjectID;
   private String stPolicyObjectRefID;
   private String stVoidFlag;
   private String stPolicyID;
   
   private String stMemberName;
   private String stAge;
   private String stIDNumber;
   private String stTimeOfLoan;
   private String stReferenceNo;
   
   private BigDecimal dbSumInsured;
   private BigDecimal dbSellingRate;
   private BigDecimal dbPremium;
   private BigDecimal dbCoinsRate;
   private BigDecimal dbCoinsPremium;
   private BigDecimal dbCoinsCommission;
   private BigDecimal dbCommissionRate;
   private BigDecimal dbRateOfPremium;
   
   private Date dtBirthDate;
   private Date dtLiquidityRate;
   private Date dtTempSettlement;
   private Date dtPayment;
   private Date dtEndOfCredit;
   
   public Date getDtBirthDate() {
      return dtBirthDate;
   }

   public void setDtBirthDate(Date dtBirthDate) {
      this.dtBirthDate = dtBirthDate;
   }
  

   public void setDtLiquidityRate(Date dtLiquidityRate) {
      this.dtLiquidityRate = dtLiquidityRate;
   }
   
         public Date getDtLiquidityRate() {
      return dtLiquidityRate;
   }

   public void setDtTempSettlement(Date dtTempSettlement) {
      this.dtTempSettlement = dtTempSettlement;
   }
   
         public Date getDtTempSettlement() {
      return dtTempSettlement;
   }

   public void setDtPayment(Date dtPayment) {
      this.dtPayment = dtPayment;
   }
   
         public Date getDtPayment() {
      return dtPayment;
   }

   public void setDtEndOfCredit(Date dtEndOfCredit) {
      this.dtEndOfCredit = dtEndOfCredit;
   }
   
         public Date getDtEndOfCredit() {
      return dtEndOfCredit;
   }

   
   public String getStMemberName() {
      return stMemberName;
   }

   public void setStMemberName(String stMemberName) {
      this.stMemberName = stMemberName;
   }
   
   public String getStAge() {
      return stAge;
   }

   public void setStAge(String stAge) {
      this.stAge = stAge;
   }
   
   public String getStIDNumber() {
      return stIDNumber;
   }

   public void setStIDNumber(String stIDNumber) {
      this.stIDNumber = stIDNumber;
   }
   
   public String getStTimeOfLoan() {
      return stTimeOfLoan;
   }

   public void setStTimeOfLoan(String stTimeOfLoan) {
      this.stTimeOfLoan = stTimeOfLoan;
   }
   
   public String getStReferenceNo() {
      return stReferenceNo;
   }

   public void setStReferenceNo(String stReferenceNo) {
      this.stReferenceNo = stReferenceNo;
   }
   
   public BigDecimal getDbSumInsured() {
      return dbSumInsured;
   }

   public void setDbSumInsured(BigDecimal dbSumInsured) {
      this.dbSumInsured = dbSumInsured;
   }
   
      public BigDecimal getDbSellingRate() {
      return dbSellingRate;
   }

   public void setDbSellingRate(BigDecimal dbSellingRate) {
      this.dbSellingRate = dbSellingRate;
   }
   
   public BigDecimal getDbPremium() {
      return dbPremium;
   }

   public void setDbPremium(BigDecimal dbPremium) {
      this.dbPremium = dbPremium;
   }
   
      public BigDecimal getDbCoinsRate() {
      return dbCoinsRate;
   }

   public void setDbCoinsRate(BigDecimal dbCoinsRate) {
      this.dbCoinsRate = dbCoinsRate;
   }

   public void setDbCoinsPremium(BigDecimal dbCoinsPremium) {
      this.dbCoinsPremium = dbCoinsPremium;
   }
   
   public BigDecimal getDbCoinsPremium() {
      return dbCoinsPremium;
   }

   public void setDbCoinsCommission(BigDecimal dbCoinsCommission) {
      this.dbCoinsCommission = dbCoinsCommission;
   }
   
   public BigDecimal getDbCoinsCommission() {
      return dbCoinsCommission;
   }

   public void setDbCommissionRate(BigDecimal dbCommissionRate) {
      this.dbCommissionRate = dbCommissionRate;
   }
   
   public BigDecimal getDbCommissionRate() {
      return dbCommissionRate;
   }
   
   public BigDecimal getDbRateOfPremium() {
      return dbRateOfPremium;
   }

   public void setDbRateOfPremium(BigDecimal dbRateOfPremium) {
      this.dbRateOfPremium = dbRateOfPremium;
   }
   
   public String getStPolicyObjectID() {
      return stPolicyObjectID;
   }

   public void setStPolicyObjectID(String stPolicyObjectID) {
      this.stPolicyObjectID = stPolicyObjectID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   
}

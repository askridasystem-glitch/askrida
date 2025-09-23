/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePremiumFactorView
 * Author:  Denny Mahendra
 * Created: May 27, 2006 2:23:35 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.ConvertUtil;
import com.crux.util.BDUtil;

import java.math.BigDecimal;

public class InsurancePremiumFactorView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_premium_factor
(
  ins_premium_factor_id int8 NOT NULL,
  description  varchar(255),
  period_rate_factor numeric,
  premi_factor numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_premium_factor_pk PRIMARY KEY (ins_premium_factor_id)
) without oids;

ALTER TABLE ins_premium_factor ADD COLUMN ref1 varchar(16);
ALTER TABLE ins_premium_factor ADD COLUMN f_default varchar(1);
ALTER TABLE ins_premium_factor ADD COLUMN f_active varchar(1);

   */

   public static String tableName = "ins_premium_factor";

   public static String fieldMap[][] = {
      {"stInsurancePremiumFactorID","ins_premium_factor_id*pk"},
      {"stDescription","description"},
      {"dbPeriodRateFactor","period_rate_factor"},
      {"dbPremiumFactor","premi_factor"},
      {"stReference1","ref1"},
      {"stDefaultFlag","f_default"},
      {"stActiveFlag","f_active"},
   };

   private String stReference1;
   private String stDefaultFlag;
   private String stActiveFlag;
   private String stInsurancePremiumFactorID;
   private String stDescription;
   private BigDecimal dbPeriodRateFactor;
   private BigDecimal dbPremiumFactor;

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStDefaultFlag() {
      return stDefaultFlag;
   }

   public void setStDefaultFlag(String stDefaultFlag) {
      this.stDefaultFlag = stDefaultFlag;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStInsurancePremiumFactorID() {
      return stInsurancePremiumFactorID;
   }

   public void setStInsurancePremiumFactorID(String stInsurancePremiumFactorID) {
      this.stInsurancePremiumFactorID = stInsurancePremiumFactorID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public BigDecimal getDbPeriodRateFactor() {
      return dbPeriodRateFactor;
   }

   public void setDbPeriodRateFactor(BigDecimal dbPeriodRateFactor) {
      this.dbPeriodRateFactor = dbPeriodRateFactor;
   }

   public BigDecimal getDbPremiumFactor() {
      return dbPremiumFactor;
   }

   public void setDbPremiumFactor(BigDecimal dbPremiumFactor) {
      this.dbPremiumFactor = dbPremiumFactor;
   }

   public String getStPremiumFactorDesc() {
      if (dbPremiumFactor==null) return "100%";

      return ConvertUtil.removeComma(dbPremiumFactor.movePointRight(2).toString())+"%";
   }
}

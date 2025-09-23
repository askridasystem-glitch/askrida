/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceRiskCategoryView
 * Author:  Denny Mahendra
 * Created: Aug 27, 2006 10:49:40 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;
import com.crux.util.Tools;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceRiskCategoryView extends DTO implements RecordAudit {

   /*
   (
  ins_risk_cat_id int8 NOT NULL,
  ins_risk_cat_code varchar(16),
  description varchar(128),
  poltype_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  tre_limit0 numeric,
  tre_limit1 numeric,
  tre_limit2 numeric,
  tre_limit3 numeric,
  CONSTRAINT ins_risk_cat_pk PRIMARY KEY (ins_risk_cat_id)
)
ALTER TABLE ins_risk_cat ADD COLUMN rate1 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate2 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate3 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate0 numeric;

   */


   public static String tableName = "ins_risk_cat";
   
   public static String comboFields[] = {"ins_risk_cat_id","description","ref1"};

   public static String fieldMap[][] = {
      {"stInsuranceRiskCategoryID","ins_risk_cat_id*pk"},
      {"stInsuranceRiskCategoryCode","ins_risk_cat_code"},
      {"stDescription","description"},
      {"stPolicyTypeID","poltype_id"},
      {"dbTreatyLimit0","tre_limit0"},
      {"dbTreatyLimit1","tre_limit1"},
      {"dbTreatyLimit2","tre_limit2"},
      {"dbTreatyLimit3","tre_limit3"},
      {"dbRate0","rate0"},
      {"dbRate1","rate1"},
      {"dbRate2","rate2"},
      {"dbRate3","rate3"},
      {"stExcRiskFlag","exc_risk_flag"},
      {"dtPeriodStartDate","period_start"},
      {"dtPeriodEndDate","period_end"},
      {"stReference1","ref1"},
      {"stActiveFlag","active_flag"},
      {"stPolicyTypeDesc", "policy_type_desc*n"},
      {"stInsuranceTreatyID","ins_treaty_id"},
      {"stRiskLevel","risk_level"},

      
   };

   private String stInsuranceRiskCategoryID;
   private String stInsuranceRiskCategoryCode;
   private String stDescription;
   private String stPolicyTypeID;
   private BigDecimal dbTreatyLimit0;
   private BigDecimal dbTreatyLimit1;
   private BigDecimal dbTreatyLimit2;
   private BigDecimal dbTreatyLimit3;
   private BigDecimal dbRate0;
   private BigDecimal dbRate1;
   private BigDecimal dbRate2;
   private BigDecimal dbRate3;
   private String stExcRiskFlag;
   private Date dtPeriodStartDate;
   private Date dtPeriodEndDate;
   private String stReference1;
   private String stActiveFlag;
   
    private String stPolicyTypeDesc;
    private String stInsuranceTreatyID;
    private String stRiskLevel;
   
   public Date getDtPeriodStartDate() {
      return dtPeriodStartDate;
   }

   public void setDtPeriodStartDate(Date dtPeriodStartDate) {
      this.dtPeriodStartDate = dtPeriodStartDate;
   }
   
   public Date getDtPeriodEndDate() {
      return dtPeriodEndDate;
   }

   public void setDtPeriodEndDate(Date dtPeriodEndDate) {
      this.dtPeriodEndDate = dtPeriodEndDate;
   }
   
   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public BigDecimal getDbRate0() {
      return dbRate0;
   }

   public void setDbRate0(BigDecimal dbRate0) {
      this.dbRate0 = dbRate0;
   }

   public BigDecimal getDbRate1() {
      return dbRate1;
   }

   public void setDbRate1(BigDecimal dbRate1) {
      this.dbRate1 = dbRate1;
   }

   public BigDecimal getDbRate2() {
      return dbRate2;
   }

   public void setDbRate2(BigDecimal dbRate2) {
      this.dbRate2 = dbRate2;
   }

   public BigDecimal getDbRate3() {
      return dbRate3;
   }

   public void setDbRate3(BigDecimal dbRate3) {
      this.dbRate3 = dbRate3;
   }

   public String getStInsuranceRiskCategoryID() {
      return stInsuranceRiskCategoryID;
   }

   public void setStInsuranceRiskCategoryID(String stInsuranceRiskCategoryID) {
      this.stInsuranceRiskCategoryID = stInsuranceRiskCategoryID;
   }

   public String getStInsuranceRiskCategoryCode() {
      return stInsuranceRiskCategoryCode;
   }

   public void setStInsuranceRiskCategoryCode(String stInsuranceRiskCategoryCode) {
      this.stInsuranceRiskCategoryCode = stInsuranceRiskCategoryCode;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }
    
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
        stPolicyTypeDesc = null;
    }

   public BigDecimal getDbTreatyLimit0() {
      return dbTreatyLimit0;
   }

   public void setDbTreatyLimit0(BigDecimal dbTreatyLimit0) {
      this.dbTreatyLimit0 = dbTreatyLimit0;
   }

   public BigDecimal getDbTreatyLimit1() {
      return dbTreatyLimit1;
   }

   public void setDbTreatyLimit1(BigDecimal dbTreatyLimit1) {
      this.dbTreatyLimit1 = dbTreatyLimit1;
   }

   public BigDecimal getDbTreatyLimit2() {
      return dbTreatyLimit2;
   }

   public void setDbTreatyLimit2(BigDecimal dbTreatyLimit2) {
      this.dbTreatyLimit2 = dbTreatyLimit2;
   }

   public BigDecimal getDbTreatyLimit3() {
      return dbTreatyLimit3;
   }

   public void setDbTreatyLimit3(BigDecimal dbTreatyLimit3) {
      this.dbTreatyLimit3 = dbTreatyLimit3;
   }

   public BigDecimal getRate(String stRiskClass) {
      if ("1".equalsIgnoreCase(stRiskClass)) return dbRate1;
      else if ("2".equalsIgnoreCase(stRiskClass)) return dbRate2;
      else if ("3".equalsIgnoreCase(stRiskClass)) return dbRate3;
      else return dbRate0;
   }
   
   public String getStExcRiskFlag() {
      return stExcRiskFlag;
   }

   public void setStExcRiskFlag(String stExcRiskFlag) {
      this.stExcRiskFlag = stExcRiskFlag;
   }
   
   public boolean isExcRiskFlag() {
      return Tools.isYes(stExcRiskFlag);
   }
   
   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }
   
   public boolean isActiveFlag() {
      return Tools.isYes(stActiveFlag);
   }
   
    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }
    
    public String getStPolicyTypeDesc() {
        if (stPolicyTypeID == null) return null;
        if (stPolicyTypeDesc == null) {
            final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
            if (ptype != null)
                stPolicyTypeDesc = ptype.getStDescription();
        }
        
        return stPolicyTypeDesc;
    }

    /**
     * @return the stInsuranceTreatyID
     */
    public String getStInsuranceTreatyID() {
        return stInsuranceTreatyID;
    }

    /**
     * @param stInsuranceTreatyID the stInsuranceTreatyID to set
     */
    public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
        this.stInsuranceTreatyID = stInsuranceTreatyID;
    }

    /**
     * @return the stRiskLevel
     */
    public String getStRiskLevel() {
        return stRiskLevel;
    }

    /**
     * @param stRiskLevel the stRiskLevel to set
     */
    public void setStRiskLevel(String stRiskLevel) {
        this.stRiskLevel = stRiskLevel;
    }
   
}

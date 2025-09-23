/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyCoinsView
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 2:09:31 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.webfin.entity.model.EntityView;
import com.crux.pool.DTOPool;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.util.BDUtil;

import java.math.BigDecimal;

import java.util.Date;

public class InsurancePolicyCoinsView extends DTO implements RecordAudit {

   /*
   CREATE TABLE ins_pol_coins
(
  ins_pol_coins_id int8 NOT NULL,
  policy_id int8,
  entity_id int8,
  share_pct numeric,
  amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_coins_pk PRIMARY KEY (ins_pol_coins_id)
ALTER TABLE ins_pol_coins ADD COLUMN premi_amt numeric;
ALTER TABLE ins_pol_coins ADD COLUMN position_code varchar(16);
ALTER TABLE ins_pol_coins ADD COLUMN f_entrybyrate varchar(1);


)
WITHOUT OIDS;
   */

   public static String tableName = "ins_pol_coins";

   public static String fieldMap[][] = {
      {"stInsurancePolicyCoinsID","ins_pol_coins_id*pk"},
      {"stPolicyID","policy_id"},
      {"stEntityID","entity_id"},
      {"dbSharePct","share_pct"},
      {"dbAmount","amount"},
      {"dbPremiAmount","premi_amt"},
      {"stPositionCode","position_code"},
      {"stFlagEntryByRate","f_entrybyrate"},
      {"stHoldingCompanyFlag","holding_company_flag"},
      {"dbHandlingFeeRate","hfee_rate"},
      {"dbHandlingFeeAmount","hfee_amount"},
      {"stAutoPremiFlag","f_auto_premi"},
      {"dbClaimAmount","claim_amt"},
      {"stAutoClaimAmount","f_auto_clmamt"},
      {"dbCommissionRate","comm_rate"},
      {"dbCommissionAmount","comm_amount"},
      {"dbDiscountRate","disc_rate"},
      {"dbDiscountAmount","disc_amount"},
      {"dbBrokerageRate","broker_rate"},
      {"dbBrokerageAmount","broker_amount"},
      {"dtPolicyDate","policy_date*n"},
      {"stInsuranceCoverID", "ins_cover_id"},
      {"stInsuranceCoverPolTypeID", "ins_cvpt_id"},
      {"stCoinsuranceType", "coins_type"},
      {"stManualPremiFlag","f_entry_premi"},
      {"stManualClaimFlag","f_entry_claim"},
      
   };

   /*
ALTER TABLE ins_pol_coins ADD COLUMN claim_amt numeric;

ALTER TABLE ins_pol_coins ADD COLUMN hfee_rate numeric;
ALTER TABLE ins_pol_coins ADD COLUMN hfee_amount numeric;
   */

   private BigDecimal dbClaimAmount;
   private BigDecimal dbHandlingFeeRate;
   private BigDecimal dbHandlingFeeAmount;
   private BigDecimal dbCommissionRate;
   private BigDecimal dbCommissionAmount;
   private BigDecimal dbDiscountRate;
   private BigDecimal dbDiscountAmount;
   private BigDecimal dbBrokerageRate;
   private BigDecimal dbBrokerageAmount;

   private String stAutoPremiFlag="Y";
   private String stInsurancePolicyCoinsID;
   private String stPolicyID;
   private String stEntityID;
   private String stPositionCode;
   private String stFlagEntryByRate;
   private String stHoldingCompanyFlag;
   private String stAutoClaimAmount;
   private BigDecimal dbSharePct;
   private BigDecimal dbPremiAmount;
   private BigDecimal dbAmount;
   private String stInsuranceCoverID;
   private String stInsuranceCoverPolTypeID;
   private String stCoinsuranceType;
   private String stManualPremiFlag;
   private String stManualClaimFlag;

   public String getStAutoClaimAmount() {
      return stAutoClaimAmount;
   }

   public void setStAutoClaimAmount(String stAutoClaimAmount) {
      this.stAutoClaimAmount = stAutoClaimAmount;
   }

   public BigDecimal getDbHandlingFeeRate() {
      return dbHandlingFeeRate;
   }

   public void setDbHandlingFeeRate(BigDecimal dbHandlingFeeRate) {
      this.dbHandlingFeeRate = dbHandlingFeeRate;
   }

   public BigDecimal getDbHandlingFeeAmount() {
      return dbHandlingFeeAmount;
   }

   public void setDbHandlingFeeAmount(BigDecimal dbHandlingFeeAmount) {
      this.dbHandlingFeeAmount = dbHandlingFeeAmount;
   }
   
   public BigDecimal getDbCommissionRate() {
      return dbCommissionRate;
   }

   public void setDbCommissionRate(BigDecimal dbCommissionRate) {
      this.dbCommissionRate = dbCommissionRate;
   }

   public BigDecimal getDbCommissionAmount() {
      return dbCommissionAmount;
   }

   public void setDbCommissionAmount(BigDecimal dbCommissionAmount) {
      this.dbCommissionAmount = dbCommissionAmount;
   }
   
   public BigDecimal getDbDiscountRate() {
      return dbDiscountRate;
   }

   public void setDbDiscountRate(BigDecimal dbDiscountRate) {
      this.dbDiscountRate = dbDiscountRate;
   }

   public BigDecimal getDbDiscountAmount() {
      return dbDiscountAmount;
   }

   public void setDbDiscountAmount(BigDecimal dbDiscountAmount) {
      this.dbDiscountAmount = dbDiscountAmount;
   }
   
   public BigDecimal getDbBrokerageRate() {
      return dbBrokerageRate;
   }

   public void setDbBrokerageRate(BigDecimal dbBrokerageRate) {
      this.dbBrokerageRate = dbBrokerageRate;
   }

   public BigDecimal getDbBrokerageAmount() {
      return dbBrokerageAmount;
   }

   public void setDbBrokerageAmount(BigDecimal dbBrokerageAmount) {
      this.dbBrokerageAmount = dbBrokerageAmount;
   }

   public String getStHoldingCompanyFlag() {
      return stHoldingCompanyFlag;
   }

   public void setStHoldingCompanyFlag(String stHoldingCompanyFlag) {
      this.stHoldingCompanyFlag = stHoldingCompanyFlag;
   }

   public String getStEntityName() {
      final EntityView ent = getEntity();
      if(ent==null) return null;
      return ent.getStEntityName();
   }

   public EntityView getEntity() {
      return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
   }

   public String getStInsurancePolicyCoinsID() {
      return stInsurancePolicyCoinsID;
   }

   public void setStInsurancePolicyCoinsID(String stInsurancePolicyCoinsID) {
      this.stInsurancePolicyCoinsID = stInsurancePolicyCoinsID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public BigDecimal getDbSharePct() {
      return dbSharePct;
   }

   public void setDbSharePct(BigDecimal dbSharePct) {
      this.dbSharePct = dbSharePct;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStPositionCode() {
      return stPositionCode;
   }

   public void setStPositionCode(String stPositionCode) {
      this.stPositionCode = stPositionCode;
   }

   public BigDecimal getDbPremiAmount() {
      return dbPremiAmount;
   }

   public void setDbPremiAmount(BigDecimal dbPremiAmount) {
      this.dbPremiAmount = dbPremiAmount;
   }

   public String getStFlagEntryByRate() {
      return stFlagEntryByRate;
   }

   public void setStFlagEntryByRate(String stFlagEntryByRate) {
      this.stFlagEntryByRate = stFlagEntryByRate;
   }

   public boolean isEntryByPctRate() {
      return Tools.isYes(stFlagEntryByRate);
   }

   public boolean isHoldingCompany() {
      return "Y".equalsIgnoreCase(stHoldingCompanyFlag);
   }

   public BigDecimal getDbPremiAmountNet() {
      return BDUtil.sub(getDbPremiAmount(), getDbHandlingFeeAmount());
   }

   public String getStAutoPremiFlag() {
      return stAutoPremiFlag;
   }

   public void setStAutoPremiFlag(String stAutoPremiFlag) {
      this.stAutoPremiFlag = stAutoPremiFlag;
   }

   public boolean isAutoPremi() {
      return Tools.isYes(stAutoPremiFlag);
   }

   public BigDecimal getDbPremiRatePct() {

      return BDUtil.getPctFromRate(BDUtil.div(getDbAmount(), getDbPremiAmountNet(),10));
   }

   public BigDecimal getDbClaimAmount() {
      return dbClaimAmount;
   }

   public void setDbClaimAmount(BigDecimal dbClaimAmount) {
      this.dbClaimAmount = dbClaimAmount;
   }
   
   private Date dtPolicyDate;
   
   public Date getDtPolicyDate() {
      return dtPolicyDate;
   }

   public void setDtPolicyDate(Date dtPolicyDate) {
      this.dtPolicyDate = dtPolicyDate;
   }

    public String getStInsuranceCoverID() {
        return stInsuranceCoverID;
    }

    public void setStInsuranceCoverID(String stInsuranceCoverID) {
        this.stInsuranceCoverID = stInsuranceCoverID;
    }

    public String getStInsuranceCoverPolTypeID() {
        return stInsuranceCoverPolTypeID;
    }

    public void setStInsuranceCoverPolTypeID(String stInsuranceCoverPolTypeID) {
        this.stInsuranceCoverPolTypeID = stInsuranceCoverPolTypeID;
    }

    public String getStCoinsuranceType() {
        return stCoinsuranceType;
    }

    public void setStCoinsuranceType(String stCoinsuranceType) {
        this.stCoinsuranceType = stCoinsuranceType;
    }
    
    public boolean isAutoClaimAmount() {
      return Tools.isYes(stAutoClaimAmount);
   }
    
    public boolean isCommission() {
        return Tools.compare(dbCommissionAmount,new BigDecimal(0))>0;
    }
    
    public boolean isBrokerage() {
        return Tools.compare(dbBrokerageAmount,new BigDecimal(0))>0;
    }
    
    public boolean isHandlingFee() {
        return Tools.compare(dbHandlingFeeAmount,new BigDecimal(0))>0;
    }

    public String getStManualPremiFlag() {
        return stManualPremiFlag;
    }

    public void setStManualPremiFlag(String stManualPremiFlag) {
        this.stManualPremiFlag = stManualPremiFlag;
    }
    
    public boolean isManualPremi() {
      return Tools.isYes(stManualPremiFlag);
   }

    /**
     * @return the stManualClaimFlag
     */
    public String getStManualClaimFlag() {
        return stManualClaimFlag;
    }

    /**
     * @param stManualClaimFlag the stManualClaimFlag to set
     */
    public void setStManualClaimFlag(String stManualClaimFlag) {
        this.stManualClaimFlag = stManualClaimFlag;
    }

    public boolean isManualClaim() {
      return Tools.isYes(stManualClaimFlag);
   }

    public boolean isAskrida() {
      return stEntityID.equalsIgnoreCase("1");
   }

    public boolean isORJiwa() {
      boolean isOR = false;

      if(stEntityID.equalsIgnoreCase("1")) isOR = true;
      else if(stEntityID.equalsIgnoreCase("2000")) isOR = true;
      else if(stEntityID.equalsIgnoreCase("2001")) isOR = true;

      return isOR;

   }

    public boolean isCommission2() {
        return Tools.isEqual(dbCommissionAmount, new BigDecimal(0));
    }

    public boolean isBrokerage2() {
        return Tools.isEqual(dbBrokerageAmount, new BigDecimal(0));
    }

    public boolean isHandlingFee2() {
        return Tools.isEqual(dbHandlingFeeAmount, new BigDecimal(0));
    }

}

/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyDeductibleView
 * Author:  Denny Mahendra
 * Created: Mar 8, 2006 5:10:08 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.ConvertUtil;
import com.crux.util.LOV;
import com.crux.lov.LOVManager;

import java.math.BigDecimal;

public class InsuranceSplitPolicyDeductibleView extends DTO implements RecordAudit {
    
   public static String tableName = "ins_split_pol_deduct";

   public static String fieldMap[][] = {
      {"stInsurancePolicyDeductibleID","ins_pol_deduct_id*pk*nd"},
      {"stDescription","description"},
      {"dbAmount","amount"},
      {"dbPct","percentage"},
      {"stDeductType","deduct_type"},
      {"dbAmountMin","amt_min"},
      {"dbAmountMax","amt_max"},
      {"stPolicyID","pol_id"},
      {"stCurrencyCode","ccy"},
      {"stInsuranceClaimCauseID","ins_clm_caus_id"},
      {"stInsurancePolicyObjectID","ins_pol_obj_id"},
      {"stDeductTypePer","deduct_type_per"},
      {"dbTimeExcess","time_excess"},
      {"stTimeExcessUnit","time_excess_unit"},
   };

   private String stInsurancePolicyDeductibleID;
   private String stDescription;
   private String stInsurancePolicyObjectID;
   private BigDecimal dbAmount;
   private BigDecimal dbPct;
   private String stDeductType;
   private String stPolicyID;
   private String stCurrencyCode;
   private String stInsuranceClaimCauseID;
   private BigDecimal dbAmountMin;
   private BigDecimal dbAmountMax;
   private String stDeductTypePer;
   private BigDecimal dbTimeExcess;
   private String stTimeExcessUnit;

   public String getStInsuranceClaimCauseID() {
      return stInsuranceClaimCauseID;
   }

   public void setStInsuranceClaimCauseID(String stInsuranceClaimCauseID) {
      this.stInsuranceClaimCauseID = stInsuranceClaimCauseID;
   }

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public String getStInsurancePolicyDeductibleID() {
      return stInsurancePolicyDeductibleID;
   }

   public void setStInsurancePolicyDeductibleID(String stInsurancePolicyDeductibleID) {
      this.stInsurancePolicyDeductibleID = stInsurancePolicyDeductibleID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public BigDecimal getDbPct() {
      return dbPct;
   }

   public void setDbPct(BigDecimal dbPct) {
      this.dbPct = dbPct;
   }

   public String getStDeductType() {
      return stDeductType;
   }

   public void setStDeductType(String stDeductType) {
      this.stDeductType = stDeductType;
   }

   public BigDecimal getDbAmountMin() {
      return dbAmountMin;
   }

   public void setDbAmountMin(BigDecimal dbAmountMin) {
      this.dbAmountMin = dbAmountMin;
   }

   public BigDecimal getDbAmountMax() {
      return dbAmountMax;
   }

   public void setDbAmountMax(BigDecimal dbAmountMax) {
      this.dbAmountMax = dbAmountMax;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStInsurancePolicyObjectID() {
      return stInsurancePolicyObjectID;
   }

   public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
      this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
   }

   public String getStClaimCauseDesc() {

      final InsuranceClaimCauseView cc = getClaimCause();

      if (cc==null) return "";

      return cc.getStDescription();
   }
   
   public InsuranceClaimCauseView getClaimCause() {
      return (InsuranceClaimCauseView) DTOPool.getInstance().getDTO(InsuranceClaimCauseView.class, stInsuranceClaimCauseID);
   }

   public String getStAutoDesc() {

      final StringBuffer sz = new StringBuffer();

      if (dbAmount!=null) sz.append(stCurrencyCode).append(' ').append(ConvertUtil.print(dbAmount, 2));
      if (dbPct!=null) sz.append(ConvertUtil.print(dbPct,2)).append(' ').append(LOVManager.getInstance().getDescription(stDeductType, "VS_DEDUCT" ));

      if (dbAmountMax!=null) sz.append(" Max ").append(stCurrencyCode).append(' ').append(ConvertUtil.print(dbAmountMax,2));
      if (dbAmountMin!=null) sz.append(" Min ").append(stCurrencyCode).append(' ').append(ConvertUtil.print(dbAmountMin,2));

      return sz.toString();
   }

   public String getStCombinedDescription() {

      final StringBuffer sz = new StringBuffer();

      sz.append(getStClaimCauseDesc());
      sz.append(":");

      if (getDbAmount()!=null) {
         sz.append(getStCurrencyCode()).append(" ").append(ConvertUtil.print(getDbAmount(),2)).append(" ");
      }

      if (getDbPct()!=null) {
         sz.append(ConvertUtil.print(getDbPct(),0)).append(" ");
      }

      if (getStDeductType()!=null) {
         sz.append(getStDeductTypeDesc()).append(" ");
      }

      if (getDbAmountMax()!=null) {
         sz.append("MAX ").append(getStCurrencyCode()).append(" ").append(ConvertUtil.print(getDbAmountMax(),2)).append(" ");
      }

      if (getDbAmountMin()!=null) {
         sz.append("MIN ").append(getStCurrencyCode()).append(" ").append(ConvertUtil.print(getDbAmountMin(),2)).append(" ");
      }

      return sz.toString();
   }

   private String getStDeductTypeDesc() {
      if (getStDeductType()==null) return null;

      final LOV vsded = LOVManager.getInstance().getLOV("VS_DEDUCT", null);

      return vsded.getComboDesc(getStDeductType());
   }
   
   public String getStDeductTypePer() {
      return stDeductTypePer;
   }

   public void setStDeductTypePer(String stDeductTypePer) {
      this.stDeductTypePer = stDeductTypePer;
   }

    /**
     * @return the dbTimeExcess
     */
    public BigDecimal getDbTimeExcess() {
        return dbTimeExcess;
    }

    /**
     * @param dbTimeExcess the dbTimeExcess to set
     */
    public void setDbTimeExcess(BigDecimal dbTimeExcess) {
        this.dbTimeExcess = dbTimeExcess;
    }

    /**
     * @return the stTimeExcessUnit
     */
    public String getStTimeExcessUnit() {
        return stTimeExcessUnit;
    }

    /**
     * @param stTimeExcessUnit the stTimeExcessUnit to set
     */
    public void setStTimeExcessUnit(String stTimeExcessUnit) {
        this.stTimeExcessUnit = stTimeExcessUnit;
    }
}

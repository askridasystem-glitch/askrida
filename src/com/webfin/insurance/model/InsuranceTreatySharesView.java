/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTreatySharesView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 12:51:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.math.BigDecimal;

public class InsuranceTreatySharesView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_treaty_shares
(
  ins_treaty_shares_id int8 NOT NULL,
  ins_treaty_detail_id int8,
  member_ent_id int8,
  sharepct numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_shares_pk PRIMARY KEY (ins_treaty_shares_id)
) without oids;
   */

   public static String tableName = "ins_treaty_shares";

   public static String fieldMap[][] = {
      {"stInsuranceTreatySharesID","ins_treaty_shares_id*pk"},
      {"stInsuranceTreatyDetailID","ins_treaty_detail_id"},
      {"stMemberEntityID","member_ent_id"},
      {"dbSharePct","sharepct"},
      {"dbPremiRate","premi_rate"},
      {"dbPremiAmount","premi_amount"},
      {"stAutoRateFlag","auto_rate_flag"},
      {"stUseRateFlag","use_rate_flag"},
      {"dbRICommRate","ricomm_rate"},
      {"dbRICommAmount","ricomm_amt"},
      {"stNotes","notes"},
      {"dbTSIAmount","tsi_amount"},
      {"stControlFlags","control_flags"},
      {"dbRICommCaptiveRate","ricomm_captive_rate"},
      {"dbRICommONRRate","ricomm_onr_rate"},
      {"stRISlipNoFormat","ri_slip_no"},
      {"dbMaxCommissionPct","max_comm_pct"},
      {"dbDiscountPct","discount_pct"},
      {"stMaxCommissionFlags","max_comm_flag"},
      {"dbRICommMoreThanMaxCommRate","ricomm_more_maxcomm_rate"},
      {"stReinsuranceEntityID","reins_ent_id"},
      {"stPremiExcessFlag","premi_excess_flag"},
      {"stPaidToEntityID","ent_paid_to"},
   };

   private BigDecimal dbPremiRate;
   private BigDecimal dbPremiAmount;
   private BigDecimal dbTSIAmount;
   private String stAutoRateFlag;
   private String stUseRateFlag;
   private BigDecimal dbRICommRate;
   private BigDecimal dbRICommAmount;
   private String stNotes;
   private String stControlFlags;

   private String stInsuranceTreatySharesID;
   private String stInsuranceTreatyDetailID;
   private String stMemberEntityID;
   private BigDecimal dbSharePct;
   private BigDecimal dbRICommCaptiveRate;
   private BigDecimal dbRICommONRRate;
   private String stRISlipNoFormat;
   private BigDecimal dbMaxCommissionPct;
   private BigDecimal dbDiscountPct;
   private String stMaxCommissionFlags;
   private BigDecimal dbRICommMoreThanMaxCommRate;
   private String stReinsuranceEntityID;
   private String stPremiExcessFlag;
   private String stPaidToEntityID;

    public String getStPaidToEntityID() {
        return stPaidToEntityID;
    }

    public void setStPaidToEntityID(String stPaidToEntityID) {
        this.stPaidToEntityID = stPaidToEntityID;
    }

    public String getStPremiExcessFlag() {
        return stPremiExcessFlag;
    }

    public void setStPremiExcessFlag(String stPremiExcessFlag) {
        this.stPremiExcessFlag = stPremiExcessFlag;
    }

    public String getStReinsuranceEntityID() {
        return stReinsuranceEntityID;
    }

    public void setStReinsuranceEntityID(String stReinsuranceEntityID) {
        this.stReinsuranceEntityID = stReinsuranceEntityID;
    }

    public BigDecimal getDbRICommMoreThanMaxCommRate() {
        return dbRICommMoreThanMaxCommRate;
    }

    public void setDbRICommMoreThanMaxCommRate(BigDecimal dbRICommMoreThanMaxCommRate) {
        this.dbRICommMoreThanMaxCommRate = dbRICommMoreThanMaxCommRate;
    }

   public BigDecimal getDbTSIAmount() {
      return dbTSIAmount;
   }

   public void setDbTSIAmount(BigDecimal dbTSIAmount) {
      this.dbTSIAmount = dbTSIAmount;
   }

   public BigDecimal getDbPremiRate() {
      return dbPremiRate;
   }

   public void setDbPremiRate(BigDecimal dbPremiRate) {
      this.dbPremiRate = dbPremiRate;
   }

   public BigDecimal getDbPremiAmount() {
      return dbPremiAmount;
   }

   public void setDbPremiAmount(BigDecimal dbPremiAmount) {
      this.dbPremiAmount = dbPremiAmount;
   }

   public String getStAutoRateFlag() {
      return stAutoRateFlag;
   }

   public void setStAutoRateFlag(String stAutoRateFlag) {
      this.stAutoRateFlag = stAutoRateFlag;
   }

   public String getStUseRateFlag() {
      return stUseRateFlag;
   }

   public void setStUseRateFlag(String stUseRateFlag) {
      this.stUseRateFlag = stUseRateFlag;
   }

   public BigDecimal getDbRICommRate() {
      return dbRICommRate;
   }

   public void setDbRICommRate(BigDecimal dbRICommRate) {
      this.dbRICommRate = dbRICommRate;
   }

   public BigDecimal getDbRICommAmount() {
      return dbRICommAmount;
   }

   public void setDbRICommAmount(BigDecimal dbRICommAmount) {
      this.dbRICommAmount = dbRICommAmount;
   }

   public String getStNotes() {
      return stNotes;
   }

   public void setStNotes(String stNotes) {
      this.stNotes = stNotes;
   }

   public String getStInsuranceTreatySharesID() {
      return stInsuranceTreatySharesID;
   }

   public void setStInsuranceTreatySharesID(String stInsuranceTreatySharesID) {
      this.stInsuranceTreatySharesID = stInsuranceTreatySharesID;
   }

   public String getStInsuranceTreatyDetailID() {
      return stInsuranceTreatyDetailID;
   }

   public void setStInsuranceTreatyDetailID(String stInsuranceTreatyDetailID) {
      this.stInsuranceTreatyDetailID = stInsuranceTreatyDetailID;
   }

   public String getStMemberEntityID() {
      return stMemberEntityID;
   }

   public void setStMemberEntityID(String stMemberEntityID) {
      this.stMemberEntityID = stMemberEntityID;
   }

   public BigDecimal getDbSharePct() {
      return dbSharePct;
   }

   public void setDbSharePct(BigDecimal dbSharePct) {
      this.dbSharePct = dbSharePct;
   }

   public String getStControlFlags() {
      return stControlFlags;
   }

   public void setStControlFlags(String stControlFlags) {
      this.stControlFlags = stControlFlags;
   }
   
   public BigDecimal getDbRICommCaptiveRate() {
      return dbRICommCaptiveRate;
   }

   public void setDbRICommCaptiveRate(BigDecimal dbRICommCaptiveRate) {
      this.dbRICommCaptiveRate = dbRICommCaptiveRate;
   }

    /**
     * @return the dbRICommONRRate
     */
    public BigDecimal getDbRICommONRRate() {
        return dbRICommONRRate;
    }

    /**
     * @param dbRICommONRRate the dbRICommONRRate to set
     */
    public void setDbRICommONRRate(BigDecimal dbRICommONRRate) {
        this.dbRICommONRRate = dbRICommONRRate;
    }

    /**
     * @return the stRISlipNoFormat
     */
    public String getStRISlipNoFormat() {
        return stRISlipNoFormat;
    }

    /**
     * @param stRISlipNoFormat the stRISlipNoFormat to set
     */
    public void setStRISlipNoFormat(String stRISlipNoFormat) {
        this.stRISlipNoFormat = stRISlipNoFormat;
    }

    /**
     * @return the dbMaxCommissionPct
     */
    public BigDecimal getDbMaxCommissionPct() {
        return dbMaxCommissionPct;
    }

    /**
     * @param dbMaxCommissionPct the dbMaxCommissionPct to set
     */
    public void setDbMaxCommissionPct(BigDecimal dbMaxCommissionPct) {
        this.dbMaxCommissionPct = dbMaxCommissionPct;
    }

    /**
     * @return the dbDiscountPct
     */
    public BigDecimal getDbDiscountPct() {
        return dbDiscountPct;
    }

    /**
     * @param dbDiscountPct the dbDiscountPct to set
     */
    public void setDbDiscountPct(BigDecimal dbDiscountPct) {
        this.dbDiscountPct = dbDiscountPct;
    }

    /**
     * @return the stMaxCommissionFlags
     */
    public String getStMaxCommissionFlags() {
        return stMaxCommissionFlags;
    }

    /**
     * @param stMaxCommissionFlags the stMaxCommissionFlags to set
     */
    public void setStMaxCommissionFlags(String stMaxCommissionFlags) {
        this.stMaxCommissionFlags = stMaxCommissionFlags;
    }

    public boolean isMaxCommissionFlags(){
        return Tools.isYes(stMaxCommissionFlags);
    }

    public boolean isPremiExcessFlag(){
        return Tools.isYes(stPremiExcessFlag);
    }
   
}

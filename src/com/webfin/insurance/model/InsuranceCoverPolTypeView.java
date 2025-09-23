/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceCoverPolTypeView
 * Author:  Denny Mahendra
 * Created: Feb 23, 2006 5:30:36 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.webfin.FinCodec;
import java.math.BigDecimal;

public class InsuranceCoverPolTypeView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_cover_poltype
(
  ins_cvpt_id int8 NOT NULL,
  ins_cover_id int8,
  pol_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  cover_category varchar(10),
  pol_subtype_id int8,
  CONSTRAINT ins_cover_poltype_pk PRIMARY KEY (ins_cvpt_id)
)
   */

   private String stInsuranceCoverPolTypeID;
   private String stInsuranceCoverID;
   private String stPolicyTypeID;
   private String stCoverCategory;
   private String stPolicySubTypeID;
   private String stDescription;

   public static String tableName = "ins_cover_poltype";

   public static String fieldMap[][] = {
      {"stInsuranceCoverPolTypeID","ins_cvpt_id*pk"},
      {"stInsuranceCoverID","ins_cover_id"},
      {"stPolicyTypeID","pol_type_id"},
      {"stCoverCategory","cover_category"},
      {"stPolicySubTypeID","pol_subtype_id"},
      {"stDescription","description*n"},
      {"stUseRateFlag","use_rate_flag"},
      {"stUseRateLockFlag","use_rate_lock_flag"},
      {"stAutoRateFlag","auto_rate_flag"},
      {"stAutoRateLockFlag","auto_rate_lock_flag"},
      {"stDefaultCoverFlag","default_cover_flag"},
      {"stManualTSIFlag","manual_tsi_flag"},
      {"stManualTSILockFlag","manual_tsi_lock_flag"},
      {"stDefaultCoverFlag2","default_cover_flag2"},
      {"stManualTSIFlag2","manual_tsi_flag2"},
      {"stManualTSILockFlag2","manual_tsi_lock_flag2"},
      {"dbTSIFactor1","tsi_factor1"},
      {"stActiveFlag","active_flag"},
   };

   private String stUseRateFlag;
   private String stUseRateLockFlag;
   private String stAutoRateFlag;
   private String stAutoRateLockFlag;
   private String stManualTSIFlag;
   private String stManualTSILockFlag;
   private String stDefaultCoverFlag;
   private String stManualTSIFlag2;
   private String stManualTSILockFlag2;
   private String stDefaultCoverFlag2;
   private BigDecimal dbTSIFactor1;
   private String stActiveFlag;


   public static String comboFields[] = {"ins_cvpt_id","description"};

   public String getStUseRateFlag() {
      return stUseRateFlag;
   }

   public void setStUseRateFlag(String stUseRateFlag) {
      this.stUseRateFlag = stUseRateFlag;
   }

   public String getStUseRateLockFlag() {
      return stUseRateLockFlag;
   }

   public void setStUseRateLockFlag(String stUseRateLockFlag) {
      this.stUseRateLockFlag = stUseRateLockFlag;
   }

   public String getStAutoRateFlag() {
      return stAutoRateFlag;
   }

   public void setStAutoRateFlag(String stAutoRateFlag) {
      this.stAutoRateFlag = stAutoRateFlag;
   }

   public String getStAutoRateLockFlag() {
      return stAutoRateLockFlag;
   }

   public void setStAutoRateLockFlag(String stAutoRateLockFlag) {
      this.stAutoRateLockFlag = stAutoRateLockFlag;
   }

   public String getStManualTSIFlag() {
      return stManualTSIFlag;
   }

   public void setStManualTSIFlag(String stManualTSIFlag) {
      this.stManualTSIFlag = stManualTSIFlag;
   }

   public String getStManualTSILockFlag() {
      return stManualTSILockFlag;
   }

   public void setStManualTSILockFlag(String stManualTSILockFlag) {
      this.stManualTSILockFlag = stManualTSILockFlag;
   }

   public String getStDefaultCoverFlag() {
      return stDefaultCoverFlag;
   }

   public void setStDefaultCoverFlag(String stDefaultCoverFlag) {
      this.stDefaultCoverFlag = stDefaultCoverFlag;
   }

   public String getStInsuranceCoverPolTypeID() {
      return stInsuranceCoverPolTypeID;
   }

   public void setStInsuranceCoverPolTypeID(String stInsuranceCoverPolTypeID) {
      this.stInsuranceCoverPolTypeID = stInsuranceCoverPolTypeID;
   }

   public String getStInsuranceCoverID() {
      return stInsuranceCoverID;
   }

   public void setStInsuranceCoverID(String stInsuranceCoverID) {
      this.stInsuranceCoverID = stInsuranceCoverID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStCoverCategory() {
      return stCoverCategory;
   }

   public void setStCoverCategory(String stCoverCategory) {
      this.stCoverCategory = stCoverCategory;
   }

   public String getStPolicySubTypeID() {
      return stPolicySubTypeID;
   }

   public void setStPolicySubTypeID(String stPolicySubTypeID) {
      this.stPolicySubTypeID = stPolicySubTypeID;
   }

   public boolean isMainCover() {
      return FinCodec.CoverCategory.MAIN.equalsIgnoreCase(stCoverCategory);
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

    public String getStManualTSIFlag2() {
        return stManualTSIFlag2;
    }

    public void setStManualTSIFlag2(String stManualTSIFlag2) {
        this.stManualTSIFlag2 = stManualTSIFlag2;
    }

    public String getStManualTSILockFlag2() {
        return stManualTSILockFlag2;
    }

    public void setStManualTSILockFlag2(String stManualTSILockFlag2) {
        this.stManualTSILockFlag2 = stManualTSILockFlag2;
    }

    public String getStDefaultCoverFlag2() {
        return stDefaultCoverFlag2;
    }

    public void setStDefaultCoverFlag2(String stDefaultCoverFlag2) {
        this.stDefaultCoverFlag2 = stDefaultCoverFlag2;
    }

    /**
     * @return the dbTSIFactor1
     */
    public BigDecimal getDbTSIFactor1() {
        return dbTSIFactor1;
    }

    /**
     * @param dbTSIFactor1 the dbTSIFactor1 to set
     */
    public void setDbTSIFactor1(BigDecimal dbTSIFactor1) {
        this.dbTSIFactor1 = dbTSIFactor1;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public boolean isMainCoverage() {
      return "MAIN".equalsIgnoreCase(stCoverCategory);
   }

}

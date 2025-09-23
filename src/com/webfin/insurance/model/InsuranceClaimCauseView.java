/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceClaimCauseView
 * Author:  Denny Mahendra
 * Created: May 21, 2006 4:42:44 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

public class InsuranceClaimCauseView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_clm_cause
(
  ins_clm_caus_id int8 NOT NULL,
  cause_desc varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  exc_claim_flag
  CONSTRAINT ins_clm_cause_pk PRIMARY KEY (ins_clm_caus_id)
)
WITHO
   */

   public static String tableName = "ins_clm_cause";
   public static String fieldMap[][] = {
      {"stInsuranceClaimCauseID","ins_clm_caus_id*pk"},
      {"stDescription","cause_desc"},
      {"stExcClaimFlag","exc_claim_flag"},
      {"stPolicyTypeID","pol_type_id"},
      {"stDefaultFlag","default_flag"},
      {"stActiveFlag","active_flag"},
      {"stSubCauseFlag","sub_cause_flag"},
      {"stSubrogationDefaultFlag","subrogation_default_flag"},

   };

   private String stInsuranceClaimCauseID;
   private String stDescription;
   private String stExcClaimFlag;
   private String stPolicyTypeID;
   private String stDefaultFlag;
   private String stActiveFlag;
   private String stSubCauseFlag;
   private String stSubrogationDefaultFlag;

    public String getStSubrogationDefaultFlag() {
        return stSubrogationDefaultFlag;
    }

    public void setStSubrogationDefaultFlag(String stSubrogationDefaultFlag) {
        this.stSubrogationDefaultFlag = stSubrogationDefaultFlag;
    }

    public String getStSubCauseFlag() {
        return stSubCauseFlag;
    }

    public void setStSubCauseFlag(String stSubCauseFlag) {
        this.stSubCauseFlag = stSubCauseFlag;
    }

   public static String comboFields[] = {"ins_clm_caus_id","cause_desc"};

   public String getStInsuranceClaimCauseID() {
      return stInsuranceClaimCauseID;
   }

   public void setStInsuranceClaimCauseID(String stInsuranceClaimCauseID) {
      this.stInsuranceClaimCauseID = stInsuranceClaimCauseID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public String getStExcClaimFlag() {
      return stExcClaimFlag;
   }

   public void setStExcClaimFlag(String stExcClaimFlag) {
      this.stExcClaimFlag = stExcClaimFlag;
   }

    public String getStPolicyTypeID()
    {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID)
    {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public String getStDefaultFlag()
    {
        return stDefaultFlag;
    }

    public void setStDefaultFlag(String stDefaultFlag)
    {
        this.stDefaultFlag = stDefaultFlag;
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

    public boolean isActive(){
        return Tools.isYes(stActiveFlag);
    }

    public boolean isHaveSubCause(){
        return Tools.isYes(stSubCauseFlag);
    }

    public boolean isSubrogationDefault(){
        return Tools.isYes(stSubrogationDefaultFlag);
    }

}

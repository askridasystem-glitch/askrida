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

public class InsuranceClaimSubCauseView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_clm_sub_cause
(
  ins_clm_sub_cause_id bigint NOT NULL,
  ins_clm_caus_id bigint NOT NULL,
  cause_desc character varying(255),
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  exc_claim_flag character(1),
  pol_type_id bigint,
  default_flag character varying(1),
  active_flag character varying(1),
  CONSTRAINT ins_clm_sub_cause_pkey PRIMARY KEY (ins_clm_sub_cause_id)
)
   */

   public static String tableName = "ins_clm_sub_cause";
   public static String fieldMap[][] = {
      {"stInsuranceClaimSubCauseID","ins_clm_sub_cause_id*pk"},
      {"stInsuranceClaimCauseID","ins_clm_caus_id"},
      {"stCauseDescription","cause_desc"},
      {"stPolicyTypeID","pol_type_id"},
      {"stDefaultFlag","default_flag"},
      {"stActiveFlag","active_flag"},

   };

   private String stInsuranceClaimSubCauseID;
   private String stInsuranceClaimCauseID;
   private String stCauseDescription;
   private String stPolicyTypeID;
   private String stDefaultFlag;

    public String getStCauseDescription() {
        return stCauseDescription;
    }

    public void setStCauseDescription(String stCauseDescription) {
        this.stCauseDescription = stCauseDescription;
    }

    public String getStInsuranceClaimSubCauseID() {
        return stInsuranceClaimSubCauseID;
    }

    public void setStInsuranceClaimSubCauseID(String stInsuranceClaimSubCauseID) {
        this.stInsuranceClaimSubCauseID = stInsuranceClaimSubCauseID;
    }
   private String stActiveFlag;

   //public static String comboFields[] = {"ins_clm_caus_id","cause_desc"};

   public String getStInsuranceClaimCauseID() {
      return stInsuranceClaimCauseID;
   }

   public void setStInsuranceClaimCauseID(String stInsuranceClaimCauseID) {
      this.stInsuranceClaimCauseID = stInsuranceClaimCauseID;
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

    public boolean isDefault(){
        return Tools.isYes(stDefaultFlag);
    }

}

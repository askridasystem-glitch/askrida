/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceClaimLossView
 * Author:  Denny Mahendra
 * Created: May 21, 2006 4:42:44 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.util.Date;

public class InsuranceClaimLossView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_claim_loss
(
  claim_loss_id bigint NOT NULL,
  claim_loss_code character varying(20),
  loss_desc character varying(255),
  create_date timestamp without time zone,
  create_who character varying(25),
  change_date timestamp without time zone,
  change_who character varying(30),
  repeated_claim_flag character varying(1),
  pol_type_id bigint
)
   */

   public static String tableName = "ins_claim_loss";

   public static String fieldMap[][] = {
      {"stInsuranceClaimLossID","claim_loss_id*pk"},
      {"stClaimLossCode","claim_loss_code"},
      {"stLossDesc","loss_desc"},
      {"dtCreateDate","create_date"},
      {"stCreateWho","create_who"},
      {"dtChangeDate","change_date"},
      {"stChangeWho","change_who"},
      {"stRepeatedClaimFlag","repeated_claim_flag"},
      {"stPolTypeID","pol_type_id"},
   };

   private String stInsuranceClaimLossID;
   private String stClaimLossCode;
   private String stLossDesc;
   private Date dtCreateDate;
   private String stCreateWho;
   private Date dtChangeDate;
   private String stChangeWho;
   private String stRepeatedClaimFlag;
   private String stPolTypeID;

   public String getStInsuranceClaimLossID() {
      return stInsuranceClaimLossID;
   }

   public void setStInsuranceClaimLossID(String stInsuranceClaimLossID) {
      this.stInsuranceClaimLossID = stInsuranceClaimLossID;
   }
   
   public String getStClaimLossCode() {
      return stClaimLossCode;
   }

   public void setStClaimLossCode(String stClaimLossCode) {
      this.stClaimLossCode = stClaimLossCode;
   }
   
   public String getStLossDesc() {
      return stLossDesc;
   }

   public void setStLossDesc(String stLossDesc) {
      this.stLossDesc = stLossDesc;
   }
   
   public String getStCreateWho() {
      return stCreateWho;
   }

   public void setStCreateWho(String stCreateWho) {
      this.stCreateWho = stCreateWho;
   }
   
   public String getStChangeWho() {
      return stChangeWho;
   }

   public void setStChangeWho(String stChangeWho) {
      this.stChangeWho = stChangeWho;
   }
   
   public String getStRepeatedClaimFlag() {
      return stRepeatedClaimFlag;
   }

   public void setStRepeatedClaimFlag(String stRepeatedClaimFlag) {
      this.stRepeatedClaimFlag = stRepeatedClaimFlag;
   }
   
   public String getStPolTypeID() {
      return stPolTypeID;
   }

   public void setStPolTypeID(String stPolTypeID) {
      this.stPolTypeID = stPolTypeID;
   }
   
   public Date getDtCreateDate() {
      return dtCreateDate;
   }

   public void setDtCreateDate(Date dtCreateDate) {
      this.dtCreateDate = dtCreateDate;
   }
   
   public Date getDtChangeDate() {
      return dtChangeDate;
   }

   public void setDtChangeDate(Date dtChangeDate) {
      this.dtChangeDate = dtChangeDate;
   }

   public boolean isPartialLossNoVoid(){
       return stClaimLossCode.equalsIgnoreCase("PART_NO_VOID")?true:false;
   }
}

/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyNomeratorView
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 11:32:25 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsurancePolicyNomeratorView extends DTO implements RecordAudit {

   /*CREATE TABLE ins_pol_nomerator
(
  ins_pol_nomerator_id varchar(16) NOT NULL,
  cc_code int8,
  policy_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_nomerator_pk PRIMARY KEY (ins_pol_nomerator_id)
) without oids;*/

   public static String tableName = "ins_pol_nomerator";

   public static String fieldMap[][] = {
      {"stNomeratorID","ins_pol_nomerator_id*pk"},
      {"stCCCode","cc_code"},
      {"stPolicyID","policy_id"},
   };

   private String stNomeratorID;
   private String stCCCode;
   private String stPolicyID;

   public String getStNomeratorID() {
      return stNomeratorID;
   }

   public void setStNomeratorID(String stNomeratorID) {
      this.stNomeratorID = stNomeratorID;
   }

   public String getStCCCode() {
      return stCCCode;
   }

   public void setStCCCode(String stCCCode) {
      this.stCCCode = stCCCode;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }
}

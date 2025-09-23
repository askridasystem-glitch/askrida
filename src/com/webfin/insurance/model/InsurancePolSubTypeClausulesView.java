/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolSubTypeClausulesView
 * Author:  Denny Mahendra
 * Created: Nov 5, 2005 10:51:42 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class InsurancePolSubTypeClausulesView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_polsubtype_clause
(
  pol_subtype_id int8 NOT NULL,
  ins_clause_id int8 NOT NULL,
  rate numeric,
  rate_type varchar(16),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  select_type varchar(16),
  CONSTRAINT policy_subtype_clausules_pk PRIMARY KEY (pol_subtype_id, ins_clause_id)
)
   */

   public static String tableName = "ins_polsubtype_clause";

   public static String fieldMap[][] = {
      {"stPolicySubtypeID","pol_subtype_id*pk"},
      {"stInsuranceClauseID","ins_clause_id"},
      {"dbRate","rate"},
      {"stRateType","rate_type"},
      {"stSelectType","select_type"},
   };


   private String stPolicySubtypeID;
   private String stInsuranceClauseID;
   private BigDecimal dbRate;
   private String stRateType;
   private String stSelectType;

   public String getStPolicySubtypeID() {
      return stPolicySubtypeID;
   }

   public void setStPolicySubtypeID(String stPolicySubtypeID) {
      this.stPolicySubtypeID = stPolicySubtypeID;
   }

   public String getStInsuranceClauseID() {
      return stInsuranceClauseID;
   }

   public void setStInsuranceClauseID(String stInsuranceClauseID) {
      this.stInsuranceClauseID = stInsuranceClauseID;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public String getStRateType() {
      return stRateType;
   }

   public void setStRateType(String stRateType) {
      this.stRateType = stRateType;
   }

   public String getStSelectType() {
      return stSelectType;
   }

   public void setStSelectType(String stSelectType) {
      this.stSelectType = stSelectType;
   }

}

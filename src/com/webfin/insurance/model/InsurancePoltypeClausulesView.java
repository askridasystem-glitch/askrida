/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePoltypeClausulesView
 * Author:  Denny Mahendra
 * Created: Nov 2, 2005 8:37:38 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.RecordAudit;
import com.crux.common.model.DTO;

import java.math.BigDecimal;

public class InsurancePoltypeClausulesView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_poltype_clausules
(
  pol_type_id int8 NOT NULL,
  ins_clause_id int8,
  rate numeric,
  rate_type varchar(3),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_poltype_clausules_pk PRIMARY KEY (pol_type_id)
)
   */

   public static String tableName = "ins_poltype_clausules";

   public static String fieldMap[][] = {
      {"stPolicyTypeID","pol_type_id*pk"},
      {"stInsuranceClauseID","ins_clause_id"},
      {"dbRate","rate"},
      {"stRateType","rate_type"},
   };

   private String stPolicyTypeID;
   private String stInsuranceClauseID;
   private BigDecimal dbRate;
   private String stRateType;

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
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
}

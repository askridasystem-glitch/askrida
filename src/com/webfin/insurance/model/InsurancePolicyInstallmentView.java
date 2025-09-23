/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyInstallmentView
 * Author:  Denny Mahendra
 * Created: Apr 23, 2006 1:39:23 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.util.Date;
import java.math.BigDecimal;

public class InsurancePolicyInstallmentView extends DTO implements RecordAudit{
   
   public static String tableName = "ins_pol_installment";

   public static String fieldMap[][] = {
      {"stInsurancePolicyInstallmentID","ins_pol_inst_id*pk"},
      {"stPolicyID","policy_id"},
      {"dbAmount","amount"},
      {"dtDueDate","inst_date"},
   };
   
   private String stInsurancePolicyInstallmentID;
   private Date dtDueDate;
   private BigDecimal dbAmount;
   private String stPolicyID;
   
   
   public String getStInsurancePolicyInstallmentID() {
      return stInsurancePolicyInstallmentID;
   }

   public void setStInsurancePolicyInstallmentID(String stInsurancePolicyInstallmentID) {
      this.stInsurancePolicyInstallmentID = stInsurancePolicyInstallmentID;
   }
       
   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }


   public Date getDtDueDate() {
      return dtDueDate;
   }

   public void setDtDueDate(Date dtDueDate) {
      this.dtDueDate = dtDueDate;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }
}

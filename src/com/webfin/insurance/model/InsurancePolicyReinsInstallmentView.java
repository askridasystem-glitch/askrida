/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyInstallmentView
 * Author:  Ahmad Rodhoni
 * Created: Juli 2019
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.util.Date;
import java.math.BigDecimal;

public class InsurancePolicyReinsInstallmentView extends DTO implements RecordAudit{
   
   public static String tableName = "ins_pol_ri_installment";

   public static String fieldMap[][] = {
      {"stInsurancePolicyReinsInstallmentID","ins_pol_ri_inst_id*pk*nd"},
      {"stInsurancePolicyReinsID","ins_pol_ri_id"},
      {"dbAmount","amount"},
      {"dtDueDate","inst_date"},
      {"dbPremiAmount","premi_amount"},
      {"dbRICommAmount","ricomm_amt"},
      {"stManualFlag","manual_f"},
      {"stInstallmentNumber","installment_no"},
   };
   
   private String stInsurancePolicyReinsInstallmentID;
   private Date dtDueDate;
   private BigDecimal dbAmount;
   private String stInsurancePolicyReinsID;
   private BigDecimal dbPremiAmount;
   private BigDecimal dbRICommAmount;
   private String stManualFlag;
   private String stInstallmentNumber;

    public String getStInstallmentNumber() {
        return stInstallmentNumber;
    }

    public void setStInstallmentNumber(String stInstallmentNumber) {
        this.stInstallmentNumber = stInstallmentNumber;
    }

   public boolean isManualCicilan(){
       return Tools.isYes(stManualFlag);
   }

    public String getStManualFlag() {
        return stManualFlag;
    }

    public void setStManualFlag(String stManualFlag) {
        this.stManualFlag = stManualFlag;
    }

    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    public BigDecimal getDbPremiAmount() {
        return dbPremiAmount;
    }

    public void setDbPremiAmount(BigDecimal dbPremiAmount) {
        this.dbPremiAmount = dbPremiAmount;
    }

    public BigDecimal getDbRICommAmount() {
        return dbRICommAmount;
    }

    public void setDbRICommAmount(BigDecimal dbRICommAmount) {
        this.dbRICommAmount = dbRICommAmount;
    }

    public Date getDtDueDate() {
        return dtDueDate;
    }

    public void setDtDueDate(Date dtDueDate) {
        this.dtDueDate = dtDueDate;
    }

    public String getStInsurancePolicyReinsID() {
        return stInsurancePolicyReinsID;
    }

    public void setStInsurancePolicyReinsID(String stInsurancePolicyReinsID) {
        this.stInsurancePolicyReinsID = stInsurancePolicyReinsID;
    }

    public String getStInsurancePolicyReinsInstallmentID() {
        return stInsurancePolicyReinsInstallmentID;
    }

    public void setStInsurancePolicyReinsInstallmentID(String stInsurancePolicyReinsInstallmentID) {
        this.stInsurancePolicyReinsInstallmentID = stInsurancePolicyReinsInstallmentID;
    }

}

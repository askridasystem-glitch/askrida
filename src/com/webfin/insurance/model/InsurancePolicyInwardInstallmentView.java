/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyInstallmentView
 * Author:  Ahmad Rodhoni
 * Created: Juli 2019
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.util.Date;
import java.math.BigDecimal;

public class InsurancePolicyInwardInstallmentView extends DTO implements RecordAudit{
   
   public static String tableName = "ins_pol_inward_installment";

   public static String fieldMap[][] = {
      {"stInsurancePolicyInwardInstallmentID","ins_pol_inward_inst_id*pk"},
      {"stARInvoiceID","ar_invoice_id"},
      {"dbAmount","amount"},
      {"dtDueDate","inst_date"},
      {"dbPremiBruto","premi_bruto"},
      {"dbKomisi","komisi"},
      {"dbFee","fee"},
   };
   
   private String stInsurancePolicyInwardInstallmentID;
   private Date dtDueDate;
   private BigDecimal dbAmount;
   private String stARInvoiceID;
   private BigDecimal dbPremiBruto;
   private BigDecimal dbKomisi;
   private BigDecimal dbFee;

    public BigDecimal getDbFee() {
        return dbFee;
    }

    public void setDbFee(BigDecimal dbFee) {
        this.dbFee = dbFee;
    }

    public BigDecimal getDbKomisi() {
        return dbKomisi;
    }

    public void setDbKomisi(BigDecimal dbKomisi) {
        this.dbKomisi = dbKomisi;
    }

    public BigDecimal getDbPremiBruto() {
        return dbPremiBruto;
    }

    public void setDbPremiBruto(BigDecimal dbPremiBruto) {
        this.dbPremiBruto = dbPremiBruto;
    }

    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    public Date getDtDueDate() {
        return dtDueDate;
    }

    public void setDtDueDate(Date dtDueDate) {
        this.dtDueDate = dtDueDate;
    }

    public String getStARInvoiceID() {
        return stARInvoiceID;
    }

    public void setStARInvoiceID(String stARInvoiceID) {
        this.stARInvoiceID = stARInvoiceID;
    }

    public String getStInsurancePolicyInwardInstallmentID() {
        return stInsurancePolicyInwardInstallmentID;
    }

    public void setStInsurancePolicyInwardInstallmentID(String stInsurancePolicyInwardInstallmentID) {
        this.stInsurancePolicyInwardInstallmentID = stInsurancePolicyInwardInstallmentID;
    }
   
}

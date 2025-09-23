/***********************************************************************
 * Module:  com.webfin.ar.model.ARTaxView
 * Author:  Denny Mahendra
 * Created: Apr 2, 2006 5:31:31 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class ARTaxView extends DTO implements RecordAudit {

   private String stTaxCode;
   private String stDescription;
   private String stInclusiveFlag;
   private String stAccountCode;
   private BigDecimal dbRate;

   public static String tableName = "ar_tax";

   public static String comboFields[] = {"tax_code","description"};

   public static String fieldMap[][] = {
      {"stTaxCode","tax_code*pk"},
      {"stDescription","description"},
      {"stInclusiveFlag","inclusive_flag"},
      {"dbRate","rate"},
      {"stAccountCode","account_code"},
   };

      /*

      CREATE TABLE ar_tax
      (
        tax_code int8 NOT NULL,
        description varchar(128),
        inclusive_flag varchar(1),
        rate numeric,
        create_date timestamp NOT NULL,
        create_who varchar(32) NOT NULL,
        change_date timestamp,
        change_who varchar(32),
        CONSTRAINT ar_tax_pk PRIMARY KEY (tax_code)
      )
      */

   public String getStTaxCode() {
      return stTaxCode;
   }

   public void setStTaxCode(String stTaxCode) {
      this.stTaxCode = stTaxCode;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStInclusiveFlag() {
      return stInclusiveFlag;
   }

   public void setStInclusiveFlag(String stInclusiveFlag) {
      this.stInclusiveFlag = stInclusiveFlag;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public String getStAccountCode() {
      return stAccountCode;
   }

   public void setStAccountCode(String stAccountCode) {
      this.stAccountCode = stAccountCode;
   }

   public boolean isPPH23BrokerInclude(){
       return "6".equalsIgnoreCase(stTaxCode);
   }

}

/***********************************************************************
 * Module:  com.webfin.gl.model.GLCurrencyView
 * Author:  Denny Mahendra
 * Created: Oct 31, 2005 10:30:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.webfin.gl.ejb.CurrencyManager;

public class GLCurrencyView extends DTO implements RecordAudit {
   /*
CREATE TABLE gl_currency
(
  ccy_code  varchar(3) NOT NULL,
  description  varchar(128) NOT NULL,
  enabled_flag  varchar(1) NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_currency_pk PRIMARY KEY (ccy_code)
);
   */

   public static String tableName = "gl_currency";

   public static String comboFields[] = {"ccy_code","description"};

   public static String fieldMap[][] = {
      {"stCurrencyCode","ccy_code*pk"},
      {"stDescription","description"},
      {"stEnabledFlag","enabled_flag"},
      {"stCcyDescription","ccy_desc"},
   };

   private String stCurrencyCode;
   private String stDescription;
   private String stEnabledFlag;
   private String stCcyDescription;

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStEnabledFlag() {
      return stEnabledFlag;
   }

   public void setStEnabledFlag(String stEnabledFlag) {
      this.stEnabledFlag = stEnabledFlag;
   }

   public boolean isPrimary() {
      return Tools.isEqual(CurrencyManager.getInstance().getMasterCurrency(),stCurrencyCode);
   }
   
   public String getStCcyDescription() {
      return stCcyDescription;
   }

   public void setStCcyDescription(String stCcyDescription) {
      this.stCcyDescription = stCcyDescription;
   }

}

/***********************************************************************
 * Module:  com.webfin.ar.model.ARSettlementExcessView
 * Author:  Denny Mahendra
 * Created: Nov 20, 2006 12:13:11 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import java.math.BigDecimal;

public class ARSettlementExcessView extends DTO implements RecordAudit {
   /*

   CREATE TABLE ar_settlement_excess
(
  ar_settlement_xc_id int8 NOT NULL,
  description  varchar(128),
  f_negative  varchar(1),
  f_positive  varchar(1),
  gl_account  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_settlement_excess_pk PRIMARY KEY (ar_settlement_xc_id)
) without oids;
*/

   public static String tableName = "ar_settlement_excess";

   public static String fieldMap[][] = {
      {"stARSettlementExcessID","ar_settlement_xc_id*pk"},
      {"stDescription","description"},
      {"stNegativeFlag","f_negative"},
      {"stPositiveFlag","f_positive"},
      {"stGLAccount","gl_account"},
      {"stARSettlementID","ar_settlement_id"},
      {"dbMaximumAmount","max_amount"},
   };

   private String stARSettlementExcessID;
   private String stARSettlementID;
   private String stDescription;
   private String stNegativeFlag;
   private String stPositiveFlag;
   private String stGLAccount;
   private BigDecimal dbMaximumAmount;

   public String getStARSettlementID() {
      return stARSettlementID;
   }

   public void setStARSettlementID(String stARSettlementID) {
      this.stARSettlementID = stARSettlementID;
   }

   public String getStARSettlementExcessID() {
      return stARSettlementExcessID;
   }

   public void setStARSettlementExcessID(String stARSettlementExcessID) {
      this.stARSettlementExcessID = stARSettlementExcessID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStNegativeFlag() {
      return stNegativeFlag;
   }

   public void setStNegativeFlag(String stNegativeFlag) {
      this.stNegativeFlag = stNegativeFlag;
   }

   public String getStPositiveFlag() {
      return stPositiveFlag;
   }

   public void setStPositiveFlag(String stPositiveFlag) {
      this.stPositiveFlag = stPositiveFlag;
   }

   public String getStGLAccount() {
      return stGLAccount;
   }

   public void setStGLAccount(String stGLAccount) {
      this.stGLAccount = stGLAccount;
   }

   public boolean isNegative() {
      return Tools.isYes(stNegativeFlag);
   }

    public BigDecimal getDbMaximumAmount() {
        return dbMaximumAmount;
    }

    public void setDbMaximumAmount(BigDecimal dbMaximumAmount) {
        this.dbMaximumAmount = dbMaximumAmount;
    }

}

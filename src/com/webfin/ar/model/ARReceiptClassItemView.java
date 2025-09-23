/***********************************************************************
 * Module:  com.webfin.ar.model.ARReceiptClassItemView
 * Author:  Denny Mahendra
 * Created: Nov 19, 2006 6:21:35 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class ARReceiptClassItemView extends DTO implements RecordAudit {

   /*
   CREATE TABLE receipt_class_item
(
  rc_item_id int8 NOT NULL,
  rc_id int8,
  description  varchar(128),
  chrg_account  varchar(128),
  chrg_account_neg  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT receipt_class_item_pk PRIMARY KEY (rc_item_id)
) without oids;
   */

   public static String tableName = "receipt_class_item";

   public static String fieldMap[][] = {
      {"stReceiptClassItemID","rc_item_id*pk"},
      {"stReceiptClassID","rc_id"},
      {"stDescription","description"},
      {"stChargeAccount","chrg_account"},
      {"stChargeAccountNeg","chrg_account_neg"},
   };

   private String stReceiptClassItemID;
   private String stReceiptClassID;
   private String stDescription;
   private String stChargeAccount;
   private String stChargeAccountNeg;

   public String getStReceiptClassItemID() {
      return stReceiptClassItemID;
   }

   public void setStReceiptClassItemID(String stReceiptClassItemID) {
      this.stReceiptClassItemID = stReceiptClassItemID;
   }

   public String getStReceiptClassID() {
      return stReceiptClassID;
   }

   public void setStReceiptClassID(String stReceiptClassID) {
      this.stReceiptClassID = stReceiptClassID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStChargeAccount() {
      return stChargeAccount;
   }

   public void setStChargeAccount(String stChargeAccount) {
      this.stChargeAccount = stChargeAccount;
   }

   public String getStChargeAccountNeg() {
      return stChargeAccountNeg;
   }

   public void setStChargeAccountNeg(String stChargeAccountNeg) {
      this.stChargeAccountNeg = stChargeAccountNeg;
   }
}

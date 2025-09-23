/***********************************************************************
 * Module:  com.webfin.ar.model.ARPaymentMethodView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:33:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;

public class ARPaymentMethodView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ar_payment_method
(
  pmt_method_id int8 NOT NULL,
  rc_id int8,
  gl_acct_id int8,
  description varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_payment_method_pk PRIMARY KEY (pmt_method_id)

ALTER TABLE payment_method ADD COLUMN bank_code varchar(8);
ALTER TABLE payment_method ADD COLUMN cc_code varchar(8);
ALTER TABLE payment_method ADD COLUMN ext_account_no varchar(32);

)
   */

   public static String tableName = "payment_method";

   public static String fieldMap[][] = {
      {"stPaymentMethodID","pmt_method_id*pk"},
      {"stReceiptClassID","rc_id"},
      {"stGLAccountID","gl_acct_id"},
      {"stDescription","description"},
      {"stBankCode","bank_code"},
      {"stCostCenterCode","cc_code"},
      {"stExternalAccountNo","ext_account_no"},
      {"stBankType","bank_type"},
   };

   private String stPaymentMethodID;
   private String stReceiptClassID;
   private String stGLAccountID;
   private String stDescription;
   private String stBankCode;
   private String stCostCenterCode;
   private String stExternalAccountNo;
   private String stBankType;
   
   public String getStBankType() {
      return stBankType;
   }

   public void setStBankType(String stBankType) {
      this.stBankType = stBankType;
   }

   public String getStExternalAccountNo() {
      return stExternalAccountNo;
   }

   public void setStExternalAccountNo(String stExternalAccountNo) {
      this.stExternalAccountNo = stExternalAccountNo;
   }

   public String getStBankCode() {
      return stBankCode;
   }

   public void setStBankCode(String stBankCode) {
      this.stBankCode = stBankCode;
   }

   public String getStCostCenterCode() {
      return stCostCenterCode;
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
   }


   public String getStPaymentMethodID() {
      return stPaymentMethodID;
   }

   public void setStPaymentMethodID(String stPaymentMethodID) {
      this.stPaymentMethodID = stPaymentMethodID;
   }

   public String getStReceiptClassID() {
      return stReceiptClassID;
   }

   public void setStReceiptClassID(String stReceiptClassID) {
      this.stReceiptClassID = stReceiptClassID;
   }

   public String getStGLAccountID() {
      return stGLAccountID;
   }

   public void setStGLAccountID(String stGLAccountID) {
      this.stGLAccountID = stGLAccountID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public ARReceiptClassView getReceiptClass() {
      return (ARReceiptClassView) DTOPool.getInstance().getDTO(ARReceiptClassView.class, getStReceiptClassID());
   }
}

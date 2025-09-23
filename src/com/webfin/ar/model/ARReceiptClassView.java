/***********************************************************************
 * Module:  com.webfin.ar.model.ARReceiptClassView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:31:41 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;
import com.crux.util.LOV;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.webfin.FinCodec;

public class ARReceiptClassView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ar_receipt_class
(
  rc_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  remit_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_class_pk PRIMARY KEY (rc_id)
)
   */
	private static ARReceiptClassView staticinstance;
	private LOV methodLOV;

   public static String tableName = "receipt_class";
   
   public static String comboFields[] = {"method_code","description"};

   public static String fieldMap[][] = {
      {"stReceiptClassID","rc_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"stRemitFlag","remit_flag"},
      {"stReceiptClassType","rc_type"},
      //{"stInvoiceType","invoice_type"},
      {"stExcessAccount","excess_account"},
      {"stExcessAccountNeg","excess_account_neg"},
      {"stRateDiffAccount","rate_diff_acc"},
      {"stRateDiffAccountNeg","rate_diff_acc_neg"},
      {"stMethodCode","method_code"},
      {"stReference1","ref1"},
   };
   
   public static ARReceiptClassView getInstance() {
      if (staticinstance == null) staticinstance = new ARReceiptClassView();
      return staticinstance;
   }

   private String stExcessAccountNeg;
   private String stRateDiffAccount;
   private String stRateDiffAccountNeg;
   private String stMethodCode;

   private String stReceiptClassID;
   private String stDescription;
   private String stActiveFlag;
   private String stRemitFlag;
   private String stReceiptClassType;
   //private String stInvoiceType;
   private String stExcessAccount;
   private String stReference1;
   
   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStReceiptClassType() {
      return stReceiptClassType;
   }

   public void setStReceiptClassType(String stReceiptClassType) {
      this.stReceiptClassType = stReceiptClassType;
   }

   /*public String getStInvoiceType() {
      return stInvoiceType;
   }

   public void setStInvoiceType(String stInvoiceType) {
      this.stInvoiceType = stInvoiceType;
   }*/

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

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStRemitFlag() {
      return stRemitFlag;
   }

   public void setStRemitFlag(String stRemitFlag) {
      this.stRemitFlag = stRemitFlag;
   }

   public boolean isNote() {
      return Tools.isEqual(stReceiptClassType,FinCodec.ReceiptClassType.NOTE);
   }

   public boolean isBank() {
      return Tools.isEqual(stReceiptClassType,FinCodec.ReceiptClassType.BANK);
   }

   /*public boolean isAP() {
      return Tools.isEqual(stInvoiceType,FinCodec.InvoiceType.AP);
   }

   public boolean isAR() {
      return Tools.isEqual(stInvoiceType,FinCodec.InvoiceType.AR);
   }*/

   public void setStExcessAccount(String stExcessAccount) {
      this.stExcessAccount = stExcessAccount;
   }

   public String getStExcessAccount() {
      return stExcessAccount;
   }

   public String getStExcessAccountNeg() {
      return stExcessAccountNeg;
   }

   public void setStExcessAccountNeg(String stExcessAccountNeg) {
      this.stExcessAccountNeg = stExcessAccountNeg;
   }

   public String getStRateDiffAccount() {
      return stRateDiffAccount;
   }

   public void setStRateDiffAccount(String stRateDiffAccount) {
      this.stRateDiffAccount = stRateDiffAccount;
   }

   public String getStRateDiffAccountNeg() {
      return stRateDiffAccountNeg;
   }

   public void setStRateDiffAccountNeg(String stRateDiffAccountNeg) {
      this.stRateDiffAccountNeg = stRateDiffAccountNeg;
   }

   public String getStMethodCode() {
      return stMethodCode;
   }

   public void setStMethodCode(String stMethodCode) {
      this.stMethodCode = stMethodCode;
   }
   
   /*
    public LOV getMethodCodeLOV() throws Exception {
      loadMethodLOV();

      return methodLOV.setLOValue(methodLOV);
   }
   
   private void loadMethodLOV() throws Exception {
      if (methodLOV==null)
			return ListUtil.getDTOListFromQuery(
              "select rc_id,description from receipt_class",
              ARReceiptClassView.class);
   }
   */
}

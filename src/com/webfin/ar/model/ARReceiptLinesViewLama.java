/***********************************************************************
 * Module:  com.webfin.ar.model.ARReceiptLinesView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:37:07 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.webfin.FinCodec;

import java.math.BigDecimal;

public class ARReceiptLinesViewLama extends DTO implements RecordAudit {
   /*
      CREATE TABLE ar_receipt_lines
(
  ar_rcl_id int8 NOT NULL,
  ar_invoice_id int8,
  amount numeric,
   int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_lines_pk PRIMARY KEY (ar_rcl_id)

  ar_settlement_xc_id
)
   */

   public static String tableName = "ar_receipt_lines";

   public static String fieldMap[][] = {
      {"stReceiptLinesID","ar_rcl_id*pk*nd"},
      {"stInvoiceID","ar_invoice_id"},
      {"dbAmount","amount"},
      {"dbEnteredAmount","amount_entered"},
      {"stReceiptID","receipt_id"},
      {"stInvoiceNo","ar_invoice_no"},
      {"dbInvoiceAmount","invoice_amount"},
      {"stLineType","line_type"},
      {"stComissionFlag","comission_flag"},
      {"stTaxableFlag","taxable_flag"},
      {"stParentID","parent_id"},
      {"stDescription","description"},
      {"dbEnteredInvoiceAmount","inv_amt_entered"},
      {"stCurrencyCode","ccy_code"},
      {"dbCurrencyRate","ccy_rate"},
      {"stCommitFlag","commit_flag"},
      {"stReceiptLinesRefID","ar_rcl_ref_id"},
      {"stInvoiceDetailID","ar_invoice_dtl_id"},
      {"stExpandedFlag","f_expanded"},
      {"stARSettlementExcessID","ar_settlement_xc_id"},
   };

   private String stARSettlementExcessID;
   private String stCurrencyCode;
   private String stCommitFlag;
   private String stExpandedFlag;
   private BigDecimal dbCurrencyRate;

   private String stReceiptLinesID;
   private String stInvoiceID;
   private String stInvoiceNo;
   private BigDecimal dbAmount;
   private BigDecimal dbEnteredAmount;
   private BigDecimal dbInvoiceAmount;
   private BigDecimal dbEnteredInvoiceAmount;
   private BigDecimal dbAmountSettled;
   private String stReceiptID;
   private String stLineType;
   private String stComissionFlag;
   private String stTaxableFlag;
   private String stParentID;
   private String stDescription;
   private String stReceiptLinesRefID;
   private String stInvoiceDetailID;

   private DTOList details;
   private ARSettlementExcessView arSettlementExcess;
   private String stNosurathutang;
   private ARTransactionLineView arTransactionLine;
   private String stARTrxLineID;
   
   public String getStNosurathutang() {
      return stNosurathutang;
   }

   public void setStNosurathutang(String stNosurathutang) {
      this.stNosurathutang = stNosurathutang;
   }
   
   public String getStARTrxLineID() {
      return stARTrxLineID;
   }

   public void setStARTrxLineID(String stARTrxLineID) {
      this.stARTrxLineID = stARTrxLineID;
   }

   public DTOList getDetails() {
      if (details==null) details = new DTOList();
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }

   public String getStCommitFlag() {
      return stCommitFlag;
   }

   public void setStCommitFlag(String stCommitFlag) {
      this.stCommitFlag = stCommitFlag;
   }

   public String getStLineType() {
      return stLineType;
   }

   public void setStLineType(String stLineType) {
      this.stLineType = stLineType;
   }

   public String getStReceiptLinesID() {
      return stReceiptLinesID;
   }

   public void setStReceiptLinesID(String stReceiptLinesID) {
      this.stReceiptLinesID = stReceiptLinesID;
   }

   public String getStInvoiceID() {
      return stInvoiceID;
   }

   public void setStInvoiceID(String stInvoiceID) {
      this.stInvoiceID = stInvoiceID;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }
   
   public BigDecimal getDbAmount2() {
   	  BigDecimal dbAmountDetail = null;
   	  final DTOList invoiceDetail = getInvoice().getDetails();
   	  
   	  for (int i = 0; i < invoiceDetail.size(); i++) {
	  		ARInvoiceDetailView invDetail = (ARInvoiceDetailView) invoiceDetail.get(i);
	  		
	  		//if(invDetail.iscol){
				if (invDetail.isComission()) continue;
				dbAmountDetail = BDUtil.add(dbAmountDetail,invDetail.getDbAmount());
			//}
      }
      //javax.swing.JOptionPane.showMessageDialog(null,"Taxed getDbAmount= "+ dbAmountDetail,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
     
      return dbAmountDetail;
   }


   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStReceiptID() {
      return stReceiptID;
   }

   public void setStReceiptID(String stReceiptID) {
      this.stReceiptID = stReceiptID;
   }

   public void setStInvoiceNo(String stInvoiceNo) {
      this.stInvoiceNo = stInvoiceNo;
   }

   public String getStInvoiceNo() {
      return stInvoiceNo;
   }

   public void setDbInvoiceAmount(BigDecimal dbInvoiceAmount) {
      this.dbInvoiceAmount = dbInvoiceAmount;
   }

   public BigDecimal getDbInvoiceAmount() {
      return dbInvoiceAmount;
   }

   public void markAsNote() {
      stLineType = FinCodec.ARReceiptLineType.NOTE;
   }

   public void markAsInvoice() {
      stLineType = FinCodec.ARReceiptLineType.INVOICE;
   }


   public ARInvoiceView getInvoice() {
      return (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, getStInvoiceID());   
   }
   
   public ARInvoiceView getInvoiceBySuratHutang() throws Exception{
      //return (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, nosurathutang);
      
      final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                    "select * from ar_invoice where no_surat_hutang = ?",
                    new Object [] {stNosurathutang},
                     ARInvoiceView.class
            ).getDTO();
      return iv;
   }

   public boolean isInvoice() {
      return FinCodec.ARReceiptLineType.INVOICE.equalsIgnoreCase(getStLineType());
   }

   public boolean isNote() {
      return FinCodec.ARReceiptLineType.NOTE.equalsIgnoreCase(getStLineType());
   }

   //public BigDecimal getDbActInvAmount() {
  //    return getInvoice().getDbAmount();
  // }
   
   public BigDecimal getDbActInvAmount() {
      return getDbAmount2();
   }

   public BigDecimal getDbActInvAmountSettled() {
      return getInvoice().getDbEnteredAmount();
   }

   public BigDecimal getDbActInvOutstandingAmount() {
      return getInvoice().getDbOutstandingAmount();
   }

   public BigDecimal getDbActInvOutstandingAmountIDR() {
      return getInvoice().getDbOutstandingAmountIDR();
   }

   public BigDecimal dbActInvSettled() {
      return getInvoice().getDbAmountSettled();
   }

   public String getStComissionFlag() {
      return stComissionFlag;
   }

   public void setStComissionFlag(String stComissionFlag) {
      this.stComissionFlag = stComissionFlag;
   }

   public String getStTaxableFlag() {
      return stTaxableFlag;
   }

   public void setStTaxableFlag(String stTaxableFlag) {
      this.stTaxableFlag = stTaxableFlag;
   }

   public String getStParentID() {
      return stParentID;
   }

   public void setStParentID(String stParentID) {
      this.stParentID = stParentID;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setDbEnteredAmount(BigDecimal dbEnteredAmount) {
      this.dbEnteredAmount = dbEnteredAmount;
   }

   public BigDecimal getDbEnteredAmount() {
      return dbEnteredAmount;
   }

   public void setDbEnteredInvoiceAmount(BigDecimal dbEnteredInvoiceAmount) {
      this.dbEnteredInvoiceAmount = dbEnteredInvoiceAmount;
   }

   public BigDecimal getDbEnteredInvoiceAmount() {
      return dbEnteredInvoiceAmount;
   }

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public BigDecimal getDbCurrencyRate() {
      return dbCurrencyRate;
   }

   public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
      this.dbCurrencyRate = dbCurrencyRate;
   }

   public void markCommit() {
      setStCommitFlag("Y");
   }

   public String getStReceiptLinesRefID() {
      return stReceiptLinesRefID;
   }

   public void setStReceiptLinesRefID(String stReceiptLinesRefID) {
      this.stReceiptLinesRefID = stReceiptLinesRefID;
   }

   public String getStInvoiceDetailID() {
      return stInvoiceDetailID;
   }

   public void setStInvoiceDetailID(String stInvoiceDetailID) {
      this.stInvoiceDetailID = stInvoiceDetailID;
   }

   public void markAsComission() {
      stLineType = FinCodec.ARReceiptLineType.COMISSION;
   }

   public boolean isComission() {
      return Tools.isEqual(FinCodec.ARReceiptLineType.COMISSION, stLineType);
   }

   public ARInvoiceDetailView getInvoiceDetail() {
      return (ARInvoiceDetailView)DTOPool.getInstance().getDTO(ARInvoiceDetailView.class, stInvoiceDetailID);
   }

   /*public BigDecimal getDbComissionSettled() {
   }*/

   public BigDecimal getDbAmountSettled() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.sub(getDbAmountSettledAct(),old==null?null:old.getDbAmount());
   }

   private ARReceiptLinesView getOldReceiptLines() {
      return (ARReceiptLinesView) getOld();
   }

   public BigDecimal getDbAmountSettledAct() {
      if (isComission())
         return getInvoiceDetail().getDbAmountSettled();

      return getInvoice().getDbAmountSettled();
   }
   
   public BigDecimal getDbPremiBruto() {
      if (getInvoiceDetail().getStRefID0().toUpperCase().startsWith("PREMI"))
         return getInvoiceDetail().getDbAmount();

      return getInvoice().getDbAmount();
   }

/*
   public BigDecimal getDbOutstandingAmount() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.add(getDbOutstandingAmountAct(),old==null?null:old.getDbAmount());
   }
*/
   
   public BigDecimal getDbOutstandingAmount() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.add(getDbOutstandingAmountAct2(),old==null?null:old.getDbAmount());
   }

   public BigDecimal getDbOutstandingAmountAct() {
      if (isComission())
         return getInvoiceDetail().getDbOustandingAmount();

      return getInvoice().getDbOutstandingAmount();

   }
   
   public BigDecimal getDbOutstandingAmountAct2() {
      //if (isComission())
         //return getInvoiceDetail().getDbOustandingAmount();

      return getInvoice().getDbOutstandingAmount();

   }

   public String getStExpandedFlag() {
      return stExpandedFlag;
   }

   public void setStExpandedFlag(String stExpandedFlag) {
      this.stExpandedFlag = stExpandedFlag;
   }

   public boolean isGL() {
      return Tools.isEqual(FinCodec.ARReceiptLineType.GL, stLineType);
   }

   public String getStARSettlementExcessID() {
      return stARSettlementExcessID;
   }

   public void setStARSettlementExcessID(String stARSettlementExcessID) {
      this.stARSettlementExcessID = stARSettlementExcessID;
   }

   public void markAsGL() {
      stLineType = FinCodec.ARReceiptLineType.GL;
   }

   public ARSettlementExcessView getARSettlementExcess() {
      if (arSettlementExcess==null)
         arSettlementExcess = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stARSettlementExcessID);

      return arSettlementExcess;
   }
   
   public ARTransactionLineView getARTransactionLine() {
      if (arTransactionLine==null)
         arTransactionLine = (ARTransactionLineView) DTOPool.getInstance().getDTO(ARTransactionLineView.class, stARTrxLineID);

      return arTransactionLine;
   }
   
   //tes
   public BigDecimal getDbActInvAmount2() throws Exception{
      return getInvoiceBySuratHutang().getDbAmount();
   }

   public BigDecimal getDbActInvAmountSettled2() throws Exception{
      return getInvoiceBySuratHutang().getDbEnteredAmount();
   }

   public BigDecimal getDbActInvOutstandingAmount2() throws Exception{
      return getInvoiceBySuratHutang().getDbOutstandingAmount();
   }

   public BigDecimal getDbActInvOutstandingAmountIDR2() throws Exception{
      return getInvoiceBySuratHutang().getDbOutstandingAmountIDR();
   }

   public BigDecimal dbActInvSettled2() throws Exception{
      return getInvoiceBySuratHutang().getDbAmountSettled();
   }
}

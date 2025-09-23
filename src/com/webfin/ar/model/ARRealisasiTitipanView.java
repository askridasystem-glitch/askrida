/***********************************************************************
 * Module:  com.webfin.ar.model.ARReceiptView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.*;

import java.math.BigDecimal;
import java.util.Date;

public class ARRealisasiTitipanView extends DTO implements RecordAudit {
   /*
   REATE TABLE ar_receipt
(
  ar_receipt_id int8 NOT NULL,
  receipt_no varchar(64),
  amount numeric,
  ccy varchar(3),
  posted_flag varchar(1),
  pmt_method_id int8,
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_pk PRIMARY KEY (ar_receipt_id)

ALTER TABLE ar_receipt ADD COLUMN description varchar(255);
ALTER TABLE ar_receipt ADD COLUMN shortdesc varchar(128);

ALTER TABLE ar_receipt ADD COLUMN ar_settlement_id int8;
ALTER TABLE ar_receipt ADD COLUMN entity_id int8;


)
   */

   public static String tableName = "ar_receipt";

   public static String fieldMap[][] = {
      {"stARReceiptID","ar_receipt_id*pk"},
      {"stARSettlementID","ar_settlement_id"},
      {"stEntityID","entity_id"},
      {"stReceiptNo","receipt_no"},
      {"dbAmount","amount"},
      {"stCurrencyCode","ccy"},
      {"stPostedFlag","posted_flag"},
      {"stPaymentMethodID","pmt_method_id"},
      {"stCancelFlag","cancel_flag"},
      {"stReceiptClassID","rc_id"},
      {"dbAmountRemain","amount_remain"},
      {"dbAmountApplied","amount_applied"},
      {"stInvoiceType","invoice_type"},
      {"dtReceiptDate","receipt_date"},
      {"stCostCenterCode","cc_code"},
      {"stAccountID","account_id"},
      {"dbCurrencyRate","ccy_rate"},
      {"stDescription","description"},
      {"stShortDescription","shortdesc"},
      {"stExcessAccountID","excess_acc_id"},
      {"dbEnteredAmount","entered_amount"},
      {"dbRateDiffAmount","rate_diff_amount"},
      {"stARAPInvoiceID","ar_ap_invoice_id"},
      {"stStatus","status"},
      {"stARTitipanID","ar_titipan_id"},
      {"stAccountEntityID","account_entity_id"},
      {"stBankType","bank_type"},
      {"stEntityName","entity_name*n"},
   };

   private BigDecimal dbEnteredAmount;
   private BigDecimal dbRateDiffAmount;

   private String stStatus;
   private String stEntityName;
   private String stARSettlementID;
   private String stEntityID;
   private String stARReceiptID;
   private String stARAPInvoiceID;
   private String stReceiptNo;
   private BigDecimal dbAmount;
   private BigDecimal dbAmountRemain;
   private BigDecimal dbAmountApplied;
   private BigDecimal dbCurrencyRate;
   private String stDescription;
   private String stShortDescription;
   private String stCurrencyCode;
   private String stPostedFlag;
   private String stPaymentMethodID;
   private String stCancelFlag;
   private String stReceiptClassID;
   private String stInvoiceType;
   private String stCostCenterCode;
   private String stAccountID;
   private String stExcessAccountID;
   private Date dtReceiptDate;
   private DTOList details;
   private DTOList notes;
   private ARAPSettlementView arapSettlementView;
   private DTOList gls;
   private ARInvoiceView arapinvoice;
   private String stARTitipanID;
   private String stBankType;
   private String stAccountEntityID;
   public boolean isUsingEntityID = false;
   

   
   public String getStAccountEntityID() {
      return stAccountEntityID;
   }

   public void setStAccountEntityID(String stAccountEntityID) {
      this.stAccountEntityID = stAccountEntityID;
   }

   public ARInvoiceView getArapinvoice() {
      if (arapinvoice == null) {

         if (getStARAPInvoiceID()!=null) {
            ARInvoiceView iv = (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, stARAPInvoiceID);

            iv.getDetails();

            arapinvoice = iv;

            return arapinvoice;
         }

         arapinvoice = new ARInvoiceView();
         arapinvoice.markNew();

         arapinvoice.setDetails(new DTOList());

         ARInvoiceDetailView ivd = new ARInvoiceDetailView();

         ivd.markNew();

         arapinvoice.getDetails().add(ivd);
      }
      return arapinvoice;
   }

   public void setArapinvoice(ARInvoiceView arapinvoice) {
      this.arapinvoice = arapinvoice;
   }
   
   public String getStBankType() {
      return stBankType;
   }

   public void setStBankType(String stBankType) {
      this.stBankType = stBankType;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStShortDescription() {
      return stShortDescription;
   }

   public void setStShortDescription(String stShortDescription) {
      this.stShortDescription = stShortDescription;
   }

   public DTOList getNotes() throws Exception {
      if (notes==null) notes=loadDetails(FinCodec.ARReceiptLineType.NOTE);
      if(notes==null) notes= new DTOList();
      return notes;
   }

   public void setNotes(DTOList notes) {
      this.notes = notes;
   }

   public String getStNoteType() {
      return isAR()?FinCodec.InvoiceType.AP:FinCodec.InvoiceType.AR; 
   }

   public String getStInvoiceType() {
      return stInvoiceType;
   }

   public void setStInvoiceType(String stInvoiceType) {
      this.stInvoiceType = stInvoiceType;
   }

   public Date getDtReceiptDate() {
      return dtReceiptDate;
   }

   public void setDtReceiptDate(Date dtReceiptDate) {
      this.dtReceiptDate = dtReceiptDate;
   }

   public String getStARReceiptID() {
      return stARReceiptID;
   }

   public void setStARReceiptID(String stARReceiptID) {
      this.stARReceiptID = stARReceiptID;
   }

   public String getStReceiptNo() {
      return stReceiptNo;
   }

   public void setStReceiptNo(String stReceiptNo) {
      this.stReceiptNo = stReceiptNo;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public String getStPostedFlag() {
      return stPostedFlag;
   }

   public void setStPostedFlag(String stPostedFlag) {
      this.stPostedFlag = stPostedFlag;
      updateStatus();
   }

   public String getStPaymentMethodID() {
      return stPaymentMethodID;
   }

   public void setStPaymentMethodID(String stPaymentMethodID) {
      this.stPaymentMethodID = stPaymentMethodID;
   }

   public String getStCancelFlag() {
      return stCancelFlag;
   }

   public void setStCancelFlag(String stCancelFlag) {
      this.stCancelFlag = stCancelFlag;
      updateStatus();
   }

   public void setStReceiptClassID(String stReceiptClassID) {
      this.stReceiptClassID = stReceiptClassID;
   }

   public String getStReceiptClassID() {
      return stReceiptClassID;
   }

   public void setDbAmountRemain(BigDecimal dbAmountRemain) {
      this.dbAmountRemain = dbAmountRemain;
   }

   public BigDecimal getDbAmountRemain() {
      return dbAmountRemain;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }

   public DTOList getDetails() throws Exception {
   	  if(details!=null) return details;
      if (details==null) details=loadDetails(FinCodec.ARReceiptLineType.INVOICE);
      if (details==null) details = new DTOList();
      return details;
   }

   public void recalculate() throws Exception {

      updateStatus();

      GLUtil.Applicator gla = new GLUtil.Applicator();

      dbAmount = BDUtil.mul(dbEnteredAmount,dbCurrencyRate,2);

      getNotes();
      if (isNote()) {

         stCurrencyCode = CurrencyManager.getInstance().getMasterCurrency();
         dbCurrencyRate = BDUtil.one;

         BigDecimal tot = null;
         if (notes!=null)
            for (int i = 0; i < notes.size(); i++) {
               ARReceiptLinesView arrc = (ARReceiptLinesView) notes.get(i);

               BigDecimal eamt = arrc.getDbEnteredAmount();

               eamt=BDUtil.mul(eamt, arrc.getInvoice().getDbCurrencyRate(),2);

               eamt=BDUtil.div(eamt, getDbCurrencyRate());

               arrc.setDbAmount(eamt);

               tot = BDUtil.add(tot, arrc.getDbAmount());
            }
         dbEnteredAmount = tot;
         dbAmount = tot;
      }

      BigDecimal tot = null;

      getDetails();
      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);
		 
		 //if(!rl.isCheck()) continue;
		 
         BigDecimal eamt = rl.getDbEnteredAmount();

         eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);

         eamt=BDUtil.div(eamt, getDbCurrencyRate());

         rl.setDbAmount(eamt);

         //tot = BDUtil.add(tot, rl.getDbAmount());

         final DTOList details = rl.getDetails();

         for (int j = 0; j < details.size(); j++) {
            ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);
			
			final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();
			
			//if(d.isCheck()) continue;
			
			//if(rcInvoiceDetail.getStRefID0().startsWith("TAX")) continue;
			
            d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
			
			if(d.isComission()||rcInvoiceDetail.isNegative()){
				if(rcInvoiceDetail.isTaxComm()||rcInvoiceDetail.isTaxBrok()||rcInvoiceDetail.isTaxHFee()){
				}else{
					tot = BDUtil.sub(tot, d.getDbAmount());
				} 
			}else{
				tot = BDUtil.add(tot, d.getDbAmount());
			}
				
			if(BDUtil.lesserThanZero(tot))
				tot = BDUtil.negate(tot);
         }
      }

      getGLs();
      for (int i = 0; i < gls.size(); i++) {
         ARReceiptLinesView rl = (ARReceiptLinesView) gls.get(i);

         //rl.setDbAmount(BDUtil.mul(rl.getDbEnteredAmount(), getDbCurrencyRate()));
         rl.setDbAmount(rl.getDbEnteredAmount());

         final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

         final boolean negative = arSettlementExcess.isNegative();

         if (negative)
            tot = BDUtil.sub(tot, rl.getDbAmount());
         else
            tot = BDUtil.add(tot, rl.getDbAmount());
            
         //javax.swing.JOptionPane.showMessageDialog(null,"Tot2="+tot,"eror",javax.swing.JOptionPane.CLOSED_OPTION);    

      }

      dbAmountApplied = tot;

      dbAmountRemain = BDUtil.sub(dbEnteredAmount, dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");

      gla.setCode('C',"C");
      gla.setDesc("C","C");*/

      gla.setCode('B',getStCostCenterCode());
      //gla.setCode('B',"B");

      gla.setCode('Y',getStEntityID());
      
      if(stAccountEntityID!=null)
      		gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());

      EntityView entity = getEntity();

      if(entity!=null)
         gla.setDesc("Y","CUMI");

      //if (BDUtil.biggerThanZero(dbAmountRemain)) {
      ARInvoiceView iv = getArapinvoice();
      iv.setDbAmount(dbAmountRemain);

      iv.setStEntityID(getStEntityID());

      iv.setStInvoiceNo(getStReceiptNo());
      iv.setDtInvoiceDate(getDtReceiptDate());
      iv.setDtDueDate(null);
      iv.setDbAmountSettled(null);
      iv.setStCurrencyCode(getStCurrencyCode());
      iv.setDbCurrencyRate(getDbCurrencyRate());
      iv.setStPostedFlag(getStPostedFlag());

      iv.setStARCustomerID(getStEntityID());
      iv.setStEntityID(getStEntityID());
      iv.setStCostCenterCode(getStCostCenterCode());

      iv.setStReferenceY0(getStEntityID());
      iv.setStReferenceY1(entity==null?null:entity.getStEntityName());

      iv.setStNoJournalFlag("Y");

      if (getSettlement()!=null) {
         /*String acID = gla.getAccountID(getSettlement().getStARAPAccount());
         iv.setStGLARAccountID(acID);
         iv.setStGLARAccountDesc(gla.getPreviewDesc());*/

         iv.setStARTransactionTypeID(getSettlement().getStARAPTrxTypeID());
      }

      if (isAP())
         iv.setStInvoiceType(FinCodec.InvoiceType.AR);
      else
         iv.setStInvoiceType(FinCodec.InvoiceType.AP);

      ARInvoiceDetailView ivd = (ARInvoiceDetailView) iv.getDetails().get(0);

      if (getPaymentMethod()!=null) {
         ivd.setStGLAccountID(getPaymentMethod().getStGLAccountID());
         ivd.setStDescription(getPaymentMethod().getStDescription());
      }else{
      	 if(getReceiptClass()!=null&&stAccountEntityID!=null){
	      	 final String ref1 = getReceiptClass().getStReference1().trim();
	      	 //final String glCode = getEntity2(stAccountEntityID).getStGLCode().trim();
	      	 //final String accountNo = ref1 + glCode + "00";
	      	 //if(true)
	      	 //	throw new RuntimeException("account= "+accountNo);
	      	 ivd.setStGLAccountID(gla.getAccountID(ref1));
	         ivd.setStDescription(gla.getPreviewDesc());
	         
      	 }

      }
      setStAccountID(ivd.getStGLAccountID());

      ivd.setStComissionFlag("N");
      ivd.setStNegativeFlag("N");
      ivd.setDbEnteredAmount(iv.getDbAmount());

      boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());

      iv.setStHideFlag(hasAmount?"N":"Y");
      iv.setStNonPostableFlag("Y");

      //}
   }
   
   public void recalculate2() throws Exception {

      updateStatus();

      GLUtil.Applicator gla = new GLUtil.Applicator();

      dbAmount = BDUtil.mul(dbEnteredAmount,dbCurrencyRate);

      getNotes();
      if (isNote()) {

         stCurrencyCode = CurrencyManager.getInstance().getMasterCurrency();
         dbCurrencyRate = BDUtil.one;

         BigDecimal tot = null;
         if (notes!=null)
            for (int i = 0; i < notes.size(); i++) {
               ARReceiptLinesView arrc = (ARReceiptLinesView) notes.get(i);

               BigDecimal eamt = arrc.getDbEnteredAmount();

               eamt=BDUtil.mul(eamt, arrc.getInvoice().getDbCurrencyRate());

               eamt=BDUtil.div(eamt, getDbCurrencyRate());

               arrc.setDbAmount(eamt);

               tot = BDUtil.add(tot, arrc.getDbAmount());
            }
         dbEnteredAmount = tot;
         dbAmount = tot;
      }

      BigDecimal tot = null;

      getDetails();
      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);

         BigDecimal eamt = rl.getDbEnteredAmount();

         eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate());

         eamt=BDUtil.div(eamt, getDbCurrencyRate());

         rl.setDbAmount(eamt);

         tot = BDUtil.add(tot, rl.getDbAmount());

         final DTOList details = rl.getDetails();

         for (int j = 0; j < details.size(); j++) {
            ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);

            d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate()));

            tot = BDUtil.add(tot, d.getDbAmount());
         }
      }

      getGLs();
      for (int i = 0; i < gls.size(); i++) {
         ARReceiptLinesView rl = (ARReceiptLinesView) gls.get(i);

         //rl.setDbAmount(BDUtil.mul(rl.getDbEnteredAmount(), getDbCurrencyRate()));
         rl.setDbAmount(rl.getDbEnteredAmount());

         final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

         final boolean negative = arSettlementExcess.isNegative();

         if (negative)
            tot = BDUtil.sub(tot, rl.getDbAmount());
         else
            tot = BDUtil.add(tot, rl.getDbAmount());
      }

      dbAmountApplied = tot;

      dbAmountRemain = BDUtil.sub(dbEnteredAmount, dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");

      gla.setCode('C',"C");
      gla.setDesc("C","C");*/

      gla.setCode('B',getStCostCenterCode());
      //gla.setCode('B',"B");

      gla.setCode('Y',getStEntityID());
      
      if(stAccountEntityID!=null)
      		gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
      
      EntityView entity = getEntity();

      //if (BDUtil.biggerThanZero(dbAmountRemain)) {
      ARInvoiceView iv = getArapinvoice();
      iv.setDbAmount(dbAmountRemain);

      iv.setStEntityID(getStEntityID());

      iv.setStInvoiceNo(getStReceiptNo());
      iv.setDtInvoiceDate(getDtReceiptDate());
      iv.setDtDueDate(null);
      iv.setDbAmountSettled(null);
      iv.setStCurrencyCode(getStCurrencyCode());
      iv.setDbCurrencyRate(getDbCurrencyRate());
      iv.setStPostedFlag(getStPostedFlag());

      iv.setStARCustomerID(getStEntityID());
      iv.setStEntityID(getStEntityID());
      iv.setStCostCenterCode(getStCostCenterCode());

      iv.setStReferenceY0(getStEntityID());
      iv.setStReferenceY1(entity==null?null:entity.getStEntityName());

      iv.setStNoJournalFlag("Y");

      if (getSettlement()!=null) {
         /*String acID = gla.getAccountID(getSettlement().getStARAPAccount());
         iv.setStGLARAccountID(acID);
         iv.setStGLARAccountDesc(gla.getPreviewDesc());*/
 
         iv.setStARTransactionTypeID(getSettlement().getStARAPTrxTypeID());
      }

      if (isAP())
         iv.setStInvoiceType(FinCodec.InvoiceType.AR);
      else
         iv.setStInvoiceType(FinCodec.InvoiceType.AP);

      ARInvoiceDetailView ivd = (ARInvoiceDetailView) iv.getDetails().get(0);

      if (getPaymentMethod()!=null) {
         ivd.setStGLAccountID(getPaymentMethod().getStGLAccountID());
         ivd.setStDescription(getPaymentMethod().getStDescription());
      }

      ivd.setStComissionFlag("N");
      ivd.setStNegativeFlag("N");
      ivd.setDbEnteredAmount(iv.getDbAmount());

      boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());

      iv.setStHideFlag(hasAmount?"N":"Y");
      iv.setStNonPostableFlag("Y");

      //}
   }

   private void updateStatus() {
           if (isCancel()) stStatus = "VOID";
      else if (isPosted()) stStatus = "POST";
      else stStatus = "NEW";
   }

   public EntityView getEntity() {
      return (EntityView) DTOPool.getInstance().getDTORO(EntityView.class, stEntityID);
   }

   public void setDbAmountApplied(BigDecimal dbAmountApplied) {
      this.dbAmountApplied = dbAmountApplied;
   }

   public BigDecimal getDbAmountApplied() {
      return dbAmountApplied;
   }

   public void validate() throws Exception {
      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);

         if (Tools.compare(rcl.getDbInvoiceAmount(),rcl.getDbAmount())<0) throw new Exception("Invalid Invoice payment amount : "+rcl.getDbAmount()+" > "+rcl.getDbInvoiceAmount());
      }

      if (Tools.compare(dbAmountRemain,BDUtil.zero)<0) throw new Exception("Invalid Payment Amount,remain= "+ dbAmountRemain+",applied= "+dbAmountApplied);

      if (isNote())
         if (Tools.compare(dbAmountRemain,BDUtil.zero)!=0) throw new Exception("Settlement Note is not balanced");
   }

   public boolean isNote() {
      final ARReceiptClassView rc = getReceiptClass();
      return rc==null?false:rc.isNote();
   }

   public ARReceiptClassView getReceiptClass() {
      return (ARReceiptClassView) (stReceiptClassID == null? null : DTOPool.getInstance().getDTO(ARReceiptClassView.class, stReceiptClassID));
   }

   public boolean isBank() {
      final ARReceiptClassView rc = getReceiptClass();
      return rc==null?false:rc.isBank();
   }

   public boolean isAP() {
      final ARAPSettlementView rc = getSettlement();
      return rc==null?false:rc.isAP();
   }

   public boolean isAR() {
      final ARAPSettlementView rc = getSettlement();
      return rc==null?false:rc.isAR();
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
   }

   public String getStCostCenterCode() {
      return stCostCenterCode;
   }

   public ARPaymentMethodView getPaymentMethod() {
      return (ARPaymentMethodView) DTOPool.getInstance().getDTO(ARPaymentMethodView.class, getStPaymentMethodID());
   }

   public void setStAccountID(String stAccountID) {
      this.stAccountID = stAccountID;
   }

   public String getStAccountID() {
      return stAccountID;
   }

   public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
      this.dbCurrencyRate = dbCurrencyRate;
   }

   public BigDecimal getDbCurrencyRate() {
      return dbCurrencyRate;
   }

   public void setStExcessAccountID(String stExcessAccountID) {
      this.stExcessAccountID = stExcessAccountID;
   }

   public String getStExcessAccountID() {
      return stExcessAccountID;
   }

   public String getStRefTRX() {
      return "RCP/"+getStARReceiptID();
   }

   public String getStInvoiceTypeDesc() {
      return (String) FinCodec.InvoiceType.getLookUp().getValue(getStInvoiceType());
   }

   public BigDecimal getDbEnteredAmount() {
      return dbEnteredAmount;
   }

   public void setDbEnteredAmount(BigDecimal dbEnteredAmount) {
      this.dbEnteredAmount = dbEnteredAmount;
   }

   public BigDecimal getDbRateDiffAmount() {
      return dbRateDiffAmount;
   }

   public void setDbRateDiffAmount(BigDecimal dbRateDiffAmount) {
      this.dbRateDiffAmount = dbRateDiffAmount;
   }

   public boolean isMasterCurrency() {
      return Tools.isEqual(stCurrencyCode, CurrencyManager.getInstance().getMasterCurrency());
   }

   public String getStARAPInvoiceID() {
      return stARAPInvoiceID;
   }

   public void setStARAPInvoiceID(String stARAPInvoiceID) {
      this.stARAPInvoiceID = stARAPInvoiceID;
   }

   public String getStARSettlementID() {
      return stARSettlementID;
   }

   public void setStARSettlementID(String stARSettlementID) {
      this.stARSettlementID = stARSettlementID;
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public ARAPSettlementView getSettlement() {
      if (arapSettlementView==null)
         arapSettlementView = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, stARSettlementID);

      return arapSettlementView;
   }

   public DTOList getGLs() throws Exception {
      if (gls==null) gls=loadDetails(FinCodec.ARReceiptLineType.GL);
      if (gls==null) gls=new DTOList();
      return gls;
   }

   public void setGls(DTOList gls) {
      this.gls = gls;
   }

   private DTOList loadDetails(String receiptLineType) throws Exception {
      if (stARReceiptID!=null)
         return ListUtil.getDTOListFromQuery(
                 "select * from  ar_receipt_lines where receipt_id = ? and line_type = ?",
                 new Object [] {stARReceiptID, receiptLineType},
                 ARReceiptLinesView.class
         );

      return null;
   }

   public String getStStatus() {
      updateStatus();
      return stStatus;
   }

   public void setStStatus(String stStatus) {
      this.stStatus = stStatus;
   }

   public void generateReceiptNo() throws Exception {

      if (stReceiptNo!=null) return;

      final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);
      final String methodCode = Tools.getDigitRightJustified(getReceiptClass().getStMethodCode(),1);
      String stBankCode = getPaymentMethod()==null?"00000":getPaymentMethod().getStBankCode();
      final String bankCode = Tools.getDigitRightJustified(stBankCode,5);
	  
      String counterKey =
              DateUtil.getMonth2Digit(getDtReceiptDate())+
              DateUtil.getYear2Digit(getDtReceiptDate());

      String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

      rn = Tools.getDigitRightJustified(rn,4);
      
      //A 01 10 1010 00000 00 001
      stReceiptNo =
              methodCode +
              counterKey +
              ccCode +
              ccCode +
              bankCode +
              "00"+
              rn;

      if (arapinvoice!=null)
         arapinvoice.setStInvoiceNo(stReceiptNo);
   }
   


   public boolean isPosted() {
      return Tools.isYes(stPostedFlag);
   }

   public boolean isCancel() {
      return Tools.isYes(stCancelFlag);
   }

   public String getStEntityName() {
      return stEntityName;
   }

   public void setStEntityName(String stEntityName) {
      this.stEntityName = stEntityName;
   }
   
   public String getStARTitipanID() {
      return stARTitipanID;
   }

   public void setStARTitipanID(String stARTitipanID) {
      this.stARTitipanID = stARTitipanID;
   }  
   
   public ARInvoiceView getInvoice(String stARInvoiceID) {
      if (stARInvoiceID==null) return null;
      return (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, stARInvoiceID);
   }
   
   public ARReceiptLinesView getARReceiptLines() {
      return (ARReceiptLinesView) DTOPool.getInstance().getDTO(ARReceiptLinesView.class, stARReceiptID);
   }
   
   public boolean isUsingEntityID() {
      return isUsingEntityID;
   }
   
   public void setUsingEntityID(boolean isUsingEntityID){
   	  this.isUsingEntityID = isUsingEntityID;
   }
   
   public EntityView getEntity2(String stEntityID) {
      return (EntityView) DTOPool.getInstance().getDTORO(EntityView.class, stEntityID);
   }
   
   /*
   public DTOList getList() throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("a.*, b.ent_name as entity_name");

      sqa.addQuery(
              " from " +
              "   ar_receipt a");
	  
	  if (stSettlementID==null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar("1  ");
      }
	  
      if (stSettlementID!=null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar(stSettlementID);
      }

      if (rcpDateFrom!=null){
         sqa.addClause("receipt_date>=?");
         sqa.addPar(DateUtil.dateBracketLow(rcpDateFrom));
      }

      if (rcpDateTo!=null){
         sqa.addClause("receipt_date<?");
         sqa.addPar(DateUtil.dateBracketHigh(rcpDateTo));
      }

      if(receiptNo!=null) {
         sqa.addClause("upper(receipt_no) like ?");
         sqa.addLike(receiptNo);
      }

      if(description!=null) {
         sqa.addClause("upper(description) like ?");
         sqa.addLike(description);
      }

      if(branch!=null) {
         sqa.addClause("a.cc_code = ?");
         sqa.addPar(branch);
      }

      if(entity!=null) {
         sqa.addClause("upper(b.ent_name) like ?");
         sqa.addLike(entity);
      }
      

      list = sqa.getList(ARReceiptView.class);

      return list;
   }
   */
   

}

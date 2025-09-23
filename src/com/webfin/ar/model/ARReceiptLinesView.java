/***********************************************************************
 * Module:  com.webfin.ar.model.ARReceiptLinesView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:37:07 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.util.ListUtil;
import com.webfin.FinCodec;
import com.webfin.insurance.model.*;
import com.crux.common.model.RecordAudit;
import com.webfin.gl.model.TitipanPremiExtracomptableView;
import com.webfin.gl.model.TitipanPremiReinsuranceView;
import com.webfin.gl.model.TitipanPremiView;

import java.math.BigDecimal;
import java.util.Date;

public class ARReceiptLinesView extends DTO implements RecordAudit {
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
      {"stCheck","check_flag"},
      {"stArInvoiceClaim","ar_invoice_claim"},
      {"dtReceiptDate","receipt_date"},
      {"stPolicyID","pol_id"},
      {"stReceiptNo","receipt_no"},
      {"dbTotalAmountPerLine","amount_per_line"},
      {"stARInvoiceDetailRootID","ar_invoice_dtl_root_id"},
      {"stTitipanPremiID","titipan_premi_id"},
      {"dbTitipanPremiAmount","titipan_premi_amount"},
      {"stExcessAccountID","xc_account_id"},
      {"dbExcessAmount","xc_amount"},
      {"stExcessDescription","xc_description"},
      {"stTitipanFlag","titipan_flag"},
      {"stAdvancePaymentFlag", "advance_payment_flag"},
      {"stAccountID", "account_id"},
      {"dbTitipanPremiTotalAmount","titipan_premi_total_amount"},
      {"dbTitipanPremiUsedAmount","titipan_premi_used_amount"},
      {"stNegativeFlag","negative_flag"},
      {"stOtherFlag","other_f"},
      {"stManualFlag","manual_f"},
      {"stTransactionMethod","trx_method"},

      {"dbAmount100","amount_100"},
      {"dbAmount1","amount_1"},
      {"dbAmount2","amount_2"},
      {"dbAmount3","amount_3"},
      {"dbAmount4","amount_4"},
      {"dbAmount5","amount_5"},
      {"dbAmount6","amount_6"},
      {"dbTaxAmount100","tax_amount_100"},
      {"dbTaxAmount1","tax_amount_1"},
      {"dbTaxAmount2","tax_amount_2"},
      {"dbTaxAmount3","tax_amount_3"},
      {"dbTaxAmount4","tax_amount_4"},
      {"dbTaxAmount5","tax_amount_5"},
      {"dbTaxAmount6","tax_amount_6"},

      {"stCheck1","check_flag_1"},
      {"stCheck2","check_flag_2"},
      {"stCheck3","check_flag_3"},
      {"stCheck4","check_flag_4"},
      {"stCheck5","check_flag_5"},
      {"stCheck6","check_flag_6"},
      
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
   private String stNegativeFlag;
   private String stCheck;
   private DTOList arInvoiceDetail;
   private boolean stLock = true;
   
   public DTOList objects;
   private Class clObjectClass;
   
   private String stArInvoiceClaim;
   private Date dtReceiptDate;
   private String stPolicyID;
   private String stReceiptNo;
   private BigDecimal dbTotalAmountPerLine;
   private String stARInvoiceDetailRootID;
   private String stTitipanPremiID;
   private BigDecimal dbTitipanPremiAmount;
   private DTOList gls;
   private String stExcessAccountID;
   private BigDecimal dbExcessAmount;
   private String stExcessDescription;
   private String stTitipanFlag;
   private DTOList listTitipan;
   private String stAdvancePaymentFlag;
   private String stAccountID;
   private BigDecimal dbTitipanPremiTotalAmount;
   private BigDecimal dbTitipanPremiUsedAmount;
   private String stOtherFlag;
   private String stManualFlag;
   private String stTransactionMethod;

   private BigDecimal dbAmount100;
   private BigDecimal dbAmount1;
   private BigDecimal dbAmount2;
   private BigDecimal dbAmount3;
   private BigDecimal dbAmount4;
   private BigDecimal dbAmount5;
   private BigDecimal dbAmount6;

   private BigDecimal dbTaxAmount100;
   private BigDecimal dbTaxAmount1;
   private BigDecimal dbTaxAmount2;
   private BigDecimal dbTaxAmount3;
   private BigDecimal dbTaxAmount4;
   private BigDecimal dbTaxAmount5;
   private BigDecimal dbTaxAmount6;

   private String stCheck1;
   private String stCheck2;
   private String stCheck3;
   private String stCheck4;
   private String stCheck5;
   private String stCheck6;

    public String getStCheck1() {
        return stCheck1;
    }

    public void setStCheck1(String stCheck1) {
        this.stCheck1 = stCheck1;
    }

    public String getStCheck2() {
        return stCheck2;
    }

    public void setStCheck2(String stCheck2) {
        this.stCheck2 = stCheck2;
    }

    public String getStCheck3() {
        return stCheck3;
    }

    public void setStCheck3(String stCheck3) {
        this.stCheck3 = stCheck3;
    }

    public String getStCheck4() {
        return stCheck4;
    }

    public void setStCheck4(String stCheck4) {
        this.stCheck4 = stCheck4;
    }

    public String getStCheck5() {
        return stCheck5;
    }

    public void setStCheck5(String stCheck5) {
        this.stCheck5 = stCheck5;
    }

    public String getStCheck6() {
        return stCheck6;
    }

    public void setStCheck6(String stCheck6) {
        this.stCheck6 = stCheck6;
    }

    public BigDecimal getDbAmount1() {
        return dbAmount1;
    }

    public void setDbAmount1(BigDecimal dbAmount1) {
        this.dbAmount1 = dbAmount1;
    }

    public BigDecimal getDbAmount100() {
        return dbAmount100;
    }

    public void setDbAmount100(BigDecimal dbAmount100) {
        this.dbAmount100 = dbAmount100;
    }

    public BigDecimal getDbAmount2() {
        return dbAmount2;
    }

    public void setDbAmount2(BigDecimal dbAmount2) {
        this.dbAmount2 = dbAmount2;
    }

    public BigDecimal getDbAmount3() {
        return dbAmount3;
    }

    public void setDbAmount3(BigDecimal dbAmount3) {
        this.dbAmount3 = dbAmount3;
    }

    public BigDecimal getDbAmount4() {
        return dbAmount4;
    }

    public void setDbAmount4(BigDecimal dbAmount4) {
        this.dbAmount4 = dbAmount4;
    }

    public BigDecimal getDbAmount5() {
        return dbAmount5;
    }

    public void setDbAmount5(BigDecimal dbAmount5) {
        this.dbAmount5 = dbAmount5;
    }

    public BigDecimal getDbAmount6() {
        return dbAmount6;
    }

    public void setDbAmount6(BigDecimal dbAmount6) {
        this.dbAmount6 = dbAmount6;
    }

    public BigDecimal getDbTaxAmount1() {
        return dbTaxAmount1;
    }

    public void setDbTaxAmount1(BigDecimal dbTaxAmount1) {
        this.dbTaxAmount1 = dbTaxAmount1;
    }

    public BigDecimal getDbTaxAmount100() {
        return dbTaxAmount100;
    }

    public void setDbTaxAmount100(BigDecimal dbTaxAmount100) {
        this.dbTaxAmount100 = dbTaxAmount100;
    }

    public BigDecimal getDbTaxAmount2() {
        return dbTaxAmount2;
    }

    public void setDbTaxAmount2(BigDecimal dbTaxAmount2) {
        this.dbTaxAmount2 = dbTaxAmount2;
    }

    public BigDecimal getDbTaxAmount3() {
        return dbTaxAmount3;
    }

    public void setDbTaxAmount3(BigDecimal dbTaxAmount3) {
        this.dbTaxAmount3 = dbTaxAmount3;
    }

    public BigDecimal getDbTaxAmount4() {
        return dbTaxAmount4;
    }

    public void setDbTaxAmount4(BigDecimal dbTaxAmount4) {
        this.dbTaxAmount4 = dbTaxAmount4;
    }

    public BigDecimal getDbTaxAmount5() {
        return dbTaxAmount5;
    }

    public void setDbTaxAmount5(BigDecimal dbTaxAmount5) {
        this.dbTaxAmount5 = dbTaxAmount5;
    }

    public BigDecimal getDbTaxAmount6() {
        return dbTaxAmount6;
    }

    public void setDbTaxAmount6(BigDecimal dbTaxAmount6) {
        this.dbTaxAmount6 = dbTaxAmount6;
    }

    public String getStTransactionMethod() {
        return stTransactionMethod;
    }

    public void setStTransactionMethod(String stTransactionMethod) {
        this.stTransactionMethod = stTransactionMethod;
    }

   public boolean isLock() {
      return stLock;
   }
   
   public boolean getStLock() {
      return stLock;
   }

   public void setStLock(boolean stLock) {
      this.stLock = stLock;
   }
   
   public boolean isCheck() {
      return Tools.isYes(stCheck);
   }
   
   public String getStCheck() {
      return stCheck;
   }

   public void setStCheck(String stCheck) {
      this.stCheck = stCheck;
   }
   
   public boolean isNegative() {
      return "Y".equalsIgnoreCase(stNegativeFlag);
   }
   
   public void setStNegativeFlag(String stNegativeFlag) {
      this.stNegativeFlag = stNegativeFlag;
   }
   
   public String getStNegativeFlag() {
      return stNegativeFlag;
   }
   
   public String getStNosurathutang() {
      return stNosurathutang;
   }

   public void setStNosurathutang(String stNosurathutang) {
      this.stNosurathutang = stNosurathutang;
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

   public boolean isInvoice() {
      return FinCodec.ARReceiptLineType.INVOICE.equalsIgnoreCase(getStLineType());
   }

   public boolean isNote() {
      return FinCodec.ARReceiptLineType.NOTE.equalsIgnoreCase(getStLineType());
   }

   public BigDecimal getDbActInvAmount() {
      return getInvoice().getDbAmount();
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
   	 //final ARReceiptLinesView old = getOldReceiptLines();
         //return BDUtil.sub(getDbAmountSettledAct(),old==null?null:old.getDbAmount()); 
         return getDbAmountSettledAct();  
   }

   private ARReceiptLinesView getOldReceiptLines() {
      return (ARReceiptLinesView) getOld();
   }

   public BigDecimal getDbAmountSettledAct() {
       //return getInvoiceDetail().getDbAmountSettled();
       if(stInvoiceDetailID!=null) return getInvoiceDetail().getDbAmountSettled();
       else return null;
   }

   public BigDecimal getDbOutstandingAmount() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.add(getDbOutstandingAmountAct2(),old==null?null:old.getDbAmount());
      //return getDbOutstandingAmountAct();
   }
 
   public BigDecimal getDbOutstandingAmountAct() {
       if(stInvoiceDetailID!=null) return getInvoiceDetail().getDbOustandingAmount();
       else return null;
   }
   
   public BigDecimal getDbOutstandingAmount2() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.add(getDbOutstandingAmountAct2(),old==null?null:old.getDbAmount());
      //return getDbOutstandingAmountAct();
   }
   
   public BigDecimal getDbOutstandingAmountAct2() {
      if (isComission())
         return getInvoiceDetail().getDbOustandingAmount();

      return getInvoice().getDbOutstandingAmount();

   }
   
   public BigDecimal getDbAmountSettled2() {
      final ARReceiptLinesView old = getOldReceiptLines();
      return BDUtil.sub(getDbAmountSettledAct2(),old==null?null:old.getDbAmount());
   }

   public BigDecimal getDbAmountSettledAct2() {
      if (isComission())
         return getInvoiceDetail().getDbAmountSettled();

      return getInvoice().getDbAmountSettled();
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
         setArSettlementExcess((ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stARSettlementExcessID));

      return arSettlementExcess;
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
   
   public DTOList getARInvoiceDetails() {
      loadARInvoiceDetails();
      return arInvoiceDetail;
   }

   public void setARInvoiceDetails(DTOList arInvoiceDetail) {
      this.arInvoiceDetail = arInvoiceDetail;
   }

   public void loadARInvoiceDetails() {
      //if (!isAutoLoadEnabled()) return;
      try {
         if (arInvoiceDetail==null)
            arInvoiceDetail = ListUtil.getDTOListFromQuery(
                    "select * from ar_invoice_details where ar_invoice_id = ? order by ar_invoice_dtl_id",
                    new Object [] {stInvoiceID},
                    ARInvoiceDetailView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   
   public DTOList getObjects() {
      loadObjects();
      return objects;
   }

   private void loadObjects() {
      try {
         if (objects == null) {
            objects = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_obj where pol_id  = ?",
                    new Object [] {getInvoice().getStAttrPolicyID()},
                    InsurancePolicyObjDefaultView.class
            );
          }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

   }

  public void setObjects(DTOList objects) {
      this.objects = objects;
   }
   
   /*
  public DTOList getObjects() {
      return objects;
   }*/
  
  private DTOList arInvoice;
   
   public DTOList getARInvoice() {
      loadARInvoice();
      return arInvoice;
   }

   public void setARInvoice(DTOList arInvoice) {
      this.arInvoice = arInvoice;
   }

   public void loadARInvoice() {
      //if (!isAutoLoadEnabled()) return;
      try {
         if (arInvoice==null)
            arInvoice = ListUtil.getDTOListFromQuery(
                    "select * from ar_invoice where ar_invoice_id = ? ",
                    new Object [] {stInvoiceID},
                    ARInvoiceView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   public void setStArInvoiceClaim(String stArInvoiceClaim) {
      this.stArInvoiceClaim = stArInvoiceClaim;
   }

   public String getStArInvoiceClaim() {
      return stArInvoiceClaim;
   }

    public Date getDtReceiptDate() {
        return dtReceiptDate;
    }

    public void setDtReceiptDate(Date dtReceiptDate) {
        this.dtReceiptDate = dtReceiptDate;
    }

    public String getStReceiptNo() {
        return stReceiptNo;
    }

    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public BigDecimal getDbTotalAmountPerLine() {
        return dbTotalAmountPerLine;
    }

    public void setDbTotalAmountPerLine(BigDecimal dbTotalAmountPerLine) {
        this.dbTotalAmountPerLine = dbTotalAmountPerLine;
    }

    public String getStARInvoiceDetailRootID() {
        return stARInvoiceDetailRootID;
    }

    public void setStARInvoiceDetailRootID(String stARInvoiceDetailRootID) {
        this.stARInvoiceDetailRootID = stARInvoiceDetailRootID;
    }
    
    public InsurancePolicyView getPolicy() {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stPolicyID);
    }

    public String getStTitipanPremiID() {
        return stTitipanPremiID;
    }

    public void setStTitipanPremiID(String stTitipanPremiID) {
        this.stTitipanPremiID = stTitipanPremiID;
    }

    public BigDecimal getDbTitipanPremiAmount()
    {
        return dbTitipanPremiAmount;
    }

    public void setDbTitipanPremiAmount(BigDecimal dbTitipanPremiAmount)
    {
        this.dbTitipanPremiAmount = dbTitipanPremiAmount;
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
        if (stReceiptID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_receipt_lines where receipt_id = ? and line_type = ? and ar_invoice_id = ? order by ar_rcl_id",
                    new Object [] {stReceiptID, receiptLineType, stInvoiceID},
                    ARReceiptLinesView.class
                    );
        
        return null;
    }

    public String getStExcessAccountID()
    {
        return stExcessAccountID;
    }

    public void setStExcessAccountID(String stExcessAccountID)
    {
        this.stExcessAccountID = stExcessAccountID;
    }

    public BigDecimal getDbExcessAmount()
    {
        return dbExcessAmount;
    }

    public void setDbExcessAmount(BigDecimal dbExcessAmount)
    {
        this.dbExcessAmount = dbExcessAmount;
    }

    public String getStExcessDescription()
    {
        return stExcessDescription;
    }

    public void setStExcessDescription(String stExcessDescription)
    {
        this.stExcessDescription = stExcessDescription;
    }

    public void setArSettlementExcess(ARSettlementExcessView arSettlementExcess) {
        this.arSettlementExcess = arSettlementExcess;
    }

    public String getStTitipanFlag() {
        return stTitipanFlag;
    }

    public void setStTitipanFlag(String stTitipanFlag) {
        this.stTitipanFlag = stTitipanFlag;
    }
    
    public boolean isTitipan(){
        return Tools.isYes(stTitipanFlag);
    }
    
    private DTOList arInvoiceParentID;
    
    public DTOList getARInvoiceDetailsParentID(String stInvoiceID) {
        try {
            if (arInvoiceParentID == null)
                arInvoiceParentID = ListUtil.getDTOListFromQuery(
                        "select * from ar_invoice_details where ar_invoice_id = ? ",
                        new Object[]{stInvoiceID},
                        ARInvoiceDetailView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return arInvoiceParentID;
    }
    
    public void setARInvoiceParentID(DTOList arInvoiceParentID) {
        this.arInvoiceParentID = arInvoiceParentID;
    }

    public TitipanPremiView getTitipanPremi() {
        return (TitipanPremiView) DTOPool.getInstance().getDTO(TitipanPremiView.class, stTitipanPremiID);
    }

    public DTOList getListTitipan()  throws Exception {
        if (listTitipan==null) listTitipan=loadDetailsTitipan(FinCodec.ARReceiptLineType.TITIPAN);
        if (listTitipan==null) listTitipan=new DTOList();
        return listTitipan;
    }

    public void setListTitipan(DTOList listTitipan) {
        this.listTitipan = listTitipan;
    }
    
    
    private DTOList loadDetailsTitipan(String receiptLineType) throws Exception {
        if (stReceiptID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_receipt_lines where receipt_id = ? and line_type = ? and ar_rcl_ref_id = ?",
                    new Object [] {stReceiptID, receiptLineType, stReceiptLinesID},
                    ARReceiptLinesView.class
                    );
        
        return null;
    }
    
    public String getStAdvancePaymentFlag() {
        return stAdvancePaymentFlag;
    }

    public void setStAdvancePaymentFlag(String stAdvancePaymentFlag) {
        this.stAdvancePaymentFlag = stAdvancePaymentFlag;
    }
    
    public boolean isAdvancePayment(){
        return Tools.isYes(stAdvancePaymentFlag);
    }
 
    public String getStAccountID() {
        return stAccountID;
    }

    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    public BigDecimal getDbTitipanPremiTotalAmount() {
        return dbTitipanPremiTotalAmount;
    }

    public void setDbTitipanPremiTotalAmount(BigDecimal dbTitipanPremiTotalAmount) {
        this.dbTitipanPremiTotalAmount = dbTitipanPremiTotalAmount;
    }

    public BigDecimal getDbTitipanPremiUsedAmount() {
        return dbTitipanPremiUsedAmount;
    }

    public void setDbTitipanPremiUsedAmount(BigDecimal dbTitipanPremiUsedAmount) {
        this.dbTitipanPremiUsedAmount = dbTitipanPremiUsedAmount;
    }

    public TitipanPremiView getTitipanPremi(String titipanID) {
        return (TitipanPremiView) DTOPool.getInstance().getDTO(TitipanPremiView.class, stTitipanPremiID);
    }

    public String getStInvoicePolid() throws Exception {

        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select (select string_agg(a.receipt_no||'_'||b.description||'_'||to_char(a.receipt_date, 'dd/mm/yyyy'),', ')) as receipt_no "
                + "from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id and b.amount_settled is not null "
                + "inner join ar_trx_line c on c.ar_trx_line_id = b.ar_trx_line_id and c.category in ('BROKERAGE','COMMISSION','HFEE','PREMIG','TAXBROK','TAXCOMM','TAXHFEE') "
                + "where a.attr_pol_id = ? and a.ar_trx_type_id in (5,6,7,11) ",
                new Object[]{stPolicyID},
                ARInvoiceView.class).getDTO();
        return iv.getStReceiptNo();
    }

    public String getHistoryTrx() throws Exception {

        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                " select ( select string_agg(a.receipt_no||'_'||a.description||'_'||to_char(a.receipt_date, 'dd/mm/yyyy'),', ')) as receipt_no "
                + "from ( select a.ar_invoice_id,a.invoice_no,b.description,getpremi(b.amount_settled is not null,b.amount_settled,c.amount_settled) as amount_settled, "
                + "getname(b.amount_settled = b.amount,a.receipt_no,c.receipt_no) as receipt_no, "
                + "getperiod(b.amount_settled = b.amount,a.receipt_date,c.receipt_date) as receipt_date  "
                + "from ar_invoice a inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id "
                + "left join ( select b.root_id,b.description,b.amount_settled,a.receipt_no,a.receipt_date "
                + "from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line c on c.ar_trx_line_id = b.ar_trx_line_id "
                + "where a.attr_pol_id = ? and b.amount_settled is not null and b.root_id is not null "
                + "and c.category in ('BROKERAGE','COMMISSION','HFEE','PREMIG','TAXBROK','TAXCOMM','TAXHFEE') "
                + ") c on c.root_id = b.ar_invoice_dtl_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = b.ar_trx_line_id "
                + "where a.attr_pol_id = ? and a.ar_trx_type_id in (5,6,7) "
                + "and d.category not in ('STAMPDUTY','PCOST') "
                + "order by a.ar_invoice_id,b.ar_invoice_dtl_id "
                + ") a where a.ar_invoice_id = ? ",
                new Object[]{stPolicyID, stPolicyID, stInvoiceID},
                ARInvoiceView.class).getDTO();
        return iv.getStReceiptNo();
    }

    /**
     * @return the stOtherFlag
     */
    public String getStOtherFlag() {
        return stOtherFlag;
    }

    /**
     * @param stOtherFlag the stOtherFlag to set
     */
    public void setStOtherFlag(String stOtherFlag) {
        this.stOtherFlag = stOtherFlag;
    }

    public boolean isOthers(){
        return Tools.isYes(stOtherFlag);
    }

    /**
     * @return the stManualFlag
     */
    public String getStManualFlag() {
        return stManualFlag;
    }

    /**
     * @param stManualFlag the stManualFlag to set
     */
    public void setStManualFlag(String stManualFlag) {
        this.stManualFlag = stManualFlag;
    }

    public boolean isManual(){
        return Tools.isYes(stManualFlag);
    }

    public TitipanPremiExtracomptableView getTitipanPremiPolisKhusus(String titipanID) {
        return (TitipanPremiExtracomptableView) DTOPool.getInstance().getDTO(TitipanPremiExtracomptableView.class, stTitipanPremiID);
    }

    public TitipanPremiExtracomptableView getTitipanPremiPolisKhusus() {
        return (TitipanPremiExtracomptableView) DTOPool.getInstance().getDTO(TitipanPremiExtracomptableView.class, stTitipanPremiID);
    }

    public TitipanPremiReinsuranceView getTitipanPremiReinsurance() {
        return (TitipanPremiReinsuranceView) DTOPool.getInstance().getDTO(TitipanPremiReinsuranceView.class, stTitipanPremiID);
    }

    public TitipanPremiReinsuranceView getTitipanPremiReinsurance(String titipanID) {
        return (TitipanPremiReinsuranceView) DTOPool.getInstance().getDTO(TitipanPremiReinsuranceView.class, stTitipanPremiID);
    }

    public boolean isCheck1() {
      return Tools.isYes(stCheck1);
   }

    public boolean isCheck2() {
      return Tools.isYes(stCheck2);
   }

    public boolean isCheck3() {
      return Tools.isYes(stCheck3);
   }

    public boolean isCheck4() {
      return Tools.isYes(stCheck4);
   }

    public boolean isCheck5() {
      return Tools.isYes(stCheck5);
   }

    public boolean isCheck6() {
      return Tools.isYes(stCheck6);
   }

}

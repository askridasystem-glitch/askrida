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
import com.webfin.gl.model.AccountView2;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.gl.util.GLUtil;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.time.DateTime;

public class ARReceiptView extends DTO implements RecordAudit {
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
     private final static transient LogManager logger = LogManager.getInstance(ARReceiptView.class);
    
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
        {"stPrintFlag","print_flag"},
        {"stFoxproReceiptNo","fox_rc_no"},
        {"stDetailsSize","details_size"},
        {"stTaxCode","tax_code"},
        {"stInsuranceTreatyTypeID","ins_treaty_type_id"},
        {"stYears","years"},
        {"stMonths","months"},
        {"stPrintCode", "print_code"},
        {"stPrintStamp", "print_stamp"},
        {"stIDRFlag", "idr_flag"},
        {"stReceiptNo2","receipt_no2"},
        {"stUserName","user_name*n"},
        {"stJournalType","journal_type"},
        {"stFilePhysic","file_physic"},
        {"stPolicyTypeID","pol_type_id"},
        {"stFileSlip","file_slip"},
        {"stReinsTypePayment","reins_type_payment"},
        {"stPolicyNo","pol_no"},
        {"stPolicyID","pol_id"},

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
    private String stFoxproReceiptNo;
    
    private String stPrintFlag;
    private String stDetailsSize;
    private String stTaxCode;
    private String stInsuranceTreatyTypeID;
    
    private String stMonths;
    private String stYears;
    private String stPrintStamp;
    private String stPrintCode;
    
    private DTOList titipan;
    
    private String stIDRFlag;
    private String stReceiptNo2;
    private String stUserName;
    private String stJournalType;
    private String stFilePhysic;

    private DTOList listHutangKomisi;
    private DTOList receiptDetails;
    private String stPolicyTypeID;
    private String stFileSlip;
    private String stReinsTypePayment;
    private String stPolicyNo;
    private String stPolicyID;

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

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

                if(iv!=null){
                    iv.getDetails();

                    arapinvoice = iv;

                    return arapinvoice;
                }
                
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
    
    public void recalculateBackup() throws Exception {
        
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
                    /*if(rcInvoiceDetail.isTaxComm()||rcInvoiceDetail.isTaxBrok()||rcInvoiceDetail.isTaxHFee()){
                    }else{
                        tot = BDUtil.sub(tot, d.getDbAmount());
                    }*/
                    tot = BDUtil.sub(tot, d.getDbAmount());
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
        
        //gla.setCode('Y',getStEntityID());
        
        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }
            
        
        EntityView entity = getEntity();
        
        if(entity!=null)
            gla.setDesc("Y",entity.getStShortName());
        
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
                
                ivd.setStGLAccountID(gla.getAccountID(ref1));
                ivd.setStDescription(gla.getPreviewDesc());
                
            }
            
        }
        
        if(stAccountEntityID!=null) setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        else setStAccountID(getPaymentMethod().getStGLAccountID());
            
        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());
        
        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());
        
        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");
        
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
        
        setStDetailsSize(String.valueOf(details.size()));
        
        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);
            
            //if(!rl.isCheck()) continue;
            
            BigDecimal eamt = rl.getDbEnteredAmount();
            
            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);
            
            //eamt=BDUtil.mul(eamt, rl.getDbCurrencyRate(),2);
            
            eamt=BDUtil.div(eamt, getDbCurrencyRate());
            
            rl.setDbAmount(eamt);
            
            //tot = BDUtil.add(tot, rl.getDbAmount());
            
            BigDecimal totalEnteredAmountPerInvoice = null;
            
            if(rl.getStARSettlementExcessID()==null){
                rl.setStExcessAccountID(null);
                rl.setStExcessDescription(null);
                rl.setDbExcessAmount(null);
            }
            
            final DTOList details = rl.getDetails();
            
            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);
                
                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                }else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){

                        if(!d.isManual())
                            d.setDbEnteredAmount(d.getDbInvoiceAmount());

                        if(rcInvoiceDetail!=null){
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), d.getInvoice().getDbCurrencyRate(),2));
                        }else{
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                        }
                        
                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }

                //d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                
                if(!d.isCheck()) continue;
                
                if(rcInvoiceDetail!=null){
                    boolean isTax = rcInvoiceDetail.isTaxComm() || rcInvoiceDetail.isTaxBrok() || rcInvoiceDetail.isTaxHFee();
                
                    if(d.isComission()||rcInvoiceDetail.isNegative()){
                        /*if(rcInvoiceDetail.isTaxComm()||rcInvoiceDetail.isTaxBrok()||rcInvoiceDetail.isTaxHFee()){
                        }else{
                            tot = BDUtil.sub(tot, d.getDbAmount());
                        }*/
                        tot = BDUtil.sub(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.sub(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }else{
                        tot = BDUtil.add(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }
                }else{
                    if(d.isNegative()){
                        tot = BDUtil.sub(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.sub(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }else{
                        tot = BDUtil.add(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }
                }

            }
            
            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            BigDecimal totalTitipanPerLine = null;
            BigDecimal totalTitipanUsedPerLine = null;

            final DTOList titipan = rl.getListTitipan();

            for (int k = 0; k < titipan.size(); k++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(k);

                totalTitipanPerLine = BDUtil.add(totalTitipanPerLine, titip.getDbTitipanPremiAmount());

                totalTitipanUsedPerLine = BDUtil.add(totalTitipanUsedPerLine, titip.getDbTitipanPremiUsedAmount());
            }

            if(rl.getListTitipan().size()>0){
                if(rl.getInvoice()==null){

                    final DTOList detailsRealisasi = rl.getDetails();

                    for (int j = 0; j < detailsRealisasi.size(); j++) {
                        ARReceiptLinesView d = (ARReceiptLinesView) detailsRealisasi.get(j);

                        d.setDbEnteredAmount(totalTitipanUsedPerLine);
                        d.setDbAmount(totalTitipanUsedPerLine);
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                    }
                }
            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            rl.setDbTitipanPremiTotalAmount(totalTitipanPerLine);
            
            if(rl.getStARSettlementExcessID()!=null){
                final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();
            
                final boolean negative = arSettlementExcess.isNegative();
                
                if (negative)
                    tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                else
                    tot = BDUtil.add(tot, rl.getDbExcessAmount());
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
        
        dbAmountRemain = BDUtil.sub(BDUtil.mul(dbEnteredAmount, getDbCurrencyRate(),2), dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");
       
      gla.setCode('C',"C");
      gla.setDesc("C","C");*/
        
        gla.setCode('B',getStCostCenterCode());
        //gla.setCode('B',"B");
        
        //gla.setCode('Y',getStEntityID());
        
        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }
        
        if(getStCostCenterCode()==null)
            gla.setCode('B',"00");
        
        EntityView entity = getEntity();
        
        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());
        
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

        if(ivd==null)
            throw new RuntimeException("Detail invoice tidak ditemukan invoice id : "+ iv.getStARInvoiceID());
        
        if (getPaymentMethod()!=null) {
            ivd.setStGLAccountID(getPaymentMethod().getStGLAccountID());
            ivd.setStDescription(getPaymentMethod().getStDescription());
        }else{
            if(getReceiptClass()!=null&&stAccountEntityID!=null){
                final String ref1 = getReceiptClass().getStReference1().trim();

                ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                ivd.setStDescription(gla.getPreviewDesc());
                
            }
        }
         
        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null){
            setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
            //setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        }
        
        if(getPaymentMethod()!=null) 
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());
        
        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());
        
        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());
        
        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");
        
        //}
    }
    
    public void recalculatePajak() throws Exception {
        
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
    
    public EntityView getPaymentEntity() {
        return (EntityView) DTOPool.getInstance().getDTORO(EntityView.class, stAccountEntityID);
    }
    
    public void setDbAmountApplied(BigDecimal dbAmountApplied) {
        this.dbAmountApplied = dbAmountApplied;
    }
    
    public BigDecimal getDbAmountApplied() {
        return dbAmountApplied;
    }
    
    public void validate() throws Exception {

        DateTime dt = new DateTime();
        DateTime oneMonthBefore = dt.minusMonths(1);
        boolean withinCurrentMonth = DateUtil.getDateStr(getDtReceiptDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(oneMonthBefore.toDate(), "yyyyMM")) || DateUtil.getDateStr(getDtReceiptDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(dt.toDate(), "yyyyMM"));
        
        //if(!withinCurrentMonth)
            //throw new RuntimeException("Hanya bisa input data bulan sebelumnya/bulan berjalan");
        
        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);
            
            //if (Tools.compare(rcl.getDbInvoiceAmount(),rcl.getDbAmount())<0) throw new Exception("Invalid Invoice payment amount : "+rcl.getDbAmount()+" > "+rcl.getDbInvoiceAmount());
            
            if(rcl.getStARSettlementExcessID()!=null){
                rcl.setArSettlementExcess(null);
                
                if(rcl.getStExcessAccountID()==null)
                    throw new RuntimeException("Akun selisih bayar harus diisi");

                if(rcl.getARSettlementExcess().getDbMaximumAmount()!=null)
                    if(BDUtil.biggerThan(rcl.getDbExcessAmount(), rcl.getARSettlementExcess().getDbMaximumAmount()))
                        throw new RuntimeException("Nilai Selisih Bayar "+rcl.getDbExcessAmount()+" > Nilai Maksimum : "+rcl.getARSettlementExcess().getDbMaximumAmount());
            }
            
                boolean withinCurrentMonthDetail = DateUtil.getDateStr(getDtReceiptDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(rcl.getDtReceiptDate(), "yyyyMM"));

                String ket = "Tanggal Bayar Data ke "+ (i+1) +" tidak sama dengan bulan & tahun transaksi";

                if(rcl.getInvoice()!=null) ket = "Tanggal Bayar Polis "+ rcl.getInvoice().getStAttrPolicyNo() +" tidak sama dengan bulan & tahun transaksi";

                if(!withinCurrentMonthDetail)
                    throw new RuntimeException(ket);

                DateTime tanggalBayar = new DateTime(rcl.getDtReceiptDate());
                DateTime currentDate = new DateTime(new Date());

                if(tanggalBayar.isAfter(currentDate))
                    throw new RuntimeException("Tanggal Bayar Polis "+ rcl.getInvoice().getStAttrPolicyNo() +" tidak boleh melewati tanggal hari ini");
        }
        
        if (Tools.compare(dbAmountRemain,BDUtil.zero)<0) throw new Exception("Pembayaran salah, selisih= "+ dbAmountRemain+",dibayar= "+dbAmountApplied);
        
        if (Tools.compare(dbAmountRemain,BDUtil.zero)>0) throw new Exception("Pembayaran salah, selisih= "+ dbAmountRemain+",dibayar= "+dbAmountApplied);
        
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
    
    private DTOList loadDetailsGLS(String receiptLineType) throws Exception {
        if (stARReceiptID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_receipt_lines where receipt_id = ? and line_type = ? and ar_invoice_id = ?",
                    new Object [] {stARReceiptID, receiptLineType, },
                    ARReceiptLinesView.class
                    );
        
        return null;
    }
    
    private DTOList loadDetails(String receiptLineType) throws Exception {
        if (stARReceiptID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_receipt_lines where receipt_id = ? and line_type = ? order by ar_rcl_id",
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
        String bankCode;
        bankCode = Tools.getDigitRightJustified(stBankCode,5);
        if(stAccountEntityID!=null)
            bankCode = getPaymentEntity().getStGLCode();
        
        String counterKey =
                DateUtil.getYear2Digit(getDtReceiptDate())+
                DateUtil.getMonth2Digit(getDtReceiptDate());
        
        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode,1));

        
        //rn = Tools.getDigitRightJustified(rn,5);
        rn = StringTools.leftPad(rn,'0', 5);
        
        //A 01 10 1010 00000 00001
        //C 0410 2020 35061 00001
        //012345678901234567890123
        // C 12 04 2020 20002 00018
        stReceiptNo =
                methodCode +
                counterKey +
                ccCode +
                ccCode +
                bankCode +
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
    
    public void recalculatePembayaranKomisi2() throws Exception {
        
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
        
        setStDetailsSize(String.valueOf(details.size()));
        
        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);
            
            //if(!rl.isCheck()) continue;
            /*
            BigDecimal eamt = rl.getDbEnteredAmount();

            if(rl.getInvoice()!=null){
                eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);
            }

            eamt=BDUtil.div(eamt, getDbCurrencyRate());
            
            rl.setDbAmount(eamt);
            */

            //tes
            BigDecimal eamt = rl.getDbEnteredAmount();

            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);

            //eamt=BDUtil.mul(eamt, rl.getDbCurrencyRate(),2);

            eamt=BDUtil.div(eamt, getDbCurrencyRate());

            rl.setDbAmount(eamt);
            //end tes

            //tot = BDUtil.add(tot, rl.getDbAmount());
            BigDecimal totalEnteredAmountPerInvoice = null;
            
            final DTOList details = rl.getDetails();
            
            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);
                
                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                    d.setDbAmount(BDUtil.mul(d.getDbAmount(), d.getInvoice().getDbCurrencyRate(),2));
                }else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){
                        d.setDbEnteredAmount(d.getDbInvoiceAmount());

                        if(rcInvoiceDetail!=null){
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), d.getInvoice().getDbCurrencyRate(),2));
                        }else{
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                        }
                        d.setDbAmount(d.getDbEnteredAmount());
                        
                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }
                
                //d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                
                if(!d.isCheck()) continue;
                
                //boolean isTax = rcInvoiceDetail.isTaxComm() || rcInvoiceDetail.isTaxBrok() || rcInvoiceDetail.isTaxHFee();

                tot = BDUtil.add(tot, d.getDbEnteredAmount());
                totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());

                if(rl.getStARSettlementExcessID()!=null){
                    final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

                    final boolean negative = arSettlementExcess.isNegative();

                    if (negative)
                        tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                    else
                        tot = BDUtil.add(tot, rl.getDbExcessAmount());
                }

                /*
                if(d.isComission()||rcInvoiceDetail.isNegative()){
                    
                    tot = BDUtil.sub(tot, d.getDbAmount());
                    totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbAmount());
                }else{
                 }*/
                
                //if(BDUtil.lesserThanZero(tot))
                    //tot = BDUtil.negate(tot);
            }
            
            BigDecimal totalTitipanPerLine = null;
            
            final DTOList titipan = rl.getListTitipan();
            
            for (int k = 0; k < titipan.size(); k++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(k);
                
                totalTitipanPerLine = BDUtil.add(totalTitipanPerLine, titip.getDbTitipanPremiAmount());
            }
            
            //rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);
            
            rl.setDbTitipanPremiTotalAmount(totalTitipanPerLine);
            
            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);
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
        
        //gla.setCode('Y',getStEntityID());
        
        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }
        
        EntityView entity = getEntity();
        
        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());
        
        //if (BDUtil.biggerThanZero(dbAmountRemain)) {
        /*
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
            if(getReceiptClass()!=null && stAccountEntityID!=null){
                final String ref1 = getReceiptClass().getStReference1().trim();

                if(ref1!=null){
                    ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                    ivd.setStDescription(gla.getPreviewDesc());
                }

            }
            
        }
        */

        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null){
            setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
            //setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        } 
        
        if(getPaymentMethod()!=null) 
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());

        /*
        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());
        
        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());
        
        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");
        */
        
        //}
    }
    
    public void recalculatePembayaranPajak() throws Exception {
        
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
        
        setStDetailsSize(String.valueOf(details.size()));

        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);
            
            BigDecimal eamt = rl.getDbEnteredAmount();

            ARInvoiceView invoice = rl.getInvoice();
            
            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);
            
            //eamt=BDUtil.div(eamt, getDbCurrencyRate());
            
            //rl.setDbAmount(eamt);

            //eamt=BDUtil.div(eamt, getDbCurrencyRate());

            rl.setDbAmount(eamt);
            
            //tot = BDUtil.add(tot, rl.getDbAmount());
            BigDecimal totalEnteredAmountPerInvoice = null;
            
            final DTOList details = rl.getDetails();
            
            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);
                
                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                //d.setDbAmount(BDUtil.div(d.getDbEnteredAmount(), invoice.getDbCurrencyRate(),2));

                d.setDbAmount(d.getDbEnteredAmount());
                
                boolean isTax = rcInvoiceDetail.isTaxComm() || rcInvoiceDetail.isTaxBrok() || rcInvoiceDetail.isTaxHFee();

                tot = BDUtil.add(tot, d.getDbAmount());

                totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbAmount());

                //d.setDbEnteredAmount(d.getDbInvoiceAmount());

                //test
                d.setDbEnteredAmount(BDUtil.mul(d.getDbInvoiceAmount(), d.getInvoice().getDbCurrencyRate(),2));

                //if(BDUtil.lesserThanZero(tot))
                    //tot = BDUtil.negate(tot);
            }
            
            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);
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
        
        //gla.setCode('Y',getStEntityID());
        
        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }
        
        EntityView entity = getEntity();
        
        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());
        
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
                
                ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                ivd.setStDescription(gla.getPreviewDesc());
            }
            
        }
        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null) setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
        
        if(getPaymentMethod()!=null) 
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());

        
        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());
        
        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());
        
        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");
        
        //}
    }
    
    public String getStPrintFlag() {
        return stPrintFlag;
    }

    public void setStPrintFlag(String stPrintFlag) {
        this.stPrintFlag = stPrintFlag;
    }
    
    public void recalculateLKS() throws Exception {
        
        updateStatus();
        
        GLUtil.Applicator gla = new GLUtil.Applicator();
        
        dbAmount = BDUtil.mul(dbEnteredAmount,dbCurrencyRate,2);

        BigDecimal tot = null;
        
        getDetails();
        
        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);

            BigDecimal eamt = rl.getDbEnteredAmount();
            
            eamt=BDUtil.mul(eamt, getDbCurrencyRate(),2);
            
            eamt=BDUtil.div(eamt, getDbCurrencyRate());
            
            rl.setDbAmount(eamt);

            BigDecimal totalEnteredAmountPerInvoice = null;
            
            final DTOList details = rl.getDetails();
            
            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);

                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();
                
                /*
                if(!d.isCheck()) d.setDbEnteredAmount(BDUtil.zero);
                else if(d.isCheck()) d.setDbEnteredAmount(d.getDbInvoiceAmount());*/
                
                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                }else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){
                        d.setDbEnteredAmount(d.getDbInvoiceAmount());
                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }

                d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                
                if(!d.isCheck()) continue;

                if(d.isComission() || d.isNegative()){
                    tot = BDUtil.sub(tot, d.getDbAmount());
                    totalEnteredAmountPerInvoice = BDUtil.sub(totalEnteredAmountPerInvoice, d.getDbAmount());
                }else{
                    tot = BDUtil.add(tot, d.getDbAmount());
                    totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbAmount());
                }
            }
            
            BigDecimal totalTitipanPerLine = null;
            BigDecimal totalTitipanUsedPerLine = null;
            
            final DTOList titipan = rl.getListTitipan();
            
            for (int k = 0; k < titipan.size(); k++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(k);
                
                totalTitipanPerLine = BDUtil.add(totalTitipanPerLine, titip.getDbTitipanPremiAmount());

                totalTitipanUsedPerLine = BDUtil.add(totalTitipanUsedPerLine, titip.getDbTitipanPremiUsedAmount());
            }

            if(rl.getListTitipan().size()>0){
                if(rl.getInvoice()==null){

                    final DTOList detailsRealisasi = rl.getDetails();

                    for (int j = 0; j < detailsRealisasi.size(); j++) {
                        ARReceiptLinesView d = (ARReceiptLinesView) detailsRealisasi.get(j);

                        d.setDbEnteredAmount(totalTitipanUsedPerLine);
                        d.setDbAmount(totalTitipanUsedPerLine);
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                    }
                }
            }
            
            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);
            
            if(rl.getStARSettlementExcessID()!=null){
                final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();
            
                final boolean negative = arSettlementExcess.isNegative();
                
                if (negative)
                    tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                else
                    tot = BDUtil.add(tot, rl.getDbExcessAmount());
            }
            
            rl.setDbTitipanPremiTotalAmount(totalTitipanPerLine);

            
            
        }

        dbAmountApplied = tot;
        
        dbAmountRemain = BDUtil.sub(dbEnteredAmount, dbAmountApplied);
        
      /*gla.setCode('A',"A");
      gla.setDesc("A","A");
       
      gla.setCode('C',"C");
      gla.setDesc("C","C");*/
        
        gla.setCode('B',getStCostCenterCode());
        //gla.setCode('B',"B");
        
        //gla.setCode('Y',getStEntityID());
        
        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }
        
        EntityView entity = getEntity();
        
        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());
        
        if(stAccountEntityID!=null) setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        
        if(getPaymentMethod()!=null) 
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());
        
        //if (BDUtil.biggerThanZero(dbAmountRemain)) {
        /*
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
                
                ivd.setStGLAccountID(gla.getAccountID(ref1));
                ivd.setStDescription(gla.getPreviewDesc());
                
            }
            
        }
     
        if(stAccountEntityID!=null) setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        
        if(getPaymentMethod()!=null) 
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());
        
        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());
        
        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());
        
        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");*/

    }

    public String getStFoxproReceiptNo()
    {
        return stFoxproReceiptNo;
    }

    public void setStFoxproReceiptNo(String stFoxproReceiptNo)
    {
        this.stFoxproReceiptNo = stFoxproReceiptNo;
    }

    public String getStDetailsSize() {
        return stDetailsSize;
    }

    public void setStDetailsSize(String stDetailsSize) {
        this.stDetailsSize = stDetailsSize;
    }

    public String getStTaxCode() {
        return stTaxCode;
    }

    public void setStTaxCode(String stTaxCode) {
        this.stTaxCode = stTaxCode;
    }
    
    public boolean isPPH21Payment(){
        if(stTaxCode==null) return false;
        
        return stTaxCode.equalsIgnoreCase("PPH21");
    }
    
    public boolean isPPH23Payment(){
        if(stTaxCode==null) return false;
        
        return stTaxCode.equalsIgnoreCase("PPH23");
    }

    /**
     * @return the stInsuranceTreatyTypeID
     */
    public String getStInsuranceTreatyTypeID() {
        return stInsuranceTreatyTypeID;
    }

    /**
     * @param stInsuranceTreatyTypeID the stInsuranceTreatyTypeID to set
     */
    public void setStInsuranceTreatyTypeID(String stInsuranceTreatyTypeID) {
        this.stInsuranceTreatyTypeID = stInsuranceTreatyTypeID;
    }
    
     public void validatePembayaranPremi() throws Exception {
    
        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);
            
            if(rcl.getInvoice()!=null)
                if (Tools.compare(rcl.getDbInvoiceAmount(),rcl.getDbAmount())<0) throw new Exception("Jumlah pembayaran : "+rcl.getDbAmount()+" > tagihan "+rcl.getDbInvoiceAmount());
            
            if(rcl.getStARSettlementExcessID()!=null){
                rcl.setArSettlementExcess(null);
                
                if(rcl.getARSettlementExcess().getDbMaximumAmount()!=null)
                    if(BDUtil.biggerThan(rcl.getDbExcessAmount(), rcl.getARSettlementExcess().getDbMaximumAmount()))
                        throw new RuntimeException("Nilai Selisih Bayar "+rcl.getDbExcessAmount()+" > Nilai Maksimum : "+rcl.getARSettlementExcess().getDbMaximumAmount());
            }
            
            if(rcl.getInvoice()!=null){
                boolean withinCurrentMonthDetail = DateUtil.getDateStr(getDtReceiptDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(rcl.getDtReceiptDate(), "yyyyMM"));

                if(!withinCurrentMonthDetail)
                    throw new RuntimeException("Tanggal Bayar Polis "+ rcl.getInvoice().getStAttrPolicyNo() +" tidak sama dengan bulan & tahun transaksi");

                DateTime tanggalBayar = new DateTime(rcl.getDtReceiptDate());
                DateTime currentDate = new DateTime(new Date());

                if(tanggalBayar.isAfter(currentDate))
                    throw new RuntimeException("Tanggal Bayar Polis "+ rcl.getInvoice().getStAttrPolicyNo() +" tidak boleh melewati tanggal hari ini");

            }

            if(rcl.getInvoice()==null){
                boolean withinCurrentMonthDetail = DateUtil.getDateStr(getDtReceiptDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(rcl.getDtReceiptDate(), "yyyyMM"));

                if(!withinCurrentMonthDetail)
                    throw new RuntimeException("Tanggal Bayar Data ke "+ (i+1) +" tidak sama dengan bulan & tahun transaksi");
            }
            
            final DTOList titipan = rcl.getListTitipan();
            for (int j = 0; j < titipan.size(); j++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                BigDecimal sisaTitipan = BDUtil.isNegative(titip.getDbTitipanPremiAmount())?BDUtil.negate(titip.getDbTitipanPremiAmount()):titip.getDbTitipanPremiAmount();
                BigDecimal titipanPremiDipakai = BDUtil.isNegative(titip.getDbTitipanPremiUsedAmount())?BDUtil.negate(titip.getDbTitipanPremiUsedAmount()):titip.getDbTitipanPremiUsedAmount();

                sisaTitipan = BDUtil.round(sisaTitipan, 2);
                titipanPremiDipakai = BDUtil.round(titipanPremiDipakai, 2);

               if(BDUtil.biggerThan(titipanPremiDipakai, sisaTitipan)){

                   TitipanPremiView titipCek = titip.getTitipanPremi(titip.getStTitipanPremiID());

                   throw new RuntimeException("Jumlah realisasi "+ titipanPremiDipakai +" lebih besar dari sisa titipan "+ sisaTitipan +" -> "+ titipCek.getStTransactionNo() +" counter "+titipCek.getStCounter());
               }
                    
            }
                       
        }


        
        if (Tools.compare(dbAmountRemain,BDUtil.zero)<0) throw new Exception("Pembayaran salah, selisih= "+ dbAmountRemain+",dibayar= "+dbAmountApplied);
        
        if (Tools.compare(dbAmountRemain,BDUtil.zero)>0) throw new Exception("Pembayaran salah, selisih= "+ dbAmountRemain+",dibayar= "+dbAmountApplied);
        
        if (isNote())
            if (Tools.compare(dbAmountRemain,BDUtil.zero)!=0) throw new Exception("Settlement Note is not balanced");
        
        
    }
     
     public AccountView2 getAccounts() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stAccountID);
    }
     
    public String getStMonths() {
        return stMonths;
    }

    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    public String getStYears() {
        return stYears;
    }

    public void setStYears(String stYears) {
        this.stYears = stYears;
    }
    
    private DTOList loadDetailsTitipan(String receiptLineType) throws Exception {
        if (stARReceiptID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_receipt_lines where receipt_id = ? and line_type = ?",
                    new Object [] {stARReceiptID, receiptLineType},
                    ARReceiptLinesView.class
                    );
        
        return null;
    }

    public DTOList getTitipan() throws Exception {
        if(titipan!=null) return titipan;
        if (titipan==null) titipan=loadDetailsTitipan(FinCodec.ARReceiptLineType.TITIPAN);
        if (titipan==null) titipan=new DTOList();
        return titipan;
    }

    public void setTitipan(DTOList titipan) {
        this.titipan = titipan;
    }
    
    public String getStPrintStamp() {
        return stPrintStamp;
    }

    public void setStPrintStamp(String stPrintStamp) {
        this.stPrintStamp = stPrintStamp;
    }

    public String getStPrintCode() {
        return stPrintCode;
    }

    public void setStPrintCode(String stPrintCode) {
        this.stPrintCode = stPrintCode;
    }
    
    public String getStIDRFlag() {
        return stIDRFlag;
    }

    public void setStIDRFlag(String stIDRFlag) {
        this.stIDRFlag = stIDRFlag;
    }
    
    public String generateReceiptNo2() throws Exception {
        
        //if (stReceiptNo!=null) return;
        String no = null;
        
        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);
        final String methodCode = Tools.getDigitRightJustified(getReceiptClass().getStMethodCode(),1);
        String stBankCode = getPaymentMethod()==null?"00000":getPaymentMethod().getStBankCode();
        String bankCode;
        bankCode = Tools.getDigitRightJustified(stBankCode,5);
        if(stAccountEntityID!=null)
            bankCode = getPaymentEntity().getStGLCode();
        
        String counterKey =
                DateUtil.getYear2Digit(getDtReceiptDate())+
                DateUtil.getMonth2Digit(getDtReceiptDate());
        
        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode,1));
        
        //rn = Tools.getDigitRightJustified(rn,5);
        rn = StringTools.leftPad(rn,'0', 5);
        
        //A 01 10 1010 00000 00001
        //C 0410 2020 35061 00001
        //012345678901234567890123
        // C 12 04 2020 20002 00018
        no =
                methodCode +
                counterKey +
                ccCode +
                ccCode +
                bankCode +
                rn;
        
        return no;

    }

    public String getStReceiptNo2() {
        return stReceiptNo2;
    }

    public void setStReceiptNo2(String stReceiptNo2) {
        this.stReceiptNo2 = stReceiptNo2;
    }

    /**
     * @return the listHutangKomisi
     */
    public DTOList getListHutangKomisi(String policyID) throws Exception {
        if(listHutangKomisi==null) listHutangKomisi = loadHutangKomisi(policyID);

        return listHutangKomisi;
    }

    /**
     * @param listHutangKomisi the listHutangKomisi to set
     */
    public void setListHutangKomisi(DTOList listHutangKomisi) {
        this.listHutangKomisi = listHutangKomisi;
    }

    private DTOList loadHutangKomisi(String stPolicyID) throws Exception {
        if (stPolicyID!=null)
            return ListUtil.getDTOListFromQuery(
                    "select * from ar_invoice where ar_trx_type_id = 11 and substr(refid2,0,4) = 'POL' "+
                    " and used_flag is null and amount_settled is null and attr_pol_id = ? ",
                    new Object [] {stPolicyID},
                    ARInvoiceView.class
                    );

        return null;
    }

    /**
     * @return the stUserName
     */
    public String getStUserName() {
        return stUserName;
    }

    /**
     * @param stUserName the stUserName to set
     */
    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }

    /**
     * @return the stJournalType
     */
    public String getStJournalType() {
        return stJournalType;
    }

    /**
     * @param stJournalType the stJournalType to set
     */
    public void setStJournalType(String stJournalType) {
        this.stJournalType = stJournalType;
    }

    public boolean isJournalOffset(){
        return stJournalType.equalsIgnoreCase("OFFSET");
    }

    public boolean isJournalNonOffset(){
        return stJournalType.equalsIgnoreCase("NON_OFFSET");
    }

    public void recalculateKlaim() throws Exception {

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

        setStDetailsSize(String.valueOf(details.size()));

        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);

            //if(!rl.isCheck()) continue;

            BigDecimal eamt = rl.getDbEnteredAmount();

            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);

            eamt=BDUtil.mul(eamt, rl.getDbCurrencyRate(),2);

            eamt=BDUtil.div(eamt, getDbCurrencyRate());

            rl.setDbAmount(eamt);

            //tot = BDUtil.add(tot, rl.getDbAmount());

            BigDecimal totalEnteredAmountPerInvoice = null;

            if(rl.getStARSettlementExcessID()==null){
                rl.setStExcessAccountID(null);
                rl.setStExcessDescription(null);
                rl.setDbExcessAmount(null);
            }

            final DTOList details = rl.getDetails();

            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);

                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                }/*else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){
                        d.setDbEnteredAmount(d.getDbInvoiceAmount());
                        d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }*/

                //d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));

                if(!d.isCheck()) continue;

                if(rcInvoiceDetail!=null){
                    boolean isTax = rcInvoiceDetail.isTaxComm() || rcInvoiceDetail.isTaxBrok() || rcInvoiceDetail.isTaxHFee();

                    if(d.isComission()||rcInvoiceDetail.isNegative()){
                        /*if(rcInvoiceDetail.isTaxComm()||rcInvoiceDetail.isTaxBrok()||rcInvoiceDetail.isTaxHFee()){
                        }else{
                            tot = BDUtil.sub(tot, d.getDbAmount());
                        }*/
                        tot = BDUtil.sub(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.sub(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }else{
                        tot = BDUtil.add(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }
                }else{
                    tot = BDUtil.add(tot, d.getDbEnteredAmount());
                    totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());

                }


                /*
                if(BDUtil.lesserThanZero(tot)){
                    tot = BDUtil.negate(tot);
                }*/

            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            if(rl.getStARSettlementExcessID()!=null){
                final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

                final boolean negative = arSettlementExcess.isNegative();

                if (negative)
                    tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                else
                    tot = BDUtil.add(tot, rl.getDbExcessAmount());
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

        dbAmountRemain = BDUtil.sub(BDUtil.mul(dbEnteredAmount, getDbCurrencyRate(),2), dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");

      gla.setCode('C',"C");
      gla.setDesc("C","C");*/

        gla.setCode('B',getStCostCenterCode());
        //gla.setCode('B',"B");

        //gla.setCode('Y',getStEntityID());

        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }

        if(getStCostCenterCode()==null)
            gla.setCode('B',"00");

        EntityView entity = getEntity();

        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());

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

                ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                ivd.setStDescription(gla.getPreviewDesc());

            }

        }

        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null){
            setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
            //setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        }

        if(getPaymentMethod()!=null)
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());

        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());

        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());

        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");

        //}
    }

    public void recalculatePembayaranKomisi() throws Exception {

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

        setStDetailsSize(String.valueOf(details.size()));

        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);

            //if(!rl.isCheck()) continue;

            BigDecimal eamt = rl.getDbEnteredAmount();

            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);

            //eamt=BDUtil.mul(eamt, rl.getDbCurrencyRate(),2);

            eamt=BDUtil.div(eamt, getDbCurrencyRate());

            rl.setDbAmount(eamt);

            //tot = BDUtil.add(tot, rl.getDbAmount());

            BigDecimal totalEnteredAmountPerInvoice = null;

            if(rl.getStARSettlementExcessID()==null){
                rl.setStExcessAccountID(null);
                rl.setStExcessDescription(null);
                rl.setDbExcessAmount(null);
            }

            final DTOList details = rl.getDetails();

            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);

                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                }else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){
                        d.setDbEnteredAmount(d.getDbInvoiceAmount());

                        if(rcInvoiceDetail!=null){
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), d.getInvoice().getDbCurrencyRate(),2));
                        }else{
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                        }

                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }

                //d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));

                if(!d.isCheck()) continue;

                tot = BDUtil.add(tot, d.getDbEnteredAmount());
                totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());

            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            BigDecimal totalTitipanPerLine = null;
            BigDecimal totalTitipanUsedPerLine = null;

            final DTOList titipan = rl.getListTitipan();

            for (int k = 0; k < titipan.size(); k++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(k);

                totalTitipanPerLine = BDUtil.add(totalTitipanPerLine, titip.getDbTitipanPremiAmount());

                totalTitipanUsedPerLine = BDUtil.add(totalTitipanUsedPerLine, titip.getDbTitipanPremiUsedAmount());
            }

            if(rl.getListTitipan().size()>0){
                if(rl.getInvoice()==null){

                    final DTOList detailsRealisasi = rl.getDetails();

                    for (int j = 0; j < detailsRealisasi.size(); j++) {
                        ARReceiptLinesView d = (ARReceiptLinesView) detailsRealisasi.get(j);

                        d.setDbEnteredAmount(totalTitipanUsedPerLine);
                        d.setDbAmount(totalTitipanUsedPerLine);
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                    }
                }
            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            rl.setDbTitipanPremiTotalAmount(totalTitipanPerLine);

            if(rl.getStARSettlementExcessID()!=null){
                final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

                final boolean negative = arSettlementExcess.isNegative();

                if (negative)
                    tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                else
                    tot = BDUtil.add(tot, rl.getDbExcessAmount());
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

        dbAmountRemain = BDUtil.sub(BDUtil.mul(dbEnteredAmount, getDbCurrencyRate(),2), dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");

      gla.setCode('C',"C");
      gla.setDesc("C","C");*/

        gla.setCode('B',getStCostCenterCode());
        //gla.setCode('B',"B");

        //gla.setCode('Y',getStEntityID());

        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }

        if(getStCostCenterCode()==null)
            gla.setCode('B',"00");

        EntityView entity = getEntity();

        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());

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

                ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                ivd.setStDescription(gla.getPreviewDesc());

            }
        }

        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null){
            setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
            //setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        }

        if(getPaymentMethod()!=null)
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());

        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());

        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());

        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");

        //}
    }

    public void recalculateInject() throws Exception {

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

        setStDetailsSize(String.valueOf(details.size()));

        for (int i = 0; i < details.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);

            //if(!rl.isCheck()) continue;

            BigDecimal eamt = rl.getDbEnteredAmount();

            //eamt=BDUtil.mul(eamt, rl.getInvoice().getDbCurrencyRate(),2);

            //eamt=BDUtil.mul(eamt, rl.getDbCurrencyRate(),2);

            eamt=BDUtil.div(eamt, getDbCurrencyRate());

            rl.setDbAmount(eamt);

            //tot = BDUtil.add(tot, rl.getDbAmount());

            BigDecimal totalEnteredAmountPerInvoice = null;

            if(rl.getStARSettlementExcessID()==null){
                rl.setStExcessAccountID(null);
                rl.setStExcessDescription(null);
                rl.setDbExcessAmount(null);
            }

            final DTOList details = rl.getDetails();

            for (int j = 0; j < details.size(); j++) {
                ARReceiptLinesView d = (ARReceiptLinesView) details.get(j);

                final ARInvoiceDetailView rcInvoiceDetail = d.getInvoiceDetail();

                if(!d.isCheck()){
                    d.setDbEnteredAmount(BDUtil.zero);
                }else if(d.isCheck()){
                    if(d.getDbInvoiceAmount()!=null){
                        d.setDbEnteredAmount(d.getDbInvoiceAmount());

                        if(rcInvoiceDetail!=null){
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), d.getInvoice().getDbCurrencyRate(),2));
                        }else{
                            d.setDbEnteredAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));
                        }

                    }else{
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                        d.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbAmount(d.getDbInvoiceAmount());
                        rl.setDbInvoiceAmount(d.getDbInvoiceAmount());
                    }
                }

                //d.setDbAmount(BDUtil.mul(d.getDbEnteredAmount(), getDbCurrencyRate(),2));

                if(!d.isCheck()) continue;

                if(rcInvoiceDetail!=null){
                    boolean isTax = rcInvoiceDetail.isTaxComm() || rcInvoiceDetail.isTaxBrok() || rcInvoiceDetail.isTaxHFee();

                    if(d.isComission()||rcInvoiceDetail.isNegative()){
                        /*if(rcInvoiceDetail.isTaxComm()||rcInvoiceDetail.isTaxBrok()||rcInvoiceDetail.isTaxHFee()){
                        }else{
                            tot = BDUtil.sub(tot, d.getDbAmount());
                        }*/
                        tot = BDUtil.sub(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.sub(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }else{
                        tot = BDUtil.add(tot, d.getDbEnteredAmount());
                        totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());
                    }
                }else{
                    tot = BDUtil.add(tot, d.getDbEnteredAmount());
                    totalEnteredAmountPerInvoice = BDUtil.add(totalEnteredAmountPerInvoice, d.getDbEnteredAmount());

                }

            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            BigDecimal totalTitipanPerLine = null;
            BigDecimal totalTitipanUsedPerLine = null;

            final DTOList titipan = rl.getListTitipan();

            for (int k = 0; k < titipan.size(); k++) {
                ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(k);

                totalTitipanPerLine = BDUtil.add(totalTitipanPerLine, titip.getDbTitipanPremiAmount());

                totalTitipanUsedPerLine = BDUtil.add(totalTitipanUsedPerLine, titip.getDbTitipanPremiUsedAmount());
            }

            if(rl.getListTitipan().size()>0){
                if(rl.getInvoice()==null){

                    final DTOList detailsRealisasi = rl.getDetails();

                    for (int j = 0; j < detailsRealisasi.size(); j++) {
                        ARReceiptLinesView d = (ARReceiptLinesView) detailsRealisasi.get(j);

                        d.setDbEnteredAmount(totalTitipanUsedPerLine);
                        d.setDbAmount(totalTitipanUsedPerLine);
                        d.setDbInvoiceAmount(d.getDbEnteredAmount());
                    }
                }
            }

            rl.setDbTotalAmountPerLine(totalEnteredAmountPerInvoice);

            rl.setDbTitipanPremiTotalAmount(totalTitipanPerLine);

            if(rl.getStARSettlementExcessID()!=null){
                final ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

                final boolean negative = arSettlementExcess.isNegative();

                if (negative)
                    tot = BDUtil.sub(tot, rl.getDbExcessAmount());
                else
                    tot = BDUtil.add(tot, rl.getDbExcessAmount());
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

        dbEnteredAmount = dbAmountApplied;

        dbAmountRemain = BDUtil.sub(BDUtil.mul(dbEnteredAmount, getDbCurrencyRate(),2), dbAmountApplied);

      /*gla.setCode('A',"A");
      gla.setDesc("A","A");

      gla.setCode('C',"C");
      gla.setDesc("C","C");*/

        gla.setCode('B',getStCostCenterCode());
        //gla.setCode('B',"B");

        //gla.setCode('Y',getStEntityID());

        if(stAccountEntityID!=null){
            gla.setCode('G',getEntity2(stAccountEntityID).getStGLCode().trim());
            gla.setCode('Y',getEntity2(stAccountEntityID).getStGLCode().trim());
        }

        if(getStCostCenterCode()==null)
            gla.setCode('B',"00");

        EntityView entity = getEntity();

        if(stAccountEntityID!=null)
            gla.setDesc("Y",getEntity2(stAccountEntityID).getStShortName());

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

                ivd.setStGLAccountID(gla.getAccountIDUsingLike(ref1));
                ivd.setStDescription(gla.getPreviewDesc());

            }
        }

        //setStAccountID(ivd.getStGLAccountID());
        if(stAccountEntityID!=null){
            setStAccountID(gla.getAccountIDUsingLike(getReceiptClass().getStReference1().trim()));
            //setStAccountID(gla.getAccountID(getReceiptClass().getStReference1().trim()));
        }

        if(getPaymentMethod()!=null)
            if(stAccountEntityID==null) setStAccountID(getPaymentMethod().getStGLAccountID());

        ivd.setStComissionFlag("N");
        ivd.setStNegativeFlag("N");
        ivd.setDbEnteredAmount(iv.getDbAmount());

        boolean hasAmount = !BDUtil.isZero(iv.getDbAmount());

        iv.setStHideFlag(hasAmount?"N":"Y");
        iv.setStNonPostableFlag("Y");

        //}
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public DTOList getReceiptDetails(String arInvoiceID) throws Exception {
        if(receiptDetails!=null) return receiptDetails;
        if (receiptDetails==null) receiptDetails=loadReceiptDetails(arInvoiceID, FinCodec.ARReceiptLineType.INVOICE);
        if (receiptDetails==null) receiptDetails = new DTOList();
        return receiptDetails;
    }

    /**
     * @param receiptDetails the receiptDetails to set
     */
    public void setReceiptDetails(DTOList receiptDetails) {
        this.receiptDetails = receiptDetails;
    }

    private DTOList loadReceiptDetails(String arInvoiceID,String receiptLineType) throws Exception {
            return ListUtil.getDTOListFromQuery(
                    "select * from ar_receipt_lines where ar_invoice_id = ? and line_type = ? order by ar_rcl_id",
                    new Object [] {arInvoiceID, receiptLineType},
                    ARReceiptLinesView.class
                    );
    }



    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    /**
     * @return the stFileSlip
     */
    public String getStFileSlip() {
        return stFileSlip;
    }

    /**
     * @param stFileSlip the stFileSlip to set
     */
    public void setStFileSlip(String stFileSlip) {
        this.stFileSlip = stFileSlip;
    }

    /**
     * @return the stReinsTypePayment
     */
    public String getStReinsTypePayment() {
        return stReinsTypePayment;
    }

    /**
     * @param stReinsTypePayment the stReinsTypePayment to set
     */
    public void setStReinsTypePayment(String stReinsTypePayment) {
        this.stReinsTypePayment = stReinsTypePayment;
    }

}

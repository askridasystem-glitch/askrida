/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvoiceView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:05:46 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.common.model.RecordAudit;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.crux.lang.LanguageManager;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.util.GLUtil;
import com.webfin.FinCodec;
import com.webfin.insurance.model.*;

import java.math.BigDecimal;
import java.util.Date;

public class ARInvoiceView extends DTO implements RecordAudit {
    
    public static boolean exludeComission = false;
    
     private final static transient LogManager logger = LogManager.getInstance(ARInvoiceView.class);
    
   /*
   CREATE TABLE ar_invoice
(
  ar_invoice_id int8 NOT NULL,
  invoice_no varchar(64),
  amount numeric,
  invoice_date timestamp,
  due_date timestamp,
  amount_settled numeric,
  ccy varchar(3),
  ar_cust_id int8,
  posted_flag varchar(1),
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
ALTER TABLE ar_invoice ADD COLUMN ent_id int8;
    
  CONSTRAINT ar_invoice_pk PRIMARY KEY (ar_invoice_id)
    
    
ALTER TABLE ar_invoice ADD COLUMN attr_pol_no varchar(32);
ALTER TABLE ar_invoice ADD COLUMN mutation_date timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_name varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_0 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_1 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_address varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi_total numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_type_id int8;
ALTER TABLE ar_invoice ADD COLUMN attr_quartal varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_underwrit varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_id int8;
    
ALTER TABLE ar_invoice ADD COLUMN commit_flag varchar(1);
ALTER TABLE ar_invoice ADD COLUMN approved_flag varchar(1);
    
ALTER TABLE ar_invoice ADD COLUMN refx0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refx1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refy0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refy1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refz0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refz1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refa0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refa1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refc0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refc1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refd0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refd1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refe0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refe1 varchar(64);
    
    
    

)
    */
    
    //MARK BUAT BERSIH2X 
    //public static String tableName = "ar_invoice_temp";
    public static String tableName = "ar_invoice"; 

    public transient static String comboFields[] = {"ent_id","no_surat_hutang","attr_pol_name"};
     
    public static String fieldMap[][] = {
        {"stARInvoiceID","ar_invoice_id*pk"},
        {"stInvoiceNo","invoice_no"},
        {"dbAmount","amount"},
        {"dbEnteredAmount","entered_amount"},
        {"dtInvoiceDate","invoice_date"},
        {"dtDueDate","due_date"},
        {"dbAmountSettled","amount_settled"},
        {"stCurrencyCode","ccy"},
        {"stARCustomerID","ar_cust_id"},
        {"stPostedFlag","posted_flag"},
        {"stCancelFlag","cancel_flag"},
        {"stGLARAccountID","gl_ar"},
        //{"stGLARAccountCode","gl_ar_code"},
        //{"stGLAPAccountCode","gl_ap_code"},
        {"stARTransactionTypeID","ar_trx_type_id"},
        {"stEntityID","ent_id"},
        {"stInvoiceType","invoice_type"},
        {"stDescription","description"},
        {"stCostCenterCode","cc_code"},
        {"stNegativeFlag","negative_flag"},
        {"dbCurrencyRate","ccy_rate"},
        
        {"stAttrPolicyNo","attr_pol_no"},
        {"dtMutationDate","mutation_date"},
        {"stAttrPolicyName","attr_pol_name"},
        {"dtAttrPolicyPeriodStart","attr_pol_per_0"},
        {"dtAttrPolicyPeriodEnd","attr_pol_per_1"},
        {"stAttrPolicyAddress","attr_pol_address"},
        {"dbAttrPolicyTSI","attr_pol_tsi"},
        {"dbAttrPolicyTSITotal","attr_pol_tsi_total"},
        {"stAttrPolicyTypeID","attr_pol_type_id"},
        {"stAttrQuartal","attr_quartal"},
        {"stAttrUnderwriting","attr_underwrit"},
        {"stAttrPolicyID","attr_pol_id"},
        {"stPremiFlag","f_premi"},
        {"stPolicyID","pol_id"},
        {"dtPaymentDate","payment_date*n"},
        
        {"stCommitFlag","commit_flag"},
        {"stApprovedFlag","approved_flag"},
        {"stNoJournalFlag","no_journal_flag"},
        {"stGLCode","gl_code*n"},
        
        {"stReferenceX0","refx0"},
        {"stReferenceX1","refx1"},
        {"stReferenceY0","refy0"},
        {"stReferenceY1","refy1"},
        {"stReferenceZ0","refz0"},
        {"stReferenceZ1","refz1"},
        {"stReferenceA0","refa0"},
        {"stReferenceA1","refa1"},
        {"stReferenceC0","refc0"},
        {"stReferenceC1","refc1"},
        {"stReferenceD0","refd0"},
        {"stReferenceD1","refd1"},
        {"stReferenceE0","refe0"},
        {"stReferenceE1","refe1"},
        
        {"stHideFlag","f_hide"},
        {"stNonPostableFlag","f_npostable"},
        {"stRefID0","refid0"},
        {"stRefID1","refid1"},
        {"stRefID2","refid2"},
        {"stRefID3","refid3"},
        {"stNoSuratHutang","no_surat_hutang"},
        {"dtSuratHutangPeriodFrom","surat_hutang_period_from"},
        {"dtSuratHutangPeriodTo","surat_hutang_period_to"},
        {"dtReceipt","receipt_date"},
        {"stRefInvoiceID","ref_invoice_id"},
        {"dbTagihanAmount","tagihan_amount*n"},
        {"stClaimNo","claim_no"},
        {"stClaimName","claim_name"},
        {"stClaimCoinsID","claim_coins_id"},
        {"stClaimCoinsName","claim_coins_name"},
        {"stClaimCoinsAddress","claim_coins_address"},
        {"stDLANo","dla_no*n"},
        {"dbDisc","disc*n"},
        {"dbPcost","bpol*n"},
        {"dbStampDuty","bmat*n"},
        {"dbPremi","premi*n"},
        {"dbComm","comm*n"},
        {"dbFee","fee*n"},
        {"dbPremiEntered","premi_entered*n"},
        {"dbCommEntered","comm_entered*n"},
        {"dbFeeEntered","fee_entered*n"},
        {"stClaimStatus","claim_status"},
        {"stFilePhysic","file_physic"},
        {"dtTransDate","trans_prod_date"},
        {"stReference3","ref3*n"},
        {"stReference4","ref4*n"},
        {"dbAmountFeeBase","amountFeebase*n"},
        {"stUsedFlag","used_flag"},
        {"stReceiptNo","receipt_no"},
        {"stReferenceNo","reference_no"},
        {"dtReferenceDate1","refdate1"},
        {"stReferenceNumber1","refnum1"},
        {"stNotifFlag","notif_flag"},
        {"stReinsurerName","reins_name*n"},
        {"stReinsEntID","reins_ent_id"},
    };
    
    public ARInvoiceView() {
    }
    
/*
ALTER TABLE ar_invoice ADD COLUMN f_hide varchar(1);
ALTER TABLE ar_invoice ADD COLUMN f_postable varchar(1);
 
 */
    
    private String stRefInvoiceID;
    private String stHideFlag;
    private String stNonPostableFlag;
    private String stNoJournalFlag;
    private String stRefID0;
    private String stRefID1;
    private String stRefID2;
    private String stRefID3;
    private String stNoSuratHutang;
    
    private String stReferenceX0;
    private String stReferenceX1;
    private String stReferenceY0;
    private String stReferenceY1;
    private String stReferenceZ0;
    private String stReferenceZ1;
    private String stReferenceA0;
    private String stReferenceA1;
    private String stReferenceC0;
    private String stReferenceC1;
    private String stReferenceD0;
    private String stReferenceD1;
    private String stReferenceE0;
    private String stReferenceE1;
    
    private String stCommitFlag="Y";
    private String stApprovedFlag;
    private String stPolicyID;
    
    private String stPremiFlag;
    
    private String stGLCode;
    private String stARInvoiceID;
    private String stInvoiceNo;
    private BigDecimal dbAmount;
    private BigDecimal dbEnteredAmount;
    private Date dtInvoiceDate;
    private Date dtDueDate;
    private BigDecimal dbAmountSettled;
    private String stCurrencyCode;
    private String stARCustomerID;
    private String stPostedFlag;
    private String stCancelFlag;
    private String stGLARAccountID;
    private String stARTransactionTypeID;
    private String stEntityID;
    private String stInvoiceType;
    private String stDescription;
    private String stCostCenterCode;
    private String stNegativeFlag;
    private BigDecimal dbCurrencyRate;
    private DTOList details;
    private DTOList list;
    private DTOList list2;
    
    private String stAttrPolicyNo;
    private Date dtMutationDate;
    private String stAttrPolicyName;
    private Date dtAttrPolicyPeriodStart;
    private Date dtAttrPolicyPeriodEnd;
    private String stAttrPolicyAddress;
    private BigDecimal dbAttrPolicyTSI;
    private BigDecimal dbAttrPolicyTSITotal;
    private String stAttrPolicyTypeID;
    private String stAttrQuartal;
    private String stAttrUnderwriting;
    private String stAttrPolicyID;
    private String stGLARAccountDesc;
    private Date dtPaymentDate;
    private Date dtSuratHutangPeriodFrom;
    private Date dtSuratHutangPeriodTo;
    private BigDecimal dbTagihanAmount;
    private boolean taxTrue;
    private ARInvoiceView ref;
    private Date dtReceipt;
    
    public DTOList objects;
    private Class clObjectClass;
    
    private DTOList list3;
    
    private String stClaimNo;
    private String stClaimName;
    private String stClaimCoinsID;
    private String stClaimCoinsName;
    private String stClaimCoinsAddress;

    private String stDLANo;
    private BigDecimal dbPcost;
    private BigDecimal dbDisc;
    private BigDecimal dbStampDuty;

    private BigDecimal dbPremi;
    private BigDecimal dbComm;
    private BigDecimal dbFee;

    private BigDecimal dbPremiEntered;
    private BigDecimal dbCommEntered;
    private BigDecimal dbFeeEntered;
    private String stClaimStatus;
    private String stFilePhysic;
    private String stReference3;
    private String stReference4;
    
    private Date dtTransDate;
    
    private DTOList arInvoiceDetailsCoins;
    private DTOList listDetailsSuratHutang;
    private BigDecimal dbAmountFeeBase;
    private String stUsedFlag;
    private String stReceiptNo;
    private String stReferenceNo;
    private Date dtReferenceDate1;
    private String stReferenceNumber1;
    private String stNotifFlag;
    private String stReinsurerName;
    private String stReinsEntID;

    public String getStReinsEntID() {
        return stReinsEntID;
    }

    public void setStReinsEntID(String stReinsEntID) {
        this.stReinsEntID = stReinsEntID;
    }

    public String getStReinsurerName() {
        return stReinsurerName;
    }

    public void setStReinsurerName(String stReinsurerName) {
        this.stReinsurerName = stReinsurerName;
    }


    public String getStNotifFlag() {
        return stNotifFlag;
    }

    public void setStNotifFlag(String stNotifFlag) {
        this.stNotifFlag = stNotifFlag;
    }

    public Date getDtReferenceDate1() {
        return dtReferenceDate1;
    }

    public void setDtReferenceDate1(Date dtReferenceDate1) {
        this.dtReferenceDate1 = dtReferenceDate1;
    }

    public String getStReferenceNumber1() {
        return stReferenceNumber1;
    }

    public void setStReferenceNumber1(String stReferenceNumber1) {
        this.stReferenceNumber1 = stReferenceNumber1;
    }

    public Date getDtReceipt() {
        return dtReceipt;
    }
    
    public void setDtReceipt(Date dtReceipt) {
        this.dtReceipt = dtReceipt;
    }
    
    public ARInvoiceView getRef() {
        //if (stRefInvoiceDetailID!=null) throw new RuntimeException("ref object not linked");
        //return ref;
        return (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, stRefInvoiceID);
        
    }
    
    public void setRef(ARInvoiceView ref) {
        this.ref = ref;
    }
    
    public String getStRefInvoiceID(){
        if (stRefInvoiceID==null)
            if (ref!=null)
                stRefInvoiceID = ref.getStARInvoiceID();
        
        return stRefInvoiceID;
    }
    
    public void setStRefInvoiceID(String stRefInvoiceID) {
        this.stRefInvoiceID = stRefInvoiceID;
    }
    
    
    public Date getDtPaymentDate() {
        return dtPaymentDate;
    }
    
    public void setDtPaymentDate(Date dtPaymentDate) {
        this.dtPaymentDate = dtPaymentDate;
    }
    
    public Date getDtSuratHutangPeriodFrom() {
        return dtSuratHutangPeriodFrom;
    }
    
    public void setDtSuratHutangPeriodFrom(Date dtSuratHutangPeriodFrom) {
        this.dtSuratHutangPeriodFrom = dtSuratHutangPeriodFrom;
    }
    
    public Date getDtSuratHutangPeriodTo() {
        return dtSuratHutangPeriodTo;
    }
    
    public void setDtSuratHutangPeriodTo(Date dtSuratHutangPeriodTo) {
        this.dtSuratHutangPeriodTo = dtSuratHutangPeriodTo;
    }
    
    public String getStPolicyID() {
        return stPolicyID;
    }
    
    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }
    
    public void setStGLARAccountDesc(String stGLARAccountDesc) {
        this.stGLARAccountDesc = stGLARAccountDesc;
    }
    
    public String getStGLARAccountID() {
        return stGLARAccountID;
    }
    
    public void setStGLARAccountID(String stGLARAccountID) {
        this.stGLARAccountID = stGLARAccountID;
    }
    
    public DTOList getDetails() {
        if (details==null) {
            if (!isNew()) {
                try {
                    details = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice_details where ar_invoice_id = ? order by ar_invoice_dtl_id",
                            new Object[] {stARInvoiceID},
                            ARInvoiceDetailView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } 
        return details;
    }
    
    public void setDetails(DTOList details) {
        this.details = details;
    }
    
    public DTOList getList() {
        if (list==null) {
            if (!isNew()) {
                try {
                    list = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice where ar_invoice_id = ?",
                            new Object[] {stARInvoiceID},
                            ARInvoiceView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    /*
    public DTOList getList2() {
        if (list2==null) {
            if (!isNew()) {
                try {
                    list2 = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice where no_surat_hutang = ? and case when amount > 0 then amount else (amount*-1) end - coalesce(amount_settled,0) > 0",
                            new Object[] {stNoSuratHutang},
                            ARInvoiceView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list2;
    }*/
    
    public DTOList getList2() {
        if (list2==null) {
            if (!isNew()) {
                try {
                    String select = "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.no_surat_hutang = ? and coalesce(a.amount,0) <> 0 and a.amount_settled is null";

                    if(dtSuratHutangPeriodFrom!=null){
                        select = select + " and date_trunc('day',a.invoice_date) >= ?";
                    }

                    if(dtSuratHutangPeriodTo!=null){
                        select = select + " and date_trunc('day',a.invoice_date) <= ?";
                    }

                    select = select +" order by a.ar_invoice_id,b.ar_invoice_dtl_id";

                    if(dtSuratHutangPeriodFrom!=null && dtSuratHutangPeriodTo!=null){
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang, dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo},
                            HashDTO.class
                            );
                    }else{
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang},
                            HashDTO.class
                            );
                    }
                    
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list2;
    }
    
    public void setList2(DTOList list2) {
        this.list2 = list2;
    }
    
    public String getStARInvoiceID() {
        return stARInvoiceID;
    }
    
    public void setStARInvoiceID(String stARInvoiceID) {
        this.stARInvoiceID = stARInvoiceID;
    }
    
    public String getStInvoiceNo() {
        return stInvoiceNo;
    }
    
    public void setStInvoiceNo(String stInvoiceNo) {
        this.stInvoiceNo = stInvoiceNo;
    }
    
    public BigDecimal getDbAmount() {
        return dbAmount;
    }
    
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }
    
    public Date getDtInvoiceDate() {
        return dtInvoiceDate;
    }
    
    public BigDecimal getDbTagihanAmount() {
        return dbTagihanAmount;
    }
    
    public void setDbTagihanAmount(BigDecimal dbTagihanAmount) {
        this.dbTagihanAmount = dbTagihanAmount;
    }
    
    public void setDtInvoiceDate(Date dtInvoiceDate) {
        this.dtInvoiceDate = dtInvoiceDate;
    }
    
    public Date getDtDueDate() {
        return dtDueDate;
    }
    
    public void setDtDueDate(Date dtDueDate) {
        this.dtDueDate = dtDueDate;
    }
    
    public BigDecimal getDbAmountSettled() {
        return dbAmountSettled;
    }
    
    public void setDbAmountSettled(BigDecimal dbAmountSettled) {
        this.dbAmountSettled = dbAmountSettled;
    }
    
    public String getStCurrencyCode() {
        return stCurrencyCode;
    }
    
    public void setStCurrencyCode(String stCurrencyCode) {
        this.stCurrencyCode = stCurrencyCode;
    }
    
    public String getStARCustomerID() {
        return stARCustomerID;
    }
    
    public void setStARCustomerID(String stARCustomerID) {
        this.stARCustomerID = stARCustomerID;
    }
    
    public String getStPostedFlag() {
        return stPostedFlag;
    }
    
    public void setStPostedFlag(String stPostedFlag) {
        this.stPostedFlag = stPostedFlag;
    }
    
    public String getStCancelFlag() {
        return stCancelFlag;
    }
    
    public void setStCancelFlag(String stCancelFlag) {
        this.stCancelFlag = stCancelFlag;
    }
    
   /*
   public BigDecimal getDbOutstandingAmount() {
          BigDecimal dbAmount0 = null;
          BigDecimal dbAmountSettled0 = null;
          final DTOList invoiceDetail = getDetails();
    
          for (int i = 0; i < invoiceDetail.size(); i++) {
                        ARInvoiceDetailView invDetail = (ARInvoiceDetailView) invoiceDetail.get(i);
    
                                if (invDetail.isComission()) continue;
                                dbAmount0 = BDUtil.add(dbAmount0,invDetail.getDbAmount());
                                dbAmountSettled0 = BDUtil.add(dbAmountSettled0,invDetail.getDbAmountSettled());
      }
      setDbAmount(dbAmount0);
          setDbAmountSettled(dbAmountSettled0);
    
      return BDUtil.sub(getDbAmount(), getDbAmountSettled());
   }*/
    
    public BigDecimal getDbOutstandingAmount() {
        return BDUtil.sub(getDbAmount(), getDbAmountSettled());
    }
    
    public void setStARTransactionTypeID(String stARTransactionTypeID) {
        this.stARTransactionTypeID = stARTransactionTypeID;
    }
    
    public String getStARTransactionTypeID() {
        return stARTransactionTypeID;
    }
    
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }
    
    public String getStEntityID() {
        return stEntityID;
    }
    
    public void setStInvoiceType(String stInvoiceType) {
        this.stInvoiceType = stInvoiceType;
    }
    
    public String getStInvoiceType() {
        return stInvoiceType;
    }
    
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }
    
    public String getStDescription() {
        return stDescription;
    }
    
    public String getStEntityName() {
        final EntityView entity = getEntity();
        
        if (entity!=null) return entity.getStEntityName();
        
        return null;
    }
    
    public EntityView getEntity() {
        if (stEntityID==null) return null;
        return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
    }
    
    public String getStGLARAccountDesc() {
        
      /*if (stGLARAccountID==null) {
         return GLUtil.Chart.getInstance().getChartDescription(stGLAccountCodeTR);
      }*/
        
        if (stGLARAccountID==null)
            return stGLARAccountDesc;
        
        
        final AccountView account = getAccount();
        
        return account.getStAccountNo()+"/"+account.getStDescription();
    }
    
    public AccountView getAccount() {
        return (AccountView) DTOPool.getInstance().getDTO(AccountView.class, getStGLARAccountID());
    }
    
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }
    
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }
    
    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
    }
    
    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }
    
    public void validate() {
        
        //if (stGLARAccountID==null) throw new RuntimeException("AR/AP Account not defined");
        
        if (stGLARAccountID==null && stARTransactionTypeID==null) throw new RuntimeException("AR/AP TRX not defined");
        
        if (stCostCenterCode==null) throw new RuntimeException("You must supply cost center for invoice !");
        
        if (stCurrencyCode==null) throw new RuntimeException("You must supply currency code for invoice !");
        
        if (dbCurrencyRate==null) throw new RuntimeException("You must supply currency rate for invoice !");
        
        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
            
            if (det.isComission()) {
                if (det.getStEntityID()==null) throw new RuntimeException("Comission should be assigned to an agent entity");
            }
        }
    }
    
    public void recalculate() {
        if(details!=null){
            recalculate(false);
        }
        
    }
    
    public void recalculate( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
                
                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());
                    
                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));
                    
                }
                
                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));
                
                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);
                
                if (exludeComission)
                    if(det.isComission()) continue;
                
                
                if (det.isNegative()) {
                    
                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
                    
                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {
                    
                    tot=BDUtil.add(tot,amtIDR);
                    
                    totE=BDUtil.add(totE,amtafterTax);
                    
                }
                
            }
            
            
            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }
        
        if (isNegative()) {
            
            dbAmount = BDUtil.negate(dbAmount);
            
            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
            
        }
        
         //if (dbAmount!=null)
            //if ((Tools.compare(dbAmount,BDUtil.zero)<0)) 
            /*
             if(BDUtil.isNegative(dbAmount))
            {
                if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
                    setStInvoiceType(FinCodec.InvoiceType.AP);
                } else {
                    setStInvoiceType(FinCodec.InvoiceType.AR);
                }
            }*/
        
        if (!isHasBeenPosted()) {
            
            stGLARAccountID=null;
            
            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();
            
            //final String stGLAccountCode = getStGLAccountCode();
            
            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();
            
            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();
            
            final String policyTypeGLCode = getPolicyTypeGLCode();
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);
            
            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1()!=null?getStReferenceA1():"");
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            glApplicator.setDesc("K",getStRefID2());
            glApplicator.setDesc("M",DateUtil.getMonth(getDtInvoiceDate()).toUpperCase());
            glApplicator.setDesc("T",DateUtil.getYear(getDtInvoiceDate()));
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());
            
            String acc = getStGLAccountCode();
            
            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());
            
            acc = LanguageManager.getInstance().translate(acc);
            
            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }
            
         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);
          
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/
            
            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");
            
            final String polTypeID = getStAttrPolicyTypeID()!=null?getStAttrPolicyTypeID():null;
            
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                String accode = ivd.getStGLAccountCode();

                //Jika KSG & Macet & Restitusi Premi
                if(polTypeID!=null){

                    if(polTypeID.equalsIgnoreCase("87") || polTypeID.equalsIgnoreCase("88")){
                        if(getPolicy()!=null){
                            if(getPolicy().isStatusEndorse()){
                                if(BDUtil.lesserThanZero(ivd.getDbAmount())){
                                    if(ivd.getTrxLine().getStGLAccount2()!=null){

                                        EntityView ent = getEntity();

                                        String kodeSumbis = "0";

                                        if (ent.isBPD()) kodeSumbis = "1"; //BPD
                                        if (ent.getStRef2().equalsIgnoreCase("62")) kodeSumbis = "2"; //bank mandiri
                                        if (ent.getStRef2().equalsIgnoreCase("1129")) kodeSumbis = "3"; // mantap
                                        if (ent.getStRef2().equalsIgnoreCase("1096")) kodeSumbis = "4"; // BNI
                                        if (ent.getStEntityName().toUpperCase().contains("BPR")) kodeSumbis = "9"; // BPR

                                        glApplicator.setCode('G',kodeSumbis);

                                        accode = ivd.getStGLAccountCodeRestitusi();
                                    }

                                }
                            }
                        }
                    }
                }

                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
                
                if(ivd.getStTaxCode()!=null){
                    if(ivd.getStNegativeFlag().equalsIgnoreCase("N")){
                        EntityView entTax = ivd.getEntity();
                          
                        glApplicator.setCode('Y',entTax.getStGLCode());
                        glApplicator.setDesc("Y",entTax.getStShortName());
                        final String accodeTax = ivd.getStGLAccountCode2();
                        if (accodeTax!=null) {
                            ivd.setStGLAccountID(glApplicator.getAccountID(accodeTax));
                            ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                        }
                        
                        glApplicator.setCode('Y',entityGLCode);
            
                        if (getEntity()!=null)
                            glApplicator.setDesc("Y",entityDesc);
                    }
                }
                
            }
        }
    }
    
    
    public void recalculate2() {
        if(details!=null){
            recalculate2(false);
        }
        
    }
    
    public void recalculate2( boolean validate) {
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        
        if (!isHasBeenPosted()) {
            
            //stGLAccountCodeTR=null;
            stGLARAccountID=null;
            
            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();
            
            //final String stGLAccountCode = getStGLAccountCode();
            
            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();
            
            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();
            
            final String policyTypeGLCode = getPolicyTypeGLCode();
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);
            
            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());
            
            String acexcesscode = null;
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
                acexcesscode = ivd.getTrxLine().getStGLExcessAccount();
            }
            
            if (acexcesscode==null) throw new RuntimeException("Salah Setting Excess Account Tax");
            
            if (acexcesscode!=null) {
                stGLARAccountID = glApplicator.getAccountID(acexcesscode);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }
            
            
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
            }
        }
    }
    
    public void recalculateSaldoAwal() {
        if(details!=null){
            recalculateSaldoAwal(false);
        }
        
    }
    
    public void recalculateSaldoAwal( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
                
                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());
                    //javax.swing.JOptionPane.showMessageDialog(null,"Taxed rate= "+ det.getDbTaxRate(),"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));
                    //javax.swing.JOptionPane.showMessageDialog(null,"Taxed amount= "+ det.getDbTaxAmount(),"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                }
                
                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));
                
                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);
                
                if (exludeComission)
                    if(det.isComission()) continue;
                
                if (det.isNegative()) {
                    
                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
                    //javax.swing.JOptionPane.showMessageDialog(null,"IsNegative1= "+tot,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                    //javax.swing.JOptionPane.showMessageDialog(null,"IsNegative2= "+totE,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                } else {
                    
                    tot=BDUtil.add(tot,amtIDR);
                    //javax.swing.JOptionPane.showMessageDialog(null,"Else negative1= "+tot,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                    totE=BDUtil.add(totE,amtafterTax);
                    //javax.swing.JOptionPane.showMessageDialog(null,"Else negative2= "+totE,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
                    
                }
            }
            
            //javax.swing.JOptionPane.showMessageDialog(null,"tot= "+tot,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
            
            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }
        
        if (dbAmount!=null)
            if (
                (Tools.compare(dbAmount,BDUtil.zero)<0)
                ) {
            //stNegativeFlag = isNegative()?"N":"Y";
            if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
                setStInvoiceType(FinCodec.InvoiceType.AP);
            } else {
                setStInvoiceType(FinCodec.InvoiceType.AR);
            }
            negateDetails();
            
            recalculate();
            
            return;
            }
        
        if (isNegative()) {
            
            dbAmount = BDUtil.negate(dbAmount);
            //javax.swing.JOptionPane.showMessageDialog(null,"isNegative 2.1= "+dbAmount,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
            
            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
            //javax.swing.JOptionPane.showMessageDialog(null,"isNegative 2.2= "+dbEnteredAmount,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
            
        }
        
        if (!isHasBeenPosted()) {
            
            //stGLAccountCodeTR=null;
            stGLARAccountID=null;
            
            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();
            
            //final String stGLAccountCode = getStGLAccountCode();
            
            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();
            
            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();
            
            final String policyTypeGLCode = getPolicyTypeGLCode();
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);
            
            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());
            //glApplicator.setDesc("X",getPolicyType().getStDescription());
            
            String acc = getStGLAccountCode();
            
            //javax.swing.JOptionPane.showMessageDialog(null,"stGLARAccountID= "+acc,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
            
            
            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());
            
            acc = LanguageManager.getInstance().translate(acc);
            
            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }
            
            //javax.swing.JOptionPane.showMessageDialog(null,"stGLARAccountID= "+stGLARAccountID,"eror",javax.swing.JOptionPane.CLOSED_OPTION);
            
            
         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);
          
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/
            
            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");
            
            
            
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
            }
        }
    }
    
    private void negateDetails() {
        
        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
            
            det.setDbEnteredAmount(BDUtil.negate(det.getDbEnteredAmount()));
        }
    }
    
    public String getPolicyTypeGLCode() {
        return stAttrPolicyTypeID==null?null:getPolicyType().getStGLCode();
    }
    
    private boolean isHasBeenPosted() {
        if (getOldInvoice()==null) return false;
        return Tools.isYes(getOldInvoice().getStPostedFlag());
        
    }
    
    private ARInvoiceView getOldInvoice() {
        return (ARInvoiceView) getOld();
    }
    
    private boolean isPosted() {
        return Tools.isYes(stPostedFlag);
    }
    
    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stAttrPolicyTypeID);
    }
    
    public DTOList getPolicyObjects() {
        loadObjects();
        return objects;
    }
    
    private void loadObjects() {
        try {
        /*
         if (objects==null)
            objects = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_obj where pol_id = ?",
                    new Object [] {stAttrPolicyID},
                    InsurancePolicyObjDefaultView.class
            );*/
            
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQuery(
                        "select * from "+DTOCache.getTableName(clObjectClass)+" where pol_id  = ?",
                        new Object [] {stAttrPolicyID},
                        clObjectClass
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public Class getClObjectClass() {
        
        if (clObjectClass==null) {
            final InsurancePolicyTypeView pt = getPolicyType();
            
            if (pt!=null)
                clObjectClass = pt.getClObjectClass();
        }
        
        return clObjectClass;
    }
    
    private boolean isNegative() {
        return "Y".equalsIgnoreCase(stNegativeFlag);
    }
    
    public String getStRefTRX() {
        return "INV/"+stARInvoiceID;
    }
    
    public boolean isAR() {
        return FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType);
    }
    
    public boolean isAP() {
        return FinCodec.InvoiceType.AP.equalsIgnoreCase(stInvoiceType);
    }
    
    public String getStAttrPolicyNo() {
        return stAttrPolicyNo;
    }
    
    public void setStAttrPolicyNo(String stAttrPolicyNo) {
        this.stAttrPolicyNo = stAttrPolicyNo;
    }
    
    public Date getDtMutationDate() {
        return dtMutationDate;
    }
    
    public void setDtMutationDate(Date dtMutationDate) {
        this.dtMutationDate = dtMutationDate;
    }
    
    public String getStAttrPolicyName() {
        return stAttrPolicyName;
    }
    
    public void setStAttrPolicyName(String stAttrPolicyName) {
        this.stAttrPolicyName = stAttrPolicyName;
    }
    
    public Date getDtAttrPolicyPeriodStart() {
        return dtAttrPolicyPeriodStart;
    }
    
    public void setDtAttrPolicyPeriodStart(Date dtAttrPolicyPeriodStart) {
        this.dtAttrPolicyPeriodStart = dtAttrPolicyPeriodStart;
    }
    
    public Date getDtAttrPolicyPeriodEnd() {
        return dtAttrPolicyPeriodEnd;
    }
    
    public void setDtAttrPolicyPeriodEnd(Date dtAttrPolicyPeriodEnd) {
        this.dtAttrPolicyPeriodEnd = dtAttrPolicyPeriodEnd;
    }
    
    public String getStAttrPolicyAddress() {
        return stAttrPolicyAddress;
    }
    
    public void setStAttrPolicyAddress(String stAttrPolicyAddress) {
        this.stAttrPolicyAddress = stAttrPolicyAddress;
    }
    
    public BigDecimal getDbAttrPolicyTSI() {
        return dbAttrPolicyTSI;
    }
    
    public void setDbAttrPolicyTSI(BigDecimal dbAttrPolicyTSI) {
        this.dbAttrPolicyTSI = dbAttrPolicyTSI;
    }
    
    public BigDecimal getDbAttrPolicyTSITotal() {
        return dbAttrPolicyTSITotal;
    }
    
    public void setDbAttrPolicyTSITotal(BigDecimal dbAttrPolicyTSITotal) {
        this.dbAttrPolicyTSITotal = dbAttrPolicyTSITotal;
    }
    
    public String getStAttrPolicyTypeID() {
        return stAttrPolicyTypeID;
    }
    
    public void setStAttrPolicyTypeID(String stAttrPolicyTypeID) {
        this.stAttrPolicyTypeID = stAttrPolicyTypeID;
    }
    
    public String getStAttrQuartal() {
        return stAttrQuartal;
    }
    
    public void setStAttrQuartal(String stAttrQuartal) {
        this.stAttrQuartal = stAttrQuartal;
    }
    
    public String getStAttrUnderwriting() {
        return stAttrUnderwriting;
    }
    
    public void setStAttrUnderwriting(String stAttrUnderwriting) {
        this.stAttrUnderwriting = stAttrUnderwriting;
    }
    
    public String getStAttrPolicyID() {
        return stAttrPolicyID;
    }
    
    public void setStAttrPolicyID(String stAttrPolicyID) {
        this.stAttrPolicyID = stAttrPolicyID;
    }
    
    public ARTransactionTypeView getARTrxType() {
        return (ARTransactionTypeView) DTOPool.getInstance().getDTO(ARTransactionTypeView.class, stARTransactionTypeID);
    }
    
    public void setDbEnteredAmount(BigDecimal dbEnteredAmount) {
        this.dbEnteredAmount = dbEnteredAmount;
    }
    
    public BigDecimal getDbEnteredAmount() {
        return dbEnteredAmount;
    }
    
    public String getStGLARAccountCode() {
        return getARTrxType().getStGLARAccount();
    }
    
    public String getStGLAPAccountCode() {
        //return getARTrxType().getStGLAPAccount();
        
        if(getStARTransactionTypeID().equalsIgnoreCase("11")){
            if(getStTaxTrue()==true){
                String code[] = getARTrxType().getStGLAPAccount().split("[\\|]");
                String APCode = "";
                
                if(getStRefID2()!=null){
                    APCode = code[1];
                }
                return APCode;
            }else{
                String code[] = getARTrxType().getStGLAPAccount().split("[\\|]");
                String APCode = code[0];
                return APCode;
                
            }
        }else{
            return getARTrxType().getStGLAPAccount();
        }
        
    }
    
    public void setStNegativeFlag(String stNegativeFlag) {
        this.stNegativeFlag = stNegativeFlag;
    }
    
    public String getStNegativeFlag() {
        return stNegativeFlag;
    }
    
    public String getStGLAccountCode() {
        return isAP()?getStGLAPAccountCode():getStGLARAccountCode();
    }
    
    public String getStPremiFlag() {
        return stPremiFlag;
    }
    
    public void setStPremiFlag(String stPremiFlag) {
        this.stPremiFlag = stPremiFlag;
    }
    
    public String getStCommitFlag() {
        return stCommitFlag;
    }
    
    public void setStCommitFlag(String stCommitFlag) {
        this.stCommitFlag = stCommitFlag;
    }
    
    public String getStApprovedFlag() {
        return stApprovedFlag;
    }
    
    public void setStApprovedFlag(String stApprovedFlag) {
        this.stApprovedFlag = stApprovedFlag;
    }
    
    public BigDecimal getDbOutstandingAmountIDR() {
        
        return BDUtil.mul(getDbOutstandingAmount(), dbCurrencyRate,2);
    }
    
    public String getStGLCode() {
        return stGLCode;
    }
    
    public void setStGLCode(String stGLCode) {
        this.stGLCode = stGLCode;
    }
    
    public void reverseInvoiceType() {
        if (isAP()) stInvoiceType=FinCodec.InvoiceType.AR; else stInvoiceType=FinCodec.InvoiceType.AP;
    }
    
    public String getStReferenceX0() {
        return stReferenceX0;
    }
    
    public void setStReferenceX0(String stReferenceX0) {
        this.stReferenceX0 = stReferenceX0;
    }
    
    public String getStReferenceX1() {
        return stReferenceX1;
    }
    
    public void setStReferenceX1(String stReferenceX1) {
        this.stReferenceX1 = stReferenceX1;
    }
    
    public String getStReferenceY0() {
        return stReferenceY0;
    }
    
    public void setStReferenceY0(String stReferenceY0) {
        this.stReferenceY0 = stReferenceY0;
    }
    
    public String getStReferenceY1() {
        return stReferenceY1;
    }
    
    public void setStReferenceY1(String stReferenceY1) {
        this.stReferenceY1 = stReferenceY1;
    }
    
    public String getStReferenceZ0() {
        return stReferenceZ0;
    }
    
    public void setStReferenceZ0(String stReferenceZ0) {
        this.stReferenceZ0 = stReferenceZ0;
    }
    
    public String getStReferenceZ1() {
        return stReferenceZ1;
    }
    
    public void setStReferenceZ1(String stReferenceZ1) {
        this.stReferenceZ1 = stReferenceZ1;
    }
    
    public String getStReferenceA0() {
        return stReferenceA0;
    }
    
    public void setStReferenceA0(String stReferenceA0) {
        this.stReferenceA0 = stReferenceA0;
    }
    
    public String getStReferenceA1() {
        return stReferenceA1;
    }
    
    public void setStReferenceA1(String stReferenceA1) {
        this.stReferenceA1 = stReferenceA1;
    }
    
    public String getStReferenceC0() {
        return stReferenceC0;
    }
    
    public void setStReferenceC0(String stReferenceC0) {
        this.stReferenceC0 = stReferenceC0;
    }
    
    public String getStReferenceC1() {
        return stReferenceC1;
    }
    
    public void setStReferenceC1(String stReferenceC1) {
        this.stReferenceC1 = stReferenceC1;
    }
    
    public String getStReferenceD0() {
        return stReferenceD0;
    }
    
    public void setStReferenceD0(String stReferenceD0) {
        this.stReferenceD0 = stReferenceD0;
    }
    
    public String getStReferenceD1() {
        return stReferenceD1;
    }
    
    public void setStReferenceD1(String stReferenceD1) {
        this.stReferenceD1 = stReferenceD1;
    }
    
    public String getStReferenceE0() {
        return stReferenceE0;
    }
    
    public void setStReferenceE0(String stReferenceE0) {
        this.stReferenceE0 = stReferenceE0;
    }
    
    public String getStReferenceE1() {
        return stReferenceE1;
    }
    
    public void setStReferenceE1(String stReferenceE1) {
        this.stReferenceE1 = stReferenceE1;
    }
    
    public String getStNoJournalFlag() {
        return stNoJournalFlag;
    }
    
    public void setStNoJournalFlag(String stNoJournalFlag) {
        this.stNoJournalFlag = stNoJournalFlag;
    }
    
    public String getStHideFlag() {
        return stHideFlag;
    }
    
    public void setStHideFlag(String stHideFlag) {
        this.stHideFlag = stHideFlag;
    }
    
    public String getStNonPostableFlag() {
        return stNonPostableFlag;
    }
    
    public void setStNonPostableFlag(String stNonPostableFlag) {
        this.stNonPostableFlag = stNonPostableFlag;
    }
    
    public String getStRefID0() {
        return stRefID0;
    }
    
    public void setStRefID0(String stRefID0) {
        this.stRefID0 = stRefID0;
    }
    
    public String getStRefID1() {
        return stRefID1;
    }
    
    public void setStRefID1(String stRefID1) {
        this.stRefID1 = stRefID1;
    }
    
    public String getStRefID2() {
        return stRefID2;
    }
    
    public void setStRefID2(String stRefID2) {
        this.stRefID2 = stRefID2;
    }
    
    public String getStRefID3() {
        return stRefID3;
    }
    
    public void setStRefID3(String stRefID3) {
        this.stRefID3 = stRefID3;
    }
    
    public String getStNoSuratHutang() {
        return stNoSuratHutang;
    }
    
    public void setStNoSuratHutang(String stNoSuratHutang) {
        this.stNoSuratHutang = stNoSuratHutang;
    }
    
    public String generateNoSuratHutang(String stEntityID,String treatyType, String polType) throws Exception {
        
        //  200/QS/01/04/2009
        
        return stNoSuratHutang =
                stEntityID+
                "/"+
                treatyType+
                "/"+
                polType+
                "/"+
                DateUtil.getMonth2Digit(new Date())+
                "/"+
                DateUtil.getYear(new Date());
        
    }
    
    public void setStTaxTrue(boolean tax){
        this.taxTrue = tax;
    }
    
    public boolean getStTaxTrue() {
        return taxTrue;
    }
    
    public String getStGLAPAccountCodeBeda(boolean komisi) {
        setStARTransactionTypeID("11");
        String code[] = getARTrxType().getStGLAPAccount().split("[\\|]");
        String APCode = "";
        
        if(komisi){
            APCode = code[2];
        }else{
            APCode = code[1];
        }
        
        return APCode;
    }
    
    private DTOList insPolicy;
    
    public DTOList getInsPolicy() {
        loadInsPolicy();
        return insPolicy;
    }
    
    public void setInsPolicy(DTOList insPolicy) {
        this.insPolicy = insPolicy;
    }
    
    public void loadInsPolicy() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (insPolicy==null)
                insPolicy = ListUtil.getDTOListFromQuery(
                        "select * from ins_policy where pol_id = ? ",
                        new Object [] {stAttrPolicyID},
                        InsurancePolicyView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public DTOList getList3() {
        if (list3==null) {
            if (!isNew()) {
                try {
                    list3 = ListUtil.getDTOListFromQuery(
                            "select a.* from ar_invoice a "+
                            "inner join ins_policy b on b.pol_id = a.attr_pol_id "+
                            "where a.claim_no = ? order by a.cc_code,b.dla_no,a.attr_pol_no ",
                            new Object[] {stClaimNo},
                            ARInvoiceView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list3;
    }
    
    public void setList3(DTOList list3) {
        this.list3 = list3;
    }
    
    public String getStClaimNo() {
        return stClaimNo;
    }
    
    public void setStClaimNo(String stClaimNo) {
        this.stClaimNo = stClaimNo;
    }
    
    public String getStClaimName() {
        return stClaimName;
    }
    
    public void setStClaimName(String stClaimName) {
        this.stClaimName = stClaimName;
    }
    
    public String getStClaimCoinsID() {
        return stClaimCoinsID;
    }
    
    public void setStClaimCoinsID(String stClaimCoinsID) {
        this.stClaimCoinsID = stClaimCoinsID;
    }
    
    public String getStClaimCoinsName() {
        return stClaimCoinsName;
    }
    
    public void setStClaimCoinsName(String stClaimCoinsName) {
        this.stClaimCoinsName = stClaimCoinsName;
    }
    
    public String getStClaimCoinsAddress() {
        return stClaimCoinsAddress;
    }
    
    public void setStClaimCoinsAddress(String stClaimCoinsAddress) {
        this.stClaimCoinsAddress = stClaimCoinsAddress;
    }
    
    public String getStDLANo() {
        return stDLANo;
    }
    
    public void setStDLANo(String stDLANo) {
        this.stDLANo = stDLANo;
    }
    
    public void setDbPcost(BigDecimal dbPcost) {
        this.dbPcost = dbPcost;
    }
    
    public BigDecimal getDbPcost() {
        return dbPcost;
    }
    
    public void setDbStampDuty(BigDecimal dbStampDuty) {
        this.dbStampDuty = dbStampDuty;
    }
    
    public BigDecimal getDbStampDuty() {
        return dbStampDuty;
    }
    
    public void setDbDisc(BigDecimal dbDisc) {
        this.dbDisc = dbDisc;
    }
    
    public BigDecimal getDbDisc() {
        return dbDisc;
    }
    
    public DTOList getARInvoiceDetailsCoins() {
      loadARInvoiceDetailsCoins();
      return arInvoiceDetailsCoins;
   }

   public void setARInvoiceDetailsCoins(DTOList arInvoiceDetailsCoins) {
      this.arInvoiceDetailsCoins = arInvoiceDetailsCoins;
   }

  public void loadARInvoiceDetailsCoins() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (arInvoiceDetailsCoins == null) {
                arInvoiceDetailsCoins = ListUtil.getDTOListFromQuery(
                        "select *,checkpremi(ar_trx_type_id = '6',(amount+attr_pol_tsi+entered_amount),amount) as amount_total from ( "
                        + "select a.attr_pol_no,a.ar_invoice_id,a.ent_id,a.ar_trx_type_id, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '55',b.amount*-1),checkreas(c.ar_trx_line_id = '23',b.amount))) as amount, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '29',b.amount))) as disc, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '28',b.amount))) as bpol, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '27',b.amount))) as bmat, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'COMMISSION' and c.item_class = 'FEEBASE' ,b.amount))) as amountFeebase, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '73',b.amount*-1),checkreas(c.category = 'COMMISSION' and c.item_class isnull ,b.amount))) as amount_settled, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'TAXCOMM',b.amount))) as ccy_rate, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '71',b.amount*-1),checkreas(c.category = 'BROKERAGE',b.amount))) as attr_pol_tsi, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'TAXBROK',b.amount))) as attr_pol_tsi_total, "
                        + "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '72',b.amount*-1),checkreas(c.category = 'HFEE',b.amount))) as entered_amount "
                        + "from ar_invoice a "
                        + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id "
                        + "left join ar_trx_line c on c.ar_trx_line_id = b.ar_trx_line_id "
                        + "where attr_pol_id = ? and a.ar_trx_type_id in (6,10) and a.ent_id not in ('94','96') "
                        + "and coalesce(a.cancel_flag,'') <> 'Y' "
                        + "group by a.attr_pol_no,a.ar_invoice_id,a.ent_id,a.ar_trx_type_id "
                        + " ) x order by ar_invoice_id,ent_id ",
                        new Object[]{stAttrPolicyID},
                        ARInvoiceView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
   public InsurancePolicyView getPolicy() {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stAttrPolicyID);
    }
   
   public void setDbPremi(BigDecimal dbPremi) {
        this.dbPremi = dbPremi;
    }
    
    public BigDecimal getDbPremi() {
        return dbPremi;
    }
    
    public void setDbComm(BigDecimal dbComm) {
        this.dbComm = dbComm;
    }
    
    public BigDecimal getDbComm() {
        return dbComm;
    }
    
    public void setDbFee(BigDecimal dbFee) {
        this.dbFee = dbFee;
    }
    
    public BigDecimal getDbFee() {
        return dbFee;
    }
    
    public void recalculateOld() {
      recalculateOld(false);
   }

   public void recalculateOld( boolean validate) {

      BigDecimal tot = null;
      BigDecimal totE = null;

      ARTransactionTypeView arTrxType = getARTrxType();

      if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

      if (!arTrxType.trxDisableDetail()) {
         for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            if (det.isTaxed()) {
               det.setDbTaxRate(det.getTax().getDbRate());
               det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount()));
            }

            final BigDecimal amt = det.getDbEnteredAmount();
            final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
            det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate()));

            final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate());

            if (exludeComission)
               if(det.isComission()) continue;

            if (det.isNegative()) {
               tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
               totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
            } else {
               tot=BDUtil.add(tot,amtIDR);
               totE=BDUtil.add(totE,amtafterTax);
            }
         }

         dbAmount = tot;
         dbEnteredAmount = totE;
      } else {
         dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate);
      }

      if (dbAmount!=null)
         if (
                 (Tools.compare(dbAmount,BDUtil.zero)<0)  
         ) {
            //stNegativeFlag = isNegative()?"N":"Y";
            if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
               setStInvoiceType(FinCodec.InvoiceType.AP);
            } else {
               setStInvoiceType(FinCodec.InvoiceType.AR);
            }
            //negateDetails();

            recalculate();

            return;
         }

      if (isNegative()) {
         dbAmount = BDUtil.negate(dbAmount);
         dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
      }

      if (!isHasBeenPosted()) {

         //stGLAccountCodeTR=null;
         stGLARAccountID=null;

         final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();

         //final String stGLAccountCode = getStGLAccountCode();

         final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

         final String policyTypeGLCode = getPolicyTypeGLCode();

         glApplicator.setCode('Y',entityGLCode);

         if (getEntity()!=null)
            glApplicator.setDesc("Y",getEntity().getStEntityName());

         glApplicator.setCode('B',stCostCenterCode);
         glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
         glApplicator.setCode('A',getStReferenceA0());
         glApplicator.setDesc("A",getStReferenceA1());
         glApplicator.setCode('C',getStReferenceC0());
         glApplicator.setDesc("C",getStReferenceC1());
         glApplicator.setCode('D',getStReferenceD0());
         glApplicator.setDesc("D",getStReferenceD1());
         glApplicator.setCode('E',getStReferenceE0());
         glApplicator.setDesc("E",getStReferenceE1());
         //glApplicator.setCode('Y',getStReferenceY0());
         //glApplicator.setDesc("Y",getStReferenceY1());
         glApplicator.setCode('Z',getStReferenceZ0());
         glApplicator.setDesc("Z",getStReferenceZ1());

         if (getPolicyType()!=null)
            glApplicator.setDesc("X",getPolicyType().getStDescription());

         String acc = getStGLAccountCode();

         if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

         acc = LanguageManager.getInstance().translate(acc);

         if (acc!=null) {
            stGLARAccountID = glApplicator.getAccountID(acc);
            if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            stGLARAccountDesc = glApplicator.getPreviewDesc();
            if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
         }

         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/

         //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");



         for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);

            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);

            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());

            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);

            ivd.setStGLAccountCodeTR(glc);*/

            final String accode = ivd.getStGLAccountCode();
            if (accode!=null) {
               ivd.setStGLAccountID(glApplicator.getAccountID(accode));
               ivd.setStAccountDesc(glApplicator.getPreviewDesc());
            }
         }
      }
   }
    
   public BigDecimal getDbFeeEntered() {
        return dbFeeEntered;
    }

    public void setDbFeeEntered(BigDecimal dbFeeEntered) {
        this.dbFeeEntered = dbFeeEntered;
    }

    public BigDecimal getDbPremiEntered() {
        return dbPremiEntered;
    }

    public void setDbPremiEntered(BigDecimal dbPremiEntered) {
        this.dbPremiEntered = dbPremiEntered;
    }

    public BigDecimal getDbCommEntered() {
        return dbCommEntered;
    }

    public void setDbCommEntered(BigDecimal dbCommEntered) {
        this.dbCommEntered = dbCommEntered;
    }

    public String getStClaimStatus() {
        return stClaimStatus;
    }

    public void setStClaimStatus(String stClaimStatus) {
        this.stClaimStatus = stClaimStatus;
    }
    
    public void recalculateInwardTreaty() {
      recalculateInwardTreaty(false);
   }

   public void recalculateInwardTreaty( boolean validate) {

      logger.logDebug("++++++++++++++ MASUK RECALCULATE INVOICE INWARD TREATY FORM +++++++++++++=");
       
      BigDecimal tot = null;
      BigDecimal totE = null;

      ARTransactionTypeView arTrxType = getARTrxType();

      if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

      
      if (!arTrxType.trxDisableDetail()) {
         for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            BigDecimal totPerLine = null;
            BigDecimal totPerLineE = null;
            
            final DTOList detailsInvoice = det.getDetails();
            for (int j = 0; j < detailsInvoice.size(); j++) {
                ARInvoiceDetailView detail = (ARInvoiceDetailView) detailsInvoice.get(j);
                
                if (detail.isTaxed()) {
                   detail.setDbTaxRate(detail.getTax().getDbRate());
                   detail.setDbTaxAmount(BDUtil.mul(detail.getDbTaxRate(), detail.getDbEnteredAmount()));
                }

                final BigDecimal amt = detail.getDbEnteredAmount();
                final BigDecimal amtafterTax = detail.getDbEnteredAmountAfterTax();
                detail.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate()));

                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate());

                if (exludeComission)
                   if(detail.isComission()) continue;

                if (detail.isNegative()) {
                   tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
                   totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                   totPerLine = BDUtil.add(totPerLine,BDUtil.negate(amtIDR));
                   totPerLineE = BDUtil.add(totPerLineE,BDUtil.negate(amtafterTax));
                } else {
                   tot=BDUtil.add(tot,amtIDR);
                   totE=BDUtil.add(totE,amtafterTax);
                   totPerLine = BDUtil.add(totPerLine,amtIDR);
                   totPerLineE = BDUtil.add(totPerLineE,amtafterTax);
                }
                
                det.setStAttrPolicyTypeID(detail.getStAttrPolicyTypeID());
                det.setStAttrUnderwriting(detail.getStAttrUnderwriting());
                det.setStRefID0(detail.getStRefID0());
            }
            
            det.setDbEnteredAmount(totPerLineE);
            det.setDbAmount(totPerLine);

         }

         dbAmount = tot;
         dbEnteredAmount = totE;
      } else {
         dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate);
      }

      /*
      if (dbAmount!=null)
         if (
                 (Tools.compare(dbAmount,BDUtil.zero)<0)  
         ) {
            //stNegativeFlag = isNegative()?"N":"Y";
            if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
               setStInvoiceType(FinCodec.InvoiceType.AP);
            } else {
               setStInvoiceType(FinCodec.InvoiceType.AR);
            }
            negateDetails();

            recalculate();

            return;
         }*/

      if (isNegative()) {
         dbAmount = BDUtil.negate(dbAmount);
         dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
      }

      //if (!isHasBeenPosted()) {

         //stGLAccountCodeTR=null;
         stGLARAccountID=null;

         final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();

         //final String stGLAccountCode = getStGLAccountCode();

         final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

         final String policyTypeGLCode = getPolicyTypeGLCode();

         glApplicator.setCode('Y',entityGLCode);

         if (getEntity()!=null)
            glApplicator.setDesc("Y",getEntity().getStEntityName());

         glApplicator.setCode('B',stCostCenterCode);
         if (getPolicyType()!=null)
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
         
         glApplicator.setCode('A',getStReferenceA0());
         glApplicator.setDesc("A",getStReferenceA1());
         glApplicator.setCode('C',getStReferenceC0());
         glApplicator.setDesc("C",getStReferenceC1());
         glApplicator.setCode('D',getStReferenceD0());
         glApplicator.setDesc("D",getStReferenceD1());
         glApplicator.setCode('E',getStReferenceE0());
         glApplicator.setDesc("E",getStReferenceE1());
         //glApplicator.setCode('Y',getStReferenceY0());
         //glApplicator.setDesc("Y",getStReferenceY1());
         glApplicator.setCode('Z',getStReferenceZ0());
         glApplicator.setDesc("Z",getStReferenceZ1());

         if (getPolicyType()!=null)
            glApplicator.setDesc("X",getPolicyType().getStDescription());

         String acc = getStGLAccountCode();

         if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

         acc = LanguageManager.getInstance().translate(acc);

         logger.logDebug(">>>>>>>>>>>>>>>> Account : "+glApplicator.getAccountID(acc));
         logger.logDebug(">>>>>>>>>>>>>>>> Account : "+glApplicator.getPreviewDesc());
         
         if (acc!=null) {
            stGLARAccountID = glApplicator.getAccountID(acc);
            if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            stGLARAccountDesc = glApplicator.getPreviewDesc();
            if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
         }

         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/

         //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");



         for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);

            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);

            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());

            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);

            ivd.setStGLAccountCodeTR(glc);*/
            glApplicator.setCode('X',ivd.getPolicyTypeGLCode());
            glApplicator.setDesc("X",ivd.getPolicyType().getStDescription());
            if (acc!=null) {
                ivd.setStGLAccountID(glApplicator.getAccountID(acc));
                ivd.setStAccountDesc(glApplicator.getPreviewDesc());
            }
            

            final DTOList detailsInvoice = ivd.getDetails();
            for (int j = 0; j < detailsInvoice.size(); j++) {
                ARInvoiceDetailView detail = (ARInvoiceDetailView) detailsInvoice.get(j);
                
                final String accode = detail.getStGLAccountCode();
                glApplicator.setCode('X',detail.getPolicyTypeGLCode());
                glApplicator.setDesc("X",detail.getPolicyType().getStDescription());
                if (accode!=null) {
                   detail.setStGLAccountID(glApplicator.getAccountID(accode));
                   detail.setStAccountDesc(glApplicator.getPreviewDesc());
                }
            }

            
         }
      //}
   }
   
   public DTOList getDetailsInwardTreaty() {
        if (details==null) {
                try {
                    details = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice_details where ar_invoice_id = ? and ar_trx_line_id is null order by ar_invoice_dtl_id",
                            new Object[] {stARInvoiceID},
                            ARInvoiceDetailView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        return details;
    }
   
   public String getStFilePhysic() {
      return stFilePhysic;
   }

   public void setStFilePhysic(String stFilePhysic) {
      this.stFilePhysic = stFilePhysic;
   }
   
   public FlexFieldHeaderView getARInvoiceFF() {
        final FlexFieldHeaderView flexFieldHeaderView = (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "INVOICE_"+ stARTransactionTypeID +"_"+ stAttrPolicyTypeID);
        return flexFieldHeaderView;
   }
   
    public Date getDtTransDate() {
        return dtTransDate;
    }

    public void setDtTransDate(Date dtTransDate) {
        this.dtTransDate = dtTransDate;
    }

    public DTOList getListDetailsSuratHutangPph21() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.* from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_surat_hutang = ? "
                            + " and b.ar_trx_line_id in (14,17,20,30,33,36,46,49,52) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoSuratHutang},
                            ARInvoiceView.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    public DTOList getListDetailsSuratHutangPph23() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.* from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_surat_hutang = ? "
                            + " and b.ar_trx_line_id in (15,18,21,31,34,37,47,50,53) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoSuratHutang},
                            ARInvoiceView.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    public void setListDetailsSuratHutang(DTOList listDetailsSuratHutang)
    {
        this.listDetailsSuratHutang = listDetailsSuratHutang;
    }
    
    public String generateNoSuratHutangCoasBackup(String stEntityID,InsurancePolicyView policy) throws Exception {
        
        //  SH-KO/0001/11/21/AA/11/11
        //  SH-KO/0002/JS.20/AK.21/XII/11
        final String stCostCenterCode = policy.getStCostCenterCode();
        
        final boolean alwaysUseJiwasraya = stCostCenterCode.equalsIgnoreCase("20") || stCostCenterCode.equalsIgnoreCase("21") ||
                                                stCostCenterCode.equalsIgnoreCase("43") || stCostCenterCode.equalsIgnoreCase("40") ||
                                                stCostCenterCode.equalsIgnoreCase("70") || stCostCenterCode.equalsIgnoreCase("10") ||
                                                stCostCenterCode.equalsIgnoreCase("13") || stCostCenterCode.equalsIgnoreCase("17") ||
                                                stCostCenterCode.equalsIgnoreCase("22");
        
        String counter = StringTools.leftPad(DateUtil.getMonth2Digit(new Date()), '0', 4);
        if(stEntityID.equalsIgnoreCase("96")){
            if(alwaysUseJiwasraya) counter = counter + ".A";
            else counter = counter + ".B";
        }
        
        
        
        String koas = stEntityID;
        if(stEntityID.equalsIgnoreCase("96")) koas = "JS.20";
        else if(stEntityID.equalsIgnoreCase("94")) koas = "BAJ.87";
        else if(stEntityID.equalsIgnoreCase("2000")) koas = "AJS_TEMP.AA";
        
        String polType = policy.getStPolicyTypeID();
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) polType = "AK.21";
        
        return stNoSuratHutang =
                "SH-KO/"+
                counter+
                "/"+
                koas +
                "/"+
                polType+
                "/"+
                DateUtil.getMonthRomawi(policy.getDtPolicyDate())+
                "/"+
                DateUtil.getYear2Digit(policy.getDtPolicyDate());
        
    }
    
    
    public String getStReference3() {
        return stReference3;
    }

    public void setStReference3(String stReference3) {
        this.stReference3 = stReference3;
    }

    public String getStReference4() {
        return stReference4;
    }

    public void setStReference4(String stReference4) {
        this.stReference4 = stReference4;
    }
    
    public String generateNoSuratHutangCoas(String stEntityID,InsurancePolicyView policy) throws Exception {
        
        //  SH-KO/0001/11/21/AA/11/11
        //  SH-KO/0002/JS.20/AK.21/XII/11
        final String stCostCenterCode = policy.getStCostCenterCode();
        
        final boolean alwaysUseJiwasraya = stCostCenterCode.equalsIgnoreCase("11") || stCostCenterCode.equalsIgnoreCase("21") ||
                                                stCostCenterCode.equalsIgnoreCase("22") || stCostCenterCode.equalsIgnoreCase("12") ||
                                                stCostCenterCode.equalsIgnoreCase("50");
        
        String counter = StringTools.leftPad(DateUtil.getMonth2Digit(policy.getDtPolicyDate()), '0', 4);

        if(stEntityID.equalsIgnoreCase("96")){
            //if(alwaysUseJiwasraya) counter = counter + ".A";
            //else counter = counter + ".B";
            
            //counter = counter + ".B";
        }
        
        final EntityView ent = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
        
        String koas = stEntityID; 

        /*
        if(stEntityID.equalsIgnoreCase("96")) koas = "JS.20";
        else if(stEntityID.equalsIgnoreCase("94")) koas = "BAJ.87";
        else if(stEntityID.equalsIgnoreCase("2000")) koas = "AJS_TEMP.AA";
        else if(stEntityID.equalsIgnoreCase("2001")) koas = "AJS_TEMP.AB";
        */
        
        String polType = policy.getStPolicyTypeID();
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
            koas = ent.getStShortName() + "." + ent.getStRefEntityID();
            polType = "AK.21";
        }
        
        return stNoSuratHutang =
                "SH-KO/"+
                counter+
                "/"+
                koas +
                "/"+
                polType+
                "/"+
                DateUtil.getMonthRomawi(policy.getDtPolicyDate())+
                "/"+
                DateUtil.getYear2Digit(policy.getDtPolicyDate());
        
    }
    
    public void recalculateClaimEndorse( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
                
                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());
                    
                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));
                    
                }
                
                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));
                
                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);
                
                if (exludeComission)
                    if(det.isComission()) continue;
                
                
                if (det.isNegative()) {
                    
                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
                    
                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {
                    
                    tot=BDUtil.add(tot,amtIDR);
                    
                    totE=BDUtil.add(totE,amtafterTax);
                    
                }
                
            }
            
            
            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }
        
        if (isNegative()) {
            
            //dbAmount = BDUtil.negate(dbAmount);
            
            //dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
            
        }
        
         if (dbAmount!=null)
            //if ((Tools.compare(dbAmount,BDUtil.zero)<0)) 
            if(BDUtil.isNegative(dbAmount))
            {
                if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
                    setStInvoiceType(FinCodec.InvoiceType.AP);
                } else {
                    setStInvoiceType(FinCodec.InvoiceType.AR);
                }
                //negateDetails();

                //recalculate();

                //return;
            }
        
        if (!isHasBeenPosted()) {
            
            stGLARAccountID=null;
            
            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();
            
            //final String stGLAccountCode = getStGLAccountCode();
            
            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();
            
            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();
            
            final String policyTypeGLCode = getPolicyTypeGLCode();
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);
            
            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());
            
            String acc = getStGLAccountCode();
            
            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());
            
            acc = LanguageManager.getInstance().translate(acc);
            
            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }
            
         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);
          
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/
            
            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");
            
            
            
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
            }
        }
    }
    
    public void recalculateTax( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
                
                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());
                    
                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));
                    
                }
                
                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));
                
                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);
                
                if (exludeComission)
                    if(det.isComission()) continue;
                
                
                if (det.isNegative()) {
                    
                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));
                    
                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {
                    
                    tot=BDUtil.add(tot,amtIDR);
                    
                    totE=BDUtil.add(totE,amtafterTax);
                    
                }
                
            }
            
            
            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }
        
        if (isNegative()) {
            
            dbAmount = BDUtil.negate(dbAmount);
            
            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
            
        }
        
         if (dbAmount!=null)
            //if ((Tools.compare(dbAmount,BDUtil.zero)<0)) 
            if(BDUtil.isNegative(dbAmount))
            {
                if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
                    setStInvoiceType(FinCodec.InvoiceType.AP);
                } else {
                    setStInvoiceType(FinCodec.InvoiceType.AR);
                }
                //negateDetails();

                //recalculate();

                //return;
            }
        
        if (!isHasBeenPosted()) {
            
            stGLARAccountID=null;
            
            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();
            
            //final String stGLAccountCode = getStGLAccountCode();
            
            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();
            
            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();
            
            final String policyTypeGLCode = getPolicyTypeGLCode();
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);
            
            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());
            
            String acc = getStGLAccountCode();
            
            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());
            
            acc = LanguageManager.getInstance().translate(acc);
            
            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }
            
         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);
          
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/
            
            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");
            
            
            
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
                
                if(ivd.getStTaxFlag()!=null){
                    final String accodeTax = ivd.getStGLAccountCode2();
                    if (accodeTax!=null) {
                        ivd.setStGLAccountID(glApplicator.getAccountID(accodeTax));
                        ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                    }
                    
                    
                }
                if(BDUtil.isZeroOrNull(ivd.getDbEnteredAmount()))
                        throw new RuntimeException("Nilai nya nol : "+ ivd.getStAccountDesc());

            }
        }
    }

    /**
     * @return the dbAmountFeeBase
     */
    public BigDecimal getDbAmountFeeBase() {
        return dbAmountFeeBase;
    }

    /**
     * @param dbAmountFeeBase the dbAmountFeeBase to set
     */
    public void setDbAmountFeeBase(BigDecimal dbAmountFeeBase) {
        this.dbAmountFeeBase = dbAmountFeeBase;
    }

    /**
     * @return the stUsedFlag
     */
    public String getStUsedFlag() {
        return stUsedFlag;
    }

    /**
     * @param stUsedFlag the stUsedFlag to set
     */
    public void setStUsedFlag(String stUsedFlag) {
        this.stUsedFlag = stUsedFlag;
    }

    public DTOList getDetailsWithoutOrder() {
        if (details==null) {
            if (!isNew()) {
                try {
                    details = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice_details where ar_invoice_id = ?",
                            new Object[] {stARInvoiceID},
                            ARInvoiceDetailView.class
                            );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return details;
    }

    public DTOList getListDetailsSuratHutangPph21New() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,a.amount as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_surat_hutang = ? "
                            + " and b.ar_trx_line_id in (14,17,20,30,33,36,46,49,52) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoSuratHutang},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    public DTOList getListDetailsSuratHutangPph23New() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,a.amount as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_surat_hutang = ? "
                            + " and b.ar_trx_line_id in (15,18,19,21,31,34,35,37,47,50,51,53,96,120) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoSuratHutang},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    /**
     * @return the stReceiptNo
     */
    public String getStReceiptNo() {
        return stReceiptNo;
    }

    /**
     * @param stReceiptNo the stReceiptNo to set
     */
    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public void recalculateClaimTax(boolean notCalculateAccount) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());

                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));

                }

                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));

                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);

                if (exludeComission)
                    if(det.isComission()) continue;


                if (det.isNegative()) {

                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));

                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {

                    tot=BDUtil.add(tot,amtIDR);

                    totE=BDUtil.add(totE,amtafterTax);

                }

            }


            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }

        if (isNegative()) {

            dbAmount = BDUtil.negate(dbAmount);

            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);

        }

        if (!isHasBeenPosted()) {

            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();

            //final String stGLAccountCode = getStGLAccountCode();

            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            final String policyTypeGLCode = getPolicyTypeGLCode();

            glApplicator.setCode('Y',entityGLCode);

            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);

            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            glApplicator.setDesc("K",getStRefID1());

            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());

            String acc = getStGLAccountCode();

            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

            acc = LanguageManager.getInstance().translate(acc);

            if(!notCalculateAccount){
                stGLARAccountID=null;
                if (acc!=null) {
                    stGLARAccountID = glApplicator.getAccountID(acc);
                    if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                    stGLARAccountDesc = glApplicator.getPreviewDesc();
                    if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                }
            }


         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/

            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");



            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);

            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);

            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());

            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);

            ivd.setStGLAccountCodeTR(glc);*/

                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getStGLDesc());
                }

                if(ivd.getStTaxFlag()!=null){ 
                        glApplicator.setCode('Y', ivd.getEntity().getStGLCode());

                        ivd.setStGLAccountID(glApplicator.getAccountID(ivd.getTrxLine().getStGLArAccountByType("AP")));
                        ivd.setStAccountDesc(glApplicator.getStGLDesc());
                }

                if(BDUtil.isZeroOrNull(ivd.getDbEnteredAmount()))
                        throw new RuntimeException("Nilai nya nol : "+ ivd.getStAccountDesc());

            }
        }
    }

    public void recalculateClaimOnly( boolean validate) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        boolean excludeTax = true;

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

                if(excludeTax)
                    if(det.isTax()) continue;

                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());

                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));

                }

                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));

                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);

                if (exludeComission)
                    if(det.isComission()) continue;

                if (det.isNegative()) {

                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));

                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {

                    tot=BDUtil.add(tot,amtIDR);

                    totE=BDUtil.add(totE,amtafterTax);

                }

            }


            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }

        if (isNegative()) {

            dbAmount = BDUtil.negate(dbAmount);

            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);

        }

         //if (dbAmount!=null)
            //if ((Tools.compare(dbAmount,BDUtil.zero)<0))
            /*
             if(BDUtil.isNegative(dbAmount))
            {
                if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
                    setStInvoiceType(FinCodec.InvoiceType.AP);
                } else {
                    setStInvoiceType(FinCodec.InvoiceType.AR);
                }
            }*/

        if (!isHasBeenPosted()) {

            stGLARAccountID=null;

            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();

            //final String stGLAccountCode = getStGLAccountCode();

            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            final String policyTypeGLCode = getPolicyTypeGLCode();

            glApplicator.setCode('Y',entityGLCode);

            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);

            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            glApplicator.setDesc("K",getStRefID2());

            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());

            String acc = getStGLAccountCode();

            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

            acc = LanguageManager.getInstance().translate(acc);

            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }

         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/

            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");



            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);

            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);

            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());

            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);

            ivd.setStGLAccountCodeTR(glc);*/

                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }

                if(ivd.getStTaxCode()!=null){
                    if(ivd.getStNegativeFlag().equalsIgnoreCase("N")){
                        EntityView entTax = ivd.getEntity();

                        glApplicator.setCode('Y',entTax.getStGLCode());
                        glApplicator.setDesc("Y",entTax.getStShortName());
                        final String accodeTax = ivd.getStGLAccountCode2();
                        if (accodeTax!=null) {
                            ivd.setStGLAccountID(glApplicator.getAccountID(accodeTax));
                            ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                        }

                        glApplicator.setCode('Y',entityGLCode);

                        if (getEntity()!=null)
                            glApplicator.setDesc("Y",entityDesc);
                    }
                }

            }
        }
    }

     public String generateNoSuratHutangCoas2(String stEntityID,InsurancePolicyView policy) throws Exception {

        //  SH-KO/0001/11/21/AA/11/11
        //  SH-KO/0002/JS.20/AK.21/XII/11
        final String stCostCenterCode = policy.getStCostCenterCode();

        String counter = StringTools.leftPad(DateUtil.getMonth2Digit(policy.getDtPolicyDate()), '0', 4);

        final EntityView ent = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        String koas = stEntityID;

        /*
        if(stEntityID.equalsIgnoreCase("96")) koas = "JS.20";
        else if(stEntityID.equalsIgnoreCase("94")) koas = "BAJ.87";
        else if(stEntityID.equalsIgnoreCase("2000")) koas = "AJS_TEMP.AA";
        else if(stEntityID.equalsIgnoreCase("2001")) koas = "AJS_TEMP.AB";
        */

        String polType = policy.getStPolicyTypeID();
        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
            koas = ent.getStShortName() + "." + ent.getStRefEntityID();
            polType = "AK.21";
        }

        stNoSuratHutang =
                "SH-KO/"+
                counter+
                "/"+
                koas +
                "/"+
                polType+
                "/"+
                DateUtil.getMonthRomawi(policy.getDtPolicyDate())+
                "/"+
                DateUtil.getYear2Digit(policy.getDtPolicyDate());

        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
            if(stEntityID.equalsIgnoreCase("96")){
                     stNoSuratHutang = policy.getStReference3();
            }else{
                     stNoSuratHutang = policy.getStReference4();
            }
        }
        

        return stNoSuratHutang;

    }


     public void recalculateClaimRIOutward(Date dtDLADate) {
        if(details!=null){
            recalculateClaimRIOutward(false,dtDLADate);
        }

    }

    public void recalculateClaimRIOutward( boolean validate,Date dtDLADate) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());

                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));

                }

                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));

                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);

                if (exludeComission)
                    if(det.isComission()) continue;


                if (det.isNegative()) {

                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));

                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {

                    tot=BDUtil.add(tot,amtIDR);

                    totE=BDUtil.add(totE,amtafterTax);

                }

            }


            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }

        if (isNegative()) {

            dbAmount = BDUtil.negate(dbAmount);

            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);

        }

        if (!isHasBeenPosted()) {

            stGLARAccountID=null;

            final String entityGLCode = stEntityID==null?null:getEntity().getStGLCode();

            //final String stGLAccountCode = getStGLAccountCode();

            final String entityDesc = getEntity().getStShortName()==null?getEntity().getStEntityName():getEntity().getStShortName();

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            final String policyTypeGLCode = getPolicyTypeGLCode();

            glApplicator.setCode('Y',entityGLCode);

            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);

            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1()!=null?getStReferenceA1():"");
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());
            glApplicator.setDesc("K",getStRefID2());
            glApplicator.setDesc("M",DateUtil.getMonth(dtDLADate).toUpperCase());
            glApplicator.setDesc("T",DateUtil.getYear(dtDLADate));

            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());

            String acc = getStGLAccountCode();

            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

            acc = LanguageManager.getInstance().translate(acc);

            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }

         /*stGLAccountCodeTR = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'Y', entityGLCode);
         stGLAccountCodeTR = GLUtil.applyCode(stGLAccountCodeTR, 'B', stCostCenterCode);*/

            //if (getPolicyType()==null) throw new RuntimeException("Data Integrity Error");



            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);

            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);

            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());

            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);

            ivd.setStGLAccountCodeTR(glc);*/

                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }

                if(ivd.getStTaxCode()!=null){
                    if(ivd.getStNegativeFlag().equalsIgnoreCase("N")){
                        EntityView entTax = ivd.getEntity();

                        glApplicator.setCode('Y',entTax.getStGLCode());
                        glApplicator.setDesc("Y",entTax.getStShortName());
                        final String accodeTax = ivd.getStGLAccountCode2();
                        if (accodeTax!=null) {
                            ivd.setStGLAccountID(glApplicator.getAccountID(accodeTax));
                            ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                        }

                        glApplicator.setCode('Y',entityGLCode);

                        if (getEntity()!=null)
                            glApplicator.setDesc("Y",entityDesc);
                    }
                }

            }
        }
    }

    public void recalculateTaxAcrual( boolean validate) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

                if (det.isTaxed()) {
                    det.setDbTaxRate(det.getTax().getDbRate());

                    det.setDbTaxAmount(BDUtil.mul(det.getDbTaxRate(), det.getDbEnteredAmount(),2));

                }

                final BigDecimal amt = det.getDbEnteredAmount();
                final BigDecimal amtafterTax = det.getDbEnteredAmountAfterTax();
                det.setDbAmount(BDUtil.mul(amt,getDbCurrencyRate(),2));

                final BigDecimal amtIDR = BDUtil.mul(amtafterTax,getDbCurrencyRate(),2);

                if (exludeComission)
                    if(det.isComission()) continue;


                if (det.isNegative()) {

                    tot=BDUtil.add(tot,BDUtil.negate(amtIDR));

                    totE=BDUtil.add(totE,BDUtil.negate(amtafterTax));
                } else {

                    tot=BDUtil.add(tot,amtIDR);

                    totE=BDUtil.add(totE,amtafterTax);

                }

            }


            dbAmount = tot;
            dbEnteredAmount = totE;
        } else {
            dbAmount = BDUtil.mul(dbEnteredAmount, dbCurrencyRate,2);
        }

        if (isNegative()) {

            dbAmount = BDUtil.negate(dbAmount);

            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);

        }

//         if (dbAmount!=null)
//            if(BDUtil.isNegative(dbAmount))
//            {
//                if (FinCodec.InvoiceType.AR.equalsIgnoreCase(stInvoiceType)) {
//                    setStInvoiceType(FinCodec.InvoiceType.AP);
//                } else {
//                    setStInvoiceType(FinCodec.InvoiceType.AR);
//                }
//            }

        if (!isHasBeenPosted()) {

            stGLARAccountID=null;

            final String entityGLCode = stEntityID==null?null:getPolicy().getEntity().getStGLCode();

            //final String stGLAccountCode = getStGLAccountCode();

            final String entityDesc = getPolicy().getEntity().getStShortName()==null?getPolicy().getEntity().getStEntityName():getPolicy().getEntity().getStShortName();

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            final String policyTypeGLCode = getPolicyTypeGLCode();

            glApplicator.setCode('Y',entityGLCode);

            if (getEntity()!=null)
                glApplicator.setDesc("Y",entityDesc);

            glApplicator.setCode('B',stCostCenterCode);
            glApplicator.setCode('X',policyTypeGLCode); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('A',getStReferenceA0());
            glApplicator.setDesc("A",getStReferenceA1());
            glApplicator.setCode('C',getStReferenceC0());
            glApplicator.setDesc("C",getStReferenceC1());
            glApplicator.setCode('D',getStReferenceD0());
            glApplicator.setDesc("D",getStReferenceD1());
            glApplicator.setCode('E',getStReferenceE0());
            glApplicator.setDesc("E",getStReferenceE1());
            //glApplicator.setCode('Y',getStReferenceY0());
            //glApplicator.setDesc("Y",getStReferenceY1());
            glApplicator.setCode('Z',getStReferenceZ0());
            glApplicator.setDesc("Z",getStReferenceZ1());

            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());

            //String acc = getStGLAccountCode();

            String artrxtype = getPolicy().getCoverSource().getStARTransactionTypeID();

            ARTransactionTypeView arTrxTypeAcrual = getARTrxTypeByID(artrxtype);

            String acc = arTrxTypeAcrual.getStGLARAccount();

            if (acc==null) throw new RuntimeException("Unable to retrieve account code, check ar_trx_type.ar_account + ap_account for ar_trx_type_id="+getStARTransactionTypeID());

            acc = LanguageManager.getInstance().translate(acc);

            if (acc!=null) {
                stGLARAccountID = glApplicator.getAccountID(acc);
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
                stGLARAccountDesc = glApplicator.getPreviewDesc();
                if (stGLARAccountID==null) throw new RuntimeException(glApplicator.errMsg);
            }

            for (int i = 0; i < details.size(); i++) {
                ARInvoiceDetailView ivd = (ARInvoiceDetailView) details.get(i);


                final String accode = ivd.getStGLAccountCode();
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getStGLDesc());
                }

                if(ivd.getStTaxFlag()!=null){
                    final String accodeTax = ivd.getStGLAccountCode2();
                    if (accodeTax!=null) {
                        ivd.setStGLAccountID(glApplicator.getAccountID(accodeTax));
                        ivd.setStAccountDesc(glApplicator.getStGLDesc());
                    }

                }
                if(BDUtil.isZeroOrNull(ivd.getDbEnteredAmount()))
                        throw new RuntimeException("Nilai nya nol : "+ ivd.getStAccountDesc());

            }
        }
    }

    public ARTransactionTypeView getARTrxTypeByID(String arTrxTypeID) {
        return (ARTransactionTypeView) DTOPool.getInstance().getDTO(ARTransactionTypeView.class, arTrxTypeID);
    }

    public String getStGLAccountCodeAcrual(String arTrxTypeID) {
        return isAP()?getStGLAPAccountCode():getStGLARAccountCode();
    }

    private DTOList list4;

    public DTOList getList4() {
        if (list4 == null) {
            if (!isNew()) {
                try {
                    String select = "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.no_surat_hutang = ? and coalesce(a.amount,0) <> 0 and a.amount_settled is null";

                    if (dtSuratHutangPeriodFrom != null) {
                        select = select + " and date_trunc('day',a.invoice_date) >= ?";
                    }

                    if (dtSuratHutangPeriodTo != null) {
                        select = select + " and date_trunc('day',a.invoice_date) <= ?";
                    }

                    if (dtAttrPolicyPeriodStart != null) {
                        select = select + " and date_trunc('day',a.attr_pol_per_0) >= ?";
                    }

                    if (dtAttrPolicyPeriodEnd != null) {
                        select = select + " and date_trunc('day',a.attr_pol_per_0) <= ?";
                    }

                    if (stAttrPolicyTypeID != null) {
                        select = select + " and a.attr_pol_type_id = " + stAttrPolicyTypeID;
                    }

                    if (stEntityID != null) {
                        select = select + " and a.ent_id = " + stEntityID;
                    }

                    select = select +" order by a.ar_invoice_id,b.ar_invoice_dtl_id";

                    if (dtSuratHutangPeriodFrom != null && dtSuratHutangPeriodTo != null && dtAttrPolicyPeriodStart != null && dtAttrPolicyPeriodEnd != null) {
                        list4 = ListUtil.getDTOListFromQuery(
                                select,
                                new Object[]{stNoSuratHutang, dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo, dtAttrPolicyPeriodStart, dtAttrPolicyPeriodEnd},
                                HashDTO.class);
                    } else {
                        list4 = ListUtil.getDTOListFromQuery(
                                select,
                                new Object[]{stNoSuratHutang},
                                HashDTO.class);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list4;
    }

    private DTOList listDetailsSuratHutangComm;

    public DTOList getListDetailsSuratHutangComm() {
        if (listDetailsSuratHutangComm == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutangComm = ListUtil.getDTOListFromQuery(
                            "select a.* from ar_invoice a "
                            + "where a.no_surat_hutang = ? order by 1 ",
                            new Object[]{stNoSuratHutang},
                            ARInvoiceView.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutangComm;
    }

    public void setListDetailsSuratHutangComm(DTOList listDetailsSuratHutangComm) {
        this.listDetailsSuratHutangComm = listDetailsSuratHutangComm;
    }

    public BigDecimal getTagihanNetto(){
        final DTOList details = getDetails();

        BigDecimal nettoAmount = null;

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            final BigDecimal amtIDR = BDUtil.mul(det.getDbAmount(),getDbCurrencyRate(),2);

            if(det.isNegative())
                nettoAmount = BDUtil.sub(nettoAmount, amtIDR);
            else
                nettoAmount = BDUtil.add(nettoAmount, amtIDR);
        }

        return nettoAmount;
    }

    private DTOList list5;

    public DTOList getList5() {

        String noSHK = null;
        String noSHKArray[] = stNoSuratHutang.split("[\\;]");

        if (noSHKArray.length == 1) {
            noSHK = "and a.no_surat_hutang = '" + stNoSuratHutang + "'";
        } else if (noSHKArray.length > 1) {
            noSHK = "and a.no_surat_hutang in ('" + noSHKArray[0] + "'";
            for (int k = 1; k < noSHKArray.length; k++) {
                noSHK = noSHK + ",'" + noSHKArray[k] + "'";
            }
            noSHK = noSHK + ")";
        }

        if (list5 == null) {
            if (!isNew()) {
                try {
                    String select = "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang "
                            + "from ar_invoice a "
                            + "inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id "
                            + "where coalesce(a.amount,0) <> 0 and a.amount_settled is null ";

                    select = select + noSHK;

                    if (stInvoiceType.equalsIgnoreCase("1")) {
                        select = select + " and a.attr_pol_type_id not in (2,3,18,20,21,23,24,31,32,33,41,42,43,44,59,60,64,69,70,71,72,73,74,83,85,92,95,96,97)";
                    }

                    if (stAttrPolicyTypeID != null) {
                        select = select + " and a.attr_pol_type_id = " + stAttrPolicyTypeID;
                    }

                    if (stEntityID != null) {
                        select = select + " and a.ent_id = " + stEntityID;
                    }

                    list5 = ListUtil.getDTOListFromQuery(
                            select,
                            HashDTO.class);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list5;
    }

    public void setList5(DTOList list5) {
        this.list5 = list5;
    }

    public boolean isAdjusterFee(){
        return "26".equalsIgnoreCase(stARTransactionTypeID);
    }

    /**
     * @return the stReferenceNo
     */
    public String getStReferenceNo() {
        return stReferenceNo;
    }

    /**
     * @param stReferenceNo the stReferenceNo to set
     */
    public void setStReferenceNo(String stReferenceNo) {
        this.stReferenceNo = stReferenceNo;
    }


    public DTOList getListNomorRekap() {
        if (list2==null) {
            if (!isNew()) {
                try {

                    String select = "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where coalesce(a.amount,0) <> 0 and a.amount_settled is null";

                    if(stNoSuratHutang!=null){
                        select = select + " and a.no_surat_hutang = ?";
                    }

                    if(dtSuratHutangPeriodFrom!=null){
                        select = select + " and date_trunc('day',a.surat_hutang_period_from) >= ?";
                    }

                    if(dtSuratHutangPeriodTo!=null){
                        select = select + " and date_trunc('day',a.surat_hutang_period_from) <= ?";
                    }

                    if(stEntityID!=null){
                        select = select + " and a.ent_id = ?";
                    }

                    select = select +" order by a.ar_invoice_id,b.ar_invoice_dtl_id";

                    if(dtSuratHutangPeriodFrom!=null && dtSuratHutangPeriodTo!=null && stNoSuratHutang!=null && stEntityID!=null){
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang, dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo,stEntityID},
                            HashDTO.class
                            );
                    }else if(dtSuratHutangPeriodFrom!=null && dtSuratHutangPeriodTo!=null
                            && stNoSuratHutang==null && stEntityID!=null){
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo, stEntityID},
                            HashDTO.class
                            );
                    } else if(stNoSuratHutang!=null && stEntityID!=null){
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang, stEntityID},
                            HashDTO.class
                            );
                    }else if(dtSuratHutangPeriodFrom!=null && dtSuratHutangPeriodTo!=null
                            && stNoSuratHutang!=null && stEntityID==null){
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang, dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo},
                            HashDTO.class
                            );
                    }else {
                        list2 = ListUtil.getDTOListFromQuery(
                            select ,
                            new Object[] {stNoSuratHutang},
                            HashDTO.class
                            );
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list2;
    }

    public BigDecimal getTotalTagihanNetto(){

        BigDecimal total = BDUtil.zero;

        final DTOList details2 = getDetails();

        for (int i = 0; i < details2.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details2.get(i);

            if(!det.isNegative())
                total = BDUtil.add(total, det.getDbAmount());

            if(det.isNegative())
                total = BDUtil.sub(total, det.getDbAmount());

        }

        return total;
    }

    private DTOList list6;

    public DTOList getList6(String ccy) {
        if (list6 == null) {
            if (!isNew()) {
                try {
                    String select = "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.reference_no = ? and coalesce(a.amount,0) <> 0 and a.amount_settled is null";

                    if (dtSuratHutangPeriodFrom != null) {
                        select = select + " and date_trunc('day',a.invoice_date) >= ?";
                    }

                    if (dtSuratHutangPeriodTo != null) {
                        select = select + " and date_trunc('day',a.invoice_date) <= ?";
                    }

                    if (dtAttrPolicyPeriodStart != null) {
                        select = select + " and date_trunc('day',a.attr_pol_per_0) >= ?";
                    }

                    if (dtAttrPolicyPeriodEnd != null) {
                        select = select + " and date_trunc('day',a.attr_pol_per_0) <= ?";
                    }

                    if (stAttrPolicyTypeID != null) {
                        select = select + " and a.attr_pol_type_id = " + stAttrPolicyTypeID;
                    }

                    if (stEntityID != null) {
                        select = select + " and a.ent_id = " + stEntityID;
                    }

                    if (ccy != null) {
                        select = select + " and a.ccy = '" + ccy +"'";
                    }


                    if (dtSuratHutangPeriodFrom != null && dtSuratHutangPeriodTo != null && dtAttrPolicyPeriodStart != null && dtAttrPolicyPeriodEnd != null) {
                        list6 = ListUtil.getDTOListFromQuery(
                                select,
                                new Object[]{stNoSuratHutang, dtSuratHutangPeriodFrom, dtSuratHutangPeriodTo, dtAttrPolicyPeriodStart, dtAttrPolicyPeriodEnd},
                                HashDTO.class);
                    } else {
                        list6 = ListUtil.getDTOListFromQuery(
                                select,
                                new Object[]{stNoSuratHutang},
                                HashDTO.class);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list6;
    }

public DTOList getListDetailsSuratHutangCommNew() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_surat_hutang = ? "
                            + " and b.ar_trx_line_id in (8,24,40,56) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoSuratHutang},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

public DTOList getListDetailsSHKRefundIzinCair() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_izin_pencairan = ? "
                            + " and a.ar_trx_type_id in (5,6,7) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoIzinCair},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    public DTOList getListDetailsSHKClaimIzinCair() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_izin_pencairan = ? "
                            + " and a.ar_trx_type_id in (12) and coalesce(a.cancel_flag,'') <> 'Y'  "
                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoIzinCair},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    private String stNoIzinCair;
    /**
     * @return the stNoIzinCair
     */
    public String getStNoIzinCair() {
        return stNoIzinCair;
    }

    /**
     * @param stNoIzinCair the stNoIzinCair to set
     */
    public void setStNoIzinCair(String stNoIzinCair) {
        this.stNoIzinCair = stNoIzinCair;
    }

    public DTOList getListDetailsSHKKomisiIzinCair() {
        if (listDetailsSuratHutang == null) {
            if (!isNew()) {
                try {
                    listDetailsSuratHutang = ListUtil.getDTOListFromQuery(
                            "select a.*,b.ar_invoice_dtl_id,b.description as ket,round(b.amount/a.ccy_rate,2) as utang from ar_invoice a "
                            + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                            + " where a.no_izin_pencairan = ? "
                            + " and b.ar_trx_line_id in (8,24,40,56) and coalesce(a.cancel_flag,'') <> 'Y' ",
//                            + " and case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0",
                            new Object[]{stNoIzinCair},
                            HashDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listDetailsSuratHutang;
    }

    public InsurancePolicyInwardView getInward() {
        return (InsurancePolicyInwardView) DTOPool.getInstance().getDTO(InsurancePolicyInwardView.class, stAttrPolicyID);
    }

    public boolean isPolicyKSG(){
        boolean isKSG = false;

        if(stAttrPolicyTypeID.equalsIgnoreCase("87") || stAttrPolicyTypeID.equalsIgnoreCase("88"))
            isKSG = true;

        return isKSG;
    }


   
}

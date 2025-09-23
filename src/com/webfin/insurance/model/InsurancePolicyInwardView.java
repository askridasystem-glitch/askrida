/***********************************************************************
 * Module:  com.webfin.ar.model.InsurancePolicyInwardView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:05:46 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.crux.lang.LanguageManager;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.util.GLUtil;
import com.webfin.FinCodec;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARTransactionTypeView;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.time.DateTime;

public class InsurancePolicyInwardView extends DTO implements RecordAudit {
    
    public static boolean exludeComission = false;
    
    private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyInwardView.class);
    
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
    public static String tableName = "ins_pol_inward";
    public static String comboFields[] = {"attr_pol_id", "attr_pol_no", "pla_no", "dla_no"};
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
        {"dbPerHole","perhole"},
        {"dbSharePct","sharepct"},
        {"dtReference2","refd2"},
        {"dbClaimAmountTotal","claim_amount_total"},
        {"dbClaimAmountOwnRisk","claim_amount_or"},
        {"stActiveFlag","active_flag"},
        {"stEffectiveFlag","effective_flag"},
        {"stCustomerID","customer_id"},
        {"stCustomerName","customer_name"},
        {"dtPLADate","pla_date"},
        {"dtDLADate","dla_date"},
        {"stPLANo","pla_no"},
        {"stDLANo","dla_no"},
        {"stTransactionNoReference","trx_no_reference"},
        {"stAttrQuartalYear","attr_quartal_year"},
        {"dbClaimEntered", "claim_entered*n"},
        {"dbProfitComm", "profitcomm*n"},
        {"dbProfitCommEntered", "profitcomm_entered*n"},
        {"dbReference1", "refn1*n"},
        {"dbReference2", "refn2*n"},
        {"dbReference3", "refn3*n"},
        {"dbReference4", "refn4*n"},
        {"dbReference5", "refn5*n"},
        {"dbReference6", "refn6*n"},
        {"dbBfee", "bfee*n"},
        {"dbReco", "reco*n"},
        {"dbBfeeEntered", "bfee_entered*n"},
        {"dbRecoEntered", "reco_entered*n"},
        {"dbClaim", "claim*n"},
        {"stInstallmentPeriodID", "inst_period_id"},
        {"stInstallmentPeriods", "inst_periods"},
        {"stInstallmentOptions", "inst_options"},
        {"stInstallmentDaysAmount", "inst_days_amount"},
        {"stInstallmentManualFlag", "inst_manual_f"},
        {"stUserInput", "user_input*n"},
        {"stAttachment1","attachment1"},
        {"stAttachment2","attachment2"},
        {"stAttachment3","attachment3"},
        {"stReinsuranceEntityID","reins_ent_id"},
        {"stReceiptNo","receipt_no*n"},
        {"stCreateName", "create_name*n"},
        {"stPolicyCurrencyCode","ccy_polis"},
        {"stFilePhysic2","file_physic2"},
        {"stFilePhysic3","file_physic3"},
    };
    
    public InsurancePolicyInwardView() {
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
    private InsurancePolicyInwardView ref;
    private Date dtReceipt;
    
    public DTOList objects;
    private Class clObjectClass;
    
    private DTOList list3;
    
    private String stClaimNo;
    private String stClaimName;
    private String stClaimCoinsID;
    private String stClaimCoinsName;
    private String stClaimCoinsAddress;
    
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
    
    private Date dtTransDate;
    
    private DTOList arInvoiceDetailsCoins;
    
    
    private BigDecimal dbPerHole;
    private BigDecimal dbSharePct;
    private Date dtReference2;
    private BigDecimal dbClaimAmountTotal;
    private BigDecimal dbClaimAmountOwnRisk;
    private String stActiveFlag;
    private String stEffectiveFlag;
    private String stCustomerID;
    private String stCustomerName;
    private Date dtPLADate;
    private Date dtDLADate;
    private String stPLANo;
    private String stDLANo;
    private String stTransactionNoReference;
    private String stAttrQuartalYear;

    private BigDecimal dbClaim;
    private BigDecimal dbClaimEntered;
    private BigDecimal dbProfitComm;
    private BigDecimal dbProfitCommEntered;

    private BigDecimal dbReference1;
    private BigDecimal dbReference2;
    private BigDecimal dbReference3;
    private BigDecimal dbReference4;
    private BigDecimal dbReference5;
    private BigDecimal dbReference6;
    private boolean endorseMode = false;

    private BigDecimal dbBfee;
    private BigDecimal dbReco;
    private BigDecimal dbBfeeEntered;
    private BigDecimal dbRecoEntered;

    private String stInstallmentPeriodID;
    private String stInstallmentPeriods;
    private DTOList installment;
    private String stInstallmentOptions;
    private String stInstallmentDaysAmount;
    private String stInstallmentManualFlag;
    private String stUserInput;

    private String stAttachment1;
    private String stAttachment2;
    private String stAttachment3;

    private DTOList invoices;
    
    private String stReceiptNo;
    private String stCreateName;
    private String stReinsuranceEntityID;
    private String stPolicyCurrencyCode;
    private String stFilePhysic2;
    private String stFilePhysic3;

    public String getStFilePhysic3() {
        return stFilePhysic3;
    }

    public void setStFilePhysic3(String stFilePhysic3) {
        this.stFilePhysic3 = stFilePhysic3;
    }

    public String getStFilePhysic2() {
        return stFilePhysic2;
    }

    public void setStFilePhysic2(String stFilePhysic2) {
        this.stFilePhysic2 = stFilePhysic2;
    }

    public String getStPolicyCurrencyCode() {
        return stPolicyCurrencyCode;
    }

    public void setStPolicyCurrencyCode(String stPolicyCurrencyCode) {
        this.stPolicyCurrencyCode = stPolicyCurrencyCode;
    }

    public String getStReinsuranceEntityID() {
        return stReinsuranceEntityID;
    }

    public void setStReinsuranceEntityID(String stReinsuranceEntityID) {
        this.stReinsuranceEntityID = stReinsuranceEntityID;
    }

    public String getStCreateName() {
        return stCreateName;
    }

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    public String getStReceiptNo() {
        return stReceiptNo;
    }

    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public DTOList getInvoices() throws Exception {
        if (invoices == null){
            loadInvoices();
        }
        return invoices;
    }

    public void setInvoices(DTOList invoices) {
        this.invoices = invoices;
    }

    public void loadInvoices() throws Exception {
        try {

            String invoiceNo = "%"+ getStInvoiceNo() + "%";

            if (invoices == null) {
                invoices = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ar_invoice"
                        + "   where"
                        + "      reference_no = ? and invoice_no like ? order by ar_invoice_id",
                        new Object[]{getStTransactionNoReference(), invoiceNo },
                        ARInvoiceView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStAttachment1() {
        return stAttachment1;
    }

    public void setStAttachment1(String stAttachment1) {
        this.stAttachment1 = stAttachment1;
    }

    public String getStAttachment2() {
        return stAttachment2;
    }

    public void setStAttachment2(String stAttachment2) {
        this.stAttachment2 = stAttachment2;
    }

    public String getStAttachment3() {
        return stAttachment3;
    }

    public void setStAttachment3(String stAttachment3) {
        this.stAttachment3 = stAttachment3;
    }


    public String getStUserInput() {
        return stUserInput;
    }

    public void setStUserInput(String stUserInput) {
        this.stUserInput = stUserInput;
    }

    public String getStInstallmentDaysAmount() {
        return stInstallmentDaysAmount;
    }

    public void setStInstallmentDaysAmount(String stInstallmentDaysAmount) {
        this.stInstallmentDaysAmount = stInstallmentDaysAmount;
    }

    public String getStInstallmentManualFlag() {
        return stInstallmentManualFlag;
    }

    public void setStInstallmentManualFlag(String stInstallmentManualFlag) {
        this.stInstallmentManualFlag = stInstallmentManualFlag;
    }

    public Date getDtReceipt() {
        return dtReceipt;
    }
    
    public void setDtReceipt(Date dtReceipt) {
        this.dtReceipt = dtReceipt;
    }
    
    public InsurancePolicyInwardView getRef() {
        //if (stRefInvoiceDetailID!=null) throw new RuntimeException("ref object not linked");
        //return ref;
        return (InsurancePolicyInwardView) DTOPool.getInstance().getDTO(InsurancePolicyInwardView.class, stRefInvoiceID);
        
    }
    
    public void setRef(InsurancePolicyInwardView ref) {
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
                            "select a.* "+
                            " from ins_pol_inward_details a "+
                            " left join ar_trx_line b on a.ar_trx_line_id = b.ar_trx_line_id "+
                            " where ar_invoice_id = ? "+
                            " order by b.order_no",
                            new Object[] {stARInvoiceID},
                            InsurancePolicyInwardDetailView.class
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
                            "select * from ins_pol_inward where ar_invoice_id = ?",
                            new Object[] {stARInvoiceID},
                            InsurancePolicyInwardView.class
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
    
    public DTOList getList2() {
        if (list2==null) {
            if (!isNew()) {
                try {
                    list2 = ListUtil.getDTOListFromQuery(
                            "select * from ar_invoice where no_surat_hutang = ?",
                            new Object[] {stNoSuratHutang},
                            InsurancePolicyInwardView.class
                            );
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
                        InsurancePolicyInwardDetailView invDetail = (InsurancePolicyInwardDetailView) invoiceDetail.get(i);
    
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
    
    public void validate() throws Exception {
        
        //if (stGLARAccountID==null) throw new RuntimeException("AR/AP Account not defined");
        
        if (stGLARAccountID==null && stARTransactionTypeID==null) throw new RuntimeException("AR/AP TRX not defined");
        
        if (stCostCenterCode==null) throw new RuntimeException("You must supply cost center for invoice !");
        
        if (stCurrencyCode==null) throw new RuntimeException("You must supply currency code for invoice !");
        
        if (dbCurrencyRate==null) throw new RuntimeException("You must supply currency rate for invoice !");

        if(BDUtil.isZeroOrNull(dbCurrencyRate))
            throw new RuntimeException("rate kurs tidak boleh kosong/nol");

        if(!getStCurrencyCode().equalsIgnoreCase("IDR")){
            if(BDUtil.lesserThanEqual(getDbCurrencyRate(), BDUtil.one))
                throw new RuntimeException("Cek kurs mata uang apakah sudah diisi dengan benar");
        }

        //CEK JIKA REAS INWARD
        if(stARTransactionTypeID.equalsIgnoreCase("1") || stARTransactionTypeID.equalsIgnoreCase("2") || stARTransactionTypeID.equalsIgnoreCase("3")
                || stARTransactionTypeID.equalsIgnoreCase("20") || stARTransactionTypeID.equalsIgnoreCase("21") || stARTransactionTypeID.equalsIgnoreCase("22")){
            
            if(isManualCicilan()){
                if(getInstallment().size()<1)
                    throw new RuntimeException("Detail Cicilan belum diinput");
            }

            if(stInstallmentOptions!=null || stInstallmentPeriodID!=null){
                if(getInstallment().size()<1)
                    throw new RuntimeException("Detail Cicilan belum diinput");
            }
        }
        

        /*
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
            
            if (det.isComission()) {
                if (det.getStEntityID()==null) throw new RuntimeException("Comission should be assigned to an agent entity");
            }
        }*/
    }

    public void validate2() {

        if(!getStCurrencyCode().equalsIgnoreCase("IDR")){
            if(BDUtil.lesserThanEqual(getDbCurrencyRate(), BDUtil.one))
                throw new RuntimeException("Cek kurs mata uang apakah sudah diisi dengan benar");
        }

        DateTime mutationDate = new DateTime(dtMutationDate);
        Date mutationDatePlus = mutationDate.plusMonths(1).toDate();
        String date = "01/" + DateUtil.getMonthDigit(mutationDatePlus) + "/" + DateUtil.getYear(mutationDatePlus);
        DateTime currentDateLastDay = new DateTime(DateUtil.getDate(date));

        Date maximumBackDate = currentDateLastDay.plusDays(9).toDate();

        boolean compare = Tools.compare(new Date(), maximumBackDate) > 0;

        if (!DateUtil.getDateStr(dtMutationDate, "^^ yyyy").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "^^ yyyy"))) {
            if (compare) {
                throw new RuntimeException("Batas waktu 10 hari setelah akhir bulan sudah terlewati");
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
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
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
            if(BDUtil.isNegative(dbAmount)) {
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
            glApplicator.setDesc("K",getStDLANo()!=null?getStDLANo():getStPLANo());
            glApplicator.setDesc("M",DateUtil.getMonth(getDtDueDate()).toUpperCase());
            glApplicator.setDesc("T",DateUtil.getYear(getDtDueDate()));
            
            if (getPolicyType()!=null)
                glApplicator.setDesc("X",getPolicyType().getStShortDescription());

            InsurancePolicyInwardDetailView det2 = (InsurancePolicyInwardDetailView) details.get(0);

            if(policyTypeGLCode==null)
                glApplicator.setCode('X',det2.getPolicyType().getStGLCode()); //change these into a better system (use var interfaces & inner class)

            if (getPolicyType()==null)
                glApplicator.setDesc("X",det2.getPolicyType().getStDescription());
            
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
            InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
            
            det.setDbEnteredAmount(BDUtil.negate(det.getDbEnteredAmount()));
        }
    }
    
    private String getPolicyTypeGLCode() {
        return stAttrPolicyTypeID==null?null:getPolicyType().getStGLCode();
    }
    
    private boolean isHasBeenPosted() {
        if (getOldInvoice()==null) return false;
        return Tools.isYes(getOldInvoice().getStPostedFlag());
        
    }
    
    private InsurancePolicyInwardView getOldInvoice() {
        return (InsurancePolicyInwardView) getOld();
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
                            "where a.claim_no = ? order by a.cc_code,b.dla_no",
                            new Object[] {stClaimNo},
                            InsurancePolicyInwardView.class
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
            if (arInvoiceDetailsCoins==null)
                arInvoiceDetailsCoins = ListUtil.getDTOListFromQuery(
                        "select *,checkpremi(ar_trx_type_id = '6',(amount+attr_pol_tsi+entered_amount),amount) as amount from ( "+
                        "select a.attr_pol_no,a.ar_invoice_id,a.ent_id,a.ar_trx_type_id, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '55',b.amount*-1),checkreas(c.ar_trx_line_id = '23',b.amount))) as amount, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '29',b.amount))) as disc, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '28',b.amount))) as bpol, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.ar_trx_line_id = '27',b.amount))) as bmat, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'COMMISSION',b.amount))) as amount_settled, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'TAXCOMM',b.amount))) as ccy_rate, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '71',b.amount*-1),checkreas(c.category = 'BROKERAGE',b.amount))) as attr_pol_tsi, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',0,checkreas(c.category = 'TAXBROK',b.amount))) as attr_pol_tsi_total, "+
                        "sum(checkpremi(a.ar_trx_type_id = '6',checkreas(c.ar_trx_line_id = '72',b.amount*-1),checkreas(c.category = 'HFEE',b.amount))) as entered_amount "+
                        "from ar_invoice a "+
                        "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id "+
                        "left join ar_trx_line c on c.ar_trx_line_id = b.ar_trx_line_id "+
                        "where attr_pol_id = ? and a.ar_trx_type_id in (6,10) and a.ent_id not in ('94','96') "+
                        "group by a.attr_pol_no,a.ar_invoice_id,a.ent_id,a.ar_trx_type_id "+
                        " ) x order by ar_invoice_id,ent_id ",
                        new Object [] {stAttrPolicyID},
                        InsurancePolicyInwardView.class
                        );
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
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
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
            
            InsurancePolicyInwardDetailView det2 = (InsurancePolicyInwardDetailView) details.get(0);
            
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",getEntity().getStEntityName());
            
            if(getEntity()==null){
                glApplicator.setCode('Y',"00000");
                glApplicator.setDesc("Y","");
            }
            
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
            glApplicator.setDesc("K",getStDLANo()!=null?getStDLANo():getStPLANo());
            glApplicator.setDesc("M",DateUtil.getMonth(getDtDueDate()).toUpperCase());
            glApplicator.setDesc("T",DateUtil.getYear(getDtDueDate()));
            
            if(policyTypeGLCode==null)
                glApplicator.setCode('X',det2.getPolicyType().getStGLCode()); //change these into a better system (use var interfaces & inner class)
            
            if (getPolicyType()==null)
                glApplicator.setDesc("X",det2.getPolicyType().getStDescription());
            
            
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
                BigDecimal totPerLine = null;
                BigDecimal totPerLineE = null;
                
                final DTOList detailsInvoice = det.getDetails();
                for (int j = 0; j < detailsInvoice.size(); j++) {
                    InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) detailsInvoice.get(j);
                    
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
            //negateDetails();
       
            recalculate();
       
            return;
         }*/
        
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
            
            //final String policyTypeGLCode = getPolicyTypeGLCode();
            
            InsurancePolicyInwardDetailView det2 = (InsurancePolicyInwardDetailView) details.get(0);
            
            glApplicator.setCode('Y',entityGLCode);
            
            if (getEntity()!=null)
                glApplicator.setDesc("Y",getEntity().getStEntityName());
            
            glApplicator.setCode('B',stCostCenterCode);
            if (det2!=null)
                glApplicator.setCode('X',det2.getPolicyTypeInward().getStGLCode()); //change these into a better system (use var interfaces & inner class)
             
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
                glApplicator.setDesc("X",det2.getPolicyTypeInward().getStDescription());
            
            if (getPolicyType()==null)
                glApplicator.setDesc("X",det2.getPolicyTypeInward().getStDescription());
            
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                glApplicator.setCode('X',ivd.getPolicyTypeGLCodeInward());
                glApplicator.setDesc("X",ivd.getPolicyTypeInward().getStDescription());
                if (acc!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(acc));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
                
                
                final DTOList detailsInvoice = ivd.getDetails();
                for (int j = 0; j < detailsInvoice.size(); j++) {
                    InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) detailsInvoice.get(j);
                    
                    final String accode = detail.getStGLAccountCode();
                    glApplicator.setCode('X',detail.getPolicyTypeGLCodeInward());
                    glApplicator.setDesc("X",detail.getPolicyTypeInward().getStDescription());
                    if (accode!=null) {
                        detail.setStGLAccountID(glApplicator.getAccountID(accode));
                        detail.setStAccountDesc(glApplicator.getPreviewDesc());
                    }
                } 
                
                
            }
        }
    }
    
    public DTOList getDetailsInwardTreaty() {
        if (details==null) {
            try {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_inward_details where ar_invoice_id = ? and ar_trx_line_id is null order by ar_invoice_dtl_id",
                        new Object[] {stARInvoiceID},
                        InsurancePolicyInwardDetailView.class
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
    
    public void recalculateClaimInwardTreaty() {
        recalculateClaimInwardTreaty(false);
    }
    
    public void recalculateClaimInwardTreaty( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
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
            
            String policyTypeGLCode = getPolicyTypeGLCode();
            if(policyTypeGLCode==null)
                policyTypeGLCode = getPolicyTypeGLCodeFromDetail();
            
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
            else
                glApplicator.setDesc("X",getPolicyTypeFromDetail().getStDescription());
            
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
            /*ivd.setStGLAccountCodeTR(null);
            ivd.setStGLAccountID(null);
             
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(ivd.getStGLAccountCode());
             
            glc = GLUtil.applyCode(glc,'X', policyTypeGLCode);
            glc = GLUtil.applyCode(glc,'B', stCostCenterCode);
             
            ivd.setStGLAccountCodeTR(glc);*/
                
                final String accode = ivd.getStGLAccountCode();
                if(ivd.getPolicyType()!=null){
                    glApplicator.setCode('X',ivd.getPolicyTypeGLCode());
                    glApplicator.setDesc("X",ivd.getPolicyType().getStDescription());
                }
                
                if (accode!=null) {
                    ivd.setStGLAccountID(glApplicator.getAccountID(accode));
                    ivd.setStAccountDesc(glApplicator.getPreviewDesc());
                }
            }
        }
    }
    
    public boolean isApproved() {
        return Tools.isYes(stApprovedFlag);
    }
    
    public InsurancePolicyTypeView getStPolicyTypeDesc() {
        final InsurancePolicyTypeView polType = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stAttrPolicyTypeID);
        
        return polType;
    }
    
    public BigDecimal getDbSharePct() {
        return dbSharePct;
    }
    
    public void setDbSharePct(BigDecimal dbSharePct) {
        this.dbSharePct = dbSharePct;
    }
    
    public BigDecimal getDbPerHole() {
        return dbPerHole;
    }
    
    public void setDbPerHole(BigDecimal dbPerHole) {
        this.dbPerHole = dbPerHole;
    }
    
    private String getPolicyTypeGLCodeFromDetail() {
        
        final DTOList detail = getDetails();
        InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) detail.get(0);
        
        return det.getStAttrPolicyTypeID()==null?null:det.getStAttrPolicyTypeID();
    }
    
    public InsurancePolicyTypeView getPolicyTypeFromDetail() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, getPolicyTypeGLCodeFromDetail());
    }
    
    public Date getDtReference2() {
        return dtReference2;
    }
    
    public void setDtReference2(Date dtReference2) {
        this.dtReference2 = dtReference2;
    }
    
    /**
     * @return the dbClaimAmountTotal
     */
    public BigDecimal getDbClaimAmountTotal() {
        return dbClaimAmountTotal;
    }
    
    /**
     * @param dbClaimAmountTotal the dbClaimAmountTotal to set
     */
    public void setDbClaimAmountTotal(BigDecimal dbClaimAmountTotal) {
        this.dbClaimAmountTotal = dbClaimAmountTotal;
    }
    
    /**
     * @return the dbClaimAmountOwnRisk
     */
    public BigDecimal getDbClaimAmountOwnRisk() {
        return dbClaimAmountOwnRisk;
    }
    
    /**
     * @param dbClaimAmountOwnRisk the dbClaimAmountOwnRisk to set
     */
    public void setDbClaimAmountOwnRisk(BigDecimal dbClaimAmountOwnRisk) {
        this.dbClaimAmountOwnRisk = dbClaimAmountOwnRisk;
    }
    
    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }
    
    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }
    
    /**
     * @return the stEffectiveFlag
     */
    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }
    
    /**
     * @param stEffectiveFlag the stEffectiveFlag to set
     */
    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }
    
    public void recalculateClaimInward( boolean validate) {
        
        BigDecimal tot = null;
        BigDecimal totE = null;
        
        ARTransactionTypeView arTrxType = getARTrxType();
        
        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");
        
        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);
                
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
            
            InsurancePolicyInwardDetailView det2 = (InsurancePolicyInwardDetailView) details.get(0);
            
            
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
            
            if(policyTypeGLCode==null)
                glApplicator.setCode('X',det2.getPolicyType().getStGLCode()); //change these into a better system (use var interfaces & inner class)
            
            if (getPolicyType()==null)
                glApplicator.setDesc("X",det2.getPolicyType().getStDescription());
            
            
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
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
                
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
    
    /**
     * @return the stCustomerID
     */
    public String getStCustomerID() {
        return stCustomerID;
    }
    
    /**
     * @param stCustomerID the stCustomerID to set
     */
    public void setStCustomerID(String stCustomerID) {
        this.stCustomerID = stCustomerID;
    }
    
    /**
     * @return the stCustomerName
     */
    public String getStCustomerName() {
        return stCustomerName;
    }
    
    /**
     * @param stCustomerName the stCustomerName to set
     */
    public void setStCustomerName(String stCustomerName) {
        this.stCustomerName = stCustomerName;
    }
    
    /**
     * @return the dtPLADate
     */
    public Date getDtPLADate() {
        return dtPLADate;
    }
    
    /**
     * @param dtPLADate the dtPLADate to set
     */
    public void setDtPLADate(Date dtPLADate) {
        this.dtPLADate = dtPLADate;
    }
    
    /**
     * @return the dtDLADate
     */
    public Date getDtDLADate() {
        return dtDLADate;
    }
    
    /**
     * @param dtDLADate the dtDLADate to set
     */
    public void setDtDLADate(Date dtDLADate) {
        this.dtDLADate = dtDLADate;
    }
    
    /**
     * @return the stPLANo
     */
    public String getStPLANo() {
        return stPLANo;
    }
    
    /**
     * @param stPLANo the stPLANo to set
     */
    public void setStPLANo(String stPLANo) {
        this.stPLANo = stPLANo;
    }
    
    public boolean isPLA(){
        return stClaimStatus.equalsIgnoreCase(FinCodec.ClaimStatus.PLA);
    }
    
    public boolean isDLA(){
        return stClaimStatus.equalsIgnoreCase(FinCodec.ClaimStatus.DLA);
    }
    
    public boolean isEffective(){
        return Tools.isYes(stEffectiveFlag);
    }
    
    /**
     * @return the stTransactionNoReference
     */
    public String getStTransactionNoReference() {
        return stTransactionNoReference;
    }
    
    /**
     * @param stTransactionNoReference the stTransactionNoReference to set
     */
    public void setStTransactionNoReference(String stTransactionNoReference) {
        this.stTransactionNoReference = stTransactionNoReference;
    }
    
    public String generateNoBukti() throws Exception {
        
        // J 01 0913 00001
        
        String header = isAR()?"J":"M";
        
        String counterKey =
                DateUtil.getYear2Digit(getDtMutationDate())+
                DateUtil.getMonth2Digit(getDtMutationDate());
        
        String rn = String.valueOf(IDFactory.createNumericID("INWARDNO" + counterKey,1));
        
        rn = StringTools.leftPad(rn,'0', 5);
        
        String treatyTypeID = null;
        
        if(getStRefID0()!=null){
            InsuranceTreatyTypesView treaty = getTreatyType(getStRefID0());
            treatyTypeID = treaty.getStTransactionNoHeader();
        }
        
        InsurancePolicyInwardDetailView detil = (InsurancePolicyInwardDetailView) getDetails().get(0);
        
        if(detil.getStRefID0()!=null){
            InsuranceTreatyTypesView treaty = getTreatyType(detil.getStRefID0());
            treatyTypeID = treaty.getStTransactionNoHeader();
        }
        
        if(getStARTransactionTypeID().equalsIgnoreCase("1")) treatyTypeID = "13";
        if(getStARTransactionTypeID().equalsIgnoreCase("3")) treatyTypeID = "07";
        if(getStARTransactionTypeID().equalsIgnoreCase("22")) treatyTypeID = "07";
        
        treatyTypeID = StringTools.leftPad(treatyTypeID,'0', 2);
        
        return stInvoiceNo =
                header +
                treatyTypeID +
                DateUtil.getMonth2Digit(getDtMutationDate())+
                DateUtil.getYear2Digit(getDtMutationDate()) +
                rn;
        
    }
    
    public InsuranceTreatyTypesView getTreatyType(String stTreatyTypeID) {
        return (InsuranceTreatyTypesView) DTOPool.getInstance().getDTO(InsuranceTreatyTypesView.class, stTreatyTypeID);
        
    }
    
    public String getStAttrQuartalYear() {
        return stAttrQuartalYear;
    }
    
    public void setStAttrQuartalYear(String stAttrQuartalYear) {
        this.stAttrQuartalYear = stAttrQuartalYear;
    }

    /**
     * @return the dbClaim
     */
    public BigDecimal getDbClaim() {
        return dbClaim;
    }

    /**
     * @param dbClaim the dbClaim to set
     */
    public void setDbClaim(BigDecimal dbClaim) {
        this.dbClaim = dbClaim;
    }

    /**
     * @return the dbClaimEntered
     */
    public BigDecimal getDbClaimEntered() {
        return dbClaimEntered;
    }

    /**
     * @param dbClaimEntered the dbClaimEntered to set
     */
    public void setDbClaimEntered(BigDecimal dbClaimEntered) {
        this.dbClaimEntered = dbClaimEntered;
    }

    /**
     * @return the dbProfitComm
     */
    public BigDecimal getDbProfitComm() {
        return dbProfitComm;
    }

    /**
     * @param dbProfitComm the dbProfitComm to set
     */
    public void setDbProfitComm(BigDecimal dbProfitComm) {
        this.dbProfitComm = dbProfitComm;
    }

    /**
     * @return the dbProfitCommEntered
     */
    public BigDecimal getDbProfitCommEntered() {
        return dbProfitCommEntered;
    }

    /**
     * @param dbProfitCommEntered the dbProfitCommEntered to set
     */
    public void setDbProfitCommEntered(BigDecimal dbProfitCommEntered) {
        this.dbProfitCommEntered = dbProfitCommEntered;
    }

    public void validatePolicyType() {
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) details.get(i);
            for (int j = 0; j < ivd.getDetails().size(); j++) {
                InsurancePolicyInwardDetailView ivdet = (InsurancePolicyInwardDetailView) ivd.getDetails().get(j);

                if (j > 0) {
                    InsurancePolicyInwardDetailView ivdet2 = (InsurancePolicyInwardDetailView) ivd.getDetails().get(j - 1);

                    String jenpol = ivdet.getStAttrPolicyTypeID();
                    String jenpol2 = ivdet2.getStAttrPolicyTypeID();

                    if (!jenpol.equalsIgnoreCase(jenpol2)) {
                        throw new RuntimeException("Jenis Polis Asuransi Tidak Sama");
                    }
                }
            }
        }
    }

    public void validate3() {

        DateTime mutationDate = new DateTime(dtInvoiceDate);
        Date mutationDatePlus = mutationDate.plusMonths(1).toDate();
        String date = "01/" + DateUtil.getMonthDigit(mutationDatePlus) + "/" + DateUtil.getYear(mutationDatePlus);
        DateTime currentDateLastDay = new DateTime(DateUtil.getDate(date));

        Date maximumBackDate = currentDateLastDay.plusDays(9).toDate();

        boolean compare = Tools.compare(new Date(), maximumBackDate) > 0;

        if (!DateUtil.getDateStr(dtInvoiceDate, "^^ yyyy").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "^^ yyyy"))) {
            if (compare) {
                throw new RuntimeException("Batas waktu 10 hari setelah akhir bulan sudah terlewati");
            }
        }
    }

    public String generateNoBuktiClaimInward() throws Exception {

        // J 01 0913 00001

        String header = isAR() ? "P" : "Q";

        String counterKey =
                DateUtil.getYear2Digit(getDtDueDate())
                + DateUtil.getMonth2Digit(getDtDueDate());

        String rn = String.valueOf(IDFactory.createNumericID("INWARDCLMNO" + counterKey, 1));

        rn = StringTools.leftPad(rn, '0', 5);

        String treatyTypeID = null;

        if (getStRefID0() != null) {
            InsuranceTreatyTypesView treaty = getTreatyType(getStRefID0());
            treatyTypeID = treaty.getStTransactionNoHeader();
        }

        InsurancePolicyInwardDetailView detil = (InsurancePolicyInwardDetailView) getDetails().get(0);

        if (detil.getStRefID0() != null) {
            InsuranceTreatyTypesView treaty = getTreatyType(detil.getStRefID0());
            treatyTypeID = treaty.getStTransactionNoHeader();
        }

        if (getStARTransactionTypeID().equalsIgnoreCase("17")) {
            treatyTypeID = "13";
        }
        if (getStARTransactionTypeID().equalsIgnoreCase("19") || getStARTransactionTypeID().equalsIgnoreCase("25")) {
            treatyTypeID = "07";
        }
        if (getStARTransactionTypeID().equalsIgnoreCase("24")) {
            treatyTypeID = "19";
        }

        treatyTypeID = StringTools.leftPad(treatyTypeID, '0', 2);

        return stInvoiceNo =
                header
                + treatyTypeID
                + DateUtil.getMonth2Digit(getDtDueDate())
                + DateUtil.getYear2Digit(getDtDueDate())
                + rn;

    }



    /**
     * @return the dbReference1
     */
    public BigDecimal getDbReference1() {
        return dbReference1;
    }

    /**
     * @param dbReference1 the dbReference1 to set
     */
    public void setDbReference1(BigDecimal dbReference1) {
        this.dbReference1 = dbReference1;
    }

    /**
     * @return the dbReference2
     */
    public BigDecimal getDbReference2() {
        return dbReference2;
    }

    /**
     * @param dbReference2 the dbReference2 to set
     */
    public void setDbReference2(BigDecimal dbReference2) {
        this.dbReference2 = dbReference2;
    }

    /**
     * @return the dbReference3
     */
    public BigDecimal getDbReference3() {
        return dbReference3;
    }

    /**
     * @param dbReference3 the dbReference3 to set
     */
    public void setDbReference3(BigDecimal dbReference3) {
        this.dbReference3 = dbReference3;
    }

    /**
     * @return the dbReference4
     */
    public BigDecimal getDbReference4() {
        return dbReference4;
    }

    /**
     * @param dbReference4 the dbReference4 to set
     */
    public void setDbReference4(BigDecimal dbReference4) {
        this.dbReference4 = dbReference4;
    }

    /**
     * @return the dbReference5
     */
    public BigDecimal getDbReference5() {
        return dbReference5;
    }

    /**
     * @param dbReference5 the dbReference5 to set
     */
    public void setDbReference5(BigDecimal dbReference5) {
        this.dbReference5 = dbReference5;
    }

    /**
     * @return the dbReference6
     */
    public BigDecimal getDbReference6() {
        return dbReference6;
    }

    /**
     * @param dbReference6 the dbReference6 to set
     */
    public void setDbReference6(BigDecimal dbReference6) {
        this.dbReference6 = dbReference6;
    }

    public void generateEndorseNo() {

        if(stInvoiceNo.length()==12){
            stInvoiceNo = stInvoiceNo + "01";
        }else if(stInvoiceNo.length()>12){

            final char[] policyno = stInvoiceNo.toCharArray();
            final String enos = stInvoiceNo.substring(12, 14);

            int eNo = Integer.parseInt(enos);

            eNo += 1;

            final String z = StringTools.leftPad(String.valueOf(eNo), '0', 2);

            final char[] ze = z.toCharArray();

            policyno[12] = ze[0];
            policyno[13] = ze[1];

            stInvoiceNo = new String(policyno);
        }

    }

    /**
     * @return the endorseMode
     */
    public boolean isEndorseMode() {
        return endorseMode;
    }

    /**
     * @param endorseMode the endorseMode to set
     */
    public void setEndorseMode(boolean endorseMode) {
        this.endorseMode = endorseMode;
    }

    /**
     * @return the dbBfee
     */
    public BigDecimal getDbBfee() {
        return dbBfee;
    }

    /**
     * @param dbBfee the dbBfee to set
     */
    public void setDbBfee(BigDecimal dbBfee) {
        this.dbBfee = dbBfee;
    }

    /**
     * @return the dbReco
     */
    public BigDecimal getDbReco() {
        return dbReco;
    }

    /**
     * @param dbReco the dbReco to set
     */
    public void setDbReco(BigDecimal dbReco) {
        this.dbReco = dbReco;
    }

    /**
     * @return the dbBfeeEntered
     */
    public BigDecimal getDbBfeeEntered() {
        return dbBfeeEntered;
    }

    /**
     * @param dbBfeeEntered the dbBfeeEntered to set
     */
    public void setDbBfeeEntered(BigDecimal dbBfeeEntered) {
        this.dbBfeeEntered = dbBfeeEntered;
    }

    /**
     * @return the dbRecoEntered
     */
    public BigDecimal getDbRecoEntered() {
        return dbRecoEntered;
    }

    /**
     * @param dbRecoEntered the dbRecoEntered to set
     */
    public void setDbRecoEntered(BigDecimal dbRecoEntered) {
        this.dbRecoEntered = dbRecoEntered;
    }

    public void recalculateInwardTreatyUpload() {
        recalculateInwardTreatyUpload(false);
    }

    public void recalculateInwardTreatyUpload( boolean validate) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");


        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

                BigDecimal totPerLine = null;
                BigDecimal totPerLineE = null;

                final DTOList detailsInvoice = det.getDetails();
                for (int j = 0; j < detailsInvoice.size(); j++) {
                    InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) detailsInvoice.get(j);

                    if(BDUtil.isZeroOrNull(detail.getDbCurrencyRate()))
                        throw new RuntimeException("Data no transaksi "+ detail.getStTransactionNo()+" salah, rate kurs kosong/nol");

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



        if (isNegative()) {
            dbAmount = BDUtil.negate(dbAmount);
            dbEnteredAmount = BDUtil.negate(dbEnteredAmount);
        }


    }

    /**
     * @return the stInstallmentPeriodID
     */
    public String getStInstallmentPeriodID() {
        return stInstallmentPeriodID;
    }

    /**
     * @param stInstallmentPeriodID the stInstallmentPeriodID to set
     */
    public void setStInstallmentPeriodID(String stInstallmentPeriodID) {
        this.stInstallmentPeriodID = stInstallmentPeriodID;
    }

    /**
     * @return the stInstallmentPeriods
     */
    public String getStInstallmentPeriods() {
        return stInstallmentPeriods;
    }

    /**
     * @param stInstallmentPeriods the stInstallmentPeriods to set
     */
    public void setStInstallmentPeriods(String stInstallmentPeriods) {
        this.stInstallmentPeriods = stInstallmentPeriods;
    }

    public DTOList getInstallment() throws Exception {
        if (installment == null) {
            loadInstallment();
        }
        return installment;
    }

    public void setInstallment(DTOList installment) {
        this.installment = installment;
    }

    public void loadInstallment() throws Exception {
        try {
            if (installment == null) {
                installment = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_inward_installment"
                        + "   where"
                        + "      ar_invoice_id = ? order by ins_pol_inward_inst_id",
                        new Object[]{getStARInvoiceID()},
                        InsurancePolicyInwardInstallmentView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public InsurancePeriodView getInstallmentPeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
    }

    public void reCalculateInstallment() throws Exception {
        getInstallment();

        if (installment == null) {
            installment = new DTOList();
        }

        final InsurancePeriodView instPeriod = getInstallmentPeriod();
   
        final BigDecimal periodAmount = BDUtil.div(getDbAmount(), new BigDecimal(installment.size()));

        final BigDecimal roundingErr = BDUtil.sub(getDbAmount(), BDUtil.mul(periodAmount, new BigDecimal(installment.size())));

        Date perDate = dtMutationDate;
        
        final DTOList items = getDetails();
        
        BigDecimal premiBruto = null;
        BigDecimal komisi = null;
        BigDecimal fee = null;
        
        for (int i = 0; i < items.size(); i++) {
            InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) items.get(i);
            
            if(det.isPremiGross2()) premiBruto = det.getDbAmount();
            else if(det.isCommission2()) komisi = det.getDbAmount();
            else fee = det.getDbAmount();
        }

        final BigDecimal periodPremiBrutoAmount = BDUtil.div(premiBruto, new BigDecimal(installment.size()));
        final BigDecimal periodKomisiAmount = BDUtil.div(komisi, new BigDecimal(installment.size()));
        final BigDecimal periodFeeAmount = BDUtil.div(fee, new BigDecimal(installment.size()));

        final BigDecimal roundingErrPremi = BDUtil.sub(premiBruto, BDUtil.mul(periodPremiBrutoAmount, new BigDecimal(installment.size())));
        final BigDecimal roundingErrKomisi = BDUtil.sub(komisi, BDUtil.mul(periodKomisiAmount, new BigDecimal(installment.size())));
        final BigDecimal roundingErrFee = BDUtil.sub(fee, BDUtil.mul(periodFeeAmount, new BigDecimal(installment.size())));

        if (perDate == null) {
            return;
        }

        
        BigDecimal total = null;
        for (int i = 0; i < installment.size(); i++) {
            InsurancePolicyInwardInstallmentView inst = (InsurancePolicyInwardInstallmentView) installment.get(i);

            if (Tools.isYes(stInstallmentManualFlag)) {
                BigDecimal amount = inst.getDbAmount();
                inst.setDbAmount(amount);
                inst.setDtDueDate(inst.getDtDueDate());
            } else {
                inst.setDbAmount(periodAmount);
                inst.setDbPremiBruto(periodPremiBrutoAmount);
                inst.setDbKomisi(periodKomisiAmount);
                inst.setDbFee(periodFeeAmount);

                if (i == 0) {
                    inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
                    inst.setDbPremiBruto(BDUtil.add(inst.getDbPremiBruto(), roundingErrPremi));
                    inst.setDbKomisi(BDUtil.add(inst.getDbKomisi(), roundingErrKomisi));
                    inst.setDbFee(BDUtil.add(inst.getDbFee(), roundingErrFee));
                }



                inst.setDtDueDate(perDate);
            }

            inst.setDbAmount(BDUtil.sub(inst.getDbPremiBruto(), inst.getDbKomisi()));
            inst.setDbAmount(BDUtil.sub(inst.getDbAmount(), inst.getDbFee()));
            
            if (instPeriod != null) {
                perDate = instPeriod.advance(perDate);
            }

            total = BDUtil.add(total, inst.getDbAmount());
        }


    }

    /**
     * @return the stInstallmentOptions
     */
    public String getStInstallmentOptions() {
        return stInstallmentOptions;
    }

    /**
     * @param stInstallmentOptions the stInstallmentOptions to set
     */
    public void setStInstallmentOptions(String stInstallmentOptions) {
        this.stInstallmentOptions = stInstallmentOptions;
    }

    private DTOList insPolicyClaim;

    public DTOList getInsPolicyClaim() {
        loadInsPolicyClaim();
        return insPolicyClaim;
    }

    public void setInsPolicyClaim(DTOList insPolicyClaim) {
        this.insPolicyClaim = insPolicyClaim;
    }

    public void loadInsPolicyClaim() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (insPolicyClaim == null) {
                insPolicyClaim = ListUtil.getDTOListFromQuery(
                        "select a.pol_id,a.pol_no,a.dla_date,a.pol_type_id,a.cc_code,a.cust_name,a.cust_address,"
                        + "a.insured_amount,a.claim_date,a.claim_chronology,b.insured_amount as insured_amount_e,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,a.period_start))) as refd2,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,a.period_end))) as refd3 "
                        + "from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id "
                        + "where a.dla_no = ?",
                        new Object[]{stDLANo},
                        InsurancePolicyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DTOList insInwardExgratia;

    /**
     * @return the insInwardExgratia
     */
    public DTOList getInsInwardExgratia() {
        loadInsInwardExgratia();
        return insInwardExgratia;
    }

    /**
     * @param insInwardExgratia the insInwardExgratia to set
     */
    public void setInsInwardExgratia(DTOList insInwardExgratia) {
        this.insInwardExgratia = insInwardExgratia;
    }

    public void loadInsInwardExgratia() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (insInwardExgratia == null) {
                insInwardExgratia = ListUtil.getDTOListFromQuery(
                        "select a.* "
                        + "from ins_pol_inward a "
                        + "where a.dla_no = ? and a.ar_trx_type_id = 24 ",
                        new Object[]{stDLANo},
                        InsurancePolicyInwardView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateOldUpload( boolean validate) {

        BigDecimal tot = null;
        BigDecimal totE = null;

        ARTransactionTypeView arTrxType = getARTrxType();

        if(arTrxType==null) throw new RuntimeException("Error retrieving AR Trx Type ("+stARTransactionTypeID+")");

        if (!arTrxType.trxDisableDetail()) {
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

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


    }

    public void recalculateInvoiceAmount(){

        BigDecimal tot = null;
        BigDecimal totE = null;

        for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

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

        logger.logWarning("####### tot = "+ tot +" tote = "+ totE);
    }

    public InsuranceTreatyTypesView getTreatyTypeCashLoss() {
        return (InsuranceTreatyTypesView) DTOPool.getInstance().getDTO(InsuranceTreatyTypesView.class, stRefID0);

    }

    public String generateVoucherNo(String type) throws Exception {

        String voucherNo = "";

        String year2Digit = DateUtil.getYear2Digit(getDtMutationDate());
        String month2Digit = DateUtil.getMonth2Digit(getDtMutationDate());
        String counterKey = month2Digit + year2Digit;


        final String ccCode = getDigit(getStCostCenterCode(), 2);

        String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("NOTA_"+ ccCode + month2Digit + year2Digit , 1)), '0', 8);

        // 00059883/DN/80/08/24
        voucherNo =
                orderNo + // A
                "/" + // B
                type + // C
                "/"+
                ccCode + // D
                "/"+
                month2Digit + // E
                "/"+
                year2Digit
                ;

        return voucherNo;
    }

    private String getDigit(String code, int i) {
        if ((code == null) || (code.length() < 1)) {
            code = "";
        }

        code = code + "000000000000000000";

        code = code.substring(0, i);

        return code;
    }

    public boolean isManualCicilan(){
        return Tools.isYes(stInstallmentManualFlag);
    }
    
}

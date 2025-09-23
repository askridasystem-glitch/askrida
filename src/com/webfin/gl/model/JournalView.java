/***********************************************************************
 * Module:  com.webfin.gl.model.JournalView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 6:29:56 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.BDUtil;
import com.crux.util.ObjectCloner;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.master.division.model.DivisionView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class JournalView extends DTO implements RecordAudit {
   /*
   CREATE TABLE gl_je_detail
(
  trx_id int8 NOT NULL,
  trx_no
  debit numeric,
  credit numeric,
  accountid int8,
  description varchar(128),
  applydate timestamp,
  journal_code varchar(5),
  fiscal_year varchar(8),
  period_no int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32)

ALTER TABLE gl_je_detail ADD COLUMN entered_debit numeric;
ALTER TABLE gl_je_detail ADD COLUMN entered_credit numeric;
ALTER TABLE gl_je_detail ADD COLUMN ccy_code varchar(3);
ALTER TABLE gl_je_detail ADD COLUMN ccy_rate numeric;

)
   */
     private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
 
   //MARK BUAT BERSIH2X 
   //public static String tableName = "gl_je_detail_temp";
   public static String tableName = "gl_je_detail";
   
   public static String fieldMap[][] = {
      {"stTransactionHeaderID","trx_hdr_id"},
      {"stTransactionID","trx_id*pk*nd"},
      {"stTransactionNo","trx_no"},
      {"dbBalance","balance*n"},
      {"dbDebit","debit"},
      {"dbCredit","credit"},
      {"lgAccountID","accountid"},
      {"stAccountNo","accountno*n"},
      {"stDescription","description"},
      {"dtApplyDate","applydate"},
      {"stJournalCode","journal_code"},
      {"lgFiscalYear","fiscal_year"},
      {"lgPeriodNo","period_no"},
      {"dbEnteredDebit","entered_debit"},
      {"dbEnteredCredit","entered_credit"},
      {"stCurrencyCode","ccy_code"},
      {"dbCurrencyRate","ccy_rate"},
      {"stRefTRX","ref_trx"},
      {"stRefReverse","ref_reverse"},
      {"stReverseFlag","reverse_flag"},
      {"stRefTrxType","ref_trx_type"},
      {"stRefTrxNo","ref_trx_no"},
      {"stRefEntID","ref_ent_id"},
      {"stRefTrxDetNo","ref_trx_det_no"},
      {"stMethodCode","method_code*n"},
      {"stGlAccountID","gl_acct_id*n"},
      {"stApproved","approved"},
      {"stPolicyNo","pol_no"},
      {"lgHeaderAccountID","hdr_accountid"},
      {"stHeaderAccountNo","hdr_accountno"},
      {"stPolicyID","pol_id"},
      {"stReference1","ref1"},
      {"dbPremiTreaty","premi_treaty*n"},
      {"dbPremiFac","premi_fact*n"},
      {"dbPremiXL","premi_xl*n"},
      {"dbClaimFac","klaim_fact*n"},
      {"dbClaimXL","klaim_xl*n"},
      {"dbClaimCash","klaim_closs*n"},
      {"stPostedFlag","posted_flag"},
      {"stIDRFlag","flag"},
      {"stYears","years"},
      {"stMonths","months"},
      {"stSummaryFlag","summary_flag"},
      {"stUserName","user_name*n"},
      {"dbSaldo","saldo*n"},
      {"stRecapitulationNo","recap_no"},
      {"stJenas", "jenas*n"},
      {"stKoda", "koda*n"},
      {"stARTransactionLineID","ar_trx_line_id"},
      {"stOwnerCode","owner_code"},
      {"stUserCode","user_code"},
      {"stBusinessTypeID","business_type_id"},

   };

   /*
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_type varchar(8);
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_no varchar(32);

   */

   private BigDecimal dbBalance;
   private BigDecimal dbEnteredDebit;
   private BigDecimal dbEnteredCredit;
   private String stRefTrxType;
   private String stRefTrxNo;
   private String stRefTrxDetNo;
   private String stCurrencyCode;
   private BigDecimal dbCurrencyRate;

   private String stReverseFlag;
   private String stTransactionHeaderID;
   private String stTransactionID;
   private String stTransactionNo;
   private String stRefTRX;
   private String stRefReverse;
   private BigDecimal dbDebit;
   private BigDecimal dbOldDebit;
   private BigDecimal dbCredit;
   private BigDecimal dbOldCredit;
   private Long lgAccountID;
   
   private String stAccountNo;
   private String stDescription;
   private Date dtApplyDate;
   private String stJournalCode;
   private Long lgFiscalYear;
   private Long lgPeriodNo;
   private String stCostCenter;
   private String stKet;
   private String stMethodCode;
   
   private Long lgAccountIDMaster;
   private String stDescriptionMaster;
   private BigDecimal dbEnteredDebitMaster;
   private BigDecimal dbEnteredCreditMaster;
   private BigDecimal dbDebitMaster;
   private BigDecimal dbCreditMaster;
   private String stAccountNoMaster;
   private String stKetMaster;
   private String stGlAccountID;
   private String stReceiptClassID;
   private String stPaymentMethodID;
   private Date dtCreateDate;
   private boolean readOnly; 
   
   private String stRefEntID;
   
   private String stApproved;
   private String stPolicyNo;
   private Long lgHeaderAccountID;
   private String stHeaderAccountNo;
   private String stPolicyID;
   private String stReference1;

   private BigDecimal dbPremiTreaty;
   private BigDecimal dbPremiFac;
   private BigDecimal dbPremiXL;
   private BigDecimal dbClaimCash;
   private BigDecimal dbClaimFac;
   private BigDecimal dbClaimXL;
   private String stPostedFlag;
   
   private String stIDRFlag;
   private String stMonths;
   private String stYears;
   private String stSummaryFlag;
   private String stUserName;
   private String stRecapitulationNo;

   private BigDecimal dbSaldo;

   private boolean Posted;
   private String stJenas;
   private String stKoda;
   private String stARTransactionLineID;
   private String stOwnerCode;
   private String stUserCode;
   private String stBusinessTypeID;

    public String getStBusinessTypeID() {
        return stBusinessTypeID;
    }

    public void setStBusinessTypeID(String stBusinessTypeID) {
        this.stBusinessTypeID = stBusinessTypeID;
    }

    public String getStOwnerCode() {
        return stOwnerCode;
    }

    public void setStOwnerCode(String stOwnerCode) {
        this.stOwnerCode = stOwnerCode;
    }

    public String getStUserCode() {
        return stUserCode;
    }

    public void setStUserCode(String stUserCode) {
        this.stUserCode = stUserCode;
    }
   
   public String getStPolicyNo() {
      return stPolicyNo;
   }

   public void setStPolicyNo(String stPolicyNo) {
      this.stPolicyNo = stPolicyNo;
   }
   
   public String getStApproved() {
      return stApproved;
   }

   public void setStApproved(String stApproved) {
      this.stApproved = stApproved;
   }
   
   public boolean getReadOnly(){
   		return readOnly;
   }
   
   public void setReadOnly(boolean read){
   		this.readOnly = read;
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
   
   public String getStGlAccountID() {
      return stGlAccountID;
   }

   public void setStGlAccountID(String stGlAccountID) {
      this.stGlAccountID = stGlAccountID;
   }

   public String getStKet() {
      return stKet;
   }

   public void setStKet(String stKet) {
      this.stKet = stKet;
   }
   
   public String getStMethodCode() {
      return stMethodCode;
   }

   public void setStMethodCode(String stMethodCode) {
      this.stMethodCode = stMethodCode;
   }
   
   public String getStRefEntID() {
      return stRefEntID;
   }

   public void setStRefEntID(String stRefEntID) {
      this.stRefEntID = stRefEntID;
   }

   public String getStCostCenter() {
      return stCostCenter;
   }

   public void setStCostCenter(String stCostCenter) {
      this.stCostCenter = stCostCenter;
   }

   public BigDecimal getDbBalance() {
      return dbBalance;
   }

   public void setDbBalance(BigDecimal dbBalance) {
      this.dbBalance = dbBalance;
   }

   public String getStRefTrxDetNo() {
      return stRefTrxDetNo;
   }

   public void setStRefTrxDetNo(String stRefTrxDetNo) {
      this.stRefTrxDetNo = stRefTrxDetNo;
   }

   public String getStRefTrxType() {
      return stRefTrxType;
   }

   public void setStRefTrxType(String stRefTrxType) {
      this.stRefTrxType = stRefTrxType;
   }

   public String getStRefTrxNo() {
      return stRefTrxNo;
   }

   public void setStRefTrxNo(String stRefTrxNo) {
      this.stRefTrxNo = stRefTrxNo;
   }

   public BigDecimal getDbEnteredDebit() {
      return dbEnteredDebit;
   }

   public void setDbEnteredDebit(BigDecimal dbEnteredDebit) {
      this.dbEnteredDebit = dbEnteredDebit;
   }

   public BigDecimal getDbEnteredCredit() {
      return dbEnteredCredit;
   }

   public void setDbEnteredCredit(BigDecimal dbEnteredCredit) {
      this.dbEnteredCredit = dbEnteredCredit;
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

   public void endUpdate() {
      dbOldCredit = dbCredit;
      dbOldDebit = dbDebit;
   }

   public String getStTransactionNo() {
      return stTransactionNo;
   }

   public void setStTransactionNo(String stTransactionNo) {
      this.stTransactionNo = stTransactionNo;
   }

   public BigDecimal getDbDebit() {
      if (dbDebit==null) dbDebit = new BigDecimal(0);
      return dbDebit;
   }

   public void setDbDebit(BigDecimal dbDebit) {
      this.dbDebit = dbDebit;
   }

   public BigDecimal getDbCredit() {
      if (dbCredit==null) dbCredit = new BigDecimal(0);
      return dbCredit;
   }

   public void setDbCredit(BigDecimal dbCredit) {
      this.dbCredit = dbCredit;
   }

   public Long getLgAccountID() {
      return lgAccountID;
   }

   public void setLgAccountID(Long lgAccountID) {
      this.lgAccountID = lgAccountID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Date getDtApplyDate() {
      return dtApplyDate;
   }

   public void setDtApplyDate(Date dtApplyDate) {
      this.dtApplyDate = dtApplyDate;
   }

   public String getStJournalCode() {
      return stJournalCode;
   }

   public void setStJournalCode(String stJournalCode) {
      this.stJournalCode = stJournalCode;
   }

   public Long getLgFiscalYear() {
      return lgFiscalYear;
   }

   public void setLgFiscalYear(Long lgFiscalYear) {
      this.lgFiscalYear = lgFiscalYear;
   }

   public Long getLgPeriodNo() {
      return lgPeriodNo;
   }

   public void setLgPeriodNo(Long lgPeriodNo) {
      this.lgPeriodNo = lgPeriodNo;
   }

   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }

    public void setDbOldCredit(BigDecimal dbOldCredit) {
        this.dbOldCredit = dbOldCredit;
    }

    public BigDecimal getDbOldCredit() {
        return dbOldCredit;
    }

    public void setDbOldDebit(BigDecimal dbOldDebit) {
        this.dbOldDebit = dbOldDebit;
    }

    public BigDecimal getDbOldDebit() {
        return dbOldDebit;
    }

   public BigDecimal getDbAdjustmentAmount() {
      final BigDecimal q = BDUtil.sub(dbCredit, dbOldCredit);
      final BigDecimal w = BDUtil.sub(dbDebit, dbOldDebit);

      return BDUtil.sub(q,w);
   }

   public JournalView getOldJournal() {
      return (JournalView) getOld();
   }

   public BigDecimal getDbBalanceAmount() {
      BigDecimal b = BDUtil.sub(dbCredit,dbDebit);
      b=b==null?new BigDecimal(0):b;
      return b;
   }

   public void setStAccountID(String ac) {
      lgAccountID = ac==null?null:new Long(ac);
   }

   public void reCalculate() {
      //dbCredit = BDUtil.mul(dbCurrencyRate, dbEnteredCredit);
      //dbDebit = BDUtil.mul(dbCurrencyRate, dbEnteredDebit);
      dbCredit = dbEnteredCredit;
      dbDebit = dbEnteredDebit;
   }

   public JournalView copy() {
      return (JournalView) ObjectCloner.deepCopy(this);
   }

   public void setDbAutoCredit(BigDecimal amt) {
      if(Tools.compare(amt, BDUtil.zero)>=0) setDbEnteredCredit(amt); else setDbEnteredDebit(amt.negate());
   }

   public void setDbAutoDebit(BigDecimal amt) {
      if(Tools.compare(amt, BDUtil.zero)>=0) setDbEnteredDebit(amt); else setDbEnteredCredit(amt.negate());
   }

   public void setStRefTRX(String stRefTRX) {
      this.stRefTRX = stRefTRX;
   }

   public String getStRefTRX() {
      return stRefTRX;
   }

   public String getStRefReverse() {
      return stRefReverse;
   }

   public void setStRefReverse(String stRefReverse) {
      this.stRefReverse = stRefReverse;
   }

   public void inverse() {

      BigDecimal temp = dbCredit;
      dbCredit = dbDebit;
      dbDebit = temp;

      temp = dbEnteredCredit;
      dbEnteredCredit = dbEnteredDebit;
      dbEnteredDebit = temp;
   }

   public String getStTransactionHeaderID() {
      return stTransactionHeaderID;
   }

   public void setStTransactionHeaderID(String stTransactionHeaderID) {
      this.stTransactionHeaderID = stTransactionHeaderID;
   }

   public String getStTransactionID() {
      return stTransactionID;
   }

   public void setStTransactionID(String stTransactionID) {
      this.stTransactionID = stTransactionID;
   }

   public String toString() {
      return "["+getStMarks()+">"+lgAccountID+":"+BDUtil.sub(dbCredit,dbDebit)+"]\n";
   }

   public void loadAccountNo() {
      AccountView acc = getAccount();

      if (acc!=null)
         setStAccountNo(acc.getStAccountNo());
   }

   public AccountView getAccount() {
      if (lgAccountID==null) return null;
      return (AccountView) DTOPool.getInstance().getDTORO(AccountView.class, String.valueOf(getLgAccountID()));
   }

   public String getStReverseFlag() {
      return stReverseFlag;
   }

   public void setStReverseFlag(String stReverseFlag) {
      this.stReverseFlag = stReverseFlag;
   }

   public void setStAccountIDNotNull(String stAccountID, String errMsg) {
      if (stAccountID==null) throw new RuntimeException(errMsg+" null account ID");
      setStAccountID(stAccountID);
      loadAccountNo();
      if (stAccountNo==null) throw new RuntimeException(errMsg+" null account NO");
   }
   
   public Long getLgAccountIDMaster() {
      return lgAccountIDMaster;
   }

   public void setLgAccountIDMaster(Long lgAccountIDMaster) {
      this.lgAccountIDMaster = lgAccountIDMaster;
   }
   
   public String getStDescriptionMaster() {
      return stDescriptionMaster;
   }

   public void setStDescriptionMaster(String stDescriptionMaster) {
      this.stDescriptionMaster = stDescriptionMaster;
   }
   
   public BigDecimal getDbDebitMaster() {
      if (dbDebitMaster==null) dbDebitMaster = new BigDecimal(0);
      return dbDebitMaster;
   }

   public void setDbDebitMaster(BigDecimal dbDebitMaster) {
      this.dbDebitMaster = dbDebitMaster;
   }

   public BigDecimal getDbCreditMaster() {
      if (dbCreditMaster==null) dbCreditMaster = new BigDecimal(0);
      return dbCreditMaster;
   }

   public void setDbCreditMaster(BigDecimal dbCreditMaster) {
      this.dbCreditMaster = dbCreditMaster;
   }
   
   public BigDecimal getDbEnteredDebitMaster() {
      return dbEnteredDebitMaster;
   }

   public void setDbEnteredDebitMaster(BigDecimal dbEnteredDebitMaster) {
      this.dbEnteredDebitMaster = dbEnteredDebitMaster;
   }

   public BigDecimal getDbEnteredCreditMaster() {
      return dbEnteredCreditMaster;
   }

   public void setDbEnteredCreditMaster(BigDecimal dbEnteredCreditMaster) {
      this.dbEnteredCreditMaster = dbEnteredCreditMaster;
   }
   
    public String getStAccountNoMaster() {
      return stAccountNoMaster;
   }

   public void setStAccountNoMaster(String stAccountNoMaster) {
      this.stAccountNoMaster = stAccountNoMaster;
   }
   
    public String getStKetMaster() {
      return stKetMaster;
   }

   public void setStKetMaster(String stKetMaster) {
      this.stKetMaster = stKetMaster;
   }
   
   public Date getDtCreateDate() {
      return dtCreateDate;
   }

   public void setDtCreateDate(Date dtCreateDate) {
      this.dtCreateDate = dtCreateDate;
   }

    public Long getLgHeaderAccountID() {
        return lgHeaderAccountID;
    }

    public void setLgHeaderAccountID(Long lgHeaderAccountID) {
        this.lgHeaderAccountID = lgHeaderAccountID;
    }

    public String getStHeaderAccountNo() {
        return stHeaderAccountNo;
    }

    public void setStHeaderAccountNo(String stHeaderAccountNo) {
        this.stHeaderAccountNo = stHeaderAccountNo;
    }
    
    
    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCurrencyCode);
        
        return costcenter;
    }

    public String getStPolicyID()
    {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID)
    {
        this.stPolicyID = stPolicyID;
    }
    
    public InsurancePolicyView getPolicy() {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stPolicyID);
    }

    public String getStReference1()
    {
        return stReference1;
    }

    public void setStReference1(String stReference1)
    {
        this.stReference1 = stReference1;
    }
    
    public BigDecimal getDbPremiTreaty() {
        return dbPremiTreaty;
    }

    public void setDbPremiTreaty(BigDecimal dbPremiTreaty) {
        this.dbPremiTreaty = dbPremiTreaty;
    }

    public BigDecimal getDbPremiFac() {
        return dbPremiFac;
    }

    public void setDbPremiFac(BigDecimal dbPremiFac) {
        this.dbPremiFac = dbPremiFac;
    }

    public BigDecimal getDbPremiXL() {
        return dbPremiXL;
    }

    public void setDbPremiXL(BigDecimal dbPremiXL) {
        this.dbPremiXL = dbPremiXL;
    }

    public BigDecimal getDbClaimCash() {
        return dbClaimCash;
    }

    public void setDbClaimCash(BigDecimal dbClaimCash) {
        this.dbClaimCash = dbClaimCash;
    }

    public BigDecimal getDbClaimFac() {
        return dbClaimFac;
    }

    public void setDbClaimFac(BigDecimal dbClaimFac) {
        this.dbClaimFac = dbClaimFac;
    }

    public BigDecimal getDbClaimXL() {
        return dbClaimXL;
    }

    public void setDbClaimXL(BigDecimal dbClaimXL) {
        this.dbClaimXL = dbClaimXL;
    }

    public String getStPostedFlag() {
        return stPostedFlag;
    }

    public void setStPostedFlag(String stPostedFlag) {
        this.stPostedFlag = stPostedFlag;
    }
    
    public String getStIDRFlag() {
        return stIDRFlag;
    }

    public void setStIDRFlag(String stIDRFlag) {
        this.stIDRFlag = stIDRFlag;
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

    public void setDbAutoCredit2(BigDecimal amt) {
      if(Tools.compare(amt, BDUtil.zero)>=0){
          setDbEnteredCredit(amt);
          setDbCredit(amt);
      } else{
          setDbEnteredDebit(amt.negate());
          setDbDebit(amt.negate());
      }
   }

   public void setDbAutoDebit2(BigDecimal amt) {
      if(Tools.compare(amt, BDUtil.zero)>=0){
          setDbEnteredDebit(amt);
          setDbDebit(amt);
      } else{
          setDbEnteredCredit(amt.negate());
          setDbCredit(amt.negate());
      }
   }

    /**
     * @return the stSummaryFlag
     */
    public String getStSummaryFlag() {
        return stSummaryFlag;
    }

    /**
     * @param stSummaryFlag the stSummaryFlag to set
     */
    public void setStSummaryFlag(String stSummaryFlag) {
        this.stSummaryFlag = stSummaryFlag;
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

    public BigDecimal getDbSaldo() {
        return dbSaldo;
    }

    public void setDbSaldo(BigDecimal dbSaldo) {
        this.dbSaldo = dbSaldo;
    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if(getStCostCenter()!=null)
                cek = cek + " and cc_code = ?";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStMonths());
            PS.setString(2, getStYears());

            if(getStCostCenter()!=null)
                PS.setString(3, getStCostCenter());

            ResultSet RS = PS.executeQuery();

            if (RS.next()){
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public void setPosted(boolean Posted) {
        this.Posted = Posted;
    }

    /**
     * @return the stRecapitulationNo
     */
    public String getStRecapitulationNo() {
        return stRecapitulationNo;
    }

    /**
     * @param stRecapitulationNo the stRecapitulationNo to set
     */
    public void setStRecapitulationNo(String stRecapitulationNo) {
        this.stRecapitulationNo = stRecapitulationNo;
    }

    public String getStJenas() {
        return stJenas;
    }

    /**
     * @param stJenas the stJenas to set
     */
    public void setStJenas(String stJenas) {
        this.stJenas = stJenas;
    }

    /**
     * @return the stKoda
     */
    public String getStKoda() {
        return stKoda;
    }

    /**
     * @param stKoda the stKoda to set
     */
    public void setStKoda(String stKoda) {
        this.stKoda = stKoda;
    }

    /**
     * @return the stARTransactionLineID
     */
    public String getStARTransactionLineID() {
        return stARTransactionLineID;
    }

    /**
     * @param stARTransactionLineID the stARTransactionLineID to set
     */
    public void setStARTransactionLineID(String stARTransactionLineID) {
        this.stARTransactionLineID = stARTransactionLineID;
    }

    public DivisionView getDivision() {
      if (stOwnerCode==null) return null;
      return (DivisionView) DTOPool.getInstance().getDTORO(DivisionView.class, stOwnerCode);
   }

    public DivisionView getOwnerDivisionView() {
        final DivisionView division = (DivisionView) DTOPool.getInstance().getDTO(DivisionView.class, stOwnerCode);

        return division;
    }

    public DivisionView getUserDivisionView() {
        final DivisionView division = (DivisionView) DTOPool.getInstance().getDTO(DivisionView.class, stUserCode);

        return division;
    }

    public BigDecimal getDbJournalAmount(){
        BigDecimal amount = BDUtil.zero;

        if(!BDUtil.isZeroOrNull(getDbDebit())) amount = getDbDebit();
        else if(!BDUtil.isZeroOrNull(getDbCredit())) amount = getDbCredit();

        return amount;
    }

}


/*
 <tr class=row0>
    <td><input type=hidden name=acid0 value=<%=jspUtil.print(jv1.getLgAccountID())%>><%=jspUtil.getInputText("acno0",new FieldValidator("accountno","Account Number","string",32),jv1.getStAccountNo(), 150, JSPUtil.MANDATORY|JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td>
    <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+0+";openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectAccount);")%></td>
    <td><%=jspUtil.getInputText("desc0",new FieldValidator("accountno","Description","string",128),jv1.getStDescription(), 150, cf)%></td>
    <td>
       <%=jspUtil.getInputText("debit0",new FieldValidator("debit","Debit Value","money16.2",-1),jv1.getDbEnteredDebit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
       <%=!isForex?"":jspUtil.getInputText("rdebit0",new FieldValidator("debit","Debit Value","money16.2",-1),jv1.getDbDebit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
    </td>
    <td>
       <%=jspUtil.getInputText("credit0",new FieldValidator("credit","Credit Value","money16.2",-1),jv1.getDbEnteredCredit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
       <%=!isForex?"":jspUtil.getInputText("rcredit0",new FieldValidator("credit","Credit Value","money16.2",-1),jv1.getDbCredit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
    </td>
 */
/***********************************************************************
 * Module:  com.webfin.ar.model.InsurancePolicyInwardDetailView
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 10:38:43 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ListUtil;
import com.webfin.ar.model.ARTaxView;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;

import java.math.BigDecimal;
import java.util.Date;

public class InsurancePolicyInwardDetailView extends DTO implements RecordAudit {

   /*
CREATE TABLE ar_invoice_details
(
  ar_invoice_dtl_id int8 NOT NULL,
  ar_invoice_id int8,
  description  varchar(255),
  amount numeric,
  gl_account_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_invoice_details_pk PRIMARY KEY (ar_invoice_dtl_id)

ALTER TABLE ar_invoice_details ADD COLUMN tax_rate numeric;
ALTER TABLE ar_invoice_details ADD COLUMN tax_amount numeric;
ALTER TABLE ar_invoice_details ADD COLUMN tax_code_settle int8;

)

   */

   public InsurancePolicyInwardDetailView() {
   }

   //MARK BUAT BERSIH2X 
   //public static String tableName = "ar_invoice_details_temp";
   public static String tableName = "ins_pol_inward_details";

   public static String fieldMap[][] = {
      {"stARInvoiceDetailID","ar_invoice_dtl_id*pk"},
      {"stARInvoiceID","ar_invoice_id"},
      {"stDescription","description"},
      {"stGLAccountID","gl_account_id"},
      {"dbAmount","amount"},
      {"dbEnteredAmount","entered_amount"},
      {"stNegativeFlag","negative_flag"},
      {"stEntityID","ent_id"},
      {"stComissionFlag","f_comission"},
      {"stARTrxLineID","ar_trx_line_id"},
      {"dbAmountSettled","amount_settled"},
      {"stTaxCode","tax_code"},
      {"stTaxCodeOnSettlement","tax_code_settle"},
      {"dbTaxRate","tax_rate"},
      {"dbTaxAmount","tax_amount"},
      {"stRefInvoiceDetailID","ref_invoice_dtl_id"},
      {"stRefID0","refid0"},
      {"stRefID1","refid1"},
      {"stRefID2","refid2"},
      {"stTaxFlag","tax_flag"},
      {"stParentID","parent_id"},
      {"stRootID","root_id"},
      {"stAttrUnderwriting","attr_underwrit"},
      {"stAttrPolicyTypeID","attr_pol_type_id"},
      {"stReinsuranceEntityID","reins_ent_id"},

      {"stCurrencyCode","ccy*n"},
      {"dbCurrencyRate","ccy_rate*n"},
      {"stDeskripsi","deskripsi*n"},
      {"stTransactionNo","trx_no*n"},
      {"stAttrQuartal","attr_quartal*n"},
      {"stAttrQuartalYear","attr_quartal_year*n"},

      {"stAttrPolicyNo","attr_pol_no*n"},
      {"dtAttrPolicyPeriodStart","attr_pol_per_0*n"},
      {"dtAttrPolicyPeriodEnd","attr_pol_per_1*n"},
      {"stAttrPolicyName","attr_pol_name*n"},
      {"stAttrPolicyAddress","attr_pol_address*n"},
      {"dbAttrPolicyTSI","attr_pol_tsi*n"},
      {"dbAttrPolicyTSITotal","attr_pol_tsi_total*n"},
      
   };

   private String stCurrencyCode;
   private BigDecimal dbCurrencyRate;
   private String stDeskripsi;
   private String stTransactionNo;
   private String stAttrQuartal;
   private String stAttrQuartalYear;


   private String stTaxFlag;
   private String stRefID0;
   private String stRefID1;
   private String stRefID2;
   private String stARInvoiceDetailID;
   private String stARInvoiceID;
   private String stDescription;
   private String stGLAccountID;
   private String stComissionFlag;
   private String stEntityID;
   private String stNegativeFlag;
   private String stARTrxLineID;
   private String stAccountDesc;
   private String stTaxCode;
   private String stTaxCodeOnSettlement;
   private String stRefInvoiceDetailID;
   private BigDecimal dbTaxRate;
   private BigDecimal dbTaxAmount;
   private BigDecimal dbAmount;
   private BigDecimal dbAmountSettled;
   private BigDecimal dbEnteredAmount;
   private ARTransactionLineView trxLine;
   private InsurancePolicyInwardDetailView ref;
   private String stParentID;
   private String stRootID;
   private String stAttrUnderwriting;
   private String stAttrPolicyTypeID;
   private DTOList details;

   private String stAttrPolicyNo;
   private String stAttrPolicyName;
   private Date dtAttrPolicyPeriodStart;
   private Date dtAttrPolicyPeriodEnd;
   private String stAttrPolicyAddress;
   private BigDecimal dbAttrPolicyTSI;
   private BigDecimal dbAttrPolicyTSITotal;
   private String stReinsuranceEntityID;

    public String getStReinsuranceEntityID() {
        return stReinsuranceEntityID;
    }

    public void setStReinsuranceEntityID(String stReinsuranceEntityID) {
        this.stReinsuranceEntityID = stReinsuranceEntityID;
    }
   
   public String getStTaxFlag() {
      return stTaxFlag;
   }

   public void setStTaxFlag(String stTaxFlag) {
      this.stTaxFlag = stTaxFlag;
   }

   public String getStRefInvoiceDetailID() {
      if (stRefInvoiceDetailID==null)
         if (ref!=null)
           stRefInvoiceDetailID = ref.getStARInvoiceDetailID();
      
      return stRefInvoiceDetailID;
   }

   public void setStRefInvoiceDetailID(String stRefInvoiceDetailID) {
      this.stRefInvoiceDetailID = stRefInvoiceDetailID;
   }

   public InsurancePolicyInwardDetailView getRef() {
      //if (stRefInvoiceDetailID!=null) throw new RuntimeException("ref object not linked");
      //return ref;
      return (InsurancePolicyInwardDetailView) DTOPool.getInstance().getDTO(InsurancePolicyInwardDetailView.class, stRefInvoiceDetailID);

   }

   public void setRef(InsurancePolicyInwardDetailView ref) {
      this.ref = ref;
   }

   public String getStGLAccountDesc() {
      return getStAccountDesc();
      //return stGLAccountDesc;
   }

   public String getStARInvoiceDetailID() {
      return stARInvoiceDetailID;
   }

   public void setStARInvoiceDetailID(String stARInvoiceDetailID) {
      this.stARInvoiceDetailID = stARInvoiceDetailID;
   }

   public String getStARInvoiceID() {
      return stARInvoiceID;
   }

   public void setStARInvoiceID(String stARInvoiceID) {
      this.stARInvoiceID = stARInvoiceID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStGLAccountID() {
      return stGLAccountID;
   }

   public void setStGLAccountID(String stGLAccountID) {
      this.stGLAccountID = stGLAccountID;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStAccountDesc() {
      final AccountView ac = getAccount();

      if (ac==null) {
         return stAccountDesc;
      }

      return ac.getStAccountNo() +"/"+ac.getStDescription();
   }

   public void setStAccountDesc(String stAccountDesc) {
      this.stAccountDesc = stAccountDesc;
   }

   private AccountView getAccount() {
      return (AccountView) DTOPool.getInstance().getDTO(AccountView.class, stGLAccountID);
   }

   public String getStNegativeFlag() {
      return stNegativeFlag;
   }

   public void setStNegativeFlag(String stNegativeFlag) {
      this.stNegativeFlag = stNegativeFlag;
   }

   public BigDecimal getDbEnteredAmount() {
      return dbEnteredAmount;
   }

   public void setDbEnteredAmount(BigDecimal dbEnteredAmount) {
      this.dbEnteredAmount = dbEnteredAmount;
   }

   public String getStGLAccountCode() {
      final ARTransactionLineView tl = getTrxLine();
      return tl==null?null:tl.getStGLAccount();
   }

   public boolean isNegative() {
      return "Y".equalsIgnoreCase(stNegativeFlag);
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public String getStComissionFlag() {
      return stComissionFlag;
   }

   public void setStComissionFlag(String stComissionFlag) {
      this.stComissionFlag = stComissionFlag;
   }

   public String getStARTrxLineID() {
      return stARTrxLineID;
   }

   public void setStARTrxLineID(String stARTrxLineID) {
      this.stARTrxLineID = stARTrxLineID;
   }

   public ARTransactionLineView getTrxLine() {
      return (ARTransactionLineView) DTOPool.getInstance().getDTO(ARTransactionLineView.class, stARTrxLineID);
   }

   public boolean isComission() {
      return Tools.isYes(stComissionFlag);
   }
   
   public boolean isDiscount(){
   	  return "DISC".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public boolean isGrossPremium(){
   	  return "PREMIG".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public boolean isCommission2(){
   	   return "COMMISSION".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isPremiGross2(){
   	   return "PREMIG".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isBrokerage2(){
   	   return "BROKERAGE".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isHandlingFee2(){
   	   return "HFEE".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isStampDuty2(){
   	   return "STAMPDUTY".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isPolicyCost2(){
   	   return "PCOST".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isDiscount2(){
   	   return "DISC".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isTaxComm(){
   	   return "TAXCOMM".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isTaxBrok(){
   	   return "TAXBROK".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
   
   public boolean isTaxHFee(){
   	   return "TAXHFEE".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }

   public void loadSettings() {
      setStDescription(getTrxLine().getStItemDesc());
      setStComissionFlag(getTrxLine().getStComissionFlag());
      setStNegativeFlag(getTrxLine().getStNegativeFlag());
   }

   public BigDecimal getDbAmountSettled() {
      return dbAmountSettled;
   }

   public void setDbAmountSettled(BigDecimal dbAmountSettled) {
      this.dbAmountSettled = dbAmountSettled;
   }

   public BigDecimal getDbOustandingAmount() {
      return BDUtil.sub(dbEnteredAmount, dbAmountSettled);
   }

   public BigDecimal getDbEnteredAmountAfterTax() {

      final BigDecimal taxamt = getDbTaxAmount();

      if (taxamt!=null)
         return BDUtil.sub(getDbEnteredAmount(), taxamt);

      return getDbEnteredAmount();
   }

   /*public BigDecimal getDbTaxAmount() {
      if (!getTrxLine().isTaxed()) return null;
      return BDUtil.mul(getTrxLine().getTax().getDbRate(), dbEnteredAmount);
   }*/

   public BigDecimal getDbTaxAmount() {
      return dbTaxAmount;
   }

   public void setDbTaxAmount(BigDecimal dbTaxAmount) {
      this.dbTaxAmount = dbTaxAmount;
   }
   
   public boolean isTax() {
      return Tools.isYes(stTaxFlag);
   }

   public boolean isTaxed() {
      return getStTaxCode()!=null;
      //return getTrxLine().isTaxed();
   }

   public String getStTaxCode() {
      return stTaxCode;
   }

   public void setStTaxCode(String stTaxCode) {
      this.stTaxCode = stTaxCode;
   }

   public ARTaxView getTax() {
      return (ARTaxView) DTOPool.getInstance().getDTO(ARTaxView.class, stTaxCode);
   }

   public BigDecimal getDbTaxRate() {
      return dbTaxRate;
   }

   public void setDbTaxRate(BigDecimal dbTaxRate) {
      this.dbTaxRate = dbTaxRate;
   }

   public String getStTaxCodeOnSettlement() {
      return stTaxCodeOnSettlement;
   }

   public void setStTaxCodeOnSettlement(String stTaxCodeOnSettlement) {
      this.stTaxCodeOnSettlement = stTaxCodeOnSettlement;
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
   
   public String getStAccountCodeFromARTrxLine(boolean isCheck) throws Exception{
   		return getTrxLine().getStGLArAccount2(isCheck);
   	
   }
   
   public boolean isComm() {
	return getTrxLine().isTaxed();
   }
   
   public boolean isClaimGross(){
   	   return "CLAIMG".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public boolean isPremiGrossCoas(){
   	   return "PREMIG".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public EntityView getEntity() {
        return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
    }

    public String getStParentID() {
        return stParentID;
    }

    public void setStParentID(String stParentID) {
        this.stParentID = stParentID;
    }

    public String getStRootID() {
        return stRootID;
    }

    public void setStRootID(String stRootID) {
        this.stRootID = stRootID;
    }
    
    public boolean isPremiGrossReas(){
   	   return "PREMI".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }

    public String getStAttrUnderwriting() {
        return stAttrUnderwriting;
    }

    public void setStAttrUnderwriting(String stAttrUnderwriting) {
        this.stAttrUnderwriting = stAttrUnderwriting;
    }

    public String getStAttrPolicyTypeID() {
        return stAttrPolicyTypeID;
    }

    public void setStAttrPolicyTypeID(String stAttrPolicyTypeID) {
        this.stAttrPolicyTypeID = stAttrPolicyTypeID;
    }
    
    public String getPolicyTypeGLCode() {
        return stAttrPolicyTypeID==null?null:getPolicyType().getStGLCode();
    }
    
    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stAttrPolicyTypeID);
    }
    
    public DTOList getDetails() {
      loadDetails();
      return details;
      
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }
   
   public void loadDetails() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (details == null)
                details = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_inward_details where ar_invoice_id = ? and ref_invoice_dtl_id = ? order by ar_invoice_dtl_id",
                        new Object[]{stARInvoiceID,stARInvoiceDetailID},
                        InsurancePolicyInwardDetailView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stCurrencyCode
     */
    public String getStCurrencyCode() {
        return stCurrencyCode;
    }

    /**
     * @param stCurrencyCode the stCurrencyCode to set
     */
    public void setStCurrencyCode(String stCurrencyCode) {
        this.stCurrencyCode = stCurrencyCode;
    }

    /**
     * @return the dbCurrencyRate
     */
    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }

    /**
     * @param dbCurrencyRate the dbCurrencyRate to set
     */
    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
    }

    /**
     * @return the stDeskripsi
     */
    public String getStDeskripsi() {
        return stDeskripsi;
    }

    /**
     * @param stDeskripsi the stDeskripsi to set
     */
    public void setStDeskripsi(String stDeskripsi) {
        this.stDeskripsi = stDeskripsi;
    }

    /**
     * @return the stTransactionNo
     */
    public String getStTransactionNo() {
        return stTransactionNo;
    }

    /**
     * @param stTransactionNo the stTransactionNo to set
     */
    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }

    /**
     * @return the stAttrQuartal
     */
    public String getStAttrQuartal() {
        return stAttrQuartal;
    }

    /**
     * @param stAttrQuartal the stAttrQuartal to set
     */
    public void setStAttrQuartal(String stAttrQuartal) {
        this.stAttrQuartal = stAttrQuartal;
    }

    /**
     * @return the stAttrQuartalYear
     */
    public String getStAttrQuartalYear() {
        return stAttrQuartalYear;
    }

    /**
     * @param stAttrQuartalYear the stAttrQuartalYear to set
     */
    public void setStAttrQuartalYear(String stAttrQuartalYear) {
        this.stAttrQuartalYear = stAttrQuartalYear;
    }

    /**
     * @return the stAttrPolicyNo
     */
    public String getStAttrPolicyNo() {
        return stAttrPolicyNo;
    }

    /**
     * @param stAttrPolicyNo the stAttrPolicyNo to set
     */
    public void setStAttrPolicyNo(String stAttrPolicyNo) {
        this.stAttrPolicyNo = stAttrPolicyNo;
    }

    /**
     * @return the stAttrPolicyName
     */
    public String getStAttrPolicyName() {
        return stAttrPolicyName;
    }

    /**
     * @param stAttrPolicyName the stAttrPolicyName to set
     */
    public void setStAttrPolicyName(String stAttrPolicyName) {
        this.stAttrPolicyName = stAttrPolicyName;
    }

    /**
     * @return the dtAttrPolicyPeriodStart
     */
    public Date getDtAttrPolicyPeriodStart() {
        return dtAttrPolicyPeriodStart;
    }

    /**
     * @param dtAttrPolicyPeriodStart the dtAttrPolicyPeriodStart to set
     */
    public void setDtAttrPolicyPeriodStart(Date dtAttrPolicyPeriodStart) {
        this.dtAttrPolicyPeriodStart = dtAttrPolicyPeriodStart;
    }

    /**
     * @return the dtAttrPolicyPeriodEnd
     */
    public Date getDtAttrPolicyPeriodEnd() {
        return dtAttrPolicyPeriodEnd;
    }

    /**
     * @param dtAttrPolicyPeriodEnd the dtAttrPolicyPeriodEnd to set
     */
    public void setDtAttrPolicyPeriodEnd(Date dtAttrPolicyPeriodEnd) {
        this.dtAttrPolicyPeriodEnd = dtAttrPolicyPeriodEnd;
    }

    /**
     * @return the stAttrPolicyAddress
     */
    public String getStAttrPolicyAddress() {
        return stAttrPolicyAddress;
    }

    /**
     * @param stAttrPolicyAddress the stAttrPolicyAddress to set
     */
    public void setStAttrPolicyAddress(String stAttrPolicyAddress) {
        this.stAttrPolicyAddress = stAttrPolicyAddress;
    }

    /**
     * @return the dbAttrPolicyTSI
     */
    public BigDecimal getDbAttrPolicyTSI() {
        return dbAttrPolicyTSI;
    }

    /**
     * @param dbAttrPolicyTSI the dbAttrPolicyTSI to set
     */
    public void setDbAttrPolicyTSI(BigDecimal dbAttrPolicyTSI) {
        this.dbAttrPolicyTSI = dbAttrPolicyTSI;
    }

    /**
     * @return the dbAttrPolicyTSITotal
     */
    public BigDecimal getDbAttrPolicyTSITotal() {
        return dbAttrPolicyTSITotal;
    }

    /**
     * @param dbAttrPolicyTSITotal the dbAttrPolicyTSITotal to set
     */
    public void setDbAttrPolicyTSITotal(BigDecimal dbAttrPolicyTSITotal) {
        this.dbAttrPolicyTSITotal = dbAttrPolicyTSITotal;
    }

    public InsurancePolicyTypeInwardView getPolicyTypeInward() {
        return (InsurancePolicyTypeInwardView) DTOPool.getInstance().getDTO(InsurancePolicyTypeInwardView.class, stAttrPolicyTypeID);
    }

    public String getPolicyTypeGLCodeInward() {
        return stAttrPolicyTypeID==null?null:getPolicyTypeInward().getStGLCode();
    }

    public boolean isBrokerageFee(){

        boolean isBroker = false;

        if(getTrxLine().getStARTrxLineID().equalsIgnoreCase("141"))
            isBroker = true;

   	return isBroker;
   }

    public boolean isTaxBrokerageFee(){

        boolean isTaxBroker = false;

        if(getStARTrxLineID().equalsIgnoreCase("142") || getStARTrxLineID().equalsIgnoreCase("143"))
            isTaxBroker = true;

   	return isTaxBroker;
   }

}

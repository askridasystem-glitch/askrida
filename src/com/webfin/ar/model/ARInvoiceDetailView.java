/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvoiceDetailView
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 10:38:43 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ListUtil;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyTypeView;

import java.math.BigDecimal;

public class ARInvoiceDetailView extends DTO implements RecordAudit {

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

   public ARInvoiceDetailView() {
   }

   //MARK BUAT BERSIH2X
   //public static String tableName = "ar_invoice_details_temp";
   public static String tableName = "ar_invoice_details";

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
      
   };

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
   private ARInvoiceDetailView ref;
   private String stParentID;
   private String stRootID;
   private String stAttrUnderwriting;
   private String stAttrPolicyTypeID;
   private DTOList details;

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

   public ARInvoiceDetailView getRef() {
      //if (stRefInvoiceDetailID!=null) throw new RuntimeException("ref object not linked");
      //return ref;
      return (ARInvoiceDetailView) DTOPool.getInstance().getDTO(ARInvoiceDetailView.class, stRefInvoiceDetailID);

   }

   public void setRef(ARInvoiceDetailView ref) {
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
      return BDUtil.sub(dbAmount, dbAmountSettled);
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
   
   public boolean isFeeBase2() {
	return Tools.isYes(getTrxLine().getStComissionFlag()) && getTrxLine().getStTaxCode()==null;
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
                        "select * from ar_invoice_details where ar_invoice_id = ? and ref_invoice_dtl_id = ? order by ar_invoice_dtl_id",
                        new Object[]{stARInvoiceID,stARInvoiceDetailID},
                        ARInvoiceDetailView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
   public boolean isUangMukaKlaim(){
   	   return "ADVPAYMENT".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public boolean isUangMukaPremi(){
   	   return "PREMIADVPAYMENT".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }
   
   public boolean isPPN(){
   	   return "PPN".equalsIgnoreCase(getTrxLine().getStItemDesc())?true:false;
   }

   public boolean isClaimCoins(){
        return "CLAIMN".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }

    public ARInvoiceDetailView getParentReceipt() {
        return (ARInvoiceDetailView) DTOPool.getInstance().getDTO(ARInvoiceDetailView.class, stRootID);
    }
    
    public ARInvoiceDetailView getParentRefInvoiceDetail() {
        return (ARInvoiceDetailView) DTOPool.getInstance().getDTO(ARInvoiceDetailView.class, stRefInvoiceDetailID);
    }
    
    public boolean isFeeBase(){
   	   return "COMMISSION".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }
    
    public String getStGLAccountCode2() {
      final ARTransactionLineView tl = getTrxLine();
      return tl==null?null:tl.getStGLApAccount2();
   }
    
    private DTOList refInvoice;  

    public DTOList getRefInvoice() {
        loadRefInvoice();
        return refInvoice;
    }

    public void setRefInvoice(DTOList refInvoice) {
        this.refInvoice = refInvoice;
    }
    
    public void loadRefInvoice() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (refInvoice == null)
                refInvoice = ListUtil.getDTOListFromQuery(
                        "select * from ar_invoice where ref_invoice_id = ? and no_surat_hutang like 'SHP/%' order by ar_invoice_id",
                        new Object[]{stRefInvoiceDetailID},
                        ARInvoiceView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
   public boolean isFeeBase3(){
        return "FEEBASE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isDeduct(){
        return "DEDUCT".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isSubrogasi(){
        return "SBRGS".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isAdjusterFee(){
        return "AFEE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isTJH(){
        return "TJH".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isTowingCost(){
        return "TCOST".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isSalvage(){
        return "SALVAGE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isEXGratia(){
        return "EXGRATIA".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isEXGratiaUW(){
        return "EXGRATIAUW".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isJBengkel(){
        return "JBENGKEL".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isSparepart(){
        return "SPAREPART".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isWreck(){
        return "WRECK".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isIntPayment(){
        return "INTPAYMENT".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isDepresiasi(){
        return "DEPRESIASI".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isPinalty(){
        return "PINALTY".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isPPH23(){
        return "PPH23".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isFEERCVY(){
        return "FEERCVRY".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isCashColl(){
        return "CCOLL".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isBunga(){
        return "BUNGA".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isExpenses(){
        return "EXPFEE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isVatFee(){
        return "VATFEE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isExAccident(){
        return "EXACCIDENT".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isAdminFee(){
        return "ADMINFEE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isSurveyAdjFee(){
        return "SADJFEE".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isMaterial(){
        return "MTRIAL".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isCostSurvey(){
        return "SCOST".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
    }

    public boolean isClaimReas(){
   	   return "KLAIM".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }

   public boolean isPPNFeebase(){
   	   return "PPN Fee Base".equalsIgnoreCase(getTrxLine().getStItemDesc())?true:false;
   }

   public boolean isKomisi(){
        return getTrxLine().getStItemClass()==null && "COMMISSION".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
    }

    public boolean isTaxJBengkel() {
        return "TAXBENGKELFEE".equalsIgnoreCase(getTrxLine().getStCategory()) ? true : false;
    }

    public boolean isPAPassenger() {
        return "PAPSSGER".equalsIgnoreCase(getTrxLine().getStItemClass()) ? true : false;
    }

    public boolean isPADriver() {
        return "PADRIVER".equalsIgnoreCase(getTrxLine().getStItemClass()) ? true : false;
    }

    public boolean isPremiGrossReas2(){
   	   return "PREMIG".equalsIgnoreCase(getTrxLine().getStItemClass())?true:false;
   }

    public String getStGLAccountCodeRestitusi() {
      final ARTransactionLineView tl = getTrxLine();
      return tl==null?null:tl.getStGLAccount2();
   }

    public boolean isKomisi2(){
        return getTrxLine().getStItemClass()==null && "COMMISSION".equalsIgnoreCase(getTrxLine().getStCategory()) && getTrxLine().getStComissionFlag()!=null?true:false;
    }

    private DTOList items;

    public DTOList getItems() {
      loadItems();
      return items;

   }

   public void setItems(DTOList items) {
      this.items = items;
   }

   public void loadItems() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (items == null)
                items = ListUtil.getDTOListFromQuery(
                        "select c.* "+
                        " from ar_invoice a "+
                        " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id "+
                        " inner join ins_pol_items c on a.attr_pol_id = c.pol_id  "+
                        " inner join ins_items d on c.ins_item_id = d.ins_item_id and d.ar_trx_line_id = b.ar_trx_line_id "+
                        " where b.ar_invoice_dtl_id = ?",
                        new Object[]{stARInvoiceDetailID},
                        InsurancePolicyItemsView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   public boolean isPPNKomisi(){
   	   return "PPN Komisi".equalsIgnoreCase(getTrxLine().getStItemDesc())?true:false;
   }

    public boolean isKomisiReas(){
   	   return "COMMISSION".equalsIgnoreCase(getTrxLine().getStCategory())?true:false;
   }

}

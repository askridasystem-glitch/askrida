/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:22:33 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.Tools;
import com.crux.util.stringutil.StringUtil;
import com.webfin.ar.model.ARTaxView;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;

import java.math.BigDecimal;

public class InsurancePolicyItemsView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_items
(
  ins_pol_item_id int8 NOT NULL,
  pol_id int8,
  ins_item_id int8,
  description varchar(255),
  amount numeric,
  ar_invoice_id int8,
  ap_invoice_id int8,
  CONSTRAINT ins_pol_items_pk PRIMARY KEY (ins_pol_item_id)

ALTER TABLE ins_pol_items ADD COLUMN calc_mode varchar(5);
ALTER TABLE ins_pol_items ADD COLUMN entered_amount numeric;

)



   */

   private String stPolicyItemID;
   private String stPolicyID;
   private String stInsItemID;
   private String stDescription;
   private BigDecimal dbAmount;
   private String stARInvoiceID;
   private String stAPInvoiceID;
   private String stEntityID;
   private String stPostedFlag;
   private String stCalculationMode;
   private String stFlagEntryByRate;
   private String stGLAccount;
   private String stGLAccountID;
   private String stGLAccountDesc;
   private String stTaxCode;
   private String stItemClass;
   private String stROFlag;
   private BigDecimal dbEnteredAmount;
   private BigDecimal dbRate;
   private InsuranceItemsView insuranceItem;
   private String stNPWP;
   private BigDecimal dbTotalAmount;
   private BigDecimal dbTotalAmountTaxable;
   private BigDecimal dbTotalAmountUntilThisMonth;
   private String stReferencePolicyID;

    public String getStReferencePolicyID() {
        return stReferencePolicyID;
    }

    public void setStReferencePolicyID(String stReferencePolicyID) {
        this.stReferencePolicyID = stReferencePolicyID;
    }

   public InsurancePolicyItemsView() {
   }

   public String getStEntityName() {
      final EntityView en = getEntity();
      return en==null?null:en.getStEntityName();
   }

   public InsuranceItemsView getInsuranceItem() {
      if (insuranceItem==null) insuranceItem=getInsItem();
      return insuranceItem;
   }

   public void setInsuranceItem(InsuranceItemsView insuranceItem) {
      this.insuranceItem = insuranceItem;
   }

   public static String tableName = "ins_pol_items";

   public static String fieldMap[][] = {
      {"stPolicyItemID","ins_pol_item_id*pk*nd"},
      {"stPolicyID","pol_id"},
      {"stInsItemID","ins_item_id"},
      {"stDescription","description"},
      {"dbAmount","amount"},
      {"stARInvoiceID","ar_invoice_id"},
      {"stAPInvoiceID","ap_invoice_id"},
      {"stEntityID","ent_id"},
      {"stPostedFlag","posted_flag"},
      {"stCalculationMode","calc_mode"},
      {"dbEnteredAmount","entered_amount"},
      {"dbRate","rate"},
      {"stFlagEntryByRate","f_entryrate"},
      {"stTaxCode","tax_code"},
      {"stItemClass","item_class"},
      {"stROFlag","f_ro"},
      {"stTaxAutoRateFlag","f_tax_auto_rate"},
      {"dbTaxAmount","tax_amount"},
      {"dbTaxRate","tax_rate"},
      {"stTaxAutoAmountFlag","f_tax_auto_amount"},
      {"stChargableFlag","f_chargable"},
      {"dbAmountSettled","amount_settled"},
      {"stClaimItemSettledFlag","f_settled"},
      {"stNPWP","npwp"},
      {"stReferencePolicyID","reference_pol_id"},
      {"dbPph21SampaiSaatIni","pph21_sd_saat_ini"},
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
      {"stPolicyNo","pol_no*n"},
      {"stNoRekening","no_rekening*n"},
      
//      {"dbTotalAmount","total_amount"},
//      {"dbTotalAmountTaxable","total_amount_taxable"},
//      {"dbTotalAmountUntilThisMonth","total_amount_until_this_month*n"},


   };

   private String stTaxAutoRateFlag;
   private String stTaxAutoAmountFlag;
   private String stChargableFlag;
   private String stClaimItemSettledFlag;
   private BigDecimal dbTaxRate;
   private BigDecimal dbTaxAmount;
   private BigDecimal dbAmountSettled;
   private String stInsuranceItemCategory;
   private BigDecimal dbPph21SampaiSaatIni;
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
   private String stPolicyNo;
   private String stNoRekening;

    public String getStNoRekening() {
        
      final EntityView en = getEntity();

      return en.getStRcNo();
    }

    public void setStNoRekening(String stNoRekening) {
        this.stNoRekening = stNoRekening;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
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

    public BigDecimal getDbAmount100() {
        return dbAmount100;
    }

    public void setDbAmount100(BigDecimal dbAmount100) {
        this.dbAmount100 = dbAmount100;
    }

    public BigDecimal getDbAmount1() {
        return dbAmount1;
    }

    public void setDbAmount1(BigDecimal dbAmount1) {
        this.dbAmount1 = dbAmount1;
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

    public BigDecimal getDbPph21SampaiSaatIni() {
        return dbPph21SampaiSaatIni;
    }

    public void setDbPph21SampaiSaatIni(BigDecimal dbPph21SampaiSaatIni) {
        this.dbPph21SampaiSaatIni = dbPph21SampaiSaatIni;
    }

   public String getStInsuranceItemCategory() {
      return stInsuranceItemCategory;
   }

   public void setStInsuranceItemCategory(String stInsuranceItemCategory) {
      this.stInsuranceItemCategory = stInsuranceItemCategory;
   }


   public String getStTaxAutoRateFlag() {
      return stTaxAutoRateFlag;
   }

   public void setStTaxAutoRateFlag(String stTaxAutoRateFlag) {
      this.stTaxAutoRateFlag = stTaxAutoRateFlag;
   }

   public String getStTaxAutoAmountFlag() {
      return stTaxAutoAmountFlag;
   }

   public void setStTaxAutoAmountFlag(String stTaxAutoAmountFlag) {
      this.stTaxAutoAmountFlag = stTaxAutoAmountFlag;
   }

   public BigDecimal getDbTaxRatePct() {
      return BDUtil.getPctFromRate(dbTaxRate);
   }

   public BigDecimal getDbTaxRate() {
      return dbTaxRate;
   }

   public void setDbTaxRatePct(BigDecimal dbTaxRate) {
      setDbTaxRate(BDUtil.getRateFromPct(dbTaxRate));
   }

   public void setDbTaxRate(BigDecimal dbTaxRate) {
      this.dbTaxRate = dbTaxRate;
   }

   public String getStPolicyItemID() {
      return stPolicyItemID;
   }

   public void setStPolicyItemID(String stPolicyItemID) {
      this.stPolicyItemID = stPolicyItemID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStInsItemID() {
      return stInsItemID;
   }

   public void setStInsItemID(String stInsItemID) {
      this.stInsItemID = stInsItemID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStARInvoiceID() {
      return stARInvoiceID;
   }

   public void setStARInvoiceID(String stARInvoiceID) {
      this.stARInvoiceID = stARInvoiceID;
   }

   public String getStAPInvoiceID() {
      return stAPInvoiceID;
   }

   public void setStAPInvoiceID(String stAPInvoiceID) {
      this.stAPInvoiceID = stAPInvoiceID;
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public String getStPostedFlag() {
      return stPostedFlag;
   }

   public void setStPostedFlag(String stPostedFlag) {
      this.stPostedFlag = stPostedFlag;
   }

   public boolean isComission() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isComission();
   }

   public boolean isKomisi() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisi();
   }

     public boolean isBrokerFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isBrokerFee();
   }

    public boolean isHandlingFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isHandlingFee();
   }

    public boolean isStampFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isStampFee();
   }

    public boolean isPolicyCost() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isPolicyCost();
   }

   public InsuranceItemsView getInsItem() {
      if(getStInsItemID()==null) return null;
      return (InsuranceItemsView) DTOPool.getInstance().getDTO(InsuranceItemsView.class, getStInsItemID());
   }

   public void setStCalculationMode(String stCalculationMode) {
      this.stCalculationMode = stCalculationMode;
   }

   public String getStCalculationMode() {
      return stCalculationMode;
   }

   public void setDbEnteredAmount(BigDecimal dbEnteredAmount) {
      this.dbEnteredAmount = dbEnteredAmount;
   }

   public BigDecimal getDbEnteredAmount() {
      return dbEnteredAmount;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public EntityView getEntity() {
      return (EntityView) (stEntityID == null?null:DTOPool.getInstance().getDTO(EntityView.class,stEntityID));
   }

   public boolean isPremi() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isPremi();
   }

   public boolean isFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isFee();
   }

   public boolean isDiscount() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isDiscount();
   }

   public String getStFlagEntryByRate() {
      return stFlagEntryByRate;
   }

   public void setStFlagEntryByRate(String stFlagEntryByRate) {
      this.stFlagEntryByRate = stFlagEntryByRate;
   }

   public boolean isEntryByRate() {
      return Tools.isYes(stFlagEntryByRate);
   }

   public BigDecimal getDbRatePct() {
      return dbRate==null?null:BDUtil.div(dbRate,BDUtil.hundred,10);
   }

   public void setDbRatePct(BigDecimal r) {
      dbRate = r==null?null:BDUtil.mul(r,BDUtil.hundred);
   }

   public void calculateRateAmount(BigDecimal factor,int scale) {
      if (isEntryByRate()){
          setDbAmount(BDUtil.mul(factor, getDbRatePct(),scale));
      }else{
          setDbRatePct(BDUtil.div(getDbAmount(),factor,15));
      }
         
   }

   public String getStGLAccount() {
      return stGLAccount;
   }

   public void setStGLAccount(String stGLAccount) {
      this.stGLAccount = stGLAccount;
   }

   public String getStAccountDesc() {
      final AccountView ac = getAccount();

      if (ac==null) {
         //return GLUtil.Chart.getInstance().getChartDescription(stGLAccount);
         return stGLAccountDesc;
      }

      return ac.getStAccountNo() +"/"+ac.getStDescription();

   }

   private AccountView getAccount() {
      return (AccountView) DTOPool.getInstance().getDTO(AccountView.class, stGLAccountID);
   }

   public String getStGLAccountID() {
      return stGLAccountID;
   }

   public void setStGLAccountID(String stGLAccountID) {
      this.stGLAccountID = stGLAccountID;
   }

   public String getStGLAccountDesc() {
      return stGLAccountDesc;//getStAccountDesc();
   }

   public void setStGLAccountDesc(String stGLAccountDesc) {
      this.stGLAccountDesc = stGLAccountDesc;
   }

   public String getStTaxCode() {
      return stTaxCode;
   }

   public void setStTaxCode(String stTaxCode) {
      this.stTaxCode = stTaxCode;
   }



   public String getStDescription2() {
        return getStInsuranceItemDescription() + " " + StringUtil.deBlank(stDescription);
    }

   private String getStInsuranceItemDescription() {
      getInsuranceItem();

      if (insuranceItem==null) return "?";

      return insuranceItem.getStDescription();

   }

   public ARTaxView getTax() {
      final ARTaxView tax = (ARTaxView) DTOPool.getInstance().getDTO(ARTaxView.class, stTaxCode);

      return tax;
   }

   public BigDecimal getDbTaxAmount() {
      return dbTaxAmount;
   }

   public void setDbTaxAmount(BigDecimal dbTaxAmount) {
      this.dbTaxAmount = dbTaxAmount;
   }

   public BigDecimal calculateDbTaxAmount() {

      final ARTaxView tx = getTax();
      if (tx==null) return null;

      final BigDecimal taxAmount = BDUtil.mul(
                    getDbAmount(),
                    tx.getDbRate()

            );

      return taxAmount;
   }

   public BigDecimal getDbNetAmount() {
      return BDUtil.sub(getDbAmount(), getDbTaxAmount());
   }

   public String getStItemClass() {
      return stItemClass;
   }

   public void setStItemClass(String stItemClass) {
      this.stItemClass = stItemClass;
   }

   public String getStROFlag() {
      return stROFlag;
   }

   public void setStROFlag(String stROFlag) {
      this.stROFlag = stROFlag;
   }

   public boolean isDeletable() {
      return !Tools.isYes(stROFlag);
   }

   public boolean isAutoTaxRate() {
      return !Tools.isYes(stTaxAutoRateFlag);
   }

   public boolean isAutoTaxAmount() {
      return !Tools.isYes(stTaxAutoAmountFlag);
   }

   private String stTaxGLAccount;
   private String stTaxGLAccountDesc;

   public String getStTaxGLAccount() {
      return stTaxGLAccount;
   }

   public void setStTaxGLAccount(String stTaxGLAccount) {
      this.stTaxGLAccount = stTaxGLAccount;
   }

   public String getStTaxGLAccountDesc() {
      return stTaxGLAccountDesc;
   }

   public void setStTaxGLAccountDesc(String stTaxGLAccountDesc) {
      this.stTaxGLAccountDesc = stTaxGLAccountDesc;
   }

   public String getStChargableFlag() {
      return stChargableFlag;
   }

   public void setStChargableFlag(String stChargableFlag) {
      this.stChargableFlag = stChargableFlag;
   }

   public String getStClaimItemSettledFlag() {
      return stClaimItemSettledFlag;
   }

   public void setStClaimItemSettledFlag(String stClaimItemSettledFlag) {
      this.stClaimItemSettledFlag = stClaimItemSettledFlag;
   }

   public BigDecimal getDbAmountSettled() {
      return dbAmountSettled;
   }

   public void setDbAmountSettled(BigDecimal dbAmountSettled) {
      this.dbAmountSettled = dbAmountSettled;
   }
   
   public String getStNPWP() {
      final EntityView en = getEntity();

      String npwp = null;

      if(en!=null) npwp = en.getStTaxFile();
      
      if(stNPWP!=null) npwp = stNPWP;
      
      return npwp;
      //return stNPWP;
      //return stNPWP!=null?stNPWP:en.getStTaxFile();
   }

   public void setStNPWP(String stNPWP) {
      this.stNPWP = stNPWP;
   }
   
   public boolean isKomisiA() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisiA();
   }
   
   public boolean isKomisiB() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisiB();
   }
   
   public boolean isKomisiC() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisiC();
   }
   
   public boolean isKomisiD() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisiD();
   }
   
   public void calculateRateAmount2(BigDecimal factor,int scale, BigDecimal askridaShare) {
      if (isEntryByRate()){
          setDbAmount(BDUtil.mul(factor, getDbRatePct(),scale));
          setDbAmount(BDUtil.mul(getDbAmount(), BDUtil.getRateFromPct(askridaShare), scale));
      }else{
          setDbRatePct(BDUtil.div(getDbAmount(),factor,15));
      }
         
   }

   public boolean isAFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isAFee();
   }

    public boolean isGratia() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isGratia(); 
   }
    
    public boolean isPPN() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isPPN();
   }
    
    public boolean isFeeBase() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isNotUseTax();
   }
    
    public boolean isJasaBengkel() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isJasaBengkel();
   }

    public boolean isPPNFeeBase() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isPPNFeeBase();
   }

   public void calculateRateAmountInclusive(BigDecimal factor,int scale,BigDecimal upFactor) {
      if (isEntryByRate()){
          setDbAmount(BDUtil.mul(factor, getDbRatePct(),scale));
      }else{
          setDbRatePct(BDUtil.div(getDbAmount(),factor,15));
      }

   }


   public boolean isBrokerFeeIncludePPN() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isBrokerFeeIncludePPN();

   }

   public boolean isKomisi2() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isKomisi2();
   }

   public boolean isPPNInclude() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isIncludePPN();

   }

    /**
     * @return the dbTotalAmount
     */
    public BigDecimal getDbTotalAmount() {
        return dbTotalAmount;
    }

    /**
     * @param dbTotalAmount the dbTotalAmount to set
     */
    public void setDbTotalAmount(BigDecimal dbTotalAmount) {
        this.dbTotalAmount = dbTotalAmount;
    }

    /**
     * @return the dbTotalAmountTaxable
     */
    public BigDecimal getDbTotalAmountTaxable() {
        return dbTotalAmountTaxable;
    }

    /**
     * @param dbTotalAmountTaxable the dbTotalAmountTaxable to set
     */
    public void setDbTotalAmountTaxable(BigDecimal dbTotalAmountTaxable) {
        this.dbTotalAmountTaxable = dbTotalAmountTaxable;
    }

    /**
     * @return the dbTotalAmountUntilThisMonth
     */
    public BigDecimal getDbTotalAmountUntilThisMonth() {
        return dbTotalAmountUntilThisMonth;
    }

    /**
     * @param dbTotalAmountUntilThisMonth the dbTotalAmountUntilThisMonth to set
     */
    public void setDbTotalAmountUntilThisMonth(BigDecimal dbTotalAmountUntilThisMonth) {
        this.dbTotalAmountUntilThisMonth = dbTotalAmountUntilThisMonth;
    }

    public boolean isSurveyAFee() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isSAdjFee();
   }

    public boolean isSubrogasi() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isSubrogasi();
   }

    public boolean isPPNComission() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isPPNComission();
   }

    public boolean isComission2() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isCommission2();
   }

    public InsurancePolicyView getReferencePolicy() {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stReferencePolicyID);
    }

    public boolean isOfGross() {

      return "GROSS".equalsIgnoreCase(stCalculationMode);
   }

    public boolean isFeeRecovery() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isFeeRecovery();
   }

    public boolean isInsentifSubrogasi() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isInsentifSubrogasi();
   }

    public BigDecimal getDbNetAmount100() {
      return BDUtil.sub(getDbAmount100(), getDbTaxAmount100());
   }

    public BigDecimal getDbNetAmount1() {
      return BDUtil.sub(getDbAmount1(), getDbTaxAmount1());
   }

    public BigDecimal getDbNetAmount2() {
      return BDUtil.sub(getDbAmount2(), getDbTaxAmount2());
   }

    public BigDecimal getDbNetAmount3() {
      return BDUtil.sub(getDbAmount3(), getDbTaxAmount3());
   }

    public BigDecimal getDbNetAmount4() {
      return BDUtil.sub(getDbAmount4(), getDbTaxAmount4());
   }

    public BigDecimal getDbNetAmount5() {
      return BDUtil.sub(getDbAmount5(), getDbTaxAmount5());
   }

    public BigDecimal getDbNetAmount6() {
      return BDUtil.sub(getDbAmount6(), getDbTaxAmount6());
   }

    public boolean isIncludePPN() {
      final InsuranceItemsView insItem = getInsItem();

      if (insItem==null) return false;

      return insItem.isIncludePPN();
   }

}

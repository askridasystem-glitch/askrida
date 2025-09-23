/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceItemsView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:51:57 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.util.GLUtil;

import java.math.BigDecimal;

public class InsuranceItemsView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_items
(
  ins_item_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  comission_flag varchar(1),
  premi_flag varchar(1),
  negative_flag varchar(1),
  item_type varchar(5),
  calc_mode varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_items_pk PRIMARY KEY (ins_item_id)

ALTER TABLE ins_items ADD COLUMN ar_trx_line_id int8;
ALTER TABLE ins_items ADD COLUMN ap_trx_line_id int8;
ALTER TABLE ins_items ADD COLUMN entry_min int8;
ALTER TABLE ins_items ADD COLUMN entry_max int8;

)
   */

   private String stInsuranceItemID;
   private String stDescription;
   private String stActiveFlag;
   private String stItemType;
   private String stCalcMode;
   private String stGLAccount;
   private String stARTransactionTypeLineID;
   private String stAPTransactionTypeLineID;
   private String stInsuranceCoverSourceID;
   private String stItemCode;
   private String stItemCategory;
   private String stInsuranceItemClass;
   private String stEntityFlag;
   private Long lgEntryMin;
   private Long lgEntryMax;
   private String stUseTaxFlag;
   private String stARAPSeparately;
   private String stItemGroup;
   private String stReinsuranceChargeFlag;
   private String stReinsuranceTaxChargeFlag;
   private String stInsuranceItemChildID;
   private BigDecimal dbDefaultValue;
   private String stIncludePPNFlag;
   private BigDecimal dbCalculationFactor1;
   private BigDecimal dbCalculationFactor2;

   public static String tableName = "ins_items";

   public static String fieldMap[][] = {
      {"stInsuranceItemID","ins_item_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"stGLAccount","gl_account"},
//      {"stNegativeFlag","negative_flag"},
      {"stItemType","item_type"},
      {"stCalcMode","calc_mode"},
      {"stARTransactionTypeLineID","ar_trx_line_id"},
      {"stAPTransactionTypeLineID","ap_trx_line_id"},
      {"stInsuranceCoverSourceID","ins_cover_source_id"},
      {"lgEntryMin","entry_min"},
      {"lgEntryMax","entry_max"},
      {"stItemCode","item_code"},
      {"stItemCategory","ins_item_cat"},
      {"stInsuranceItemClass","ins_item_class"},
      {"stEntityFlag","entity_flag"},
      {"stUseTaxFlag","use_tax"},
      {"stARAPSeparately","ar_ap_separately"},
      {"stItemGroup","item_group"},
      {"stReinsuranceChargeFlag","reins_charge"},
      {"stReinsuranceTaxChargeFlag","reins_tax_charge"},
      {"stInsuranceItemChildID","ins_item_child_id"},
      {"dbDefaultValue","default_value"},
      {"stIncludePPNFlag","include_ppn_f"},
      {"dbCalculationFactor1","calc_factor1"},
      {"dbCalculationFactor2","calc_factor2"},

   };

    public BigDecimal getDbCalculationFactor1() {
        return dbCalculationFactor1;
    }

    public void setDbCalculationFactor1(BigDecimal dbCalculationFactor1) {
        this.dbCalculationFactor1 = dbCalculationFactor1;
    }

    public BigDecimal getDbCalculationFactor2() {
        return dbCalculationFactor2;
    }

    public void setDbCalculationFactor2(BigDecimal dbCalculationFactor2) {
        this.dbCalculationFactor2 = dbCalculationFactor2;
    }

   public String getStEntityFlag() {
      return stEntityFlag;
   }

   public void setStEntityFlag(String stEntityFlag) {
      this.stEntityFlag = stEntityFlag;
   }

   public String getStItemCategory() {
      return stItemCategory;
   }

   public void setStItemCategory(String stItemCategory) {
      this.stItemCategory = stItemCategory;
   }

   public String getStItemCode() {
      return stItemCode;
   }

   public void setStItemCode(String stItemCode) {
      this.stItemCode = stItemCode;
   }

   public static String comboFields[] = {"ins_item_id","description"};


   public String getStInsuranceItemID() {
      return stInsuranceItemID;
   }

   public void setStInsuranceItemID(String stInsuranceItemID) {
      this.stInsuranceItemID = stInsuranceItemID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStItemType() {
      return stItemType;
   }

   public void setStItemType(String stItemType) {
      this.stItemType = stItemType;
   }

   public String getStCalcMode() {
      return stCalcMode;
   }

   public void setStCalcMode(String stCalcMode) {
      this.stCalcMode = stCalcMode;
   }

   public boolean isComission() {
      //return Tools.isYes(stCommissionFlag);
      return FinCodec.InsuranceItemType.KOMISI.equalsIgnoreCase(stItemType);
   }

   public boolean isPremi() {
      //return Tools.isYes(stCommissionFlag);
      return FinCodec.InsuranceItemType.PREMI.equalsIgnoreCase(stItemType);
   }

   public boolean isFee() {
      return FinCodec.InsuranceItemType.FEE.equalsIgnoreCase(stItemType);
   }

   public boolean isDiscount() {
      return FinCodec.InsuranceItemType.DISCOUNT.equalsIgnoreCase(stItemType);
   }

   public boolean isKomisi(){
       return "COMM".equalsIgnoreCase(stItemCategory);
   }

   public boolean isBrokerFee(){
       return "BROKR".equalsIgnoreCase(stItemCategory);
   }

   public boolean isHandlingFee(){
       return "HFEE".equalsIgnoreCase(stItemCategory);
   }

   public boolean isStampFee(){
       return "SFEE".equalsIgnoreCase(stItemCategory);
   }

   public boolean isPolicyCost(){
       return "PCOST".equalsIgnoreCase(stItemCategory);
   }
   
   public boolean isKomisiA(){
       return "A".equalsIgnoreCase(stItemCode);
   }
   
   public boolean isKomisiB(){
       return "B".equalsIgnoreCase(stItemCode);
   }
   
   public boolean isKomisiC(){
       return "C".equalsIgnoreCase(stItemCode);
   }
   
   public boolean isKomisiD(){
       return "D".equalsIgnoreCase(stItemCode);
   }

   public String getStGLAccount() {
      return stGLAccount;
   }

   public void setStGLAccount(String stGLAccount) {
      this.stGLAccount = stGLAccount;
   }

   public String getStARTransactionTypeLineID() {
      return stARTransactionTypeLineID;
   }

   public void setStARTransactionTypeLineID(String stARTransactionTypeLineID) {
      this.stARTransactionTypeLineID = stARTransactionTypeLineID;
   }

   public String getStAPTransactionTypeLineID() {
      return stAPTransactionTypeLineID;
   }

   public void setStAPTransactionTypeLineID(String stAPTransactionTypeLineID) {
      this.stAPTransactionTypeLineID = stAPTransactionTypeLineID;
   }

   public String getStInsuranceCoverSourceID() {
      return stInsuranceCoverSourceID;
   }

   public void setStInsuranceCoverSourceID(String stInsuranceCoverSourceID) {
      this.stInsuranceCoverSourceID = stInsuranceCoverSourceID;
   }

   public Long getLgEntryMin() {
      return lgEntryMin;
   }

   public void setLgEntryMin(Long lgEntryMin) {
      this.lgEntryMin = lgEntryMin;
   }

   public Long getLgEntryMax() {
      return lgEntryMax;
   }

   public void setLgEntryMax(Long lgEntryMax) {
      this.lgEntryMax = lgEntryMax;
   }

   public ARTransactionLineView getARTrxLine() {
      return (ARTransactionLineView)DTOPool.getInstance().getDTO(ARTransactionLineView.class,stARTransactionTypeLineID);
   }

   public String getStInsuranceItemClass() {
      return stInsuranceItemClass;
   }

   public void setStInsuranceItemClass(String stInsuranceItemClass) {
      this.stInsuranceItemClass = stInsuranceItemClass;
   }

   public boolean isAFee(){
       return "AFEE".equalsIgnoreCase(stItemCategory);
   }

   public boolean isGratia(){
       return "GRATIA".equalsIgnoreCase(stItemCategory);
   }
   
   public boolean isPPN(){
       return "PPN".equalsIgnoreCase(stItemCategory);
   }

    public String getStUseTaxFlag()
    {
        return stUseTaxFlag;
    }

    public void setStUseTaxFlag(String stUseTaxFlag)
    {
        this.stUseTaxFlag = stUseTaxFlag;
    }
    
    public boolean isNotUseTax(){
       return "N".equalsIgnoreCase(stUseTaxFlag);
   }
    
    public boolean isFeeBase(){
       return "FEEBASE".equalsIgnoreCase(stItemCategory);
   }
    
    public boolean isJasaBengkel() {
      //return Tools.isYes(stCommissionFlag);
      return "BENGKELFEE".equalsIgnoreCase(stItemType);
   }

   public boolean isAdjusterFee(){
       return "ADJ".equalsIgnoreCase(stItemType);
   }

   public boolean isSubrogasi(){
       return "SUBROGATION".equalsIgnoreCase(stItemType);
   }

   public boolean isSalvage(){
       return "SALVAGE".equalsIgnoreCase(stItemType);
   }

    /**
     * @return the stARAPSeparately
     */
    public String getStARAPSeparately() {
        return stARAPSeparately;
    }

    /**
     * @param stARAPSeparately the stARAPSeparately to set
     */
    public void setStARAPSeparately(String stARAPSeparately) {
        this.stARAPSeparately = stARAPSeparately;
    }

    public boolean isCreateArApSeparately(){
       return "Y".equalsIgnoreCase(stARAPSeparately);
   }

        /**
     * @return the stItemGroup
     */
    public String getStItemGroup() {
        return stItemGroup;
    }

    /**
     * @param stItemGroup the stItemGroup to set
     */
    public void setStItemGroup(String stItemGroup) {
        this.stItemGroup = stItemGroup;
    }

    /**
     * @return the stReinsuranceChargeFlag
     */
    public String getStReinsuranceChargeFlag() {
        return stReinsuranceChargeFlag;
    }

    /**
     * @param stReinsuranceChargeFlag the stReinsuranceChargeFlag to set
     */
    public void setStReinsuranceChargeFlag(String stReinsuranceChargeFlag) {
        this.stReinsuranceChargeFlag = stReinsuranceChargeFlag;
    }

    public boolean isNotReinsCharge(){
        return Tools.isNo(stReinsuranceChargeFlag);
    }

    public boolean isFeeBase2(){
        return "FEEBASE".equalsIgnoreCase(stItemGroup);
    }
    
    public boolean isCommission2(){
        return "COMM".equalsIgnoreCase(stItemGroup);
    }

    public boolean isUangMukaKlaim(){
       return "ADVPAYMENT".equalsIgnoreCase(stItemType);
   }

    public boolean isUseTax(){
       return "Y".equalsIgnoreCase(stUseTaxFlag);
   }

    /**
     * @return the stReinsuranceTaxChargeFlag
     */
    public String getStReinsuranceTaxChargeFlag() {
        return stReinsuranceTaxChargeFlag;
    }

    /**
     * @param stReinsuranceTaxChargeFlag the stReinsuranceTaxChargeFlag to set
     */
    public void setStReinsuranceTaxChargeFlag(String stReinsuranceTaxChargeFlag) {
        this.stReinsuranceTaxChargeFlag = stReinsuranceTaxChargeFlag;
    }

    public boolean isPPNFeeBase(){
       return "PPN_FB".equalsIgnoreCase(stItemCategory);
   }

    public boolean isBrokerFeeIncludePPN(){
       return "BROKR".equalsIgnoreCase(stItemCategory) && "BROKRPPN".equalsIgnoreCase(stItemCode);
   }

    /**
     * @return the stInsuranceItemChildID
     */
    public String getStInsuranceItemChildID() {
        return stInsuranceItemChildID;
    }

    /**
     * @param stInsuranceItemChildID the stInsuranceItemChildID to set
     */
    public void setStInsuranceItemChildID(String stInsuranceItemChildID) {
        this.stInsuranceItemChildID = stInsuranceItemChildID;
    }

    /**
     * @return the dbDefaultValue
     */
    public BigDecimal getDbDefaultValue() {
        return dbDefaultValue;
    }

    /**
     * @param dbDefaultValue the dbDefaultValue to set
     */
    public void setDbDefaultValue(BigDecimal dbDefaultValue) {
        this.dbDefaultValue = dbDefaultValue;
    }

   public boolean isKomisi2(){
       return "COMM".equalsIgnoreCase(stItemGroup);
   }

   public boolean isIncludePPN(){
       return "Y".equalsIgnoreCase(getStIncludePPNFlag());
   }

    /**
     * @return the stIncludePPNFlag
     */
    public String getStIncludePPNFlag() {
        return stIncludePPNFlag;
    }

    /**
     * @param stIncludePPNFlag the stIncludePPNFlag to set
     */
    public void setStIncludePPNFlag(String stIncludePPNFlag) {
        this.stIncludePPNFlag = stIncludePPNFlag;
    }

    public boolean isSAdjFee() {
        return "SADJFEE".equalsIgnoreCase(stItemCategory);
    }

    public boolean isSurveyFee(){
       return "SURVEY".equalsIgnoreCase(stItemType);
   }

    public boolean isPPNComission(){
       return "PPN_COMM".equalsIgnoreCase(stItemCategory);
   }

    public boolean isFeeRecovery(){
       return "FEESUBRO".equalsIgnoreCase(stItemType);
   }

    public boolean isInsentifSubrogasi(){
       return "INSENTIFSUBRO".equalsIgnoreCase(stItemType);
   }

    public boolean isDeductible(){
       return "DEDUCT".equalsIgnoreCase(stItemType);
   }

    public boolean isClaimGross(){
       return "CLAIMG".equalsIgnoreCase(stItemType);
   }

    public boolean isTJH(){
       return "51".equalsIgnoreCase(stInsuranceItemID);
   }

    public boolean isBiayaSparepart(){
       return "74".equalsIgnoreCase(stInsuranceItemID);
   }

    public boolean isBunga(){
       return "55".equalsIgnoreCase(stInsuranceItemID);
   }

    public boolean isNotActive(){
        return "N".equalsIgnoreCase(stActiveFlag);
    }

}

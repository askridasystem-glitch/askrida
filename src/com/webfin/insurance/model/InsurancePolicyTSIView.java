/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyTSIView
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:38:42 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;

import java.math.BigDecimal;

public class InsurancePolicyTSIView extends DTO implements RecordAudit {

   /*
   CREATE TABLE
(
   int8 NOT NULL,
   int8,
   int8,
   text,
   numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_tsi_pk PRIMARY KEY (ins_pol_tsi_id)
)
WITH
   */

   private String stInsurancePolicyTSIID;
   private String stInsurancePolicyTSIRefID;
   private String stDescription;
   private String stPolicyID;
   private String stInsuranceTSIID;
   private String stInsuranceTSIDesc;
   private String stInsurancePolicyObjectID;
   private String stVoidFlag;
   private String stAutoFlag;
   private BigDecimal dbInsuredAmount;
   private BigDecimal dbInsuredAmountRef;
   private BigDecimal dbInsuredAmountFull;
 
   public static String tableName = "ins_pol_tsi";

   public static String fieldMap[][] = {
      {"stInsurancePolicyTSIID","ins_pol_tsi_id*pk*nd"},
      {"stInsurancePolicyTSIRefID","ins_pol_tsi_ref_id"},
      {"stPolicyID","pol_id"},
      {"stInsuranceTSIID","ins_tsi_cat_id"},
      {"stDescription","description"},
      {"stInsuranceTSIDesc","ins_tsi_cat_desc*n"},
      {"dbInsuredAmount","insured_amount"},
      {"stInsurancePolicyObjectID","ins_pol_obj_id"},
      {"dbInsuredAmountRef","insured_amount_ref"},
      {"stVoidFlag","void_flag"},
      {"stAutoFlag","auto_flag"},
      {"stDefaultTSIFlag","default_tsi_flag"},
      {"stManualTSILockFlag","manual_tsi_lock_flag"},
      {"stInsuranceTSIPolTypeID","ins_tcpt_id"},
      {"stTSICategoryDescription","tsi_cat_desc"},
      {"dbInsuredAmountFull","insured_amount_full"},


   };

   /*ALTER TABLE ins_pol_tsi ADD COLUMN default_tsi_flag varchar(1);
ALTER TABLE ins_pol_tsi ADD COLUMN manual_tsi_lock_flag varchar(1);
*/

   private String stDefaultTSIFlag;
   private String stManualTSILockFlag;
   private String stInsuranceTSIPolTypeID;
   private InsuranceTSIPolTypeView insuranceTSIPolType;
   private String stExcRiskFlag;
   private String stTSICategoryDescription;
   
   public String getStTSIExcluded() {
      final InsuranceTSIView tsi2 = getInsuranceTSI();
   	  
      String flag = "";
      if(tsi2.getStExcTSIFlag()==null) flag = "";
      else if(tsi2.getStExcTSIFlag().equalsIgnoreCase("Y")) flag = "Y";
      
      return flag;
   }
   
   public String getStExcRiskFlag() {
      return stExcRiskFlag;
   }

   public void setStExcRiskFlag(String stExcRiskFlag) {
      this.stExcRiskFlag = stExcRiskFlag;
   }

   public String getStInsuranceTSIPolTypeID() {
      return stInsuranceTSIPolTypeID;
   }

   public void setStInsuranceTSIPolTypeID(String stInsuranceTSIPolTypeID) {
      this.stInsuranceTSIPolTypeID = stInsuranceTSIPolTypeID;
   }

   public String getStDefaultTSIFlag() {
      return stDefaultTSIFlag;
   }

   public void setStDefaultTSIFlag(String stDefaultTSIFlag) {
      this.stDefaultTSIFlag = stDefaultTSIFlag;
   }

   public String getStManualTSILockFlag() {
      return stManualTSILockFlag;
   }

   public void setStManualTSILockFlag(String stManualTSILockFlag) {
      this.stManualTSILockFlag = stManualTSILockFlag;
   }

   public String getStAutoFlag() {
      return stAutoFlag;
   }

   public void setStAutoFlag(String stAutoFlag) {
      this.stAutoFlag = stAutoFlag;
   }

   public String getStInsuranceTSIDesc2() {

      String q = getStInsuranceTSIDesc();

      if (q==null) q="";

      if (isVoid()) q+=" (VOID)";

      return q;
   }

   public String getStInsuranceTSIDesc() {
      //stInsuranceTSIDesc = LanguageManager.getInstance().translate(getInsuranceTSI().getStDescription(), "ENG") +" / "+ LanguageManager.getInstance().translate(getInsuranceTSI().getStDescription(), "INA");

      stInsuranceTSIDesc = getInsuranceTSI().getStDescription();
      return stInsuranceTSIDesc;
   }

   public InsuranceTSIView getInsuranceTSI() {
      return (InsuranceTSIView) DTOPool.getInstance().getDTO(InsuranceTSIView.class, getStInsuranceTSIID());
   }

   public void setStInsuranceTSIDesc(String stInsuranceTSIDesc) {
      this.stInsuranceTSIDesc = stInsuranceTSIDesc;
   }

   public String getStInsurancePolicyTSIID() {
      return stInsurancePolicyTSIID;
   }

   public void setStInsurancePolicyTSIID(String stInsurancePolicyTSIID) {
      this.stInsurancePolicyTSIID = stInsurancePolicyTSIID;
   }

   public String getStDescriptionAuto() {
      return getStDescription()==null?getStInsuranceTSIDesc():getStDescription(); 
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStInsuranceTSIID() {

      if (stInsuranceTSIID==null) {
         stInsuranceTSIID = getInsuranceTSIPolTypeView().getStInsuranceTSICategoryID();
      }

      return stInsuranceTSIID;
   }

   private InsuranceTSIPolTypeView getInsuranceTSIPolTypeView() {

      if (insuranceTSIPolType==null)
         insuranceTSIPolType = (InsuranceTSIPolTypeView) DTOPool.getInstance().getDTO(InsuranceTSIPolTypeView.class, stInsuranceTSIPolTypeID);

      return insuranceTSIPolType;
   }

   public void setStInsuranceTSIID(String stInsuranceTSIID) {
      this.stInsuranceTSIID = stInsuranceTSIID;
   }

   public BigDecimal getDbInsuredAmount() {
      return dbInsuredAmount;
   }

   public void setDbInsuredAmount(BigDecimal dbInsuredAmount) {
      this.dbInsuredAmount = dbInsuredAmount;
   }

   public String getStInsurancePolicyObjectID() {
      return stInsurancePolicyObjectID;
   }

   public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
      this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
   }

   public BigDecimal getDbInsuredAmountRef() {
      return dbInsuredAmountRef;
   }

   public void setDbInsuredAmountRef(BigDecimal dbInsuredAmountRef) {
      this.dbInsuredAmountRef = dbInsuredAmountRef;
   }

   public String getStInsurancePolicyTSIRefID() {
      return stInsurancePolicyTSIRefID;
   }

   public void setStInsurancePolicyTSIRefID(String stInsurancePolicyTSIRefID) {
      this.stInsurancePolicyTSIRefID = stInsurancePolicyTSIRefID;
   }

   public void doVoid() {
      if (stInsurancePolicyTSIRefID==null) return;

      if(isVoid()) return;

      stVoidFlag = "Y";
      //dbInsuredAmount = BDUtil.sub(BDUtil.zero, dbInsuredAmount);
      dbInsuredAmount = BDUtil.zero;
   }

   public boolean isVoid() {
      return Tools.isYes(stVoidFlag);
   }

   public String getStVoidFlag() {
      return stVoidFlag;
   }

   public void setStVoidFlag(String stVoidFlag) {
      this.stVoidFlag = stVoidFlag;
   }

   public boolean hasRef() {
      return stInsurancePolicyTSIRefID != null;
   }

   public void initializeDefaults() {
      getInsuranceTSIPolTypeView();

      if (insuranceTSIPolType.getStManualTSIFlag()!=null)
         setStAutoFlag(Tools.isYes(insuranceTSIPolType.getStManualTSIFlag())?"N":"Y");

      setStManualTSILockFlag(insuranceTSIPolType.getStManualTSILockFlag());

      setStDefaultTSIFlag(insuranceTSIPolType.getStDefaultTSIFlag());
   }

    /**
     * @return the stTSICategoryDescription
     */
    public String getStTSICategoryDescription() {
        return stTSICategoryDescription;
    }

    /**
     * @param stTSICategoryDescription the stTSICategoryDescription to set
     */
    public void setStTSICategoryDescription(String stTSICategoryDescription) {
        this.stTSICategoryDescription = stTSICategoryDescription;
    }

    /**
     * @return the dbInsuredAmountFull
     */
    public BigDecimal getDbInsuredAmountFull() {
        return dbInsuredAmountFull;
    }

    /**
     * @param dbInsuredAmountFull the dbInsuredAmountFull to set
     */
    public void setDbInsuredAmountFull(BigDecimal dbInsuredAmountFull) {
        this.dbInsuredAmountFull = dbInsuredAmountFull;
    }

    public InsurancePolicyTSIView getRefTSI() {
      return (InsurancePolicyTSIView) DTOPool.getInstance().getDTO(InsurancePolicyTSIView.class, stInsurancePolicyTSIRefID);
   }
}

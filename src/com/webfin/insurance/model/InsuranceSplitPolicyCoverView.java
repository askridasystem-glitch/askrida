/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyCoverView
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:41:22 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.webfin.FinCodec;

import java.math.BigDecimal;

public class InsuranceSplitPolicyCoverView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_cover
(
  ins_pol_cover_id int8 NOT NULL,
  pol_id int8,
  ins_cover_id int8,
  insured_amount numeric,
  rate numeric,
  premi numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_cover_pk PRIMARY KEY (ins_pol_cover_id)
)
   */

   public static String tableName = "ins_split_pol_cover";

   public static String fieldMap[][] = {
      {"stInsurancePolicyCoverID", "ins_pol_cover_id*pk*nd"},
      {"stInsurancePolicyCoverRefID", "ins_pol_cover_ref_id"},
      {"stPolicyID", "pol_id"},
      {"stInsuranceCoverID", "ins_cover_id"},
      {"stInsuranceCoverDesc", "ins_cover_desc*n"},
      {"dbInsuredAmount", "insured_amount"},
      {"dbRate", "rate"},
      {"dbPremi", "premi"},
      {"stEntryMode", "entry_mode"},
      {"stEntryInsuredAmountFlag", "f_entry_insamt"},
      {"stEntryRateFlag", "f_entry_rate"},
      {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
      {"stCoverCategory", "cover_category"},
      {"stInsuranceCoverPolTypeID", "ins_cvpt_id"},
      {"stVoidFlag", "void_flag"},
      {"dbPremiNew", "premi_new"},
      {"stCalculationDesc", "calculation_desc"},
      {"stAutoRateFlag", "f_autorate"},
      {"stDefaultCoverFlag", "default_cover_flag"},
      {"stRateScale", "rate_scale"},
      {"stZoneID", "zone_id"},
      {"stDescription", "description"},
      {"stEntryPremiFlag", "f_entry_premi"},
      {"dbPremiNot", "premi_not*n"},
      {"dbPremiTotal", "premi_tot*n"},
      {"stDepreciationYear", "depreciation_year"},
      {"dbDepreciationPct", "depreciation_pct"},
	  
   };

   /*default_cover_flag*/

   private String stInsurancePolicyCoverID;
   private String stInsurancePolicyCoverRefID;
   private String stPolicyID;
   private String stRateScale;
   private String stVoidFlag;
   private String stInsuranceCoverID;
   private String stInsuranceCoverDesc;
   private String stEntryMode;
   private String stEntryInsuredAmountFlag;
   private String stEntryRateFlag;
   private String stInsurancePolicyObjectID;
   private String stCoverCategory;
   private String stCalculationDesc;
   private String stInsuranceCoverPolTypeID;
   private String stAutoRateFlag;
   private String stDefaultCoverFlag;
   private BigDecimal dbInsuredAmount;
   private BigDecimal dbRate;
   private BigDecimal dbPremi;
   private BigDecimal dbPremiNew;
   private InsuranceCoverPolTypeView coverPolType;
   private String stZoneID;
   private String stDescription;
   private BigDecimal dbPremiNot;
   private BigDecimal dbPremiTotal;
   private String stEntryPremiFlag;
   private String stDepreciationYear;
   private BigDecimal dbDepreciationPct;
   
   public BigDecimal getDbPremiNot() {
      return dbPremiNot;
   }

   public void setDbPremiNot(BigDecimal dbPremiNot) {
      this.dbPremiNot = dbPremiNot;
   }
   
   public BigDecimal getDbPremiTotal() {
      return dbPremiTotal;
   }

   public void setDbPremiTotal(BigDecimal dbPremiTotal) {
      this.dbPremiTotal = dbPremiTotal;
   }

   public String getStDefaultCoverFlag() {
      return stDefaultCoverFlag;
   }

   public void setStDefaultCoverFlag(String stDefaultCoverFlag) {
      this.stDefaultCoverFlag = stDefaultCoverFlag;
   }
   
   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStAutoRateFlag() {
      return stAutoRateFlag;
   }

   public void setStAutoRateFlag(String stAutoRateFlag) {
      this.stAutoRateFlag = stAutoRateFlag;
   }

   public String getStCalculationDesc() {
      return stCalculationDesc;
   }

   public void setStCalculationDesc(String stCalculationDesc) {
      this.stCalculationDesc = stCalculationDesc;
   }

   public BigDecimal getDbPremiNew() {
      return dbPremiNew;
   }

   public void setDbPremiNew(BigDecimal dbPremiNew) {
      this.dbPremiNew = dbPremiNew;
   }

   public BigDecimal getDbPrevPremiAct() {
      final InsuranceSplitPolicyCoverView rc = getRefCover();

      if (rc==null) return null;

      return rc.getDbPremi();
   }

   public BigDecimal getDbPrevPremi() {
      final InsuranceSplitPolicyCoverView rc = getRefCover();

      if (rc==null) return null;

      if (!isEntryRate()) return null;

      final BigDecimal newPremi = BDUtil.mul(getDbRatePct(), getDbInsuredAmount());

      return BDUtil.sub(newPremi, rc.getDbPremi());
   }

   public InsuranceSplitPolicyCoverView getRefCover() {
      return (InsuranceSplitPolicyCoverView) DTOPool.getInstance().getDTO(InsuranceSplitPolicyCoverView.class, stInsurancePolicyCoverRefID);
   }

   public void setDbPrevPremi(String dbPrevPremi) {
   }

   public String getStEntryMode() {
      return stEntryMode;
   }

   public void setStEntryMode(String stEntryMode) {
      this.stEntryMode = stEntryMode;
   }

   public String getStInsuranceCoverDesc2() {
      String q = getStInsuranceCoverDesc();

      if (q==null) q="";

      if (isVoid()) q+=" (VOID)";

      return q;
   }

   public String getStInsuranceCoverDesc() {
      stInsuranceCoverDesc = getInsuranceCoverage().getStDescription();

      return stInsuranceCoverDesc;
   }

   public void setStInsuranceCoverDesc(String stInsuranceCoverDesc) {
      this.stInsuranceCoverDesc = stInsuranceCoverDesc;
   }

   public String getStInsurancePolicyCoverID() {
      return stInsurancePolicyCoverID;
   }

   public void setStInsurancePolicyCoverID(String stInsurancePolicyCoverID) {
      this.stInsurancePolicyCoverID = stInsurancePolicyCoverID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStInsuranceCoverID() {
      return stInsuranceCoverID;
   }

   public void setStInsuranceCoverID(String stInsuranceCoverID) {
      this.stInsuranceCoverID = stInsuranceCoverID;
   }

   public BigDecimal getDbInsuredAmount() {
      return dbInsuredAmount;
   }

   public void setDbInsuredAmount(BigDecimal dbInsuredAmount) {
      this.dbInsuredAmount = dbInsuredAmount;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public BigDecimal getDbPremi() {
      return dbPremi;
   }

   public void setDbPremi(BigDecimal dbPremi) {
      this.dbPremi = dbPremi;
   }

   public boolean isMainCover() {
      return FinCodec.CoverCategory.MAIN.equalsIgnoreCase(stCoverCategory);
//      final InsuranceCoverView icv = getInsuranceCoverage();
//      return icv==null?false:icv.isMainCover();
   }

   public InsuranceCoverView getInsuranceCoverage() {
      return (InsuranceCoverView) DTOPool.getInstance().getDTO(InsuranceCoverView.class, stInsuranceCoverID);
   }

   public InsuranceCoverPolTypeView getInsuranceCoveragePolType() {
      return (InsuranceCoverPolTypeView) DTOPool.getInstance().getDTO(InsuranceCoverPolTypeView.class, stInsuranceCoverPolTypeID);
   }

   public String getStEntryInsuredAmountFlag() {
      return stEntryInsuredAmountFlag;
   }

   public void setStEntryInsuredAmountFlag(String stEntryInsuredAmountFlag) {
      this.stEntryInsuredAmountFlag = stEntryInsuredAmountFlag;
   }

   public String getStEntryRateFlag() {
      return stEntryRateFlag;
   }

   public void setStEntryRateFlag(String stEntryRateFlag) {
      this.stEntryRateFlag = stEntryRateFlag;
   }

   public boolean isEntryInsuredAmount() {
      return Tools.isYes(getStEntryInsuredAmountFlag());
   }
   
   public boolean isEntryPremi() {
      return Tools.isYes(getStEntryPremiFlag());
   }

   public boolean isEntryRate() {
      return Tools.isYes(getStEntryRateFlag());
   }

   public String getStInsurancePolicyObjectID() {
      return stInsurancePolicyObjectID;
   }

   public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
      this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
   }

   public BigDecimal getDbRatePct() {
      return dbRate==null?null:BDUtil.div(dbRate, BDUtil.hundred,10);
   }

   public void setDbRatePct(BigDecimal r) {
      dbRate = r==null?null:BDUtil.mul(r, BDUtil.hundred);
   }
   
   public BigDecimal getDbRateMile() {
      return dbRate==null?null:BDUtil.div(dbRate, BDUtil.thousand,10);
   }

   public void setDbRateMile(BigDecimal r) {
      dbRate = r==null?null:BDUtil.mul(r, BDUtil.thousand);
   }

   public String getStCoverCategory() {
      return stCoverCategory;
   }

   public void setStCoverCategory(String stCoverCategory) {
      this.stCoverCategory = stCoverCategory;
   }

   public String getStInsuranceCoverPolTypeID() {
      return stInsuranceCoverPolTypeID;
   }

   public void setStInsuranceCoverPolTypeID(String stInsuranceCoverPolTypeID) {
      this.stInsuranceCoverPolTypeID = stInsuranceCoverPolTypeID;
   }

   public String getStInsurancePolicyCoverRefID() {
      return stInsurancePolicyCoverRefID;
   }

   public void setStInsurancePolicyCoverRefID(String stInsurancePolicyCoverRefID) {
      this.stInsurancePolicyCoverRefID = stInsurancePolicyCoverRefID;
   }

   public String getStVoidFlag() {
      return stVoidFlag;
   }

   public void setStVoidFlag(String stVoidFlag) {
      this.stVoidFlag = stVoidFlag;
   }

   public void doVoid() {
      if (stInsurancePolicyCoverRefID==null) return;

      if (!getRefCover().isEntryRate()) throw new RuntimeException("Unable to automatically calculate cover voidance amount because premium entered manually");

      if (isVoid()) return;
      stVoidFlag="Y";
      stEntryRateFlag="Y";
      //dbPremi = BDUtil.sub(BDUtil.zero, getDbPrevPremiAct());
      dbRate=getRefCover().getDbRate();
      dbInsuredAmount = BDUtil.zero;
      stEntryInsuredAmountFlag="Y";
   }

   public boolean isVoid() {
      return Tools.isYes(stVoidFlag);
   }

   public boolean hasRef() {
      return stInsurancePolicyCoverRefID!=null;
   }

   public void initializeDefaults() {
      getCoverPolType();

      if (coverPolType.getStUseRateFlag()!=null) setStEntryRateFlag(coverPolType.getStUseRateFlag());
      if (coverPolType.getStManualTSIFlag()!=null) setStEntryInsuredAmountFlag(coverPolType.getStManualTSIFlag());
      if (coverPolType.getStAutoRateFlag()!=null) setStAutoRateFlag(coverPolType.getStAutoRateFlag());

      setStDefaultCoverFlag(coverPolType.getStDefaultCoverFlag());
   }

   public InsuranceCoverPolTypeView getCoverPolType() {
      if (coverPolType != null) return coverPolType;
      coverPolType = (InsuranceCoverPolTypeView) DTOPool.getInstance().getDTO(InsuranceCoverPolTypeView.class, stInsuranceCoverPolTypeID);

      return coverPolType;
   }

   public boolean isAutoRate() {
      return Tools.isYes(stAutoRateFlag);
   }

   public String getStRateScale() {
      if (stRateScale==null) stRateScale="%";
      return stRateScale;
   }

   public void setStRateScale(String stRateScale) {
      this.stRateScale = stRateScale;
   }

   public String getStRateScaleSymbol() {
      return (String) FinCodec.RateScale.getLookUp().getDescription(getStRateScale());
   }
   
   public String getStZoneID() {
      return stZoneID;
   }

   public void setStZoneID(String stZoneID) {
      this.stZoneID = stZoneID;
   }
   
   public String getStEntryPremiFlag() {
      return stEntryPremiFlag;
   }

   public void setStEntryPremiFlag(String stEntryPremiFlag) {
      this.stEntryPremiFlag = stEntryPremiFlag;
   }
   
   public String getStInsCoverDesc() {
      stInsuranceCoverDesc = getInsuranceCoverage().getStShortDesc();

      return stInsuranceCoverDesc;
   }

   public void setStInsCoverDesc(String stInsuranceCoverDesc) {
      this.stInsuranceCoverDesc = stInsuranceCoverDesc;
   }
   
   public boolean isFlag() {
        boolean isflag = false;

        if(getInsuranceCoverage()!=null)
                if(getInsuranceCoverage().getStFlag()!=null)
                        if(getInsuranceCoverage().getStFlag().equalsIgnoreCase("Y")) isflag = true;
   		
        return isflag; 
    }

    /**
     * @return the stDepreciationYear
     */
    public String getStDepreciationYear() {
        return stDepreciationYear;
    }

    /**
     * @param stDepreciationYear the stDepreciationYear to set
     */
    public void setStDepreciationYear(String stDepreciationYear) {
        this.stDepreciationYear = stDepreciationYear;
    }

    /**
     * @return the dbDepreciationPct
     */
    public BigDecimal getDbDepreciationPct() {
        return dbDepreciationPct;
    }

    /**
     * @param dbDepreciationPct the dbDepreciationPct to set
     */
    public void setDbDepreciationPct(BigDecimal dbDepreciationPct) {
        this.dbDepreciationPct = dbDepreciationPct;
    }
}

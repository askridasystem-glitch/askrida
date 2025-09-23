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
import com.crux.util.DTOList;
import com.webfin.entity.model.EntityView;

import java.math.BigDecimal;

public class InsurancePolicyCoverReinsView extends DTO implements RecordAudit {
   /*
CREATE TABLE ins_pol_cover_ri
(
  ins_pol_cover_ri_id bigint NOT NULL,
  ins_pol_ri_id bigint,
  ins_treaty_shares_id bigint,
  member_ent_id bigint,
  ins_pol_cover_id bigint,
  pol_id bigint,
   ins_pol_treaty_id bigint,
  ins_pol_tre_det_id bigint,
  ins_treaty_detail_id bigint,
  ins_cover_id bigint,
  insured_amount numeric,
  rate numeric,
  premi numeric,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  entry_mode character varying(10),
  ins_pol_obj_id bigint,
  f_entry_insamt character varying(1),
  f_entry_rate character varying(1),
  cover_category character varying(16),
  ins_cpvt_id bigint,
  ins_pol_cover_ref_id numeric,
  void_flag character varying(1),
  premi_new numeric,
  calculation_desc character varying(255),
  f_autorate character varying(1),
  default_cover_flag character varying(1),
  rate_scale character varying(1)
)
WITH OIDS;
ALTER TABLE ins_pol_cover_ri OWNER TO postgres;
   */

   public static String tableName = "ins_pol_cover_ri";

   public static String fieldMap[][] = {
   	  {"stInsurancePolicyCoverReinsID", "ins_pol_cover_ri_id*pk*nd"}, 
   	  {"stInsurancePolicyReinsID","ins_pol_ri_id"},
   	  {"stInsuranceTreatySharesID","ins_treaty_shares_id"},
      {"stMemberEntityID","member_ent_id"},  
      {"stInsurancePolicyCoverID", "ins_pol_cover_id"},
      {"stInsurancePolicyCoverRefID", "ins_pol_cover_ref_id"},
      {"stPolicyID", "pol_id"},
      {"stInsurancePolicyTreatyID","ins_pol_treaty_id"},
      {"stInsurancePolicyTreatyDetailID","ins_pol_tre_det_id"},
      {"stInsuranceTreatyDetailID","ins_treaty_detail_id"},
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
      {"dbSharePct","share_pct"},
      {"dbCommissionRate","comm_rate"},
      {"dbCommissionAmount","comm_amount"},
   };

   /*default_cover_flag*/

   private String stInsurancePolicyCoverReinsID;
   private String stInsurancePolicyReinsID;
   private String stInsuranceTreatySharesID;
   private String stMemberEntityID;
   private String stInsurancePolicyCoverID;
   private String stInsurancePolicyCoverRefID;
   private String stPolicyID;
   private String stInsurancePolicyTreatyID;
   private String stInsurancePolicyTreatyDetailID;
   private String stInsuranceTreatyDetailID;
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
   private DTOList coverage;
   private BigDecimal dbSharePct;
   private BigDecimal dbCommissionRate;
   private BigDecimal dbCommissionAmount;
   
    public String getStInsurancePolicyReinsID() {
      return stInsurancePolicyReinsID;
   }

   public void setStInsurancePolicyReinsID(String stInsurancePolicyReinsID) {
      this.stInsurancePolicyReinsID = stInsurancePolicyReinsID;
   }

   public String getStInsuranceTreatySharesID() {
      return stInsuranceTreatySharesID;
   }

   public void setStInsuranceTreatySharesID(String stInsuranceTreatySharesID) {
      this.stInsuranceTreatySharesID = stInsuranceTreatySharesID;
   }

   public String getStMemberEntityID() {
      return stMemberEntityID;
   }

   public void setStMemberEntityID(String stMemberEntityID) {
      this.stMemberEntityID = stMemberEntityID;
   }
   
   public String getStInsurancePolicyTreatyID() {
      return stInsurancePolicyTreatyID;
   }

   public void setStInsurancePolicyTreatyID(String stInsurancePolicyTreatyID) {
      this.stInsurancePolicyTreatyID = stInsurancePolicyTreatyID;
   }
   
   public String getStInsurancePolicyTreatyDetailID() {
      return stInsurancePolicyTreatyDetailID;
   }

   public void setStInsurancePolicyTreatyDetailID(String stInsurancePolicyTreatyDetailID) {
      this.stInsurancePolicyTreatyDetailID = stInsurancePolicyTreatyDetailID;
   }
   
   public String getStInsuranceTreatyDetailID() {
      return stInsuranceTreatyDetailID;
   }

   public void setStInsuranceTreatyDetailID(String stInsuranceTreatyDetailID) {
      this.stInsuranceTreatyDetailID = stInsuranceTreatyDetailID;
   }

   public String getStDefaultCoverFlag() {
      return stDefaultCoverFlag;
   }

   public void setStDefaultCoverFlag(String stDefaultCoverFlag) {
      this.stDefaultCoverFlag = stDefaultCoverFlag;
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
      final InsurancePolicyCoverReinsView rc = getRefCover();

      if (rc==null) return null;

      return rc.getDbPremi();
   }

   public BigDecimal getDbPrevPremi() {
      final InsurancePolicyCoverReinsView rc = getRefCover();

      if (rc==null) return null;

      if (!isEntryRate()) return null;

      final BigDecimal newPremi = BDUtil.mul(getDbRatePct(), getDbInsuredAmount());

      return BDUtil.sub(newPremi, rc.getDbPremi());
   }

   public InsurancePolicyCoverReinsView getRefCover() {
      return (InsurancePolicyCoverReinsView) DTOPool.getInstance().getDTO(InsurancePolicyCoverReinsView.class, stInsurancePolicyCoverRefID);
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
   
   public String getStInsurancePolicyCoverReinsID() {
      return stInsurancePolicyCoverReinsID;
   }

   public void setStInsurancePolicyCoverReinsID(String stInsurancePolicyCoverReinsID) {
      this.stInsurancePolicyCoverReinsID = stInsurancePolicyCoverReinsID;
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
      //dbInsuredAmount = BDUtil.zero;
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
   
   public EntityView getEntityName(String stEntID){
   		final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class,stEntID);
   		
   		return entity;
   }
   
   public BigDecimal getDbSharePct() {
      return dbSharePct;
   }

   public void setDbSharePct(BigDecimal dbSharePct) {
      this.dbSharePct = dbSharePct;
   }
   
   public BigDecimal getDbCommissionRate() {
      return dbCommissionRate;
   }

   public void setDbCommissionRate(BigDecimal dbCommissionRate) {
      this.dbCommissionRate = dbCommissionRate;
   }

   public BigDecimal getDbCommissionAmount() {
      return dbCommissionAmount;
   }

   public void setDbCommissionAmount(BigDecimal dbCommissionAmount) {
      this.dbCommissionAmount = dbCommissionAmount;
   }
}

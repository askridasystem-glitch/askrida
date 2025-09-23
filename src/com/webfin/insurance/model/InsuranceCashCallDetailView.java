/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTreatyDetailView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 12:48:52 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.ar.model.ARTransactionTypeView;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceCashCallDetailView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_treaty_detail
(
  ins_treaty_detail_id int NOT NULL,
  treaty_type  varchar(5),
  policy_type_id int8,
  treaty_limit numeric,
  rate_mindep_pct numeric,
  comm_ri_pct numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_detail_pk PRIMARY KEY (ins_treaty_detail_id)
) without oids;

ALTER TABLE ins_treaty_detail ADD COLUMN xol_lower numeric;
ALTER TABLE ins_treaty_detail ADD COLUMN xol_upper numeric;

   */

   public static String tableName = "ins_cashcall_detail";

   public static String fieldMap[][] = {
      {"stInsuranceTreatyDetailID","ins_cashcall_detail_id*pk"},
      {"stInsuranceTreatyID","ins_cashcall_id"},
      {"stTreatyTypeID","treaty_type"},
      {"stComboDesc","combo_desc*n"},
      {"stPolicyTypeID","policy_type_id"},
      {"dbTreatyLimit","treaty_limit"},
      {"dbPremiRatePct","premi_rate"},
      {"dbCommissionRatePct","comm_ri_pct"},
      {"dbQuotaSharePct","qs_pct"},
      {"stParentID","parent_id"},
      {"dbTSIPct","tsi_pct"},
      {"stARTrxLineID","ar_trx_line_id"},
      {"stARTrxLineIDClaim","ar_trx_line_id_clm"},
      {"dbXOLLower","xol_lower"},
      {"dbXOLUpper","xol_upper"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"dbTSIMax","tsi_max"},
      {"dbCommissionRateCaptivePct","comm_ri_captive_pct"},
      {"stDivideByYearsFlags","divide_by_years_flag"},
      {"stInsuranceCoverID", "ins_cover_id"},
      {"stPremiumCommFactorFlags","premium_comm_factor_flag"},
      {"dbPremiumCommFactorLimit","premium_comm_factor_limit"},
      {"stDeleteFlag","delete_flag"},
      {"dbInwardCapacityPct","inward_capacity_pct"},
      {"stBypassValidationRI","bypass_validate_ri"},

   };

   public static String comboFields[] = {"index","combo_desc"};

   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private String stInsuranceTreatyDetailID;
   private String stInsuranceTreatyID;
   private String stTreatyTypeID;
   private String stPolicyTypeID;
   private String stParentID;
   private String stARTrxLineID;
   private String stARTrxLineIDClaim;
   private BigDecimal dbXOLLower;
   private BigDecimal dbXOLUpper;
   private BigDecimal dbTSIPct;
   private BigDecimal dbTreatyLimit;
   private BigDecimal dbPremiRatePct;
   private BigDecimal dbCommissionRatePct;
   private BigDecimal dbQuotaSharePct;
   private InsuranceTreatyTypesView treatyType;
   private DTOList shares;
   private InsuranceCashCallDetailView parent;
   private DTOList owningList;
   private BigDecimal dbTSIMax;
   private BigDecimal dbCommissionRateCaptivePct;
   private String stDivideByYearsFlags;
   private String stInsuranceCoverID;
   private String stPremiumCommFactorFlags;
   private BigDecimal dbPremiumCommFactorLimit;
   private String stDeleteFlag;
   private BigDecimal dbInwardCapacityPct;
   private String stBypassValidationRI;
   
  
   public BigDecimal getDbTSIMax() {
      return dbTSIMax;
   }

   public void setDbTSIMax(BigDecimal dbTSIMax) {
      this.dbTSIMax = dbTSIMax;
   }

   public void setStComboDesc(String stComboDesc) {
   }

   public InsuranceCashCallDetailView getParent() {
      return parent;
   }

   public void setParent(InsuranceCashCallDetailView parent) {
      this.parent = parent;
   }

   public DTOList getOwningList() {
      return owningList;
   }

   public void setOwningList(DTOList owningList) {
      this.owningList = owningList;
   }

   public String getStParentIndex() {
      if (parent==null) return null;
      return String.valueOf(owningList.indexOf(parent));
   }

   public void setStParentIndex(String stParentIndex) {

      if (stParentIndex == null) {
         parent=null;
         stParentID=null;
         return;
      }

      InsuranceCashCallDetailView np = (InsuranceCashCallDetailView) owningList.get(Integer.parseInt(stParentIndex));
      parent=np;
   }

   public String getStComboDesc() {
      return stTreatyTypeID;
   }

   public String getStARTrxLineID() {
      return stARTrxLineID;
   }

   public void setStARTrxLineID(String stARTrxLineID) {
      this.stARTrxLineID = stARTrxLineID;
   }

   public BigDecimal getDbTSIPct() {
      return dbTSIPct;
   }

   public void setDbTSIPct(BigDecimal dbTSIPct) {
      this.dbTSIPct = dbTSIPct;
   }

   public String getStParentID() {

      if (parent!=null) stParentID = parent.getStInsuranceTreatyDetailID();

      return stParentID;
   }

   public void setStParentID(String stParentID) {
      this.stParentID = stParentID;
   }

   public BigDecimal getDbQuotaSharePct() {
      return dbQuotaSharePct;
   }

   public void setDbQuotaSharePct(BigDecimal dbQuotaSharePct) {
      this.dbQuotaSharePct = dbQuotaSharePct;
   }

   public DTOList getShares() {
      loadShares();
      return shares;
   }

   private void loadShares() {
      try {
         if (shares==null)
            shares = ListUtil.getDTOListFromQuery(
                    "select * from ins_treaty_shares where coalesce(delete_flag,'N') <> 'Y' and ins_treaty_detail_id = ?",
                    new Object [] {stInsuranceTreatyDetailID},
                    InsuranceTreatySharesView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

   }

   public void setShares(DTOList shares) {
      this.shares = shares;
   }

   public InsuranceTreatyTypesView getTreatyType() {
      if (treatyType == null) treatyType = (InsuranceTreatyTypesView) DTOPool.getInstance().getDTO(InsuranceTreatyTypesView.class, stTreatyTypeID);
      return treatyType;
   }

   public void setTreatyType(InsuranceTreatyTypesView treatyType) {
      this.treatyType = treatyType;
   }

   public String getStInsuranceTreatyDetailID() {
      return stInsuranceTreatyDetailID;
   }

   public void setStInsuranceTreatyDetailID(String stInsuranceTreatyDetailID) {
      this.stInsuranceTreatyDetailID = stInsuranceTreatyDetailID;
   }

   public String getStTreatyTypeID() {
      return stTreatyTypeID;
   }

   public void setStTreatyTypeID(String stTreatyTypeID) {
      this.stTreatyTypeID = stTreatyTypeID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public BigDecimal getDbTreatyLimit() {
      return dbTreatyLimit;
   }

   public void setDbTreatyLimit(BigDecimal dbTreatyLimit) {
      this.dbTreatyLimit = dbTreatyLimit;
   }

   public BigDecimal getDbPremiRatePct() {
      return dbPremiRatePct;
   }

   public void setDbPremiRatePct(BigDecimal dbPremiRatePct) {
      this.dbPremiRatePct = dbPremiRatePct;
   }

   public BigDecimal getDbCommissionRatePct() {
      return dbCommissionRatePct;
   }

   public void setDbCommissionRatePct(BigDecimal dbCommissionRatePct) {
      this.dbCommissionRatePct = dbCommissionRatePct;
   }

   public String getStInsuranceTreatyID() {
      return stInsuranceTreatyID;
   }

   public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
      this.stInsuranceTreatyID = stInsuranceTreatyID;
   }

   public boolean isOR() {
      return Tools.isEqual("OR", stTreatyTypeID);
   }

   public boolean isQS() {
      return Tools.isEqual("QS", stTreatyTypeID);
   }
   
   public boolean isQSKR() {
      return Tools.isEqual("QSKR", stTreatyTypeID);
   }
   
   public boolean isBPDAN() {
      return Tools.isEqual("BPDAN", stTreatyTypeID);
   }
   
   public boolean isMaipark() {
      return Tools.isEqual("PARK", stTreatyTypeID);
   }

   public boolean isFreeTSI() {
      getTreatyType();
      return treatyType!=null && treatyType.isFreeTSI();
   }
   
   public boolean isFacultative() {
      return Tools.isEqual("FAC", stTreatyTypeID);
   }

   public ARTransactionLineView getARTrxLine() {
      return (ARTransactionLineView) DTOPool.getInstance().getDTO(ARTransactionLineView.class, stARTrxLineID);
   }

   public BigDecimal getDbXOLLower() {
      return dbXOLLower;
   }

   public void setDbXOLLower(BigDecimal dbXOLLower) {
      this.dbXOLLower = dbXOLLower;
   }

   public BigDecimal getDbXOLUpper() {
      return dbXOLUpper;
   }

   public void setDbXOLUpper(BigDecimal dbXOLUpper) {
      this.dbXOLUpper = dbXOLUpper;
   }

   public BigDecimal getDbXOLMaxAmount() {
      return BDUtil.sub(getDbXOLUpper(), getDbXOLLower());
   }

   public String getStARTrxLineIDClaim() {
      return stARTrxLineIDClaim;
   }

   public void setStARTrxLineIDClaim(String stARTrxLineIDClaim) {
      this.stARTrxLineIDClaim = stARTrxLineIDClaim;
   }

   public ARTransactionLineView getARTrxLineClaim() {
      return (ARTransactionLineView) DTOPool.getInstance().getDTO(ARTransactionLineView.class, stARTrxLineIDClaim);
   }

   public Date getDtPeriodStart() {
      return dtPeriodStart;
   }

   public void setDtPeriodStart(Date dtPeriodStart) {
      this.dtPeriodStart = dtPeriodStart;
   }

   public Date getDtPeriodEnd() {
      return dtPeriodEnd;
   }

   public void setDtPeriodEnd(Date dtPeriodEnd) {
      this.dtPeriodEnd = dtPeriodEnd;
   }
   
   public BigDecimal getDbCommissionRateCaptivePct() {
      return dbCommissionRateCaptivePct;
   }

   public void setDbCommissionRateCaptivePct(BigDecimal dbCommissionRateCaptivePct) {
      this.dbCommissionRateCaptivePct = dbCommissionRateCaptivePct;
   }
   
   public String getStDivideByYearsFlags() {
      return stDivideByYearsFlags;
   }

   public void setStDivideByYearsFlags(String stDivideByYearsFlags) {
      this.stDivideByYearsFlags = stDivideByYearsFlags;
   }
   
   public boolean isDivideByYears() {
        return Tools.isYes(stDivideByYearsFlags);
   }
   
   public String getStInsuranceCoverID() {
      return stInsuranceCoverID;
   }

   public void setStInsuranceCoverID(String stInsuranceCoverID) {
      this.stInsuranceCoverID = stInsuranceCoverID;
   }
   
   public boolean isExcessOfLoss() {
      return stTreatyTypeID.startsWith("XOL");
      //return Tools.isEqual("XOL1", stTreatyTypeID);
   }
   
   public boolean isFacultativeObligatory1() {
      return Tools.isEqual("FACO", stTreatyTypeID);
   }
   
   public boolean isFacultativeObligatory3() {
      return Tools.isEqual("FACO3", stTreatyTypeID);
   }

    /**
     * @return the stPremiumCommFactorFlags
     */
    public String getStPremiumCommFactorFlags() {
        return stPremiumCommFactorFlags;
    }

    /**
     * @param stPremiumCommFactorFlags the stPremiumCommFactorFlags to set
     */
    public void setStPremiumCommFactorFlags(String stPremiumCommFactorFlags) {
        this.stPremiumCommFactorFlags = stPremiumCommFactorFlags;
    }

    public boolean isPremiumCommFactor(){
        return Tools.isYes(stPremiumCommFactorFlags);
    }

    /**
     * @return the dbPremiumCommFactorLimit
     */
    public BigDecimal getDbPremiumCommFactorLimit() {
        return dbPremiumCommFactorLimit;
    }

    /**
     * @param dbPremiumCommFactorLimit the dbPremiumCommFactorLimit to set
     */
    public void setDbPremiumCommFactorLimit(BigDecimal dbPremiumCommFactorLimit) {
        this.dbPremiumCommFactorLimit = dbPremiumCommFactorLimit;
    }

    /**
     * @return the stDeleteFlag
     */
    public String getStDeleteFlag() {
        return stDeleteFlag;
    }

    /**
     * @param stDeleteFlag the stDeleteFlag to set
     */
    public void setStDeleteFlag(String stDeleteFlag) {
        this.stDeleteFlag = stDeleteFlag;
    }

    /**
     * @return the dbInwardCapacityPct
     */
    public BigDecimal getDbInwardCapacityPct() {
        return dbInwardCapacityPct;
    }

    /**
     * @param dbInwardCapacityPct the dbInwardCapacityPct to set
     */
    public void setDbInwardCapacityPct(BigDecimal dbInwardCapacityPct) {
        this.dbInwardCapacityPct = dbInwardCapacityPct;
    }

    /**
     * @return the stBypassValidationRI
     */
    public String getStBypassValidationRI() {
        return stBypassValidationRI;
    }

    /**
     * @param stBypassValidationRI the stBypassValidationRI to set
     */
    public void setStBypassValidationRI(String stBypassValidationRI) {
        this.stBypassValidationRI = stBypassValidationRI;
    }

    public boolean isByPassValidationRI(){
        return Tools.isYes(stBypassValidationRI);
    }
   
}

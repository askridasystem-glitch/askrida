/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:05:00 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.controller.FormTab;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.RecordBackup;
import com.crux.common.parameter.Parameter;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.ff.model.FlexFieldDetailView;
import com.crux.lov.LOVManager;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.FinCodec;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.custom.CustomHandler;
import com.webfin.insurance.custom.CustomHandlerManager;
import com.webfin.insurance.ejb.InsurancePolicyUtil;
import com.webfin.system.region.model.RegionView;
import com.webfin.WebFinLOVRegistry;
import com.crux.lang.LanguageManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.sql.ResultSetMetaData;

public class CoverNoteView extends DTO implements RecordAudit, RecordBackup {
   /*
   CREATE TABLE ins_policy
(
  pol_id int8 NOT NULL,
  pol_no varchar(32),
  description varchar(255),
  ccy varchar(3),
  posted_flag varchar(1),
  CONSTRAINT ins_policy_pk PRIMARY KEY (pol_id)
)
WIT

ALTER TABLE ins_policy ADD COLUMN period_start timestamp;
ALTER TABLE ins_policy ADD COLUMN period_end timestamp;

ALTER TABLE ins_policy ADD COLUMN bus_source varchar(32);
ALTER TABLE ins_policy ADD COLUMN region_id int8;
ALTER TABLE ins_policy ADD COLUMN captive_flag varchar(1);
ALTER TABLE ins_policy ADD COLUMN inward_flag varchar(1);


ALTER TABLE ins_policy ADD COLUMN prod_name varchar(255);
ALTER TABLE ins_policy ADD COLUMN prod_address varchar(255);
ALTER TABLE ins_policy ADD COLUMN prod_id int8;
ALTER TABLE ins_policy ADD COLUMN sppa_no varchar(128);
ALTER TABLE ins_policy ADD COLUMN ins_period_base_id int8;
ALTER TABLE ins_policy ADD COLUMN period_rate_before numeric;


   */

   public static String tableName = "ins_policy";

   public static String fieldMap[][] = {
      {"stPolicyID","pol_id*pk"},
      {"stSPPANo","sppa_no"},
      {"stPrintCode","print_code"},
      {"stParentID","parent_id"},
      {"stPolicyNo","pol_no"},
      {"stDescription","description"},
      {"stCurrencyCode","ccy"},
      {"stPostedFlag","posted_flag"},
      {"dbAmount","amount"},
      {"stPolicyTypeID","pol_type_id"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"stPolicySubTypeID","pol_subtype_id"},
      {"stPeriodBaseID","ins_period_base_id"},
      {"stPolicyTypeDesc","policy_type_desc*n"},
      {"stPeriodBaseBeforeID","ins_period_base_b4"},
      {"dbPremiBase","premi_base"},
      {"dbPremiTotal","premi_total"},
      {"dbPremiTotalAfterDisc","premi_total_adisc"},
      {"dbTotalDue","total_due"},
      {"dbTotalFee","total_fee"},
      {"dbPremiRate","premi_rate"},
      {"dbPremiNetto","premi_netto"},
      {"dbInsuredAmount","insured_amount"},
      {"dbInsuredAmountEndorse","insured_amount_e"},
      {"dtPolicyDate","policy_date"},
      {"stBusinessSourceCode","bus_source"},
      {"stRegionID","region_id"},
      {"stCaptiveFlag","captive_flag"},
      {"stInwardFlag","inward_flag"},
      {"dbCurrencyRate","ccy_rate"},
      {"stCostCenterCode","cc_code"},
      {"stEntityID","entity_id"},
      {"stConditionID","condition_id"},
      {"stRiskCategoryID","risk_category_id"},
      {"stCoverTypeCode","cover_type_code"},
      {"stProductionOnlyFlag","f_prodmode"},
      {"stCustomerName","cust_name"},
      {"stProducerName","prod_name"},
      {"stCustomerAddress","cust_address"},
      {"stProducerAddress","prod_address"},
      {"stProducerID","prod_id"},
      {"stMasterPolicyID","master_policy_id"},
      {"stPolicyTypeGroupID","ins_policy_type_grp_id"},
      {"stInsurancePeriodID","ins_period_id"},
      {"stInstallmentPeriodID","inst_period_id"},
      {"lgInstallmentPeriods","inst_periods"},
      {"dbPeriodRate","period_rate"},
      {"dbPeriodRateBefore","period_rate_before"},
      {"stStatus","status"},
      {"stActiveFlag","active_flag"},
      {"dtPremiPayDate","premi_pay_date"},

      {"stReference1","ref1"},
      {"stReference2","ref2"},
      {"stReference3","ref3"},
      {"stReference4","ref4"},
      {"stReference5","ref5"},
      {"stReference6","ref6"},
      {"stReference7","ref7"},
      {"stReference8","ref8"},
      {"stReference9","ref9"},
      {"stReference10","ref10"},
      {"stReference11","ref11"},
      {"stReference12","ref12"},

      {"dtReference1","refd1"},
      {"dtReference2","refd2"},
      {"dtReference3","refd3"},
      {"dtReference4","refd4"},
      {"dtReference5","refd5"},

      {"dbReference1","refn1"},
      {"dbReference2","refn2"},
      {"dbReference3","refn3"},
      {"dbReference4","refn4"},
      {"dbReference5","refn5"},

      {"stClaimObjectID","claim_object_id"},
      {"stClaimNo","claim_no"},
      {"dtClaimDate","claim_date"},
      {"stClaimCauseID","claim_cause"},
      {"stClaimCauseDesc","claim_cause_desc"},
      {"stClaimEventLocation","event_location"},
      {"stClaimPersonID","claim_person_id"},
      {"stClaimPersonName","claim_person_name"},
      {"stClaimPersonAddress","claim_person_address"},
      {"stClaimPersonStatus","claim_person_status"},
      {"dbClaimAmountEstimate","claim_amount_est"},
      {"stClaimCurrency","claim_currency"},
      {"stClaimLossStatus","claim_loss_status"},
      {"stClaimBenefit","claim_benefit"},
      {"stClaimDocuments","claim_documents"},
      {"dbClaimDeductionAmount","claim_ded_amount"},
      {"dbClaimAmount","claim_amount"},
      {"dbClaimREAmount","claim_ri_amount"},
      {"dbClaimCustAmount","claim_cust_amount"},
      {"dbClaimDeductionCustAmount","claim_cust_ded_amount"},
      {"dbClaimAmountApproved","claim_amount_approved"},
      {"stDLARemark","dla_remark"},

      {"dtEndorseDate","endorse_date"},
      {"stEndorseNotes","endorse_notes"},
      {"stEffectiveFlag","effective_flag"},
      {"stClaimStatus","claim_status"},
      {"stRootID","root_id"},
      {"stPremiumFactorID","ins_premium_factor_id"},
      {"stDLANo","dla_no"},
      {"stPLANo","pla_no"},
      {"stInsuranceTreatyID","ins_treaty_id"},
      {"stNDUpdate","nd_update"},

      {"dbNDComm1","nd_comm1"},
      {"dbNDComm2","nd_comm2"},
      {"dbNDComm3","nd_comm3"},
      {"dbNDComm4","nd_comm4"},
      {"dbNDBrok1","nd_brok1"},
      {"dbNDBrok1Pct","nd_brok1pct"},
      {"dbNDBrok2","nd_brok2"},
      {"dbNDBrok2Pct","nd_brok2pct"},
      {"dbNDHFee","nd_hfee"},
      {"dbNDHFeePct","nd_hfeepct"},
      {"dbNDSFee","nd_sfee"},
      {"dbNDPCost","nd_pcost"},

      {"dbPremiPaid","premi_paid*n"},
      {"dbAPComis","ap_comis*n"},
      {"dbAPComisP","ap_comis_p*n"},
      {"dbAPBrok","ap_brok*n"},
      {"dbAPBrokP","ap_brok_p*n"},
      {"dbNDPremi1","nd_premi1"},
      {"dbNDPremi2","nd_premi2"},
      {"dbNDPremi3","nd_premi3"},
      {"dbNDPremi4","nd_premi4"},
      {"dbNDPremiRate1","nd_premirate1"},
      {"dbNDPremiRate2","nd_premirate2"},
      {"dbNDPremiRate3","nd_premirate3"},
      {"dbNDPremiRate4","nd_premirate4"},
      {"dbNDDisc1","nd_disc1"},
      {"dbNDDisc2","nd_disc2"},
      {"dbNDDisc1Pct","nd_disc1pct"},
      {"dbNDDisc2Pct","nd_disc2pct"},
      {"stObjectDescription","odescription*n"},

      {"stPFXClauses","pfx_clauses"},
      {"stPFXInterest","pfx_interest"},
      {"stPFXCoverage","pfx_coverage"},
      {"stPFXDeductible","pfx_deductible"},
      {"stPrintStamp","print_stamp"},

      {"stRIFinishFlag","f_ri_finish"},
      {"stCoTreatyID","co_treaty_id"},
      
      {"dtDLADate","dla_date"},
      {"stApprovedClaimNo","approved_claim_no"},
      {"dtApprovedClaimDate","claim_approved_date"},
      {"stApprovedClaimFlag","claim_approved_flag"},
      {"stActiveClaimFlag","claim_active_flag"},
      {"stEffectiveClaimFlag","claim_effective_flag"},
      {"stRateMethod","rate_method"},
      {"stRateMethodDesc","rate_method_desc"},
      {"dbClaimAmountEndorse","claim_amount_endorse"},
      {"stCoverNoteFlag","cover_note_flag"},

   };
   
   private Date dtDLADate;
   private String stApprovedClaimNo;
   private Date dtApprovedClaimDate;
   private String stApprovedClaimFlag;
   private String stActiveClaimFlag;
   private String stEffectiveClaimFlag;

   private BigDecimal dbAPComis;
   private BigDecimal dbAPComisP;
   private BigDecimal dbAPBrok;
   private BigDecimal dbAPBrokP;

   private String stCoTreatyID;
   private String stPrintStamp;
   private String stRIFinishFlag;
   private String stPFXClauses;
   private String stPLANo;
   private String stPFXInterest;
   private String stPFXCoverage;
   private String stPFXDeductible;
   private String stClaimObjectID;
   private BigDecimal dbPremiPaid;
   private BigDecimal dbClaimAmount;
   private BigDecimal dbClaimREAmount;
   private BigDecimal dbClaimCustAmount;
   private BigDecimal dbClaimAmountApproved;
   private Date dtPremiPayDate;

   private BigDecimal dbNDComm1;
   private BigDecimal dbNDComm2;
   private BigDecimal dbNDComm3;
   private BigDecimal dbNDComm4;
   private BigDecimal dbNDBrok1;
   private BigDecimal dbNDBrok1Pct;
   private BigDecimal dbNDBrok2;
   private BigDecimal dbNDBrok2Pct;
   private BigDecimal dbNDHFee;
   private BigDecimal dbNDHFeePct;
   private BigDecimal dbNDSFee;
   private BigDecimal dbNDPCost;

   private String stObjectDescription;

   private BigDecimal dbNDPremi1;
   private BigDecimal dbNDPremi2;
   private BigDecimal dbNDPremi3;
   private BigDecimal dbNDPremi4;
   private BigDecimal dbNDPremiRate1;
   private BigDecimal dbNDPremiRate2;
   private BigDecimal dbNDPremiRate3;
   private BigDecimal dbNDPremiRate4;
   private BigDecimal dbNDDisc1;
   private BigDecimal dbNDDisc2;
   private BigDecimal dbNDDisc1Pct;
   private BigDecimal dbNDDisc2Pct;
   private EntityView holdingCompany;
   private InsurancePolicyCoinsView holdingCoin;
   private InsurancePolicyObjectView claimObject;
   private InsurancePolicyCoverView getCoverage;
   private InsurancePolicyCoverReinsView getCoverageReins;
   private String stRateMethod;
   private String stRateMethodDesc;
   private BigDecimal dbClaimAmountEndorse;
   
   private DTOList list1;
   private DTOList list2;
   
   private String objIndex;
   
   private String stLunas;
   
   private String stCoverNoteFlag;
   
   public String getStCoverNoteFlag() {
      return stCoverNoteFlag;
   }

   public void setStCoverNoteFlag(String stCoverNoteFlag) {
      this.stCoverNoteFlag = stCoverNoteFlag;
   }
   
   public BigDecimal getDbClaimAmountEndorse() {
      return dbClaimAmountEndorse;
   }

   public void setDbClaimAmountEndorse(BigDecimal dbClaimAmountEndorse) {
      this.dbClaimAmountEndorse = dbClaimAmountEndorse;
   }

   public String getStRateMethod() {
      return stRateMethod;
   }

   public void setStRateMethod(String stRateMethod) {
      this.stRateMethod = stRateMethod;
   }
   
   public String getStRateMethodDesc() {
      return stRateMethodDesc;
   }
   
   public void setStRateMethodDesc(String stRateMethodDesc) {
      this.stRateMethodDesc = stRateMethodDesc;
   }
   
   public String getStActiveClaimFlag() {
      return stActiveClaimFlag;
   }

   public void setStActiveClaimFlag(String stActiveClaimFlag) {
      this.stActiveClaimFlag = stActiveClaimFlag;
   }
   
   public String getStEffectiveClaimFlag() {
      return stEffectiveClaimFlag;
   }

   public void setStEffectiveClaimFlag(String stEffectiveClaimFlag) {
      this.stEffectiveClaimFlag = stEffectiveClaimFlag;
   }
   
   
   public BigDecimal getDbPremiPaid() {
      return dbPremiPaid;
   }

   public void setDbPremiPaid(BigDecimal dbPremiPaid) {
      this.dbPremiPaid = dbPremiPaid;
   }

   public Date getDtPremiPayDate() {
      return dtPremiPayDate;
   }

   public void setDtPremiPayDate(Date dtPremiPayDate) {
      this.dtPremiPayDate = dtPremiPayDate;
   }
   
   public Date getDtDLADate() {
      return dtDLADate;
   }

   public void setDtDLADate(Date dtDLADate) {
      this.dtDLADate = dtDLADate;
   }
   
   public Date getDtApprovedClaimDate() {
      return dtApprovedClaimDate;
   }

   public void setDtApprovedClaimDate(Date dtApprovedClaimDate) {
      this.dtApprovedClaimDate = dtApprovedClaimDate;
   }
   
   public String getStApprovedClaimNo() {
      return stApprovedClaimNo;
   }

   public void setStApprovedClaimNo(String stApprovedClaimNo) {
      this.stApprovedClaimNo = stApprovedClaimNo;
   }
   
   public String getStApprovedClaimFlag() {
      return stApprovedClaimFlag;
   }

   public void setStApprovedClaimFlag(String stApprovedClaimFlag) {
      this.stApprovedClaimFlag = stApprovedClaimFlag;
   }

   public String getStPFXClauses() {
      return stPFXClauses;
   }

   public void setStPFXClauses(String stPFXClauses) {
      this.stPFXClauses = stPFXClauses;
   }

   public String getStPFXInterest() {
      return stPFXInterest;
   }

   public void setStPFXInterest(String stPFXInterest) {
      this.stPFXInterest = stPFXInterest;
   }

   public String getStPFXCoverage() {
      return stPFXCoverage;
   }

   public void setStPFXCoverage(String stPFXCoverage) {
      this.stPFXCoverage = stPFXCoverage;
   }

   public String getStPFXDeductible() {
      return stPFXDeductible;
   }

   public void setStPFXDeductible(String stPFXDeductible) {
      this.stPFXDeductible = stPFXDeductible;
   }

   public BigDecimal getDbNDBrok1Pct() {
      return dbNDBrok1Pct;
   }

   public String getStCoTreatyID() {
      return stCoTreatyID;
   }

   public void setStCoTreatyID(String stCoTreatyID) {
      this.stCoTreatyID = stCoTreatyID;
   }

   public void setDbNDBrok1Pct(BigDecimal dbNDBrok1Pct) {
      this.dbNDBrok1Pct = dbNDBrok1Pct;
   }

   public BigDecimal getDbNDBrok2Pct() {
      return dbNDBrok2Pct;
   }

   public void setDbNDBrok2Pct(BigDecimal dbNDBrok2Pct) {
      this.dbNDBrok2Pct = dbNDBrok2Pct;
   }

   public BigDecimal getDbNDHFeePct() {
      return dbNDHFeePct;
   }

   public void setDbNDHFeePct(BigDecimal dbNDHFeePct) {
      this.dbNDHFeePct = dbNDHFeePct;
   }

   public BigDecimal getDbNDPremi1() {
      return dbNDPremi1;
   }

   public void setDbNDPremi1(BigDecimal dbNDPremi1) {
      this.dbNDPremi1 = dbNDPremi1;
   }

   public BigDecimal getDbNDPremi2() {
      return dbNDPremi2;
   }

   public void setDbNDPremi2(BigDecimal dbNDPremi2) {
      this.dbNDPremi2 = dbNDPremi2;
   }

   public BigDecimal getDbNDPremi3() {
      return dbNDPremi3;
   }

   public void setDbNDPremi3(BigDecimal dbNDPremi3) {
      this.dbNDPremi3 = dbNDPremi3;
   }

   public BigDecimal getDbNDPremi4() {
      return dbNDPremi4;
   }

   public void setDbNDPremi4(BigDecimal dbNDPremi4) {
      this.dbNDPremi4 = dbNDPremi4;
   }

   public BigDecimal getDbNDPremiRate1() {
      return dbNDPremiRate1;
   }

   public void setDbNDPremiRate1(BigDecimal dbNDPremiRate1) {
      this.dbNDPremiRate1 = dbNDPremiRate1;
   }

   public BigDecimal getDbNDPremiRate2() {
      return dbNDPremiRate2;
   }

   public void setDbNDPremiRate2(BigDecimal dbNDPremiRate2) {
      this.dbNDPremiRate2 = dbNDPremiRate2;
   }

   public BigDecimal getDbNDPremiRate3() {
      return dbNDPremiRate3;
   }

   public void setDbNDPremiRate3(BigDecimal dbNDPremiRate3) {
      this.dbNDPremiRate3 = dbNDPremiRate3;
   }

   public BigDecimal getDbNDPremiRate4() {
      return dbNDPremiRate4;
   }

   public void setDbNDPremiRate4(BigDecimal dbNDPremiRate4) {
      this.dbNDPremiRate4 = dbNDPremiRate4;
   }

   public BigDecimal getDbNDDisc1() {
      return dbNDDisc1;
   }

   public void setDbNDDisc1(BigDecimal dbNDDisc1) {
      this.dbNDDisc1 = dbNDDisc1;
   }

   public BigDecimal getDbNDDisc2() {
      return dbNDDisc2;
   }

   public void setDbNDDisc2(BigDecimal dbNDDisc2) {
      this.dbNDDisc2 = dbNDDisc2;
   }

   public BigDecimal getDbNDDisc1Pct() {
      return dbNDDisc1Pct;
   }

   public void setDbNDDisc1Pct(BigDecimal dbNDDisc1Pct) {
      this.dbNDDisc1Pct = dbNDDisc1Pct;
   }

   public BigDecimal getDbNDDisc2Pct() {
      return dbNDDisc2Pct;
   }

   public void setDbNDDisc2Pct(BigDecimal dbNDDisc2Pct) {
      this.dbNDDisc2Pct = dbNDDisc2Pct;
   }

   public BigDecimal getDbNDComm1() {
      return dbNDComm1;
   }

   public void setDbNDComm1(BigDecimal dbNDComm1) {
      this.dbNDComm1 = dbNDComm1;
   }

   public BigDecimal getDbNDComm2() {
      return dbNDComm2;
   }

   public void setDbNDComm2(BigDecimal dbNDComm2) {
      this.dbNDComm2 = dbNDComm2;
   }

   public BigDecimal getDbNDComm3() {
      return dbNDComm3;
   }

   public void setDbNDComm3(BigDecimal dbNDComm3) {
      this.dbNDComm3 = dbNDComm3;
   }

   public BigDecimal getDbNDComm4() {
      return dbNDComm4;
   }

   public void setDbNDComm4(BigDecimal dbNDComm4) {
      this.dbNDComm4 = dbNDComm4;
   }

   public BigDecimal getDbNDBrok1() {
      return dbNDBrok1;
   }

   public void setDbNDBrok1(BigDecimal dbNDBrok1) {
      this.dbNDBrok1 = dbNDBrok1;
   }

   public BigDecimal getDbNDBrok2() {
      return dbNDBrok2;
   }

   public void setDbNDBrok2(BigDecimal dbNDBrok2) {
      this.dbNDBrok2 = dbNDBrok2;
   }

   public BigDecimal getDbNDHFee() {
      return dbNDHFee;
   }

   public void setDbNDHFee(BigDecimal dbNDHFee) {
      this.dbNDHFee = dbNDHFee;
   }

   public BigDecimal getDbNDSFee() {
      return dbNDSFee;
   }

   public void setDbNDSFee(BigDecimal dbNDSFee) {
      this.dbNDSFee = dbNDSFee;
   }

   public BigDecimal getDbNDPCost() {
      return dbNDPCost;
   }

   public void setDbNDPCost(BigDecimal dbNDPCost) {
      this.dbNDPCost = dbNDPCost;
   }


   /*
ALTER TABLE ins_policy ADD COLUMN nd_comm1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm3 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm4 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_brok1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_brok2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_hfee numeric;
ALTER TABLE ins_policy ADD COLUMN nd_sfee numeric;
ALTER TABLE ins_policy ADD COLUMN nd_pcost numeric;

   */

   private Date dtEndorseDate;
   private String stEffectiveFlag;
   private String stRootID;
   private String stClaimStatus;
   private String stPremiumFactorID;

   public String getStNDUpdate() {
      return null;
   }

   public void setStNDUpdate(String stNDUpdate) {
   }


   /*
ALTER TABLE ins_policy ADD COLUMN endorse_date timestamp;
ALTER TABLE ins_policy ADD COLUMN effective_flag varchar(1);
ALTER TABLE ins_policy ADD COLUMN claim_status varchar(10);

   */

   private String stClaimNo;
   private Date dtClaimDate;
   private String stClaimCauseID;
   private String stClaimCauseDesc;
   private String stClaimEventLocation;
   private String stClaimPersonID;
   private String stClaimPersonName;
   private String stDLANo;
   private String stClaimPersonAddress;
   private String stClaimPersonStatus;
   private BigDecimal dbClaimAmountEstimate;
   private BigDecimal dbClaimDeductionAmount;
   private BigDecimal dbClaimDeductionCustAmount;
   private String stClaimCurrency;
   private String stClaimLossStatus;
   private String stClaimBenefit;
   private String stClaimDocuments;
   private String stInsuranceTreatyID;
   private String stNomerator;
   private String stDLARemark;

   /*
`ALTER TABLE ins_policy ADD COLUMN claim_no varchar(128);
`ALTER TABLE ins_policy ADD COLUMN claim_date timestamp;
`ALTER TABLE ins_policy ADD COLUMN claim_cause int8;
`ALTER TABLE ins_policy ADD COLUMN claim_cause_desc text;
`ALTER TABLE ins_policy ADD COLUMN event_location text;
`ALTER TABLE ins_policy ADD COLUMN claim_person_id int8;
`ALTER TABLE ins_policy ADD COLUMN claim_person_name varchar(255);
`ALTER TABLE ins_policy ADD COLUMN claim_person_address varchar(255);
`ALTER TABLE ins_policy ADD COLUMN claim_person_status varchar(255);
`ALTER TABLE ins_policy ADD COLUMN claim_amount_est numeric;
`ALTER TABLE ins_policy ADD COLUMN claim_currency varchar(3);
`ALTER TABLE ins_policy ADD COLUMN claim_loss_status varchar(32);
`ALTER TABLE ins_policy ADD COLUMN claim_benefit varchar(10);
`ALTER TABLE ins_policy ADD COLUMN claim_documents text;

   */

   private String stEndorseNotes;
   private String stPeriodBaseID;

   private String stReference1;
   private String stReference2;
   private String stReference3;
   private String stReference4;
   private String stReference5;
   private String stReference6;
   private String stReference7;
   private String stReference8;
   private String stReference9;
   private String stReference10;
   private String stReference11;
   private String stReference12;
   private String stPrintCode;

   private Date dtReference1;
   private Date dtReference2;
   private Date dtReference3;
   private Date dtReference4;
   private Date dtReference5;

   private BigDecimal dbReference1;
   private BigDecimal dbReference2;
   private BigDecimal dbReference3;
   private BigDecimal dbReference4;
   private BigDecimal dbReference5;

//   ALTER TABLE ins_policy ADD COLUMN cust_name varchar(255);
//   ALTER TABLE ins_policy ADD COLUMN cust_address varchar(255);
//   ALTER TABLE ins_policy ADD COLUMN period_rate numeric;
//
//   ALTER TABLE ins_policy ADD COLUMN ins_period_id int8;
//   ALTER TABLE ins_policy ADD COLUMN inst_period_id int8;
//   ALTER TABLE ins_policy ADD COLUMN inst_periods int8;



   private Date dtPolicyDate;

   private String stSPPANo;
   private String stActiveFlag;
   private String stInsurancePeriodID;
   private String stParentID;
   private String stInstallmentPeriodID;
   private Long lgInstallmentPeriods;
   private String stProducerID;
   private String stProducerName;
   private String stProducerEntName;
   private String stProducerAddress;
   private String stCustomerName;
   private String stCustomerAddress;
   private String stBusinessSourceCode;
   private String stProductionOnlyFlag;
   private String stRegionID;
   private String stCaptiveFlag;
   private String stInwardFlag;
   private String stEntityID;
   private String stConditionID;
   private static String stRiskCategoryID;
   private String stPolicyTypeGroupID;
   private String stPeriodBaseBeforeID;

   private String stPolicyID;
   private String stMasterPolicyID;
   private String stPolicyNo;
   private String stPolicyTypeID;
   private String stPolicyTypeDesc;
   private String stDescription;
   private String stCurrencyCode;
   private String stPostedFlag;
   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private BigDecimal dbAmount;
   private BigDecimal dbPremiBase;
   private BigDecimal dbPremiTotal;
   private BigDecimal dbPremiTotalAfterDisc;
   private BigDecimal dbTotalDue;
   private BigDecimal dbPremiNetto;
   private BigDecimal dbPremiRate;
   private BigDecimal dbInsuredAmount;
   private BigDecimal dbInsuredAmountEndorse;
   private BigDecimal dbCurrencyRate;
   private BigDecimal dbCoinsCheckInsAmount;
   private BigDecimal dbCoinsCheckPremiAmount;
   private BigDecimal dbPeriodRate;
   private BigDecimal dbPeriodRateBefore;
   private BigDecimal dbInsuredAmountAfterKurs;
   private String stPolicySubTypeID;
   public String stCostCenterCode;
   public static String stCostCenterCode2;
   private String stStatus;
   private String stNextStatus;
   private String stCoverTypeCode;
   private String stLockCoinsFlag;
   private Class clObjectClass;
   private FormTab tab;

   private DTOList details;
   private DTOList claimDocuments;

   private DTOList claimItems;
   private DTOList clausules;
   private DTOList objects;
   private DTOList entities;
   private DTOList covers;
   private DTOList coveragereins;
   private DTOList suminsureds;
   private DTOList coins;
   private DTOList deductibles;
   private InsurancePolicyEntityView owner;
   private DTOList installment;
   private DTOList arinvoices;
   private DTOList treaties;
   private BigDecimal dbTotalFee;
   private boolean nomeratorLoaded;
   private EntityView entity;
   private String stDocumentBranchingFlag;
   private DTOList coverage;

   public String getStLockCoinsFlag() {
      return stLockCoinsFlag;
   }

   public void setStLockCoinsFlag(String stLockCoinsFlag) {
      this.stLockCoinsFlag = stLockCoinsFlag;
   }

   public String getStDocumentBranchingFlag() {
      return stDocumentBranchingFlag;
   }

   public void setStDocumentBranchingFlag(String stDocumentBranchingFlag) {
      this.stDocumentBranchingFlag = stDocumentBranchingFlag;
   }

   public DTOList getClaimDocuments() {
      if (claimDocuments==null && stPolicyID!=null && stPolicyTypeID!=null)
         claimDocuments = loadDocuments(stPolicyTypeID, stPolicyID, "CLAIM");
      return claimDocuments;
   }

   private static DTOList loadDocuments(String poltype, String stPolicyID, String documentClass) {
      try {

         final DTOList l = ListUtil.getDTOListFromQuery(
                             "   select " +
                             "      b.*,c.description,a.ins_document_type_id " +
                             "   from " +
                             "      ins_documents a" +
                             "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id" +
                             "      left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id" +
                             "   where " +
                             "      a.pol_type_id=? " +
                             "      and a.document_class=? " ,
                             new Object [] {stPolicyID, poltype, documentClass},
                             InsurancePolicyDocumentView.class
                     );

         for (int i = 0; i < l.size(); i++) {
            InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

            if (docs.getStInsurancePolicyDocumentID()!=null) docs.setStSelectedFlag("Y");

            docs.setStPolicyID(stPolicyID);
            docs.setStDocumentClass(documentClass);
         }

         return l;

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setClaimDocuments(DTOList claimDocuments) {
      this.claimDocuments = claimDocuments;
   }

   public DTOList getTreaties() {
      loadTreaties();
      return treaties;
   }

   private void loadTreaties() {
      try {
         if (treaties==null) {
            treaties = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_treaty where policy_id=?",
                    new Object [] {stPolicyID},
                    InsurancePolicyTreatyView.class
            );
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setTreaties(DTOList treaties) {
      this.treaties = treaties;
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public String getStBusinessSourceCode() {
      return stBusinessSourceCode;
   }

   public void setStBusinessSourceCode(String stBusinessSourceCode) {
      this.stBusinessSourceCode = stBusinessSourceCode;
   }

   public String getStRegionID() {
      return stRegionID;
   }

   public void setStRegionID(String stRegionID) {
      this.stRegionID = stRegionID;
   }

   public String getStCaptiveFlag() {
      return stCaptiveFlag;
   }

   public void setStCaptiveFlag(String stCaptiveFlag) {
      this.stCaptiveFlag = stCaptiveFlag;
   }

   public String getStInwardFlag() {
      return stInwardFlag;
   }

   public void setStInwardFlag(String stInwardFlag) {
      this.stInwardFlag = stInwardFlag;
   }

   public Class getClObjectClass() {

      if (clObjectClass==null) {
         final InsurancePolicyTypeView pt = getPolicyType();

         if (pt!=null)
            clObjectClass = pt.getClObjectClass();

         /*final boolean isObjectMap = pt.getStPolicyTypeCode().indexOf("OM_")==0;

         if (isObjectMap)
            clObjectClass = InsurancePolicyObjDefaultView.class;
         else if (pt!=null)
            clObjectClass = (Class) FinCodec.PolicyTypeCodeMap.getLookUp().getValue(pt.getStPolicyTypeCode());*/
      }

      return clObjectClass;
   }

   public InsurancePolicyTypeView getPolicyType() {
      return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
   }

   public void setClObjectClass(Class clObjectClass) {
      this.clObjectClass = clObjectClass;
   }

   public final static transient String TAB_PREMI = "premi";
   public final static transient String TAB_CLAUSULES = "clausula";
   public final static transient String TAB_OBJECTS = "objects";
   public final static transient String TAB_OWNER = "owner";

   public FormTab getTab() {
      if (tab==null) {
         tab = new FormTab();

         tab.add(new FormTab.TabBean(TAB_PREMI,"PREMI",true));
         tab.add(new FormTab.TabBean(TAB_CLAUSULES,"CLAUSULES",true));
         tab.add(new FormTab.TabBean(TAB_OBJECTS,"OBJECTS",true));
         tab.add(new FormTab.TabBean(TAB_OWNER,"OWNER",true));

         tab.setActiveTab(TAB_PREMI);
      }
      return tab;
   }

   public void setTab(FormTab tab) {
      this.tab = tab;
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

   public void calculatePeriods() {

      boolean endorsement = isStatusEndorse();

      Date pstart=dtPeriodStart;

      try {
         if (endorsement) pstart = getParentPolicy().getDtPeriodEnd();
      } catch (Exception e) {
      }

	  if(!endorsement){
	  	if(pstart!=null && dtPeriodEnd!=null) 
         {
         	
         	
            long l = dtPeriodEnd.getTime()-pstart.getTime();

            l/=(1000l*60l*60l*24l);

            final BigDecimal periodDays = new BigDecimal(l);

           
             /*Coding  asli sebelum ubah untuk periodBase
             */
            final BigDecimal periodBase = periodDays.divide(new BigDecimal("366"), 10, BigDecimal.ROUND_HALF_DOWN);
			
			
			//javax.swing.JOptionPane.showMessageDialog(null,"periodDays= "+ periodDays,"Eror",javax.swing.JOptionPane.DEFAULT_OPTION);

			
////	            //Start update
////	            BigDecimal periodBase = null;
////	            
////	            
////	            if(periodDays.equals(new BigDecimal(0)))
////	            {
////	            	periodBase = new BigDecimal(100);
////	            }
////	            else
////	            {
////	            	periodBase = periodDays.divide(periodDays, 1, BigDecimal.ROUND_HALF_DOWN);
////	            
////	            }
////				//End update
			
			
            if(stPeriodBaseID!=null) {
               final BigDecimal baseu = getPeriodBase().getDbBaseUnit();
               if (baseu!=null) {


                  final BigDecimal periodFactor = periodBase.multiply(baseu);

				  if(endorsement){
				  		dbPeriodRate = new BigDecimal(l);
				  }else{
				  		dbPeriodRate = periodFactor;
				  }
                  	

                  if (Tools.compare(dbPeriodRate, BDUtil.zero)<0) dbPeriodRate = BDUtil.zero;
               }
            }

            if (stPremiumFactorID==null) {
               stPremiumFactorID=InsurancePolicyUtil.getInstance().findPremiumFactor(periodBase);
            }
         }
        


      	
	  }else{
	  	
				if (endorsement)
	         {
	            Date pend=dtPeriodEnd;
	
	            try {
	               pend = getParentPolicy().getDtPeriodEnd();
	            } catch (Exception e) {
	            }
	
	            long l = pend.getTime()-dtEndorseDate.getTime();
	            l/=(1000l*60l*60l*24l);
	
	            final BigDecimal periodDays = new BigDecimal(l);
	
	            
	             /*Coding  asli sebelum ubah untuk periodBase
	             */
	            //final BigDecimal periodBase2 = periodDays2.divide(new BigDecimal("366"), 10, BigDecimal.ROUND_HALF_DOWN);
				
	
				
	            //Start update
	            BigDecimal periodBase = null;
	            
	            
	            if(periodDays.equals(new BigDecimal(0)))
	            {
	            	periodBase = new BigDecimal(100);
	            }
	            else
	            {
	            	periodBase = periodDays.divide(periodDays, 10, BigDecimal.ROUND_HALF_DOWN);
	            
	            }
				//End update
				
	
	            if(stPeriodBaseBeforeID!=null) {
	               final BigDecimal baseu = getPeriodBaseBefore().getDbBaseUnit();
	               if (baseu!=null) {
	
	                  final BigDecimal periodFactor = periodBase.multiply(baseu);
	
	                  dbPeriodRateBefore = periodFactor;
	
	                  if (Tools.compare(dbPeriodRateBefore, BDUtil.zero)<0) dbPeriodRateBefore = BDUtil.zero;
	               }
	            }
	         }	  	
		  }

   }
   
   public void defaultPeriods() {
		setDbPeriodRate(new BigDecimal(100));
		setStPeriodBaseID("2");
		setStPremiumFactorID("1");
   }

   public void recalculate() throws Exception{
      recalculateBasic();
      recalculateClaim();
      invokeCustomHandlers();
      recalculateBasic();
      recalculateClaim();
      validateTreaty(false);
      invokeCustomHandlers();
      //recalculateTreaty();
   }
   

   private void recalculateClaim() {

      getClaimItems();

      dbClaimDeductionAmount = null;
      dbClaimDeductionCustAmount = null;
      dbClaimAmount = null;
      dbClaimCustAmount = null;
      dbClaimREAmount = null;

      if(claimItems!=null)

         for (int i = 0; i < claimItems.size(); i++) {

            InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

            final boolean claimg = Tools.isEqual(it.getInsuranceItem().getStItemType(), "CLAIMG");
            final boolean deduct = Tools.isEqual(it.getInsuranceItem().getStItemType(), "DEDUCT");

            final boolean custAmount = claimg || deduct;

            //it.calculateRateAmount(getDbClaimAmountEstimate(),s);

            final boolean chargeable = Tools.isYes(it.getStChargableFlag());


            if (claimg) {
               //it.setDbAmount(getDbClaimAmountEstimate());
               if(isStatusClaimPLA())
               		it.setDbAmount(getDbClaimAmountEstimate());
               else if(isStatusClaimDLA())
               		it.setDbAmount(getDbClaimAmountApproved());	
            }

            BigDecimal amt = it.getDbAmount();

            ARTransactionLineView arTrxLine = it.getInsItem().getARTrxLine();

            if (arTrxLine==null) throw new RuntimeException(" Unable to retrieve ARTRXLINE for ins item : "+it.getInsItem().getStInsuranceItemID());

            final boolean neg = arTrxLine.isNegative();

            if (neg) dbClaimDeductionAmount = BDUtil.add(dbClaimDeductionAmount,amt);
            if (neg && custAmount) dbClaimDeductionCustAmount = BDUtil.add(dbClaimDeductionCustAmount,amt);

            if (neg) amt=BDUtil.negate(amt);

            if (chargeable)
               dbClaimREAmount = BDUtil.add(dbClaimREAmount, amt);

            dbClaimAmount = BDUtil.add(dbClaimAmount, amt);

            if (custAmount)
               dbClaimCustAmount = BDUtil.add(dbClaimCustAmount, amt);
         }

      if (claimObject!=null) {
         final DTOList treatyDetails = claimObject.getTreatyDetails();

         BigDecimal claimTot = dbClaimREAmount;
         
         BigDecimal sisaORXOL = BDUtil.zero;
         
         BigDecimal sisaORXOL1 = BDUtil.zero;
         
         BigDecimal sisaORXOL2 = BDUtil.zero;

         final BigDecimal totalTSI = claimObject.getDbObjectInsuredAmountShare();
         
         final BigDecimal totalTSI2 = claimObject.getDbObjectInsuredAmount();

         final HashMap trdMap = treatyDetails.getMapOf("ins_treaty_detail_id");

         for (int i = 0; i < treatyDetails.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(i);

            //final BigDecimal shareRatio = BDUtil.div(trd.getDbTSIAmount(), totalTSI);

            //trd.setDbClaimAmount(BDUtil.div(BDUtil.mul(trd.getDbTSIAmount(),claimTot),totalTSI));
            //trd.setDbClaimAmountCalc(trd.getDbClaimAmount());

           // if (trd.getStParentID()==null) {
            	
            	//trd.setDbClaimAmount(BDUtil.div(BDUtil.mul(trd.getDbTSIAmount(),claimTot),totalTSI2));
            	trd.setDbClaimAmount(BDUtil.div(BDUtil.mul(trd.getDbTSIAmount(),claimTot,2),totalTSI2));
            	trd.setDbClaimAmount(trd.getDbClaimAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN));
            
            	InsuranceTreatyView treatyView = claimObject.getTreaty();
            	
            	final DTOList treatyDetails2 = treatyView.getDetails(stPolicyTypeID);
            	
            	for(int j=0; j<treatyDetails2.size();j++){
            		InsuranceTreatyDetailView tredetView = (InsuranceTreatyDetailView) treatyDetails2.get(j);
            		
            	   
            		if(tredetView.getDbXOLLower()!=null)
            		   if(trd.getTreatyDetail().isOR()){
            		   	 if(tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL1")){
            				if(Tools.compare(trd.getDbClaimAmount(),tredetView.getDbXOLLower())>0){
            					sisaORXOL = BDUtil.sub(trd.getDbClaimAmount(),tredetView.getDbXOLLower());
            					trd.setDbClaimAmount(tredetView.getDbXOLLower());
            				}
            			 }
            		   }else if(trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL1")){
            		   	  if(tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL1")){
            		   	  	trd.setDbClaimAmount(sisaORXOL);
            		   		if(Tools.compare(trd.getDbClaimAmount(),tredetView.getDbXOLUpper())>0){
            		   			sisaORXOL1 = trd.getDbClaimAmount();
            		   			trd.setDbClaimAmount(tredetView.getDbXOLUpper());
            		   		}
            		   	  }

            		   }else if(trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL2")){
            		   	  if(tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL2")){
            		   	  	trd.setDbClaimAmount(sisaORXOL1);
            		   		if(Tools.compare(trd.getDbClaimAmount(),tredetView.getDbXOLUpper())>0){
            		   			sisaORXOL2 = trd.getDbClaimAmount();
            		   			trd.setDbClaimAmount(tredetView.getDbXOLUpper());
            		   		}
            		   	  }

            		   }else if(trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL3")){
            		   	  if(tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL3")){
            		   	  	trd.setDbClaimAmount(sisaORXOL2);
            		   		if(Tools.compare(trd.getDbClaimAmount(),tredetView.getDbXOLUpper())>0){
            		   			//sisaORXOL1 = trd.getDbClaimAmount();
            		   			trd.setDbClaimAmount(tredetView.getDbXOLUpper());
            		   		}
            		   	  }

            		   }
            		   
            		   
            		   

            	}
            //	javax.swing.JOptionPane.showMessageDialog(null,"Claim Amount= ("+ trd.getDbTSIAmount()+" x "+claimTot+") / "+totalTSI2,"TEs",javax.swing.JOptionPane.CLOSED_OPTION,null);
            	
               /*if (Tools.compare(trd.getDbTSIAmount(), claimTot)>0) {
                  trd.setDbClaimAmount(claimTot);
               } else {
                  trd.setDbClaimAmount(trd.getDbTSIAmount());
               }

               claimTot  = BDUtil.sub(claimTot,trd.getDbClaimAmount());*/

            /*} else {
               InsurancePolicyTreatyDetailView trdparent =  (InsurancePolicyTreatyDetailView) trdMap.get(trd.getStParentID());
				*/
               /*if (Tools.compare(trd.getDbTSIAmount(), claimTot)>0) {
                  trd.setDbClaimAmount(claimTot);
               } else {
                  trd.setDbClaimAmount(trd.getDbTSIAmount());
               }

               claimTot  = BDUtil.sub(claimTot,trd.getDbClaimAmount());*/

               /*trd.setDbClaimAmount(BDUtil.mul(trdparent.getDbClaimAmount(), BDUtil.getRateFromPct(trd.getDbTSIPct())));
               trdparent.setDbClaimAmount(BDUtil.sub(trdparent.getDbClaimAmount(), trd.getDbClaimAmount()));*/
				
				//ubah doni
				/*
               if (trd.getTreatyDetail().getDbXOLLower()!=null) {
                  if (Tools.compare(trdparent.getDbClaimAmount(), trd.getTreatyDetail().getDbXOLLower())>0) {
                     trd.setDbClaimAmount(BDUtil.sub(trdparent.getDbClaimAmountCalc(), trd.getTreatyDetail().getDbXOLLower()));

                     if (Tools.compare(trd.getDbClaimAmount(), trd.getTreatyDetail().getDbXOLMaxAmount())>0) {
                        trd.setDbClaimAmount(trd.getTreatyDetail().getDbXOLMaxAmount());
                     }

                     trdparent.setDbClaimAmount(
                             BDUtil.sub(
                                     trdparent.getDbClaimAmount(),
                                     trd.getDbClaimAmount()
                             )
                     );
                  }
               }
            }*/
			
			
            final DTOList shares = trd.getShares();

            for (int j = 0; j < shares.size(); j++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);

               //ri.setDbClaimAmount(BDUtil.mul(trd.getDbClaimAmount(), BDUtil.getRateFromPct(ri.getDbSharePct())));
                ri.setDbClaimAmount(BDUtil.mul(trd.getDbClaimAmount(), BDUtil.getRateFromPct(ri.getDbSharePct()),2));

            }


         }
         
         //tes
          {
      		try{
		  	 list2 = ListUtil.getDTOListFromQuery(
	                 "select exc_claim_flag from ins_clm_cause"+
					 " where ins_clm_caus_id=? ",
	                 new Object [] {stClaimCauseID},
	                 InsuranceClaimCauseView.class
	
	         );
		  }catch (Exception e) {
	         throw new RuntimeException(e);
	      }
      
	     InsuranceClaimCauseView claimCause = (InsuranceClaimCauseView) list2.get(0);
             
             
         DTOList coins = getCoins();

         for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

            //if (co.isHoldingCompany()) continue;

            //boolean autoCalc = !Tools.isYes(co.getStAutoClaimAmount());
			if(stPolicyTypeID.equalsIgnoreCase("21")){
				if(claimCause.getStExcClaimFlag()!=null){
					if(claimCause.getStExcClaimFlag().equalsIgnoreCase("Y")){
					if(co.isHoldingCompany()){ co.setDbClaimAmount(BDUtil.zero);}
					else{
					   if(isStatusClaimPLA())
		               		co.setDbClaimAmount(getDbClaimAmountEstimate());
		               else if(isStatusClaimDLA())
		               		co.setDbClaimAmount(getDbClaimAmountApproved());
					}
				  }
				  	
				  
		      }else if(claimCause.getStExcClaimFlag()==null){
		      	  
		      	  if(co.isHoldingCompany()){
		      	  		if(isStatusClaimPLA())
		               		co.setDbClaimAmount(getDbClaimAmountEstimate());
		               else if(isStatusClaimDLA())
		               		co.setDbClaimAmount(getDbClaimAmountApproved());
		      	  } 
		      	  else co.setDbClaimAmount(BDUtil.zero);
		      }
			}else{
				
				BigDecimal claimAmt =null;
				
				if(isStatusClaimPLA())
		               		claimAmt = BDUtil.mul(getDbClaimAmountEstimate(), BDUtil.getRateFromPct(co.getDbSharePct()),2);
		        else if(isStatusClaimDLA())
		               		claimAmt = BDUtil.mul(getDbClaimAmountApproved(), BDUtil.getRateFromPct(co.getDbSharePct()),2);
	
                co.setDbClaimAmount(claimAmt);
			}
			
			
              
         }
             //
         }
	 
         //
		/*
         DTOList coins = getCoins();

         for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

            if (co.isHoldingCompany()) continue;

            boolean autoCalc = !Tools.isYes(co.getStAutoClaimAmount());

            if (autoCalc) {
               BigDecimal claimAmt = BDUtil.mul(getDbClaimAmount(), BDUtil.getRateFromPct(co.getDbSharePct()));

               co.setDbClaimAmount(claimAmt);
            }
         }*/
         /*
	         if(getDbClaimAmountApproved()!=null)
		         if(Tools.compare(getDbClaimAmountApproved(),getDbObjectTSIAmount(Integer.parseInt(objIndex)))>0){
		         	setDbClaimAmountApproved(BDUtil.zero);
		         	throw new RuntimeException("Jumlah Klaim Disetujui > Jumlah TSI");
		         }*/  

      }
   }

   private void invokeCustomHandlers() {
      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

         //CustomHandlerManager.getInstance().onCalculate(this, obj);
      }
   }
   
   


public void recalculateTreaty()throws Exception {
      if (getStCoverTypeCode()==null) return;
      if (getStPolicyTypeID()==null) return;
      final DTOList coin = getCoins();
 	  boolean isCoas;
 	  if(hasCoIns()) isCoas = true;
 	  else isCoas = false;
 	  
 	  BigDecimal checkPremiAmount = new BigDecimal(0);

      try {
      	
      	  //cek koas
      	  
      	  if(isCoas){
		      	{
		         final BigDecimal BD100 = new BigDecimal(100);
		
		         BigDecimal checkInsAmount=null;
		         
		
		         InsurancePolicyCoinsView holdingcoin = getHoldingCoin();
		
		         BigDecimal totPct = null;
		         BigDecimal totTSI = null;
		         BigDecimal premiAfterDisc = null;
		
		         for (int i = 0; i < coin.size(); i++) {
		            InsurancePolicyCoinsView coin2 = (InsurancePolicyCoinsView) coin.get(i);
					
					if(!coin2.isHoldingCompany()) continue;
					
		            checkPremiAmount = coin2.getDbPremiAmount();
		         }
		 
		        }
      	  }
      	  


         for (int i = 0; i < this.objects.size(); i++) {
            InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) this.objects.get(i);

            final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();
            
            //if(getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE))
            //	BigDecimal insuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
            
            final BigDecimal insuredAmount2 = ipo.getDbObjectInsuredAmountShare();
            
            //final BigDecimal insuredAmountKurs = BDUtil.mul(ipo.getDbObjectInsuredAmountShare(),dbCurrencyRate);
            final BigDecimal insuredAmountKurs = BDUtil.mul(ipo.getDbObjectInsuredAmountShare(),dbCurrencyRate,2);

            final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

            final DTOList treaties = ipo.getTreaties();

            if (treaties.size()<1) {
               final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

               tre.markNew();
               tre.setStInsuranceTreatyID(getStInsuranceTreatyID());
               tre.setStPolicyID(stPolicyID);

               treaties.add(tre);
            }

            final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView)treaties.get(0);

            //tre.adjustRatio(tlr,dbCurrencyRate);

            //tre.raiseToTSI(getStPolicyTypeID() , insuredAmount, tlr, dbCurrencyRate);
            
            
            for (int j = 0; j < tre.getDetails().size(); j++) {

               InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) tre.getDetails().get(j);

               if (trd.getTreatyDetail().isOR()) {

                  BigDecimal premRat = BDUtil.div(trd.getDbTSIAmount(),ipo.getDbObjectInsuredAmount(),19);

                  //BigDecimal orPremi = BDUtil.mul(premRat,ipo.getDbObjectPremiTotalAmount());
                  BigDecimal orPremi = BDUtil.mul(premRat,ipo.getDbObjectPremiTotalAmount(),2);

                  //trd.setDbPremiAmount(orPremi);
						
                  BigDecimal premiRate = BDUtil.div(orPremi, trd.getDbTSIAmount(),18);

                  //trd.setDbPremiRatePct(premiRate);

                  DTOList shares = trd.getShares();

                  for (int k = 0; k < shares.size(); k++) {
                     InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                     BigDecimal rateActual = new BigDecimal(0);
                     ri.setStUseRateFlag("N");
                     ri.setStAutoRateFlag("N");
                     
                     if(getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual = BDUtil.getPctFromRate(premiRate);
			         else if(getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual = BDUtil.getMileFromRate(premiRate);


                     ri.setDbPremiRate(rateActual);
                     ri.setDbPremiAmount(orPremi);
                  }   
               }
            }

			if(isCoas) ipo.setDbObjectPremiTotalAmount(checkPremiAmount);
			
			getCoverage2();
			
			tre.setStRateMethod(getStRateMethod());
			
            //tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, BDUtil.zero);
			
         }
      } catch (RuntimeException e) {
         setStInsuranceTreatyID(null);
         setStCoverTypeCode(null);
         throw e;
      }

   }
   
public void recalculateTreatyEndorse()throws Exception{
	  recalculateTreaty();
	  
	  final DTOList parentObj = getParentPolicy().getObjects();
	  
	  final DTOList obj = getObjects();
	  
	  for(int i=0; i< parentObj.size(); i++){
	  		InsurancePolicyObjectView parentObjView = (InsurancePolicyObjectView) parentObj.get(i);
	  		
	  		final DTOList parentTreaties = parentObjView.getTreaties();
	  		final InsurancePolicyTreatyView parentTre = (InsurancePolicyTreatyView)parentTreaties.get(0);
	  		
	  		for(int k=0;k<obj.size();k++){
	  			InsurancePolicyObjectView ObjView = (InsurancePolicyObjectView) obj.get(i);
	  			
	  			for(int j=0;j < parentTre.getDetails().size();j++){
	  			InsurancePolicyTreatyDetailView parentTredet = (InsurancePolicyTreatyDetailView) parentTre.getDetails().get(j);


	  				final DTOList treaties = ObjView.getTreaties();
	  				final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView)treaties.get(0);
	  				
	  				for(int l=0;l < tre.getDetails().size();l++){
	  					InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tre.getDetails().get(j);
	  					
	  					if(parentTredet.getStInsuranceTreatyDetailID().equalsIgnoreCase(tredet.getStInsuranceTreatyDetailID())){
	  						//javax.swing.JOptionPane.showMessageDialog(null,"premi= "+tredet.getDbPremiAmount(),"tes",javax.swing.JOptionPane.CLOSED_OPTION);
	  						//javax.swing.JOptionPane.showMessageDialog(null,"parent premi= "+parentTredet.getDbPremiAmount(),"tes",javax.swing.JOptionPane.CLOSED_OPTION);

	  						tredet.setDbPremiAmount(BDUtil.sub(tredet.getDbPremiAmount(),parentTredet.getDbPremiAmount()));
	  					}
	  				}
	  			
	  		}
	  		}
	
	  }
}

/*
   public void recalculateBasic() {
      /*BigDecimal tot=null;
      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

         ip.getInsuranceItem().isComission()

         tot = BDUtil.add(tot,ip.getDbAmount());
      }

      dbAmount = tot;*/

      /*final InsurancePeriodView insurancePeriod = getInsurancePeriod();

      if (insurancePeriod!=null) {
         dtPeriodEnd = insurancePeriod.advance(dtPeriodStart);
         dbPeriodRate = insurancePeriod.getDbYearlyRate();
      } else {*/
         //dbPeriodRate = new BigDecimal(DateUtil.sub(dtPeriodStart, dtPeriodEnd)).divide(new BigDecimal(1000l*60l*60l*24l*366l), 5 ,BigDecimal.ROUND_HALF_UP);
      //}
      
/*
      if (stPolicyTypeID==null) return;

      loadClausules();
      loadEntities();
      loadDetails();
      loadObjects();

      if (stPolicyTypeID!=null) {

         FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

         if (objectMap!=null) {
            for (int i = 0; i < objects.size(); i++) {
               InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

               DTOList details = objectMap.getDetails();

               for (int p = 0; p < details.size(); p++) {
                  FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                  if (ffd.getStLOV()!=null) {
                     String desc = LOVManager.getInstance().getDescription(
                             (String) obj.getProperty(ffd.getStFieldRef()),
                             ffd.getStLOV());

                     obj.setProperty(ffd.getStFieldRef()+"Desc", desc);
                  }
               }
            }

         }
      }


      if ((objects!=null) && (objects.size()>0)) {

         dbInsuredAmount = null;

         for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            obj.reCalculate();



            dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
           	dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());                   
            
         }

      }

      if (dbPremiRate!=null) {
         dbPremiBase = BDUtil.mul(dbInsuredAmount, dbPremiRate);
      }
	  
      dbPremiTotal = dbPremiBase;

      if (clausules!=null)
         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

            icl.recalculate();

            if (icl.isSelected()) {

               if (icl.getDbRate()!=null) {
                  icl.setDbAmount(BDUtil.mul(icl.getDbRate(), dbPremiBase));
               }

               dbPremiTotal = BDUtil.add(dbPremiTotal, icl.getDbAmount());
            }
            else dbAmount = BDUtil.zero;
         }

      for (int j = 0; j < objects.size(); j++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

         DTOList cls = obj.getClausules();

         if (cls==null) {
            cls = new DTOList();
            obj.setClausules(cls);
         }

         final HashMap clsMap = new HashMap();

         for (int i = 0; i < cls.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) cls.get(i);
            clsMap.put(icl.getStClauseID(),icl);
         }

         final HashMap masterMap = new HashMap();

         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

            if (icl.isSelected()) {
               masterMap.put(icl.getStClauseID(), icl);

               if (icl.isObjectClause()) {

                  if (clsMap.containsKey(icl.getStClauseID())) continue;

                  final InsurancePolicyClausulesView iclx = new InsurancePolicyClausulesView();

                  iclx.setStClauseID(icl.getStClauseID());
                  iclx.setStDescription(icl.getStDescription());
                  iclx.setStWording(icl.getStWording());
                  iclx.setStActiveFlag(icl.getStActiveFlag());
                  iclx.setStPolicyID(icl.getStPolicyID());
                  iclx.setStRateType(icl.getStRateType());
                  iclx.setDbRate(icl.getDbRate());
                  iclx.setStLevel(icl.getStLevel());

                  iclx.markNew();

                  cls.add(iclx);
               }
            }
         }

         // auto delete when master clause is not active anymore
         for (int i = cls.size()-1; i >=0 ; i--) {
            InsurancePolicyClausulesView ccl = (InsurancePolicyClausulesView) cls.get(i);

            if (!masterMap.containsKey(ccl.getStClauseID())) cls.delete(i);
         }

         obj.reCalculate();

         dbPremiTotal = BDUtil.add(dbPremiTotal, obj.getDbObjectPremiTotalAmount());
      }

      dbTotalFee = null;
      BigDecimal totalComission = null;
      BigDecimal totalDiscount = null;
      BigDecimal factor = null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

		 
		 
         if (!item.isPremi()) continue;

         item.calculateRateAmount(getDbInsuredAmount());

         dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
      }

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (!item.isDiscount()) continue;

         item.calculateRateAmount(dbPremiTotal);

         totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
      }

      final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

      dbPremiTotalAfterDisc = totalAfterDiscount;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isFee()); else continue;

         item.calculateRateAmount(totalAfterDiscount);

         dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
      }

      dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         item.calculateRateAmount(totalAfterDiscount);

         totalComission = BDUtil.add(totalComission, item.getDbAmount());
		 
		 
		// if(BDUtil.isZeroOrNull(item.getDbTaxAmount())){
		 	if (item.isAutoTaxRate()) {
            if (item.getStTaxCode()!=null)
               item.setDbTaxRate(item.getTax().getDbRate());
               item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
         		//javax.swing.JOptionPane.showMessageDialog(null,"Tax1= "+ item.getDbTaxAmount(),"tax",javax.swing.JOptionPane.CLOSED_OPTION);

         	}else if(!item.isAutoTaxRate()){
         		
         		item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
         		//javax.swing.JOptionPane.showMessageDialog(null,"Tax2= "+ item.getDbTaxAmount(),"tax",javax.swing.JOptionPane.CLOSED_OPTION);

         	}
		 //}

         //if(!item.isComission()) item.setDbTaxAmount(new BigDecimal(0));

      }

      //calculatePPH21Progressive();

//      for (int i = 0; i < details.size(); i++) {
//         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
//
//         if (item.isComission()){
//         		//javax.swing.JOptionPane.showMessageDialog(null,"Comission","tax",javax.swing.JOptionPane.CLOSED_OPTION);
//				item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
//         	}
//         else{
//         	if (item.isAutoTaxRate()){
//           	item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
//         	//javax.swing.JOptionPane.showMessageDialog(null,"tax= "+item.isAutoTaxAmount(),"tax",javax.swing.JOptionPane.CLOSED_OPTION);
//         	}
//         }
//
//         
//      }


      //dbPremiTotal = BDUtil.sub(dbPremiTotal, totalDiscount);

      dbPremiNetto = BDUtil.sub(dbPremiTotalAfterDisc, totalComission);

      /*final GLUtil.GLAccountCache glcache = new GLUtil.GLAccountCache();

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         final String glacc = item.getInsItem().getStGLAccount();

         if(glacc!=null) {
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(glacc);

            if (item.getStEntityID()!=null)
               glc = GLUtil.applyCode(glc, 'Y', item.getEntity().getStGLCode());
            glc = GLUtil.applyCode(glc, 'B', stCostCenterCode);
            glc = GLUtil.applyCode(glc,'X', getPolicyType().getStGLCode());

            final AccountView ac = glcache.getAccountByAccountID(glc);
            if (ac!=null)
               item.setStGLAccountID(ac.getStAccountID());
            item.setStGLAccount(glc);
         }
      }*/
	
		
/*		
      final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

      glApplicator.setCode('X',getStPolicyTypeID());
      glApplicator.setDesc("X",getStPolicyTypeDesc());
      glApplicator.setCode('Y',getStEntityID());
      glApplicator.setDesc("Y",getStEntityName());
      glApplicator.setCode('B',getStCostCenterCode());

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         InsuranceItemsView insItem = item.getInsItem();

         if (insItem==null) throw new RuntimeException("Ins item not found : "+item);

         ARTransactionLineView arTrxLine = insItem.getARTrxLine();

         if (arTrxLine==null) throw new RuntimeException("AR TRX Line not found : "+item);

         final String accode = arTrxLine.getStGLAccount();

         item.setStGLAccountID(glApplicator.getAccountID(accode));
         item.setStGLAccountDesc(glApplicator.getPreviewDesc());

         if (item.isComission()) {
            if (item.getStTaxCode()!=null){
               item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
               item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
            }
         }
      }


      {
         final BigDecimal BD100 = new BigDecimal(100);

         BigDecimal checkInsAmount=null;
         BigDecimal checkPremiAmount=null;

         InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

         BigDecimal totPct = null;
         BigDecimal totTSI = null;

         for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            if (coin.isHoldingCompany()) continue;

            if (!coin.isEntryByPctRate()) {
               final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero)>0;
               BigDecimal pct;

               if (hasAmount)
                  pct = BDUtil.div(dbInsuredAmount, coin.getDbAmount());
               else
                  pct = BDUtil.zero;

               coin.setDbSharePct(BDUtil.getPctFromRate(pct));
            } else {
               coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct())));
            }

            if (coin.isAutoPremi())
               coin.setDbPremiAmount(BDUtil.mul(dbPremiNetto,BDUtil.getRateFromPct(coin.getDbSharePct())));

            coin.setDbHandlingFeeAmount(BDUtil.mul(coin.getDbPremiAmount(),BDUtil.getRateFromPct(coin.getDbHandlingFeeRate())));

            totPct = BDUtil.add(totPct,coin.getDbSharePct());
            totTSI = BDUtil.add(totTSI,coin.getDbAmount());

            checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
            checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());
         }

         if (holdingcoin!=null) {
            holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
            holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
            holdingcoin.setDbPremiAmount(null);
         }

         dbCoinsCheckInsAmount = checkInsAmount;
         dbCoinsCheckPremiAmount = checkPremiAmount;
      }

      reCalculateInstallment();

      if (isStatusEndorse()) recalculateEndorsement();
   }
*/


public void recalculateBasic() throws Exception{
      /*BigDecimal tot=null;
      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

         ip.getInsuranceItem().isComission()

         tot = BDUtil.add(tot,ip.getDbAmount());
      }

      dbAmount = tot;*/

      /*final InsurancePeriodView insurancePeriod = getInsurancePeriod();

      if (insurancePeriod!=null) {
         dtPeriodEnd = insurancePeriod.advance(dtPeriodStart);
         dbPeriodRate = insurancePeriod.getDbYearlyRate();
      } else {*/
         //dbPeriodRate = new BigDecimal(DateUtil.sub(dtPeriodStart, dtPeriodEnd)).divide(new BigDecimal(1000l*60l*60l*24l*366l), 5 ,BigDecimal.ROUND_HALF_UP);
      //}

      if (stPolicyTypeID==null) return;

      loadClausules();
      loadEntities();
      loadDetails();
      loadObjects();

      if (stPolicyTypeID!=null) {

         FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

         if (objectMap!=null) {
            for (int i = 0; i < objects.size(); i++) {
               InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

               DTOList details = objectMap.getDetails();

               for (int p = 0; p < details.size(); p++) {
                  FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                  if (ffd.getStLOV()!=null) {
                     String desc = LOVManager.getInstance().getDescription(
                             (String) obj.getProperty(ffd.getStFieldRef()),
                             ffd.getStLOV());
					 
                     obj.setProperty(ffd.getStFieldRef()+"Desc", desc);
                  }
               }
            }

         }
      }


      if ((objects!=null) && (objects.size()>0)) {

         dbInsuredAmount = null;
         dbInsuredAmountAfterKurs = null;

         for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            //obj.reCalculate2();
            //recalculateTreaty();

			//dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(),dbCurrencyRate));
			dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(),dbCurrencyRate,2));
            dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
            dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());
         }

      }

      if (dbPremiRate!=null) {
         //dbPremiBase = BDUtil.mul(dbInsuredAmount, dbPremiRate);
         //dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate);
         dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate,2);
      }

      dbPremiTotal = dbPremiBase;

      if (clausules!=null)
         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

            icl.recalculate();

            if (icl.isSelected()) {

               if (icl.getDbRate()!=null) {
                  //icl.setDbAmount(BDUtil.mul(icl.getDbRate(), dbPremiBase));
                  icl.setDbAmount(BDUtil.mul(icl.getDbRate(), dbPremiBase,2));
               }

               dbPremiTotal = BDUtil.add(dbPremiTotal, icl.getDbAmount());
            }
            else dbAmount = BDUtil.zero;
         }

      for (int j = 0; j < objects.size(); j++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

         DTOList cls = obj.getClausules();

         if (cls==null) {
            cls = new DTOList();
            obj.setClausules(cls);
         }

         final HashMap clsMap = new HashMap();

         for (int i = 0; i < cls.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) cls.get(i);
            clsMap.put(icl.getStClauseID(),icl);
         }

         final HashMap masterMap = new HashMap();

         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

            if (icl.isSelected()) {
               masterMap.put(icl.getStClauseID(), icl);

               if (icl.isObjectClause()) {

                  if (clsMap.containsKey(icl.getStClauseID())) continue;

                  final InsurancePolicyClausulesView iclx = new InsurancePolicyClausulesView();

                  iclx.setStClauseID(icl.getStClauseID());
                  iclx.setStDescription(icl.getStDescription());
                  iclx.setStWording(icl.getStWording());
                  iclx.setStActiveFlag(icl.getStActiveFlag());
                  iclx.setStPolicyID(icl.getStPolicyID());
                  iclx.setStRateType(icl.getStRateType());
                  iclx.setDbRate(icl.getDbRate());
                  iclx.setStLevel(icl.getStLevel());

                  iclx.markNew();

                  cls.add(iclx);
               }
            }
         }

         // auto delete when master clause is not active anymore
         for (int i = cls.size()-1; i >=0 ; i--) {
            InsurancePolicyClausulesView ccl = (InsurancePolicyClausulesView) cls.get(i);

            if (!masterMap.containsKey(ccl.getStClauseID())) cls.delete(i);
         }

         obj.reCalculate();

         dbPremiTotal = BDUtil.add(dbPremiTotal, obj.getDbObjectPremiTotalAmount());
      }

      dbTotalFee = null;
      BigDecimal totalComission = null;
      BigDecimal totalDiscount = null;
      BigDecimal factor = null;
      BigDecimal totalTax = null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (!item.isPremi()) continue;

         //item.calculateRateAmount(getDbInsuredAmount());

         dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
      }

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (!item.isDiscount()) continue;

         //item.calculateRateAmount(dbPremiTotal);

         totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
      }

      final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

      dbPremiTotalAfterDisc = totalAfterDiscount;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isFee()); else continue;

         //item.calculateRateAmount(totalAfterDiscount);

         dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
      }

      dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

        // item.calculateRateAmount(totalAfterDiscount);

         totalComission = BDUtil.add(totalComission, item.getDbAmount());

         if (item.isAutoTaxRate()) {
            if (item.getStTaxCode()!=null)
               item.setDbTaxRate(item.getTax().getDbRate());
         }
         
        if (item.getStTaxCode()!=null){
               totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
         }

      }

      calculatePPH21Progressive2();
      
      //calculatePPH21Progressive();
      
      calculatePPH23();
      
      //tambahin pengecekan yg punya NPWP dan Tidak Punya NPWP
      /* jika ada NPWP = 2%
       * jika tidak ada NPWP = 4%
       * 
      */

	 
      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if (item.isAutoTaxAmount()){
            //item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
            item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(),2));
         }
      }


      //dbPremiTotal = BDUtil.sub(dbPremiTotal, totalDiscount);

      dbPremiNetto = BDUtil.sub(dbPremiTotalAfterDisc, totalComission);
      
      dbPremiNetto = BDUtil.add(dbPremiNetto,totalTax);

      /*final GLUtil.GLAccountCache glcache = new GLUtil.GLAccountCache();

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         final String glacc = item.getInsItem().getStGLAccount();

         if(glacc!=null) {
            String glc = GLUtil.Chart.getInstance().getChartCodeOnly(glacc);

            if (item.getStEntityID()!=null)
               glc = GLUtil.applyCode(glc, 'Y', item.getEntity().getStGLCode());
            glc = GLUtil.applyCode(glc, 'B', stCostCenterCode);
            glc = GLUtil.applyCode(glc,'X', getPolicyType().getStGLCode());

            final AccountView ac = glcache.getAccountByAccountID(glc);
            if (ac!=null)
               item.setStGLAccountID(ac.getStAccountID());
            item.setStGLAccount(glc);
         }
      }*/


      final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

      glApplicator.setCode('X',getStPolicyTypeID());
      glApplicator.setDesc("X",getStPolicyTypeDesc());
      glApplicator.setCode('Y',getStEntityID());
      glApplicator.setDesc("Y",getStEntityName());
      glApplicator.setCode('B',getStCostCenterCode());

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         InsuranceItemsView insItem = item.getInsItem();

         if (insItem==null) throw new RuntimeException("Ins item not found : "+item);

         ARTransactionLineView arTrxLine = insItem.getARTrxLine();

         if (arTrxLine==null) throw new RuntimeException("AR TRX Line not found : "+item);

         final String accode = arTrxLine.getStGLAccount();

         item.setStGLAccountID(glApplicator.getAccountID(accode));
         item.setStGLAccountDesc(glApplicator.getPreviewDesc());

         if (item.isComission()) {
            if (item.getStTaxCode()!=null){
               item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
               item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
            }
         }
      }


      {
         final BigDecimal BD100 = new BigDecimal(100);

         BigDecimal checkInsAmount=null;
         BigDecimal checkPremiAmount=null;

         InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

         BigDecimal totPct = null;
         BigDecimal totTSI = null;
         BigDecimal premiAfterDisc = null;

         for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            if (coin.isHoldingCompany()) continue;

            if (!coin.isEntryByPctRate()) {
               final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero)>0;
               BigDecimal pct;

               if (hasAmount)
                  pct = BDUtil.div(dbInsuredAmount, coin.getDbAmount());
               else
                  pct = BDUtil.zero;

               coin.setDbSharePct(BDUtil.getPctFromRate(pct));
            } else {
               //coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct())));
                 coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct()),2));
            }

            if (coin.isAutoPremi())
               coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal,BDUtil.getRateFromPct(coin.getDbSharePct()),2));
               //coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal,BDUtil.getRateFromPct(coin.getDbSharePct())));
			
			/*
            coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(),BDUtil.getRateFromPct(coin.getDbDiscountRate())));
            premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(),coin.getDbDiscountAmount());
            coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbCommissionRate())));
		    coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbBrokerageRate())));
            coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbHandlingFeeRate())));
		    *///coin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
		    
		    coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(),BDUtil.getRateFromPct(coin.getDbDiscountRate()),2));
            premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(),coin.getDbDiscountAmount());
            coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbCommissionRate()),2));
		    coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbBrokerageRate()),2));
            coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()),2));

			
            totPct = BDUtil.add(totPct,coin.getDbSharePct());
            totTSI = BDUtil.add(totTSI,coin.getDbAmount());
            
            
            //coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct())));

            checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
            //checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());
         }

         if (holdingcoin!=null) {
            holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
            holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
            holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal,BDUtil.getRateFromPct(holdingcoin.getDbSharePct()),2));
            checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
            //checkPremiAmount = BDUtil.add(checkPremiAmount, holdingcoin.getDbPremiAmount());
         }
         
         
         dbCoinsCheckInsAmount = checkInsAmount;
         //dbCoinsCheckPremiAmount = checkPremiAmount;
      }
      
      //tes
      if(getStCoverTypeCode()!=null){
      		boolean isCoas;
      if(getStCoverTypeCode().equalsIgnoreCase("DIRECT")) isCoas = false;
 	  else isCoas = true;

      }

      //end

      reCalculateInstallment();

      if (isStatusEndorse()) recalculateEndorsement();
   }

/*
   private void calculatePPH21Progressive() {

      BigDecimal dbPPH21tot=null;
      
      String [] taxTab = null;
      String [] taxTab2 = null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("1".equalsIgnoreCase(item.getStTaxCode())) {
            dbPPH21tot = BDUtil.add(dbPPH21tot, item.getDbAmount());
         }
      }


      try{
      	      //cari limit
      final SQLUtil S = new SQLUtil();

            try {
				
				int i=0;
				
               final PreparedStatement PS = S.setQuery(
                       "select rate1,rate2 from ins_rates_big where rate_class = 'TAX21'"
               );

               final ResultSet RS = PS.executeQuery();
               
               ResultSetMetaData rsmd = RS.getMetaData();
               int iColumnSize = 0;
               int iRowSize = 0;
				
				if(RS.next()){
					iColumnSize = rsmd.getColumnCount();
					taxTab = new String[iColumnSize];
					taxTab2 = new String[iColumnSize];
					while(RS.next()){
               		taxTab[i] = String.valueOf(RS.getBigDecimal(1));
               		taxTab2[i] = String.valueOf(RS.getBigDecimal(2)); 
               		i++;
               		
                  }
				}
               
                  
            } finally {
               S.release();
            }
      }catch (Exception e) {
         throw new RuntimeException(e);
      }


      //
      BigDecimal taxAmount = null;

      BigDecimal dbPPH21totS = dbPPH21tot;

      for (int i = 0; i < taxTab.length; i++) {
         String [] t = taxTab;
         String [] t2 = taxTab2;

         BigDecimal amt = null;

         BigDecimal lim = new BigDecimal(t[0]);

         if (Tools.compare(lim,BDUtil.zero)<0) lim=dbPPH21tot;

         if (Tools.compare(dbPPH21tot, lim)<0) {
            amt=dbPPH21tot;
         } else {
            amt = lim;
         }

         //taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t2[0])));
         
         if(hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t2[0])));
		 }else if(!hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t2[0])),new BigDecimal(1.20)));
		 }

         dbPPH21tot = BDUtil.sub(dbPPH21tot, amt);

         if (Tools.compare(dbPPH21tot,BDUtil.zero)<=0) break;
      }

      BigDecimal actPct = BDUtil.div(taxAmount, dbPPH21totS);

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("1".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
      }

   }
*/
    private void calculatePPH21Progressive2() {

      BigDecimal dbPPH21tot=null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("1".equalsIgnoreCase(item.getStTaxCode())||
         	"4".equalsIgnoreCase(item.getStTaxCode())||
         	"7".equalsIgnoreCase(item.getStTaxCode())) {
            dbPPH21tot = BDUtil.add(dbPPH21tot, item.getDbAmount());
         }
      }

      /*

25.000.000 x 5%     =    1.250.000
50.000.000 x 10%   =    5.000.000
100.000.000 x 15%  = 15.000.000
200.000.000 x 35%  = 70.000.000
125.000.000 x 35% = 43.750.000
      */

      String[][] taxTab = new String [][] {
         {"50000000","0.05"},
         {"250000000","0.15"},
         {"500000000","0.25"},
         {"-1","0.30"},
      };

      BigDecimal taxAmount = null;

      BigDecimal dbPPH21totS = dbPPH21tot;

      for (int i = 0; i < taxTab.length; i++) {
         String [] t = taxTab[i];

         BigDecimal amt = null;

         BigDecimal lim = new BigDecimal(t[0]);

         if (Tools.compare(lim,BDUtil.zero)<0) lim=dbPPH21tot;

         if (Tools.compare(dbPPH21tot, lim)<0) {
            amt=dbPPH21tot;
         } else {
            amt = lim;
         }
			
		 if(hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t[1]),2));
		 }else if(!hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]),2),new BigDecimal(1.20)));
		 }
         //javax.swing.JOptionPane.showMessageDialog(null,"taxAmount= "+ taxAmount,"Eror",javax.swing.JOptionPane.DEFAULT_OPTION);


         dbPPH21tot = BDUtil.sub(dbPPH21tot, amt);

         if (Tools.compare(dbPPH21tot,BDUtil.zero)<=0) break;
      }

      BigDecimal actPct = BDUtil.div(taxAmount, dbPPH21totS,5);

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("1".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("4".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("7".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("1".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
         
         if ("4".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
         
         if ("7".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
      }

   }
   
   private void calculatePPH23() {

      BigDecimal dbPPH23tot=null;
      BigDecimal taxAmount = null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("2".equalsIgnoreCase(item.getStTaxCode())||
         	"5".equalsIgnoreCase(item.getStTaxCode())||
         	"8".equalsIgnoreCase(item.getStTaxCode())) {
            dbPPH23tot = BDUtil.add(dbPPH23tot, item.getDbAmount());
         }
         
         
      
         if(hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, new BigDecimal(0.02),2));
		 }else if(!hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, new BigDecimal(0.04),2));	     
		 }
      }
      
      

      /*

25.000.000 x 5%     =    1.250.000
50.000.000 x 10%   =    5.000.000
100.000.000 x 15%  = 15.000.000
200.000.000 x 35%  = 70.000.000
125.000.000 x 35% = 43.750.000
      */
	/*
      String[][] taxTab = new String [][] {
         {"50000000","0.05"},
         {"250000000","0.15"},
         {"500000000","0.25"},
         {"-1","0.30"},
      };

      BigDecimal taxAmount = null;

      BigDecimal dbPPH21totS = dbPPH21tot;

      for (int i = 0; i < taxTab.length; i++) {
         String [] t = taxTab[i];

         BigDecimal amt = null;

         BigDecimal lim = new BigDecimal(t[0]);

         if (Tools.compare(lim,BDUtil.zero)<0) lim=dbPPH21tot;

         if (Tools.compare(dbPPH21tot, lim)<0) {
            amt=dbPPH21tot;
         } else {
            amt = lim;
         }
			
		 if(hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t[1])));
		 }else if(!hasNPWP()){
		 	taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1])),new BigDecimal(1.20)));
		 }
         

         dbPPH21tot = BDUtil.sub(dbPPH21tot, amt);

         if (Tools.compare(dbPPH21tot,BDUtil.zero)<=0) break;
      }*/
      BigDecimal dbPPH23totS = dbPPH23tot;

      BigDecimal actPct = BDUtil.div(taxAmount, dbPPH23totS);

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         if (item.isComission()); else continue;

         if ("2".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("5".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("8".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate()) {
            item.setDbTaxRate(actPct);
         }
         
         if ("2".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
         
         if ("5".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
         
         if ("8".equalsIgnoreCase(item.getStTaxCode()) && item.isAutoTaxRate() && item.isAutoTaxAmount()) {
            item.setDbTaxAmount(taxAmount);
         }
      }

   }


   public InsurancePolicyCoinsView getHoldingCoin() {

      if (holdingCoin==null) {
         final DTOList coins = getCoins();
         for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coinsView = (InsurancePolicyCoinsView) coins.get(i);

            if (coinsView.isHoldingCompany()) {
               holdingCoin = coinsView;
            }
         }
      }

      return holdingCoin;
   }

   private void recalculateEndorsement() {
      dbInsuredAmountEndorse = BDUtil.sub(dbInsuredAmount,getParentPolicy().getDbInsuredAmount());
   	  
   }

   public InsurancePolicyView getParentPolicy() {
      return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
   }
 

   public InsurancePeriodView getInsurancePeriod() {
      return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
   }

   private void reCalculateInstallment() {
      installment = new DTOList();

      if (getLgInstallmentPeriods()==null) return;

      final long n = getLgInstallmentPeriods().longValue();

      final InsurancePeriodView instPeriod = getInstallmentPeriod();

      final BigDecimal periodAmount = BDUtil.div(dbPremiNetto, new BigDecimal(n));

      final BigDecimal roundingErr = BDUtil.sub(dbPremiNetto, BDUtil.mul(periodAmount, new BigDecimal(n)));

      Date perDate = dtPeriodStart;

      if (perDate==null) return;

      for (int i=1;i<=n;i++) {
         final InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();

         if (i==0)
            for (int j = 0; j < details.size(); j++) {
               InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(j);

               if (item.isFee()) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), item.getDbAmount()));
            }

         inst.setDbAmount(periodAmount);
         inst.setDtDueDate(perDate);

         if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
         if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), dbTotalFee));

         installment.add(inst);

         if (instPeriod!=null)
            perDate = instPeriod.advance(perDate);

      }
   }

   public DTOList getInstallment() {
      return installment;
   }

   public void setInstallment(DTOList installment) {
      this.installment = installment;
   }

   public InsurancePeriodView getInstallmentPeriod() {
      return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
   }

   public DTOList getCoins() {
      if (coins==null) loadCoins();
      return coins;
   }

   public void setCoins(DTOList coins) {
      this.coins = coins;
   }

   public DTOList getCovers() {
      return covers;
   }

   public void setCovers(DTOList covers) {
      this.covers = covers;
   }
   
   public DTOList getCoveragereins() {
      return coveragereins;
   }

   public void setCoveragereins(DTOList coveragereins) {
      this.coveragereins = coveragereins;
   }

   public DTOList getSuminsureds() {
      return suminsureds;
   }

   public void setSuminsureds(DTOList suminsureds) {
      this.suminsureds = suminsureds;
   }

   public InsurancePolicyEntityView getOwner() {
      if (owner==null) {
         if (entities!=null)
            for (int i = 0; i < entities.size(); i++) {
               InsurancePolicyEntityView ent = (InsurancePolicyEntityView) entities.get(i);
               if (ent.isPolicyOwner()) {
                  owner = ent; break;
               }
            }
      }
      return owner;
   }

   public void setOwner(InsurancePolicyEntityView owner) {
      this.owner = owner;
   }

   public DTOList getEntities() {
      loadEntities();
      return entities;
   }

   public void loadEntities() {
      if (!isAutoLoadEnabled()) return;
      try {
         if(entities == null)
            entities = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_entity where pol_id = ?",
                    new Object [] {stPolicyID},
                    InsurancePolicyEntityView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setEntities(DTOList entities) {
      this.entities = entities;
   }

   public DTOList getClausules() {
      loadClausules();
      return clausules;
   }

   public String getStEntityName() {
      final EntityView ent = getEntity();

      if (ent==null) return null;

      return ent.getStEntityName();
   }

   public EntityView getEntity() {

      if (entity!=null)
         if (!Tools.isEqual(entity.getStEntityID(), stEntityID)) entity=null;

      if (entity==null)
         entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

      return entity;
   }
   
   public EntityView getEntity2(String stEntID) {

      if (entity!=null)
         if (!Tools.isEqual(entity.getStEntityID(), stEntID)) entity=null;

      if (entity==null)
         entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);

      return entity;
   }

   public void loadClausules() {
      //if (!isAutoLoadEnabled()) return;
      try {
         if (clausules == null && stPolicyTypeID!=null) {
            clausules = ListUtil.getDTOListFromQuery(
                    "   select " +
                    "      a.*,b.description,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default " +
                    "   from " +
                    "      ins_clausules b " +
                    "      left join ins_pol_clausules a on " +
                    "         a.ins_clause_id = b.ins_clause_id" +
                    "         and a.pol_id = ? " +
                    "         and a.ins_pol_obj_id is null" +
                    "   where b.pol_type_id = ? and b.active_flag = 'Y'" +
                    "   order by b.shortdesc" ,
                    new Object [] {stPolicyID, stPolicyTypeID},
                    InsurancePolicyClausulesView.class
            );

            for (int i = 0; i < clausules.size(); i++) {
               InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
               if (icl.getStPolicyID()!=null) icl.select(); else {
                  icl.setDbRate(icl.getDbRateDefault());
                  icl.markNew();
               }
               icl.setStSelectedFlag(icl.isSelected()?"Y":"N");
            }

            if (isNew()) {
               for (int i = 0; i < clausules.size(); i++) {
                  InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                  if (Tools.isYes(icl.getStDefaultFlag())) {
                     icl.setStSelectedFlag("Y");
                     icl.select();
                  }
               }
            }

         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setClausules(DTOList clausules) {
      this.clausules = clausules;
   }


   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
      stPolicyTypeDesc = null;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStPolicyNo() {
      return stPolicyNo;
   }

   public void setStPolicyNo(String stPolicyNo) {
      this.stPolicyNo = stPolicyNo;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public String getStPostedFlag() {
      return stPostedFlag;
   }

   public void setStPostedFlag(String stPostedFlag) {
      this.stPostedFlag = stPostedFlag;
   }

   public DTOList getClaimItems() {
      loadClaimItems();
      return claimItems;
   }

   private void loadClaimItems() {
      try {
         if (claimItems==null)
            claimItems = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_items where pol_id = ? and item_class='CLM'",
                    new Object [] {stPolicyID},
                    InsurancePolicyItemsView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

   }

   public void setClaimItems(DTOList claimItems) {
      this.claimItems = claimItems;
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
         if (details==null)
            details = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_items where pol_id = ? and item_class='PRM'",
                    new Object [] {stPolicyID},
                    InsurancePolicyItemsView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
      this.stPolicyTypeDesc = stPolicyTypeDesc;
   }

   public String getStPolicyTypeDesc() {
      if (stPolicyTypeID==null) return null;
      if (stPolicyTypeDesc == null) {
         final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
         if (ptype!=null)
            stPolicyTypeDesc = ptype.getStDescription();

         if (stPolicySubTypeID!=null) {
            final InsurancePolicySubTypeView polsubtype = (InsurancePolicySubTypeView) DTOPool.getInstance().getDTO(InsurancePolicySubTypeView.class, stPolicySubTypeID);
            if (polsubtype!=null)
               stPolicyTypeDesc += " / "+polsubtype.getStDescription();
         }
      }

      return stPolicyTypeDesc;
   }

   public void setStPolicySubTypeID(String stPolicySubTypeID) {
      this.stPolicySubTypeID = stPolicySubTypeID;
   }

   public String getStPolicySubTypeID() {
      return stPolicySubTypeID;
   }

   public void setDbPremiBase(BigDecimal dbPremiBase) {
      this.dbPremiBase = dbPremiBase;
   }

   public BigDecimal getDbPremiBase() {
      return dbPremiBase;
   }

   public void setDbPremiTotal(BigDecimal dbPremiTotal) {
      this.dbPremiTotal = dbPremiTotal;
   }

   public BigDecimal getDbPremiTotal() {
      return dbPremiTotal;
   }

   public void setDbPremiRate(BigDecimal dbPremiRate) {
      this.dbPremiRate = dbPremiRate;
   }

   public BigDecimal getDbPremiRate() {
      return dbPremiRate;
   }

   public void setObjects(DTOList objects) {
      this.objects = objects;
   }

   public DTOList getObjects() {
      loadObjects();
      return objects;
   }

   public void loadObjects() {
      try {
         getClObjectClass();
         if (objects == null) {
            objects = ListUtil.getDTOListFromQuery(
                    "select * from "+DTOCache.getTableName(clObjectClass)+" where pol_id  = ?",
                    new Object [] {stPolicyID},
                    clObjectClass
            );

            for (int i = 0; i < objects.size(); i++) {
               InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

               obj.setStPolicyID(this.getStPolicyID());
               //obj.setPolicy(this);
            }
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public InsurancePolicyObjectView createObject() {
      try {
         getClObjectClass();

         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView) clObjectClass.newInstance();

         obj.markNew();

         return obj;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setDbInsuredAmount(BigDecimal dbInsuredAmount) {
      this.dbInsuredAmount = dbInsuredAmount;
   }

   public BigDecimal getDbInsuredAmount() {
      return dbInsuredAmount;
   }

   public String getStPeriodLength() {
      final long l = getLgPeriodLengthDays();

      if (l==-1) return "?";

      return l+"";
   }

   public long getLgPeriodLengthDays() {
      if (dtPeriodStart==null || dtPeriodEnd==null) return -1;

      return (dtPeriodEnd.getTime()-dtPeriodStart.getTime())/(1000l*60l*60l*24l);
   }

   public void setDtPolicyDate(Date dtPolicyDate) {
      this.dtPolicyDate = dtPolicyDate;
   }

   public Date getDtPolicyDate() {
      return dtPolicyDate;
   }

   public void setDbPremiNetto(BigDecimal dbPremiNetto) {
      this.dbPremiNetto = dbPremiNetto;
   }

   public BigDecimal getDbPremiNetto() {
      return dbPremiNetto;
   }

   public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
      this.dbCurrencyRate = dbCurrencyRate;
   }

   public BigDecimal getDbCurrencyRate() {
      return dbCurrencyRate;
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
      //WebFinLOVRegistry.getInstance().setStCostC(stCostCenterCode);
   }

   public String getStCostCenterCode() {
   	   //WebFinLOVRegistry.getInstance().setStCostC(stCostCenterCode);
      return stCostCenterCode;
   }
   
   public void setStCostCenterCode2(String stCostCenterCode) {
      this.stCostCenterCode2 = stCostCenterCode;
      //WebFinLOVRegistry.getInstance().setStCostC(stCostCenterCode);
   }

   public static String getStCostCenterCode2() {
   	   //WebFinLOVRegistry.getInstance().setStCostC(stCostCenterCode);
      return stCostCenterCode2;
   }

   public void setStConditionID(String stConditionID) {
      this.stConditionID = stConditionID;
   }

   public String getStConditionID() {
      return stConditionID;
   }

   public void setStRiskCategoryID(String stRiskCategoryID) {
      this.stRiskCategoryID = stRiskCategoryID;
   }

   public static String getStRiskCategoryID() {
      return stRiskCategoryID;
   }

   public void setStCoverTypeCode(String stCoverTypeCode) {
      this.stCoverTypeCode = stCoverTypeCode;
   }

   public String getStCoverTypeCode() {
      return stCoverTypeCode;
   }

   public void loadCoins() {
      try {
         if (coins==null) {
            coins=ListUtil.getDTOListFromQuery(
                    "   select " +
                    "      *" +
                    "   from" +
                    "      ins_pol_coins" +
                    "   where" +
                    "      policy_id = ?",
                    new Object [] {getStPolicyID()},
                    InsurancePolicyCoinsView.class
            );
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public BigDecimal getDbCoinsCheckInsAmount() {
      return dbCoinsCheckInsAmount;
   }

   public void setDbCoinsCheckInsAmount(BigDecimal dbCoinsCheckInsAmount) {
      this.dbCoinsCheckInsAmount = dbCoinsCheckInsAmount;
   }

   public BigDecimal getDbCoinsCheckPremiAmount() {
      return dbCoinsCheckPremiAmount;
   }

   public void setDbCoinsCheckPremiAmount(BigDecimal dbCoinsCheckPremiAmount) {
      this.dbCoinsCheckPremiAmount = dbCoinsCheckPremiAmount;
   }

   public String getStProductionOnlyFlag() {
      return stProductionOnlyFlag;
   }

   public void setStProductionOnlyFlag(String stProductionOnlyFlag) {
      this.stProductionOnlyFlag = stProductionOnlyFlag;
   }

   public boolean isProductionMode() {
      return Tools.isYes(getStProductionOnlyFlag());
   }

   public String getStCustomerName() {
      return stCustomerName;
   }

   public void setStCustomerName(String stCustomerName) {
      this.stCustomerName = stCustomerName;
   }

   public String getStCustomerAddress() {
      return stCustomerAddress;
   }

   public void setStCustomerAddress(String stCustomerAddress) {
      this.stCustomerAddress = stCustomerAddress;
   }

   public String getStMasterPolicyID() {
      return stMasterPolicyID;
   }

   public void setStMasterPolicyID(String stMasterPolicyID) {
      this.stMasterPolicyID = stMasterPolicyID;
   }

   public String getStProducerID() {
      return stProducerID;
   }

   public void setStProducerID(String stProducerID) {
      this.stProducerID = stProducerID;
   }

   public String getStProducerName() {
      return stProducerName;
   }

   public void setStProducerName(String stProducerName) {
      this.stProducerName = stProducerName;
   }

   public String getStProducerEntName() {
      return stProducerEntName;
   }

   public void setStProducerEntName(String stProducerEntName) {
      this.stProducerEntName = stProducerEntName;
   }

   public String getStProducerAddress() {
      return stProducerAddress;
   }

   public void setStProducerAddress(String stProducerAddress) {
      this.stProducerAddress = stProducerAddress;
   }

   public DTOList getDeductibles() {
      loadDeductibles();
      return deductibles;
   }

   private void loadDeductibles() {
      try {
         if(deductibles==null)
            deductibles = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_deduct where pol_id = ? and ins_pol_obj_id is null",
                    new Object [] {stPolicyID},
                    InsurancePolicyDeductibleView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setDeductibles(DTOList deductibles) {
      this.deductibles = deductibles;
   }

   public String getStPolicyTypeGroupID() {
      return stPolicyTypeGroupID;
   }

   public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
      this.stPolicyTypeGroupID = stPolicyTypeGroupID;
   }

   public BigDecimal getDbPremiTotalAfterDisc() {
      return dbPremiTotalAfterDisc;
   }

   public void setDbPremiTotalAfterDisc(BigDecimal dbPremiTotalAfterDisc) {
      this.dbPremiTotalAfterDisc = dbPremiTotalAfterDisc;
   }

   public BigDecimal getDbTotalDue() {
      return dbTotalDue;
   }

   public void setDbTotalDue(BigDecimal dbTotalDue) {
      this.dbTotalDue = dbTotalDue;
   }

   public boolean isLeader() {

      final InsuranceCoverSourceView cs = getCoverSource();

      if (cs==null) return false;

      return cs.isLeader();
   }

   public InsuranceCoverSourceView getCoverSource() {
      return (InsuranceCoverSourceView) DTOPool.getInstance().getDTO(InsuranceCoverSourceView.class,stCoverTypeCode);
   }
   
   public InsurancePolicyObjDefaultView getRiskCategory() {
      return (InsurancePolicyObjDefaultView) DTOPool.getInstance().getDTORO(InsurancePolicyObjDefaultView.class,stPolicyID);
   }

   public String getStInsurancePeriodID() {
      return stInsurancePeriodID;
   }

   public void setStInsurancePeriodID(String stInsurancePeriodID) {
      this.stInsurancePeriodID = stInsurancePeriodID;
   }

   public String getStInstallmentPeriodID() {
      return stInstallmentPeriodID;
   }

   public void setStInstallmentPeriodID(String stInstallmentPeriodID) {
      this.stInstallmentPeriodID = stInstallmentPeriodID;
   }

   public Long getLgInstallmentPeriods() {
      return lgInstallmentPeriods;
   }

   public void setLgInstallmentPeriods(Long lgInstallmentPeriods) {
      this.lgInstallmentPeriods = lgInstallmentPeriods;
   }

   public DTOList getArinvoices() {
      loadARInvoices();
      return arinvoices;
   }

   private void loadARInvoices() {
      try {

         arinvoices = ListUtil.getDTOListFromQuery(
                 "      select " +
                 "         a.*," +
                 "         (" +
                 "            select max(receipt_date) from ar_receipt_lines c, ar_receipt b\n" +
                 "            where c.ar_invoice_id=a.ar_invoice_id and c.receipt_id=b.ar_receipt_id" +
                 "         ) as payment_date" +
                 "      from " +
                 "         ar_invoice   a" +
                 "      where " +
                 "         ar_trx_type_id = ? and attr_pol_id = ?",
                 new Object [] {getCoverSource().getStARTransactionTypeID(), getStParentID()},
                 ARInvoiceView.class
         );

      } catch (NullPointerException e) {

         arinvoices = new DTOList();
         
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setArinvoices(DTOList arinvoices) {
      this.arinvoices = arinvoices;
   }

   public BigDecimal getDbPeriodRate() {
      return dbPeriodRate;
   }

   public void setDbPeriodRate(BigDecimal dbPeriodRate) {
      this.dbPeriodRate = dbPeriodRate;
   }

   public FlexFieldHeaderView getSPPAFF() {
      final FlexFieldHeaderView flexFieldHeaderView = (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "SPPAH_" + stPolicyTypeID);
      return flexFieldHeaderView;
   }

   public FlexFieldHeaderView getClaimFF() {
      final FlexFieldHeaderView flexFieldHeaderView = (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "CLAIMH_" + stPolicyTypeID);
      return flexFieldHeaderView;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStReference2() {
      return stReference2;
   }

   public void setStReference2(String stReference2) {
      this.stReference2 = stReference2;
   }

   public String getStReference3() {
      return stReference3;
   }

   public void setStReference3(String stReference3) {
      this.stReference3 = stReference3;
   }

   public String getStReference4() {
      return stReference4;
   }

   public void setStReference4(String stReference4) {
      this.stReference4 = stReference4;
   }

   public String getStReference5() {
      return stReference5;
   }

   public void setStReference5(String stReference5) {
      this.stReference5 = stReference5;
   }

   public String getStReference6() {
      return stReference6;
   }

   public void setStReference6(String stReference6) {
      this.stReference6 = stReference6;
   }

   public String getStReference7() {
      return stReference7;
   }

   public void setStReference7(String stReference7) {
      this.stReference7 = stReference7;
   }

   public String getStReference8() {
      return stReference8;
   }

   public void setStReference8(String stReference8) {
      this.stReference8 = stReference8;
   }

   public String getStReference9() {
      return stReference9;
   }

   public void setStReference9(String stReference9) {
      this.stReference9 = stReference9;
   }

   public String getStReference10() {
      return stReference10;
   }

   public void setStReference10(String stReference10) {
      this.stReference10 = stReference10;
   }

   public String getStReference11() {
      return stReference11;
   }

   public void setStReference11(String stReference11) {
      this.stReference11 = stReference11;
   }

   public String getStReference12() {
      return stReference12;
   }

   public void setStReference12(String stReference12) {
      this.stReference12 = stReference12;
   }

   public Date getDtReference1() {
      return dtReference1;
   }

   public void setDtReference1(Date dtReference1) {
      this.dtReference1 = dtReference1;
   }

   public Date getDtReference2() {
      return dtReference2;
   }

   public void setDtReference2(Date dtReference2) {
      this.dtReference2 = dtReference2;
   }

   public Date getDtReference3() {
      return dtReference3;
   }

   public void setDtReference3(Date dtReference3) {
      this.dtReference3 = dtReference3;
   }

   public Date getDtReference4() {
      return dtReference4;
   }

   public void setDtReference4(Date dtReference4) {
      this.dtReference4 = dtReference4;
   }

   public String getStClaimNo() {
      return stClaimNo;
   }

   public void setStClaimNo(String stClaimNo) {
      this.stClaimNo = stClaimNo;
   }

   public Date getDtClaimDate() {
      return dtClaimDate;
   }

   public void setDtClaimDate(Date dtClaimDate) {
      this.dtClaimDate = dtClaimDate;
   }

   public String getStClaimCauseID() {
      return stClaimCauseID;
   }

   public void setStClaimCauseID(String stClaimCauseID) {
      this.stClaimCauseID = stClaimCauseID;
   }

   public String getStClaimCauseDesc() {
      return stClaimCauseDesc;
   }

   public void setStClaimCauseDesc(String stClaimCauseDesc) {
      this.stClaimCauseDesc = stClaimCauseDesc;
   }

   public String getStClaimEventLocation() {
      return stClaimEventLocation;
   }

   public void setStClaimEventLocation(String stClaimEventLocation) {
      this.stClaimEventLocation = stClaimEventLocation;
   }

   public String getStClaimPersonID() {
      return stClaimPersonID;
   }

   public void setStClaimPersonID(String stClaimPersonID) {
      this.stClaimPersonID = stClaimPersonID;
   }

   public String getStClaimPersonName() {
      return stClaimPersonName;
   }

   public void setStClaimPersonName(String stClaimPersonName) {
      this.stClaimPersonName = stClaimPersonName;
   }

   public String getStClaimPersonAddress() {
      return stClaimPersonAddress;
   }

   public void setStClaimPersonAddress(String stClaimPersonAddress) {
      this.stClaimPersonAddress = stClaimPersonAddress;
   }

   public String getStClaimPersonStatus() {
      return stClaimPersonStatus;
   }

   public void setStClaimPersonStatus(String stClaimPersonStatus) {
      this.stClaimPersonStatus = stClaimPersonStatus;
   }

   public BigDecimal getDbClaimAmountEstimate() {
      return dbClaimAmountEstimate;
   }

   public void setDbClaimAmountEstimate(BigDecimal dbClaimAmountEstimate) {
      this.dbClaimAmountEstimate = dbClaimAmountEstimate;
   }

   public String getStClaimCurrency() {
      return stClaimCurrency;
   }

   public void setStClaimCurrency(String stClaimCurrency) {
      this.stClaimCurrency = stClaimCurrency;
   }

   public String getStClaimLossStatus() {
      return stClaimLossStatus;
   }

   public void setStClaimLossStatus(String stClaimLossStatus) {
      this.stClaimLossStatus = stClaimLossStatus;
   }

   public String getStClaimBenefit() {
      return stClaimBenefit;
   }

   public void setStClaimBenefit(String stClaimBenefit) {
      this.stClaimBenefit = stClaimBenefit;
   }

   public String getStClaimDocuments() {
      return stClaimDocuments;
   }

   public void setStClaimDocuments(String stClaimDocuments) {
      this.stClaimDocuments = stClaimDocuments;
   }

   public Date getDtReference5() {
      return dtReference5;
   }

   public void setDtReference5(Date dtReference5) {
      this.dtReference5 = dtReference5;
   }

   public BigDecimal getDbReference1() {
      return dbReference1;
   }

   public void setDbReference1(BigDecimal dbReference1) {
      this.dbReference1 = dbReference1;
   }

   public BigDecimal getDbReference2() {
      return dbReference2;
   }

   public void setDbReference2(BigDecimal dbReference2) {
      this.dbReference2 = dbReference2;
   }

   public BigDecimal getDbReference3() {
      return dbReference3;
   }

   public void setDbReference3(BigDecimal dbReference3) {
      this.dbReference3 = dbReference3;
   }

   public BigDecimal getDbReference4() {
      return dbReference4;
   }

   public void setDbReference4(BigDecimal dbReference4) {
      this.dbReference4 = dbReference4;
   }

   public BigDecimal getDbReference5() {
      return dbReference5;
   }

   public void setDbReference5(BigDecimal dbReference5) {
      this.dbReference5 = dbReference5;
   }

   public BigDecimal getDbTotalFee() {
      return dbTotalFee;
   }

   public void setDbTotalFee(BigDecimal dbTotalFee) {
      this.dbTotalFee = dbTotalFee;
   }

   public Date getDtEndorseDate() {
      return dtEndorseDate;
   }

   public void setDtEndorseDate(Date dtEndorseDate) {
      this.dtEndorseDate = dtEndorseDate;
   }

   public String getStEffectiveFlag() {
      return stEffectiveFlag;
   }

   public void setStEffectiveFlag(String stEffectiveFlag) {
      this.stEffectiveFlag = stEffectiveFlag;
   }

   public String getStClaimStatusDesc() {
      return FinCodec.ClaimStatus.getLookUp().getComboDesc(stClaimStatus);
   }

   public String getStClaimStatus() {
      return stClaimStatus;
   }

   public void setStClaimStatus(String stClaimStatus) {
      this.stClaimStatus = stClaimStatus;
   }

   public String getStParentID() {
      return stParentID;
   }

   public void setStParentID(String stParentID) {
      this.stParentID = stParentID;
   }

   public String getStStatusDesc() {
      String stat = (String) FinCodec.PolicyStatus.getLookUp().getDescription(stStatus);

      if (isStatusClaim()) return stat+"/"+getStClaimStatus();

      return stat;
   }

   public String getStStatus() {
      return stStatus;
   }

   public void setStStatus(String stStatus) {
      this.stStatus = stStatus;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public boolean isActive() {
      return Tools.isYes(stActiveFlag);
   }

   public String getStNextStatus() {
      return stNextStatus;
   }

   public void setStNextStatus(String stNextStatus) {
      this.stNextStatus = stNextStatus;
   }

   public String getStSPPANo() {
      return stSPPANo;
   }

   public void setStSPPANo(String stSPPANo) {
      this.stSPPANo = stSPPANo;
   }

   private String getStCurrentStatus() {
      return stNextStatus==null?stStatus:stNextStatus;
   }

   public boolean isStatusDraft() {return FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusPolicy() {return FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusEndorse() {return FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusClaim() {return FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusCancel() {return FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusSPPA() {return FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusRenewal() {return FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStCurrentStatus());}
   public boolean isStatusClaimPLA() {return FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());}
   public boolean isStatusClaimDLA() {return FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());}
   public boolean isStatusClaimEndorse() {return FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStCurrentStatus());}

  
   public void generatePolicyNo() throws Exception {
      String coasCode = (String)FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

      final EntityView entity = getEntity();

      if (entity==null) throw new Exception("You must select customer for this policy");

      String custCategory = entity.getStCategory1();

      custCategory = getDigit(custCategory,1);

      final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(),'0',2);

      String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
      String counterKey =
              DateUtil.getMonth2Digit(getDtPolicyDate())+
              year2Digit;

      final RegionView reg = getRegion();

      if (reg==null) throw new RuntimeException("Unable to get region (code="+getStRegionID()+")");

      final String ccCode = getDigit(reg.getStCostCenterCode(),2);
      final String regCode = getDigit(reg.getStRegionCode(),2);

      //  ABCCDDEEFFGGHHHHII
      //  040320??1006000300
      //  12345678901234567890
      //  01234567890123456789


      stPolicyNo =
      		  "99"+
              coasCode+ // A
              custCategory+ // B
              getDigit(policyType2Digit,2)+ // C
              ccCode+ // D
              regCode+ // E
              counterKey+ // Fg
              StringTools.leftPad(String.valueOf(IDFactory.createNumericID(policyType2Digit+year2Digit+ccCode,1)),'0',4)+ //H
              "00" //I
              ;
   }
   
   public void generatePLANo() throws Exception {
      String custCategory = entity.getStCategory1();

      custCategory = getDigit(custCategory,1);

      final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(),'0',2);

      String year2Digit = DateUtil.getYear2Digit(new Date());
      String month2Digit = DateUtil.getMonth2Digit(new Date());
      String counterKey =
              DateUtil.getMonth2Digit(getDtPolicyDate())+
              year2Digit;

      final RegionView reg = getRegion();

      if (reg==null) throw new RuntimeException("Unable to get region (code="+getStRegionID()+")");

      final String ccCode = getDigit(reg.getStCostCenterCode(),2);
      final String regCode = getDigit(reg.getStRegionCode(),2);

      //  LKS/21/21/1108/519
      //  040320??1006000300
      //  12345678901234567890
      //  01234567890123456789
	  
	  stPLANo = "LKS/"+ getDigit(policyType2Digit,2) +"/"+
	  			ccCode +"/"+ month2Digit + year2Digit +"/"+
	  			StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);



   }
   
      public void generateDLANo() throws Exception {
      
      String custCategory = entity.getStCategory1();

      custCategory = getDigit(custCategory,1);

      final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(),'0',2);

      String year2Digit = DateUtil.getYear2Digit(new Date());
      String month2Digit = DateUtil.getMonth2Digit(new Date());
      String counterKey =
              DateUtil.getMonth2Digit(getDtPolicyDate())+
              year2Digit;

      final RegionView reg = getRegion();

      if (reg==null) throw new RuntimeException("Unable to get region (code="+getStRegionID()+")");

      final String ccCode = getDigit(reg.getStCostCenterCode(),2);
      final String regCode = getDigit(reg.getStRegionCode(),2);

      //  LKS/21/21/1108/519
      //  040320??1006000300
      //  12345678901234567890
      //  01234567890123456789
	  
	  stDLANo = "LKP/"+ getDigit(policyType2Digit,2) +"/"+
	  			ccCode +"/"+ month2Digit + year2Digit +"/"+
	  			StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);

   }

   private RegionView getRegion() {
      final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegionID);

      return reg;
   }

   private String getDigit(String code, int i) {
      if ((code==null) || (code.length()<1)) code="";

      code=code+"000000000000000000";

      code=code.substring(0,i);

      return code;
   }

   public void generateEndorseNo() {

      final char[] policyno = stPolicyNo.toCharArray();

      final String enos = stPolicyNo.substring(16,18);

      int eNo = Integer.parseInt(enos);

      eNo+=1;

      final String z = StringTools.leftPad(String.valueOf(eNo),'0',2);

      final char[] ze = z.toCharArray();

      policyno[16]=ze[0];
      policyno[17]=ze[1];

      stPolicyNo = new String(policyno);

      /*final int n = policyno.length-1;

      if (policyno[n]=='9') policyno[n]='A';
      else if (policyno[n]=='Z') policyno[n]='a';
      else if (policyno[n]=='z') throw new RuntimeException("Running out endorsement code");
      else
         policyno[n]++;

      stPolicyNo = new String(policyno);*/
   }
   
   
      public void generateClaimEndorseDLA() {

      final char[] dlano = stDLANo.toCharArray();

      final String enos = stDLANo.substring(16,19);

      int eNo = Integer.parseInt(enos);

      eNo+=1;

      final String z = StringTools.leftPad(String.valueOf(eNo),'0',3);

      final char[] ze = z.toCharArray();

      dlano[16]=ze[0];
      dlano[17]=ze[1];
      dlano[18]=ze[2];

      if(stDLANo.endsWith("A"))
      		stDLANo = stDLANo +"B";
      else if(stDLANo.endsWith("B"))
      		stDLANo = stDLANo +"C";
      else if(stDLANo.endsWith("C"))
      		stDLANo = stDLANo +"D";
      else
      		stDLANo = stDLANo +"A";

	  /*
      final int n = dlano.length-1;

      if (dlano[n]=='9') dlano[n]='A';
      else if (dlano[n]=='Z') dlano[n]='a';
      else if (dlano[n]=='z') throw new RuntimeException("Running out endorsement code");
      else
         dlano[n]++;

      stDLANo = new String(dlano);*/
   }


   public boolean isEffective() {
      return Tools.isYes(stEffectiveFlag);
   }

   public boolean isPosted() {
      return Tools.isYes(stPostedFlag);
   }

   public String getStEndorseNotes() {
      return stEndorseNotes;
   }

   public void setStEndorseNotes(String stEndorseNotes) {
      this.stEndorseNotes = stEndorseNotes;
   }

   public String getStPrintCode() {
      return stPrintCode;
   }

   public void setStPrintCode(String stPrintCode) {
      this.stPrintCode = stPrintCode;
   }

   public String getStRootID() {
      return stRootID;
   }

   public void setStRootID(String stRootID) {
      this.stRootID = stRootID;
   }

   public BigDecimal getDbInsuredAmountEndorse() {
      return dbInsuredAmountEndorse;
   }

   public void setDbInsuredAmountEndorse(BigDecimal dbInsuredAmountEndorse) {
      this.dbInsuredAmountEndorse = dbInsuredAmountEndorse;
   }

   public String getStPeriodBaseID() {
      return stPeriodBaseID;
   }

   public void setStPeriodBaseID(String stPeriodBaseID) {
      this.stPeriodBaseID = stPeriodBaseID;
   }

   public BigDecimal getDbPeriodRateBefore() {
      return dbPeriodRateBefore;
   }

   public void setDbPeriodRateBefore(BigDecimal dbPeriodRateBefore) {
      this.dbPeriodRateBefore = dbPeriodRateBefore;
   }

   public String getStPeriodBaseBeforeID() {
      return stPeriodBaseBeforeID;
   }

   public void setStPeriodBaseBeforeID(String stPeriodBaseBeforeID) {
      this.stPeriodBaseBeforeID = stPeriodBaseBeforeID;
   }

   public String getDbPeriodRateDesc() {
      final BigDecimal baseu = getPeriodBase().getDbBaseUnit();

      String pr = String.valueOf(getDbPeriodRate());

      pr = ConvertUtil.removeTrailing(pr);

      if (baseu.longValue()==100) return pr+" %";

      return pr +" / " + baseu;
   }

   private PeriodBaseView getPeriodBase() {
      return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, stPeriodBaseID);
   }

   public BigDecimal getDbPeriodRateFactor() {

      final PeriodBaseView periodBase = getPeriodBase();

      if (periodBase==null) return null;

      final BigDecimal baseu = periodBase.getDbBaseUnit();

      if (dbPeriodRate==null || baseu==null) return null;

      return dbPeriodRate.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
   }

   public BigDecimal getDbPeriodRateBeforeFactor() {

      final PeriodBaseView pbb = getPeriodBaseBefore();

      if(pbb==null) return null;

      final BigDecimal baseu = pbb.getDbBaseUnit();

      if (dbPeriodRateBefore==null || baseu==null) return null;

      return dbPeriodRateBefore.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
   }

   public String getStPeriodRateBeforeDesc() {
      final PeriodBaseView periodBaseBefore = getPeriodBaseBefore();

      if (periodBaseBefore == null) return null;

      final BigDecimal baseu = periodBaseBefore.getDbBaseUnit();
      String be4 = String.valueOf(getDbPeriodRateBefore());

      be4=ConvertUtil.removeTrailing(be4);

      if (baseu.longValue()==100) {
         return be4+" %";
      }

      return be4 +" / " + baseu;

   }

   private PeriodBaseView getPeriodBaseBefore() {
      return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, stPeriodBaseBeforeID);
   }

   public String getStPremiumFactorID() {
      return stPremiumFactorID;
   }

   public void setStPremiumFactorID(String stPremiumFactorID) {
      this.stPremiumFactorID = stPremiumFactorID;
   }

   public InsurancePremiumFactorView getPremiumFactor() {
      return (InsurancePremiumFactorView) DTOPool.getInstance().getDTO(InsurancePremiumFactorView.class, stPremiumFactorID);
   }

   public String getStPremiumFactorDesc() {

      if (stPremiumFactorID==null) return "100%";

      return getPremiumFactor().getStPremiumFactorDesc();
   }

   public BigDecimal getDbPremiumFactorValue() {
      if (stPremiumFactorID==null) return BDUtil.one;
      return getPremiumFactor().getDbPremiumFactor();
   }

   public boolean isClaimDLA() {
      return isStatusClaim() && FinCodec.ClaimStatus.DLA.equalsIgnoreCase(stClaimStatus);
   }

   public String getStDLANo() {
      return stDLANo;
   }

   public void setStDLANo(String stDLANo) {
      this.stDLANo = stDLANo;
   }

   public String getStInsuranceTreatyID() {
      return stInsuranceTreatyID;
   }

   public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
      this.stInsuranceTreatyID = stInsuranceTreatyID;
   }

   public boolean hasCoIns() {

      final InsuranceCoverSourceView cs = getCoverSource();

      if (cs==null) return false;

      return cs.isCoins() && cs.isLeader();
   }

   public String getStNomerator() throws Exception {

      if (!nomeratorLoaded) {
         nomeratorLoaded = true;

         SQLUtil S = new SQLUtil();

         try {
            PreparedStatement PS = S.setQuery("select serial_number from ins_prt_log where policy_id = ? order by ins_prt_log_id desc limit 1");
            PS.setString(1, stPolicyID);
            ResultSet RS = PS.executeQuery();
            if (RS.next())
               stNomerator = RS.getString(1);
         } finally {
            S.release();
         }
      }

      return stNomerator;
   }

   public void setStNomerator(String stNomerator) {
      this.stNomerator = stNomerator;
   }

   public String getStCostCenterDesc() {

      final GLCostCenterView gcc = getCostCenter();

      if (gcc==null) return null;

      return gcc.getStDescription();
   }

   private GLCostCenterView getCostCenter() {

      final GLCostCenterView gcc = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class,stCostCenterCode );

      return gcc;

   }

   public BigDecimal getDbTotalDisc() {
      return BDUtil.sub(dbPremiTotal, dbPremiTotalAfterDisc);
   }

   public BigDecimal getDbTotalComm() {

      return BDUtil.sub( dbPremiTotalAfterDisc, dbPremiNetto);

   }

   public String getStClaimLossStatusDesc() {
      return LOVManager.getInstance().getDescription(stClaimLossStatus, "VS_CLAIM_LOSS");
   }

   public String getStClaimBenefitDesc() {
      return LOVManager.getInstance().getDescription(stClaimBenefit, "VS_CLAIM_BENEFIT");
   }

   public boolean isInward() {
      return Tools.isYes(stInwardFlag);
   }

   public boolean isLockTSI() {
      final CustomHandler handler = getHandler();

      if (handler==null) return false;

      return handler.isLockTSI();
   }

   public CustomHandler getHandler() {
      if (getPolicyType()==null) return null;
      return getPolicyType().getHandler();
   }

   public String findTSIPolTypeID(String stPolicyTypeID, String stInsuranceTSIID) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("select ins_tcpt_id from ins_tsicat_poltype where ins_tsi_cat_id=? and pol_type_id=?");

         PS.setString(1,stInsuranceTSIID);
         PS.setString(2,stPolicyTypeID);

         final ResultSet RS = PS.executeQuery();

         if (RS.next()) return RS.getString(1);

         return null;

      } finally {
         S.release();
      }
   }

   public String getStEntityPostalCode() {
      final EntityAddressView entityPrimaryAddress = getEntityPrimaryAddress();

      if (entityPrimaryAddress==null) return null;

      return entityPrimaryAddress.getStPostalCode();
   }

   public EntityAddressView getEntityPrimaryAddress() {
      getEntity();

      if (entity==null) return null;

      final EntityAddressView primaryAddress = entity.getPrimaryAddress();

      return primaryAddress;
   }

   public String getStSPPANoTrace() throws Exception {
      final InsurancePolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

      if (sppa==null) return null;

      return sppa.getStSPPANo();
   }

   private InsurancePolicyView getLink(String status) throws Exception {

      final String sessionCacheKey = "PO_LINK_"+stRootID+"/"+status;

      final ThreadContext trd = ThreadContext.getInstance();
      Object o = trd.get(sessionCacheKey);

      if (o==null) {
         final DTOList l = ListUtil.getDTOListFromQuery(
                          "select * from ins_policy where root_id = ? and status = ? order by pol_id desc limit 1",
                          new Object [] {stRootID, status},
                          InsurancePolicyView.class
                  );

         if (l.size()>0) {
            o=l.get(0);

            trd.put(sessionCacheKey, o);
         } else
            o=Void.class;
      }

      if (o.equals(Void.class)) return null;

      return (InsurancePolicyView)o;
   }

   public Date getDtSPPADateTrace() throws Exception {

      final InsurancePolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

      if (sppa==null) return null;

      return sppa.getDtCreateDate();
   }

   public String getStProposalNo() throws Exception {
       final InsurancePolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

      if (draft==null) return null;

      return draft.getStPolicyNo();

   }

   public Date getStProposalDate() throws Exception {

       final InsurancePolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

      if (draft==null) return null;

      return draft.getDtCreateDate();
   }

   public Object getStObjectDescription(int i) {

      loadObjects();

      if (objects.size()<=i) return null;

      final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

      return o.getStObjectDescription();
   }
   
   public BigDecimal getDbObjectTSIAmount(int i) {

      loadObjects();

      if (objects.size()<=i) return null;

      final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

      return o.getDbObjectInsuredAmount();
   }

   public BigDecimal getDbObjectPremiRate(int n,int i) throws Exception {

      loadObjects();

      if (objects.size()<=i) return null;

      final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(n);

      final InsurancePolicyCoverView cover = o.getCover(i);

      if (cover ==null) return null;

      return cover.getDbRate();
   }

   public BigDecimal getDbComission(int i) {
      loadDetails();

      for (int j = 0; j < details.size(); j++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(j);

         if (item.getInsItem().isComission()) {
            i--;

            if (i<0) return item.getDbAmount();
         }
      }

      return null;
   }
  
   public String getStObjectDescription() {
      return stObjectDescription;
   }

   public void setStObjectDescription(String stObjectDescription) {
      this.stObjectDescription = stObjectDescription;
   }

   public EntityView getHoldingCompany() {
      if (holdingCompany==null) {
         final String hCompany = Parameter.readString("UWRIT_CURRENT_COMPANY");
         holdingCompany = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, hCompany);
      }
      return holdingCompany;
   }
   
   
   
   
   public InsurancePolicyCoverView getCoverageView() {
      return (InsurancePolicyCoverView) DTOPool.getInstance().getDTO(InsurancePolicyCoverView.class, stPolicyID);
   }
   
   public InsurancePolicyCoverReinsView getCoverageReinsView() {
      return (InsurancePolicyCoverReinsView) DTOPool.getInstance().getDTO(InsurancePolicyCoverReinsView.class, stPolicyID);
   }
   
   

   public BigDecimal getDbTotalItemAmount(String stItemCat) {
      getDetails();

      BigDecimal tot =  null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         final boolean inCat = Tools.isEqual(item.getInsItem().getStItemCategory(), stItemCat);

         if (inCat) {
            tot = BDUtil.add(tot, item.getDbAmount());
         }
      }

      return tot;
   }
   
   public BigDecimal getDbTaxAmount2() {
      getDetails();

      BigDecimal tax2 =  null;

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         //final boolean inCat = Tools.isEqual(item.getInsItem().getStItemCategory(), stItemCat);

         //if (inCat) {
            tax2 = item.getDbTaxAmount();
         //}
      }

      return tax2;
   }
   
   
   public BigDecimal getDbTaxAmount3() throws Exception{
      getDetails();

//      BigDecimal tax2 =  null;
//
//      try{
//	  	 list1 = ListUtil.getDTOListFromQuery(
//                "select sum(tax_amount) "+
//				"from ins_pol_items "+
//				"where pol_id=? and item_class='PRM' and tax_code is not null",
//                new Object [] {stPolicyID},
//                 InsurancePolicyItemsView.class
//
//         );
//	  }catch (Exception e) {
//         throw new RuntimeException(e);
//      }
//      
//      InsurancePolicyItemsView tred = (InsurancePolicyItemsView) list1.get(0);
//      
//      tax2 = tred.getDbTaxAmount();
//      return tax2;
      
      ///
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("select sum(tax_amount) "+
										"from ins_pol_items "+
										"where pol_id=? and item_class='PRM' and tax_code is not null");

         PS.setString(1,stPolicyID);
         

         final ResultSet RS = PS.executeQuery();

         if (RS.next()) return RS.getBigDecimal(1);

         return null;

      } finally {
         S.release();
      }
   }
   
   
   public BigDecimal getDbTaxAmount4() throws Exception{
      getDetails();

      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("select sum(tax_amount) "+
										"from ins_pol_items "+
										"where pol_id=? and item_class='PRM' and tax_code is not null");

         PS.setString(1,stPolicyID);
         

         final ResultSet RS = PS.executeQuery();

         if (RS.next()) return RS.getBigDecimal(1);

         return null;

      } finally {
         S.release();
      }
   }
   
   /*
   public DTOList getCoinsList() throws Exception{
      getDetails();

      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("SELECT * "+
										"FROM INS_POL_COINS "+
										"WHERE POLICY_ID=? ");

         PS.setString(1,stPolicyID);
         

         final ResultSet RS = PS.executeQuery();

         if (RS.next()) return RS.getl;

         return null;

      } finally {
         S.release();
      }
   }
   */
   
   public String getStEntId2() {
      getDetails();

      String entId2 = " ";

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         //final boolean inCat = Tools.isEqual(item.getInsItem().getStItemCategory(), stItemCat);

         //if (inCat) {
            entId2 = item.getStEntityID();
         //}
      }

      return entId2;
   }
   

   public BigDecimal getDbClaimDeductionAmount() {
      return dbClaimDeductionAmount;
   }

   public void setDbClaimDeductionAmount(BigDecimal dbClaimDeductionAmount) {
      this.dbClaimDeductionAmount = dbClaimDeductionAmount;
   }

   public BigDecimal getDbClaimAmount() {
      return dbClaimAmount;
   }

   public void setDbClaimAmount(BigDecimal dbClaimAmount) {
      this.dbClaimAmount = dbClaimAmount;
   }

   public BigDecimal getDbClaimAmountApproved() {
      return dbClaimAmountApproved;
   }

   public void setDbClaimAmountApproved(BigDecimal dbClaimAmountApproved) {
      this.dbClaimAmountApproved = dbClaimAmountApproved;
   }

   public String getStPLANo() {
      return stPLANo;
   }

   public void setStPLANo(String stPLANo) {
      this.stPLANo = stPLANo;
   }

   public String getStDLARemark() {
      return stDLARemark;
   }

   public void setStDLARemark(String stDLARemark) {
      this.stDLARemark = stDLARemark;
   }

   public BigDecimal getDbClaimCustAmount() {
      return dbClaimCustAmount;
   }

   public void setDbClaimCustAmount(BigDecimal dbClaimCustAmount) {
      this.dbClaimCustAmount = dbClaimCustAmount;
   }

   public BigDecimal getDbClaimDeductionCustAmount() {
      return dbClaimDeductionCustAmount;
   }

   public void setDbClaimDeductionCustAmount(BigDecimal dbClaimDeductionCustAmount) {
      this.dbClaimDeductionCustAmount = dbClaimDeductionCustAmount;
   }

   public BigDecimal getDbClaimREAmount() {
      return dbClaimREAmount;
   }

   public void setDbClaimREAmount(BigDecimal dbClaimREAmount) {
      this.dbClaimREAmount = dbClaimREAmount;
   }

   public String getStClaimObjectID() {
      return stClaimObjectID;
   }

   public void setStClaimObjectID(String stClaimObjectID) {
      this.stClaimObjectID = stClaimObjectID;
   }

   public InsurancePolicyObjectView getClaimObject() {

      if (stClaimObjectID==null) return null;

      if (claimObject!=null && !Tools.isEqual(stClaimObjectID, claimObject.getStPolicyObjectID())) claimObject=null;

      if (claimObject!=null) return claimObject;

      getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

         if (Tools.isEqual(obj.getStPolicyObjectID(), stClaimObjectID)) {
            claimObject = obj;
            return obj;
         }
      }

      return null;
   }

   public void validate(boolean isApproving) {

      if (isApproving) {
         if (isStatusClaim()) {
         	if(isStatusClaimPLA()){
         		if (getStPLANo()==null) throw new RuntimeException("PLA No required");
         		
         		setStClaimStatus(FinCodec.ClaimStatus.PLA);
         		setStActiveFlag("Y");
         		
         	}else if(isStatusClaimDLA()){
         		if (getStDLANo()==null) throw new RuntimeException("DLA No required");
				
				setStEffectiveClaimFlag("Y");
				setStEffectiveFlag("Y");
				setStApprovedClaimFlag("Y");
				setDtApprovedClaimDate(new Date());
				//setStClaimStatus(FinCodec.ClaimStatus.DLA);
         	}  
         }


         if (isStatusPolicy() || isStatusEndorse()||isStatusRenewal())
            validateTreaty(true);
      }

      if (isModified()) {

         if (isStatusClaim()) {

         } else {
            if (dtPolicyDate!=null) {

               boolean withinCurrentMonth = DateUtil.getDateStr(getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

               if(!withinCurrentMonth) throw new RuntimeException("Invalid policy date");

            }
         }

      }

      if (getDtPeriodEnd()!=null && getDtPeriodStart()!=null) {
         boolean validPeriod = Tools.compare(DateUtil.truncDate(getDtPeriodStart()), DateUtil.truncDate(getDtPeriodEnd()))<=0;

         if (!validPeriod) throw new RuntimeException("Invalid Period Start / End Date");
      }
      
      if(isStatusClaimEndorse()){
      }else{
	      	        if (getDtEndorseDate()!=null) {
	         boolean validEndorseDate = Tools.compare(getDtEndorseDate(), getDtPeriodStart())>=0 &&
	                  Tools.compare(getDtEndorseDate(), getDtPeriodEnd())<=0;
	        			
	         //if (!validEndorseDate)
	         //   throw new RuntimeException("Invalid Endorsement Date");
	      }
      }




   }

   private void validateTreaty(boolean raiseErrors) {

      stRIFinishFlag = null;

      if (!(isStatusEndorse() || isStatusPolicy() || isStatusRenewal())) return;

      DTOList objects = getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

         DTOList treatyDetails = obj.getTreatyDetails();

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

            boolean equalTSI = Tools.isEqual(trd.getDbMemberTreatyTSITotal(), trd.getDbTSIAmount());
				
            if (!equalTSI) {
               if (raiseErrors){
               	    throw new RuntimeException("Alokasi Treaty Salah !");
                }else{
               		return;
               }         
            }

            DTOList shares = trd.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

               if (!ri.isApproved()) {
                  if (raiseErrors)
                     throw new RuntimeException("Alokasi Reasuransi Belum Disetujui !");
                  else
                     return;
               }
            }
         }
      }

      stRIFinishFlag = "Y";

   }

   public String getStRIFinishFlag() {
      return stRIFinishFlag;
   }

   public void setStRIFinishFlag(String stRIFinishFlag) {
      this.stRIFinishFlag = stRIFinishFlag;
   }

   public String getStPrintStamp() {
      return stPrintStamp;
   }

   public void setStPrintStamp(String stPrintStamp) {
      this.stPrintStamp = stPrintStamp;
   }

   public BigDecimal getDbAPComis() {
      return dbAPComis;
   }

   public void setDbAPComis(BigDecimal dbAPComis) {
      this.dbAPComis = dbAPComis;
   }

   public BigDecimal getDbAPComisP() {
      return dbAPComisP;
   }

   public void setDbAPComisP(BigDecimal dbAPComisP) {
      this.dbAPComisP = dbAPComisP;
   }

   public BigDecimal getDbAPBrok() {
      return dbAPBrok;
   }

   public void setDbAPBrok(BigDecimal dbAPBrok) {
      this.dbAPBrok = dbAPBrok;
   }

   public BigDecimal getDbAPBrokP() {
      return dbAPBrokP;
   }

   public void setDbAPBrokP(BigDecimal dbAPBrokP) {
      this.dbAPBrokP = dbAPBrokP;
   }

   public BigDecimal getDbFieldValueByFieldName(String s) {
      return (BigDecimal) getFieldValueByFieldName(s);
   }

   public BigDecimal getDbAttribute(String s) {

      return (BigDecimal) getAttribute(s);
   }

   public String getStRiskClass() {
      getObjects();

      int riskclass = 1;

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

         if (obj.getStRiskClass()==null) continue;

         try {
            int rc = Integer.parseInt(obj.getStRiskClass());
            if (rc<1 || rc>3) throw new RuntimeException("Risk class should be 1 to 3");
            if (rc>riskclass) riskclass=rc;
         } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid risk class value : "+obj.getStRiskClass());
         }

      }

      return String.valueOf(riskclass);
   }
   
   public boolean hasNPWP(){
   		boolean tes = false;
   		 for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
		 
         if (item.isComission()); else continue;
         
         if(item.getStEntityID()!=null){
         	final EntityView ent = getEntity2(item.getStEntityID());
         	if(ent.getStTaxFile()!=null){
         		tes = true;
         	}else if(ent.getStTaxFile()==null){
         		tes = false;
         	}
         }
      }
      return tes;
   }
   
   
   public void checkPayment() throws Exception{
   	  BigDecimal dbAmountSettled = null;
   	  BigDecimal dbAmount = null;
   	  	
   	  DTOList invoice = getArinvoices();
   	  for(int i = 0;i < invoice.size(); i++){
   	  	 ARInvoiceView invoice2 = (ARInvoiceView) invoice.get(i);
   	  	 
   	  	 dbAmountSettled = BDUtil.add(dbAmountSettled,invoice2.getDbAmountSettled());
   	  	 dbAmount = BDUtil.add(dbAmount,invoice2.getDbAmount());
   	  }
   	  
   	  if(Tools.compare(dbAmountSettled,dbAmount) < 0){
   	  	  //belum bayar atau belum lunas
   	  	  setStLunas("N");
   	  }else if(Tools.compare(dbAmountSettled,dbAmount) >= 0){
   	  	  //udah lunas
   	  	  setStLunas("Y");
   	  }
   	   
   }
   
   public boolean canClaimAgain()throws Exception{
   	  final SQLUtil S = new SQLUtil();
      
      boolean cekClaim = false;
      try {
         final PreparedStatement PS = S.setQuery("select claim_loss_status "+
										"FROM ins_policy "+
										"WHERE parent_id=?  and status = 'CLAIM' and claim_effective_flag='Y'");

         PS.setString(1,stPolicyID);
         
         final ResultSet RS = PS.executeQuery();

         if (RS.next()) //cekClaim = true;
         {
	         final String lossStatus = RS.getString("claim_loss_status");
	         
	         final String cekClaimRepeat = checkCanClaimRepeatedly(lossStatus);
	         
	         if(cekClaimRepeat.equalsIgnoreCase("Y"))
	         		cekClaim = true;
         }else{
         	 cekClaim = true;
         }

         return cekClaim;

      } finally {
         S.release();
      }

   }
   
   public String checkCanClaimRepeatedly(String claim_loss)throws Exception{
   	  final SQLUtil S = new SQLUtil();
      
      String cekRepeat = "N";
      try {
         final PreparedStatement PS = S.setQuery("select repeated_claim_flag "+
										"FROM ins_claim_loss "+
										"WHERE claim_loss_code = ?  and pol_type_id = ?");

         PS.setString(1,claim_loss);
         PS.setString(2,stPolicyTypeID);
         
         final ResultSet RS = PS.executeQuery();
         
         if (RS.next()) //cekClaim = true;
         {
         	String repeat_flag = RS.getString("repeated_claim_flag");
	         
	        if(repeat_flag!=null)
	        	if(repeat_flag.equalsIgnoreCase("Y")) cekRepeat = "Y";

         }

         return cekRepeat;

      } finally {
         S.release();
      }

   }
   
   public boolean isLunas(){
   	   return Tools.isYes(stLunas);
   }
   
   public void setStLunas(String stLunas) {
      this.stLunas = stLunas;
   }
   
   public DTOList getCoverage2() {
      loadCoverage2();
      return coverage;
   }

   public void setCoverage2(DTOList coverage) {
      this.coverage = coverage;
   }

   public void loadCoverage2() {
      //if (!isAutoLoadEnabled()) return;
      try {
         if (coverage==null)
            coverage = ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_cover where pol_id = ? ",
                    new Object [] {stPolicyID},
                    InsurancePolicyCoverView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   public void setObjIndex(String objIndex){
     this.objIndex = objIndex;
   }
   
   public String getObjIndex(){
   	  return objIndex;
   }
   
}

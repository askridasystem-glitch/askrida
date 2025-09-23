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
import com.crux.login.model.UserSessionView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.sql.ResultSetMetaData;
import java.lang.reflect.Method;

public class InsurancePolicyParentView extends DTO implements RecordAudit, RecordBackup {
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
    private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyView.class);

    public static String tableName = "ins_policy";
    
    public static String comboFields[] = {"pol_id","pol_no"};

    public static String fieldMap[][] = {
            {"stPolicyID", "pol_id*pk"},
            {"stSPPANo", "sppa_no"},
            {"stPrintCode", "print_code"},
            {"stParentID", "parent_id"},
            {"stPolicyNo", "pol_no"},
            {"stDescription", "description"},
            {"stCurrencyCode", "ccy"},
            {"stPostedFlag", "posted_flag"},
            {"dbAmount", "amount"},
            {"stPolicyTypeID", "pol_type_id"},
            {"dtPeriodStart", "period_start"},
            {"dtPeriodEnd", "period_end"},
            {"stPolicySubTypeID", "pol_subtype_id"},
            {"stPeriodBaseID", "ins_period_base_id"},
            {"stPolicyTypeDesc", "policy_type_desc*n"},
            {"stPeriodBaseBeforeID", "ins_period_base_b4"},
            {"dbPremiBase", "premi_base"},
            {"dbPremiTotal", "premi_total"},
            {"dbPremiTotalAfterDisc", "premi_total_adisc"},
            {"dbTotalDue", "total_due"},
            {"dbTotalFee", "total_fee"},
            {"dbPremiRate", "premi_rate"},
            {"dbPremiNetto", "premi_netto"},
            {"dbInsuredAmount", "insured_amount"},
            {"dbInsuredAmountEndorse", "insured_amount_e"},
            {"dtPolicyDate", "policy_date"},
            {"stBusinessSourceCode", "bus_source"},
            {"stRegionID", "region_id"},
            {"stCaptiveFlag", "captive_flag"},
            {"stInwardFlag", "inward_flag"},
            {"dbCurrencyRate", "ccy_rate"},
            {"stCostCenterCode", "cc_code"},
            {"stEntityID", "entity_id"},
            {"stConditionID", "condition_id"},
            {"stRiskCategoryID", "risk_category_id"},
            {"stCoverTypeCode", "cover_type_code"},
            {"stProductionOnlyFlag", "f_prodmode"},
            {"stCustomerName", "cust_name"},
            {"stProducerName", "prod_name"},
            {"stCustomerAddress", "cust_address"},
            {"stProducerAddress", "prod_address"},
            {"stProducerID", "prod_id"},
            {"stMasterPolicyID", "master_policy_id"},
            {"stPolicyTypeGroupID", "ins_policy_type_grp_id"},
            {"stInsurancePeriodID", "ins_period_id"},
            {"stInstallmentPeriodID", "inst_period_id"},
            {"stInstallmentPeriods", "inst_periods"},
            {"dbPeriodRate", "period_rate"},
            {"dbPeriodRateBefore", "period_rate_before"},
            {"stStatus", "status"},
            {"stActiveFlag", "active_flag"},
            {"dtPremiPayDate", "premi_pay_date"},
            {"stReference1", "ref1"},
            {"stReference2", "ref2"},
            {"stReference3", "ref3"},
            {"stReference4", "ref4"},
            {"stReference5", "ref5"},
            {"stReference6", "ref6"},
            {"stReference7", "ref7"},
            {"stReference8", "ref8"},
            {"stReference9", "ref9"},
            {"stReference10", "ref10"},
            {"stReference11", "ref11"},
            {"stReference12", "ref12"},
            {"dtReference1", "refd1"},
            {"dtReference2", "refd2"},
            {"dtReference3", "refd3"},
            {"dtReference4", "refd4"},
            {"dtReference5", "refd5"},
            {"dbReference1", "refn1"},
            {"dbReference2", "refn2"},
            {"dbReference3", "refn3"},
            {"dbReference4", "refn4"},
            {"dbReference5", "refn5"},
            {"stClaimObjectID", "claim_object_id"},
            {"stClaimNo", "claim_no"},
            {"dtClaimDate", "claim_date"},
            {"stClaimCauseID", "claim_cause"},
            {"stClaimCauseDesc", "claim_cause_desc"},
            {"stClaimEventLocation", "event_location"},
            {"stClaimPersonID", "claim_person_id"},
            {"stClaimPersonName", "claim_person_name"},
            {"stClaimPersonAddress", "claim_person_address"},
            {"stClaimPersonStatus", "claim_person_status"},
            {"dbClaimAmountEstimate", "claim_amount_est"},
            {"stClaimCurrency", "claim_currency"},
            {"stClaimLossStatus", "claim_loss_status"},
            {"stClaimBenefit", "claim_benefit"},
            {"stClaimDocuments", "claim_documents"},
            {"dbClaimDeductionAmount", "claim_ded_amount"},
            {"dbClaimAmount", "claim_amount"},
            {"dbClaimREAmount", "claim_ri_amount"},
            {"dbClaimCustAmount", "claim_cust_amount"},
            {"dbClaimDeductionCustAmount", "claim_cust_ded_amount"},
            {"dbClaimAmountApproved", "claim_amount_approved"},
            {"stDLARemark", "dla_remark"},
            {"dtEndorseDate", "endorse_date"},
            {"stEndorseNotes", "endorse_notes"},
            {"stEffectiveFlag", "effective_flag"},
            {"stClaimStatus", "claim_status"},
            {"stRootID", "root_id"},
            {"stPremiumFactorID", "ins_premium_factor_id"},
            {"stDLANo", "dla_no"},
            {"stPLANo", "pla_no"},
            {"stInsuranceTreatyID", "ins_treaty_id"},
            {"stNDUpdate", "nd_update"},
            {"dbNDComm1", "nd_comm1"},
            {"dbNDComm2", "nd_comm2"},
            {"dbNDComm3", "nd_comm3"},
            {"dbNDComm4", "nd_comm4"},
            {"dbNDBrok1", "nd_brok1"},
            {"dbNDBrok1Pct", "nd_brok1pct"},
            {"dbNDBrok2", "nd_brok2"},
            {"dbNDBrok2Pct", "nd_brok2pct"},
            {"dbNDHFee", "nd_hfee"},
            {"dbNDHFeePct", "nd_hfeepct"},
            {"dbNDSFee", "nd_sfee"},
            {"dbNDPCost", "nd_pcost"},
            {"dbNDPremi1", "nd_premi1"},
            {"dbNDPremi2", "nd_premi2"},
            {"dbNDPremi3", "nd_premi3"},
            {"dbNDPremi4", "nd_premi4"},
            {"dbNDPremiRate1", "nd_premirate1"},
            {"dbNDPremiRate2", "nd_premirate2"},
            {"dbNDPremiRate3", "nd_premirate3"},
            {"dbNDPremiRate4", "nd_premirate4"},
            {"dbNDDisc1", "nd_disc1"},
            {"dbNDDisc2", "nd_disc2"},
            {"dbNDDisc1Pct", "nd_disc1pct"},
            {"dbNDDisc2Pct", "nd_disc2pct"},
            {"stPFXClauses", "pfx_clauses"},
            {"stPFXInterest", "pfx_interest"},
            {"stPFXCoverage", "pfx_coverage"},
            {"stPFXDeductible", "pfx_deductible"},
            {"stPrintStamp", "print_stamp"},
            {"stRIFinishFlag", "f_ri_finish"},
            {"stCoTreatyID", "co_treaty_id"},
            {"dbCurrencyRateTreaty", "ccy_rate_treaty"},
            {"stManualInstallmentFlag", "manual_inst_flag"},
            {"stApprovedWho", "approved_who"},
            {"dtApprovedDate", "approved_date"},
            {"dtDLADate", "dla_date"},
            {"stApprovedClaimNo", "approved_claim_no"},
            {"dtApprovedClaimDate", "claim_approved_date"},
            {"stApprovedClaimFlag", "claim_approved_flag"},
            {"stActiveClaimFlag", "claim_active_flag"},
            {"stEffectiveClaimFlag", "claim_effective_flag"},
            {"stRateMethod", "rate_method"},
            {"stRateMethodDesc", "rate_method_desc"},
            {"dbClaimAmountEndorse", "claim_amount_endorse"},
            {"stCoverNoteFlag", "cover_note_flag"},
            {"stKreasiTypeID", "kreasi_type_id"},
            {"stKreasiTypeDesc", "kreasi_type_desc"},
            {"dbNDTaxComm1", "nd_taxcomm1"},
            {"dbNDTaxComm2", "nd_taxcomm2"},
            {"dbNDTaxComm3", "nd_taxcomm3"},
            {"dbNDTaxComm4", "nd_taxcomm4"},
            {"dbNDTaxBrok1", "nd_taxbrok1"},
            {"dbNDTaxBrok2", "nd_taxbrok2"},
            {"dbNDTaxHFee", "nd_taxhfee"},
            {"stCoinsID", "coinsurer_id"},
            {"stCoinsName", "coinsurer_name"},
            {"stPeriodDesc", "period_desc"},
            {"dtPLADate","pla_date"},
			{"stClaimLetter","claim_letter_no"},
			{"stReinsuranceApprovedWho","ri_approved_who"},
			{"dtReinsuranceApprovedDate","ri_approved_date"},
			{"stDaysLength","days_length"},	
			{"stObjectDescription", "odescription*n"},
			{"dbPremiPaid", "premi_paid*n"},
            {"dbAPComis", "ap_comis*n"},
            {"dbAPComisP", "ap_comis_p*n"},
            {"dbAPBrok", "ap_brok*n"},
            {"dbAPBrokP", "ap_brok_p*n"},
            {"dbAmountCo", "amount_co*n"},
            {"dbPremiAmt", "premi_amt*n"},
            {"stCoinsPolicyNo", "coins_pol_no"},
            {"dbSharePct", "share_pct"},
            {"dbDiskon", "diskon*n"},
            {"dbKomisi", "komisi*n"},
            {"dbBrok", "brok*n"},
            {"dbDiscPremi", "diskon_premi*n"},
            {"dbPremiKo", "premi_koas*n"},
            {"stTreatyType", "treaty_type*n"},
            {"dbTsiReas", "tsi_reas*n"},
            {"dbPremiOR", "premi_or*n"},
            {"dbPremiReas", "premi_reas*n"},
            {"dbTax", "tax*n"},
            {"dbJumlah", "jumlah*n"},
            {"dbPolis", "polis*n"},
            {"dbKlaim", "klaim_koas*n"},
            {"dbBay", "bay*n"},
            {"dbFee", "fee*n"},
            {"dbSal", "sal*n"},
            {"dbTsiAskrida", "tsi_askrida*n"},
            {"dbPremiAskrida", "premi_askrida*n"},
            {"dbTsiKo", "tsi_ko*n"},
            {"dbPremiKoas", "premi_ko*n"},
            {"dbTsiBPDAN", "tsi_bpdan*n"},
            {"dbPremiBPDAN", "premi_bpdan*n"},
            {"dbTsiOR", "tsi_or*n"},
            {"dbTsiQS", "tsi_qs*n"},
            {"dbPremiQS", "premi_qs*n"},
            {"dbTsiSPL", "tsi_spl*n"},
            {"dbPremiSPL", "premi_spl*n"},
            {"dbTsiFAC", "tsi_fac*n"},
            {"dbPremiFAC", "premi_fac*n"},
            {"dbTsiPARK", "tsi_park*n"},
            {"dbPremiPARK", "premi_park*n"},
            {"dbTsiFACO", "tsi_faco*n"},
            {"dbPremiFACO", "premi_faco*n"},
            {"stPeriod", "period*n"},
            {"dbTsiXOL1", "tsi_xol1*n"},
            {"dbPremiXOL1", "premi_xol1*n"},
            {"dbTsiXOL2", "tsi_xol2*n"},
            {"dbPremiXOL2", "premi_xol2*n"},
            {"dbTsiXOL3", "tsi_xol3*n"},
            {"dbPremiXOL3", "premi_xol3*n"},
            {"dbTsiXOL4", "tsi_xol4*n"},
            {"dbPremiXOL4", "premi_xol4*n"},
            {"dbTsiXOL5", "tsi_xol5*n"},
            {"dbPremiXOL5", "premi_xol5*n"},
            {"stPeriod", "period*n"},
            {"dbCommBPDAN", "comm_bpdan*n"},
            {"dbCommOR", "comm_or*n"},
            {"dbCommQS", "comm_qs*n"},
            {"dbCommSPL", "comm_spl*n"},
            {"dbCommFAC", "comm_fac*n"},
            {"dbCommPARK", "comm_park*n"},
            {"dbCommFACO", "comm_faco*n"},
            {"dtConfirmDate","confirm_date"},

    };

    private BigDecimal dbCommBPDAN;
    private BigDecimal dbCommOR;
    private BigDecimal dbCommSPL;
    private BigDecimal dbCommQS;
    private BigDecimal dbCommFAC;
    private BigDecimal dbCommPARK;
    private BigDecimal dbCommFACO;
    private BigDecimal dbTsiXOL1;
    private BigDecimal dbTsiXOL2;
    private BigDecimal dbTsiXOL3;
    private BigDecimal dbTsiXOL4;
    private BigDecimal dbTsiXOL5;
    private BigDecimal dbPremiXOL1;
    private BigDecimal dbPremiXOL2;
    private BigDecimal dbPremiXOL3;
    private BigDecimal dbPremiXOL4;
    private BigDecimal dbPremiXOL5;
    private BigDecimal dbTsiAskrida;
    private BigDecimal dbTsiKo;
    private BigDecimal dbPremiAskrida;
    private BigDecimal dbPremiKoas;
    private BigDecimal dbTsiBPDAN;
    private BigDecimal dbTsiOR;
    private BigDecimal dbTsiSPL;
    private BigDecimal dbTsiQS;
    private BigDecimal dbTsiFAC;
    private BigDecimal dbTsiPARK;
    private BigDecimal dbTsiFACO;
    private BigDecimal dbPremiBPDAN;
    private BigDecimal dbPremiSPL;
    private BigDecimal dbPremiQS;
    private BigDecimal dbPremiFAC;
    private BigDecimal dbPremiPARK;
    private BigDecimal dbPremiFACO;
    private String stPeriod;
    private Date dtPLADate;
	private String stClaimLetter;
	private String stDaysLength;

    private BigDecimal dbPolis;
    private BigDecimal dbKlaim;
    private BigDecimal dbBay;
    private BigDecimal dbFee;
    private BigDecimal dbSal;

    private DTOList cause;

    private BigDecimal dbJumlah;
    private BigDecimal dbTax;
    private String stPeriodDesc;
    private String stKreasiTypeID;
    private String stKreasiTypeDesc;
    private String stCoinsID;
    private String stCoinsName;
    private String stCoinsPolicyNo;
    private BigDecimal dbSharePct;

    private String stTreatyType;
    private BigDecimal dbTsiReas;
    private BigDecimal dbPremiOR;
    private BigDecimal dbPremiReas;

    private Date dtDLADate;
    private String stApprovedClaimNo;
    private Date dtApprovedClaimDate;
    private String stApprovedClaimFlag;
    private String stActiveClaimFlag;
    private String stEffectiveClaimFlag;
    private String stPrintFlag;

    private BigDecimal dbAPComis;
    private BigDecimal dbAPComisP;
    private BigDecimal dbAPBrok;
    private BigDecimal dbAPBrokP;
    private BigDecimal dbAmountCo;
    private BigDecimal dbOwnInsuredAmount;
    private BigDecimal dbAllPremium;
    private BigDecimal dbPremiKo;

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
    
    private BigDecimal dbNDTaxComm1;
    private BigDecimal dbNDTaxComm2;
    private BigDecimal dbNDTaxComm3;
    private BigDecimal dbNDTaxComm4;
    
    private BigDecimal dbNDTaxBrok1;
    private BigDecimal dbNDTaxBrok2;
    
    private BigDecimal dbNDTaxHFee;

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
    private DTOList address;
    private DTOList bayar;
    private DTOList abakreasi;

    private String objIndex;

    private String stLunas;
    private String stCoverNoteFlag;

    private boolean excludeReasMode = false;
    private String stFilePhysic;

    private String stInsuranceItemCategory;

    private BigDecimal dbPremiAmt;
    private BigDecimal dbDiskon;
    private BigDecimal dbKomisi;
    private BigDecimal dbBrok;
    private BigDecimal dbDiscPremi;
    
    private Date dtConfirmDate;

    private DTOList objectsClaim;

    int scale = 0;

    private InsuranceRiskCategoryView riskcat;
    
    public String getStDaysLength() {
        return stDaysLength;
    }

    public void setStDaysLength(String stDaysLength) {
        this.stDaysLength = stDaysLength;
    }

    public String getStInsuranceItemCategory() {
        return stInsuranceItemCategory;
    }

    public void setStInsuranceItemCategory(String stInsuranceItemCategory) {
        this.stInsuranceItemCategory = stInsuranceItemCategory;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public BigDecimal getDbAmountCo() {
        return dbAmountCo;
    }

    public void setDbAmountCo(BigDecimal dbAmountCo) {
        this.dbAmountCo = dbAmountCo;
    }

    public String getStPeriodDesc() {
        return stPeriodDesc;
    }

    public void setStPeriodDesc(String stPeriodDesc) {
        this.stPeriodDesc = stPeriodDesc;
    }

    public BigDecimal getDbClaimAmountEndorse() {
        return dbClaimAmountEndorse;
    }

    public void setDbClaimAmountEndorse(BigDecimal dbClaimAmountEndorse) {
        this.dbClaimAmountEndorse = dbClaimAmountEndorse;
    }

    public String getStCoverNoteFlag() {
        return stCoverNoteFlag;
    }

    public void setStCoverNoteFlag(String stCoverNoteFlag) {
        this.stCoverNoteFlag = stCoverNoteFlag;
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
    private String stInstallmentPeriods;
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
    private BigDecimal dbCurrencyRateTreaty;
    private BigDecimal dbCoinsCheckInsAmount;
    private BigDecimal dbCoinsCheckPremiAmount;
    private BigDecimal dbPeriodRate;
    private BigDecimal dbPeriodRateBefore;
    private BigDecimal dbInsuredAmountAfterKurs;
    private String stPolicySubTypeID;
    public String stCostCenterCode;
    public String stManualInstallmentFlag;
    private String stApprovedWho;
    private Date dtApprovedDate;
    private String stReinsuranceApprovedWho;
    private Date dtReinsuranceApprovedDate;

    public static String stCostCenterCode2;
    private String stStatus;
    private String stNextStatus;
    private String stCoverTypeCode;
    private String stLockCoinsFlag;
    private Class clObjectClass;
    private FormTab tab;

    private DTOList details;
    private DTOList claimDocuments;
    private DTOList policyDocuments;

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
    private boolean isAddPeriodDesc = false;
    private boolean isLampiran = false;
    
    public String getStReinsuranceApprovedWho() {
        return stReinsuranceApprovedWho;
    }

    public void setStReinsuranceApprovedWho(String stReinsuranceApprovedWho) {
        this.stReinsuranceApprovedWho = stReinsuranceApprovedWho;
    }

    public Date getDtReinsuranceApprovedDate() {
        return dtReinsuranceApprovedDate;
    }

    public void setDtReinsuranceApprovedDate(Date dtReinsuranceApprovedDate) {
        this.dtReinsuranceApprovedDate = dtReinsuranceApprovedDate;
    }

    public String getStManualInstallmentFlag() {
        return stManualInstallmentFlag;
    }

    public void setStManualInstallmentFlag(String stManualInstallmentFlag) {
        this.stManualInstallmentFlag = stManualInstallmentFlag;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    public Date getDtApprovedDate() {
        return dtApprovedDate;
    }

    public void setDtApprovedDate(Date dtApprovedDate) {
        this.dtApprovedDate = dtApprovedDate;
    }

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
    
    public DTOList getPolicyDocuments() {
        if (policyDocuments == null && stPolicyID != null && stPolicyTypeID != null)
            policyDocuments = loadPolicyDocuments(stPolicyTypeID, stPolicyID, "POLICY");
        return policyDocuments;
    }

    private static DTOList loadPolicyDocuments(String poltype, String stPolicyID, String documentClass) {
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
                            "      and a.document_class=? ",
                    new Object[]{stPolicyID, poltype, documentClass},
                    InsurancePolicyDocumentView.class
            );

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) docs.setStSelectedFlag("Y");

                docs.setStPolicyID(stPolicyID);
                docs.setStDocumentClass(documentClass);
            }

            return l;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPolicyDocuments(DTOList policyDocuments) {
        this.policyDocuments = policyDocuments;
    }
    
    //end 

    public DTOList getClaimDocuments() {
        if (claimDocuments == null && stPolicyID != null && stPolicyTypeID != null)
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
                            "      and a.document_class=? ",
                    new Object[]{stPolicyID, poltype, documentClass},
                    InsurancePolicyDocumentView.class
            );

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) docs.setStSelectedFlag("Y");

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
            if (treaties == null) {
                treaties = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_treaty where policy_id=?",
                        new Object[]{stPolicyID},
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

        if (clObjectClass == null) {
            final InsurancePolicyTypeView pt = getPolicyType();

            if (pt != null)
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
        if (tab == null) {
            tab = new FormTab();

            tab.add(new FormTab.TabBean(TAB_PREMI, "PREMI", true));
            tab.add(new FormTab.TabBean(TAB_CLAUSULES, "CLAUSULES", true));
            tab.add(new FormTab.TabBean(TAB_OBJECTS, "OBJECTS", true));
            tab.add(new FormTab.TabBean(TAB_OWNER, "OWNER", true));

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

        boolean endorsement = isStatusEndorse()||isStatusEndorseRI();

        Date pstart = dtPeriodStart;

        try {
            if (endorsement) pstart = getParentPolicy().getDtPeriodEnd();
        } catch (Exception e) {
        }

        if (!endorsement) {
            if (pstart != null && dtPeriodEnd != null) {
                long l = dtPeriodEnd.getTime() - pstart.getTime();

                l /= (1000l * 60l * 60l * 24l);

                final BigDecimal periodDays = new BigDecimal(l);

                final BigDecimal periodBase = periodDays.divide(new BigDecimal("366"), 10, BigDecimal.ROUND_HALF_DOWN);

                if (stPeriodBaseID != null) {
                    final BigDecimal baseu = getPeriodBase().getDbBaseUnit();
                    if (baseu != null) {


                        final BigDecimal periodFactor = periodBase.multiply(baseu);

                        if (endorsement) {
                            dbPeriodRate = new BigDecimal(l);
                        } else {
                            dbPeriodRate = periodFactor;
                        }
                        if (Tools.compare(dbPeriodRate, BDUtil.zero) < 0) dbPeriodRate = BDUtil.zero;
                    }
                }

                if (stPremiumFactorID == null) {
                    stPremiumFactorID = InsurancePolicyUtil.getInstance().findPremiumFactor(periodBase);
                }
            }
        } else {
            if (endorsement) {
                Date pend = dtPeriodEnd;

                try {
                    pend = getParentPolicy().getDtPeriodEnd();
                } catch (Exception e) {
                }

                long l = pend.getTime() - dtEndorseDate.getTime();
                l /= (1000l * 60l * 60l * 24l);

                final BigDecimal periodDays = new BigDecimal(l);


                /*Coding  asli sebelum ubah untuk periodBase
                     */
                //final BigDecimal periodBase2 = periodDays2.divide(new BigDecimal("366"), 10, BigDecimal.ROUND_HALF_DOWN);


                //Start update
                BigDecimal periodBase = null;


                if (periodDays.equals(new BigDecimal(0))) {
                    periodBase = new BigDecimal(100);
                } else {
                    periodBase = periodDays.divide(periodDays, 10, BigDecimal.ROUND_HALF_DOWN);

                }
                //End update


                if (stPeriodBaseBeforeID != null) {
                    final BigDecimal baseu = getPeriodBaseBefore().getDbBaseUnit();
                    if (baseu != null) {

                        final BigDecimal periodFactor = periodBase.multiply(baseu);

                        dbPeriodRateBefore = periodFactor;

                        if (Tools.compare(dbPeriodRateBefore, BDUtil.zero) < 0) dbPeriodRateBefore = BDUtil.zero;
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

    public void recalculate() throws Exception {
        recalculateBasic();
        if (isStatusClaim()) recalculateClaim();
       
        recalculateBasic();
       
        	
        if (isStatusClaim()) recalculateClaim();
        if (isStatusEndorse()||isStatusEndorseRI())
            if (stPolicyTypeID.equalsIgnoreCase("21"))
                recalculateEndorsePAKreasi();

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI())
            validateTreaty(false);
    }
    
   

    public void recalculateCoverRI() throws Exception {
        //final DTOList object = getObjects();

        for (int i = 0; i < this.objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);


                final DTOList shares = trdi.getShares();

                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                    final DTOList cover = getCoverage2();
                    for (int l = 0; l < cover.size(); l++) {
                        InsurancePolicyCoverView cover2 = (InsurancePolicyCoverView) cover.get(l);

                        final InsurancePolicyCoverReinsView cvreins2 = new InsurancePolicyCoverReinsView();

                        cvreins2.setStInsuranceCoverPolTypeID(cover2.getStInsuranceCoverPolTypeID());

                        cvreins2.initializeDefaults();

                        cvreins2.setStInsuranceCoverID(cover2.getStInsuranceCoverID());
                        cvreins2.setStCoverCategory(cover2.getStCoverCategory());
                        cvreins2.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

                        cvreins2.markNew();

                        if (ri.getCoverage().size() >= cover.size()) break;

                        ri.getCoverage().add(cvreins2);


                    }

                }

                //perhitungan rincian cover reins baru
                /*
                final DTOList coverPolis = coverage;

                final DTOList cover = ri.getCoverage();
                for(int k = 0; k < cover.size(); k++){
                    InsurancePolicyCoverReinsView cov = (InsurancePolicyCoverReinsView) cover.get(k);

                    if(cov.isEntryInsuredAmount()) cov.setDbInsuredAmount(cov.getDbInsuredAmount());
                    else cov.setDbInsuredAmount(ri.getDbTSIAmount());

                     for(int l=0;l<coverPolis.size();l++){
                       InsurancePolicyCoverView coverPol = (InsurancePolicyCoverView) coverPolis.get(l);



                      if(coverPol.getStInsuranceCoverID().equalsIgnoreCase(cov.getStInsuranceCoverID())){
                           if(cov.isEntryRate()){
                               cov.setDbRate(cov.getDbRate());
                               //cov.setStEntryRateFlag(coverPol.getStEntryRateFlag());
                           }else{
                              cov.setDbRate(coverPol.getDbRate());
                              //cov.setStEntryRateFlag(coverPol.getStEntryRateFlag());
                           }

                           }
                       }

                      //if(cov.isEntryRate()){
                          BigDecimal rateActual = new BigDecimal(0);
                        if(getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual = cov.getDbRatePct();
                        else if(getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual = cov.getDbRateMile();


                          cov.setDbPremi(BDUtil.mul(cov.getDbInsuredAmount(),rateActual,2));
                          cov.setDbPremiNew(cov.getDbPremi());

                          cov.setStMemberEntityID(ri.getStMemberEntityID());
                         cov.setStInsuranceTreatySharesID(ri.getStInsuranceTreatySharesID());
                    // }
                }*/


                //end

            }

        }

    }


    private void recalculateClaim() {

        getClaimItems();

        dbClaimDeductionAmount = null;
        dbClaimDeductionCustAmount = null;
        dbClaimAmount = null;
        dbClaimCustAmount = null;
        dbClaimREAmount = null;

        if (!getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;

        if (claimItems != null)

            for (int i = 0; i < claimItems.size(); i++) {

                InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                final boolean claimg = Tools.isEqual(it.getInsuranceItem().getStItemType(), "CLAIMG");
                final boolean deduct = Tools.isEqual(it.getInsuranceItem().getStItemType(), "DEDUCT");

                final boolean custAmount = claimg || deduct;

                it.calculateRateAmount(getDbClaimAmountEstimate(), scale);

                final boolean chargeable = Tools.isYes(it.getStChargableFlag());


                if (claimg) {
                    //it.setDbAmount(getDbClaimAmountEstimate());
                    if (isStatusClaimPLA())
                        it.setDbAmount(getDbClaimAmountEstimate());
                    else if (isStatusClaimDLA())
                        it.setDbAmount(getDbClaimAmountApproved());
                }

                BigDecimal amt = it.getDbAmount();

                ARTransactionLineView arTrxLine = it.getInsItem().getARTrxLine();

                if (arTrxLine == null)
                    throw new RuntimeException(" Unable to retrieve ARTRXLINE for ins item : " + it.getInsItem().getStInsuranceItemID());

                final boolean neg = arTrxLine.isNegative();

                if (neg) dbClaimDeductionAmount = BDUtil.add(dbClaimDeductionAmount, amt);
                if (neg && custAmount) dbClaimDeductionCustAmount = BDUtil.add(dbClaimDeductionCustAmount, amt);

                if (neg) amt = BDUtil.negate(amt);

                if (chargeable)
                    dbClaimREAmount = BDUtil.add(dbClaimREAmount, amt);

                dbClaimAmount = BDUtil.add(dbClaimAmount, amt);

                if (custAmount)
                    dbClaimCustAmount = BDUtil.add(dbClaimCustAmount, amt);
            }

        if (claimObject != null) {
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
                trd.setDbClaimAmount(BDUtil.div(BDUtil.mul(trd.getDbTSIAmount(), claimTot, scale), totalTSI2));
                trd.setDbClaimAmount(trd.getDbClaimAmount().setScale(0, BigDecimal.ROUND_HALF_DOWN));

                InsuranceTreatyView treatyView = claimObject.getTreaty();

                final DTOList treatyDetails2 = treatyView.getDetails(stPolicyTypeID);

                for (int j = 0; j < treatyDetails2.size(); j++) {
                    InsuranceTreatyDetailView tredetView = (InsuranceTreatyDetailView) treatyDetails2.get(j);


                    if (tredetView.getDbXOLLower() != null)
                        if (trd.getTreatyDetail().isOR()) {
                            if (tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL1")) {
                                if (Tools.compare(trd.getDbClaimAmount(), tredetView.getDbXOLLower()) > 0) {
                                    sisaORXOL = BDUtil.sub(trd.getDbClaimAmount(), tredetView.getDbXOLLower());
                                    trd.setDbClaimAmount(tredetView.getDbXOLLower());
                                }
                            }
                        } else if (trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL1")) {
                            if (tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL1")) {
                                trd.setDbClaimAmount(sisaORXOL);
                                if (Tools.compare(trd.getDbClaimAmount(), tredetView.getDbXOLUpper()) > 0) {
                                    sisaORXOL1 = trd.getDbClaimAmount();
                                    trd.setDbClaimAmount(tredetView.getDbXOLUpper());
                                }
                            }

                        } else if (trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL2")) {
                            if (tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL2")) {
                                trd.setDbClaimAmount(sisaORXOL1);
                                if (Tools.compare(trd.getDbClaimAmount(), tredetView.getDbXOLUpper()) > 0) {
                                    sisaORXOL2 = trd.getDbClaimAmount();
                                    trd.setDbClaimAmount(tredetView.getDbXOLUpper());
                                }
                            }

                        } else if (trd.getTreatyDetail().getStTreatyTypeID().equalsIgnoreCase("XOL3")) {
                            if (tredetView.getStTreatyTypeID().equalsIgnoreCase("XOL3")) {
                                trd.setDbClaimAmount(sisaORXOL2);
                                if (Tools.compare(trd.getDbClaimAmount(), tredetView.getDbXOLUpper()) > 0) {
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
                    ri.setDbClaimAmount(BDUtil.mul(trd.getDbClaimAmount(), BDUtil.getRateFromPct(ri.getDbSharePct()), scale));

                }


            }

            //tes
            {
                try {
                    list2 = ListUtil.getDTOListFromQuery(
                            "select exc_claim_flag from ins_clm_cause" +
                                    " where ins_clm_caus_id=? ",
                            new Object[]{stClaimCauseID},
                            InsuranceClaimCauseView.class

                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                InsuranceClaimCauseView claimCause = (InsuranceClaimCauseView) list2.get(0);


                DTOList coins = getCoins2();

                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

                    //if (co.isHoldingCompany()) continue;

                    //boolean autoCalc = !Tools.isYes(co.getStAutoClaimAmount());
                    if (stPolicyTypeID.equalsIgnoreCase("21")) {
                        if (claimCause.getStExcClaimFlag() != null) {
                            if (claimCause.getStExcClaimFlag().equalsIgnoreCase("Y")) {
                                if (co.isHoldingCompany()) {
                                    co.setDbClaimAmount(BDUtil.zero);
                                } else {
                                    if (isStatusClaimPLA())
                                        co.setDbClaimAmount(getDbClaimAmountEstimate());
                                    else if (isStatusClaimDLA())
                                        co.setDbClaimAmount(getDbClaimAmountApproved());
                                }
                            }


                        } else if (claimCause.getStExcClaimFlag() == null) {

                            if (co.isHoldingCompany()) {
                                if (isStatusClaimPLA())
                                    co.setDbClaimAmount(getDbClaimAmountEstimate());
                                else if (isStatusClaimDLA())
                                    co.setDbClaimAmount(getDbClaimAmountApproved());
                            } else co.setDbClaimAmount(BDUtil.zero);
                        }
                    } else {

                        BigDecimal claimAmt = null;

                        if (isStatusClaimPLA())
                            claimAmt = BDUtil.mul(getDbClaimAmountEstimate(), BDUtil.getRateFromPct(co.getDbSharePct()), scale);
                        else if (isStatusClaimDLA())
                            claimAmt = BDUtil.mul(getDbClaimAmountApproved(), BDUtil.getRateFromPct(co.getDbSharePct()), scale);

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

    

    

    
    
  public BigDecimal getDbCoinsSessionPct()throws Exception{
   final SQLUtil S = new SQLUtil();
   BigDecimal session_pct = null;
   try {
      final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
      
      
      
         final PreparedStatement PS = S.setQuery("select * "+ 
									"from ins_co_scale "+
									"where ( ? between scale_lower and scale_upper) and pol_type_id = ? "+
									"and ( ? >= scale_period_start and ? <= scale_period_end);");
		 
		 if(this.getStCoverTypeCode().equalsIgnoreCase("COINSIN")){
		 	if(this.getDbSharePct()==null) throw new RuntimeException("Share Askrida Belum Diisi");
		 	else PS.setBigDecimal(1, this.getDbSharePct());
		 }else{
		 	PS.setBigDecimal(1, holdingCoin.getDbSharePct());
		 }		
   
         PS.setString(2,this.getStPolicyTypeID());
         S.setParam(3,this.getDtPeriodStart());
         S.setParam(4,this.getDtPeriodStart());
         
         final ResultSet RS = PS.executeQuery();
         
         if (RS.next())
         {
         	session_pct = RS.getBigDecimal("session_pct");
         }
         
         return session_pct;

      }finally{
      		S.release();
      	} 
      
      
   }


    public void recalculateBasicBackup() throws Exception {
        if (stPolicyTypeID == null) return;

        loadClausules();
        loadEntities();
        loadDetails();
        //loadObjects();

        if (!getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;

        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                    DTOList details = objectMap.getDetails();

                    for (int p = 0; p < details.size(); p++) {
                        FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                        if (ffd.getStLOV() != null) {
                            String desc = LOVManager.getInstance().getDescription(
                                    (String) obj.getProperty(ffd.getStFieldRef()),
                                    ffd.getStLOV());

                            obj.setProperty(ffd.getStFieldRef() + "Desc", desc);
                        }
                    }
                }

            }
        }

        if ((objects != null) && (objects.size() > 0)) {

            dbInsuredAmount = null;
            dbInsuredAmountAfterKurs = null;


            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                obj.reCalculate();

                //dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(),dbCurrencyRate));

                dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(), dbCurrencyRate, scale));

                dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
                dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());
            }

        }

        if (dbPremiRate != null) {
            //dbPremiBase = BDUtil.mul(dbInsuredAmount, dbPremiRate);
            //dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate);
            dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate, scale);
        }

        dbPremiTotal = dbPremiBase;

        /*
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
        */

        for (int j = 0; j < objects.size(); j++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

            DTOList cls = obj.getClausules();

            if (cls == null) {
                cls = new DTOList();
                obj.setClausules(cls);
            }

            final HashMap clsMap = new HashMap();

            for (int i = 0; i < cls.size(); i++) {
                InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) cls.get(i);
                clsMap.put(icl.getStClauseID(), icl);
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
                        //iclx.setStDescription2(icl.getStDescription2());

                        iclx.markNew();

                        cls.add(iclx);
                    }
                }
            }

            // auto delete when master clause is not active anymore
            for (int i = cls.size() - 1; i >= 0; i--) {
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

            item.calculateRateAmount(getDbInsuredAmount(), scale);

            dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
        }

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isDiscount()) continue;

            item.calculateRateAmount(dbPremiTotal, scale);

            totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
        }

        final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

        dbPremiTotalAfterDisc = totalAfterDiscount;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isFee()) ;
            else continue;

            item.calculateRateAmount(totalAfterDiscount, scale);

            dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
        }

        dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            item.calculateRateAmount(totalAfterDiscount, scale);

            totalComission = BDUtil.add(totalComission, item.getDbAmount());

            if (item.isAutoTaxRate()) {
                if (item.getStTaxCode() != null)
                    item.setDbTaxRate(item.getTax().getDbRate());
            }

            if (item.getStTaxCode() != null) {
                totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
            }

        }

        //set data polis komisi, bfee, hfee, biaya polis, materai, diskon
        BigDecimal[] komisi = new BigDecimal[4];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isKomisi()) ;
            else continue;

            if (komisi[0] == null) {
                komisi[0] = item.getDbAmount();
                continue;
            }
            if (komisi[1] == null) {
                komisi[1] = item.getDbAmount();
                continue;
            }
            if (komisi[2] == null) {
                komisi[2] = item.getDbAmount();
                continue;
            }
            if (komisi[3] == null) {
                komisi[3] = item.getDbAmount();
                continue;
            }
        }
        dbNDComm1 = komisi[0];
        dbNDComm2 = komisi[1];
        dbNDComm3 = komisi[2];
        dbNDComm4 = komisi[3];

        //if(true) throw new RuntimeException("Komisi 1="+komisi[0]+" , komisi2="+komisi[1]);


        BigDecimal[] brokerfee = new BigDecimal[2];
        BigDecimal[] brokerfeepct = new BigDecimal[2];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isBrokerFee()) ;
            else continue;

            if (brokerfee[0] == null) {
                brokerfee[0] = item.getDbAmount();
                brokerfeepct[0] = item.getDbRatePct();
                continue;
            }
            if (brokerfee[1] == null) {
                brokerfee[1] = item.getDbAmount();
                brokerfeepct[1] = item.getDbRatePct();
                continue;
            }

        }
        dbNDBrok1 = brokerfee[0];
        dbNDBrok2 = brokerfee[1];
        dbNDBrok1Pct = brokerfeepct[0];
        dbNDBrok2Pct = brokerfeepct[1];

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isHandlingFee() || item.isStampFee() || item.isPolicyCost()) {
                if (item.isHandlingFee()) {
                    dbNDHFee = item.getDbAmount();
                    dbNDHFeePct = item.getDbRatePct();
                } else if (item.isStampFee()) {
                    dbNDSFee = item.getDbAmount();
                } else if (item.isPolicyCost()) {
                    dbNDPCost = item.getDbAmount();
                }
            } else {
                continue;
            }
        }

        BigDecimal[] discount = new BigDecimal[2];
        BigDecimal[] discountpct = new BigDecimal[2];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isDiscount()) ;
            else continue;

            if (discount[0] == null) {
                discount[0] = item.getDbAmount();
                discountpct[0] = item.getDbRatePct();
                continue;
            }
            if (discount[1] == null) {
                discount[1] = item.getDbAmount();
                discountpct[1] = item.getDbRatePct();
                continue;
            }

        }
        dbNDDisc1 = discount[0];
        dbNDDisc2 = discount[1];
        dbNDDisc1Pct = discountpct[0];
        dbNDDisc2Pct = discountpct[1];

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

            if (item.isComission()) ;
            else continue;

            if (item.isAutoTaxAmount()) {
                //item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount()));
                item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(), scale));
            }
        }


        //dbPremiTotal = BDUtil.sub(dbPremiTotal, totalDiscount);

        dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);
        //dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);

        //dbPremiNetto = BDUtil.add(dbPremiNetto,totalTax);

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

        glApplicator.setCode('X', getStPolicyTypeID());
        glApplicator.setDesc("X", getStPolicyTypeDesc());
        //glApplicator.setCode('Y',getStEntityID());
        glApplicator.setCode('Y', getEntity().getStGLCode());
        if (getEntity().getStGLCode().equalsIgnoreCase("00000"))
            glApplicator.setDesc("Y", "");
        else
            glApplicator.setDesc("Y", getStEntityName());

        glApplicator.setCode('B', getStCostCenterCode());

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            InsuranceItemsView insItem = item.getInsItem();

            if (insItem == null) throw new RuntimeException("Ins item not found : " + item);

            ARTransactionLineView arTrxLine = insItem.getARTrxLine();

            if (arTrxLine == null) throw new RuntimeException("AR TRX Line not found : " + item);

            final String accode = arTrxLine.getStGLAccount();

            item.setStGLAccountID(glApplicator.getAccountID(accode));
            item.setStGLAccountDesc(glApplicator.getPreviewDesc());

            /*
            if (item.isComission()) {
               if (item.getStTaxCode()!=null){
                  item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
                  item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
               }
            }*/

            if (item.getStEntityID() != null) {
                glApplicator.setCode('Y', item.getEntity().getStGLCode());
                if ("00000".equalsIgnoreCase(item.getEntity().getStGLCode())) {
                    glApplicator.setDesc("Y", "");
                } else {
                    glApplicator.setDesc("Y", item.getEntity().getStEntityName());
                }


                item.setStGLAccountID(glApplicator.getAccountID(accode));
                item.setStGLAccountDesc(glApplicator.getPreviewDesc());

                if (item.isComission()) {
                    if (item.getStTaxCode() != null) {
                        item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
                        item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
                    }
                }
            }


        }


        {
            final BigDecimal BD100 = new BigDecimal(100);

            BigDecimal checkInsAmount = null;
            BigDecimal checkPremiAmount = null;

            InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

            BigDecimal totPct = null;
            BigDecimal totTSI = null;
            BigDecimal premiAfterDisc = null;

            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                if (coin.isHoldingCompany()) continue;

                if (!coin.isEntryByPctRate()) {
                    final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero) > 0;
                    BigDecimal pct;

                    if (hasAmount)
                        pct = BDUtil.div(dbInsuredAmount, coin.getDbAmount());
                    else
                        pct = BDUtil.zero;

                    coin.setDbSharePct(BDUtil.getPctFromRate(pct));
                } else {
                    //coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct())));
                    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }

                if (coin.isAutoPremi())
                    coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                //coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal,BDUtil.getRateFromPct(coin.getDbSharePct())));

                /*
                coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(),BDUtil.getRateFromPct(coin.getDbDiscountRate())));
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(),coin.getDbDiscountAmount());
                coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbCommissionRate())));
                coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbBrokerageRate())));
                coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc,BDUtil.getRateFromPct(coin.getDbHandlingFeeRate())));
                *///coin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));

                coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));


                totPct = BDUtil.add(totPct, coin.getDbSharePct());
                totTSI = BDUtil.add(totTSI, coin.getDbAmount());


                //coin.setDbAmount(BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(coin.getDbSharePct())));

                checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
                //checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());
            }

            if (holdingcoin != null) {
                holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
                holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
                holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoin.getDbSharePct()), scale));
                checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
                //checkPremiAmount = BDUtil.add(checkPremiAmount, holdingcoin.getDbPremiAmount());
                //setDbOwnInsuredAmount(holdingcoin.getDbAmount());
            }


            dbCoinsCheckInsAmount = checkInsAmount;
            //dbCoinsCheckPremiAmount = checkPremiAmount;
        }

        //tes
        if (getStCoverTypeCode() != null) {
            boolean isCoas;
            if (getStCoverTypeCode().equalsIgnoreCase("DIRECT")) isCoas = false;
            else isCoas = true;

        }

        reCalculateInstallment();
        //reCalculateInstallment2();

        if (isStatusEndorse()) recalculateEndorsement();
    }

    public void recalculateBasic() throws Exception {
        if (stPolicyTypeID == null) return;

        loadClausules();
        loadEntities();
        loadDetails();
        //loadObjects();

        if (!getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        if(dtPeriodStart!=null && dtPeriodEnd!=null)
        	setStDaysLength(getStPeriodLength());
		
        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                    DTOList details = objectMap.getDetails();

                    for (int p = 0; p < details.size(); p++) {
                        FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                        if (ffd.getStLOV() != null && !Tools.isYes(ffd.getStRefresh())) {
                            String desc = LOVManager.getInstance().getDescription(
                                    (String) obj.getProperty(ffd.getStFieldRef()),
                                    ffd.getStLOV());

                            obj.setProperty(ffd.getStFieldRef() + "Desc", desc);
                        }
                    }
                }

            }
        }

        if ((objects != null) && (objects.size() > 0)) {

            dbInsuredAmount = null;
            dbInsuredAmountAfterKurs = null;


            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                obj.reCalculate();

                dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(), dbCurrencyRate, scale));
                dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
                dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());
            }
        }

        if (dbPremiRate != null) {
            dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate, scale);
        }

        dbPremiTotal = dbPremiBase;

        for (int j = 0; j < objects.size(); j++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

            DTOList cls = obj.getClausules();

            if (cls == null) {
                cls = new DTOList();
                obj.setClausules(cls);
            }

            final HashMap clsMap = new HashMap();

            for (int i = 0; i < cls.size(); i++) {
                InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) cls.get(i);
                clsMap.put(icl.getStClauseID(), icl);
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
                        //iclx.setStDescription2(icl.getStDescription2());

                        iclx.markNew();

                        cls.add(iclx);
                    }
                }
            }

            // auto delete when master clause is not active anymore
            for (int i = cls.size() - 1; i >= 0; i--) {
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

            item.calculateRateAmount(getDbInsuredAmount(), scale);

            dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
        }

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isDiscount()) continue;

            item.calculateRateAmount(dbPremiTotal, scale);

            totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
        }

        final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

        dbPremiTotalAfterDisc = totalAfterDiscount;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isFee()) ;
            else continue;

            item.calculateRateAmount(totalAfterDiscount, scale);

            dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
        }

        dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            item.calculateRateAmount(totalAfterDiscount, scale);

            totalComission = BDUtil.add(totalComission, item.getDbAmount());

            if (item.isAutoTaxRate()) {
                if (item.getStTaxCode() != null)
                    item.setDbTaxRate(item.getTax().getDbRate());
            }

            if (item.getStTaxCode() != null) {
                totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
            }

        }

        //set data polis komisi, bfee, hfee, biaya polis, materai, diskon
		recalculateFee();

        calculatePPH21Progressive2();

        calculatePPH23();

        //tambahin pengecekan yg punya NPWP dan Tidak Punya NPWP
        /* jika ada NPWP = 2%
         * jika tidak ada NPWP = 4%
         *
        */
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            if (item.isAutoTaxAmount()) {
                item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(), scale));
            }
        }

        dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);

        final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

        glApplicator.setCode('X', getStPolicyTypeID());
        glApplicator.setDesc("X", getStPolicyTypeDesc());
        glApplicator.setCode('Y', getEntity().getStGLCode());
        if (getEntity().getStGLCode().equalsIgnoreCase("00000"))
            glApplicator.setDesc("Y", "");
        else
            glApplicator.setDesc("Y", getStEntityName());

        glApplicator.setCode('B', getStCostCenterCode());

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            InsuranceItemsView insItem = item.getInsItem();

            if (insItem == null) throw new RuntimeException("Ins item not found : " + item);

            ARTransactionLineView arTrxLine = insItem.getARTrxLine();

            if (arTrxLine == null) throw new RuntimeException("AR TRX Line not found : " + item);

            final String accode = arTrxLine.getStGLAccount();

            item.setStGLAccountID(glApplicator.getAccountID(accode));
            item.setStGLAccountDesc(glApplicator.getPreviewDesc());

            if (item.getStEntityID() != null) {
                glApplicator.setCode('Y', item.getEntity().getStGLCode());
                if ("00000".equalsIgnoreCase(item.getEntity().getStGLCode())) {
                    glApplicator.setDesc("Y", "");
                } else {
                    glApplicator.setDesc("Y", item.getEntity().getStEntityName());
                }

                item.setStGLAccountID(glApplicator.getAccountID(accode));
                item.setStGLAccountDesc(glApplicator.getPreviewDesc());

                if (item.isComission()) {
                    if (item.getStTaxCode() != null) {
                        item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
                        item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
                    }
                }
            }
        }

        {
            final BigDecimal BD100 = new BigDecimal(100);

            BigDecimal checkInsAmount = null;
            BigDecimal checkPremiAmount = null;

            InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

            BigDecimal totPct = null;
            BigDecimal totTSI = null;
            BigDecimal premiAfterDisc = null;

            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                if (coin.isHoldingCompany()) continue;

                if (!coin.isEntryByPctRate()) {
                    final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero) > 0;
                    BigDecimal pct;

                    if (hasAmount)
                        pct = BDUtil.div(dbInsuredAmount, coin.getDbAmount());
                    else
                        pct = BDUtil.zero;

                    coin.setDbSharePct(BDUtil.getPctFromRate(pct));
                } else {
                    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }

                if (coin.isAutoPremi())
                    coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));

                coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                if(!"21".equalsIgnoreCase(getStPolicyTypeID())) coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));


                totPct = BDUtil.add(totPct, coin.getDbSharePct());
                totTSI = BDUtil.add(totTSI, coin.getDbAmount());

                checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
            }
	            if (holdingcoin != null) {
	                holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
	                holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
	                if (holdingcoin.isAutoPremi())
	                	holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoin.getDbSharePct()), scale));
	                
	                checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
	            }

            dbCoinsCheckInsAmount = checkInsAmount;
        }
        
        reCalculateInstallment();

        if (isStatusEndorse()||isStatusEndorseRI()) recalculateEndorsement();

   }
    
    
    private void recalculateFee(){
    	BigDecimal[] komisi = new BigDecimal[4];
    	BigDecimal[] taxKomisi = new BigDecimal[4];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isKomisi()) ;
            else continue;

            if (komisi[0] == null) {
                komisi[0] = item.getDbAmount();
                taxKomisi[0] = item.getDbTaxAmount();
                continue;
            }
            if (komisi[1] == null) {
                komisi[1] = item.getDbAmount();
                taxKomisi[1] = item.getDbTaxAmount();
                continue;
            }
            if (komisi[2] == null) {
                komisi[2] = item.getDbAmount();
                taxKomisi[2] = item.getDbTaxAmount();
                continue;
            }
            if (komisi[3] == null) {
                komisi[3] = item.getDbAmount();
                taxKomisi[3] = item.getDbTaxAmount();
                continue;
            }
        }
        dbNDComm1 = komisi[0];
        dbNDComm2 = komisi[1];
        dbNDComm3 = komisi[2];
        dbNDComm4 = komisi[3];
        
        dbNDTaxComm1 = taxKomisi[0];
        dbNDTaxComm2 = taxKomisi[1];
        dbNDTaxComm3 = taxKomisi[2];
        dbNDTaxComm4 = taxKomisi[3];

        BigDecimal[] brokerfee = new BigDecimal[2];
        BigDecimal[] brokerfeepct = new BigDecimal[2];
        BigDecimal[] taxbrokerfee = new BigDecimal[2];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isBrokerFee()) ;
            else continue;

            if (brokerfee[0] == null) {
                brokerfee[0] = item.getDbAmount();
                brokerfeepct[0] = item.getDbRatePct();
                taxbrokerfee[0] = item.getDbTaxAmount();
                continue;
            }
            if (brokerfee[1] == null) {
                brokerfee[1] = item.getDbAmount();
                brokerfeepct[1] = item.getDbRatePct();
                taxbrokerfee[1] = item.getDbTaxAmount();
                continue;
            }

        }
        dbNDBrok1 = brokerfee[0];
        dbNDBrok2 = brokerfee[1];
        dbNDBrok1Pct = brokerfeepct[0];
        dbNDBrok2Pct = brokerfeepct[1];
        
        dbNDTaxBrok1 = taxbrokerfee[0];
        dbNDTaxBrok2 = taxbrokerfee[1];

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isHandlingFee() || item.isStampFee() || item.isPolicyCost()) {
                if (item.isHandlingFee()) {
                    dbNDHFee = item.getDbAmount();
                    dbNDHFeePct = item.getDbRatePct();
                    dbNDTaxHFee = item.getDbTaxAmount();
                } else if (item.isStampFee()) {
                    dbNDSFee = item.getDbAmount();
                } else if (item.isPolicyCost()) {
                    dbNDPCost = item.getDbAmount();
                }
            } else {
                continue;
            }
        }

        BigDecimal[] discount = new BigDecimal[2];
        BigDecimal[] discountpct = new BigDecimal[2];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isDiscount()) ;
            else continue;

            if (discount[0] == null) {
                discount[0] = item.getDbAmount();
                discountpct[0] = item.getDbRatePct();
                continue;
            }
            if (discount[1] == null) {
                discount[1] = item.getDbAmount();
                discountpct[1] = item.getDbRatePct();
                continue;
            }

        }
        dbNDDisc1 = discount[0];
        dbNDDisc2 = discount[1];
        dbNDDisc1Pct = discountpct[0];
        dbNDDisc2Pct = discountpct[1];
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

        BigDecimal dbPPH21tot = null;


        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            if ("1".equalsIgnoreCase(item.getStTaxCode()) ||
                    "4".equalsIgnoreCase(item.getStTaxCode()) ||
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

        String[][] taxTab = new String[][]{
                {"50000000", "0.05"},
                {"250000000", "0.15"},
                {"500000000", "0.25"},
                {"-1", "0.30"},
        };

        BigDecimal taxAmount = null;

        BigDecimal dbPPH21totS = dbPPH21tot;

        for (int i = 0; i < taxTab.length; i++) {
            String[] t = taxTab[i];

            BigDecimal amt = null;

            BigDecimal lim = new BigDecimal(t[0]);

            if (Tools.compare(lim, BDUtil.zero) < 0) lim = dbPPH21tot;

            if (Tools.compare(dbPPH21tot, lim) < 0) {
                amt = dbPPH21tot;
            } else {
                amt = lim;
            }
			
			//cek lagi 
            if (hasNPWP()) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t[1]), scale));
            } else if (!hasNPWP()) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), scale), new BigDecimal(1.20)));
            }

            dbPPH21tot = BDUtil.sub(dbPPH21tot, amt);

            if (Tools.compare(dbPPH21tot, BDUtil.zero) <= 0) break;
        }

        BigDecimal actPct = BDUtil.div(taxAmount, dbPPH21totS, 5);

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

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

        BigDecimal dbPPH23tot = null;
        BigDecimal taxAmount = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            if ("2".equalsIgnoreCase(item.getStTaxCode()) ||
                    "5".equalsIgnoreCase(item.getStTaxCode()) ||
                    "8".equalsIgnoreCase(item.getStTaxCode())) {
                dbPPH23tot = BDUtil.add(dbPPH23tot, item.getDbAmount());
            }

            if (item.getStNPWP()!=null) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, new BigDecimal(0.02), scale));
            } else if (item.getStNPWP()==null) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, new BigDecimal(0.04), scale));
            }
        }

        BigDecimal dbPPH23totS = dbPPH23tot;

        BigDecimal actPct = BDUtil.div(taxAmount, dbPPH23totS);

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

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

        if (holdingCoin == null) {
            final DTOList coins = getCoins2();
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
        dbInsuredAmountEndorse = BDUtil.sub(dbInsuredAmount, getParentPolicy().getDbInsuredAmount());

    }

    public InsurancePolicyView getParentPolicy() {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
    }


    public InsurancePeriodView getInsurancePeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
    }

    public void reCalculateInstallment() throws Exception {
        getInstallment();

        if (installment == null) installment = new DTOList();

        //final long n = getLgInstallmentPeriods().longValue();

        //final int o = getLgInstallmentPeriods().intValue();

        final InsurancePeriodView instPeriod = getInstallmentPeriod();

        //final BigDecimal periodAmount = BDUtil.div(dbPremiNetto, new BigDecimal(n));

        //final BigDecimal roundingErr = BDUtil.sub(dbPremiNetto, BDUtil.mul(periodAmount, new BigDecimal(n)));

        //final BigDecimal periodAmount = BDUtil.div(dbPremiTotalAfterDisc, new BigDecimal(n));

        final BigDecimal periodAmount = BDUtil.div(dbPremiTotalAfterDisc, new BigDecimal(installment.size()));

        //final BigDecimal roundingErr = BDUtil.sub(dbPremiTotalAfterDisc, BDUtil.mul(periodAmount, new BigDecimal(n)));
        final BigDecimal roundingErr = BDUtil.sub(dbPremiTotalAfterDisc, BDUtil.mul(periodAmount, new BigDecimal(installment.size())));

        Date perDate = dtPeriodStart;

        if (perDate == null) return;

        //for (int i=1;i<=n;i++) {

        //final InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();
        BigDecimal total = null;
        for (int i = 0; i < installment.size(); i++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);

            if (Tools.isYes(stManualInstallmentFlag)) {
                BigDecimal amount = inst.getDbAmount();
                inst.setDbAmount(amount);
                inst.setDtDueDate(inst.getDtDueDate());
            } else {
                inst.setDbAmount(periodAmount);
                if (i == 0) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
                if (i == 0) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), dbTotalFee));
                inst.setDtDueDate(perDate);
            }
            /*
                inst.setDbAmount(periodAmount);
                if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
                if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), dbTotalFee));

            inst.setDtDueDate(perDate);

            installment.add(inst);*/

            if (instPeriod != null)
                perDate = instPeriod.advance(perDate);

            total = BDUtil.add(total, inst.getDbAmount());
        }

        //if (Tools.compare(total, BDUtil.add(dbPremiTotal, dbTotalFee)) > 0)
            //throw new RuntimeException("Total Premi Cicilan Lebih Besar Dari Premi Bruto");
    }

    public DTOList getInstallment() throws Exception {
        if (installment == null) loadInstallment();
        return installment;
    }

    public void setInstallment(DTOList installment) {
        this.installment = installment;
    }

    public void loadInstallment() throws Exception {
        try {
            if (installment == null) {
                installment = ListUtil.getDTOListFromQuery(
                        "   select " +
                                "      *" +
                                "   from" +
                                "      ins_pol_installment" +
                                "   where" +
                                "      policy_id = ? ",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyInstallmentView.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InsurancePeriodView getInstallmentPeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
    }

    public DTOList getCoins() {
        if (coins == null) loadCoins2();
        return coins;
    }

    public DTOList getCoins2() {
        if (coins == null) loadCoins();
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
        if (owner == null) {
            if (entities != null)
                for (int i = 0; i < entities.size(); i++) {
                    InsurancePolicyEntityView ent = (InsurancePolicyEntityView) entities.get(i);
                    if (ent.isPolicyOwner()) {
                        owner = ent;
                        break;
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
            if (entities == null)
                entities = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_entity where pol_id = ?",
                        new Object[]{stPolicyID},
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

        if (ent == null) return null;

        return ent.getStEntityName();
    }

    public EntityView getEntity() {

        if (entity != null)
            if (!Tools.isEqual(entity.getStEntityID(), stEntityID)) entity = null;

        if (entity == null)
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return entity;
    }

    public EntityView getEntity2(String stEntID) {

        if (entity != null)
            if (!Tools.isEqual(entity.getStEntityID(), stEntID)) entity = null;

        if (entity == null)
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);

        return entity;
    }

    public void loadClausules() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (clausules == null && stPolicyTypeID != null) {
                clausules = ListUtil.getDTOListFromQuery(
                        "   select " +
                                "      a.*,b.description,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default " +
                                "   from " +
                                "      ins_clausules b " +
                                "      left join ins_pol_clausules a on " +
                                "         a.ins_clause_id = b.ins_clause_id" +
                                "         and a.pol_id = ? " +
                                "         and a.ins_pol_obj_id is null" +
                                "   where b.pol_type_id = ? and cc_code=? and b.active_flag = 'Y'" +
                                "   order by b.shortdesc",
                        new Object[]{stPolicyID, stPolicyTypeID, stCostCenterCode},
                        InsurancePolicyClausulesView.class
                );

                for (int i = 0; i < clausules.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                    if (icl.getStPolicyID() != null) icl.select();
                    else {
                        icl.setDbRate(icl.getDbRateDefault());
                        icl.markNew();
                    }
                    icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
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
            if (claimItems == null)
                claimItems = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='CLM'",
                        new Object[]{stPolicyID},
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
            if (details == null)
                details = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='PRM'",
                        new Object[]{stPolicyID},
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
        if (stPolicyTypeID == null) return null;
        if (stPolicyTypeDesc == null) {
            final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
            if (ptype != null)
                stPolicyTypeDesc = ptype.getStShortDescription();
            //stPolicyTypeDesc = ptype.getStDescription();

            if (stPolicySubTypeID != null) {
                final InsurancePolicySubTypeView polsubtype = (InsurancePolicySubTypeView) DTOPool.getInstance().getDTO(InsurancePolicySubTypeView.class, stPolicySubTypeID);
                if (polsubtype != null)
                    stPolicyTypeDesc += " / " + polsubtype.getStDescription();
            }
        }

        return stPolicyTypeDesc;
    }

    public String getStPolicyTypeDesc2() {
        if (stPolicyTypeID == null) return null;
        if (stPolicyTypeDesc == null) {
            final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
            if (ptype != null)
                stPolicyTypeDesc = ptype.getStDescription();

            if (stPolicySubTypeID != null) {
                final InsurancePolicySubTypeView polsubtype = (InsurancePolicySubTypeView) DTOPool.getInstance().getDTO(InsurancePolicySubTypeView.class, stPolicySubTypeID);
                if (polsubtype != null)
                    stPolicyTypeDesc += " / " + polsubtype.getStDescription();
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

        if (l == -1) return "?";

        return l + "";
    }

    public long getLgPeriodLengthDays() {
        if (dtPeriodStart == null || dtPeriodEnd == null) return -1;

        return (dtPeriodEnd.getTime() - dtPeriodStart.getTime()) / (1000l * 60l * 60l * 24l);
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

    public void setDbCurrencyRateTreaty(BigDecimal dbCurrencyRateTreaty) {
        this.dbCurrencyRateTreaty = dbCurrencyRateTreaty;
    }

    public BigDecimal getDbCurrencyRateTreaty() {
        return dbCurrencyRateTreaty;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode2(String stCostCenterCode) {
        this.stCostCenterCode2 = stCostCenterCode;
    }

    public static String getStCostCenterCode2() {
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

    /*
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
    }*/

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
            if (deductibles == null)
                deductibles = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_deduct where pol_id = ? and ins_pol_obj_id is null",
                        new Object[]{stPolicyID},
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

        if (cs == null) return false;

        return cs.isLeader();
    }

    public boolean isMember() {

        final InsuranceCoverSourceView cs = getCoverSource();

        if (cs == null) return false;

        return cs.isMember();
    }

    public InsuranceCoverSourceView getCoverSource() {
        return (InsuranceCoverSourceView) DTOPool.getInstance().getDTO(InsuranceCoverSourceView.class, stCoverTypeCode);
    }

    public InsurancePolicyObjDefaultView getRiskCategory() {
        return (InsurancePolicyObjDefaultView) DTOPool.getInstance().getDTORO(InsurancePolicyObjDefaultView.class, stPolicyID);
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

    public String getStInstallmentPeriods() {
        return stInstallmentPeriods;
    }

    public void setStInstallmentPeriods(String stInstallmentPeriods) {
        this.stInstallmentPeriods = stInstallmentPeriods;
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
                            "         ar_trx_type_id = ? and attr_pol_id = ? order by a.ar_invoice_id",
                    new Object[]{getCoverSource().getStARTransactionTypeID(), getStPolicyID()},
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

        if (isStatusClaim()) return stat + "/" + getStClaimStatus();

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
        return stNextStatus == null ? stStatus : stNextStatus;
    }

    public boolean isStatusDraft() {
        return FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusPolicy() {
        return FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(getStCurrentStatus());
    }
    
    public boolean isStatusHistory() {
        return FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusEndorse() {
        return FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(getStCurrentStatus());
    }
    
    public boolean isStatusEndorseRI() {
        return FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusClaim() {
        return FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusCancel() {
        return FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusSPPA() {
        return FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusRenewal() {
        return FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusClaimPLA() {
        return FinCodec.ClaimStatus.PLA.equalsIgnoreCase(getStClaimStatus());
    }

    public boolean isStatusClaimDLA() {
        return FinCodec.ClaimStatus.DLA.equalsIgnoreCase(getStClaimStatus());
    }

    public boolean isStatusClaimEndorse() {
        return FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(getStCurrentStatus());
    }
    
    
    public void generatePersetujuanPrinsipNo() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        final EntityView entity = getEntity();

        if (entity == null) throw new Exception("You must select customer for this policy");

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate()) +
                        year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789


        stReference1 =
                        getDigit(policyType2Digit, 2) + // C
                        ccCode + // D
                        regCode + // E
                        counterKey + // Fg
                        StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H
                        "00" //I
                ;
    }


    public void generatePolicyNo() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        final EntityView entity = getEntity();

        if (entity == null) throw new Exception("You must select customer for this policy");

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate()) +
                        year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789


        stPolicyNo =
                        "PI/"+
                        coasCode + // A
                        custCategory + // B
                        getDigit(policyType2Digit, 2) + // C
                        ccCode + // D
                        regCode + // E
                        counterKey + // Fg
                        StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PI_" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H
                        "00" //I
                ;
    }

    public void generatePLANo() throws Exception {
        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate()) +
                        year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        stPLANo = "LKS/" + getDigit(policyType2Digit, 2) + "/" +
                ccCode + "/" + month2Digit + year2Digit + "/" +
                StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim", 1)), '0', 4);


    }

    public void generateDLANo() throws Exception {

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate()) +
                        year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        stDLANo = "LKP/" + getDigit(policyType2Digit, 2) + "/" +
                ccCode + "/" + month2Digit + year2Digit + "/" +
                //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);
                getStPLANo().substring(15);

    }

    public String generateDLANo2() throws Exception {

        String dla = null;

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate()) +
                        year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        dla = "LKP/" + getDigit(policyType2Digit, 2) + "/" +
                ccCode + "/" + month2Digit + year2Digit + "/" +
                //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);
                getStPLANo().substring(15);

        return dla;

    }

    private RegionView getRegion() {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegionID);

        return reg;
    }

    private String getDigit(String code, int i) {
        if ((code == null) || (code.length() < 1)) code = "";

        code = code + "000000000000000000";

        code = code.substring(0, i);

        return code;
    }

    public void generateEndorseNo() {

        final char[] policyno = stPolicyNo.toCharArray();

        final String enos = stPolicyNo.substring(16, 18);

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 2);

        final char[] ze = z.toCharArray();

        policyno[16] = ze[0];
        policyno[17] = ze[1];

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

        final String enos = stDLANo.substring(16, 19);

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 3);

        final char[] ze = z.toCharArray();

        dlano[16] = ze[0];
        dlano[17] = ze[1];
        dlano[18] = ze[2];

        if (stDLANo.endsWith("A"))
            stDLANo = stDLANo + "B";
        else if (stDLANo.endsWith("B"))
            stDLANo = stDLANo + "C";
        else if (stDLANo.endsWith("C"))
            stDLANo = stDLANo + "D";
        else
            stDLANo = stDLANo + "A";

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

        if (baseu.longValue() == 100) return pr + " %";

        return pr + " / " + baseu;
    }

    private PeriodBaseView getPeriodBase() {
        return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, stPeriodBaseID);
    }

    public BigDecimal getDbPeriodRateFactor() {

        final PeriodBaseView periodBase = getPeriodBase();

        if (periodBase == null) return null;

        final BigDecimal baseu = periodBase.getDbBaseUnit();

        if (dbPeriodRate == null || baseu == null) return null;

        return dbPeriodRate.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getDbPeriodRateBeforeFactor() {

        final PeriodBaseView pbb = getPeriodBaseBefore();

        if (pbb == null) return null;

        final BigDecimal baseu = pbb.getDbBaseUnit();

        if (dbPeriodRateBefore == null || baseu == null) return null;

        return dbPeriodRateBefore.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getStPeriodRateBeforeDesc() {
        final PeriodBaseView periodBaseBefore = getPeriodBaseBefore();

        if (periodBaseBefore == null) return null;

        final BigDecimal baseu = periodBaseBefore.getDbBaseUnit();
        String be4 = String.valueOf(getDbPeriodRateBefore());

        be4 = ConvertUtil.removeTrailing(be4);

        if (baseu.longValue() == 100) {
            return be4 + " %";
        }

        return be4 + " / " + baseu;

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

        if (stPremiumFactorID == null) return "100%";

        return getPremiumFactor().getStPremiumFactorDesc();
    }

    public BigDecimal getDbPremiumFactorValue() {
        if (stPremiumFactorID == null) return BDUtil.one;
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
        boolean coins = false;
        final InsuranceCoverSourceView cs = getCoverSource();

        if (cs == null) return false;

        if (cs.isCoins() && cs.isLeader())
            coins = true;

        if (cs.isCoins() && cs.isMember())
            coins = true;

        return coins;
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

        if (gcc == null) return null;

        return gcc.getStDescription();
    }

    private GLCostCenterView getCostCenter() {

        final GLCostCenterView gcc = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return gcc;

    }

    public BigDecimal getDbTotalDisc() {
        return BDUtil.sub(dbPremiTotal, dbPremiTotalAfterDisc);
    }

    public BigDecimal getDbTotalComm() {

        return BDUtil.sub(dbPremiTotalAfterDisc, dbPremiNetto);

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

        if (handler == null) return false;

        return handler.isLockTSI();
    }

    public CustomHandler getHandler() {
        if (getPolicyType() == null) return null;
        return getPolicyType().getHandler();
    }

    public String findTSIPolTypeID(String stPolicyTypeID, String stInsuranceTSIID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select ins_tcpt_id from ins_tsicat_poltype where ins_tsi_cat_id=? and pol_type_id=?");

            PS.setString(1, stInsuranceTSIID);
            PS.setString(2, stPolicyTypeID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;

        } finally {
            S.release();
        }
    }

    public String getStEntityPostalCode() {
        final EntityAddressView entityPrimaryAddress = getEntityPrimaryAddress();

        if (entityPrimaryAddress == null) return null;

        return entityPrimaryAddress.getStPostalCode();
    }

    public EntityAddressView getEntityPrimaryAddress() {
        getEntity();

        if (entity == null) return null;

        final EntityAddressView primaryAddress = entity.getPrimaryAddress();

        return primaryAddress;
    }

    public String getStSPPANoTrace() throws Exception {
        final InsurancePolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

        if (sppa == null) return null;

        return sppa.getStSPPANo();
    }

    private InsurancePolicyView getLink(String status) throws Exception {

        final String sessionCacheKey = "PO_LINK_" + stRootID + "/" + status;

        final ThreadContext trd = ThreadContext.getInstance();
        Object o = trd.get(sessionCacheKey);

        if (o == null) {
            final DTOList l = ListUtil.getDTOListFromQuery(
                    "select * from ins_policy where root_id = ? and status = ? order by pol_id desc limit 1",
                    new Object[]{stRootID, status},
                    InsurancePolicyView.class
            );

            if (l.size() > 0) {
                o = l.get(0);

                trd.put(sessionCacheKey, o);
            } else
                o = Void.class;
        }

        if (o.equals(Void.class)) return null;

        return (InsurancePolicyView) o;
    }

    public Date getDtSPPADateTrace() throws Exception {

        final InsurancePolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

        if (sppa == null) return null;

        return sppa.getDtCreateDate();
    }

    public String getStProposalNo() throws Exception {
        final InsurancePolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

        if (draft == null) return null;

        return draft.getStPolicyNo();

    }

    public Date getStProposalDate() throws Exception {

        final InsurancePolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

        if (draft == null) return null;

        return draft.getDtCreateDate();
    }

    public Object getStObjectDescription(int i) {

        //loadObjects();

        if (objects.size() <= i) return null;

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

        return o.getStObjectDescription();
    }

    public BigDecimal getDbObjectTSIAmount(int i) {

        //loadObjects();

        if (objects.size() <= i) return null;

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

        return o.getDbObjectInsuredAmount();
    }

    public BigDecimal getDbObjectPremiRate(int n, int i) throws Exception {

        //loadObjects();

        if (objects.size() <= i) return null;

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(n);

        final InsurancePolicyCoverView cover = o.getCover(i);

        if (cover == null) return null;

        return cover.getDbRate();
    }

    public BigDecimal getDbComission(int i) {
        loadDetails();

        for (int j = 0; j < details.size(); j++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(j);

            if (item.getInsItem().isComission()) {
                i--;

                if (i < 0) return item.getDbAmount();
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
        if (holdingCompany == null) {
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

        BigDecimal tot = null;

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

        BigDecimal tax2 = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            //final boolean inCat = Tools.isEqual(item.getInsItem().getStItemCategory(), stItemCat);

            //if (inCat) {
            tax2 = item.getDbTaxAmount();
            //}
        }

        return tax2;
    }


    public BigDecimal getDbTaxAmount3() throws Exception {
        getDetails();

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select sum(tax_amount) " +
                    "from ins_pol_items " +
                    "where pol_id=? and item_class='PRM' and tax_code is not null");

            PS.setString(1, stPolicyID);


            final ResultSet RS = PS.executeQuery();

            if (RS.next()) return RS.getBigDecimal(1);

            return null;

        } finally {
            S.release();
        }
    }


    public BigDecimal getDbTaxAmount4(String PolicyID) throws Exception {
        getDetails();

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select coalesce(sum(b.tax_amount*a.ccy_rate),0) " +
                    "from ins_policy a " +
                    "inner join ins_pol_items b on b.pol_id = a.pol_id " +
                    "where b.pol_id=? and b.item_class='PRM' and b.tax_code is not null");

            PS.setString(1, PolicyID);


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

            entId2 = item.getStEntityID();
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

        if (stClaimObjectID == null) return null;

        if (claimObject != null && !Tools.isEqual(stClaimObjectID, claimObject.getStPolicyObjectID()))
            claimObject = null;

        if (claimObject != null) return claimObject;

        //getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            if (Tools.isEqual(obj.getStPolicyObjectID(), stClaimObjectID)) {
                claimObject = obj;
                return obj;
            }
        }

        return null;
    }

    public void validate(boolean isApproving) throws Exception {

        if (isApproving) {
            if (isStatusClaim()) {
                if (isStatusClaimPLA()) {
                    if (getStPLANo() == null) throw new RuntimeException("PLA No required");

                    setStClaimStatus(FinCodec.ClaimStatus.PLA);
                    setStActiveFlag("Y");

                } else if (isStatusClaimDLA()) {
                    if (getStDLANo() == null) throw new RuntimeException("DLA No required");

                    setStEffectiveClaimFlag("Y");
                    setStEffectiveFlag("Y");
                    setStApprovedClaimFlag("Y");
                    setDtApprovedClaimDate(new Date());
                    //setStClaimStatus(FinCodec.ClaimStatus.DLA);
                }
            }


            if (isStatusPolicy() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI())
                validateTreaty(true);
        }

        if (isStatusClaim())
            if (isStatusClaimPLA())
                if (getStClaimObjectID() == null)
                    throw new RuntimeException("Objek Klaim Belum Dipilih");

        if (isModified()) {

            if (isStatusClaim()) {

            } else {
                if (dtPolicyDate != null) {

                    final boolean blockValidPolicyDate = Parameter.readBoolean("BLOCKING_POLICY_DATE");

                    boolean withinCurrentMonth = DateUtil.getDateStr(getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                    if (blockValidPolicyDate)
                        if (!withinCurrentMonth) throw new RuntimeException("Invalid policy date");

                }
            }

        }

        if (getDtPeriodEnd() != null && getDtPeriodStart() != null) {
            boolean validPeriod = Tools.compare(DateUtil.truncDate(getDtPeriodStart()), DateUtil.truncDate(getDtPeriodEnd())) <= 0;

            if (!validPeriod) throw new RuntimeException("Invalid Period Start / End Date");
        }

        if (isStatusClaimEndorse()) {
        } else {
            if (getDtEndorseDate() != null) {
                boolean validEndorseDate = Tools.compare(getDtEndorseDate(), getDtPeriodStart()) >= 0 &&
                        Tools.compare(getDtEndorseDate(), getDtPeriodEnd()) <= 0;

                //if (!validEndorseDate)
                //   throw new RuntimeException("Invalid Endorsement Date");
            }
        }


    }

    public void validateTreaty(boolean raiseErrors) {

        stRIFinishFlag = null;

        if (!(isStatusEndorse() || isStatusPolicy() || isStatusRenewal() || isStatusEndorseRI())) return;

        //DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objDefault = (InsurancePolicyObjDefaultView) objects.get(i);

            DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                boolean equalTSI = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                boolean equalPremi = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyPremiTotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbPremiAmount(), BDUtil.one, scale));

                if (!equalTSI) {
                    if (raiseErrors) {
                        throw new RuntimeException("Alokasi TSI Reas Objek ["+ (i + 1) +"] " + objDefault.getStReference1() + " Salah");
                    } else {
                        return;
                    }
                }

                if (!equalPremi) {
                    if (raiseErrors) {
                        throw new RuntimeException("Alokasi Premi Reas Objek ["+ (i + 1) +"] " + objDefault.getStReference1() + " Salah");
                    } else {
                        return;
                    }
                }

                DTOList shares = trd.getShares();

                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                    if (!ri.isApproved()) {
                        if (raiseErrors)
                            throw new RuntimeException("Alokasi Reas Objek ["+ (i + 1) +"] " + objDefault.getStReference1() + " Belum Disetujui");
                        else
                            return;
                    }

                    if (ri.getStApprovedFlag() == null)
                        stRIFinishFlag = null;
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
        //getObjects();

        int riskclass = 1;

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            if (obj.getStRiskClass() == null) continue;

            try {
                int rc = Integer.parseInt(obj.getStRiskClass());
                if (rc < 1 || rc > 3) throw new RuntimeException("Risk class should be 1 to 3");
                if (rc > riskclass) riskclass = rc;
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid risk class value : " + obj.getStRiskClass());
            }

        }

        return String.valueOf(riskclass);
    }

    public boolean hasNPWP() {
        boolean tes = false;
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()) ;
            else continue;

            if (item.getStEntityID() != null) {
                final EntityView ent = getEntity2(item.getStEntityID());
                if (ent.getStTaxFile() != null) {
                    tes = true;
                } else if (ent.getStTaxFile() == null) {
                    tes = false;
                }
            }
        }
        return tes;
    }

	/*
    public void checkPayment() throws Exception {
        BigDecimal dbAmountSettled = null;
        BigDecimal dbAmount = null;

        DTOList invoice = getArinvoices();
        for (int i = 0; i < invoice.size(); i++) {
            ARInvoiceView invoice2 = (ARInvoiceView) invoice.get(i);

            dbAmountSettled = BDUtil.add(dbAmountSettled, invoice2.getDbAmountSettled());
            dbAmount = BDUtil.add(dbAmount, invoice2.getDbAmount());
        }

        if (Tools.compare(dbAmountSettled, dbAmount) < 0) {
            //belum bayar atau belum lunas
            setStLunas("N");
        } else if (Tools.compare(dbAmountSettled, dbAmount) >= 0) {
            //udah lunas
            setStLunas("Y");
        }

    }*/
    
    
    public boolean isPremiPaid() throws Exception {
        final SQLUtil S = new SQLUtil();
        boolean canClaim = false;
        try {
            final PreparedStatement PS = S.setQuery("select sum(preto) as tagihan, sum(bayar) as bayar "+
										" from aba_bayar1 "+
										" where pol_id = ? ");
            PS.setString(1, stParentID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) 
            {
                final BigDecimal tagihan = RS.getBigDecimal("tagihan");

                final BigDecimal bayar = RS.getBigDecimal("bayar");

				//if(BDUtil.isEqual(tagihan,bayar,2)||BDUtil.biggerThan(bayar,tagihan)) canClaim = true;	
            	if(bayar!=null) canClaim = true;
            }

            return canClaim;

        } finally {
            S.release();
        }
    }

    public boolean canClaimAgain() throws Exception {
        final SQLUtil S = new SQLUtil();

        boolean canClaim = true;
        try {
            final PreparedStatement PS = S.setQuery("select claim_loss_id " +
                    "FROM ins_pol_obj " +
                    "WHERE ins_pol_obj_ref_id = ?");

            PS.setString(1, stClaimObjectID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) 
            {
                final String lossID = RS.getString("claim_loss_id");
				
				if(lossID!=null){
					final String cekClaimRepeat = checkCanClaimRepeatedly(lossID);
					
					if (cekClaimRepeat.equalsIgnoreCase("Y"))
                    	canClaim = true;
                    else
                    	canClaim = false;
				}else{
					canClaim = true;
				}   
            }

            return canClaim;

        } finally {
            S.release();
        }

    }

    public String checkCanClaimRepeatedly(String lossID) throws Exception {
        final SQLUtil S = new SQLUtil();

        String cekRepeat = "N";
        try {
            final PreparedStatement PS = S.setQuery("select repeated_claim_flag " +
                    "FROM ins_claim_loss " +
                    "WHERE claim_loss_id = ?  and pol_type_id = ?");

            PS.setString(1, lossID);
            PS.setString(2, stPolicyTypeID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) //cekClaim = true;
            {
                String repeat_flag = RS.getString("repeated_claim_flag");

                if (repeat_flag != null)
                    if (repeat_flag.equalsIgnoreCase("Y")) cekRepeat = "Y";

            }

            return cekRepeat;

        } finally {
            S.release();
        }

    }

    public boolean isLunas() {
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
            if (coverage == null)
                coverage = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_cover where pol_id = ? order by ins_pol_cover_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoverView.class
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setObjIndex(String objIndex) {
        this.objIndex = objIndex;
    }

    public String getObjIndex() {
        return objIndex;
    }

    public InsuranceTSIView getInsuranceTSI(String stInsuranseTSIID) {
        return (InsuranceTSIView) DTOPool.getInstance().getDTO(InsuranceTSIView.class, stInsuranseTSIID);
    }

//   public BigDecimal getDbTotalComm1() {
//   		return BDUtil.add(dbNDHFee, dbNDBrok1);
//   }

    public void validateZoneLimit() throws Exception {
        final DTOList cover = getCoverage2();

        for (int i = 0; i < cover.size(); i++) {
            InsurancePolicyCoverView polisCover = (InsurancePolicyCoverView) cover.get(i);

            if (polisCover.getStZoneID() == null) continue;

            BigDecimal limit = getZoneLimit(polisCover.getStZoneID());

            boolean overLimit = Tools.compare(getDbInsuredAmount(), limit) >= 0 ? true : false;

            if (overLimit) {
                throw new RuntimeException("TSI Melebihi Limit Zona !");
            }
        }
    }

    public BigDecimal getZoneLimit(String zoneid) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                            "      limit1 " +
                            "   from " +
                            "         ins_zone_limit b " +
                            "   where" +
                            "      b.zone_id=?");

            S.setParam(1, zoneid);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getBigDecimal(1);

            return null;
        } finally {

            S.release();
        }
    }

    public String getInsuranceTreatyID(Date per_start) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                            "      ins_treaty_id,treaty_name " +
                            "   from " +
                            "         ins_treaty" +
                            "   where" +
                            "      treaty_period_start <= ? " +
                            "   and treaty_period_end >= ? " +
                            "   and treaty_class = 'RE' ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;
        } finally {

            S.release();
        }
    }
    
    public String getRiskCategoryID(String stPolicyTypeId, Date per_start) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                            "      ins_risk_cat_id " +
                            "   from " +
                            "         ins_risk_cat" +
                            "   where" +
                            "      period_start <= ? " +
                            "   and period_end >= ? " +
                            "   and poltype_id = ?  ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, stPolicyTypeId);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;
        } finally {

            S.release();
        }
    }

    public void validateObject() {

        //getObjects();
        boolean cek = false;
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
           
            if (obj.getStReference1() == null) {
                cek = true;
                throw new RuntimeException("Data Objek [" + (i + 1) + "] Tidak Boleh Kosong!");
            }
            if (obj.getStRiskCategoryID() == null)
                throw new RuntimeException("Kode Resiko Objek [" + (i + 1) + "] Belum Diisi");
        }

        //return cek;
        
        validateMandatoryFlexField();
    }

    public String getStKreasiTypeID() {
        return stKreasiTypeID;
    }

    public void setStKreasiTypeID(String stKreasiTypeID) {
        this.stKreasiTypeID = stKreasiTypeID;
    }

    public String getStKreasiTypeDesc() {
        return stKreasiTypeDesc;
    }

    public void setStKreasiTypeDesc(String stKreasiTypeDesc) {
        this.stKreasiTypeDesc = stKreasiTypeDesc;
    }

    public String getStCoinsID() {
        return stCoinsID;
    }

    public void setStCoinsID(String stCoinsID) {
        this.stCoinsID = stCoinsID;
    }

    public String getStCoinsName() {
        return stCoinsName;
    }

    public void setStCoinsName(String stCoinsName) {
        this.stCoinsName = stCoinsName;
    }

    public void addPeriodDesc() {
        setAddPeriodDesc(true);
    }

    public boolean isAddPeriodDesc() {
        return isAddPeriodDesc;
    }

    public void setAddPeriodDesc(boolean isAddPeriodDesc) {
        this.isAddPeriodDesc = isAddPeriodDesc;
    }

    public void loadCoins() {
        try {
            if (coins == null) {
                coins = ListUtil.getDTOListFromQuery(
                        "   select " +
                                "      *" +
                                "   from" +
                                "      ins_pol_coins" +
                                "   where" +
                                "      policy_id = ?",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCoins2() {
        try {
            if (coins == null) {
                coins = ListUtil.getDTOListFromQuery(
                        "   select " +
                                "      *" +
                                "   from" +
                                "      ins_pol_coins" +
                                "   where" +
                                "      policy_id = ? and entity_id <> 1",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showItemsAccount() {
        final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

        glApplicator.setCode('X', getStPolicyTypeID());
        glApplicator.setDesc("X", getStPolicyTypeDesc());
        //glApplicator.setCode('Y',getStEntityID());
        glApplicator.setCode('Y', getEntity().getStGLCode());
        if (getEntity().getStGLCode().equalsIgnoreCase("00000"))
            glApplicator.setDesc("Y", "");
        else
            glApplicator.setDesc("Y", getStEntityName());

        glApplicator.setCode('B', getStCostCenterCode());

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            InsuranceItemsView insItem = item.getInsItem();

            if (insItem == null) throw new RuntimeException("Ins item not found : " + item);

            ARTransactionLineView arTrxLine = insItem.getARTrxLine();

            if (arTrxLine == null) throw new RuntimeException("AR TRX Line not found : " + item);

            final String accode = arTrxLine.getStGLAccount();

            item.setStGLAccountID(glApplicator.getAccountID(accode));
            item.setStGLAccountDesc(glApplicator.getPreviewDesc());

            /*
            if (item.isComission()) {
               if (item.getStTaxCode()!=null){
                  item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
                  item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
               }
            }*/

            if (item.getStEntityID() != null) {
                if (item.getEntity().getStGLCode() == null)
                    throw new RuntimeException("GL Code " + item.getEntity().getStEntityName() + " Tidak Ada");

                glApplicator.setCode('Y', item.getEntity().getStGLCode());
                if ("00000".equalsIgnoreCase(item.getEntity().getStGLCode())) {
                    glApplicator.setDesc("Y", "");
                } else {
                    glApplicator.setDesc("Y", item.getEntity().getStEntityName());
                }


                item.setStGLAccountID(glApplicator.getAccountID(accode));
                item.setStGLAccountDesc(glApplicator.getPreviewDesc());

                if (item.isComission()) {
                    if (item.getStTaxCode() != null) {
                        item.setStTaxGLAccount(glApplicator.getAccountID(item.getTax().getStAccountCode()));
                        item.setStTaxGLAccountDesc(glApplicator.getPreviewDesc());
                    }
                }
            }


        }
    }

    public String getStPrintFlag() {
        String print = "";
        if (getStPrintCode() != null)
            print = "Y";

        return print;
    }

    public void setStPrintFlag(String stPrintFlag) {
        this.stPrintFlag = stPrintFlag;
    }

    public String getStCoinsPolicyNo() {
        return stCoinsPolicyNo;
    }

    public void setStCoinsPolicyNo(String stCoinsPolicyNo) {
        this.stCoinsPolicyNo = stCoinsPolicyNo;
    }

    public boolean isExcludeReasMode() {
        return excludeReasMode;
    }

    public void setExcludeReasMode(boolean excludeReasMode) {
        this.excludeReasMode = excludeReasMode;
    }

    public void addLampiran() {
        setLampiran(true);
    }

    public boolean isLampiran() {
        return isLampiran;
    }

    public void setLampiran(boolean isLampiran) {
        this.isLampiran = isLampiran;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public BigDecimal getDbInsuredAmountRp() {
        return BDUtil.mul(dbInsuredAmount, dbCurrencyRate);
    }

    public BigDecimal getDbPremiAmtRp() {
        return BDUtil.mul(getDbPremiAmt(), dbCurrencyRate);
    }

    public BigDecimal getDbTotalFeeRp() {
        return BDUtil.mul(dbTotalFee, dbCurrencyRate);
    }


    public BigDecimal getDbPremiTotalAfterDiscRp() {
        return BDUtil.mul(dbPremiTotalAfterDisc, dbCurrencyRate);
    }

    public BigDecimal getDbPremiAmt() {
        return dbPremiAmt;
    }


    public BigDecimal getDbDiskon() {
        return dbDiskon;
    }

    public void setDbDiskon(BigDecimal dbDiskon) {
        this.dbDiskon = dbDiskon;
    }

    public BigDecimal getDbKomisi() {
        return dbKomisi;
    }

    public void setDbKomisi(BigDecimal dbKomisi) {
        this.dbKomisi = dbKomisi;
    }

    public BigDecimal getDbBrok() {
        return dbBrok;
    }

    public void setDbBrok(BigDecimal dbBrok) {
        this.dbBrok = dbBrok;
    }

    public BigDecimal getDbDiscPremi() {
        return dbDiscPremi;
    }

    public void setDbDiscPremi(BigDecimal dbDiscPremi) {
        this.dbDiscPremi = dbDiscPremi;
    }

    public void setDbPremiAmt(BigDecimal dbPremiAmt) {
        this.dbPremiAmt = dbPremiAmt;
    }

    public BigDecimal getDbClaimAmountRp() {
        return BDUtil.mul(dbClaimAmount, dbCurrencyRate);
    }

    

    public void setDbPremiKo(BigDecimal dbPremiKo) {
        this.dbPremiKo = dbPremiKo;
    }

    public BigDecimal getDbPremiKo() {
        return dbPremiKo;
    }

    public void setDbPremiReas(BigDecimal dbPremiReas) {
        this.dbPremiReas = dbPremiReas;
    }

    public BigDecimal getDbPremiReas() {
        return dbPremiReas;
    }

    public void setDbPremiOR(BigDecimal dbPremiOR) {
        this.dbPremiOR = dbPremiOR;
    }

    public BigDecimal getDbPremiOR() {
        return dbPremiOR;
    }

    public void setDbTsiReas(BigDecimal dbTsiReas) {
        this.dbTsiReas = dbTsiReas;
    }

    public BigDecimal getDbTsiReas() {
        return dbTsiReas;
    }

    public void setStTreatyType(String stTreatyType) {
        this.stTreatyType = stTreatyType;
    }

    public String getStTreatyType() {
        return stTreatyType;
    }

    public RegionView getCcCode(String stRegID) {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegID);

        return reg;
    }
    
    public GLCostCenterView getCostCenter(String stCostCenterCode) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return costcenter;
    }


    public void setDbTax(BigDecimal dbTax) {
        this.dbTax = dbTax;
    }

    public BigDecimal getDbTax() {
        return dbTax;
    }

    public void setDbJumlah(BigDecimal dbJumlah) {
        this.dbJumlah = dbJumlah;
    }

    public BigDecimal getDbJumlah() {
        return dbJumlah;
    }


    public void setDbPolis(BigDecimal dbPolis) {
        this.dbPolis = dbPolis;
    }

    public BigDecimal getDbPolis() {
        return dbPolis;
    }

    public void setDbBay(BigDecimal dbBay) {
        this.dbBay = dbBay;
    }

    public BigDecimal getDbBay() {
        return dbBay;
    }

    public void setDbFee(BigDecimal dbFee) {
        this.dbFee = dbFee;
    }

    public BigDecimal getDbFee() {
        return dbFee;
    }

    public void setDbSal(BigDecimal dbSal) {
        this.dbSal = dbSal;
    }

    public BigDecimal getDbSal() {
        return dbSal;
    }

    public BigDecimal getDbKlaim() {
        return dbKlaim;
    }

    public void setDbKlaim(BigDecimal dbKlaim) {
        this.dbKlaim = dbKlaim;
    }

    public DTOList getClaimCause() {
        loadCause();
        return cause;
    }

    public void setClaimCause(DTOList cause) {
        this.cause = cause;
    }

    public void loadCause() {
        try {
            if (cause == null)
                cause = ListUtil.getDTOListFromQuery(
                        "select b.cause_desc " +
                                "from ins_policy a " +
                                "inner join ins_clm_cause b on b.ins_clm_caus_id = a.claim_cause " +
                                "where a.pol_id = ?",
                        new Object[]{stPolicyID},
                        InsuranceClaimCauseView.class
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateEndorsePAKreasi() {
        if (isStatusEndorse()||isStatusEndorseRI()) {
            BigDecimal totPremiRIEndorse = null;
            BigDecimal totCommissionRIEndorse = null;
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) objects.get(i);

                BigDecimal dbPremiRIEndorse = BDUtil.sub(objx.getDbReference2(), getParentObject(objx.getStPolicyObjectRefID()).getDbReference2());
                totPremiRIEndorse = BDUtil.add(totPremiRIEndorse, dbPremiRIEndorse);

                BigDecimal dbCommissionRIEndorse = BDUtil.sub(objx.getDbReference9(), getParentObject(objx.getStPolicyObjectRefID()).getDbReference9());
                totCommissionRIEndorse = BDUtil.add(totCommissionRIEndorse, dbCommissionRIEndorse);

                DTOList coins = getCoins2();
                if (coins.size() == 2)
                    for (int j = 0; j < coins.size(); j++) {
                        InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coins.get(j);

                        if (coins3.isHoldingCompany())
                            coins3.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
                        else coins3.setStEntityID(getStCoinsID());

                        if (coins3.isHoldingCompany()) {
                            coins3.setDbPremiAmount(BDUtil.sub(getDbPremiTotal(), totPremiRIEndorse));
                            coins3.setDbCommissionAmount(null);
                        } else {
                            coins3.setDbSharePct(BDUtil.zero);
                            coins3.setStEntityID(getStCoinsID());
                            coins3.setDbPremiAmount(totPremiRIEndorse);
                            coins3.setDbCommissionAmount(totCommissionRIEndorse);
                        }

                    }

            }

        }
    }

    public InsurancePolicyObjDefaultView getParentObject(String parentObjectID) {
        return (InsurancePolicyObjDefaultView) DTOPool.getInstance().getDTO(InsurancePolicyObjDefaultView.class, parentObjectID);
    }

    public void setDbTsiBPDAN(BigDecimal dbTsiBPDAN) {
        this.dbTsiBPDAN = dbTsiBPDAN;
    }

    public BigDecimal getDbTsiBPDAN() {
        return dbTsiBPDAN;
    }

    public void setDbTsiOR(BigDecimal dbTsiOR) {
        this.dbTsiOR = dbTsiOR;
    }

    public BigDecimal getDbTsiOR() {
        return dbTsiOR;
    }

    public void setDbTsiSPL(BigDecimal dbTsiSPL) {
        this.dbTsiSPL = dbTsiSPL;
    }

    public BigDecimal getDbTsiSPL() {
        return dbTsiSPL;
    }

    public void setDbTsiQS(BigDecimal dbTsiQS) {
        this.dbTsiQS = dbTsiQS;
    }

    public BigDecimal getDbTsiQS() {
        return dbTsiQS;
    }

    public void setDbTsiFAC(BigDecimal dbTsiFAC) {
        this.dbTsiFAC = dbTsiFAC;
    }

    public BigDecimal getDbTsiFAC() {
        return dbTsiFAC;
    }

    public void setDbTsiPARK(BigDecimal dbTsiPARK) {
        this.dbTsiPARK = dbTsiPARK;
    }

    public BigDecimal getDbTsiPARK() {
        return dbTsiPARK;
    }

    public void setDbTsiFACO(BigDecimal dbTsiFACO) {
        this.dbTsiFACO = dbTsiFACO;
    }

    public BigDecimal getDbTsiFACO() {
        return dbTsiFACO;
    }

    public void setDbTsiKo(BigDecimal dbTsiKo) {
        this.dbTsiKo = dbTsiKo;
    }

    public BigDecimal getDbTsiKo() {
        return dbTsiKo;
    }

    public void setDbTsiAskrida(BigDecimal dbTsiAskrida) {
        this.dbTsiAskrida = dbTsiAskrida;
    }

    public BigDecimal getDbTsiAskrida() {
        return dbTsiAskrida;
    }

    public void setDbPremiBPDAN(BigDecimal dbPremiBPDAN) {
        this.dbPremiBPDAN = dbPremiBPDAN;
    }

    public BigDecimal getDbPremiBPDAN() {
        return dbPremiBPDAN;
    }

    public void setDbPremiSPL(BigDecimal dbPremiSPL) {
        this.dbPremiSPL = dbPremiSPL;
    }

    public BigDecimal getDbPremiSPL() {
        return dbPremiSPL;
    }

    public void setDbPremiQS(BigDecimal dbPremiQS) {
        this.dbPremiQS = dbPremiQS;
    }

    public BigDecimal getDbPremiQS() {
        return dbPremiQS;
    }

    public void setDbPremiFAC(BigDecimal dbPremiFAC) {
        this.dbPremiFAC = dbPremiFAC;
    }

    public BigDecimal getDbPremiFAC() {
        return dbPremiFAC;
    }

    public void setDbPremiPARK(BigDecimal dbPremiPARK) {
        this.dbPremiPARK = dbPremiPARK;
    }

    public BigDecimal getDbPremiPARK() {
        return dbPremiPARK;
    }

    public void setDbPremiFACO(BigDecimal dbPremiFACO) {
        this.dbPremiFACO = dbPremiFACO;
    }

    public BigDecimal getDbPremiFACO() {
        return dbPremiFACO;
    }

    public void setDbPremiAskrida(BigDecimal dbPremiAskrida) {
        this.dbPremiAskrida = dbPremiAskrida;
    }

    public BigDecimal getDbPremiAskrida() {
        return dbPremiAskrida;
    }

    public void setDbPremiKoas(BigDecimal dbPremiKoas) {
        this.dbPremiKoas = dbPremiKoas;
    }

    public BigDecimal getDbPremiKoas() {
        return dbPremiKoas;
    }

    public void setStPeriod(String stPeriod) {
        this.stPeriod = stPeriod;
    }

    public String getStPeriod() {
        return stPeriod;
    }

    public BigDecimal getDbSharePct() {
        return dbSharePct;
    }

    public void setDbSharePct(BigDecimal dbSharePct) {
        this.dbSharePct = dbSharePct;
    }

    public void setDbTsiXOL1(BigDecimal dbTsiXOL1) {
        this.dbTsiXOL1 = dbTsiXOL1;
    }

    public BigDecimal getDbTsiXOL1() {
        return dbTsiXOL1;
    }

    public void setDbTsiXOL2(BigDecimal dbTsiXOL2) {
        this.dbTsiXOL2 = dbTsiXOL2;
    }

    public BigDecimal getDbTsiXOL2() {
        return dbTsiXOL2;
    }

    public void setDbTsiXOL3(BigDecimal dbTsiXOL3) {
        this.dbTsiXOL3 = dbTsiXOL3;
    }

    public BigDecimal getDbTsiXOL3() {
        return dbTsiXOL3;
    }

    public void setDbTsiXOL4(BigDecimal dbTsiXOL4) {
        this.dbTsiXOL4 = dbTsiXOL4;
    }

    public BigDecimal getDbTsiXOL4() {
        return dbTsiXOL4;
    }

    public void setDbTsiXOL5(BigDecimal dbTsiXOL5) {
        this.dbTsiXOL5 = dbTsiXOL5;
    }

    public BigDecimal getDbTsiXOL5() {
        return dbTsiXOL5;
    }

    public void setDbPremiXOL1(BigDecimal dbPremiXOL1) {
        this.dbPremiXOL1 = dbPremiXOL1;
    }

    public BigDecimal getDbPremiXOL1() {
        return dbPremiXOL1;
    }

    public void setDbPremiXOL2(BigDecimal dbPremiXOL2) {
        this.dbPremiXOL2 = dbPremiXOL2;
    }

    public BigDecimal getDbPremiXOL2() {
        return dbPremiXOL2;
    }

    public void setDbPremiXOL3(BigDecimal dbPremiXOL3) {
        this.dbPremiXOL3 = dbPremiXOL3;
    }

    public BigDecimal getDbPremiXOL3() {
        return dbPremiXOL3;
    }

    public void setDbPremiXOL4(BigDecimal dbPremiXOL4) {
        this.dbPremiXOL4 = dbPremiXOL4;
    }

    public BigDecimal getDbPremiXOL4() {
        return dbPremiXOL4;
    }

    public void setDbPremiXOL5(BigDecimal dbPremiXOL5) {
        this.dbPremiXOL5 = dbPremiXOL5;
    }

    public BigDecimal getDbPremiXOL5() {
        return dbPremiXOL5;
    }

    public String getStCostCenterAddress() {

        final GLCostCenterView gcc = getCostCenter();

        if (gcc == null) return null;

        return gcc.getStAddress();
    }

    public boolean isCaptivePolicy() throws Exception {
        final EntityView entity = getEntity();

        if (entity == null) throw new Exception("You must select customer for this policy");

        String custCategory = entity.getStCategory1();

        final String businessSource = getDigit(custCategory, 1);

        //logger.logDebug("businessSource= " + businessSource);
        if ("1".equalsIgnoreCase(businessSource)) return false;
        else if ("2".equalsIgnoreCase(businessSource)) return true;
        else if ("3".equalsIgnoreCase(businessSource)) return true;
        else if ("4".equalsIgnoreCase(businessSource)) return true;
        else throw new RuntimeException("Format No Polis Salah");

    }

    public void validateTaxCode() throws Exception {
        final DTOList details = getDetails();

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(i);

            if (!items.isComission()) continue;

            if (items.getInsItem().getStItemCategory().equalsIgnoreCase("COMM")) {
                if (!items.getStTaxCode().equalsIgnoreCase("1") &&
                        !items.getStTaxCode().equalsIgnoreCase("2") &&
                        !items.getStTaxCode().equalsIgnoreCase("3"))
                    throw new RuntimeException("Jenis Pajak Harus Pajak Komisi");
            } else if (items.getInsItem().getStItemCategory().equalsIgnoreCase("BROKR")) {
                if (!items.getStTaxCode().equalsIgnoreCase("4") &&
                        !items.getStTaxCode().equalsIgnoreCase("5") &&
                        !items.getStTaxCode().equalsIgnoreCase("6"))
                    throw new RuntimeException("Jenis Pajak Harus Pajak Brokerage");
            } else if (items.getInsItem().getStItemCategory().equalsIgnoreCase("HFEE")) {
                if (!items.getStTaxCode().equalsIgnoreCase("7") &&
                        !items.getStTaxCode().equalsIgnoreCase("8") &&
                        !items.getStTaxCode().equalsIgnoreCase("9"))
                    throw new RuntimeException("Jenis Pajak Harus Pajak Handling Fee");
            }
            
            if(items.getStEntityID()==null)
            		throw new RuntimeException("Penerima Komisi/Broker Fee/Handling Fee Belum Diisi");

        }
    }

    public void validateData() throws Exception {
    	if(isStatusClaimPLA())
    		if(!canClaimAgain())
    			throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Objek Sama");
        
        validateMandatoryFlexField();
        validateTaxCode();
    }

    
    public String getStUserApprovedDesc() {

        final UserSessionView user = getUserApproved();

        if (user == null) return null;

        return user.getStUserName();
    }

    public String getStUserDivision() {

        final UserSessionView user = getUserApproved();

        if (user == null) return null;

        return user.getStDivision();
    }

    private UserSessionView getUserApproved() {

        final UserSessionView user = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stApprovedWho);

        return user;

    }

    public DTOList getAddress() {
        if (address == null) loadAddress();
        return address;
    }

    public void setAddress(DTOList address) {
        this.address = address;
    }

    public void loadAddress() {
        try {
            if (address == null) {
                address = ListUtil.getDTOListFromQuery(
                        "   select " +
                                "      *" +
                                "   from" +
                                "      ent_address" +
                                "   where" +
                                "      ent_id = ?",
                        new Object[]{getStProducerID()},
                        EntityAddressView.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
   public String getStClaimLetter() {
      return stClaimLetter;
   }

   public void setStClaimLetter(String stClaimLetter) {
      this.stClaimLetter = stClaimLetter;
   }
   
   public Date getDtPLADate() {
      return dtPLADate;
   }

   public void setDtPLADate(Date dtPLADate) {
      this.dtPLADate = dtPLADate;
   }
   
   public BigDecimal getDbNDTaxComm1() {
        return dbNDTaxComm1;
    }

    public void setDbNDTaxComm1(BigDecimal dbNDTaxComm1) {
        this.dbNDTaxComm1 = dbNDTaxComm1;
    }
    
    public BigDecimal getDbNDTaxComm2() {
        return dbNDTaxComm2;
    }

    public void setDbNDTaxComm2(BigDecimal dbNDTaxComm2) {
        this.dbNDTaxComm2 = dbNDTaxComm2;
    }
    
    public BigDecimal getDbNDTaxComm3() {
        return dbNDTaxComm3;
    }

    public void setDbNDTaxComm3(BigDecimal dbNDTaxComm3) {
        this.dbNDTaxComm3 = dbNDTaxComm3;
    }
    
    public BigDecimal getDbNDTaxComm4() {
        return dbNDTaxComm4;
    }

    public void setDbNDTaxComm4(BigDecimal dbNDTaxComm4) {
        this.dbNDTaxComm4 = dbNDTaxComm4;
    }
    
    public BigDecimal getDbNDTaxBrok1() {
        return dbNDTaxBrok1;
    }

    public void setDbNDTaxBrok1(BigDecimal dbNDTaxBrok1) {
        this.dbNDTaxBrok1 = dbNDTaxBrok1;
    }
    
    public BigDecimal getDbNDTaxBrok2() {
        return dbNDTaxBrok2;
    }

    public void setDbNDTaxBrok2(BigDecimal dbNDTaxBrok2) {
        this.dbNDTaxBrok2 = dbNDTaxBrok2;
    }
    
    public BigDecimal getDbNDTaxHFee() {
        return dbNDTaxHFee;
    }

    public void setDbNDTaxHFee(BigDecimal dbNDTaxHFee) {
        this.dbNDTaxHFee = dbNDTaxHFee;
    }
    
    public String getSignByApproval(String stApprovedWho) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "  select vs_description "+
					" from s_valueset "+
					" where  "+
					" vs_group = 'CLAIM_SIGN' and "+
 					" ref1 in(select branch "+
					" from s_users "+
					" where user_id = ?)");

            S.setParam(1, stApprovedWho);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;
        } finally {

            S.release();
        }
    }
    
    public DTOList getBayar() {
        loadBayar();
        return bayar;
    }

    public void loadBayar() {
        try {
            if (bayar == null)
                bayar = ListUtil.getDTOListFromQuery(
                        "select * from aba_bayar1 where pol_id = ?",
                        new Object[]{stParentID},
                        InsuranceBayar1View.class
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBayar(DTOList bayar) {
        this.bayar = bayar;
    }
    
    public void validateMandatoryFlexField(){
    	if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                    DTOList details = objectMap.getDetails();

                    for (int p = 0; p < details.size(); p++) {
                        FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);
						
                        if(Tools.isYes(ffd.getStMandatoryFlag())){
                        	if(obj.getProperty(ffd.getStFieldRef())==null)
                        		throw new RuntimeException(ffd.getStFieldDesc()+" Objek Ke " + (i + 1) + " Belum Diisi");
                        }
                    }
                    
                    if (obj.getStRiskCategoryID() == null)
                		throw new RuntimeException("Kode Resiko Objek Ke " + (i + 1) + " Belum Diisi");
                }

            }
        }
    }
    
  
   
   public void setDbCommBPDAN(BigDecimal dbCommBPDAN) {
        this.dbCommBPDAN = dbCommBPDAN;
    }

    public BigDecimal getDbCommBPDAN() {
        return dbCommBPDAN;
    }

    public void setDbCommOR(BigDecimal dbCommOR) {
        this.dbCommOR = dbCommOR;
    }

    public BigDecimal getDbCommOR() {
        return dbCommOR;
    }

    public void setDbCommSPL(BigDecimal dbCommSPL) {
        this.dbCommSPL = dbCommSPL;
    }

    public BigDecimal getDbCommSPL() {
        return dbCommSPL;
    }

    public void setDbCommQS(BigDecimal dbCommQS) {
        this.dbCommQS = dbCommQS;
    }

    public BigDecimal getDbCommQS() {
        return dbCommQS;
    }

    public void setDbCommFAC(BigDecimal dbCommFAC) {
        this.dbCommFAC = dbCommFAC;
    }

    public BigDecimal getDbCommFAC() {
        return dbCommFAC;
    }

    public void setDbCommPARK(BigDecimal dbCommPARK) {
        this.dbCommPARK = dbCommPARK;
    }

    public BigDecimal getDbCommPARK() {
        return dbCommPARK;
    }

    public void setDbCommFACO(BigDecimal dbCommFACO) {
        this.dbCommFACO = dbCommFACO;
    }

    public BigDecimal getDbCommFACO() {
        return dbCommFACO;
    }
    
    public Date getDtConfirmDate() {
      return dtConfirmDate;
   }

   public void setDtConfirmDate(Date dtConfirmDate) {
      this.dtConfirmDate = dtConfirmDate;
   }
   
    public DTOList getABAKreasi() {
        loadABAKreasi();
        return abakreasi;
    }

    public void loadABAKreasi() {
        try {
            if (abakreasi == null)
                abakreasi = ListUtil.getDTOListFromQuery(
                        "select * from aba_kreasi where pol_no = ?",
                        new Object[]{stPolicyNo},
                        InsuranceKreasiView.class
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setABAKreasi(DTOList abakreasi) {
        this.abakreasi = abakreasi;
    }
}

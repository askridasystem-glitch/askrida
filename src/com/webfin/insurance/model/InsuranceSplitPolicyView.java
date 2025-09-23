/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyView
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:05:00 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.controller.FormTab;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.RecordBackup;
import com.crux.common.parameter.Parameter;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.ff.model.FlexFieldDetailView;
import com.crux.ff.model.FlexTableView;
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
import com.crux.login.model.UserSessionView;
import com.crux.util.crypt.Crypt;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.model.GLCostCenterView3;
import com.webfin.gl.model.GLCurrencyView;
import com.webfin.incoming.model.ApprovalBODView;
import com.webfin.insurance.custom.PAKreasiHandler;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.UserManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.joda.time.DateTime;
import org.joda.time.Years;

public class InsuranceSplitPolicyView extends DTO implements RecordAudit, RecordBackup {
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
    2
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

    private boolean usePMKBaru = true;

    private final static transient LogManager logger = LogManager.getInstance(InsuranceSplitPolicyView.class);
    public static String tableName = "ins_split_policy";
    public static String comboFields[] = {"pol_id", "pol_no", "cust_name", "pla_no", "dla_no", "jumlahklaim"};
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
        {"dtPLADate", "pla_date"},
        {"stClaimLetter", "claim_letter_no"},
        {"stReinsuranceApprovedWho", "ri_approved_who"},
        {"dtReinsuranceApprovedDate", "ri_approved_date"},
        {"stDaysLength", "days_length"},
        {"stUnits", "units"},
        {"stDocumentPrintFlag", "document_print_flag"},
        {"dtPaymentDate", "payment_date"},
        {"stPaymentNotes", "payment_note"},
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
        {"dtConfirmDate", "confirm_date"},
        {"stClaimClientName", "claim_client_name"},
        {"stClaimAccountNo", "claim_account_no"},
        {"stClaimNumberID", "claim_number_id"},
        {"dbClaimBalance", "claim_balance_total"},
        {"stClaimChronology", "claim_chronology"},
        {"stWarranty", "warranty"},
        {"stUnderwritingFinishFlag", "f_uw_finish"},
        {"stClaimFinishFlag", "f_claim_finish"},
        {"stEndorseMode", "endorse_mode"},
        {"stRejectFlag", "reject_flag"},
        // {"stCoinsuranceTypeID", "coins_type_id"},
        {"stCreateWho", "create_who*n"},
        {"dbAPTax", "ap_tax*n"},
        {"dbAPTaxP", "ap_tax_p*n"},
        {"stStatusOther", "status_other"},
        {"stRejectNotes", "reject_notes"},
        {"stAdminNotes", "admin_notes"},
        {"dbNDPPN", "nd_ppn"},
        {"stManualReinsuranceFlag", "f_manual_ri"},
        {"stParentPolicyNo", "parent_pol_no"},
        {"stRenewalCounter", "renewal_counter"},
        {"dbClaimAdvancePaymentAmount", "claim_adv_payment_amount"},
        {"stReference2Desc", "ref2desc"},
        {"stCreateName", "create_name*n"},
        {"stApprovedName", "approved_name*n"},
        {"stApprovedReinsName", "approved_reins_name*n"},
        {"stClaimObjectParentID", "claim_object_parent_id"},
        {"stClientIP", "client_ip"},
        {"stPassword", "password"},
        {"dbNDFeeBase1", "nd_feebase1"},
        {"dbNDFeeBase2", "nd_feebase2"},
        {"stReadyToApproveFlag", "f_ready_to_approve"},
        {"stCoLeaderID", "co_leader_id"},
        {"stRIPostedFlag", "ri_posted_flag"},
        {"dbClaimBPDAN", "claim_bpdan*n"},
        {"dbClaimOR", "claim_or*n"},
        {"dbClaimSPL", "claim_qs*n"},
        {"dbClaimQS", "claim_spl*n"},
        {"dbClaimFAC", "claim_fac*n"},
        {"dbClaimPARK", "claim_park*n"},
        {"dbClaimFACO", "claim_faco*n"},
        {"dbClaimXOL1", "claim_xol1*n"},
        {"dbDanaReas", "danaReas*n"},
        {"dbCommXOL1", "comm_xol1*n"},
        {"stSignCode", "sign_code"},
        {"stReceiptNo", "receipt_no"},
        {"stPrintCounter", "print_counter"},
        {"dbAPSFeeP", "ap_sfee_p*n"},
        {"dbAPPCostP", "ap_pcost_p*n"},
        {"dbAPDiscP", "ap_disc_p*n"},
        {"dbAPHFeeP", "ap_hfee_p*n"},
        {"dbAPBFeeP", "ap_bfee_p*n"},
        {"dbAPFBaseP", "ap_fbase_p*n"},
        {"dtReceiptDate", "receipt_date*n"},
        {"stCommEntityID1", "comm_ent_id1"},
        {"stCommEntityID2", "comm_ent_id2"},
        {"stCommEntityID3", "comm_ent_id3"},
        {"stCommEntityID4", "comm_ent_id4"},
        {"stBfeeEntityID1", "bfee_ent_id1"},
        {"stBfeeEntityID2", "bfee_ent_id2"},
        {"stFbaseEntityID1", "feebase_ent_id1"},
        {"stFbaseEntityID2", "feebase_ent_id2"},
        {"stHfeeEntityID1", "hfee_ent_id1"},
        {"dbTsiFACO1", "tsi_faco1*n"},
        {"dbPremiFACO1", "premi_faco1*n"},
        {"dbTsiFACO2", "tsi_faco2*n"},
        {"dbPremiFACO2", "premi_faco2*n"},
        {"dbTsiFACO3", "tsi_faco3*n"},
        {"dbPremiFACO3", "premi_faco3*n"},
        {"dbTsiJP", "tsi_jp*n"},
        {"dbPremiJP", "premi_jp*n"},
        {"dbTsiQSKR", "tsi_qskr*n"},
        {"dbPremiQSKR", "premi_qskr*n"},
        {"dbCommBPDAN", "comm_bpdan*n"},
        {"dbCommOR", "comm_or*n"},
        {"dbCommQS", "comm_qs*n"},
        {"dbCommSPL", "comm_spl*n"},
        {"dbCommFAC", "comm_fac*n"},
        {"dbCommPARK", "comm_park*n"},
        {"dbCommFACO", "comm_faco*n"},
        {"dbCommFACO1", "comm_faco1*n"},
        {"dbCommFACO2", "comm_faco2*n"},
        {"dbCommFACO3", "comm_faco3*n"},
        {"dbCommJP", "comm_jp*n"},
        {"dbCommQSKR", "comm_qskr*n"},
        {"dbClaimBPDAN", "claim_bpdan*n"},
        {"dbClaimOR", "claim_or*n"},
        {"dbClaimSPL", "claim_qs*n"},
        {"dbClaimQS", "claim_spl*n"},
        {"dbClaimFAC", "claim_fac*n"},
        {"dbClaimPARK", "claim_park*n"},
        {"dbClaimFACO", "claim_faco*n"},
        {"dbClaimXOL1", "claim_xol1*n"},
        {"dbClaimJP", "claim_jp*n"},
        {"dbClaimQSKR", "claim_qskr*n"},
        {"dbNDBrokerageAfterPPN", "nd_brok_after_ppn"},
        {"stAllowSamePeriodFlag", "allow_same_period_f"},
        {"stValidateClaimFlag", "f_validate_claim"},
        {"stReference13", "ref13"},
        {"stAccountno", "accountno*n"},
        {"dtClaimPaymentDate", "claim_payment_date"},
        {"stClaimPaymentUsedFlag", "claim_payment_used_f"},
        {"stGatewayDataFlag", "gateway_data_f"},
        {"stGatewayPolID", "gateway_pol_id"},
        {"dbAPPpnP", "ap_ppn_p*n"},
        {"stCheckingFlag", "checking_flag"},
        {"stManualCoinsFlag", "f_manual_coins"},
        {"dbCurrencyRateClaim", "ccy_rate_claim"},
        {"stClaimLocationID", "claim_location_id"},
        {"stRiskApprovedWho", "risk_approved_who"},
        {"dtRiskApprovedDate", "risk_approved_date"},
        {"stRiskApproved", "risk_approved_f"},
        {"stMarketingOfficerWho", "marketing_officer_who"},
        {"stReverseNotes", "reverse_notes"},
        {"dtClaimPaymentDate2", "claim_payment_date2*n"},
        {"stDataSourceID", "data_source_id"},
        //{"stNotificationUserID", "notif_user_id"},
        //{"stEffectiveTempFlag", "effective_temp_flag"},
        {"stNomorBuktiBayar", "no_bukti_bayar*n"},
        {"dtTanggalBayar", "tanggal_bayar*n"},
        {"stNomorReferensiPembayaran", "no_referensi_pembayaran*n"},
        {"dtSparetimeDate", "sparetime_date"},
        {"dtRejectDate", "reject_date"},
        {"stClaimPengajuName", "claim_pengaju_name*n"},
        {"stClaimLossID", "claim_loss_id*n"},
        {"dtClaimProposeDate", "claim_propose_date"},
        {"dbClaimSubroPaid", "claimsubropaid"},
        {"stJumlahKlaim", "jumlahklaim*n"},
        {"stReferenceNo","reference_no"},
        {"stJumlahObjek","jumlah_objek*n"},
        {"stJumlahObjekTerproses","objek_terproses*n"},
        //{"stReference14", "ref14"},
        //{"stReference15", "ref15"},
        //{"stReference16", "ref16"},
        //{"stClaimSubCauseID", "claim_sub_cause"},
        //{"stClaimSubCauseOther", "claim_sub_cause_other"},

    };
    private String stAccountno;
    private String stClaimClientName;
    private String stClaimAccountNo;
    private String stClaimNumberID;
    private BigDecimal dbClaimBalance;
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
    private BigDecimal dbTsiJP;
    private BigDecimal dbPremiJP;
    private BigDecimal dbCommJP;
    private BigDecimal dbTsiQSKR;
    private BigDecimal dbPremiQSKR;
    private BigDecimal dbCommQSKR;
    private BigDecimal dbClaimJP;
    private BigDecimal dbClaimQSKR;
    private BigDecimal dbTsiFACO1;
    private BigDecimal dbPremiFACO1;
    private BigDecimal dbCommFACO1;
    private BigDecimal dbTsiFACO2;
    private BigDecimal dbPremiFACO2;
    private BigDecimal dbCommFACO2;
    private BigDecimal dbTsiFACO3;
    private BigDecimal dbPremiFACO3;
    private BigDecimal dbCommFACO3;
    private String stPeriod;
    private Date dtPLADate;
    private String stClaimLetter;
    private String stDaysLength;
    private String stUnits;
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
    //private BigDecimal dbOwnInsuredAmount;
    //private BigDecimal dbAllPremium;
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
//    private InsurancePolicyCoverView getCoverage;
//    private InsurancePolicyCoverReinsView getCoverageReins;
    private String stRateMethod;
    private String stRateMethodDesc;
    private BigDecimal dbClaimAmountEndorse;
//    private DTOList list1;
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
    private String stDocumentPrintFlag;
    private BigDecimal dbPremiAmt;
    private BigDecimal dbDiskon;
    private BigDecimal dbKomisi;
    private BigDecimal dbBrok;
    private BigDecimal dbDiscPremi;
    private Date dtConfirmDate;
    private DTOList objectsClaim;
    int scale = 0;
    private InsuranceRiskCategoryView riskcat;
    private String stClaimChronology;
    private DTOList coinsCoverage;
    private DTOList abakoasur;
    private String stWarranty;
    //private String stCoinsuranceTypeID;
    private String stUnderwritingFinishFlag;
    private String stClaimFinishFlag;
    private String stEndorseMode;
    private String stRejectFlag;
    private String stCreateWho;
    private InsurancePolicyCoinsView holdingCoinCoverage;
    private BigDecimal dbAPTax;
    private BigDecimal dbAPTaxP;
    private String stStatusOther;
    private String stRejectNotes;
    private String stAdminNotes;
    private BigDecimal dbNDPPN;
    private String stManualReinsuranceFlag;
    private String stParentPolicyNo;
    private String stRenewalCounter;
    private BigDecimal dbClaimAdvancePaymentAmount;
    private String stReference2Desc;
    private String stCreateName;
    private String stApprovedName;
    private String stApprovedReinsName;
    private boolean editReasOnlyMode = false;
    private String stClaimObjectParentID;
    private String stClientIP;
    private String stPassword;
    private BigDecimal dbNDFeeBase1;
    private BigDecimal dbNDFeeBase2;
    private String stReadyToApproveFlag;
    private String stCoLeaderID;
    private String stRIPostedFlag;
    private BigDecimal dbClaimBPDAN;
    private BigDecimal dbClaimOR;
    private BigDecimal dbClaimSPL;
    private BigDecimal dbClaimQS;
    private BigDecimal dbClaimFAC;
    private BigDecimal dbClaimPARK;
    private BigDecimal dbClaimFACO;
    private BigDecimal dbClaimXOL1;
    private BigDecimal dbDanaReas;
    private BigDecimal dbCommXOL1;
    private String stSignCode;
    private String stReceiptNo;
    private String stPrintCounter;
    private BigDecimal dbAPSFeeP;
    private BigDecimal dbAPPCostP;
    private BigDecimal dbAPBFeeP;
    private BigDecimal dbAPHFeeP;
    private BigDecimal dbAPFBaseP;
    private BigDecimal dbAPDiscP;
    private Date dtReceiptDate;
    private String stCommEntityID1;
    private String stCommEntityID2;
    private String stCommEntityID3;
    private String stCommEntityID4;
    private String stBfeeEntityID1;
    private String stBfeeEntityID2;
    private String stFbaseEntityID1;
    private String stFbaseEntityID2;
    private String stHfeeEntityID1;
    private BigDecimal dbNDBrokerageAfterPPN;
    private String stAllowSamePeriodFlag;
    private String stValidateClaimFlag;
    private String stReference13;
    private Date dtClaimPaymentDate;
    private String stClaimPaymentUsedFlag;
    private String stGatewayDataFlag;
    private String stGatewayPolID;
    private BigDecimal dbAPPpnP;
    private String stCheckingFlag;
    private String stManualCoinsFlag;
    private BigDecimal dbCurrencyRateClaim;
    private String stClaimLocationID;
    private String stRiskApprovedWho;
    private Date dtRiskApprovedDate;
    private String stRiskApproved;
    private String stNotificationUserID;
    private String stEffectiveTempFlag;
    private String stMarketingOfficerWho;
    private String stReverseNotes;
    private Date dtClaimPaymentDate2;
    private String stDataSourceID;
    private String stNomorBuktiBayar;
    private Date dtTanggalBayar;
    private String stNomorReferensiPembayaran;
    private Date dtSparetimeDate;
    private boolean taxAcrualBases = true; //Parameter.readBoolean("AR_TAX_ACRUALBASES");

    private boolean isDouble = false;
    private String stDoubleDescription = "";
    private Date dtRejectDate;
    private String stClaimPengajuName;
    private String stClaimLossID;
    private Date dtClaimProposeDate;
    private BigDecimal dbClaimSubroPaid;
    private String stReferenceNo;

    private String stReference14;
    private String stReference15;
    private String stReference16;
    private String stClaimSubCauseID;
    private String stClaimSubCauseOther;
    private String stJumlahObjek;

    public String getStJumlahObjekTerproses() {
        return stJumlahObjekTerproses;
    }

    public void setStJumlahObjekTerproses(String stJumlahObjekTerproses) {
        this.stJumlahObjekTerproses = stJumlahObjekTerproses;
    }
    private String stJumlahObjekTerproses;

    public String getStJumlahObjek() {
        return stJumlahObjek;
    }

    public void setStJumlahObjek(String stJumlahObjek) {
        this.stJumlahObjek = stJumlahObjek;
    }

    public String getStClaimSubCauseOther() {
        return stClaimSubCauseOther;
    }

    public void setStClaimSubCauseOther(String stClaimSubCauseOther) {
        this.stClaimSubCauseOther = stClaimSubCauseOther;
    }

    public String getStClaimSubCauseID() {
        return stClaimSubCauseID;
    }

    public void setStClaimSubCauseID(String stClaimSubCauseID) {
        this.stClaimSubCauseID = stClaimSubCauseID;
    }

    public String getStReference14() {
        return stReference14;
    }

    public void setStReference14(String stReference14) {
        this.stReference14 = stReference14;
    }

    public String getStReference15() {
        return stReference15;
    }

    public void setStReference15(String stReference15) {
        this.stReference15 = stReference15;
    }

    public String getStReference16() {
        return stReference16;
    }

    public void setStReference16(String stReference16) {
        this.stReference16 = stReference16;
    }
    
    public String getStReferenceNo() {
        return stReferenceNo;
    }

    public void setStReferenceNo(String stReferenceNo) {
        this.stReferenceNo = stReferenceNo;
    }

    public Date getDtClaimProposeDate() {
        return dtClaimProposeDate;
    }

    public void setDtClaimProposeDate(Date dtClaimProposeDate) {
        this.dtClaimProposeDate = dtClaimProposeDate;
    }

    public String getStClaimLossID() {
        return stClaimLossID;
    }

    public void setStClaimLossID(String stClaimLossID) {
        this.stClaimLossID = stClaimLossID;
    }

    public String getStClaimPengajuName() {
        return stClaimPengajuName;
    }

    public void setStClaimPengajuName(String stClaimPengajuName) {
        this.stClaimPengajuName = stClaimPengajuName;
    }

    public Date getDtRejectDate() {
        return dtRejectDate;
    }

    public void setDtRejectDate(Date dtRejectDate) {
        this.dtRejectDate = dtRejectDate;
    }

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
    private DTOList coverageReins;
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
    private DTOList objectRestitusi;
    private DTOList clausules2;
    private DTOList items;
    private DTOList clausulesNew;
    private DTOList systemDocuments;
    private DTOList reinsInvoices;

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
        if (policyDocuments == null && stPolicyID != null && stPolicyTypeID != null) {
            policyDocuments = loadPolicyDocuments(stPolicyTypeID, stPolicyID, "POLICY");
        }

        return policyDocuments;
    }

    private static DTOList loadPolicyDocuments(String poltype, String stPolicyID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      b.*,c.description,a.ins_document_type_id "
                    + "   from "
                    + "      ins_documents a"
                    + "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id"
                    + "      left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id"
                    + "   where "
                    + "      a.pol_type_id=? "
                    + "      and a.document_class=? ",
                    new Object[]{stPolicyID, poltype, documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

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

    public DTOList getClaimDocuments() {
        if (claimDocuments == null && stPolicyID != null && stPolicyTypeID != null) {
            claimDocuments = loadDocuments(stPolicyTypeID, stPolicyID, "CLAIM");
        }
        return claimDocuments;
    }

    private static DTOList loadDocuments(String poltype, String stPolicyID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      b.*,c.description,a.ins_document_type_id "
                    + "   from "
                    + "      ins_documents a"
                    + "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id"
                    + "      left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id"
                    + "   where "
                    + "      a.pol_type_id=? "
                    + "      and a.document_class=? ",
                    new Object[]{stPolicyID, poltype, documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

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
                        InsurancePolicyTreatyView.class);
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

            if (pt != null) {
                clObjectClass = pt.getSplitClObjectClass();
            }

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

        boolean endorsement = isStatusEndorse() || isStatusEndorseRI();

        Date pstart = dtPeriodStart;

        try {
            if (endorsement) {
                pstart = getParentPolicy().getDtPeriodEnd();
            }
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
                        if (Tools.compare(dbPeriodRate, BDUtil.zero) < 0) {
                            dbPeriodRate = BDUtil.zero;
                        }
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

                        if (Tools.compare(dbPeriodRateBefore, BDUtil.zero) < 0) {
                            dbPeriodRateBefore = BDUtil.zero;
                        }
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
        validateEntity();
        validateDoubleObject();

        recalculateBasic();
        recalculateClaim();
        invokeCustomHandlers(true);

        recalculateBasic();
        invokeCustomHandlers(false);
        invokeCustomHandlersPolicy(false);
        recalculateClaim();

        recalculateEndorsePAKreasi();

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward() || isStatusClaimInward()) {
            validateTreaty(false); 
        } 
 
    }

    public void recalculateClaimOnly() throws Exception {

        if (getStNextStatus() != null) {
            if (getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)) {
                if (isStatusClaimPLA()) {

                    recalculateClaim();
                    invokeCustomHandlersClaimObject(true);
                    invokeCustomHandlersClaimObject(false);
                    invokeCustomHandlersPolicy(false);
                    recalculateClaim();

                } else {

                    recalculateBasic();
                    recalculateClaim();
                    invokeCustomHandlers(true);
                    recalculateBasic();
                    invokeCustomHandlers(false);
                    invokeCustomHandlersPolicy(false);
                    recalculateClaim();
                    recalculateEndorsePAKreasi();
                }
            } else {

                recalculateBasic();
                recalculateClaim();
                invokeCustomHandlers(true);
                recalculateBasic();
                invokeCustomHandlers(false);
                invokeCustomHandlersPolicy(false);
                recalculateClaim();
                recalculateEndorsePAKreasi();

            }
        } else {

            recalculateBasic();
            recalculateClaim();
            invokeCustomHandlers(true);
            recalculateBasic();
            invokeCustomHandlers(false);
            invokeCustomHandlersPolicy(false);
            recalculateClaim();
            recalculateEndorsePAKreasi();
        }

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()) {
            validateTreaty(false);
        }

        validateDoubleObject();

    }

    public void recalculateInterkoneksi() throws Exception {

        recalculateBasic();
        recalculateClaim();
        invokeCustomHandlers(true);
        recalculateBasic();
        invokeCustomHandlers(false);
        invokeCustomHandlersPolicy(false);
        recalculateClaim();
        recalculateEndorsePAKreasi();

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()) {
            validateTreaty(false);
        }

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

                        if (ri.getCoverage().size() >= cover.size()) {
                            break;
                        }

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

    public void recalculateClaim() throws Exception {

        if (isStatusClaim() || isStatusClaimEndorse() || isStatusClaimInward()) {

            InsuranceClaimCauseView claimCause = getClaimCauses();

            stClaimFinishFlag = "Y";

            getClaimItems();

            dbClaimDeductionAmount = null;
            dbClaimDeductionCustAmount = null;
            dbClaimAmount = null;
            dbClaimCustAmount = null;
            dbClaimREAmount = null;
            BigDecimal claimAmount = null;
            BigDecimal adjusterAmount = null;

            BigDecimal amtTotal = new BigDecimal(0);
            BigDecimal amtOSSubro = new BigDecimal(0);
            BigDecimal amtSubro = new BigDecimal(0);
            BigDecimal amtSubroBefore = new BigDecimal(0);
            BigDecimal amtDeduct = new BigDecimal(0);

            if (!getStClaimCurrency().equalsIgnoreCase("IDR")) {
                scale = 2;
            } else {
                scale = 2;
            }

            if (claimItems != null) {
                for (int i = 0; i < claimItems.size(); i++) {

                    InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                    final boolean claimg = Tools.isEqual(it.getInsuranceItem().getStItemType(), "CLAIMG");
                    final boolean deduct = Tools.isEqual(it.getInsuranceItem().getStItemType(), "DEDUCT");
                    final boolean penalty = Tools.isEqual(it.getInsuranceItem().getStItemType(), "PENALTY");
                    final boolean jasabengkel = Tools.isEqual(it.getInsuranceItem().getStItemType(), "BENGKELFEE");
                    final boolean sparepart = Tools.isEqual(it.getInsuranceItem().getStItemType(), "SPAREPART");
                    final boolean panjarKlaim = Tools.isEqual(it.getInsuranceItem().getStItemType(), "ADVPAYMENT");
                    final boolean adjusterFee = Tools.isEqual(it.getInsuranceItem().getStItemType(), "ADJ");
                    final boolean expensesFee = Tools.isEqual(it.getInsuranceItem().getStItemType(), "EXPFEE");
                    final boolean PPN = Tools.isEqual(it.getInsuranceItem().getStItemType(), "PPN");
                    final boolean subrogasi = Tools.isEqual(it.getInsuranceItem().getStItemType(), "SUBROGATION");

                    final boolean custAmount = claimg || deduct || jasabengkel || sparepart || adjusterFee || expensesFee;

                    if (panjarKlaim || PPN) {
                        continue;
                    }

                    if (isStatusClaim() || isStatusClaimInward()) {
                        if (isStatusClaimPLA()) {
                            claimAmount = getDbClaimAmountEstimate();
                        } else if (isStatusClaimDLA()) {
                            claimAmount = getDbClaimAmountApproved();
                        }
                    } else if (isStatusClaimEndorse()) {

                        
                        if (subrogasi && getDbClaimSubroPaid()!=null) {
                            claimAmount = it.getDbAmount();

                            amtDeduct = getTotalDeductible();
                            amtSubroBefore = getKlaimBefore();
                            amtSubro = BDUtil.add(getKlaimBefore(), claimAmount);

                            logger.logDebug("@@@@@@@@@@@@@@@@@@ deduct " + amtDeduct);
                            logger.logDebug("@@@@@@@@@@@@@@@@@@ klaimbefore " + getKlaimBefore());
                            logger.logDebug("@@@@@@@@@@@@@@@@@@ totalsubro " + getTotalSubrogasi());
                            logger.logDebug("@@@@@@@@@@@@@@@@@@ estimate " + getDbClaimAmountEstimate());
                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subrobefore " + amtSubroBefore);
                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subro " + amtSubro);

//                            amtTotal = BDUtil.sub(getDbClaimAmountEstimate(), amtSubroBefore);
//                            amtOSSubro = BDUtil.mulRound(getDbClaimAmountEstimate(), BDUtil.getRateFromPct(getDbClaimSubroPaid()), 0);
//                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subro1 " + amtOSSubro);
//                            amtOSSubro = BDUtil.sub(amtOSSubro, amtDeduct);
//                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subro2 " + amtOSSubro);
//                            amtOSSubro = BDUtil.sub(amtOSSubro, amtSubro);
//                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subro3 " + amtOSSubro);
//                            amtOSSubro = BDUtil.sub(amtOSSubro, amtSubroBefore);
//                            logger.logDebug("@@@@@@@@@@@@@@@@@@ subro4 " + amtOSSubro);


//                            setDbClaimAmountApproved(amtTotal);
//                            setDbClaimAmountEndorse(amtOSSubro);
                            setDbClaimAmountApproved(BDUtil.mul(getDbClaimAmountEstimate(), BDUtil.getRateFromPct(getDbClaimSubroPaid())));
//                            setDbClaimAmountEstimate(getDbClaimAmountApproved());
                            setDbClaimAmountEndorse(BDUtil.sub(getDbClaimAmountApproved(), amtSubro));
                        }

                        claimAmount = BDUtil.sub(getDbClaimAmountEndorse(), getDbClaimAmountApproved());

                        if(getDbClaimSubroPaid()!=null)
                            claimAmount = BDUtil.zero;
                    }

                    it.calculateRateAmount(claimAmount, scale);

                    final boolean chargeable = Tools.isYes(it.getStChargableFlag());

                    if (claimg) {
                        it.setDbAmount(claimAmount);
                    }

                    BigDecimal amt = it.getDbAmount();

                    ARTransactionLineView arTrxLine = it.getInsItem().getARTrxLine();

                    if (arTrxLine == null) {
                        throw new RuntimeException(" Unable to retrieve ARTRXLINE for ins item : " + it.getInsItem().getStInsuranceItemID());
                    }

                    final boolean neg = arTrxLine.isNegative();

                    if (neg) {
                        dbClaimDeductionAmount = BDUtil.add(dbClaimDeductionAmount, amt);
                    }
                    if (neg && custAmount) {
                        dbClaimDeductionCustAmount = BDUtil.add(dbClaimDeductionCustAmount, amt);
                    }

                    if (neg) {
                        amt = BDUtil.negate(amt);
                    }

                    if (chargeable) {
                        dbClaimREAmount = BDUtil.add(dbClaimREAmount, amt);
                    }

                    dbClaimAmount = BDUtil.add(dbClaimAmount, amt);

                    //if (custAmount)
                    dbClaimCustAmount = BDUtil.add(dbClaimCustAmount, amt);

                    if (neg && penalty) {
                        it.calculateRateAmount(dbClaimCustAmount, scale);
                    }

                    if (jasabengkel && getStClaimNumberID() != null) {
                        it.setStEntityID(getStClaimNumberID());
                        it.setStNPWP(getEntity2(getStClaimNumberID()).getStTaxFile());
                        it.setStTaxCode(getEntity2(getStClaimNumberID()).getStClaimTaxCode());
                    }

                    if (it.getStTaxCode() != null) {
                        boolean hasNPWP = it.getStNPWP() != null ? true : false;
                        BigDecimal ratePajak = getPPHRate(it.getStTaxCode(), hasNPWP, getDtPeriodStart());
                        boolean chargeTax = Tools.isYes(it.getInsItem().getStReinsuranceTaxChargeFlag());

                        if (it.isAutoTaxRate()) {
                            it.setDbTaxRate(ratePajak);
                        }

                        if (it.isAutoTaxAmount()) {
                            it.setDbTaxAmount(BDUtil.mul(it.getDbTaxRate(), it.getDbAmount(), scale));
                            if (jasabengkel && BDUtil.isZeroOrNull(it.getDbAmount())) {
                                it.setDbTaxAmount(BDUtil.mul(it.getDbTaxRate(), claimAmount, scale));
                            }
                        }

                        if (chargeTax) {
                            dbClaimAmount = BDUtil.sub(dbClaimAmount, it.getDbTaxAmount());
                            dbClaimCustAmount = BDUtil.sub(dbClaimCustAmount, it.getDbTaxAmount());
                            dbClaimREAmount = BDUtil.sub(dbClaimREAmount, it.getDbTaxAmount());
                        }

                    }

                    if (adjusterFee) {
                        adjusterAmount = it.getDbAmount();
                    }

                }
            }

            //PERHITUNGAN PPN

            for (int i = 0; i < claimItems.size(); i++) {

                InsurancePolicyItemsView itemPPN = (InsurancePolicyItemsView) claimItems.get(i);

                final boolean isPPN = Tools.isEqual(itemPPN.getInsuranceItem().getStItemType(), "PPN");

                if (!isPPN) {
                    continue;
                }

                itemPPN.calculateRateAmount(adjusterAmount, scale);

                //dbClaimCustAmount = BDUtil.add(dbClaimCustAmount, itemPPN.getDbAmount());
                dbClaimAmount = BDUtil.add(dbClaimAmount, itemPPN.getDbAmount());
                dbClaimREAmount = BDUtil.add(dbClaimREAmount, itemPPN.getDbAmount());
            }
            //END PPN

            //cek maksimal TSI untuk klaim
            if (claimObject != null) {
                BigDecimal tsiCurrency = BDUtil.mul(claimObject.getDbObjectInsuredAmount(), getDbCurrencyRate(), scale);

                if (BDUtil.biggerThan(claimAmount, tsiCurrency)) {
                    throw new RuntimeException("Jumlah Klaim " + claimAmount + " Melebihi TSI " + tsiCurrency);
                }
            }

            if (claimObject != null) {
                final DTOList treatyDetails = claimObject.getTreatyDetails();

                BigDecimal claimTot = dbClaimREAmount;
                BigDecimal sisaORXOL = BDUtil.zero;
                BigDecimal sisaORXOL1 = BDUtil.zero;
                BigDecimal sisaORXOL2 = BDUtil.zero;
                //final BigDecimal totalTSI = claimObject.getDbObjectInsuredAmountShare();
                BigDecimal totalTSI2 = claimObject.getDbObjectInsuredAmount();
                //final BigDecimal totalPremi = claimObject.getDbObjectPremiTotalAmount();
                //final HashMap trdMap = treatyDetails.getMapOf("ins_treaty_detail_id");
                BigDecimal totalTSITreaty = BDUtil.zero;

                if (!BDUtil.isZeroOrNull(claimObject.getDbLimitOfLiability())) {
                    //totalTSI2 = BDUtil.mul(totalTSI2, BDUtil.getRateFromPct(claimObject.getDbLimitOfLiability()), scale);
                }


                for (int i = 0; i < treatyDetails.size(); i++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(i);

                    totalTSITreaty = BDUtil.add(totalTSITreaty, trd.getDbTSIAmount());   
                }

//                if(!BDUtil.isEqual(totalTSI2, totalTSITreaty, scale))
//                    if(!BDUtil.isZeroOrNull(totalTSITreaty))
//                        totalTSI2 = totalTSITreaty;

                for (int i = 0; i < treatyDetails.size(); i++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(i);

                    if (!trd.isManualClaimSpreading()) {

                        trd.setDbClaimAmount(BDUtil.div(BDUtil.mul(trd.getDbTSIAmount(), claimTot, scale), totalTSI2));

                        if (scale == 0) {
                            trd.setDbClaimAmount(trd.getDbClaimAmount().setScale(scale, BigDecimal.ROUND_HALF_DOWN));
                        }

                        InsuranceTreatyView treatyView = claimObject.getTreaty();

                        final DTOList treatyDetails2 = treatyView.getDetails(stPolicyTypeID);

                        for (int j = 0; j < treatyDetails2.size(); j++) {
                            InsuranceTreatyDetailView tredetView = (InsuranceTreatyDetailView) treatyDetails2.get(j);


                            if (tredetView.getDbXOLLower() != null) {
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
                        }
                    }

                    final DTOList shares = trd.getShares();

                    for (int j = 0; j < shares.size(); j++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);

                        ri.setDbClaimAmount(BDUtil.mul(trd.getDbClaimAmount(), BDUtil.getRateFromPct(ri.getDbSharePct()), scale));

                        if (BDUtil.isZeroOrNull(ri.getDbSharePct())) {
                            BigDecimal proRate = BDUtil.div(ri.getDbTSIAmount(), trd.getDbTSIAmount(), 7);
                            ri.setDbClaimAmount(BDUtil.mulRound(proRate, trd.getDbClaimAmount(), scale));
                        }
                    }
                }

                //tes
                {

                    DTOList coinsMurni = getCoins2();
                    BigDecimal askridaSharePct = null;
                    BigDecimal askridaShareTSI = null;
                    BigDecimal askridaSharePremi = null;

                    for (int i = 0; i < coinsMurni.size(); i++) {
                        InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni.get(i);

                        if (coins3.isHoldingCompany()) {
                            askridaSharePct = coins3.getDbSharePct();
                            askridaShareTSI = coins3.getDbAmount();
                            askridaSharePremi = coins3.getDbPremiAmount();
                        }
                    }

                    if (!isManualCoins()) {
                        DTOList coins = getCoinsCoverage();
                        boolean manualClaim = false;
                        BigDecimal claimLeader = null;
                        for (int i = 0; i < coins.size(); i++) {
                            InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

                            //if (co.isHoldingCompany()) continue;
                            //khusus pa kreasi
                            if (stPolicyTypeID.equalsIgnoreCase("21")) {

                                if (co.isManualClaim()) {
                                } else {
                                    if (claimCause != null) {
                                        if (claimCause.getStExcClaimFlag() != null) {
                                            if (claimCause.getStExcClaimFlag().equalsIgnoreCase("Y")) {
                                                if (!co.isAutoClaimAmount() && !manualClaim) {
                                                    if (co.isHoldingCompany()) {
                                                        co.setDbClaimAmount(BDUtil.zero);
                                                        if (coins.size() == 1) {
                                                            co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(askridaSharePct)));
                                                        }
                                                    } else {
                                                        if (isStatusClaimPLA()) {
                                                            co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(askridaSharePct)));
                                                        } else if (isStatusClaimDLA()) //co.setDbClaimAmount(getDbClaimAmountApproved());
                                                        {
                                                            co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(askridaSharePct)));
                                                        }
                                                    }
                                                } else if (co.isAutoClaimAmount() || manualClaim) {
                                                    co.setStAutoClaimAmount("Y");
                                                    co.setDbClaimAmount(co.getDbClaimAmount());
                                                    if (!co.isHoldingCompany()) {
                                                        co.setDbClaimAmount(BDUtil.negate(claimLeader));
                                                    }
                                                    if (co.isHoldingCompany()) {
                                                        manualClaim = true;
                                                        claimLeader = co.getDbClaimAmount();
                                                    }
                                                }
                                            }
                                        } else if (claimCause.getStExcClaimFlag() == null) {
                                            if (!co.isAutoClaimAmount() && !manualClaim) {
                                                if (co.isHoldingCompany()) {
                                                    if (isStatusClaimPLA()) //co.setDbClaimAmount(getDbClaimAmountEstimate());
                                                    {
                                                        co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(askridaSharePct)));
                                                    } else if (isStatusClaimDLA()) //co.setDbClaimAmount(getDbClaimAmountApproved());
                                                    {
                                                        co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(askridaSharePct)));
                                                    }
                                                } else {
                                                    co.setDbClaimAmount(BDUtil.zero);
                                                }
                                            } else if (co.isAutoClaimAmount() || manualClaim) {
                                                //co.setDbClaimAmount(co.getDbClaimAmount());
                                                //if(co.isHoldingCompany()) manualClaim = true;
                                                co.setStAutoClaimAmount("Y");
                                                co.setDbClaimAmount(co.getDbClaimAmount());
                                                if (!co.isHoldingCompany()) {
                                                    co.setDbClaimAmount(BDUtil.negate(claimLeader));
                                                }
                                                if (co.isHoldingCompany()) {
                                                    manualClaim = true;
                                                    claimLeader = co.getDbClaimAmount();
                                                }
                                            }

                                            int tahun = Integer.parseInt(DateUtil.getYear(getDtPolicyDate()));
                                            int bulan = DateUtil.getMonthDigit(getDtPolicyDate());
                                            boolean beforeApril2008 = false;
                                            if (tahun < 2008) {
                                                beforeApril2008 = true;
                                            }
                                            if (tahun == 2008 && bulan < 4) {
                                                beforeApril2008 = true;
                                            }

                                            if (claimCause.getStInsuranceClaimCauseID().equalsIgnoreCase("3732")) {
                                                if (beforeApril2008) {
                                                    co.setDbClaimAmount(BDUtil.mul(dbClaimAmount, new BigDecimal(0.5)));
                                                }
                                            }

                                        }
                                    }


                                }

                            } else {

                                BigDecimal claimAmt = null;

                                if (isStatusClaimPLA()) {
                                    claimAmt = BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(co.getDbSharePct()), scale);
                                } else if (isStatusClaimDLA()) {
                                    claimAmt = BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(co.getDbSharePct()), scale);
                                }

                                co.setDbClaimAmount(claimAmt);
                            }
                        }
                    }

                    //

                    //KLAIM KO MURNI
                    DTOList coins2 = getCoins2();

                    for (int i = 0; i < coins2.size(); i++) {
                        InsurancePolicyCoinsView co2 = (InsurancePolicyCoinsView) coins2.get(i);

                        // if (co2.isHoldingCompany()) continue;

                        BigDecimal claimAmt = null;

                        if (isStatusClaimPLA()) {
                            if (!co2.isManualClaim()) {
                                claimAmt = BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(co2.getDbSharePct()), scale);
                            } else {
                                claimAmt = co2.getDbClaimAmount();
                            }
                        } else if (isStatusClaimDLA()) {
                            if (!co2.isManualClaim()) {
                                claimAmt = BDUtil.mul(dbClaimAmount, BDUtil.getRateFromPct(co2.getDbSharePct()), scale);
                            } else {
                                claimAmt = co2.getDbClaimAmount();
                            }
                        }

                        co2.setDbClaimAmount(claimAmt);
                    }
                }
            }

            if (getClaimCauses() != null) {
                setStClaimCauseDesc(getClaimCauses().getStDescription());
            }

        }


    }

    private void invokeCustomHandlers(boolean validate) {
        for (int i = 0; i < objects.size(); i++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

            //CustomHandlerManager.getInstance().onCalculateSplit(this, obj, validate);
        }
    }

    private void invokeCustomHandlersClaimObject(boolean validate) {

        InsurancePolicyObjectView obj = (InsurancePolicyObjectView) claimObject;

        //CustomHandlerManager.getInstance().onCalculate(this, obj, validate);

    }

    private void invokeTreatyCustomHandlers(InsurancePolicyObjectView obj2) {
        //CustomHandlerManager.getInstance().onCalculateTreaty(this, obj2);
    }

    public void invokeCustomCriteria(InsurancePolicyObjectView obj2) {
        //CustomHandlerManager.getInstance().customCriteria(this, obj2);
    }

    public void recalculateTreaty() throws Exception {
        if (getStCoverTypeCode() == null) {
            return;
        }
        if (getStPolicyTypeID() == null) {
            return;
        }

        try {
            int scale = 0;
            BigDecimal dbInsuranceCoinsScale = null;

            if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
                scale = 2;
            }

            if (hasCoIns()) {
                final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
                dbInsuranceCoinsScale = getDbCoinsSessionPct();
            }

            for (int i = 0; i < this.objects.size(); i++) {
                InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) objects.get(i);

                //final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();
                BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();

                final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

                BigDecimal tlrMaipark = null;
                if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {
                    tlrMaipark = ipo.getDbTreatyLimitRatioMaipark();
                }

                final DTOList treaties = ipo.getTreaties();

                if (treaties.size() < 1) {
                    final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

                    tre.markNew();
                    tre.setStInsuranceTreatyID(ipo.getStInsuranceTreatyID());
                    tre.setStPolicyID(stPolicyID);
                    treaties.add(tre);
                }

                final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
                tre.setObject(ipo);

                if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {

                    if (isStatusEndorse()) {
                        if (BDUtil.isZeroOrNull(insuredAmount)) {
                            insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());
                        }
                    }

                    if (!isStatusEndorseRI()) {
                        //tre.adjustRatioEarthquake(this, tlrMaipark, tlr, dbCurrencyRateTreaty, getStCurrencyCode());
                        tre.raiseToTSIEarthquake(getStPolicyTypeID(), insuredAmount, tlr, tlrMaipark, dbCurrencyRateTreaty, getStCurrencyCode());
                    }
                } else if (stPolicyTypeGroupID.equalsIgnoreCase("6")) {
                    invokeTreatyCustomHandlers(ipo);
                } else {
                    //if(isStatusEndorse())
                    //   if(BDUtil.isZeroOrNull(insuredAmount)) insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());

                    tre.adjustRatio(tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                    if (!isStatusEndorse() && !isStatusEndorseRI()) {
                        tre.raiseToTSI(getStPolicyTypeID(), insuredAmount, tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                    }

                }

                if (hasCoIns()) {
                    ipo.setDbObjectPremiTotalBeforeCoinsuranceAmount(ipo.getDbObjectPremiTotalAmount());
                    ipo.setDbObjectPremiTotalAmount(BDUtil.mul(ipo.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()), scale));
                }

                invokeTreatyCustomHandlers(ipo);
                tre.setStRateMethod(getStRateMethod());
                tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, dbInsuranceCoinsScale);

            }
        } catch (RuntimeException e) {
            setStInsuranceTreatyID(null);
            setStCoverTypeCode(null);
            throw e;
        }

    }

    public void recalculateTreatyClaimObject() throws Exception {
        if (getStCoverTypeCode() == null) {
            return;
        }
        if (getStPolicyTypeID() == null) {
            return;
        }

        try {
            int scale = 0;
            BigDecimal dbInsuranceCoinsScale = null;

            if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
                scale = 2;
            }

            if (hasCoIns()) {
                final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
                dbInsuranceCoinsScale = getDbCoinsSessionPct();
            }

            //for (int i = 0; i < this.objects.size(); i++) {
            InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) claimObject;

            //final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();
            BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();

            final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

            BigDecimal tlrMaipark = null;
            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {
                tlrMaipark = ipo.getDbTreatyLimitRatioMaipark();
            }

            final DTOList treaties = ipo.getTreaties();

            if (treaties.size() < 1) {
                final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

                tre.markNew();
                tre.setStInsuranceTreatyID(ipo.getStInsuranceTreatyID());
                tre.setStPolicyID(stPolicyID);
                treaties.add(tre);
            }

            final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
            tre.setObject(ipo);

            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {

                if (isStatusEndorse()) {
                    if (BDUtil.isZeroOrNull(insuredAmount)) {
                        insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());
                    }
                }

                if (!isStatusEndorseRI()) {
//                    tre.adjustRatioEarthquake(this, tlrMaipark, tlr, dbCurrencyRateTreaty, getStCurrencyCode());
                    tre.raiseToTSIEarthquake(getStPolicyTypeID(), insuredAmount, tlr, tlrMaipark, dbCurrencyRateTreaty, getStCurrencyCode());
                }
            } else if (stPolicyTypeGroupID.equalsIgnoreCase("6")) {
                invokeTreatyCustomHandlers(ipo);
            } else {
                //if(isStatusEndorse())
                //   if(BDUtil.isZeroOrNull(insuredAmount)) insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());

                tre.adjustRatio(tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                if (!isStatusEndorse() && !isStatusEndorseRI()) {
                    tre.raiseToTSI(getStPolicyTypeID(), insuredAmount, tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                }

            }

            if (hasCoIns()) {
                ipo.setDbObjectPremiTotalBeforeCoinsuranceAmount(ipo.getDbObjectPremiTotalAmount());
                ipo.setDbObjectPremiTotalAmount(BDUtil.mul(ipo.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()), scale));
            }

            invokeTreatyCustomHandlers(ipo);
            tre.setStRateMethod(getStRateMethod());
            tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, dbInsuranceCoinsScale);
            //tre.setTanggalReas();

            //}
        } catch (RuntimeException e) {
            setStInsuranceTreatyID(null);
            setStCoverTypeCode(null);
            throw e;
        }

    }

    public void recalculateTreatyInitialize() throws Exception {
        if (getStCoverTypeCode() == null) {
            return;
        }
        if (getStPolicyTypeID() == null) {
            return;
        }

        try {
            int scale = 0;
            BigDecimal dbInsuranceCoinsScale = null;

            if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
                scale = 2;
            }

            if (hasCoIns()) {
                final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
                dbInsuranceCoinsScale = getDbCoinsSessionPct();
            }

            for (int i = 0; i < this.objects.size(); i++) {
                InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) objects.get(i);

                //final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();
                BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();

                final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

                BigDecimal tlrMaipark = null;
                if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {
                    tlrMaipark = ipo.getDbTreatyLimitRatioMaipark();
                }

                final DTOList treaties = ipo.getTreaties();

                if (treaties.size() < 1) {
                    final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

                    tre.markNew();
                    tre.setStInsuranceTreatyID(ipo.getStInsuranceTreatyID());
                    tre.setStPolicyID(stPolicyID);
                    treaties.add(tre);
                }

                final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
                tre.setObject(ipo);

                if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {

                    if (isStatusEndorse()) {
                        if (BDUtil.isZeroOrNull(insuredAmount)) {
                            insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());
                        }
                    }

//                    tre.adjustRatioEarthquake(this, tlrMaipark, tlr, dbCurrencyRateTreaty, getStCurrencyCode());
                    tre.raiseToTSIEarthquake(getStPolicyTypeID(), insuredAmount, tlr, tlrMaipark, dbCurrencyRateTreaty, getStCurrencyCode());
                } else if (stPolicyTypeGroupID.equalsIgnoreCase("6")) {
                    invokeTreatyCustomHandlers(ipo);
                } else {
                    tre.adjustRatio(tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                    tre.raiseToTSI(getStPolicyTypeID(), insuredAmount, tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                }

                if (hasCoIns()) {
                    ipo.setDbObjectPremiTotalBeforeCoinsuranceAmount(ipo.getDbObjectPremiTotalAmount());
                    ipo.setDbObjectPremiTotalAmount(BDUtil.mul(ipo.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()), scale));
                }

                invokeTreatyCustomHandlers(ipo);
                tre.setStRateMethod(getStRateMethod());
                tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, dbInsuranceCoinsScale);

            }
        } catch (RuntimeException e) {
            setStInsuranceTreatyID(null);
            setStCoverTypeCode(null);
            throw e;
        }

    }

    public void recalculateTreatyReverse() throws Exception {
        if (getStCoverTypeCode() == null) {
            return;
        }
        if (getStPolicyTypeID() == null) {
            return;
        }

        try {
            int scale = 0;
            BigDecimal dbInsuranceCoinsScale = null;
            final String currencyCode = getStCurrencyCode();
            final String stEarthquakeTypeId = FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE;

            if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
                scale = 2;
            }

            if (hasCoIns()) {
                final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
                dbInsuranceCoinsScale = getDbCoinsSessionPct();
            }

            for (int i = 0; i < this.objects.size(); i++) {
                InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) objects.get(i);

                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) ipo;

                final BigDecimal insuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
                final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmount();

                final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

                BigDecimal tlrMaipark = null;
                if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {
                    tlrMaipark = ipo.getDbTreatyLimitRatioMaipark();
                }

                final DTOList treaties = ipo.getTreaties();

                if (treaties.size() < 1) {
                    final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

                    tre.markNew();
                    tre.setStInsuranceTreatyID(ipo.getStInsuranceTreatyID());
                    tre.setStPolicyID(stPolicyID);
                    treaties.add(tre);
                }

                final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
                tre.setObject(ipo);

                if ((stPolicyTypeGroupID.equalsIgnoreCase(stEarthquakeTypeId))) {
//                    tre.adjustRatioEarthquake(this, tlrMaipark, tlr, dbCurrencyRateTreaty, currencyCode);
                    tre.raiseToTSIEarthquake(getStPolicyTypeID(), insuredAmount, tlr, tlrMaipark, dbCurrencyRateTreaty, currencyCode);
                } else {
                    tre.adjustRatio(tlr, dbCurrencyRateTreaty, currencyCode, null);
                    tre.raiseToTSI(getStPolicyTypeID(), insuredAmount, tlr, dbCurrencyRateTreaty, currencyCode, null);
                }

                if (hasCoIns()) {
                    ipo.setDbObjectPremiTotalAmount(BDUtil.mul(ipo.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()), scale));
                }

                if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                    BigDecimal selisihPremi = null;

                    selisihPremi = BDUtil.sub(obj.getDbReference6(), obj.getDbReference2());

                    ipo.setDbObjectPremiTotalAmount(selisihPremi);
                }

                tre.setStRateMethod(getStRateMethod());
                tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, dbInsuranceCoinsScale);

            }
        } catch (RuntimeException e) {
            setStInsuranceTreatyID(null);
            setStCoverTypeCode(null);
            throw e;
        }

    }

    public BigDecimal getDbCoinsSessionPct() throws Exception {
        final SQLUtil S = new SQLUtil();
        BigDecimal session_pct = null;
        try {

            final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();

            final PreparedStatement PS = S.setQuery("select * "
                    + "from ins_co_scale "
                    + "where ( ? between scale_lower and scale_upper) and pol_type_id = ? "
                    + "and ( ? >= scale_period_start and ? <= scale_period_end);");

            if (this.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                if (this.getDbSharePct() == null) {
                    throw new RuntimeException("Share Askrida Belum Diisi");
                } else {
                    PS.setBigDecimal(1, this.getDbSharePct());
                }
            } else {
                PS.setBigDecimal(1, holdingCoin.getDbSharePct());
            }

            PS.setString(2, this.getStPolicyTypeID());
            S.setParam(3, this.getDtPeriodStart());
            S.setParam(4, this.getDtPeriodStart());

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                session_pct = RS.getBigDecimal("session_pct");
            }

            return session_pct;

        } finally {
            S.release();
        }


    }

    public void recalculateTreatyEndorse() throws Exception {
        try {
            recalculateTreaty();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        final DTOList parentObj = getParentPolicy().getObjects();

        final DTOList obj = getObjects();

        for (int i = 0; i < parentObj.size(); i++) {
            InsurancePolicyObjectView parentObjView = (InsurancePolicyObjectView) parentObj.get(i);

            final DTOList parentTreaties = parentObjView.getTreaties();
            final InsurancePolicyTreatyView parentTre = (InsurancePolicyTreatyView) parentTreaties.get(0);

            for (int k = 0; k < obj.size(); k++) {
                InsurancePolicyObjectView ObjView = (InsurancePolicyObjectView) obj.get(i);

                for (int j = 0; j < parentTre.getDetails().size(); j++) {
                    InsurancePolicyTreatyDetailView parentTredet = (InsurancePolicyTreatyDetailView) parentTre.getDetails().get(j);

                    final DTOList treaties = ObjView.getTreaties();
                    final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);

                    for (int l = 0; l < tre.getDetails().size(); l++) {
                        InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tre.getDetails().get(j);

                        if (parentTredet.getStInsuranceTreatyDetailID().equalsIgnoreCase(tredet.getStInsuranceTreatyDetailID())) {
                            tredet.setDbPremiAmount(BDUtil.sub(tredet.getDbPremiAmount(), parentTredet.getDbPremiAmount()));
                        }
                    }

                }
            }

        }
    }

    public void recalculateBasic() throws Exception { 
        if (stPolicyTypeID == null) {
            return;
        }

        loadClausules();
        loadDetails();
        loadObjects();

        stUnderwritingFinishFlag = "Y";
        isDouble = false;
        stDoubleDescription = "";

        if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
            scale = 2;
        } else {
            scale = 0;
        }

        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

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
            dbInsuredAmountEndorse = null;

            for (int i = 0; i < objects.size(); i++) {
                InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                InsuranceSplitPolicyObjDefaultView objc = (InsuranceSplitPolicyObjDefaultView) obj;

                if (isStatusDraft() || isStatusSPPA() || isStatusPolicy() || isStatusRenewal() || isStatusHistory()) {
                    objc.setStOrderNo(String.valueOf(i + 1));
                }

                if (isStatusEndorse() && objc.getStOrderNo() == null) {
                    objc.setStOrderNo(String.valueOf(i + 1));
                }

                obj.reCalculate();

                setCustomStringToPolicy(objc);

                dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(), dbCurrencyRate, scale));
                dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
                dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());
            }

        }

        if (dbPremiRate != null) {
            dbPremiBase = BDUtil.mul(dbInsuredAmountAfterKurs, dbPremiRate, scale);
        }

        dbPremiTotal = dbPremiBase;

        int size = objects.size();
        DTOList cls1 = new DTOList();

        if ((objects != null) && (objects.size() > 0)){
            if(size > 3000){
                InsuranceSplitPolicyObjectView obj1 = (InsuranceSplitPolicyObjectView) objects.get(0);

                cls1 = obj1.getClausules();
            }
        }
        
        for (int j = 0; j < objects.size(); j++) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(j);


            obj.reCalculate();

            obj.applyHighestRiskCode();

            obj.validateSplitPolis(this);

            //DateTime bulanProduksi = new DateTime(getDtPeriodStart());
            //DateTime bulanProduksi = new DateTime(new Date());
            //DateTime bulanOJK = new DateTime(getPolicyType().getDtReference1());

            if (getPolicyType().getStControlFlags() != null) {
                 {
                     {
                        obj.validateHighestRateOJK();
                        //obj.validateDeductibleOJK();
                    }
                }
            }
 
            dbPremiTotal = BDUtil.add(dbPremiTotal, obj.getDbObjectPremiTotalAmount());
            dbInsuredAmountEndorse = BDUtil.add(dbInsuredAmountEndorse, obj.getDbObjectInsuredAmountShareEndorse());

        }

        dbTotalFee = null;
        BigDecimal totalComission = null;
        BigDecimal totalDiscount = null;

        BigDecimal totalTax = null;
        totalKomisiTanpaPajak = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isPremi()) {
                continue;
            }

            item.calculateRateAmount(getDbInsuredAmount(), scale);

            dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
        }

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isDiscount()) {
                continue;
            }

            item.calculateRateAmount(dbPremiTotal, scale);

            totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
        }

        final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

        dbPremiTotalAfterDisc = totalAfterDiscount;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isFee()); else {
                continue;
            }

            if (item.isPPN() || item.isPPNFeeBase()) {
                continue;
            }

            item.calculateRateAmount(totalAfterDiscount, scale);

            dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
        }

        dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

        InsurancePolicyCoinsView co = getHoldingCoin();

        //BigDecimal dbTotalBrokerage = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if(co!=null){
                if (!BDUtil.isZeroOrNull(co.getDbCommissionAmount())) {
                    item.setDbAmount(co.getDbCommissionAmount());
                }
            }
            

            BigDecimal afterDiscount = totalAfterDiscount;

            if(!usePMKBaru){
                if (item.isBrokerFeeIncludePPN() || item.isPPNInclude()) {
                    afterDiscount = BDUtil.mul(BDUtil.div(new BigDecimal(100), new BigDecimal(110), 15), afterDiscount, scale);
                }
            }

            if(usePMKBaru){
                if (item.isPPNInclude()) {
                    afterDiscount = BDUtil.mul(BDUtil.div(item.getInsItem().getDbCalculationFactor1(), item.getInsItem().getDbCalculationFactor2(), 15), afterDiscount, scale);
                }
            }

            item.calculateRateAmount(afterDiscount, scale);

            totalComission = BDUtil.add(totalComission, item.getDbAmount());

            if (item.isAutoTaxRate()) {
                if (item.getStTaxCode() != null) {
                    item.setDbTaxRate(item.getTax().getDbRate());
                }
            }

            if (item.getStTaxCode() != null) {
                totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
            }

        }

        //set data polis komisi, bfee, hfee, biaya polis, materai, diskon
        recalculateFee();

        //recalculateAkumulasiKomisi();

        calculatePPH21And23();

        //tambahin pengecekan yg punya NPWP dan Tidak Punya NPWP
        /* jika ada NPWP = 2%
         * jika tidak ada NPWP = 4%
         *
         */

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if (item.isAutoTaxAmount()) {
                item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(), scale));
            }

            if (item.isAutoTaxAmount()) {
                if (item.getStTaxCode() != null) {
                    if (item.getTax().isPPH23BrokerInclude()) {
                        //item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), dbNDBrokerageAfterPPN, scale));
                    }
                }
            }

            //PERHITUNGAN UNTUK PERUBAHAN KE ACRUAL BASES
            if (isTaxAcrualBases()) {
                //totalComission = BDUtil.sub(totalComission, item.getDbTaxAmount());
            }

            //totalKomisiTanpaPajak = BDUtil.sub(dbTotalDue,BDUtil.sub(totalComission, item.getDbTaxAmount()));
        }

        //calculatePPH21Progressive();

        dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);

        if (!BDUtil.isZeroOrNull(dbNDPPN)) {
            dbPremiNetto = BDUtil.sub(dbPremiNetto, dbNDPPN);
        }

        

        {
            //final BigDecimal BD100 = new BigDecimal(100);

            BigDecimal checkInsAmount = null;
            BigDecimal checkPremiAmount = null;

            InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

            BigDecimal totPct = null;
            BigDecimal totTSI = null;
            BigDecimal premiAfterDisc = null;


            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                if (coin.isHoldingCompany()) {
                    continue;
                }


                //if (!coin.isEntryByPctRate()) {
                //    final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero) > 0;
                    /*BigDecimal pct;

                if (hasAmount)
                pct = BDUtil.div(coin.getDbAmount(), dbInsuredAmount);
                else
                pct = BDUtil.zero;

                coin.setDbSharePct(BDUtil.getPctFromRate(pct));*/

                //} else {
                //    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                //}

                if (coin.isEntryByPctRate()) {
                    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                    coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }


                //if (coin.isAutoPremi())
                //coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));

                if (!coin.isEntryByPctRate() && !coin.isAutoPremi() && !coin.isManualPremi()) {
                    coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5));
                    coin.setDbPremiAmount(BDUtil.mul(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5), dbPremiTotal, scale));
                }

                if (!coin.isEntryByPctRate() && coin.isAutoPremi()) {
                    //coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount,5));
                    coin.setDbPremiAmount(coin.getDbPremiAmount());
                }

                if (coin.isManualPremi()) {
                    //coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount,5));
                    coin.setDbPremiAmount(coin.getDbPremiAmount());
                }

                if (!BDUtil.isZeroOrNull(coin.getDbDiscountRate())) {
                    coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                }
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                
                if (!BDUtil.isZeroOrNull(coin.getDbCommissionRate())) {
                    coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                }
                if (!BDUtil.isZeroOrNull(coin.getDbBrokerageRate())) {
                    coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                }
                if (!BDUtil.isZeroOrNull(coin.getDbHandlingFeeRate())) {
                    coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));
                }

                totPct = BDUtil.add(totPct, coin.getDbSharePct());
                totTSI = BDUtil.add(totTSI, coin.getDbAmount());

                checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
                checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());

            }

            if (holdingcoin != null) {
                if (holdingcoin.isManualPremi()) {
                    holdingcoin.setDbPremiAmount(holdingcoin.getDbPremiAmount());
                }

                if (!holdingcoin.isManualPremi()) {
                    holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
                    holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
                    if (holdingcoin.isAutoPremi()) {
                        holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoin.getDbSharePct()), scale));
                    }
                }

                if (BDUtil.biggerThan(BDUtil.add(holdingcoin.getDbPremiAmount(), checkPremiAmount), dbPremiTotal)) {
                    holdingcoin.setDbPremiAmount(BDUtil.sub(dbPremiTotal, checkPremiAmount));
                }

                holdingcoin.setDbDiscountAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbDiscountRate()), scale));
                holdingcoin.setDbCommissionAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbCommissionRate()), scale));
                holdingcoin.setDbBrokerageAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbBrokerageRate()), scale));

                checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
            }

            dbCoinsCheckInsAmount = checkInsAmount;
        }

        reCalculateInstallment();

        if (isStatusEndorse() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryEndorsemen()) {
            recalculateEndorsement();
        }

    }

    private void recalculateFee() {
        BigDecimal[] komisi = new BigDecimal[4];
        BigDecimal[] taxKomisi = new BigDecimal[4];
        String[] agenKomisi = new String[4];
        BigDecimal dbTotalFeeBase = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isKomisi()); else {
                continue;
            }

            if (item.isFeeBase()) {
                dbTotalFeeBase = BDUtil.add(dbTotalFeeBase, item.getDbAmount());
            }

            if (item.isKomisi() && !item.isFeeBase()) {
                if (komisi[0] == null) {
                    komisi[0] = item.getDbAmount();
                    taxKomisi[0] = item.getDbTaxAmount();
                    taxKomisi[0] = item.getDbTaxAmount();
                    agenKomisi[0] = item.getStEntityID();
                    continue;
                }
                if (komisi[1] == null) {
                    komisi[1] = item.getDbAmount();
                    taxKomisi[1] = item.getDbTaxAmount();
                    taxKomisi[1] = item.getDbTaxAmount();
                    agenKomisi[1] = item.getStEntityID();
                    continue;
                }
                if (komisi[2] == null) {
                    komisi[2] = item.getDbAmount();
                    taxKomisi[2] = item.getDbTaxAmount();
                    taxKomisi[2] = item.getDbTaxAmount();
                    agenKomisi[2] = item.getStEntityID();
                    continue;
                }
                if (komisi[3] == null) {
                    komisi[3] = item.getDbAmount();
                    taxKomisi[3] = item.getDbTaxAmount();
                    taxKomisi[3] = item.getDbTaxAmount();
                    agenKomisi[3] = item.getStEntityID();
                    continue;
                }
            }

            /*
            if (komisi[0] == null) {
            if(item.getStTaxCode().equalsIgnoreCase("2")){
            komisi[0] = item.getDbAmount();
            taxKomisi[0] = item.getDbTaxAmount();
            continue;
            }
            }
            if (komisi[1] == null) {
            if(item.getStTaxCode().equalsIgnoreCase("1")){
            komisi[1] = item.getDbAmount();
            taxKomisi[1] = item.getDbTaxAmount();
            continue;
            }
            }
            if (komisi[2] == null) {
            if(item.getStTaxCode().equalsIgnoreCase("1")){
            komisi[2] = item.getDbAmount();
            taxKomisi[2] = item.getDbTaxAmount();
            continue;
            }
            }
            if (komisi[3] == null) {
            if(item.getStTaxCode().equalsIgnoreCase("1")){
            komisi[3] = item.getDbAmount();
            taxKomisi[3] = item.getDbTaxAmount();
            continue;
            }
            }*/
        }
        dbNDComm1 = komisi[0];
        dbNDComm2 = komisi[1];
        dbNDComm3 = komisi[2];
        dbNDComm4 = komisi[3];

        dbNDTaxComm1 = taxKomisi[0];
        dbNDTaxComm2 = taxKomisi[1];
        dbNDTaxComm3 = taxKomisi[2];
        dbNDTaxComm4 = taxKomisi[3];

        stCommEntityID1 = agenKomisi[0];
        stCommEntityID2 = agenKomisi[1];
        stCommEntityID3 = agenKomisi[2];
        stCommEntityID4 = agenKomisi[3];

        BigDecimal[] brokerfee = new BigDecimal[2];
        BigDecimal[] brokerfeepct = new BigDecimal[2];
        BigDecimal[] taxbrokerfee = new BigDecimal[2];
        BigDecimal dbTotalBrokerage = null;

        String[] agenBrokerfee = new String[2];

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isBrokerFee()); else {
                continue;
            }

            dbTotalBrokerage = BDUtil.add(dbTotalBrokerage, item.getDbAmount());

            if (brokerfee[0] == null) {
                brokerfee[0] = item.getDbAmount();
                brokerfeepct[0] = item.getDbRatePct();
                taxbrokerfee[0] = item.getDbTaxAmount();
                taxbrokerfee[0] = item.getDbTaxAmount();
                agenBrokerfee[0] = item.getStEntityID();
                continue;
            }
            if (brokerfee[1] == null) {
                brokerfee[1] = item.getDbAmount();
                brokerfeepct[1] = item.getDbRatePct();
                taxbrokerfee[1] = item.getDbTaxAmount();
                taxbrokerfee[1] = item.getDbTaxAmount();
                agenBrokerfee[1] = item.getStEntityID();
                continue;
            }
        }

        dbNDBrok1 = brokerfee[0];
        dbNDBrok2 = brokerfee[1];
        dbNDBrok1Pct = brokerfeepct[0];
        dbNDBrok2Pct = brokerfeepct[1];

        dbNDTaxBrok1 = taxbrokerfee[0];
        dbNDTaxBrok2 = taxbrokerfee[1];

        stBfeeEntityID1 = agenBrokerfee[0];
        stBfeeEntityID2 = agenBrokerfee[1];

        dbNDSFee = null;
        dbNDPCost = null;
        dbTotalFee = null;
        dbNDPPN = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isHandlingFee() || item.isStampFee() || item.isPolicyCost() || item.isPPN() || item.isPPNFeeBase()) {
                if (item.isHandlingFee()) {
                    dbNDHFee = item.getDbAmount();
                    dbNDHFeePct = item.getDbRatePct();
                    dbNDTaxHFee = item.getDbTaxAmount();

                    stHfeeEntityID1 = item.getStEntityID();
                } else if (item.isStampFee()) {
                    dbNDSFee = item.getDbAmount();
                } else if (item.isPolicyCost()) {
                    dbNDPCost = item.getDbAmount();
                } else if (item.isPPN()) {
                    item.calculateRateAmount(dbTotalBrokerage, scale);
                    dbNDPPN = item.getDbAmount();
                } else if (item.isPPNFeeBase()) {
                    item.calculateRateAmount(dbTotalFeeBase, scale);
                    dbNDPPN = item.getDbAmount();
                }
            } else {
                continue;
            }
        }

        dbTotalFee = BDUtil.add(dbNDSFee, dbNDPCost);
        //dbTotalFee = BDUtil.sub(dbTotalFee, dbNDPPN);
        //dbTotalDue = BDUtil.sub(dbTotalDue, dbNDPPN);

        dbNDBrokerageAfterPPN = BDUtil.sub(dbTotalBrokerage, dbNDPPN);

        BigDecimal[] discount = new BigDecimal[2];
        BigDecimal[] discountpct = new BigDecimal[2];
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isDiscount()); else {
                continue;
            }

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

        dbNDFeeBase1 = null;
        dbNDFeeBase2 = null;
        BigDecimal[] feebase = new BigDecimal[2];

        String[] agenFeebase = new String[2];

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isFeeBase()); else {
                continue;
            }

            if (feebase[0] == null) {
                feebase[0] = item.getDbAmount();
                agenFeebase[0] = item.getStEntityID();
                continue;
            }
            if (feebase[1] == null) {
                feebase[1] = item.getDbAmount();
                agenFeebase[1] = item.getStEntityID();
                continue;
            }

        }

        dbNDFeeBase1 = feebase[0];
        dbNDFeeBase2 = feebase[1];

        stFbaseEntityID1 = agenFeebase[0];
        stFbaseEntityID2 = agenFeebase[1];

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

            if (item.isComission()); else {
                continue;
            }

            if ("1".equalsIgnoreCase(item.getStTaxCode())
                    || "4".equalsIgnoreCase(item.getStTaxCode())
                    || "7".equalsIgnoreCase(item.getStTaxCode())) {
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
            {"-1", "0.30"},};

        BigDecimal taxAmount = null;

        BigDecimal dbPPH21totS = dbPPH21tot;

        for (int i = 0; i < taxTab.length; i++) {
            String[] t = taxTab[i];

            BigDecimal amt = null;

            BigDecimal lim = new BigDecimal(t[0]);

            if (Tools.compare(lim, BDUtil.zero) < 0) {
                lim = dbPPH21tot;
            }

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

            if (Tools.compare(dbPPH21tot, BDUtil.zero) <= 0) {
                break;
            }
        }

        BigDecimal actPct = BDUtil.div(taxAmount, dbPPH21totS, 5);

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

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

    private void calculatePPH21And23() throws Exception {

        BigDecimal dbPPH23tot = null;
        BigDecimal taxAmount = null;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission() || item.isJasaBengkel()); else {
                continue;
            }

            dbPPH23tot = BDUtil.add(dbPPH23tot, item.getDbAmount());

            /*
            if ("2".equalsIgnoreCase(item.getStTaxCode()) ||
            "5".equalsIgnoreCase(item.getStTaxCode()) ||
            "8".equalsIgnoreCase(item.getStTaxCode())) {
            dbPPH23tot = BDUtil.add(dbPPH23tot, item.getDbAmount());
            }*/
            boolean hasNPWP = item.getStNPWP() != null ? true : false;

            if (item.getStNPWP() != null) {
                if (item.getStNPWP().equalsIgnoreCase("")) {
                    hasNPWP = false;
                }
            }

            BigDecimal ratePajak = getPPHRate(item.getStTaxCode(), hasNPWP, getDtPeriodStart());

            if (item.isAutoTaxRate()) {
                item.setDbTaxRate(ratePajak);
            }

            taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, BDUtil.getRateFromPct(item.getDbTaxRate())));
            /*
            if (item.getStNPWP()!=null) {
            taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, ratePajak, scale));
            } else if (item.getStNPWP()==null) {
            taxAmount = BDUtil.add(taxAmount, BDUtil.mul(dbPPH23tot, new BigDecimal(0.04), scale));
            }*/
        }

        BigDecimal dbPPH23totS = dbPPH23tot;

        BigDecimal actPct = BDUtil.div(taxAmount, dbPPH23totS);
        /*
        for (int i = 0; i < details.size(); i++) {
        InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

        if (item.isComission()) ;
        else continue;

        if(item.isAutoTaxRate())
        item.setDbTaxRate(actPct);

        if(item.isAutoTaxRate() && item.isAutoTaxAmount())
        item.setDbTaxAmount(taxAmount);


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

        }}*/

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
        //if(getParentPolicy()==null) throw new RuntimeException("Parent Polis Tidak Ada");
        if (getParentPolicy() != null) {
            dbInsuredAmountEndorse = BDUtil.sub(dbInsuredAmount, getParentPolicy().getDbInsuredAmount());
        }

    }

    public InsuranceSplitPolicyView getParentPolicy() {
        return (InsuranceSplitPolicyView) DTOPool.getInstance().getDTO(InsuranceSplitPolicyView.class, stParentID);
    }

    public InsurancePeriodView getInsurancePeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
    }

    public void reCalculateInstallment() throws Exception {
        getInstallment();

        if (installment == null) {
            installment = new DTOList();
        }

        //final long n = getLgInstallmentPeriods().longValue();

        //final int o = getLgInstallmentPeriods().intValue();

        final InsurancePeriodView instPeriod = getInstallmentPeriod();

        //final BigDecimal periodAmount = BDUtil.div(dbPremiNetto, new BigDecimal(n));

        //final BigDecimal roundingErr = BDUtil.sub(dbPremiNetto, BDUtil.mul(periodAmount, new BigDecimal(n)));

        //final BigDecimal periodAmount = BDUtil.div(dbPremiTotalAfterDisc, new BigDecimal(n));

        final BigDecimal periodAmount = BDUtil.div(dbPremiTotalAfterDisc, new BigDecimal(installment.size()));

        //final BigDecimal roundingErr = BDUtil.sub(dbPremiTotalAfterDisc, BDUtil.mul(periodAmount, new BigDecimal(n)));
        final BigDecimal roundingErr = BDUtil.sub(dbPremiTotalAfterDisc, BDUtil.mul(periodAmount, new BigDecimal(installment.size())));

        Date perDate = dtPolicyDate;

        if (perDate == null) {
            return;
        }

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
                if (i == 0) {
                    inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
                }
                if (i == 0) {
                    inst.setDbAmount(BDUtil.add(inst.getDbAmount(), dbTotalFee));
                }
                inst.setDtDueDate(perDate);
            }
            /*
            inst.setDbAmount(periodAmount);
            if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
            if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), dbTotalFee));

            inst.setDtDueDate(perDate);

            installment.add(inst);*/

            if (instPeriod != null) {
                perDate = instPeriod.advance(perDate);
            }

            total = BDUtil.add(total, inst.getDbAmount());
        }

        if (!Tools.isEqual(total, dbTotalDue)) {
            //throw new RuntimeException("Total Premi Cicilan Tidak Sama Dengan Tagihan Netto");
        }
    }

    public DTOList getInstallment() throws Exception {
        if (installment == null) {
            loadInstallment();
        }
        return installment;
    }

    public void setInstallment(DTOList installment) {
        this.installment = installment;
    }

    public void loadInstallment() throws Exception {
        try {
            if (installment == null) {
                installment = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_installment"
                        + "   where"
                        + "      policy_id = ? order by ins_pol_inst_id",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyInstallmentView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InsurancePeriodView getInstallmentPeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, stInstallmentPeriodID);
    }

    public DTOList getCoins() {
        if (coins == null) {
            loadCoins2();
        }
        return coins;
    }

    public DTOList getCoins2() {
        if (coins == null) {
            loadCoins();
        }
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

    public DTOList getSuminsureds() {
        return suminsureds;
    }

    public void setSuminsureds(DTOList suminsureds) {
        this.suminsureds = suminsureds;
    }

    public InsurancePolicyEntityView getOwner() {
        if (owner == null) {
            if (entities != null) {
                for (int i = 0; i < entities.size(); i++) {
                    InsurancePolicyEntityView ent = (InsurancePolicyEntityView) entities.get(i);
                    if (ent.isPolicyOwner()) {
                        owner = ent;
                        break;
                    }
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
        if (!isAutoLoadEnabled()) {
            return;
        }
        try {
            if (entities == null) {
                entities = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_entity where pol_id = ?",
                        new Object[]{stPolicyID},
                        InsurancePolicyEntityView.class);
            }
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

        if (ent == null) {
            return null;
        }

        return ent.getStEntityName();
    }

    public EntityView getEntity() {

        if (entity != null) {
            if (!Tools.isEqual(entity.getStEntityID(), stEntityID)) {
                entity = null;
            }
        }

        if (entity == null) {
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
        }

        return entity;
    }

    public EntityView getEntity2(String stEntID) {

        if (entity != null) {
            if (!Tools.isEqual(entity.getStEntityID(), stEntID)) {
                entity = null;
            }
        }

        if (entity == null) {
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);
        }

        return entity;
    }

    public void loadClausules() {
        //if (!isAutoLoadEnabled()) return;

        try {
            if (clausules == null && stPolicyTypeID != null) {
                clausules = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      a.*,b.description,b.description_new,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default,b.child_clausules,b.parent_id  "
                        + "   from "
                        + "      ins_clausules b "
                        + "      left join ins_pol_clausules a on "
                        + "         a.ins_clause_id = b.ins_clause_id"
                        + "         and a.pol_id = ? "
                        + "         and a.ins_pol_obj_id is null"
                        + "   where b.pol_type_id = ? and cc_code = ? "
                        + "   order by b.orderseq, b.shortdesc",
                        new Object[]{stPolicyID, stPolicyTypeID, stCostCenterCode},
                        InsurancePolicyClausulesView.class);

                String ent_name = "";
                if (getEntity() != null) {
                    ent_name = getEntity().getStEntityName();
                }

                for (int i = 0; i < clausules.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                    icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

                    if (icl.getStPolicyID() != null) {
                        icl.select();
                        icl.markUpdate();
                    } else {
                        icl.setDbRate(icl.getDbRateDefault());
                        icl.markNew();
                    }

                    icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
                }

                if (isNew()) {
                    for (int i = 0; i < clausules.size(); i++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                        icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

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

        /*
        try {
        if (clausules==null) {
        clausules = ListUtil.getDTOListFromQuery(
        "select a.*,b.shortdesc from ins_pol_clausules a inner join ins_clausules b on a.ins_clause_id = b.ins_clause_id where pol_id =? order by ins_pol_claus_id",
        new Object [] {getStPolicyID()},
        InsurancePolicyClausulesView.class
        );
        }
        } catch (Exception e) {
        throw new RuntimeException(e);
        }*/
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
            if (claimItems == null) {
                claimItems = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='CLM' order by ins_pol_item_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class);
            }
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
            if (details == null) {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='PRM'",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class);
            }
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
        if (stPolicyTypeID == null) {
            return null;
        }
        if (stPolicyTypeDesc == null) {
            final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
            if (ptype != null) {
                stPolicyTypeDesc = ptype.getStShortDescription();
            }
            //stPolicyTypeDesc = ptype.getStDescription();

            if (stPolicySubTypeID != null) {
                final InsurancePolicySubTypeView polsubtype = (InsurancePolicySubTypeView) DTOPool.getInstance().getDTO(InsurancePolicySubTypeView.class, stPolicySubTypeID);
                if (polsubtype != null) {
                    stPolicyTypeDesc += " / " + polsubtype.getStDescription();
                }
            }
        }

        return stPolicyTypeDesc;
    }

    public String getStPolicyTypeDesc2() {
        if (stPolicyTypeID == null) {
            return null;
        }
        if (stPolicyTypeDesc == null) {
            final InsurancePolicyTypeView ptype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
            if (ptype != null) {
                stPolicyTypeDesc = ptype.getStDescription();
            }

            if (stPolicySubTypeID != null) {
                final InsurancePolicySubTypeView polsubtype = (InsurancePolicySubTypeView) DTOPool.getInstance().getDTO(InsurancePolicySubTypeView.class, stPolicySubTypeID);
                if (polsubtype != null) {
                    stPolicyTypeDesc += " / " + polsubtype.getStDescription();
                }
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
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getObjectsCI() {
        loadObjectsCI();
        return objects;
    }

    public void loadObjectsCI() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ins_pol_obj_id",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
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

        if (l == -1) {
            return "?";
        }

        return l + "";
    }

    public long getLgPeriodLengthDays() {
        if (dtPeriodStart == null || dtPeriodEnd == null) {
            return -1;
        }

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
            if (deductibles == null) {
                deductibles = ListUtil.getDTOListFromQuery(
                        "select * from ins_split_pol_deduct where pol_id = ? and ins_pol_obj_id is null",
                        new Object[]{stPolicyID},
                        InsuranceSplitPolicyDeductibleView.class);
            }
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

        if (cs == null) {
            return false;
        }

        return cs.isLeader();
    }

    public boolean isMember() {

        final InsuranceCoverSourceView cs = getCoverSource();

        if (cs == null) {
            return false;
        }

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

            String trx_type_id = getCoverSource().getStARTransactionTypeID();

            if (isStatusClaim() || isStatusClaimEndorse()) {
                trx_type_id = "12";
            }

            arinvoices = ListUtil.getDTOListFromQuery(
                    "      select "
                    + "         a.*,"
                    + "         ("
                    + "            select max(b.receipt_date) from ar_receipt_lines c, ar_receipt b\n"
                    + "            where c.ar_invoice_id=a.ar_invoice_id and c.receipt_id=b.ar_receipt_id"
                    + "         ) as payment_date"
                    + "      from "
                    + "         ar_invoice   a"
                    + "      where "
                    + "         coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id = ? and attr_pol_id = ? order by a.ar_invoice_id",
                    new Object[]{trx_type_id, getStPolicyID()},
                    ARInvoiceView.class);

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

        if (isStatusClaim()) {
            return stat + "/" + getStClaimStatus();
        }

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

    public boolean isStatusEndorseIntern() {
        return FinCodec.PolicyStatus.ENDORSEIN.equalsIgnoreCase(getStCurrentStatus());
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

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String month2Digit = DateUtil.getMonth2Digit(getDtPolicyDate());
        String counterKey = month2Digit + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        //String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);


        stReference1 =
                getDigit(policyType2Digit, 2) + // C
                ccCode + // D
                regCode + // E
                counterKey + // Fg
                orderNo + //H
                "00" //I
                ;
    }

    public void generatePolicyNo() throws Exception {
        

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String month2Digit = DateUtil.getMonth2Digit(getDtPolicyDate());
        String counterKey = month2Digit + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        //String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID(policyType2Digit + year2Digit + ccCode, 1)), '0', 4);

        String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("SPLIT" + policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789


        stPolicyNo =
                getDigit(policyType2Digit, 2) + // C
                ccCode + // D
                regCode + // E
                counterKey + // Fg
                orderNo + //H
                "00" //I
                ;
    }

    public void generatePolicyNoWithoutCounter() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        EntityView entity = getEntity();

        if (getStPolicyTypeGroupID().equalsIgnoreCase("7")) {
            final DTOList obj = getObjects();
            InsurancePolicyObjDefaultView object = (InsurancePolicyObjDefaultView) obj.get(0);
            entity = getEntity2(object.getStReference2());
        }

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate())
                + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789


        stPolicyNo =
                coasCode + // A
                custCategory + // B
                getDigit(policyType2Digit, 2) + // C
                ccCode + // D
                regCode + // E
                counterKey;
    }

    public void generatePLANo() throws Exception {
        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate())
                + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        String orderNo = null;

        orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim" + policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        /*
        stPLANo = "LKS/" + getDigit(policyType2Digit, 2) + "/" +
        ccCode + "/" + month2Digit + year2Digit + "/" +
        StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim", 1)), '0', 4);
         */

        stPLANo = "LKS/" + getDigit(policyType2Digit, 2) + "/"
                + ccCode + "/" + month2Digit + year2Digit + "/"
                + orderNo;

    }

    public void generateDLANo() throws Exception {

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate())
                + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        stDLANo = "LKP/" + getDigit(policyType2Digit, 2) + "/"
                + ccCode + "/" + getStPLANo().substring(10, 14) + "/"
                + //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);
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
                DateUtil.getMonth2Digit(getDtPolicyDate())
                + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        dla = "LKP/" + getDigit(policyType2Digit, 2) + "/"
                + ccCode + "/" + month2Digit + year2Digit + "/"
                + //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim",1)),'0',4);
                getStPLANo().substring(15);

        return dla;

    }

    public RegionView getRegion() {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegionID);

        return reg;
    }

    private String getDigit(String code, int i) {
        if ((code == null) || (code.length() < 1)) {
            code = "";
        }

        code = code + "000000000000000000";

        code = code.substring(0, i);

        return code;
    }

    public void generateEndorseNo() {

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        final char[] policyno = stPolicyNo.toCharArray();

        final String enos = stPolicyNo.substring(16, 18);

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 2);

        final char[] ze = z.toCharArray();

        policyno[16] = ze[0];
        policyno[17] = ze[1];

        stPolicyNo = new String(policyno);

        /*
        stPolicyNo = stPolicyNo.substring(0,4) +
        ccCode +
        regCode +
        stPolicyNo.substring(8);*/

        /*final int n = policyno.length-1;

        if (policyno[n]=='9') policyno[n]='A';
        else if (policyno[n]=='Z') policyno[n]='a';
        else if (policyno[n]=='z') throw new RuntimeException("Running out endorsement code");
        else
        policyno[n]++;

        stPolicyNo = new String(policyno);*/
    }

    public void generateEndorseNoPAKreasi() {

        final char[] policyno = stPolicyNo.toCharArray();

        final String enosEnd = stPolicyNo.substring(16, 18);

        String enos = stPolicyNo.substring(8, 12);
        if (enosEnd.equalsIgnoreCase("00")) {
            enos = "0000";
        }

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 4);

        final char[] ze = z.toCharArray();

        policyno[8] = ze[0];
        policyno[9] = ze[1];
        policyno[10] = ze[2];
        policyno[11] = ze[3];

        final char[] zee = "01".toCharArray();

        policyno[16] = zee[0];
        policyno[17] = zee[1];

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
        /*
        final String enos = stDLANo.substring(16, 19);
        
        int eNo = Integer.parseInt(enos);
        
        eNo += 1;
        
        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 3);
        
        final char[] ze = z.toCharArray();
        
        dlano[16] = ze[0];
        dlano[17] = ze[1];
        dlano[18] = ze[2];*/
        //   LKP/21/11/0520/0036
        String noLKP = stDLANo.replaceAll("LKP", "");

        if (noLKP.endsWith("A")) {
            noLKP = noLKP.replaceAll("A", "B");
        } else if (noLKP.endsWith("B")) {
            noLKP = noLKP.replaceAll("B", "C");
        } else if (noLKP.endsWith("C")) {
            noLKP = noLKP.replaceAll("C", "D");
        } else if (noLKP.endsWith("D")) {
            noLKP = noLKP.replaceAll("D", "E");
        } else if (noLKP.endsWith("E")) {
            noLKP = noLKP.replaceAll("E", "F");
        } else if (noLKP.endsWith("F")) {
            noLKP = noLKP.replaceAll("F", "G");
        } else if (noLKP.endsWith("G")) {
            noLKP = noLKP.replaceAll("G", "H");
        } else if (noLKP.endsWith("H")) {
            noLKP = noLKP.replaceAll("H", "I");
        } else if (noLKP.endsWith("I")) {
            noLKP = noLKP.replaceAll("I", "J");
        } else if (noLKP.endsWith("J")) {
            noLKP = noLKP.replaceAll("J", "K");
        } else if (noLKP.endsWith("K")) {
            noLKP = noLKP.replaceAll("K", "L");
        } else if (noLKP.endsWith("L")) {
            noLKP = noLKP.replaceAll("L", "M");
        } else if (noLKP.endsWith("M")) {
            noLKP = noLKP.replaceAll("M", "N");
        } else if (noLKP.endsWith("N")) {
            noLKP = noLKP.replaceAll("N", "O");
        } else if (noLKP.endsWith("O")) {
            noLKP = noLKP.replaceAll("O", "P");
        } else if (noLKP.endsWith("P")) {
            noLKP = noLKP.replaceAll("P", "Q");
        } else if (noLKP.endsWith("Q")) {
            noLKP = noLKP.replaceAll("Q", "R");
        } else if (noLKP.endsWith("R")) {
            noLKP = noLKP.replaceAll("R", "S");
        } else if (noLKP.endsWith("S")) {
            noLKP = noLKP.replaceAll("S", "T");
        } else if (noLKP.endsWith("T")) {
            noLKP = noLKP.replaceAll("T", "U");
        } else if (noLKP.endsWith("U")) {
            noLKP = noLKP.replaceAll("U", "V");
        } else if (noLKP.endsWith("V")) {
            noLKP = noLKP.replaceAll("V", "W");
        } else if (noLKP.endsWith("W")) {
            noLKP = noLKP.replaceAll("W", "X");
        } else if (noLKP.endsWith("X")) {
            noLKP = noLKP.replaceAll("X", "Y");
        } else if (noLKP.endsWith("Y")) {
            noLKP = noLKP.replaceAll("Y", "Z");
        } else {
            noLKP = noLKP + "A";
        }

        stDLANo = "LKP"+ noLKP;

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

        if (baseu.longValue() == 100) {
            return pr + " %";
        }

        return pr + " / " + baseu;
    }

    private PeriodBaseView getPeriodBase() {
        return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, stPeriodBaseID);
    }

    public BigDecimal getDbPeriodRateFactor() {

        final PeriodBaseView periodBase = getPeriodBase();

        if (periodBase == null) {
            return null;
        }

        final BigDecimal baseu = periodBase.getDbBaseUnit();

        if (dbPeriodRate == null || baseu == null) {
            return null;
        }

        return dbPeriodRate.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getDbPeriodRateBeforeFactor() {

        final PeriodBaseView pbb = getPeriodBaseBefore();

        if (pbb == null) {
            return null;
        }

        final BigDecimal baseu = pbb.getDbBaseUnit();

        if (dbPeriodRateBefore == null || baseu == null) {
            return null;
        }

        return dbPeriodRateBefore.divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getStPeriodRateBeforeDesc() {
        final PeriodBaseView periodBaseBefore = getPeriodBaseBefore();

        if (periodBaseBefore == null) {
            return null;
        }

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

        if (stPremiumFactorID == null) {
            return "100%";
        }

        return getPremiumFactor().getStPremiumFactorDesc();
    }

    public BigDecimal getDbPremiumFactorValue() {
        if (stPremiumFactorID == null) {
            return BDUtil.one;
        }
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

        if (cs == null) {
            return false;
        }

        if (cs.isCoins() && cs.isLeader()) {
            coins = true;
        }

        if (cs.isCoins() && cs.isMember()) {
            coins = true;
        }

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

                if (RS.next()) {
                    stNomerator = RS.getString(1);
                }

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

        if (gcc == null) {
            return null;
        }

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

        if (handler == null) {
            return false;
        }

        return handler.isLockTSI();
    }

    public CustomHandler getHandler() {
        if (getPolicyType() == null) {
            return null;
        }
        return getPolicyType().getHandler();
    }

    public String findTSIPolTypeID(String stPolicyTypeID, String stInsuranceTSIID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select ins_tcpt_id from ins_tsicat_poltype where ins_tsi_cat_id=? and pol_type_id=?");

            PS.setString(1, stInsuranceTSIID);
            PS.setString(2, stPolicyTypeID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public String getStEntityPostalCode() {
        final EntityAddressView entityPrimaryAddress = getEntityPrimaryAddress();

        if (entityPrimaryAddress == null) {
            return null;
        }

        return entityPrimaryAddress.getStPostalCode();
    }

    public EntityAddressView getEntityPrimaryAddress() {
        getEntity();

        if (entity == null) {
            return null;
        }

        final EntityAddressView primaryAddress = entity.getPrimaryAddress();

        return primaryAddress;
    }

    public String getStSPPANoTrace() throws Exception {
        final InsuranceSplitPolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

        if (sppa == null) {
            return null;
        }

        return sppa.getStSPPANo();
    }

    private InsuranceSplitPolicyView getLink(String status) throws Exception {

        final String sessionCacheKey = "PO_LINK_" + stRootID + "/" + status;

        final ThreadContext trd = ThreadContext.getInstance();
        Object o = trd.get(sessionCacheKey);

        if (o == null) {
            final DTOList l = ListUtil.getDTOListFromQuery(
                    "select * from ins_policy where root_id = ? and status = ? order by pol_id desc limit 1",
                    new Object[]{stRootID, status},
                    InsuranceSplitPolicyView.class);

            if (l.size() > 0) {
                o = l.get(0);

                trd.put(sessionCacheKey, o);
            } else {
                o = Void.class;
            }
        }

        if (o.equals(Void.class)) {
            return null;
        }

        return (InsuranceSplitPolicyView) o;
    }

    public Date getDtSPPADateTrace() throws Exception {

        final InsuranceSplitPolicyView sppa = getLink(FinCodec.PolicyStatus.SPPA);

        if (sppa == null) {
            return null;
        }

        return sppa.getDtCreateDate();
    }

    public String getStProposalNo() throws Exception {
        final InsuranceSplitPolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

        if (draft == null) {
            return null;
        }

        return draft.getStPolicyNo();

    }

    public Date getStProposalDate() throws Exception {

        final InsuranceSplitPolicyView draft = getLink(FinCodec.PolicyStatus.DRAFT);

        if (draft == null) {
            return null;
        }

        return draft.getDtCreateDate();
    }

    public Object getStObjectDescription(int i) {

        loadObjects();

        if (objects.size() <= i) {
            return null;
        }

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

        return o.getStObjectDescription();
    }

    public BigDecimal getDbObjectTSIAmount(int i) {

        loadObjects();

        if (objects.size() <= i) {
            return null;
        }

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(i);

        return o.getDbObjectInsuredAmount();
    }

    public BigDecimal getDbObjectPremiRate(int n, int i) throws Exception {

        loadObjects();

        if (objects.size() <= i) {
            return null;
        }

        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) objects.get(n);

        final InsurancePolicyCoverView cover = o.getCover(i);

        if (cover == null) {
            return null;
        }

        return cover.getDbRate();
    }

    public BigDecimal getDbComission(int i) {
        loadDetails();

        for (int j = 0; j < details.size(); j++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(j);

            if (item.getInsItem().isComission()) {
                i--;

                if (i < 0) {
                    return item.getDbAmount();
                }
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

            tax2 = item.getDbTaxAmount();
        }

        return tax2;
    }

    public BigDecimal getDbTaxAmount3() throws Exception {
        getDetails();

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select sum(tax_amount) "
                    + "from ins_pol_items "
                    + "where pol_id=? and item_class='PRM' and tax_code is not null");

            PS.setString(1, stPolicyID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public BigDecimal getDbTaxAmount4(String PolicyID) throws Exception {
        getDetails();

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select coalesce(sum(b.tax_amount*a.ccy_rate),0) "
                    + "from ins_policy a "
                    + "inner join ins_pol_items b on b.pol_id = a.pol_id "
                    + "where b.pol_id=? and b.item_class='PRM' and b.tax_code is not null");

            PS.setString(1, PolicyID);


            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

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

        if (stClaimObjectID == null) {
            return null;
        }

        if (claimObject != null && !Tools.isEqual(stClaimObjectID, claimObject.getStPolicyObjectID())) {
            setClaimObject(null);
        }

        if (claimObject != null) {
            return claimObject;
        }

        getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            if (Tools.isEqual(obj.getStPolicyObjectID(), stClaimObjectID)) {
                setClaimObject(obj);
                return obj;
            }
        }

        return null;
    }

    public void validate(boolean isApproving) throws Exception {

        if (isApproving) {
            if (isStatusClaim() || isStatusClaimEndorse()) {
                if (isStatusClaimPLA()) {
                    if (getStPLANo() == null) {
                        throw new RuntimeException("No PLA/LKS Belum Diisi");
                    }

                    setStClaimStatus(FinCodec.ClaimStatus.PLA);
                    setStActiveFlag("Y");
                } else if (isStatusClaimDLA()) {
                    if (getStDLANo() == null) {
                        throw new RuntimeException("No DLA/LKP Belum Diisi");
                    }

                    setStEffectiveClaimFlag("Y");
                    setStEffectiveFlag("Y");
                    setStApprovedClaimFlag("Y");
                    setDtApprovedClaimDate(new Date());
                }
            }

            if (isStatusPolicy() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()) {
                validateTreaty(true);
            }
        }

        if (isStatusClaim() || isStatusClaimEndorse()) {
            if (isStatusClaimPLA()) {
                if (getStClaimObjectID() == null) {
                    throw new RuntimeException("Objek Klaim Belum Dipilih");
                }
            }
        }

        if (isModified()) {
            if (isStatusClaimDLA() || isStatusClaimEndorse()) {
                if (isStatusClaimDLA() || isStatusClaimEndorse()) {
                    if (dtDLADate != null) {
                        boolean withinCurrentMonth = DateUtil.getDateStr(getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                        if (!withinCurrentMonth) {
                            throw new RuntimeException("Tanggal LKP Tidak Valid (Sudah Tutup Produksi), Ubah Tanggal LKP menjadi Tanggal Sekarang (Bulan Produksi)");
                        }
                    }
                }
            } else if (isStatusHistory()) {
            } else {
                if (dtPolicyDate != null) {
                    if (!isStatusClaim() && !isStatusClaimEndorse() && !isStatusSPPA()) {

                        final boolean isAdmin = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");

                        final boolean isUserSuperEdit = SessionManager.getInstance().getSession().hasResource("USER_POL_SUPER_EDIT");

                        boolean withinCurrentMonth = DateUtil.getDateStr(getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                        boolean canEntryBackDate = UserManager.getInstance().getUser().canEntryBackdate();

                        DateTime currentDateLastDay = new DateTime(getDtPolicyDate());
                        currentDateLastDay = currentDateLastDay.dayOfMonth().withMaximumValue();

                        //int batasMaxHari = 4;
                        BigDecimal maxDate = Parameter.readNum("UWRIT_BACKDATE_DATE");
                        int batasMaxHari = Integer.parseInt(String.valueOf(maxDate)) + 1;
                        DateTime maximumBackDate = new DateTime();
                        maximumBackDate = currentDateLastDay.plusDays(batasMaxHari);

                        DateTime currentDate = new DateTime(new Date());
                        DateTime policyDate = new DateTime(getDtPolicyDate());

                        if (isApproving) {
                            if (currentDate.isBefore(policyDate)) {
                                throw new RuntimeException("Tanggal Polis / Endorsemen Tidak Valid (Melebihi Tanggal Approval)");
                            }
                        }

                        if (canEntryBackDate) {
                            if (currentDate.isAfter(maximumBackDate)) {
                                throw new RuntimeException("Batas waktu "+ String.valueOf(maxDate) +" hari approval setelah akhir bulan sudah terlewati");
                            }
                        }

                        if (canEntryBackDate) {
                            if (currentDate.isBefore(maximumBackDate.plusDays(1)) && currentDate.isAfter(currentDateLastDay)) {
                                dtApprovedDate = currentDateLastDay.toDate();
                            }
                        }

                        if (!canEntryBackDate) {
                            if (!isUserSuperEdit) {
                                if (isAdmin) {
                                    if (!isEditReasOnlyMode()) {
                                        if (!withinCurrentMonth) {
                                            throw new RuntimeException("Tanggal Polis / Endorsemen Tidak Valid (Sudah Tutup Produksi)");
                                        }
                                    }
                                } else {
                                    if (!withinCurrentMonth) {
                                        dtPolicyDate = new Date();
                                        
                                    }
                                }
                            }
                        }
                    }


                }

                if (getDtPeriodStart() != null && getDtPeriodEnd() != null) {
                    cekPeriodeAwal();
                }
            }
        }

        if (isStatusClaim()) {
            if (isStatusClaimPLA()) {
                //if (getStPLANo() == null) throw new RuntimeException("NO LKS Belum Diisi");

                if (getDbClaimAmountEstimate() == null) {
                    throw new RuntimeException("Jumlah Klaim Perkiraan Belum Diisi");
                }

                setStClaimStatus(FinCodec.ClaimStatus.PLA);
                setStActiveFlag("Y");

                int tgl = Tools.compare(DateUtil.truncDate(getDtPeriodStart()), DateUtil.truncDate(getDtPeriodEnd()));

                String desc = "";
                boolean validPeriod = true;
                if (tgl == 1) {
                    validPeriod = false;
                }
                //if(tgl == 0) desc = "Periode Awal / Periode Akhir Tidak Boleh Sama";
                if (tgl == 1) {
                    desc = "Periode Akhir Tidak Boleh < Periode Awal";
                }
                if (!validPeriod) {
                    throw new RuntimeException(desc);
                }

                if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                    if (getStClaimCauseID().equalsIgnoreCase("3733")) {
                        if (getStClaimNumberID() == null) {
                            throw new RuntimeException("Nama Bengkel Harus Diisi");
                        }
                    }
                }

            } else if (isStatusClaimDLA()) {
                if (getStDLANo() == null) {
                    throw new RuntimeException("NO LKP Belum Diisi");
                }

                if (getDbClaimAmountApproved() == null) {
                    throw new RuntimeException("Jumlah Klaim Disetujui Belum Diisi");
                }

                if (getDtDLADate() == null) {
                    throw new RuntimeException("Tanggal LKP Belum Diisi");
                }

                if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                    if (getStClaimCauseID().equalsIgnoreCase("3733")) {
                        if (getStClaimNumberID() == null) {
                            throw new RuntimeException("Nama Bengkel Harus Diisi");
                        }
                    }
                }
            }
        }

        if (isStatusHistory()) {
            if (getStPolicyNo().length() != 18) {
                throw new RuntimeException("Format No Polis History Harus 18 Digit, Isi 2 Digit Terakhir Dengan No Terakhir Pada Sistem Lama");
            }

            if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                if (getStCoinsID() == null) {
                    throw new RuntimeException("Member koasuransi harus diisi");
                }
            }
        }

        if (isStatusPolicy()) {
            if (getStPolicyNo() != null) {
                if (getStPolicyNo().length() != 18) {
                    throw new RuntimeException("Format No Polis Harus 18 Digit");
                }
            }
        }

        if (isCoinsurance()) {
            if (!"21".equalsIgnoreCase(getStPolicyTypeID())) {
                if (getCoins2().size() == 1) {
                    throw new RuntimeException("Polis Co-leader harus di isi member koasuransi");
                }
            }
        }

        if (getPeriodLengthYears() > 50) {
            throw new RuntimeException("Cek Periode Awal dan Periode Akhir apakah sudah betul");
        }

    }

    private boolean checkExclusionTSI(InsurancePolicyObjectView obj) throws Exception{

        boolean isExcludedTSI = false;

        DTOList tsidetail = obj.getSuminsureds();

        for (int m = 0; m < tsidetail.size(); m++) {
            InsurancePolicyTSIView tsiPolicy = (InsurancePolicyTSIView) tsidetail.get(m);

            InsuranceTSIView tsi = tsiPolicy.getInsuranceTSI();

            if (tsi.getStExcTSIFlag() != null) {
                if (tsi.getStExcTSIFlag().equalsIgnoreCase("Y")) {
                    isExcludedTSI = true;
                }
            }
        }

        return isExcludedTSI;
    }

    public void validateTreaty(boolean raiseErrors)  throws Exception {

        if (stRIFinishFlag != null) {
            if (Tools.isYes(stRIFinishFlag)) {
                if (stReinsuranceApprovedWho != null) {
                    return;
                }
            }
        }

        stRIFinishFlag = null;

        boolean validasiReasuransi = false;

        String message = "";

        if (isStatusEndorse()) {
            if (getPolicyType().isRIBlockEndorse()) {
                if (raiseErrors) {
                    if (stReinsuranceApprovedWho == null) {
                        throw new RuntimeException("Endorsemen ini harus melalui persetujuan bagian reasuransi kantor pusat terlebih dahulu");
                    }
                } else {
                    return;
                }
            }
        }

        if (!(isStatusEndorse() || isStatusPolicy() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward())) {
            return;
        }

        DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objDefault = (InsurancePolicyObjDefaultView) objects.get(i);

            DTOList treatyDetails = obj.getTreatyDetails();

            BigDecimal premiRITotal = null;

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                boolean equalTSI = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                BigDecimal cekSelisih = BDUtil.sub(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                if (BDUtil.lesserThanEqual(cekSelisih, BDUtil.one) && BDUtil.biggerThanEqual(cekSelisih, new BigDecimal(-1))) {
                    equalTSI = true;
                }

                boolean equalPremi = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyPremiTotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbPremiAmount(), BDUtil.one, scale));

                InsuranceTreatyDetailView tredet = trd.getTreatyDetail();

                if (!tredet.getTreatyType().isNonXOL()) {
                    premiRITotal = BDUtil.add(premiRITotal, trd.getDbMemberTreatyPremiTotal());
                }

                if (!tredet.isByPassValidationRI()) {

                    if (!equalTSI) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi TSI Reas " + tredet.getTreatyType().getStInsuranceTreatyTypeName() + " Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                        } else {
                            return;
                        }
                    }

                    if (!equalPremi) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi Premi Reas " + tredet.getTreatyType().getStInsuranceTreatyTypeName() + " Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                        } else {
                            return;
                        }
                    }
                }

                if (isStatusEndorse()) {
                    if (getStPolicyTypeGroupID().equalsIgnoreCase("1")) {
                        if (tredet.isFacultative()) {
                            validasiReasuransi = true;
                            message = " - Ada Treaty Fakultatif";
                        }


                        if (isBatalTotalEndorseMode()) {
                            validasiReasuransi = true;
                            message = " - Batal Total";
                        }

                    }
                }

                if (!isStatusEndorse()) {
                    if (getStPolicyTypeID().equalsIgnoreCase("59")) {

                        DateTime birthDate = new DateTime(objDefault.getDtReference1());
                        DateTime startDate = new DateTime(objDefault.getDtReference2());

                        Years y = Years.yearsBetween(birthDate, startDate);
                        int year = y.getYears();

                        int usiaPlusLamaKredit = year + Integer.parseInt(objDefault.getStReference5());

                        if (usiaPlusLamaKredit > 75) {
                            validasiReasuransi = true;
                            message = " - (Usia + Lama) > 75 Tahun";
                        }
                    }

                    Date periodeAwal = getDtPeriodStart();
                    if (getStPolicyTypeID().equalsIgnoreCase("59")) {
                        periodeAwal = objDefault.getDtReference2();
                    }

                    final int jumlahHari = DateUtil.getDaysAmount(periodeAwal, getDtPolicyDate());

                    if (!getStPolicyTypeID().equalsIgnoreCase("2")) {
                        if (jumlahHari > 180) {
                            validasiReasuransi = true;
                            message = " - Tanggal Awal ke Tanggal Polis  > 180 Hari";
                        }
                    }

                    if (Integer.parseInt(DateUtil.getYear(getDtPeriodStart())) > Integer.parseInt(DateUtil.getYear(new Date()))) {
                        validasiReasuransi = true;
                        message = " - Tahun Tanggal Awal > Tahun Tanggal Polis";
                    }

                }


                DTOList shares = trd.getShares();

                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                    if (!ri.isApproved()) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi Reas Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Belum Disetujui");
                        } else {
                            return;
                        }
                    }

                    if (ri.getStApprovedFlag() == null) {
                        stRIFinishFlag = null;
                    }

                }
            }

        }

        if (validasiReasuransi) {
            if (raiseErrors) {
                if (stReinsuranceApprovedWho == null) {
                    throw new RuntimeException("Polis/Endorsemen ini harus melalui persetujuan reasuransi kantor pusat" + message);
                }
            } else {
                return;
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

            if (obj.getStRiskClass() == null) {
                continue;
            }

            try {
                int rc = Integer.parseInt(obj.getStRiskClass());
                if (rc < 1 || rc > 3) {
                    throw new RuntimeException("Risk class should be 1 to 3");
                }
                if (rc > riskclass) {
                    riskclass = rc;
                }
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

            if (item.isComission()); else {
                continue;
            }

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
            final PreparedStatement PS = S.setQuery("select sum(preto) as tagihan, sum(bayar) as bayar "
                    + " from aba_bayar1 "
                    + " where pol_id = ? ");
            PS.setString(1, stParentID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                final BigDecimal tagihan = RS.getBigDecimal("tagihan");

                final BigDecimal bayar = RS.getBigDecimal("bayar");

                //if(BDUtil.isEqual(tagihan,bayar,2)||BDUtil.biggerThan(bayar,tagihan)) canClaim = true;
                if (bayar != null) {
                    canClaim = true;
                }
            }

            return canClaim;

        } finally {
            S.release();
        }
    }

    public boolean isPremiPaidInOldSystem() throws Exception {
        boolean canClaim = false;
        /*
        InsurancePolicyView parent = getParentPolicy();

        if(parent.getDtPaymentDate()!=null){
        canClaim = true;
        setDtPaymentDate(parent.getDtPaymentDate());
        setStPaymentNotes(parent.getStPaymentNotes());
        }*/


        if (getDtPaymentDate() != null) {
            canClaim = true;
            setDtPaymentDate(getDtPaymentDate());
            setStPaymentNotes(getStPaymentNotes());
        }

        //InsurancePolicyView parent = getParentPolicy();

        //final DTOList pembayaran = parent.getArinvoices();

        final DTOList pembayaran = getArinvoices();

        if (pembayaran.size() > 0) {
            ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

            if (inv.getDbAmountSettled() != null) {
                if (inv.getDtPaymentDate() != null) {
                    canClaim = true;
                    setDtPaymentDate(inv.getDtPaymentDate());
                    //setStPaymentNotes(inv.getstrec);
                }
            }
        }

        return canClaim;
    }

    public boolean canClaimAgain() throws Exception {
        final SQLUtil S = new SQLUtil();

        boolean canClaim = true;
        try {
            final PreparedStatement PS = S.setQuery("select claim_loss_id "
                    + "FROM ins_pol_obj "
                    + "WHERE ins_pol_obj_ref_id = ? limit 1");

            PS.setString(1, stClaimObjectID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                final String lossID = RS.getString("claim_loss_id");

                if (lossID != null) {
                    final String cekClaimRepeat = checkCanClaimRepeatedly(lossID);

                    if (cekClaimRepeat.equalsIgnoreCase("Y")) {
                        canClaim = true;
                    } else {
                        canClaim = false;
                    }
                } else {
                    canClaim = true;
                }
            }

            if (getClaimObject().getStVoidFlag() == null) {
                canClaim = true;
            }

            return canClaim;

        } finally {
            S.release();
        }

    }

    public void cekSudahInputLKS() throws Exception {

        InsurancePolicyObjDefaultView objectKlaim = (InsurancePolicyObjDefaultView) claimObject;

        if(objectKlaim.getStOrderNo()!=null){

                final SQLUtil S = new SQLUtil();

                try {



                    final PreparedStatement PS = S.setQuery("select a.pol_id,a.pla_no, a.dla_no ,b.claim_loss_id "+
                                                            "from ins_policy a "+
                                                            "inner join ins_poL_obj b on a.pol_id = b.pol_id "+
                                                            "where a.status in ('CLAIM','CLAIM ENDORSE') and active_flag = 'Y' "+
                                                            "and substr(pol_no, 0, 17) = ? and b.order_no = ? "+
                                                            "and a.pol_id <> ?"+
                                                            " order by a.pol_id desc limit 1");


                    PS.setString(1, getStPolicyNo().substring(0, 16));
                    PS.setString(2, objectKlaim.getStOrderNo());
                    //PS.setString(3, objectKlaim.getStReference1().toUpperCase().trim());
                    PS.setString(3, getStPolicyID());

                    final ResultSet RS = PS.executeQuery();

                    if (RS.next()) {

                        final String policyID = RS.getString("pol_id");
                        final String plaNo = RS.getString("pla_no");
                        final String dlaNo = RS.getString("dla_no");
                        final String lossID = RS.getString("claim_loss_id");

                        if (plaNo != null) {
                            final String cekClaimRepeat = checkCanClaimRepeatedly(lossID);

                            if (cekClaimRepeat.equalsIgnoreCase("N"))
                                throw new RuntimeException("Sudah pernah input klaim dengan objek yang sama pada "+ plaNo);
                        }
                    }

                } finally {
                    S.release();
                }
         }else if(objectKlaim.getStOrderNo()==null){

                final SQLUtil S = new SQLUtil();

                try {



                    final PreparedStatement PS = S.setQuery("select a.pol_id,a.pla_no, a.dla_no ,b.claim_loss_id "+
                                                            "from ins_policy a "+
                                                            "inner join ins_poL_obj b on a.pol_id = b.pol_id "+
                                                            "where a.status in ('CLAIM','CLAIM ENDORSE') and active_flag = 'Y' "+
                                                            "and substr(pol_no, 0, 17) = ? and btrim(upper(b.ref1)) = ?  "+
                                                            "and a.pol_id <> ?"+
                                                            " order by a.pol_id desc limit 1");



                    PS.setString(1, getStPolicyNo().substring(0, 16));
                    PS.setString(2, objectKlaim.getStReference1().toUpperCase().trim());
                    PS.setString(3, getStPolicyID());

                    final ResultSet RS = PS.executeQuery();

                    if (RS.next()) {

                        final String policyID = RS.getString("pol_id");
                        final String plaNo = RS.getString("pla_no");
                        final String dlaNo = RS.getString("dla_no");
                        final String lossID = RS.getString("claim_loss_id");

                        if (plaNo != null) {
                            final String cekClaimRepeat = checkCanClaimRepeatedly(lossID);

                            if (cekClaimRepeat.equalsIgnoreCase("N"))
                                throw new RuntimeException("Sudah pernah input klaim dengan objek yang sama pada "+ plaNo);
                        }
                    }

                } finally {
                    S.release();
                }
         }

        

    }

    public String checkCanClaimRepeatedly(String lossID) throws Exception {
        final SQLUtil S = new SQLUtil();

        String cekRepeat = "N";
        try {
            final PreparedStatement PS = S.setQuery("select repeated_claim_flag "
                    + "FROM ins_claim_loss "
                    + "WHERE claim_loss_id = ?  and pol_type_id = ?");

            PS.setString(1, lossID);
            PS.setString(2, stPolicyTypeID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) //cekClaim = true;
            {
                String repeat_flag = RS.getString("repeated_claim_flag");

                if (repeat_flag != null) {
                    if (repeat_flag.equalsIgnoreCase("Y")) {
                        cekRepeat = "Y";
                    }
                }

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
            if (coverage == null) {
                coverage = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_cover where pol_id = ? order by ins_pol_cover_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoverView.class);
            }
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

            if (polisCover.getStZoneID() == null) {
                continue;
            }

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
                    "   select "
                    + "      limit1 "
                    + "   from "
                    + "         ins_zone_limit b "
                    + "   where"
                    + "      b.zone_id=?");

            S.setParam(1, zoneid);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public String getInsuranceTreatyID(Date per_start) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      ins_treaty_id,treaty_name "
                    + "   from "
                    + "         ins_treaty"
                    + "   where"
                    + "      treaty_period_start <= ? "
                    + "   and treaty_period_end >= ? "
                    + "   and treaty_class = 'RE' ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public String getRiskCategoryID(String stPolicyTypeId, Date per_start) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      ins_risk_cat_id "
                    + "   from "
                    + "         ins_risk_cat"
                    + "   where"
                    + "      period_start <= ? "
                    + "   and period_end >= ? "
                    + "   and poltype_id = ?  ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, stPolicyTypeId);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
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
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where policy_id = ? and coins_type = 'COINS'"
                        + "   order by entity_id, ins_pol_coins_id",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCoins2() {
        try {
            if (coins == null) {
                coins = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where"
                        + "      policy_id = ? and entity_id <> 1",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class);
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
        if (getEntity().getStGLCode().equalsIgnoreCase("00000")) {
            glApplicator.setDesc("Y", "");
        } else {
            glApplicator.setDesc("Y", getStEntityName());
        }

        glApplicator.setCode('B', getStCostCenterCode());

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            InsuranceItemsView insItem = item.getInsItem();

            if (insItem == null) {
                throw new RuntimeException("Ins item not found : " + item);
            }

            ARTransactionLineView arTrxLine = insItem.getARTrxLine();

            if (arTrxLine == null) {
                throw new RuntimeException("AR TRX Line not found : " + item);
            }

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
                if (item.getEntity().getStGLCode() == null) {
                    throw new RuntimeException("GL Code " + item.getEntity().getStEntityName() + " Tidak Ada");
                }

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
        if (getStPrintCode() != null) {
            print = "Y";
        }

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

    public DTOList getObjectsClaim() {
        loadObjectsClaim();
        return objectsClaim;
    }

    public void loadObjectsClaim() {
        try {
            getClObjectClass();
            if (objectsClaim == null) {
                objectsClaim = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where ins_pol_obj_id  = ? ",
                        new Object[]{stClaimObjectID},
                        clObjectClass);

                for (int i = 0; i < objectsClaim.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objectsClaim.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            if (cause == null) {
                cause = ListUtil.getDTOListFromQuery(
                        "select b.cause_desc "
                        + "from ins_policy a "
                        + "inner join ins_clm_cause b on b.ins_clm_caus_id = a.claim_cause "
                        + "where a.pol_id = ?",
                        new Object[]{stPolicyID},
                        InsuranceClaimCauseView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateEndorsePAKreasi() throws Exception {
        if (!stPolicyTypeID.equalsIgnoreCase("21") && !stPolicyTypeID.equalsIgnoreCase("59")) {
            return;
        }

        if (isStatusEndorse() || isStatusEndorseRI() || isStatusEndorseIntern()) {
            BigDecimal totPremiRIEndorse = null;
            BigDecimal premiTotal = null;
            BigDecimal totCommissionRIEndorse = null;
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) objects.get(i);

                DTOList cover = obj.getCoverage();
                for (int k = 0; k < cover.size(); k++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(k);

                    premiTotal = BDUtil.add(premiTotal, cov.getDbPremi());

                }

                if (objx.getDtReference5() != null) {
                    continue;
                }

                if (objx.getStPolicyObjectRefID() == null) {
                    continue;
                }

                BigDecimal dbPremiRIEndorse = BDUtil.sub(objx.getDbReference2(), getParentObject(objx.getStPolicyObjectRefID()).getDbReference2());
                totPremiRIEndorse = BDUtil.add(totPremiRIEndorse, dbPremiRIEndorse);

                BigDecimal dbCommissionRIEndorse = BDUtil.sub(objx.getDbReference9(), getParentObject(objx.getStPolicyObjectRefID()).getDbReference9());
                totCommissionRIEndorse = BDUtil.add(totCommissionRIEndorse, dbCommissionRIEndorse);

            }

            dbTotalFee = null;
            BigDecimal totalComission = null;
            BigDecimal totalDiscount = null;
            BigDecimal factor = null;
            BigDecimal totalTax = null;
            dbPremiTotal = premiTotal;

            //start here
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                if (!item.isPremi()) {
                    continue;
                }

                item.calculateRateAmount(getDbInsuredAmount(), scale);

                dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
            }

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                if (!item.isDiscount()) {
                    continue;
                }

                item.calculateRateAmount(dbPremiTotal, scale);

                totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
            }
            final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

            dbPremiTotalAfterDisc = totalAfterDiscount;

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                if (item.isFee()); else {
                    continue;
                }

                if (item.isPPN() || item.isPPNFeeBase()) {
                    continue;
                }

                item.calculateRateAmount(totalAfterDiscount, scale);

                dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
            }

            dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                if (item.isComission()); else {
                    continue;
                }

                BigDecimal afterDiscount = totalAfterDiscount;

                if (item.isBrokerFeeIncludePPN()) {
                    afterDiscount = BDUtil.mul(BDUtil.div(new BigDecimal(100), new BigDecimal(110), 15), afterDiscount, scale);
                }

                item.calculateRateAmount(afterDiscount, scale);

                //item.calculateRateAmount(totalAfterDiscount, scale);

                totalComission = BDUtil.add(totalComission, item.getDbAmount());

                if (item.isAutoTaxRate()) {
                    if (item.getStTaxCode() != null) {
                        item.setDbTaxRate(item.getTax().getDbRate());
                    }
                }

                if (item.getStTaxCode() != null) {
                    totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
                }

            }

            //set data polis komisi, bfee, hfee, biaya polis, materai, diskon
            recalculateFee();

            //calculatePPH21Progressive2();

            calculatePPH21And23();

            //tambahin pengecekan yg punya NPWP dan Tidak Punya NPWP
        /* jika ada NPWP = 2%
             * jika tidak ada NPWP = 4%
             *
             */
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                if (item.isComission()); else {
                    continue;
                }

                if (item.isAutoTaxAmount()) {
                    item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(), scale));
                }

                if (item.isAutoTaxAmount()) {
                    if (item.getStTaxCode() != null) {
                        if (item.getTax().isPPH23BrokerInclude()) {
                            item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), dbNDBrokerageAfterPPN, scale));
                        }
                    }
                }


            }

            //calculatePPH21Progressive();

            dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);

            if (!BDUtil.isZeroOrNull(dbNDPPN)) {
                dbPremiNetto = BDUtil.sub(dbPremiNetto, dbNDPPN);
            }
            //end here
            //dbTotalDue = premiTotal;

            //setDbPremiTotal(premiTotal);

            //setDbPremiTotalAfterDisc(premiTotal);

            //setDbPremiNetto(premiTotal);


            //final BigDecimal BD100 = new BigDecimal(100);

            if (stPolicyTypeID.equalsIgnoreCase("59")) {
                BigDecimal checkInsAmount = null;
                BigDecimal checkPremiAmount = null;

                InsurancePolicyCoinsView holdingcoin = getHoldingCoin();

                BigDecimal totPct = null;
                BigDecimal totTSI = null;
                BigDecimal premiAfterDisc = null;


                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                    if (coin.isHoldingCompany()) {
                        continue;
                    }

                    if (coin.isEntryByPctRate()) {
                        coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                        coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                    }

                    if (!coin.isEntryByPctRate() && !coin.isAutoPremi() && !coin.isManualPremi()) {
                        coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5));
                        coin.setDbPremiAmount(BDUtil.mul(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5), dbPremiTotal, scale));
                    }

                    if (!coin.isEntryByPctRate() && coin.isAutoPremi()) {
                        coin.setDbPremiAmount(coin.getDbPremiAmount());
                    }

                    if (coin.isManualPremi()) {
                        coin.setDbPremiAmount(coin.getDbPremiAmount());
                    }

                    if (!BDUtil.isZeroOrNull(coin.getDbDiscountRate())) {
                        coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                    }
                    premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                    //if(!"21".equalsIgnoreCase(getStPolicyTypeID())) coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                    if (!BDUtil.isZeroOrNull(coin.getDbCommissionRate())) {
                        coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                    }
                    if (!BDUtil.isZeroOrNull(coin.getDbBrokerageRate())) {
                        coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                    }
                    if (!BDUtil.isZeroOrNull(coin.getDbHandlingFeeRate())) {
                        coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));
                    }

                    totPct = BDUtil.add(totPct, coin.getDbSharePct());
                    totTSI = BDUtil.add(totTSI, coin.getDbAmount());

                    checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
                    checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());

                }
                if (holdingcoin != null) {
                    if (holdingcoin.isManualPremi()) {
                        holdingcoin.setDbPremiAmount(holdingcoin.getDbPremiAmount());
                    }

                    if (!holdingcoin.isManualPremi()) {
                        holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
                        holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
                        if (holdingcoin.isAutoPremi()) {
                            holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoin.getDbSharePct()), scale));
                        }
                    }

                    if (BDUtil.biggerThan(BDUtil.add(holdingcoin.getDbPremiAmount(), checkPremiAmount), dbPremiTotal)) {
                        holdingcoin.setDbPremiAmount(BDUtil.sub(dbPremiTotal, checkPremiAmount));
                    }

                    holdingcoin.setDbDiscountAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbDiscountRate()), scale));

                    holdingcoin.setDbCommissionAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbCommissionRate()), scale));
                    checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
                }

                dbCoinsCheckInsAmount = checkInsAmount;
            }

            reCalculateInstallment();
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

        if (gcc == null) {
            return null;
        }

        return gcc.getStAddress();
    }

    public boolean isCaptivePolicy() throws Exception {
        final EntityView entity = getEntity();

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        String custCategory = entity.getStCategory1();

        final String businessSource = getDigit(custCategory, 1);

        //logger.logDebug("businessSource= " + businessSource);
        if ("1".equalsIgnoreCase(businessSource)) {
            return false;
        } else if ("2".equalsIgnoreCase(businessSource)) {
            return true;
        } else if ("3".equalsIgnoreCase(businessSource)) {
            return true;
        } else if ("4".equalsIgnoreCase(businessSource)) {
            return true;
        } else {
            throw new RuntimeException("Format No Polis Salah");
        }

    }

    public void validateTaxCode() throws Exception {
        final DTOList details = getDetails();

        boolean usePajakIncludePPN = false;
        boolean usePPN = false;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(i);

            if (!isStatusClaim()) {
                if (items.getInsItem().getStItemCategory().equalsIgnoreCase("PCOST") || items.getInsItem().getStItemCategory().equalsIgnoreCase("SFEE")) {
                    if (items.getDbAmount() == null) {
                        throw new RuntimeException("Biaya Polis/Materai Harus Diisi / Dihapus Jika Tidak Ada");
                    }
                }
            }

            if (!isStatusClaim()) {
                if (getStPolicyTypeGroupID().equalsIgnoreCase("7")) {
                    if (getStDataSourceID() == null) {
                        if (getDbNDPCost() == null || getDbNDSFee() == null) {
                            throw new RuntimeException("Biaya Polis & Materai Harus Diisi");
                        }
                    }
                }
            }

            if (items.getInsItem().getStItemCategory().equalsIgnoreCase("PPN")) {
                usePPN = true;
            }

            if (!items.isComission() || items.getInsItem().isNotUseTax()) {
                continue;
            }

            if (items.getInsItem().getStItemCategory().equalsIgnoreCase("COMM")) {
                if (!items.getStTaxCode().equalsIgnoreCase("1")
                        && !items.getStTaxCode().equalsIgnoreCase("2")
                        && !items.getStTaxCode().equalsIgnoreCase("3")) {
                    throw new RuntimeException("Jenis Pajak Harus Pajak Komisi");
                }
            } else if (items.getInsItem().getStItemCategory().equalsIgnoreCase("BROKR")) {

                if (items.getStTaxCode().equalsIgnoreCase("6")) {
                    usePajakIncludePPN = true;
                }

                if (!items.getStTaxCode().equalsIgnoreCase("4")
                        && !items.getStTaxCode().equalsIgnoreCase("5")
                        && !items.getStTaxCode().equalsIgnoreCase("6")) {
                    throw new RuntimeException("Jenis Pajak Harus Pajak Brokerage");
                }
            } else if (items.getInsItem().getStItemCategory().equalsIgnoreCase("HFEE")) {
                if (!items.getStTaxCode().equalsIgnoreCase("7")
                        && !items.getStTaxCode().equalsIgnoreCase("8")
                        && !items.getStTaxCode().equalsIgnoreCase("9")) {
                    throw new RuntimeException("Jenis Pajak Harus Pajak Handling Fee");
                }
            } else if (items.getInsItem().getStItemCategory().equalsIgnoreCase("BENGKELFEE")) {
                if (!items.getStTaxCode().equalsIgnoreCase("10")
                        && !items.getStTaxCode().equalsIgnoreCase("12")) {
                    throw new RuntimeException("Jenis Pajak Harus Pajak Jasa Bengkel");
                }
            }

            if (items.getStEntityID() == null) {
                throw new RuntimeException("Penerima Komisi/Broker Fee/Handling Fee Belum Diisi");
            }

        }

        if (!isStatusClaim() && !isStatusClaimEndorse() && !isStatusEndorse()) {
            if (usePajakIncludePPN && usePPN) {
                throw new RuntimeException("Pajak broker tidak boleh include PPN jika sudah menginput PPN terpisah");
            }
        }

        /*
        final DTOList claimdetails = getClaimItems();

        for (int j = 0; j < claimdetails.size(); j++) {
        InsurancePolicyItemsView itemsClaim = (InsurancePolicyItemsView) claimdetails.get(j);

        if(isStatusClaim())
        if(itemsClaim.getInsItem().getStItemType().equalsIgnoreCase("BENGKELFEE"))
        if(itemsClaim.getDbAmount() == null)
        throw new RuntimeException("Jasa Bengkel belum diisi");
        }*/
    }

    public void validateData() throws Exception {
        if (isStatusClaimPLA()) {
            if (!canClaimAgain()) {
                throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Objek Sama");
            }

            cekSudahInputLKS();  
        }

//        if (getStNextStatus() != null) {
//            if (getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM) || getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSECLAIM))
//                cekDoubleNoLKP();
//        }


        validateObjects(); 
        validateTaxCode();
    }

    public void validateExclusionRisk(boolean isApproving) throws Exception {

        boolean isExcludedTSI = false;

        final DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            //cek tsi excluded
            DTOList tsidetail = obj.getSuminsureds();

            for (int m = 0; m < tsidetail.size(); m++) {
                InsurancePolicyTSIView tsiPolicy = (InsurancePolicyTSIView) tsidetail.get(m);

                InsuranceTSIView tsi = tsiPolicy.getInsuranceTSI();

                if (tsi.getStExcTSIFlag() != null) {
                    if (tsi.getStExcTSIFlag().equalsIgnoreCase("Y")) {
                        isExcludedTSI = true;
                    }
                }
            }
            //end tsi

            if (obj.getStRiskCategoryID() != null) {
                riskcat = obj.getRiskCategory();
            }

            final DTOList treaty = obj.getTreaties();

            for (int j = 0; j < treaty.size(); j++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(j);

                final DTOList treatyDetails = tre.getDetails();
                for (int k = 0; k < treatyDetails.size(); k++) {
                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                    final DTOList shares = tredet.getShares();
                    for (int l = 0; l < shares.size(); l++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(l);

                        if (riskcat.getStExcRiskFlag() != null) {
                            if (riskcat.getStExcRiskFlag().equalsIgnoreCase("Y")) {
                                if (isApproving) {
                                    throw new RuntimeException("Kode Resiko Dikecualikan");
                                } else {
                                    ri.setStApprovedFlag(null);
                                }
                            }
                        }

                        if (isExcludedTSI) {
                            if (isApproving) {
                                throw new RuntimeException("Kode TSI Dikecualikan");
                            } else {
                                ri.setStApprovedFlag(null);
                            }
                        }


                        if (isManualReinsuranceFlag()) {
                            if (isApproving) {
                                throw new RuntimeException("Reas harus di spread manual");
                            } else {
                                ri.setStApprovedFlag(null);
                            }
                        }

                        if (riskcat.getStExcRiskFlag() == null && !isExcludedTSI && !isManualReinsuranceFlag()) {
                            if (!tredet.getTreatyDetail().isFacultative()) {
                                ri.setStApprovedFlag("Y");
                            }
                        }

                    }
                }
            }

            //end
        }
    }

    public String getStUserApprovedDesc() {

        final UserSessionView user = getUserApproved();

        if (user == null) {
            return null;
        }

        return user.getStUserName();
    }

    public String getStUserDivision() {

        final UserSessionView user = getUserApproved();

        if (user == null) {
            return null;
        }

        return user.getStDivision();
    }

    public UserSessionView getUserApproved() {

        final UserSessionView user = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stApprovedWho);

        return user;

    }

    public DTOList getAddress() {
        if (address == null) {
            loadAddress();
        }
        return address;
    }

    public void setAddress(DTOList address) {
        this.address = address;
    }

    public void loadAddress() {
        try {
            if (address == null) {
                address = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ent_address"
                        + "   where"
                        + "      ent_id = ?",
                        new Object[]{getStProducerID()},
                        EntityAddressView.class);
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
                    "  select vs_description "
                    + " from s_valueset "
                    + " where  "
                    + " vs_group = 'CLAIM_SIGN' and "
                    + " ref1 in(select branch "
                    + " from s_users "
                    + " where user_id = ?)");

            S.setParam(1, stApprovedWho);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

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
            if (bayar == null) {
                bayar = ListUtil.getDTOListFromQuery(
                        "select * from aba_bayar1 where pol_id = ?",
                        new Object[]{stParentID},
                        InsuranceBayar1View.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBayar(DTOList bayar) {
        this.bayar = bayar;
    }

    public void validateObjects() throws Exception {
        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    InsuranceSplitPolicyObjDefaultView objx = (InsuranceSplitPolicyObjDefaultView) obj;

                    if (!isStatusClaim()) {
                        DTOList details = objectMap.getDetails();

                        for (int p = 0; p < details.size(); p++) {
                            FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                            if (!isBypassValidation()) {
                                if (Tools.isYes(ffd.getStMandatoryFlag())) {
                                    if (obj.getProperty(ffd.getStFieldRef()) == null) {
                                        throw new RuntimeException(ffd.getStFieldDesc() + " Objek No " + (i + 1) + " Belum Diisi");
                                    }

                                }
                            }

                            if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                                if (objx.getStReference7() == null) {
                                    throw new RuntimeException("Kode Penggunaan Objek No " + (i + 1) + " Belum Diisi");
                                }

                                if (objx.getStReference7().equalsIgnoreCase("11") || objx.getStReference7().equalsIgnoreCase("12")) {
                                    if (objx.getStReference10() == null) {
                                        throw new RuntimeException("Deskripsi penggunaan wajib diisi untuk Komersial dan Kendaraan Khusus");
                                    }
                                }
                            }

                            /*
                            if(ffd.getStFieldDesc().toUpperCase().contains("KODE RISIKO")){
                            if(!objx.getStReference3().toUpperCase().contains(obj.getRiskCategory().getStInsuranceRiskCategoryCode()))
                            throw new RuntimeException("Kode resiko " + "Objek No " + (i + 1) + " tidak sama, cek lagi.");
                            }
                             */
                        }

                        //validasi jenis kredit
                        validateJenisKredit(obj);
                    }

                    

                    if (obj.getStRiskCategoryID() == null) {
                        throw new RuntimeException("Kode Resiko Objek No " + (i + 1) + " Belum Diisi");
                    }

                    if (obj.getSuminsureds().size() < 1) {
                        throw new RuntimeException("Harga Pertanggungan Objek No. " + (i + 1) + " Belum Diisi");
                    }

                    if (obj.getCoverage().size() < 1) {
                        throw new RuntimeException("Coverage Objek No. " + (i + 1) + " Belum Diisi");
                    }

                    if (isEnabledSubrogasi()) {
                        if (obj.getStSubrogasiID() == null) {
                            throw new RuntimeException("Pilihan Subrogasi Objek No. " + (i + 1) + " Belum Diisi Ya/Tidak");
                        } else {
                            if (obj.getStSubrogasiID().equalsIgnoreCase("1")) {
                                setStReference13("Y");
                            } else {
                                setStReference13(null);
                            }
                        }
                    }

                    if (!isBypassValidation()) {
                        if (isStatusDraft() || isStatusSPPA() || isStatusPolicy() || isStatusRenewal()) {
                            if (getStPolicyTypeGroupID().equalsIgnoreCase("1")) {
                                if (obj.getCoverage().size() > 0) {
                                    final DTOList coverage = obj.getCoverage();

                                    for (int j = 0; j < coverage.size(); j++) {
                                        InsuranceSplitPolicyCoverView cov = (InsuranceSplitPolicyCoverView) coverage.get(j);

                                        if (BDUtil.isZeroOrNull(cov.getDbPremiNew())) {
                                            throw new RuntimeException("Nilai Premi Coverage Objek No. " + (i + 1) + " Belum diisi");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void validateTSILimit(BigDecimal transactionLimit) throws Exception {

        final DTOList object = getObjects();

        for (int i = 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            InsurancePolicyObjDefaultView object2 = (InsurancePolicyObjDefaultView) obj;

            String riskLevel = "";

            if (object2.getRiskCategory().getStRiskLevel() != null) {
                riskLevel = object2.getRiskCategory().getStRiskLevel();
            }

            final BigDecimal transactionLimitPerRiskCode = getTransactionLimitPerRiskCode("ACCEPT", getStRiskClass(), SessionManager.getInstance().getUserID(), obj.getStRiskCategoryCode());

            final BigDecimal transactionLimitPerRiskLevel = getTransactionLimitPerRiskLevel("ACCEPT", getStRiskClass(), SessionManager.getInstance().getUserID(), riskLevel);

            boolean enoughLimit = false;

            BigDecimal tsiObject = BDUtil.mul(obj.getDbObjectInsuredAmount(), getDbCurrencyRate());

            if (!BDUtil.isZeroOrNull(object2.getDbLimitOfLiability2())) {
                tsiObject = BDUtil.mul(object2.getDbLimitOfLiability2(), getDbCurrencyRate());
            }

            if (!BDUtil.isZeroOrNull(transactionLimitPerRiskCode)) {
                enoughLimit = Tools.compare(transactionLimitPerRiskCode, tsiObject) >= 0;
            } else {
                enoughLimit = Tools.compare(transactionLimit, tsiObject) >= 0;
            }

            if (!enoughLimit) {
                stUnderwritingFinishFlag = "N";
                stEffectiveFlag = "N";
                setStRiskApproved("N");

                //getRemoteInsurance().saveInputPaymentDate(this, approvalMode);
                throw new RuntimeException("Nilai TSI Objek No (" + (i + 1) + ") " + object2.getStReference1() + "  Melebihi Limit Kewenangan Anda (" + tsiObject + " > " + transactionLimit + ")");

            }

            //validasi objek
            if (!isStatusClaim()) {
                    if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                        if (object2.getStReference7().equalsIgnoreCase("11") || object2.getStReference7().equalsIgnoreCase("12")) {
                            String cc_code = SessionManager.getInstance().getSession().getStBranch();

                            if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
                                throw new RuntimeException("Untuk penggunaan Komersial dan Kendaraan Khusus harus persetujuan Underwriting Kantor Pusat");
                            }
                        }
                  }
            }
        }

    }

    public void validateTSILimitByPejabat(BigDecimal transactionLimit) throws Exception {

        final DTOList object = getObjects();

        for (int i = 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            InsurancePolicyObjDefaultView object2 = (InsurancePolicyObjDefaultView) obj;

            boolean enoughLimit = false;

            BigDecimal tsiObject = BDUtil.mul(obj.getDbObjectInsuredAmount(), getDbCurrencyRate());

            if (!BDUtil.isZeroOrNull(object2.getDbLimitOfLiability2())) {
                tsiObject = BDUtil.mul(object2.getDbLimitOfLiability2(), getDbCurrencyRate());
            }

            enoughLimit = Tools.compare(transactionLimit, tsiObject) >= 0;

            if (!enoughLimit) {
                stUnderwritingFinishFlag = "N";
                stEffectiveFlag = "N";
                setStRiskApproved("N");

//                getRemoteInsurance().saveInputPaymentDate(this, approvalMode);
                throw new RuntimeException("Nilai TSI Objek No (" + (i + 1) + ") " + object2.getStReference1() + "  Melebihi Limit Kewenangan Anda (" + tsiObject + " > " + transactionLimit + ")");

            }

            //validasi objek
            if (!isStatusClaim()) {
                    if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                        if (object2.getStReference7().equalsIgnoreCase("11") || object2.getStReference7().equalsIgnoreCase("12")) {
                            String cc_code = SessionManager.getInstance().getSession().getStBranch();

                            if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
                                throw new RuntimeException("Untuk penggunaan Komersial dan Kendaraan Khusus harus persetujuan Underwriting Kantor Pusat");
                            }
                        }
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
            if (abakreasi == null) {
                abakreasi = ListUtil.getDTOListFromQuery(
                        "select * from aba_kreasi where pol_no = ? and cc_code = ? order by norut",
                        new Object[]{stPolicyNo, stCostCenterCode},
                        InsuranceKreasiView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setABAKreasi(DTOList abakreasi) {
        this.abakreasi = abakreasi;
    }

    public String getStUnits() {
        return stUnits;
    }

    public void setStUnits(String stUnits) {
        this.stUnits = stUnits;
    }

    public DTOList getObjectRestitusi() {
        loadObjectRestitusi();
        return objectRestitusi;
    }

    public void loadObjectRestitusi() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ?  and refd5 is not null order by ins_pol_obj_id ",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private DTOList parentpolicy;
    private DTOList parentpolicy2;

    public DTOList getParentPolicy2() {
        loadParentPolicy2();
        return parentpolicy2;
    }

    public void loadParentPolicy2() {
        try {
            if (parentpolicy2 == null) {
                parentpolicy2 = ListUtil.getDTOListFromQuery(
                        "select a.policy_date,a.pol_no as policy_id,c.ref_ent_id as entity_id, "
                        + "getpremiend(b.entity_id,a.insured_amount*a.ccy_rate,b.amount*a.ccy_rate*-1) as amount, "
                        + "getpremiend(b.entity_id,a.premi_total*a.ccy_rate,b.premi_amt*a.ccy_rate*-1) as premi_amt "
                        + "from ins_policy a "
                        + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                        + "left join ent_master c on c.ent_id = b.entity_id "
                        + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.ref2 = ? "
                        + "order by a.policy_date,a.pol_no,c.ref_ent_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadParentPolicy() {
        try {
            if (parentpolicy == null) {
                parentpolicy = ListUtil.getDTOListFromQuery(
                        "select a.policy_date,a.pol_no as policy_id,c.ref_ent_id as entity_id, "
                        + "getpremiend(b.entity_id,a.premi_total*a.ccy_rate,b.premi_amt*a.ccy_rate*-1) as premi_amt "
                        + "from ins_policy a "
                        + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                        + "left join ent_master c on c.ent_id = b.entity_id "
                        + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.ref2 = ? "
                        + "order by a.policy_date,a.pol_no,c.ref_ent_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParentPolicy2(DTOList parentpolicy) {
        this.parentpolicy = parentpolicy;
    }

    public BigDecimal getPPHRate(String taxCode, boolean hasNPWP, Date dtPeriodStart) throws Exception {
        final SQLUtil S = new SQLUtil();

        String rateSelect = "";

        if (hasNPWP) {
            rateSelect = "rate1_npwp";
        } else {
            rateSelect = "rate1";
        }

        try {
            S.setQuery(
                    "   select " + rateSelect
                    + "   from "
                    + "         ar_tax_rate "
                    + "   where"
                    + " tax_code = ? "
                    + " and period_start <= ? "
                    + "   and period_end >= ? ");

            S.setParam(1, taxCode);
            S.setParam(2, dtPeriodStart);
            S.setParam(3, dtPeriodStart);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public String getStDocumentPrintFlag() {
        return stDocumentPrintFlag;
    }

    public void setStDocumentPrintFlag(String stDocumentPrintFlag) {
        this.stDocumentPrintFlag = stDocumentPrintFlag;
    }
    private DTOList rootpolicy;

    public DTOList getRootPolicy() {
        loadRootPolicy();
        return rootpolicy;
    }

    public void loadRootPolicy() {
        try {
            if (rootpolicy == null) {
                rootpolicy = ListUtil.getDTOListFromQuery(
                        "select * "
                        + " from ins_policy "
                        + " where status = 'ENDORSE' and active_flag = 'Y' "
                        + " and root_id = ? ",
                        new Object[]{stRootID},
                        InsuranceSplitPolicyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkEndorseNoBefore(String stPolicyNo) throws Exception {
        final DTOList root = getRootPolicy();

        for (int i = 0; i < root.size(); i++) {
            InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) root.get(i);

            if (pol.getStPolicyNo().equalsIgnoreCase(stPolicyNo)) {
                throw new RuntimeException("No Endorse " + stPolicyNo + " Sudah Pernah Dibuat");
            }

        }
    }
    private DTOList historypolicy;

    public DTOList getHistoryPolicy() {
        loadHistoryPolicy();
        return historypolicy;
    }

    public void loadHistoryPolicy() {
        try {
            if (historypolicy == null) {
                historypolicy = ListUtil.getDTOListFromQuery(
                        "select substr(pol_no,0,17) as pol_no "
                        + " from ins_policy "
                        + " where status = 'HISTORY' and pol_no like ? limit 5",
                        new Object[]{stPolicyNo.length() > 16 ? stPolicyNo.substring(0, 16) + "%" : stPolicyNo + "%"},
                        InsuranceSplitPolicyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private DTOList oldpayment;

    public DTOList getOldPayment() {
        loadOldPayment();
        return oldpayment;
    }

    public void loadOldPayment() {
        try {
            if (oldpayment == null) {
                oldpayment = ListUtil.getDTOListFromQuery(
                        "select * "
                        + " from aba_lunas "
                        + " where substr(nopol,0,17) = ? limit 1",
                        new Object[]{stPolicyNo.substring(0, 16)},
                        InsuranceLunasView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkHistoryPolicyNo() throws Exception {
        final DTOList history = getHistoryPolicy();

        for (int i = 0; i < history.size(); i++) {
            InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) history.get(i);

            if (pol.getStPolicyNo().equalsIgnoreCase(getStPolicyNo().substring(0, 16))) {
                if (getStPolicyID() == null) {
                    throw new RuntimeException("Polis History " + getStPolicyNo().substring(0, 16) + " Sudah Pernah Dibuat");
                }
            }
        }
    }

    public void validateHistoryPolicy() throws Exception {

        final DTOList oldpayment = getOldPayment();
        if (oldpayment != null) {
            InsuranceLunasView lunas = (InsuranceLunasView) oldpayment.get(0);

            if (lunas == null) {
                throw new RuntimeException("Tanggal Pembayaran Tidak Ditemukan, Silakan Input Manual");
            }

            if (lunas.getDtTanggalBayar() == null) {
                throw new RuntimeException("Tanggal Pembayaran Tidak Ditemukan, Silakan Input Manual");
            }

            setDtPaymentDate(lunas.getDtTanggalBayar());
            setStPaymentNotes("No Bukti : " + lunas.getStInsuranceNoBukti());
        }

    }
    private Date dtPaymentDate;

    public Date getDtPaymentDate() {
        return dtPaymentDate;
    }

    public void setDtPaymentDate(Date dtPaymentDate) {
        this.dtPaymentDate = dtPaymentDate;
    }

    public String getPolicyNoOldFormat(String nopolis) throws Exception {
        String nopol = "";

        final String enos = nopolis.substring(16, 18);
        if (enos.equalsIgnoreCase("01")) {
            nopol = nopolis.substring(0, 16) + "A";
        } else if (enos.equalsIgnoreCase("02")) {
            nopol = nopolis.substring(0, 16) + "B";
        } else if (enos.equalsIgnoreCase("03")) {
            nopol = nopolis.substring(0, 16) + "C";
        } else if (enos.equalsIgnoreCase("04")) {
            nopol = nopolis.substring(0, 16) + "D";
        } else if (enos.equalsIgnoreCase("05")) {
            nopol = nopolis.substring(0, 16) + "E";
        } else if (enos.equalsIgnoreCase("06")) {
            nopol = nopolis.substring(0, 16) + "F";
        } else if (enos.equalsIgnoreCase("07")) {
            nopol = nopolis.substring(0, 16) + "G";
        } else if (enos.equalsIgnoreCase("08")) {
            nopol = nopolis.substring(0, 16) + "H";
        } else if (enos.equalsIgnoreCase("09")) {
            nopol = nopolis.substring(0, 16) + "I";
        } else if (enos.equalsIgnoreCase("10")) {
            nopol = nopolis.substring(0, 16) + "J";
        } else if (enos.equalsIgnoreCase("11")) {
            nopol = nopolis.substring(0, 16) + "K";
        } else if (enos.equalsIgnoreCase("12")) {
            nopol = nopolis.substring(0, 16) + "L";
        } else if (enos.equalsIgnoreCase("13")) {
            nopol = nopolis.substring(0, 16) + "M";
        } else if (enos.equalsIgnoreCase("14")) {
            nopol = nopolis.substring(0, 16) + "N";
        } else if (enos.equalsIgnoreCase("15")) {
            nopol = nopolis.substring(0, 16) + "O";
        } else if (enos.equalsIgnoreCase("16")) {
            nopol = nopolis.substring(0, 16) + "P";
        } else if (enos.equalsIgnoreCase("17")) {
            nopol = nopolis.substring(0, 16) + "Q";
        } else if (enos.equalsIgnoreCase("18")) {
            nopol = nopolis.substring(0, 16) + "R";
        } else if (enos.equalsIgnoreCase("19")) {
            nopol = nopolis.substring(0, 16) + "S";
        } else if (enos.equalsIgnoreCase("20")) {
            nopol = nopolis.substring(0, 16) + "T";
        } else if (enos.equalsIgnoreCase("21")) {
            nopol = nopolis.substring(0, 16) + "U";
        } else if (enos.equalsIgnoreCase("22")) {
            nopol = nopolis.substring(0, 16) + "V";
        } else if (enos.equalsIgnoreCase("23")) {
            nopol = nopolis.substring(0, 16) + "W";
        } else if (enos.equalsIgnoreCase("24")) {
            nopol = nopolis.substring(0, 16) + "X";
        } else if (enos.equalsIgnoreCase("25")) {
            nopol = nopolis.substring(0, 16) + "Y";
        } else if (enos.equalsIgnoreCase("26")) {
            nopol = nopolis.substring(0, 16) + "Z";
        } else if (enos.equalsIgnoreCase("27")) {
            nopol = nopolis.substring(0, 16) + "a";
        } else if (enos.equalsIgnoreCase("28")) {
            nopol = nopolis.substring(0, 16) + "b";
        } else if (enos.equalsIgnoreCase("29")) {
            nopol = nopolis.substring(0, 16) + "c";
        } else if (enos.equalsIgnoreCase("30")) {
            nopol = nopolis.substring(0, 16) + "d";
        } else if (enos.equalsIgnoreCase("31")) {
            nopol = nopolis.substring(0, 16) + "e";
        } else if (enos.equalsIgnoreCase("32")) {
            nopol = nopolis.substring(0, 16) + "f";
        } else if (enos.equalsIgnoreCase("33")) {
            nopol = nopolis.substring(0, 16) + "g";
        } else if (enos.equalsIgnoreCase("34")) {
            nopol = nopolis.substring(0, 16) + "h";
        } else if (enos.equalsIgnoreCase("35")) {
            nopol = nopolis.substring(0, 16) + "i";
        } else if (enos.equalsIgnoreCase("36")) {
            nopol = nopolis.substring(0, 16) + "j";
        } else if (enos.equalsIgnoreCase("37")) {
            nopol = nopolis.substring(0, 16) + "k";
        } else if (enos.equalsIgnoreCase("38")) {
            nopol = nopolis.substring(0, 16) + "l";
        } else if (enos.equalsIgnoreCase("39")) {
            nopol = nopolis.substring(0, 16) + "m";
        } else if (enos.equalsIgnoreCase("40")) {
            nopol = nopolis.substring(0, 16) + "n";
        } else if (enos.equalsIgnoreCase("41")) {
            nopol = nopolis.substring(0, 16) + "o";
        } else if (enos.equalsIgnoreCase("42")) {
            nopol = nopolis.substring(0, 16) + "p";
        } else if (enos.equalsIgnoreCase("43")) {
            nopol = nopolis.substring(0, 16) + "q";
        } else if (enos.equalsIgnoreCase("44")) {
            nopol = nopolis.substring(0, 16) + "r";
        } else if (enos.equalsIgnoreCase("45")) {
            nopol = nopolis.substring(0, 16) + "s";
        } else if (enos.equalsIgnoreCase("46")) {
            nopol = nopolis.substring(0, 16) + "t";
        } else if (enos.equalsIgnoreCase("47")) {
            nopol = nopolis.substring(0, 16) + "u";
        } else if (enos.equalsIgnoreCase("48")) {
            nopol = nopolis.substring(0, 16) + "v";
        } else if (enos.equalsIgnoreCase("49")) {
            nopol = nopolis.substring(0, 16) + "w";
        } else if (enos.equalsIgnoreCase("50")) {
            nopol = nopolis.substring(0, 16) + "x";
        } else if (enos.equalsIgnoreCase("51")) {
            nopol = nopolis.substring(0, 16) + "y";
        } else if (enos.equalsIgnoreCase("52")) {
            nopol = nopolis.substring(0, 16) + "z";
        } else {
            nopol = nopolis;
        }

        return nopol;
    }
    private String stPaymentNotes;

    public String getStPaymentNotes() {
        return stPaymentNotes;
    }

    public void setStPaymentNotes(String stPaymentNotes) {
        this.stPaymentNotes = stPaymentNotes;
    }

    public BigDecimal getDbClaimBalance() {
        return dbClaimBalance;
    }

    public void setDbClaimBalance(BigDecimal dbClaimBalance) {
        this.dbClaimBalance = dbClaimBalance;
    }

    public void setStClaimAccountNo(String stClaimAccountNo) {
        this.stClaimAccountNo = stClaimAccountNo;
    }

    public String getStClaimAccountNo() {
        return stClaimAccountNo;
    }

    public void setStClaimClientName(String stClaimClientName) {
        this.stClaimClientName = stClaimClientName;
    }

    public String getStClaimClientName() {
        return stClaimClientName;
    }

    public void setStClaimNumberID(String stClaimNumberID) {
        this.stClaimNumberID = stClaimNumberID;
    }

    public String getStClaimNumberID() {
        return stClaimNumberID;
    }

    public void setStClaimChronology(String stClaimChronology) {
        this.stClaimChronology = stClaimChronology;
    }

    public String getStClaimChronology() {
        return stClaimChronology;
    }

    public String convertOldPolicyNoToNewFormat(String nopolis) throws Exception {
        final String stLastEndorsemen = getStLastOldEndorsemenNo(nopolis);
        //1421214102050105A
        //1421214102050105z
        //142121410205010502
        //012345678901234567
        String nopol = nopolis.substring(0, 16) + "00";

        if (stLastEndorsemen != null) {
            final String enos = stLastEndorsemen.length() == 17 ? stLastEndorsemen.substring(16, 17) : null;

            if (enos != null) {
                if (enos.equals("A")) {
                    nopol = nopolis.substring(0, 16) + "01";
                } else if (enos.equals("B")) {
                    nopol = nopolis.substring(0, 16) + "02";
                } else if (enos.equals("C")) {
                    nopol = nopolis.substring(0, 16) + "03";
                } else if (enos.equals("D")) {
                    nopol = nopolis.substring(0, 16) + "04";
                } else if (enos.equals("E")) {
                    nopol = nopolis.substring(0, 16) + "05";
                } else if (enos.equals("F")) {
                    nopol = nopolis.substring(0, 16) + "06";
                } else if (enos.equals("G")) {
                    nopol = nopolis.substring(0, 16) + "07";
                } else if (enos.equals("H")) {
                    nopol = nopolis.substring(0, 16) + "08";
                } else if (enos.equals("I")) {
                    nopol = nopolis.substring(0, 16) + "09";
                } else if (enos.equals("J")) {
                    nopol = nopolis.substring(0, 16) + "10";
                } else if (enos.equals("K")) {
                    nopol = nopolis.substring(0, 16) + "11";
                } else if (enos.equals("L")) {
                    nopol = nopolis.substring(0, 16) + "12";
                } else if (enos.equals("M")) {
                    nopol = nopolis.substring(0, 16) + "13";
                } else if (enos.equals("N")) {
                    nopol = nopolis.substring(0, 16) + "14";
                } else if (enos.equals("O")) {
                    nopol = nopolis.substring(0, 16) + "15";
                } else if (enos.equals("P")) {
                    nopol = nopolis.substring(0, 16) + "16";
                } else if (enos.equals("Q")) {
                    nopol = nopolis.substring(0, 16) + "17";
                } else if (enos.equals("R")) {
                    nopol = nopolis.substring(0, 16) + "18";
                } else if (enos.equals("S")) {
                    nopol = nopolis.substring(0, 16) + "19";
                } else if (enos.equals("T")) {
                    nopol = nopolis.substring(0, 16) + "20";
                } else if (enos.equals("U")) {
                    nopol = nopolis.substring(0, 16) + "21";
                } else if (enos.equals("V")) {
                    nopol = nopolis.substring(0, 16) + "22";
                } else if (enos.equals("W")) {
                    nopol = nopolis.substring(0, 16) + "23";
                } else if (enos.equals("X")) {
                    nopol = nopolis.substring(0, 16) + "24";
                } else if (enos.equals("Y")) {
                    nopol = nopolis.substring(0, 16) + "25";
                } else if (enos.equals("Z")) {
                    nopol = nopolis.substring(0, 16) + "26";
                } else if (enos.equals("a")) {
                    nopol = nopolis.substring(0, 16) + "27";
                } else if (enos.equals("b")) {
                    nopol = nopolis.substring(0, 16) + "28";
                } else if (enos.equals("c")) {
                    nopol = nopolis.substring(0, 16) + "29";
                } else if (enos.equals("d")) {
                    nopol = nopolis.substring(0, 16) + "30";
                } else if (enos.equals("e")) {
                    nopol = nopolis.substring(0, 16) + "31";
                } else if (enos.equals("f")) {
                    nopol = nopolis.substring(0, 16) + "32";
                } else if (enos.equals("g")) {
                    nopol = nopolis.substring(0, 16) + "33";
                } else if (enos.equals("h")) {
                    nopol = nopolis.substring(0, 16) + "34";
                } else if (enos.equals("i")) {
                    nopol = nopolis.substring(0, 16) + "35";
                } else if (enos.equals("j")) {
                    nopol = nopolis.substring(0, 16) + "36";
                } else if (enos.equals("k")) {
                    nopol = nopolis.substring(0, 16) + "37";
                } else if (enos.equals("l")) {
                    nopol = nopolis.substring(0, 16) + "38";
                } else if (enos.equals("m")) {
                    nopol = nopolis.substring(0, 16) + "39";
                } else if (enos.equals("n")) {
                    nopol = nopolis.substring(0, 16) + "40";
                } else if (enos.equals("o")) {
                    nopol = nopolis.substring(0, 16) + "41";
                } else if (enos.equals("p")) {
                    nopol = nopolis.substring(0, 16) + "42";
                } else if (enos.equals("q")) {
                    nopol = nopolis.substring(0, 16) + "43";
                } else if (enos.equals("r")) {
                    nopol = nopolis.substring(0, 16) + "44";
                } else if (enos.equals("s")) {
                    nopol = nopolis.substring(0, 16) + "45";
                } else if (enos.equals("t")) {
                    nopol = nopolis.substring(0, 16) + "46";
                } else if (enos.equals("u")) {
                    nopol = nopolis.substring(0, 16) + "47";
                } else if (enos.equals("v")) {
                    nopol = nopolis.substring(0, 16) + "48";
                } else if (enos.equals("w")) {
                    nopol = nopolis.substring(0, 16) + "49";
                } else if (enos.equals("x")) {
                    nopol = nopolis.substring(0, 16) + "50";
                } else if (enos.equals("y")) {
                    nopol = nopolis.substring(0, 16) + "51";
                } else if (enos.equals("z")) {
                    nopol = nopolis.substring(0, 16) + "52";
                } else {
                    nopol = nopolis;
                }
            }
        } else {
            nopol = nopol;
        }


        return nopol;
    }

    public String getStLastOldEndorsemenNo(String nopol) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select pol_no "
                    + " from aba_kreasi "
                    + " where pol_no like ? "
                    + " order by pol_no desc "
                    + " limit 1");

            PS.setString(1, nopol + "%");

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    public DTOList getCoinsCoverage() {
        if (coinsCoverage == null) {
            loadCoinsCoverage();
        }
        return coinsCoverage;
    }

    public void loadCoinsCoverage() {
        try {
            if (coinsCoverage == null) {
                coinsCoverage = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where"
                        + " policy_id = ?  and coins_type = 'COINS_COVER' order by entity_id, ins_pol_coins_id ",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getPolicyNoOldFormat2(String nopolis) throws Exception {
        String nopol = "";

        final String enos = nopolis.substring(16, 18);
        if (enos.equalsIgnoreCase("01")) {
            nopol = nopolis.substring(0, 16) + "A";
        } else if (enos.equalsIgnoreCase("02")) {
            nopol = nopolis.substring(0, 16) + "B";
        } else if (enos.equalsIgnoreCase("03")) {
            nopol = nopolis.substring(0, 16) + "C";
        } else if (enos.equalsIgnoreCase("04")) {
            nopol = nopolis.substring(0, 16) + "D";
        } else if (enos.equalsIgnoreCase("05")) {
            nopol = nopolis.substring(0, 16) + "E";
        } else if (enos.equalsIgnoreCase("06")) {
            nopol = nopolis.substring(0, 16) + "F";
        } else if (enos.equalsIgnoreCase("07")) {
            nopol = nopolis.substring(0, 16) + "G";
        } else if (enos.equalsIgnoreCase("08")) {
            nopol = nopolis.substring(0, 16) + "H";
        } else if (enos.equalsIgnoreCase("09")) {
            nopol = nopolis.substring(0, 16) + "I";
        } else if (enos.equalsIgnoreCase("10")) {
            nopol = nopolis.substring(0, 16) + "J";
        } else if (enos.equalsIgnoreCase("11")) {
            nopol = nopolis.substring(0, 16) + "K";
        } else if (enos.equalsIgnoreCase("12")) {
            nopol = nopolis.substring(0, 16) + "L";
        } else if (enos.equalsIgnoreCase("13")) {
            nopol = nopolis.substring(0, 16) + "M";
        } else if (enos.equalsIgnoreCase("14")) {
            nopol = nopolis.substring(0, 16) + "N";
        } else if (enos.equalsIgnoreCase("15")) {
            nopol = nopolis.substring(0, 16) + "O";
        } else if (enos.equalsIgnoreCase("16")) {
            nopol = nopolis.substring(0, 16) + "P";
        } else if (enos.equalsIgnoreCase("17")) {
            nopol = nopolis.substring(0, 16) + "Q";
        } else if (enos.equalsIgnoreCase("18")) {
            nopol = nopolis.substring(0, 16) + "R";
        } else if (enos.equalsIgnoreCase("19")) {
            nopol = nopolis.substring(0, 16) + "S";
        } else if (enos.equalsIgnoreCase("20")) {
            nopol = nopolis.substring(0, 16) + "T";
        } else if (enos.equalsIgnoreCase("21")) {
            nopol = nopolis.substring(0, 16) + "U";
        } else if (enos.equalsIgnoreCase("22")) {
            nopol = nopolis.substring(0, 16) + "V";
        } else if (enos.equalsIgnoreCase("23")) {
            nopol = nopolis.substring(0, 16) + "W";
        } else if (enos.equalsIgnoreCase("24")) {
            nopol = nopolis.substring(0, 16) + "X";
        } else if (enos.equalsIgnoreCase("25")) {
            nopol = nopolis.substring(0, 16) + "Y";
        } else if (enos.equalsIgnoreCase("26")) {
            nopol = nopolis.substring(0, 16) + "Z";
        } else if (enos.equalsIgnoreCase("27")) {
            nopol = nopolis.substring(0, 16) + "a";
        } else if (enos.equalsIgnoreCase("28")) {
            nopol = nopolis.substring(0, 16) + "b";
        } else if (enos.equalsIgnoreCase("29")) {
            nopol = nopolis.substring(0, 16) + "c";
        } else if (enos.equalsIgnoreCase("30")) {
            nopol = nopolis.substring(0, 16) + "d";
        } else if (enos.equalsIgnoreCase("31")) {
            nopol = nopolis.substring(0, 16) + "e";
        } else if (enos.equalsIgnoreCase("32")) {
            nopol = nopolis.substring(0, 16) + "f";
        } else if (enos.equalsIgnoreCase("33")) {
            nopol = nopolis.substring(0, 16) + "g";
        } else if (enos.equalsIgnoreCase("34")) {
            nopol = nopolis.substring(0, 16) + "h";
        } else if (enos.equalsIgnoreCase("35")) {
            nopol = nopolis.substring(0, 16) + "i";
        } else if (enos.equalsIgnoreCase("36")) {
            nopol = nopolis.substring(0, 16) + "j";
        } else if (enos.equalsIgnoreCase("37")) {
            nopol = nopolis.substring(0, 16) + "k";
        } else if (enos.equalsIgnoreCase("38")) {
            nopol = nopolis.substring(0, 16) + "l";
        } else if (enos.equalsIgnoreCase("39")) {
            nopol = nopolis.substring(0, 16) + "m";
        } else if (enos.equalsIgnoreCase("40")) {
            nopol = nopolis.substring(0, 16) + "n";
        } else if (enos.equalsIgnoreCase("41")) {
            nopol = nopolis.substring(0, 16) + "o";
        } else if (enos.equalsIgnoreCase("42")) {
            nopol = nopolis.substring(0, 16) + "p";
        } else if (enos.equalsIgnoreCase("43")) {
            nopol = nopolis.substring(0, 16) + "q";
        } else if (enos.equalsIgnoreCase("44")) {
            nopol = nopolis.substring(0, 16) + "r";
        } else if (enos.equalsIgnoreCase("45")) {
            nopol = nopolis.substring(0, 16) + "s";
        } else if (enos.equalsIgnoreCase("46")) {
            nopol = nopolis.substring(0, 16) + "t";
        } else if (enos.equalsIgnoreCase("47")) {
            nopol = nopolis.substring(0, 16) + "u";
        } else if (enos.equalsIgnoreCase("48")) {
            nopol = nopolis.substring(0, 16) + "v";
        } else if (enos.equalsIgnoreCase("49")) {
            nopol = nopolis.substring(0, 16) + "w";
        } else if (enos.equalsIgnoreCase("50")) {
            nopol = nopolis.substring(0, 16) + "x";
        } else if (enos.equalsIgnoreCase("51")) {
            nopol = nopolis.substring(0, 16) + "y";
        } else if (enos.equalsIgnoreCase("52")) {
            nopol = nopolis.substring(0, 16) + "z";
        } else if (enos.equalsIgnoreCase("00")) {
            nopol = nopolis.substring(0, 16);
        } else {
            nopol = nopolis;
        }

        return nopol;
    }

    public DTOList getABAKoasur() {
        loadABAKoasur();
        return abakoasur;
    }

    public void setABAKoasur(DTOList abakoasur) {
        this.abakoasur = abakoasur;
    }

    public void loadABAKoasur() {
        try {
            if (abakoasur == null) {
                abakoasur = ListUtil.getDTOListFromQuery(
                        "select * from aba_koasur where nopol = ? order by norek",
                        new Object[]{getPolicyNoOldFormat2(stPolicyNo).substring(0, 16)},
                        InsuranceKoasurView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getCoinsAll() {
        if (coins == null) {
            loadCoinsAll();
        }
        return coins;
    }

    public void loadCoinsAll() {
        try {
            //String condition = " policy_id = ? ";//old
            //new
            String condition = " policy_id = ? ";
            if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                condition = condition + " order by entity_id, ins_pol_coins_id";
            }
            if (coins == null) {
                coins = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where"
                        + condition,
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getCoverageReinsurance() {
        if (coverageReins == null) {
            loadCoverageReinsurance();
        }
        return coverageReins;
    }

    public void loadCoverageReinsurance() {
        try {
            if (coverageReins == null) {
                coverageReins = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_cover_ri"
                        + "   where"
                        + " pol_id = ?  ",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoverReinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateCoverageReinsurance() throws Exception {
        BigDecimal premiKreasi = null;
        BigDecimal premiPHK = null;

        if (!getStPolicyTypeID().equals("21")) {
            return;
        }

        InsurancePolicyCoinsView coins = getHoldingCoin();

        for (int j = 0; j < objects.size(); j++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

            final DTOList covers = obj.getCoverage();
            for (int k = 0; k < covers.size(); k++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(k);

                if (cov.getStInsuranceCoverID().equalsIgnoreCase("78")) {
                    premiKreasi = BDUtil.add(premiKreasi, cov.getDbPremi());
                }

                if (cov.getStInsuranceCoverID().equalsIgnoreCase("124") || cov.getStInsuranceCoverID().equalsIgnoreCase("125")) {
                    premiPHK = BDUtil.add(premiPHK, cov.getDbPremi());
                }
            }
        }

        final DTOList coverReins = getCoverageReinsurance();
        for (int i = 0; i < coverReins.size(); i++) {
            InsurancePolicyCoverReinsView coverrei = (InsurancePolicyCoverReinsView) coverReins.get(i);

            coverrei.setDbInsuredAmount(BDUtil.mul(coins.getDbAmount(), BDUtil.getRateFromPct(coverrei.getDbSharePct())));
            if (coverrei.getStInsuranceCoverID().equalsIgnoreCase("78")) {
                coverrei.setDbPremi(BDUtil.mul(premiKreasi, BDUtil.getRateFromPct(coverrei.getDbSharePct())));
                coverrei.setDbPremi(BDUtil.roundUp(coverrei.getDbPremi()));
            }

            if (coverrei.getStInsuranceCoverID().equalsIgnoreCase("124") || coverrei.getStInsuranceCoverID().equalsIgnoreCase("125")) {
                coverrei.setDbPremi(BDUtil.mul(premiPHK, BDUtil.getRateFromPct(coverrei.getDbSharePct())));
                coverrei.setDbPremi(BDUtil.roundUp(coverrei.getDbPremi()));
            }

            coverrei.setDbCommissionAmount(BDUtil.mul(coverrei.getDbPremi(), BDUtil.getRateFromPct(coverrei.getDbCommissionRate())));
            coverrei.setDbCommissionAmount(BDUtil.roundUp(coverrei.getDbCommissionAmount()));
        }
    }
    private DTOList objects_year;

    public void setObjects_Year(DTOList objects_year) {
        this.objects_year = objects_year;
    }

    public DTOList getObjects_Year() {
        loadObjects_Year();
        return objects_year;
    }

    public void loadObjects_Year() {
        try {
            getClObjectClass();
            if (objects_year == null) {
                objects_year = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ref6,ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects_year.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects_year.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getCoins3() {
        loadCoins3();
        return coins;
    }

    public void loadCoins3() {
        try {
            String condition = " policy_id = ? ";
            if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                condition = condition + " order by entity_id, ins_pol_coins_id";
            }

            coins = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      *"
                    + "   from"
                    + "      ins_pol_coins"
                    + "   where"
                    + condition,
                    new Object[]{getStPolicyID()},
                    InsurancePolicyCoinsView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generatePersetujuanPrinsipEndorseNo() {

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

        final char[] persetujuanprinsipno = stReference1.toCharArray();

        /*
        5844440910001400
        01234567890123456
         **/

        final String enos = stReference1.substring(14, 16);

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 2);

        final char[] ze = z.toCharArray();

        persetujuanprinsipno[14] = ze[0];
        persetujuanprinsipno[15] = ze[1];

        stReference1 = new String(persetujuanprinsipno);

    }

    public InsuranceSplitPolicyView getStParentPolicy() {
        return (InsuranceSplitPolicyView) DTOPool.getInstance().getDTO(InsuranceSplitPolicyView.class, stReference2);
    }
    private DTOList rootpolicy2;

    public DTOList getRootPolicy2() {
        loadRootPolicy2();
        return rootpolicy2;
    }

    public void loadRootPolicy2() {
        try {
            if (rootpolicy2 == null) {
                rootpolicy2 = ListUtil.getDTOListFromQuery(
                        "select * "
                        + " from ins_policy "
                        + " where pol_no = ? limit 1",
                        new Object[]{stPolicyNo},
                        InsuranceSplitPolicyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkPolicyNoBefore(String stPolicyNo) throws Exception {
        final DTOList root2 = getRootPolicy2();

        for (int i = 0; i < root2.size(); i++) {
            InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) root2.get(i);

            if (pol.getStPolicyNo().equalsIgnoreCase(stPolicyNo)) {
                throw new RuntimeException("No Polis " + stPolicyNo + " Sudah Pernah Dibuat");
            }

        }
    }
    private boolean approvalMode;

    public boolean isApprovalMode() {
        return approvalMode;
    }

    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public DTOList getObjectsBAJ() {
        loadObjectsBAJ();
        return objects;
    }

    public void loadObjectsBAJ() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? and ref8 = '94' order by ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCoinsSelf() {

        final InsuranceCoverSourceView cs = getCoverSource();

        if (cs == null) {
            return false;
        }

        return cs.isCoinsSelf();
    }
    private boolean coasAJS = false;
    private boolean coasJS = false;

    public boolean isCoasAJS() {
        return coasAJS;
    }

    public void setCoasAJS(boolean coasAJS) {
        this.coasAJS = coasAJS;
    }

    public boolean isCoasJS() {
        return coasJS;
    }

    public void setCoasJS(boolean coasJS) {
        this.coasJS = coasJS;
    }
    private DTOList konversiDocuments;

    public DTOList getKonversiDocuments() {
        if (konversiDocuments == null && stPolicyID != null) {
            konversiDocuments = loadKonversiDocuments(stPolicyID, "KONVERSI");
        } else if (konversiDocuments == null && stPolicyID == null) {
            konversiDocuments = loadKonversiDocuments("KONVERSI");
        }
        return konversiDocuments;
    }

    private static DTOList loadKonversiDocuments(String stPolicyID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      b.*,c.description,a.ins_document_type_id "
                    + "   from "
                    + "      ins_documents a"
                    + "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id"
                    + "      left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id"
                    + "   where "
                    + "      a.document_class=? ",
                    new Object[]{stPolicyID, documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

                docs.setStPolicyID(stPolicyID);
                docs.setStDocumentClass(documentClass);
            }

            return l;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static DTOList loadKonversiDocuments(String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      c.*,a.ins_document_type_id "
                    + "   from "
                    + "      ins_documents a"
                    + "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id"
                    + "   where "
                    + "      a.document_class=? ",
                    new Object[]{documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

                //docs.setStPolicyID(stPolicyID);
                docs.setStDocumentClass(documentClass);
            }

            return l;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setKonversiDocuments(DTOList konversiDocuments) {
        this.konversiDocuments = konversiDocuments;
    }

    public String getStWarranty() {
        return stWarranty;
    }

    public void setStWarranty(String stWarranty) {
        this.stWarranty = stWarranty;
    }

    /*
    public String getStCoinsuranceTypeID() {
    return stCoinsuranceTypeID;
    }

    public void setStCoinsuranceTypeID(String stCoinsuranceTypeID) {
    this.stCoinsuranceTypeID = stCoinsuranceTypeID;
    }*/
    public String getStUnderwritingFinishFlag() {
        return stUnderwritingFinishFlag;
    }

    public void setStUnderwritingFinishFlag(String stUnderwritingFinishFlag) {
        this.stUnderwritingFinishFlag = stUnderwritingFinishFlag;
    }

    public String getStClaimFinishFlag() {
        return stClaimFinishFlag;
    }

    public void setStClaimFinishFlag(String stClaimFinishFlag) {
        this.stClaimFinishFlag = stClaimFinishFlag;
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public void validateLimit(boolean raiseErrors) {

        stRIFinishFlag = null;
        stUnderwritingFinishFlag = null;
        stClaimFinishFlag = null;

        if (!(isStatusEndorse() || isStatusPolicy() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern())) {
            return;
        }

        DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objDefault = (InsurancePolicyObjDefaultView) objects.get(i);

            DTOList treatyDetails = obj.getTreatyDetails();

            BigDecimal premiRITotal = null;

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                boolean equalTSI = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                boolean equalPremi = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyPremiTotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbPremiAmount(), BDUtil.one, scale));

                if (!trd.getTreatyDetail().getTreatyType().isNonXOL()) {
                    premiRITotal = BDUtil.add(premiRITotal, trd.getDbMemberTreatyPremiTotal());
                }

                if (!equalTSI) {
                    if (raiseErrors) {
                        throw new RuntimeException("Alokasi TSI Reas Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                    } else {
                        return;
                    }
                }

                if (!equalPremi) {
                    if (raiseErrors) {
                        throw new RuntimeException("Alokasi Premi Reas Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                    } else {
                        return;
                    }
                }

                DTOList shares = trd.getShares();

                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                    if (!ri.isApproved()) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi Reas Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Belum Disetujui");
                        } else {
                            return;
                        }
                    }

                    if (ri.getStApprovedFlag() == null) {
                        stRIFinishFlag = null;
                    }
                }
            }

            boolean equalPremiTotal = true;

            if (BDUtil.lesserThan(BDUtil.sub(obj.getDbObjectPremiTotalAmount(), premiRITotal), new BigDecimal(-1)) || BDUtil.biggerThan(BDUtil.sub(obj.getDbObjectPremiTotalAmount(), premiRITotal), BDUtil.one)) {
                equalPremiTotal = false;
            }

            /*
            if(!equalPremiTotal){
            if (raiseErrors)
            throw new RuntimeException("Alokasi Premi Reas Objek ["+ (i + 1) +"] " + objDefault.getStReference1() + " Tidak Sama Antara Total dengan rincian");
            else
            return;
            }*/
        }

        stRIFinishFlag = "Y";

    }
    private DTOList objects_koas;

    public void setObjects_Koas(DTOList objects_koas) {
        this.objects_koas = objects_koas;
    }

    public DTOList getObjects_Koas() {
        loadObjects_Koas();
        return objects_koas;
    }

    public void loadObjects_Koas() {
        try {
            getClObjectClass();
            if (objects_koas == null) {
                objects_koas = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ref8,ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects_koas.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects_koas.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStKodePos(String kodePos) throws Exception {

        SQLUtil S = new SQLUtil();
        String ins_risk_cat_id = null;

        try {
            /*
            PreparedStatement PS = S.setQuery("select region_map_id "+
            " building_desc from s_region_map3 "+
            " where (upper(region_name) like ? "+
            " or upper(city_name) like ? or postal_code like ? "+
            " or building_desc like ?) limit 1");
            PS.setString(1, "%"+ kodePos +"%");
            PS.setString(2, "%"+ kodePos +"%");
            PS.setString(3, "%"+ kodePos +"%");
            PS.setString(4, "%"+ kodePos +"%");
             */

            PreparedStatement PS = S.setQuery("select region_map_id "
                    + " building_desc from s_region_map3 "
                    + " where postal_code = ? "
                    + " limit 1");
            PS.setString(1, kodePos);

            ResultSet RS = PS.executeQuery();
            if (RS.next()) {
                ins_risk_cat_id = RS.getString(1);
            }
        } finally {
            S.release();
        }

        return ins_risk_cat_id;
    }

    @Override
    public String getStCreateWho() {
        return stCreateWho;
    }

    @Override
    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }

    public boolean isStatusTemporaryPolicy() {
        return FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusTemporaryEndorsemen() {
        return FinCodec.PolicyStatus.ENDORSETEMPORARY.equalsIgnoreCase(getStCurrentStatus());
    }

    public GLCurrencyView getCurrency() {
        return (GLCurrencyView) DTOPool.getInstance().getDTO(GLCurrencyView.class, stCurrencyCode);
    }

    public InsurancePolicyCoinsView getHoldingCoinCoverage() {

        if (holdingCoinCoverage == null) {
            final DTOList coins = getCoinsCoverage();
            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coinsView = (InsurancePolicyCoinsView) coins.get(i);

                if (coinsView.isHoldingCompany()) {
                    holdingCoinCoverage = coinsView;
                }
            }
        }

        return holdingCoinCoverage;
    }

    public InsuranceSplitPolicyView getTemporaryPolicy() {
        return (InsuranceSplitPolicyView) DTOPool.getInstance().getDTO(InsuranceSplitPolicyView.class, stReference6);
    }
    private DTOList reins;

    public DTOList getReins() {
        loadReins();
        return reins;
    }

    public void setReins(DTOList reins) {
        this.reins = reins;
    }

    public void loadReins() {
        //if (!isAutoLoadEnabled()) return;
        try {
            //if (reins == null)
            reins = ListUtil.getDTOListFromQuery(
                    "select f.ins_treaty_detail_id,ins_treaty_shares_id,e.member_ent_id, sum(e.tsi_amount) as tsi_amount, sum(coalesce(e.tsi_amount_e,0)) as tsi_amount_e, "
                    + " sum(e.premi_amount) premi_amount, sum(coalesce(e.premi_amount_e,0)) premi_amount_e, sum(e.ricomm_amt) ricomm_amt, sum(coalesce(e.ricomm_amt_e,0)) ricomm_amt_e, e.ri_slip_no "
                    + " from ins_policy a "
                    + " inner join ins_pol_obj b on a.pol_id = b.pol_id "
                    + " inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "
                    + " inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id "
                    + " inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id "
                    + " inner join ins_pol_ri e on d.ins_pol_tre_det_id = e.ins_pol_tre_det_id "
                    + " where  a.pol_id = ? "
                    + " group by e.ins_treaty_shares_id,f.ins_treaty_detail_id,e.member_ent_id,e.ri_slip_no",
                    new Object[]{stPolicyID},
                    InsurancePolicyReinsView.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InsuranceClaimCauseView getClaimCauses() {
        return (InsuranceClaimCauseView) DTOPool.getInstance().getDTO(InsuranceClaimCauseView.class, stClaimCauseID);
    }
    public DTOList claimItemsFee;

    public DTOList getClaimItemsFee() {
        loadClaimItemsFee();
        return claimItemsFee;
    }

    private void loadClaimItemsFee() {
        try {
            if (claimItemsFee == null) {
                claimItemsFee = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='CLM' and ins_item_id in (50,76,78,81,82,83,84) order by ins_pol_item_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setClaimItemsFee(DTOList claimItemsFee) {
        this.claimItemsFee = claimItemsFee;
    }

    public boolean checkHistoryPolicyNoUsingExcel() throws Exception {
        final DTOList history = getHistoryPolicy();
        boolean sudahPernah = false;
        for (int i = 0; i < history.size(); i++) {
            InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) history.get(i);

            if (pol.getStPolicyNo().equalsIgnoreCase(getStPolicyNo().substring(0, 16))) {
                if (getStPolicyID() == null) {
                    sudahPernah = true;
                }
            }
            //throw new RuntimeException("Polis History "+ getStPolicyNo().substring(0,16) + " Sudah Pernah Dibuat");
        }
        return sudahPernah;
    }

    public BigDecimal getDbTotalComm2() {

        return BDUtil.sub(dbTotalDue, dbPremiNetto);

    }
    private DTOList rootclaim;

    public DTOList getRootClaim() {
        loadRootClaim();
        return rootclaim;
    }

    public void loadRootClaim() {
        try {
            if (rootclaim == null) {
                rootclaim = ListUtil.getDTOListFromQuery(
                        "select pol_no "
                        + " from ins_policy "
                        + " where substr(pol_no,0,17) = ? and active_flag='Y' and effective_flag = 'Y' order by pol_no desc",
                        new Object[]{stPolicyNo.subSequence(0, 16)},
                        InsuranceSplitPolicyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkLastPolicyNo(String stPolicyNo) throws Exception {
        final DTOList claim = getRootClaim();

        for (int i = 0; i < claim.size(); i++) {
            InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) claim.get(0);

            if (!pol.getStPolicyNo().equalsIgnoreCase(stPolicyNo)) {
                throw new RuntimeException("Klaim harus dibuat dari no polis/endorsemen terakhir : " + pol.getStPolicyNo());
            }

        }
    }

    private void invokeCustomHandlersCoinsurance(boolean validate) {
        // CustomHandlerManager.getInstance().onCalculateCoinsurance(this);
    }

    public String getStEndorseMode() {
        return stEndorseMode;
    }

    public void setStEndorseMode(String stEndorseMode) {
        this.stEndorseMode = stEndorseMode;
    }

    public boolean isBatalTotalEndorseMode() {
        return "TOTAL".equalsIgnoreCase(getStEndorseMode());
    }

    public boolean isTSIEndorseMode() {
        return "PARTIAL_SI".equalsIgnoreCase(getStEndorseMode());
    }

    public boolean isRateEndorseMode() {
        return "PARTIAL_RATE".equalsIgnoreCase(getStEndorseMode());
    }

    public boolean isDescriptionEndorseMode() {
        return "DESCRIPTION".equalsIgnoreCase(getStEndorseMode());
    }

    public boolean isRestitutionEndorseMode() {
        return "RESTITUTION".equalsIgnoreCase(getStEndorseMode());
    }

    public boolean isOthersEndorseMode() {
        return "OTHERS".equalsIgnoreCase(getStEndorseMode());
    }

    public String getStRejectFlag() {
        return stRejectFlag;
    }

    public void setStRejectFlag(String stRejectFlag) {
        this.stRejectFlag = stRejectFlag;
    }

    public String getStApprovedName() {
        //if(stApprovedWho==null) return "";

        //UserSessionView view = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stApprovedWho);

        //return view.getStUserName();
        return stApprovedName;
    }

    public BigDecimal getDbAPTaxP() {
        return dbAPTaxP;
    }

    public void setDbAPTaxP(BigDecimal dbAPTaxP) {
        this.dbAPTaxP = dbAPTaxP;
    }

    public BigDecimal getDbAPTax() {
        return dbAPTax;
    }

    public void setDbAPTax(BigDecimal dbAPTax) {
        this.dbAPTax = dbAPTax;
    }

    public void recalculateMandiri() throws Exception {
        recalculateBasicMandiri();
        recalculateClaim();
        invokeCustomHandlers(true);
        recalculateBasicMandiri();
        invokeCustomHandlers(false);
        recalculateClaim();
        recalculateEndorsePAKreasi();

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen()) {
            validateTreaty(false);
        }

    }

    public void recalculateBasicMandiri() throws Exception {
        if (stPolicyTypeID == null) {
            return;
        }

        loadClausules();
        //loadEntities();
        loadDetails();
        loadObjects();

        stUnderwritingFinishFlag = "Y";

        if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
            scale = 2;
        } else {
            scale = 0;
        }

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

        //calc period factor
        DateTime startDate = new DateTime(getDtPeriodStart());
        DateTime endDate = new DateTime(getDtPeriodEnd());
        Years y = Years.yearsBetween(startDate, endDate);
        int year = y.getYears();

        if (year <= 1) {
            setDbPeriodRate(BDUtil.one);
        } else if (year > 1) {
            setDbPeriodRate(new BigDecimal(year));
        }
        //end

        setCoasAJS(false);
        setCoasJS(false);

        if ((objects != null) && (objects.size() > 0)) {

            dbInsuredAmount = null;
            dbInsuredAmountAfterKurs = null;

            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                obj.reCalculate();

                //penentuan coas kreasi ke BAJ /JS

                if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                    InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                    final boolean isMoreThan500Million = BDUtil.biggerThanEqual(objx.getDbReference4(), new BigDecimal(500000000));
                    final boolean lesserThan500Million = BDUtil.lesserThan(objx.getDbReference4(), new BigDecimal(500000000));
                    final boolean alwaysUseJiwasraya = stCostCenterCode.equalsIgnoreCase("20") || stCostCenterCode.equalsIgnoreCase("21")
                            || stCostCenterCode.equalsIgnoreCase("43") || stCostCenterCode.equalsIgnoreCase("40")
                            || stCostCenterCode.equalsIgnoreCase("70");

                    if (!isStatusHistory() && !isStatusEndorse() && !isStatusClaim() && !isStatusClaimEndorse() && !isStatusEndorseRI()) {
                        if (isMoreThan500Million) {
                            objx.setStReference8("96");
                        } else if (lesserThan500Million) {
                            objx.setStReference8("2000");
                        }

                        if (alwaysUseJiwasraya) {
                            objx.setStReference8("96");
                        }

                        setStCoinsID(null);
                    }

                    if (isStatusHistory() || isStatusEndorse()) {
                        if (getStCoinsID() != null) {
                            if (objx.getStReference8() == null) {
                                objx.setStReference8(getStCoinsID());
                            }
                        }
                    }


                    if (objx.getStReference8() != null) {
                        if (objx.getStReference8().equalsIgnoreCase("2000")) {
                            setCoasAJS(true);
                        }

                        if (objx.getStReference8().equalsIgnoreCase("96")) {
                            setCoasJS(true);
                        }
                    }
                }

                if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND) || getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)) {
                    InsurancePolicyObjDefaultView objc = (InsurancePolicyObjDefaultView) obj;
                    if (objc.getStReference1() != null) {
                        setStReference5(objc.getStReference1()); //nama principal
                        setStReference7(objc.getStReference2());//id principal
                        setStReference8(objc.getStReference3());//alamat principal
                    }
                }

                dbInsuredAmountAfterKurs = BDUtil.add(dbInsuredAmountAfterKurs, BDUtil.mul(obj.getDbObjectInsuredAmount(), dbCurrencyRate, scale));
                dbInsuredAmount = BDUtil.add(dbInsuredAmount, obj.getDbObjectInsuredAmount());
                dbPremiBase = BDUtil.add(dbPremiBase, obj.getDbObjectPremiAmount());
            }
            //RECALCULATE CLAIM KE CO BAJ /JS
            if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                if (isStatusClaim() && getClaimObject() != null) {
                    setCoasAJS(false);
                    setCoasJS(false);
                    InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) getClaimObject();
                    if (obje.getStReference8() != null) {
                        if (obje.getStReference8().equalsIgnoreCase("2000")) {
                            setCoasAJS(true);
                        }

                        if (obje.getStReference8().equalsIgnoreCase("96")) {
                            setCoasJS(true);
                        }
                    }
                }
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

                        if (clsMap.containsKey(icl.getStClauseID())) {
                            continue;
                        }

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


            for (int i = cls.size() - 1; i >= 0; i--) {
                InsurancePolicyClausulesView ccl = (InsurancePolicyClausulesView) cls.get(i);

                if (!masterMap.containsKey(ccl.getStClauseID())) {
                    cls.delete(i);
                }
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

            if (!item.isPremi()) {
                continue;
            }

            item.calculateRateAmount(getDbInsuredAmount(), scale);

            dbPremiTotal = BDUtil.add(dbPremiTotal, item.getDbAmount());
        }

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isDiscount()) {
                continue;
            }

            item.calculateRateAmount(dbPremiTotal, scale);

            totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
        }
        final BigDecimal totalAfterDiscount = BDUtil.sub(dbPremiTotal, totalDiscount);

        dbPremiTotalAfterDisc = totalAfterDiscount;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isFee()); else {
                continue;
            }

            item.calculateRateAmount(totalAfterDiscount, scale);

            dbTotalFee = BDUtil.add(dbTotalFee, item.getDbAmount());
        }

        dbTotalDue = BDUtil.add(dbPremiTotalAfterDisc, dbTotalFee);

        InsurancePolicyCoinsView co = getHoldingCoin();

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if (!BDUtil.isZeroOrNull(co.getDbCommissionAmount())) {
                item.setDbAmount(co.getDbCommissionAmount());
            }
            item.calculateRateAmount(totalAfterDiscount, scale);

            totalComission = BDUtil.add(totalComission, item.getDbAmount());

            if (item.isAutoTaxRate()) {
                if (item.getStTaxCode() != null) {
                    item.setDbTaxRate(item.getTax().getDbRate());
                }
            }

            if (item.getStTaxCode() != null) {
                totalTax = BDUtil.add(totalTax, item.getDbTaxAmount());
            }

        }

        //set data polis komisi, bfee, hfee, biaya polis, materai, diskon
        recalculateFee();

        //calculatePPH21Progressive2();

        calculatePPH21And23();

        //tambahin pengecekan yg punya NPWP dan Tidak Punya NPWP
        /* jika ada NPWP = 2%
         * jika tidak ada NPWP = 4%
         *
         */
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if (item.isAutoTaxAmount()) {
                item.setDbTaxAmount(BDUtil.mul(item.getDbTaxRate(), item.getDbAmount(), scale));
            }
        }

        dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission);

        /*
        if(isCoinsSelf()){
        dbPremiNetto = BDUtil.sub(dbTotalDue, totalComission2);
        }*/


        final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

        glApplicator.setCode('X', getStPolicyTypeID());
        glApplicator.setDesc("X", getStPolicyTypeDesc());
        glApplicator.setCode('Y', getEntity().getStGLCode());
        if (getEntity().getStGLCode().equalsIgnoreCase("00000")) {
            glApplicator.setDesc("Y", "");
        } else {
            glApplicator.setDesc("Y", getStEntityName());
        }

        glApplicator.setCode('B', getStCostCenterCode());

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            InsuranceItemsView insItem = item.getInsItem();

            if (insItem == null) {
                throw new RuntimeException("Ins item not found : " + item);
            }

            ARTransactionLineView arTrxLine = insItem.getARTrxLine();

            if (arTrxLine == null) {
                throw new RuntimeException("AR TRX Line not found : " + item);
            }

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

                if (coin.isHoldingCompany()) {
                    continue;
                }


                //if (!coin.isEntryByPctRate()) {
                //    final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero) > 0;
                    /*BigDecimal pct;

                if (hasAmount)
                pct = BDUtil.div(coin.getDbAmount(), dbInsuredAmount);
                else
                pct = BDUtil.zero;

                coin.setDbSharePct(BDUtil.getPctFromRate(pct));*/

                //} else {
                //    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                //}

                if (coin.isEntryByPctRate()) {
                    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                    coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }


                //if (coin.isAutoPremi())
                //coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));

                if (!coin.isEntryByPctRate() && !coin.isAutoPremi()) {
                    coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5));
                    coin.setDbPremiAmount(BDUtil.mul(BDUtil.div(coin.getDbAmount(), dbInsuredAmount, 5), dbPremiTotal, scale));
                }

                if (!coin.isEntryByPctRate() && coin.isAutoPremi()) {
                    //coin.setDbSharePct(BDUtil.div(coin.getDbAmount(), dbInsuredAmount,5));
                    coin.setDbPremiAmount(coin.getDbPremiAmount());
                }

                coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                //if(!"21".equalsIgnoreCase(getStPolicyTypeID())) coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));

                totPct = BDUtil.add(totPct, coin.getDbSharePct());
                totTSI = BDUtil.add(totTSI, coin.getDbAmount());

                checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
                checkPremiAmount = BDUtil.add(checkPremiAmount, coin.getDbPremiAmount());

            }
            if (holdingcoin != null) {
                holdingcoin.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
                holdingcoin.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
                if (holdingcoin.isAutoPremi()) {
                    holdingcoin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoin.getDbSharePct()), scale));
                }

                if (BDUtil.biggerThan(BDUtil.add(holdingcoin.getDbPremiAmount(), checkPremiAmount), dbPremiTotal)) {
                    holdingcoin.setDbPremiAmount(BDUtil.sub(dbPremiTotal, checkPremiAmount));
                }

                holdingcoin.setDbCommissionAmount(BDUtil.mul(holdingcoin.getDbPremiAmount(), BDUtil.getRateFromPct(holdingcoin.getDbCommissionRate()), scale));
                checkInsAmount = BDUtil.add(checkInsAmount, holdingcoin.getDbAmount());
            }

            dbCoinsCheckInsAmount = checkInsAmount;
        }

        //recalculate co cover
        if (isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen()) {
            final BigDecimal BD100 = new BigDecimal(100);

            BigDecimal checkInsAmount = null;
            BigDecimal checkPremiAmount = null;

            InsurancePolicyCoinsView holdingcoincoverage = getHoldingCoinCoverage();

            BigDecimal totPct = null;
            BigDecimal totTSI = null;
            BigDecimal premiAfterDisc = null;

            DTOList coinsCoverage2 = getCoinsCoverage();
            for (int i = 0; i < coinsCoverage2.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinsCoverage2.get(i);

                if (coin.isHoldingCompany()) {
                    continue;
                }

                if (!coin.isEntryByPctRate()) {
                    final boolean hasAmount = Tools.compare(coin.getDbAmount(), BDUtil.zero) > 0;
                    BigDecimal pct;

                    if (hasAmount) {
                        pct = BDUtil.div(dbInsuredAmount, coin.getDbAmount());
                    } else {
                        pct = BDUtil.zero;
                    }

                    coin.setDbSharePct(BDUtil.getPctFromRate(pct));
                } else {
                    coin.setDbAmount(BDUtil.mul(dbInsuredAmount, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }

                if (coin.isAutoPremi()) {
                    coin.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(coin.getDbSharePct()), scale));
                }

                coin.setDbDiscountAmount(BDUtil.mul(coin.getDbPremiAmount(), BDUtil.getRateFromPct(coin.getDbDiscountRate()), scale));
                premiAfterDisc = BDUtil.sub(coin.getDbPremiAmount(), coin.getDbDiscountAmount());
                if (!"21".equalsIgnoreCase(getStPolicyTypeID())) {
                    coin.setDbCommissionAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbCommissionRate()), scale));
                }
                coin.setDbBrokerageAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbBrokerageRate()), scale));
                coin.setDbHandlingFeeAmount(BDUtil.mul(premiAfterDisc, BDUtil.getRateFromPct(coin.getDbHandlingFeeRate()), scale));


                totPct = BDUtil.add(totPct, coin.getDbSharePct());
                totTSI = BDUtil.add(totTSI, coin.getDbAmount());

                checkInsAmount = BDUtil.add(checkInsAmount, coin.getDbAmount());
            }
            if (holdingcoincoverage != null) {
                holdingcoincoverage.setDbSharePct(BDUtil.sub(BDUtil.hundred, totPct));
                holdingcoincoverage.setDbAmount(BDUtil.sub(dbInsuredAmount, totTSI));
                if (holdingcoincoverage.isAutoPremi()) {
                    holdingcoincoverage.setDbPremiAmount(BDUtil.mul(dbPremiTotal, BDUtil.getRateFromPct(holdingcoincoverage.getDbSharePct()), scale));
                }

                checkInsAmount = BDUtil.add(checkInsAmount, holdingcoincoverage.getDbAmount());
            }

            dbCoinsCheckInsAmount = checkInsAmount;
        }

        //end recalculate co cover

        reCalculateInstallment();

        if (isStatusEndorse() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryEndorsemen()) {
            recalculateEndorsement();
        }

    }

    public String getStStatusOther() {
        return stStatusOther;
    }

    public void setStStatusOther(String stStatusOther) {
        this.stStatusOther = stStatusOther;
    }

    public boolean validateClaimObject(InsurancePolicyObjectView claimObj) throws Exception {
        final DTOList suminsured = claimObj.getSuminsureds();

        BigDecimal totalTSI = null;
        boolean canNotClaim = false;
        for (int i = 0; i < suminsured.size(); i++) {
            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsured.get(i);

            totalTSI = BDUtil.add(totalTSI, tsi.getDbInsuredAmount());
        }

        if (BDUtil.isZeroOrNull(totalTSI)) {
            canNotClaim = true;
        }

        if (canNotClaim) {
            setStClaimObjectID(null);
            //return false;
            //throw new RuntimeException("TSI object klaim sudah nol, tidak bisa di klaim");  
        }

        return canNotClaim;
    }

    public String getStRejectNotes() {
        return stRejectNotes;
    }

    public void setStRejectNotes(String stRejectNotes) {
        this.stRejectNotes = stRejectNotes;
    }

    public String getStApprovedReinsName() {
//        if(stReinsuranceApprovedWho==null) return "";
//        
//        UserSessionView view = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stReinsuranceApprovedWho);
//        
//        return view.getStUserName();
        return stApprovedReinsName;
    }

    public void validateDoubleObject() throws Exception {
        if (getStPolicyTypeID().equalsIgnoreCase("21") || getStPolicyTypeID().equalsIgnoreCase("59") || getStPolicyTypeID().equalsIgnoreCase("80")) {
            if (isStatusDraft() || isStatusSPPA() || isStatusPolicy()) {
                final SQLUtil S = new SQLUtil();
                String desc = "";
                boolean isDouble = false;

                final DTOList object = getObjects();

                for (int i = 0; i < object.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

                    InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                    boolean tidakDouble = false;

                    if (getStPolicyTypeID().equalsIgnoreCase("59")) {
                        if (objx.getStReference15() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference15());
                        }
                    }

                    if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                        if (objx.getStReference24() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference24());
                        }
                    }

                    if (getStPolicyTypeID().equalsIgnoreCase("80")) {
                        if (objx.getStReference15() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference15());
                        }
                    }

                    if (tidakDouble) {
                        continue;
                    }

                    try {
                        final PreparedStatement PS = S.setQuery("select a.pol_id,a.pol_no,b.ins_pol_obj_id,b.ref1,b.refd1,b.refd2 "
                                + " from ins_policy a inner join ins_pol_obj b on a.pol_id = b.poL_id "
                                + " where a.cc_code = ? and trim(upper(b.ref1)) = ? and b.refd1 = ? and b.refd2 = ? and b.insured_amount = ? and a.status in ('POLICY','RENEWAL') and coalesce(a.active_flag,'N') = 'Y'");

                        PS.setString(1, getStCostCenterCode());
                        PS.setString(2, objx.getStReference1().trim().toUpperCase());
                        PS.setObject(3, objx.getDtReference1());
                        PS.setObject(4, objx.getDtReference2());
                        PS.setObject(5, objx.getDbObjectInsuredAmount());

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()) {
                            if (objx.getStPolicyObjectID() != null) {
                                if (objx.getStPolicyObjectID().equalsIgnoreCase(RS.getString("ins_pol_obj_id"))) {
                                    continue;
                                }
                            }

                            desc = desc + "Debitur " + objx.getStReference1() + " sudah diinput pada nopol : " + RS.getString("pol_no") + "<br>";
                            isDouble = true;
                        }

                    } finally {
                        S.release();
                    }
                }

                if (isDouble) {
                    throw new RuntimeException(desc);
                }

                S.release();
            }
        }

    }

    public String getStCreateName() {
        //if(stCreateWho==null) return "";

        //UserSessionView view = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stCreateWho);

        //return view.getStUserName();
        return stCreateName;
    }

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    public String getStAdminNotes() {
        return stAdminNotes;
    }

    public void setStAdminNotes(String stAdminNotes) {
        this.stAdminNotes = stAdminNotes;
    }

    public BigDecimal getDbNDPPN() {
        return dbNDPPN;
    }

    public void setDbNDPPN(BigDecimal dbNDPPN) {
        this.dbNDPPN = dbNDPPN;
    }

    public int getPeriodLengthYears() {
        Date policyDateStart = getDtPeriodStart();
        Date policyDateEnd = getDtPeriodEnd();

        DateTime startDate = new DateTime(policyDateStart);
        DateTime endDate = new DateTime(policyDateEnd);
        Years y = Years.yearsBetween(startDate, endDate);
        int years = y.getYears();

        return years;
    }

    public String getStManualReinsuranceFlag() {
        return stManualReinsuranceFlag;
    }

    public void setStManualReinsuranceFlag(String stManualReinsuranceFlag) {
        this.stManualReinsuranceFlag = stManualReinsuranceFlag;
    }

    public boolean isManualReinsuranceFlag() {
        return Tools.isYes(stManualReinsuranceFlag);
    }

    public boolean isFireEndorseNeedManualReinsurance() {
        final int length = getPeriodLengthYears();

        if (!stPolicyTypeGroupID.equalsIgnoreCase("1")) {
            return false;
        }
        if (!isStatusEndorse()) {
            return false;
        }

        boolean isLongPolice = false;
        if (length > 1) {
            isLongPolice = true;
        }
        return isLongPolice;
    }

    public String getStParentPolicyNo() {
        return stParentPolicyNo;
    }

    public void setStParentPolicyNo(String stParentPolicyNo) {
        this.stParentPolicyNo = stParentPolicyNo;
    }

    public String getStRenewalCounter() {
        return stRenewalCounter;
    }

    public void setStRenewalCounter(String stRenewalCounter) {
        this.stRenewalCounter = stRenewalCounter;
    }

    public String calculateRenewalCounter(String counter) {
        if (counter == null) {
            counter = "0";
        }

        return String.valueOf(Integer.parseInt(counter) + 1);
    }

    public UserSessionView getCreateUser() {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stCreateWho);
    }

    public BigDecimal getDbClaimAdvancePaymentAmount() {
        return dbClaimAdvancePaymentAmount;
    }

    public void setDbClaimAdvancePaymentAmount(BigDecimal dbClaimAdvancePaymentAmount) {
        this.dbClaimAdvancePaymentAmount = dbClaimAdvancePaymentAmount;
    }

    public boolean isCoMember() {
        return getStCoverTypeCode().equalsIgnoreCase("COINSIN");
    }

    public boolean isCoinsurance() {
        return getStCoverTypeCode().equalsIgnoreCase("COINSOUT");
    }

    public String getStReference2Desc() {
        return stReference2Desc;
    }

    public void setStReference2Desc(String stReference2Desc) {
        this.stReference2Desc = stReference2Desc;
    }

    public void setStApprovedName(String stApprovedName) {
        this.stApprovedName = stApprovedName;
    }

    public void setStApprovedReinsName(String stApprovedReinsName) {
        this.stApprovedReinsName = stApprovedReinsName;
    }

    public boolean isEditReasOnlyMode() {
        return editReasOnlyMode;
    }

    public void setEditReasOnlyMode(boolean editReasOnlyMode) {
        this.editReasOnlyMode = editReasOnlyMode;
    }

    public void setPrincipal() {
        if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND) || getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)) {
            InsurancePolicyObjDefaultView objc = (InsurancePolicyObjDefaultView) getObjects().get(0);
            if (objc.getStReference1() != null) {
                setStReference5(objc.getStReference1()); //nama principal
                setStReference7(objc.getStReference2());//id principal
                setStReference8(objc.getStReference3());//alamat principal
            }
        }
    }

    public String getStClaimObjectParentID() {
        return stClaimObjectParentID;
    }

    public void setStClaimObjectParentID(String stClaimObjectParentID) {
        this.stClaimObjectParentID = stClaimObjectParentID;
    }

    public boolean isStatusInward() {
        return FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isRecalculateTreaty() {
        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal()
                || isStatusHistory() || isStatusEndorseRI() || isStatusEndorseIntern()
                || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()
                || isStatusClaimInward()) {
            return true;
        }

        return false;
    }

    public BigDecimal getClaimLimit() throws Exception {

        BigDecimal claimLeader = BDUtil.sub(getHoldingCoin().getDbClaimAmount(), getDbClaimDeductionAmount());

        return claimLeader;

    }

    public boolean isDirect() {
        return getStCoverTypeCode().equalsIgnoreCase("DIRECT");
    }

    public DTOList getClausules2() {
        loadClausules2();
        return clausules2;
    }

    public void loadClausules2() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (clausules2 == null && stPolicyTypeID != null) {
                clausules2 = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      a.*,b.description,b.description_new,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default "
                        + "   from "
                        + "      ins_clausules b "
                        + "      left join ins_pol_clausules a on "
                        + "         a.ins_clause_id = b.ins_clause_id"
                        + "         and a.pol_id = ? "
                        + "         and a.ins_pol_obj_id is null"
                        + "   where b.pol_type_id = ? and (cc_code is null or cc_code=?) and b.active_flag = 'Y' and b.f_default = 'Y' "
                        + "   order by b.orderseq, b.shortdesc",
                        new Object[]{stPolicyID, stPolicyTypeID, stCostCenterCode},
                        InsurancePolicyClausulesView.class);

                String ent_name = "";
                if (getEntity() != null) {
                    ent_name = getEntity().getStEntityName();
                }

                for (int i = 0; i < clausules2.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules2.get(i);

                    icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

                    if (icl.getStPolicyID() != null) {
                        icl.select();
                    } else {
                        icl.setDbRate(icl.getDbRateDefault());
                        icl.markNew();
                    }

                    icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
                }

                if (isNew()) {
                    for (int i = 0; i < clausules2.size(); i++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules2.get(i);

                        icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

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

    public void setClausules2(DTOList clausules2) {
        this.clausules2 = clausules2;
    }

    public String getStClientIP() {
        return stClientIP;
    }

    public void setStClientIP(String stClientIP) {
        this.stClientIP = stClientIP;
    }

    public String getStPassword() {
        return stPassword;
    }

    public void setStPassword(String stPassword) {
        this.stPassword = stPassword;
    }

    public BigDecimal getDbNDFeeBase1() {
        return dbNDFeeBase1;
    }

    public void setDbNDFeeBase1(BigDecimal dbNDFeeBase1) {
        this.dbNDFeeBase1 = dbNDFeeBase1;
    }

    public BigDecimal getDbNDFeeBase2() {
        return dbNDFeeBase2;
    }

    public void setDbNDFeeBase2(BigDecimal dbNDFeeBase2) {
        this.dbNDFeeBase2 = dbNDFeeBase2;
    }

    public DTOList getItems(String stPolicyID) {
        try {
            if (items == null) {
                items = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_items where pol_id = ? and item_class='PRM' and tax_code is not null",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public void setItems(DTOList items) {
        this.items = items;
    }

    public String getStReadyToApproveFlag() {
        return stReadyToApproveFlag;
    }

    public void setStReadyToApproveFlag(String stReadyToApproveFlag) {
        this.stReadyToApproveFlag = stReadyToApproveFlag;
    }

    public boolean isReadyToApprove() {
        return Tools.isYes(stReadyToApproveFlag);
    }

    public boolean isShouldValidateApproval() {
        boolean shouldValidate = (isStatusPolicy() || isStatusEndorse() || isStatusClaimDLA()
                || isStatusRenewal());

        return shouldValidate;
    }

    public BigDecimal getDbTotalFeeBase() {
        return BDUtil.add(dbNDFeeBase1, dbNDFeeBase2);
    }

    public BigDecimal getDbCoinsTSI(String stEntityID) throws Exception {
        final SQLUtil S = new SQLUtil();
        BigDecimal totalTSI = null;
        try {

            final PreparedStatement PS = S.setQuery("select sum(insured_amount) as total_tsi"
                    + " from ins_pol_obj "
                    + " where pol_id = ? and ref8 = ? ");

            PS.setString(1, this.getStPolicyID());
            PS.setString(2, stEntityID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                totalTSI = RS.getBigDecimal("total_tsi");
            }

            return totalTSI;

        } finally {
            S.release();
        }
    }

    public BigDecimal getDbCoinsDetailsSize(String stEntityID) throws Exception {
        final SQLUtil S = new SQLUtil();
        BigDecimal totalTSI = null;
        try {

            final PreparedStatement PS = S.setQuery("select count(ins_pol_obj_id) as jumlah"
                    + " from ins_pol_obj "
                    + " where pol_id = ? and ref8 = ? ");

            PS.setString(1, this.getStPolicyID());
            PS.setString(2, stEntityID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                totalTSI = RS.getBigDecimal("jumlah");
            }

            return totalTSI;

        } finally {
            S.release();
        }
    }

    public BigDecimal getTransactionLimitPerRiskCode(String cat, String cat2, String userID, String riskCode) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      max(c.refn" + cat2 + ") "
                    + "   from "
                    + "         s_user_roles b "
                    + "         inner join ff_table c on c.fft_group_id='CAPA' and c.ref1=b.role_id and c.ref2=? and c.ref3=?"
                    + "   where"
                    + " c.active_flag = 'Y' "
                    + " and b.user_id=? and c.ref4 = ?"
                    + " and period_start <= ? and period_end >= ?");

            S.setParam(1, cat);
            S.setParam(2, getStPolicyTypeID());
            S.setParam(3, userID);
            S.setParam(4, riskCode);
            S.setParam(5, getDtPolicyDate());
            S.setParam(6, getDtPolicyDate());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }
    public DTOList reinsuranceReins;

    public DTOList getReinsuranceReins() {
        loadReinsuranceReins();
        return reinsuranceReins;
    }

    private void loadReinsuranceReins() {
        try {
            if (reinsuranceReins == null) {
                reinsuranceReins = ListUtil.getDTOListFromQuery(
                        " select cust_name,saham,member_ent_id,ricomm_rate,"
                        + " sum(getpremi2(ins_cover_id1=139,insured)) as tsi_cover1,"
                        + " sum(getpremi2(ins_cover_id1=139,premi_rate)) as rate_cover1,"
                        + " sum(getpremi2(ins_cover_id1=139,premi_amt)) as premi_cover1,"
                        + " sum(getpremi2(ins_cover_id1=139,komisi)) as premi_amount,"
                        + " sum(getpremi2(ins_cover_id1=143,insured)) as tsi_cover2,"
                        + " sum(getpremi2(ins_cover_id1=143,premi_rate)) as rate_cover2,"
                        + " sum(getpremi2(ins_cover_id1=143,premi_amt)) as premi_cover2,"
                        + " sum(getpremi2(ins_cover_id1=143,komisi)) as premi_rate "
                        + " from (  select f.ent_name as cust_name,f.ent_id as member_ent_id,e.sharepct as saham,e.ins_cover_id1,e.premi_rate as premi_rate,"
                        + " coalesce(sum(e.tsi_amount*a.ccy_rate),0) as insured,coalesce(sum(e.premi_amount*a.ccy_rate),0) as premi_amt,e.ricomm_rate, "
                        + " coalesce(sum(e.ricomm_amt*a.ccy_rate),0) as komisi"
                        + " from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id"
                        + " inner join ins_pol_treaty c on c.ins_pol_obj_id = b.ins_pol_obj_id"
                        + " inner join ins_pol_treaty_detail d on d.ins_pol_treaty_id = c.ins_pol_treaty_id"
                        + " inner join ins_pol_ri e on e.ins_pol_tre_det_id = d.ins_pol_tre_det_id"
                        + " inner join ins_policy_types h on h.pol_type_id = a.pol_type_id"
                        + " inner join ent_master f on f.ent_id = e.member_ent_id"
                        + " inner join ins_treaty_detail g on g.ins_treaty_detail_id = e.ins_treaty_detail_id"
                        + " where a.status in ('ENDORSE','POLICY','RENEWAL','ENDORSE RI') and a.effective_flag='Y'"
                        + " and g.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')"
                        + " and a.pol_id = ? "
                        + " group by f.ent_id,f.ent_name,e.sharepct,e.ricomm_rate,e.ins_cover_id1,e.premi_rate"
                        + " ) x group by cust_name,saham,ricomm_rate,member_ent_id,ricomm_rate order by cust_name ",
                        new Object[]{stPolicyID},
                        InsurancePolicyReinsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setReinsuranceReins(DTOList reinsuranceReins) {
        this.reinsuranceReins = reinsuranceReins;
    }
    private DTOList coverageRekap;

    public DTOList getCoverageRecap() {
        loadCoverageRecap();
        return coverageRekap;
    }

    public void setCoverageRecap(DTOList coverageRekap) {
        this.coverageRekap = coverageRekap;
    }

    public void loadCoverageRecap() {
        try {
            if (coverageRekap == null) {
                coverageRekap = ListUtil.getDTOListFromQuery(
                        "select sum(insured_amount) as insured_amount,sum(premi) as premi "
                        + "from ins_pol_cover "
                        + "where pol_id  = ? "
                        + "group by insured_amount",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoverView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStCoLeaderID() {
        return stCoLeaderID;
    }

    public void setStCoLeaderID(String stCoLeaderID) {
        this.stCoLeaderID = stCoLeaderID;
    }

    public void setCustomStringToPolicy(InsuranceSplitPolicyObjDefaultView objc) {
        if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND) || getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)) {
            if (!getStPolicyTypeID().equalsIgnoreCase("59")) {
                if (objc.getStReference1() != null) {
                    setStReference5(objc.getStReference1()); //nama principal
                    setStReference7(objc.getStReference2());//id principal
                    setStReference8(objc.getStReference3());//alamat principal
                }
            }
        }

        if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND) || getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)) {
            if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND)) {
                if (!getStPolicyTypeID().equalsIgnoreCase("59")) {
                    if (objc.getStReference11() != null) {
                        setStReference10(objc.getStReference11());
                    }
                }
                if (getStPolicyTypeID().equalsIgnoreCase("52")) {
                    if (objc.getStReference12() != null) {
                        setStReference10(objc.getStReference12());
                    }
                }
            }

            if (getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)) {
                if (objc.getStReference6() != null) {
                    setStReference10(objc.getStReference6());
                }
            }
        }

        if (getStPolicyTypeID().equalsIgnoreCase("60")) {
            setStReference5(objc.getStReference3Desc());
            setStReference10(objc.getStReference19());
        }

        if (getStPolicyTypeID().equalsIgnoreCase("1") || getStPolicyTypeID().equalsIgnoreCase("81")) {
            if (getObjects().size() == 1) {
                if (!isStatusEndorse()) {
                    //objc.setDtReference1(getDtPeriodStart());
                    //objc.setDtReference2(getDtPeriodEnd());
                }
            }
        }
    }

    public void validateAkumulasiResikoLimit(BigDecimal transactionLimit) throws Exception {

        if (!getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE)
                && !getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND)) {
            return;
        }

        if (getStPolicyTypeID().equalsIgnoreCase("59")) {
            return;
        }

        final DTOList object = getObjects();

        for (int i = 0; i < object.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

            InsurancePolicyObjDefaultView object2 = (InsurancePolicyObjDefaultView) obj;

            final BigDecimal akumulasiResikoPerPrincipal = getAkumulasiResikoPrincipal(object2.getStReference2(), getStPolicyTypeGroupID());

            boolean enoughLimit = false;

            enoughLimit = Tools.compare(transactionLimit, akumulasiResikoPerPrincipal) >= 0;

            if (!enoughLimit) {
                stUnderwritingFinishFlag = "N";
                stEffectiveFlag = "N";
//                getRemoteInsurance().saveInputPaymentDate(this, approvalMode);
                throw new RuntimeException("Nilai Akumulasi Resiko Principal " + object2.getStReference1() + " (" + akumulasiResikoPerPrincipal + ") Melebihi Limit Kewenangan Anda (" + transactionLimit + ")");
            }
        }
    }

    public BigDecimal getAkumulasiResikoPrincipal(String stPrincipalID, String stPolicyTypeGroupID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      amount "
                    + "   from "
                    + "         ins_pol_risk_accumulation "
                    + "   where"
                    + "  entity_id = ? and pol_type_group_id = ? ");

            S.setParam(1, stPrincipalID);
            S.setParam(2, stPolicyTypeGroupID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getDbCoinsDetailsSize2(String stEntityID) throws Exception {
        final SQLUtil S = new SQLUtil();
        BigDecimal totalTSI = null;
        try {

            final PreparedStatement PS = S.setQuery("select count(ins_pol_obj_id) as jumlah"
                    + " from ins_pol_obj "
                    + " where pol_id = ? and ref8 = ? and refn2 <> 0");

            PS.setString(1, this.getStPolicyID());
            PS.setString(2, stEntityID);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                totalTSI = RS.getBigDecimal("jumlah");
            }

            return totalTSI;

        } finally {
            S.release();
        }
    }

    public boolean isEffectiveReinsurance() {
        return Tools.isYes(stRIFinishFlag);
    }

    public String getStRIPostedFlag() {
        return stRIPostedFlag;
    }

    public void setStRIPostedFlag(String stRIPostedFlag) {
        this.stRIPostedFlag = stRIPostedFlag;
    }

    public BigDecimal getDbClaimBPDAN() {
        return dbClaimBPDAN;
    }

    public void setDbClaimBPDAN(BigDecimal dbClaimBPDAN) {
        this.dbClaimBPDAN = dbClaimBPDAN;
    }

    public BigDecimal getDbClaimFAC() {
        return dbClaimFAC;
    }

    public void setDbClaimFAC(BigDecimal dbClaimFAC) {
        this.dbClaimFAC = dbClaimFAC;
    }

    public BigDecimal getDbClaimFACO() {
        return dbClaimFACO;
    }

    public void setDbClaimFACO(BigDecimal dbClaimFACO) {
        this.dbClaimFACO = dbClaimFACO;
    }

    public BigDecimal getDbClaimOR() {
        return dbClaimOR;
    }

    public void setDbClaimOR(BigDecimal dbClaimOR) {
        this.dbClaimOR = dbClaimOR;
    }

    public BigDecimal getDbClaimPARK() {
        return dbClaimPARK;
    }

    public void setDbClaimPARK(BigDecimal dbClaimPARK) {
        this.dbClaimPARK = dbClaimPARK;
    }

    public BigDecimal getDbClaimQS() {
        return dbClaimQS;
    }

    public void setDbClaimQS(BigDecimal dbClaimQS) {
        this.dbClaimQS = dbClaimQS;
    }

    public BigDecimal getDbClaimSPL() {
        return dbClaimSPL;
    }

    public void setDbClaimSPL(BigDecimal dbClaimSPL) {
        this.dbClaimSPL = dbClaimSPL;
    }

    public BigDecimal getDbClaimXOL1() {
        return dbClaimXOL1;
    }

    public void setDbClaimXOL1(BigDecimal dbClaimXOL1) {
        this.dbClaimXOL1 = dbClaimXOL1;
    }

    public BigDecimal getDbCommXOL1() {
        return dbCommXOL1;
    }

    public void setDbCommXOL1(BigDecimal dbCommXOL1) {
        this.dbCommXOL1 = dbCommXOL1;
    }

    public BigDecimal getDbDanaReas() {
        return dbDanaReas;
    }

    public void setDbDanaReas(BigDecimal dbDanaReas) {
        this.dbDanaReas = dbDanaReas;
    }

    public void generatePolicyNoFromPersetujuanPrinsip() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        EntityView entity = getEntity();

        if (getStPolicyTypeGroupID().equalsIgnoreCase("7") && !getStPolicyTypeID().equalsIgnoreCase("59")) {
            final DTOList obj = getObjects();
            InsurancePolicyObjDefaultView object = (InsurancePolicyObjDefaultView) obj.get(0);
            entity = getEntity2(object.getStReference2());
        }

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        if (stReference1 == null) {
            throw new Exception("Nomor persetujuan prinsip masih kosong");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        //  045824020213000300
        //    5824021212015000
        //  012345678901234567

        stPolicyNo =
                coasCode + // A
                custCategory + // B
                stReference1;
    }

    /**
     * @return the stSignCode
     */
    public String getStSignCode() {
        return stSignCode;
    }

    /**
     * @param stSignCode the stSignCode to set
     */
    public void setStSignCode(String stSignCode) {
        this.stSignCode = stSignCode;
    }

    public boolean sudahAdaNoPolisFromPP(String pp) throws Exception {

        SQLUtil S = new SQLUtil();
        boolean sudahAda = false;

        try {
            PreparedStatement PS = S.setQuery("select pol_no "
                    + " from ins_policy "
                    + " where status in ('POLICY','RENEWAL') "
                    + " and pol_no = ? "
                    + " limit 1");

            PS.setString(1, getPolicyNoFromPersetujuanPrinsip());

            ResultSet RS = PS.executeQuery();
            if (RS.next()) {
                sudahAda = true;
            }

        } finally {
            S.release();
        }

        return sudahAda;
    }

    public DTOList getReportClausules() {
        loadReportClausules();
        return clausules;
    }

    public void loadReportClausules() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (clausules == null && stPolicyTypeID != null) {
                clausules = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      a.*,b.description,b.description_new,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default,b.child_clausules  "
                        + "   from "
                        + "      ins_clausules b "
                        + "      left join ins_pol_clausules a on "
                        + "         a.ins_clause_id = b.ins_clause_id"
                        + "         and a.pol_id = ? "
                        + "         and a.ins_pol_obj_id is null"
                        + "   where b.pol_type_id = ? and (cc_code = ? or cc_code is null) "
                        + "   order by b.orderseq, b.shortdesc ",
                        new Object[]{stPolicyID, stPolicyTypeID, stCostCenterCode},
                        InsurancePolicyClausulesView.class);

                String ent_name = "";
                if (getEntity() != null) {
                    ent_name = getEntity().getStEntityName();
                }

                for (int i = 0; i < clausules.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                    icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

                    if (icl.getStPolicyID() != null) {
                        icl.select();
                    } else {
                        icl.setDbRate(icl.getDbRateDefault());
                        icl.markNew();
                    }

                    icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
                }

                if (isNew()) {
                    for (int i = 0; i < clausules.size(); i++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                        icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

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

    public void invokeCustomHandlersPolicy(boolean validate) {
        if (objects.size() > 0) {
            InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(0);

            //CustomHandlerManager.getInstance().onCalculatePolicy(this, obj, validate);
        }

    }

    /**
     * @return the stReceiptNo
     */
    public String getStReceiptNo() {
        return stReceiptNo;
    }

    /**
     * @param stReceiptNo the stReceiptNo to set
     */
    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public String getEncryptedApprovedWho() throws Exception {
        return Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes(getStApprovedWho())));
    }

    /**
     * @return the stPrintCounter
     */
    public String getStPrintCounter() {
        return stPrintCounter;
    }

    /**
     * @param stPrintCounter the stPrintCounter to set
     */
    public void setStPrintCounter(String stPrintCounter) {
        this.stPrintCounter = stPrintCounter;
    }

    public String getUserIDSign() {
        String sub_cccode = getCostCenter(getStCostCenterCode()).getStSubCostCenterCode();

        String NIPPimpinan = Parameter.readString("BRANCH_" + sub_cccode);

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        UserSessionView user = getUser(stApprovedWho);

        String useridSign = NIPPimpinan;

        if (!NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
            if (user.getStBranch() != null) {
                if (user.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                    if (user.hasSignAuthority()) {
                        useridSign = user.getStUserID();
                    }
                }
            }
        }

        return useridSign;
    }

    public String getParaf() {

        String NIPPimpinan = Parameter.readString("BRANCH_WAKIL_ID_" + getStCostCenterCode()); //04740297
        String NIPWakil = null;

        //Parameter.readString("BRANCH_" + getStCostCenterCode());
        //Parameter.readString("BRANCH_WAKIL_ID_"+ getStCostCenterCode());

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        String useridSign = null;

        if (getUser(stApprovedWho).getDtInActiveDate() != null) {
            NIPWakil = NIPPimpinan;
        } else {
            NIPWakil = stApprovedWho;//13111311
        }

        if (NIPWakil != null) {
            UserSessionView userWakil = getUser(NIPWakil);

            if (NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
                if (userWakil.getStBranch() != null) {
                    if (userWakil.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                        if (userWakil.getStParaf() != null) {
                            useridSign = userWakil.getStUserID();
                        }
                    }
                }
            }

            if (!NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
                if (userWakil.getStBranch() != null) {
                    if (userWakil.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                        if (!userWakil.hasSignAuthority()) {
                            useridSign = userWakil.getStUserID();
                        }
                    }
                }
            }
        }

        return useridSign;
    }

    public String getUserIDSignName() {

        String sub_cccode = getCostCenter(getStCostCenterCode()).getStSubCostCenterCode();

        String NIPPimpinan = Parameter.readString("BRANCH_" + sub_cccode);

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        UserSessionView user = getUser(stApprovedWho);
        UserSessionView userPimpinan = getUser(NIPPimpinan);

        String useridSignName = userPimpinan.getStUserName();


        if (!NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
            if (user.getStBranch() != null) {
                if (user.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                    if (user.hasSignAuthority()) {
                        useridSignName = user.getStUserName();
                    }
                }
            }
        }

        return useridSignName.toUpperCase();
    }

    public BigDecimal getDbAPSFeeP() {
        return dbAPSFeeP;
    }

    public void setDbAPSFeeP(BigDecimal dbAPSFeeP) {
        this.dbAPSFeeP = dbAPSFeeP;
    }

    public BigDecimal getDbAPPCostP() {
        return dbAPPCostP;
    }

    public void setDbAPPCostP(BigDecimal dbAPPCostP) {
        this.dbAPPCostP = dbAPPCostP;
    }

    public BigDecimal getDbAPBFeeP() {
        return dbAPBFeeP;
    }

    public void setDbAPBFeeP(BigDecimal dbAPBFeeP) {
        this.dbAPBFeeP = dbAPBFeeP;
    }

    public BigDecimal getDbAPHFeeP() {
        return dbAPHFeeP;
    }

    public void setDbAPHFeeP(BigDecimal dbAPHFeeP) {
        this.dbAPHFeeP = dbAPHFeeP;
    }

    public BigDecimal getDbAPFBaseP() {
        return dbAPFBaseP;
    }

    public void setDbAPFBaseP(BigDecimal dbAPFBaseP) {
        this.dbAPFBaseP = dbAPFBaseP;
    }

    public BigDecimal getDbAPDiscP() {
        return dbAPDiscP;
    }

    public void setDbAPDiscP(BigDecimal dbAPDiscP) {
        this.dbAPDiscP = dbAPDiscP;
    }

    public Date getDtReceiptDate() {
        return dtReceiptDate;
    }

    public void setDtReceiptDate(Date dtReceiptDate) {
        this.dtReceiptDate = dtReceiptDate;
    }

    public void checkDetailsLimit() throws Exception {
        final DTOList detail = getDetails();

        for (int i = 0; i < detail.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) detail.get(i);

            if (!item.isComission()) {
                continue;
            }

            BigDecimal limit = getComissionItemLimit(item);

            if (!BDUtil.isZeroOrNull(limit)) {
                if (BDUtil.biggerThan(item.getDbRate(), limit)) {
                    throw new RuntimeException("Rate " + item.getInsItem().getStDescription() + " " + item.getDbRate() + " Lebih besar dari limit " + limit);
                }
            }
        }
    }

    public BigDecimal getComissionItemLimit(InsurancePolicyItemsView item) throws Exception {
        // ref1 = cabang
        //ref2 = region
        //ref3 = jenis asuransi
        //ref4 = ins item group
        // ref5 = group company

        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "    select * "
                + "  from ff_table  "
                + "   where ref1 = ? and ref2 = ? and ref3 = ? "
                + "   and ref4 = ? and (ref5 = ? or ref5 is null) "
                + "   and fft_group_id='COMM_ITEM' and active_flag = 'Y' "
                + "   order by ref5::bigint "
                + "   limit 1",
                new Object[]{getStCostCenterCode(), getStRegionID(), getStPolicyTypeID(), item.getInsItem().getStItemGroup(), getEntity().getStRef2()},
                FlexTableView.class).getDTO();

        return ft == null ? null : ft.getDbReference1();
    }

    public DTOList getObjectsByID(String stObjID) {
        loadObjectsClaimByID(stObjID);
        return objectsClaim;
    }

    public void loadObjectsClaimByID(String stObjID) {
        try {
            getClObjectClass();
            if (objectsClaim == null) {
                objectsClaim = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where ins_pol_obj_id  = ? ",
                        new Object[]{stObjID},
                        clObjectClass);

                for (int i = 0; i < objectsClaim.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objectsClaim.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStCommEntityID1() {
        return stCommEntityID1;
    }

    public void setStCommEntityID1(String stCommEntityID1) {
        this.stCommEntityID1 = stCommEntityID1;
    }

    public String getStCommEntityID2() {
        return stCommEntityID2;
    }

    public void setStCommEntityID2(String stCommEntityID2) {
        this.stCommEntityID2 = stCommEntityID2;
    }

    public String getStCommEntityID3() {
        return stCommEntityID3;
    }

    public void setStCommEntityID3(String stCommEntityID3) {
        this.stCommEntityID3 = stCommEntityID3;
    }

    public String getStCommEntityID4() {
        return stCommEntityID4;
    }

    public void setStCommEntityID4(String stCommEntityID4) {
        this.stCommEntityID4 = stCommEntityID4;
    }

    public String getStBfeeEntityID1() {
        return stBfeeEntityID1;
    }

    public void setStBfeeEntityID1(String stBfeeEntityID1) {
        this.stBfeeEntityID1 = stBfeeEntityID1;
    }

    public String getStBfeeEntityID2() {
        return stBfeeEntityID2;
    }

    public void setStBfeeEntityID2(String stBfeeEntityID2) {
        this.stBfeeEntityID2 = stBfeeEntityID2;
    }

    public String getStFbaseEntityID1() {
        return stFbaseEntityID1;
    }

    public void setStFbaseEntityID1(String stFbaseEntityID1) {
        this.stFbaseEntityID1 = stFbaseEntityID1;
    }

    public String getStFbaseEntityID2() {
        return stFbaseEntityID2;
    }

    public void setStFbaseEntityID2(String stFbaseEntityID2) {
        this.stFbaseEntityID2 = stFbaseEntityID2;
    }

    public String getStHfeeEntityID1() {
        return stHfeeEntityID1;
    }

    public void setStHfeeEntityID1(String stHfeeEntityID1) {
        this.stHfeeEntityID1 = stHfeeEntityID1;
    }

    public BigDecimal getTotalCommPCT() {
        final DTOList detail = getDetails();

        BigDecimal totalKomisi = null;

        for (int i = 0; i < detail.size(); i++) {
            InsurancePolicyItemsView det = (InsurancePolicyItemsView) detail.get(i);

            if (det.isComission()) {
                totalKomisi = BDUtil.add(totalKomisi, det.getDbRate());
            }

        }

        return totalKomisi;
    }

    public EntityView getProducer() {

        if (entity != null) {
            if (!Tools.isEqual(entity.getStEntityID(), stProducerID)) {
                entity = null;
            }
        }

        if (entity == null) {
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stProducerID);
        }

        return entity;
    }

    public void cekPeriodeAwal() {
        int periodeAwal = Integer.parseInt(DateUtil.getYear(getDtPeriodStart()));
        int tanggalPolis = Integer.parseInt(DateUtil.getYear(getDtPolicyDate()));

        if (periodeAwal - tanggalPolis > 2) {
            throw new RuntimeException("Cek periode awal polis apakah sudah betul");
        }

        DateTime startDate = new DateTime(getDtPeriodStart());
        DateTime endDate = new DateTime(getDtPeriodEnd());
        DateTime polDate = new DateTime(getDtPolicyDate());

        Years y = Years.yearsBetween(startDate, endDate);
        int year = y.getYears();

        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("Tanggal periode akhir tidak boleh < periode awal");
        }

        if (!isStatusClaim() && !isStatusEndorse() && !isStatusHistory() && !isStatusClaimEndorse()) {
            if ((polDate.getYear() - startDate.getYear()) > 17) {
                throw new RuntimeException("Cek tanggal polis dan periode awal polis apakah sudah betul");
            }

            if (year > 30) {
                throw new RuntimeException("Cek periode awal & periode akhir polis apakah sudah betul");
            }
        }

        if (startDate.isEqual(endDate)) {
            if (!isAllowSamePeriod()) {
                throw new RuntimeException("Apakah periode awal & periode akhir polis boleh sama ? Jika ya, centang flag periode pertanggungan sama");
            }
        }

    }

    public void recalculateTreatyInitializePerObject(InsurancePolicyObjectView obj) throws Exception {
        if (getStCoverTypeCode() == null) {
            return;
        }
        if (getStPolicyTypeID() == null) {
            return;
        }

        try {
            int scale = 0;
            BigDecimal dbInsuranceCoinsScale = null;

            if (!getStCurrencyCode().equalsIgnoreCase("IDR")) {
                scale = 2;
            }

            if (hasCoIns()) {
                final InsurancePolicyCoinsView holdingCoin = getHoldingCoin();
                dbInsuranceCoinsScale = getDbCoinsSessionPct();
            }

            //for (int i = 0; i < this.objects.size(); i++) {
            InsurancePolicyObjectView ipo = (InsurancePolicyObjectView) obj;

            //final BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();
            BigDecimal insuredAmount = ipo.getDbObjectInsuredAmountShare();

            final BigDecimal tlr = ipo.getDbTreatyLimitRatio();

            BigDecimal tlrMaipark = null;
            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {
                tlrMaipark = ipo.getDbTreatyLimitRatioMaipark();
            }

            final DTOList treaties = ipo.getTreaties();

            if (treaties.size() < 1) {
                final InsurancePolicyTreatyView tre = new InsurancePolicyTreatyView();

                tre.markNew();
                tre.setStInsuranceTreatyID(ipo.getStInsuranceTreatyID());
                tre.setStPolicyID(stPolicyID);
                treaties.add(tre);
            }

            final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
            tre.setObject(ipo);

            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)) {

                if (isStatusEndorse()) {
                    if (BDUtil.isZeroOrNull(insuredAmount)) {
                        insuredAmount = BDUtil.negate(ipo.getDbObjectInsuredAmountShareEndorse());
                    }
                }

//                tre.adjustRatioEarthquake(this, tlrMaipark, tlr, dbCurrencyRateTreaty, getStCurrencyCode());
                tre.raiseToTSIEarthquake(getStPolicyTypeID(), insuredAmount, tlr, tlrMaipark, dbCurrencyRateTreaty, getStCurrencyCode());
            } else if (stPolicyTypeGroupID.equalsIgnoreCase("6")) {
                invokeTreatyCustomHandlers(ipo);
            } else {
                tre.adjustRatio(tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
                tre.raiseToTSI(getStPolicyTypeID(), insuredAmount, tlr, dbCurrencyRateTreaty, getStCurrencyCode(), null);
            }

            if (hasCoIns()) {
                ipo.setDbObjectPremiTotalBeforeCoinsuranceAmount(ipo.getDbObjectPremiTotalAmount());
                ipo.setDbObjectPremiTotalAmount(BDUtil.mul(ipo.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()), scale));
            }

            invokeTreatyCustomHandlers(ipo);
            tre.setStRateMethod(getStRateMethod());
            tre.calculate(ipo, insuredAmount, ipo.getDbObjectPremiTotalAmount(), stPolicyTypeGroupID, dbCurrencyRate, dbInsuranceCoinsScale);

            //}
        } catch (RuntimeException e) {
            setStInsuranceTreatyID(null);
            setStCoverTypeCode(null);
            throw e;
        }

    }
    private BigDecimal totalKomisiTanpaPajak;

    /**
     * @return the totalKomisiTanpaPajak
     */
    public BigDecimal getTotalKomisiTanpaPajak() {
        return totalKomisiTanpaPajak;
    }

    /**
     * @param totalKomisiTanpaPajak the totalKomisiTanpaPajak to set
     */
    public void setTotalKomisiTanpaPajak(BigDecimal totalKomisiTanpaPajak) {
        this.totalKomisiTanpaPajak = totalKomisiTanpaPajak;
    }

    public BigDecimal getTotalCommPCTInduk() {
        final DTOList detail = getDetailsInduk();

        BigDecimal totalKomisi = null;

        for (int i = 0; i < detail.size(); i++) {
            InsurancePolicyItemsView det = (InsurancePolicyItemsView) detail.get(i);

            if (det.isComission()) {
                totalKomisi = BDUtil.add(totalKomisi, det.getDbRate());
            }

        }

        return totalKomisi;
    }
    private DTOList detailsInduk;

    public DTOList getDetailsInduk() {
        loadDetailsInduk();
        return detailsInduk;
    }

    public void setDetailsInduk(DTOList detailsInduk) {
        this.detailsInduk = detailsInduk;
    }

    public void loadDetailsInduk() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (detailsInduk == null) {
                if (getStPolicyNo() != null) {
                    /*
                    detailsInduk = ListUtil.getDTOListFromQuery(
                    "select b.* from ins_policy a inner join ins_pol_items b on a.pol_id = b.pol_id "+
                    " where substr(pol_no, 0, 17) = ?  and item_class='PRM'  and status in ('POLICY','RENEWAL')",
                    new Object[]{getStPolicyNo().length()>16?getStPolicyNo().substring(0, 16):getStPolicyNo()},
                    InsurancePolicyItemsView.class
                    );*/
                    detailsInduk = ListUtil.getDTOListFromQuery(
                            "select b.* from ins_policy a inner join ins_pol_items b on a.pol_id = b.pol_id "
                            + " where a.pol_id = ?  and item_class='PRM'  and status in ('POLICY','RENEWAL','ENDORSE')",
                            new Object[]{getStPolicyID()},
                            InsurancePolicyItemsView.class);
                } else {
                    detailsInduk = ListUtil.getDTOListFromQuery(
                            "select b.* from ins_policy a inner join ins_pol_items b on a.pol_id = b.pol_id "
                            + " where a.pol_id = ?  and item_class='PRM'",
                            new Object[]{getStPolicyID()},
                            InsurancePolicyItemsView.class);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private DTOList parentpolicy3;

    public DTOList getParentPolicy3() {
        loadParentPolicy3();
        return parentpolicy3;
    }

    public void loadParentPolicy3() {
        try {
            if (parentpolicy3 == null) {
                parentpolicy3 = ListUtil.getDTOListFromQuery(
                        "select a.policy_date,a.pol_no as policy_id,c.ref_ent_id as entity_id, "
                        + "getpremiend(b.entity_id,a.insured_amount*a.ccy_rate,0) as insured_amount, "
                        + "getpremiend(b.entity_id,a.nd_pcost*a.ccy_rate,0) as refn1, "
                        + "getpremiend(b.entity_id,a.nd_sfee*a.ccy_rate,0) as refn2, "
                        + "getpremiend(b.entity_id,a.premi_total*a.ccy_rate,b.premi_amt*a.ccy_rate*-1) as premi_total "
                        + "from ins_policy a "
                        + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                        + "left join ent_master c on c.ent_id = b.entity_id "
                        + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.ref2 = ? "
                        + "order by a.policy_date,a.pol_no,c.ref_ent_id",
                        new Object[]{stReference2},
                        InsurancePolicyObjDefaultView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParentPolicy3(DTOList parentpolicy3) {
        this.parentpolicy3 = parentpolicy3;
    }

    /**
     * @return the dbTsiJP
     */
    public BigDecimal getDbTsiJP() {
        return dbTsiJP;
    }

    /**
     * @param dbTsiJP the dbTsiJP to set
     */
    public void setDbTsiJP(BigDecimal dbTsiJP) {
        this.dbTsiJP = dbTsiJP;
    }

    /**
     * @return the dbPremiJP
     */
    public BigDecimal getDbPremiJP() {
        return dbPremiJP;
    }

    /**
     * @param dbPremiJP the dbPremiJP to set
     */
    public void setDbPremiJP(BigDecimal dbPremiJP) {
        this.dbPremiJP = dbPremiJP;
    }

    /**
     * @return the dbCommJP
     */
    public BigDecimal getDbCommJP() {
        return dbCommJP;
    }

    /**
     * @param dbCommJP the dbCommJP to set
     */
    public void setDbCommJP(BigDecimal dbCommJP) {
        this.dbCommJP = dbCommJP;
    }

    /**
     * @return the dbTsiQSKR
     */
    public BigDecimal getDbTsiQSKR() {
        return dbTsiQSKR;
    }

    /**
     * @param dbTsiQSKR the dbTsiQSKR to set
     */
    public void setDbTsiQSKR(BigDecimal dbTsiQSKR) {
        this.dbTsiQSKR = dbTsiQSKR;
    }

    /**
     * @return the dbPremiQSKR
     */
    public BigDecimal getDbPremiQSKR() {
        return dbPremiQSKR;
    }

    /**
     * @param dbPremiQSKR the dbPremiQSKR to set
     */
    public void setDbPremiQSKR(BigDecimal dbPremiQSKR) {
        this.dbPremiQSKR = dbPremiQSKR;
    }

    /**
     * @return the dbCommQSKR
     */
    public BigDecimal getDbCommQSKR() {
        return dbCommQSKR;
    }

    /**
     * @param dbCommQSKR the dbCommQSKR to set
     */
    public void setDbCommQSKR(BigDecimal dbCommQSKR) {
        this.dbCommQSKR = dbCommQSKR;
    }

    /**
     * @return the dbClaimJP
     */
    public BigDecimal getDbClaimJP() {
        return dbClaimJP;
    }

    /**
     * @param dbClaimJP the dbClaimJP to set
     */
    public void setDbClaimJP(BigDecimal dbClaimJP) {
        this.dbClaimJP = dbClaimJP;
    }

    /**
     * @return the dbClaimQSKR
     */
    public BigDecimal getDbClaimQSKR() {
        return dbClaimQSKR;
    }

    /**
     * @param dbClaimQSKR the dbClaimQSKR to set
     */
    public void setDbClaimQSKR(BigDecimal dbClaimQSKR) {
        this.dbClaimQSKR = dbClaimQSKR;
    }

    /**
     * @return the dbTsiFACO1
     */
    public BigDecimal getDbTsiFACO1() {
        return dbTsiFACO1;
    }

    /**
     * @param dbTsiFACO1 the dbTsiFACO1 to set
     */
    public void setDbTsiFACO1(BigDecimal dbTsiFACO1) {
        this.dbTsiFACO1 = dbTsiFACO1;
    }

    /**
     * @return the dbPremiFACO1
     */
    public BigDecimal getDbPremiFACO1() {
        return dbPremiFACO1;
    }

    /**
     * @param dbPremiFACO1 the dbPremiFACO1 to set
     */
    public void setDbPremiFACO1(BigDecimal dbPremiFACO1) {
        this.dbPremiFACO1 = dbPremiFACO1;
    }

    /**
     * @return the dbCommFACO1
     */
    public BigDecimal getDbCommFACO1() {
        return dbCommFACO1;
    }

    /**
     * @param dbCommFACO1 the dbCommFACO1 to set
     */
    public void setDbCommFACO1(BigDecimal dbCommFACO1) {
        this.dbCommFACO1 = dbCommFACO1;
    }

    /**
     * @return the dbTsiFACO2
     */
    public BigDecimal getDbTsiFACO2() {
        return dbTsiFACO2;
    }

    /**
     * @param dbTsiFACO2 the dbTsiFACO2 to set
     */
    public void setDbTsiFACO2(BigDecimal dbTsiFACO2) {
        this.dbTsiFACO2 = dbTsiFACO2;
    }

    /**
     * @return the dbPremiFACO2
     */
    public BigDecimal getDbPremiFACO2() {
        return dbPremiFACO2;
    }

    /**
     * @param dbPremiFACO2 the dbPremiFACO2 to set
     */
    public void setDbPremiFACO2(BigDecimal dbPremiFACO2) {
        this.dbPremiFACO2 = dbPremiFACO2;
    }

    /**
     * @return the dbCommFACO2
     */
    public BigDecimal getDbCommFACO2() {
        return dbCommFACO2;
    }

    /**
     * @param dbCommFACO2 the dbCommFACO2 to set
     */
    public void setDbCommFACO2(BigDecimal dbCommFACO2) {
        this.dbCommFACO2 = dbCommFACO2;
    }

    /**
     * @return the dbTsiFACO3
     */
    public BigDecimal getDbTsiFACO3() {
        return dbTsiFACO3;
    }

    /**
     * @param dbTsiFACO3 the dbTsiFACO3 to set
     */
    public void setDbTsiFACO3(BigDecimal dbTsiFACO3) {
        this.dbTsiFACO3 = dbTsiFACO3;
    }

    /**
     * @return the dbPremiFACO3
     */
    public BigDecimal getDbPremiFACO3() {
        return dbPremiFACO3;
    }

    /**
     * @param dbPremiFACO3 the dbPremiFACO3 to set
     */
    public void setDbPremiFACO3(BigDecimal dbPremiFACO3) {
        this.dbPremiFACO3 = dbPremiFACO3;
    }

    /**
     * @return the dbCommFACO3
     */
    public BigDecimal getDbCommFACO3() {
        return dbCommFACO3;
    }

    /**
     * @param dbCommFACO3 the dbCommFACO3 to set
     */
    public void setDbCommFACO3(BigDecimal dbCommFACO3) {
        this.dbCommFACO3 = dbCommFACO3;
    }

    public void validateTreatyNotManual(boolean raiseErrors) {

        if (stRIFinishFlag != null) {
            if (Tools.isYes(stRIFinishFlag)) {
                if (stReinsuranceApprovedWho != null) {
                    return;
                }
            }
        }

        stRIFinishFlag = null;

        if (!(isStatusEndorse() || isStatusPolicy() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward())) {
            return;
        }

        DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objDefault = (InsurancePolicyObjDefaultView) objects.get(i);

            DTOList treatyDetails = obj.getTreatyDetails();

            BigDecimal premiRITotal = null;

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                boolean equalTSI = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                BigDecimal cekSelisih = BDUtil.sub(BDUtil.mul(trd.getDbMemberTreatyTSITotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbTSIAmount(), BDUtil.one, scale));

                if (BDUtil.lesserThanEqual(cekSelisih, BDUtil.one) && BDUtil.biggerThanEqual(cekSelisih, new BigDecimal(-1))) {
                    equalTSI = true;
                }

                boolean equalPremi = Tools.isEqual(BDUtil.mul(trd.getDbMemberTreatyPremiTotal(), BDUtil.one, scale), BDUtil.mul(trd.getDbPremiAmount(), BDUtil.one, scale));

                InsuranceTreatyDetailView tredet = trd.getTreatyDetail();

                if (!trd.getTreatyDetail().getTreatyType().isNonXOL()) {
                    premiRITotal = BDUtil.add(premiRITotal, trd.getDbMemberTreatyPremiTotal());
                }

                if (!tredet.isByPassValidationRI()) {
                    if (!equalTSI) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi TSI Reas " + trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() + " Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                        } else {
                            return;
                        }
                    }

                    if (!equalPremi) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi Premi Reas " + trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() + " Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Salah");
                        } else {
                            return;
                        }
                    }
                }


                DTOList shares = trd.getShares();

                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                    if (!ri.isApproved()) {
                        if (raiseErrors) {
                            throw new RuntimeException("Alokasi Reas Objek [" + (i + 1) + "] " + objDefault.getStReference1() + " Belum Disetujui");
                        } else {
                            return;
                        }
                    }

                    if (ri.getStApprovedFlag() == null) {
                        stRIFinishFlag = null;
                    }
                }
            }

            boolean equalPremiTotal = true;

            if (BDUtil.lesserThan(BDUtil.sub(obj.getDbObjectPremiTotalAmount(), premiRITotal), new BigDecimal(-1)) || BDUtil.biggerThan(BDUtil.sub(obj.getDbObjectPremiTotalAmount(), premiRITotal), BDUtil.one)) {
                equalPremiTotal = false;
            }
        }


        stRIFinishFlag = "Y";

    }

    public void validateNotManual(boolean isApproving) throws Exception {

        if (isApproving) {
            if (isStatusClaim() || isStatusClaimEndorse()) {
                if (isStatusClaimPLA()) {
                    if (getStPLANo() == null) {
                        throw new RuntimeException("No PLA/LKS Belum Diisi");
                    }

                    setStClaimStatus(FinCodec.ClaimStatus.PLA);
                    setStActiveFlag("Y");
                } else if (isStatusClaimDLA()) {
                    if (getStDLANo() == null) {
                        throw new RuntimeException("No DLA/LKP Belum Diisi");
                    }

                    setStEffectiveClaimFlag("Y");
                    setStEffectiveFlag("Y");
                    setStApprovedClaimFlag("Y");
                    setDtApprovedClaimDate(new Date());
                }
            }

            if (isStatusPolicy() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()) {
                validateTreatyNotManual(true);
            }
        }

        if (isStatusClaim() || isStatusClaimEndorse()) {
            if (isStatusClaimPLA()) {
                if (getStClaimObjectID() == null) {
                    throw new RuntimeException("Objek Klaim Belum Dipilih");
                }
            }
        }

        if (isModified()) {
            if (isStatusClaimDLA() || isStatusClaimEndorse()) {
                if (isStatusClaimDLA() || isStatusClaimEndorse()) {
                    if (dtDLADate != null) {

                        boolean withinCurrentMonth = DateUtil.getDateStr(getDtDLADate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                        if (!withinCurrentMonth) {
                            throw new RuntimeException("Tanggal LKP Tidak Valid (Sudah Tutup Produksi), Ubah Tanggal LKP menjadi Tanggal Sekarang (Bulan Produksi)");
                        }

                    }
                }
            } else if (isStatusHistory()) {
            } else {
                if (dtPolicyDate != null) {

                    //final boolean blockValidPolicyDate = Parameter.readBoolean("BLOCKING_POLICY_DATE");
                    final boolean isAdmin = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
                    //final boolean blockValidPolicyDateAdmin = Parameter.readBoolean("BLOCKING_POLICY_DATE_ADMIN");
                    final boolean isUserSuperEdit = SessionManager.getInstance().getSession().hasResource("USER_POL_SUPER_EDIT");

                    boolean withinCurrentMonth = DateUtil.getDateStr(getDtPolicyDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

                    boolean canEntryBackDate = UserManager.getInstance().getUser().canEntryBackdate();

                    DateTime currentDateLastDay = new DateTime(getDtPolicyDate());
                    currentDateLastDay = currentDateLastDay.dayOfMonth().withMaximumValue();

                    int batasMaxHari = 3;
                    DateTime maximumBackDate = new DateTime();
                    maximumBackDate = currentDateLastDay.plusDays(batasMaxHari);

                    DateTime currentDate = new DateTime(new Date());

                    if (currentDate.isAfter(maximumBackDate)) {
                        if (canEntryBackDate) {
                            throw new RuntimeException("Batas waktu 3 hari approval setelah akhir bulan sudah terlewati");
                        }
                    }

                    if (!canEntryBackDate) {
                        if (!isUserSuperEdit) {
                            if (isAdmin) {
                                if (!isEditReasOnlyMode()) {
                                    if (!withinCurrentMonth) {
                                        throw new RuntimeException("Tanggal Polis / Endorsemen Tidak Valid (Sudah Tutup Produksi)");
                                    }
                                }
                            } else {
                                if (!withinCurrentMonth) {
                                    dtPolicyDate = new Date();
                                }
                            }
                        }
                    }

                }

                if (getDtPeriodStart() != null && getDtPeriodEnd() != null) {
                    cekPeriodeAwal();
                }
            }
        }


        if (isStatusClaim()) {
            if (isStatusClaimPLA()) {
                //if (getStPLANo() == null) throw new RuntimeException("NO LKS Belum Diisi");

                if (getDbClaimAmountEstimate() == null) {
                    throw new RuntimeException("Jumlah Klaim Perkiraan Belum Diisi");
                }

                setStClaimStatus(FinCodec.ClaimStatus.PLA);
                setStActiveFlag("Y");

                int tgl = Tools.compare(DateUtil.truncDate(getDtPeriodStart()), DateUtil.truncDate(getDtPeriodEnd()));

                String desc = "";
                boolean validPeriod = true;
                if (tgl == 1) {
                    validPeriod = false;
                }
                //if(tgl == 0) desc = "Periode Awal / Periode Akhir Tidak Boleh Sama";
                if (tgl == 1) {
                    desc = "Periode Akhir Tidak Boleh < Periode Awal";
                }
                if (!validPeriod) {
                    throw new RuntimeException(desc);
                }

                if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                    if (getStClaimCauseID().equalsIgnoreCase("3733")) {
                        if (getStClaimNumberID() == null) {
                            throw new RuntimeException("Nama Bengkel Harus Diisi");
                        }
                    }
                }

            } else if (isStatusClaimDLA()) {
                if (getStDLANo() == null) {
                    throw new RuntimeException("NO LKP Belum Diisi");
                }

                if (getDbClaimAmountApproved() == null) {
                    throw new RuntimeException("Jumlah Klaim Disetujui Belum Diisi");
                }

                if (getDtDLADate() == null) {
                    throw new RuntimeException("Tanggal LKP Belum Diisi");
                }

                if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                    if (getStClaimCauseID().equalsIgnoreCase("3733")) {
                        if (getStClaimNumberID() == null) {
                            throw new RuntimeException("Nama Bengkel Harus Diisi");
                        }
                    }
                }
            }
        }

        if (isStatusHistory()) {
            if (getStPolicyNo().length() != 18) {
                throw new RuntimeException("Format No Polis History Harus 18 Digit, Isi 2 Digit Terakhir Dengan No Terakhir Pada Sistem Lama");
            }

            if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                if (getStCoinsID() == null) {
                    throw new RuntimeException("Member koasuransi harus diisi");
                }
            }
        }

        if (isStatusPolicy()) {
            if (getStPolicyNo() != null) {
                if (getStPolicyNo().length() != 18) {
                    throw new RuntimeException("Format No Polis Harus 18 Digit");
                }
            }
        }

        if (isCoinsurance()) {
            if (!"21".equalsIgnoreCase(getStPolicyTypeID())) {
                if (getCoins2().size() == 1) {
                    throw new RuntimeException("Polis Co-leader harus di isi member koasuransi");
                }
            }
        }

        if (getPeriodLengthYears() > 50) {
            throw new RuntimeException("Cek Periode Awal dan Periode Akhir apakah sudah betul");
        }

    }

    /**
     * @return the dbNDBrokerageAfterPPN
     */
    public BigDecimal getDbNDBrokerageAfterPPN() {
        return dbNDBrokerageAfterPPN;
    }

    /**
     * @param dbNDBrokerageAfterPPN the dbNDBrokerageAfterPPN to set
     */
    public void setDbNDBrokerageAfterPPN(BigDecimal dbNDBrokerageAfterPPN) {
        this.dbNDBrokerageAfterPPN = dbNDBrokerageAfterPPN;
    }

    /**
     * @return the clausulesNew
     */
    public DTOList getClausulesNew() {
        loadClausulesNew();
        return clausulesNew;
    }

    /**
     * @param clausulesNew the clausulesNew to set
     */
    public void setClausulesNew(DTOList clausulesNew) {
        this.clausulesNew = clausulesNew;
    }

    private void loadClausulesNew() {
        try {
            if (clausulesNew == null) {
                clausulesNew = ListUtil.getDTOListFromQuery(
                        "select a.*,b.shortdesc from ins_pol_clausules a inner join ins_clausules b on a.ins_clause_id = b.ins_clause_id where pol_id =? order by ins_pol_claus_id",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyClausulesView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReverseReinsurance() {
        boolean cek = false;
        if (stRIPostedFlag == null) {
            cek = false;
        }

        if (stRIPostedFlag != null) {
            if (stRIPostedFlag.equalsIgnoreCase("N")) {
                cek = true;
            }
            if (stRIPostedFlag.equalsIgnoreCase("Y")) {
                cek = false;
            }
        }

        return cek;

    }

    /**
     * @return the stAllowSamePeriodFlag
     */
    public String getStAllowSamePeriodFlag() {
        return stAllowSamePeriodFlag;
    }

    /**
     * @param stAllowSamePeriodFlag the stAllowSamePeriodFlag to set
     */
    public void setStAllowSamePeriodFlag(String stAllowSamePeriodFlag) {
        this.stAllowSamePeriodFlag = stAllowSamePeriodFlag;
    }

    public boolean isAllowSamePeriod() {
        return Tools.isYes(getStAllowSamePeriodFlag());
    }

    public void calculatePeriodsObject() {

        boolean endorsement = isStatusEndorse() || isStatusEndorseRI();

        Date pstart = dtPeriodStart;

        try {
            if (endorsement) {
                pstart = getParentPolicy().getDtPeriodEnd();
            }
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
                        if (Tools.compare(dbPeriodRate, BDUtil.zero) < 0) {
                            dbPeriodRate = BDUtil.zero;
                        }
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

                        if (Tools.compare(dbPeriodRateBefore, BDUtil.zero) < 0) {
                            dbPeriodRateBefore = BDUtil.zero;
                        }
                    }
                }
            }
        }

    }

    public String getStValidateClaimFlag() {
        return stValidateClaimFlag;
    }

    public void setStValidateClaimFlag(String stValidateClaimFlag) {
        this.stValidateClaimFlag = stValidateClaimFlag;
    }

    /**
     * @return the stReference13
     */
    public String getStReference13() {
        return stReference13;
    }

    /**
     * @param stReference13 the stReference13 to set
     */
    public void setStReference13(String stReference13) {
        this.stReference13 = stReference13;
    }

    /**
     * @return the stAccountno
     */
    public String getStAccountno() {
        return stAccountno;
    }

    /**
     * @param stAccountno the stAccountno to set
     */
    public void setStAccountno(String stAccountno) {
        this.stAccountno = stAccountno;
    }

    /**
     * @param claimObject the claimObject to set
     */
    public void setClaimObject(InsurancePolicyObjectView claimObject) {
        this.claimObject = claimObject;
    }

    public void validateObjectsApprove() throws Exception {
        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                    InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                    if (!isStatusClaim()) {
                        DTOList details = objectMap.getDetails();

                        for (int p = 0; p < details.size(); p++) {
                            FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                            if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                                if (objx.getStReference7().equalsIgnoreCase("11") || objx.getStReference7().equalsIgnoreCase("12")) {
                                    String cc_code = SessionManager.getInstance().getSession().getStBranch();

                                    if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
                                        throw new RuntimeException("Untuk penggunaan Komersial dan Kendaraan Khusus harus persetujuan Underwriting Kantor Pusat");
                                    }
                                }

                            }

                        }
                    }

                }
            }
        }
    }

    /**
     * @return the dtClaimPaymentDate
     */
    public Date getDtClaimPaymentDate() {
        return dtClaimPaymentDate;
    }

    /**
     * @param dtClaimPaymentDate the dtClaimPaymentDate to set
     */
    public void setDtClaimPaymentDate(Date dtClaimPaymentDate) {
        this.dtClaimPaymentDate = dtClaimPaymentDate;
    }

    /**
     * @return the stClaimPaymentUsedFlag
     */
    public String getStClaimPaymentUsedFlag() {
        return stClaimPaymentUsedFlag;
    }

    /**
     * @param stClaimPaymentUsedFlag the stClaimPaymentUsedFlag to set
     */
    public void setStClaimPaymentUsedFlag(String stClaimPaymentUsedFlag) {
        this.stClaimPaymentUsedFlag = stClaimPaymentUsedFlag;
    }

    /**
     * @return the stGatewayDataFlag
     */
    public String getStGatewayDataFlag() {
        return stGatewayDataFlag;
    }

    /**
     * @param stGatewayDataFlag the stGatewayDataFlag to set
     */
    public void setStGatewayDataFlag(String stGatewayDataFlag) {
        this.stGatewayDataFlag = stGatewayDataFlag;
    }

    public boolean isDataGateway() {
        return Tools.isYes(stGatewayDataFlag);
    }

    /**
     * @return the dbAPPpnP
     */
    public BigDecimal getDbAPPpnP() {
        return dbAPPpnP;
    }

    /**
     * @param dbAPPpnP the dbAPPpnP to set
     */
    public void setDbAPPpnP(BigDecimal dbAPPpnP) {
        this.dbAPPpnP = dbAPPpnP;
    }

    public void validateDataUploadEndorse() throws Exception {
        if (isStatusClaimPLA()) {
            if (!canClaimAgain()) {
                throw new RuntimeException("Sudah Pernah Klaim Sebelumnya Dengan Objek Sama");
            }
        }

        validateObjectsUploadEndorse();
        validateTaxCode();
    }

    public void validateObjectsUploadEndorse() throws Exception {
        if (stPolicyTypeID != null) {

            FlexFieldHeaderView objectMap = getPolicyType().getObjectMap();

            if (objectMap != null) {
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

                    InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                    if (!isStatusClaim()) {
                        DTOList details = objectMap.getDetails();

                        for (int p = 0; p < details.size(); p++) {
                            FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(p);

                            if (Tools.isYes(ffd.getStMandatoryFlag())) {
                                if (obj.getProperty(ffd.getStFieldRef()) == null) {
                                    throw new RuntimeException(ffd.getStFieldDesc() + " Objek No " + (i + 1) + " Belum Diisi");
                                }

                            }

                            if (getStPolicyTypeID().equalsIgnoreCase("3")) {
                                if (objx.getStReference7() == null) {
                                    throw new RuntimeException("Kode Penggunaan Objek No " + (i + 1) + " Belum Diisi");
                                }

                                if (objx.getStReference7().equalsIgnoreCase("11") || objx.getStReference7().equalsIgnoreCase("12")) {
                                    if (objx.getStReference10() == null) {
                                        throw new RuntimeException("Deskripsi penggunaan wajib diisi untuk Komersial dan Kendaraan Khusus");
                                    }
                                }
                            }

                            /*
                            if(ffd.getStFieldDesc().toUpperCase().contains("KODE RISIKO")){
                            if(!objx.getStReference3().toUpperCase().contains(obj.getRiskCategory().getStInsuranceRiskCategoryCode()))
                            throw new RuntimeException("Kode resiko " + "Objek No " + (i + 1) + " tidak sama, cek lagi.");
                            }
                             */
                        }
                    }

                    /*
                    if("1".equalsIgnoreCase(stPolicyTypeGroupID)){
                    if("29313".equalsIgnoreCase(obj.getRiskCategory().getStInsuranceRiskCategoryCode()))
                    if(objx.getStReference7()==null)
                    throw new RuntimeException("Kode Resiko 29313 harus mengisi field Kode Akumulasi");
                    }*/

                    if (obj.getStRiskCategoryID() == null) {
                        throw new RuntimeException("Kode Resiko Objek No " + (i + 1) + " Belum Diisi");
                    }
                    /*
                    if(obj.getDeductibles().size()>1){
                    final DTOList deduct = obj.getDeductibles();
                    for (int j = 0; j < deduct.size(); j++)
                    {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deduct.get(j);

                    if(BDUtil.isZeroOrNull(ded.getDbAmount()) && BDUtil.isZeroOrNull(ded.getDbPct()))
                    throw new RuntimeException("Deductible Belum diisi");
                    }
                    }*/



                }
            }
        }
    }

    public DTOList getSystemDocuments() {
        if (systemDocuments == null && stPolicyID != null && stPolicyTypeID != null) {
            systemDocuments = loadSystemDocuments(stPolicyTypeID, stPolicyID, "SYSTEM");
        }

        return systemDocuments;
    }

    private static DTOList loadSystemDocuments(String poltype, String stPolicyID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select "
                    + "      b.*,c.description,a.ins_document_type_id "
                    + "   from "
                    + "      ins_documents a"
                    + "      inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id"
                    + "      left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id"
                    + "   where "
                    + "      a.pol_type_id=? "
                    + "      and a.document_class=? ",
                    new Object[]{stPolicyID, poltype, documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

                docs.setStPolicyID(stPolicyID);
                docs.setStDocumentClass(documentClass);
            }

            return l;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystemDocuments(DTOList systemDocuments) {
        this.systemDocuments = systemDocuments;
    }

    /**
     * @return the stCheckingFlag
     */
    public String getStCheckingFlag() {
        return stCheckingFlag;
    }

    /**
     * @param stCheckingFlag the stCheckingFlag to set
     */
    public void setStCheckingFlag(String stCheckingFlag) {
        this.stCheckingFlag = stCheckingFlag;
    }

    public boolean isBypassValidation() {
        return Tools.isYes(stCheckingFlag);
    }

    /**
     * @return the stGatewayPolID
     */
    public String getStGatewayPolID() {
        return stGatewayPolID;
    }

    /**
     * @param stGatewayPolID the stGatewayPolID to set
     */
    public void setStGatewayPolID(String stGatewayPolID) {
        this.stGatewayPolID = stGatewayPolID;
    }

    /**
     * @return the stManualCoinsFlag
     */
    public String getStManualCoinsFlag() {
        return stManualCoinsFlag;
    }

    /**
     * @param stManualCoinsFlag the stManualCoinsFlag to set
     */
    public void setStManualCoinsFlag(String stManualCoinsFlag) {
        this.stManualCoinsFlag = stManualCoinsFlag;
    }

    public boolean isManualCoins() {
        return Tools.isYes(stManualCoinsFlag);
    }

    /**
     * @return the dbCurrencyRateClaim
     */
    public BigDecimal getDbCurrencyRateClaim() {
        return dbCurrencyRateClaim;
    }

    /**
     * @param dbCurrencyRateClaim the dbCurrencyRateClaim to set
     */
    public void setDbCurrencyRateClaim(BigDecimal dbCurrencyRateClaim) {
        this.dbCurrencyRateClaim = dbCurrencyRateClaim;
    }

    /**
     * @return the stClaimLocationID
     */
    public String getStClaimLocationID() {
        return stClaimLocationID;
    }

    /**
     * @param stClaimLocationID the stClaimLocationID to set
     */
    public void setStClaimLocationID(String stClaimLocationID) {
        this.stClaimLocationID = stClaimLocationID;
    }

    public String getParaf2() {

        String NIPPimpinan = Parameter.readString("PARAF_" + getStCostCenterCode());

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        String useridSign = null;

        if (NIPPimpinan != null) {
            UserSessionView userWakil = getUser(NIPPimpinan);

            useridSign = userWakil.getStUserID();
        }

        return useridSign;
    }

    public GLCostCenterView3 getCostCenter3() {

        final GLCostCenterView3 gcc = (GLCostCenterView3) DTOPool.getInstance().getDTO(GLCostCenterView3.class, stCostCenterCode);

        return gcc;

    }

    /**
     * @return the stRiskApprovedWho
     */
    public String getStRiskApprovedWho() {
        return stRiskApprovedWho;
    }

    /**
     * @param stRiskApprovedWho the stRiskApprovedWho to set
     */
    public void setStRiskApprovedWho(String stRiskApprovedWho) {
        this.stRiskApprovedWho = stRiskApprovedWho;
    }

    /**
     * @return the dtRiskApprovedDate
     */
    public Date getDtRiskApprovedDate() {
        return dtRiskApprovedDate;
    }

    /**
     * @param dtRiskApprovedDate the dtRiskApprovedDate to set
     */
    public void setDtRiskApprovedDate(Date dtRiskApprovedDate) {
        this.dtRiskApprovedDate = dtRiskApprovedDate;
    }

    /**
     * @return the stRiskApproved
     */
    public String getStRiskApproved() {
        return stRiskApproved;
    }

    /**
     * @param stRiskApproved the stRiskApproved to set
     */
    public void setStRiskApproved(String stRiskApproved) {
        this.stRiskApproved = stRiskApproved;
    }

    public boolean isApprovedRisk() {
        return Tools.isYes(stRiskApproved);
    }

    public boolean isOverLimit() {
        return Tools.isYes(stUnderwritingFinishFlag);
    }

    /**
     * @return the stNotificationUserID
     */
    public String getStNotificationUserID() {
        return stNotificationUserID;
    }

    /**
     * @param stNotificationUserID the stNotificationUserID to set
     */
    public void setStNotificationUserID(String stNotificationUserID) {
        this.stNotificationUserID = stNotificationUserID;
    }

    /**
     * @return the stEffectiveTempFlag
     */
    public String getStEffectiveTempFlag() {
        return stEffectiveTempFlag;
    }

    /**
     * @param stEffectiveTempFlag the stEffectiveTempFlag to set
     */
    public void setStEffectiveTempFlag(String stEffectiveTempFlag) {
        this.stEffectiveTempFlag = stEffectiveTempFlag;
    }

    public void recalculateSpreading() throws Exception {
        //recalculateBasicSpreading();
        //recalculateClaim();
        //recalculateBasicSpreading();
        invokeCustomHandlersSpreading(true);
        invokeCustomHandlersSpreading(false);
        invokeCustomHandlersPolicy(false);
        //recalculateClaim();
        recalculateEndorsePAKreasi();

        if (isStatusPolicy() || isStatusClaim() || isStatusEndorse() || isStatusRenewal() || isStatusEndorseRI() || isStatusEndorseIntern() || isStatusTemporaryPolicy() || isStatusTemporaryEndorsemen() || isStatusInward()) {
            validateTreaty(false);
        }
    }

    private void invokeCustomHandlersSpreading(boolean validate) {
        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

//            CustomHandlerManager.getInstance().onCalculateSpreading(this, obj, validate);
        }
    }

    /**
     * @return the taxAcrualBases
     */
    public boolean isTaxAcrualBases() {
        return taxAcrualBases;
    }

    /**
     * @param taxAcrualBases the taxAcrualBases to set
     */
    public void setTaxAcrualBases(boolean taxAcrualBases) {
        this.taxAcrualBases = taxAcrualBases;
    }
    private DTOList policyApprovalBOD;

    public DTOList getPolicyApprovalBOD() {
        loadPolicyApprovalBOD();
        return policyApprovalBOD;
    }

    private void loadPolicyApprovalBOD() {
        try {
            if (policyApprovalBOD == null) {
                policyApprovalBOD = ListUtil.getDTOListFromQuery(
                        " select a.ref_no,a.out_id,a.approval_type,a.pol_id,a.letter_no,count(a.in_id) as cc, "
                        + "(select count(b.out_id) from uploadbod_dist b where b.out_id::text = a.out_id) as cc_id "
                        + "from approvalbod_letter a "
                        + "where a.approval_type is not null and a.pol_id = ? and a.approval_type = 'SETUJUI' "
                        + "group by a.ref_no,a.sender,a.subject,a.out_id,a.approval_type,a.pol_id,a.letter_no "
                        + "order by a.out_id ",
                        new Object[]{stPolicyID},
                        ApprovalBODView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getCoverageCIS() {
        loadCoverageCIS();
        return coverage;
    }

    public void setCoverageCIS(DTOList coverage) {
        this.coverage = coverage;
    }

    public void loadCoverageCIS() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (coverage == null) {
                coverage = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_cover where pol_id = ? and ins_cover_id in (8,237) order by ins_pol_cover_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyCoverView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stMarketingOfficerWho
     */
    public String getStMarketingOfficerWho() {
        return stMarketingOfficerWho;
    }

    /**
     * @param stMarketingOfficerWho the stMarketingOfficerWho to set
     */
    public void setStMarketingOfficerWho(String stMarketingOfficerWho) {
        this.stMarketingOfficerWho = stMarketingOfficerWho;
    }

    /**
     * @return the stReverseNotes
     */
    public String getStReverseNotes() {
        return stReverseNotes;
    }

    /**
     * @param stReverseNotes the stReverseNotes to set
     */
    public void setStReverseNotes(String stReverseNotes) {
        this.stReverseNotes = stReverseNotes;
    }

    public boolean canSeeCoinsurance() {
        boolean hasCoIns = hasCoIns();
        boolean directPolicy = false;

        final String type_code = getPolicyType() != null ? getPolicyType().getStPolicyTypeCode() : "";

        if (getStCoverTypeCode() != null) {
            if (getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                hasCoIns = false;
            }

            if (getStCoverTypeCode().equalsIgnoreCase("DIRECT")) {
                directPolicy = true;
            }
        }

        boolean canSeeCoins = hasCoIns;

        if (SessionManager.getInstance().getSession().getStBranch() != null) {
            if (!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00") && type_code.equalsIgnoreCase("OM_KREASI")) {
                canSeeCoins = false;
            }
        }

        if (getPolicyType() != null) {
            if (type_code.equalsIgnoreCase("OM_CREDIT")) {
                canSeeCoins = false;
            }
        }

        return canSeeCoins;

    }

    public void defineTreaty() throws Exception {
        final boolean inward = getCoverSource().isInward();

        String treatyID = getDtPeriodStart() != null ? getInsuranceTreatyID(getDtPeriodStart()) : Parameter.readString(inward ? "UWRIT_DEF_ITREATY" : "UWRIT_DEF_OTREATY");

        final DTOList objects = getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            if (getStPolicyTypeID().equalsIgnoreCase("3") || getStPolicyTypeID().equalsIgnoreCase("1") || getStPolicyTypeID().equalsIgnoreCase("81")) {
                if (obj.getDtReference1() != null) {
                    treatyID = obj.getDtReference1() != null ? getInsuranceTreatyID(obj.getDtReference1()) : Parameter.readString(inward ? "UWRIT_DEF_ITREATY" : "UWRIT_DEF_OTREATY");
                }
            }

            if (getStPolicyTypeID().equalsIgnoreCase("85")) {
                treatyID = "36";
            }

            if (obj.getRiskCategory().getStInsuranceTreatyID() != null) {
                treatyID = obj.getRiskCategory().getStInsuranceTreatyID();
            }

            if (getStPolicyTypeID().equalsIgnoreCase("59")) {
                setCreditTreaty(obj);
            }


        }
    }

    public void setCreditTreaty(InsurancePolicyObjDefaultView obj) throws Exception {

        final boolean inward = getCoverSource().isInward();
        String treatyID = getDtPeriodStart() != null ? getInsuranceTreatyID(getDtPeriodStart()) : Parameter.readString(inward ? "UWRIT_DEF_ITREATY" : "UWRIT_DEF_OTREATY");

        boolean orOnly = true;

        if (getStPolicyTypeID().equalsIgnoreCase("59")) {
            int usiaKredit = Integer.parseInt(obj.getStReference2());
            int usiaPlusLamaKredit = usiaKredit + Integer.parseInt(obj.getStReference5());

            if (usiaKredit >= 17 && usiaKredit <= 64 && usiaPlusLamaKredit <= 70) {
                orOnly = false;
            }

            //jika entity id nya TIB PENSIUNAN BNI, pakai treaty autofac tib bni
            if(getStEntityID().equalsIgnoreCase("1032872"))
                treatyID = "186";
        }

        if (getStPolicyTypeID().equalsIgnoreCase("21")) {
            int usiaKreasi = Integer.parseInt(obj.getStReference2());
            int usiaPlusLamaKreasi = usiaKreasi + Integer.parseInt(obj.getStReference4());

            boolean isORKreasi = obj.getStReference8().equalsIgnoreCase("1");

            if (usiaKreasi >= 17 && usiaKreasi <= 64 && usiaPlusLamaKreasi <= 70 && isORKreasi) {
                orOnly = false;
            }

        }

        if (getStRegionID().equalsIgnoreCase("39")) {
            orOnly = true;
        }

        if (orOnly) {
            obj.setStInsuranceTreatyID("36");
        } else {
            obj.setStInsuranceTreatyID(treatyID);
        }

    }

    public boolean isBankMantap() {

        boolean isMantap = false;

        if (getEntity().getStRef2().equalsIgnoreCase("1129")) {
            isMantap = true;
        }

        return isMantap;
    }

    /**
     * @return the dtClaimPaymentDate2
     */
    public Date getDtClaimPaymentDate2() {
        return dtClaimPaymentDate2;
    }

    /**
     * @param dtClaimPaymentDate2 the dtClaimPaymentDate2 to set
     */
    public void setDtClaimPaymentDate2(Date dtClaimPaymentDate2) {
        this.dtClaimPaymentDate2 = dtClaimPaymentDate2;
    }

    public String getPolicyNoFromPersetujuanPrinsip() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        EntityView entity = getEntity();

        if (getStPolicyTypeGroupID().equalsIgnoreCase("7") && !getStPolicyTypeID().equalsIgnoreCase("59")) {
            final DTOList obj = getObjects();
            InsurancePolicyObjDefaultView object = (InsurancePolicyObjDefaultView) obj.get(0);
            entity = getEntity2(object.getStReference2());
        }

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        if (stReference1 == null) {
            throw new Exception("Nomor persetujuan prinsip masih kosong");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        //  045824020213000300
        //    5824021212015000
        //  012345678901234567

        return coasCode + // A
                custCategory + // B
                stReference1;
    }

    public String getInsuranceTreatyID(Date per_start, String companyGroupID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      ins_treaty_id,treaty_name "
                    + "   from "
                    + "         ins_treaty"
                    + "   where"
                    + "      treaty_period_start <= ? "
                    + "   and treaty_period_end >= ? "
                    + "   and company_group_id = ? ");

            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, companyGroupID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public boolean isBankNagari() {

        boolean isBankNagari = false;

        if (getEntity().getStRef2().equalsIgnoreCase("203")) {
            isBankNagari = true;
        }

        return isBankNagari;
    }

    /**
     * @return the stDataSourceID
     */
    public String getStDataSourceID() {
        return stDataSourceID;
    }

    /**
     * @param stDataSourceID the stDataSourceID to set
     */
    public void setStDataSourceID(String stDataSourceID) {
        this.stDataSourceID = stDataSourceID;
    }

    public BigDecimal getTransactionLimitPerRiskLevel(String cat, String cat2, String userID, String riskLevel) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "      max(c.refn" + cat2 + ") "
                    + "   from "
                    + "         s_user_roles b "
                    + "         inner join ff_table c on c.fft_group_id='CAPA' and c.ref1=b.role_id and c.ref2=? and c.ref3=?"
                    + "   where"
                    + " c.active_flag = 'Y' "
                    + " and b.user_id=? and c.ref6 = ?"
                    + " and period_start <= ? and period_end >= ?");

            S.setParam(1, cat);
            S.setParam(2, getStPolicyTypeID());
            S.setParam(3, userID);
            S.setParam(4, riskLevel);
            S.setParam(5, getDtPolicyDate());
            S.setParam(6, getDtPolicyDate());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void getLowestRateOJK() throws Exception {
    }

    


    public BigDecimal getAkumulasiKomisi(String stEntityID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select b.ent_id,sum(b.amount) as total_komisi,sum(b.amount)*0.5 as komisi_kena_pajak_kumulatif "
                    + " from ins_policy a "
                    + " inner join ins_pol_items b on a.pol_id = b.pol_id "
                    + " where a.status in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag = 'Y' "
                    + " and date_trunc('day',policy_date)>= (extract(year from now())::character varying ||'-01-01'):: timestamp without time zone "
                    + " and date_trunc('day',policy_date)<= (extract(year from now())::character varying ||'-12-31'):: timestamp without time zone "
                    + " and b.ins_item_id in (select ins_item_id from ins_items where item_type = 'COMIS' and coalesce(use_tax,'Y') <> 'N') "
                    + " and b.ent_id = ? "
                    + " group by b.ent_id");

            S.setParam(1, stEntityID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal("total_komisi");
            }

            return null;
        } finally {
            S.release();
        }
    }
    DTOList akumulasiKomisi;

    public DTOList getDataAkumulasiKomisi(String stEntityID) {
        loadAkumulasiKomisi(stEntityID);
        return akumulasiKomisi;
    }

    private void loadAkumulasiKomisi(String stEntityID) {
        try {
            if (akumulasiKomisi == null) {
                akumulasiKomisi = ListUtil.getDTOListFromQuery(
                        "select b.ent_id,sum(b.amount) as total_komisi,sum(b.amount)*0.5 as komisi_kena_pajak_kumulatif "
                        + " from ins_policy a "
                        + " inner join ins_pol_items b on a.pol_id = b.pol_id "
                        + " where a.status in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag = 'Y' "
                        + " and date_trunc('day',policy_date)>= (extract(year from now())::character varying ||'-01-01'):: timestamp without time zone "
                        + " and date_trunc('day',policy_date)<= (extract(year from now())::character varying ||'-12-31'):: timestamp without time zone "
                        + " and b.ins_item_id in (select ins_item_id from ins_items where item_type = 'COMIS' and coalesce(use_tax,'Y') <> 'N') "
                        + " and b.ent_id = ? "
                        + " group by b.ent_id",
                        new Object[]{stEntityID},
                        HashDTO.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateAkumulasiKomisi() throws Exception {

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if (item.getStEntityID() != null) {

                BigDecimal akumulasiKomisi = BDUtil.zero;

                BigDecimal cekAkumulasi = getAkumulasiKomisi(item.getStEntityID());

                if (cekAkumulasi != null) {
                    akumulasiKomisi = cekAkumulasi;
                }

                BigDecimal akumulasiPlusKomisi = BDUtil.add(akumulasiKomisi, item.getDbAmount());

                BigDecimal penghasilanKenaPajakKumulatif = BDUtil.mul(akumulasiKomisi, new BigDecimal(0.5), scale);

                item.setDbTotalAmountTaxable(penghasilanKenaPajakKumulatif);

                item.setDbTotalAmount(akumulasiKomisi);

                item.setDbTotalAmountUntilThisMonth(akumulasiPlusKomisi);

            }

        }
    }

    private void calculatePPH21Progressive() {


        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (item.isComission()); else {
                continue;
            }

            if (!"1".equalsIgnoreCase(item.getStTaxCode())
                    && !"4".equalsIgnoreCase(item.getStTaxCode())) {
                continue;
            }

            BigDecimal akumulasiKomisi = item.getDbTotalAmountTaxable();
            BigDecimal komisiPolis = BDUtil.mul(item.getDbAmount(), new BigDecimal(0.5), scale);
            BigDecimal totalKomisiCurrent = BDUtil.add(akumulasiKomisi, komisiPolis);

            String[][] taxTab = new String[][]{
                {"50000000", "0.05"},
                {"250000000", "0.15"},
                {"500000000", "0.25"},
                {"1000000000000", "0.30"},};

            BigDecimal taxAmount = null;

            BigDecimal sisaKomisi = komisiPolis;

            BigDecimal amt = komisiPolis;
            BigDecimal komisiTerpakai = BDUtil.zero;

            for (int j = 0; j < taxTab.length; j++) {
                String[] t = taxTab[j];

                if (BDUtil.lesserThan(new BigDecimal(t[0]), akumulasiKomisi)) {
                    continue;
                }

                System.out.println("+++++++++++++++++++++++++++++++++++++++++");


                BigDecimal lim = new BigDecimal(t[0]);

                amt = sisaKomisi;

                if (BDUtil.biggerThan(totalKomisiCurrent, lim)) {
                    amt = BDUtil.sub(BDUtil.sub(lim, akumulasiKomisi), komisiTerpakai);

                    komisiPolis = BDUtil.sub(komisiPolis, amt);
                }

                System.out.println(" penghasilan kena pajak : " + amt);

                //cek lagi
                boolean hasNPWP = item.getStNPWP() != null ? true : false;

                if (item.getStNPWP() != null) {
                    if (item.getStNPWP().equalsIgnoreCase("")) {
                        hasNPWP = false;
                    }
                }

                if (hasNPWP) {
                    taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t[1]), 0));
                } else if (!hasNPWP) {
                    taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), 0), new BigDecimal(1.20), 0));
                }

                komisiTerpakai = BDUtil.add(komisiTerpakai, amt);

                sisaKomisi = BDUtil.sub(sisaKomisi, amt);

                System.out.println(" pct pajak : " + t[1]);
                System.out.println(" pajak ke " + (j + 1) + " : " + BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), 0), new BigDecimal(1.20), 0));
                System.out.println(" sisa Komisi : " + sisaKomisi);
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");

                if (BDUtil.isZero(sisaKomisi)) {
                    break;
                }
            }

            BigDecimal actPct = BDUtil.div(taxAmount, item.getDbAmount(), 5);

            System.out.println(" total pajak : " + taxAmount);
            item.setDbTaxAmount(taxAmount);
            //item.setDbTaxRatePct(null);
            item.setDbTaxRate(actPct);

        }
    }

    /**
     * @return the stNomorBuktiBayar
     */
    public String getStNomorBuktiBayar() {
        return stNomorBuktiBayar;
    }

    /**
     * @param stNomorBuktiBayar the stNomorBuktiBayar to set
     */
    public void setStNomorBuktiBayar(String stNomorBuktiBayar) {
        this.stNomorBuktiBayar = stNomorBuktiBayar;
    }

    /**
     * @return the dtTanggalBayar
     */
    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    /**
     * @param dtTanggalBayar the dtTanggalBayar to set
     */
    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }

    /**
     * @return the stNomorReferensiPembayaran
     */
    public String getStNomorReferensiPembayaran() {
        return stNomorReferensiPembayaran;
    }

    /**
     * @param stNomorReferensiPembayaran the stNomorReferensiPembayaran to set
     */
    public void setStNomorReferensiPembayaran(String stNomorReferensiPembayaran) {
        this.stNomorReferensiPembayaran = stNomorReferensiPembayaran;
    }

    /**
     * @return the dtSparetimeDate
     */
    public Date getDtSparetimeDate() {
        return dtSparetimeDate;
    }

    /**
     * @param dtSparetimeDate the dtSparetimeDate to set
     */
    public void setDtSparetimeDate(Date dtSparetimeDate) {
        this.dtSparetimeDate = dtSparetimeDate;
    }
    private DTOList subrogasi;

    /**
     * @return the subrogasi
     */
    public DTOList getSubrogasi() {
        loadSubrogasi();
        return subrogasi;
    }

    private void loadSubrogasi() {
        try {
            if (subrogasi == null) {
                subrogasi = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_subrogasi where pol_id = ? order by ins_pol_subrogasi_id",
                        new Object[]{stPolicyID},
                        InsurancePolicySubrogasiView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param subrogasi the subrogasi to set
     */
    public void setSubrogasi(DTOList subrogasi) {
        this.subrogasi = subrogasi;
    }
    private boolean enabledSubrogasi = false;

    /**
     * @return the enabledSubrogasi
     */
    public boolean isEnabledSubrogasi() throws Exception {
        boolean subrogasi = false;
        final InsurancePolicyTypeView polType = getPolicyType();
        if (polType != null) {
            subrogasi = polType.checkProperty("SUBROGASI", "Y");
        }
        return subrogasi;
    }

    /**
     * @param enabledSubrogasi the enabledSubrogasi to set
     */
    public void setEnabledSubrogasi(boolean enabledSubrogasi) {
        this.enabledSubrogasi = enabledSubrogasi;
    }

    public void loadObjectsInterkoneksi() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQueryDS(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass,
                        "GATEWAY");

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadClausulesInterkoneksi() {
        //if (!isAutoLoadEnabled()) return;

        try {
            if (clausules == null && stPolicyTypeID != null) {
                clausules = ListUtil.getDTOListFromQueryDS(
                        "   select "
                        + "      a.*,b.description,b.description_new,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default,b.child_clausules,b.parent_id  "
                        + "   from "
                        + "      ins_clausules b "
                        + "      left join ins_pol_clausules a on "
                        + "         a.ins_clause_id = b.ins_clause_id"
                        + "         and a.pol_id = ? "
                        + "         and a.ins_pol_obj_id is null"
                        + "   where b.pol_type_id = ? and cc_code = ? "
                        + "   order by b.orderseq, b.shortdesc",
                        new Object[]{stPolicyID, stPolicyTypeID, stCostCenterCode},
                        InsurancePolicyClausulesView.class, "GATEWAY");

                String ent_name = "";
                if (getEntity() != null) {
                    ent_name = getEntity().getStEntityName();
                }

                for (int i = 0; i < clausules.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                    icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

                    if (icl.getStPolicyID() != null) {
                        icl.select();
                        icl.markUpdate();
                    } else {
                        icl.setDbRate(icl.getDbRateDefault());
                        icl.markNew();
                    }

                    icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
                }

                if (isNew()) {
                    for (int i = 0; i < clausules.size(); i++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                        icl.setStDescription(icl.getStDescription().replaceAll("%S%", ent_name));

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

    public void loadEntitiesInterkoneksi() {
        if (!isAutoLoadEnabled()) {
            return;
        }
        try {
            if (entities == null) {
                entities = ListUtil.getDTOListFromQueryDS(
                        "select * from ins_pol_entity where pol_id = ?",
                        new Object[]{stPolicyID},
                        InsurancePolicyEntityView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDetailsInterkoneksi() {
        //if (!isAutoLoadEnabled()) return;
        try {
            if (details == null) {
                details = ListUtil.getDTOListFromQueryDS(
                        "select * from ins_pol_items where pol_id = ? and item_class='PRM'",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCoinsInterkoneksi() {
        try {
            if (coins == null) {
                coins = ListUtil.getDTOListFromQueryDS(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where policy_id = ? and coins_type = 'COINS'"
                        + "   order by entity_id, ins_pol_coins_id",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadInstallmentInterkoneksi() throws Exception {
        try {
            if (installment == null) {
                installment = ListUtil.getDTOListFromQueryDS(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_installment"
                        + "   where"
                        + "      policy_id = ? order by ins_pol_inst_id",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyInstallmentView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getObjectsInterkoneksi() {
        loadObjectsInterkoneksi2();
        return objects;
    }

    public void loadObjectsInterkoneksi2() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQueryDS(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? order by ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass, "GATEWAY");

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getCoinsInterkoneksi() {
        if (coins == null) {
            loadCoinsInterkoneksi();
        }
        return coins;
    }

    public DTOList getDetailsInterkoneksi() {
        loadDetailsInterkoneksi();
        return details;
    }

    public DTOList getClaimItemsInterkoneksi() {
        loadClaimItemsInterkoneksi();
        return claimItems;
    }

    private void loadClaimItemsInterkoneksi() {
        try {
            if (claimItems == null) {
                claimItems = ListUtil.getDTOListFromQueryDS(
                        "select * from ins_pol_items where pol_id = ? and item_class='CLM' order by ins_pol_item_id",
                        new Object[]{stPolicyID},
                        InsurancePolicyItemsView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public DTOList getInstallmentInterkoneksi() throws Exception {
        if (installment == null) {
            loadInstallmentInterkoneksi();
        }
        return installment;
    }

    public DTOList getCoinsCoverageInterkoneksi() {
        if (coinsCoverage == null) {
            loadCoinsCoverageInterkoneksi();
        }
        return coinsCoverage;
    }

    public void loadCoinsCoverageInterkoneksi() {
        try {
            if (coinsCoverage == null) {
                coinsCoverage = ListUtil.getDTOListFromQueryDS(
                        "   select "
                        + "      *"
                        + "   from"
                        + "      ins_pol_coins"
                        + "   where"
                        + " policy_id = ?  and coins_type = 'COINS_COVER' order by entity_id, ins_pol_coins_id ",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyCoinsView.class, "GATEWAY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generatePolicyNoInterkoneksi() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        EntityView entity = getEntity();

        if (getStPolicyTypeGroupID().equalsIgnoreCase("7") && !getStPolicyTypeID().equalsIgnoreCase("59")) {
            final DTOList obj = getObjectsInterkoneksi();
            InsurancePolicyObjDefaultView object = (InsurancePolicyObjDefaultView) obj.get(0);
            entity = getEntity2(object.getStReference2());
        }

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String month2Digit = DateUtil.getMonth2Digit(getDtPolicyDate());
        String counterKey = month2Digit + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        //String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID(policyType2Digit + year2Digit + ccCode, 1)), '0', 4);

        String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID(policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789
        if (isStatusTemporaryPolicy()) {
            coasCode = "9";
        }

        stPolicyNo =
                coasCode + // A
                custCategory + // B
                getDigit(policyType2Digit, 2) + // C
                ccCode + // D
                regCode + // E
                counterKey + // Fg
                orderNo + //H
                "00" //I
                ;
    }

    public void generatePersetujuanPrinsipNoInterkoneksi() throws Exception {
        String coasCode = (String) FinCodec.InsuranceCoverType.getLookUpCoasCode().getValue(getStCoverTypeCode());

        final EntityView entity = getEntity();

        if (entity == null) {
            throw new Exception("You must select customer for this policy");
        }

        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(getDtPolicyDate());
        String month2Digit = DateUtil.getMonth2Digit(getDtPolicyDate());
        String counterKey = month2Digit + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        //String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  ABCCDDEEFFGGHHHHII
        //	  010320200210010400
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);


        stReference1 =
                getDigit(policyType2Digit, 2) + // C
                ccCode + // D
                regCode + // E
                counterKey + // Fg
                orderNo + //H
                "00" //I
                ;
    }
    private DTOList entityByName;

    public void loadEntityByName(String nama) {
        try {
            if (entityByName == null) {
                entityByName = ListUtil.getDTOListFromQuery(
                        "select * "
                        + " from ent_master "
                        + " where cc_code = ? and trim(upper(ent_name)) like ? ",
                        new Object[]{stCostCenterCode, nama.trim().replaceAll("PT", "").toUpperCase()},
                        EntityView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the entityByName
     */
    public DTOList getEntityByName(String nama) {
        loadEntityByName(nama);
        return entityByName;
    }

    public boolean isPolisIndukPremiPaid() throws Exception {
        boolean canClaim = false;

        if (getDtPaymentDate() != null) {
            canClaim = true;
            //setDtPaymentDate(getDtPaymentDate());
            //setStPaymentNotes(getStPaymentNotes());
        }

        final DTOList pembayaran = getArinvoicesPolisInduk();

        if (pembayaran.size() > 0) {
            ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

            if (inv.getDbAmountSettled() != null) {
                if (inv.getDtPaymentDate() != null) {
                    canClaim = true;
                    //setDtPaymentDate(inv.getDtPaymentDate());
                    //setStPaymentNotes(inv.getstrec);
                }
            }
        }

        return canClaim;
    }

    public DTOList getArinvoicesPolisInduk() {
        loadARInvoicesPolisInduk();
        return arinvoices;
    }

    private void loadARInvoicesPolisInduk() {
        try {

            String trx_type_id = getCoverSource().getStARTransactionTypeID();

            if (isStatusClaim()) {
                trx_type_id = "12";
            }

            arinvoices = ListUtil.getDTOListFromQuery(
                    "      select "
                    + "         a.amount_settled,"
                    + "         ("
                    + "            select max(b.receipt_date) from ar_receipt_lines c, ar_receipt b\n"
                    + "            where c.ar_invoice_id=a.ar_invoice_id and c.receipt_id=b.ar_receipt_id"
                    + "         ) as payment_date"
                    + "      from "
                    + "         ar_invoice   a"
                    + "      where "
                    + "         coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id = ? and attr_pol_no = ?"
                    + "  union "
                    + " select premi_total as amount_settled,payment_date "
                    + " from ins_policy "
                    + " where status = 'HISTORY' and effective_flag = 'Y' and active_flag = 'Y' "
                    + " and substr(pol_no,0,17) = ?",
                    new Object[]{trx_type_id, getStPolicyNo().substring(0, 16) + "00", getStPolicyNo().substring(0, 16)},
                    ARInvoiceView.class);

        } catch (NullPointerException e) {

            arinvoices = new DTOList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validateEntity() throws Exception {

        final EntityView entity = getEntity();

        if(!isStatusClaim() && !isStatusClaimEndorse()){
            if(entity!=null){
                if (entity.getStCustomerStatus() != null) {
                    if (entity.getStCustomerStatus().equalsIgnoreCase("BLACK LIST")) {
                        throw new RuntimeException("Customer di BLACK LIST, tidak dapat diinput");
                    }
                }
            }
        }
        
    }

    private DTOList treatyInduk;

    public void loadTreatyInduk(String noUrut) {
        try {
            if (treatyInduk == null) {
                treatyInduk = ListUtil.getDTOListFromQuery(
                        "select c.*,b.insured_amount as tsi_amount "+
                        " from ins_policy a "+
                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                        " inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "+
                        " where effective_flag = 'Y' and active_flag = 'Y' "+
                        " and a.status in ('POLICY','HISTORY','RENEWAL','INWARD') "+
                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? ",
                        new Object[]{stPolicyNo.substring(0, 16),noUrut},
                        InsurancePolicyTreatyView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the entityByName
     */
    public DTOList getTreatyInduk(String noUrut) {
        loadTreatyInduk(noUrut);
        return treatyInduk;
    }

    public boolean isPolis(){
        boolean polis = false;

        if(stPolicyNo.substring(16,18).equalsIgnoreCase("00")) polis = true;

        return polis;
    }

    public BigDecimal getPremiNetto(){

        BigDecimal premiNett = null;
        BigDecimal totalKomisi = BDUtil.zero;
        BigDecimal totalDiskon = BDUtil.zero;
        BigDecimal totalBrokerfee = BDUtil.zero;

        totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, getDbNDComm1()),getDbNDComm2());
        totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, getDbNDComm3()),getDbNDComm4());
        totalKomisi = BDUtil.add(BDUtil.add(totalKomisi, getDbNDFeeBase1()),getDbNDFeeBase2());
        totalBrokerfee = BDUtil.add(BDUtil.add(totalBrokerfee, getDbNDBrok1()), getDbNDBrok2());

        premiNett = BDUtil.sub(getDbPremiTotal(), totalKomisi);
        premiNett = BDUtil.sub(premiNett, totalBrokerfee);
        totalDiskon = BDUtil.add(getDbNDDisc1(), getDbNDDisc2());
        premiNett = BDUtil.sub(premiNett, totalDiskon);

        return premiNett;
    }
    
    public void validateDoubleObject(InsurancePolicyObjectView obj) throws Exception {
        if (getStPolicyTypeID().equalsIgnoreCase("21") || getStPolicyTypeID().equalsIgnoreCase("59") || getStPolicyTypeID().equalsIgnoreCase("80")) {

            if (isStatusDraft() || isStatusSPPA() || isStatusPolicy()) {

                    final SQLUtil S = new SQLUtil();

                    InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                    boolean tidakDouble = false;

                    if (getStPolicyTypeID().equalsIgnoreCase("59")) {
                        if (objx.getStReference15() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference15());
                        }
                    }

                    if (getStPolicyTypeID().equalsIgnoreCase("21")) {
                        if (objx.getStReference24() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference24());
                        }
                    }

                    if (getStPolicyTypeID().equalsIgnoreCase("80")) {
                        if (objx.getStReference15() != null) {
                            tidakDouble = Tools.isYes(objx.getStReference15());
                        }
                    }

                    if (tidakDouble) {
                        return;
                    }

                    try {
                        final PreparedStatement PS = S.setQuery("select a.pol_id,a.pol_no,b.ins_pol_obj_id,b.ref1,b.refd1,b.refd2 "
                                + " from ins_policy a inner join ins_pol_obj b on a.pol_id = b.poL_id "
                                + " where a.cc_code = ? and trim(upper(b.ref1)) = ? and b.refd1 = ? and b.refd2 = ? and b.insured_amount = ? and a.status in ('POLICY','RENEWAL') and coalesce(a.active_flag,'N') = 'Y'");

                        PS.setString(1, getStCostCenterCode());
                        PS.setString(2, objx.getStReference1().trim().toUpperCase());
                        PS.setObject(3, objx.getDtReference1());
                        PS.setObject(4, objx.getDtReference2());
                        PS.setObject(5, objx.getDbObjectInsuredAmount());

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()) {
                            if (objx.getStPolicyObjectID() != null) {
                                if (objx.getStPolicyObjectID().equalsIgnoreCase(RS.getString("ins_pol_obj_id"))) {
                                    return;
                                }
                            }

                            stDoubleDescription = stDoubleDescription + "Debitur " + objx.getStReference1() + " sudah diinput pada nopol : " + RS.getString("pol_no") + "<br>";
                            isDouble = true;
                        }

                    } finally {
                        S.release();
                    }

                S.release();
            }
        }

    }

    public void validateJenisKredit(InsuranceSplitPolicyObjectView obj) throws Exception{
        if(!getStPolicyTypeID().equalsIgnoreCase("21") && !getStPolicyTypeID().equalsIgnoreCase("59"))
            return;

        if(isStatusEndorse()||isStatusClaim()||isStatusClaimEndorse()||isStatusEndorseRI()||isStatusHistory())
            return;

        if(getStAdminNotes()!=null)
            if(getStAdminNotes().equalsIgnoreCase("APPROVED"))
                return;

        if(isBypassValidation())
            return;

        //final DTOList object = policy.getObjects();

        DateTime bulanIzinKredit = new DateTime(DateUtil.getDate("19/06/2012"));
        DateTime bulanNovember = new DateTime(DateUtil.getDate("01/11/2012"));
        //DateTime bulanDesember = new DateTime(DateUtil.getDate("01/12/2012"));
        DateTime tglCreate = new DateTime(getDtCreateDate());

        PAKreasiHandler hd = new PAKreasiHandler();

        hd.getJenisKredit(getStKreasiTypeID());

        //String jenis_kredit = hd.getJenisKredit(policy.getStKreasiTypeID());
        //String jenis_rate = hd.getJenisRate(policy.getStKreasiTypeID());
        String jenis_kredit = hd.getJenisKredit();
        String jenis_rate = hd.getJenisRate();
        boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
        boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
        boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");

        String onlyPlafonKreasi = hd.getRef3(getStKreasiTypeID());
        boolean onlyPlafon = Tools.isNo(onlyPlafonKreasi);

        InsuranceSplitPolicyObjDefaultView objx = (InsuranceSplitPolicyObjDefaultView) obj;


        if(objx.getStReference20()!=null)
            if(Tools.isYes(objx.getStReference20()))
                return;


        DateTime tglAwal = new DateTime(objx.getDtReference2());

        if(getStPolicyTypeID().equalsIgnoreCase("21")){

            //DateTime bulanIzinKreasiSemarang = new DateTime(DateUtil.getDate("01/01/2013"));
            //DateTime tglPolis = new DateTime(policy.getDtPolicyDate());

            BigDecimal tsi = objx.getDbReference4();
            int maksUsia = Integer.parseInt(objx.getStReference2()) + Integer.parseInt(objx.getStReference4());

            if(getStCostCenterCode().equalsIgnoreCase("22"))
                return;

            if(tglCreate.isBefore(bulanNovember))
                return;

            if(BDUtil.biggerThanEqual(tsi, new BigDecimal(1000000000)))
                return;

            if(maksUsia > 65)
                return;

            if(objx.getStReference21()!=null)
                if(Tools.isYes(objx.getStReference21()))
                    return;

            if(tglAwal.isAfter(bulanIzinKredit)){
                    if(onlyPlafon){
                        setStKreasiTypeID(null);
                        throw new RuntimeException("Asuransi PA Kreasi hanya boleh jenis Plafon Awal, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
                    }
            }

        }

        if(getStPolicyTypeID().equalsIgnoreCase("59")){
            if(tglAwal.isAfter(bulanIzinKredit)){
                if(isPlafonAwal){
                    setStKreasiTypeID(null);
                    throw new RuntimeException("Asuransi Kredit hanya boleh jenis Baki Debet, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
                }
            }

            if(tglAwal.isBefore(bulanIzinKredit)){
                    setStKreasiTypeID(null);
                    throw new RuntimeException("Asuransi Kredit hanya boleh jenis Baki Debet, Debitur : "+ objx.getStReference1()+" No : "+ objx.getStOrderNo());
            }

        }

    }

    public boolean isDataSubrogasi(){
        return Tools.isYes(getStReference13());
    }

    public boolean isBankJatengH2H() {

        boolean isBankJateng = false;

        if (getEntity().getStRef2().equalsIgnoreCase("211")) {
            isBankJateng = true;
        }

        return isBankJateng;
    }

    public String getUserIDSignAppWho() {

        String NIPPimpinan = getStApprovedWho();

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        UserSessionView user = getUser(stApprovedWho);

        String useridSign = NIPPimpinan;

        if (!NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
            if (user.getStBranch() != null) {
                if (user.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                    if (user.hasSignAuthority()) {
                        useridSign = user.getStUserID();
                    }
                }
            }
        }

        return useridSign;
    }

    public String getUserIDSignNameAppWho() {

        String NIPPimpinan = getStApprovedWho();

        if (stApprovedWho == null) {
            stApprovedWho = NIPPimpinan;
        }

        UserSessionView user = getUser(stApprovedWho);
        UserSessionView userPimpinan = getUser(NIPPimpinan);

        String useridSignName = userPimpinan.getStUserName();


        if (!NIPPimpinan.equalsIgnoreCase(stApprovedWho)) {
            if (user.getStBranch() != null) {
                if (user.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                    if (user.hasSignAuthority()) {
                        useridSignName = user.getStUserName();
                    }
                }
            }
        }

        return useridSignName.toUpperCase();
    }

    public BigDecimal getTotalSubrogasi() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select sum(claim_ded_amount) from ins_policy "
                    + "where dla_no in (select dla_no from ins_policy "
                    + "where status = 'CLAIM ENDORSE' and claim_status = 'DLA' "
                    + "and pla_no = ? order by pol_id ) "
                    + "and active_flag = 'Y' " //and effective_flag = 'Y' "
                    + "and date_trunc('day',dla_date) <= ? ");

            S.setParam(1, stPLANo);
            S.setParam(2, dtDLADate);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getKlaimBefore() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select sum(coalesce(claim_ded_amount,0)) from ins_policy "
                    + "where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' "
                    + "and pla_no = ? "
                    + "and active_flag = 'Y' " //and effective_flag = 'Y' "
                    + "and date_trunc('day',dla_date) < ? ");

            S.setParam(1, stPLANo);
            S.setParam(2, DateUtil.getDateStr(dtDLADate, "yyyy-MM-dd"));

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getTotalDeductible() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select sum(coalesce(b.amount,0)) from ins_policy a "
                    + "inner join ins_pol_items b on b.pol_id = a.pol_id and b.ins_item_id = 47 "
                    + "where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' "
                    + "and pla_no = ? "
                    + "and active_flag = 'Y' "
                    + "and date_trunc('day',dla_date) < ? ");

            S.setParam(1, stPLANo);
            S.setParam(2, dtDLADate);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public boolean isStatusClaimInward() {
        return FinCodec.PolicyStatus.CLAIMINWARD.equalsIgnoreCase(getStCurrentStatus());
    }

    public void generateDLANoClaimInward() throws Exception {
        String custCategory = entity.getStCategory1();

        custCategory = getDigit(custCategory, 1);

        final String policyType2Digit = StringTools.leftPad(getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey =
                DateUtil.getMonth2Digit(getDtPolicyDate())
                + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) {
            throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");
        }

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);
        String orderNo = null;

        orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim" + policyType2Digit + month2Digit + year2Digit + ccCode, 1)), '0', 4);

        //  LKS/21/21/1108/519
        //  040320??1006000300
        //  12345678901234567890
        //  01234567890123456789

        /*
        stPLANo = "LKS/" + getDigit(policyType2Digit, 2) + "/" +
        ccCode + "/" + month2Digit + year2Digit + "/" +
        StringTools.leftPad(String.valueOf(IDFactory.createNumericID("claim", 1)), '0', 4);
         */

        stPLANo = "LKS/" + getDigit(policyType2Digit, 2) + "/"
                + ccCode + "/" + month2Digit + year2Digit + "/"
                + orderNo;

        stDLANo = "LKP/" + getDigit(policyType2Digit, 2) + "/"
                + ccCode + "/" + month2Digit + year2Digit + "/"
                + orderNo;

    }

    private DTOList reverse;

    public DTOList getReverse() throws Exception {
        if (reverse == null) {
            loadReverse();
        }
        return reverse;
    }

    public void setReverse(DTOList reverse) {
        this.reverse = reverse;
    }

    public void loadReverse() throws Exception {
        try {
            if (reverse == null) {
                reverse = ListUtil.getDTOListFromQuery(
                        "   select "
                        + "      *, reverse_date::character varying as reverse_time"
                        + "   from"
                        + "      ins_pol_reverse_history"
                        + "   where"
                        + "      pol_id = ? order by ins_pol_reverse_id ",
                        new Object[]{getStPolicyID()},
                        InsurancePolicyReverseHistoryView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cekDoubleNoLKP() throws Exception {

        //cek jika save lks
        if (getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)){
            if(stPLANo!=null){

                final SQLUtil S = new SQLUtil();

                    try {
                        final PreparedStatement PS = S.setQuery("select pla_no "+
                                                            " from ins_policy "+
                                                            " where pla_no = ? ");

                        PS.setString(1, stPLANo);

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()) {

                            final String plaNo = RS.getString("pla_no");

                            if (plaNo != null) {
                                    throw new RuntimeException("Sudah pernah input LKS dengan no yang sama");
                            }
                        }

                    } finally {
                        S.release();
                    }
            }
        }


        //cek jika save endorse klaim
        if (getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSECLAIM)){

            if(stDLANo!=null){

                final SQLUtil S = new SQLUtil();

                    try {
                        final PreparedStatement PS = S.setQuery("select dla_no "+
                                                            " from ins_policy "+
                                                            " where dla_no = ? ");

                        PS.setString(1, stDLANo);

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()) {

                            final String dlaNo = RS.getString("dla_no");

                            if (dlaNo != null) {
                                    throw new RuntimeException("Sudah pernah input LKP dengan no LKP yang sama");
                            }
                        }

                    } finally {
                        S.release();
                    }
            }
        }

    }

    public boolean isBankSumut() {

        boolean isBankSumut = false;

        if (getEntity().getStRef2().equalsIgnoreCase("202")) {
            isBankSumut = true;
        }

        return isBankSumut;
    }

    /**
     * @return the dbClaimSubroPaid
     */
    public BigDecimal getDbClaimSubroPaid() {
        return dbClaimSubroPaid;
    }

    /**
     * @param dbClaimSubroPaid the dbClaimSubroPaid to set
     */
    public void setDbClaimSubroPaid(BigDecimal dbClaimSubroPaid) {
        this.dbClaimSubroPaid = dbClaimSubroPaid;
    }

    public boolean isPolisH2H() {
        return "1".equalsIgnoreCase(stDataSourceID);
    }

    public boolean isDataInterkoneksi() {
        return "2".equalsIgnoreCase(stDataSourceID);
    }

    private String stJumlahKlaim;

    /**
     * @return the stJumlahKlaim
     */
    public String getStJumlahKlaim() {
        return stJumlahKlaim;
    }

    /**
     * @param stJumlahKlaim the stJumlahKlaim to set
     */
    public void setStJumlahKlaim(String stJumlahKlaim) {
        this.stJumlahKlaim = stJumlahKlaim;
    }

    public void setPeriods(int year) {
        setDbPeriodRate(new BigDecimal(100));
        setStPeriodBaseID("2");
        setStPremiumFactorID("1");

        if(year>1){
            BigDecimal factorPengkali = BDUtil.mul(new BigDecimal(year), BDUtil.hundred);

            setDbPeriodRate(factorPengkali);
        }
    }

    public DTOList getReinsInvoices() {
        loadReinsInvoices();
        return reinsInvoices;
    }

    public void setReinsInvoices(DTOList reinsInvoices) {
        this.reinsInvoices = reinsInvoices;
    }

    private void loadReinsInvoices() {
        try {

            String trx_type_id = "13";

            if (isStatusClaim() || isStatusClaimEndorse()) {
                //trx_type_id = "12";
            }

            reinsInvoices = ListUtil.getDTOListFromQuery(
                    "      select "
                    + "         a.*,"
                    + "         ("
                    + "            select max(b.receipt_date) from ar_receipt_lines c, ar_receipt b\n"
                    + "            where c.ar_invoice_id=a.ar_invoice_id and c.receipt_id=b.ar_receipt_id"
                    + "         ) as payment_date, b.ent_name as reins_name"
                    + "      from "
                    + "         ar_invoice   a left join ent_master b on a.ent_id = b.ent_id"
                    + "      where "
                    + "         coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id = ? and attr_pol_id = ? order by a.ar_invoice_id",
                    new Object[]{trx_type_id, getStPolicyID()},
                    ARInvoiceView.class);

        } catch (NullPointerException e) {

            reinsInvoices = new DTOList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EntityView getEntityByID(String stEntID) {

        if (entity == null) {
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);
        }

        return entity;
    }

    public boolean isStatusEndorseClaimInward() {
        return FinCodec.PolicyStatus.ENDORSECLAIMINWARD.equalsIgnoreCase(getStCurrentStatus());
    }

    public DTOList getObjectsProcess() {
        loadObjectsProcess();
        return objects;
    }

    public void loadObjectsProcess() {
        try {
            getClObjectClass();
            if (objects == null) {
                objects = ListUtil.getDTOListFromQuery(
                        "select * from " + DTOCache.getTableName(clObjectClass) + " where pol_id  = ? and status is null order by ins_pol_obj_id asc",
                        new Object[]{stPolicyID},
                        clObjectClass);

                for (int i = 0; i < objects.size(); i++) {
                    InsuranceSplitPolicyObjectView obj = (InsuranceSplitPolicyObjectView) objects.get(i);

                    obj.setStPolicyID(this.getStPolicyID());
                    obj.setPolicy(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

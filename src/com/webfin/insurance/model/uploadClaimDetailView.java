/***********************************************************************
 * Module:  com.crux.login.model.uploadEndorsemenView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.UserSession;
import com.crux.file.FileView;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.UserLogView;
import com.crux.login.model.UserRoleView;
import com.webfin.gl.model.GLCostCenterView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.system.region.model.RegionView;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

public class uploadClaimDetailView extends DTO implements RecordAudit
{
    private HashMap attributes = new HashMap();
    
    public void setAttribute(String x, Object o)
    {
        attributes.put(x,o);
    }
    
    public Object getAttribute(String x)
    {
        return attributes.get(x);
    }

    /*
    CREATE TABLE ins_upload_claim_detail
(
  ins_upload_dtl_id bigint NOT NULL,
  ins_upload_id bigint,
  recap_no character varying(255),
  file_id character varying(255),
  pol_id bigint,
  ins_pol_obj_id bigint,
  pol_no character varying(32),
  order_no bigint,
  description character varying(255),
  file_physic bigint,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  tgl_pengajuan_klaim timestamp without time zone,
  tgl_klaim timestamp without time zone,
  tgl_lks timestamp without time zone,
  claim_cause_id bigint,
  claim_event_location text,
  claim_person_name character varying(255),
  claim_person_address character varying(255),
  claim_person_status character varying(255),
  claim_loss_id bigint,
  claim_benefit bigint,
  claim_document character varying(255),
  claim_chronology text,
  claim_ccy character varying(32),
  claim_ccy_rate numeric,
  claim_amount_estimate numeric,
  approved_who character varying(32),
  status character varying(32),
  CONSTRAINT ins_upload_claim_detail_pkey PRIMARY KEY (ins_upload_dtl_id)
)
*/
    public static String tableName = "ins_upload_claim_detail";
    
    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_upload_dtl_id*pk*nd"},
        {"stInsuranceUploadID", "ins_upload_id"},
        {"stPolicyID", "pol_id"},
        {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
        {"stPolicyNo", "pol_no"},
        {"stOrderNo", "order_no"},
        {"stDescription", "description"},
        {"dtTanggalPengajuanKlaim", "tgl_pengajuan_klaim"},
        {"dtTanggalKlaim", "tgl_klaim"},
        {"dtTanggalLKS", "tgl_lks"},
        {"stClaimCauseID", "claim_cause_id"},
        {"stClaimEventLocation", "claim_event_location"},
        {"stClaimPersonName", "claim_person_name"},
        {"stClaimPersonAddress", "claim_person_address"},
        {"stClaimPersonStatus", "claim_person_status"},
        {"stClaimLossID", "claim_loss_id"},
        {"stClaimBenefit", "claim_benefit"},
        {"stClaimDocument", "claim_document"},
        {"stClaimChronology", "claim_chronology"},
        {"stClaimCurrency", "claim_ccy"},
        {"dbClaimCurrencyRate", "claim_ccy_rate"},
        {"dbClaimAmountEstimate", "claim_amount_estimate"},
        {"dbClaimDeductibleAmount", "deductible_amount"},
        {"stStatus", "status"},
        {"stApprovedWho", "approved_who"},
        {"stFilePhysic", "file_physic"},
        {"stPotensiSubrogasi", "potensi_subrogasi"},
        {"stClaimSubCauseID", "claim_sub_cause_id"},
        {"dbSurveyCostAmount", "survey_cost_amount"},
        {"stProcessType", "process_type"},
        {"stSurveyorEntID", "surveyor_ent_id"},
        {"stSurveyNotes", "survey_notes"},
        {"stRejectNotes", "reject_notes"},
        {"dbPPNAmount", "ppn_amount"},

    };

    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stPolicyID;
    private String stInsurancePolicyObjectID;
    private String stPolicyNo;
    private String stOrderNo;
    private String stDescription;
    private Date dtTanggalPengajuanKlaim;
    private Date dtTanggalKlaim;
    private Date dtTanggalLKS;
    private String stClaimCauseID;
    private String stClaimEventLocation;
    private String stClaimPersonName;
    private String stClaimPersonAddress;
    private String stClaimPersonStatus;
    private String stClaimLossID;
    private String stClaimBenefit;
    private String stClaimDocument;
    private String stClaimChronology;
    private String stClaimCurrency;
    private BigDecimal dbClaimCurrencyRate;
    private BigDecimal dbClaimAmountEstimate;
    private BigDecimal dbClaimDeductibleAmount;
    private String stStatus;
    private String stApprovedWho;
    private String stFilePhysic;
    private String stPotensiSubrogasi;
    private String stClaimSubCauseID;

    private BigDecimal dbSurveyCostAmount;
    private String stProcessType;
    private String stSurveyorEntID;
    private String stSurveyNotes;
    private String stRejectNotes;
    private BigDecimal dbPPNAmount;

    public BigDecimal getDbPPNAmount() {
        return dbPPNAmount;
    }

    public void setDbPPNAmount(BigDecimal dbPPNAmount) {
        this.dbPPNAmount = dbPPNAmount;
    }

    public String getStRejectNotes() {
        return stRejectNotes;
    }

    public void setStRejectNotes(String stRejectNotes) {
        this.stRejectNotes = stRejectNotes;
    }

    public String getStSurveyNotes() {
        return stSurveyNotes;
    }

    public void setStSurveyNotes(String stSurveyNotes) {
        this.stSurveyNotes = stSurveyNotes;
    }

    public String getStSurveyorEntID() {
        return stSurveyorEntID;
    }

    public void setStSurveyorEntID(String stSurveyorEntID) {
        this.stSurveyorEntID = stSurveyorEntID;
    }

    public BigDecimal getDbSurveyCostAmount() {
        return dbSurveyCostAmount;
    }

    public void setDbSurveyCostAmount(BigDecimal dbSurveyCostAmount) {
        this.dbSurveyCostAmount = dbSurveyCostAmount;
    }

    public String getStProcessType() {
        return stProcessType;
    }

    public void setStProcessType(String stProcessType) {
        this.stProcessType = stProcessType;
    }

    public String getStClaimSubCauseID() {
        return stClaimSubCauseID;
    }

    public void setStClaimSubCauseID(String stClaimSubCauseID) {
        this.stClaimSubCauseID = stClaimSubCauseID;
    }

    public String getStPotensiSubrogasi() {
        return stPotensiSubrogasi;
    }

    public void setStPotensiSubrogasi(String stPotensiSubrogasi) {
        this.stPotensiSubrogasi = stPotensiSubrogasi;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public String getStClaimLossID() {
        return stClaimLossID;
    }

    public void setStClaimLossID(String stClaimLossID) {
        this.stClaimLossID = stClaimLossID;
    }

    public BigDecimal getDbClaimAmountEstimate() {
        return dbClaimAmountEstimate;
    }

    public void setDbClaimAmountEstimate(BigDecimal dbClaimAmountEstimate) {
        this.dbClaimAmountEstimate = dbClaimAmountEstimate;
    }

    public BigDecimal getDbClaimCurrencyRate() {
        return dbClaimCurrencyRate;
    }

    public void setDbClaimCurrencyRate(BigDecimal dbClaimCurrencyRate) {
        this.dbClaimCurrencyRate = dbClaimCurrencyRate;
    }

    public Date getDtTanggalKlaim() {
        return dtTanggalKlaim;
    }

    public void setDtTanggalKlaim(Date dtTanggalKlaim) {
        this.dtTanggalKlaim = dtTanggalKlaim;
    }

    public Date getDtTanggalLKS() {
        return dtTanggalLKS;
    }

    public void setDtTanggalLKS(Date dtTanggalLKS) {
        this.dtTanggalLKS = dtTanggalLKS;
    }

    public Date getDtTanggalPengajuanKlaim() {
        return dtTanggalPengajuanKlaim;
    }

    public void setDtTanggalPengajuanKlaim(Date dtTanggalPengajuanKlaim) {
        this.dtTanggalPengajuanKlaim = dtTanggalPengajuanKlaim;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    public String getStClaimBenefit() {
        return stClaimBenefit;
    }

    public void setStClaimBenefit(String stClaimBenefit) {
        this.stClaimBenefit = stClaimBenefit;
    }

    public String getStClaimCauseID() {
        return stClaimCauseID;
    }

    public void setStClaimCauseID(String stClaimCauseID) {
        this.stClaimCauseID = stClaimCauseID;
    }

    public String getStClaimChronology() {
        return stClaimChronology;
    }

    public void setStClaimChronology(String stClaimChronology) {
        this.stClaimChronology = stClaimChronology;
    }

    public String getStClaimCurrency() {
        return stClaimCurrency;
    }

    public void setStClaimCurrency(String stClaimCurrency) {
        this.stClaimCurrency = stClaimCurrency;
    }

    public String getStClaimDocument() {
        return stClaimDocument;
    }

    public void setStClaimDocument(String stClaimDocument) {
        this.stClaimDocument = stClaimDocument;
    }

    public String getStClaimEventLocation() {
        return stClaimEventLocation;
    }

    public void setStClaimEventLocation(String stClaimEventLocation) {
        this.stClaimEventLocation = stClaimEventLocation;
    }

    public String getStClaimPersonAddress() {
        return stClaimPersonAddress;
    }

    public void setStClaimPersonAddress(String stClaimPersonAddress) {
        this.stClaimPersonAddress = stClaimPersonAddress;
    }

    public String getStClaimPersonName() {
        return stClaimPersonName;
    }

    public void setStClaimPersonName(String stClaimPersonName) {
        this.stClaimPersonName = stClaimPersonName;
    }

    public String getStClaimPersonStatus() {
        return stClaimPersonStatus;
    }

    public void setStClaimPersonStatus(String stClaimPersonStatus) {
        this.stClaimPersonStatus = stClaimPersonStatus;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStInsurancePolicyObjectID() {
        return stInsurancePolicyObjectID;
    }

    public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
        this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
    }

    public String getStInsuranceUploadDetailID() {
        return stInsuranceUploadDetailID;
    }

    public void setStInsuranceUploadDetailID(String stInsuranceUploadDetailID) {
        this.stInsuranceUploadDetailID = stInsuranceUploadDetailID;
    }

    public String getStInsuranceUploadID() {
        return stInsuranceUploadID;
    }

    public void setStInsuranceUploadID(String stInsuranceUploadID) {
        this.stInsuranceUploadID = stInsuranceUploadID;
    }

    public String getStOrderNo() {
        return stOrderNo;
    }

    public void setStOrderNo(String stOrderNo) {
        this.stOrderNo = stOrderNo;
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

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public BigDecimal getDbClaimDeductibleAmount() {
        return dbClaimDeductibleAmount;
    }

    public void setDbClaimDeductibleAmount(BigDecimal dbClaimDeductibleAmount) {
        this.dbClaimDeductibleAmount = dbClaimDeductibleAmount;
    }

    public String getEntityID(String stKodeBank) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ent_id " +
                    "   from " +
                    "         ent_master " +
                    "   where" +
                    "      ref_gateway_code = ?");

            S.setParam(1,stKodeBank);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public InsuranceClaimLossView getClaimLoss() {
        return (InsuranceClaimLossView) DTOPool.getInstance().getDTO(InsuranceClaimLossView.class, getStClaimLossID());
    }

    public boolean isDLAApprovedProcess(){
        return getStProcessType().equalsIgnoreCase("DLA_APPROVED")?true:false;
    }

    public boolean isDLAEndorseProcess(){
        return getStProcessType().equalsIgnoreCase("DLA_ENDORSE_APPROVED")?true:false;
    }

    public boolean isDLARejectProcess(){
        return getStProcessType().equalsIgnoreCase("DLA_REJECT")?true:false;
    }

    public boolean isPLAApprovedProcess(){
        return getStProcessType().equalsIgnoreCase("PLA_APPROVED")?true:false;
    }

}

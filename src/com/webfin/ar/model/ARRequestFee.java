/***********************************************************************
 * Module:  com.webfin.ar.model.ARRequestFee
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.RecordBackup;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.model.AccountView2;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.BiayaOperasionalDetail;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.master.division.model.DivisionView;
import com.webfin.system.region.model.RegionView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ARRequestFee extends DTO implements RecordAudit, RecordBackup {

    private final static transient LogManager logger = LogManager.getInstance(ARRequestFee.class);
    public static String tableName = "ar_request_fee";
    public static String fieldMap[][] = {
        {"stARRequestID", "req_id*pk"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stTransactionNo", "trans_no"},
        {"stRequestNo", "request_no"},
        {"stAccountID", "accountid"},
        {"stAccountNo", "accountno"},
        {"stAccountDesc", "accountdesc"},
        {"stRKAPGroupID", "rkapgroupid"},
        {"stBiaopGroupID", "biaopgroupid"},
        {"stBiaopTypeID", "biaoptypeid"},
        {"dtTglRequest", "request_date"},
        {"StYears", "years"},
        {"StMonths", "months"},
        {"stCurrency", "ccy"},
        {"dbCurrencyRate", "ccy_rate"},
        {"stDescription", "description"},
        {"stDescriptionApproved", "description_app"},
        {"dbNominal", "nominal"},
        {"dbNominalUsed", "nominal_used"},
        {"dbNominalBack", "nominal_back"},
        {"stDeleted", "deleted"},
        {"stActiveFlag", "act_flag"},
        {"stEffectiveFlag", "eff_flag"},
        {"stCashierFlag", "cashier_flag"},
        {"stStatus", "status"},
        {"stApprovedWho", "approved_who"},
        {"dtApprovedDate", "approved_date"},
        {"stParentID", "parent_id"},
        {"stCashierWho", "cashier_who"},
        {"dtCashierDate", "cashier_date"},
        {"stCreateName", "create_name*n"},
        {"stApprovedName", "approved_name*n"},
        {"stCashierName", "cashier_name*n"},
        {"stPrintFlag", "print_flag"},
        {"stReceiptClassID", "rc_id"},
        {"stPilihan", "pilihan"},
        {"stAccountIDChoice", "account_id_choice"},
        {"stPolicyClaimNo", "policy_claim_no"},
        {"stDocuments", "documents"},
        {"stKwitansi", "kwitansi"},
        {"dtTglCashback", "cashback_date"},
        {"stProposalID", "proposalid"},
        {"stNoSuratHutang", "no_surat_hutang"},
        {"stRootID", "root_id"},
        {"stAnggaranType", "anggaran_type"},
        {"stValidasiF", "validasi_f"},
        {"stCashflowF", "cashflow_f"},
        {"stCashflowWho", "cashflow_who"},
        {"dtCashflowDate", "cashflow_date"},
        {"dtCashflowStart", "cashflow_start"},
        {"dtCashflowEnd", "cashflow_end"},};
    private String stARRequestID;
    private String stCostCenterCode;
    private String stRegionID;
    private String stTransactionNo;
    private String stRequestNo;
    private String stAccountID;
    private String stRKAPGroupID;
    private String stBiaopGroupID;
    private String stBiaopTypeID;
    private Date dtTglRequest;
    private String StYears;
    private String StMonths;
    private String stCurrency;
    private BigDecimal dbCurrencyRate;
    private String stDescription;
    private String stDescriptionApproved;
    private BigDecimal dbNominal;
    private BigDecimal dbNominalUsed;
    private BigDecimal dbNominalBack;
    private String stDeleted;
    private String stActiveFlag;
    private String stEffectiveFlag;
    private String stCashierFlag;
    private String stStatus;
    private String stApprovedWho;
    private Date dtApprovedDate;
    private String stAccountNo;
    private String stAccountDesc;
    private String stNextStatus;
    private String stParentID;
    private String stCreateName;
    private String stApprovedName;
    private String stCashierName;
    private String stCashierWho;
    private Date dtCashierDate;
    private String stPrintFlag;
    private String stReceiptClassID;
    private String stPilihan;
    private DTOList policyDocuments;
    private DTOList documents;
    private String stAccountIDChoice;
    private String stPolicyClaimNo;
    private String stKwitansi;
    private String stDocuments;
    private Date dtTglCashback;
    private String stProposalID;
    private String stNoSuratHutang;
    private String stRootID;
    private String stAnggaranType;
    private String stValidasiF;
    private String stCashflowF;
    private String stCashflowWho;
    private Date dtCashflowDate;
    private Date dtCashflowStart;
    private Date dtCashflowEnd;
    private boolean ownerPms = SessionManager.getInstance().getSession().hasResource("REQ_PMS");
    private boolean ownerUmum = SessionManager.getInstance().getSession().hasResource("REQ_UMUM");
    private boolean ownerAdm = SessionManager.getInstance().getSession().hasResource("REQ_ADM");

    /**
     * @return the stARRequestID
     */
    public String getStARRequestID() {
        return stARRequestID;
    }

    /**
     * @param stARRequestID the stARRequestID to set
     */
    public void setStARRequestID(String stARRequestID) {
        this.stARRequestID = stARRequestID;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stRegionID
     */
    public String getStRegionID() {
        return stRegionID;
    }

    /**
     * @param stRegionID the stRegionID to set
     */
    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the stTransactionNo
     */
    public String getStTransactionNo() {
        return stTransactionNo;
    }

    /**
     * @param stTransactionNo the stTransactionNo to set
     */
    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }

    /**
     * @return the dtTglRequest
     */
    public Date getDtTglRequest() {
        return dtTglRequest;
    }

    /**
     * @param dtTglRequest the dtTglRequest to set
     */
    public void setDtTglRequest(Date dtTglRequest) {
        this.dtTglRequest = dtTglRequest;
    }

    /**
     * @return the StYears
     */
    public String getStYears() {
        return StYears;
    }

    /**
     * @param StYears the StYears to set
     */
    public void setStYears(String StYears) {
        this.StYears = StYears;
    }

    /**
     * @return the StMonths
     */
    public String getStMonths() {
        return StMonths;
    }

    /**
     * @param StMonths the StMonths to set
     */
    public void setStMonths(String StMonths) {
        this.StMonths = StMonths;
    }

    /**
     * @return the stCurrency
     */
    public String getStCurrency() {
        return stCurrency;
    }

    /**
     * @param stCurrency the stCurrency to set
     */
    public void setStCurrency(String stCurrency) {
        this.stCurrency = stCurrency;
    }

    /**
     * @return the dbCurrencyRate
     */
    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }

    /**
     * @param dbCurrencyRate the dbCurrencyRate to set
     */
    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
    }

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    /**
     * @return the dbNominal
     */
    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    /**
     * @param dbNominal the dbNominal to set
     */
    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    /**
     * @return the stDeleted
     */
    public String getStDeleted() {
        return stDeleted;
    }

    /**
     * @param stDeleted the stDeleted to set
     */
    public void setStDeleted(String stDeleted) {
        this.stDeleted = stDeleted;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stEffectiveFlag
     */
    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    /**
     * @param stEffectiveFlag the stEffectiveFlag to set
     */
    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public boolean isEffective() {
        return Tools.isYes(stEffectiveFlag);
    }

    public boolean isActive() {
        return Tools.isYes(stActiveFlag);
    }
    private boolean Posted;

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if (getStCostCenterCode() != null) {
                cek = cek + " and cc_code = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStMonths());
            PS.setString(2, getStYears());

            if (getStCostCenterCode() != null) {
                PS.setString(3, getStCostCenterCode());


            }
            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public void setPosted(boolean Posted) {
        this.Posted = Posted;
    }
    private DTOList details;

    public DTOList getDetails(String ARReqID) {
        loadDetails(ARReqID);
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }

    public void loadDetails(String ARReqID) {
        try {
            if (details == null) {
                details = ListUtil.getDTOListFromQuery(
                        "select * from ar_request_fee where req_id = ? ",
                        new Object[]{ARReqID},
                        ARRequestFee.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stStatus
     */
    public String getStStatus() {
        return stStatus;
    }

    /**
     * @param stStatus the stStatus to set
     */
    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    /**
     * @return the stApprovedWho
     */
    public String getStApprovedWho() {
        return stApprovedWho;
    }

    /**
     * @param stApprovedWho the stApprovedWho to set
     */
    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    /**
     * @return the dtApprovedDate
     */
    public Date getDtApprovedDate() {
        return dtApprovedDate;
    }

    /**
     * @param dtApprovedDate the dtApprovedDate to set
     */
    public void setDtApprovedDate(Date dtApprovedDate) {
        this.dtApprovedDate = dtApprovedDate;
    }

    /**
     * @return the stAccountID
     */
    public String getStAccountID() {
        return stAccountID;
    }

    /**
     * @param stAccountID the stAccountID to set
     */
    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    /**
     * @return the stRKAPGroupID
     */
    public String getStRKAPGroupID() {
        return stRKAPGroupID;
    }

    /**
     * @param stRKAPGroupID the stRKAPGroupID to set
     */
    public void setStRKAPGroupID(String stRKAPGroupID) {
        this.stRKAPGroupID = stRKAPGroupID;
    }

    /**
     * @return the stBiaopGroupID
     */
    public String getStBiaopGroupID() {
        return stBiaopGroupID;
    }

    /**
     * @param stBiaopGroupID the stBiaopGroupID to set
     */
    public void setStBiaopGroupID(String stBiaopGroupID) {
        this.stBiaopGroupID = stBiaopGroupID;
    }

    /**
     * @return the stBiaopTypeID
     */
    public String getStBiaopTypeID() {
        return stBiaopTypeID;
    }

    /**
     * @param stBiaopTypeID the stBiaopTypeID to set
     */
    public void setStBiaopTypeID(String stBiaopTypeID) {
        this.stBiaopTypeID = stBiaopTypeID;
    }

    /**
     * @return the stAccountNo
     */
    public String getStAccountNo() {
        return stAccountNo;
    }

    /**
     * @param stAccountNo the stAccountNo to set
     */
    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    /**
     * @return the stAccountDesc
     */
    public String getStAccountDesc() {
        return stAccountDesc;
    }

    /**
     * @param stAccountDesc the stAccountDesc to set
     */
    public void setStAccountDesc(String stAccountDesc) {
        this.stAccountDesc = stAccountDesc;
    }

    public RegionView getRegion() {
        final RegionView region = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegionID);

        return region;
    }

    public BiayaOperasionalDetail getBiaopDetil() {
        final BiayaOperasionalDetail dtl = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, stBiaopTypeID);

        return dtl;
    }

    public void generateNoRequest() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        final String ccReg = getStRegionID();
//        final String ccReg = Tools.getDigitRightJustified(getRegion().getStRegionCode(), 2);

        String pilihan = "P";
//        if ("1".equalsIgnoreCase(getStAnggaranType())) {
//            pilihan = "PK";
//        } else {
//            pilihan = "BR";
//        }

        String counterKey =
                DateUtil.getYear2Digit(getDtTglRequest())
                + DateUtil.getMonth2Digit(getDtTglRequest());

        String rn = String.valueOf(IDFactory.createNumericID("REQNO" + counterKey + ccCode, 1));

        rn = StringTools.leftPad(rn, '0', 5);

        stRequestNo =
                pilihan
                + counterKey
                + ccCode
                + ccReg
                + rn
                + "00";
    }

    public String getStNextStatus() {
        return stNextStatus;
    }

    public void setStNextStatus(String stNextStatus) {
        this.stNextStatus = stNextStatus;
    }

    private String getStCurrentStatus() {
        return stNextStatus == null ? stStatus : stNextStatus;
    }

    public boolean isStatusRequest() {
        return "REQ".equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusApproval() {
        return "APP".equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusCashback() {
        return "CSB".equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusRefund() {
        return "RFD".equalsIgnoreCase(getStCurrentStatus());
    }

    /**
     * @return the stRequestNo
     */
    public String getStRequestNo() {
        return stRequestNo;
    }

    /**
     * @param stRequestNo the stRequestNo to set
     */
    public void setStRequestNo(String stRequestNo) {
        this.stRequestNo = stRequestNo;
    }

    /**
     * @return the dbNominalUsed
     */
    public BigDecimal getDbNominalUsed() {
        return dbNominalUsed;
    }

    /**
     * @param dbNominalUsed the dbNominalUsed to set
     */
    public void setDbNominalUsed(BigDecimal dbNominalUsed) {
        this.dbNominalUsed = dbNominalUsed;
    }

    public void generateNoTransaction() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);

        String rcid = getStReceiptClassID();

        String counterKey =
                DateUtil.getYear2Digit(getDtTglRequest())
                + DateUtil.getMonth2Digit(getDtTglRequest());

        String glcode = getAccounts().getStAccountNo().substring(5, 10);

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H

        rn = StringTools.leftPad(rn, '0', 5);

        stTransactionNo =
                rcid
                + counterKey
                + ccCode
                + ccCode
                + glcode
                + rn;

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
    }

    /**
     * @return the stParentID
     */
    public String getStParentID() {
        return stParentID;
    }

    /**
     * @param stParentID the stParentID to set
     */
    public void setStParentID(String stParentID) {
        this.stParentID = stParentID;
    }

    /**
     * @return the stCreateName
     */
    public String getStCreateName() {
        return stCreateName;
    }

    /**
     * @param stCreateName the stCreateName to set
     */
    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    /**
     * @return the stApprovedName
     */
    public String getStApprovedName() {
        return stApprovedName;
    }

    /**
     * @param stApprovedName the stApprovedName to set
     */
    public void setStApprovedName(String stApprovedName) {
        this.stApprovedName = stApprovedName;
    }

    /**
     * @return the stCashierFlag
     */
    public String getStCashierFlag() {
        return stCashierFlag;
    }

    /**
     * @param stCashierFlag the stCashierFlag to set
     */
    public void setStCashierFlag(String stCashierFlag) {
        this.stCashierFlag = stCashierFlag;
    }

    /**
     * @return the stCashierWho
     */
    public String getStCashierWho() {
        return stCashierWho;
    }

    /**
     * @param stCashierWho the stCashierWho to set
     */
    public void setStCashierWho(String stCashierWho) {
        this.stCashierWho = stCashierWho;
    }

    /**
     * @return the dtCashierDate
     */
    public Date getDtCashierDate() {
        return dtCashierDate;
    }

    /**
     * @param dtCashierDate the dtCashierDate to set
     */
    public void setDtCashierDate(Date dtCashierDate) {
        this.dtCashierDate = dtCashierDate;
    }

    /**
     * @return the stCashierName
     */
    public String getStCashierName() {
        return stCashierName;
    }

    /**
     * @param stCashierName the stCashierName to set
     */
    public void setStCashierName(String stCashierName) {
        this.stCashierName = stCashierName;
    }

    public boolean isCashierFlag() {
        return Tools.isYes(stCashierFlag);
    }

    /**
     * @return the stDescriptionApproved
     */
    public String getStDescriptionApproved() {
        return stDescriptionApproved;
    }

    /**
     * @param stDescriptionApproved the stDescriptionApproved to set
     */
    public void setStDescriptionApproved(String stDescriptionApproved) {
        this.stDescriptionApproved = stDescriptionApproved;
    }

    /**
     * @return the dbNominalBack
     */
    public BigDecimal getDbNominalBack() {
        return dbNominalBack;
    }

    /**
     * @param dbNominalBack the dbNominalBack to set
     */
    public void setDbNominalBack(BigDecimal dbNominalBack) {
        this.dbNominalBack = dbNominalBack;
    }

    /**
     * @return the stPrintFlag
     */
    public String getStPrintFlag() {
        return stPrintFlag;
    }

    /**
     * @param stPrintFlag the stPrintFlag to set
     */
    public void setStPrintFlag(String stPrintFlag) {
        this.stPrintFlag = stPrintFlag;
    }

    public UserSessionView getUser(String userID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, userID);
    }

    /**
     * @return the stReceiptClassID
     */
    public String getStReceiptClassID() {
        return stReceiptClassID;
    }

    /**
     * @param stReceiptClassID the stReceiptClassID to set
     */
    public void setStReceiptClassID(String stReceiptClassID) {
        this.stReceiptClassID = stReceiptClassID;
    }

    public ARReceiptClassView getReceiptClass() {
        return (ARReceiptClassView) (stReceiptClassID == null ? null : DTOPool.getInstance().getDTO(ARReceiptClassView.class, stReceiptClassID));
    }

    public AccountView2 getAccounts() {
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stAccountID);
    }

    public DTOList getPolicyDocuments() {
        if (policyDocuments == null && stARRequestID != null) {
            policyDocuments = loadPolicyDocuments(stARRequestID, "APPROVED");
        }

        return policyDocuments;
    }

    private static DTOList loadPolicyDocuments(String stARRequestID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                    "   select b.*,c.description,a.ins_document_type_id "
                    + " from ins_documents a "
                    + " inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id "
                    + " left join ins_pol_documents b on  b.document_class=a.document_class and b.policy_id=? and b.ins_document_type_id=a.ins_document_type_id "
                    + " where a.document_class=? ",
                    new Object[]{stARRequestID, documentClass},
                    InsurancePolicyDocumentView.class);

            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyDocumentView docs = (InsurancePolicyDocumentView) l.get(i);

                if (docs.getStInsurancePolicyDocumentID() != null) {
                    docs.setStSelectedFlag("Y");
                }

                docs.setStPolicyID(stARRequestID);
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

    public DTOList getDocuments() {
        loadDocuments();
        return documents;
    }

    private void loadDocuments() {
        try {
            if (documents == null) {
                documents = ListUtil.getDTOListFromQuery(
                        "select * from ar_request_documents where in_id = ? and delete_flag is null ",
                        new Object[]{stARRequestID},
                        ARRequestDocumentsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocuments(DTOList documents) {
        this.documents = documents;
    }

    public UserSessionView getUserApproved() {

        final UserSessionView user = (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stApprovedWho);

        return user;

    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return costcenter;
    }

    public boolean isPrintFlag() {
        return Tools.isYes(stPrintFlag);
    }

    /**
     * @return the stAccountIDChoice
     */
    public String getStAccountIDChoice() {
        return stAccountIDChoice;
    }

    /**
     * @param stAccountIDChoice the stAccountIDChoice to set
     */
    public void setStAccountIDChoice(String stAccountIDChoice) {
        this.stAccountIDChoice = stAccountIDChoice;
    }

    /**
     * @return the stPolicyClaimNo
     */
    public String getStPolicyClaimNo() {
        return stPolicyClaimNo;
    }

    /**
     * @param stPolicyClaimNo the stPolicyClaimNo to set
     */
    public void setStPolicyClaimNo(String stPolicyClaimNo) {
        this.stPolicyClaimNo = stPolicyClaimNo;
    }

    /**
     * @return the stKwitansi
     */
    public String getStKwitansi() {
        return stKwitansi;
    }

    /**
     * @param stKwitansi the stKwitansi to set
     */
    public void setStKwitansi(String stKwitansi) {
        this.stKwitansi = stKwitansi;
    }

    /**
     * @return the stDocument
     */
    public String getStDocuments() {
        return stDocuments;
    }

    /**
     * @param stDocument the stDocument to set
     */
    public void setStDocuments(String stDocuments) {
        this.stDocuments = stDocuments;
    }

    /**
     * @return the dtTglCashback
     */
    public Date getDtTglCashback() {
        return dtTglCashback;
    }

    /**
     * @param dtTglCashback the dtTglCashback to set
     */
    public void setDtTglCashback(Date dtTglCashback) {
        this.dtTglCashback = dtTglCashback;
    }

    /**
     * @return the stNoSuratHutang
     */
    public String getStNoSuratHutang() {
        return stNoSuratHutang;
    }

    /**
     * @param stNoSuratHutang the stNoSuratHutang to set
     */
    public void setStNoSuratHutang(String stNoSuratHutang) {
        this.stNoSuratHutang = stNoSuratHutang;
    }

    /**
     * @return the stProposalID
     */
    public String getStProposalID() {
        return stProposalID;
    }

    /**
     * @param stProposalID the stProposalID to set
     */
    public void setStProposalID(String stProposalID) {
        this.stProposalID = stProposalID;
    }
//    public ARRequestDocumentsView getParentObject(String parentObjectID) {
//        return (ARRequestDocumentsView) DTOPool.getInstance().getDTO(ARRequestDocumentsView.class, parentObjectID);
//    }
    private DTOList reqObject;

    /**
     * @return the reqObject
     */
    public DTOList getReqObject() {
        loadReqObject();
        return reqObject;
    }

    /**
     * @param reqObject the reqObject to set
     */
    public void setReqObject(DTOList reqObject) {
        this.reqObject = reqObject;
    }

    public void loadReqObject() {
        try {
            if (reqObject == null) {
                reqObject = ListUtil.getDTOListFromQuery(
                        "select * from ar_request_fee_obj where req_id = ? and delete_flag is null ",
                        new Object[]{stARRequestID},
                        ARRequestFeeObj.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String findAccountUangMuka(String stCostCenterCode) throws Exception {
        String bunga = new String("172900000000 " + stCostCenterCode);

        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where cc_code = ? and accountno = ?");

            PS.setString(1, stCostCenterCode);
            PS.setString(2, bunga);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

//    public void generateEndorseNo() throws Exception {
//
//        String rn = String.valueOf(IDFactory.createNumericID("REQ" + stRequestNo, 1));
//
//        rn = StringTools.leftPad(rn, '0', 2);
//
//        stRequestNo = stRequestNo + "-" + rn;
//    }
    public void generateEndorseNo() {

        final char[] policyno = stRequestNo.toCharArray();

        final String enos = stRequestNo.substring(15, 17);

        int eNo = Integer.parseInt(enos);

        eNo += 1;

        final String z = StringTools.leftPad(String.valueOf(eNo), '0', 2);

        final char[] ze = z.toCharArray();

        policyno[15] = ze[0];
        policyno[16] = ze[1];

        stRequestNo = new String(policyno);
    }

    public void checkRealisasiNoBefore(String stRequestNo) throws Exception {
        final DTOList root = getRootRequest();

        for (int i = 0; i < root.size(); i++) {
            ARRequestFee pol = (ARRequestFee) root.get(i);

            if (pol.getStRequestNo().equalsIgnoreCase(stRequestNo)) {
                throw new RuntimeException("No Request " + stRequestNo + " Sudah Pernah Dibuat");
            }

        }
    }
    private DTOList rootrequest;

    public DTOList getRootRequest() {
        loadRootRequest();
        return rootrequest;
    }

    public void loadRootRequest() {
        try {
            if (rootrequest == null) {
                rootrequest = ListUtil.getDTOListFromQuery(
                        "select * "
                        + "from ar_request_fee "
                        + "where status = 'REALISASI' and act_flag = 'Y' "
                        + "and root_id = ? ",
                        new Object[]{stRootID},
                        ARRequestFee.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stRootID
     */
    public String getStRootID() {
        return stRootID;
    }

    /**
     * @param stRootID the stRootID to set
     */
    public void setStRootID(String stRootID) {
        this.stRootID = stRootID;
    }

    /**
     * @return the stPilihan
     */
    public String getStPilihan() {
        return stPilihan;
    }

    /**
     * @param stPilihan the stPilihan to set
     */
    public void setStPilihan(String stPilihan) {
        this.stPilihan = stPilihan;
    }
    private DTOList reqKertaskerja;

    /**
     * @return the reqkertaskerja
     */
    public DTOList getReqKertaskerja() {
        loadReqKertaskerja();
        return reqKertaskerja;
    }

    /**
     * @param reqkertaskerja the reqkertaskerja to set
     */
    public void setReqKertaskerja(DTOList reqKertaskerja) {
        this.reqKertaskerja = reqKertaskerja;
    }

    public void loadReqKertaskerja() {
        try {
            if (reqKertaskerja == null) {
                reqKertaskerja = ListUtil.getDTOListFromQuery(
                        "select req_id,accountid2,sum(total_nilai) as total_nilai "
                        + "from ar_request_fee_obj where req_id = ? and delete_flag is null "
                        + "group by req_id,accountid2 ",
                        new Object[]{stARRequestID},
                        ARRequestFeeObj.class);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stAnggaranType
     */
    public String getStAnggaranType() {
        return stAnggaranType;
    }

    /**
     * @param stAnggaranType the stAnggaranType to set
     */
    public void setStAnggaranType(String stAnggaranType) {
        this.stAnggaranType = stAnggaranType;
    }

    public boolean isStatusProposal() {
        return "PENGAJUAN".equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isStatusRealized() {
        return "REALISASI".equalsIgnoreCase(getStCurrentStatus());
    }

    public boolean isProgramKerja() {
        return "1".equalsIgnoreCase(getStAnggaranType());
    }

    public boolean isBiayaRutin() {
        return "2".equalsIgnoreCase(getStAnggaranType());
    }

    public boolean isValidasiFlag() {
        return Tools.isYes(stValidasiF);
    }

    /**
     * @return the stValidasiF
     */
    public String getStValidasiF() {
        return stValidasiF;
    }

    /**
     * @param stValidasiF the stValidasiF to set
     */
    public void setStValidasiF(String stValidasiF) {
        this.stValidasiF = stValidasiF;
    }
    private DTOList approval;

    /**
     * @return the approval
     */
    public DTOList getApproval() {
        loadApproval();
        return approval;
    }

    /**
     * @param approval the approval to set
     */
    public void setApproval(DTOList approval) {
        this.approval = approval;
    }

    private void loadApproval() {
        try {
            if (approval == null) {
                approval = ListUtil.getDTOListFromQuery(
                        "select * from ar_request_approval where in_id = ? and delete_flag is null order by app_in_id ",
                        new Object[]{stARRequestID},
                        ARRequestApprovalView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the ownerPms
     */
    public boolean isOwnerPms() {
        return ownerPms;
    }

    /**
     * @param ownerPms the ownerPms to set
     */
    public void setOwnerPms(boolean ownerPms) {
        this.ownerPms = ownerPms;
    }

    /**
     * @return the ownerUmum
     */
    public boolean isOwnerUmum() {
        return ownerUmum;
    }

    /**
     * @param ownerUmum the ownerUmum to set
     */
    public void setOwnerUmum(boolean ownerUmum) {
        this.ownerUmum = ownerUmum;
    }

    /**
     * @return the ownerAdm
     */
    public boolean isOwnerAdm() {
        return ownerAdm;
    }

    /**
     * @param ownerAdm the ownerAdm to set
     */
    public void setOwnerAdm(boolean ownerAdm) {
        this.ownerAdm = ownerAdm;
    }

    /**
     * @return the stCashflowF
     */
    public String getStCashflowF() {
        return stCashflowF;
    }

    /**
     * @param stCashflowF the stCashflowF to set
     */
    public void setStCashflowF(String stCashflowF) {
        this.stCashflowF = stCashflowF;
    }

    /**
     * @return the stCashflowWho
     */
    public String getStCashflowWho() {
        return stCashflowWho;
    }

    /**
     * @param stCashflowWho the stCashflowWho to set
     */
    public void setStCashflowWho(String stCashflowWho) {
        this.stCashflowWho = stCashflowWho;
    }

    /**
     * @param dtCashflowDate the dtCashflowDate to set
     */
    public void setDtCashflowDate(Date dtCashflowDate) {
        this.dtCashflowDate = dtCashflowDate;
    }

    /**
     * @return the dtCashflowStart
     */
    public Date getDtCashflowStart() {
        return dtCashflowStart;
    }

    /**
     * @param dtCashflowStart the dtCashflowStart to set
     */
    public void setDtCashflowStart(Date dtCashflowStart) {
        this.dtCashflowStart = dtCashflowStart;
    }

    /**
     * @return the dtCashflowEnd
     */
    public Date getDtCashflowEnd() {
        return dtCashflowEnd;
    }

    /**
     * @param dtCashflowEnd the dtCashflowEnd to set
     */
    public void setDtCashflowEnd(Date dtCashflowEnd) {
        this.dtCashflowEnd = dtCashflowEnd;
    }

    public boolean isCashflowFlag() {
        return Tools.isYes(stCashflowF);
    }

    /**
     * @return the dtCashflowDate
     */
    public Date getDtCashflowDate() {
        return dtCashflowDate;
    }

    public void generateNoRealisasi() {

        String pilihan = "R";

        stRequestNo =
                pilihan
                + stRequestNo.substring(1, 16);
    }

    public String getDivision() throws Exception {

        HashDTO ft = (HashDTO) ListUtil.getDTOListFromQuery(
                "select code from s_division where rkap_id = ? ",
                new Object[]{stRegionID},
                HashDTO.class).getDTO();

        return ft == null ? null : ft.getFieldValueByFieldNameST("code");
    }
}

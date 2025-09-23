/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionStatistikReportForm
 * Author:  Denny Mahendra
 * Created: Aug 15, 2006 11:17:20 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.controller.FOPServlet;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.crux.common.model.HashDTO;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsuranceTreatyTypesView;
import com.webfin.insurance.ejb.PostProcessorManager;
import com.webfin.FinCodec;
import com.crux.lang.LanguageManager;
import com.crux.login.model.UserSessionView;
import com.crux.util.BDUtil;
import com.crux.util.DateUtil;
import com.crux.util.FTPUploadFile;
import com.crux.util.FileTransferDirectory;
import com.crux.util.LogManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.system.region.model.RegionView;
import java.sql.PreparedStatement;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import com.crux.util.JNDIUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import java.io.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletOutputStream;

import com.crux.util.SQLUtil;
import com.webfin.entity.model.EntityView;
import com.webfin.insurance.model.InsurancePolicyTypeGroupView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import java.sql.ResultSet;
import javax.ejb.SessionContext;

public class ProductionStatistikReportForm extends Form {

    public SessionContext ctx;
    private Date expirePeriodFrom;
    private Date expirePeriodTo;
    private Date periodFrom;
    private Date periodTo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date entryDateFrom;
    private Date entryDateTo;
    private Date periodEndFrom;
    private Date periodEndTo;
    private Date restFrom;
    private Date restTo;
    private Date appDateFrom;
    private Date appDateTo;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeID;
    private String stInsCompanyID;
    private String stInsCompanyName;
    private String stEntityName;
    private String stFltCoverType;
    private String stFltTreatyType;
    private String stFltTreatyTypeDesc;
    private String stFltCoverTypeDesc;
    private String stFltClaimStatus;
    private String stFltClaimStatusDesc;
    private String stCustCategory1;
    private String stEntityID;
    private String stPolicyTypeDesc;
    private String stPrintForm;
    private String stFontSize;
    private String stLang;
    private String stPolicyNo;
    private String stRiskLocation;
    private String stPostCode;
    private String stRiskCardNo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stRiskCode;
    private String stCustCategory1Desc;
    private boolean enableRiskFilter;
    private boolean enableSelectForm = true;
    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String stReportTitle;
    private String stReport;
    private String stReportDesc;
    private String stReportType;
    private String stNama;
    private String stMarketerID;
    private String stMarketerName;
    private String stCompanyID;
    private String stCompanyName;
    private String stZoneCode;
    private String stZoneCodeName;
    private String stPolicyTypeGroupDesc;
    private String stReportTypeOfFile;
    private String stFileName;
    private String stZoneEquake;
    private String stZoneEquakeName;
    private String stNoUrut;
    private String stNumber;
    private String stCreateID;
    private String stCreateName;
    private String stRekapNo;
    private String stAuthorized;
    private String stPostCodeDesc;
    private String stCoinsurerID;
    private String stCoinsurerName;
    private String stRiskCodeName;
    String stTreatyYear = "";
    private DTOList list;
    private String stABAFlag = "N";
    private String stIndex = "N";
    private String stReceiptNo;
    private String stObject;
    private String stParentID;
    private String stCustStatus;
    private String stStatus;
    private String stYearTreaty;
    private String stTime;
    private String stTax;
    private String stTaxDesc;
    private String stWilayahID;
    private String stWilayahName;
    private String stCompTypeID;
    private String stCompTypeName;
    private String stCreditID;
    private String stCreditName;
    private String stItemClaimID;
    private String stItemClaimName;
    private String stKreasiID;
    private String stKreasiName;
    private String stReinsID;
    private String stReinsName;
    private String stGroupID;
    private String stGroupName;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stTriwulan;
    private String stCompanyProdID;
    private String stCompanyProdName;
    private String stValidasi;
    private String stFFac;
    private String stMarketerOffID;
    private String stMarketerOffName;
    private String stProfileID;
    private String stProfileName;
    private String stGroupJenisID;
    private String stGroupJenisName;
    private String stGroupOutgoID;
    private String stGroupOutgoName;
    private String stPolicyTypeCreditID;
    private String stPolicyTypeCreditDesc;
    private String stUsiaID;
    private String stUsiaDesc;
    private String stRiskProfileID;
    private String stRiskProfileDesc;
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;
    private String stBranchSource;
    private String stBranchSourceDesc;
    private String stRegionSource;
    private String stRegionSourceDesc;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionMarketingReportForm.class);

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public String getStZoneEquake() {
        return stZoneEquake;
    }

    public void setStZoneEquake(String stZoneEquake) {
        this.stZoneEquake = stZoneEquake;
    }

    public String getStZoneEquakeName() {
        return stZoneEquakeName;
    }

    public void setStZoneEquakeName(String stZoneEquakeName) {
        this.stZoneEquakeName = stZoneEquakeName;
    }

    public String getStFileName() {
        return stFileName;
    }

    public void setStFileName(String stFileName) {
        this.stFileName = stFileName;
    }

    public String getStMarketerID() {
        return stMarketerID;
    }

    public void setStMarketerID(String stMarketerID) {
        this.stMarketerID = stMarketerID;
    }

    public String getStMarketerName() {
        return stMarketerName;
    }

    public void setStMarketerName(String stMarketerName) {
        this.stMarketerName = stMarketerName;
    }

    public String getStCompanyID() {
        return stCompanyID;
    }

    public void setStCompanyID(String stCompanyID) {
        this.stCompanyID = stCompanyID;
    }

    public String getStCompanyName() {
        return stCompanyName;
    }

    public void setStCompanyName(String stCompanyName) {
        this.stCompanyName = stCompanyName;
    }

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public String getStReportTypeOfFile() {
        return stReportTypeOfFile;
    }

    public void setStReportTypeOfFile(String stReportTypeOfFile) {
        this.stReportTypeOfFile = stReportTypeOfFile;
    }

    public String getStFltClaimStatus() {
        return stFltClaimStatus;
    }

    public void setStFltClaimStatus(String stFltClaimStatus) {
        this.stFltClaimStatus = stFltClaimStatus;
    }

    public String getStReport() {
        return stReport;
    }

    public void setStReport(String stReport) {
        this.stReport = stReport;
    }

    public String getStReportDesc() {
        return stReportDesc;
    }

    public void setStReportDesc(String stReportDesc) {
        this.stReportDesc = stReportDesc;
    }

    public String getStFltClaimStatusDesc() {
        return stFltClaimStatusDesc;
    }

    public void setStFltClaimStatusDesc(String stFltClaimStatusDesc) {
        this.stFltClaimStatusDesc = stFltClaimStatusDesc;
    }

    public String getStEntityName() {
        return stEntityName;
    }

    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }

    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    public String getStFltTreatyTypeDesc() {
        return stFltTreatyTypeDesc;
    }

    public void setStFltTreatyTypeDesc(String stFltTreatyTypeDesc) {
        this.stFltTreatyTypeDesc = stFltTreatyTypeDesc;
    }

    public String getStFltTreatyType() {
        return stFltTreatyType;
    }

    public void setStFltTreatyType(String stFltTreatyType) {
        this.stFltTreatyType = stFltTreatyType;
    }

    public String getStBranchDesc() {
        return stBranchDesc;
    }

    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public boolean isEnableSelectForm() {
        return enableSelectForm;
    }

    public void setEnableSelectForm(boolean enableSelectForm) {
        this.enableSelectForm = enableSelectForm;
    }
    private static String[][] objectFields = {
        {"b.description", "odescription",},
        {"b.nd_premi1", "nd_premi1",},
        {"b.nd_premi2", "nd_premi2",},
        {"b.nd_premi3", "nd_premi3",},
        {"b.nd_premi4", "nd_premi4",},
        {"b.nd_premirate1", "nd_premirate1",},
        {"b.nd_premirate2", "nd_premirate2",},
        {"b.nd_premirate3", "nd_premirate3",},
        {"b.nd_premirate4", "nd_premirate4",},};
    private static String[][] objectFields2 = {
        {"coalesce(b.ref1d,b.ref1)", "obj_ref1",},
        {"coalesce(b.ref2d,b.ref2)", "obj_ref2",},
        {"coalesce(b.ref3d,b.ref3)", "obj_ref3",},
        {"coalesce(b.ref4d,b.ref4)", "obj_ref4",},
        {"coalesce(b.ref5d,b.ref5)", "obj_ref5",},
        {"coalesce(b.ref6d,b.ref6)", "obj_ref6",},
        {"coalesce(b.ref7d,b.ref7)", "obj_ref7",},
        {"b.riskcode", "riskcode",},
        {"b.risk_location", "risk_location",},
        {"b.riskcardno", "riskcardno",},
        {"b.insured_amount", "obj_insured_amount",},
        {"b.premi_total", "obj_premi_total",},};
    private static String[][] policyFields = {
        {"a.pol_id", "pol_id",},
        {"a.pol_no", "pol_no",},
        {"a.description", "description",},
        {"a.ccy", "ccy",},
        {"a.posted_flag", "posted_flag",},
        {"a.create_date", "create_date",},
        {"a.create_who", "create_who",},
        {"a.change_date", "change_date",},
        {"a.change_who", "change_who",},
        {"a.pol_type_id", "pol_type_id",},
        {"a.amount", "amount",},
        {"a.period_start", "period_start",},
        {"a.period_end", "period_end",},
        {"a.pol_subtype_id", "pol_subtype_id",},
        {"a.premi_base", "premi_base",},
        {"a.premi_total", "premi_total",},
        {"a.premi_rate", "premi_rate",},
        {"a.insured_amount", "insured_amount",},
        {"a.policy_date", "policy_date",},
        {"a.bus_source", "bus_source",},
        {"a.region_id", "region_id",},
        {"a.captive_flag", "captive_flag",},
        {"a.inward_flag", "inward_flag",},
        {"a.premi_netto", "premi_netto",},
        {"a.ccy_rate", "ccy_rate",},
        {"a.cc_code", "cc_code",},
        {"a.entity_id", "entity_id",},
        {"a.condition_id", "condition_id",},
        {"a.risk_category_id", "risk_category_id",},
        {"a.cover_type_code", "cover_type_code",},
        {"a.f_prodmode", "f_prodmode",},
        {"a.cust_name", "cust_name",},
        {"a.cust_address", "cust_address",},
        {"a.master_policy_id", "master_policy_id",},
        {"a.prod_name", "prod_name",},
        {"a.prod_address", "prod_address",},
        {"a.prod_id", "prod_id",},
        {"a.ins_policy_type_grp_id", "ins_policy_type_grp_id",},
        {"a.premi_total_adisc", "premi_total_adisc",},
        {"a.total_due", "total_due",},
        {"a.ins_period_id", "ins_period_id",},
        {"a.inst_period_id", "inst_period_id",},
        {"a.inst_periods", "inst_periods",},
        {"a.period_rate", "period_rate",},
        {"a.ref1", "ref1",},
        {"a.ref2", "ref2",},
        {"a.ref3", "ref3",},
        {"a.ref4", "ref4",},
        {"a.ref5", "ref5",},
        {"a.ref6", "ref6",},
        {"a.ref7", "ref7",},
        {"a.ref8", "ref8",},
        {"a.ref9", "ref9",},
        {"a.ref10", "ref10",},
        {"a.ref11", "ref11",},
        {"a.ref12", "ref12",},
        {"a.refd1", "refd1",},
        {"a.refd2", "refd2",},
        {"a.refd3", "refd3",},
        {"a.refd4", "refd4",},
        {"a.refd5", "refd5",},
        {"a.refn1", "refn1",},
        {"a.refn2", "refn2",},
        {"a.refn3", "refn3",},
        {"a.refn4", "refn4",},
        {"a.refn5", "refn5",},
        {"a.parent_id", "parent_id",},
        {"a.status", "status",},
        {"a.active_flag", "active_flag",},
        {"a.sppa_no", "sppa_no",},
        {"a.claim_no", "claim_no",},
        {"a.claim_date", "claim_date",},
        {"a.claim_cause", "claim_cause",},
        {"a.claim_cause_desc", "claim_cause_desc",},
        {"a.event_location", "event_location",},
        {"a.claim_person_id", "claim_person_id",},
        {"a.claim_person_name", "claim_person_name",},
        {"a.claim_person_address", "claim_person_address",},
        {"a.claim_person_status", "claim_person_status",},
        {"a.claim_amount_est", "claim_amount_est",},
        {"a.claim_currency", "claim_currency",},
        {"a.claim_loss_status", "claim_loss_status",},
        {"a.claim_benefit", "claim_benefit",},
        {"a.claim_documents", "claim_documents",},
        {"a.endorse_date", "endorse_date",},
        {"a.effective_flag", "effective_flag",},
        {"a.claim_status", "claim_status",},
        {"a.endorse_notes", "endorse_notes",},
        {"a.print_code", "print_code",},
        {"a.root_id", "root_id",},
        {"a.insured_amount_e", "insured_amount_e",},
        {"a.ins_period_base_id", "ins_period_base_id",},
        {"a.period_rate_before", "period_rate_before",},
        {"a.ins_period_base_b4", "ins_period_base_b4",},
        {"a.ins_premium_factor_id", "ins_premium_factor_id",},
        {"a.dla_no", "dla_no",},
        {"a.ins_treaty_id", "ins_treaty_id",},
        {"a.total_fee", "total_fee",},
        {"a.nd_comm1", "nd_comm1",},
        {"a.nd_comm2", "nd_comm2",},
        {"a.nd_comm3", "nd_comm3",},
        {"a.nd_comm4", "nd_comm4",},
        {"a.nd_brok1", "nd_brok1",},
        {"a.nd_brok2", "nd_brok2",},
        {"a.nd_hfee", "nd_hfee",},
        {"a.nd_sfee", "nd_sfee",},
        {"a.nd_pcost", "nd_pcost",},
        {"a.nd_update", "nd_update",},
        {"a.nd_brok1pct", "nd_brok1pct",},
        {"a.nd_brok2pct", "nd_brok2pct",},
        {"a.nd_hfeepct", "nd_hfeepct",},
        {"a.nd_disc1", "nd_disc1",},
        {"a.nd_disc2", "nd_disc2",},
        {"a.nd_disc1pct", "nd_disc1pct",},
        {"a.nd_disc2pct", "nd_disc2pct",},
        {"a.odescription", "odescription",},
        {"a.pfx_clauses", "pfx_clauses",},
        {"a.pfx_interest", "pfx_interest",},
        {"a.pfx_coverage", "pfx_coverage",},
        {"a.pfx_deductible", "pfx_deductible",},
        {"a.claim_ded_amount", "claim_ded_amount",},
        {"a.premi_pay_date", "premi_pay_date",},
        {"a.claim_amount", "claim_amount",},
        {"a.claim_amount_approved", "claim_amount_approved",},
        {"a.pla_no", "pla_no",},
        {"a.dla_remark", "dla_remark",},
        {"a.claim_cust_amount", "claim_cust_amount",},
        {"a.claim_cust_ded_amount", "claim_cust_ded_amount",},
        {"a.claim_ri_amount", "claim_ri_amount",},
        {"a.claim_object_id", "claim_object_id",},};

    public String getStCustCategory1Desc() {
        return stCustCategory1Desc;
    }

    public void setStCustCategory1Desc(String stCustCategory1Desc) {
        this.stCustCategory1Desc = stCustCategory1Desc;
    }

    public String getStFltCoverTypeDesc() {
        return stFltCoverTypeDesc;
    }

    public void setStFltCoverTypeDesc(String stFltCoverTypeDesc) {
        this.stFltCoverTypeDesc = stFltCoverTypeDesc;
    }

    public void onChangePolicyTypeGroup() {
    }

    public void onChangeBranchGroup() {
    }

    public void onChangeReport() {
    }

    public String getStPolicyTypeGroupID() {
        return stPolicyTypeGroupID;
    }

    public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
        this.stPolicyTypeGroupID = stPolicyTypeGroupID;
    }

    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public Date getPolicyDateFrom() {
        return policyDateFrom;
    }

    public void setPolicyDateFrom(Date policyDateFrom) {
        this.policyDateFrom = policyDateFrom;
    }

    public Date getPolicyDateTo() {
        return policyDateTo;
    }

    public void setPolicyDateTo(Date policyDateTo) {
        this.policyDateTo = policyDateTo;
    }

    public Date getEntryDateFrom() {
        return entryDateFrom;
    }

    public void setEntryDateFrom(Date entryDateFrom) {
        this.entryDateFrom = entryDateFrom;
    }

    public Date getEntryDateTo() {
        return entryDateTo;
    }

    public void setEntryDateTo(Date entryDateTo) {
        this.entryDateTo = entryDateTo;
    }

    public boolean isEnableRiskFilter() {
        return enableRiskFilter;
    }

    public void setEnableRiskFilter(boolean enableRiskFilter) {
        this.enableRiskFilter = enableRiskFilter;
    }

    public String getStRiskLocation() {
        return stRiskLocation;
    }

    public void setStRiskLocation(String stRiskLocation) {
        this.stRiskLocation = stRiskLocation;
    }

    public String getStPostCode() {
        return stPostCode;
    }

    public void setStPostCode(String stPostCode) {
        this.stPostCode = stPostCode;
    }

    public String getStRiskCardNo() {
        return stRiskCardNo;
    }

    public void setStRiskCardNo(String stRiskCardNo) {
        this.stRiskCardNo = stRiskCardNo;
    }

    public String getStRiskCode() {
        return stRiskCode;
    }

    public void setStRiskCode(String stRiskCode) {
        this.stRiskCode = stRiskCode;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    public String getStPrintForm() {
        return stPrintForm;
    }

    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    public Date getExpirePeriodFrom() {
        return expirePeriodFrom;
    }

    public void setExpirePeriodFrom(Date expirePeriodFrom) {
        this.expirePeriodFrom = expirePeriodFrom;
    }

    public Date getExpirePeriodTo() {
        return expirePeriodTo;
    }

    public void setExpirePeriodTo(Date expirePeriodTo) {
        this.expirePeriodTo = expirePeriodTo;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStCustCategory1() {
        return stCustCategory1;
    }

    public void setStCustCategory1(String stCustCategory1) {
        this.stCustCategory1 = stCustCategory1;
    }

    public String getStFltCoverType() {
        return stFltCoverType;
    }

    public void setStFltCoverType(String stFltCoverType) {
        this.stFltCoverType = stFltCoverType;
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/prodrpt")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
    }

    private void validateNumber() throws Exception {

        if (getStReport().equalsIgnoreCase("rppkreasi_koas") || getStReport().equalsIgnoreCase("rppkreasi_nettkoas")) {
            if (getStNoUrut() == null && getStRekapNo() == null) {
                throw new RuntimeException("No. belum diisi");
            }
        }
    }

    public void clickPrint() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        loadFormList();

        validateNumber();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final ArrayList plist = new ArrayList();

        plist.add(stReport + "_" + stFltTreatyType);

        plist.add(stReport + "_" + stValidasi);

        plist.add(stReport + "_" + stIndex);

        plist.add(stReport + "_" + stBranch);

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stPolicyTypeID);

        plist.add(stReport + "_" + stPolicyTypeGroupID);

        plist.add(stReport);

        String urx = null;
        String urxSave = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/insurance/prodrpt/" + s + ".fop?xlang=" + stLang;
                urxSave = "/pages/insurance/prodrpt/" + s + "_SAVE.fop";
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        if (urxSave == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        if (stReport.equals("rpp_rekap_cab") || stReport.equals("rpp_rekap_jenis")) {
            String dateFrom = LanguageManager.getInstance().translate(DateUtil.getDateStr(policyDateFrom, "^yyyy"));
            String dateTo = LanguageManager.getInstance().translate(DateUtil.getDateStr(policyDateTo, "^yyyy"));

            String filename = stReport + "-" + dateFrom + "-" + dateTo;
            SessionManager.getInstance().getRequest().setAttribute("SAVE_TO_FILE", "Y");
            SessionManager.getInstance().getRequest().setAttribute("FILE_NAME", filename);
            SessionManager.getInstance().getRequest().setAttribute("REPORT_PROD", "Y");

            FOPServlet srv = new FOPServlet();
            srv.saveFOP(SessionManager.getInstance().getRequest(), SessionManager.getInstance().getResponse(), urxSave);

//            //COPY POLIS KE DIRECTORY
//            String filePath = "D:/fin-repository/report_temp/edocument/" + filename + "_temp.pdf";
//            String uploadPath = "uw-" + filename + ".pdf";
        }

        super.redirect(urx);

    }

    private DTOList loadList() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY");

        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return defaultQuery();
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public void clickPrintTransfer() throws Exception {

        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY_EXCEL");

        final Method m = this.getClass().getMethod(queryID, null);

        final DTOList data = (DTOList) m.invoke(this, null);

        final DTOList data2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        InsuranceProdukView prod = (InsuranceProdukView) data2.get(0);

        //SessionManager.getInstance().getRequest().setAttribute(queryID, data);

        //getRemoteInsurance().updateABAProdukByDate(data2);
        //if(true) throw new RuntimeException("data= "+prod.getStInsuranceNoPolis());
        //updateABAProdukByDate(data);

        exportExcelTransfer();

    }

    public void exportExcelTransfer() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_PRINTING", stReport), getStLang());

        setStFileName(fileName);

        final String exportID = (String) refPropMap.get("EXPORT");

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    public void clickPrintExcel() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcel();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        exportExcel();
    }

    public void exportExcel() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_PRINTING", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcel() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY_EXCEL");


        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return defaultQuery();
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public void btnPrintProd() throws Exception {
        PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        super.redirect("/pages/insurance/prodrpt/prodreport.fop");
    }

    public void btnPrintReminder() throws Exception {

        PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        super.redirect("/pages/insurance/prodrpt/reminderLetters.fop");
    }

    public DTOList NONE() throws Exception {
        return null;
    }

    private DTOList defaultQuery() throws Exception {
        final boolean joinObject = "Y".equalsIgnoreCase((String) refPropMap.get("JOIN_OBJECT"));
        final boolean joinObject2 = "Y".equalsIgnoreCase((String) refPropMap.get("JOIN_OBJECT2"));
        final boolean joinRI = "Y".equalsIgnoreCase((String) refPropMap.get("JOIN_RI"));
        final boolean TRANSPOSE_RI_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("TRANSPOSE_RI_PREMI"));
        final boolean TRANSPOSE_RI_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("TRANSPOSE_RI_CLAIM"));
        final boolean TRANSPOSE_RI_TSI = "Y".equalsIgnoreCase((String) refPropMap.get("TRANSPOSE_RI_TSI"));
        final boolean USE_HASH = "Y".equalsIgnoreCase((String) refPropMap.get("USE_HASH"));
        final boolean USE_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("USE_PREMI"));
        final boolean FLT_OUTS_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_PREMI"));
        final boolean FLT_PAID_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_PREMI"));
        final boolean USE_EXT = "Y".equalsIgnoreCase((String) refPropMap.get("USE_EXT")) || USE_PREMI;
        final String FLT_POL_TYPE = (String) refPropMap.get("FLT_POL_TYPE");
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));


        final boolean groupAll = "Y".equalsIgnoreCase((String) refPropMap.get("GROUP_ALL"));

        if (FLT_POL_TYPE != null) {
            stPolicyTypeID = FLT_POL_TYPE;
        }

        final String groupBy = (String) refPropMap.get("GROUP");

        final boolean groupOn = groupBy != null || groupAll;

        SQLAssembler sqa = new SQLAssembler();

        for (int i = 0; i < policyFields.length; i++) {
            String[] policyField = policyFields[i];

            if (groupOn) {
                sqa.addSelect("summ(" + policyField[0] + ") as " + policyField[1]);
            } else if (i == 0) {
                sqa.addSelect(" distinct " + policyField[0] + " as " + policyField[1]);
            } else {
                sqa.addSelect(policyField[0] + " as " + policyField[1]);
            }
        }

        if (joinObject) {
            for (int i = 0; i < objectFields.length; i++) {
                String[] f = objectFields[i];

                if (groupOn) {
                    sqa.addSelect("summ(" + f[0] + ") as " + f[1]);
                } else {
                    sqa.addSelect(f[0] + " as " + f[1]);
                }
            }
        }

        if (joinObject2) {
            for (int i = 0; i < objectFields2.length; i++) {
                String[] f = objectFields2[i];

                if (groupOn) {
                    sqa.addSelect("summ(" + f[0] + ") as " + f[1]);
                } else {
                    sqa.addSelect(f[0] + " as " + f[1]);
                }
            }

            sqa.addSelect(
                    "( select sum(claim_amount) from ins_policy where effective_flag='Y' and status='CLAIM' \n"
                    + "and\n"
                    + "pol_id in (select pol_id from ins_pol_obj where ins_pol_obj_ref_id = b.ins_pol_obj_id)) as obj_claim_amount");

        }

        //sqa.addSelect("a.*");

        sqa.addQuery(
                "   from  "
                + "      ins_policy a");

        if (joinObject || joinObject2) {
            sqa.addQuery(" inner join ins_pol_obj b on b.pol_id=a.pol_id");
        }


        /*
        sqa.addQuery(
        "         left join ent_master c on c.ent_id = a.entity_id, ins_pol_items d"
        );*/
        sqa.addQuery(
                "         left join ent_master c on c.ent_id = a.entity_id ");


        if (joinRI) {
            sqa.addQuery(" inner join ins_pol_treaty e on e.ins_pol_obj_id = b.ins_pol_obj_id");
            sqa.addQuery(" inner join ins_pol_treaty_detail f on e.ins_pol_treaty_id = f.ins_pol_treaty_id");
            sqa.addQuery(" inner join ins_pol_ri g on g.ins_pol_tre_det_id = f.ins_pol_tre_det_id");
            sqa.addQuery(" inner join ent_master h on h.ent_id = g.member_ent_id");
            sqa.addQuery(" inner join ins_treaty_detail i on i.ins_treaty_detail_id = f.ins_treaty_detail_id");

            addAutoSumm(sqa, groupOn, "h.ent_id", "ri_ent_id");
            addAutoSumm(sqa, groupOn, "h.ent_name", "ri_ent_name");
            addAutoSumm(sqa, groupOn, "g.claim_amount", "ri_claim_amount");
            addAutoSumm(sqa, groupOn, "g.premi_amount", "ri_premi_amount");
            addAutoSumm(sqa, groupOn, "g.sharepct", "ri_sharepct");
            addAutoSumm(sqa, groupOn, "g.tsi_amount", "ri_tsi_amount");
            addAutoSumm(sqa, groupOn, "i.treaty_type", "treaty_type");

            DTOList treTypes = getTreatyTypes();

            if (groupOn) {
                for (int i = 0; i < treTypes.size(); i++) {
                    InsuranceTreatyTypesView tt = (InsuranceTreatyTypesView) treTypes.get(i);

                    if (TRANSPOSE_RI_PREMI) {
                        sqa.addSelect("summ(case when i.treaty_type='" + tt.getStInsuranceTreatyTypeID() + "' then g.premi_amount else 0 end) as ri_premi_amt_" + tt.getStInsuranceTreatyTypeID());
                    }

                    if (TRANSPOSE_RI_CLAIM) {
                        sqa.addSelect("summ(case when i.treaty_type='" + tt.getStInsuranceTreatyTypeID() + "' then g.claim_amount else 0 end) as ri_claim_amt_" + tt.getStInsuranceTreatyTypeID());
                    }

                    if (TRANSPOSE_RI_TSI) {
                        sqa.addSelect("summ(case when i.treaty_type='" + tt.getStInsuranceTreatyTypeID() + "' then g.tsi_amount else 0 end) as ri_tsi_amt_" + tt.getStInsuranceTreatyTypeID());
                    }
                }
            }

            if (stInsCompanyID != null) {
                sqa.addClause("g.member_ent_id = ?");
                sqa.addPar(stInsCompanyID);
            }

            if (stFltTreatyType != null) {
                sqa.addClause("i.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }

        }

        if (stFltClaimStatus != null) {
            sqa.addClause("a.claim_status = ?");
            sqa.addPar(stFltClaimStatus);
        }

        if (status != null) {
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(status)) {
                sqa.addClause("a.status in (?,?,?)");
                sqa.addPar(FinCodec.PolicyStatus.POLICY);
                sqa.addPar(FinCodec.PolicyStatus.ENDORSE);
                sqa.addPar(FinCodec.PolicyStatus.RENEWAL);
            } else {
                sqa.addClause("a.status = ?");
                sqa.addPar(status);
            }
        }

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("a.claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start)>=?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start)<=?");
            sqa.addPar(periodTo);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date)>=?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date)<=?");
            sqa.addPar(policyDateTo);
        }

        if (entryDateFrom != null) {
            sqa.addClause("a.create_date>=?");
            sqa.addPar(entryDateFrom);
        }

        if (expirePeriodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end)>=?");
            sqa.addPar(expirePeriodFrom);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id=?");
            sqa.addPar(stPolicyTypeID);
        }

        if (expirePeriodTo != null) {
            sqa.addClause("date_trunc('day',a.period_end)<=?");
            sqa.addPar(expirePeriodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code=?");
            sqa.addPar(stBranch);
        }

        if (stFltCoverType != null) {
            sqa.addClause("a.cover_type_code = ?");
            sqa.addPar(stFltCoverType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final SQLAssembler sqaobj = new SQLAssembler();

        sqaobj.addSelect("distinct pol_id");
        sqaobj.addQuery("from ins_pol_obj a ");

        if (stRiskLocation != null) {
            sqaobj.addClause("upper(risk_location) like ?");
            sqaobj.addPar(stRiskLocation.toUpperCase());
        }

        if (stPostCode != null) {
            sqaobj.addClause("a.ref6d like ?");
            sqaobj.addPar('%' + stPostCode + '%');
        }

        if (stRiskCode != null) {
            sqaobj.addClause("a.ins_risk_cat_id like ?");
            sqaobj.addPar('%' + stRiskCode + '%');
        }

        if (stZoneCode != null) {
            sqaobj.addQuery(" inner join ins_pol_cover b on b.ins_pol_obj_id = a.ins_pol_obj_id");
            sqaobj.addClause("b.zone_id like ?");
            sqaobj.addPar('%' + stZoneCode + '%');
        }

        /*
        if (stPostCode!=null) {
        sqaobj.addQuery(" inner join s_region_map2 b on b.region_map_id = a.zipcode");
        sqaobj.addClause("upper(b.postal_code) like ?");
        sqaobj.addPar(stRiskLocation.toUpperCase());
        }

        if (stRiskCode!=null) {
        sqaobj.addClause("a.riskcode like ?");
        sqaobj.addPar(stRiskCode.toUpperCase());
        }*/

        if (stRiskCardNo != null) {
            sqaobj.addClause("a.riskcardno like ?");
            sqaobj.addPar(stRiskCardNo.toUpperCase());
        }

        if (sqaobj.hasClause()) {
            sqa.addClause("pol_id in (" + sqaobj.getSQL() + ")");
            sqa.getPar().addAll(sqaobj.getPar());
        }

        if (USE_PREMI) {
            sqa.addSelect("(coalesce ((select sum(amount_settled) from ar_invoice where ar_invoice.refid0='PREMI/'||a.pol_id and commit_flag='Y'),0)) as premi_paid");
        }

        if (USE_EXT) {
            sqa.addSelect("(coalesce((select sum(amount) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis");
            sqa.addSelect("(coalesce((select sum(amount_settled) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis_p");
            sqa.addSelect("(coalesce((select sum(amount) from ar_pol2 where ins_item_cat='BROKR' and pol_id=a.pol_id ),0)) as ap_brok");
            sqa.addSelect("(coalesce((select sum(amount_settled) from ar_pol2 where ins_item_cat='BROKR' and pol_id=a.pol_id),0)) as ap_brok_p");
        }

        //tes

        if (groupAll) {
            for (int i = 0; i < objectFields.length; i++) {
                String[] f = objectFields[i];

                if (groupAll) //sqa.addSelect("summ("+f[0]+") as "+f[1]);
                {
                    sqa.addGroup(f[0]);
                }

            }
        }

        for (int i = 0; i < policyFields.length; i++) {
            String[] policyField = policyFields[i];

            if (groupAll) //sqa.addSelect("summ("+policyField[0]+") as "+policyField[1]);
            {
                sqa.addGroup(policyField[0]);
            }

        }

//      if (groupAll)
//         for (int i = 0; i < objectFields.length; i++) {
//            String[] f = objectFields[i];
//
//            if (groupAll)
//               //sqa.addSelect("summ("+f[0]+") as "+f[1]);
//               sqa.addGroup(policyField[0]);
//           // else
//               //sqa.addSelect(f[0]+" as "+f[1]);
//         }
        //finish


        if (groupBy != null) {
            sqa.addGroup(groupBy);
        }
        //else
        //sqa.addOrder("date_trunc('day',a.policy_date)");

        boolean isClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(status);

        if (!isClaim) {
            sqa.addClause("a.effective_flag = 'Y'");
            //sqa.addClause(" d.pol_id=a.pol_id and d.tax_amount != 0");
        }

        if (EFFECTIVE) {
            sqa.addClause("a.effective_flag = 'Y'");
            //sqa.addClause(" d.pol_id=a.pol_id and d.tax_amount != 0");
        }


        boolean USE_BLANKET = FLT_OUTS_PREMI || FLT_PAID_PREMI;

        if (USE_BLANKET) {
            String gquery = sqa.getSQL();
            ArrayList vpat = sqa.getPar();

            sqa = new SQLAssembler();
            sqa.setPar(vpat);
            sqa.addQuery(" from (" + gquery + ") x");
            sqa.addSelect("x.*");
        }

        if (FLT_OUTS_PREMI) {
            sqa.addClause("premi_paid+ap_comis_p+ap_brok_p < premi_netto+ap_comis+ap_brok");
        }

        if (FLT_PAID_PREMI) {
            sqa.addClause("premi_paid+ap_comis_p+ap_brok_p = premi_netto+ap_comis+ap_brok");
        }


        /*
        SQLAssembler sql2 = new SQLAssembler();

        sql2.addSelect(" a.* ");
        sql2.addQuery(" from ins_pol_items a, ins_policy b "+
        " where b.pol_id=a.pol_id ");
        sql2.addOrder(" b.policy_date");*/


        DTOList l;
        //DTOList m;

        if (USE_HASH) {
            l = sqa.getList(HashDTO.class);
        } else {
            l = sqa.getList(InsurancePolicyView.class);
            //m = sql2.getList(InsurancePolicyItemsView.class);

        }

        return l;
    }

    public String getStInsCompanyID() {
        return stInsCompanyID;
    }

    public void setStInsCompanyID(String stInsCompanyID) {
        this.stInsCompanyID = stInsCompanyID;
    }

    public String getStInsCompanyName() {
        return stInsCompanyName;
    }

    public void setStInsCompanyName(String stInsCompanyName) {
        this.stInsCompanyName = stInsCompanyName;
    }

    private void addAutoSumm(SQLAssembler sqa, boolean groupOn, String field, String alias) {
        if (groupOn) {
            sqa.addSelect("summ(" + field + ") as " + alias);
        } else {
            sqa.addSelect(field + " as " + alias);
        }
    }

    private DTOList getTreatyTypes() throws Exception {
        return ListUtil.getDTOListFromQuery("select ins_treaty_type_id from ins_treaty_types", InsuranceTreatyTypesView.class);
    }

    public void marketing() {
    }

    public void uwrit() {
        enableRiskFilter = true;
    }

    public void chgform() {
    }

    public void go() {
        stPrintForm = (String) getAttribute("rpt");
        enableSelectForm = false;

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_PRINTING");
    }

    public String getStReportTitle() {
        return stReportTitle;
    }

    public void setStReportTitle(String stReportTitle) {
        this.stReportTitle = stReportTitle;
    }

    public String getStZoneCode() {
        return stZoneCode;
    }

    public void setStZoneCode(String stZoneCode) {
        this.stZoneCode = stZoneCode;
    }

    public String getStZoneCodeName() {
        return stZoneCodeName;
    }

    public void setStZoneCodeName(String stZoneCodeName) {
        this.stZoneCodeName = stZoneCodeName;
    }

    public String getStPolicyTypeGroupDesc() {
        return stPolicyTypeGroupDesc;
    }

    public void setStPolicyTypeGroupDesc(String stPolicyTypeGroupDesc) {
        this.stPolicyTypeGroupDesc = stPolicyTypeGroupDesc;
    }

    public String getStNoUrut() {
        return stNoUrut;
    }

    public void setStNoUrut(String stNoUrut) {
        this.stNoUrut = stNoUrut;
    }

    public String getStNama() {
        return stNama;
    }

    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    public String getStBranchName() {
        return stBranchName;
    }

    public void setStBranchName(String stBranchName) {
        this.stBranchName = stBranchName;
    }

    public String getStCreateID() {
        return stCreateID;
    }

    public void setStCreateID(String stCreateID) {
        this.stCreateID = stCreateID;
    }

    public String getStCreateName() {
        return stCreateName;
    }

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    public String getStRekapNo() {
        return stRekapNo;
    }

    public void setStRekapNo(String stRekapNo) {
        this.stRekapNo = stRekapNo;
    }

    public String getStAuthorized() {
        return stAuthorized;
    }

    public void setStAuthorized(String stAuthorized) {
        this.stAuthorized = stAuthorized;
    }

    public String getStPostCodeDesc() {
        return stPostCodeDesc;
    }

    public void setStPostCodeDesc(String stPostCodeDesc) {
        this.stPostCodeDesc = stPostCodeDesc;
    }
    private String stPostCode2;
    private String stPostCodeDesc2;

    public String getStPostCode2() {
        return stPostCode2;
    }

    public void setStPostCode2(String stPostCode2) {
        this.stPostCode2 = stPostCode2;
    }

    public String getStPostCodeDesc2() {
        return stPostCodeDesc2;
    }

    public void setStPostCodeDesc2(String stPostCodeDesc2) {
        this.stPostCodeDesc2 = stPostCodeDesc2;
    }

    public PostalCodeView getPostCode(String stPostCode) {
        final PostalCodeView postcode = (PostalCodeView) DTOPool.getInstance().getDTO(PostalCodeView.class, stPostCode);

        return postcode;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public String getStYearTreaty() {
        return stYearTreaty;
    }

    public void setStYearTreaty(String stYearTreaty) {
        this.stYearTreaty = stYearTreaty;
    }

    public EntityView getCoinsurer() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stCoinsurerID);

        return entity;
    }

    public EntityView getEntity() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return entity;
    }

    public String getEntityShortName() {

        final EntityView shortname = getEntity();

        if (shortname == null) {
            return null;
        }

        return shortname.getStShortName();
    }

    public String getEntityRefEntity() {

        final EntityView refent = getEntity();

        if (refent == null) {
            return null;
        }

        return refent.getStRefEntityID();
    }

    public String getEntityAddress() {

        final EntityView address = getEntity();

        if (address == null) {
            return null;
        }

        return address.getStAddress();
    }

    public String getStNumber() {
        return stNumber;
    }

    public void setStNumber(String stNumber) {
        this.stNumber = stNumber;
    }

    public String getStCoinsurerID() {
        return stCoinsurerID;
    }

    public void setStCoinsurerID(String stCoinsurerID) {
        this.stCoinsurerID = stCoinsurerID;
    }

    public String getStCoinsurerName() {
        return stCoinsurerName;
    }

    public void setStCoinsurerName(String stCoinsurerName) {
        this.stCoinsurerName = stCoinsurerName;
    }

    public String getStRiskCodeName() {
        return stRiskCodeName;
    }

    public void setStRiskCodeName(String stRiskCodeName) {
        this.stRiskCodeName = stRiskCodeName;
    }

    public Date getAppDateFrom() {
        return appDateFrom;
    }

    public void setAppDateFrom(Date appDateFrom) {
        this.appDateFrom = appDateFrom;
    }

    public Date getAppDateTo() {
        return appDateTo;
    }

    public void setAppDateTo(Date appDateTo) {
        this.appDateTo = appDateTo;
    }

    public String getStIndex() {
        return stIndex;
    }

    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
    }

    public String getStObject() {
        return stObject;
    }

    public void setStObject(String stObject) {
        this.stObject = stObject;
    }

    public String getStReceiptNo() {
        return stReceiptNo;
    }

    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return costcenter;
    }

    public InsurancePolicyView getPolicy(String stParentID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
    }

    public String getStCustStatus() {
        return stCustStatus;
    }

    public void setStCustStatus(String stCustStatus) {
        this.stCustStatus = stCustStatus;
    }

    public Date getPeriodEndFrom() {
        return periodEndFrom;
    }

    public void setPeriodEndFrom(Date periodEndFrom) {
        this.periodEndFrom = periodEndFrom;
    }

    public Date getPeriodEndTo() {
        return periodEndTo;
    }

    public void setPeriodEndTo(Date periodEndTo) {
        this.periodEndTo = periodEndTo;
    }

    public String getStTime() {
        return stTime;
    }

    public void setStTime(String stTime) {
        this.stTime = stTime;
    }

    public String getStTax() {
        return stTax;
    }

    public void setStTax(String stTax) {
        this.stTax = stTax;
    }

    public String getStTaxDesc() {
        return stTaxDesc;
    }

    public void setStTaxDesc(String stTaxDesc) {
        this.stTaxDesc = stTaxDesc;
    }

    public Date getRestFrom() {
        return restFrom;
    }

    public void setRestFrom(Date restFrom) {
        this.restFrom = restFrom;
    }

    public Date getRestTo() {
        return restTo;
    }

    public void setRestTo(Date restTo) {
        this.restTo = restTo;
    }

    public String getStCompTypeID() {
        return stCompTypeID;
    }

    public void setStCompTypeID(String stCompTypeID) {
        this.stCompTypeID = stCompTypeID;
    }

    public String getStCompTypeName() {
        return stCompTypeName;
    }

    public void setStCompTypeName(String stCompTypeName) {
        this.stCompTypeName = stCompTypeName;
    }

    public String getStCreditID() {
        return stCreditID;
    }

    public void setStCreditID(String stCreditID) {
        this.stCreditID = stCreditID;
    }

    public String getStCreditName() {
        return stCreditName;
    }

    public void setStCreditName(String stCreditName) {
        this.stCreditName = stCreditName;
    }

    public String getStItemClaimID() {
        return stItemClaimID;
    }

    public void setStItemClaimID(String stItemClaimID) {
        this.stItemClaimID = stItemClaimID;
    }

    public String getStItemClaimName() {
        return stItemClaimName;
    }

    public void setStItemClaimName(String stItemClaimName) {
        this.stItemClaimName = stItemClaimName;
    }

    public String getStWilayahID() {
        return stWilayahID;
    }

    public void setStWilayahID(String stWilayahID) {
        this.stWilayahID = stWilayahID;
    }

    public String getStWilayahName() {
        return stWilayahName;
    }

    public void setStWilayahName(String stWilayahName) {
        this.stWilayahName = stWilayahName;
    }

    public String getStKreasiID() {
        return stKreasiID;
    }

    public void setStKreasiID(String stKreasiID) {
        this.stKreasiID = stKreasiID;
    }

    public String getStKreasiName() {
        return stKreasiName;
    }

    public void setStKreasiName(String stKreasiName) {
        this.stKreasiName = stKreasiName;
    }

    public String getStReinsID() {
        return stReinsID;
    }

    public void setStReinsID(String stReinsID) {
        this.stReinsID = stReinsID;
    }

    public String getStReinsName() {
        return stReinsName;
    }

    public void setStReinsName(String stReinsName) {
        this.stReinsName = stReinsName;
    }

    public DTOList EXCEL_STATISTIK_OLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code,a.pol_type_id,a.policy_date,a.pol_id,quote_ident(a.pol_no) as pol_no,a.status,a.ccy_rate,b.ins_pol_obj_id,"
                + "a.entity_id,c.ent_name as sumbis,c.ref2 as grupsumbis,a.prod_id,d.ent_name as marketer,d.ref2 as grupmarketer,"
                + "trim(b.description) as description,"
                + "(select vs_description from s_valueset where vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "and vs_code = (case when a.pol_type_id in (21,59) then b.ref13 else null end)) as coverage, "
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end, "
                + "case when a.status <> 'ENDORSE' then ( select sum(insured_amount * ccy_rate) "
                + "from ins_pol_tsi y where y.ins_pol_obj_id = b.ins_pol_obj_id) "
                + "else (b.insured_amount * ccy_rate) end  as tsi_obj,"
                + "( select sum(premi_new * ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "coalesce((select sum((y.amount*-1) * ccy_rate) from ins_pol_coins y "
                + "where y.policy_id = a.pol_id and y.entity_id <> 1),0)  as tsiko_obj,"
                + "a.premi_bruto,a.premiko,a.diskon,a.hfee,a.comm1,a.bfee,a.feebase,a.ppn ");

        sqa.addQuery(" from ins_policies3 a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ent_master d on d.ent_id = a.prod_id ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
//            sqa.addPar('%' + stPolicyNo + '%');
        }

        String sql = "select row_number() over(partition by 0) AS nomor,a.status,"
                + "a.entity_id,sumbis,a.grupsumbis,a.prod_id,marketer,a.grupmarketer,"
                + "a.cc_code,a.pol_type_id,a.pol_id,a.pol_no,a.ccy_rate,a.policy_date,a.ins_pol_obj_id,a.description,"
                + "a.tgl_lahir,period_start,period_end,tsi_obj,premi_obj,"
                + "round(tsiko_obj/debitur,2) as tsiko_obj,"
                + "round(premiko/debitur,2) as premiko_obj,"
                + "round(diskon/debitur,2) as diskon_obj,"
                + "round(comm1/debitur,2) as komisi_obj,"
                + "round(bfee/debitur,2) as brok1_obj,"
                + "round(hfee/debitur,2) as hfee_obj,"
                + "round(feebase/debitur,2) as feebase1_obj,"
                + "round(ppn/debitur,2) as ppn_obj,coverage from ( "
                + sqa.getSQL() + " order by a.pol_no,b.ins_pol_obj_id ) a "
                + "where (a.premi_obj <> 0 or a.premiko <> 0 or a.diskon <> 0 or a.comm1 <> 0 "
                + "or a.bfee <> 0 or a.hfee <> 0 or a.feebase <> 0  or a.ppn <> 0) "
                + "order by a.cc_code,a.pol_type_id,a.policy_date,a.pol_id ";

//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                HashDTO.class);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.pol_id,quote_ident(pol_no) as pol_no,b.ins_pol_obj_id,"
                + "a.entity_id,trim(c.ent_name) as sumbis,c.ref2 as grupsumbis,"
                + "a.prod_id,trim(d.ent_name) as marketer,d.ref2 as grupmarketer,"
                + "trim(b.description) as description,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "a.policy_date as tgl_polis,a.approved_date as tgl_setujui,a.claim_date as tgl_klaim,"
                + "(a.insured_amount*a.ccy_rate) as insured_amount, a.dla_no,"
                + "a.claim_chronology as kronologi,e.cause_desc, "
                + "(a.claim_amount*a.ccy_rate_claim) as claim_amount,a.status,a.claim_status,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or, "
                + "round(sum(checkreas(j.treaty_type<>'OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_ri , "
                + "(select string_agg(g.receipt_date::text, '| ') from ar_invoice g "
                + "where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_date,"
                + "(select string_agg(g.receipt_no, '| ') from ar_invoice g "
                + "where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_no ");

        sqa2.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ent_master d on d.ent_id = a.prod_id "
                + "left join ins_clm_cause e on e.ins_clm_caus_id = a.claim_cause "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa2.addClause("a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa2.addClause("a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status IN ('DLA')");

        if (appDateFrom != null) {
            sqa2.addClause("date_trunc('day',a.claim_approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa2.addClause("date_trunc('day',a.claim_approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa2.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa2.addClause("a.region_id = '" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        if (stCustCategory1 != null) {
            sqa2.addClause("c.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        if (stPolicyTypeGroupID != null) {
            sqa2.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa2.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa2.addClause("a.pol_no like '%" + stPolicyNo + "%'");
//            sqa.addPar('%' + stPolicyNo + '%');
        }

        String sql2 = "select row_number() over(partition by 0) AS nomor,cc_code,pol_type_id,"
                + "entity_id,sumbis,grupsumbis,status,claim_status,prod_id,marketer,grupmarketer,description,"
                + "pol_id,pol_no,tgl_lahir,ins_pol_obj_id,period_start,period_end,tgl_polis,tgl_setujui,"
                + "tgl_klaim,insured_amount,dla_no,kronologi,cause_desc,claim_amount,claim_or,claim_ri,receipt_date,receipt_no "
                + " from ( " + sqa2.getSQL() + " group by a.cc_code,a.pol_type_id,a.pol_id,a.pol_no,b.ins_pol_obj_id,a.entity_id,"
                + "c.ent_name,a.prod_id,d.ent_name,e.cause_desc,c.ref2,d.ref2 order by 1,2,4 ) a order by 1 ";

//        final DTOList l2 = ListUtil.getDTOListFromQuery(
//                sql2,
//                sqa2.getPar(),
//                HashDTO.class);
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);
//
//        return l;

        SQLUtil S = new SQLUtil();
        SQLUtil S2 = new SQLUtil();

        String nama_file1 = "premi_bulanan_statistik_" + DateUtil.getMonth2Digit(appDateFrom) + DateUtil.getYear(appDateFrom) + ".csv";
        String nama_file2 = "klaim_bulanan_statistik_" + DateUtil.getMonth2Digit(appDateFrom) + DateUtil.getYear(appDateFrom) + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file1 + "' With CSV HEADER;";

        sql2 = "Copy ("
                + sql2
                + " ) To 'D://exportdb/statistik/" + nama_file2 + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);
        final PreparedStatement ps2 = S2.setQuery(sql2);

        boolean tes = ps.execute();
        boolean tes2 = ps2.execute();

        S.release();
        S2.release();

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
//        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
        String INPUT_FILE = "P:\\statistik\\" + nama_file1;//utk core 250.53
        String COPY_FILE_TO = "W:\\statistik\\" + nama_file1;
        File source = new File(INPUT_FILE);
        File target = new File(COPY_FILE_TO);

        FileTransferDirectory transfer = new FileTransferDirectory();
        transfer.copyWithStreams(source, target, false);
        System.out.println(String.valueOf(INPUT_FILE));
        System.out.println(String.valueOf(COPY_FILE_TO));
        System.out.println("Done.");

        if (source.delete()) {
            System.out.println("Deleted the file: " + source.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
//        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
        String INPUT_FILE2 = "P:\\statistik\\" + nama_file2;//utk core 250.53
        String COPY_FILE_TO2 = "W:\\statistik\\" + nama_file2;
        File source2 = new File(INPUT_FILE2);
        File target2 = new File(COPY_FILE_TO2);

//        FileTransferDirectory transfer = new FileTransferDirectory();
        transfer.copyWithStreams(source2, target2, false);
        System.out.println(String.valueOf(INPUT_FILE2));
        System.out.println(String.valueOf(COPY_FILE_TO2));
        System.out.println("Done.");

        if (source2.delete()) {
            System.out.println("Deleted the file: " + source2.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        return new DTOList();
    }

    public DTOList EXCEL_STATISTIK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.policy_date,a.pol_id,a.pol_no,a.status,a.ccy_rate,a.entity_id,a.prod_id,"
                + "a.period_start,a.period_end,a.approved_date,getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) AS insured_amount,"
                + "a.premi_total*a.ccy_rate AS premi_total,a.premi_total_adisc*a.ccy_rate AS premi_total_adisc,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (29,22,36,15) AND a.pol_id = x.pol_id),0) AS nd_pcost,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (28,21,35,14) AND a.pol_id = x.pol_id),0) AS nd_sfee,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (25,18,32,11,112,116,117) AND a.pol_id = x.pol_id),0) AS nd_comm1,"
                + "COALESCE(( SELECT sum(x.tax_amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (25,18,32,11,112,116,117) AND x.tax_code in (1,2) "
                + "AND a.pol_id = x.pol_id),0) AS nd_taxcomm1,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) "
                + "AND a.pol_id = x.pol_id),0) AS nd_brok1,"
                + "COALESCE(( SELECT sum(x.tax_amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) "
                + "AND x.tax_code in (4,5,6) AND a.pol_id = x.pol_id),0) AS nd_taxbrok1,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (27,20,34,13) AND a.pol_id = x.pol_id),0) AS nd_hfee,"
                + "COALESCE(( SELECT sum(x.tax_amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (27,20,34,13) "
                + "AND x.tax_code = 9 AND a.pol_id = x.pol_id),0) AS nd_taxhfee,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (30,23,37,16) AND a.pol_id = x.pol_id),0) AS nd_disc1,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) "
                + "AND a.pol_id = x.pol_id),0) AS nd_feebase1,"
                + "COALESCE(( SELECT sum(x.amount*a.ccy_rate) AS sum FROM ins_pol_items x WHERE x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) "
                + "AND a.pol_id = x.pol_id),0) AS nd_ppn ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
//            sqa.addPar('%' + stPolicyNo + '%');
        }

        String sql = "select row_number() over(partition by 0) AS nomor,a.status,"
                + "a.entity_id,a.sumbis, a.grupsumbis,a.prod_id,a.marketer, a.grupmarketer,"
                + "a.cc_code,a.pol_type_id,a.pol_id,a.pol_no,a.ccy_rate,a.policy_date,"
                + "a.ins_pol_obj_id,a.description,a.tgl_lahir,period_start,period_end,"
                + "tsi_obj,premi_obj,round(tsiko_obj/debitur,2) as tsiko_obj,"
                + "round(premiko/debitur,2) as premiko_obj,round(diskon/debitur,2) as diskon_obj,"
                + "round(comm1/debitur,2) as komisi_obj,round(bfee/debitur,2) as brok1_obj,"
                + "round(hfee/debitur,2) as hfee_obj,round(feebase/debitur,2) as feebase1_obj,"
                + "round(ppn/debitur,2) as ppn_obj,coverage from ( "
                + "select a.cc_code,a.pol_type_id,a.policy_date,a.pol_id,quote_ident(a.pol_no) as pol_no,"
                + "a.status,a.ccy_rate,b.ins_pol_obj_id,a.entity_id,c.ent_name as sumbis,c.ref2 as grupsumbis,"
                + "a.prod_id,d.ent_name as marketer,d.ref2 as grupmarketer,trim(b.description) as description,"
                + "(select vs_description from s_valueset where vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "and vs_code = (case when a.pol_type_id in (21,59) then b.ref13 else null end)) as coverage,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "case when a.status <> 'ENDORSE' then ( select sum(insured_amount*ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) else (b.insured_amount*ccy_rate) end  as tsi_obj,"
                + "( select sum(premi_new*ccy_rate) from ins_pol_cover y where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "coalesce((select sum((y.amount*-1)*ccy_rate) from ins_pol_coins y where y.policy_id = a.pol_id and y.entity_id <> 1),0)  as tsiko_obj,"
                + "a.premi_bruto,a.premiko,a.diskon,a.hfee,a.comm1,a.bfee,a.feebase,a.ppn from ( "
                + "SELECT a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.policy_date,a.pol_id,a.pol_no,a.status,a.ccy_rate,a.entity_id,a.prod_id,"
                + "a.period_start,a.period_end,a.approved_date,sum(getpremiend(b.entity_id,COALESCE(a.insured_amount,0),0)) AS tsi_bruto,"
                + "sum(getpremiend(b.entity_id,0,COALESCE(b.amount*a.ccy_rate,0))) AS tsiko,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.premi_total,0),0)) AS premi_bruto,"
                + "sum(getpremiend(b.entity_id,0,COALESCE(b.premi_amt*a.ccy_rate,0))) AS premiko,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_pcost,0),0)) AS biapol,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_sfee,0),0)) AS biamat,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_ppn,0),0)) AS ppn,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_disc1,0),COALESCE(b.disc_amount*a.ccy_rate,0)*(-1))) AS diskon,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_hfee,0),COALESCE(b.hfee_amount*a.ccy_rate,0)*(-1))) AS hfee,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_taxhfee,0),0)) AS pajak_hfee,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_comm1,0),COALESCE(b.comm_amount*a.ccy_rate,0)*(-1))) AS comm1,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_taxcomm1,0),0)) AS pajak_comm1,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_brok1,0),COALESCE(b.broker_amount*a.ccy_rate,0)*(-1))) AS bfee,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_taxbrok1,0),0)) AS pajak_bfee,"
                + "sum(getpremiend(b.entity_id,COALESCE(a.nd_feebase1,0),0)) AS feebase FROM ( "
                + sqa.getSQL() + " ) a INNER JOIN ins_pol_coins b ON b.policy_id = a.pol_id "
                + "WHERE (b.entity_id <> 1 OR b.coins_type <> 'COINS_COVER') "
                + "GROUP BY a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.policy_date,a.pol_id,"
                + "a.pol_no,a.status,a.ccy_rate,a.entity_id,a.prod_id,a.period_start,a.period_end,"
                + "a.approved_date )  a inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ent_master d on d.ent_id = a.prod_id order by a.pol_no,b.ins_pol_obj_id "
                + ") a where (a.premi_obj <> 0 or a.premiko <> 0 or a.diskon <> 0 or a.comm1 <> 0 "
                + "or a.bfee <> 0 or a.hfee <> 0 or a.feebase <> 0  or a.ppn <> 0) "
                + "order by a.cc_code,a.pol_type_id,a.policy_date,a.pol_id ";

//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                HashDTO.class);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.pol_id,quote_ident(pol_no) as pol_no,b.ins_pol_obj_id,"
                + "a.entity_id,trim(c.ent_name) as sumbis,c.ref2 as grupsumbis,"
                + "a.prod_id,trim(d.ent_name) as marketer,d.ref2 as grupmarketer,"
                + "trim(b.description) as description,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "a.policy_date as tgl_polis,a.approved_date as tgl_setujui,a.claim_date as tgl_klaim,"
                + "(a.insured_amount*a.ccy_rate) as insured_amount, a.dla_no,"
                + "a.claim_chronology as kronologi,e.cause_desc, "
                + "(a.claim_amount*a.ccy_rate_claim) as claim_amount,a.status,a.claim_status,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or, "
                + "round(sum(checkreas(j.treaty_type<>'OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_ri , "
                + "(select string_agg(g.receipt_date::text, '| ') from ar_invoice g "
                + "where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_date,"
                + "(select string_agg(g.receipt_no, '| ') from ar_invoice g "
                + "where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_no ");

        sqa2.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ent_master d on d.ent_id = a.prod_id "
                + "left join ins_clm_cause e on e.ins_clm_caus_id = a.claim_cause "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa2.addClause("a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa2.addClause("a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status IN ('DLA')");

        if (appDateFrom != null) {
            sqa2.addClause("date_trunc('day',a.claim_approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa2.addClause("date_trunc('day',a.claim_approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa2.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa2.addClause("a.region_id = '" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        if (stCustCategory1 != null) {
            sqa2.addClause("c.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        if (stPolicyTypeGroupID != null) {
            sqa2.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa2.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa2.addClause("a.pol_no like '%" + stPolicyNo + "%'");
//            sqa.addPar('%' + stPolicyNo + '%');
        }

        String sql2 = "select row_number() over(partition by 0) AS nomor,cc_code,pol_type_id,"
                + "entity_id,sumbis,grupsumbis,status,claim_status,prod_id,marketer,grupmarketer,description,"
                + "pol_id,pol_no,tgl_lahir,ins_pol_obj_id,period_start,period_end,tgl_polis,tgl_setujui,"
                + "tgl_klaim,insured_amount,dla_no,kronologi,cause_desc,claim_amount,claim_or,claim_ri,receipt_date,receipt_no "
                + " from ( " + sqa2.getSQL() + " group by a.cc_code,a.pol_type_id,a.pol_id,a.pol_no,b.ins_pol_obj_id,a.entity_id,"
                + "c.ent_name,a.prod_id,d.ent_name,e.cause_desc,c.ref2,d.ref2 order by 1,2,4 ) a order by 1 ";

//        final DTOList l2 = ListUtil.getDTOListFromQuery(
//                sql2,
//                sqa2.getPar(),
//                HashDTO.class);
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);
//
//        return l;

        SQLUtil S = new SQLUtil();
        SQLUtil S2 = new SQLUtil();

        String nama_file1 = "premi_bulanan_statistik_" + DateUtil.getMonth2Digit(appDateFrom) + DateUtil.getYear(appDateFrom) + ".csv";
        String nama_file2 = "klaim_bulanan_statistik_" + DateUtil.getMonth2Digit(appDateFrom) + DateUtil.getYear(appDateFrom) + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file1 + "' With CSV HEADER;";

        sql2 = "Copy ("
                + sql2
                + " ) To 'D://exportdb/statistik/" + nama_file2 + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);
        final PreparedStatement ps2 = S2.setQuery(sql2);

        boolean tes = ps.execute();
        boolean tes2 = ps2.execute();

        S.release();
        S2.release();

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
//        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
        String INPUT_FILE = "P:\\statistik\\" + nama_file1;//utk core 250.53
        String COPY_FILE_TO = "W:\\statistik\\" + nama_file1;
        File source = new File(INPUT_FILE);
        File target = new File(COPY_FILE_TO);

        FileTransferDirectory transfer = new FileTransferDirectory();
        transfer.copyWithStreams(source, target, false);
        System.out.println(String.valueOf(INPUT_FILE));
        System.out.println(String.valueOf(COPY_FILE_TO));
        System.out.println("Done.");

        if (source.delete()) {
            System.out.println("Deleted the file: " + source.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
//        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
        String INPUT_FILE2 = "P:\\statistik\\" + nama_file2;//utk core 250.53
        String COPY_FILE_TO2 = "W:\\statistik\\" + nama_file2;
        File source2 = new File(INPUT_FILE2);
        File target2 = new File(COPY_FILE_TO2);

//        FileTransferDirectory transfer = new FileTransferDirectory();
        transfer.copyWithStreams(source2, target2, false);
        System.out.println(String.valueOf(INPUT_FILE2));
        System.out.println(String.valueOf(COPY_FILE_TO2));
        System.out.println("Done.");

        if (source2.delete()) {
            System.out.println("Deleted the file: " + source2.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        return new DTOList();
    }

    public void EXPORT_STATISTIK() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheetpremi = wb.createSheet("premi");
        XSSFSheet sheetklaim = wb.createSheet("klaim");

        final DTOList listpremi = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList listklaim = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int i = 0; i < listpremi.size(); i++) {
            HashDTO h = (HashDTO) listpremi.get(i);

            //bikin header
            XSSFRow row0 = sheetpremi.createRow(0);
            row0.createCell(0).setCellValue("nomor");
            row0.createCell(1).setCellValue("status");
            row0.createCell(2).setCellValue("entity_id");
            row0.createCell(3).setCellValue("sumbis");
            row0.createCell(4).setCellValue("grupsumbis");
            row0.createCell(5).setCellValue("prod_id");
            row0.createCell(6).setCellValue("marketer");
            row0.createCell(7).setCellValue("grupmarketer");
            row0.createCell(8).setCellValue("cc_code");
            row0.createCell(9).setCellValue("pol_type_id");
            row0.createCell(10).setCellValue("pol_id");
            row0.createCell(11).setCellValue("pol_no");
            row0.createCell(12).setCellValue("ccy_rate");
            row0.createCell(13).setCellValue("policy_date");
            row0.createCell(14).setCellValue("ins_pol_obj_id");
            row0.createCell(15).setCellValue("description");
            row0.createCell(16).setCellValue("tgl_lahir");
            row0.createCell(17).setCellValue("period_start");
            row0.createCell(18).setCellValue("period_end");
            row0.createCell(19).setCellValue("tsi_obj");
            row0.createCell(20).setCellValue("premi_obj");
            row0.createCell(21).setCellValue("tsiko_obj");
            row0.createCell(22).setCellValue("premiko_obj");
            row0.createCell(23).setCellValue("diskon_obj");
            row0.createCell(24).setCellValue("komisi_obj");
            row0.createCell(25).setCellValue("brok1_obj");
            row0.createCell(26).setCellValue("hfee_obj");
            row0.createCell(27).setCellValue("feebase1_obj");
            row0.createCell(28).setCellValue("ppn_obj");
            row0.createCell(29).setCellValue("coverage");

            //bikin isi cell
            XSSFRow row = sheetpremi.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("nomor").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("entity_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("grupsumbis"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("prod_id").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("marketer"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("grupmarketer"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_obj_id").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("description"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi_obj").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tsiko_obj").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("premiko_obj").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("diskon_obj").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("komisi_obj").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("brok1_obj").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("hfee_obj").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("feebase1_obj").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("ppn_obj").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("coverage"));

        }

        for (int j = 0; j < listklaim.size(); j++) {
            HashDTO h = (HashDTO) listklaim.get(j);

            //bikin header
            XSSFRow row0 = sheetklaim.createRow(0);
            row0.createCell(0).setCellValue("nomor");
            row0.createCell(1).setCellValue("cc_code");
            row0.createCell(2).setCellValue("pol_type_id");
            row0.createCell(3).setCellValue("entity_id");
            row0.createCell(4).setCellValue("sumbis");
            row0.createCell(5).setCellValue("grupsumbis");
            row0.createCell(6).setCellValue("status");
            row0.createCell(7).setCellValue("claim_status");
            row0.createCell(8).setCellValue("prod_id");
            row0.createCell(9).setCellValue("marketer");
            row0.createCell(10).setCellValue("grupmarketer");
            row0.createCell(11).setCellValue("description");
            row0.createCell(12).setCellValue("pol_id");
            row0.createCell(13).setCellValue("pol_no");
            row0.createCell(14).setCellValue("tgl_lahir");
            row0.createCell(15).setCellValue("ins_pol_obj_id");
            row0.createCell(16).setCellValue("period_start");
            row0.createCell(17).setCellValue("period_end");
            row0.createCell(18).setCellValue("tgl_polis");
            row0.createCell(19).setCellValue("tgl_setujui");
            row0.createCell(20).setCellValue("tgl_klaim");
            row0.createCell(21).setCellValue("insured_amount");
            row0.createCell(22).setCellValue("dla_no");
            row0.createCell(23).setCellValue("kronologi");
            row0.createCell(24).setCellValue("cause_desc");
            row0.createCell(25).setCellValue("claim_amount");
            row0.createCell(26).setCellValue("claim_or");
            row0.createCell(27).setCellValue("claim_ri");
            row0.createCell(28).setCellValue("receipt_date");
            row0.createCell(29).setCellValue("receipt_no");

            //bikin isi cell
            XSSFRow row = sheetklaim.createRow(j + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("nomor").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("entity_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("grupsumbis"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("claim_status"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("prod_id").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("marketer"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("grupmarketer"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_obj_id").doubleValue());
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("tgl_polis"));
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("tgl_setujui"));
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameDT("tgl_klaim"));
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameST("cause_desc"));
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("claim_ri").doubleValue());
            if (h.getFieldValueByFieldNameST("receipt_date") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("receipt_date"));
            }
            if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public String getStGroupID() {
        return stGroupID;
    }

    public void setStGroupID(String stGroupID) {
        this.stGroupID = stGroupID;
    }

    public String getStGroupName() {
        return stGroupName;
    }

    public void setStGroupName(String stGroupName) {
        this.stGroupName = stGroupName;
    }

    public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public String getStRegion() {
        return stRegion;
    }

    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }

    public String getStRegionDesc() {
        return stRegionDesc;
    }

    public void setStRegionDesc(String stRegionDesc) {
        this.stRegionDesc = stRegionDesc;
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    public String getStRegionName() {
        return stRegionName;
    }

    public void setStRegionName(String stRegionName) {
        this.stRegionName = stRegionName;
    }

    public RegionView getRegion() {
        final RegionView region = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegion);

        return region;
    }

    public boolean isCanNavigateRegion() {
        return canNavigateRegion;
    }

    public void setCanNavigateRegion(boolean canNavigateRegion) {
        this.canNavigateRegion = canNavigateRegion;
    }

    public String getStTriwulan() {
        return stTriwulan;
    }

    public void setStTriwulan(String stTriwulan) {
        this.stTriwulan = stTriwulan;
    }

    public String getStCompanyProdID() {
        return stCompanyProdID;
    }

    public void setStCompanyProdID(String stCompanyProdID) {
        this.stCompanyProdID = stCompanyProdID;
    }

    public String getStCompanyProdName() {
        return stCompanyProdName;
    }

    public void setStCompanyProdName(String stCompanyProdName) {
        this.stCompanyProdName = stCompanyProdName;
    }

    /**
     * @return the stValidasi
     */
    public String getStValidasi() {
        return stValidasi;
    }

    /**
     * @param stValidasi the stValidasi to set
     */
    public void setStValidasi(String stValidasi) {
        this.stValidasi = stValidasi;
    }

    public EntityView getMarketer() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stMarketerID);

        return entity;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    /**
     * @return the stFFac
     */
    public String getStFFac() {
        return stFFac;
    }

    /**
     * @param stFFac the stFFac to set
     */
    public void setStFFac(String stFFac) {
        this.stFFac = stFFac;
    }

    public InsurancePolicyTypeGroupView getPolGroup() {
        final InsurancePolicyTypeGroupView polgroup = (InsurancePolicyTypeGroupView) DTOPool.getInstance().getDTO(InsurancePolicyTypeGroupView.class, stPolicyTypeGroupID);

        return polgroup;
    }

    public InsurancePolicyTypeView getPolType() {
        final InsurancePolicyTypeView poltype = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);

        return poltype;
    }

    /**
     * @return the stMarketerOffID
     */
    public String getStMarketerOffID() {
        return stMarketerOffID;
    }

    /**
     * @param stMarketerOffID the stMarketerOffID to set
     */
    public void setStMarketerOffID(String stMarketerOffID) {
        this.stMarketerOffID = stMarketerOffID;
    }

    /**
     * @return the stMarketerOffName
     */
    public String getStMarketerOffName() {
        return stMarketerOffName;
    }

    /**
     * @param stMarketerOffName the stMarketerOffName to set
     */
    public void setStMarketerOffName(String stMarketerOffName) {
        this.stMarketerOffName = stMarketerOffName;
    }

    /**
     * @return the stProfileID
     */
    public String getStProfileID() {
        return stProfileID;
    }

    /**
     * @param stProfileID the stProfileID to set
     */
    public void setStProfileID(String stProfileID) {
        this.stProfileID = stProfileID;
    }

    /**
     * @return the stProfileName
     */
    public String getStProfileName() {
        return stProfileName;
    }

    /**
     * @param stProfileName the stProfileName to set
     */
    public void setStProfileName(String stProfileName) {
        this.stProfileName = stProfileName;
    }

    /**
     * @return the stGroupJenisID
     */
    public String getStGroupJenisID() {
        return stGroupJenisID;
    }

    /**
     * @param stGroupJenisID the stGroupJenisID to set
     */
    public void setStGroupJenisID(String stGroupJenisID) {
        this.stGroupJenisID = stGroupJenisID;
    }

    /**
     * @return the stGroupJenisName
     */
    public String getStGroupJenisName() {
        return stGroupJenisName;
    }

    /**
     * @param stGroupJenisName the stGroupJenisName to set
     */
    public void setStGroupJenisName(String stGroupJenisName) {
        this.stGroupJenisName = stGroupJenisName;
    }

    /**
     * @return the stGroupOutgoID
     */
    public String getStGroupOutgoID() {
        return stGroupOutgoID;
    }

    /**
     * @param stGroupOutgoID the stGroupOutgoID to set
     */
    public void setStGroupOutgoID(String stGroupOutgoID) {
        this.stGroupOutgoID = stGroupOutgoID;
    }

    /**
     * @return the stGroupOutgoName
     */
    public String getStGroupOutgoName() {
        return stGroupOutgoName;
    }

    /**
     * @param stGroupOutgoName the stGroupOutgoName to set
     */
    public void setStGroupOutgoName(String stGroupOutgoName) {
        this.stGroupOutgoName = stGroupOutgoName;
    }

    public boolean isClosed() throws Exception {
        String month = null;
        String years = null;

        if (policyDateFrom != null) {
            month = DateUtil.getMonth2Digit(policyDateFrom);
            years = DateUtil.getYear(policyDateFrom);
        }

        if (appDateFrom != null) {
            month = DateUtil.getMonth2Digit(appDateFrom);
            years = DateUtil.getYear(appDateFrom);
        }

        SQLUtil S = new SQLUtil();

        boolean isClosed = false;

        try {
            String cek = "select gl_post_id from ins_closing where months = ? and years = ? and posted_flag = 'Y' ";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, month);
            PS.setString(2, years);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isClosed = true;
            }

        } finally {
            S.release();
        }

        return isClosed;
    }

    public DTOList EXCEL_RISK_CONTROL_3() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.period_start,a.period_end,f.ref2 as grupmarketer,f.ent_name as marketer,e.ref2 as grupsumbis,a.entity_id,e.ent_name,a.cust_name,"
                + "a.status,a.pol_type_id,a.policy_date,a.pol_id,pol_no,substr(a.pol_no,0,17) as sub_polno,b.ins_pol_obj_id,b.ins_pol_obj_ref_root_id,"
                + "b.order_no,(checkstatus(a.pol_type_id in (60),trim(b.ref3d),trim(b.ref1))) as nama,"
                + "REPLACE(trim(b.description),' (VOID)','') as description,b.debitur_act,b.ref1,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,getperiod(a.pol_type_id=60,b.refd4,a.period_start)))) as refd2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,getperiod(a.pol_type_id=60,b.refd5,a.period_end)))) as refd3, "
                + "a.ccy,a.ccy_rate,getpremi(status='ENDORSE',(a.insured_amount_e*a.ccy_rate),(a.insured_amount*a.ccy_rate)) as insured_amount,"
                + "NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2,NULLIF(a.premi_total*ccy_rate,0) as premi_total,"
                + "(select premi_total from (select distinct pol_no,NULLIF(y.premi_total*ccy_rate,0) as premi_total from ins_policy y "
                + "where y.status in ('POLICY','RENEWAL','HISTORY') and y.active_flag = 'Y' and y.effective_flag = 'Y' "
                + ") y where substr(y.pol_no,0,17) = substr(a.pol_no,0,17) and a.status = 'ENDORSE') as premi_total_ref,"
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj, "
                + "(select sum((y.insured_amount * a.ccy_rate)*-1) from ins_pol_tsi y where y.ins_pol_tsi_id in (select ins_pol_tsi_ref_id from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id)) as tsi_obj_ref, "
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "(select sum(y.premi_total * a.ccy_rate) from ins_pol_obj y where y.ins_pol_obj_id in (select ins_pol_obj_ref_root_id from ins_pol_obj y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id)) as premi_obj_ref2,"
                + "(select sum(z.premi_new * a.ccy_rate) from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id where y.ins_pol_obj_ref_root_id in ("
                + "select ins_pol_obj_ref_root_id from ins_pol_obj y where y.ins_pol_obj_id = b.ins_pol_obj_id) and substr(x.pol_no,0,17) = substr(a.pol_no,0,17) "
                + "and x.status in ('POLICY','RENEWAL','HISTORY') and x.active_flag = 'Y' and x.effective_flag = 'Y' ) as premi_obj_ref,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as disc_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as komisi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as bfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as hfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as fbase_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as ppn_obj,"
                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko_obj, "
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko_obj, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko_obj, "
                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko_obj, "
                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko_obj, "
                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko_obj, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount * a.ccy_rate)) "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as other,"
                + " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount * a.ccy_rate)),2) as tsi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.premi_amount * a.ccy_rate)),2) as premi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt * a.ccy_rate)),2) as komisi_or, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount * a.ccy_rate)),2) as tsi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount * a.ccy_rate)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt * a.ccy_rate)),2) as komisi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.tsi_amount * a.ccy_rate)),2) as tsi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.premi_amount * a.ccy_rate)),2) as premi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.ricomm_amt * a.ccy_rate)),2) as komisi_kscbi, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount * a.ccy_rate)),2) as tsi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount * a.ccy_rate)),2) as premi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt * a.ccy_rate)),2) as komisi_spl, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount * a.ccy_rate)),2) as tsi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount * a.ccy_rate)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt * a.ccy_rate)),2) as komisi_fac, "
                + " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount * a.ccy_rate)),2) as tsi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount * a.ccy_rate)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt * a.ccy_rate)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount * a.ccy_rate)),2) as tsi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount * a.ccy_rate)),2) as premi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt * a.ccy_rate)),2) as komisi_park, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount * a.ccy_rate)),2) as tsi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount * a.ccy_rate)),2) as premi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount * a.ccy_rate)),2) as tsi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount * a.ccy_rate)),2) as premi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount * a.ccy_rate)),2) as tsi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount * a.ccy_rate)),2) as premi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount * a.ccy_rate)),2) as tsi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount * a.ccy_rate)),2) as premi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco3, "
                + " round(sum(checkreas(j.treaty_type='JP',i.tsi_amount * a.ccy_rate)),2) as tsi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.premi_amount * a.ccy_rate)),2) as premi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt * a.ccy_rate)),2) as komisi_jp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.tsi_amount * a.ccy_rate)),2) as tsi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.premi_amount * a.ccy_rate)),2) as premi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.ricomm_amt * a.ccy_rate)),2) as komisi_facp,"
                + " round(sum(checkreas(j.treaty_type='QSKR',i.tsi_amount * a.ccy_rate)),2) as tsi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.premi_amount * a.ccy_rate)),2) as premi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.ricomm_amt * a.ccy_rate)),2) as komisi_qskr, "
                + " c.vs_description as coverage,b.ref2,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.ref9d,null)) as kode_pos,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),d.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),k.ins_risk_cat_code,null)) as ins_risk_cat_code,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,a.cover_type_code,"
                + "coalesce((SELECT SUM(rate) FROM INS_POL_ITEMS y WHERE y.pol_id = a.pol_id and y.ins_item_id in (select ins_item_id from ins_items "
                + "where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct,"
                + "(select string_agg(l.description||' - rate : '||round(getvalid2(z.rate is null,z.rate),3)||' - insured : '||z.insured_amount, '| ')"
                + "from ins_cover l inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1,"
                + "m.vs_description as kreasi_type_desc,a.endorse_notes ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = i.ins_treaty_detail_id "
                + "left join s_valueset c on c.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and c.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join s_valueset d on d.vs_code = b.ref7 and d.vs_group = 'INSOBJ_CREDIT_STATUS' "
                + "left join s_valueset l on l.vs_code = b.ref4 and l.vs_group = 'INSOBJ_PEKERJAAN_DEBITUR' "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id  "
                + "left join ins_risk_cat k on k.ins_risk_cat_id = b.ins_risk_cat_id "
                + "left join s_valueset m on m.vs_code = a.kreasi_type_id and m.vs_group = 'INSOBJ_KREASI_KREDIT' ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
//        sqa.addClause(" a.pol_type_id in (21,59,60,73,74,80) ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = '" + stPostCode + "'");
            //sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = '" + stPostCodeDesc2 + "'");
            //sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = '" + stZoneCode + "'");
            //sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = '" + stZoneEquakeName + "'");
            //sqa.addPar(stZoneEquakeName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
            //sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = '" + stYearTreaty + "'");
            //sqa.addPar(stYearTreaty);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("coalesce(a.effective_flag,'') <> 'Y'");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        if (stCreditID != null) {
            sqa.addClause("a.kreasi_type_id = '" + stCreditID + "'");
//            sqa.addPar(stCreditID);
        }

        String sql = "select period_start,period_end,policy_date,status,quote_ident(pol_no) as pol_no,grupmarketer,marketer,entity_id,ent_name,cust_name,ccy_rate,ins_pol_obj_id,"
                + "ref1,coverage,kreasi_type_desc,ref2,kode_pos,kriteria,risk_class,tgl_lahir,refd2,refd3,ins_risk_cat_code,ccy,"
                + "tsi_e as tsi_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*tsiko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(tsiko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*tsiko_obj,2) end,0) as tsiko_obj,"
                + "premi_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*premiko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(premiko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*premiko_obj,2) end,0) as premiko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*diskonko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(diskonko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*diskonko_obj,2) end,0) as diskonko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*commko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(commko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*commko_obj,2) end,0) as commko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*bfeeko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(bfeeko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*bfeeko_obj,2) end,0) as bfeeko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*hfeeko_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(hfeeko_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*hfeeko_obj,2) end,0) as hfeeko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*disc_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(disc_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*disc_obj,2) end,0) as disc_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*komisi_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(komisi_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*komisi_obj,2) end,0) as komisi_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*bfee_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(bfee_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*bfee_obj,2) end,0) as bfee_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*hfee_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(hfee_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*hfee_obj,2) end,0) as hfee_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*fbase_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(fbase_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*fbase_obj,2) end,0) as fbase_obj,"
                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*ppn_obj,2) "
                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(ppn_obj/debitur,2) "
                + "else round((premi_obj_ref/premi_total_ref)*ppn_obj,2) end,0) as ppn_obj,"
                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,"
                + "tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,"
                + "tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,"
                + "tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,cover_type_code,total_komisi_pct,desc1,endorse_notes "
                + "from ( "
                + "select status_deb,status,period_start,period_end,grupmarketer,marketer,grupsumbis,entity_id,ent_name,cust_name,pol_type_id,policy_date,pol_id,"
                + "pol_no,sub_polno,ins_pol_obj_id,ref1,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,"
                + "insured_amount,premi_total2,premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,tsi_e,premi_obj,disc_obj,komisi_obj,bfee_obj,"
                + "hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,tsi_or,"
                + "premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,"
                + "premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,"
                + "komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,"
                + "komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,kreasi_type_desc,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,"
                + "risk_class,cover_type_code,desc1,endorse_notes from ( "
                + "select case when status IN ('POLICY','RENEWAL','HISTORY') then 'POLICY' "
                + "when status IN ('ENDORSE') and tsi_obj_ref is null then 'ADDED' "
                + "when tsi_e <> 0 and premi_obj <> 0 and tsi_obj_ref is not null then 'RESTITUSI' "
                + "when tsi_e <> 0 and premi_obj = 0 and tsi_obj_ref is not null then 'ENDTSI' "
                + "when tsi_e = 0 and premi_obj <> 0 and tsi_obj_ref is not null then 'ENDPREMI' "
                + "when tsi_e = 0 and premi_obj = 0 and tsi_obj_ref is not null and premi_total is null "
                + " and (disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0 or tsiko_obj <> 0 or "
                + "premiko_obj <> 0 or diskonko_obj <> 0 or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0) then 'OTHERS' "
                + "end as status_deb,status, period_start,period_end,grupmarketer,marketer,grupsumbis,entity_id,ent_name,cust_name,pol_type_id,policy_date,pol_id,"
                + "pol_no,sub_polno,ins_pol_obj_id,ref1,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,"
                + "insured_amount,premi_total2,premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,tsi_e,premi_obj,disc_obj,komisi_obj,bfee_obj,"
                + "hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,tsi_or,"
                + "premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,"
                + "premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,"
                + "komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,"
                + "komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,kreasi_type_desc,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,"
                + "risk_class,cover_type_code,desc1,endorse_notes from ( "
                + "select period_start,period_end,grupmarketer,marketer,grupsumbis,entity_id,ent_name,cust_name,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,"
                + "ins_pol_obj_id,ref1,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,insured_amount,premi_total2,"
                + "premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,getpremi(status='ENDORSE',(coalesce(tsi_obj,0)+coalesce(tsi_obj_ref,0)),coalesce(tsi_obj,0)) as tsi_e,"
                + "coalesce(premi_obj,0) as premi_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,"
                + "hfeeko_obj,building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,"
                + "premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,"
                + "premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,"
                + "tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,kreasi_type_desc,desc1,endorse_notes "
                + "from ( " + sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,b.ref1,"
                + "b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,"
                + "a.share_pct,a.ccy,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total,b.premi_total_bcoins,d.vs_description,k.ins_risk_cat_code,"
                + "l.vs_description,c.vs_description,f.ref2,f.ent_name,e.ref2,e.ent_name,m.vs_description order by a.pol_no,b.ins_pol_obj_id "
                + ") a where a.ins_pol_obj_id is not null ";

//        String sql = "select status,period_start,period_end,grupmarketer,marketer,grupsumbis,sumbis,cust_name,pol_type_id,policy_date,pol_id,"
//                + "quote_ident(pol_no) as pol_no,ins_pol_obj_id,order_no,nama,description,tgl_lahir,refd2,refd3,ccy,ccy_rate,tsi_e as tsi_obj,premi_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*disc_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(disc_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*disc_obj,2) end,0) as disc_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*komisi_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(komisi_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*komisi_obj,2) end,0) as komisi_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*bfee_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(bfee_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*bfee_obj,2) end,0) as bfee_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*hfee_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(hfee_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*hfee_obj,2) end,0) as hfee_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*fbase_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(fbase_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*fbase_obj,2) end,0) as fbase_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*ppn_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(ppn_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*ppn_obj,2) end,0) as ppn_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*tsiko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(tsiko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*tsiko_obj,2) end,0) as tsiko_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*premiko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(premiko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*premiko_obj,2) end,0) as premiko_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*diskonko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(diskonko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*diskonko_obj,2) end,0) as diskonko_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*commko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(commko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*commko_obj,2) end,0) as commko_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*bfeeko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(bfeeko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*bfeeko_obj,2) end,0) as bfeeko_obj,"
//                + "coalesce(case when status_deb in ('POLICY','ADDED','RESTITUSI') then round((premi_obj/premi_total)*hfeeko_obj,2) "
//                + "when status_deb IN ('ENDTSI','ENDPREMI') then round(hfeeko_obj/debitur,2) "
//                + "else round((premi_obj_ref/premi_total_ref)*hfeeko_obj,2) end,0) as hfeeko_obj,"
//                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,"
//                + "premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,"
//                + "komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,"
//                + "komisi_jp,tsi_facp,premi_facp,komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,kreasi_type_desc,coverage,ref2,"
//                + "kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,desc1,endorse_notes from ( "
//                + "select status_deb,status,period_start,period_end,grupmarketer,marketer,grupsumbis,sumbis,cust_name,pol_type_id,policy_date,pol_id,"
//                + "pol_no,sub_polno,ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,"
//                + "insured_amount,premi_total2,premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,tsi_e,premi_obj,disc_obj,komisi_obj,bfee_obj,"
//                + "hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,tsi_or,"
//                + "premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,"
//                + "premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,"
//                + "komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,"
//                + "komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,kreasi_type_desc,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,"
//                + "risk_class,cover_type_code,desc1,endorse_notes from ( "
//                + "select case when status IN ('POLICY','RENEWAL','HISTORY') then 'POLICY' "
//                + "when status IN ('ENDORSE') and tsi_obj_ref is null then 'ADDED' "
//                + "when tsi_e <> 0 and premi_obj <> 0 and tsi_obj_ref is not null then 'RESTITUSI' "
//                + "when tsi_e <> 0 and premi_obj = 0 and tsi_obj_ref is not null then 'ENDTSI' "
//                + "when tsi_e = 0 and premi_obj <> 0 and tsi_obj_ref is not null then 'ENDPREMI' "
//                + "when tsi_e = 0 and premi_obj = 0 and tsi_obj_ref is not null and premi_total is null "
//                + " and (disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0 or tsiko_obj <> 0 or "
//                + "premiko_obj <> 0 or diskonko_obj <> 0 or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0) then 'OTHERS' "
//                + "end as status_deb,status, period_start,period_end,grupmarketer,marketer,grupsumbis,sumbis,cust_name,pol_type_id,policy_date,pol_id,"
//                + "pol_no,sub_polno,ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,"
//                + "insured_amount,premi_total2,premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,tsi_e,premi_obj,disc_obj,komisi_obj,bfee_obj,"
//                + "hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,tsi_or,"
//                + "premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,"
//                + "premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,"
//                + "komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,"
//                + "komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,kreasi_type_desc,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,"
//                + "risk_class,cover_type_code,desc1,endorse_notes from ( "
//                + "select period_start,period_end,grupmarketer,marketer,grupsumbis,sumbis,cust_name,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,"
//                + "ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,insured_amount,premi_total2,"
//                + "premi_total,premi_total_ref,premi_obj_ref,tsi_obj,tsi_obj_ref,getpremi(status='ENDORSE',(coalesce(tsi_obj,0)+coalesce(tsi_obj_ref,0)),coalesce(tsi_obj,0)) as tsi_e,"
//                + "coalesce(premi_obj,0) as premi_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,"
//                + "hfeeko_obj,building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,"
//                + "premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,"
//                + "premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,"
//                + "tsi_qskr,premi_qskr,komisi_qskr,total_komisi_pct,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,kreasi_type_desc,desc1,endorse_notes "
//                + "from ( " + sqa.getSQL()
//                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,b.ref1,"
//                + "b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,"
//                + "a.share_pct,a.ccy,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total,b.premi_total_bcoins,d.vs_description,k.ins_risk_cat_code,"
//                + "l.vs_description,c.vs_description,f.ref2,f.ent_name,e.ref2,e.ent_name,m.vs_description order by a.pol_no,b.ins_pol_obj_id "
//                + ") a where a.ins_pol_obj_id is not null ";

        if (periodFrom != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sql = sql + " and date_trunc('day',a.refd3) >= '" + periodEndFrom + "'";
            //sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sql = sql + " and date_trunc('day',a.refd3) <= '" + periodEndTo + "'";
            //sqa.addPar(periodEndTo);
        }

        sql = sql + ") a order by a.pol_no,a.ins_pol_obj_id ) a where status_deb is not null order by a.pol_no,a.ins_pol_obj_id "
                + ") a order by a.pol_no,a.ins_pol_obj_id ";

        String nama_file = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "statistik"; //"akuntansi"; //"statistik";
        String pass = "St@t1sT!k234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\statistik\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();

    }

    public DTOList EXCEL_RISK_CONTROL_1() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.period_start,a.period_end,a.policy_date,a.status,quote_ident(pol_no) as pol_no,a.active_flag,"
                + "f.ref2 as grupmarketer,f.ent_name as marketer,a.entity_id,e.ent_name as ent_name,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "(checkstatus(a.pol_type_id in (60),b.ref3d,b.ref1)) as ref1,c.vs_description as coverage,b.ref2,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.ref9d,null)) as kode_pos,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),d.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,getperiod(a.pol_type_id=60,b.refd4,a.period_start)))) as refd2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,getperiod(a.pol_type_id=60,b.refd5,a.period_end)))) as refd3, "
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),k.ins_risk_cat_code,null)) as ins_risk_cat_code,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,"
                + "a.cover_type_code,a.ccy, NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2, NULLIF(a.premi_total*ccy_rate,0) as premi_total,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,"
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,"
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "b.premi_total_bcoins as premi_total_e,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as nd_comm1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as nd_ppn,"
                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko, "
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko, "
                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko, "
                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko, "
                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount * a.ccy_rate)) "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as other,"
                + " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount * a.ccy_rate)),2) as tsi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.premi_amount * a.ccy_rate)),2) as premi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt * a.ccy_rate)),2) as komisi_or, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount * a.ccy_rate)),2) as tsi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount * a.ccy_rate)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt * a.ccy_rate)),2) as komisi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.tsi_amount * a.ccy_rate)),2) as tsi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.premi_amount * a.ccy_rate)),2) as premi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.ricomm_amt * a.ccy_rate)),2) as komisi_kscbi, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount * a.ccy_rate)),2) as tsi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount * a.ccy_rate)),2) as premi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt * a.ccy_rate)),2) as komisi_spl, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount * a.ccy_rate)),2) as tsi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount * a.ccy_rate)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt * a.ccy_rate)),2) as komisi_fac, "
                + " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount * a.ccy_rate)),2) as tsi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount * a.ccy_rate)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt * a.ccy_rate)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount * a.ccy_rate)),2) as tsi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount * a.ccy_rate)),2) as premi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt * a.ccy_rate)),2) as komisi_park, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount * a.ccy_rate)),2) as tsi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount * a.ccy_rate)),2) as premi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount * a.ccy_rate)),2) as tsi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount * a.ccy_rate)),2) as premi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount * a.ccy_rate)),2) as tsi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount * a.ccy_rate)),2) as premi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount * a.ccy_rate)),2) as tsi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount * a.ccy_rate)),2) as premi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco3, "
                + " round(sum(checkreas(j.treaty_type='JP',i.tsi_amount * a.ccy_rate)),2) as tsi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.premi_amount * a.ccy_rate)),2) as premi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt * a.ccy_rate)),2) as komisi_jp, "
                + " coalesce((SELECT SUM(rate) FROM INS_POL_ITEMS y WHERE y.pol_id = a.pol_id "
                + " and y.ins_item_id in (select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct,"
                + " (select string_agg(l.description||' - rate : '||round(getvalid2(z.rate is null,z.rate),3)||' - insured : '||z.insured_amount, '| ')"
                + " from ins_cover l inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1,"
                + " a.endorse_notes,m.vs_description as kreasi_type_desc ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = i.ins_treaty_detail_id "
                + "left join s_valueset c on c.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and c.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join s_valueset d on d.vs_code = b.ref7 and d.vs_group = 'INSOBJ_CREDIT_STATUS' "
                + "left join s_valueset l on l.vs_code = b.ref4 and l.vs_group = 'INSOBJ_PEKERJAAN_DEBITUR' "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id  "
                + "left join ins_risk_cat k on k.ins_risk_cat_id = b.ins_risk_cat_id "
                + "left join s_valueset m on m.vs_code = a.kreasi_type_id and m.vs_group = 'INSOBJ_KREASI_KREDIT' ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
//        sqa.addClause(" a.pol_type_id in (21,59,60,73,74,80) ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = '" + stPostCode + "'");
            //sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = '" + stPostCodeDesc2 + "'");
            //sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = '" + stZoneCode + "'");
            //sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = '" + stZoneEquakeName + "'");
            //sqa.addPar(stZoneEquakeName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
            //sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = '" + stYearTreaty + "'");
            //sqa.addPar(stYearTreaty);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("coalesce(a.effective_flag,'') <> 'Y'");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        if (stCreditID != null) {
            sqa.addClause("a.kreasi_type_id = '" + stCreditID + "'");
//            sqa.addPar(stCreditID);
        }

        String sql = "select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,"
                + "cust_name,ccy_rate,ins_pol_obj_id,ref1,coverage,kreasi_type_desc,ref2,kode_pos,kriteria,risk_class,active_flag,"
                + "tgl_lahir,refd2,refd3,ins_risk_cat_code,ccy,tsi_obj,tsiko_obj,premi_obj,premiko_obj,"
                + "diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,"
                + "building,machine,stock,other,tsi_or,premi_or ,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,"
                + "komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs ,komisi_qs,tsi_park,premi_park,"
                + "komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1 ,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,"
                + "tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,cover_type_code,total_komisi_pct,desc1,endorse_notes from ( "
                //                + "round((((komisi_obj+bfee_obj+hfee_obj+fbase_obj+ppn_obj)/getpremi(premi_obj=0,1,premi_obj)*100)),2) as total_komisi_pct,desc1 from ( "
                + "select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,"
                + "cust_name,ccy_rate,ins_pol_obj_id,ref1,coverage,kreasi_type_desc,ref2,kode_pos,kriteria,risk_class,active_flag,"
                + "tgl_lahir,refd2,refd3,ins_risk_cat_code,ccy,getpremi(status = 'ENDORSE',round(insured_amount/debitur,2),tsi_obj) as tsi_obj,"
                + "premi_obj,round(tsiko/debitur,2) as tsiko_obj,round(premiko/debitur,2) as premiko_obj, round(diskonko/debitur,2) as diskonko_obj,"
                + "round(commko/debitur,2) as commko_obj, round(bfeeko/debitur,2) as bfeeko_obj, round(hfeeko/debitur,2) as hfeeko_obj,"
                //                + "round(nd_disc1/debitur,2) as disc_obj,round(nd_comm1/debitur,2) as komisi_obj,round(nd_brok1/debitur,2) as bfee_obj,"
                //                + "round(nd_hfee/debitur,2) as hfee_obj,round(nd_feebase1/debitur,2) as fbase_obj,round(nd_ppn/debitur,2) as ppn_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_disc1/debitur,2),round((premi_obj/premi_total)*nd_disc1,2)) as disc_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_comm1/debitur,2),round((premi_obj/premi_total)*nd_comm1,2)) as komisi_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_brok1/debitur,2),round((premi_obj/premi_total)*nd_brok1,2)) as bfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_hfee/debitur,2),round((premi_obj/premi_total)*nd_hfee,2)) as hfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_feebase1/debitur,2) ,round((premi_obj/premi_total)*nd_feebase1,2)) as fbase_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_ppn/debitur,2),round((premi_obj/premi_total)*nd_ppn,2)) as ppn_obj,"
                + "building,machine,stock,other,tsi_or,premi_or ,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,"
                + "tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac, tsi_qs,premi_qs ,komisi_qs,tsi_park,premi_park,komisi_park,"
                + "tsi_faco,premi_faco,komisi_faco, tsi_faco1,premi_faco1 ,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,"
                + "tsi_jp,premi_jp,komisi_jp,cover_type_code,total_komisi_pct,desc1,endorse_notes from ( " + sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,"
                + "b.ins_pol_obj_id,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,c.vs_description,"
                + "b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,a.share_pct,a.ccy,a.insured_amount,a.premi_total,a.endorse_notes,m.vs_description,"
                + "b.insured_amount,b.premi_total,b.premi_total_bcoins,f.ref2,f.ent_name,e.ent_name,d.vs_description,k.ins_risk_cat_code,"
                + "l.vs_description order by a.pol_no,b.ins_pol_obj_id ) a where a.active_flag = 'Y' and a.debitur <> 0 ";

        if (periodFrom != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sql = sql + " and date_trunc('day',a.refd3) >= '" + periodEndFrom + "'";
            //sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sql = sql + " and date_trunc('day',a.refd3) <= '" + periodEndTo + "'";
            //sqa.addPar(periodEndTo);
        }

        sql = sql + ") a where (a.tsi_obj <> 0 or a.tsiko_obj <> 0 or a.premi_obj <> 0 or a.premiko_obj <> 0 or a.diskonko_obj <> 0 "
                + "or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0 or a.disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 "
                + "or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0) order by a.pol_no,a.ins_pol_obj_id ";

//        String namafile = null;
//        if (stNoUrut != null) {
//            namafile = "risk_control_statistik_" + stNoUrut + ".csv";
//        } else {
//            namafile = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";
//        }

        String nama_file = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "statistik"; //"akuntansi"; //"statistik";
        String pass = "St@t1sT!k234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\statistik\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        //COPY POLIS KE DIRECTORY
////        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
////        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
//        String INPUT_FILE = "P:\\statistik\\" + nama_file;//utk core 250.53
//        String COPY_FILE_TO = "W:\\statistik\\" + nama_file;
//        File source = new File(INPUT_FILE);
//        File target = new File(COPY_FILE_TO);
//
//        FileTransferDirectory transfer = new FileTransferDirectory();
//        transfer.copyWithStreams(source, target, false);
//        System.out.println(String.valueOf(INPUT_FILE));
//        System.out.println(String.valueOf(COPY_FILE_TO));
//        System.out.println("Done.");
//
//        if (source.delete()) {
//            System.out.println("Deleted the file: " + source.getName());
//        } else {
//            System.out.println("Failed to delete the file.");
//        }

        return new DTOList();

    }

    public DTOList EXCEL_RISK_CONTROL_2() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.policy_date,a.status,a.pol_id,quote_ident(pol_no) as pol_no,a.cust_name,b.ins_pol_obj_id,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end, "
                + "b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref7d as ref7,b.ref8d as ref8,c.vs_description as coverage,"
                + "b.refd1,b.refd2,b.refd3,b.refd4,b.refd5,a.cover_type_code,b.order_no,b.void_flag,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "a.ccy,(a.insured_amount * a.ccy_rate) as insured_amount,(a.premi_total * a.ccy_rate) as premi_total,NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2, "
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as insured_amount_obj,"
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_total_obj,"
                + "b.premi_total_bcoins as premi_total_e,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as discko, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount * a.ccy_rate)) "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as other,"
                + " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount * a.ccy_rate)),2) as tsi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.premi_amount * a.ccy_rate)),2) as premi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt * a.ccy_rate)),2) as komisi_or, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount * a.ccy_rate)),2) as tsi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount * a.ccy_rate)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt * a.ccy_rate)),2) as komisi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount * a.ccy_rate)),2) as tsi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount * a.ccy_rate)),2) as premi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt * a.ccy_rate)),2) as komisi_spl, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount * a.ccy_rate)),2) as tsi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount * a.ccy_rate)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt * a.ccy_rate)),2) as komisi_fac, "
                + " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount * a.ccy_rate)),2) as tsi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount * a.ccy_rate)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt * a.ccy_rate)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount * a.ccy_rate)),2) as tsi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount * a.ccy_rate)),2) as premi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt * a.ccy_rate)),2) as komisi_park, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount * a.ccy_rate)),2) as tsi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount * a.ccy_rate)),2) as premi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount * a.ccy_rate)),2) as tsi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount * a.ccy_rate)),2) as premi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount * a.ccy_rate)),2) as tsi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount * a.ccy_rate)),2) as premi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount * a.ccy_rate)),2) as tsi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount * a.ccy_rate)),2) as premi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco3, "
                + " round(sum(checkreas(j.treaty_type='JP',i.tsi_amount * a.ccy_rate)),2) as tsi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.premi_amount * a.ccy_rate)),2) as premi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt * a.ccy_rate)),2) as komisi_jp, "
                + " a.endorse_notes ");

        sqa.addQuery("from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id "
                + "left join ent_master e on e.ent_id = a.prod_id "
                + "left join s_valueset c on c.vs_code = b.ref13 and c.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
//        sqa.addClause(" b.premi_total <> 0 ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= '" + periodFrom + "'");
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= '" + periodTo + "'");
            //sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end) >= '" + periodEndFrom + "'");
            //sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sqa.addClause("date_trunc('day',a.period_end) <= '" + periodEndTo + "'");
            //sqa.addPar(periodEndTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = '" + stPostCode + "'");
            //sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = '" + stPostCodeDesc2 + "'");
            //sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = '" + stZoneCode + "'");
            //sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = '" + stZoneEquakeName + "'");
            //sqa.addPar(stZoneEquakeName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
            //sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = '" + stYearTreaty + "'");
            //sqa.addPar(stYearTreaty);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("coalesce(a.effective_flag,'') <> 'Y'");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("k.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        String sql = "select date_part('year', age(period_end, period_start)) AS years,"
                + "a.*,round(premiko/debitur,2) as premiko_obj,"
                + "round(nd_disc1/debitur,2)-round(discko/debitur,2) as disc_obj "
                + "from ( " + sqa.getSQL() + " group by a.period_start,a.period_end,a.policy_date,"
                + "a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,"
                + "b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,"
                + "c.vs_description,b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,a.share_pct,a.ccy,"
                + "a.insured_amount,a.premi_total,b.insured_amount,b.premi_total,b.premi_total_bcoins "
                + "order by a.pol_no,b.ins_pol_obj_id ) a where a.premi_total_obj <> 0 or a.premiko <> 0 or a.discko <> 0 or nd_disc1 <> 0 "
                + "order by a.pol_no,a.ins_pol_obj_id ";

        String nama_file = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
//        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
        String INPUT_FILE = "P:\\statistik\\" + nama_file;//utk core 250.53
        String COPY_FILE_TO = "W:\\statistik\\" + nama_file;
        File source = new File(INPUT_FILE);
        File target = new File(COPY_FILE_TO);

        FileTransferDirectory transfer = new FileTransferDirectory();
        transfer.copyWithStreams(source, target, false);
        System.out.println(String.valueOf(INPUT_FILE));
        System.out.println(String.valueOf(COPY_FILE_TO));
        System.out.println("Done.");

        if (source.delete()) {
            System.out.println("Deleted the file: " + source.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        return new DTOList();

    }
    private String year;
    private String perDateFrom;

    /**
     * @return the perDateFrom
     */
    public String getPerDateFrom() {
        return perDateFrom;
    }

    /**
     * @param perDateFrom the perDateFrom to set
     */
    public void setPerDateFrom(String perDateFrom) {
        this.perDateFrom = perDateFrom;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }
    private String stLossProfile;
    private String stLossProfileName;

    /**
     * @return the stLossProfile
     */
    public String getStLossProfile() {
        return stLossProfile;
    }

    /**
     * @param stLossProfile the stLossProfile to set
     */
    public void setStLossProfile(String stLossProfile) {
        this.stLossProfile = stLossProfile;
    }

    /**
     * @return the stLossProfileName
     */
    public String getStLossProfileName() {
        return stLossProfileName;
    }

    /**
     * @param stLossProfileName the stLossProfileName to set
     */
    public void setStLossProfileName(String stLossProfileName) {
        this.stLossProfileName = stLossProfileName;
    }

    public DTOList EXCEL_RKP_REINS() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,a.period_end))) as period_end,"
                + "a.dla_date,a.claim_date,a.policy_date,a.pol_id,quote_ident(pol_no) as pol_no,a.pla_no,a.dla_no,e.ent_name as sumbis,f.ent_name as pemasar,"
                + "a.cust_name,a.ccy_rate_claim,a.claim_currency as kurs,"
                + "(case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end) as tertanggung,"
                + "(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "coalesce((b.insured_amount*a.ccy_rate_claim),0) as insured_amount,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,"
                + "coalesce((a.claim_amount_est*a.ccy_rate_claim),0) as claim_amount_est,"
                + "coalesce((a.claim_amount_approved*a.ccy_rate_claim),0) as claim_amount_approved,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.claim_amount*a.ccy_rate_claim)),2) as claim_bppdan, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.claim_amount*a.ccy_rate_claim)),2) as claim_spl, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.claim_amount*a.ccy_rate_claim)),2) as claim_fac, "
                + "round(sum(checkreas(j.treaty_type='QS',i.claim_amount*a.ccy_rate_claim)),2) as claim_qs, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.claim_amount*a.ccy_rate_claim)),2) as claim_park, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.claim_amount*a.ccy_rate_claim)),2) as claim_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.claim_amount*a.ccy_rate_claim)),2) as claim_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.claim_amount*a.ccy_rate_claim)),2) as claim_faco2, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.claim_amount*a.ccy_rate_claim)),2) as claim_faco3, "
                + "round(sum(checkreas(j.treaty_type='JP',i.claim_amount*a.ccy_rate_claim)),2) as claim_jp, "
                + "round(sum(checkreas(j.treaty_type='QSKR',i.claim_amount*a.ccy_rate_claim)),2) as claim_qskr, "
                + "round(sum(checkreas(j.treaty_type='KSCBI',i.claim_amount*a.ccy_rate_claim)),2) as claim_kscbi, "
                + "a.claim_approved_date,k.cause_desc as penyebab,d.vs_description as coverage,a.kreasi_type_desc as jenis_kredit,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),m.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "checkstatus(a.pol_type_id in (1,2,19,81,83),(select string_agg(y.ins_risk_cat_code,'| ') "
                + "from ( select y.ins_risk_cat_code from ins_pol_obj x inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.ins_risk_cat_code ) y ), "
                + "(select string_agg(y.description,'| ') from ( select y.description from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id where x.pol_id = a.pol_id "
                + "group by y.description ) y )) as risk_code, "
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,"
                + "(select string_agg(g.receipt_date::text, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as tgl_bayar,"
                + "(select string_agg(g.receipt_no, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as nobuk, "
                + "a.cc_code,n.region_code,a.cc_code_source,o.region_code as region_code_source, "
                + "(case when a.pol_type_id in (87,88) then b.refn11 end) as premi100,"
                + "(case when a.pol_type_id in (87,88) then b.refd7 end) as tglawal_pk,"
                + "(case when a.pol_type_id in (87,88) then b.refd8 end) as tglakhir_pk ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join s_valueset d on d.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and d.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join s_valueset l on l.vs_code = b.ref4 and l.vs_group = 'INSOBJ_PEKERJAAN_DEBITUR' "
                + "left join s_valueset m on m.vs_code = b.ref7 and m.vs_group = 'INSOBJ_CREDIT_STATUS' "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id "
                + " left join s_region n on n.region_id = a.region_id "
                + " left join s_region o on o.region_id = a.region_id_source ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE') ");

        if (EFF_CLAIM) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (ACT_CLAIM) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = '" + stBranch + "'");
////            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("a.region_id = '" + stRegion + "'");
////            sqa.addPar(stRegion);
//        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("a.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("a.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("a.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((a.cc_code = '" + stBranch + "') or (a.cc_code = '80' and a.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("a.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("a.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
//            sqa.addPar(stCompanyID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
//            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
//            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
//            sqa.addPar(stCompanyProdID);
        }

        if (stCreditID != null) {
            sqa.addClause("a.kreasi_type_id = '" + stCreditID + "'");
//            sqa.addPar(stCreditID);
        }

        String sql = "select a.*,sum(checkreas(c.ins_item_id in (46),c.amount*a.ccy_rate_claim)) as KlaimBruto,"
                + "sum(checkreas(c.ins_item_id in (47),((c.amount*-1)*a.ccy_rate_claim))) as Deductible,"
                + "sum(checkreas(c.ins_item_id in (48),((c.amount*-1)*a.ccy_rate_claim))) as Subrogasi,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as BiayaAdjuster,"
                + "sum(checkreas(c.ins_item_id in (53),((c.amount*-1)*a.ccy_rate_claim))) as Salvage,"
                + "sum(checkreas(c.ins_item_id in (52),c.amount*a.ccy_rate_claim)) as BiayaDerek,"
                + "sum(checkreas(c.ins_item_id in (54),c.amount*a.ccy_rate_claim)) as ExGratiaKlaim,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as JasaBengkel,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code is not null,((c.tax_amount*-1)*a.ccy_rate_claim))) as PajakJasaBengkel,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as Ppn,"
                + "sum(checkreas(c.ins_item_id in (60),((c.amount*-1)*a.ccy_rate_claim))) as Depresiasi,"
                + "sum(checkreas(c.ins_item_id in (65),((c.amount*-1)*a.ccy_rate_claim))) as Penalty,"
                + "sum(checkreas(c.ins_item_id in (75),((c.amount*-1)*a.ccy_rate_claim))) as JoinPlacement,"
                + "sum(checkreas(c.ins_item_id in (79),((c.amount*-1)*a.ccy_rate_claim))) as CashCollateralSubrograsi,"
                + "sum(checkreas(c.ins_item_id not in (46,47,48,50,52,53,54,70,76,60,65,75,79) and item_class = 'CLM',c.amount*a.ccy_rate_claim)) as Lainlain "
                + "from ( " + sqa.getSQL() + "  group by a.pol_type_id,b.refd1,b.refd2,b.refd3,a.period_start,a.period_end,a.dla_date,a.claim_date,a.policy_date,a.pol_no,a.pla_no,a.dla_no,b.refn11,b.refd7,b.refd8,"
                + "e.ent_name,f.ent_name,a.cust_name,a.ccy_rate_claim,a.claim_currency,a.ins_policy_type_grp_id,b.ref2,b.ref3d,b.ref1,b.insured_amount,a.claim_amount,a.cc_code,n.region_code,a.cc_code_source,o.region_code,"
                + "a.claim_amount_est,a.claim_amount_approved,a.claim_approved_date,k.cause_desc,d.vs_description,a.kreasi_type_desc,a.pol_id,b.risk_class,l.vs_description,m.vs_description,a.pol_id "
                + "order by a.pol_no ) a inner join ins_pol_items c on c.pol_id = a.pol_id "
                + "group by period_start,period_end,dla_date,claim_date,policy_date,a.pol_id,pol_no,pla_no,dla_no,sumbis,pemasar,cust_name,tgl_lahir,ccy_rate_claim,kurs,tertanggung,insured_amount,"
                + "claim_amount,claim_amount_est,claim_amount_approved,claim_or,claim_bppdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_faco1,claim_faco2,claim_faco3,claim_jp,premi100,tglawal_pk,tglakhir_pk,"
                + "claim_qskr,claim_kscbi,claim_approved_date,penyebab,coverage,jenis_kredit,kriteria,risk_code,risk_class,tgl_bayar,nobuk,cc_code,region_code,cc_code_source,region_code_source order by a.pol_no,a.dla_no ";

//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                HashDTO.class);
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
//
//        return l;

//        String namafile = null;
//        if (stNoUrut != null) {
//            namafile = "risk_control_statistik_" + stNoUrut + ".csv";
//        } else {
//            namafile = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";
//        }

        String nama_file = "risk_control_klaim_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "statistik"; //"akuntansi"; //"statistik";
        String pass = "St@t1sT!k234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\statistik\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        //COPY POLIS KE DIRECTORY
////        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
////        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
//        String INPUT_FILE = "P:\\statistik\\" + nama_file;//utk core 250.53
//        String COPY_FILE_TO = "W:\\statistik\\" + nama_file;
//        File source = new File(INPUT_FILE);
//        File target = new File(COPY_FILE_TO);
//
//        FileTransferDirectory transfer = new FileTransferDirectory();
//        transfer.copyWithStreams(source, target, false);
//        System.out.println(String.valueOf(INPUT_FILE));
//        System.out.println(String.valueOf(COPY_FILE_TO));
//        System.out.println("Done.");
//
//        if (source.delete()) {
//            System.out.println("Deleted the file: " + source.getName());
//        } else {
//            System.out.println("Failed to delete the file.");
//        }

        return new DTOList();

    }

    public void EXPORT_RKP_REINS() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("uy");
            row0.createCell(1).setCellValue("period_start");
            row0.createCell(2).setCellValue("period_end");
            row0.createCell(3).setCellValue("pol_id");
            row0.createCell(4).setCellValue("dla_date");
            row0.createCell(5).setCellValue("claim_date");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("pol_no");
            row0.createCell(8).setCellValue("pla_no");
            row0.createCell(9).setCellValue("dla_no");
            row0.createCell(10).setCellValue("cust_name");
            row0.createCell(11).setCellValue("name");
            row0.createCell(12).setCellValue("coins_name");
            row0.createCell(13).setCellValue("coins_amount");
            row0.createCell(14).setCellValue("insured_amount");
            row0.createCell(15).setCellValue("claim_amount");
            row0.createCell(16).setCellValue("claim_amount_est");
            row0.createCell(17).setCellValue("claim_amount_approved");
            row0.createCell(18).setCellValue("claim_or");
            row0.createCell(19).setCellValue("claim_bppdan");
            row0.createCell(20).setCellValue("claim_spl");
            row0.createCell(21).setCellValue("claim_fac");
            row0.createCell(22).setCellValue("claim_qs");
            row0.createCell(23).setCellValue("claim_park");
            row0.createCell(24).setCellValue("claim_faco");
            row0.createCell(25).setCellValue("claim_faco1");
            row0.createCell(26).setCellValue("claim_faco2");
            row0.createCell(27).setCellValue("claim_faco3");
            row0.createCell(28).setCellValue("claim_jp");
            row0.createCell(29).setCellValue("claim_qskr");
            row0.createCell(30).setCellValue("claim_kscbi");
            row0.createCell(31).setCellValue("premiko");
            //row0.createCell(23).setCellValue("status");
            row0.createCell(32).setCellValue("cob");
            row0.createCell(33).setCellValue("penyebab klaim");
            row0.createCell(34).setCellValue("claim_approved_date");
            row0.createCell(35).setCellValue("No Bukti");
            row0.createCell(36).setCellValue("Tgl Bayar");
            row0.createCell(37).setCellValue("marketer");
            row0.createCell(38).setCellValue("Nama Nasabah");
            row0.createCell(39).setCellValue("No Rekening");
            row0.createCell(40).setCellValue("Entity ID");
            row0.createCell(41).setCellValue("Risk Code");
            row0.createCell(42).setCellValue("Status Kerugian");
            row0.createCell(43).setCellValue("Kurs Klaim");
            row0.createCell(44).setCellValue("Kurs");
            row0.createCell(45).setCellValue("Marketer ID");
            row0.createCell(46).setCellValue("Kronologi");
            if (getStPolicyTypeID() != null) {
                if (getStPolicyTypeID().equalsIgnoreCase("21")
                        || getStPolicyTypeID().equalsIgnoreCase("59")) {
                    row0.createCell(47).setCellValue("Tgl Lahir");
                }
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("uy"));
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("name"));
            if (h.getFieldValueByFieldNameST("coins_name") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("coins_name"));
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_est") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_est").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_approved") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_bppdan") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco1") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco2") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("claim_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco3") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("claim_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_jp") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("claim_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qskr") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("claim_qskr").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_kscbi") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("premiko").doubleValue());
            }
            /*
            if (getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus()==null) continue;
            row.createCell(23).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus());
             */
            if (h.getFieldValueByFieldNameST("pol_id") != null) {
                //row.createCell(31).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getStPolicyTypeDesc());
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                //row.createCell(32).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
                row.createCell(33).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            }
            if (ACT_CLAIM) {
                if (h.getFieldValueByFieldNameST("claim_status").equalsIgnoreCase("PLA")) {
                    if (h.getFieldValueByFieldNameDT("approved_date") != null) {
                        row.createCell(34).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
                    }
                }
            } else if (EFF_CLAIM) {
                if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                    row.createCell(34).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
                }
            }
            if (h.getFieldValueByFieldNameST("payment_note") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameST("payment_note"));
            }
            if (h.getFieldValueByFieldNameST("payment_date") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("payment_date"));
            }
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            if (h.getFieldValueByFieldNameST("ins_policy_type_grp_id").equalsIgnoreCase("10")) {
                if (h.getFieldValueByFieldNameST("claim_client_name") != null) {
                    row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("claim_client_name"));
                }
                if (h.getFieldValueByFieldNameST("claim_account_no") != null) {
                    row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("claim_account_no"));
                }
            }
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("entity_id"));
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("status_kerugian"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("claim_currency"));
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameST("prod_id"));
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            if (getStPolicyTypeID() != null) {
                if (getStPolicyTypeID().equalsIgnoreCase("21")
                        || getStPolicyTypeID().equalsIgnoreCase("59")) {
                    if (h.getFieldValueByFieldNameDT("refd1") != null) {
                        row.createCell(47).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
                    }
                }
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }
    private Date DLADateFrom;
    private Date DLADateTo;
    private Date claimDateFrom;
    private Date claimDateTo;

    public Date getClaimDateFrom() {
        return claimDateFrom;
    }

    public void setClaimDateFrom(Date claimDateFrom) {
        this.claimDateFrom = claimDateFrom;
    }

    public Date getClaimDateTo() {
        return claimDateTo;
    }

    public void setClaimDateTo(Date claimDateTo) {
        this.claimDateTo = claimDateTo;
    }

    public Date getDLADateFrom() {
        return DLADateFrom;
    }

    public void setDLADateFrom(Date DLADateFrom) {
        this.DLADateFrom = DLADateFrom;
    }

    public Date getDLADateTo() {
        return DLADateTo;
    }

    public void setDLADateTo(Date DLADateTo) {
        this.DLADateTo = DLADateTo;
    }

    /**
     * @return the stPolicyTypeCreditID
     */
    public String getStPolicyTypeCreditID() {
        return stPolicyTypeCreditID;
    }

    /**
     * @param stPolicyTypeCreditID the stPolicyTypeCreditID to set
     */
    public void setStPolicyTypeCreditID(String stPolicyTypeCreditID) {
        this.stPolicyTypeCreditID = stPolicyTypeCreditID;
    }

    /**
     * @return the stPolicyTypeCreditDesc
     */
    public String getStPolicyTypeCreditDesc() {
        return stPolicyTypeCreditDesc;
    }

    /**
     * @param stPolicyTypeCreditDesc the stPolicyTypeCreditDesc to set
     */
    public void setStPolicyTypeCreditDesc(String stPolicyTypeCreditDesc) {
        this.stPolicyTypeCreditDesc = stPolicyTypeCreditDesc;
    }

    public DTOList EXCEL_RISK_PROFILE_CREDIT() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

//        String getDebitur = "select a.pol_id,count(a.ins_pol_obj_id) as debitur from ( "
//                + "select a.pol_id,b.ins_pol_obj_id,"
//                + "(getperiod(a.pol_type_id in (21,59,73,74,80),b.refd2,b.refd4)) as refd2,"
//                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
//                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,"
//                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
//                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj, "
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as nd_comm1,"
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
//                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
//                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as nd_ppn,"
//                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko, "
//                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko, "
//                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko, "
//                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko, "
//                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko, "
//                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
//                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko "
//                + "from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id "
//                + "where a.active_flag = 'Y' and a.effective_flag = 'Y' "
//                + "and a.status IN ('POLICY','RENEWAL','ENDORSE') and a.pol_type_id in (21,59,60,73,74,80) ";
//
//        if (getPolicyDateFrom() != null) {
//            getDebitur = getDebitur + " and date_trunc('day',a.policy_date) >= '" + getPolicyDateFrom() + "'";
//        }
//        if (getPolicyDateTo() != null) {
//            getDebitur = getDebitur + " and date_trunc('day',a.policy_date) <= '" + getPolicyDateTo() + "'";
//        }
//
//        getDebitur = getDebitur + " group by a.pol_id,b.ins_pol_obj_id,b.refd2,b.refd4 "
//                + "order by a.pol_no,b.ins_pol_obj_id ) a where (a.tsi_obj <> 0 or a.tsiko <> 0 or a.premi_obj <> 0 or a.premiko <> 0 "
//                + "or a.diskonko <> 0 or commko <> 0 or bfeeko <> 0 or hfeeko <> 0 or nd_disc1 <> 0 or nd_comm1 <> 0 or nd_brok1 <> 0 or nd_hfee <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0) ";
//
//        if (getPeriodFrom() != null) {
//            getDebitur = getDebitur + " and date_trunc('day',a.refd2) >= '" + getPeriodFrom() + "'";
//        }
//
//        if (getPeriodTo() != null) {
//            getDebitur = getDebitur + " and date_trunc('day',a.refd2) <= '" + getPeriodTo() + "'";
//        }
//
//        getDebitur = getDebitur + " group by a.pol_id order by a.pol_id ";

        sqa.addSelect(" a.period_start,a.period_end,a.policy_date,a.status,quote_ident(pol_no) as pol_no,a.active_flag, "
                + "f.ref2 as grupmarketer,f.ent_name as marketer,a.entity_id,e.ent_name,a.cust_name as cust_name,"
                + "a.ccy_rate,a.pol_id,b.ins_pol_obj_id,count(b.ins_pol_obj_id) as jumlah,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "b.description as description,(getname(a.pol_type_id in (21,59,80),b.ref2,'0'))::numeric as usia,"
                + "(getperiod(a.pol_type_id in (21,59,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (21,59,73,74,80),b.refd2,b.refd4)) as refd2,"
                + "(getperiod(a.pol_type_id in (21,59,73,74,80),b.refd3,b.refd5)) as refd3,"
                + "a.ccy, NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2, NULLIF(a.premi_total*ccy_rate,0) as premi_total,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,"
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,"
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as nd_comm1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as nd_ppn,"
                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko, "
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko, "
                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko, "
                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko, "
                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                //                + "inner join ( " + getDebitur + " ) c on c.pol_id = a.pol_id "
                + "inner join ent_master e on e.ent_id = a.entity_id "
                + "inner join ent_master f on f.ent_id = a.prod_id ");

        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");
        sqa.addClause(" a.status IN ('POLICY','RENEWAL','ENDORSE')");
//        sqa.addClause(" a.pol_no in ('010122020519025101','010320200420000202','342121211111244618','345921210719096202','010315151119000401','015912120918086504','040522020720005400','010114140720004100') ");

//        if (stStatus != null) {
//            sqa.addClause("a.status = '" + stStatus + "'");
//            //sqa.addPar(policyDateTo);
//        }
//
        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeCreditID != null) {
            sqa.addClause("a.pol_type_id in (" + stPolicyTypeCreditID + ")");
            //sqa.addPar(stPolicyTypeID);
        } else {
            sqa.addClause("a.pol_type_id in (21,59,60,73,74,80)");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        String sql = "select a.ref4, a.ref5,coalesce(sum(b.jumlah),0) as debitur, "
                + "coalesce(sum(b.tsi_obj),0) as tsi_obj, coalesce(sum(b.premi_obj-b.premiko_obj),0) as premi_obj,"
                + "coalesce(sum(b.disc_obj-b.diskonko_obj),0) as disc_obj, coalesce(sum(b.fbase_obj),0) as fbase_obj, "
                + "coalesce(sum(b.komisi_obj+b.bfee_obj+b.hfee_obj+b.ppn_obj),0) as outgo_obj, coalesce(sum(b.commko_obj+b.bfeeko_obj+b.hfeeko_obj),0) as outgoko_obj "
                + "from band_level a "
                + "left join ( select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,cust_name,ccy_rate,"
                + "pol_id,ins_pol_obj_id,debitur,jumlah,description,usia,tgl_lahir,refd2,refd3,jwaktu,ccy,active_flag,"
                + "round(tsi_obj,2) as tsi_obj,round(tsiko_obj,2) as tsiko_obj,round(premi_obj,2) as premi_obj,round(premiko_obj,2) as premiko_obj,"
                + "round(diskonko_obj,2) as diskonko_obj,round(commko_obj,2) as commko_obj,round(bfeeko_obj,2) as bfeeko_obj,round(hfeeko_obj,2) as hfeeko_obj,"
                + "round(disc_obj,2) as disc_obj,round(komisi_obj,2) as komisi_obj,round(bfee_obj,2) as bfee_obj,"
                + "round(hfee_obj,2) as hfee_obj,round(fbase_obj,2) as fbase_obj,round(ppn_obj,2) as ppn_obj from ( "
                + "select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,"
                + "cust_name,pol_id,ins_pol_obj_id,debitur,jumlah,description,usia,tgl_lahir,refd2,refd3,"
                + "(EXTRACT(year FROM age(refd3,refd2))*12) as jwaktu,ccy,ccy_rate,active_flag,"
                + "getpremi(status = 'ENDORSE',round(insured_amount/debitur,2),tsi_obj) as tsi_obj,premi_obj,"
                + "round(tsiko/debitur,2) as tsiko_obj, round(premiko/debitur,2) as premiko_obj, round(diskonko/debitur,2) as diskonko_obj,"
                + "round(commko/debitur,2) as commko_obj, round(bfeeko/debitur,2) as bfeeko_obj, round(hfeeko/debitur,2) as hfeeko_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_disc1/debitur,2),round((premi_obj/premi_total)*nd_disc1,2)) as disc_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_comm1/debitur,2),round((premi_obj/premi_total)*nd_comm1,2)) as komisi_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_brok1/debitur,2),round((premi_obj/premi_total)*nd_brok1,2)) as bfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_hfee/debitur,2),round((premi_obj/premi_total)*nd_hfee,2)) as hfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_feebase1/debitur,2) ,round((premi_obj/premi_total)*nd_feebase1,2)) as fbase_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_ppn/debitur,2),round((premi_obj/premi_total)*nd_ppn,2)) as ppn_obj from ( "
                + "select * from ( " + sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,a.pol_id,"
                + "b.ins_pol_obj_id,a.description,b.ref2,b.refd1,b.refd2,b.refd3,a.ccy,a.insured_amount,a.premi_total,a.premi_total_adisc,"
                + "f.ref2,f.ent_name,e.ent_name order by a.pol_no,b.ins_pol_obj_id ) a where a.active_flag = 'Y' ";
//                + "and (a.tsi_obj <> 0 or a.tsiko <> 0 or a.premi_obj <> 0 or a.premiko <> 0 or a.diskonko <> 0 or commko <> 0 or bfeeko <> 0 or hfeeko <> 0 "
//                + "or nd_disc1 <> 0 or nd_comm1 <> 0 or nd_brok1 <> 0 or nd_hfee <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0) ";

        if (periodFrom != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        sql = sql + " order by a.pol_no,a.ins_pol_obj_id ) a ) a where a.active_flag = 'Y' "
                + "and (a.tsi_obj <> 0 or a.tsiko_obj <> 0 or a.premi_obj <> 0 or a.premiko_obj <> 0 or a.diskonko_obj <> 0 "
                + "or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0 or a.disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 "
                + "or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0) ";

        if (stUsiaID != null) {
            if (stUsiaID.equalsIgnoreCase("1")) {
                sql = sql + " and usia <= '55' ";
            } else if (stUsiaID.equalsIgnoreCase("2")) {
                sql = sql + " and usia > '55' ";
            } else {
                sql = sql + " and usia = '0' ";
            }
        }

        sql = sql + " order by a.pol_no,a.ins_pol_obj_id ";

        sql = sql + " ) b on b." + stRiskProfileID + " between a.ref1 and a.ref2 "
                + "where a.ref3 = '" + stRiskProfileID + "' and a.group_desc = 'riskprofilecredit' "
                + "group by a.ref4, a.ref5,a.lvl order by a.lvl ";

//        String nama_file = "risk_profile_credit" + System.currentTimeMillis() + ".csv";
//
//        sql = "Copy ("
//                + sql
//                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";
//
//        SQLUtil S = new SQLUtil();
//
//        final PreparedStatement ps = S.setQuery(sql);
//
//        boolean tes = ps.execute();
//
//        S.release();
//
//        return new DTOList();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RISK_PROFILE_CREDIT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet(stRiskProfileDesc);

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header0
            XSSFRow row00 = sheet.createRow(0);
            row00.createCell(0).setCellValue("RISK PROFILE MASA ASURANSI");

            //bikin header1
            XSSFRow row1 = sheet.createRow(2);
            row1.createCell(0).setCellValue("POLIS : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header2
            XSSFRow row2 = sheet.createRow(3);
            row2.createCell(0).setCellValue("PERIODE : " + DateUtil.getDateStr(getPeriodFrom()) + " sd " + DateUtil.getDateStr(getPeriodTo()));

            //bikin header3
            XSSFRow row3 = sheet.createRow(4);
            row3.createCell(0).setCellValue("POLICY CLASS : " + getStPolicyTypeCreditDesc());

            //bikin header3
            XSSFRow row4 = sheet.createRow(5);
            row4.createCell(0).setCellValue("USIA : " + getStUsiaDesc());

            //bikin header3
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("GRUP SUMBER BISNIS : " + getStCompanyName());
            row5.createCell(4).setCellValue("SUMBER BISNIS : " + getStEntityName());

            //bikin header3
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("GRUP MARKETER : " + getStCompanyProdName());
            row6.createCell(4).setCellValue("MARKETER : " + getStMarketerName());

            //bikin header
            XSSFRow row0 = sheet.createRow(9);
            row0.createCell(0).setCellValue("range 1");
            row0.createCell(1).setCellValue("range 2");
            row0.createCell(2).setCellValue("debitur");
            row0.createCell(3).setCellValue("tsi");
            row0.createCell(4).setCellValue("premi gross");
            row0.createCell(5).setCellValue("feebase");
            row0.createCell(6).setCellValue("outgo");

            BigDecimal prego = BDUtil.sub(h.getFieldValueByFieldNameBD("premi_obj"), h.getFieldValueByFieldNameBD("disc_obj"));
            BigDecimal outgo = BDUtil.sub(h.getFieldValueByFieldNameBD("outgo_obj"), h.getFieldValueByFieldNameBD("outgoko_obj"));

//bikin isi cell
            XSSFRow row = sheet.createRow(i + 10);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("debitur").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
            row.createCell(4).setCellValue(prego.doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("fbase_obj").doubleValue());
            row.createCell(6).setCellValue(outgo.doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    /**
     * @return the stUsiaID
     */
    public String getStUsiaID() {
        return stUsiaID;
    }

    /**
     * @param stUsiaID the stUsiaID to set
     */
    public void setStUsiaID(String stUsiaID) {
        this.stUsiaID = stUsiaID;
    }

    /**
     * @return the stUsiaDesc
     */
    public String getStUsiaDesc() {
        return stUsiaDesc;
    }

    /**
     * @param stUsiaDesc the stUsiaDesc to set
     */
    public void setStUsiaDesc(String stUsiaDesc) {
        this.stUsiaDesc = stUsiaDesc;
    }

    /**
     * @return the stRiskProfileID
     */
    public String getStRiskProfileID() {
        return stRiskProfileID;
    }

    /**
     * @param stRiskProfileID the stRiskProfileID to set
     */
    public void setStRiskProfileID(String stRiskProfileID) {
        this.stRiskProfileID = stRiskProfileID;
    }

    /**
     * @return the stRiskProfileDesc
     */
    public String getStRiskProfileDesc() {
        return stRiskProfileDesc;
    }

    /**
     * @param stRiskProfileDesc the stRiskProfileDesc to set
     */
    public void setStRiskProfileDesc(String stRiskProfileDesc) {
        this.stRiskProfileDesc = stRiskProfileDesc;
    }

    public DTOList EXCEL_RISK_MASA_CREDIT() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.period_start,a.period_end,a.policy_date,a.status,quote_ident(pol_no) as pol_no,a.active_flag, "
                + "f.ref2 as grupmarketer,f.ent_name as marketer,a.entity_id,e.ent_name,a.cust_name as cust_name,"
                + "a.ccy_rate,a.pol_id,b.ins_pol_obj_id,count(b.ins_pol_obj_id) as jumlah,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                //                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id and x.debitur_act is not null) as debitur,"
                + "b.description as description,(getname(a.pol_type_id in (21,59,80),b.ref2,'0'))::numeric as usia,"
                + "(getperiod(a.pol_type_id in (21,59,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (21,59,73,74,80),b.refd2,b.refd4)) as refd2,"
                + "(getperiod(a.pol_type_id in (21,59,73,74,80),b.refd3,b.refd5)) as refd3,"
                + "a.ccy, NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2, NULLIF(a.premi_total*ccy_rate,0) as premi_total,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,"
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,"
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as nd_comm1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as nd_ppn,"
                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko, "
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko, "
                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko, "
                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko, "
                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "inner join ent_master e on e.ent_id = a.entity_id "
                + "inner join ent_master f on f.ent_id = a.prod_id ");

        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");
        sqa.addClause(" a.status IN ('POLICY','RENEWAL','ENDORSE')");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeCreditID != null) {
            sqa.addClause("a.pol_type_id in (" + stPolicyTypeCreditID + ")");
            //sqa.addPar(stPolicyTypeID);
        } else {
            sqa.addClause("a.pol_type_id in (21,59,60,73,74,80)");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        String sql = "select a.ref4, a.ref5,round(sum(checkreas(tahun<='2012',jumlah)),2) as th2012,"
                + "round(sum(checkreas(tahun='2013',jumlah)),2) as th2013,round(sum(checkreas(tahun='2014',jumlah)),2) as th2014,"
                + "round(sum(checkreas(tahun='2015',jumlah)),2) as th2015,round(sum(checkreas(tahun='2016',jumlah)),2) as th2016,"
                + "round(sum(checkreas(tahun='2017',jumlah)),2) as th2017,round(sum(checkreas(tahun='2018',jumlah)),2) as th2018,"
                + "round(sum(checkreas(tahun='2019',jumlah)),2) as th2019,round(sum(checkreas(tahun='2020',jumlah)),2) as th2020,"
                + "round(sum(checkreas(tahun>'2020',jumlah)),2) as th2021 "
                + "from band_level a "
                + "left join ( select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,cust_name,ccy_rate,"
                + "pol_id,ins_pol_obj_id,debitur,jumlah,description,usia,tgl_lahir,refd2,refd3,tahun,jwaktu,ccy,active_flag,"
                + "round(tsi_obj,2) as tsi_obj,round(tsiko_obj,2) as tsiko_obj,round(premi_obj,2) as premi_obj,round(premiko_obj,2) as premiko_obj,"
                + "round(diskonko_obj,2) as diskonko_obj,round(commko_obj,2) as commko_obj,round(bfeeko_obj,2) as bfeeko_obj,round(hfeeko_obj,2) as hfeeko_obj,"
                + "round(disc_obj,2) as disc_obj,round(komisi_obj,2) as komisi_obj,round(bfee_obj,2) as bfee_obj,"
                + "round(hfee_obj,2) as hfee_obj,round(fbase_obj,2) as fbase_obj,round(ppn_obj,2) as ppn_obj from ( "
                + "select period_start,period_end,policy_date,status,pol_no,grupmarketer,marketer,entity_id,ent_name,"
                + "cust_name,pol_id,ins_pol_obj_id,debitur,jumlah,description,usia,tgl_lahir,refd2,refd3,"
                + "EXTRACT(year FROM (refd2)) as tahun,(EXTRACT(year FROM age(refd3,refd2))*12) as jwaktu,ccy,ccy_rate,active_flag,"
                + "getpremi(status = 'ENDORSE',round(insured_amount/debitur,2),tsi_obj) as tsi_obj,premi_obj,"
                + "round(tsiko/debitur,2) as tsiko_obj, round(premiko/debitur,2) as premiko_obj, round(diskonko/debitur,2) as diskonko_obj,"
                + "round(commko/debitur,2) as commko_obj, round(bfeeko/debitur,2) as bfeeko_obj, round(hfeeko/debitur,2) as hfeeko_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_disc1/debitur,2),round((premi_obj/premi_total)*nd_disc1,2)) as disc_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_comm1/debitur,2),round((premi_obj/premi_total)*nd_comm1,2)) as komisi_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_brok1/debitur,2),round((premi_obj/premi_total)*nd_brok1,2)) as bfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_hfee/debitur,2),round((premi_obj/premi_total)*nd_hfee,2)) as hfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_feebase1/debitur,2) ,round((premi_obj/premi_total)*nd_feebase1,2)) as fbase_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_ppn/debitur,2),round((premi_obj/premi_total)*nd_ppn,2)) as ppn_obj from ( "
                + "select * from ( " + sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,a.pol_id,"
                + "b.ins_pol_obj_id,a.description,b.ref2,b.refd1,b.refd2,b.refd3,a.ccy,a.insured_amount,a.premi_total,a.premi_total_adisc,"
                + "f.ref2,f.ent_name,e.ent_name order by a.pol_no,b.ins_pol_obj_id ) a where a.active_flag = 'Y' ";

        if (periodFrom != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        sql = sql + " order by a.pol_no,a.ins_pol_obj_id ) a ) a where a.active_flag = 'Y' "
                + "and (a.tsi_obj <> 0 or a.tsiko_obj <> 0 or a.premi_obj <> 0 or a.premiko_obj <> 0 or a.diskonko_obj <> 0 "
                + "or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0 or a.disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 "
                + "or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0) ";

        if (stUsiaID != null) {
            if (stUsiaID.equalsIgnoreCase("1")) {
                sql = sql + " and usia <= '55' ";
            } else if (stUsiaID.equalsIgnoreCase("2")) {
                sql = sql + " and usia > '55' ";
            } else {
                sql = sql + " and usia = '0' ";
            }
        }

        sql = sql + " order by a.pol_no,a.ins_pol_obj_id ";

        sql = sql + " ) b on b." + stRiskProfileID + " between a.ref1 and a.ref2 "
                + "where a.ref3 = '" + stRiskProfileID + "' and a.group_desc = 'riskprofilecredit' "
                + "group by a.ref4, a.ref5,a.lvl order by a.lvl ";

//        String nama_file = "risk_profile_credit" + System.currentTimeMillis() + ".csv";
//
//        sql = "Copy ("
//                + sql
//                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";
//
//        SQLUtil S = new SQLUtil();
//
//        final PreparedStatement ps = S.setQuery(sql);
//
//        boolean tes = ps.execute();
//
//        S.release();
//
//        return new DTOList();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RISK_MASA_CREDIT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet(stRiskProfileDesc);

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header0
            XSSFRow row00 = sheet.createRow(0);
            row00.createCell(0).setCellValue("RISK PROFILE MASA ASURANSI");

            //bikin header1
            XSSFRow row1 = sheet.createRow(2);
            row1.createCell(0).setCellValue("POLIS : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header3
            XSSFRow row2 = sheet.createRow(3);
            row2.createCell(0).setCellValue("POLICY CLASS : " + getStPolicyTypeCreditDesc());

            //bikin header3
            XSSFRow row3 = sheet.createRow(4);
            row3.createCell(0).setCellValue("USIA : " + getStUsiaDesc());

            //bikin header3
            XSSFRow row4 = sheet.createRow(5);
            row4.createCell(0).setCellValue("GRUP SUMBER BISNIS : " + getStCompanyName());
            row4.createCell(4).setCellValue("SUMBER BISNIS : " + getStEntityName());

            //bikin header3
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("GRUP MARKETER : " + getStCompanyProdName());
            row5.createCell(4).setCellValue("MARKETER : " + getStMarketerName());

            //bikin header
            XSSFRow row0 = sheet.createRow(8);
            row0.createCell(0).setCellValue("range 1");
            row0.createCell(1).setCellValue("range 2");
            row0.createCell(2).setCellValue("<= 2012");
            row0.createCell(3).setCellValue("2013");
            row0.createCell(4).setCellValue("2014");
            row0.createCell(5).setCellValue("2015");
            row0.createCell(6).setCellValue("2016");
            row0.createCell(7).setCellValue("2017");
            row0.createCell(8).setCellValue("2018");
            row0.createCell(9).setCellValue("2019");
            row0.createCell(10).setCellValue("2020");
            row0.createCell(11).setCellValue(">=2021");

//bikin isi cell
            XSSFRow row = sheet.createRow(i + 9);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("th2012").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("th2013").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("th2014").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("th2015").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("th2016").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("th2017").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("th2018").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("th2019").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("th2020").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("th2021").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void deleteFile(String file) {
        String fileName = file;
        // A File object to represent the filename
        File f = new File(fileName);


        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) {
            logger.logInfo("Delete: no such file or directory: " + fileName);
            return;
        }

        //throw new IllegalArgumentException(
        //"Delete: no such file or directory: " + fileName);

        if (!f.canWrite()) {
            throw new IllegalArgumentException("Delete: write protected: "
                    + fileName);
        }

        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) {
                throw new IllegalArgumentException(
                        "Delete: directory not empty: " + fileName);
            }
        }

        // Attempt to delete it
        boolean success = f.delete();

        if (success) {
            logger.logInfo("Delete: deletion success : " + fileName);
        }

        if (!success) {
            logger.logInfo("Delete: deletion failed");
        }
        //throw new IllegalArgumentException("Delete: deletion failed");
    }

    public DTOList EXCEL_RISK_CONTROL_NEW() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        /*riskcontrol new v6*/
        sqa.addSelect(" a.period_start,a.period_end,f.ref2 as grupmarketer,f.ent_name as marketer,e.ref2 as grupsumbis,a.entity_id,e.ent_name,a.cust_name,"
                + "a.status,a.pol_type_id,a.policy_date,a.pol_id,pol_no,substr(a.pol_no,0,17) as sub_polno,"
                + "b.ins_pol_obj_id,b.ins_pol_obj_ref_root_id,b.order_no,(checkstatus(a.pol_type_id in (60),trim(b.ref3d),trim(b.ref1))) as nama,"
                + "REPLACE(trim(b.description),' (VOID)','') as description,(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                + "(getperiod(a.pol_type_id in (4,20,21,59,63,64,71,73,74,80,97),b.refd1,null)) as tgl_lahir,"
                + "(case when a.pol_type_id in (4,21,59,64,73,74,80) then coalesce(b.refd2,a.period_start) "
                + "when a.pol_type_id in (1,3,5,24,81) then coalesce(b.refd1,a.period_start) "
                + "when a.pol_type_id = 60 then coalesce(b.refd4,a.period_start) else a.period_start end ) as refd2,"
                + "(case when a.pol_type_id in (4,21,59,64,73,74,80) then coalesce(b.refd3,a.period_end) "
                + "when a.pol_type_id in (1,3,5,24,81) then coalesce(b.refd2,a.period_end) "
                + "when a.pol_type_id = 60 then coalesce(b.refd5,a.period_end) else a.period_end end ) as refd3,"
                + "a.ccy,a.ccy_rate,getpremi(status='ENDORSE',(a.insured_amount_e*a.ccy_rate),(a.insured_amount*a.ccy_rate)) as tsi_polis,"
                + "coalesce(a.premi_total*ccy_rate,0) as premi_polis,"
                + "(select premi_total from ( select distinct pol_no,NULLIF(y.insured_amount*ccy_rate,0) as premi_total from ins_policy y "
                + "where y.status in ('POLICY','RENEWAL','HISTORY') and substr(y.pol_no,0,17) = substr(a.pol_no,0,17) and y.active_flag = 'Y' and y.effective_flag = 'Y' "
                + ") y where substr(y.pol_no,0,17) = substr(a.pol_no,0,17) and a.status = 'ENDORSE') as tsi_polis_ref,"
                + "(select premi_total from (select distinct pol_no,NULLIF(y.premi_total*ccy_rate,0) as premi_total from ins_policy y "
                + "where y.status in ('POLICY','RENEWAL','HISTORY') and substr(y.pol_no,0,17) = substr(a.pol_no,0,17) and y.active_flag = 'Y' and y.effective_flag = 'Y' "
                + ") y where substr(y.pol_no,0,17) = substr(a.pol_no,0,17) and a.status = 'ENDORSE') as premi_polis_ref,"
                + "(select sum(z.premi_new * a.ccy_rate) from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id where y.ins_pol_obj_id in ("
                + "select ins_pol_obj_ref_root_id from ins_pol_obj y where y.ins_pol_obj_id = b.ins_pol_obj_id) and substr(x.pol_no,0,17) = substr(a.pol_no,0,17) "
                + "and x.status in ('POLICY','RENEWAL','HISTORY') and x.active_flag = 'Y' and x.effective_flag = 'Y' ) as premi_obj_ref_polis,"
                + "(select sum(z.premi_new * a.ccy_rate) from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id where y.ins_pol_obj_id in ("
                + "select ins_pol_obj_ref_root_id from ins_pol_obj y where y.ins_pol_obj_id = b.ins_pol_obj_id) and x.effective_flag = 'Y' ) as premi_obj_ref_sppa,"
                + "round((select sum(x.insured_amount*ccy_rate) from ins_pol_obj x where x.pol_id = a.pol_id),2) as tsi_polis_x,"
                + "coalesce(b.insured_amount*ccy_rate,0) as tsi_obj_end, coalesce(b.premi_total*ccy_rate,0) as premi_obj_end, "
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,  "
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj, "
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as disc_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as komisi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as bfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as hfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as fbase_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as ppn_obj,"
                + "( select coalesce(sum(amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as tsiko_obj, "
                + "( select coalesce(sum(premi_amt * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as premiko_obj, "
                + "( select coalesce(sum(disc_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as diskonko_obj, "
                + "( select coalesce(sum(comm_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as commko_obj, "
                + "( select coalesce(sum(broker_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as bfeeko_obj, "
                + "( select coalesce(sum(hfee_amount * a.ccy_rate),0) from ins_pol_coins z  "
                + "where z.policy_id=a.pol_id and a.cover_type_code in ('COINSOUTSELF','COINSOUT') and z.entity_id <> 1 ) as hfeeko_obj, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z where z.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount * a.ccy_rate)) "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),z.insured_amount * a.ccy_rate))  "
                + "from ins_pol_tsi z  where z.ins_pol_obj_id=b.ins_pol_obj_id) as other,"
                + " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount * a.ccy_rate)),2) as tsi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.premi_amount * a.ccy_rate)),2) as premi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt * a.ccy_rate)),2) as komisi_or, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount * a.ccy_rate)),2) as tsi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount * a.ccy_rate)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt * a.ccy_rate)),2) as komisi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.tsi_amount * a.ccy_rate)),2) as tsi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.premi_amount * a.ccy_rate)),2) as premi_kscbi,  "
                + " round(sum(checkreas(j.treaty_type='KSCBI',i.ricomm_amt * a.ccy_rate)),2) as komisi_kscbi, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount * a.ccy_rate)),2) as tsi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount * a.ccy_rate)),2) as premi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt * a.ccy_rate)),2) as komisi_spl, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount * a.ccy_rate)),2) as tsi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount * a.ccy_rate)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt * a.ccy_rate)),2) as komisi_fac, "
                + " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount * a.ccy_rate)),2) as tsi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount * a.ccy_rate)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt * a.ccy_rate)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount * a.ccy_rate)),2) as tsi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount * a.ccy_rate)),2) as premi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt * a.ccy_rate)),2) as komisi_park, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount * a.ccy_rate)),2) as tsi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount * a.ccy_rate)),2) as premi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount * a.ccy_rate)),2) as tsi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount * a.ccy_rate)),2) as premi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount * a.ccy_rate)),2) as tsi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount * a.ccy_rate)),2) as premi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount * a.ccy_rate)),2) as tsi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount * a.ccy_rate)),2) as premi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt * a.ccy_rate)),2) as komisi_faco3, "
                + " round(sum(checkreas(j.treaty_type='JP',i.tsi_amount * a.ccy_rate)),2) as tsi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.premi_amount * a.ccy_rate)),2) as premi_jp, "
                + " round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt * a.ccy_rate)),2) as komisi_jp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.tsi_amount * a.ccy_rate)),2) as tsi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.premi_amount * a.ccy_rate)),2) as premi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.ricomm_amt * a.ccy_rate)),2) as komisi_facp,"
                + " round(sum(checkreas(j.treaty_type='QSKR',i.tsi_amount * a.ccy_rate)),2) as tsi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.premi_amount * a.ccy_rate)),2) as premi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.ricomm_amt * a.ccy_rate)),2) as komisi_qskr, "
                + " c.vs_description as coverage,b.ref2,a.endorse_notes,m.vs_description as kreasi_type_desc,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.ref9d,null)) as kode_pos,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),d.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),k.ins_risk_cat_code,null)) as ins_risk_cat_code,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,a.cover_type_code,"
                + "coalesce((SELECT SUM(rate) FROM INS_POL_ITEMS y WHERE y.pol_id = a.pol_id "
                + "and y.ins_item_id in (select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct,"
                + "(select string_agg(l.description||' - rate : '||round(getvalid2(z.rate is null,z.rate),3)||' - insured : '||z.insured_amount, '| ') "
                + "from ins_cover l inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1, "
                + "checkstatus(a.pol_type_id in (59,73,74),b.ref4,b.ref7) as no_pk,"
                + "checkstatus(a.pol_type_id in (59,73,74),b.ref12,b.ref20) as no_referensi,"
                + "checkstatus(a.pol_type_id in (59,73,74),b.ref16,checkstatus(a.pol_type_id = 80,b.ref8,b.ref25)) as no_rek_pinj ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = i.ins_treaty_detail_id "
                + "left join s_valueset c on c.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and c.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join s_valueset d on d.vs_code = b.ref7 and d.vs_group = 'INSOBJ_CREDIT_STATUS' "
                + "left join s_valueset l on l.vs_code = b.ref4 and l.vs_group = 'INSOBJ_PEKERJAAN_DEBITUR' "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id  "
                + "left join ins_risk_cat k on k.ins_risk_cat_id = b.ins_risk_cat_id "
                + "left join s_valueset m on m.vs_code = a.kreasi_type_id and m.vs_group = 'INSOBJ_KREASI_KREDIT' ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
//        sqa.addClause(" a.pol_type_id in (21,59,60,73,74,80) ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = '" + stPostCode + "'");
            //sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = '" + stPostCodeDesc2 + "'");
            //sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = '" + stZoneCode + "'");
            //sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = '" + stZoneEquakeName + "'");
            //sqa.addPar(stZoneEquakeName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
            //sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = '" + stYearTreaty + "'");
            //sqa.addPar(stYearTreaty);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("coalesce(a.effective_flag,'') <> 'Y'");
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("f.ref2 = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        if (stCreditID != null) {
            sqa.addClause("a.kreasi_type_id = '" + stCreditID + "'");
//            sqa.addPar(stCreditID);
        }

        String sql = "select period_start,period_end,policy_date,status,quote_ident(pol_no) as pol_no,grupmarketer,marketer,entity_id,ent_name,cust_name,ccy_rate,"
                + "ins_pol_obj_id,nama,coverage,kreasi_type_desc,ref2,kode_pos,kriteria,risk_class,tgl_lahir,refd2,refd3,ins_risk_cat_code,"
                + "ccy,tsi_obj,tsiko_obj,premi_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,"
                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,"
                + "tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,"
                + "tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,cover_type_code,total_komisi_pct,desc1,endorse_notes from ( "
                + "select period_start,period_end,grupmarketer,marketer,entity_id,ent_name,cust_name,status_deb,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,ins_pol_obj_id,ins_pol_obj_ref_root_id,"
                + "order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,tsi_polis,tsi_polis_x,premi_polis,premi_polis_ref,premi_obj_ref,tsi_obj,premi_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*tsi_polis,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*tsi_polis,2) end,0) as tsi_obj_penys,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*tsi_polis,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*premi_polis,2) end,0) as premi_obj_penys,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*disc_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*disc_obj,2) end,0) as disc_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*komisi_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*komisi_obj,2) end,0) as komisi_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*bfee_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*bfee_obj,2) end,0) as bfee_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*hfee_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*hfee_obj,2) end,0) as hfee_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*fbase_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*fbase_obj,2) end,0) as fbase_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*ppn_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*ppn_obj,2) end,0) as ppn_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*tsiko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*tsiko_obj,2) end,0) as tsiko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*premiko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*premiko_obj,2) end,0) as premiko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*diskonko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*diskonko_obj,2) end,0) as diskonko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*commko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*commko_obj,2) end,0) as commko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*bfeeko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*bfeeko_obj,2) end,0) as bfeeko_obj,"
                + "coalesce(case when status_deb in ('POLICY','ENDOUTGO') then round((premi_obj_ref/premi_polis_ref)*hfeeko_obj,2) "
                + "when status_deb in ('ENDADD','ENDPREMI') then round((premi_obj/premi_polis)*hfeeko_obj,2) end,0) as hfeeko_obj,"
                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,"
                + "tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,"
                + "tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,tsi_qskr,premi_qskr,"
                + "komisi_qskr,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,total_komisi_pct,desc1,no_pk,no_referensi,no_rek_pinj,kreasi_type_desc,endorse_notes from ( "
                + "select period_start,period_end,grupmarketer,marketer,entity_id,ent_name,cust_name,status_deb,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,"
                + "ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,tsi_polis,tsi_polis_x,"
                + "getpremi(status_deb in ('ENDADD','ENDPREMI') and premi_polis = 0,1,premi_polis) as premi_polis,"
                + "getpremi(status_deb in ('POLICY','ENDOUTGO') and premi_polis_ref = 0 and premi_obj_ref = 0,1,premi_polis_ref) as premi_polis_ref,"
                + "getpremi(status_deb in ('POLICY','ENDOUTGO') and premi_polis_ref = 0 and premi_obj_ref = 0,1,premi_obj_ref) as premi_obj_ref,"
                + "tsi_obj,premi_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,"
                + "tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,"
                + "tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,"
                + "tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,coverage,"
                + "ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,total_komisi_pct,desc1,no_pk,no_referensi,no_rek_pinj,kreasi_type_desc,endorse_notes from ( "
                + "select period_start,period_end,grupmarketer,marketer,entity_id,ent_name,cust_name,status_deb,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,"
                + "tgl_lahir,refd2,refd3,ccy,ccy_rate,tsi_polis,tsi_polis_x,premi_polis,premi_polis_ref,getpremi(status_deb='ENDOUTGO' and (premi_obj_ref is null or premi_obj_ref = 0),premi_polis_ref,premi_obj_ref) as premi_obj_ref,"
                + "tsi_obj,premi_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,building,machine,stock,other,"
                + "tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,"
                + "tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,"
                + "tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,tsi_qskr,premi_qskr,komisi_qskr,coverage,"
                + "ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,total_komisi_pct,desc1,no_pk,no_referensi,no_rek_pinj,kreasi_type_desc,endorse_notes from ( "
                + "select case when status IN ('POLICY','RENEWAL','HISTORY') then 'POLICY' "
                + "when status = 'ENDORSE' and premi_polis_ref <> 0 and premi_obj_ref is null and premi_obj <> 0 then 'ENDADD' "
                + "when status = 'ENDORSE' and premi_obj <> 0 then 'ENDPREMI' "
                + "when status = 'ENDORSE' and premi_obj = 0 and (tsi_obj <> 0 or disc_obj <> 0 or komisi_obj <> 0 or bfee_obj <> 0 or hfee_obj <> 0 or fbase_obj <> 0 or ppn_obj <> 0 or tsiko_obj <> 0 or premiko_obj <> 0 or diskonko_obj <> 0 or commko_obj <> 0 or bfeeko_obj <> 0 or hfeeko_obj <> 0) then 'ENDOUTGO' else null end as status_deb,"
                + "period_start,period_end,grupmarketer,marketer,entity_id,ent_name,cust_name,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,ins_pol_obj_id,ins_pol_obj_ref_root_id,order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,"
                + "tsi_polis,tsi_polis_x,premi_polis,premi_polis_ref,premi_obj_ref,tsi_obj,premi_obj,disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,"
                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,"
                + "tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,"
                + "tsi_qskr,premi_qskr,komisi_qskr,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,total_komisi_pct,desc1,no_pk,no_referensi,no_rek_pinj,kreasi_type_desc,endorse_notes from ( "
                + "select period_start,period_end,grupmarketer,marketer,entity_id,ent_name,cust_name,status,pol_type_id,policy_date,pol_id,pol_no,sub_polno,ins_pol_obj_id,getpremi(status='ENDORSE',ins_pol_obj_ref_root_id,ins_pol_obj_id) as ins_pol_obj_ref_root_id,"
                + "order_no,nama,description,debitur,tgl_lahir,refd2,refd3,ccy,ccy_rate,tsi_polis,tsi_polis_ref,premi_polis,tsi_polis_x,getpremi(status='ENDORSE',coalesce(premi_polis_ref,premi_polis),coalesce(premi_polis,0)) as premi_polis_ref,"
                + "getpremi(status='ENDORSE',coalesce(premi_obj_ref_polis,premi_obj_ref_sppa),coalesce(premi_obj,0)) as premi_obj_ref,"
                + "getpremi(status='ENDORSE',coalesce(tsi_obj_end,tsi_obj),coalesce(tsi_obj,0)) as tsi_obj,"
                + "getpremi(status='ENDORSE',coalesce(premi_obj,premi_obj_end),coalesce(premi_obj,0)) as premi_obj,"
                + "disc_obj,komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,tsiko_obj,premiko_obj,diskonko_obj,commko_obj,bfeeko_obj,hfeeko_obj,"
                + "building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,"
                + "tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,"
                + "tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_facp,premi_facp,komisi_facp,tsi_qskr,premi_qskr,"
                + "komisi_qskr,coverage,ref2,kode_pos,kriteria,ins_risk_cat_code,risk_class,cover_type_code,total_komisi_pct,desc1,no_pk,no_referensi,no_rek_pinj,kreasi_type_desc,endorse_notes from ( "
                + sqa.getSQL() + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,"
                + "b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,"
                + "a.share_pct,a.ccy,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total,b.premi_total_bcoins,d.vs_description,k.ins_risk_cat_code,l.vs_description,c.vs_description, "
                + "f.ref2,f.ent_name,e.ref2,e.ent_name,m.vs_description order by a.pol_no,b.ins_pol_obj_id ) a where a.ins_pol_obj_id is not null ";

        if (periodFrom != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sql = sql + " and date_trunc('day',a.refd3) >= '" + periodEndFrom + "'";
            //sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sql = sql + " and date_trunc('day',a.refd3) <= '" + periodEndTo + "'";
            //sqa.addPar(periodEndTo);
        }

        sql = sql + "order by a.pol_no,a.ins_pol_obj_id ) a where (tsi_polis <> 0 or tsi_polis_ref <> 0) "
                + ") a where status_deb is not null order by a.pol_no,a.ins_pol_obj_id ) a order by a.pol_no,a.ins_pol_obj_id "
                + ") a order by a.pol_no,a.ins_pol_obj_id ) a order by a.pol_no,a.ins_pol_obj_id ";

        String nama_file = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "statistik"; //"akuntansi"; //"statistik";
        String pass = "St@t1sT!k234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\statistik\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();

    }

    public DTOList EXCEL_RISK_CONTROL_V2() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        /*riskcontrol new v6*/
        sqa.addSelect(" a.period_start,a.period_end,a.policy_date,a.status,';'||a.pol_no as pol_no,a.grupmarketer,a.marketer,a.sumbis,a.cust_name,"
                + "a.ccy_rate,b.ins_pol_obj_id,b.nama,b.coverage,a.kreasi_type_desc,b.ref2,b.kode_pos,b.kriteria,b.risk_class,b.tgl_lahir,b.refd2,b.refd3,"
                + "b.ins_risk_cat_code,b.tsi_obj_rev as tsi_obj,b.tsiko_obj,b.premi_obj_rev as premi_obj,b.premiko_obj,b.diskonko_obj,b.commko_obj,b.bfeeko_obj,b.hfeeko_obj,b.disc_obj,"
                + "komisi_obj,bfee_obj,hfee_obj,fbase_obj,ppn_obj,building,machine,stock,other,tsi_or,premi_or,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,"
                + "tsi_kscbi,premi_kscbi,komisi_kscbi,tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,komisi_qs,"
                + "tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,"
                + "tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,b.no_pk,b.desc1,a.endorse_notes, "
                + "a.cc_code,a.cc_code_source,a.region_id_source as region_source,"
                + "(case when a.pol_type_id in (87,88) then c.refn11 end) as premi100,"
                + "(case when a.pol_type_id in (87,88) then c.refd7 end) as tglawal_pk,"
                + "(case when a.pol_type_id in (87,88) then c.refd8 end) as tglakhir_pk ");

        sqa.addQuery(" from data_debitur b "
                + "inner join data_polis a on a.pol_id = b.pol_id "
                + "inner join prod_obj_detil c on c.ins_pol_obj_id = b.ins_pol_obj_id ");

        sqa.addClause(" b.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" b.delete_f is null ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("a.cc_code = '" + stBranch + "'");
//                sqa.addPar(stBranch);

                if (getStBranchSource() != null) {
                    sqa.addClause("a.cc_code_source = '" + stBranchSource + "'");
//                    sqa.addPar(stBranchSource);
                }
                if (getStRegionSource() != null) {
                    sqa.addClause("a.region_id_source = '" + stRegionSource + "'");
//                    sqa.addPar(stRegionSource);
                }
            } else {
                sqa.addClause("((a.cc_code = '" + stBranch + "') or (a.cc_code = '80' and a.cc_code_source = '" + stBranch + "'))");
//                sqa.addPar(stBranch);
//                sqa.addPar(stBranch);
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id ='" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                sqa.addClause("a.cc_code = '80'");
//            sqa.addPar(stBussinessPolType);
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                sqa.addClause("a.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stEntityID != null) {
            sqa.addClause("a.idsumbis = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.grups = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.idmarketer = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.grupm = '" + stCompanyProdID + "'");
            //sqa.addPar(stCompanyProdID);
        }

        String sql = sqa.getSQL() + " order by a.pol_no,b.ins_pol_obj_id ";

        String nama_file = "risk_control_statistik_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/statistik/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil("WHOUSEDS");

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "statistik"; //"akuntansi"; //"statistik";
        String pass = "St@t1sT!k234"; //"Akunt@n$1234"; //"St@t1sT!k234";
//        String file = "P:\\statistik\\" + nama_file;//utk core local
        String file = "Q:\\statistik\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();

    }

    /**
     * @return the stBussinessPolType
     */
    public String getStBussinessPolType() {
        return stBussinessPolType;
    }

    /**
     * @param stBussinessPolType the stBussinessPolType to set
     */
    public void setStBussinessPolType(String stBussinessPolType) {
        this.stBussinessPolType = stBussinessPolType;
    }

    /**
     * @return the stBussinessPolTypeCob
     */
    public String getStBussinessPolTypeCob() {
        return stBussinessPolTypeCob;
    }

    /**
     * @param stBussinessPolTypeCob the stBussinessPolTypeCob to set
     */
    public void setStBussinessPolTypeCob(String stBussinessPolTypeCob) {
        this.stBussinessPolTypeCob = stBussinessPolTypeCob;
    }

    /**
     * @return the stBranchSource
     */
    public String getStBranchSource() {
        return stBranchSource;
    }

    /**
     * @param stBranchSource the stBranchSource to set
     */
    public void setStBranchSource(String stBranchSource) {
        this.stBranchSource = stBranchSource;
    }

    /**
     * @return the stBranchSourceDesc
     */
    public String getStBranchSourceDesc() {
        return stBranchSourceDesc;
    }

    /**
     * @param stBranchSourceDesc the stBranchSourceDesc to set
     */
    public void setStBranchSourceDesc(String stBranchSourceDesc) {
        this.stBranchSourceDesc = stBranchSourceDesc;
    }

    /**
     * @return the stRegionSource
     */
    public String getStRegionSource() {
        return stRegionSource;
    }

    /**
     * @param stRegionSource the stRegionSource to set
     */
    public void setStRegionSource(String stRegionSource) {
        this.stRegionSource = stRegionSource;
    }

    /**
     * @return the stRegionSourceDesc
     */
    public String getStRegionSourceDesc() {
        return stRegionSourceDesc;
    }

    /**
     * @param stRegionSourceDesc the stRegionSourceDesc to set
     */
    public void setStRegionSourceDesc(String stRegionSourceDesc) {
        this.stRegionSourceDesc = stRegionSourceDesc;
    }
}

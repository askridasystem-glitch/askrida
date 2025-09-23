/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionReinsuranceReportForm
 * Author:  Denny Mahendra
 * Created: Aug 15, 2006 11:17:20 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.util.DTOList;
import com.crux.util.JSPUtil;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsuranceTreatyTypesView;
import com.webfin.insurance.ejb.PostProcessorManager;
import com.webfin.FinCodec;
import com.crux.lang.LanguageManager;
import com.crux.util.BDUtil;
import com.crux.util.DateUtil;
import com.crux.util.LogManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletOutputStream;

import com.crux.util.SQLUtil;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.insurance.model.InsurancePolicyTypeGroupView;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import java.sql.ResultSet;
import javax.ejb.SessionContext;
import org.joda.time.DateTime;

public class ProductionReinsuranceReportForm extends Form {

    public SessionContext ctx;
    private Date periodFrom;
    private Date expirePeriodFrom;
    private Date expirePeriodTo;
    private Date periodTo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date entryDateFrom;
    private Date entryDateTo;
    private Date periodEndFrom;
    private Date periodEndTo;
    private Date ReinsDateFrom;
    private Date ReinsDateTo;
    private Date journalDateFrom;
    private Date journalDateTo;
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
    private String stObject;
    private String stParentID;
    private String stCustStatus;
    private String stStatus;
    private String stYearTreaty;
    private String stTime;
    private String stCompTypeID;
    private String stCompTypeName;
    private String stReinsID;
    private String stReinsName;
    private String stIndex;
    private String stGroupID;
    private String stGroupName;
    private String stRISlip;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stTriwulan;
    private String stPolCredit;
    private String stNonCreditID;
    private String stNonCreditName;
    private String stCompanyProdID;
    private String stCompanyProdName;
    private String stCurrency;
    private String stTreatyYearID;
    private String stTreatyYearDesc;
    private String stPolTypeGrpSoaID;
    private String stPolTypeGrpSoaDesc;
    private String stTreatyYearGrpID;
    private String stTreatyYearGrpDesc;
    private String stPolJenas;
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;
    private String stBranchSource;
    private String stBranchSourceDesc;
    private String stRegionSource;
    private String stRegionSourceDesc;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionReinsuranceReportForm.class);

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

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stPolicyTypeID + "_" + stRISlip);

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stPolicyTypeID);

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stRISlip);

        plist.add(stReport + "_" + stPolicyTypeGroupID);

        plist.add(stReport + "_" + stRISlip);

        plist.add(stReport + "_" + stNonCreditID);

        plist.add(stReport + "_" + stFltTreatyType);

        plist.add(stReport + "_" + stIndex);

        plist.add(stReport);

        String urx = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/insurance/prodrpt/" + s + ".fop?xlang=" + stLang;
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
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

    public DTOList BORDERO() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	getbordero5(j.treaty_type='OR',j.treaty_type='FACO',a.pol_id) as pol_id, "
                + " getbordero2(j.treaty_type='OR',j.treaty_type='FACO',a.pol_no) as pol_no, "
                + " getbordero2(j.treaty_type='OR',j.treaty_type='FACO',substr(a.cust_name,1,35)) as cust_name, "
                + " getbordero6(j.treaty_type='OR',j.treaty_type='FACO',a.period_start) as period_start, "
                + " getbordero6(j.treaty_type='OR',j.treaty_type='FACO',a.period_end) as period_end, "
                + " getbordero2(j.treaty_type='OR',j.treaty_type='FACO',a.ccy) as ccy, "
                + " getbordero(j.treaty_type='OR',j.treaty_type='FACO',a.insured_amount*a.ccy_rate) as insured_amount, "
                + " j.treaty_type,"
                + " sum(getbordero4(j.treaty_type='OR',i.tsi_amount*a.ccy_rate)) as tsi_reas, "
                + " sum(getbordero3(j.treaty_type='OR',i.premi_amount*a.ccy_rate)) as premi_or, "
                + " sum(getbordero4(j.treaty_type='OR',i.premi_amount*a.ccy_rate)) as premi_reas,"
                + " a.status");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id=a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stBussinessPolType != null) {
            sqa.addClause("f.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }

        final String sql = sqa.getSQL() + " group by j.treaty_type,a.pol_id,a.pol_no,a.cust_name,k.order , "
                + " a.period_start,a.period_end,a.insured_amount,a.ccy_rate,a.ccy,a.status"
                + " order by a.pol_no,k.order asc";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_BORDERO() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	getbordero5(j.treaty_type='OR',j.treaty_type='FACO',a.pol_id) as pol_id, "
                + " ';'||getbordero2(j.treaty_type='OR',j.treaty_type='FACO',a.pol_no) as pol_no, "
                + " getbordero2(j.treaty_type='OR',j.treaty_type='FACO',substr(a.cust_name,1,35)) as cust_name, "
                + " getbordero6(j.treaty_type='OR',j.treaty_type='FACO',a.period_start) as period_start, "
                + " getbordero6(j.treaty_type='OR',j.treaty_type='FACO',a.period_end) as period_end, "
                + " getbordero2(j.treaty_type='OR',j.treaty_type='FACO',a.ccy) as ccy, "
                + " getbordero(j.treaty_type='OR',j.treaty_type='FACO',a.insured_amount*a.ccy_rate) as insured_amount, "
                + " j.treaty_type,a.status,"
                + " sum(getbordero4(j.treaty_type='OR',i.tsi_amount*a.ccy_rate)) as tsi_reas, "
                + " sum(getbordero3(j.treaty_type='OR',i.premi_amount*a.ccy_rate)) as premi_or, "
                + " sum(getbordero4(j.treaty_type='OR',i.premi_amount*a.ccy_rate)) as premi_reas, "
                + " sum(getbordero4(j.treaty_type='OR',i.ricomm_amt*a.ccy_rate)) as komisi_reas ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id=a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type ");

        sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y'");
        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5')");

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

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        String sql = sqa.getSQL() + " group by j.treaty_type,a.pol_id,a.pol_no,a.cust_name,k.order , "
                + " a.period_start,a.period_end,a.insured_amount,a.ccy_rate,a.ccy,a.status ";

        sql = sql + " order by a.pol_no,k.order asc ";

        SQLUtil S = new SQLUtil();

        String nama_file = "bordero_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

    }

    public void EXPORT_BORDERO() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_id");
            row0.createCell(1).setCellValue("pol_no");
            row0.createCell(2).setCellValue("cust_name");
            row0.createCell(3).setCellValue("period_start");
            row0.createCell(4).setCellValue("period_end");
            row0.createCell(5).setCellValue("ccy");
            row0.createCell(6).setCellValue("insured_amount");
            row0.createCell(7).setCellValue("treaty_type");
            row0.createCell(8).setCellValue("tsi_reas");
            row0.createCell(9).setCellValue("premi_or");
            row0.createCell(10).setCellValue("premi_reas");
            row0.createCell(11).setCellValue("ket.");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            if (h.getFieldValueByFieldNameBD("pol_id") != null) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            if (h.getFieldValueByFieldNameST("cust_name") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            }
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            if (h.getFieldValueByFieldNameST("ccy") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("treaty_type") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            }
            if (h.getFieldValueByFieldNameBD("tsi_reas") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tsi_reas").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_or") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_reas") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_reas").doubleValue());
            }
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("status"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList CONSORTIUM() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4) as period,a.pol_no,a.cust_name,a.ccy_rate, "
                + "getpostcode(a.pol_type_id,b.ref6d,b.ref9d) as print_code,b.ref1,c.ins_risk_cat_code as cover_type_code, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)),0),0) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + " as total_due, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)),0),0) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + " as total_fee, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)),0),0) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + " as premi_rate, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)),0),0) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + " as premi_netto,b.insured_amount as insured_amount, "
                + "(select round(sum(x.rate),4)||' '||a.rate_method_desc "
                + "from ins_policy y "
                + "inner join ins_pol_cover x on x.pol_id = y.pol_id "
                + "where y.pol_id = a.pol_id) as treaty_type, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2),0) as tsi_faco, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2),0) as premi_faco, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2),0) as komisi");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "left join ins_risk_cat c on b.ins_risk_cat_id = c.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause(" a.pol_type_id=2");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + " group by a.policy_date,a.period_start,a.period_end,a.pol_no,a.pol_id,a.cust_name,a.ccy_rate, "
                + "a.pol_type_id,b.ref6d,b.ref9d,b.ref1,c.ins_risk_cat_code,b.ins_pol_obj_id, "
                + "b.insured_amount,a.rate_method_desc "
                + "order by a.pol_no,b.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList NOTAREINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stNonCreditID != null) {
            if (stNonCreditID.equalsIgnoreCase("1")) {
                sqa.addSelect(
                        " a.cover_type_code,j.treaty_type as co_treaty_id,a.comm_amount,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " sum(coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0)) as premi_amt, "
                        + " sum(coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0)) as nd_brok2pct ");
                String query = null;
                query = " from ( select a.cc_code,a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,"
                        + " (a.premi_total*a.ccy_rate) as premi_total,a.status,a.active_flag,a.effective_flag,a.policy_date,"
                        + " a.period_start,a.period_end,a.ccy,a.ccy_rate,a.ins_policy_type_grp_id,a.pol_type_id "
                        + " ,(select sum(z.rate) "
                        + " from ins_items x "
                        + " inner join ins_pol_items z on z.ins_item_id = x.ins_item_id "
                        + " where z.pol_id = a.pol_id and x.item_group in ('BROKR','COMM','FEEBASE','HFEE') group by a.pol_id) as comm_amount "
                        + " from ins_policy a"
                        + " inner join ins_pol_cover d on d.pol_id = a.pol_id"
                        + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
                        + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                        + " and a.pol_no in ( select pol_no from ins_policy a "
                        + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
                if (getPolicyDateFrom() != null) {
                    query = query + " and date_trunc('day',a.policy_date) >= ? ";
                    sqa.addPar(policyDateFrom);
                }
                if (getPolicyDateTo() != null) {
                    query = query + " and date_trunc('day',a.policy_date) <= ? ";
                    sqa.addPar(policyDateTo);
                }
                query = query + " ) group by a.cc_code,a.pol_id,a.pol_no,e.cover_type,a.status,a.active_flag,a.effective_flag,a.policy_date,"
                        + " a.period_start,a.period_end,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,a.ins_policy_type_grp_id,d.ins_cover_id ) a "
                        + " inner join ins_pol_obj c on c.pol_id=a.pol_id "
                        + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                        + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                        + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                        + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                        + " inner join ent_master k on k.ent_id = i.member_ent_id ";
                sqa.addQuery(query);
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sqa.addSelect(
                        " b.cover_type_code,b.ins_policy_group_id,b.norut,b.captive,j.treaty_type as co_treaty_id," //j.treaty_type as co_treaty_id,
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.premi_amount*a.ccy_rate)),0)) as premi_amt, "
                        + " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.ricomm_amt*a.ccy_rate)),0)) as nd_brok2pct ");

                String query = null;
                query = " from ins_policy a"
                        + " inner join (select a.pol_no,captive,a.ins_cover_id,a.rate,a.ins_policy_group_id,a.norut,a.cover_type_code,"
                        + " sum(a.premi_new) as premi_new,sum(a.premi_total) as premi_total,"
                        + " sum(getpremi2(a.premi_new=0,a.rate)) as rate,sum(getpremi2(a.premi_total=0,a.premi_rate)) as premi_rate,"
                        + " sum(getpremi(a.premi_new=0 and a.premi_total=0,getvalid(a.rate is null,a.rate),a.premi_new)) as premi_new2,"
                        + " sum(getpremi(a.premi_new=0 and a.premi_total=0,a.premi_rate,a.premi_total)) as premi_total2 "
                        + " from ( select a.pol_no,getcaptive2(substr(a.pol_no,2,1),d.ins_cover_id,a.pol_type_id) as captive,d.ins_cover_id,"
                        + " e.ins_policy_group_id,e.norut,e.cover_type as cover_type_code,d.rate,a.premi_rate,"
                        + " coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total"
                        + " from ins_policy a"
                        + " inner join ins_pol_obj b on b.pol_id = a.pol_id"
                        + " inner join ins_pol_cover d on d.ins_pol_obj_id = b.ins_pol_obj_id"
                        + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id"
                        + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                        + " and a.pol_no in ( select pol_no from ins_policy a "
                        + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
                if (getPolicyDateFrom() != null) {
                    query = query + " and date_trunc('day',a.policy_date) >= ? ";
                    sqa.addPar(policyDateFrom);
                }
                if (getPolicyDateTo() != null) {
                    query = query + " and date_trunc('day',a.policy_date) <= ? ";
                    sqa.addPar(policyDateTo);
                }
                query = query + " ) group by a.pol_no,d.ins_cover_id,d.rate,a.pol_type_id,a.premi_total,a.ccy_rate,e.ins_policy_group_id,e.norut,e.cover_type,a.premi_rate ORDER BY A.POL_NO"
                        + " ) a group by a.pol_no,captive,a.ins_cover_id,a.ins_policy_group_id,a.norut,a.cover_type_code,a.rate,a.premi_rate"
                        + " ORDER BY A.POL_NO ) b on b.pol_no = a.pol_no"
                        + " inner join ins_pol_obj c on c.pol_id = a.pol_id"
                        + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"
                        + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"
                        + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                        + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                        + " inner join ent_master k on k.ent_id = i.member_ent_id ";

                sqa.addQuery(query);
            }
        } else {
            sqa.addSelect(
                    " b.cover_type_code,b.ins_policy_group_id,b.norut,b.captive,j.treaty_type as co_treaty_id," //j.treaty_type as co_treaty_id,
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.premi_amount*a.ccy_rate)),0)) as premi_amt, "
                    + " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.ricomm_amt*a.ccy_rate)),0)) as nd_brok2pct ");

            String query = null;
            query = " from ins_policy a"
                    + " inner join (select a.pol_no,captive,a.ins_cover_id,a.rate,a.ins_policy_group_id,a.norut,a.cover_type_code,"
                    + " sum(a.premi_new) as premi_new,sum(a.premi_total) as premi_total,"
                    + " sum(getpremi2(a.premi_new=0,a.rate)) as rate,sum(getpremi2(a.premi_total=0,a.premi_rate)) as premi_rate,"
                    + " sum(getpremi(a.premi_new=0 and a.premi_total=0,getvalid(a.rate is null,a.rate),a.premi_new)) as premi_new2,"
                    + " sum(getpremi(a.premi_new=0 and a.premi_total=0,a.premi_rate,a.premi_total)) as premi_total2 "
                    + " from ( select a.pol_no,getcaptive2(substr(a.pol_no,2,1),d.ins_cover_id,a.pol_type_id) as captive,d.ins_cover_id,"
                    + " e.ins_policy_group_id,e.norut,e.cover_type as cover_type_code,d.rate,a.premi_rate,"
                    + " coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total"
                    + " from ins_policy a"
                    + " inner join ins_pol_obj b on b.pol_id = a.pol_id"
                    + " inner join ins_pol_cover d on d.ins_pol_obj_id = b.ins_pol_obj_id"
                    + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id"
                    + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                    + " and a.pol_no in ( select pol_no from ins_policy a "
                    + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                    + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
            if (getPolicyDateFrom() != null) {
                query = query + " and date_trunc('day',a.policy_date) >= ? ";
                sqa.addPar(policyDateFrom);
            }
            if (getPolicyDateTo() != null) {
                query = query + " and date_trunc('day',a.policy_date) <= ? ";
                sqa.addPar(policyDateTo);
            }
            query = query + " ) group by a.pol_no,d.ins_cover_id,d.rate,a.pol_type_id,a.premi_total,a.ccy_rate,e.ins_policy_group_id,e.norut,e.cover_type,a.premi_rate ORDER BY A.POL_NO"
                    + " ) a group by a.pol_no,captive,a.ins_cover_id,a.ins_policy_group_id,a.norut,a.cover_type_code,a.rate,a.premi_rate"
                    + " ORDER BY A.POL_NO ) b on b.pol_no = a.pol_no"
                    + " inner join ins_pol_obj c on c.pol_id = a.pol_id"
                    + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"
                    + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"
                    + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                    + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                    + " inner join ent_master k on k.ent_id = i.member_ent_id ";

            sqa.addQuery(query);
        }

        sqa.addClause("a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause("a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("k.ent_id = ?");
            sqa.addPar(stEntityID);
        }
        /*
        if(stPolicyTypeID!=null){
        sqa.addClause("a.pol_type_id = ?");
        sqa.addPar(stPolicyTypeID);
        }

        if(stPolicyTypeGroupID!=null){
        sqa.addClause("a.ins_policy_type_grp_id = ?");
        sqa.addPar(stPolicyTypeGroupID);
        }
         */

        if (stNonCreditID != null) {
            if (stNonCreditID.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id in (21,59)");
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id not in (21,59)");
            }
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        String sql = null;

        if (stNonCreditID != null) {
            if (stNonCreditID.equalsIgnoreCase("1")) {
                sql = " select b.lvl as parent_id,b.ref4,b.ref5,a.cover_type_code,co_treaty_id,"
                        + "sum(a.premi_amt) as premi_amt,sum(a.nd_brok2pct) as nd_brok2pct,nd_brok1pct from band_level b left join ( "
                        + "select a.cover_type_code,co_treaty_id,a.comm_amount,"
                        + "a.premi_amt,a.nd_brok2pct,round(((nd_brok2pct/premi_amt)*100),2) as nd_brok1pct from ( "
                        + sqa.getSQL() + " group by a.cover_type_code,a.comm_amount,j.treaty_type,a.pol_type_id,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end ) "
                        + "a where (a.premi_amt <> 0 or a.nd_brok2pct <> 0) ";
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sql = " select a.co_treaty_id,a.cover_type_code,round(sum(a.premi_amt),0) as premi_amt,"
                        + "round(sum(a.nd_brok2pct),0) as nd_brok2pct from ( select a.cover_type_code,a.ins_policy_group_id,a.norut,a.captive,co_treaty_id,"
                        + "a.premi_amt,a.nd_brok2pct from ( "
                        + sqa.getSQL() + " group by b.cover_type_code,b.captive,b.ins_policy_group_id,b.norut,j.treaty_type,a.pol_type_id,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                        + " ) a where (a.premi_amt <> 0 or a.nd_brok2pct <> 0) ";
            }
        } else {
            sql = " select a.co_treaty_id,a.cover_type_code,round(sum(a.premi_amt),0) as premi_amt,"
                    + "round(sum(a.nd_brok2pct),0) as nd_brok2pct from ( select a.cover_type_code,a.ins_policy_group_id,a.norut,a.captive,co_treaty_id,"
                    + "a.premi_amt,a.nd_brok2pct from ( "
                    + sqa.getSQL() + " group by b.cover_type_code,b.captive,b.ins_policy_group_id,b.norut,j.treaty_type,a.pol_type_id,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                    + " ) a where (a.premi_amt <> 0 or a.nd_brok2pct <> 0) ";
        }


        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        if (stNonCreditID != null) {
            if (stNonCreditID.equalsIgnoreCase("1")) {
                sql = sql + " ) as a on a.comm_amount between b.ref1 and b.ref2 "
                        + "where b.group_desc = 'NOTAREINS' group by b.lvl,b.ref4,b.ref5,a.cover_type_code,co_treaty_id,nd_brok1pct "
                        + "order by a.cover_type_code,b.lvl ";
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sql = sql + " ) a group by a.co_treaty_id,a.cover_type_code "
                        + "order by a.cover_type_code,a.co_treaty_id ";
            }
        } else {
            sql = sql + " ) a group by a.co_treaty_id,a.cover_type_code "
                    + "order by a.cover_type_code,a.co_treaty_id ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList REAS() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.policy_date,a.pol_type_id,a.period_start,a.period_end,a.pol_id,a.pol_no,substr(a.cust_name,1,25) as cust_name,a.ccy,a.insured_amount,a.premi_total, "
                + " (round(sum(i.premi_rate),4)||' '||a.rate_method_desc) as rate_method_desc,(round(a.share_pct,2)||' %') as rate_method,"
                + " 	(	select coalesce(sum(y.amount),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + " 	where y.policy_id=a.pol_id) as tsi_askrida, "
                + " 	(	select coalesce(sum(y.premi_amt),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + " 	where y.policy_id=a.pol_id) as premi_askrida, "
                + " 	(	select coalesce(sum(y.amount),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id<>1 "
                + " 	where y.policy_id=a.pol_id) as tsi_ko, "
                + " 	(	select coalesce(sum(y.premi_amt),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id<>1 "
                + " 	where y.policy_id=a.pol_id) as premi_ko, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)) as tsi_bpdan, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)) as premi_bpdan, "
                + " sum(checkreas(j.treaty_type='OR',i.tsi_amount)) as tsi_or, "
                + " sum(checkreas(j.treaty_type='OR',i.premi_amount)) as premi_or, "
                + " sum(checkreas(j.treaty_type='QS',i.tsi_amount)) as tsi_qs, "
                + " sum(checkreas(j.treaty_type='QS',i.premi_amount)) as premi_qs, "
                + " sum(checkreas(j.treaty_type='SPL',i.tsi_amount)) as tsi_spl, "
                + " sum(checkreas(j.treaty_type='SPL',i.premi_amount)) as premi_spl, "
                + " sum(checkreas(j.treaty_type='FAC',i.tsi_amount)) as tsi_fac, "
                + " sum(checkreas(j.treaty_type='FAC',i.premi_amount)) as premi_fac, "
                + " sum(checkreas(j.treaty_type='PARK',i.tsi_amount)) as tsi_park, "
                + " sum(checkreas(j.treaty_type='PARK',i.premi_amount)) as premi_park, "
                + " sum(checkreas(j.treaty_type='FACO',i.tsi_amount)) as tsi_faco, "
                + " sum(checkreas(j.treaty_type='FACO',i.premi_amount)) as premi_faco, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)) as comm_bpdan, "
                + " sum(checkreas(j.treaty_type='OR',i.ricomm_amt)) as comm_or, "
                + " sum(checkreas(j.treaty_type='QS',i.ricomm_amt)) as comm_qs, "
                + " sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)) as comm_spl, "
                + " sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)) as comm_fac, "
                + " sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)) as comm_park, "
                + " sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)) as comm_faco, "
                + " sum(checkreas(j.treaty_type='XOL1',i.tsi_amount)) as tsi_xol1, "
                + " sum(checkreas(j.treaty_type='XOL2',i.tsi_amount)) as tsi_xol2, "
                + " sum(checkreas(j.treaty_type='XOL3',i.tsi_amount)) as tsi_xol3, "
                + " sum(checkreas(j.treaty_type='XOL4',i.tsi_amount)) as tsi_xol4, "
                + " sum(checkreas(j.treaty_type='XOL5',i.tsi_amount)) as tsi_xol5, "
                + " sum(checkreas(j.treaty_type='XOL1',i.premi_amount)) as premi_xol1, "
                + " sum(checkreas(j.treaty_type='XOL2',i.premi_amount)) as premi_xol2, "
                + " sum(checkreas(j.treaty_type='XOL3',i.premi_amount)) as premi_xol3, "
                + " sum(checkreas(j.treaty_type='XOL4',i.premi_amount)) as premi_xol4, "
                + " sum(checkreas(j.treaty_type='XOL5',i.premi_amount)) as premi_xol5");

        sqa.addQuery("from "
                + " ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id=a.pol_id "
                + " inner join ins_policy_types f on f.pol_type_id =a.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        final String sql = sqa.getSQL() + " group by a.policy_date,a.pol_type_id,a.period_start,a.period_end,a.pol_id,a.pol_no,"
                + "a.cust_name,a.ccy,a.ccy_rate,a.insured_amount,a.premi_total,a.share_pct,a.rate_method_desc "
                + " order by a.pol_no asc";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList SUMM_REAS_REINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC")
                    || stFltTreatyType.equalsIgnoreCase("FACO")
                    || stFltTreatyType.equalsIgnoreCase("JP")
                    || stFltTreatyType.equalsIgnoreCase("FACO3")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
                        } else {
                            sqa.addSelect(" a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
                        }
                    } else {
                        sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
                    }
                } else {
                    sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                            + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                            + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
                }

            } else {
                sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                        + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
            }
        } else {
            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " k.short_name as cust_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2");
        }

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");
        sqa.addClause(" a.effective_flag='Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stCoinsurerID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("f.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }

        String sql;

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC")
                    || stFltTreatyType.equalsIgnoreCase("FACO")
                    || stFltTreatyType.equalsIgnoreCase("JP")
                    || stFltTreatyType.equalsIgnoreCase("FACO3")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                                    + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type";
                        } else {
                            sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                                    + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,a.pol_no";
                        }
                    } else {
                        sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                                + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,a.pol_no";
                    }
                } else {
                    sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                            + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,a.pol_no";
                }
            } else {
                sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                        + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type";
            }
        } else {
            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                    + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type";
        }

        sql = " select * from ( " + sql + " ) a ";

        if (getPeriodFrom() != null) {
            sql = sql + " where date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        if (getStFltTreatyType() != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC")
                    || stFltTreatyType.equalsIgnoreCase("FACO")
                    || stFltTreatyType.equalsIgnoreCase("JP")
                    || stFltTreatyType.equalsIgnoreCase("FACO3")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2 from ( "
                                    + sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
                        } else {
                            sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                        }
                    } else {
                        sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                    }
                } else {
                    sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                }
            } else {
                sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                        + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2 from ( "
                        + sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                        + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
            }
        } else {
            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2 from ( "
                    + sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
        }

        sql = sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList SUMM_REAS_POLTYPE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
                        } else {
                            sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
                        }
                    } else {
                        sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
                    }
                } else {
                    sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                            + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                            + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
                }
            } else {
                sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                        + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
            }
        } else {
            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id ");
        }

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id "
                + " inner join ins_treaty l on l.ins_treaty_id = g.ins_treaty_id "
                + " left join ent_master m on m.ent_id = i.reins_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");
        sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stCoinsurerID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("f.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }
        /*
        if (stPolicyTypeGroupID != null) {
        sqa.addClause("f.ins_policy_type_grp_id = ?");
        sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
        sqa.addClause("a.pol_type_id = ?");
        sqa.addPar(stPolicyTypeID);
        }*/

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }
        /*
        if (stFltTreatyType != null) {
        if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
        sqa.addClause("j.treaty_type in ('SPL','QS')");
        } else {
        sqa.addClause("j.treaty_type = ?");
        sqa.addPar(stFltTreatyType);
        }
        }*/

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
                if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(stFltTreatyType);
                }
            }
        }

        String sql;

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
                        } else {
                            sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                        }
                    } else {
                        sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                                + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                    }
                } else {
                    sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                            + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                }
            } else {
                sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                        + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
            }
        } else {
            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id "
                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
        }

        sql = " select * from ( " + sql + " ) a ";

        if (getPeriodFrom() != null && getStTreatyYearID() != null) {
            throw new RuntimeException("Period Start dan Treaty Year harus diinput salah satu");
        }

        if (getPeriodFrom() != null) {
            sql = sql + " where date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        if (getStTreatyYearID() != null) {
            sql = sql + " where a.ins_treaty_id = ? ";
            sqa.addPar(stTreatyYearID);
        }

        if (getStFltTreatyType() != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,"
                                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id from ( "
                                    + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id ";
                        } else {
                            sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                        }
                    } else {
                        sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                    }
                } else {
                    sql = " select * from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                }
            } else {
                sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,"
                        + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id from ( "
                        + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                        + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id ";
            }
        } else {
            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,"
                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id from ( "
                    + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id ";
        }

        sql = sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList RISK_CONTROL() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_no,a.period_start,a.period_end,a.ccy,a.insured_amount,b.insured_amount as insured_amount_e,b.premi_total as premi_total,"
                + "b.ref9d as kode_pos,getpostcode(a.pol_type_id,b.ref7,coalesce(null,'')) as kode_acc,"
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as total_due, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as total_fee, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_rate, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_netto, "
                + "sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)) as premi_bpdan, "
                + "sum(checkreas(j.treaty_type='PARK',i.premi_amount)) as premi_park, "
                + "sum(checkreas(j.treaty_type='OR',i.premi_amount)) as premi_or, "
                + "sum(checkreas(j.treaty_type='SPL',i.premi_amount)) as premi_spl, "
                + "sum(checkreas(j.treaty_type='FAC',i.premi_amount)) as premi_fac");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join  ins_zone_limit d on d.zone_id = getzone3(a.pol_type_id in (1,81), b.ref7)::bigint "
                + "left join  ins_risk_cat e on e.ins_risk_cat_id = b.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id");

        sqa.addClause("a.active_flag = 'Y'");

        if (stStatus != null) {
            sqa.addClause(" a.status = '" + stStatus + "'");
        } else {
            sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        }

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end) >= ?");
            sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sqa.addClause("date_trunc('day',a.period_end) <= ?");
            sqa.addPar(periodEndTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("b.ref7 = ?");
            sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (stRiskCode != null) {
            sqa.addClause("e.ins_risk_cat_code = ?");
            sqa.addPar(stRiskCode);
        }


        final String sql = sqa.getSQL() + " group by a.pol_no,a.period_start,a.period_end,a.ccy,a.insured_amount,b.insured_amount,b.premi_total,b.ins_pol_obj_id,a.pol_type_id,b.ref7,b.ref9d "
                + "order by a.pol_no,b.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList SUMM_REAS_TREATY() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	j.treaty_type,a.pol_type_id,"
                + " sum(getpremi2(k.ref1<>'88',round(i.premi_amount,2)*a.ccy_rate)) as premi_total,"
                + " sum(getpremi2(k.ref1='88',round(i.premi_amount,2)*a.ccy_rate)) as premi_netto,"
                + " sum(getpremi2(k.ref1<>'88',round(i.ricomm_amt,2)*a.ccy_rate)) as nd_comm1,"
                + " sum(getpremi2(k.ref1='88',round(i.ricomm_amt,2)*a.ccy_rate)) as nd_comm2");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from "
                + " ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stCoinsurerID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        final String sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.ins_policy_type_grp_id "
                + " order by j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList BORDERO_KBG() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (getStPolicyTypeGroupID() != null) {
            if (getStPolicyTypeGroupID().equalsIgnoreCase("15")) {
                sqa.addSelect("	a.pol_no,a.pol_type_id,a.period_start,a.period_end,m.ent_id,m.ent_name,n.ent_name as cust_name,a.cc_code,"
                        + "a.insured_amount,a.premi_total,a.ccy,a.ccy_rate,i.ricomm_rate as share_pct,i.ri_slip_no as ref2,f.short_desc as ref4,i.notes as ref3,"
                        + "coalesce(i.tsi_amount,0) as tsi_amount,coalesce(i.premi_amount,0) as premi_netto,coalesce(i.ricomm_amt,0) as nd_comm1,coalesce((i.premi_amount-i.ricomm_amt),0) as premi_base ");
            } else if (!getStPolicyTypeGroupID().equalsIgnoreCase("15")) {
                sqa.addSelect("	a.pol_no,a.pol_type_id,a.period_start,a.period_end,m.ent_id,m.ent_name,a.cust_name,n.ent_name as prod_name,a.cc_code,"
                        + "c.insured_amount as insured_amount,c.premi_total as premi_total,a.ccy,a.ccy_rate,i.ricomm_rate as share_pct,"
                        + "c.ref1,c.ref2,c.ref3,c.ref4,c.ref5,c.ref6,c.ref7,c.ref8,c.ref9,c.ref10,c.ref3d as claim_person_name,f.short_desc as prod_address,i.notes as description,i.ri_slip_no as cust_address,c.refd1,"
                        + "coalesce(i.tsi_amount,0) as tsi_amount,coalesce(i.premi_amount,0) as premi_netto,coalesce(i.ricomm_amt,0) as nd_comm1,coalesce((i.premi_amount-i.ricomm_amt),0) as premi_base ");
            }
        } else {
            sqa.addSelect(" a.pol_no,a.pol_type_id,a.period_start,a.period_end,m.ent_id,m.ent_name,a.cust_name,n.ent_name as prod_name,a.cc_code,"
                    + "c.insured_amount as insured_amount,c.premi_total as premi_total,a.ccy,a.ccy_rate,i.ricomm_rate as share_pct,"
                    + "c.ref1,c.ref2,c.ref3,c.ref4,c.ref5,c.ref6,c.ref7,c.ref8,c.ref9,c.ref10,c.ref3d as claim_person_name,f.short_desc as prod_address,i.notes as description,i.ri_slip_no as cust_address,c.refd1,"
                    + "coalesce(i.tsi_amount,0) as tsi_amount,coalesce(i.premi_amount,0) as premi_netto,coalesce(i.ricomm_amt,0) as nd_comm1,coalesce((i.premi_amount-i.ricomm_amt),0) as premi_base ");
        }

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id=a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type"
                + " inner join ent_master m on m.ent_id = i.member_ent_id"
                + " inner join ent_master n on n.ent_id = a.entity_id ");

        sqa.addClause("	j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        //sqa.addClause(" i.premi_amount <> 0");

        if (stStatus != null) {
            if (stStatus.equalsIgnoreCase("ENDORSE")) {
                sqa.addClause("a.status = 'ENDORSE'");
            } else {
                sqa.addClause("a.status in ('POLICY','RENEWAL','ENDORSE RI')");
            }
        } else {
            sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stMarketerID != null) {
            sqa.addClause("m.ent_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stCurrency != null) {
            if (stCurrency.equalsIgnoreCase("IDR")) {
                sqa.addClause("a.ccy = 'IDR'");
            } else {
                sqa.addClause("a.ccy <> 'IDR'");
            }
        }

        if (stRekapNo != null) {
            sqa.addClause("i.ri_slip_no = ?");
            sqa.addPar(stRekapNo);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("f.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }

        String sql = null;

        if (getStPolicyTypeGroupID() != null) {
            if (getStPolicyTypeGroupID().equalsIgnoreCase("15")) {
                sql = "select cc_code,pol_no,cust_name,period_start,period_end,sum(insured_amount) as insured_amount,sum(premi_total) as premi_total,ccy,ccy_rate,ref2,ref3,ref4,share_pct,"
                        + " sum(tsi_amount) as tsi_reas,sum(premi_netto) as premi_netto,sum(nd_comm1) as nd_comm1,sum(premi_base) as premi_base "
                        + " from ( " + sqa.getSQL() + " ) a where premi_base <> 0 group by cc_code,pol_no,cust_name,period_start,period_end,ref2,ref3,ref4,"
                        + " ccy,ccy_rate,share_pct order by pol_no ";
            } else if (!getStPolicyTypeGroupID().equalsIgnoreCase("15")) {
                sql = "select cc_code,pol_no,cust_name,prod_name,period_start,period_end,sum(insured_amount) as insured_amount,sum(premi_total) as premi_total,ccy,ccy_rate,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,ref9,ref10,prod_address,cust_address,claim_person_name,share_pct,description,refd1 as change_date,"
                        + " sum(tsi_amount) as tsi_reas,sum(premi_netto) as premi_netto,sum(nd_comm1) as nd_comm1,sum(premi_base) as premi_base "
                        + " from ( " + sqa.getSQL() + " ) a where premi_base <> 0 group by cc_code,pol_no,cust_name,prod_name,period_start,period_end,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,ref9,ref10,description,prod_address,cust_address,claim_person_name,refd1,"
                        + " ccy,ccy_rate,share_pct order by cust_address,pol_no ";
            }
        } else {
            sql = "select cc_code,pol_no,cust_name,prod_name,period_start,period_end,sum(insured_amount) as insured_amount,sum(premi_total) as premi_total,ccy,ccy_rate,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,ref9,ref10,prod_address,cust_address,claim_person_name,share_pct,description,refd1 as change_date,"
                    + " sum(tsi_amount) as tsi_reas,sum(premi_netto) as premi_netto,sum(nd_comm1) as nd_comm1,sum(premi_base) as premi_base "
                    + " from ( " + sqa.getSQL() + " ) a where premi_base <> 0 group by cc_code,pol_no,cust_name,prod_name,period_start,period_end,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,ref9,ref10,description,prod_address,cust_address,claim_person_name,refd1,"
                    + " ccy,ccy_rate,share_pct order by cust_address,pol_no ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_BORDERO_KBG() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,a.cust_name,a.pol_type_id,a.period_start,a.period_end,a.ccy_rate,a.ccy_rate_treaty,"
                + "c.ref1,c.ref2,c.ref3,c.ref4,c.ref5,c.ref6,c.ref7,c.ref8,c.ref9,c.ref10,"
                + "c.insured_amount as insured_amount_obj,c.premi_total as premi_total_obj,"
                + "a.insured_amount,a.premi_total,"
                + "j.treaty_type,k.ent_id,k.ent_name,i.sharepct,"
                + "sum(i.tsi_amount) as tsi_amount,"
                + "sum(i.premi_amount) as premi_netto,"
                + "sum(i.ricomm_amt) as nd_comm1,"
                + "sum(i.premi_amount-i.ricomm_amt) as premi_base");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("	j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stMarketerID != null) {
            sqa.addClause("k.ent_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stCurrency != null) {
            if (stCurrency.equalsIgnoreCase("IDR")) {
                sqa.addClause("a.ccy = 'IDR'");
            } else {
                sqa.addClause("a.ccy <> 'IDR'");
            }
        }

        if (stRekapNo != null) {
            sqa.addClause("i.ri_slip_no = ?");
            sqa.addPar(stRekapNo);
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.cust_name,c.ref1,c.ref2,c.ref3,"
                + "c.ref4,c.ref5,c.ref6,c.ref7,c.ref8,c.ref9,c.ref10,c.ins_pol_obj_id,c.insured_amount,c.premi_total,"
                + "j.treaty_type,i.sharepct,k.ent_id,k.ent_name,a.pol_type_id,a.period_start,"
                + "a.period_end,a.insured_amount,a.premi_total,a.ccy_rate,a.ccy_rate_treaty "
                + " order by a.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BORDERO_KBG() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("cust_name");
            row0.createCell(2).setCellValue("debitur");
            row0.createCell(3).setCellValue("pol_type_id");
            row0.createCell(4).setCellValue("period_start");
            row0.createCell(5).setCellValue("period_end");
            row0.createCell(6).setCellValue("insured_amount");
            row0.createCell(7).setCellValue("premi_total");
            row0.createCell(8).setCellValue("insured_amount_obj");
            row0.createCell(9).setCellValue("premi_total_obj");
            row0.createCell(10).setCellValue("treaty");
            row0.createCell(11).setCellValue("ent_id");
            row0.createCell(12).setCellValue("ent_name");
            row0.createCell(13).setCellValue("kurs");
            row0.createCell(14).setCellValue("kurs ri");
            row0.createCell(15).setCellValue("tsi_ri");
            row0.createCell(16).setCellValue("share");
            row0.createCell(17).setCellValue("premi_ri");
            row0.createCell(18).setCellValue("comm_ri");
            row0.createCell(19).setCellValue("nett_ri");
            row0.createCell(20).setCellValue("ref2");
            row0.createCell(21).setCellValue("ref3");
            row0.createCell(22).setCellValue("ref4");
            row0.createCell(23).setCellValue("ref5");
            row0.createCell(24).setCellValue("ref6");
            row0.createCell(25).setCellValue("ref7");
            row0.createCell(26).setCellValue("ref8");
            row0.createCell(27).setCellValue("ref9");
            row0.createCell(28).setCellValue("ref10");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount_obj") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("insured_amount_obj").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_obj") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_total_obj").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("treaty_type") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            }
            if (h.getFieldValueByFieldNameBD("ent_id") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("ent_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ent_name") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_treaty").doubleValue());
            if (h.getFieldValueByFieldNameBD("tsi_amount") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("sharepct") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("sharepct").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_netto") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("premi_netto").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm1") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_base") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ref2") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("ref2"));
            }
            if (h.getFieldValueByFieldNameST("ref3") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("ref3"));
            }
            if (h.getFieldValueByFieldNameST("ref4") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            }
            if (h.getFieldValueByFieldNameST("ref5") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            }
            if (h.getFieldValueByFieldNameST("ref6") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameST("ref6"));
            }
            if (h.getFieldValueByFieldNameST("ref7") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameST("ref7"));
            }
            if (h.getFieldValueByFieldNameST("ref8") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("ref8"));
            }
            if (h.getFieldValueByFieldNameST("ref9") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameST("ref9"));
            }
            if (h.getFieldValueByFieldNameST("ref10") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("ref10"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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
                "   from "
                + "      ins_policy a");

        if (joinObject || joinObject2) {
            sqa.addQuery(" inner join ins_pol_obj b on b.pol_id=a.pol_id");
        }


        /*
        sqa.addQuery(
        "   left join ent_master c on c.ent_id = a.entity_id, ins_pol_items d"
        );*/
        sqa.addQuery(
                "   left join ent_master c on c.ent_id = a.entity_id ");


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
//   for (int i = 0; i < objectFields.length; i++) {
//      String[] f = objectFields[i];
//
//      if (groupAll)
//   //sqa.addSelect("summ("+f[0]+") as "+f[1]);
//   sqa.addGroup(policyField[0]);
//     // else
//   //sqa.addSelect(f[0]+" as "+f[1]);
//   }
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

    public void updateABAProdukByDate(DTOList data) throws Exception {
        try {

            if (true) {
                throw new RuntimeException("test");
            }

            final SQLUtil S = new SQLUtil();

            final DTOList produk2 = new DTOList();

            produk2.markAllUpdate();

            for (int i = 0; i < data.size(); i++) {
                InsuranceProdukView produk = (InsuranceProdukView) data.get(i);

                if (true) {
                    throw new RuntimeException(produk.getStPolicyID());
                }

                produk2.add(produk);

                produk.markUpdate();

                produk.setStFlag("Y");
            }

            S.store(produk2);
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
        }
    }

    public String getStNoUrut() {
        return stNoUrut;
    }

    public void setStNoUrut(String stNoUrut) {
        this.stNoUrut = stNoUrut;
    }

    /*
    public DTOList SOA() throws Exception {
    final SQLAssembler sqa = new SQLAssembler();
    final SQLAssembler sqa1 = new SQLAssembler();

    sqa.addSelect(
    " e.description as cover_type_code,j.treaty_type as co_treaty_id, a.ccy,"+
    " sum(coalesce((((d.premi_new*a.ccy_rate)/(getvalid(a.premi_total=0,a.premi_total)*a.ccy_rate))*(h.premi_amount*a.ccy_rate)),0)) as premi_amt,"+
    " j.comm_ri_pct as nd_brok1pct,"+
    " sum(coalesce((((d.premi_new*a.ccy_rate)/(getvalid(a.premi_total=0,a.premi_total)*a.ccy_rate))*(h.comm_amt*a.ccy_rate)),0)) as nd_brok2pct,"+
    " sum(coalesce((((d.premi_new*a.ccy_rate)/(getvalid(a.premi_total=0,a.premi_total)*a.ccy_rate))*(h.premi_amount*a.ccy_rate)),0))-sum(coalesce((((d.premi_new*a.ccy_rate)/(getvalid(a.premi_total=0,a.premi_total)*a.ccy_rate))*(h.comm_amt*a.ccy_rate)),0)) as premi_netto, "+
    " i.sharepct as nd_hfeepct"
    );


    sqa.addQuery(
    " from ins_policy a"+
    " inner join ins_pol_obj c on c.pol_id=a.pol_id"+
    " inner join ins_pol_cover d on a.pol_id = d.pol_id"+
    " inner join ins_cover e on d.ins_cover_id = e.ins_cover_id"+
    " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"+
    " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"+
    " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"+
    " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"+
    " inner join ent_master m on m.ent_id = i.member_ent_id ");


    sqa.addClause("a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

    sqa.addClause("a.effective_flag = 'Y'");

    sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5')");

    if(periodFrom!=null) {
    sqa.addClause("date_trunc('day',a.period_start) >= ?");
    sqa.addPar(periodFrom);
    }

    if(periodTo!=null) {
    sqa.addClause("date_trunc('day',a.period_start) <= ?");
    sqa.addPar(periodTo);
    }

    if (policyDateFrom!=null) {
    sqa.addClause("date_trunc('day',a.policy_date) >= ?");
    sqa.addPar(policyDateFrom);
    }

    if (policyDateTo!=null) {
    sqa.addClause("date_trunc('day',a.policy_date) <= ?");
    sqa.addPar(policyDateTo);
    }

    if (stBranch!=null) {
    sqa.addClause("a.cc_code = ?");
    sqa.addPar(stBranch);
    }

    if (stRegion!=null) {
    sqa.addClause("a.region_id = ?");
    sqa.addPar(stRegion);
    }

    if (stEntityID!=null) {
    sqa.addClause("m.ent_id = ?");
    sqa.addPar(stEntityID);
    }

    if(stPolicyTypeID!=null){
    sqa.addClause("a.pol_type_id = ?");
    sqa.addPar(stPolicyTypeID);
    }

    if(stPolicyTypeGroupID!=null){
    sqa.addClause("a.ins_policy_type_grp_id = ?");
    sqa.addPar(stPolicyTypeGroupID);
    }

    if(stFltTreatyType!=null){
    sqa.addClause("j.treaty_type = ?");
    sqa.addPar(stFltTreatyType);
    }

    final String sql = "select * from ( "+sqa.getSQL()+" group by d.ins_cover_id,e.description,j.treaty_type,j.comm_ri_pct,a.ccy,h.claim_amount,i.sharepct " +
    " ) a where premi_amt <> 0 or nd_brok2pct <> 0 order by cover_type_code ";

    sqa1.addSelect(
    " e.description as cover_type_code, sum(i.claim_amount) as claim_amount "
    );

    sqa1.addQuery(
    " from ins_policy a"+
    " inner join ins_pol_obj c on c.pol_id=a.pol_id"+
    " inner join ins_pol_cover d on a.pol_id = d.pol_id"+
    " inner join ins_cover e on d.ins_cover_id = e.ins_cover_id"+
    " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"+
    " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"+
    " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"+
    " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"+
    " inner join ent_master m on m.ent_id = i.member_ent_id ");


    sqa1.addClause("a.status IN ('CLAIM')");
    sqa1.addClause("a.claim_status = 'DLA'");
    sqa1.addClause("a.claim_effective_flag = 'Y'");
    sqa1.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5')");

    if(periodFrom!=null) {
    sqa1.addClause("date_trunc('day',a.period_start) >= ?");
    sqa1.addPar(periodFrom);
    }

    if(periodTo!=null) {
    sqa1.addClause("date_trunc('day',a.period_start) <= ?");
    sqa1.addPar(periodTo);
    }

    if (policyDateFrom!=null) {
    sqa1.addClause("date_trunc('day',a.claim_approved_date) >= ?");
    sqa1.addPar(policyDateFrom);
    }

    if (policyDateTo!=null) {
    sqa1.addClause("date_trunc('day',a.claim_approved_date) <= ?");
    sqa1.addPar(policyDateTo);
    }

    if (stBranch!=null) {
    sqa1.addClause("a.cc_code = ?");
    sqa1.addPar(stBranch);
    }

    if (stEntityID!=null) {
    sqa1.addClause("m.ent_id = ?");
    sqa1.addPar(stEntityID);
    }

    if(stPolicyTypeID!=null){
    sqa1.addClause("a.pol_type_id = ?");
    sqa1.addPar(stPolicyTypeID);
    }

    if(stPolicyTypeGroupID!=null){
    sqa1.addClause("a.ins_policy_type_grp_id = ?");
    sqa1.addPar(stPolicyTypeGroupID);
    }

    if(stFltTreatyType!=null){
    sqa1.addClause("j.treaty_type = ?");
    sqa1.addPar(stFltTreatyType);
    }

    final String sql1 = "select * from ( "+sqa1.getSQL()+" group by e.description,a.pol_id,a.pol_no,a.dla_no " +
    " ) a where claim_amount <> 0 order by cover_type_code ";

    final DTOList l = ListUtil.getDTOListFromQuery(
    sql,
    sqa.getPar(),
    InsurancePolicyView.class
    );

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    final DTOList m = ListUtil.getDTOListFromQuery(
    sql1,
    sqa1.getPar(),
    InsurancePolicyView.class
    );

    SessionManager.getInstance().getRequest().setAttribute("RPS", m);

    return l;
    }
     */
    /*
    public DTOList SOA_OLD() throws Exception {
    final SQLAssembler sqa = new SQLAssembler();

    if (stPolicyTypeGroupID != null) {
    if (stPolicyTypeID != null) {
    if (stPolicyTypeID.equalsIgnoreCase("59") || stPolicyTypeID.equalsIgnoreCase("64")) {
    sqa.addSelect(" a.cover_type_code,a.share_pct,a.cust_name,a.entity_id,"
    + " sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
    //+ " round(((nd_brok2pct/getvalid(premi_amt=0,premi_amt))*100),2) as nd_hfeepct "
    + " from ( select a.cover_type_code,j.treaty_type as co_treaty_id,i.sharepct as share_pct,a.captive as cust_name,k.ent_id as entity_id, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0))) as premi_amt, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0))) as nd_brok2pct, "
    + " sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA', coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0))) as claim_amount "
    + " from ( "
    + " select a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total,"
    + " a.status,a.claim_status,a.effective_flag,a.policy_date,a.approved_date,a.period_start,a.ccy,a.ccy_rate,a.pol_type_id,getcaptive(substr(a.pol_no,2,1),d.ins_cover_id) as captive,"
    + " a.ins_policy_type_grp_id "
    + " from ins_policy a "
    + " inner join ins_pol_cover d on d.pol_id = a.pol_id "
    + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
    + " group by a.pol_id,a.pol_no,e.cover_type,a.status,a.claim_status,a.effective_flag, "
    + " a.policy_date,a.approved_date,a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,d.ins_cover_id,a.ins_policy_type_grp_id ) a ");
    } else {
    sqa.addSelect(" a.cover_type_code,a.share_pct,a.cust_name,a.entity_id,"
    + " sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
    //+ " round(((nd_brok2pct/getvalid(premi_amt=0,premi_amt))*100),2) as nd_hfeepct "
    + " from ( select a.cover_type_code,j.treaty_type as co_treaty_id,i.sharepct as share_pct,a.captive as cust_name,k.ent_id as entity_id, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0))) as premi_amt, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0))) as nd_brok2pct, "
    + " sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA', coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0))) as claim_amount "
    + " from ( "
    + " select a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total,"
    + " a.status,a.claim_status,a.effective_flag,a.policy_date,a.approved_date,a.period_start,a.ccy,a.ccy_rate,a.pol_type_id,getcaptive(substr(a.pol_no,2,1),d.ins_cover_id) as captive,"
    + " a.ins_policy_type_grp_id "
    + " from ins_policy a "
    + " inner join ins_pol_cover d on d.pol_id = a.pol_id "
    + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
    + " group by a.pol_id,a.pol_no,e.cover_type,a.status,a.claim_status,a.effective_flag, "
    + " a.policy_date,a.approved_date,a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,d.ins_cover_id,a.ins_policy_type_grp_id ) a ");
    }
    } else if (stPolicyTypeID == null) {
    sqa.addSelect(" a.cover_type_code,a.share_pct,a.cust_name,a.entity_id,"
    + " sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
    //+ " round(((nd_brok2pct/getvalid(premi_amt=0,premi_amt))*100),2) as nd_hfeepct "
    + " from ( select a.cover_type_code,j.treaty_type as co_treaty_id,i.sharepct as share_pct,a.captive as cust_name,k.ent_id as entity_id, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0))) as premi_amt, "
    + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0))) as nd_brok2pct, "
    + " sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA', coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0))) as claim_amount "
    + " from ( "
    + " select a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total,"
    + " a.status,a.claim_status,a.effective_flag,a.policy_date,a.approved_date,a.period_start,a.ccy,a.ccy_rate,a.pol_type_id,getcaptive(substr(a.pol_no,2,1),d.ins_cover_id) as captive,"
    + " a.ins_policy_type_grp_id "
    + " from ins_policy a "
    + " inner join ins_pol_cover d on d.pol_id = a.pol_id "
    + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
    + " group by a.pol_id,a.pol_no,e.cover_type,a.status,a.claim_status,a.effective_flag, "
    + " a.policy_date,a.approved_date,a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,d.ins_cover_id,a.ins_policy_type_grp_id ) a ");
    }
    }

    sqa.addQuery(
    " inner join ins_pol_obj c on c.pol_id = a.pol_id "
    + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
    + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
    + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
    + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
    + " inner join ent_master k on k.ent_id = i.member_ent_id ");


    sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");

    sqa.addClause("a.effective_flag = 'Y'");

    if (periodFrom != null) {
    sqa.addClause("date_trunc('day',a.period_start) >= ?");
    sqa.addPar(periodFrom);
    }

    if (periodTo != null) {
    sqa.addClause("date_trunc('day',a.period_start) <= ?");
    sqa.addPar(periodTo);
    }

    if (policyDateFrom != null) {
    sqa.addClause("date_trunc('day',a.approved_date) >= ?");
    sqa.addPar(policyDateFrom);
    }

    if (policyDateTo != null) {
    sqa.addClause("date_trunc('day',a.approved_date) <= ?");
    sqa.addPar(policyDateTo);
    }

    if (stBranch != null) {
    sqa.addClause("a.cc_code = ?");
    sqa.addPar(stBranch);
    }

    if (stRegion != null) {
    sqa.addClause("a.region_id = ?");
    sqa.addPar(stRegion);
    }

    if (stEntityID != null) {
    sqa.addClause("k.ent_id = ?");
    sqa.addPar(stEntityID);
    }

    if (stPolicyTypeID != null) {
    sqa.addClause("a.pol_type_id = ?");
    sqa.addPar(stPolicyTypeID);
    }

    if (stPolicyTypeGroupID != null) {
    sqa.addClause("a.ins_policy_type_grp_id = ?");
    sqa.addPar(stPolicyTypeGroupID);
    }

    if (stFltTreatyType != null) {
    //if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
    //    sqa.addClause("j.treaty_type in ('SPL','QS')");
    //} else {
    sqa.addClause("j.treaty_type = ?");
    sqa.addPar(stFltTreatyType);
    //}
    }

    String sql = null;

    if (stPolicyTypeGroupID != null) {
    if (stPolicyTypeID != null) {
    if (stPolicyTypeID.equalsIgnoreCase("59") || stPolicyTypeID.equalsIgnoreCase("64")) {
    sql = sqa.getSQL() + " group by a.cover_type_code,j.treaty_type,i.sharepct,a.captive,k.ent_id "
    + ") a where (premi_amt <> 0 or claim_amount <> 0) group by a.cover_type_code,a.share_pct,a.cust_name,a.entity_id order by a.cover_type_code ";
    } else {
    sql = sqa.getSQL() + " group by a.cover_type_code,j.treaty_type,k.ent_id,i.sharepct,a.captive "
    + ") a where (premi_amt <> 0 or claim_amount <> 0) group by a.cover_type_code,a.share_pct,a.cust_name,a.entity_id order by a.cover_type_code,a.cust_name ";
    }
    } else if (stPolicyTypeID == null) {
    sql = sqa.getSQL() + " group by a.cover_type_code,j.treaty_type,k.ent_id,i.sharepct,a.captive "
    + ") a where (premi_amt <> 0 or claim_amount <> 0) group by a.cover_type_code,a.share_pct,a.cust_name,a.entity_id order by a.cover_type_code,a.cust_name ";
    }
    }

    final DTOList l = ListUtil.getDTOListFromQuery(
    sql,
    sqa.getPar(),
    InsurancePolicyView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
    }
     */
    public DTOList EXCEL_ACCUMULATION() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4) as u_year,a.pol_no,a.status,a.cust_name, "
                + "b.ref9d as postal_code,b.ref1 as occupation,b.risk_class,b.ref3 as risk_code,b.ref5 as address,a.ccy,a.ccy_rate, "
                + "a.insured_amount,  a.premi_total, "
                + "(case b.risk_class when 1 then c.tre_limit1 when 2 then c.tre_limit2 when 3 then c.tre_limit3 else 0 end) as treaty_limit, "
                + "( select coalesce(round(sum(getkoas(z.ins_tsi_cat_id  in (6,105),z.insured_amount)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as building, "
                + "( select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as machine, "
                + "( select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as stock, "
                + "( select coalesce(round(sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as other, "
                + "b.insured_amount as total_tsi, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id=27,y.premi)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as cov_fire, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id in (90,91,92,93,94),y.premi)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as cov_riot, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id in (21,22),y.premi)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as cov_earthquake, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id=29,y.premi)),0),0) "
                + "  from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as cov_flood, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id=114,y.premi)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as cov_water_damage, "
                + "( select coalesce(round(sum(getkoas(y.ins_cover_id=87,y.premi)),0),0) "
                + "from ins_policy x "
                + "inner join ins_pol_cover y on y.pol_id = x.pol_id "
                + "inner join ins_cover z on z.ins_cover_id = y.ins_cover_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id)    as cov_prop_all_risk, "
                + "coalesce(round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2),0) as tsi_spl, "
                + "coalesce(round(sum(checkreas(j.treaty_type='SPL',i.premi_amount)),2),0) as premi_spl, "
                + "coalesce(round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)),2),0) as komisi_spl ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "left join ins_risk_cat c on b.ins_risk_cat_id = c.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("(a.pol_type_id=1 or a.pol_type_id=81 or a.pol_type_id=19)");

        sqa.addClause("j.treaty_type = 'SPL'");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = sqa.getSQL() + " group by a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4),a.pol_no,a.status,a.pol_id,a.cust_name,a.insured_amount,a.premi_total, "
                + "b.ref1,b.risk_class,b.ref3,c.description,b.ref5,a.ccy,a.ccy_rate,b.insured_amount,a.rate_method_desc,b.ref6d, "
                + "c.tre_limit1,c.tre_limit2,c.tre_limit3,b.ins_pol_obj_id,a.pol_type_id,b.ref9d,c.ins_risk_cat_code,b.ref2d,c.exc_risk_flag "
                + "order by a.pol_type_id,substr(a.period_start::text,1,4),a.pol_no,b.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_ACCUMULATION() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("policy_date");
            row0.createCell(1).setCellValue("period_start");
            row0.createCell(2).setCellValue("period_end");
            row0.createCell(3).setCellValue("u_year");
            row0.createCell(4).setCellValue("pol_no");
            row0.createCell(5).setCellValue("status");
            row0.createCell(6).setCellValue("cust_name");
            row0.createCell(7).setCellValue("postal_code");
            row0.createCell(8).setCellValue("occupation");
            row0.createCell(9).setCellValue("risk_class");
            row0.createCell(10).setCellValue("risk_code");
            row0.createCell(11).setCellValue("address");
            row0.createCell(12).setCellValue("ccy");
            row0.createCell(13).setCellValue("ccy_rate");
            row0.createCell(14).setCellValue("insured_amount");
            row0.createCell(15).setCellValue("premi_total");
            row0.createCell(16).setCellValue("treaty_limit");
            row0.createCell(17).setCellValue("building");
            row0.createCell(18).setCellValue("machine");
            row0.createCell(19).setCellValue("stock");
            row0.createCell(20).setCellValue("other");
            row0.createCell(21).setCellValue("total_tsi");
            row0.createCell(22).setCellValue("cov_fire");
            row0.createCell(23).setCellValue("cov_riot");
            row0.createCell(24).setCellValue("cov_earthquake");
            row0.createCell(25).setCellValue("cov_flood");
            row0.createCell(26).setCellValue("cov_water_damage");
            row0.createCell(27).setCellValue("cov_prop_all_risk");
            row0.createCell(28).setCellValue("tsi_spl");
            row0.createCell(29).setCellValue("premi_spl");
            row0.createCell(30).setCellValue("komisi_spl");



            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("u_year"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("postal_code"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("occupation"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("address"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("total_tsi").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("cov_fire").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("cov_riot").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("cov_earthquake").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("cov_flood").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("cov_water_damage").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("cov_prop_all_risk").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl").doubleValue());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public String getStNama() {
        return stNama;
    }

    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    public DTOList EXCEL_REAS_TREATY() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	m.short_desc,a.status,a.pol_no,a.period_start,a.period_end,a.policy_date,i.ri_slip_no as f_ri_finish,l.reas_ent_id,l.ent_name,a.ccy,a.ccy_rate,(a.premi_total) as premi_base,"
                + "sum(coalesce(i.premi_amount,0)) as premi_total,sum(coalesce(i.ricomm_amt,0)) as nd_comm1 ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id "
                + "left join ent_master l on l.ent_id = i.member_ent_id "
                + "left join ins_policy_types m on m.pol_type_id = a.pol_type_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("k.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        if (stEntityID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = " select short_desc,reas_ent_id,ent_name,status,pol_no,period_start,period_end,policy_date,f_ri_finish,ccy,ccy_rate,sum(premi_base) as premi_base,sum(premi_total) as premi_total,sum(nd_comm1) as nd_comm1,sum(premi_total-nd_comm1) as premi_total_adisc from ( "
                + sqa.getSQL() + " group by a.status,a.ccy,a.pol_no,a.period_start,a.period_end,a.policy_date,a.ccy_rate,a.premi_total,i.ri_slip_no,l.reas_ent_id,l.ent_name,m.short_desc "
                + " ) x group by short_desc,reas_ent_id,ent_name,status,pol_no,period_start,period_end,policy_date,f_ri_finish,ccy,ccy_rate order by pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REAS_TREATY() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(getStCoinsurerName());

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("status");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("period_end");
            row0.createCell(4).setCellValue("policy_date");
            row0.createCell(5).setCellValue("ri_slip_no");
            row0.createCell(6).setCellValue("bisnis");
            row0.createCell(7).setCellValue("ent_id");
            row0.createCell(8).setCellValue("name");
            row0.createCell(9).setCellValue("ccy");
            row0.createCell(10).setCellValue("ccy_rate");
            row0.createCell(11).setCellValue("premi_total");
            row0.createCell(12).setCellValue("premi_reas");
            row0.createCell(13).setCellValue("komisi_reas");
            row0.createCell(14).setCellValue("netto_reas");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("f_ri_finish"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList REAS_TREATY() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,a.cust_name,l.short_name as prod_name,a.period_start,a.period_end,a.policy_date,i.ri_slip_no as f_ri_finish,"
                + "a.ccy,a.ccy_rate,(c.premi_total) as premi_base,c.ref5 as ref5,c.ref7d as ref7,c.risk_class as ref2,"
                + "(select sum(x.rate) from ins_pol_cover x where x.ins_pol_obj_id = c.ins_pol_obj_id group by x.ins_pol_obj_id) as refn1, "
                + "sum(coalesce(i.premi_amount,0)) as premi_total,sum(coalesce(i.ricomm_amt,0)) as nd_comm1 ");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id "
                + "left join ent_master l on l.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("substr(a.pol_no,2,1) = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        if (stCoinsurerID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        final String sql = " select pol_no,cust_name,prod_name,period_start,period_end,policy_date,f_ri_finish,ccy,ccy_rate,ref5,ref7,ref2,refn1,sum(premi_base) as premi_base,sum(premi_total) as premi_total,sum(nd_comm1) as nd_comm1,sum(premi_total-nd_comm1) as premi_total_adisc from ( "
                + sqa.getSQL() + " group by a.ccy,a.pol_no,a.cust_name,l.short_name,a.period_start,a.period_end,a.policy_date,a.ccy_rate,c.premi_total,i.ri_slip_no,c.ins_pol_obj_id,c.ref5,c.ref7d,c.risk_class"
                + " ) x group by pol_no,cust_name,prod_name,period_start,period_end,policy_date,f_ri_finish,ccy,ccy_rate,ref5,ref7,ref2,refn1 order by prod_name,pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

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

    public DTOList EXCEL_REAS() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.entity_id,a.policy_date,a.pol_type_id,a.period_start,a.period_end,a.pol_id,a.pol_no,a.cust_name as cust_name,b.ref5,b.risk_class,b.ref3,a.ccy,a.insured_amount,a.premi_total, "
                + " (round(sum(i.premi_rate),4)||' %') as rate_method_desc,(round(a.share_pct,2)||' %') as rate_method,"
                + "(   	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " 	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " 	where x.pol_id=a.pol_id) as building, "
                + "(      select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " 	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " 	where x.pol_id=a.pol_id) as mesin, "
                + "(  select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " 	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " 	where x.pol_id=a.pol_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " 	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " 	where x.pol_id=a.pol_id) as inventaris, "
                + " 	(	select coalesce(sum(y.amount),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + " 	where y.policy_id=a.pol_id) as tsi_askrida, "
                + " 	(	select coalesce(sum(y.premi_amt),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + " 	where y.policy_id=a.pol_id) as premi_askrida, "
                + " 	(	select coalesce(sum(y.amount),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id<>1 "
                + " 	where y.policy_id=a.pol_id) as tsi_ko, "
                + " 	(	select coalesce(sum(y.premi_amt),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id<>1 "
                + " 	where y.policy_id=a.pol_id) as premi_ko, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)) as tsi_bpdan, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)) as premi_bpdan, "
                + " sum(checkreas(j.treaty_type='OR',i.tsi_amount)) as tsi_or, "
                + " sum(checkreas(j.treaty_type='OR',i.premi_amount)) as premi_or, "
                + " sum(checkreas(j.treaty_type='QS',i.tsi_amount)) as tsi_qs, "
                + " sum(checkreas(j.treaty_type='QS',i.premi_amount)) as premi_qs, "
                + " sum(checkreas(j.treaty_type='SPL',i.tsi_amount)) as tsi_spl, "
                + " sum(checkreas(j.treaty_type='SPL',i.premi_amount)) as premi_spl, "
                + " sum(checkreas(j.treaty_type='FAC',i.tsi_amount)) as tsi_fac, "
                + " sum(checkreas(j.treaty_type='FAC',i.premi_amount)) as premi_fac, "
                + " sum(checkreas(j.treaty_type='PARK',i.tsi_amount)) as tsi_park, "
                + " sum(checkreas(j.treaty_type='PARK',i.premi_amount)) as premi_park, "
                + " sum(checkreas(j.treaty_type='FACO',i.tsi_amount)) as tsi_faco, "
                + " sum(checkreas(j.treaty_type='FACO',i.premi_amount)) as premi_faco, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)) as comm_bpdan, "
                + " sum(checkreas(j.treaty_type='OR',i.ricomm_amt)) as comm_or, "
                + " sum(checkreas(j.treaty_type='QS',i.ricomm_amt)) as comm_qs, "
                + " sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)) as comm_spl, "
                + " sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)) as comm_fac, "
                + " sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)) as comm_park, "
                + " sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)) as comm_faco, "
                + " sum(checkreas(j.treaty_type='XOL1',i.tsi_amount)) as tsi_xol1, "
                + " sum(checkreas(j.treaty_type='XOL2',i.tsi_amount)) as tsi_xol2, "
                + " sum(checkreas(j.treaty_type='XOL3',i.tsi_amount)) as tsi_xol3, "
                + " sum(checkreas(j.treaty_type='XOL4',i.tsi_amount)) as tsi_xol4, "
                + " sum(checkreas(j.treaty_type='XOL5',i.tsi_amount)) as tsi_xol5, "
                + " sum(checkreas(j.treaty_type='XOL1',i.premi_amount)) as premi_xol1, "
                + " sum(checkreas(j.treaty_type='XOL2',i.premi_amount)) as premi_xol2, "
                + " sum(checkreas(j.treaty_type='XOL3',i.premi_amount)) as premi_xol3, "
                + " sum(checkreas(j.treaty_type='XOL4',i.premi_amount)) as premi_xol4, "
                + " sum(checkreas(j.treaty_type='XOL5',i.premi_amount)) as premi_xol5");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from "
                + " ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id=a.pol_id " + clauseObj
                + " inner join ins_policy_types f on f.pol_type_id =a.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("k.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        final String sql = sqa.getSQL() + " group by a.entity_id,a.policy_date,a.pol_type_id,a.period_start,a.period_end,a.pol_id,a.pol_no,a.cust_name,"
                + " b.ref5,b.risk_class,b.ref3,a.ccy,a.insured_amount,a.premi_total,a.share_pct "
                + " order by a.pol_no asc";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_REAS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("policy date");
            row0.createCell(1).setCellValue("pol type id");
            row0.createCell(2).setCellValue("period start");
            row0.createCell(3).setCellValue("period end");
            row0.createCell(4).setCellValue("pol id");
            row0.createCell(5).setCellValue("pol no");
            row0.createCell(6).setCellValue("entity id");
            row0.createCell(7).setCellValue("cust name");
            row0.createCell(8).setCellValue("alamat");
            row0.createCell(9).setCellValue("kelas");
            row0.createCell(10).setCellValue("kode resiko");
            row0.createCell(11).setCellValue("ccy");
            row0.createCell(12).setCellValue("insured amount");
            row0.createCell(13).setCellValue("building");
            row0.createCell(14).setCellValue("machine");
            row0.createCell(15).setCellValue("stocks");
            row0.createCell(16).setCellValue("inventaris");
            row0.createCell(17).setCellValue("premi total");
            row0.createCell(18).setCellValue("rate method desc");
            row0.createCell(19).setCellValue("rate method");
            row0.createCell(20).setCellValue("tsi askrida");
            row0.createCell(21).setCellValue("premi askrida");
            row0.createCell(22).setCellValue("tsi ko");
            row0.createCell(23).setCellValue("premi ko");
            row0.createCell(24).setCellValue("tsi bpdan");
            row0.createCell(25).setCellValue("premi bpdan");
            row0.createCell(26).setCellValue("tsi or");
            row0.createCell(27).setCellValue("premi or");
            row0.createCell(28).setCellValue("tsi qs");
            row0.createCell(29).setCellValue("premi qs");
            row0.createCell(30).setCellValue("tsi spl");
            row0.createCell(31).setCellValue("premi spl");
            row0.createCell(32).setCellValue("tsi fac");
            row0.createCell(33).setCellValue("premi fac");
            row0.createCell(34).setCellValue("tsi park");
            row0.createCell(35).setCellValue("premi park");
            row0.createCell(36).setCellValue("tsi faco");
            row0.createCell(37).setCellValue("premi faco");
            row0.createCell(38).setCellValue("comm bpdan");
            row0.createCell(39).setCellValue("comm or");
            row0.createCell(40).setCellValue("comm qs");
            row0.createCell(41).setCellValue("comm spl");
            row0.createCell(42).setCellValue("comm fac");
            row0.createCell(43).setCellValue("comm park");
            row0.createCell(44).setCellValue("comm faco");
            row0.createCell(45).setCellValue("tsi xol1");
            row0.createCell(46).setCellValue("tsi xol2");
            row0.createCell(47).setCellValue("tsi xol3");
            row0.createCell(48).setCellValue("tsi xol4");
            row0.createCell(49).setCellValue("tsi xol5");
            row0.createCell(50).setCellValue("premi xol1");
            row0.createCell(51).setCellValue("premi xol2");
            row0.createCell(52).setCellValue("premi xol3");
            row0.createCell(53).setCellValue("premi xol4");
            row0.createCell(54).setCellValue("premi xol5");
            row0.createCell(55).setCellValue("trans date");
            row0.createCell(56).setCellValue("trans flag");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("entity_id").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            if (h.getFieldValueByFieldNameST("ref5") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            }
            if (h.getFieldValueByFieldNameST("risk_class") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            }
            if (h.getFieldValueByFieldNameST("ref3") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ref3"));
            }
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("mesin").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("inventaris").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            if (h.getFieldValueByFieldNameST("rate_method_desc") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameST("rate_method_desc"));
            }
            if (h.getFieldValueByFieldNameST("rate_method") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("rate_method"));
            }
            if (h.getFieldValueByFieldNameBD("tsi_askrida") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("tsi_askrida").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_askrida") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("premi_askrida").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_ko") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("tsi_ko").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_ko") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("premi_ko").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_bpdan") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("tsi_bpdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_bpdan") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_bpdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_or") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("tsi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_or") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("premi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_qs") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_qs") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_spl") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_spl") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_fac") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_fac") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_park") != null) {
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("tsi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_park") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("premi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_bpdan") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("comm_bpdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_or") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("comm_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_qs") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("comm_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_spl") != null) {
                row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("comm_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_fac") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("comm_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_park") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("comm_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_faco") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("comm_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_xol1") != null) {
                row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("tsi_xol1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_xol2") != null) {
                row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("tsi_xol2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_xol3") != null) {
                row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("tsi_xol3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_xol4") != null) {
                row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("tsi_xol4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_xol5") != null) {
                row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("tsi_xol5").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_xol1") != null) {
                row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("premi_xol1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_xol2") != null) {
                row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("premi_xol2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_xol3") != null) {
                row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("premi_xol3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_xol4") != null) {
                row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("premi_xol4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_xol5") != null) {
                row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("premi_xol5").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public DTOList LAPDAN() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,a.cc_code,a.ccy,a.ccy_rate,coalesce(a.premi_total,0) as premi_base,"
                + "sum(coalesce(i.premi_amount,0)) as premi_total,sum(coalesce(i.ricomm_amt,0)) as nd_comm1");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("k.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        final String sql = " select cc_code,ccy,sum(premi_base) as premi_base,sum(premi_total) as premi_total,sum(nd_comm1) as nd_comm1,sum(premi_total-nd_comm1) as premi_total_adisc "
                + " from ( " + sqa.getSQL() + " group by a.cc_code,a.ccy,a.ccy_rate,a.premi_total,a.pol_no ) x where (premi_base <> 0 or premi_total <> 0 or nd_comm1 <> 0) group by cc_code,ccy "
                + " order by cc_code,ccy";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList LAPDANPERBULAN() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code,coalesce(a.premi_total*a.ccy_rate,0) as premi_base,i.ricomm_rate as amount,"
                + "sum(coalesce(i.premi_amount*a.ccy_rate,0)) as premi_total,sum(coalesce(i.ricomm_amt*a.ccy_rate,0)) as nd_comm1");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("claim_status = ?");
            sqa.addPar(FLT_CLAIM_STATUS);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("k.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stStatus != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatus);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        final String sql = " select sum(premi_base) as premi_base,sum(premi_total) as premi_total,sum(nd_comm1) as nd_comm1,sum(premi_total-nd_comm1) as premi_total_adisc,amount "
                + " from ( " + sqa.getSQL() + " group by a.cc_code,a.ccy_rate,a.premi_total,i.ricomm_rate ) x where (premi_base <> 0 or premi_total <> 0 or nd_comm1 <> 0) group by amount ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }
    /*
    public void EXCEL_RISK_CONTROL() throws Exception {
    final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
    final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

    final SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(
    " a.create_who,a.endorse_notes,a.period_start,a.period_end," +
    " a.policy_date,(substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years," +
    " a.status,a.pol_no,a.ref1 as nomor_pp,a.entity_id::text,k.ent_name,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d as kode_pos,b.ref10,b.ref11, "+
    " b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.ref11,b.risk_class,b.refd1,b.refd2,b.refd3, "+
    " f.ins_risk_cat_code, a.ccy,a.insured_amount,a.premi_total,b.insured_amount as insured_amount_e,b.premi_total_bcoins as premi_total_e,b.premi_total as premi_total_d, "+
    " round(((sum(i.premi_amount)/sum(case i.tsi_amount when 0 then 1 else i.tsi_amount end))*100),4) as rate_method_desc, "+ //i.valid_ri_date,
    " round(sum(i.premi_rate),5) as premi_rate,b.order_no, "+
    " round(a.share_pct,2) as rate_method, "+
    " (	select round(sum(z.rate),5) "+
    " from ins_policy x "+
    " inner join ins_pol_obj y on y.pol_id = x.pol_id "+
    " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "+
    " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "+
    "(	select sum(getkoas(z.ins_tsi_cat_id=6,z.insured_amount)) "+
    " from ins_policy x "+
    " inner join ins_pol_obj y on y.pol_id = x.pol_id "+
    " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "+
    " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "+
    "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40),z.insured_amount)) "+
    " from ins_policy x "+
    " inner join ins_pol_obj y on y.pol_id = x.pol_id "+
    " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "+
    " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "+
    "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "+
    " from ins_policy x "+
    " inner join ins_pol_obj y on y.pol_id = x.pol_id "+
    " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "+
    " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "+
    "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "+
    " from ins_policy x "+
    " inner join ins_pol_obj y on y.pol_id = x.pol_id "+
    " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "+
    " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "+
    " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2) as tsi_or, "+
    " round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) as premi_or, "+
    " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt)),2) as komisi_or, "+
    " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2) as tsi_bppdan, "+
    " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)),2) as premi_bppdan, "+
    " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)),2) as komisi_bppdan, "+
    " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2) as tsi_spl, "+
    " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount)),2) as premi_spl, "+
    " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)),2) as komisi_spl, "+
    " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2) as tsi_fac, "+
    " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2) as premi_fac, "+
    " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2) as komisi_fac, "+
    " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount)),2) as tsi_qs, "+
    " round(sum(checkreas(j.treaty_type='QS',i.premi_amount)),2) as premi_qs, "+
    " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt)),2) as komisi_qs, "+
    " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2) as tsi_park, "+
    " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount)),2) as premi_park, "+
    " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)),2) as komisi_park, "+
    " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2) as tsi_faco, "+
    " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2) as premi_faco, "+
    " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2) as komisi_faco, "+
    " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount)),2) as tsi_faco1, "+
    " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount)),2) as premi_faco1, "+
    " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt)),2) as komisi_faco1, "+
    " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount)),2) as tsi_faco2, "+
    " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount)),2) as premi_faco2, "+
    " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt)),2) as komisi_faco2, "+
    " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount)),2) as tsi_faco3, "+
    " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount)),2) as premi_faco3, "+
    " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt)),2) as komisi_faco3, "+
    " a.cover_type_code, "+
    " checkpremi(a.cover_type_code = 'COINSIN',(select y.share_pct "+
    " from ins_policy x "+
    " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 "+
    " where x.pol_id = a.pol_id),a.share_pct) as member, "+
    " coalesce((SELECT SUM(rate) "+
    " FROM INS_POL_ITEMS y "+
    " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct, "+
    " coalesce((SELECT SUM(amount) "+
    " FROM INS_POL_ITEMS y "+
    " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi"
    );

    sqa.addQuery("from ins_policy a "+
    "inner join ins_pol_obj b on b.pol_id = a.pol_id "+
    "left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "+
    "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "+
    "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
    "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
    "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
    "left join  ent_master k on k.ent_id = a.entity_id "
    );

    sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

    if (policyDateFrom!=null) {
    sqa.addClause("date_trunc('day',a.policy_date) >= '"+policyDateFrom+"'");
    //sqa.addPar(policyDateFrom);
    }

    if (policyDateTo!=null) {
    sqa.addClause("date_trunc('day',a.policy_date) <= '"+policyDateTo+"'");
    //sqa.addPar(policyDateTo);
    }

    if(periodFrom!=null) {
    sqa.addClause("date_trunc('day',a.period_start) >= "+periodFrom);
    //sqa.addPar(periodFrom);
    }

    if(periodTo!=null) {
    sqa.addClause("date_trunc('day',a.period_start) <= "+periodTo);
    //sqa.addPar(periodTo);
    }

    if(periodEndFrom!=null) {
    sqa.addClause("date_trunc('day',a.period_end) >= "+periodEndFrom);
    //sqa.addPar(periodEndFrom);
    }

    if(periodEndTo!=null) {
    sqa.addClause("date_trunc('day',a.period_end) <= "+periodEndTo);
    //sqa.addPar(periodEndTo);
    }

    if (stBranch!=null) {
    sqa.addClause("a.cc_code = "+stBranch);
    //sqa.addPar(stBranch);
    }

    if (stRegion!=null) {
    sqa.addClause("a.region_id = "+stRegion);
    //sqa.addPar(stRegion);
    }

    if(stPolicyTypeGroupID!=null){
    sqa.addClause("a.ins_policy_type_grp_id = "+stPolicyTypeGroupID);
    //sqa.addPar(stPolicyTypeGroupID);
    }

    if(stPolicyTypeID!=null){
    sqa.addClause("a.pol_type_id = "+stPolicyTypeID);
    //sqa.addPar(stPolicyTypeID);
    }

    if (stPolicyNo!=null) {
    sqa.addClause("a.pol_no like '%"+stPolicyNo+"%'");
    //sqa.addPar('%'+stPolicyNo+'%');
    }

    if (stPostCode!=null) {
    sqa.addClause("b.ref9 = "+stPostCode);
    //sqa.addPar(stPostCode);
    }

    if (stPostCodeDesc2!=null) {
    sqa.addClause("b.ref9d = "+stPostCodeDesc2);
    //sqa.addPar(stPostCodeDesc2);
    }

    if (stZoneCode!=null) {
    sqa.addClause("d.zone_id = "+stZoneCode);
    //sqa.addPar(stZoneCode);
    }

    if (stZoneEquakeName!=null) {
    sqa.addClause("b.ref2d = "+stZoneEquakeName);
    //sqa.addPar(stZoneEquakeName);
    }

    if(stFltTreatyType!=null){
    sqa.addClause("j.treaty_type = "+stFltTreatyType);
    //sqa.addPar(stFltTreatyType);
    }

    if (stYearTreaty!=null){
    sqa.addClause("substr(i.valid_ri_date::text,1,4) = " +stYearTreaty);
    //sqa.addPar(stYearTreaty);
    }

    if (Tools.isYes(stIndex)) {
    sqa.addClause("a.effective_flag = 'Y'");
    }

    if (Tools.isNo(stIndex)) {
    sqa.addClause("a.effective_flag = 'N'");
    }

    String sql = "select * from ( " +sqa.getSQL()+ " group by a.endorse_notes,a.ref1,a.pol_id,a.status,a.pol_no,a.entity_id,k.ent_name,a.cust_name,a.period_start,a.period_end,a.policy_date,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d,b.ref10,b.ref11,f.ins_risk_cat_code,b.refd1,b.refd2,b.refd3, "+
    " a.create_who,a.ccy,a.ccy_rate,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total_bcoins,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.risk_class,b.order_no "+
    //" ,i.valid_ri_date having count(a.pol_no)>1 " +
    ") x ";

    if (getStTime()!=null) {
    sql = sql + " where years > '1' ";
    }

    sql = sql + "order by pol_no,ins_pol_obj_id";

    SQLUtil S = new SQLUtil();

    String nama_file = "riskcontrol_"+System.currentTimeMillis()+".csv";

    sql = "Copy ("+
    sql +
    " ) To 'D://exportdb/csv/"+ nama_file +"' With CSV HEADER;";

    final PreparedStatement ps = S.setQuery(sql);

    boolean tes = ps.execute();

    S.release();

    File file = new File("//db03.askrida.co.id/exportdb/csv/"+nama_file);
    int length = 0;
    ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

    SessionManager.getInstance().getResponse().setContentType("text/csv");
    SessionManager.getInstance().getResponse().setContentLength((int)file.length());

    // sets HTTP header
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

    int BUFSIZE = 4096;
    byte[] byteBuffer = new byte[BUFSIZE];
    DataInputStream in = new DataInputStream(new FileInputStream(file));

    // reads the file's bytes and writes them to the response stream
    while ((in != null) && ((length = in.read(byteBuffer)) != -1))
    {
    outStream.write(byteBuffer,0,length);
    }

    in.close();
    outStream.close();

    file.delete();

    }
     */

    public DTOList EXCEL_RISK_CONTROL_OLD() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.create_who,a.endorse_notes,a.period_start,a.period_end,"
                + " a.policy_date,(substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,"
                + " a.status,a.pol_no,a.ref1 as nomor_pp,a.entity_id::text,k.ent_name,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d as kode_pos,b.ref10,b.ref11, "
                + " b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.ref11,b.risk_class,b.refd1,b.refd2,b.refd3,b.refd5, "
                + " f.ins_risk_cat_code, a.ccy,a.insured_amount,a.premi_total,b.insured_amount as insured_amount_e,b.premi_total_bcoins as premi_total_e,b.premi_total as premi_total_d, "
                + " round(((sum(i.premi_amount)/sum(case i.tsi_amount when 0 then 1 else i.tsi_amount end))*100),4) as rate_method_desc, " + //i.valid_ri_date,
                " round(sum(i.premi_rate),5) as premi_rate,b.order_no, "
                + " round(a.share_pct,2) as rate_method, "
                + " (	select round(sum(z.rate),5) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + " round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2) as tsi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) as premi_or, "
                + " round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt)),2) as komisi_or, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2) as tsi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)),2) as komisi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2) as tsi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.premi_amount)),2) as premi_spl, "
                + " round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)),2) as komisi_spl, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2) as tsi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2) as komisi_fac, "
                + " round(sum(checkreas(j.treaty_type='QS',i.tsi_amount)),2) as tsi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2) as tsi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount)),2) as premi_park, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)),2) as komisi_park, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2) as tsi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2) as premi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2) as komisi_faco, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount)),2) as tsi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount)),2) as premi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt)),2) as komisi_faco1, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount)),2) as tsi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount)),2) as premi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt)),2) as komisi_faco2, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount)),2) as tsi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount)),2) as premi_faco3, "
                + " round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt)),2) as komisi_faco3, "
                + " a.cover_type_code, "
                + " checkpremi(a.cover_type_code = 'COINSIN',(select y.share_pct "
                + " from ins_policy x "
                + " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 and a.pol_type_id <> 21 "
                + " where x.pol_id = a.pol_id),coalesce(a.share_pct,100)) as member, "
                + " coalesce((SELECT SUM(rate) "
                + " FROM INS_POL_ITEMS y "
                + " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct, "
                + " coalesce((SELECT SUM(amount) "
                + " FROM INS_POL_ITEMS y "
                + " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join  ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end) >= ?");
            sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sqa.addClause("date_trunc('day',a.period_end) <= ?");
            sqa.addPar(periodEndTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = ?");
            sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stYearTreaty != null) {
            sqa.addClause("substr(i.valid_ri_date::text,1,4) = ?");
            sqa.addPar(stYearTreaty);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        String sql = "select * from ( " + sqa.getSQL() + " group by a.endorse_notes,a.ref1,a.pol_id,a.status,a.pol_no,a.entity_id,k.ent_name,a.cust_name,a.period_start,a.period_end,a.policy_date,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d,b.ref10,b.ref11,f.ins_risk_cat_code,b.refd1,b.refd2,b.refd3,b.refd5, "
                + " a.create_who,a.ccy,a.ccy_rate,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total_bcoins,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.risk_class,b.order_no "
                + //" ,i.valid_ri_date having count(a.pol_no)>1 " +
                ") x ";

        if (getStTime() != null) {
            if (getStTime().equalsIgnoreCase("1")) {
                sql = sql + " where years > '1' ";
            } else if (getStTime().equalsIgnoreCase("2")) {
                sql = sql + " where years <= '1' ";
            }
        }

        sql = sql + "order by pol_no,ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_RISK_CONTROL() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start_obj,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end_obj, "
                + " a.period_start,a.period_end,c.short_desc as cob,a.cc_code,a.cc_code_source,"
                + " a.create_who,a.endorse_notes,a.policy_date,a.status,a.pol_id,quote_ident(pol_no) as pol_no,a.ref1 as nomor_pp,a.entity_id::text,k.ent_name,a.cust_name,a.ccy_rate,b.ins_pol_obj_id,"
                + " b.ref1,b.ref2,b.ref22,quote_ident(b.ref3) as ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d as kode_pos,b.ref10,b.ref11,getzone3(a.pol_type_id in (1,81),b.ref16) as provinsi, "
                + " b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.risk_class,b.refd1,b.refd2,b.refd3,b.refd5, "
                + " f.ins_risk_cat_code, a.ccy,a.insured_amount,a.premi_total,"
                //                + "b.insured_amount as insured_amount_obj,b.premi_total as premi_total_obj,"
                + " ( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + " where y.ins_pol_obj_id = b.ins_pol_obj_id) as insured_amount_obj,  "
                + " ( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + " where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_total_obj,"
                + " b.premi_total_bcoins as premi_total_e, "
                //+ " round(((sum(i.premi_amount)/(case sum(i.tsi_amount) when 0 then 1 else sum(i.tsi_amount) end))*100),4) as rate_method_desc, "  //i.valid_ri_date,
                //+" round(sum(i.premi_rate),5) as premi_rate,"
                + " b.order_no, round(a.share_pct,2) as rate_method, "
                + " (	select round(sum(z.rate),5) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + " (  select sum(getkoas(z.ins_cover_id in (269),z.premi)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_fire, "
                + " (  select sum(getkoas(z.ins_cover_id in (270,272),z.premi)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_rsmd, "
                + " (  select sum(getkoas(z.ins_cover_id in (274),z.premi)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_flood, "
                + " (  select sum(getkoas(z.ins_cover_id in (273),z.premi)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_eqvet, "
                + " (  select sum(getkoas(z.ins_cover_id not in (269,270,272,274,273),z.premi)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as premi_others, "
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
                + " round(sum(checkreas(j.treaty_type='QS' and i.member_ent_id = 1,i.premi_amount * a.ccy_rate)),2) as premi_qs_aba, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt * a.ccy_rate)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS' and i.member_ent_id = 1,i.ricomm_amt * a.ccy_rate)),2) as komisi_qs_aba, "
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
                + " a.cover_type_code, "
                + " checkpremi(a.cover_type_code = 'COINSIN',(select y.share_pct "
                + " from ins_policy x "
                + " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 and x.pol_type_id not in (21,59) "
                + " where x.pol_id = a.pol_id),coalesce(a.share_pct,100)) as member, "
                + " coalesce((SELECT SUM(rate) "
                + " FROM INS_POL_ITEMS y "
                + " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi_pct, "
                + " coalesce((SELECT SUM(amount) "
                + " FROM INS_POL_ITEMS y "
                + " WHERE y.pol_id = a.pol_id and y.ins_item_id in(select ins_item_id from ins_items where (item_type = 'COMIS' or ins_item_cat = 'PPN'))),0) as total_komisi,l.user_name, "
                + "(select string_agg(l.description||' - rate : '||round(getvalid2(z.rate is null,z.rate),3)||' - insured : '||z.insured_amount, '| ') "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1,"
                + "d.treaty_name,b.refn3,a.kreasi_type_desc " //+ ",c.mutation_date as tgl_pembukuan "
                );

        String clauseObj = "";
        String objID = "";

        if (policyDateFrom != null) {
            //if(DateUtil.getYear(policyDateFrom)){
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
            //}
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "inner join ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master k on k.ent_id = a.entity_id "
                + "left join s_users l on l.user_id = a.ri_approved_who "
                + "left join ent_master e on e.ent_id = a.prod_id "
                + "left join ins_treaty d on d.ins_treaty_id = g.ins_treaty_id " //+ "left join ar_invoice c on c.attr_pol_id = a.pol_id and i.member_ent_id = c.ent_id "
                );

        sqa.addClause("a.active_flag = 'Y'");

        if (stStatus != null) {
            sqa.addClause(" a.status = '" + stStatus + "'");
        } else {
            sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (journalDateFrom != null) {
            sqa.addClause("date_trunc('day',c.mutation_date) >= '" + journalDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (journalDateTo != null) {
            sqa.addClause("date_trunc('day',c.mutation_date) <= '" + journalDateTo + "'");
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

//        if (stPolicyNo != null) {
//            sqa.addClause("a.pol_no like '%" + stPolicyNo + "%'");
//            //sqa.addPar('%'+stPolicyNo+'%');
//        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqa.addClause("a.pol_no = '" + stPolicyNo + "'");
//                sqa.addClause("a.pol_no = ?");
//                sqa.addPar(stPolicyNo);
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqa.addClause(polno);
            }
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

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
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

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stBussinessPolType != null) {
            sqa.addClause("c.business_type_id = '" + stBussinessPolType + "'");
//            sqa.addPar(stBussinessPolType);
        }

        /*
        if (stPolicyTypeGroupID != null) {
        sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
        //sqa.addPar(stPolicyTypeGroupID);
        }
        
        if (stPolicyTypeID != null) {
        sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
        //sqa.addPar(stPolicyTypeID);
        }*/

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
                //sqa.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
                //sqa.addPar(stPolicyTypeID);
            }
        }
        /*
        if (stYearTreaty != null) {
        sqa.addClause("substr(i.valid_ri_date::text,1,4) = '" + stYearTreaty + "'");
        //sqa.addPar(stYearTreaty);
        }*/
        /*
        if (stFltTreatyType != null) {
        sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
        //sqa.addPar(stFltTreatyType);
        }*/

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
                sqa.addClause("j.treaty_type = '" + stFltTreatyType + "'");
//                sqa.addPar(stFltTreatyType);
            }
        }

        String sql = "select (substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,a.cc_code,a.cc_code_source as koda_nonaks,"
                + "period_start,period_end,create_who,endorse_notes,policy_date,status,cob,pol_id,pol_no,nomor_pp,entity_id,ent_name,"
                + "cust_name,ccy_rate,ins_pol_obj_id,ref1,ref2,ref22,ref3,ref4,ref5,ref6,ref7,ref8,kode_pos,ref10,ref11,provinsi,ref1d,ref2d,"
                + "ref3d,ref4d,ref5d,ref6d,ref7d,ref8d,risk_class,refd1,refd2,refd3,refd5,ins_risk_cat_code,ccy,insured_amount,"
                + "premi_total,insured_amount_obj,premi_total_obj,premi_total_e,order_no,rate_method,rate_cover,building,machine,"
                + "stock,other,premi_fire,premi_rsmd,premi_flood,premi_eqvet,premi_others,tsi_or,premi_or,komisi_or,tsi_bppdan,"
                + "premi_bppdan,komisi_bppdan,tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac,tsi_qs,premi_qs,premi_qs_aba,komisi_qs,komisi_qs_aba,"
                + "tsi_park,premi_park,komisi_park,tsi_faco,premi_faco,komisi_faco,tsi_faco1,premi_faco1,komisi_faco1,tsi_faco2,"
                + "premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,tsi_jp,premi_jp,komisi_jp,tsi_kscbi,premi_kscbi,"
                + "komisi_kscbi,cover_type_code,member,total_komisi_pct,total_komisi,user_name,desc1,treaty_name,refn3,kreasi_type_desc "
                + "from ( " + sqa.getSQL() + " group by a.endorse_notes,a.ref1,a.pol_id,a.status,a.pol_no,a.entity_id,k.ent_name,l.user_name,"
                + "a.cust_name,a.period_start,a.period_end,a.policy_date,b.ref1,b.ref2,b.ref22,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d,"
                + "b.ref10,b.ref11,b.ref16,f.ins_risk_cat_code,b.refd1,b.refd2,b.refd3,b.refd5,a.create_who,a.ccy,a.ccy_rate,a.insured_amount,a.premi_total,b.insured_amount,"
                + "b.premi_total_bcoins,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,"
                + "b.ref7d,b.ref8d,b.risk_class,b.order_no,d.treaty_name,b.refn3,c.short_desc,a.kreasi_type_desc,a.cc_code,a.cc_code_source "
                //+ ",c.mutation_date "
                + //" ,i.valid_ri_date having count(a.pol_no)>1 " +
                " ) a ";


        if (getStStatus() != null) {
            sql = sql + " where a.status = '" + stStatus + "'";
        } else {
            sql = sql + " where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')";
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start_obj) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start_obj) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        if (getPeriodEndFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_end_obj) >= '" + periodEndFrom + "'";
            //sqa.addPar(periodEndFrom);
        }

        if (getPeriodEndTo() != null) {
            sql = sql + " and date_trunc('day',a.period_end_obj) <= '" + periodEndTo + "'";
            //sqa.addPar(periodEndTo);
        }

        sql = "select * from ( " + sql + " ) a ";

        if (getStTime() != null) {
            if (getStTime().equalsIgnoreCase("1")) {
                sql = sql + " where years > '1' ";
            } else if (getStTime().equalsIgnoreCase("2")) {
                sql = sql + " where years <= '1' ";
            }
        }

        sql = sql + " order by pol_no,ins_pol_obj_id";

        String nama_file = "risk_acc_control_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

//        final PreparedStatement ps = S.setQuery(sql);
//
//        boolean tes = ps.execute();
//
//        S.release();
//
//        return new DTOList();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

    }

    public void EXPORT_RISK_CONTROL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        int page = 0;
        int baris = 0;
        int j = 0;
        int jumlahBarisPerSheet = 30000;

        DateTime startDate = new DateTime();
        DateTime endDate = new DateTime();
        Date ValidEndDate;
        int norut = 0;
        int date = 1;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin sheet fleksibel
            if (i % jumlahBarisPerSheet == 0) {
                page = page + 1;
                HSSFSheet sheet = wb.createSheet("risk control" + page);
                baris = 0;
            }

            //bikin header
            HSSFRow row0 = wb.getSheet("risk control" + page).createRow(0);

            row0.createCell(0).setCellValue("period_start");
            row0.createCell(1).setCellValue("period_end");
            row0.createCell(2).setCellValue("policy_date");
            row0.createCell(3).setCellValue("status");
            row0.createCell(4).setCellValue("pol_no");
            row0.createCell(5).setCellValue("cust_name");
            row0.createCell(6).setCellValue("norut");
            row0.createCell(7).setCellValue("ref1");
            row0.createCell(8).setCellValue("ref2");
            row0.createCell(9).setCellValue("ref3");
            row0.createCell(10).setCellValue("ref4");
            row0.createCell(11).setCellValue("ref5");
            row0.createCell(12).setCellValue("ref6");
            row0.createCell(13).setCellValue("ref7");
            row0.createCell(14).setCellValue("ref8");
            row0.createCell(15).setCellValue("kode_pos");
            row0.createCell(16).setCellValue("ins_risk_cat_code");
            row0.createCell(17).setCellValue("ccy");
            row0.createCell(18).setCellValue("kurs");
            row0.createCell(19).setCellValue("insured_amount");
            row0.createCell(20).setCellValue("premi_total");
            row0.createCell(21).setCellValue("insured_amount_e");
            row0.createCell(22).setCellValue("premi_total_e");
            row0.createCell(23).setCellValue("premi_total_d");
            row0.createCell(24).setCellValue("rate_method_desc");
            row0.createCell(25).setCellValue("premi_rate");
            row0.createCell(26).setCellValue("rate_method");
            row0.createCell(27).setCellValue("rate_cover");
            row0.createCell(28).setCellValue("building");
            row0.createCell(29).setCellValue("machine");
            row0.createCell(30).setCellValue("stock");
            row0.createCell(31).setCellValue("other");
            row0.createCell(32).setCellValue("tsi_or");
            row0.createCell(33).setCellValue("premi_or");
            row0.createCell(34).setCellValue("komisi_or");
            row0.createCell(35).setCellValue("tsi_bppdan");
            row0.createCell(36).setCellValue("premi_bppdan");
            row0.createCell(37).setCellValue("komisi_bppdan");
            row0.createCell(38).setCellValue("tsi_spl");
            row0.createCell(39).setCellValue("premi_spl");
            row0.createCell(40).setCellValue("komisi_spl");
            row0.createCell(41).setCellValue("tsi_fac");
            row0.createCell(42).setCellValue("premi_fac");
            row0.createCell(43).setCellValue("komisi_fac");
            row0.createCell(44).setCellValue("tsi_qs");
            row0.createCell(45).setCellValue("premi_qs");
            row0.createCell(46).setCellValue("komisi_qs");
            row0.createCell(47).setCellValue("tsi_park");
            row0.createCell(48).setCellValue("premi_park");
            row0.createCell(49).setCellValue("komisi_park");
            row0.createCell(50).setCellValue("tsi_faco");
            row0.createCell(51).setCellValue("premi_faco");
            row0.createCell(52).setCellValue("komisi_faco");
            row0.createCell(53).setCellValue("tsi_faco1");
            row0.createCell(54).setCellValue("premi_faco1");
            row0.createCell(55).setCellValue("komisi_faco1");
            row0.createCell(56).setCellValue("tsi_faco2");
            row0.createCell(57).setCellValue("premi_faco2");
            row0.createCell(58).setCellValue("komisi_faco2");
            row0.createCell(59).setCellValue("tsi_faco3");
            row0.createCell(60).setCellValue("premi_faco3");
            row0.createCell(61).setCellValue("komisi_faco3");
            row0.createCell(62).setCellValue("tsi_jp");
            row0.createCell(63).setCellValue("premi_jp");
            row0.createCell(64).setCellValue("komisi_jp");
            row0.createCell(65).setCellValue("cover_type_code");
            row0.createCell(66).setCellValue("member");
            row0.createCell(67).setCellValue("ref1d");
            row0.createCell(68).setCellValue("ref2d");
            row0.createCell(69).setCellValue("ref3d");
            row0.createCell(70).setCellValue("ref4d");
            row0.createCell(71).setCellValue("ref5d");
            row0.createCell(72).setCellValue("ref6d");
            row0.createCell(73).setCellValue("ref7d");
            row0.createCell(74).setCellValue("ref8d");
            row0.createCell(75).setCellValue("ref10");
            row0.createCell(76).setCellValue("ref11");
            row0.createCell(77).setCellValue("risk_class");
            row0.createCell(78).setCellValue("refd1");
            row0.createCell(79).setCellValue("refd2");
            row0.createCell(80).setCellValue("refd3");
            row0.createCell(81).setCellValue("jangka waktu");
            row0.createCell(82).setCellValue("endorse notes");
            row0.createCell(83).setCellValue("no urut debitur");
            row0.createCell(84).setCellValue("entity_id");
            row0.createCell(85).setCellValue("entity_name");
            row0.createCell(86).setCellValue("total_komisi");
            row0.createCell(87).setCellValue("total_komisi_pct");
            //row0.createCell(81).setCellValue("r/i date start");
            //row0.createCell(82).setCellValue("r/i date end");

            norut++;

            if (i > 0) {
                HashDTO s = (HashDTO) list.get(i - 1);
                String nopol = s.getFieldValueByFieldNameST("pol_no");
                String nopol2 = h.getFieldValueByFieldNameST("pol_no");
                if (!nopol.equalsIgnoreCase(nopol2)) {

                    norut = 1;
                }
            }

            //bikin isi cell
            HSSFRow row = wb.getSheet("risk control" + page).createRow(baris + 1);

            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(6).setCellValue(String.valueOf(norut));
            if (h.getFieldValueByFieldNameST("ref1") != null) {
                row.createCell(7).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref1")));
            }
            if (h.getFieldValueByFieldNameST("ref2") != null) {
                row.createCell(8).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2")));
            }
            if (h.getFieldValueByFieldNameST("ref3") != null) {
                row.createCell(9).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref3")));
            }
            if (h.getFieldValueByFieldNameST("ref4") != null) {
                row.createCell(10).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref4")));
            }
            if (h.getFieldValueByFieldNameST("ref5") != null) {
                row.createCell(11).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref5")));
            }
            if (h.getFieldValueByFieldNameST("ref6") != null) {
                row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref6")));
            }
            if (h.getFieldValueByFieldNameST("ref7") != null) {
                row.createCell(13).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref7")));
            }
            if (h.getFieldValueByFieldNameST("ref8") != null) {
                row.createCell(14).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref8")));
            }
            if (h.getFieldValueByFieldNameST("kode_pos") != null) {
                row.createCell(15).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kode_pos")));
            }
            if (h.getFieldValueByFieldNameST("ins_risk_cat_code") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ins_risk_cat_code"));
            }
            if (h.getFieldValueByFieldNameST("ccy") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            }
            if (h.getFieldValueByFieldNameBD("ccy_rate") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount_e") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("insured_amount_e").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_e") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("premi_total_e").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_d") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("premi_total_d").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_method_desc") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("rate_method_desc").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_rate") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_method") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("rate_method").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_cover") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("rate_cover").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("building") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("machine") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("stock") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("other") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_or") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("tsi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_or") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("premi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_or") != null) {
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("komisi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_bppdan") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("tsi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_bppdan") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("premi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_bppdan") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("komisi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_spl") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_spl") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_spl") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_fac") != null) {
                row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_fac") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_fac") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_qs") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_qs") != null) {
                row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("premi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_qs") != null) {
                row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_park") != null) {
                row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("tsi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_park") != null) {
                row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("premi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_park") != null) {
                row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("komisi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco") != null) {
                row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco") != null) {
                row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco") != null) {
                row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco1") != null) {
                row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco1") != null) {
                row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("premi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco1") != null) {
                row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco2") != null) {
                row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco2") != null) {
                row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("premi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco2") != null) {
                row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco3") != null) {
                row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco3") != null) {
                row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("premi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco3") != null) {
                row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_jp") != null) {
                row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("tsi_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_jp") != null) {
                row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("premi_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_jp") != null) {
                row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("komisi_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("cover_type_code") != null) {
                row.createCell(65).setCellValue(h.getFieldValueByFieldNameST("cover_type_code"));
            }
            if (h.getFieldValueByFieldNameBD("member") != null) {
                row.createCell(66).setCellValue(h.getFieldValueByFieldNameBD("member").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ref1d") != null) {
                row.createCell(67).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref1d")));
            }
            if (h.getFieldValueByFieldNameST("ref2d") != null) {
                row.createCell(68).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2d")));
            }
            if (h.getFieldValueByFieldNameST("ref3d") != null) {
                row.createCell(69).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref3d")));
            }
            if (h.getFieldValueByFieldNameST("ref4d") != null) {
                row.createCell(70).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref4d")));
            }
            if (h.getFieldValueByFieldNameST("ref5d") != null) {
                row.createCell(71).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref5d")));
            }
            if (h.getFieldValueByFieldNameST("ref6d") != null) {
                row.createCell(72).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref6d")));
            }
            if (h.getFieldValueByFieldNameST("ref7d") != null) {
                row.createCell(73).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref7d")));
            }
            if (h.getFieldValueByFieldNameST("ref8d") != null) {
                row.createCell(74).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref8d")));
            }
            if (h.getFieldValueByFieldNameST("ref10") != null) {
                row.createCell(75).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref10")));
            }
            if (h.getFieldValueByFieldNameST("ref11") != null) {
                row.createCell(76).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref11")));
            }
            if (h.getFieldValueByFieldNameST("risk_class") != null) {
                row.createCell(77).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("risk_class")));
            }
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row.createCell(78).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
            }
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(79).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(80).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
            row.createCell(81).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(82).setCellValue(h.getFieldValueByFieldNameST("endorse_notes"));
            //row.createCell(79).setCellValue("note");
            if (h.getFieldValueByFieldNameBD("order_no") != null) {
                row.createCell(83).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            }

            row.createCell(84).setCellValue(h.getFieldValueByFieldNameST("entity_id"));
            row.createCell(85).setCellValue(h.getFieldValueByFieldNameST("ent_name"));

            if (h.getFieldValueByFieldNameBD("total_komisi") != null) {
                row.createCell(86).setCellValue(h.getFieldValueByFieldNameBD("total_komisi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("total_komisi_pct") != null) {
                row.createCell(87).setCellValue(h.getFieldValueByFieldNameBD("total_komisi_pct").doubleValue());
            }

            baris = baris + 1;

        }


        SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName().trim() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public EntityView getCoinsurer() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stCoinsurerID);

        return entity;
    }

    public EntityView getEntity() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return entity;
    }

    public EntityView getMarketer() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stMarketerID);

        return entity;
    }

    public InsurancePolicyTypeView getPolicyType(String stPolicyTypeID) {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
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

    public String getStObject() {
        return stObject;
    }

    public void setStObject(String stObject) {
        this.stObject = stObject;
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return costcenter;
    }

    public InsurancePolicyView getPolicy(String stParentID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
    }

    public DTOList EXCEL_LOSS_PROFILE_RI_PAKREASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.lvl,a.ref4,a.ref5,"
                + " coalesce(count(b.pol_no),0) as jumlah,"
                + " coalesce(sum(b.premi_or)-sum(premi_koas),0) as premi_total ");

        String sql = "left join ( "
                + " select pol_no,insured_amount,"
                + " round(((premi_or*share_pct)/100),0) as premi_or,round(((premi_koas*share_pct)/100),0) as premi_koas "
                + " from (select a.pol_id,a.cc_code,a.pol_no,"
                + " getpositive(c.insured_amount < 0,c.insured_amount*-1,c.insured_amount) as insured_amount,"
                + " (c.refn6) as premi_or,(c.refn2) as premi_koas, "
                + " (select d.share_pct from ins_pol_coins d where d.policy_id = a.pol_id and d.entity_id = 1 and d.coins_type= 'COINS') as share_pct "
                + " from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id and c.refn6 <> 0 "
                + " where a.status in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag='Y' "
                + " and a.ins_policy_type_grp_id = 9 and a.pol_type_id = 21 ";

        if (getPolicyDateFrom() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) >= ?";
            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) <= ?";
            sqa.addPar(policyDateTo);
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',c.refd2) >= ?";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',c.refd2) <= ?";
            sqa.addPar(periodTo);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = ?";
            sqa.addPar(stBranch);
        }

        sqa.addQuery(" from band_level a " + sql + " ) x ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        sqa.addClause(" a.pol_type_grp_id = 9 ");
        sqa.addClause(" a.pol_type_id = 21 ");
        sqa.addClause(" a.group_desc = 'RISK_LOSS_PROFILE' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LOSS_PROFILE_RI_PAKREASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF PERSONAL ACCIDENT INSURANCE (PA KREASI)");

            //bikin header
            XSSFRow row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("PA KREASI");

            //bikin header
            XSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("POLICY DATE : " + DateUtil.getDateStr(getPolicyDateFrom()) + " TO " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row3 = sheet.createRow(3);
            if (getPeriodFrom() != null) {
                row3.createCell(0).setCellValue("UNDERWRITING YEAR : " + DateUtil.getYear(getPeriodFrom()));
            }

            //bikin header
            XSSFRow row4 = sheet.createRow(4);
            if (getStBranch() != null) {
                row4.createCell(0).setCellValue("BRANCH : " + getStBranch());
            }

            //bikin header
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("SUM INSURED");
            row5.createCell(1).setCellValue("PREMIUM");

            //bikin header
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("RISK BASIS");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_LOSS_PROFILE_RI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.lvl,a.ref4,a.ref5,"
                + " coalesce(count(b.pol_no),0) as jumlah,coalesce(sum(b.premi_total),0) as premi_total");

        String sql = "left join ( "
                + " select a.pol_no,"
                + " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate),a.insured_amount*a.ccy_rate)) as insured_amount,"
                + " sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) as premi_total"
                + " from ins_policy a"
                + " inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + " where a.status in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag='Y'"
                + " and (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') ";

        if (getPolicyDateFrom() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) >= ?";
            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) <= ?";
            sqa.addPar(policyDateTo);
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= ?";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ?";
            sqa.addPar(periodTo);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = ?";
            sqa.addPar(stBranch);
        }

        if (getStPolicyTypeGroupID() != null) {
            sql = sql + " and a.ins_policy_type_grp_id = ?";
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            sql = sql + " and a.pol_type_id = ?";
            sqa.addPar(stPolicyTypeID);
        }

        sqa.addQuery(" from band_level a " + sql + " group by pol_no "
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.enabled = 'Y'");
        }

        sqa.addClause(" a.group_desc = 'RISK_LOSS_PROFILE' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LOSS_PROFILE_RI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStPolicyTypeGroupDesc().toUpperCase() + " INSURANCE");

            //bikin header
            XSSFRow row1 = sheet.createRow(1);
            if (getStPolicyTypeDesc() != null) {
                row1.createCell(0).setCellValue(getStPolicyTypeDesc().toUpperCase());
            }

            //bikin header
            XSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("POLICY DATE : " + DateUtil.getDateStr(getPolicyDateFrom()) + " TO " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row3 = sheet.createRow(3);
            if (getPeriodFrom() != null) {
                row3.createCell(0).setCellValue("UNDERWRITING YEAR : " + DateUtil.getYear(getPeriodFrom()));
            }

            //bikin header
            XSSFRow row4 = sheet.createRow(4);
            if (getStBranch() != null) {
                row4.createCell(0).setCellValue("BRANCH : " + getStBranch());
            }

            //bikin header
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("SUM INSURED");
            row5.createCell(1).setCellValue("PREMIUM");

            //bikin header
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("RISK BASIS");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_LOSS_PROFILE_PROD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.lvl,a.ref4,a.ref5,"
                + " coalesce(count(b.pol_no),0) as jumlah,coalesce(sum(b.premi_total)-sum(disc),0) as premi_total ");

        String sql = "left join ( "
                + " select e.ins_policy_type_grp_id2,a.pol_no,"
                + " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate),a.insured_amount*a.ccy_rate)) as insured_amount,"
                + " sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) as premi_total, "
                + " sum(getpremiend(d.entity_id,((coalesce(a.nd_disc1*a.ccy_rate,0)) + (coalesce(a.nd_disc2*a.ccy_rate,0))),(coalesce(d.disc_amount*a.ccy_rate,0))*-1)) as disc "
                + " from ins_policy a"
                + " inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + " left join ins_policy_types e on e.pol_type_id = a.pol_type_id "
                + " where a.status in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag='Y'"
                + " and (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') ";

        if (getPolicyDateFrom() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) >= ?";
            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            sql = sql + " and date_trunc('day',a.policy_date) <= ?";
            sqa.addPar(policyDateTo);
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= ?";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ?";
            sqa.addPar(periodTo);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = ?";
            sqa.addPar(stBranch);
        }

        if (getStGroupID() != null) {
            sql = sql + " and e.ins_policy_type_grp_id2 = ?";
            sqa.addPar(stGroupID);
        }

        sqa.addQuery(" from band_level a " + sql + " group by e.ins_policy_type_grp_id2,pol_no "
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        if (stGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stGroupID);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.enabled = 'Y'");
        }

        sqa.addClause(" a.group_desc = 'PEFINDO' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LOSS_PROFILE_PROD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStGroupName().toUpperCase() + " INSURANCE");

            //bikin header
            XSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("POLICY DATE : " + DateUtil.getDateStr(getPolicyDateFrom()) + " TO " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row3 = sheet.createRow(3);
            if (getPeriodFrom() != null) {
                row3.createCell(0).setCellValue("UNDERWRITING YEAR : " + DateUtil.getYear(getPeriodFrom()));
            }

            //bikin header
            XSSFRow row4 = sheet.createRow(4);
            if (getStBranch() != null) {
                row4.createCell(0).setCellValue("BRANCH : " + getStBranch());
            }

            //bikin header
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("SUM INSURED");
            row5.createCell(1).setCellValue("PREMIUM");

            //bikin header
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("RISK BASIS");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public String getStIndex() {
        return stIndex;
    }

    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
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

    public Date getReinsDateFrom() {
        return ReinsDateFrom;
    }

    public void setReinsDateFrom(Date ReinsDateFrom) {
        this.ReinsDateFrom = ReinsDateFrom;
    }

    public Date getReinsDateTo() {
        return ReinsDateTo;
    }

    public void setReinsDateTo(Date ReinsDateTo) {
        this.ReinsDateTo = ReinsDateTo;
    }

    public String getStRISlip() {
        return stRISlip;
    }

    public void setStRISlip(String stRISlip) {
        this.stRISlip = stRISlip;
    }

    public DTOList EXCEL_BPPDAN() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.order_no,getperiod(a.pol_type_id in (1,81),b.refd1,a.period_start) period_start_det,getperiod(a.pol_type_id in (1,81),b.refd2,a.period_end) as period_end_det,"
                + " a.policy_date,a.period_start,a.period_end,i.valid_ri_date,a.endorse_notes,a.endorse_mode,a.status,"
                + " (substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,"
                + " a.pol_no,a.pol_type_id::text,a.cust_name,b.ins_pol_obj_id,getname(a.ins_policy_type_grp_id=1,b.ref5,b.ref8) as alamat,b.ref9d as kode_pos,b.risk_class,"
                + " f.ins_risk_cat_code,a.ccy,a.ccy_rate,b.premi_total as premi_total_d, "
                + " (	select round(sum(z.rate),5) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "

                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (100,109),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as bi, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107,100,109),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "

                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (90,91,92,93,94)) as rate_rsmd41, "
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (21,22,207,213)) as rate_equake42, "
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (29)) as rate_flood43,"

                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (27,239,269)) as rate_fire,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (21,22,207,213,324)) as rate_eqvet,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (29,274)) as rate_tsfwd,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (270)) as rate_rsmd,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (249,272)) as rate_rsmdcc,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (243,278)) as rate_bi,"
                + "(   select round(sum(z.rate),5)  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id and z.ins_cover_id in (253,279)) as rate_other,"

                + " sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)) as premi_bppdan, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)) as komisi_bppdan, "
                + " checkpremi(a.cover_type_code= 'COINSIN',(select y.share_pct  from ins_policy x "
                + " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 "
                + " where x.pol_id = a.pol_id),a.share_pct) as member,i.notes,k.user_name");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join s_users k on k.user_id = a.ri_approved_who ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type = 'BPDAN'");
        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.pol_type_id in (1,19,81) ");

        /*
        if (policyDateFrom!=null&&ReinsDateFrom!=null) {
        sqa.addClause("((date_trunc('day',a.policy_date) >= ? and date_trunc('day',a.policy_date) <= ?) "+
        "or (date_trunc('day',i.valid_ri_date) >= ? and date_trunc('day',i.valid_ri_date) <= ?))");
        sqa.addPar(policyDateFrom);
        sqa.addPar(policyDateTo);
        sqa.addPar(ReinsDateFrom);
        sqa.addPar(ReinsDateTo);
        }
         */

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (ReinsDateFrom != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) >= ?");
            sqa.addPar(ReinsDateFrom);
        }

        if (ReinsDateTo != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) <= ?");
            sqa.addPar(ReinsDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        String sql = "select * from ( " + sqa.getSQL() + " group by a.endorse_mode,a.endorse_notes,a.policy_date,b.order_no,a.pol_id,a.pol_no,a.cust_name,a.period_start,a.period_end,b.ref5,f.ins_risk_cat_code,a.status, "
                + " a.ccy,a.ccy_rate,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.risk_class,i.valid_ri_date,b.refd1,b.refd2,a.pol_type_id,b.ref9d,a.ins_policy_type_grp_id,b.ref8,i.notes,k.user_name "
                + " ) x ";

        if (getStTime() != null) {
            if (getStTime().equalsIgnoreCase("1")) {
                sql = sql + " where years > '1' ";
            } else if (getStTime().equalsIgnoreCase("2")) {
                sql = sql + " where years <= '1' ";
            }
        }

        sql = sql + "order by pol_no,ins_pol_obj_id,valid_ri_date ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BPPDAN() throws Exception {

        //XSSFWorkbook wb = new XSSFWorkbook();
        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        //XSSFSheet sheet = wb.createSheet("new sheet");
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        Date Valid;
        Date periodDet;
        Date periodEnd;
        int yearRIDate;
        int yearPeriodDetDate;
        int Days;
        int Years = 365;

        int tahun;

        DateTime ValidRIDate = new DateTime();
        DateTime periodDetailStart = new DateTime();
        DateTime periodDetailEnd = new DateTime();
        Date periodDetailStart2;
        Date periodDetailEnd2;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            //XSSFRow row0 = sheet.createRow(0);
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("no");
            row0.createCell(1).setCellValue("norut");
            row0.createCell(2).setCellValue("pol_no");
            row0.createCell(3).setCellValue("u/year");
            row0.createCell(4).setCellValue("status");
            row0.createCell(5).setCellValue("nama");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("period_start");
            row0.createCell(8).setCellValue("period_end");
            row0.createCell(9).setCellValue("periode_start Detail");
            row0.createCell(10).setCellValue("period_end Detail");
            row0.createCell(11).setCellValue("RI date_start");
            row0.createCell(12).setCellValue("risk_class");
            row0.createCell(13).setCellValue("ins_risk_cat_code");
            row0.createCell(14).setCellValue("rate_cover");
            row0.createCell(15).setCellValue("alamat");
            row0.createCell(16).setCellValue("kode_pos");
            row0.createCell(17).setCellValue("ccy");
            row0.createCell(18).setCellValue("ccy_rate");
            row0.createCell(19).setCellValue("premi_total_d");
            row0.createCell(20).setCellValue("building");
            row0.createCell(21).setCellValue("machine");
            row0.createCell(22).setCellValue("stock");
            row0.createCell(23).setCellValue("bi");
            row0.createCell(24).setCellValue("other");
            row0.createCell(25).setCellValue("rate 4.1AAA");
            row0.createCell(26).setCellValue("rate 4.2");
            row0.createCell(27).setCellValue("rate 4.3");
            row0.createCell(28).setCellValue("rate 4.4");
            row0.createCell(29).setCellValue("rate 4.8");
            row0.createCell(30).setCellValue("rate 4.10");
            row0.createCell(31).setCellValue("rate fire");
            row0.createCell(32).setCellValue("rate eqvet");
            row0.createCell(33).setCellValue("rate tsfwd");
            row0.createCell(34).setCellValue("rate rsmd");
            row0.createCell(35).setCellValue("rate rsmdcc");
            row0.createCell(36).setCellValue("rate bi");
            row0.createCell(37).setCellValue("rate other");
            row0.createCell(38).setCellValue("premi_bppdan");
            row0.createCell(39).setCellValue("komisi_bppdan");
            row0.createCell(40).setCellValue("Jenis Polis");
            row0.createCell(41).setCellValue("Endorse Notes");
            row0.createCell(42).setCellValue("Member");
            row0.createCell(43).setCellValue("Catatan");
            row0.createCell(44).setCellValue("Approved RI");

            Valid = DateUtil.truncDate(h.getFieldValueByFieldNameDT("valid_ri_date"));
            periodDet = DateUtil.truncDate(h.getFieldValueByFieldNameDT("period_start_det"));
            periodEnd = DateUtil.truncDate(h.getFieldValueByFieldNameDT("period_end_det"));

            yearRIDate = Integer.parseInt(DateUtil.getYear(Valid)); //2013
            yearPeriodDetDate = yearRIDate - 1; //2012
/*
            if (Tools.compare(DateUtil.getYear(periodDet), DateUtil.getYear(Valid)) < 0) {
            if (Tools.compare(DateUtil.getMonth(periodDet), DateUtil.getMonth(Valid)) != 0) {
            periodDetailStart = new DateTime(DateUtil.getDate(
            DateUtil.getDays2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + DateUtil.getMonth2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + yearPeriodDetDate));
            } else if (Tools.compare(DateUtil.getMonth(periodDet), DateUtil.getMonth(Valid)) == 0) {
            periodDetailStart = new DateTime(DateUtil.getDate(
            DateUtil.getDays2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + DateUtil.getMonth2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + yearRIDate));
            }
            } else {
            periodDetailStart = new DateTime(DateUtil.getDate(
            DateUtil.getDays2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + DateUtil.getMonth2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
            + yearRIDate));
            }
             *
             */

            periodDetailStart = new DateTime(DateUtil.getDate(
                    DateUtil.getDays2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
                    + DateUtil.getMonth2Digit(h.getFieldValueByFieldNameDT("period_start_det")) + "/"
                    + yearRIDate));

            Days = DateUtil.dateDiff(h.getFieldValueByFieldNameDT("period_start_det"), h.getFieldValueByFieldNameDT("period_end_det"));
            tahun = Integer.parseInt(DateUtil.getYear(h.getFieldValueByFieldNameDT("period_end_det"))) - Integer.parseInt(DateUtil.getYear(h.getFieldValueByFieldNameDT("period_start_det")));

            //logger.logDebug("$$$$$$$$$$$$$$$$$ : "+ Days);
            //logger.logDebug("################# : "+ tahun);

            if (Tools.compare(new BigDecimal(Days), new BigDecimal(365)) < 0) {
                periodDetailEnd = new DateTime(periodEnd);
            } else {
                periodDetailEnd = periodDetailStart.plusYears(1);
            }

            //periodDetailStart2 = periodDetailStart.toDate();
            //periodDetailEnd2 = periodDetailEnd.toDate();

            if (Tools.compare(new BigDecimal(tahun), new BigDecimal(1)) > 0) {
                periodDetailStart2 = periodDetailStart.toDate();
                periodDetailEnd2 = periodDetailEnd.toDate();
            } else {
                periodDetailStart2 = h.getFieldValueByFieldNameDT("period_start_det");
                periodDetailEnd2 = h.getFieldValueByFieldNameDT("period_end_det");
            }

            //bikin isi cell
            //XSSFRow row = sheet.createRow(i + 1);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(String.valueOf(i + 1));
            if (h.getFieldValueByFieldNameBD("order_no") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            }
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            if (h.getFieldValueByFieldNameDT("valid_ri_date") != null) {
                row.createCell(3).setCellValue(DateUtil.getYear(periodDetailStart2));
            }
//            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no").substring(17, 18));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(5).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_start_det"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("period_end_det"));

            if (h.getFieldValueByFieldNameDT("period_start_det") != null) {
                row.createCell(9).setCellValue(periodDetailStart2);
            }

            if (h.getFieldValueByFieldNameDT("period_start_det") != null) {
                row.createCell(10).setCellValue(periodDetailEnd2);
            }

            if (h.getFieldValueByFieldNameDT("valid_ri_date") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("valid_ri_date"));
            }

            if (h.getFieldValueByFieldNameST("risk_class") != null) {
                row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("risk_class")));
            }
            if (h.getFieldValueByFieldNameST("ins_risk_cat_code") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("ins_risk_cat_code"));
            }
            if (h.getFieldValueByFieldNameBD("rate_cover") != null) {
                if (h.getFieldValueByFieldNameBD("rate_equake42") != null) {
                    row.createCell(14).setCellValue(BDUtil.zero.doubleValue());
                } else {
                    row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("rate_cover").doubleValue());
                }
            }
            if (h.getFieldValueByFieldNameST("alamat") != null) {
                row.createCell(15).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("alamat")));
            }
            if (h.getFieldValueByFieldNameST("kode_pos") != null) {
                row.createCell(16).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kode_pos")));
            }
            if (h.getFieldValueByFieldNameST("ccy") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            }
            if (h.getFieldValueByFieldNameBD("ccy_rate") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_d") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_total_d").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("building") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("machine") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("stock") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("bi") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("bi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("other") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_rsmd41") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("rate_rsmd41").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_equake42") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("rate_equake42").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_flood43") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("rate_flood43").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_fire") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("rate_fire").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_eqvet") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("rate_eqvet").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_tsfwd") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("rate_tsfwd").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_rsmd") != null) {
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("rate_rsmd").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_rsmdcc") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("rate_rsmdcc").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_bi") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("rate_bi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_other") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("rate_other").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_bppdan") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("premi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_bppdan") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("komisi_bppdan").doubleValue());
            }
            row.createCell(40).setCellValue(LanguageManager.getInstance().translate(getPolicyType(h.getFieldValueByFieldNameST("pol_type_id")).getStShortDescription()));
            if (h.getFieldValueByFieldNameST("endorse_notes") != null) {
                row.createCell(41).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("endorse_notes")));
            }
            if (h.getFieldValueByFieldNameBD("member") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("member").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("notes") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("notes"));
            }
            if (h.getFieldValueByFieldNameST("user_name") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameST("user_name"));
            }

        }

        String fileName = getStFileName() + "_" + System.currentTimeMillis();

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + fileName + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

        /*
        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
         */

    }

    public void EXCEL_CASH_MANAGEMENT() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	f.description as cob,a.period_start,a.period_end,a.policy_date,';'||a.pol_no as policy_no,"
                + "a.cust_name as the_insured,c.order_no as nr,c.ref1 as from,c.ref2 as to,a.ccy as curr,a.ccy_rate as rate,"
                //+ "c.insured_amount as insured,(c.insured_amount*0.5) as tsi_ri_OR, "
                + "c.insured_amount as insured_perobjek,"
                + "round(sum(checkreas(j.treaty_type='OR',h.base_tsi_amount)),2) as insured, "
                + "round(sum(checkreas(j.treaty_type='OR',h.tsi_amount)),2) as tsi_ri_OR, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2) as tsiri_limit_I, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount)),2) as tsiri_limit_II, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2) as premiri_limit_I, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount)),2) as premiri_limit_II, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2) as commri_limit_I, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt)),2) as commri_limit_II,c.refd1 as dated ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_pol_cover e on e.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_cover f on f.ins_cover_id = e.ins_cover_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type in ('OR','FACO','FACO3')");
        sqa.addClause(" a.effective_flag='Y' ");
        sqa.addClause(" a.ins_policy_type_grp_id = 15");

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

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stMarketerID != null) {
            sqa.addClause("k.ent_id in ('1','" + stMarketerID + "')");
            //sqa.addPar(stMarketerID);
        }

        String sql = "select * from ( " + sqa.getSQL() + " group by f.description,a.period_start,a.period_end,a.policy_date,a.pol_no,a.cust_name,c.order_no,"
                + "c.ref1,c.ref2,a.ccy,a.ccy_rate,c.refd1,c.insured_amount ";

        sql = sql + " order by a.pol_no,c.order_no ) a where tsiri_limit_I <> 0 "
                + " order by a.policy_no,a.nr ";

        SQLUtil S = new SQLUtil();

        String nama_file = "cm_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */
    }

    public void EXPORT_CASH_MANAGEMENT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            if (getStMarketerName() != null) {
                row1.createCell(0).setCellValue("POLICY DATE : " + DateUtil.getDateStr(getPolicyDateFrom()) + " TO " + DateUtil.getDateStr(getPolicyDateTo()));
            }

            XSSFRow row2 = sheet.createRow(1);
            if (getStMarketerName() != null) {
                row2.createCell(0).setCellValue(getStMarketerName());
            }

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("class of bussiness");
            row0.createCell(1).setCellValue("period_start");
            row0.createCell(2).setCellValue("period_end");
            row0.createCell(3).setCellValue("policy no");
            row0.createCell(4).setCellValue("the insured");
            row0.createCell(5).setCellValue("nr.");
            row0.createCell(6).setCellValue("From");
            row0.createCell(7).setCellValue("To");
            row0.createCell(8).setCellValue("curr");
            row0.createCell(9).setCellValue("rate");
            row0.createCell(10).setCellValue("insured");
            row0.createCell(11).setCellValue("tsi_r/i OR");
            row0.createCell(12).setCellValue("tsi_r/i limit I");
            row0.createCell(13).setCellValue("tsi_r/i limit II");
            row0.createCell(14).setCellValue("premi_r/i limit I");
            row0.createCell(15).setCellValue("premi_r/i limit II");
            row0.createCell(16).setCellValue("comm_r/i limit I");
            row0.createCell(17).setCellValue("comm_r/i limit II");
            row0.createCell(18).setCellValue("TSI Objek");
            row0.createCell(19).setCellValue("dated");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(6).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref1")));
            row.createCell(7).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2")));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            if (h.getFieldValueByFieldNameBD("tsi_or") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco2") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco2") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("premi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco2") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void onChangeBranchGroup() {
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

    public boolean isCanNavigateRegion() {
        return canNavigateRegion;
    }

    public void setCanNavigateRegion(boolean canNavigateRegion) {
        this.canNavigateRegion = canNavigateRegion;
    }

    public String getStRegionName() {
        return stRegionName;
    }

    public void setStRegionName(String stRegionName) {
        this.stRegionName = stRegionName;
    }

    public DTOList EXCEL_RISK_CONTROL_COVER_OLD() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "coalesce(b.refd1,a.period_start) period_start,coalesce(b.refd2,a.period_end) as period_end,a.policy_date,"
                + "a.create_who,(substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,"
                + "a.pol_type_id,b.ins_pol_obj_id,i.ins_pol_treaty_id,a.status,a.pol_id,a.pol_no,k.ent_name as cust_name,b.ref1,b.ref2,b.ref22,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref11,"
                + "b.ref8,b.ref9d as kode_pos,b.ref10,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.ref11,b.risk_class,"
                + "b.refd1,b.refd2,b.refd3,b.refd5,f.ins_risk_cat_code, a.ccy,a.insured_amount,a.premi_total,b.insured_amount as insured_amount_e,"
                + "b.premi_total_bcoins as premi_total_e,b.premi_total as premi_total_d, "
                + "round(((sum(i.premi_amount)/sum(case i.tsi_amount when 0 then 1 else i.tsi_amount end))*100),4) as rate_method_desc, "
                + "round(sum(i.premi_rate),5) as premi_rate,round(a.share_pct,2) as rate_method, "
                + "(   select round(sum(z.rate),5) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount))   "
                + "  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(  select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount))   "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(    select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id    "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id   "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + "(select string_agg(l.description2,'|') "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id "
                //+ "and z.ins_cover_id in (2,22,27,29,62,63,64,65,87,90,91,92,93,94,101,105,107,108,114,120,121,122,123,128,129,130,131,140,141,142,148,210,215,216,217,234,239,243,249,251,253,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1,  "/*
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and z.ins_cover_id in (269,280) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc1, "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (270,281) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc2,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (271,282) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc3, "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (272,283) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc4, "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (273,284) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc5, "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (274,285) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc6,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (275,286) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc7,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (276,287) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc8,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (277,288) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc9,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and l.ins_cover_id in (278,289) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc10,"
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id and z.ins_cover_id in (279,290) "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id group by l.description,z.ins_cover_id) as desc11, "*/
                + "round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2) as tsi_or, "
                + "round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) as premi_or, "
                + "round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt)),2) as komisi_or, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2) as tsi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)),2) as premi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)),2) as komisi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2) as tsi_spl, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.premi_amount)),2) as premi_spl, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)),2) as komisi_spl, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2) as tsi_fac, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2) as premi_fac, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2) as komisi_fac, "
                + "round(sum(checkreas(j.treaty_type='QS',i.tsi_amount)),2) as tsi_qs, "
                + "round(sum(checkreas(j.treaty_type='QS',i.premi_amount)),2) as premi_qs, "
                + "round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt)),2) as komisi_qs, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2) as tsi_park, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.premi_amount)),2) as premi_park, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)),2) as komisi_park,"
                + "round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2) as tsi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2) as premi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2) as komisi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount)),2) as tsi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount)),2) as premi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt)),2) as komisi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount)),2) as tsi_faco2, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount)),2) as premi_faco2, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt)),2) as komisi_faco2,  "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount)),2) as tsi_faco3, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount)),2) as premi_faco3, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt)),2) as komisi_faco3,"
                + "a.cover_type_code ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + " left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " left join ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (periodEndFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end) >= ?");
            sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sqa.addClause("date_trunc('day',a.period_end) <= ?");
            sqa.addPar(periodEndTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        final String sql = sqa.getSQL() + " group by b.refd1,a.period_start,b.refd2,a.period_end,a.policy_date,a.create_who,a.nd_feebase1,a.nd_feebase2,a.nd_comm1,a.nd_comm2,"
                + "a.pol_type_id,b.ins_pol_obj_id,i.ins_pol_treaty_id,a.status,a.pol_id,a.pol_no,k.ent_name,a.cust_name,b.ref1,b.ref2,b.ref22,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref11,b.ref8,"
                + "b.ref9d,b.ref10,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.ref11,b.risk_class,b.refd1,b.refd2,b.refd3,b.refd5,f.ins_risk_cat_code, "
                + "a.ccy,a.insured_amount,a.premi_total,b.insured_amount,b.premi_total_bcoins,b.premi_total,a.share_pct,a.cover_type_code "
                + "order by a.pol_type_id,a.pol_no,b.ins_pol_obj_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_RISK_CONTROL_COVER() throws Exception {
        final boolean NON_EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("NON_EFFECTIVE"));
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.period_start,a.period_end,a.policy_date,a.status,';'||a.pol_no as pol_no,a.cust_name,b.order_no as norut,"
                + "b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9d as kode_pos,f.ins_risk_cat_code,a.ccy,a.ccy_rate as kurs,"
                + "a.insured_amount,a.premi_total,b.insured_amount as insured_amount_e,b.premi_total_bcoins as premi_total_e,b.premi_total as premi_total_d,"
                + "round(sum(i.premi_rate),5) as premi_rate,round(a.share_pct,2) as rate_method, "
                + "(   select round(sum(z.rate),5) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount))   "
                + "  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(  select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount))   "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(    select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + "from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id    "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id   "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + "round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2) as tsi_or, "
                + "round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) as premi_or, "
                + "round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt)),2) as komisi_or, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2) as tsi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)),2) as premi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)),2) as komisi_bppdan, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2) as tsi_spl, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.premi_amount)),2) as premi_spl, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.ricomm_amt)),2) as komisi_spl, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2) as tsi_fac, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2) as premi_fac, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2) as komisi_fac, "
                + "round(sum(checkreas(j.treaty_type='QS',i.tsi_amount)),2) as tsi_qs, "
                + "round(sum(checkreas(j.treaty_type='QS',i.premi_amount)),2) as premi_qs, "
                + "round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt)),2) as komisi_qs, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2) as tsi_park, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.premi_amount)),2) as premi_park, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)),2) as komisi_park,"
                + "round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2) as tsi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2) as premi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2) as komisi_faco, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount)),2) as tsi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount)),2) as premi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt)),2) as komisi_faco1, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.tsi_amount)),2) as tsi_faco2, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.premi_amount)),2) as premi_faco2, "
                + "round(sum(checkreas(j.treaty_type='FACO2',i.ricomm_amt)),2) as komisi_faco2,  "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.tsi_amount)),2) as tsi_faco3, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.premi_amount)),2) as premi_faco3, "
                + "round(sum(checkreas(j.treaty_type='FACO3',i.ricomm_amt)),2) as komisi_faco3,"
                + "a.cover_type_code,b.ref1d,b.ref2d,b.ref3d,b.ref4d,b.ref5d,b.ref6d,b.ref7d,b.ref8d,b.ref10,b.ref11,b.risk_class,"
                + "b.refd1,b.refd2,b.refd3,b.refd5,(substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as jangkawaktu,"
                + "a.endorse_notes,a.pol_id,b.ins_pol_obj_id,"
                + "(select string_agg(l.description2,'|') "
                + "from ins_cover l "
                + "inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id "
                + "where z.ins_pol_obj_id = b.ins_pol_obj_id) as desc1 ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + " left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " left join ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");
        sqa.addClause(" a.active_flag = 'Y'");

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
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        String sql = sqa.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,26,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81 "
                + " order by a.pol_type_id,a.pol_no,b.ins_pol_obj_id ";

        SQLUtil S = new SQLUtil();

        String nama_file = "risk_acc_control_cover_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

    }

    public void EXPORT_RISK_CONTROL_COVER() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        DateTime startDate = new DateTime();
        DateTime endDate = new DateTime();
        Date ValidEndDate;
        int norut = 0;
        int date = 1;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("period_start");
            row0.createCell(1).setCellValue("period_end");
            row0.createCell(2).setCellValue("policy_date");
            row0.createCell(3).setCellValue("status");
            row0.createCell(4).setCellValue("pol_no");
            row0.createCell(5).setCellValue("cust_name");
            row0.createCell(6).setCellValue("norut");
            row0.createCell(7).setCellValue("ref1");
            row0.createCell(8).setCellValue("ref2");
            row0.createCell(9).setCellValue("ref3");
            row0.createCell(10).setCellValue("ref4");
            row0.createCell(11).setCellValue("ref5");
            row0.createCell(12).setCellValue("ref6");
            row0.createCell(13).setCellValue("ref7");
            row0.createCell(14).setCellValue("ref8");
            row0.createCell(15).setCellValue("kode_pos");
            row0.createCell(16).setCellValue("ins_risk_cat_code");
            row0.createCell(17).setCellValue("ccy");
            row0.createCell(18).setCellValue("kurs");
            row0.createCell(19).setCellValue("insured_amount");
            row0.createCell(20).setCellValue("premi_total");
            row0.createCell(21).setCellValue("insured_amount_e");
            row0.createCell(22).setCellValue("premi_total_e");
            row0.createCell(23).setCellValue("premi_total_d");
            row0.createCell(25).setCellValue("premi_rate");
            row0.createCell(26).setCellValue("rate_method");
            row0.createCell(27).setCellValue("rate_cover");
            row0.createCell(28).setCellValue("building");
            row0.createCell(29).setCellValue("machine");
            row0.createCell(30).setCellValue("stock");
            row0.createCell(31).setCellValue("other");
            row0.createCell(32).setCellValue("tsi_or");
            row0.createCell(33).setCellValue("premi_or");
            row0.createCell(34).setCellValue("komisi_or");
            row0.createCell(35).setCellValue("tsi_bppdan");
            row0.createCell(36).setCellValue("premi_bppdan");
            row0.createCell(37).setCellValue("komisi_bppdan");
            row0.createCell(38).setCellValue("tsi_spl");
            row0.createCell(39).setCellValue("premi_spl");
            row0.createCell(40).setCellValue("komisi_spl");
            row0.createCell(41).setCellValue("tsi_fac");
            row0.createCell(42).setCellValue("premi_fac");
            row0.createCell(43).setCellValue("komisi_fac");
            row0.createCell(44).setCellValue("tsi_qs");
            row0.createCell(45).setCellValue("premi_qs");
            row0.createCell(46).setCellValue("komisi_qs");
            row0.createCell(47).setCellValue("tsi_park");
            row0.createCell(48).setCellValue("premi_park");
            row0.createCell(49).setCellValue("komisi_park");
            row0.createCell(50).setCellValue("tsi_faco");
            row0.createCell(51).setCellValue("premi_faco");
            row0.createCell(52).setCellValue("komisi_faco");
            row0.createCell(53).setCellValue("tsi_faco1");
            row0.createCell(54).setCellValue("premi_faco1");
            row0.createCell(55).setCellValue("komisi_faco1");
            row0.createCell(56).setCellValue("tsi_faco2");
            row0.createCell(57).setCellValue("premi_faco2");
            row0.createCell(58).setCellValue("komisi_faco2");
            row0.createCell(59).setCellValue("tsi_faco3");
            row0.createCell(60).setCellValue("premi_faco3");
            row0.createCell(61).setCellValue("komisi_faco3");
            row0.createCell(62).setCellValue("cover_type_code");
            row0.createCell(63).setCellValue("ref1d");
            row0.createCell(64).setCellValue("ref2d");
            row0.createCell(65).setCellValue("ref3d");
            row0.createCell(66).setCellValue("ref4d");
            row0.createCell(67).setCellValue("ref5d");
            row0.createCell(68).setCellValue("ref6d");
            row0.createCell(69).setCellValue("ref7d");
            row0.createCell(70).setCellValue("ref8d");
            row0.createCell(71).setCellValue("ref10");
            row0.createCell(72).setCellValue("ref11");
            row0.createCell(73).setCellValue("risk_class");
            row0.createCell(74).setCellValue("refd1");
            row0.createCell(75).setCellValue("refd2");
            row0.createCell(76).setCellValue("refd3");
            row0.createCell(77).setCellValue("jangka waktu");
            row0.createCell(78).setCellValue("endorse notes");
            row0.createCell(79).setCellValue("no urut debitur");
            /*row0.createCell(81).setCellValue("desc2");
            row0.createCell(82).setCellValue("desc3");
            row0.createCell(83).setCellValue("desc4");
            row0.createCell(84).setCellValue("desc5");
            row0.createCell(85).setCellValue("desc6");
            row0.createCell(86).setCellValue("desc7");
            row0.createCell(87).setCellValue("desc8");
            row0.createCell(88).setCellValue("desc9");
            row0.createCell(89).setCellValue("desc10");
            row0.createCell(90).setCellValue("desc11");*/
            row0.createCell(80).setCellValue("pol_id");
            row0.createCell(81).setCellValue("desc1");
            row0.createCell(82).setCellValue("refd5");

            norut++;

            if (i > 0) {
                HashDTO s = (HashDTO) list.get(i - 1);
                String nopol = s.getFieldValueByFieldNameST("pol_no");
                String nopol2 = h.getFieldValueByFieldNameST("pol_no");
                if (!nopol.equalsIgnoreCase(nopol2)) {

                    norut = 1;
                }
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(6).setCellValue(String.valueOf(norut));
            if (h.getFieldValueByFieldNameST("ref1") != null) {
                row.createCell(7).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref1")));
            }
            if (h.getFieldValueByFieldNameST("ref2") != null) {
                row.createCell(8).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2")));
            }
            if (h.getFieldValueByFieldNameST("ref3") != null) {
                row.createCell(9).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref3")));
            }
            if (h.getFieldValueByFieldNameST("ref4") != null) {
                row.createCell(10).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref4")));
            }
            if (h.getFieldValueByFieldNameST("ref5") != null) {
                row.createCell(11).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref5")));
            }
            if (h.getFieldValueByFieldNameST("ref6") != null) {
                row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref6")));
            }
            if (h.getFieldValueByFieldNameST("ref7") != null) {
                row.createCell(13).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref7")));
            }
            if (h.getFieldValueByFieldNameST("ref8") != null) {
                row.createCell(14).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref8")));
            }
            if (h.getFieldValueByFieldNameST("kode_pos") != null) {
                row.createCell(15).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kode_pos")));
            }
            if (h.getFieldValueByFieldNameST("ins_risk_cat_code") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ins_risk_cat_code"));
            }
            if (h.getFieldValueByFieldNameST("ccy") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            }
            if (h.getFieldValueByFieldNameBD("ccy_rate") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount_e") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("insured_amount_e").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_e") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("premi_total_e").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_d") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("premi_total_d").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_rate") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_method") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("rate_method").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_cover") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("rate_cover").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("building") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("machine") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("stock") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("other") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_or") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("tsi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_or") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("premi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_or") != null) {
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("komisi_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_bppdan") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("tsi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_bppdan") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("premi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_bppdan") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("komisi_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_spl") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_spl") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_spl") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_fac") != null) {
                row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_fac") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_fac") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_qs") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_qs") != null) {
                row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("premi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_qs") != null) {
                row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_park") != null) {
                row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("tsi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_park") != null) {
                row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("premi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_park") != null) {
                row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("komisi_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco") != null) {
                row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco") != null) {
                row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco") != null) {
                row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco1") != null) {
                row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco1") != null) {
                row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("premi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco1") != null) {
                row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco2") != null) {
                row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco2") != null) {
                row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("premi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco2") != null) {
                row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_faco3") != null) {
                row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_faco3") != null) {
                row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("premi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_faco3") != null) {
                row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("cover_type_code") != null) {
                row.createCell(62).setCellValue(h.getFieldValueByFieldNameST("cover_type_code"));
            }
            /*
            if (h.getFieldValueByFieldNameST("ref1d") != null) {
            row.createCell(63).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref1d")));
            }
            if (h.getFieldValueByFieldNameST("ref2d") != null) {
            row.createCell(64).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2d")));
            }
            if (h.getFieldValueByFieldNameST("ref3d") != null) {
            row.createCell(65).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref3d")));
            }
            if (h.getFieldValueByFieldNameST("ref4d") != null) {
            row.createCell(66).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref4d")));
            }
            if (h.getFieldValueByFieldNameST("ref5d") != null) {
            row.createCell(67).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref5d")));
            }
            if (h.getFieldValueByFieldNameST("ref6d") != null) {
            row.createCell(68).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref6d")));
            }
            if (h.getFieldValueByFieldNameST("ref7d") != null) {
            row.createCell(69).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref7d")));
            }
            if (h.getFieldValueByFieldNameST("ref8d") != null) {
            row.createCell(70).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref8d")));
            }
            if (h.getFieldValueByFieldNameST("ref10") != null) {
            row.createCell(71).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref10")));
            }
            if (h.getFieldValueByFieldNameST("ref11") != null) {
            row.createCell(72).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref11")));
            }
            if (h.getFieldValueByFieldNameST("risk_class") != null) {
            row.createCell(73).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("risk_class")));
            }*/
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row.createCell(74).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
            }
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(75).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(76).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
            row.createCell(77).setCellValue(h.getFieldValueByFieldNameST("years"));
            /*if (h.getFieldValueByFieldNameST("endorse_notes") != null) {
            row.createCell(78).setCellValue(h.getFieldValueByFieldNameST("endorse_notes"));
            }*/
            if (h.getFieldValueByFieldNameBD("order_no") != null) {
                row.createCell(79).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            }/*
            if (h.getFieldValueByFieldNameST("desc2") != null) {
            row.createCell(81).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc2")));
            }
            if (h.getFieldValueByFieldNameST("desc3") != null) {
            row.createCell(82).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc3")));
            }
            if (h.getFieldValueByFieldNameST("desc4") != null) {
            row.createCell(83).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc4")));
            }
            if (h.getFieldValueByFieldNameST("desc5") != null) {
            row.createCell(84).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc5")));
            }
            if (h.getFieldValueByFieldNameST("desc6") != null) {
            row.createCell(85).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc6")));
            }
            if (h.getFieldValueByFieldNameST("desc7") != null) {
            row.createCell(86).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc7")));
            }
            if (h.getFieldValueByFieldNameST("desc8") != null) {
            row.createCell(87).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc8")));
            }
            if (h.getFieldValueByFieldNameST("desc9") != null) {
            row.createCell(88).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc9")));
            }
            if (h.getFieldValueByFieldNameST("desc10") != null) {
            row.createCell(89).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc10")));
            }
            if (h.getFieldValueByFieldNameST("desc11") != null) {
            row.createCell(90).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc11")));
            }*/
            row.createCell(80).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            if (h.getFieldValueByFieldNameST("desc1") != null) {
                row.createCell(81).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc1")));
            }
            if (h.getFieldValueByFieldNameDT("refd5") != null) {
                row.createCell(82).setCellValue(h.getFieldValueByFieldNameDT("refd5"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

//        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//        wb.write(outByteStream);
//        byte[] outArray = outByteStream.toByteArray();
//        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        SessionManager.getInstance().getResponse().setContentLength(outArray.length);
//        SessionManager.getInstance().getResponse().setHeader("Expires:", "0"); // eliminates browser caching
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName().trim() + ".xlsx;");
//        OutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();
//        sosStream.write(outArray);
//        sosStream.flush();
//        sosStream.close();

    }

    public DTOList EXCEL_LOSS_PROFILE_RI_FIRE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        int taon = Integer.parseInt(DateUtil.getYear(policyDateFrom));
        int taon1 = taon - 1;
        int taon2 = taon - 2;

        sqa.addSelect("a.lvl,a.ref4,a.ref5, "
                + "   coalesce(sum(b.pol_no)) as jumlah,coalesce(sum(b.premi_total),0) as premi_total , "
                + "   coalesce(sum(b.claim_no),0) as jumlah_claim, coalesce(sum(b.claim_total),0) as claim_total,"
                + "   coalesce(sum(b.pol_no2)) as jumlah2,coalesce(sum(b.premi_total2),0) as premi_total2 , "
                + "   coalesce(sum(b.claim_no2),0) as jumlah_claim2, coalesce(sum(b.claim_total2),0) as claim_total2,"
                + "   coalesce(sum(b.pol_no3)) as jumlah3,coalesce(sum(b.premi_total3),0) as premi_total3 , "
                + "   coalesce(sum(b.claim_no3),0) as jumlah_claim3, coalesce(sum(b.claim_total3),0) as claim_total3");

        String sql = " left join (  select "
                + " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate), a.insured_amount*a.ccy_rate)) as insured_amount,";



        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = " + taon2 + "  then 1 else 0 end  as pol_no , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = " + taon2 + "  then round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) else 0 end as premi_total , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) = " + taon2 + "   then 1 else 0 end as claim_no , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) = " + taon2 + " then a.claim_amount else 0 end as claim_total, ";

//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        //---------------
        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = " + taon1 + "  then 1 else 0 end  as pol_no2 , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = " + taon1 + "  then round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) else 0 end as premi_total2 , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) = " + taon1 + "   then 1 else 0 end as claim_no2 , ";
//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) = " + taon1 + " then a.claim_amount else 0 end as claim_total2, ";

//        sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        //--------------

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = ?  then 1 else 0 end  as pol_no3 , ";
            sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and date_part('year',a.policy_date) = ?  then round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2) else 0 end as premi_total3 , ";
            sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) = ?   then 1 else 0 end as claim_no3 , ";
            sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        if (getPolicyDateFrom() != null) {

            sql = sql + " case when a.status in ('CLAIM') and date_part('year',a.policy_date) =  ? then a.claim_amount else 0 end as claim_total3 ";
            sqa.addPar(DateUtil.getYear(policyDateFrom));
        }

        sql = sql + " from ins_policy a   join ins_pol_coins d on d.policy_id = a.pol_id "
                + "  inner join ins_pol_obj b on b.pol_id = a.pol_id left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "  inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id"
                + "  inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "  inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "  inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "  where  a.effective_flag = 'Y' ";

// if (getStBranch()!=null) {
//      sql = sql + " and a.cc_code = ?";
//      sqa.addPar(stBranch);
//  }

        if (getStPolicyTypeGroupID() != null) {
            sql = sql + " and a.ins_policy_type_grp_id = ?";
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            sql = sql + " and a.pol_type_id = ?";
            sqa.addPar(stPolicyTypeID);
        }

        sqa.addQuery(" from band_level a " + sql + " group by a.policy_date,  a.status , a.claim_amount"
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.enabled = 'Y'");
        }

        sqa.addClause(" a.group_desc = 'RISK_LOSS_PROFILE' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LOSS_PROFILE_RI_FIRE() throws Exception {

        int taon = Integer.parseInt(DateUtil.getYear(policyDateFrom));
        int taon1 = taon - 1;


        BigDecimal premi_total = BigDecimal.ZERO;
        BigDecimal jumlah = BigDecimal.ZERO;
        BigDecimal claim_total = BigDecimal.ZERO;
        BigDecimal jumlah_claim = BigDecimal.ZERO;

        BigDecimal premi_total2 = BigDecimal.ZERO;
        BigDecimal jumlah2 = BigDecimal.ZERO;
        BigDecimal claim_total2 = BigDecimal.ZERO;
        BigDecimal jumlah_claim2 = BigDecimal.ZERO;

        BigDecimal premi_total3 = BigDecimal.ZERO;
        BigDecimal jumlah3 = BigDecimal.ZERO;
        BigDecimal claim_total3 = BigDecimal.ZERO;
        BigDecimal jumlah_claim3 = BigDecimal.ZERO;

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStPolicyTypeGroupDesc().toUpperCase() + " INSURANCE");

            //bikin header
            XSSFRow row1 = sheet.createRow(2);
            if (getStPolicyTypeDesc() != null) {
                row1.createCell(0).setCellValue("NET RETENTION");
            }

            //bikin header
            XSSFRow row2 = sheet.createRow(3);
            row2.createCell(0).setCellValue("COMPANY : PT.ASURANSI BANGUN ASKRIDA");

            //bikin header
            XSSFRow row3 = sheet.createRow(4);
            if (getPeriodFrom() != null) {
                row3.createCell(0).setCellValue("UNDERWRITING YEAR : " + DateUtil.getYear(getPeriodFrom()));
            }

            //bikin header
            XSSFRow row4 = sheet.createRow(5);
            if (getStBranch() != null) {
                row4.createCell(0).setCellValue("BRANCH : " + getStBranch());
            }

            //bikin header
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("SUM INSURED");
            row5.createCell(1).setCellValue("");
            row5.createCell(2).setCellValue("");
            row5.createCell(3).setCellValue(taon1 - 1);
            row5.createCell(4).setCellValue("");
            row5.createCell(5).setCellValue("");
            row5.createCell(6).setCellValue("");
            row5.createCell(7).setCellValue(taon1);
            row5.createCell(8).setCellValue("");
            row5.createCell(9).setCellValue("");
            row5.createCell(10).setCellValue("");
            row5.createCell(11).setCellValue(String.valueOf(DateUtil.getYear(policyDateFrom)));
            row5.createCell(12).setCellValue("");


            XSSFRow row8 = sheet.createRow(7);
            row8.createCell(0).setCellValue("RISK BASIS");
            row8.createCell(1).setCellValue("PREMIUM");
            row8.createCell(2).setCellValue("");

//
            row8.createCell(3).setCellValue("INCURRED CLAIM");
            row8.createCell(4).setCellValue("");

//
            row8.createCell(5).setCellValue("PREMIUM");
            row8.createCell(6).setCellValue("");

//
            row8.createCell(7).setCellValue("INCURRED CLAIM");
            row8.createCell(8).setCellValue("");
//
//
            row8.createCell(9).setCellValue("PREMIUM");
            row8.createCell(10).setCellValue("");

//
            row8.createCell(11).setCellValue("INCURRED CLAIM");
            row8.createCell(12).setCellValue("");



            //bikin header
            XSSFRow row6 = sheet.createRow(8);
            row6.createCell(0).setCellValue("");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");


            row6.createCell(3).setCellValue("IN AMOUNT");
            row6.createCell(4).setCellValue("NO");


            row6.createCell(5).setCellValue("IN AMOUNT");
            row6.createCell(6).setCellValue("NO");


            row6.createCell(7).setCellValue("IN AMOUNT");
            row6.createCell(8).setCellValue("NO");



            row6.createCell(9).setCellValue("IN AMOUNT");
            row6.createCell(10).setCellValue("NO");

            row6.createCell(11).setCellValue("IN AMOUNT");
            row6.createCell(12).setCellValue("NO");



            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 9);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premi_total") != null ? h.getFieldValueByFieldNameBD("premi_total").doubleValue() : 0);
            premi_total = premi_total.add(h.getFieldValueByFieldNameBD("premi_total"));

            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah") != null ? h.getFieldValueByFieldNameBD("jumlah").doubleValue() : 0);
            jumlah = jumlah.add(h.getFieldValueByFieldNameBD("jumlah"));

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total").doubleValue() : 0);
            claim_total = claim_total.add(h.getFieldValueByFieldNameBD("claim_total"));

            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim").doubleValue() : 0);
            jumlah_claim = jumlah_claim.add(h.getFieldValueByFieldNameBD("jumlah_claim"));

            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("premi_total2") != null ? h.getFieldValueByFieldNameBD("premi_total2").doubleValue() : 0);
            premi_total2 = premi_total2.add(h.getFieldValueByFieldNameBD("premi_total2"));

            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("jumlah2") != null ? h.getFieldValueByFieldNameBD("jumlah2").doubleValue() : 0);
            jumlah2 = jumlah2.add(h.getFieldValueByFieldNameBD("jumlah2"));

            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2").doubleValue() : 0);
            claim_total2 = claim_total2.add(h.getFieldValueByFieldNameBD("claim_total2"));

            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2").doubleValue() : 0);
            jumlah_claim2 = jumlah_claim2.add(h.getFieldValueByFieldNameBD("jumlah_claim2"));

            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_total3") != null ? h.getFieldValueByFieldNameBD("premi_total3").doubleValue() : 0);
            premi_total3 = premi_total3.add(h.getFieldValueByFieldNameBD("premi_total3"));

            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("jumlah3") != null ? h.getFieldValueByFieldNameBD("jumlah3").doubleValue() : 0);
            jumlah3 = jumlah3.add(h.getFieldValueByFieldNameBD("jumlah3"));

            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3").doubleValue() : 0);
            claim_total3 = claim_total3.add(h.getFieldValueByFieldNameBD("claim_total3"));

            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3").doubleValue() : 0);
            jumlah_claim3 = jumlah_claim3.add(h.getFieldValueByFieldNameBD("jumlah_claim3"));



        }

        XSSFRow rows = sheet.createRow(list.size() + 10);
        rows.createCell(0).setCellValue("TOTAL ");
        rows.createCell(1).setCellValue(premi_total.doubleValue());
        rows.createCell(2).setCellValue(jumlah.doubleValue());

        rows.createCell(3).setCellValue(claim_total.doubleValue());
        rows.createCell(4).setCellValue(jumlah_claim.doubleValue());


        rows.createCell(5).setCellValue(premi_total2.doubleValue());
        rows.createCell(6).setCellValue(jumlah2.doubleValue());

        rows.createCell(7).setCellValue(claim_total2.doubleValue());
        rows.createCell(8).setCellValue(jumlah_claim2.doubleValue());

        rows.createCell(9).setCellValue(premi_total3.doubleValue());
        rows.createCell(10).setCellValue(jumlah3.doubleValue());

        rows.createCell(11).setCellValue(claim_total3.doubleValue());
        rows.createCell(12).setCellValue(jumlah_claim3.doubleValue());


        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList REKAP_BPPDAN() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code,a.endorse_notes,b.order_no,coalesce(b.refd1,a.period_start) as period_start_det,"
                + " a.policy_date,a.period_start,a.period_end,i.valid_ri_date,"
                + " (substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,"
                + " a.pol_no,a.pol_type_id::text,a.cust_name,b.ins_pol_obj_id,getname(a.ins_policy_type_grp_id=1,b.ref5,b.ref8) as alamat,b.ref9d as kode_pos,b.risk_class,"
                + " f.ins_risk_cat_code,a.ccy,a.ccy_rate,b.premi_total as premi_total_d, "
                + " (	select round(sum(z.rate),5) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)) as premi_bppdan, "
                + " sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)) as komisi_bppdan, "
                + " checkpremi(a.cover_type_code= 'COINSIN',(select y.share_pct  from ins_policy x "
                + " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 "
                + " where x.pol_id = a.pol_id),a.share_pct) as member ");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type = 'BPDAN'");
        sqa.addClause(" a.pol_type_id in (1,19,81) ");

//        sqa.addSelect("*");
//        sqa.addQuery("from reas_bppdan2");

        /*
        if (policyDateFrom!=null&&ReinsDateFrom!=null) {
        sqa.addClause("((date_trunc('day',a.policy_date) >= ? and date_trunc('day',a.policy_date) <= ?) "+
        "or (date_trunc('day',i.valid_ri_date) >= ? and date_trunc('day',i.valid_ri_date) <= ?))");
        sqa.addPar(policyDateFrom);
        sqa.addPar(policyDateTo);
        sqa.addPar(ReinsDateFrom);
        sqa.addPar(ReinsDateTo);
        }
         */

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (ReinsDateFrom != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) >= ?");
            sqa.addPar(ReinsDateFrom);
        }

        if (ReinsDateTo != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) <= ?");
            sqa.addPar(ReinsDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneCode != null) {
            sqa.addClause("d.zone_id = ?");
            sqa.addPar(stZoneCode);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        String sql = "select cc_code,ccy,sum(premi_total_d) as premi_total,sum(premi_bppdan) as premi_bpdan,sum(komisi_bppdan) as comm_bpdan "
                + " from ( " + sqa.getSQL() + " group by a.cc_code,a.policy_date,b.order_no,a.pol_id,a.pol_no,a.cust_name,a.period_start,a.period_end,b.ref5,f.ins_risk_cat_code,a.endorse_notes, "
                + " a.ccy,a.ccy_rate,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.risk_class,i.valid_ri_date,b.refd1,b.refd2,a.pol_type_id,b.ref9d,a.ins_policy_type_grp_id,b.ref8 "
                + " ) x ";

//        String sql = sqa.getSQL();

        if (getStTime() != null) {
            if (getStTime().equalsIgnoreCase("1")) {
                sql = sql + " where years > '1' ";
            } else if (getStTime().equalsIgnoreCase("2")) {
                sql = sql + " where years <= '1' ";
            }
        }

        sql = sql + "group by cc_code,ccy order by cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public String getStTriwulan() {
        return stTriwulan;
    }

    public void setStTriwulan(String stTriwulan) {
        this.stTriwulan = stTriwulan;
    }

    public String getStPolCredit() {
        return stPolCredit;
    }

    public void setStPolCredit(String stPolCredit) {
        this.stPolCredit = stPolCredit;
    }

    public String getStNonCreditID() {
        return stNonCreditID;
    }

    public void setStNonCreditID(String stNonCreditID) {
        this.stNonCreditID = stNonCreditID;
    }

    public String getStNonCreditName() {
        return stNonCreditName;
    }

    public void setStNonCreditName(String stNonCreditName) {
        this.stNonCreditName = stNonCreditName;
    }

    public DTOList REKAP_MAIPARK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code,a.endorse_notes,b.order_no,coalesce(b.refd1,a.period_start) as period_start_det,"
                + " a.policy_date,a.period_start,a.period_end,i.valid_ri_date,"
                + " (substr(a.period_end::text,1,4)::numeric-substr(a.period_start::text,1,4)::numeric)::text as years,"
                + " a.pol_no,a.pol_type_id::text,a.cust_name,b.ins_pol_obj_id,getname(a.ins_policy_type_grp_id=1,b.ref5,b.ref8) as alamat,b.ref9d as kode_pos,b.risk_class,"
                + " f.ins_risk_cat_code,a.ccy,a.ccy_rate,b.premi_total as premi_total_d, "
                + " (	select round(sum(z.rate),5) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as rate_cover, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as building, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as machine, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as stock, "
                + "(	select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as other, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.premi_amount)),2) as premi_bppdan, "
                + " round(sum(checkreas(j.treaty_type='PARK',i.ricomm_amt)),2) as komisi_bppdan, "
                + " checkpremi(a.cover_type_code= 'COINSIN',(select y.share_pct  from ins_policy x "
                + " inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id = 1 "
                + " where x.pol_id = a.pol_id),a.share_pct) as member ");

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join  ins_risk_cat f on f.ins_risk_cat_id = b.ins_risk_cat_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type = 'PARK'");
        sqa.addClause(" a.pol_type_id in (1,19,81) ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (ReinsDateFrom != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) >= ?");
            sqa.addPar(ReinsDateFrom);
        }

        if (ReinsDateTo != null) {
            sqa.addClause("date_trunc('day',i.valid_ri_date) <= ?");
            sqa.addPar(ReinsDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        String sql = "select cc_code,ccy,ccy_rate,sum(premi_total_d) as premi_total,sum(premi_bppdan) as premi_bpdan,sum(komisi_bppdan) as comm_bpdan "
                + " from ( " + sqa.getSQL() + " group by a.cc_code,a.policy_date,b.order_no,a.pol_id,a.pol_no,a.cust_name,a.period_start,a.period_end,b.ref5,f.ins_risk_cat_code,a.endorse_notes, "
                + " a.ccy,a.ccy_rate,b.premi_total,a.cover_type_code,a.share_pct,b.ins_pol_obj_id,b.risk_class,i.valid_ri_date,b.refd1,b.refd2,a.pol_type_id,b.ref9d,a.ins_policy_type_grp_id,b.ref8 "
                + " ) x ";

        if (getStTime() != null) {
            if (getStTime().equalsIgnoreCase("1")) {
                sql = sql + " where years > '1' ";
            } else if (getStTime().equalsIgnoreCase("2")) {
                sql = sql + " where years <= '1' ";
            }
        }

        sql = sql + "group by cc_code,ccy,ccy_rate order by cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_MAIPARK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" coalesce(null,'') as ref1,e.short_desc as ref2,getname(a.status in ('POLICY','RENEWAL','ENDORSE RI'),a.pol_no,substr(a.pol_no,1,16)||'00') as nopol,"
                + " getname(a.status in ('ENDORSE'),a.pol_no,NULL) as ref4,getname(a.status in ('ENDORSE'),substr(a.pol_no,18,1),NULL) as ref5,c.risk_class as risk_no,"
                + " split_part(cust_name, 'QQ. ', 1) as cust_name,split_part(cust_name, 'QQ. ', 2) as qq,"
                + " a.cust_address as alamat,c.ref8 as alamat_resiko,c.ref2d as zona_gempa,coalesce(null,'') as ref10,coalesce(null,'') as ref11,coalesce(null,'') as ref12,"
                + " c.ref9d as postal_code,m.ins_risk_cat_code as occupation_code,c.ref4 as build_cat,a.status,c.order_no,"
                + " c.ref3 as class_construction,c.ref7 as year_build,k.vs_description as jml_lantai,a.period_start,a.period_end,a.ccy,"
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount))  from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as building, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as machine, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as stock, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (109),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as bussiness,  "
                + "( select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as other,  "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,6,12,21,22,23,25,27,28,29,30,33,35,39,40,44,56,57,58,90,91,92,107,79,93,94,95,105,106,107,108,109),z.insured_amount))  "
                + "from ins_policy x  inner join ins_pol_obj y on y.pol_id = x.pol_id  "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id  where y.ins_pol_obj_id=c.ins_pol_obj_id) as total_tsi_obj, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as building2, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as machine2, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount))  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as stock2, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (109),z.insured_amount))  from ins_policy x "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as bussiness2, "
                + "( select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount)) from ins_policy x  "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  "
                + "where y.ins_pol_obj_id=c.ins_pol_obj_id) as other2, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (2,6,12,21,22,23,25,27,28,29,30,33,35,39,40,44,56,57,58,90,91,92,107,79,93,94,95,105,106,107,108,109),z.insured_amount))  "
                + "from ins_policy x  inner join ins_pol_obj y on y.pol_id = x.pol_id  "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_ref_id  where y.ins_pol_obj_id=c.ins_pol_obj_id) as total_tsi_obj2, "
                + "coalesce(getpremi(a.status in ('ENDORSE'),a.insured_amount_e,a.insured_amount),0) as total_tsi,a.cover_type_code as coins_status,"
                + " round(getpremi(a.cover_type_code = 'COINSIN',a.share_pct,(select z.share_pct "
                + " from ins_pol_coins z where z.policy_id = a.pol_id and z.position_code = 'LDR')),2) as coins_share,"
                + " getname(a.cover_type_code = 'COINSIN',a.coins_pol_no,NULL) as ref29,a.status as status_policy,"
                //                + " (select x.description from ins_pol_cover z inner join ins_cover x on x.ins_cover_id = z.ins_cover_id "
                //                + " and z.ins_cover_id in (207,213,21) where z.ins_pol_obj_id = c.ins_pol_obj_id group by x.description) as ref31, "
                + " (select string_agg(x.description, '| ') from ins_pol_cover z inner join ins_cover x on x.ins_cover_id = z.ins_cover_id "
                + " and z.ins_cover_id in (207,213,21) where z.ins_pol_obj_id = c.ins_pol_obj_id ) as ref31, "
                + " checkreas(j.treaty_type='PARK',i.tsi_amount) as tsi,checkreas(j.treaty_type='PARK',i.premi_amount) as premi,checkreas(j.treaty_type='PARK',i.ricomm_amt) as comm,"
                + " coalesce(null,'') as ref34,coalesce(null,'') as ref35,coalesce(null,'') as ref36,coalesce(null,'') as ref37,"
                + " coalesce(null,'') as ref38,coalesce(null,'') as ref39,coalesce(null,'') as ref40,(a.period_end-a.period_start)::text as ref41,coalesce(null,'') as ref42,"
                + " substr(a.period_start::text,1,4) as under_year,coalesce(null,'') as ref44,a.endorse_notes,"
                + " ( select string_agg(z.description,',') as a "
                + " from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + " inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id = c.ins_pol_obj_id "
                + " and z.ins_tsi_cat_id in (109) ) as indemnity,"
                + " ( select z.time_excess||' '||z.time_excess_unit from ins_policy x "
                + " inner join ins_pol_obj y on y.pol_id = x.pol_id  inner join ins_pol_deduct z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + " where y.ins_pol_obj_id=c.ins_pol_obj_id and z.ins_clm_caus_id in (4021) group by z.time_excess,z.time_excess_unit) as time_excess ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " left join ent_master b on b.ent_id = a.entity_id "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                //+ " inner join ins_pol_tsi d on d.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_types e on e.pol_type_id = a.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " left join ins_risk_cat m on m.ins_risk_cat_id = c.ins_risk_cat_id "
                + " left join s_valueset k on k.vs_code = c.ref10 and k.vs_group = 'INSOBJ_EQ_STOREY' ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.pol_type_id in (1,19,81) ");
        sqa.addClause(" (i.premi_amount is not null or i.ricomm_amt is not null) ");
        sqa.addClause(" j.treaty_type = 'PARK'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stPostCode != null) {
            sqa.addClause("b.ref9 = ?");
            sqa.addPar(stPostCode);
        }

        if (stPostCodeDesc2 != null) {
            sqa.addClause("b.ref9d = ?");
            sqa.addPar(stPostCodeDesc2);
        }

        if (stZoneEquakeName != null) {
            sqa.addClause("b.ref2d = ?");
            sqa.addPar(stZoneEquakeName);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        final String sql = " select ref1,ref2,nopol,ref4,ref5,risk_no,cust_name,qq,alamat,alamat_resiko,zona_gempa,ref10,ref11,ref12,postal_code,"
                + "occupation_code,build_cat,class_construction,year_build,jml_lantai,period_start,period_end,ccy,"
                + "getpremi(status = 'ENDORSE',coalesce(building,0)-coalesce(building2,0),coalesce(building,0)) as building,"
                + "getpremi(status = 'ENDORSE',coalesce(machine,0)-coalesce(machine2,0),coalesce(machine,0)) as machine,"
                + "getpremi(status = 'ENDORSE',coalesce(stock,0)-coalesce(stock2,0),coalesce(stock,0)) as stock,"
                + "getpremi(status = 'ENDORSE',coalesce(bussiness,0)-coalesce(bussiness2,0),coalesce(bussiness,0)) as bussiness,"
                + "getpremi(status = 'ENDORSE',coalesce(other,0)-coalesce(other2,0),coalesce(other,0)) as other,"
                + "getpremi(status = 'ENDORSE',coalesce(total_tsi_obj,0)-coalesce(total_tsi_obj2,0),coalesce(total_tsi_obj,0)) as total_tsi_obj,"
                + "total_tsi,coins_status,coins_share,ref29,status_policy,ref31,"
                + "tsi,premi,comm,ref34,ref35,ref36,ref37,ref38,ref39,ref40,ref41,ref42,under_year,ref44,endorse_notes,indemnity,time_excess "
                + " from ( " + sqa.getSQL() + " group by a.pol_id,a.pol_no,a.cust_name,a.cust_address,a.period_start,a.period_end,a.ccy,c.ins_pol_obj_id,a.policy_date, "
                + "c.insured_amount,c.insured_amount_e,j.treaty_type,i.tsi_amount,i.premi_amount,i.ricomm_amt,c.ref9d,m.ins_risk_cat_code,c.risk_class,c.ref4,c.ref3,c.ref7,c.ref8,c.ref10, "
                + "a.cover_type_code,a.share_pct,a.status,e.short_desc,k.vs_description,a.status "
                + "order by a.policy_date,a.pol_no,c.ins_pol_obj_id ) a order by a.nopol,a.ref4 asc,a.order_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_MAIPARK() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal insuredBuilding = new BigDecimal(0);
        BigDecimal insuredMachine = new BigDecimal(0);
        BigDecimal insuredStock = new BigDecimal(0);
        BigDecimal insuredBussiness = new BigDecimal(0);
        BigDecimal insuredOther = new BigDecimal(0);
        BigDecimal insured = new BigDecimal(0);
        int norut = 0;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Running No. (1)");
            row0.createCell(1).setCellValue("Main Policy Cover (2)");
            row0.createCell(2).setCellValue("Original Policy No. (3)");
            row0.createCell(3).setCellValue("Endorsement No. (4)");
            row0.createCell(4).setCellValue("Endorsement Count (5)");
            row0.createCell(5).setCellValue("Risk No. (6)");
            row0.createCell(6).setCellValue("Name Of Insured (7)");
            row0.createCell(7).setCellValue("QQ (8)");
            row0.createCell(8).setCellValue("Location of Risk (9)");
            row0.createCell(9).setCellValue("City/ Kabupaten (10)");
            row0.createCell(10).setCellValue("Kecamatan (11)");
            row0.createCell(11).setCellValue("Kelurahan (12)");
            row0.createCell(12).setCellValue("Postal Code (13)");
            row0.createCell(13).setCellValue("Occupation Code (14)");
            row0.createCell(14).setCellValue("Building Category (15)");
            row0.createCell(15).setCellValue("Class of Construction (16)");
            row0.createCell(16).setCellValue("Year Build (17)");
            row0.createCell(17).setCellValue("Building Storey (18)");
            row0.createCell(18).setCellValue("Inception/ Effective Date (19)");
            row0.createCell(19).setCellValue("Expiry Date (20)");
            row0.createCell(20).setCellValue("Currency (21)");
            row0.createCell(21).setCellValue("Building (22)");
            row0.createCell(22).setCellValue("Machinery (23)");
            row0.createCell(23).setCellValue("Stock (24)");
            row0.createCell(24).setCellValue("Others (25)");
            row0.createCell(25).setCellValue("Total (26)");
            row0.createCell(26).setCellValue("Co-Ins Status (27)");
            row0.createCell(27).setCellValue("Co-Ins Share (%) (28)");
            row0.createCell(28).setCellValue("Leader Policy No. (29)");
            row0.createCell(29).setCellValue("Policy Status (30)");
            row0.createCell(30).setCellValue("First Loss Limit (%) (31)");
            row0.createCell(31).setCellValue("Sum Insured (32)");
            row0.createCell(32).setCellValue("Premium (33)");
            row0.createCell(33).setCellValue("Gross Profit (34)");
            row0.createCell(34).setCellValue("Gross Wages (35)");
            row0.createCell(35).setCellValue("SI Increasing In Cost of Working (36)");
            row0.createCell(36).setCellValue("SI Others (37)");
            row0.createCell(37).setCellValue("Total Sum Insured (38)");
            row0.createCell(38).setCellValue("Period of Indemnity (Number of Months) (39)");
            row0.createCell(39).setCellValue("Time Excess (Number of Days) (40)");
            row0.createCell(40).setCellValue("Prorata Hari (41)");
            row0.createCell(41).setCellValue("Stock (42)");
            row0.createCell(42).setCellValue("Underwriting Year (43)");
            row0.createCell(43).setCellValue("Remark (44)");
            row0.createCell(44).setCellValue("Comm (45)");

            norut++;

            if (i > 0) {
                HashDTO s = (HashDTO) list.get(i - 1);
                String nopol = s.getFieldValueByFieldNameST("nopol") + s.getFieldValueByFieldNameST("ref4");
                String nopol2 = h.getFieldValueByFieldNameST("nopol") + h.getFieldValueByFieldNameST("ref4");
                if (!nopol.equalsIgnoreCase(nopol2)) {

                    norut = 1;
                }
            }

            if (h.getFieldValueByFieldNameST("coins_status").equalsIgnoreCase("COINSIN")) {
                insuredBuilding = BDUtil.div(h.getFieldValueByFieldNameBD("building"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
                insuredMachine = BDUtil.div(h.getFieldValueByFieldNameBD("machine"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
                insuredStock = BDUtil.div(h.getFieldValueByFieldNameBD("stock"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
                insuredBussiness = BDUtil.div(h.getFieldValueByFieldNameBD("bussiness"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
                insuredOther = BDUtil.div(h.getFieldValueByFieldNameBD("other"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
                insured = BDUtil.div(h.getFieldValueByFieldNameBD("total_tsi_obj"), BDUtil.getRateFromPct(h.getFieldValueByFieldNameBD("coins_share")));
            } else {
                insuredBuilding = h.getFieldValueByFieldNameBD("building");
                insuredMachine = h.getFieldValueByFieldNameBD("machine");
                insuredStock = h.getFieldValueByFieldNameBD("stock");
                insuredBussiness = h.getFieldValueByFieldNameBD("bussiness");
                insuredOther = h.getFieldValueByFieldNameBD("other");
                insured = h.getFieldValueByFieldNameBD("total_tsi_obj");
            }

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(String.valueOf(i + 1));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ref2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(5).setCellValue(String.valueOf(norut));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("qq"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("alamat_resiko"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("zona_gempa"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ref11"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ref12"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("postal_code"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("occupation_code"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("build_cat"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("class_construction"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("year_build"));
            row.createCell(17).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jml_lantai")));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            if (insuredBuilding != null) {
                row.createCell(21).setCellValue(insuredBuilding.doubleValue());
            }
            if (insuredMachine != null) {
                row.createCell(22).setCellValue(insuredMachine.doubleValue());
            }
            if (insuredStock != null) {
                row.createCell(23).setCellValue(insuredStock.doubleValue());
            }
            if (insuredOther != null) {
                row.createCell(24).setCellValue(insuredOther.doubleValue());
            }
            if (insured != null) {
                row.createCell(25).setCellValue(insured.doubleValue());
            }
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("coins_status"));
            if (h.getFieldValueByFieldNameBD("coins_share") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("coins_share").doubleValue() + " %");
            }
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("ref29"));
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("status_policy"));
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameST("ref31"));
            if (h.getFieldValueByFieldNameBD("tsi") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            }
            if (insuredBussiness != null) {
                row.createCell(33).setCellValue(insuredBussiness.doubleValue());
            }
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("ref35"));
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameST("ref36"));
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("ref37"));
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("ref38"));
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("indemnity"));
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("time_excess"));
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("ref41"));
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("ref42"));
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("under_year"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("endorse_notes"));
            if (h.getFieldValueByFieldNameBD("comm") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("comm").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public String getPeriodTitleDescription() throws Exception {

        String periodTo = Integer.toString(DateUtil.getMonthDigit(policyDateTo) + 1);
        String year = DateUtil.getYear(policyDateTo).toString();

        if (year != null) {

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(periodTo, year);

            Date d = pd.getDtEndDate();

            return DateUtil.getDateStr(d, "^^ dd yyyy");
        }

        return "???";
    }

    public DTOList KARK_LPH() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.period_start,a.period_end, a.policy_date,a.pol_id,a.pol_no,a.cust_name,"
                + "a.ccy,a.ccy_rate,b.ref5,b.ins_pol_obj_id,"
                + "(   select sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)) "
                + "from ins_policy x     "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id      "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as refn1, "
                + "( select sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount))      "
                + "from ins_policy x      "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id    "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id      "
                + " where y.ins_pol_obj_id=b.ins_pol_obj_id) as refn2, "
                + "(  select sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)) "
                + "from ins_policy x      "
                + "inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id       "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as refn3, "
                + "(    select sum(getkoas(z.ins_tsi_cat_id not in (6,105,39,40,106,56,57,58,90,91,92,107),z.insured_amount))   "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id        "
                + "inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as refn4,"
                + "(    select sum(getkoas(z.ins_cover_id in (28),z.premi))     "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id   "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id     "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_comm1,"
                + "(    select sum(getkoas(z.ins_cover_id in (22),z.premi))       "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id        "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_comm2,"
                + "(    select sum(getkoas(z.ins_cover_id in (94),z.premi))       "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id        "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_comm3,"
                + "(    select sum(getkoas(z.ins_cover_id in (90,91,92),z.premi))     "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id       "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_comm4,"
                + "(    select sum(getkoas(z.ins_cover_id in (93),z.premi))       "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id        "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_brok1,"
                + "(    select sum(getkoas(z.ins_cover_id not in (22,28,90,91,92,93,94),z.premi))  "
                + "from ins_policy x inner join ins_pol_obj y on y.pol_id = x.pol_id        "
                + "inner join ins_pol_cover z on z.ins_pol_obj_id = y.ins_pol_obj_id        "
                + "where y.ins_pol_obj_id=b.ins_pol_obj_id) as nd_brok2 ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') ");
        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.ins_policy_type_grp_id = 12 and a.pol_type_id = 2");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = " select a.period_start,a.period_end,a.policy_date,a.pol_id,a.pol_no,a.cust_name,a.ccy,"
                + "a.ccy_rate,a.ins_pol_obj_id,a.ref5,((a.refn1*b.share_pct)/100) as refn1,"
                + "((a.refn2*b.share_pct)/100) as refn2,((a.refn3*b.share_pct)/100) as refn3,"
                + "((a.refn4*b.share_pct)/100) as refn4,((a.nd_comm1*b.share_pct)/100) as nd_comm1,"
                + "((a.nd_comm2*b.share_pct)/100) as nd_comm2,((a.nd_comm3*b.share_pct)/100) as nd_comm3,"
                + "((a.nd_comm4*b.share_pct)/100) as nd_comm4,((a.nd_brok1*b.share_pct)/100) as nd_brok1,"
                + "((a.nd_brok2*b.share_pct)/100) as nd_brok2 from ( "
                + sqa.getSQL() + " ) a inner join ins_pol_coins b on b.policy_id = a.pol_id and b.entity_id = 1 "
                + " order by a.policy_date,a.pol_id,a.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        if (getStNoUrut() != null) {
            getRemoteInsurance().updateNoLPHKark(l, this);
        }

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList KARK_RPB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.period_start,a.period_end, a.policy_date,a.pol_id,a.pol_no,a.cust_name,"
                + "a.ccy,a.ccy_rate,a.ref1,a.premi_total,a.nd_comm1,a.nd_comm2 ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') ");
        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.ins_policy_type_grp_id = 12 and a.pol_type_id = 2");
        sqa.addClause(" a.ref1 is not null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = sqa.getSQL() + " order by a.ref1,a.cc_code,a.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        if (getStNoUrut() != null) {
            getRemoteInsurance().updateNoRPBKark(l, this);
        }

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public InsurancePolicyTypeView getPolicyType() {
        final InsurancePolicyTypeView polType = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);

        return polType;
    }

    public InsurancePolicyTypeGroupView getPolicyTypeGroup() {
        final InsurancePolicyTypeGroupView polType = (InsurancePolicyTypeGroupView) DTOPool.getInstance().getDTO(InsurancePolicyTypeGroupView.class, stPolicyTypeGroupID);

        return polType;
    }

    public DTOList KSCBI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_no,b.ref1,a.cust_name,b.ref9d as ref2,b.ref12 as ref3,a.period_start,a.period_end,a.pol_type_id,"
                + "(b.insured_amount*a.ccy_rate) as insured_amount,(b.premi_total*a.ccy_rate) as premi_total,"
                + "(i.tsi_amount*a.ccy_rate) as amount,(i.premi_amount*a.ccy_rate) as premi_base,(i.ricomm_amt*a.ccy_rate) as premi_netto ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        sqa.addClause(" a.ins_policy_type_grp_id in (7,8,34)");
        sqa.addClause(" j.treaty_type='KSCBI'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + " order by a.policy_date,a.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_KSCBI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("';'||a.pol_no as polno,b.ref1 as principal,a.cust_name as obligee,b.ref9d as penerbit,b.ref12 as nopenjamin,a.period_start as periodeawal,a.period_end as periodeakhir,c.short_desc as jenis,"
                + "(b.insured_amount*a.ccy_rate) as nilaijaminan100,(b.premi_total*a.ccy_rate) as nilaijaminan10,"
                + "(i.tsi_amount*a.ccy_rate) as biayajasa,(i.premi_amount*a.ccy_rate) as handlingfee,(i.ricomm_amt*a.ccy_rate) as totalpool ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        sqa.addClause(" a.ins_policy_type_grp_id in (7,8,34)");
        sqa.addClause(" j.treaty_type='KSCBI'");

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

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stRegion + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '%'" + stPolicyNo + "'%'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        String sql = sqa.getSQL() + " order by a.policy_date,a.pol_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "kscbi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

    }

    public DTOList SOA_OLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,a.pol_no,substr(a.policy_date::text,1,4) as tahun,substr(a.policy_date::text,6,2) as bulan,"
                + "a.cover_type_code,j.treaty_type as co_treaty_id,i.sharepct as share_pct,k.ent_id as entity_id,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'),(i.premi_amount*a.ccy_rate))) as premi_amt,"
                + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD'),(i.ricomm_amt*a.ccy_rate))) as nd_brok2pct,"
                + "sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA',(i.claim_amount*a.ccy_rate))) as claim_amount ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id=a.pol_id " + clauseObj
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id ");


        sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("k.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("2") || stPolicyTypeGroupID.equalsIgnoreCase("9")) {
                if (stPolicyTypeID != null) {
                    if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
                        sqa.addClause("a.ins_policy_type_grp_id in (2,9)");
                    } else {
                        sqa.addClause("a.ins_policy_type_grp_id = ?");
                        sqa.addPar(stPolicyTypeGroupID);
                    }
                }
            } else {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(stPolicyTypeGroupID);
            }
        }

        if (stPolicyTypeID != null) {
            if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
                sqa.addClause("a.pol_type_id in (21,59)");
            } else {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }

//        if (stFltTreatyType != null) {
//            if (stPolicyTypeID != null) {
//                if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
//                    if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
//                        sqa.addClause("j.treaty_type in ('SPL','QS')");
//                    } else {
//                        sqa.addClause("j.treaty_type = ?");
//                        sqa.addPar(stFltTreatyType);
//                    }
//                } else {
//                    sqa.addClause("j.treaty_type = ?");
//                    sqa.addPar(stFltTreatyType);
//                }
//            }
//        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        String sql = null;

        sql = " select a.entity_id,a.share_pct,sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
                + " from ( " + sqa.getSQL() + " group by a.pol_id,a.pol_no,substr(a.policy_date::text,1,4),substr(a.policy_date::text,6,2),"
                + " a.cover_type_code,j.treaty_type,k.ent_id,i.sharepct,c.refd1,c.refd2,c.refd3 "
                + " ) a where (premi_amt <> 0 or claim_amount <> 0) ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        sql = sql + " group by a.entity_id,a.share_pct order by a.entity_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    /**
     * @return the journalDateFrom
     */
    public Date getJournalDateFrom() {
        return journalDateFrom;
    }

    /**
     * @param journalDateFrom the journalDateFrom to set
     */
    public void setJournalDateFrom(Date journalDateFrom) {
        this.journalDateFrom = journalDateFrom;
    }

    /**
     * @return the journalDateTo
     */
    public Date getJournalDateTo() {
        return journalDateTo;
    }

    /**
     * @param journalDateTo the journalDateTo to set
     */
    public void setJournalDateTo(Date journalDateTo) {
        this.journalDateTo = journalDateTo;
    }

    /**
     * @return the stCompanyProdID
     */
    public String getStCompanyProdID() {
        return stCompanyProdID;
    }

    /**
     * @param stCompanyProdID the stCompanyProdID to set
     */
    public void setStCompanyProdID(String stCompanyProdID) {
        this.stCompanyProdID = stCompanyProdID;
    }

    /**
     * @return the stCompanyProdName
     */
    public String getStCompanyProdName() {
        return stCompanyProdName;
    }

    /**
     * @param stCompanyProdName the stCompanyProdName to set
     */
    public void setStCompanyProdName(String stCompanyProdName) {
        this.stCompanyProdName = stCompanyProdName;
    }

    /*
    public DTOList SOA_TREATY_OLD() throws Exception {
    final SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" d.group_name as ref1,j.treaty_type as ref2, "
    + "sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, "
    + "sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amt, "
    + "sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_brok2pct, "
    + "sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount ");


    sqa.addQuery(" from ins_policy a "
    + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
    + "inner join ins_policy_types f on f.pol_type_id = a.pol_type_id "
    + "inner join ins_policy_type_grp d on d.ins_policy_type_grp_id = f.ins_policy_type_grp_id "
    + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
    + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
    + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
    + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

    sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
    sqa.addClause("a.active_flag = 'Y'");
    sqa.addClause("a.effective_flag = 'Y'");

    String queryDate = "((a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') "
    + "and date_trunc('day',a.policy_date) >= '" + policyDateFrom + "' "
    + "and date_trunc('day',a.policy_date) <= '" + policyDateTo + "' ) or "
    + "(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
    + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateFrom + "' "
    + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateTo + "' )) ";

    sqa.addClause(queryDate);

    if (periodFrom != null) {
    sqa.addClause("case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) >= ? "
    + "when a.pol_type_id in (1,3,5,81) then date_trunc('day',c.refd1) >= ? "
    + "else date_trunc('day',a.period_start) >= ? end ");
    sqa.addPar(periodFrom);
    sqa.addPar(periodFrom);
    sqa.addPar(periodFrom);
    }

    if (periodTo != null) {
    sqa.addClause("case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) <= ? "
    + "when a.pol_type_id in (1,3,5,81) then date_trunc('day',c.refd1) <= ? "
    + "else date_trunc('day',a.period_start) <= ? end ");
    sqa.addPar(periodTo);
    sqa.addPar(periodTo);
    sqa.addPar(periodTo);
    }

    if (stBranch != null) {
    sqa.addClause("a.cc_code = ?");
    sqa.addPar(stBranch);
    }

    if (stRegion != null) {
    sqa.addClause("a.region_id = ?");
    sqa.addPar(stRegion);
    }

    if (stEntityID != null) {
    sqa.addClause("i.member_ent_id = ?");
    sqa.addPar(stEntityID);
    }

    if (stPolicyTypeGroupID != null) {
    if (stPolicyTypeGroupID.equalsIgnoreCase("2") || stPolicyTypeGroupID.equalsIgnoreCase("9")) {
    if (stPolicyTypeID != null) {
    if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
    sqa.addClause("a.ins_policy_type_grp_id in (2,9)");
    } else {
    sqa.addClause("a.ins_policy_type_grp_id = ?");
    sqa.addPar(stPolicyTypeGroupID);
    }
    }
    } else {
    sqa.addClause("a.ins_policy_type_grp_id = ?");
    sqa.addPar(stPolicyTypeGroupID);
    }
    }

    if (stPolicyTypeID != null) {
    if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
    sqa.addClause("a.pol_type_id in (21,59)");
    } else {
    sqa.addClause("a.pol_type_id = ?");
    sqa.addPar(stPolicyTypeID);
    }
    }

    if (stFltTreatyType != null) {
    if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
    sqa.addClause("j.treaty_type in ('SPL','QS')");
    } else {
    sqa.addClause("j.treaty_type = ?");
    sqa.addPar(stFltTreatyType);
    }
    }

    if (stPolCredit != null) {
    sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
    }

    final String sql = sqa.getSQL() + " group by d.group_name,j.treaty_type order by d.group_name ";

    final DTOList l = ListUtil.getDTOListFromQuery(
    sql,
    sqa.getPar(),
    InsurancePolicyView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
    }
     */
    public DTOList SOA_TREATY_OLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String query = "j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.desc_soa as description,f.premi_rate_soa,f.comm_rate_soa,"
                + "a.ccy,a.ccy_rate,a.ccy_rate_treaty,';'||a.pol_no as pol_no,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end, "
                + "k.ent_id,k.short_name as cust_name, "
                + "(select z.sharepct from ins_treaty x "
                + "inner join ins_treaty_detail y on y.ins_treaty_id = x.ins_treaty_id "
                + "inner join ins_treaty_shares z on z.ins_treaty_detail_id = y.ins_treaty_detail_id "
                + "where substr(x.treaty_period_start::text,1,4) = '" + DateUtil.getYear(periodFrom) + "' and x.treaty_class = 'RE' "
                + "and y.policy_type_id = a.pol_type_id and y.treaty_type = '" + stFltTreatyType + "' and z.member_ent_id = " + stEntityID + " ) as rate,"
                + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') "
                + "and date_trunc('day',a.policy_date) >= '" + policyDateFrom + "' "
                + "and date_trunc('day',a.policy_date) <= '" + policyDateTo + "' ,(i.premi_amount*a.ccy_rate))) as premi_netto,"
                + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') "
                + "and date_trunc('day',a.policy_date) >= '" + policyDateFrom + "' "
                + "and date_trunc('day',a.policy_date) <= '" + policyDateTo + "' ,(i.ricomm_amt*a.ccy_rate))) as nd_comm1,"
                + "sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateFrom + "' "
                + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateTo + "' ,(i.claim_amount*a.ccy_rate_claim))) as claim_amount ";

        sqa.addSelect(query);

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("f.desc_soa <> 'UNCLASSED'");
        //sqa.addClause("a.pol_type_id in (13,15,16,6,7,8,9,10,68,82,45,46,51,52,53,54)");

        String queryDate = "((a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') "
                + "and date_trunc('day',a.policy_date) >= '" + policyDateFrom + "' "
                + "and date_trunc('day',a.policy_date) <= '" + policyDateTo + "' ) or "
                + "(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateFrom + "' "
                + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateTo + "' )) ";

        sqa.addClause(queryDate);

//        if (periodFrom != null) {
//            sqa.addClause("case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) >= ? "
//                    + "when a.pol_type_id in (1,3,5,81) then date_trunc('day',c.refd1) >= ? "
//                    + "else date_trunc('day',a.period_start) >= ? end ");
//            sqa.addPar(periodFrom);
//            sqa.addPar(periodFrom);
//            sqa.addPar(periodFrom);
//        }
//
//        if (periodTo != null) {
//            sqa.addClause("case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) <= ? "
//                    + "when a.pol_type_id in (1,3,5,81) then date_trunc('day',c.refd1) <= ? "
//                    + "else date_trunc('day',a.period_start) <= ? end ");
//            sqa.addPar(periodTo);
//            sqa.addPar(periodTo);
//            sqa.addPar(periodTo);
//        }
//
//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = ?");
//            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("a.region_id = ?");
//            sqa.addPar(stRegion);
//        }

//        if (stEntityID != null) {
//            sqa.addClause("i.member_ent_id = ?");
//            sqa.addPar(stEntityID);
//        }

        if (stPolicyTypeGroupID != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("2") || stPolicyTypeGroupID.equalsIgnoreCase("9")) {
                if (stPolicyTypeID != null) {
                    if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
                        sqa.addClause("a.ins_policy_type_grp_id in (2,9)");
                    } else {
                        sqa.addClause("a.ins_policy_type_grp_id = ?");
                        sqa.addPar(stPolicyTypeGroupID);
                    }
                }
            } else {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(stPolicyTypeGroupID);
            }
        }

        if (stPolicyTypeID != null) {
            if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
                sqa.addClause("a.pol_type_id in (21,59)");
            } else {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        final String sql = "select a.description as ref1,a.treaty_type as ref2,"
                + "coalesce(a.rate,0) as refn1,a.premi_rate_soa as refn2,a.comm_rate_soa as refn3,"
                + "sum(premi100) as premi_total,sum(coalesce(claim_amount,0)) as claim_amount "
                + "from ( select a.*,"
                + "getpremi(a.treaty_type  in ('QS','SPL'),a.premi_netto/(a.premi_rate_soa/100),a.premi_netto) as premi100 "
                + "from ( "
                + sqa.getSQL() + " group by j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.desc_soa,f.premi_rate_soa,f.comm_rate_soa,"
                + "a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,k.ent_id ,a.pol_no "
                + "order by f.ins_policy_type_grp_id,a.pol_type_id,f.desc_soa,j.treaty_type,k.short_name "
                + " ) a where date_trunc('day',a.period_start) >= '" + getPeriodFrom() + "' "
                + " and date_trunc('day',a.period_start) <= '" + getPeriodTo() + "' "
                + " and (a.premi_netto <> 0 or a.nd_comm1 <> 0 or claim_amount <> 0) "
                + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name "
                + " ) a where a.description <> 'UNCLASSED' "
                + "group by a.description,a.treaty_type,a.rate,a.premi_rate_soa,a.comm_rate_soa order by a.description,a.treaty_type ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
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

    public DTOList SOA_TREATY_OLD2() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String query = "treaty_type,ins_policy_type_grp_id,pol_type_id,a.desc_soa as description,"
                + "premi_rate_soa,comm_rate_soa,ccy,ccy_rate,ccy_rate_treaty,pol_no,ins_pol_obj_id,"
                + "period_start,period_end,ent_id,sum(premi_netto) as premi_netto,sum(nd_comm1) as nd_comm1,sum(claim_amount) as claim_amount,"
                + "(select z.sharepct from ins_treaty x "
                + "inner join ins_treaty_detail y on y.ins_treaty_id = x.ins_treaty_id "
                + "inner join ins_treaty_shares z on z.ins_treaty_detail_id = y.ins_treaty_detail_id "
                + "where substr(x.treaty_period_start::text,1,4) = '" + DateUtil.getYear(periodFrom) + "' and x.treaty_class = 'RE' "
                + "and y.policy_type_id = a.pol_type_id and y.treaty_type = '" + stFltTreatyType + "' and z.member_ent_id = " + stEntityID + " ) as rate ";

        sqa.addSelect(query);

        sqa.addQuery(" from s_report_for_soa a ");

        String bulanStart = DateUtil.getMonth2Digit(policyDateFrom);
        String bulanEnd = DateUtil.getMonth2Digit(policyDateTo);
        sqa.addClause("a.bulan between ? and ? ");
        sqa.addPar(bulanStart);
        sqa.addPar(bulanEnd);

        sqa.addClause("a.tahun = ? ");
        sqa.addPar(DateUtil.getYear(policyDateTo));

        //sqa.addClause("a.desc_soa <> 'UNCLASSED'");

//        sqa.addClause("a.ins_policy_type_grp_id in (8,4)");

//        if (stPolicyTypeGroupID != null) {
//            if (stPolicyTypeGroupID.equalsIgnoreCase("2") || stPolicyTypeGroupID.equalsIgnoreCase("9")) {
//                if (stPolicyTypeID != null) {
//                    if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
//                        sqa.addClause("a.ins_policy_type_grp_id in (2,9)");
//                    } else {
//                        sqa.addClause("a.ins_policy_type_grp_id = ?");
//                        sqa.addPar(stPolicyTypeGroupID);
//                    }
//                }
//            } else {
//                sqa.addClause("a.ins_policy_type_grp_id = ?");
//                sqa.addPar(stPolicyTypeGroupID);
//            }
//        }
//
//        if (stPolicyTypeID != null) {
//            if (stPolicyTypeID.equalsIgnoreCase("21") || stPolicyTypeID.equalsIgnoreCase("59")) {
//                sqa.addClause("a.pol_type_id in (21,59)");
//            } else {
//                sqa.addClause("a.pol_type_id = ?");
//                sqa.addPar(stPolicyTypeID);
//            }
//        }

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                sqa.addClause("a.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("a.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        final String sql = "select a.description as ref1,a.treaty_type as ref2,"
                + "coalesce(a.rate,0) as refn1,a.premi_rate_soa as refn2,a.comm_rate_soa as refn3,"
                + "sum(premi100) as premi_total,sum(coalesce(claim_amount,0)) as claim_amount "
                + "from ( select a.*,"
                + "getpremi(a.treaty_type  in ('QS'),a.premi_netto/(a.premi_rate_soa/100),a.premi_netto) as premi100 "
                + "from ( "
                + sqa.getSQL() + " group by treaty_type,ins_policy_type_grp_id,pol_type_id,"
                + "a.desc_soa,premi_rate_soa,comm_rate_soa,ccy,ccy_rate,ccy_rate_treaty,pol_no,"
                + "ins_pol_obj_id,period_start,period_end,ent_id "
                + "order by a.ins_policy_type_grp_id,a.pol_type_id,a.desc_soa,a.treaty_type,a.ent_id "
                + " ) a where date_trunc('day',a.period_start) >= '" + getPeriodFrom() + "' "
                + " and date_trunc('day',a.period_start) <= '" + getPeriodTo() + "' "
                + " and (a.premi_netto <> 0 or a.nd_comm1 <> 0 or claim_amount <> 0) "
                + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type "
                + " ) a group by a.description,a.treaty_type,a.premi_rate_soa,a.comm_rate_soa,a.rate "
                + "order by a.description,a.treaty_type ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_REKAPJENIS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("j.treaty_type,a.pol_type_id,';'||a.pol_no as pol_no,a.status,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                + "(round(a.premi_total,2)*a.ccy_rate) as premi_total,"
                + "sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                + "sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm1 ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y'");
        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause(" j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        String sql = "select a.pol_type_id,a.description,sum(coalesce(premi_total,0)) as premi_total,"
                + "sum(coalesce(premiOR,0)) as premiOR,sum(coalesce(premiBPDAN,0)) as premiBPDAN,"
                + "sum(coalesce(premiQS,0)) as premiQS,sum(coalesce(premiSPL,0)) as premiSPL,"
                + "sum(coalesce(premiQSKR,0)) as premiQSKR,sum(coalesce(premiJP,0)) as premiJP,"
                + "sum(coalesce(premiKSCBI,0)) as premiKSCBI,sum(coalesce(premiPARK,0)) as premiPARK,"
                + "sum(coalesce(premiFAC,0)) as premiFAC,sum(coalesce(premiFACO,0)) as premiFACO,"
                + "sum(coalesce(premiFACO1,0)) as premiFACO1,sum(coalesce(premiFACO2,0)) as premiFACO2,"
                + "sum(coalesce(premiFACO3,0)) as premiFACO3,sum(coalesce(premiFACP,0)) as premiFACP,"
                + "sum(coalesce(commOR,0)) as commOR,sum(coalesce(commBPDAN,0)) as commBPDAN,"
                + "sum(coalesce(commQS,0)) as commQS,sum(coalesce(commSPL,0)) as commSPL,"
                + "sum(coalesce(commQSKR,0)) as commQSKR,sum(coalesce(commJP,0)) as commJP,"
                + "sum(coalesce(commKSCBI,0)) as commKSCBI,sum(coalesce(commPARK,0)) as commPARK,"
                + "sum(coalesce(commFAC,0)) as commFAC,sum(coalesce(commFACO,0)) as commFACO,"
                + "sum(coalesce(commFACO1,0)) as commFACO1,sum(coalesce(commFACO2,0)) as commFACO2,"
                + "sum(coalesce(commFACO3,0)) as commFACO3,sum(coalesce(commFACP,0)) as commFACP "
                + " from ins_policy_types a left join ( "
                + "select a.pol_type_id,a.premi_total,"
                + "sum(getpremi2(a.treaty_type = 'BPDAN',a.premi_netto)) as premiBPDAN,"
                + "sum(getpremi2(a.treaty_type = 'FAC',a.premi_netto)) as premiFAC,"
                + "sum(getpremi2(a.treaty_type = 'FACO',a.premi_netto)) as premiFACO,"
                + "sum(getpremi2(a.treaty_type = 'FACO1',a.premi_netto)) as premiFACO1,"
                + "sum(getpremi2(a.treaty_type = 'FACO2',a.premi_netto)) as premiFACO2,"
                + "sum(getpremi2(a.treaty_type = 'FACO3',a.premi_netto)) as premiFACO3,"
                + "sum(getpremi2(a.treaty_type = 'FACP',a.premi_netto)) as premiFACP,"
                + "sum(getpremi2(a.treaty_type = 'JP',a.premi_netto)) as premiJP,"
                + "sum(getpremi2(a.treaty_type = 'KSCBI',a.premi_netto)) as premiKSCBI,"
                + "sum(getpremi2(a.treaty_type = 'OR',a.premi_netto)) as premiOR,"
                + "sum(getpremi2(a.treaty_type = 'PARK',a.premi_netto)) as premiPARK,"
                + "sum(getpremi2(a.treaty_type = 'QS',a.premi_netto)) as premiQS,"
                + "sum(getpremi2(a.treaty_type = 'QSKR',a.premi_netto)) as premiQSKR,"
                + "sum(getpremi2(a.treaty_type = 'SPL',a.premi_netto)) as premiSPL,"
                + "sum(getpremi2(a.treaty_type = 'BPDAN',a.nd_comm1)) as commBPDAN,"
                + "sum(getpremi2(a.treaty_type = 'FAC',a.nd_comm1)) as commFAC,"
                + "sum(getpremi2(a.treaty_type = 'FACO',a.nd_comm1)) as commFACO,"
                + "sum(getpremi2(a.treaty_type = 'FACO1',a.nd_comm1)) as commFACO1,"
                + "sum(getpremi2(a.treaty_type = 'FACO2',a.nd_comm1)) as commFACO2,"
                + "sum(getpremi2(a.treaty_type = 'FACO3',a.nd_comm1)) as commFACO3,"
                + "sum(getpremi2(a.treaty_type = 'FACP',a.nd_comm1)) as commFACP,"
                + "sum(getpremi2(a.treaty_type = 'JP',a.nd_comm1)) as commJP,"
                + "sum(getpremi2(a.treaty_type = 'KSCBI',a.nd_comm1)) as commKSCBI,"
                + "sum(getpremi2(a.treaty_type = 'OR',a.nd_comm1)) as commOR,"
                + "sum(getpremi2(a.treaty_type = 'PARK',a.nd_comm1)) as commPARK,"
                + "sum(getpremi2(a.treaty_type = 'QS',a.nd_comm1)) as commQS,"
                + "sum(getpremi2(a.treaty_type = 'QSKR',a.nd_comm1)) as commQSKR,"
                + "sum(getpremi2(a.treaty_type = 'SPL',a.nd_comm1)) as commSPL from ( "
                + sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,a.pol_no,a.status,a.premi_total,c.refd1,c.refd2,c.refd3,"
                + "a.period_start,a.period_end,a.ccy_rate ) a where status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        sql = sql + " and (a.premi_netto <> 0 or a.nd_comm1 <> 0) "
                + "group by a.pol_type_id,a.premi_total order by a.pol_type_id "
                + ") b on a.pol_type_id = b.pol_type_id "
                + "group by a.pol_type_id,a.description order by a.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REKAPJENIS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("type ID");
            row0.createCell(1).setCellValue("description");
            row0.createCell(2).setCellValue("premi_total");
            row0.createCell(3).setCellValue("premior");
            row0.createCell(4).setCellValue("premibpdan");
            row0.createCell(5).setCellValue("premiqs");
            row0.createCell(6).setCellValue("premispl");
            row0.createCell(7).setCellValue("premiqskr");
            row0.createCell(8).setCellValue("premijp");
            row0.createCell(9).setCellValue("premikscbi");
            row0.createCell(10).setCellValue("premipark");
            row0.createCell(11).setCellValue("premifac");
            row0.createCell(12).setCellValue("premifaco");
            row0.createCell(13).setCellValue("premifaco1");
            row0.createCell(14).setCellValue("premifaco2");
            row0.createCell(15).setCellValue("premifaco3");
            row0.createCell(16).setCellValue("premifacp");
            row0.createCell(17).setCellValue("commor");
            row0.createCell(18).setCellValue("commbpdan");
            row0.createCell(19).setCellValue("commqs");
            row0.createCell(20).setCellValue("commspl");
            row0.createCell(21).setCellValue("commqskr");
            row0.createCell(22).setCellValue("commjp");
            row0.createCell(23).setCellValue("commkscbi");
            row0.createCell(24).setCellValue("commpark");
            row0.createCell(25).setCellValue("commfac");
            row0.createCell(26).setCellValue("commfaco");
            row0.createCell(27).setCellValue("commfaco1");
            row0.createCell(28).setCellValue("commfaco2");
            row0.createCell(29).setCellValue("commfaco3");
            row0.createCell(30).setCellValue("commfacp");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premior").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("premibpdan").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("premiqs").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("premispl").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premiqskr").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premijp").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premikscbi").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premipark").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premifac").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premifaco").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premifaco1").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premifaco2").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("premifaco3").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("premifacp").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("commor").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("commbpdan").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("commqs").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("commspl").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("commqskr").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("commjp").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("commkscbi").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("commpark").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("commfac").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("commfaco").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("commfaco1").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("commfaco2").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("commfaco3").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("commfacp").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_SUMM_REAS_POLTYPE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + "k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + "sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                                    + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
                        } else {
                            sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                    + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                                    + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
                        }
                    } else {
                        sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                                + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                                + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                                + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
                    }
                } else {
                    sqa.addSelect("	a.pol_id,a.pol_no,i.ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                            + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                            + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                            + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                            + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
                }
            } else {
                sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                        + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                        + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
            }
        } else {
            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,count(a.pol_no) as pol_no,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " k.short_name as cust_name,m.short_name as prod_name,sum(round(i.premi_amount,2)) as premi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_netto,"
                    + " sum(round(i.ricomm_amt,2)) as nd_comm1,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm2,l.ins_treaty_id,"
                    + "sum(round(i.tsi_amount,2)) as tsi_total,sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_netto,l.treaty_name ");
        }

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id "
                + " inner join ins_treaty l on l.ins_treaty_id = g.ins_treaty_id "
                + " left join ent_master m on m.ent_id = i.reins_ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");

        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");
        sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y'");
//        sqa.addClause(" a.pol_no = '044111110419000500' ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stCoinsurerID != null) {
            sqa.addClause("i.member_ent_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("f.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }
        /*
        if (stPolicyTypeGroupID != null) {
        sqa.addClause("a.ins_policy_type_grp_id = ?");
        sqa.addPar(stPolicyTypeGroupID);
        }
        
        if (stPolicyTypeID != null) {
        sqa.addClause("a.pol_type_id = ?");
        sqa.addPar(stPolicyTypeID);
        }*/

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }
        /*
        if (stFltTreatyType != null) {
        if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
        sqa.addClause("j.treaty_type in ('SPL','QS')");
        } else {
        sqa.addClause("j.treaty_type = ?");
        sqa.addPar(stFltTreatyType);
        }
        }*/

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
                if (stFltTreatyType.equalsIgnoreCase("SPL") || stFltTreatyType.equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(stFltTreatyType);
                }
            }
        }

        String sql;

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
                        } else {
                            sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                        }
                    } else {
                        sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                                + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                    }
                } else {
                    sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                            + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
                }
            } else {
                sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                        + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
            }
        } else {
            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,m.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name "
                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
        }

        sql = " select * from ( " + sql + " ) a ";

        if (getPeriodFrom() != null && getStTreatyYearID() != null) {
            throw new RuntimeException("Period Start dan Treaty Year harus diinput salah satu");
        }

        if (getPeriodFrom() != null) {
            sql = sql + " where date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        if (getStTreatyYearID() != null) {
            sql = sql + " where a.ins_treaty_id = ? ";
            sqa.addPar(stTreatyYearID);
        }

        if (getStFltTreatyType() != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {

                if (stPolicyTypeGroupID != null) {
                    if (stPolicyTypeID != null) {
                        if (stPolicyTypeID.equalsIgnoreCase("59")) {
                            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id::text,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,sum(tsi_total) as tsi_total,sum(tsi_netto) as tsi_netto,"
                                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id,a.treaty_name from ( "
                                    + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id,a.treaty_name ";
                        } else {
                            sql = " select a.pol_type_id::text,a.cust_name,a.prod_name,a.pol_no,a.treaty_type,a.treaty_name,a.ccy,a.ccy_rate, "
                                    + "tsi_total,tsi_netto,premi_total,premi_netto,nd_comm1,nd_comm2 "
                                    + "from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                        }
                    } else {
                        sql = " select a.pol_type_id::text,a.cust_name,a.prod_name,a.pol_no,a.treaty_type,a.treaty_name,a.ccy,a.ccy_rate, "
                                + "tsi_total,tsi_netto,premi_total,premi_netto,nd_comm1,nd_comm2 "
                                + "from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                    }
                } else {
                    sql = " select a.pol_type_id::text,a.cust_name,a.prod_name,a.pol_no,a.treaty_type,a.treaty_name,a.ccy,a.ccy_rate, "
                            + "tsi_total,tsi_netto,premi_total,premi_netto,nd_comm1,nd_comm2 "
                            + "from ( " + sql + " ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) ";
                }
            } else {
                sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id::text,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,sum(tsi_total) as tsi_total,sum(tsi_netto) as tsi_netto,"
                        + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id,a.treaty_name from ( "
                        + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                        + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id,a.treaty_name ";
            }
        } else {
            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id::text,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,sum(a.pol_no) as amount,sum(tsi_total) as tsi_total,sum(tsi_netto) as tsi_netto,"
                    + "sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id,a.treaty_name from ( "
                    + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where (a.premi_netto <> 0 or a.nd_comm2 <> 0) "
                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,a.prod_name,a.ins_treaty_id,a.treaty_name ";
        }

        sql = sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_SUMM_REAS_POLTYPE() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("JENIS");
            row0.createCell(1).setCellValue("REINSURER");
            row0.createCell(2).setCellValue("COMPANY");
            row0.createCell(3).setCellValue("TREATY");
            row0.createCell(4).setCellValue("TREATY NAME");
            row0.createCell(5).setCellValue("KURS");
            row0.createCell(6).setCellValue("MATA UANG");
            row0.createCell(7).setCellValue("TSI ORI");
            row0.createCell(8).setCellValue("TSI IDR");
            row0.createCell(9).setCellValue("PREMI ORI");
            row0.createCell(10).setCellValue("PREMI IDR");
            row0.createCell(11).setCellValue("RICOMM ORI");
            row0.createCell(12).setCellValue("RICOMM IDR");
            row0.createCell(13).setCellValue("JUMLAH POLIS");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(getPolicyType(h.getFieldValueByFieldNameST("pol_type_id")).getStShortDescription());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("treaty_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("tsi_total").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tsi_netto").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_netto").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            if (h.getFieldValueByFieldNameBD("amount") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    /**
     * @return the stTreatyYearID
     */
    public String getStTreatyYearID() {
        return stTreatyYearID;
    }

    /**
     * @param stTreatyYearID the stTreatyYearID to set
     */
    public void setStTreatyYearID(String stTreatyYearID) {
        this.stTreatyYearID = stTreatyYearID;
    }

    /**
     * @return the stTreatyYearDesc
     */
    public String getStTreatyYearDesc() {
        return stTreatyYearDesc;
    }

    /**
     * @param stTreatyYearDesc the stTreatyYearDesc to set
     */
    public void setStTreatyYearDesc(String stTreatyYearDesc) {
        this.stTreatyYearDesc = stTreatyYearDesc;
    }

    public DTOList EXCEL_REKAPPREMIREbyJURNAL() throws Exception {
        final boolean REASURADUR = "Y".equalsIgnoreCase((String) refPropMap.get("REASURADUR"));
        final boolean JENAS = "Y".equalsIgnoreCase((String) refPropMap.get("JENAS"));

        final SQLAssembler sqa = new SQLAssembler();

        String gljedetail = null;

        if (journalDateFrom != null) {
            String gljedetailYear = DateUtil.getYear(journalDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }
        } else {
            gljedetail = "gl_je_detail";
        }

        String ket1 = null;
        String ket2 = null;
        if (REASURADUR) {
            ket1 = "a.ref_ent_id,c.short_name,";
            ket2 = "inner join ent_master c on c.ent_id = a.ref_ent_id ";
        } else if (JENAS) {
            ket1 = "d.pol_type_id,c.description,";
            ket2 = "inner join ins_policy_types c on c.pol_type_id = d.pol_type_id ";
        }

        sqa.addSelect(ket1 + " substr(b.accountno,1,4) as akun,"
                + "sum(round(a.debit,2)-round(a.credit,2)) as balance ");

        sqa.addQuery(" from " + gljedetail + " a "
                + "inner join gl_accounts b on b.account_id = a.accountid "
                + "and substr(b.accountno,1,4) in ('6331','6341','7721','7722','7241','7251') and b.acctype is null "
                + "inner join ins_policy d on d.pol_no = a.pol_no and d.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') "
                + ket2);

        if (journalDateFrom != null) {
            sqa.addClause("a.fiscal_year = ? ");
            sqa.addPar(DateUtil.getYear(journalDateFrom));
            sqa.addClause("a.period_no = ? ");
            sqa.addPar(DateUtil.getMonthDigit(journalDateFrom));
        }

        if (journalDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(journalDateFrom);
        }

        if (journalDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(journalDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.ref_ent_id = ?");
            sqa.addPar(stMarketerID);
        }

        String ket3 = null;
        if (REASURADUR) {
            ket3 = "a.ref_ent_id as ref1,a.short_name as ref2,";
        } else if (JENAS) {
            ket3 = "a.pol_type_id as ref1,a.description as ref2,";
        }

        String sql = "select " + ket3
                + "sum(getpremi2(akun in ('6331','6341'),balance)) as premire,"
                + "sum(getpremi2(akun in ('7721','7722'),balance)) as commre,"
                + "sum(getpremi2(akun in ('7241','7251'),balance)) as claimre "
                + "from ( " + sqa.getSQL() + " group by 1,2,3 order by 1 "
                + ") a group by 1,2 order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REKAPPREMIREbyJURNAL() throws Exception {
        final boolean REASURADUR = "Y".equalsIgnoreCase((String) refPropMap.get("REASURADUR"));
        final boolean JENAS = "Y".equalsIgnoreCase((String) refPropMap.get("JENAS"));

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        String ket1 = null;
        if (REASURADUR) {
            ket1 = "Reasuradur";
        } else if (JENAS) {
            ket1 = "COB";
        }

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowH = sheet.createRow(0);
            rowH.createCell(0).setCellValue("Company / COB : " + getStMarketerName() + " - " + getStPolicyTypeDesc() + " - " + getStBranchName());

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("ID");
            row0.createCell(1).setCellValue(ket1);
            row0.createCell(2).setCellValue("Premi Reas");
            row0.createCell(3).setCellValue("Komisi Reas");
            row0.createCell(4).setCellValue("Klaim Reas");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("ref1").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ref2")));
            if (h.getFieldValueByFieldNameBD("premire") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("premire").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("commre") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("commre").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claimre") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claimre").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public String getYearPosting() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(" select years from gl_posting where final_flag = 'Y' "
                    + "order by gl_post_id desc limit 1 ");

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void EXCEL_RISK_CONTROL_KARK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,quote_ident(a.pol_no) as pol_no,a.ref1 as lph,a.cust_name as tertanggung,a.period_start,a.period_end,b.ref1 as penggunaan,"
                + "b.ref2 as koderesiko,b.ref5 as alamat,b.ref4d as penerangan,b.ref7d as kodepasar,b.ref9d as kodepos, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=578,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as bangunan, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=579,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as gangguan, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=580,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as hakpakai, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=581,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as renovasi, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=582,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as kontrak, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=583,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as garansikredit, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=584,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as peralatan, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=585,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as stokbarang, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id=586,z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as mesin, "
                + "(   select sum(getkoas(z.ins_tsi_cat_id in (587,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38),z.insured_amount)) "
                + "from ins_pol_tsi z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as others, "
                + "(   select sum(getkoas(z.ins_cover_id in (269),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_fire,  "
                + "(   select sum(getkoas(z.ins_cover_id in (270),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_rsmd,  "
                + "(   select sum(getkoas(z.ins_cover_id in (271),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_huruhara,  "
                + "(   select sum(getkoas(z.ins_cover_id in (272),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_rsmd_huruhara,  "
                + "(   select sum(getkoas(z.ins_cover_id in (273),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_eqvet,  "
                + "(   select sum(getkoas(z.ins_cover_id in (274),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_flood,  "
                + "(   select sum(getkoas(z.ins_cover_id in (275),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_landslide,  "
                + "(   select sum(getkoas(z.ins_cover_id in (276),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_terrorism,  "
                + "(   select sum(getkoas(z.ins_cover_id in (277),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_veh_impact,  "
                + "(   select sum(getkoas(z.ins_cover_id in (278),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_time_excess,  "
                + "(   select sum(getkoas(z.ins_cover_id in (279),z.rate)) "
                + "from ins_pol_cover z "
                + "where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_removal_debris, "
                + "(   select sum(getkoas(z.ins_cover_id in (28,90,91,92,93,94,22,29,114,217,253,270,273,274,269,269,270,271,272,273,274,275,276,277,278,216,279),z.rate)) "
                + "from ins_pol_cover z where z.ins_pol_obj_id=b.ins_pol_obj_id) as rate_all, "
                + "(   select sum(getkoas(z.ins_cover_id in (28,90,91,92,93,94,22,29,114,217,253,270,273,274,269,269,270,271,272,273,274,275,276,277,278,216,279),z.premi)) "
                + "from ins_pol_cover z where z.ins_pol_obj_id=b.ins_pol_obj_id) as premi ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') ");
        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.ins_policy_type_grp_id = 12 and a.pol_type_id = 2");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
//            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
//            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
//            sqa.addPar(stRegion);
        }

        String sql = sqa.getSQL() + " order by a.policy_date,a.pol_id,b.ins_pol_obj_id ";

        SQLUtil S = new SQLUtil();

        String nama_file = "kark_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

    }

    public DTOList SOA_TREATY_OLD3() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String sqlPremi = "select a.treaty_type,a.description,"
                + "sum(tsi_total) as tsi_total,sum(tsi_netto) as tsi_netto,sum(premi_total) as premi_total,sum(premi_netto) as premi_netto,"
                + "sum(coalesce(nd_comm1,0)) as nd_comm1,sum(coalesce(nd_comm2,0)) as nd_comm2,a.ins_treaty_id,a.treaty_name from ( "
                + "select j.treaty_type,f.desc_soa as description,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,  "
                + "sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_total,sum(round(i.premi_amount,2)*a.ccy_rate) as premi_total,sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm1,"
                + "sum(getpremi2(i.member_ent_id = " + stCoinsurerID + ",round(i.tsi_amount,2)*a.ccy_rate)) as tsi_netto,"
                + "sum(getpremi2(i.member_ent_id = " + stCoinsurerID + ",round(i.premi_amount,2)*a.ccy_rate)) as premi_netto,"
                + "sum(getpremi2(i.member_ent_id = " + stCoinsurerID + ",round(i.ricomm_amt,2)*a.ccy_rate)) as nd_comm2,"
                + "l.ins_treaty_id,l.treaty_name,i.member_ent_id,k.short_name as cust_name  "
                + "from ins_policy a  "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id and c.ins_pol_obj_id > 9999 "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id  "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id  "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id  "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id  "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "inner join ins_treaty l on l.ins_treaty_id = g.ins_treaty_id  "
                + "inner join ins_policy_type_grp d on d.ins_policy_type_grp_id = f.ins_pol_type_grp_soa "
                + "inner join s_valueset m on m.vs_code = l.treaty_grp_id and m.vs_group = 'TREATY_GROUP' "
                + "where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') "
                + "and a.active_flag='Y' and a.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date) >= '" + policyDateFrom + "' ";
//            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
//            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date) <= '" + policyDateTo + "' ";
//            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
//            sqa.addPar(policyDateTo);
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and j.treaty_type in ('SPL','QS') ";
//                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqlPremi = sqlPremi + " and j.treaty_type = '" + stFltTreatyType + "' ";
//                sqa.addClause("j.treaty_type = ?");
//                sqa.addPar(stFltTreatyType);
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
//                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
//                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (getStPolTypeGrpSoaID() != null) {
            sqlPremi = sqlPremi + " and f.ins_pol_type_grp_soa = " + stPolTypeGrpSoaID;
//            sqa.addPar(stTreatyYearID);
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and l.treaty_grp_id = '" + stTreatyYearGrpID + "'";
//            sqa.addPar(stTreatyYearID);
        }

//        if (getStCoinsurerID() != null) {
//            sqlPremi = sqlPremi + " and i.member_ent_id = " + stCoinsurerID;
////            sqa.addClause("i.member_ent_id = ?");
////            sqa.addPar(stCoinsurerID);
//        }

        sqlPremi = sqlPremi + " group by j.treaty_type,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.pol_type_id,f.desc_soa,"
                + "c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name,i.member_ent_id,k.short_name "
                + " ) a where a.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') ";

        if (getStTreatyYearID() != null) {
            sqlPremi = sqlPremi + " and a.ins_treaty_id = " + stTreatyYearID;
//            sqa.addPar(stTreatyYearID);
        }

        sqlPremi = sqlPremi + " group by a.treaty_type,a.description,a.ins_treaty_id,a.treaty_name "
                + "order by a.description,a.treaty_type ";

        String sqlKlaim = " select a.treaty_type,a.description,sum(claim_amount) as claim_amount,sum(claim_amount_e) as claim_amount_e,a.ins_treaty_id,a.treaty_name "
                + "from ( select j.treaty_type,f.desc_soa as description,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.ccy_rate_claim,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end, "
                + "sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount,"
                + "sum(getpremi2(i.member_ent_id = " + stCoinsurerID + ",round(i.claim_amount,2)*a.ccy_rate_claim)) as claim_amount_e,"
                + "l.ins_treaty_id,l.treaty_name,i.member_ent_id,k.short_name as cust_name "
                + "from ins_policy a  "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id  "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id  "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id  "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id   "
                + "inner join ins_treaty l on l.ins_treaty_id = g.ins_treaty_id  "
                + "where  a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' and j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') "
                + "and a.active_flag='Y' and a.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.claim_approved_date) >= '" + policyDateFrom + "' ";
//            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
//            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.claim_approved_date) <= '" + policyDateTo + "' ";
//            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
//            sqa.addPar(policyDateTo);
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlKlaim = sqlKlaim + " and j.treaty_type in ('SPL','QS') ";
//                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqlKlaim = sqlKlaim + " and j.treaty_type = '" + stFltTreatyType + "' ";
//                sqa.addClause("j.treaty_type = ?");
//                sqa.addPar(stFltTreatyType);
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
//                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
//                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (getStPolTypeGrpSoaID() != null) {
            sqlKlaim = sqlKlaim + " and f.ins_pol_type_grp_soa = " + stPolTypeGrpSoaID;
//            sqa.addPar(stTreatyYearID);
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and l.treaty_grp_id = '" + stTreatyYearGrpID + "'";
//            sqa.addPar(stTreatyYearID);
        }

//        if (getStCoinsurerID() != null) {
//            sqlKlaim = sqlKlaim + " and i.member_ent_id = " + stCoinsurerID;
////            sqa.addClause("i.member_ent_id = ?");
////            sqa.addPar(stCoinsurerID);
//        }

        sqlKlaim = sqlKlaim + " group by j.treaty_type,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.ccy_rate_claim,a.pol_type_id,f.desc_soa,"
                + "c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,l.ins_treaty_id,l.treaty_name,i.member_ent_id,k.short_name "
                + " ) a where a.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') ";

        if (getStTreatyYearID() != null) {
            sqlKlaim = sqlKlaim + " and a.ins_treaty_id = " + stTreatyYearID;
//            sqa.addPar(stTreatyYearID);
        }

        sqlKlaim = sqlKlaim + " group by a.treaty_type,a.description,a.ins_treaty_id,a.treaty_name "
                + "order by a.treaty_type,a.description  ";

        sqa.addSelect(" a.treaty_type as ref1,a.description as ref2,a.tsi_total as insured_amount,a.premi_total,a.nd_comm1,coalesce(b.claim_amount,0) as claim_amount,"
                + "a.tsi_netto as insured_amount_e,a.premi_netto,a.nd_comm2,coalesce(b.claim_amount_e,0) as claim_amount_endorse ");

        sqa.addQuery(" from ( " + sqlPremi + " ) a "
                + "left join ( " + sqlKlaim + " ) b on b.treaty_type = a.treaty_type and b.description = a.description ");

        String sql = sqa.getSQL() + " order by a.treaty_type,a.description ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_FORMAT_EXCEL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,B.ORDER_NO,b.ref1,b.refd1 as tgl_awal,b.refd2 as tgl_akhir,f.treaty_type,g.ent_name,b.insured_amount, "
                + "  b.premi_total, ins_pol_ri_id,e.ins_pol_treaty_id ,e.ins_pol_tre_det_id ,e.ins_treaty_detail_id ,e.ins_treaty_shares_id ,e.member_ent_id , "
                + "  e.sharepct ,e.premi_amount ,e.or_flag ,e.tsi_amount ,e.premi_rate ,e.auto_rate_flag,e.use_rate_flag ,e.ricomm_rate ,e.ricomm_amt ,e.ri_slip_no ,e.valid_ri_date,a.status");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "   inner join ins_pol_obj b on a.pol_id = b.pol_id " + clauseObj
                + "    inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id "
                + "    inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id "
                + "    inner join ins_pol_ri e on d.ins_pol_tre_det_id = e.ins_pol_tre_det_id "
                + "    inner join ent_master g on e.member_ent_id = g.ent_id");


        if (getStStatus() != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(getStStatus());
        } else {
            sqa.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        }

        sqa.addClause(" a.active_flag = 'Y'");

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqa.addClause("a.pol_no = ?");
                sqa.addPar(stPolicyNo);
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqa.addClause(polno);
            }
        }

        sqa.addOrder("A.POL_ID,B.INS_POL_OBJ_ID,e.valid_ri_date,f.ins_treaty_detail_id");

        final DTOList l = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" a.pol_no,a.policy_date,a.period_start,f.treaty_type,b.insured_amount,b.ref1,b.refd1 as tgl_awal,b.refd2 as tgl_akhir,d.*");

        sqa2.addQuery("from ins_policy a "
                + "  inner join ins_pol_obj b on a.pol_id = b.pol_id "
                + "  inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id "
                + "  inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id "
                + "  inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id");

        if (getStStatus() != null) {
            sqa2.addClause("a.status = ?");
            sqa2.addPar(getStStatus());
        } else {
            sqa2.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        }

        //sqa2.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa2.addClause(" a.active_flag = 'Y'");

//        if (stPolicyNo != null) {
//            sqa.addClause("a.pol_no like ?");
//            sqa.addPar('%' + stPolicyNo + '%');
//        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqa2.addClause("a.pol_no = ?");
                sqa2.addPar(stPolicyNo);
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqa2.addClause(polno);
            }
        }

        sqa2.addOrder("A.POL_ID,B.INS_POL_OBJ_ID,f.ins_treaty_detail_id");

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sqa2.getSQL(),
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;
    }

    public void EXPORT_FORMAT_EXCEL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet detil
        XSSFSheet sheet = wb.createSheet("detil");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("policy_date");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("treaty_type");
            row0.createCell(4).setCellValue("insured_amount");
            row0.createCell(5).setCellValue("ref1");
            row0.createCell(6).setCellValue("tgl_awal");
            row0.createCell(7).setCellValue("tgl_akhir");
            row0.createCell(8).setCellValue("ins_pol_tre_det_id");
            row0.createCell(9).setCellValue("ins_pol_treaty_id");
            row0.createCell(10).setCellValue("ins_treaty_detail_id");
            row0.createCell(11).setCellValue("tsi_amount");
            row0.createCell(12).setCellValue("premi_amount");
            row0.createCell(13).setCellValue("comm_rate");
            row0.createCell(14).setCellValue("comm_amt");
            row0.createCell(15).setCellValue("treaty_limit");
            row0.createCell(16).setCellValue("treaty_limit_ratio");
            row0.createCell(17).setCellValue("parent_id");
            row0.createCell(18).setCellValue("tsi_pct");
            row0.createCell(19).setCellValue("premi_rate");
            row0.createCell(20).setCellValue("base_tsi_amount");
            row0.createCell(21).setCellValue("claim_amount");
            row0.createCell(22).setCellValue("edit_flag");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            if (h.getFieldValueByFieldNameDT("tgl_awal") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgl_awal"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_akhir") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tgl_akhir"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_treaty_id").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ins_treaty_detail_id").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            if (h.getFieldValueByFieldNameBD("comm_rate") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("comm_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_amt") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("comm_amt").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("treaty_limit") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("treaty_limit_ratio") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit_ratio").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("parent_id") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("parent_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_pct") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("tsi_pct").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_rate") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("base_tsi_amount") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("base_tsi_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("edit_flag") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("edit_flag"));
            }
        }

        //bikin sheet detil
        XSSFSheet sheet2 = wb.createSheet("member");

        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int j = 0; j < list2.size(); j++) {
            HashDTO h = (HashDTO) list2.get(j);

            //bikin header
            XSSFRow row02 = sheet2.createRow(0);
            row02.createCell(0).setCellValue("pol_no");
            row02.createCell(1).setCellValue("order_no");
            row02.createCell(2).setCellValue("ref1");
            row02.createCell(3).setCellValue("tgl_awal");
            row02.createCell(4).setCellValue("tgl_akhir");
            row02.createCell(5).setCellValue("treaty_type");
            row02.createCell(6).setCellValue("ent_name");
            row02.createCell(7).setCellValue("insured_amount");
            row02.createCell(8).setCellValue("premi_total");
            row02.createCell(9).setCellValue("ins_pol_ri_id");
            row02.createCell(10).setCellValue("ins_pol_tre_det_id");
            row02.createCell(11).setCellValue("ins_pol_treaty_id");
            row02.createCell(12).setCellValue("ins_treaty_detail_id");
            row02.createCell(13).setCellValue("ins_treaty_shares_id");
            row02.createCell(14).setCellValue("member_ent_id");
            row02.createCell(15).setCellValue("sharepct");
            row02.createCell(16).setCellValue("premi_amount");
            row02.createCell(17).setCellValue("or_flag");
            row02.createCell(18).setCellValue("tsi_amount");
            row02.createCell(19).setCellValue("premi_rate");
            row02.createCell(20).setCellValue("auto_rate_flag");
            row02.createCell(21).setCellValue("use_rate_flag");
            row02.createCell(22).setCellValue("ricomm_rate");
            row02.createCell(23).setCellValue("ricomm_amt");
            row02.createCell(24).setCellValue("ri_slip_no");
            row02.createCell(25).setCellValue("valid_ri_date");
            row02.createCell(26).setCellValue("status");

            //bikin isi cell
            XSSFRow row2 = sheet2.createRow(j + 1);
            row2.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row2.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row2.createCell(2).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            if (h.getFieldValueByFieldNameDT("tgl_awal") != null) {
                row2.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tgl_awal"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_akhir") != null) {
                row2.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("tgl_akhir"));
            }
            row2.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row2.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row2.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row2.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            row2.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_ri_id").doubleValue());
            if (h.getFieldValueByFieldNameBD("ins_pol_tre_det_id") != null) {
                row2.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ins_pol_treaty_id") != null) {
                row2.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_treaty_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ins_treaty_detail_id") != null) {
                row2.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ins_treaty_detail_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ins_treaty_shares_id") != null) {
                row2.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ins_treaty_shares_id").doubleValue());
            }
            row2.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("member_ent_id").doubleValue());
            if (h.getFieldValueByFieldNameBD("sharepct") != null) {
                row2.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("sharepct").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_amount") != null) {
                row2.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("or_flag") != null) {
                row2.createCell(17).setCellValue(h.getFieldValueByFieldNameST("or_flag"));
            }
            if (h.getFieldValueByFieldNameBD("tsi_amount") != null) {
                row2.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_rate") != null) {
                row2.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("auto_rate_flag") != null) {
                row2.createCell(20).setCellValue(h.getFieldValueByFieldNameST("auto_rate_flag"));
            }
            if (h.getFieldValueByFieldNameST("use_rate_flag") != null) {
                row2.createCell(21).setCellValue(h.getFieldValueByFieldNameST("use_rate_flag"));
            }
            if (h.getFieldValueByFieldNameBD("ricomm_rate") != null) {
                row2.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("ricomm_rate").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ricomm_amt") != null) {
                row2.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("ricomm_amt").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ri_slip_no") != null) {
                row2.createCell(24).setCellValue(h.getFieldValueByFieldNameST("ri_slip_no"));
            }
            if (h.getFieldValueByFieldNameDT("valid_ri_date") != null) {
                row2.createCell(25).setCellValue(h.getFieldValueByFieldNameDT("valid_ri_date"));
            }
            row2.createCell(26).setCellValue(h.getFieldValueByFieldNameST("status"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    /**
     * @return the stPolTypeGrpSoaID
     */
    public String getStPolTypeGrpSoaID() {
        return stPolTypeGrpSoaID;
    }

    /**
     * @param stPolTypeGrpSoaID the stPolTypeGrpSoaID to set
     */
    public void setStPolTypeGrpSoaID(String stPolTypeGrpSoaID) {
        this.stPolTypeGrpSoaID = stPolTypeGrpSoaID;
    }

    /**
     * @return the stPolTypeGrpSoaDesc
     */
    public String getStPolTypeGrpSoaDesc() {
        return stPolTypeGrpSoaDesc;
    }

    /**
     * @param stPolTypeGrpSoaDesc the stPolTypeGrpSoaDesc to set
     */
    public void setStPolTypeGrpSoaDesc(String stPolTypeGrpSoaDesc) {
        this.stPolTypeGrpSoaDesc = stPolTypeGrpSoaDesc;
    }

    /**
     * @return the stTreatyYearGrpID
     */
    public String getStTreatyYearGrpID() {
        return stTreatyYearGrpID;
    }

    /**
     * @param stTreatyYearGrpID the stTreatyYearGrpID to set
     */
    public void setStTreatyYearGrpID(String stTreatyYearGrpID) {
        this.stTreatyYearGrpID = stTreatyYearGrpID;
    }

    /**
     * @return the stTreatyYearGrpDesc
     */
    public String getStTreatyYearGrpDesc() {
        return stTreatyYearGrpDesc;
    }

    /**
     * @param stTreatyYearGrpDesc the stTreatyYearGrpDesc to set
     */
    public void setStTreatyYearGrpDesc(String stTreatyYearGrpDesc) {
        this.stTreatyYearGrpDesc = stTreatyYearGrpDesc;
    }

    public DTOList SOA_INVOICE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        //PREMI
        sqa.addSelect("	a.ar_invoice_id,a.ar_cust_id,a.refc1 as treaty_type,b.ent_name as attr_pol_name,a.attr_pol_type_id,c.desc_soa as description,a.no_surat_hutang,a.amount as premi_netto,"
                + "(select sum(d.amount) from ar_invoice_details d where d.ar_invoice_id = a.ar_invoice_id and d.ar_trx_line_id = 66) as premi_total,"
                + "(select sum(d.amount) from ar_invoice_details d where d.ar_invoice_id = a.ar_invoice_id and d.ar_trx_line_id = 67) as nd_comm1  ");

        sqa.addQuery("from ar_invoice a "
                + "inner join ent_master b on b.ent_id = a.ar_cust_id "
                + "inner join ins_policy_types c on c.pol_type_id = a.attr_pol_type_id "
                + "inner join ar_invoice_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause(" a.posted_flag = 'Y'");
//        sqa.addClause(" a.attr_pol_no = '010120020123031600'");

        String sqlClosingP = "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'PREMIUM_RI_OUTWARD' and a.treaty_type in ('QS','SPL','FACO1')) or (a.closing_type = 'PREMIUM_RI_INWARD_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlClosingP = sqlClosingP + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlClosingP = sqlClosingP + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlClosingP = sqlClosingP + " order by no_surat_hutang ";

        sqa.addClause(" a.no_surat_hutang in (" + sqlClosingP + ")");

        String sqlPremi = "select a.ar_cust_id,a.treaty_type as cust_address,attr_pol_name,description,"
                + "sum(premi_netto) as premi_netto,sum(premi_total) as premi_total,sum(nd_comm1) as nd_comm1 from ( "
                + sqa.getSQL() + " group by a.ar_invoice_id,a.ar_cust_id,a.refc1,b.ent_name,a.due_date,a.attr_pol_type_id,c.desc_soa,a.no_surat_hutang,a.amount order by a.ar_cust_id,a.attr_pol_type_id "
                + " ) a where a.no_surat_hutang is not null ";

        if (getStEntityID() != null) {
            sqlPremi = sqlPremi + " and a.ar_cust_id = " + stEntityID;
        }

        sqlPremi = sqlPremi + " group by a.ar_cust_id,treaty_type,attr_pol_name,description order by a.description,a.ar_cust_id,a.treaty_type ";

        //KLAIM
        final SQLAssembler sqa2 = new SQLAssembler();
        sqa2.addSelect(" a.ar_invoice_id,a.ar_cust_id,a.refc1 as treaty_type,b.ent_name as attr_pol_name,a.attr_pol_type_id,c.desc_soa as description,a.no_surat_hutang,sum(a.amount) as claim_amount ");

        sqa2.addQuery("from ar_invoice a "
                + "inner join ent_master b on b.ent_id = a.ar_cust_id "
                + "inner join ins_policy_types c on c.pol_type_id = a.attr_pol_type_id ");

        sqa2.addClause(" a.posted_flag = 'Y'");

        String sqlClosingK = "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'CLAIM_RI_OUTWARD' and a.treaty_type in ('QS','SPL','FACO1')) or (a.closing_type = 'CLAIM_RI_INWARD_TO_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlClosingK = sqlClosingK + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlClosingK = sqlClosingK + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlClosingK = sqlClosingK + " order by no_surat_hutang ";

        sqa2.addClause(" a.no_surat_hutang in (" + sqlClosingK + ")");

        String sqlKlaim = "select a.ar_cust_id,a.treaty_type as cust_address,attr_pol_name,description,sum(claim_amount) as claim_amount from ( "
                + sqa2.getSQL() + " group by a.ar_invoice_id,a.ar_cust_id,a.refc1,b.ent_name,a.due_date,a.attr_pol_type_id,c.desc_soa,a.no_surat_hutang order by a.ar_cust_id,a.attr_pol_type_id "
                + " ) a where a.no_surat_hutang is not null ";

        if (getStEntityID() != null) {
            sqlKlaim = sqlKlaim + " and a.ar_cust_id = " + stEntityID;
        }

        sqlKlaim = sqlKlaim + " group by a.ar_cust_id,treaty_type,attr_pol_name,description order by a.description,a.ar_cust_id,a.treaty_type ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sqlPremi,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sqlKlaim,
                sqa2.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public DTOList SOA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        //PREMI
        sqa.addSelect("a.grupcob as cust_address,a.treaty_grp_id,a.treaty_type as description,a.member_ent_id,a.reins_ent_id,"
                + "sum(a.tsi_reins) as insured_amount,sum(a.premi_reins) as premi_total,sum(a.comm_reins) as nd_comm1 ");

        sqa.addQuery("from ins_pol_ri_treaty a ");

        String sqlClosingP = "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'PREMIUM_RI_OUTWARD' and a.treaty_type in ('QS','SPL','FACO1')) or (a.closing_type = 'PREMIUM_RI_INWARD_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlClosingP = sqlClosingP + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlClosingP = sqlClosingP + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlClosingP = sqlClosingP + " order by no_surat_hutang ";

        sqa.addClause(" a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in (" + sqlClosingP + ") group by 1 ) ");
        sqa.addClause(" a.treaty_type in ('QS','SPL','FACO1')");

        if (stEntityID != null) {
            sqa.addClause(" a.member_ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stReinsID != null) {
            sqa.addClause(" a.reins_ent_id = ?");
            sqa.addPar(stReinsID);
        }

        if (stTreatyYearGrpID != null) {
            sqa.addClause(" a.treaty_grp_id = ?");
            sqa.addPar(stTreatyYearGrpID);
        }

        String sql = sqa.getSQL() + " group by a.grupcob,a.treaty_grp_id,a.treaty_type,a.member_ent_id,a.reins_ent_id order by a.treaty_grp_id,a.grupcob,a.treaty_type ";

        //KLAIM
        final SQLAssembler sqa2 = new SQLAssembler();
        sqa2.addSelect(" a.grupcob as cust_address,a.treaty_grp_id,a.treaty_type as description,a.member_ent_id,a.reins_ent_id,sum(a.claim_reins) as claim_amount ");

        sqa2.addQuery("from ins_pol_ri_treaty a ");

        String sqlClosingK = "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'CLAIM_RI_OUTWARD' and a.treaty_type in ('QS','SPL','FACO1')) or (a.closing_type = 'CLAIM_RI_INWARD_TO_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlClosingK = sqlClosingK + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlClosingK = sqlClosingK + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlClosingK = sqlClosingK + " order by no_surat_hutang ";

        sqa2.addClause(" a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in (" + sqlClosingK + ") group by 1 ) ");
        sqa2.addClause(" a.treaty_type in ('QS','SPL','FACO1')");
        sqa2.addClause(" coalesce(a.claim_cash_call_f,'N') <> 'Y' ");

        if (stEntityID != null) {
            sqa2.addClause(" a.member_ent_id = ?");
            sqa2.addPar(stEntityID);
        }

        if (stReinsID != null) {
            sqa2.addClause(" a.reins_ent_id = ?");
            sqa2.addPar(stReinsID);
        }

        if (stTreatyYearGrpID != null) {
            sqa2.addClause(" a.treaty_grp_id = ?");
            sqa2.addPar(stTreatyYearGrpID);
        }

        String sql2 = sqa2.getSQL() + " group by a.grupcob,a.treaty_grp_id,a.treaty_type,a.member_ent_id,a.reins_ent_id order by a.treaty_grp_id,a.grupcob,a.treaty_type ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public DTOList SOA_TREATY() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

//        String sqlPremi = "select a.treaty_grp_id,b.group_name as ref2,a.treaty_type as ref1,"
        String sqlPremi = "select a.treaty_grp_id,a.ins_treaty_id,(case when c.ref2 = 'MULTIYEARS' then b.group_name||' Multiyears' else b.group_name end) as ref2,a.treaty_type as ref1,"
                + "sum(a.tsi_reins*a.ccy_rate) as insured_amount,sum(a.premi_reins*a.ccy_rate) as premi_total,sum(a.comm_reins*a.ccy_rate) as nd_comm1,"
                + "sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.tsi_reins*a.ccy_rate)) as insured_amount_e,"
                + "sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.premi_reins*a.ccy_rate)) as premi_netto,"
                + "sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.comm_reins*a.ccy_rate)) as nd_comm2 "
                + "from ins_pol_ri_treaty a inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + "left join ins_treaty c on c.ins_treaty_id = a.ins_treaty_id and c.ref2 = 'MULTIYEARS' "
                + "where a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and a.treaty_type in ('QS','SPL') ";
//                + "where a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in ( "
//                + "select no_surat_hutang from ins_gl_closing a "
//                + "where ((a.closing_type = 'PREMIUM_RI_OUTWARD' and a.treaty_type in ('QS','SPL')) or (a.closing_type = 'PREMIUM_RI_INWARD_OUTWARD')) ";
//
//        if (getPolicyDateFrom() != null) {
//            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
//        }
//
//        if (getPolicyDateTo() != null) {
//            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
//        }
//        sqlPremi = sqlPremi + " order by no_surat_hutang ) group by 1 ) and a.treaty_type in ('QS','SPL') ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlPremi = sqlPremi + " and " + polno;
        }

        if (policyTypeList != null) {
            sqlPremi = sqlPremi + " and a.pol_type_id in (" + policyTypeList + ")";
        } else {

            if (stPolicyTypeGroupID != null) {
                sqlPremi = sqlPremi + " and a.ins_policy_type_grp_id = " + stPolicyTypeGroupID;
            }

            if (stPolicyTypeID != null) {
                sqlPremi = sqlPremi + " and a.pol_type_id = " + stPolicyTypeID;
            }
        }

        sqlPremi = "select a.treaty_grp_id,a.ref2,a.ref1,sum(a.insured_amount) as insured_amount,sum(a.premi_total) as premi_total,sum(a.nd_comm1) as nd_comm1,"
                + "sum(a.insured_amount_e) as insured_amount_e,sum(a.premi_netto) as premi_netto,sum(a.nd_comm2) as nd_comm2 from ( "
                + sqlPremi + " group by a.treaty_grp_id,a.ins_treaty_id,b.group_name,a.treaty_type,c.ref2 order by b.group_name,a.treaty_type "
                + " ) a group by a.treaty_grp_id,a.ref2,a.ref1 order by a.ref2,a.treaty_grp_id,a.ref1 ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sqlPremi,
                sqaP.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

//        String sqlKlaim = "select a.treaty_grp_id,b.group_name as ref2,a.treaty_type as ref1,"
        String sqlKlaim = "select a.treaty_grp_id,a.ins_treaty_id,(case when c.ref2 = 'MULTIYEARS' then b.group_name||' Multiyears' else b.group_name end) as ref2,a.treaty_type as ref1,"
                + "sum(a.claim_reins*a.ccy_rate_claim) as claim_amount,"
                + "sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.claim_reins*a.ccy_rate_claim)) as claim_amount_endorse "
                + "from ins_pol_ri_treaty a inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + "left join ins_treaty c on c.ins_treaty_id = a.ins_treaty_id and c.ref2 = 'MULTIYEARS' "
                + "where a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') and a.treaty_type in ('QS','SPL') and coalesce(a.claim_cash_call_f,'N') <> 'Y'  ";
//                + "where a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in ( "
//                + "select no_surat_hutang from ins_gl_closing a "
//                + "where ((a.closing_type = 'CLAIM_RI_OUTWARD' and a.treaty_type in ('QS','SPL')) or (a.closing_type = 'CLAIM_RI_INWARD_TO_OUTWARD')) ";
//
//        if (getPolicyDateFrom() != null) {
//            sqlKlaim = sqlKlaim + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
//        }
//
//        if (getPolicyDateTo() != null) {
//            sqlKlaim = sqlKlaim + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
//        }
//        sqlKlaim = sqlKlaim + " order by no_surat_hutang ) group by 1 ) and a.treaty_type in ('QS','SPL') and coalesce(a.claim_cash_call_f,'N') <> 'Y' ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlKlaim = sqlKlaim + " and " + polno;
        }

        if (policyTypeList != null) {
            sqlKlaim = sqlKlaim + " and a.pol_type_id in (" + policyTypeList + ")";
        } else {

            if (stPolicyTypeGroupID != null) {
                sqlKlaim = sqlKlaim + " and a.ins_policy_type_grp_id = " + stPolicyTypeGroupID;
            }

            if (stPolicyTypeID != null) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id = " + stPolicyTypeID;
            }
        }
        sqlKlaim = "select a.treaty_grp_id,a.ref2,a.ref1,sum(a.claim_amount) as claim_amount,sum(a.claim_amount_endorse) as claim_amount_endorse from ( "
                + sqlKlaim + " group by a.treaty_grp_id,a.ins_treaty_id,b.group_name,a.treaty_type,c.ref2 order by b.group_name,a.treaty_type "
                + ") a group by a.treaty_grp_id,a.ref2,a.ref1 order by a.ref2,a.treaty_grp_id,a.ref1 ";

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sqlKlaim,
                sqaK.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

//        sqa.addSelect(" a.grupcob as ref2,a.treaty_type as ref1,a.insured_amount,a.premi_total,a.nd_comm1,coalesce(b.claim_total,0) as claim_amount,"
//                + "a.tsi_reins as insured_amount_e,a.premi_reins as premi_netto,a.comm_reins as nd_comm2,coalesce(b.claim_reins,0) as claim_amount_endorse ");
//
//        sqa.addQuery(" from ( " + sqlPremi + " ) a "
//                + "left join ( " + sqlKlaim + " ) b on b.treaty_type = a.treaty_type and b.grupcob = a.grupcob ");
//
//        String sql = sqa.getSQL() + " order by a.grupcob,a.treaty_type ";
//
//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                InsurancePolicyView.class);
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return premi;
    }

    /**
     * @return the stPolJenas
     */
    public String getStPolJenas() {
        return stPolJenas;
    }

    /**
     * @param stPolJenas the stPolJenas to set
     */
    public void setStPolJenas(String stPolJenas) {
        this.stPolJenas = stPolJenas;
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

    public DTOList EXCEL_SOA_TREATY_OLD() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.treaty_grp_id,b.group_name as ref2,a.treaty_type as ref1,a.member_ent_id,a.reins_ent_id,"
                + "sum(a.tsi_reins*a.ccy_rate) as insured_amount,sum(a.premi_reins*a.ccy_rate) as premi_total,sum(a.comm_reins*a.ccy_rate) as nd_comm1 "
                + "from ins_pol_ri_treaty a inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + "where a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in ( "
                + "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'PREMIUM_RI_OUTWARD' and a.treaty_type in ('QS','SPL')) or (a.closing_type = 'PREMIUM_RI_INWARD_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlPremi = sqlPremi + " order by no_surat_hutang ) group by 1 ) and a.treaty_type in ('QS','SPL') ";

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlPremi = sqlPremi + " and " + polno;
        }
        sqlPremi = sqlPremi + " group by a.treaty_grp_id,b.group_name,a.treaty_type,a.member_ent_id,a.reins_ent_id order by b.group_name,a.treaty_type,a.member_ent_id,a.reins_ent_id ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sqlPremi,
                sqaP.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.treaty_grp_id,b.group_name as ref2,a.treaty_type as ref1,a.member_ent_id,a.reins_ent_id,"
                + "sum(a.claim_reins*a.ccy_rate_claim) as claim_amount "
                + "from ins_pol_ri_treaty a inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + "where a.pol_id in ( select a.attr_pol_id from ar_invoice a where a.no_surat_hutang in ( "
                + "select no_surat_hutang from ins_gl_closing a "
                + "where ((a.closing_type = 'CLAIM_RI_OUTWARD' and a.treaty_type in ('QS','SPL')) or (a.closing_type = 'CLAIM_RI_INWARD_TO_OUTWARD')) ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.policy_date_start) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.policy_date_start) <= '" + policyDateTo + "' ";
        }
        sqlKlaim = sqlKlaim + " order by no_surat_hutang ) group by 1 ) and a.treaty_type in ('QS','SPL') and coalesce(a.claim_cash_call_f,'N') <> 'Y'  ";

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlKlaim = sqlKlaim + " and " + polno;
        }
        sqlKlaim = sqlKlaim + " group by a.treaty_grp_id,b.group_name,a.treaty_type,a.member_ent_id,a.reins_ent_id order by b.group_name,a.treaty_type,a.member_ent_id,a.reins_ent_id ";

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sqlKlaim,
                sqaK.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public void EXPORT_SOA_TREATY_OLD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("Premi");
        XSSFSheet sheetK = wb.createSheet("Klaim");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("status");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("period_end");
            row0.createCell(4).setCellValue("policy_date");
            row0.createCell(5).setCellValue("ri_slip_no");
            row0.createCell(6).setCellValue("bisnis");
            row0.createCell(7).setCellValue("ent_id");
            row0.createCell(8).setCellValue("name");
            row0.createCell(9).setCellValue("ccy");
            row0.createCell(10).setCellValue("ccy_rate");
            row0.createCell(11).setCellValue("premi_total");
            row0.createCell(12).setCellValue("premi_reas");
            row0.createCell(13).setCellValue("komisi_reas");
            row0.createCell(14).setCellValue("netto_reas");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("f_ri_finish"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
        }

        final DTOList listK = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int j = 0; j < listK.size(); j++) {
            HashDTO h = (HashDTO) listK.get(j);

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("status");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("period_end");
            row0.createCell(4).setCellValue("policy_date");
            row0.createCell(5).setCellValue("ri_slip_no");
            row0.createCell(6).setCellValue("bisnis");
            row0.createCell(7).setCellValue("ent_id");
            row0.createCell(8).setCellValue("name");
            row0.createCell(9).setCellValue("ccy");
            row0.createCell(10).setCellValue("ccy_rate");
            row0.createCell(11).setCellValue("premi_total");
            row0.createCell(12).setCellValue("premi_reas");
            row0.createCell(13).setCellValue("komisi_reas");
            row0.createCell(14).setCellValue("netto_reas");

            //bikin isi cell
            XSSFRow row = sheet.createRow(j + 2);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("f_ri_finish"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public DTOList EXCEL_PROPOTIONAL_TREATY() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.cc_code,a.pol_type_id,a.cust_name,a.pol_id,pol_no as pol_no,b.ins_pol_obj_id,b.order_no,"
                + "(checkstatus(a.pol_type_id in (60),trim(b.ref3d),trim(b.ref1))) as nama,i.vs_description as cov_askred,"
                + "(  select string_agg(x.description, '| ')  from ins_pol_cover y inner join ins_cover x on x.ins_cover_id = y.ins_cover_id where y.ins_pol_obj_id = b.ins_pol_obj_id) as coverage,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,getperiod(a.pol_type_id=60,b.refd4,a.period_start)))) as refd2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,getperiod(a.pol_type_id=60,b.refd5,a.period_end)))) as refd3, "
                + "h.ins_pol_type_grp_soa,g.ins_treaty_id,g.treaty_grp_id,f.treaty_type,e.member_ent_id,e.reins_ent_id,j.short_name as reins_name,a.claim_date,a.pla_date,a.dla_date,"
                + "a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.ccy_rate_claim,getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,a.premi_total,a.claim_amount,"
                + "coalesce(b.insured_amount*ccy_rate,0) as tsi_obj,  "
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,e.ri_slip_no,"
                + "sum(round(e.tsi_amount,2)) as tsi_reins,sum(round(e.premi_amount,2)) as premi_reins,"
                + "sum(round(e.ricomm_amt,2)) as comm_reins,sum(round(e.claim_amount,2)) as claim_reins "
                + "from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "inner join ins_pol_treaty c on c.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail d on d.ins_pol_treaty_id = c.ins_pol_treaty_id "
                + "inner join ins_pol_ri e on e.ins_pol_tre_det_id = d.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id "
                + "inner join ins_treaty g on g.ins_treaty_id = c.ins_treaty_id "
                + "inner join ins_policy_types h on h.pol_type_id = a.pol_type_id "
                + "left join s_valueset i on i.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and i.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join ent_master j on j.ent_id = e.reins_ent_id "
                + "where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD') and a.active_flag='Y' and a.effective_flag='Y' ";
//                + "and a.pol_id in (11470992,11461813,11501567,11457505) ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlPremi = sqlPremi + " and e.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and f.treaty_type in ('SPL','QS')";
            } else {
                sqlPremi = sqlPremi + " and f.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and g.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlPremi = sqlPremi + " and " + polno;
        }
        sqlPremi = sqlPremi + " group by a.policy_date,a.approved_date,a.status,a.cc_code,a.pol_type_id,a.pol_id,b.ins_pol_obj_id,h.ins_pol_type_grp_soa,"
                + "g.ins_treaty_id,f.treaty_type,g.treaty_grp_id,e.ri_slip_no,e.member_ent_id,e.reins_ent_id,i.vs_description,j.short_name "
                + "order by a.pol_id,a.pol_type_id,g.treaty_grp_id,e.member_ent_id,e.reins_ent_id ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.cc_code,a.pol_type_id,a.cust_name,a.pol_id,pol_no as pol_no,b.ins_pol_obj_id,b.order_no,"
                + "(checkstatus(a.pol_type_id in (60),trim(b.ref3d),trim(b.ref1))) as nama,i.vs_description as cov_askred,"
                + "(  select string_agg(x.description, '| ')  from ins_pol_cover y inner join ins_cover x on x.ins_cover_id = y.ins_cover_id where y.ins_pol_obj_id = b.ins_pol_obj_id) as coverage,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,getperiod(a.pol_type_id=60,b.refd4,a.period_start)))) as refd2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,getperiod(a.pol_type_id=60,b.refd5,a.period_end)))) as refd3, "
                + "h.ins_pol_type_grp_soa,g.ins_treaty_id,g.treaty_grp_id,f.treaty_type,e.member_ent_id,e.reins_ent_id,j.short_name as reins_name,a.claim_date,a.pla_date,a.dla_date,"
                + "a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.ccy_rate_claim,getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,a.premi_total,a.claim_amount,"
                + "coalesce(b.insured_amount*ccy_rate,0) as tsi_obj,  "
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,e.ri_slip_no,"
                + "sum(round(e.tsi_amount,2)) as tsi_reins,sum(round(e.premi_amount,2)) as premi_reins,"
                + "sum(round(e.ricomm_amt,2)) as comm_reins,sum(round(e.claim_amount,2)) as claim_reins "
                + "from ins_policy a inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "inner join ins_pol_treaty c on c.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail d on d.ins_pol_treaty_id = c.ins_pol_treaty_id "
                + "inner join ins_pol_ri e on e.ins_pol_tre_det_id = d.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id "
                + "inner join ins_treaty g on g.ins_treaty_id = c.ins_treaty_id "
                + "inner join ins_policy_types h on h.pol_type_id = a.pol_type_id "
                + "left join s_valueset i on i.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and i.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join ent_master j on j.ent_id = e.reins_ent_id "
                + "where a.status IN ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') and a.claim_status IN ('DLA') and a.active_flag='Y' and a.effective_flag='Y' ";
//                + "and a.pol_id in (11470992,11461813,11501567,11457505)";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlKlaim = sqlKlaim + " and e.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlKlaim = sqlKlaim + " and f.treaty_type in ('SPL','QS')";
            } else {
                sqlKlaim = sqlKlaim + " and f.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and g.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88)";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
            sqlKlaim = sqlKlaim + " and " + polno;
        }
        sqlKlaim = sqlKlaim + " group by a.policy_date,a.approved_date,a.status,a.cc_code,a.pol_type_id,a.pol_id,b.ins_pol_obj_id,h.ins_pol_type_grp_soa,"
                + "g.ins_treaty_id,f.treaty_type,g.treaty_grp_id,e.ri_slip_no,e.member_ent_id,e.reins_ent_id,i.vs_description,j.short_name "
                + "order by a.pol_id,a.pol_type_id,g.treaty_grp_id,e.member_ent_id,e.reins_ent_id ";

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sqlKlaim,
                sqaK.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public DTOList EXCEL_PROPOTIONAL_TREATY_WHOUSE() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.pol_no,a.cust_name,a.order_no,a.nama,a.coverage,a.treaty_grp_id,a.refd2,a.refd3,a.tsi_obj,"
                + "a.ccy_rate,a.ccy_rate_treaty,a.tsi_reins,a.premi_reins,a.comm_reins,a.reins_ent_id,a.reins_ent_name,b.period_start,b.period_end "
                + "from prod_reas_treaty_obj a "
                + "left join data_polis b on b.pol_id = a.pol_id "
                + "where a.status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI','INWARD') ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlPremi = sqlPremi + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlPremi = sqlPremi + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlPremi = sqlPremi + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlPremi = sqlPremi + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlPremi = sqlPremi + " and " + jenid;
        }
        sqlPremi = sqlPremi + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList premi = ListUtil.getDTOListFromQueryDS(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.pol_no,a.cust_name,a.order_no,a.nama,a.coverage,a.treaty_grp_id,a.refd2,a.refd3,a.claim_date,a.tsi_obj,"
                + "a.ccy_rate_claim,a.claim_amount,a.claim_reins,a.reins_ent_id,a.reins_ent_name from prod_reas_treaty_obj a "
                + "where a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlKlaim = sqlKlaim + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlKlaim = sqlKlaim + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlKlaim = sqlKlaim + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlKlaim = sqlKlaim + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlKlaim = sqlKlaim + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlKlaim = sqlKlaim + " and " + jenid;
        }
        sqlKlaim = sqlKlaim + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList klaim = ListUtil.getDTOListFromQueryDS(
                sqlKlaim,
                sqaK.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public DTOList EXCEL_PROPOTIONAL_TREATY_WHOUSENEW() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.pol_no,a.cust_name,a.order_no,a.nama,a.coverage,a.treaty_grp_id,a.refd2,a.refd3,a.tsi_obj,"
                + "a.ccy_rate,a.ccy_rate_treaty,a.tsi_reins,a.premi_reins,a.comm_reins,a.reins_ent_id,a.reins_ent_name,b.period_start,b.period_end "
                + "from prod_reas_treaty_obj a "
                + "left join data_polis b on b.pol_id = a.pol_id "
                + "where a.status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI','INWARD') ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlPremi = sqlPremi + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlPremi = sqlPremi + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlPremi = sqlPremi + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlPremi = sqlPremi + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlPremi = sqlPremi + " and " + jenid;
        }
        sqlPremi = sqlPremi + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList premi = ListUtil.getDTOListFromQueryDS(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.pol_no,a.cust_name,a.order_no,a.nama,a.coverage,a.treaty_grp_id,a.refd2,a.refd3,a.claim_date,a.tsi_obj,"
                + "a.ccy_rate_claim,a.claim_amount,a.claim_reins,a.reins_ent_id,a.reins_ent_name from prod_reas_treaty_obj a "
                + "where a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlKlaim = sqlKlaim + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlKlaim = sqlKlaim + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlKlaim = sqlKlaim + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlKlaim = sqlKlaim + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlKlaim = sqlKlaim + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlKlaim = sqlKlaim + " and " + jenid;
        }
        sqlKlaim = sqlKlaim + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList klaim = ListUtil.getDTOListFromQueryDS(
                sqlKlaim,
                sqaK.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public void EXPORT_PROPOTIONAL_TREATY() throws Exception {

        String treaty = null;
        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                treaty = "SPL,QS";
            } else {
                treaty = getStFltTreatyType();
            }
        } else {
            treaty = getTreatyTypeList();
        }

        String cob = null;
        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                cob = "Group Non AKS";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                cob = "Group AKS";
            } else {
                cob = "AKS Non Konsumtif";
            }
        } else {
            cob = "Non Group";
        }

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet1
//        XSSFSheet sheet = wb.createSheet("Prop_tty_Premi");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        int page = 0;
        int baris = 0;
        int j = 0;
        int jumlahBarisPerSheet = 10000;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin sheet fleksibel
            if (i % jumlahBarisPerSheet == 0) {
                page = page + 1;
                XSSFSheet sheet = wb.createSheet("Prop_tty_Premi" + page);
                baris = 0;
            }

//            XSSFRow rowH0 = sheet.createRow(0);
//            rowH0.createCell(0).setCellValue("BORDERO PREMI");
//            XSSFRow rowH1 = sheet.createRow(1);
//            rowH1.createCell(0).setCellValue("Borderaux No ");
//            XSSFRow rowH2 = sheet.createRow(2);
//            rowH2.createCell(0).setCellValue("Treaty Type ");
//            rowH2.createCell(1).setCellValue(": " + treaty);
//            XSSFRow rowH3 = sheet.createRow(3);
//            rowH3.createCell(0).setCellValue("Reinsurer ");
//            rowH3.createCell(1).setCellValue(": " + getStCoinsurerName());
//            XSSFRow rowH4 = sheet.createRow(4);
//            rowH4.createCell(0).setCellValue("Borderaux Period ");
//            rowH4.createCell(1).setCellValue(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateFrom(), "d ^^ yyyy")) + " - " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateTo(), "d ^^ yyyy")));
//            XSSFRow rowH5 = sheet.createRow(5);
//            rowH5.createCell(0).setCellValue("Class of Bussiness ");
//            rowH5.createCell(1).setCellValue(": " + cob);

            //bikin header
//            XSSFRow row0 = sheet.createRow(7);
            XSSFRow row0 = wb.getSheet("Prop_tty_Premi" + page).createRow(0);
            row0.createCell(0).setCellValue("Policy No");
            row0.createCell(1).setCellValue("The Insured");
            row0.createCell(2).setCellValue("Object No");
            row0.createCell(3).setCellValue("Object Insured");
            row0.createCell(4).setCellValue("Coverage");
            row0.createCell(5).setCellValue("UW. Year");
            row0.createCell(6).setCellValue("Period Start");
            row0.createCell(7).setCellValue("Period End");
            row0.createCell(8).setCellValue("Insured");
            row0.createCell(9).setCellValue("Insured Reins");
            row0.createCell(10).setCellValue("Premi Reins");
            row0.createCell(11).setCellValue("Comm Reins");
            row0.createCell(12).setCellValue("SOB");
            row0.createCell(13).setCellValue("Curr");
            row0.createCell(14).setCellValue("Curr Treaty");
            row0.createCell(15).setCellValue("Norut");

//            BigDecimal tsiri = BDUtil.mul(h.getFieldValueByFieldNameBD("tsi_reins"), h.getFieldValueByFieldNameBD("ccy_rate"));
//            BigDecimal premiri = BDUtil.mul(h.getFieldValueByFieldNameBD("premi_reins"), h.getFieldValueByFieldNameBD("ccy_rate"));
//            BigDecimal commri = BDUtil.mul(h.getFieldValueByFieldNameBD("comm_reins"), h.getFieldValueByFieldNameBD("ccy_rate"));

            //bikin isi cell
//            XSSFRow row = sheet.createRow(i + 8);
//            row.createCell(0).setCellValue(String.valueOf(i + 1));
            XSSFRow row = wb.getSheet("Prop_tty_Premi" + page).createRow(baris + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(3).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("nama")));
            row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("coverage")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            } else {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            } else {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
//            row.createCell(9).setCellValue(tsiri.doubleValue());
//            row.createCell(10).setCellValue(premiri.doubleValue());
//            row.createCell(11).setCellValue(commri.doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("tsi_reins").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_reins").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_reins").doubleValue());
            if (h.getFieldValueByFieldNameBD("reins_ent_id") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("reins_ent_name"));
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_treaty").doubleValue());
            row.createCell(15).setCellValue(String.valueOf(i + 1));

            baris = baris + 1;
        }

        //bikin sheet2
        XSSFSheet sheet2 = wb.createSheet("Prop_tty_Klaim");

        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        for (int jk = 0; jk < list2.size(); jk++) {
            HashDTO h = (HashDTO) list2.get(jk);
            /*
            XSSFRow rowH0 = sheet2.createRow(0);
            rowH0.createCell(0).setCellValue("BORDERO KLAIM");
            XSSFRow rowH1 = sheet2.createRow(1);
            rowH1.createCell(0).setCellValue("Borderaux No ");
            XSSFRow rowH2 = sheet2.createRow(2);
            rowH2.createCell(0).setCellValue("Treaty Type ");
            rowH2.createCell(1).setCellValue(": " + treaty);
            XSSFRow rowH3 = sheet2.createRow(3);
            rowH3.createCell(0).setCellValue("Reinsurer ");
            rowH3.createCell(1).setCellValue(": " + getStCoinsurerName());
            XSSFRow rowH4 = sheet2.createRow(4);
            rowH4.createCell(0).setCellValue("Borderaux Period ");
            rowH4.createCell(1).setCellValue(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateFrom(), "d ^^ yyyy")) + " - " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateTo(), "d ^^ yyyy")));
            XSSFRow rowH5 = sheet2.createRow(5);
            rowH5.createCell(0).setCellValue("Class of Bussiness ");
            rowH5.createCell(1).setCellValue(": " + cob);*/

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("Policy No");
            row0.createCell(1).setCellValue("The Insured");
            row0.createCell(2).setCellValue("Object No");
            row0.createCell(3).setCellValue("Object Insured");
            row0.createCell(4).setCellValue("Coverage");
            row0.createCell(5).setCellValue("UW. Year");
            row0.createCell(6).setCellValue("Period Start");
            row0.createCell(7).setCellValue("Period End");
            row0.createCell(8).setCellValue("DOL");
            row0.createCell(9).setCellValue("Insured");
            row0.createCell(10).setCellValue("Claim");
            row0.createCell(11).setCellValue("Claim Reins");
            row0.createCell(12).setCellValue("SOB");
            row0.createCell(13).setCellValue("Curr Claim");

            //bikin isi cell
            XSSFRow row = sheet2.createRow(jk + 1);
//            row.createCell(0).setCellValue(String.valueOf(i + 1));
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(3).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("nama")));
            row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("coverage")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_reins").doubleValue());
            if (h.getFieldValueByFieldNameBD("reins_ent_id") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("reins_ent_name"));
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
        }

        String fileName = getStFileName() + "_" + System.currentTimeMillis();

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_BORDERO_TREATY_WHOUSE() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.pol_id,a.ins_pol_obj_id,a.refd2,a.refd3,(case when a.status in ('CLAIM','CLAIM ENDORSE') and a.subro_id is null then 'CLAIM' "
                + "when a.status in ('CLAIM','CLAIM ENDORSE') and a.subro_id is not null then 'SUBRO' "
                + "when a.status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI','INWARD') then 'POLICY' end) as status,"
                + "(DATE_PART('y',refd3)-DATE_PART('y',refd2)) as tahun,a.member_ent_id,a.reins_ent_id,"
                + "a.tsi_reins*a.ccy_rate as tsi_reins,a.premi_reins*a.ccy_rate as premi_reins,a.comm_reins*a.ccy_rate as comm_reins,a.claim_reins*a.ccy_rate_claim as claim_reins,a.subro_id "
                + "from prod_reas_treaty_obj a where a.status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI','INWARD','CLAIM','CLAIM ENDORSE') ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

//        if (getStCoinsurerID() != null) {
//            sqlPremi = sqlPremi + " and a.member_ent_id = '" + stCoinsurerID + "'";
//        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlPremi = sqlPremi + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlPremi = sqlPremi + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlPremi = sqlPremi + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlPremi = sqlPremi + " and " + jenid;
        }
        sqlPremi = "select a.tahun,sum(debitur_pol) as debitur_pol,sum(tsi_ri_tot) as tsi_ri_tot,sum(premi_ri_tot) as premi_ri_tot,"
                + "sum(comm_ri_tot) as comm_ri_tot,sum(tsi_ri) as tsi_ri,sum(premi_ri) as premi_ri,sum(comm_ri) as comm_ri,"
                + "sum(debitur_clm) as debitur_clm,sum(claim_ri_tot) as claim_ri_tot,sum(claim_ri) as claim_ri,sum(debitur_sub) as debitur_sub,"
                + "sum(claim_ri_sub_tot) as claim_ri_sub_tot,sum(claim_ri_sub) as claim_ri_sub from ( "
                + "select a.tahun,getpremi2(a.status = 'POLICY',count(a.debitur)) as debitur_pol,"
                + "getpremi2(a.status = 'POLICY',sum(a.tsi_ri_total)) as tsi_ri_tot,getpremi2(a.status = 'POLICY',sum(a.premi_ri_total)) as premi_ri_tot,"
                + "getpremi2(a.status = 'POLICY',sum(a.comm_ri_total)) as comm_ri_tot,getpremi2(a.status = 'POLICY',sum(a.tsi_ri)) as tsi_ri,"
                + "getpremi2(a.status = 'POLICY',sum(a.premi_ri)) as premi_ri,getpremi2(a.status = 'POLICY',sum(a.comm_ri)) as comm_ri,"
                + "getpremi2(a.status = 'CLAIM',count(a.debitur)) as debitur_clm,getpremi2(a.status = 'CLAIM',sum(a.claim_ri_total)) as claim_ri_tot,"
                + "getpremi2(a.status = 'CLAIM',sum(a.claim_ri)) as claim_ri,getpremi2(a.status = 'SUBRO',count(a.debitur)) as debitur_sub,"
                + "getpremi2(a.status = 'SUBRO',sum(a.claim_ri_total)) as claim_ri_sub_tot,getpremi2(a.status = 'SUBRO',sum(a.claim_ri)) as claim_ri_sub from ( "
                + "select a.tahun,a.status,a.ins_pol_obj_id as debitur,getpremi2(a.status = 'POLICY',sum(tsi_reins)) as tsi_ri_total,"
                + "getpremi2(a.status = 'POLICY',sum(premi_reins)) as premi_ri_total,getpremi2(a.status = 'POLICY',sum(comm_reins)) as comm_ri_total,"
                + "getpremi2(a.status = 'POLICY',sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.tsi_reins))) as tsi_ri,"
                + "getpremi2(a.status = 'POLICY',sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.premi_reins))) as premi_ri,"
                + "getpremi2(a.status = 'POLICY',sum(getpremi2(a.member_ent_id = " + stCoinsurerID + ",a.comm_reins))) as comm_ri,"
                + "getpremi2(a.status in ('CLAIM','SUBRO'),sum(claim_reins)) as claim_ri_total,"
                + "getpremi2(a.status in ('CLAIM','SUBRO'),sum(getpremi2(a.member_ent_id = 1034166,a.claim_reins))) as claim_ri from ( "
                + sqlPremi + " order by a.pol_id,a.ins_pol_obj_id ) a group by a.tahun,a.status,a.ins_pol_obj_id "
                + "order by a.tahun ) a group by a.tahun,a.status,a.debitur order by a.tahun ) a group by a.tahun order by a.tahun ";

        final DTOList premi = ListUtil.getDTOListFromQueryDS(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        return premi;
    }

    public void EXPORT_BORDERO_TREATY() throws Exception {

        String treaty = null;
        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                treaty = "SPL,QS";
            } else {
                treaty = getStFltTreatyType();
            }
        } else {
            treaty = getTreatyTypeList();
        }

        String cob = null;
        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                cob = "Group Non AKS";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                cob = "Group AKS";
            } else {
                cob = "AKS Non Konsumtif";
            }
        } else {
            cob = "Non Group";
        }

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet1
        XSSFSheet sheet = wb.createSheet("Bordero_tty");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowH0 = sheet.createRow(0);
            rowH0.createCell(0).setCellValue("BORDERO TREATY");
            XSSFRow rowH1 = sheet.createRow(1);
            rowH1.createCell(0).setCellValue("Borderaux No ");
            XSSFRow rowH2 = sheet.createRow(2);
            rowH2.createCell(0).setCellValue("Treaty Type ");
            rowH2.createCell(1).setCellValue(": " + treaty);
            XSSFRow rowH3 = sheet.createRow(3);
            rowH3.createCell(0).setCellValue("Reinsurer ");
            rowH3.createCell(1).setCellValue(": " + getStCoinsurerName());
            XSSFRow rowH4 = sheet.createRow(4);
            rowH4.createCell(0).setCellValue("Borderaux Period ");
            rowH4.createCell(1).setCellValue(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateFrom(), "d ^^ yyyy")) + " - " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateTo(), "d ^^ yyyy")));
            XSSFRow rowH5 = sheet.createRow(5);
            rowH5.createCell(0).setCellValue("Class of Bussiness ");
            rowH5.createCell(1).setCellValue(": " + cob);

            //bikin header
            XSSFRow row0 = sheet.createRow(7);
            row0.createCell(0).setCellValue("tahun");
            row0.createCell(1).setCellValue("jml_debitur");
            row0.createCell(2).setCellValue("tsi");
            row0.createCell(3).setCellValue("premium");
            row0.createCell(4).setCellValue("ri comm");
            row0.createCell(5).setCellValue("netto");
            row0.createCell(6).setCellValue("tsi");
            row0.createCell(7).setCellValue("premium");
            row0.createCell(8).setCellValue("ri comm");
            row0.createCell(9).setCellValue("netto");
            row0.createCell(10).setCellValue("jml_debitur");
            row0.createCell(11).setCellValue("klaim");
            row0.createCell(12).setCellValue("share");
            row0.createCell(13).setCellValue("jml_debitur");
            row0.createCell(14).setCellValue("subro");
            row0.createCell(15).setCellValue("share");

            BigDecimal nett100 = new BigDecimal(0);
            BigDecimal nettri = new BigDecimal(0);

            nett100 = BDUtil.sub(h.getFieldValueByFieldNameBD("premi_ri_tot"), h.getFieldValueByFieldNameBD("comm_ri_tot"));
            nettri = BDUtil.sub(h.getFieldValueByFieldNameBD("premi_ri"), h.getFieldValueByFieldNameBD("comm_ri"));

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            if (h.getFieldValueByFieldNameBD("tahun") != null) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("tahun").doubleValue());
            }
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("debitur_pol").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("tsi_ri_tot").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi_ri_tot").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("comm_ri_tot").doubleValue());
            row.createCell(5).setCellValue(nett100.doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("tsi_ri").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_ri").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("comm_ri").doubleValue());
            row.createCell(9).setCellValue(nettri.doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("debitur_clm").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_ri_tot").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim_ri").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("debitur_sub").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim_ri_sub_tot").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim_ri_sub").doubleValue());
        }

        String fileName = getStFileName() + "_" + System.currentTimeMillis();

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_AUTOFAC_KREDIT_WHOUSE() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        String sqlPremi = "select a.*,b.period_start,b.period_end from prod_reas_treaty_obj a "
                + "left join data_polis b on b.pol_id = a.pol_id "
                + "where a.status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI','INWARD') ";

        if (getPolicyDateFrom() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlPremi = sqlPremi + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlPremi = sqlPremi + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlPremi = sqlPremi + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlPremi = sqlPremi + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlPremi = sqlPremi + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlPremi = sqlPremi + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlPremi = sqlPremi + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlPremi = sqlPremi + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlPremi = sqlPremi + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlPremi = sqlPremi + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlPremi = sqlPremi + " and " + jenid;
        }
        sqlPremi = sqlPremi + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList premi = ListUtil.getDTOListFromQueryDS(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.*,b.cause_desc from prod_reas_treaty_obj a "
                + "left join prod_klaim b on b.pol_id = a.pol_id "
                + "where a.status in ('CLAIM','CLAIM ENDORSE') ";

        if (getPolicyDateFrom() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) >= '" + policyDateFrom + "' ";
        }

        if (getPolicyDateTo() != null) {
            sqlKlaim = sqlKlaim + " and date_trunc('day',a.approved_date) <= '" + policyDateTo + "' ";
        }

        if (getStCoinsurerID() != null) {
            sqlKlaim = sqlKlaim + " and a.member_ent_id = '" + stCoinsurerID + "'";
        }

        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                sqlKlaim = sqlKlaim + " and a.treaty_type in ('SPL','QS')";
            } else {
                sqlKlaim = sqlKlaim + " and a.treaty_type = '" + stFltTreatyType + "'";
            }
        }

        if (getStTreatyYearGrpID() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_grp_id = '" + stTreatyYearGrpID + "'";
        }

        if (stPolicyNo != null) {
            String morePolis[] = stPolicyNo.split("[\\,]");
            if (morePolis.length == 1) {
                sqlKlaim = sqlKlaim + " and a.pol_no = '" + stPolicyNo + "'";
            } else if (morePolis.length > 1) {
                String polno = "a.pol_no in ('" + morePolis[0] + "'";
                for (int k = 1; k < morePolis.length; k++) {
                    polno = polno + ",'" + morePolis[k] + "'";
                }
                polno = polno + ")";
                sqlKlaim = sqlKlaim + " and " + polno;
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86) ";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88) ";
            } else {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97) ";
            }
        }

        if (getStPolCredit() != null) {
            if (getStPolCredit().equalsIgnoreCase("1")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id not in (21,59,31,32,33,80,87,88) ";
            } else if (getStPolCredit().equalsIgnoreCase("2")) {
                sqlKlaim = sqlKlaim + " and a.pol_type_id in (59,80,87,88) ";
            }
        }

        if (getStPolJenas() != null) {
            String morePolis[] = getStPolJenas().split("[\\,]");
            String jenid = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                jenid = jenid + "," + morePolis[k];
            }
            jenid = jenid + ")";
            sqlKlaim = sqlKlaim + " and " + jenid;
        }
        sqlKlaim = sqlKlaim + " order by a.pol_id,a.ins_pol_obj_id,a.pol_type_id,a.treaty_grp_id,a.member_ent_id,a.reins_ent_id ";

        final DTOList klaim = ListUtil.getDTOListFromQueryDS(
                sqlKlaim,
                sqaK.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public void EXPORT_AUTOFAC_KREDIT() throws Exception {

        String treaty = null;
        if (getStFltTreatyType() != null) {
            if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                treaty = "SPL,QS";
            } else {
                treaty = getStFltTreatyType();
            }
        } else {
            treaty = getTreatyTypeList();
        }

        String cob = null;
        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                cob = "Group Non AKS";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                cob = "Group AKS";
            } else {
                cob = "AKS Non Konsumtif";
            }
        } else {
            cob = "Non Group";
        }

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet1
        XSSFSheet sheet = wb.createSheet("autofac_tty_Premi");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowH0 = sheet.createRow(0);
            rowH0.createCell(0).setCellValue("BORDERO PREMI");
            XSSFRow rowH1 = sheet.createRow(1);
            rowH1.createCell(0).setCellValue("Borderaux No ");
            XSSFRow rowH2 = sheet.createRow(2);
            rowH2.createCell(0).setCellValue("Treaty Type ");
            rowH2.createCell(1).setCellValue(": " + treaty);
            XSSFRow rowH3 = sheet.createRow(3);
            rowH3.createCell(0).setCellValue("Reinsurer ");
            rowH3.createCell(1).setCellValue(": " + getStCoinsurerName());
            XSSFRow rowH4 = sheet.createRow(4);
            rowH4.createCell(0).setCellValue("Borderaux Period ");
            rowH4.createCell(1).setCellValue(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateFrom(), "d ^^ yyyy")) + " - " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateTo(), "d ^^ yyyy")));
            XSSFRow rowH5 = sheet.createRow(5);
            rowH5.createCell(0).setCellValue("Class of Bussiness ");
            rowH5.createCell(1).setCellValue(": " + cob);

            //bikin header
            XSSFRow row0 = sheet.createRow(7);
            row0.createCell(0).setCellValue("Policy No");
            row0.createCell(1).setCellValue("The Insured");
            row0.createCell(2).setCellValue("Object No");
            row0.createCell(3).setCellValue("Object Insured");
            row0.createCell(4).setCellValue("Coverage");
            row0.createCell(5).setCellValue("UW. Year");
            row0.createCell(6).setCellValue("Period Start");
            row0.createCell(7).setCellValue("Period End");
            row0.createCell(8).setCellValue("Insured");
            row0.createCell(9).setCellValue("Curr");
            row0.createCell(10).setCellValue("Curr Treaty");
            row0.createCell(11).setCellValue("Insured Reins");
            row0.createCell(12).setCellValue("Premi Reins");
            row0.createCell(13).setCellValue("Comm Reins");
            row0.createCell(14).setCellValue("SOB");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("nama"));
            row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("coverage")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_treaty").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("tsi_reins").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_reins").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("comm_reins").doubleValue());
            if (h.getFieldValueByFieldNameST("reins_ent_name") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("reins_ent_name"));
            }
        }

        //bikin sheet2
        XSSFSheet sheet2 = wb.createSheet("autofac_tty_Klaim");

        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        for (int i = 0; i < list2.size(); i++) {
            HashDTO h = (HashDTO) list2.get(i);

            XSSFRow rowH0 = sheet2.createRow(0);
            rowH0.createCell(0).setCellValue("BORDERO KLAIM");
            XSSFRow rowH1 = sheet2.createRow(1);
            rowH1.createCell(0).setCellValue("Borderaux No ");
            XSSFRow rowH2 = sheet2.createRow(2);
            rowH2.createCell(0).setCellValue("Treaty Type ");
            rowH2.createCell(1).setCellValue(": " + treaty);
            XSSFRow rowH3 = sheet2.createRow(3);
            rowH3.createCell(0).setCellValue("Reinsurer ");
            rowH3.createCell(1).setCellValue(": " + getStCoinsurerName());
            XSSFRow rowH4 = sheet2.createRow(4);
            rowH4.createCell(0).setCellValue("Borderaux Period ");
            rowH4.createCell(1).setCellValue(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateFrom(), "d ^^ yyyy")) + " - " + LanguageManager.getInstance().translate(DateUtil.getDateStr(getPolicyDateTo(), "d ^^ yyyy")));
            XSSFRow rowH5 = sheet2.createRow(5);
            rowH5.createCell(0).setCellValue("Class of Bussiness ");
            rowH5.createCell(1).setCellValue(": " + cob);

            //bikin header
            XSSFRow row0 = sheet2.createRow(7);
            row0.createCell(0).setCellValue("Policy No");
            row0.createCell(1).setCellValue("The Insured");
            row0.createCell(2).setCellValue("Object No");
            row0.createCell(3).setCellValue("Object Insured");
            row0.createCell(4).setCellValue("Coverage");
            row0.createCell(5).setCellValue("UW. Year");
            row0.createCell(6).setCellValue("Period Start");
            row0.createCell(7).setCellValue("Period End");
            row0.createCell(8).setCellValue("DOL");
            row0.createCell(9).setCellValue("Insured");
            row0.createCell(10).setCellValue("Curr Claim");
            row0.createCell(11).setCellValue("Claim");
            row0.createCell(12).setCellValue("Claim Reins");
            row0.createCell(13).setCellValue("SOB");
            row0.createCell(14).setCellValue("Sebab Klaim");

            //bikin isi cell
            XSSFRow row = sheet2.createRow(i + 8);
//            row.createCell(0).setCellValue(String.valueOf(i + 1));
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("nama"));
            row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("coverage")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            } else {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            } else {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim_reins").doubleValue());
            if (h.getFieldValueByFieldNameST("reins_ent_name") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("reins_ent_name"));
            }
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("cause_desc"));
        }

        String fileName = getStFileName() + "_" + System.currentTimeMillis();

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }
    private String treatyTypeList;
    private String policyTypeList;

    public String getTreatyTypeList() {
        return treatyTypeList;
    }

    public void setTreatyTypeList(String treatyTypeList) {
        this.treatyTypeList = treatyTypeList;
    }

    public void addTreatyType() {

        String treatyList = getTreatyTypeList();

        treatyList = treatyList + ",'" + stFltTreatyType + "'";

        treatyList = treatyList.replaceAll("null,", "");

        setTreatyTypeList(treatyList);
    }

    /**
     * @return the policyTypeList
     */
    public String getPolicyTypeList() {
        return policyTypeList;
    }

    /**
     * @param policyTypeList the policyTypeList to set
     */
    public void setPolicyTypeList(String policyTypeList) {
        this.policyTypeList = policyTypeList;
    }

    public void addPolicyType() {

        String treatyList = getPolicyTypeList();

        treatyList = treatyList + "," + stPolicyTypeID;

        treatyList = treatyList.replaceAll("null,", "");

        setPolicyTypeList(treatyList);

    }

    public DTOList EXCEL_SOA_TREATY() throws Exception {
        final SQLAssembler sqaP = new SQLAssembler();

        sqaP.addSelect("TO_CHAR(a.approved_date, 'YY') as tahun,TO_CHAR(a.approved_date, 'MM') as bulan,a.treaty_grp_id,a.ins_treaty_id,c.treaty_name,"
                + " (case when c.ref2 = 'MULTIYEARS' then b.group_name||' Multiyears' else b.group_name end) as group_name,f.short_desc as cob,a.pol_id,';'||d.pol_no as pol_no,"
                + "d.dla_no as lkpno,a.treaty_type as treaty,a.member_ent_id,e.ent_name as member,sum(a.tsi_reins*a.ccy_rate) as insured_ri,"
                + "sum(a.premi_reins*a.ccy_rate) as premi_ri,sum(a.comm_reins*a.ccy_rate) as komisi_ri ");

        sqaP.addQuery("from ins_pol_ri_treaty a "
                + " inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + " inner join ins_treaty c on c.ins_treaty_id = a.ins_treaty_id "
                + " inner join ins_policy d on d.pol_id = a.pol_id "
                + " inner join ent_master e on e.ent_id = a.member_ent_id "
                + " inner join ins_policy_types f on f.pol_type_id = a.pol_type_id ");

        sqaP.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI','INWARD')");
        sqaP.addClause(" a.treaty_type in ('QS','SPL','FACO1')");
        sqaP.addClause(" a.member_ent_id <> 1 ");

        if (policyDateFrom != null) {
            sqaP.addClause("date_trunc('day',a.approved_date) >= ?");
            sqaP.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqaP.addClause("date_trunc('day',a.approved_date) <= ?");
            sqaP.addPar(policyDateTo);
        }

        if (stTreatyYearGrpID != null) {
            sqaP.addClause("a.treaty_grp_id = ?");
            sqaP.addPar(stTreatyYearGrpID);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqaP.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqaP.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stPolJenas != null) {
            String morePolis[] = stPolJenas.split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
//            sqlPremi = sqlPremi + " and " + polno;
            sqaP.addClause(polno);
        }

        if (policyTypeList != null) {
            sqaP.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqaP.addClause("a.ins_policy_type_grp_id = ?");
                sqaP.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqaP.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            }
        }

        if (stCoinsurerID != null) {
            sqaP.addClause("a.member_ent_id = ?");
            sqaP.addPar(stCoinsurerID);
        }

        String sqlPremi = sqaP.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10,11,12,13 order by pol_id ";

        final DTOList premi = ListUtil.getDTOListFromQuery(
                sqlPremi,
                sqaP.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", premi);

        final SQLAssembler sqaK = new SQLAssembler();

        sqaK.addSelect("TO_CHAR(a.approved_date, 'YY') as tahun,TO_CHAR(a.approved_date, 'MM') as bulan,a.treaty_grp_id,a.ins_treaty_id,c.treaty_name,"
                + " (case when c.ref2 = 'MULTIYEARS' then b.group_name||' Multiyears' else b.group_name end) as group_name,f.short_desc as cob,a.pol_id,';'||d.pol_no as pol_no,"
                + "d.dla_no as lkpno,a.treaty_type as treaty,a.member_ent_id,e.ent_name as member,sum(a.claim_reins*a.ccy_rate_claim) as klaim_ri");

        sqaK.addQuery("from ins_pol_ri_treaty a "
                + " inner join ins_policy_type_grp b on b.ins_policy_type_grp_id = a.ins_pol_type_grp_soa "
                + " inner join ins_treaty c on c.ins_treaty_id = a.ins_treaty_id "
                + " inner join ins_policy d on d.pol_id = a.pol_id "
                + " inner join ent_master e on e.ent_id = a.member_ent_id "
                + " inner join ins_policy_types f on f.pol_type_id = a.pol_type_id ");

        sqaK.addClause(" a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD')");
        sqaK.addClause(" a.treaty_type in ('QS','SPL','FACO1')");
        sqaK.addClause(" a.member_ent_id <> 1 ");
        sqaK.addClause(" coalesce(a.claim_cash_call_f,'N') <> 'Y' ");

        if (policyDateFrom != null) {
            sqaK.addClause("date_trunc('day',a.approved_date) >= ?");
            sqaK.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqaK.addClause("date_trunc('day',a.approved_date) <= ?");
            sqaK.addPar(policyDateTo);
        }

        if (stTreatyYearGrpID != null) {
            sqaK.addClause("a.treaty_grp_id = ?");
            sqaK.addPar(stTreatyYearGrpID);
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqaK.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqaK.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        if (stPolJenas != null) {
            String morePolis[] = stPolJenas.split("[\\,]");
            String polno = "a.pol_type_id not in (" + morePolis[0];
            for (int k = 1; k < morePolis.length; k++) {
                polno = polno + "," + morePolis[k];
            }
            polno = polno + ")";
//            sqlPremi = sqlPremi + " and " + polno;
            sqaK.addClause(polno);
        }

        if (policyTypeList != null) {
            sqaK.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqaK.addClause("a.ins_policy_type_grp_id = ?");
                sqaK.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqaK.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            }
        }

        if (stCoinsurerID != null) {
            sqaK.addClause("a.member_ent_id = ?");
            sqaK.addPar(stCoinsurerID);
        }

        String sqlKlaim = sqaK.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10,11,12,13 order by pol_id ";

        final DTOList klaim = ListUtil.getDTOListFromQuery(
                sqlKlaim,
                sqaK.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", klaim);

        return premi;
    }

    public void EXPORT_SOA_TREATY() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("Premi");
        XSSFSheet sheetK = wb.createSheet("Klaim");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("tahun");
            row0.createCell(1).setCellValue("bulan");
            row0.createCell(2).setCellValue("treaty_grp_id");
            row0.createCell(3).setCellValue("ins_treaty_id");
            row0.createCell(4).setCellValue("treaty_name");
            row0.createCell(5).setCellValue("group_name");
            row0.createCell(6).setCellValue("cob");
            row0.createCell(7).setCellValue("pol_id");
            row0.createCell(8).setCellValue("pol_no");
            row0.createCell(9).setCellValue("treaty");
            row0.createCell(10).setCellValue("member_ent_id");
            row0.createCell(11).setCellValue("member");
            row0.createCell(12).setCellValue("insured_ri");
            row0.createCell(13).setCellValue("premi_ri");
            row0.createCell(14).setCellValue("komisi_ri");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("tahun"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("bulan"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("ins_treaty_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("treaty_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("group_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cob"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("treaty"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("member_ent_id").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("member"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("insured_ri").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premi_ri").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("komisi_ri").doubleValue());
        }

        final DTOList listK = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int j = 0; j < listK.size(); j++) {
            HashDTO h = (HashDTO) listK.get(j);

            //bikin header
            XSSFRow row0 = sheetK.createRow(1);
            row0.createCell(0).setCellValue("tahun");
            row0.createCell(1).setCellValue("bulan");
            row0.createCell(2).setCellValue("treaty_grp_id");
            row0.createCell(3).setCellValue("ins_treaty_id");
            row0.createCell(4).setCellValue("treaty_name");
            row0.createCell(5).setCellValue("group_name");
            row0.createCell(6).setCellValue("cob");
            row0.createCell(7).setCellValue("pol_id");
            row0.createCell(8).setCellValue("pol_no");
            row0.createCell(9).setCellValue("lkpno");
            row0.createCell(10).setCellValue("treaty");
            row0.createCell(11).setCellValue("member_ent_id");
            row0.createCell(12).setCellValue("member");
            row0.createCell(13).setCellValue("klaim_ri");

            //bikin isi cell
            XSSFRow row = sheetK.createRow(j + 2);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("tahun"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("bulan"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("ins_treaty_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("treaty_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("group_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cob"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("lkpno"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("treaty"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("member_ent_id").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("member"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("klaim_ri").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public GLCostCenterView getCostCenter(String branch) {
        return (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, branch);
    }

    public DTOList EXCEL_REKAP_REAS_CABANGCOB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	(case when a.cc_code = '80' then a.cc_code_source else a.cc_code end) as cc_code,a.pol_type_id,a.pol_id,a.pol_no,"
                + "sum(getpremi2(b.treaty_type = 'BPDAN',tsi_reins)) as tsi_BPDAN,"
                + "sum(getpremi2(b.treaty_type = 'BPDAN',premi_reins)) as premi_BPDAN,"
                + "sum(getpremi2(b.treaty_type = 'BPDAN',comm_reins)) as komisi_BPDAN,"
                + "sum(getpremi2(b.treaty_type = 'FAC',tsi_reins)) as tsi_FAC,"
                + "sum(getpremi2(b.treaty_type = 'FAC',premi_reins)) as premi_FAC,"
                + "sum(getpremi2(b.treaty_type = 'FAC',comm_reins)) as komisi_FAC,"
                + "sum(getpremi2(b.treaty_type = 'FACO',tsi_reins)) as tsi_FACO,"
                + "sum(getpremi2(b.treaty_type = 'FACO',premi_reins)) as premi_FACO,"
                + "sum(getpremi2(b.treaty_type = 'FACO',comm_reins)) as komisi_FACO,"
                + "sum(getpremi2(b.treaty_type = 'FACO1',tsi_reins)) as tsi_FACO1,"
                + "sum(getpremi2(b.treaty_type = 'FACO1',premi_reins)) as premi_FACO1,"
                + "sum(getpremi2(b.treaty_type = 'FACO1',comm_reins)) as komisi_FACO1,"
                + "sum(getpremi2(b.treaty_type = 'FACO2',tsi_reins)) as tsi_FACO2,"
                + "sum(getpremi2(b.treaty_type = 'FACO2',premi_reins)) as premi_FACO2,"
                + "sum(getpremi2(b.treaty_type = 'FACO2',comm_reins)) as komisi_FACO2,"
                + "sum(getpremi2(b.treaty_type = 'FACO3',tsi_reins)) as tsi_FACO3,"
                + "sum(getpremi2(b.treaty_type = 'FACO3',premi_reins)) as premi_FACO3,"
                + "sum(getpremi2(b.treaty_type = 'FACO3',comm_reins)) as komisi_FACO3,"
                + "sum(getpremi2(b.treaty_type = 'JP',tsi_reins)) as tsi_JP,"
                + "sum(getpremi2(b.treaty_type = 'JP',premi_reins)) as premi_JP,"
                + "sum(getpremi2(b.treaty_type = 'JP',comm_reins)) as komisi_JP,"
                + "sum(getpremi2(b.treaty_type = 'KSCBI',tsi_reins)) as tsi_KSCBI,"
                + "sum(getpremi2(b.treaty_type = 'KSCBI',premi_reins)) as premi_KSCBI,"
                + "sum(getpremi2(b.treaty_type = 'KSCBI',comm_reins)) as komisi_KSCBI,"
                + "sum(getpremi2(b.treaty_type = 'PARK',tsi_reins)) as tsi_PARK,"
                + "sum(getpremi2(b.treaty_type = 'PARK',premi_reins)) as premi_PARK,"
                + "sum(getpremi2(b.treaty_type = 'PARK',comm_reins)) as komisi_PARK,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id <> 1,tsi_reins)) as tsi_QS,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id <> 1,premi_reins)) as premi_QS,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id <> 1,comm_reins)) as komisi_QS,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id = 1,tsi_reins)) as tsi_QS_aba,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id = 1,premi_reins)) as premi_QS_aba,"
                + "sum(getpremi2(b.treaty_type = 'QS' and member_ent_id = 1,comm_reins)) as komisi_QS_aba,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id <> 1,tsi_reins)) as tsi_SPL,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id <> 1,premi_reins)) as premi_SPL,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id <> 1,comm_reins)) as komisi_SPL,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id = 1,tsi_reins)) as tsi_SPL_aba,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id = 1,premi_reins)) as premi_SPL_aba,"
                + "sum(getpremi2(b.treaty_type = 'SPL' and member_ent_id = 1,comm_reins)) as komisi_SPL_aba ");

        sqa.addQuery("from ins_pol_ri_treaty b "
                + "inner join ins_policy a on a.pol_id = b.pol_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            }
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + "group by 1,2,3,4 order by 1,2,3 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final String sql2 = "select cc_code,pol_type_id,sum(tsi_bpdan) as tsi_bpdan,sum(premi_bpdan) as premi_bpdan,sum(komisi_bpdan) as komisi_bpdan,"
                + "sum(tsi_fac) as tsi_fac,sum(premi_fac) as premi_fac,sum(komisi_fac) as komisi_fac,"
                + "sum(tsi_faco) as tsi_faco,sum(premi_faco) as premi_faco,sum(komisi_faco) as komisi_faco,"
                + "sum(tsi_faco1) as tsi_faco1,sum(premi_faco1) as premi_faco1,sum(komisi_faco1) as komisi_faco1,"
                + "sum(tsi_faco2) as tsi_faco2,sum(premi_faco2) as premi_faco2,sum(komisi_faco2) as komisi_faco2,"
                + "sum(tsi_faco3) as tsi_faco3,sum(premi_faco3) as premi_faco3,sum(komisi_faco3) as komisi_faco3,"
                + "sum(tsi_jp) as tsi_jp,sum(premi_jp) as premi_jp,sum(komisi_jp) as komisi_jp,sum(tsi_kscbi) as tsi_kscbi,"
                + "sum(premi_kscbi) as premi_kscbi,sum(komisi_kscbi) as komisi_kscbi,"
                + "sum(tsi_park) as tsi_park,sum(premi_park) as premi_park,sum(komisi_park) as komisi_park,"
                + "sum(tsi_qs) as tsi_qs,sum(premi_qs) as premi_qs,sum(komisi_qs) as komisi_qs,"
                + "sum(tsi_qs_aba) as tsi_qs_aba,sum(premi_qs_aba) as premi_qs_aba,sum(komisi_qs_aba) as komisi_qs_aba,"
                + "sum(tsi_spl) as tsi_spl,sum(premi_spl) as premi_spl,sum(komisi_spl) as komisi_spl,"
                + "sum(tsi_spl_aba) as tsi_spl_aba,sum(premi_spl_aba) as premi_spl_aba,sum(komisi_spl_aba) as komisi_spl_aba "
                + "from ( "
                + sqa.getSQL() + " group by 1,2,3,4 order by 1,2 ) a group by 1,2 order by 1,2 ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;
    }

    public void EXPORT_REKAP_REAS_CABANGCOB() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet2 = wb.createSheet("rekap");
        XSSFSheet sheet = wb.createSheet("detil");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("cc_code");
            row0.createCell(1).setCellValue("cabang");
            row0.createCell(2).setCellValue("pol_type_id");
            row0.createCell(3).setCellValue("cob");
            row0.createCell(4).setCellValue("pol_id");
            row0.createCell(5).setCellValue("pol_no");
            row0.createCell(6).setCellValue("tsi_bpdan");
            row0.createCell(7).setCellValue("premi_bpdan");
            row0.createCell(8).setCellValue("komisi_bpdan");
            row0.createCell(9).setCellValue("tsi_fac");
            row0.createCell(10).setCellValue("premi_fac");
            row0.createCell(11).setCellValue("komisi_fac");
            row0.createCell(12).setCellValue("tsi_faco");
            row0.createCell(13).setCellValue("premi_faco");
            row0.createCell(14).setCellValue("komisi_faco");
            row0.createCell(15).setCellValue("tsi_faco1");
            row0.createCell(16).setCellValue("premi_faco1");
            row0.createCell(17).setCellValue("komisi_faco1");
            row0.createCell(18).setCellValue("tsi_faco2");
            row0.createCell(19).setCellValue("premi_faco2");
            row0.createCell(20).setCellValue("komisi_faco2");
            row0.createCell(21).setCellValue("tsi_faco3");
            row0.createCell(22).setCellValue("premi_faco3");
            row0.createCell(23).setCellValue("komisi_faco3");
            row0.createCell(24).setCellValue("tsi_jp");
            row0.createCell(25).setCellValue("premi_jp");
            row0.createCell(26).setCellValue("komisi_jp");
            row0.createCell(27).setCellValue("tsi_kscbi");
            row0.createCell(28).setCellValue("premi_kscbi");
            row0.createCell(29).setCellValue("komisi_kscbi");
            row0.createCell(30).setCellValue("tsi_park");
            row0.createCell(31).setCellValue("premi_park");
            row0.createCell(32).setCellValue("komisi_park");
            row0.createCell(33).setCellValue("tsi_qs");
            row0.createCell(34).setCellValue("premi_qs");
            row0.createCell(35).setCellValue("komisi_qs");
            row0.createCell(36).setCellValue("tsi_qs_aba");
            row0.createCell(37).setCellValue("premi_qs_aba");
            row0.createCell(38).setCellValue("komisi_qs_aba");
            row0.createCell(39).setCellValue("tsi_spl");
            row0.createCell(40).setCellValue("premi_spl");
            row0.createCell(41).setCellValue("komisi_spl");
            row0.createCell(42).setCellValue("tsi_spl_aba");
            row0.createCell(43).setCellValue("premi_spl_aba");
            row0.createCell(44).setCellValue("komisi_spl_aba");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(getCostCenter(h.getFieldValueByFieldNameST("cc_code")).getStDescription());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").toString());
            row.createCell(3).setCellValue(LanguageManager.getInstance().translate(getPolicyType(h.getFieldValueByFieldNameBD("pol_type_id").toString()).getStDescription()));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("tsi_bpdan").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_bpdan").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("komisi_bpdan").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco1").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("premi_faco1").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco1").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco2").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_faco2").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco2").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco3").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("premi_faco3").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco3").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("tsi_jp").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_jp").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("komisi_jp").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("tsi_kscbi").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("premi_kscbi").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("komisi_kscbi").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("tsi_park").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("premi_park").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("komisi_park").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("premi_qs").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs_aba").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("premi_qs_aba").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs_aba").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl_aba").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("premi_spl_aba").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl_aba").doubleValue());

        }

        for (int i = 0; i < list2.size(); i++) {
            HashDTO h = (HashDTO) list2.get(i);

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("cc_code");
            row0.createCell(1).setCellValue("cabang");
            row0.createCell(2).setCellValue("pol_type_id");
            row0.createCell(3).setCellValue("cob");
            row0.createCell(4).setCellValue("tsi_bpdan");
            row0.createCell(5).setCellValue("premi_bpdan");
            row0.createCell(6).setCellValue("komisi_bpdan");
            row0.createCell(7).setCellValue("tsi_fac");
            row0.createCell(8).setCellValue("premi_fac");
            row0.createCell(9).setCellValue("komisi_fac");
            row0.createCell(10).setCellValue("tsi_faco");
            row0.createCell(11).setCellValue("premi_faco");
            row0.createCell(12).setCellValue("komisi_faco");
            row0.createCell(13).setCellValue("tsi_faco1");
            row0.createCell(14).setCellValue("premi_faco1");
            row0.createCell(15).setCellValue("komisi_faco1");
            row0.createCell(16).setCellValue("tsi_faco2");
            row0.createCell(17).setCellValue("premi_faco2");
            row0.createCell(18).setCellValue("komisi_faco2");
            row0.createCell(19).setCellValue("tsi_faco3");
            row0.createCell(20).setCellValue("premi_faco3");
            row0.createCell(21).setCellValue("komisi_faco3");
            row0.createCell(22).setCellValue("tsi_jp");
            row0.createCell(23).setCellValue("premi_jp");
            row0.createCell(24).setCellValue("komisi_jp");
            row0.createCell(25).setCellValue("tsi_kscbi");
            row0.createCell(26).setCellValue("premi_kscbi");
            row0.createCell(27).setCellValue("komisi_kscbi");
            row0.createCell(28).setCellValue("tsi_park");
            row0.createCell(29).setCellValue("premi_park");
            row0.createCell(30).setCellValue("komisi_park");
            row0.createCell(31).setCellValue("tsi_qs");
            row0.createCell(32).setCellValue("premi_qs");
            row0.createCell(33).setCellValue("komisi_qs");
            row0.createCell(34).setCellValue("tsi_qs_aba");
            row0.createCell(35).setCellValue("premi_qs_aba");
            row0.createCell(36).setCellValue("komisi_qs_aba");
            row0.createCell(37).setCellValue("tsi_spl");
            row0.createCell(38).setCellValue("premi_spl");
            row0.createCell(39).setCellValue("komisi_spl");
            row0.createCell(40).setCellValue("tsi_spl_aba");
            row0.createCell(41).setCellValue("premi_spl_aba");
            row0.createCell(42).setCellValue("komisi_spl_aba");

            //bikin isi cell
            XSSFRow row = sheet2.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(getCostCenter(h.getFieldValueByFieldNameST("cc_code")).getStDescription());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").toString());
            row.createCell(3).setCellValue(LanguageManager.getInstance().translate(getPolicyType(h.getFieldValueByFieldNameBD("pol_type_id").toString()).getStDescription()));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("tsi_bpdan").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("premi_bpdan").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("komisi_bpdan").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_faco").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco1").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_faco1").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco1").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco2").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("premi_faco2").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco2").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco3").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi_faco3").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco3").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("tsi_jp").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("premi_jp").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("komisi_jp").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("tsi_kscbi").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("premi_kscbi").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("komisi_kscbi").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("tsi_park").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_park").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("komisi_park").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("premi_qs").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("tsi_qs_aba").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("premi_qs_aba").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("komisi_qs_aba").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("premi_spl").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("tsi_spl_aba").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("premi_spl_aba").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("komisi_spl_aba").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }
}

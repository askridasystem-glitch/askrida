/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionUtilitiesReportForm
 * Author:  Denny Mahendra
 * Created: Aug 15, 2006 11:17:20 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.InsurancePolicyTypeGroupView;
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
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.care.model.AdmLinkClaim;
import com.webfin.care.model.AdmLinkPremi;
import com.webfin.entity.model.EntityView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsuranceItemsView;
import com.webfin.insurance.model.InsurancePolicyCoinsView;
import com.webfin.insurance.model.InsurancePolicyInwardDetailView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyTreatyView;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import java.math.BigInteger;
import javax.ejb.SessionContext;


import java.sql.PreparedStatement;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;

public class ProductionUtilitiesIFRS17ReportForm extends Form {

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
    private Date appDateFrom;
    private Date appDateTo;
    private Date journalDateFrom;
    private Date journalDateTo;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeID;
    private String stInsCompanyID;
    private String stInsCompanyName;
    private String stEntityName;
    private String stRISlip;
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
    private String stBranch;// = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc;// = SessionManager.getInstance().getSession().getCostCenterDesc();
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
    private String stMarketerID;
    private String stMarketerName;
    private String stZoneCode;
    private String stZoneCodeName;
    private String stPolicyTypeGroupDesc;
    private String stReportTypeOfFile;
    private String stFileName;
    private String stAuthorized;
    private String stPostCodeDesc;
    private String stRiskCodeName;
    String stTreatyYear = "";
    private DTOList list;
    private String stABAFlag = "N";
    private String stIndex = "N";
    private String stRegion; //= SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc;// = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stPolCredit;
    private boolean canNavigateBranch;// = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion;// = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionUtilitiesIFRS17ReportForm.class);

    private String stPolicyID;

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
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

    public void clickPrint() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        loadFormList();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final ArrayList plist = new ArrayList();

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stPolicyTypeID);

        plist.add(stReport + "_" + stPolicyTypeGroupID);

        plist.add(stReport + "_" + stFltTreatyType);

        plist.add(stReport + "_" + stIndex);

        plist.add(stReport + "_" + stRISlip);

        plist.add(stReport);

        String urx = null;

        //logger.logDebug("printPolicy: scanlist:"+plist);

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

        if (exportID != null) {
            final Method m = this.getClass().getMethod(exportID, null);

            m.invoke(this, null);
        }

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

    public void EXPORT_BPPDAN() throws Exception {

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
            row0.createCell(6).setCellValue("pol_id");
            row0.createCell(7).setCellValue("cust_name");
            row0.createCell(8).setCellValue("postal_code");
            row0.createCell(9).setCellValue("zone_code");
            row0.createCell(10).setCellValue("ref1");
            row0.createCell(11).setCellValue("risk_class");
            row0.createCell(12).setCellValue("ins_risk_cat_code");
            row0.createCell(13).setCellValue("ref3");
            row0.createCell(14).setCellValue("description");
            row0.createCell(15).setCellValue("ref5");
            row0.createCell(16).setCellValue("ccy");
            row0.createCell(17).setCellValue("ccy_rate");
            row0.createCell(18).setCellValue("insured_amount");
            row0.createCell(19).setCellValue("premi_total");
            row0.createCell(20).setCellValue("treaty_limit");
            row0.createCell(21).setCellValue("exc_risk_flag");
            row0.createCell(22).setCellValue("building");
            row0.createCell(23).setCellValue("machine");
            row0.createCell(24).setCellValue("stock");
            row0.createCell(25).setCellValue("other");
            row0.createCell(26).setCellValue("total_tsi");
            row0.createCell(27).setCellValue("premi_rate");
            row0.createCell(28).setCellValue("tsi_or");
            row0.createCell(29).setCellValue("premi_or");
            row0.createCell(30).setCellValue("komisi_or");
            row0.createCell(31).setCellValue("tsi_bppdan");
            row0.createCell(32).setCellValue("premi_bppdan");
            row0.createCell(33).setCellValue("komisi_bppdan");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("u_year"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("postal_code"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("zone_code"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("ins_risk_cat_code"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("ref3"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("exc_risk_flag"));
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("total_tsi").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameST("premi_rate"));
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("tsi_or").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_or").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("komisi_or").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("tsi_bppdan").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("premi_bppdan").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("komisi_bppdan").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_MAIPARK() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");


        int norut = 0;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Running No. (1)");
            row0.createCell(1).setCellValue("Main Policy Cover (2)");
            row0.createCell(2).setCellValue("Original Policy No. (3)");
            row0.createCell(3).setCellValue("Endorsement No. (4)");
            row0.createCell(4).setCellValue("Endorsement Count (5)");
            row0.createCell(5).setCellValue("Risk No. (6)");
            row0.createCell(6).setCellValue("Name Of Insured (7)");
            row0.createCell(7).setCellValue("Location of Insured (8)");
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

            norut++;

            if (i > 0) {
                HashDTO s = (HashDTO) list.get(i - 1);
                String nopol = s.getFieldValueByFieldNameST("nopol");
                String nopol2 = h.getFieldValueByFieldNameST("nopol");
                if (!nopol.equalsIgnoreCase(nopol2)) {

                    norut = 1;
                }
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ref2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(5).setCellValue(String.valueOf(norut));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("alamat"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("zona_gempa"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("ref10"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ref11"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("ref12"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("postal_code"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("occupation_code"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("build_cat"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("class_construction"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("year_build"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ref18"));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("total_tsi").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("coins_status"));
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameST("coins_share"));
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("ref29"));
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("status_policy"));
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameST("ref31"));
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameST("ref34"));
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("ref35"));
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameST("ref36"));
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("ref37"));
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("ref38"));
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("ref39"));
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("ref40"));
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("ref41"));
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("ref42"));
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("under_year"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("ref44"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_CONS() throws Exception {

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
            row0.createCell(6).setCellValue("pol_id");
            row0.createCell(7).setCellValue("cust_name");
            row0.createCell(8).setCellValue("postal_code");
            row0.createCell(9).setCellValue("zone_code");
            row0.createCell(10).setCellValue("ref1");
            row0.createCell(11).setCellValue("risk_class");
            row0.createCell(12).setCellValue("ins_risk_cat_code");
            row0.createCell(13).setCellValue("ref3");
            row0.createCell(14).setCellValue("description");
            row0.createCell(15).setCellValue("ref5");
            row0.createCell(16).setCellValue("building");
            row0.createCell(17).setCellValue("machine");
            row0.createCell(18).setCellValue("stock");
            row0.createCell(19).setCellValue("other");
            row0.createCell(20).setCellValue("total_tsi");
            row0.createCell(21).setCellValue("premi_rate");
            row0.createCell(22).setCellValue("tsi_amount");
            row0.createCell(23).setCellValue("premi_amount");
            row0.createCell(24).setCellValue("komisi_amount");



            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("u_year"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("postal_code"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("zone_code"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("ins_risk_cat_code"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("ref3"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("building").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("machine").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("stock").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("other").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("total_tsi").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("premi_rate"));
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("tsi_amount").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("premi_amount").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("komisi_amount").doubleValue());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_FAC() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("kode_pos");
            row0.createCell(1).setCellValue("risk_code");
            row0.createCell(2).setCellValue("risk_class");
            row0.createCell(3).setCellValue("ins_pol_tre_det_id");
            row0.createCell(4).setCellValue("ri_slip_no");
            row0.createCell(5).setCellValue("treaty_limit");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("period_start");
            row0.createCell(8).setCellValue("period_end");
            row0.createCell(9).setCellValue("pol_id");
            row0.createCell(10).setCellValue("pol_no");
            row0.createCell(11).setCellValue("cust_name");
            row0.createCell(12).setCellValue("ins_pol_obj_id");
            row0.createCell(13).setCellValue("objek");
            row0.createCell(14).setCellValue("pol_type_id");
            row0.createCell(15).setCellValue("short_desc");
            row0.createCell(16).setCellValue("ref_ent_id");
            row0.createCell(17).setCellValue("ccy");
            row0.createCell(18).setCellValue("ccy_rate");
            row0.createCell(19).setCellValue("ent_name");
            row0.createCell(20).setCellValue("tsi_polis");
            row0.createCell(21).setCellValue("tsi_askrida");
            row0.createCell(22).setCellValue("tsi_per_objek");
            row0.createCell(23).setCellValue("tsi_fac");
            row0.createCell(24).setCellValue("premi_polis");
            row0.createCell(25).setCellValue("premi_askrida");
            row0.createCell(26).setCellValue("premi_per_objek");
            row0.createCell(27).setCellValue("premi_fac");
            row0.createCell(28).setCellValue("komisi_fac");
            row0.createCell(29).setCellValue("premi_jp");
            row0.createCell(30).setCellValue("komisi_jp");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kode_pos")));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("ri_slip_no"));
            if (h.getFieldValueByFieldNameBD("treaty_limit") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit").doubleValue());
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_obj_id").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("objek"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ref_ent_id"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            if (h.getFieldValueByFieldNameBD("tsi_polis") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("tsi_polis").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_askrida") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tsi_askrida").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_per_objek") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("tsi_per_objek").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi_fac") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("tsi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_polis") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("premi_polis").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_askrida") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_askrida").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_per_objek") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("premi_per_objek").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_fac") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_fac") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_jp") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_jp") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("komisi_jp").doubleValue());
            }



        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_AUTOFAC1() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("kode_pos");
            row0.createCell(1).setCellValue("risk_code");
            row0.createCell(2).setCellValue("risk_class");
            row0.createCell(3).setCellValue("ins_pol_tre_det_id");
            row0.createCell(4).setCellValue("ri_slip_no");
            row0.createCell(5).setCellValue("treaty_limit");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("period_start");
            row0.createCell(8).setCellValue("period_end");
            row0.createCell(9).setCellValue("pol_id");
            row0.createCell(10).setCellValue("pol_no");
            row0.createCell(11).setCellValue("cust_name");
            row0.createCell(12).setCellValue("ins_pol_obj_id");
            row0.createCell(13).setCellValue("objek");
            row0.createCell(14).setCellValue("pol_type_id");
            row0.createCell(15).setCellValue("short_desc");
            row0.createCell(16).setCellValue("ref_ent_id");
            row0.createCell(17).setCellValue("ccy");
            row0.createCell(18).setCellValue("ccy_rate");
            row0.createCell(19).setCellValue("ent_name");
            row0.createCell(20).setCellValue("tsi_polis");
            row0.createCell(21).setCellValue("tsi_askrida");
            row0.createCell(22).setCellValue("tsi_per_objek");
            row0.createCell(23).setCellValue("tsi_autofac1");
            row0.createCell(24).setCellValue("premi_polis");
            row0.createCell(25).setCellValue("premi_askrida");
            row0.createCell(26).setCellValue("premi_per_objek");
            row0.createCell(27).setCellValue("premi_autofac1");
            row0.createCell(28).setCellValue("komisi_autofac1");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kode_pos")));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("risk_class"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_tre_det_id").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("ri_slip_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("treaty_limit").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_obj_id").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("objek"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ref_ent_id"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("tsi_polis").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tsi_askrida").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("tsi_per_objek").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("tsi_faco1").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("premi_polis").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("premi_askrida").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("premi_per_objek").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("premi_faco1").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("komisi_faco1").doubleValue());



        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public DTOList EXCEL_BPPDAN() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4) as u_year,a.pol_no,a.status,a.pol_id,a.cust_name, "
                + "b.ref9d as postal_code,getzone2(b.ref6d) as zone_code,b.ref1,b.risk_class,c.ins_risk_cat_code, "
                + "b.ref3,c.description,b.ref5,a.ccy,a.ccy_rate,a.insured_amount,a.premi_total, "
                + "(case b.risk_class when 1 then c.tre_limit1 when 2 then c.tre_limit2 when 3 then c.tre_limit3 else 0 end) as treaty_limit,c.exc_risk_flag, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + "	as building,"
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (39,40,106),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + "	as machine, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (56,57,58,90,91,92,107),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + "	as stock, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id) "
                + "	as other,b.insured_amount as total_tsi, "
                + "sum(round(i.premi_rate,4))||' '||a.rate_method_desc as premi_rate, "
                + "coalesce(round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2),0) as tsi_or, "
                + "coalesce(round(sum(checkreas(j.treaty_type='OR',i.premi_amount)),2),0) as premi_or, "
                + "coalesce(round(sum(checkreas(j.treaty_type='OR',i.ricomm_amt)),2),0) as komisi_or, "
                + "coalesce(round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2),0) as tsi_bppdan, "
                + "coalesce(round(sum(checkreas(j.treaty_type='BPDAN',i.premi_amount)),2),0) as premi_bppdan, "
                + "coalesce(round(sum(checkreas(j.treaty_type='BPDAN',i.ricomm_amt)),2),0) as komisi_bppdan ");


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

        sqa.addClause("(a.pol_type_id=1 or a.pol_type_id=81 or a.pol_type_id=19)");

        sqa.addClause("j.treaty_type in ('OR','BPDAN')");

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

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
        }

        final String sql = sqa.getSQL() + " group by a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4),a.pol_no,a.status,a.pol_id,a.cust_name,"
                + "a.insured_amount,a.premi_total, b.ref9d,b.ref6d,b.ref1,b.risk_class,b.ref3,c.description,b.ref5,c.ins_risk_cat_code,c.exc_risk_flag,a.ccy,a.ccy_rate,b.insured_amount,"
                + "a.rate_method_desc, c.tre_limit1,c.tre_limit2,c.tre_limit3,b.ins_pol_obj_id,a.pol_type_id "
                + "order by a.policy_date,a.period_start,a.pol_no,b.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_CONS() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.policy_date,a.period_start,a.period_end,substr(a.period_start::text,1,4) as u_year,a.pol_no,a.status,a.pol_id,a.cust_name, "
                + "getpostcode(a.pol_type_id,b.ref6d,b.ref9d) as postal_code,getzone2(b.ref6d) as zone_code,b.ref1,b.risk_class,c.ins_risk_cat_code,b.ref3,c.description,b.ref5, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (6,105),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id)  "
                + "	as building, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (39,106),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id)  "
                + "	as machine, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id in (56,107),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id)  "
                + "	as stock, "
                + "(	select coalesce(round(sum(getkoas(z.ins_tsi_cat_id not in (6,39,56,105,106,107),z.insured_amount)),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_obj y on y.pol_id = x.pol_id "
                + "	inner join ins_pol_tsi z on z.ins_pol_obj_id = y.ins_pol_obj_id "
                + "	where y.ins_pol_obj_id=b.ins_pol_obj_id)  "
                + "	as other,b.insured_amount as total_tsi, "
                + "(select round(sum(x.rate),4)||' '||a.rate_method_desc "
                + "from ins_policy y "
                + "inner join ins_pol_cover x on x.pol_id = y.pol_id "
                + "where y.pol_id = a.pol_id) as premi_rate, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2),0) as tsi_amount, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2),0) as premi_amount, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2),0) as komisi_amount");


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

        sqa.addClause("a.pol_type_id=2");

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

        final String sql = sqa.getSQL() + " group by a.policy_date,a.period_start,a.period_end,a.pol_no,a.status,a.pol_id,a.cust_name, "
                + "a.pol_type_id,b.ref6d,b.ref9d,b.ref1,b.risk_class,c.ins_risk_cat_code,b.ref3,c.description,b.ref5,b.ins_pol_obj_id, "
                + "b.insured_amount,a.rate_method_desc "
                + "order by a.pol_no,b.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_FAC() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("c.ref6d as kode_pos,m.ins_risk_cat_code as risk_code,c.risk_class,h.ins_pol_tre_det_id,i.ri_slip_no, "
                + "(case c.risk_class when '1' then m.tre_limit1 when '2' then m.tre_limit2 when '3' then m.tre_limit3 else '0' end) as treaty_limit, "
                + "a.policy_date,a.period_start,a.period_end,a.pol_id,a.pol_no,a.cust_name,c.ins_pol_obj_id, "
                + "c.ref1 as objek,a.pol_type_id,f.short_desc,k.reas_ent_id as ref_ent_id,a.ccy,a.ccy_rate,k.ent_name,a.insured_amount as tsi_polis, "
                + "(	select coalesce(round(sum(y.amount),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as tsi_askrida, "
                + "coalesce(c.insured_amount,0) as tsi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2),0) as tsi_fac, "
                + "coalesce(a.premi_total,0) as premi_polis, "
                + "(	select coalesce(sum(y.premi_amt),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as premi_askrida, "
                + "coalesce(c.premi_total,0) as premi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2),0) as premi_fac, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2),0) as komisi_fac, "
                + "coalesce(round(sum(checkreas(j.treaty_type='JP',i.premi_amount)),2),0) as premi_jp, "
                + "coalesce(round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt)),2),0) as komisi_jp ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "left join ent_master b on b.ent_id = a.entity_id "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "left join ins_risk_cat m on m.ins_risk_cat_id = c.ins_risk_cat_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("j.treaty_type in ('FAC','JP')");

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

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.period_start,a.period_end,a.pol_id,a.policy_date,a.cust_name,"
                + "c.ref1,c.ref6d,a.pol_type_id,a.ccy,a.ccy_rate,m.ins_risk_cat_code,c.risk_class,"
                + "m.tre_limit1,m.tre_limit2,m.tre_limit3,a.pol_type_id,c.ins_pol_obj_id,c.insured_amount,c.premi_total,"
                + "f.short_desc,a.insured_amount,a.premi_total,c.refn2,a.ccy_rate_treaty,i.ri_slip_no,k.ent_name,k.reas_ent_id,h.ins_pol_tre_det_id "
                + "order by pol_type_id, pol_no,ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_AUTOFAC1() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("c.ref6d as kode_pos,m.ins_risk_cat_code as risk_code,c.risk_class,h.ins_pol_tre_det_id,i.ri_slip_no, "
                + "(case c.risk_class when '1' then m.tre_limit1 when '2' then m.tre_limit2 when '3' then m.tre_limit3 else '0' end) as treaty_limit, "
                + "a.policy_date,a.period_start,a.period_end,a.pol_id,a.pol_no,a.cust_name,c.ins_pol_obj_id, "
                + "c.ref1 as objek,a.pol_type_id,f.short_desc,k.reas_ent_id as ref_ent_id,a.ccy,a.ccy_rate,k.ent_name,a.insured_amount as tsi_polis, "
                + "(	select coalesce(round(sum(y.amount),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as tsi_askrida, "
                + "coalesce(c.insured_amount,0) as tsi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO1',i.tsi_amount)),2),0) as tsi_faco1, "
                + "coalesce(a.premi_total,0) as premi_polis, "
                + "(	select coalesce(sum(y.premi_amt),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as premi_askrida, "
                + "coalesce(c.premi_total,0) as premi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO1',i.premi_amount)),2),0) as premi_faco1, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO1',i.ricomm_amt)),2),0) as komisi_faco1 ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "left join ent_master b on b.ent_id = a.entity_id "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "left join ins_risk_cat m on m.ins_risk_cat_id = c.ins_risk_cat_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("j.treaty_type = 'FACO1'");

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

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.period_start,a.period_end,a.pol_id,a.policy_date,a.cust_name,"
                + "c.ref1,c.ref6d,a.pol_type_id,a.ccy,a.ccy_rate,m.ins_risk_cat_code,c.risk_class,"
                + "m.tre_limit1,m.tre_limit2,m.tre_limit3,a.pol_type_id,c.ins_pol_obj_id,c.insured_amount,c.premi_total,"
                + "f.short_desc,a.insured_amount,a.premi_total,c.refn2,a.ccy_rate_treaty,i.ri_slip_no,k.ent_name,k.reas_ent_id,h.ins_pol_tre_det_id "
                + "order by pol_type_id, pol_no,ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList AUTOFAC1() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,a.cust_name,a.pol_type_id,getperiod(a.pol_type_id = 59,c.refd2,a.period_start) as period_start,a.ccy,"
                + "getperiod(a.pol_type_id = 59,c.refd3,a.period_end) as period_end,(c.insured_amount*a.ccy_rate) as insured_amount,"
                + "(c.premi_total*a.ccy_rate) as premi_total,coalesce(c.ref3d,c.ref1) as ref1,m.ent_id,m.ent_name,"
                + "sum(i.tsi_amount*a.ccy_rate) as insured_amount_e,sum(i.premi_amount*a.ccy_rate) as premi_netto,"
                + "sum(i.ricomm_amt*a.ccy_rate) as nd_comm1,sum((i.premi_amount*a.ccy_rate)-(i.ricomm_amt*a.ccy_rate)) as premi_base ");


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

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause("	j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        sqa.addClause("	j.treaty_type = 'FACO1'");

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
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.cust_name,m.ent_id,m.ent_name,a.pol_type_id,a.period_start,a.period_end,"
                + "a.insured_amount,a.premi_total,c.insured_amount,c.premi_total,a.ccy_rate,c.ref3d,c.ref1,c.ins_pol_obj_id,a.ccy "
                + " order by a.pol_no,c.ins_pol_obj_id ";

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

    public DTOList EXCEL_FACO() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("c.ref6d as kode_pos,m.ins_risk_cat_code as risk_code,c.risk_class,h.ins_pol_tre_det_id,i.ri_slip_no, "
                + "(case c.risk_class when '1' then m.tre_limit1 when '2' then m.tre_limit2 when '3' then m.tre_limit3 else '0' end) as treaty_limit, "
                + "a.policy_date,a.period_start,a.period_end,a.pol_id,a.pol_no,a.cust_name,c.ins_pol_obj_id, "
                + "c.ref1 as objek,a.pol_type_id,f.short_desc,k.reas_ent_id as ref_ent_id,a.ccy,a.ccy_rate,k.ent_name,a.insured_amount as tsi_polis, "
                + "(	select coalesce(round(sum(y.amount),0),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as tsi_askrida, "
                + "coalesce(c.insured_amount,0) as tsi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2),0) as tsi_fac, "
                + "coalesce(a.premi_total,0) as premi_polis, "
                + "(	select coalesce(sum(y.premi_amt),0) "
                + "	from ins_policy x "
                + "	inner join ins_pol_coins y on y.policy_id = x.pol_id and y.entity_id=1 "
                + "	where y.policy_id=a.pol_id) "
                + "	as premi_askrida, "
                + "coalesce(c.premi_total,0) as premi_per_objek, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.premi_amount)),2),0) as premi_fac, "
                + "coalesce(round(sum(checkreas(j.treaty_type='FACO',i.ricomm_amt)),2),0) as komisi_fac ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }
        sqa.addQuery("from ins_policy a "
                + "left join ent_master b on b.ent_id = a.entity_id "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "left join ins_risk_cat m on m.ins_risk_cat_id = c.ins_risk_cat_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("j.treaty_type = 'FACO'");

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

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.period_start,a.period_end,a.pol_id,a.policy_date,a.cust_name,"
                + "c.ref1,c.ref6d,a.pol_type_id,a.ccy,a.ccy_rate,m.ins_risk_cat_code,c.risk_class,"
                + "m.tre_limit1,m.tre_limit2,m.tre_limit3,a.pol_type_id,c.ins_pol_obj_id,c.insured_amount,c.premi_total,"
                + "f.short_desc,a.insured_amount,a.premi_total,c.refn2,a.ccy_rate_treaty,i.ri_slip_no,k.ent_name,k.reas_ent_id,h.ins_pol_tre_det_id "
                + "order by pol_type_id, pol_no,ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_SAHAM() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" substr((getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start)))::text,1,4) as uy,"
                + "substr((getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end)))::text,1,4) as uy2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                + "a.ins_policy_type_grp_id, a.pol_type_id, f.short_desc,a.cc_code,c.ins_pol_obj_id,c.order_no,"
                + "j.treaty_type,a.policy_date,a.pol_id,quote_ident(a.pol_no) as pol_no,k.reas_ent_id as ref_ent_id,k.short_name as reasuradur,a.ccy,a.ccy_rate,i.sharepct,"
                + "coalesce(a.insured_amount,0) as insured_amount,coalesce(a.premi_total,0) as premi_total,"
                + "coalesce(c.insured_amount,0) as insured_amount_obj,coalesce(c.premi_total,0) as premi_total_obj,"
                + "coalesce(i.tsi_amount,0) as tsi_reas,coalesce(i.premi_amount,0) as premi_reas,coalesce(i.ricomm_amt,0) as komisi_reas, "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id1 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc1,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id1) as kode_captive1,  "
                + "i.premi_cover1,round(((i.premi_cover1*i.ricomm_rate)/100),0) as komisi1,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id2 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc2,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id2) as kode_captive2,  "
                + "i.premi_cover2,round(((i.premi_cover2*i.ricomm_rate)/100),0) as komisi2,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id3 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc3,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id3) as kode_captive3,  "
                + "i.premi_cover3,round(((i.premi_cover3*i.ricomm_rate)/100),0) as komisi3,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id4 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc4,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id4) as kode_captive4,  "
                + "i.premi_cover4,round(((i.premi_cover4*i.ricomm_rate)/100),0) as komisi4,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id5 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc5,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id5) as kode_captive5,  "
                + "i.premi_cover5,round(((i.premi_cover5*i.ricomm_rate)/100),0) as komisi5,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id6 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc6,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id6) as kode_captive6,  "
                + "i.premi_cover6,round(((i.premi_cover6*i.ricomm_rate)/100),0) as komisi6,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id7 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc7,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id7) as kode_captive7,  "
                + "i.premi_cover7,round(((i.premi_cover7*i.ricomm_rate)/100),0) as komisi7,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id8 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc8,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id8) as kode_captive8,  "
                + "i.premi_cover8,round(((i.premi_cover8*i.ricomm_rate)/100),0) as komisi8,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id9 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc9,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id9) as kode_captive9,  "
                + "i.premi_cover9,round(((i.premi_cover9*i.ricomm_rate)/100),0) as komisi9,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id10 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc10,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id10) as kode_captive10,  "
                + "i.premi_cover10,round(((i.premi_cover10*i.ricomm_rate)/100),0) as komisi10,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id11 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc11,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id11) as kode_captive11,  "
                + "i.premi_cover11,round(((i.premi_cover11*i.ricomm_rate)/100),0) as komisi11,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id12 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc12,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id12) as kode_captive12,  "
                + "i.premi_cover12,round(((i.premi_cover12*i.ricomm_rate)/100),0) as komisi12,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id13 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc13,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id13) as kode_captive13,  "
                + "i.premi_cover13,round(((i.premi_cover13*i.ricomm_rate)/100),0) as komisi13,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id14 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc14,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id14) as kode_captive14,  "
                + "i.premi_cover14,round(((i.premi_cover14*i.ricomm_rate)/100),0) as komisi14,  "
                + "(select l.description "
                + "from ins_cover l "
                + "inner join ins_pol_ri z on z.ins_cover_id15 = l.ins_cover_id  "
                + "where z.ins_pol_ri_id = i.ins_pol_ri_id) as desc15,getcaptive(substr(a.pol_no,2,1),i.ins_cover_id15) as kode_captive15,  "
                + "i.premi_cover15,round(((i.premi_cover15*i.ricomm_rate)/100),0) as komisi15,d.mutation_date as tgl_pembukuan ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery("from ins_policy a "
                + "left join ent_master b on b.ent_id = a.entity_id "
                + "inner join ins_pol_obj c on c.pol_id=a.pol_id " + clauseObj
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id  "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "left join ar_invoice d on d.attr_pol_id = a.pol_id and i.member_ent_id = d.ent_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR','FAC','BPDAN','PARK','FACO')");

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
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (journalDateFrom != null) {
            sqa.addClause("date_trunc('day',d.mutation_date) >= '" + journalDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (getJournalDateTo() != null) {
            sqa.addClause("date_trunc('day',d.mutation_date) <= '" + getJournalDateTo() + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
        }

        String sql = " select * from ( " + sqa.getSQL() + " order by a.pol_type_id, a.pol_no,c.ins_pol_obj_id,k.reas_ent_id ) a "
                + " where (premi_reas <> 0 or komisi_reas <> 0) ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.period_start) >= '" + periodFrom + "'";
            //sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.period_start) <= '" + periodTo + "'";
            //sqa.addPar(periodTo);
        }

        sql = sql + " order by a.pol_type_id, a.pol_no,a.ins_pol_obj_id,a.ref_ent_id ";

        SQLUtil S = new SQLUtil();

        String nama_file = "reins_saham_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
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

    public void EXPORT_SAHAM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal poltypegrp = new BigDecimal(0);

        String name = "";

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            poltypegrp = h.getFieldValueByFieldNameBD("ins_policy_type_grp_id");

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("u_year");
            row0.createCell(1).setCellValue("u_year2");
            row0.createCell(2).setCellValue("pol_type_id");
            row0.createCell(3).setCellValue("short_desc");
            row0.createCell(4).setCellValue("cc_code");
            row0.createCell(5).setCellValue("treaty_type");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("pol_no");
            row0.createCell(8).setCellValue("ref_ent_id");
            row0.createCell(9).setCellValue("reasuradur");
            row0.createCell(10).setCellValue("ccy");
            row0.createCell(11).setCellValue("ccy_rate");
            row0.createCell(12).setCellValue("sharepct");
            row0.createCell(13).setCellValue("tsi_reas");
            row0.createCell(14).setCellValue("premi_reas");
            row0.createCell(15).setCellValue("komisi_reas");
            row0.createCell(16).setCellValue("desc1");
            row0.createCell(17).setCellValue("kode_captive1");
            row0.createCell(18).setCellValue("premi_cover1");
            row0.createCell(19).setCellValue("komisi1");
            row0.createCell(20).setCellValue("desc2");
            row0.createCell(21).setCellValue("kode_captive2");
            row0.createCell(22).setCellValue("premi_cover2");
            row0.createCell(23).setCellValue("komisi2");
            row0.createCell(24).setCellValue("desc3");
            row0.createCell(25).setCellValue("kode_captive3");
            row0.createCell(26).setCellValue("premi_cover3");
            row0.createCell(27).setCellValue("komisi3");
            row0.createCell(28).setCellValue("desc4");
            row0.createCell(29).setCellValue("kode_captive4");
            row0.createCell(30).setCellValue("premi_cover4");
            row0.createCell(31).setCellValue("komisi4");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("u_year"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("u_year2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            if (h.getFieldValueByFieldNameST("ref_ent_id") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ref_ent_id"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("reasuradur"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            if (h.getFieldValueByFieldNameBD("sharepct") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("sharepct").doubleValue());
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("tsi_reas").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("premi_reas").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("komisi_reas").doubleValue());
            if (Tools.isEqual(poltypegrp, new BigDecimal(1))) {
                row.createCell(16).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc1")));
                if (h.getFieldValueByFieldNameST("kode_captive1") != null) {
                    row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("kode_captive1"));
                }
                if (h.getFieldValueByFieldNameBD("premi_cover1") != null) {
                    row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("premi_cover1").doubleValue());
                }
                if (h.getFieldValueByFieldNameBD("komisi1") != null) {
                    row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("komisi1").doubleValue());
                }
                if (h.getFieldValueByFieldNameST("desc2") != null) {
                    row.createCell(20).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc2")));
                }
                if (h.getFieldValueByFieldNameST("kode_captive2") != null) {
                    row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("kode_captive2"));
                }
                if (h.getFieldValueByFieldNameBD("premi_cover2") != null) {
                    row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("premi_cover2").doubleValue());
                }
                if (h.getFieldValueByFieldNameBD("komisi2") != null) {
                    row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("komisi2").doubleValue());
                }
                if (h.getFieldValueByFieldNameST("desc3") != null) {
                    row.createCell(24).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc3")));
                }
                if (h.getFieldValueByFieldNameST("kode_captive3") != null) {
                    row.createCell(25).setCellValue(h.getFieldValueByFieldNameST("kode_captive3"));
                }
                if (h.getFieldValueByFieldNameBD("premi_cover3") != null) {
                    row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("premi_cover3").doubleValue());
                }
                if (h.getFieldValueByFieldNameBD("komisi3") != null) {
                    row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("komisi3").doubleValue());
                }
                if (h.getFieldValueByFieldNameST("desc4") != null) {
                    row.createCell(28).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("desc4")));
                }
                if (h.getFieldValueByFieldNameST("kode_captive4") != null) {
                    row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("kode_captive4"));
                }
                if (h.getFieldValueByFieldNameBD("premi_cover4") != null) {
                    row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("premi_cover4").doubleValue());
                }
                if (h.getFieldValueByFieldNameBD("komisi4") != null) {
                    row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("komisi4").doubleValue());
                }
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_MAIPARK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	coalesce(null,'') as ref1,coalesce(null,'') as ref2,a.pol_no as nopol,coalesce(null,'') as ref4,coalesce(null,'') as ref5,c.risk_class as risk_no,a.cust_name,a.cust_address as alamat,c.ref8 as zona_gempa,"
                + "	coalesce(null,'') as ref10,coalesce(null,'') as ref11,coalesce(null,'') as ref12,c.ref9d as postal_code,m.ins_risk_cat_code as occupation_code,c.ref4 as build_cat,"
                + "	c.ref3 as class_construction,c.ref7 as year_build,coalesce(null,'') as ref18,a.period_start,a.period_end,a.ccy,"
                + "	sum(getkoas(d.ins_tsi_cat_id in (6,105),d.insured_amount)) as building,"
                + "	sum(getkoas(d.ins_tsi_cat_id in (39,40,106),d.insured_amount)) as machine,"
                + "	sum(getkoas(d.ins_tsi_cat_id in (56,57,58,90,91,92,107),d.insured_amount)) as stock,"
                + "	sum(getkoas(d.ins_tsi_cat_id in (2,12,21,23,25,27,29,30,33,34,35,44,79,95,97,98,103,108,109),d.insured_amount)) as other,"
                + "	c.insured_amount as total_tsi,"
                + "	a.cover_type_code as coins_status,(round(a.share_pct,2)||' %') as coins_share,coalesce(null,'') as ref29,a.status as status_policy,coalesce(null,'') as ref31,"
                + "	a.insured_amount as tsi,a.premi_total as premi,"
                + "	coalesce(null,'') as ref34,coalesce(null,'') as ref35,coalesce(null,'') as ref36,coalesce(null,'') as ref37,coalesce(null,'') as ref38,coalesce(null,'') as ref39,coalesce(null,'') as ref40,coalesce(null,'') as ref41,coalesce(null,'') as ref42,"
                + "	substr(a.period_start::text,1,4) as under_year,coalesce(null,'') as ref44");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }
        sqa.addQuery("from ins_policy a "
                + "	left join ent_master b on b.ent_id = a.entity_id "
                + "	inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "	inner join ins_pol_tsi d on d.ins_pol_obj_id = c.ins_pol_obj_id "
                + "	inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "	inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "	inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "	inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "	left join ins_risk_cat m on m.ins_risk_cat_id = c.ins_risk_cat_id");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

        sqa.addClause(" a.pol_type_id = 19");

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

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + " group by a.pol_no,a.cust_name,a.cust_address,a.period_start,a.period_end,a.ccy,a.premi_total,c.ins_pol_obj_id,a.policy_date, "
                + "c.insured_amount,a.insured_amount,i.premi_amount,c.ref9d,m.ins_risk_cat_code,c.risk_class,c.ref4,c.ref3,c.ref7,c.ref8,c.ref10, "
                + "a.cover_type_code,a.share_pct,a.status "
                + "order by a.policy_date,a.pol_no,c.ins_pol_obj_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

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

    public String getStBranchName() {
        return stBranchName;
    }

    public void setStBranchName(String stBranchName) {
        this.stBranchName = stBranchName;
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

    public DTOList EXCEL_ABA_PRODUK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.cc_code,a.pol_id,a.nopol,a.pol_no,a.tglpol,a.nama,a.harper,a.preto,a.biapol,a.biamat,a.komisi,a.tglm,a.tgla,"
                + "a.relasi,tglent,a.kali,a.dollar,a.kodeko,a.persko,a.diskon,a.h_fee,a.b_fee,a.entity_id,a.flag,coalesce(a.feebase,0) as feebase ");

        sqa.addQuery("from aba_produk a  "
                + "left join ins_policy b on b.pol_id = a.pol_id");

        //sqa.addClause(" a.flag is null ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("b.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (Tools.isYes(stABAFlag)) {
            sqa.addClause("a.flag = 'Y'");
        }

        if (Tools.isNo(stABAFlag)) {
            sqa.addClause("a.flag is null");
        }

        final String sql = sqa.getSQL() + " order by a.nopol,a.kodeko";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final DTOList m = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        if (Tools.isNo(stABAFlag)) {
            //getRemoteInsurance().updateTransferProduk(m, this);
            //getRemoteInsurance().updateTransferProd(m, this);
        }

        return l;
    }

    public void EXPORT_ABA_PRODUK() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("produk");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_id");
            row0.createCell(1).setCellValue("nopol");
            row0.createCell(2).setCellValue("nobuk");
            row0.createCell(3).setCellValue("nopolm");
            row0.createCell(4).setCellValue("cnoplm");
            row0.createCell(5).setCellValue("tglpol");
            row0.createCell(6).setCellValue("nama");
            row0.createCell(7).setCellValue("harper");
            row0.createCell(8).setCellValue("preto");
            row0.createCell(9).setCellValue("biapol");
            row0.createCell(10).setCellValue("biamat");
            row0.createCell(11).setCellValue("komisi");
            row0.createCell(12).setCellValue("batal");
            row0.createCell(13).setCellValue("tglm");
            row0.createCell(14).setCellValue("tgla");
            row0.createCell(15).setCellValue("relasi");
            row0.createCell(16).setCellValue("tglent");
            row0.createCell(17).setCellValue("flnd");
            row0.createCell(18).setCellValue("cetdn");
            row0.createCell(19).setCellValue("kali");
            row0.createCell(20).setCellValue("dollar");
            row0.createCell(21).setCellValue("fltutup");
            row0.createCell(22).setCellValue("kodeko");
            row0.createCell(23).setCellValue("persko");
            row0.createCell(24).setCellValue("nomorko");
            row0.createCell(25).setCellValue("clerk");
            row0.createCell(26).setCellValue("tglketik");
            row0.createCell(27).setCellValue("flpro");
            row0.createCell(28).setCellValue("diskon");
            row0.createCell(29).setCellValue("h_fee");
            row0.createCell(30).setCellValue("b_fee");
            row0.createCell(31).setCellValue("feebase");
            row0.createCell(32).setCellValue("agen");
            row0.createCell(33).setCellValue("tgltran");
            row0.createCell(34).setCellValue("tglrest");
            row0.createCell(35).setCellValue("entity_id");
            row0.createCell(36).setCellValue("flag");
            row0.createCell(37).setCellValue("cabang");
            row0.createCell(38).setCellValue("pol_no");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            //if(h.getFieldValueByFieldNameST("nopol")!=null)
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            //if(h.getFieldValueByFieldNameDT("tglpol")!=null)
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tglpol"));
            //if(h.getFieldValueByFieldNameST("nama")!=null)
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("nama"));
            //if(h.getFieldValueByFieldNameBD("harper")!=null)
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("harper").doubleValue());
            //if(h.getFieldValueByFieldNameBD("preto")!=null)
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("preto").doubleValue());
            //if(h.getFieldValueByFieldNameBD("biapol")!=null)
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("biapol").doubleValue());
            //if(h.getFieldValueByFieldNameBD("biamat")!=null)
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("biamat").doubleValue());
            //if(h.getFieldValueByFieldNameBD("komisi")!=null)
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("komisi").doubleValue());
            //if(h.getFieldValueByFieldNameDT("tglm")!=null)
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameDT("tglm"));
            //if(h.getFieldValueByFieldNameDT("tgla")!=null)
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameDT("tgla"));
            //if(h.getFieldValueByFieldNameST("relasi")!=null)
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("relasi"));
            if (h.getFieldValueByFieldNameDT("tglent") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameDT("tglent"));
            }
            //if(h.getFieldValueByFieldNameST("kali")!=null)
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("kali"));
            //if(h.getFieldValueByFieldNameBD("dollar")!=null)
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("dollar").doubleValue());
            //if(h.getFieldValueByFieldNameST("diskon")!=null)
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
            if (h.getFieldValueByFieldNameBD("persko") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("persko").doubleValue());
            }
            //if(h.getFieldValueByFieldNameBD("diskon")!=null)
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            //if(h.getFieldValueByFieldNameBD("h_fee")!=null)
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("h_fee").doubleValue());
            //if(h.getFieldValueByFieldNameBD("b_fee")!=null)
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("b_fee").doubleValue());
            if (h.getFieldValueByFieldNameBD("feebase") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("feebase").doubleValue());
            }
            //if(h.getFieldValueByFieldNameBD("entity_id")!=null)
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("entity_id").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("flag"));
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ABA_BAYAR() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_id ");

        sqa.addQuery("from aba_produk a  "
                + "left join ins_policy b on b.pol_id = a.pol_id");

        //sqa.addClause(" a.flag is null ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("b.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (Tools.isYes(stABAFlag)) {
            sqa.addClause("a.flag = 'Y'");
        }

        if (Tools.isNo(stABAFlag)) {
            sqa.addClause("a.flag is null");
        }

        final String sql = " select a.* from aba_bayar1 a where a.pol_id in ( " + sqa.getSQL() + " ) order by a.nopol,a.kodeko";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_ABA_BAYAR() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("bayar");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("nopol");
            row0.createCell(1).setCellValue("konter");
            row0.createCell(2).setCellValue("kodeko");
            row0.createCell(3).setCellValue("nobuk");
            row0.createCell(4).setCellValue("norek");
            row0.createCell(5).setCellValue("preto");
            row0.createCell(6).setCellValue("jtempo");
            row0.createCell(7).setCellValue("komisi");
            row0.createCell(8).setCellValue("diskon");
            row0.createCell(9).setCellValue("h_fee");
            row0.createCell(10).setCellValue("b_fee");
            row0.createCell(11).setCellValue("feebase");
            row0.createCell(12).setCellValue("tglbay");
            row0.createCell(13).setCellValue("bayar");
            row0.createCell(14).setCellValue("keter");
            row0.createCell(15).setCellValue("tglent");
            row0.createCell(16).setCellValue("kodent");
            row0.createCell(17).setCellValue("kodecet");
            row0.createCell(18).setCellValue("nobukdn");
            row0.createCell(19).setCellValue("tglketik");
            row0.createCell(20).setCellValue("tgbre");
            row0.createCell(21).setCellValue("komre");
            row0.createCell(22).setCellValue("tgltran");
            row0.createCell(23).setCellValue("tglrest");
            row0.createCell(24).setCellValue("tgltran1");
            row0.createCell(25).setCellValue("tglrest1");
            row0.createCell(26).setCellValue("pol_id");
            //row0.createCell(27).setCellValue("flag");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("konter").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
            //row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            //row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("norek"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("preto").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("jtempo"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("komisi").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("h_fee").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("b_fee").doubleValue());
            if (h.getFieldValueByFieldNameBD("feebase") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("feebase").doubleValue());
            }
            //row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("tglbay"));
            //row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("bayar").doubleValue());
            //row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("keter"));
            //row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("tglent"));
            //row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("kodent"));
            //row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("kodecet"));
            //row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("nobukdn"));
            //row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("tglketik"));
            //row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("tgbre"));
            //row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("komre").doubleValue());
            //row.createCell(21).setCellValue(h.getFieldValueByFieldNameDT("tgltran"));
            //row.createCell(22).setCellValue(h.getFieldValueByFieldNameDT("tglrest"));
            //row.createCell(23).setCellValue(h.getFieldValueByFieldNameDT("tgltran1"));
            //row.createCell(24).setCellValue(h.getFieldValueByFieldNameDT("tglrest1"));
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            //row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("flag"));


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ABA_HUTANG() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_id ");

        sqa.addQuery("from aba_produk a  "
                + "left join ins_policy b on b.pol_id = a.pol_id");

        //sqa.addClause(" a.flag is null ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("b.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (Tools.isYes(stABAFlag)) {
            sqa.addClause("a.flag = 'Y'");
        }

        if (Tools.isNo(stABAFlag)) {
            sqa.addClause("a.flag is null");
        }

        final String sql = " select a.* from aba_hutang a where a.pol_id in ( " + sqa.getSQL() + " ) order by a.nopol,a.kodeko";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_ABA_HUTANG() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("hutang");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_id");
            row0.createCell(1).setCellValue("nopol");
            row0.createCell(2).setCellValue("konter");
            row0.createCell(3).setCellValue("kodeko");
            row0.createCell(4).setCellValue("nobuk1");
            row0.createCell(5).setCellValue("norek1");
            row0.createCell(6).setCellValue("nila");
            row0.createCell(7).setCellValue("tglb1");
            row0.createCell(8).setCellValue("bayhut1");
            row0.createCell(9).setCellValue("ket1");
            row0.createCell(10).setCellValue("tglent1");
            row0.createCell(11).setCellValue("kodent1");
            row0.createCell(12).setCellValue("nobuk2");
            row0.createCell(13).setCellValue("norek2");
            row0.createCell(14).setCellValue("nilb");
            row0.createCell(15).setCellValue("tglb2");
            row0.createCell(16).setCellValue("bayhut2");
            row0.createCell(17).setCellValue("ket2");
            row0.createCell(18).setCellValue("tglent2");
            row0.createCell(19).setCellValue("kodent2");
            row0.createCell(20).setCellValue("nilc");
            row0.createCell(21).setCellValue("nobuk3");
            row0.createCell(22).setCellValue("norek3");
            row0.createCell(23).setCellValue("tglb3");
            row0.createCell(24).setCellValue("bayhut3");
            row0.createCell(25).setCellValue("ket3");
            row0.createCell(26).setCellValue("tglent3");
            row0.createCell(27).setCellValue("kodent3");
            row0.createCell(28).setCellValue("nobuk4");
            row0.createCell(29).setCellValue("norek4");
            row0.createCell(30).setCellValue("nild");
            row0.createCell(31).setCellValue("tglb4");
            row0.createCell(32).setCellValue("bayhut4");
            row0.createCell(33).setCellValue("ket4");
            row0.createCell(34).setCellValue("tglent4");
            row0.createCell(35).setCellValue("kodent4");
            row0.createCell(36).setCellValue("nobuk5");
            row0.createCell(37).setCellValue("norek5");
            row0.createCell(38).setCellValue("tglb5");
            row0.createCell(39).setCellValue("bayhut5");
            row0.createCell(40).setCellValue("ket5");
            row0.createCell(41).setCellValue("tglent5");
            row0.createCell(42).setCellValue("kodent5");
            row0.createCell(43).setCellValue("feebase");
            row0.createCell(44).setCellValue("tgltran1");
            row0.createCell(45).setCellValue("tglrest1");
            row0.createCell(46).setCellValue("tgltran2");
            row0.createCell(47).setCellValue("tglrest2");
            row0.createCell(48).setCellValue("tgltran3");
            row0.createCell(49).setCellValue("tglrest3");
            row0.createCell(50).setCellValue("tgltran4");
            row0.createCell(51).setCellValue("tglrest4");
            row0.createCell(52).setCellValue("tgltran5");
            row0.createCell(53).setCellValue("tglrest5");
            row0.createCell(54).setCellValue("tglrest");
            row0.createCell(55).setCellValue("tgltran");
            //row0.createCell(56).setCellValue("flag");
            row0.createCell(56).setCellValue("kdpajaka");
            row0.createCell(57).setCellValue("kdpajakb");
            row0.createCell(58).setCellValue("kdpajakc");
            row0.createCell(59).setCellValue("kdpajakd");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("konter").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
//row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nobuk1"));
//row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("norek1"));
            if (h.getFieldValueByFieldNameBD("nila") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nila").doubleValue());
            }
//row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tglb1"));
//row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("bayhut1").doubleValue());
//row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("ket1"));
//row.createCell(10).setCellValue(h.getFieldValueByFieldNameDT("tglent1"));
//row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("kodent1"));
//row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("nobuk2"));
//row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("norek2"));
            if (h.getFieldValueByFieldNameBD("nilb") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("nilb").doubleValue());
            }
//row.createCell(15).setCellValue(h.getFieldValueByFieldNameDT("tglb2"));
//row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("bayhut2").doubleValue());
//row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("ket2"));
//row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("tglent2"));
//row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("kodent2"));
            if (h.getFieldValueByFieldNameBD("nilc") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("nilc").doubleValue());
            }
//row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("nobuk3"));
//row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("norek3"));
//row.createCell(23).setCellValue(h.getFieldValueByFieldNameDT("tglb3"));
//row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("bayhut3").doubleValue());
//row.createCell(25).setCellValue(h.getFieldValueByFieldNameST("ket3"));
//row.createCell(26).setCellValue(h.getFieldValueByFieldNameDT("tglent3"));
//row.createCell(27).setCellValue(h.getFieldValueByFieldNameST("kodent3"));
//row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("nobuk4"));
//row.createCell(29).setCellValue(h.getFieldValueByFieldNameST("norek4"));
            if (h.getFieldValueByFieldNameBD("nild") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("nild").doubleValue());
            }
//row.createCell(31).setCellValue(h.getFieldValueByFieldNameDT("tglb4"));
//row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("bayhut4").doubleValue());
//row.createCell(33).setCellValue(h.getFieldValueByFieldNameST("ket4"));
//row.createCell(34).setCellValue(h.getFieldValueByFieldNameDT("tglent4"));
//row.createCell(35).setCellValue(h.getFieldValueByFieldNameST("kodent4"));
//row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("nobuk5"));
//row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("norek5"));
//row.createCell(38).setCellValue(h.getFieldValueByFieldNameDT("tglb5"));
            if (h.getFieldValueByFieldNameBD("bayhut5") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("bayhut5").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nile") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("nile").doubleValue());
            }
//row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("ket5"));
//row.createCell(41).setCellValue(h.getFieldValueByFieldNameDT("tglent5"));
//row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("kodent5"));
//row.createCell(43).setCellValue(h.getFieldValueByFieldNameDT("tgltran1"));
//row.createCell(44).setCellValue(h.getFieldValueByFieldNameDT("tglrest1"));
//row.createCell(45).setCellValue(h.getFieldValueByFieldNameDT("tgltran2"));
//row.createCell(46).setCellValue(h.getFieldValueByFieldNameDT("tglrest2"));
//row.createCell(47).setCellValue(h.getFieldValueByFieldNameDT("tgltran3"));
//row.createCell(48).setCellValue(h.getFieldValueByFieldNameDT("tglrest3"));
//row.createCell(49).setCellValue(h.getFieldValueByFieldNameDT("tgltran4"));
//row.createCell(50).setCellValue(h.getFieldValueByFieldNameDT("tglrest4"));
//row.createCell(51).setCellValue(h.getFieldValueByFieldNameDT("tgltran5"));
//row.createCell(52).setCellValue(h.getFieldValueByFieldNameDT("tglrest5"));
//row.createCell(53).setCellValue(h.getFieldValueByFieldNameDT("tglrest"));
//row.createCell(54).setCellValue(h.getFieldValueByFieldNameDT("tgltran"));
            //row.createCell(56).setCellValue(h.getFieldValueByFieldNameST("flag"));
            if (h.getFieldValueByFieldNameBD("kdpajaka") != null) {
                row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("kdpajaka").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakb") != null) {
                row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("kdpajakb").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakc") != null) {
                row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("kdpajakc").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakd") != null) {
                row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("kdpajakd").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ABA_PAJAK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_id ");

        sqa.addQuery("from aba_produk a  "
                + "left join ins_policy b on b.pol_id = a.pol_id");

        //sqa.addClause(" a.flag is null ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("b.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (Tools.isYes(stABAFlag)) {
            sqa.addClause("a.flag = 'Y'");
        }

        if (Tools.isNo(stABAFlag)) {
            sqa.addClause("a.flag is null");
        }

        final String sql = " select a.* from aba_pajak a where a.pol_id in ( " + sqa.getSQL() + " ) order by a.nopol,a.kodeko";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_ABA_PAJAK() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("pajak");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_id");
            row0.createCell(1).setCellValue("nopol");
            row0.createCell(2).setCellValue("kodeko");
            row0.createCell(3).setCellValue("konter");
            row0.createCell(4).setCellValue("pajak_a");
            row0.createCell(5).setCellValue("pajak_b");
            row0.createCell(6).setCellValue("pajak_c");
            row0.createCell(7).setCellValue("pajak_d");
            row0.createCell(8).setCellValue("b_fee");
            row0.createCell(9).setCellValue("baypaj_a");
            row0.createCell(10).setCellValue("baypaj_b");
            row0.createCell(11).setCellValue("baypaj_c");
            row0.createCell(12).setCellValue("baypaj_d");
            row0.createCell(13).setCellValue("bayb_fee");
            row0.createCell(14).setCellValue("tglbay_a");
            row0.createCell(15).setCellValue("tglbay_b");
            row0.createCell(16).setCellValue("tglbay_c");
            row0.createCell(17).setCellValue("tglbay_d");
            row0.createCell(18).setCellValue("tglb_fee");
            row0.createCell(19).setCellValue("nobuk_a");
            row0.createCell(20).setCellValue("nobuk_b");
            row0.createCell(21).setCellValue("nobuk_c");
            row0.createCell(22).setCellValue("nobuk_d");
            row0.createCell(23).setCellValue("nobukb_fee");
            row0.createCell(24).setCellValue("norek_a");
            row0.createCell(25).setCellValue("norek_b");
            row0.createCell(26).setCellValue("norek_c");
            row0.createCell(27).setCellValue("norek_d");
            row0.createCell(28).setCellValue("norekb_fee");
            row0.createCell(29).setCellValue("tglent_a");
            row0.createCell(30).setCellValue("tglent_b");
            row0.createCell(31).setCellValue("tglent_c");
            row0.createCell(32).setCellValue("tglent_d");
            row0.createCell(33).setCellValue("tglentb_fee");
            row0.createCell(34).setCellValue("tgltran");
            row0.createCell(35).setCellValue("tglrest");
            row0.createCell(36).setCellValue("npwp1");
            row0.createCell(37).setCellValue("npwp2");
            row0.createCell(38).setCellValue("npwp3");
            row0.createCell(39).setCellValue("npwp4");
            row0.createCell(40).setCellValue("npwp5");
            //row0.createCell(41).setCellValue("flag");
            row0.createCell(41).setCellValue("kdpajaka");
            row0.createCell(42).setCellValue("kdpajakb");
            row0.createCell(43).setCellValue("kdpajakc");
            row0.createCell(44).setCellValue("kdpajakd");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("konter").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("pajak_a").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("pajak_b").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("pajak_c").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("pajak_d").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("b_fee").doubleValue());
            if (h.getFieldValueByFieldNameST("npwp1") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameST("npwp1"));
            }
            if (h.getFieldValueByFieldNameST("npwp2") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("npwp2"));
            }
            if (h.getFieldValueByFieldNameST("npwp3") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("npwp3"));
            }
            if (h.getFieldValueByFieldNameST("npwp4") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("npwp4"));
            }
            if (h.getFieldValueByFieldNameST("npwp5") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("npwp5"));
            }
            //if (h.getFieldValueByFieldNameST("flag")!=null)
            //row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("flag"));
            if (h.getFieldValueByFieldNameBD("kdpajaka") != null) {
                row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("kdpajaka").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakb") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("kdpajakb").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakc") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("kdpajakc").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("kdpajakd") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("kdpajakd").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
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

    public String getStABAFlag() {
        return stABAFlag;
    }

    public void setStABAFlag(String stABAFlag) {
        this.stABAFlag = stABAFlag;
    }

    public String getStRiskCodeName() {
        return stRiskCodeName;
    }

    public void setStRiskCodeName(String stRiskCodeName) {
        this.stRiskCodeName = stRiskCodeName;
    }

    public String getStIndex() {
        return stIndex;
    }

    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return costcenter;
    }

    public InsurancePolicyView getPolicy(String stParentID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
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

    public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public DTOList EXCEL_CLAUSULES() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.ins_clause_id,a.description,a.pol_type_id,a.shortdesc,a.cc_code ");

        sqa.addQuery(" from ins_clausules a ");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = sqa.getSQL() + " order by a.cc_code,a.ins_clause_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAUSULES() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("id");
            row0.createCell(1).setCellValue("description");
            row0.createCell(2).setCellValue("pol_type_id");
            row0.createCell(3).setCellValue("shortdesc");
            row0.createCell(4).setCellValue("cc_code");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("ins_clause_id").doubleValue());
            if (h.getFieldValueByFieldNameST("description") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            }
            if (h.getFieldValueByFieldNameBD("pol_type_id") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("shortdesc") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("shortdesc"));
            }
            if (h.getFieldValueByFieldNameST("cc_code") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_PAKREASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code as cabang,a.policy_date as tgl_polis,a.pol_no as no_polis,b.ref1 as nama,"
                + "b.refd1 as tgl_lhr,b.refd2 as tgl_cair,b.refd3 as tgl_akhir,b.ref2 as umur,b.ref4 as lama,b.insured_amount,"
                + "getpremi(a.pol_type_id = 21,b.refn6,b.premi_total) as premi100,getpremi2(a.pol_type_id = 21,b.refn2) as premi_ko,getpremi2(a.pol_type_id = 21,b.refn9) as comm_ko,a.kreasi_type_desc as jenis_kredit");

        sqa.addQuery(" from ins_policy a"
                + " inner join ins_pol_obj b on b.pol_id  = a.pol_id ");

        sqa.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL') ");
        sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y' ");
        sqa.addClause(" a.pol_type_id in (21,59) ");
        sqa.addClause(" (b.refn2 <> '0' or b.refn6 <> 0 or b.premi_total <> 0) ");

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

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,b.ins_pol_obj_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_PAKREASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("tgl_polis");
            row0.createCell(2).setCellValue("no_polis");
            row0.createCell(3).setCellValue("nama");
            row0.createCell(4).setCellValue("tgl_lhr");
            row0.createCell(5).setCellValue("tgl_cair");
            row0.createCell(6).setCellValue("tgl_akhir");
            row0.createCell(7).setCellValue("umur");
            row0.createCell(8).setCellValue("lama");
            row0.createCell(9).setCellValue("insured_amount");
            row0.createCell(10).setCellValue("premi100");
            row0.createCell(11).setCellValue("premi_ko");
            row0.createCell(12).setCellValue("comm_ko");
            row0.createCell(13).setCellValue("jenis_kredit");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("tgl_polis"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("no_polis"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("nama"));
            if (h.getFieldValueByFieldNameDT("tgl_lhr") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("tgl_lhr"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_cair") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tgl_cair"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_akhir") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgl_akhir"));
            }
            if (h.getFieldValueByFieldNameST("umur") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("umur"));
            }
            if (h.getFieldValueByFieldNameST("lama") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("lama"));
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi100") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi100").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_ko") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_ko").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("comm_ko") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("comm_ko").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("jenis_kredit") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("jenis_kredit"));
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
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

    public void EXCEL_AUTOFAC() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" c.description,';'||a.cc_code as koda,a.pol_id,';'||a.pol_no as pol_no,a.cust_name,substr(b.refd2::text,1,4) as uw_year,"
                + " a.policy_date as tgl_polis,b.refd2 as tglm,b.refd3 as tgla,b.ref1 as nama,a.insured_amount,a.nd_disc1,"
                + " a.nd_disc2,a.nd_comm1,a.nd_comm2,a.nd_comm3,a.nd_comm4,a.nd_brok1,a.nd_brok2,a.nd_feebase1,a.nd_feebase2,"
                + " (select sum(rate) from ins_pol_items x where x.pol_id = a.pol_id and ins_item_id in (18,32)) as rate_comm,"
                + " (select sum(rate) from ins_pol_items x where x.pol_id = a.pol_id and ins_item_id in (23,37)) as rate_disc,"
                + " (select sum(rate) from ins_pol_items x where x.pol_id = a.pol_id and ins_item_id in (19,33)) as rate_brok,"
                + " (select sum(rate) from ins_pol_items x where x.pol_id = a.pol_id and ins_item_id in (67,69)) as rate_feebase,"
                + " b.insured_amount as tsi,b.refn7 as komisi_koas,b.refn6 as premi100,b.refn2 as premi_koas,"
                + " (select sum(x.premi)"
                + " from ins_pol_obj z"
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id and x.ins_cover_id = 78"
                + " where z.ins_pol_obj_id = b.ins_pol_obj_id) as premi_kreasi,"
                + " (select sum(x.premi)"
                + " from ins_pol_obj z"
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id and x.ins_cover_id = 124"
                + " where z.ins_pol_obj_id = b.ins_pol_obj_id) as premi_phk,a.kreasi_type_desc, "
                + " round(sum(checkreas(j.treaty_type='QS',i.premi_amount)),2) as premi_qs, "
                + " round(sum(checkreas(j.treaty_type='QS',i.ricomm_amt)),2) as komisi_qs, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.premi_amount)),2) as premi_fac, "
                + " round(sum(checkreas(j.treaty_type='FAC',i.ricomm_amt)),2) as komisi_fac ");


        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and b.ins_pol_obj_id > 9999 and a.policy_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id " + clauseObj
                + "inner join gl_cost_center c on c.cc_code = a.cc_code "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        sqa.addClause(" a.pol_type_id = 21");

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

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
        }

        String sql = sqa.getSQL() + " GROUP BY c.description,a.cc_code,a.pol_id,b.ins_pol_obj_id,a.pol_no,"
                + " a.cust_name,b.refd2,a.policy_date,b.refd2,b.refd3,b.ref1,a.insured_amount,a.nd_disc1,"
                + " a.nd_disc2,a.nd_comm1,a.nd_comm2,a.nd_comm3,a.nd_comm4,a.nd_brok1,a.nd_brok2,a.nd_feebase1,"
                + " a.nd_feebase2,b.insured_amount,b.refn7,b.refn6,b.refn2 "
                + " order by a.cc_code,a.pol_no,b.ins_pol_obj_id ";

        SQLUtil S = new SQLUtil();

        String nama_file = "pakreasi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
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

    public void EXPORT_AUTOFAC() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("description");
            row0.createCell(1).setCellValue("cc_code");
            row0.createCell(2).setCellValue("pol_id");
            row0.createCell(3).setCellValue("pol_no");
            row0.createCell(4).setCellValue("cust_name");
            row0.createCell(5).setCellValue("uw_year");
            row0.createCell(6).setCellValue("tgl_polis");
            row0.createCell(7).setCellValue("tglm");
            row0.createCell(8).setCellValue("tgla");
            row0.createCell(9).setCellValue("nama");
            row0.createCell(10).setCellValue("insured_amount");
            row0.createCell(11).setCellValue("nd_disc1");
            row0.createCell(12).setCellValue("nd_disc2");
            row0.createCell(13).setCellValue("nd_comm1");
            row0.createCell(14).setCellValue("nd_comm2");
            row0.createCell(15).setCellValue("nd_comm3");
            row0.createCell(16).setCellValue("nd_comm4");
            row0.createCell(17).setCellValue("nd_brok1");
            row0.createCell(18).setCellValue("nd_brok2");
            row0.createCell(19).setCellValue("nd_feebase1");
            row0.createCell(20).setCellValue("nd_feebase2");
            row0.createCell(21).setCellValue("rate_comm");
            row0.createCell(22).setCellValue("rate_disc");
            row0.createCell(23).setCellValue("rate_brok");
            row0.createCell(24).setCellValue("rate_feebase");
            row0.createCell(25).setCellValue("tsi");
            row0.createCell(26).setCellValue("komisi_koas");
            row0.createCell(27).setCellValue("premi100");
            row0.createCell(28).setCellValue("premi_koas");
            row0.createCell(29).setCellValue("premi_kreasi");
            row0.createCell(30).setCellValue("premi_phk");
            row0.createCell(31).setCellValue("kreasi_type_desc");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("uw_year"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgl_polis"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tglm"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tgla"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("nama"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            if (h.getFieldValueByFieldNameBD("nd_disc1") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_disc2") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("nd_disc2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm1") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm2") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm3") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("nd_comm3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm4") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("nd_comm4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_brok1") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("nd_brok1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_brok2") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("nd_brok2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_feebase1") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("nd_feebase1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_feebase2") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("nd_feebase2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("_") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("rate_comm").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_disc") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("rate_disc").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_brok") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("rate_brok").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("rate_feebase") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("rate_feebase").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tsi") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi_koas") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("komisi_koas").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi100") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("premi100").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_koas") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("premi_koas").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_kreasi") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("premi_kreasi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_phk") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("premi_phk").doubleValue());
            }
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameST("kreasi_type_desc"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
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

    public String getStRISlip() {
        return stRISlip;
    }

    public void setStRISlip(String stRISlip) {
        this.stRISlip = stRISlip;
    }

    public EntityView getMarketer() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stMarketerID);

        return entity;
    }

    public InsurancePolicyTypeView getPolicyType() {
        final InsurancePolicyTypeView polType = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);

        return polType;
    }

    public InsurancePolicyTypeGroupView getPolicyTypeGroup() {
        final InsurancePolicyTypeGroupView polType = (InsurancePolicyTypeGroupView) DTOPool.getInstance().getDTO(InsurancePolicyTypeGroupView.class, stPolicyTypeGroupID);

        return polType;
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
     * @return the stPolCredit
     */
    public String getStPolCredit() {
        return stPolCredit;
    }

    /**
     * @param stPolCredit the stPolCredit to set
     */
    public void setStPolCredit(String stPolCredit) {
        this.stPolCredit = stPolCredit;
    }

    DTOList admlinkPremi;

    public DTOList getAdmlinkPremi() {
        return admlinkPremi;
    }

    public void setAdmlinkPremi(DTOList admlinkPremi) {
        this.admlinkPremi = admlinkPremi;
    }

    public DTOList EXCEL_ADMLINK_PREMI() throws Exception {

        /*
        final DTOList listPolicy = ListUtil.getDTOListFromQuery(
                    "select * from ins_policy "+
                    " where status in ('POLICY','RENEWAL') and effective_flag = 'Y' and active_flag='Y'"+
                    " and date_trunc('day',approved_date) >= ? and date_trunc('day',approved_date) <= ?"+
                    " order by pol_id limit 50",
                    new Object [] {appDateFrom, appDateTo},
                    InsurancePolicyView.class);
        */

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	* ");

        sqa.addQuery(" from ins_policy a ");
        sqa.addClause(" status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' and active_flag= 'Y' ");
        //sqa.addClause(" status in ('POLICY','RENEWAL','ENDORSE') and pol_no = '045920201124000100' ");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo!= null) {
            sqa.addClause("a.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stPolicyID!= null) {
            sqa.addClause("a.pol_id = ?");
            sqa.addPar(stPolicyID);
        }

        final String sql = sqa.getSQL() + " order by cc_code, pol_id";

        final DTOList listPolicy = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        admlinkPremi = new DTOList();

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView pol = (InsurancePolicyView) listPolicy.get(i);
            
            logger.logDebug("########################  CREATE NOTA CARE POLIS ke "+ i + " "+ pol.getStPolicyNo());

            DTOList objects = pol.getObjects();

            EntityView entity = pol.getEntity();

            //BUAT NOTA PER OBJECT
            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView object = (InsurancePolicyObjectView) objects.get(j);

                //NOTA PIUTANG PREMI
                addPiutangPremi(pol, entity, object, j);

                //NOTA OUTGO
                addOutgo(pol, entity, object, j);

                //NOTA KOASURANSI
                addHutangKoasuransi(pol, entity, object, j);

            }
        }

        //SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkPremi);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkPremi;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {
            
                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkPremi;
    }


    public void addPiutangPremi(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        AdmLinkPremi admPremi = new AdmLinkPremi();
        admPremi.markNew();

        admPremi.setVoucher(pol.generateVoucherNo("CN",null));
        admPremi.setPolId(pol.getStPolicyID());
        admPremi.setDate(pol.getDtPolicyDate());
        admPremi.setBranch(pol.getStCostCenterCode());
        admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
        admPremi.setCode(pol.getStPolicyTypeID());
        admPremi.setAtype("N");

        if(pol.isStatusEndorse()){
            if(pol.isBatalTotalEndorseMode()) admPremi.setAtype("C");
            else admPremi.setAtype("E");
        }

        admPremi.setData("1");
        admPremi.setType("DI");
        admPremi.setIsType("I");
        admPremi.setPolicyNo(pol.getStPolicyNo());
        admPremi.setInception(pol.getDtPeriodStart());
        admPremi.setExpiry(pol.getDtPeriodEnd());
        admPremi.setEffective(pol.getDtApprovedDate());

        admPremi.setRiskStart(pol.getDtPeriodStart());
        admPremi.setRiskEnd(pol.getDtPeriodEnd());

        if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                        || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

            admPremi.setRiskStart(obj.getDtReference2());
            admPremi.setRiskEnd(obj.getDtReference3());
        }

         if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

             admPremi.setRiskStart(obj.getDtReference1());
             admPremi.setRiskEnd(obj.getDtReference2());
         }

        admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
        admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
        admPremi.setCurrency(pol.getStCurrencyCode());
        admPremi.setRate(pol.getDbCurrencyRate());
        admPremi.setPremium(obj.getDbObjectPremiTotalAmount());

        if(counter==0){
            admPremi.setFee(pol.getDbNDPCost());
            admPremi.setDuty(pol.getDbNDSFee());
        }

        BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

        admPremi.setDiscount(BDUtil.negate(discount));
        admPremi.setSegment("N/A");
        admPremi.setRefId(pol.getStEntityID());

        String objString = obj.getStObjectDescriptionWithoutCounter().length()<254?obj.getStObjectDescriptionWithoutCounter():obj.getStObjectDescriptionWithoutCounter().substring(0, 200);

        admPremi.setInsured(entity.getStEntityName() + " QQ "+ objString);
        admPremi.setRemarks("PIUTANG PREMI");
        //admPremi.setMo(pol.getStMarketingOfficerWho());
        admPremi.setMo(pol.getStCreateWho());
        admPremi.setSourceRefId(pol.getStProducerID());

        getAdmlinkPremi().add(admPremi);

    }

    public void addOutgo(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        final DTOList details = pol.getDetails();

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if(BDUtil.isZeroOrNull(item.getDbAmount())) continue;

            if(item.isComission()){

                AdmLinkPremi admPremi = new AdmLinkPremi();
                admPremi.markNew();

                admPremi.setVoucher(pol.generateVoucherNo("DN", null));
                admPremi.setPolId(pol.getStPolicyID());
                admPremi.setDate(pol.getDtPolicyDate());
                admPremi.setBranch(pol.getStCostCenterCode());
                admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                admPremi.setCode(pol.getStPolicyTypeID());
                admPremi.setAtype("N");

                if(pol.isStatusEndorse()){
                    if(pol.isBatalTotalEndorseMode()) admPremi.setAtype("C");
                    else admPremi.setAtype("E");
                }

                admPremi.setData("1");

                if(item.isKomisi()) admPremi.setType("BA");
                if(item.isBrokerFee()) admPremi.setType("BB");
                if(item.isFeeBase()) admPremi.setType("BD");
                if(item.isHandlingFee()) admPremi.setType("BO");

                admPremi.setIsType("I");
                admPremi.setPolicyNo(pol.getStPolicyNo());
                admPremi.setInception(pol.getDtPeriodStart());
                admPremi.setExpiry(pol.getDtPeriodEnd());
                admPremi.setEffective(pol.getDtApprovedDate());

                admPremi.setRiskStart(pol.getDtPeriodStart());
                admPremi.setRiskEnd(pol.getDtPeriodEnd());

                if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                    admPremi.setRiskStart(obj.getDtReference2());
                    admPremi.setRiskEnd(obj.getDtReference3());
                }

                 if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                     admPremi.setRiskStart(obj.getDtReference1());
                     admPremi.setRiskEnd(obj.getDtReference2());
                 }

                admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                admPremi.setCurrency(pol.getStCurrencyCode());
                admPremi.setRate(pol.getDbCurrencyRate());

                final BigDecimal komisiProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), item.getDbAmount(),0));

                admPremi.setCommission(komisiProRate);

                if(admPremi.getType().equalsIgnoreCase("BA") || admPremi.getType().equalsIgnoreCase("BB")){

                    BigDecimal rateVAT = new BigDecimal(-1.1);

                    if(admPremi.getType().equalsIgnoreCase("BB"))
                        rateVAT = new BigDecimal(-2.2);

                    BigDecimal vatProrate = BDUtil.mul(BDUtil.getRateFromPct(rateVAT), komisiProRate,0);

                    BigDecimal ppn = BDUtil.zero;
                    //dapetin ppn nya
                    for (int j = 0; j < details.size(); j++) {
                        InsurancePolicyItemsView itemPpn = (InsurancePolicyItemsView) details.get(j);

                        if(itemPpn.isPPN() || itemPpn.isPPNComission()){
                            ppn = itemPpn.getDbAmount();
                        }

                    }

                    final BigDecimal PpnProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), ppn,0));

                    //admPremi.setVat(vatProrate);
                    admPremi.setVat(PpnProRate);

                    //BigDecimal subsidies = BDUtil.negate(vatProrate);
                    BigDecimal subsidies = BDUtil.negate(PpnProRate);

                    admPremi.setSubsidies(subsidies);

                    //pajak
                    BigDecimal ratePPH = new BigDecimal(-2.5);

                    //RATE BROKER FEE
                    if(admPremi.getType().equalsIgnoreCase("BB"))
                        ratePPH = new BigDecimal(-2);

                    BigDecimal taxProrate = BDUtil.mul(BDUtil.getRateFromPct(ratePPH), komisiProRate,0);

                    BigDecimal pajakProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), item.getDbTaxAmount(),0));

                    //admPremi.setTax(taxProrate);
                    admPremi.setTax(pajakProRate);
                }

                if(admPremi.getType().equalsIgnoreCase("BD")){

                    //BigDecimal vatProrate = BDUtil.mul(BDUtil.getRateFromPct(new BigDecimal(11)), komisiProRate,0);
                    BigDecimal ppn = BDUtil.zero;
                    //dapetin ppn nya
                    for (int j = 0; j < details.size(); j++) {
                        InsurancePolicyItemsView itemPpn = (InsurancePolicyItemsView) details.get(j);

                        if(itemPpn.isPPNFeeBase()){
                            ppn = itemPpn.getDbAmount();
                        }

                    }

                    BigDecimal PpnProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), ppn ,0));

                    //admPremi.setVat(vatProrate);
                    admPremi.setVat(PpnProRate);
                    admPremi.setSubsidies(null);
                    admPremi.setTax(null);

                }

                admPremi.setSegment("N/A");

                if(item.isKomisi() || item.isBrokerFee() || item.isHandlingFee()) admPremi.setRefId(item.getStEntityID());
                else admPremi.setRefId(pol.getStEntityID());

                String objString =obj.getStObjectDescriptionWithoutCounter().length()<254?obj.getStObjectDescriptionWithoutCounter():obj.getStObjectDescriptionWithoutCounter().substring(0, 200);

                admPremi.setInsured(entity.getStEntityName() + " QQ "+ objString);

                if(item.isKomisi()) admPremi.setRemarks("KOMISI AGEN");
                if(item.isFeeBase()) admPremi.setRemarks("FEE BASE");
                if(item.isBrokerFee()) admPremi.setRemarks("BROKER FEE");

                //admPremi.setMo(pol.getStMarketingOfficerWho());
                admPremi.setMo(pol.getStCreateWho());

                if(item.isKomisi() || item.isBrokerFee()) admPremi.setSourceRefId(item.getStEntityID());
                else admPremi.setSourceRefId(pol.getStProducerID());


                getAdmlinkPremi().add(admPremi);
            }

        }



    }

    public void addHutangKoasuransi(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        final DTOList coins = pol.getCoins();

        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

            if(co.isAskrida()) continue;

            AdmLinkPremi admPremi = new AdmLinkPremi();
            admPremi.markNew();

            admPremi.setVoucher(pol.generateVoucherNo("DN", null));
            admPremi.setPolId(pol.getStPolicyID());
            admPremi.setDate(pol.getDtPolicyDate());
            admPremi.setBranch(pol.getStCostCenterCode());
            admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
            admPremi.setCode(pol.getStPolicyTypeID());
            admPremi.setAtype("N");

            if(pol.isStatusEndorse()){
                if(pol.isBatalTotalEndorseMode()) admPremi.setAtype("C");
                else admPremi.setAtype("E");
            }

            admPremi.setData("1");
            admPremi.setType("CF");
            admPremi.setIsType("I");
            admPremi.setPolicyNo(pol.getStPolicyNo());
            admPremi.setInception(pol.getDtPeriodStart());
            admPremi.setExpiry(pol.getDtPeriodEnd());
            admPremi.setEffective(pol.getDtApprovedDate());

            admPremi.setRiskStart(pol.getDtPeriodStart());
            admPremi.setRiskEnd(pol.getDtPeriodEnd());

            if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                            || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                admPremi.setRiskStart(obj.getDtReference2());
                admPremi.setRiskEnd(obj.getDtReference3());
            }

             if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                 admPremi.setRiskStart(obj.getDtReference1());
                 admPremi.setRiskEnd(obj.getDtReference2());
             }

            admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
            admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
            admPremi.setCurrency(pol.getStCurrencyCode());
            admPremi.setRate(pol.getDbCurrencyRate());

            BigDecimal premiProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), co.getDbPremiAmount(),0));

            admPremi.setPremium(premiProRate);

            BigDecimal komisiProRate = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), co.getDbCommissionAmount(),0);

            admPremi.setCommission(komisiProRate);

            BigDecimal discountProRate = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), co.getDbDiscountAmount(),0);

            admPremi.setDiscount(BDUtil.negate(discountProRate));

            admPremi.setSegment("N/A");
            admPremi.setRefId(co.getStEntityID());

            String objString =obj.getStObjectDescriptionWithoutCounter().length()<254?obj.getStObjectDescriptionWithoutCounter():obj.getStObjectDescriptionWithoutCounter().substring(0, 200);

            admPremi.setInsured(entity.getStEntityName() + " QQ "+ objString);
            admPremi.setRemarks("PREMI KOASURANSI");
            //admPremi.setMo(pol.getStMarketingOfficerWho());
            admPremi.setMo(pol.getStCreateWho());
            admPremi.setSourceRefId(pol.getStProducerID());

            getAdmlinkPremi().add(admPremi);
        }

    }

    public void EXPORT_ADMLINK_PREMI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("PremiResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkPremi h = (AdmLinkPremi) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("VOUCHER");
            row0.createCell(1).setCellValue("DATE");
            row0.createCell(2).setCellValue("BRANCH");
            row0.createCell(3).setCellValue("CT");
            row0.createCell(4).setCellValue("DUEDATE");
            row0.createCell(5).setCellValue("WPC");
            row0.createCell(6).setCellValue("CODE");
            row0.createCell(7).setCellValue("ATYPE");
            row0.createCell(8).setCellValue("Data");
            row0.createCell(9).setCellValue("TYPE");
            row0.createCell(10).setCellValue("ISType");
            row0.createCell(11).setCellValue("POLICYNO");
            row0.createCell(12).setCellValue("Inception");
            row0.createCell(13).setCellValue("Expiry");
            row0.createCell(14).setCellValue("Effective");
            row0.createCell(15).setCellValue("RCODE");
            row0.createCell(16).setCellValue("Risk_Start");
            row0.createCell(17).setCellValue("Risk_End");
            row0.createCell(18).setCellValue("DOCNO");
            row0.createCell(19).setCellValue("REFNO");
            row0.createCell(20).setCellValue("CURRENCY");
            row0.createCell(21).setCellValue("RATE");
            row0.createCell(22).setCellValue("PREMIUM");
            row0.createCell(23).setCellValue("UJROH");
            row0.createCell(24).setCellValue("COMMISSION");
            row0.createCell(25).setCellValue("FEE");
            row0.createCell(26).setCellValue("DUTY");
            row0.createCell(27).setCellValue("DISCOUNT");
            row0.createCell(28).setCellValue("Claim");
            row0.createCell(29).setCellValue("Adjustment");
            row0.createCell(30).setCellValue("Subsidies");
            row0.createCell(31).setCellValue("Subrogation");
            row0.createCell(32).setCellValue("VAT");
            row0.createCell(33).setCellValue("TAX");
            row0.createCell(34).setCellValue("DEPOSIT");
            row0.createCell(35).setCellValue("OTHERFEE_1");
            row0.createCell(36).setCellValue("OTHERFEE_2");
            row0.createCell(37).setCellValue("OTHERTAX_1");
            row0.createCell(38).setCellValue("OTHERTAX_2");
            row0.createCell(39).setCellValue("PNOMINAL_VAT");
            row0.createCell(40).setCellValue("PNOMINAL_TAX");
            row0.createCell(41).setCellValue("SEGMENT");
            row0.createCell(42).setCellValue("PAYMENT");
            row0.createCell(43).setCellValue("NAME");
            row0.createCell(44).setCellValue("ADDRESS");
            row0.createCell(45).setCellValue("REFID");
            row0.createCell(46).setCellValue("TAXTYPE");
            row0.createCell(47).setCellValue("LOB");
            row0.createCell(48).setCellValue("INSURED");
            row0.createCell(49).setCellValue("TSI");
            row0.createCell(50).setCellValue("COVERAGE");
            row0.createCell(51).setCellValue("REMARKS");
            row0.createCell(52).setCellValue("MO");
            row0.createCell(53).setCellValue("SOURCE");
            row0.createCell(54).setCellValue("SOURCE_ADDRESS");
            row0.createCell(55).setCellValue("SOURCE_REFID");
            row0.createCell(56).setCellValue("ACCOUNTNO");
            row0.createCell(57).setCellValue("ACCOUNTNAME");
            row0.createCell(58).setCellValue("BANKNAME");
            row0.createCell(59).setCellValue("BANK");
            row0.createCell(60).setCellValue("BANKBRANCH");
            row0.createCell(61).setCellValue("INSTALLMENT");
            row0.createCell(62).setCellValue("DUEDATE_1");
            row0.createCell(63).setCellValue("AMOUNTDUE_1");
            row0.createCell(64).setCellValue("DUEDATE_2");
            row0.createCell(65).setCellValue("AMOUNTDUE_2");
            row0.createCell(66).setCellValue("DUEDATE_3");
            row0.createCell(67).setCellValue("AMOUNTDUE_3");
            row0.createCell(68).setCellValue("DUEDATE_4");
            row0.createCell(69).setCellValue("AMOUNTDUE_4");
            row0.createCell(70).setCellValue("DUEDATE_5");
            row0.createCell(71).setCellValue("AMOUNTDUE_5");
            row0.createCell(72).setCellValue("DUEDATE_6");
            row0.createCell(73).setCellValue("AMOUNTDUE_6");
            row0.createCell(74).setCellValue("DUEDATE_7");
            row0.createCell(75).setCellValue("AMOUNTDUE_7");
            row0.createCell(76).setCellValue("DUEDATE_8");
            row0.createCell(77).setCellValue("AMOUNTDUE_8");
            row0.createCell(78).setCellValue("DUEDATE_9");
            row0.createCell(79).setCellValue("AMOUNTDUE_9");
            row0.createCell(80).setCellValue("DUEDATE_10");
            row0.createCell(81).setCellValue("AMOUNTDUE_10");
            row0.createCell(82).setCellValue("DUEDATE_11");
            row0.createCell(83).setCellValue("AMOUNTDUE_11");
            row0.createCell(84).setCellValue("DUEDATE_12");
            row0.createCell(85).setCellValue("AMOUNTDUE_12");
            row0.createCell(86).setCellValue("Payment_CC");
            row0.createCell(87).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)
            row.createCell(0).setCellValue("");

            if(h.getVoucher()!=null)
                row.createCell(0).setCellValue(h.getVoucher());

            row.createCell(1).setCellValue(h.getDate());
            row.getCell(1).setCellStyle(cellStyle);

            row.createCell(2).setCellValue(h.getBranch());
            row.createCell(3).setCellValue(h.getCt());
            row.createCell(4).setCellValue(h.getDueDate());
            row.getCell(4).setCellStyle(cellStyle);
            //row.createCell(5).setCellValue(h.getWpc());
            row.createCell(6).setCellValue(h.getCode());
            row.createCell(7).setCellValue(h.getAtype());
            row.createCell(8).setCellValue(h.getData());
            row.createCell(9).setCellValue(h.getType());
            row.createCell(10).setCellValue(h.getIsType());
            row.createCell(11).setCellValue(h.getPolicyNo());
            row.createCell(12).setCellValue(h.getInception());
            row.getCell(12).setCellStyle(cellStyle);
            row.createCell(13).setCellValue(h.getExpiry());
            row.getCell(13).setCellStyle(cellStyle);
            row.createCell(14).setCellValue(h.getEffective());
            row.getCell(14).setCellStyle(cellStyle);
            row.createCell(15).setCellValue(h.getRcode());
            row.createCell(16).setCellValue(h.getRiskStart());
            row.getCell(16).setCellStyle(cellStyle);
            row.createCell(17).setCellValue(h.getRiskEnd());
            row.getCell(17).setCellStyle(cellStyle);
            row.createCell(18).setCellValue(h.getDocNo());
            row.createCell(19).setCellValue(h.getRefNo());
            row.createCell(20).setCellValue(h.getCurrency());
            row.createCell(21).setCellValue(h.getRate().doubleValue());

            if(h.getPremium()!=null)
                row.createCell(22).setCellValue(h.getPremium().doubleValue());

            row.createCell(23).setCellValue(""); //UJROH

            if(h.getCommission()!=null)
                row.createCell(24).setCellValue(h.getCommission().doubleValue());

            if(h.getFee()!=null)
                row.createCell(25).setCellValue(h.getFee().doubleValue());

            if(h.getDuty()!=null)
                row.createCell(26).setCellValue(h.getDuty().doubleValue());

            if(h.getDiscount()!=null)
                row.createCell(27).setCellValue(h.getDiscount().doubleValue());

            row.createCell(28).setCellValue("");
            row.createCell(29).setCellValue("");

            if(h.getSubsidies()!=null)
                row.createCell(30).setCellValue(h.getSubsidies().doubleValue());

            row.createCell(31).setCellValue("");

            if(h.getVat()!=null)
                row.createCell(32).setCellValue(h.getVat().doubleValue());

            if(h.getTax()!=null)
                row.createCell(33).setCellValue(h.getTax().doubleValue());

            row.createCell(34).setCellValue("");
            row.createCell(35).setCellValue("");
            row.createCell(36).setCellValue("");
            row.createCell(37).setCellValue("");
            row.createCell(38).setCellValue("");
            row.createCell(39).setCellValue("");
            //row.createCell(40).setCellValue("PNOMINAL_TAX");
            row.createCell(41).setCellValue(h.getSegment());
            //row.createCell(42).setCellValue(h.getPayment().doubleValue());
            //row.createCell(43).setCellValue("NAME");
            //row.createCell(44).setCellValue("ADDRESS");
            row.createCell(45).setCellValue(h.getRefId());
            //row.createCell(46).setCellValue(h.getTaxType());
            //row.createCell(47).setCellValue(h.getLob());
            row.createCell(48).setCellValue(h.getInsured());
            //row.createCell(49).setCellValue(h.getTsi().doubleValue());
            //row.createCell(50).setCellValue("COVERAGE");
            row.createCell(51).setCellValue(h.getRemarks());
            row.createCell(52).setCellValue(h.getMo());
            //row.createCell(53).setCellValue("SOURCE");
            //row.createCell(54).setCellValue("SOURCE_ADDRESS");
            row.createCell(55).setCellValue(h.getSourceRefId());
            //row.createCell(56).setCellValue("ACCOUNTNO");
            //row.createCell(57).setCellValue("ACCOUNTNAME");
            //row.createCell(58).setCellValue("BANKNAME");
            //row.createCell(59).setCellValue("BANK");
            //row.createCell(60).setCellValue("BANKBRANCH");
            //row.createCell(61).setCellValue("INSTALLMENT");
            /*
            row.createCell(62).setCellValue("DUEDATE_1");
            row.createCell(63).setCellValue("AMOUNTDUE_1");
            row.createCell(64).setCellValue("DUEDATE_2");
            row.createCell(65).setCellValue("AMOUNTDUE_2");
            row.createCell(66).setCellValue("DUEDATE_3");
            row.createCell(67).setCellValue("AMOUNTDUE_3");
            row.createCell(68).setCellValue("DUEDATE_4");
            row.createCell(69).setCellValue("AMOUNTDUE_4");
            row.createCell(70).setCellValue("DUEDATE_5");
            row.createCell(71).setCellValue("AMOUNTDUE_5");
            row.createCell(72).setCellValue("DUEDATE_6");
            row.createCell(73).setCellValue("AMOUNTDUE_6");
            row.createCell(74).setCellValue("DUEDATE_7");
            row.createCell(75).setCellValue("AMOUNTDUE_7");
            row.createCell(76).setCellValue("DUEDATE_8");
            row.createCell(77).setCellValue("AMOUNTDUE_8");
            row.createCell(78).setCellValue("DUEDATE_9");
            row.createCell(79).setCellValue("AMOUNTDUE_9");
            row.createCell(80).setCellValue("DUEDATE_10");
            row.createCell(81).setCellValue("AMOUNTDUE_10");
            row.createCell(82).setCellValue("DUEDATE_11");
            row.createCell(83).setCellValue("AMOUNTDUE_11");
            row.createCell(84).setCellValue("DUEDATE_12");
            row.createCell(85).setCellValue("AMOUNTDUE_12");
            row.createCell(86).setCellValue("Payment_CC");
            row.createCell(87).setCellValue("Pdate");
            */

            
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }
    
    DTOList admlinkClaim;

    public DTOList getAdmlinkClaim() {
        return admlinkClaim;
    }

    public void setAdmlinkClaim(DTOList admlinkClaim) {
        this.admlinkClaim = admlinkClaim;
    }

    public DTOList EXCEL_ADMLINK_CLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	* ");

        sqa.addQuery(" from ins_policy a ");
        sqa.addClause(" status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y' and active_flag='Y' ");

        //sqa.addClause("a.pol_id in (2952963,12428171)");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo!= null) {
            sqa.addClause("a.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stPolicyID!= null) {
            sqa.addClause("a.pol_id = ?");
            sqa.addPar(stPolicyID);
        }

        final String sql = sqa.getSQL() + " order by cc_code, pol_id ";

        final DTOList listClaim = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        admlinkClaim = new DTOList();

        for (int i = 0; i < listClaim.size(); i++) {
            InsurancePolicyView pol = (InsurancePolicyView) listClaim.get(i);

            logger.logDebug("########################  CREATE NOTA KLAIM CARE POLIS ke "+ i + " "+ pol.getStDLANo());

            DTOList objects = pol.getObjects();

            EntityView entity = pol.getEntity();

            //BUAT NOTA PER OBJECT
            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView object = (InsurancePolicyObjectView) objects.get(j);

                //NOTA HUTANG KLAIM DIRECT
                addHutangKlaim(pol, entity, object, j);

                //NOTA OTHER PARTIES / ITEM KLAIM
                addOtherParties(pol, entity, object, j);

                //NOTA KLAIM KOASURANSI
                addKlaimKoasuransi(pol, entity, object, j);

            }

        }

        //SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkClaim);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkClaim;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {

                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkClaim;
    }

     public void addHutangKlaim(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

         //KLAIM BRUTO / JASA BENGKEL / bunga

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        BigDecimal deductible = BDUtil.zero;
        BigDecimal claimBruto = BDUtil.zero;
        BigDecimal tax = BDUtil.zero;

        final DTOList claimItems = pol.getClaimItems();

        for (int i = 0; i < claimItems.size(); i++) {
             InsurancePolicyItemsView item = (InsurancePolicyItemsView) claimItems.get(i);

             if(item.getInsuranceItem().isDeductible()){
                 deductible = item.getDbAmount();
             }

             if(item.getInsuranceItem().isClaimGross()){
                 claimBruto = item.getDbAmount();
             }

             if(item.getInsuranceItem().isJasaBengkel()){
                 claimBruto = item.getDbAmount();
             }

             if(item.getDbTaxAmount()!=null)
                 tax = item.getDbTaxAmount();

             if(item.getInsuranceItem().isBunga()){
                 claimBruto = BDUtil.add(claimBruto, item.getDbAmount());
             }

        }

        if(BDUtil.isZeroOrNull(claimBruto)) return;

        AdmLinkClaim admClaim = new AdmLinkClaim();
        admClaim.markNew();

        admClaim.setVoucher(pol.generateVoucherNo("DN", pol.getDtDLADate()));
        admClaim.setPolId(pol.getStPolicyID());
        admClaim.setBranch(pol.getStCostCenterCode());
        admClaim.setClaimNo(pol.getStDLANo());
        admClaim.setRefNo(pol.getStPLANo());
        admClaim.setRefId(pol.getStEntityID());
        admClaim.setDate(pol.getDtDLADate());
        admClaim.setDueDate(DateUtil.addDays(pol.getDtDLADate(), 30));

        String insured = entity.getStEntityName();

        if(obj.getStReference1()!=null)
            insured = insured + " QQ "+ obj.getStReference1();

        admClaim.setInsured(insured);
        admClaim.setCode(pol.getStPolicyTypeID());
        admClaim.setType("DI");
        admClaim.setIsType("I");
        admClaim.setPolicyNo(pol.getStPolicyNoInduk());

        admClaim.setStartDate(pol.getDtPeriodStart());
        admClaim.setExpiryDate(pol.getDtPeriodEnd());

        if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                        || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

            admClaim.setStartDate(obj.getDtReference2());
            admClaim.setExpiryDate(obj.getDtReference3());
        }

         if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

             admClaim.setStartDate(obj.getDtReference1());
             admClaim.setExpiryDate(obj.getDtReference2());
         }

        admClaim.setDateOfLoss(pol.getDtClaimDate());
        admClaim.setSettledDate(pol.getDtApprovedDate());
        admClaim.setRemarks("CLAIM DIRECT");
        admClaim.setCurrency(pol.getStClaimCurrency());
        admClaim.setRate(pol.getDbCurrencyRateClaim());

        //ANGKA_ANGKA KLAIM

        //BigDecimal claimAmount = BDUtil.negate(pol.getDbClaimAmountApproved());
        BigDecimal claimAmount = BDUtil.negate(claimBruto);

        admClaim.setClaim(claimAmount);

        if(!BDUtil.isZeroOrNull(deductible))
            admClaim.setDeductible(deductible);

        if(!BDUtil.isZeroOrNull(tax))
            admClaim.setTax(tax);


        admClaim.setSegment("N/A");
        //admClaim.setMo(pol.getStMarketingOfficerWho());
        admClaim.setMo(pol.getStCreateWho());
        admClaim.setSourceRefId(pol.getStProducerID());

        getAdmlinkClaim().add(admClaim);

    }

     public void addOtherParties(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        final DTOList claimItems = pol.getClaimItems();

        int ctr = 1;

        for (int i = 0; i < claimItems.size(); i++) {
             InsurancePolicyItemsView item = (InsurancePolicyItemsView) claimItems.get(i);

             InsuranceItemsView items = item.getInsuranceItem();

             if(items.isSalvage() || items.isSubrogasi() || items.isAdjusterFee() ||
                     items.isFeeRecovery() || items.isInsentifSubrogasi() || items.isBiayaSparepart() || items.isTJH() || items.isSurveyFee()){

                    AdmLinkClaim admClaim = new AdmLinkClaim();
                    admClaim.markNew();

                    admClaim.setVoucher(pol.generateVoucherNo("DN", pol.getDtDLADate()));
                    admClaim.setPolId(pol.getStPolicyID());
                    admClaim.setBranch(pol.getStCostCenterCode());
                    admClaim.setClaimNo(pol.getStDLANo());
                    admClaim.setRefNo(pol.getStPLANo()+"-"+ ctr);
                    admClaim.setRefId(pol.getStEntityID());
                    admClaim.setDate(pol.getDtDLADate());
                    admClaim.setDueDate(DateUtil.addDays(pol.getDtDLADate(), 30));

                    String insured = entity.getStEntityName();

                    if(obj.getStReference1()!=null)
                        insured = insured + " QQ "+ obj.getStReference1();

                    admClaim.setInsured(insured);
                    admClaim.setCode(pol.getStPolicyTypeID());
                    admClaim.setType("OP");
                    admClaim.setIsType("I");
                    admClaim.setPolicyNo(pol.getStPolicyNoInduk());

                    admClaim.setStartDate(pol.getDtPeriodStart());
                    admClaim.setExpiryDate(pol.getDtPeriodEnd());

                    if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                    || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                        admClaim.setStartDate(obj.getDtReference2());
                        admClaim.setExpiryDate(obj.getDtReference3());
                    }

                     if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                         admClaim.setStartDate(obj.getDtReference1());
                         admClaim.setExpiryDate(obj.getDtReference2());
                     }

                    admClaim.setDateOfLoss(pol.getDtClaimDate());
                    admClaim.setSettledDate(pol.getDtApprovedDate());
                    admClaim.setRemarks("CLAIM DIRECT");
                    admClaim.setCurrency(pol.getStClaimCurrency());
                    admClaim.setRate(pol.getDbCurrencyRateClaim());

                    //ANGKA_ANGKA KLAIM
                    if(item.getInsuranceItem().isSalvage()){
                        admClaim.setSalvage(item.getDbAmount());
                        admClaim.setRemarks("CLAIM SALVAGE");
                    }

                    if(item.getInsuranceItem().isSubrogasi()){
                        admClaim.setSubrogation(item.getDbAmount());
                        admClaim.setRemarks("CLAIM SUBROGATION");
                    }

                    if(item.getInsuranceItem().isAdjusterFee()){
                        admClaim.setClaim(item.getDbAmount());
                        admClaim.setRemarks("CLAIM ADJUSTER FEE");
                    }

                    if(item.getInsuranceItem().isFeeRecovery()){
                        admClaim.setSubrogation(BDUtil.negate(item.getDbAmount()));
                        admClaim.setRemarks("FEE SUBROGATION");
                    }

                    if(item.getInsuranceItem().isInsentifSubrogasi()){
                        admClaim.setSubrogation(BDUtil.negate(item.getDbAmount()));
                        admClaim.setRemarks("INSENTIF SUBROGATION");
                    }

                    if(items.isBiayaSparepart()){
                        admClaim.setCost(BDUtil.negate(item.getDbAmount()));
                        admClaim.setRemarks("SPAREPART");
                    }

                    if(items.isTJH()){
                        admClaim.setCost(BDUtil.negate(item.getDbAmount()));
                        admClaim.setRemarks("TJH");
                    }
                    
                    if(items.isSurveyFee()){
                        admClaim.setCost(BDUtil.negate(item.getDbAmount()));
                        admClaim.setRemarks("BIAYA SURVEY");
                    }



                    admClaim.setSegment("N/A");
                    //admClaim.setMo(pol.getStMarketingOfficerWho());
                    admClaim.setMo(pol.getStCreateWho());
                    admClaim.setSourceRefId(pol.getStProducerID());

                    getAdmlinkClaim().add(admClaim);

                    ctr++;

             }

        }



    }

     public void addKlaimKoasuransi(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        final DTOList coins = pol.getCoins();

         for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

                if(co.isAskrida()) continue;

                if(BDUtil.isZeroOrNull(co.getDbClaimAmount())) continue;

                AdmLinkClaim admClaim = new AdmLinkClaim();
                admClaim.markNew();

                admClaim.setPolId(pol.getStPolicyID());
                admClaim.setBranch(pol.getStCostCenterCode());
                admClaim.setClaimNo(pol.getStDLANo());
                admClaim.setRefNo(pol.getStPLANo());
                admClaim.setRefId(co.getStEntityID());
                admClaim.setDate(pol.getDtDLADate());
                admClaim.setDueDate(DateUtil.addDays(pol.getDtDLADate(), 30));

                String insured = entity.getStEntityName();

                if(obj.getStReference1()!=null)
                    insured = insured + " QQ "+ obj.getStReference1();

                admClaim.setInsured(insured);
                admClaim.setCode(pol.getStPolicyTypeID());
                admClaim.setType("CF");
                admClaim.setIsType("I");
                admClaim.setPolicyNo(pol.getStPolicyNoInduk());

                admClaim.setStartDate(pol.getDtPeriodStart());
                admClaim.setExpiryDate(pol.getDtPeriodEnd());

                if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                    admClaim.setStartDate(obj.getDtReference2());
                    admClaim.setExpiryDate(obj.getDtReference3());
                }

                 if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                     admClaim.setStartDate(obj.getDtReference1());
                     admClaim.setExpiryDate(obj.getDtReference2());
                 }

                admClaim.setDateOfLoss(pol.getDtClaimDate());
                admClaim.setSettledDate(pol.getDtApprovedDate());
                admClaim.setRemarks("CLAIM KOASURANSI");
                admClaim.setCurrency(pol.getStClaimCurrency());
                admClaim.setRate(pol.getDbCurrencyRateClaim());

                //ANGKA_ANGKA KLAIM
                BigDecimal claimAmountProRate = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), co.getDbClaimAmount(),0);

                admClaim.setClaim(claimAmountProRate);

                String nota = BDUtil.isNegative(claimAmountProRate)?"CN":"DN";

                admClaim.setVoucher(pol.generateVoucherNo(nota, pol.getDtDLADate()));


                admClaim.setSegment("N/A");
                //admClaim.setMo(pol.getStMarketingOfficerWho());
                admClaim.setMo(pol.getStCreateWho());
                admClaim.setSourceRefId(pol.getStProducerID());

                getAdmlinkClaim().add(admClaim);

         }



    }

     public void EXPORT_ADMLINK_CLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("ClaimResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkClaim h = (AdmLinkClaim) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("BRANCH");
            row0.createCell(1).setCellValue("CT");
            row0.createCell(2).setCellValue("CLAIMNO");
            row0.createCell(3).setCellValue("REFNO");
            row0.createCell(4).setCellValue("VOUCHER");
            row0.createCell(5).setCellValue("NAME");
            row0.createCell(6).setCellValue("ADDRESS");
            row0.createCell(7).setCellValue("REFID");
            row0.createCell(8).setCellValue("TAXTYPE");
            row0.createCell(9).setCellValue("LOB");
            row0.createCell(10).setCellValue("DATE");
            row0.createCell(11).setCellValue("DUEDATE");
            row0.createCell(12).setCellValue("INSURED");
            row0.createCell(13).setCellValue("CODE");
            row0.createCell(14).setCellValue("RCODE");
            row0.createCell(15).setCellValue("TYPE");
            row0.createCell(16).setCellValue("ISType");
            row0.createCell(17).setCellValue("POLICYNO");
            row0.createCell(18).setCellValue("Start_Date");
            row0.createCell(19).setCellValue("Expiry_Date");
            row0.createCell(20).setCellValue("TSI");
            row0.createCell(21).setCellValue("Date_OF_Loss");
            row0.createCell(22).setCellValue("Settled_Date");
            row0.createCell(23).setCellValue("REMARKS");
            row0.createCell(24).setCellValue("CURRENCY");
            row0.createCell(25).setCellValue("RATE");
            row0.createCell(26).setCellValue("CLAIM");
            row0.createCell(27).setCellValue("DEDUCTIBLE");
            row0.createCell(28).setCellValue("SALVAGE");
            row0.createCell(29).setCellValue("COST");
            row0.createCell(30).setCellValue("TAX");
            row0.createCell(31).setCellValue("SUBROGATION");
            row0.createCell(32).setCellValue("PNOMINAL_VAT");
            row0.createCell(33).setCellValue("PNOMINAL_TAX");
            row0.createCell(34).setCellValue("SEGMENT");
            row0.createCell(35).setCellValue("MO");
            row0.createCell(36).setCellValue("SOURCE");
            row0.createCell(37).setCellValue("SOURCE_ADDRESS");
            row0.createCell(38).setCellValue("SOURCE_REFID");
            row0.createCell(39).setCellValue("ACCOUNTNO");
            row0.createCell(40).setCellValue("ACCOUNTNAME");
            row0.createCell(41).setCellValue("BANKNAME");
            row0.createCell(42).setCellValue("BANK");
            row0.createCell(43).setCellValue("BANKBRANCH");
            row0.createCell(44).setCellValue("INSTALLMENT");
            row0.createCell(45).setCellValue("DUEDATE_1");
            row0.createCell(46).setCellValue("AMOUNTDUE_1");
            row0.createCell(47).setCellValue("DUEDATE_2");
            row0.createCell(48).setCellValue("AMOUNTDUE_2");
            row0.createCell(49).setCellValue("DUEDATE_3");
            row0.createCell(50).setCellValue("AMOUNTDUE_3");
            row0.createCell(51).setCellValue("DUEDATE_4");
            row0.createCell(52).setCellValue("AMOUNTDUE_4");
            row0.createCell(53).setCellValue("DUEDATE_5");
            row0.createCell(54).setCellValue("AMOUNTDUE_5");
            row0.createCell(55).setCellValue("DUEDATE_6");
            row0.createCell(56).setCellValue("AMOUNTDUE_6");
            row0.createCell(57).setCellValue("DUEDATE_7");
            row0.createCell(58).setCellValue("AMOUNTDUE_7");
            row0.createCell(59).setCellValue("DUEDATE_8");
            row0.createCell(60).setCellValue("AMOUNTDUE_8");
            row0.createCell(61).setCellValue("DUEDATE_9");
            row0.createCell(62).setCellValue("AMOUNTDUE_9");
            row0.createCell(63).setCellValue("DUEDATE_10");
            row0.createCell(64).setCellValue("AMOUNTDUE_10");
            row0.createCell(65).setCellValue("DUEDATE_11");
            row0.createCell(66).setCellValue("AMOUNTDUE_11");
            row0.createCell(67).setCellValue("DUEDATE_12");
            row0.createCell(68).setCellValue("AMOUNTDUE_12");
            row0.createCell(69).setCellValue("Payment_CC");
            row0.createCell(70).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)

            row.createCell(0).setCellValue(h.getBranch());
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue(h.getClaimNo());
            row.createCell(3).setCellValue(h.getRefNo());
            row.createCell(4).setCellValue(h.getVoucher());
            row.createCell(7).setCellValue(h.getRefId());
            row.createCell(10).setCellValue(h.getDate());
            row.getCell(10).setCellStyle(cellStyle);
            row.createCell(11).setCellValue(h.getDueDate());
            row.getCell(11).setCellStyle(cellStyle);
            row.createCell(12).setCellValue(h.getInsured());
            row.createCell(13).setCellValue(h.getCode());
            row.createCell(15).setCellValue(h.getType());
            row.createCell(16).setCellValue(h.getIsType());
            row.createCell(17).setCellValue(h.getPolicyNo());
            row.createCell(18).setCellValue(h.getStartDate());
            row.getCell(18).setCellStyle(cellStyle);
            row.createCell(19).setCellValue(h.getExpiryDate());
            row.getCell(19).setCellStyle(cellStyle);
            row.createCell(21).setCellValue(h.getDateOfLoss());
            row.getCell(21).setCellStyle(cellStyle);
            row.createCell(22).setCellValue(h.getSettledDate());
            row.getCell(22).setCellStyle(cellStyle);
            row.createCell(23).setCellValue(h.getRemarks());
            row.createCell(24).setCellValue(h.getCurrency());
            row.createCell(25).setCellValue(h.getRate().doubleValue());

            if(h.getClaim()!=null)
                row.createCell(26).setCellValue(h.getClaim().doubleValue());

            if(h.getDeductible()!=null)
                row.createCell(27).setCellValue(h.getDeductible().doubleValue());

            if(h.getSalvage()!=null)
                row.createCell(28).setCellValue(h.getSalvage().doubleValue());

            if(h.getCost()!=null)
                row.createCell(29).setCellValue(h.getCost().doubleValue());

            if(h.getTax()!=null)
                row.createCell(30).setCellValue(h.getTax().doubleValue());

            if(h.getSubrogation()!=null)
                row.createCell(31).setCellValue(h.getSubrogation().doubleValue());

            if(h.getPNominalVat()!=null)
                row.createCell(32).setCellValue(h.getPNominalVat().doubleValue());

            if(h.getPNominalTax()!=null)
                row.createCell(33).setCellValue(h.getPNominalTax().doubleValue());

            row.createCell(34).setCellValue(h.getSegment());
            row.createCell(35).setCellValue(h.getMo());

            row.createCell(38).setCellValue(h.getSourceRefId());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

     public DTOList EXCEL_ADMLINK_PREMI_RI() throws Exception {

         //cari closingan reas dulu
         String sqlClosing = " select date_trunc('month',policy_date_end) as bulan,a.*,b.description as policy_desc  "+
                             "    from ins_gl_closing a left join ins_policy_types b on a.pol_type_id = b.pol_type_id    "+
                             "    where closing_type in ('PREMIUM_RI_OUTWARD','PREMIUM_RI_INWARD','PREMIUM_RI_INWARD_OUTWARD','PREMIUM_RI_OUTWARD_XOL','PREMIUM_RI_INWARD_FAC','PROFIT_COMMISION_INWARD')  "+
                             "    and date_trunc('month',policy_date_end) = ?  "+
                             "    order by closing_id desc ";

         final DTOList listClosingan = ListUtil.getDTOListFromQuery(
                sqlClosing,
                new Object[]{appDateFrom},
                InsuranceClosingView.class);

         admlinkPremi = new DTOList();

         for (int i = 0; i < listClosingan.size(); i++) {
             InsuranceClosingView close = (InsuranceClosingView) listClosingan.get(i);

             //cari invoice by no surat hutang
             String sqlInvoice = "select * "+
                                 " from ar_invoice "+
                                 " where no_surat_hutang = ? and ar_cust_id <> 1 ";

             final DTOList listInvoice = ListUtil.getDTOListFromQuery(
                sqlInvoice,
                new Object[]{close.getStNoSuratHutang()},
                ARInvoiceView.class);

             for (int j = 0; j < listInvoice.size(); j++) {
                 ARInvoiceView invoice = (ARInvoiceView) listInvoice.get(j);

                 //dapetin polis nya
                 InsurancePolicyView polis = invoice.getPolicy();

                 logger.logDebug("########################  CREATE NOTA CARE NO SURAT HUTANG : "+ invoice.getStNoSuratHutang() +" POLIS ke "+ j + " "+ polis.getStPolicyNo());

                 EntityView entity = polis.getEntity();

                 addHutangPremiReinsuranceOutwardByInvoice(polis, entity, j, invoice);

             }

         }

         /*

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	* ");

        sqa.addQuery(" from ins_policy a ");
        sqa.addClause(" status in ('POLICY','RENEWAL') and effective_flag = 'Y' and active_flag= 'Y' ");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo!= null) {
            sqa.addClause("a.pol_no = ?");
            sqa.addPar(stPolicyNo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        final String sql = sqa.getSQL() + " order by pol_id limit 50 ";

        final DTOList listPolicy = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        admlinkPremi = new DTOList();

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView pol = (InsurancePolicyView) listPolicy.get(i);

            DTOList objects = pol.getObjects();

            EntityView entity = pol.getEntity();

            //BUAT NOTA PER OBJECT
            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView object = (InsurancePolicyObjectView) objects.get(j);

                //NOTA HUTANG PREMI REAS OUTWARD
                addHutangPremiReinsuranceOutward(pol, entity, object, j);

            }

        }*/

        SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkPremi);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkPremi;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {

                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkPremi;
    }

    public void addHutangPremiReinsuranceOutward(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        final DTOList treaty = object.getTreaties();

        for (int i = 0; i < treaty.size(); i++) {
            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(i);

            final DTOList details = tre.getDetails();

            for (int j = 0; j < details.size(); j++) {
                InsurancePolicyTreatyDetailView det = (InsurancePolicyTreatyDetailView) details.get(j);

                if(det.getTreatyDetail().isOR()) continue;

                final DTOList member = det.getShares();

                //BUAT NOTA PER MEMBER
                for (int k = 0; k < member.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(k);

                    if(ri.getStMemberEntityID().equalsIgnoreCase("1")) continue;

                    AdmLinkPremi admPremi = new AdmLinkPremi();
                    admPremi.markNew();

                    admPremi.setPolId(pol.getStPolicyID());
                    admPremi.setDate(pol.getDtPolicyDate());
                    admPremi.setBranch(pol.getStCostCenterCode());
                    admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                    admPremi.setCode(pol.getStPolicyTypeID());
                    admPremi.setAtype("N");
                    admPremi.setData("1");
                    
                    final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

                    admPremi.setType(type.getStInsuranceTreatyTypeReference());
                    admPremi.setIsType("I");
                    admPremi.setPolicyNo(pol.getStPolicyNo());
                    admPremi.setInception(pol.getDtPeriodStart());
                    admPremi.setExpiry(pol.getDtPeriodEnd());
                    admPremi.setEffective(pol.getDtApprovedDate());

                    admPremi.setRiskStart(pol.getDtPeriodStart());
                    admPremi.setRiskEnd(pol.getDtPeriodEnd());

                    if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                    || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                        admPremi.setRiskStart(obj.getDtReference2());
                        admPremi.setRiskEnd(obj.getDtReference3());
                    }

                     if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                         admPremi.setRiskStart(obj.getDtReference1());
                         admPremi.setRiskEnd(obj.getDtReference2());
                     }

                    admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                    admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());

                    if(ri.getStRISlipNo()!=null){
                        admPremi.setRefNo(ri.getStRISlipNo());
                    }

                    admPremi.setCurrency(pol.getStCurrencyCode());
                    admPremi.setRate(pol.getDbCurrencyRate());

                    BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
                    BigDecimal komisi = ri.getDbRICommAmount();

                    admPremi.setPremium(premi);
                    admPremi.setCommission(komisi);

                    admPremi.setFee(null);
                    admPremi.setDuty(null);

                    //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

                    admPremi.setDiscount(null);
                    admPremi.setSegment("N/A");
                    admPremi.setRefId(ri.getStMemberEntityID());
                    admPremi.setInsured(entity.getStEntityName() + " QQ "+ object.getStObjectDescriptionWithoutCounter());
                    admPremi.setRemarks("PREMI R/I "+ type.getStInsuranceTreatyTypeName());
                    admPremi.setMo(pol.getStMarketingOfficerWho());
                    admPremi.setSourceRefId(pol.getStProducerID());

                    getAdmlinkPremi().add(admPremi);

                }

            }

        }



    }

    public void addHutangPremiReinsuranceOutwardByInvoice(InsurancePolicyView pol, EntityView entity, int counter, ARInvoiceView invoice) throws Exception{

        AdmLinkPremi admPremi = new AdmLinkPremi();
        admPremi.markNew();

        admPremi.setVoucher(pol.generateVoucherNo("DN", pol.getDtPolicyDate()));
        admPremi.setPolId(pol.getStPolicyID());
        admPremi.setDate(pol.getDtPolicyDate());
        admPremi.setBranch(pol.getStCostCenterCode());
        admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
        admPremi.setCode(pol.getStPolicyTypeID());
        admPremi.setAtype("N");
        admPremi.setData("1");

        String sqlTreatyType = "select * "+
                                 " from ins_treaty_types "+
                                 " where treaty_type_name = ? ";

         final DTOList treatyType = ListUtil.getDTOListFromQuery(
            sqlTreatyType,
            new Object[]{invoice.getStReferenceZ1()},
            InsuranceTreatyTypesView.class);

        final InsuranceTreatyTypesView type = (InsuranceTreatyTypesView) treatyType.get(0);

        //final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

        admPremi.setType(type.getStInsuranceTreatyTypeReference());
        admPremi.setIsType("I");
        admPremi.setPolicyNo(pol.getStPolicyNo());
        admPremi.setInception(pol.getDtPeriodStart());
        admPremi.setExpiry(pol.getDtPeriodEnd());
        admPremi.setEffective(pol.getDtApprovedDate());

        admPremi.setRiskStart(pol.getDtPeriodStart());
        admPremi.setRiskEnd(pol.getDtPeriodEnd());

        admPremi.setRiskStart(invoice.getDtAttrPolicyPeriodStart());
        admPremi.setRiskEnd(invoice.getDtAttrPolicyPeriodEnd());

        if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                        || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

            //admPremi.setRiskStart(obj.getDtReference2());
            //admPremi.setRiskEnd(obj.getDtReference3());
        }

         if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

             //admPremi.setRiskStart(obj.getDtReference1());
             //admPremi.setRiskEnd(obj.getDtReference2());
         }

        admPremi.setDocNo(pol.getStPolicyNo());
        admPremi.setRefNo(invoice.getStNoSuratHutang());

        if(invoice.getStReferenceA1()!=null){
            admPremi.setRefNo(invoice.getStReferenceA1());
        }

        admPremi.setCurrency(pol.getStCurrencyCode());
        admPremi.setRate(pol.getDbCurrencyRate());

        final DTOList details = invoice.getDetails();

        BigDecimal premi = BDUtil.zero;
        BigDecimal komisi = BDUtil.zero;

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("PREMI")){
                premi = BDUtil.negate(det.getDbEnteredAmount());
            }

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("KOMISI")){
                komisi = det.getDbEnteredAmount();
            }

        }

        //BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
        //BigDecimal komisi = ri.getDbRICommAmount();

        admPremi.setPremium(premi);
        admPremi.setCommission(komisi);

        admPremi.setFee(null);
        admPremi.setDuty(null);

        //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

        admPremi.setDiscount(null);
        admPremi.setSegment("N/A");
        admPremi.setRefId(invoice.getStARCustomerID());

        //pol.getObjects();

        admPremi.setInsured(entity.getStEntityName());
        admPremi.setRemarks("PREMI R/I "+ type.getStInsuranceTreatyTypeName());
        admPremi.setMo(pol.getStMarketingOfficerWho());
        admPremi.setSourceRefId(pol.getStProducerID());

        getAdmlinkPremi().add(admPremi);

        /*
        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        final DTOList treaty = object.getTreaties();

        for (int i = 0; i < treaty.size(); i++) {
            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(i);

            final DTOList details = tre.getDetails();

            for (int j = 0; j < details.size(); j++) {
                InsurancePolicyTreatyDetailView det = (InsurancePolicyTreatyDetailView) details.get(j);

                if(det.getTreatyDetail().isOR()) continue;

                final DTOList member = det.getShares();

                //BUAT NOTA PER MEMBER
                for (int k = 0; k < member.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(k);

                    if(ri.getStMemberEntityID().equalsIgnoreCase("1")) continue;

                    AdmLinkPremi admPremi = new AdmLinkPremi();
                    admPremi.markNew();

                    admPremi.setPolId(pol.getStPolicyID());
                    admPremi.setDate(pol.getDtPolicyDate());
                    admPremi.setBranch(pol.getStCostCenterCode());
                    admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                    admPremi.setCode(pol.getStPolicyTypeID());
                    admPremi.setAtype("N");
                    admPremi.setData("1");

                    final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

                    admPremi.setType(type.getStInsuranceTreatyTypeReference());
                    admPremi.setIsType("I");
                    admPremi.setPolicyNo(pol.getStPolicyNo());
                    admPremi.setInception(pol.getDtPeriodStart());
                    admPremi.setExpiry(pol.getDtPeriodEnd());
                    admPremi.setEffective(pol.getDtApprovedDate());

                    admPremi.setRiskStart(pol.getDtPeriodStart());
                    admPremi.setRiskEnd(pol.getDtPeriodEnd());

                    if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                    || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                        admPremi.setRiskStart(obj.getDtReference2());
                        admPremi.setRiskEnd(obj.getDtReference3());
                    }

                     if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                         admPremi.setRiskStart(obj.getDtReference1());
                         admPremi.setRiskEnd(obj.getDtReference2());
                     }

                    admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                    admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());

                    if(ri.getStRISlipNo()!=null){
                        admPremi.setRefNo(ri.getStRISlipNo());
                    }

                    admPremi.setCurrency(pol.getStCurrencyCode());
                    admPremi.setRate(pol.getDbCurrencyRate());

                    BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
                    BigDecimal komisi = ri.getDbRICommAmount();

                    admPremi.setPremium(premi);
                    admPremi.setCommission(komisi);

                    admPremi.setFee(null);
                    admPremi.setDuty(null);

                    //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

                    admPremi.setDiscount(null);
                    admPremi.setSegment("N/A");
                    admPremi.setRefId(ri.getStMemberEntityID());
                    admPremi.setInsured(entity.getStEntityName() + " QQ "+ object.getStObjectDescriptionWithoutCounter());
                    admPremi.setRemarks("PREMI R/I "+ type.getStInsuranceTreatyTypeName());
                    admPremi.setMo(pol.getStMarketingOfficerWho());
                    admPremi.setSourceRefId(pol.getStProducerID());

                    getAdmlinkPremi().add(admPremi);

                }

            }

        }
        */



    }

    public void addOutgox(InsurancePolicyView pol, EntityView entity, InsurancePolicyObjectView object, int counter) throws Exception{

        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        final DTOList details = pol.getDetails();

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if(BDUtil.isZeroOrNull(item.getDbAmount())) continue;

            if(item.isComission()){

                AdmLinkPremi admPremi = new AdmLinkPremi();
                admPremi.markNew();

                admPremi.setPolId(pol.getStPolicyID());
                admPremi.setDate(pol.getDtPolicyDate());
                admPremi.setBranch(pol.getStCostCenterCode());
                admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                admPremi.setCode(pol.getStPolicyTypeID());
                admPremi.setAtype("N");
                admPremi.setData("1");

                if(item.isKomisi()) admPremi.setType("BA");
                if(item.isBrokerFee()) admPremi.setType("BB");
                if(item.isFeeBase()) admPremi.setType("BD");

                admPremi.setIsType("I");
                admPremi.setPolicyNo(pol.getStPolicyNo());
                admPremi.setInception(pol.getDtPeriodStart());
                admPremi.setExpiry(pol.getDtPeriodEnd());
                admPremi.setEffective(pol.getDtApprovedDate());

                admPremi.setRiskStart(pol.getDtPeriodStart());
                admPremi.setRiskEnd(pol.getDtPeriodEnd());

                if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                    admPremi.setRiskStart(obj.getDtReference2());
                    admPremi.setRiskEnd(obj.getDtReference3());
                }

                 if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                     admPremi.setRiskStart(obj.getDtReference1());
                     admPremi.setRiskEnd(obj.getDtReference2());
                 }

                admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                admPremi.setCurrency(pol.getStCurrencyCode());
                admPremi.setRate(pol.getDbCurrencyRate());

                BigDecimal komisiProRate = BDUtil.negate(BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()), item.getDbAmount(),0));

                admPremi.setCommission(komisiProRate);

                if(admPremi.getType().equalsIgnoreCase("BA") || admPremi.getType().equalsIgnoreCase("BB")){

                    BigDecimal vatProrate = BDUtil.mul(BDUtil.getRateFromPct(new BigDecimal(-1.1)), komisiProRate,0);

                    admPremi.setVat(vatProrate);

                    BigDecimal subsidies = BDUtil.negate(vatProrate);

                    admPremi.setSubsidies(subsidies);

                    BigDecimal taxProrate = BDUtil.mul(BDUtil.getRateFromPct(new BigDecimal(-2.5)), komisiProRate,0);

                    admPremi.setTax(taxProrate);
                }

                if(admPremi.getType().equalsIgnoreCase("BD")){

                    BigDecimal vatProrate = BDUtil.mul(BDUtil.getRateFromPct(new BigDecimal(11)), komisiProRate,0);

                    admPremi.setVat(vatProrate);
                    admPremi.setSubsidies(null);
                    admPremi.setTax(null);

                }


                admPremi.setSegment("N/A");

                if(item.isKomisi() || item.isBrokerFee()) admPremi.setRefId(item.getStEntityID());
                else admPremi.setRefId(pol.getStEntityID());

                admPremi.setInsured(entity.getStEntityName() + " QQ "+ obj.getStReference1());

                if(item.isKomisi()) admPremi.setRemarks("KOMISI AGEN");
                if(item.isFeeBase()) admPremi.setRemarks("FEE BASE");
                if(item.isBrokerFee()) admPremi.setRemarks("BROKER FEE");

                admPremi.setMo(pol.getStMarketingOfficerWho());

                if(item.isKomisi() || item.isBrokerFee()) admPremi.setSourceRefId(item.getStEntityID());
                else admPremi.setSourceRefId(pol.getStProducerID());


                getAdmlinkPremi().add(admPremi);
            }

        }



    }

    public void EXPORT_ADMLINK_PREMI_RI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("PremiResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkPremi h = (AdmLinkPremi) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("VOUCHER");
            row0.createCell(1).setCellValue("DATE");
            row0.createCell(2).setCellValue("BRANCH");
            row0.createCell(3).setCellValue("CT");
            row0.createCell(4).setCellValue("DUEDATE");
            row0.createCell(5).setCellValue("WPC");
            row0.createCell(6).setCellValue("CODE");
            row0.createCell(7).setCellValue("ATYPE");
            row0.createCell(8).setCellValue("Data");
            row0.createCell(9).setCellValue("TYPE");
            row0.createCell(10).setCellValue("ISType");
            row0.createCell(11).setCellValue("POLICYNO");
            row0.createCell(12).setCellValue("Inception");
            row0.createCell(13).setCellValue("Expiry");
            row0.createCell(14).setCellValue("Effective");
            row0.createCell(15).setCellValue("RCODE");
            row0.createCell(16).setCellValue("Risk_Start");
            row0.createCell(17).setCellValue("Risk_End");
            row0.createCell(18).setCellValue("DOCNO");
            row0.createCell(19).setCellValue("REFNO");
            row0.createCell(20).setCellValue("CURRENCY");
            row0.createCell(21).setCellValue("RATE");
            row0.createCell(22).setCellValue("PREMIUM");
            row0.createCell(23).setCellValue("UJROH");
            row0.createCell(24).setCellValue("COMMISSION");
            row0.createCell(25).setCellValue("FEE");
            row0.createCell(26).setCellValue("DUTY");
            row0.createCell(27).setCellValue("DISCOUNT");
            row0.createCell(28).setCellValue("Claim");
            row0.createCell(29).setCellValue("Adjustment");
            row0.createCell(30).setCellValue("Subsidies");
            row0.createCell(31).setCellValue("Subrogation");
            row0.createCell(32).setCellValue("VAT");
            row0.createCell(33).setCellValue("TAX");
            row0.createCell(34).setCellValue("DEPOSIT");
            row0.createCell(35).setCellValue("OTHERFEE_1");
            row0.createCell(36).setCellValue("OTHERFEE_2");
            row0.createCell(37).setCellValue("OTHERTAX_1");
            row0.createCell(38).setCellValue("OTHERTAX_2");
            row0.createCell(39).setCellValue("PNOMINAL_VAT");
            row0.createCell(40).setCellValue("PNOMINAL_TAX");
            row0.createCell(41).setCellValue("SEGMENT");
            row0.createCell(42).setCellValue("PAYMENT");
            row0.createCell(43).setCellValue("NAME");
            row0.createCell(44).setCellValue("ADDRESS");
            row0.createCell(45).setCellValue("REFID");
            row0.createCell(46).setCellValue("TAXTYPE");
            row0.createCell(47).setCellValue("LOB");
            row0.createCell(48).setCellValue("INSURED");
            row0.createCell(49).setCellValue("TSI");
            row0.createCell(50).setCellValue("COVERAGE");
            row0.createCell(51).setCellValue("REMARKS");
            row0.createCell(52).setCellValue("MO");
            row0.createCell(53).setCellValue("SOURCE");
            row0.createCell(54).setCellValue("SOURCE_ADDRESS");
            row0.createCell(55).setCellValue("SOURCE_REFID");
            row0.createCell(56).setCellValue("ACCOUNTNO");
            row0.createCell(57).setCellValue("ACCOUNTNAME");
            row0.createCell(58).setCellValue("BANKNAME");
            row0.createCell(59).setCellValue("BANK");
            row0.createCell(60).setCellValue("BANKBRANCH");
            row0.createCell(61).setCellValue("INSTALLMENT");
            row0.createCell(62).setCellValue("DUEDATE_1");
            row0.createCell(63).setCellValue("AMOUNTDUE_1");
            row0.createCell(64).setCellValue("DUEDATE_2");
            row0.createCell(65).setCellValue("AMOUNTDUE_2");
            row0.createCell(66).setCellValue("DUEDATE_3");
            row0.createCell(67).setCellValue("AMOUNTDUE_3");
            row0.createCell(68).setCellValue("DUEDATE_4");
            row0.createCell(69).setCellValue("AMOUNTDUE_4");
            row0.createCell(70).setCellValue("DUEDATE_5");
            row0.createCell(71).setCellValue("AMOUNTDUE_5");
            row0.createCell(72).setCellValue("DUEDATE_6");
            row0.createCell(73).setCellValue("AMOUNTDUE_6");
            row0.createCell(74).setCellValue("DUEDATE_7");
            row0.createCell(75).setCellValue("AMOUNTDUE_7");
            row0.createCell(76).setCellValue("DUEDATE_8");
            row0.createCell(77).setCellValue("AMOUNTDUE_8");
            row0.createCell(78).setCellValue("DUEDATE_9");
            row0.createCell(79).setCellValue("AMOUNTDUE_9");
            row0.createCell(80).setCellValue("DUEDATE_10");
            row0.createCell(81).setCellValue("AMOUNTDUE_10");
            row0.createCell(82).setCellValue("DUEDATE_11");
            row0.createCell(83).setCellValue("AMOUNTDUE_11");
            row0.createCell(84).setCellValue("DUEDATE_12");
            row0.createCell(85).setCellValue("AMOUNTDUE_12");
            row0.createCell(86).setCellValue("Payment_CC");
            row0.createCell(87).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)
            row.createCell(0).setCellValue("");

            if(h.getVoucher()!=null)
                row.createCell(0).setCellValue(h.getVoucher());

            row.createCell(1).setCellValue(h.getDate());
            row.getCell(1).setCellStyle(cellStyle);

            row.createCell(2).setCellValue(h.getBranch());
            row.createCell(3).setCellValue(h.getCt());
            row.createCell(4).setCellValue(h.getDueDate());
            row.getCell(4).setCellStyle(cellStyle);
            //row.createCell(5).setCellValue(h.getWpc());
            row.createCell(6).setCellValue(h.getCode());
            row.createCell(7).setCellValue(h.getAtype());
            row.createCell(8).setCellValue(h.getData());
            row.createCell(9).setCellValue(h.getType());
            row.createCell(10).setCellValue(h.getIsType());
            row.createCell(11).setCellValue(h.getPolicyNo());
            row.createCell(12).setCellValue(h.getInception());
            row.getCell(12).setCellStyle(cellStyle);
            row.createCell(13).setCellValue(h.getExpiry());
            row.getCell(13).setCellStyle(cellStyle);
            row.createCell(14).setCellValue(h.getEffective());
            row.getCell(14).setCellStyle(cellStyle);
            row.createCell(15).setCellValue(h.getRcode());
            row.createCell(16).setCellValue(h.getRiskStart());
            row.getCell(16).setCellStyle(cellStyle);
            row.createCell(17).setCellValue(h.getRiskEnd());
            row.getCell(17).setCellStyle(cellStyle);
            row.createCell(18).setCellValue(h.getDocNo());
            row.createCell(19).setCellValue(h.getRefNo());
            row.createCell(20).setCellValue(h.getCurrency());
            row.createCell(21).setCellValue(h.getRate().doubleValue());

            if(h.getPremium()!=null)
                row.createCell(22).setCellValue(h.getPremium().doubleValue());

            row.createCell(23).setCellValue(""); //UJROH

            if(h.getCommission()!=null)
                row.createCell(24).setCellValue(h.getCommission().doubleValue());

            if(h.getFee()!=null)
                row.createCell(25).setCellValue(h.getFee().doubleValue());

            if(h.getDuty()!=null)
                row.createCell(26).setCellValue(h.getDuty().doubleValue());

            if(h.getDiscount()!=null)
                row.createCell(27).setCellValue(h.getDiscount().doubleValue());

            row.createCell(28).setCellValue("");
            row.createCell(29).setCellValue("");

            if(h.getSubsidies()!=null)
                row.createCell(30).setCellValue(h.getSubsidies().doubleValue());

            row.createCell(31).setCellValue("");

            if(h.getVat()!=null)
                row.createCell(32).setCellValue(h.getVat().doubleValue());

            if(h.getTax()!=null)
                row.createCell(33).setCellValue(h.getTax().doubleValue());

            row.createCell(34).setCellValue("");
            row.createCell(35).setCellValue("");
            row.createCell(36).setCellValue("");
            row.createCell(37).setCellValue("");
            row.createCell(38).setCellValue("");
            row.createCell(39).setCellValue("");
            //row.createCell(40).setCellValue("PNOMINAL_TAX");
            row.createCell(41).setCellValue(h.getSegment());
            //row.createCell(42).setCellValue(h.getPayment().doubleValue());
            //row.createCell(43).setCellValue("NAME");
            //row.createCell(44).setCellValue("ADDRESS");
            row.createCell(45).setCellValue(h.getRefId());
            //row.createCell(46).setCellValue(h.getTaxType());
            //row.createCell(47).setCellValue(h.getLob());
            row.createCell(48).setCellValue(h.getInsured());
            //row.createCell(49).setCellValue(h.getTsi().doubleValue());
            //row.createCell(50).setCellValue("COVERAGE");
            row.createCell(51).setCellValue(h.getRemarks());
            row.createCell(52).setCellValue(h.getMo());
            //row.createCell(53).setCellValue("SOURCE");
            //row.createCell(54).setCellValue("SOURCE_ADDRESS");
            row.createCell(55).setCellValue(h.getSourceRefId());
            //row.createCell(56).setCellValue("ACCOUNTNO");
            //row.createCell(57).setCellValue("ACCOUNTNAME");
            //row.createCell(58).setCellValue("BANKNAME");
            //row.createCell(59).setCellValue("BANK");
            //row.createCell(60).setCellValue("BANKBRANCH");
            //row.createCell(61).setCellValue("INSTALLMENT");
            /*
            row.createCell(62).setCellValue("DUEDATE_1");
            row.createCell(63).setCellValue("AMOUNTDUE_1");
            row.createCell(64).setCellValue("DUEDATE_2");
            row.createCell(65).setCellValue("AMOUNTDUE_2");
            row.createCell(66).setCellValue("DUEDATE_3");
            row.createCell(67).setCellValue("AMOUNTDUE_3");
            row.createCell(68).setCellValue("DUEDATE_4");
            row.createCell(69).setCellValue("AMOUNTDUE_4");
            row.createCell(70).setCellValue("DUEDATE_5");
            row.createCell(71).setCellValue("AMOUNTDUE_5");
            row.createCell(72).setCellValue("DUEDATE_6");
            row.createCell(73).setCellValue("AMOUNTDUE_6");
            row.createCell(74).setCellValue("DUEDATE_7");
            row.createCell(75).setCellValue("AMOUNTDUE_7");
            row.createCell(76).setCellValue("DUEDATE_8");
            row.createCell(77).setCellValue("AMOUNTDUE_8");
            row.createCell(78).setCellValue("DUEDATE_9");
            row.createCell(79).setCellValue("AMOUNTDUE_9");
            row.createCell(80).setCellValue("DUEDATE_10");
            row.createCell(81).setCellValue("AMOUNTDUE_10");
            row.createCell(82).setCellValue("DUEDATE_11");
            row.createCell(83).setCellValue("AMOUNTDUE_11");
            row.createCell(84).setCellValue("DUEDATE_12");
            row.createCell(85).setCellValue("AMOUNTDUE_12");
            row.createCell(86).setCellValue("Payment_CC");
            row.createCell(87).setCellValue("Pdate");
            */


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ADMLINK_CLAIM_RI() throws Exception {

         //cari closingan reas dulu
         String sqlClosing = " select date_trunc('month',policy_date_end) as bulan,a.*,b.description as policy_desc  "+
                             "    from ins_gl_closing a left join ins_policy_types b on a.pol_type_id = b.pol_type_id    "+
                             "    where closing_type IN ('CLAIM_RI_OUTWARD','CLAIM_RI_INWARD','CLAIM_RI_INWARD_TO_OUTWARD')  "+
                             "    and date_trunc('month',policy_date_end) = ? "+
                             "    order by closing_id ";

         final DTOList listClosingan = ListUtil.getDTOListFromQuery(
                sqlClosing,
                new Object[]{appDateFrom},
                InsuranceClosingView.class);

         admlinkClaim = new DTOList();

         for (int i = 0; i < listClosingan.size(); i++) {
             InsuranceClosingView close = (InsuranceClosingView) listClosingan.get(i);

             //cari invoice by no surat hutang
             String sqlInvoice = "select * "+
                                 " from ar_invoice "+
                                 " where no_surat_hutang = ? and ar_cust_id <> 1 ";

             final DTOList listInvoice = ListUtil.getDTOListFromQuery(
                sqlInvoice,
                new Object[]{close.getStNoSuratHutang()},
                ARInvoiceView.class);

             for (int j = 0; j < listInvoice.size(); j++) {
                 ARInvoiceView invoice = (ARInvoiceView) listInvoice.get(j);

                 if(invoice.getStARTransactionTypeID().equalsIgnoreCase("17") || invoice.getStARTransactionTypeID().equalsIgnoreCase("18")
                         || invoice.getStARTransactionTypeID().equalsIgnoreCase("19") || invoice.getStARTransactionTypeID().equalsIgnoreCase("23")){

                     //dapetin inward nya
                     InsurancePolicyInwardView polis = invoice.getInward();

                     EntityView entity = polis.getEntity();

                     addClaimReinsuranceInwardByInvoice(polis, entity, j, invoice);

                 }else{
                     //dapetin LKP nya
                     InsurancePolicyView polis = invoice.getPolicy();

                     logger.logDebug("########################  CREATE NOTA CARE NO SURAT HUTANG : "+ invoice.getStNoSuratHutang() +" LKP ke "+ j + " "+ polis.getStDLANo());

                    EntityView entity = polis.getEntity();

                    addClaimReinsuranceOutwardByInvoice(polis, entity, j, invoice);


                 }
             }

         }


        SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkClaim);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkClaim;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {

                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkClaim;
    }

    public void addClaimReinsuranceOutwardByInvoice(InsurancePolicyView pol, EntityView entity, int counter, ARInvoiceView invoice) throws Exception{

        AdmLinkClaim admClaim = new AdmLinkClaim();
        admClaim.markNew();

        admClaim.setVoucher(pol.generateVoucherNo("CN", pol.getDtDLADate()));
        admClaim.setPolId(pol.getStPolicyID());
        admClaim.setBranch(pol.getStCostCenterCode());
        admClaim.setClaimNo(pol.getStDLANo());
        admClaim.setRefNo(invoice.getStNoSuratHutang());
        admClaim.setRefId(pol.getStEntityID());
        admClaim.setDate(pol.getDtDLADate());
        admClaim.setDueDate(DateUtil.addDays(pol.getDtDLADate(), 30));

        String insured = entity.getStEntityName();

        final DTOList objects = pol.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            insured = insured + " QQ "+ obj.getStObjectDescriptionWithoutCounter();

        }

        admClaim.setInsured(insured);
        admClaim.setCode(pol.getStPolicyTypeID());

        String sqlTreatyType = "select * "+
                                 " from ins_treaty_types "+
                                 " where treaty_type_name = ? ";

         final DTOList treatyType = ListUtil.getDTOListFromQuery(
            sqlTreatyType,
            new Object[]{invoice.getStReferenceZ1()},
            InsuranceTreatyTypesView.class);

        final InsuranceTreatyTypesView type = (InsuranceTreatyTypesView) treatyType.get(0);

        //final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

        admClaim.setType(type.getStInsuranceTreatyTypeReference());

        admClaim.setIsType("I");
        admClaim.setPolicyNo(pol.getStPolicyNoInduk());

        admClaim.setStartDate(pol.getDtPeriodStart());
        admClaim.setExpiryDate(pol.getDtPeriodEnd());

        if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                        || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

            //admClaim.setStartDate(obj.getDtReference2());
            //admClaim.setExpiryDate(obj.getDtReference3());
        }

         if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

             //admClaim.setStartDate(obj.getDtReference1());
             //admClaim.setExpiryDate(obj.getDtReference2());
         }

        admClaim.setDateOfLoss(pol.getDtClaimDate());
        admClaim.setSettledDate(pol.getDtApprovedDate());
        admClaim.setRemarks("CLAIM R/I "+ type.getStInsuranceTreatyTypeName());

        admClaim.setCurrency(pol.getStClaimCurrency());
        admClaim.setRate(pol.getDbCurrencyRateClaim());

        //ANGKA_ANGKA KLAIM
        final DTOList details = invoice.getDetails();

        BigDecimal klaimAmount = BDUtil.zero;

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("KLAIM")){
                klaimAmount = det.getDbEnteredAmount();
            }

        }

        admClaim.setClaim(klaimAmount);


        admClaim.setSegment("N/A");
        //admClaim.setMo(pol.getStMarketingOfficerWho());
        admClaim.setMo(pol.getStCreateWho());
        admClaim.setSourceRefId(pol.getStProducerID());

        getAdmlinkClaim().add(admClaim);

        // BATAS KLAIM

        /*
        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        final DTOList treaty = object.getTreaties();

        for (int i = 0; i < treaty.size(); i++) {
            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(i);

            final DTOList details = tre.getDetails();

            for (int j = 0; j < details.size(); j++) {
                InsurancePolicyTreatyDetailView det = (InsurancePolicyTreatyDetailView) details.get(j);

                if(det.getTreatyDetail().isOR()) continue;

                final DTOList member = det.getShares();

                //BUAT NOTA PER MEMBER
                for (int k = 0; k < member.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(k);

                    if(ri.getStMemberEntityID().equalsIgnoreCase("1")) continue;

                    AdmLinkPremi admPremi = new AdmLinkPremi();
                    admPremi.markNew();

                    admPremi.setPolId(pol.getStPolicyID());
                    admPremi.setDate(pol.getDtPolicyDate());
                    admPremi.setBranch(pol.getStCostCenterCode());
                    admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                    admPremi.setCode(pol.getStPolicyTypeID());
                    admPremi.setAtype("N");
                    admPremi.setData("1");

                    final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

                    admPremi.setType(type.getStInsuranceTreatyTypeReference());
                    admPremi.setIsType("I");
                    admPremi.setPolicyNo(pol.getStPolicyNo());
                    admPremi.setInception(pol.getDtPeriodStart());
                    admPremi.setExpiry(pol.getDtPeriodEnd());
                    admPremi.setEffective(pol.getDtApprovedDate());

                    admPremi.setRiskStart(pol.getDtPeriodStart());
                    admPremi.setRiskEnd(pol.getDtPeriodEnd());

                    if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                    || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                        admPremi.setRiskStart(obj.getDtReference2());
                        admPremi.setRiskEnd(obj.getDtReference3());
                    }

                     if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                         admPremi.setRiskStart(obj.getDtReference1());
                         admPremi.setRiskEnd(obj.getDtReference2());
                     }

                    admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                    admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());

                    if(ri.getStRISlipNo()!=null){
                        admPremi.setRefNo(ri.getStRISlipNo());
                    }

                    admPremi.setCurrency(pol.getStCurrencyCode());
                    admPremi.setRate(pol.getDbCurrencyRate());

                    BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
                    BigDecimal komisi = ri.getDbRICommAmount();

                    admPremi.setPremium(premi);
                    admPremi.setCommission(komisi);

                    admPremi.setFee(null);
                    admPremi.setDuty(null);

                    //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

                    admPremi.setDiscount(null);
                    admPremi.setSegment("N/A");
                    admPremi.setRefId(ri.getStMemberEntityID());
                    admPremi.setInsured(entity.getStEntityName() + " QQ "+ object.getStObjectDescriptionWithoutCounter());
                    admPremi.setRemarks("PREMI R/I "+ type.getStInsuranceTreatyTypeName());
                    admPremi.setMo(pol.getStMarketingOfficerWho());
                    admPremi.setSourceRefId(pol.getStProducerID());

                    getAdmlinkPremi().add(admPremi);

                }

            }

        }
        */



    }

    public void addClaimReinsuranceInwardByInvoice(InsurancePolicyInwardView pol, EntityView entity, int counter, ARInvoiceView invoice) throws Exception{

        AdmLinkClaim admClaim = new AdmLinkClaim();
        admClaim.markNew();

        admClaim.setVoucher(pol.generateVoucherNo("CN"));
        admClaim.setPolId(pol.getStPolicyID());
        admClaim.setBranch(pol.getStCostCenterCode());
        admClaim.setClaimNo(pol.getStDLANo());
        admClaim.setRefNo(invoice.getStNoSuratHutang());
        admClaim.setRefId(pol.getStEntityID());
        admClaim.setDate(pol.getDtMutationDate());
        admClaim.setDueDate(DateUtil.addDays(pol.getDtMutationDate(), 30));

        String insured = entity.getStEntityName();



        admClaim.setInsured(insured);
        admClaim.setCode(pol.getStAttrPolicyTypeID());

        String sqlTreatyType = "select * "+
                                 " from ins_treaty_types "+
                                 " where ins_treaty_type_id = ? ";

         final DTOList treatyType = ListUtil.getDTOListFromQuery(
            sqlTreatyType,
            new Object[]{pol.getStRefID0()},
            InsuranceTreatyTypesView.class);

        final InsuranceTreatyTypesView type = (InsuranceTreatyTypesView) treatyType.get(0);

        //final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

        admClaim.setType(type.getStInsuranceTreatyTypeReference());

        admClaim.setIsType("I");
        admClaim.setPolicyNo(pol.getStAttrPolicyNo());

        admClaim.setStartDate(pol.getDtAttrPolicyPeriodStart());
        admClaim.setExpiryDate(pol.getDtAttrPolicyPeriodEnd());

        admClaim.setDateOfLoss(pol.getDtReference2());
        admClaim.setSettledDate(pol.getDtMutationDate());
        admClaim.setRemarks("CLAIM R/I INWARD "+ type.getStInsuranceTreatyTypeName());

        admClaim.setCurrency(pol.getStCurrencyCode());
        admClaim.setRate(pol.getDbCurrencyRate());

        //ANGKA_ANGKA KLAIM
        final DTOList details = invoice.getDetails();

        BigDecimal klaimAmount = BDUtil.zero;

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("KLAIM")){
                klaimAmount = det.getDbEnteredAmount();
            }

        }

        admClaim.setClaim(klaimAmount);


        admClaim.setSegment("N/A");
        //admClaim.setMo(pol.getStMarketingOfficerWho());
        admClaim.setMo(pol.getStCreateWho());
        admClaim.setSourceRefId(pol.getStEntityID());

        getAdmlinkClaim().add(admClaim);

        // BATAS KLAIM

        /*
        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object;

        if(BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalAmount())) return;

        final DTOList treaty = object.getTreaties();

        for (int i = 0; i < treaty.size(); i++) {
            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaty.get(i);

            final DTOList details = tre.getDetails();

            for (int j = 0; j < details.size(); j++) {
                InsurancePolicyTreatyDetailView det = (InsurancePolicyTreatyDetailView) details.get(j);

                if(det.getTreatyDetail().isOR()) continue;

                final DTOList member = det.getShares();

                //BUAT NOTA PER MEMBER
                for (int k = 0; k < member.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) member.get(k);

                    if(ri.getStMemberEntityID().equalsIgnoreCase("1")) continue;

                    AdmLinkPremi admPremi = new AdmLinkPremi();
                    admPremi.markNew();

                    admPremi.setPolId(pol.getStPolicyID());
                    admPremi.setDate(pol.getDtPolicyDate());
                    admPremi.setBranch(pol.getStCostCenterCode());
                    admPremi.setDueDate(DateUtil.addDays(pol.getDtPolicyDate(), 30));
                    admPremi.setCode(pol.getStPolicyTypeID());
                    admPremi.setAtype("N");
                    admPremi.setData("1");

                    final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

                    admPremi.setType(type.getStInsuranceTreatyTypeReference());
                    admPremi.setIsType("I");
                    admPremi.setPolicyNo(pol.getStPolicyNo());
                    admPremi.setInception(pol.getDtPeriodStart());
                    admPremi.setExpiry(pol.getDtPeriodEnd());
                    admPremi.setEffective(pol.getDtApprovedDate());

                    admPremi.setRiskStart(pol.getDtPeriodStart());
                    admPremi.setRiskEnd(pol.getDtPeriodEnd());

                    if(pol.getStPolicyTypeID().equalsIgnoreCase("21") || pol.getStPolicyTypeID().equalsIgnoreCase("59")|| pol.getStPolicyTypeID().equalsIgnoreCase("64")
                                    || pol.getStPolicyTypeID().equalsIgnoreCase("4")){

                        admPremi.setRiskStart(obj.getDtReference2());
                        admPremi.setRiskEnd(obj.getDtReference3());
                    }

                     if(pol.getStPolicyTypeID().equalsIgnoreCase("3") || pol.getStPolicyTypeID().equalsIgnoreCase("1") || pol.getStPolicyTypeID().equalsIgnoreCase("81")){

                         admPremi.setRiskStart(obj.getDtReference1());
                         admPremi.setRiskEnd(obj.getDtReference2());
                     }

                    admPremi.setDocNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());
                    admPremi.setRefNo(pol.getStPolicyNo()+ "-" + obj.getStOrderNo());

                    if(ri.getStRISlipNo()!=null){
                        admPremi.setRefNo(ri.getStRISlipNo());
                    }

                    admPremi.setCurrency(pol.getStCurrencyCode());
                    admPremi.setRate(pol.getDbCurrencyRate());

                    BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
                    BigDecimal komisi = ri.getDbRICommAmount();

                    admPremi.setPremium(premi);
                    admPremi.setCommission(komisi);

                    admPremi.setFee(null);
                    admPremi.setDuty(null);

                    //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

                    admPremi.setDiscount(null);
                    admPremi.setSegment("N/A");
                    admPremi.setRefId(ri.getStMemberEntityID());
                    admPremi.setInsured(entity.getStEntityName() + " QQ "+ object.getStObjectDescriptionWithoutCounter());
                    admPremi.setRemarks("PREMI R/I "+ type.getStInsuranceTreatyTypeName());
                    admPremi.setMo(pol.getStMarketingOfficerWho());
                    admPremi.setSourceRefId(pol.getStProducerID());

                    getAdmlinkPremi().add(admPremi);

                }

            }

        }
        */



    }

    public void EXPORT_ADMLINK_CLAIM_RI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("ClaimResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkClaim h = (AdmLinkClaim) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("BRANCH");
            row0.createCell(1).setCellValue("CT");
            row0.createCell(2).setCellValue("CLAIMNO");
            row0.createCell(3).setCellValue("REFNO");
            row0.createCell(4).setCellValue("VOUCHER");
            row0.createCell(5).setCellValue("NAME");
            row0.createCell(6).setCellValue("ADDRESS");
            row0.createCell(7).setCellValue("REFID");
            row0.createCell(8).setCellValue("TAXTYPE");
            row0.createCell(9).setCellValue("LOB");
            row0.createCell(10).setCellValue("DATE");
            row0.createCell(11).setCellValue("DUEDATE");
            row0.createCell(12).setCellValue("INSURED");
            row0.createCell(13).setCellValue("CODE");
            row0.createCell(14).setCellValue("RCODE");
            row0.createCell(15).setCellValue("TYPE");
            row0.createCell(16).setCellValue("ISType");
            row0.createCell(17).setCellValue("POLICYNO");
            row0.createCell(18).setCellValue("Start_Date");
            row0.createCell(19).setCellValue("Expiry_Date");
            row0.createCell(20).setCellValue("TSI");
            row0.createCell(21).setCellValue("Date_OF_Loss");
            row0.createCell(22).setCellValue("Settled_Date");
            row0.createCell(23).setCellValue("REMARKS");
            row0.createCell(24).setCellValue("CURRENCY");
            row0.createCell(25).setCellValue("RATE");
            row0.createCell(26).setCellValue("CLAIM");
            row0.createCell(27).setCellValue("DEDUCTIBLE");
            row0.createCell(28).setCellValue("SALVAGE");
            row0.createCell(29).setCellValue("COST");
            row0.createCell(30).setCellValue("TAX");
            row0.createCell(31).setCellValue("SUBROGATION");
            row0.createCell(32).setCellValue("PNOMINAL_VAT");
            row0.createCell(33).setCellValue("PNOMINAL_TAX");
            row0.createCell(34).setCellValue("SEGMENT");
            row0.createCell(35).setCellValue("MO");
            row0.createCell(36).setCellValue("SOURCE");
            row0.createCell(37).setCellValue("SOURCE_ADDRESS");
            row0.createCell(38).setCellValue("SOURCE_REFID");
            row0.createCell(39).setCellValue("ACCOUNTNO");
            row0.createCell(40).setCellValue("ACCOUNTNAME");
            row0.createCell(41).setCellValue("BANKNAME");
            row0.createCell(42).setCellValue("BANK");
            row0.createCell(43).setCellValue("BANKBRANCH");
            row0.createCell(44).setCellValue("INSTALLMENT");
            row0.createCell(45).setCellValue("DUEDATE_1");
            row0.createCell(46).setCellValue("AMOUNTDUE_1");
            row0.createCell(47).setCellValue("DUEDATE_2");
            row0.createCell(48).setCellValue("AMOUNTDUE_2");
            row0.createCell(49).setCellValue("DUEDATE_3");
            row0.createCell(50).setCellValue("AMOUNTDUE_3");
            row0.createCell(51).setCellValue("DUEDATE_4");
            row0.createCell(52).setCellValue("AMOUNTDUE_4");
            row0.createCell(53).setCellValue("DUEDATE_5");
            row0.createCell(54).setCellValue("AMOUNTDUE_5");
            row0.createCell(55).setCellValue("DUEDATE_6");
            row0.createCell(56).setCellValue("AMOUNTDUE_6");
            row0.createCell(57).setCellValue("DUEDATE_7");
            row0.createCell(58).setCellValue("AMOUNTDUE_7");
            row0.createCell(59).setCellValue("DUEDATE_8");
            row0.createCell(60).setCellValue("AMOUNTDUE_8");
            row0.createCell(61).setCellValue("DUEDATE_9");
            row0.createCell(62).setCellValue("AMOUNTDUE_9");
            row0.createCell(63).setCellValue("DUEDATE_10");
            row0.createCell(64).setCellValue("AMOUNTDUE_10");
            row0.createCell(65).setCellValue("DUEDATE_11");
            row0.createCell(66).setCellValue("AMOUNTDUE_11");
            row0.createCell(67).setCellValue("DUEDATE_12");
            row0.createCell(68).setCellValue("AMOUNTDUE_12");
            row0.createCell(69).setCellValue("Payment_CC");
            row0.createCell(70).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)

            row.createCell(0).setCellValue(h.getBranch());
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue(h.getClaimNo());
            row.createCell(3).setCellValue(h.getRefNo());
            row.createCell(4).setCellValue(h.getVoucher());
            row.createCell(7).setCellValue(h.getRefId());
            row.createCell(10).setCellValue(h.getDate());
            row.getCell(10).setCellStyle(cellStyle);
            row.createCell(11).setCellValue(h.getDueDate());
            row.getCell(11).setCellStyle(cellStyle);
            row.createCell(12).setCellValue(h.getInsured());
            row.createCell(13).setCellValue(h.getCode());
            row.createCell(15).setCellValue(h.getType());
            row.createCell(16).setCellValue(h.getIsType());
            row.createCell(17).setCellValue(h.getPolicyNo());
            row.createCell(18).setCellValue(h.getStartDate());
            row.getCell(18).setCellStyle(cellStyle);
            row.createCell(19).setCellValue(h.getExpiryDate());
            row.getCell(19).setCellStyle(cellStyle);
            row.createCell(21).setCellValue(h.getDateOfLoss());
            row.getCell(21).setCellStyle(cellStyle);
            row.createCell(22).setCellValue(h.getSettledDate());
            row.getCell(22).setCellStyle(cellStyle);
            row.createCell(23).setCellValue(h.getRemarks());
            row.createCell(24).setCellValue(h.getCurrency());
            row.createCell(25).setCellValue(h.getRate().doubleValue());

            if(h.getClaim()!=null)
                row.createCell(26).setCellValue(h.getClaim().doubleValue());

            if(h.getDeductible()!=null)
                row.createCell(27).setCellValue(h.getDeductible().doubleValue());

            if(h.getSalvage()!=null)
                row.createCell(28).setCellValue(h.getSalvage().doubleValue());

            if(h.getCost()!=null)
                row.createCell(29).setCellValue(h.getCost().doubleValue());

            if(h.getTax()!=null)
                row.createCell(30).setCellValue(h.getTax().doubleValue());

            if(h.getSubrogation()!=null)
                row.createCell(31).setCellValue(h.getSubrogation().doubleValue());

            if(h.getPNominalVat()!=null)
                row.createCell(32).setCellValue(h.getPNominalVat().doubleValue());

            if(h.getPNominalTax()!=null)
                row.createCell(33).setCellValue(h.getPNominalTax().doubleValue());

            row.createCell(34).setCellValue(h.getSegment());
            row.createCell(35).setCellValue(h.getMo());

            row.createCell(38).setCellValue(h.getSourceRefId());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ADMLINK_PREMI_INWARD() throws Exception {

         //cari closingan reas dulu
         String sqlInward = " select * "+
                            " from ins_pol_inward "+
                            " where approved_flag = 'Y' and ar_trx_type_id in (1,2,3) "+
                            " and date_trunc('day',mutation_date) >= ? and date_trunc('day',mutation_date) <= ? ";

         final DTOList listInward = ListUtil.getDTOListFromQuery(
                sqlInward,
                new Object[]{appDateFrom, appDateTo},
                InsurancePolicyInwardView.class);

         admlinkPremi = new DTOList();

         for (int i = 0; i < listInward.size(); i++) {
             InsurancePolicyInwardView inw = (InsurancePolicyInwardView) listInward.get(i);


             //logger.logDebug("########################  CREATE NOTA CARE NO SURAT HUTANG : "+ invoice.getStNoSuratHutang() +" POLIS ke "+ j + " "+ polis.getStPolicyNo());

             EntityView entity = inw.getEntity();

             //addHutangPremiReinsuranceOutwardByInvoice(polis, entity, j, invoice);

             //cari invoice by no invoice
             String sqlInvoice = "select * "+
                                 " from ar_invoice "+
                                 " where invoice_no = ? ";

             final DTOList listInvoice = ListUtil.getDTOListFromQuery(
                sqlInvoice,
                new Object[]{inw.getStInvoiceNo()},
                ARInvoiceView.class);

             for (int j = 0; j < listInvoice.size(); j++) {
                 ARInvoiceView invoice = (ARInvoiceView) listInvoice.get(j);

                 logger.logDebug("########################  CREATE NOTA CARE NO INVOICE : "+ inw.getStInvoiceNo() +" POLIS ke "+ j );

                 addPremiInward(inw, entity, j, invoice);

             }

         }

        SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkPremi);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkPremi;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {

                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkPremi;
    }

    public void addPremiInward(InsurancePolicyInwardView pol, EntityView entity, int counter, ARInvoiceView invoice) throws Exception{

        AdmLinkPremi admPremi = new AdmLinkPremi();
        admPremi.markNew();

        admPremi.setVoucher(pol.generateVoucherNo("CN"));
        admPremi.setPolId(pol.getStARInvoiceID());
        admPremi.setDate(pol.getDtMutationDate());

        logger.logDebug("############# trx type id = "+ pol.getStARTransactionTypeID());
        logger.logDebug("############# tgl mutation = "+ pol.getDtMutationDate());

        admPremi.setBranch(pol.getStCostCenterCode());
        admPremi.setDueDate(DateUtil.addDays(pol.getDtMutationDate(), 30));

        admPremi.setAtype("N");
        admPremi.setData("1");

        //final InsuranceTreatyTypesView type = det.getTreatyDetail().getTreatyType();

        String typeTx = "";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("1")) typeTx = "IR";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("2")) typeTx = "IT";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("3")) typeTx = "IX";

        admPremi.setCode(pol.getStAttrPolicyTypeID());

        if(pol.getStARTransactionTypeID().equalsIgnoreCase("2")){
            final DTOList details = pol.getDetailsInwardTreaty();

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

                admPremi.setCode(det.getStAttrPolicyTypeID());
            }
        }

        admPremi.setType(typeTx);
        admPremi.setIsType("I");
        admPremi.setPolicyNo(pol.getStAttrPolicyNo());
        
        admPremi.setEffective(pol.getDtMutationDate());

        admPremi.setInception(pol.getDtAttrPolicyPeriodStart());
        admPremi.setExpiry(pol.getDtAttrPolicyPeriodEnd());
        admPremi.setRiskStart(pol.getDtAttrPolicyPeriodStart());
        admPremi.setRiskEnd(pol.getDtAttrPolicyPeriodEnd());

        if(pol.getStARTransactionTypeID().equalsIgnoreCase("2") || pol.getStARTransactionTypeID().equalsIgnoreCase("3")){

            admPremi.setInception(pol.getDtMutationDate());
            admPremi.setExpiry(pol.getDtMutationDate());
            admPremi.setRiskStart(pol.getDtMutationDate());
            admPremi.setRiskEnd(pol.getDtMutationDate());
        } 


        admPremi.setDocNo(pol.getStInvoiceNo());
        admPremi.setRefNo(pol.getStTransactionNoReference());

        if(invoice.getStReferenceA1()!=null){
            //admPremi.setRefNo(invoice.getStReferenceA1());
        }

        admPremi.setCurrency(pol.getStCurrencyCode());
        admPremi.setRate(pol.getDbCurrencyRate());

        final DTOList details = invoice.getDetails();

        BigDecimal premi = BDUtil.zero;
        BigDecimal komisi = BDUtil.zero;

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("PREMI")){
                premi = det.getDbEnteredAmount();
            }

            if(det.getTrxLine().getStItemDesc().toUpperCase().contains("KOMISI")){
                komisi = BDUtil.negate(det.getDbEnteredAmount());
            }

        }

        //BigDecimal premi = BDUtil.negate(ri.getDbPremiAmount());
        //BigDecimal komisi = ri.getDbRICommAmount();

        admPremi.setPremium(premi);
        admPremi.setCommission(komisi);

        admPremi.setFee(null);
        admPremi.setDuty(null);

        //BigDecimal discount = BDUtil.mul(BDUtil.div(obj.getDbObjectPremiTotalAmount(), pol.getDbPremiTotal()),pol.getDbNDDisc1(),0);

        admPremi.setDiscount(null);
        admPremi.setSegment("N/A");
        admPremi.setRefId(pol.getStEntityID());

        //pol.getObjects();

        admPremi.setInsured(entity.getStEntityName());
        admPremi.setRemarks("PREMI INWARD "+ pol.getARTrxType().getStDescription());
        admPremi.setMo("1");
        admPremi.setSourceRefId(pol.getStEntityID());

        getAdmlinkPremi().add(admPremi);

    }

    public void EXPORT_ADMLINK_PREMI_INWARD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("PremiResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkPremi h = (AdmLinkPremi) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("VOUCHER");
            row0.createCell(1).setCellValue("DATE");
            row0.createCell(2).setCellValue("BRANCH");
            row0.createCell(3).setCellValue("CT");
            row0.createCell(4).setCellValue("DUEDATE");
            row0.createCell(5).setCellValue("WPC");
            row0.createCell(6).setCellValue("CODE");
            row0.createCell(7).setCellValue("ATYPE");
            row0.createCell(8).setCellValue("Data");
            row0.createCell(9).setCellValue("TYPE");
            row0.createCell(10).setCellValue("ISType");
            row0.createCell(11).setCellValue("POLICYNO");
            row0.createCell(12).setCellValue("Inception");
            row0.createCell(13).setCellValue("Expiry");
            row0.createCell(14).setCellValue("Effective");
            row0.createCell(15).setCellValue("RCODE");
            row0.createCell(16).setCellValue("Risk_Start");
            row0.createCell(17).setCellValue("Risk_End");
            row0.createCell(18).setCellValue("DOCNO");
            row0.createCell(19).setCellValue("REFNO");
            row0.createCell(20).setCellValue("CURRENCY");
            row0.createCell(21).setCellValue("RATE");
            row0.createCell(22).setCellValue("PREMIUM");
            row0.createCell(23).setCellValue("UJROH");
            row0.createCell(24).setCellValue("COMMISSION");
            row0.createCell(25).setCellValue("FEE");
            row0.createCell(26).setCellValue("DUTY");
            row0.createCell(27).setCellValue("DISCOUNT");
            row0.createCell(28).setCellValue("Claim");
            row0.createCell(29).setCellValue("Adjustment");
            row0.createCell(30).setCellValue("Subsidies");
            row0.createCell(31).setCellValue("Subrogation");
            row0.createCell(32).setCellValue("VAT");
            row0.createCell(33).setCellValue("TAX");
            row0.createCell(34).setCellValue("DEPOSIT");
            row0.createCell(35).setCellValue("OTHERFEE_1");
            row0.createCell(36).setCellValue("OTHERFEE_2");
            row0.createCell(37).setCellValue("OTHERTAX_1");
            row0.createCell(38).setCellValue("OTHERTAX_2");
            row0.createCell(39).setCellValue("PNOMINAL_VAT");
            row0.createCell(40).setCellValue("PNOMINAL_TAX");
            row0.createCell(41).setCellValue("SEGMENT");
            row0.createCell(42).setCellValue("PAYMENT");
            row0.createCell(43).setCellValue("NAME");
            row0.createCell(44).setCellValue("ADDRESS");
            row0.createCell(45).setCellValue("REFID");
            row0.createCell(46).setCellValue("TAXTYPE");
            row0.createCell(47).setCellValue("LOB");
            row0.createCell(48).setCellValue("INSURED");
            row0.createCell(49).setCellValue("TSI");
            row0.createCell(50).setCellValue("COVERAGE");
            row0.createCell(51).setCellValue("REMARKS");
            row0.createCell(52).setCellValue("MO");
            row0.createCell(53).setCellValue("SOURCE");
            row0.createCell(54).setCellValue("SOURCE_ADDRESS");
            row0.createCell(55).setCellValue("SOURCE_REFID");
            row0.createCell(56).setCellValue("ACCOUNTNO");
            row0.createCell(57).setCellValue("ACCOUNTNAME");
            row0.createCell(58).setCellValue("BANKNAME");
            row0.createCell(59).setCellValue("BANK");
            row0.createCell(60).setCellValue("BANKBRANCH");
            row0.createCell(61).setCellValue("INSTALLMENT");
            row0.createCell(62).setCellValue("DUEDATE_1");
            row0.createCell(63).setCellValue("AMOUNTDUE_1");
            row0.createCell(64).setCellValue("DUEDATE_2");
            row0.createCell(65).setCellValue("AMOUNTDUE_2");
            row0.createCell(66).setCellValue("DUEDATE_3");
            row0.createCell(67).setCellValue("AMOUNTDUE_3");
            row0.createCell(68).setCellValue("DUEDATE_4");
            row0.createCell(69).setCellValue("AMOUNTDUE_4");
            row0.createCell(70).setCellValue("DUEDATE_5");
            row0.createCell(71).setCellValue("AMOUNTDUE_5");
            row0.createCell(72).setCellValue("DUEDATE_6");
            row0.createCell(73).setCellValue("AMOUNTDUE_6");
            row0.createCell(74).setCellValue("DUEDATE_7");
            row0.createCell(75).setCellValue("AMOUNTDUE_7");
            row0.createCell(76).setCellValue("DUEDATE_8");
            row0.createCell(77).setCellValue("AMOUNTDUE_8");
            row0.createCell(78).setCellValue("DUEDATE_9");
            row0.createCell(79).setCellValue("AMOUNTDUE_9");
            row0.createCell(80).setCellValue("DUEDATE_10");
            row0.createCell(81).setCellValue("AMOUNTDUE_10");
            row0.createCell(82).setCellValue("DUEDATE_11");
            row0.createCell(83).setCellValue("AMOUNTDUE_11");
            row0.createCell(84).setCellValue("DUEDATE_12");
            row0.createCell(85).setCellValue("AMOUNTDUE_12");
            row0.createCell(86).setCellValue("Payment_CC");
            row0.createCell(87).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)
            row.createCell(0).setCellValue("");

            if(h.getVoucher()!=null)
                row.createCell(0).setCellValue(h.getVoucher());

            row.createCell(1).setCellValue(h.getDate());
            row.getCell(1).setCellStyle(cellStyle);

            row.createCell(2).setCellValue(h.getBranch());
            row.createCell(3).setCellValue(h.getCt());
            row.createCell(4).setCellValue(h.getDueDate());
            row.getCell(4).setCellStyle(cellStyle);
            //row.createCell(5).setCellValue(h.getWpc());
            row.createCell(6).setCellValue(h.getCode());
            row.createCell(7).setCellValue(h.getAtype());
            row.createCell(8).setCellValue(h.getData());
            row.createCell(9).setCellValue(h.getType());
            row.createCell(10).setCellValue(h.getIsType());
            row.createCell(11).setCellValue(h.getPolicyNo());
            row.createCell(12).setCellValue(h.getInception());
            row.getCell(12).setCellStyle(cellStyle);
            row.createCell(13).setCellValue(h.getExpiry());
            row.getCell(13).setCellStyle(cellStyle);
            row.createCell(14).setCellValue(h.getEffective());
            row.getCell(14).setCellStyle(cellStyle);
            row.createCell(15).setCellValue(h.getRcode());
            row.createCell(16).setCellValue(h.getRiskStart());
            row.getCell(16).setCellStyle(cellStyle);
            row.createCell(17).setCellValue(h.getRiskEnd());
            row.getCell(17).setCellStyle(cellStyle);
            row.createCell(18).setCellValue(h.getDocNo());
            row.createCell(19).setCellValue(h.getRefNo());
            row.createCell(20).setCellValue(h.getCurrency());
            row.createCell(21).setCellValue(h.getRate().doubleValue());

            if(h.getPremium()!=null)
                row.createCell(22).setCellValue(h.getPremium().doubleValue());

            row.createCell(23).setCellValue(""); //UJROH

            if(h.getCommission()!=null)
                row.createCell(24).setCellValue(h.getCommission().doubleValue());

            if(h.getFee()!=null)
                row.createCell(25).setCellValue(h.getFee().doubleValue());

            if(h.getDuty()!=null)
                row.createCell(26).setCellValue(h.getDuty().doubleValue());

            if(h.getDiscount()!=null)
                row.createCell(27).setCellValue(h.getDiscount().doubleValue());

            row.createCell(28).setCellValue("");
            row.createCell(29).setCellValue("");

            if(h.getSubsidies()!=null)
                row.createCell(30).setCellValue(h.getSubsidies().doubleValue());

            row.createCell(31).setCellValue("");

            if(h.getVat()!=null)
                row.createCell(32).setCellValue(h.getVat().doubleValue());

            if(h.getTax()!=null)
                row.createCell(33).setCellValue(h.getTax().doubleValue());

            row.createCell(34).setCellValue("");
            row.createCell(35).setCellValue("");
            row.createCell(36).setCellValue("");
            row.createCell(37).setCellValue("");
            row.createCell(38).setCellValue("");
            row.createCell(39).setCellValue("");
            //row.createCell(40).setCellValue("PNOMINAL_TAX");
            row.createCell(41).setCellValue(h.getSegment());
            //row.createCell(42).setCellValue(h.getPayment().doubleValue());
            //row.createCell(43).setCellValue("NAME");
            //row.createCell(44).setCellValue("ADDRESS");
            row.createCell(45).setCellValue(h.getRefId());
            //row.createCell(46).setCellValue(h.getTaxType());
            //row.createCell(47).setCellValue(h.getLob());
            row.createCell(48).setCellValue(h.getInsured());
            //row.createCell(49).setCellValue(h.getTsi().doubleValue());
            //row.createCell(50).setCellValue("COVERAGE");
            row.createCell(51).setCellValue(h.getRemarks());
            row.createCell(52).setCellValue(h.getMo());
            //row.createCell(53).setCellValue("SOURCE");
            //row.createCell(54).setCellValue("SOURCE_ADDRESS");
            row.createCell(55).setCellValue(h.getSourceRefId());
            //row.createCell(56).setCellValue("ACCOUNTNO");
            //row.createCell(57).setCellValue("ACCOUNTNAME");
            //row.createCell(58).setCellValue("BANKNAME");
            //row.createCell(59).setCellValue("BANK");
            //row.createCell(60).setCellValue("BANKBRANCH");
            //row.createCell(61).setCellValue("INSTALLMENT");
            /*
            row.createCell(62).setCellValue("DUEDATE_1");
            row.createCell(63).setCellValue("AMOUNTDUE_1");
            row.createCell(64).setCellValue("DUEDATE_2");
            row.createCell(65).setCellValue("AMOUNTDUE_2");
            row.createCell(66).setCellValue("DUEDATE_3");
            row.createCell(67).setCellValue("AMOUNTDUE_3");
            row.createCell(68).setCellValue("DUEDATE_4");
            row.createCell(69).setCellValue("AMOUNTDUE_4");
            row.createCell(70).setCellValue("DUEDATE_5");
            row.createCell(71).setCellValue("AMOUNTDUE_5");
            row.createCell(72).setCellValue("DUEDATE_6");
            row.createCell(73).setCellValue("AMOUNTDUE_6");
            row.createCell(74).setCellValue("DUEDATE_7");
            row.createCell(75).setCellValue("AMOUNTDUE_7");
            row.createCell(76).setCellValue("DUEDATE_8");
            row.createCell(77).setCellValue("AMOUNTDUE_8");
            row.createCell(78).setCellValue("DUEDATE_9");
            row.createCell(79).setCellValue("AMOUNTDUE_9");
            row.createCell(80).setCellValue("DUEDATE_10");
            row.createCell(81).setCellValue("AMOUNTDUE_10");
            row.createCell(82).setCellValue("DUEDATE_11");
            row.createCell(83).setCellValue("AMOUNTDUE_11");
            row.createCell(84).setCellValue("DUEDATE_12");
            row.createCell(85).setCellValue("AMOUNTDUE_12");
            row.createCell(86).setCellValue("Payment_CC");
            row.createCell(87).setCellValue("Pdate");
            */


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_ADMLINK_CLAIM_INWARD() throws Exception {

         //cari closingan reas dulu
         String sqlInward = " select * "+
                            " from ins_pol_inward "+
                            " where approved_flag = 'Y' and ar_trx_type_id in (17,18,19,23) and claim_status = 'DLA' "+
                            " and date_trunc('day',mutation_date) >= ? and date_trunc('day',mutation_date) <= ? ";

         final DTOList listInward = ListUtil.getDTOListFromQuery(
                sqlInward,
                new Object[]{appDateFrom, appDateTo},
                InsurancePolicyInwardView.class);

         admlinkClaim = new DTOList();

         for (int i = 0; i < listInward.size(); i++) {
             InsurancePolicyInwardView inw = (InsurancePolicyInwardView) listInward.get(i);


             //logger.logDebug("########################  CREATE NOTA CARE NO SURAT HUTANG : "+ invoice.getStNoSuratHutang() +" POLIS ke "+ j + " "+ polis.getStPolicyNo());

             EntityView entity = inw.getEntity();

             //addHutangPremiReinsuranceOutwardByInvoice(polis, entity, j, invoice);

             //cari invoice by no invoice
             String sqlInvoice = "select * "+
                                 " from ar_invoice "+
                                 " where invoice_no = ? ";

             final DTOList listInvoice = ListUtil.getDTOListFromQuery(
                sqlInvoice,
                new Object[]{inw.getStInvoiceNo()},
                ARInvoiceView.class);

             for (int j = 0; j < listInvoice.size(); j++) {
                 ARInvoiceView invoice = (ARInvoiceView) listInvoice.get(j);

                 logger.logDebug("########################  CREATE NOTA CARE NO INVOICE : "+ inw.getStInvoiceNo() +" POLIS ke "+ j );

                 addClaimInward(inw, entity, j, invoice);

             }

         }

        SessionManager.getInstance().getRequest().setAttribute("RPT", admlinkClaim);

        //Jika dicentang maka simpan ke tabel utk ke API care
        if (Tools.isYes(stABAFlag)){

            SQLUtil S = new SQLUtil();

            try {
                    final DTOList adm = admlinkClaim;
                    adm.markAllNew();

                    S.store(adm);

            }catch (Exception e) {

                ctx.setRollbackOnly();

                throw e;

            } finally {
                S.release();
            }
        }

        return admlinkClaim;
    }

     public void addClaimInward(InsurancePolicyInwardView pol, EntityView entity, int counter, ARInvoiceView invoice) throws Exception{

        AdmLinkClaim admClaim = new AdmLinkClaim();
        admClaim.markNew();

        admClaim.setVoucher(pol.generateVoucherNo("DN"));
        admClaim.setPolId(pol.getStPolicyID());
        admClaim.setBranch(pol.getStCostCenterCode());
        admClaim.setClaimNo(pol.getStDLANo());
        admClaim.setRefNo(pol.getStInvoiceNo());
        admClaim.setRefId(pol.getStEntityID());
        admClaim.setDate(pol.getDtMutationDate());
        admClaim.setDueDate(DateUtil.addDays(pol.getDtMutationDate(), 30));

        String insured = entity.getStEntityName();

        admClaim.setInsured(insured);
        admClaim.setCode(pol.getStAttrPolicyTypeID());

        String typeTx = "";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("1")) typeTx = "IR";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("2")) typeTx = "IT";
        if(pol.getStARTransactionTypeID().equalsIgnoreCase("3")) typeTx = "IX";

        admClaim.setType(pol.getTreatyTypeCashLoss().getStInsuranceTreatyTypeReferenceClaim());
        admClaim.setIsType("I");
        admClaim.setPolicyNo(pol.getStAttrPolicyNo());

        admClaim.setStartDate(pol.getDtAttrPolicyPeriodStart());
        admClaim.setExpiryDate(pol.getDtAttrPolicyPeriodEnd());

        admClaim.setDateOfLoss(pol.getDtReference2());
        admClaim.setSettledDate(pol.getDtMutationDate());
        admClaim.setRemarks("CLAIM INWARD "+ pol.getTreatyTypeCashLoss().getStInsuranceTreatyTypeName());
        admClaim.setCurrency(pol.getStCurrencyCode());
        admClaim.setRate(pol.getDbCurrencyRate());

        //ANGKA_ANGKA KLAIM
        BigDecimal claimAmount = BDUtil.zero;

        final DTOList details = pol.getDetails();

         for (int i = 0; i < details.size(); i++) {
             InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

             if(det.getStDescription().toUpperCase().contains("KLAIM"))
                 claimAmount = det.getDbEnteredAmount();
         }

        claimAmount = BDUtil.negate(claimAmount);

        admClaim.setClaim(claimAmount);

        //if(!BDUtil.isZeroOrNull(deductible))
            //admClaim.setDeductible(deductible);


        admClaim.setSegment("N/A");
        //admClaim.setMo(pol.getStMarketingOfficerWho());
        admClaim.setMo(pol.getStCreateWho());
        admClaim.setSourceRefId(pol.getStEntityID());

        getAdmlinkClaim().add(admClaim);


    }

     public void EXPORT_ADMLINK_CLAIM_INWARD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("ClaimResult");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            AdmLinkClaim h = (AdmLinkClaim) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("BRANCH");
            row0.createCell(1).setCellValue("CT");
            row0.createCell(2).setCellValue("CLAIMNO");
            row0.createCell(3).setCellValue("REFNO");
            row0.createCell(4).setCellValue("VOUCHER");
            row0.createCell(5).setCellValue("NAME");
            row0.createCell(6).setCellValue("ADDRESS");
            row0.createCell(7).setCellValue("REFID");
            row0.createCell(8).setCellValue("TAXTYPE");
            row0.createCell(9).setCellValue("LOB");
            row0.createCell(10).setCellValue("DATE");
            row0.createCell(11).setCellValue("DUEDATE");
            row0.createCell(12).setCellValue("INSURED");
            row0.createCell(13).setCellValue("CODE");
            row0.createCell(14).setCellValue("RCODE");
            row0.createCell(15).setCellValue("TYPE");
            row0.createCell(16).setCellValue("ISType");
            row0.createCell(17).setCellValue("POLICYNO");
            row0.createCell(18).setCellValue("Start_Date");
            row0.createCell(19).setCellValue("Expiry_Date");
            row0.createCell(20).setCellValue("TSI");
            row0.createCell(21).setCellValue("Date_OF_Loss");
            row0.createCell(22).setCellValue("Settled_Date");
            row0.createCell(23).setCellValue("REMARKS");
            row0.createCell(24).setCellValue("CURRENCY");
            row0.createCell(25).setCellValue("RATE");
            row0.createCell(26).setCellValue("CLAIM");
            row0.createCell(27).setCellValue("DEDUCTIBLE");
            row0.createCell(28).setCellValue("SALVAGE");
            row0.createCell(29).setCellValue("COST");
            row0.createCell(30).setCellValue("TAX");
            row0.createCell(31).setCellValue("SUBROGATION");
            row0.createCell(32).setCellValue("PNOMINAL_VAT");
            row0.createCell(33).setCellValue("PNOMINAL_TAX");
            row0.createCell(34).setCellValue("SEGMENT");
            row0.createCell(35).setCellValue("MO");
            row0.createCell(36).setCellValue("SOURCE");
            row0.createCell(37).setCellValue("SOURCE_ADDRESS");
            row0.createCell(38).setCellValue("SOURCE_REFID");
            row0.createCell(39).setCellValue("ACCOUNTNO");
            row0.createCell(40).setCellValue("ACCOUNTNAME");
            row0.createCell(41).setCellValue("BANKNAME");
            row0.createCell(42).setCellValue("BANK");
            row0.createCell(43).setCellValue("BANKBRANCH");
            row0.createCell(44).setCellValue("INSTALLMENT");
            row0.createCell(45).setCellValue("DUEDATE_1");
            row0.createCell(46).setCellValue("AMOUNTDUE_1");
            row0.createCell(47).setCellValue("DUEDATE_2");
            row0.createCell(48).setCellValue("AMOUNTDUE_2");
            row0.createCell(49).setCellValue("DUEDATE_3");
            row0.createCell(50).setCellValue("AMOUNTDUE_3");
            row0.createCell(51).setCellValue("DUEDATE_4");
            row0.createCell(52).setCellValue("AMOUNTDUE_4");
            row0.createCell(53).setCellValue("DUEDATE_5");
            row0.createCell(54).setCellValue("AMOUNTDUE_5");
            row0.createCell(55).setCellValue("DUEDATE_6");
            row0.createCell(56).setCellValue("AMOUNTDUE_6");
            row0.createCell(57).setCellValue("DUEDATE_7");
            row0.createCell(58).setCellValue("AMOUNTDUE_7");
            row0.createCell(59).setCellValue("DUEDATE_8");
            row0.createCell(60).setCellValue("AMOUNTDUE_8");
            row0.createCell(61).setCellValue("DUEDATE_9");
            row0.createCell(62).setCellValue("AMOUNTDUE_9");
            row0.createCell(63).setCellValue("DUEDATE_10");
            row0.createCell(64).setCellValue("AMOUNTDUE_10");
            row0.createCell(65).setCellValue("DUEDATE_11");
            row0.createCell(66).setCellValue("AMOUNTDUE_11");
            row0.createCell(67).setCellValue("DUEDATE_12");
            row0.createCell(68).setCellValue("AMOUNTDUE_12");
            row0.createCell(69).setCellValue("Payment_CC");
            row0.createCell(70).setCellValue("Pdate");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)

            row.createCell(0).setCellValue(h.getBranch());
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue(h.getClaimNo());
            row.createCell(3).setCellValue(h.getRefNo());
            row.createCell(4).setCellValue(h.getVoucher());
            row.createCell(7).setCellValue(h.getRefId());
            row.createCell(10).setCellValue(h.getDate());
            row.getCell(10).setCellStyle(cellStyle);
            row.createCell(11).setCellValue(h.getDueDate());
            row.getCell(11).setCellStyle(cellStyle);
            row.createCell(12).setCellValue(h.getInsured());
            row.createCell(13).setCellValue(h.getCode());
            row.createCell(15).setCellValue(h.getType());
            row.createCell(16).setCellValue(h.getIsType());
            row.createCell(17).setCellValue(h.getPolicyNo());
            row.createCell(18).setCellValue(h.getStartDate());
            row.getCell(18).setCellStyle(cellStyle);
            row.createCell(19).setCellValue(h.getExpiryDate());
            row.getCell(19).setCellStyle(cellStyle);
            row.createCell(21).setCellValue(h.getDateOfLoss());
            row.getCell(21).setCellStyle(cellStyle);
            row.createCell(22).setCellValue(h.getSettledDate());
            row.getCell(22).setCellStyle(cellStyle);
            row.createCell(23).setCellValue(h.getRemarks());
            row.createCell(24).setCellValue(h.getCurrency());
            row.createCell(25).setCellValue(h.getRate().doubleValue());

            if(h.getClaim()!=null)
                row.createCell(26).setCellValue(h.getClaim().doubleValue());

            if(h.getDeductible()!=null)
                row.createCell(27).setCellValue(h.getDeductible().doubleValue());

            if(h.getSalvage()!=null)
                row.createCell(28).setCellValue(h.getSalvage().doubleValue());

            if(h.getCost()!=null)
                row.createCell(29).setCellValue(h.getCost().doubleValue());

            if(h.getTax()!=null)
                row.createCell(30).setCellValue(h.getTax().doubleValue());

            if(h.getSubrogation()!=null)
                row.createCell(31).setCellValue(h.getSubrogation().doubleValue());

            if(h.getPNominalVat()!=null)
                row.createCell(32).setCellValue(h.getPNominalVat().doubleValue());

            if(h.getPNominalTax()!=null)
                row.createCell(33).setCellValue(h.getPNominalTax().doubleValue());

            row.createCell(34).setCellValue(h.getSegment());
            row.createCell(35).setCellValue(h.getMo());

            row.createCell(38).setCellValue(h.getSourceRefId());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

     public DTOList EXCEL_PROFILE() throws Exception {


        final DTOList listProfile = ListUtil.getDTOListFromQuery(
                                    "select ent_id, cc_code AS branch,ref1 as kode_lob, "+
                                    " (select ref3 from s_valueset x where vs_group = 'COMP_TYPE' and vs_code = a.ref1) as kode_lob_care, "+
                                    " (select vs_description from s_valueset x where vs_group = 'COMP_TYPE' and vs_code = a.ref1) as desc_lob, "+
                                    " ent_name ,address as address_fix, "+
                                    " case when tax_code = 'PPH23' then 'PASAL23' when tax_code = 'PPH21' then 'PASAL21' else null end as tax_type, "+
                                    " coalesce(replace(replace(tax_file,'-',''),'.',''),'000000000000000') as tax_file_care,* "+
                                    " from ent_master a "+
                                    " where coalesce(active_flag,'Y') <> 'N' "+
                                    " and ent_Id > 1042623 "+
                                    " order by ent_id "+
                                    " ",
                    EntityView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", listProfile);


        return listProfile;
    }

     public void EXPORT_PROFILE() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("Profile");

        CreationHelper createHelper = wb.getCreationHelper();

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            EntityView h = (EntityView) list.get(i);

            logger.logDebug("########################  CREATE PROFILE : "+ h.getStEntityID() +" data ke "+ i );

            //bikin header
            XSSFRow row0 = sheet.createRow(0);

            row0.createCell(0).setCellValue("ID");
            row0.createCell(1).setCellValue("Branch");
            row0.createCell(2).setCellValue("PType");
            row0.createCell(3).setCellValue("LOB");
            row0.createCell(4).setCellValue("IntermediaryF");
            row0.createCell(5).setCellValue("CorporateF");
            row0.createCell(6).setCellValue("RefID");
            row0.createCell(7).setCellValue("Salutation");
            row0.createCell(8).setCellValue("Name");
            row0.createCell(9).setCellValue("Address_1");
            row0.createCell(10).setCellValue("Address_2");
            row0.createCell(11).setCellValue("Address_3");
            row0.createCell(12).setCellValue("City");
            row0.createCell(13).setCellValue("ZipCode");
            row0.createCell(14).setCellValue("Phone_1");
            row0.createCell(15).setCellValue("Phone_2");
            row0.createCell(16).setCellValue("Mobile_1");
            row0.createCell(17).setCellValue("Mobile_2");
            row0.createCell(18).setCellValue("Fax_1");
            row0.createCell(19).setCellValue("Fax_2");
            row0.createCell(20).setCellValue("Email_1");
            row0.createCell(21).setCellValue("Email_2");
            row0.createCell(22).setCellValue("Province");
            row0.createCell(23).setCellValue("Country");
            row0.createCell(24).setCellValue("Area");
            row0.createCell(25).setCellValue("PIC");
            row0.createCell(26).setCellValue("PICTitle");
            row0.createCell(27).setCellValue("PICAddress");
            row0.createCell(28).setCellValue("PICPhone");
            row0.createCell(29).setCellValue("CType");
            row0.createCell(30).setCellValue("CGroup");
            row0.createCell(31).setCellValue("SCGroup");
            row0.createCell(32).setCellValue("ID_Type");
            row0.createCell(33).setCellValue("ID_No");
            row0.createCell(34).setCellValue("ID_Name");
            row0.createCell(35).setCellValue("Gender");
            row0.createCell(36).setCellValue("BirthDate");
            row0.createCell(37).setCellValue("BirthPlace");
            row0.createCell(38).setCellValue("TaxType");
            row0.createCell(39).setCellValue("TaxID");
            row0.createCell(40).setCellValue("TaxName");
            row0.createCell(41).setCellValue("TaxAddress");
            row0.createCell(42).setCellValue("VAT");
            row0.createCell(43).setCellValue("VatSubsidiesF");
            row0.createCell(44).setCellValue("TAX");
            row0.createCell(45).setCellValue("TaxLoading");
            row0.createCell(46).setCellValue("LicenseNo");
            row0.createCell(47).setCellValue("LicenseDate");
            row0.createCell(48).setCellValue("LicenseExpiry");
            row0.createCell(49).setCellValue("HSCode");
            row0.createCell(50).setCellValue("BIType");



            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            //if(h.getFieldValueByFieldNameBD("pol_id")!=null)
            row.createCell(0).setCellValue(h.getStEntityID());
            row.createCell(1).setCellValue(h.getStCostCenterCode());
            row.createCell(2).setCellValue("D");
            row.createCell(3).setCellValue(h.getStLOBCare());
            row.createCell(4).setCellValue("1");

            if(h.getStEntityClass().equalsIgnoreCase("INST"))
                row.createCell(5).setCellValue("1");
            else
                row.createCell(5).setCellValue("0");

            row.createCell(6).setCellValue(h.getStEntityID());
            //row.createCell(7).setCellValue("Salutation");
            row.createCell(8).setCellValue(h.getStEntityName());
            row.createCell(9).setCellValue(h.getStAddressFix());
            //row.createCell(10).setCellValue("Address_2");
            //row.createCell(11).setCellValue("Address_3");
            //row.createCell(12).setCellValue("City");
            //row.createCell(13).setCellValue("ZipCode");
            row.createCell(14).setCellValue("-");
            row.createCell(15).setCellValue("-");
            row.createCell(16).setCellValue("-");
            row.createCell(17).setCellValue("-");
            //row.createCell(18).setCellValue("Fax_1");
            //row.createCell(19).setCellValue("Fax_2");
            //row.createCell(20).setCellValue("Email_1");
            //row.createCell(21).setCellValue("Email_2");
            //row.createCell(22).setCellValue("Province");
            row.createCell(23).setCellValue("ID");
            //row.createCell(24).setCellValue("Area");
            row.createCell(25).setCellValue("-");
            //row.createCell(26).setCellValue("PICTitle");
            //row.createCell(27).setCellValue("PICAddress");
            //row.createCell(28).setCellValue("PICPhone");
            row.createCell(29).setCellValue("-");
            row.createCell(30).setCellValue(h.getStRef2());
            //row.createCell(31).setCellValue("SCGroup");
            //row.createCell(32).setCellValue("ID_Type");
            //row.createCell(33).setCellValue("ID_No");
            //row.createCell(34).setCellValue("ID_Name");

            if(h.getStEntityClass().equalsIgnoreCase("INDV")){
                row.createCell(35).setCellValue("MALE");
                row.createCell(36).setCellValue(new Date());
                row.getCell(36).setCellStyle(cellStyle);
                row.createCell(37).setCellValue("JAKARTA");
            }

            row.createCell(38).setCellValue(h.getStTaxType());
            row.createCell(39).setCellValue(h.getStTaxFileCare());
            //row.createCell(40).setCellValue("TaxName");
            //row.createCell(41).setCellValue("TaxAddress");
            //row.createCell(42).setCellValue("VAT");
            //row.createCell(43).setCellValue("VatSubsidiesF");
            //row.createCell(44).setCellValue("TAX");
            //row.createCell(45).setCellValue("TaxLoading");
            //row.createCell(46).setCellValue("LicenseNo");
            //row.createCell(47).setCellValue("LicenseDate");
            //row.createCell(48).setCellValue("LicenseExpiry");
            //row.createCell(49).setCellValue("HSCode");
            //row.createCell(50).setCellValue("BIType");


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName()+"_"+ DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH-mm-ss")  + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

}

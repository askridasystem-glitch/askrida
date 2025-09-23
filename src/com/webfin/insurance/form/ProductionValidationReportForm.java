/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionValidationReportForm
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
import com.crux.common.model.HashDTO;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.InsurancePolicyTypeGroupView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsuranceTreatyTypesView;
import com.webfin.FinCodec;
import com.crux.lang.LanguageManager;
import com.crux.util.DateUtil;
import com.crux.util.FTPUploadFile;
import com.crux.util.FileTransferDirectory;
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

import java.io.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.servlet.ServletOutputStream;

import com.crux.util.SQLUtil;
import com.webfin.entity.model.EntityView;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import java.sql.PreparedStatement;
import javax.ejb.SessionContext;

public class ProductionValidationReportForm extends Form {

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
    private Date receiptDateFrom;
    private Date receiptDateTo;
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
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;
    private String stBranchSource;
    private String stBranchSourceDesc;
    private String stRegionSource;
    private String stRegionSourceDesc;
    String stTreatyYear = "";
    private DTOList list;
    private String stABAFlag = "N";
    private String stIndex = "N";
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionUtilitiesReportForm.class);

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
        setTitle("VALIDASI DATA");

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
     * @return the receiptDateFrom
     */
    public Date getReceiptDateFrom() {
        return receiptDateFrom;
    }

    /**
     * @param receiptDateFrom the receiptDateFrom to set
     */
    public void setReceiptDateFrom(Date receiptDateFrom) {
        this.receiptDateFrom = receiptDateFrom;
    }

    /**
     * @return the receiptDateTo
     */
    public Date getReceiptDateTo() {
        return receiptDateTo;
    }

    /**
     * @param receiptDateTo the receiptDateTo to set
     */
    public void setReceiptDateTo(Date receiptDateTo) {
        this.receiptDateTo = receiptDateTo;
    }

    public DTOList EXCEL_CEK_TITIPAN_WITH_JURNAL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.trx_no,a.months,a.years,sum(a.amount) as total_titipan,"
                + " (select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2) end)"
                + "  from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id where y.accountno like '489%' and z.trx_no = a.trx_no )as total_jurnal");

        sqa.addQuery("from ar_titipan_premi a");
        sqa.addClause(" coalesce(approved, 'N') = 'Y' ");

        if (receiptDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(receiptDateFrom);
        }

        if (receiptDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(receiptDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addGroup("a.trx_no,a.months,a.years");

        final String sql = " select substr(trx_no,6,2) as cabang,* from ( " + sqa.getSQL() + " ) x where total_titipan <> total_jurnal"
                + " order by years::bigint,months::bigint";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_TITIPAN_WITH_JURNAL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_titipan_jurnal");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR BUKTI");
            row0.createCell(2).setCellValue("BULAN");
            row0.createCell(3).setCellValue("TAHUN");
            row0.createCell(4).setCellValue("TOTAL TITIPAN");
            row0.createCell(5).setCellValue("TOTAL JURNAL");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("total_titipan").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_REALISASI_WITH_JURNAL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.receipt_no2,a.months,a.years,sum(b.titipan_premi_used_amount) as titipan_premi_terpakai,"
                + " (select sum(case when z.debit <> 0 then round(z.debit,2) else round(z.credit,2) * -1 end)"
                + "   from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id where y.accountno like '489%' and z.trx_no = a.receipt_no2 )as total_jurnal");

        sqa.addQuery(" from ar_receipt a inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id ");
        sqa.addClause(" a.ar_settlement_id in (25,33) ");
        sqa.addClause(" coalesce(a.posted_flag, 'N') = 'Y' ");
        sqa.addClause(" b.titipan_premi_id is not null ");

        if (receiptDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) >= ?");
            sqa.addPar(receiptDateFrom);
        }

        if (receiptDateTo != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) <= ?");
            sqa.addPar(receiptDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addGroup("a.receipt_no2,a.months,a.years");

        final String sql = " select substr(receipt_no2,6,2) as cabang,* from ( " + sqa.getSQL() + " )  zz where titipan_premi_terpakai <> total_jurnal"
                + " order by years::bigint,months::bigint";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_REALISASI_WITH_JURNAL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_titipan_jurnal");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR BUKTI");
            row0.createCell(2).setCellValue("BULAN");
            row0.createCell(3).setCellValue("TAHUN");
            row0.createCell(4).setCellValue("TOTAL REALISASI");
            row0.createCell(5).setCellValue("TOTAL JURNAL");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("receipt_no2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("titipan_premi_terpakai").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_PRODUKSI_WITH_JURNAL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code as koda,a.create_date as tglentry,a.policy_date as tglpolis,a.period_start as periode_awal,a.period_end as periode_akhir, "
                + " a.pol_id,';'||a.pol_no as pol_no,a.cust_name as tertanggung,a.entity_id,';'||c.ref_ent_id as kodeko,';'||a.ref1 as no_pp,a.ref5 as nama_pp,a.coins_pol_no as no_polis_rujukan, "
                + "  a.ccy,a.ccy_rate as kurs, "
                + "  getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1) as premi_bruto,  "
                + "  c.ref_ent_id,a.pol_no as nopol,b.coins_type,c.ent_name as koas,c.gl_code");

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_coins b on b.policy_id = a.pol_id  "
                + "    left join ent_master c on c.ent_id = b.entity_id   "
                + "    left join ent_master d on d.ent_id = a.entity_id   "
                + "    left join ent_master e on e.ent_id = a.prod_id   ");

        sqa.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL') ");
        sqa.addClause(" (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') and a.active_flag='Y' and a.effective_flag='Y' ");
        sqa.addClause(" c.ref_ent_id = '00' ");

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

        final String sql = " select * "
                + " from ( "
                + " select koda,pol_id,pol_no,round(premi_bruto,2) as premi_bruto, "
                + " coalesce((select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2)  end) "
                + " from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id  "
                + " where (case when a.ref_ent_id = '00' then (y.accountno like '6131%' or y.accountno like '6132%' or y.accountno like '7914%') else y.accountno like '6133%' || gl_code || '%' end) and z.pol_no = a.nopol ),0)as total_jurnal "
                + " from ( " + sqa.getSQL() + " )  a "
                + ")zz where round(premi_bruto,0) <> round(total_jurnal,0) ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PRODUKSI_WITH_JURNAL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_produksi_jurnal");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR POLIS");
            row0.createCell(2).setCellValue("PREMI");
            row0.createCell(3).setCellValue("JURNAL");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("premi_bruto").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_PRODUKSI_DOUBLE_INVOICE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,pol_no,round(a.premi_netto * a.ccy_rate,0) as premi_netto, "
                + " (select round(sum(amount),0) from ar_invoice x where x.ar_trx_type_id in (5,6,7) and coalesce(x.cancel_flag,'N') <> 'Y' and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id) as total_invoice");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status in ('POLICY','ENDORSE','RENEWAL')  and effective_flag = 'Y' and active_flag = 'Y' ");

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

        sqa.addGroup("a.pol_id,pol_no");

        final String sql = " select a.cc_code,a.policy_date,a.pol_id,b.ar_invoice_id,a.pol_no,round(a.premi_netto * a.ccy_rate,0) as premi_netto,"
                + "   (select round(sum(amount),0) from ar_invoice x where x.ar_trx_type_id in (5,6,7) and coalesce(x.cancel_flag,'N') <> 'Y' and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id) as total_invoice,"
                + "   b.receipt_no,b.receipt_date"
                + "   from ins_policy a"
                + "   inner join ar_invoice b on a.pol_id = b.attr_pol_id"
                + "   where ar_trx_type_id in (5,6,7) and coalesce(b.cancel_flag,'N') <> 'Y' and coalesce(b.posted_flag,'Y') = 'Y'"
                + "   and a.pol_id in ("
                + "                select m.pol_id "
                + "                from ( " + sqa.getSQL() + ") m "
                + "                inner join ins_policy n on m.pol_id = n.pol_id "
                + "                where m.premi_netto <> 0 and m.premi_netto <> m.total_invoice and (m.premi_netto - m.total_invoice) not in (1,-1) ) order by pol_id,b.ar_invoice_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PRODUKSI_DOUBLE_INVOICE() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_produksi_double");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR POLIS");
            row0.createCell(2).setCellValue("TANGGAL POLIS");
            row0.createCell(3).setCellValue("PREMI NETTO");
            row0.createCell(4).setCellValue("TAGIHAN NETTO");
            row0.createCell(5).setCellValue("NO BUKTI BAYAR");
            row0.createCell(6).setCellValue("TANGGAL BAYAR");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi_netto").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("total_invoice").doubleValue());
            if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            }
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_PEMBAYARAN_WITH_JURNAL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_receipt_id,upper(f.description) as description,a.receipt_no,e.attr_pol_no,e.attr_pol_id,a.months,a.years, "
                + " sum(case when c.negative_flag = 'Y' and tax_flag is null then c.amount * -1 when c.negative_flag <> 'Y' and tax_flag is null then c.amount end) as tagihan_netto_per_invoice,"
                + " sum(case when c.negative_flag = 'Y' then c.amount * -1 when c.negative_flag <> 'Y' and tax_flag is null then c.amount end) as tagihan_netto_per_invoice2,"
                + " (select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2) end)"
                + " from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id where y.accountno like '133%' and z.trx_no = a.receipt_no and z.pol_no = e.attr_pol_no)as total_jurnal");

        sqa.addQuery(" from ar_receipt a "
                + " inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id "
                + " inner join ar_invoice_details c on b.ar_invoice_dtl_id = c.ar_invoice_dtl_id "
                + " inner join ar_invoice e on b.ar_invoice_id = e.ar_invoice_id "
                + " left join ar_settlement f on f.ar_settlement_id = a.ar_settlement_id ");

        sqa.addClause(" a.ar_settlement_id in (1,25,38,41) and coalesce(a.posted_flag, 'N') = 'Y'  ");

        if (receiptDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) >= ?");
            sqa.addPar(receiptDateFrom);
        }

        if (receiptDateTo != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) <= ?");
            sqa.addPar(receiptDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("e.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addGroup("a.ar_receipt_id,f.description,a.receipt_no,e.attr_pol_no,e.attr_pol_id,a.months,a.years");

        final String sql = " select substr(receipt_no,5,2) as cabang,*, "
                + " (select premi_netto from ins_policy x where x.pol_id = xx.attr_pol_id) as premi_netto_produk"
                + " from "
                + " ( " + sqa.getSQL() + " ) xx "
                + " where (tagihan_netto_per_invoice <> total_jurnal) and (tagihan_netto_per_invoice2 <> total_jurnal) "
                + " order by ar_receipt_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PEMBAYARAN_WITH_JURNAL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_pembayaran_dengan_jurnal");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR BUKTI");
            row0.createCell(2).setCellValue("NOMOR POLIS");
            row0.createCell(3).setCellValue("BULAN");
            row0.createCell(4).setCellValue("TAHUN");
            row0.createCell(5).setCellValue("PEMBAYARAN NETTO");
            row0.createCell(6).setCellValue("TOTAL JURNAL");
            row0.createCell(7).setCellValue("TAGIHAN NETTO POLIS");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("attr_pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("tagihan_netto_per_invoice").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_netto_produk").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_KLAIM_DOUBLE_INVOICE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,pol_no,a.dla_no,round(a.claim_amount * a.ccy_rate_claim,0) as claim_amount, "
                + " ( select coalesce(round(sum(y.amount*(getpremi(y.negative_flag = 'Y',-1,1))),0),0) "
                + " from ar_invoice x "
                + " inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                + " where x.ar_trx_type_id in (12,26) and coalesce(x.cancel_flag,'N') <> 'Y' "
                + " and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice,"
                + " ( select coalesce(round(sum(y.amount*(getpremi(y.negative_flag = 'Y',-1,1))),0),0)  from ar_invoice x "
                + " inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                + " where x.ar_trx_type_id in (11) and coalesce(x.cancel_flag,'N') <> 'Y' "
                + " and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as jasa_bengkel ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' and effective_flag = 'Y' and active_flag = 'Y' ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addGroup("a.pol_id,pol_no");

        final String sql = " select a.cc_code,a.policy_date,a.dla_no,a.pol_id,b.ar_invoice_id,a.pol_no,round(a.claim_amount * a.ccy_rate_claim,0) as claim_amount, "
                + " ( select coalesce(round(sum(y.amount*(getpremi(y.negative_flag = 'Y',-1,1))),0),0) "
                + " from ar_invoice x "
                + " inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                + " where x.ar_trx_type_id in (12,26) and coalesce(x.cancel_flag,'N') <> 'Y' "
                + " and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice,"
                + " b.receipt_no,b.receipt_date "
                + " from ins_policy a"
                + " inner join ar_invoice b on a.pol_id = b.attr_pol_id"
                + " where ar_trx_type_id in (12,26) and coalesce(b.cancel_flag,'N') <> 'Y' and coalesce(b.posted_flag,'Y') = 'Y'"
                + " and a.pol_id in ( "
                + " select m.pol_id "
                + " from ( "
                + sqa.getSQL() + " ) m "
                + " inner join ins_policy n on m.pol_id = n.pol_id "
                + " where m.claim_amount <> 0 and m.claim_amount <> m.total_invoice and (m.claim_amount - m.total_invoice) not in (1,-1) ) order by pol_id,b.ar_invoice_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_KLAIM_DOUBLE_INVOICE() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet 1
        HSSFSheet sheet = wb.createSheet("cek_klaim_double");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR POLIS");
            row0.createCell(2).setCellValue("NOMOR LKP");
            row0.createCell(3).setCellValue("TANGGAL POLIS");
            row0.createCell(4).setCellValue("KLAIM NETTO");
            row0.createCell(5).setCellValue("UTANG KLAIM NETTO");
            row0.createCell(6).setCellValue("TOTAL JURNAL");
            row0.createCell(7).setCellValue("NO BUKTI BAYAR");
            row0.createCell(8).setCellValue("TANGGAL BAYAR");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("total_invoice").doubleValue());
            if (h.getFieldValueByFieldNameBD("total_jurnal") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            }
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }

        }

        HSSFSheet sheet2 = wb.createSheet("cek_hutang_ke_prod");

        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");

        for (int j = 0; j < list2.size(); j++) {
            HashDTO h = (HashDTO) list2.get(j);

            //bikin header
            HSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR POLIS");
            row0.createCell(2).setCellValue("POLIS ID");
            row0.createCell(3).setCellValue("NOMOR LKP");
            row0.createCell(4).setCellValue("TANGGAL LKP");
            row0.createCell(5).setCellValue("JUMLAH HUTANG");
            row0.createCell(6).setCellValue("JUMLAH PROD KLAIM");

            //bikin isi cell
            HSSFRow row = sheet2.createRow(j + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("attr_pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("attr_pol_id"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("refid2"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("total_prod_claim").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_KOMISI_DOUBLE_INVOICE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.pol_id::text,';'||a.pol_no as pol_no,b.ent_id::text,(b.amount-b.tax_amount) as amount,"
                + "';'||(b.pol_id||''||b.ent_id||''||round(b.amount-b.tax_amount,0)) as zzz,"
                + "count(b.pol_id||''||b.ent_id||''||b.amount-b.tax_amount) as comm ");

        sqa.addQuery(" from ins_policy a inner join ins_pol_items b on b.pol_id = a.pol_id "
                + "inner join ins_items c on c.ins_item_id = b.ins_item_id ");

        sqa.addClause(" status in ('POLICY','RENEWAL','ENDORSE') and a.active_flag = 'Y' and a.effective_flag = 'Y' and c.item_type = 'COMIS' and c.item_group <> 'FEEBASE' ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        sqa.addGroup("b.pol_id,a.pol_no,b.ent_id,b.amount,b.tax_amount");

        sqa.addOrder("a.pol_no");

        final String sql = " select * from ( "
                + "select a.*,b.oscomm "
                + "from ( " + sqa.getSQL() + ") a "
                + "left join ( "
                + "select attr_pol_id,';'||attr_pol_no as attr_pol_no,ent_id,amount,';'||(attr_pol_id||''||ent_id||''||round(amount,0)) as zzz,"
                + "count(attr_pol_id||''||ent_id||''||amount) as oscomm "
                + "from ar_invoice a "
                + "where ar_trx_type_id = 11 and a.no_surat_hutang is null and coalesce(a.cancel_flag,'') <> 'Y' and a.amount_settled is null "
                + "group by invoice_no,attr_pol_id,attr_pol_no,ent_id,amount order by attr_pol_no ) b on b.zzz = a.zzz order by a.pol_no ) a where comm < oscomm ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_KOMISI_DOUBLE_INVOICE() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_komisi_double");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("polid");
            row0.createCell(1).setCellValue("polis");
            row0.createCell(2).setCellValue("entid");
            row0.createCell(3).setCellValue("nilai");
            row0.createCell(4).setCellValue("jumlah komisi produksi");
            row0.createCell(5).setCellValue("jumlah komisi os");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("ent_id"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("comm").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("oscomm").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_PROD_KLAIM_2_WAY() throws Exception {

        EXCEL_CEK_PROD_KLAIM_TO_FIN_ACCOUNT();

        EXCEL_CEK_HUT_KLAIM_TO_PROD();

        return null;
    }

    public DTOList EXCEL_CEK_PROD_KLAIM_TO_FIN_ACCOUNT() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,pol_no,a.dla_no,"
                //+ "round(a.claim_amount * a.ccy_rate_claim,0) as claim_amount,"
                + " round((select sum(getpremi(c.use_tax = 'Y',b.amount-b.tax_amount,b.amount*(getpremi(c.negative_flag = 'Y',-1,1)))) "
                + " from ins_policy x inner join ins_pol_items b on b.pol_id = x.pol_id and b.item_class = 'CLM' "
                + " inner join ins_items c on c.ins_item_id = b.ins_item_id and c.ins_item_id <> 61 where x.pol_id = a.pol_id)*a.ccy_rate_claim,0) as claim_amount, "
                + "		 ( select coalesce(round(sum(y.amount*(getpremi((x.ar_trx_type_id = 11 and y.negative_flag = 'N' and y.tax_flag = 'Y') "
                + "or (x.ar_trx_type_id = 12 and y.negative_flag = 'Y'),-1,1))),0),0) "
                + "from ar_invoice x inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                + "where x.ar_trx_type_id in (12,26,11) and coalesce(x.cancel_flag,'N') <> 'Y' and (y.ar_trx_line_id <> 96 or y.negative_flag <> 'Y') "
                + "and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice, "
                + "		 ("
                + "			select round(sum(credit-debit),0) from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id"
                + "			where (k.accountno like '3371%' or k.accountno like '48930%') and substr(j.trx_no,1,1) = 'K' and j.pol_no = a.pol_no"
                + "			and trim(substring(TRIM(right(j.description, 23)), position('LKP' in TRIM(right(j.description, 23))))) = a.dla_no"
                + "			and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)"
                + "		) as total_jurnal,"
                + "				("
                + "			select round(sum(debit-credit),0) from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id"
                + "			where (k.accountno like '14910%') and substr(j.trx_no,1,1) = 'L' and j.pol_no = a.pol_no"
                + "			and trim(substring(TRIM(right(j.description, 23)), position('LKP' in TRIM(right(j.description, 23))))) = a.dla_no"
                + "			and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)"
                + "		) as total_jurnal_koas ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' and effective_flag = 'Y' and active_flag = 'Y' ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        final String sql = "  select a.cc_code,a.policy_date,a.dla_date,a.pol_id,b.ar_invoice_id,a.pol_no,a.dla_no,"
                //+ "round(a.claim_amount*a.ccy_rate_claim,0) as claim_amount,"
                + " round((select sum(getpremi(c.use_tax = 'Y',b.amount-b.tax_amount,b.amount*(getpremi(c.negative_flag = 'Y',-1,1)))) "
                + " from ins_policy x inner join ins_pol_items b on b.pol_id = x.pol_id and b.item_class = 'CLM' "
                + " inner join ins_items c on c.ins_item_id = b.ins_item_id and c.ins_item_id <> 61 where x.pol_id = a.pol_id)*a.ccy_rate_claim,0) as claim_amount, "
                + "		 ( select coalesce(round(sum(y.amount*(getpremi((x.ar_trx_type_id = 11 and y.negative_flag = 'N' and y.tax_flag = 'Y') "
                + "or (x.ar_trx_type_id = 12 and y.negative_flag = 'Y'),-1,1))),0),0) "
                + "from ar_invoice x inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                + "where x.ar_trx_type_id in (12,26,11) and coalesce(x.cancel_flag,'N') <> 'Y' and (y.ar_trx_line_id <> 96 or y.negative_flag <> 'Y') "
                + "and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice, "
                + "		("
                + "			select round(sum(credit-debit),0) from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id"
                + "			where (k.accountno like '3371%' or k.accountno like '48930%') and substr(j.trx_no,1,1) = 'K' and j.pol_no = a.pol_no"
                + "			and trim(substring(TRIM(right(j.description, 23)), position('LKP' in TRIM(right(j.description, 23))))) = a.dla_no"
                + "			and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)"
                + "		) as total_jurnal,"
                + "		("
                + "			select round(sum(debit-credit),0) from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id"
                + "			where (k.accountno like '14910%') and substr(j.trx_no,1,1) = 'L' and j.pol_no = a.pol_no"
                + "			and trim(substring(TRIM(right(j.description, 23)), position('LKP' in TRIM(right(j.description, 23))))) = a.dla_no"
                + "			and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)"
                + "		) as total_jurnal_koas,"
                + "           b.receipt_no,b.receipt_date"
                + "           from ins_policy a"
                + "           inner join ar_invoice b on a.pol_id = b.attr_pol_id"
                + "           where ar_trx_type_id in (12,26) and coalesce(b.cancel_flag,'N') <> 'Y' and coalesce(b.posted_flag,'Y') = 'Y' and coalesce(a.admin_notes,'') <> 'OK' "
                + "           and a.pol_id in ("
                + "                           select m.pol_id"
                + "                        from ("
                + sqa.getSQL() + " ) m "
                + "                 inner join ins_policy n on m.pol_id = n.pol_id"
                + "                 where coalesce(m.claim_amount,0) <> coalesce(m.total_invoice,0) or coalesce(m.claim_amount,0) <> coalesce(m.total_jurnal,0) "
                + "                        or coalesce(m.total_invoice,0) <> coalesce(m.total_jurnal,0)"
                + "                ) order by pol_id,b.ar_invoice_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l);

        return l;
    }

    public DTOList EXCEL_CEK_HUT_KLAIM_TO_PROD() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" cc_code,attr_pol_no,attr_pol_id::character varying,a.refid2,a.mutation_date,round(sum(case when b.negative_flag = 'Y' then b.amount*-1 else b.amount end),0) as amount,"
                + " coalesce((select round(coalesce(claim_amount*ccy_rate_claim),0) "
                + " from ins_policy x where x.pol_id = a.pol_id),0) as total_prod_claim ");

        sqa.addQuery(" from ar_invoice a "
                + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id ");

        sqa.addClause(" a.ar_trx_type_id in (12,26) and coalesce(a.cancel_flag,'N') <> 'Y' and coalesce(a.claim_status,'') <> 'OK' and coalesce(a.posted_flag,'Y') = 'Y' ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day', a.mutation_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day', a.mutation_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addGroup("cc_code,attr_pol_no,attr_pol_id,a.refid2,a.mutation_date,pol_id");

        final String sql = "  select * "
                + "                        from ("
                + sqa.getSQL() + " ) m "
                + "                 where m.amount <> m.total_prod_claim ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", l);

        return l;
    }

    public DTOList EXCEL_CEK_PEMBAYARAN_KOMISI_WITH_JURNAL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.receipt_no2,a.months,a.years,sum(b.titipan_premi_used_amount) as titipan_premi_terpakai,"
                + "	(select sum(case when z.debit <> 0 then round(z.debit,2) else round(z.credit,2) * -1 end)"
                + "	  from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id where y.accountno like '489%' and z.trx_no = a.receipt_no2 )as total_jurnal ");

        sqa.addQuery(" from ar_receipt a"
                + "	inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id ");

        sqa.addClause(" a.ar_settlement_id in (2,33,39) ");
        sqa.addClause(" coalesce(a.posted_flag, 'N') = 'Y' and b.titipan_premi_id is not null ");

        if (receiptDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) >= ?");
            sqa.addPar(receiptDateFrom);
        }

        if (receiptDateTo != null) {
            sqa.addClause("date_trunc('day',a.receipt_date) <= ?");
            sqa.addPar(receiptDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        final String sql = " SELECT substr(receipt_no2,6,2) as cabang,*"
                + " FROM ( "
                + sqa.getSQL() + " group by a.receipt_no2,a.months,a.years"
                + " ) zz where titipan_premi_terpakai <> total_jurnal "
                + " order by years::bigint,months::bigint ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PEMBAYARAN_KOMISI_WITH_JURNAL() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_titipan_jurnal");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("NOMOR BUKTI");
            row0.createCell(2).setCellValue("BULAN");
            row0.createCell(3).setCellValue("TAHUN");
            row0.createCell(4).setCellValue("TOTAL TITIPAN");
            row0.createCell(5).setCellValue("TOTAL JURNAL");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("receipt_no2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("titipan_premi_terpakai").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("total_jurnal").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_JURNAL_WITH_PROD_KLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" pol_no,trim(substring(TRIM(right(a.description, 23)), position('LKP' in TRIM(right(a.description, 23))))) as no_lkp, applydate,"
                + "credit-debit as amount,"
                + "	(SELECT approved_date FROM INS_POLICY where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y'"
                + "		and dla_no = trim(substring(TRIM(right(a.description, 23)), position('LKP' in TRIM(right(a.description, 23)))))) as tgl_approved ");

        sqa.addQuery(" from gl_je_detail a "
                + " inner join gl_accounts b on a.accountid = b.account_id ");

        sqa.addClause(" b.accountno like '3371%' and substr(a.trx_no,1,1) = 'K'  and a.description like '%LKP%' ");

        if (receiptDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(receiptDateFrom);
        }

        if (receiptDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(receiptDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        String sql = sqa.getSQL() + " and trim(substring(TRIM(right(a.description, 23)), position('LKP' in TRIM(right(a.description, 23))))) not in "
                + "(	"
                + "	select dla_no "
                + "	from ins_policy where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y' ";

        if (getReceiptDateFrom() != null) {
            sql = sql + "	and date_trunc('day',approved_date) >= '" + receiptDateFrom + "'";
            //sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            //sqa.addPar(receiptDateFrom);
        }

        if (getReceiptDateTo() != null) {
            sql = sql + "	and date_trunc('day',approved_date) <= '" + receiptDateTo + "'";
            //sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            //sqa.addPar(receiptDateTo);
        }
        sql = sql + " ) ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_JURNAL_WITH_PROD_KLAIM() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_klaim_double");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("NOMOR POLIS");
            row0.createCell(1).setCellValue("NOMOR LKP");
            row0.createCell(2).setCellValue("TANGGAL JURNAL");
            row0.createCell(3).setCellValue("UTANG KLAIM NETTO");
            row0.createCell(4).setCellValue("TANGGAL APPROVED");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("no_lkp"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            if (h.getFieldValueByFieldNameDT("tgl_approved") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("tgl_approved"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void EXCEL_PRODUKSI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code as koda,a.cc_code_source as koda_nonaks,a.policy_date as tglpolis,a.pol_id,';'||a.pol_no as pol_no,a.cust_name as tertanggung,a.prod_name as marketer,a.ccy,a.ccy_rate,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,(a.premi_total*a.ccy_rate) as premi_total,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (29,22,36,15) and a.pol_id = x.pol_id),0) as nd_pcost,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (28,21,35,14) and a.pol_id = x.pol_id),0) as nd_sfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and a.pol_id = x.pol_id),0) as nd_comm1,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (1) and a.pol_id = x.pol_id),0) as nd_taxcomm21,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (2) and a.pol_id = x.pol_id),0) as nd_taxcomm23,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (4)and a.pol_id = x.pol_id),0) as nd_taxbrok21,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (5,6)and a.pol_id = x.pol_id),0) as nd_taxbrok23,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and x.tax_code in (9)and a.pol_id = x.pol_id),0) as nd_taxhfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (57,58,59,71,76,78,85,86,87,91,101) and a.pol_id = x.pol_id),0) as nd_ppn,"
                + "e.vs_description as company_type,g.vs_description as company_grup,f.vs_description as sumbis ");

        sqa.addQuery(" from ins_policy a "
                + "left join ent_master d on d.ent_id = a.entity_id "
                + "left join s_valueset e on e.vs_code = d.ref1 and e.vs_group = 'COMP_TYPE' "
                + "left join s_valueset f on f.vs_code = d.category1 and f.vs_group = 'ASK_BUS_SOURCE' "
                + "left join s_company_group g on g.vs_code::text = d.ref2 ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag = 'Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            //sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
            //sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(stBranch);

                if (stBranchSource != null) {
                    sqa.addClause("a.cc_code_source = ?");
                    sqa.addPar(stBranchSource);
                }
                if (stRegionSource != null) {
                    sqa.addClause("a.region_id_source = ?");
                    sqa.addPar(stRegionSource);
                }
            } else {
                sqa.addClause("((a.cc_code = ?) or (a.cc_code = '80' and a.cc_code_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stBranch);
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("a.cc_code = '80'");
//            sqa.addPar(stBussinessPolType);
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("a.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,38,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,97)");
            } else {
                sqa.addClause("a.pol_type_id in (21,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80)");
            }
        }

//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            //sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("a.region_id = '" + stRegion + "'");
//            //sqa.addPar(stRegion);
//        }

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

        String sql = sqa.getSQL();

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

        sql = "select a.koda,a.koda_nonaks,a.tglpolis,a.pol_id,a.pol_no,';'||c.ref_ent_id as kodeko,a.tertanggung,a.marketer,a.ccy,a.ccy_rate as kurs,"
                + "getpremiend(b.entity_id,coalesce(a.insured_amount,0),coalesce(b.amount*a.ccy_rate,0)*-1) as tsi, "
                + "getpremiend(b.entity_id,coalesce(a.premi_total,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1) as premi_bruto, "
                + "getpremiend(b.entity_id,coalesce(a.nd_pcost,0),0) as biapol, "
                + "getpremiend(b.entity_id,coalesce(a.nd_sfee,0),0) as biamat, "
                + "getpremiend(b.entity_id,coalesce(a.nd_ppn,0),0) as ppn, "
                + "getpremiend(b.entity_id,coalesce(a.nd_disc1,0),(coalesce(b.disc_amount*a.ccy_rate,0))*-1) as diskon, "
                + "getpremiend(b.entity_id,coalesce(a.nd_hfee,0),coalesce(b.hfee_amount*a.ccy_rate,0)*-1) as hfee, "
                + "getpremiend(b.entity_id,coalesce(a.nd_taxhfee,0),0) as pajak_hfee, "
                + "getpremiend(b.entity_id,coalesce(a.nd_comm1,0),(coalesce(b.comm_amount*a.ccy_rate,0))*-1) as comm1, "
                + "getpremiend(b.entity_id,coalesce(a.nd_taxcomm21,0),0) as pajak_comm21, "
                + "getpremiend(b.entity_id,coalesce(a.nd_taxcomm23,0),0) as pajak_comm23, "
                + "getpremiend(b.entity_id,coalesce(a.nd_brok1,0),(coalesce(b.broker_amount*a.ccy_rate,0))*-1) as bfee,"
                + "getpremiend(b.entity_id,coalesce(a.nd_taxbrok21,0),0) as pajak_bfee21, "
                + "getpremiend(b.entity_id,coalesce(a.nd_taxbrok23,0),0) as pajak_bfee23, "
                + "getpremiend(b.entity_id,coalesce(a.nd_feebase1,0),0) as feebase,"
                + "a.company_type,a.company_grup,a.sumbis "
                + " from ( "
                + sql + " ) a inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + "left join ent_master c on c.ent_id = b.entity_id "
                + "where (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') "
                + "order by a.koda,a.koda_nonaks,a.tglpolis,a.pol_no,b.entity_id asc ";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_PRODUKSI_" + System.currentTimeMillis() + ".csv";

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
    }

    public DTOList EXCEL_CEK_PAJAK() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,"
                + "sum(getpremi2(d.ar_trx_line_id in (14,17,20,30,33,36,46,49,52),a.amount)) as inv_tax21,"
                + "sum(getpremi2(d.ar_trx_line_id in (15,18,19,31,34,35,37,47,50,51,53,107),a.amount)) as inv_tax23");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is not null ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.invoice_type = 'AP'");
//        sqa.addClause("substr(a.no_surat_hutang,25,7) = '" + DateUtil.getMonth2Digit(policyDateFrom) + "/" + DateUtil.getYear(policyDateFrom) + "'");
        sqa.addClause("a.no_surat_hutang like '%" + DateUtil.getMonth2Digit(policyDateFrom) + "/" + DateUtil.getYear(policyDateFrom) + "'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("substr(a.refid2,0,4) = 'TAX'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        String sql = "select a.cc_code,"
                + "sum(prod_tax21) as prod_tax21,"
                + "sum(inv_tax21) as inv_tax21,"
                + "sum(prod_tax23) as prod_tax23,"
                + "sum(inv_tax23) as inv_tax23 from ( " + sqa.getSQL()
                + "group by 1 order by 1 ) a "
                + "inner join ( select a.cc_code,"
                + "sum(getpremi2(b.ins_item_id in (11,18,25,32,12,19,26,33,88,89,90,13,20,27,34,100,105,106,107,108) and tax_code in (1,4,9),round((b.tax_amount*a.ccy_rate),2))) as prod_tax21,"
                + "sum(getpremi2(b.ins_item_id in (11,18,25,32,12,19,26,33,88,89,90,13,20,27,34,100,105,106,107,108) and tax_code in (2,5,6),round((b.tax_amount*a.ccy_rate),2))) as prod_tax23 "
                + "from ins_policies a "
                + "inner join ins_pol_items b on b.pol_id = a.pol_id "
                + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.active_flag='Y' and a.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            sql = sql + "and date_trunc('day',a.policy_date) >= '" + getPolicyDateFrom() + "'";
        }

        if (getPolicyDateTo() != null) {
            sql = sql + "and date_trunc('day',a.policy_date) <= '" + getPolicyDateTo() + "'";
        }

        sql = sql + " group by 1 order by 1 "
                + " ) b on b.cc_code = a.cc_code "
                + "group by 1 order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PAJAK() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_pajak");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal Polis : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            HSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Cabang");
            row0.createCell(1).setCellValue("Produksi Pajak pph21");
            row0.createCell(2).setCellValue("OS Pajak pph21");
            row0.createCell(3).setCellValue("Produksi Pajak pph23");
            row0.createCell(4).setCellValue("OS Pajak pph23");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("prod_tax21").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("inv_tax21").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("prod_tax23").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("inv_tax23").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_CEK_PREMIRE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String select1 = "select k.short_name as cust_name,k.ent_id,"
                + "sum(round(i.premi_amount,2)*a.ccy_rate) as premi,"
                + "sum(round(i.ricomm_amt,2)*a.ccy_rate) as nd_comm "
                + "from ins_policy a "
                + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id and (i.premi_amount <> 0 or i.ricomm_amt <> 0) "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ent_master k on k.ent_id = i.member_ent_id "
                + "where a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                + "and j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') "
                + "and a.active_flag='Y' and a.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            select1 = select1 + "and date_trunc('day',a.policy_date) >= '" + getPolicyDateFrom() + "'";
        }

        if (getPolicyDateTo() != null) {
            select1 = select1 + "and date_trunc('day',a.policy_date) <= '" + getPolicyDateTo() + "'";
        }
        select1 = select1 + "group by 1,2 order by 1 ";

        String select2 = "select a.ref_ent_id,c.short_name,sum(round(a.debit,2)-round(a.credit,2)) as balance "
                + "from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid and substr(b.accountno,1,3) in ('633','634') and b.acctype is null "
                + "inner join ent_master c on c.ent_id = a.ref_ent_id "
                + "inner join ins_policy d on d.pol_no = a.pol_no and d.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                + "where a.fiscal_year = '" + DateUtil.getYear(getPolicyDateTo()) + "' and a.period_no = " + DateUtil.getMonthDigit(getPolicyDateTo()) + " ";

        if (getPolicyDateFrom() != null) {
            select2 = select2 + "and date_trunc('day',d.policy_date) >= '" + getPolicyDateFrom() + "'";
        }

        if (getPolicyDateTo() != null) {
            select2 = select2 + "and date_trunc('day',d.policy_date) <= '" + getPolicyDateTo() + "'";
        }
        select2 = select2 + "group by 1,2 order by 1 ";

        sqa.addSelect(" a.cust_name,sum(round(a.premi,2)) as premire,sum(round(b.balance,2)) as jurnalre ");

        sqa.addQuery(" from (" + select1 + ") a "
                + "inner join (" + select2 + ") b on b.ref_ent_id = a.ent_id ");

        sqa.addClause("(a.premi <> 0 or a.nd_comm <> 0)");

        String sql = "select * from ( " + sqa.getSQL()
                + "group by 1 order by 1 "
                + ") a where premire <> jurnalre ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CEK_PREMIRE() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("cek_premire");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal Polis : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            HSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Reasuradur");
            row0.createCell(1).setCellValue("PremiRe");
            row0.createCell(2).setCellValue("JurnalRe");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premire").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jurnalre").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_PRODUKSI_REAS_OLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" m.description as cabang,a.period_start,a.period_end,a.policy_date,a.status,quote_ident(pol_no) as pol_no,"
                + "a.entity_id,trim(a.cust_name) as cust_name,trim(e.ent_name) as tertanggung,e.ref2 as grupsumbis,"
                + "a.prod_id,trim(a.prod_name) as prod_name,trim(f.ent_name) as marketer,f.ref2 as grupmarketer,"
                + "n.vs_description as company_type,p.vs_description as company_grup,o.vs_description as sumbis,"
                + "a.ccy_rate,a.pol_id,b.ins_pol_obj_id,(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as debitur,"
                + "b.ref1,c.vs_description as coverage,b.ref2,a.kreasi_type_desc,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.ref9d,null)) as kode_pos,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),d.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,a.period_start))) as refd2,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,a.period_end))) as refd3, "
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),k.ins_risk_cat_code,null)) as ins_risk_cat_code,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,"
                + "a.cover_type_code,a.ccy, NULLIF(a.premi_total_adisc*ccy_rate,0) as premi_total2, NULLIF(a.premi_total*ccy_rate,0) as premi_total,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,"
                + "( select sum(y.insured_amount * a.ccy_rate) from ins_pol_tsi y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as tsi_obj,"
                + "( select sum(y.premi_new * a.ccy_rate) from ins_pol_cover y "
                + "where y.ins_pol_obj_id = b.ins_pol_obj_id) as premi_obj,"
                + "b.premi_total_bcoins as premi_total_e,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (29,22,36,15) and a.pol_id = x.pol_id),0) as nd_pcost,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (28,21,35,14) and a.pol_id = x.pol_id),0) as nd_sfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as nd_disc1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (25,18,32,11) and a.pol_id = x.pol_id),0) as nd_comm1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as nd_brok1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as nd_hfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as nd_feebase1,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x "
                + "where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as nd_ppn,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (1) and a.pol_id = x.pol_id),0) as nd_taxcomm21,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (2) and a.pol_id = x.pol_id),0) as nd_taxcomm23,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (4)and a.pol_id = x.pol_id),0) as nd_taxbrok21,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (5,6)and a.pol_id = x.pol_id),0) as nd_taxbrok23,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and x.tax_code in (9)and a.pol_id = x.pol_id),0) as nd_taxhfee,"
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
                + " a.endorse_notes ");

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
                + "left join gl_cost_center m on m.cc_code = a.cc_code "
                + "left join s_valueset n on n.vs_code = e.ref1 and n.vs_group = 'COMP_TYPE' "
                + "left join s_valueset o on o.vs_code = e.category1 and o.vs_group = 'ASK_BUS_SOURCE' "
                + "left join s_company_group p on p.vs_code::text = e.ref2 ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
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

        String sql = "select a.cabang,a.period_start,a.period_end,a.entity_id,a.cust_name,a.tertanggung,a.grupsumbis,a.prod_id,a.prod_name,a.marketer,a.grupmarketer,"
                + "a.company_type,a.company_grup,a.sumbis,a.policy_date as tglpolis,a.pol_id,';'||a.pol_no as polno,a.ccy,a.ccy_rate as kurs,"
                + "sum(tsi_obj-tsiko_obj) as tsi_obj,sum(premi_obj-premiko_obj) as premi_obj,"
                + "sum(biapol_obj) as biapol_obj,sum(biamat_obj) as biamat_obj,sum(ppn_obj) as ppn_obj,sum(disc_obj-diskonko_obj) as disc_obj,"
                + "sum(hfee_obj-hfeeko_obj) as hfee_obj,sum(taxhfee_obj) as taxhfee_obj,"
                + "sum(komisi_obj-commko_obj) as komisi_obj,sum(taxcomm21_obj) as taxkomisi21_obj,sum(taxcomm23_obj) as taxkomisi23_obj,"
                + "sum(bfee_obj-bfeeko_obj) as bfee_obj,sum(taxbrok21_obj) as taxbfee21_obj,sum(taxbrok23_obj) as taxbfee23_obj,sum(fbase_obj) as fbase_obj,"
                + "sum(tsi_or) as tsi_or,sum(premi_or) as premi_or,sum(komisi_or) as komisi_or,"
                + "sum(tsi_bppdan) as tsi_bppdan,sum(premi_bppdan) as premi_bppdan,sum(komisi_bppdan) as komisi_bppdan,"
                + "sum(tsi_kscbi) as tsi_kscbi,sum(premi_kscbi) as premi_kscbi,sum(komisi_kscbi) as komisi_kscbi,"
                + "sum(tsi_spl) as tsi_spl,sum(premi_spl) as premi_spl,sum(komisi_spl) as komisi_spl,"
                + "sum(tsi_fac) as tsi_fac,sum(premi_fac) as premi_fac,sum(komisi_fac) as komisi_fac,"
                + "sum(tsi_qs) as tsi_qs,sum(premi_qs) as premi_qs,sum(komisi_qs) as komisi_qs,"
                + "sum(tsi_park) as tsi_park,sum(premi_park) as premi_park,sum(komisi_park) as komisi_park,"
                + "sum(tsi_faco) as tsi_faco,sum(premi_faco) as premi_faco,sum(komisi_faco) as komisi_faco,"
                + "sum(tsi_faco1) as tsi_faco1,sum(premi_faco1) as premi_faco1,sum(komisi_faco1) as komisi_faco1,"
                + "sum(tsi_faco2) as tsi_faco2,sum(premi_faco2) as premi_faco2,sum(komisi_faco2) as komisi_faco2,"
                + "sum(tsi_faco3) as tsi_faco3,sum(premi_faco3) as premi_faco3,sum(komisi_faco3) as komisi_faco3,"
                + "sum(tsi_jp) as tsi_jp,sum(premi_jp) as premi_jp,sum(komisi_jp) as komisi_jp "
                + "from ( select cabang,period_start,period_end,policy_date,status,pol_no,a.entity_id,a.cust_name,a.tertanggung,a.grupsumbis,"
                + "a.prod_id,a.prod_name,a.marketer,a.grupmarketer,a.company_type,a.company_grup,a.sumbis,"
                + "ccy_rate,pol_id,ins_pol_obj_id,ref1,coverage,kreasi_type_desc,ref2,kode_pos,kriteria,risk_class,"
                + "tgl_lahir,refd2,refd3,ins_risk_cat_code,ccy,getpremi(status = 'ENDORSE',round(insured_amount/debitur,2),tsi_obj) as tsi_obj,"
                + "premi_obj,round(tsiko/debitur,2) as tsiko_obj,round(premiko/debitur,2) as premiko_obj, round(diskonko/debitur,2) as diskonko_obj,"
                + "round(commko/debitur,2) as commko_obj, round(bfeeko/debitur,2) as bfeeko_obj, round(hfeeko/debitur,2) as hfeeko_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_pcost/debitur,2),round((premi_obj/premi_total)*nd_pcost,2)) as biapol_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_sfee/debitur,2),round((premi_obj/premi_total)*nd_sfee,2)) as biamat_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_disc1/debitur,2),round((premi_obj/premi_total)*nd_disc1,2)) as disc_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_comm1/debitur,2),round((premi_obj/premi_total)*nd_comm1,2)) as komisi_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_brok1/debitur,2),round((premi_obj/premi_total)*nd_brok1,2)) as bfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_hfee/debitur,2),round((premi_obj/premi_total)*nd_hfee,2)) as hfee_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_feebase1/debitur,2) ,round((premi_obj/premi_total)*nd_feebase1,2)) as fbase_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_ppn/debitur,2),round((premi_obj/premi_total)*nd_ppn,2)) as ppn_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_taxcomm21/debitur,2),round((premi_obj/premi_total)*nd_taxcomm21,2)) as taxcomm21_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_taxcomm23/debitur,2),round((premi_obj/premi_total)*nd_taxcomm23,2)) as taxcomm23_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_taxbrok21/debitur,2),round((premi_obj/premi_total)*nd_taxbrok21,2)) as taxbrok21_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_taxbrok23/debitur,2),round((premi_obj/premi_total)*nd_taxbrok23,2)) as taxbrok23_obj,"
                + "getpremi(status in ('ENDORSE'),round(nd_taxhfee/debitur,2),round((premi_obj/premi_total)*nd_taxhfee,2)) as taxhfee_obj,"
                + "building,machine,stock,other,tsi_or,premi_or ,komisi_or,tsi_bppdan,premi_bppdan,komisi_bppdan,tsi_kscbi,premi_kscbi,komisi_kscbi,"
                + "tsi_spl,premi_spl,komisi_spl,tsi_fac,premi_fac,komisi_fac, tsi_qs,premi_qs ,komisi_qs,tsi_park,premi_park,komisi_park,"
                + "tsi_faco,premi_faco,komisi_faco, tsi_faco1,premi_faco1 ,komisi_faco1,tsi_faco2,premi_faco2,komisi_faco2,tsi_faco3,premi_faco3,komisi_faco3,"
                + "tsi_jp,premi_jp,komisi_jp,cover_type_code,total_komisi_pct,desc1,endorse_notes from ( " + sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,"
                + "b.ins_pol_obj_id,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10,b.refd1,b.refd2,c.vs_description,"
                + "b.refd3,b.refd4,b.refd5,b.refd6,a.cover_type_code,a.share_pct,a.ccy,a.insured_amount,a.premi_total,"
                + "b.insured_amount,b.premi_total,b.premi_total_bcoins,e.ref2,f.ref2,f.ent_name,e.ent_name,d.vs_description,k.ins_risk_cat_code,"
                + "l.vs_description,m.description,n.vs_description,p.vs_description,o.vs_description  order by a.pol_no,b.ins_pol_obj_id ) a "
                + "order by a.pol_no,a.ins_pol_obj_id "
                + ") a group by a.cabang,a.period_start,a.period_end,a.entity_id,a.cust_name,a.tertanggung,a.grupsumbis,"
                + "a.prod_id,a.prod_name,a.marketer,a.grupmarketer,a.company_type,a.company_grup,a.sumbis,a.policy_date,a.pol_id,a.pol_no,a.ccy,a.ccy_rate order by a.pol_no ";

//        SQLUtil S = new SQLUtil();
//
//        String nama_file = "EXCEL_PRODUKSIRE_" + System.currentTimeMillis() + ".csv";
//
//        sql = "Copy ("
//                + sql
//                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";
//
//        final PreparedStatement ps = S.setQuery(sql);
//
//        boolean tes = ps.execute();
//
//        S.release();
//
//        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
//        int length = 0;
//        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();
//
//        SessionManager.getInstance().getResponse().setContentType("text/csv");
//        SessionManager.getInstance().getResponse().setContentLength((int) file.length());
//
//        // sets HTTP header
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");
//
//        int BUFSIZE = 4096;
//        byte[] byteBuffer = new byte[BUFSIZE];
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//
//        // reads the file's bytes and writes them to the response stream
//        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
//            outStream.write(byteBuffer, 0, length);
//        }
//
//        in.close();
//        outStream.close();
//
//        file.delete();

        String nama_file = "produksi_ri" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();
        
        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        //COPY POLIS KE DIRECTORY
////        String INPUT_FILE = "D:\\exportdb\\statistik\\" + nama_file;//utk core 250.52
////        String INPUT_FILE = "T:\\statistik\\" + nama_file;//utk local
//        String INPUT_FILE = "P:\\accounting\\" + nama_file;//utk core 250.53
//        String COPY_FILE_TO = "W:\\accounting\\" + nama_file;
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

    public DTOList EXCEL_PRODUKSI_REAS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" m.description as cabang,a.period_start,a.period_end,a.entity_id,"
                + "trim(a.cust_name) as cust_name,trim(e.ent_name) as tertanggung,"
                + "e.ref2 as grupsumbis,a.prod_id,trim(a.prod_name) as prod_name,"
                + "trim(f.ent_name) as marketer,f.ref2 as grupmarketer,"
                + "n.vs_description as company_type,p.vs_description as company_grup,o.vs_description as sumbis,"
                + "a.status,a.pol_type_id,a.policy_date,a.pol_id,quote_ident(a.pol_no) pol_no,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as tsi_obj,(a.premi_total*a.ccy_rate) as premi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (29,22,36,15) and a.pol_id = x.pol_id),0) as biapol_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (28,21,35,14) and a.pol_id = x.pol_id),0) as biamat_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as disc_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and a.pol_id = x.pol_id),0) as komisi_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as bfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as hfee_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as fbase_obj,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as ppn_obj,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (1) and a.pol_id = x.pol_id),0) as taxcomm21_obj,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11) and x.tax_code in (2) and a.pol_id = x.pol_id),0) as taxcomm23_obj,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (4)and a.pol_id = x.pol_id),0) as taxbfee21_obj,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (5,6)and a.pol_id = x.pol_id),0) as taxbfee23_obj,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and x.tax_code in (9)and a.pol_id = x.pol_id),0) as taxhfee_obj,"
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
                + " round(sum(checkreas(j.treaty_type='JP',i.ricomm_amt * a.ccy_rate)),2) as komisi_jp,"
                + " round(sum(checkreas(j.treaty_type='FACP',i.tsi_amount * a.ccy_rate)),2) as tsi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.premi_amount * a.ccy_rate)),2) as premi_facp, "
                + " round(sum(checkreas(j.treaty_type='FACP',i.ricomm_amt * a.ccy_rate)),2) as komisi_facp,"
                + " round(sum(checkreas(j.treaty_type='QSKR',i.tsi_amount * a.ccy_rate)),2) as tsi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.premi_amount * a.ccy_rate)),2) as premi_qskr, "
                + " round(sum(checkreas(j.treaty_type='QSKR',i.ricomm_amt * a.ccy_rate)),2) as komisi_qskr  ");

        sqa.addQuery(" from ins_policy a "
                + "left join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = i.ins_treaty_detail_id "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id  "
                + "left join gl_cost_center m on m.cc_code = a.cc_code "
                + "left join s_valueset n on n.vs_code = e.ref1 and n.vs_group = 'COMP_TYPE' "
                + "left join s_valueset o on o.vs_code = e.category1 and o.vs_group = 'ASK_BUS_SOURCE' "
                + "left join s_company_group p on p.vs_code::text = e.ref2 ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause(" a.active_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
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

        String sql = sqa.getSQL()
                + " group by a.period_start,a.period_end,a.policy_date,a.status,a.pol_id,"
                + "a.pol_no,a.entity_id,a.cust_name,a.ccy_rate,a.ccy,a.insured_amount,"
                + "a.premi_total,m.description,n.vs_description,p.vs_description,"
                + "o.vs_description,e.ent_name,e.ref2,f.ent_name,f.ref2 order by a.pol_no ";

        String nama_file = "produksi_ri" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        SQLUtil S = new SQLUtil();

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
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
}

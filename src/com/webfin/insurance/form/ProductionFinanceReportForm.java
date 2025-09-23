/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionFinanceReportForm
 * Author:  Denny Mahendra
 * Created: Aug 15, 2006 11:17:20 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.parameter.Parameter;
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
import com.crux.util.BDUtil;
import com.crux.util.DateUtil;
import com.crux.util.FTPUploadFile;
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
import com.crux.valueset.model.ValueSetView;
import com.webfin.entity.model.EntityView;
import com.webfin.insurance.model.BiayaOperasionalDetail;
import com.webfin.insurance.model.BiayaOperasionalGroup;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.SessionContext;

public class ProductionFinanceReportForm extends Form {

    private String stPrintForm;
    private String stFontSize;
    private String stLang;
    private boolean enableRiskFilter;
    private boolean enableSelectForm = true;
    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String stReportTitle;
    private String stReport;
    private String stReportDesc;
    private String stReportType;
    private String stReportTypeOfFile;
    private String stFileName;
    public SessionContext ctx;
    private DTOList list;
    private Date expirePeriodFrom;
    private Date expirePeriodTo;
    private Date periodFrom;
    private Date periodTo;
    private Date periodEndFrom;
    private Date periodEndTo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date entryDateFrom;
    private Date entryDateTo;
    private Date claimDateFrom;
    private Date claimDateTo;
    private Date appDateFrom;
    private Date appDateTo;
    private Date PLADateFrom;
    private Date PLADateTo;
    private Date DLADateFrom;
    private Date DLADateTo;
    private Date paymentDateFrom;
    private Date paymentDateTo;
    private Date perDateFrom;
    private Date createDateFrom;
    private Date createDateTo;
    private Date paymentPremiDateFrom;
    private Date paymentPremiDateTo;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeGroupDesc;
    private String stPolicyTypeID;
    private String stPolicyTypeDesc;
    private String stInsCompanyID;
    private String stInsCompanyName;
    private String stEntityID;
    private String stEntityName;
    private String stFltCoverType;
    private String stFltTreatyType;
    private String stFltTreatyTypeDesc;
    private String stFltCoverTypeDesc;
    private String stFltClaimStatus;
    private String stFltClaimStatusDesc;
    private String stPolicyNo;
    private String stRiskLocation;
    private String stPostCode;
    private String stRiskCardNo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stRiskCode;
    private String stCustCategory1Desc;
    private String stMarketerID;
    private String stMarketerName;
    private String stCompanyID;
    private String stCompanyName;
    private String stReceiptNo;
    private String stTax;
    private String stTaxDesc;
    private String stCreateID;
    private String stCreateName;
    private String stZoneCode;
    private String stKreasiName;
    private String stReinsID;
    private String stReinsName;
    private String stCustCategory1;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stDlaNo;
    private String stTriwulan;
    private String stMarketerOffID;
    private String stMarketerOffName;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private String stRecapFlag;
    private String stPeriodMonth;
    private String stPeriodMonthDesc;
    private String year;
    private String stTime;
    private String stPolCredit;
    private String stCoinsurerID;
    private String stCoinsurerName;
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;
    private String stBranchSource;
    private String stBranchSourceDesc;
    private String stRegionSource;
    private String stRegionSourceDesc;
    private final static transient LogManager logger = LogManager.getInstance(ProductionFinanceReportForm.class);

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

    public String getStFltCoverType() {
        return stFltCoverType;
    }

    public void setStFltCoverType(String stFltCoverType) {
        this.stFltCoverType = stFltCoverType;
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");

        if (stReportType.toUpperCase().equalsIgnoreCase("HO_FINANCE")) {
            setPolicyDateFrom(DateUtil.getDate("01/01/2013"));
        }
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

        plist.add(stReport + "_" + stRecapFlag);

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

    public DTOList OUTSTANDINGPREMITEMP() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "b.cc_code,b.pol_id,b.policy_date,b.pol_no,b.ref6,b.cust_name,b.ccy_rate,"
                + "sum(getpremi2(d.category = 'PREMIG', c.amount)) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(getpremi2(d.category = 'COMMISSION', c.amount)) as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = b.entity_id ");

        sqa.addClause("b.status IN ('POLICY','RENEWAL')");
        sqa.addClause("b.effective_flag = 'Y'");
        sqa.addClause("a.invoice_type = 'AR'");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");
        sqa.addClause("b.ref6 is not null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }
        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = sqa.getSQL() + " group by b.cc_code,b.pol_id,b.policy_date,b.ref6,b.pol_no,b.cust_name,b.ccy_rate,a.ar_invoice_id "
                + " order by b.cc_code,b.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList OUTSTANDINGPREMIINTERN() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.cc_code,e.ent_id::text,e.ent_name,(substr(a.refid1,6,1)::numeric+1) as sppa_no, "
                + "a.attr_pol_id as pol_id,a.invoice_no as dla_no,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,"
                + "a.mutation_date as policy_date,a.due_date as claim_date,a.receipt_date,a.ccy_rate,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as entity_id,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");

        String query = " substr(a.attr_pol_no,1,16)||a.ar_trx_type_id in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16)||a.ar_trx_type_id as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }
        /*NON AKS*/
//        if (getStBranch() != null) {
//            query = query + " and a.cc_code = '" + stBranch + "'";
//            //sqa.addPar(stBranch);
//        }
//
//        if (getStRegion() != null) {
//            query = query + " and b.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                query = query + " and b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    query = query + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    query = query + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                query = query + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                query = query + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                query = query + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        query = query + " group by substr(a.attr_pol_no,1,16),a.ar_trx_type_id ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        /*NON AKS*/
//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = ? ");
//            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("b.region_id = ? ");
//            sqa.addPar(stRegion);
//        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
//            sqa.addPar(stBussinessPolType);
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("a.ent_id = ? ");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = ? ");
            sqa.addPar(stMarketerID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ? ");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("f.pol_type_id = ? ");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ? ");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ? ");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ? ");
            sqa.addPar(stCustCategory1);
        }

        String sql = "select * from ( " + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.invoice_no,a.attr_pol_id,substr(a.refid1,6,1), "
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0) "
                + " order by a.cc_code,a.pol_no,a.sppa_no,a.entity_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList OUTSTANDINGCOMM() throws Exception {
        final boolean FLT_OUTS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_COMM"));
        final boolean FLT_OUTS_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_TAX"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "b.cc_code,b.pol_id,b.policy_date,b.pol_no,b.cust_name,b.ccy_rate,"
                + "round(b.premi_total,0) as premi_total,round(coalesce(b.nd_pcost,0),0) as nd_pcost,round(coalesce(b.nd_sfee,0),0) as nd_sfee,"
                + "(round(coalesce(b.nd_disc1,0),0) + round(coalesce(b.nd_disc2,0),0)) as nd_disc1,"
                + "(round(coalesce(b.nd_comm1,0),0) + round(coalesce(b.nd_comm2,0),0) + round(coalesce(b.nd_comm3,0),0) + round(coalesce(b.nd_comm4,0),0)) as nd_comm1,"
                + "(round(coalesce(b.nd_brok1,0),0) + round(coalesce(b.nd_brok2,0),0)) as nd_brok1,"
                + "(round(coalesce(b.nd_hfee,0),0)) as nd_hfee,"
                + "(round(coalesce(b.nd_taxcomm1,0),0) + round(coalesce(b.nd_taxcomm2,0),0) + round(coalesce(b.nd_taxcomm3,0),0) + round(coalesce(b.nd_taxcomm4,0),0)) as nd_taxcomm1,"
                + "(round(coalesce(b.nd_taxbrok1,0),0) + round(coalesce(b.nd_taxbrok2,0),0)) as nd_taxbrok1,"
                + "(round(coalesce(b.nd_taxhfee,0),0)) as nd_taxhfee,"
                + "round(b.premi_netto,0) as premi_netto,round(b.total_due,0) as total_due,round(coalesce(a.amount_settled,0),0) as ap_comis_p");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.pol_id ");

        sqa.addClause("b.status IN ('POLICY','RENEWAL')");
        sqa.addClause("b.effective_flag = 'Y'");
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        if (FLT_OUTS_COMM) {
            sqa.addClause("substr(a.refid2,0,4) = 'POL'");
        }

        if (FLT_OUTS_TAX) {
            sqa.addClause("substr(a.refid2,0,4) = 'TAX'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',b.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }
        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no like ? ");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
//            sqa.addPar(stBussinessPolType);
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " order by b.cc_code,b.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_LAPKEU() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.pol_id,a.pol_no,substr(a.policy_date::text,6,2) as month,a.cc_code,c.description as cabang,d.description as jenis,a.pol_type_id, "
                + "sum(getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1)) as premi_total,  "
                /*+ "sum(getpremiend(b.entity_id,coalesce(a.nd_pcost*a.ccy_rate,0),0)) as nd_pcost,  "
                + "sum(getpremiend(b.entity_id,coalesce(a.nd_sfee*a.ccy_rate,0),0)) as nd_sfee,  "
                + "sum(getpremiend(b.entity_id,((coalesce(a.nd_disc1*a.ccy_rate,0)) + (coalesce(a.nd_disc2*a.ccy_rate,0))),(coalesce(b.disc_amount*a.ccy_rate,0))*-1)) as nd_disc1, "
                + "sum(getpremiend(b.entity_id,((coalesce(a.nd_feebase1*a.ccy_rate,0)) + (coalesce(a.nd_feebase2*a.ccy_rate,0))),0)) as nd_feebase, "
                + "sum(getpremiend(b.entity_id,((coalesce(a.nd_brok1*a.ccy_rate,0)) + (coalesce(a.nd_brok2*a.ccy_rate,0))),(coalesce(b.broker_amount*a.ccy_rate,0))*-1))as nd_brok1, "
                + "sum(getpremiend(b.entity_id,coalesce(a.nd_hfee*a.ccy_rate,0),coalesce(b.hfee_amount*a.ccy_rate,0)*-1)) as nd_hfee,  "*/
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (29,22,36,15) and a.pol_id = x.pol_id) as nd_pcost,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (28,21,35,14) and a.pol_id = x.pol_id) as nd_sfee,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id) as nd_disc1,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id) as nd_feebase,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id) as nd_brok1,"
                + "(select coalesce(sum(x.tax_amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and tax_code in (4)) as tax_bfee21,"
                + "(select coalesce(sum(x.tax_amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and tax_code in (5,6)) as tax_bfee23,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id) as nd_hfee,"
                + "(select coalesce(sum(x.tax_amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (13,20,27,34) and tax_code in (9)) as tax_hfee,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (11,18,25,32) and tax_code in (1)) as comm21,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (11,18,25,32) and tax_code in (2)) as comm23,"
                + "(select coalesce(sum(x.tax_amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (11,18,25,32) and tax_code in (1)) as tax_comm21,"
                + "(select coalesce(sum(x.tax_amount*a.ccy_rate),0) from ins_pol_items x where a.pol_id = x.pol_id and x.ins_item_id in (11,18,25,32) and tax_code in (2)) as tax_comm23,"
                + "(select coalesce(sum(x.amount*a.ccy_rate),0) from ins_pol_items x where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id) as nd_ppn,"
                + "sum(getpremiend(b.entity_id,0,coalesce(b.comm_amount*a.ccy_rate,0))) as commko,"
                + "sum(getpremiend(b.entity_id,0,coalesce(b.disc_amount*a.ccy_rate,0))) as discko,"
                + "sum(getpremiend(b.entity_id,0,coalesce(b.broker_amount*a.ccy_rate,0))) as bfeeko,"
                + "sum(getpremiend(b.entity_id,0,coalesce(b.hfee_amount*a.ccy_rate,0))) as hfeeko ");

        sqa.addQuery(
                " from ins_policy a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join gl_cost_center c on c.cc_code = a.cc_code "
                + " inner join ins_policy_types d on d.pol_type_id = a.pol_type_id "
                + " left join ent_master e on e.ent_id = a.entity_id ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.effective_flag='Y'");
        sqa.addClause("(b.entity_id <> 1 or b.coins_type <> 'COINS_COVER')");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

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

        final String sql = " select month,cc_code as kode,cabang, "
                + "sum(premi_total) as premi,sum(nd_pcost) as nd_pcost,sum(nd_sfee) as nd_sfee,sum(nd_disc1-discko) as nd_disc1,"
                + "sum(nd_feebase) as feebase, sum(nd_brok1-bfeeko) as nd_brok1, sum(nd_hfee-hfeeko) as nd_hfee, sum(comm21-commko) as comm21,"
                + "sum(comm23) as comm23,sum(tax_bfee21) as tax_bfee21,sum(tax_bfee23) as tax_bfee23,sum(tax_hfee) as tax_hfee,"
                + "sum(tax_comm21) as tax_comm21,sum(tax_comm23) as tax_comm23,sum(nd_ppn) as nd_ppn"
                + " from ( " + sqa.getSQL() + " group by a.pol_id,a.pol_no,substr(a.policy_date::text,6,2),a.cc_code,c.description,d.description,a.pol_type_id "
                + " ) x group by month,cc_code,cabang order by month,cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_LAPKEU() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("bulan");
            row0.createCell(1).setCellValue("kode");
            row0.createCell(2).setCellValue("cabang");
            row0.createCell(3).setCellValue("premi");
            row0.createCell(4).setCellValue("biapol");
            row0.createCell(5).setCellValue("biamat");
            row0.createCell(6).setCellValue("diskon");
            row0.createCell(7).setCellValue("feebase");
            row0.createCell(8).setCellValue("bfee");
            row0.createCell(9).setCellValue("hfee");
            row0.createCell(10).setCellValue("komisi 23");
            row0.createCell(11).setCellValue("komisi 21");
            row0.createCell(12).setCellValue("Pajak Bfee 23");
            row0.createCell(13).setCellValue("Pajak Bfee 21");
            row0.createCell(14).setCellValue("Pajak Hfee");
            row0.createCell(15).setCellValue("Pajak Komisi 23");
            row0.createCell(16).setCellValue("Pajak Komisi 21");
            row0.createCell(17).setCellValue("Ppn");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("month"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("kode"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nd_pcost").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_sfee").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("feebase").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_brok1").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_hfee").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("comm23").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm21").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("tax_bfee23").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("tax_bfee21").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("tax_hfee").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("tax_comm23").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("tax_comm21").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("nd_ppn").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList PREMIPAID() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "d.cc_code,coalesce(d.policy_date,c.mutation_date) as policy_date,coalesce(d.cust_name,c.attr_pol_name) as cust_name,d.ccy_rate,coalesce(d.pol_no,c.attr_pol_no) as pol_no,"
                + "getname(f.ar_settlement_id in (1,2,25,33,38,41,44),'00',coalesce(c.kode_ko,g.ref_ent_id)) as bus_source,getpremi2(f.ar_settlement_id in (1,25,38,41,44),substr(c.refid1,6,1)::numeric+1) as inst, "
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PREMIG',round(a.amount,0))) as premi_total,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'STAMPDUTY',round(a.amount,0))) as nd_sfee,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PCOST',round(a.amount,0))) as nd_pcost,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'DISC',round(a.amount,0))) as nd_disc1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_hfee,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_brok1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount,0))) as nd_ppn,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount,0))) as nd_feebase1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_comm1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXCOMM' and a.f_comission = 'Y',round(a.amount,0))) as nd_taxcomm1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXBFEE' and a.f_comission = 'Y',round(a.amount,0))) as nd_taxbrok1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXHFEE' and a.f_comission = 'Y',round(a.amount,0))) as tax_hfee,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 55,round((a.amount*-1),0))) as premi_total_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 70,round((a.amount*-1),0))) as nd_disc1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 71,round((a.amount*-1),0))) as nd_brok1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 72,round((a.amount*-1),0))) as nd_hfee_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 73,round((a.amount*-1),0))) as nd_comm1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 55,round((a.amount_settled*-1),0))) as premi_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 70,round((a.amount_settled*-1),0))) as disc_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 71,round((a.amount_settled*-1),0))) as bfee_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 72,round((a.amount_settled*-1),0))) as hfee_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 73,round((a.amount_settled*-1),0))) as comm_paid_co ");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "left join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = c.ent_id ");

        sqa.addClause("f.ar_settlement_id in (1,2,13,25,33)");
        sqa.addClause("f.status = 'POST'");
        sqa.addClause("f.posted_flag = 'Y'");
        //sqa.addClause("c.ar_trx_type_id in (5,6,7,11,10)");
        //sqa.addClause("b.ar_invoice_dtl_root_id is not null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("d.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = "select cc_code,policy_date,ccy_rate,pol_no,bus_source,cust_name,"
                + "getpremi(bus_source = '00',sum(premi_total),sum(premi_total_co)) as premi_total,"
                + "sum(nd_sfee) as ap_sfee_p,sum(nd_pcost) as ap_pcost_p,sum(nd_feebase1) as nd_feebase1,sum(nd_ppn) as nd_ppn,"
                + "sum(nd_taxcomm1) as nd_taxcomm1,sum(nd_taxbrok1) as nd_taxbrok1,sum(tax_hfee) as tax_hfee,"
                + "getpremi(bus_source = '00',sum(nd_disc1),sum(nd_disc1_co)) as nd_disc1,"
                + "getpremi(bus_source = '00',sum(nd_brok1),sum(nd_brok1_co)) as nd_brok1,"
                + "getpremi(bus_source = '00',sum(nd_hfee),sum(nd_hfee_co)) as nd_hfee,"
                + "getpremi(bus_source = '00',sum(nd_comm1),sum(nd_comm1_co)) as nd_comm1,"
                + "getpremi(bus_source = '00',sum(premi_paid),sum(premi_paid_co)) as premi_paid,sum(ap_sfee_p) as ap_sfee_p,sum(ap_pcost_p) as ap_pcost_p,"
                + "getpremi(bus_source = '00',sum(ap_disc_p),sum(disc_paid_co)) as ap_disc_p,getpremi(bus_source = '00',sum(ap_bfee_p),sum(bfee_paid_co)) as ap_bfee_p,"
                + "getpremi(bus_source = '00',sum(ap_hfee_p),sum(hfee_paid_co)) as ap_hfee_p,getpremi(bus_source = '00',sum(ap_comis_p),sum(comm_paid_co)) as ap_comis_p,"
                + "sum(ap_fbase_p) as ap_fbase_p,sum(ap_ppn_p) as ap_ppn_p,sum(ap_tax_p) as ap_tax_p from ( "
                + sqa.getSQL()
                + " group by d.cc_code,d.policy_date,c.mutation_date,d.cust_name,c.attr_pol_name,d.ccy_rate,d.pol_no,c.attr_pol_no,f.ar_settlement_id,c.kode_ko,g.ref_ent_id,c.refid1 "
                + " order by d.cc_code,d.pol_no "
                + " ) a where (premi_paid <> 0 or ap_sfee_p <> 0 or ap_pcost_p <> 0 or premi_paid_co <> 0 or ap_hfee_p <> 0 or ap_bfee_p <> 0 or ap_comis_p <> 0 or ap_fbase_p <> 0 or ap_ppn_p <> 0 or ap_disc_p <> 0) "
                + "group by cc_code,policy_date,ccy_rate,pol_no,inst,bus_source,cust_name "
                + "order by a.cc_code,a.pol_no,bus_source ";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_PREMIPAID() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "c.cc_code,coalesce(d.policy_date,c.mutation_date) as policy_date,coalesce(d.cust_name,c.attr_pol_name) as cust_name,c.ccy_rate,';'||coalesce(d.pol_no,c.attr_pol_no) as pol_no,"
                //+ "coalesce(c.kode_ko,coalesce(g.ref_ent_id,'00')) as kodeko,"
                + "getname(f.ar_settlement_id in (1,2,25,33,38,41,44),'00',coalesce(c.kode_ko,g.ref_ent_id)) as kodeko,getpremi2(f.ar_settlement_id in (1,25,38,41,44),substr(c.refid1,6,1)::numeric+1) as inst, "
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PREMIG',round(a.amount,0))) as premi_total,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'STAMPDUTY',round(a.amount,0))) as nd_sfee,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PCOST',round(a.amount,0))) as nd_pcost,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'DISC',round(a.amount,0))) as nd_disc1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_hfee,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_brok1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount,0))) as nd_ppn,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount,0))) as nd_feebase1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount,0))) as nd_comm1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXCOMM' and a.f_comission = 'Y',round(a.amount,0))) as nd_taxcomm1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXBFEE' and a.f_comission = 'Y',round(a.amount,0))) as nd_taxbrok1,"
                + "sum(getpremi2(f.ar_settlement_id in (1,25,38,41,44) and e.category = 'TAXHFEE' and a.f_comission = 'Y',round(a.amount,0))) as tax_hfee,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 55,round((a.amount*-1),0))) as premi_total_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 70,round((a.amount*-1),0))) as nd_disc1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 71,round((a.amount*-1),0))) as nd_brok1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 72,round((a.amount*-1),0))) as nd_hfee_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 73,round((a.amount*-1),0))) as nd_comm1_co,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,44) and e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(f.ar_settlement_id in (1,2,25,33,38,41,8) and e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 55,round((a.amount_settled*-1),0))) as premi_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 70,round((a.amount_settled*-1),0))) as disc_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 71,round((a.amount_settled*-1),0))) as bfee_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 72,round((a.amount_settled*-1),0))) as hfee_paid_co,"
                + "sum(getpremi2(f.ar_settlement_id in (13) and e.ar_trx_line_id = 73,round((a.amount_settled*-1),0))) as comm_paid_co ");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "left join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = c.ent_id ");

        sqa.addClause("f.ar_settlement_id in (1,2,8,13,25,33)");
        sqa.addClause("f.status = 'POST'");
        sqa.addClause("f.posted_flag = 'Y'");
        //sqa.addClause("c.ar_trx_type_id in (5,6,7,11,10)");
        //sqa.addClause("b.ar_invoice_dtl_root_id is not null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= '" + paymentDateFrom + "'");
            //sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= '" + paymentDateTo + "'");
            //sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("d.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("d.pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("g.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        String sql = " select koda,tglpolis,pol_no,inst,kodeko,cust_name, "
                + "premi,biamat,biapol,feebase,ppn,diskon,bfee,hfee,komisi,tax_comm,tax_bfee,tax_hfee,"
                + "((premi+(biamat+biapol))-(((feebase+ppn)+(diskon+bfee))+(hfee+komisi))) as premi_netto,"
                + "premi_dibayar,diskon_dibayar,feebase_dibayar,ppn_dibayar,komisi_dibayar,bfee_dibayar,"
                + "hfee_dibayar,pajak_dibayar from ("
                + "select cc_code as koda,policy_date as tglpolis,pol_no,inst,';'||kodeko as kodeko,cust_name, "
                + "getpremi(kodeko = '00',sum(premi_total),sum(premi_total_co)) as premi,"
                + "sum(nd_sfee) as biamat,sum(nd_pcost) as biapol,sum(nd_feebase1) as feebase,sum(nd_ppn) as ppn,"
                + "getpremi(kodeko = '00',sum(nd_disc1),sum(nd_disc1_co)) as diskon,"
                + "getpremi(kodeko = '00',sum(nd_brok1),sum(nd_brok1_co)) as bfee,"
                + "getpremi(kodeko = '00',sum(nd_hfee),sum(nd_hfee_co)) as hfee,"
                + "getpremi(kodeko = '00',sum(nd_comm1),sum(nd_comm1_co)) as komisi,"
                + "sum(nd_taxcomm1) as tax_comm,sum(nd_taxbrok1) as tax_bfee,sum(tax_hfee) as tax_hfee,"
                + "getpremi(kodeko = '00',sum((premi_paid+ap_pcost_p)+ap_sfee_p),sum(premi_paid_co)) as premi_dibayar,"
                + "getpremi(kodeko = '00',sum(ap_disc_p),sum(disc_paid_co)) as diskon_dibayar,"
                + "sum(ap_fbase_p) as feebase_dibayar,sum(ap_ppn_p) as ppn_dibayar,"
                + "getpremi(kodeko = '00',sum(ap_comis_p),sum(comm_paid_co)) as komisi_dibayar,"
                //+ "sum(ap_pcost_p) as biapol_dibayar,sum(ap_sfee_p) as biamat_dibayar,
                + "getpremi(kodeko = '00',sum(ap_hfee_p),sum(hfee_paid_co)) as hfee_dibayar,getpremi(kodeko = '00',sum(ap_bfee_p),sum(bfee_paid_co)) as bfee_dibayar,sum(ap_tax_p) as pajak_dibayar from ( "
                + sqa.getSQL()
                + " group by d.policy_date,c.mutation_date,d.cust_name,c.attr_pol_name,c.cc_code,c.ccy_rate,d.pol_no,c.attr_pol_no,c.ent_id,g.ref_ent_id,c.kode_ko,f.ar_settlement_id,c.refid1 "
                + " order by c.cc_code,c.attr_pol_no "
                + " ) a where (premi_paid <> 0 or ap_sfee_p <> 0 or ap_pcost_p <> 0 or premi_paid_co <> 0 or ap_hfee_p <> 0 or ap_bfee_p <> 0 or ap_comis_p <> 0 or ap_fbase_p <> 0 or ap_ppn_p <> 0 or ap_disc_p <> 0) "
                + "group by cc_code,policy_date,pol_no,inst,kodeko,cust_name "
                + " ) a order by a.koda,a.pol_no,a.inst desc,a.kodeko ";


        String nama_file = null;
        nama_file = "premi_paid" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    public DTOList PREMIRECEIPT() throws Exception {
        final boolean FLT_PAID_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_PREMI"));
        final boolean FLT_PAID_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COMM"));
        final boolean FLT_PAID_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_TAX"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "f.create_who,d.cc_code,f.receipt_no as description,f.receipt_date as period_start,d.policy_date,d.cust_name,d.ccy_rate,"
                + "d.pol_no,round((d.premi_total*d.ccy_rate),0) as premi_total,round((d.premi_netto*d.ccy_rate),0) as premi_netto,round((d.total_due*d.ccy_rate),0) as total_due,"
                + "round(coalesce((d.nd_pcost*d.ccy_rate),0),0) as nd_pcost,round(coalesce((d.nd_sfee*d.ccy_rate),0),0) as nd_sfee,round(coalesce((d.nd_ppn*d.ccy_rate),0),0) as nd_ppn, "
                + "(round(coalesce((d.nd_disc1*d.ccy_rate),0),0) + round(coalesce((d.nd_disc2*d.ccy_rate),0),0)) as nd_disc1,"
                + "((round(coalesce((d.nd_comm1*d.ccy_rate),0),0) + round(coalesce((d.nd_comm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_comm3*d.ccy_rate),0),0) + round(coalesce((d.nd_comm4*d.ccy_rate),0),0))) as nd_comm1,"
                + "(round(coalesce((d.nd_brok1*d.ccy_rate),0),0) + round(coalesce((d.nd_brok2*d.ccy_rate),0),0)) as nd_brok1,"
                + "(round(coalesce((d.nd_hfee*d.ccy_rate),0),0)) as nd_hfee,"
                + "(round(coalesce((d.nd_feebase1*d.ccy_rate),0),0) + round(coalesce((d.nd_feebase2*d.ccy_rate),0),0)) as nd_feebase1,"
                + "((round(coalesce((d.nd_taxcomm1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_taxcomm3*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm4*d.ccy_rate),0),0))) as nd_taxcomm1,"
                + "(round(coalesce((d.nd_taxbrok1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxbrok2*d.ccy_rate),0),0)) as nd_taxbrok1,"
                + "(round(coalesce((d.nd_taxhfee*d.ccy_rate),0),0)) as nd_taxhfee,"
                + "sum(getpremi2(e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id and b.ar_invoice_dtl_root_id is not null "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = d.entity_id "
                + "left join ent_master h on h.ent_id = d.prod_id ");

        sqa.addClause("f.status = 'POST'");

        if (FLT_PAID_PREMI) {
            sqa.addClause("f.ar_settlement_id = 1");
        }

        if (FLT_PAID_COMM) {
            sqa.addClause("f.ar_settlement_id = 2");
        }

        if (FLT_PAID_TAX) {
            sqa.addClause("f.ar_settlement_id = 8");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (createDateFrom != null) {
            sqa.addClause("date_trunc('day',f.create_date) >= '" + createDateFrom + "'");
            //sqa.addPar(createDateFrom);
        }

        if (createDateTo != null) {
            sqa.addClause("date_trunc('day',f.create_date) <= '" + createDateTo + "'");
            //sqa.addPar(createDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("f.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("e.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else {
                sqa.addClause("e.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            }
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("c.attr_pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }


        final String sql = sqa.getSQL()
                + "group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,d.cust_name,d.cc_code,d.ccy_rate,"
                + "d.pol_no,d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,d.nd_comm4,"
                + "d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,"
                + "d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee,d.nd_feebase1,d.nd_feebase2,d.nd_ppn "
                + "order by f.receipt_date,f.receipt_no,d.pol_no ";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_OS_PREMI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " distinct a.entity_id,a.pol_no,a.cust_name,a.policy_date,coalesce(a.premi_total,0) as premi_total,coalesce(a.premi_total_adisc,0) as premi_total_adisc,"
                + " coalesce(a.total_fee,0) as total_fee,coalesce(a.total_due,0) as total_due,coalesce(a.premi_netto,0) as premi_netto, "
                + " (coalesce ((select sum(amount_settled) from ar_invoice where ar_invoice.refid0='PREMI/'||a.pol_id and commit_flag='Y'),0)) as premi_paid,"
                + /* " (coalesce((select sum(amount) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis,"+
                " (coalesce((select sum(amount_settled) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis_p,"+
                " (coalesce((select sum(amount) from ar_pol2 where ins_item_cat='BROKR' and pol_id=a.pol_id ),0)) as ap_brok,"+
                " (coalesce((select sum(amount_settled) from ar_pol2 where ins_item_cat='BROKR' and pol_id=a.pol_id),0)) as ap_brok_p,"+
                 */ " (select coalesce(sum(getpremi2(q.ins_item_cat='COMM',y.amount)),0) "
                + "	from ins_policy x  "
                + "	inner join ins_pol_items y on y.pol_id = x.pol_id "
                + "	inner join ins_items q on q.ins_item_id = y.ins_item_id "
                + "	where y.pol_id=a.pol_id) as komisi, "
                + " (select coalesce(sum(y.tax_amount),0) "
                + " 	from ins_policy x "
                + " 	inner join ins_pol_items y on y.pol_id = x.pol_id "
                + " 	where y.pol_id=a.pol_id and y.item_class='PRM' and y.tax_code is not null) as tax ");

        sqa.addQuery(
                " from ins_policy a "
                + " left join ent_master c on c.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");

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
            sqa.addClause("c.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = "select  entity_id,pol_no,cust_name,policy_date,premi_total,(premi_total-premi_total_adisc) as diskon,"
                + "total_fee as biaya_adm,total_due as tag_netto,"
                + "(komisi-tax) as total_komisi,tax as pajak,premi_netto,premi_paid as premi_dibayar,ap_comis_p as komisi_dibayar,"
                + "ap_brok_p as broker_dibayar,(premi_netto-premi_paid) as out_premi,"
                + "(ap_comis-ap_comis_p) as out_comm,(ap_brok-ap_brok_p) as out_broker from ( " + sqa.getSQL() + " ) x "
                + " where premi_paid < premi_netto "
                + "order by entity_id,pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_OS_PREMI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Entity_ID");
            row0.createCell(1).setCellValue("Nomor Polis");
            row0.createCell(2).setCellValue("Nama Tertanggung");
            row0.createCell(3).setCellValue("Tanggal Polis");
            row0.createCell(4).setCellValue("Premi Total");
            row0.createCell(5).setCellValue("Diskon");
            row0.createCell(6).setCellValue("Biaya Adm");
            row0.createCell(7).setCellValue("Tagihan Netto");
            row0.createCell(8).setCellValue("Total Komisi");
            row0.createCell(9).setCellValue("Pajak");
            row0.createCell(10).setCellValue("Premi Netto");
            row0.createCell(11).setCellValue("Premi Dibayar");
            row0.createCell(12).setCellValue("Komisi Dibayar");
            row0.createCell(13).setCellValue("Brokerage Dibayar");
            row0.createCell(14).setCellValue("Outstanding Premi");
            row0.createCell(15).setCellValue("Outstanding Komisi");
            row0.createCell(16).setCellValue("Outstanding Brokerage");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(String.valueOf(h.getFieldValueByFieldNameBD("entity_id")));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("biaya_adm").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("tag_netto").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("total_komisi").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pajak").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_netto").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_dibayar").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("komisi_dibayar").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("broker_dibayar").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("out_premi").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("out_comm").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("out_broker").doubleValue());

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

    public String getStPolicyTypeGroupDesc() {
        return stPolicyTypeGroupDesc;
    }

    public void setStPolicyTypeGroupDesc(String stPolicyTypeGroupDesc) {
        this.stPolicyTypeGroupDesc = stPolicyTypeGroupDesc;
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

    public Date getPLADateFrom() {
        return PLADateFrom;
    }

    public void setPLADateFrom(Date PLADateFrom) {
        this.PLADateFrom = PLADateFrom;
    }

    public Date getPLADateTo() {
        return PLADateTo;
    }

    public void setPLADateTo(Date PLADateTo) {
        this.PLADateTo = PLADateTo;
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

    public Date getPaymentDateFrom() {
        return paymentDateFrom;
    }

    public void setPaymentDateFrom(Date paymentDateFrom) {
        this.paymentDateFrom = paymentDateFrom;
    }

    public Date getPaymentDateTo() {
        return paymentDateTo;
    }

    public void setPaymentDateTo(Date paymentDateTo) {
        this.paymentDateTo = paymentDateTo;
    }

    public String getStReceiptNo() {
        return stReceiptNo;
    }

    public void setStReceiptNo(String stReceiptNo) {
        this.stReceiptNo = stReceiptNo;
    }

    public DTOList OUTSTANDING_CLAIM() throws Exception {
        final boolean FLT_OUTS_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM"));
        final boolean FLT_OUTS_CLAIM_CO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_CO"));
        final boolean FLT_OUTS_CLAIM_RI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_RI"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " b.create_who,a.cc_code,a.pol_type_id,a.pol_id,a.cust_name,b.ar_cust_id as entity_id,a.policy_date,a.dla_date,a.pol_no,a.ccy_rate_claim,a.dla_no,b.no_surat_hutang as ref1,"
                + "round(a.insured_amount*a.ccy_rate,0) as insured_amount,round(a.premi_total*a.ccy_rate,0) as premi_total,"
                + "round(coalesce(a.nd_pcost*a.ccy_rate,0),0) as nd_pcost,round(coalesce(a.nd_sfee*a.ccy_rate,0),0) as nd_sfee,"
                + "round(coalesce(a.claim_amount_approved*a.ccy_rate_claim,0),0) as claim_amount_approved,round(coalesce(b.amount,0),0) as total_due,"
                + "round(coalesce(b.amount_settled,0),0) as ap_comis_p, "
                + "getpremi(invoice_type = 'AP',coalesce((select sum(x.amount) from ar_invoice x where x.ar_trx_type_id = 11 and x.attr_pol_id = b.attr_pol_id),0),0) as tax, "
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (61,111)),0) as premi_base ");

        sqa.addQuery(
                " from ar_invoice b "
                + " left join ins_policy a on a.pol_id = b.attr_pol_id "
                + " left join ent_master c on c.ent_id = a.entity_id ");

        sqa.addClause("coalesce(b.amount,0) <> 0");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");

        if (FLT_OUTS_CLAIM) {
            sqa.addClause("invoice_type = 'AP'");
            sqa.addClause("ar_trx_type_id = 12");
        }

        if (FLT_OUTS_CLAIM_RI) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 14");
        }

        if (FLT_OUTS_CLAIM_CO) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 16");
        }

        //sqa.addClause("b.amount - coalesce(b.amount_settled,0) > 0");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(appDateTo);
        }

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
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
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
            sqa.addClause("b.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("c.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        String query = " b.refid1 in ( select pla_no from ( "
                + "select x.refid1 as pla_no,sum(round(coalesce(x.amount,0),2)) as amount "
                + "from ar_invoice x "
                + "left join ins_policy z on z.pol_id = x.attr_pol_id "
                //+ "where coalesce(x.amount,0) <> 0 and coalesce(x.negative_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";
                + "where coalesce(x.amount,0) <> 0 and coalesce(x.cancel_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";

        if (getAppDateFrom() != null) {
            query = query + " and date_trunc('day',x.mutation_date) >= '" + appDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getAppDateTo() != null) {
            query = query + " and date_trunc('day',x.mutation_date) <= '" + appDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (x.receipt_date is null or date_trunc('day',x.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            query = query + " and z.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and z.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyTypeGroupID() != null) {
            query = query + " and z.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'";
            //sqa.addClause("z.ins_policy_type_grp_id = ?");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            query = query + " and z.pol_type_id = '" + stPolicyTypeID + "'";
            //sqa.addClause("z.pol_type_id = ?");
            //sqa.addPar(stPolicyTypeID);
        }

        query = query + " group by x.refid1 ) a where amount <> 0 ) ";

        sqa.addClause(query);

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,b.ar_cust_id,b.no_surat_hutang ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList OUTSTANDINGCOREINS() throws Exception {
        final boolean FLT_OUTS_COINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_COINS"));
        final boolean FLT_OUTS_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_REINS"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.policy_date,b.create_who,a.cc_code,b.pol_id,b.pol_no,a.ar_cust_id as entity_id,a.ccy_rate,a.no_surat_hutang as cust_name,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=66,c.amount)),0),0) as nd_brok1,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=66,c.amount_settled)),0),0) as nd_brok1pct,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=67,c.amount)),0),0) as nd_brok2,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=67,c.amount_settled)),0),0) as nd_brok2pct,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=55,c.amount)),0),0) as refn1,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=55,c.amount_settled)),0),0) as refn2,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=73,c.amount)),0),0) as refn3,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=73,c.amount_settled)),0),0) as refn4 ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id "
                + " left join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master d on d.ent_id = b.entity_id "
                + " left join ent_master e on e.ent_id = a.ent_id ");

        if (FLT_OUTS_COINS) {
            sqa.addClause("invoice_type = 'AP'");
            sqa.addClause("ar_trx_type_id = 10");
        }

        if (FLT_OUTS_REINS) {
            sqa.addClause("invoice_type = 'AP'");
            sqa.addClause("ar_trx_type_id = 13");
        }

        sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        sqa.addClause("a.used_flag is null");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("a.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(stKreasiName);
        }

        if (stReinsName != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(stReinsName);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = "select * from ( " + sqa.getSQL() + " group by b.policy_date,b.create_who,a.cc_code,"
                + " b.pol_id,b.pol_no,a.ar_cust_id,a.ccy_rate,a.no_surat_hutang,a.invoice_type "
                + " order by a.cc_code,b.pol_no,a.ar_cust_id,a.no_surat_hutang "
                + " ) a where pol_no is not null order by a.cc_code,a.pol_no,a.entity_id,a.cust_name ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList RECEIPT() throws Exception {
        final boolean FLT_PAID_COINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COINS"));
        final boolean FLT_PAID_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_REINS"));
        final boolean FLT_PAID_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM"));
        final boolean FLT_PAID_CLAIM_CO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM_CO"));
        final boolean FLT_PAID_CLAIM_RI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM_RI"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "e.create_who,e.receipt_no as description,e.receipt_date as period_start,d.cc_code,d.pol_id,d.policy_date,"
                + "d.dla_date,d.pol_no,d.ccy_rate,d.dla_no,d.pol_type_id,f.ar_cust_id as entity_id,f.no_surat_hutang as cust_name,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=66,a.amount)),0),0) as nd_brok1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=66,a.amount_settled)),0),0) as nd_brok1pct,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=67,a.amount)),0),0) as nd_brok2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=67,a.amount_settled)),0),0) as nd_brok2pct,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=55,a.amount)),0),0) as refn1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=55,a.amount_settled)),0),0) as refn2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=73,a.amount)),0),0) as refn3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=73,a.amount_settled)),0),0) as refn4,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=57,a.amount)),0),0) as nd_comm1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (58,59,64),a.amount)),0),0) as nd_comm2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (60,61,62,63,65),a.amount)),0),0) as nd_comm3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=57,a.amount_settled)),0),0) as nd_comm4,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=68,a.amount)),0),0) as nd_taxcomm1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=68,a.amount_settled)),0),0) as nd_taxcomm2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=69,a.amount)),0),0) as nd_taxcomm3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=69,a.amount_settled)),0),0) as nd_taxcomm4");

        sqa.addQuery(
                " from ar_invoice_details a "
                + " inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id "
                + " left join ins_policy d on d.pol_id = b.pol_id "
                + " left join ar_receipt e on e.ar_receipt_id = b.receipt_id"
                + " left join ar_invoice f on f.ar_invoice_id = b.ar_invoice_id "
                + " left join ent_master g on g.ent_id = d.entity_id ");

        sqa.addClause("e.status = 'POST'");

        if (FLT_PAID_REINS) {
            sqa.addClause("e.ar_settlement_id = 9");
        }

        if (FLT_PAID_CLAIM) {
            sqa.addClause("e.ar_settlement_id = 10");
        }

        if (FLT_PAID_CLAIM_RI) {
            sqa.addClause("e.ar_settlement_id = 11");
        }

        if (FLT_PAID_COINS) {
            sqa.addClause("e.ar_settlement_id = 13");
        }

        if (FLT_PAID_CLAIM_CO) {
            sqa.addClause("e.ar_settlement_id = 14");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',d.claim_approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',d.claim_approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',d.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',d.claim_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("e.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stReinsName != null) {
            sqa.addClause("f.no_surat_hutang = ?");
            sqa.addPar(stReinsName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("f.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
            sqa.addClause("f.no_surat_hutang = ?");
            sqa.addPar(stKreasiName);
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("d.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(d.pla_no like ? or d.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
        }

        final String sql = sqa.getSQL()
                + " group by e.create_who,e.receipt_no,e.receipt_date,d.cc_code,d.pol_id,d.policy_date,d.dla_date,d.pol_no,d.ccy_rate,f.ar_cust_id,"
                + " d.dla_no,d.pol_type_id,f.no_surat_hutang order by e.receipt_date,d.pol_no,f.no_surat_hutang ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    /*
    public GLCostCenterView getCostCenter() {
    final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

    return costcenter;
    }
     */
    public GLCostCenterView getCostCenter(String stCostCenter) {
        return (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenter);
    }

    public InsurancePolicyView getPolicy(String stParentID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
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

    public DTOList COMM() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("c.receipt_no as description,c.receipt_date as period_start,c.create_who,"
                + " coalesce(b.policy_date,a.mutation_date) as policy_date,a.due_date,a.receipt_date,b.pol_id,coalesce(b.pol_no,a.attr_pol_no) as pol_no,"
                + " coalesce(d.ent_name,a.attr_pol_name) as cust_name,b.premi_total,b.premi_netto,"
                + " coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee,"
                + " (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + " (coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1,"
                + " (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1,"
                + " (coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2,"
                + " (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1,"
                + " coalesce(b.nd_hfee,0) as nd_hfee,"
                + " (coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1,"
                + " (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2,"
                + " (coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1,"
                + " coalesce(b.nd_taxhfee,0) as nd_taxhfee,"
                + " sum(coalesce(a.amount_settled,0)) as ap_comis_p ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id "
                + " left join ar_receipt c on c.ar_ap_invoice_id = a.ar_invoice_id "
                + " left join ent_master d on d.ent_id = b.entity_id ");

        sqa.addClause("substr(a.refid2,1,3) = 'POL'");
        sqa.addClause("a.ar_trx_type_id = 11");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        //if (FLT_OS_COMM) {
        //    sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        //    sqa.addClause("a.used_flag is null ");
        //}

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01 00:00:00'");
//            //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01 00:00:00'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= ?");
//            sqa.addPar(Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01");
//        }
//
//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= ?");
//            sqa.addPar(perDateFrom);
//        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        /*NON AKS*/
//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = ?");
//            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("b.region_id = ?");
//            sqa.addPar(stRegion);
//        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                if (stBranch != null) {
                    sqa.addClause("b.cc_code = '80' and b.cc_code_source = ?");
                    sqa.addPar(stBranch);
                }

                if (stRegion != null) {
                    sqa.addClause("b.region_id_source = ?");
                    sqa.addPar(stRegion);
                }
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                if (stBranch != null) {
                    sqa.addClause("b.cc_code = ?");
                    sqa.addPar(stBranch);
                }

                if (stRegion != null) {
                    sqa.addClause("b.region_id = ?");
                    sqa.addPar(stRegion);
                }
            }
        } else {
            if (stBranch != null) {
                sqa.addClause("((b.cc_code = ?) or (b.cc_code = '80' and b.cc_code_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stBranch);
            }
            if (stRegion != null) {
                sqa.addClause("((b.cc_code = ? and b.region_id = ?) or (b.cc_code = '80' and b.cc_code_source = ? and b.region_id_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stRegion);
                sqa.addPar(stBranch);
                sqa.addPar(stRegion);
            }
        }

        if (stEntityID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("c.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no like ? ");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        /*if (paymentPremiDateFrom != null) {
        sqa.addClause("a.attr_pol_id in (select g.pol_id from ar_receipt f "
        + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null "
        + "and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'"
        + "and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "' group by g.pol_id )");
        }*/

        if (paymentPremiDateFrom != null) {
            sqa.addClause(" a.attr_pol_id in ( select a.pol_id from ar_invoice f "
                    + "inner join ins_policy i on i.pol_id = a.pol_id "
                    + "inner join ent_master h on h.ent_id = i.entity_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.amount_settled is not null "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) "
                    + "and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'"
                    + "and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "')");
        }

        final String sql = sqa.getSQL() + " group by c.receipt_no,c.receipt_date,c.create_who,b.policy_date,b.pol_id,b.pol_no,d.ent_name,b.cust_name,b.premi_total,"
                + " b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,b.nd_feebase1,b.nd_feebase2,a.due_date,a.receipt_date,"
                + " b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,b.nd_comm2,b.nd_comm3,a.attr_pol_no,a.attr_pol_name,a.mutation_date,"
                + " b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee order by a.attr_pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList PAJAK() throws Exception {
        final boolean FLT_OS_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_TAX"));
        final boolean FLT_PAID_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_TAX"));

        final SQLAssembler sqa = new SQLAssembler();

        String tax = null;

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                tax = "a.tax_code in (1,4,7)";
            } else if (stTax.equalsIgnoreCase("2")) {
                tax = "a.tax_code in (2,5,8)";
            } else if (stTax.equalsIgnoreCase("3")) {
                tax = "a.tax_code = 10";
            }
        }

        sqa.addSelect(
                " a.ar_invoice_id,a.no_surat_hutang,b.ent_name as prod_name,coalesce(a.amount_settled,0) as ap_tax_p, "
                + " e.receipt_no as description,e.receipt_date as period_start,e.create_who, "
                + " d.policy_date,a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,d.cust_name,d.premi_total,coalesce(d.nd_pcost,0) as nd_pcost,coalesce(d.nd_sfee,0) as nd_sfee,"
                + " (coalesce(d.nd_disc1,0)+coalesce(d.nd_disc2,0)) as nd_disc1, "
                + " (select sum(coalesce(a.amount,0)) from ins_pol_items a "
                + " where a.pol_id = d.pol_id and a.ins_item_id in (11,12,13,18,19,20,25,26,27,32,33,34,70) and "
                + tax + " ) as nd_comm1,"
                + " (select sum(coalesce(a.tax_amount,0)) from ins_pol_items a "
                + " where a.pol_id = d.pol_id and a.ins_item_id in (11,12,13,18,19,20,25,26,27,32,33,34,70) and "
                + tax + " ) as nd_taxcomm1 ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ar_invoice_details c on a.ar_invoice_id = c.ar_invoice_id "
                + " left join ins_policy d on d.pol_id = a.attr_pol_id "
                + " left join ar_receipt_lines e on e.ar_invoice_id = a.ar_invoice_id ");

        if (stTax.equalsIgnoreCase("1") || stTax.equalsIgnoreCase("2")) {
            sqa.addClause("a.no_surat_hutang is not null");
        }
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("a.ar_trx_type_id in (11,12)");

        if (FLT_OS_TAX) {
            sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        }

        if (FLT_PAID_TAX) {
            sqa.addClause("e.receipt_no is not null");
        }

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
                sqa.addPar(Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
                sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
        }

//        if (policyDateFrom != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(policyDateFrom);
//        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',e.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',e.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("e.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("b.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("c.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else if (stTax.equalsIgnoreCase("2")) {
                sqa.addClause("c.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            } else if (stTax.equalsIgnoreCase("3")) {
                sqa.addClause("c.ar_trx_line_id = 96");
            }
        }

        if (stReceiptNo != null) {
            sqa.addClause("e.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + " order by d.cc_code,e.receipt_no,a.attr_pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public String getStZoneCode() {
        return stZoneCode;
    }

    public void setStZoneCode(String stZoneCode) {
        this.stZoneCode = stZoneCode;
    }

    public String getStKreasiName() {
        return stKreasiName;
    }

    public void setStKreasiName(String stKreasiName) {
        this.stKreasiName = stKreasiName;
    }

    public String getStReinsName() {
        return stReinsName;
    }

    public void setStReinsName(String stReinsName) {
        this.stReinsName = stReinsName;
    }

    public String getStCustCategory1() {
        return stCustCategory1;
    }

    public void setStCustCategory1(String stCustCategory1) {
        this.stCustCategory1 = stCustCategory1;
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

    public String getStCreateName() {
        return stCreateName;
    }

    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
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

    public Date getPerDateFrom() {
        return perDateFrom;
    }

    public void setPerDateFrom(Date perDateFrom) {
        this.perDateFrom = perDateFrom;
    }

    public RegionView getRegion() {
        final RegionView region = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegion);

        return region;
    }

    public DTOList EXCEL_CADANGAN_PREMI() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.status,a.cc_code,c.description as cabang,a.pol_id,a.pol_no,substr(a.pol_no,1,16)||b.ref1 as xxx,b.ins_pol_obj_id,b.ref1 as nama,b.insured_amount,"
                + "(select sum(premi) from ins_pol_cover a where a.ins_pol_obj_id = b.ins_pol_obj_id) as premi,b.refd2 as tglcair,b.refd3 as tglakhir,"
                + "extract('day' from b.refd3-b.refd2)::numeric as n,extract('day' from b.refd3-'2012-10-31 00:00:00')::numeric as t ");

        sqa.addQuery(
                " from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + " left join gl_cost_center c on c.cc_code = a.cc_code ");

        sqa.addClause("a.status in ('POLICY','RENEWAL')");
        sqa.addClause("a.effective_flag='Y'");
        sqa.addClause("a.pol_type_id in (4,21,59)");
        sqa.addClause("a.pol_id in (662633,184590,231264)");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (periodEndFrom != null) {
            sqa.addClause("date_trunc('day',b.refd3) >= ?");
            sqa.addPar(periodEndFrom);
        }

        if (periodEndTo != null) {
            sqa.addClause("date_trunc('day',b.refd3) <= ?");
            sqa.addPar(periodEndTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        String sql = "select a.cc_code,a.cabang,sum(insured_amount) as insured_amount,sum(premi) as premi,sum(((n-t)/n)*premi) as cadangan from ( "
                + sqa.getSQL() + " and (substr(a.pol_no,1,16)||b.ref1 not in ( "
                + " (select substr(a.pol_no,1,16)||b.ref1 as xxx "
                + " from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + " where a.status IN ('ENDORSE') and a.effective_flag = 'Y' and ((a.pol_type_id = 21 and b.refn6 <> 0 ) "
                + " or a.pol_type_id in (4,59)) ";

        if (getPeriodEndFrom() != null) {
            sql = sql + "and date_trunc('day',b.refd3) >= ? ";
            sqa.addPar(periodEndFrom);
        }

        sql = sql + " group by substr(a.pol_no,1,16)||b.ref1)) ";

        sql = sql + " or substr(a.pol_no,1,16)||b.ref1 not in ( "
                + " (select substr(a.pol_no,1,16)||b.ref1 as zzz "
                + " from ins_policy a"
                + " inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id"
                + " where a.status IN ('CLAIM') and a.effective_flag = 'Y' and a.claim_status = 'DLA' and a.pol_type_id in (21,4,59) "
                + " group by substr(a.pol_no,1,16)||b.ref1))) "
                + " ) a group by a.cc_code,a.cabang order by a.cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_CADANGAN_PREMI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cabang");
            row0.createCell(2).setCellValue("insured");
            row0.createCell(3).setCellValue("premi");
            row0.createCell(4).setCellValue("cadangan");

            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("cadangan").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public void EXCEL_OUTSTANDINGPREMIINTERN() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,b.cc_code_source,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,substr(a.refid1,6,2) as inst,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,b.prod_name as marketer_ext,"
                + "a.mutation_date,a.due_date,a.receipt_no,a.receipt_date,h.vs_description as trx_method,a.ccy_rate,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who,b.period_start,b.period_end,b.marketing_officer_who ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " inner join ent_master e on e.ent_id = a.ent_id "
                + " inner join ent_master f on f.ent_id = b.entity_id "
                + " inner join ent_master g on g.ent_id = b.prod_id "
                + " left join s_valueset h on h.vs_code = a.trx_method and h.vs_group = 'RECEIPT_TRX_METHOD' ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");

        String query = " substr(a.attr_pol_no,1,16)||a.ar_trx_type_id in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16)||a.ar_trx_type_id as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + "inner join ent_master e on e.ent_id = a.ent_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'";
                //sqa.addPar(policyDateFrom);
            } else {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

//        if (getPolicyDateFrom() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (getPolicyDateTo() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            //sqa.addPar(policyDateTo);
//        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                query = query + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    query = query + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    query = query + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                query = query + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                query = query + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                query = query + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                query = query + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStCompanyID() != null) {
            query = query + " and e.ref2 = '" + stCompanyID + "'";
            //sqa.addPar(stCompanyID);
        }

        query = query + " group by substr(a.attr_pol_no,1,16),a.ar_trx_type_id ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,';'||a.cc_code_source as koda_nonaks,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,a.invoice_no,';'||a.attr_pol_no as nopol,(a.inst::numeric+1) as inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,a.nd_comm1 as komisi_excl_tax,"
                + "a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee,((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,';'||a.create_who as createwho,"
                + "a.period_start,a.period_end,a.marketing_officer_who,a.marketer_ext,a.trx_method  "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,h.vs_description,a.cc_code,b.cc_code_source,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,b.prod_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,b.period_start,b.period_end,b.marketing_officer_who "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,a.ar_invoice_id,a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100intern_" + System.currentTimeMillis() + ".csv";

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

    public void EXCEL_OUTSTANDINGPREMIINTERN_XLS() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,"
                + "a.mutation_date,a.due_date,a.receipt_date,a.ccy_rate,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                //+ "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                //+ "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                //+ "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master f on f.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");

        String query = " substr(a.attr_pol_no,1,16)||a.ar_trx_type_id in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16)||a.ar_trx_type_id as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

//        if (getPolicyDateFrom() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (getPolicyDateTo() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            //sqa.addPar(policyDateTo);
//        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            query = query + " and a.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        query = query + " group by substr(a.attr_pol_no,1,16),a.ar_trx_type_id ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_date as tglbayar,a.invoice_no,';'||a.attr_pol_no as nopol,(substr(a.refid1,6,1)::numeric+1) as inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto "
                + " from ( " + sqa.getSQL() + " group by a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,substr(a.refid1,6,1),a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100intern_" + System.currentTimeMillis() + ".csv";

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

    public void EXPORT_OUTSTANDINGPREMIINTERN_WITHAGING() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("detil");
        XSSFSheet sheet2 = wb.createSheet("per cabang");
        XSSFSheet sheet3 = wb.createSheet("per cob");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        final DTOList list3 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("polid");
            row0.createCell(2).setCellValue("tglpolis");
            row0.createCell(3).setCellValue("tgl_jtempo");
            row0.createCell(4).setCellValue("nobuk");
            row0.createCell(5).setCellValue("tglbayar");
//            row0.createCell(6).setCellValue("invoice_no");
            row0.createCell(6).setCellValue("nopol");
            row0.createCell(7).setCellValue("inst");
            row0.createCell(8).setCellValue("kodeko");
//            row0.createCell(10).setCellValue("nama_bank");
//            row0.createCell(11).setCellValue("sumbis");
//            row0.createCell(12).setCellValue("marketer_name");
//            row0.createCell(13).setCellValue("principal");
            row0.createCell(9).setCellValue("premi_bruto");
            row0.createCell(10).setCellValue("biapol");
            row0.createCell(11).setCellValue("biamat");
            row0.createCell(12).setCellValue("feebase");
            row0.createCell(13).setCellValue("diskon");
            row0.createCell(14).setCellValue("ppn");
            row0.createCell(15).setCellValue("komisi_excl_tax");
            row0.createCell(16).setCellValue("tax_komisi");
            row0.createCell(17).setCellValue("hfee_excl_tax");
            row0.createCell(18).setCellValue("tax_hfee");
            row0.createCell(19).setCellValue("bfee_excl_tax");
            row0.createCell(20).setCellValue("tax_bfee");
            row0.createCell(21).setCellValue("tag_netto");

            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("polid").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tglpolis"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tgl_jtempo"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            if (h.getFieldValueByFieldNameDT("tglbayar") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tglbayar"));
            }
//            if (h.getFieldValueByFieldNameST("invoice_no") != null) {
//                row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
//            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            if (h.getFieldValueByFieldNameBD("inst") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("inst").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("kodeko") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
            }
//            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
//            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
//            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("marketer_name"));
//            if (h.getFieldValueByFieldNameST("principal") != null) {
//                row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("principal"));
//            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_bruto").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("biapol").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("biamat").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("feebase").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("komisi_excl_tax").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("tax_komisi").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("hfee_excl_tax").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("tax_hfee").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("bfee_excl_tax").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("tax_bfee").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tag_netto").doubleValue());

        }

        for (int j = 0; j < list2.size(); j++) {
            HashDTO h = (HashDTO) list2.get(j);

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("<= 60 hari");
            row0.createCell(2).setCellValue("> 60 hari");

            XSSFRow row = sheet2.createRow(j + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("kurang").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("lebih").doubleValue());
        }

        for (int k = 0; k < list3.size(); k++) {
            HashDTO h = (HashDTO) list3.get(k);

            //bikin header
            XSSFRow row0 = sheet3.createRow(0);
            row0.createCell(0).setCellValue("cob");
            row0.createCell(1).setCellValue("<= 60 hari");
            row0.createCell(2).setCellValue("> 60 hari");

            XSSFRow row = sheet3.createRow(k + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("poltype").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("kurang").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("lebih").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void EXCEL_COMM() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("coalesce(b.policy_date,a.mutation_date) as policy_date,"
                + " a.due_date,a.receipt_no,a.receipt_date,b.pol_id,a.cc_code as koda,b.cc_code_source as koda_nonaks,coalesce(b.pol_no,a.attr_pol_no) as pol_no,"
                + " coalesce(d.ent_name,a.attr_pol_name) as cust_name,coalesce(b.cust_name) as tertanggung,"
                + " b.premi_total,b.premi_netto,coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee,"
                + " (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + " (coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1,"
                + " (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1,"
                + " (coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2,"
                + " (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1,"
                + " coalesce(b.nd_hfee,0) as nd_hfee,"
                + " (coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1,"
                + " (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2,"
                + " (coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1,"
                + " coalesce(b.nd_taxhfee,0) as nd_taxhfee,coalesce(b.nd_ppn,0) as nd_ppn, "
                + " sum(getpremi2(g.category = 'HFEE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_hfee,"
                + " sum(getpremi2(g.category = 'BROKERAGE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_bfee,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class = 'FEEBASE' and f.f_comission = 'Y',round(f.amount,0))) as os_fbase,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_comm,"
                + " sum(getpremi2(g.item_desc like 'PPN%', f.amount)) as os_ppn ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(
                " from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                //                + " left join ar_receipt_lines c on c.ar_invoice_id = a.ar_invoice_id and c.line_type = 'INVOC' "
                //                + " left join ar_receipt h on h.ar_receipt_id = c.receipt_id "
                + " left join ent_master d on d.ent_id = b.entity_id "
                //                + " left join ent_master e on e.ent_id = a.ent_id "
                + " inner join ar_invoice_details f on f.ar_invoice_id = a.ar_invoice_id "
                + " left join ar_trx_line g on g.ar_trx_line_id = f.ar_trx_line_id ");

        sqa.addClause("substr(a.refid2,1,3) = 'POL'");
        sqa.addClause("a.ar_trx_type_id = 11");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        //if (FLT_OS_COMM) {
        //    sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        //    sqa.addClause("a.used_flag is null ");
        //}

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= '" + perDateFrom + "'");
//            //sqa.addPar(policyDateTo);
//        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            //sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("b.region_id = " + stRegion);
//            //sqa.addPar(stRegion);
//        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*String payment = " a.attr_pol_id in ( select g.pol_id from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";*/

        String payment = " a.attr_pol_id in ( select f.attr_pol_id from ar_invoice f "
                + "inner join ins_policy b on b.pol_id = f.attr_pol_id "
                //                + "inner join ent_master h on h.ent_id = i.entity_id "
                + "where f.posted_flag = 'Y' and coalesce(f.cancel_flag,'') <> 'Y' and f.amount_settled is not null "
                + "and f.invoice_type in ('AR','AP') and f.ar_trx_type_id in (5,6,7) ";

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
            } else {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'";
            }
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (policyDateTo != null) {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + policyDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + perDateFrom + "'";
        }

        if (paymentPremiDateFrom != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (paymentPremiDateTo != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + perDateFrom + "' ";
        }

        if (stPolicyNo != null) {
            payment = payment + " and f.attr_pol_no = '" + stPolicyNo + "' ";
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                payment = payment + " and b.cc_code = '" + stBranch + "'";

                if (stBranchSource != null) {
                    payment = payment + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (stRegionSource != null) {
                    payment = payment + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                payment = payment + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            payment = payment + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                payment = payment + " and b.cc_code = '80'";
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                payment = payment + " and b.cc_code <> '80'";
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                payment = payment + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else {
                payment = payment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            }
        }
//
//        if (stCompanyID != null) {
//            payment = payment + " and h.ref2 = '" + stCompanyID + "'";
//        }


        payment = payment + " group by f.attr_pol_id ) ";

        sqa.addClause(payment);

        String sql = " select tglpolis,tgl_jtempo,nobuk,tglbayar,polid,';'||koda as koda,';'||a.koda_nonaks as koda_nonaks,';'||nopol as nopol,nama_bank,tertanggung,premi_total,biapol,biamat,premi_bruto,feebase,ppn,"
                + " diskon,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,os_hfee as huthfee,os_bfee as hutbfee,os_fbase as hutfbase,os_comm as hutkom,os_ppn as hutppn "
                + " from ( select a.policy_date as tglpolis,a.pol_id as polid,"
                + " a.pol_no as nopol,a.koda,a.koda_nonaks,a.cust_name as nama_bank,a.tertanggung,a.premi_total,a.nd_pcost as biapol,a.nd_sfee as biamat,"
                + " ((nd_sfee+nd_pcost)+a.premi_total) as premi_bruto,a.nd_feebase1 as feebase,a.nd_ppn as ppn,a.nd_disc1 as diskon,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,"
                + " ((a.nd_comm1+nd_comm2)-(a.nd_taxcomm1+nd_taxcomm2)) as komisi,(a.nd_taxcomm1+nd_taxcomm2) as tax_komisi,"
                + " (nd_brok1-nd_taxbrok1) as bfee,nd_taxbrok1 as tax_bfee,(nd_hfee-nd_taxhfee) as hfee,nd_taxhfee as tax_hfee,"
                + " os_hfee,os_bfee,os_fbase,os_comm,os_ppn from ( " + sqa.getSQL()
                + " group by b.policy_date,b.pol_id,b.pol_no,a.cc_code,d.ent_name,b.premi_total,"
                + " b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,b.nd_feebase1,b.nd_feebase2,a.attr_pol_no,"
                + " a.attr_pol_name,b.cust_name,a.mutation_date,b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,a.due_date,a.receipt_no,a.receipt_date,"
                + " b.nd_comm2,b.nd_comm3,b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee,b.nd_ppn ";

        sql = sql + " ) a ) a order by a.nopol ";

        SQLUtil S = new SQLUtil();

        String nama_file = "oscomm_" + System.currentTimeMillis() + ".csv";

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

    public void EXCEL_COMM_XLS() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("coalesce(b.policy_date,a.mutation_date) as policy_date,"
                + " a.due_date,a.receipt_no,a.receipt_date,b.pol_id,a.cc_code as koda,coalesce(b.pol_no,a.attr_pol_no) as pol_no,"
                + " coalesce(d.ent_name,a.attr_pol_name) as cust_name,b.premi_total,b.premi_netto,"
                + " coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee,"
                + " (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + " (coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1,"
                + " (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1,"
                + " (coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2,"
                + " (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1,"
                + " coalesce(b.nd_hfee,0) as nd_hfee,"
                + " (coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1,"
                + " (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2,"
                + " (coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1,"
                + " coalesce(b.nd_taxhfee,0) as nd_taxhfee,coalesce(b.nd_ppn,0) as nd_ppn, "
                + " sum(getpremi2(g.category = 'HFEE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_hfee,"
                + " sum(getpremi2(g.category = 'BROKERAGE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_bfee,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class = 'FEEBASE' and f.f_comission = 'Y',round(f.amount,0))) as os_fbase,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_comm, "
                + " sum(getpremi2(g.item_desc like 'PPN%', f.amount)) as os_ppn ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }
        sqa.addQuery(
                " from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                + " left join ar_receipt_lines c on c.ar_invoice_id = a.ar_invoice_id and c.line_type = 'INVOC' "
                + " left join ar_receipt h on h.ar_receipt_id = c.receipt_id "
                + " left join ent_master d on d.ent_id = b.entity_id "
                //                + " left join ent_master e on e.ent_id = a.ent_id "
                + " inner join ar_invoice_details f on f.ar_invoice_id = a.ar_invoice_id "
                + " left join ar_trx_line g on g.ar_trx_line_id = f.ar_trx_line_id ");

        sqa.addClause("substr(a.refid2,1,3) = 'POL'");
        sqa.addClause("a.ar_trx_type_id = 11");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        //if (FLT_OS_COMM) {
        //    sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        //    sqa.addClause("a.used_flag is null ");
        //}

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= '" + perDateFrom + "'");
//            //sqa.addPar(policyDateTo);
//        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

//        if (stBranch != null) {
//            sqa.addClause("b.cc_code = '" + stBranch + "'");
//            //sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("b.region_id = " + stRegion);
//            //sqa.addPar(stRegion);
//        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

//        if (stCustCategory1 != null) {
//            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
//            //sqa.addPar(stCustCategory1);
//        }

        /*if (paymentPremiDateFrom != null || paymentPremiDateTo != null) {
        String payment = " a.attr_pol_id in (select g.pol_id from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";

        if (paymentPremiDateFrom != null) {
        payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        }
        if (paymentPremiDateTo != null) {
        payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        }

        if (stCompanyID != null) {
        payment = payment + " and h.ref2 = '" + stCompanyID + "'";
        }
        payment = payment + " group by g.pol_id )";

        sqa.addClause(payment);
        }*/

        if (paymentPremiDateFrom != null || paymentPremiDateTo != null) {
            String payment = " a.attr_pol_id in ( select f.attr_pol_id from ar_invoice f "
                    + "inner join ins_policy b on b.pol_id = f.attr_pol_id "
                    //                    + "inner join ent_master h on h.ent_id = i.entity_id "
                    + "where f.posted_flag = 'Y' and coalesce(f.cancel_flag,'') <> 'Y' and f.amount_settled is not null "
                    + "and f.invoice_type in ('AR','AP') and f.ar_trx_type_id in (5,6,7) ";

            if (policyDateFrom != null) {
                if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                    payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
                } else {
                    payment = payment + " and date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'";
                }
            } else {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
            }

            if (policyDateTo != null) {
                payment = payment + " and date_trunc('day',b.policy_date) <= '" + policyDateTo + "'";
            } else {
                payment = payment + " and date_trunc('day',b.policy_date) <= '" + perDateFrom + "'";
            }

            if (paymentPremiDateFrom != null) {
                payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
            }
            if (paymentPremiDateTo != null) {
                payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
            }

            if (stPolicyNo != null) {
                payment = payment + " and f.attr_pol_no = '" + stPolicyNo + "' ";
                //sqa.addPar('%' + stPolicyNo + '%');
            }

            if (stBranch != null) {
                if (stBranch.equalsIgnoreCase("80")) {
                    payment = payment + " and b.cc_code = '" + stBranch + "'";

                    if (stBranchSource != null) {
                        payment = payment + " and b.cc_code_source = '" + stBranchSource + "'";
                    }
                    if (stRegionSource != null) {
                        payment = payment + " and b.region_id_source = '" + stRegionSource + "'";
                    }
                } else {
                    payment = payment + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
                }
                //sqa.addPar(stBranch);
            }

            if (stRegion != null) {
                payment = payment + " and b.region_id = '" + stRegion + "'";
                //sqa.addPar(stRegion);
            }

            if (stBussinessPolType != null) {
                if (stBussinessPolType.equalsIgnoreCase("1")) {
                    payment = payment + " and b.cc_code = '80'";
                } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                    payment = payment + " and b.cc_code <> '80'";
                }
            }

            if (stBussinessPolTypeCob != null) {
                if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                    payment = payment + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
                } else {
                    payment = payment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
                }
            }
//
//        if (stCompanyID != null) {
//            payment = payment + " and h.ref2 = '" + stCompanyID + "'";
//        }

            payment = payment + " group by f.attr_pol_id )";
            sqa.addClause(payment);
        }

        String sql = " select tglpolis,tgl_jtempo,nobuk,tglbayar,polid,';'||koda as koda,';'||nopol as nopol,nama_bank,premi_total,biapol,biamat,premi_bruto,feebase,ppn, "
                + " diskon,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,os_hfee as huthfee,os_bfee as hutbfee,os_fbase as hutfbase,os_comm as hutkom,os_ppn as hutppn "
                + "from ( select a.policy_date as tglpolis,a.pol_id as polid,"
                + " a.pol_no as nopol,a.koda,a.cust_name as nama_bank,a.premi_total,a.nd_pcost as biapol,a.nd_sfee as biamat,"
                + " ((nd_sfee+nd_pcost)+a.premi_total) as premi_bruto,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,"
                + " ((a.nd_comm1+nd_comm2)-(a.nd_taxcomm1+nd_taxcomm2)) as komisi,(a.nd_taxcomm1+nd_taxcomm2) as tax_komisi,"
                + " (nd_brok1-nd_taxbrok1) as bfee,nd_taxbrok1 as tax_bfee,(nd_hfee-nd_taxhfee) as hfee,nd_taxhfee as tax_hfee,nd_ppn as ppn,"
                + " os_hfee,os_bfee,os_fbase,os_comm,os_ppn from ( " + sqa.getSQL()
                + " group by b.policy_date,b.pol_id,b.pol_no,a.cc_code,d.ent_name,b.premi_total,"
                + " b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,b.nd_feebase1,b.nd_feebase2,a.attr_pol_no,"
                + " a.attr_pol_name,a.mutation_date,b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,a.due_date,a.receipt_no,a.receipt_date,"
                + " b.nd_comm2,b.nd_comm3,b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee,b.nd_ppn ";

        sql = sql + " ) a ) a order by a.nopol ";

        SQLUtil S = new SQLUtil();

        String nama_file = "ho_oscomm_" + System.currentTimeMillis() + ".csv";

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

    public void EXPORT_COMM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal TotalComm = new BigDecimal(0);
        BigDecimal TotalTax = new BigDecimal(0);
        BigDecimal TotalPremiBruto = new BigDecimal(0);
        BigDecimal TotalPremiNetto = new BigDecimal(0);
        BigDecimal TotalOSComm = new BigDecimal(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            XSSFRow row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("Sampai Dengan : " + DateUtil.getDateStr(getPerDateFrom()));

            XSSFRow row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue("Cabang : " + getStBranchDesc() != null ? getStBranchDesc() : getStBranchName());

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("tgl polis");
            row0.createCell(1).setCellValue("pol ID");
            row0.createCell(2).setCellValue("no polis");
            row0.createCell(3).setCellValue("nama");
            row0.createCell(4).setCellValue("premi total");
            row0.createCell(5).setCellValue("biapol");
            row0.createCell(6).setCellValue("biamat");
            row0.createCell(7).setCellValue("tabrut");
            row0.createCell(8).setCellValue("feebase");
            row0.createCell(9).setCellValue("diskon");
            row0.createCell(10).setCellValue("komisi");
            row0.createCell(11).setCellValue("tax komisi");
            row0.createCell(12).setCellValue("premi netto");
            row0.createCell(13).setCellValue("hutkom");

            TotalTax = BDUtil.add(h.getFieldValueByFieldNameBD("nd_taxcomm1"), h.getFieldValueByFieldNameBD("nd_taxcomm2"));
            TotalTax = BDUtil.add(TotalTax, h.getFieldValueByFieldNameBD("nd_taxbrok1"));
            TotalTax = BDUtil.add(TotalTax, h.getFieldValueByFieldNameBD("nd_taxhfee"));

            TotalComm = BDUtil.add(h.getFieldValueByFieldNameBD("nd_comm1"), h.getFieldValueByFieldNameBD("nd_comm2"));
            TotalComm = BDUtil.add(TotalComm, h.getFieldValueByFieldNameBD("nd_brok1"));
            TotalComm = BDUtil.add(TotalComm, h.getFieldValueByFieldNameBD("nd_hfee"));

            TotalPremiBruto = BDUtil.add(h.getFieldValueByFieldNameBD("nd_pcost"), h.getFieldValueByFieldNameBD("nd_sfee"));
            TotalPremiBruto = BDUtil.add(TotalPremiBruto, h.getFieldValueByFieldNameBD("premi_total"));

            TotalPremiNetto = BDUtil.sub(TotalPremiBruto, TotalComm);
            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, h.getFieldValueByFieldNameBD("nd_disc1"));
            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, h.getFieldValueByFieldNameBD("nd_feebase1"));

            TotalOSComm = BDUtil.sub(TotalComm, h.getFieldValueByFieldNameBD("ap_comis_p"));

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_pcost").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nd_sfee").doubleValue());
            row.createCell(7).setCellValue(TotalPremiBruto.doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_feebase1").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            row.createCell(10).setCellValue(BDUtil.sub(TotalComm, TotalTax).doubleValue());
            row.createCell(11).setCellValue(TotalTax.doubleValue());
            row.createCell(12).setCellValue(TotalPremiNetto.doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("os_comm").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_OUTSTANDING_CLAIM() throws Exception {
        final boolean FLT_OUTS_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM"));
        final boolean FLT_OUTS_CLAIM_CO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_CO"));
        final boolean FLT_OUTS_CLAIM_RI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_RI"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " b.create_who,b.receipt_date,a.cc_code,a.pol_type_id,a.pol_id,a.cust_name,b.ar_cust_id as entity_id,a.policy_date,a.dla_date,a.pol_no,a.ccy_rate_claim,a.dla_no,b.no_surat_hutang as ref1,"
                + "(select d.loss_desc from ins_pol_obj b inner join ins_claim_loss d on d.claim_loss_id = b.claim_loss_id and a.claim_object_id = b.ins_pol_obj_id )as status_kerugian,"
                + "round(a.insured_amount*a.ccy_rate,0) as insured_amount,round(a.premi_total*a.ccy_rate,0) as premi_total,"
                + "round(coalesce(a.nd_pcost*a.ccy_rate,0),0) as nd_pcost,round(coalesce(a.nd_sfee*a.ccy_rate,0),0) as nd_sfee,"
                + "round(coalesce(a.claim_amount_approved*a.ccy_rate_claim,0),0) as claim_amount_approved,round(coalesce(b.amount,0),0) as amount,"
                + "round(coalesce(b.amount_settled,0),0) as ap_comis_p,(coalesce(b.amount,0)-coalesce(b.amount_settled,0)) as ap_comis, "
                + "getpremi(invoice_type = 'AP',coalesce((select sum(x.amount) from ar_invoice x where x.ar_trx_type_id = 11 and x.attr_pol_id = b.attr_pol_id),0),0) as pajak_klaim,"
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (61,111)),0) as hutklaim_adj ");

        sqa.addQuery(
                " from ar_invoice b "
                + " left join ins_policy a on a.pol_id = b.attr_pol_id "
                + " left join ent_master c on c.ent_id = a.entity_id ");

        sqa.addClause("coalesce(b.amount,0) <> 0");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");

        if (FLT_OUTS_CLAIM) {
            sqa.addClause("invoice_type in ('AP','AR')");
            sqa.addClause("ar_trx_type_id = 12");
        }

        if (FLT_OUTS_CLAIM_RI) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 14");
        }

        if (FLT_OUTS_CLAIM_CO) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 16");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
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
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
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
            sqa.addClause("b.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("c.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        String query = " b.refid1 in ( select pla_no from ( "
                + "select x.refid1 as pla_no,sum(round(coalesce(x.amount,0),2)) as amount "
                + "from ar_invoice x "
                + "left join ins_policy z on z.pol_id = x.attr_pol_id "
                //+ "where coalesce(x.amount,0) <> 0 and coalesce(x.negative_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";
                + "where coalesce(x.amount,0) <> 0 and coalesce(x.cancel_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";

        if (getAppDateFrom() != null) {
            query = query + " and date_trunc('day',x.mutation_date) >= '" + appDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getAppDateTo() != null) {
            query = query + " and date_trunc('day',x.mutation_date) <= '" + appDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (x.receipt_date is null or date_trunc('day',x.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                query = query + " and z.cc_code = '" + stBranch + "'";
//                sqa.addClause("a.cc_code = ?");
//                sqa.addPar(stBranch);

                if (stBranchSource != null) {
                    query = query + " and z.cc_code_source = '" + stBranchSource + "'";
//                    sqa.addClause("a.cc_code_source = ?");
//                    sqa.addPar(stBranchSource);
                }
                if (stRegionSource != null) {
                    query = query + " and z.region_id_source = '" + stRegionSource + "'";
//                    sqa.addClause("a.region_id_source = ?");
//                    sqa.addPar(stRegionSource);
                }
            } else {
                query = query + " and ((z.cc_code = '" + stBranch + "') or (z.cc_code = '80' and z.cc_code_source = '" + stBranch + "'))";
//                sqa.addClause("((a.cc_code = ?) or (a.cc_code = '80' and a.cc_code_source = ?))");
//                sqa.addPar(stBranch);
//                sqa.addPar(stBranch);
            }
        }

//        if (getStBranch() != null) {
//            query = query + " and z.cc_code = '" + stBranch + "'";
//            //sqa.addPar(stBranch);
//        }

        if (getStRegion() != null) {
            query = query + " and z.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyTypeGroupID() != null) {
            query = query + " and z.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'";
            //sqa.addClause("z.ins_policy_type_grp_id = ?");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            query = query + " and z.pol_type_id = '" + stPolicyTypeID + "'";
            //sqa.addClause("z.pol_type_id = ?");
            //sqa.addPar(stPolicyTypeID);
        }

        query = query + " group by x.refid1 ) a where amount <> 0 ) ";

        sqa.addClause(query);

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,b.ar_cust_id,b.no_surat_hutang ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_OUTSTANDING_CLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("LAPORAN HUTANG KLAIM");

            XSSFRow row2 = sheet.createRow(1);
            if (getAppDateFrom() != null) {
                row2.createCell(0).setCellValue(DateUtil.getDateStr(getAppDateFrom()) + " sd " + DateUtil.getDateStr(getAppDateTo()));
            } else {
                row2.createCell(0).setCellValue(" sd " + DateUtil.getDateStr(getAppDateTo()));
            }

            XSSFRow row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue(DateUtil.getDateStr(getPerDateFrom()));

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("tgl polis");
            row0.createCell(3).setCellValue("no polis");
            row0.createCell(4).setCellValue("tgl lkp");
            row0.createCell(5).setCellValue("no lkp");
            row0.createCell(6).setCellValue("nama");
            row0.createCell(7).setCellValue("nilai klaim");
            row0.createCell(8).setCellValue("klaim dibayar");
            row0.createCell(9).setCellValue("hutang klaim");
            row0.createCell(10).setCellValue("pajak klaim");
            row0.createCell(11).setCellValue("tgl bayar");
            row0.createCell(12).setCellValue("hutang klaim adj");
            row0.createCell(13).setCellValue("kerugian");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            if (h.getFieldValueByFieldNameBD("pol_type_id") != null) {
                row.createCell(1).setCellValue(getPolicyType(h.getFieldValueByFieldNameBD("pol_type_id").toString()).getStShortDescription());
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            if (h.getFieldValueByFieldNameST("dla_no") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            }
            if (h.getFieldValueByFieldNameST("cust_name") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            }
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("ap_comis_p").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ap_comis").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("pajak_klaim").doubleValue());
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("hutklaim_adj").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("status_kerugian"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_OUTSTANDING_CLAIM_OLD() throws Exception {
        final boolean FLT_OUTS_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM"));
        final boolean FLT_OUTS_CLAIM_CO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_CO"));
        final boolean FLT_OUTS_CLAIM_RI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_CLAIM_RI"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " b.create_who,b.receipt_date,a.cc_code,a.pol_type_id::text,a.pol_id,a.cust_name,b.ar_cust_id as entity_id,a.policy_date,a.dla_date,a.pol_no,a.ccy_rate_claim,a.dla_no,b.no_surat_hutang as ref1,"
                + "(select d.loss_desc from ins_pol_obj b inner join ins_claim_loss d on d.claim_loss_id = b.claim_loss_id and a.claim_object_id = b.ins_pol_obj_id )as status_kerugian,"
                + "round(a.insured_amount*a.ccy_rate,0) as insured_amount,round(a.premi_total*a.ccy_rate,0) as premi_total,"
                + "round(coalesce(a.nd_pcost*a.ccy_rate,0),0) as nd_pcost,round(coalesce(a.nd_sfee*a.ccy_rate,0),0) as nd_sfee,"
                + "round(coalesce(a.claim_amount*a.ccy_rate_claim,0),0) as claim_amount,round(coalesce(b.amount,0),0) as amount,"
                + "round(coalesce(b.amount_settled,0),0) as ap_comis_p,(coalesce(b.amount,0)-coalesce(b.amount_settled,0)) as ap_comis, "
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (57)),0) as klaimbruto,"
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (59)),0) as subro,"
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (104)),0) as feesubro, "
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id not in (57,59,104)),0) as lain, "
                + "getpremi(invoice_type = 'AP',coalesce((select sum(x.amount) from ar_invoice x where x.ar_trx_type_id = 11 and x.attr_pol_id = b.attr_pol_id),0),0) as pajak_klaim,"
                + "coalesce((select sum(x.amount) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id and x.ar_trx_line_id in (61,111)),0) as hutklaim_adj ");

        sqa.addQuery(
                " from ar_invoice b "
                + " left join ins_policy a on a.pol_id = b.attr_pol_id "
                + " left join ent_master c on c.ent_id = a.entity_id ");

        sqa.addClause("coalesce(b.amount,0) <> 0");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");

        if (FLT_OUTS_CLAIM) {
            sqa.addClause("invoice_type in ('AP','AR')");
            sqa.addClause("ar_trx_type_id = 12");
        }

        if (FLT_OUTS_CLAIM_RI) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 14");
        }

        if (FLT_OUTS_CLAIM_CO) {
            sqa.addClause("invoice_type = 'AR'");
            sqa.addClause("ar_trx_type_id = 16");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(appDateTo);
        }

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
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
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
            sqa.addClause("b.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("c.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("c.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        String query = " b.refid1 in ( select pla_no from ( "
                + "select x.refid1 as pla_no,sum(round(coalesce(x.amount,0),2)) as amount "
                + "from ar_invoice x "
                + "left join ins_policy z on z.pol_id = x.attr_pol_id "
                //+ "where coalesce(x.amount,0) <> 0 and coalesce(x.negative_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";
                + "where coalesce(x.amount,0) <> 0 and coalesce(x.cancel_flag,'') <> 'Y' and x.invoice_type in ('AP','AR') and x.ar_trx_type_id = 12 ";

        if (getAppDateFrom() != null) {
            query = query + " and date_trunc('day',x.mutation_date) >= '" + appDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getAppDateTo() != null) {
            query = query + " and date_trunc('day',x.mutation_date) <= '" + appDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (x.receipt_date is null or date_trunc('day',x.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            query = query + " and z.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and z.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyTypeGroupID() != null) {
            query = query + " and z.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'";
            //sqa.addClause("z.ins_policy_type_grp_id = ?");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            query = query + " and z.pol_type_id = '" + stPolicyTypeID + "'";
            //sqa.addClause("z.pol_type_id = ?");
            //sqa.addPar(stPolicyTypeID);
        }

        query = query + " group by x.refid1 ) a where amount <> 0 ) ";

        sqa.addClause(query);

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,b.ar_cust_id,b.no_surat_hutang ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_OUTSTANDING_CLAIM_OLD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String kodeko = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("LAPORAN HUTANG KLAIM");

            XSSFRow row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue(DateUtil.getDateStr(getAppDateFrom()) + " sd " + DateUtil.getDateStr(getAppDateTo()));

            XSSFRow row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue(DateUtil.getDateStr(getPerDateFrom()));

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("tgl polis");
            row0.createCell(3).setCellValue("no polis");
            row0.createCell(4).setCellValue("tgl lkp");
            row0.createCell(5).setCellValue("no lkp");
            row0.createCell(6).setCellValue("nama");
            row0.createCell(7).setCellValue("nilai klaim");
            row0.createCell(8).setCellValue("klaim bruto");
            row0.createCell(9).setCellValue("subro");
            row0.createCell(10).setCellValue("fee subro");
            row0.createCell(11).setCellValue("lain");
            row0.createCell(12).setCellValue("klaim dibayar");
            row0.createCell(13).setCellValue("hutang klaim");
            row0.createCell(14).setCellValue("pajak klaim");
            row0.createCell(15).setCellValue("tgl bayar");
            row0.createCell(16).setCellValue("hutang klaim adj");
            row0.createCell(17).setCellValue("kerugian");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(getPolicyType(h.getFieldValueByFieldNameST("pol_type_id")).getStShortDescription());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("subro").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("feesubro").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("lain").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ap_comis_p").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ap_comis").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("pajak_klaim").doubleValue());
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("hutklaim_adj").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("status_kerugian"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public InsurancePolicyTypeView getPolicyType(String stPolicyTypeID) {
        final InsurancePolicyTypeView polType = (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);

        return polType;
    }

    public DTOList EXCEL_RECEIPT() throws Exception {
        final boolean FLT_PAID_COINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COINS"));
        final boolean FLT_PAID_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_REINS"));
        final boolean FLT_PAID_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM"));
        final boolean FLT_PAID_CLAIM_CO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM_CO"));
        final boolean FLT_PAID_CLAIM_RI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_CLAIM_RI"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "e.create_who,e.receipt_no as description,b.receipt_date as period_start,d.cc_code,d.pol_id,d.policy_date,g.ent_name,f.attr_pol_name,"
                + "d.dla_date,d.pol_no,d.ccy_rate,d.dla_no,d.pol_type_id::text,f.ar_cust_id as entity_id,f.no_surat_hutang as cust_name,h.ref_ent_id,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=66,a.amount)),0),0) as nd_brok1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=66,a.amount_settled)),0),0) as nd_brok1pct,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=67,a.amount)),0),0) as nd_brok2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=67,a.amount_settled)),0),0) as nd_brok2pct,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=55,a.amount)),0),0) as refn1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=55,a.amount_settled)),0),0) as refn2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=71,a.amount)),0),0) as refn3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=71,a.amount_settled)),0),0) as refn4,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=72,a.amount)),0),0) as refn5,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=72,a.amount_settled)),0),0) as refn6,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=73,a.amount)),0),0) as refn7,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=73,a.amount_settled)),0),0) as refn8,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (57),a.amount)),0),0) as nd_comm1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (59),a.amount)),0),0) as nd_comm2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (104),a.amount)),0),0) as nd_comm3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id not in (57,59,104),a.amount)),0),0) as nd_comm4,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (57,58,59,60,62,63,64,65,85,86,87,88,90,91,96,103,104,105,106,110,111,112,115,117,118,119,120,122,123,134,136,137,138),a.amount_settled)),0),0) as nd_comm14,"
                //                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (57,90,104,105,106,110,115,117,119,122,123),a.amount)),0),0) as nd_comm1,"
                //                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (58,59,64,85,91,112,120),a.amount)),0),0) as nd_comm2,"
                //                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (60,62,63,65,96,103,111,118,134,136,137,138),a.amount)),0),0) as nd_comm3,"
                //                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id in (57,90,104,105,106,110,115,117,119,122,123,58,59,64,85,91,112,120,60,62,63,65,96,103,111,118,134,136,137,138),a.amount_settled)),0),0) as nd_comm4,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=68,a.amount)),0),0) as nd_taxcomm1,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=68,a.amount_settled)),0),0) as nd_taxcomm2,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=69,a.amount)),0),0) as nd_taxcomm3,"
                + "round(coalesce(sum(getpremi2(a.ar_trx_line_id=69,a.amount_settled)),0),0) as nd_taxcomm4 ");

        sqa.addQuery(
                " from ar_invoice_details a "
                + " inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id "
                + " left join ins_policy d on d.pol_id = b.pol_id "
                + " left join ar_receipt e on e.ar_receipt_id = b.receipt_id"
                + " left join ar_invoice f on f.ar_invoice_id = b.ar_invoice_id "
                + " left join ent_master g on g.ent_id = d.entity_id "
                + " left join ent_master h on h.ent_id = f.ent_id ");

        sqa.addClause("e.status = 'POST'");

        if (FLT_PAID_REINS) {
            sqa.addClause("e.ar_settlement_id = 9");
        }

        if (FLT_PAID_CLAIM) {
            sqa.addClause("e.ar_settlement_id in (10,40)");
        }

        if (FLT_PAID_CLAIM_RI) {
            sqa.addClause("e.ar_settlement_id = 11");
        }

        if (FLT_PAID_COINS) {
            sqa.addClause("e.ar_settlement_id = 13");
        }

        if (FLT_PAID_CLAIM_CO) {
            sqa.addClause("e.ar_settlement_id = 14");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',d.claim_approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',d.claim_approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',d.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',d.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCoinsurerID != null) {
            sqa.addClause("f.ar_cust_id = ?");
            sqa.addPar(stCoinsurerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("d.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("e.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stReinsName != null) {
            sqa.addClause("f.no_surat_hutang = ?");
            sqa.addPar(stReinsName);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("f.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
            sqa.addClause("f.no_surat_hutang = ?");
            sqa.addPar(stKreasiName);
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("d.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(d.pla_no like ? or d.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
        }

        if (stCustCategory1 != null) {
            sqa.addClause("g.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        final String sql = sqa.getSQL()
                + " group by e.create_who,e.receipt_no,b.receipt_date,d.cc_code,d.pol_id,d.policy_date,d.dla_date,d.pol_no,d.ccy_rate,f.ar_cust_id,"
                + " d.dla_no,d.pol_type_id,f.no_surat_hutang,h.ref_ent_id,g.ent_name,f.attr_pol_name "
                + "order by b.receipt_date,d.pol_no,f.no_surat_hutang ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RECEIPT_CLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String kodeko = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("nobuk");
            row0.createCell(3).setCellValue("receipt date");
            row0.createCell(4).setCellValue("tgl polis");
            row0.createCell(5).setCellValue("no polis");
            row0.createCell(6).setCellValue("tgl klaim");
            row0.createCell(7).setCellValue("no lkp");
            row0.createCell(8).setCellValue("klaim bruto");
            row0.createCell(9).setCellValue("subro");
            row0.createCell(10).setCellValue("fee subro");
            row0.createCell(11).setCellValue("lain");
            row0.createCell(12).setCellValue("claim paid");
            row0.createCell(13).setCellValue("user");

            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_type_id"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nd_comm3").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("nd_comm4").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("nd_comm14").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("create_who"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void EXPORT_RECEIPT_CLAIMCO() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String kodeko = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("nobuk");
            row0.createCell(3).setCellValue("receipt date");
            row0.createCell(4).setCellValue("tgl polis");
            row0.createCell(5).setCellValue("no polis");
            row0.createCell(6).setCellValue("tgl klaim");
            row0.createCell(7).setCellValue("no lkp");
            row0.createCell(8).setCellValue("claim CO");
            row0.createCell(9).setCellValue("claim CO paid");
            row0.createCell(10).setCellValue("user");

            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_type_id"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_taxcomm3").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_taxcomm4").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("create_who"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList PANJARKLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.cc_code,a.pol_type_id,b.receipt_no as receipt_no2,b.receipt_date as mutation_date,"
                + "d.receipt_date,a.claim_approved_date,a.entity_id,d.receipt_no,"
                + "d.ar_invoice_claim as dla_no,a.pol_no,d.amount_per_line as premi_total ");

        sqa.addQuery(" from ar_invoice b "
                + "left join ins_policy a on a.pol_id = b.attr_pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ar_receipt_lines d on d.pol_id = b.attr_pol_id "
                + "left join ar_receipt f on f.ar_receipt_id = d.receipt_id ");

        //sqa.addClause("(coalesce(f.negative_flag,'') <> 'Y' and coalesce(f.posted_flag,'Y') = 'Y')");
        sqa.addClause("(coalesce(f.cancel_flag,'') <> 'Y' and coalesce(f.posted_flag,'Y') = 'Y')");
        sqa.addClause("b.invoice_type in ('AP','AR')");
        sqa.addClause("b.ar_trx_type_id = 12");
        sqa.addClause("b.refid0 = 'PANJAR'");
        sqa.addClause("coalesce(d.amount_per_line,0) <> 0");
        sqa.addClause("d.line_type = 'INVOC'");
        sqa.addClause("d.advance_payment_flag = 'Y'");

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',d.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',d.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
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
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("b.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
            sqa.addPar('%' + stDlaNo + '%');
        }

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,a.pol_no,d.ar_invoice_claim ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_PANJARKLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.ar_invoice_id,b.attr_pol_id,a.cc_code,a.pol_type_id::text,b.receipt_no as receipt_no2,b.receipt_date as mutation_date,a.claim_approved_date,d.receipt_date,"
                + "a.entity_id::text, d.ar_invoice_claim as dla_no,a.claim_approved_date,a.pol_no, d.amount_per_line as premi_total,d.receipt_no,"
                + "getpremi(b.invoice_type = 'AP',coalesce((select sum(x.amount) from ar_invoice x where x.ar_trx_type_id = 11 and x.attr_pol_id = b.attr_pol_id),0),0) as pajak_klaim ");

        sqa.addQuery(" from ar_invoice b "
                + "left join ins_policy a on a.pol_id = b.attr_pol_id "
                + "left join ent_master c on c.ent_id = a.entity_id "
                + "left join ar_receipt_lines d on d.pol_id = b.attr_pol_id "
                + "left join ar_receipt f on f.ar_receipt_id = d.receipt_id ");

        //sqa.addClause("(coalesce(f.negative_flag,'') <> 'Y' and coalesce(f.posted_flag,'Y') = 'Y')");
        sqa.addClause("(coalesce(f.cancel_flag,'') <> 'Y' and coalesce(f.posted_flag,'Y') = 'Y')");
        sqa.addClause("b.invoice_type in ('AP','AR')");
        sqa.addClause("b.ar_trx_type_id = 12");
        sqa.addClause("b.refid0 = 'PANJAR'");
        sqa.addClause("coalesce(d.amount_per_line,0) <> 0");
        sqa.addClause("d.line_type = 'INVOC'");
        sqa.addClause("d.advance_payment_flag = 'Y'");

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',d.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',d.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
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
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("b.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
            sqa.addPar('%' + stDlaNo + '%');
        }

        final String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,a.pol_no,d.ar_invoice_claim ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_PANJARKLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String nobuk = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("LAPORAN PANJAR KLAIM");

            XSSFRow row2 = sheet.createRow(1);
            if (getPaymentDateFrom() != null) {
                row2.createCell(0).setCellValue("Tanggal Bayar : " + DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));
            }

            XSSFRow row3 = sheet.createRow(2);
            if (getPerDateFrom() != null) {
                row3.createCell(0).setCellValue("Per Tanggal : " + DateUtil.getDateStr(getPerDateFrom()));
            }

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("KODA");
            row0.createCell(1).setCellValue("COB");
            row0.createCell(2).setCellValue("TGL BAYAR");
            row0.createCell(3).setCellValue("BANK");
            row0.createCell(4).setCellValue("NOBUK");
            row0.createCell(5).setCellValue("TGL SETUJUI KLAIM");
            row0.createCell(6).setCellValue("LKS/LKP");
            row0.createCell(7).setCellValue("NO POLIS");
            row0.createCell(8).setCellValue("JUMLAH");
            row0.createCell(9).setCellValue("NOBUK REALISASI");
            row0.createCell(10).setCellValue("TGL REALISASI");

            if (h.getFieldValueByFieldNameDT("mutation_date") != null) {
                nobuk = h.getFieldValueByFieldNameST("receipt_no2");
            } else {
                nobuk = null;
            }

            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(getPolicyType(h.getFieldValueByFieldNameST("pol_type_id")).getStShortDescription());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            row.createCell(3).setCellValue(getEntity2(h.getFieldValueByFieldNameST("entity_id")).getStEntityName());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            if (nobuk != null) {
                row.createCell(9).setCellValue(nobuk);
            }
            if (h.getFieldValueByFieldNameDT("mutation_date") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public EntityView getEntity2(String stEntityID) {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return entity;
    }

    public DTOList OUTSTANDINGPREMI() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.cc_code,e.ent_id::text,e.ent_name,(substr(a.refid1,6,1)::numeric+1) as sppa_no,"
                + "a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as entity_id,"
                + "a.mutation_date as policy_date,a.due_date as claim_date,a.receipt_date,a.ccy_rate,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        //sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'";
                //sqa.addPar(policyDateFrom);
            } else {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        /*NonAKS*/
//        if (getStBranch() != null) {
//            query = query + " and a.cc_code = '" + stBranch + "'";
//            //sqa.addPar(stBranch);
//        }
//
//        if (getStRegion() != null) {
//            query = query + " and b.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                query = query + " and b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    query = query + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    query = query + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                query = query + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                query = query + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                query = query + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                query = query + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        /*NonAKS*/
//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = ? ");
//            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("b.region_id = ? ");
//            sqa.addPar(stRegion);
//        }

        if (stEntityID != null) {
            sqa.addClause("a.ent_id = ? ");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = ? ");
            sqa.addPar(stMarketerID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("f.ins_policy_type_grp_id = ? ");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("f.pol_type_id = ? ");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ? ");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ? ");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ? ");
            sqa.addPar(stCustCategory1);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
//            sqa.addPar(stBussinessPolType);
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        String sql = "select * from ( " + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id,substr(a.refid1,6,1),"
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                //+ " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.pol_no,a.sppa_no,a.entity_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_OUTSTANDINGPREMI() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,b.cc_code_source,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,b.prod_name as marketer_ext,"
                + "a.mutation_date,a.due_date,a.receipt_no,a.receipt_date,h.vs_description as trx_method,a.ccy_rate,substr(a.refid1,6,2) as inst_o,b.no as inst,b.inst_date,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,b.pol_type_id,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.attr_pol_id) as jumlah,"
                + "(select string_agg(trim(x.ref1),'|')||'|'||string_agg(trim(x.ref4),'|')||'|'||string_agg(trim(x.ref16),'|') from ins_pol_obj x where x.pol_id = a.attr_pol_id) as debitur,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                //                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who,b.period_start,b.period_end,b.marketing_officer_who ");

        String installment = "select row_number() over(partition by c.policy_id order by c.ins_pol_inst_id) as no,b.pol_id,b.pol_no,b.entity_id,b.period_start,b.period_end, "
                + "b.prod_id,b.ref5,b.create_who,c.inst_date,b.ins_policy_type_grp_id,b.pol_type_id,b.cc_code,b.region_id,e.ent_name as marketing_officer_who,b.prod_name, "
                + "b.cc_code_source,b.region_id_source,b.payment_company_id "
                + "from ins_policy b "
                + "left join ins_pol_installment c on c.policy_id = b.pol_id "
                + "left join ent_master e on e.ent_id = b.marketing_officer_who::int "
                + "where b.status in ('POLICY','ENDORSE','RENEWAL') and b.active_flag='Y' and b.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + policyDateFrom + "'";
            }
        } else {
            installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            installment = installment + " and date_trunc('day',b.approved_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                installment = installment + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    installment = installment + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    installment = installment + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                installment = installment + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            installment = installment + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                installment = installment + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                installment = installment + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                installment = installment + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                installment = installment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                installment = installment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyTypeGroupID() != null) {
            installment = installment + " and b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'";
            //sqa.addPar(stBranch);
        }

        if (getStPolicyTypeID() != null) {
            installment = installment + " and b.pol_type_id = '" + stPolicyTypeID + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            installment = installment + " and b.pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStMarketerOffID() != null) {
            installment = installment + " and b.marketing_officer_who = '" + stMarketerOffID + "'";
//            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        installment = installment + " order by c.ins_pol_inst_id ";

        String cekpolis = "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a inner join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + "inner join ent_master e on e.ent_id = b.entity_id "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            cekpolis = cekpolis + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                cekpolis = cekpolis + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    cekpolis = cekpolis + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    cekpolis = cekpolis + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                cekpolis = cekpolis + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            cekpolis = cekpolis + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                cekpolis = cekpolis + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                cekpolis = cekpolis + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                cekpolis = cekpolis + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                cekpolis = cekpolis + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                cekpolis = cekpolis + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyNo() != null) {
            cekpolis = cekpolis + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStCompanyID() != null) {
            cekpolis = cekpolis + " and e.ref2 = '" + stCompanyID + "'";
            //sqa.addPar(stCompanyID);
        }

        if (getStCustCategory1() != null) {
            cekpolis = cekpolis + " and e.category1 = '" + stCustCategory1 + "'";
            //sqa.addPar(stCustCategory1);
        }

        cekpolis = cekpolis + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ";


        sqa.addQuery(
                " from ar_invoice a "
                //                + " left join ins_policy c on c.pol_id = a.attr_pol_id "
                + " left join ( " + installment + " ) b on b.pol_id = a.attr_pol_id and b.no = substr(a.refid1,6,2)::numeric+1"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //                + " inner join ent_master e on e.ent_id = a.ent_id "
                + " inner join ent_master e on e.ent_id = b.entity_id "
                + " inner join ent_master g on g.ent_id = b.prod_id "
                + " inner join ( " + cekpolis + " ) i on i.pol_no = substr(a.attr_pol_no,1,16) "
                + " left join s_valueset h on h.vs_code = a.trx_method and h.vs_group = 'RECEIPT_TRX_METHOD' ");

        sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        //sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

//        String query = " substr(a.attr_pol_no,1,16) in ( "
//                + "select pol_no from ( "
//                + "select substr(a.attr_pol_no,1,16) as pol_no,"
//                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
//                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
//                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
//                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
//                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
//                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
//                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
//                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
//                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
//                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
//                + "inner join ent_master e on e.ent_id = a.ent_id "
//                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
//                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
//                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";
//
//        if (getPolicyDateFrom() != null) {
//            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
//                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
//                //sqa.addPar(policyDateFrom);
//            } else {
//                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            }
//        } else {
//            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (getPolicyDateTo() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            //sqa.addPar(policyDateTo);
//        }
//
//        if (getPerDateFrom() != null) {
//            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
//            //sqa.addPar(perDateFrom);
//        }
//
//        if (getStBranch() != null) {
//            query = query + " and a.cc_code = '" + stBranch + "'";
//            //sqa.addPar(stBranch);
//        }
//
//        if (getStRegion() != null) {
//            query = query + " and b.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }
//
//        if (getStPolicyNo() != null) {
//            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
//            //sqa.addPar('%'+stPolicyNo+'%');
//        }
//
//        if (getStCompanyID() != null) {
//            query = query + " and e.ref2 = '" + stCompanyID + "'";
//            //sqa.addPar(stCompanyID);
//        }
//
//        if (getStCustCategory1() != null) {
//            query = query + " and e.category1 = '" + stCustCategory1 + "'";
//            //sqa.addPar(stCustCategory1);
//        }
//
//        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";
//
//        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

////        if (perDateFrom != null) {
////            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
////            //sqa.addPar(policyDateFrom);
////        }
////
////        if (perDateFrom != null) {
////            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + perDateFrom + "'");
////            //sqa.addPar(policyDateTo);
////        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,';'||a.cc_code_source as koda_nonaks,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.inst_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tgl_bayar,';'||a.attr_pol_no as nopol,a.inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,';'||a.create_who as createwho,a.period_start,a.period_end,a.marketing_officer_who,marketer_ext, "
                + " cekstatus(a.pol_type_id = 59 and a.jumlah=1,debitur) as debitur,a.trx_method "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,h.vs_description,a.cc_code,b.cc_code_source,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,b.prod_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,b.no,b.inst_date,b.pol_type_id,"
                + " b.period_start,b.period_end,b.marketing_officer_who order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,a.ar_invoice_id,a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100_" + System.currentTimeMillis() + ".csv";

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

    public void EXCEL_OUTSTANDINGPREMI_XLS() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,"
                + "a.mutation_date,a.due_date,a.receipt_date,a.ccy_rate,b.no as inst,b.inst_date,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                //+ "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                //+ "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                //+ "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid ");

        String installment = "select row_number() over(partition by c.policy_id order by c.ins_pol_inst_id) as no,b.pol_id,b.pol_no,b.entity_id,b.prod_id,b.ref5,b.create_who,c.inst_date "
                + "from ins_policy b "
                + "left join ins_pol_installment c on c.policy_id = b.pol_id "
                + "where b.status in ('POLICY','ENDORSE','RENEWAL') and b.active_flag='Y' and b.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + policyDateFrom + "'";
            }
        } else {
            installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            installment = installment + " and date_trunc('day',b.approved_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getStBranch() != null) {
            installment = installment + " and b.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            installment = installment + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            installment = installment + " and b.pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        installment = installment + " order by c.ins_pol_inst_id ";

        sqa.addQuery(
                " from ar_invoice a "
                //+ " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " left join ( " + installment + " ) b on b.pol_id = a.attr_pol_id and b.no = substr(a.refid1,6,2)::numeric+1"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master f on f.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        //sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            query = query + " and a.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

////        if (perDateFrom != null) {
////            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
////            //sqa.addPar(policyDateFrom);
////        }
////
////        if (perDateFrom != null) {
////            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + perDateFrom + "'");
////            //sqa.addPar(policyDateTo);
////        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.inst_date as tgl_jtempo,a.receipt_date as tgl_bayar,';'||a.attr_pol_no as nopol,a.inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto "
                + " from ( " + sqa.getSQL() + " group by a.receipt_date,a.cc_code,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.no,b.inst_date "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,substr(a.refid1,6,1),a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100_" + System.currentTimeMillis() + ".csv";

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

    public void EXPORT_OUTSTANDINGPREMI_WITHAGING() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("detil");
        XSSFSheet sheet2 = wb.createSheet("per cabang");
        XSSFSheet sheet3 = wb.createSheet("per cob");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        final DTOList list3 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("polid");
            row0.createCell(2).setCellValue("tglpolis");
            row0.createCell(3).setCellValue("tgl_jtempo");
            row0.createCell(4).setCellValue("nobuk");
            row0.createCell(5).setCellValue("tgl_bayar");
            row0.createCell(6).setCellValue("nopol");
            row0.createCell(7).setCellValue("inst");
            row0.createCell(8).setCellValue("kodeko");
//            row0.createCell(9).setCellValue("nama_bank");
//            row0.createCell(10).setCellValue("sumbis");
//            row0.createCell(11).setCellValue("marketer_name");
//            row0.createCell(12).setCellValue("principal");
            row0.createCell(9).setCellValue("premi_bruto");
            row0.createCell(10).setCellValue("biapol");
            row0.createCell(11).setCellValue("biamat");
            row0.createCell(12).setCellValue("feebase");
            row0.createCell(13).setCellValue("diskon");
            row0.createCell(14).setCellValue("ppn");
            row0.createCell(15).setCellValue("komisi_excl_tax");
            row0.createCell(16).setCellValue("tax_komisi");
            row0.createCell(17).setCellValue("hfee_excl_tax");
            row0.createCell(18).setCellValue("tax_hfee");
            row0.createCell(19).setCellValue("bfee_excl_tax");
            row0.createCell(20).setCellValue("tax_bfee");
            row0.createCell(21).setCellValue("tag_netto");

            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("polid").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tglpolis"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tgl_jtempo"));
            if (h.getFieldValueByFieldNameST("nobuk") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_bayar") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tgl_bayar"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("nopol"));
            if (h.getFieldValueByFieldNameBD("inst") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("inst").doubleValue());
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
//            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
//            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
//            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("marketer_name"));
//            if (h.getFieldValueByFieldNameST("principal") != null) {
//                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("principal"));
//            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_bruto").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("biapol").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("biamat").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("feebase").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("komisi_excl_tax").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("tax_komisi").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("hfee_excl_tax").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("tax_hfee").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("bfee_excl_tax").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("tax_bfee").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("tag_netto").doubleValue());
        }

        for (int j = 0; j < list2.size(); j++) {
            HashDTO h = (HashDTO) list2.get(j);

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("<= 60 hari");
            row0.createCell(2).setCellValue("> 60 hari");

            XSSFRow row = sheet2.createRow(j + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("kurang").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("lebih").doubleValue());
        }

        for (int k = 0; k < list3.size(); k++) {
            HashDTO h = (HashDTO) list3.get(k);

            //bikin header
            XSSFRow row0 = sheet3.createRow(0);
            row0.createCell(0).setCellValue("cob");
            row0.createCell(1).setCellValue("<= 60 hari");
            row0.createCell(2).setCellValue("> 60 hari");

            XSSFRow row = sheet3.createRow(k + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("poltype").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("kurang").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("lebih").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXCEL_PREMIRECEIPT() throws Exception {
        final boolean FLT_PAID_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_PREMI"));
        final boolean FLT_PAID_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COMM"));
        final boolean FLT_PAID_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_TAX"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "f.create_who,d.cc_code,f.receipt_no,f.receipt_date,d.policy_date,g.ent_name,d.ccy_rate,(substr(c.refid1,6,1)::numeric+1) as inst, "
                + "d.pol_no,round((d.premi_total*d.ccy_rate),0) as premi_total,round((d.premi_netto*d.ccy_rate),0) as premi_netto,round((d.total_due*d.ccy_rate),0) as total_due,"
                + "round(coalesce((d.nd_pcost*d.ccy_rate),0),0) as nd_pcost,round(coalesce((d.nd_sfee*d.ccy_rate),0),0) as nd_sfee,round(coalesce((d.nd_ppn*d.ccy_rate),0),0) as nd_ppn, "
                + "(round(coalesce((d.nd_disc1*d.ccy_rate),0),0) + round(coalesce((d.nd_disc2*d.ccy_rate),0),0)) as nd_disc1,"
                + "(round(coalesce((d.nd_comm1*d.ccy_rate),0),0) + round(coalesce((d.nd_comm2*d.ccy_rate),0),0)) as nd_comm1,"
                + "(round(coalesce((d.nd_comm3*d.ccy_rate),0),0) + round(coalesce((d.nd_comm4*d.ccy_rate),0),0)) as nd_comm2,"
                + "(round(coalesce((d.nd_brok1*d.ccy_rate),0),0) + round(coalesce((d.nd_brok2*d.ccy_rate),0),0)) as nd_brok1,"
                + "(round(coalesce((d.nd_hfee*d.ccy_rate),0),0)) as nd_hfee,"
                + "(round(coalesce((d.nd_feebase1*d.ccy_rate),0),0) + round(coalesce((d.nd_feebase2*d.ccy_rate),0),0)) as nd_feebase1,"
                + "(round(coalesce((d.nd_taxcomm1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm2*d.ccy_rate),0),0)) as nd_taxcomm1,"
                + "(round(coalesce((d.nd_taxcomm3*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm4*d.ccy_rate),0),0)) as nd_taxcomm2,"
                + "(round(coalesce((d.nd_taxbrok1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxbrok2*d.ccy_rate),0),0)) as nd_taxbrok1,"
                + "(round(coalesce((d.nd_taxhfee*d.ccy_rate),0),0)) as nd_taxhfee,"
                + "sum(getpremi2(e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id and b.ar_invoice_dtl_root_id is not null "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = d.entity_id "
                + "left join ent_master h on h.ent_id = d.prod_id ");

        sqa.addClause("f.status = 'POST'");

        if (FLT_PAID_PREMI) {
            sqa.addClause("f.ar_settlement_id = 1");
        }

        if (FLT_PAID_COMM) {
            sqa.addClause("f.ar_settlement_id = 2");
        }

        if (FLT_PAID_TAX) {
            sqa.addClause("f.ar_settlement_id = 8");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= '" + paymentDateFrom + "'");
            //sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= '" + paymentDateTo + "'");
            //sqa.addPar(paymentDateTo);
        }

        if (createDateFrom != null) {
            sqa.addClause("date_trunc('day',f.create_date) >= '" + createDateFrom + "'");
            //sqa.addPar(createDateFrom);
        }

        if (createDateTo != null) {
            sqa.addClause("date_trunc('day',f.create_date) <= '" + createDateTo + "'");
            //sqa.addPar(createDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = " + stRegion);
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("d.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("f.receipt_no like '%" + stReceiptNo + "%'");
            //sqa.addPar('%'+stReceiptNo+'%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("e.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else {
                sqa.addClause("e.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            }
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("c.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (stCustCategory1 != null) {
            sqa.addClause("g.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        String sql = null;
        String nama_file = null;

        if (FLT_PAID_PREMI) {
            sql = "select tglbayar,nobuk,tglpol,nopol,inst,premi,bia_adm,tag_bruto,feebase,ppn,diskon,(komisi-tax_komisi) as komisi,tax_komisi,"
                    + " ((tag_bruto)-(((feebase+ppn)+(diskon))+(komisi-tax_komisi))) as premi_netto,"
                    + " premi_dibayar,diskon_dibayar,komisi_dibayar,ap_fbase_p as feebase_dibayar,ap_ppn_p as ppn_dibayar,ap_tax_p as pajak_dibayar,"
                    + " ((komisi-tax_komisi)-ap_comis_p) as oscomm,os_feebase as osfeebase,(tax_komisi-ap_tax_p) as ostaxcomm,nama_bank"
                    + " from ( "
                    + " select a.receipt_date as tglbayar,a.receipt_no as nobuk,a.policy_date as tglpol,a.inst,';'||a.pol_no as nopol,a.premi_total as premi,(a.nd_sfee+a.nd_pcost) as bia_adm,"
                    + " ((a.nd_sfee+a.nd_pcost)+a.premi_total) as tag_bruto,a.nd_ppn as ppn,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,((nd_hfee+nd_brok1)+(a.nd_comm1+nd_comm2)) as komisi,"
                    + " (nd_feebase1-ap_fbase_p) as os_feebase,a.ap_fbase_p,a.ap_comis_p,a.ap_tax_p,ap_ppn_p,((nd_taxhfee+nd_taxbrok1)+(a.nd_taxcomm1+nd_taxcomm2)) as tax_komisi,a.premi_netto as premi_netto,"
                    + " ((ap_sfee_p+ap_pcost_p)+premi_paid) as premi_dibayar,ap_disc_p as diskon_dibayar,((ap_hfee_p+ap_bfee_p)+ap_comis_p) as komisi_dibayar, a.ent_name as nama_bank "
                    + " from ( " + sqa.getSQL()
                    + " group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,g.ent_name,d.cc_code,d.ccy_rate,d.nd_ppn,substr(c.refid1,6,1), "
                    + " d.pol_no,d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,d.nd_comm4,d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,"
                    + " d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee,d.nd_feebase1,d.nd_feebase2 "
                    + " order by f.receipt_date,f.receipt_no,d.pol_no ";

            sql = sql + " ) a ) a order by a.tglbayar,a.nobuk,a.nopol,a.inst ";

            nama_file = "premi_receipt_" + System.currentTimeMillis() + ".csv";

        }

        if (FLT_PAID_COMM) {
            sql = "select tglbayar,nobuk,tglpol,nopol,premi,bia_adm,tag_bruto,feebase,ppn,diskon,(komisi-tax_komisi) as komisi,tax_komisi,"
                    + " ((tag_bruto)-(((feebase+ppn)+(diskon))+(komisi-tax_komisi))) as premi_netto,"
                    + " komisi_dibayar,fbase_dibayar,bfee_dibayar,hfee_dibayar,"
                    + " ((komisi-tax_komisi)-ap_comis_p) as oscomm,(tax_komisi-ap_tax_p) as ostaxcomm,os_feebase,nama_bank"
                    + " from ( "
                    + " select a.receipt_date as tglbayar,a.receipt_no as nobuk,a.policy_date as tglpol,';'||a.pol_no as nopol,a.premi_total as premi,(a.nd_sfee+a.nd_pcost) as bia_adm,"
                    + " ((a.nd_sfee+a.nd_pcost)+a.premi_total) as tag_bruto,a.nd_ppn as ppn,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,((nd_hfee+nd_brok1)+(a.nd_comm1+nd_comm2)) as komisi,"
                    + " (nd_feebase1-ap_fbase_p) as os_feebase,a.ap_comis_p,a.ap_tax_p,((nd_taxhfee+nd_taxbrok1)+(a.nd_taxcomm1+nd_taxcomm2)) as tax_komisi,a.premi_netto as premi_netto,"
                    + " ((ap_sfee_p+ap_pcost_p)+premi_paid) as premi_dibayar,ap_hfee_p as hfee_dibayar,ap_bfee_p as bfee_dibayar,ap_fbase_p as fbase_dibayar,ap_comis_p as komisi_dibayar, a.ent_name as nama_bank "
                    + " from ( " + sqa.getSQL()
                    + " group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,g.ent_name,d.cc_code,d.ccy_rate,d.nd_ppn,substr(c.refid1,6,1),"
                    + " d.pol_no,d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,d.nd_comm4,d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,"
                    + " d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee,d.nd_feebase1,d.nd_feebase2 "
                    + " order by f.receipt_date,f.receipt_no,d.pol_no ";

            sql = sql + " ) a ) a order by a.tglbayar,a.nobuk,a.nopol ";

            nama_file = "comm_receipt_" + System.currentTimeMillis() + ".csv";

        }

        if (FLT_PAID_TAX) {
            sql = "select tglbayar,nobuk,tglpol,nopol,premi,bia_adm,tag_bruto,feebase,ppn,diskon,(komisi-tax_komisi) as komisi,tax_komisi,"
                    + " ((tag_bruto)-(((feebase+ppn)+(diskon))+(komisi-tax_komisi))) as premi_netto,"
                    + "ap_tax_p as pajak_dibayar,(tax_komisi-ap_tax_p) as ostaxcomm,nama_bank from ( "
                    + " select a.receipt_date as tglbayar,a.receipt_no as nobuk,a.policy_date as tglpol,';'||a.pol_no as nopol,a.premi_total as premi,(a.nd_sfee+a.nd_pcost) as bia_adm,"
                    + " ((a.nd_sfee+a.nd_pcost)+a.premi_total) as tag_bruto,a.nd_ppn as ppn,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,((nd_hfee+nd_brok1)+(a.nd_comm1+nd_comm2)) as komisi,"
                    + " (nd_feebase1-ap_fbase_p) as os_feebase,a.ap_comis_p,a.ap_tax_p,((nd_taxhfee+nd_taxbrok1)+(a.nd_taxcomm1+nd_taxcomm2)) as tax_komisi,a.premi_netto as premi_netto,"
                    + " ((ap_sfee_p+ap_pcost_p)+premi_paid) as premi_dibayar,((ap_hfee_p+ap_bfee_p)+(ap_fbase_p+ap_comis_p)) as komisi_dibayar, a.ent_name as nama_bank "
                    + " from ( " + sqa.getSQL()
                    + " group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,g.ent_name,d.cc_code,d.ccy_rate,d.nd_ppn,substr(c.refid1,6,1),"
                    + " d.pol_no,d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,d.nd_comm4,d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,"
                    + " d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee,d.nd_feebase1,d.nd_feebase2 "
                    + " order by f.receipt_date,f.receipt_no,d.pol_no ";

            sql = sql + " ) a ) a order by a.tglbayar,a.nobuk,a.nopol ";

            nama_file = "tax_receipt_" + System.currentTimeMillis() + ".csv";

        }

        SQLUtil S = new SQLUtil();

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

    public void EXPORT_PREMIRECEIPT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal TotalPremiBruto = null;
        BigDecimal TotalBiaMatPol = null;
        BigDecimal TotalComm = null;
        BigDecimal TotalTax = null;
        BigDecimal TotalPremiPaid = null;
        BigDecimal OutstandingComm = null;
        BigDecimal OutstandingTaxComm = null;
        BigDecimal OutstandingFeebase = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            if (getPolicyDateFrom() != null) {
                row1.createCell(0).setCellValue("Tanggal Polis : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));
            }

            XSSFRow row2 = sheet.createRow(1);
            if (getPaymentDateFrom() != null) {
                row2.createCell(0).setCellValue("Tanggal Bayar : " + DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));
            }

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("tgl bayar");
            row0.createCell(1).setCellValue("no bukti");
            row0.createCell(2).setCellValue("tgl polis");
            row0.createCell(3).setCellValue("no polis");
            row0.createCell(4).setCellValue("premi");
            row0.createCell(5).setCellValue("biaya adm");
            row0.createCell(6).setCellValue("tag bruto");
            row0.createCell(7).setCellValue("feebase");
            row0.createCell(8).setCellValue("diskon");
            row0.createCell(9).setCellValue("komisi");
            row0.createCell(10).setCellValue("pajak komisi");
            row0.createCell(11).setCellValue("premi netto");
            row0.createCell(12).setCellValue("premi dibayar");
            row0.createCell(13).setCellValue("os komisi");
            row0.createCell(14).setCellValue("os pajak");
            row0.createCell(15).setCellValue("os feebase");
            row0.createCell(16).setCellValue("tertanggung");

            TotalTax = BDUtil.add(h.getFieldValueByFieldNameBD("nd_taxcomm1"), h.getFieldValueByFieldNameBD("nd_taxcomm2"));
            TotalTax = BDUtil.add(TotalTax, h.getFieldValueByFieldNameBD("nd_taxbrok1"));
            TotalTax = BDUtil.add(TotalTax, h.getFieldValueByFieldNameBD("nd_taxhfee"));

            TotalComm = BDUtil.add(h.getFieldValueByFieldNameBD("nd_comm1"), h.getFieldValueByFieldNameBD("nd_comm2"));
            TotalComm = BDUtil.add(TotalComm, h.getFieldValueByFieldNameBD("nd_brok1"));
            TotalComm = BDUtil.add(TotalComm, h.getFieldValueByFieldNameBD("nd_hfee"));

            TotalPremiBruto = BDUtil.add(h.getFieldValueByFieldNameBD("nd_pcost"), h.getFieldValueByFieldNameBD("nd_sfee"));
            TotalPremiBruto = BDUtil.add(TotalPremiBruto, h.getFieldValueByFieldNameBD("premi_total"));

            TotalBiaMatPol = BDUtil.add(h.getFieldValueByFieldNameBD("nd_pcost"), h.getFieldValueByFieldNameBD("nd_sfee"));

            TotalPremiPaid = BDUtil.add(h.getFieldValueByFieldNameBD("premi_paid"), h.getFieldValueByFieldNameBD("ap_sfee_p"));
            TotalPremiPaid = BDUtil.add(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_pcost_p"));
            TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_disc_p"));
            TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_hfee_p"));
            TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_bfee_p"));
            TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_fbase_p"));
            TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_comis_p"));
            TotalPremiPaid = BDUtil.add(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_tax_p"));

            OutstandingComm = BDUtil.sub(BDUtil.sub(TotalComm, TotalTax), h.getFieldValueByFieldNameBD("ap_comis_p"));
            OutstandingTaxComm = BDUtil.sub(TotalTax, h.getFieldValueByFieldNameBD("ap_tax_p"));
            OutstandingFeebase = BDUtil.sub(h.getFieldValueByFieldNameBD("nd_feebase1"), h.getFieldValueByFieldNameBD("ap_fbase_p"));

            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            if (h.getFieldValueByFieldNameST("description") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            if (TotalBiaMatPol != null) {
                row.createCell(5).setCellValue(TotalBiaMatPol.doubleValue());
            }
            if (TotalPremiBruto != null) {
                row.createCell(6).setCellValue(TotalPremiBruto.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_feebase1") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("nd_feebase1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_disc1") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            }
            if (TotalComm != null) {
                row.createCell(9).setCellValue(TotalComm.doubleValue());
            }
            if (TotalTax != null) {
                row.createCell(10).setCellValue(TotalTax.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_netto") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_netto").doubleValue());
            }
            if (TotalPremiPaid != null) {
                row.createCell(12).setCellValue(TotalPremiPaid.doubleValue());
            }
            if (OutstandingComm != null) {
                row.createCell(13).setCellValue(OutstandingComm.doubleValue());
            }
            if (OutstandingTaxComm != null) {
                row.createCell(14).setCellValue(OutstandingTaxComm.doubleValue());
            }
            if (OutstandingFeebase != null) {
                row.createCell(15).setCellValue(OutstandingFeebase.doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ent_name") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList REALISASI_PREMIRECEIPT() throws Exception {
        final boolean FLT_PAID_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_PREMI"));
        final boolean FLT_PAID_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "f.create_who,d.cc_code,f.receipt_no as description,f.receipt_date as period_start,d.policy_date,d.cust_name,d.ccy_rate,"
                + "d.pol_no,round((d.premi_total*d.ccy_rate),0) as premi_total,round((d.premi_netto*d.ccy_rate),0) as premi_netto,round((d.total_due*d.ccy_rate),0) as total_due,"
                + "round(coalesce((d.nd_pcost*d.ccy_rate),0),0) as nd_pcost,round(coalesce((d.nd_sfee*d.ccy_rate),0),0) as nd_sfee,round(coalesce((d.nd_ppn*d.ccy_rate),0),0) as nd_ppn, "
                + "(round(coalesce((d.nd_disc1*d.ccy_rate),0),0) + round(coalesce((d.nd_disc2*d.ccy_rate),0),0)) as nd_disc1,"
                + "((round(coalesce((d.nd_comm1*d.ccy_rate),0),0) + round(coalesce((d.nd_comm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_comm3*d.ccy_rate),0),0) + round(coalesce((d.nd_comm4*d.ccy_rate),0),0))) as nd_comm1,"
                + "(round(coalesce((d.nd_brok1*d.ccy_rate),0),0) + round(coalesce((d.nd_brok2*d.ccy_rate),0),0)) as nd_brok1,"
                + "(round(coalesce((d.nd_hfee*d.ccy_rate),0),0)) as nd_hfee,"
                + "(round(coalesce((d.nd_feebase1*d.ccy_rate),0),0) + round(coalesce((d.nd_feebase2*d.ccy_rate),0),0)) as nd_feebase1,"
                + "((round(coalesce((d.nd_taxcomm1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_taxcomm3*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm4*d.ccy_rate),0),0))) as nd_taxcomm1,"
                + "(round(coalesce((d.nd_taxbrok1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxbrok2*d.ccy_rate),0),0)) as nd_taxbrok1,"
                + "(round(coalesce((d.nd_taxhfee*d.ccy_rate),0),0)) as nd_taxhfee,"
                + "sum(getpremi2(e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id and b.ar_invoice_dtl_root_id is not null "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = d.entity_id ");

        sqa.addClause("f.status = 'POST'");

        if (FLT_PAID_PREMI) {
            sqa.addClause("f.ar_settlement_id = 25");
        }

        if (FLT_PAID_COMM) {
            sqa.addClause("f.ar_settlement_id = 33");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',d.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',d.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (createDateFrom != null) {
            sqa.addClause("date_trunc('day',f.create_date) >= '" + createDateFrom + "'");
            //sqa.addPar(createDateFrom);
        }

        if (createDateTo != null) {
            sqa.addClause("date_trunc('day',f.create_date) <= '" + createDateTo + "'");
            //sqa.addPar(createDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("f.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("e.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else {
                sqa.addClause("e.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            }
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("c.attr_pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }


        final String sql = sqa.getSQL()
                + "group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,d.cust_name,d.cc_code,d.ccy_rate,"
                + "d.pol_no,d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,d.nd_comm4,"
                + "d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,"
                + "d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee,d.nd_feebase1,d.nd_feebase2,d.nd_ppn "
                + "order by f.receipt_date,f.receipt_no,d.pol_no ";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_REALISASI_PREMIRECEIPT() throws Exception {
        final boolean FLT_PAID_PREMI = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_PREMI"));
        final boolean FLT_PAID_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "f.create_who,c.cc_code,f.receipt_no as description,f.receipt_date as period_start,coalesce(d.policy_date,c.mutation_date) as policy_date,coalesce(g.ent_name,h.ent_name) as ent_name,(substr(c.refid1,6,1)::numeric+1) as inst,"
                + "d.ccy_rate,';'||c.attr_pol_no as pol_no,round((d.premi_total*d.ccy_rate),0) as premi_total,round((d.premi_netto*d.ccy_rate),0) as premi_netto,round((d.total_due*d.ccy_rate),0) as total_due,"
                + "round(coalesce((d.nd_pcost*d.ccy_rate),0),0) as nd_pcost,round(coalesce((d.nd_sfee*d.ccy_rate),0),0) as nd_sfee,round(coalesce((d.nd_ppn*d.ccy_rate),0),0) as nd_ppn, "
                + "(round(coalesce((d.nd_disc1*d.ccy_rate),0),0) + round(coalesce((d.nd_disc2*d.ccy_rate),0),0)) as nd_disc1,"
                + "((round(coalesce((d.nd_comm1*d.ccy_rate),0),0) + round(coalesce((d.nd_comm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_comm3*d.ccy_rate),0),0) + round(coalesce((d.nd_comm4*d.ccy_rate),0),0))) as nd_comm1,"
                + "(round(coalesce((d.nd_brok1*d.ccy_rate),0),0) + round(coalesce((d.nd_brok2*d.ccy_rate),0),0)) as nd_brok1,"
                + "(round(coalesce((d.nd_hfee*d.ccy_rate),0),0)) as nd_hfee,"
                + "(round(coalesce((d.nd_feebase1*d.ccy_rate),0),0) + round(coalesce((d.nd_feebase2*d.ccy_rate),0),0)) as nd_feebase1,"
                + "((round(coalesce((d.nd_taxcomm1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm2*d.ccy_rate),0),0)) + (round(coalesce((d.nd_taxcomm3*d.ccy_rate),0),0) + round(coalesce((d.nd_taxcomm4*d.ccy_rate),0),0))) as nd_taxcomm1,"
                + "(round(coalesce((d.nd_taxbrok1*d.ccy_rate),0),0) + round(coalesce((d.nd_taxbrok2*d.ccy_rate),0),0)) as nd_taxbrok1,"
                + "(round(coalesce((d.nd_taxhfee*d.ccy_rate),0),0)) as nd_taxhfee,"
                + "sum(getpremi2(e.category = 'PREMIG',round(a.amount_settled,0))) as premi_paid,"
                + "sum(getpremi2(e.category = 'STAMPDUTY',round(a.amount_settled,0))) as ap_sfee_p,"
                + "sum(getpremi2(e.category = 'PCOST',round(a.amount_settled,0))) as ap_pcost_p,"
                + "sum(getpremi2(e.category = 'DISC',round(a.amount_settled,0))) as ap_disc_p,"
                + "sum(getpremi2(e.category = 'HFEE' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_hfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc <> 'PPN' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_bfee_p,"
                + "sum(getpremi2(e.category = 'BROKERAGE' and e.item_desc = 'PPN' and e.item_class is null and a.f_comission is null,round(a.amount_settled,0))) as ap_ppn_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class = 'FEEBASE' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_fbase_p,"
                + "sum(getpremi2(e.category = 'COMMISSION' and e.item_class is null and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_comis_p,"
                + "sum(getpremi2(e.category like 'TAX%' and a.f_comission = 'Y',round(a.amount_settled,0))) as ap_tax_p");

        sqa.addQuery(
                "from ar_invoice_details a "
                + "inner join ar_receipt_lines b on b.ar_invoice_dtl_id = a.ar_invoice_dtl_id and b.ar_invoice_dtl_root_id is not null "
                + "left join ar_invoice c on c.ar_invoice_id = a.ar_invoice_id "
                + "left join ins_policy d on d.pol_id = c.attr_pol_id "
                + "left join ar_trx_line e on e.ar_trx_line_id = a.ar_trx_line_id "
                + "left join ar_receipt f on f.ar_receipt_id = b.receipt_id "
                + "left join ent_master g on g.ent_id = d.entity_id "
                + "left join ent_master h on h.ent_id = c.ent_id ");

        sqa.addClause("f.status = 'POST'");

        if (FLT_PAID_PREMI) {
            sqa.addClause("f.ar_settlement_id = 25");
        }

        if (FLT_PAID_COMM) {
            sqa.addClause("f.ar_settlement_id = 33");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',c.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',c.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',b.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (createDateFrom != null) {
            sqa.addClause("date_trunc('day',f.create_date) >= ?");
            sqa.addPar(createDateFrom);
        }

        if (createDateTo != null) {
            sqa.addClause("date_trunc('day',f.create_date) <= ?");
            sqa.addPar(createDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("c.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("d.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stReceiptNo != null) {
            sqa.addClause("f.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("g.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("e.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else {
                sqa.addClause("e.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            }
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("c.attr_pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }


        final String sql = sqa.getSQL()
                + "group by f.create_who,f.receipt_date,f.receipt_no,d.policy_date,g.ent_name,c.cc_code,d.ccy_rate,c.attr_pol_no,"
                + "d.premi_total,d.premi_netto,d.total_due,d.nd_pcost,d.nd_sfee,d.nd_comm1,d.nd_comm2,d.nd_comm3,substr(c.refid1,6,1),"
                + "d.nd_comm4,d.nd_disc1,d.nd_disc2,d.nd_brok1,d.nd_brok2,d.nd_hfee,d.nd_feebase1,d.nd_feebase2,d.nd_ppn,"
                + "d.nd_taxcomm1,d.nd_taxcomm2,d.nd_taxcomm3,d.nd_taxcomm4,d.nd_taxbrok1,d.nd_taxbrok2,d.nd_taxhfee, "
                + "c.mutation_date,h.ent_name "
                + "order by f.receipt_date,f.receipt_no,c.attr_pol_no,substr(c.refid1,6,1) ";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REALISASI_PREMIRECEIPT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal TotalPremiBruto = null;
        BigDecimal TotalPremiNetto = null;
        BigDecimal TotalBiaMatPol = null;
        BigDecimal TotalComm = null;
        BigDecimal TotalTax = null;
        BigDecimal TotalPremiPaid = null;
        BigDecimal TotalCommPaid = null;
        BigDecimal OutstandingComm = null;
        BigDecimal OutstandingTaxComm = null;
        BigDecimal OutstandingFeebase = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            if (getPolicyDateFrom() != null) {
                row1.createCell(0).setCellValue("Tanggal Polis : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));
            }

            XSSFRow row2 = sheet.createRow(1);
            if (getPaymentDateFrom() != null) {
                row2.createCell(0).setCellValue("Tanggal Bayar : " + DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));
            }

            //bikin header
            XSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("tgl bayar");
            row0.createCell(1).setCellValue("no bukti");
            row0.createCell(2).setCellValue("tgl polis");
            row0.createCell(3).setCellValue("no polis");
            row0.createCell(4).setCellValue("inst");
            row0.createCell(5).setCellValue("premi");
            row0.createCell(6).setCellValue("biaya adm");
            row0.createCell(7).setCellValue("tag bruto");
            row0.createCell(8).setCellValue("feebase");
            row0.createCell(9).setCellValue("ppn");
            row0.createCell(10).setCellValue("diskon");
            row0.createCell(11).setCellValue("komisi");
            row0.createCell(12).setCellValue("pajak komisi");
            row0.createCell(13).setCellValue("premi netto");
            row0.createCell(14).setCellValue("premi dibayar");
            row0.createCell(15).setCellValue("diskon dibayar");
            row0.createCell(16).setCellValue("komisi dibayar");
            row0.createCell(17).setCellValue("pajak dibayar");
            row0.createCell(18).setCellValue("feebase dibayar");
            row0.createCell(19).setCellValue("ppn dibayar");
            row0.createCell(20).setCellValue("os komisi");
            row0.createCell(21).setCellValue("os pajak");
            row0.createCell(22).setCellValue("os feebase");
            row0.createCell(23).setCellValue("tertanggung");

            TotalTax = BDUtil.add(h.getFieldValueByFieldNameBD("nd_taxcomm1"), h.getFieldValueByFieldNameBD("nd_taxbrok1"));
            TotalTax = BDUtil.add(TotalTax, h.getFieldValueByFieldNameBD("nd_taxhfee"));

            TotalComm = BDUtil.add(h.getFieldValueByFieldNameBD("nd_comm1"), h.getFieldValueByFieldNameBD("nd_brok1"));
            TotalComm = BDUtil.add(TotalComm, h.getFieldValueByFieldNameBD("nd_hfee"));

            TotalPremiBruto = BDUtil.add(h.getFieldValueByFieldNameBD("nd_pcost"), h.getFieldValueByFieldNameBD("nd_sfee"));
            TotalPremiBruto = BDUtil.add(TotalPremiBruto, h.getFieldValueByFieldNameBD("premi_total"));

            TotalBiaMatPol = BDUtil.add(h.getFieldValueByFieldNameBD("nd_pcost"), h.getFieldValueByFieldNameBD("nd_sfee"));

            TotalPremiNetto = BDUtil.sub(TotalPremiBruto, BDUtil.sub(TotalComm, TotalTax));
            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, h.getFieldValueByFieldNameBD("nd_feebase1"));
            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, h.getFieldValueByFieldNameBD("nd_ppn"));
            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, h.getFieldValueByFieldNameBD("nd_disc1"));

            TotalPremiPaid = BDUtil.add(h.getFieldValueByFieldNameBD("premi_paid"), h.getFieldValueByFieldNameBD("ap_sfee_p"));
            TotalPremiPaid = BDUtil.add(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_pcost_p"));
            //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_disc_p"));
            //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_hfee_p"));
            //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_bfee_p"));
            //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_fbase_p"));
            //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_comis_p"));
            //TotalPremiPaid = BDUtil.add(TotalPremiPaid, h.getFieldValueByFieldNameBD("ap_tax_p"));

            TotalCommPaid = BDUtil.add(h.getFieldValueByFieldNameBD("ap_comis_p"), h.getFieldValueByFieldNameBD("ap_bfee_p"));
            TotalCommPaid = BDUtil.add(TotalCommPaid, h.getFieldValueByFieldNameBD("ap_hfee_p"));
            TotalCommPaid = BDUtil.sub(TotalCommPaid, h.getFieldValueByFieldNameBD("ap_tax_p"));

            OutstandingComm = BDUtil.sub(BDUtil.sub(TotalComm, TotalTax), h.getFieldValueByFieldNameBD("ap_comis_p"));
            OutstandingComm = BDUtil.sub(OutstandingComm, h.getFieldValueByFieldNameBD("ap_bfee_p"));
            OutstandingComm = BDUtil.sub(OutstandingComm, h.getFieldValueByFieldNameBD("ap_hfee_p"));

            OutstandingTaxComm = BDUtil.sub(TotalTax, h.getFieldValueByFieldNameBD("ap_tax_p"));
            OutstandingFeebase = BDUtil.sub(h.getFieldValueByFieldNameBD("nd_feebase1"), h.getFieldValueByFieldNameBD("ap_fbase_p"));

            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            if (h.getFieldValueByFieldNameST("description") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            if (h.getFieldValueByFieldNameBD("inst") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("inst").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            }
            if (TotalBiaMatPol != null) {
                row.createCell(6).setCellValue(TotalBiaMatPol.doubleValue());
            }
            if (TotalPremiBruto != null) {
                row.createCell(7).setCellValue(TotalPremiBruto.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_feebase1") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_feebase1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_ppn") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_ppn").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_disc1") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            }
            if (TotalComm != null) {
                row.createCell(11).setCellValue(BDUtil.sub(TotalComm, TotalTax).doubleValue());
            }
            if (TotalTax != null) {
                row.createCell(12).setCellValue(TotalTax.doubleValue());
            }
            if (TotalPremiNetto != null) {
                row.createCell(13).setCellValue(TotalPremiNetto.doubleValue());
            }
            if (TotalPremiPaid != null) {
                row.createCell(14).setCellValue(TotalPremiPaid.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ap_disc_p") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("ap_disc_p").doubleValue());
            }
            if (TotalCommPaid != null) {
                row.createCell(16).setCellValue(TotalCommPaid.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ap_tax_p") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("ap_tax_p").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ap_fbase_p") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("ap_fbase_p").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ap_ppn_p") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("ap_ppn_p").doubleValue());
            }
            if (OutstandingComm != null) {
                row.createCell(20).setCellValue(OutstandingComm.doubleValue());
            }
            if (OutstandingTaxComm != null) {
                row.createCell(21).setCellValue(OutstandingTaxComm.doubleValue());
            }
            if (OutstandingFeebase != null) {
                row.createCell(22).setCellValue(OutstandingFeebase.doubleValue());
            }
            if (h.getFieldValueByFieldNameST("ent_name") != null) {
                row.createCell(23).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("ent_name")));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList SOA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.cover_type_code,a.captive as cust_name,a.months as ref1,a.years as ref2,a.co_treaty_id,sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
                + " from ( select a.cover_type_code,a.ins_policy_type_grp_id,a.captive,substr(a.period_start::text,6,2) as months,substr(a.period_start::text,1,4) as years,j.treaty_type as co_treaty_id, "
                + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0))) as premi_amt, "
                + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0))) as nd_brok2pct, "
                + " sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA', coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0))) as claim_amount "
                + " from ( "
                + " select a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total,"
                + " a.status,a.claim_status,a.effective_flag,a.policy_date,a.period_start,a.ccy,a.ccy_rate,a.pol_type_id,getcaptive(substr(a.pol_no,2,1),d.ins_cover_id) as captive,"
                + " a.ins_policy_type_grp_id "
                + " from ins_policy a "
                + " inner join ins_pol_cover d on d.pol_id = a.pol_id "
                + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
                + " group by a.pol_id,a.pol_no,e.cover_type,a.status,a.claim_status,a.effective_flag, "
                + " a.policy_date,a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,d.ins_cover_id,a.ins_policy_type_grp_id ) a ");


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
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stEntityID != null) {
            sqa.addClause("k.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        final String sql = sqa.getSQL() + " group by a.cover_type_code,a.captive,j.treaty_type,a.period_start,a.ins_policy_type_grp_id ) a "
                + "where (premi_amt <> 0 or claim_amount <> 0) group by a.ins_policy_type_grp_id,a.cover_type_code,a.captive,a.months,a.years,a.co_treaty_id order by a.ins_policy_type_grp_id,a.years,a.months,a.cover_type_code,a.captive ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_SOA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.cover_type_code,a.captive,a.months,a.years,a.co_treaty_id,sum(premi_amt) as premi_amt,sum(nd_brok2pct) as nd_brok2pct,sum(claim_amount) as claim_amount "
                + " from ( select a.cover_type_code,a.ins_policy_type_grp_id,a.captive,substr(a.period_start::text,6,2) as months,substr(a.period_start::text,1,4) as years,j.treaty_type as co_treaty_id, "
                + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.premi_amount*a.ccy_rate)),0))) as premi_amt, "
                + " sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'), coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.ricomm_amt*a.ccy_rate)),0))) as nd_brok2pct, "
                + " sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA', coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0))) as claim_amount "
                + " from ( "
                + " select a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,(a.premi_total*a.ccy_rate) as premi_total,"
                + " a.status,a.claim_status,a.effective_flag,a.policy_date,a.period_start,a.ccy,a.ccy_rate,a.pol_type_id,getcaptive(substr(a.pol_no,2,1),d.ins_cover_id) as captive,"
                + " a.ins_policy_type_grp_id "
                + " from ins_policy a "
                + " inner join ins_pol_cover d on d.pol_id = a.pol_id "
                + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
                + " group by a.pol_id,a.pol_no,e.cover_type,a.status,a.claim_status,a.effective_flag, "
                + " a.policy_date,a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.pol_type_id,d.ins_cover_id,a.ins_policy_type_grp_id ) a ");


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
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stEntityID != null) {
            sqa.addClause("k.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        final String sql = sqa.getSQL() + " group by a.cover_type_code,a.captive,j.treaty_type,a.period_start,a.ins_policy_type_grp_id ) a "
                + "where (premi_amt <> 0 or claim_amount <> 0) group by a.ins_policy_type_grp_id,a.cover_type_code,a.captive,a.months,a.years,a.co_treaty_id order by a.ins_policy_type_grp_id,a.years,a.months,a.cover_type_code,a.captive ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_SOA() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal netto = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("TECHNICAL ACCOUNT STATEMENT");

            XSSFRow row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("Account Period : " + Parameter.readString("MONTH_" + DateUtil.getMonth(getPolicyDateFrom())) + " " + DateUtil.getYear(getPolicyDateFrom()));

            XSSFRow row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("In Account With : " + getStEntityName().toUpperCase());

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("C.O.B");
            row0.createCell(1).setCellValue("CAPTIVE");
            row0.createCell(2).setCellValue("MONTH");
            row0.createCell(3).setCellValue("TYPE OF TREATY");
            row0.createCell(4).setCellValue("PREMIUM");
            row0.createCell(5).setCellValue("COMMISSION");
            row0.createCell(6).setCellValue("LOSSES PAID");
            row0.createCell(7).setCellValue("BALANCE DUE TO");

            netto = BDUtil.sub(h.getFieldValueByFieldNameBD("premi_amt"), h.getFieldValueByFieldNameBD("nd_brok2pct"));
            netto = BDUtil.sub(netto, h.getFieldValueByFieldNameBD("claim_amount"));

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cover_type_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("captive"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("co_treaty_id") + " " + h.getFieldValueByFieldNameST("years"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("premi_amt").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_brok2pct").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(7).setCellValue(netto.doubleValue());


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList REALISASI_PANJARKLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " b.cc_code,b.pol_type_id,a.receipt_date,b.claim_approved_date,b.entity_id,a.receipt_no,"
                + " coalesce(e.refid2,e.refid1) as dla_no,b.pol_no,a.amount_per_line as premi_total ");

        sqa.addQuery(
                " from ar_receipt_lines a "
                + " inner join ins_policy b on b.pol_id = a.pol_id "
                + " inner join ent_master d on d.ent_id = b.entity_id "
                + " inner join ar_invoice e on e.attr_pol_id = a.pol_id "
                + " left join ar_receipt c on c.ar_receipt_id = a.receipt_id ");

        sqa.addClause("e.ar_trx_type_id = 12");
        sqa.addClause("c.ar_settlement_id = 10");
        sqa.addClause("e.refid0 = 'PANJAR'");
        sqa.addClause("a.line_type = 'INVOC'");
        //sqa.addClause("coalesce(e.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(e.cancel_flag,'') <> 'Y'");

        if (perDateFrom != null) {
            sqa.addClause("date_trunc('day',b.claim_approved_date) >= ?");
            sqa.addPar(DateUtil.getYear(perDateFrom) + "-01-01 00:00:00");
        }

        if (perDateFrom != null) {
            sqa.addClause("date_trunc('day',b.claim_approved_date) <= ?");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("b.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("b.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(b.pla_no like ? or b.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
            sqa.addPar('%' + stDlaNo + '%');
        }

        String sql = sqa.getSQL() + " and e.ar_invoice_id not in ( "
                + "select  e.ar_invoice_id from ar_receipt_lines a "
                + "left join ins_policy b on b.pol_id = a.pol_id "
                + "left join ent_master d on d.ent_id = b.entity_id "
                + "left join ar_invoice e on e.attr_pol_id = a.pol_id "
                + "left join ar_receipt c on c.ar_receipt_id = a.receipt_id "
                + "where e.ar_trx_type_id = 12 and c.ar_settlement_id = 34 and e.refid0 = 'PANJAR' and a.line_type = 'INVOC' ) ";

        sql = sql + " order by b.cc_code,b.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_REALISASI_PANJARKLAIM() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " b.ar_invoice_id,b.attr_pol_id,a.cc_code,a.pol_type_id::text,b.receipt_date,a.claim_approved_date,b.mutation_date,a.entity_id::text, coalesce(b.refid2,b.refid1) as dla_no,a.pol_no,"
                + " round(coalesce(b.amount,0),0) as premi_total,"
                + " getpremi(b.invoice_type = 'AP',coalesce((select sum(x.amount) from ar_invoice x where x.ar_trx_type_id = 11 and x.attr_pol_id = b.attr_pol_id),0),0) as pajak_klaim ");

        sqa.addQuery(
                " from ar_invoice b  "
                + " left join ins_policy a on a.pol_id = b.attr_pol_id  "
                + " left join ent_master c on c.ent_id = a.entity_id  ");


        sqa.addClause("b.invoice_type in ('AP','AR')");
        sqa.addClause("b.ar_trx_type_id = 12");
        sqa.addClause("b.refid0 = 'PANJAR'");
        sqa.addClause("coalesce(b.amount,0) <> 0");
        //sqa.addClause("coalesce(b.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");


        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',b.mutation_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(b.receipt_date is null or date_trunc('day',b.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        /*
        if (perDateFrom != null) {
        sqa.addClause("date_trunc('day',b.claim_approved_date) >= ?");
        sqa.addPar(DateUtil.getYear(perDateFrom) + "-01-01 00:00:00");
        }

        if (perDateFrom != null) {
        sqa.addClause("date_trunc('day',b.claim_approved_date) <= ?");
        sqa.addPar(perDateFrom);
        }*/

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
            sqa.addPar('%' + stDlaNo + '%');
        }

        String sql = sqa.getSQL();

        sql = sql + " order by a.cc_code,a.pol_no,b.ar_cust_id,b.no_surat_hutang";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REALISASI_PANJARKLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("LAPORAN PANJAR KLAIM YANG BISA DIREALISASI");

            XSSFRow row2 = sheet.createRow(1);
            if (getPLADateFrom() != null) {
                row2.createCell(0).setCellValue(DateUtil.getDateStr(getPerDateFrom()));
            }

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("KODA");
            row0.createCell(1).setCellValue("COB");
            row0.createCell(2).setCellValue("TGL BAYAR");
            row0.createCell(3).setCellValue("TGL SETUJUI");
            row0.createCell(4).setCellValue("BANK");
            row0.createCell(5).setCellValue("LKS/LKP");
            row0.createCell(6).setCellValue("NO POLIS");
            row0.createCell(7).setCellValue("JUMLAH");
            row0.createCell(8).setCellValue("PAJAK KLAIM");

            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(getPolicyType(h.getFieldValueByFieldNameST("pol_type_id")).getStShortDescription());

            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            row.createCell(4).setCellValue(getEntity2(h.getFieldValueByFieldNameST("entity_id")).getStEntityName());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("pajak_klaim").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public String getStDlaNo() {
        return stDlaNo;
    }

    public void setStDlaNo(String stDlaNo) {
        this.stDlaNo = stDlaNo;
    }

    public String getStTriwulan() {
        return stTriwulan;
    }

    public void setStTriwulan(String stTriwulan) {
        this.stTriwulan = stTriwulan;
    }

    public void EXPORT_RECEIPT_COINS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("tgl bayar");
            row0.createCell(1).setCellValue("no bukti");
            row0.createCell(2).setCellValue("surat hutang");
            row0.createCell(3).setCellValue("tertanggung");
            row0.createCell(4).setCellValue("tgl polis");
            row0.createCell(5).setCellValue("no polis");
            row0.createCell(6).setCellValue("kodeko");
            row0.createCell(7).setCellValue("premi");
            row0.createCell(8).setCellValue("komisi");
            row0.createCell(9).setCellValue("bfee");
            row0.createCell(10).setCellValue("hfee");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("attr_pol_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ref_ent_id"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("refn2").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("refn4").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("refn6").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("refn8").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_OUTSTANDINGCOREINS() throws Exception {
        final boolean FLT_OUTS_COINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_COINS"));
        final boolean FLT_OUTS_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OUTS_REINS"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.policy_date,a.cc_code,b.pol_id,b.pol_no,coalesce(a.kode_ko,coalesce(e.ref_ent_id,'00')) as kodeko,"
                + "a.ccy_rate,a.no_surat_hutang as cust_name, "
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=55,c.amount*-1)),0),0) as Premi,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=70,c.amount*-1)),0),0) as Diskon,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=71,c.amount*-1)),0),0) as Bfee,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=72,c.amount*-1)),0),0) as HFee,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=73,c.amount*-1)),0),0) as Komisi,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=66,c.amount)),0),0) as Premire,"
                + "round(coalesce(sum(getpremi2(c.ar_trx_line_id=67,c.amount)),0),0) as Komisire ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id "
                + " left join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master d on d.ent_id = b.entity_id "
                + " left join ent_master e on e.ent_id = a.ent_id ");

        if (FLT_OUTS_COINS) {
            sqa.addClause("invoice_type = 'AP'");
            sqa.addClause("ar_trx_type_id = 10");
        }

        if (FLT_OUTS_REINS) {
            sqa.addClause("invoice_type = 'AP'");
            sqa.addClause("ar_trx_type_id = 13");
        }

        sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        sqa.addClause("a.used_flag is null");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("a.no_surat_hutang like ?");
            sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(stKreasiName);
        }

        if (stReinsName != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(stReinsName);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        final String sql = "select * from ( " + sqa.getSQL() + " group by b.policy_date,a.cc_code,b.pol_id,b.pol_no,e.ent_id,a.kode_ko,e.ref_ent_id,a.ccy_rate,"
                + "a.no_surat_hutang order by a.cc_code,b.pol_no,e.ent_id,a.no_surat_hutang "
                + " ) a where pol_no is not null order by a.cc_code,a.pol_no,a.cust_name ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_OUTSTANDINGCOREINS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("tgl polis");
            row0.createCell(1).setCellValue("koda");
            row0.createCell(2).setCellValue("polis");
            row0.createCell(3).setCellValue("kodeko");
            row0.createCell(4).setCellValue("kurs");
            row0.createCell(5).setCellValue("nama");
            row0.createCell(6).setCellValue("premi");
            row0.createCell(7).setCellValue("diskon");
            row0.createCell(8).setCellValue("bfee");
            row0.createCell(9).setCellValue("hfee");
            row0.createCell(10).setCellValue("komisi");
            row0.createCell(11).setCellValue("premire");
            row0.createCell(12).setCellValue("komisire");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("kodeko"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("diskon").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("bfee").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("hfee").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("komisi").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premire").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("komisire").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public Date getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(Date createDateFrom) {
        this.createDateFrom = createDateFrom;
    }

    public Date getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }

    public Date getPaymentPremiDateFrom() {
        return paymentPremiDateFrom;
    }

    public void setPaymentPremiDateFrom(Date paymentPremiDateFrom) {
        this.paymentPremiDateFrom = paymentPremiDateFrom;
    }

    public Date getPaymentPremiDateTo() {
        return paymentPremiDateTo;
    }

    public void setPaymentPremiDateTo(Date paymentPremiDateTo) {
        this.paymentPremiDateTo = paymentPremiDateTo;
    }

    public DTOList OUTSTANDINGUMURPIUTANGPREMI() throws Exception {

        if (policyDateTo == null) {
            throw new RuntimeException("Tanggal Polis harus diisi");
        }

        Calendar cal = Calendar.getInstance();
        if (policyDateTo != null) {
            cal.setTime(getTanggal60hariSblmHari(policyDateTo));
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("  x.description,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
                + " select pol_type_id, description as description,"
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + "  then sum(premi_total) end as premi_total, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
                + "  then   sum(premi_total) end  as premi_netto ");

        sqa.addQuery("  from ("
                + " select b.pol_type_id, f.description, a.mutation_date as policy_date, a.receipt_date,"
                + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + " else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total"
                + " from ar_invoice a"
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id"
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id"
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
                + " and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '2013-12-31 00:00:00')"
                + " and substr(a.attr_pol_no,1,16) in ("
                + " select pol_no from ("
                + " select b.pol_type_id, substr(a.attr_pol_no,1,16) as pol_no,"
                + " sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class = 'PREMIG', c.amount)*-1,getpremi2(d.item_class = 'PREMIG', c.amount))) as premi_total"
                + " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id"
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) "
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) "
                + " and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '2013-12-31 00:00:00') "
                + " group by b.pol_type_id ,substr(a.attr_pol_no,1,16)"
                + " ) a where premi_total <> 0 ) group by  b.pol_type_id,f.description,a.ar_trx_type_id,a.receipt_date,a.mutation_date"
                + " ) a where premi_total <> 0 "
                + " group by pol_type_id,policy_date ,description order by pol_type_id ) x"
                + " ");

        final String sql = sqa.getSQL() + " group by x.pol_type_id,x.description order by pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public Date getTanggal60hariSblmHari(Date tanggal) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggal);
        cal.add(Calendar.DATE, -60);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        System.out.println(cal.getTime() + " <<<<");
        return cal.getTime();
    }

    public DTOList OUTSTANDINGUMURPIUTANGPREMIDAERAH() throws Exception {

        if (policyDateTo == null) {
            throw new RuntimeException("Tanggal Polis harus diisi");
        }

        Calendar cal = Calendar.getInstance();
        if (policyDateTo != null) {
            cal.setTime(getTanggal60hariSblmHari(policyDateTo));
        }

        final SQLAssembler sqa = new SQLAssembler();
        /*
        sqa.addSelect("  x.cc_code,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
        + " select k.cc_code, "
        + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
        + "  then   sum(premi_total) end  as premi_total, "
        + " case when date_trunc('day',policy_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  then sum(premi_total) end as premi_netto ");

        sqa.addQuery(" from ( select cc_code,policy_date, totalpremineto+totaltax-nd_disc1-nd_feebase1 as premi_total "
        + " from ( select cc_code,policy_date,totalpremibruto - totalcomm as totalpremineto,nd_disc1,nd_feebase1,totaltax"
        + " from ( select cc_code,policy_date, totalcom+totaltax as totalcomm,totalpremibruto,totaltax,nd_disc1,nd_feebase1"
        + " from (select cc_code,policy_date, nd_pcost + nd_sfee + premi_total as totalpremibruto , nd_comm1 + nd_brok1 + nd_hfee  as totalcom , nd_taxcomm1 + nd_taxbrok1 + nd_taxhfee as totaltax,nd_disc1,nd_feebase1"
        + " from (select * from ("
        + " select a.ar_trx_type_id::text,a.attr_pol_type_id::text,a.cc_code,g.description as cabang,a.attr_pol_id as pol_id,a.receipt_date,a.mutation_date as policy_date,"
        + " coalesce(e.ref_ent_id,e.ent_id::text) as ent_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,a.ccy_rate,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1"
        + " else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
        + " sum(getpremi2(d.category = 'PCOST',c.amount)) as nd_pcost,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
        + " sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
        + " sum(getpremi2(d.category = 'BROKERAGE', c.amount)) as nd_brok1,sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
        + " sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
        + " coalesce(sum(c.amount_settled),0) as premi_paid"
        + " from ar_invoice a  left join ins_policy b on b.pol_id = a.attr_pol_id"
        + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
        + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
        + " left join ent_master e on e.ent_id = a.ent_id"
        + " left join ins_policy_types f on f.pol_type_id = a.attr_pol_type_id"
        + " left join gl_cost_center g on g.cc_code = a.cc_code "
        + " where a.posted_flag = 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
        + "  and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'"
        + "  and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(perDateFrom) + "') "
        + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,g.description,a.attr_pol_id, a.receipt_date,e.ref_ent_id,"
        + " e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id  order by"
        + " a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0  order by a.cc_code,a.pol_no,a.ar_trx_type_id ) o )g ) f ) j ) k group by k.cc_code,k.policy_date ) x");

        final String sql = sqa.getSQL() + " group by x.cc_code order by x.cc_code ";
         */

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.cc_code,e.ent_id::text,e.ent_name,"
                + "a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,coalesce(a.kode_ko,coalesce(e.ref_ent_id,'00')) as kodeko,"
                + "a.mutation_date as policy_date,a.receipt_date,a.ccy_rate,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");
        sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= ? ";
            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= ?";
            sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )";
            sqa.addPar(perDateFrom);
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ? ");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ? ");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        String sql = " select x.cc_code,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
                + " select a.cc_code, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " then sum(premi_total) end as premi_total, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
                + " and date_trunc('day',policy_date) < '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
                + " then sum(premi_total) end  as premi_netto from ( "
                + " select a.cc_code,policy_date,sum((coalesce(premi_total,0)+coalesce(nd_sfee,0))+coalesce(nd_pcost,0)) as premi_total from ( "
                + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id,"
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a group by a.cc_code,a.policy_date ) a where premi_total <> 0 "
                + " group by a.cc_code,a.policy_date order by a.cc_code ) x group by x.cc_code order by x.cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);


        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList OUTSTANDINGUMURPIUTANGPREMIJENPOL() throws Exception {

        if (policyDateTo == null) {
            throw new RuntimeException("Tanggal Polis harus diisi");
        }

        Calendar cal = Calendar.getInstance();
        if (policyDateTo != null) {
            cal.setTime(getTanggal60hariSblmHari(policyDateTo));
        }

        final SQLAssembler sqa = new SQLAssembler();
        /*
        sqa.addSelect("  x.cc_code,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
        + " select k.cc_code, "
        + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
        + "  then   sum(premi_total) end  as premi_total, "
        + " case when date_trunc('day',policy_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  then sum(premi_total) end as premi_netto ");

        sqa.addQuery(" from ( select cc_code,policy_date, totalpremineto+totaltax-nd_disc1-nd_feebase1 as premi_total "
        + " from ( select cc_code,policy_date,totalpremibruto - totalcomm as totalpremineto,nd_disc1,nd_feebase1,totaltax"
        + " from ( select cc_code,policy_date, totalcom+totaltax as totalcomm,totalpremibruto,totaltax,nd_disc1,nd_feebase1"
        + " from (select cc_code,policy_date, nd_pcost + nd_sfee + premi_total as totalpremibruto , nd_comm1 + nd_brok1 + nd_hfee  as totalcom , nd_taxcomm1 + nd_taxbrok1 + nd_taxhfee as totaltax,nd_disc1,nd_feebase1"
        + " from (select * from ("
        + " select a.ar_trx_type_id::text,a.attr_pol_type_id::text,a.cc_code,g.description as cabang,a.attr_pol_id as pol_id,a.receipt_date,a.mutation_date as policy_date,"
        + " coalesce(e.ref_ent_id,e.ent_id::text) as ent_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,a.ccy_rate,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1"
        + " else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
        + " sum(getpremi2(d.category = 'PCOST',c.amount)) as nd_pcost,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
        + " sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
        + " sum(getpremi2(d.category = 'BROKERAGE', c.amount)) as nd_brok1,sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
        + " sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
        + " coalesce(sum(c.amount_settled),0) as premi_paid"
        + " from ar_invoice a  left join ins_policy b on b.pol_id = a.attr_pol_id"
        + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
        + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
        + " left join ent_master e on e.ent_id = a.ent_id"
        + " left join ins_policy_types f on f.pol_type_id = a.attr_pol_type_id"
        + " left join gl_cost_center g on g.cc_code = a.cc_code "
        + " where a.posted_flag = 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
        + "  and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'"
        + "  and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(perDateFrom) + "') "
        + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,g.description,a.attr_pol_id, a.receipt_date,e.ref_ent_id,"
        + " e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id  order by"
        + " a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0  order by a.cc_code,a.pol_no,a.ar_trx_type_id ) o )g ) f ) j ) k group by k.cc_code,k.policy_date ) x");

        final String sql = sqa.getSQL() + " group by x.cc_code order by x.cc_code ";
         */

        sqa.addSelect(
                " a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.cc_code,e.ent_id::text,e.ent_name,a.attr_pol_type_id as pol_type_id,"
                + "a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,coalesce(a.kode_ko,coalesce(e.ref_ent_id,'00')) as kodeko,"
                + "a.mutation_date as policy_date,a.receipt_date,a.ccy_rate,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");
        sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= ? ";
            sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= ?";
            sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )";
            sqa.addPar(perDateFrom);
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0)  ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ? ");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ? ");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        String sql = " select x.pol_type_id,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
                + " select a.pol_type_id, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " then sum(premi_total) end as premi_total, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
                + " and date_trunc('day',policy_date) < '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
                + " then sum(premi_total) end  as premi_netto from ( "
                + " select a.pol_type_id,policy_date,sum((coalesce(premi_total,0)+coalesce(nd_sfee,0))+coalesce(nd_pcost,0)) as premi_total from ( "
                + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id,"
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.attr_pol_type_id,a.attr_pol_no,a.kode_ko ) a group by a.pol_type_id,a.policy_date ) a where premi_total <> 0 "
                + " group by a.pol_type_id,a.policy_date order by a.pol_type_id ) x group by x.pol_type_id order by x.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);


        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_OUTSTANDINGUMURPIUTANGPREMIDAERAH() throws Exception {

        if (policyDateTo == null) {
            throw new RuntimeException("Tanggal Polis harus diisi");
        }

        Calendar cal = Calendar.getInstance();
        if (policyDateTo != null) {
            cal.setTime(getTanggal60hariSblmHari(policyDateTo));
        }

        final SQLAssembler sqa = new SQLAssembler();
        /*
        sqa.addSelect("  x.cc_code,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
        + " select k.cc_code, "
        + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
        + "  then   sum(premi_total) end  as premi_total, "
        + " case when date_trunc('day',policy_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  then sum(premi_total) end as premi_netto ");

        sqa.addQuery(" from ( select cc_code,policy_date, totalpremineto+totaltax-nd_disc1-nd_feebase1 as premi_total "
        + " from ( select cc_code,policy_date,totalpremibruto - totalcomm as totalpremineto,nd_disc1,nd_feebase1,totaltax"
        + " from ( select cc_code,policy_date, totalcom+totaltax as totalcomm,totalpremibruto,totaltax,nd_disc1,nd_feebase1"
        + " from (select cc_code,policy_date, nd_pcost + nd_sfee + premi_total as totalpremibruto , nd_comm1 + nd_brok1 + nd_hfee  as totalcom , nd_taxcomm1 + nd_taxbrok1 + nd_taxhfee as totaltax,nd_disc1,nd_feebase1"
        + " from (select * from ("
        + " select a.ar_trx_type_id::text,a.attr_pol_type_id::text,a.cc_code,g.description as cabang,a.attr_pol_id as pol_id,a.receipt_date,a.mutation_date as policy_date,"
        + " coalesce(e.ref_ent_id,e.ent_id::text) as ent_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,a.ccy_rate,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1"
        + " else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
        + " sum(getpremi2(d.category = 'PCOST',c.amount)) as nd_pcost,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
        + " sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
        + " sum(getpremi2(d.category = 'BROKERAGE', c.amount)) as nd_brok1,sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
        + " sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
        + " coalesce(sum(c.amount_settled),0) as premi_paid"
        + " from ar_invoice a  left join ins_policy b on b.pol_id = a.attr_pol_id"
        + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
        + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
        + " left join ent_master e on e.ent_id = a.ent_id"
        + " left join ins_policy_types f on f.pol_type_id = a.attr_pol_type_id"
        + " left join gl_cost_center g on g.cc_code = a.cc_code "
        + " where a.posted_flag = 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
        + "  and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'"
        + "  and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(perDateFrom) + "') "
        + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,g.description,a.attr_pol_id, a.receipt_date,e.ref_ent_id,"
        + " e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id  order by"
        + " a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0  order by a.cc_code,a.pol_no,a.ar_trx_type_id ) o )g ) f ) j ) k group by k.cc_code,k.policy_date ) x");

        final String sql = sqa.getSQL() + " group by x.cc_code order by x.cc_code ";
         */

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.cc_code,e.ent_id::text,e.ent_name,g.description as cabang,"
                + "a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,coalesce(a.kode_ko,coalesce(e.ref_ent_id,'00')) as kodeko,"
                + "a.mutation_date as policy_date,a.receipt_date,a.ccy_rate,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join gl_cost_center g on g.cc_code = a.cc_code "
                + " left join ins_policy_types f on f.pol_type_id = b.pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");
        sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        String sql = " select x.cc_code as koda,x.cabang,sum(x.premi_total) as os_1_sd_60_hari,sum(x.premi_netto) as os_diatas_60_hari from ( "
                + " select a.cc_code,a.cabang, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " then sum(premi_total) end as premi_total, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
                + " and date_trunc('day',policy_date) < '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
                + " then sum(premi_total) end  as premi_netto from ( "
                + " select a.cc_code,a.cabang,policy_date,sum((coalesce(premi_total,0)+coalesce(nd_sfee,0))+coalesce(nd_pcost,0)) as premi_total from ( "
                + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,g.description,a.attr_pol_id,"
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a group by a.cc_code,a.cabang,a.policy_date ) a where premi_total <> 0 "
                + " group by a.cc_code,a.cabang,a.policy_date order by a.cc_code ) x group by x.cc_code,x.cabang order by x.cc_code ";

        String nama_file = null;
        nama_file = "osumurpiutang_" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    public void EXCEL_OUTSTANDINGUMURPIUTANGPREMIJENPOL() throws Exception {

        if (policyDateTo == null) {
            throw new RuntimeException("Tanggal Polis harus diisi");
        }

        Calendar cal = Calendar.getInstance();
        if (policyDateTo != null) {
            cal.setTime(getTanggal60hariSblmHari(policyDateTo));
        }

        final SQLAssembler sqa = new SQLAssembler();
        /*
        sqa.addSelect("  x.cc_code,sum(x.premi_total) as premi_total,sum(x.premi_netto) as premi_netto from ( "
        + " select k.cc_code, "
        + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
        + "  then   sum(premi_total) end  as premi_total, "
        + " case when date_trunc('day',policy_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
        + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  then sum(premi_total) end as premi_netto ");

        sqa.addQuery(" from ( select cc_code,policy_date, totalpremineto+totaltax-nd_disc1-nd_feebase1 as premi_total "
        + " from ( select cc_code,policy_date,totalpremibruto - totalcomm as totalpremineto,nd_disc1,nd_feebase1,totaltax"
        + " from ( select cc_code,policy_date, totalcom+totaltax as totalcomm,totalpremibruto,totaltax,nd_disc1,nd_feebase1"
        + " from (select cc_code,policy_date, nd_pcost + nd_sfee + premi_total as totalpremibruto , nd_comm1 + nd_brok1 + nd_hfee  as totalcom , nd_taxcomm1 + nd_taxbrok1 + nd_taxhfee as totaltax,nd_disc1,nd_feebase1"
        + " from (select * from ("
        + " select a.ar_trx_type_id::text,a.attr_pol_type_id::text,a.cc_code,g.description as cabang,a.attr_pol_id as pol_id,a.receipt_date,a.mutation_date as policy_date,"
        + " coalesce(e.ref_ent_id,e.ent_id::text) as ent_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,a.ccy_rate,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1"
        + " else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
        + " sum(getpremi2(d.category = 'PCOST',c.amount)) as nd_pcost,"
        + " case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
        + " sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
        + " sum(getpremi2(d.category = 'BROKERAGE', c.amount)) as nd_brok1,sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
        + " sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
        + " coalesce(sum(c.amount_settled),0) as premi_paid"
        + " from ar_invoice a  left join ins_policy b on b.pol_id = a.attr_pol_id"
        + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
        + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
        + " left join ent_master e on e.ent_id = a.ent_id"
        + " left join ins_policy_types f on f.pol_type_id = a.attr_pol_type_id"
        + " left join gl_cost_center g on g.cc_code = a.cc_code "
        + " where a.posted_flag = 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10)"
        + "  and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'"
        + "  and date_trunc('day',a.mutation_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' and date_trunc('day',a.mutation_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
        + "  and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(perDateFrom) + "') "
        + " group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,g.description,a.attr_pol_id, a.receipt_date,e.ref_ent_id,"
        + " e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id  order by"
        + " a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0  order by a.cc_code,a.pol_no,a.ar_trx_type_id ) o )g ) f ) j ) k group by k.cc_code,k.policy_date ) x");

        final String sql = sqa.getSQL() + " group by x.cc_code order by x.cc_code ";
         */

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.cc_code,e.ent_id::text,e.ent_name,g.description as cabang,a.attr_pol_type_id,f.description as jenpol, "
                + "a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.attr_pol_name as cust_name,coalesce(a.kode_ko,coalesce(e.ref_ent_id,'00')) as kodeko,"
                + "a.mutation_date as policy_date,a.receipt_date,a.ccy_rate,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,coalesce(sum(c.amount_settled),0) as premi_paid ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join gl_cost_center g on g.cc_code = a.cc_code "
                + " left join ins_policy_types f on f.pol_type_id = a.attr_pol_type_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");
        sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ " where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        String sql = " select x.attr_pol_type_id as jenid,x.jenpol,sum(x.premi_total) as os_1_sd_60_hari,sum(x.premi_netto) as os_diatas_60_hari from ( "
                + " select a.attr_pol_type_id,a.jenpol, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "' "
                + " and date_trunc('day',policy_date) <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateTo) + "'"
                + " then sum(premi_total) end as premi_total, "
                + " case when date_trunc('day',policy_date) >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(policyDateFrom) + "' "
                + " and date_trunc('day',policy_date) < '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()) + "'"
                + " then sum(premi_total) end  as premi_netto from ( "
                + " select a.attr_pol_type_id,a.jenpol,policy_date,sum((coalesce(premi_total,0)+coalesce(nd_sfee,0))+coalesce(nd_pcost,0)) as premi_total from ( "
                + sqa.getSQL() + " group by a.ar_trx_type_id,a.attr_pol_type_id,f.description,a.cc_code,g.description,a.attr_pol_id,"
                + " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id "
                + " order by a.attr_pol_type_id,a.attr_pol_no,a.kode_ko ) a group by a.attr_pol_type_id,a.jenpol,a.policy_date ) a where premi_total <> 0 "
                + " group by a.attr_pol_type_id,a.jenpol,a.policy_date order by a.attr_pol_type_id ) x group by x.attr_pol_type_id,x.jenpol order by x.attr_pol_type_id ";

        String nama_file = null;
        nama_file = "osumurpiutang_" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    public DTOList BIAOPERASIONAL() throws Exception {
        final boolean FLT_BIAPEM = "Y".equalsIgnoreCase((String) refPropMap.get("BIAPEM"));
        final boolean FLT_BIAUM = "Y".equalsIgnoreCase((String) refPropMap.get("BIAUM"));
        final boolean FLT_BIAADM = "Y".equalsIgnoreCase((String) refPropMap.get("BIAADM"));

        final SQLAssembler sqa = new SQLAssembler();
        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(policyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

//        if (gljedetailYear.equalsIgnoreCase("2012")
//                || gljedetailYear.equalsIgnoreCase("2013")) {
//            gljedetail = "gl_je_detail_" + gljedetailYear;
//        } else {
//            gljedetail = "gl_je_detail";
//        }

        sqa.addSelect(
                " b.accountno,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.description,b.owner_code,b.user_code ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_BIAPEM) {
            sqa.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_BIAUM) {
            sqa.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_BIAADM) {
            sqa.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(stBranch);
        }

        String sql = null;

        if (getStPolicyTypeGroupID() != null) {
            if (getStPolicyTypeID() != null) {
                String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                if (account.length == 1) {
                    sql = "select b.description,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                            + "from s_biaop_detail a left join ( "
                            + sqa.getSQL() + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                            + " where a.biaop_dtl_id = ? ";
                    sqa.addPar(stPolicyTypeID);
                    sql = sql + " order by b.policy_date,b.accountno ";
                } else if (account.length > 1) {
                    sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                    for (int k = 1; k < account.length; k++) {
                        sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                    }
                    sql = sql + ") order by a.applydate,b.accountno";
                }
            } else {
                String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                if (account.length == 1) {
                    sql = "select b.description,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                            + "from s_biaop_group a left join ( "
                            + sqa.getSQL() + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                            + " where a.biaop_grp_id = ? ";
                    sqa.addPar(stPolicyTypeGroupID);
                    sql = sql + " order by b.policy_date,b.accountno ";
                } else if (account.length > 1) {
                    sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                    for (int k = 1; k < account.length; k++) {
                        sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                    }
                    sql = sql + ") order by a.applydate,b.accountno";
                }
            }
        } else {
            sql = sqa.getSQL() + " order by a.applydate,b.accountno";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList BIARKAP() throws Exception {
        final boolean FLT_RKAPUW = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUW"));
        final boolean FLT_RKAPIN = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPIN"));
        final boolean FLT_RKAPPEM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPPEM"));
        final boolean FLT_RKAPUM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUM"));
        final boolean FLT_RKAPADM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPADM"));
        final boolean FLT_RKAPDPT = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPDPT"));

        final SQLAssembler sqa = new SQLAssembler();
        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(policyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

//        if (gljedetailYear.equalsIgnoreCase("2012")
//                || gljedetailYear.equalsIgnoreCase("2013")) {
//            gljedetail = "gl_je_detail_" + gljedetailYear;
//        } else {
//            gljedetail = "gl_je_detail";
//        }

        sqa.addSelect(
                " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no,a.owner_code,a.user_code ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_RKAPUW) {
            sqa.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(stBranch);
        }

        String sql = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        //                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                        sql = sql + " ) ) b on (substr(b.accountno,1,3) between '" + account[0].substring(0, 3) + "' and '" + account[account.length - 1].substring(0, 3) + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                }
            } else {
                sql = sqa.getSQL() + " order by a.applydate,b.accountno";
            }
        }

        if (Tools.isYes(stRecapFlag)) {
//            sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
//                    + sqa.getSQL() + " ) a group by substr(a.accountno,1,5)||'0000000' ";

            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                                + "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno "
                                + " ) a group by substr(a.accountno,1,5)||'0000000' ";
                    } else if (account.length > 1) {
                        sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                                + "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno "
                                + " ) a group by substr(a.accountno,1,5)||'0000000' ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                                + "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno "
                                + " ) a group by substr(a.accountno,1,5)||'0000000' ";
                    } else if (account.length > 1) {
                        sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                                + "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno "
                                + " ) a group by substr(a.accountno,1,5)||'0000000' ";
                    }
                }
            } else {
                sql = sqa.getSQL() + " order by substr(a.accountno,1,5)||'0000000'";
            }
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public BiayaOperasionalDetail getBiaopDetail() {
        final BiayaOperasionalDetail polType = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, stPolicyTypeID);

        return polType;
    }

    public BiayaOperasionalGroup getBiaopGroup() {
        final BiayaOperasionalGroup polGroup = (BiayaOperasionalGroup) DTOPool.getInstance().getDTO(BiayaOperasionalGroup.class, stPolicyTypeGroupID);

        return polGroup;
    }

    public void EXCEL_BIARKAPNEW() throws Exception {
        final boolean FLT_RKAPUW = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUW"));
        final boolean FLT_RKAPIN = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPIN"));
        final boolean FLT_RKAPPEM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPPEM"));
        final boolean FLT_RKAPUM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUM"));
        final boolean FLT_RKAPADM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPADM"));
        final boolean FLT_RKAPDPT = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPDPT"));

        final SQLAssembler sqa = new SQLAssembler();
        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(policyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

//        if (gljedetailYear.equalsIgnoreCase("2012")
//                || gljedetailYear.equalsIgnoreCase("2013")) {
//            gljedetail = "gl_je_detail_" + gljedetailYear;
//        } else {
//            gljedetail = "gl_je_detail";
//        }

        sqa.addSelect(
                " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no,a.owner_code,a.user_code ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_RKAPUW) {
            sqa.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        String sql = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = '" + DateUtil.getYear(policyDateFrom) + "' "
                                + " where a.biaop_dtl_id = '" + stPolicyTypeID + "'";
                        //sqa.addPar(DateUtil.getYear(policyDateFrom));
                        //sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = '" + DateUtil.getYear(policyDateFrom) + "' "
                                + " where a.biaop_dtl_id = '" + stPolicyTypeID + "'";
                        //sqa.addPar(DateUtil.getYear(policyDateFrom));
                        //sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = '" + DateUtil.getYear(policyDateFrom) + "' "
                                + " where a.biaop_grp_id = '" + stPolicyTypeGroupID + "'";
                        //sqa.addPar(DateUtil.getYear(policyDateFrom));
                        //sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = '" + DateUtil.getYear(policyDateFrom) + "' "
                                + " where a.biaop_grp_id = '" + stPolicyTypeGroupID + "'";
                        //sqa.addPar(DateUtil.getYear(policyDateFrom));
                        //sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                }
            } else {
                sql = sqa.getSQL() + " order by a.applydate,b.accountno";
            }
        }

        if (Tools.isYes(stRecapFlag)) {
            sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                    + sqa.getSQL() + " ) a group by substr(a.accountno,1,5)||'0000000' order by substr(a.accountno,1,5)||'0000000' ";
        }

        SQLUtil S = new SQLUtil();

        String nama_file = "biarkap_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCEL_BIARKAPOLD() throws Exception {
        final boolean FLT_RKAPUW = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUW"));
        final boolean FLT_RKAPIN = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPIN"));
        final boolean FLT_RKAPPEM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPPEM"));
        final boolean FLT_RKAPUM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUM"));
        final boolean FLT_RKAPADM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPADM"));
        final boolean FLT_RKAPDPT = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPDPT"));

        final SQLAssembler sqa = new SQLAssembler();
        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(policyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

//        if (gljedetailYear.equalsIgnoreCase("2012")
//                || gljedetailYear.equalsIgnoreCase("2013")) {
//            gljedetail = "gl_je_detail_" + gljedetailYear;
//        } else {
//            gljedetail = "gl_je_detail";
//        }

        sqa.addSelect(
                " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no,a.owner_code,a.user_code ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_RKAPUW) {
            sqa.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(stBranch);
        }

        String sql = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_detail a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
//                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                        sql = sql + " ) ) b on (substr(b.accountno,1,3) between '" + account[0].substring(0, 3) + "' and '" + account[account.length - 1].substring(0, 3) + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_dtl_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    } else if (account.length > 1) {
                        sql = "select b.accountno,b.description,b.pol_no,b.cc_code,b.policy_date,b.amount,c.konvensional as insured_amount,b.owner_code,b.user_code "
                                + "from s_biaop_group a left join ( "
                                + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
//                        sql = sql + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                        sql = sql + " ) ) b on (substr(b.accountno,1,3) between '" + account[0].substring(0, 3) + "' and '" + account[account.length - 1].substring(0, 3) + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " where a.biaop_grp_id = ? ";
                        sqa.addPar(DateUtil.getYear(policyDateFrom));
                        sqa.addPar(stPolicyTypeGroupID);
                        sql = sql + " order by b.policy_date,b.accountno ";
                    }
                }
            } else {
                sql = sqa.getSQL() + " order by a.applydate,b.accountno";
            }
        }

        if (Tools.isYes(stRecapFlag)) {
            sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                    + sqa.getSQL() + " ) a group by substr(a.accountno,1,5)||'0000000' order by substr(a.accountno,1,5)||'0000000' ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        //rekap per cabang
        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(
                " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no ");

        sqa2.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_RKAPUW) {
            sqa2.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa2.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa2.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa2.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa2.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa2.addClause("date_trunc('day',a.applydate) >= ?");
            sqa2.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa2.addClause("date_trunc('day',a.applydate) <= ?");
            sqa2.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa2.addClause("substr(b.accountno,14,2) = ?");
            sqa2.addPar(stBranch);
        }

        String sql2 = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount,c.konvensional as insured_amount "
                                + "from s_biaop_detail a left join ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " inner join gl_cost_center d on d.cc_code = b.cc_code "
                                + " where a.biaop_dtl_id = ? ";
                        sqa2.addPar(DateUtil.getYear(policyDateFrom));
                        sqa2.addPar(stPolicyTypeID);
                        sql2 = sql2 + " group by 1,2,4 order by 1 ";
                    } else if (account.length > 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount,c.konvensional as insured_amount "
                                + "from s_biaop_detail a left join ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql2 = sql2 + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql2 = sql2 + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " inner join gl_cost_center d on d.cc_code = b.cc_code "
                                + " where a.biaop_dtl_id = ? ";
                        sqa2.addPar(DateUtil.getYear(policyDateFrom));
                        sqa2.addPar(stPolicyTypeID);
                        sql2 = sql2 + " group by 1,2,4 order by 1 ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount,c.konvensional as insured_amount "
                                + "from s_biaop_group a left join ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "')"
                                + " ) b on (substr(b.accountno,1,length(a.account)) between a.account and a.account2) "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " inner join gl_cost_center d on d.cc_code = b.cc_code "
                                + " where a.biaop_grp_id = ? ";
                        sqa2.addPar(DateUtil.getYear(policyDateFrom));
                        sqa2.addPar(stPolicyTypeGroupID);
                        sql2 = sql2 + " group by 1,2,4 order by 1 ";
                    } else if (account.length > 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount,c.konvensional as insured_amount "
                                + "from s_biaop_group a left join ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql2 = sql2 + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
//                        sql2 = sql2 + " ) ) b on (substr(b.accountno,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                        sql2 = sql2 + " ) ) b on (substr(b.accountno,1,3) between '" + account[0].substring(0, 3) + "' and '" + account[account.length - 1].substring(0, 3) + "') "
                                + " left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id and c.years = ? "
                                + " inner join gl_cost_center d on d.cc_code = b.cc_code "
                                + " where a.biaop_grp_id = ? ";
                        sqa2.addPar(DateUtil.getYear(policyDateFrom));
                        sqa2.addPar(stPolicyTypeGroupID);
                        sql2 = sql2 + " group by 1,2,4 order by 1 ";
                    }
                }
            } else {
                sql2 = "select a.cc_code,c.description as cabang,sum(a.amount) as amount from ( "
                        + sqa2.getSQL() + " ) a inner join gl_cost_center c on c.cc_code = a.cc_code "
                        + "group by 1,2 order by 1 ";
            }
        }

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;

    }

    public DTOList EXCEL_BIARKAP() throws Exception {
        final boolean FLT_RKAPUW = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUW"));
        final boolean FLT_RKAPIN = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPIN"));
        final boolean FLT_RKAPPEM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPPEM"));
        final boolean FLT_RKAPUM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUM"));
        final boolean FLT_RKAPADM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPADM"));
        final boolean FLT_RKAPDPT = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPDPT"));

        final SQLAssembler sqa = new SQLAssembler();
        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(policyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

        String select = " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no,a.owner_code,a.user_code ";

        if (FLT_RKAPPEM) {
            select = select + " ,c.pms ";
        }

        sqa.addSelect(select);

        String query = " from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ";

        if (FLT_RKAPPEM) {
            query = query + " left join ( select a.no_bukti_bayar as nobuk,"
                    + "(case when a.fileapp is not null then 'NON 0.5%' else '0.5%' end) as pms "
                    + "from biaya_pemasaran a where a.validasi_f = 'Y' and a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y' "
                    + "and a.no_bukti_bayar is not null ) c on c.nobuk = a.trx_no ";
        }

        sqa.addQuery(query);

        if (FLT_RKAPUW) {
            sqa.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(stBranch);
        }

        String sql = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "') order by a.applydate,b.accountno ";
                    } else if (account.length > 1) {
                        sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + ") order by a.applydate,b.accountno ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "') order by a.applydate,b.accountno ";
                    } else if (account.length > 1) {
                        sql = sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql = sql + ") order by a.applydate,b.accountno ";
                    }
                }
            } else {
                sql = sqa.getSQL() + " order by a.applydate,b.accountno";
            }
        }

        if (Tools.isYes(stRecapFlag)) {
            sql = "select substr(a.accountno,1,5)||'0000000' as accountno,sum(a.amount) as amount from ( "
                    + sqa.getSQL() + " ) a group by substr(a.accountno,1,5)||'0000000' order by substr(a.accountno,1,5)||'0000000' ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        //rekap per cabang
        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(
                " b.accountno,a.description,b.noper,substr(b.accountno,14,2) as cc_code,a.applydate as policy_date,"
                + "(a.debit-a.credit) as amount,a.trx_no as pol_no ");

        sqa2.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid ");

        if (FLT_RKAPUW) {
            sqa2.addClause("((substr(b.accountno,1,2) between '61' and '64') or substr(b.accountno,1,1) = '7')");
        }

        if (FLT_RKAPIN) {
            sqa2.addClause("substr(b.accountno,1,2) = '65'");
        }

        if (FLT_RKAPPEM) {
            sqa2.addClause("substr(b.accountno,1,2) = '81'");
        }

        if (FLT_RKAPUM) {
            sqa2.addClause("substr(b.accountno,1,2) = '82'");
        }

        if (FLT_RKAPADM) {
            sqa2.addClause("substr(b.accountno,1,2) = '83'");
        }

        if (policyDateFrom != null) {
            sqa2.addClause("date_trunc('day',a.applydate) >= ?");
            sqa2.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa2.addClause("date_trunc('day',a.applydate) <= ?");
            sqa2.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa2.addClause("substr(b.accountno,14,2) = ?");
            sqa2.addPar(stBranch);
        }

        String sql2 = null;

        if (Tools.isNo(stRecapFlag)) {
            if (getStPolicyTypeGroupID() != null) {
                if (getStPolicyTypeID() != null) {
                    String account[] = getBiaopDetail().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount from ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "') order by a.applydate,b.accountno "
                                + ") b inner join gl_cost_center d on d.cc_code = b.cc_code group by 1,2 order by 1,2 ";
                    } else if (account.length > 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount from ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql2 = sql2 + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql2 = sql2 + ") order by a.applydate,b.accountno "
                                + ") b inner join gl_cost_center d on d.cc_code = b.cc_code group by 1,2 order by 1,2 ";
                    }
                } else {
                    String account[] = getBiaopGroup().getStAccount().split("[\\|]");
                    if (account.length == 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount from ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "') order by a.applydate,b.accountno "
                                + ") b inner join gl_cost_center d on d.cc_code = b.cc_code group by 1,2 order by 1,2 ";
                    } else if (account.length > 1) {
                        sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount from ( "
                                + sqa2.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
                        for (int k = 1; k < account.length; k++) {
                            sql2 = sql2 + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
                        }
                        sql2 = sql2 + ") order by a.applydate,b.accountno "
                                + ") b inner join gl_cost_center d on d.cc_code = b.cc_code group by 1,2 order by 1,2 ";
                    }
                }
            } else {
                sql2 = "select b.cc_code,d.description as cabang,sum(b.amount) as amount from ( "
                        + sqa2.getSQL() + " order by a.applydate,b.accountno "
                        + ") b inner join gl_cost_center d on d.cc_code = b.cc_code group by 1,2 order by 1,2 ";
            }
        }

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        //getRKAP
        if (getStPolicyTypeGroupID() != null) {
            final SQLAssembler sqa3 = new SQLAssembler();

            String selectQuery = null;
            String fromQuery = null;
            if (getStPolicyTypeID() != null) {
                selectQuery = "a.biaop_dtl_id,a.description,a.biaop_grp_id,a.account,c.rkap_group_id,c.konvensional as insured_amount ";
                fromQuery = "s_biaop_detail";
            } else {
                selectQuery = "a.biaop_grp_id,a.description,a.account,c.konvensional as insured_amount ";
                fromQuery = "s_biaop_group";
            }

            sqa3.addSelect(selectQuery);

            sqa3.addQuery(" from " + fromQuery + " a "
                    + "left join gl_rkap_group c on c.rkap_group_id = a.rkap_group_id ");

            if (policyDateFrom != null) {
                sqa3.addClause("c.years = ?");
                sqa3.addPar(DateUtil.getYear(policyDateFrom));
            }

            if (stPolicyTypeGroupID != null) {
                if (stPolicyTypeID != null) {
                    sqa3.addClause("a.biaop_dtl_id = ?");
                    sqa3.addPar(stPolicyTypeID);
                } else {
                    sqa3.addClause("a.biaop_grp_id = ?");
                    sqa3.addPar(stPolicyTypeGroupID);
                }
            }

            String sql3 = sqa3.getSQL();

            final DTOList l3 = ListUtil.getDTOListFromQuery(
                    sql3,
                    sqa3.getPar(),
                    HashDTO.class);

            SessionManager.getInstance().getRequest().setAttribute("RPT3", l3);
        }

        return l;

    }

    public void EXPORT_BIARKAP() throws Exception {
        final boolean FLT_RKAPUW = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUW"));
        final boolean FLT_RKAPIN = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPIN"));
        final boolean FLT_RKAPPEM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPPEM"));
        final boolean FLT_RKAPUM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPUM"));
        final boolean FLT_RKAPADM = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPADM"));
        final boolean FLT_RKAPDPT = "Y".equalsIgnoreCase((String) refPropMap.get("RKAPDPT"));

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");
        XSSFSheet sheet2 = wb.createSheet("rekap cabang");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        final DTOList list3 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");

        BigDecimal saldo = null;
        BigDecimal amount = null;
        BigDecimal amountRKAP = null;

        if (getStPolicyTypeGroupID() != null) {
            for (int i = 0; i < list3.size(); i++) {
                HashDTO h = (HashDTO) list3.get(i);

                if (getStPolicyTypeGroupID() != null) {
                    amountRKAP = h.getFieldValueByFieldNameBD("insured_amount");
                } else {
                    if (FLT_RKAPUW) {
                        amountRKAP = BDUtil.mul(new BigDecimal(366358), new BigDecimal(1000000));
                    }
                    if (FLT_RKAPIN) {
                        amountRKAP = BDUtil.mul(new BigDecimal(55225), new BigDecimal(1000000));
                    }
                    if (FLT_RKAPPEM) {
                        amountRKAP = BDUtil.mul(new BigDecimal(22940), new BigDecimal(1000000));
                    }
                    if (FLT_RKAPUM) {
                        amountRKAP = BDUtil.mul(new BigDecimal(184810), new BigDecimal(1000000));
                    }
                    if (FLT_RKAPADM) {
                        amountRKAP = BDUtil.mul(new BigDecimal(49558), new BigDecimal(1000000));
                    }
                    if (FLT_RKAPDPT) {
                        amountRKAP = BDUtil.mul(new BigDecimal(165825), new BigDecimal(1000000));
                    }
                }

                XSSFRow row1 = sheet.createRow(0);
                if (getStBranch() == null) {
                    row1.createCell(4).setCellValue("RKAP Nasional " + DateUtil.getYear(getPolicyDateFrom()));
                    if (amountRKAP != null) {
                        row1.createCell(5).setCellValue(amountRKAP.doubleValue());
                    }
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

//            if (getStPolicyTypeGroupID() != null) {
//                amountRKAP = h.getFieldValueByFieldNameBD("insured_amount");
//            } else {
//                if (FLT_RKAPUW) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(366358), new BigDecimal(1000000));
//                }
//                if (FLT_RKAPIN) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(55225), new BigDecimal(1000000));
//                }
//                if (FLT_RKAPPEM) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(22940), new BigDecimal(1000000));
//                }
//                if (FLT_RKAPUM) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(184810), new BigDecimal(1000000));
//                }
//                if (FLT_RKAPADM) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(49558), new BigDecimal(1000000));
//                }
//                if (FLT_RKAPDPT) {
//                    amountRKAP = BDUtil.mul(new BigDecimal(165825), new BigDecimal(1000000));
//                }
//            }
//
//            XSSFRow row1 = sheet.createRow(0);
//            if (getStBranch() == null) {
//                row1.createCell(4).setCellValue("RKAP Nasional " + DateUtil.getYear(getPolicyDateFrom()));
//                if (amountRKAP != null) {
//                    row1.createCell(5).setCellValue(amountRKAP.doubleValue());
//                }
//            }

            //bikin header
            XSSFRow row0 = sheet.createRow(1);
            row0.createCell(0).setCellValue("Tgl Mutasi");
            row0.createCell(1).setCellValue("Cabang");
            row0.createCell(2).setCellValue("No Bukti");
            row0.createCell(3).setCellValue("No Akun");
            row0.createCell(4).setCellValue("Keterangan");
            row0.createCell(5).setCellValue("Nilai");
            row0.createCell(6).setCellValue("Saldo");
            row0.createCell(7).setCellValue("Pemilik");
            row0.createCell(8).setCellValue("Pengguna");
            if (FLT_RKAPPEM) {
                row0.createCell(9).setCellValue("Biaya");
            }

            amount = BDUtil.add(amount, h.getFieldValueByFieldNameBD("amount"));
            //amountRKAP = h.getFieldValueByFieldNameBD("insured_amount");

            if (i == 0) {
                saldo = BDUtil.sub(amountRKAP, saldo);

                XSSFRow row = sheet.createRow(i + 2);
                if (Tools.isNo(getStRecapFlag())) {
                    row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
                    row.createCell(1).setCellValue(getCostCenter(h.getFieldValueByFieldNameST("cc_code")).getStDescription());
                    row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
                    row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
                    if (h.getFieldValueByFieldNameST("description") != null) {
                        row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
                    }
                    row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
                    row.createCell(6).setCellValue(BDUtil.sub(saldo, h.getFieldValueByFieldNameBD("amount")).doubleValue());
                    row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("owner_code"));
                    row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("user_code"));
                    if (FLT_RKAPPEM) {
                        row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pms"));
                    }
                }
                if (Tools.isYes(getStRecapFlag())) {
                    String description = null;
                    description = Parameter.readStringAccounts("ACCOUNT_" + h.getFieldValueByFieldNameST("accountno").substring(0, 5));

                    row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
                    row.createCell(4).setCellValue(description);
                    row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
                    row.createCell(6).setCellValue(BDUtil.sub(saldo, h.getFieldValueByFieldNameBD("amount")).doubleValue());
                    row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("owner_code"));
                    row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("user_code"));
                }
            } else if (i > 0) {
                XSSFRow row = sheet.createRow(i + 2);
                if (Tools.isNo(getStRecapFlag())) {
                    row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
                    row.createCell(1).setCellValue(getCostCenter(h.getFieldValueByFieldNameST("cc_code")).getStDescription());
                    row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
                    row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
                    if (h.getFieldValueByFieldNameST("description") != null) {
                        row.createCell(4).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
                    }
                    row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
                    row.createCell(6).setCellValue(BDUtil.sub(saldo, amount).doubleValue());
                    row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("owner_code"));
                    row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("user_code"));
                    if (FLT_RKAPPEM) {
                        row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pms"));
                    }
                }

                if (Tools.isYes(getStRecapFlag())) {
                    String description = null;
                    description = Parameter.readStringAccounts("ACCOUNT_" + h.getFieldValueByFieldNameST("accountno").substring(0, 5));

                    row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
                    row.createCell(4).setCellValue(description);
                    row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
                    row.createCell(6).setCellValue(BDUtil.sub(saldo, amount).doubleValue());
                }
            }
        }

        for (int i = 0; i < list2.size(); i++) {
            HashDTO h = (HashDTO) list2.get(i);

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("Cabang");
            row0.createCell(1).setCellValue("Nilai");

            XSSFRow row = sheet2.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_PAJAK() throws Exception {
        final boolean FLT_OS_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_TAX"));
        final boolean FLT_PAID_TAX = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_PAID_TAX"));

        final SQLAssembler sqa = new SQLAssembler();

        String tax = null;

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                tax = "a.tax_code in (1,4,7)";
            } else if (stTax.equalsIgnoreCase("2")) {
                tax = "a.tax_code in (2,5,8)";
            } else if (stTax.equalsIgnoreCase("3")) {
                tax = "a.tax_code = 10";
            }
        }

        sqa.addSelect(
                " a.ar_invoice_id,a.no_surat_hutang,b.ent_name as prod_name,coalesce(a.amount_settled,0) as ap_tax_p, "
                + " e.receipt_no,e.receipt_date,e.create_who, "
                + " d.policy_date,a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,d.cust_name,d.premi_total,"
                + " coalesce(d.nd_pcost,0) as nd_pcost,coalesce(d.nd_sfee,0) as nd_sfee,"
                + " (coalesce(d.nd_disc1,0)+coalesce(d.nd_disc2,0)) as nd_disc1, "
                + " (select sum(coalesce(a.amount,0)) from ins_pol_items a "
                + " where a.pol_id = d.pol_id and a.ins_item_id in (11,12,13,18,19,20,25,26,27,32,33,34,70) and "
                + tax + " ) as komisi,"
                + " (select sum(coalesce(a.tax_amount,0)) from ins_pol_items a "
                + " where a.pol_id = d.pol_id and a.ins_item_id in (11,12,13,18,19,20,25,26,27,32,33,34,70) and "
                + tax + " ) as pajak_komisi ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ar_invoice_details c on a.ar_invoice_id = c.ar_invoice_id "
                + " left join ins_policy d on d.pol_id = a.attr_pol_id "
                + " left join ar_receipt_lines e on e.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("a.ar_trx_type_id in (11,12)");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (FLT_OS_TAX) {
            sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        }

        if (FLT_PAID_TAX) {
            sqa.addClause("e.receipt_no is not null");
        }

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
                sqa.addPar(Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
                sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
        }

//        if (policyDateFrom != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(policyDateFrom);
//        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',e.receipt_date) >= ?");
            sqa.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',e.receipt_date) <= ?");
            sqa.addPar(paymentDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("d.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("d.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("d.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("d.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("d.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("e.create_who = ?");
            sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("b.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("b.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stTax != null) {
            if (stTax.equalsIgnoreCase("1")) {
                sqa.addClause("c.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            } else if (stTax.equalsIgnoreCase("2")) {
                sqa.addClause("c.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107)");
            } else if (stTax.equalsIgnoreCase("3")) {
                sqa.addClause("c.ar_trx_line_id = 96");
            }
        }

        if (stReceiptNo != null) {
            sqa.addClause("e.receipt_no like ?");
            sqa.addPar('%' + stReceiptNo + '%');
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        final String sql = sqa.getSQL() + " order by d.cc_code,e.receipt_no,a.attr_pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_PAJAK() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal Polis : " + DateUtil.getDateStr(getPolicyDateFrom()) + " sd " + DateUtil.getDateStr(getPolicyDateTo()));

            XSSFRow row2 = sheet.createRow(1);
            if (getPaymentDateFrom() != null) {
                row2.createCell(0).setCellValue("Tanggal Bayar : " + DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));
            } else if (getPerDateFrom() != null) {
                row2.createCell(0).setCellValue("Per Tanggal : " + DateUtil.getDateStr(getPerDateFrom()));
            }

            XSSFRow row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue("Cabang : " + getStBranchDesc() != null ? getStBranchDesc() : getStBranchName());

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("tgl polis");
            row0.createCell(1).setCellValue("no polis");
            row0.createCell(2).setCellValue("nama tertanggung");
            row0.createCell(3).setCellValue("premi bruto");
            row0.createCell(4).setCellValue("biapol");
            row0.createCell(5).setCellValue("biamat");
            row0.createCell(6).setCellValue("diskon");
            row0.createCell(7).setCellValue("komisi");
            row0.createCell(8).setCellValue("tax komisi");
            row0.createCell(9).setCellValue("surat hutang");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi_total").doubleValue());
            if (h.getFieldValueByFieldNameBD("nd_pcost") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nd_pcost").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_sfee") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_sfee").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_disc1") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nd_disc1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("komisi") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("komisi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("pajak_komisi") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("pajak_komisi").doubleValue());
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("no_surat_hutang"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    private void loadFormListRKAP() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/prodrpt")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
    }

    public void clickPrintRKAP() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        loadFormListRKAP();

        final DTOList l = loadListRKAP();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final ArrayList plist = new ArrayList();

        plist.add(stReport + "_" + stPolicyTypeGroupID + "_" + stPolicyTypeID);

        plist.add(stReport + "_" + stPolicyTypeGroupID);

        plist.add(stReport + "_" + stFltTreatyType);

        plist.add(stReport + "_" + stRecapFlag);

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

    private DTOList loadListRKAP() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_RKAP", stReport);

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

    public void clickPrintExcelRKAP() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcelRKAP();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        exportExcelRKAP();
    }

    public void exportExcelRKAP() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_RKAP", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_RKAP", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcelRKAP() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_RKAP", stReport);

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

    public void EXCEL_OUTSTANDINGPREMI2() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,e.ent_id::text,e.ent_name,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,substr(a.refid1,6,2) as inst,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,"
                + "a.mutation_date,a.due_date,a.receipt_date,a.ccy_rate,b.endorse_mode,a.receipt_no,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"//+ "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                //+ "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                //+ "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master f on f.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        //sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            query = query + " and a.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_date as tgl_bayar,a.receipt_no as nobukti,';'||a.attr_pol_no as nopol,(a.inst::numeric+1) as inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn as ppn,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn))) as tag_netto,a.endorse_mode as tipe_endorse "
                + " from ( " + sqa.getSQL() + " group by a.receipt_date,a.cc_code,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.endorse_mode,a.receipt_no "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                //+ " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,a.ar_invoice_id,a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100_" + System.currentTimeMillis() + ".csv";

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

    public BiayaOperasionalGroup getPolGroup() {
        final BiayaOperasionalGroup polgroup = (BiayaOperasionalGroup) DTOPool.getInstance().getDTO(BiayaOperasionalGroup.class, stPolicyTypeGroupID);

        return polgroup;
    }

    public BiayaOperasionalDetail getPolType() {
        final BiayaOperasionalDetail poltype = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, stPolicyTypeID);

        return poltype;
    }

    public ValueSetView getValueset() {
        final ValueSetView value = (ValueSetView) DTOPool.getInstance().getDTO(ValueSetView.class, stReport);

        return value;
    }

    public void EXCEL_PANJARKLAIM2() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,coalesce(a.pla_date,a.dla_date) as tgl_dla,a.claim_payment_date as tgl_bayar,a.entity_id,coalesce(a.pla_no,a.dla_no) as dla_no,';'||a.pol_no as pol_no,a.claim_amount,a.receipt_no ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ent_master b on b.ent_id = a.entity_id ");

        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'N'");
        sqa.addClause("a.status in('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.claim_payment_used_f = 'Y'");

        if (paymentDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_payment_date) >= '" + paymentDateFrom + "'");
            //.addPar(paymentDateFrom);
        }

        if (paymentDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_payment_date) <= '" + paymentDateTo + "'");
            //sqa.addPar(paymentDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.claim_payment_date is null or date_trunc('day',a.claim_payment_date) < '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("b.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            //sqa.addPar('%' + stDlaNo + '%');
        }

        String sql = sqa.getSQL() + " order by a.cc_code,a.pol_no,a.pol_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "Laporan Rincian Panjar Klaim (sd hari ini belum setujui)_" + System.currentTimeMillis() + ".csv";

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

    }

    /**
     * @return the stRecapFlag
     */
    public String getStRecapFlag() {
        return stRecapFlag;
    }

    /**
     * @param stRecapFlag the stRecapFlag to set
     */
    public void setStRecapFlag(String stRecapFlag) {
        this.stRecapFlag = stRecapFlag;
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
     * @return the stPeriodMonth
     */
    public String getStPeriodMonth() {
        return stPeriodMonth;
    }

    /**
     * @param stPeriodMonth the stPeriodMonth to set
     */
    public void setStPeriodMonth(String stPeriodMonth) {
        this.stPeriodMonth = stPeriodMonth;
    }

    /**
     * @return the stPeriodMonthDesc
     */
    public String getStPeriodMonthDesc() {
        return stPeriodMonthDesc;
    }

    /**
     * @param stPeriodMonthDesc the stPeriodMonthDesc to set
     */
    public void setStPeriodMonthDesc(String stPeriodMonthDesc) {
        this.stPeriodMonthDesc = stPeriodMonthDesc;
    }

    public void EXCEL_DETAILOBJEK_XLS() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(" from detil_objek_" + stPeriodMonthDesc + year);

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stTime != null) {
            if (stTime.equalsIgnoreCase("1")) {
                sqa.addClause(" masa_asuransi > '1' ");
            } else if (stTime.equalsIgnoreCase("2")) {
                sqa.addClause(" masa_asuransi <= '1' ");
            }
        }

        if (stPolCredit != null) {
            sqa.addClause("pol_type_id not in (21,59,31,32,33,80,87,88)");
        }

        String sql = sqa.getSQL() + " order by ins_policy_type_grp_id,pol_type_id,nomor ";

        SQLUtil S = new SQLUtil();

        String nama_file = "detil_objek_" + System.currentTimeMillis() + ".csv";

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

    /**
     * @return the stTime
     */
    public String getStTime() {
        return stTime;
    }

    /**
     * @param stTime the stTime to set
     */
    public void setStTime(String stTime) {
        this.stTime = stTime;
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

    public DTOList EXCEL_OUTSTANDINGPREMIINTERN_WITHAGING() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,substr(a.refid1,6,2) as inst,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,"
                + "a.mutation_date,a.due_date,a.receipt_no,a.receipt_date,a.ccy_rate,date_part('day','" + perDateFrom + "'-a.mutation_date) as hari,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,a.attr_pol_type_id,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master f on f.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");

        String query = " substr(a.attr_pol_no,1,16)||a.ar_trx_type_id in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16)||a.ar_trx_type_id as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            } else {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
        }

        if (getStBranch() != null) {
            query = query + " and a.cc_code = '" + stBranch + "'";
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
        }

        query = query + " group by substr(a.attr_pol_no,1,16),a.ar_trx_type_id ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ? ");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ? ");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = ? ");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = ? ");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = ? ");
            sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ? ");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ? ");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ? ");
            sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = ? ");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ? ");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ? ");
            sqa.addPar(stCustCategory1);
        }

        String sql = "select a.cc_code as koda,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,a.invoice_no,a.attr_pol_no as nopol,(a.inst::numeric+1) as inst,"
                + " a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,a.create_who as createwho "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,substr(a.refid1,6,1),a.kodeko ";

        String sql2 = "select a.cc_code as koda,sum(getpremi2(a.hari<=60,a.premi_total)) as kurang,sum(getpremi2(a.hari>60,a.premi_total)) as lebih "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,a.attr_pol_type_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0) group by a.cc_code order by a.cc_code ";

        String sql3 = "select a.attr_pol_type_id as poltype,sum(getpremi2(a.hari<=60,a.premi_total)) as kurang,sum(getpremi2(a.hari>60,a.premi_total)) as lebih "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,a.attr_pol_type_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0) group by a.attr_pol_type_id order by a.attr_pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        final DTOList l3 = ListUtil.getDTOListFromQuery(
                sql3,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", l3);

        return l;
    }

    public DTOList EXCEL_OUTSTANDINGPREMI_WITHAGING() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,"
                + "a.mutation_date,a.due_date,a.receipt_no,a.receipt_date,a.ccy_rate,date_part('day','" + perDateFrom + "'-a.mutation_date) as hari,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,a.attr_pol_type_id,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master f on f.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id ");

        sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");

        String query = " substr(a.attr_pol_no,1,16) in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            } else {
                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
        }

        if (getStBranch() != null) {
            query = query + " and a.cc_code = '" + stBranch + "'";
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
        }

        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ? ");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ? )");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = ? ");
            sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = ? ");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = ? ");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = ? ");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = ? ");
            sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = ? ");
            sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = ? ");
            sqa.addPar(stMarketerOffID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ? ");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ? ");
            sqa.addPar(stCustCategory1);
        }

        String sql = "select a.cc_code as koda,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tgl_bayar,a.attr_pol_no as nopol,(substr(a.refid1,6,1)::numeric+1) as inst,"
                + " a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,a.create_who as createwho "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,substr(a.refid1,6,1),a.kodeko ";

        String sql2 = "select a.cc_code as koda,sum(getpremi2(a.hari<=60,a.premi_total)) as kurang,sum(getpremi2(a.hari>60,a.premi_total)) as lebih "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,a.attr_pol_type_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0) group by a.cc_code order by a.cc_code ";

        String sql3 = "select a.attr_pol_type_id as poltype,sum(getpremi2(a.hari<=60,a.premi_total)) as kurang,sum(getpremi2(a.hari>60,a.premi_total)) as lebih "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,a.cc_code,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,a.attr_pol_type_id "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0) group by a.attr_pol_type_id order by a.attr_pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        final DTOList l3 = ListUtil.getDTOListFromQuery(
                sql3,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", l3);

        return l;

    }

    public void EXCEL_OUTSTANDINGPREMI_JP() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code as koda,a.approved_date as tglsetujui,a.policy_date as tglpolis,"
                + "a.pol_id,';'||a.pol_no as pol_no,a.cust_name as tertanggung,"
                + "a.coins_pol_no as no_polis_rujukan,a.ccy,a.ccy_rate,"
                + "getpremi(a.status = 'ENDORSE',a.insured_amount_e*a.ccy_rate,a.insured_amount*a.ccy_rate) as insured_amount,(a.premi_total*a.ccy_rate) as premi_total,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (29,22,36,15) and a.pol_id = x.pol_id),0) as biapol,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (28,21,35,14) and a.pol_id = x.pol_id),0) as biamat,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11,112,116,117) and a.pol_id = x.pol_id),0) as komisi,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (25,18,32,11,112,116,117) and x.tax_code in (1,2) and a.pol_id = x.pol_id),0) as tax_komisi,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and a.pol_id = x.pol_id),0) as bfee,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (26,19,33,12,88,89,90,100,105,106,107,108) and x.tax_code in (4,5,6) and a.pol_id = x.pol_id),0) as tax_bfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and a.pol_id = x.pol_id),0) as hfee,"
                + "coalesce((select sum(x.tax_amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (27,20,34,13) and x.tax_code in (9) and a.pol_id = x.pol_id),0) as tax_hfee,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (30,23,37,16) and a.pol_id = x.pol_id),0) as diskon,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (68,67,69,66,92,93,94,95,96,97,98,99) and a.pol_id = x.pol_id),0) as feebase,"
                + "coalesce((select sum(x.amount*a.ccy_rate) from ins_pol_items x where x.ins_item_id in (59,58,71,57,76,78,85,86,87,91) and a.pol_id = x.pol_id),0) as ppn,"
                + "sum(i.premi_amount*a.ccy_rate) as premi_jp,sum(i.ricomm_amt*a.ccy_rate) as comm_jp,"
                + "b.ar_invoice_id,b.mutation_date as tglclosing,b.invoice_no as nobuk,b.amount ");

        sqa.addQuery(" from ins_policy a "
                + "left join ar_invoice b on b.attr_pol_id = a.pol_id and b.ar_trx_type_id = 13 "
                + "and coalesce(b.cancel_flag,'') <> 'Y' and b.refd1 = 'Join Placement' "
                + "inner join ins_pol_obj c on c.pol_id=a.pol_id  "
                + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id  "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id  "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id  "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type and j.treaty_type = 'JP'  ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.cover_type_code = 'COINSIN'");
        sqa.addClause("a.coins_pol_no is not null");

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.policy_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
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

        if (stPolicyNo != null) {
            sqa.addClause("a.coins_pol_no = '" + stPolicyNo + "' ");
//            sqa.addPar(stPolicyNo);
        }

        String sql = "select koda,tglsetujui,tglpolis,pol_id,pol_no,tertanggung,no_polis_rujukan,ccy,ccy_rate,"
                + "insured_amount,premi_total,biapol,biamat,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,diskon,feebase,ppn,"
                + "((biamat+biapol)+a.premi_total)-((feebase+diskon)+((hfee+bfee)+(komisi+ppn))) as tag_netto,"
                + "premi_jp,comm_jp,ar_invoice_id,tglclosing,nobuk,amount from ( "
                + sqa.getSQL() + " group by a.cc_code,a.approved_date,a.policy_date,a.pol_id,a.pol_no,a.cust_name,a.coins_pol_no,a.ccy,a.ccy_rate,"
                + "b.ar_invoice_id,b.mutation_date,b.invoice_no,b.amount order by a.policy_date,a.pol_no ) a where (premi_total <> 0 or biamat <> 0 or "
                + "biapol <> 0 or hfee <> 0 or bfee <> 0 or komisi <> 0 or feebase <> 0 or ppn <> 0 or diskon <> 0) order by a.tglpolis,a.pol_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premiJP_" + System.currentTimeMillis() + ".csv";

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

    /**
     * @return the stCoinsurerID
     */
    public String getStCoinsurerID() {
        return stCoinsurerID;
    }

    /**
     * @param stCoinsurerID the stCoinsurerID to set
     */
    public void setStCoinsurerID(String stCoinsurerID) {
        this.stCoinsurerID = stCoinsurerID;
    }

    /**
     * @return the stReinsID
     */
    public String getStReinsID() {
        return stReinsID;
    }

    /**
     * @param stReinsID the stReinsID to set
     */
    public void setStReinsID(String stReinsID) {
        this.stReinsID = stReinsID;
    }

    public void EXPORT_RECEIPT_REINS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        //HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");
        //HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(DateUtil.getDateStr(getPaymentDateFrom()) + " sd " + DateUtil.getDateStr(getPaymentDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(4);
            row0.createCell(0).setCellValue("tgl bayar");
            row0.createCell(1).setCellValue("no bukti");
            row0.createCell(2).setCellValue("surat hutang");
            row0.createCell(3).setCellValue("tertanggung");
            row0.createCell(4).setCellValue("tgl polis");
            row0.createCell(5).setCellValue("no polis");
            row0.createCell(6).setCellValue("premi");
            row0.createCell(7).setCellValue("premi dibayar");
            row0.createCell(8).setCellValue("komisi");
            row0.createCell(9).setCellValue("komisi dibayar");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            if (h.getFieldValueByFieldNameST("attr_pol_name") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("attr_pol_name"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nd_brok1").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("nd_brok1pct").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_brok2").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nd_brok2pct").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_MUTASIRECEIPT() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" row_number() over(order by a.trx_id) as no,a.trx_id,a.accountid,"
                + "a.applydate as tanggal,a.trx_no as nobuk,b.accountno as norek,"
                + "a.description as keterangan,a.debit,a.credit,a.pol_no,a.owner_code,a.user_code,"
                + "getsumbis(substr(a.pol_no,2,1)) as sumbis,a.hdr_accountno as bank,a.ref_trx_type as invoice ");

        sqa.addQuery(" from gl_je_detail a  "
                + "left join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause("((substr(b.accountno,1,3)) not between '122' and '122')");
        sqa.addClause("b.acctype is null");
        sqa.addClause("a.flag = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(policyDateFrom);

            sqa.addClause("a.fiscal_year = ?");
            sqa.addPar(DateUtil.getYear(policyDateFrom));

            sqa.addClause("a.period_no = ?");
            sqa.addPar(DateUtil.getMonthDigit(policyDateFrom));
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(policyDateTo);
        }

        String trx = "select a.receipt_no from ar_receipt a "
                + "inner join ar_settlement b on b.ar_settlement_id = a.ar_settlement_id "
                + "where a.ar_settlement_id in (38,39,40,41) and a.posted_flag = 'Y'";

        if (policyDateFrom != null) {
            trx = trx + "and a.years = '" + DateUtil.getYear(policyDateFrom) + "' and a.months = '" + DateUtil.getMonth2Digit(policyDateFrom) + "' ";
        }

        sqa.addClause("a.trx_no in (" + trx + ")");

        final String sql = sqa.getSQL() + " order by a.trx_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" row_number() over(order by a.trx_id) as no,a.trx_id,a.accountid,"
                + "a.applydate as tanggal,a.trx_no as nobuk,b.accountno as norek,"
                + "a.description as keterangan,a.debit,a.credit,a.pol_no,a.owner_code,a.user_code,"
                + "getsumbis(substr(a.pol_no,2,1)) as sumbis,a.hdr_accountno as bank,a.ref_trx_type as invoice ");

        sqa2.addQuery(" from gl_je_detail a  "
                + "left join gl_accounts b on b.account_id = a.accountid ");

        sqa2.addClause("((substr(b.accountno,1,3)) not between '122' and '122')");
        sqa2.addClause("b.acctype is null");
        sqa2.addClause("a.flag = 'Y'");

        if (policyDateFrom != null) {
            sqa2.addClause("date_trunc('day',a.applydate) >= ?");
            sqa2.addPar(policyDateFrom);

            sqa2.addClause("a.fiscal_year = ?");
            sqa2.addPar(DateUtil.getYear(policyDateFrom));

            sqa2.addClause("a.period_no = ?");
            sqa2.addPar(DateUtil.getMonthDigit(policyDateFrom));
        }

        if (policyDateTo != null) {
            sqa2.addClause("date_trunc('day',a.applydate) <= ?");
            sqa2.addPar(policyDateTo);
        }

        String trx2 = "select a.receipt_no from ar_receipt a "
                + "inner join ar_settlement b on b.ar_settlement_id = a.ar_settlement_id "
                + "where a.ar_settlement_id in (38,39,40,41) and a.posted_flag = 'Y'";

        if (policyDateFrom != null) {
            trx2 = trx2 + "and a.years = '" + DateUtil.getYear(policyDateFrom) + "' and a.months = '" + DateUtil.getMonth2Digit(policyDateFrom) + "' ";
        }

        sqa2.addClause("a.trx_no not in (" + trx2 + ")");

        final String sql2 = sqa2.getSQL() + " order by a.trx_id ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        final SQLAssembler sqa3 = new SQLAssembler();

        sqa3.addSelect("a.receipt_no,b.description ");

        sqa3.addQuery(" from ar_receipt a "
                + "inner join ar_settlement b on b.ar_settlement_id = a.ar_settlement_id ");

        sqa3.addClause("a.ar_settlement_id in (38,39,40,41)");
        sqa3.addClause("a.posted_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa3.addClause("a.years = ?");
            sqa3.addPar(DateUtil.getYear(policyDateFrom));

            sqa3.addClause("a.months = ?");
            sqa3.addPar(DateUtil.getMonth2Digit(policyDateFrom));
        }

        final String sql3 = sqa3.getSQL() + " order by a.receipt_no ";

        final DTOList l3 = ListUtil.getDTOListFromQuery(
                sql3,
                sqa3.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", l3);

        final SQLAssembler sqa4 = new SQLAssembler();

        sqa4.addSelect("a.receipt_no,b.description ");

        sqa4.addQuery(" from ar_receipt a "
                + "inner join ar_settlement b on b.ar_settlement_id = a.ar_settlement_id ");

        sqa4.addClause("a.ar_settlement_id not in (38,39,40,41)");
        sqa4.addClause("a.posted_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa4.addClause("a.years = ?");
            sqa4.addPar(DateUtil.getYear(policyDateFrom));

            sqa4.addClause("a.months = ?");
            sqa4.addPar(DateUtil.getMonth2Digit(policyDateFrom));
        }

        final String sql4 = sqa4.getSQL() + " order by a.receipt_no ";

        final DTOList l4 = ListUtil.getDTOListFromQuery(
                sql4,
                sqa4.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT4", l4);

        return l;

    }

    public void EXPORT_MUTASIRECEIPT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheetIncl = wb.createSheet("Incl.Sentra");
        XSSFSheet sheetExcl = wb.createSheet("Excl.Sentra");
        XSSFSheet sheetPembIncl = wb.createSheet("Pemb.Incl.Sentra");
        XSSFSheet sheetPembExcl = wb.createSheet("Pemb.Excl.Sentra");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        final DTOList list3 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");
        final DTOList list4 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT4");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheetIncl.createRow(0);
            row0.createCell(0).setCellValue("no");
            row0.createCell(1).setCellValue("trx_id");
            row0.createCell(2).setCellValue("accountid");
            row0.createCell(3).setCellValue("tanggal");
            row0.createCell(4).setCellValue("nobuk");
            row0.createCell(5).setCellValue("norek");
            row0.createCell(6).setCellValue("keterangan");
            row0.createCell(7).setCellValue("debit");
            row0.createCell(8).setCellValue("credit");
            row0.createCell(9).setCellValue("pol_no");
            row0.createCell(10).setCellValue("sumbis");
            row0.createCell(11).setCellValue("bank");
            row0.createCell(12).setCellValue("invoice");

            //bikin isi cell
            XSSFRow row = sheetIncl.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("no").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("trx_id").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("accountid").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tanggal"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("norek"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("keterangan"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bank"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("invoice"));

        }

        for (int i = 0; i < list2.size(); i++) {
            HashDTO h = (HashDTO) list2.get(i);

            //bikin header
            XSSFRow row0 = sheetExcl.createRow(0);
            row0.createCell(0).setCellValue("no");
            row0.createCell(1).setCellValue("trx_id");
            row0.createCell(2).setCellValue("accountid");
            row0.createCell(3).setCellValue("tanggal");
            row0.createCell(4).setCellValue("nobuk");
            row0.createCell(5).setCellValue("norek");
            row0.createCell(6).setCellValue("keterangan");
            row0.createCell(7).setCellValue("debit");
            row0.createCell(8).setCellValue("credit");
            row0.createCell(9).setCellValue("pol_no");
            row0.createCell(10).setCellValue("sumbis");
            row0.createCell(11).setCellValue("bank");
            row0.createCell(12).setCellValue("invoice");

            //bikin isi cell
            XSSFRow row = sheetExcl.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("no").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("trx_id").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("accountid").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tanggal"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("norek"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("keterangan"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bank"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("invoice"));

        }

        for (int i = 0; i < list3.size(); i++) {
            HashDTO h = (HashDTO) list3.get(i);

            //bikin header
            XSSFRow row0 = sheetPembIncl.createRow(0);
            row0.createCell(0).setCellValue("receipt_no");
            row0.createCell(1).setCellValue("description");

            //bikin isi cell
            XSSFRow row = sheetPembIncl.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));

        }

        for (int i = 0; i < list4.size(); i++) {
            HashDTO h = (HashDTO) list4.get(i);

            //bikin header
            XSSFRow row0 = sheetPembExcl.createRow(0);
            row0.createCell(0).setCellValue("receipt_no");
            row0.createCell(1).setCellValue("description");

            //bikin isi cell
            XSSFRow row = sheetPembExcl.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXCEL_OUTSTANDINGPREMI_FAC() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code as koda,a.approved_date as tglsetujui,a.policy_date as tglpolis,a.pol_id,';'||a.pol_no as pol_no,"
                + "a.cust_name as tertanggung,d.ent_name as reasuradur,"
                + "a.ccy,a.ccy_rate,sum(getpremi2(c.ar_trx_line_id = 66,c.amount)) as premi_fac,"
                + "sum(getpremi2(c.ar_trx_line_id = 67,c.amount)) as comm_fac,"
                + "b.ar_invoice_id,b.mutation_date as tglclosing,b.invoice_no as nobuk,b.amount ");

        sqa.addQuery(" from ins_policies a "
                + "left join ar_invoice b on b.attr_pol_id = a.pol_id and b.ar_trx_type_id = 13 "
                + "and coalesce(b.cancel_flag,'') <> 'Y' and b.refd1 = 'FAC' "
                + "inner join ar_invoice_details c on c.ar_invoice_id = b.ar_invoice_id "
                + "inner join ent_master d on d.ent_id = b.ent_id ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag = 'Y'");

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.policy_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.policy_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause(" (b.receipt_date is null or date_trunc('day',b.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
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
//            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        String sql = "select koda,tglsetujui,tglpolis,pol_id,pol_no,tertanggung,reasuradur,ccy,ccy_rate,"
                + "premi_fac,comm_fac,ar_invoice_id,tglclosing,nobuk,amount from ( "
                + sqa.getSQL() + " group by a.cc_code,a.approved_date,a.policy_date,a.pol_id,a.pol_no,a.cust_name,d.ent_name,"
                + "a.ccy,a.ccy_rate,b.ar_invoice_id,b.mutation_date,b.invoice_no,b.amount ) a order by a.tglpolis,a.pol_no  ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premiFAC_" + System.currentTimeMillis() + ".csv";

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

    public void EXCEL_COMM_PREMI() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("coalesce(b.policy_date,a.mutation_date) as policy_date,"
                + " a.due_date,a.receipt_no,a.receipt_date,b.pol_id,a.cc_code as koda,coalesce(b.pol_no,a.attr_pol_no) as pol_no,"
                + " coalesce(d.ent_name,a.attr_pol_name) as cust_name,coalesce(b.cust_name) as tertanggung,"
                + " b.premi_total,b.premi_netto,coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee,"
                + " (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + " (coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1,"
                + " (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1,"
                + " (coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2,"
                + " (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1,"
                + " coalesce(b.nd_hfee,0) as nd_hfee,"
                + " (coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1,"
                + " (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2,"
                + " (coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1,"
                + " coalesce(b.nd_taxhfee,0) as nd_taxhfee,coalesce(b.nd_ppn,0) as nd_ppn, "
                + " sum(getpremi2(g.category = 'HFEE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_hfee,"
                + " sum(getpremi2(g.category = 'BROKERAGE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_bfee,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class = 'FEEBASE' and f.f_comission = 'Y',round(f.amount,0))) as os_fbase,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_comm,"
                + " sum(getpremi2(g.item_desc like 'PPN%', f.amount)) as os_ppn,c.receipt_date as tgl_premi,c.receipt_no as nobuk_premi ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        /*String getNobukPremi = " left join ( select a.pol_id,string_agg(a.receipt_date::text,'|') as receipt_date,string_agg(a.receipt_no,'|') as receipt_no "
        + " from ( select g.pol_id,g.receipt_date,f.receipt_no from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";*/

        String getNobukPremi = " left join ( select a.pol_id,string_agg(a.receipt_date::text,'|') as receipt_date,string_agg(a.receipt_no,'|') as receipt_no "
                + " from ( select f.pol_id,f.receipt_date,f.receipt_no from ar_invoice f "
                + " inner join ins_policy i on i.pol_id = a.pol_id "
                + " inner join ent_master h on h.ent_id = i.entity_id "
                + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.amount_settled is not null  "
                + " and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7)";

        if (paymentPremiDateFrom != null) {
            getNobukPremi = getNobukPremi + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        } else {
            getNobukPremi = getNobukPremi + " and date_trunc('day',f.receipt_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (paymentPremiDateTo != null) {
            getNobukPremi = getNobukPremi + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        } else {
            getNobukPremi = getNobukPremi + " and date_trunc('day',f.receipt_date) <= '" + perDateFrom + "' ";
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                getNobukPremi = getNobukPremi + " and i.cc_code = '" + stBranch + "'";

                if (stBranchSource != null) {
                    getNobukPremi = getNobukPremi + " and i.cc_code_source = '" + stBranchSource + "'";
                }
                if (stRegionSource != null) {
                    getNobukPremi = getNobukPremi + " and i.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                getNobukPremi = getNobukPremi + " and ((i.cc_code = '" + stBranch + "') or (i.cc_code = '80' and i.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            getNobukPremi = getNobukPremi + " and i.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                getNobukPremi = getNobukPremi + " and i.cc_code = '80'";
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                getNobukPremi = getNobukPremi + " and i.cc_code <> '80'";
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                getNobukPremi = getNobukPremi + " and i.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else {
                getNobukPremi = getNobukPremi + " and i.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            }
        }

        if (stCompanyID != null) {
            getNobukPremi = getNobukPremi + " and h.ref2 = '" + stCompanyID + "'";
        }

//        getNobukPremi = getNobukPremi + " group by g.pol_id,g.receipt_date,f.receipt_no ) a group by a.pol_id ) ";
        getNobukPremi = getNobukPremi + " ) a group by a.pol_id ) ";

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                + " left join ent_master d on d.ent_id = b.entity_id "
                //                + " left join ent_master e on e.ent_id = a.ent_id "
                + " inner join ar_invoice_details f on f.ar_invoice_id = a.ar_invoice_id "
                + " left join ar_trx_line g on g.ar_trx_line_id = f.ar_trx_line_id "
                + getNobukPremi + " c on c.pol_id = a.attr_pol_id ");

        sqa.addClause("substr(a.refid2,1,3) = 'POL'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        //if (FLT_OS_COMM) {
        //    sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        //    sqa.addClause("a.used_flag is null ");
        //}

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = " + stRegion);
            //sqa.addPar(stRegion);
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*String payment = " a.attr_pol_id in ( select g.pol_id from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";*/

        String payment = " a.attr_pol_id in ( select f.attr_pol_id from ar_invoice f "
                //                + "inner join ins_policy i on i.pol_id = f.pol_id "
                //                + "inner join ent_master h on h.ent_id = i.entity_id "
                + "where f.posted_flag = 'Y' and coalesce(f.cancel_flag,'') <> 'Y' and f.amount_settled is not null "
                + "and f.invoice_type in ('AR','AP') and f.ar_trx_type_id in (5,6,7) ";

        if (paymentPremiDateFrom != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (paymentPremiDateTo != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + perDateFrom + "' ";
        }

//        if (stBranch != null) {
//            if (stBranch.equalsIgnoreCase("80")) {
//                payment = payment + " and i.cc_code = '" + stBranch + "'";
//
//                if (stBranchSource != null) {
//                    payment = payment + " and i.cc_code_source = '" + stBranchSource + "'";
//                }
//                if (stRegionSource != null) {
//                    payment = payment + " and i.region_id_source = '" + stRegionSource + "'";
//                }
//            } else {
//                payment = payment + " and ((i.cc_code = '" + stBranch + "') or (i.cc_code = '80' and i.cc_code_source = '" + stBranch + "'))";
//            }
//            //sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            payment = payment + " and i.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }
//
//        if (stBussinessPolType != null) {
//            if (stBussinessPolType.equalsIgnoreCase("1")) {
//                payment = payment + " and i.cc_code = '80'";
//            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
//                payment = payment + " and i.cc_code <> '80'";
//            }
//        }
//
//        if (stBussinessPolTypeCob != null) {
//            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
//                payment = payment + " and i.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
//            } else {
//                payment = payment + " and i.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
//            }
//        }
//
//        if (stCompanyID != null) {
//            payment = payment + " and h.ref2 = '" + stCompanyID + "'";
//        }

        payment = payment + " group by f.attr_pol_id ) ";

        sqa.addClause(payment);

        String sql = " select tglpolis,tgl_jtempo,nobuk,tglbayar,polid,';'||koda as koda,';'||nopol as nopol,nama_bank,tertanggung,premi_total,biapol,biamat,premi_bruto,feebase,ppn,"
                + " diskon,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,os_hfee as huthfee,os_bfee as hutbfee,os_fbase as hutfbase,os_comm as hutkom,os_ppn as hutppn,tgl_premi,nobuk_premi "
                + " from ( select a.policy_date as tglpolis,a.pol_id as polid,"
                + " a.pol_no as nopol,a.koda,a.cust_name as nama_bank,a.tertanggung,a.premi_total,a.nd_pcost as biapol,a.nd_sfee as biamat,"
                + " ((nd_sfee+nd_pcost)+a.premi_total) as premi_bruto,a.nd_feebase1 as feebase,a.nd_ppn as ppn,a.nd_disc1 as diskon,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,"
                + " ((a.nd_comm1+nd_comm2)-(a.nd_taxcomm1+nd_taxcomm2)) as komisi,(a.nd_taxcomm1+nd_taxcomm2) as tax_komisi,"
                + " (nd_brok1-nd_taxbrok1) as bfee,nd_taxbrok1 as tax_bfee,(nd_hfee-nd_taxhfee) as hfee,nd_taxhfee as tax_hfee,"
                + " os_hfee,os_bfee,os_fbase,os_comm,os_ppn,tgl_premi,nobuk_premi from ( " + sqa.getSQL()
                + " group by b.policy_date,b.pol_id,b.pol_no,a.cc_code,d.ent_name,b.premi_total,"
                + " b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,b.nd_feebase1,b.nd_feebase2,a.attr_pol_no,"
                + " a.attr_pol_name,b.cust_name,a.mutation_date,b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,a.due_date,a.receipt_no,a.receipt_date,"
                + " b.nd_comm2,b.nd_comm3,b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee,b.nd_ppn,c.receipt_date,c.receipt_no ";

        sql = sql + " ) a ) a order by a.nopol ";

        SQLUtil S = new SQLUtil();

        String nama_file = "oscomm_" + System.currentTimeMillis() + ".csv";

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

    public void clickPrintExcelFile() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcelFile();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

//        exportExcel();
    }

    private DTOList loadListExcelFile() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY_EXCELFILE");


        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return defaultQuery();
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public DTOList EXCEL_OUTSTANDINGPREMI_FILE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,b.cc_code_source,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,b.prod_name as marketer_ext,"
                + "a.mutation_date,a.due_date,a.receipt_no,a.receipt_date,h.vs_description as trx_method,a.ccy_rate,substr(a.refid1,6,2) as inst_o,b.no as inst,b.inst_date,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,b.pol_type_id,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.attr_pol_id) as jumlah,"
                + "(select string_agg(trim(x.ref1),'|')||'|'||string_agg(trim(x.ref4),'|')||'|'||string_agg(trim(x.ref16),'|') from ins_pol_obj x where x.pol_id = a.attr_pol_id) as debitur,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who,b.period_start,b.period_end,b.marketing_officer_who ");

        String installment = "select row_number() over(partition by c.policy_id order by c.ins_pol_inst_id) as no,b.pol_id,b.pol_no,b.entity_id,b.period_start,b.period_end, "
                + "b.prod_id,b.ref5,b.create_who,c.inst_date,b.ins_policy_type_grp_id,b.pol_type_id,b.cc_code,b.region_id,e.ent_name as marketing_officer_who,b.prod_name, "
                + "b.cc_code_source,b.region_id_source,b.payment_company_id "
                + "from ins_policy b "
                + "left join ins_pol_installment c on c.policy_id = b.pol_id "
                + "left join ent_master e on e.ent_id = b.marketing_officer_who::int "
                + "where b.status in ('POLICY','ENDORSE','RENEWAL') and b.active_flag='Y' and b.effective_flag='Y' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                installment = installment + " and date_trunc('day',b.approved_date) >= '" + policyDateFrom + "'";
            }
        } else {
            installment = installment + " and date_trunc('day',b.approved_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            installment = installment + " and date_trunc('day',b.approved_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                installment = installment + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    installment = installment + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    installment = installment + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                installment = installment + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            installment = installment + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                installment = installment + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                installment = installment + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                installment = installment + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                installment = installment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                installment = installment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyTypeGroupID() != null) {
            installment = installment + " and b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'";
            //sqa.addPar(stBranch);
        }

        if (getStPolicyTypeID() != null) {
            installment = installment + " and b.pol_type_id = '" + stPolicyTypeID + "'";
            //sqa.addPar(stRegion);
        }

        if (getStPolicyNo() != null) {
            installment = installment + " and b.pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStCompanyID() != null) {
            installment = installment + " and e.ref2 = '" + stCompanyID + "'";
            //sqa.addPar(stCompanyID);
        }

        if (getStMarketerOffID() != null) {
            installment = installment + " and b.marketing_officer_who = '" + stMarketerOffID + "'";
//            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        installment = installment + " order by c.ins_pol_inst_id ";


        String cekpolis = "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16) as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a inner join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + "inner join ent_master e on e.ent_id = a.ent_id "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";

        if (getPolicyDateFrom() != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
                //sqa.addPar(policyDateFrom);
            } else {
                cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
            }
        } else {
            cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
            //sqa.addPar(policyDateFrom);
        }

        if (getPolicyDateTo() != null) {
            cekpolis = cekpolis + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

        if (getPerDateFrom() != null) {
            cekpolis = cekpolis + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                cekpolis = cekpolis + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    cekpolis = cekpolis + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    cekpolis = cekpolis + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                cekpolis = cekpolis + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            cekpolis = cekpolis + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                cekpolis = cekpolis + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                cekpolis = cekpolis + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                cekpolis = cekpolis + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                cekpolis = cekpolis + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                cekpolis = cekpolis + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyNo() != null) {
            cekpolis = cekpolis + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStCompanyID() != null) {
            cekpolis = cekpolis + " and e.ref2 = '" + stCompanyID + "'";
            //sqa.addPar(stCompanyID);
        }

        if (getStCustCategory1() != null) {
            cekpolis = cekpolis + " and e.category1 = '" + stCustCategory1 + "'";
            //sqa.addPar(stCustCategory1);
        }

        if (getStPolicyNo() != null) {
            cekpolis = cekpolis + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        if (getStCompanyID() != null) {
            cekpolis = cekpolis + " and e.ref2 = '" + stCompanyID + "'";
            //sqa.addPar(stCompanyID);
        }

        if (getStCustCategory1() != null) {
            cekpolis = cekpolis + " and e.category1 = '" + stCustCategory1 + "'";
            //sqa.addPar(stCustCategory1);
        }

        cekpolis = cekpolis + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ";


        sqa.addQuery(
                " from ar_invoice a "
                //                + " left join ins_policy c on c.pol_id = a.attr_pol_id "
                + " left join ( " + installment + " ) b on b.pol_id = a.attr_pol_id and b.no = substr(a.refid1,6,2)::numeric+1"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                + " inner join ent_master e on e.ent_id = a.ent_id "
                + " inner join ent_master f on f.ent_id = b.entity_id "
                + " inner join ent_master g on g.ent_id = b.prod_id "
                + " inner join ( " + cekpolis + " ) i on i.pol_no = substr(a.attr_pol_no,1,16) "
                + " left join s_valueset h on h.vs_code = a.trx_method and h.vs_group = 'RECEIPT_TRX_METHOD'");

        sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        //sqa.addClause("a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021'");
        //sqa.addClause("a.amount - coalesce(a.amount_settled,0) > 0");

//        String query = " substr(a.attr_pol_no,1,16) in ( "
//                + "select pol_no from ( "
//                + "select substr(a.attr_pol_no,1,16) as pol_no,"
//                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
//                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
//                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
//                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
//                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
//                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
//                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
//                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
//                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
//                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
//                + "inner join ent_master e on e.ent_id = a.ent_id "
//                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
//                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
//                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' ";
//
//        if (getPolicyDateFrom() != null) {
//            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
//                query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
//                //sqa.addPar(policyDateFrom);
//            } else {
//                query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            }
//        } else {
//            query = query + " and date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'";
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (getPolicyDateTo() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            //sqa.addPar(policyDateTo);
//        }
//
//        if (getPerDateFrom() != null) {
//            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
//            //sqa.addPar(perDateFrom);
//        }
//
//        if (getStBranch() != null) {
//            query = query + " and a.cc_code = '" + stBranch + "'";
//            //sqa.addPar(stBranch);
//        }
//
//        if (getStRegion() != null) {
//            query = query + " and b.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }
//
//        if (getStPolicyNo() != null) {
//            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
//            //sqa.addPar('%'+stPolicyNo+'%');
//        }
//
//        if (getStCompanyID() != null) {
//            query = query + " and e.ref2 = '" + stCompanyID + "'";
//            //sqa.addPar(stCompanyID);
//        }
//
//        query = query + " group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) ";
//
//        sqa.addClause(query);

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_OSPREMI")) <= 0) {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
                //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
                //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + Parameter.readString("SPLIT_DATA_OSPREMI") + "-01-01 00:00:00'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,';'||a.cc_code_source as koda_nonaks,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.inst_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tgl_bayar,';'||a.attr_pol_no as nopol,a.inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,"
                + " a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee, "
                + " ((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,';'||a.create_who as createwho,a.period_start,a.period_end,a.marketing_officer_who,a.marketer_ext,"
                + " cekstatus(a.pol_type_id = 59 and a.jumlah=1,debitur) as debitur,a.trx_method "
                + " from ( " + sqa.getSQL() + " group by a.receipt_no,a.receipt_date,h.vs_description,a.cc_code,b.cc_code_source,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,b.prod_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,b.no,b.inst_date,b.pol_type_id,"
                + " b.period_start,b.period_end,b.marketing_officer_who order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,a.ar_invoice_id,a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

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

//        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
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

    public DTOList EXCEL_OUTSTANDINGPREMIINTERN_FILE() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.ar_invoice_id,a.ar_trx_type_id::text,a.invoice_no,a.refid1,a.cc_code,b.cc_code_source,e.ent_id::text,e.ent_name as sumbis,b.ref5 as principal,"
                + "a.attr_pol_id,a.attr_pol_no,a.attr_pol_name as cust_name,b.prod_id as marketer_id,g.ent_name as marketer_name,b.prod_name as marketer_ext,"
                + "a.mutation_date,a.due_date,a.receipt_date,a.ccy_rate,h.vs_description as trx_method,"
                + "getname(a.ar_trx_type_id in (5,6,7),'00',coalesce(a.kode_ko,e.ref_ent_id)) as kodeko,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 "
                + "else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 "
                + "else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                + "sum(getpremi2(d.category = 'TAXCOMM', c.amount)) as nd_taxcomm1,"
                //+ "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                + "sum(getpremi2(d.category = 'TAXHFEE', c.amount)) as nd_taxhfee,"
                //+ "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                //+ "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1,"
                + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1,"
                + "coalesce(sum(c.amount_settled),0) as premi_paid,b.create_who,b.period_start,b.period_end,b.marketing_officer_who ");

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id"
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id"
                + " left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //                + " left join ent_master e on e.ent_id = a.ent_id "
                + " left join ent_master e on e.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = b.prod_id "
                + " left join s_valueset h on h.vs_code = a.trx_method and h.vs_group = 'RECEIPT_TRX_METHOD' ");

        sqa.addClause("a.posted_flag = 'Y'");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (5,6,7,10)");

        String query = " substr(a.attr_pol_no,1,16)||a.ar_trx_type_id in ( "
                + "select pol_no from ( "
                + "select substr(a.attr_pol_no,1,16)||a.ar_trx_type_id as pol_no,"
                + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) "
                + "else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
//                + "sum(getpremi2((d.category = 'BROKERAGE' and d.item_desc = 'PPN') or (d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base'), c.amount)) as nd_ppn,"
                + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn_bfee,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Fee Base', c.amount)) as nd_ppn_fbase,"
                + "sum(getpremi2(d.category = 'COMMISSION' and d.item_desc = 'PPN Komisi', c.amount)) as nd_ppn_komisi,"
                + "sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                + "from ar_invoice a left join ins_policy b on b.pol_id = a.attr_pol_id "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                //+ "where a.posted_flag = 'Y' and coalesce(a.negative_flag,'') <> 'Y' "
                + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) ";

        if (getPolicyDateFrom() != null) {
            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
        }

        if (getPolicyDateTo() != null) {
            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
            //sqa.addPar(policyDateTo);
        }

//        if (getPolicyDateFrom() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (getPolicyDateTo() != null) {
//            query = query + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            //sqa.addPar(policyDateTo);
//        }

        if (getPerDateFrom() != null) {
            query = query + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
            //sqa.addPar(perDateFrom);
        }

        if (getStBranch() != null) {
            if (getStBranch().equalsIgnoreCase("80")) {
                query = query + " and b.cc_code = '" + stBranch + "'";

                if (getStBranchSource() != null) {
                    query = query + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (getStRegionSource() != null) {
                    query = query + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                query = query + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            query = query + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (getStBussinessPolType() != null) {
            if (getStBussinessPolType().equalsIgnoreCase("1")) {
                query = query + " and b.cc_code = '80'";
            } else if (getStBussinessPolType().equalsIgnoreCase("2")) {
                query = query + " and b.cc_code <> '80'";
            }
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                query = query + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            } else {
                query = query + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)";
            }
        }

        if (getStPolicyNo() != null) {
            query = query + " and a.attr_pol_no = '" + stPolicyNo + "'";
            //sqa.addPar('%'+stPolicyNo+'%');
        }

        query = query + " group by substr(a.attr_pol_no,1,16),a.ar_trx_type_id ) a where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) ) ";

        sqa.addClause(query);

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'");
            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("e.ent_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("a.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
            //sqa.addPar(stCustCategory1);
        }

        /*
        String sql = "select * from ( "+ sqa.getSQL() +" group by a.ar_trx_type_id,a.attr_pol_type_id,a.cc_code,a.attr_pol_id," +
        " a.receipt_date,e.ref_ent_id,e.ent_id,a.mutation_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id " +
        " order by a.cc_code,a.attr_pol_no,a.ar_trx_type_id ) a where premi_total <> 0 " +
        " order by a.cc_code,a.pol_no,a.ar_trx_type_id ";
         */

        String sql = "select ';'||a.cc_code as koda,';'||a.cc_code_source as koda_nonaks,a.attr_pol_id as polid,a.mutation_date as tglpolis,a.due_date as tgl_jtempo,a.receipt_date as tglbayar,a.invoice_no,';'||a.attr_pol_no as nopol,(substr(a.refid1,6,1)::numeric+1) as inst,"
                + " ';'||a.kodeko as kodeko,a.cust_name as nama_bank,a.sumbis,a.marketer_name,principal,a.premi_total as premi_bruto,nd_pcost as biapol,nd_sfee as biamat,a.nd_feebase1 as feebase,a.nd_disc1 as diskon,a.nd_ppn_bfee as ppn_bfee,a.nd_ppn_fbase as ppn_fbase,a.nd_ppn_komisi as ppn_komisi,"
                + " a.nd_comm1 as komisi_excl_tax,a.nd_taxcomm1 as tax_komisi,nd_hfee as hfee_excl_tax,nd_taxhfee as tax_hfee,nd_brok1 as bfee_excl_tax,nd_taxbrok1 as tax_bfee,((nd_sfee+nd_pcost)+a.premi_total)-((a.nd_feebase1+a.nd_disc1)+((nd_hfee+nd_brok1)+(a.nd_comm1+a.nd_ppn_bfee)+(a.nd_ppn_fbase+a.nd_ppn_komisi))) as tag_netto,"
                + " a.period_start,a.period_end,a.marketing_officer_who,a.marketer_ext,a.trx_method "
                + " from ( " + sqa.getSQL() + " group by a.receipt_date,h.vs_description,a.cc_code,b.cc_code_source,a.invoice_no,a.attr_pol_id,e.ref_ent_id,b.ref5,b.prod_id,e.ent_name,g.ent_name,b.prod_name,a.refid1, "
                + " e.ent_id,a.ar_trx_type_id,a.mutation_date,a.due_date,a.attr_pol_no,a.attr_pol_name,a.ccy_rate,a.ar_invoice_id,a.refid0,a.kode_ko,b.create_who,b.period_start,b.period_end,b.marketing_officer_who "
                + " order by a.cc_code,a.attr_pol_no,a.kode_ko ) a "
                + " where (premi_total <> 0 or nd_sfee <> 0 or nd_pcost <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn_bfee <> 0 or nd_ppn_fbase <> 0 or nd_ppn_komisi <> 0 or nd_disc1 <> 0) "
                + " order by a.cc_code,a.attr_pol_no,substr(a.refid1,6,1),a.kodeko ";

        SQLUtil S = new SQLUtil();

        String nama_file = "os_premi100intern_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

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

//        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
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

    public DTOList EXCEL_COMM_FILE() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("coalesce(b.policy_date,a.mutation_date) as policy_date,"
                + " a.due_date,a.receipt_no,a.receipt_date,b.pol_id,a.cc_code as koda,b.cc_code_source as koda_nonaks,coalesce(b.pol_no,a.attr_pol_no) as pol_no,"
                + " coalesce(d.ent_name,a.attr_pol_name) as cust_name,coalesce(b.cust_name) as tertanggung,"
                + " b.premi_total,b.premi_netto,coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee,"
                + " (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + " (coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1,"
                + " (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1,"
                + " (coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2,"
                + " (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1,"
                + " coalesce(b.nd_hfee,0) as nd_hfee,"
                + " (coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1,"
                + " (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2,"
                + " (coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1,"
                + " coalesce(b.nd_taxhfee,0) as nd_taxhfee,coalesce(b.nd_ppn,0) as nd_ppn, "
                + " sum(getpremi2(g.category = 'HFEE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_hfee,"
                + " sum(getpremi2(g.category = 'BROKERAGE' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_bfee,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class = 'FEEBASE' and f.f_comission = 'Y',round(f.amount,0))) as os_fbase,"
                + " sum(getpremi2(g.category = 'COMMISSION' and g.item_class is null and f.f_comission = 'Y',round(f.amount,0))) as os_comm,"
                + " sum(getpremi2(g.item_desc like 'PPN%', f.amount)) as os_ppn ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(
                " from ar_invoice a "
                + " left join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                //                + " left join ar_receipt_lines c on c.ar_invoice_id = a.ar_invoice_id and c.line_type = 'INVOC' "
                //                + " left join ar_receipt h on h.ar_receipt_id = c.receipt_id "
                + " left join ent_master d on d.ent_id = b.entity_id "
                //                + " left join ent_master e on e.ent_id = a.ent_id "
                + " inner join ar_invoice_details f on f.ar_invoice_id = a.ar_invoice_id "
                + " left join ar_trx_line g on g.ar_trx_line_id = f.ar_trx_line_id ");

        sqa.addClause("substr(a.refid2,1,3) = 'POL'");
        sqa.addClause("a.ar_trx_type_id = 11");
        //sqa.addClause("coalesce(a.negative_flag,'') <> 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        //if (FLT_OS_COMM) {
        //    sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0 else a.amount - coalesce(a.amount_settled,0) > 0 end ");
        //    sqa.addClause("a.used_flag is null ");
        //}

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
//        }
//
//        if (perDateFrom != null) {
//            sqa.addClause("date_trunc('day',coalesce(b.policy_date,a.mutation_date)) <= '" + perDateFrom + "'");
//            //sqa.addPar(policyDateTo);
//        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

//        if (stCustCategory1 != null) {
//            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
//            //sqa.addPar(stCustCategory1);
//        }

        /*String payment = " a.attr_pol_id in ( select g.pol_id from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " left join ins_policy i on i.pol_id = g.pol_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";*/

        String payment = " a.attr_pol_id in ( select f.attr_pol_id from ar_invoice f "
                + "inner join ins_policy b on b.pol_id = f.attr_pol_id "
                //                + "inner join ent_master h on h.ent_id = i.entity_id "
                + "where f.posted_flag = 'Y' and coalesce(f.cancel_flag,'') <> 'Y' and f.amount_settled is not null "
                + "and f.invoice_type in ('AR','AP') and f.ar_trx_type_id in (5,6,7) ";

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
            } else {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'";
            }
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (policyDateTo != null) {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + policyDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + perDateFrom + "'";
        }

        if (paymentPremiDateFrom != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (paymentPremiDateTo != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + perDateFrom + "' ";
        }

        if (stPolicyNo != null) {
            payment = payment + " and f.attr_pol_no = '" + stPolicyNo + "' ";
            //sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                payment = payment + " and b.cc_code = '" + stBranch + "'";

                if (stBranchSource != null) {
                    payment = payment + " and b.cc_code_source = '" + stBranchSource + "'";
                }
                if (stRegionSource != null) {
                    payment = payment + " and b.region_id_source = '" + stRegionSource + "'";
                }
            } else {
                payment = payment + " and ((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))";
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            payment = payment + " and b.region_id = '" + stRegion + "'";
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                payment = payment + " and b.cc_code = '80'";
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                payment = payment + " and b.cc_code <> '80'";
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                payment = payment + " and b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
            } else {
                payment = payment + " and b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
            }
        }
//
//        if (stCompanyID != null) {
//            payment = payment + " and h.ref2 = '" + stCompanyID + "'";
//        }


        payment = payment + " group by f.attr_pol_id ) ";

        sqa.addClause(payment);

        String sql = " select tglpolis,tgl_jtempo,nobuk,tglbayar,polid,';'||koda as koda,';'||a.koda_nonaks as koda_nonaks,';'||nopol as nopol,nama_bank,tertanggung,premi_total,biapol,biamat,premi_bruto,feebase,ppn,"
                + " diskon,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,os_hfee as huthfee,os_bfee as hutbfee,os_fbase as hutfbase,os_comm as hutkom,os_ppn as hutppn "
                + " from ( select a.policy_date as tglpolis,a.pol_id as polid,"
                + " a.pol_no as nopol,a.koda,a.koda_nonaks,a.cust_name as nama_bank,a.tertanggung,a.premi_total,a.nd_pcost as biapol,a.nd_sfee as biamat,"
                + " ((nd_sfee+nd_pcost)+a.premi_total) as premi_bruto,a.nd_feebase1 as feebase,a.nd_ppn as ppn,a.nd_disc1 as diskon,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar,"
                + " ((a.nd_comm1+nd_comm2)-(a.nd_taxcomm1+nd_taxcomm2)) as komisi,(a.nd_taxcomm1+nd_taxcomm2) as tax_komisi,"
                + " (nd_brok1-nd_taxbrok1) as bfee,nd_taxbrok1 as tax_bfee,(nd_hfee-nd_taxhfee) as hfee,nd_taxhfee as tax_hfee,"
                + " os_hfee,os_bfee,os_fbase,os_comm,os_ppn from ( " + sqa.getSQL()
                + " group by b.policy_date,b.pol_id,b.pol_no,a.cc_code,d.ent_name,b.premi_total,"
                + " b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,b.nd_feebase1,b.nd_feebase2,a.attr_pol_no,"
                + " a.attr_pol_name,b.cust_name,a.mutation_date,b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,a.due_date,a.receipt_no,a.receipt_date,"
                + " b.nd_comm2,b.nd_comm3,b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee,b.nd_ppn ";

        sql = sql + " ) a ) a order by a.nopol ";

        SQLUtil S = new SQLUtil();

        String nama_file = "oscomm_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

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

//        File file = new File(Parameter.readString("SYS_DB_FOLDER") + nama_file);
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

    public void EXCEL_COMM_INVOICE() throws Exception {
        final boolean FLT_OS_COMM = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_OS_COMM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.cc_code,b.cc_code_source,b.policy_date,a.mutation_date,a.ar_invoice_id,a.due_date,a.receipt_no,a.receipt_date,"
                + "b.pol_id,b.pol_no,b.cust_name,b.premi_total,b.premi_netto,"
                + "coalesce(b.nd_pcost,0) as nd_pcost,coalesce(b.nd_sfee,0) as nd_sfee, (coalesce(b.nd_feebase1,0)+coalesce(b.nd_feebase2,0)) as nd_feebase1,"
                + "(coalesce(b.nd_disc1,0)+coalesce(b.nd_disc2,0)) as nd_disc1, (coalesce(b.nd_comm1,0)+coalesce(b.nd_comm2,0)) as nd_comm1, "
                + "(coalesce(b.nd_comm3,0)+coalesce(b.nd_comm4,0)) as nd_comm2, (coalesce(b.nd_brok1,0)+coalesce(b.nd_brok2,0)) as nd_brok1, coalesce(b.nd_hfee,0) as nd_hfee,"
                + "(coalesce(b.nd_taxcomm1,0)+coalesce(b.nd_taxcomm2,0)) as nd_taxcomm1, (coalesce(b.nd_taxcomm3,0)+coalesce(b.nd_taxcomm4,0)) as nd_taxcomm2, "
                + "(coalesce(b.nd_taxbrok1,0)+coalesce(b.nd_taxbrok2,0)) as nd_taxbrok1, coalesce(b.nd_taxhfee,0) as nd_taxhfee, coalesce(b.nd_ppn,0) as nd_ppn,"
                + "sum(getpremi2(c.ar_trx_line_id in (8,24,40),coalesce(c.amount,0))) as os_COMM, "
                + "sum(getpremi2(c.ar_trx_line_id in (9,25,41,132),coalesce(c.amount,0))) as os_BFEE, "
                + "sum(getpremi2(c.ar_trx_line_id in (10,26,42),coalesce(c.amount,0))) as os_HFEE,"
                + "sum(getpremi2(c.ar_trx_line_id in (92,93,94),coalesce(c.amount,0))) as os_FBASE,"
                + "sum(getpremi2(c.ar_trx_line_id in (82,83,84,133),coalesce(c.amount,0))) as os_PPN_BFEE,"
                + "sum(getpremi2(c.ar_trx_line_id in (129,130,131),coalesce(c.amount,0))) as os_PPN_FBASE ");

        String clauseObj = "";

        if (policyDateFrom != null) {
            clauseObj = " and a.ar_invoice_id > 9999 and a.mutation_date >= '" + policyDateFrom + "' ";
        }

        sqa.addQuery(
                " from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ins_policy b on b.pol_id = a.attr_pol_id " + clauseObj
                + "inner join ent_master d on d.ent_id = b.entity_id ");
//                + "inner join ent_master e on e.ent_id = a.ent_id ");

        sqa.addClause("a.ar_trx_type_id in (5,6,7,11)");
        sqa.addClause("c.tax_flag is null");
        sqa.addClause("c.negative_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("(c.amount_settled <> 0 or c.amount_settled is null)");

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
            } else {
                sqa.addClause("date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'");
//            //sqa.addPar(policyDateFrom);
            }
        } else {
            sqa.addClause("date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01'");
//            //sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',b.policy_date) <= '" + policyDateTo + "'");
//            //sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')");
            //sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
                sqa.addClause("b.cc_code = '" + stBranch + "'");

                if (stBranchSource != null) {
                    sqa.addClause("b.cc_code_source = '" + stBranchSource + "'");
                }
                if (stRegionSource != null) {
                    sqa.addClause("b.region_id_source = '" + stRegionSource + "'");
                }
            } else {
                sqa.addClause("((b.cc_code = '" + stBranch + "') or (b.cc_code = '80' and b.cc_code_source = '" + stBranch + "'))");
            }
            //sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("b.region_id = '" + stRegion + "'");
            //sqa.addPar(stRegion);
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                sqa.addClause("b.cc_code = '80'");
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("b.cc_code <> '80'");
            }
        }

        if (stBussinessPolTypeCob != null) {
            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
                sqa.addClause("b.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else {
                sqa.addClause("b.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            }
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("b.prod_id = '" + stMarketerID + "'");
            //sqa.addPar(stMarketerID);
        }

        if (stMarketerOffID != null) {
            sqa.addClause("b.marketing_officer_who = '" + stMarketerOffID + "'");
            //sqa.addPar(stMarketerOffID);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("b.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("b.pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stCreateID != null) {
            sqa.addClause("b.create_who = '" + stCreateID + "'");
            //sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no = '" + stPolicyNo + "'");
            //sqa.addPar('%' + stPolicyNo + '%');
        }

//        if (stCustCategory1 != null) {
//            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
//            //sqa.addPar(stCustCategory1);
//        }

        /*String payment = " a.attr_pol_id in ( select g.pol_id from ar_receipt f "
        + " inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
        + " inner join ent_master h on h.ent_id = f.entity_id "
        + " left join ins_policy i on i.pol_id = g.pol_id "
        + " where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41,44) and g.pol_id is not null ";
        payment = payment + " group by g.pol_id ) ";*/

        String payment = " a.attr_pol_id in ( select f.attr_pol_id from ar_invoice f "
                + "inner join ins_policy b on b.pol_id = f.attr_pol_id "
                //                + "inner join ent_master h on h.ent_id = i.entity_id "
                + "where f.posted_flag = 'Y' and coalesce(f.cancel_flag,'') <> 'Y' and f.amount_settled is not null "
                + "and f.invoice_type in ('AR','AP') and f.ar_trx_type_id in (5,6,7) ";

        if (policyDateFrom != null) {
            if (Tools.compare(DateUtil.getYear(policyDateFrom), Parameter.readString("SPLIT_DATA_HUTKOM")) <= 0) {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
            } else {
                payment = payment + " and date_trunc('day',b.policy_date) >= '" + policyDateFrom + "'";
            }
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (policyDateTo != null) {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + policyDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',b.policy_date) <= '" + perDateFrom + "'";
        }

        if (paymentPremiDateFrom != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + paymentPremiDateFrom + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) >= '" + Parameter.readString("SPLIT_DATA_HUTKOM") + "-01-01' ";
        }

        if (paymentPremiDateTo != null) {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + paymentPremiDateTo + "'";
        } else {
            payment = payment + " and date_trunc('day',f.receipt_date) <= '" + perDateFrom + "' ";
        }

        if (stPolicyNo != null) {
            payment = payment + " and f.attr_pol_no = '" + stPolicyNo + "' ";
            //sqa.addPar('%' + stPolicyNo + '%');
        }

//        if (stBranch != null) {
//            if (stBranch.equalsIgnoreCase("80")) {
//                payment = payment + " and i.cc_code = '" + stBranch + "'";
//
//                if (stBranchSource != null) {
//                    payment = payment + " and i.cc_code_source = '" + stBranchSource + "'";
//                }
//                if (stRegionSource != null) {
//                    payment = payment + " and i.region_id_source = '" + stRegionSource + "'";
//                }
//            } else {
//                payment = payment + " and ((i.cc_code = '" + stBranch + "') or (i.cc_code = '80' and i.cc_code_source = '" + stBranch + "'))";
//            }
//            //sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            payment = payment + " and i.region_id = '" + stRegion + "'";
//            //sqa.addPar(stRegion);
//        }
//
//        if (stBussinessPolType != null) {
//            if (stBussinessPolType.equalsIgnoreCase("1")) {
//                payment = payment + " and i.cc_code = '80'";
//            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
//                payment = payment + " and i.cc_code <> '80'";
//            }
//        }
//
//        if (stBussinessPolTypeCob != null) {
//            if (stBussinessPolTypeCob.equalsIgnoreCase("1")) {
//                payment = payment + " and i.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)";
//            } else {
//                payment = payment + " and i.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)";
//            }
//        }
//
//        if (stCompanyID != null) {
//            payment = payment + " and h.ref2 = '" + stCompanyID + "'";
//        }


        payment = payment + " group by f.attr_pol_id ) ";

        sqa.addClause(payment);

        String sql = " select ar_invoice_id,tglpolis,tgl_jtempo,nobuk,tglbayar,polid,quote_ident(koda) as koda,quote_ident(koda_nonaks) as koda_nonaks,quote_ident(nopol) as nopol,nama_bank,"
                + "premi_total,biapol,biamat,premi_bruto,feebase,ppn,diskon,komisi,tax_komisi,bfee,tax_bfee,hfee,tax_hfee,os_hfee as huthfee,os_bfee as hutbfee,"
                + "os_fbase as hutfbase,os_comm as hutkom,os_ppn_bfee as hutppn_bfee,os_ppn_fbase as hutppn_fbase from ( "
                + "select a.ar_invoice_id,a.policy_date as tglpolis,a.pol_id as polid, a.pol_no as nopol,a.cc_code as koda,a.cc_code_source as koda_nonaks,a.cust_name as nama_bank,a.premi_total,a.nd_pcost as biapol,a.nd_sfee as biamat, "
                + "((nd_sfee+nd_pcost)+a.premi_total) as premi_bruto,a.nd_feebase1 as feebase,a.nd_ppn as ppn,a.nd_disc1 as diskon,a.due_date as tgl_jtempo,a.receipt_no as nobuk,a.receipt_date as tglbayar, "
                + "((a.nd_comm1+nd_comm2)-(a.nd_taxcomm1+nd_taxcomm2)) as komisi,(a.nd_taxcomm1+nd_taxcomm2) as tax_komisi, (nd_brok1-nd_taxbrok1) as bfee,nd_taxbrok1 as tax_bfee,"
                + "(nd_hfee-nd_taxhfee) as hfee,nd_taxhfee as tax_hfee, os_hfee,os_bfee,os_fbase,os_comm,os_ppn_bfee,os_ppn_fbase from (  " + sqa.getSQL()
                + " group by a.ar_invoice_id,b.policy_date,b.pol_id,b.pol_no,a.cc_code,d.ent_name,b.premi_total, b.premi_netto,b.nd_pcost,b.nd_sfee,b.nd_disc1,b.nd_disc2,a.attr_pol_no,"
                + "b.nd_feebase1,b.nd_feebase2,a.attr_pol_no, a.attr_pol_name,a.mutation_date,b.nd_taxcomm1,b.nd_taxcomm2,b.nd_taxcomm3,b.nd_taxcomm4,b.nd_comm1,a.due_date,a.receipt_no,a.receipt_date, "
                + "b.nd_comm2,b.nd_comm3,b.nd_comm4,b.nd_brok1,b.nd_brok2,b.nd_hfee,b.nd_taxbrok1,b.nd_taxbrok2,b.nd_taxhfee,d.ref2 ) a ) a order by a.nopol ";

        SQLUtil S = new SQLUtil();

        String nama_file = "oscomm_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCEL_RINCIAN_HUTANGPIUTANG_REINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String sql1 = "select 'Outward'::text as kategori,c.closing_type||' '||coalesce(c.treaty_type,'') as closing_type,a.invoice_no as invoice,a.ar_invoice_id,"
                + "a.cc_code as koda,c.no_surat_hutang,a.mutation_date as tglpolis,''::text as reff_inward,a.ar_cust_id,b.short_name as ceding,"
                + "coalesce(a.refa1,a.refid2) as slip,a.attr_pol_no as polis,a.attr_pol_name as tertanggung,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Premi R/A' and d.ar_invoice_id = a.ar_invoice_id),0) as premi_ri,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Komisi R/A' and d.ar_invoice_id = a.ar_invoice_id),0) as komisi_ri,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Klaim R/A' and d.ar_invoice_id = a.ar_invoice_id),0) as klaim_ri,"
                + "0::numeric as bfee_ri,a.amount as premi_nett,a.refdate1 as tgl_binding,a.receipt_no as nobukti,a.receipt_date as tglbayar ";

        sql1 = sql1 + " from ar_invoice a "
                + "inner join ent_master b on b.ent_id = a.ar_cust_id "
                + "inner join ins_gl_closing c on c.no_surat_hutang = a.no_surat_hutang ";

        sql1 = sql1 + " where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.amount <> 0";
        sql1 = sql1 + " and a.invoice_type in ('AR','AP') ";
        sql1 = sql1 + " and a.ar_trx_type_id in (13,14,22)";

        if (policyDateFrom != null) {
            sql1 = sql1 + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sql1 = sql1 + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
//            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sql1 = sql1 + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
//            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
//            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sql1 = sql1 + " and a.cc_code = '" + stBranch + "'";
//            sqa.addClause("a.cc_code = ?");
//            sqa.addPar(stBranch);
        }

        if (stEntityID != null) {
            sql1 = sql1 + " and b.ent_id = '" + stEntityID + "'";
//            sqa.addClause("b.ent_id = ?");
//            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeID != null) {
            sql1 = sql1 + " and a.attr_pol_type_id = '" + stPolicyTypeID + "'";
//            sqa.addClause("a.attr_pol_type_id = ?");
//            sqa.addPar(stPolicyTypeID);
        }

        /*if (stFltTreatyType != null) {
        sqa.addClause("a.no_surat_hutang like ?");
        sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
        sqa.addClause("a.no_surat_hutang = ?");
        sqa.addPar(stKreasiName);
        }

        if (stReinsName != null) {
        sqa.addClause("a.no_surat_hutang = ?");
        sqa.addPar(stReinsName);
        }

        if (stCreateID != null) {
        sqa.addClause("b.create_who = ?");
        sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
        sqa.addClause("d.ref2 = ?");
        sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
        sqa.addClause("e.category1 = ?");
        sqa.addPar(stCustCategory1);
        }*/

        String sqlOutward = "select kategori,closing_type,invoice,koda,no_surat_hutang,tglpolis,reff_inward,ar_cust_id,ceding,slip,polis,tertanggung,"
                + "sum(premi_ri) as premi_ri,sum(komisi_ri) as komisi_ri,sum(klaim_ri) as klaim_ri,sum(bfee_ri) as bfee_ri,sum(premi_nett) as premi_nett,"
                + "tgl_binding,nobukti,tglbayar from ( " + sql1 + " ) a group by kategori,closing_type,invoice,koda,no_surat_hutang,"
                + "tglpolis,reff_inward,ar_cust_id,ceding,slip,polis,tertanggung,tgl_binding,nobukti,tglbayar ";


        String sql2 = "select 'Inward'::text as kategori,c.description as closing_type,a.invoice_no as invoice,a.ar_invoice_id,a.cc_code as koda,a.no_surat_hutang,a.mutation_date as tglpolis,"
                + "a.reference_no as reff_inward,a.ent_id,b.short_name as ceding,a.refa1 as slip,a.attr_pol_no as polis,a.attr_pol_name as tertanggung,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Premi Bruto' and d.ar_invoice_id = a.ar_invoice_id),0) as premi_ri,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Komisi' and d.ar_invoice_id = a.ar_invoice_id),0) as komisi_ri,0::numeric as klaim_ri,"
                + "coalesce((select sum(d.amount) from ar_invoice_details d where d.description = 'Brokerage Fee' and d.ar_invoice_id = a.ar_invoice_id),0) as bfee_ri,"
                + "a.amount as premi_nett,a.refdate1 as tgl_binding,a.receipt_no as nobukti,a.receipt_date as tglbayar ";

        sql2 = sql2 + " from ar_invoice a "
                + "inner join ent_master b on b.ent_id = a.ent_id "
                + "inner join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id ";

        sql2 = sql2 + "where coalesce(a.cancel_flag,'') <> 'Y' and a.amount <> 0 ";
        sql2 = sql2 + " and a.invoice_type in ('AR','AP')";
        sql2 = sql2 + " and (a.ar_trx_type_id in (1,2,3,20,21) or (a.claim_status = 'DLA' and a.ar_trx_type_id in (23,25)))";

        if (policyDateFrom != null) {
            sql2 = sql2 + " and date_trunc('day',a.mutation_date) >= '" + policyDateFrom + "'";
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sql2 = sql2 + " and date_trunc('day',a.mutation_date) <= '" + policyDateTo + "'";
//            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
//            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sql2 = sql2 + " and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + perDateFrom + "')";
//            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
//            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sql2 = sql2 + " and a.cc_code = '" + stBranch + "'";
//            sqa.addClause("a.cc_code = ?");
//            sqa.addPar(stBranch);
        }

        if (stEntityID != null) {
            sql2 = sql2 + " and b.ent_id = '" + stEntityID + "'";
//            sqa.addClause("b.ent_id = ?");
//            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeID != null) {
            sql2 = sql2 + " and a.attr_pol_type_id = '" + stPolicyTypeID + "'";
//            sqa.addClause("a.attr_pol_type_id = ?");
//            sqa.addPar(stPolicyTypeID);
        }

        /*if (stFltTreatyType != null) {
        sqaInward.addClause("a.no_surat_hutang like ?");
        sqaInward.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
        sqaInward.addClause("a.no_surat_hutang = ?");
        sqaInward.addPar(stKreasiName);
        }

        if (stReinsName != null) {
        sqaInward.addClause("a.no_surat_hutang = ?");
        sqaInward.addPar(stReinsName);
        }

        if (stCreateID != null) {
        sqaInward.addClause("b.create_who = ?");
        sqaInward.addPar(stCreateID);
        }

        if (stCompanyID != null) {
        sqaInward.addClause("d.ref2 = ?");
        sqaInward.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
        sqaInward.addClause("e.category1 = ?");
        sqaInward.addPar(stCustCategory1);
        }*/

        String sqlInward = "select kategori,closing_type,invoice,koda,no_surat_hutang,tglpolis,reff_inward,ent_id,ceding,slip,polis,tertanggung,"
                + "sum(premi_ri) as premi_ri,sum(komisi_ri) as komisi_ri,sum(klaim_ri) as klaim_ri,sum(bfee_ri) as bfee_ri,sum(premi_nett) as premi_nett,"
                + "tgl_binding,nobukti,tglbayar from ( " + sql2 + " ) a group by kategori,closing_type,invoice,koda,no_surat_hutang,"
                + "tglpolis,reff_inward,ent_id,ceding,slip,polis,tertanggung,tgl_binding,nobukti,tglbayar ";

        String sql = "select * from ( " + sqlOutward + " union all " + sqlInward + " ) a order by a.kategori desc,a.invoice asc ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RINCIAN_HUTANGPIUTANG_REINS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("kategori");
            row0.createCell(1).setCellValue("closing_type");
            row0.createCell(2).setCellValue("invoice");
            row0.createCell(3).setCellValue("koda");
            row0.createCell(4).setCellValue("no_surat_hutang");
            row0.createCell(5).setCellValue("tglpolis");
            row0.createCell(6).setCellValue("reff_inward");
            row0.createCell(7).setCellValue("ar_cust_id");
            row0.createCell(8).setCellValue("ceding");
            row0.createCell(9).setCellValue("slip");
            row0.createCell(10).setCellValue("polis");
            row0.createCell(11).setCellValue("tertanggung");
            row0.createCell(12).setCellValue("premi_ri");
            row0.createCell(13).setCellValue("komisi_ri");
            row0.createCell(14).setCellValue("klaim_ri");
            row0.createCell(15).setCellValue("bfee_ri");
            row0.createCell(16).setCellValue("premi_nett");
            row0.createCell(17).setCellValue("tgl_binding");
            row0.createCell(18).setCellValue("nobukti");
            row0.createCell(19).setCellValue("tglbayar");

            String tertanggung = null;
            if (h.getFieldValueByFieldNameST("tertanggung") != null) {
                tertanggung = h.getFieldValueByFieldNameST("tertanggung").replace("/", "");
                tertanggung = tertanggung.replace(";", "");
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("kategori"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("closing_type"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("invoice"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("no_surat_hutang"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tglpolis"));
            if (h.getFieldValueByFieldNameST("reff_inward") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("reff_inward"));
            }
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("ar_cust_id").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ceding"));
            if (h.getFieldValueByFieldNameST("slip") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("slip"));
            }
            if (h.getFieldValueByFieldNameST("polis") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("polis"));
            }
            if (h.getFieldValueByFieldNameST("tertanggung") != null) {
                row.createCell(11).setCellValue(tertanggung);
            }
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("premi_ri").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("komisi_ri").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("klaim_ri").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("bfee_ri").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("premi_nett").doubleValue());
            if (h.getFieldValueByFieldNameDT("tgl_binding") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameDT("tgl_binding"));
            }
            if (h.getFieldValueByFieldNameST("nobukti") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameST("nobukti"));
            }
            if (h.getFieldValueByFieldNameDT("tglbayar") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("tglbayar"));
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_REKAP_HUTANGPIUTANG_REINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.attr_pol_id,a.ar_invoice_id,a.ar_cust_id as reasid,c.short_name as ceding,a.ccy as kurs,a.ccy_rate as rate,a.invoice_type as tipe,a.mutation_date,a.amount,a.amount_settled,"
                + "sum(case when b.negative_flag = 'Y' then b.amount*-1 else b.amount end) as amount_kurs,sum(case when b.negative_flag = 'Y' then b.entered_amount*-1 else b.entered_amount end) as amount_ori ");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id "
                + "inner join ent_master c on c.ent_id = a.ar_cust_id ");

        sqa.addClause("a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' and a.amount <> 0 ");
        sqa.addClause("a.invoice_type in ('AR','AP')");
        sqa.addClause("a.ar_trx_type_id in (13,14)");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (perDateFrom != null) {
            sqa.addClause("(a.receipt_date is null or date_trunc('day',a.receipt_date) > ?)");
            sqa.addPar(perDateFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stEntityID != null) {
            sqa.addClause("c.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        /*if (stFltTreatyType != null) {
        sqa.addClause("a.no_surat_hutang like ?");
        sqa.addPar('%' + stFltTreatyType + '%');
        }

        if (stKreasiName != null) {
        sqa.addClause("a.no_surat_hutang = ?");
        sqa.addPar(stKreasiName);
        }

        if (stReinsName != null) {
        sqa.addClause("a.no_surat_hutang = ?");
        sqa.addPar(stReinsName);
        }

        if (stCreateID != null) {
        sqa.addClause("b.create_who = ?");
        sqa.addPar(stCreateID);
        }

        if (stCompanyID != null) {
        sqa.addClause("d.ref2 = ?");
        sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
        sqa.addClause("e.category1 = ?");
        sqa.addPar(stCustCategory1);
        }*/

        String sql = "select a.reasid,a.ceding,"
                + "sum(getpremi2(a.tipe = 'AP' and a.kurs = 'IDR', a.amount)) as hutang_idr,sum(getpremi2(a.tipe = 'AP' and a.kurs = 'USD', a.amount)) as hutang_usd,"
                + "sum(getpremi2(a.tipe = 'AP' and a.kurs = 'SGD', a.amount)) as hutang_sgd,sum(getpremi2(a.tipe = 'AP' and a.kurs = 'EUR', a.amount)) as hutang_eur,"
                + "sum(getpremi2(a.tipe = 'AP' and a.kurs not in ('IDR','USD','SGD','EUR'), a.amount)) as hutang_oth,sum(getpremi2(a.tipe = 'AR' and a.kurs = 'IDR', a.amount)) as piutang_idr,"
                + "sum(getpremi2(a.tipe = 'AR' and a.kurs = 'USD', a.amount)) as piutang_usd,sum(getpremi2(a.tipe = 'AR' and a.kurs = 'SGD', a.amount)) as piutang_sgd,"
                + "sum(getpremi2(a.tipe = 'AR' and a.kurs = 'EUR', a.amount)) as piutang_eur,sum(getpremi2(a.tipe = 'AR' and a.kurs not in ('IDR','USD','SGD','EUR'), a.amount)) as piutang_oth "
                + "from ( " + sqa.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10 order by a.attr_pol_id,a.ar_invoice_id ) a group by 1,2 order by 1,2 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_REKAP_HUTANGPIUTANG_REINS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("reasid");
            row0.createCell(1).setCellValue("ceding");
            row0.createCell(2).setCellValue("hutang_idr");
            row0.createCell(3).setCellValue("hutang_usd");
            row0.createCell(4).setCellValue("hutang_sgd");
            row0.createCell(5).setCellValue("hutang_eur");
            row0.createCell(6).setCellValue("hutang_oth");
            row0.createCell(7).setCellValue("piutang_idr");
            row0.createCell(8).setCellValue("piutang_usd");
            row0.createCell(9).setCellValue("piutang_sgd");
            row0.createCell(10).setCellValue("piutang_eur");
            row0.createCell(11).setCellValue("piutang_oth");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("reasid").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ceding"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("hutang_idr").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("hutang_usd").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("hutang_sgd").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("hutang_eur").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("hutang_oth").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("piutang_idr").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("piutang_usd").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("piutang_sgd").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("piutang_eur").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("piutang_oth").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }
}

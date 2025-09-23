/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionClaimReportForm
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
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.*;
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
import com.crux.util.JSPUtil;

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
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.postalcode.model.PostalCodeView;
import com.webfin.system.region.model.RegionView;
import java.sql.ResultSet;
import javax.ejb.SessionContext;
import org.apache.poi.hssf.util.CellRangeAddress;

public class ProductionClaimReportForm extends Form {

    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String stReportTitle;
    private String stReport;
    private String stReportDesc;
    private String stReportType;
    private DTOList list;
    private String stReportTypeOfFile;
    private String stFileName;
    private boolean enableRiskFilter;
    private boolean enableSelectForm = true;
    private String stPrintForm;
    private String stFontSize;
    private String stLang;
    public SessionContext ctx;
    private Date expirePeriodFrom;
    private Date expirePeriodTo;
    private Date periodFrom;
    private Date periodTo;
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
    private String stPolicyTypeGroupID;
    private String stPolicyTypeGroupDesc;
    private String stPolicyTypeID;
    private String stPolicyTypeDesc;
    private String stInsCompanyID;
    private String stInsCompanyName;
    private String stFltClaimStatus;
    private String stFltClaimStatusDesc;
    private String stEntityID;
    private String stEntityName;
    private String stPolicyNo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stMarketerID;
    private String stMarketerName;
    private String stCompanyID;
    private String stCompanyName;
    private String stCoinsurerID;
    private String stCoinsurerName;
    private String stStatus;
    private String stItemClaimID;
    private String stItemClaimName;
    private String stFltTreatyType;
    private String stFltTreatyTypeDesc;
    private String stZoneCode;
    private String stFltCoverType;
    private String stObject;
    private String stRiskCardNo;
    private String stCustCategory1;
    private String stRiskLocation;
    private String stPostCode;
    private String stRiskCode;
    private String stNoUrut;
    private String stAuthorized;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stIndex;
    private String stGroupID;
    private String stGroupName;
    private String stDlaNo;
    private String stTriwulan;
    private String stNonCreditID;
    private String stCompanyProdID;
    private String stCompanyProdName;
    private String stPolCredit;
    private String stRISlip;
    private String stCompTypeID;
    private String stCompTypeName;
    private String stMarketerOffID;
    private String stMarketerOffName;
    private String usiaFrom;
    private String usiaTo;
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;
    private String stBranchSource;
    private String stBranchSourceDesc;
    private String stRegionSource;
    private String stRegionSourceDesc;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionClaimReportForm.class);

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

    public DTOList KLAIM() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code,a.dla_no,a.status,a.pol_no,a.pol_type_id, "
                + " getname(a.ins_policy_type_grp_id=10,a.claim_client_name,f.ref1) as cust_name,d.short_desc as prod_name, "
                + " getpremi(a.cover_type_code = 'DIRECT' or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUT'),(a.claim_amount*a.ccy_rate),getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id = 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id = 1),(a.claim_amount*a.ccy_rate))) as claim_amount, "
                + " getpremi(a.cover_type_code = 'DIRECT' or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUT'),(b.claim_amt*a.ccy_rate),getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id = 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id = 1),(a.claim_amount*a.ccy_rate))) as claim_amount_endorse, "
                + " getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER' and b.entity_id <> 1) or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id <> 1),(b.claim_amt*a.ccy_rate)) as nd_taxcomm1, "
                + " a.dla_date,checkstatus(b.entity_id = 1,'OR',e.short_name) as coinsurer_name ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ins_pol_obj f on f.ins_pol_obj_id = a.claim_object_id "
                + " left join gl_cost_center c on c.cc_code = a.cc_code "
                + " inner join ins_policy_types d on d.pol_type_id = a.pol_type_id "
                + " left join ent_master e on e.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = a.entity_id ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("b.claim_amt <> 0");
        sqa.addClause("((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS'))");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (stFltClaimStatus != null) {
            sqa.addClause("a.claim_status = ?");
            sqa.addPar(stFltClaimStatus);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (status == null) {
            sqa.addClause("a.status in (?,?,?)");
            sqa.addPar(FinCodec.PolicyStatus.ENDORSE);
            sqa.addPar(FinCodec.PolicyStatus.POLICY);
            sqa.addPar(FinCodec.PolicyStatus.RENEWAL);
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
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

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = sqa.getSQL() + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no,b.coins_type,b.entity_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        return l;
    }

    public DTOList KLAIM_KO() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.claim_date,a.dla_no,a.pol_no,substr(a.cust_name,1,25)||' qq. '||substr(c.ref1,1,15) as cust_name,"
                + "(b.claim_amt*a.ccy_rate) as premi_total");

        sqa.addQuery("   from       ins_policy a "
                + "	inner join ent_master e on e.ent_id = a.entity_id"
                + "	inner join ins_pol_coins b on b.policy_id = a.pol_id and b.entity_id <> 1 "
                + "	inner join ins_pol_obj c on c.ins_pol_obj_id = a.claim_object_id");

        sqa.addClause("a.cover_type_code <> 'DIRECT'");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("a.claim_status = ?");
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

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("b.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + "   order by "
                + "a.dla_no,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList AUTOFAC() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.claim_cause,sum(a.claim_amount*a.ccy_rate) as claim_amount");

        sqa.addQuery("   from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id");

        sqa.addClause(" a.claim_cause in (1576,3732,3734) ");

        sqa.addClause(" a.ins_policy_type_grp_id = 9 ");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("a.claim_status = ?");
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

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("b.refd2 >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("b.refd2 <= ?");
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

        final String sql = sqa.getSQL() + " group by a.claim_cause";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList AUTOFAC_DETIL() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.claim_cause,a.dla_no,a.pol_no,b.ref1 as cust_name,(a.claim_amount*a.ccy_rate) as claim_cust_amount");

        sqa.addQuery("   from ins_policy a "
                + " inner join ins_pol_obj b on b.pol_id = a.pol_id");

        sqa.addClause(" a.claim_cause in (1576,3732,3734) ");

        sqa.addClause(" a.ins_policy_type_grp_id = 9 ");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("a.claim_status = ?");
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

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("b.refd2 >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("b.refd2 <= ?");
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

        final String sql = sqa.getSQL() + " order by a.cc_code,a.dla_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList KLAIM_KREASI() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	count(dla_no) as jumlah,b.claim_cause,b.claim_amount,dla_no ");

        sqa.addQuery(" from ins_policy b ");

        sqa.addClause("b.effective_flag='Y'");
        sqa.addClause("b.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("b.claim_status = 'DLA'");
        sqa.addClause("b.pol_type_id = 21");

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


        final String sql = "select lvl,ref4 as ref1,ref5 as ref2, "
                + " sum(getkoas(claim_cause=1575,jumlah)) as nd_comm1, "
                + " sum(getkoas(claim_cause=1575,claim_amount)) as premi_base, "
                + " sum(getkoas(claim_cause=1576,jumlah)) as nd_comm2, "
                + " sum(getkoas(claim_cause=1576,claim_amount)) as premi_total, "
                + " sum(getkoas(claim_cause=3732,jumlah)) as nd_comm3, "
                + " sum(getkoas(claim_cause=3732,claim_amount)) as premi_total_adisc, "
                + " sum(getkoas(claim_cause=3734,jumlah)) as nd_comm4, "
                + " sum(getkoas(claim_cause=3734,claim_amount)) as total_due, "
                + " sum(getkoas(claim_cause not in (1575,1576,3732,3734),jumlah)) as nd_brok1, "
                + " sum(getkoas(claim_cause not in (1575,1576,3732,3734),claim_amount)) as total_fee "
                + " from band_level a left join ( " + sqa.getSQL() + " group by b.claim_cause,b.claim_amount,dla_no) as b on b.claim_amount between a.ref1 and a.ref2 "
                + " where a.group_desc = 'PAKREASI' "
                + " group by lvl,ref4,ref5 order by lvl,ref4,ref5";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        //javax.swing.JOptionPane.showMessageDialog(null,"Eror= "+l,"tes",javax.swing.JOptionPane.CLOSED_OPTION);

        return l;
    }

    public DTOList EXCEL_KLAIM_POS2() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from claim a ");
        //sqa.addClause("a.pol_no not in ('040310100911000600')");

        if (EFF_CLAIM) {
            sqa.addClause("a.claim_effective_flag = 'Y'");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.approved_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.approved_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (ACT_CLAIM) {
            sqa.addClause("a.claim_effective_flag is null");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'N'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.dla_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.dla_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.refd2) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.refd2) <= ?");
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String receipt = " select b.pol_id,b.receipt_no,b.receipt_date from ar_receipt a "
                + "inner join ar_receipt_lines b on b.receipt_id = a.ar_receipt_id "
                + "where a.posted_flag = 'Y' and b.line_type = 'INVOC' and a.ar_settlement_id = 10 and b.ar_invoice_id is not null ";

        if (stBranch != null) {
            receipt = receipt + " and a.cc_code = ? ";
            sqa.addPar(stBranch);
        }

        if (appDateFrom != null) {
            receipt = receipt + " and date_trunc('day',a.receipt_date) >= ? ";
            sqa.addPar(appDateFrom);
        }

        receipt = receipt + " group by b.pol_id,b.receipt_no,b.receipt_date ";

        String sql = "select a.*,b.receipt_no,b.receipt_date "
                + " from ( " + sqa.getSQL() + " ) a "
                + "left join ( " + receipt + " ) b on b.pol_id::text = a.pol_id "
                + "where a.active_flag='Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no,a.coins_type ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList EXCEL_KLAIM_POS_PLA() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from claim_pla a ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= ?");
            sqa.addPar(PLADateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= ?");
            sqa.addPar(PLADateTo);
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stItemClaimID != null) {
            if (stItemClaimID.equalsIgnoreCase("1")) {
                sqa.addClause("a.item_klaim = 54 ");
            } else {
                sqa.addClause("a.item_klaim = 50 ");
            }
        }

        final String sql = sqa.getSQL();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList RKP() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.cc_code,a.region_id,a.cc_code_source,a.region_id_source,a.dla_date,a.policy_date,a.pol_no,a.pla_no,a.dla_no,a.cust_name,"
                + "(b.insured_amount*a.ccy_rate) as insured_amount,checkstatus(a.ins_policy_type_grp_id=10,b.ref2,b.ref1) as prod_name,"
                + "(a.claim_amount_est*a.ccy_rate) as claim_amount_est,(a.claim_amount_approved*a.ccy_rate_claim) as claim_amount_approved");

        sqa.addQuery(" from ins_policy a "
                + " inner join ent_master e on e.ent_id = a.entity_id"
                + " inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id");

        sqa.addClause("a.status IN ('CLAIM','CLAIM ENDORSE')");

        if (EFF_CLAIM) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (ACT_CLAIM) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }
        /*NON AKS*/
//        if (stBranch != null) {
//            sqa.addClause("a.cc_code = ?");
//            sqa.addPar(stBranch);
//        }
//
//        if (stRegion != null) {
//            sqa.addClause("a.region_id = ?");
//            sqa.addPar(stRegion);
//        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCoinsurerID != null) {
            sqa.addClause("b.ref8 = ?");
            sqa.addPar(stCoinsurerID);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like ?");
            sqa.addPar('%' + stPolicyNo + '%');
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("e.ref1 in " + type);
//            sqa.addPar(type);
        }

        if (stMarketerOffID != null) {
            //sqa.addClause("a.marketing_officer_who = '" + stMarketerOffID + "'");
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stObject != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("10")) {
                sqa.addClause("upper(b.ref2) like ?");
            } else {
                sqa.addClause("upper(b.ref1) like ?");
            }
            sqa.addPar('%' + stObject.toUpperCase() + '%');
        }

        if (stBussinessPolType != null) {
            if (stBussinessPolType.equalsIgnoreCase("1")) {
                if (stBranch != null) {
                    sqa.addClause("a.cc_code = '80' and a.cc_code_source = ?");
                    sqa.addPar(stBranch);
                }

                if (stRegion != null) {
                    sqa.addClause("a.region_id_source = ?");
                    sqa.addPar(stRegion);
                }
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                if (stBranch != null) {
                    sqa.addClause("a.cc_code = ?");
                    sqa.addPar(stBranch);
                }

                if (stRegion != null) {
                    sqa.addClause("a.region_id = ?");
                    sqa.addPar(stRegion);
                }
            }
        } else {
            if (stBranch != null) {
                sqa.addClause("((a.cc_code = ?) or (a.cc_code = '80' and a.cc_code_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stBranch);
            }
            if (stRegion != null) {
                sqa.addClause("((a.cc_code = ? and a.region_id = ?) or (a.cc_code = '80' and a.cc_code_source = ? and a.region_id_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stRegion);
                sqa.addPar(stBranch);
                sqa.addPar(stRegion);
            }
        }

        final String sql = sqa.getSQL() + "   order by "
                + "a.pla_date,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList RKS() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.cc_code,a.region_id,a.cc_code_source,a.region_id_source,a.pla_date,a.policy_date,a.pol_no,a.pla_no,a.cust_name,(b.insured_amount*a.ccy_rate) as insured_amount,"
                + "checkstatus(a.ins_policy_type_grp_id=10,b.ref2,b.ref1) as prod_name,(a.claim_amount_est*a.ccy_rate_claim) as claim_amount_est,(a.claim_amount_approved*a.ccy_rate_claim) as claim_amount_approved");

        sqa.addQuery(" from ins_policy a "
                + " inner join ent_master e on e.ent_id = a.entity_id"
                + " inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id");

        sqa.addClause("a.pol_id not in ( "
                + "select a.parent_id "
                + "from ins_policy a "
                + "where a.claim_status = 'DLA' and a.active_flag = 'Y' "
                + "and (a.effective_flag = 'N' or a.effective_flag is null) "
                + "and a.status in ('CLAIM','CLAIM ENDORSE')) ");

        sqa.addClause("a.status IN ('CLAIM')");
        sqa.addClause("a.claim_status = 'PLA'");
        sqa.addClause("a.active_flag = 'Y'");

        if (EFF_CLAIM) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (ACT_CLAIM) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= ?");
            sqa.addPar(PLADateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= ?");
            sqa.addPar(PLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
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

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("e.ref1 in " + type);
//            sqa.addPar(type);
        }

        if (stMarketerOffID != null) {
            //sqa.addClause("a.marketing_officer_who = '" + stMarketerOffID + "'");
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stObject != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("10")) {
                sqa.addClause("upper(b.ref2) like ?");
            } else {
                sqa.addClause("upper(b.ref1) like ?");
            }
            sqa.addPar('%' + stObject.toUpperCase() + '%');
        }

        final String sql = sqa.getSQL() + "   order by "
                + "a.pla_date,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

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

            if (getStFltTreatyType() != null) {
                sqa.addClause("i.treaty_type = ?");
                sqa.addPar(getStFltTreatyType());
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

        if (getStFltCoverType() != null) {
            sqa.addClause("a.cover_type_code = ?");
            sqa.addPar(getStFltCoverType());
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

        if (getStZoneCode() != null) {
            sqaobj.addQuery(" inner join ins_pol_cover b on b.ins_pol_obj_id = a.ins_pol_obj_id");
            sqaobj.addClause("b.zone_id like ?");
            sqaobj.addPar('%' + getStZoneCode() + '%');
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

    public DTOList EXCEL_KLAIM_POS() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	c.description,a.ins_policy_type_grp_id,a.dla_no,a.status,a.pol_id,a.pol_no,a.policy_date, "
                + " getname(a.ins_policy_type_grp_id=10,a.claim_client_name,f.ref1) as cust_name,d.short_desc,f.insured_amount, "
                + " getpremi(a.cover_type_code = 'DIRECT' or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUT'),(a.claim_amount*a.ccy_rate_claim),getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id = 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id = 1),(a.claim_amount*a.ccy_rate_claim))) as claim_amount, "
                + " getpremi(a.cover_type_code = 'DIRECT' or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUT'),(b.claim_amt*a.ccy_rate_claim),getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id = 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id = 1),(a.claim_amount*a.ccy_rate_claim))) as hutang, "
                + " getbordero3((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER' and b.entity_id <> 1) or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS' and b.entity_id <> 1),(b.claim_amt*a.ccy_rate_claim)) as piutang, "
                + " f.refd1,f.refd2,f.refd3,a.claim_date,a.dla_date,a.claim_approved_date,checkstatus(b.entity_id = 1,'OR',e.short_name) as short_name,a.claim_cause_desc, "
                + " (select g.claim_no "
                + " from ar_invoice g "
                + " where g.attr_pol_id = a.pol_id and "
                + " ((INVOICE_NO LIKE 'L%' AND CLAIM_NO IS NOT NULL) or (INVOICE_NO like 'K%' AND CLAIM_NO IS NOT NULL)) "
                + " group by g.claim_no) as claim_no ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ins_pol_obj f on f.ins_pol_obj_id = a.claim_object_id "
                + " left join gl_cost_center c on c.cc_code = a.cc_code "
                + " inner join ins_policy_types d on d.pol_type_id = a.pol_type_id "
                + " left join ent_master e on e.ent_id = b.entity_id "
                + " left join ent_master g on g.ent_id = a.entity_id ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("b.claim_amt <> 0");
        sqa.addClause("((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS'))");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (stFltClaimStatus != null) {
            sqa.addClause("a.claim_status = ?");
            sqa.addPar(stFltClaimStatus);
        }

        boolean displayEffective = true;

        if (isClaim && stFltClaimStatus != null) {
            displayEffective = false;
        }

        if (displayEffective) {
            sqa.addClause("a.effective_flag='Y'");
        }

        if (status == null) {
            sqa.addClause("a.status in (?,?,?)");
            sqa.addPar(FinCodec.PolicyStatus.ENDORSE);
            sqa.addPar(FinCodec.PolicyStatus.POLICY);
            sqa.addPar(FinCodec.PolicyStatus.RENEWAL);
        }

        if (isClaim) {
            sqa.addClause("a.status in(?,?)");
            sqa.addPar(FinCodec.PolicyStatus.CLAIM);
            sqa.addPar(FinCodec.PolicyStatus.ENDORSECLAIM);

        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
        }

        if (stCompanyID != null) {
            sqa.addClause("g.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = sqa.getSQL() + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no,b.coins_type,b.entity_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public DTOList KLAIM2() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from claim a ");

        if (EFF_CLAIM) {
            sqa.addClause("a.claim_effective_flag = 'Y'");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.approved_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.approved_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (ACT_CLAIM) {
            sqa.addClause("a.claim_effective_flag is null");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'N'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.dla_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.dla_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.refd2) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.refd2) <= ?");
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

        if (stDlaNo != null) {
            sqa.addClause("(a.pla_no like ? or a.dla_no like ? )");
            sqa.addPar('%' + stDlaNo + '%');
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String sql = "select * from ( " + sqa.getSQL() + " ) a where a.active_flag='Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no,a.coins_type ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        return l;

    }

    public void EXPORT_KLAIM_POS() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal total = new BigDecimal(0);
        BigDecimal itemklaim = new BigDecimal(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Daerah");
            row0.createCell(1).setCellValue("No. LKS");
            row0.createCell(2).setCellValue("No. LKP");
            row0.createCell(3).setCellValue("No. Polis");
            row0.createCell(4).setCellValue("Nama Bank");
            row0.createCell(5).setCellValue("Nama Pemasar");
            row0.createCell(6).setCellValue("Nama Tertanggung");
            row0.createCell(7).setCellValue("K.O.B");
            row0.createCell(8).setCellValue("Curr");
            row0.createCell(9).setCellValue("Kurs");
            row0.createCell(10).setCellValue("Insured Amount");
            row0.createCell(11).setCellValue("Claim Amount");
            row0.createCell(12).setCellValue("Koas Dibayar");
            row0.createCell(13).setCellValue("Koas Diterima");
            row0.createCell(14).setCellValue("Hutang Klaim");
            row0.createCell(15).setCellValue("Piutang Klaim");
            row0.createCell(16).setCellValue("Tanggal Klaim");
            row0.createCell(17).setCellValue("Tanggal LKP");
            row0.createCell(18).setCellValue("Tanggal Setujui");
            row0.createCell(19).setCellValue("Tanggal Polis");
            row0.createCell(20).setCellValue("Penyebab Klaim");
            row0.createCell(21).setCellValue("Keterangan");
            row0.createCell(22).setCellValue("No Klaim");
            row0.createCell(23).setCellValue("Pol ID");
            row0.createCell(24).setCellValue("Tanggal Lahir");
            row0.createCell(25).setCellValue("Tanggal Awal");
            row0.createCell(26).setCellValue("Tanggal Akhir");
            row0.createCell(27).setCellValue("Jenis Kredit");
            row0.createCell(28).setCellValue("Claim Amount (Akuntansi)");
            row0.createCell(29).setCellValue("Hutang Klaim (Akuntansi)");
            row0.createCell(30).setCellValue("Subrogation");
            row0.createCell(31).setCellValue("Fee Recovery");
            row0.createCell(32).setCellValue("Adjuster Fee");
            row0.createCell(33).setCellValue("Biaya Derek");
            row0.createCell(34).setCellValue("Salvage");
            row0.createCell(35).setCellValue("Ex Gratia Klaim");
            row0.createCell(36).setCellValue("Bengkel");
            row0.createCell(37).setCellValue("Pajak");
            row0.createCell(38).setCellValue("PPn");
            row0.createCell(39).setCellValue("No Bukti");
            row0.createCell(40).setCellValue("Tgl Bayar");

            //row0.createCell(24).setCellValue("Item Klaim");
            //row0.createCell(25).setCellValue("Item Klaim Amount");
            //row0.createCell(25).setCellValue("Nilai Item Klaim");
            //row0.createCell(24).setCellValue("LKS Amount");

//            String endorse = h.getFieldValueByFieldNameST("status");
//            if (endorse.equalsIgnoreCase("CLAIM ENDORSE")) {
//                claim = h.getFieldValueByFieldNameBD("endorse");
//            } else {
//                claim = h.getFieldValueByFieldNameBD("claim_amount");
//            }

            itemklaim = BDUtil.sub(h.getFieldValueByFieldNameBD("claim_amount"), h.getFieldValueByFieldNameBD("subrogation"));
            itemklaim = BDUtil.sub(itemklaim, h.getFieldValueByFieldNameBD("afee"));
            itemklaim = BDUtil.sub(itemklaim, h.getFieldValueByFieldNameBD("tcost"));
            itemklaim = BDUtil.sub(itemklaim, h.getFieldValueByFieldNameBD("salvage"));
            itemklaim = BDUtil.sub(itemklaim, h.getFieldValueByFieldNameBD("egklaim"));
            itemklaim = BDUtil.add(itemklaim, h.getFieldValueByFieldNameBD("pajak"));
            itemklaim = BDUtil.add(itemklaim, h.getFieldValueByFieldNameBD("ppn"));
            itemklaim = BDUtil.add(itemklaim, h.getFieldValueByFieldNameBD("feerecov"));

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("customer_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("marketer_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("claim_currency"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            row.createCell(12).setCellValue(total.doubleValue());
            if (h.getFieldValueByFieldNameBD("premi_base") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_endorse") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_endorse").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_base") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("premi_base").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("claim_date") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            }
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                row.createCell(20).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
            }
            if (h.getFieldValueByFieldNameST("coinsurer_name") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("coinsurer_name"));
            }
            if (h.getFieldValueByFieldNameST("claim_no") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("claim_no"));
            }
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
            } else {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameST("umur"));
            }
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
            if (h.getFieldValueByFieldNameST("kreasi_type_desc") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameST("kreasi_type_desc"));
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(28).setCellValue(itemklaim.doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("subrogation") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("subrogation").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("feerecov") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("feerecov").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("afee") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("afee").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tcost") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("tcost").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("salvage") != null) {
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("egklaim") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("egklaim").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("bengkel") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("bengkel").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("pajak") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("pajak").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ppn") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            }
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }

            //row.createCell(24).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getDbClaimAmount().doubleValue());
            //if (h.getFieldValueByFieldNameST("item_klaim")!=null)
            //    row.createCell(24).setCellValue(LanguageManager.getInstance().translate(getInsItem(h.getFieldValueByFieldNameST("item_klaim")).getStDescription()));
            //if (h.getFieldValueByFieldNameBD("item_klaim_amount")!=null)
            //    row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("item_klaim_amount").doubleValue());
            //if (h.getFieldValueByFieldNameBD("item_klaim_amount")!=null)
            //row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("item_klaim_amount").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void EXPORT_KLAIM_POS_PLA() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal total = new BigDecimal(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Daerah");
            row0.createCell(1).setCellValue("No. LKS");
            row0.createCell(2).setCellValue("No. Polis");
            row0.createCell(3).setCellValue("Nama Tertanggung");
            row0.createCell(4).setCellValue("K.O.B");
            row0.createCell(5).setCellValue("Insured Amount");
            row0.createCell(6).setCellValue("Claim Amount");
            row0.createCell(7).setCellValue("Koas Dibayar");
            row0.createCell(8).setCellValue("Koas Diterima");
            row0.createCell(9).setCellValue("Hutang Klaim");
            row0.createCell(10).setCellValue("Piutang Klaim");
            row0.createCell(11).setCellValue("Tanggal Klaim");
            row0.createCell(12).setCellValue("Tanggal LKS");
            row0.createCell(13).setCellValue("Tanggal Setujui");
            row0.createCell(14).setCellValue("Tanggal Polis");
            row0.createCell(15).setCellValue("Penyebab Klaim");
            row0.createCell(16).setCellValue("Keterangan");
            row0.createCell(17).setCellValue("Pol ID");
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row0.createCell(18).setCellValue("Tanggal Lahir");
            }
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row0.createCell(19).setCellValue("Tanggal Awal");
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row0.createCell(20).setCellValue("Tanggal Akhir");
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            row.createCell(7).setCellValue(total.doubleValue());
            if (h.getFieldValueByFieldNameBD("nd_taxcomm1") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("nd_taxcomm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_endorse") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_endorse").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_taxcomm1") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nd_taxcomm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("claim_date") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            }
            if (h.getFieldValueByFieldNameDT("pla_date") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameDT("pla_date"));
            }
            if (h.getFieldValueByFieldNameDT("approved_date") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause_desc") != null) {
                row.createCell(15).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("claim_cause_desc")));
            }
            if (h.getFieldValueByFieldNameST("coinsurer_name") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("coinsurer_name"));
            }
            if (h.getFieldValueByFieldNameBD("pol_id") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("refd1") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("refd1"));
            }
            if (h.getFieldValueByFieldNameDT("refd2") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameDT("refd2"));
            }
            if (h.getFieldValueByFieldNameDT("refd3") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameDT("refd3"));
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public String getStObject() {
        return stObject;
    }

    public void setStObject(String stObject) {
        this.stObject = stObject;
    }

    public DTOList EXCEL_KLAIM_AAUI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	e.ins_policy_type_grp_id,e.group_name,a.dla_no,count(a.dla_no) as jumlah,a.status, "
                + " getpremi(a.cover_type_code = 'DIRECT' or (a.pol_type_id = '21' and a.cover_type_code = 'COINSOUT'),(a.claim_amount*a.ccy_rate_claim),getbordero3((a.pol_type_id = '21' and b.coins_type = 'COINS_COVER') or (a.pol_type_id = '21' and a.cover_type_code = 'COINSOUTSELF' and b.entity_id = '1') or (a.pol_type_id <> '21' and b.coins_type = 'COINS' and b.entity_id = '1'),(a.claim_amount*a.ccy_rate_claim))) as claim_amount, "
                + " getbordero3((a.pol_type_id = '21' and b.coins_type = 'COINS_COVER' and b.entity_id <> '1') or (a.pol_type_id = '21' and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> '1') or (a.pol_type_id <> '21' and b.coins_type = 'COINS' and b.entity_id <> '1'),(b.claim_amt*a.ccy_rate_claim)) as piutang_klaim ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ins_pol_obj c on c.ins_pol_obj_id = a.claim_object_id "
                + " inner join ins_policy_types d on d.pol_type_id = a.pol_type_id "
                + " inner join ins_policy_type_grp e on e.ins_policy_type_grp_id = d.ins_policy_type_grp_id2 ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("b.claim_amt <> 0");
        sqa.addClause("a.effective_flag='Y'");
        sqa.addClause("a.status in('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("((a.pol_type_id = 21 and b.coins_type = 'COINS_COVER') or (a.pol_type_id = 21 and a.cover_type_code = 'COINSOUTSELF' and b.entity_id <> 1) or (a.pol_type_id <> 21 and b.coins_type = 'COINS'))");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
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

        final String sql = " select group_name, "
                + " sum(checkreas(status='CLAIM',jumlah)) as claim, "
                + " sum(checkreas(status='CLAIM ENDORSE',jumlah)) as claim_endorse, "
                + " sum(claim_amount) as claim_amount, "
                + " sum(piutang_klaim) as piutang_klaim "
                + " from ( " + sqa.getSQL() + " group by e.ins_policy_type_grp_id,e.group_name,a.dla_no,a.status,a.ccy_rate_claim,a.cover_type_code,a.pol_type_id, "
                + " a.claim_amount,b.coins_type,b.entity_id,b.claim_amt ) x "
                + " group by group_name,ins_policy_type_grp_id "
                + " order by ins_policy_type_grp_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_AAUI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("group_name");
            row0.createCell(1).setCellValue("claim");
            row0.createCell(2).setCellValue("claim endorse");
            row0.createCell(3).setCellValue("amount");
            row0.createCell(4).setCellValue("piutang");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("group_name")));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("claim").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("claim_endorse").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("piutang_klaim").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_RKP_REINS() throws Exception {
        //final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        //final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.create_date,coalesce(a.claim_approved_date,a.approved_date) as claim_approved_date,substr(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))::text,1,4) as uy,"
                + "p.treaty_grp_id,e.ent_name as ent_name,f.ent_name as prod_name,a.kreasi_type_desc,b.ref7d,b.ref12,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                //+ "getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,a.period_start) as period_start,getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,a.period_end) as period_end,"
                + "a.ins_policy_type_grp_id::character varying,a.pol_type_id::character varying,a.pol_id::character varying,a.claim_date,a.status,a.claim_cause::text,k.cause_desc,l.short_desc, "
                + "coalesce(a.dla_date,a.pla_date) as dla_date,a.claim_propose_date as propose_date,a.policy_date,a.claim_status,a.pol_no,a.pla_no,a.dla_no,a.cust_name,"
                + "case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end as name,"
                + "coalesce((b.insured_amount*a.ccy_rate_claim),0) as insured_amount,coalesce((a.claim_amount_est*a.ccy_rate_claim),0) as claim_amount_est,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,coalesce((a.claim_amount_approved*a.ccy_rate_claim),0) as claim_amount_approved,"
                + "coalesce(checkreas(a.pol_type_id=21,b.refn2*a.ccy_rate),0) as premiko,a.claim_currency,a.ccy_rate_claim,"
                + "a.claim_client_name,a.claim_account_no,a.entity_id::text,a.prod_id::text,(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "n.user_name as createwho,o.user_name as approvedwho,m.ent_name as marketingofficer, "
                + "d.loss_desc as status_kerugian,a.claim_chronology as kronologi,v.vs_description as kategori,u.description as cabang,l.description as cob,"
                + "checkstatus(a.pol_type_id in (1,2,19,81,83),(select string_agg(y.ins_risk_cat_code,'| ') from ( "
                + "select y.ins_risk_cat_code from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.ins_risk_cat_code ) y ), "
                + "(select string_agg(y.description,'| ') from ( "
                + "select y.description from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.description ) y )) as risk_code, "
                + "(select sum(l.claim_amt) "
                + "from ins_pol_coins l "
                + "where l.policy_id = a.pol_id and l.position_code = 'MEM' group by a.pol_id) as coins_amount,"
                + "(select coalesce(e.short_name,'') from ent_master e where a.coinsurer_id::bigint = e.ent_id) as coins_name,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or,  "
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
                + "(select string_agg(g.receipt_date::text, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as payment_date,"
                + "(select string_agg(g.receipt_no, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as payment_note, "
                + "c.vs_description as jengrup,a.cc_code,r.region_code,a.cc_code_source,s.region_code as region_code_source,q.ent_name as paymentname,t.vs_description as potensi_subro ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                //                + " left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP'"
                + " left join ins_claim_loss d on d.claim_loss_id = b.claim_loss_id "
                + " left join ent_master e on e.ent_id = a.entity_id "
                + " left join ent_master f on f.ent_id = a.prod_id "
                + " left join s_valueset c on c.vs_code = f.ref1 and c.vs_group = 'COMP_TYPE' "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + " left join ins_policy_types l on l.pol_type_id = a.pol_type_id "
                + " left join ent_master m on m.ent_id::text = a.marketing_officer_who "
                + " left join s_users n on n.user_id = a.create_who "
                + " left join s_users o on o.user_id = a.approved_who "
                + " inner join ins_treaty p on p.ins_treaty_id = g.ins_treaty_id "
                + " left join ent_master q on q.ent_id = a.payment_company_id::int "
                + " left join s_region r on r.region_id = a.region_id "
                + " left join s_region s on s.region_id = a.region_id_source "
                + " left join s_valueset t on t.vs_code = a.ref13 and t.vs_group='SUBROGASI' "
                + "inner join gl_cost_center u on u.cc_code = a.cc_code "
                + "left join s_valueset v on v.vs_code = e.category1 and v.vs_group = 'ASK_BUS_SOURCE' ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE') ");

        if (EFF_CLAIM) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (ACT_CLAIM) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and coalesce(a.effective_flag,'') <> 'Y') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {
            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
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
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ?");
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

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("e.ref1 in " + type);
//            sqa.addPar(type);
        }

        if (stMarketerOffID != null) {
            //sqa.addClause("a.marketing_officer_who = '" + stMarketerOffID + "'");
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stCompanyProdID != null) {
//            sqa.addClause("e.ref2 = '" + stCompanyProdID + "'");
            sqa.addClause("f.ref2 = ?");
            sqa.addPar(stCompanyProdID);
        }

        //if (stCoinsurerID != null) {
        //    sqa.addClause("i.member_ent_id = ?");
        //    sqa.addPar(stCoinsurerID);
        //}

        if (stObject != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("10")) {
                sqa.addClause("upper(b.ref2) like ?");
            } else {
                sqa.addClause("upper(b.ref1) like ?");
            }
            sqa.addPar('%' + stObject.toUpperCase() + '%');
        }

        final String sql = sqa.getSQL() + "  group by a.status,a.claim_status,a.coinsurer_id,a.period_start,a.period_end,a.pol_type_id,a.pol_id,a.pla_date,a.dla_date,"
                + "a.claim_propose_date,a.policy_date,a.create_date,a.claim_approved_date,d.loss_desc,a.claim_chronology,t.vs_description,a.pol_no,a.pla_no,a.dla_no,"
                + "a.cust_name,a.ins_policy_type_grp_id,b.ref2,b.ref1,a.claim_cause,b.refd1,b.refd2,b.refd3,b.ref3d,k.cause_desc,l.short_desc,a.claim_client_name,"
                + "a.claim_account_no,c.vs_description,a.kreasi_type_desc,b.ref7d,b.ref12,q.ent_name,a.cc_code,s.region_code, b.insured_amount,a.ccy_rate,"
                + "a.claim_currency,a.ccy_rate_claim,a.claim_amount_est,a.claim_amount,a.claim_amount_approved,a.claim_date,b.refn2,a.pol_type_id,f.ent_name,"
                + "e.ent_name,a.entity_id,a.prod_id,m.ent_name,n.user_name,o.user_name,p.treaty_grp_id,a.cc_code_source,r.region_code,v.vs_description,u.description,l.description "
                + " order by a.pla_date,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

//        if (ACT_CLAIM) {
//            updateOSKlaimLKSLKP(l, "LKP");
////            updateBalance(sql);
//        }

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

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
            row0.createCell(1).setCellValue("uy reins");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("period_end");
            row0.createCell(4).setCellValue("pol_id");
            row0.createCell(5).setCellValue("dla_date");
            row0.createCell(6).setCellValue("claim_date");
            row0.createCell(7).setCellValue("policy_date");
            row0.createCell(8).setCellValue("approved_date");
            row0.createCell(9).setCellValue("pol_no");
            row0.createCell(10).setCellValue("pla_no");
            row0.createCell(11).setCellValue("dla_no");
            row0.createCell(12).setCellValue("cust_name");
            row0.createCell(13).setCellValue("name");
            row0.createCell(14).setCellValue("coins_name");
            row0.createCell(15).setCellValue("coins_amount");
            row0.createCell(16).setCellValue("insured_amount");
            row0.createCell(17).setCellValue("claim_amount");
            row0.createCell(18).setCellValue("claim_amount_est");
            row0.createCell(19).setCellValue("claim_amount_approved");
            row0.createCell(20).setCellValue("claim_or");
            row0.createCell(21).setCellValue("claim_bppdan");
            row0.createCell(22).setCellValue("claim_spl");
            row0.createCell(23).setCellValue("claim_fac");
            row0.createCell(24).setCellValue("claim_qs");
            row0.createCell(25).setCellValue("claim_park");
            row0.createCell(26).setCellValue("claim_faco");
            row0.createCell(27).setCellValue("claim_faco1");
            row0.createCell(28).setCellValue("claim_faco2");
            row0.createCell(29).setCellValue("claim_faco3");
            row0.createCell(30).setCellValue("claim_jp");
            row0.createCell(31).setCellValue("claim_qskr");
            row0.createCell(32).setCellValue("claim_kscbi");
            row0.createCell(33).setCellValue("Premiko");
            row0.createCell(34).setCellValue("COB");
            row0.createCell(35).setCellValue("Penyebab Klaim");
            row0.createCell(36).setCellValue("Tgl Setujui");
            row0.createCell(37).setCellValue("No Bukti");
            row0.createCell(38).setCellValue("Tgl Bayar");
            row0.createCell(39).setCellValue("Marketer");
            row0.createCell(40).setCellValue("Nama Nasabah");
            row0.createCell(41).setCellValue("No Rekening");
            row0.createCell(42).setCellValue("Entity ID");
            row0.createCell(43).setCellValue("Risk Code");
            row0.createCell(44).setCellValue("Status Kerugian");
            row0.createCell(45).setCellValue("Kurs Klaim");
            row0.createCell(46).setCellValue("Kurs");
            row0.createCell(47).setCellValue("Marketer ID");
            row0.createCell(48).setCellValue("Kronologi");
            row0.createCell(49).setCellValue("Tgl Lahir");
            row0.createCell(50).setCellValue("Tgl Entry");
            row0.createCell(51).setCellValue("Create Who");
            row0.createCell(52).setCellValue("Approved Who");
            row0.createCell(53).setCellValue("Marketing Officer");
            row0.createCell(54).setCellValue("Tanggal Pengajuan");
            row0.createCell(55).setCellValue("Jenis Marketer");
            row0.createCell(56).setCellValue("Sumbis");
            row0.createCell(57).setCellValue("Jenis Kredit");
            row0.createCell(58).setCellValue("Pekerjaan");
            row0.createCell(59).setCellValue("No.Rek Pinjaman");
            row0.createCell(60).setCellValue("Koda");
            row0.createCell(61).setCellValue("Region");
            row0.createCell(62).setCellValue("Koda Non AKS");
            row0.createCell(63).setCellValue("Region Non AKS");
            row0.createCell(64).setCellValue("Potensi");
            row0.createCell(65).setCellValue("Kategori");
            row0.createCell(66).setCellValue("Cabang");
            row0.createCell(67).setCellValue("Cob");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("name"));
            if (h.getFieldValueByFieldNameST("coins_name") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("coins_name"));
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_est") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_est").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_approved") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_bppdan") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco1") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("claim_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco2") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("claim_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco3") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("claim_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_jp") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("claim_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qskr") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("claim_qskr").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_kscbi") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("premiko").doubleValue());
            }
            /*
            if (getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus()==null) continue;
            row.createCell(23).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus());
             */
            if (h.getFieldValueByFieldNameST("pol_id") != null) {
                //row.createCell(31).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getStPolicyTypeDesc());
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                //row.createCell(32).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
                row.createCell(35).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            }
            if (ACT_CLAIM) {
                if (h.getFieldValueByFieldNameST("claim_status").equalsIgnoreCase("PLA")) {
                    if (h.getFieldValueByFieldNameDT("approved_date") != null) {
                        row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
                    }
                }
            } else if (EFF_CLAIM) {
                if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                    row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
                }
            }
            if (h.getFieldValueByFieldNameST("payment_note") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("payment_note"));
            }
            if (h.getFieldValueByFieldNameST("payment_date") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("payment_date"));
            }
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            if (h.getFieldValueByFieldNameST("ins_policy_type_grp_id").equalsIgnoreCase("10")) {
                if (h.getFieldValueByFieldNameST("claim_client_name") != null) {
                    row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("claim_client_name"));
                }
                if (h.getFieldValueByFieldNameST("claim_account_no") != null) {
                    row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("claim_account_no"));
                }
            }
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("entity_id"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameST("status_kerugian"));
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameST("claim_currency"));
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameST("prod_id"));
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(49).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameDT("create_date"));
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameST("createwho"));
            if (h.getFieldValueByFieldNameST("approvedwho") != null) {
                row.createCell(52).setCellValue(h.getFieldValueByFieldNameST("approvedwho"));
            }
            if (h.getFieldValueByFieldNameST("marketingofficer") != null) {
                row.createCell(53).setCellValue(h.getFieldValueByFieldNameST("marketingofficer"));
            }
            if (h.getFieldValueByFieldNameDT("propose_date") != null) {
                row.createCell(54).setCellValue(h.getFieldValueByFieldNameDT("propose_date"));
            }
            if (h.getFieldValueByFieldNameST("jengrup") != null) {
                row.createCell(55).setCellValue(h.getFieldValueByFieldNameST("jengrup"));
            }
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            if (h.getFieldValueByFieldNameST("kreasi_type_desc") != null) {
                row.createCell(57).setCellValue(h.getFieldValueByFieldNameST("kreasi_type_desc"));
            }
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameST("ref7d"));
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameST("ref12"));
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameST("region_code"));
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameST("cc_code_source"));
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameST("region_code_source"));
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameST("potensi_subro"));
            row.createCell(65).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kategori")));
            row.createCell(66).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(67).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cob")));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_RKS_REINS() throws Exception {
        //final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        //final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.approved_date,substr(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))::text,1,4) as uy,"
                + "e.ent_name as ent_name,f.ent_name as prod_name,a.kreasi_type_desc,b.ref7d,b.ref12,q.vs_description as kategori,p.description as cabang,k.description as cob,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                //+ "getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,a.period_start) as period_start,getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,a.period_end) as period_end,"
                + "a.ins_policy_type_grp_id::character varying,a.pol_type_id::character varying,a.pol_id::character varying,a.claim_date,a.pla_date,a.claim_propose_date as propose_date,a.status, "
                + "coalesce(a.pla_date,null) as pla_date,a.policy_date,a.pol_no,a.pla_no,a.cust_name,upper(checkstatus(a.ins_policy_type_grp_id=10,b.ref2,b.ref1)) as name,a.claim_cause::text,"
                + "coalesce((b.insured_amount*a.ccy_rate),0) as insured_amount,coalesce((a.claim_amount_est*a.ccy_rate_claim),0) as claim_amount_est,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,coalesce((a.claim_amount_approved*a.ccy_rate_claim),0) as claim_amount_approved,"
                + "coalesce(checkreas(a.pol_type_id=21,b.refn2*a.ccy_rate),0) as coins_amount,(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir, "
                + "a.claim_client_name,a.claim_account_no,a.entity_id::text,c.ent_name as marketingofficer,"
                + "(select coalesce(e.short_name,'') from ent_master e where a.coinsurer_id::bigint = e.ent_id) as coins_name,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or,  "
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
                + "d.vs_description as jengrup,a.cc_code,m.region_code,a.cc_code_source,n.region_code as region_code_source,l.ent_name as paymentname,o.vs_description as potensi_subro ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id"
                + " left join ent_master c on c.ent_id::text = a.marketing_officer_who "
                + " inner join ent_master e on e.ent_id = a.entity_id"
                + " inner join ent_master f on f.ent_id = a.prod_id"
                + " left join s_valueset d on d.vs_code = f.ref1 and d.vs_group = 'COMP_TYPE' "
                + " left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + " left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ins_policy_types k on k.pol_type_id = a.pol_type_id "
                + " left join ent_master l on l.ent_id = a.payment_company_id::int "
                + " left join s_region m on m.region_id = a.region_id "
                + " left join s_region n on n.region_id = a.region_id_source "
                + "left join s_valueset o on o.vs_code = a.ref13 and o.vs_group='SUBROGASI' "
                + "inner join gl_cost_center p on p.cc_code = a.cc_code "
                + "left join s_valueset q on q.vs_code = e.category1 and q.vs_group = 'ASK_BUS_SOURCE' ");

        sqa.addClause("a.pol_id not in ( "
                + "select a.parent_id "
                + "from ins_policy a "
                + "where a.claim_status = 'DLA' and a.active_flag = 'Y' "
                + "and (a.effective_flag = 'N' or a.effective_flag is null) "
                + "and a.status in ('CLAIM','CLAIM ENDORSE')) ");

        sqa.addClause("a.status IN ('CLAIM')");
        sqa.addClause("a.claim_status = 'PLA'");
        sqa.addClause("a.active_flag = 'Y'");

        if (EFF_CLAIM) {
            sqa.addClause("a.effective_flag = 'Y'");
        }

        if (ACT_CLAIM) {
            sqa.addClause("a.effective_flag = 'N'");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= ?");
            sqa.addPar(PLADateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= ?");
            sqa.addPar(PLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
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
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
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

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("e.ref1 in " + type);
//            sqa.addPar(type);
        }

        if (stMarketerOffID != null) {
            //sqa.addClause("a.marketing_officer_who = '" + stMarketerOffID + "'");
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stObject != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("10")) {
                sqa.addClause("upper(b.ref2) like ?");
            } else {
                sqa.addClause("upper(b.ref1) like ?");
            }
            sqa.addPar('%' + stObject.toUpperCase() + '%');
        }

        if (stCompanyProdID != null) {
//            sqa.addClause("e.ref2 = '" + stCompanyProdID + "'");
            sqa.addClause("f.ref2 = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("k.business_type_id = ?");
            sqa.addPar(stBussinessPolType);
        }

        final String sql = sqa.getSQL() + "   group by a.status,a.coinsurer_id,b.refd1,b.refd2,b.refd3,a.period_start,a.period_end,a.pol_type_id,a.pol_id,"
                + "a.pla_date,a.claim_propose_date,o.vs_description,a.policy_date,a.claim_date,a.pol_no,a.pla_no,a.cust_name,a.ins_policy_type_grp_id,b.ref2,"
                + "b.ref1,a.claim_cause,a.claim_client_name,a.claim_account_no,a.kreasi_type_desc,b.ref7d,b.ref12,a.cc_code,n.region_code,b.insured_amount,"
                + "a.ccy_rate,a.ccy_rate_claim,a.claim_amount_est,a.claim_amount,a.claim_amount_approved,b.refn2,a.pol_type_id,a.entity_id,f.ent_name,"
                + "e.ent_name,c.ent_name,d.vs_description,a.cc_code_source,m.region_code,l.ent_name,q.vs_description,p.description,k.description "
                + " order by a.pla_date,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

//        if (EFF_CLAIM) {
//            updateOSKlaimLKSLKP(l, "LKS");
//        }

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RKS_REINS() throws Exception {

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
            row0.createCell(4).setCellValue("pla_date");
            row0.createCell(5).setCellValue("claim_date");
            row0.createCell(6).setCellValue("policy_date");
            row0.createCell(7).setCellValue("pol_no");
            row0.createCell(8).setCellValue("pla_no");
            row0.createCell(9).setCellValue("cust_name");
            row0.createCell(10).setCellValue("name");
            row0.createCell(11).setCellValue("coins_name");
            row0.createCell(12).setCellValue("insured_amount");
            row0.createCell(13).setCellValue("claim_amount");
            row0.createCell(14).setCellValue("claim_amount_est");
            row0.createCell(15).setCellValue("claim_or");
            row0.createCell(16).setCellValue("claim_bppdan");
            row0.createCell(17).setCellValue("claim_fac");
            row0.createCell(18).setCellValue("claim_spl");
            row0.createCell(19).setCellValue("claim_qs");
            row0.createCell(20).setCellValue("claim_park");
            row0.createCell(21).setCellValue("claim_faco");
            row0.createCell(22).setCellValue("claim_faco1");
            row0.createCell(23).setCellValue("claim_faco2");
            row0.createCell(24).setCellValue("claim_faco3");
            row0.createCell(25).setCellValue("claim_jp");
            row0.createCell(26).setCellValue("claim_qskr");
            row0.createCell(27).setCellValue("claim_kscbi");
            row0.createCell(28).setCellValue("coins_amount");
            row0.createCell(29).setCellValue("penyebab klaim");
            row0.createCell(30).setCellValue("cob");
            row0.createCell(31).setCellValue("Nama Nasabah");
            row0.createCell(32).setCellValue("No Rekening");
            row0.createCell(33).setCellValue("Entity ID");
            row0.createCell(34).setCellValue("Marketer");
            row0.createCell(35).setCellValue("Marketer Officer");
            row0.createCell(36).setCellValue("Tanggal Pengajuan");
            row0.createCell(37).setCellValue("Jenis Marketer");
            row0.createCell(37).setCellValue("Jenis Marketer");
            row0.createCell(38).setCellValue("Tgl Lahir");
            row0.createCell(39).setCellValue("Sumbis");
            row0.createCell(40).setCellValue("Jenis Kredit");
            row0.createCell(41).setCellValue("Pekerjaan");
            row0.createCell(42).setCellValue("No.Rek Pinjaman");
            row0.createCell(43).setCellValue("Koda");
            row0.createCell(44).setCellValue("Region");
            row0.createCell(45).setCellValue("Koda Non AKS");
            row0.createCell(46).setCellValue("Region Non AKS");
            row0.createCell(47).setCellValue("Potensi");
            row0.createCell(48).setCellValue("Kategori");
            row0.createCell(49).setCellValue("Cabang");
            row0.createCell(50).setCellValue("Cob");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            if (h.getFieldValueByFieldNameST("uy") != null) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("uy"));
            }
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (h.getFieldValueByFieldNameDT("pla_date") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("pla_date"));
            }
            if (h.getFieldValueByFieldNameDT("claim_date") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(9).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cust_name")));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("name"));
            if (h.getFieldValueByFieldNameST("coins_name") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("coins_name"));
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_est") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_est").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_bppdan") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco1") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco2") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("claim_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco3") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("claim_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_jp") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qskr") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("claim_qskr").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_kscbi") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            /*if (getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getStStatus()!=null)
            row.createCell(21).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getStStatus());
             */
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                row.createCell(29).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
            }
            if (h.getFieldValueByFieldNameST("pol_id") != null) {
                row.createCell(30).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getStPolicyTypeDesc());
            }
            if (h.getFieldValueByFieldNameST("ins_policy_type_grp_id").equalsIgnoreCase("10")) {
                if (h.getFieldValueByFieldNameST("claim_client_name") != null) {
                    row.createCell(31).setCellValue(h.getFieldValueByFieldNameST("claim_client_name"));
                }
                if (h.getFieldValueByFieldNameST("claim_account_no") != null) {
                    row.createCell(32).setCellValue(h.getFieldValueByFieldNameST("claim_account_no"));
                }
            }
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameST("entity_id"));
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            if (h.getFieldValueByFieldNameST("marketingofficer") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameST("marketingofficer"));
            }
            if (h.getFieldValueByFieldNameDT("propose_date") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("propose_date"));
            }
            if (h.getFieldValueByFieldNameST("jengrup") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("jengrup"));
            }
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("kreasi_type_desc"));
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("ref7d"));
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("ref12"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameST("region_code"));
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameST("cc_code_source"));
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameST("region_code_source"));
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameST("potensi_subro"));
            row.createCell(48).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("kategori")));
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(50).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cob")));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return costcenter;
    }

    public InsurancePolicyView getPolicy(String stParentID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stParentID);
    }

    public DTOList EXCEL_LOSS_PROFILE_CL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.lvl,a.ref4,a.ref5,"
                + " coalesce(count(b.pol_no),0) as jumlah,coalesce(sum(b.claim_amount),0) as claim_amount");

        String sql = "left join ( "
                + " select policy_date,substr(policy_date::text,1,4) as years,pol_no,status,"
                + " sum(insured_amount*ccy_rate) as insured_amount,sum(claim_amount*ccy_rate_claim) as claim_amount"
                + " from ins_policy "
                + " where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y'";

        if (getAppDateFrom() != null) {
            sql = sql + " and date_trunc('day',approved_date) >= ?";
            sqa.addPar(appDateFrom);
        }

        if (getAppDateTo() != null) {
            sql = sql + " and date_trunc('day',approved_date) <= ?";
            sqa.addPar(appDateTo);
        }

        if (getClaimDateFrom() != null) {
            sql = sql + " and date_trunc('day',claim_date) >= ?";
            sqa.addPar(claimDateFrom);
        }

        if (getClaimDateTo() != null) {
            sql = sql + " and date_trunc('day',claim_date) <= ?";
            sqa.addPar(claimDateTo);
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',period_start) >= ?";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',period_start) <= ?";
            sqa.addPar(periodTo);
        }

        if (getStBranch() != null) {
            sql = sql + " and cc_code = ?";
            sqa.addPar(stBranch);
        }

        if (getStPolicyTypeGroupID() != null) {
            sql = sql + " and ins_policy_type_grp_id = ?";
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            sql = sql + " and pol_type_id = ?";
            sqa.addPar(stPolicyTypeID);
        }

        sqa.addQuery(" from band_level a " + sql + " group by policy_date,substr(policy_date::text,1,4),pol_no,status"
                + " order by substr(policy_date::text,1,4) ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
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

    public void EXPORT_LOSS_PROFILE_CL() throws Exception {

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
            if (getAppDateFrom() != null) {
                row2.createCell(0).setCellValue("APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " TO " + DateUtil.getDateStr(getAppDateTo()));
            }

            //bikin header
            XSSFRow row3 = sheet.createRow(3);
            if (getPeriodFrom() != null) {
                row3.createCell(0).setCellValue("U. YEAR : " + DateUtil.getYear(getPeriodFrom()));
            }

            //bikin header
            XSSFRow row4 = sheet.createRow(4);
            if (getStBranch() != null) {
                row4.createCell(0).setCellValue("BRANCH : " + getStBranch());
            }

            //bikin header
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("SUM INSURED");
            row5.createCell(1).setCellValue("INCURRED CLAIM");

            //bikin header
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("RISK BASIS");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_CLAIM_REAS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	substr(a.period_start::text,1,4) as uw,a.period_start,a.period_end,a.claim_date,"
                + "a.pol_id,a.pol_no,a.dla_no,b.ref1,b.ref2,a.status,"
                + "(select cause_desc from ins_clm_cause x where x.ins_clm_caus_id = a.claim_cause) as cause_desc,"
                + "sum(checkreas(j.treaty_type='BPDAN',i.claim_amount)) as claim_bpdan,"
                + "sum(checkreas(j.treaty_type='OR',i.claim_amount)) as claim_or,"
                + "sum(checkreas(j.treaty_type='QS',i.claim_amount)) as claim_qs,"
                + "sum(checkreas(j.treaty_type='SPL',i.claim_amount)) as claim_spl,"
                + "sum(checkreas(j.treaty_type='FAC',i.claim_amount)) as claim_fac,"
                + "sum(checkreas(j.treaty_type='PARK',i.claim_amount)) as claim_park,"
                + "sum(checkreas(j.treaty_type='FACO',i.claim_amount)) as claim_faco,"
                + "sum(checkreas(j.treaty_type='XOL1',i.claim_amount)) as claim_xol1,"
                + "sum(checkreas(j.treaty_type='XOL2',i.claim_amount)) as claim_xol2,"
                + "sum(checkreas(j.treaty_type='XOL3',i.claim_amount)) as claim_xol3,"
                + "sum(checkreas(j.treaty_type='XOL4',i.claim_amount)) as claim_xol4,"
                + "sum(checkreas(j.treaty_type='XOL5',i.claim_amount)) as claim_xol5");

        sqa.addQuery(" from ins_policy a"
                + " inner join ins_pol_obj b on b.ins_pol_obj_id=a.claim_object_id"
                + " inner join ins_policy_types f on f.pol_type_id =a.pol_type_id"
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ent_master k on k.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.effective_flag='Y'");

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = sqa.getSQL() + " group by a.claim_date,a.period_start,a.period_end,a.pol_id,a.pol_no,a.dla_no,substr(a.period_start::text,1,4),a.status,b.ref1,b.ref2,a.claim_cause "
                + " order by a.pol_no asc";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAIM_REAS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("uw");
            row0.createCell(1).setCellValue("period_start");
            row0.createCell(2).setCellValue("period_end");
            row0.createCell(3).setCellValue("claim_date");
            row0.createCell(4).setCellValue("pol_id");
            row0.createCell(5).setCellValue("pol_no");
            row0.createCell(6).setCellValue("dla_no");
            row0.createCell(7).setCellValue("nama");
            row0.createCell(8).setCellValue("usia");
            row0.createCell(9).setCellValue("status");
            row0.createCell(10).setCellValue("Penyebab Klaim");
            row0.createCell(11).setCellValue("claim_bpdan");
            row0.createCell(12).setCellValue("claim_or");
            row0.createCell(13).setCellValue("claim_qs");
            row0.createCell(14).setCellValue("claim_spl");
            row0.createCell(15).setCellValue("claim_fac");
            row0.createCell(16).setCellValue("claim_park");
            row0.createCell(17).setCellValue("claim_faco");
            row0.createCell(18).setCellValue("claim_xol1");
            row0.createCell(19).setCellValue("claim_xol2");
            row0.createCell(20).setCellValue("claim_xol3");
            row0.createCell(21).setCellValue("claim_xol4");
            row0.createCell(22).setCellValue("claim_xol5");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("uw"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            if (h.getFieldValueByFieldNameST("ref1") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            }
            if (h.getFieldValueByFieldNameST("ref2") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ref2"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(10).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            if (h.getFieldValueByFieldNameBD("claim_bpdan") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_bpdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol1") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim_xol1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol2") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_xol2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol3") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_xol3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol4") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_xol4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol5") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_xol5").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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

    public void EXCEL_KLAIM_MV() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code as cabang,a.dla_no,f.ref4 as rangka,f.ref5 as mesin,f.ref3 as tahun,f.ref1 as polisi,';'||a.pol_no as pol_no,a.status,a.insured_amount,"
                + " a.claim_date as tgl_klaim,a.dla_date as tgl_LKP,a.claim_approved_date as tgl_setujui,a.event_location as tempat_kejadian, e.vs_description as lokasi_kejadian,"
                + " b.cause_desc as penyebab_klaim,a.claim_chronology as kronologi,a.ccy as mata_uang,a.claim_loss_status as status_klaim,"
                + " a.claim_amount_est as klaim_ajukan,a.claim_amount_approved as klaim_disetujui,a.claim_amount as biaya_klaim,"
                + " (select sum(checkreas(x.ins_item_id = '47',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as deduct,"
                + " (select sum(checkreas(x.ins_item_id = '48',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as subrogation,"
                + " (select sum(checkreas(x.ins_item_id = '49',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Wreck,"
                + " (select sum(checkreas(x.ins_item_id = '50',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Adjuster_Fee,"
                + " (select sum(checkreas(x.ins_item_id = '51',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as tjh,"
                + " (select sum(checkreas(x.ins_item_id = '52',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Biaya_Derek,"
                + " (select sum(checkreas(x.ins_item_id = '53',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Salvage,"
                + " (select sum(checkreas(x.ins_item_id = '54',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Ex_Gratia,"
                + " (select sum(checkreas(x.ins_item_id = '55',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Bunga,"
                + " (select sum(checkreas(x.ins_item_id = '56',x.amount))"
                + " from ins_pol_items x"
                + " where x.pol_id = a.pol_id) as Santunan_Kecelakaan,"
                + " g.loss_desc as status_kerugian,"
                + " (select string_agg(l.description, '| ') "
                + " from ins_cover l "
                + " inner join ins_pol_cover z on z.ins_cover_id = l.ins_cover_id "
                + " and z.ins_cover_id in (2,22,29,62,63,64,65,91,92,93,94,101,107,108,114,120,121,122,123,128,129,130,131,140,141,142,148,210,234,251,280,281,282,283,284,285,286,287,288,289,290) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id) as jaminan1 "/*
                + " (select cekstatus(x.ins_cover_id in (2),q.description) "
                + " from ins_pol_obj z "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (2) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan1, "
                + " (select cekstatus(x.ins_cover_id in (107),q.description) "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (107) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan2, "
                + " (select cekstatus(x.ins_cover_id in (108),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (108) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan3, "
                + " (select cekstatus(x.ins_cover_id in (101),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (101) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan4, "
                + " (select cekstatus(x.ins_cover_id in (62),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (62) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan5, "
                + " (select cekstatus(x.ins_cover_id in (63),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (63) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan6, "
                + " (select cekstatus(x.ins_cover_id in (64),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (64) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan7, "
                + " (select cekstatus(x.ins_cover_id in (91),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (91) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan8, "
                + " (select cekstatus(x.ins_cover_id in (92),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (92) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan9, "
                + " (select cekstatus(x.ins_cover_id in (93),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (93) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan10, "
                + " (select cekstatus(x.ins_cover_id in (94),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (94) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan11, "
                + " (select cekstatus(x.ins_cover_id = '22',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '22' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan12, "
                + " (select cekstatus(x.ins_cover_id = '29',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '29' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan13, "
                + " (select cekstatus(x.ins_cover_id = '65',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '65' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan14, "
                + " (select cekstatus(x.ins_cover_id = '114',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '114' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan15, "
                + " (select cekstatus(x.ins_cover_id = '120',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '120' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan16, "
                + " (select cekstatus(x.ins_cover_id = '121',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '121' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan17, "
                + " (select cekstatus(x.ins_cover_id = '122',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '122' "
                + "where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan18, "
                + " (select cekstatus(x.ins_cover_id = '123',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '123' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan19, "
                + " (select cekstatus(x.ins_cover_id = '128',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '128' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan20, "
                + " (select cekstatus(x.ins_cover_id = '129',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '129' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan21, "
                + " (select cekstatus(x.ins_cover_id = '130',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '130' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan22, "
                + " (select cekstatus(x.ins_cover_id = '131',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '131' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan23, "
                + " (select cekstatus(x.ins_cover_id = '140',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '140' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan24, "
                + " (select cekstatus(x.ins_cover_id = '141',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '141' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan25, "
                + " (select cekstatus(x.ins_cover_id = '142',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '142' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan26, "
                + " (select cekstatus(x.ins_cover_id = '148',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '148' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan27, "
                + " (select cekstatus(x.ins_cover_id = '210',q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id = '210' "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan28, "
                + " (select cekstatus(x.ins_cover_id in (280),q.description) "
                + " from ins_pol_obj z "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (280) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan29, "
                + " (select cekstatus(x.ins_cover_id in (281),q.description) "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (281) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan30, "
                + " (select cekstatus(x.ins_cover_id in (282),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (282) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan31, "
                + " (select cekstatus(x.ins_cover_id in (283),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (283) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan32, "
                + " (select cekstatus(x.ins_cover_id in (284),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (284) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan33, "
                + " (select cekstatus(x.ins_cover_id in (285),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (285) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan34, "
                + " (select cekstatus(x.ins_cover_id in (286),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (286) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan35, "
                + " (select cekstatus(x.ins_cover_id in (287),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (287) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan36, "
                + " (select cekstatus(x.ins_cover_id in (288),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (288) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan37, "
                + " (select cekstatus(x.ins_cover_id in (289),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (289) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan38, "
                + " (select cekstatus(x.ins_cover_id in (290),q.description)  "
                + " from ins_pol_obj z  "
                + " inner join ins_pol_cover x on x.ins_pol_obj_id = z.ins_pol_obj_id  "
                + " inner join ins_cover q on q.ins_cover_id = x.ins_cover_id and q.ins_cover_id in (290) "
                + " where z.ins_pol_obj_id = f.ins_pol_obj_id group by q.description,x.ins_cover_id) as jaminan39 "*/);

        sqa.addQuery(" from ins_policy a"
                + " inner join ins_pol_obj f on f.ins_pol_obj_id = a.claim_object_id"
                + " left join gl_cost_center c on c.cc_code = a.cc_code"
                + " inner join ins_policy_types d on d.pol_type_id = a.pol_type_id "
                + " left join ins_clm_cause b on b.ins_clm_caus_id = a.claim_cause "
                + " left join ins_claim_loss g on g.claim_loss_id = f.claim_loss_id "
                + " left join s_valueset e on e.vs_code = a.claim_location_id and e.vs_group = 'CLAIM_KODE_WILAYAH' ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.status in('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.ins_policy_type_grp_id = 11 ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            //sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
            //sqa.addPar(appDateTo);
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

        sql = sql + " order by a.cc_code,a.pol_no,a.dla_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_CLAIM_MV_" + System.currentTimeMillis() + ".csv";

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

    public void EXPORT_KLAIM_MV() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("dla_no");
            row0.createCell(2).setCellValue("rangka");
            row0.createCell(3).setCellValue("mesin");
            row0.createCell(4).setCellValue("polisi");
            row0.createCell(5).setCellValue("pol_no");
            row0.createCell(6).setCellValue("status");
            row0.createCell(7).setCellValue("tgl_klaim");
            row0.createCell(8).setCellValue("tgl_lkp");
            row0.createCell(9).setCellValue("tgl_setujui");
            row0.createCell(10).setCellValue("tempat_kejadian");
            row0.createCell(11).setCellValue("penyebab_klaim");
            row0.createCell(12).setCellValue("kronologi");
            row0.createCell(13).setCellValue("mata_uang");
            row0.createCell(14).setCellValue("status_klaim");
            row0.createCell(15).setCellValue("insured_amount");
            row0.createCell(16).setCellValue("klaim_ajukan");
            row0.createCell(17).setCellValue("klaim_disetujui");
            row0.createCell(18).setCellValue("biaya_klaim");
            row0.createCell(19).setCellValue("deduct");
            row0.createCell(20).setCellValue("subrogation");
            row0.createCell(21).setCellValue("wreck");
            row0.createCell(22).setCellValue("adjuster_fee");
            row0.createCell(23).setCellValue("tjh");
            row0.createCell(24).setCellValue("biaya_derek");
            row0.createCell(25).setCellValue("salvage");
            row0.createCell(26).setCellValue("ex_gratia");
            row0.createCell(27).setCellValue("bunga");
            row0.createCell(28).setCellValue("santunan_kecelakaan");
            row0.createCell(29).setCellValue("jaminan/coverage");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("rangka"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("mesin"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("polisi"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tgl_klaim"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tgl_lkp"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameDT("tgl_setujui"));
            if (h.getFieldValueByFieldNameST("tempat_kejadian") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("tempat_kejadian"));
            }
            if (h.getFieldValueByFieldNameST("penyebab_klaim") != null) {
                row.createCell(11).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("penyebab_klaim")));
            }
            if (h.getFieldValueByFieldNameST("kronologi") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("mata_uang"));
            if (h.getFieldValueByFieldNameST("status_klaim") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("status_klaim"));
            }
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("klaim_ajukan").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("klaim_disetujui").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("biaya_klaim").doubleValue());
            if (h.getFieldValueByFieldNameBD("deduct") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("deduct").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("subrogation") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("subrogation").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("wreck") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("wreck").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("adjuster_fee") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("adjuster_fee").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("tjh") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("tjh").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("biaya_derek") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("biaya_derek").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("salvage") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("ex_gratia") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("ex_gratia").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("bunga") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("santunan_kecelakaan") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("santunan_kecelakaan").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("jaminan1") != null
                    || h.getFieldValueByFieldNameST("jaminan2") != null
                    || h.getFieldValueByFieldNameST("jaminan3") != null
                    || h.getFieldValueByFieldNameST("jaminan4") != null
                    || h.getFieldValueByFieldNameST("jaminan5") != null
                    || h.getFieldValueByFieldNameST("jaminan6") != null
                    || h.getFieldValueByFieldNameST("jaminan7") != null
                    || h.getFieldValueByFieldNameST("jaminan8") != null
                    || h.getFieldValueByFieldNameST("jaminan9") != null
                    || h.getFieldValueByFieldNameST("jaminan10") != null
                    || h.getFieldValueByFieldNameST("jaminan11") != null
                    || h.getFieldValueByFieldNameST("jaminan12") != null
                    || h.getFieldValueByFieldNameST("jaminan13") != null
                    || h.getFieldValueByFieldNameST("jaminan14") != null
                    || h.getFieldValueByFieldNameST("jaminan15") != null
                    || h.getFieldValueByFieldNameST("jaminan16") != null
                    || h.getFieldValueByFieldNameST("jaminan17") != null
                    || h.getFieldValueByFieldNameST("jaminan18") != null
                    || h.getFieldValueByFieldNameST("jaminan19") != null
                    || h.getFieldValueByFieldNameST("jaminan20") != null
                    || h.getFieldValueByFieldNameST("jaminan21") != null
                    || h.getFieldValueByFieldNameST("jaminan22") != null
                    || h.getFieldValueByFieldNameST("jaminan23") != null
                    || h.getFieldValueByFieldNameST("jaminan24") != null
                    || h.getFieldValueByFieldNameST("jaminan25") != null
                    || h.getFieldValueByFieldNameST("jaminan26") != null
                    || h.getFieldValueByFieldNameST("jaminan27") != null
                    || h.getFieldValueByFieldNameST("jaminan28") != null
                    || h.getFieldValueByFieldNameST("jaminan29") != null
                    || h.getFieldValueByFieldNameST("jaminan30") != null
                    || h.getFieldValueByFieldNameST("jaminan31") != null
                    || h.getFieldValueByFieldNameST("jaminan32") != null
                    || h.getFieldValueByFieldNameST("jaminan33") != null
                    || h.getFieldValueByFieldNameST("jaminan34") != null
                    || h.getFieldValueByFieldNameST("jaminan35") != null
                    || h.getFieldValueByFieldNameST("jaminan36") != null
                    || h.getFieldValueByFieldNameST("jaminan37") != null
                    || h.getFieldValueByFieldNameST("jaminan38") != null
                    || h.getFieldValueByFieldNameST("jaminan39") != null) {
                row.createCell(29).setCellValue(
                        LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan1")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan2")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan3")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan4")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan5")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan6")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan7")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan8")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan9")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan10")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan11")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan12")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan13")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan14")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan15")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan16")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan17")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan18")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan19")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan20")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan21")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan22")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan23")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan24")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan25")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan26")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan27")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan28")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan29")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan30")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan31")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan32")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan33")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan34")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan35")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan36")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan37")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan38")) + ", "
                        + LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan39")));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public DTOList REKAP_KLAIM_CAB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.category,a.cc_code,sum(a.claim_amount_endorse) as hutang_klaim,a.active_flag,a.refd2,a.refd3 ");

        sqa.addQuery(" from claim a ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String sql = "select b.cc_code,"
                + " sum(getkoas(category='1',hutang_klaim)) as nd_comm1,"
                + " sum(getkoas(category='2',hutang_klaim)) as nd_comm2,"
                + " sum(getkoas(category='3',hutang_klaim)) as nd_comm3,"
                + " sum(getkoas(category='4',hutang_klaim)) as nd_comm4, "
                + " sum(hutang_klaim) as premi_total_adisc "
                + " from gl_cost_center b left join ( "
                + sqa.getSQL() + " group by a.category,a.cc_code,a.active_flag,a.refd2,a.refd3 "
                + " ) a on a.cc_code = b.cc_code "
                + " where a.active_flag = 'Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " group by b.cc_code order by b.cc_code";

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

    public DTOList REKAP_KLAIM_JENIS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.category,a.pol_type_id,sum(a.claim_amount_endorse) as hutang_klaim,a.active_flag,a.refd2,a.refd3 ");

        sqa.addQuery("from claim a ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String sql = "select b.pol_type_id,"
                + " sum(getkoas(category='1',hutang_klaim)) as nd_comm1,"
                + " sum(getkoas(category='2',hutang_klaim)) as nd_comm2,"
                + " sum(getkoas(category='3',hutang_klaim)) as nd_comm3,"
                + " sum(getkoas(category='4',hutang_klaim)) as nd_comm4,"
                + " sum(hutang_klaim) as premi_total_adisc "
                + " from ins_pol_types b left join ( "
                + sqa.getSQL() + " group by a.category,a.pol_type_id,a.active_flag,a.refd2,a.refd3 "
                + " ) a on a.pol_type_id = b.pol_type_id "
                + " where a.active_flag = 'Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " group by b.pol_type_id order by b.pol_type_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public String getStFltTreatyType() {
        return stFltTreatyType;
    }

    public void setStFltTreatyType(String stFltTreatyType) {
        this.stFltTreatyType = stFltTreatyType;
    }

    public String getStZoneCode() {
        return stZoneCode;
    }

    public void setStZoneCode(String stZoneCode) {
        this.stZoneCode = stZoneCode;
    }

    public String getStFltCoverType() {
        return stFltCoverType;
    }

    public void setStFltCoverType(String stFltCoverType) {
        this.stFltCoverType = stFltCoverType;
    }

    public String getStRiskCardNo() {
        return stRiskCardNo;
    }

    public void setStRiskCardNo(String stRiskCardNo) {
        this.stRiskCardNo = stRiskCardNo;
    }

    public String getStCustCategory1() {
        return stCustCategory1;
    }

    public void setStCustCategory1(String stCustCategory1) {
        this.stCustCategory1 = stCustCategory1;
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

    public String getStRiskCode() {
        return stRiskCode;
    }

    public void setStRiskCode(String stRiskCode) {
        this.stRiskCode = stRiskCode;
    }

    public DTOList RKP_CONFIRM() throws Exception {
        final String FLT_CLAIM_STATUS = (String) refPropMap.get("FLT_CLAIM_STATUS");
        final boolean EFFECTIVE = "Y".equalsIgnoreCase((String) refPropMap.get("EFFECTIVE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.pol_id,a.pol_no,a.dla_no,a.claim_object_id,b.ref1,a.ccy,a.claim_amount,a.claim_cust_amount,a.cc_code,a.pol_type_id,a.claim_letter_no,a.dla_remark, "
                + "(select x.ins_item_id from ins_pol_items x where x.pol_id = a.pol_id and x.ins_item_id = '54') as a, "
                + "(select x.ins_item_id from ins_pol_items x where x.pol_id = a.pol_id and x.ins_item_id = '50') as b ");

        sqa.addQuery("   from ins_policy a "
                + "	inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id");

        boolean isClaim = "CLAIM".equalsIgnoreCase(status);

        if (FLT_CLAIM_STATUS != null) {
            sqa.addClause("a.claim_status = ?");
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

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (stNoUrut != null) {
            sqa.addClause("a.claim_letter_no like ?");
            sqa.addPar('%' + stNoUrut + '%');
        }

        final String sql = "select pol_id,pol_no,dla_no,claim_object_id,ref1,ccy,claim_amount,claim_cust_amount,cc_code,pol_type_id,claim_letter_no,dla_remark,"
                + "coalesce(a,b) as parent_id from ( " + sqa.getSQL() + " ) x order by pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public String getStNoUrut() {
        return stNoUrut;
    }

    public void setStNoUrut(String stNoUrut) {
        this.stNoUrut = stNoUrut;
    }

    public String getStAuthorized() {
        return stAuthorized;
    }

    public void setStAuthorized(String stAuthorized) {
        this.stAuthorized = stAuthorized;
    }

    public DTOList EXCEL_REKAP_KLAIM_CAB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.category,a.cc_code,sum(a.claim_amount) as hutang_klaim,a.active_flag,a.refd2,a.refd3 ");

        sqa.addQuery(" from claim a ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String sql = "select b.cc_code,b.description,"
                + " coalesce(sum(getkoas(category='1',hutang_klaim)),0) as nd_comm1,"
                + " coalesce(sum(getkoas(category='2',hutang_klaim)),0) as nd_comm2,"
                + " coalesce(sum(getkoas(category='3',hutang_klaim)),0) as nd_comm3,"
                + " coalesce(sum(getkoas(category='4',hutang_klaim)),0) as nd_comm4, "
                + " coalesce(sum(hutang_klaim),0) as premi_total_adisc "
                + " from gl_cost_center b left join ( "
                + sqa.getSQL() + " group by a.category,a.cc_code,a.pol_type_id,a.active_flag,a.refd2,a.refd3 "
                + " ) a on a.cc_code = b.cc_code "
                + " where a.active_flag = 'Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " group by b.cc_code,b.description order by b.cc_code ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_REKAP_KLAIM_JENIS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.category,a.pol_type_id,sum(a.claim_amount) as hutang_klaim,a.active_flag,a.refd2,a.refd3 ");

        sqa.addQuery("from claim a ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        String sql = "select b.pol_type_id,b.short_desc,"
                + " coalesce(sum(getkoas(category='1',hutang_klaim)),0) as nd_comm1,"
                + " coalesce(sum(getkoas(category='2',hutang_klaim)),0) as nd_comm2,"
                + " coalesce(sum(getkoas(category='3',hutang_klaim)),0) as nd_comm3,"
                + " coalesce(sum(getkoas(category='4',hutang_klaim)),0) as nd_comm4,"
                + " coalesce(sum(hutang_klaim),0) as premi_total_adisc "
                + " from ins_pol_types b left join ( "
                + sqa.getSQL() + " group by a.category,a.pol_type_id,a.active_flag,a.refd2,a.refd3 "
                + " ) a on a.pol_type_id = b.pol_type_id "
                + " where a.active_flag = 'Y' ";

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " group by b.pol_type_id,b.short_desc order by b.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_REKAP_KLAIM_CAB() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("kode");
            row0.createCell(1).setCellValue("cabang");
            row0.createCell(2).setCellValue("umum");
            row0.createCell(3).setCellValue("pemda");
            row0.createCell(4).setCellValue("perusda");
            row0.createCell(5).setCellValue("bpd");
            row0.createCell(6).setCellValue("klaim");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            if (h.getFieldValueByFieldNameBD("nd_comm1") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm2") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm3") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nd_comm3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm4") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_comm4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_adisc") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_REKAP_KLAIM_JENIS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String jenpol = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("kode");
            row0.createCell(1).setCellValue("jenis");
            row0.createCell(2).setCellValue("umum");
            row0.createCell(3).setCellValue("pemda");
            row0.createCell(4).setCellValue("perusda");
            row0.createCell(5).setCellValue("bpd");
            row0.createCell(6).setCellValue("klaim");

            jenpol = String.valueOf(h.getFieldValueByFieldNameBD("pol_type_id"));
            if (jenpol.length() == 1) {
                jenpol = "0" + jenpol;
            } else {
                jenpol = jenpol;
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(jenpol);
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            if (h.getFieldValueByFieldNameBD("nd_comm1") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm2") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm3") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nd_comm3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm4") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_comm4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_adisc") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
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

    public DTOList EXCEL_STATISTIC_OF_PREMIUM() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	ax.taon as taon, ax.premium as premium,ax.brokcomm as Brook,ax.premium - ax.brokcomm as netPremium,   ax.paid as paid,ax.outstanding as outstanding,ax.paid+ax.outstanding as total");

        String sql = " select 	date_part('year',a.period_start) as taon,"
                + " sum ( case when  a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then i.premi_amount else 0 end ) as premium , "
                + " sum ( case when  a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then i.ricomm_amt else 0 end ) as brokcomm , "
                + " sum ( case when  a.effective_flag ='Y' and a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then i.claim_amount else 0 end ) as paid , "
                + " sum ( case when  a.effective_flag ='N' and a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then i.claim_amount else 0 end ) as outstanding "
                + " from ins_policy a inner join ins_pol_obj b on b.pol_id=a.pol_id"
                + " inner join ins_policy_types f on f.pol_type_id = a.pol_type_id"
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id";

        sqa.addQuery(" from ( " + sql);

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        sql = sqa.getSQL() + " group by date_part('year',a.period_start) 	"
                + " order by date_part('year',a.period_start) ) as ax ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_STATISTIC_OF_PREMIUM() throws Exception {

        int taon = Integer.parseInt(DateUtil.getYear(periodFrom));
        int taon1 = taon - 1;

        double gros = 0;
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

        //bikin header
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("STATISTIC OF PREMIUM & CLAIM");

        //bikin header
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("TREATY REINSURANCE PROGRAMME");

        //bikin header
        XSSFRow row2 = sheet.createRow(2);
        if (getStPolicyTypeGroupDesc() != null) {
            row2.createCell(0).setCellValue("CLASS : " + getStPolicyTypeGroupDesc().toUpperCase());
        }

        //bikin header
        XSSFRow row3 = sheet.createRow(3);
        if (getStFltTreatyType() != null) {
            row3.createCell(0).setCellValue("TYPE : " + getStFltTreatyType());
        }

        XSSFRow row4 = sheet.createRow(4);
        if (getAppDateFrom() != null) {
            row4.createCell(0).setCellValue("APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " s/d " + DateUtil.getDateStr(getAppDateTo()));
        }

        //bikin header
        XSSFRow row6 = sheet.createRow(5);
        if (getPeriodFrom() != null) {
            row6.createCell(0).setCellValue("PERIOD DATE : " + DateUtil.getDateStr(getPeriodFrom()) + " s/d " + DateUtil.getDateStr(getPeriodTo()));
        }

        //bikin header
        XSSFRow row5 = sheet.createRow(7);
        row5.createCell(0).setCellValue("U/Y");
        row5.createCell(1).setCellValue("PREMIUM");
        //row5.createCell(2).setCellValue("BROK/COMM");
        //row5.createCell(3).setCellValue("NET PREMIUM");
        row5.createCell(2).setCellValue("CLAIM PAID");
        row5.createCell(3).setCellValue("CLAIM OUTSTANDING");
        row5.createCell(4).setCellValue("CLAIM TOTAL");
        row5.createCell(5).setCellValue("LOST RATIO GROSS");
        row5.createCell(6).setCellValue("LOST RATIO NETT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);

            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("taon").doubleValue());

            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premium") != null ? h.getFieldValueByFieldNameBD("premium").doubleValue() : 0);

            premi_total = premi_total.add(h.getFieldValueByFieldNameBD("premium"));
            /*
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("Brook")!=null?h.getFieldValueByFieldNameBD("Brook").doubleValue():0);
            jumlah = jumlah.add(h.getFieldValueByFieldNameBD("Brook")!=null?h.getFieldValueByFieldNameBD("Brook"):BigDecimal.ZERO);

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("netPremium")!=null?h.getFieldValueByFieldNameBD("netPremium").doubleValue():0);
            claim_total = claim_total.add(h.getFieldValueByFieldNameBD("netPremium")!=null?h.getFieldValueByFieldNameBD("netPremium"):BigDecimal.ZERO);
             */
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("paid") != null ? h.getFieldValueByFieldNameBD("paid").doubleValue() : 0);
            jumlah_claim = jumlah_claim.add(h.getFieldValueByFieldNameBD("paid") != null ? h.getFieldValueByFieldNameBD("paid") : BigDecimal.ZERO);

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("outstanding") != null ? h.getFieldValueByFieldNameBD("outstanding").doubleValue() : 0);
            premi_total2 = premi_total2.add(h.getFieldValueByFieldNameBD("outstanding") != null ? h.getFieldValueByFieldNameBD("outstanding") : BigDecimal.ZERO);

            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("total") != null ? h.getFieldValueByFieldNameBD("total").doubleValue() : 0);
            jumlah2 = jumlah2.add(h.getFieldValueByFieldNameBD("total") != null ? h.getFieldValueByFieldNameBD("total") : BigDecimal.ZERO);


//             jumlah2 = jumlah2.add(h.getFieldValueByFieldNameBD("total"));

            double premium = h.getFieldValueByFieldNameBD("premium") != null ? h.getFieldValueByFieldNameBD("premium").doubleValue() : 0;
            BigDecimal grosx = BigDecimal.ZERO;


            if (h.getFieldValueByFieldNameBD("total").doubleValue() != 0) {
                double grosss = h.getFieldValueByFieldNameBD("total").doubleValue() / premium;
                grosx = new BigDecimal(grosss);  // h.getFieldValueByFieldNameBD("total").divide(new BigDecimal(premium)) ;
            }

            claim_total2 = claim_total2.add(grosx);
            row.createCell(5).setCellValue(grosx.doubleValue());


            double netPremium = h.getFieldValueByFieldNameBD("netPremium") != null ? h.getFieldValueByFieldNameBD("netPremium").doubleValue() : 0;
            BigDecimal net = BigDecimal.ZERO;
            if (h.getFieldValueByFieldNameBD("total").doubleValue() != 0) {
                double nets = h.getFieldValueByFieldNameBD("total").doubleValue() / netPremium;
                net = new BigDecimal(nets); // h.getFieldValueByFieldNameBD("total").divide(new BigDecimal(netPremium));
            }
            jumlah_claim2 = jumlah_claim2.add(net);
            row.createCell(6).setCellValue(net.doubleValue());

        }

        System.out.println(gros + " <<<<<<<<<<<<------------------------------ ");

        XSSFRow rows = sheet.createRow(list.size() + 9);
        rows.createCell(0).setCellValue("TOTAL ");
        rows.createCell(1).setCellValue(premi_total.doubleValue());
        /*
        rows.createCell(2).setCellValue(jumlah.doubleValue());
        rows.createCell(3).setCellValue(claim_total.doubleValue());
         */
        rows.createCell(2).setCellValue(jumlah_claim.doubleValue());

        rows.createCell(3).setCellValue(premi_total2.doubleValue());
        rows.createCell(4).setCellValue(jumlah2.doubleValue());

        rows.createCell(5).setCellValue(claim_total2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        rows.createCell(6).setCellValue(jumlah_claim2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_LOSS_PROFILE_UNDERWRITE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        int taon = Integer.parseInt(DateUtil.getYear(periodFrom));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;


        sqa.addSelect("a.lvl,a.ref4,a.ref5, "
                + " coalesce(sum(b.claim_no),0) as jumlah_claim, coalesce(sum(b.claim_total),0) as claim_total,      "
                + " coalesce(sum(b.claim_no2),0) as jumlah_claim2, coalesce(sum(b.claim_total2),0) as claim_total2,   "
                + " coalesce(sum(b.claim_no3),0) as jumlah_claim3, coalesce(sum(b.claim_total3),0) as claim_total3,  "
                + " coalesce(sum(b.claim_no4),0) as jumlah_claim4, coalesce(sum(b.claim_total4),0) as claim_total4, "
                + " coalesce(sum(b.claim_no5),0) as jumlah_claim5, coalesce(sum(b.claim_total5),0) as claim_total5");

        String sql = " left join (  select "
                + " round(getpremi(a.status = 'CLAIM ENDORSE',getpremi(a.claim_amount < 0,(a.claim_amount*a.ccy_rate_claim)*-1,a.claim_amount*a.ccy_rate_claim), a.claim_amount*a.ccy_rate_claim),0) as claim_amt,"
                + " a.pol_id,a.pol_no, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no2, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total2, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no3, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total3, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no4 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total4, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no5 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + "and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total5 "
                + " from ins_policy a inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + " where (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') ";

//       if (getStBranch()!=null) {
//            sql = sql + " and a.cc_code = ?";
//            sqa.addPar(stBranch);
//        }

        if (Tools.isYes(stIndex)) {
            sql = sql + " and a.effective_flag = 'Y'";
        }

        if (Tools.isNo(stIndex)) {
            sql = sql + " and a.effective_flag = 'N'";
        }

        if (getStPolicyTypeGroupID() != null) {
            sql = sql + " and a.ins_policy_type_grp_id = ?";
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            sql = sql + " and a.pol_type_id = ?";
            sqa.addPar(stPolicyTypeID);
        }

        if (getStPolCredit() != null) {
            sql = sql + " and a.pol_type_id  not in (59) ";
        }

        sqa.addQuery(" from band_level a " + sql + " group by a.pol_id,a.pol_no,a.claim_amount,a.ccy_rate_claim,a.claim_approved_date,a.period_start,a.status,a.claim_status,d.entity_id"
                + " ) as b on b.claim_amt between a.ref1 and a.ref2 ");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        sqa.addClause(" a.group_desc = 'CLAIM_LOSS_PROFILES' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LOSS_PROFILE_UNDERWRITE() throws Exception {


        int taon = Integer.parseInt(DateUtil.getYear(periodFrom));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;

        BigDecimal jumlah_claim = BigDecimal.ZERO;
        BigDecimal claim_total = BigDecimal.ZERO;

        BigDecimal jumlah_claim2 = BigDecimal.ZERO;
        BigDecimal claim_total2 = BigDecimal.ZERO;

        BigDecimal jumlah_claim3 = BigDecimal.ZERO;
        BigDecimal claim_total3 = BigDecimal.ZERO;

        BigDecimal jumlah_claim4 = BigDecimal.ZERO;
        BigDecimal claim_total4 = BigDecimal.ZERO;

        BigDecimal jumlah_claim5 = BigDecimal.ZERO;
        BigDecimal claim_total5 = BigDecimal.ZERO;

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");


        //bikin header
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStPolicyTypeGroupDesc().toUpperCase() + " INSURANCE");

        //bikin header
        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("CLAIM APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " s/d " + DateUtil.getDateStr(getAppDateTo()));

        //bikin header
        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("CLAIM PERIOD DATE : " + DateUtil.getDateStr(getPeriodFrom()) + " s/d " + DateUtil.getDateStr(getPeriodTo()));

        //bikin header
        XSSFRow row5 = sheet.createRow(4);
        row5.createCell(0).setCellValue("RANGE AMOUNT");
        row5.createCell(1).setCellValue("");
        row5.createCell(2).setCellValue(taon4);
        row5.createCell(3).setCellValue("");
        row5.createCell(4).setCellValue(taon3);
        row5.createCell(5).setCellValue("");
        row5.createCell(6).setCellValue(taon3);
        row5.createCell(7).setCellValue("");
        row5.createCell(8).setCellValue(taon1);
        row5.createCell(9).setCellValue("");
        row5.createCell(10).setCellValue(String.valueOf(DateUtil.getYear(appDateFrom)) + " ( s/d " + String.valueOf(DateUtil.getMonth(appDateTo)) + String.valueOf(DateUtil.getYear(appDateTo)) + ")");
        row5.createCell(11).setCellValue("");

        XSSFRow row8 = sheet.createRow(5);
        row8.createCell(0).setCellValue("OFF LOSS");
        row8.createCell(1).setCellValue("NO. OF LOSS");
        row8.createCell(2).setCellValue("IN AMOUNT");
        row8.createCell(3).setCellValue("NO. OF LOSS");
        row8.createCell(4).setCellValue("IN AMOUNT");
        row8.createCell(5).setCellValue("NO. OF LOSS");
        row8.createCell(6).setCellValue("IN AMOUNT");
        row8.createCell(7).setCellValue("NO. OF LOSS");
        row8.createCell(8).setCellValue("IN AMOUNT");
        row8.createCell(9).setCellValue("NO. OF LOSS");
        row8.createCell(10).setCellValue("IN AMOUNT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 6);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));

            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4").doubleValue() : 0);
            jumlah_claim4 = jumlah_claim4.add(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4") : BigDecimal.ZERO);

            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4").doubleValue() : 0);
            claim_total4 = claim_total4.add(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4") : BigDecimal.ZERO);

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3").doubleValue() : 0);
            jumlah_claim3 = jumlah_claim3.add(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3") : BigDecimal.ZERO);

            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3").doubleValue() : 0);
            claim_total3 = claim_total3.add(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3") : BigDecimal.ZERO);

            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2").doubleValue() : 0);
            jumlah_claim2 = jumlah_claim2.add(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2") : BigDecimal.ZERO);

            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2").doubleValue() : 0);
            claim_total2 = claim_total2.add(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2") : BigDecimal.ZERO);

            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim").doubleValue() : 0);
            jumlah_claim = jumlah_claim.add(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim") : BigDecimal.ZERO);

            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total").doubleValue() : 0);
            claim_total = claim_total.add(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total") : BigDecimal.ZERO);

            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5").doubleValue() : 0);
            jumlah_claim5 = jumlah_claim5.add(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5") : BigDecimal.ZERO);

            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5").doubleValue() : 0);
            claim_total5 = claim_total5.add(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5") : BigDecimal.ZERO);
        }

        XSSFRow rows = sheet.createRow(list.size() + 6);
        rows.createCell(0).setCellValue("TOTAL ");
        rows.createCell(1).setCellValue(jumlah_claim4.doubleValue());
        rows.createCell(2).setCellValue(claim_total4.doubleValue());
        rows.createCell(3).setCellValue(jumlah_claim3.doubleValue());
        rows.createCell(4).setCellValue(claim_total3.doubleValue());
        rows.createCell(5).setCellValue(jumlah_claim2.doubleValue());
        rows.createCell(6).setCellValue(claim_total2.doubleValue());
        rows.createCell(7).setCellValue(jumlah_claim.doubleValue());
        rows.createCell(8).setCellValue(claim_total.doubleValue());
        rows.createCell(9).setCellValue(jumlah_claim5.doubleValue());
        rows.createCell(10).setCellValue(claim_total5.doubleValue());

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    /*
    public DTOList EXCEL_RISK_LOSS_PROFILE_UNDERWRITE() throws Exception {
    final SQLAssembler sqa = new SQLAssembler();

    int taon =  Integer.parseInt(DateUtil.getYear(appDateTo));
    int taon1 = taon - 1;
    int taon2 = taon - 2;
    int taon3 = taon - 3;
    int taon4 = taon - 4;

    sqa.addSelect("a.lvl,a.ref4,a.ref5, " +
    " coalesce(sum(b.pol_no)) as jumlah,coalesce(sum(b.premi_total),0) as premi_total ," +
    " coalesce(sum(b.claim_no),0) as jumlah_claim, coalesce(sum(b.claim_total),0) as claim_total,  " +
    " coalesce(sum(b.pol_no2)) as jumlah2,coalesce(sum(b.premi_total2),0) as premi_total2 ,   " +
    " coalesce(sum(b.claim_no2),0) as jumlah_claim2, coalesce(sum(b.claim_total2),0) as claim_total2," +
    " coalesce(sum(b.pol_no3)) as jumlah3,coalesce(sum(b.premi_total3),0) as premi_total3 ,    " +
    " coalesce(sum(b.claim_no3),0) as jumlah_claim3, coalesce(sum(b.claim_total3),0) as claim_total3,  " +
    " coalesce(sum(b.pol_no4)) as jumlah4,coalesce(sum(b.premi_total4),0) as premi_total4 ,    " +
    " coalesce(sum(b.claim_no4),0) as jumlah_claim4, coalesce(sum(b.claim_total4),0) as claim_total4 ," +
    " coalesce(sum(b.pol_no5)) as jumlah5,coalesce(sum(b.premi_total5),0) as premi_total5 ,    " +
    " coalesce(sum(b.claim_no5),0) as jumlah_claim5, coalesce(sum(b.claim_total5),0) as claim_total5  "
    );

    String sql = " left join (  select " +
    " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate_claim), a.insured_amount*a.ccy_rate)) as insured_amount," +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then 1 else 0 end  as pol_no ," +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon4+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total," +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then 1 else 0 end  as pol_no2 ,  " +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total2 , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no2 , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon3+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total2, " +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then 1 else 0 end  as pol_no3 , " +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total3 , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no3 ," +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon2+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total3, " +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then 1 else 0 end  as pol_no4 ,  " +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total4 ," +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no4 , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateFrom)+"-"+DateUtil.getDays(appDateFrom));
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(appDateTo)+"-"+DateUtil.getDays(appDateTo));
    //sqa.addPar(appDateFrom);
    //sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodFrom)+"-"+DateUtil.getDays(periodFrom));
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(taon1+"-"+DateUtil.getMonth2Digit(periodTo)+"-"+DateUtil.getDays(periodTo));
    }
    sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total4," +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(appDateFrom);
    sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(periodFrom);
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(periodTo);
    }
    sql = sql + " then 1 else 0 end  as pol_no5 ," +
    " case when a.status in ('POLICY','ENDORSE','RENEWAL') " +
    " and date_trunc('day',a.policy_date) >= ? " +
    " and date_trunc('day',a.policy_date) <= ? " ;
    sqa.addPar(appDateFrom);
    sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(periodFrom);
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(periodTo);
    }
    sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total5 ," +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(appDateFrom);
    sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(periodFrom);
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(periodTo);
    }
    sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no5 , " +
    " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' " +
    " and date_trunc('day',a.approved_date) >= ? " +
    " and date_trunc('day',a.approved_date) <= ? " ;
    sqa.addPar(appDateFrom);
    sqa.addPar(appDateTo);
    if (getPeriodFrom()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) >= ? ";
    sqa.addPar(periodFrom);
    }
    if (getPeriodTo()!=null) {
    sql = sql + "and date_trunc('day',a.period_start) <= ? ";
    sqa.addPar(periodTo);
    }
    sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total5 ";


    sql = sql + " from ins_policy a inner join ins_pol_coins d on d.policy_id = a.pol_id " +
    " where (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') ";

    if (Tools.isYes(stIndex)) {
    sql = sql + " and a.effective_flag = 'Y'";
    }

    if (Tools.isNo(stIndex)) {
    sql = sql + " and a.effective_flag = 'N'";
    }

    if (getStPolicyTypeGroupID()!=null) {
    sql = sql + " and a.ins_policy_type_grp_id = ?";
    sqa.addPar(stPolicyTypeGroupID);
    }

    sqa.addQuery(" from band_level a "+ sql + " group by a.policy_date,a.claim_approved_date,a.period_start,a.status , a.claim_status,d.entity_id" +
    " ) as b on b.insured_amount between a.ref1 and a.ref2 "
    );

    if (stPolicyTypeGroupID!=null) {
    sqa.addClause("a.pol_type_grp_id = ?");
    sqa.addPar(stPolicyTypeGroupID);
    }

    sqa.addClause(" a.group_desc = 'CLAIM_RISK_LOSS' ");

    sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

    final DTOList l = ListUtil.getDTOListFromQuery(
    sql,
    sqa.getPar(),
    HashDTO.class
    );

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
    }
     */
    public DTOList EXCEL_RISK_LOSS_PROFILE_UNDERWRITE() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        int taon = Integer.parseInt(DateUtil.getYear(appDateTo));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;

        sqa.addSelect("a.lvl,a.ref4,a.ref5, "
                + " coalesce(sum(b.pol_no)) as jumlah,coalesce(sum(b.premi_total),0) as premi_total ,"
                + " coalesce(sum(b.claim_no),0) as jumlah_claim, coalesce(sum(b.claim_total),0) as claim_total,  "
                + " coalesce(sum(b.pol_no2)) as jumlah2,coalesce(sum(b.premi_total2),0) as premi_total2 ,   "
                + " coalesce(sum(b.claim_no2),0) as jumlah_claim2, coalesce(sum(b.claim_total2),0) as claim_total2,"
                + " coalesce(sum(b.pol_no3)) as jumlah3,coalesce(sum(b.premi_total3),0) as premi_total3 ,    "
                + " coalesce(sum(b.claim_no3),0) as jumlah_claim3, coalesce(sum(b.claim_total3),0) as claim_total3,  "
                + " coalesce(sum(b.pol_no4)) as jumlah4,coalesce(sum(b.premi_total4),0) as premi_total4 ,    "
                + " coalesce(sum(b.claim_no4),0) as jumlah_claim4, coalesce(sum(b.claim_total4),0) as claim_total4 ,"
                + " coalesce(sum(b.pol_no5)) as jumlah5,coalesce(sum(b.premi_total5),0) as premi_total5 ,    "
                + " coalesce(sum(b.claim_no5),0) as jumlah_claim5, coalesce(sum(b.claim_total5),0) as claim_total5  ");

        String sql = " left join (  select "
                + " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate), a.insured_amount*a.ccy_rate)) as insured_amount,"
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end  as pol_no ,"
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total,"
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end  as pol_no2 ,  "
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total2 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no2 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total2, "
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end  as pol_no3 , "
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total3 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no3 ,"
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total3, "
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end  as pol_no4 ,  "
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total4 ,"
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no4 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total4,"
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then 1 else 0 end  as pol_no5 ,"
                + " case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) else 0 end as premi_total5 ,"
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " and d.entity_id = 1 then 1 else 0 end as claim_no5 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " and d.entity_id = 1 then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total5 ";


        sql = sql + " from ins_policy a inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + " where (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') ";

        if (Tools.isYes(stIndex)) {
            sql = sql + " and a.effective_flag = 'Y'";
        }

        if (Tools.isNo(stIndex)) {
            sql = sql + " and a.effective_flag = 'N'";
        }

        if (getStPolicyTypeGroupID() != null) {
            sql = sql + " and a.ins_policy_type_grp_id = ?";
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (getStPolicyTypeID() != null) {
            sql = sql + " and a.pol_type_id = ?";
            sqa.addPar(stPolicyTypeID);
        }

        if (getStPolCredit() != null) {
            sql = sql + " and a.pol_type_id  not in (59) ";
        }

        sqa.addQuery(" from band_level a " + sql + " group by a.policy_date,a.claim_approved_date,a.period_start,a.status , a.claim_status,d.entity_id"
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        sqa.addClause(" a.group_desc = 'CLAIM_RISK_LOSS' ");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_RISK_LOSS_PROFILE_UNDERWRITE() throws Exception {

        int taon = Integer.parseInt(DateUtil.getYear(appDateTo));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;

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


        BigDecimal premi_total4 = BigDecimal.ZERO;
        BigDecimal jumlah4 = BigDecimal.ZERO;
        BigDecimal claim_total4 = BigDecimal.ZERO;
        BigDecimal jumlah_claim4 = BigDecimal.ZERO;


        BigDecimal premi_total5 = BigDecimal.ZERO;
        BigDecimal jumlah5 = BigDecimal.ZERO;
        BigDecimal claim_total5 = BigDecimal.ZERO;
        BigDecimal jumlah_claim5 = BigDecimal.ZERO;



        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStPolicyTypeGroupDesc().toUpperCase() + " INSURANCE");

        //bikin header
        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " s/d " + DateUtil.getDateStr(getAppDateTo()));

        //bikin header
        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("PERIOD DATE : " + DateUtil.getDateStr(getPeriodFrom()) + " s/d " + DateUtil.getDateStr(getPeriodTo()));

        //bikin header
        XSSFRow row5 = sheet.createRow(4);
        row5.createCell(0).setCellValue("SUM INSURED");
        row5.createCell(1).setCellValue(taon4);
        row5.createCell(2).setCellValue("");
        row5.createCell(3).setCellValue("");
        row5.createCell(4).setCellValue("");
        row5.createCell(5).setCellValue(taon3);
        row5.createCell(6).setCellValue("");
        row5.createCell(7).setCellValue("");
        row5.createCell(8).setCellValue("");
        row5.createCell(9).setCellValue(taon2);
        row5.createCell(10).setCellValue("");
        row5.createCell(11).setCellValue("");
        row5.createCell(12).setCellValue("");
        row5.createCell(13).setCellValue(taon1);
        row5.createCell(14).setCellValue("");
        row5.createCell(15).setCellValue("");
        row5.createCell(16).setCellValue("");
        row5.createCell(17).setCellValue(String.valueOf(DateUtil.getYear(appDateFrom)));
        row5.createCell(18).setCellValue("");
        row5.createCell(19).setCellValue("");
        row5.createCell(20).setCellValue("");

        XSSFRow row8 = sheet.createRow(5);
        row8.createCell(0).setCellValue("RISK BASIS");
        row8.createCell(1).setCellValue("PREMIUM");
        row8.createCell(2).setCellValue("");
        row8.createCell(3).setCellValue("INCURRED CLAIM");
        row8.createCell(4).setCellValue("");
        row8.createCell(5).setCellValue("PREMIUM");
        row8.createCell(6).setCellValue("");
        row8.createCell(7).setCellValue("INCURRED CLAIM");
        row8.createCell(8).setCellValue("");
        row8.createCell(9).setCellValue("PREMIUM");
        row8.createCell(10).setCellValue("");
        row8.createCell(11).setCellValue("INCURRED CLAIM");
        row8.createCell(12).setCellValue("");
        row8.createCell(13).setCellValue("PREMIUM");
        row8.createCell(14).setCellValue("");
        row8.createCell(15).setCellValue("INCURRED CLAIM");
        row8.createCell(16).setCellValue("");
        row8.createCell(17).setCellValue("PREMIUM");
        row8.createCell(18).setCellValue("");
        row8.createCell(19).setCellValue("INCURRED CLAIM");
        row8.createCell(20).setCellValue("");

        //bikin header
        XSSFRow row6 = sheet.createRow(6);
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
        row6.createCell(13).setCellValue("IN AMOUNT");
        row6.createCell(14).setCellValue("NO");
        row6.createCell(15).setCellValue("IN AMOUNT");
        row6.createCell(16).setCellValue("NO");
        row6.createCell(17).setCellValue("IN AMOUNT");
        row6.createCell(18).setCellValue("NO");
        row6.createCell(19).setCellValue("IN AMOUNT");
        row6.createCell(20).setCellValue("NO");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 7);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("premi_total") != null ? h.getFieldValueByFieldNameBD("premi_total").doubleValue() : 0);
            premi_total = premi_total.add(h.getFieldValueByFieldNameBD("premi_total") != null ? h.getFieldValueByFieldNameBD("premi_total") : BigDecimal.ZERO);

            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah") != null ? h.getFieldValueByFieldNameBD("jumlah").doubleValue() : 0);
            jumlah = jumlah.add(h.getFieldValueByFieldNameBD("jumlah") != null ? h.getFieldValueByFieldNameBD("jumlah") : BigDecimal.ZERO);

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total").doubleValue() : 0);
            claim_total = claim_total.add(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total") : BigDecimal.ZERO);

            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim").doubleValue() : 0);
            jumlah_claim = jumlah_claim.add(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim") : BigDecimal.ZERO);

            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("premi_total2") != null ? h.getFieldValueByFieldNameBD("premi_total2").doubleValue() : 0);
            premi_total2 = premi_total2.add(h.getFieldValueByFieldNameBD("premi_total2") != null ? h.getFieldValueByFieldNameBD("premi_total2") : BigDecimal.ZERO);

            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("jumlah2") != null ? h.getFieldValueByFieldNameBD("jumlah2").doubleValue() : 0);
            jumlah2 = jumlah2.add(h.getFieldValueByFieldNameBD("jumlah2") != null ? h.getFieldValueByFieldNameBD("jumlah2") : BigDecimal.ZERO);

            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2").doubleValue() : 0);
            claim_total2 = claim_total2.add(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2") : BigDecimal.ZERO);

            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2").doubleValue() : 0);
            jumlah_claim2 = jumlah_claim2.add(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2") : BigDecimal.ZERO);

            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("premi_total3") != null ? h.getFieldValueByFieldNameBD("premi_total3").doubleValue() : 0);
            premi_total3 = premi_total3.add(h.getFieldValueByFieldNameBD("premi_total3") != null ? h.getFieldValueByFieldNameBD("premi_total3") : BigDecimal.ZERO);

            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("jumlah3") != null ? h.getFieldValueByFieldNameBD("jumlah3").doubleValue() : 0);
            jumlah3 = jumlah3.add(h.getFieldValueByFieldNameBD("jumlah3") != null ? h.getFieldValueByFieldNameBD("jumlah3") : BigDecimal.ZERO);

            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3").doubleValue() : 0);
            claim_total3 = claim_total3.add(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3") : BigDecimal.ZERO);

            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3").doubleValue() : 0);
            jumlah_claim3 = jumlah_claim3.add(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3") : BigDecimal.ZERO);

            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premi_total4") != null ? h.getFieldValueByFieldNameBD("premi_total4").doubleValue() : 0);
            premi_total4 = premi_total4.add(h.getFieldValueByFieldNameBD("premi_total4") != null ? h.getFieldValueByFieldNameBD("premi_total4") : BigDecimal.ZERO);

            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("jumlah4") != null ? h.getFieldValueByFieldNameBD("jumlah4").doubleValue() : 0);
            jumlah4 = jumlah4.add(h.getFieldValueByFieldNameBD("jumlah4") != null ? h.getFieldValueByFieldNameBD("jumlah4") : BigDecimal.ZERO);

            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4").doubleValue() : 0);
            claim_total4 = claim_total4.add(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4") : BigDecimal.ZERO);

            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4").doubleValue() : 0);
            jumlah_claim4 = jumlah_claim4.add(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4") : BigDecimal.ZERO);

            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("premi_total5") != null ? h.getFieldValueByFieldNameBD("premi_total5").doubleValue() : 0);
            premi_total5 = premi_total5.add(h.getFieldValueByFieldNameBD("premi_total5") != null ? h.getFieldValueByFieldNameBD("premi_total5") : BigDecimal.ZERO);

            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("jumlah5") != null ? h.getFieldValueByFieldNameBD("jumlah5").doubleValue() : 0);
            jumlah5 = jumlah5.add(h.getFieldValueByFieldNameBD("jumlah5") != null ? h.getFieldValueByFieldNameBD("jumlah5") : BigDecimal.ZERO);

            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5").doubleValue() : 0);
            claim_total5 = claim_total5.add(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5") : BigDecimal.ZERO);

            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5").doubleValue() : 0);
            jumlah_claim5 = jumlah_claim5.add(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5") : BigDecimal.ZERO);
        }

        XSSFRow rows = sheet.createRow(list.size() + 7);
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
        rows.createCell(13).setCellValue(premi_total4.doubleValue());
        rows.createCell(14).setCellValue(jumlah4.doubleValue());
        rows.createCell(15).setCellValue(claim_total4.doubleValue());
        rows.createCell(16).setCellValue(jumlah_claim4.doubleValue());
        rows.createCell(17).setCellValue(premi_total5.doubleValue());
        rows.createCell(18).setCellValue(jumlah5.doubleValue());
        rows.createCell(19).setCellValue(claim_total5.doubleValue());
        rows.createCell(20).setCellValue(jumlah_claim5.doubleValue());

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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
    /*
    public DTOList EXCEL_CLAIM_RECAP_ALL() throws Exception {
    final SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("	a.pol_type_id,a.cc_code,a.claim_amount as hutang_klaim, " +
    "(case when a.dla_no is not null then 1 else 0 end) as jumlah,a.dla_no " );

    sqa.addQuery(" from ins_policy a ");

    sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
    sqa.addClause(" a.claim_status IN ('DLA')");
    sqa.addClause(" a.effective_flag='Y'");

    if (DLADateFrom!=null) {
    sqa.addClause("date_trunc('day',a.dla_date) >= ?");
    sqa.addPar(DLADateFrom);
    }

    if (DLADateTo!=null) {
    sqa.addClause("date_trunc('day',a.dla_date) <= ?");
    sqa.addPar(DLADateTo);
    }

    if (claimDateFrom!=null) {
    sqa.addClause("date_trunc('day',a.claim_date) >= ?");
    sqa.addPar(claimDateFrom);
    }

    if (claimDateTo!=null) {
    sqa.addClause("date_trunc('day',a.claim_date) <= ?");
    sqa.addPar(claimDateTo);
    }

    if(appDateFrom!=null) {
    sqa.addClause("date_trunc('day',a.approved_date) >= ?");
    sqa.addPar(appDateFrom);
    }

    if(appDateTo!=null) {
    sqa.addClause("date_trunc('day',a.approved_date) <= ?");
    sqa.addPar(appDateTo);
    }

    final String sql = "select b.pol_type_id,b.description," +
    "sum(getkoas(cc_code='10',jumlah)) as kasus10, sum(getkoas(cc_code='10',hutang_klaim)) as Aceh, " +
    "sum(getkoas(cc_code='11',jumlah)) as kasus11, sum(getkoas(cc_code='11',hutang_klaim)) as Medan, " +
    "sum(getkoas(cc_code='12',jumlah)) as kasus12, sum(getkoas(cc_code='12',hutang_klaim)) as Padang, " +
    "sum(getkoas(cc_code='13',jumlah)) as kasus13, sum(getkoas(cc_code='13',hutang_klaim)) as Riau, " +
    "sum(getkoas(cc_code='14',jumlah)) as kasus14, sum(getkoas(cc_code='14',hutang_klaim)) as Jambi, " +
    "sum(getkoas(cc_code='15',jumlah)) as kasus15, sum(getkoas(cc_code='15',hutang_klaim)) as Palembang, " +
    "sum(getkoas(cc_code='16',jumlah)) as kasus16, sum(getkoas(cc_code='16',hutang_klaim)) as Bengkulu, " +
    "sum(getkoas(cc_code='17',jumlah)) as kasus17, sum(getkoas(cc_code='17',hutang_klaim)) as Lampung, " +
    "sum(getkoas(cc_code='18',jumlah)) as kasus18, sum(getkoas(cc_code='18',hutang_klaim)) as BaBel, " +
    "sum(getkoas(cc_code='20',jumlah)) as kasus20, sum(getkoas(cc_code='20',hutang_klaim)) as Jakarta, " +
    "sum(getkoas(cc_code='21',jumlah)) as kasus21, sum(getkoas(cc_code='21',hutang_klaim)) as Bandung, " +
    "sum(getkoas(cc_code='22',jumlah)) as kasus22, sum(getkoas(cc_code='22',hutang_klaim)) as Semarang, " +
    "sum(getkoas(cc_code='23',jumlah)) as kasus23, sum(getkoas(cc_code='23',hutang_klaim)) as Yogyakarta, " +
    "sum(getkoas(cc_code='24',jumlah)) as kasus24, sum(getkoas(cc_code='24',hutang_klaim)) as Surabaya, " +
    "sum(getkoas(cc_code='25',jumlah)) as kasus25, sum(getkoas(cc_code='25',hutang_klaim)) as Serang , " +
    "sum(getkoas(cc_code='30',jumlah)) as kasus30, sum(getkoas(cc_code='30',hutang_klaim)) as Pontianak, " +
    "sum(getkoas(cc_code='31',jumlah)) as kasus31, sum(getkoas(cc_code='31',hutang_klaim)) as Palangkaraya, " +
    "sum(getkoas(cc_code='32',jumlah)) as kasus32, sum(getkoas(cc_code='32',hutang_klaim)) as Banjarmasin, " +
    "sum(getkoas(cc_code='33',jumlah)) as kasus33, sum(getkoas(cc_code='33',hutang_klaim)) as Samarinda, " +
    "sum(getkoas(cc_code='40',jumlah)) as kasus40, sum(getkoas(cc_code='40',hutang_klaim)) as Manado, " +
    "sum(getkoas(cc_code='41',jumlah)) as kasus41, sum(getkoas(cc_code='41',hutang_klaim)) as Palu, " +
    "sum(getkoas(cc_code='42',jumlah)) as kasus42, sum(getkoas(cc_code='42',hutang_klaim)) as Kendari, " +
    "sum(getkoas(cc_code='43',jumlah)) as kasus43, sum(getkoas(cc_code='43',hutang_klaim)) as Makasar, " +
    "sum(getkoas(cc_code='44',jumlah)) as kasus44, sum(getkoas(cc_code='44',hutang_klaim)) as Mamuju, " +
    "sum(getkoas(cc_code='45',jumlah)) as kasus45, sum(getkoas(cc_code='45',hutang_klaim)) as Gorontalo, " +
    "sum(getkoas(cc_code='50',jumlah)) as kasus50, sum(getkoas(cc_code='50',hutang_klaim)) as Denpasar, " +
    "sum(getkoas(cc_code='51',jumlah)) as kasus51, sum(getkoas(cc_code='51',hutang_klaim)) as Mataram, " +
    "sum(getkoas(cc_code='52',jumlah)) as kasus52, sum(getkoas(cc_code='52',hutang_klaim)) as Kupang, " +
    "sum(getkoas(cc_code='60',jumlah)) as kasus60, sum(getkoas(cc_code='60',hutang_klaim)) as Ambon, " +
    "sum(getkoas(cc_code='61',jumlah)) as kasus61, sum(getkoas(cc_code='61',hutang_klaim)) as Ternate, " +
    "sum(getkoas(cc_code='70',jumlah)) as kasus70, sum(getkoas(cc_code='70',hutang_klaim)) as Papua, " +
    "sum(coalesce(jumlah,0)) as jumlah, sum(coalesce(hutang_klaim,0)) as premi_total_adisc "+
    "from ins_policy_types b left join (  "+
    sqa.getSQL()+" ) a on a.pol_type_id = b.pol_type_id  " +
    "group by b.pol_type_id,b.description order by b.pol_type_id ";

    final DTOList l = ListUtil.getDTOListFromQuery(
    sql,
    sqa.getPar(),
    HashDTO.class
    );

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
    }
     */

    public DTOList EXCEL_CLAIM_RECAP_ALL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.pol_type_id,b.description,"
                + "sum(getkoas(cc_code='00',jumlah)) as kasus_00,sum(getkoas(cc_code='00',jumlah_total)) as kasus_total_00,sum(getkoas(cc_code='00',klaim)) as klaim_00,sum(getkoas(cc_code='00',klaim_total)) as klaim_total_00,sum(getkoas(cc_code='00',preto)) as preto_00,sum(getkoas(cc_code='00',tax_comm)) as tax_comm_00,sum(getkoas(cc_code='00',preto_total)) as preto_total_00,sum(getkoas(cc_code='00',tax_comm_total)) as tax_comm_total_00,"
                + "sum(getkoas(cc_code='01',jumlah)) as kasus_01,sum(getkoas(cc_code='01',jumlah_total)) as kasus_total_01,sum(getkoas(cc_code='01',klaim)) as klaim_01,sum(getkoas(cc_code='01',klaim_total)) as klaim_total_01,sum(getkoas(cc_code='01',preto)) as preto_01,sum(getkoas(cc_code='01',tax_comm)) as tax_comm_01,sum(getkoas(cc_code='01',preto_total)) as preto_total_01,sum(getkoas(cc_code='01',tax_comm_total)) as tax_comm_total_01,"
                + "sum(getkoas(cc_code='10',jumlah)) as kasus_10,sum(getkoas(cc_code='10',jumlah_total)) as kasus_total_10,sum(getkoas(cc_code='10',klaim)) as klaim_10,sum(getkoas(cc_code='10',klaim_total)) as klaim_total_10,sum(getkoas(cc_code='10',preto)) as preto_10,sum(getkoas(cc_code='10',tax_comm)) as tax_comm_10,sum(getkoas(cc_code='10',preto_total)) as preto_total_10,sum(getkoas(cc_code='10',tax_comm_total)) as tax_comm_total_10,"
                + "sum(getkoas(cc_code='11',jumlah)) as kasus_11,sum(getkoas(cc_code='11',jumlah_total)) as kasus_total_11,sum(getkoas(cc_code='11',klaim)) as klaim_11,sum(getkoas(cc_code='11',klaim_total)) as klaim_total_11,sum(getkoas(cc_code='11',preto)) as preto_11,sum(getkoas(cc_code='11',tax_comm)) as tax_comm_11,sum(getkoas(cc_code='11',preto_total)) as preto_total_11,sum(getkoas(cc_code='11',tax_comm_total)) as tax_comm_total_11,"
                + "sum(getkoas(cc_code='12',jumlah)) as kasus_12,sum(getkoas(cc_code='12',jumlah_total)) as kasus_total_12,sum(getkoas(cc_code='12',klaim)) as klaim_12,sum(getkoas(cc_code='12',klaim_total)) as klaim_total_12,sum(getkoas(cc_code='12',preto)) as preto_12,sum(getkoas(cc_code='12',tax_comm)) as tax_comm_12,sum(getkoas(cc_code='12',preto_total)) as preto_total_12,sum(getkoas(cc_code='12',tax_comm_total)) as tax_comm_total_12,"
                + "sum(getkoas(cc_code='13',jumlah)) as kasus_13,sum(getkoas(cc_code='13',jumlah_total)) as kasus_total_13,sum(getkoas(cc_code='13',klaim)) as klaim_13,sum(getkoas(cc_code='13',klaim_total)) as klaim_total_13,sum(getkoas(cc_code='13',preto)) as preto_13,sum(getkoas(cc_code='13',tax_comm)) as tax_comm_13,sum(getkoas(cc_code='13',preto_total)) as preto_total_13,sum(getkoas(cc_code='13',tax_comm_total)) as tax_comm_total_13,"
                + "sum(getkoas(cc_code='14',jumlah)) as kasus_14,sum(getkoas(cc_code='14',jumlah_total)) as kasus_total_14,sum(getkoas(cc_code='14',klaim)) as klaim_14,sum(getkoas(cc_code='14',klaim_total)) as klaim_total_14,sum(getkoas(cc_code='14',preto)) as preto_14,sum(getkoas(cc_code='14',tax_comm)) as tax_comm_14,sum(getkoas(cc_code='14',preto_total)) as preto_total_14,sum(getkoas(cc_code='14',tax_comm_total)) as tax_comm_total_14,"
                + "sum(getkoas(cc_code='15',jumlah)) as kasus_15,sum(getkoas(cc_code='15',jumlah_total)) as kasus_total_15,sum(getkoas(cc_code='15',klaim)) as klaim_15,sum(getkoas(cc_code='15',klaim_total)) as klaim_total_15,sum(getkoas(cc_code='15',preto)) as preto_15,sum(getkoas(cc_code='15',tax_comm)) as tax_comm_15,sum(getkoas(cc_code='15',preto_total)) as preto_total_15,sum(getkoas(cc_code='15',tax_comm_total)) as tax_comm_total_15,"
                + "sum(getkoas(cc_code='16',jumlah)) as kasus_16,sum(getkoas(cc_code='16',jumlah_total)) as kasus_total_16,sum(getkoas(cc_code='16',klaim)) as klaim_16,sum(getkoas(cc_code='16',klaim_total)) as klaim_total_16,sum(getkoas(cc_code='16',preto)) as preto_16,sum(getkoas(cc_code='16',tax_comm)) as tax_comm_16,sum(getkoas(cc_code='16',preto_total)) as preto_total_16,sum(getkoas(cc_code='16',tax_comm_total)) as tax_comm_total_16,"
                + "sum(getkoas(cc_code='17',jumlah)) as kasus_17,sum(getkoas(cc_code='17',jumlah_total)) as kasus_total_17,sum(getkoas(cc_code='17',klaim)) as klaim_17,sum(getkoas(cc_code='17',klaim_total)) as klaim_total_17,sum(getkoas(cc_code='17',preto)) as preto_17,sum(getkoas(cc_code='17',tax_comm)) as tax_comm_17,sum(getkoas(cc_code='17',preto_total)) as preto_total_17,sum(getkoas(cc_code='17',tax_comm_total)) as tax_comm_total_17,"
                + "sum(getkoas(cc_code='18',jumlah)) as kasus_18,sum(getkoas(cc_code='18',jumlah_total)) as kasus_total_18,sum(getkoas(cc_code='18',klaim)) as klaim_18,sum(getkoas(cc_code='18',klaim_total)) as klaim_total_18,sum(getkoas(cc_code='18',preto)) as preto_18,sum(getkoas(cc_code='18',tax_comm)) as tax_comm_18,sum(getkoas(cc_code='18',preto_total)) as preto_total_18,sum(getkoas(cc_code='18',tax_comm_total)) as tax_comm_total_18,"
                + "sum(getkoas(cc_code='19',jumlah)) as kasus_19,sum(getkoas(cc_code='19',jumlah_total)) as kasus_total_19,sum(getkoas(cc_code='19',klaim)) as klaim_19,sum(getkoas(cc_code='19',klaim_total)) as klaim_total_19,sum(getkoas(cc_code='19',preto)) as preto_19,sum(getkoas(cc_code='19',tax_comm)) as tax_comm_19,sum(getkoas(cc_code='19',preto_total)) as preto_total_19,sum(getkoas(cc_code='19',tax_comm_total)) as tax_comm_total_19,"
                + "sum(getkoas(cc_code='20',jumlah)) as kasus_20,sum(getkoas(cc_code='20',jumlah_total)) as kasus_total_20,sum(getkoas(cc_code='20',klaim)) as klaim_20,sum(getkoas(cc_code='20',klaim_total)) as klaim_total_20,sum(getkoas(cc_code='20',preto)) as preto_20,sum(getkoas(cc_code='20',tax_comm)) as tax_comm_20,sum(getkoas(cc_code='20',preto_total)) as preto_total_20,sum(getkoas(cc_code='20',tax_comm_total)) as tax_comm_total_20,"
                + "sum(getkoas(cc_code='21',jumlah)) as kasus_21,sum(getkoas(cc_code='21',jumlah_total)) as kasus_total_21,sum(getkoas(cc_code='21',klaim)) as klaim_21,sum(getkoas(cc_code='21',klaim_total)) as klaim_total_21,sum(getkoas(cc_code='21',preto)) as preto_21,sum(getkoas(cc_code='21',tax_comm)) as tax_comm_21,sum(getkoas(cc_code='21',preto_total)) as preto_total_21,sum(getkoas(cc_code='21',tax_comm_total)) as tax_comm_total_21,"
                + "sum(getkoas(cc_code='22',jumlah)) as kasus_22,sum(getkoas(cc_code='22',jumlah_total)) as kasus_total_22,sum(getkoas(cc_code='22',klaim)) as klaim_22,sum(getkoas(cc_code='22',klaim_total)) as klaim_total_22,sum(getkoas(cc_code='22',preto)) as preto_22,sum(getkoas(cc_code='22',tax_comm)) as tax_comm_22,sum(getkoas(cc_code='22',preto_total)) as preto_total_22,sum(getkoas(cc_code='22',tax_comm_total)) as tax_comm_total_22,"
                + "sum(getkoas(cc_code='23',jumlah)) as kasus_23,sum(getkoas(cc_code='23',jumlah_total)) as kasus_total_23,sum(getkoas(cc_code='23',klaim)) as klaim_23,sum(getkoas(cc_code='23',klaim_total)) as klaim_total_23,sum(getkoas(cc_code='23',preto)) as preto_23,sum(getkoas(cc_code='23',tax_comm)) as tax_comm_23,sum(getkoas(cc_code='23',preto_total)) as preto_total_23,sum(getkoas(cc_code='23',tax_comm_total)) as tax_comm_total_23,"
                + "sum(getkoas(cc_code='24',jumlah)) as kasus_24,sum(getkoas(cc_code='24',jumlah_total)) as kasus_total_24,sum(getkoas(cc_code='24',klaim)) as klaim_24,sum(getkoas(cc_code='24',klaim_total)) as klaim_total_24,sum(getkoas(cc_code='24',preto)) as preto_24,sum(getkoas(cc_code='24',tax_comm)) as tax_comm_24,sum(getkoas(cc_code='24',preto_total)) as preto_total_24,sum(getkoas(cc_code='24',tax_comm_total)) as tax_comm_total_24,"
                + "sum(getkoas(cc_code='25',jumlah)) as kasus_25,sum(getkoas(cc_code='25',jumlah_total)) as kasus_total_25,sum(getkoas(cc_code='25',klaim)) as klaim_25,sum(getkoas(cc_code='25',klaim_total)) as klaim_total_25,sum(getkoas(cc_code='25',preto)) as preto_25,sum(getkoas(cc_code='25',tax_comm)) as tax_comm_25,sum(getkoas(cc_code='25',preto_total)) as preto_total_25,sum(getkoas(cc_code='25',tax_comm_total)) as tax_comm_total_25,"
                + "sum(getkoas(cc_code='30',jumlah)) as kasus_30,sum(getkoas(cc_code='30',jumlah_total)) as kasus_total_30,sum(getkoas(cc_code='30',klaim)) as klaim_30,sum(getkoas(cc_code='30',klaim_total)) as klaim_total_30,sum(getkoas(cc_code='30',preto)) as preto_30,sum(getkoas(cc_code='30',tax_comm)) as tax_comm_30,sum(getkoas(cc_code='30',preto_total)) as preto_total_30,sum(getkoas(cc_code='30',tax_comm_total)) as tax_comm_total_30,"
                + "sum(getkoas(cc_code='31',jumlah)) as kasus_31,sum(getkoas(cc_code='31',jumlah_total)) as kasus_total_31,sum(getkoas(cc_code='31',klaim)) as klaim_31,sum(getkoas(cc_code='31',klaim_total)) as klaim_total_31,sum(getkoas(cc_code='31',preto)) as preto_31,sum(getkoas(cc_code='31',tax_comm)) as tax_comm_31,sum(getkoas(cc_code='31',preto_total)) as preto_total_31,sum(getkoas(cc_code='31',tax_comm_total)) as tax_comm_total_31,"
                + "sum(getkoas(cc_code='32',jumlah)) as kasus_32,sum(getkoas(cc_code='32',jumlah_total)) as kasus_total_32,sum(getkoas(cc_code='32',klaim)) as klaim_32,sum(getkoas(cc_code='32',klaim_total)) as klaim_total_32,sum(getkoas(cc_code='32',preto)) as preto_32,sum(getkoas(cc_code='32',tax_comm)) as tax_comm_32,sum(getkoas(cc_code='32',preto_total)) as preto_total_32,sum(getkoas(cc_code='32',tax_comm_total)) as tax_comm_total_32,"
                + "sum(getkoas(cc_code='33',jumlah)) as kasus_33,sum(getkoas(cc_code='33',jumlah_total)) as kasus_total_33,sum(getkoas(cc_code='33',klaim)) as klaim_33,sum(getkoas(cc_code='33',klaim_total)) as klaim_total_33,sum(getkoas(cc_code='33',preto)) as preto_33,sum(getkoas(cc_code='33',tax_comm)) as tax_comm_33,sum(getkoas(cc_code='33',preto_total)) as preto_total_33,sum(getkoas(cc_code='33',tax_comm_total)) as tax_comm_total_33,"
                + "sum(getkoas(cc_code='40',jumlah)) as kasus_40,sum(getkoas(cc_code='40',jumlah_total)) as kasus_total_40,sum(getkoas(cc_code='40',klaim)) as klaim_40,sum(getkoas(cc_code='40',klaim_total)) as klaim_total_40,sum(getkoas(cc_code='40',preto)) as preto_40,sum(getkoas(cc_code='40',tax_comm)) as tax_comm_40,sum(getkoas(cc_code='40',preto_total)) as preto_total_40,sum(getkoas(cc_code='40',tax_comm_total)) as tax_comm_total_40,"
                + "sum(getkoas(cc_code='41',jumlah)) as kasus_41,sum(getkoas(cc_code='41',jumlah_total)) as kasus_total_41,sum(getkoas(cc_code='41',klaim)) as klaim_41,sum(getkoas(cc_code='41',klaim_total)) as klaim_total_41,sum(getkoas(cc_code='41',preto)) as preto_41,sum(getkoas(cc_code='41',tax_comm)) as tax_comm_41,sum(getkoas(cc_code='41',preto_total)) as preto_total_41,sum(getkoas(cc_code='41',tax_comm_total)) as tax_comm_total_41,"
                + "sum(getkoas(cc_code='42',jumlah)) as kasus_42,sum(getkoas(cc_code='42',jumlah_total)) as kasus_total_42,sum(getkoas(cc_code='42',klaim)) as klaim_42,sum(getkoas(cc_code='42',klaim_total)) as klaim_total_42,sum(getkoas(cc_code='42',preto)) as preto_42,sum(getkoas(cc_code='42',tax_comm)) as tax_comm_42,sum(getkoas(cc_code='42',preto_total)) as preto_total_42,sum(getkoas(cc_code='42',tax_comm_total)) as tax_comm_total_42,"
                + "sum(getkoas(cc_code='43',jumlah)) as kasus_43,sum(getkoas(cc_code='43',jumlah_total)) as kasus_total_43,sum(getkoas(cc_code='43',klaim)) as klaim_43,sum(getkoas(cc_code='43',klaim_total)) as klaim_total_43,sum(getkoas(cc_code='43',preto)) as preto_43,sum(getkoas(cc_code='43',tax_comm)) as tax_comm_43,sum(getkoas(cc_code='43',preto_total)) as preto_total_43,sum(getkoas(cc_code='43',tax_comm_total)) as tax_comm_total_43,"
                + "sum(getkoas(cc_code='44',jumlah)) as kasus_44,sum(getkoas(cc_code='44',jumlah_total)) as kasus_total_44,sum(getkoas(cc_code='44',klaim)) as klaim_44,sum(getkoas(cc_code='44',klaim_total)) as klaim_total_44,sum(getkoas(cc_code='44',preto)) as preto_44,sum(getkoas(cc_code='44',tax_comm)) as tax_comm_44,sum(getkoas(cc_code='44',preto_total)) as preto_total_44,sum(getkoas(cc_code='44',tax_comm_total)) as tax_comm_total_44,"
                + "sum(getkoas(cc_code='45',jumlah)) as kasus_45,sum(getkoas(cc_code='45',jumlah_total)) as kasus_total_45,sum(getkoas(cc_code='45',klaim)) as klaim_45,sum(getkoas(cc_code='45',klaim_total)) as klaim_total_45,sum(getkoas(cc_code='45',preto)) as preto_45,sum(getkoas(cc_code='45',tax_comm)) as tax_comm_45,sum(getkoas(cc_code='45',preto_total)) as preto_total_45,sum(getkoas(cc_code='45',tax_comm_total)) as tax_comm_total_45,"
                + "sum(getkoas(cc_code='50',jumlah)) as kasus_50,sum(getkoas(cc_code='50',jumlah_total)) as kasus_total_50,sum(getkoas(cc_code='50',klaim)) as klaim_50,sum(getkoas(cc_code='50',klaim_total)) as klaim_total_50,sum(getkoas(cc_code='50',preto)) as preto_50,sum(getkoas(cc_code='50',tax_comm)) as tax_comm_50,sum(getkoas(cc_code='50',preto_total)) as preto_total_50,sum(getkoas(cc_code='50',tax_comm_total)) as tax_comm_total_50,"
                + "sum(getkoas(cc_code='51',jumlah)) as kasus_51,sum(getkoas(cc_code='51',jumlah_total)) as kasus_total_51,sum(getkoas(cc_code='51',klaim)) as klaim_51,sum(getkoas(cc_code='51',klaim_total)) as klaim_total_51,sum(getkoas(cc_code='51',preto)) as preto_51,sum(getkoas(cc_code='51',tax_comm)) as tax_comm_51,sum(getkoas(cc_code='51',preto_total)) as preto_total_51,sum(getkoas(cc_code='51',tax_comm_total)) as tax_comm_total_51,"
                + "sum(getkoas(cc_code='52',jumlah)) as kasus_52,sum(getkoas(cc_code='52',jumlah_total)) as kasus_total_52,sum(getkoas(cc_code='52',klaim)) as klaim_52,sum(getkoas(cc_code='52',klaim_total)) as klaim_total_52,sum(getkoas(cc_code='52',preto)) as preto_52,sum(getkoas(cc_code='52',tax_comm)) as tax_comm_52,sum(getkoas(cc_code='52',preto_total)) as preto_total_52,sum(getkoas(cc_code='52',tax_comm_total)) as tax_comm_total_52,"
                + "sum(getkoas(cc_code='60',jumlah)) as kasus_60,sum(getkoas(cc_code='60',jumlah_total)) as kasus_total_60,sum(getkoas(cc_code='60',klaim)) as klaim_60,sum(getkoas(cc_code='60',klaim_total)) as klaim_total_60,sum(getkoas(cc_code='60',preto)) as preto_60,sum(getkoas(cc_code='60',tax_comm)) as tax_comm_60,sum(getkoas(cc_code='60',preto_total)) as preto_total_60,sum(getkoas(cc_code='60',tax_comm_total)) as tax_comm_total_60,"
                + "sum(getkoas(cc_code='61',jumlah)) as kasus_61,sum(getkoas(cc_code='61',jumlah_total)) as kasus_total_61,sum(getkoas(cc_code='61',klaim)) as klaim_61,sum(getkoas(cc_code='61',klaim_total)) as klaim_total_61,sum(getkoas(cc_code='61',preto)) as preto_61,sum(getkoas(cc_code='61',tax_comm)) as tax_comm_61,sum(getkoas(cc_code='61',preto_total)) as preto_total_61,sum(getkoas(cc_code='61',tax_comm_total)) as tax_comm_total_61,"
                + "sum(getkoas(cc_code='70',jumlah)) as kasus_70,sum(getkoas(cc_code='70',jumlah_total)) as kasus_total_70,sum(getkoas(cc_code='70',klaim)) as klaim_70,sum(getkoas(cc_code='70',klaim_total)) as klaim_total_70,sum(getkoas(cc_code='70',preto)) as preto_70,sum(getkoas(cc_code='70',tax_comm)) as tax_comm_70,sum(getkoas(cc_code='70',preto_total)) as preto_total_70,sum(getkoas(cc_code='70',tax_comm_total)) as tax_comm_total_70,"
                + "sum(coalesce(jumlah,0)) as kasus,sum(coalesce(jumlah_total,0)) as kasus_total,sum(coalesce(klaim,0)) as klaim,sum(coalesce(klaim_total,0)) as klaim_total,sum(coalesce(preto,0)) as preto,sum(coalesce(tax_comm,0)) as tax_comm,sum(coalesce(preto_total,0)) as preto_total,sum(coalesce(tax_comm_total,0)) as tax_comm_total ");

        String sql = " left join (  select "
                + " a.pol_type_id,a.cc_code,"
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.premi_adisc,0) else 0 end) as preto,  "
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then (select coalesce(sum(x.tax_amount),0) "
                + " from ins_pol_items x where a.pol_id = x.pol_id "
                + " and x.ins_item_id in (11,18,25,32,12,19,26,33,13,20,27,34,88,89,90,100,105,106,107,108) "
                + " and tax_code in (1,4,7,2,5,8)) else 0 end) as tax_comm,  "
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.premi_adisc,0) else 0 end) as preto_total,"
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then (select coalesce(sum(x.tax_amount),0) "
                + " from ins_pol_items x where a.pol_id = x.pol_id "
                + " and x.ins_item_id in (11,18,25,32,12,19,26,33,13,20,27,34,88,89,90,100,105,106,107,108) "
                + " and tax_code in (1,4,7,2,5,8)) else 0 end) as tax_comm_total,  "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.claim_amount,0) else 0 end) as klaim,  "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + "then 1 else 0 end) as jumlah, "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.claim_amount,0) else 0 end) as klaim_total,"
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + "then 1 else 0 end) as jumlah_total "
                + " from ins_policy_produksi a ";
        //+ " where a.active_flag = 'Y' and a.effective_flag = 'Y' "
        //+ " and (a.status in ('POLICY','RENEWAL','ENDORSE') or (a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA')) ";

        sqa.addQuery(" from ins_policy_types b " + sql + " group by a.pol_type_id,a.cc_code order by a.cc_code ) ");

        sql = sqa.getSQL() + " a on a.pol_type_id = b.pol_type_id group by b.pol_type_id,b.description order by b.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAIM_RECAP_ALL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);
            /*
            BigDecimal preto = new BigDecimal(0);
            BigDecimal pretoTotal = new BigDecimal(0);
            BigDecimal preto00 = new BigDecimal(0);
            BigDecimal pretoTotal00 = new BigDecimal(0);
            BigDecimal preto01 = new BigDecimal(0);
            BigDecimal pretoTotal01 = new BigDecimal(0);
            BigDecimal preto10 = new BigDecimal(0);
            BigDecimal pretoTotal10 = new BigDecimal(0);
            BigDecimal preto11 = new BigDecimal(0);
            BigDecimal pretoTotal11 = new BigDecimal(0);
            BigDecimal preto12 = new BigDecimal(0);
            BigDecimal pretoTotal12 = new BigDecimal(0);
            BigDecimal preto13 = new BigDecimal(0);
            BigDecimal pretoTotal13 = new BigDecimal(0);
            BigDecimal preto14 = new BigDecimal(0);
            BigDecimal pretoTotal14 = new BigDecimal(0);
            BigDecimal preto15 = new BigDecimal(0);
            BigDecimal pretoTotal15 = new BigDecimal(0);
            BigDecimal preto16 = new BigDecimal(0);
            BigDecimal pretoTotal16 = new BigDecimal(0);
            BigDecimal preto17 = new BigDecimal(0);
            BigDecimal pretoTotal17 = new BigDecimal(0);
            BigDecimal preto18 = new BigDecimal(0);
            BigDecimal pretoTotal18 = new BigDecimal(0);
            BigDecimal preto20 = new BigDecimal(0);
            BigDecimal pretoTotal20 = new BigDecimal(0);
            BigDecimal preto21 = new BigDecimal(0);
            BigDecimal pretoTotal21 = new BigDecimal(0);
            BigDecimal preto22 = new BigDecimal(0);
            BigDecimal pretoTotal22 = new BigDecimal(0);
            BigDecimal preto23 = new BigDecimal(0);
            BigDecimal pretoTotal23 = new BigDecimal(0);
            BigDecimal preto24 = new BigDecimal(0);
            BigDecimal pretoTotal24 = new BigDecimal(0);
            BigDecimal preto25 = new BigDecimal(0);
            BigDecimal pretoTotal25 = new BigDecimal(0);
            BigDecimal preto30 = new BigDecimal(0);
            BigDecimal pretoTotal30 = new BigDecimal(0);
            BigDecimal preto31 = new BigDecimal(0);
            BigDecimal pretoTotal31 = new BigDecimal(0);
            BigDecimal preto32 = new BigDecimal(0);
            BigDecimal pretoTotal32 = new BigDecimal(0);
            BigDecimal preto33 = new BigDecimal(0);
            BigDecimal pretoTotal33 = new BigDecimal(0);
            BigDecimal preto40 = new BigDecimal(0);
            BigDecimal pretoTotal40 = new BigDecimal(0);
            BigDecimal preto41 = new BigDecimal(0);
            BigDecimal pretoTotal41 = new BigDecimal(0);
            BigDecimal preto42 = new BigDecimal(0);
            BigDecimal pretoTotal42 = new BigDecimal(0);
            BigDecimal preto43 = new BigDecimal(0);
            BigDecimal pretoTotal43 = new BigDecimal(0);
            BigDecimal preto44 = new BigDecimal(0);
            BigDecimal pretoTotal44 = new BigDecimal(0);
            BigDecimal preto45 = new BigDecimal(0);
            BigDecimal pretoTotal45 = new BigDecimal(0);
            BigDecimal preto50 = new BigDecimal(0);
            BigDecimal pretoTotal50 = new BigDecimal(0);
            BigDecimal preto51 = new BigDecimal(0);
            BigDecimal pretoTotal51 = new BigDecimal(0);
            BigDecimal preto52 = new BigDecimal(0);
            BigDecimal pretoTotal52 = new BigDecimal(0);
            BigDecimal preto60 = new BigDecimal(0);
            BigDecimal pretoTotal60 = new BigDecimal(0);
            BigDecimal preto61 = new BigDecimal(0);
            BigDecimal pretoTotal61 = new BigDecimal(0);
            BigDecimal preto70 = new BigDecimal(0);
            BigDecimal pretoTotal70 = new BigDecimal(0);*/

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(2).setCellValue("kasus_00");
            row0.createCell(3).setCellValue("kasus_total_00");
            row0.createCell(4).setCellValue("klaim_00");
            row0.createCell(5).setCellValue("klaim_total_00");
            row0.createCell(6).setCellValue("preto_00");
            row0.createCell(7).setCellValue("pretoTotal_00");
            row0.createCell(8).setCellValue("taxcomm_00");
            row0.createCell(9).setCellValue("taxcommTotal_00");
            row0.createCell(10).setCellValue("kasus_01");
            row0.createCell(11).setCellValue("kasus_total_01");
            row0.createCell(12).setCellValue("klaim_01");
            row0.createCell(13).setCellValue("klaim_total_01");
            row0.createCell(14).setCellValue("preto_01");
            row0.createCell(15).setCellValue("pretoTotal_01");
            row0.createCell(16).setCellValue("taxcomm_01");
            row0.createCell(17).setCellValue("taxcommTotal_01");
            row0.createCell(18).setCellValue("kasus_10");
            row0.createCell(19).setCellValue("kasus_total_10");
            row0.createCell(20).setCellValue("klaim_10");
            row0.createCell(21).setCellValue("klaim_total_10");
            row0.createCell(22).setCellValue("preto_10");
            row0.createCell(23).setCellValue("pretoTotal_10");
            row0.createCell(24).setCellValue("taxcomm_10");
            row0.createCell(25).setCellValue("taxcommTotal_10");
            row0.createCell(26).setCellValue("kasus_11");
            row0.createCell(27).setCellValue("kasus_total_11");
            row0.createCell(28).setCellValue("klaim_11");
            row0.createCell(29).setCellValue("klaim_total_11");
            row0.createCell(30).setCellValue("preto_11");
            row0.createCell(31).setCellValue("pretoTotal_11");
            row0.createCell(32).setCellValue("taxcomm_11");
            row0.createCell(33).setCellValue("taxcommTotal_11");
            row0.createCell(34).setCellValue("kasus_12");
            row0.createCell(35).setCellValue("kasus_total_12");
            row0.createCell(36).setCellValue("klaim_12");
            row0.createCell(37).setCellValue("klaim_total_12");
            row0.createCell(38).setCellValue("preto_12");
            row0.createCell(39).setCellValue("pretoTotal_12");
            row0.createCell(40).setCellValue("taxcomm_12");
            row0.createCell(41).setCellValue("taxcommTotal_12");
            row0.createCell(42).setCellValue("kasus_13");
            row0.createCell(43).setCellValue("kasus_total_13");
            row0.createCell(44).setCellValue("klaim_13");
            row0.createCell(45).setCellValue("klaim_total_13");
            row0.createCell(46).setCellValue("preto_13");
            row0.createCell(47).setCellValue("pretoTotal_13");
            row0.createCell(48).setCellValue("taxcomm_13");
            row0.createCell(49).setCellValue("taxcommTotal_13");
            row0.createCell(50).setCellValue("kasus_14");
            row0.createCell(51).setCellValue("kasus_total_14");
            row0.createCell(52).setCellValue("klaim_14");
            row0.createCell(53).setCellValue("klaim_total_14");
            row0.createCell(54).setCellValue("preto_14");
            row0.createCell(55).setCellValue("pretoTotal_14");
            row0.createCell(56).setCellValue("taxcomm_14");
            row0.createCell(57).setCellValue("taxcommTotal_14");
            row0.createCell(58).setCellValue("kasus_15");
            row0.createCell(59).setCellValue("kasus_total_15");
            row0.createCell(60).setCellValue("klaim_15");
            row0.createCell(61).setCellValue("klaim_total_15");
            row0.createCell(62).setCellValue("preto_15");
            row0.createCell(63).setCellValue("pretoTotal_15");
            row0.createCell(64).setCellValue("taxcomm_15");
            row0.createCell(65).setCellValue("taxcommTotal_15");
            row0.createCell(66).setCellValue("kasus_16");
            row0.createCell(67).setCellValue("kasus_total_16");
            row0.createCell(68).setCellValue("klaim_16");
            row0.createCell(69).setCellValue("klaim_total_16");
            row0.createCell(70).setCellValue("preto_16");
            row0.createCell(71).setCellValue("pretoTotal_16");
            row0.createCell(72).setCellValue("taxcomm_16");
            row0.createCell(73).setCellValue("taxcommTotal_16");
            row0.createCell(74).setCellValue("kasus_17");
            row0.createCell(75).setCellValue("kasus_total_17");
            row0.createCell(76).setCellValue("klaim_17");
            row0.createCell(77).setCellValue("klaim_total_17");
            row0.createCell(78).setCellValue("preto_17");
            row0.createCell(79).setCellValue("pretoTotal_17");
            row0.createCell(80).setCellValue("taxcomm_17");
            row0.createCell(81).setCellValue("taxcommTotal_17");
            row0.createCell(82).setCellValue("kasus_18");
            row0.createCell(83).setCellValue("kasus_total_18");
            row0.createCell(84).setCellValue("klaim_18");
            row0.createCell(85).setCellValue("klaim_total_18");
            row0.createCell(86).setCellValue("preto_18");
            row0.createCell(87).setCellValue("pretoTotal_18");
            row0.createCell(88).setCellValue("taxcomm_18");
            row0.createCell(89).setCellValue("taxcommTotal_18");
            row0.createCell(90).setCellValue("kasus_19");
            row0.createCell(91).setCellValue("kasus_total_19");
            row0.createCell(92).setCellValue("klaim_19");
            row0.createCell(93).setCellValue("klaim_total_19");
            row0.createCell(94).setCellValue("preto_19");
            row0.createCell(95).setCellValue("pretoTotal_19");
            row0.createCell(96).setCellValue("taxcomm_19");
            row0.createCell(97).setCellValue("taxcommTotal_19");
            row0.createCell(98).setCellValue("kasus_20");
            row0.createCell(99).setCellValue("kasus_total_20");
            row0.createCell(100).setCellValue("klaim_20");
            row0.createCell(101).setCellValue("klaim_total_20");
            row0.createCell(102).setCellValue("preto_20");
            row0.createCell(103).setCellValue("pretoTotal_20");
            row0.createCell(104).setCellValue("taxcomm_20");
            row0.createCell(105).setCellValue("taxcommTotal_20");
            row0.createCell(106).setCellValue("kasus_21");
            row0.createCell(107).setCellValue("kasus_total_21");
            row0.createCell(108).setCellValue("klaim_21");
            row0.createCell(109).setCellValue("klaim_total_21");
            row0.createCell(110).setCellValue("preto_21");
            row0.createCell(111).setCellValue("pretoTotal_21");
            row0.createCell(112).setCellValue("taxcomm_21");
            row0.createCell(113).setCellValue("taxcommTotal_21");
            row0.createCell(114).setCellValue("kasus_22");
            row0.createCell(115).setCellValue("kasus_total_22");
            row0.createCell(116).setCellValue("klaim_22");
            row0.createCell(117).setCellValue("klaim_total_22");
            row0.createCell(118).setCellValue("preto_22");
            row0.createCell(119).setCellValue("pretoTotal_22");
            row0.createCell(120).setCellValue("taxcomm_22");
            row0.createCell(121).setCellValue("taxcommTotal_22");
            row0.createCell(122).setCellValue("kasus_23");
            row0.createCell(123).setCellValue("kasus_total_23");
            row0.createCell(124).setCellValue("klaim_23");
            row0.createCell(125).setCellValue("klaim_total_23");
            row0.createCell(126).setCellValue("preto_23");
            row0.createCell(127).setCellValue("pretoTotal_23");
            row0.createCell(128).setCellValue("taxcomm_23");
            row0.createCell(129).setCellValue("taxcommTotal_23");
            row0.createCell(130).setCellValue("kasus_24");
            row0.createCell(131).setCellValue("kasus_total_24");
            row0.createCell(132).setCellValue("klaim_24");
            row0.createCell(133).setCellValue("klaim_total_24");
            row0.createCell(134).setCellValue("preto_24");
            row0.createCell(135).setCellValue("pretoTotal_24");
            row0.createCell(136).setCellValue("taxcomm_24");
            row0.createCell(137).setCellValue("taxcommTotal_24");
            row0.createCell(138).setCellValue("kasus_25");
            row0.createCell(139).setCellValue("kasus_total_25");
            row0.createCell(140).setCellValue("klaim_25");
            row0.createCell(141).setCellValue("klaim_total_25");
            row0.createCell(142).setCellValue("preto_25");
            row0.createCell(143).setCellValue("pretoTotal_25");
            row0.createCell(144).setCellValue("taxcomm_25");
            row0.createCell(145).setCellValue("taxcommTotal_25");
            row0.createCell(146).setCellValue("kasus_30");
            row0.createCell(147).setCellValue("kasus_total_30");
            row0.createCell(148).setCellValue("klaim_30");
            row0.createCell(149).setCellValue("klaim_total_30");
            row0.createCell(150).setCellValue("preto_30");
            row0.createCell(151).setCellValue("pretoTotal_30");
            row0.createCell(152).setCellValue("taxcomm_30");
            row0.createCell(153).setCellValue("taxcommTotal_30");
            row0.createCell(154).setCellValue("kasus_31");
            row0.createCell(155).setCellValue("kasus_total_31");
            row0.createCell(156).setCellValue("klaim_31");
            row0.createCell(157).setCellValue("klaim_total_31");
            row0.createCell(158).setCellValue("preto_31");
            row0.createCell(159).setCellValue("pretoTotal_31");
            row0.createCell(160).setCellValue("taxcomm_31");
            row0.createCell(161).setCellValue("taxcommTotal_31");
            row0.createCell(162).setCellValue("kasus_32");
            row0.createCell(163).setCellValue("kasus_total_32");
            row0.createCell(164).setCellValue("klaim_32");
            row0.createCell(165).setCellValue("klaim_total_32");
            row0.createCell(166).setCellValue("preto_32");
            row0.createCell(167).setCellValue("pretoTotal_32");
            row0.createCell(168).setCellValue("taxcomm_32");
            row0.createCell(169).setCellValue("taxcommTotal_32");
            row0.createCell(170).setCellValue("kasus_33");
            row0.createCell(171).setCellValue("kasus_total_33");
            row0.createCell(172).setCellValue("klaim_33");
            row0.createCell(173).setCellValue("klaim_total_33");
            row0.createCell(174).setCellValue("preto_33");
            row0.createCell(175).setCellValue("pretoTotal_33");
            row0.createCell(176).setCellValue("taxcomm_33");
            row0.createCell(177).setCellValue("taxcommTotal_33");
            row0.createCell(178).setCellValue("kasus_40");
            row0.createCell(179).setCellValue("kasus_total_40");
            row0.createCell(180).setCellValue("klaim_40");
            row0.createCell(181).setCellValue("klaim_total_40");
            row0.createCell(182).setCellValue("preto_40");
            row0.createCell(183).setCellValue("pretoTotal_40");
            row0.createCell(184).setCellValue("taxcomm_40");
            row0.createCell(185).setCellValue("taxcommTotal_40");
            row0.createCell(186).setCellValue("kasus_41");
            row0.createCell(187).setCellValue("kasus_total_41");
            row0.createCell(188).setCellValue("klaim_41");
            row0.createCell(189).setCellValue("klaim_total_41");
            row0.createCell(190).setCellValue("preto_41");
            row0.createCell(191).setCellValue("pretoTotal_41");
            row0.createCell(192).setCellValue("taxcomm_41");
            row0.createCell(193).setCellValue("taxcommTotal_41");
            row0.createCell(194).setCellValue("kasus_42");
            row0.createCell(195).setCellValue("kasus_total_42");
            row0.createCell(196).setCellValue("klaim_42");
            row0.createCell(197).setCellValue("klaim_total_42");
            row0.createCell(198).setCellValue("preto_42");
            row0.createCell(199).setCellValue("pretoTotal_42");
            row0.createCell(200).setCellValue("taxcomm_42");
            row0.createCell(201).setCellValue("taxcommTotal_42");
            row0.createCell(202).setCellValue("kasus_43");
            row0.createCell(203).setCellValue("kasus_total_43");
            row0.createCell(204).setCellValue("klaim_43");
            row0.createCell(205).setCellValue("klaim_total_43");
            row0.createCell(206).setCellValue("preto_43");
            row0.createCell(207).setCellValue("pretoTotal_43");
            row0.createCell(208).setCellValue("taxcomm_43");
            row0.createCell(209).setCellValue("taxcommTotal_43");
            row0.createCell(210).setCellValue("kasus_44");
            row0.createCell(211).setCellValue("kasus_total_44");
            row0.createCell(212).setCellValue("klaim_44");
            row0.createCell(213).setCellValue("klaim_total_44");
            row0.createCell(214).setCellValue("preto_44");
            row0.createCell(215).setCellValue("pretoTotal_44");
            row0.createCell(216).setCellValue("taxcomm_44");
            row0.createCell(217).setCellValue("taxcommTotal_44");
            row0.createCell(218).setCellValue("kasus_45");
            row0.createCell(219).setCellValue("kasus_total_45");
            row0.createCell(220).setCellValue("klaim_45");
            row0.createCell(221).setCellValue("klaim_total_45");
            row0.createCell(222).setCellValue("preto_45");
            row0.createCell(223).setCellValue("pretoTotal_45");
            row0.createCell(224).setCellValue("taxcomm_45");
            row0.createCell(225).setCellValue("taxcommTotal_45");
            row0.createCell(226).setCellValue("kasus_50");
            row0.createCell(227).setCellValue("kasus_total_50");
            row0.createCell(228).setCellValue("klaim_50");
            row0.createCell(229).setCellValue("klaim_total_50");
            row0.createCell(230).setCellValue("preto_50");
            row0.createCell(231).setCellValue("pretoTotal_50");
            row0.createCell(232).setCellValue("taxcomm_50");
            row0.createCell(233).setCellValue("taxcommTotal_50");
            row0.createCell(234).setCellValue("kasus_51");
            row0.createCell(235).setCellValue("kasus_total_51");
            row0.createCell(236).setCellValue("klaim_51");
            row0.createCell(237).setCellValue("klaim_total_51");
            row0.createCell(238).setCellValue("preto_51");
            row0.createCell(239).setCellValue("pretoTotal_51");
            row0.createCell(240).setCellValue("taxcomm_51");
            row0.createCell(241).setCellValue("taxcommTotal_51");
            row0.createCell(242).setCellValue("kasus_52");
            row0.createCell(243).setCellValue("kasus_total_52");
            row0.createCell(244).setCellValue("klaim_52");
            row0.createCell(245).setCellValue("klaim_total_52");
            row0.createCell(246).setCellValue("preto_52");
            row0.createCell(247).setCellValue("pretoTotal_52");
            row0.createCell(248).setCellValue("taxcomm_52");
            row0.createCell(249).setCellValue("taxcommTotal_52");
            row0.createCell(250).setCellValue("kasus_60");
            row0.createCell(251).setCellValue("kasus_total_60");
            row0.createCell(252).setCellValue("klaim_60");
            row0.createCell(253).setCellValue("klaim_total_60");
            row0.createCell(254).setCellValue("preto_60");
            row0.createCell(255).setCellValue("pretoTotal_60");
            row0.createCell(256).setCellValue("taxcomm_60");
            row0.createCell(257).setCellValue("taxcommTotal_60");
            row0.createCell(258).setCellValue("kasus_61");
            row0.createCell(259).setCellValue("kasus_total_61");
            row0.createCell(260).setCellValue("klaim_61");
            row0.createCell(261).setCellValue("klaim_total_61");
            row0.createCell(262).setCellValue("preto_61");
            row0.createCell(263).setCellValue("pretoTotal_61");
            row0.createCell(264).setCellValue("taxcomm_61");
            row0.createCell(265).setCellValue("taxcommTotal_61");
            row0.createCell(266).setCellValue("kasus_70");
            row0.createCell(267).setCellValue("kasus_total_70");
            row0.createCell(268).setCellValue("klaim_70");
            row0.createCell(269).setCellValue("klaim_total_70");
            row0.createCell(270).setCellValue("preto_70");
            row0.createCell(271).setCellValue("pretoTotal_70");
            row0.createCell(272).setCellValue("taxcomm_70");
            row0.createCell(273).setCellValue("taxcommTotal_70");
            row0.createCell(274).setCellValue("jumlah kasus");
            row0.createCell(275).setCellValue("jumlah kasus total");
            row0.createCell(276).setCellValue("jumlah klaim");
            row0.createCell(277).setCellValue("jumlah klaim total");
            row0.createCell(278).setCellValue("jumlah preto");
            row0.createCell(279).setCellValue("jumlah preto total");
            row0.createCell(280).setCellValue("jumlah taxcomm");
            row0.createCell(281).setCellValue("jumlah taxcomm total");

            /*
            preto00 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_00"), h.getFieldValueByFieldNameBD("tax_comm_00"));
            pretoTotal00 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_00"), h.getFieldValueByFieldNameBD("tax_comm_total_00"));
            preto01 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_01"), h.getFieldValueByFieldNameBD("tax_comm_01"));
            pretoTotal01 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_01"), h.getFieldValueByFieldNameBD("tax_comm_total_01"));
            preto10 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_10"), h.getFieldValueByFieldNameBD("tax_comm_10"));
            pretoTotal10 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_10"), h.getFieldValueByFieldNameBD("tax_comm_total_10"));
            preto11 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_11"), h.getFieldValueByFieldNameBD("tax_comm_11"));
            pretoTotal11 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_11"), h.getFieldValueByFieldNameBD("tax_comm_total_11"));
            preto12 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_12"), h.getFieldValueByFieldNameBD("tax_comm_12"));
            pretoTotal12 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_12"), h.getFieldValueByFieldNameBD("tax_comm_total_12"));
            preto13 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_13"), h.getFieldValueByFieldNameBD("tax_comm_13"));
            pretoTotal13 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_13"), h.getFieldValueByFieldNameBD("tax_comm_total_13"));
            preto14 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_14"), h.getFieldValueByFieldNameBD("tax_comm_14"));
            pretoTotal14 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_14"), h.getFieldValueByFieldNameBD("tax_comm_total_14"));
            preto15 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_15"), h.getFieldValueByFieldNameBD("tax_comm_15"));
            pretoTotal15 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_15"), h.getFieldValueByFieldNameBD("tax_comm_total_15"));
            preto16 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_16"), h.getFieldValueByFieldNameBD("tax_comm_16"));
            pretoTotal16 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_16"), h.getFieldValueByFieldNameBD("tax_comm_total_16"));
            preto17 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_17"), h.getFieldValueByFieldNameBD("tax_comm_17"));
            pretoTotal17 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_17"), h.getFieldValueByFieldNameBD("tax_comm_total_17"));
            preto18 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_18"), h.getFieldValueByFieldNameBD("tax_comm_18"));
            pretoTotal18 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_18"), h.getFieldValueByFieldNameBD("tax_comm_total_18"));
            preto20 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_20"), h.getFieldValueByFieldNameBD("tax_comm_20"));
            pretoTotal20 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_20"), h.getFieldValueByFieldNameBD("tax_comm_total_20"));
            preto21 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_21"), h.getFieldValueByFieldNameBD("tax_comm_21"));
            pretoTotal21 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_21"), h.getFieldValueByFieldNameBD("tax_comm_total_21"));
            preto22 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_22"), h.getFieldValueByFieldNameBD("tax_comm_22"));
            pretoTotal22 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_22"), h.getFieldValueByFieldNameBD("tax_comm_total_22"));
            preto23 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_23"), h.getFieldValueByFieldNameBD("tax_comm_23"));
            pretoTotal23 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_23"), h.getFieldValueByFieldNameBD("tax_comm_total_23"));
            preto24 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_24"), h.getFieldValueByFieldNameBD("tax_comm_24"));
            pretoTotal24 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_24"), h.getFieldValueByFieldNameBD("tax_comm_total_24"));
            preto25 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_25"), h.getFieldValueByFieldNameBD("tax_comm_25"));
            pretoTotal25 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_25"), h.getFieldValueByFieldNameBD("tax_comm_total_25"));
            preto30 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_30"), h.getFieldValueByFieldNameBD("tax_comm_30"));
            pretoTotal30 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_30"), h.getFieldValueByFieldNameBD("tax_comm_total_30"));
            preto31 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_31"), h.getFieldValueByFieldNameBD("tax_comm_31"));
            pretoTotal31 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_31"), h.getFieldValueByFieldNameBD("tax_comm_total_31"));
            preto32 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_32"), h.getFieldValueByFieldNameBD("tax_comm_32"));
            pretoTotal32 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_32"), h.getFieldValueByFieldNameBD("tax_comm_total_32"));
            preto33 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_33"), h.getFieldValueByFieldNameBD("tax_comm_33"));
            pretoTotal33 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_33"), h.getFieldValueByFieldNameBD("tax_comm_total_33"));
            preto40 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_40"), h.getFieldValueByFieldNameBD("tax_comm_40"));
            pretoTotal40 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_40"), h.getFieldValueByFieldNameBD("tax_comm_total_40"));
            preto41 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_41"), h.getFieldValueByFieldNameBD("tax_comm_41"));
            pretoTotal41 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_41"), h.getFieldValueByFieldNameBD("tax_comm_total_41"));
            preto42 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_42"), h.getFieldValueByFieldNameBD("tax_comm_42"));
            pretoTotal42 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_42"), h.getFieldValueByFieldNameBD("tax_comm_total_42"));
            preto43 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_43"), h.getFieldValueByFieldNameBD("tax_comm_43"));
            pretoTotal43 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_43"), h.getFieldValueByFieldNameBD("tax_comm_total_43"));
            preto44 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_44"), h.getFieldValueByFieldNameBD("tax_comm_44"));
            pretoTotal44 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_44"), h.getFieldValueByFieldNameBD("tax_comm_total_44"));
            preto45 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_45"), h.getFieldValueByFieldNameBD("tax_comm_45"));
            pretoTotal45 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_45"), h.getFieldValueByFieldNameBD("tax_comm_total_45"));
            preto50 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_50"), h.getFieldValueByFieldNameBD("tax_comm_50"));
            pretoTotal50 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_50"), h.getFieldValueByFieldNameBD("tax_comm_total_50"));
            preto51 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_51"), h.getFieldValueByFieldNameBD("tax_comm_51"));
            pretoTotal51 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_51"), h.getFieldValueByFieldNameBD("tax_comm_total_51"));
            preto52 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_52"), h.getFieldValueByFieldNameBD("tax_comm_52"));
            pretoTotal52 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_52"), h.getFieldValueByFieldNameBD("tax_comm_total_52"));
            preto60 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_60"), h.getFieldValueByFieldNameBD("tax_comm_60"));
            pretoTotal60 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_60"), h.getFieldValueByFieldNameBD("tax_comm_total_60"));
            preto61 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_61"), h.getFieldValueByFieldNameBD("tax_comm_61"));
            pretoTotal61 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_61"), h.getFieldValueByFieldNameBD("tax_comm_total_61"));
            preto70 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_70"), h.getFieldValueByFieldNameBD("tax_comm_70"));
            pretoTotal70 = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total_70"), h.getFieldValueByFieldNameBD("tax_comm_total_70"));
            preto = BDUtil.add(h.getFieldValueByFieldNameBD("preto"), h.getFieldValueByFieldNameBD("tax_comm"));
            pretoTotal = BDUtil.add(h.getFieldValueByFieldNameBD("preto_total"), h.getFieldValueByFieldNameBD("tax_comm_total"));
             */

//bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("kasus_00").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_00").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("klaim_00").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_00").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("preto_00").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("preto_total_00").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_00").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_00").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("kasus_01").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_01").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("klaim_01").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_01").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("preto_01").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("preto_total_01").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_01").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_01").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("kasus_10").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_10").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("klaim_10").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_10").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("preto_10").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("preto_total_10").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_10").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_10").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("kasus_11").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_11").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("klaim_11").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_11").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("preto_11").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("preto_total_11").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_11").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_11").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("kasus_12").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_12").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("klaim_12").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_12").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("preto_12").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("preto_total_12").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_12").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_12").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("kasus_13").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_13").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("klaim_13").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_13").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("preto_13").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("preto_total_13").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_13").doubleValue());
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_13").doubleValue());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("kasus_14").doubleValue());
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_14").doubleValue());
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("klaim_14").doubleValue());
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_14").doubleValue());
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("preto_14").doubleValue());
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("preto_total_14").doubleValue());
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_14").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_14").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("kasus_15").doubleValue());
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_15").doubleValue());
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("klaim_15").doubleValue());
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_15").doubleValue());
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("preto_15").doubleValue());
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("preto_total_15").doubleValue());
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_15").doubleValue());
            row.createCell(65).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_15").doubleValue());
            row.createCell(66).setCellValue(h.getFieldValueByFieldNameBD("kasus_16").doubleValue());
            row.createCell(67).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_16").doubleValue());
            row.createCell(68).setCellValue(h.getFieldValueByFieldNameBD("klaim_16").doubleValue());
            row.createCell(69).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_16").doubleValue());
            row.createCell(70).setCellValue(h.getFieldValueByFieldNameBD("preto_16").doubleValue());
            row.createCell(71).setCellValue(h.getFieldValueByFieldNameBD("preto_total_16").doubleValue());
            row.createCell(72).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_16").doubleValue());
            row.createCell(73).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_16").doubleValue());
            row.createCell(74).setCellValue(h.getFieldValueByFieldNameBD("kasus_17").doubleValue());
            row.createCell(75).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_17").doubleValue());
            row.createCell(76).setCellValue(h.getFieldValueByFieldNameBD("klaim_17").doubleValue());
            row.createCell(77).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_17").doubleValue());
            row.createCell(78).setCellValue(h.getFieldValueByFieldNameBD("preto_17").doubleValue());
            row.createCell(79).setCellValue(h.getFieldValueByFieldNameBD("preto_total_17").doubleValue());
            row.createCell(80).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_17").doubleValue());
            row.createCell(81).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_17").doubleValue());
            row.createCell(82).setCellValue(h.getFieldValueByFieldNameBD("kasus_18").doubleValue());
            row.createCell(83).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_18").doubleValue());
            row.createCell(84).setCellValue(h.getFieldValueByFieldNameBD("klaim_18").doubleValue());
            row.createCell(85).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_18").doubleValue());
            row.createCell(86).setCellValue(h.getFieldValueByFieldNameBD("preto_18").doubleValue());
            row.createCell(87).setCellValue(h.getFieldValueByFieldNameBD("preto_total_18").doubleValue());
            row.createCell(88).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_18").doubleValue());
            row.createCell(89).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_18").doubleValue());
            row.createCell(90).setCellValue(h.getFieldValueByFieldNameBD("kasus_19").doubleValue());
            row.createCell(91).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_19").doubleValue());
            row.createCell(92).setCellValue(h.getFieldValueByFieldNameBD("klaim_19").doubleValue());
            row.createCell(93).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_19").doubleValue());
            row.createCell(94).setCellValue(h.getFieldValueByFieldNameBD("preto_19").doubleValue());
            row.createCell(95).setCellValue(h.getFieldValueByFieldNameBD("preto_total_19").doubleValue());
            row.createCell(96).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_19").doubleValue());
            row.createCell(97).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_19").doubleValue());
            row.createCell(98).setCellValue(h.getFieldValueByFieldNameBD("kasus_20").doubleValue());
            row.createCell(99).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_20").doubleValue());
            row.createCell(100).setCellValue(h.getFieldValueByFieldNameBD("klaim_20").doubleValue());
            row.createCell(101).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_20").doubleValue());
            row.createCell(102).setCellValue(h.getFieldValueByFieldNameBD("preto_20").doubleValue());
            row.createCell(103).setCellValue(h.getFieldValueByFieldNameBD("preto_total_20").doubleValue());
            row.createCell(104).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_20").doubleValue());
            row.createCell(105).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_20").doubleValue());
            row.createCell(106).setCellValue(h.getFieldValueByFieldNameBD("kasus_21").doubleValue());
            row.createCell(107).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_21").doubleValue());
            row.createCell(108).setCellValue(h.getFieldValueByFieldNameBD("klaim_21").doubleValue());
            row.createCell(109).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_21").doubleValue());
            row.createCell(110).setCellValue(h.getFieldValueByFieldNameBD("preto_21").doubleValue());
            row.createCell(111).setCellValue(h.getFieldValueByFieldNameBD("preto_total_21").doubleValue());
            row.createCell(112).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_21").doubleValue());
            row.createCell(113).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_21").doubleValue());
            row.createCell(114).setCellValue(h.getFieldValueByFieldNameBD("kasus_22").doubleValue());
            row.createCell(115).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_22").doubleValue());
            row.createCell(116).setCellValue(h.getFieldValueByFieldNameBD("klaim_22").doubleValue());
            row.createCell(117).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_22").doubleValue());
            row.createCell(118).setCellValue(h.getFieldValueByFieldNameBD("preto_22").doubleValue());
            row.createCell(119).setCellValue(h.getFieldValueByFieldNameBD("preto_total_22").doubleValue());
            row.createCell(120).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_22").doubleValue());
            row.createCell(121).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_22").doubleValue());
            row.createCell(122).setCellValue(h.getFieldValueByFieldNameBD("kasus_23").doubleValue());
            row.createCell(123).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_23").doubleValue());
            row.createCell(124).setCellValue(h.getFieldValueByFieldNameBD("klaim_23").doubleValue());
            row.createCell(125).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_23").doubleValue());
            row.createCell(126).setCellValue(h.getFieldValueByFieldNameBD("preto_23").doubleValue());
            row.createCell(127).setCellValue(h.getFieldValueByFieldNameBD("preto_total_23").doubleValue());
            row.createCell(128).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_23").doubleValue());
            row.createCell(129).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_23").doubleValue());
            row.createCell(130).setCellValue(h.getFieldValueByFieldNameBD("kasus_24").doubleValue());
            row.createCell(131).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_24").doubleValue());
            row.createCell(132).setCellValue(h.getFieldValueByFieldNameBD("klaim_24").doubleValue());
            row.createCell(133).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_24").doubleValue());
            row.createCell(134).setCellValue(h.getFieldValueByFieldNameBD("preto_24").doubleValue());
            row.createCell(135).setCellValue(h.getFieldValueByFieldNameBD("preto_total_24").doubleValue());
            row.createCell(136).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_24").doubleValue());
            row.createCell(137).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_24").doubleValue());
            row.createCell(138).setCellValue(h.getFieldValueByFieldNameBD("kasus_25").doubleValue());
            row.createCell(139).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_25").doubleValue());
            row.createCell(140).setCellValue(h.getFieldValueByFieldNameBD("klaim_25").doubleValue());
            row.createCell(141).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_25").doubleValue());
            row.createCell(142).setCellValue(h.getFieldValueByFieldNameBD("preto_25").doubleValue());
            row.createCell(143).setCellValue(h.getFieldValueByFieldNameBD("preto_total_25").doubleValue());
            row.createCell(144).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_25").doubleValue());
            row.createCell(145).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_25").doubleValue());
            row.createCell(146).setCellValue(h.getFieldValueByFieldNameBD("kasus_30").doubleValue());
            row.createCell(147).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_30").doubleValue());
            row.createCell(148).setCellValue(h.getFieldValueByFieldNameBD("klaim_30").doubleValue());
            row.createCell(149).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_30").doubleValue());
            row.createCell(150).setCellValue(h.getFieldValueByFieldNameBD("preto_30").doubleValue());
            row.createCell(151).setCellValue(h.getFieldValueByFieldNameBD("preto_total_30").doubleValue());
            row.createCell(152).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_30").doubleValue());
            row.createCell(153).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_30").doubleValue());
            row.createCell(154).setCellValue(h.getFieldValueByFieldNameBD("kasus_31").doubleValue());
            row.createCell(155).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_31").doubleValue());
            row.createCell(156).setCellValue(h.getFieldValueByFieldNameBD("klaim_31").doubleValue());
            row.createCell(157).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_31").doubleValue());
            row.createCell(158).setCellValue(h.getFieldValueByFieldNameBD("preto_31").doubleValue());
            row.createCell(159).setCellValue(h.getFieldValueByFieldNameBD("preto_total_31").doubleValue());
            row.createCell(160).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_31").doubleValue());
            row.createCell(161).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_31").doubleValue());
            row.createCell(162).setCellValue(h.getFieldValueByFieldNameBD("kasus_32").doubleValue());
            row.createCell(163).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_32").doubleValue());
            row.createCell(164).setCellValue(h.getFieldValueByFieldNameBD("klaim_32").doubleValue());
            row.createCell(165).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_32").doubleValue());
            row.createCell(166).setCellValue(h.getFieldValueByFieldNameBD("preto_32").doubleValue());
            row.createCell(167).setCellValue(h.getFieldValueByFieldNameBD("preto_total_32").doubleValue());
            row.createCell(168).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_32").doubleValue());
            row.createCell(169).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_32").doubleValue());
            row.createCell(170).setCellValue(h.getFieldValueByFieldNameBD("kasus_33").doubleValue());
            row.createCell(171).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_33").doubleValue());
            row.createCell(172).setCellValue(h.getFieldValueByFieldNameBD("klaim_33").doubleValue());
            row.createCell(173).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_33").doubleValue());
            row.createCell(174).setCellValue(h.getFieldValueByFieldNameBD("preto_33").doubleValue());
            row.createCell(175).setCellValue(h.getFieldValueByFieldNameBD("preto_total_33").doubleValue());
            row.createCell(176).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_33").doubleValue());
            row.createCell(177).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_33").doubleValue());
            row.createCell(178).setCellValue(h.getFieldValueByFieldNameBD("kasus_40").doubleValue());
            row.createCell(179).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_40").doubleValue());
            row.createCell(180).setCellValue(h.getFieldValueByFieldNameBD("klaim_40").doubleValue());
            row.createCell(181).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_40").doubleValue());
            row.createCell(182).setCellValue(h.getFieldValueByFieldNameBD("preto_40").doubleValue());
            row.createCell(183).setCellValue(h.getFieldValueByFieldNameBD("preto_total_40").doubleValue());
            row.createCell(184).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_40").doubleValue());
            row.createCell(185).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_40").doubleValue());
            row.createCell(186).setCellValue(h.getFieldValueByFieldNameBD("kasus_41").doubleValue());
            row.createCell(187).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_41").doubleValue());
            row.createCell(188).setCellValue(h.getFieldValueByFieldNameBD("klaim_41").doubleValue());
            row.createCell(189).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_41").doubleValue());
            row.createCell(190).setCellValue(h.getFieldValueByFieldNameBD("preto_41").doubleValue());
            row.createCell(191).setCellValue(h.getFieldValueByFieldNameBD("preto_total_41").doubleValue());
            row.createCell(192).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_41").doubleValue());
            row.createCell(193).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_41").doubleValue());
            row.createCell(194).setCellValue(h.getFieldValueByFieldNameBD("kasus_42").doubleValue());
            row.createCell(195).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_42").doubleValue());
            row.createCell(196).setCellValue(h.getFieldValueByFieldNameBD("klaim_42").doubleValue());
            row.createCell(197).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_42").doubleValue());
            row.createCell(198).setCellValue(h.getFieldValueByFieldNameBD("preto_42").doubleValue());
            row.createCell(199).setCellValue(h.getFieldValueByFieldNameBD("preto_total_42").doubleValue());
            row.createCell(200).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_42").doubleValue());
            row.createCell(201).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_42").doubleValue());
            row.createCell(202).setCellValue(h.getFieldValueByFieldNameBD("kasus_43").doubleValue());
            row.createCell(203).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_43").doubleValue());
            row.createCell(204).setCellValue(h.getFieldValueByFieldNameBD("klaim_43").doubleValue());
            row.createCell(205).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_43").doubleValue());
            row.createCell(206).setCellValue(h.getFieldValueByFieldNameBD("preto_43").doubleValue());
            row.createCell(207).setCellValue(h.getFieldValueByFieldNameBD("preto_total_43").doubleValue());
            row.createCell(208).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_43").doubleValue());
            row.createCell(209).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_43").doubleValue());
            row.createCell(210).setCellValue(h.getFieldValueByFieldNameBD("kasus_44").doubleValue());
            row.createCell(211).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_44").doubleValue());
            row.createCell(212).setCellValue(h.getFieldValueByFieldNameBD("klaim_44").doubleValue());
            row.createCell(213).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_44").doubleValue());
            row.createCell(214).setCellValue(h.getFieldValueByFieldNameBD("preto_44").doubleValue());
            row.createCell(215).setCellValue(h.getFieldValueByFieldNameBD("preto_total_44").doubleValue());
            row.createCell(216).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_44").doubleValue());
            row.createCell(217).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_44").doubleValue());
            row.createCell(218).setCellValue(h.getFieldValueByFieldNameBD("kasus_45").doubleValue());
            row.createCell(219).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_45").doubleValue());
            row.createCell(220).setCellValue(h.getFieldValueByFieldNameBD("klaim_45").doubleValue());
            row.createCell(221).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_45").doubleValue());
            row.createCell(222).setCellValue(h.getFieldValueByFieldNameBD("preto_45").doubleValue());
            row.createCell(223).setCellValue(h.getFieldValueByFieldNameBD("preto_total_45").doubleValue());
            row.createCell(224).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_45").doubleValue());
            row.createCell(225).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_45").doubleValue());
            row.createCell(226).setCellValue(h.getFieldValueByFieldNameBD("kasus_50").doubleValue());
            row.createCell(227).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_50").doubleValue());
            row.createCell(228).setCellValue(h.getFieldValueByFieldNameBD("klaim_50").doubleValue());
            row.createCell(229).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_50").doubleValue());
            row.createCell(230).setCellValue(h.getFieldValueByFieldNameBD("preto_50").doubleValue());
            row.createCell(231).setCellValue(h.getFieldValueByFieldNameBD("preto_total_50").doubleValue());
            row.createCell(232).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_50").doubleValue());
            row.createCell(233).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_50").doubleValue());
            row.createCell(234).setCellValue(h.getFieldValueByFieldNameBD("kasus_51").doubleValue());
            row.createCell(235).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_51").doubleValue());
            row.createCell(236).setCellValue(h.getFieldValueByFieldNameBD("klaim_51").doubleValue());
            row.createCell(237).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_51").doubleValue());
            row.createCell(238).setCellValue(h.getFieldValueByFieldNameBD("preto_51").doubleValue());
            row.createCell(239).setCellValue(h.getFieldValueByFieldNameBD("preto_total_51").doubleValue());
            row.createCell(240).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_51").doubleValue());
            row.createCell(241).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_51").doubleValue());
            row.createCell(242).setCellValue(h.getFieldValueByFieldNameBD("kasus_52").doubleValue());
            row.createCell(243).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_52").doubleValue());
            row.createCell(244).setCellValue(h.getFieldValueByFieldNameBD("klaim_52").doubleValue());
            row.createCell(245).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_52").doubleValue());
            row.createCell(246).setCellValue(h.getFieldValueByFieldNameBD("preto_52").doubleValue());
            row.createCell(247).setCellValue(h.getFieldValueByFieldNameBD("preto_total_52").doubleValue());
            row.createCell(248).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_52").doubleValue());
            row.createCell(249).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_52").doubleValue());
            row.createCell(250).setCellValue(h.getFieldValueByFieldNameBD("kasus_60").doubleValue());
            row.createCell(251).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_60").doubleValue());
            row.createCell(252).setCellValue(h.getFieldValueByFieldNameBD("klaim_60").doubleValue());
            row.createCell(253).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_60").doubleValue());
            row.createCell(254).setCellValue(h.getFieldValueByFieldNameBD("preto_60").doubleValue());
            row.createCell(255).setCellValue(h.getFieldValueByFieldNameBD("preto_total_60").doubleValue());
            row.createCell(256).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_60").doubleValue());
            row.createCell(257).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_60").doubleValue());
            row.createCell(258).setCellValue(h.getFieldValueByFieldNameBD("kasus_61").doubleValue());
            row.createCell(259).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_61").doubleValue());
            row.createCell(260).setCellValue(h.getFieldValueByFieldNameBD("klaim_61").doubleValue());
            row.createCell(261).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_61").doubleValue());
            row.createCell(262).setCellValue(h.getFieldValueByFieldNameBD("preto_61").doubleValue());
            row.createCell(263).setCellValue(h.getFieldValueByFieldNameBD("preto_total_61").doubleValue());
            row.createCell(264).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_61").doubleValue());
            row.createCell(265).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_61").doubleValue());
            row.createCell(266).setCellValue(h.getFieldValueByFieldNameBD("kasus_70").doubleValue());
            row.createCell(267).setCellValue(h.getFieldValueByFieldNameBD("kasus_total_70").doubleValue());
            row.createCell(268).setCellValue(h.getFieldValueByFieldNameBD("klaim_70").doubleValue());
            row.createCell(269).setCellValue(h.getFieldValueByFieldNameBD("klaim_total_70").doubleValue());
            row.createCell(270).setCellValue(h.getFieldValueByFieldNameBD("preto_70").doubleValue());
            row.createCell(271).setCellValue(h.getFieldValueByFieldNameBD("preto_total_70").doubleValue());
            row.createCell(272).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_70").doubleValue());
            row.createCell(273).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total_70").doubleValue());
            row.createCell(274).setCellValue(h.getFieldValueByFieldNameBD("kasus").doubleValue());
            row.createCell(275).setCellValue(h.getFieldValueByFieldNameBD("kasus_total").doubleValue());
            row.createCell(276).setCellValue(h.getFieldValueByFieldNameBD("klaim").doubleValue());
            row.createCell(277).setCellValue(h.getFieldValueByFieldNameBD("klaim_total").doubleValue());
            row.createCell(278).setCellValue(h.getFieldValueByFieldNameBD("preto").doubleValue());
            row.createCell(279).setCellValue(h.getFieldValueByFieldNameBD("preto_total").doubleValue());
            row.createCell(280).setCellValue(h.getFieldValueByFieldNameBD("klaim_total").doubleValue());
            row.createCell(281).setCellValue(h.getFieldValueByFieldNameBD("tax_comm_total").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_CLAIM_RECAP_ALL_OS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,a.cc_code,a.claim_amount as hutang_klaim, "
                + "(case when a.dla_no is not null then 1 else 0 end) as jumlah,a.dla_no ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='N'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = "select b.pol_type_id,b.description,"
                + "sum(getkoas(cc_code='10',jumlah)) as kasus10, sum(getkoas(cc_code='10',hutang_klaim)) as Aceh, "
                + "sum(getkoas(cc_code='11',jumlah)) as kasus11, sum(getkoas(cc_code='11',hutang_klaim)) as Medan, "
                + "sum(getkoas(cc_code='12',jumlah)) as kasus12, sum(getkoas(cc_code='12',hutang_klaim)) as Padang, "
                + "sum(getkoas(cc_code='13',jumlah)) as kasus13, sum(getkoas(cc_code='13',hutang_klaim)) as Riau, "
                + "sum(getkoas(cc_code='14',jumlah)) as kasus14, sum(getkoas(cc_code='14',hutang_klaim)) as Jambi, "
                + "sum(getkoas(cc_code='15',jumlah)) as kasus15, sum(getkoas(cc_code='15',hutang_klaim)) as Palembang, "
                + "sum(getkoas(cc_code='16',jumlah)) as kasus16, sum(getkoas(cc_code='16',hutang_klaim)) as Bengkulu, "
                + "sum(getkoas(cc_code='17',jumlah)) as kasus17, sum(getkoas(cc_code='17',hutang_klaim)) as Lampung, "
                + "sum(getkoas(cc_code='18',jumlah)) as kasus18, sum(getkoas(cc_code='18',hutang_klaim)) as BaBel, "
                + "sum(getkoas(cc_code='20',jumlah)) as kasus20, sum(getkoas(cc_code='20',hutang_klaim)) as Jakarta, "
                + "sum(getkoas(cc_code='21',jumlah)) as kasus21, sum(getkoas(cc_code='21',hutang_klaim)) as Bandung, "
                + "sum(getkoas(cc_code='22',jumlah)) as kasus22, sum(getkoas(cc_code='22',hutang_klaim)) as Semarang, "
                + "sum(getkoas(cc_code='23',jumlah)) as kasus23, sum(getkoas(cc_code='23',hutang_klaim)) as Yogyakarta, "
                + "sum(getkoas(cc_code='24',jumlah)) as kasus24, sum(getkoas(cc_code='24',hutang_klaim)) as Surabaya, "
                + "sum(getkoas(cc_code='25',jumlah)) as kasus25, sum(getkoas(cc_code='25',hutang_klaim)) as Serang , "
                + "sum(getkoas(cc_code='30',jumlah)) as kasus30, sum(getkoas(cc_code='30',hutang_klaim)) as Pontianak, "
                + "sum(getkoas(cc_code='31',jumlah)) as kasus31, sum(getkoas(cc_code='31',hutang_klaim)) as Palangkaraya, "
                + "sum(getkoas(cc_code='32',jumlah)) as kasus32, sum(getkoas(cc_code='32',hutang_klaim)) as Banjarmasin, "
                + "sum(getkoas(cc_code='33',jumlah)) as kasus33, sum(getkoas(cc_code='33',hutang_klaim)) as Samarinda, "
                + "sum(getkoas(cc_code='40',jumlah)) as kasus40, sum(getkoas(cc_code='40',hutang_klaim)) as Manado, "
                + "sum(getkoas(cc_code='41',jumlah)) as kasus41, sum(getkoas(cc_code='41',hutang_klaim)) as Palu, "
                + "sum(getkoas(cc_code='42',jumlah)) as kasus42, sum(getkoas(cc_code='42',hutang_klaim)) as Kendari, "
                + "sum(getkoas(cc_code='43',jumlah)) as kasus43, sum(getkoas(cc_code='43',hutang_klaim)) as Makasar, "
                + "sum(getkoas(cc_code='44',jumlah)) as kasus44, sum(getkoas(cc_code='44',hutang_klaim)) as Mamuju, "
                + "sum(getkoas(cc_code='45',jumlah)) as kasus45, sum(getkoas(cc_code='45',hutang_klaim)) as Gorontalo, "
                + "sum(getkoas(cc_code='50',jumlah)) as kasus50, sum(getkoas(cc_code='50',hutang_klaim)) as Denpasar, "
                + "sum(getkoas(cc_code='51',jumlah)) as kasus51, sum(getkoas(cc_code='51',hutang_klaim)) as Mataram, "
                + "sum(getkoas(cc_code='52',jumlah)) as kasus52, sum(getkoas(cc_code='52',hutang_klaim)) as Kupang, "
                + "sum(getkoas(cc_code='60',jumlah)) as kasus60, sum(getkoas(cc_code='60',hutang_klaim)) as Ambon, "
                + "sum(getkoas(cc_code='61',jumlah)) as kasus61, sum(getkoas(cc_code='61',hutang_klaim)) as Ternate, "
                + "sum(getkoas(cc_code='70',jumlah)) as kasus70, sum(getkoas(cc_code='70',hutang_klaim)) as Papua, "
                + "sum(coalesce(jumlah,0)) as jumlah, sum(coalesce(hutang_klaim,0)) as premi_total_adisc "
                + "from ins_policy_types b left join (  "
                + sqa.getSQL() + " ) a on a.pol_type_id = b.pol_type_id  "
                + "group by b.pol_type_id,b.description order by b.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAIM_RECAP_ALL_OS() throws Exception {

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
            row0.createCell(2).setCellValue("kasus10");
            row0.createCell(3).setCellValue("aceh");
            row0.createCell(4).setCellValue("kasus11");
            row0.createCell(5).setCellValue("medan");
            row0.createCell(6).setCellValue("kasus12");
            row0.createCell(7).setCellValue("padang");
            row0.createCell(8).setCellValue("kasus13");
            row0.createCell(9).setCellValue("riau");
            row0.createCell(10).setCellValue("kasus14");
            row0.createCell(11).setCellValue("jambi");
            row0.createCell(12).setCellValue("kasus15");
            row0.createCell(13).setCellValue("palembang");
            row0.createCell(14).setCellValue("kasus16");
            row0.createCell(15).setCellValue("bengkulu");
            row0.createCell(16).setCellValue("kasus17");
            row0.createCell(17).setCellValue("lampung");
            row0.createCell(18).setCellValue("kasus18");
            row0.createCell(19).setCellValue("babel");
            row0.createCell(20).setCellValue("kasus20");
            row0.createCell(21).setCellValue("jakarta");
            row0.createCell(22).setCellValue("kasus21");
            row0.createCell(23).setCellValue("bandung");
            row0.createCell(24).setCellValue("kasus22");
            row0.createCell(25).setCellValue("semarang");
            row0.createCell(26).setCellValue("kasus23");
            row0.createCell(27).setCellValue("yogyakarta");
            row0.createCell(28).setCellValue("kasus24");
            row0.createCell(29).setCellValue("surabaya");
            row0.createCell(30).setCellValue("kasus25");
            row0.createCell(31).setCellValue("serang");
            row0.createCell(32).setCellValue("kasus30");
            row0.createCell(33).setCellValue("pontianak");
            row0.createCell(34).setCellValue("kasus31");
            row0.createCell(35).setCellValue("palangkaraya");
            row0.createCell(36).setCellValue("kasus32");
            row0.createCell(37).setCellValue("banjarmasin");
            row0.createCell(38).setCellValue("kasus33");
            row0.createCell(39).setCellValue("samarinda");
            row0.createCell(40).setCellValue("kasus40");
            row0.createCell(41).setCellValue("manado");
            row0.createCell(42).setCellValue("kasus41");
            row0.createCell(43).setCellValue("palu");
            row0.createCell(44).setCellValue("kasus42");
            row0.createCell(45).setCellValue("kendari");
            row0.createCell(46).setCellValue("kasus43");
            row0.createCell(47).setCellValue("makasar");
            row0.createCell(48).setCellValue("kasus44");
            row0.createCell(49).setCellValue("mamuju");
            row0.createCell(50).setCellValue("kasus45");
            row0.createCell(51).setCellValue("gorontalo");
            row0.createCell(52).setCellValue("kasus50");
            row0.createCell(53).setCellValue("denpasar");
            row0.createCell(54).setCellValue("kasus51");
            row0.createCell(55).setCellValue("mataram");
            row0.createCell(56).setCellValue("kasus52");
            row0.createCell(57).setCellValue("kupang");
            row0.createCell(58).setCellValue("kasus60");
            row0.createCell(59).setCellValue("ambon");
            row0.createCell(60).setCellValue("kasus61");
            row0.createCell(61).setCellValue("ternate");
            row0.createCell(62).setCellValue("kasus70");
            row0.createCell(63).setCellValue("papua");
            row0.createCell(64).setCellValue("jumlah");
            row0.createCell(65).setCellValue("total");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("kasus10").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("aceh").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("kasus11").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("medan").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("kasus12").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("padang").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("kasus13").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("riau").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("kasus14").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("jambi").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("kasus15").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("palembang").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("kasus16").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("bengkulu").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("kasus17").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("lampung").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("kasus18").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("babel").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("kasus20").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("jakarta").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("kasus21").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("bandung").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("kasus22").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("semarang").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("kasus23").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("yogyakarta").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("kasus24").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("surabaya").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("kasus25").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("serang").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("kasus30").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("pontianak").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("kasus31").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("palangkaraya").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("kasus32").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("banjarmasin").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("kasus33").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("samarinda").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("kasus40").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("manado").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("kasus41").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("palu").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("kasus42").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("kendari").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("kasus43").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("makasar").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("kasus44").doubleValue());
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("mamuju").doubleValue());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("kasus45").doubleValue());
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("gorontalo").doubleValue());
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("kasus50").doubleValue());
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("denpasar").doubleValue());
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("kasus51").doubleValue());
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("mataram").doubleValue());
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("kasus52").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("kupang").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("kasus60").doubleValue());
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("ambon").doubleValue());
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("kasus61").doubleValue());
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("ternate").doubleValue());
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("kasus70").doubleValue());
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("papua").doubleValue());
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());
            row.createCell(65).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_CLAIM_RECAP_LKS_ALL_OS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,a.cc_code,a.claim_amount as hutang_klaim, "
                + "(case when a.pla_no is not null then 1 else 0 end) as jumlah,a.pla_no ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause(" a.status = 'CLAIM'");
        sqa.addClause(" a.claim_status IN ('PLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='N'");

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= ?");
            sqa.addPar(PLADateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= ?");
            sqa.addPar(PLADateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = "select b.pol_type_id,b.description,"
                + "sum(getkoas(cc_code='10',jumlah)) as kasus10, sum(getkoas(cc_code='10',hutang_klaim)) as Aceh, "
                + "sum(getkoas(cc_code='11',jumlah)) as kasus11, sum(getkoas(cc_code='11',hutang_klaim)) as Medan, "
                + "sum(getkoas(cc_code='12',jumlah)) as kasus12, sum(getkoas(cc_code='12',hutang_klaim)) as Padang, "
                + "sum(getkoas(cc_code='13',jumlah)) as kasus13, sum(getkoas(cc_code='13',hutang_klaim)) as Riau, "
                + "sum(getkoas(cc_code='14',jumlah)) as kasus14, sum(getkoas(cc_code='14',hutang_klaim)) as Jambi, "
                + "sum(getkoas(cc_code='15',jumlah)) as kasus15, sum(getkoas(cc_code='15',hutang_klaim)) as Palembang, "
                + "sum(getkoas(cc_code='16',jumlah)) as kasus16, sum(getkoas(cc_code='16',hutang_klaim)) as Bengkulu, "
                + "sum(getkoas(cc_code='17',jumlah)) as kasus17, sum(getkoas(cc_code='17',hutang_klaim)) as Lampung, "
                + "sum(getkoas(cc_code='18',jumlah)) as kasus18, sum(getkoas(cc_code='18',hutang_klaim)) as BaBel, "
                + "sum(getkoas(cc_code='20',jumlah)) as kasus20, sum(getkoas(cc_code='20',hutang_klaim)) as Jakarta, "
                + "sum(getkoas(cc_code='21',jumlah)) as kasus21, sum(getkoas(cc_code='21',hutang_klaim)) as Bandung, "
                + "sum(getkoas(cc_code='22',jumlah)) as kasus22, sum(getkoas(cc_code='22',hutang_klaim)) as Semarang, "
                + "sum(getkoas(cc_code='23',jumlah)) as kasus23, sum(getkoas(cc_code='23',hutang_klaim)) as Yogyakarta, "
                + "sum(getkoas(cc_code='24',jumlah)) as kasus24, sum(getkoas(cc_code='24',hutang_klaim)) as Surabaya, "
                + "sum(getkoas(cc_code='25',jumlah)) as kasus25, sum(getkoas(cc_code='25',hutang_klaim)) as Serang , "
                + "sum(getkoas(cc_code='30',jumlah)) as kasus30, sum(getkoas(cc_code='30',hutang_klaim)) as Pontianak, "
                + "sum(getkoas(cc_code='31',jumlah)) as kasus31, sum(getkoas(cc_code='31',hutang_klaim)) as Palangkaraya, "
                + "sum(getkoas(cc_code='32',jumlah)) as kasus32, sum(getkoas(cc_code='32',hutang_klaim)) as Banjarmasin, "
                + "sum(getkoas(cc_code='33',jumlah)) as kasus33, sum(getkoas(cc_code='33',hutang_klaim)) as Samarinda, "
                + "sum(getkoas(cc_code='40',jumlah)) as kasus40, sum(getkoas(cc_code='40',hutang_klaim)) as Manado, "
                + "sum(getkoas(cc_code='41',jumlah)) as kasus41, sum(getkoas(cc_code='41',hutang_klaim)) as Palu, "
                + "sum(getkoas(cc_code='42',jumlah)) as kasus42, sum(getkoas(cc_code='42',hutang_klaim)) as Kendari, "
                + "sum(getkoas(cc_code='43',jumlah)) as kasus43, sum(getkoas(cc_code='43',hutang_klaim)) as Makasar, "
                + "sum(getkoas(cc_code='44',jumlah)) as kasus44, sum(getkoas(cc_code='44',hutang_klaim)) as Mamuju, "
                + "sum(getkoas(cc_code='45',jumlah)) as kasus45, sum(getkoas(cc_code='45',hutang_klaim)) as Gorontalo, "
                + "sum(getkoas(cc_code='50',jumlah)) as kasus50, sum(getkoas(cc_code='50',hutang_klaim)) as Denpasar, "
                + "sum(getkoas(cc_code='51',jumlah)) as kasus51, sum(getkoas(cc_code='51',hutang_klaim)) as Mataram, "
                + "sum(getkoas(cc_code='52',jumlah)) as kasus52, sum(getkoas(cc_code='52',hutang_klaim)) as Kupang, "
                + "sum(getkoas(cc_code='60',jumlah)) as kasus60, sum(getkoas(cc_code='60',hutang_klaim)) as Ambon, "
                + "sum(getkoas(cc_code='61',jumlah)) as kasus61, sum(getkoas(cc_code='61',hutang_klaim)) as Ternate, "
                + "sum(getkoas(cc_code='70',jumlah)) as kasus70, sum(getkoas(cc_code='70',hutang_klaim)) as Papua, "
                + "sum(coalesce(jumlah,0)) as jumlah, sum(coalesce(hutang_klaim,0)) as premi_total_adisc "
                + "from ins_policy_types b left join (  "
                + sqa.getSQL() + " ) a on a.pol_type_id = b.pol_type_id  "
                + "group by b.pol_type_id,b.description order by b.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAIM_RECAP_LKS_ALL_OS() throws Exception {

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
            row0.createCell(2).setCellValue("kasus10");
            row0.createCell(3).setCellValue("aceh");
            row0.createCell(4).setCellValue("kasus11");
            row0.createCell(5).setCellValue("medan");
            row0.createCell(6).setCellValue("kasus12");
            row0.createCell(7).setCellValue("padang");
            row0.createCell(8).setCellValue("kasus13");
            row0.createCell(9).setCellValue("riau");
            row0.createCell(10).setCellValue("kasus14");
            row0.createCell(11).setCellValue("jambi");
            row0.createCell(12).setCellValue("kasus15");
            row0.createCell(13).setCellValue("palembang");
            row0.createCell(14).setCellValue("kasus16");
            row0.createCell(15).setCellValue("bengkulu");
            row0.createCell(16).setCellValue("kasus17");
            row0.createCell(17).setCellValue("lampung");
            row0.createCell(18).setCellValue("kasus18");
            row0.createCell(19).setCellValue("babel");
            row0.createCell(20).setCellValue("kasus20");
            row0.createCell(21).setCellValue("jakarta");
            row0.createCell(22).setCellValue("kasus21");
            row0.createCell(23).setCellValue("bandung");
            row0.createCell(24).setCellValue("kasus22");
            row0.createCell(25).setCellValue("semarang");
            row0.createCell(26).setCellValue("kasus23");
            row0.createCell(27).setCellValue("yogyakarta");
            row0.createCell(28).setCellValue("kasus24");
            row0.createCell(29).setCellValue("surabaya");
            row0.createCell(30).setCellValue("kasus25");
            row0.createCell(31).setCellValue("serang");
            row0.createCell(32).setCellValue("kasus30");
            row0.createCell(33).setCellValue("pontianak");
            row0.createCell(34).setCellValue("kasus31");
            row0.createCell(35).setCellValue("palangkaraya");
            row0.createCell(36).setCellValue("kasus32");
            row0.createCell(37).setCellValue("banjarmasin");
            row0.createCell(38).setCellValue("kasus33");
            row0.createCell(39).setCellValue("samarinda");
            row0.createCell(40).setCellValue("kasus40");
            row0.createCell(41).setCellValue("manado");
            row0.createCell(42).setCellValue("kasus41");
            row0.createCell(43).setCellValue("palu");
            row0.createCell(44).setCellValue("kasus42");
            row0.createCell(45).setCellValue("kendari");
            row0.createCell(46).setCellValue("kasus43");
            row0.createCell(47).setCellValue("makasar");
            row0.createCell(48).setCellValue("kasus44");
            row0.createCell(49).setCellValue("mamuju");
            row0.createCell(50).setCellValue("kasus45");
            row0.createCell(51).setCellValue("gorontalo");
            row0.createCell(52).setCellValue("kasus50");
            row0.createCell(53).setCellValue("denpasar");
            row0.createCell(54).setCellValue("kasus51");
            row0.createCell(55).setCellValue("mataram");
            row0.createCell(56).setCellValue("kasus52");
            row0.createCell(57).setCellValue("kupang");
            row0.createCell(58).setCellValue("kasus60");
            row0.createCell(59).setCellValue("ambon");
            row0.createCell(60).setCellValue("kasus61");
            row0.createCell(61).setCellValue("ternate");
            row0.createCell(62).setCellValue("kasus70");
            row0.createCell(63).setCellValue("papua");
            row0.createCell(64).setCellValue("jumlah");
            row0.createCell(65).setCellValue("total");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("kasus10").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("aceh").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("kasus11").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("medan").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("kasus12").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("padang").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("kasus13").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("riau").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("kasus14").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("jambi").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("kasus15").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("palembang").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("kasus16").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("bengkulu").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("kasus17").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("lampung").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("kasus18").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("babel").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("kasus20").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("jakarta").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("kasus21").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("bandung").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("kasus22").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("semarang").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("kasus23").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("yogyakarta").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("kasus24").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("surabaya").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("kasus25").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("serang").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("kasus30").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("pontianak").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("kasus31").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("palangkaraya").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("kasus32").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("banjarmasin").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("kasus33").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("samarinda").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("kasus40").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("manado").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("kasus41").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("palu").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("kasus42").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("kendari").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("kasus43").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("makasar").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("kasus44").doubleValue());
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("mamuju").doubleValue());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("kasus45").doubleValue());
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("gorontalo").doubleValue());
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("kasus50").doubleValue());
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("denpasar").doubleValue());
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("kasus51").doubleValue());
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("mataram").doubleValue());
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("kasus52").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("kupang").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("kasus60").doubleValue());
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("ambon").doubleValue());
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("kasus61").doubleValue());
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("ternate").doubleValue());
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("kasus70").doubleValue());
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("papua").doubleValue());
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());
            row.createCell(65).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public InsuranceItemsView getInsItem(String stInsItem) {
        return (InsuranceItemsView) DTOPool.getInstance().getDTO(InsuranceItemsView.class, stInsItem);
    }

    public InsuranceClaimCauseView getClaimCause(String stClaimCause) {
        return (InsuranceClaimCauseView) DTOPool.getInstance().getDTO(InsuranceClaimCauseView.class, stClaimCause);
    }

    public String getStIndex() {
        return stIndex;
    }

    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
    }

    public DTOList EXCEL_KLAIM_PA_KREASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_id,a.cc_code as daerah,a.dla_no as nomor_lkp,a.pol_no as no_polis,a.cust_name as nama,a.pol_type_id as bisnis,a.insured_amount as tsi "
                + " ,i.claim_amount as nilai_klaim,b.refd2 as tgl_awal,b.refd3 as tgl_akhir, b.ref2 as usia,b.refd1 as tgllahir,a.claim_cause as kdkeja,           "
                + " (select cause_desc from ins_clm_cause x where x.ins_clm_caus_id = a.claim_cause) as cause_desc , a.claim_date as tglklaim,"
                + "case when l.entity_id != 1 and l.amount != null then  l.amount else 0 end as koas ,"
                + " case when l.entity_id  = 1 then  l.amount else 0 end as retensi  ");

        sqa.addQuery(" from ins_policy a"
                + " inner join ins_pol_obj b on b.ins_pol_obj_id=a.claim_object_id"
                + " inner join ins_policy_types f on f.pol_type_id =a.pol_type_id"
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ent_master k on k.ent_id = a.entity_id "
                + " inner join ins_pol_coins l on l.policy_id = a.pol_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')  ");
        sqa.addClause(" a.claim_status IN ('DLA') ");
        sqa.addClause(" a.effective_flag='Y' ");
        sqa.addClause(" j.treaty_type ='OR' ");
        sqa.addClause(" a.pol_type_id = '21' ");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" l.coins_type ='COINS_COVER' ");
        sqa.addClause(" l.claim_amt <> 0 ");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        final String sql = sqa.getSQL() + " order by a.pol_no asc ";
//                " group by a.claim_date,a.cust_name,a.cc_code,a.period_start,i.claim_amount,a.pol_type_id,a.insured_amount,a.period_end,"
//                + " b.refd2,b.refd3, a.pol_id,a.pol_no,a.dla_no,substr(a.period_start::text,1,4),a.status,b.ref1,b.ref2,a.claim_cause"


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_PA_KREASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        //bikin header
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("DATA KLAIM PA KREASI");

        XSSFRow row01 = sheet.createRow(1);
        row01.createCell(0).setCellValue("PERIODE " + DateUtil.getDateStr(getDLADateFrom()) + " s/d " + DateUtil.getDateStr(getDLADateTo()));

        XSSFRow row1 = sheet.createRow(3);
        row1.createCell(0).setCellValue("PDPOL");
        row1.createCell(1).setCellValue("DAERAH");
        row1.createCell(2).setCellValue("NOMOR LKP");
        row1.createCell(3).setCellValue("NOMOR POLIS");
        row1.createCell(4).setCellValue("NAMA");
        row1.createCell(5).setCellValue("BISNIS");
        row1.createCell(6).setCellValue("TSI");
        row1.createCell(7).setCellValue("NILAI KLAIM");
        row1.createCell(8).setCellValue("TGL AWAL");
        row1.createCell(9).setCellValue("TGL AKHIR");
        row1.createCell(10).setCellValue("USIA");
        row1.createCell(11).setCellValue("TGL LAHIR");
        row1.createCell(12).setCellValue("KOAS");
        row1.createCell(13).setCellValue("TGL LKP");
        row1.createCell(14).setCellValue("KD KEJA");
        row1.createCell(15).setCellValue("KET KEJA");
        row1.createCell(16).setCellValue("TGL KLAIM");
        row1.createCell(17).setCellValue("RETENSI SENDIRI");
        row1.createCell(18).setCellValue("SISTEM REASURANSI");
        row1.createCell(19).setCellValue("KOMISI REASURANSI");

        System.out.println(list.size() + " <--------------------------------------------------------------- x");
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);
            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST(""));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("daerah") != null ? h.getFieldValueByFieldNameST("daerah") : "");
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("nomor_lkp") != null ? h.getFieldValueByFieldNameST("nomor_lkp") : "");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("no_polis") != null ? h.getFieldValueByFieldNameST("no_polis") : "");
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nama") != null ? h.getFieldValueByFieldNameST("nama") : "");
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("bisnis").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("nilai_klaim").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tgl_awal") != null ? h.getFieldValueByFieldNameDT("tgl_awal") : new Date());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameDT("tgl_akhir") != null ? h.getFieldValueByFieldNameDT("tgl_akhir") : new Date());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("usia") != null ? h.getFieldValueByFieldNameST("usia") : "");
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("tgllahir") != null ? h.getFieldValueByFieldNameDT("tgllahir") : new Date());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("koas").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST(""));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("kdkeja").doubleValue());
            row.createCell(15).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameDT("tglklaim"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("retensi").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameST(""));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameST(""));


        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_PORTOFOLIO_PA_KREASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_id,a.cc_code as daerah,a.dla_no as nomor_lkp,a.pol_no as no_polis,a.cust_name as nama,a.pol_type_id as bisnis,a.insured_amount as tsi "
                + " ,i.claim_amount as nilai_klaim,b.refd2 as tgl_awal,b.refd3 as tgl_akhir, b.ref2 as usia,b.refd1 as tgllahir          "
                + "  , a.claim_date as tglklaim ,b.refn1 as koas,"
                + " b.premi_total_bcoins as premifull");

        sqa.addQuery(" from ins_policy a"
                + " inner join ins_pol_obj b on b.pol_id=a.pol_id"
                + " inner join ins_policy_types f on f.pol_type_id =a.pol_type_id"
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ent_master k on k.ent_id = a.entity_id ");

        sqa.addClause(" a.status IN ('POLICY','RENEWAL')  ");
//         sqa.addClause(" a.claim_status IN ('DLA') ");
        sqa.addClause(" a.effective_flag='Y' ");
//            sqa.addClause(" j.treaty_type ='OR' ");
        sqa.addClause(" a.pol_type_id = '21' ");
//        sqa.addClause(" a.active_flag='Y'");
//        sqa.addClause(" l.coins_type ='COINS_COVER' ");
//            sqa.addClause(" l.claim_amt <> 0 ");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        final String sql = sqa.getSQL() + " order by a.pol_no asc ";
//                " group by a.claim_date,a.cust_name,a.cc_code,a.period_start,i.claim_amount,a.pol_type_id,a.insured_amount,a.period_end,"
//                + " b.refd2,b.refd3, a.pol_id,a.pol_no,a.dla_no,substr(a.period_start::text,1,4),a.status,b.ref1,b.ref2,a.claim_cause"


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_PORTOFOLIO_PA_KREASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        //bikin header

        XSSFRow row0 = sheet.createRow(0);

        row0.createCell(8).setCellValue("DATA PORTOFOLIO PA KREASI");

        XSSFRow row01 = sheet.createRow(1);
        row01.createCell(8).setCellValue("PERIODE " + DateUtil.getDateStr(policyDateFrom) + " s/d " + DateUtil.getDateStr(policyDateTo));

        XSSFRow row1 = sheet.createRow(3);
        row1.createCell(0).setCellValue("PDPOL");
        row1.createCell(1).setCellValue("DAERAH");
        row1.createCell(2).setCellValue("NOMOR LKP");
        row1.createCell(3).setCellValue("NOMOR POLIS");
        row1.createCell(4).setCellValue("NAMA");
        row1.createCell(5).setCellValue("BISNIS");
        row1.createCell(6).setCellValue("TSI");
        row1.createCell(7).setCellValue("PREMI 100%");
        row1.createCell(8).setCellValue("TGL AWAL");
        row1.createCell(9).setCellValue("TGL AKHIR");
        row1.createCell(10).setCellValue("USIA");
        row1.createCell(11).setCellValue("TGL LAHIR");
        row1.createCell(12).setCellValue("KOAS");
        row1.createCell(13).setCellValue("RETENSI SENDIRI");
        row1.createCell(14).setCellValue("SISTEM REASURANSI");
        row1.createCell(15).setCellValue("KOMISI REASURANSI");

        System.out.println(list.size() + " <--------------------------------------------------------------- x");
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);
            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST(""));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("daerah") != null ? h.getFieldValueByFieldNameST("daerah") : "");
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("nomor_lkp") != null ? h.getFieldValueByFieldNameST("nomor_lkp") : "");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("no_polis") != null ? h.getFieldValueByFieldNameST("no_polis") : "");
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nama") != null ? h.getFieldValueByFieldNameST("nama") : "");
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("bisnis").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premifull").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tgl_awal") != null ? h.getFieldValueByFieldNameDT("tgl_awal") : new Date());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameDT("tgl_akhir") != null ? h.getFieldValueByFieldNameDT("tgl_akhir") : new Date());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("usia") != null ? h.getFieldValueByFieldNameST("usia") : "");
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("tgllahir") != null ? h.getFieldValueByFieldNameDT("tgllahir") : new Date());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("koas").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("retensi").doubleValue());


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
                + " coalesce(count(b.dla_no),0) as jumlah,"
                + "coalesce(sum(b.claim_amount_est),0) as claim_amount_est, "
                + "coalesce(sum(b.claim_amount),0) as claim_amount");

        String sql = "left join ( "
                + " select e.ins_policy_type_grp_id2,a.dla_no, "
                + " sum(getpremi(a.status = 'ENDORSE',getpremi(a.insured_amount_e < 0,(a.insured_amount_e*a.ccy_rate)*-1,a.insured_amount_e*a.ccy_rate),a.insured_amount*a.ccy_rate)) as insured_amount, "
                + " sum(a.claim_amount_est*a.ccy_rate_claim) as claim_amount_est, "
                + " sum(a.claim_amount*a.ccy_rate_claim) as claim_amount "
                + " from ins_policy a "
                + " left join ins_policy_types e on e.pol_type_id = a.pol_type_id "
                + " where a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + " and a.active_flag='Y' and a.claim_effective_flag = 'Y' and a.effective_flag = 'Y' ";

        if (getAppDateFrom() != null) {
            sql = sql + " and date_trunc('day',a.approved_date) >= ?";
            sqa.addPar(appDateFrom);
        }

        if (getAppDateTo() != null) {
            sql = sql + " and date_trunc('day',a.approved_date) <= ?";
            sqa.addPar(appDateTo);
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

        sqa.addQuery(" from band_level a " + sql + " group by e.ins_policy_type_grp_id2,a.dla_no "
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
            if (getStGroupName() != null) {
                row0.createCell(0).setCellValue("RISK AND LOSS PROFILES OF " + getStGroupName().toUpperCase() + " INSURANCE");
            }

            //bikin header
            XSSFRow row2 = sheet.createRow(2);
            if (getAppDateFrom() != null) {
                row2.createCell(0).setCellValue("APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " TO " + DateUtil.getDateStr(getAppDateTo()));
            }

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
            row5.createCell(1).setCellValue("CLAIM");

            //bikin header
            XSSFRow row6 = sheet.createRow(7);
            row6.createCell(0).setCellValue("RISK BASIS");
            row6.createCell(1).setCellValue("IN AMOUNT");
            row6.createCell(2).setCellValue("NO");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 8);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4") + " - " + h.getFieldValueByFieldNameST("ref5"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
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

    public DTOList EXCEL_JENIS_UW() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        int taon = Integer.parseInt(DateUtil.getYear(periodFrom));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;


        sqa.addSelect("a.pol_type_id,a.short_desc, "
                + " coalesce(sum(b.claim_no),0) as jumlah_claim, coalesce(sum(b.claim_total),0) as claim_total, "
                + " coalesce(sum(b.claim_no2),0) as jumlah_claim2, coalesce(sum(b.claim_total2),0) as claim_total2, "
                + " coalesce(sum(b.claim_no3),0) as jumlah_claim3, coalesce(sum(b.claim_total3),0) as claim_total3, "
                + " coalesce(sum(b.claim_no4),0) as jumlah_claim4, coalesce(sum(b.claim_total4),0) as claim_total4, "
                + " coalesce(sum(b.claim_no5),0) as jumlah_claim5, coalesce(sum(b.claim_total5),0) as claim_total5 ");

        String sql = " left join ( select a.pol_type_id,a.pol_id,a.pol_no,a.dla_no, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end as claim_no, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon1 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end as claim_no2, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA'"
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon2 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total2, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end as claim_no3, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon3 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total3, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then 1 else 0 end as claim_no4 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodFrom) + "-" + DateUtil.getDays(periodFrom));
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(taon4 + "-" + DateUtil.getMonth2Digit(periodTo) + "-" + DateUtil.getDays(periodTo));
        }
        sql = sql + " then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total4, "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then 1 else 0 end as claim_no5 , "
                + " case when a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA' "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + " then sum(a.claim_amount*a.ccy_rate_claim) else 0 end as claim_total5 "
                + " from ins_policy a ";

//       if (getStBranch()!=null) {
//            sql = sql + " and a.cc_code = ?";
//            sqa.addPar(stBranch);
//        }

        sql = sql + " where a.active_flag = 'Y' and a.effective_flag = 'Y'";

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = ?";
            sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            sql = sql + " and a.region_id = ?";
            sqa.addPar(stRegion);
        }

        sqa.addQuery(" from ins_policy_types a " + sql + " group by a.pol_type_id,a.pol_id,a.pol_no,a.period_start,a.claim_approved_date,a.status,a.claim_status,a.claim_amount,a.ccy_rate_claim,a.dla_no "
                + " ) as b on b.pol_type_id = a.pol_type_id ");

        sql = sqa.getSQL() + " group by a.pol_type_id,a.short_desc order by a.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_JENIS_UW() throws Exception {


        int taon = Integer.parseInt(DateUtil.getYear(periodFrom));
        int taon1 = taon - 1;
        int taon2 = taon - 2;
        int taon3 = taon - 3;
        int taon4 = taon - 4;

        BigDecimal jumlah_claim = BigDecimal.ZERO;
        BigDecimal claim_total = BigDecimal.ZERO;

        BigDecimal jumlah_claim2 = BigDecimal.ZERO;
        BigDecimal claim_total2 = BigDecimal.ZERO;

        BigDecimal jumlah_claim3 = BigDecimal.ZERO;
        BigDecimal claim_total3 = BigDecimal.ZERO;

        BigDecimal jumlah_claim4 = BigDecimal.ZERO;
        BigDecimal claim_total4 = BigDecimal.ZERO;

        BigDecimal jumlah_claim5 = BigDecimal.ZERO;
        BigDecimal claim_total5 = BigDecimal.ZERO;

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        //bikin header
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("CLAIM OF TYPE INSURANCE PER UNDERWRITING YEAR");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("CLAIM APPROVED DATE : " + DateUtil.getDateStr(getAppDateFrom()) + " s/d " + DateUtil.getDateStr(getAppDateTo()));

        //bikin header
        XSSFRow row5 = sheet.createRow(3);
        row5.createCell(0).setCellValue("TYPE ID");
        row5.createCell(1).setCellValue("COB");
        row5.createCell(2).setCellValue("JUMLAH");
        row5.createCell(3).setCellValue("CLAIM PERIOD " + taon4);
        row5.createCell(4).setCellValue("JUMLAH");
        row5.createCell(5).setCellValue("CLAIM PERIOD " + taon3);
        row5.createCell(6).setCellValue("JUMLAH");
        row5.createCell(7).setCellValue("CLAIM PERIOD " + taon2);
        row5.createCell(8).setCellValue("JUMLAH");
        row5.createCell(9).setCellValue("CLAIM PERIOD " + taon1);
        row5.createCell(10).setCellValue("JUMLAH");
        row5.createCell(11).setCellValue("CLAIM PERIOD " + taon);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("short_desc"));

            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4").doubleValue() : 0);
            jumlah_claim4 = jumlah_claim4.add(h.getFieldValueByFieldNameBD("jumlah_claim4") != null ? h.getFieldValueByFieldNameBD("jumlah_claim4") : BigDecimal.ZERO);

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4").doubleValue() : 0);
            claim_total4 = claim_total4.add(h.getFieldValueByFieldNameBD("claim_total4") != null ? h.getFieldValueByFieldNameBD("claim_total4") : BigDecimal.ZERO);

            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3").doubleValue() : 0);
            jumlah_claim3 = jumlah_claim3.add(h.getFieldValueByFieldNameBD("jumlah_claim3") != null ? h.getFieldValueByFieldNameBD("jumlah_claim3") : BigDecimal.ZERO);

            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3").doubleValue() : 0);
            claim_total3 = claim_total3.add(h.getFieldValueByFieldNameBD("claim_total3") != null ? h.getFieldValueByFieldNameBD("claim_total3") : BigDecimal.ZERO);

            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2").doubleValue() : 0);
            jumlah_claim2 = jumlah_claim2.add(h.getFieldValueByFieldNameBD("jumlah_claim2") != null ? h.getFieldValueByFieldNameBD("jumlah_claim2") : BigDecimal.ZERO);

            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2").doubleValue() : 0);
            claim_total2 = claim_total2.add(h.getFieldValueByFieldNameBD("claim_total2") != null ? h.getFieldValueByFieldNameBD("claim_total2") : BigDecimal.ZERO);

            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim").doubleValue() : 0);
            jumlah_claim = jumlah_claim.add(h.getFieldValueByFieldNameBD("jumlah_claim") != null ? h.getFieldValueByFieldNameBD("jumlah_claim") : BigDecimal.ZERO);

            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total").doubleValue() : 0);
            claim_total = claim_total.add(h.getFieldValueByFieldNameBD("claim_total") != null ? h.getFieldValueByFieldNameBD("claim_total") : BigDecimal.ZERO);

            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5").doubleValue() : 0);
            jumlah_claim5 = jumlah_claim5.add(h.getFieldValueByFieldNameBD("jumlah_claim5") != null ? h.getFieldValueByFieldNameBD("jumlah_claim5") : BigDecimal.ZERO);

            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5").doubleValue() : 0);
            claim_total5 = claim_total5.add(h.getFieldValueByFieldNameBD("claim_total5") != null ? h.getFieldValueByFieldNameBD("claim_total5") : BigDecimal.ZERO);
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

    public DTOList EXCEL_KLAIM_AKUNTANSI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,b.description as jenpol,a.cc_code as koda,d.description as cabang,a.pol_id,a.dla_no,a.pol_no,e.ent_name,"
                + "a.cover_type_code,a.claim_approved_date,getname(a.ins_policy_type_grp_id=10,a.claim_client_name,g.ref1) as prod_name, "
                + "sum(checkreas(c.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as bebanklaim,"
                + "sum(checkreas(c.ins_item_id in (48,49,53,73),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as recoveryklaim,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as uwlain,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as adjfee,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as ppn,"
                + "(select l.description "
                + "from ins_pol_items l "
                + "where l.pol_id = a.pol_id and l.ins_item_id in (50) group by l.description) as nama_adjfee,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as jasabengkel,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code in (10,12),c.tax_amount*a.ccy_rate_claim)) as pph_jasabengkel,"
                + "sum(checkreas(c.ins_item_id in (75),c.amount*a.ccy_rate_claim)) as join_plcmnt,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as survey,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as ppn_total,"
                + "sum(checkreas(c.ins_item_id in (79),c.amount*a.ccy_rate_claim)) as cash_clltrl,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as material,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as surveyadjfee,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as expensesfee,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as vatfee, "
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as materai ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id"
                + " inner join ins_pol_items c on c.pol_id = a.pol_id"
                + " inner join ins_pol_obj g on g.ins_pol_obj_id = a.claim_object_id"
                + " inner join ins_items f on f.ins_item_id = c.ins_item_id"
                + " inner join gl_cost_center d on d.cc_code = a.cc_code"
                + " inner join ent_master e on e.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = " select a.pol_type_id,a.jenpol,a.koda,a.cabang,a.dla_no,a.pol_id,a.pol_no,a.ent_name,a.prod_name,a.claim_approved_date, "
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,bebanklaim,0) as bebanklaim,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,recoveryklaim,0) as recoveryklaim,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,uwlain,0) as uwlain,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppn,0) as ppn,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,adjfee,0) as adjfee,"
                + "getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,nama_adjfee) as nama_adjfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,jasabengkel,0) as jasabengkel,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,pph_jasabengkel,0) as pph_jasabengkel,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,join_plcmnt,0) as join_plcmnt,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,survey,0) as survey,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppn_total,0) as ppn_total,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,cash_clltrl,0) as cash_clltrl,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,material,0) as material,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,surveyadjfee,0) as surveyadjfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,expensesfee,0) as expensesfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,vatfee,0) as vatfee, "
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,materai,0) as materai, "
                + "getname(a.cover_type_code in ('COINSOUT','COINSOUTSELF') and b.claim_amt <> 0 and b.entity_id <> 1,c.short_name,null) as coins,"
                + "getpremi(b.claim_amt = 0 or b.entity_id in (1,2000,2001),null,b.claim_amt) as coins_amount,b.coins_type "
                + " from ( " + sqa.getSQL() + " group by a.pol_type_id,b.description,a.cc_code,d.description,a.pol_id,a.dla_no,a.pol_no,e.ent_name,a.claim_client_name,g.ref1,a.cover_type_code,a.claim_approved_date "
                + " order by a.cc_code,a.dla_no ) a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ent_master c on c.ent_id = b.entity_id "
                //+ " where b.position_code = 'MEM' "
                + " order by a.koda,a.pol_type_id,a.dla_no,b.coins_type desc,b.entity_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_AKUNTANSI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("poltype");
            row0.createCell(1).setCellValue("jenpol");
            row0.createCell(2).setCellValue("koda");
            row0.createCell(3).setCellValue("cabang");
            row0.createCell(4).setCellValue("tgl setujui");
            row0.createCell(5).setCellValue("dla no");
            row0.createCell(6).setCellValue("pol id");
            row0.createCell(7).setCellValue("no polis");
            row0.createCell(8).setCellValue("nama");
            row0.createCell(9).setCellValue("tertanggung");
            row0.createCell(10).setCellValue("beban klaim");
            row0.createCell(11).setCellValue("recovery klaim");
            row0.createCell(12).setCellValue("uw lain");
            row0.createCell(13).setCellValue("ppn");
            row0.createCell(14).setCellValue("adjuster fee");
            row0.createCell(15).setCellValue("nama adjuster fee");
            row0.createCell(16).setCellValue("jasa bengkel");
            row0.createCell(17).setCellValue("pajak bengkel");
            row0.createCell(18).setCellValue("join placement");
            row0.createCell(19).setCellValue("survey");
            row0.createCell(20).setCellValue("ppn total");
            row0.createCell(21).setCellValue("cash colleteral");
            row0.createCell(22).setCellValue("material");
            row0.createCell(23).setCellValue("surveyadjfee");
            row0.createCell(24).setCellValue("expensesfee");
            row0.createCell(25).setCellValue("vatfee");
            row0.createCell(26).setCellValue("materai");
            row0.createCell(27).setCellValue("nilai koas");
            row0.createCell(28).setCellValue("koasuradur");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jenpol")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("bebanklaim").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("recoveryklaim").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("uwlain").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("adjfee").doubleValue());
            if (h.getFieldValueByFieldNameST("nama_adjfee") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("nama_adjfee"));
            }
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("pph_jasabengkel").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("join_plcmnt").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("survey").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("ppn_total").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("cash_clltrl").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("surveyadjfee").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("expensesfee").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("vatfee").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("materai").doubleValue());
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("coins") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameST("coins"));
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList KLAIM_AKUNTANSI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,b.short_desc as ref1,a.cc_code,d.description as ref2,a.pol_id,a.dla_no,a.pol_no,"
                + "e.ent_name as cust_name,getname(a.ins_policy_type_grp_id=10,a.claim_client_name,g.ref1) as prod_name,a.cover_type_code,a.claim_approved_date,"
                + "sum(checkreas(c.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as nd_comm1,"
                + "sum(checkreas(c.ins_item_id in (48,49,53,73,75,79),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as nd_comm2,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as nd_comm3,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as nd_comm4,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as nd_ppn,"
                + "(select l.description "
                + "from ins_pol_items l "
                + "where l.pol_id = a.pol_id and l.ins_item_id in (50,77) group by l.description) as ref3,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as nd_brok1,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code in (10,12),c.tax_amount*a.ccy_rate_claim)) as nd_brok2,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount)) as refn1,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount)) as refn2,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount)) as refn3,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as nd_premirate1,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as nd_premirate2,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as nd_premirate3, "
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as nd_premirate4 ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id"
                + " inner join ins_pol_items c on c.pol_id = a.pol_id"
                + " inner join ins_pol_obj g on g.ins_pol_obj_id = a.claim_object_id"
                + " inner join ins_items f on f.ins_item_id = c.ins_item_id"
                + " inner join gl_cost_center d on d.cc_code = a.cc_code"
                + " inner join ent_master e on e.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = " select a.dla_no||a.nd_comm1 as sppa_no,a.* from ( "
                + " select a.pol_type_id,a.ref1,a.cc_code,a.ref2,a.dla_no,a.pol_id,a.pol_no,a.cust_name,a.prod_name,a.claim_approved_date,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm1,0) as nd_comm1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm2,0) as nd_comm2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm3,0) as nd_comm3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm4,0) as nd_comm4,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_ppn,0) as nd_ppn,"
                + "getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.ref3) as ref3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_brok1,0) as nd_brok1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_brok2,0) as nd_brok2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn1,0) as refn1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn2,0) as refn2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn3,0) as refn3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate1,0) as nd_premirate1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate2,0) as nd_premirate2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate3,0) as nd_premirate3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate4,0) as nd_premirate4,"
                + "getname(a.cover_type_code in ('COINSOUT','COINSOUTSELF') and b.claim_amt <> 0 and b.entity_id <> 1,c.short_name,null) as coinsurer_name,"
                + "getpremi(b.claim_amt = 0 or b.entity_id in (1,2000,2001),null,b.claim_amt) as premi_base "
                + " from ( " + sqa.getSQL() + " group by a.pol_type_id,b.short_desc,a.cc_code,d.description,a.pol_id,a.dla_no,a.pol_no,e.ent_name,a.claim_client_name,g.ref1,a.cover_type_code,a.claim_approved_date "
                + " order by a.cc_code,a.dla_no ) a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ent_master c on c.ent_id = b.entity_id "
                //+ " where b.position_code = 'MEM' "
                + " order by a.cc_code,a.pol_type_id,a.dla_no,b.coins_type desc "
                + " ) a order by a.cc_code,a.pol_type_id,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        return l;
    }

    public DTOList EXCEL_KLAIM_AKUNTANSI_REKAP() throws Exception {
        final boolean CABANG = "Y".equalsIgnoreCase((String) refPropMap.get("CABANG"));
        final boolean JENPOL = "Y".equalsIgnoreCase((String) refPropMap.get("JENPOL"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,b.description as jenpol,a.cc_code as koda,d.description as cabang,a.pol_id,a.dla_no,a.pol_no,e.ent_name,a.cover_type_code,"
                + "sum(checkreas(c.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as bebanklaim,"
                + "sum(checkreas(c.ins_item_id in (48,49,53,73),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as recoveryklaim,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as uwlain,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as ppn,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as adjfee,"
                + "(select l.description "
                + "from ins_pol_items l "
                + "where l.pol_id = a.pol_id and l.ins_item_id in (50,77) group by l.description) as nama_adjfee,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as jasabengkel,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code in (10,12),c.tax_amount*a.ccy_rate_claim)) as pph_jasabengkel,"
                + "sum(checkreas(c.ins_item_id in (75),((c.amount*-1)*a.ccy_rate_claim))) as join_plcmnt,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as survey,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as ppn_total,"
                + "sum(checkreas(c.ins_item_id in (79),((c.amount*-1)*a.ccy_rate_claim))) as cash_clltrl,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as material,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as surveyadjfee,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as expensesfee,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as vatfee,"
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as materai ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id"
                + " inner join ins_pol_items c on c.pol_id = a.pol_id"
                + " inner join ins_items f on f.ins_item_id = c.ins_item_id"
                + " inner join gl_cost_center d on d.cc_code = a.cc_code"
                + " inner join ent_master e on e.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        String rekap = null;
        String order = null;
        if (CABANG) {
            rekap = "select a.koda,a.cabang,";
            order = "a.koda,a.cabang";
        }
        if (JENPOL) {
            rekap = "select a.pol_type_id,a.jenpol,";
            order = "a.pol_type_id,a.jenpol";
        }

        final String sql = rekap
                + " sum(bebanklaim) as bebanklaim,sum(recoveryklaim) as recoveryklaim,"
                + " sum(uwlain) as uwlain,sum(ppn) as ppn,sum(adjfee) as adjfee,nama_adjfee,"
                + " sum(jasabengkel) as jasabengkel,sum(pph_jasabengkel) as pph_jasabengkel,"
                + " sum(join_plcmnt) as join_plcmnt,sum(survey) as survey,sum(materai) as materai,"
                + " sum(ppn_total) as ppn_total,sum(cash_clltrl) as cash_clltrl,sum(material) as material,"
                + " sum(surveyadjfee) as surveyadjfee,sum(expensesfee) as expensesfee,sum(vatfee) as vatfee,"
                + " sum(coins_amount) as coins_amount,coins from ( "
                + " select a.pol_type_id,a.jenpol,a.koda,a.cabang,a.dla_no,a.pol_id,a.pol_no,a.ent_name,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,bebanklaim,0) as bebanklaim,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,recoveryklaim,0) as recoveryklaim,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,uwlain,0) as uwlain,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppn,0) as ppn,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,adjfee,0) as adjfee,"
                + "getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,nama_adjfee) as nama_adjfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,jasabengkel,0) as jasabengkel,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,pph_jasabengkel,0) as pph_jasabengkel,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,join_plcmnt,0) as join_plcmnt,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,survey,0) as survey,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppn_total,0) as ppn_total,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,cash_clltrl,0) as cash_clltrl,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,material,0) as material,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,surveyadjfee,0) as surveyadjfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,expensesfee,0) as expensesfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,vatfee,0) as vatfee,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.materai,0) as materai,"
                + "getname(a.cover_type_code in ('COINSOUT','COINSOUTSELF') and b.claim_amt <> 0 and b.entity_id <> 1,c.short_name,null) as coins,"
                + "getpremi(b.claim_amt = 0 or b.entity_id in (1,2000,2001),null,b.claim_amt) as coins_amount,b.coins_type "
                + " from ( " + sqa.getSQL() + " group by a.pol_type_id,b.description,a.cc_code,d.description,a.pol_id,a.dla_no,a.pol_no,e.ent_name,a.cover_type_code "
                + " order by a.cc_code,a.dla_no ) a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ent_master c on c.ent_id = b.entity_id "
                //+ " where b.position_code = 'MEM' "
                + " order by a.koda,a.pol_type_id,a.dla_no,b.coins_type desc,b.entity_id "
                + " ) a group by " + order + ",nama_adjfee,coins "
                + " order by " + order;

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_AKUNTANSI_REKAP() throws Exception {
        final boolean CABANG = "Y".equalsIgnoreCase((String) refPropMap.get("CABANG"));
        final boolean JENPOL = "Y".equalsIgnoreCase((String) refPropMap.get("JENPOL"));

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            if (CABANG) {
                row0.createCell(0).setCellValue("koda");
                row0.createCell(1).setCellValue("cabang");
            }
            if (JENPOL) {
                row0.createCell(0).setCellValue("poltype");
                row0.createCell(1).setCellValue("jenpol");
            }
            row0.createCell(2).setCellValue("beban klaim");
            row0.createCell(3).setCellValue("recovery klaim");
            row0.createCell(4).setCellValue("uw lain");
            row0.createCell(5).setCellValue("ppn");
            row0.createCell(6).setCellValue("adjuster fee");
            row0.createCell(7).setCellValue("nama adjuster fee");
            row0.createCell(8).setCellValue("jasa bengkel");
            row0.createCell(9).setCellValue("pajak bengkel");
            row0.createCell(10).setCellValue("join placement");
            row0.createCell(11).setCellValue("survey");
            row0.createCell(12).setCellValue("ppn total");
            row0.createCell(13).setCellValue("cash colleteral");
            row0.createCell(14).setCellValue("material");
            row0.createCell(15).setCellValue("surveyadjfee");
            row0.createCell(16).setCellValue("expensesfee");
            row0.createCell(17).setCellValue("vatfee");
            row0.createCell(18).setCellValue("materai");
            row0.createCell(19).setCellValue("nilai koas");
            row0.createCell(20).setCellValue("koasuradur");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            if (CABANG) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            }
            if (JENPOL) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
                row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jenpol")));
            }
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("bebanklaim").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("recoveryklaim").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("uwlain").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("adjfee").doubleValue());
            if (h.getFieldValueByFieldNameST("nama_adjfee") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("nama_adjfee"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pph_jasabengkel").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("join_plcmnt").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("survey").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("ppn_total").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("cash_clltrl").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("surveyadjfee").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("expensesfee").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("vatfee").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("materai").doubleValue());
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("coins") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("coins"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList KLAIM_AKUNTANSI_REKAP() throws Exception {
        final boolean CABANG = "Y".equalsIgnoreCase((String) refPropMap.get("CABANG"));
        final boolean JENPOL = "Y".equalsIgnoreCase((String) refPropMap.get("JENPOL"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,b.short_desc as ref1,a.cc_code,d.description as ref2,a.pol_id,a.dla_no,a.pol_no,e.ent_name as cust_name,a.cover_type_code,"
                + "sum(checkreas(c.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as nd_comm1,"
                + "sum(checkreas(c.ins_item_id in (48,49,53,73,75,79),((c.amount*(getpremi(f.negative_flag = 'Y',-1,1)))*a.ccy_rate_claim))) as nd_comm2,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as nd_comm3,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as nd_comm4,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as nd_ppn,"
                + "(select l.description "
                + "from ins_pol_items l "
                + "where l.pol_id = a.pol_id and l.ins_item_id in (50,77) group by l.description) as ref3,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as nd_brok1,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code in (10,12),c.tax_amount*a.ccy_rate_claim)) as nd_brok2,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as refn1,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as refn2,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as refn3,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as nd_premirate1,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as nd_premirate2,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as nd_premirate3,"
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as nd_premirate4 ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id"
                + " inner join ins_pol_items c on c.pol_id = a.pol_id"
                + " inner join ins_items f on f.ins_item_id = c.ins_item_id"
                + " inner join gl_cost_center d on d.cc_code = a.cc_code"
                + " inner join ent_master e on e.ent_id = a.entity_id");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        String rekap = null;
        String order = null;
        if (CABANG) {
            rekap = "select a.cc_code,a.ref2,";
            order = "a.cc_code,a.ref2";
        }
        if (JENPOL) {
            rekap = "select a.pol_type_id,a.ref1,";
            order = "a.pol_type_id,a.ref1";
        }

        final String sql = rekap
                + " sum(nd_comm1) as nd_comm1,sum(nd_comm2) as nd_comm2,"
                + " sum(nd_comm3) as nd_comm3,sum(nd_comm4) as nd_comm4,sum(nd_ppn) as nd_ppn,ref3,"
                + " sum(nd_brok1) as nd_brok1,sum(nd_brok2) as nd_brok2,"
                + " sum(refn1) as refn1,sum(refn2) as refn2,sum(refn3) as refn3,"
                + " sum(nd_premirate1) as nd_premirate1,sum(nd_premirate2) as nd_premirate2,sum(nd_premirate3) as nd_premirate3,sum(nd_premirate4) as nd_premirate4,"
                + " sum(premi_base) as premi_base,coinsurer_name from ( "
                + " select a.pol_type_id,a.ref1,a.cc_code,a.ref2,a.dla_no,a.pol_id,a.pol_no,a.cust_name,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm1,0) as nd_comm1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm2,0) as nd_comm2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm3,0) as nd_comm3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_comm4,0) as nd_comm4,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_ppn,0) as nd_ppn,"
                + "getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.ref3) as ref3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_brok1,0) as nd_brok1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_brok2,0) as nd_brok2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn1,0) as refn1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn2,0) as refn2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.refn3,0) as refn3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate1,0) as nd_premirate1,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate2,0) as nd_premirate2,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate3,0) as nd_premirate3,"
                + "getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,a.nd_premirate4,0) as nd_premirate4,"
                + "getname(a.cover_type_code in ('COINSOUT','COINSOUTSELF') and b.claim_amt <> 0 and b.entity_id <> 1,c.short_name,null) as coinsurer_name,"
                + "getpremi(b.claim_amt = 0 or b.entity_id in (1,2000,2001),null,b.claim_amt) as premi_base "
                + " from ( " + sqa.getSQL() + " group by a.pol_type_id,b.short_desc,a.cc_code,d.description,a.pol_id,a.dla_no,a.pol_no,e.ent_name,a.cover_type_code "
                + " order by a.cc_code,a.dla_no ) a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ent_master c on c.ent_id = b.entity_id "
                //+ " where b.position_code = 'MEM' "
                + " order by a.cc_code,a.pol_type_id,a.dla_no,b.coins_type desc "
                + " ) a group by " + order + ",ref3,coinsurer_name"
                + " order by " + order;

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        return l;
    }

    public DTOList RKP_OJK() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.lvl,a.ref4,a.ref5,"
                + "coalesce(count(b.dla_no)) as jumlah,"
                + "coalesce(sum(b.claim_amount),0) as claim_amount,"
                + "coalesce(sum(b.claim_paid),0) as claim_amount_est ");

        String sql = " left join ( select a.dla_no,coalesce((a.insured_amount*a.ccy_rate),0) as insured_amount, "
                + " (case when date_trunc('day',a.dla_date) >= ? "
                + " and date_trunc('day',a.dla_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then coalesce((a.claim_amount*a.ccy_rate_claim),0) else 0 end) as claim_amount, "
                + " (case when date_trunc('day',a.payment_date) >= ? "
                + " and date_trunc('day',a.payment_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        sql = sql + " then coalesce((a.claim_amount*a.ccy_rate_claim),0) else 0 end) as claim_paid ";

        sql = sql + " from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id "
                + " where a.status IN ('CLAIM','CLAIM ENDORSE') ";

        if (Tools.isYes(getStIndex())) {
            sql = sql + " and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
        }

        if (Tools.isNo(getStIndex())) {
            sql = sql + " and ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))";
        }

        if (getAppDateFrom() != null) {
            sql = sql + " and date_trunc('day',a.dla_date) >= ?";
            sqa.addPar(appDateFrom);
        }

        if (getAppDateTo() != null) {
            sql = sql + " and date_trunc('day',a.dla_date) <= ?";
            sqa.addPar(appDateTo);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = ?";
            sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            sql = sql + " and a.region_id = ?";
            sqa.addPar(stRegion);
        }

        if (getStGroupID() != null) {
            sql = sql + " and b.ins_policy_type_grp_id2 = ?";
            sqa.addPar(stGroupID);
        }

        sqa.addQuery(" from band_level a " + sql + " order by a.dla_date,a.pol_no "
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        sqa.addClause(" a.group_desc = 'OJK'");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 order by a.lvl ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_RKP_OJK_OLD() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.lvl as no,a.ref4 as a,a.ref5 as b,"
                + "coalesce(count(b.dla_no)) as jumlah_polis,"
                + "coalesce(sum(b.claim_amount),0) as klaim_disetujui,"
                + "coalesce(sum(b.claim_year_paid),0) as klaim_tahun_berjalan_dibayar, "
                + "coalesce(sum(b.claim_paid),0) as klaim_dibayar ");

        String sql = " left join ( select a.dla_no,coalesce((a.insured_amount*a.ccy_rate),0) as insured_amount, "
                + " (case when date_trunc('day',a.approved_date) >= '" + appDateFrom + "'"
                + " and date_trunc('day',a.approved_date) <= '" + appDateTo + "'";
        //sqa.addPar(appDateFrom);
        //sqa.addPar(appDateTo);
        sql = sql + " then coalesce((a.claim_amount*a.ccy_rate_claim),0) else 0 end) as claim_amount, "
                + " (case when date_trunc('day',a.payment_date) >= '" + appDateFrom + "'"
                + " and date_trunc('day',a.payment_date) <= '" + appDateTo + "'";
        //sqa.addPar(appDateFrom);
        //sqa.addPar(appDateTo);
        sql = sql + " then coalesce((a.claim_amount*a.ccy_rate_claim),0) else 0 end) as claim_paid, "
                + " (case when date_trunc('day',a.approved_date) >= '" + appDateFrom + "'"
                + " and date_trunc('day',a.approved_date) <= '" + appDateTo + "'"
                + " and date_trunc('day',a.payment_date) >= '" + appDateFrom + "'"
                + " and date_trunc('day',a.payment_date) <= '" + appDateTo + "'";
        //sqa.addPar(appDateFrom);
        //sqa.addPar(appDateTo);
        sql = sql + " then coalesce((a.claim_amount*a.ccy_rate_claim),0) else 0 end) as claim_year_paid ";

        sql = sql + " from ins_policy a "
                + " inner join ins_policy_types b on b.pol_type_id = a.pol_type_id "
                + " where a.status IN ('CLAIM','CLAIM ENDORSE') ";

        if (Tools.isYes(getStIndex())) {
            sql = sql + " and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
        }

        if (Tools.isNo(getStIndex())) {
            sql = sql + " and ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))";
        }
        /*
        if (getAppDateFrom() != null) {
        sql = sql + " and date_trunc('day',a.approved_date) >= '" + appDateFrom + "'";
        //sqa.addPar(appDateFrom);
        }

        if (getAppDateTo() != null) {
        sql = sql + " and date_trunc('day',a.approved_date) <= '" + appDateTo + "'";
        sqa.addPar(appDateTo);
        }
         */

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = '" + stBranch + "'";
            //sqa.addPar(stBranch);
        }

        if (getStRegion() != null) {
            sql = sql + " and a.region_id = " + stRegion;
            //sqa.addPar(stRegion);
        }

        if (getStGroupID() != null) {
            sql = sql + " and b.ins_policy_type_grp_id2 = " + stGroupID;
            //sqa.addPar(stGroupID);
        }

        sqa.addQuery(" from band_level a " + sql + " order by a.dla_date,a.pol_no "
                + " ) as b on b.insured_amount between a.ref1 and a.ref2 ");

        sqa.addClause(" a.group_desc = 'OJK'");

        sql = sqa.getSQL() + " group by a.lvl,a.ref4,a.ref5 ";

        sql = sql + " order by a.lvl";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_OJK_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCEL_KLAIM_AKUNTANSI_OUTWARD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code,c.short_desc as cob,a.pol_id,a.pol_no,a.dla_no,a.claim_date,a.dla_date,e.ent_name,a.claim_cause_desc,"
                + "b.ref1 as nama,b.refd1 as tgl_lahir,a.policy_date,coalesce(b.refd2,a.period_start) as periode_awal,coalesce(b.refd3,a.period_end) as periode_akhir,"
                + "b.ref2 as usia,substr(a.period_start::text,1,4) as uy,a.ccy,a.ccy_rate,a.ccy_rate_claim,b.insured_amount,a.claim_amount,"
                + "j.treaty_type,i.member_ent_id,f.short_name,sum(i.claim_amount) as claimri_amount, "
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.claim_amount*a.ccy_rate_claim)),2) as claim_bppdan, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.claim_amount*a.ccy_rate_claim)),2) as claim_spl, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.claim_amount*a.ccy_rate_claim)),2) as claim_fac, "
                + "round(sum(checkreas(j.treaty_type='QS',i.claim_amount*a.ccy_rate_claim)),2) as claim_qs, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.claim_amount*a.ccy_rate_claim)),2) as claim_park, "
                + "round(sum(checkreas(j.treaty_type in ('FACO','FACO1','FACO2','FACO3'),i.claim_amount*a.ccy_rate_claim)),2) as claim_faco, "
                + "round(sum(checkreas(j.treaty_type='JP',i.claim_amount*a.ccy_rate_claim)),2) as claim_jp, "
                + "round(sum(checkreas(j.treaty_type='QSKR',i.claim_amount*a.ccy_rate_claim)),2) as claim_qskr, "
                + "round(sum(checkreas(j.treaty_type='KSCBI',i.claim_amount*a.ccy_rate_claim)),2) as claim_kscbi, "
                + "round(sum(checkreas(j.treaty_type in ('XOL1','XOL2','XOL3','XOL4','XOL5'),i.claim_amount*a.ccy_rate_claim)),2) as claim_xol1 ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = "select a.cc_code,a.cob,a.uy,a.periode_awal,a.periode_akhir,a.policy_date,a.claim_date,a.dla_date,"
                + "a.pol_id,a.pol_no,getzone3(a.treaty_type = 'OR',a.dla_no) as dla_no,a.ent_name,a.claim_cause_desc,a.nama,a.tgl_lahir,a.usia,a.ccy,a.ccy_rate,a.ccy_rate_claim,"
                + "getpremi(a.treaty_type = 'OR',a.insured_amount,0) as insured_amount,"
                + "getpremi(a.treaty_type = 'OR',a.bebanklaim,0) as bebanklaim,"
                + "getpremi(a.treaty_type = 'OR',a.recoveryklaim,0) as recoveryklaim,"
                + "getpremi(a.treaty_type = 'OR',a.uwlain,0) as uwlain,"
                + "getpremi(a.treaty_type = 'OR',a.adjfee,0) as adjfee,"
                + "getpremi(a.treaty_type = 'OR',a.jasabengkel,0) as jasabengkel,"
                + "getpremi(a.treaty_type = 'OR',a.pph_jasabengkel,0) as pph_jasabengkel,"
                + "getpremi(a.treaty_type = 'OR',a.ppn,0) as ppn,"
                + "getpremi(a.treaty_type = 'OR',a.join_plcmnt,0) as join_plcmnt,"
                + "getpremi(a.treaty_type = 'OR',a.survey,0) as survey,"
                + "getpremi(a.treaty_type = 'OR',a.ppn_total,0) as ppn_total,"
                + "getpremi(a.treaty_type = 'OR',a.cash_clltrl,0) as cash_clltrl,"
                + "getpremi(a.treaty_type = 'OR',a.material,0) as material,"
                + "getpremi(a.treaty_type = 'OR',a.surveyadjfee,0) as surveyadjfee,"
                + "getpremi(a.treaty_type = 'OR',a.expensesfee,0) as expensesfee,"
                + "getpremi(a.treaty_type = 'OR',a.vatfee,0) as vatfee,"
                + "getpremi(a.treaty_type = 'OR',a.materai,0) as materai,"
                + "a.treaty_type,a.member_ent_id,a.short_name,"
                + "claim_or,claim_bppdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 from ( "
                + "select a.cc_code,a.cob,a.pol_id,a.pol_no,a.dla_no,a.claim_date,a.dla_date,a.ent_name,a.claim_cause_desc,"
                + "a.nama,a.tgl_lahir,a.policy_date,a.periode_awal,a.periode_akhir,a.usia,a.uy,a.ccy,a.ccy_rate,a.ccy_rate_claim,"
                + "(a.insured_amount*a.ccy_rate_claim) as insured_amount,"
                + "sum(checkreas(d.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((d.amount*a.ccy_rate_claim)*(getpremi(k.negative_flag = 'Y',-1,1))))) as bebanklaim,"
                + "sum(checkreas(d.ins_item_id in (48,49,53,73),((d.amount*a.ccy_rate_claim)*(getpremi(k.negative_flag = 'Y',-1,1))))) as recoveryklaim,"
                + "sum(checkreas(d.ins_item_id in (72),(d.amount*a.ccy_rate_claim))) as uwlain,"
                + "sum(checkreas(d.ins_item_id in (50),(d.amount*a.ccy_rate_claim))) as adjfee,"
                + "sum(checkreas(d.ins_item_id in (70),(d.amount*a.ccy_rate_claim))) as jasabengkel,"
                + "sum(checkreas(d.ins_item_id in (70) and d.tax_code in (10,12),(d.tax_amount*a.ccy_rate_claim))) as pph_jasabengkel,"
                + "sum(checkreas(d.ins_item_id in (76),d.amount*a.ccy_rate_claim)) as ppn,"
                + "sum(checkreas(d.ins_item_id in (75),d.amount*a.ccy_rate_claim)) as join_plcmnt,"
                + "sum(checkreas(d.ins_item_id in (77),d.amount*a.ccy_rate_claim)) as survey,"
                + "sum(checkreas(d.ins_item_id in (78),d.amount*a.ccy_rate_claim)) as ppn_total,"
                + "sum(checkreas(d.ins_item_id in (79),d.amount*a.ccy_rate_claim)) as cash_clltrl,"
                + "sum(checkreas(d.ins_item_id in (80),d.amount*a.ccy_rate_claim)) as material,"
                + "sum(checkreas(d.ins_item_id in (81),d.amount*a.ccy_rate_claim)) as surveyadjfee,"
                + "sum(checkreas(d.ins_item_id in (82),d.amount*a.ccy_rate_claim)) as expensesfee,"
                + "sum(checkreas(d.ins_item_id in (83),d.amount*a.ccy_rate_claim)) as vatfee, "
                + "sum(checkreas(d.ins_item_id in (84),d.amount*a.ccy_rate_claim)) as materai, "
                + "a.treaty_type,a.member_ent_id,a.short_name,a.claim_amount,a.claimri_amount, "
                + "claim_or,claim_bppdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 "
                + "from ( " + sqa.getSQL() + " "
                + "group by a.cc_code,c.short_desc,a.pol_id,a.pol_no,a.dla_no,a.claim_date,e.ent_name,a.claim_cause_desc,a.period_start,a.period_end,"
                + "b.ref1,b.refd1,a.policy_date,b.refd2,b.refd3,b.ref2,a.ccy,a.ccy_rate,a.ccy_rate_claim,b.insured_amount,a.claim_amount,j.treaty_type,i.member_ent_id,f.short_name "
                + "order by a.cc_code,a.pol_type_id,a.pol_no,i.member_ent_id ) a inner join ins_pol_items d on d.pol_id = a.pol_id "
                + "inner join ins_items k on k.ins_item_id = d.ins_item_id where a.claim_amount <> 0 "
                + "group by a.cc_code,a.cob,a.pol_id,a.pol_no,a.dla_no,a.claim_date,a.dla_date,a.ent_name,a.claim_cause_desc,a.nama,a.tgl_lahir,a.policy_date,"
                + "a.periode_awal,a.periode_akhir,a.usia,a.uy,a.ccy,a.ccy_rate,a.ccy_rate_claim,a.insured_amount,a.treaty_type,a.member_ent_id,a.short_name,a.claim_amount,a.claimri_amount, "
                + "claim_or,claim_bppdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 "
                + "order by a.cc_code,a.dla_no,a.pol_no,a.member_ent_id ) a order by a.cc_code,a.dla_no,a.pol_no,a.treaty_type,a.member_ent_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_AKUNTANSI_OUTWARD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("uy");
            row0.createCell(3).setCellValue("periode awal");
            row0.createCell(4).setCellValue("periode akhir");
            row0.createCell(5).setCellValue("tgl polis");
            row0.createCell(6).setCellValue("tgl klaim");
            row0.createCell(7).setCellValue("tgl dla");
            row0.createCell(8).setCellValue("no polis");
            row0.createCell(9).setCellValue("no dla");
            row0.createCell(10).setCellValue("tertanggung");
            row0.createCell(11).setCellValue("penyebab claim");
            row0.createCell(12).setCellValue("nama");
            row0.createCell(13).setCellValue("tgl lahir");
            row0.createCell(14).setCellValue("usia");
            row0.createCell(15).setCellValue("curr");
            row0.createCell(16).setCellValue("kurs");
            row0.createCell(17).setCellValue("tsi");
            row0.createCell(18).setCellValue("nilai klaim");
            row0.createCell(19).setCellValue("recovery klaim");
            row0.createCell(20).setCellValue("uwlain");
            row0.createCell(21).setCellValue("adjfee");
            row0.createCell(22).setCellValue("jasabengkel");
            row0.createCell(23).setCellValue("pph jasabengkel");
            row0.createCell(24).setCellValue("ppn");
            row0.createCell(25).setCellValue("join_plcmnt");
            row0.createCell(26).setCellValue("survey");
            row0.createCell(27).setCellValue("ppn_total");
            row0.createCell(28).setCellValue("cash_clltrl");
            row0.createCell(29).setCellValue("material");
            row0.createCell(30).setCellValue("surveyadjfee");
            row0.createCell(31).setCellValue("expensesfee");
            row0.createCell(32).setCellValue("vatfee");
            row0.createCell(33).setCellValue("materai");
            row0.createCell(34).setCellValue("reasurasur");
            row0.createCell(35).setCellValue("claim or");
            row0.createCell(36).setCellValue("claim bppdan");
            row0.createCell(37).setCellValue("claim spl");
            row0.createCell(38).setCellValue("claim fac");
            row0.createCell(39).setCellValue("claim qs");
            row0.createCell(40).setCellValue("claim park");
            row0.createCell(41).setCellValue("claim faco");
            row0.createCell(42).setCellValue("claim jp");
            row0.createCell(43).setCellValue("claim qskr");
            row0.createCell(44).setCellValue("claim kscbi");
            row0.createCell(45).setCellValue("claim xol");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cob"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("periode_awal"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("periode_akhir"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            if (h.getFieldValueByFieldNameST("dla_no") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            }
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            if (h.getFieldValueByFieldNameST("claim_cause_desc") != null) {
                row.createCell(11).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("claim_cause_desc")));
            }
            if (h.getFieldValueByFieldNameST("nama") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("nama"));
            }
            if (h.getFieldValueByFieldNameST("cob").equalsIgnoreCase("KREASI") || h.getFieldValueByFieldNameST("cob").equalsIgnoreCase("CREDIT")) {
                if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                    row.createCell(13).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
                }
                if (h.getFieldValueByFieldNameST("usia") != null) {
                    row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("usia"));
                }
            }
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("bebanklaim").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("recoveryklaim").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("uwlain").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("adjfee").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("pph_jasabengkel").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("join_plcmnt").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("survey").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("ppn_total").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("cash_clltrl").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("surveyadjfee").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("expensesfee").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("vatfee").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("materai").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_bppdan") != null) {
                row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_jp") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("claim_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qskr") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("claim_qskr").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_kscbi") != null) {
                row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_xol1") != null) {
                row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("claim_xol1").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList KLAIM_AKUNTANSI_OUTWARD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.cc_code,c.short_desc as cob,a.pol_id,';'||a.pol_no as pol_no,a.dla_no,a.claim_date,a.dla_date,e.ent_name,a.claim_cause_desc,"
                + "b.ref1 as nama,b.refd1 as tgl_lahir,a.policy_date,coalesce(b.refd2,a.period_start) as period_start,coalesce(b.refd3,a.period_end) as period_end,"
                + "b.ref2 as usia,a.ccy,a.ccy_rate,a.ccy_rate_claim,b.insured_amount,a.claim_amount,j.treaty_type,i.member_ent_id,f.short_name,sum(i.claim_amount) as claimri_amount, "
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or, "
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.claim_amount*a.ccy_rate_claim)),2) as claim_bpdan, "
                + "round(sum(checkreas(j.treaty_type='SPL',i.claim_amount*a.ccy_rate_claim)),2) as claim_spl, "
                + "round(sum(checkreas(j.treaty_type='FAC',i.claim_amount*a.ccy_rate_claim)),2) as claim_fac, "
                + "round(sum(checkreas(j.treaty_type='QS',i.claim_amount*a.ccy_rate_claim)),2) as claim_qs, "
                + "round(sum(checkreas(j.treaty_type='PARK',i.claim_amount*a.ccy_rate_claim)),2) as claim_park, "
                + "round(sum(checkreas(j.treaty_type in ('FACO','FACO1','FACO2','FACO3'),i.claim_amount*a.ccy_rate_claim)),2) as claim_faco, "
                + "round(sum(checkreas(j.treaty_type='JP',i.claim_amount*a.ccy_rate_claim)),2) as claim_jp, "
                + "round(sum(checkreas(j.treaty_type='QSKR',i.claim_amount*a.ccy_rate_claim)),2) as claim_qskr, "
                + "round(sum(checkreas(j.treaty_type='KSCBI',i.claim_amount*a.ccy_rate_claim)),2) as claim_kscbi "
                + "round(sum(checkreas(j.treaty_type in ('XOL1','XOL2','XOL3','XOL4','XOL5'),i.claim_amount*a.ccy_rate_claim)),2) as claim_xol1 ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = "select a.cc_code,a.cob as ref5,a.period_start,a.period_end,a.policy_date,a.claim_date,a.dla_date,"
                + "a.pol_id,a.pol_no,getzone3(a.treaty_type = 'OR',a.dla_no) as dla_no,a.ent_name,a.claim_cause_desc,a.nama as ref3,a.tgl_lahir as create_date,a.usia as ref4,a.ccy,a.ccy_rate,a.ccy_rate_claim,"
                + "getpremi(a.treaty_type = 'OR',a.insured_amount,0) as insured_amount,"
                + "getpremi(a.treaty_type = 'OR',a.bebanklaim,0) as nd_comm1,"
                + "getpremi(a.treaty_type = 'OR',a.recoveryklaim,0) as nd_comm2,"
                + "getpremi(a.treaty_type = 'OR',a.uwlain,0) as nd_comm3,"
                + "getpremi(a.treaty_type = 'OR',a.adjfee,0) as nd_comm4,"
                + "getpremi(a.treaty_type = 'OR',a.jasabengkel,0) as nd_brok1,"
                + "getpremi(a.treaty_type = 'OR',a.pph_jasabengkel,0) as nd_brok2,a.treaty_type as ref1,a.member_ent_id as entity_id,a.short_name as ref2,"
                + "getpremi(a.treaty_type = 'OR',0,a.claim_amount) as claim_amount,claim_or,claim_bpdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 "
                + "from ( select a.cc_code,a.cob,a.pol_id,a.pol_no,a.dla_no,a.claim_date,a.dla_date,a.ent_name,a.claim_cause_desc,"
                + "a.nama,a.tgl_lahir,a.policy_date,a.period_start,a.period_end,a.usia,a.ccy,a.ccy_rate,a.ccy_rate_claim,"
                + "(a.insured_amount*a.ccy_rate_claim) as insured_amount,"
                + "sum(checkreas(d.ins_item_id in (46,47,51,52,54,56,60,64,65,74,55),((d.amount*a.ccy_rate_claim)*(getpremi(k.negative_flag = 'Y',-1,1))))) as bebanklaim,"
                + "sum(checkreas(d.ins_item_id in (48,49,53,73),((d.amount*a.ccy_rate_claim)*(getpremi(k.negative_flag = 'Y',-1,1))))) as recoveryklaim,"
                + "sum(checkreas(d.ins_item_id in (72),(d.amount*a.ccy_rate_claim))) as uwlain,"
                + "sum(checkreas(d.ins_item_id in (50),(d.amount*a.ccy_rate_claim))) as adjfee,"
                + "sum(checkreas(d.ins_item_id in (70),(d.amount*a.ccy_rate_claim))) as jasabengkel,"
                + "sum(checkreas(d.ins_item_id in (70) and d.tax_code in (10,12),(d.tax_amount*a.ccy_rate_claim))) as pph_jasabengkel,"
                + "a.treaty_type,a.member_ent_id,a.short_name,a.claim_amount,a.claimri_amount,claim_or,claim_bpdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 "
                + "from ( " + sqa.getSQL() + " "
                + "group by a.cc_code,c.short_desc,a.pol_id,a.pol_no,a.dla_no,a.claim_date,e.ent_name,a.claim_cause_desc,a.period_start,a.period_end,"
                + "b.ref1,b.refd1,a.policy_date,b.refd2,b.refd3,b.ref2,a.ccy,a.ccy_rate,a.ccy_rate_claim,b.insured_amount,j.treaty_type,i.member_ent_id,f.short_name "
                + "order by a.cc_code,a.pol_type_id,a.pol_no,i.member_ent_id ) a inner join ins_pol_items d on d.pol_id = a.pol_id "
                + "inner join ins_items k on k.ins_item_id = d.ins_item_id where a.claim_amount <> 0 "
                + "group by a.cc_code,a.cob,a.pol_id,a.pol_no,a.dla_no,a.claim_date,a.dla_date,a.ent_name,a.claim_cause_desc,a.nama,a.tgl_lahir,a.policy_date,"
                + "a.period_start,a.period_end,a.usia,a.ccy,a.ccy_rate,a.ccy_rate_claim,a.insured_amount,a.treaty_type,a.member_ent_id,a.short_name,a.claim_amount,a.claimri_amount, "
                + "claim_or,claim_bpdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_jp,claim_qskr,claim_kscbi,claim_xol1 "
                + "order by a.cc_code,a.dla_no,a.pol_no,a.member_ent_id ) a order by a.cc_code,a.dla_no,a.pol_no,a.treaty_type,a.member_ent_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStPolicyTypeID() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        return l;
    }

    public DTOList NOTACLAIMREINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stNonCreditID != null) {
            if (stNonCreditID.equalsIgnoreCase("1")) {
                sqa.addSelect(
                        " a.cover_type_code,j.treaty_type as co_treaty_id,a.comm_amount,"
                        + " sum(coalesce(((a.premi_new/getvalid(a.premi_total=0,a.premi_total))*(i.claim_amount*a.ccy_rate)),0)) as premi_amt ");

                String query = null;
                query = " from ( select a.cc_code,a.pol_id,a.pol_no,e.cover_type as cover_type_code,coalesce(sum(d.premi_new*a.ccy_rate),0) as premi_new,"
                        + " (a.premi_total*a.ccy_rate) as premi_total,a.status,a.active_flag,a.effective_flag,a.policy_date,"
                        + " a.period_start,a.ccy,a.ccy_rate,a.ccy_rate_claim,a.ins_policy_type_grp_id,a.pol_type_id "
                        + " ,(select sum(z.rate) "
                        + " from ins_items x "
                        + " inner join ins_pol_items z on z.ins_item_id = x.ins_item_id "
                        + " where z.pol_id = a.pol_id and x.item_group in ('BROKR','COMM','FEEBASE','HFEE') group by a.pol_id) as comm_amount "
                        + " from ins_policy a"
                        + " inner join ins_pol_cover d on d.pol_id = a.pol_id"
                        + " inner join ins_cover_poltype e on e.ins_cover_id = d.ins_cover_id and e.pol_type_id = a.pol_type_id "
                        + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                        + " and a.pol_no in ( select pol_no from ins_policy a "
                        + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
                if (getAppDateFrom() != null) {
                    query = query + " and date_trunc('day',a.approved_date) >= ? ";
                    sqa.addPar(appDateFrom);
                }
                if (getAppDateTo() != null) {
                    query = query + " and date_trunc('day',a.approved_date) <= ? ";
                    sqa.addPar(appDateTo);
                }
                query = query + " ) group by a.cc_code,a.pol_id,a.pol_no,e.cover_type,a.status,a.active_flag,a.effective_flag,a.policy_date,"
                        + " a.period_start,a.ccy,a.premi_total,a.ccy_rate,a.ccy_rate_claim,a.pol_type_id,a.ins_policy_type_grp_id,d.ins_cover_id ) a "
                        + " inner join ins_pol_obj c on c.pol_id=a.pol_id "
                        + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                        + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                        + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                        + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                        + " inner join ent_master k on k.ent_id = i.member_ent_id ";
                sqa.addQuery(query);
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sqa.addSelect(
                        " b.cover_type_code,b.ins_policy_group_id,b.norut,b.captive,j.treaty_type as co_treaty_id," + //j.treaty_type as co_treaty_id,
                        " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.claim_amount*a.ccy_rate)),0)) as premi_amt ");

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
                        + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                        + " and a.pol_no in ( select pol_no from ins_policy a "
                        + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
                if (getAppDateFrom() != null) {
                    query = query + " and date_trunc('day',a.approved_date) >= ? ";
                    sqa.addPar(appDateFrom);
                }
                if (getAppDateTo() != null) {
                    query = query + " and date_trunc('day',a.approved_date) <= ? ";
                    sqa.addPar(appDateTo);
                }
                query = query + " ) group by a.pol_no,d.ins_cover_id,d.rate,a.pol_type_id,a.premi_total,a.ccy_rate,a.ccy_rate,e.ins_policy_group_id,e.norut,e.cover_type,a.premi_rate ORDER BY A.POL_NO"
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
                    " b.cover_type_code,b.ins_policy_group_id,b.norut,b.captive,j.treaty_type as co_treaty_id," + //j.treaty_type as co_treaty_id,
                    " sum(coalesce(((getvalid(b.premi_new2=0,b.premi_new2)/getvalid(b.premi_total2=0,b.premi_total2))*(i.claim_amount*a.ccy_rate)),0)) as premi_amt ");

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
                    + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                    + " and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                    + " and a.pol_no in ( select pol_no from ins_policy a "
                    + " where a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                    + " and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
            if (getAppDateFrom() != null) {
                query = query + " and date_trunc('day',a.approved_date) >= ? ";
                sqa.addPar(appDateFrom);
            }
            if (getAppDateTo() != null) {
                query = query + " and date_trunc('day',a.approved_date) <= ? ";
                sqa.addPar(appDateTo);
            }
            query = query + " ) group by a.pol_no,d.ins_cover_id,d.rate,a.pol_type_id,a.premi_total,a.ccy_rate,a.ccy_rate_claim,e.ins_policy_group_id,e.norut,e.cover_type,a.premi_rate ORDER BY A.POL_NO"
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

        sqa.addClause("a.status IN ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA'");
        sqa.addClause("a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause("j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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
                        + "sum(a.premi_amt) as premi_amt from band_level b left join ( "
                        + "select a.cover_type_code,co_treaty_id,a.comm_amount,a.premi_amt from ( "
                        + sqa.getSQL() + " group by a.cover_type_code,a.comm_amount,j.treaty_type ) "
                        + "a where a.premi_amt <> 0 ) as a on a.comm_amount between b.ref1 and b.ref2 "
                        + "where b.group_desc = 'NOTAREINS' group by b.lvl,b.ref4,b.ref5,a.cover_type_code,co_treaty_id "
                        + "order by a.cover_type_code,b.lvl ";
            } else if (stNonCreditID.equalsIgnoreCase("2")) {
                sql = " select a.co_treaty_id,a.cover_type_code,round(sum(a.premi_amt),0) as premi_amt "
                        + "from ( select a.cover_type_code,a.ins_policy_group_id,a.norut,a.captive,co_treaty_id,"
                        + "a.premi_amt from ( "
                        + sqa.getSQL() + " group by b.cover_type_code,b.captive,b.ins_policy_group_id,b.norut,j.treaty_type "
                        + " ) a where a.premi_amt <> 0 ) a group by a.co_treaty_id,a.cover_type_code "
                        + "order by a.cover_type_code,a.co_treaty_id ";
            }
        } else {
            sql = " select a.co_treaty_id,a.cover_type_code,round(sum(a.premi_amt),0) as premi_amt "
                    + "from ( select a.cover_type_code,a.ins_policy_group_id,a.norut,a.captive,co_treaty_id,"
                    + "a.premi_amt from ( "
                    + sqa.getSQL() + " group by b.cover_type_code,b.captive,b.ins_policy_group_id,b.norut,j.treaty_type "
                    + " ) a where a.premi_amt <> 0 ) a group by a.co_treaty_id,a.cover_type_code "
                    + "order by a.cover_type_code,a.co_treaty_id ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    /**
     * @return the stNonCreditID
     */
    public String getStNonCreditID() {
        return stNonCreditID;
    }

    /**
     * @param stNonCreditID the stNonCreditID to set
     */
    public void setStNonCreditID(String stNonCreditID) {
        this.stNonCreditID = stNonCreditID;
    }

    public String getStFltTreatyTypeDesc() {
        return stFltTreatyTypeDesc;
    }

    public void setStFltTreatyTypeDesc(String stFltTreatyTypeDesc) {
        this.stFltTreatyTypeDesc = stFltTreatyTypeDesc;
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

    public DTOList CLAIM_RECAP_ALL2() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String sql = " a.pol_type_id,a.cc_code,"
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.premi_adisc,0) else 0 end) as premi_netto,  "
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then (select coalesce(sum(x.tax_amount),0) "
                + " from ins_pol_items x where a.pol_id = x.pol_id "
                + " and x.ins_item_id in (11,18,25,32,12,19,26,33,13,20,27,34,88,89,90,100,105,106,107,108) "
                + " and tax_code in (1,4,7,2,5,8)) else 0 end) as nd_taxcomm1,  "
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.premi_adisc,0) else 0 end) as premi_total,"
                + " sum (case when a.status in ('POLICY','ENDORSE','RENEWAL') "
                + " and date_trunc('day',a.policy_date) >= ? "
                + " and date_trunc('day',a.policy_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then (select coalesce(sum(x.tax_amount),0) "
                + " from ins_pol_items x where a.pol_id = x.pol_id "
                + " and x.ins_item_id in (11,18,25,32,12,19,26,33,13,20,27,34,88,89,90,100,105,106,107,108) "
                + " and tax_code in (1,4,7,2,5,8)) else 0 end) as nd_taxcomm2,  "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.claim_amount,0) else 0 end) as claim_amount,  "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(appDateFrom);
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + "then 1 else 0 end) as nd_feebase1, "
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + " then coalesce(a.claim_amount,0) else 0 end) as claim_amount_approved,"
                + " sum (case when a.status in ('CLAIM','CLAIM ENDORSE') "
                + " and date_trunc('day',a.approved_date) >= ? "
                + " and date_trunc('day',a.approved_date) <= ? ";
        sqa.addPar(DateUtil.getYear(appDateFrom) + "-01-" + DateUtil.getDays(appDateFrom));
        sqa.addPar(appDateTo);
        if (getPeriodFrom() != null) {
            sql = sql + "and date_trunc('day',a.period_start) >= ? ";
            sqa.addPar(periodFrom);
        }
        if (getPeriodTo() != null) {
            sql = sql + "and date_trunc('day',a.period_start) <= ? ";
            sqa.addPar(periodTo);
        }
        sql = sql + "then 1 else 0 end) as nd_feebase2 ";

        sqa.addSelect(sql);

        sqa.addQuery(" from ins_policy_produksi a ");

        //sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        //sqa.addClause(" (a.status in ('POLICY','RENEWAL','ENDORSE') or (a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status ='DLA')) ");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql2 = sqa.getSQL() + " group by a.cc_code,a.pol_type_id "
                + " order by a.cc_code,a.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql2,
                sqa.getPar(),
                InsurancePolicyView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXCEL_VALIDATE_CLAIM() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||a.pol_no as nopol,a.pla_no,a.pla_date,a.dla_no,a.dla_date,a.claim_amount ");

        sqa.addQuery("   from ins_policy a ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.active_flag='Y'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= '" + DLADateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= '" + DLADateTo + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= '" + PLADateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= '" + PLADateTo + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
            //sqa.addPar(policyDateFrom);
        }

        if (Tools.isYes(stIndex)) {
            sqa.addClause("a.f_validate_claim='Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause("coalesce(a.f_validate_claim,'') <> 'Y'");
        }

        String sql = sqa.getSQL() + " order by a.cc_code,a.pla_no,a.dla_no,a.pol_no";

        SQLUtil S = new SQLUtil();

        String nama_file = "validasi klaim_" + System.currentTimeMillis() + ".csv";

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

    public DTOList BORDERO_KBG() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_no,a.pol_type_id,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),c.refd1,getperiod(a.pol_type_id=60,c.refd4,a.period_start)))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),c.refd2,getperiod(a.pol_type_id=60,c.refd5,a.period_end)))) as period_end,"
                + "m.ent_id,m.ent_name,a.cust_name,n.ent_name as prod_name,a.cc_code,a.claim_date,a.claim_cause,"
                + "c.insured_amount as insured_amount,c.premi_total as premi_total,i.tsi_amount,a.ccy,a.ccy_rate,a.ccy_rate_claim,i.ricomm_rate as share_pct,"
                + "c.ref1,c.ref2,c.ref3,c.ref4,c.ref5,c.ref3d as claim_person_name,f.short_desc as ref6,i.notes as ref7,i.claim_ri_slip_no as ref8,c.refd1,"
                + "coalesce(a.claim_amount,0) as claim_amount_approved, coalesce(i.claim_amount,0) as claim_amount ");

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id=a.pol_id "
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type"
                + " inner join ent_master m on m.ent_id = i.member_ent_id"
                + " inner join ent_master n on n.ent_id = a.entity_id "
                + " inner join ins_clm_cause o on o.ins_clm_caus_id = a.claim_cause ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("	j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");

        if (Tools.isYes(stIndex)) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

//        if (stPolicyTypeID != null) {
//            sqa.addClause("a.pol_type_id = ?");
//            sqa.addPar(stPolicyTypeID);
//        }

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

//        if (stFltTreatyType != null) {
//            sqa.addClause("j.treaty_type = ?");
//            sqa.addPar(stFltTreatyType);
//        }

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        String sql = null;

        sql = "select cc_code,pol_no,cust_name,prod_name,period_start,period_end,insured_amount,premi_total,ccy,ccy_rate,ccy_rate_claim,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,claim_person_name,share_pct,refd1 as change_date,claim_date,claim_cause,"
                + " sum(tsi_amount) as tsi_reas,sum(claim_amount) as claim_amount,sum(claim_amount_approved) as claim_amount_approved "
                + " from ( " + sqa.getSQL() + " ) a group by cc_code,pol_no,cust_name,prod_name,period_start,period_end,insured_amount,premi_total,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,claim_person_name,refd1,claim_date,claim_cause,"
                + " ccy,ccy_rate,ccy_rate_claim,share_pct order by cc_code,pol_no ";

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
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.approved_date,a.pol_id,a.pol_no,a.pla_no,a.dla_no,a.pol_type_id,j.treaty_type,e.ins_treaty_id,e.treaty_name,e.treaty_grp_id,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),c.refd1,getperiod(a.pol_type_id=60,c.refd4,a.period_start)))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),c.refd2,getperiod(a.pol_type_id=60,c.refd5,a.period_end)))) as period_end,"
                + "m.ent_id,m.ent_name,n.ent_name as prod_name,a.cc_code,a.claim_date,o.cause_desc as claim_cause,a.cust_name,"
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),l.ins_risk_cat_code,null)) as okupasi,a.cover_type_code as status,"
                + "c.insured_amount as insured_amount,c.premi_total as premi_total,a.ccy,a.ccy_rate,a.ccy_rate_claim,i.ricomm_rate as share_pct,"
                + "c.ref1,c.ref2,c.ref3,c.ref4,c.ref5,c.ref3d as claim_person_name,f.short_desc as ref6,i.notes as ref7,i.claim_ri_slip_no as ref8,c.refd1,"
                + "coalesce(a.claim_amount,0) as claim_amount_approved, coalesce(i.tsi_amount,0) as tsi_amount,coalesce(i.claim_amount,0) as claim_amount,m.ent_name as reasuradur,d.ent_name as panel_ri, "
                + "(select 'Subrogasi'::text from ins_pol_items x where x.pol_id = a.pol_id and x.ins_item_id = '48' group by 1) as subrogasi ");

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id=a.pol_id "
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + " inner join ins_treaty_types k on k.ins_treaty_type_id = j.treaty_type"
                + " inner join ent_master m on m.ent_id = i.member_ent_id"
                + " inner join ent_master n on n.ent_id = a.entity_id "
                + " inner join ins_clm_cause o on o.ins_clm_caus_id = a.claim_cause "
                + " left join ent_master d on d.ent_id = i.reins_ent_id "
                + " inner join ins_treaty e on e.ins_treaty_id = g.ins_treaty_id "
                + "left join ins_risk_cat l on l.ins_risk_cat_id = c.ins_risk_cat_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD')");
        sqa.addClause("	j.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
        //sqa.addClause(" a.claim_status = 'DLA'");
        //sqa.addClause(" a.active_flag='Y' and a.effective_flag='Y' ");

        if (Tools.isYes(stIndex)) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

//        if (stPolicyTypeID != null) {
//            sqa.addClause("a.pol_type_id = ?");
//            sqa.addPar(stPolicyTypeID);
//        }

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

//        if (stFltTreatyType != null) {
//            sqa.addClause("j.treaty_type = ?");
//            sqa.addPar(stFltTreatyType);
//        }

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(stFltTreatyType);
            }
        }

        if (Tools.isYes(stCashcall)) {
            sqa.addClause("a.claim_cash_call_f = 'Y'");
        }
//        else {
//            sqa.addClause("coalesce(a.claim_cash_call_f,'N') <> 'Y'");
//        }

        String sql = null;

        sql = "select approved_date,pol_id,pol_no,pla_no,dla_no,cc_code,cust_name,prod_name,period_start,period_end,insured_amount,premi_total,ccy,ccy_rate,ccy_rate_claim,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,claim_person_name,share_pct,refd1 as change_date,claim_date,claim_cause,"
                + " sum(tsi_amount) as tsi_reas,sum(claim_amount) as claim_reas,sum(claim_amount_approved) as claim_amount_approved,reasuradur,panel_ri,treaty_type,ins_treaty_id,treaty_name,treaty_grp_id,subrogasi,okupasi,status "
                + " from ( " + sqa.getSQL() + " ) a group by approved_date,pol_id,cc_code,pol_no,pla_no,dla_no,cust_name,prod_name,period_start,period_end,insured_amount,premi_total,ref1,ref2,ref3,ref4,ref5,ref6,ref7,ref8,claim_person_name,refd1,claim_date,claim_cause,"
                + " panel_ri,treaty_type,ins_treaty_id,treaty_name,treaty_grp_id,ccy,ccy_rate,ccy_rate_claim,share_pct,reasuradur,subrogasi,okupasi,status order by cc_code,pol_no,pla_no,dla_no ";

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
            row0.createCell(0).setCellValue("policy no");
            row0.createCell(1).setCellValue("no.LKS");
            row0.createCell(2).setCellValue("no.LKP");
            row0.createCell(3).setCellValue("the insured");
            row0.createCell(4).setCellValue("debitur");
            row0.createCell(5).setCellValue("debitur kredit konstruksi");
            row0.createCell(6).setCellValue("period start");
            row0.createCell(7).setCellValue("period end");
            row0.createCell(8).setCellValue("kurs");
            row0.createCell(9).setCellValue("curr");
            row0.createCell(10).setCellValue("insured amount");
            row0.createCell(11).setCellValue("claim amount");
            row0.createCell(12).setCellValue("tsi share ORI");
            row0.createCell(13).setCellValue("tsi share IDR");
            row0.createCell(14).setCellValue("claim share ORI");
            row0.createCell(15).setCellValue("claim share IDR");
            row0.createCell(16).setCellValue("treaty type");
            row0.createCell(17).setCellValue("treaty name");
            row0.createCell(18).setCellValue("DOL");
            row0.createCell(19).setCellValue("cause of claim");
            row0.createCell(20).setCellValue("reasuradur");
            row0.createCell(21).setCellValue("panel reas");
            row0.createCell(22).setCellValue("jenis");
            row0.createCell(23).setCellValue("tglsetujui");
            row0.createCell(24).setCellValue("okupasi");
            row0.createCell(25).setCellValue("status");

            BigDecimal tsiIDR = BDUtil.mul(h.getFieldValueByFieldNameBD("tsi_reas"), h.getFieldValueByFieldNameBD("ccy_rate"));
            BigDecimal claimIDR = BDUtil.mul(h.getFieldValueByFieldNameBD("claim_reas"), h.getFieldValueByFieldNameBD("ccy_rate_claim"));

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            if (h.getFieldValueByFieldNameST("dla_no") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("ref1"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("claim_person_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("tsi_reas").doubleValue());
            row.createCell(13).setCellValue(tsiIDR.doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim_reas").doubleValue());
            row.createCell(15).setCellValue(claimIDR.doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("treaty_type"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("treaty_name"));
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                row.createCell(19).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("claim_cause")));
            }
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("reasuradur"));
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("panel_ri"));
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("subrogasi"));
            if (h.getFieldValueByFieldNameDT("approved_date") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
            }
            if (h.getFieldValueByFieldNameST("okupasi") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameST("okupasi"));
            }
            if (h.getFieldValueByFieldNameST("status") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameST("status"));
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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
     * @return the stRISlip
     */
    public String getStRISlip() {
        return stRISlip;
    }

    /**
     * @param stRISlip the stRISlip to set
     */
    public void setStRISlip(String stRISlip) {
        this.stRISlip = stRISlip;
    }

    public DTOList EXCEL_KLAIM_AKUNTANSI_ALL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,b.description as jenpol,a.cc_code as koda,d.description as cabang,a.cover_type_code,"
                + "a.pol_id,a.dla_no,';'||a.pol_no as pol_no,a.ccy,a.ccy_rate_claim,e.ent_name,a.claim_date,a.claim_payment_date,"
                + "getname(a.ins_policy_type_grp_id=10,a.claim_client_name,g.ref1) as tertanggung,a.claim_approved_date,"
                + "g.ref2,g.ref3,g.ref4,g.ref5,g.ref6,g.ref7,g.ref8,g.ref9,g.ref10,g.ref16,g.insured_amount,g.premi_total,"
                + "g.ref1d,g.ref2d,g.ref3d,g.ref4d,g.ref5d,g.ref6d,g.ref7d,g.ref8d,g.ref9d,g.ins_risk_cat_id,a.claim_chronology,h.cause_desc,"
                + "getperiod(a.pol_type_id in (4,21,59,64,73,74,80),g.refd2,a.period_start) as period_start,getperiod(a.pol_type_id in (4,21,59,64,73,74,80),g.refd3,a.period_end) as period_end,"
                + "sum(checkreas(c.ins_item_id in (46),c.amount*a.ccy_rate_claim)) as klaim,"
                + "sum(checkreas(c.ins_item_id in (47),c.amount*a.ccy_rate_claim)) as deduct,"
                + "sum(checkreas(c.ins_item_id in (48),c.amount*a.ccy_rate_claim)) as subrogasi,"
                + "sum(checkreas(c.ins_item_id in (49),c.amount*a.ccy_rate_claim)) as wreck,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as adjfee,"
                + "(select l.description from ins_pol_items l where l.pol_id = a.pol_id and l.ins_item_id in (50,77) group by l.description) as nama_adjfee,"
                + "sum(checkreas(c.ins_item_id in (50) and c.tax_code = 11,c.tax_amount*a.ccy_rate_claim)) as pph_adjfee,"
                + "sum(checkreas(c.ins_item_id in (51),c.amount*a.ccy_rate_claim)) as tjh,"
                + "sum(checkreas(c.ins_item_id in (52),c.amount*a.ccy_rate_claim)) as biaderek,"
                + "sum(checkreas(c.ins_item_id in (53),c.amount*a.ccy_rate_claim)) as salvage,"
                + "sum(checkreas(c.ins_item_id in (54),c.amount*a.ccy_rate_claim)) as exgratiaklaim,"
                + "sum(checkreas(c.ins_item_id in (55),c.amount*a.ccy_rate_claim)) as bunga,"
                + "sum(checkreas(c.ins_item_id in (56),c.amount*a.ccy_rate_claim)) as santunan,"
                + "sum(checkreas(c.ins_item_id in (60),c.amount*a.ccy_rate_claim)) as depresiasi,"
                + "sum(checkreas(c.ins_item_id in (64),c.amount*a.ccy_rate_claim)) as interimpayment,"
                + "sum(checkreas(c.ins_item_id in (65),c.amount*a.ccy_rate_claim)) as penalti,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as jasabengkel,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code in (10,12),c.tax_amount*a.ccy_rate_claim)) as pph_jasabengkel,"
                + "(select k.ent_name from ins_pol_items l inner join ent_master k on k.ent_id = l.ent_id where l.pol_id = a.pol_id and l.tax_code in (10,12) group by k.ent_name) as nama_bengkel,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as exgratiauw,"
                + "sum(checkreas(c.ins_item_id in (73),c.amount*a.ccy_rate_claim)) as feerecovery,"
                + "sum(checkreas(c.ins_item_id in (74),c.amount*a.ccy_rate_claim)) as biasparepart,"
                + "sum(checkreas(c.ins_item_id in (75),c.amount*a.ccy_rate_claim)) as joinplacement,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as ppn,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as biasurvey,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as ppntotal,"
                + "sum(checkreas(c.ins_item_id in (79),c.amount*a.ccy_rate_claim)) as cashcollatrl,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as material,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as surveyadjfee,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as expensesfee,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as vatfee, "
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as materai, "
                + "sum(c.amount*a.ccy_rate_claim) as claim_total,"
                + "array_to_string(array(select string_agg(j.description, ', ') "
                + "from ins_pol_cover i "
                + "inner join ins_cover j on j.ins_cover_id = i.ins_cover_id "
                + "where i.ins_pol_obj_id = g.ins_pol_obj_id "
                + "group by i.ins_cover_id order by i.ins_cover_id ), ', ') as jaminan, "
                + "k.vs_description as company_type,j.vs_description as company_grup,i.vs_description as sumbis ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_policy_types b on b.pol_type_id = a.pol_type_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id "
                + "inner join ins_pol_obj g on g.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_items f on f.ins_item_id = c.ins_item_id "
                + "inner join gl_cost_center d on d.cc_code = a.cc_code "
                + "inner join ent_master e on e.ent_id = a.entity_id "
                + "left join ins_clm_cause h on h.ins_clm_caus_id = a.claim_cause "
                + "left join s_valueset i on i.vs_code = e.category1 and i.vs_group = 'ASK_BUS_SOURCE' "
                + "left join s_company_group j on j.vs_code::text = e.ref2 "
                + "left join s_valueset k on k.vs_code = e.ref1 and k.vs_group = 'COMP_TYPE' ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        //sqa.addClause(" a.pol_type_id in (2)");
        //sqa.addClause(" g.ref3 = '2931'");

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        final String sql = "select a.*,e.ins_risk_cat_code as kd_okupasi,e.description as okupasi_desc from ( "
                + " select a.pol_type_id,a.jenpol,a.koda,a.cabang,a.dla_no,a.pol_id,a.pol_no,b.entity_id,a.ent_name,a.tertanggung,a.claim_approved_date,a.claim_payment_date,b.position_code,a.ccy,a.ccy_rate_claim,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,klaim,0) as klaim,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,deduct,0) as deduct,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,subrogasi,0) as subrogasi,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,wreck,0) as wreck,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,adjfee,0) as adjfee,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,tjh,0) as tjh,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,biaderek,0) as biaderek,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,salvage,0) as salvage,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,exgratiaklaim,0) as exgratiaklaim,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,bunga,0) as bunga,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,santunan,0) as santunan,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,depresiasi,0) as depresiasi,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,interimpayment,0) as interimpayment,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,penalti,0) as penalti,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,jasabengkel,0) as jasabengkel,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,exgratiauw,0) as exgratiauw,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,feerecovery,0) as feerecovery,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,biasparepart,0) as biasparepart,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,joinplacement,0) as joinplacement,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppn,0) as ppn,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,biasurvey,0) as biasurvey,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,ppntotal,0) as ppntotal,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,cashcollatrl,0) as cashcollatrl,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,material,0) as material,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,surveyadjfee,0) as surveyadjfee,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,expensesfee,0) as expensesfee,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,vatfee,0) as vatfee,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,materai,0) as materai,"
                + " getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,nama_adjfee) as nama_adjfee,"
                + " getzone3(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,nama_bengkel) as nama_bengkel,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,pph_jasabengkel,0) as pph_jasabengkel,"
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,pph_adjfee,0) as pph_adjfee, "
                + " getpremi(((b.coins_type = 'COINS_COVER' and a.pol_type_id = 21 and b.position_code = 'LDR') or (b.coins_type = 'COINS' and a.pol_type_id <> 21)) and b.entity_id = 1,claim_total,0) as claim_total, "
                + " getname(a.cover_type_code in ('COINSOUT','COINSOUTSELF') and b.claim_amt <> 0 and b.entity_id <> 1,c.short_name,null) as coins,"
                + " getpremi(b.claim_amt = 0 or b.entity_id in (1,2000,2001),null,(b.claim_amt*a.ccy_rate_claim)) as coins_amount,b.coins_type,a.cover_type_code,"
                + " substr(a.period_start::text,1,4) as tahun_uy,a.period_start as tglawal,a.period_end as tglakhir,"
                + " a.ref2,a.ref3,a.ref4,a.ref5 as alamat,a.ref6,a.ref7,a.ref8,a.ref9,a.ref10,a.ref16,a.insured_amount as tsi,a.premi_total as premi,"
                + " a.ref1d,a.ref2d,a.ref3d,a.ref4d,a.ref5d,a.ref6d,a.ref7d,a.ref8d,a.ref9d as kodepos,a.ins_risk_cat_id,a.claim_chronology,a.cause_desc,jaminan,a.claim_date, "
                + " a.company_type,a.company_grup,a.sumbis "
                + " from ( " + sqa.getSQL() + " group by a.pol_type_id,b.description,a.cc_code,d.description,a.pol_id,a.dla_no,a.pol_no,a.ccy,a.ccy_rate_claim,e.ent_name,a.claim_client_name,g.ins_risk_cat_id,"
                + " g.ins_pol_obj_id,g.refd1,g.refd2,g.refd3,a.period_start,a.period_end,g.ref1,g.ref2,g.ref3,g.ref4,g.ref5,g.ref6,g.ref7,g.ref8,g.ref9,g.ref10,g.ref16,a.claim_chronology,h.cause_desc,a.claim_date,a.claim_payment_date,"
                + " g.ref1d,g.ref2d,g.ref3d,g.ref4d,g.ref5d,g.ref6d,g.ref7d,g.ref8d,g.ref9d,a.cover_type_code,a.claim_approved_date,g.insured_amount,g.premi_total,k.vs_description,j.vs_description,i.vs_description "
                + " order by a.cc_code,a.dla_no ) a "
                + " inner join ins_pol_coins b on b.policy_id = a.pol_id "
                + " inner join ent_master c on c.ent_id = b.entity_id "
                //+ " where b.position_code = 'MEM'"
                + " order by a.koda,a.pol_type_id,a.dla_no,b.coins_type desc,b.entity_id "
                + " ) a inner join ins_risk_cat e on e.ins_risk_cat_id = a.ins_risk_cat_id "
                + " where ( claim_total <> 0 or coins_amount <> 0 ) "
                + " order by a.koda,a.pol_type_id,a.dla_no,a.coins_type,a.entity_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_AKUNTANSI_ALL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        BigDecimal bebanKlaim = null;

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_type_id");
            row0.createCell(1).setCellValue("jenpol");
            row0.createCell(2).setCellValue("koda");
            row0.createCell(3).setCellValue("cabang");
            row0.createCell(4).setCellValue("dla_no");
            row0.createCell(5).setCellValue("pol_id");
            row0.createCell(6).setCellValue("pol_no");
            row0.createCell(7).setCellValue("ccy");
            row0.createCell(8).setCellValue("ccy_rate_claim");
            row0.createCell(9).setCellValue("cover_type_code");
            row0.createCell(10).setCellValue("ent_name");
            row0.createCell(11).setCellValue("tertanggung");
            row0.createCell(12).setCellValue("claim_approved_date");
            row0.createCell(13).setCellValue("position_code");
            row0.createCell(14).setCellValue("klaim");
            row0.createCell(15).setCellValue("deduct");
            row0.createCell(16).setCellValue("subrogasi");
            row0.createCell(17).setCellValue("wreck");
            row0.createCell(18).setCellValue("adjfee");
            row0.createCell(19).setCellValue("tjh");
            row0.createCell(20).setCellValue("biaderek");
            row0.createCell(21).setCellValue("salvage");
            row0.createCell(22).setCellValue("exgratiaklaim");
            row0.createCell(23).setCellValue("bunga");
            row0.createCell(24).setCellValue("santunan");
            row0.createCell(25).setCellValue("depresiasi");
            row0.createCell(26).setCellValue("interimpayment");
            row0.createCell(27).setCellValue("penalti");
            row0.createCell(28).setCellValue("jasabengkel");
            row0.createCell(29).setCellValue("exgratiauw");
            row0.createCell(30).setCellValue("feerecovery");
            row0.createCell(31).setCellValue("biasparepart");
            row0.createCell(32).setCellValue("joinplacement");
            row0.createCell(33).setCellValue("ppn");
            row0.createCell(34).setCellValue("biasurvey");
            row0.createCell(35).setCellValue("ppntotal");
            row0.createCell(36).setCellValue("cashcollatrl");
            row0.createCell(37).setCellValue("material");
            row0.createCell(38).setCellValue("surveyadjfee");
            row0.createCell(39).setCellValue("expensesfee");
            row0.createCell(40).setCellValue("vatfee");
            row0.createCell(41).setCellValue("materai");
            row0.createCell(42).setCellValue("coins");
            row0.createCell(43).setCellValue("coins_amount");
            row0.createCell(44).setCellValue("coins_type");
            row0.createCell(45).setCellValue("pph_jasabengkel");
            row0.createCell(46).setCellValue("pph_adjfee");
            row0.createCell(47).setCellValue("nama adjfee");
            row0.createCell(48).setCellValue("nama bengkel");
            row0.createCell(49).setCellValue("tahun_uy");
            row0.createCell(50).setCellValue("tglawal");
            row0.createCell(51).setCellValue("tglakhir");
            row0.createCell(52).setCellValue("alamat");
            row0.createCell(53).setCellValue("kodepos");
            row0.createCell(54).setCellValue("kd_okupasi");
            row0.createCell(55).setCellValue("okupasi_desc");
            row0.createCell(56).setCellValue("tsi");
            row0.createCell(57).setCellValue("premi");
            row0.createCell(58).setCellValue("kronologis");
            row0.createCell(59).setCellValue("penyebab klaim");
            row0.createCell(60).setCellValue("kode resiko");
            row0.createCell(61).setCellValue("tgl klaim");
            row0.createCell(62).setCellValue("company type");
            row0.createCell(63).setCellValue("company grup");
            row0.createCell(64).setCellValue("sumbis");
            row0.createCell(65).setCellValue("tgl bayar klaim");
            row0.createCell(66).setCellValue("no.rek pinjaman");
            row0.createCell(67).setCellValue("no.perj kredit");
            //row0.createCell(59).setCellValue("jaminan");


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jenpol")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("cover_type_code"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("position_code"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("klaim").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("deduct").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("subrogasi").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("wreck").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("adjfee").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("tjh").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("biaderek").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("exgratiaklaim").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("santunan").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("depresiasi").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("interimpayment").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("penalti").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("exgratiauw").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("feerecovery").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("biasparepart").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("joinplacement").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("biasurvey").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("ppntotal").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("cashcollatrl").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("surveyadjfee").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("expensesfee").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("vatfee").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("materai").doubleValue());
            if (h.getFieldValueByFieldNameST("coins") != null) {
                row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("coins"));
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameST("coins_type"));
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("pph_jasabengkel").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("pph_adjfee").doubleValue());
            if (h.getFieldValueByFieldNameST("nama_adjfee") != null) {
                row.createCell(47).setCellValue(h.getFieldValueByFieldNameST("nama_adjfee"));
            }
            if (h.getFieldValueByFieldNameST("nama_bengkel") != null) {
                row.createCell(48).setCellValue(h.getFieldValueByFieldNameST("nama_bengkel"));
            }
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameST("tahun_uy"));
            if (h.getFieldValueByFieldNameDT("tglawal") != null) {
                row.createCell(50).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            }
            if (h.getFieldValueByFieldNameDT("tglakhir") != null) {
                row.createCell(51).setCellValue(h.getFieldValueByFieldNameDT("tglakhir"));
            }
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameST("alamat"));
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameST("kodepos"));
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameST("kd_okupasi"));
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameST("okupasi_desc"));
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            if (h.getFieldValueByFieldNameST("claim_chronology") != null) {
                row.createCell(58).setCellValue(h.getFieldValueByFieldNameST("claim_chronology"));
            }
            if (h.getFieldValueByFieldNameST("cause_desc") != null) {
                row.createCell(59).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            }
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameST("ref3"));
            if (h.getFieldValueByFieldNameDT("claim_date") != null) {
                row.createCell(61).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            }
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameST("company_type"));
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameST("company_grup"));
            row.createCell(64).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("sumbis")));
            if (h.getFieldValueByFieldNameDT("claim_payment_date") != null) {
                row.createCell(65).setCellValue(h.getFieldValueByFieldNameDT("claim_payment_date"));
            }
            if (h.getFieldValueByFieldNameBD("pol_type_id").toString().equalsIgnoreCase("59")) {
                if (h.getFieldValueByFieldNameST("ref16") != null) {
                    row.createCell(66).setCellValue(h.getFieldValueByFieldNameST("ref16"));
                }
                if (h.getFieldValueByFieldNameST("ref4") != null) {
                    row.createCell(67).setCellValue(h.getFieldValueByFieldNameST("ref4"));
                }
            }
            // if (h.getFieldValueByFieldNameST("jaminan") != null) {
            //    row.createCell(59).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("jaminan")));
            //}
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList CLAIM_RECAP_ALL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.pol_type_id,c.short_desc as cust_name,"
                + "sum(getkoas(a.cc_code='10',a.claim_amount)) as tsi_bpdan,"
                + "sum(getkoas(a.cc_code='11',a.claim_amount)) as tsi_or,"
                + "sum(getkoas(a.cc_code='12',a.claim_amount)) as tsi_qs,"
                + "sum(getkoas(a.cc_code='13',a.claim_amount)) as tsi_spl,"
                + "sum(getkoas(a.cc_code='14',a.claim_amount)) as tsi_fac,"
                + "sum(getkoas(a.cc_code='15',a.claim_amount)) as tsi_park,"
                + "sum(getkoas(a.cc_code='16',a.claim_amount)) as tsi_faco,"
                + "sum(getkoas(a.cc_code='17',a.claim_amount)) as tsi_xol1,"
                + "sum(getkoas(a.cc_code='18',a.claim_amount)) as tsi_xol2,"
                + "sum(getkoas(a.cc_code='19',a.claim_amount)) as tsi_xol3,"
                + "sum(getkoas(a.cc_code='20',a.claim_amount)) as tsi_xol4,"
                + "sum(getkoas(a.cc_code='21',a.claim_amount)) as tsi_xol5,"
                + "sum(getkoas(a.cc_code='22',a.claim_amount)) as premi_bpdan,"
                + "sum(getkoas(a.cc_code='23',a.claim_amount)) as premi_or,"
                + "sum(getkoas(a.cc_code='24',a.claim_amount)) as premi_qs,"
                + "sum(getkoas(a.cc_code='25',a.claim_amount)) as premi_spl,"
                + "sum(getkoas(a.cc_code='30',a.claim_amount)) as premi_fac,"
                + "sum(getkoas(a.cc_code='31',a.claim_amount)) as premi_park,"
                + "sum(getkoas(a.cc_code='32',a.claim_amount)) as premi_faco,"
                + "sum(getkoas(a.cc_code='33',a.claim_amount)) as premi_xol1,"
                + "sum(getkoas(a.cc_code='40',a.claim_amount)) as premi_xol2,"
                + "sum(getkoas(a.cc_code='41',a.claim_amount)) as premi_xol3,"
                + "sum(getkoas(a.cc_code='42',a.claim_amount)) as premi_xol4,"
                + "sum(getkoas(a.cc_code='43',a.claim_amount)) as premi_xol5,"
                + "sum(getkoas(a.cc_code='44',a.claim_amount)) as comm_bpdan,"
                + "sum(getkoas(a.cc_code='45',a.claim_amount)) as comm_or,"
                + "sum(getkoas(a.cc_code='50',a.claim_amount)) as comm_qs,"
                + "sum(getkoas(a.cc_code='51',a.claim_amount)) as comm_spl,"
                + "sum(getkoas(a.cc_code='52',a.claim_amount)) as comm_fac,"
                + "sum(getkoas(a.cc_code='60',a.claim_amount)) as comm_park,"
                + "sum(getkoas(a.cc_code='61',a.claim_amount)) as comm_faco,"
                + "sum(getkoas(a.cc_code='70',a.claim_amount)) as comm_xol1 ");

        sqa.addQuery(" from ins_policy_produksi a "
                + "inner join gl_cost_center b on b.cc_code = a.cc_code "
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause(" a.claim_status IN ('DLA')");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodTo != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
        }

        final String sql = sqa.getSQL() + " group by a.pol_type_id,c.short_desc "
                + " order by a.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_KLAIM_POS3() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from claim3 a ");

        if (EFF_CLAIM) {
            sqa.addClause("a.claim_effective_flag = 'Y'");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.approved_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.approved_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (ACT_CLAIM) {
            sqa.addClause("a.claim_effective_flag is null");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'N'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.dla_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.dla_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stItemClaimID != null) {
            sqa.addClause("a.itemid = ? ");
            sqa.addPar(stItemClaimID);
        }

        String sql = null;

        if (stItemClaimID != null) {
            if (stItemClaimID.equalsIgnoreCase("48")) {

                sql = "select a.*,b.receipt_date,b.receipt_no "
                        + " from ( " + sqa.getSQL() + " ) a "
                        + "  inner join ar_invoice b on b.attr_pol_id::text = a.pol_id "
                        + " inner join ar_invoice_details c on c.ar_invoice_id = b.ar_invoice_id and c.ar_trx_line_id = 59 "
                        + " where a.active_flag='Y'";
            } else {

                sql = "select a.* "
                        + " from ( " + sqa.getSQL() + " ) a "
                        + " where a.active_flag='Y'";
            }
        } else {
            sql = "select a.* "
                    + " from ( " + sqa.getSQL() + " ) a "
                    + " where a.active_flag='Y'";
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_KLAIM_POS3() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal total = new BigDecimal(0);
        BigDecimal itemklaim = new BigDecimal(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Daerah");
            row0.createCell(1).setCellValue("No. LKS");
            row0.createCell(2).setCellValue("No. LKP");
            row0.createCell(3).setCellValue("No. Polis");
            row0.createCell(4).setCellValue("Nama Bank");
            row0.createCell(5).setCellValue("Nama Pemasar");
            row0.createCell(6).setCellValue("Nama Tertanggung");
            row0.createCell(7).setCellValue("K.O.B");
            row0.createCell(8).setCellValue("Curr");
            row0.createCell(9).setCellValue("Kurs");
            row0.createCell(10).setCellValue("Nama Item");
            row0.createCell(11).setCellValue("Claim Amount");
            row0.createCell(12).setCellValue("Tanggal Klaim");
            row0.createCell(13).setCellValue("Tanggal LKP");
            row0.createCell(14).setCellValue("Tanggal Setujui");
            row0.createCell(15).setCellValue("Tanggal Polis");
            row0.createCell(16).setCellValue("Penyebab Klaim");
            row0.createCell(17).setCellValue("Keterangan");
            row0.createCell(18).setCellValue("No Klaim");
            row0.createCell(19).setCellValue("Pol ID");
            if (getStItemClaimID() != null) {
                if (getStItemClaimID().equalsIgnoreCase("48")) {
                    row0.createCell(20).setCellValue("No Bukti");
                    row0.createCell(21).setCellValue("Tgl Bayar");
                }
            }

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("customer_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("marketer_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(10).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("item_klaim")));
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("claim_date") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            }
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            }
            if (h.getFieldValueByFieldNameDT("policy_date") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                row.createCell(16).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
            }
            if (h.getFieldValueByFieldNameST("coinsurer_name") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("coinsurer_name"));
            }
            if (h.getFieldValueByFieldNameST("claim_no") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameST("claim_no"));
            }
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (getStItemClaimID() != null) {
                if (getStItemClaimID().equalsIgnoreCase("48")) {
                    if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                        row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
                    }
                    if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                        row.createCell(21).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
                    }
                }
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList SUMM_REAS_REINS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (stFltTreatyType != null) {
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {
                sqa.addSelect("	a.pol_id,a.pol_no,i.claim_ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
            } else {
                sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
            }
        } else {
            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
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

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {
            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
        }

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
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
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {
                sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.claim_ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                        + " order by k.short_name,f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,a.pol_no";
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
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {
                sql = " select * from ( " + sql + " ) a where a.claim_amount_est <> 0 ";
            } else {
                sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                        + "sum(claim_amount) as claim_amount,sum(claim_amount_est) as claim_amount_est from ( "
                        + sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ) a where a.claim_amount_est <> 0 "
                        + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
            }
        } else {
            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                    + "sum(claim_amount) as claim_amount,sum(claim_amount_est) as claim_amount_est from ( "
                    + sql + " order by a.cust_name,a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type ) a where a.claim_amount_est <> 0 "
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
                sqa.addSelect("	a.pol_id,a.pol_no,i.claim_ri_slip_no as ref2,j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
            } else {
                sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                        + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
            }
        } else {
            sqa.addSelect("j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.short_desc as ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd2,getperiod(a.pol_type_id in (1,3,5,81),c.refd1,a.period_start))) as period_start,"
                    + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),c.refd3,getperiod(a.pol_type_id in (1,3,5,81),c.refd2,a.period_end))) as period_end,"
                    + " k.short_name as cust_name,sum(round(i.claim_amount,2)) as claim_amount,sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount_est ");
        }

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + " inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + " inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + " inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + " inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + " inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + " inner join ent_master k on k.ent_id = i.member_ent_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause("	j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
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

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {
            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(stPolicyTypeID);
            }
        }

        if (stPolCredit != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
        }

        if (treatyTypeList != null) {
            sqa.addClause("j.treaty_type in (" + treatyTypeList + ")");
        } else {
            if (stFltTreatyType != null) {
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
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {
                sql = sqa.getSQL() + " group by a.pol_id,a.pol_no,i.claim_ri_slip_no,j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                        + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name,a.pol_no";
            } else {
                sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                        + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
            }
        } else {
            sql = sqa.getSQL() + " group by j.treaty_type,a.pol_type_id,f.short_desc,f.ins_policy_type_grp_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end "
                    + " order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.short_name";
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
            if (stFltTreatyType.equalsIgnoreCase("FAC") || stFltTreatyType.equalsIgnoreCase("FACO") || stFltTreatyType.equalsIgnoreCase("JP")) {
                sql = " select * from ( " + sql + " ) a where a.claim_amount_est <> 0 ";
            } else {
                sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                        + "sum(claim_amount) as claim_amount,sum(claim_amount_est) as claim_amount_est from ( "
                        + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where a.claim_amount_est <> 0 "
                        + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
            }
        } else {
            sql = "select a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name,"
                    + "sum(claim_amount) as claim_amount,sum(claim_amount_est) as claim_amount_est from ( "
                    + sql + " order by a.ins_policy_type_grp_id,a.pol_type_id,a.treaty_type,a.cust_name ) a where a.claim_amount_est <> 0 "
                    + "group by a.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ref1,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.cust_name ";
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

    public RegionView getRegion() {
        final RegionView region = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegion);

        return region;
    }

    public DTOList KLAIM_POS3() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.dla_date,a.dla_no,a.pol_id,a.pol_no,a.cust_name,a.short_desc as ref1,a.ccy,"
                + "a.ccy_rate_claim,a.item_klaim as ref2,a.claim_cause,a.claim_amount,a.active_flag,a.refd2,a.cc_code,a.ins_policy_type_grp_id ");

        sqa.addQuery(" from claim3 a ");

        if (EFF_CLAIM) {
            sqa.addClause("a.claim_effective_flag = 'Y'");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.approved_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.approved_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (ACT_CLAIM) {
            sqa.addClause("a.claim_effective_flag is null");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'N'");

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.dla_date) >= ?");
                sqa.addPar(appDateFrom);
            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.dla_date) <= ?");
                sqa.addPar(appDateTo);
            }

        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.customerid = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("a.marketerid = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stItemClaimID != null) {
            sqa.addClause("a.itemid = ? ");
            sqa.addPar(stItemClaimID);
        }

        String sql = null;

        if (stItemClaimID.equalsIgnoreCase("48")) {

            sql = "select a.*,b.receipt_date,b.receipt_no "
                    + " from ( " + sqa.getSQL() + " ) a "
                    + "  inner join ar_invoice b on b.attr_pol_id::text = a.pol_id "
                    + " inner join ar_invoice_details c on c.ar_invoice_id = b.ar_invoice_id and c.ar_trx_line_id = 59 "
                    + " where a.active_flag='Y'";
        } else {

            sql = "select a.* "
                    + " from ( " + sqa.getSQL() + " ) a "
                    + " where a.active_flag='Y'";
        }

        if (getPeriodFrom() != null) {
            sql = sql + " and date_trunc('day',a.refd2) >= ? ";
            sqa.addPar(periodFrom);
        }

        if (getPeriodTo() != null) {
            sql = sql + " and date_trunc('day',a.refd2) <= ? ";
            sqa.addPar(periodTo);
        }

        sql = sql + " order by a.cc_code,a.ins_policy_type_grp_id,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_KLAIM_REJECT() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("  ';'||a.pol_no as polno,a.pla_no as lks,a.dla_no as lkp,"
                + "(getperiod(a.pol_type_id in (4,21,59),b.refd2,getperiod(a.pol_type_id in (1,3,24,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59),b.refd3,getperiod(a.pol_type_id in (1,3,24,81),b.refd2,a.period_end))) as period_end,"
                + "a.cust_name as cust_name,e.ent_name as sumbis,f.ent_name as marketer,"
                + "case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end as name,"
                + "k.cause_desc as penyebab,coalesce((a.insured_amount*a.ccy_rate),0) as tsi,coalesce((a.claim_amount*a.ccy_rate_claim),0) as klaim,"
                + "a.reject_date as tgl_tolak,a.reject_notes as catatan ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause ");

//        sqa.addClause("a.claim_status in ('PLA','DLA')");
//        sqa.addClause("a.active_flag = 'N' ");
//        sqa.addClause("coalesce(a.effective_flag,null) <> 'Y'");
//        sqa.addClause("a.status in('CLAIM','CLAIM ENDORSE')");

        sqa.addClause("a.claim_status in ('DLA')");
        sqa.addClause("a.active_flag = 'N' ");
        sqa.addClause("coalesce(a.effective_flag,null) <> 'Y'");
        sqa.addClause("a.status in('CLAIM')");

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= '" + PLADateFrom + "'");
            //sqa.addPar(appDateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= '" + PLADateTo + "'");
            //sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= '" + DLADateFrom + "'");
            //sqa.addPar(appDateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= '" + DLADateTo + "'");
            //sqa.addPar(appDateTo);
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

        sql = sql + " order by a.cc_code,a.pol_no,a.pla_no,a.dla_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_CLAIM_REJECT_" + System.currentTimeMillis() + ".csv";

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
     * @return the stCompTypeID
     */
    public String getStCompTypeID() {
        return stCompTypeID;
    }

    /**
     * @param stCompTypeID the stCompTypeID to set
     */
    public void setStCompTypeID(String stCompTypeID) {
        this.stCompTypeID = stCompTypeID;
    }

    /**
     * @return the stCompTypeName
     */
    public String getStCompTypeName() {
        return stCompTypeName;
    }

    /**
     * @param stCompTypeName the stCompTypeName to set
     */
    public void setStCompTypeName(String stCompTypeName) {
        this.stCompTypeName = stCompTypeName;
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

    public DTOList KLAIM_SUBROGASI2() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,a.pol_type_id,checkstatus(a.ins_policy_type_grp_id in (7,8,30),a.ref5,a.cust_name) as cust_name,"
                + "a.period_start,a.period_end,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,a.pol_no,"
                + "row_number() over(partition by a.pla_no order by a.dla_no) as ref1,"
                + "a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date,a.claim_amount");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.ref13 = 'Y'");
        //sqa.addClause("a.pla_no in ('LKS/59/70/0517/0007','LKS/56/24/0217/0001','LKS/60/12/1115/0001','LKS/59/16/1015/0005')");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        String query = "select a.pla_no from ins_policy a where a.status = 'CLAIM' and a.ref13 = 'Y' "
                + "and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
        // + "and a.pla_no in ('LKS/59/70/0517/0007','LKS/56/24/0217/0001','LKS/60/12/1115/0001','LKS/59/16/1015/0005')";

        if (getAppDateFrom() != null) {
            query = query + " and date_trunc('day',a.approved_date) >= '" + getAppDateFrom() + "'";
        }

        if (getAppDateTo() != null) {
            query = query + " and date_trunc('day',a.approved_date) <= '" + getAppDateTo() + "'";
        }

        sqa.addClause("a.pla_no in (" + query + ")");

        final String sql = sqa.getSQL() + " order by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,pol_no,a.dla_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList KLAIM_SUBROGASI1() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8,30),a.ref5,a.cust_name) as cust_name,"
                + "a.period_start,a.period_end,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,"
                + "a.pol_no,a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date,"
                + "c.negative_flag,checkstatus(b.ins_item_id = 48,'subrogasi',checkstatus(a.status = 'CLAIM','klaim','klaim_e')) as claim_status,"
                + "getpremi(a.status in ('CLAIM','CLAIM ENDORSE') and b.ins_item_id <> 48,b.amount,b.amount) as claim_amount ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_items b on b.pol_id = a.pol_id and b.item_class = 'CLM' "
                + "inner join ins_items c on c.ins_item_id = b.ins_item_id ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.ref13 = 'Y'");
        //sqa.addClause("a.pla_no in ('LKS/59/70/0517/0007','LKS/56/24/0217/0001','LKS/60/12/1115/0001','LKS/59/16/1015/0005')");

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        String query = "select a.pla_no from ins_policy a where a.status = 'CLAIM' and a.ref13 = 'Y' "
                + "and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";
//        + "and a.pla_no in ('LKS/59/30/1115/0011','LKS/59/15/0816/0011','LKS/59/52/0516/0006') ";

        if (getAppDateFrom() != null) {
            query = query + " and date_trunc('day',a.approved_date) >= '" + getAppDateFrom() + "'";
        }

        if (getAppDateTo() != null) {
            query = query + " and date_trunc('day',a.approved_date) <= '" + getAppDateTo() + "'";
        }

        sqa.addClause("a.pla_no in (" + query + ") ");

        final String sql = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.cust_name,a.period_start,a.period_end,a.insured_amount,a.claim_date,"
                + "a.pol_id,pol_no,a.pla_no,a.dla_no,a.claim_status,"
                + "checkstatus(a.claim_status = 'subrogasi',row_number() over(partition by a.pla_no,a.claim_status order by a.dla_no)::text,0::text) as ref1,"
                + "a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date,sum(getpremi(a.negative_flag='Y',a.claim_amount*-1,a.claim_amount)) as claim_amount from ( "
                + sqa.getSQL() + " order by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,pol_no,a.dla_no,b.ins_item_id "
                + ") a group by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,cust_name,a.period_start,a.period_end,a.insured_amount,a.claim_date,"
                + "a.pol_id,a.pol_no,a.pla_no,a.dla_no,a.claim_status,a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date "
                + "order by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.pol_no,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public BigDecimal getClaimAmount(String claim_no, String claim_status) throws Exception {

        DTOList gll = getClaimAmountQ(claim_no, claim_status);

        if (gll == null) {
            return null;
        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");
        }

        InsurancePolicyView gli = (InsurancePolicyView) gll.get(0);

        return gli.getDbAmount();

    }

    public DTOList getClaimAmountQ(String claim_no, String claim_status) throws Exception {

        SQLUtil S = new SQLUtil();

        try {

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,"
                    + "checkstatus(a.ins_policy_type_grp_id in (7,8,30),a.ref5,a.cust_name) as cust_name,"
                    + "a.period_start,a.period_end,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,"
                    + "a.pol_no,a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date,"
                    + "c.negative_flag,checkstatus(b.ins_item_id = 48,'subrogasi',checkstatus(a.status = 'CLAIM','klaim','klaim_e')) as claim_status,"
                    + "getpremi(a.status in ('CLAIM','CLAIM ENDORSE') and b.ins_item_id <> 48,b.amount,b.amount) as claim_amount ");

            sqa.addQuery(" from ins_policy a "
                    + "inner join ins_pol_items b on b.pol_id = a.pol_id and b.item_class = 'CLM' "
                    + "inner join ins_items c on c.ins_item_id = b.ins_item_id ");

            sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");
            sqa.addClause("a.ref13 = 'Y'");

            String query = "select a.pla_no from ins_policy a where a.status = 'CLAIM' and a.ref13 = 'Y' "
                    + "and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";

            sqa.addClause("a.pla_no in (" + query + ") ");

            String sql = "select sum(a.claim_amount) as bx from ( "
                    + "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.cust_name,a.period_start,a.period_end,a.insured_amount,a.claim_date,"
                    + "a.pol_id,pol_no,a.pla_no,a.dla_no,a.claim_status,"
                    + "checkstatus(a.claim_status = 'subrogasi',row_number() over(partition by a.pla_no,a.claim_status order by a.dla_no)::text,0::text) as ref1,"
                    + "a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date,sum(getpremi(a.negative_flag='Y',a.claim_amount*-1,a.claim_amount)) as claim_amount from ( "
                    + sqa.getSQL() + " order by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,pol_no,a.dla_no,b.ins_item_id "
                    + ") a group by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,cust_name,a.period_start,a.period_end,a.insured_amount,a.claim_date,"
                    + "a.pol_id,a.pol_no,a.pla_no,a.dla_no,a.claim_status,a.ccy,a.ccy_rate_claim,a.claim_payment_date,a.claim_approved_date "
                    + "order by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.pol_no,a.dla_no "
                    + " ) a ";

            if (claim_status.equalsIgnoreCase("DLA")) {
                sql = sql + " where a.claim_status in ('subrogasi') and a.dla_no = '" + claim_no + "'";
            } else if (claim_status.equalsIgnoreCase("PLA")) {
                sql = sql + " where a.claim_status in ('klaim','klaim_e') and a.pla_no = '" + claim_no + "'";
            }

            sql = sql + " group by a.pla_no order by a.pla_no ";

            DTOList gll;

            PreparedStatement PS = S.setQuery(sql);

            ResultSet RS = PS.executeQuery();

            RS.next();

            BigDecimal bd = RS.getBigDecimal("bx");

            InsurancePolicyView gli = new InsurancePolicyView();

            gli.setDbAmount(bd);

            gll = new DTOList();

            gll.add(gli);

            return gll;

        } finally {

            S.release();

        }
    }

    public DTOList EXCEL_RKP_OJK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code as koda,d.description as cabang,c.ins_policy_type_grp_id2 as ins_policy_type_grp_id,a.pol_type_id,"
                + "a.pol_id,a.pol_no,b.ent_id,b.ent_name,b.category1 as sumbis,b.address,f.sub_region_name as kabupaten,"
                + "(select count(x.ins_pol_obj_id) from ins_pol_obj x where x.pol_id = a.pol_id) as peserta,"
                + "sum(a.claim_amount*a.ccy_rate_claim) as claim ");

        sqa.addQuery("from ins_policy a "
                + "inner join ent_master b on b.ent_id = a.entity_id "
                + "inner join ent_address f on f.ent_id = b.ent_id "
                + "inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "inner join gl_cost_center d on d.cc_code = a.cc_code ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag='Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        final String sql = " select koda,cabang,ent_name,"
                + "sum(getpremi2(ins_policy_type_grp_id = 16,claim)) as claim1,"
                + "sum(getpremi2(ins_policy_type_grp_id = 16,polno)) as polno1,"
                + "sum(getpremi2(ins_policy_type_grp_id = 16,peserta)) as peserta1,"
                + "sum(getpremi2(ins_policy_type_grp_id = 17,claim)) as claim2,"
                + "sum(getpremi2(ins_policy_type_grp_id = 17,polno)) as polno2,"
                + "sum(getpremi2(ins_policy_type_grp_id = 17,peserta)) as peserta2,"
                + "sum(getpremi2(ins_policy_type_grp_id = 18,claim)) as claim3,"
                + "sum(getpremi2(ins_policy_type_grp_id = 18,polno)) as polno3,"
                + "sum(getpremi2(ins_policy_type_grp_id = 18,peserta)) as peserta3,"
                + "sum(getpremi2(ins_policy_type_grp_id = 19,claim)) as claim4,"
                + "sum(getpremi2(ins_policy_type_grp_id = 19,polno)) as polno4,"
                + "sum(getpremi2(ins_policy_type_grp_id = 19,peserta)) as peserta4,"
                + "sum(getpremi2(ins_policy_type_grp_id = 20,claim)) as claim5,"
                + "sum(getpremi2(ins_policy_type_grp_id = 20,polno)) as polno5,"
                + "sum(getpremi2(ins_policy_type_grp_id = 20,peserta)) as peserta5,"
                + "sum(getpremi2(ins_policy_type_grp_id = 21,claim)) as claim6,"
                + "sum(getpremi2(ins_policy_type_grp_id = 21,polno)) as polno6,"
                + "sum(getpremi2(ins_policy_type_grp_id = 21,peserta)) as peserta6,"
                + "sum(getpremi2(ins_policy_type_grp_id = 22,claim)) as claim7,"
                + "sum(getpremi2(ins_policy_type_grp_id = 22,polno)) as polno7,"
                + "sum(getpremi2(ins_policy_type_grp_id = 22,peserta)) as peserta7,"
                + "sum(getpremi2(ins_policy_type_grp_id = 23,claim)) as claim8,"
                + "sum(getpremi2(ins_policy_type_grp_id = 23,polno)) as polno8,"
                + "sum(getpremi2(ins_policy_type_grp_id = 23,peserta)) as peserta8,"
                + "sum(getpremi2(ins_policy_type_grp_id = 24,claim)) as claim9,"
                + "sum(getpremi2(ins_policy_type_grp_id = 24,polno)) as polno9,"
                + "sum(getpremi2(ins_policy_type_grp_id = 24,peserta)) as peserta9,"
                + "sum(getpremi2(ins_policy_type_grp_id = 25,claim)) as claim10,"
                + "sum(getpremi2(ins_policy_type_grp_id = 25,polno)) as polno10,"
                + "sum(getpremi2(ins_policy_type_grp_id = 25,peserta)) as peserta10,"
                + "sum(getpremi2(ins_policy_type_grp_id = 26,claim)) as claim11,"
                + "sum(getpremi2(ins_policy_type_grp_id = 26,polno)) as polno11,"
                + "sum(getpremi2(ins_policy_type_grp_id = 26,peserta)) as peserta11,"
                + "sum(getpremi2(ins_policy_type_grp_id = 27,claim)) as claim12,"
                + "sum(getpremi2(ins_policy_type_grp_id = 27,polno)) as polno12,"
                + "sum(getpremi2(ins_policy_type_grp_id = 27,peserta)) as peserta12,"
                + "sum(getpremi2(ins_policy_type_grp_id = 28,claim)) as claim13,"
                + "sum(getpremi2(ins_policy_type_grp_id = 28,polno)) as polno13,"
                + "sum(getpremi2(ins_policy_type_grp_id = 28,peserta)) as peserta13,"
                + "sum(getpremi2(ins_policy_type_grp_id = 31,claim)) as claim14,"
                + "sum(getpremi2(ins_policy_type_grp_id = 31,polno)) as polno14,"
                + "sum(getpremi2(ins_policy_type_grp_id = 31,peserta)) as peserta14,"
                + "sum(polno) as polno,sum(peserta) as peserta,sum(claim) as claim "
                + "from ( select koda,cabang,ins_policy_type_grp_id,coalesce(kabupaten,ent_name) as ent_name, "
                + "count(pol_no) as polno,sum(peserta) as peserta,sum(claim) as claim "
                + "from ( " + sqa.getSQL()
                + " group by a.cc_code,d.description,c.ins_policy_type_grp_id2,a.pol_type_id,"
                + "a.pol_id,a.pol_no,b.ent_id,b.ent_name,b.category1,b.address,f.sub_region_name "
                + ") a group by koda,cabang,ins_policy_type_grp_id,kabupaten,ent_name "
                + "order by koda,ins_policy_type_grp_id ) a "
                + "group by koda,cabang,ent_name order by koda,ent_name ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RKP_OJK() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("detail");
        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(1).setCellValue("Koda");
            row0.createCell(2).setCellValue("Provinsi");
            row0.createCell(3).setCellValue("Kabupaten/Kota");
            row0.createCell(4).setCellValue("Harta Benda");
            row0.createCell(7).setCellValue("Kendaraan Bermotor");
            row0.createCell(10).setCellValue("Pengangkutan");
            row0.createCell(13).setCellValue("Rangka Kapal");
            row0.createCell(16).setCellValue("Satelit");
            row0.createCell(19).setCellValue("Energi Darat");
            row0.createCell(22).setCellValue("Energi Offshore");
            row0.createCell(25).setCellValue("Rekayasa");
            row0.createCell(28).setCellValue("Tanggung Gugat");
            row0.createCell(31).setCellValue("Kecelakaan Diri");
            row0.createCell(34).setCellValue("Kredit");
            row0.createCell(37).setCellValue("Penjaminan");
            row0.createCell(40).setCellValue("Aneka");
            row0.createCell(43).setCellValue("Kesehatan");
            row0.createCell(46).setCellValue("Total");

            sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 18));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 19, 21));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 22, 24));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 25, 27));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 28, 30));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 31, 33));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 34, 36));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 37, 39));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 40, 42));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 43, 45));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 46, 48));

            //bikin header
            HSSFRow row1 = sheet.createRow(1);
            row1.createCell(4).setCellValue("jumlah");
            row1.createCell(5).setCellValue("jumlah");
            row1.createCell(6).setCellValue("jumlah");
            row1.createCell(7).setCellValue("jumlah");
            row1.createCell(8).setCellValue("jumlah");
            row1.createCell(9).setCellValue("jumlah");
            row1.createCell(10).setCellValue("jumlah");
            row1.createCell(11).setCellValue("jumlah");
            row1.createCell(12).setCellValue("jumlah");
            row1.createCell(13).setCellValue("jumlah");
            row1.createCell(14).setCellValue("jumlah");
            row1.createCell(15).setCellValue("jumlah");
            row1.createCell(16).setCellValue("jumlah");
            row1.createCell(17).setCellValue("jumlah");
            row1.createCell(18).setCellValue("jumlah");
            row1.createCell(19).setCellValue("jumlah");
            row1.createCell(20).setCellValue("jumlah");
            row1.createCell(21).setCellValue("jumlah");
            row1.createCell(22).setCellValue("jumlah");
            row1.createCell(23).setCellValue("jumlah");
            row1.createCell(24).setCellValue("jumlah");
            row1.createCell(25).setCellValue("jumlah");
            row1.createCell(26).setCellValue("jumlah");
            row1.createCell(27).setCellValue("jumlah");
            row1.createCell(28).setCellValue("jumlah");
            row1.createCell(29).setCellValue("jumlah");
            row1.createCell(30).setCellValue("jumlah");
            row1.createCell(31).setCellValue("jumlah");
            row1.createCell(32).setCellValue("jumlah");
            row1.createCell(33).setCellValue("jumlah");
            row1.createCell(34).setCellValue("jumlah");
            row1.createCell(35).setCellValue("jumlah");
            row1.createCell(36).setCellValue("jumlah");
            row1.createCell(37).setCellValue("jumlah");
            row1.createCell(38).setCellValue("jumlah");
            row1.createCell(39).setCellValue("jumlah");
            row1.createCell(40).setCellValue("jumlah");
            row1.createCell(41).setCellValue("jumlah");
            row1.createCell(42).setCellValue("jumlah");
            row1.createCell(43).setCellValue("jumlah");
            row1.createCell(44).setCellValue("jumlah");
            row1.createCell(45).setCellValue("jumlah");
            row1.createCell(46).setCellValue("jumlah");
            row1.createCell(47).setCellValue("jumlah");
            row1.createCell(48).setCellValue("jumlah");

            HSSFRow row2 = sheet.createRow(2);
            row2.createCell(4).setCellValue("(Rupiah)");
            row2.createCell(5).setCellValue("polis");
            row2.createCell(6).setCellValue("peserta");
            row2.createCell(7).setCellValue("(Rupiah)");
            row2.createCell(8).setCellValue("polis");
            row2.createCell(9).setCellValue("peserta");
            row2.createCell(10).setCellValue("(Rupiah)");
            row2.createCell(11).setCellValue("polis");
            row2.createCell(12).setCellValue("peserta");
            row2.createCell(13).setCellValue("(Rupiah)");
            row2.createCell(14).setCellValue("polis");
            row2.createCell(15).setCellValue("peserta");
            row2.createCell(16).setCellValue("(Rupiah)");
            row2.createCell(17).setCellValue("polis");
            row2.createCell(18).setCellValue("peserta");
            row2.createCell(19).setCellValue("(Rupiah)");
            row2.createCell(20).setCellValue("polis");
            row2.createCell(21).setCellValue("peserta");
            row2.createCell(22).setCellValue("(Rupiah)");
            row2.createCell(23).setCellValue("polis");
            row2.createCell(24).setCellValue("peserta");
            row2.createCell(25).setCellValue("(Rupiah)");
            row2.createCell(26).setCellValue("polis");
            row2.createCell(27).setCellValue("peserta");
            row2.createCell(28).setCellValue("(Rupiah)");
            row2.createCell(29).setCellValue("polis");
            row2.createCell(30).setCellValue("peserta");
            row2.createCell(31).setCellValue("(Rupiah)");
            row2.createCell(32).setCellValue("polis");
            row2.createCell(33).setCellValue("peserta");
            row2.createCell(34).setCellValue("(Rupiah)");
            row2.createCell(35).setCellValue("polis");
            row2.createCell(36).setCellValue("peserta");
            row2.createCell(37).setCellValue("(Rupiah)");
            row2.createCell(38).setCellValue("polis");
            row2.createCell(39).setCellValue("peserta");
            row2.createCell(40).setCellValue("(Rupiah)");
            row2.createCell(41).setCellValue("polis");
            row2.createCell(42).setCellValue("peserta");
            row2.createCell(43).setCellValue("(Rupiah)");
            row2.createCell(44).setCellValue("polis");
            row2.createCell(45).setCellValue("peserta");
            row2.createCell(46).setCellValue("(Rupiah)");
            row2.createCell(47).setCellValue("polis");
            row2.createCell(48).setCellValue("peserta");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 3);
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claim1").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("polno1").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("peserta1").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim2").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("polno2").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("peserta2").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim3").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("polno3").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("peserta3").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim4").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("polno4").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("peserta4").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("claim5").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("polno5").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("peserta5").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim6").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("polno6").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("peserta6").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim7").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("polno7").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("peserta7").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim8").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("polno8").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("peserta8").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("claim9").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("polno9").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("peserta9").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("claim10").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("polno10").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("peserta10").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("claim11").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("polno11").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("peserta11").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("claim12").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("polno12").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("peserta12").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("claim13").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("polno13").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("peserta13").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("claim14").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("polno14").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("peserta14").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("claim").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("polno").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("peserta").doubleValue());

        }

        //bikin sheet
        HSSFSheet sheetRekap = wb.createSheet("rekap");
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

//bikin header
            HSSFRow row0 = sheetRekap.createRow(0);
            row0.createCell(1).setCellValue("Koda");
            row0.createCell(2).setCellValue("Provinsi");
            row0.createCell(3).setCellValue("Kabupaten/Kota");
            row0.createCell(4).setCellValue("Total Klaim");
            row0.createCell(5).setCellValue("Total Polis");
            row0.createCell(6).setCellValue("Total Peserta");

            //bikin isi cell
            HSSFRow row = sheetRekap.createRow(i + 1);
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claim").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("polno").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("peserta").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList REKAP_KLAIM_USIA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_type_id,c.cause_desc as claim_cause_desc, "
                + "sum(coalesce((a.claim_amount*a.ccy_rate_claim),0)) as claim_amount ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_clm_cause c on c.ins_clm_caus_id = a.claim_cause ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag='Y'");
        sqa.addClause("a.pol_type_id in (21,59)");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        if (usiaFrom != null) {
            sqa.addClause("b.ref2 >= ?");
            sqa.addPar(usiaFrom);
        }

        if (usiaTo != null) {
            sqa.addClause("b.ref2 <= ?");
            sqa.addPar(usiaTo);
        }

        String sql = sqa.getSQL() + " group by 1,2 order by 1,2 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    /**
     * @return the usiaFrom
     */
    public String getUsiaFrom() {
        return usiaFrom;
    }

    /**
     * @param usiaFrom the usiaFrom to set
     */
    public void setUsiaFrom(String usiaFrom) {
        this.usiaFrom = usiaFrom;
    }

    /**
     * @return the usiaTo
     */
    public String getUsiaTo() {
        return usiaTo;
    }

    /**
     * @param usiaTo the usiaTo to set
     */
    public void setUsiaTo(String usiaTo) {
        this.usiaTo = usiaTo;
    }

    public DTOList EXCEL_DETAILKLAIM_OLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	f.description as cabang,a.policy_date,a.claim_date,a.claim_approved_date,a.dla_date,"
                + "a.pol_id,quote_ident(a.pol_no) as pol_no,a.pla_no,a.dla_no,a.cust_name as cust_name,"
                + "upper(checkstatus(a.ins_policy_type_grp_id=10,b.ref2,checkstatus(a.pol_type_id=60,b.description,b.ref1))) as name,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "coalesce((b.insured_amount*a.ccy_rate_claim),0) as insured_amount,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,k.cause_desc,"
                + "sum(checkreas(c.ins_item_id in (46),c.amount*a.ccy_rate_claim)) as KlaimBruto,"
                + "sum(checkreas(c.ins_item_id in (47),((c.amount*-1)*a.ccy_rate_claim))) as Deductible,"
                + "sum(checkreas(c.ins_item_id in (48),((c.amount*-1)*a.ccy_rate_claim))) as Subrogasi,"
                + "sum(checkreas(c.ins_item_id in (49),c.amount*a.ccy_rate_claim)) as Wreck,"
                + "sum(checkreas(c.ins_item_id in (50),c.amount*a.ccy_rate_claim)) as BiayaAdjuster,"
                + "sum(checkreas(c.ins_item_id in (51),c.amount*a.ccy_rate_claim)) as TJH,"
                + "sum(checkreas(c.ins_item_id in (52),c.amount*a.ccy_rate_claim)) as BiayaDerek,"
                + "sum(checkreas(c.ins_item_id in (53),((c.amount*-1)*a.ccy_rate_claim))) as Salvage,"
                + "sum(checkreas(c.ins_item_id in (54),c.amount*a.ccy_rate_claim)) as ExGratiaKlaim,"
                + "sum(checkreas(c.ins_item_id in (55),c.amount*a.ccy_rate_claim)) as Bunga,"
                + "sum(checkreas(c.ins_item_id in (56),c.amount*a.ccy_rate_claim)) as SantunanKecelakaan,"
                + "sum(checkreas(c.ins_item_id in (60),((c.amount*-1)*a.ccy_rate_claim))) as Depresiasi,"
                + "sum(checkreas(c.ins_item_id in (62),c.amount*a.ccy_rate_claim)) as UangMukaPremi,"
                + "sum(checkreas(c.ins_item_id in (63),c.amount*a.ccy_rate_claim)) as UangMukaKomisi,"
                + "sum(checkreas(c.ins_item_id in (64),c.amount*a.ccy_rate_claim)) as InterimPayment,"
                + "sum(checkreas(c.ins_item_id in (65),((c.amount*-1)*a.ccy_rate_claim))) as Penalty,"
                + "sum(checkreas(c.ins_item_id in (70),c.amount*a.ccy_rate_claim)) as JasaBengkel,"
                + "sum(checkreas(c.ins_item_id in (70) and c.tax_code is not null,((c.tax_amount*-1)*a.ccy_rate_claim))) as PajakJasaBengkel,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as ExGratiaBebanUnderwritingLainLain,"
                + "sum(checkreas(c.ins_item_id in (73),c.amount*a.ccy_rate_claim)) as FeeRecovery,"
                + "sum(checkreas(c.ins_item_id in (74),c.amount*a.ccy_rate_claim)) as BiayaSparepart,"
                + "sum(checkreas(c.ins_item_id in (75),((c.amount*-1)*a.ccy_rate_claim))) as JoinPlacement,"
                + "sum(checkreas(c.ins_item_id in (76),c.amount*a.ccy_rate_claim)) as PPN,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as BiayaSurvey,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as PPNTotal,"
                + "sum(checkreas(c.ins_item_id in (79),((c.amount*-1)*a.ccy_rate_claim))) as CashCollateralSubrograsi,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as Material,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as SurveyAdjusmentFee,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as Expenses,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as VATFee,"
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as BiayaAdministrasiMateraidll,"
                + "sum(checkreas(c.ins_item_id in (109),c.amount*a.ccy_rate_claim)) as ForensicFee,"
                + "a.claim_chronology as kronologi,g.vs_description as potensi_subro,"
                + "(select string_agg(g.receipt_date::text, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_date,"
                + "(select string_agg(g.receipt_no, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_no ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id "
                + "inner join ent_master d on d.ent_id = a.entity_id "
                + "inner join ins_items e on e.ins_item_id = c.ins_item_id "
                + "inner join gl_cost_center f on f.cc_code = a.cc_code "
                + "left join s_valueset g on g.vs_code = a.ref13 and g.vs_group='SUBROGASI' "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "inner join ent_master h on h.ent_id = a.prod_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        if (stCustCategory1 != null) {
            sqa.addClause("d.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCompanyID != null) {
            sqa.addClause("d.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stMarketerID != null) {
            sqa.addClause("a.prod_id = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
            sqa.addClause("h.ref2 = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("d.ref1 in " + type);
//            sqa.addPar(type);
        }

        final String sql = sqa.getSQL() + " group by a.policy_date,a.claim_date,a.claim_approved_date,a.dla_date,a.pol_id,a.pol_no,a.pla_no,a.dla_no,a.cust_name,"
                + "a.ins_policy_type_grp_id,b.ref2,b.ref1,b.refd1,b.insured_amount,a.ccy_rate_claim,a.claim_amount,k.cause_desc,f.description,b.description,g.vs_description "
                + "order by a.cc_code,a.dla_date,a.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_DETAILKLAIM_OLD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("tglpolis");
            row0.createCell(2).setCellValue("tglklaim");
            row0.createCell(3).setCellValue("tglsetujui");
            row0.createCell(4).setCellValue("tgldla");
            row0.createCell(5).setCellValue("polid");
            row0.createCell(6).setCellValue("polno");
            row0.createCell(7).setCellValue("plano");
            row0.createCell(8).setCellValue("dlano");
            row0.createCell(9).setCellValue("customer");
            row0.createCell(10).setCellValue("debitur");
            row0.createCell(11).setCellValue("tgllahir");
            row0.createCell(12).setCellValue("insured");
            row0.createCell(13).setCellValue("klaim");
            row0.createCell(14).setCellValue("klaimbruto");
            row0.createCell(15).setCellValue("deductible");
            row0.createCell(16).setCellValue("subrogasi");
            row0.createCell(17).setCellValue("wreck");
            row0.createCell(18).setCellValue("biayaadjuster");
            row0.createCell(19).setCellValue("tjh");
            row0.createCell(20).setCellValue("biayaderek");
            row0.createCell(21).setCellValue("salvage");
            row0.createCell(22).setCellValue("exgratiaklaim");
            row0.createCell(23).setCellValue("bunga");
            row0.createCell(24).setCellValue("santunankecelakaan");
            row0.createCell(25).setCellValue("depresiasi");
            row0.createCell(26).setCellValue("uangmukapremi");
            row0.createCell(27).setCellValue("uangmukakomisi");
            row0.createCell(28).setCellValue("interimpayment");
            row0.createCell(29).setCellValue("penalty");
            row0.createCell(30).setCellValue("jasabengkel");
            row0.createCell(31).setCellValue("pajakjasabengkel");
            row0.createCell(32).setCellValue("exgratiabebanunderwritinglainlain");
            row0.createCell(33).setCellValue("feerecovery");
            row0.createCell(34).setCellValue("biayasparepart");
            row0.createCell(35).setCellValue("joinplacement");
            row0.createCell(36).setCellValue("ppn");
            row0.createCell(37).setCellValue("biayasurvey");
            row0.createCell(38).setCellValue("ppntotal");
            row0.createCell(39).setCellValue("cashcollateralsubrograsi");
            row0.createCell(40).setCellValue("material");
            row0.createCell(41).setCellValue("surveyadjusmentfee");
            row0.createCell(42).setCellValue("expenses");
            row0.createCell(43).setCellValue("vatfee");
            row0.createCell(44).setCellValue("biayaadministrasimateraidll");
            row0.createCell(45).setCellValue("forensicfee");
            row0.createCell(46).setCellValue("kronologi");
            row0.createCell(47).setCellValue("penyebab");
            row0.createCell(48).setCellValue("tglbayar");
            row0.createCell(49).setCellValue("buktibayar");
            row0.createCell(50).setCellValue("potensi subrogasi");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("name"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("deductible").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("subrogasi").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("wreck").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("biayaadjuster").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("tjh").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("biayaderek").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("exgratiaklaim").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("santunankecelakaan").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("depresiasi").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("uangmukapremi").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("uangmukakomisi").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("interimpayment").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("penalty").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("pajakjasabengkel").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("exgratiabebanunderwritinglainlain").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("feerecovery").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("biayasparepart").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("joinplacement").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("biayasurvey").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("ppntotal").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("cashcollateralsubrograsi").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("surveyadjusmentfee").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("expenses").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("vatfee").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("biayaadministrasimateraidll").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("forensicfee").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            row.createCell(47).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameST("receipt_date"));
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            row.createCell(50).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("potensi_subro")));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_DETAILKLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("period_start");
            row0.createCell(1).setCellValue("period_end");
            row0.createCell(2).setCellValue("cabang");
            row0.createCell(3).setCellValue("status");
            row0.createCell(4).setCellValue("pol_type_id");
            row0.createCell(5).setCellValue("propose_date");
            row0.createCell(6).setCellValue("dla_date");
            row0.createCell(7).setCellValue("claim_date");
            row0.createCell(8).setCellValue("policy_date");
            row0.createCell(9).setCellValue("pol_id");
            row0.createCell(10).setCellValue("pol_no");
            row0.createCell(11).setCellValue("pla_no");
            row0.createCell(12).setCellValue("dla_no");
            row0.createCell(13).setCellValue("sumbis");
            row0.createCell(14).setCellValue("pemasar");
            row0.createCell(15).setCellValue("cust_name");
            row0.createCell(16).setCellValue("ccy_rate_claim");
            row0.createCell(17).setCellValue("tertanggung");
            row0.createCell(18).setCellValue("tgl_lahir");
            row0.createCell(19).setCellValue("insured_amount");
            row0.createCell(20).setCellValue("claim_amount");
            row0.createCell(21).setCellValue("claim_amount_est");
            row0.createCell(22).setCellValue("claim_amount_approved");
            row0.createCell(23).setCellValue("klaim_or");
            row0.createCell(24).setCellValue("klaim_bppdan");
            row0.createCell(25).setCellValue("klaim_spl");
            row0.createCell(26).setCellValue("klaim_fac");
            row0.createCell(27).setCellValue("klaim_qs");
            row0.createCell(28).setCellValue("klaim_park");
            row0.createCell(29).setCellValue("klaim_faco");
            row0.createCell(30).setCellValue("klaim_faco1");
            row0.createCell(31).setCellValue("klaim_faco2");
            row0.createCell(32).setCellValue("klaim_faco3");
            row0.createCell(33).setCellValue("klaim_jp");
            row0.createCell(34).setCellValue("klaim_qskr");
            row0.createCell(35).setCellValue("klaim_kscbi");
            row0.createCell(36).setCellValue("approved_date");
            row0.createCell(37).setCellValue("penyebab");
            row0.createCell(38).setCellValue("tgl_bayar");
            row0.createCell(39).setCellValue("nobuk");
            row0.createCell(40).setCellValue("cc_code_source");
            row0.createCell(41).setCellValue("klaimbruto");
            row0.createCell(42).setCellValue("deductible");
            row0.createCell(43).setCellValue("subrogasi");
            row0.createCell(44).setCellValue("biayaadjuster");
            row0.createCell(45).setCellValue("salvage");
            row0.createCell(46).setCellValue("biayaderek");
            row0.createCell(47).setCellValue("exgratiaklaim");
            row0.createCell(48).setCellValue("jasabengkel");
            row0.createCell(49).setCellValue("pajakjasabengkel");
            row0.createCell(50).setCellValue("ppn");
            row0.createCell(51).setCellValue("depresiasi");
            row0.createCell(52).setCellValue("penalty");
            row0.createCell(53).setCellValue("joinplacement");
            row0.createCell(54).setCellValue("cashcollateralsubrograsi");
            row0.createCell(55).setCellValue("wreck");
            row0.createCell(56).setCellValue("tjh");
            row0.createCell(57).setCellValue("bunga");
            row0.createCell(58).setCellValue("sntn_kclkn");
            row0.createCell(59).setCellValue("exgratia_bebanuw");
            row0.createCell(60).setCellValue("feerecov");
            row0.createCell(61).setCellValue("by_sparepart");
            row0.createCell(62).setCellValue("by_survey");
            row0.createCell(63).setCellValue("ppn");
            row0.createCell(64).setCellValue("material");
            row0.createCell(65).setCellValue("survey_adjfee");
            row0.createCell(66).setCellValue("expenses");
            row0.createCell(67).setCellValue("vat_fee");
            row0.createCell(68).setCellValue("by_adm");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            if (h.getFieldValueByFieldNameDT("propose_date") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("propose_date"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("pemasar"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_est").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("klaim_or").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("klaim_bppdan").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("klaim_spl").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("klaim_fac").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("klaim_qs").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("klaim_park").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("klaim_faco").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("klaim_faco1").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("klaim_faco2").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("klaim_faco3").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("klaim_jp").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("klaim_qskr").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("klaim_kscbi").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("penyebab"));
            if (h.getFieldValueByFieldNameDT("tgl_bayar") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameDT("tgl_bayar"));
            }
            if (h.getFieldValueByFieldNameST("no_bukti") != null) {
                row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("no_bukti"));
            }
            if (h.getFieldValueByFieldNameST("cc_code_source") != null) {
                row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("cc_code_source"));
            }
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("deductible").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("subrogasi").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("biayaadjuster").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("biayaderek").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("exgratiaklaim").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("jasabengkel").doubleValue());
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("pajakjasabengkel").doubleValue());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("depresiasi").doubleValue());
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("penalty").doubleValue());
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("joinplacement").doubleValue());
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("cashcollateralsubrograsi").doubleValue());
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("wreck").doubleValue());
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("tjh").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("sntn_kclkn").doubleValue());
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("exgratia_bebanuw").doubleValue());
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("feerecov").doubleValue());
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("by_sparepart").doubleValue());
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("by_survey").doubleValue());
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("material").doubleValue());
            row.createCell(65).setCellValue(h.getFieldValueByFieldNameBD("survey_adjfee").doubleValue());
            row.createCell(66).setCellValue(h.getFieldValueByFieldNameBD("expenses").doubleValue());
            row.createCell(67).setCellValue(h.getFieldValueByFieldNameBD("vat_fee").doubleValue());
            row.createCell(68).setCellValue(h.getFieldValueByFieldNameBD("by_adm").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void updateOSKlaimLKSLKP(DTOList data, String klaim) throws Exception {

        Date dateReport = null;
        if (klaim.equalsIgnoreCase("LKS")) {
            dateReport = getAppDateTo();
        } else if (klaim.equalsIgnoreCase("LKP")) {
            dateReport = getDLADateTo();
        }

        final SQLUtil S = new SQLUtil();

        try {

            for (int i = 0; i < data.size(); i++) {
                HashDTO h = (HashDTO) data.get(i);

                PreparedStatement PS = S.setQuery("update ins_policy set report_date = ? where pol_id = ? ");

                PS.setDate(1, new java.sql.Date(dateReport.getTime()));
                PS.setObject(2, h.getFieldValueByFieldNameST("pol_id"));

                int s = PS.executeUpdate();
            }

        } finally {
            S.release();
        }
    }

//    public void updateBalance(String sql) throws Exception, RemoteException {
//
//        Date dateReport = null;
//        if (klaim.equalsIgnoreCase("LKS")) {
//            dateReport = getAppDateTo();
//        } else if (klaim.equalsIgnoreCase("LKP")) {
//            dateReport = getDLADateTo();
//        }
//
//        final SQLUtil S = new SQLUtil();
//
//        try {
//            for (int j = 0; j < data.size(); j++) {
//                HashDTO h = (HashDTO) data.get(j);
//
//                PreparedStatement PS = S.setQuery("update ins_policy set report_date = ? where pol_id = ? ");
//
//                PS.setDate(1, new java.sql.Date(dateReport.getTime()));
//                PS.setObject(2, h.getFieldValueByFieldNameST("pol_id"));
//
//                int i = PS.executeUpdate();
//
//                if (i == 0) {
//                    S.releaseResource();
//
//                    PS = S.setQuery("insert into gl_acct_bal(account_id,period_year,period_no,bal) values(?,?,?,?)");
//                    PS.setObject(1, lgAccountID);
//                    PS.setObject(2, lgPeriodYear);
//                    PS.setObject(3, lgPeriodNo);
//                    PS.setBigDecimal(4, am);
//
//                    i = PS.executeUpdate();
//
//                    if (i == 0) {
//                        throw new Exception("Failed to update gl account balance");
//                    }
//                }
//            }
//        } finally {
//            S.release();
//        }
//    }
    public DTOList KLAIM_SUBROGASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String searchSubro = "select a.pla_no from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.item_class = 'CLM' and c.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + "and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) ";

        if (appDateFrom != null) {
//            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '" + appDateFrom + "' ";
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '2009-12-01 00:00:00' ";
        }
        if (appDateTo != null) {
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }
        if (stBranch != null) {
            searchSubro = searchSubro + "and a.cc_code = '" + stBranch + "' ";
        }
        if (stRegion != null) {
            searchSubro = searchSubro + "and a.region_id = '" + stRegion + "' ";
        }

        searchSubro = searchSubro + "group by a.pla_no";

        String lkpInduk = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8),b.ref2,checkstatus(a.ins_policy_type_grp_id in (30),b.ref14,a.entity_id::text)) as principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,"
                + "a.claim_approved_date,a.status,a.claim_amount,c.receipt_date as payment_date,c.receipt_no as payment_note "
                + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP' "
                + "where a.status IN ('CLAIM') and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                + "and a.pla_no in ( " + searchSubro + " )";

        String lkpSubro = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,"
                + "sum(claim_amount) as claim_amount,a.payment_date,a.payment_note from ( "
                + "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8),b.ref2,checkstatus(a.ins_policy_type_grp_id in (30),b.ref14,a.entity_id::text)) as principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,"
                + "a.claim_approved_date,a.status,((d.amount*-1)*a.ccy_rate_claim) as claim_amount,c.receipt_date as payment_date,c.receipt_no as payment_note "
                + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP' "
                + "inner join ins_pol_items d on d.pol_id = a.pol_id and d.item_class = 'CLM' and d.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                //                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) "
                + "and a.pla_no in ( " + searchSubro + " ) ";

        if (appDateTo != null) {
            lkpSubro = lkpSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }

        lkpSubro = lkpSubro + " ) a group by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,a.entity_id,"
                + "a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,a.payment_date,a.payment_note ";

        String unionAll = lkpInduk + " union all " + lkpSubro;

        sqa.addSelect(" a.*,b.ent_name as prod_name,c.ent_name as cust_name ");

        sqa.addQuery(" from ( " + unionAll + " ) a "
                + "inner join ent_master b on b.ent_id = a.principal_id::int "
                + "inner join ent_master c on c.ent_id = a.entity_id "
                + "inner join gl_cost_center d on d.cc_code = a.cc_code "
                + "inner join ins_policy_types e on e.pol_type_id = a.pol_type_id ");

        String sql = sqa.getSQL() + " order by pla_no,dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_KLAIM_SUBROGASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String searchSubro = "select a.pla_no from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.item_class = 'CLM' and c.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + "and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) ";

        if (appDateFrom != null) {
//            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '" + appDateFrom + "' ";
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '2009-12-01 00:00:00' ";
        }
        if (appDateTo != null) {
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }
        if (stBranch != null) {
            searchSubro = searchSubro + "and a.cc_code = '" + stBranch + "' ";
        }
        if (stRegion != null) {
            searchSubro = searchSubro + "and a.region_id = '" + stRegion + "' ";
        }

        searchSubro = searchSubro + "group by a.pla_no";

        String lkpInduk = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8),b.ref2,checkstatus(a.ins_policy_type_grp_id in (30),b.ref14,a.entity_id::text)) as principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,"
                + "a.claim_approved_date,a.status,a.claim_amount,c.receipt_date as payment_date,c.receipt_no as payment_note "
                + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP' "
                + "where a.status IN ('CLAIM') and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                + "and a.pla_no in ( " + searchSubro + " )";

        String lkpSubro = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,"
                + "sum(claim_amount) as claim_amount,a.payment_date,a.payment_note from ( "
                + "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8),b.ref2,checkstatus(a.ins_policy_type_grp_id in (30),b.ref14,a.entity_id::text)) as principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,"
                + "a.claim_approved_date,a.status,((d.amount*-1)*a.ccy_rate_claim) as claim_amount,c.receipt_date as payment_date,c.receipt_no as payment_note "
                + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP' "
                + "inner join ins_pol_items d on d.pol_id = a.pol_id and d.item_class = 'CLM' and d.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                //                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) "
                + "and a.pla_no in ( " + searchSubro + " ) ";

        if (appDateTo != null) {
            lkpSubro = lkpSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }

        lkpSubro = lkpSubro + " ) a group by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,a.entity_id,"
                + "a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,a.payment_date,a.payment_note ";

        String unionAll = lkpInduk + " union all " + lkpSubro;

        sqa.addSelect(" d.description as cabang,e.short_desc as jenas,a.*,b.ent_name as principal_name,c.ent_name as cust_name ");

        sqa.addQuery(" from ( " + unionAll + " ) a "
                + "inner join ent_master b on b.ent_id = a.principal_id::int "
                + "inner join ent_master c on c.ent_id = a.entity_id "
                + "inner join gl_cost_center d on d.cc_code = a.cc_code "
                + "inner join ins_policy_types e on e.pol_type_id = a.pol_type_id ");

        String batal = " select a.pla_no from ins_policy a "
                + "where a.status in ('CLAIM ENDORSE') and a.ref13 = 'I' "
                + "and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";

        if (appDateFrom != null) {
            batal = batal + " and date_trunc('day',a.approved_date) >= '" + appDateFrom + "'";
        }

        if (appDateTo != null) {
            batal = batal + " and date_trunc('day',a.approved_date) <= '" + appDateTo + "'";
        }

        sqa.addClause("a.pla_no not in (" + batal + ")");

        String sql = sqa.getSQL() + " order by pla_no,dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

        /*SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_CLAIM_SUBROGASI_" + System.currentTimeMillis() + ".csv";

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

        file.delete();*/
    }

    public DTOList EXCEL_KLAIM_SUBROGASI_REKAP() throws Exception {
        final boolean PERJENIS = "Y".equalsIgnoreCase((String) refPropMap.get("PERJENIS"));
        final boolean PERCABANG = "Y".equalsIgnoreCase((String) refPropMap.get("PERCABANG"));

        final SQLAssembler sqa = new SQLAssembler();

        String searchSubro = "select a.pla_no from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.item_class = 'CLM' and c.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' "
                + "and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) ";

        if (appDateFrom != null) {
//            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '" + appDateFrom + "' ";
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) >= '2009-12-01 00:00:00' ";
        }
        if (appDateTo != null) {
            searchSubro = searchSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }
        if (stBranch != null) {
            searchSubro = searchSubro + "and a.cc_code = '" + stBranch + "' ";
        }
        if (stRegion != null) {
            searchSubro = searchSubro + "and a.region_id = '" + stRegion + "' ";
        }

        searchSubro = searchSubro + "group by a.pla_no";

        String lkpSubro = "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,"
                + "sum(claim_amount) as claim_amount,a.payment_date,a.payment_note from ( "
                + "select a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,"
                + "checkstatus(a.ins_policy_type_grp_id in (7,8),b.ref2,checkstatus(a.ins_policy_type_grp_id in (30),b.ref14,a.entity_id::text)) as principal_id,"
                + "a.entity_id,a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,"
                + "a.claim_approved_date,a.status,((d.amount*-1)*a.ccy_rate_claim) as claim_amount,c.receipt_date as payment_date,c.receipt_no as payment_note "
                + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ar_invoice c on c.attr_pol_id = a.pol_id and c.ar_trx_type_id = 12 and coalesce(c.cancel_flag,'') <> 'Y' and c.amount_settled is not null and c.invoice_type = 'AP' "
                + "inner join ins_pol_items d on d.pol_id = a.pol_id and d.item_class = 'CLM' and d.ins_item_id = 48 "
                + "where a.status IN ('CLAIM ENDORSE') and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                //                + "and a.pol_type_id in (21,59,45,46,51,52,53,54,47,48,55,56,57,58,73,74,60,3) "
                + "and a.pla_no in ( " + searchSubro + " ) ";

        if (appDateTo != null) {
            lkpSubro = lkpSubro + "and date_trunc('day',a.approved_date) <= '" + appDateTo + "' ";
        }

        lkpSubro = lkpSubro + " ) a group by a.cc_code,a.ins_policy_type_grp_id,a.pol_type_id,a.period_start,a.period_end,a.principal_id,a.entity_id,"
                + "a.insured_amount,a.claim_date,a.pol_id,a.pla_no,a.dla_no,pol_no,a.claim_approved_date,a.status,a.payment_date,a.payment_note ";

        sqa.addSelect(" substr(a.claim_approved_date::text,1,4) as tahun,"
                + "substr(a.claim_approved_date::text,6,2) as bulan,"
                + "a.* ");

        sqa.addQuery(" from ( " + lkpSubro + " ) a ");

//        String sql = sqa.getSQL() + " order by pla_no,dla_no ";

        String sql = null;
        if (PERJENIS) {
            sql = "select tahun,bulan,"
                    + "sum(getklaim(pol_type_id = 21,claim_amount)) as claim21,"
                    + "sum(getklaim(pol_type_id = 59,claim_amount)) as claim59,"
                    + "sum(getklaim(pol_type_id = 45,claim_amount)) as claim45,"
                    + "sum(getklaim(pol_type_id = 46,claim_amount)) as claim46,"
                    + "sum(getklaim(pol_type_id = 51,claim_amount)) as claim51,"
                    + "sum(getklaim(pol_type_id = 52,claim_amount)) as claim52,"
                    + "sum(getklaim(pol_type_id = 53,claim_amount)) as claim53,"
                    + "sum(getklaim(pol_type_id = 54,claim_amount)) as claim54,"
                    + "sum(getklaim(pol_type_id = 47,claim_amount)) as claim47,"
                    + "sum(getklaim(pol_type_id = 48,claim_amount)) as claim48,"
                    + "sum(getklaim(pol_type_id = 55,claim_amount)) as claim55,"
                    + "sum(getklaim(pol_type_id = 56,claim_amount)) as claim56,"
                    + "sum(getklaim(pol_type_id = 57,claim_amount)) as claim57,"
                    + "sum(getklaim(pol_type_id = 58,claim_amount)) as claim58,"
                    + "sum(getklaim(pol_type_id = 73,claim_amount)) as claim73,"
                    + "sum(getklaim(pol_type_id = 74,claim_amount)) as claim74,"
                    + "sum(getklaim(pol_type_id = 60,claim_amount)) as claim60,"
                    + "sum(getklaim(pol_type_id = 3,claim_amount)) as claim3,"
                    + "sum(claim_amount) as total from ( " + sqa.getSQL()
                    + " order by 1,2,3 ) a group by 1,2 order by 1,2 ";
        }

        if (PERCABANG) {
            sql = "select a.cc_code,b.description as cabang,"
                    + "sum(getklaim(bulan = '01',claim_amount)) as bulan01,"
                    + "sum(getklaim(bulan = '02',claim_amount)) as bulan02,"
                    + "sum(getklaim(bulan = '03',claim_amount)) as bulan03,"
                    + "sum(getklaim(bulan = '04',claim_amount)) as bulan04,"
                    + "sum(getklaim(bulan = '05',claim_amount)) as bulan05,"
                    + "sum(getklaim(bulan = '06',claim_amount)) as bulan06,"
                    + "sum(getklaim(bulan = '07',claim_amount)) as bulan07,"
                    + "sum(getklaim(bulan = '08',claim_amount)) as bulan08,"
                    + "sum(getklaim(bulan = '09',claim_amount)) as bulan09,"
                    + "sum(getklaim(bulan = '10',claim_amount)) as bulan10,"
                    + "sum(getklaim(bulan = '11',claim_amount)) as bulan11,"
                    + "sum(getklaim(bulan = '12',claim_amount)) as bulan12, "
                    + "sum(claim_amount) as total from ( " + sqa.getSQL()
                    + " order by 1,2,3 ) a inner join gl_cost_center b on b.cc_code = a.cc_code "
                    + "group by 1,2 order by 1,2 ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_SUBROGASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowHeader = sheet.createRow(0);
            rowHeader.createCell(0).setCellValue("PERIODE : " + DateUtil.getDateStr(appDateFrom, "dd-MM-yyyy") + " - " + DateUtil.getDateStr(appDateTo, "dd-MM-yyyy"));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("CABANG");
            row0.createCell(1).setCellValue("PRINCIPAL");
            row0.createCell(2).setCellValue("COB");
            row0.createCell(3).setCellValue("JW.MULAI");
            row0.createCell(4).setCellValue("JW.AKHIR");
            row0.createCell(5).setCellValue("N.JAMINAN");
            row0.createCell(6).setCellValue("PLA");
            row0.createCell(7).setCellValue("DLA");
            row0.createCell(8).setCellValue("STATUS");
            row0.createCell(9).setCellValue("DOL");
            row0.createCell(10).setCellValue("TGLSETUJUI");
            row0.createCell(11).setCellValue("TGLBAYAR");
            row0.createCell(12).setCellValue("AMOUNT");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("principal_name"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("jenas"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("status"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            if (h.getFieldValueByFieldNameDT("payment_date") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("payment_date"));
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_KLAIM_SUBROGASI_JENIS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowHeader = sheet.createRow(0);
            rowHeader.createCell(0).setCellValue("PERIODE : " + DateUtil.getDateStr(appDateFrom, "dd-MM-yyyy") + " - " + DateUtil.getDateStr(appDateTo, "dd-MM-yyyy"));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("TAHUN");
            row0.createCell(1).setCellValue("BULAN");
            row0.createCell(2).setCellValue("KREASI");
            row0.createCell(3).setCellValue("CREDIT");
            row0.createCell(4).setCellValue("SB-JPSP");
            row0.createCell(5).setCellValue("SB-JSB");
            row0.createCell(6).setCellValue("SB-PW");
            row0.createCell(7).setCellValue("SB-PL");
            row0.createCell(8).setCellValue("SB-PUM");
            row0.createCell(9).setCellValue("SB-PM");
            row0.createCell(10).setCellValue("KBG-JPSP");
            row0.createCell(11).setCellValue("KBG-JKBG");
            row0.createCell(12).setCellValue("KBG-PW");
            row0.createCell(13).setCellValue("KBG-PL");
            row0.createCell(14).setCellValue("KBG-PUM");
            row0.createCell(15).setCellValue("KBG-PM");
            row0.createCell(16).setCellValue("MODAL KERJA");
            row0.createCell(17).setCellValue("INVESTASI");
            row0.createCell(18).setCellValue("KONSTRUKSI");
            row0.createCell(19).setCellValue("VEHICLE");
            row0.createCell(20).setCellValue("TOTAL");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("tahun"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("bulan"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("claim21").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("claim59").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("claim45").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("claim46").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("claim51").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("claim52").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("claim53").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("claim54").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim47").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim48").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim55").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim56").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("claim57").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("claim58").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("claim73").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim74").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim60").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim3").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("total").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_KLAIM_SUBROGASI_CABANG() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow rowHeader = sheet.createRow(0);
            rowHeader.createCell(0).setCellValue("PERIODE : " + DateUtil.getDateStr(appDateFrom, "dd-MM-yyyy") + " - " + DateUtil.getDateStr(appDateTo, "dd-MM-yyyy"));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("KODA");
            row0.createCell(1).setCellValue("CABANG");
            row0.createCell(2).setCellValue("JAN");
            row0.createCell(3).setCellValue("FEB");
            row0.createCell(4).setCellValue("MAR");
            row0.createCell(5).setCellValue("APR");
            row0.createCell(6).setCellValue("MEI");
            row0.createCell(7).setCellValue("JUN");
            row0.createCell(8).setCellValue("JUL");
            row0.createCell(9).setCellValue("AUG");
            row0.createCell(10).setCellValue("SEP");
            row0.createCell(11).setCellValue("OKT");
            row0.createCell(12).setCellValue("NOV");
            row0.createCell(13).setCellValue("DES");
            row0.createCell(14).setCellValue("TOTAL");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cabang"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("bulan01").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("bulan02").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("bulan03").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("bulan04").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("bulan05").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("bulan06").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("bulan07").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("bulan08").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("bulan09").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("bulan10").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("bulan11").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("bulan12").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("total").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_KLAIM_BPPDAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_id,(case when a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' then 'DLA' else 'PLA' end) as status,"
                + "a.pla_no,a.dla_no,a.pol_no,a.policy_date,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "substr(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))::text,1,4) as uy,"
                + "coalesce(a.share_pct,100) as share_askrida,a.cust_name as tertanggung,a.cust_address as alamat,"
                + "(case when a.pol_type_id in (1,81) then b.ref5 else b.ref8 end) as alamat_objek,b.order_no,coalesce((b.insured_amount),0) as tsi,"
                + "coalesce((select sum(x.amount) from ins_pol_items x where x.ins_item_id in (46,51) and a.pol_id = x.pol_id),0) as klaimbruto,"
                + "coalesce((select sum(x.amount) from ins_pol_items x where x.ins_item_id in (47,60,65) and a.pol_id = x.pol_id),0) as deductible,"
                + "coalesce((select sum(x.amount) from ins_pol_items x where x.ins_item_id in (50,109,110) and a.pol_id = x.pol_id),0) as adjuster,"
                + "coalesce((select sum(x.amount) from ins_pol_items x where x.ins_item_id in (53) and a.pol_id = x.pol_id),0) as salvage,"
                + "coalesce((a.claim_amount),0) as klaim_nett,coalesce((a.claim_amount_est),0) as klaim_estimasi,k.mapping_ojk as penyebab,"
                + "TO_CHAR(a.pla_date, 'DD-MM-YYYY') as tgl_lks,TO_CHAR(a.claim_approved_date, 'DD-MM-YYYY') as tgl_setujui,TO_CHAR(a.claim_date, 'DD-MM-YYYY') as tgl_klaim,TO_CHAR(a.claim_propose_date, 'DD-MM-YYYY') as tgl_pengajuan,TO_CHAR(a.confirm_date, 'DD-MM-YYYY') as tgl_confirm,"
                + "a.ccy as kurs,a.ccy_rate as rate,a.claim_currency as kurs_claim,a.ccy_rate_claim as rate_claim,"
                + "round(sum(checkreas(j.treaty_type='BPDAN',h.tsi_pct)),2) as rate_bppdan,"
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.claim_amount)),2) as claim_bppdan,a.pol_type_id,"
                + "(case when a.pol_type_id = 1 then 'PSAKI' when a.pol_type_id = 81 then 'IAR' else 'PSAGBI' end) as cob ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.pol_type_id in (1,81,19)");
        sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y') or ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                + "or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))) ");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (PLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.pla_date) >= ?");
            sqa.addPar(PLADateFrom);
        }

        if (PLADateTo != null) {
            sqa.addClause("date_trunc('day',a.pla_date) <= ?");
            sqa.addPar(PLADateTo);
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

        String sqlpla = "select * from ( " + sqa.getSQL() + " group by a.pla_no,a.dla_no,a.pol_no,a.pol_type_id,b.order_no,b.refd1,b.refd2,b.refd3,b.ref5,b.ref8,"
                + "a.period_start,a.period_end,a.cust_name,a.cust_address,b.insured_amount,a.ccy,a.ccy_rate,a.claim_currency,a.ccy_rate_claim,a.claim_amount,"
                + "a.pol_id,k.mapping_ojk order by a.pla_date,a.pol_no,a.dla_no  ) a where status = 'PLA' and claim_bppdan <> 0 order by a.tgl_lks,a.pol_no,a.dla_no ";

        String sqldla = "select * from ( " + sqa.getSQL() + " group by a.pla_no,a.dla_no,a.pol_no,a.pol_type_id,b.order_no,b.refd1,b.refd2,b.refd3,b.ref5,b.ref8,"
                + "a.period_start,a.period_end,a.cust_name,a.cust_address,b.insured_amount,a.ccy,a.ccy_rate,a.claim_currency,a.ccy_rate_claim,a.claim_amount,"
                + "a.pol_id,k.mapping_ojk order by a.pla_date,a.pol_no,a.dla_no  ) a where status = 'DLA' and claim_bppdan <> 0 order by a.tgl_lks,a.pol_no,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sqlpla,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sqldla,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;
    }

    public void EXPORT_KLAIM_BPPDAN() throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("PLA");
        XSSFSheet sheet2 = wb.createSheet("DLA");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("No PLA");
            row0.createCell(1).setCellValue("No Policy");
            row0.createCell(2).setCellValue("Underwriting Year");
            row0.createCell(3).setCellValue("Risk No");
            row0.createCell(4).setCellValue("Claim No");
            row0.createCell(5).setCellValue("Insured Name");
            row0.createCell(6).setCellValue("Lokasi Pertanggungan/Alamat Pertanggungan");
            row0.createCell(7).setCellValue("TSI Currency");
            row0.createCell(8).setCellValue("Currency Claim");
            row0.createCell(9).setCellValue("Claim 100% (ORI)");
            row0.createCell(10).setCellValue("PLA Share BPPDAN");
            row0.createCell(11).setCellValue("Document Date");
            row0.createCell(12).setCellValue("Confirmation Date");
            row0.createCell(13).setCellValue("Claim Date");
            row0.createCell(14).setCellValue("Date of Loss");
            row0.createCell(15).setCellValue("Cause of Loss");
            row0.createCell(16).setCellValue("Policy Type");
            row0.createCell(17).setCellValue("Remark by Ceding");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("alamat_objek"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("kurs_claim"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("rate_claim").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("klaim_estimasi").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("tgl_lks"));
            if (h.getFieldValueByFieldNameST("tgl_confirm") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("tgl_confirm"));
            }
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("tgl_pengajuan"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("tgl_klaim"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("penyebab"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("cob"));

        }

        for (int i = 0; i < list2.size(); i++) {
            HashDTO h = (HashDTO) list2.get(i);

            //bikin header
            XSSFRow row0 = sheet2.createRow(0);
            row0.createCell(0).setCellValue("No PLA");
            row0.createCell(1).setCellValue("No Policy");
            row0.createCell(2).setCellValue("Underwriting Year");
            row0.createCell(3).setCellValue("Risk No");
            row0.createCell(4).setCellValue("Claim No");
            row0.createCell(5).setCellValue("Claim 100% (ORI)");
            row0.createCell(6).setCellValue("Deductible");
            row0.createCell(7).setCellValue("Adjuster");
            row0.createCell(8).setCellValue("Salvage");
            row0.createCell(9).setCellValue("Total Outstanding");
            row0.createCell(10).setCellValue("Claim BPPDAN Share");
            row0.createCell(11).setCellValue("Remark by Ceding");

            BigDecimal Klaim = BDUtil.add(h.getFieldValueByFieldNameBD("klaimbruto"), h.getFieldValueByFieldNameBD("deductible"));
            Klaim = BDUtil.add(Klaim, h.getFieldValueByFieldNameBD("adjuster"));
            Klaim = BDUtil.add(Klaim, h.getFieldValueByFieldNameBD("salvage"));

            BigDecimal osKlaim = BDUtil.sub(Klaim, h.getFieldValueByFieldNameBD("klaim_estimasi"));

            //bikin isi cell
            XSSFRow row = sheet2.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("order_no").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("deductible").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("adjuster").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("salvage").doubleValue());
            row.createCell(9).setCellValue(osKlaim.doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_KLAIM_BPPDANKSCBI() throws Exception {
        final boolean BPPDAN = "Y".equalsIgnoreCase((String) refPropMap.get("BPPDAN"));
        final boolean KSCBI = "Y".equalsIgnoreCase((String) refPropMap.get("KSCBI"));
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,a.policy_date,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "substr(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))::text,1,4) as uy,"
                + "coalesce(a.share_pct,100) as share_askrida,"
                + "a.cust_name as tertanggung,a.cust_address as alamat,"
                + "coalesce((a.insured_amount*a.ccy_rate),0) as tsi100,"
                + "coalesce((select sum(x.amount*a.ccy_rate_claim) from ins_pol_items x "
                + "where x.ins_item_id in (46) and a.pol_id = x.pol_id),0) as klaimbruto,"
                + "coalesce((select sum(x.amount*a.ccy_rate_claim) from ins_pol_items x "
                + "where x.ins_item_id in (47) and a.pol_id = x.pol_id),0) as deductible,"
                + "coalesce((select sum(x.amount*a.ccy_rate_claim) from ins_pol_items x "
                + "where x.ins_item_id in (50) and a.pol_id = x.pol_id),0) as adjuster,"
                + "coalesce((select sum(x.amount*a.ccy_rate_claim) from ins_pol_items x "
                + "where x.ins_item_id in (73) and a.pol_id = x.pol_id),0) as recovery,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as klaim_nett,"
                + "coalesce((a.claim_amount_est*a.ccy_rate_claim),0) as klaim_estimasi,"
                + "a.claim_chronology as kronologi,k.cause_desc as penyebab,"
                + "a.pla_date as tgl_lks,a.claim_approved_date as tgl_setujui,a.claim_date as tgl_klaim,"
                + "a.claim_propose_date as tgl_pengajuan,a.ccy as kurs,a.ccy_rate as rate,a.claim_currency as kurs_claim,a.ccy_rate_claim as rate_claim,"
                + "round(sum(checkreas(j.treaty_type='BPDAN',h.tsi_pct)),2) as rate_bppdan,"
                + "round(sum(checkreas(j.treaty_type='BPDAN',i.claim_amount*a.ccy_rate_claim)),2) as claim_bppdan,"
                + "a.pol_type_id,c.description as cob,"
                + "round(sum(checkreas(j.treaty_type='KSCBI',i.claim_amount*a.ccy_rate_claim)),2) as claim_kscbi, "
                + "a.ref5 as principal,"
                + "(select string_agg(g.receipt_date::text, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as receipt_date ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "left join ins_policy_types c on c.pol_type_id = a.pol_type_id ");

        sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        String sql = "select * from ( " + sqa.getSQL() + " group by a.pla_no,a.dla_no,a.pol_no,a.pol_type_id,b.refd1,b.refd2,b.refd3,"
                + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.insured_amount,a.ccy,a.ccy_rate,a.claim_currency,a.ccy_rate_claim,"
                + "a.claim_amount,a.pol_id,k.cause_desc,c.description order by a.pla_date,a.pol_no,a.dla_no "
                + " ) a ";

        if (BPPDAN) {
            sql = sql + " where claim_bppdan <> 0 order by a.tgl_lks,a.pol_no,a.dla_no";
        }
        if (KSCBI) {
            sql = sql + " where claim_kscbi <> 0 order by a.tgl_lks,a.pol_no,a.dla_no";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KLAIM_BPPDANOLD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("SLA_REF_PLA");
            row0.createCell(1).setCellValue("SLA_BLK_RISK_CODE");
            row0.createCell(2).setCellValue("SLA_NUMBER_RISK_CODE");
            row0.createCell(3).setCellValue("SLA_CCLM_NO");
            row0.createCell(4).setCellValue("SLA_POL_NO");
            row0.createCell(5).setCellValue("SLA_PERIODE_AWAL");
            row0.createCell(6).setCellValue("SLA_PERIODE_AKHIR");
            row0.createCell(7).setCellValue("SLA_UW");
            row0.createCell(8).setCellValue("SLA_SHARE");
            row0.createCell(9).setCellValue("SLA_INSURED");
            row0.createCell(10).setCellValue("SLA_ADDRESS");
            row0.createCell(11).setCellValue("SLA_TSI_CURR");
            row0.createCell(12).setCellValue("SLA_TOT_SI");
            row0.createCell(13).setCellValue("SLA_CURRENCY");
            row0.createCell(14).setCellValue("SLA_CLAIM_AMT");
            row0.createCell(15).setCellValue("SLA_DEDUCTIBLE");
            row0.createCell(16).setCellValue("SLA_ADJUSTER");
            row0.createCell(17).setCellValue("SLA_RECOVERY");
            row0.createCell(18).setCellValue("SLA_TOT_AMT");
            row0.createCell(19).setCellValue("SLA_OS_AMT");
            row0.createCell(20).setCellValue("SLA_DESC");
            row0.createCell(21).setCellValue("SLA_CAUSE_OF_LOSS");
            row0.createCell(22).setCellValue("SLA_DOC_DATE");
            row0.createCell(23).setCellValue("SLA_RATE");
            row0.createCell(24).setCellValue("SLA_SHARE BPPDAN");
            row0.createCell(25).setCellValue("SLA_CLAIM_DATE");
            row0.createCell(26).setCellValue("SLA_LOSS_DATE");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("share_askrida").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("alamat"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("kurs"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("tsi100").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("kurs_claim"));
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("deductible").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("adjuster").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("recovery").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("klaim_nett").doubleValue());
            row.createCell(19).setCellValue("");
            if (h.getFieldValueByFieldNameST("kronologi") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            }
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameST("penyebab"));
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameDT("tgl_lks"));
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("rate_claim").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            if (h.getFieldValueByFieldNameDT("tgl_pengajuan") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameDT("tgl_pengajuan"));
            }
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameDT("tgl_klaim"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXPORT_KLAIM_KSCBI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_type_id");
            row0.createCell(1).setCellValue("cob");
            row0.createCell(2).setCellValue("pol_no");
            row0.createCell(3).setCellValue("tgl_polis");
            row0.createCell(4).setCellValue("pla_no");
            row0.createCell(5).setCellValue("dla_no");
            row0.createCell(6).setCellValue("principal");
            row0.createCell(7).setCellValue("obligee");
            row0.createCell(8).setCellValue("tgl_bayar");
            row0.createCell(9).setCellValue("tgl_klaim");
            row0.createCell(10).setCellValue("klaimbruto");
            row0.createCell(11).setCellValue("claim_kscbi");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_type_id").doubleValue());
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cob")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("principal"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            if (h.getFieldValueByFieldNameDT("tgl_bayar") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tgl_bayar"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameDT("tgl_klaim"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_POTENSI_SUBROGASI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pla_no,a.dla_no,quote_ident(a.pol_no) as pol_no,a.policy_date,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "a.cust_name as tertanggung,"
                + "a.cust_address as alamat,"
                + "coalesce((a.insured_amount*a.ccy_rate),0) as tsi,"
                + "coalesce((select sum(x.amount*a.ccy_rate_claim) from ins_pol_items x "
                + "where x.ins_item_id in (46) and a.pol_id = x.pol_id),0) as KlaimBruto,"
                + "a.claim_chronology as kronologi,k.cause_desc as penyebab ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause  ");

        sqa.addClause(" a.status IN ('CLAIM') ");
        sqa.addClause(" a.claim_status IN ('DLA')");
        sqa.addClause(" a.active_flag='Y'");
        sqa.addClause(" a.effective_flag='Y'");
        sqa.addClause(" a.ref13 = 'Y'");

        String batal = " select a.pla_no from ins_policy a "
                + "where a.status in ('CLAIM ENDORSE') and a.ref13 = 'I' "
                + "and a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ";

        if (appDateFrom != null) {
            batal = batal + " and date_trunc('day',a.approved_date) >= '" + appDateFrom + "'";
        }

        if (appDateTo != null) {
            batal = batal + " and date_trunc('day',a.approved_date) <= '" + appDateTo + "'";
        }

        sqa.addClause("a.pla_no not in (" + batal + ")");

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
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

        String sql = sqa.getSQL() + " order by a.pla_date,a.pol_no,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_POTENSI_SUBROGASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pla_no");
            row0.createCell(1).setCellValue("dla_no");
            row0.createCell(2).setCellValue("pol_no");
            row0.createCell(3).setCellValue("policy_date");
            row0.createCell(4).setCellValue("period_start");
            row0.createCell(5).setCellValue("period_end");
            row0.createCell(6).setCellValue("tertanggung");
            row0.createCell(7).setCellValue("alamat");
            row0.createCell(8).setCellValue("tsi");
            row0.createCell(9).setCellValue("klaimbruto");
            row0.createCell(10).setCellValue("kronologi");
            row0.createCell(11).setCellValue("penyebab");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("alamat"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("tsi").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("klaimbruto").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            row.createCell(11).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("penyebab")));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXCEL_KLAIM_SPESIFIK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" c.description as cabang,a.pol_id,quote_ident(a.pol_no) as pol_no,a.pla_no,a.dla_no,a.dla_date,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,"
                + "a.claim_sub_cause as kode_penyebab_spesifik,d.cause_desc as penyebab_spesifik,a.claim_sub_cause_other as keterangan_lainnya,"
                + "e.vs_description as pekerjaan,a.ref15 as keterangan_lainnya,a.ref16 as instansi_bekerja ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join gl_cost_center c on c.cc_code = a.cc_code "
                + "left join ins_clm_sub_cause d on d.ins_clm_sub_cause_id = a.claim_sub_cause "
                + "left join s_valueset e on e.vs_code = a.ref14 and e.vs_group = 'INSOBJ_CREDIT_PEKERJAAN_CLAIM' ");

        sqa.addClause("a.status in('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.active_flag = 'Y' ");
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

        String sql = sqa.getSQL();

        sql = sql + " order by a.cc_code,a.dla_date ";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_KLAIM_SPESIFIK_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCEL_RKP_INWARD() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.create_date,coalesce(a.claim_approved_date,a.approved_date) as claim_approved_date,substr(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))::text,1,4) as uy,"
                + "p.treaty_grp_id,e.ent_name as ent_name,f.ent_name as prod_name,coalesce(a.tsi_100,0) as tsi_100,coalesce(a.claim_100,0) as claim_100,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd2,getperiod(a.pol_type_id in (1,3,5,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80),b.refd3,getperiod(a.pol_type_id in (1,3,5,81),b.refd2,a.period_end))) as period_end,"
                + "a.ins_policy_type_grp_id::character varying,a.pol_type_id::character varying,a.pol_id::character varying,a.claim_date,a.status,a.claim_cause::text,k.cause_desc,l.short_desc, "
                + "coalesce(a.dla_date,a.pla_date) as dla_date,a.claim_propose_date as propose_date,a.policy_date,a.claim_status,a.pol_no,a.pla_no,a.dla_no,a.cust_name,"
                + "case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end as name,"
                + "coalesce((b.insured_amount*a.ccy_rate_claim),0) as insured_amount,coalesce((a.claim_amount_est*a.ccy_rate_claim),0) as claim_amount_est,"
                + "coalesce((a.claim_amount*a.ccy_rate_claim),0) as claim_amount,coalesce((a.claim_amount_approved*a.ccy_rate_claim),0) as claim_amount_approved,"
                + "coalesce(checkreas(a.pol_type_id=21,b.refn2*a.ccy_rate),0) as premiko,a.claim_currency,a.ccy_rate_claim,"
                + "a.claim_client_name,a.claim_account_no,a.entity_id::text,a.prod_id::text,(getperiod(a.pol_type_id in (4,21,59,73,74,80),b.refd1,null)) as tgl_lahir,"
                + "n.user_name as createwho,o.user_name as approvedwho,m.ent_name as marketingofficer, "
                + "d.loss_desc as status_kerugian,a.claim_chronology as kronologi,"
                + "checkstatus(a.pol_type_id in (1,2,19,81,83),(select string_agg(y.ins_risk_cat_code,'| ') from ( "
                + "select y.ins_risk_cat_code from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.ins_risk_cat_code ) y ), "
                + "(select string_agg(y.description,'| ') from ( "
                + "select y.description from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.description ) y )) as risk_code, "
                + "(select sum(l.claim_amt) "
                + "from ins_pol_coins l "
                + "where l.policy_id = a.pol_id and l.position_code = 'MEM' group by a.pol_id) as coins_amount,"
                + "(select coalesce(e.short_name,'') from ent_master e where a.coinsurer_id::bigint = e.ent_id) as coins_name,"
                + "round(sum(checkreas(j.treaty_type='OR',i.claim_amount*a.ccy_rate_claim)),2) as claim_or,  "
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
                + "(select string_agg(g.receipt_date::text, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as payment_date,"
                + "(select string_agg(g.receipt_no, '| ') "
                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as payment_note, "
                + "c.vs_description as jengrup ");

        sqa.addQuery(" from ins_policy a "
                + "   left join ent_master e on e.ent_id = a.entity_id"
                + "   left join ent_master f on f.ent_id = a.prod_id"
                + "   inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "   inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "   inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "   inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "   inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                + "   left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "   left join ins_policy_types l on l.pol_type_id = a.pol_type_id "
                + "   left join ins_claim_loss d on d.claim_loss_id = b.claim_loss_id "
                + "   left join ent_master m on m.ent_id::text = a.marketing_officer_who "
                + "   left join s_users n on n.user_id = a.create_who "
                + "   left join s_users o on o.user_id = a.approved_who "
                + "   left join s_valueset c on c.vs_code = f.ref1 and c.vs_group = 'COMP_TYPE' "
                + "   inner join ins_treaty p on p.ins_treaty_id = g.ins_treaty_id ");

        sqa.addClause(" a.status in ('CLAIM INWARD','ENDORSE CLAIM INWARD') ");

        if (EFF_CLAIM) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (ACT_CLAIM) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (claimDateFrom != null) {
            sqa.addClause("date_trunc('day',a.claim_date) >= ?");
            sqa.addPar(claimDateFrom);
        }

        if (claimDateTo != null) {
            sqa.addClause("date_trunc('day',a.claim_date) <= ?");
            sqa.addPar(claimDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
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
                sqa.addClause("a.pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (stBussinessPolTypeCob.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97,87,88)");
            } else {
                sqa.addClause("a.pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        if (stCompanyID != null) {
            sqa.addClause("e.ref2 = ?");
            sqa.addPar(stCompanyID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = ?");
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

        if (stCompTypeID != null) {
            String type = null;
            if (stCompTypeID.equalsIgnoreCase("93") || stCompTypeID.equalsIgnoreCase("98")) {
                type = "('93','98')";
            } else if (stCompTypeID.equalsIgnoreCase("89") || stCompTypeID.equalsIgnoreCase("97")) {
                type = "('89','97')";
            } else {
                type = "('" + stCompTypeID + "')";
            }
            sqa.addClause("e.ref1 in " + type);
//            sqa.addPar(type);
        }

        if (stMarketerOffID != null) {
            //sqa.addClause("a.marketing_officer_who = '" + stMarketerOffID + "'");
            sqa.addClause("a.marketing_officer_who = ?");
            sqa.addPar(stMarketerOffID);
        }

        if (stCompanyProdID != null) {
//            sqa.addClause("e.ref2 = '" + stCompanyProdID + "'");
            sqa.addClause("f.ref2 = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stObject != null) {
            if (stPolicyTypeGroupID.equalsIgnoreCase("10")) {
                sqa.addClause("upper(b.ref2) like ?");
            } else {
                sqa.addClause("upper(b.ref1) like ?");
            }
            sqa.addPar('%' + stObject.toUpperCase() + '%');
        }

        final String sql = sqa.getSQL() + "  group by a.status,a.claim_status,a.coinsurer_id,a.period_start,a.period_end,a.pol_type_id,a.pol_id,a.pla_date,a.dla_date,a.claim_propose_date,a.policy_date,a.create_date,a.claim_approved_date,d.loss_desc,a.claim_chronology,"
                + " a.pol_no,a.pla_no,a.dla_no,a.cust_name,a.ins_policy_type_grp_id,b.ref2,b.ref1,a.claim_cause,b.refd1,b.refd2,b.refd3,b.ref3d,k.cause_desc,l.short_desc,a.claim_client_name,a.claim_account_no,c.vs_description,a.tsi_100,a.claim_100, "
                + " b.insured_amount,a.ccy_rate,a.claim_currency,a.ccy_rate_claim,a.claim_amount_est,a.claim_amount,a.claim_amount_approved,a.claim_date,b.refn2,a.pol_type_id,f.ent_name,e.ent_name,a.entity_id,a.prod_id,m.ent_name,n.user_name,o.user_name,p.treaty_grp_id  "
                + " order by a.pla_date,a.pol_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_RKP_INWARD() throws Exception {
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
            row0.createCell(1).setCellValue("uy reins");
            row0.createCell(2).setCellValue("period_start");
            row0.createCell(3).setCellValue("period_end");
            row0.createCell(4).setCellValue("pol_id");
            row0.createCell(5).setCellValue("dla_date");
            row0.createCell(6).setCellValue("claim_date");
            row0.createCell(7).setCellValue("policy_date");
            row0.createCell(8).setCellValue("approved_date");
            row0.createCell(9).setCellValue("pol_no");
            row0.createCell(10).setCellValue("pla_no");
            row0.createCell(11).setCellValue("dla_no");
            row0.createCell(12).setCellValue("cust_name");
            row0.createCell(13).setCellValue("name");
            row0.createCell(14).setCellValue("coins_name");
            row0.createCell(15).setCellValue("coins_amount");
            row0.createCell(16).setCellValue("insured_amount");
            row0.createCell(17).setCellValue("claim_amount");
            row0.createCell(18).setCellValue("claim_amount_est");
            row0.createCell(19).setCellValue("claim_amount_approved");
            row0.createCell(20).setCellValue("claim_or");
            row0.createCell(21).setCellValue("claim_bppdan");
            row0.createCell(22).setCellValue("claim_spl");
            row0.createCell(23).setCellValue("claim_fac");
            row0.createCell(24).setCellValue("claim_qs");
            row0.createCell(25).setCellValue("claim_park");
            row0.createCell(26).setCellValue("claim_faco");
            row0.createCell(27).setCellValue("claim_faco1");
            row0.createCell(28).setCellValue("claim_faco2");
            row0.createCell(29).setCellValue("claim_faco3");
            row0.createCell(30).setCellValue("claim_jp");
            row0.createCell(31).setCellValue("claim_qskr");
            row0.createCell(32).setCellValue("claim_kscbi");
            row0.createCell(33).setCellValue("Premiko");
            row0.createCell(34).setCellValue("COB");
            row0.createCell(35).setCellValue("Penyebab Klaim");
            row0.createCell(36).setCellValue("Tgl Setujui");
            row0.createCell(37).setCellValue("No Bukti");
            row0.createCell(38).setCellValue("Tgl Bayar");
            row0.createCell(39).setCellValue("Marketer");
            row0.createCell(40).setCellValue("Nama Nasabah");
            row0.createCell(41).setCellValue("No Rekening");
            row0.createCell(42).setCellValue("Entity ID");
            row0.createCell(43).setCellValue("Risk Code");
            row0.createCell(44).setCellValue("Status Kerugian");
            row0.createCell(45).setCellValue("Kurs Klaim");
            row0.createCell(46).setCellValue("Kurs");
            row0.createCell(47).setCellValue("Marketer ID");
            row0.createCell(48).setCellValue("Kronologi");
            row0.createCell(49).setCellValue("Tgl Lahir");
            row0.createCell(50).setCellValue("Tgl Entry");
            row0.createCell(51).setCellValue("Create Who");
            row0.createCell(52).setCellValue("Approved Who");
            row0.createCell(53).setCellValue("Marketing Officer");
            row0.createCell(54).setCellValue("Tanggal Pengajuan");
            row0.createCell(55).setCellValue("Jenis Marketer");
            row0.createCell(56).setCellValue("Sumbis");
            row0.createCell(57).setCellValue("Tsi100");
            row0.createCell(58).setCellValue("Claim100");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("uy"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("treaty_grp_id"));
            if (h.getFieldValueByFieldNameDT("period_start") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            }
            if (h.getFieldValueByFieldNameDT("period_end") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
            }
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_id"));
            if (h.getFieldValueByFieldNameDT("dla_date") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("dla_date"));
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("name"));
            if (h.getFieldValueByFieldNameST("coins_name") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("coins_name"));
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("coins_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("insured_amount") != null) {
                row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("insured_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("claim_amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_est") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_est").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_amount_approved") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_or") != null) {
                row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("claim_or").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_bppdan") != null) {
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("claim_bppdan").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_spl") != null) {
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("claim_spl").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_fac") != null) {
                row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("claim_fac").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qs") != null) {
                row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("claim_qs").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_park") != null) {
                row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("claim_park").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco") != null) {
                row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("claim_faco").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco1") != null) {
                row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("claim_faco1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco2") != null) {
                row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("claim_faco2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_faco3") != null) {
                row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("claim_faco3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_jp") != null) {
                row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("claim_jp").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_qskr") != null) {
                row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("claim_qskr").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("claim_kscbi") != null) {
                row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("claim_kscbi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("coins_amount") != null) {
                row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("premiko").doubleValue());
            }
            /*
            if (getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus()==null) continue;
            row.createCell(23).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getParentPolicy().getParentPolicy().getStStatus());
             */
            if (h.getFieldValueByFieldNameST("pol_id") != null) {
                //row.createCell(31).setCellValue(getPolicy(h.getFieldValueByFieldNameST("pol_id")).getStPolicyTypeDesc());
                row.createCell(34).setCellValue(h.getFieldValueByFieldNameST("short_desc"));
            }
            if (h.getFieldValueByFieldNameST("claim_cause") != null) {
                //row.createCell(32).setCellValue(LanguageManager.getInstance().translate(getClaimCause(h.getFieldValueByFieldNameST("claim_cause")).getStDescription()));
                row.createCell(35).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("cause_desc")));
            }
            if (ACT_CLAIM) {
                if (h.getFieldValueByFieldNameST("claim_status").equalsIgnoreCase("PLA")) {
                    if (h.getFieldValueByFieldNameDT("approved_date") != null) {
                        row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("approved_date"));
                    }
                }
            } else if (EFF_CLAIM) {
                if (h.getFieldValueByFieldNameDT("claim_approved_date") != null) {
                    row.createCell(36).setCellValue(h.getFieldValueByFieldNameDT("claim_approved_date"));
                }
            }
            if (h.getFieldValueByFieldNameST("payment_note") != null) {
                row.createCell(37).setCellValue(h.getFieldValueByFieldNameST("payment_note"));
            }
            if (h.getFieldValueByFieldNameST("payment_date") != null) {
                row.createCell(38).setCellValue(h.getFieldValueByFieldNameST("payment_date"));
            }
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameST("prod_name"));
            if (h.getFieldValueByFieldNameST("ins_policy_type_grp_id").equalsIgnoreCase("10")) {
                if (h.getFieldValueByFieldNameST("claim_client_name") != null) {
                    row.createCell(40).setCellValue(h.getFieldValueByFieldNameST("claim_client_name"));
                }
                if (h.getFieldValueByFieldNameST("claim_account_no") != null) {
                    row.createCell(41).setCellValue(h.getFieldValueByFieldNameST("claim_account_no"));
                }
            }
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameST("entity_id"));
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameST("risk_code"));
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameST("status_kerugian"));
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameST("claim_currency"));
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate_claim").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameST("prod_id"));
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
            if (h.getFieldValueByFieldNameDT("tgl_lahir") != null) {
                row.createCell(49).setCellValue(h.getFieldValueByFieldNameDT("tgl_lahir"));
            }
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameDT("create_date"));
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameST("createwho"));
            if (h.getFieldValueByFieldNameST("approvedwho") != null) {
                row.createCell(52).setCellValue(h.getFieldValueByFieldNameST("approvedwho"));
            }
            if (h.getFieldValueByFieldNameST("marketingofficer") != null) {
                row.createCell(53).setCellValue(h.getFieldValueByFieldNameST("marketingofficer"));
            }
            if (h.getFieldValueByFieldNameDT("propose_date") != null) {
                row.createCell(54).setCellValue(h.getFieldValueByFieldNameDT("propose_date"));
            }
            if (h.getFieldValueByFieldNameST("jengrup") != null) {
                row.createCell(55).setCellValue(h.getFieldValueByFieldNameST("jengrup"));
            }
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("tsi_100").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("claim_100").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_KLAIM_SUBROGASI_OS() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("distinct a.pol_no,a.pla_no,a.dla_no,a.create_date as tglbuat,a.claim_propose_date as tglajuan,"
                + "a.cust_name,(case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end) as nama,"
                + "coalesce((select sum(amount) from ins_pol_items x where x.pol_id = a.pol_id and x.ins_item_id = 48),0) as subrogasi,"
                + "coalesce((select sum(amount) from ins_pol_items x where x.pol_id = a.pol_id and x.ins_item_id = 73),0) as recovery,"
                + "k.cause_desc as penyebab,d.loss_desc as kerugian,a.claim_chronology as kronologi ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.ins_item_id in (48,73) "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "left join ins_claim_loss d on d.claim_loss_id = b.claim_loss_id ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        if (ACT_CLAIM) {
            sqa.addClause("a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        } else {
            sqa.addClause("((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + "or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");
        }

        if (entryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ? ");
            sqa.addPar(entryDateFrom);
        }

        if (entryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ? ");
            sqa.addPar(entryDateTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
        }

        String sql = sqa.getSQL() + " order by a.pol_no,a.pla_no,a.dla_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

        /*SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_CLAIM_SUBROGASI_" + System.currentTimeMillis() + ".csv";

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

        file.delete();*/
    }

    public void EXPORT_KLAIM_SUBROGASI_OS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("pol_no");
            row0.createCell(1).setCellValue("pla_no");
            row0.createCell(2).setCellValue("dla_no");
            row0.createCell(3).setCellValue("tgl_buat");
            row0.createCell(4).setCellValue("cust_name");
            row0.createCell(5).setCellValue("nama");
            row0.createCell(6).setCellValue("subrogasi");
            row0.createCell(7).setCellValue("recovery");
            row0.createCell(8).setCellValue("penyebab");
            row0.createCell(9).setCellValue("kerugian");
            row0.createCell(10).setCellValue("kronologi");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("pla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            if (h.getFieldValueByFieldNameDT("tglbuat") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tglbuat"));
            }
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("nama"));
            if (h.getFieldValueByFieldNameBD("subrogasi") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("subrogasi").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("recovery") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("recovery").doubleValue());
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("penyebab"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("kerugian"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("kronologi"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public void EXCEL_DETAILKLAIM() throws Exception {
        final boolean ACT_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("ACT_CLAIM"));
        final boolean EFF_CLAIM = "Y".equalsIgnoreCase((String) refPropMap.get("EFF_CLAIM"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80,87,88),b.refd2,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd1,a.period_start))) as period_start,"
                + "(getperiod(a.pol_type_id in (4,21,59,64,73,74,80,87,88),b.refd3,getperiod(a.pol_type_id in (1,3,5,24,81),b.refd2,a.period_end))) as period_end,"
                + "s.vs_description as kategori,c.description as cabang,n.description as cob,a.status,a.pol_type_id,a.claim_propose_date as propose_date,a.dla_date,a.claim_date,a.policy_date,"
                + "a.pol_id,quote_ident(pol_no) as pol_no,a.pla_no,a.dla_no,e.ent_name as sumbis,f.ent_name as pemasar,a.cust_name,a.ccy_rate_claim,a.claim_currency as kurs,"
                + "(case when a.ins_policy_type_grp_id = 10 then b.ref2 when a.pol_type_id = 60 then b.ref3d else b.ref1 end) as tertanggung,"
                + "(getperiod(a.pol_type_id in (4,21,59,73,74,80,87,88),b.refd1,null)) as tgl_lahir,';'||(case when a.pol_type_id in (59,87,88) then b.ref3 end) as nik_ktp,"
                + "';'||(case when a.pol_type_id in (59,73,74,87,88) then b.ref16 end) as no_rek, "
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
                + "a.approved_date,k.cause_desc as penyebab,d.vs_description as coverage,a.kreasi_type_desc as jenis_kredit,"
                + "(checkstatus(a.pol_type_id in (4,21,59,64,73,74),m.vs_description,checkstatus(a.pol_type_id in (80),l.vs_description,null))) as kriteria,"
                + "checkstatus(a.pol_type_id in (1,2,19,81,83),(select string_agg(y.ins_risk_cat_code,'| ') "
                + "from ( select y.ins_risk_cat_code from ins_pol_obj x inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id "
                + "where x.pol_id = a.pol_id group by y.ins_risk_cat_code ) y ), "
                + "(select string_agg(y.description,'| ') from ( select y.description from ins_pol_obj x "
                + "inner join ins_risk_cat y on y.ins_risk_cat_id = x.ins_risk_cat_id where x.pol_id = a.pol_id "
                + "group by y.description ) y )) as risk_code, "
                + "(checkstatus(a.pol_type_id in (1,2,6,7,9,10,11,12,16,17,19,23,61,62,65,66,67,68,69,70,72,81,82,83,84),b.risk_class,null)) as risk_class,"
                //                + "(select string_agg(g.receipt_date::text, '| ') "
                //                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as tgl_bayar,"
                //                + "(select string_agg(g.receipt_no, '| ') "
                //                + "from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12) as nobuk "
                + "(select g.receipt_date from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12 order by receipt_no asc limit 1) as tgl_bayar,"
                + "(select g.receipt_no from ar_invoice g where g.attr_pol_id = a.pol_id and g.ar_trx_type_id = 12 order by receipt_no asc limit 1) as nobuk,"
                + "';'||a.cc_code_source as cc_code_source,';'||p.region_code as region_code,o.ent_name as paymentname,a.refd1 as tglsubro, "
                + "a.claim_sub_cause as kode_penyebab_spesifik,r.cause_desc as penyebab_spesifik,a.claim_sub_cause_other as keterangan_lainnya,"
                + "q.vs_description as pekerjaan,a.ref15 as keterangan,a.ref16 as instansi_bekerja,a.claim_cash_call_f ");

        sqa.addQuery(" from ins_policy a "
                + "inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                + "inner join gl_cost_center c on c.cc_code = a.cc_code "
                + "left join s_valueset d on d.vs_code = checkstatus(a.pol_type_id in (59,73,74),b.ref13,b.ref10) and d.vs_group = 'INSOBJ_CREDIT_COVER_PACKAGE' "
                + "left join s_valueset l on l.vs_code = b.ref4 and l.vs_group = 'INSOBJ_PEKERJAAN_DEBITUR' "
                + "left join s_valueset m on m.vs_code = b.ref7 and m.vs_group = 'INSOBJ_CREDIT_STATUS' "
                + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                + "left join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "left join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "left join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "left join ins_clm_cause k on k.ins_clm_caus_id = a.claim_cause "
                + "left join ent_master e on e.ent_id = a.entity_id "
                + "left join ent_master f on f.ent_id = a.prod_id "
                + "inner join ins_policy_types n on n.pol_type_id = a.pol_type_id "
                + "left join ent_master o on o.ent_id = a.payment_company_id::int "
                + "left join s_region p on p.region_id = a.region_id_source "
                + "left join s_valueset q on q.vs_code = a.ref14 and q.vs_group = 'INSOBJ_CREDIT_PEKERJAAN_CLAIM' "
                + "left join ins_clm_sub_cause r on r.ins_clm_sub_cause_id = a.claim_sub_cause "
                + "left join s_valueset s on s.vs_code = e.category1 and s.vs_group = 'ASK_BUS_SOURCE' ");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD')");

        if (Tools.isYes(stIndex)) {
            sqa.addClause(" a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause(" ((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                    + " or (a.claim_status = 'PLA' and a.active_flag = 'Y'))");
        }

        if (appDateFrom != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= '" + DLADateFrom + "'");
//            sqa.addPar(appDateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= '" + DLADateTo + "'");
//            sqa.addPar(appDateTo);
        }

        if (policyTypeList != null) {
            sqa.addClause("a.pol_type_id in (" + policyTypeList + ")");
        } else {

            if (stPolicyTypeGroupID != null) {
                sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
//            sqa.addPar(stPolicyTypeGroupID);
            }

            if (stPolicyTypeID != null) {
                sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            }
        }

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
//            sqa.addPar(stBussinessPolType);
            } else if (stBussinessPolType.equalsIgnoreCase("2")) {
                sqa.addClause("a.cc_code <> '80'");
//            sqa.addPar(stBussinessPolType);
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

        if (stCustCategory1 != null) {
            sqa.addClause("e.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        if (stBussinessPolType != null) {
            sqa.addClause("n.business_type_id = '" + stBussinessPolType + "'");
//            sqa.addPar(stBussinessPolType);
        }

        if (stPolicyNo != null) {
            sqa.addClause("a.pol_no like '" + stPolicyNo + "%'");
//            sqa.addPar(stPolicyNo + '%');
        }

//        if (stCreditID != null) {
//            sqa.addClause("a.kreasi_type_id = '" + stCreditID + "'");
////            sqa.addPar(stCreditID);
//        }

        if (Tools.isYes(stCashcall)) {
            sqa.addClause("a.claim_cash_call_f = 'Y'");
        }
//        else {
//            sqa.addClause("coalesce(a.claim_cash_call_f,'N') <> 'Y'");
//        }

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
                + "sum(checkreas(c.ins_item_id in (49),c.amount*a.ccy_rate_claim)) as wreck,"
                + "sum(checkreas(c.ins_item_id in (51),c.amount*a.ccy_rate_claim)) as tjh,"
                + "sum(checkreas(c.ins_item_id in (55),c.amount*a.ccy_rate_claim)) as bunga,"
                + "sum(checkreas(c.ins_item_id in (56),c.amount*a.ccy_rate_claim)) as sntn_kclkn,"
                + "sum(checkreas(c.ins_item_id in (61),c.amount*a.ccy_rate_claim)) as umk,"
                + "sum(checkreas(c.ins_item_id in (72),c.amount*a.ccy_rate_claim)) as exgratia_bebanuw,"
                + "sum(checkreas(c.ins_item_id in (73),c.amount*a.ccy_rate_claim)) as feerecov,"
                + "sum(checkreas(c.ins_item_id in (74),c.amount*a.ccy_rate_claim)) as by_sparepart,"
                + "sum(checkreas(c.ins_item_id in (77),c.amount*a.ccy_rate_claim)) as by_survey,"
                + "sum(checkreas(c.ins_item_id in (78),c.amount*a.ccy_rate_claim)) as ppn,"
                + "sum(checkreas(c.ins_item_id in (80),c.amount*a.ccy_rate_claim)) as material,"
                + "sum(checkreas(c.ins_item_id in (81),c.amount*a.ccy_rate_claim)) as survey_adjfee,"
                + "sum(checkreas(c.ins_item_id in (82),c.amount*a.ccy_rate_claim)) as expenses,"
                + "sum(checkreas(c.ins_item_id in (83),c.amount*a.ccy_rate_claim)) as vat_fee,"
                + "sum(checkreas(c.ins_item_id in (84),c.amount*a.ccy_rate_claim)) as by_adm,"
                + "sum(checkreas(c.ins_item_id in (102),c.amount*a.ccy_rate_claim)) as klaim_reas,"
                + "sum(checkreas(c.ins_item_id in (103),c.amount*a.ccy_rate_claim)) as pa_pnp,"
                + "sum(checkreas(c.ins_item_id in (104),c.amount*a.ccy_rate_claim)) as pa_png,"
                + "sum(checkreas(c.ins_item_id in (109),c.amount*a.ccy_rate_claim)) as by_forensik,"
                + "sum(checkreas(c.ins_item_id in (110),c.amount*a.ccy_rate_claim)) as by_konsultan,"
                + "sum(checkreas(c.ins_item_id not in (46,47,48,49,50,51,52,53,54,55,56,60,61,65,70,72,73,74,76,77,78,79,80,81,82,83,84,102,103,104,109,110) and item_class = 'CLM',c.amount*a.ccy_rate_claim)) as Lainlain "
                + "from ( " + sqa.getSQL() + "  group by c.description,a.pol_type_id,a.status,b.refd1,b.refd2,b.refd3,b.ref3,b.ref16,b.ref8,b.ref25,a.period_start,a.period_end,a.claim_propose_date,a.dla_date,a.claim_date,a.policy_date,a.pol_no,a.pla_no,a.dla_no,"
                + "e.ent_name,f.ent_name,a.cust_name,a.ccy_rate_claim,a.claim_currency,a.ins_policy_type_grp_id,b.ref2,b.ref3d,b.ref1,b.insured_amount,a.claim_amount,a.cc_code_source,p.region_code,o.ent_name,n.description,s.vs_description,a.claim_cash_call_f,"
                + "a.claim_amount_est,a.claim_amount_approved,a.approved_date,k.cause_desc,d.vs_description,a.kreasi_type_desc,a.pol_id,b.risk_class,l.vs_description,m.vs_description,a.pol_id,a.refd1,a.claim_sub_cause,r.cause_desc,a.claim_sub_cause_other,q.vs_description,a.ref15,a.ref16 "
                + "order by a.pol_no ) a inner join ins_pol_items c on c.pol_id = a.pol_id "
                + "where a.status IN ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') ";

        if (paymentDateFrom != null) {
            sql = sql + " and date_trunc('day',a.tgl_bayar) >= '" + getPaymentDateFrom() + "'";
        }
        if (paymentDateTo != null) {
            sql = sql + " and date_trunc('day',a.tgl_bayar) <= '" + getPaymentDateTo() + "'";
        }

        sql = sql + " group by period_start,period_end,cabang,status,pol_type_id,propose_date,dla_date,claim_date,policy_date,a.pol_id,pol_no,pla_no,dla_no,"
                + "sumbis,pemasar,cust_name,ccy_rate_claim,kurs,tertanggung,tgl_lahir,nik_ktp,no_rek,insured_amount,claim_amount,claim_amount_est,claim_amount_approved,"
                + "claim_or,claim_bppdan,claim_spl,claim_fac,claim_qs,claim_park,claim_faco,claim_faco1,claim_faco2,claim_faco3,claim_jp,claim_qskr,claim_kscbi,"
                + "approved_date,penyebab,coverage,jenis_kredit,kriteria,risk_code,risk_class,tgl_bayar,nobuk,cc_code_source,region_code,paymentname,tglsubro,"
                + "kode_penyebab_spesifik,penyebab_spesifik,keterangan_lainnya,pekerjaan,keterangan,instansi_bekerja,cob,kategori,claim_cash_call_f "
                + " order by a.pol_no,a.dla_no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_DETILKLAIM_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCEL_DETAILKLAIM_WH() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("refd2 as period_start,refd3 as period_end,cc_code as cabang,status,pol_type_id,claim_propose_date as propose_date,dla_date,claim_date,policy_date,"
                + "pol_id,pol_no,pla_no,dla_no,sumbis,marketer as pemasar,cust_name,ccy_rate_claim,nama as tertanggung,tgl_lahir,insured_amount,claim_amount,claim_amount_est,claim_amount_approved,"
                + "klaim_or,klaim_bppdan,klaim_spl,klaim_fac,klaim_qs,klaim_park,klaim_faco,klaim_faco1,klaim_faco2,klaim_faco3,klaim_jp,klaim_qskr,klaim_kscbi,"
                + "approved_date,cause_desc as penyebab,b.tgl_bayar,b.no_bukti,cc_code_source,klaimbruto,deductible,subrogasi,biayaadjuster,"
                + "salvage,biayaderek,exgratiaklaim,jasabengkel,pajakjasabengkel,ppn,depresiasi,penalty,joinplacement,cashcollateralsubrograsi,wreck,tjh,bunga,"
                + "santunankecelakaan as sntn_kclkn,exgratiabebanunderwritinglainlain as exgratia_bebanuw,feerecovery as feerecov,biayasparepart as by_sparepart,"
                + "biayasurvey as by_survey,ppn,material,surveyadjusmentfee as survey_adjfee,expenses,vatfee as vat_fee,biayaadministrasimateraidll as by_adm ");

        String invoice = "select a.* from ( "
                + "select row_number() over(partition by a.attr_pol_id order by a.attr_pol_id,a.ar_invoice_dtl_id asc) AS nomor,"
                + "a.attr_pol_id,a.attr_pol_no,a.dlano,a.no_bukti,a.tgl_bayar from data_invoice_claim a where a.tipe in ('DI','OP') ";
        if (paymentDateFrom != null) {
            invoice = invoice + " and date_trunc('day',a.tgl_bayar) >= '" + getPaymentDateFrom() + "'";
        }
        if (paymentDateTo != null) {
            invoice = invoice + " and date_trunc('day',a.tgl_bayar) <= '" + getPaymentDateTo() + "'";
        }

        invoice = invoice + " order by a.attr_pol_id,a.ar_invoice_dtl_id ) a where nomor = 1 order by a.attr_pol_id ";

        sqa.addQuery(" from prod_klaim a "
                + "left join (" + invoice + ") b on b.attr_pol_id = a.pol_id");

        sqa.addClause(" a.status in ('CLAIM','CLAIM ENDORSE') ");

        if (appDateFrom != null) {
//            sqa.addClause("date_trunc('day',a.approved_date) >= '" + appDateFrom + "'");
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(appDateFrom);
        }

        if (appDateTo != null) {
//            sqa.addClause("date_trunc('day',a.approved_date) <= '" + appDateTo + "'");
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(appDateTo);
        }

        if (stPolicyTypeID != null) {
//            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stBranch != null) {
            if (stBranch.equalsIgnoreCase("80")) {
//                sqa.addClause("a.cc_code = '" + stBranch + "'");
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(stBranch);

                if (stBranchSource != null) {
//                    sqa.addClause("a.cc_code_source = '" + stBranchSource + "'");
                    sqa.addClause("a.cc_code_source = ?");
                    sqa.addPar(stBranchSource);
                }
                if (stRegionSource != null) {
//                    sqa.addClause("a.region_id_source = '" + stRegionSource + "'");
                    sqa.addClause("a.region_id_source = ?");
                    sqa.addPar(stRegionSource);
                }
            } else {
//                sqa.addClause("((a.cc_code = '" + stBranch + "') or (a.cc_code = '80' and a.cc_code_source = '" + stBranch + "'))");
                sqa.addClause("((a.cc_code = ?) or (a.cc_code = '80' and a.cc_code_source = ?))");
                sqa.addPar(stBranch);
                sqa.addPar(stBranch);
            }
        }

        if (stRegion != null) {
//            sqa.addClause("a.region_id = '" + stRegion + "'");
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stRegion);
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
//            sqa.addClause("a.grups = '" + stCompanyID + "'");
            sqa.addClause("a.grups = ?");
            sqa.addPar(stCompanyID);
        }

        if (stEntityID != null) {
//            sqa.addClause("a.idsumbis = '" + stEntityID + "'");
            sqa.addClause("a.idsumbis = ?");
            sqa.addPar(stEntityID);
        }

        if (stMarketerID != null) {
//            sqa.addClause("a.idmarketer = '" + stMarketerID + "'");
            sqa.addClause("a.idmarketer = ?");
            sqa.addPar(stMarketerID);
        }

        if (stCompanyProdID != null) {
//            sqa.addClause("a.grupm = '" + stCompanyProdID + "'");
            sqa.addClause("a.grupm = ?");
            sqa.addPar(stCompanyProdID);
        }

        if (stPolicyNo != null) {
//            sqa.addClause("a.pol_no like '" + stPolicyNo + "%'");
            sqa.addClause("a.pol_no like ?");
            sqa.addPar(stPolicyNo + '%');
        }

        if (paymentDateFrom != null && paymentDateTo != null) {

            String payment = "select a.attr_pol_id from ( select row_number() over(partition by a.attr_pol_id order by a.attr_pol_id,a.ar_invoice_dtl_id asc) AS nomor,a.attr_pol_id,a.attr_pol_no,"
                    + "a.dlano,a.no_bukti,a.tgl_bayar from data_invoice_claim a where a.tipe in ('DI','OP') ";

            if (paymentDateFrom != null) {
                payment = payment + " and date_trunc('day',a.tgl_bayar) >= '" + getPaymentDateFrom() + "'";
            }
            if (paymentDateTo != null) {
                payment = payment + " and date_trunc('day',a.tgl_bayar) <= '" + getPaymentDateTo() + "'";
            }

            payment = payment + " order by a.attr_pol_id,a.ar_invoice_dtl_id ) a where nomor = 1 order by a.attr_pol_id ";

            sqa.addClause("a.pol_id in (" + payment + ") ");
        }

        String sql = sqa.getSQL() + " order by a.pol_no,a.dla_no ";


        final DTOList l = ListUtil.getDTOListFromQueryDS(
                sql,
                sqa.getPar(),
                HashDTO.class, "WHOUSEDS");

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

//        SQLUtil S = new SQLUtil("WHOUSEDS");

//        String nama_file = "EXCEL_DETILKLAIM_" + System.currentTimeMillis() + ".csv";
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
//        File file = new File("//dwhs.askrida.co.id//exportdb/csv/" + nama_file);
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
    }

    /**
     * @return the paymentDateFrom
     */
    public Date getPaymentDateFrom() {
        return paymentDateFrom;
    }

    /**
     * @param paymentDateFrom the paymentDateFrom to set
     */
    public void setPaymentDateFrom(Date paymentDateFrom) {
        this.paymentDateFrom = paymentDateFrom;
    }

    /**
     * @return the paymentDateTo
     */
    public Date getPaymentDateTo() {
        return paymentDateTo;
    }

    /**
     * @param paymentDateTo the paymentDateTo to set
     */
    public void setPaymentDateTo(Date paymentDateTo) {
        this.paymentDateTo = paymentDateTo;
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

    public DTOList EXCEL_PROPOTIONAL_TREATY_WHOUSE() throws Exception {
        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.pol_no,a.cust_name,a.order_no,a.nama,a.coverage,a.treaty_grp_id,a.refd2,a.refd3,a.claim_date,a.tsi_obj,"
                + "a.ccy_rate_claim,a.claim_amount,a.claim_reins,a.reins_ent_id,a.reins_ent_name from prod_reas_treaty_obj a "
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

        if (getTreatyTypeList() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_type in (" + treatyTypeList + ")";
        } else {
            if (getStFltTreatyType() != null) {
                if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                    sqlKlaim = sqlKlaim + " and a.treaty_type in ('SPL','QS')";
                } else {
                    sqlKlaim = sqlKlaim + " and a.treaty_type = '" + stFltTreatyType + "'";
                }
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

        return klaim;
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

        //bikin sheet2
        XSSFSheet sheet2 = wb.createSheet("Prop_tty_Klaim");

        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        for (int jk = 0; jk < list2.size(); jk++) {
            HashDTO h = (HashDTO) list2.get(jk);

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

        if (getTreatyTypeList() != null) {
            sqlPremi = sqlPremi + " and a.treaty_type in (" + treatyTypeList + ")";
        } else {
            if (getStFltTreatyType() != null) {
                if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                    sqlPremi = sqlPremi + " and a.treaty_type in ('SPL','QS')";
                } else {
                    sqlPremi = sqlPremi + " and a.treaty_type = '" + stFltTreatyType + "'";
                }
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
        final SQLAssembler sqaK = new SQLAssembler();

        String sqlKlaim = "select a.*,b.cause_desc from prod_reas_treaty_obj a "
                + "inner join prod_klaim b on b.pol_id = a.pol_id "
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

        if (getTreatyTypeList() != null) {
            sqlKlaim = sqlKlaim + " and a.treaty_type in (" + treatyTypeList + ")";
        } else {
            if (getStFltTreatyType() != null) {
                if (getStFltTreatyType().equalsIgnoreCase("SPL") || getStFltTreatyType().equalsIgnoreCase("QS")) {
                    sqlKlaim = sqlKlaim + " and a.treaty_type in ('SPL','QS')";
                } else {
                    sqlKlaim = sqlKlaim + " and a.treaty_type = '" + stFltTreatyType + "'";
                }
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

        return klaim;
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
    private String stTreatyYearGrpID;
    private String stTreatyYearGrpDesc;

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
    private String stPolJenas;

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
    private String stCashcall;

    /**
     * @return the stCashcall
     */
    public String getStCashcall() {
        return stCashcall;
    }

    /**
     * @param stCashcall the stCashcall to set
     */
    public void setStCashcall(String stCashcall) {
        this.stCashcall = stCashcall;
    }
}

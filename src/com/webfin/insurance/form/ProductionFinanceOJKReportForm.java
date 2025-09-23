/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionFinanceOJKReportForm
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
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.BiayaOperasionalDetail;
import com.webfin.insurance.model.BiayaOperasionalGroup;
import com.webfin.insurance.model.InsuranceProdukView;
import com.webfin.insurance.model.RincianOJKReport;
import com.webfin.postalcode.model.PostalCodeView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.SessionContext;

public class ProductionFinanceOJKReportForm extends Form {

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
    private Date periodFrom;
    private Date periodTo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date appDateFrom;
    private Date appDateTo;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeGroupDesc;
    private String stPolicyTypeID;
    private String stPolicyTypeDesc;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String stTriwulan;
    private String stIndex;
    private String stYear;
    private String stProdOJKID;
    private String stProdOJKDesc;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
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

    public DTOList RPT_DATARINCIAN() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();
        String trwulan = "";

        System.out.println("lewat sini" + getStTriwulan());

        if (getStTriwulan() != null) {
            if (getStTriwulan().equalsIgnoreCase("1")) {
                trwulan = " , sum(bal1+bal2+bal3) as refn1";
            } else if (getStTriwulan().equalsIgnoreCase("2")) {
                trwulan = " , sum(bal4+bal5+bal6) as refn1";
            } else if (getStTriwulan().equalsIgnoreCase("3")) {
                trwulan = " , sum(bal7+bal8+bal9) as refn1";
            } else if (getStTriwulan().equalsIgnoreCase("4")) {
                trwulan = " , sum(bal10+bal1+bal12) as refn1";
            }
        }

        sqa.addSelect("d.vs_description as ref10 " + trwulan + " ");
        sqa.addQuery("from gl_acct_bal2 a"
                + " inner join gl_accounts b on b.account_id = a.account_id"
                + " inner join ent_master c on c.gl_code = substr(b.accountno,6,5)"
                + " inner join s_company_group d on cast(d.vs_code as text) = c.ref2 ");

        sqa.addClause(" a.period_year = " + Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        sqa.addClause(" a.idr_flag = 'Y'");
        sqa.addClause(" substr(b.accountno,1,4) = '1112'");
        sqa.addClause(" c.gl_code <> '00000'");

        final String sql = sqa.getSQL() + "  group by d.vs_description order by d.vs_description";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        return l;


    }

    public DTOList RPT_DATAOJK102() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();
        System.out.println("lewat sinix" + getStTriwulan());
        String trwulan = "";

        if (getStTriwulan() != null) {
            if (getStTriwulan().equalsIgnoreCase("1")) {
                trwulan = " , sum(bal1+bal2+bal3) as refn1";
            } else if (getStTriwulan().equalsIgnoreCase("2")) {
                trwulan = " , sum(bal4+bal5+bal6) as refn2";
            } else if (getStTriwulan().equalsIgnoreCase("3")) {
                trwulan = " , sum(bal7+bal8+bal9) as refn3";
            } else if (getStTriwulan().equalsIgnoreCase("4")) {
                trwulan = " , sum(bal10+bal1+bal12) as refn4";
            }
        }
        sqa.addSelect("c.ent_name as ref10  " + trwulan + " ");
        sqa.addQuery("from gl_acct_bal2 a"
                + " inner join gl_accounts b on b.account_id = a.account_id"
                + " inner join ent_master c on c.gl_code = substr(b.accountno,6,5)");
//                     + " inner join s_company_group d on cast(d.vs_code as text) = c.ref2 ");

        sqa.addClause(" a.period_year = " + Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        sqa.addClause(" a.idr_flag = 'Y'");
        sqa.addClause(" substr(b.accountno,1,4) = '1112'");
        sqa.addClause(" c.gl_code <> '00000'");

        final String sql = sqa.getSQL() + "  group by c.ent_name order by c.ent_name";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        return l;


    }

    public DTOList RPT_DATAOJK104() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();


        String trwulan = "";

        if (getStTriwulan() != null) {
            if (getStTriwulan().equalsIgnoreCase("1")) {
                trwulan = " , sum(bal1+bal2+bal3) as refn1";
            } else if (getStTriwulan().equalsIgnoreCase("2")) {
                trwulan = " , sum(bal4+bal5+bal6) as refn2";
            } else if (getStTriwulan().equalsIgnoreCase("3")) {
                trwulan = " , sum(bal7+bal8+bal9) as refn3";
            } else if (getStTriwulan().equalsIgnoreCase("4")) {
                trwulan = " , sum(bal10+bal1+bal12) as refn4";
            }
        }
        sqa.addSelect("e.description as ref10  " + trwulan + " ");
        sqa.addQuery(" from gl_acct_bal2 a "
                + " inner join gl_accounts b on b.account_id = a.account_id"
                + " inner join ent_master c on c.gl_code = substr(b.accountno,6,5)"
                + " inner join s_company_group d on cast(d.vs_code as text) = c.ref2"
                + " inner join gl_accounts e on e.account_id = a.account_id");
        sqa.addClause(" a.period_year = " + Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        sqa.addClause(" a.idr_flag = 'Y'");
        sqa.addClause(" substr(b.accountno,1,4) = '1112'");
        sqa.addClause(" c.gl_code <> '00000'");
        final String sql = sqa.getSQL() + "   group by e.description order by e.description";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);


        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        return l;

    }

    public DTOList RPT_DATAOJK108() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();


        String trwulan = "";

        if (getStTriwulan() != null) {
            if (stTriwulan.equalsIgnoreCase("1")) {
                trwulan = " , sum(bal1+bal2+bal3) as refn1";
            } else if (stTriwulan.equalsIgnoreCase("2")) {
                trwulan = " , sum(bal4+bal5+bal6) as refn2";
            } else if (stTriwulan.equalsIgnoreCase("3")) {
                trwulan = " , sum(bal7+bal8+bal9) as refn3";
            } else if (stTriwulan.equalsIgnoreCase("4")) {
                trwulan = " , sum(bal10+bal1+bal12) as refn4";
            }
        }
        sqa.addSelect("e.description as ref10  " + trwulan + " ");
        sqa.addQuery(" from gl_acct_bal2 a "
                + " inner join gl_accounts b on b.account_id = a.account_id"
                + " inner join ent_master c on c.gl_code = substr(b.accountno,6,5)"
                + " inner join s_company_group d on cast(d.vs_code as text) = c.ref2"
                + " inner join gl_accounts e on e.account_id = a.account_id");
        sqa.addClause(" a.period_year = " + Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        sqa.addClause(" a.idr_flag = 'Y'");
        sqa.addClause(" substr(b.accountno,1,4) = '1112'");
        sqa.addClause(" c.gl_code <> '00000'");
        final String sql = sqa.getSQL() + "   group by e.description order by e.description";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        return l;

    }

    /**
     * @return the periodFrom
     */
    public Date getPeriodFrom() {
        return periodFrom;
    }

    /**
     * @param periodFrom the periodFrom to set
     */
    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    /**
     * @return the periodTo
     */
    public Date getPeriodTo() {
        return periodTo;
    }

    /**
     * @param periodTo the periodTo to set
     */
    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    /**
     * @return the policyDateFrom
     */
    public Date getPolicyDateFrom() {
        return policyDateFrom;
    }

    /**
     * @param policyDateFrom the policyDateFrom to set
     */
    public void setPolicyDateFrom(Date policyDateFrom) {
        this.policyDateFrom = policyDateFrom;
    }

    /**
     * @return the policyDateTo
     */
    public Date getPolicyDateTo() {
        return policyDateTo;
    }

    /**
     * @param policyDateTo the policyDateTo to set
     */
    public void setPolicyDateTo(Date policyDateTo) {
        this.policyDateTo = policyDateTo;
    }

    /**
     * @return the appDateFrom
     */
    public Date getAppDateFrom() {
        return appDateFrom;
    }

    /**
     * @param appDateFrom the appDateFrom to set
     */
    public void setAppDateFrom(Date appDateFrom) {
        this.appDateFrom = appDateFrom;
    }

    /**
     * @return the appDateTo
     */
    public Date getAppDateTo() {
        return appDateTo;
    }

    /**
     * @param appDateTo the appDateTo to set
     */
    public void setAppDateTo(Date appDateTo) {
        this.appDateTo = appDateTo;
    }

    /**
     * @return the stPolicyTypeGroupID
     */
    public String getStPolicyTypeGroupID() {
        return stPolicyTypeGroupID;
    }

    /**
     * @param stPolicyTypeGroupID the stPolicyTypeGroupID to set
     */
    public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
        this.stPolicyTypeGroupID = stPolicyTypeGroupID;
    }

    /**
     * @return the stPolicyTypeGroupDesc
     */
    public String getStPolicyTypeGroupDesc() {
        return stPolicyTypeGroupDesc;
    }

    /**
     * @param stPolicyTypeGroupDesc the stPolicyTypeGroupDesc to set
     */
    public void setStPolicyTypeGroupDesc(String stPolicyTypeGroupDesc) {
        this.stPolicyTypeGroupDesc = stPolicyTypeGroupDesc;
    }

    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    /**
     * @return the stBranchName
     */
    public String getStBranchName() {
        return stBranchName;
    }

    /**
     * @param stBranchName the stBranchName to set
     */
    public void setStBranchName(String stBranchName) {
        this.stBranchName = stBranchName;
    }

    /**
     * @return the stRegion
     */
    public String getStRegion() {
        return stRegion;
    }

    /**
     * @param stRegion the stRegion to set
     */
    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }

    /**
     * @return the stRegionDesc
     */
    public String getStRegionDesc() {
        return stRegionDesc;
    }

    /**
     * @param stRegionDesc the stRegionDesc to set
     */
    public void setStRegionDesc(String stRegionDesc) {
        this.stRegionDesc = stRegionDesc;
    }

    /**
     * @return the stRegionName
     */
    public String getStRegionName() {
        return stRegionName;
    }

    /**
     * @param stRegionName the stRegionName to set
     */
    public void setStRegionName(String stRegionName) {
        this.stRegionName = stRegionName;
    }

    /**
     * @return the stTriwulan
     */
    public String getStTriwulan() {
        return stTriwulan;
    }

    /**
     * @param stTriwulan the stTriwulan to set
     */
    public void setStTriwulan(String stTriwulan) {
        this.stTriwulan = stTriwulan;
    }

    /**
     * @return the stIndex
     */
    public String getStIndex() {
        return stIndex;
    }

    /**
     * @param stIndex the stIndex to set
     */
    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
    }

    /**
     * @return the canNavigateBranch
     */
    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    /**
     * @param canNavigateBranch the canNavigateBranch to set
     */
    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    /**
     * @return the canNavigateRegion
     */
    public boolean isCanNavigateRegion() {
        return canNavigateRegion;
    }

    /**
     * @param canNavigateRegion the canNavigateRegion to set
     */
    public void setCanNavigateRegion(boolean canNavigateRegion) {
        this.canNavigateRegion = canNavigateRegion;
    }

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/prodrpt_ojk")).list();

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

        final DTOList l = RPT_DATAOJK();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final ArrayList plist = new ArrayList();

        plist.add(stReport);

        String urx = null;

        //logger.logDebug("printPolicy: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/insurance/prodrpt_ojk/" + s + ".fop?xlang=" + getStLang();
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        super.redirect(urx);

    }

    /**
     * @return the stLang
     */
    public String getStLang() {
        return stLang;
    }

    /**
     * @param stLang the stLang to set
     */
    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    /**
     * @return the stFontSize
     */
    public String getStFontSize() {
        return stFontSize;
    }

    /**
     * @param stFontSize the stFontSize to set
     */
    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    /**
     * @return the stPrintForm
     */
    public String getStPrintForm() {
        return stPrintForm;
    }

    /**
     * @param stPrintForm the stPrintForm to set
     */
    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    /**
     * @return the enableRiskFilter
     */
    public boolean isEnableRiskFilter() {
        return enableRiskFilter;
    }

    /**
     * @param enableRiskFilter the enableRiskFilter to set
     */
    public void setEnableRiskFilter(boolean enableRiskFilter) {
        this.enableRiskFilter = enableRiskFilter;
    }

    /**
     * @return the stReportTitle
     */
    public String getStReportTitle() {
        return stReportTitle;
    }

    /**
     * @param stReportTitle the stReportTitle to set
     */
    public void setStReportTitle(String stReportTitle) {
        this.stReportTitle = stReportTitle;
    }

    public void go() {
        stPrintForm = (String) getAttribute("rpt");
        enableSelectForm = false;

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_PRINTING");
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public void onChangePolicyTypeGroup() {
    }

    public void onChangeReport() {
    }

    public void onChangeBranchGroup() {
    }

    public DTOList RPT_DATAOJK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String fld = "bal";
        String query = null;
        String plist = null;

        query = "a.account_id as pol_id,b.accountno as pol_no,b.noper,b.description,substr(b.accountno,14,2) as cc_code,c.description as ref1,";

        if (stTriwulan != null) {
            if (stTriwulan.equalsIgnoreCase("I")) {
                plist = GLUtil.getPeriodList(0, 3, fld, "+");
                query = query + "coalesce(sum(" + plist + "),0) as premi_total ";
            } else if (stTriwulan.equalsIgnoreCase("II")) {
                plist = GLUtil.getPeriodList(0, 6, fld, "+");
                query = query + "coalesce(sum(" + plist + "),0) as premi_total ";
            } else if (stTriwulan.equalsIgnoreCase("III")) {
                plist = GLUtil.getPeriodList(0, 9, fld, "+");
                query = query + "coalesce(sum(" + plist + "),0) as premi_total ";
            } else if (stTriwulan.equalsIgnoreCase("IV")) {
                plist = GLUtil.getPeriodList(0, 12, fld, "+");
                query = query + "coalesce(sum(" + plist + "),0) as premi_total ";
            }
        }

        sqa.addSelect(query);

        sqa.addQuery(" from gl_accounts b "
                + "left join gl_acct_bal2 a on b.account_id = a.account_id "
                + "left join gl_cost_center c on c.cc_code = substr(b.accountno,14,2) ");

            sqa.addClause("a.idr_flag = 'Y'");
            sqa.addClause("b.acctype is null");

        if (stBranch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(stBranch);
        }

        if (stYear != null) {
            sqa.addClause("a.period_year = ?");
            sqa.addPar(stYear);
        }

        String sql = null;

        String account[] = getOJKReport().getStAccount().split("[\\|]");
        if (account.length == 1) {
            sql = "select b.pol_no,b.cc_code,b.noper,b.description,b.cc_code,b.premi_total,a.ref1 "
                    + " from s_report_ojk a left join (  "
                    + sqa.getSQL() + " group by a.account_id,b.accountno,b.noper,b.description,b.accountno,c.description "
                    + ") b on (substr(b.pol_no,1,length(a.account)) between a.account and a.account) "
                    + " where a.ojk_id = ? and b.premi_total <> 0 ";
            sqa.addPar(stProdOJKID);
            sql = sql + " order by b.cc_code,b.pol_no ";
        } else if (account.length > 1) {
            sql = "select b.pol_no,b.cc_code,b.noper,b.description,b.cc_code,b.premi_total,a.ref1 "
                    + " from s_report_ojk a left join (  "
                    + sqa.getSQL() + " and (substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
            for (int k = 1; k < account.length; k++) {
                sql = sql + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
            }
            sql = sql + " ) group by a.account_id,b.accountno,b.noper,b.description,b.accountno,c.description "
                    + " ) b on (substr(b.pol_no,1," + account[account.length - 1].length() + ") between '" + account[0] + "' and '" + account[account.length - 1] + "') "
                    + " where a.ojk_id = ? and b.premi_total <> 0 ";
            sqa.addPar(stProdOJKID);
            sql = sql + " order by b.cc_code,b.pol_no ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    /**
     * @return the stYear
     */
    public String getStYear() {
        return stYear;
    }

    /**
     * @param stYear the stYear to set
     */
    public void setStYear(String stYear) {
        this.stYear = stYear;
    }

    /**
     * @return the stProdOJKID
     */
    public String getStProdOJKID() {
        return stProdOJKID;
    }

    /**
     * @param stProdOJKID the stProdOJKID to set
     */
    public void setStProdOJKID(String stProdOJKID) {
        this.stProdOJKID = stProdOJKID;
    }

    /**
     * @return the stProdOJKDesc
     */
    public String getStProdOJKDesc() {
        return stProdOJKDesc;
    }

    /**
     * @param stProdOJKDesc the stProdOJKDesc to set
     */
    public void setStProdOJKDesc(String stProdOJKDesc) {
        this.stProdOJKDesc = stProdOJKDesc;
    }

    public RincianOJKReport getOJKReport() {
        final RincianOJKReport ojkid = (RincianOJKReport) DTOPool.getInstance().getDTO(RincianOJKReport.class, stProdOJKID);

        return ojkid;
    }
}

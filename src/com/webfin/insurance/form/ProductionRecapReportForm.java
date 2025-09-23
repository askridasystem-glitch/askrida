/***********************************************************************
 * Module:  com.webfin.insurance.form.ProductionRecapReportForm
 * Author:  Denny Mahendra
 * Created: Sep 17, 2006 1:58:50 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.controller.FOPServlet;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.lov.LOVManager;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.ServletOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ProductionRecapReportForm extends Form {

    /**
     * @return the formList
     */
    public static HashSet getFormList() {
        return formList;
    }

    /**
     * @param aFormList the formList to set
     */
    public static void setFormList(HashSet aFormList) {
        formList = aFormList;
    }
    private Date dtPeriodFrom;
    private Date dtPeriodTo;
    private DTOList currentData;
    private HashMap map1;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date mutationFrom;
    private Date mutationTo;
    private String periodFrom;
    private String periodFromName;
    private String periodTo;
    private String periodToName;
    private String year;
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
    private String stReportType;
    private String stReport;
    private String stReportDesc;
    private boolean enableRiskFilter;
    private boolean enableSelectForm = true;
    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String stPrintForm;
    private String stFontSize;
    private String stLang;
    private String stReportTitle;
    private String stFileName;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(ProductionMarketingReportForm.class);

    public Date getDtPeriodFrom() {
        return dtPeriodFrom;
    }

    public void setDtPeriodFrom(Date dtPeriodFrom) {
        this.dtPeriodFrom = dtPeriodFrom;
    }

    public Date getDtPeriodTo() {
        return dtPeriodTo;
    }

    public void setDtPeriodTo(Date dtPeriodTo) {
        this.dtPeriodTo = dtPeriodTo;
    }

//    public void initialize() {
//        setTitle("PRODUCTION SUMMARY REPORT");
//    }
    public void print() throws Exception {


        final SQLAssembler sqa = new SQLAssembler();
        sqa.setStDS("!DrcDS!");

        sqa.addSelect("      b.category1 as sumbis, cc_code ,b.ins_inward_flag as inward_flag,"
                + "      sum(a.premi_total) as premi_total");

        sqa.addQuery("   from "
                + "      ins_policy a"
                + "      left join ent_master b on b.ent_id = a.entity_id");


        if (dtPeriodFrom != null) {
            sqa.addClause("a.policy_date >= ?");
            sqa.addPar(dtPeriodFrom);
        }

        if (dtPeriodTo != null) {
            sqa.addClause("a.policy_date <= ?");
            sqa.addPar(dtPeriodTo);
        }

        final String sql = sqa.getSQL() + "   group by "
                + "      cc_code,ins_inward_flag,b.category1";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyView.class);

        currentData = l;

        map1 = currentData.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final InsurancePolicyView pol = (InsurancePolicyView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("DATA", l);

        redirect("/pages/insurance/prodrpt/prodrekap.fop");
    }

    public BigDecimal getAmount(String regCode, String cod) {

        final InsurancePolicyView pol = (InsurancePolicyView) map1.get(regCode + "/" + cod);

        if (pol == null) {
            return null;
        }

        return pol.getDbPremiTotal();
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
     * @return the mutationFrom
     */
    public Date getMutationFrom() {
        return mutationFrom;
    }

    /**
     * @param mutationFrom the mutationFrom to set
     */
    public void setMutationFrom(Date mutationFrom) {
        this.mutationFrom = mutationFrom;
    }

    /**
     * @return the mutationTo
     */
    public Date getMutationTo() {
        return mutationTo;
    }

    /**
     * @param mutationTo the mutationTo to set
     */
    public void setMutationTo(Date mutationTo) {
        this.mutationTo = mutationTo;
    }

    /**
     * @return the stReportType
     */
    public String getStReportType() {
        return stReportType;
    }

    /**
     * @param stReportType the stReportType to set
     */
    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    /**
     * @return the stReport
     */
    public String getStReport() {
        return stReport;
    }

    /**
     * @param stReport the stReport to set
     */
    public void setStReport(String stReport) {
        this.stReport = stReport;
    }

    /**
     * @return the stReportDesc
     */
    public String getStReportDesc() {
        return stReportDesc;
    }

    /**
     * @param stReportDesc the stReportDesc to set
     */
    public void setStReportDesc(String stReportDesc) {
        this.stReportDesc = stReportDesc;
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
     * @return the enableSelectForm
     */
    public boolean isEnableSelectForm() {
        return enableSelectForm;
    }

    /**
     * @param enableSelectForm the enableSelectForm to set
     */
    public void setEnableSelectForm(boolean enableSelectForm) {
        this.enableSelectForm = enableSelectForm;
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
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/prodrpt")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
    }

    private DTOList loadList() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING_REKAP", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY");

        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return null;
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public void clickPrintValidate() throws Exception {
        loadFormList();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        String filename = stReport + "-" + periodFromName + year + "-" + periodToName + year;
        SessionManager.getInstance().getRequest().setAttribute("SAVE_TO_FILE", "Y");
        SessionManager.getInstance().getRequest().setAttribute("FILE_NAME", filename);
        SessionManager.getInstance().getRequest().setAttribute("REPORT_PROD", "Y");

        final ArrayList plist = new ArrayList();

        plist.add(stReport);

        String urxSave = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urxSave = "/pages/insurance/prodrpt/" + s + "_SAVE.fop";
                break;
            }
        }

        if (urxSave == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        FOPServlet srv = new FOPServlet();
        srv.saveFOP(SessionManager.getInstance().getRequest(), SessionManager.getInstance().getResponse(), urxSave);
        //SessionManager.getInstance().getRequest().getRequestDispatcher(urxSave).forward(SessionManager.getInstance().getRequest(), SessionManager.getInstance().getResponse());

        //COPY POLIS KE DIRECTORY
        String filePath = "D:/fin-repository/report_temp/edocument/" + filename + "_temp.pdf";
        String uploadPath = "uw-" + filename + ".pdf";
//        copyWithFTP(filePath, uploadPath);

        super.redirect(urxSave);
    }

    /**
     * @return the ref1
     */
    public String getRef1() {
        return ref1;
    }

    /**
     * @param ref1 the ref1 to set
     */
    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    /**
     * @return the refPropMap
     */
    public HashMap getRefPropMap() {
        return refPropMap;
    }

    /**
     * @param refPropMap the refPropMap to set
     */
    public void setRefPropMap(HashMap refPropMap) {
        this.refPropMap = refPropMap;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void onChangeReport() {
    }

    public void chgform() {
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

    public void go() {
        stPrintForm = (String) getAttribute("rpt");
        enableSelectForm = false;

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_PRINTING_REKAP");
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

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public DTOList REKAP_CAB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.sumbis,a.cc_code,sum(a.premi) as premi,");

        String sql = " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20)";

        if (getPeriodFrom() != null) {
            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (getYear() != null) {
            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa.addPar(year);
        }

        sqa.addQuery(sql + " ) as premi_base from ins_rekap_produksi a ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        sql = "select e.cc_code,"
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + "sum(premi) as premi_total_adisc,premi_base "
                + "from gl_cost_center e "
                + "left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.cc_code "
                + ") z on z.cc_code = e.cc_code "
                + "group by e.cc_code,premi_base order by e.cc_code";

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

    public DTOList REKAP_JENIS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.sumbis,a.pol_type_id,sum(a.premi) as premi, ");

        String sql = " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20)";

        if (getPeriodFrom() != null) {
            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (getYear() != null) {
            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa.addPar(year);
        }

        if (getStBranch() != null) {
            sql = sql + " and c.cc_code = ?";
            sqa.addPar(stBranch);
        }

        sqa.addQuery(sql + " ) as premi_base from ins_rekap_produksi a  ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stBranch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("region_id = ?");
            sqa.addPar(stRegion);
        }

        sql = "select e.pol_type_id,e.description,"
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + "coalesce(sum(premi),0) as premi_total_adisc,coalesce(premi_base,0) as premi_base "
                + "from ins_policy_types e "
                + "left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.pol_type_id "
                + ") z on z.pol_type_id = e.pol_type_id "
                + "group by e.pol_type_id,e.description,premi_base order by e.pol_type_id";

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

    public DTOList INWARD_REKAP() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_trx_type_id,a.mutation_date,substr(a.mutation_date::text,6,2) as bulan,"
                + "sum(checkreas(d.ar_trx_line_id in ('1','4','77','79','102'),d.amount)) as premi,"
                + "sum(checkreas(d.ar_trx_line_id in ('2','5','89','80'),d.amount)) as comm ");

        sqa.addQuery(" from ins_pol_inward a "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.ar_trx_type_id in (1,2,3,20)");
        sqa.addClause("a.approved_flag = 'Y'");

        if (periodFrom != null) {
            sqa.addClause("substr(a.mutation_date::text,6,2) between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("substr(a.mutation_date::text,1,4) = ?");
            sqa.addPar(year);
        }

        final String sql = "select b.ref2 as refx0,b.ref4 as refx1,"
                + "sum(getpremi2(a.ar_trx_type_id = 1,premi)) as premi,"
                + "sum(getpremi2(a.ar_trx_type_id = 2,premi)) as bpol,"
                + "sum(getpremi2(a.ar_trx_type_id = 3,premi)) as bmat,"
                + "sum(getpremi2(a.ar_trx_type_id = 20,comm)) as comm, "
                + "sum(premi) as fee "
                + "from gl_periode b left join ( " + sqa.getSQL() + " group by a.ar_trx_type_id,a.mutation_date "
                + "order by a.ar_trx_type_id,a.mutation_date "
                + ") a on a.bulan = b.ref2 "
                + "group by b.ref2,b.ref4,substr(a.mutation_date::text,6,2) "
                + "order by b.ref2";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    /**
     * @return the periodFrom
     */
    public String getPeriodFrom() {
        return periodFrom;
    }

    /**
     * @param periodFrom the periodFrom to set
     */
    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    /**
     * @return the periodTo
     */
    public String getPeriodTo() {
        return periodTo;
    }

    /**
     * @param periodTo the periodTo to set
     */
    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
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
     * @return the periodFromName
     */
    public String getPeriodFromName() {
        return periodFromName;
    }

    /**
     * @param periodFromName the periodFromName to set
     */
    public void setPeriodFromName(String periodFromName) {
        this.periodFromName = periodFromName;
    }

    /**
     * @return the periodToName
     */
    public String getPeriodToName() {
        return periodToName;
    }

    /**
     * @param periodToName the periodToName to set
     */
    public void setPeriodToName(String periodToName) {
        this.periodToName = periodToName;
    }

    public void copyWithFTP(String filePath, String uploadPath) {
        String host = "192.168.200.19";
        String user = "dinal";
        String pass = "askrida00";
        int port = 21;

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(filePath);

            String firstRemoteFile = uploadPath;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public DTOList REKAP_CABOUTLET() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.sumbis,a.cc_code,a.region_id,sum(a.premi) as premi, ");

        String sql = " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20)";

        if (getPeriodFrom() != null) {
            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (getYear() != null) {
            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa.addPar(year);
        }

        sqa.addQuery(sql + " ) as premi_base from ins_rekap_produksi a ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        sql = "select e.cc_code,e.region_code2,e.desc2 as description, "
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + "sum(premi) as premi_total_adisc,premi_base "
                + "from s_region e "
                + "left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.cc_code,a.region_id "
                + ") z on z.region_id = e.region_id "
                + " where premi <> 0 "
                + " group by e.cc_code,e.desc2,e.region_code2,premi_base "
                + " order by e.cc_code,e.region_code2 ";

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

    public DTOList EXCEL_REKAP_CABOUTLET() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.sumbis,a.cc_code,a.region_id,sum(a.premi) as premi ");

        sqa.addQuery(" from ins_rekap_produksi a ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = "select e.cc_code,e.region_code2,e.desc2 as description, "
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + "sum(premi) as premi_total_adisc "
                + "from s_region e "
                + "left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.cc_code,a.region_id "
                + ") z on z.region_id = e.region_id "
                + " group by e.cc_code,e.desc2,e.region_code2 "
                + " order by e.cc_code,e.region_code2 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) as inward ");

        sqa2.addQuery(" from ins_pol_inward c "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id ");

        sqa2.addClause("c.approved_flag = 'Y'");
        sqa2.addClause("c.ar_trx_type_id in (1,2,3,20)");

        if (getPeriodFrom() != null) {
            sqa2.addClause("substr(c.mutation_date::text,6,2) between ? and ?");
//            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa2.addPar(periodFrom);
            sqa2.addPar(periodTo);
        }

        if (getYear() != null) {
            sqa2.addClause("substr(c.mutation_date::text,1,4) = ?");
//            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa2.addPar(year);
        }

        String sql2 = sqa2.getSQL();

        final DTOList m = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPS", m);

        return l;
    }

    public void EXPORT_REKAP_CABOUTLET() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPS");

        HashDTO g = (HashDTO) list2.get(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(getMonthFromTitleDescription() + " sd " + getMonthTitleDescription());

            HSSFRow row2 = sheet.createRow(1);
            if (stPolicyTypeGroupID != null) {
                row2.createCell(0).setCellValue(getStPolicyTypeGroupDesc().toUpperCase() + " -- " + getStPolicyTypeDesc().toUpperCase());
            }

            //bikin header
            HSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("cabang");
            row0.createCell(1).setCellValue("outlet");
            row0.createCell(2).setCellValue("description");
            row0.createCell(3).setCellValue("umum");
            row0.createCell(4).setCellValue("pemda");
            row0.createCell(5).setCellValue("perusda");
            row0.createCell(6).setCellValue("bpd");
            row0.createCell(7).setCellValue("jumlah");

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("region_code2"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("description"));
            if (h.getFieldValueByFieldNameBD("nd_comm1") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("nd_comm1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm2") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nd_comm2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm3") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nd_comm3").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("nd_comm4") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nd_comm4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("premi_total_adisc") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_total_adisc").doubleValue());
            }

        }

        HSSFRow rowInward = sheet.createRow(list.size() + 5);
        rowInward.createCell(1).setCellValue("INWARD");
        if (g.getFieldValueByFieldNameBD("inward") != null) {
            rowInward.createCell(6).setCellValue(g.getFieldValueByFieldNameBD("inward").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_REKAP_CAB() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.sumbis,a.cc_code,sum(a.premi) as premi ");

        sqa.addQuery(" from ins_rekap_produksi a ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        String sql = "select z.cc_code,z.description,nd_comm1,nd_comm2,nd_comm3,nd_comm4,"
                + " premi_total_adisc,sum(amount) as amount from ( "
                + " select e.cc_code,e.description,"
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + "sum(premi) as premi_total_adisc "
                + " from gl_cost_center e "
                + " left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.cc_code "
                + " ) z on z.cc_code = e.cc_code "
                + " group by e.cc_code,e.description"
                + " ) z left join ins_statistic_product_cab a on a.cc_code = z.cc_code ";

        sql = sql + " where a.years = '" + getYear() + "'";

        sql = sql + " group by z.cc_code,z.description,nd_comm1,nd_comm2,nd_comm3,nd_comm4,premi_total_adisc "
                + " order by z.cc_code";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) as inward ");

        sqa2.addQuery(" from ins_pol_inward c "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id ");

        sqa2.addClause("c.approved_flag = 'Y'");
        sqa2.addClause("c.ar_trx_type_id in (1,2,3,20)");

        if (getPeriodFrom() != null) {
            sqa2.addClause("substr(c.mutation_date::text,6,2) between ? and ?");
//            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa2.addPar(periodFrom);
            sqa2.addPar(periodTo);
        }

        if (getYear() != null) {
            sqa2.addClause("substr(c.mutation_date::text,1,4) = ?");
//            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa2.addPar(year);
        }

        String sql2 = sqa2.getSQL();

        final DTOList m = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPS", m);

        return l;
    }

    public void EXPORT_REKAP_CAB() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPS");

        HashDTO g = (HashDTO) list2.get(0);

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(getMonthFromTitleDescription() + " sd " + getMonthTitleDescription());

            HSSFRow row2 = sheet.createRow(1);
            if (stPolicyTypeGroupID != null) {
                row2.createCell(0).setCellValue(getStPolicyTypeGroupDesc().toUpperCase() + " -- " + getStPolicyTypeDesc().toUpperCase());
            }

            //bikin header
            HSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("cabang");
            row0.createCell(2).setCellValue("umum");
            row0.createCell(3).setCellValue("pemda");
            row0.createCell(4).setCellValue("perusda");
            row0.createCell(5).setCellValue("bpd");
            row0.createCell(6).setCellValue("jumlah");
//            row0.createCell(7).setCellValue("target");

//            final Date perStart = getPolicyDateFrom();
//            final Date perEnd = getPolicyDateTo();
//
//            DateTime startDate = new DateTime(perStart);
//            DateTime endDate = new DateTime(perEnd);
//            Months m = Months.monthsBetween(startDate, endDate);
//            int mon = m.getMonths() + 1;
//
//            BigDecimal a = new BigDecimal(12000);
//            BigDecimal b = new BigDecimal(1000);
//            String month = String.valueOf(mon);
//            BigDecimal c = new BigDecimal(month);
//            BigDecimal target = h.getFieldValueByFieldNameBD("amount");
//            target = BDUtil.roundUp(BDUtil.div(target, a));
//            target = BDUtil.roundUp(BDUtil.mul(target, b));

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 4);
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
//            if (h.getFieldValueByFieldNameBD("amount") != null) {
//                row.createCell(7).setCellValue(BDUtil.mul(target, c).doubleValue());
//            }
        }

        HSSFRow rowInward = sheet.createRow(list.size() + 5);
        rowInward.createCell(1).setCellValue("INWARD");
        if (g.getFieldValueByFieldNameBD("inward") != null) {
            rowInward.createCell(6).setCellValue(g.getFieldValueByFieldNameBD("inward").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_REKAP_JENIS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("	a.sumbis,a.pol_type_id,sum(a.premi) as premi ");

        sqa.addQuery(" from ins_rekap_produksi a ");

        if (periodFrom != null) {
            sqa.addClause("bulan between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (year != null) {
            sqa.addClause("tahun = ?");
            sqa.addPar(year);
        }

        if (stBranch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stRegion != null) {
            sqa.addClause("region_id = ?");
            sqa.addPar(stRegion);
        }

        String sql = " select e.pol_type_id,e.description,"
                + "sum(getkoas(sumbis='1',premi)) as nd_comm1,"
                + "sum(getkoas(sumbis='2',premi)) as nd_comm2,"
                + "sum(getkoas(sumbis='3',premi)) as nd_comm3,"
                + "sum(getkoas(sumbis='4',premi)) as nd_comm4,"
                + " sum(premi) as premi_total_adisc "
                + " from ins_policy_types e "
                + " left join ( "
                + sqa.getSQL() + " group by a.sumbis,a.pol_type_id "
                + " ) z on z.pol_type_id = e.pol_type_id "
                + " group by e.pol_type_id,e.description "
                + " order by e.pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        if (getStBranch() == null) {
            final SQLAssembler sqa2 = new SQLAssembler();

            sqa2.addSelect(" sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) as inward ");

            sqa2.addQuery(" from ins_pol_inward c "
                    + " inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id ");

            sqa2.addClause("c.approved_flag = 'Y'");
            sqa2.addClause("c.ar_trx_type_id in (1,2,3,20)");

            if (getPeriodFrom() != null) {
            sqa2.addClause("substr(c.mutation_date::text,6,2) between ? and ?");
//            sql = sql + " and substr(c.mutation_date::text,6,2) between ? and ?";
            sqa2.addPar(periodFrom);
            sqa2.addPar(periodTo);
        }

        if (getYear() != null) {
            sqa2.addClause("substr(c.mutation_date::text,1,4) = ?");
//            sql = sql + " and substr(c.mutation_date::text,1,4) = ?";
            sqa2.addPar(year);
        }

            String sql2 = sqa2.getSQL();

            final DTOList m = ListUtil.getDTOListFromQuery(
                    sql2,
                    sqa2.getPar(),
                    HashDTO.class);

            SessionManager.getInstance().getRequest().setAttribute("RPS", m);
        }

        return l;
    }

    public void EXPORT_REKAP_JENIS() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        final DTOList list2;
        HashDTO g = null;

        if (getStBranch() == null) {
            list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPS");
            g = (HashDTO) list2.get(0);
        }

        String jenpol = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue(getMonthFromTitleDescription() + " sd " + getMonthTitleDescription());

            HSSFRow row2 = sheet.createRow(1);
            if (getStPolicyTypeGroupID() != null) {
                row2.createCell(0).setCellValue(getStBranch().toUpperCase());
            }

            HSSFRow row0 = sheet.createRow(3);
            row0.createCell(0).setCellValue("kode");
            row0.createCell(1).setCellValue("jenis");
            row0.createCell(2).setCellValue("umum");
            row0.createCell(3).setCellValue("pemda");
            row0.createCell(4).setCellValue("perusda");
            row0.createCell(5).setCellValue("bpd");
            row0.createCell(6).setCellValue("jumlah");
//            row0.createCell(7).setCellValue("target");

//            final Date perStart = getPolicyDateFrom();
//            final Date perEnd = getPolicyDateTo();
//
//            DateTime startDate = new DateTime(perStart);
//            DateTime endDate = new DateTime(perEnd);
//            Months m = Months.monthsBetween(startDate, endDate);
//            int mon = m.getMonths() + 1;
//
//            BigDecimal a = new BigDecimal(12000);
//            BigDecimal b = new BigDecimal(1000);
//            String month = String.valueOf(mon);
//            BigDecimal c = new BigDecimal(month);
//            BigDecimal target = h.getFieldValueByFieldNameBD("amount");
//            target = BDUtil.roundUp(BDUtil.div(target, a));
//            target = BDUtil.roundUp(BDUtil.mul(target, b));

            jenpol = String.valueOf(h.getFieldValueByFieldNameBD("pol_type_id"));
            if (jenpol.length() == 1) {
                jenpol = "0" + jenpol;
            } else {
                jenpol = jenpol;
            }

            //bikin isi cell
            HSSFRow row = sheet.createRow(i + 4);
            row.createCell(0).setCellValue(jenpol);
            row.createCell(1).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
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
//            if (h.getFieldValueByFieldNameBD("amount") != null) {
//                row.createCell(7).setCellValue(BDUtil.mul(target, c).doubleValue());
//            }
        }

        if (getStBranch() == null) {
            HSSFRow rowInward = sheet.createRow(list.size() + 5);
            rowInward.createCell(1).setCellValue("INWARD");
            if (g.getFieldValueByFieldNameBD("inward") != null) {
                rowInward.createCell(6).setCellValue(g.getFieldValueByFieldNameBD("inward").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

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
     * @return the stBranch
     */
    public String getStBranch() {
        return stBranch;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    /**
     * @return the stBranchDesc
     */
    public String getStBranchDesc() {
        return stBranchDesc;
    }

    /**
     * @param stBranchDesc the stBranchDesc to set
     */
    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
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
     * @return the stPolicyTypeDesc
     */
    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    /**
     * @param stPolicyTypeDesc the stPolicyTypeDesc to set
     */
    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    /**
     * @return the stFileName
     */
    public String getStFileName() {
        return stFileName;
    }

    /**
     * @param stFileName the stFileName to set
     */
    public void setStFileName(String stFileName) {
        this.stFileName = stFileName;
    }

    public void clickPrintExcel() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcel();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        exportExcel();
    }

    public void exportExcel() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING_REKAP", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_PRINTING_REKAP", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcel() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING_REKAP", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY_EXCEL");


        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return null;
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public void clickPrint() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        loadFormList();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final ArrayList plist = new ArrayList();

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

    public String getPeriodTitleDescription() throws Exception {

        if (year != null) {

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(periodTo, year);

            Date d = pd.getDtEndDate();

            return "PER " + DateUtil.getDateStr(d, "d ^^ yyyy");
        }

        return "???";
    }

    public String getMonthFromTitleDescription() throws Exception {

        PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodFrom, year);

        Date d1 = pd1.getDtEndDate();

        return DateUtil.getDateStr(d1, "^^ yyyy");
    }

    public String getMonthTitleDescription() throws Exception {

        PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodTo, year);

        Date d1 = pd1.getDtEndDate();

        return DateUtil.getDateStr(d1, "^^ yyyy");
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

    public void onChangePolicyTypeGroup() {
    }

    public void onChangeBranchGroup() {
    }
}

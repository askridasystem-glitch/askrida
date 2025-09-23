/***********************************************************************
 * Module:  com.webfin.pks.form.PKSProductionReportForm
 * Author:  Denny Mahendra
 * Created: Aug 15, 2006 11:17:20 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.pks.form;

import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.crux.util.LogManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.Method;

import java.io.*;

import com.webfin.pks.model.PerjanjianKerjasamaView;
import java.sql.PreparedStatement;
import javax.ejb.SessionContext;
import javax.servlet.ServletOutputStream;

public class PKSProductionReportForm extends Form {

    private SessionContext ctx;
    private Date periodFrom;
    private Date periodTo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date remarkDateFrom;
    private Date remarkDateTo;
    private Date receiveDateFrom;
    private Date receiveDateTo;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeGroupDesc;
    private String stPolicyTypeID;
    private String stPolicyTypeDesc;
    private String stEntityID;
    private String stEntityName;
    private String stPrintForm;
    private String stLang;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchName = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchDesc;
    private boolean enableRiskFilter;
    private boolean enableSelectForm = true;
    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String stReportTitle;
    private String stReport;
    private String stReportDesc;
    private String stReportType;
    private DTOList list;
    private String stCustCategory1;
    private static HashSet formList = null;
    private final static transient LogManager logger = LogManager.getInstance(PKSProductionReportForm.class);

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public Date getRemarkDateFrom() {
        return remarkDateFrom;
    }

    public void setRemarkDateFrom(Date remarkDateFrom) {
        this.remarkDateFrom = remarkDateFrom;
    }

    public Date getRemarkDateTo() {
        return remarkDateTo;
    }

    public void setRemarkDateTo(Date remarkDateTo) {
        this.remarkDateTo = remarkDateTo;
    }

    public Date getReceiveDateFrom() {
        return receiveDateFrom;
    }

    public void setReceiveDateFrom(Date receiveDateFrom) {
        this.receiveDateFrom = receiveDateFrom;
    }

    public Date getReceiveDateTo() {
        return receiveDateTo;
    }

    public void setReceiveDateTo(Date receiveDateTo) {
        this.receiveDateTo = receiveDateTo;
    }

    public DTOList PKS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(" from perjanjian_kerjasama a "
                + "  left join ent_master b on b.ent_id = a.entity_id ");

        if (receiveDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receive_date) >= ?");
            sqa.addPar(receiveDateFrom);
        }

        if (receiveDateTo != null) {
            sqa.addClause("date_trunc('day',a.receive_date) <= ?");
            sqa.addPar(receiveDateTo);
        }

        if (remarkDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(remarkDateFrom);
        }

        if (remarkDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(remarkDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("b.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        final String sql = sqa.getSQL() + " order by a.pks_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                PerjanjianKerjasamaView.class);

        HashMap map1 = l.getMapOf(
                new DTOList.KeyBuilder() {

                    public String buildKey(DTO x) {
                        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) x;

                        return pol.getStCostCenterCode() + "/" + pol.getStBusinessSourceCode();
                    }
                });

        l.setAttribute("AMOUNT_MAP", map1);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/pks/prodrpt")).list();

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

        plist.add(stReport);

        String urx = null;

        //logger.logDebug("printPolicy: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/pks/prodrpt/" + s + ".fop?xlang=" + stLang;
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
            return null;
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
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

    public String getStPolicyTypeGroupID() {
        return stPolicyTypeGroupID;
    }

    public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
        this.stPolicyTypeGroupID = stPolicyTypeGroupID;
    }

    public String getStPolicyTypeGroupDesc() {
        return stPolicyTypeGroupDesc;
    }

    public void setStPolicyTypeGroupDesc(String stPolicyTypeGroupDesc) {
        this.stPolicyTypeGroupDesc = stPolicyTypeGroupDesc;
    }

    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStEntityName() {
        return stEntityName;
    }

    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }

    public String getStPrintForm() {
        return stPrintForm;
    }

    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public String getStBranchName() {
        return stBranchName;
    }

    public void setStBranchName(String stBranchName) {
        this.stBranchName = stBranchName;
    }

    public String getStBranchDesc() {
        return stBranchDesc;
    }

    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
    }

    public boolean isEnableRiskFilter() {
        return enableRiskFilter;
    }

    public void setEnableRiskFilter(boolean enableRiskFilter) {
        this.enableRiskFilter = enableRiskFilter;
    }

    public boolean isEnableSelectForm() {
        return enableSelectForm;
    }

    public void setEnableSelectForm(boolean enableSelectForm) {
        this.enableSelectForm = enableSelectForm;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public HashMap getRefPropMap() {
        return refPropMap;
    }

    public void setRefPropMap(HashMap refPropMap) {
        this.refPropMap = refPropMap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStReportTitle() {
        return stReportTitle;
    }

    public void setStReportTitle(String stReportTitle) {
        this.stReportTitle = stReportTitle;
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

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public DTOList getList() {
        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void go() {
        stPrintForm = (String) getAttribute("rpt");
        enableSelectForm = false;

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_PRINTING");
    }

    public void chgform() {
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public void onChangePolicyTypeGroup() {
    }

    public void onChangeReport() {
    }

    /**
     * @return the stCustCategory1
     */
    public String getStCustCategory1() {
        return stCustCategory1;
    }

    /**
     * @param stCustCategory1 the stCustCategory1 to set
     */
    public void setStCustCategory1(String stCustCategory1) {
        this.stCustCategory1 = stCustCategory1;
    }

    public void EXCEL_PKS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" e.description as koda,a.cust_name as bank,c.description as cob,a.pol_no as no_askrida,a.bank_no as no_bank,"
                + "a.policy_date as tgl_ttd,a.receive_date as tgl_terima,a.period_start as tgl_awal,a.period_end as tgl_awal,d.vs_description as sumbis ");

        sqa.addQuery(" from perjanjian_kerjasama a "
                + "  inner join ent_master b on b.ent_id = a.entity_id "
                + "  inner join ins_policy_types c on c.pol_type_id = a.pol_type_id "
                + "  inner join s_valueset d on d.vs_code = b.category1 and d.vs_group = 'ASK_BUS_SOURCE' "
                + "  inner join gl_cost_center e on e.cc_code = a.cc_code ");

        if (receiveDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receive_date) >= '" + receiveDateFrom + "'");
//            sqa.addPar(receiveDateFrom);
        }

        if (receiveDateTo != null) {
            sqa.addClause("date_trunc('day',a.receive_date) <= '" + receiveDateTo + "'");
//            sqa.addPar(receiveDateTo);
        }

        if (remarkDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= '" + remarkDateFrom + "'");
//            sqa.addPar(remarkDateFrom);
        }

        if (remarkDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= '" + remarkDateTo + "'");
//            sqa.addPar(remarkDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= '" + periodFrom + "'");
//            sqa.addPar(periodFrom);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= '" + periodTo + "'");
//            sqa.addPar(periodTo);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '" + stPolicyTypeGroupID + "'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '" + stPolicyTypeID + "'");
//            sqa.addPar(stPolicyTypeID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
//            sqa.addPar(stBranch);
        }

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '" + stEntityID + "'");
//            sqa.addPar(stEntityID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("b.category1 = '" + stCustCategory1 + "'");
//            sqa.addPar(stCustCategory1);
        }

        String sql = sqa.getSQL() + " order by a.pks_id";

        SQLUtil S = new SQLUtil();

        String nama_file = "pks_" + System.currentTimeMillis() + ".csv";

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

    public void clickPrintExcel() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcel();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

//        exportExcel();
    }

//    public void exportExcel() throws Exception {
//        ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING", stReport);
//
//        refPropMap = Tools.getPropMap(ref1);
//
//        final String exportID = (String) refPropMap.get("EXPORT");
//
//        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_PRINTING", stReport), getStLang());
//
//        setStFileName(fileName);
//
//        final Method m = this.getClass().getMethod(exportID, null);
//
//        m.invoke(this, null);
//    }
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

    public DTOList defaultQuery() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.cc_code as koda,a.cust_name as bank,a.pol_type_id as jenid,a.pol_no as no_askrida,a.bank_no as no_bank,"
                + "a.policy_date as tgl_ttd,a.receive_date as tgl_terima,a.period_start as tgl_awal,a.period_end as tgl_awal,b.category1 as sumbis ");

        sqa.addQuery(" from perjanjian_kerjasama a "
                + "  left join ent_master b on b.ent_id = a.entity_id ");

        if (receiveDateFrom != null) {
            sqa.addClause("date_trunc('day',a.receive_date) >= ?");
            sqa.addPar(receiveDateFrom);
        }

        if (receiveDateTo != null) {
            sqa.addClause("date_trunc('day',a.receive_date) <= ?");
            sqa.addPar(receiveDateTo);
        }

        if (remarkDateFrom != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(remarkDateFrom);
        }

        if (remarkDateTo != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(remarkDateTo);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(periodFrom);
        }

        if (periodFrom != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(periodTo);
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

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stCustCategory1 != null) {
            sqa.addClause("b.category1 = ?");
            sqa.addPar(stCustCategory1);
        }

        String sql = sqa.getSQL() + " order by a.pks_id";

        SQLUtil S = new SQLUtil();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }
}

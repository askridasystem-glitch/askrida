/***********************************************************************
 * Module:  com.webfin.ar.forms.PemasaranRPTForm
 * Author:  Denny Mahendra
 * Created: Mar 7, 2007 10:55:04 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.lang.LanguageManager;
import com.crux.web.controller.SessionManager;
import com.crux.lov.LOVManager;
import com.crux.util.fop.FOPTableSource;
import java.util.Date;
import com.crux.util.*;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;

import com.crux.web.form.Form;
import com.webfin.ar.model.BiayaPemasaranView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PemasaranRPTForm extends Form {

    private String stPolicyNo;
    private Date policyDateFrom;
    private Date policyDateTo;
    private Date DLADateFrom;
    private Date DLADateTo;
    private String stEntityName;
    private String stCustomer;
    private String stAccount;
    private String stTransactionType;
    private String stEntityDesc;
    private String stCustomerDesc;
    private String stReportType;
    private String stPrintForm;
    private String stReport;
    private String stCompanyID;
    private String stCompanyName;
    private String stPolicyTypeID;
    private String stPolicyTypeName;
    private String ref1;
    private String status;
    private String stLang;
    private String stReportTitle;
    private boolean enableSelectForm = true;
    private HashMap refPropMap;
    private String stIndex = "N";
    private static HashSet formList = null;
    private String stFltTreatyType;
    private String stFileName;
    private String stApproved;
    private Date approvedDateFrom;
    private Date approvedDateTo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stRegion = SessionManager.getInstance().getSession().getStRegion();
    private String stRegionDesc = SessionManager.getInstance().getSession().getRegionDesc();
    private String stRegionName;
    private String period;
    private String periodName;
    private String year;

    public String getTrxtype() {
        return stTransactionType;
    }

    public void setTrxtype(String stTransactionType) {
        this.stTransactionType = stTransactionType;
    }

    public String getAccount() {
        return stAccount;
    }

    public void setAccount(String stAccount) {
        this.stAccount = stAccount;
    }

    public String getCustomer() {
        return stCustomer;
    }

    public void setCustomer(String stCustomer) {
        this.stCustomer = stCustomer;
    }

    public String getCustomer_desc() {
        return stCustomerDesc;
    }

    public void setCustomer_desc(String stCustomerDesc) {
        this.stCustomerDesc = stCustomerDesc;
    }

    public String getEntity() {
        return stEntityName;
    }

    public void setEntity(String stEntityName) {
        this.stEntityName = stEntityName;
    }

    public String getEntity_desc() {
        return stEntityDesc;
    }

    public void setEntity_desc(String stEntityDesc) {
        this.stEntityDesc = stEntityDesc;
    }

    public String getPolicyno() {
        return stPolicyNo;
    }

    public void setPolicyno(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    public void print() throws Exception {
        loadReport();
        redirect("/pages/ar/report/rptArAPDetail.fop");
    }

    private void loadReport() throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "a.attr_pol_no, b.pol_no,d.ent_name as cust_name, c.ent_name, a.invoice_type, a.invoice_date, a.due_date, a.amount, e.accountno, e.description, \n"
                + "f.credit as amtcr, \n"
                + "f.debit as amtdb");
        sqa.addQuery(
                "   from ar_invoice a\n"
                + "      inner join gl_je_detail f on f.ref_trx = 'INV' and f.ref_trx_no::bigint = a.ar_invoice_id\n"
                + "      left join ins_policy b on b.pol_id=a.attr_pol_id\n"
                + "      left join ent_master d on d.ent_id=b.entity_id\n"
                + "      left join ent_master c on c.ent_id = a.ent_id\n"
                + "      left join gl_accounts e on e.account_id = f.accountid");

        if (stPolicyNo != null) {
            sqa.addClause("a.attr_pol_no like ?");
            sqa.addPar(stPolicyNo);
        }

        if (stTransactionType != null) {
            sqa.addClause("a.ar_trx_type_id=?");
            sqa.addPar(stTransactionType);
        }

        if (stAccount != null) {
            sqa.addClause("e.accountno like ?");
            sqa.addPar(stAccount);
        }

        if (stEntityName != null) {
            sqa.addClause("a.ent_id=?");
            sqa.addPar(stEntityName);
        }

        if (stCustomer != null) {
            sqa.addClause("b.entity_id=?");
            sqa.addPar(stCustomer);
        }

        if (policyDateFrom != null) {
            sqa.addClause("b.policy_date>=?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("b.policy_date<?");
            sqa.addPar(policyDateTo);
        }

        sqa.addOrder("a.invoice_date, f.trx_id");


        final DTOList r = ListUtil.getDTOListFromQuery(sqa.getSQL(), sqa.getPar(), HashDTO.class);

        HashDTO tot = new HashDTO();

        tot.setFieldValueByFieldName("no", "");
        tot.setFieldValueByFieldName("amount", r.getTotal("amount"));
        tot.setFieldValueByFieldName("amtcr", r.getTotal("amtcr"));
        tot.setFieldValueByFieldName("amtdb", r.getTotal("amtdb"));
        tot.setFieldValueByFieldName("ent_name", "TOTAL");

        r.add(tot);

        final String[] columnTitles = {
            "No",
            "No. Polis",
            "Nama Tertanggung",
            "Penerima/Pembayar",
            "Tipe",
            "Tanggal",
            "Jatuh Tempo",
            "Jumlah",
            "Rekening",
            "Keterangan",
            "Debit",
            "Kredit",};


        final FOPTableSource fopTableSource = new FOPTableSource(
                12,
                new int[]{4, 10, 15, 15, 3, 6, 6, 10, 9, 15, 10, 10},
                27,
                "cm") {

            public int getRowCount() {
                return r.size();
            }

            public Object getColumnValue(int rowNo, int columnNo) {
                HashDTO h = (HashDTO) r.get(rowNo);
                switch (columnNo) {
                    case 0:
                        return h.getFieldValueByFieldNameST("no") == null ? String.valueOf(rowNo + 1) : "";
                    case 1:
                        return h.getFieldValueByFieldNameST("pol_no");
                    case 2:
                        return JSPUtil.printX(h.getFieldValueByFieldNameST("cust_name"));
                    case 3:
                        return JSPUtil.printX(h.getFieldValueByFieldNameST("ent_name"));
                    case 4:
                        return h.getFieldValueByFieldNameST("invoice_type");
                    case 5:
                        return h.getFieldValueByFieldNameDT("invoice_date");
                    case 6:
                        return h.getFieldValueByFieldNameDT("due_date");
                    case 7:
                        return h.getFieldValueByFieldNameBD("amount");
                    case 8:
                        return h.getFieldValueByFieldNameST("accountno");
                    case 9:
                        return JSPUtil.printX(h.getFieldValueByFieldNameST("description"));
                    case 10:
                        return h.getFieldValueByFieldNameBD("amtcr");
                    case 11:
                        return h.getFieldValueByFieldNameBD("amtdb");
                }
                return "?";
            }

            public String getColumnHeader(int columnNo) {
                return columnTitles[columnNo];
            }

            public String getColumnAlign(int rowNo, int columnNo) {
                switch (columnNo) {
                    case 7:
                    case 10:
                    case 11:
                        return "end";
                }
                ;

                return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        SessionManager.getInstance().getRequest().setAttribute("RPT", fopTableSource);
        SessionManager.getInstance().getRequest().setAttribute("FRX", this);

    }

    public void chgform() {
    }

    public void onChangeReport() {
    }

    public String getStReportTitle() {
        return stReportTitle;
    }

    public void setStReportTitle(String stReportTitle) {
        this.stReportTitle = stReportTitle;
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

    public String getStReport() {
        return stReport;
    }

    public void setStReport(String stReport) {
        this.stReport = stReport;
    }

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public String getStPolicyTypeName() {
        return stPolicyTypeName;
    }

    public void setStPolicyTypeName(String stPolicyTypeName) {
        this.stPolicyTypeName = stPolicyTypeName;
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

    private DTOList loadList() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_BIAPEM", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY");


        status = (String) refPropMap.get("STATUS");

        if (queryID != null) {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        } else {
            return null;
        }
    }

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/ar/report")).list();

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

        plist.add(stReport + "_" + stTransactionType);

        plist.add(stReport + "_" + stPolicyTypeID);

        plist.add(stReport + "_" + stIndex);

        plist.add(stReport);

        String urx = null;

        //logger.logDebug("printPolicy: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/ar/report/" + s + ".fop?xlang=" + stLang;
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        super.redirect(urx);

    }

    public boolean isEnableSelectForm() {
        return enableSelectForm;
    }

    public void setEnableSelectForm(boolean enableSelectForm) {
        this.enableSelectForm = enableSelectForm;
    }

    public void go() {
        stPrintForm = (String) getAttribute("rpt");
        enableSelectForm = false;

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_BIAPEM");
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public String getStIndex() {
        return stIndex;
    }

    public void setStIndex(String stIndex) {
        this.stIndex = stIndex;
    }

    public String getStFileName() {
        return stFileName;
    }

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
        ref1 = LOVManager.getInstance().getRef1("PROD_BIAPEM", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_BIAPEM", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcel() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_BIAPEM", stReport);

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

    public DTOList BIAPEM() throws Exception {

        String fld = "bal";
        String func = "sum";
        long periodFrom = DateUtil.getMonthDigit(policyDateFrom);
        long periodTo = DateUtil.getMonthDigit(policyDateTo);
        int tahun = Integer.parseInt(DateUtil.getYear(policyDateFrom)) - 1;
        String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.jenis_pemasaranid,d.koda,sum(b.nilai) as biaya ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");
        sqa.addClause("a.fileapp is null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.entry_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.entry_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.koda = ?");
            sqa.addPar(stBranch);
        }

        String sql = "select a.jenis_pemasaranid as accountid,c.description as accountno,sum(a.biaya) as biaya,"
                + "coalesce(" + func + "(" + plist + "),0) as total_biaya "
                + "from (" + sqa.getSQL() + " group by b.jenis_pemasaranid,d.koda order by b.jenis_pemasaranid ) a "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = a.jenis_pemasaranid::int "
                + "left join gl_neraca_total_det d on d.gl_ins_id = c.gl_ins_id and d.tahun = " + tahun;

        if (stBranch != null) {
            sql = sql + " and d.cc_code = '" + stBranch + "' ";
        }

        sql = sql + " group by a.jenis_pemasaranid,c.description order by a.jenis_pemasaranid ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqanon = new SQLAssembler();

        sqanon.addSelect(" b.jenis_pemasaranid,sum(b.nilai) as biaya ");

        sqanon.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqanon.addClause("a.validasi_f = 'Y'");
        sqanon.addClause("a.status1 = 'Y'");
        sqanon.addClause("a.status2 = 'Y'");
        sqanon.addClause("a.status3 = 'Y'");
        sqanon.addClause("a.status4 = 'Y'");
        sqanon.addClause("a.fileapp is not null");

        if (policyDateFrom != null) {
            sqanon.addClause("date_trunc('day',a.entry_date) >= ?");
            sqanon.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqanon.addClause("date_trunc('day',a.entry_date) <= ?");
            sqanon.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqanon.addClause("d.koda = ?");
            sqanon.addPar(stBranch);
        }

        String sqlnon = "select a.jenis_pemasaranid as accountid,c.description as accountno,sum(a.biaya) as biaya,"
                + "coalesce(" + func + "(" + plist + "),0) as total_biaya "
                + "from (" + sqanon.getSQL() + " group by b.jenis_pemasaranid order by b.jenis_pemasaranid ) a "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = a.jenis_pemasaranid::int "
                + "left join gl_neraca_total_det d on d.gl_ins_id = c.gl_ins_id and d.tahun = " + tahun;

        if (stBranch != null) {
            sqlnon = sqlnon + " and d.cc_code = '" + stBranch + "' ";
        }

        sqlnon = sqlnon + " group by a.jenis_pemasaranid,c.description order by a.jenis_pemasaranid ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sqlnon,
                sqanon.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;

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

    public String getStFltTreatyType() {
        return stFltTreatyType;
    }

    /**
     * @return the stApproved
     */
    public String getStApproved() {
        return stApproved;
    }

    /**
     * @param stApproved the stApproved to set
     */
    public void setStApproved(String stApproved) {
        this.stApproved = stApproved;
    }

    /**
     * @return the approvedDateFrom
     */
    public Date getApprovedDateFrom() {
        return approvedDateFrom;
    }

    /**
     * @param approvedDateFrom the approvedDateFrom to set
     */
    public void setApprovedDateFrom(Date approvedDateFrom) {
        this.approvedDateFrom = approvedDateFrom;
    }

    /**
     * @return the approvedDateTo
     */
    public Date getApprovedDateTo() {
        return approvedDateTo;
    }

    /**
     * @param approvedDateTo the approvedDateTo to set
     */
    public void setApprovedDateTo(Date approvedDateTo) {
        this.approvedDateTo = approvedDateTo;
    }

    public InsurancePolicyTypeView getPolicyType(String stPolicyTypeID) {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
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

    public void onChangeBranchGroup() {
    }

    public DTOList BIAPEMDET() throws Exception {
        if (stBranch == null) {
            throw new RuntimeException("Cabang harus dipilih!");
        }

        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.entry_date,b.accountid,b.accountno,b.description as accountdesc,"
//                + "b.nilai as biaya,a.no_bukti,a.no_bukti_bayar,a.fileapp ");

        sqa.addSelect(" a.entry_date,d.description as cc_code,b.jenis_pemasaranid as accountid,c.description as accountno,b.description as accountdesc,"
                + "b.nilai as biaya,b.excess_ratepajak,b.excess_amount,a.no_bukti,a.no_bukti_bayar,a.fileapp ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = b.jenis_pemasaranid::int "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");
        sqa.addClause("a.fileapp is null");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.entry_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.entry_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.koda = ?");
            sqa.addPar(stBranch);
        }

        String sql = sqa.getSQL() + " order by a.cc_code,a.entry_date,b.jenis_pemasaranid ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqanon = new SQLAssembler();

//        sqa.addSelect(" a.entry_date,b.accountid,b.accountno,b.description as accountdesc,"
//                + "b.nilai as biaya,a.no_bukti,a.no_bukti_bayar,a.fileapp ");

        sqanon.addSelect(" a.entry_date,d.description as cc_code,b.jenis_pemasaranid as accountid,c.description as accountno,b.description as accountdesc,"
                + "b.nilai as biaya,b.excess_ratepajak,b.excess_amount,a.no_bukti,a.no_bukti_bayar,a.fileapp ");

        sqanon.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = b.jenis_pemasaranid::int "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqanon.addClause("a.validasi_f = 'Y'");
        sqanon.addClause("a.status1 = 'Y'");
        sqanon.addClause("a.status2 = 'Y'");
        sqanon.addClause("a.status3 = 'Y'");
        sqanon.addClause("a.status4 = 'Y'");
        sqanon.addClause("a.fileapp is not null");

        if (policyDateFrom != null) {
            sqanon.addClause("date_trunc('day',a.entry_date) >= ?");
            sqanon.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqanon.addClause("date_trunc('day',a.entry_date) <= ?");
            sqanon.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqanon.addClause("d.koda = ?");
            sqanon.addPar(stBranch);
        }

        String sqlnon = sqanon.getSQL() + " order by a.cc_code,a.entry_date,b.jenis_pemasaranid ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sqlnon,
                sqanon.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        int bulanNow = Integer.parseInt(DateUtil.getMonth2Digit(policyDateFrom));
        int tahunNow = Integer.parseInt(DateUtil.getYear(policyDateFrom));
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
            beforeMonth = "12";
            bulanCode = 12;
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
            if (bulanCode < 10) {
                beforeMonth = "0" + bulanCode;
            } else {
                beforeMonth = String.valueOf(bulanCode);
            }

            tahunCode = tahunNow;
        }

        final SQLAssembler sqatarget = new SQLAssembler();

        sqatarget.addSelect(" b.category1 as bus_source,c.koda,c.description as cabang,"
                + "sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqatarget.addQuery(" from ins_policies a "
                + "inner join ent_master b on b.ent_id = a.entity_id "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

        sqatarget.addClause("a.active_flag='Y'");
        sqatarget.addClause("a.effective_flag = 'Y'");
        sqatarget.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqatarget.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqatarget.addClause("a.pol_type_id in (59,87,88)");

        if (policyDateFrom != null) {
            sqatarget.addClause("date_trunc('day',a.approved_date) >= ?");
            sqatarget.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqatarget.addClause("date_trunc('day',a.approved_date) <= ?");
            sqatarget.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqatarget.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sqltarget = "select a.koda as cc_code,a.cabang as accountdesc,b.bln" + bulanCode + " as biaya,a.total as total_biaya "
                + "from ( select a.koda,a.cabang,"
                + "coalesce(sum(a.premi-a.diskon),0) as total from ( "
                + sqatarget.getSQL() + " group by b.category1,c.koda,c.description order by c.koda "
                + ") a group by a.koda,a.cabang order by a.koda ) a "
                + "left join ins_statistic_target_perbulan b on b.cc_code = a.koda "
                + "and b.jenis = 'kredit' and b.years = '" + tahunCode + "'";

        final DTOList l3 = ListUtil.getDTOListFromQuery(
                sqltarget,
                sqatarget.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", l3);

        return l;

    }

    public DTOList BIAPEM_TARGET() throws Exception {

        int bulanNow = Integer.parseInt(period);
        int tahunNow = Integer.parseInt(year);
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
            beforeMonth = "12";
            bulanCode = 12;
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
            if (bulanCode < 10) {
                beforeMonth = "0" + bulanCode;
            } else {
                beforeMonth = String.valueOf(bulanCode);
            }

            tahunCode = tahunNow;
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.category1 as bus_source,c.koda,c.description as cabang,"
                + "sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqa.addQuery(" from ins_policies a "
                + "inner join ent_master b on b.ent_id = a.entity_id "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + "inner join s_region e on e.region_id = a.region_id ");

        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.pol_type_id in (59,87,88)");
//        sqa.addClause("e.region_code <> '99'");

        if (period != null) {
            sqa.addClause("substr(a.approved_date::text,6,2) = '" + beforeMonth + "'");
        }

        if (year != null) {
            sqa.addClause("substr(a.approved_date::text,1,4) = '" + tahunCode + "'");
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sql = "select a.koda as cc_code,a.cabang as accountdesc,b.bln" + bulanCode + " as biaya,a.total as total_biaya "
                + "from ( select a.koda,a.cabang,"
                + "coalesce(sum(a.premi-a.diskon),0) as total from ( "
                + sqa.getSQL() + " group by b.category1,c.koda,c.description order by c.koda "
                + ") a group by a.koda,a.cabang order by a.koda ) a "
                + "left join ins_statistic_target_perbulan b on b.cc_code = a.koda "
                + "and b.jenis = 'kredit' and b.years = '" + tahunCode + "'";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
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
     * @return the periodName
     */
    public String getPeriodName() {
        return periodName;
    }

    /**
     * @param periodName the periodName to set
     */
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public DTOList BIAPEM_REALISASI() throws Exception {

        int monthInt = Integer.parseInt(period);
        String monthStr = null;

        if (monthInt < 10) {
            monthStr = "0" + monthInt;
        } else {
            monthStr = String.valueOf(monthInt);
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.months,c.koda,c.description as cabang,(case when a.fileapp is not null then 'EXC' else 'INC' end) as pms,"
                + "round(a.biaya,2) as biaya,sum(a.total_biaya) as total_biaya ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");
        sqa.addClause("a.fileapp is null");

        if (period != null) {
            sqa.addClause("a.months = '" + monthStr + "'");
        }

        if (year != null) {
            sqa.addClause("a.years = '" + year + "'");
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sql = "select a.pms,a.months,a.koda as cc_code,a.cabang as accountdesc,sum(a.biaya) as biaya,sum(a.total_biaya) as total_biaya from ( "
                + sqa.getSQL() + " group by 1,2,3,4,5 "
                + ") a group by a.pms,a.months,a.koda,a.cabang order by a.pms,a.months,a.koda ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        final SQLAssembler sqanon = new SQLAssembler();

        sqanon.addSelect(" a.months,c.koda,c.description as cabang,(case when a.fileapp is not null then 'EXC' else 'INC' end) as pms,"
                + "round(a.biaya,2) as biaya,sum(a.total_biaya) as total_biaya ");

        sqanon.addQuery(" from biaya_pemasaran a "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code ");

        sqanon.addClause("a.validasi_f = 'Y'");
        sqanon.addClause("a.status1 = 'Y'");
        sqanon.addClause("a.status2 = 'Y'");
        sqanon.addClause("a.status3 = 'Y'");
        sqanon.addClause("a.status4 = 'Y'");
        sqanon.addClause("a.fileapp is not null");

        if (period != null) {
            sqanon.addClause("a.months = '" + monthStr + "'");
        }

        if (year != null) {
            sqanon.addClause("a.years = '" + year + "'");
        }

        if (stBranch != null) {
            sqanon.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sqlnon = "select a.pms,a.months,a.koda as cc_code,a.cabang as accountdesc,sum(a.biaya) as biaya,sum(a.total_biaya) as total_biaya from ( "
                + sqanon.getSQL() + " group by 1,2,3,4,5 "
                + ") a group by a.pms,a.months,a.koda,a.cabang order by a.pms,a.months,a.koda ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sqlnon,
                sqanon.getPar(),
                BiayaPemasaranView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;

    }

    public DTOList EXCEL_BIAPEM() throws Exception {

        String fld = "bal";
        String func = "sum";
        long periodFrom = DateUtil.getMonthDigit(policyDateFrom);
        long periodTo = DateUtil.getMonthDigit(policyDateTo);
        int tahun = Integer.parseInt(DateUtil.getYear(policyDateFrom)) - 1;
        String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.jenis_pemasaranid,sum(b.nilai) as biaya,(case when a.fileapp is not null then 'NON1%' else '1%' end) as pms ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.entry_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.entry_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.koda = ?");
            sqa.addPar(stBranch);
        }

        String sql = "select a.jenis_pemasaranid as accountid,c.description as accountno,sum(a.biaya) as biaya,"
                + "coalesce(" + func + "(" + plist + "),0) as total_biaya,a.pms "
                + "from (" + sqa.getSQL() + " group by b.jenis_pemasaranid,a.fileapp order by b.jenis_pemasaranid ) a "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = a.jenis_pemasaranid::int "
                + "left join gl_neraca_total_det d on d.gl_ins_id = c.gl_ins_id and d.tahun = " + tahun;

        if (stBranch != null) {
            sql = sql + " and d.cc_code = '" + stBranch + "' ";
        }

        sql = sql + " group by a.pms,a.jenis_pemasaranid,c.description order by a.pms,a.jenis_pemasaranid ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BIAPEM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("rekap");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Jenis Biaya");
            row0.createCell(1).setCellValue("RKAP");
            row0.createCell(2).setCellValue("Penggunaan");
            row0.createCell(3).setCellValue("Prosentase");
            row0.createCell(4).setCellValue("Biaya");

            BigDecimal persen = BDUtil.getPctFromRate(BDUtil.div(h.getFieldValueByFieldNameBD("biaya"), h.getFieldValueByFieldNameBD("total_biaya"), 4));
            System.out.print("@@@@@@@@@@@@@@@@ persen : " + persen);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("total_biaya").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("biaya").doubleValue());
            row.createCell(3).setCellValue(persen.doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pms"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_BIAPEMDET() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.entry_date,d.description as cob,b.jenis_pemasaranid as accountid,c.description as accountno,b.description as accountdesc,"
                + "b.nilai as biaya,b.excess_ratepajak,b.excess_amount,a.no_bukti,a.no_bukti_bayar,(case when a.fileapp is not null then 'NON 0.5%' else '0.5%' end) as pms ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                + "inner join s_biaop_detail c on c.biaop_dtl_id = b.jenis_pemasaranid::int "
                + "inner join gl_cost_center2 d on d.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.entry_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.entry_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("d.koda = ?");
            sqa.addPar(stBranch);
        }

        String sql = sqa.getSQL() + " order by a.cc_code,a.fileapp,a.entry_date,b.jenis_pemasaranid ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BIAPEMDET() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("detil");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Tanggal Biaya");
            row0.createCell(1).setCellValue("Cabang");
            row0.createCell(2).setCellValue("Akun");
            row0.createCell(3).setCellValue("Keterangan");
            row0.createCell(4).setCellValue("Penggunaan");
            row0.createCell(5).setCellValue("Rate");
            row0.createCell(6).setCellValue("Pajak");
            row0.createCell(7).setCellValue("No. Bukti");
            row0.createCell(8).setCellValue("Biaya");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("entry_date"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cob"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountdesc"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("biaya").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("excess_ratepajak").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("excess_amount").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("no_bukti_bayar"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pms"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_BIAPEMTARGET() throws Exception {

        int bulanNow = Integer.parseInt(period);
        int tahunNow = Integer.parseInt(year);
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
            beforeMonth = "12";
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
            if (bulanCode < 10) {
                beforeMonth = "0" + bulanCode;
            } else {
                beforeMonth = String.valueOf(bulanCode);
            }
//            beforeMonth = String.valueOf(bulanCode);

            tahunCode = tahunNow;
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.category1 as bus_source,c.koda,c.description as cabang,"
                + "sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqa.addQuery(" from ins_policies a "
                + "inner join ent_master b on b.ent_id = a.entity_id "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id "
                + "inner join s_region e on e.region_id = a.region_id ");

        sqa.addClause("a.active_flag='Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.pol_type_id in (59,87,88)");
//        sqa.addClause("e.region_code <> '99'");

        if (period != null) {
            sqa.addClause("substr(a.approved_date::text,6,2) = '" + beforeMonth + "'");
        }

        if (year != null) {
            sqa.addClause("substr(a.approved_date::text,1,4) = '" + tahunCode + "'");
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sql = "select a.koda as cc_code,a.cabang as accountdesc,b.bln" + bulanCode + " as biaya,a.total as total_biaya "
                + "from ( select a.koda,a.cabang,"
                + "coalesce(sum(a.premi-a.diskon),0) as total from ( "
                + sqa.getSQL() + " group by b.category1,c.koda,c.description order by c.koda "
                + ") a group by a.koda,a.cabang order by a.koda ) a "
                + "left join ins_statistic_target_perbulan b on b.cc_code = a.koda "
                + "and b.jenis = 'kredit' and b.years = '" + tahunCode + "'";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BIAPEMTARGET() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("target");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Periode : " + getPeriodName() + " " + getYear());

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Cabang");
            row0.createCell(1).setCellValue("Target");
            row0.createCell(2).setCellValue("Realisasi");
            row0.createCell(3).setCellValue("(%)");
            row0.createCell(4).setCellValue("Biaya Maks. 0.5%");

            BigDecimal cabang = new BigDecimal(0);
            BigDecimal biaya = h.getFieldValueByFieldNameBD("biaya");
            BigDecimal totalbiaya = h.getFieldValueByFieldNameBD("total_biaya");

            if (h.getFieldValueByFieldNameST("cc_code").equalsIgnoreCase("10")) {
                cabang = BDUtil.zero;
            } else {
                cabang = totalbiaya;
            }

            BigDecimal persen = BDUtil.getPctFromRate(BDUtil.div(totalbiaya, biaya, 4));
            System.out.print("@@@@@@@@@@@@@@@@ persen : " + persen);
            BigDecimal saldoBiaya = new BigDecimal(0);
            BigDecimal rateBiaya = new BigDecimal(Parameter.readString("RATE_BIAYAPMS"));
            if (Tools.compare(totalbiaya, BDUtil.zero) > 0) {
                if (Tools.compare(totalbiaya, biaya) <= 0) {
                    saldoBiaya = BDUtil.mul(totalbiaya, BDUtil.getRateFromPct(rateBiaya));
                    saldoBiaya = BDUtil.mul(saldoBiaya, BDUtil.getRateFromPct(persen));
                }
                if (Tools.compare(totalbiaya, biaya) > 0) {
                    saldoBiaya = BDUtil.mul(totalbiaya, BDUtil.getRateFromPct(rateBiaya));
                }
            } else {
                saldoBiaya = BDUtil.zero;
            }


            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountdesc"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("biaya").doubleValue());
            row.createCell(2).setCellValue(cabang.doubleValue());
            row.createCell(3).setCellValue(persen.doubleValue());
            row.createCell(4).setCellValue(saldoBiaya.doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_BIAPEMREALISASI() throws Exception {

        int monthInt = Integer.parseInt(period);
        String monthStr = null;

        if (monthInt < 10) {
            monthStr = "0" + monthInt;
        } else {
            monthStr = String.valueOf(monthInt);
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.months,c.koda,c.description as cabang,(case when a.fileapp is not null then 'NON1%' else '1%' end) as pms,"
                + "round(a.biaya,2) as biaya,sum(a.total_biaya) as total_biaya ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join gl_cost_center2 c on c.cc_code = a.cc_code ");

        sqa.addClause("a.validasi_f = 'Y'");
        sqa.addClause("a.status1 = 'Y'");
        sqa.addClause("a.status2 = 'Y'");
        sqa.addClause("a.status3 = 'Y'");
        sqa.addClause("a.status4 = 'Y'");

        if (period != null) {
            sqa.addClause("a.months = '" + monthStr + "'");
        }

        if (year != null) {
            sqa.addClause("a.years = '" + year + "'");
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '" + stBranch + "'");
        }

        String sql = "select a.pms,a.months,a.koda as cc_code,a.cabang as accountdesc,sum(a.biaya) as biaya,sum(a.total_biaya) as total_biaya from ( "
                + sqa.getSQL() + " group by 1,2,3,4,5 "
                + ") a group by a.pms,a.months,a.koda,a.cabang order by a.pms,a.months,a.koda ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BIAPEMREALISASI() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("realisasi");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Periode : " + getPeriodName() + " " + getYear());

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Cabang");
            row0.createCell(1).setCellValue("Biaya Pemasaran");
            row0.createCell(2).setCellValue("Realisasi Biaya");
            row0.createCell(3).setCellValue("(%)");
            row0.createCell(4).setCellValue("Biaya");

            BigDecimal cabang = new BigDecimal(0);
            BigDecimal biaya;
            if (h.getFieldValueByFieldNameBD("biaya") != null) {
                biaya = h.getFieldValueByFieldNameBD("biaya");
            } else {
                biaya = BDUtil.zero;
            }

            BigDecimal totalbiaya = h.getFieldValueByFieldNameBD("total_biaya");

            if (h.getFieldValueByFieldNameST("cc_code").equalsIgnoreCase("10")) {
                cabang = BDUtil.zero;
            } else {
                cabang = totalbiaya;
            }

            BigDecimal persen = BDUtil.getPctFromRate(BDUtil.div(totalbiaya, biaya, 4));
            System.out.print("@@@@@@@@@@@@@@@@ persen : " + persen);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountdesc"));
            row.createCell(1).setCellValue(biaya.doubleValue());
            row.createCell(2).setCellValue(cabang.doubleValue());
            row.createCell(3).setCellValue(persen.doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pms"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public GLCostCenterView getCostCenter() {

        final GLCostCenterView branch = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return branch;

    }
}

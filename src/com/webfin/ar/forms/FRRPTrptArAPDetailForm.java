/***********************************************************************
 * Module:  com.webfin.ar.forms.FRRPTrptArAPDetailForm
 * Author:  Denny Mahendra
 * Created: Mar 7, 2007 10:55:04 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.lang.LanguageManager;
import com.crux.web.controller.SessionManager;
import com.crux.lov.LOVManager;
import com.crux.util.fop.FOPTableSource;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import java.util.Date;
import com.crux.util.*;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;

import com.crux.web.form.Form;
import com.webfin.insurance.model.InsurancePolicyTypeInwardView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import java.io.DataInputStream;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FRRPTrptArAPDetailForm extends Form {

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
    private String stBussinessPolType;
    private String stBussinessPolTypeCob;

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
        ref1 = LOVManager.getInstance().getRef1("PROD_INWARD", stReport);

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

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_INWARD");
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
    }

    public DTOList INWARD_FAC() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.short_name as create_who,coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,a.invoice_no,a.trx_no_reference,a.description, "
                + " a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,a.attr_pol_tsi_total,a.attr_pol_tsi,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id = '1',d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id = '2',d.amount)) as comm,"
                + " sum(checkreas(d.ar_trx_line_id = '3',d.amount)) as fee,"
                + " sum(checkreas(d.ar_trx_line_id = '1',d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '2',d.entered_amount)) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '3',d.entered_amount)) as fee_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 1");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,b.short_name,b.reas_ent_id,b.ent_id,a.invoice_no,a.description, "
                + " a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,a.attr_pol_tsi_total,a.attr_pol_tsi,a.attr_pol_type_id "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList INWARD_TREATY() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,d.attr_pol_type_id,a.attr_quartal,a.attr_quartal_year,coalesce(d.attr_underwrit,a.attr_underwrit) as attr_underwrit,a.invoice_date,"
                + " a.invoice_no,a.trx_no_reference,a.mutation_date,coalesce(d.refid0,a.refid0) as refid0,b.ent_id,b.ent_name,coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,b.short_name as create_who,a.ccy,a.ccy_rate,a.description,"
                + " sum(checkreas(d.ar_trx_line_id = '4',d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id = '5',d.amount)) as comm,"
                + " sum(checkreas(d.ar_trx_line_id = '6',d.amount)) as fee, "
                + " sum(checkreas(d.ar_trx_line_id = '127',d.amount)) as bfee, "
                + " sum(checkreas(d.ar_trx_line_id = '128',d.amount)) as reco, "
                + " sum(checkreas(d.ar_trx_line_id = '4',d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '5',d.entered_amount)) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '6',d.entered_amount)) as fee_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '127',d.entered_amount)) as bfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id = '128',d.entered_amount)) as reco_entered ");

        sqa.addQuery(" from ins_pol_inward a"
                + " left join ent_master b on b.ent_id = a.ent_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 2");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,a.attr_quartal_year,d.attr_pol_type_id,a.attr_quartal,d.attr_underwrit,a.invoice_date,a.refid0,"
                + " a.invoice_no,d.refid0,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate,a.description,a.attr_underwrit "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList INWARD_EXCESS() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.invoice_date,a.mutation_date,a.description, "
                + " a.invoice_no,a.trx_no_reference,coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,b.short_name as create_who,a.ccy,a.ccy_rate,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id = '77',d.amount)) as premi, "
                + " sum(checkreas(d.ar_trx_line_id = '77',d.entered_amount)) as premi_entered, "
                + " sum(checkreas(d.ar_trx_line_id = '89',d.amount)) as comm, "
                + " sum(checkreas(d.ar_trx_line_id = '89',d.entered_amount)) as comm_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 3");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        final String sql = sqa.getSQL() + " group by a.invoice_date,a.mutation_date,a.description, "
                + " a.invoice_no,a.trx_no_reference,b.reas_ent_id,b.ent_id,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
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
        ref1 = LOVManager.getInstance().getRef1("PROD_INWARD", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_INWARD", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcel() throws Exception {
        //ref1 = LOVManager.getInstance().getRef1("PROD_PRINTING",stPrintForm);
        ref1 = LOVManager.getInstance().getRef1("PROD_INWARD", stReport);

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

    public DTOList EXCEL_FAC() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.short_name,coalesce(b.reas_ent_id,b.ent_id::text) as reas_ent_id,a.invoice_no,a.description,a.mutation_date,e.ent_name as reinsured, "
                + " a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,a.attr_pol_tsi_total,a.attr_pol_tsi,a.attr_pol_type_id,a.attr_pol_name, "
                + " sum(checkreas(d.ar_trx_line_id = 1,coalesce(d.entered_amount,0))) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id = 2,coalesce(d.entered_amount,0))) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id = 3,coalesce(d.entered_amount,0))) as fee_entered, "
                + "sum(checkreas(d.ar_trx_line_id = 141,d.entered_amount)) as bfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 142,d.entered_amount)) as ppn_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 143,d.entered_amount)) as pph23_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 144,d.entered_amount)) as klaim_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 145,d.entered_amount)) as recov_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 146,d.entered_amount)) as hfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 147,d.entered_amount)) as mgfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 148,d.entered_amount)) as opfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 149,d.entered_amount)) as gfund_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 1");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,b.short_name,b.reas_ent_id,b.ent_id,a.invoice_no,a.description,a.mutation_date, "
                + " a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,a.attr_pol_tsi_total,a.attr_pol_tsi,a.attr_pol_type_id,a.attr_pol_name,e.ent_name "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_FAC() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        BigDecimal komisi = new BigDecimal(0);
        BigDecimal netto = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Reasuradur");
            row0.createCell(1).setCellValue("Kd.");
            row0.createCell(2).setCellValue("Pol");
            row0.createCell(3).setCellValue("No. Slip");
            row0.createCell(4).setCellValue("Tgl. Pertanggungan");
            row0.createCell(5).setCellValue("Tgl. Pertanggungan");
            row0.createCell(6).setCellValue("Curr");
            row0.createCell(7).setCellValue("Kurs ");
            row0.createCell(8).setCellValue("Jumlah Pertanggungan");
            row0.createCell(9).setCellValue("Our Share");
            row0.createCell(10).setCellValue("Premi");
            row0.createCell(11).setCellValue("Komisi");
            row0.createCell(12).setCellValue("Netto");
            row0.createCell(13).setCellValue("Brokerage Fee");
            row0.createCell(14).setCellValue("PPN/VAT");
            row0.createCell(15).setCellValue("ppH23-Inclusive");
            row0.createCell(16).setCellValue("Klaim");
            row0.createCell(17).setCellValue("Recoveries");
            row0.createCell(18).setCellValue("Handling Fee");
            row0.createCell(19).setCellValue("Management Fee");
            row0.createCell(20).setCellValue("Operational Fee");
            row0.createCell(21).setCellValue("Guarantee Fund");
            row0.createCell(22).setCellValue("Description");
            row0.createCell(23).setCellValue("Tgl. Mutasi");
            row0.createCell(24).setCellValue("Risk Name");
            row0.createCell(25).setCellValue("Jenis COB");
            row0.createCell(26).setCellValue("Reinsured");

            komisi = BDUtil.add(h.getFieldValueByFieldNameBD("comm_entered"), h.getFieldValueByFieldNameBD("fee_entered"));
            netto = BDUtil.sub(h.getFieldValueByFieldNameBD("premi_entered"), komisi);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("attr_pol_per_0"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("attr_pol_per_1"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_tsi_total").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_tsi").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(11).setCellValue(komisi.doubleValue());
            row.createCell(12).setCellValue(netto.doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("bfee_entered").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("ppn_entered").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("pph23_entered").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("klaim_entered").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("recov_entered").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("hfee_entered").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("mgfee_entered").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("opfee_entered").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("gfund_entered").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameST("attr_pol_name"));
            row.createCell(25).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_TREATY() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,d.attr_pol_type_id,a.attr_quartal,a.attr_quartal_year,coalesce(d.attr_underwrit,a.attr_underwrit) as attr_underwrit,"
                + "a.invoice_date,a.mutation_date,a.invoice_no,coalesce(d.refid0,a.refid0) as refid0,b.ent_name,coalesce(b.reas_ent_id,b.ent_id::text) as reas_ent_id,"
                + "b.short_name,a.ccy,a.ccy_rate,a.description,e.ent_name as reinsured,"
                + " sum(checkreas(d.ar_trx_line_id = 4,coalesce(d.entered_amount,0))) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id = 5,coalesce(d.entered_amount,0))) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id = 6,coalesce(d.entered_amount,0))) as claim_entered, "
                + " sum(checkreas(d.ar_trx_line_id = 127,coalesce(d.entered_amount,0))) as bfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id = 128,coalesce(d.entered_amount,0))) as reco_entered, "
                + "sum(checkreas(d.ar_trx_line_id = 150,d.entered_amount)) as ppn_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 151,d.entered_amount)) as pph23_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 152,d.entered_amount)) as fee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 153,d.entered_amount)) as hfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 154,d.entered_amount)) as mgfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 155,d.entered_amount)) as opfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 156,d.entered_amount)) as gfund_entered ");

        sqa.addQuery(" from ins_pol_inward a"
                + " left join ent_master b on b.ent_id = a.ent_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 2");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,a.attr_quartal_year,d.attr_pol_type_id,a.attr_quartal,d.attr_underwrit,a.attr_underwrit,a.invoice_date,a.mutation_date,"
                + " a.invoice_no,d.refid0,a.refid0,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate,a.description,e.ent_name "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_TREATY() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Bisnis");
            row0.createCell(1).setCellValue("Quartal");
            row0.createCell(2).setCellValue("Tanggal");
            row0.createCell(3).setCellValue("Tahun UW.");
            row0.createCell(4).setCellValue("Treaty");
            row0.createCell(5).setCellValue("Uraian");
            row0.createCell(6).setCellValue("Kd. Perusahaan");
            row0.createCell(7).setCellValue("Perusahaan");
            row0.createCell(8).setCellValue("Curr");
            row0.createCell(9).setCellValue("Kurs");
            row0.createCell(10).setCellValue("Premi");
            row0.createCell(11).setCellValue("Komisi");
            row0.createCell(12).setCellValue("Klaim");
            row0.createCell(13).setCellValue("Broker Fee");
            row0.createCell(14).setCellValue("Recoveries");
            row0.createCell(15).setCellValue("PPN/VAT");
            row0.createCell(16).setCellValue("ppH23-Inclusive");
            row0.createCell(17).setCellValue("Fee");
            row0.createCell(18).setCellValue("Handling Fee");
            row0.createCell(19).setCellValue("Management Fee");
            row0.createCell(20).setCellValue("Operational Fee");
            row0.createCell(21).setCellValue("Guarantee Fund");
            row0.createCell(22).setCellValue("Description");
            row0.createCell(23).setCellValue("Tgl. Mutasi");
            row0.createCell(24).setCellValue("Jenis COB");
            row0.createCell(25).setCellValue("Reinsured");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            if (h.getFieldValueByFieldNameBD("attr_pol_type_id") != null) {
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("attr_quartal") != null) {
                row.createCell(1).setCellValue("TW." + h.getFieldValueByFieldNameST("attr_quartal") + "/" + h.getFieldValueByFieldNameST("attr_quartal_year"));
            }
            if (h.getFieldValueByFieldNameDT("invoice_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("invoice_date"));
            }
            if (h.getFieldValueByFieldNameST("attr_underwrit") != null) {
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("attr_underwrit"));
            }
            if (h.getFieldValueByFieldNameST("refid0") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("refid0"));
            }
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_entered").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("claim_entered").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("bfee_entered").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("reco_entered").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("ppn_entered").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("pph23_entered").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("fee_entered").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("hfee_entered").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("mgfee_entered").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("opfee_entered").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("gfund_entered").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(24).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_EXCESS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.invoice_date,a.mutation_date,a.description, "
                + " a.invoice_no,coalesce(b.reas_ent_id,b.ent_id::text) as reas_ent_id,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id,e.ent_name as reinsured, "
                + " sum(checkreas(d.ar_trx_line_id = 77,coalesce(d.entered_amount,0))) as premi_entered, "
                + " sum(checkreas(d.ar_trx_line_id = 89,coalesce(d.entered_amount,0))) as comm_entered, "
                + "sum(checkreas(d.ar_trx_line_id = 157,d.entered_amount)) as bfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 158,d.entered_amount)) as ppn_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 159,d.entered_amount)) as pph23_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 160,d.entered_amount)) as klaim_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 161,d.entered_amount)) as recov_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 162,d.entered_amount)) as fee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 163,d.entered_amount)) as hfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 164,d.entered_amount)) as mgfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 165,d.entered_amount)) as opfee_entered,"
                + "sum(checkreas(d.ar_trx_line_id = 166,d.entered_amount)) as gfund_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 3");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " group by a.invoice_date,a.mutation_date,a.description, "
                + " a.invoice_no,b.reas_ent_id,b.ent_id,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id,e.ent_name "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_EXCESS() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Bisnis");
            row0.createCell(1).setCellValue("Tanggal");
            row0.createCell(2).setCellValue("Tanggal Mutasi");
            row0.createCell(3).setCellValue("Uraian");
            row0.createCell(4).setCellValue("Kd.");
            row0.createCell(5).setCellValue("Perusahaan");
            row0.createCell(6).setCellValue("Curr");
            row0.createCell(7).setCellValue("Kurs");
            row0.createCell(8).setCellValue("Premi");
            row0.createCell(9).setCellValue("Komisi");
            row0.createCell(10).setCellValue("Brokerage Fee");
            row0.createCell(11).setCellValue("PPN/VAT");
            row0.createCell(12).setCellValue("ppH23-Inclusive");
            row0.createCell(13).setCellValue("Klaim");
            row0.createCell(14).setCellValue("Recoveries");
            row0.createCell(15).setCellValue("Fee");
            row0.createCell(16).setCellValue("Handling Fee");
            row0.createCell(17).setCellValue("Management Fee");
            row0.createCell(18).setCellValue("Operational Fee");
            row0.createCell(19).setCellValue("Guarantee Fund");
            row0.createCell(20).setCellValue("Description");
            row0.createCell(21).setCellValue("Jenis COB");
            row0.createCell(22).setCellValue("Reinsured");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("invoice_date"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("comm_entered").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("bfee_entered").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("ppn_entered").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("pph23_entered").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("klaim_entered").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("recov_entered").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("fee_entered").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("hfee_entered").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("mgfee_entered").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("opfee_entered").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("gfund_entered").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(21).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList INWARD_ALL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" c.description as description,a.ar_trx_type_id,a.invoice_date,a.mutation_date,coalesce(d.refid0,a.refid0) as refid0, "
                + " a.ar_invoice_id,a.invoice_no,a.trx_no_reference, coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,b.short_name as create_who,a.ccy,a.ccy_rate, "
                + " coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.short_name,"
                + " sum(checkreas(d.ar_trx_line_id in (1,4,77,79),d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id in (80),d.amount)) as profitcomm,"
                + " sum(checkreas(d.ar_trx_line_id in (2,5,89,100),d.amount)) as comm,"
                //                + " sum(checkreas(d.ar_trx_line_id in (3,127),d.amount)) as fee,"
                + " sum(checkreas(d.ar_trx_line_id in (3,152,162,171,127,141,157,167,128,145,161,170,135,142,150,158,168,143,151,159,169,146,153,163,172,147,154,164,173,148,155,165,174,149,156,166,175),d.amount)) as fee,"
                //                + " sum(checkreas(d.ar_trx_line_id in (6,81,108),d.amount)) as claim,"
                + " sum(checkreas(d.ar_trx_line_id in (6,81,108,144,160),d.amount)) as claim,"
                + " sum(checkreas(d.ar_trx_line_id in (1,4,77,79),d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (80),d.entered_amount)) as profitcomm_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (2,5,89,100),d.entered_amount)) as comm_entered,"
                //                + " sum(checkreas(d.ar_trx_line_id in (3,127),d.entered_amount)) as fee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (3,152,162,171,127,141,157,167,128,145,161,170,135,142,150,158,168,143,151,159,169,146,153,163,172,147,154,164,173,148,155,165,174,149,156,166,175),d.entered_amount)) as fee_entered,"
                //                + " sum(checkreas(d.ar_trx_line_id in (6,81,108),d.entered_amount)) as claim_entered ");
                + " sum(checkreas(d.ar_trx_line_id in (6,81,108,144,160),d.entered_amount)) as claim_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //.addClause("a.approved_flag = 'Y'");
        sqa.addClause("(a.ar_trx_type_id in (1,2,3,20,21) or (a.claim_status = 'DLA' and a.ar_trx_type_id = 23))");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("coalesce(a.attr_pol_type_id,d.attr_pol_type_id) = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        final String sql = sqa.getSQL()
                + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date,a.mutation_date,a.refid0,d.refid0,d.attr_pol_type_id,"
                + " a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate "
                + " order by a.ar_trx_type_id,a.attr_pol_type_id,b.reas_ent_id";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList INWARD_PROFIT() throws Exception {
        final boolean inward = "Y".equalsIgnoreCase((String) refPropMap.get("INWARD"));
        final boolean outward = "Y".equalsIgnoreCase((String) refPropMap.get("OUTWARD"));
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.short_name as create_who,coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,a.invoice_no,a.description, "
                + " a.ccy,a.trx_no_reference,a.ccy_rate,a.attr_quartal,coalesce(d.attr_underwrit,"
                + "a.attr_underwrit) as attr_underwrit,a.invoice_date,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id in ('79',97),d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id in ('100',98),d.amount)) as comm,"
                + " sum(checkreas(d.ar_trx_line_id in ('80',99),d.amount)) as profitcomm,"
                + " sum(checkreas(d.ar_trx_line_id = '81',d.amount)) as claim,"
                + " sum(checkreas(d.ar_trx_line_id in ('79',97),d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id in ('100',98),d.entered_amount)) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id in ('80',99),d.entered_amount)) as profitcomm_entered, "
                + " sum(checkreas(d.ar_trx_line_id = '81',d.entered_amount)) as claim_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        if (inward) {
            sqa.addClause("a.ar_trx_type_id = 20");
        } else if (outward) {
            sqa.addClause("a.ar_trx_type_id = 21");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (Tools.isNo(stApproved)) {
            sqa.addClause("coalesce(a.approved_flag,'N') <> 'Y'");
        }

        final String sql = sqa.getSQL() + " group by b.short_name,b.reas_ent_id,b.ent_id,a.invoice_no,a.trx_no_reference,a.description,a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,d.refid0,a.refid0, "
                + " a.attr_quartal,d.attr_underwrit,a.attr_underwrit,a.invoice_date,a.mutation_date,a.attr_pol_type_id "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_PROFIT() throws Exception {
        final boolean inward = "Y".equalsIgnoreCase((String) refPropMap.get("INWARD"));
        final boolean outward = "Y".equalsIgnoreCase((String) refPropMap.get("OUTWARD"));
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.short_name,coalesce(b.reas_ent_id,b.ent_id::text) as reas_ent_id,a.invoice_no,a.description, "
                + " a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,coalesce(d.refid0,a.refid0) as refid0,e.ent_name as reinsured, "
                + " a.attr_quartal,coalesce(d.attr_underwrit,a.attr_underwrit) as attr_underwrit,a.invoice_date,a.mutation_date,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id in (79,97),d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id in (100,98),d.amount)) as comm,"
                + " sum(checkreas(d.ar_trx_line_id in (80,99),d.amount)) as profitcomm,"
                + " sum(checkreas(d.ar_trx_line_id = 81,d.amount)) as claim,"
                + " sum(checkreas(d.ar_trx_line_id in (79,97),d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (100,98),d.entered_amount)) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (80,99),d.entered_amount)) as profitcomm_entered, "
                + " sum(checkreas(d.ar_trx_line_id = 81,d.entered_amount)) as claim_entered, "
                + "sum(checkreas(d.ar_trx_line_id = 167,d.entered_amount)) as bfee,"
                + "sum(checkreas(d.ar_trx_line_id = 168,d.entered_amount)) as ppn,"
                + "sum(checkreas(d.ar_trx_line_id = 169,d.entered_amount)) as pph23,"
                + "sum(checkreas(d.ar_trx_line_id = 170,d.entered_amount)) as recov,"
                + "sum(checkreas(d.ar_trx_line_id = 171,d.entered_amount)) as fee,"
                + "sum(checkreas(d.ar_trx_line_id = 172,d.entered_amount)) as hfee,"
                + "sum(checkreas(d.ar_trx_line_id = 173,d.entered_amount)) as mgfee,"
                + "sum(checkreas(d.ar_trx_line_id = 174,d.entered_amount)) as opfee,"
                + "sum(checkreas(d.ar_trx_line_id = 175,d.entered_amount)) as gfund ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        if (inward) {
            sqa.addClause("a.ar_trx_type_id = 20");
        } else if (outward) {
            sqa.addClause("a.ar_trx_type_id = 21");
        }

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (Tools.isNo(stApproved)) {
            sqa.addClause("coalesce(a.approved_flag,'N') <> 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " group by b.short_name,b.reas_ent_id,b.ent_id,a.invoice_no,a.description,a.attr_pol_per_0,a.attr_pol_per_1,a.ccy,a.ccy_rate,d.refid0,a.refid0, "
                + " a.attr_quartal,d.attr_underwrit,a.attr_underwrit,a.invoice_date,a.mutation_date,a.attr_pol_type_id,e.ent_name "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_PROFIT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Bisnis");
            row0.createCell(1).setCellValue("Quartal");
            row0.createCell(2).setCellValue("Tanggal");
            row0.createCell(3).setCellValue("Tahun UW.");
            row0.createCell(4).setCellValue("Treaty");
            row0.createCell(5).setCellValue("Uraian");
            row0.createCell(6).setCellValue("Kd. Perusahaan");
            row0.createCell(7).setCellValue("Perusahaan");
            row0.createCell(8).setCellValue("Curr");
            row0.createCell(9).setCellValue("Kurs");
            row0.createCell(10).setCellValue("Premi");
            row0.createCell(11).setCellValue("Komisi");
            row0.createCell(12).setCellValue("Profit Komisi");
            row0.createCell(13).setCellValue("Klaim");
            row0.createCell(14).setCellValue("Brokerage Fee");
            row0.createCell(15).setCellValue("PPN/VAT");
            row0.createCell(16).setCellValue("ppH23-Inclusive");
            row0.createCell(17).setCellValue("Recoveries");
            row0.createCell(18).setCellValue("Fee");
            row0.createCell(19).setCellValue("Handling Fee");
            row0.createCell(20).setCellValue("Management Fee");
            row0.createCell(21).setCellValue("Operational Fee");
            row0.createCell(22).setCellValue("Guarantee Fund");
            row0.createCell(23).setCellValue("Description");
            row0.createCell(24).setCellValue("Tgl. Mutasi");
            row0.createCell(25).setCellValue("Jenis COB");
            row0.createCell(26).setCellValue("Reinsured");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            if (h.getFieldValueByFieldNameST("attr_quartal") != null) {
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("attr_quartal"));
            }
            if (h.getFieldValueByFieldNameDT("invoice_date") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("invoice_date"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("attr_underwrit"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("refid0"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("comm_entered").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("profitcomm_entered").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("claim_entered").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("bfee").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("pph23").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("recov").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("fee").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("hfee").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("mgfee").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("opfee").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("gfund").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(25).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList INWARD_CLAIM() throws Exception {
        final boolean FLT_CLAIM_FAC = "Y".equalsIgnoreCase((String) refPropMap.get("FAC"));
        final boolean FLT_CLAIM_TREATY = "Y".equalsIgnoreCase((String) refPropMap.get("TREATY"));
        final boolean FLT_CLAIM_XOL = "Y".equalsIgnoreCase((String) refPropMap.get("XOL"));
        final boolean FLT_CLAIM_CL = "Y".equalsIgnoreCase((String) refPropMap.get("CL"));
        final boolean FLT_CLAIM_EXGRATIA = "Y".equalsIgnoreCase((String) refPropMap.get("EXGRATIA"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(" from ins_pol_inward a ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.approved_flag = 'Y'");

        if (FLT_CLAIM_FAC) {
            sqa.addClause("a.ar_trx_type_id = 17");
        }

        if (FLT_CLAIM_TREATY) {
            sqa.addClause("a.ar_trx_type_id = 18");
        }

        if (FLT_CLAIM_XOL) {
            sqa.addClause("a.ar_trx_type_id in (19,25)");
        }

        if (FLT_CLAIM_CL) {
            sqa.addClause("a.ar_trx_type_id = 23");
        }

        if (FLT_CLAIM_EXGRATIA) {
            sqa.addClause("a.ar_trx_type_id = 24");
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= ?");
            sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= ?");
            sqa.addPar(DLADateTo);
        }

        if (approvedDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(approvedDateFrom);
        }

        if (approvedDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(approvedDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(stFltTreatyType);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_invoice_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

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

    public DTOList INWARD_EXCESSOUT() throws Exception {
        final boolean FLT_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("REINS"));
        final boolean FLT_TYPE = "Y".equalsIgnoreCase((String) refPropMap.get("TYPE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.invoice_date,a.mutation_date,a.description, "
                + " a.invoice_no,a.trx_no_reference, coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,b.short_name as create_who,a.ccy,a.ccy_rate,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id = '102',d.amount)) as premi, "
                + " sum(checkreas(d.ar_trx_line_id = '102',d.entered_amount)) as premi_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 22");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        String sql = sqa.getSQL() + " group by a.invoice_date,a.mutation_date,a.description,a.invoice_no,a.trx_no_reference, "
                + "b.reas_ent_id,b.ent_id::text,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id ";

        if (FLT_REINS) {
            sql = sql + " order by b.reas_ent_id";
        }

        if (FLT_TYPE) {
            sql = sql + " order by a.attr_pol_type_id";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_EXCESSOUT() throws Exception {
        final boolean FLT_REINS = "Y".equalsIgnoreCase((String) refPropMap.get("REINS"));
        final boolean FLT_TYPE = "Y".equalsIgnoreCase((String) refPropMap.get("TYPE"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.invoice_date,a.mutation_date,a.description,a.trx_no_reference,e.ent_name as reinsured, "
                + " a.invoice_no,coalesce(b.reas_ent_id,b.ent_id::text) as ent_id,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id, "
                + " sum(checkreas(d.ar_trx_line_id = '102',coalesce(d.entered_amount,0))) as premi_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 22");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        String sql = sqa.getSQL() + " group by a.invoice_date,a.mutation_date,a.description,a.trx_no_reference, "
                + " a.invoice_no,b.reas_ent_id,b.ent_id,b.short_name,a.ccy,a.ccy_rate,a.attr_pol_type_id,e.ent_name ";

        if (FLT_REINS) {
            sql = sql + " order by b.reas_ent_id,a.attr_pol_type_id";
        }

        if (FLT_TYPE) {
            sql = sql + " order by a.attr_pol_type_id,b.reas_ent_id";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_EXCESSOUT() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Bisnis");
            row0.createCell(1).setCellValue("Tanggal");
            row0.createCell(2).setCellValue("Tanggal Mutasi");
            row0.createCell(3).setCellValue("Uraian");
            row0.createCell(4).setCellValue("Kd.");
            row0.createCell(5).setCellValue("Perusahaan");
            row0.createCell(6).setCellValue("Curr");
            row0.createCell(7).setCellValue("Kurs");
            row0.createCell(8).setCellValue("Premi");
            row0.createCell(9).setCellValue("Description");
            row0.createCell(10).setCellValue("Jenis COB");
            row0.createCell(11).setCellValue("Reinsured");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("invoice_date"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(10).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList OUTWARD_XOL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,b.ent_name,b.short_name as create_who,b.ent_id,b.reas_ent_id as ar_cust_id,"
                + "a.attr_pol_type_id,a.invoice_no,a.ccy,a.ccy_rate,a.due_date,a.mutation_date,a.description,"
                + "sum(checkreas(d.ar_trx_line_id = '102',d.amount)) as premi,"
                + "sum(checkreas(d.ar_trx_line_id = '102',d.entered_amount)) as premi_entered ");

        sqa.addQuery(" from ins_pol_inward a"
                + " left join ent_master b on b.ent_id = a.ent_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 22");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,b.ent_name,b.short_name,a.description,"
                + "b.ent_id,b.reas_ent_id,a.attr_pol_type_id,a.invoice_no,a.ccy,a.ccy_rate,a.due_date,a.mutation_date "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_OUTWARD_XOL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,b.ent_name,b.short_name,a.ent_id,b.reas_ent_id,a.attr_pol_type_id,"
                + "a.invoice_no,a.ccy,a.ccy_rate,a.due_date,a.mutation_date,refa0,refa1,a.description,e.ent_name as reinsured, "
                + "sum(checkreas(d.ar_trx_line_id = '102',coalesce(d.amount,0))) as premi,"
                + "sum(checkreas(d.ar_trx_line_id = '102',coalesce(d.entered_amount,0))) as premi_entered ");

        sqa.addQuery(" from ins_pol_inward a"
                + " left join ent_master b on b.ent_id = a.ent_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id = 22");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("d.attr_pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL() + " group by a.ar_invoice_id,refa0,refa1,b.ent_name,b.short_name,a.description,"
                + " a.ent_id,b.reas_ent_id,a.attr_pol_type_id,a.invoice_no,a.ccy,a.due_date,a.mutation_date,a.ccy_rate,e.ent_name "
                + " order by b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_OUTWARD_XOL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("Reasuradur");
            row0.createCell(1).setCellValue("Kd.");
            row0.createCell(2).setCellValue("Pol");
            row0.createCell(3).setCellValue("No. Slip");
            row0.createCell(4).setCellValue("Due Date");
            row0.createCell(5).setCellValue("Mutation Date");
            row0.createCell(6).setCellValue("Curr");
            row0.createCell(7).setCellValue("Kurs ");
            row0.createCell(8).setCellValue("Premi");
            row0.createCell(9).setCellValue("Layer ");
            row0.createCell(10).setCellValue("Type");
            row0.createCell(11).setCellValue("Keterangan");
            row0.createCell(12).setCellValue("Jenis COB");
            row0.createCell(13).setCellValue("Reinsured");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("short_name"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("reas_ent_id"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("due_date"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("refa0"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("refa1"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(12).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("reinsured"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
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

    public void setStFltTreatyType(String stFltTreatyType) {
        this.stFltTreatyType = stFltTreatyType;
    }

    public DTOList EXCEL_INWARD_ALL() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" c.description as inward,a.ar_trx_type_id,a.invoice_date,a.mutation_date,coalesce(d.refid0,a.refid0) as refid0,a.attr_pol_name,a.description, "
                + " a.ar_invoice_id,a.invoice_no,a.trx_no_reference,a.attr_pol_no,a.attr_pol_per_0,a.attr_pol_per_1,a.attr_pol_tsi_total,a.attr_pol_tsi,a.attr_quartal,a.attr_underwrit,"
                + "coalesce(b.reas_ent_id,b.ent_id::text) as ar_cust_id,b.short_name as create_who,a.ccy,a.ccy_rate,e.ent_name as reinsured, "
                + " coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.short_name,"
                + " sum(checkreas(d.ar_trx_line_id in (1,4,77,79),d.amount)) as premi,"
                + " sum(checkreas(d.ar_trx_line_id in (80),d.amount)) as profitcomm,"
                + " sum(checkreas(d.ar_trx_line_id in (2,5,89,100),d.amount)) as comm,"
                + " sum(checkreas(d.ar_trx_line_id in (3,152,162,171),d.amount)) as fee,"
                + " sum(checkreas(d.ar_trx_line_id in (6,81,108,144,160),d.amount)) as claim,"
                + " sum(checkreas(d.ar_trx_line_id in (127,141,157,167),d.amount)) as bfee,"
                + " sum(checkreas(d.ar_trx_line_id in (128,145,161,170),d.amount)) as recoveries,"
                + " sum(checkreas(d.ar_trx_line_id in (135),d.amount)) as reinstatement,"
                + " sum(checkreas(d.ar_trx_line_id in (142,150,158,168),d.amount)) as ppn,"
                + " sum(checkreas(d.ar_trx_line_id in (143,151,159,169),d.amount)) as pph23,"
                + " sum(checkreas(d.ar_trx_line_id in (146,153,163,172),d.amount)) as hfee,"
                + " sum(checkreas(d.ar_trx_line_id in (147,154,164,173),d.amount)) as mgfee,"
                + " sum(checkreas(d.ar_trx_line_id in (148,155,165,174),d.amount)) as opfee,"
                + " sum(checkreas(d.ar_trx_line_id in (149,156,166,175),d.amount)) as gfund,"
                + " sum(checkreas(d.ar_trx_line_id in (1,4,77,79),d.entered_amount)) as premi_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (80),d.entered_amount)) as profitcomm_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (2,5,89,100),d.entered_amount)) as comm_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (3,152,162,171),d.entered_amount)) as fee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (6,81,108,144,160),d.entered_amount)) as claim_entered, "
                + " sum(checkreas(d.ar_trx_line_id in (127,141,157,167),d.entered_amount)) as bfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (128,145,161,170),d.entered_amount)) as recoveries_entered, "
                + " sum(checkreas(d.ar_trx_line_id in (135),d.entered_amount)) as reinstatement_entered, "
                + " sum(checkreas(d.ar_trx_line_id in (142,150,158,168),d.entered_amount)) as ppn_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (143,151,159,169),d.entered_amount)) as pph23_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (146,153,163,172),d.entered_amount)) as hfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (147,154,164,173),d.entered_amount)) as mgfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (148,155,165,174),d.entered_amount)) as opfee_entered,"
                + " sum(checkreas(d.ar_trx_line_id in (149,156,166,175),d.entered_amount)) as gfund_entered ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id"
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("(a.ar_trx_type_id in (1,2,3,20,21,22) or (a.claim_status = 'DLA' and a.ar_trx_type_id in (17,18,19,23,25)))");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("coalesce(a.attr_pol_type_id,d.attr_pol_type_id) = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        final String sql = sqa.getSQL()
                + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date,a.mutation_date,a.refid0,d.refid0,d.attr_pol_type_id,"
                + " a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate,e.ent_name "
                + " order by a.ar_trx_type_id,a.attr_pol_type_id,b.reas_ent_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_INWARD_ALL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("inward");
            row0.createCell(1).setCellValue("ar_trx_type_id");
            row0.createCell(2).setCellValue("trx_date");
            row0.createCell(3).setCellValue("mutation_date");
            row0.createCell(4).setCellValue("invoice_no");
            row0.createCell(5).setCellValue("trx_no_reference");
            row0.createCell(6).setCellValue("attr_pol_no");
            row0.createCell(7).setCellValue("tglawal");
            row0.createCell(8).setCellValue("tglakhir");
            row0.createCell(9).setCellValue("insured");
            row0.createCell(10).setCellValue("share askrida");
            row0.createCell(11).setCellValue("quartal");
            row0.createCell(12).setCellValue("tahun_uy");
            row0.createCell(13).setCellValue("refid0");
            row0.createCell(14).setCellValue("risk_name");
            row0.createCell(15).setCellValue("description");
            row0.createCell(16).setCellValue("ccy");
            row0.createCell(17).setCellValue("ccy_rate");
            row0.createCell(18).setCellValue("attr_pol_type_id");
            row0.createCell(19).setCellValue("perusahaan");
            row0.createCell(20).setCellValue("premi IDR");
            row0.createCell(21).setCellValue("comm IDR");
            row0.createCell(22).setCellValue("fee IDR");
            row0.createCell(23).setCellValue("claim IDR");
            row0.createCell(24).setCellValue("bfee IDR");
            row0.createCell(25).setCellValue("recoveries IDR");
            row0.createCell(26).setCellValue("reinstatement IDR");
            row0.createCell(27).setCellValue("profitcomm IDR");
            row0.createCell(28).setCellValue("ppn IDR");
            row0.createCell(29).setCellValue("pph23 IDR");
            row0.createCell(30).setCellValue("hfee IDR");
            row0.createCell(31).setCellValue("mgfee IDR");
            row0.createCell(32).setCellValue("opfee IDR");
            row0.createCell(33).setCellValue("gfund IDR");
            row0.createCell(34).setCellValue("premi non IDR");
            row0.createCell(35).setCellValue("comm non IDR");
            row0.createCell(36).setCellValue("fee non IDR");
            row0.createCell(37).setCellValue("claim non IDR");
            row0.createCell(38).setCellValue("bfee non IDR");
            row0.createCell(39).setCellValue("recoveries non IDR");
            row0.createCell(40).setCellValue("reinstatement non IDR");
            row0.createCell(41).setCellValue("profitcomm non IDR");
            row0.createCell(42).setCellValue("ppn non IDR");
            row0.createCell(43).setCellValue("pph23 non IDR");
            row0.createCell(44).setCellValue("hfee non IDR");
            row0.createCell(45).setCellValue("mgfee non IDR");
            row0.createCell(46).setCellValue("opfee non IDR");
            row0.createCell(47).setCellValue("gfund non IDR");
            row0.createCell(48).setCellValue("tgl mutasi");
            row0.createCell(49).setCellValue("Jenis COB");
            row0.createCell(50).setCellValue("Reinsured");

//            BigDecimal komisiIDR = BDUtil.sub(h.getFieldValueByFieldNameBD("comm"), h.getFieldValueByFieldNameBD("profitcomm"));
//            BigDecimal komisiORI = BDUtil.sub(h.getFieldValueByFieldNameBD("comm_entered"), h.getFieldValueByFieldNameBD("profitcomm_entered"));

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("inward"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("ar_trx_type_id").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("invoice_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("invoice_no"));
            if (h.getFieldValueByFieldNameST("trx_no_reference") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("trx_no_reference"));
            }
            if (h.getFieldValueByFieldNameST("attr_pol_no") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("attr_pol_no"));
            }
            if (h.getFieldValueByFieldNameDT("attr_pol_per_0") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("attr_pol_per_0"));
            }
            if (h.getFieldValueByFieldNameDT("attr_pol_per_1") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("attr_pol_per_1"));
            }
            if (h.getFieldValueByFieldNameBD("attr_pol_tsi_total") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_tsi_total").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("attr_pol_tsi") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_tsi").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("attr_quartal") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("attr_quartal"));
            }
            if (h.getFieldValueByFieldNameST("attr_underwrit") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("attr_underwrit"));
            }
            if (h.getFieldValueByFieldNameST("refid0") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("refid0"));
            }
            if (h.getFieldValueByFieldNameST("attr_pol_name") != null) {
                row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("attr_pol_name"));
            }
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("ccy"));
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("attr_pol_type_id").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameST("ent_name"));
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("comm").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("fee").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("claim").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("bfee").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("recoveries").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("reinstatement").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("profitcomm").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("ppn").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("pph23").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("hfee").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("mgfee").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("opfee").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("gfund").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("premi_entered").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("comm_entered").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("fee_entered").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("claim_entered").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("bfee_entered").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("recoveries_entered").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("reinstatement_entered").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("profitcomm_entered").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("ppn_entered").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("pph23_entered").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("hfee_entered").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("mgfee_entered").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("opfee_entered").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("gfund_entered").doubleValue());

            row.createCell(48).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(49).setCellValue(getPolicyTypeInward(h.getFieldValueByFieldNameBD("attr_pol_type_id").toString()).getStDescription());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameST("reinsured"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList INWARD_REKAP() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_trx_type_id,a.mutation_date,substr(a.mutation_date::text,6,2) as bulan,"
                + "sum(checkreas(d.ar_trx_line_id in (1,4,77,79,102,135),d.amount)) as premi,"
//                + "sum(checkreas(d.ar_trx_line_id in (2,5,89,80),d.amount)) as comm ","
                + "sum(checkreas(d.ar_trx_line_id in (80),d.amount)) as comm "
                );

        sqa.addQuery(" from ins_pol_inward a "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

//        sqa.addClause("a.ar_trx_type_id in (1,2,3,20)");
        sqa.addClause("(a.ar_trx_type_id in (1,2,3,20,21) or (a.claim_status = 'DLA' and a.ar_trx_type_id = 23))");
//        sqa.addClause("(a.ar_trx_type_id in (1,2,3,20,21,22) or (a.claim_status = 'DLA' and a.ar_trx_type_id in (17,18,19,23,25)))");
        sqa.addClause("a.approved_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        final String sql = "select b.ref2 as refx0,b.ref4 as refx1,"
                + "sum(getpremi2(a.ar_trx_type_id = 1,premi-comm)) as premi,"
                + "sum(getpremi2(a.ar_trx_type_id = 2,premi-comm)) as bpol,"
                + "sum(getpremi2(a.ar_trx_type_id = 3,premi-comm)) as bmat,"
                + "sum(getpremi2(a.ar_trx_type_id = 20,premi-comm)) as comm, "
                + "sum(getpremi2(a.ar_trx_type_id = 23,premi-comm)) as disc, "
                + "sum(premi-comm) as fee "
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

    public void EXCEL_INWARD_CLAIM() throws Exception {
        final boolean FLT_CLAIM_FAC = "Y".equalsIgnoreCase((String) refPropMap.get("FAC"));
        final boolean FLT_CLAIM_TREATY = "Y".equalsIgnoreCase((String) refPropMap.get("TREATY"));
        final boolean FLT_CLAIM_XOL = "Y".equalsIgnoreCase((String) refPropMap.get("XOL"));
        final boolean FLT_CLAIM_CL = "Y".equalsIgnoreCase((String) refPropMap.get("CL"));
        final boolean FLT_CLAIM_EXGRATIA = "Y".equalsIgnoreCase((String) refPropMap.get("EXGRATIA"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" c.description as description,a.mutation_date as approved_date,coalesce(d.refid0,a.refid0) as treaty,a.refd2 as dateloss,a.claim_no,"
                + "';'||a.invoice_no as nobukti,';'||coalesce(a.trx_no_reference,a.attr_pol_no) as nopolis,';'||a.pla_no as pla_no,';'||a.dla_no as dla_no,"
                + "b.ent_name as reasuradur,a.attr_pol_name as tertanggung,b.short_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as pol_type_id,a.ccy,a.ccy_rate,"
                + "sum(checkreas(d.ar_trx_line_id in ('74','75','76','108','109'),d.amount)) as klaim_idr,"
                + "sum(checkreas(d.ar_trx_line_id in ('101'),d.amount)) as adjfee_idr,"
                + "sum(checkreas(d.ar_trx_line_id in ('135'),d.amount)) as reinst_idr,"
                + "sum(checkreas(d.ar_trx_line_id in ('74','75','76','108','109'),d.entered_amount)) as klaim_ori,"
                + "sum(checkreas(d.ar_trx_line_id in ('101'),d.entered_amount)) as adjfee_ori, "
                + "sum(checkreas(d.ar_trx_line_id in ('135'),d.entered_amount)) as reinst_ori,"
                + "a.attr_underwrit,a.attr_pol_per_0,a.attr_pol_per_1,a.attr_pol_tsi,a.attr_pol_tsi_total,a.claim_amount_total ");

        sqa.addQuery(" from ins_pol_inward a "
                + "left join ent_master b on b.ent_id = a.ent_id "
                + "left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        //sqa.addClause("a.claim_status = 'DLA'");
        //sqa.addClause("a.approved_flag = 'Y'");

        if (Tools.isYes(stIndex)) {
            sqa.addClause(" a.claim_status = 'DLA' and a.approved_flag = 'Y'");
        }

        if (Tools.isNo(stIndex)) {
            sqa.addClause(" a.claim_status = 'DLA' and coalesce(a.approved_flag,'') <> 'Y'");
        }

        if (FLT_CLAIM_FAC) {
            sqa.addClause("a.ar_trx_type_id = 17");
        }

        if (FLT_CLAIM_TREATY) {
            sqa.addClause("a.ar_trx_type_id = 18");
        }

        if (FLT_CLAIM_XOL) {
            sqa.addClause("a.ar_trx_type_id in (19,25)");
        }

        if (FLT_CLAIM_CL) {
            sqa.addClause("a.ar_trx_type_id = 23");
        }

        if (FLT_CLAIM_EXGRATIA) {
            sqa.addClause("a.ar_trx_type_id = 24");
        }

        if (DLADateFrom != null) {
            sqa.addClause("date_trunc('day',a.dla_date) >= '" + DLADateFrom + "'");
            //sqa.addPar(DLADateFrom);
        }

        if (DLADateTo != null) {
            sqa.addClause("date_trunc('day',a.dla_date) <= '" + DLADateTo + "'");
            //sqa.addPar(DLADateTo);
        }

        if (approvedDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= '" + approvedDateFrom + "'");
            //sqa.addPar(approvedDateFrom);
        }

        if (approvedDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= '" + approvedDateTo + "'");
            //sqa.addPar(approvedDateTo);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.attr_pol_type_id = '" + stPolicyTypeID + "'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stFltTreatyType != null) {
            sqa.addClause("a.refid0 = '" + stFltTreatyType + "'");
            //sqa.addPar(stFltTreatyType);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = '" + stCompanyID + "'");
            //sqa.addPar(stCompanyID);
        }

        if (getStBussinessPolTypeCob() != null) {
            if (getStBussinessPolTypeCob().equalsIgnoreCase("1")) {
                sqa.addClause("a.attr_pol_type_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,86)");
            } else if (getStBussinessPolTypeCob().equalsIgnoreCase("2")) {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80,97)");
            } else {
                sqa.addClause("a.attr_pol_type_id in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,60,73,74,75,76,77,78,80,97)");
            }
        }

        String sql = sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.mutation_date,a.invoice_date,a.attr_underwrit,a.attr_pol_per_0,a.attr_pol_per_1,a.attr_pol_tsi,a.claim_amount_total, "
                + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,a.attr_pol_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate,a.refd2,a.claim_no "
                + "order by a.ar_trx_type_id,a.attr_pol_type_id,b.reas_ent_id ";

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        InsurancePolicyView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;*/

        SQLUtil S = new SQLUtil();

        String nama_file = "inwardclaim_" + System.currentTimeMillis() + ".csv";

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

    public DTOList EXCESSXOL_CREDIT() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.trx_no_reference,a.ent_id,a.description,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = '1',coalesce(d.amount,0))) as refn1,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = '2',coalesce(d.amount,0))) as refn2,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = '3',coalesce(d.amount,0))) as refn3,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = '4',coalesce(d.amount,0))) as refn4,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = '5',coalesce(d.amount,0))) as refn5,"
                + "sum(checkreas(d.ar_trx_line_id = '102' and a.refa0 = 'SUBLAYER',coalesce(d.amount,0))) as refn6 ");

        sqa.addQuery(" from ins_pol_inward a "
                + " left join ent_master b on b.ent_id = a.ent_id "
                + " inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + " left join ent_master e on e.ent_id = a.reins_ent_id ");

        sqa.addClause(" (ar_trx_type_id = 22 or ar_trx_type_id in (select ar_trx_type_id from ar_trx_type where parent_trx_id = 22)) ");
        sqa.addClause("a.approved_flag = 'Y'");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        String sql = sqa.getSQL() + " group by a.trx_no_reference,a.ent_id,a.description "
                + " order by a.trx_no_reference ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_REKAP_INWARD_ALL() throws Exception {
        final boolean COMPANY = "Y".equalsIgnoreCase((String) refPropMap.get("COMPANY"));
        final boolean JENPOL = "Y".equalsIgnoreCase((String) refPropMap.get("JENPOL"));

//        String rekap = null;
        String rekap2 = null;
//
//        if (COMPANY) {
//            rekap = "b.ent_id,b.ent_name";
//        } else if (JENPOL) {
//            rekap = "coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as attr_pol_type_id,e.description";
//        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.ent_id,b.ent_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as attr_pol_type_id,e.description,"
                + "sum(getpremi2(d.ar_trx_line_id = 1, d.amount))as premi_fac,"
                + "sum(getpremi2(d.ar_trx_line_id = 2, d.amount))as komisi_fac,"
                + "sum(getpremi2(d.ar_trx_line_id = 3, d.amount))as fee_fac,"
                + "sum(getpremi2(d.ar_trx_line_id = 4, d.amount)) as premi_treaty,"
                + "sum(getpremi2(d.ar_trx_line_id = 5, d.amount)) as komisi_treaty,"
                + "sum(getpremi2(d.ar_trx_line_id = 6, d.amount)) as klaim_treaty,"
                + "sum(getpremi2(d.ar_trx_line_id = 127, d.amount)) as bfee_treaty,"
                + "sum(getpremi2(d.ar_trx_line_id = 128, d.amount)) as recov_treaty,"
                + "sum(getpremi2(d.ar_trx_line_id = 77, d.amount)) as premi_xol,"
                + "sum(getpremi2(d.ar_trx_line_id = 89, d.amount)) as komisi_xol,"
                + "sum(getpremi2(d.ar_trx_line_id = 79, d.amount)) as premi_prof_inw,"
                + "sum(getpremi2(d.ar_trx_line_id = 80, d.amount)) as komisi_prof_inw,"
                + "sum(getpremi2(d.ar_trx_line_id = 81, d.amount)) as klaim_prof_inw,"
                + "sum(getpremi2(d.ar_trx_line_id = 100, d.amount)) as komisi_inw,"
                + "sum(getpremi2(d.ar_trx_line_id = 97, d.amount)) as premi_prof_outw,"
                + "sum(getpremi2(d.ar_trx_line_id = 98, d.amount)) as adpremi_prof_outw,"
                + "sum(getpremi2(d.ar_trx_line_id = 99, d.amount)) as lesspremi_prof_outw,"
                + "sum(getpremi2(d.ar_trx_line_id = 102, d.amount)) as premi_outw_xol ");

        sqa.addQuery(" from ins_pol_inward a "
                + "inner join ent_master b on b.ent_id = a.ent_id "
                + "inner join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id "
                + "inner join ins_policy_types e on e.pol_type_id = coalesce(a.attr_pol_type_id,d.attr_pol_type_id) ");

        //sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (1,2,3,20,21,22)");

        if (policyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(policyDateFrom);
        }

        if (policyDateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(policyDateTo);
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
                sqa.addClause("coalesce(a.attr_pol_type_id,d.attr_pol_type_id) in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,23,24,31,32,33,35,36,37,41,42,43,44,61,62,63,64,65,66,67,68,69,70,71,72,81,82,83,84,85,92,95,96,97)");
            } else {
                sqa.addClause("coalesce(a.attr_pol_type_id,d.attr_pol_type_id) in (21,38,45,46,47,48,51,52,53,54,55,56,57,58,59,60,73,74,75,76,77,78,80)");
            }
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("coalesce(a.attr_pol_type_id,d.attr_pol_type_id) = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stCompanyID != null) {
            sqa.addClause("a.ent_id = ?");
            sqa.addPar(stCompanyID);
        }

        if (Tools.isYes(stApproved)) {
            sqa.addClause("a.approved_flag = 'Y'");
        }

        if (COMPANY) {
            rekap2 = "select a.ent_id as id,a.ent_name as name";
        } else if (JENPOL) {
            rekap2 = "select a.attr_pol_type_id as id,a.description as name";
        }

        String sql = rekap2 + ",sum(premi_fac) as premi_fac,sum(komisi_fac) as komisi_fac,"
                + "sum(fee_fac) as fee_fac,sum(premi_treaty) as premi_treaty,"
                + "sum(komisi_treaty) as komisi_treaty,sum(klaim_treaty) as klaim_treaty,"
                + "sum(bfee_treaty) as bfee_treaty,sum(recov_treaty) as recov_treaty,"
                + "sum(premi_xol) as premi_xol,sum(komisi_xol) as komisi_xol,"
                + "sum(premi_prof_inw) as premi_prof_inw,sum(komisi_prof_inw) as komisi_prof_inw,"
                + "sum(klaim_prof_inw) as klaim_prof_inw,sum(komisi_inw) as komisi_inw,"
                + "sum(premi_prof_outw) as premi_prof_outw,sum(adpremi_prof_outw) as adpremi_prof_outw,"
                + "sum(lesspremi_prof_outw) as lesspremi_prof_outw,sum(premi_outw_xol) as premi_outw_xol "
                + "from ( " + sqa.getSQL() + " group by 1,2,3,4 order by 1,3 ";

        if (COMPANY) {
            sql = sql + " ) a group by a.ent_id,a.ent_name "
                    + "order by a.ent_id ";
        } else if (JENPOL) {
            sql = sql + " ) a group by a.attr_pol_type_id,a.description "
                    + "order by a.attr_pol_type_id ";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_REKAP_INWARD_ALL() throws Exception {
        final boolean COMPANY = "Y".equalsIgnoreCase((String) refPropMap.get("COMPANY"));
        final boolean JENPOL = "Y".equalsIgnoreCase((String) refPropMap.get("JENPOL"));

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
//            XSSFRow row1 = sheet.createRow(0);
//            row1.createCell(0).setCellValue("Tanggal : " + DateUtil.getDateStr(getPolicyDateFrom()) + " s/d " + DateUtil.getDateStr(getPolicyDateTo()));

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            if (COMPANY) {
                row0.createCell(1).setCellValue("ID");
                row0.createCell(2).setCellValue("produk");
            } else if (JENPOL) {
                row0.createCell(1).setCellValue("jenid");
                row0.createCell(2).setCellValue("jenpol");
            }
            row0.createCell(3).setCellValue("facultatif");
            row0.createCell(6).setCellValue("treaty");
            row0.createCell(11).setCellValue("xol");
            row0.createCell(13).setCellValue("profit comm inw treaty");
            row0.createCell(17).setCellValue("profit comm outw treaty");
            row0.createCell(20).setCellValue("outward xol");

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 10));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 16));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 19));

            //bikin header
            XSSFRow row1 = sheet.createRow(1);
            row1.createCell(3).setCellValue("premi");
            row1.createCell(4).setCellValue("komisi");
            row1.createCell(5).setCellValue("fee");
            row1.createCell(6).setCellValue("premi");
            row1.createCell(7).setCellValue("komisi");
            row1.createCell(8).setCellValue("klaim");
            row1.createCell(9).setCellValue("bfee");
            row1.createCell(10).setCellValue("recov");
            row1.createCell(11).setCellValue("premi");
            row1.createCell(12).setCellValue("komisi");
            row1.createCell(13).setCellValue("premi");
            row1.createCell(14).setCellValue("komisi");
            row1.createCell(15).setCellValue("klaim");
            row1.createCell(16).setCellValue("komisi");
            row1.createCell(17).setCellValue("premi");
            row1.createCell(18).setCellValue("adpremi");
            row1.createCell(19).setCellValue("lesspremi");
            row1.createCell(20).setCellValue("premi");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 2);
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("id").doubleValue());
            row.createCell(2).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("name")));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("premi_fac").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("komisi_fac").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("fee_fac").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("premi_treaty").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("komisi_treaty").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("klaim_treaty").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("bfee_treaty").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("recov_treaty").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("premi_xol").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("komisi_xol").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("premi_prof_inw").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("komisi_prof_inw").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("klaim_prof_inw").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("komisi_inw").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("premi_prof_outw").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("adpremi_prof_outw").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("lesspremi_prof_outw").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("premi_outw_xol").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public InsurancePolicyTypeView getPolicyType(String stPolicyTypeID) {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
    }

    public InsurancePolicyTypeInwardView getPolicyTypeInward(String stPolicyTypeID) {
        return (InsurancePolicyTypeInwardView) DTOPool.getInstance().getDTO(InsurancePolicyTypeInwardView.class, stPolicyTypeID);
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
}

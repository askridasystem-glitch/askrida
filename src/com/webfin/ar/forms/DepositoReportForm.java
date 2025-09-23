/***********************************************************************
 * Module:  com.webfin.ar.forms.DepositoReportForm
 * Author:  Denny Mahendra
 * Created: Mar 7, 2007 10:55:04 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;
import com.crux.lov.LOVManager;
import com.webfin.ar.model.ARInvestmentBungaView;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentPencairanView;
import java.util.Date;
import com.crux.util.*;
import com.crux.common.model.*;

import com.crux.web.form.Form;
import com.webfin.ar.model.ARRequestFee;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import java.io.DataInputStream;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

public class DepositoReportForm extends Form {

    ARInvestmentBungaView bunga;
    private String stReportType;
    private String stPrintForm;
    private String stReport;
    private String ref1;
    private String status;
    private String stLang;
    private String stReportTitle;
    private String stFileName;
    private HashMap refPropMap;
    private static HashSet formList = null;
    private boolean enableSelectForm = true;
    private Date DepoDateFrom;
    private Date DepoDateTo;
    private Date CairDateFrom;
    private Date CairDateTo;
    private Date BungaDateFrom;
    private Date BungaDateTo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stBranchDesc = SessionManager.getInstance().getSession().getCostCenterDesc();
    private String stBranchName;
    private String stCompTypeID;
    private String stCompTypeName;
    private String stReceiptClassID;
    private String stEntityID;
    private String stEntityName;
    private Date dtTglAwalFrom;
    private Date dtTglAkhirFrom;
    private String stStatusFile;
    private String stDescription;
    private String stActiveFlag;
    private String stStatusID;
    private String stStatusName;
    private Date PeriodDateFrom;
    private Date PeriodDateTo;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    private boolean canNavigateRegion = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVREG");
    private Date PengajuanFrom;
    private Date PengajuanTo;
    private Date RealisasiFrom;
    private Date RealisasiTo;
    private Date ValidasiFrom;
    private Date ValidasiTo;

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

    private DTOList loadList() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_DEPOSITO", stReport);

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

        stReportTitle = LOVManager.getInstance().getDescription(stPrintForm, "VS_PROD_DEPOSITO");
    }

    public void initialize() {
        setTitle("REPORT");

        stReportType = (String) getAttribute("rpt");
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
        ref1 = LOVManager.getInstance().getRef1("PROD_DEPOSITO", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_DEPOSITO", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

    private DTOList loadListExcel() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_DEPOSITO", stReport);

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

    public Date getDepoDateFrom() {
        return DepoDateFrom;
    }

    public void setDepoDateFrom(Date DepoDateFrom) {
        this.DepoDateFrom = DepoDateFrom;
    }

    public Date getDepoDateTo() {
        return DepoDateTo;
    }

    public void setDepoDateTo(Date DepoDateTo) {
        this.DepoDateTo = DepoDateTo;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public String getStBranchDesc() {
        return stBranchDesc;
    }

    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
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

    public DTOList PEMBENTUKAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_deposito a ");

        //sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        sqa.addClause("status = 'DEPOSITO'");
        sqa.addClause("journal_status = 'NEW'");

        if (DepoDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tgldepo) >= ?");
            sqa.addPar(DepoDateFrom);
        }

        if (DepoDateTo != null) {
            sqa.addClause("date_trunc('day',a.tgldepo) <= ?");
            sqa.addPar(DepoDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_depo_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentDepositoView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList PENCAIRAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_pencairan a ");

        //sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        if (CairDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tglcair) >= ?");
            sqa.addPar(CairDateFrom);
        }

        if (CairDateTo != null) {
            sqa.addClause("date_trunc('day',a.tglcair) <= ?");
            sqa.addPar(CairDateTo);
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause("date_trunc('day',a.tglawal) <= ?");
            sqa.addPar(dtTglAwalFrom);
        }

        if (dtTglAkhirFrom != null) {
            sqa.addClause("date_trunc('day',a.tglakhir) >= ?");
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addClause("date_trunc('day',a.tglakhir) <= ?");
            sqa.addPar(dtTglAkhirFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_cair_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentPencairanView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList BUNGA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_bunga a ");

        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.delete_flag is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        if (BungaDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tglbunga) >= ?");
            sqa.addPar(BungaDateFrom);
        }

        if (BungaDateTo != null) {
            sqa.addClause("date_trunc('day',a.tglbunga) <= ?");
            sqa.addPar(BungaDateTo);
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause("date_trunc('day',a.tglawal) <= ?");
            sqa.addPar(dtTglAwalFrom);
        }

        if (dtTglAkhirFrom != null) {
            sqa.addClause("date_trunc('day',a.tglakhir) >= ?");
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addClause("date_trunc('day',a.tglakhir) <= ?");
            sqa.addPar(dtTglAkhirFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_bunga_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentBungaView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_PEMBENTUKAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||nodefo as nodefo,hari,bulan,kodedepo,tglawal,tgldepo,nominal,"
                + "d.description as nama_depo,c.description as nama_bank,bunga,"
                + "ket,bukti_b,account_depo,account_bank ");

        sqa.addQuery(" from ar_inv_deposito a "
                + "inner join gl_accounts c on c.account_id = a.kdbank "
                + "inner join gl_accounts d on d.account_id = a.norekdep ");

        //sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        sqa.addClause("status = 'DEPOSITO'");
        sqa.addClause("journal_status = 'NEW'");

        if (DepoDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tgldepo) >= ?");
            sqa.addPar(DepoDateFrom);
        }

        if (DepoDateTo != null) {
            sqa.addClause("date_trunc('day',a.tgldepo) <= ?");
            sqa.addPar(DepoDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_depo_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_PEMBENTUKAN() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String waktu = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("bilyet");
            row0.createCell(1).setCellValue("waktu");
            row0.createCell(2).setCellValue("tanggal awal");
            row0.createCell(3).setCellValue("tanggal pendebetan");
            row0.createCell(4).setCellValue("nominal");
            row0.createCell(5).setCellValue("bank");
            row0.createCell(6).setCellValue("rate bunga");
            row0.createCell(7).setCellValue("keterangan");
            row0.createCell(8).setCellValue("no bukti");
            row0.createCell(9).setCellValue("akun depo");
            row0.createCell(10).setCellValue("akun bank");

            if (h.getFieldValueByFieldNameST("kodedepo").equalsIgnoreCase("1")) {
                waktu = h.getFieldValueByFieldNameST("hari") + " hari";
            } else {
                waktu = h.getFieldValueByFieldNameST("bulan") + " bulan";
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(1).setCellValue(waktu);
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tgldepo"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            if (h.getFieldValueByFieldNameST("ket") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ket"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("account_bank"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public Date getCairDateFrom() {
        return CairDateFrom;
    }

    public void setCairDateFrom(Date CairDateFrom) {
        this.CairDateFrom = CairDateFrom;
    }

    public Date getCairDateTo() {
        return CairDateTo;
    }

    public void setCairDateTo(Date CairDateTo) {
        this.CairDateTo = CairDateTo;
    }

    public Date getBungaDateFrom() {
        return BungaDateFrom;
    }

    public void setBungaDateFrom(Date BungaDateFrom) {
        this.BungaDateFrom = BungaDateFrom;
    }

    public Date getBungaDateTo() {
        return BungaDateTo;
    }

    public void setBungaDateTo(Date BungaDateTo) {
        this.BungaDateTo = BungaDateTo;
    }

    public String getStReceiptClassID() {
        return stReceiptClassID;
    }

    public void setStReceiptClassID(String stReceiptClassID) {
        this.stReceiptClassID = stReceiptClassID;
    }

    public void refresh() {
    }

    public String getStBranchName() {
        return stBranchName;
    }

    public void setStBranchName(String stBranchName) {
        this.stBranchName = stBranchName;
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

    public Date getDtTglAwalFrom() {
        return dtTglAwalFrom;
    }

    public void setDtTglAwalFrom(Date dtTglAwalFrom) {
        this.dtTglAwalFrom = dtTglAwalFrom;
    }

    public Date getDtTglAkhirFrom() {
        return dtTglAkhirFrom;
    }

    public void setDtTglAkhirFrom(Date dtTglAkhirFrom) {
        this.dtTglAkhirFrom = dtTglAkhirFrom;
    }

    public DTOList DEPOSITO() throws Exception {
        final boolean FLT_AKTIF = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_AKTIF"));
        final boolean FLT_JTEMPO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_JTEMPO"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_deposito a ");

        if (FLT_AKTIF) {
            sqa.addClause(" a.effective_flag = 'Y'");
            sqa.addClause(" a.deleted is null");
            //sqa.addClause(" date_trunc('day',a.tglawal) >= '" + DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01'");
            //sqa.addClause(" date_trunc('day',a.tglawal) <= '" + dtTglAwalFrom + "'");

            sqa.addClause("((date_trunc('day',a.tglakhir) > '" + dtTglAwalFrom + "'"
                    + "and date_trunc('day',a.tglawal) < '" + dtTglAwalFrom + "' ) or "
                    + "(date_trunc('day',a.tglawal) >= '" + DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01'"
                    + "and date_trunc('day',a.tglawal) <= '" + dtTglAwalFrom + "' ))");

            sqa.addClause("(a.tglcair is null or (date_trunc('MONTH',a.tglawal) < date_trunc('MONTH',a.tglcair)))");
        }

        if (FLT_JTEMPO) {
            //sqa.addClause(" a.active_flag = 'Y'");
            sqa.addClause(" a.deleted is null");
            //sqa.addClause(" date_trunc('day',a.tglakhir) >= ?");
            //sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            //sqa.addClause(" date_trunc('day',a.tglakhir) <= ?");
            //sqa.addPar(dtTglAkhirFrom);
            //sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > ?)");
            //sqa.addPar(DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01");


            sqa.addClause("((date_trunc('day',a.tglakhir) >= ? "
                    + "and date_trunc('day',a.tglakhir) <= ? ) or "
                    + "(date_trunc('day',a.tglakhir) >= ? "
                    + "and date_trunc('day',a.tglawal) <= ? ))");
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addPar(dtTglAkhirFrom);
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addPar(dtTglAkhirFrom);
            sqa.addClause("(a.tglcair is null or (date_trunc('MONTH',a.tglakhir) < date_trunc('MONTH',a.tglcair)))");
        }

        sqa.addClause("a.effective_flag = 'Y'");

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatusName);
        }

        final String sql = sqa.getSQL() + " order by a.ar_depo_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentDepositoView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public String getStStatusFile() {
        return stStatusFile;
    }

    public void setStStatusFile(String stStatusFile) {
        this.stStatusFile = stStatusFile;
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

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public String getStStatusID() {
        return stStatusID;
    }

    public void setStStatusID(String stStatusID) {
        this.stStatusID = stStatusID;
    }

    public String getStStatusName() {
        return stStatusName;
    }

    public void setStStatusName(String stStatusName) {
        this.stStatusName = stStatusName;
    }

    public void EXCEL_DEPOSITO() throws Exception {
        final boolean FLT_AKTIF = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_AKTIF"));
        final boolean FLT_JTEMPO = "Y".equalsIgnoreCase((String) refPropMap.get("FLT_JTEMPO"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||a.nodefo as bilyet,(case when a.kodedepo = '1' then 'hari' else 'bulan' end) as waktu,"
                + "a.tglawal,a.tglakhir,a.tgldepo as tgldebet,a.norekdep,a.nominal,a.nama_bank as bank,a.bunga,a.ket as keterangan,a.bukti_b as buktib,a.account_bank,a.nama_bank,a.account_depo,a.nama_depo ");

        sqa.addQuery(" from ar_inv_deposito a ");

        if (FLT_AKTIF) {
            sqa.addClause(" a.effective_flag = 'Y'");
            sqa.addClause(" a.deleted is null");
            //sqa.addClause(" date_trunc('day',a.tglawal) >= '" + DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01'");
            //sqa.addClause(" date_trunc('day',a.tglawal) <= '" + dtTglAwalFrom + "'");

            sqa.addClause("((date_trunc('day',a.tglakhir) > '" + dtTglAwalFrom + "'"
                    + "and date_trunc('day',a.tglawal) < '" + dtTglAwalFrom + "' ) or "
                    + "(date_trunc('day',a.tglawal) >= '" + DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01'"
                    + "and date_trunc('day',a.tglawal) <= '" + dtTglAwalFrom + "' ))");

            sqa.addClause("(a.tglcair is null or (date_trunc('MONTH',a.tglawal) < date_trunc('MONTH',a.tglcair)))");
        }

        if (FLT_JTEMPO) {
            sqa.addClause(" a.effective_flag = 'Y'");
            sqa.addClause(" a.deleted is null");
            //sqa.addClause(" date_trunc('day',a.tglakhir) >= ?");
            //sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            //sqa.addClause(" date_trunc('day',a.tglakhir) <= ?");
            //sqa.addPar(dtTglAkhirFrom);
            //sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > ?)");
            //sqa.addPar(DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01");


            sqa.addClause("((date_trunc('day',a.tglakhir) >= '" + DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01'"
                    + "and date_trunc('day',a.tglakhir) <= '" + dtTglAkhirFrom + "' ) or "
                    + "(date_trunc('day',a.tglakhir) >= '" + DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01'"
                    + "and date_trunc('day',a.tglawal) <= '" + dtTglAkhirFrom + "' ))");
            //sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            //sqa.addPar(dtTglAkhirFrom);
            //sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            //sqa.addPar(dtTglAkhirFrom);
            sqa.addClause("(a.tglcair is null or (date_trunc('MONTH',a.tglakhir) < date_trunc('MONTH',a.tglcair)))");
        }

        sqa.addClause("a.effective_flag = 'Y'");

        if (stBranch != null) {
            sqa.addClause("a.koda = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = '" + stReceiptClassID + "'");
            //sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = '" + stCompTypeID + "'");
            //sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = '" + stStatusName + "'");
            //sqa.addPar(stStatusName);
        }

        String sql = sqa.getSQL() + " order by a.ar_depo_id";

        String nama_file = null;
        nama_file = "deposito_" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    public void EXPORT_DEPOSITO() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String waktu = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("bilyet");
            row0.createCell(1).setCellValue("waktu");
            row0.createCell(2).setCellValue("tanggal awal");
            row0.createCell(3).setCellValue("tanggal akhir");
            row0.createCell(4).setCellValue("tanggal pendebetan");
            row0.createCell(5).setCellValue("nominal");
            row0.createCell(6).setCellValue("bank");
            row0.createCell(7).setCellValue("rate bunga");
            row0.createCell(8).setCellValue("keterangan");
            row0.createCell(9).setCellValue("no bukti");
            row0.createCell(10).setCellValue("akun depo");
            row0.createCell(11).setCellValue("akun bank");

            if (h.getFieldValueByFieldNameST("kodedepo").equalsIgnoreCase("1")) {
                waktu = h.getFieldValueByFieldNameST("hari") + " hari";
            } else {
                waktu = h.getFieldValueByFieldNameST("bulan") + " bulan";
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(1).setCellValue(waktu);
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tglakhir"));
            if (h.getFieldValueByFieldNameDT("tgldepo") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("tgldepo"));
            }
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            if (h.getFieldValueByFieldNameST("ket") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("ket"));
            }
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("account_bank"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_BUNGA() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.* ");
        sqa.addSelect(" ';'||nodefo as nodefo,tglbunga,ar_depo_id,nominal,angka,angka1,account_bank,"
                + "nama_bank,account_depo,nama_depo,ket,bukti_d,bukti_b ");

        sqa.addQuery(" from ar_inv_bunga a ");

        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.delete_flag is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        if (BungaDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tglbunga) >= ?");
            sqa.addPar(BungaDateFrom);
        }

        if (BungaDateTo != null) {
            sqa.addClause("date_trunc('day',a.tglbunga) <= ?");
            sqa.addPar(BungaDateTo);
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause("date_trunc('day',a.tglawal) <= ?");
            sqa.addPar(dtTglAwalFrom);
        }

        if (dtTglAkhirFrom != null) {
            sqa.addClause("date_trunc('day',a.tglakhir) >= ?");
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addClause("date_trunc('day',a.tglakhir) <= ?");
            sqa.addPar(dtTglAkhirFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_bunga_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_BUNGA() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("bilyet");
            row0.createCell(1).setCellValue("tanggal bunga");
            row0.createCell(2).setCellValue("tanggal bentuk");
            row0.createCell(3).setCellValue("nominal");
            row0.createCell(4).setCellValue("nominal entry");
            row0.createCell(5).setCellValue("nominal hitung");
            row0.createCell(6).setCellValue("rekening bank");
            row0.createCell(7).setCellValue("bank");
            row0.createCell(8).setCellValue("rekening bentuk");
            row0.createCell(9).setCellValue("deposito");
            row0.createCell(10).setCellValue("keterangan");
            row0.createCell(11).setCellValue("no bukti bunga");
            row0.createCell(12).setCellValue("no bukti bentuk");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("tglbunga"));
            row.createCell(2).setCellValue(getDeposito(h.getFieldValueByFieldNameBD("ar_depo_id").toString()).getDtTgldepo());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("angka").doubleValue());
            if (h.getFieldValueByFieldNameBD("angka1") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("angka1").doubleValue());
            }
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("account_bank"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("nama_depo"));
            if (h.getFieldValueByFieldNameST("ket") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ket"));
            }
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bukti_d"));
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_PENCAIRAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //sqa.addSelect(" a.* ");
        sqa.addSelect(" ';'||nodefo as nodefo,hari,bulan,kodedepo,tglawal,tglcair,nominal,nama_bank,bunga,"
                + "ket,bukti_b,bukti_c,account_depo,account_bank ");

        sqa.addQuery(" from ar_inv_pencairan a ");

        //sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");

        /*
        if (Tools.isYes(stActiveFlag)) {
        sqa.addClause("a.active_flag = 'Y'");
        }

        if (Tools.isNo(stActiveFlag)) {
        sqa.addClause("(a.active_flag = 'N' or a.active_flag = 'Y')");
        }
         */

        if (CairDateFrom != null) {
            sqa.addClause("date_trunc('day',a.tglcair) >= ?");
            sqa.addPar(CairDateFrom);
        }

        if (CairDateTo != null) {
            sqa.addClause("date_trunc('day',a.tglcair) <= ?");
            sqa.addPar(CairDateTo);
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause("date_trunc('day',a.tglawal) <= ?");
            sqa.addPar(dtTglAwalFrom);
        }

        if (dtTglAkhirFrom != null) {
            sqa.addClause("date_trunc('day',a.tglakhir) >= ?");
            sqa.addPar(DateUtil.getYear(dtTglAkhirFrom) + "-" + DateUtil.getMonth2Digit(dtTglAkhirFrom) + "-01");
            sqa.addClause("date_trunc('day',a.tglakhir) <= ?");
            sqa.addPar(dtTglAkhirFrom);
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        final String sql = sqa.getSQL() + " order by a.ar_cair_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_PENCAIRAN() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String waktu = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("bilyet");
            row0.createCell(1).setCellValue("waktu");
            row0.createCell(2).setCellValue("tanggal awal");
            row0.createCell(3).setCellValue("tanggal pencairan");
            row0.createCell(4).setCellValue("nominal");
            row0.createCell(5).setCellValue("bank");
            row0.createCell(6).setCellValue("rate bunga");
            row0.createCell(7).setCellValue("keterangan");
            row0.createCell(8).setCellValue("no bukti");
            row0.createCell(9).setCellValue("akun depo");
            row0.createCell(10).setCellValue("akun bank");
            row0.createCell(11).setCellValue("no bukti bentuk");

            if (h.getFieldValueByFieldNameST("kodedepo").equalsIgnoreCase("1")) {
                waktu = h.getFieldValueByFieldNameST("hari") + " hari";
            } else {
                waktu = h.getFieldValueByFieldNameST("bulan") + " bulan";
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(1).setCellValue(waktu);
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("tglcair"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            if (h.getFieldValueByFieldNameST("ket") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ket"));
            }
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("bukti_c"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("account_bank"));
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList BUNGA_DEPOSITO() throws Exception {

        DateTime startDate = new DateTime(dtTglAwalFrom);
        DateTime endDate = new DateTime();

        endDate = startDate.plusMonths(1);

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_bunga a ");

        sqa.addClause(" a.active_flag = 'Y' ");
        sqa.addClause(" a.effective_flag = 'Y' ");
        sqa.addClause(" a.delete_flag is null");

        if (dtTglAwalFrom != null) {
            sqa.addClause(" date_trunc('day',a.tglbunga) >= ?");
            sqa.addPar(DateUtil.getYear(endDate.toDate()) + "-" + DateUtil.getMonth2Digit(endDate.toDate()) + "-01");
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause(" date_trunc('day',a.tglbunga) <= ?");
            sqa.addPar(endDate.toDate());
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatusName);
        }

        String sql = " select b.bukti_d,b.tglbunga,b.angka,a.* "
                + " from ar_inv_deposito a "
                + " left join ( " + sqa.getSQL() + " ) b on b.ar_depo_id = a.ar_depo_id "
                + " where a.tglcair is null and a.effective_flag = 'Y' ";

        if (getDtTglAwalFrom() != null) {
            sql = sql + " and date_trunc('day',a.tglawal) >= ? ";
            sqa.addPar(DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01");
        }

        if (getDtTglAwalFrom() != null) {
            sql = sql + " and date_trunc('day',a.tglawal) <= ? ";
            sqa.addPar(dtTglAwalFrom);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.koda = ? ";
            sqa.addPar(stBranch);
        }

        if (getStReceiptClassID() != null) {
            sql = sql + " and a.rc_id = ? ";
            sqa.addPar(stReceiptClassID);
        }

        if (getStCompTypeID() != null) {
            sql = sql + " and a.comp_type = ? ";
            sqa.addPar(stCompTypeID);
        }

        if (getStEntityID() != null) {
            sql = sql + " and a.kdbank = ? ";
            sqa.addPar(stEntityID);
        }

        if (getStStatusID() != null) {
            sql = sql + " and a.status = ? ";
            sqa.addPar(stStatusName);
        }

        sql = sql + " order by a.nodefo ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentDepositoView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList EXCEL_BUNGA_DEPOSITO() throws Exception {

        DateTime startDate = new DateTime(dtTglAwalFrom);
        DateTime endDate = new DateTime();

        endDate = startDate.plusMonths(1);

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_bunga a ");

        sqa.addClause(" a.active_flag = 'Y' ");
        sqa.addClause(" a.effective_flag = 'Y' ");
        sqa.addClause(" a.delete_flag is null");

        if (dtTglAwalFrom != null) {
            sqa.addClause(" date_trunc('day',a.tglbunga) >= ?");
            sqa.addPar(DateUtil.getYear(endDate.toDate()) + "-" + DateUtil.getMonth2Digit(endDate.toDate()) + "-01");
        }

        if (dtTglAwalFrom != null) {
            sqa.addClause(" date_trunc('day',a.tglbunga) <= ?");
            sqa.addPar(endDate.toDate());
        }

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatusName);
        }

        String sql = " select b.ar_bunga_id,b.bukti_d,b.tglbunga,b.angka,a.* "
                + " from ar_inv_deposito a "
                + " left join ( " + sqa.getSQL() + " ) b on b.ar_depo_id = a.ar_depo_id "
                + " where a.tglcair is null and a.effective_flag = 'Y' ";

        if (getDtTglAwalFrom() != null) {
            sql = sql + " and date_trunc('day',a.tglawal) >= ? ";
            sqa.addPar(DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01");
        }

        if (getDtTglAwalFrom() != null) {
            sql = sql + " and date_trunc('day',a.tglawal) <= ? ";
            sqa.addPar(dtTglAwalFrom);
        }

        if (getStBranch() != null) {
            sql = sql + " and a.koda = ? ";
            sqa.addPar(stBranch);
        }

        if (getStReceiptClassID() != null) {
            sql = sql + " and a.rc_id = ? ";
            sqa.addPar(stReceiptClassID);
        }

        if (getStCompTypeID() != null) {
            sql = sql + " and a.comp_type = ? ";
            sqa.addPar(stCompTypeID);
        }

        if (getStEntityID() != null) {
            sql = sql + " and a.kdbank = ? ";
            sqa.addPar(stEntityID);
        }

        if (getStStatusID() != null) {
            sql = sql + " and a.status = ? ";
            sqa.addPar(stStatusName);
        }

        sql = sql + " order by a.nodefo ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_BUNGA_DEPOSITO() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String waktu = null;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("koda");
            row0.createCell(1).setCellValue("bilyet");
            row0.createCell(2).setCellValue("bukti b");
            row0.createCell(3).setCellValue("waktu");
            row0.createCell(4).setCellValue("tanggal awal");
            row0.createCell(5).setCellValue("tanggal akhir");
            row0.createCell(6).setCellValue("tanggal pendebetan");
            row0.createCell(7).setCellValue("nominal");
            row0.createCell(8).setCellValue("bank");
            row0.createCell(9).setCellValue("rate bunga");
            row0.createCell(10).setCellValue("keterangan");
            row0.createCell(11).setCellValue("bukti d");
            row0.createCell(12).setCellValue("tanggal bunga");
            row0.createCell(13).setCellValue("angka");
            row0.createCell(14).setCellValue("angka");

            if (h.getFieldValueByFieldNameST("kodedepo").equalsIgnoreCase("1")) {
                waktu = h.getFieldValueByFieldNameST("hari") + " hari";
            } else {
                waktu = h.getFieldValueByFieldNameST("bulan") + " bulan";
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));
            row.createCell(3).setCellValue(waktu);
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("tglakhir"));
            if (h.getFieldValueByFieldNameDT("tgldepo") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgldepo"));
            }
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("nominal").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("nama_bank"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("bunga").doubleValue());
            if (h.getFieldValueByFieldNameST("ket") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("ket"));
            }
            if (h.getFieldValueByFieldNameST("bukti_d") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bukti_d"));
            }
            if (h.getFieldValueByFieldNameDT("tglbunga") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameDT("tglbunga"));
            }
            if (h.getFieldValueByFieldNameBD("angka") != null) {
                row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("angka").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public ARInvestmentDepositoView getDeposito(String stARDepoID) {
        final ARInvestmentDepositoView deposito = (ARInvestmentDepositoView) DTOPool.getInstance().getDTO(ARInvestmentDepositoView.class, stARDepoID);

        return deposito;
    }

    /**
     * @return the PeriodDateFrom
     */
    public Date getPeriodDateFrom() {
        return PeriodDateFrom;
    }

    /**
     * @param PeriodDateFrom the PeriodDateFrom to set
     */
    public void setPeriodDateFrom(Date PeriodDateFrom) {
        this.PeriodDateFrom = PeriodDateFrom;
    }

    /**
     * @return the PeriodDateTo
     */
    public Date getPeriodDateTo() {
        return PeriodDateTo;
    }

    /**
     * @param PeriodDateTo the PeriodDateTo to set
     */
    public void setPeriodDateTo(Date PeriodDateTo) {
        this.PeriodDateTo = PeriodDateTo;
    }

    public DTOList FEE_APPROVED() throws Exception {
        final boolean ACT = "Y".equalsIgnoreCase((String) refPropMap.get("ACT"));
        final boolean NACT = "Y".equalsIgnoreCase((String) refPropMap.get("NACT"));
        final boolean CASHBACK = "Y".equalsIgnoreCase((String) refPropMap.get("CASHBACK"));

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_request_fee a ");
        sqa.addClause(" a.deleted is null");

        if (ACT) {
            sqa.addClause(" a.status = 'PENGAJUAN'");
            sqa.addClause(" a.act_flag = 'Y'");
            sqa.addClause(" a.eff_flag = 'Y'");
            sqa.addClause(" a.validasi_f = 'Y'");
            sqa.addClause("coalesce(a.cashier_flag,'N') <> 'Y' ");
        }

        if (NACT) {
            sqa.addClause(" a.status = 'REALISASI'");
            sqa.addClause(" a.act_flag = 'Y'");
            sqa.addClause(" a.eff_flag = 'Y'");
            sqa.addClause(" a.validasi_f = 'Y'");
            sqa.addClause("coalesce(a.cashier_flag,'N') <> 'Y' ");
        }

        if (CASHBACK) {
            sqa.addClause(" a.status = 'REALISASI'");
            sqa.addClause(" a.act_flag = 'Y'");
            sqa.addClause(" a.eff_flag = 'Y'");
            sqa.addClause(" a.validasi_f = 'Y'");
            sqa.addClause(" a.cashier_flag = 'Y'");
        }

        if (PengajuanFrom != null) {
            sqa.addClause("date_trunc('day',a.request_date) >= ?");
            sqa.addPar(PengajuanFrom);
        }

        if (PengajuanTo != null) {
            sqa.addClause("date_trunc('day',a.request_date) <= ?");
            sqa.addPar(PengajuanTo);
        }

        if (RealisasiFrom != null) {
            sqa.addClause("date_trunc('day',a.request_date) >= ?");
            sqa.addPar(RealisasiFrom);
        }

        if (RealisasiTo != null) {
            sqa.addClause("date_trunc('day',a.request_date) <= ?");
            sqa.addPar(RealisasiFrom);
        }

        if (ValidasiFrom != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) >= ?");
            sqa.addPar(ValidasiFrom);
        }

        if (ValidasiTo != null) {
            sqa.addClause("date_trunc('day',a.cashier_date) <= ?");
            sqa.addPar(ValidasiTo   );
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stBranch != null) {
            sqa.addClause("a.region_id = ?");
            sqa.addPar(stBranch);
        }

        final String sql = sqa.getSQL() + " order by a.req_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARRequestFee.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public DTOList FEE_REQUEST() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_request_fee a ");
        sqa.addClause(" ((a.status = 'REQUEST' and a.act_flag = 'Y' and a.eff_flag = 'Y') or "
                + " (a.status = 'APPROVED' and a.act_flag = 'Y' and coalesce(a.eff_flag,'N') <> 'Y'))");

        sqa.addClause(" a.deleted is null");

        if (PeriodDateFrom != null) {
            sqa.addClause("date_trunc('day',a.request_date) >= ?");
            sqa.addPar(PeriodDateFrom);
        }

        if (PeriodDateTo != null) {
            sqa.addClause("date_trunc('day',a.request_date) <= ?");
            sqa.addPar(PeriodDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        final String sql = sqa.getSQL() + " order by a.req_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARRequestFee.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXCEL_DEPOSITOFIX() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||a.nodefo as bilyet,(case when a.kodedepo = '1' then a.hari||' '||'hari' else a.bulan||' '||'bulan' end) as waktu,"
                + "a.tglawal,a.tglakhir,a.tgldepo as tgldebet,a.norekdep,a.nominal,a.bunga,a.norekening,"
                + "a.bukti_b as buktib,a.account_bank,c.description as nama_bank,a.account_depo,d.description as nama_depo,a.koda, "
                + "(select string_agg(b.bukti_d,'|')) as buktid,(select string_agg(b.angka::text,'|')) as bunga ");

        sqa.addQuery(" from ar_inv_perpanjangan a "
                + "left join ( select a.* from ar_inv_bunga a "
                + "where a.effective_flag = 'Y' and a.delete_flag is null "
                + "and date_trunc('day',a.tglbunga) >= '" + DateUtil.getYear(dtTglAwalFrom) + "-" + DateUtil.getMonth2Digit(dtTglAwalFrom) + "-01' "
                + "and date_trunc('day',a.tglbunga) <= '" + dtTglAwalFrom + "' ) "
                + " b on b.nodefo||b.norekdep||coalesce(b.bukti_b,'') = a.nodefo||a.norekdep||coalesce(a.bukti_b,'') "
                + "left join gl_accounts c on c.account_id = a.kdbank "
                + "left join gl_accounts d on d.account_id = a.norekdep ");

//        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");
        sqa.addClause(" date_trunc('day',a.tgldepo) <= '" + dtTglAwalFrom + "'");
        sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > '" + dtTglAwalFrom + "')");

        if (stBranch != null) {
            sqa.addClause("a.koda = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = '" + stReceiptClassID + "'");
            //sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = '" + stCompTypeID + "'");
            //sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = '" + stStatusName + "'");
            //sqa.addPar(stStatusName);
        }

        String sql = sqa.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 order by 1 ";

        String nama_file = null;
        nama_file = "deposito_" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    public DTOList DEPOSITOFIX() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ar_inv_perpanjangan a ");

        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");
        sqa.addClause(" date_trunc('day',a.tgldepo) <= '" + dtTglAwalFrom + "'");
        sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > '" + dtTglAwalFrom + "')");

        if (stBranch != null) {
            sqa.addClause("a.koda = ?");
            sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = ?");
            sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = ?");
            sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = ?");
            sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = ?");
            sqa.addPar(stStatusName);
        }

        final String sql = sqa.getSQL() + " order by tglawal,bukti_b ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvestmentDepositoView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXCEL_BUNGAFIX() throws Exception {
        int tahunCodeLast;
        int bulanCode = DateUtil.getMonthDigit(BungaDateFrom);

        String policyDateStart = null;
        String policyDateEnd = null;
        Date dateStart = null;
        Date dateEnd = null;
        String bulan = null;
        String tahun = null;

        if (bulanCode == 1) {
            tahunCodeLast = Integer.parseInt(DateUtil.getYear(BungaDateFrom)) - 1;

            bulan = "12";
            tahun = Integer.toString(tahunCodeLast);

            policyDateStart = tahunCodeLast + "-12-01 00:00:00";
            policyDateEnd = tahunCodeLast + "-12-31 00:00:00";

            dateStart = DateUtil.getDate("01/12/" + tahunCodeLast);
            dateEnd = DateUtil.getDate("31/12/" + tahunCodeLast);
        } else if (bulanCode > 1) {
            int bulanCodeNow = bulanCode - 1;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(BungaDateFrom));
            dateStart = pd.getDtStartDate();
            dateEnd = pd.getDtEndDate();

            bulan = DateUtil.getMonth2Digit(dateStart);
            tahun = DateUtil.getYear(dateStart);

            policyDateStart = dateStart.toString();
            policyDateEnd = dateEnd.toString();
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||a.nodefo as bilyet,(case when a.kodedepo = '1' then 'hari' else 'bulan' end) as waktu,"
                + "a.tglawal,a.tglakhir,a.tgldepo as tgldebet,a.norekdep,a.nominal,a.bunga,"
                + "a.bukti_b as buktib,a.account_bank,a.nama_bank,a.account_depo,a.nama_depo,a.koda, "
                + "(select string_agg(b.bukti_d,'|')) as buktid,(select string_agg(b.angka::text,'|')) as bunga ");

        sqa.addQuery(" from ar_inv_perpanjangan a "
                + "left join ( select a.* from ar_inv_bunga a "
                + "where a.effective_flag = 'Y' and a.delete_flag is null "
                + "and date_trunc('day',a.tglbunga) >= '" + BungaDateFrom + "' "
                + "and date_trunc('day',a.tglbunga) <= '" + BungaDateTo + "' ) "
                + " b on b.nodefo||b.norekdep||coalesce(b.bukti_b,'') = a.nodefo||a.norekdep||coalesce(a.bukti_b,'') ");

        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y'");
        sqa.addClause(" a.deleted is null");
        sqa.addClause(" date_trunc('day',a.tgldepo) <= '" + policyDateEnd + "'");
        sqa.addClause(" (a.tglcair is null or date_trunc('day',a.tglcair) > '" + policyDateEnd + "')");

        if (stBranch != null) {
            sqa.addClause("a.koda = '" + stBranch + "'");
            //sqa.addPar(stBranch);
        }

        if (stReceiptClassID != null) {
            sqa.addClause("a.rc_id = '" + stReceiptClassID + "'");
            //sqa.addPar(stReceiptClassID);
        }

        if (stCompTypeID != null) {
            sqa.addClause("a.comp_type = '" + stCompTypeID + "'");
            //sqa.addPar(stCompTypeID);
        }

        if (stEntityID != null) {
            sqa.addClause("a.kdbank = '" + stEntityID + "'");
            //sqa.addPar(stEntityID);
        }

        if (stStatusID != null) {
            sqa.addClause("a.status = '" + stStatusName + "'");
            //sqa.addPar(stStatusName);
        }

        String sql = sqa.getSQL() + " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14 order by 1 ";

        String nama_file = null;
        nama_file = "deposito_" + System.currentTimeMillis() + ".csv";

        SQLUtil S = new SQLUtil();

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

    /**
     * @return the PengajuanFrom
     */
    public Date getPengajuanFrom() {
        return PengajuanFrom;
    }

    /**
     * @param PengajuanFrom the PengajuanFrom to set
     */
    public void setPengajuanFrom(Date PengajuanFrom) {
        this.PengajuanFrom = PengajuanFrom;
    }

    /**
     * @return the PengajuanTo
     */
    public Date getPengajuanTo() {
        return PengajuanTo;
    }

    /**
     * @param PengajuanTo the PengajuanTo to set
     */
    public void setPengajuanTo(Date PengajuanTo) {
        this.PengajuanTo = PengajuanTo;
    }

    /**
     * @return the RealisasiFrom
     */
    public Date getRealisasiFrom() {
        return RealisasiFrom;
    }

    /**
     * @param RealisasiFrom the RealisasiFrom to set
     */
    public void setRealisasiFrom(Date RealisasiFrom) {
        this.RealisasiFrom = RealisasiFrom;
    }

    /**
     * @return the RealisasiTo
     */
    public Date getRealisasiTo() {
        return RealisasiTo;
    }

    /**
     * @param RealisasiTo the RealisasiTo to set
     */
    public void setRealisasiTo(Date RealisasiTo) {
        this.RealisasiTo = RealisasiTo;
    }

    /**
     * @return the ValidasiFrom
     */
    public Date getValidasiFrom() {
        return ValidasiFrom;
    }

    /**
     * @param ValidasiFrom the ValidasiFrom to set
     */
    public void setValidasiFrom(Date ValidasiFrom) {
        this.ValidasiFrom = ValidasiFrom;
    }

    /**
     * @return the ValidasiTo
     */
    public Date getValidasiTo() {
        return ValidasiTo;
    }

    /**
     * @param ValidasiTo the ValidasiTo to set
     */
    public void setValidasiTo(Date ValidasiTo) {
        this.ValidasiTo = ValidasiTo;
    }
}
